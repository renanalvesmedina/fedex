package com.mercurio.lms.contasreceber.model.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemOcorrenciaPreFatura;
import com.mercurio.lms.contasreceber.model.OcorrenciaPreFatura;
import com.mercurio.lms.contasreceber.model.dao.CarregarArquivoRecebidoPreFaturaPadraoDAO;
import com.mercurio.lms.contasreceber.model.param.ArquivoRecebidoPreFaturaPadraoTelaParam;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 *
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.carregarArquivoRecebidoPreFaturaPadraoService"
 */
public class CarregarArquivoRecebidoPreFaturaPadraoService {

	private static final Locale LOCALE_DEFAULT = new Locale("pt","BR");

	private static final String SEPARATOR_CSV = ";";

	private static final String ID_FILIAL_DEVEDOR_DOC_SERV_FAT = "idFilialDevedorDocServFat";
	
	private static final String TP_SITUACAO_DOCTO_SERVICO = "tpSituacaoDoctoServico";

	private static final Logger LOGGER = LogManager.getLogger(CarregarArquivoRecebidoPreFaturaPadraoService.class);
	
	private static final String SG_FILIAL = "sgFilial";

	private static final String ID_FILIAL = "idFilial";

	private static final String TP_MODAL = "tpModal";

	private static final String ID_DOCTOSERVICO = "idDoctoservico";

	private static final String NR_DOCTO_SERVICO = "nrDoctoServico";

	private static final String DH_EMISSAO = "dhEmissao";

	private static final String TP_ABRANGENCIA = "tpAbrangencia";

	private static final String EM_FATURA = "F";

	private static final String LIQUIDADO = "L";
	
	private static final String AUTORIZADO = "A";

	private static final String NR_IDENTIFICACAO = "nrIdentificacao";

	private static final String ID_DEVEDOR_DOCTO_SERVICO_SERV_FAT = "idDevedorDoctoServicoServFat";

	private static final String TP_SITUACAO_COBRANCA = "tpSituacaoCobranca";

	private static final String VL_DEVIDO = "vlDevido";

	private static final String INFO_NR_PRE_FATURA = "Excel padr�o TNT";
	
	private static final String NOTA_FISCAL_SERVICO = "NFS";

	private static final String NOTA_FISCAL_SERV_ELETRONICO = "NSE";

	private static final String NOTA_FISCAL_DESCONTO = "NDN";

	private static final String NOTA_FISCAL = "NFT";

	private static final String NOTA_FISCAL_ELETRONICO = "NTE";

	private static final String CONHECIMENTO = "CTR";	
	
	private static final String CONHECIMENTO_ELETRONICO = "CTE";	
	
	private static final List TP_SITUACAO_APROVACAO = Arrays.asList(new String[]{"E","R"});
	
	private CarregarArquivoRecebidoPreFaturaPadraoDAO carregarArquivoRecebidoPreFaturaPadraoDAO;
	
	private ItemOcorrenciaPreFaturaService itemOcorrenciaPreFaturaService;
	
	private DescontoService descontoService; 
	
	private FaturaService faturaService;
	
	private OcorrenciaPreFaturaService ocorrenciaPreFaturaService;
	
	private GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService;
	
	private ConfiguracoesFacade configuracoesFacade;
	
	private ReportExecutionManager reportExecutionManager;
	
	private ClienteService clienteService;
	
	private FilialService filialService;
	
	private ParametroGeralService parametroGeralService;
	
	private DomainValueService domainValueService;
	
