package com.mercurio.lms.prestcontasciaaerea.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.CiaFilialMercurioService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta;
import com.mercurio.lms.prestcontasciaaerea.model.service.CarregarPrestacaoContaDecendialService;
import com.mercurio.lms.prestcontasciaaerea.model.service.FaturamentoCiaAereaService;
import com.mercurio.lms.prestcontasciaaerea.model.service.PrestacaoContaService;
import com.mercurio.lms.prestcontasciaaerea.model.service.ValorPrestacaoContaService;
import com.mercurio.lms.tributos.model.CalcularPisCofinsCsllIrInss;
import com.mercurio.lms.tributos.model.service.CalcularIssService;
import com.mercurio.lms.tributos.model.service.CalcularPisCofinsCsllIrInssService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 *
 * @spring.bean id="lms.prestcontasciaaerea.emitirPrestacaoContasService"
 * @spring.property name="reportName" value="com/mercurio/lms/prestcontasciaaerea/report/emitirPrestacaoContasDecendial.jasper"
 * @author Robson Edemar Gehl
 */
public class EmitirPrestacaoContasService extends ReportServiceSupport {

	
	private PrestacaoContaService prestacaoContaService;
	private ValorPrestacaoContaService valorPrestacaoContaService;
	private CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService;
	private FilialService filialService;
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoPessoaService;
	private CiaFilialMercurioService ciaFilialMercurioService;
	private CarregarPrestacaoContaDecendialService carregarPrestacaoContaDecendialService;
	private ParametroGeralService parametroGeralService;
	private ConfiguracoesFacade configuracoesFacade;
	private CalcularIssService calcularIssService;
	private FaturamentoCiaAereaService faturamentoCiaAereaService;

	public FaturamentoCiaAereaService getFaturamentoCiaAereaService() {
		return faturamentoCiaAereaService;
	}

	public void setFaturamentoCiaAereaService(
			FaturamentoCiaAereaService faturamentoCiaAereaService) {
		this.faturamentoCiaAereaService = faturamentoCiaAereaService;
	}

	/**
	 * Busca os dados que são mostrados no Cabeçalho e no rodapé do Relatório principal 
	 * @param pc
	 * @return
	 */
	public Map findCabecalhoRodape(PrestacaoConta pc){

		/*
		 *   Em algum momento, obscuro na construção deste método, o CGLib é quebrado, returnando null  em ...getFilial().
		 *   Por isto é feito este teste -- o de comparar o getter de filial com null --, para fazer o find da CiaFilialMercurio 
		 * e pegar a Filial e Empresa
		 */
		if (pc.getCiaFilialMercurio().getFilial() == null){
			CiaFilialMercurio ciaFilialMercurio = getCiaFilialMercurioService().findById(pc.getCiaFilialMercurio().getIdCiaFilialMercurio());
			pc.setCiaFilialMercurio(ciaFilialMercurio);
		}

		Filial filial = pc.getCiaFilialMercurio().getFilial();
		Empresa empresa = pc.getCiaFilialMercurio().getEmpresa();
		
		EnderecoPessoa enderecoPessoa = getEnderecoPessoaService().findEnderecoPessoaPadrao(filial.getIdFilial());

		Map map = new HashMap();
		map.put("ID_PRESTACAO_CONTA", pc.getIdPrestacaoConta());
		map.put("NUMERO",(pc.getNrPrestacaoConta() != null) ? pc.getNrPrestacaoConta().toString() : "");
		map.put("COMPANHIA", empresa.getPessoa().getNmPessoa() );
		map.put("FILIAL", 
				new StringBuffer()
					.append(filial.getSgFilial()).append("QX - ")
					.append(filial.getPessoa().getNmFantasia())
					.toString()
				);

		map.put("ENDERECO",	this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa()) );
		map.put("BAIRRO", enderecoPessoa.getDsBairro() );
		map.put("CEP", enderecoPessoa.getNrCep() );
		map.put("CIDADE", enderecoPessoa.getMunicipio().getNmMunicipio() );
		map.put("UF", enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		map.put("DAC",  (filial.getEmpresa().getNrDac() != null) ? filial.getEmpresa().getNrDac().toString() : "");
		map.put("PERIODO_VENDAS_INICIAL", JTFormatUtils.format(pc.getDtInicial()));
		map.put("PERIODO_VENDAS_FINAL", JTFormatUtils.format(pc.getDtFinal()));
		map.put("VENCIMENTO", JTFormatUtils.format(pc.getDtVencimento()));
		map.put("QTD_AWB", pc.getQtAwb() );
		map.put("SAP", "" );
		map.put("MOEDA", filial.getMoeda().getSiglaSimbolo() );

		String strIdEmpresa = empresa.getIdEmpresa().toString();
		
		boolean isTAM = getParametroGeralService().validateDsConteudoByNmParametro( strIdEmpresa , "ID_TAM");
		
		if (pc.getCiaFilialMercurio().getDsIdentificadorCiaAerea()!=null) {
			map.put("DS_IDENTIFICADOR", pc.getCiaFilialMercurio().getDsIdentificadorCiaAerea()+":" );
			map.put("IATA", pc.getCiaFilialMercurio().getVlIdentificadorCiaAerea() );
		}

		if ( getParametroGeralService().validateDsConteudoByNmParametro( strIdEmpresa , "ID_VARIG") ){
			
			map.put("SAP", "1" );
			map.put("CODIGO", Long.valueOf(1) );
			
		}
		
		//dados para o rodapé do report

		BigDecimal zero = new BigDecimal(0);

		BigDecimal pesoTotal = getValorPrestacaoContaService().findTotalByTpValor(pc.getIdPrestacaoConta(), "PE");

		map.put("PESO_TOTAL", pesoTotal);	
		
		BigDecimal valorComissao = zero;

		if (isTAM){

			valorComissao = getValorPrestacaoContaService().findTotalByTpValor(pc.getIdPrestacaoConta(), "FR");
			
			BigDecimal pcComissaoAereo = new BigDecimal(1).setScale(2);
			pcComissaoAereo = (BigDecimal)configuracoesFacade.getValorParametro("PC_COMISSAO_AEREO");				
			
			valorComissao = valorComissao.setScale(2).multiply(pcComissaoAereo.setScale(2)).setScale(2).divide(new BigDecimal(100).setScale(2), BigDecimal.ROUND_HALF_DOWN).setScale(2);

		}
		
		map.put("VALOR_COMISSAO", valorComissao);

		return map;
	}
	
