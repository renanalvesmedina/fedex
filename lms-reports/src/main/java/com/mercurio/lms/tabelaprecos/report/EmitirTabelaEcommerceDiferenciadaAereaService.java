package com.mercurio.lms.tabelaprecos.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.report.EmitirTabelaTaxaCombustivelLandscapeService;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;

/**
 * 30.03.02.08A Tabela E-commerce Diferenciada Aerea
 * 
 * @spring.bean id="lms.tabelaprecos.report.emitirTabelaEcommerceDiferenciadaAereaService"
 * @spring.property name="reportName" value="/com/mercurio/lms/tabelaprecos/report/emitirTabelaEcommerceDiferenciadaAereo.vm"
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="280"
 * @spring.property name="crossTabBandWidths" value="396"
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class EmitirTabelaEcommerceDiferenciadaAereaService extends ReportServiceSupport { 

	/* Nome da Tebela temporaria */
	private static final String NOME_TABELA = "TMP_E_DIFERENCIADAS";
	
	private EmitirTabelaTaxaCombustivelLandscapeService emitirTabelaTaxaCombustivel;

	/* Para utilização de RecursosMensagens*/
    private ConfiguracoesFacade configuracoesFacade;
    private DomainValueService domainValueService;
	private TabelasClienteService tabelasClienteService;

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}
    
	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}	
    
    
	public JRReportDataObject execute(Map parameters) throws Exception {
		return null;
	}

	public List<Map<String, String>> findDados(Map parameters) throws Exception {
		Long idTabelaPreco = MapUtils.getLong(parameters,"tabelaPreco.idTabelaPreco");

    	List result = getJdbcTemplate().queryForList(montaSql(),
    		new Long[]{idTabelaPreco,idTabelaPreco,idTabelaPreco,idTabelaPreco});

    	for (Object object : result) {
			Map map = (Map)object;
			map.put("VALOR_FIXO", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FIXO")));
			map.put("FRETEPORKG", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"FRETEPORKG")));
			map.put("VL_ADVALOREM", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_ADVALOREM")));
		}

    	
    	getColumns(result,true,parameters);
    	Set setCrosstab = new LinkedHashSet();
		populateTable(setCrosstab,result,parameters);
		parameters.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(setCrosstab.size())});		
		montaParametersReport(parameters,idTabelaPreco);
		return result;
	}

    private void getColumns(List<Map> data, boolean isReport11, Map parameters) {
		HashMap tabelas = new HashMap();
		List headersKG = new ArrayList();
		Set<Double> headersPE = new LinkedHashSet();
		BigDecimal anterior = null;

		/* Descobre as Colunas(Headers) do relatorio. */
		for (Map element : data) {		
			/* captura todos os headers */
 		
				BigDecimal atual = (BigDecimal) element.get("VL_FAIXA_PROGRESSIVA");
				element.put("C_HEADER", element.get("VL_FAIXA_PROGRESSIVA")+"P");
				if(anterior != null && anterior.equals(atual) || headersKG.contains(atual)){					
					continue;
	}
				headersKG.add(atual);			
				anterior = atual;
			}

		Comparator comparator = new Comparator() {
			public int compare(Object arg0, Object arg1) {
				BigDecimal obj1 =  (BigDecimal) arg0;
				BigDecimal obj2 =  (BigDecimal) arg1;
				return obj1.compareTo(obj2);
			}
		};
		
		Collections.sort(headersKG, comparator);

		/* pega os header de KG - somente os que não se repetem */
		String lastElement = null;
		List<Map<String,String>> mapHeader = new ArrayList<Map<String,String>>();
		
		for (Object currentElement : headersKG) {
			Map<String, String> header = new HashMap<String, String>();
			String valor = "";	
			
			valor = currentElement+" kg (R$/CTRC)";
				
			header.put("valor", String.valueOf(currentElement)+"P");
			header.put("label", valor);
			mapHeader.add(header);
			
		}
		
		parameters.put("HEADER", mapHeader);
		
		
	}


    /**
	 *  Metodo que grava os dados na base.
	 *  
	 * @author Alexandre Poletto - LMS - GT5
	 * @param JdbcTemplate instancia de JdbcTemplate, List com os dados a serem inseridos.
	 * @return void  
	 */
	private void populateTable(Set setCrosstab, List listDados, Map parameters) {	

		List listaUFOrigem = new ArrayList();
		List listaUFDestino = new ArrayList();
        HashMap tabelas = new HashMap();
		String sql = "";
		List sqlRegistros = new ArrayList();
		StringBuffer colunas = null;
		StringBuffer values = null;
		String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
		String cdMinimoProgressivo = null; 
		int i = 1;
		/* Descobre as Colunas(Headers) do relatorio. */
		for (Iterator it = listDados.iterator(); it.hasNext(); i++) { 
			Map map = (Map)it.next();

			String tarifa = MapUtils.getString(map,"CD_TARIFA");
			Object column 		= map.get("VL_FAIXA_PROGRESSIVA");
			Object frete 		= map.get("FRETEPORKG");
			Object dado   		= map.get("VALOR_FIXO"); 
			Object adv   		= map.get("VL_ADVALOREM");
			Object rotaPreco	= map.get("ID_ROTA_PRECO");
			Object nrColunaDin  = map.get("NR_COLUNA_DINAMICA");
			String sigla = MapUtils.getString(map,"UF_DESTINO");

	        String chave = tarifa+rotaPreco.toString();
	   		if (!tabelas.containsKey(chave)){

	   			//pega os dados de origem e destino apenas se a rota e a tarifa forem novas
	   			String ufOrigem 	= MapUtils.getString(map,"UF_ORIGEM");
				String ufDestino 	= MapUtils.getString(map,"UF_DESTINO");
		        String mOrigem 		= MapUtils.getString(map,"ORIGEM");
		        String mDestino 	= MapUtils.getString(map,"DESTINO");
		        cdMinimoProgressivo = MapUtils.getString(map,"CD_MINIMO_PROGRESSIVO");

		        //se a mOrigem é nula, verifica se o uf esta na lista, se estiver então escreve "Todo o Estado" 
		        //senão escreve "Demais localidades"
		        if(mOrigem == null){
					mOrigem = !listaUFOrigem.contains(ufOrigem) ? msgTodoEstado : msgDemaisLocalidades;
				}else{
					//se o mOrigem não é nulo, insere o uf dele na lista
					listaUFOrigem.add(ufOrigem);
				}

				if(mDestino == null){
					mDestino = !listaUFDestino.contains(ufDestino) ? msgTodoEstado : msgDemaisLocalidades;
				}else{
					listaUFDestino.add(ufDestino);
				}

				String origem = ufOrigem + " - " + mOrigem;
				String destino = ufDestino + " - " + mDestino;
				map.put("ORIGEM", origem);
				map.put("DESTINO", destino);
				map.put("SIGLA", sigla);

	   		}

	   		parameters.put(("COLUMN"+nrColunaDin.toString()), column.toString());
		}
		parameters.put("CD_MINIMO_PROGRESSIVO", cdMinimoProgressivo);
	}	


	/**
	 * Metodo que monta os parameters do relatorio
	 * 
	 * @param parameters
	 * @param idTabelaPreco
	 */
	private void montaParametersReport(Map parameters, Long idTabelaPreco) {		 
		
		String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,getJdbcTemplate());
    	String tipoServico = getTabelasClienteService().getTipoServicoTabela(idTabelaPreco,getJdbcTemplate());
    	Map servico = getTabelasClienteService().getServicoTabela(idTabelaPreco, getJdbcTemplate());

		parameters.put("usuarioEmissor",SessionUtils.getUsuarioLogado().getNmUsuario());
		parameters.put("MOEDA", dsSimbolo);
		parameters.put("SERVICO", tipoServico);
		
		parameters.put("MODAL", domainValueService.findDomainValueDescription("DM_MODAL", MapUtils.getString(servico, "TP_MODAL")));
		parameters.put("ABRANGENCIA", domainValueService.findDomainValueDescription("DM_ABRANGENCIA", MapUtils.getString(servico, "TP_ABRANGENCIA")));

		int[] subReports = {
			TabelasClienteUtil.SUBREPORT_GENERALIDADES_TABELA_PRECO,
			TabelasClienteUtil.SUBREPORT_FORMALIDADES_AEREO,
			TabelasClienteUtil.SUBREPORT_SERVICOSAD,
			TabelasClienteUtil.SUBREPORT_COLETA,
			TabelasClienteUtil.SUBREPORT_ENTREGA,
			TabelasClienteUtil.SUBREPORT_TAXA_TERRESTRE,
			TabelasClienteUtil.SUBREPORT_LEGENDAS
			};

		getTabelasClienteService().montaSubReportsTabelaPreco(
			idTabelaPreco,
			subReports,
			configuracoesFacade,
			getJdbcTemplate(),
			parameters);

		//TAXA COMBUSTIVEL
		Map mapTaxCombustivel = getTabelasClienteService().getSubRelTaxaCombustivel(idTabelaPreco, getJdbcTemplate());
		List listTaxCombustivel = (List)mapTaxCombustivel.get("RESULT");
		Set subCrossTab = (Set)mapTaxCombustivel.get("SUBCROSSTAB");
		if(listTaxCombustivel !=null && !listTaxCombustivel.isEmpty()) {
			List listColumn = new ArrayList();
			listColumn.addAll(subCrossTab);
			String nomeSubRel = TabelasClienteUtil.PATH_TABELAPRECOS + "report/subReportTaxaCombustivel_Landscape_ct_" + subCrossTab.size() + ".jasper";

			parameters.put(TabelasClienteUtil.KEY_PARAMETER_TAXA_COMBUSTIVEL, new JRMapCollectionDataSource(listTaxCombustivel));
			for(int i=0; i<subCrossTab.size();i++) {
				mapTaxCombustivel.put("COLUMN"+(i+1), listColumn.get(i));
			}
			emitirTabelaTaxaCombustivel.setMapParameters(mapTaxCombustivel);
			parameters.put("SERVICE_TX_COMB", emitirTabelaTaxaCombustivel);
		}
		
	}

    private String montaSql(){
    	StringBuffer sql = new StringBuffer(); 
    	sql.append(" SELECT TARIFA.CD_TARIFA,  \n")
	    	.append("       TARIFA.valor_fixo, FRETE.FRETEPORKG, TARIFA.ID_ROTA_PRECO, \n")
	    	.append("     	TARIFA.UF_ORIGEM, \n")
			.append("     	TARIFA.UF_DESTINO, \n")
			.append("       ADVALOREM.VL_ADVALOREM, \n")
			.append("       TARIFA.VL_FAIXA_PROGRESSIVA, \n")
			.append("       TARIFA.CD_MINIMO_PROGRESSIVO, \n")
			.append("       TARIFA.PS_MINIMO, \n")
			.append(" 		TARIFA.NR_COLUNA_DINAMICA, \n")
			.append("     	TARIFA.ORIGEM, \n")
			.append("     	TARIFA.DESTINO  \n")
			.append(" FROM  \n") 
			.append("      (SELECT TP.CD_TARIFA_PRECO AS CD_TARIFA, TP.ID_TARIFA_PRECO as ID_TARIFA, VFP.VL_FIXO AS VALOR_FIXO,   \n") 	
			.append("       		FP.VL_FAIXA_PROGRESSIVA, FP.CD_MINIMO_PROGRESSIVO, RP.ID_ROTA_PRECO,     \n")
			.append("       		TAB.PS_MINIMO AS PS_MINIMO,  \n") 
			.append("     			UF_ORIGEM.SG_UNIDADE_FEDERATIVA as UF_ORIGEM,  \n")
			.append("     	    	UF_DESTINO.SG_UNIDADE_FEDERATIVA as UF_DESTINO,  \n")
			.append("				COLUNA_DINAMICA.NR_COLUNA_DINAMICA,  \n")		
			.append(" 				decode(rp.ID_MUNICIPIO_ORIGEM,null, \n")
			.append("					decode(A_ORIGEM.SG_AEROPORTO,null, \n")
			.append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append(" , \n")
			.append("						decode(M_ORIGEM.NM_MUNICIPIO,null,A_ORIGEM.SG_AEROPORTO,A_ORIGEM.SG_AEROPORTO || ' - ' || M_ORIGEM.NM_MUNICIPIO)), \n")
			.append("			    		M_ORIGEM.NM_MUNICIPIO) as ORIGEM,  \n")
			.append(" 				decode(rp.ID_MUNICIPIO_DESTINO,null, \n")
			.append("					decode(A_DESTINO.SG_AEROPORTO,null, \n")
			.append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append(" , \n")		
			.append("						decode(M_DESTINO.NM_MUNICIPIO,null,A_DESTINO.SG_AEROPORTO,A_DESTINO.SG_AEROPORTO || ' - ' || M_DESTINO.NM_MUNICIPIO)), \n")
			.append("			    		M_DESTINO.NM_MUNICIPIO) as DESTINO  \n")
			.append("       FROM   \n")
			.append("      		 TABELA_PRECO TAB,   \n") 	
			.append("      		 TABELA_PRECO_PARCELA TPP,   \n") 	
			.append("      		 PARCELA_PRECO PP,   \n") 	
			.append("      		 FAIXA_PROGRESSIVA FP,   \n") 	
			.append("      		 TARIFA_PRECO TP,   \n") 	
			.append("      		 VALOR_FAIXA_PROGRESSIVA VFP,   \n") 	
			.append("      		 TARIFA_PRECO_ROTA TPR,   \n") 	
			.append("      		 PESSOA P_ORIGEM, \n")
			.append("      		 PESSOA P_DESTINO, \n")
			.append("      		 ENDERECO_PESSOA EP_ORIGEM, \n")
			.append("      		 ENDERECO_PESSOA EP_DESTINO, \n")
			.append("      		 MUNICIPIO M_ORIGEM, \n")
			.append("      		 MUNICIPIO M_DESTINO, \n")
			.append("     		 UNIDADE_FEDERATIVA UF_ORIGEM,  \n")
			.append("     		 UNIDADE_FEDERATIVA UF_DESTINO,  \n")
			.append("     		 TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM,  \n")
			.append("     		 TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO,  \n")
			.append("      		 AEROPORTO A_ORIGEM, \n" )
			.append("      		 AEROPORTO A_DESTINO, \n" )
			.append("     		 FILIAL F_ORIGEM,  \n")
			.append("     		 FILIAL F_DESTINO,  \n")
			.append(" 			 PESSOA po_filial,  \n")
			.append(" 			 PESSOA pd_filial,  \n")		
			.append("      		 ROTA_PRECO RP,   \n")

			.append("      		 (select TABCOLDIN.*, rownum as nr_coluna_dinamica \n")
			.append("      		  from (select tp.id_tabela_preco, fp.id_faixa_progressiva, fp.vl_faixa_progressiva, \n")
			.append("      		  			fp.cd_minimo_progressivo, fp.id_tabela_preco_parcela \n")
			.append("       		 	from faixa_progressiva fp,  \n")
			.append("       		 		tabela_preco_parcela tpp,  tabela_preco tp, parcela_preco pp  \n")
			.append(" 					where  fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela  \n")
			.append(" 						and tpp.id_parcela_preco = pp.id_parcela_preco \n")
			.append(" 						and pp.CD_PARCELA_PRECO = 'IDFretePeso' \n")
			.append(" 						and tpp.id_tabela_preco = tp.id_tabela_preco  and tp.id_tabela_preco = ?  \n")
			.append(" 			  		order by fp.vl_faixa_progressiva) TABCOLDIN) COLUNA_DINAMICA  \n")

			.append("       WHERE   \n") 	
			.append("      		 TAB.ID_TABELA_PRECO = ?   \n") 	
			.append("      		 AND TPP.ID_TABELA_PRECO = TAB.ID_TABELA_PRECO   \n") 	
			.append("      		 AND  TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO   \n") 	
			.append("      		 AND  PP.CD_PARCELA_PRECO = 'IDFretePeso'   \n") 	
			.append("      		 AND  PP.TP_PARCELA_PRECO = 'P'   \n") 	
			.append("      		 AND  PP.TP_PRECIFICACAO = 'M'   \n")
			.append("			 AND COLUNA_DINAMICA.id_faixa_progressiva = fp.id_faixa_progressiva \n")
			.append("			 AND COLUNA_DINAMICA.id_tabela_preco_parcela = TPP.ID_TABELA_PRECO_PARCELA \n")
			.append(" 			 AND  VFP.ID_FAIXA_PROGRESSIVA = FP.ID_FAIXA_PROGRESSIVA  \n")
			.append(" 			 AND  TPP.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA  \n")
			.append(" 			 AND  VFP.ID_TARIFA_PRECO = TP.ID_TARIFA_PRECO  \n")
			.append(" 			 AND  TP.ID_TARIFA_PRECO = TPR.ID_TARIFA_PRECO  \n")
			.append(" 			 AND  TPR.ID_ROTA_PRECO = RP.ID_ROTA_PRECO  \n")
			.append("			 AND  TPR.ID_TABELA_PRECO = TAB.ID_TABELA_PRECO  \n")
			.append("      		 AND  RP.ID_AEROPORTO_ORIGEM = A_ORIGEM.ID_AEROPORTO (+) \n" )
			.append("      		 AND  RP.ID_AEROPORTO_DESTINO = A_DESTINO.ID_AEROPORTO (+) \n")
			.append("			 AND  RP.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA  \n")
			.append("			 AND  RP.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA  \n")
			
			.append("      AND A_ORIGEM.ID_AEROPORTO = P_ORIGEM.ID_PESSOA (+) \n" )
		   .append("      AND A_DESTINO.ID_AEROPORTO = P_DESTINO.ID_PESSOA (+) \n" )
		   
		   .append("      AND P_ORIGEM.ID_ENDERECO_PESSOA = EP_ORIGEM.ID_ENDERECO_PESSOA (+) \n" )
		   .append("      AND P_DESTINO.ID_ENDERECO_PESSOA = EP_DESTINO.ID_ENDERECO_PESSOA (+) \n" )

		   .append("      AND EP_ORIGEM.ID_MUNICIPIO = M_ORIGEM.ID_MUNICIPIO (+) \n" )
		   .append("      AND EP_DESTINO.ID_MUNICIPIO = M_DESTINO.ID_MUNICIPIO (+) \n" )
			
			
			.append("     		 AND  RP.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+) \n")
			.append("     		 AND  RP.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+) \n")
			.append(" 			 AND  RP.ID_FILIAL_ORIGEM = po_FILIAL.ID_Pessoa (+) \n")
			.append(" 			 AND  RP.ID_FILIAL_DESTINO = pd_FILIAL.ID_Pessoa (+) \n")		
			.append("     		 AND  RP.ID_TIPO_LOCALIZACAO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+) \n")
			.append("     		 AND  RP.ID_TIPO_LOCALIZACAO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)) TARIFA,  \n")
			.append("      (SELECT  PF.VL_PRECO_FRETE as FRETEPORKG, TP.ID_TARIFA_PRECO AS ID_FRETE   \n") 	
			.append("       FROM   \n")
			.append("      		 TABELA_PRECO TAB,   \n") 
			.append("      		 TABELA_PRECO_PARCELA TPP,   \n") 	
			.append("      		 PARCELA_PRECO PP,   \n") 	
			.append("  			 PRECO_FRETE PF,  \n")
			.append("      		 TARIFA_PRECO TP   \n") 	
			.append("       WHERE   \n") 	
			.append("      		 TAB.ID_TABELA_PRECO = ?   \n") 	
			.append("      		 AND TPP.ID_TABELA_PRECO = TAB.ID_TABELA_PRECO   \n") 	
			.append("      		 AND  TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO   \n") 	
			.append("      		 AND  PP.CD_PARCELA_PRECO = 'IDFreteQuilo'   \n") 	
			.append("      		 AND  PP.TP_PARCELA_PRECO = 'P'   \n") 	
			.append("      		 AND  PP.TP_PRECIFICACAO = 'P'   \n")
			.append(" 			 AND  TPP.ID_TABELA_PRECO_PARCELA = PF.ID_TABELA_PRECO_PARCELA  \n")
			.append(" 			 AND  Pf.ID_TARIFA_PRECO = TP.ID_TARIFA_PRECO) FRETE ,  \n")
			.append("      (SELECT  PF.VL_PRECO_FRETE as VL_ADVALOREM, TP.ID_TARIFA_PRECO AS ID_ADVALOREM   \n") 	
			.append("       FROM   \n")
			.append("             TABELA_PRECO TAB, \n")	
			.append("      		 TABELA_PRECO_PARCELA TPP,  \n") 	
			.append("      		 PARCELA_PRECO PP,  \n") 	
			.append("  			 PRECO_FRETE PF,  \n")
			.append("      		 TARIFA_PRECO TP   \n") 	
			.append("       WHERE   \n") 	
			.append("      		 TAB.ID_TABELA_PRECO = ?   \n") 	
			.append("      		 AND TPP.ID_TABELA_PRECO = TAB.ID_TABELA_PRECO   \n") 	
			.append("      		 AND  TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO   \n") 	
			.append("      		 AND  PP.CD_PARCELA_PRECO = 'IDAdvalorem'   \n") 	
			.append("      		 AND  PP.TP_PARCELA_PRECO = 'P'  \n") 	
			.append("      		 AND  PP.TP_PRECIFICACAO = 'P'   \n")
			.append(" 			 AND  TPP.ID_TABELA_PRECO_PARCELA = PF.ID_TABELA_PRECO_PARCELA  \n")
			.append(" 			 AND  Pf.ID_TARIFA_PRECO = TP.ID_TARIFA_PRECO) ADVALOREM   \n")
			.append(" WHERE  \n")
			.append("       FRETE.ID_FRETE = ADVALOREM.ID_ADVALOREM  \n")
			.append("       AND  TARIFA.ID_TARIFA = FRETE.ID_FRETE   \n") 	
			.append(" ORDER BY  TARIFA.CD_TARIFA, TARIFA.ID_ROTA_PRECO, TARIFA.VL_FAIXA_PROGRESSIVA, TARIFA.ORIGEM, TARIFA.DESTINO  \n");

    	return sql.toString();
    }

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setEmitirTabelaTaxaCombustivel(
			EmitirTabelaTaxaCombustivelLandscapeService emitirTabelaTaxaCombustivel) {
		this.emitirTabelaTaxaCombustivel = emitirTabelaTaxaCombustivel;
	}
}