	private DoctoServicoService doctoServicoService;
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}	
	
	public TypedFlatMap executeImportacao(ArquivoRecebidoPreFaturaPadraoTelaParam param){
		if(!faturaService.validaDtEmissao(param.getDtEmissao())){
			throw new BusinessException("LMS-36099");
		}
							
		/*Empresa*/ 
		Long idEmpresa = SessionUtils.getFilialSessao().getEmpresa().getIdEmpresa();
		
		/*Grava a tabela de ocorr�ncia*/
		OcorrenciaPreFatura ocorrencia = this.storeOcorrenciaPreFatura(param);
				
		StringBuilder msgInfo = new StringBuilder();
		
		Integer qtTotalDocumentos = 0;
		
		Long idCliente = null;
						
		/*Lista de id devedores*/ 
		List<Long> lstDevedorDocServFat = new ArrayList<Long>();
		
		List<String> listaDoctosAceitos = Arrays.asList("CTE","CTR","NFS","NFT","NTE","NSE");
		List<DomainValue> listaDomainValuesAceitos = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", listaDoctosAceitos);
		String dsCTE = "";
		Map<String, String> mapaDomainValuesAceitos = new HashMap<String, String>();
		
		for(DomainValue domainValue : listaDomainValuesAceitos) {
			mapaDomainValuesAceitos.put(domainValue.getDescriptionAsString(), domainValue.getValue());
			if(CONHECIMENTO_ELETRONICO.equals(domainValue.getValue())) {
				dsCTE = domainValue.getDescriptionAsString();
			}
		}
		
		List<PreFaturaPadrao> list = this.getDataFile(param);
		for (PreFaturaPadrao pre : list) {
			/*Busca o primeiro tipo de Documento de Servi�o que existe*/
			if( pre.getDsTpDoctoServico() == null) {
				pre.setTpDoctoServico(CONHECIMENTO_ELETRONICO);
				pre.setDsTpDoctoServico(dsCTE);
			} else {
				if(!mapaDomainValuesAceitos.containsKey(pre.getDsTpDoctoServico())) {
					pre.setTpDoctoServico(CONHECIMENTO_ELETRONICO);
					pre.setDsTpDoctoServico(dsCTE);
				} else {					
					pre.setTpDoctoServico(mapaDomainValuesAceitos.get(pre.getDsTpDoctoServico()));					
				}
			}					
			
			/*Verifica se o cliente existe*/
			if(qtTotalDocumentos == 0){ 
				Cliente cliente = clienteService.findByNrIdentificacao(pre.getNrIdentificacao());
				if(cliente == null){
					throw new BusinessException("LMS-28006", new Object[]{pre.getNrIdentificacao()});
				}
				idCliente = cliente.getIdCliente();
			}
						
			/*Monta a mensagem, caso ocorra algum erro*/
			msgInfo = new StringBuilder();
			msgInfo.append(pre.getDsTpDoctoServico()).append(" Filial origem: ")
				   .append(pre.getSgFilialOrigem()).append(" Nr. Doc.: ").append(pre.getNrDoctoServico());
			
			/*Verifica a filial de origem*/
			Map<String, Object> mp = this.obtemDadosUnidadeDeNegocio(pre.getSgFilialOrigem(),idEmpresa);
			if(mp.isEmpty()){				
				throw new BusinessException("LMS-36251", new Object[]{msgInfo.toString()});
			}
			
			/*Obtem dados do documento de servico*/
			Map<String, Object> map = this.obtemDadosDocServicoPreFaturaPadrao(
					MapUtils.getString(mp, "sgFilial"), pre.getTpDoctoServico(), pre.getNrDoctoServico(), idEmpresa, idCliente, pre.getDtEmissao());
			
			if(map != null && map.isEmpty()){				
				throw new BusinessException("LMS-36115", new Object[]{msgInfo.toString()});
			}
			
			/*Verifica o valor devido*/
			if(!pre.getVlFrete().equals(MapUtilsPlus.getBigDecimal(map, VL_DEVIDO))){
				throw new BusinessException("LMS-36252", new Object[]{msgInfo.append(" >> valor da base :")
						.append(MapUtilsPlus.getBigDecimal(map, VL_DEVIDO))
						.append(" valor arquivo : ").append(pre.getVlFrete()).toString()});
			}
			
			/*Salva a ocorr�ncia  dependendo do tpSituacaoCobranca*/ 
			map.put("tpDoctoServico", pre.getTpDoctoServico() );
			map.put("dtEmissao", pre.getDtEmissao());
			map.put("idOcorrencia", ocorrencia.getIdOcorrenciaPreFatura());
			map.put("sgFilial", pre.getSgFilialOrigem());
			
			if (!this.validaSituacaoCobranca(map)){
				continue;
			}
			
			if(!doctoServicoService.isMonitoramentoEletronicoAutorizado(
					pre.getTpDoctoServico(), MapUtils.getString(map, TP_SITUACAO_DOCTO_SERVICO))) {
				storeItemOcorrenciaPreFatura(map, "LMS-36282");
				continue;
			}
					
			/*Busca o TP_SITUACAO_APROVACAO atrav�s da tabela desconto*/			
			Desconto desconto = descontoService.findDescontoByIdDevedor(MapUtils.getLong(map, ID_DEVEDOR_DOCTO_SERVICO_SERV_FAT));								
			if(desconto != null && TP_SITUACAO_APROVACAO.contains(desconto.getTpSituacaoAprovacao().getValue())){
				storeItemOcorrenciaPreFatura(map, "LMS-36010");	
				continue;
			}
			
			/*Se a filial do devedor for diferente de filial da sess�o*/			
			if(!MapUtils.getLong(map, ID_FILIAL_DEVEDOR_DOC_SERV_FAT).equals(SessionUtils.getFilialSessao().getIdFilial())){
				throw new BusinessException("LMS-36119", new Object[]{msgInfo.toString()});
			}
			
			/*Verifica se a identificacao do arquivo � igual a identificacao do banco*/ 
			if(!pre.getNrIdentificacao().equals(MapUtils.getString(map, NR_IDENTIFICACAO))){						
				throw new BusinessException("LMS-36120", new Object[]{msgInfo.toString()});
			}
			
			/*Valida situacao do documento de servico*/
			if (!this.validaSituacaoDocumento(map, pre.getTpDoctoServico())){
				continue;
			}
			
			/*Adiciona o id do devedor na lista ara ap�s adicionar no objeto param*/
			lstDevedorDocServFat.add(MapUtils.getLong(map, ID_DEVEDOR_DOCTO_SERVICO_SERV_FAT));
			
			qtTotalDocumentos = qtTotalDocumentos + 1; 
											
		}/*for*/
		
		if(qtTotalDocumentos == 0){
			TypedFlatMap error = new TypedFlatMap();
			error.put("erroPreFatura", configuracoesFacade.getMensagem("LMS-36112"));
			return error;
		}
		
		/*Informa a quantidade total de documentos*/				
		param.setQtDocumentos(qtTotalDocumentos);
		
		/*Informa a lista de Devedor Doc Fat*/
		param.setLstDevedorDocServFat(lstDevedorDocServFat);
		
		param.setNrPreFatura(ocorrencia.getNrPreFatura());
		
		
		/*Fatura*/
		Fatura fatura = this.storeFatura(param);
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idFatura", fatura.getIdFatura());
		return retorno;
	}
	
	/**
	 * Gera o relat�rio de fatura
	 * 
	 * @param  idFatura
	 */
	public TypedFlatMap generateReport(Long idFatura){
		
		String reportLocation = "";
		TypedFlatMap tfm = new TypedFlatMap();
		
		if(idFatura != null){
			
			TypedFlatMap parameters = new TypedFlatMap();
			parameters.put("importacaoPreFaturas", idFatura.toString());
			parameters.put("tpFormatoRelatorio", "pdf");			
			
			try {
				reportLocation = this.reportExecutionManager.generateReportLocator("lms.contasreceber.emitirFaturasNacionaisService", parameters);
			} catch (Exception e) {
				String mensagem = configuracoesFacade.getMensagem("LMS-01130");
				tfm.put("erroImpressao", MessageFormat.format(mensagem, "Fatura salva, mas não foi possível imprimir o documento. " +e.getMessage()));
				LOGGER.error(e);
			}
			tfm.put("ex", "");			
		}
		
		tfm.put("_value", reportLocation);

		return tfm;
	}	
	
	/**
	 * Valida situa��o de cobran�a 
	 * 
	 * @param map
	 */
	private boolean validaSituacaoCobranca(Map map){
		
		if(LIQUIDADO.equals(MapUtils.getString(map, TP_SITUACAO_COBRANCA))){
			storeItemOcorrenciaPreFatura(map, "LMS-36116");
			return false;
		} else if(EM_FATURA.equals(MapUtils.getString(map, TP_SITUACAO_COBRANCA))){
			storeItemOcorrenciaPreFatura(map, "LMS-36118");
			return false;
		} 
		return true;
	}
	
	/**
	 * Salva os dados da fatura 
	 * 
	 * @param param
	 * @return Fatura
	 */
	private Fatura storeFatura(ArquivoRecebidoPreFaturaPadraoTelaParam param){
		
		Fatura fatura = new Fatura();
		fatura.setDtEmissao(param.getDtEmissao());
		fatura.setDtVencimento(param.getDtVencimento());
		fatura.setBlGerarBoleto(param.getBlGerarBoleto());
		fatura.setCedente(param.getCedente());
		fatura.setDivisaoCliente(param.getDivisaoCliente());
		fatura.setTpModal(param.getTpModal());
		fatura.setTpAbrangencia(param.getTpAbrangencia());
		fatura.setQtDocumentos(param.getQtDocumentos());
		fatura.setBlGerarEdi(Boolean.TRUE);
		fatura.setBlGerarBoleto(param.getBlGerarBoleto());
		fatura.setTpFatura(new DomainValue("R"));
		fatura.setNrPreFatura(param.getNrPreFatura());

		return gerarFaturaArquivoRecebidoService.storeFaturaWithIdsDevedorDocServFat(fatura, param.getLstDevedorDocServFat());	
	}
	
	/**
	 * Salva informa��es da Ocorr�ncia de Pre Fatura
	 * 
	 */
	private OcorrenciaPreFatura storeOcorrenciaPreFatura(ArquivoRecebidoPreFaturaPadraoTelaParam param){

		OcorrenciaPreFatura ocorrencia = new OcorrenciaPreFatura();
		ocorrencia.setCliente(param.getCliente());
		ocorrencia.setNrPreFatura(INFO_NR_PRE_FATURA);
		ocorrencia.setDtEmissao(param.getDtEmissao());
		ocorrencia.setDtVencimento(param.getDtVencimento());
		ocorrencia.setDhImportacao(JTDateTimeUtils.getDataHoraAtual());
		ocorrencia.setNmArquivo(param.getFileName());
		ocorrenciaPreFaturaService.store(ocorrencia);
		
		//LMS-845: Deixa salvar primeiro para gerar o id, ap�s utiliza o id como numero da pre-fatura como 
		//especificado
		ocorrencia.setNrPreFatura(ocorrencia.getIdOcorrenciaPreFatura().toString());
		ocorrenciaPreFaturaService.store(ocorrencia);
		
		return ocorrencia;
	}
	
	/**
	 * Obtem a situa��o de documentos (CTR,NFT,NDN,NFS)
	 * @param map
	 * @param tpDoctoServico
	 * @return
	 */
	private boolean validaSituacaoDocumento(Map map, String tpDoctoServico){
		
		Long idDoctoServico = MapUtils.getLong(map, ID_DOCTOSERVICO); 
		
		String situacao = null;
		if(CONHECIMENTO.equals(tpDoctoServico) || NOTA_FISCAL.equals(tpDoctoServico) ||
				CONHECIMENTO_ELETRONICO.equals(tpDoctoServico) || NOTA_FISCAL_ELETRONICO.equals(tpDoctoServico) ){
			situacao = carregarArquivoRecebidoPreFaturaPadraoDAO.findTpSituacaoConhecimento(idDoctoServico);
		}else if(NOTA_FISCAL_DESCONTO.equals(tpDoctoServico)){
			situacao = carregarArquivoRecebidoPreFaturaPadraoDAO.findTpSituacaoNotaDebito(idDoctoServico);
		}else if(NOTA_FISCAL_SERVICO.equals(tpDoctoServico) || NOTA_FISCAL_SERV_ELETRONICO.equals(tpDoctoServico)){
			situacao = carregarArquivoRecebidoPreFaturaPadraoDAO.findTpSituacaoNotaServico(idDoctoServico);
		}
		
		/*Se situacao for igual a C*/
		if("C".equals(situacao)){
			storeItemOcorrenciaPreFatura(map, "LMS-36117");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Salva o objeto ItemOcorrenciaPreFatura
	 * @param parametes
	 */
	private void storeItemOcorrenciaPreFatura(Map parametes, String exception){
		
		ItemOcorrenciaPreFatura iop = new ItemOcorrenciaPreFatura();
		
		/*Ocorrencia*/
		OcorrenciaPreFatura ocorrenciaPreFatura = new OcorrenciaPreFatura();
		ocorrenciaPreFatura.setIdOcorrenciaPreFatura(MapUtils.getLong(parametes, "idOcorrencia"));
		
		iop.setOcorrenciaPreFatura(ocorrenciaPreFatura);
		iop.setTpDoctoServico(MapUtilsPlus.getDomainValue(parametes, "tpDoctoServico"));
		iop.setSgFilial(MapUtils.getString(parametes, SG_FILIAL));
		iop.setNrDoctoServico(MapUtils.getLong(parametes, NR_DOCTO_SERVICO));
		iop.setDtEmissao(MapUtilsPlus.getYearMonthDay(parametes, "dtEmissao"));
		iop.setObItemOcorrenciaPreFatura(configuracoesFacade.getMensagem(exception));
		/*Store ItemOcorrenciaPreFatura */
		itemOcorrenciaPreFaturaService.store(iop);
	}
	
	
	/**
	 * Obtem os dados da filial centro de custo atrav�s da sigla filial de origem
	 * @param sgFilialOrigem
	 * @return Map
	 */
	private Map<String, Object> obtemDadosUnidadeDeNegocio(String sgFilialOrigem, Long idEmpresa){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(sgFilialOrigem.length() > 2){
			List<Map> lsFilial = filialService. findFilialBySgEmpresaLookup(sgFilialOrigem, idEmpresa);
			if(lsFilial != null && !lsFilial.isEmpty()){
				Map<String, Object> filial = lsFilial.get(0);
				map.put(ID_FILIAL, MapUtils.getLong(filial, "idFilial"));
				map.put(SG_FILIAL, MapUtils.getString(filial, "sgFilial"));
			}
		}else{
				
			List<Object[]> list = carregarArquivoRecebidoPreFaturaPadraoDAO.findFilialUnidadeNegocio(sgFilialOrigem,idEmpresa);
			if(list != null && !list.isEmpty()){
				Object[] data = list.get(0);
				map.put(ID_FILIAL, data[0]);
				map.put(SG_FILIAL, data[1]);
			}	
		}
		return map;
	} 
	
	/**
	 * Obtem dados de pre faturamento
	 * 
	 * @param sgFilialOrigem
	 * @param tpDoctoServico
	 * @param nrDoctoServico
	 * @param dtEmissao
	 * @return Map
	 */
	private Map<String, Object> obtemDadosDocServicoPreFaturaPadrao(String sgFilialOrigem, String tpDoctoServico, Long nrDoctoServico, Long idEmpresa, Long idCliente, YearMonthDay dtEmissao) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Object[]> list = carregarArquivoRecebidoPreFaturaPadraoDAO
			.findDataDocServicoPreFaturaPadrao(sgFilialOrigem,tpDoctoServico,nrDoctoServico,idEmpresa,idCliente,dtEmissao);
		
		if(list != null && !list.isEmpty()){
			Object[] data = list.get(0);
			map.put(TP_MODAL, data[0]);
			map.put(TP_ABRANGENCIA, data[1]);
			map.put(ID_DOCTOSERVICO, data[2]);
			map.put(NR_IDENTIFICACAO, StringUtils.leftPad((String)data[3], 14, '0'));
			map.put(NR_DOCTO_SERVICO, data[4]);
			map.put(DH_EMISSAO, data[5]);
			map.put(ID_DEVEDOR_DOCTO_SERVICO_SERV_FAT, data[6]);
			map.put(VL_DEVIDO, data[7]);
			map.put(TP_SITUACAO_COBRANCA, data[8]);
			map.put(ID_FILIAL_DEVEDOR_DOC_SERV_FAT, data[9]);
			map.put(TP_SITUACAO_DOCTO_SERVICO, data[10]);
		}
		
		return map;
	}
	
	/**
	 * Faz a leitura do arquivo xsl
	 * 
	 * @param param
	 * @return List
	 */
	@SuppressWarnings("deprecation")
	private List<PreFaturaPadrao> getDataFile(ArquivoRecebidoPreFaturaPadraoTelaParam param){
		
		String simpleFormat = null;
			
		List<PreFaturaPadrao> list = new ArrayList<PreFaturaPadrao>();
		
		PreFaturaPadrao pfp = null;
		
		String[] dataLine = null;
		
		Number vlFrete = null;
		
		String line = null;
		try{
			
			while( (line = param.getArquivo().readLine()) != null ){
				
				dataLine = line.split(SEPARATOR_CSV);
				if(StringUtils.isBlank(dataLine[0])){
					continue;
				}
				
				pfp = new PreFaturaPadrao();
				
				/*Sigla filial*/
				pfp.setSgFilialOrigem(StringUtils.trim(dataLine[0]));
				
				/*Numero documento*/
				pfp.setNrDoctoServico(LongUtils.getLong(StringUtils.trim(dataLine[1])));
				
				/*Data do documento formato dd/MM/yyyy*/
				if(dataLine[2].length() == 8 ){
					simpleFormat = "dd/MM/yy";
				}else{
					simpleFormat = "dd/MM/yyyy";					
				}
				
				pfp.setDtEmissao(DateTimeFormat.forPattern(simpleFormat).parseDateTime(dataLine[2]).toYearMonthDay());
				
				/*Identificacao do cliente*/
				pfp.setNrIdentificacao(StringUtils.leftPad(StringUtils.trim(dataLine[3]), 14, '0'));
				
				/*Valor do documento*/				
				vlFrete = DecimalFormat.getInstance(LOCALE_DEFAULT).parse(StringUtils.trim(dataLine[4]));				
				pfp.setVlFrete(BigDecimalUtils.getBigDecimal(vlFrete));
				
				if(dataLine.length > 5) {
					pfp.setDsTpDoctoServico(dataLine[5]);
				}
				
				list.add(pfp);
				
			}/*while*/	
		
		}catch (IOException e) {
			LOGGER.error(e);			
		} catch (ParseException e) {
			LOGGER.error(e);
		}		
		return list;
	}
	
	class PreFaturaPadrao{
		
		String sgFilialOrigem;
		String tpDoctoServico;
		String dsTpDoctoServico;
		Long nrDoctoServico;
		YearMonthDay dtEmissao;
		String nrIdentificacao;
		BigDecimal vlFrete;
		
		public String getSgFilialOrigem() {
			return sgFilialOrigem;
		}
		public void setSgFilialOrigem(String sgFilialOrigem) {
			this.sgFilialOrigem = sgFilialOrigem;
		}
		public String getTpDoctoServico() {
			return tpDoctoServico;
		}
		public void setTpDoctoServico(String tpDoctoServico) {
			this.tpDoctoServico = tpDoctoServico;
		}
		public String getDsTpDoctoServico() {
			return dsTpDoctoServico;
		}
		public void setDsTpDoctoServico(String dsTpDoctoServico) {
			this.dsTpDoctoServico = dsTpDoctoServico;
		}
		public Long getNrDoctoServico() {
			return nrDoctoServico;
		}
		public void setNrDoctoServico(Long nrDoctoServico) {
			this.nrDoctoServico = nrDoctoServico;
		}		
		public String getNrIdentificacao() {
			return nrIdentificacao;
		}
		public void setNrIdentificacao(String nrIdentificacao) {
			this.nrIdentificacao = nrIdentificacao;
		}
		public BigDecimal getVlFrete() {
			return vlFrete;
		}
		public void setVlFrete(BigDecimal vlFrete) {
			this.vlFrete = vlFrete;
		}
		public YearMonthDay getDtEmissao() {
			return dtEmissao;
		}
		public void setDtEmissao(YearMonthDay dtEmissao) {
			this.dtEmissao = dtEmissao;
		}
		
	} 

	public CarregarArquivoRecebidoPreFaturaPadraoDAO getCarregarArquivoRecebidoPreFaturaPadraoDAO() {
		return carregarArquivoRecebidoPreFaturaPadraoDAO;
	}

	public void setCarregarArquivoRecebidoPreFaturaPadraoDAO(
			CarregarArquivoRecebidoPreFaturaPadraoDAO carregarArquivoRecebidoPreFaturaPadraoDAO) {
		this.carregarArquivoRecebidoPreFaturaPadraoDAO = carregarArquivoRecebidoPreFaturaPadraoDAO;
	}

	public ItemOcorrenciaPreFaturaService getItemOcorrenciaPreFaturaService() {
		return itemOcorrenciaPreFaturaService;
	}

	public void setItemOcorrenciaPreFaturaService(
			ItemOcorrenciaPreFaturaService itemOcorrenciaPreFaturaService) {
		this.itemOcorrenciaPreFaturaService = itemOcorrenciaPreFaturaService;
	}

	public DescontoService getDescontoService() {
		return descontoService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public OcorrenciaPreFaturaService getOcorrenciaPreFaturaService() {
		return ocorrenciaPreFaturaService;
	}

	public void setOcorrenciaPreFaturaService(
			OcorrenciaPreFaturaService ocorrenciaPreFaturaService) {
		this.ocorrenciaPreFaturaService = ocorrenciaPreFaturaService;
	}

	public GerarFaturaArquivoRecebidoService getGerarFaturaArquivoRecebidoService() {
		return gerarFaturaArquivoRecebidoService;
	}

	public void setGerarFaturaArquivoRecebidoService(
			GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService) {
		this.gerarFaturaArquivoRecebidoService = gerarFaturaArquivoRecebidoService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
}