	/*
	 * Map correspondente a uma linha (registro/tupla) do Intervalo de AWBs
	 */
	private Map createRowIntervaloAwb(String form, Integer qtd, Integer de, Integer ckInicial, Integer ate, Integer ckFinal){
		Map map = new HashMap();
		map.put("FORM", form);
		map.put("QTD", qtd );
		map.put("DE", de );
		map.put("CK_INICIAL", ckInicial );
		map.put("ATE", ate );
		map.put("CK_FINAL", ckFinal );
		return map;
	}
	
	/**
	 * Busca os intervalos de Awbs da Prestação de Conta.<BR>
	 * @param idPrestacaoConta
	 * @return
	 */
	public JRDataSource findIntervalosAwb(Long idPrestacaoConta){
		StringBuffer sql = new StringBuffer()
			.append("select ((intervalo.nr_intervalo_final - intervalo.nr_intervalo_inicial) + 1) as QTD, ")
			.append("intervalo.nr_intervalo_inicial as DE, intervalo.dv_intervalo_inicial as CK_INICIAL, ")
			.append("intervalo.nr_intervalo_final as ATE, intervalo.dv_intervalo_final as CK_FINAL ")
			.append("from intervalo_awb intervalo ")
			.append("where intervalo.id_prestacao_conta = ? ")
			.append("order by intervalo.nr_intervalo_inicial ");
		
		List list = getJdbcTemplate().query(sql.toString(), new Object[]{idPrestacaoConta}, new RowMapper(){
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return createRowIntervaloAwb("2042", 
						Integer.valueOf(rs.getInt(1)), 
						Integer.valueOf(rs.getInt(2)),
						Integer.valueOf(rs.getInt(3)), 
						Integer.valueOf(rs.getInt(4)), 
						Integer.valueOf(rs.getInt(5)));
			}
		});

		/*
		 * Quantidade mínima de linhas que devem ser impressas no relatório 
		 */
		int qtdeMinimaLinhas = 12; 
		
		/*  No relatório é necessário a impressão de 6 linhas, com duas colunas.
		 *  Quando o resultado da consulta for menor que 'qtdeMinimaLinhas', o loop abaixo preenche as linhas restantes, para que o report 
		 * seja montado corretamente.
		 */
		for (int listSize = list.size(); listSize < qtdeMinimaLinhas; listSize++){
			list.add(createRowIntervaloAwb(null, null, null, null, null, null));//linhas vazias
		}
		
		return createReportDataObject( new JRMapCollectionDataSource(list) , new HashMap() ).getDataSource();
	}
	
	/**
	 * Busca os Demonstrativo de ICMS da Prestação de Conta.<BR> 
	 * @param idPrestacaoConta
	 * @return
	 */
	public JRDataSource findDemostrativoICMS(Long idPrestacaoConta){
		StringBuffer sql = new StringBuffer()
			.append("select ip.pc_aliquota as ALIQUOTA, ip.vl_frete as FRETE, ip.vl_taxa as TAXA, ip.vl_advalorem  as ADVALOREM, ip.vl_icms ICMS ")
			.append("from icms_prestacao ip ")
			.append("where ip.id_prestacao_conta = ? ")
			.append("order by ip.pc_aliquota ");
		
		List list = getJdbcTemplate().query(sql.toString(), new Object[]{idPrestacaoConta} , new RowMapper(){

			public Object mapRow(ResultSet rs, int count) throws SQLException {
				Map map = new HashMap();
				map.put("ALIQUOTA",rs.getBigDecimal(1));
				map.put("FRETE",rs.getBigDecimal(2));
				map.put("TAXA",rs.getBigDecimal(3));
				map.put("ADVALOREM",rs.getBigDecimal(4));
				map.put("ICMS",rs.getBigDecimal(5));
				return map;
			}
			
		});

		if (!list.isEmpty()){ 
		
			/*
			 *   Estes códigos são utilizados no sub-report de Demonstrativo do ICMS, no field CODIGO.
			 *   Não é algo que está na base de dados, por isto é realizado um loop e, de maneira ordenada, 
			 * inserindo os códigos em cada linha (Map)
			 */
			String codigos[] = {"A","B","C","D","E"};

			Iterator iter = list.iterator();
			int count = 0; //contador para linhas
			//Percorre dados, inserindo um código para cada linha
			while(iter.hasNext()){
				Map map = (Map) iter.next();
				map.put("CODIGO",codigos[count]);
				count++;
			}

			//Preenche as linhas que faltaram, com os codigos.
			for (int i = count; i < codigos.length; i++){
				Map map = new HashMap();
				BigDecimal zero = new BigDecimal(0);
				map.put("CODIGO",codigos[i]);
				map.put("ALIQUOTA", zero);
				map.put("FRETE", zero);
				map.put("TAXA", zero);
				map.put("ADVALOREM", zero);
				map.put("ICMS", zero);
				list.add(map);
			}
		}

		return createReportDataObject( new JRMapCollectionDataSource(list) , new HashMap() ).getDataSource();
	}

	/**
	 * Busca os os Valores da Prestação de Conta.<BR>
	 * @param idPrestacaoConta
	 * @return
	 */
	public List findDemonstrativoVendas(Long idPrestacaoConta){
		StringBuffer sql = new StringBuffer()
			.append("select valor.vl_tipo_prestacao_conta VL_TP_PRESTACAO_CONTA, valor.tp_valor TP_VALOR, ")
			.append("valor.tp_forma_pagamento TP_FORMA_PAGAMENTO ")
			.append("from valor_prestacao_conta valor where valor.id_prestacao_conta = ?");
	
		List list = getJdbcTemplate().query(sql.toString(), new Object[]{idPrestacaoConta} , new RowMapper(){
			public Object mapRow(ResultSet rs, int count) throws SQLException {
				Map map = new HashMap();
				map.put("valor",rs.getBigDecimal(1));
				map.put("tpValor",rs.getString(2));
				map.put("tpFormaPagamento",rs.getString(3));
				return map;
			}
		});
		return list;
	}
	
	/*
	 * Map correspondente a uma linha (registro/tupla) do Demonstrativo de Vendas
	 */
	private Map initializeMapTamGol(){
		Map map = new HashMap();
		map.put("tarifaCtaCorrente", null);
		map.put("tarifaFreteCobrar", null);
		map.put("tarifaPagos", null);
		map.put("totalTarifa", null);
		return map;
	}
	
	/**
	 * Verifica se é um tipo de valor válido para o somatorio de valores da TAM e GOL. 
	 * @param tpValor
	 * @return
	 */
	private boolean isValidTpValorTamGol(String tpValor){
		return (tpValor.equals("TC") || tpValor.equals("TD") || tpValor.equals("FR"));
	}
	
	/**
	 * 
	 * @param idPrestacaoConta
	 * @param idEmpresa
	 * @return
	 */
	public JRDataSource findDemonstrativoVendasTamGol(Long idPrestacaoConta, Long idEmpresa){

		boolean isGol = getParametroGeralService().validateDsConteudoByNmParametro(idEmpresa.toString(), "ID_GOL");
		boolean isTam = getParametroGeralService().validateDsConteudoByNmParametro(idEmpresa.toString(), "ID_TAM");
		
		//Apenas faz a busca quando for Gol ou Tam, caso contrário retorna null.
		if (!isGol && !isTam){
			JRDataSource jrEpt = new JREmptyDataSource();
			return  jrEpt;
		}
		
		//Inicializa linha para Tam e Gol (dados que serão mostrados no relatório)
		Map dvTam = initializeMapTamGol();
		
		//Busca os valores da prestacao de conta
		List valores = findDemonstrativoVendas(idPrestacaoConta);
		Iterator iter = valores.iterator();
		BigDecimal zero = new BigDecimal(0);

		/*
		 * Itera os valores, somando quando os tipos de valores forem TC, TD ou FR.
		 * O somatório é realizado para três formas de pagamento: Frete a Cobrar, Cta Corrente e Pagos.
		 * Também é realizado um somatório total.
		 */
		BigDecimal tarifaFreteCobrar = zero;
		BigDecimal tarifaCtaCorrente = zero;
		BigDecimal tarifaPagos = zero;
		BigDecimal totalTarifa = zero;
		while(iter.hasNext()){
			Map map = (Map) iter.next();
			if ( isValidTpValorTamGol((String) map.get("tpValor")) ){
				
				BigDecimal valor = (BigDecimal) map.get("valor");
				String tpFormaPagamento = (String) map.get("tpFormaPagamento");
				
				if (tpFormaPagamento.equals("AP")){
					tarifaFreteCobrar = tarifaFreteCobrar.add(valor);
				
				//Cta Corrente apenas para Tam
				}else if (tpFormaPagamento.equals("CC") && isTam){
					tarifaCtaCorrente = tarifaCtaCorrente.add(valor);

				}else if (tpFormaPagamento.equals("PG")){
					
					//No caso da GOL, somar em tarifaCtaCorrente
					if (isGol){
						tarifaCtaCorrente = tarifaCtaCorrente.add(valor);	
					}else{
						//Para TAM
						tarifaPagos = tarifaPagos.add(valor);
					}
				}

				//somatório do valor total da tarifa
				totalTarifa = totalTarifa.add(valor);
			}
		}
		
		//Insere os valores das tarifas no Map que será retornado para o relatório
		dvTam.put("tarifaFreteCobrar",tarifaFreteCobrar);
		dvTam.put("tarifaCtaCorrente",tarifaCtaCorrente);
		dvTam.put("tarifaPagos",tarifaPagos);
		dvTam.put("totalTarifa",totalTarifa);
		
		List list = new ArrayList(1);
		list.add(dvTam);
		return createReportDataObject( new JRMapCollectionDataSource(list) , new HashMap() ).getDataSource();
	}

	/**
	 * Verifica se o tipo de valor da prestação de conta é válido.<BR>
	 * Tipos válidos: {"qa", "fr", "tc", "td", "ad", "fd"}
	 * @param tpValor
	 * @return
	 */
	private boolean isValidTpValorPrestacaoContaVarig(String tpValor){
		String[] linhas = {"qa", "fr", "tc", "td", "ad", "fd"};
		for (int i = 0; i < linhas.length; i++){
			if (linhas[i].equalsIgnoreCase(tpValor)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Busca Demonstrativo de Vendas para Varig.<BR>
	 * @param idPrestacaoConta
	 * @param idEmpresa
	 * @return
	 */
	public JRDataSource findDemonstrativoVendasVarig(Long idPrestacaoConta){

		List list = findDemonstrativoVendas(idPrestacaoConta);

		/* Para cada linha, cria um Map com a matriz dos totais.
		 * Inicializa a matriz com os valores zerados, utilizando java.math.BigDecimal.
		 * Com o Tipo da Forma de Pagamento é feito uma validação para preencher a matriz.
		 */
		Map matriz = initializeMapPrestacaoContas(); //Matriz de valores para relatorio
		Iterator iter = list.iterator();
		BigDecimal valorTotal = new BigDecimal(0);
		while(iter.hasNext()){

			Map map = (Map) iter.next();
			String tpValor = (String) map.get("tpValor");
			//Valida o tipo de valor e realiza a identificação das coluna/linhas
			if (isValidTpValorPrestacaoContaVarig(tpValor)){
				
				BigDecimal valor = (BigDecimal) map.get("valor");
				String tpFormaPagamento = (String) map.get("tpFormaPagamento");
				
				// Somatório para o total dos valores
				// Não soma as quantidades
				if (!tpValor.equals("QA")) {
					valorTotal = valorTotal.add(valor);
				}
				
				if (tpFormaPagamento.equals("AP")){
					setValor(matriz, valor, tpValor, "FreteCobrar");
				}else if (tpFormaPagamento.equals("CC")){
					setValor(matriz, valor, tpValor, "CtaCorrente");
				}else if (tpFormaPagamento.equals("PG")){
					setValor(matriz, valor, tpValor, "Pagos");
				}
			}
		}
		matriz.put("valorTotal", valorTotal);
		//Sobrepoe a coleção para apenas um Map, que é a matriz que será mostrada no relatório.
		list = new ArrayList(1);
		list.add(matriz);
		return createReportDataObject( new JRMapCollectionDataSource(list) , new HashMap() ).getDataSource();
	}

	/**
	 * Inicializa a 'matriz' de valores da Prestação de Contas, utilizada no sub-report Demonstrativo de Vendas.<BR>
	 * Linhas: {"qa", "fr", "tc", "td", "ad", "fd"} <BR>
	 * Colunas: {"FreteCobrar", "CtaCorrente", "Pagos"} <BR>
	 * O nome dos campos é uma concatenação entre linha e coluna, por exemplo: qaFreteCobrar, qaCtaCorrente, frPagos, etc.<BR>
	 * @return map de valores inicializados com Zero (java.math.BigDecimal)
	 */
	private Map initializeMapPrestacaoContas(){
		String[] linhas = {"qa", "fr", "tc", "td", "ad", "fd"};
		String[] colunas = {"FreteCobrar", "CtaCorrente", "Pagos"};

		Map map = new HashMap();
		BigDecimal zero = new BigDecimal(0);
		for (int i = 0; i < linhas.length; i++){
			for(int j = 0; j < colunas.length; j++){
				map.put(linhas[i] + colunas[j], zero); //a cocatenação de linha+coluna deverá ser utilizada no relatório (textfield)
			}
		}
		return map;
	}
	
	/**
	 * Atribui o valor na matriz, conforme tipo de valor e coluna informados.<BR>
	 * @param map matriz inicializada que receberá o novo valor
	 * @param valor valor da Prestação de Conta
	 * @param tpValor Tipo de Valor
	 * @param coluna Coluna que deve ser preenchida
	 */
	private void setValor(Map map, BigDecimal valor, String tpValor, String coluna){
		map.put(tpValor.toLowerCase() + coluna, valor);
	}


	/**
	 * Busca o total de frete.<BR>
	 * Busca o Valor Prestação de Conta, onde o tipo de valor é FR.<BR> 
	 * @param idPrestacaoConta
	 * @return
	 */
	public BigDecimal findTotalFrete(Long idPrestacaoConta){
		return getValorPrestacaoContaService().findTotalByTpValor(idPrestacaoConta, "FR");
	}

	public CalcularPisCofinsCsllIrInss findCalcularPisCofinsCsllIrInss(Long idPrestacaoConta, 
			Long idEmpresa, BigDecimal totalFrete, Long idServicoTributo){
		
		List calculos = getCalcularPisCofinsCsllIrInssService().calcularPisCofinsCsllIrInssPessoaJudirica(
				idEmpresa, "I", "OU", null, 
				idServicoTributo,
				getPrestacaoContaService().findById(idPrestacaoConta).getDtEmissao(),
				totalFrete
			);
		
		if (!calculos.isEmpty()){
			
			Iterator iter = calculos.iterator();
			while(iter.hasNext()){
				CalcularPisCofinsCsllIrInss pisCofinsCsllIrInss = (CalcularPisCofinsCsllIrInss) iter.next();
				if (pisCofinsCsllIrInss.getTpImpostoCalculado().equals("IR")){ 
					return pisCofinsCsllIrInss;
				}
			}
		}
		return null;
	}

	/**
	 * Calcula a TLL de Venda.<BR>
	 * O calculo é o somatório do valor da prestação de conta onde o tipo do falor é FR e PG.<BR>
	 * @param idPrestacaoConta
	 * @return  
	 */
	public BigDecimal findTtlVendaFPago(Long idPrestacaoConta){
		return getValorPrestacaoContaService().findTotalByTpValor(idPrestacaoConta, "FR", "PG");
	}
	
	/*
	 * Representa os dados do Demonstrativo de Contas, a serem enviados para o report.
	 */
	private void initializeMapDemonstrativoPrestacaoContas(Map map){
		BigDecimal zero = new BigDecimal(0);
		map.put("IRRF", zero);
		map.put("VALOR_PAGAR_AGENTE", zero);
		map.put("TOTAL_FRETE", zero);
		map.put("COMISSAO_SOBRE_FRETE", zero);
		map.put("NOTAS_DEBITO", zero);
		map.put("DESCONTO_MERCADO", zero);
		map.put("TAXAS_DEVIDAS_AGENTE", zero);
		map.put("NOTAS_CREDITO", zero);
		map.put("FRETE_NACIONAL", zero);
		map.put("TTL_VENDA_F_PAGO", zero);
		map.put("VALOR_COMISSAO", zero);
		map.put("ISS", zero);
		map.put("TOTAL_ENTRADAS", zero);
		map.put("TOTAL_SAIDAS", zero);
		map.put("IMPORTANCIA_LIQUIDA", zero);
	}
	
	/**
	 * Busca os Demonstrativos da Prestação de Conta.<BR> 
	 * @param idPrestacaoConta
	 * @param idEmpresa
	 * @param idFilial
	 * @return
	 */
	public JRDataSource findDemonstrativoPrestacaoContas(Long idPrestacaoConta, Long idEmpresa, Long idFilial){

		Map map = new HashMap();
		initializeMapDemonstrativoPrestacaoContas(map);
		BigDecimal zero = new BigDecimal(0);
		Long idServicoTributo = null;

		idServicoTributo = Long.valueOf(((BigDecimal)configuracoesFacade.getValorParametro("ID_AGENCIAMENTO_CARGA_AEREA")).longValue());		

		BigDecimal totalFrete = findTotalFrete(idPrestacaoConta);

			
		BigDecimal pcComissaoAereo = (BigDecimal)configuracoesFacade.getValorParametro("PC_COMISSAO_AEREO");		
			
		BigDecimal comissaoSobreFrete = totalFrete.setScale(2).multiply(pcComissaoAereo.setScale(2)).divide(new BigDecimal(100).setScale(2), BigDecimal.ROUND_HALF_DOWN).setScale(2, BigDecimal.ROUND_HALF_DOWN);

		// VARIG
		if (getParametroGeralService().validateDsConteudoByNmParametro(idEmpresa.toString(), "ID_VARIG")){
			//alteracao regra 2.6 - data: 01.2009 calcular com base na comissão do faturamento cia aerea.
			pcComissaoAereo = getFaturamentoCiaAereaService().findPcComissaoCiaAerea(idEmpresa, idFilial);
			comissaoSobreFrete = totalFrete.setScale(2).multiply(pcComissaoAereo.setScale(2)).divide(new BigDecimal(100).setScale(2), BigDecimal.ROUND_HALF_DOWN).setScale(2, BigDecimal.ROUND_HALF_DOWN);

			BigDecimal irrf = findCalcularPisCofinsCsllIrInss(idPrestacaoConta, idEmpresa, comissaoSobreFrete.setScale(2), idServicoTributo).getVlImposto().setScale(2);
			BigDecimal valorPagarAgente = totalFrete.setScale(2).add(irrf.setScale(2)).subtract(comissaoSobreFrete.setScale(2)).setScale(2);

			map.put("IRRF", irrf);
			map.put("VALOR_PAGAR_AGENTE", valorPagarAgente);
			map.put("TOTAL_FRETE", totalFrete);
			map.put("COMISSAO_SOBRE_FRETE", comissaoSobreFrete);
			
		// TAM E GOL
		}else if (getParametroGeralService().validateDsConteudoByNmParametro(idEmpresa.toString(), "ID_GOL") 
				|| getParametroGeralService().validateDsConteudoByNmParametro(idEmpresa.toString(), "ID_TAM")){
			
			BigDecimal ttlVenda = findTtlVendaFPago(idPrestacaoConta);

			BigDecimal valorComissao6Pc = zero;
			BigDecimal iss = zero;
			BigDecimal aliquotaIss = zero;

			if (!(getPessoaService().validateNrIdentificacaoStartsWith(idFilial, "955917230011587")
					&& getParametroGeralService().validateDsConteudoByNmParametro(idEmpresa.toString(), "ID_GOL"))){

				valorComissao6Pc = comissaoSobreFrete;
				
				EnderecoPessoa enderecoPessoa = getEnderecoPessoaService().findEnderecoPessoaPadrao(idFilial);

				Long idMunicipioFilial = enderecoPessoa.getMunicipio().getIdMunicipio(); 		
				
				Map mIss = calcularIssService.calcularIssCiaAerea(idMunicipioFilial, idMunicipioFilial, null, idServicoTributo, JTDateTimeUtils.getDataAtual(), valorComissao6Pc);
				
				iss = (BigDecimal)mIss.get("vlIss");
				aliquotaIss = (BigDecimal)mIss.get("pcAliquotaIss");
			
			}

			BigDecimal totalEntradas = ttlVenda.add(iss);
			BigDecimal totalSaidas = valorComissao6Pc;
			BigDecimal importanciaLiquida = totalEntradas.subtract(totalSaidas);
			
			map.put("FRETE_NACIONAL", totalFrete);
			map.put("VALOR_COMISSAO", valorComissao6Pc);
			map.put("PC_COMISSAO_AEREO", pcComissaoAereo);
			map.put("TTL_VENDA_F_PAGO", ttlVenda);
			map.put("ISS", iss);
			map.put("ALIQUOTA_ISS", aliquotaIss);
			map.put("TOTAL_ENTRADAS", totalEntradas);
			map.put("TOTAL_SAIDAS", totalSaidas);
			map.put("IMPORTANCIA_LIQUIDA", importanciaLiquida);
		}
		
		List list = new ArrayList(1);
		list.add(map);
		return createReportDataObject( new JRMapCollectionDataSource(list) , new HashMap() ).getDataSource();
	}
	
	/**
	 * Busca AWBs cancelados.<BR>
	 * @param idPrestacaoConta
	 * @return
	 */
	public JRDataSource findAWBsCancelados(Long idPrestacaoConta, Long idEmpresa){
		StringBuffer sql = new StringBuffer()
			.append("select c.nr_awb_cancelado NUMERO from awb_cancelado c ")
			.append("where c.id_prestacao_conta = ? ")
			.append("order by c.nr_awb_cancelado ");

		List list = getJdbcTemplate().query(sql.toString(), new Object[]{idPrestacaoConta}, new RowMapper(){
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map map = new HashMap();
				map.put("NUMERO", new Double(rs.getDouble(1)) );
				return map;
			}
		});

		//Quantidade de registros que devem ser exibidos no relatório.
		int qtdeRegistrosExibidos = 30;

		//Se for GOL ou TAM, então o número de linhas para exibição é o dobro
		if (getParametroGeralService().validateDsConteudoByNmParametro(idEmpresa.toString(), "ID_GOL") 
			|| getParametroGeralService().validateDsConteudoByNmParametro(idEmpresa.toString(), "ID_TAM")){
			qtdeRegistrosExibidos = 60;
		}
		
		//Preenche a Collection com o numero de registros definido
		for (int i = list.size(); i < qtdeRegistrosExibidos; i++){
			Map map = new HashMap();
			map.put("NUMERO", null );//registro vazio -- apenas para visualização, no sub-report
			list.add(map);
		}

		return createReportDataObject( new JRMapCollectionDataSource(list) , new HashMap() ).getDataSource();
	}
	
	
	/**
	 *   Busca a Prestação de conta.<BR> 
	 *   NOTA: Este método não deveria existir. Deve ser transparente em PrestacaoContaService (melhoria), 
	 * onde o finder devolveria o objeto e não uma List.<BR>
	 * @param idFilial
	 * @param idEmpresa
	 * @param nrPrestacaoConta
	 * @param dtInicial
	 * @param dtFinal
	 * @return
	 */
	private PrestacaoConta findPrestacaoConta(Long idFilial, Long idEmpresa, Long nrPrestacaoConta, YearMonthDay dtInicial, YearMonthDay dtFinal){
		List list = getPrestacaoContaService().findPrestacaoContaByUnique(idEmpresa, idFilial, nrPrestacaoConta, dtInicial, dtFinal);
		if (list.isEmpty()){
			return null;
		}else{
			return (PrestacaoConta) list.get(0);
		}
		
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {		

		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/*
		 * Data inicial deve considerar o dia do periodo de venda como sendo o dia do mes/ano informado
		 */
		YearMonthDay dtInicial = tfm.getYearMonthDay("dtInicial");
		Integer diaPeriodoVendas = tfm.getInteger("diaPeriodoVendas");
		
		if (dtInicial != null && diaPeriodoVendas != null){
			dtInicial = JTDateTimeUtils.setDay(dtInicial,diaPeriodoVendas.intValue()); 
			tfm.put("dtInicial", dtInicial);
		}

		PrestacaoConta pc = findPrestacaoConta(tfm.getLong("filial.idFilial"),
											   tfm.getLong("empresa.idEmpresa"),
											   tfm.getLong("nrPrestacaoConta"),
											   tfm.getYearMonthDay("dtInicial"),
											   tfm.getYearMonthDay("dtFinal"));
		
		if (pc == null && tfm.getLong("nrPrestacaoConta") == null){
			//Carregar e buscar novamente para montar o relatório
			Long nrPrestacaoConta = getCarregarPrestacaoContaDecendialService().storePrestacaoContaDecencial(
					tfm.getLong("filial.idFilial"),
					tfm.getLong("empresa.idEmpresa"),
					tfm.getYearMonthDay("dtInicial"),
					tfm.getYearMonthDay("dtFinal")
					);
			
			
		
			
						
			/*
			 * Se ocorrer erro ao carregar prestao de conta decendial, o finder retornará null.
			 */
			pc = findPrestacaoConta(
					tfm.getLong("filial.idFilial"),tfm.getLong("empresa.idEmpresa"),
					nrPrestacaoConta,tfm.getYearMonthDay("dtInicial"),tfm.getYearMonthDay("dtFinal"));
		}
		
		/*
		 *   Quando nao carregar os dados corretamente, retorna JRReportDataObject com uma Collection vazia,
		 * para que o relatório seja aberto, em branco.
		 */
		if (pc == null){
			return createReportDataObject(
					new JRMapCollectionDataSource(Collections.EMPTY_LIST), 
					new HashMap()
				);
		}

		Map cabecalho = findCabecalhoRodape(pc);
		List list = new ArrayList(1);
		list.add(cabecalho);
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(list);

		JRReportDataObject jr = createReportDataObject(jrMap, parameters);

		Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("empresa", tfm.getLong("empresa.idEmpresa"));
        parametersReport.put("filial", tfm.getLong("filial.idFilial"));
        
        parametersReport.put("isGOL", 
        		Boolean.valueOf(getParametroGeralService().validateDsConteudoByNmParametro(tfm.getString("empresa.idEmpresa"), "ID_GOL")));

        parametersReport.put("isTAM", 
        		Boolean.valueOf(getParametroGeralService().validateDsConteudoByNmParametro(tfm.getString("empresa.idEmpresa"), "ID_TAM")));
        
        parametersReport.put("isVARIG", 
        		Boolean.valueOf(getParametroGeralService().validateDsConteudoByNmParametro(tfm.getString("empresa.idEmpresa"), "ID_VARIG")));
        
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tfm.getString("tpFormatoRelatorio"));
        
        jr.setParameters(parametersReport);
        
		return jr;
	
	}

	public PrestacaoContaService getPrestacaoContaService() {
		return prestacaoContaService;
	}

	public void setPrestacaoContaService(PrestacaoContaService prestacaoContaService) {
		this.prestacaoContaService = prestacaoContaService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ValorPrestacaoContaService getValorPrestacaoContaService() {
		return valorPrestacaoContaService;
	}

	public void setValorPrestacaoContaService(
			ValorPrestacaoContaService valorPrestacaoContaService) {
		this.valorPrestacaoContaService = valorPrestacaoContaService;
	}

	public CalcularPisCofinsCsllIrInssService getCalcularPisCofinsCsllIrInssService() {
		return calcularPisCofinsCsllIrInssService;
	}

	public void setCalcularPisCofinsCsllIrInssService(
			CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService) {
		this.calcularPisCofinsCsllIrInssService = calcularPisCofinsCsllIrInssService;
	}

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public CarregarPrestacaoContaDecendialService getCarregarPrestacaoContaDecendialService() {
		return carregarPrestacaoContaDecendialService;
	}

	public void setCarregarPrestacaoContaDecendialService(
			CarregarPrestacaoContaDecendialService carregarPrestacaoContaDecendialService) {
		this.carregarPrestacaoContaDecendialService = carregarPrestacaoContaDecendialService;
	}

	public CiaFilialMercurioService getCiaFilialMercurioService() {
		return ciaFilialMercurioService;
	}

	public void setCiaFilialMercurioService(
			CiaFilialMercurioService ciaFilialMercurioService) {
		this.ciaFilialMercurioService = ciaFilialMercurioService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setCalcularIssService(CalcularIssService calcularIssService) {
		this.calcularIssService = calcularIssService;
	}

	
	
}