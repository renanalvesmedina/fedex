package com.mercurio.lms.expedicao.model.service;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.tntbrasil.integracao.domains.expedicao.DocumentoEnvioAverbacao;
import br.com.tntbrasil.integracao.domains.expedicao.MercadoriaNotaFiscalDMN;
import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalDocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.expedicao.PessoaNotaFiscalDocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.fedex.BloqueioLiberacaoAgendamentoFedexDTO;
import br.com.tntbrasil.integracao.domains.fedex.OcorrenciaFedexDTO;
import br.com.tntbrasil.integracao.domains.fedex.OcorrenciaFedexDocumentoDTO;
import br.com.tntbrasil.integracao.domains.fedex.OcorrenciaFedexNotaRncDTO;
import br.com.tntbrasil.integracao.domains.fedex.OcorrenciaFedexOcorrenciaEntregaDTO;
import br.com.tntbrasil.integracao.domains.fedex.OcorrenciaFedexOcorrenciaEntregaRedespachoDTO;
import br.com.tntbrasil.integracao.domains.fedex.OcorrenciaFedexRncDTO;
import br.com.tntbrasil.integracao.domains.fedex.RomaneioEntregaDocumentoDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.sim.EventoDoctoServicoFedexDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.vendas.CTeDMN;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.FilialRotaCc;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.FilialRotaCcService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConfiguracoesFacadeImpl;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.constantes.entidades.ConsParametroGeral;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.dto.EVersaoXMLCTE;
import com.mercurio.lms.expedicao.model.CartaCorrecaoCampo;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.DoctoServicoSeguros;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.dao.ConhecimentoDAO;
import com.mercurio.lms.expedicao.model.dao.MonitoramentoDocEletronicoDAO;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.NotaOcorrenciaNc;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServico;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.seguros.model.service.AverbacaoDoctoServicoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.ChaveRPStGenerator;
import com.mercurio.lms.util.DocumentoEnvioAverbacaoHelper;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.SeguroClienteService;

@Assynchronous
public class MonitoramentoDocEletronicoService extends CrudService<MonitoramentoDocEletronico, Long> {
    
	private static final String DC_SERIECNH = "SERIECNH";
	private static final String DC_CONHFDX = "CONHFDX";
	private static final String DC_DTCONH = "DTCONH";
	private static final String DC_CNPJFDX = "CNPJFDX";
	private static final String DC_MODSEFAZ = "MODSEFAZ";
	private static final String NR_CHAVE_DOCUMENTO_ANTERIOR = "NR_CHAVE_DOCUMENTO_ANTERIOR";
	private static final String CD_INTEGRA_NOTFIS_FDX = "CD_INTEGRA_NOTFIS_FDX";
	private static final String INTEGRA_NOTFIS_FAAV = "INTEGRA_NOTFIS_FAAV";
	
    private static final String TP_DESTINO_ATEM = "ATEM";
    private static final String TP_WEBSERVICE_AVERBACAO_CANCELADO = "C";
    private static final String WEB_SERVICE_CTE_AVERBACAO = "averbaCTe";
	
	private static final String MODAL_MANIFESTO_FDX = "MODAL_MANIFESTO_FDX";
	private static final String CNPJ_PROPRIETARIO_FDX = "CNPJ_PROPRIETARIO_FDX";
	private static final String SIM = "S";
	private static final String NTE = "NTE";
	private static final String CTE = "CTE";
	private static final String DT_INICIO_NOTFIS_RPST_FDX = "DT_INICIO_NOTFIS_RPST_FDX";
	private static final List<String> TIPOS_REDESPACHO_MDS = Arrays.asList("0","3");
	private static final String TP_REDESPACHO_SUBCONTRATACAO = "4";

	private Logger log = LogManager.getLogger(this.getClass());
	private static final String EVENTOS_EDI_FDX = "EVENTOS_EDI_FDX";
	private static final String FILE_NAME = "fileName";
	
	private static final String NM_TABELA_OCORREN = "TRACKING FEDEX MDS";
	private static final String NM_TABELA_OCORREN_SUB = "TRACKING FEDEX SUB";
	private static final Short CD_OCORRENCIA_RNC = Short.valueOf("407");
	private static final Short CD_OCORRENCIA_ENTREGA_REALIZADA = Short.valueOf("21");
	private static final Short CD_OCORRENCIA_ENTREGA = Short.valueOf("59");
	private static final Short CD_OCORRENCIA_ENTREGA_PARCIAL = Short.valueOf("102");
	private static final Short CD_EVENTO_ENTREGA_PARCIAL_FINALIZADA = Short.valueOf("154");
	
	private static final String PARAMETRO_FILIAL_INTEGRA_NOTFIS_FAAV = "INTEGRA_NOTFIS_FAAV";

	private ParcelaDoctoServicoService parcelaDoctoServicoService;
	private ValorCustoService valorCustoService;
	private DoctoServicoService doctoServicoService;
	private InscricaoEstadualService inscricaoEstadualService;
	private DadosComplementoService dadosComplementoService;
	private CalcularFreteService calcularFreteService;
	private DevedorDocServService devedorDocServService;
	private GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private ConfiguracoesFacadeImpl configuracoesFacadeImpl;
	private ConhecimentoDAO conhecimentoDAO;
	
	private FilialService filialService;
	private ParametroGeralService parametroGeralService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventoRastreabilidadeInternacionalService;
	private ConhecimentoService conhecimentoService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	
	private ObservacaoDoctoServicoService observacaoDoctoServicoService;
	private ImpostoServicoService impostoServicoService;
	private ParametroFilialService parametroFilialService;
	private EmitirDocumentoService emitirDocumentoService;
	private NFEConjugadaService nfeConjugadaService;
	private MoedaService moedaService;
	private PaisService paisService;
	private DensidadeService densidadeService;
	private ControleTrechoService controleTrechoService;
	
	private ManifestoService manifestoService;
	private ConfiguracoesFacade configuracoesFacade;
	private IntegracaoJmsService integracaoJmsService;
	
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	
	private FilialRotaCcService filialRotaCcService;
	
	// LMSA-6322
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	
	// LMSA-7369
	private DoctoServicoSegurosService doctoServicoSegurosService;
	private ApoliceSeguroService apoliceSeguroService;
	private AverbacaoDoctoServicoService averbacaoDoctoServicoService;
	private SeguroClienteService seguroClienteService;
	private ControleCargaService controleCargaService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private NaoConformidadeService naoConformidadeService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	
	private static final String DESCRICAO_DOCTO_SERVICO_REJEICAO_RPS = "%Número fiscal do RPS:%";
	

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMonitoramentoDocEletronicoDAO(MonitoramentoDocEletronicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MonitoramentoDocEletronicoDAO getMonitoramentoDocEletronicoDao() {
		return (MonitoramentoDocEletronicoDAO) getDao();
	}

	/**
	 * Recupera uma instância de <code>MonitoramentoDocEletronico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MonitoramentoDocEletronico findById(java.lang.Long id) {
		return (MonitoramentoDocEletronico)super.findById(id);
	}
	
	@Override
	public MonitoramentoDocEletronico get(Long id) {
		return super.get(id);
	}
	
	public List<Map<String,Object>> findByListFatura(List<Long> idFaturaList, String tpDocumento){
		List<Map<String, Object>> list = getMonitoramentoDocEletronicoDao().findByListFatura(idFaturaList, tpDocumento);
		for (Map<String, Object> map: list) {
			if (ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(map.get("tpDocumento"))) {
				Map<String, Object> nfeInfs = executeMontarInfNse((Long)map.get("idConhecimento"), (Long)map.get("idMonitoramentoDocEletronic"));
				map.put("nfeInfs", nfeInfs);
			} else if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(map.get("tpDocumento"))) {
				Map<String, Object> nfeInfs = executeMontarInfNte((Long)map.get("idConhecimento"), (Long)map.get("idMonitoramentoDocEletronic"));
				map.put("nfeInfs", nfeInfs);
			}
			Map<String, Object> mapComplemento = conhecimentoDAO.findComplementosXMLCTE((Long)map.get("idConhecimento"));
			map.put("complementoXML", mapComplemento);
		}
		return list;
	}
	
	public List<Object[]> findByIdFatura(Long idFatura) {
		return getMonitoramentoDocEletronicoDao().findByIdFatura(idFatura);
	}
	
	public List<String> findNrChaveByIdFatura(Long idFatura) {
		return getMonitoramentoDocEletronicoDao().findNrChaveByIdFatura(idFatura);
	}
	
	public String findNrChaveByIdDoctoServico(Long idDoctoServico) {
        return getMonitoramentoDocEletronicoDao().findNrChaveByIdDoctoServico(idDoctoServico);
    }
	
	public boolean findExistsConhecimentoEletronicoByCriteria(Map<String, Object> criteria){
		boolean isCte = (Boolean)criteria.get("isCte");
		boolean isNte = (Boolean)criteria.get("isNte");
		List<String> tpDocumentoServicoList = new ArrayList<String>();
		if(isCte){
		      tpDocumentoServicoList.add(CTE);
		}
		if(isNte){
		      tpDocumentoServicoList.add(NTE);
		}
		criteria.put("tpDocumentoServicoList", tpDocumentoServicoList);
		
		return getMonitoramentoDocEletronicoDao().findExistsConhecimentoEletronicoByCriteria(criteria);
	}
	
	public List<Map<String, Object>> findNteToReportByListFatura(List<Long> faturas) {
		return getMonitoramentoDocEletronicoDao().findNteToReportByListFatura(faturas);
	}
	
	public List<Map<String, Object>> findNseToReportByListFatura(List<Long> faturas) {
		return getMonitoramentoDocEletronicoDao().findNseToReportByListFatura(faturas);
	}
	
	private TypedFlatMap parseXmlCte(CartaCorrecaoCampo cartaCorrecao, byte[] documentData) {
		TypedFlatMap tfmXmlCteOrigput = new TypedFlatMap();
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(new ByteArrayInputStream(documentData));
			XPath xPath =  XPathFactory.newInstance().newXPath();
			
			String key = cartaCorrecao.getChave();
			String expression = cartaCorrecao.getxPath();
			String value = xPath.compile(expression).evaluate(xmlDocument);
			
			tfmXmlCteOrigput.put("sgCampo", configuracoesFacadeImpl.getMensagem(key));
			tfmXmlCteOrigput.put("sgValorCTE", value);
		} catch (Throwable t) {
			throw new InfrastructureException(t);
		}
		
		return tfmXmlCteOrigput;
	}
	
	public List<TypedFlatMap> findXmlByIdDoctoServico(Long idDoctoServico) {
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		
		TBDatabaseInputCTE tbDatabaseInputCTE = integracaoNDDigitalService.findXmlByIdDoctoServico(idDoctoServico);
		for(CartaCorrecaoCampo c : CartaCorrecaoCampo.values()){
			list.add(parseXmlCte(c, tbDatabaseInputCTE.getDocumentData()));
		}
		return list;
	}
	
	public void executeRegerarCTE(Map<String,Object> bean){
		Long idDoctoServico = (Long)bean.get("idDoctoServico");
		Long idIETomador = (Long)bean.get("idIETomador");
		Long idIERemetente = (Long)bean.get("idIERemetente");
		Long idIEDestinatario = (Long)bean.get("idIEDestinatario");
		Long idIEExpedidor = (Long)bean.get("idIEExpedidor");
		Long idIERecebedor = (Long)bean.get("idIERecebedor");

		DoctoServico doctoServico = doctoServicoService.findByIdWithRemetenteDestinatario(idDoctoServico);
		if( idIETomador != null ){
			InscricaoEstadual ieTomador = inscricaoEstadualService.findById(idIETomador);
			DevedorDocServ devedorDocServ = devedorDocServService.findDevedorByDoctoServico(idDoctoServico);
			devedorDocServ.setInscricaoEstadual(ieTomador);
			devedorDocServService.store(devedorDocServ);
		}
		
		if( idIERemetente != null ){
			InscricaoEstadual ieRemetente = inscricaoEstadualService.findById(idIERemetente);
			doctoServico.setInscricaoEstadualRemetente(ieRemetente);
		}
		if( idIEDestinatario != null ){
			InscricaoEstadual ieDestinatario = inscricaoEstadualService.findById(idIEDestinatario);
			doctoServico.setInscricaoEstadualDestinatario(ieDestinatario);
		}
		
		if( idIEExpedidor != null ){
			InscricaoEstadual ieExpedidor = inscricaoEstadualService.findById(idIEExpedidor);
			if( doctoServico.getClienteByIdClienteRedespacho() != null ){
				DadosComplemento dcIeRedespacho = dadosComplementoService.findByIdConhecimentoDocServico(idDoctoServico, ConstantesExpedicao.IE_REDESPACHO);
				if( dcIeRedespacho != null ){
					dcIeRedespacho.setDsValorCampo(ieExpedidor.getNrInscricaoEstadual());
					dadosComplementoService.store(dcIeRedespacho);
				}
			}
		}
		if( idIERecebedor != null ){
			InscricaoEstadual ieRecebedor = inscricaoEstadualService.findById(idIERecebedor);
			if( doctoServico.getClienteByIdClienteConsignatario() != null ){
				DadosComplemento dcIeConsignatario = dadosComplementoService.findByIdConhecimentoDocServico(idDoctoServico, ConstantesExpedicao.IE_CONSIGNATARIO);
				if( dcIeConsignatario != null ){
					dcIeConsignatario.setDsValorCampo(ieRecebedor.getNrInscricaoEstadual());
					dadosComplementoService.store(dcIeConsignatario);
				}
			}
		}

		parcelaDoctoServicoService.removeByIdDoctoServico(idDoctoServico, false);
		valorCustoService.removeByIdDoctoServico(idDoctoServico, false);
		
		doctoServicoService.store(doctoServico);
				
		Map<String,Object> dadosCtrc = new HashMap<String, Object>();
		dadosCtrc.put("idDoctoServico", doctoServico.getIdDoctoServico());
		dadosCtrc.put("nrConhecimento", doctoServico.getNrDoctoServico());
		dadosCtrc.put("dtPrevistaEntrega", doctoServico.getDtPrevEntrega());
		dadosCtrc.put("nrDiasPrevEntrega", doctoServico.getNrDiasPrevEntrega());
		dadosCtrc.put("tpDoctoServico", doctoServico.getTpDocumentoServico());
		calcularFreteService.executeCalculoFrete(dadosCtrc);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MonitoramentoDocEletronico monitoramentoDocEletronico) {
		return super.store(monitoramentoDocEletronico);
	}
	
	public MonitoramentoDocEletronico findMonitoramentoDocEletronicoByIdDoctoServicoAndTpSituacaoDocumento(Long idDoctoServico,
			String tpSituacaoDocumento){
		
		return getMonitoramentoDocEletronicoDao()
					.findMonitoramentoDocEletronicoByIdDoctoServicoAndTpSituacaoDocumento(idDoctoServico, tpSituacaoDocumento);	
	}
	
	public void executeMonitoramentoDocEletronicoEmitidosAutorizados(){
	    List<Long> idsFiliaisEnvioNotFis = getIdsFilaisFedex();
	    if (CollectionUtils.isNotEmpty(idsFiliaisEnvioNotFis)) {
	        List<MonitoramentoDocEletronico> listMonitoramentos = getMonitoramentoDocEletronicoDao().findMonitoramentoDocEletronicoEmitidosAutorizados(parametroGeralService.findSimpleConteudoByNomeParametro(DT_INICIO_NOTFIS_RPST_FDX).toString(), idsFiliaisEnvioNotFis);
	        if(CollectionUtils.isNotEmpty(listMonitoramentos)){
	            for(MonitoramentoDocEletronico mde: listMonitoramentos){
	                EventoDocumentoServico eds = getEventoEmissaoDocumentoServico(mde.getDoctoServico().getEventoDocumentoServicos());
	                if(eds!= null){
	                    EventoDocumentoServicoDMN eventoDocumentoServicoDMN = new EventoDocumentoServicoDMN();
	                    eventoDocumentoServicoDMN.setIdDoctoServico(mde.getDoctoServico().getIdDoctoServico());
	                    eventoDocumentoServicoDMN.setIdFilialEvento(eds.getFilial().getIdFilial());
	                    eventoDocumentoServicoDMN.setCdEvento(eds.getEvento().getCdEvento());
	                    eventoDocumentoServicoDMN.setIdFilialOrigem(mde.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial());
	                    eventoDocumentoServicoDMN.setIdFilialDestino(mde.getDoctoServico().getFilialByIdFilialDestino().getIdFilial());
	                    
	                    enviaMdsEmissaoRpsCte(eventoDocumentoServicoDMN, mde);
	                }
	            }
	        }
	    }
	}
	
    private List<Long> getIdsFilaisFedex() {
    	
    	List<Integer> cdsFiliais = new ArrayList<Integer>();
    	cdsFiliais.addAll(asListInteger((String) configuracoesFacade.getValorParametro(CD_INTEGRA_NOTFIS_FDX)));
        cdsFiliais.addAll(filialService.findCodFiliaisIntegraNotfis());
        
        if (CollectionUtils.isNotEmpty(cdsFiliais)) {
            List<Filial> filiaisEnvioNotFis = filialService.findFiliaisByCodsFilial(cdsFiliais);

            List<Long> idsFiliaisEnvioNotFis = new ArrayList<Long>();
            for (Filial filial : filiaisEnvioNotFis) {
                idsFiliaisEnvioNotFis.add(filial.getIdFilial());
            }
            return idsFiliaisEnvioNotFis;
        } else {
            return Collections.emptyList();
        }
    }
    
	private EventoDocumentoServico getEventoEmissaoDocumentoServico(List <EventoDocumentoServico> listEventoDocumentoServico) {
	    for(EventoDocumentoServico eventoDocumentoServico: listEventoDocumentoServico){
	        if(ConstantesEventosDocumentoServico.CD_EVENTO_DOCUMENTO_EMITIDO.equals(eventoDocumentoServico.getEvento().getCdEvento())){
	            return eventoDocumentoServico;
	        }
	    }
	    return null;
	}
	
    public void generateNotFisFedEXAgainByMonitoramentoDocEletronico(Long idMonitoramentoDocEletronico) {
        // validar existencia de evento de documento para o monitoramento eletronico informado 
        // Nao existindo devolver uma mensagem de negocio
    	String paramEventosEdiFedex = (String) parametroGeralService.findConteudoByNomeParametro(EVENTOS_EDI_FDX, false);
    	List<Short> cdsEventosEdiFedex = new ArrayList<Short>();
    	for(String cdEvento : Arrays.asList(paramEventosEdiFedex.split(","))){
    		cdsEventosEdiFedex.add(Short.valueOf(cdEvento));
    	}
    	
        Integer count = getMonitoramentoDocEletronicoDao().countReSendEventoDocumentoByControleCarga(idMonitoramentoDocEletronico, cdsEventosEdiFedex);
        if (count == null || count == 0) {
            throw new BusinessException("LMS-05416");
        }
        // buscar a lista de eventos existentes para os documentos do controle de carga
        List<EventoDocumentoServicoDMN> eventos = getMonitoramentoDocEletronicoDao().getReSendEventoDocumentoByControleCarga(idMonitoramentoDocEletronico, cdsEventosEdiFedex);
        // enviar lista de documentos para reenvio
        this.executeEnvioNotFisFedex(eventos);
    }
	
	public void executeEnvioNotFisFedex(List<EventoDocumentoServicoDMN> eventoDocumentoServicoDMNlist) {
	    if (CollectionUtils.isNotEmpty(eventoDocumentoServicoDMNlist)) {
	        for (EventoDocumentoServicoDMN evento : eventoDocumentoServicoDMNlist) {
	            executeEnvioNotFisFedex(evento);
	        }
	    }
	}
	
	public boolean findBlEnviaFilaTimerNotfis(Long idDoctoServico) {
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		
		return doctoServico.getFilialLocalizacao().getIdFilial().equals(doctoServico.getFilialByIdFilialDestino().getIdFilial()) &&
				"S".equals(configuracoesFacade.getValorParametro(doctoServico.getFilialLocalizacao().getIdFilial(), PARAMETRO_FILIAL_INTEGRA_NOTFIS_FAAV));
	}
	
	public void executeEnvioNotFisFedex(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
		MonitoramentoDocEletronico mde = this.findMonitoramentoDocEletronicoByIdDoctoServico(eventoDocumentoServicoDMN.getIdDoctoServico());

		if(mde!=null) {
		    enviaMdsEmissaoRpsCte(eventoDocumentoServicoDMN, mde);
		    
			if (!Boolean.TRUE.equals(mde.getDoctoServico().getBlFluxoSubcontratacao())){
			    enviaMdsSaidaPortariaCte(eventoDocumentoServicoDMN, mde);
			    enviaLinehaulCargaCompartilhadaSaidaPortariaCte(eventoDocumentoServicoDMN, mde);
			    enviaMdsNoTerminal(eventoDocumentoServicoDMN, mde);
			    enviaContingencia(eventoDocumentoServicoDMN, mde);
			}
		    
		}
	}
	
	private void enviaContingencia(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, MonitoramentoDocEletronico mde) {
	    if(ConstantesEventosDocumentoServico.CD_EVENTO_SAIDA_NA_PORTARIA_ENTREGA.equals(eventoDocumentoServicoDMN.getCdEvento())
	            && "S".equals((String) configuracoesFacade.getValorParametro(eventoDocumentoServicoDMN.getIdFilialEvento(), INTEGRA_NOTFIS_FAAV))){
	        
	        JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, null, "0"));
	        jmsMessageSender.addHeader(FILE_NAME, buildFileNameMds(mde));
	        integracaoJmsService.storeMessage(jmsMessageSender);
	    }
	}
	
	private void enviaMdsNoTerminal(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, MonitoramentoDocEletronico mde) {
	    String tpDocumentoServico = mde.getDoctoServico().getTpDocumentoServico().getValue();
	    
	    if(mde.getDoctoServico().getFilialByIdFilialDestino().getIdFilial().equals(mde.getDoctoServico().getFilialLocalizacao().getIdFilial())
	            && validateCdEventoFimDescargLiberaAlteraManFilLocal(eventoDocumentoServicoDMN)
	            && "S".equals((String) configuracoesFacade.getValorParametro(mde.getDoctoServico().getFilialByIdFilialDestino().getIdFilial(), INTEGRA_NOTFIS_FAAV))
	            && ConstantesEventosDocumentoServico.CD_LOCALIZACAO_MERCADORIA_NO_TERMINAL.equals(mde.getDoctoServico().getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())){
	        
	    	controleCargaService.gerarManifestoFedex(eventoDocumentoServicoDMN.getIdDoctoServico(), mde.getDoctoServico().getFilialByIdFilialDestino().getIdFilial());
	    	
	        if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)){
	            mde.setNrChaveEletrRpstFdx(ChaveRPStGenerator.gerarChaveAcesso((Conhecimento) mde.getDoctoServico()));
	        }
	        JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, null, "0"));
	        jmsMessageSender.addHeader(FILE_NAME, buildFileNameMds(mde));
	        integracaoJmsService.storeMessage(jmsMessageSender);
	        
	        this.store(mde);
	    }
	    
	    if(mde.getDoctoServico().getFilialByIdFilialDestino().getIdFilial().equals(mde.getDoctoServico().getFilialLocalizacao().getIdFilial())
	            && ConstantesEventosDocumentoServico.CD_EVENTO_OCORRENCIA_ENTREGA.equals(eventoDocumentoServicoDMN.getCdEvento())
	            && "S".equals((String) configuracoesFacade.getValorParametro(mde.getDoctoServico().getFilialByIdFilialDestino().getIdFilial(), INTEGRA_NOTFIS_FAAV))
	            && ConstantesEventosDocumentoServico.CD_LOCALIZACAO_MERCADORIA_NO_TERMINAL.equals(mde.getDoctoServico().getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())){
	    	
	    	OcorrenciaEntrega ocorrenciaEntrega = ocorrenciaEntregaService.findOcorrenciaEntregaByCodigoTipo(eventoDocumentoServicoDMN.getCdOcorrenciaEntrega());
			if (("R".equals(ocorrenciaEntrega.getTpOcorrencia().getValue()) || "N".equals(ocorrenciaEntrega.getTpOcorrencia().getValue())) && ocorrenciaEntrega.getBlOcasionadoMercurio()) {

				controleCargaService.gerarManifestoFedex(eventoDocumentoServicoDMN.getIdDoctoServico(), mde.getDoctoServico().getFilialByIdFilialDestino().getIdFilial());

				if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)) {
					mde.setNrChaveEletrRpstFdx(ChaveRPStGenerator.gerarChaveAcesso((Conhecimento) mde.getDoctoServico()));
				}
				JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, null, "0"));
				jmsMessageSender.addHeader(FILE_NAME, buildFileNameMds(mde));
				integracaoJmsService.storeMessage(jmsMessageSender);

				this.store(mde);
			}
	    }
	    
	}

	private boolean validateCdEventoFimDescargLiberaAlteraManFilLocal(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
		return ConstantesEventosDocumentoServico.CD_EVENTO_FIM_DE_DESCARGA.equals(eventoDocumentoServicoDMN.getCdEvento()) ||
			ConstantesEventosDocumentoServico.CD_EVENTO_LIBERACAO.equals(eventoDocumentoServicoDMN.getCdEvento()) ||
			ConstantesEventosDocumentoServico.CD_EVENTO_ALTERACAO_MAN_FILIAL_LOCALIZ.equals(eventoDocumentoServicoDMN.getCdEvento());
	}
	
	private void enviaMdsSaidaPortariaCte(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, MonitoramentoDocEletronico mde){
	    String tpDocumentoServico = mde.getDoctoServico().getTpDocumentoServico().getValue();
	    if(ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumentoServico)){
	        Manifesto lastManifestoViagem = manifestoService.findLastManifestoViagemByIdDoctoServico (eventoDocumentoServicoDMN.getIdDoctoServico());
	        if(validateFilialParametrizado(eventoDocumentoServicoDMN, lastManifestoViagem)){
	            JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, lastManifestoViagem, "0"));
	            jmsMessageSender.addHeader(FILE_NAME, buildFileNameMdsSaidaPortariaCte(mde, lastManifestoViagem));
	            integracaoJmsService.storeMessage(jmsMessageSender);
	        }
	    }
	}
	
   private void enviaLinehaulCargaCompartilhadaSaidaPortariaCte(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, MonitoramentoDocEletronico mde) {
       String tpDocumentoServico = mde.getDoctoServico().getTpDocumentoServico().getValue();
       
       if(ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumentoServico)){
    	   
            Manifesto manifesto = manifestoService.findManifestoViagemByIdDoctoServicoFilialEvento(eventoDocumentoServicoDMN.getIdDoctoServico(),
                    eventoDocumentoServicoDMN.getIdFilialEvento());
           
            if(validateProprietarioManifestoDocto(eventoDocumentoServicoDMN, manifesto)){
                // LMSA-7369
                JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, manifesto, "1"));
                jmsMessageSender.addHeader(FILE_NAME, buildFileNameLinehaul(mde,manifesto));
                integracaoJmsService.storeMessage(jmsMessageSender);
            }

            if (validateFilialEventoOrigemManifestoCargaCompartilhada(eventoDocumentoServicoDMN, manifesto)) {
                JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, manifesto,"2"));
                jmsMessageSender.addHeader(FILE_NAME, buildFileNameCargaCompartilhada(mde,manifesto));
                integracaoJmsService.storeMessage(jmsMessageSender);
            }
        }
    }
	
	private void enviaMdsEmissaoRpsCte(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, MonitoramentoDocEletronico mde) {
	    String tpDocumentoServico = mde.getDoctoServico().getTpDocumentoServico().getValue();
	    
	    if (ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumentoServico) &&
	            Boolean.TRUE.equals(mde.getDoctoServico().getBlFluxoSubcontratacao())){
	    	enviaCteFluxoSubcontratacao(eventoDocumentoServicoDMN,mde);
	    }else{
	    	if(validateEventoEmissaoFilialParametrizado(eventoDocumentoServicoDMN) && validateEmissaoAutorizacao(mde)){
		        if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)){
		            mde.setNrChaveEletrRpstFdx(ChaveRPStGenerator.gerarChaveAcesso((Conhecimento) mde.getDoctoServico()));
		        }
		        JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, null, "3"));
		        jmsMessageSender.addHeader(FILE_NAME, buildFileNameMds(mde));
		        integracaoJmsService.storeMessage(jmsMessageSender);
		        
		        this.store(mde);
		    }
		    
		    if(validateEventoEmissaoFilialParametrizadoLocalizacaoNoTerminal(eventoDocumentoServicoDMN, mde.getDoctoServico()) && validateEmissaoAutorizacao(mde)){
		    	
		    	controleCargaService.gerarManifestoFedex(eventoDocumentoServicoDMN.getIdDoctoServico(), eventoDocumentoServicoDMN.getIdFilialDestino());
		    	
		        if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)){
		            mde.setNrChaveEletrRpstFdx(ChaveRPStGenerator.gerarChaveAcesso((Conhecimento) mde.getDoctoServico()));
		        }
		        JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, null, "3"));
		        jmsMessageSender.addHeader(FILE_NAME, buildFileNameMds(mde));
		        integracaoJmsService.storeMessage(jmsMessageSender);
		        
		        this.store(mde);
		    }
	    }
	    

	}
	
	
	private void enviaCteFluxoSubcontratacao(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, MonitoramentoDocEletronico mde){
	    if (validateEmissaoAutorizacao(mde) 
	    		&& ConstantesEventosDocumentoServico.CD_EVENTO_DOCUMENTO_EMITIDO.equals(eventoDocumentoServicoDMN.getCdEvento())){
	    	JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_NOTFIS, buildDto(mde, null, TP_REDESPACHO_SUBCONTRATACAO));
	        jmsMessageSender.addHeader(FILE_NAME, buildFileNameSubcontratacao(mde));
	        integracaoJmsService.storeMessage(jmsMessageSender);
	    }
	}
	
	
	private boolean validateEventoEmissaoFilialParametrizadoLocalizacaoNoTerminal(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, DoctoServico doctoServico) {
	    return eventoDocumentoServicoDMN.getIdFilialOrigem().equals(eventoDocumentoServicoDMN.getIdFilialEvento())
	            && eventoDocumentoServicoDMN.getIdFilialOrigem().equals(eventoDocumentoServicoDMN.getIdFilialDestino())
	            && ConstantesEventosDocumentoServico.CD_EVENTO_DOCUMENTO_EMITIDO.equals(eventoDocumentoServicoDMN.getCdEvento())
	            && "S".equals((String) configuracoesFacade.getValorParametro(eventoDocumentoServicoDMN.getIdFilialDestino(), INTEGRA_NOTFIS_FAAV))
	            && ConstantesEventosDocumentoServico.CD_LOCALIZACAO_MERCADORIA_NO_TERMINAL.equals(doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
	}	
	
	private boolean validateEventoEmissaoFilialParametrizado(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
	    if(eventoDocumentoServicoDMN.getIdFilialOrigem().equals(eventoDocumentoServicoDMN.getIdFilialEvento())
	            && eventoDocumentoServicoDMN.getIdFilialOrigem().equals(eventoDocumentoServicoDMN.getIdFilialDestino())
	            && ConstantesEventosDocumentoServico.CD_EVENTO_DOCUMENTO_EMITIDO.equals(eventoDocumentoServicoDMN.getCdEvento())){
	        Filial filial = filialService.findById(eventoDocumentoServicoDMN.getIdFilialDestino());
	        List<String> cdsFiliais = asList((String)configuracoesFacade.getValorParametro(CD_INTEGRA_NOTFIS_FDX));
	        return CollectionUtils.isNotEmpty(cdsFiliais) && cdsFiliais.contains(filial.getCodFilial().toString());
	    }
	    return false;
	}	
	
		
	private boolean validateEmissaoAutorizacao(MonitoramentoDocEletronico mde) {
	    String tpDocumentoServico = mde.getDoctoServico().getTpDocumentoServico().getValue();
	    return ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_AUTORIZADO.equals(mde.getTpSituacaoDocumento().getValue()) 
	            && ((ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico) 
	                    && mde.getNrChaveEletrRpstFdx() == null) 
	                    || ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumentoServico));
	}
	
	
	// LMSA-6253
	private boolean validateFilialParametrizado(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, Manifesto manifesto) {
		boolean result = eventoDocumentoServicoDMN != null && manifesto != null;
		result = result && manifesto.getControleCarga() != null;
		result = result && manifesto.getControleCarga().getIdControleCarga() != null;
		result = result && ConstantesEventosDocumentoServico.CD_EVENTO_SAIDA_NA_PORTARIA.equals(eventoDocumentoServicoDMN.getCdEvento());
		
		if (result) {
		    List<FilialRotaCc> filialRotaCcs = filialRotaCcService.findByControleCarga(manifesto.getControleCarga().getIdControleCarga());
		    Filial filial = null;
		    Byte nrOrdem = null;
		    
		    for (FilialRotaCc filialRotaCc:filialRotaCcs){
		        if (filialRotaCc.getFilial().getIdFilial().equals(eventoDocumentoServicoDMN.getIdFilialEvento())){
		            nrOrdem = filialRotaCc.getNrOrdem();
		            continue;
		        }
		        
		        Long prevOrder = filialRotaCc.getNrOrdem().longValue()-1;
		        if (nrOrdem != null && nrOrdem.equals(new Byte(prevOrder.byteValue()))){
		            filial = filialRotaCc.getFilial();
		            break;
		        }
		    }
		    
			// invalida o 'result' para associar o teste final 
			result = false;
		    
		    if (filial != null){
		        List<String> cdsFiliais = asList((String)configuracoesFacade.getValorParametro(CD_INTEGRA_NOTFIS_FDX));
		        result = CollectionUtils.isNotEmpty(cdsFiliais) && cdsFiliais.contains(filial.getCodFilial().toString());
		    }
		     
		}
		
	    return result;
	}
	
	// LMSA-6253
	private boolean validateProprietarioManifestoDocto(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, Manifesto manifesto) {
		boolean valido = eventoDocumentoServicoDMN != null && manifesto != null;
		valido = valido && manifesto.getControleCarga() != null;
		valido = valido && validateProprietarioFedex(manifesto.getControleCarga());
		valido = valido && validateManifestoFedex(eventoDocumentoServicoDMN, manifesto); 
		return valido;
	}
	
	// LMSA6322
	private boolean validateFilialEventoOrigemManifestoCargaCompartilhada(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, Manifesto manifesto) {
		// valido se parametros necessarios para as verificacoes ter sido informados
		boolean cargaCompartilhadaTNTFedEX = eventoDocumentoServicoDMN != null && manifesto != null;
		cargaCompartilhadaTNTFedEX = cargaCompartilhadaTNTFedEX && eventoDocumentoServicoDMN.getIdFilialEvento() != null;
		cargaCompartilhadaTNTFedEX = cargaCompartilhadaTNTFedEX && manifesto.getFilialByIdFilialOrigem() != null;
		
		// permanece valido se filial do evento eh igual filial de origem do manifesto
		cargaCompartilhadaTNTFedEX = cargaCompartilhadaTNTFedEX && eventoDocumentoServicoDMN.getIdFilialEvento().equals(manifesto.getFilialByIdFilialOrigem().getIdFilial());
		
		// permanece valido se tipo de carga compartilhada da solicitacao de contratacao do controle de carga for 'C1'
		cargaCompartilhadaTNTFedEX = cargaCompartilhadaTNTFedEX && manifesto.getControleCarga() != null;
		cargaCompartilhadaTNTFedEX = cargaCompartilhadaTNTFedEX && manifesto.getControleCarga().getSolicitacaoContratacao() != null;
		cargaCompartilhadaTNTFedEX = cargaCompartilhadaTNTFedEX && manifesto.getControleCarga().getSolicitacaoContratacao().getTpCargaCompartilhada() != null;
		cargaCompartilhadaTNTFedEX = cargaCompartilhadaTNTFedEX && "C1".equals(manifesto.getControleCarga().getSolicitacaoContratacao().getTpCargaCompartilhada().getValue());
		cargaCompartilhadaTNTFedEX = cargaCompartilhadaTNTFedEX && ConstantesEventosDocumentoServico.CD_EVENTO_SAIDA_NA_PORTARIA.equals(eventoDocumentoServicoDMN.getCdEvento());
		return cargaCompartilhadaTNTFedEX;
	}
	
	
	private boolean validateProprietarioFedex(ControleCarga controleCarga) {
		List<String> cnpjProprietarios = asList((String)configuracoesFacade.getValorParametro(CNPJ_PROPRIETARIO_FDX));
		return CollectionUtils.isNotEmpty(cnpjProprietarios) && cnpjProprietarios.contains(controleCarga.getProprietario().getPessoa().getNrIdentificacao().substring(0, 8));
	}
	
	private boolean validateManifestoFedex(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, Manifesto manifesto) {
		List<String> parametroModal = asList((String)configuracoesFacade.getValorParametro(MODAL_MANIFESTO_FDX));
		return eventoDocumentoServicoDMN.getIdFilialEvento().equals(manifesto.getFilialByIdFilialOrigem().getIdFilial())&&
				CollectionUtils.isNotEmpty(parametroModal) && 
				parametroModal.contains(manifesto.getTpModal().getValue());
	}
	
	private List<String> asList(String valorParametro) {
		if(valorParametro == null){
			return Collections.emptyList();
		}
		
		return Arrays.asList(valorParametro.split(";"));
	}
	
    private List<Integer> asListInteger(String valorParametro) {
        if (valorParametro == null) {
            return Collections.emptyList();
        }

        List<Integer> retorno = new ArrayList<Integer>();
        List<String> stringList = Arrays.asList(valorParametro.split(";"));
        for (String string : stringList) {
            retorno.add(Integer.valueOf(string));
        }
        return retorno ;
    }
    
    private String buildFileNameLinehaul(MonitoramentoDocEletronico mde, Manifesto manifesto) {
        StringBuilder fileName = new StringBuilder();

        fileName.append("RI");
        fileName.append(manifesto.getFilialByIdFilialOrigem().getCdFilialFedex().toString());
        fileName.append("_");
        fileName.append(manifesto.getManifestoViagemNacional().getNrManifestoOrigem());
        fileName.append("_");
        fileName.append(mde.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
        fileName.append(mde.getDoctoServico().getNrDoctoServico());

        return fileName.toString();
    }
	
    private String buildFileNameMds(MonitoramentoDocEletronico mde){
        StringBuilder fileName = new StringBuilder();
        fileName.append(mde.getDoctoServico().getFilialByIdFilialDestino().getCdFilialFedex().toString());
        fileName.append("_");
        fileName.append(mde.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
        fileName.append(mde.getDoctoServico().getNrDoctoServico());
        
        return fileName.toString();
    }
    
    private String buildFileNameSubcontratacao(MonitoramentoDocEletronico mde){
    	
        StringBuilder fileName = new StringBuilder();
        fileName.append(mde.getDoctoServico().getFluxoFilial().getEmpresaSubcontratada().getNrIdentificacao());
        fileName.append("_");
        fileName.append(mde.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
        fileName.append(mde.getDoctoServico().getNrDoctoServico());
        
        return fileName.toString();
    }
    

	private String buildFileNameMdsSaidaPortariaCte(MonitoramentoDocEletronico mde, Manifesto manifesto){
        StringBuilder fileName = new StringBuilder();
        fileName.append(manifesto.getFilialByIdFilialDestino().getCdFilialFedex().toString());
        fileName.append("_");
        fileName.append(mde.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
        fileName.append(mde.getDoctoServico().getNrDoctoServico());
        
        return fileName.toString();
    }

	// LMSA-6322
	private String buildFileNameCargaCompartilhada(MonitoramentoDocEletronico mde, Manifesto manifesto){
        StringBuilder fileName = new StringBuilder();
        fileName.append("CC");
        fileName.append(manifesto.getFilialByIdFilialOrigem().getCdFilialFedex().toString());
        fileName.append("_");
        fileName.append(manifesto.getControleCarga().getNrControleCarga());
        fileName.append("_");
        fileName.append(mde.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
        fileName.append(mde.getDoctoServico().getNrDoctoServico());
        return fileName.toString();
    }
	
	private EventoDoctoServicoFedexDMN buildDto(MonitoramentoDocEletronico mde, Manifesto manifesto, String tpRedespacho) {
		EventoDoctoServicoFedexDMN dto = new EventoDoctoServicoFedexDMN();
		
		buildCabecalho(dto, mde, manifesto, tpRedespacho);
		dto.setRemetente(buildDadosPessoaEventoDoctoServico(
				getPessoaByCliente(mde.getDoctoServico().getClienteByIdClienteRemetente()), "REM", mde.getDoctoServico().getInscricaoEstadualRemetente()));
		
		// LMSA-7369
		dto.setDestinatario(setDadosAdicionaisDestinatario(
				buildDadosPessoaEventoDoctoServico(
						getPessoaByCliente(mde.getDoctoServico().getClienteByIdClienteDestinatario()), "DEST"), 
				mde,
				manifesto,
				tpRedespacho
				)
			);
		
		dto.setConsignatario(buildDadosPessoaEventoDoctoServico(
				getPessoaByCliente(mde.getDoctoServico().getClienteByIdClienteConsignatario()), "CON"));
		
		if (TIPOS_REDESPACHO_MDS.contains(dto.getTpRedespacho()) && alterouEnderecoEntrega(mde.getDoctoServico().getIdDoctoServico())){
			dto.setRedespacho(buildDadosPessoaEventoDoctoServicoRedespachoMDS(
					getPessoaByCliente(mde.getDoctoServico().getClienteByIdClienteDestinatario()), mde.getDoctoServico().getIdDoctoServico()));
		} else {
			dto.setRedespacho(buildDadosPessoaEventoDoctoServico(
					getPessoaByCliente(mde.getDoctoServico().getClienteByIdClienteRedespacho()), "RED"));
		}
		
		dto.setResponsavel(buildDadosPessoaEventoDoctoServico(mde.getDoctoServico().getFilialByIdFilialOrigem().getPessoa(), "RES"));
		
		dto.setListNotaFiscal(buildDadosNotaFiscal(dto, mde, tpRedespacho));
	
		return dto;
	}

	private void buildCabecalho(EventoDoctoServicoFedexDMN dto, MonitoramentoDocEletronico mde, Manifesto manifesto, String tpRedespacho) {
		dto.setNmFantasiaRemetente(mde.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getNmFantasia());
		dto.setNmFantasiaDestinatario(mde.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa().getNmFantasia());
		dto.setDhEmissao(mde.getDoctoServico().getDhEmissao());
		
		if ("CTE".equals(mde.getDoctoServico().getTpDocumentoServico().getValue())){
			dto.setNrChave(mde.getNrChave());
		}else{
			dto.setNrChave(mde.getNrChaveEletrRpstFdx());
		}
		
		dto.setQtCTEManifesto(0);
		dto.setTpRedespacho(tpRedespacho);
		
		if(manifesto!= null){
			ControleCarga controleCarga = manifesto.getControleCarga();
			
			if (!TP_REDESPACHO_SUBCONTRATACAO.equals(tpRedespacho)){
				dto.setDhEmissaoManifesto(manifesto.getDhEmissaoManifesto());
				dto.setNrManifesto(manifesto.getManifestoViagemNacional().getNrManifestoOrigem().toString());
				dto.setCnpjExpedidor(manifesto.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
				dto.setCnpjRecebedor(manifesto.getFilialByIdFilialDestino().getPessoa().getNrIdentificacao());
				
				dto.setAnoViagem(Long.valueOf(controleCarga.getDhGeracao().getYear()));
				dto.setNrViagem(controleCarga.getNrControleCarga());
				dto.setSgFilialOrigemViagem(controleCarga.getFilialByIdFilialOrigem().getSgFilial());
				dto.setSgFilialOrigemManifesto(manifesto.getFilialByIdFilialOrigem().getSgFilial());

				ControleTrecho controleTrecho = controleTrechoService.findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(
						controleCarga.getIdControleCarga(),
						manifesto.getFilialByIdFilialOrigem().getIdFilial() ,
						manifesto.getFilialByIdFilialDestino().getIdFilial());
				
				dto.setDhPrevisaoChegadaViagem(controleTrecho.getDhPrevisaoChegada());

				if (controleCarga.getMeioTransporteByIdSemiRebocado() != null){
					dto.setNrPlacaCarreta(controleCarga.getMeioTransporteByIdSemiRebocado().getNrIdentificador());
				}
				
				if (controleCarga.getMeioTransporteByIdTransportado() != null){
					dto.setNrPlacaVeiculo(controleCarga.getMeioTransporteByIdTransportado().getNrIdentificador());
				}
				dto.setCnpjMotorista(controleCarga.getMotorista().getPessoa().getNrIdentificacao());
				dto.setNmMotorista(controleCarga.getMotorista().getPessoa().getNmPessoa());
				
				dto.setCdFilialDestinoManifesto((String)conteudoParametroFilialService.findConteudoByNomeParametro(manifesto.getFilialByIdFilialDestino().getIdFilial(), "CD_FILIAL_FEDEX", false));
				dto.setCdFilialDestinoViagem((String)conteudoParametroFilialService.findConteudoByNomeParametro(controleCarga.getFilialByIdFilialDestino().getIdFilial(), "CD_FILIAL_FEDEX", false));
				dto.setQtManifestosViagem(Long.valueOf(controleCarga.getManifestos().size()));
			}
			
			dto.setQtCTEManifesto(manifesto.getManifestoViagemNacional().getNrCto());
			
		}
	}
	
	private PessoaNotaFiscalDocumentoServicoDMN buildDadosPessoaEventoDoctoServicoRedespachoMDS(Pessoa pessoa, Long idDoctoServico){
		PessoaNotaFiscalDocumentoServicoDMN pessoaDto = null;
		if(pessoa != null){
			pessoaDto = new PessoaNotaFiscalDocumentoServicoDMN();
			pessoaDto.setNmPessoa(pessoa.getNmPessoa());
			pessoaDto.setNrIdentificacao(pessoa.getNrIdentificacao());
			pessoaDto.setNrInscricaoEstadual(getInscricaoEstadualAtiva(pessoa.getInscricaoEstaduais()));
			pessoaDto.setTpPessoa(pessoa.getTpPessoa().getValue());
			EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa(); 
			if(enderecoPessoa != null) {
				buildEnderecoPessoaDtoMDS(idDoctoServico, pessoaDto, enderecoPessoa);
				buildDsTelefone(pessoa, pessoaDto, enderecoPessoa);
			}
		}
		return pessoaDto;
	}
	
	private boolean alterouEnderecoEntrega(Long idDoctoServico){
		return conhecimentoService.findById(idDoctoServico).getDsEnderecoEntrega() != null;
	}
	
	private void buildEnderecoPessoaDtoMDS(Long idDoctoServico,PessoaNotaFiscalDocumentoServicoDMN pessoaDto, EnderecoPessoa enderecoPessoa) {
		
		Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
		
		if(conhecimento.getTipoLogradouroEntrega() != null) enderecoPessoa.setTipoLogradouro(conhecimento.getTipoLogradouroEntrega());
		
		
		pessoaDto.setDsEndereco(buildDsEnderecoMDS(conhecimento));
		pessoaDto.setNmBairro(FormatUtils.removeAccents(conhecimento.getDsBairroEntrega()));
		pessoaDto.setNmMunicipio(FormatUtils.removeAccents(conhecimento.getMunicipioByIdMunicipioEntrega().getNmMunicipio()));
		pessoaDto.setNrCep(conhecimento.getNrCepEntrega());
		pessoaDto.setSgUnidadeFederativa(conhecimento.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa().getSgUnidadeFederativa());
	    pessoaDto.setDsIbge(buildCdIbgeMDS(conhecimento));
		pessoaDto.setDsComplemento(FormatUtils.removeAccents(conhecimento.getDsComplementoEntrega()));
			
	}
	
	private Pessoa getPessoaByCliente(Cliente cliente){
		if(cliente != null){
			return cliente.getPessoa();
		}
		return null;
	}
	
	private PessoaNotaFiscalDocumentoServicoDMN buildDadosPessoaEventoDoctoServico(Pessoa pessoa, String tpPessoa) {
		return buildDadosPessoaEventoDoctoServico(pessoa, tpPessoa, null);
	}
	
	private PessoaNotaFiscalDocumentoServicoDMN buildDadosPessoaEventoDoctoServico(Pessoa pessoa, String tpPessoa, InscricaoEstadual inscricaoEstadual) {
		PessoaNotaFiscalDocumentoServicoDMN pessoaDto = null;
		if(pessoa != null){
			pessoaDto = new PessoaNotaFiscalDocumentoServicoDMN();
			
			pessoaDto.setNmPessoa(FormatUtils.removeAccents(pessoa.getNmPessoa()));
			pessoaDto.setNrIdentificacao(pessoa.getNrIdentificacao());
			if(inscricaoEstadual != null){
				pessoaDto.setNrInscricaoEstadual(inscricaoEstadual.getNrInscricaoEstadual());
			}else{
				pessoaDto.setNrInscricaoEstadual(getInscricaoEstadualAtiva(pessoa.getInscricaoEstaduais()));
			}
			pessoaDto.setTpPessoa(pessoa.getTpPessoa().getValue());
			
			EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa(); 
			if(enderecoPessoa != null) {	
				pessoaDto.setDsEndereco(buildDsEndereco(enderecoPessoa));
				
				if (enderecoPessoa.getDsBairro() == null || enderecoPessoa.getDsBairro().length() < 2) {
				    pessoaDto.setNmBairro(parametroGeralService.findSimpleConteudoByNomeParametro(ConsParametroGeral.BAIRRO_PADRAO));
		        } else {
		            pessoaDto.setNmBairro(FormatUtils.removeAccents(enderecoPessoa.getDsBairro()));
		        }
				
				pessoaDto.setNmMunicipio(FormatUtils.removeAccents(enderecoPessoa.getMunicipio().getNmMunicipio()));
				pessoaDto.setNrCep(enderecoPessoa.getNrCep());
				pessoaDto.setSgUnidadeFederativa(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
				
				if(!"REM".equals(tpPessoa)) {	
					pessoaDto.setDsIbge(buildCdIbge(enderecoPessoa));
					pessoaDto.setDsComplemento(FormatUtils.removeAccents(enderecoPessoa.getDsComplemento()));
					
				}
				
				buildDsTelefone(pessoa, pessoaDto, enderecoPessoa);
			}
		}
		return pessoaDto;
	}
	
	private void buildDsTelefone(Pessoa pessoa, PessoaNotaFiscalDocumentoServicoDMN pessoaDto, EnderecoPessoa enderecoPessoa) {
		List<TelefoneEndereco> listTE = enderecoPessoa.getTelefoneEnderecos();
		for (TelefoneEndereco telefoneEndereco : listTE) {
			
			if(validaTpUsoTelefone(telefoneEndereco) && relacionaTpPessoaETpTelefone(pessoa, telefoneEndereco)){
				StringBuilder telefone = new StringBuilder();
				telefone.append(telefoneEndereco.getNrDdd());
				telefone.append(telefoneEndereco.getNrTelefone());
				pessoaDto.setDsTelefone(telefone.toString());
				break;
			} 
		}
	}
	
	@SuppressWarnings("rawtypes")
	private PessoaNotaFiscalDocumentoServicoDMN setDadosAdicionaisDestinatario(PessoaNotaFiscalDocumentoServicoDMN pessoaDto, MonitoramentoDocEletronico mde, Manifesto manifesto, String tipoRedespacho){
		if(pessoaDto != null && mde != null && mde.getDoctoServico() != null && mde.getDoctoServico().getDtPrevEntrega() != null){
			pessoaDto.setDataPrevEntrega(mde.getDoctoServico().getDtPrevEntrega().toString("yyyyMMdd"));
		}
		
		pessoaDto.setQtVolumesDoctoServico(mde.getDoctoServico().getQtVolumes());
		pessoaDto.setVlDoctoServico(mde.getDoctoServico().getVlMercadoria());
		pessoaDto.setPsDoctoServico(mde.getDoctoServico().getPsAferido());
		pessoaDto.setPsCubadoDoctoServico(mde.getDoctoServico().getPsCubadoReal());
		pessoaDto.setDhEmissao(mde.getDoctoServico().getDhEmissao());
		pessoaDto.setNrDoctoServico(mde.getDoctoServico().getNrDoctoServico().toString());
		pessoaDto.setCnpjEmissorDocto(mde.getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
		pessoaDto.setCnpjTomadorDocto(mde.getDoctoServico().getDevedorDocServs().get(0).getCliente().getPessoa().getNrIdentificacao());
		pessoaDto.setSerieDocto("0");
		
		// LMSA-7369
		// popular os campos de averbacao somente de tipo de redespacho for 0 ou espaço em branco
		if (pessoaDto != null && "1".equals(tipoRedespacho)) {
			AverbacaoDoctoServico averbacaoDoctoServico = averbacaoDoctoServicoService.findByIdDoctoServicoAverbado(mde.getDoctoServico().getIdDoctoServico());
			if (averbacaoDoctoServico != null) {
				pessoaDto.setNrProtocoloAverbacao(averbacaoDoctoServico.getNrProtocolo());
				pessoaDto.setNrAverbacao(averbacaoDoctoServico.getNrAverbacao());
			} else {
			    String nrAverbMDFE = parametroGeralService.findConteudoByNomeParametro("NUMERO_AVERB_MDFE", false).toString();
			    pessoaDto.setNrAverbacao(nrAverbMDFE);
			}
			
			DoctoServicoSeguros doctoServicoSeguro = doctoServicoSegurosService.findByIdDoctoServico(mde.getDoctoServico().getIdDoctoServico());
			if (doctoServicoSeguro == null) {
				List apolices = apoliceSeguroService.findSegValues(
						parametroGeralService.findSimpleConteudoByNomeParametro(ConsParametroGeral.TP_SEG_OBRIGATORIO).toString()
						);
				Filial filial = filialService.findById(manifesto.getFilialByIdFilialOrigem().getIdFilial());
				pessoaDto = valorizaDadosApoliceSeguroNoDestinatario(
						pessoaDto, 
						apolices, 
						filial.getPessoa().getTpPessoa().getValue(), 
						filial.getPessoa().getNrIdentificacao()
						);
			} else {
				List segurosCliente = seguroClienteService.findSegValues(
						mde.getDoctoServico().getIdDoctoServico(), 
						mde.getDoctoServico().getClienteByIdClienteRemetente().getIdCliente(),
						new DateTime()
						);
				
				pessoaDto = valorizaDadosSeguroClienteNoDestinatario(
						pessoaDto, 
						segurosCliente, 
						mde.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getTpPessoa().getValue(), 
						mde.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao()
						);
			}
		}

		return pessoaDto;
	}

	// LMSA-7369
	@SuppressWarnings("rawtypes")
	private PessoaNotaFiscalDocumentoServicoDMN valorizaDadosApoliceSeguroNoDestinatario(PessoaNotaFiscalDocumentoServicoDMN destinatario, List apolices, String tipoPessoaResponsavelSeguro, String nrIdentificacaoResponsavel) {
        if (apolices != null && !apolices.isEmpty()) {
        	Object[] apolice = (Object[]) apolices.get(0);
       		destinatario.setNmSeguradora(apolice[0] != null ? apolice[0].toString() : null);
        	destinatario.setNrApolice(apolice[1] != null ? apolice[1].toString() : null);
       		destinatario.setCnpjSeguradora(apolice[3] != null ? apolice[3].toString() : null);
       		
       		if ("J".equals(tipoPessoaResponsavelSeguro)) {
       			destinatario.setCnpjResponsavelSeguro(nrIdentificacaoResponsavel);
       		} else {
       			destinatario.setCpfResponsavelSeguro(nrIdentificacaoResponsavel);
       		}
        }
        return destinatario;
	}

	// LMSA-7369
	@SuppressWarnings("rawtypes")
	private PessoaNotaFiscalDocumentoServicoDMN valorizaDadosSeguroClienteNoDestinatario(PessoaNotaFiscalDocumentoServicoDMN destinatario, List segurosCliente, String tipoPessoaResponsavelSeguro, String nrIdentificacaoResponsavel) {
        if (segurosCliente != null && !segurosCliente.isEmpty()) {
        	Object[] seguro = (Object[]) segurosCliente.get(0);
       		destinatario.setNmSeguradora(seguro[0] != null ? seguro[0].toString() : null);
        	destinatario.setNrApolice(seguro[1] != null ? seguro[1].toString() : null);
       		destinatario.setCnpjSeguradora(seguro[3] != null ? seguro[3].toString() : null);
       		
       		if ("J".equals(tipoPessoaResponsavelSeguro)) {
       			destinatario.setCnpjResponsavelSeguro(nrIdentificacaoResponsavel);
       		} else {
       			destinatario.setCpfResponsavelSeguro(nrIdentificacaoResponsavel);
       		}
        }
        return destinatario;
	}

	
	private String buildCdIbge(EnderecoPessoa enderecoPessoa) {
		StringBuilder dsIbge = new StringBuilder();
		dsIbge.append(enderecoPessoa.getMunicipio().getUnidadeFederativa().getNrIbge().toString());
		dsIbge.append(enderecoPessoa.getMunicipio().getCdIbge().toString());
		return dsIbge.toString();
	}

	private String buildCdIbgeMDS(Conhecimento conhecimento){
		StringBuilder dsIbge = new StringBuilder();
		dsIbge.append(conhecimento.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa().getNrIbge().toString());
		dsIbge.append(conhecimento.getMunicipioByIdMunicipioEntrega().getCdIbge().toString());
		return dsIbge.toString();
	}

	private String buildDsEndereco(EnderecoPessoa enderecoPessoa) {
		StringBuilder dsEndereco = new StringBuilder();
		dsEndereco.append(FormatUtils.removeAccents(enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro().toString()));
		dsEndereco.append(" ");
		dsEndereco.append(FormatUtils.removeAccents(enderecoPessoa.getDsEndereco()));
		return dsEndereco.toString();
	}

	private String buildDsEnderecoMDS(Conhecimento conhecimento) {
		StringBuilder dsEndereco = new StringBuilder();
		// LMSA-7229
		dsEndereco.append(
				FormatUtils.removeAccents(
						conhecimento.getTipoLogradouroEntrega() != null && conhecimento.getTipoLogradouroEntrega().getDsTipoLogradouro() != null 
								? conhecimento.getTipoLogradouroEntrega().getDsTipoLogradouro().toString() : "")
				);
		dsEndereco.append(" ");
		dsEndereco.append(FormatUtils.removeAccents(conhecimento.getDsEnderecoEntrega()));
		dsEndereco.append(",");
		dsEndereco.append(conhecimento.getNrEntrega() != null ? conhecimento.getNrEntrega() : "");
		dsEndereco.append(" ");
		dsEndereco.append(FormatUtils.removeAccents(conhecimento.getDsComplementoEntrega() != null ? conhecimento.getDsComplementoEntrega() : ""));
		return dsEndereco.toString();
	}


	String getInscricaoEstadualAtiva(List<InscricaoEstadual> listIEPessoa){
		if(CollectionUtils.isNotEmpty(listIEPessoa)){
			for(InscricaoEstadual ie : listIEPessoa){
				if("A".equals(ie.getTpSituacao().getValue())){
					return ie.getNrInscricaoEstadual();
				}
			}
		}
		return null;
	}
	
	private boolean relacionaTpPessoaETpTelefone(Pessoa pessoa,
			TelefoneEndereco telefoneEndereco) {
		return ("J".equals(pessoa.getTpPessoa().getValue()) && "C".equals(telefoneEndereco.getTpTelefone().getValue())) 
				|| ("F".equals(pessoa.getTpPessoa().getValue()) && "R".equals(telefoneEndereco.getTpTelefone().getValue()));
	}

	private boolean validaTpUsoTelefone(TelefoneEndereco telefoneEndereco) {
		return "FO".equals(telefoneEndereco.getTpUso().getValue()) || "FF".equals(telefoneEndereco.getTpUso().getValue());
	}
		
	@SuppressWarnings("unchecked")
	private List<NotaFiscalDocumentoServicoDMN> buildDadosNotaFiscal(EventoDoctoServicoFedexDMN dto, MonitoramentoDocEletronico mde, String tpRedespacho ) {
		List<NotaFiscalConhecimento> listNotaFiscalConhecimento = notaFiscalConhecimentoService.
				findByConhecimento(mde.getDoctoServico().getIdDoctoServico());
		ArrayList<NotaFiscalDocumentoServicoDMN> listNotaFiscalDto = new ArrayList<NotaFiscalDocumentoServicoDMN>();
		
		BigDecimal vlTotalNotas = BigDecimalUtils.ZERO;
		BigDecimal psTotalNotasConhecimento = getPsTotalNotasConhecimento(listNotaFiscalConhecimento);
		BigDecimal psTotalNotas = BigDecimalUtils.ZERO;
		BigDecimal psTotalCubagemNotas = BigDecimalUtils.ZERO;
		Integer qtTotalVolumes = 0;
		BigDecimal vlTotalCobradoNotas = BigDecimalUtils.ZERO;
		BigDecimal vlTotalSeguro = BigDecimalUtils.ZERO;
		
		for(NotaFiscalConhecimento nfc : listNotaFiscalConhecimento) {
			NotaFiscalDocumentoServicoDMN nfDto = new NotaFiscalDocumentoServicoDMN();
			nfDto.setSerieNotaFiscal(nfc.getDsSerie());
			nfDto.setNumeroNotaFiscal(nfc.getNrNotaFiscal());
			nfDto.setDtNotaFiscal(nfc.getDtEmissao());
			nfDto.setDsEspecieVolume(nfc.getConhecimento().getDsEspecieVolume());
			nfDto.setVlIcms(nfc.getVlIcms());
			nfDto.setNrChave(nfc.getNrChave());

			nfDto.setVlTotal(nfc.getVlTotal());
			vlTotalNotas = vlTotalNotas.add(nfc.getVlTotal());
			
			// LMSA-6322
			Conhecimento cto = conhecimentoService.findById(nfc.getConhecimento().getIdDoctoServico());
			String auxDescricaoNaturezaProduto = (cto.getNaturezaProduto() != null ? 
					(cto.getNaturezaProduto().getDsNaturezaProduto() != null ?
							cto.getNaturezaProduto().getDsNaturezaProduto().toString() : null) : null);
			nfDto.setDsNaturezaProduto(auxDescricaoNaturezaProduto);
			
			if (!"0".equals(tpRedespacho)) {
				nfDto.setPsMercadoria(getPsMercadoriaProporcional(nfc, psTotalNotasConhecimento, mde.getDoctoServico().getPsAferido()));
			} else { // mds
				BigDecimal psInformado = null;
				String tpPesoFedex = (String) configuracoesFacade.getValorParametro(mde.getDoctoServico().getFilialByIdFilialDestino().getIdFilial(), "TIPO_PESO_NOTFIS_FDX");
				if ("FATURADO".equals(tpPesoFedex)) {
					psInformado = mde.getDoctoServico().getPsReferenciaCalculo();
				} else if ("DECLARADO".equals(tpPesoFedex)) {
					psInformado = mde.getDoctoServico().getPsReal();
				} else if ("AFERIDO".equals(tpPesoFedex)) {
					psInformado = mde.getDoctoServico().getPsAferido();
				} else if ("CUBADO".equals(tpPesoFedex)) {
					if (mde.getDoctoServico().getPsAforado() != null && mde.getDoctoServico().getPsAforado().compareTo(BigDecimal.ZERO) > 0) {
						psInformado = mde.getDoctoServico().getPsAforado();
					} else {
						psInformado = mde.getDoctoServico().getPsAferido();
					}
				} else {
					psInformado = mde.getDoctoServico().getPsAferido();
				}

				nfDto.setPsMercadoria(getPsMercadoriaProporcional(nfc, psTotalNotasConhecimento, psInformado));
			}
			
			psTotalNotas = psTotalNotas.add(nfDto.getPsMercadoria());
			
			nfDto.setQtdVolumes(nfc.getQtVolumes().intValue());
			qtTotalVolumes = qtTotalVolumes + nfc.getQtVolumes().intValue();
			
			nfDto.setListMercadoria(buildDadosMercadoriaNotaFiscal(nfc));
			
			Conhecimento conhecimento = conhecimentoService.findById(mde.getDoctoServico().getIdDoctoServico());
			if(conhecimento!= null && "DE".equals(conhecimento.getTpConhecimento().getValue())){
				nfDto.setTpDocumento("4");
			}else if("NTE".equals(mde.getDoctoServico().getTpDocumentoServico().getValue())){
				nfDto.setTpDocumento("3");
			}else if("00".equals(nfc.getTpDocumento()) || "99".equals(nfc.getTpDocumento())){
				nfDto.setTpDocumento("2");
			}else{
				nfDto.setTpDocumento("1");
			}
			
			listNotaFiscalDto.add(nfDto);
		}
		dto.setVlTotalNotas(vlTotalNotas);
		dto.setPsTotalNotas(psTotalNotas);
		dto.setPsTotalCubagemNotas(psTotalCubagemNotas);
		dto.setQtTotalVolumes(qtTotalVolumes);
		dto.setVlTotalCobradoNotas(vlTotalCobradoNotas);
		dto.setVlTotalSeguro(vlTotalSeguro);
		
		if("1".equals(dto.getTpRedespacho())){
			dto.getDestinatario().setQtNotasDocto(listNotaFiscalConhecimento.size());
		}else{
			dto.getDestinatario().setQtNotasDocto(0);
		}
		
		return listNotaFiscalDto;
	}
	
	private BigDecimal getPsTotalNotasConhecimento(List<NotaFiscalConhecimento> listNotaFiscalConhecimento){
		BigDecimal psTotalNotas = BigDecimalUtils.ZERO;
		for(NotaFiscalConhecimento nfc : listNotaFiscalConhecimento) {
			psTotalNotas = psTotalNotas.add(nfc.getPsMercadoria());
		}
		return psTotalNotas;
	}
	
	private BigDecimal getPsMercadoriaProporcional(NotaFiscalConhecimento nfc, BigDecimal psTotalNotas, BigDecimal psInformado){
		if(psInformado != null 
				&& BigDecimalUtils.gtZero(psInformado)
				&& BigDecimalUtils.gtZero(nfc.getPsMercadoria())){
			return nfc.getPsMercadoria().multiply(psInformado).divide(psTotalNotas, 3, RoundingMode.HALF_EVEN);
		}else{
			return nfc.getPsMercadoria();
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<MercadoriaNotaFiscalDMN> buildDadosMercadoriaNotaFiscal(NotaFiscalConhecimento nfc) {
		List<VolumeNotaFiscal> listVolumenotaFiscal = nfc.getVolumeNotaFiscais();
		ArrayList<MercadoriaNotaFiscalDMN> listMercadoriaDto = new ArrayList<MercadoriaNotaFiscalDMN>();
		for(VolumeNotaFiscal vnf : listVolumenotaFiscal) {
			MercadoriaNotaFiscalDMN mercadoriaDto = new MercadoriaNotaFiscalDMN();
			mercadoriaDto.setDsEspecieVolume(nfc.getConhecimento().getDsEspecieVolume());
			mercadoriaDto.setNrSequencia(vnf.getNrSequencia());
			mercadoriaDto.setNrVolumeEmbarque(vnf.getNrVolumeEmbarque());
			
			// LMSA-6322
			if(vnf.getDispositivoUnitizacao() != null) {
			    DispositivoUnitizacao du = dispositivoUnitizacaoService.findById(vnf.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
	            mercadoriaDto.setCodigoBarrasConsumidor(du != null ? du.getNrIdentificacao() : null);
			}
			
			listMercadoriaDto.add(mercadoriaDto);
		}
		return listMercadoriaDto;
	}
	
	public BloqueioLiberacaoAgendamentoFedexDTO executeEnriqueceBloqueioLiberacaoAgendamento(EventoDocumentoServicoDMN eventoDocumentoServicoDMN){
		
		
		MonitoramentoDocEletronico mde = this.findMonitoramentoDocEletronicoByIdDoctoServico(eventoDocumentoServicoDMN.getIdDoctoServico());
		DoctoServico doctoServico = doctoServicoService.findById(eventoDocumentoServicoDMN.getIdDoctoServico());
		AgendamentoDoctoServico agendamentoDoctoServico = agendamentoDoctoServicoService.findAgendamentoByIdDoctoServicoTpSituacao(doctoServico.getIdDoctoServico(), "A");
		
		if(!Boolean.TRUE.equals(eventoDocumentoServicoDMN.getBlOcorrenciaDoctoServicoManual()) &&
				agendamentoDoctoServico == null){
			return null;
		}
		
		String conteudoParametroFilial = (String) conteudoParametroFilialService.findConteudoByNomeParametro(doctoServico.getFilialByIdFilialDestino().getIdFilial(), "INTEGRA_BLOQ_AG_FDX", false);
		boolean blFilialParametrizada = conteudoParametroFilial != null && SIM.equals(conteudoParametroFilial);
		
		if(blFilialParametrizada && 
				mde != null && 
				("CTE".equals(doctoServico.getTpDocumentoServico().getValue()) || "NTE".equals(doctoServico.getTpDocumentoServico().getValue()))) {
			
			BloqueioLiberacaoAgendamentoFedexDTO bloqueioLiberacaoAgendamentoFedexDTO = new BloqueioLiberacaoAgendamentoFedexDTO();
			bloqueioLiberacaoAgendamentoFedexDTO.setNrChave(buildChaveBloqueioLiberacaoAgendamento(mde));
			bloqueioLiberacaoAgendamentoFedexDTO.setCNPJRemetente(doctoServico.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
			bloqueioLiberacaoAgendamentoFedexDTO.setNrDoctoServico(doctoServico.getNrDoctoServico().toString());
			bloqueioLiberacaoAgendamentoFedexDTO.setDtGeracaoBloqueioLiberacao(eventoDocumentoServicoDMN.getDhEvento().toString("yyyyMMdd"));
			bloqueioLiberacaoAgendamentoFedexDTO.setHrGeracaoBloqueioLiberacao(eventoDocumentoServicoDMN.getDhEvento().toString("HHmm"));
			bloqueioLiberacaoAgendamentoFedexDTO.setComplemento(buildDataHoraAgendamento(eventoDocumentoServicoDMN, agendamentoDoctoServico));
			bloqueioLiberacaoAgendamentoFedexDTO.setTpBloqueio(buildTpBloqueioLiberacaoAgendamento(eventoDocumentoServicoDMN));
			bloqueioLiberacaoAgendamentoFedexDTO.setSiglaFilialOrigemCte(doctoServico.getFilialByIdFilialOrigem().getSgFilial());
			bloqueioLiberacaoAgendamentoFedexDTO.setCdFilialFedexDestino(doctoServico.getFilialByIdFilialDestino().getCdFilialFedex().toString());
			
			return bloqueioLiberacaoAgendamentoFedexDTO;
		}
		
		return null;
	}
	
	private String buildChaveBloqueioLiberacaoAgendamento(MonitoramentoDocEletronico mde) {
		if(mde!=null && "CTE".equals(mde.getDoctoServico().getTpDocumentoServico().getValue())) {
			return mde.getNrChave();
		} else {
			return mde.getNrChaveEletrRpstFdx();
		}
	}

	private String buildTpBloqueioLiberacaoAgendamento(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
		if(Boolean.TRUE.equals(eventoDocumentoServicoDMN.getBlOcorrenciaDoctoServicoManual())){
			OcorrenciaPendencia ocorrenciaPendencia =  ocorrenciaPendenciaService.findByCodigoOcorrencia((short)eventoDocumentoServicoDMN.getCdOcorrenciaPendencia());
			if ("B".equals(ocorrenciaPendencia.getTpOcorrencia().getValue())) {
				return "05";
			}else{
				return "00";
			}
		} else {
			return "01";
		}
	}

	private String buildDataHoraAgendamento(EventoDocumentoServicoDMN eventoDocumentoServicoDMN, AgendamentoDoctoServico agendamentoDoctoServico) {
		String retorno = "";
		
		if(!Boolean.TRUE.equals(eventoDocumentoServicoDMN.getBlOcorrenciaDoctoServicoManual())){
			if(agendamentoDoctoServico!= null){
				String dtAgend = JTFormatUtils.format(agendamentoDoctoServico.getAgendamentoEntrega().getDtAgendamento(), "yyyyMMdd");
				String hrAgend = "";
				if(null != agendamentoDoctoServico.getAgendamentoEntrega().getTurno()){
					hrAgend = agendamentoDoctoServico.getAgendamentoEntrega().getTurno().getHrTurnoInicial().toString(DateTimeFormat.forPattern("HHmm"));;
				}else{
					hrAgend = agendamentoDoctoServico.getAgendamentoEntrega().getHrPreferenciaInicial().toString(DateTimeFormat.forPattern("HHmm"));
				}
				retorno = dtAgend+hrAgend;
			}
		}
		return retorno;
	}
        

	
	public OcorrenciaFedexDTO executeEnriqueceOcorrenciaEntrega(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {

        EventoDocumentoServico eventoDocumentoServico;
        if (eventoDocumentoServicoDMN.getIdEventoDocumentoServico() != null) {
            eventoDocumentoServico = eventoDocumentoServicoService.findById(eventoDocumentoServicoDMN.getIdEventoDocumentoServico());
        } else {
            eventoDocumentoServico = eventoDocumentoServicoService.findEventoDoctoServicoFilial(
                    eventoDocumentoServicoDMN.getIdDoctoServico(), eventoDocumentoServicoDMN.getCdEvento(), eventoDocumentoServicoDMN.getIdFilialEvento());
        }

        DoctoServico doctoServico = doctoServicoService.findById(eventoDocumentoServico.getDoctoServico().getIdDoctoServico());
        if (doctoServico == null || !validateTipoDocumento(doctoServico) ) {
            return null;
        }
        
        Map<String,Object> dadosEvento = new HashMap<String, Object>();
        
        dadosEvento.put("idEvento",eventoDocumentoServico.getEvento().getIdEvento());
        dadosEvento.put("dhEvento",eventoDocumentoServico.getDhEvento());
        dadosEvento.put("cdEvento",eventoDocumentoServico.getEvento().getCdEvento());
        dadosEvento.put("idFilialEvento",eventoDocumentoServico.getFilial().getIdFilial());
        dadosEvento.put("sgFilialEvento",eventoDocumentoServico.getFilial().getSgFilial());
        
        if (eventoDocumentoServico.getOcorrenciaEntrega()!= null){
        	dadosEvento.put("cdOcorrenciaEntrega",eventoDocumentoServico.getOcorrenciaEntrega().getCdOcorrenciaEntrega());
        	dadosEvento.put("idOcorrenciaEntrega",eventoDocumentoServico.getOcorrenciaEntrega().getIdOcorrenciaEntrega());
        }

        if (eventoDocumentoServico.getOcorrenciaPendencia() != null){
        	dadosEvento.put("idOcorrenciaPendencia",eventoDocumentoServico.getOcorrenciaPendencia().getIdOcorrenciaPendencia());
        }
        
        Conhecimento conhecimento = (Conhecimento)doctoServico;
        if (isEventoFinalizacaoEntregaParcial(conhecimento,dadosEvento) || isEventoEntregaParcial(dadosEvento)){
            return null;
        }
        
        List<Map<String,Object>> notasFiscais = new ArrayList<Map<String,Object>>();
        List<NotaFiscalConhecimento> notasFiscaisConhecimento = new ArrayList<NotaFiscalConhecimento>();
        if(eventoDocumentoServico.getOcorrenciaEntrega() == null && eventoDocumentoServico.getOcorrenciaPendencia() == null){
            notasFiscaisConhecimento = notaFiscalConhecimentoService.findNotasFiscaisConhecimentoSemNotaFiscalOperada(doctoServico.getIdDoctoServico());
        }else{
            notasFiscaisConhecimento = notaFiscalConhecimentoService.findByConhecimento(doctoServico.getIdDoctoServico());
        }
        
        for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimento) {
			Map<String,Object> mapNota = new HashMap<String, Object>();
			mapNota.put("dsSerie", notaFiscalConhecimento.getDsSerie());
			mapNota.put("nrNotaFiscal", notaFiscalConhecimento.getNrNotaFiscal());
			mapNota.put("dtEmissao", notaFiscalConhecimento.getDtEmissao());
			mapNota.put("cnpjEmissor", findCnpjEmissor(notaFiscalConhecimento));
			
			notasFiscais.add(mapNota);
		}
        
        return executeBuildOcorrenciaFedexDTO(doctoServico, dadosEvento,notasFiscais);
    }
	
	private String findCnpjEmissor(NotaFiscalConhecimento notaFiscalConhecimento){
		if (notaFiscalConhecimento.getNrChave() != null){
			return notaFiscalConhecimento.getNrChave().substring(6, 20);
		}else{
			Cliente cliente = notaFiscalConhecimento.getCliente();
			return cliente.getPessoa().getNrIdentificacao();
		}
	}
	
	private boolean validateTipoDocumento(DoctoServico doctoServico){
		String tpDocumento = doctoServico.getTpDocumentoServico().getValue();
		
		return ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumento)
				|| ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumento);
	}
    
	/**
	 * metodo refatorado, este aqui deve comtemplar as mesmas regras 
	 * 
	 * 
	 * @param eventoDocumentoServicoDMN
	 * @return
	 */
	public OcorrenciaFedexDTO executeBuildOcorrenciaFedexDTO(DoctoServico doctoServico, Map dadosEvento, List<Map<String, Object>> notasFiscaisInformadas) {

        if (validateDoctoServicoCTEOrNTE(doctoServico)) {
            if (!(doctoServico instanceof Conhecimento)) {
                return null;
            }

            Conhecimento conhecimento = (Conhecimento)doctoServico;
            
            if (isTrackingFedex(conhecimento, dadosEvento)) {
                return null;
            }
            
            if (Boolean.TRUE.equals(conhecimento.getBlRedespachoIntermediario())) {
                Short cdOcorrenciaCliente = buildCodigoOcorrenciaByClienteRemetente(doctoServico.getClienteByIdClienteRemetente().getIdCliente(), dadosEvento);
                if (cdOcorrenciaCliente != null) {
                    OcorrenciaFedexDTO ocorrenciaFedexDTO = buildOcorrenciaFedexDTO(doctoServico);
                    ocorrenciaFedexDTO.setDocumentos(buildListOcorrenciaFedexDocumentoDTO(dadosEvento, cdOcorrenciaCliente, doctoServico, notasFiscaisInformadas));
                    return ocorrenciaFedexDTO;
                }
            } else if (doctoServico.getClienteByIdClienteRedespacho() != null || BooleanUtils.isTrue(conhecimento.getBlRedespachoColeta())) {
                return executeBuildOcorrenciaFedexDto(
                        dadosEvento, 
                        doctoServico, 
                        ConstantesExpedicao.CNPJ_GERA_OCORREN_FEDEX, 
                        NM_TABELA_OCORREN,
                        Boolean.FALSE,
                        notasFiscaisInformadas);
            }else if (conhecimento.getNrCtrcSubcontratante() != null){
                return executeBuildOcorrenciaFedexDto(
                        dadosEvento, 
                        doctoServico, 
                        ConstantesExpedicao.CNPJ_GERA_OCORREN_FEDEX_SUB, 
                        NM_TABELA_OCORREN_SUB,
                        Boolean.TRUE, notasFiscaisInformadas);
            }
        }

        return null;
    }

    private boolean isEventoFinalizacaoEntregaParcial(
			Conhecimento conhecimento, Map dadosEvento) {
    	Short eventoEntrega = (Short)dadosEvento.get("cdEvento");
    	
    	if (CD_OCORRENCIA_ENTREGA_REALIZADA.equals(eventoEntrega)){
    		List<EventoDocumentoServico> eventosFinalizacaoEntregaParcial = eventoDocumentoServicoService.findByDoctoServico(conhecimento.getIdDoctoServico(), CD_EVENTO_ENTREGA_PARCIAL_FINALIZADA);
    		return CollectionUtils.isNotEmpty(eventosFinalizacaoEntregaParcial);
    	}
    	
		return false;
	}
    
    private boolean isEventoEntregaParcial(Map dadosEvento) {
        Short cdEvento = (Short)dadosEvento.get("cdEvento");
        Short cdOcorrenciaEntrega = (Short)dadosEvento.get("cdOcorrenciaEntrega");
        if (CD_OCORRENCIA_ENTREGA.equals(cdEvento) && cdOcorrenciaEntrega != null && CD_OCORRENCIA_ENTREGA_PARCIAL.equals(cdOcorrenciaEntrega) ){
            return true;
        }
        
        return false;
    }

	private OcorrenciaFedexDTO executeBuildOcorrenciaFedexDto(
    		Map dadosEvento,
            DoctoServico doctoServico, 
            String nmParametroTomadorFedex, 
            String nmTabelaOcorrem,
            Boolean isCtrcSubcontratante, List<Map<String, Object>> notasInformadas) {
        Cliente tomadorFedex = this.getTomadorFedex(doctoServico.getIdDoctoServico(), nmParametroTomadorFedex);
        if (tomadorFedex != null) {
            Short cdOcorrenciaCliente = buildCodigoOcorrenciaByClienteTomador(tomadorFedex.getIdCliente(), dadosEvento, nmTabelaOcorrem);
            if (cdOcorrenciaCliente != null) {
                OcorrenciaFedexDTO ocorrenciaFedexDTO = buildOcorrenciaFedexDTOProcessadoTomador(doctoServico, isCtrcSubcontratante);
                ocorrenciaFedexDTO.setDocumentos(buildListOcorrenciaFedexDocumentoDTOProcessadoTomador(dadosEvento, cdOcorrenciaCliente, doctoServico,notasInformadas));
                
                if (CD_OCORRENCIA_RNC.equals(cdOcorrenciaCliente) && isCtrcSubcontratante) {
                    ocorrenciaFedexDTO.setOcorrenciaFedexRncDTO(buildOcorrenciaFedexRncDTO(dadosEvento, doctoServico, cdOcorrenciaCliente));
                }
                return ocorrenciaFedexDTO;
            }
        }
        return null;
    }
	
    /**
     * LMSA-7625 - Foi verificado que documentos de serviços do tipo CTE, que
     * for de redespacho e onde o CNPJ raiz for Fedex não deve ser processado o
     * documento em questão.
     *
     * @param conhecimento
     * @param eventoDocumentoServico
     * @return True se é tracking Fedex. Caso contrário, false.
     */
    private boolean isTrackingFedex(Conhecimento conhecimento, Map dadosEvento) {
    	
    	Short cdOcorrenciaEntrega = (Short)dadosEvento.get("cdOcorrenciaEntrega");
    	
        if (conhecimento == null
                || cdOcorrenciaEntrega == null
                || conhecimento.getDadosComplementos() == null) {
            return false;
        }

        if ("CTE".equals(conhecimento.getTpDocumentoServico().getValue().toUpperCase().replace("[-~*_]", "")) 
                && BooleanUtils.isTrue(conhecimento.getBlRedespachoColeta())) {
            String dsConteudoFedex = parametroGeralService.findByNomeParametro("CNPJ_PROPRIETARIO_FDX").getDsConteudo();
            String nrIdentificacao = getNrIdentificacaoFromCliente(conhecimento.getClienteByIdClienteConsignatario());
            
            if(nrIdentificacao!= null && dsConteudoFedex.startsWith(nrIdentificacao)){
                return true;
            }
        }

        return false;
    }
        
    private String getNrIdentificacaoFromCliente(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        Pessoa pessoa = cliente.getPessoa();
        
        if (pessoa == null || pessoa.getNrIdentificacao() == null || pessoa.getNrIdentificacao().length() < 7) {
            return null;
        }
        
        return pessoa.getNrIdentificacao().substring(0, 7);
    }
        
	private boolean validateDoctoServicoCTEOrNTE(DoctoServico doctoServico) {
		return ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(doctoServico.getTpDocumentoServico().getValue()) || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(doctoServico.getTpDocumentoServico().getValue());
	}
	
	private Cliente getTomadorFedex(Long idDoctoServico, String cnpjGeraOcorrenFedex) {
		String cnpjsRaizTomador = parametroGeralService.findSimpleConteudoByNomeParametro(cnpjGeraOcorrenFedex);
		String cnpjRaiz = null;
		
		DevedorDocServ dds = devedorDocServService.findDevedorByDoctoServico(idDoctoServico);
		if(dds != null && dds.getCliente() != null && dds.getCliente().getPessoa() != null 
				&& dds.getCliente().getPessoa().getNrIdentificacao() !=null 
				&& dds.getCliente().getPessoa().getNrIdentificacao().length() >= 8) {
			cnpjRaiz = dds.getCliente().getPessoa().getNrIdentificacao().substring(0, 8);
			if(cnpjsRaizTomador.contains(cnpjRaiz)){
				return dds.getCliente();
			}
		}
        
		return null;
	}

	private Short buildCodigoOcorrenciaByClienteTomador(Long idClienteTomador, Map dadosEvento, String nmTabelaOcorren) {

		Long idEvento = (Long) dadosEvento.get("idEvento");
		Long idOcorrenciaEntrega = (Long) dadosEvento.get("idOcorrenciaEntrega");
		Long idOcorrenciaPendencia =  (Long) dadosEvento.get("idOcorrenciaPendencia");
		boolean hasOcorrenciaEntrega = idOcorrenciaEntrega != null;
		boolean hasOcorrenciaPendencia = idOcorrenciaPendencia != null;
		
		Long idLMS = hasOcorrenciaPendencia ?
					idOcorrenciaPendencia
					:(hasOcorrenciaEntrega ? idOcorrenciaEntrega : idEvento);
		
		String tpOcorrencia = hasOcorrenciaPendencia ? "2" : (hasOcorrenciaEntrega ? "1" : "3");

		return eventoDocumentoServicoService.findCdOcorrenciaCliente(idClienteTomador, idLMS, tpOcorrencia, nmTabelaOcorren);
	}
	
	private Short buildCodigoOcorrenciaByClienteRemetente(Long idClienteRemetente, Map dadosEvento) {
		
		Long idEvento = (Long) dadosEvento.get("idEvento");
		Long idOcorrenciaPendencia =  (Long) dadosEvento.get("idOcorrenciaPendencia");
		
		boolean hasOcorrenciaPendencia = idOcorrenciaPendencia != null;
		
		Long idLMS = hasOcorrenciaPendencia	? idOcorrenciaPendencia	: idEvento;
		
		String tpOcorrencia = hasOcorrenciaPendencia ? "2" : "3";

		return eventoDocumentoServicoService.findCdOcorrenciaCliente(idClienteRemetente, idLMS, tpOcorrencia, "TRACKING FEDEX LIN");
	}
	
	private OcorrenciaFedexDTO buildOcorrenciaFedexDTOProcessadoTomador(DoctoServico doctoServico, Boolean isCtrcSubcontratante) {
		OcorrenciaFedexDTO ocorrenciaFedexDTO = new OcorrenciaFedexDTO();
		ocorrenciaFedexDTO.setNomeRemetente("TNT MERCURIO");
		ocorrenciaFedexDTO.setNomeDestinatario("FEDEX");
		ocorrenciaFedexDTO.setDhOperacao(new DateTime());
		ocorrenciaFedexDTO.setIdentificacaoIntercambio("OCO50"+new SimpleDateFormat("ddMM").format(new Date())+"999");
		ocorrenciaFedexDTO.setSiglaNumCTe(doctoServico.getFilialByIdFilialOrigem().getSgFilial()+doctoServico.getNrDoctoServico());
		ocorrenciaFedexDTO.setBlRedespachoIntermediario(Boolean.FALSE);
		if(isCtrcSubcontratante){
            ocorrenciaFedexDTO.setBlProcessamentoTomador(Boolean.FALSE);
            ocorrenciaFedexDTO.setBlCtrcSubcontratante(Boolean.TRUE);
		}else{
		    ocorrenciaFedexDTO.setBlProcessamentoTomador(Boolean.TRUE);
		    ocorrenciaFedexDTO.setBlCtrcSubcontratante(Boolean.FALSE);
		}
		return ocorrenciaFedexDTO;
	}
	
	private OcorrenciaFedexDTO buildOcorrenciaFedexDTO(DoctoServico doctoServico) {
		OcorrenciaFedexDTO ocorrenciaFedexDTO = new OcorrenciaFedexDTO();
		ocorrenciaFedexDTO.setNomeRemetente(doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
		ocorrenciaFedexDTO.setNomeDestinatario(doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
		ocorrenciaFedexDTO.setDhOperacao(new DateTime());
		ocorrenciaFedexDTO.setIdentificacaoIntercambio("OCO50"+new SimpleDateFormat("ddMM").format(new Date())+"999");
		ocorrenciaFedexDTO.setSiglaNumCTe(doctoServico.getFilialByIdFilialOrigem().getSgFilial()+doctoServico.getNrDoctoServico());
		ocorrenciaFedexDTO.setBlRedespachoIntermediario(Boolean.TRUE);
		ocorrenciaFedexDTO.setBlProcessamentoTomador(Boolean.FALSE);
		ocorrenciaFedexDTO.setBlCtrcSubcontratante(Boolean.FALSE);
		return ocorrenciaFedexDTO;
	}
	
	private String buscaDadosComplemento(Conhecimento conhecimento, String dsCampo, Integer beginIndex, Integer endIndex) {
		String valorComplemento = buscaDadosComplementoInformacaoClienteByIdDoctoServicoAndTpRegistro(conhecimento.getIdDoctoServico(), dsCampo);
		
		if (validateBuscaComplementoNrChaveDocumentoAnterior(conhecimento, valorComplemento)) {
			String valor = buscaDadosComplementoTipoNrChaveDocumentoAnterior(conhecimento.getIdDoctoServico());
			return subStringValorDadosComplemento(beginIndex, endIndex, valor);
		}
		return valorComplemento;
	}

	private String buscaDadosComplementoInformacaoClienteByIdDoctoServicoAndTpRegistro(Long idDoctoServico, String dsCampo) {
		final DadosComplemento complemento = dadosComplementoService.findByIdConhecimentoTpRegistro(idDoctoServico, dsCampo);
		
		if(complemento != null && complemento.getDsValorCampo() != null){
			return complemento.getDsValorCampo();
		}
		return null;
	}

	private boolean validateBuscaComplementoNrChaveDocumentoAnterior(Conhecimento conhecimento, String valorComplemento) {
		return (BooleanUtils.isTrue(conhecimento.getBlIndicadorEdi()) && StringUtils.isBlank(valorComplemento)) || BooleanUtils.isFalse(conhecimento.getBlIndicadorEdi());
	}

	private String subStringValorDadosComplemento(Integer beginIndex, Integer endIndex, String valor) {
		if (StringUtils.isNotBlank(valor) && valor.length() >= endIndex.intValue()) {
			return valor.substring(beginIndex, endIndex);
		}
		return valor;
	}
	
	private String buscaDadosComplementoTipoNrChaveDocumentoAnterior(Long idDoctoServico) {
		return buscaDadosComplementoByIdDoctoServicoAndTpRegistro(idDoctoServico, NR_CHAVE_DOCUMENTO_ANTERIOR);
	}

	private String buscaDadosComplementoByIdDoctoServicoAndTpRegistro(Long idDoctoServico, String dsCampo){
		final DadosComplemento complemento = dadosComplementoService.findByIdConhecimentoDocServico(idDoctoServico, dsCampo);
		if(complemento != null && complemento.getDsValorCampo() != null){
			return complemento.getDsValorCampo();
		}
		return null;
	}
	
	public String buscaDadosComplementoDocServico(Long idDoctoServico, String dsCampo){
		final DadosComplemento complemento = dadosComplementoService.findByIdConhecimentoDocServico(idDoctoServico, dsCampo);
		if(complemento != null && complemento.getDsValorCampo() != null){
			return complemento.getDsValorCampo();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String buscaNomeRecebedor(Long idDoctoServico) {
 		List list = null;
 		
		list = manifestoEntregaDocumentoService.findNomeRecebedorByIdDoctoServico(idDoctoServico);
		
		if (list != null && !list.isEmpty()){
			Map<String,Object> dados = (Map<String,Object>)list.get(0);
			return ((String)dados.get("nmRecebedor"));
		}
		
		return null;
	}
	
	private List<OcorrenciaFedexDocumentoDTO> buildListOcorrenciaFedexDocumentoDTOProcessadoTomador(Map dadosEvento, Short cdOcorrenciaCliente, DoctoServico doctoServico,List<Map<String,Object>> listDadosNotas) {
		List<OcorrenciaFedexDocumentoDTO> listDocumentos = new ArrayList<OcorrenciaFedexDocumentoDTO>();
		OcorrenciaFedexDocumentoDTO documentoDTO = new OcorrenciaFedexDocumentoDTO();
		documentoDTO.setIdentificacaoDocumento("OCORR50"+new SimpleDateFormat("ddMM").format(new Date())+"000");
		documentoDTO.setCnpjTransportadora(doctoServico.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
		documentoDTO.setRazaoSocialTransportadora(doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmPessoa());
		documentoDTO.setOcorrenciasEntrega(buildListOcorrenciaEntregaDTOProcessadoTomador(dadosEvento, cdOcorrenciaCliente, doctoServico,listDadosNotas));
		documentoDTO.setTotalOcorrencias(Long.valueOf(documentoDTO.getOcorrenciasEntrega().size()));
	
		listDocumentos.add(documentoDTO);
		return listDocumentos;
	}
	
	private List<OcorrenciaFedexDocumentoDTO> buildListOcorrenciaFedexDocumentoDTO(Map dadosEvento, Short cdOcorrenciaCliente, DoctoServico doctoServico, List<Map<String,Object>> listNotas) {
		List<OcorrenciaFedexDocumentoDTO> listDocumentos = new ArrayList<OcorrenciaFedexDocumentoDTO>();
		OcorrenciaFedexDocumentoDTO documentoDTO = new OcorrenciaFedexDocumentoDTO();
		documentoDTO.setIdentificacaoDocumento("OCORR50"+new SimpleDateFormat("ddMM").format(new Date())+"000");
		documentoDTO.setCnpjTransportadora(doctoServico.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
		documentoDTO.setRazaoSocialTransportadora(doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmPessoa());
		documentoDTO.setOcorrenciasEntrega(buildListOcorrenciaEntregaDTO(dadosEvento, cdOcorrenciaCliente, doctoServico, listNotas));
		documentoDTO.setTotalOcorrencias(Long.valueOf(documentoDTO.getOcorrenciasEntrega().size()));
		
		listDocumentos.add(documentoDTO);
		return listDocumentos;
	}
	
	private OcorrenciaFedexRncDTO buildOcorrenciaFedexRncDTO(Map dadosEvento, DoctoServico doctoServico, Short cdOcorrenciaCliente) {
	    OcorrenciaFedexRncDTO ocorrenciaFedexRncDTO = new OcorrenciaFedexRncDTO();
	    ocorrenciaFedexRncDTO.setCnpjEmissor(doctoServico.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
	    ocorrenciaFedexRncDTO.setSgFilialDocto(doctoServico.getFilialByIdFilialOrigem().getSgFilial());
	    ocorrenciaFedexRncDTO.setSerieDocto("000");
	    ocorrenciaFedexRncDTO.setNumeroDocto(doctoServico.getNrDoctoServico().toString());
	    ocorrenciaFedexRncDTO.setDataEmissao(doctoServico.getDhEmissao());
	    ocorrenciaFedexRncDTO.setChaveCte(this.findNrChaveByIdDoctoServico(doctoServico.getIdDoctoServico()));
	    ocorrenciaFedexRncDTO.setCodigoOcorrencia(cdOcorrenciaCliente.toString());
	    ocorrenciaFedexRncDTO.setDataHoraOcorrencia((DateTime)dadosEvento.get("dhEvento"));
	    
	    NaoConformidade naoConformidade = naoConformidadeService.findByIdDoctoServico(doctoServico.getIdDoctoServico());
	    OcorrenciaNaoConformidade ocorrenciaNaoConformidade = ocorrenciaNaoConformidadeService.findFirstOcorrenciaByIdNaoConformidade(naoConformidade.getIdNaoConformidade());
	    ocorrenciaFedexRncDTO.setSgFilialRnc(naoConformidade.getFilial().getSgFilial());
	    ocorrenciaFedexRncDTO.setNrRnc(naoConformidade.getNrNaoConformidade().toString());
	    ocorrenciaFedexRncDTO.setMotivoRnc(ocorrenciaNaoConformidade.getMotivoAberturaNc().getTpMotivo().getValue());
	    
	    DadosComplemento dadosComplementoChave = dadosComplementoService.findByIdConhecimentoDocServico(doctoServico.getIdDoctoServico(), "NR_CHAVE_DOCUMENTO_ANTERIOR");
	    DadosComplemento dadosComplementoData = dadosComplementoService.findByIdConhecimentoTpRegistro(doctoServico.getIdDoctoServico(), "DTCONH");
	    ocorrenciaFedexRncDTO.setChaveCteSub(dadosComplementoChave.getDsValorCampo());
	    ocorrenciaFedexRncDTO.setCnpjEmissorSub(dadosComplementoChave.getDsValorCampo().substring(6,20));
	    ocorrenciaFedexRncDTO.setSerieDoctoSub(dadosComplementoChave.getDsValorCampo().substring(23,26));
	    ocorrenciaFedexRncDTO.setNumeroDoctoSub(dadosComplementoChave.getDsValorCampo().substring(25,34));
	    ocorrenciaFedexRncDTO.setDataEmissaoSub(getDtEmissaoDadosComplemento(dadosComplementoChave, dadosComplementoData));
	    
	    Conhecimento conhecimento = (Conhecimento)doctoServico;
	    ocorrenciaFedexRncDTO.setTpProcessamento(Boolean.TRUE.equals(conhecimento.getBlIndicadorEdi()) ? "S" : "N");
	    
	    ocorrenciaFedexRncDTO.setListOcorrenciaFedexNotasRncDTO(buildOcorrenciaFedexNotasRncDTO(ocorrenciaNaoConformidade));
	    
	    return ocorrenciaFedexRncDTO;
	}
	
	private List<OcorrenciaFedexNotaRncDTO> buildOcorrenciaFedexNotasRncDTO(OcorrenciaNaoConformidade ocorrenciaNaoConformidade){
	    List<OcorrenciaFedexNotaRncDTO> listOcorrenciaFedexNotaRncDTO = new ArrayList<OcorrenciaFedexNotaRncDTO>();
	    
	    List<NotaFiscalConhecimento> listNotaFiscalConhecimento = notaFiscalConhecimentoService.findByIdOcorrenciaNaoConformidade(ocorrenciaNaoConformidade.getIdOcorrenciaNaoConformidade());
	    for(NotaFiscalConhecimento nota : listNotaFiscalConhecimento){
	        OcorrenciaFedexNotaRncDTO ocorrenciaFedexNotaRncDTO = new OcorrenciaFedexNotaRncDTO();
	        ocorrenciaFedexNotaRncDTO.setSerieNota(nota.getDsSerie());
	        ocorrenciaFedexNotaRncDTO.setNumeroNota(nota.getNrNotaFiscal().toString());
	        ocorrenciaFedexNotaRncDTO.setDataEmissao(nota.getDtEmissao());
	        ocorrenciaFedexNotaRncDTO.setChaveNFE(nota.getNrChave());
	        
	        listOcorrenciaFedexNotaRncDTO.add(ocorrenciaFedexNotaRncDTO);
	    }
	    
	    return listOcorrenciaFedexNotaRncDTO;
	}
	
	private String getDtEmissaoDadosComplemento(DadosComplemento dadosComplementoChave, DadosComplemento dadosComplementoData) {
        String dataFormatada = null;
        if(dadosComplementoData!= null){
            dataFormatada = JTDateTimeUtils.validateStringDate(dadosComplementoData.getDsValorCampo());
        }
        
        if(dataFormatada == null){
            StringBuilder dataEmissaoFormated = new StringBuilder();
            dataEmissaoFormated.append("01");
            dataEmissaoFormated.append(dadosComplementoChave.getDsValorCampo().substring(4,6));
            dataEmissaoFormated.append(dadosComplementoChave.getDsValorCampo().substring(2,4));
            
            dataFormatada = DateTimeFormat.forPattern("ddMMyy").parseDateTime(dataEmissaoFormated.toString()).toString("ddMMyyyy");
        }
        return dataFormatada;
    }
	
	@SuppressWarnings("unchecked")
	private List<OcorrenciaFedexOcorrenciaEntregaDTO> buildListOcorrenciaEntregaDTOProcessadoTomador(Map dadosEvento, Short cdOcorrenciaCliente,
				DoctoServico doctoServico, List<Map<String,Object>> listDadosNotas) {
	
		String cnpjContratante = null;
		DevedorDocServ devedorDocServ = devedorDocServService.findDevedorByDoctoServico(doctoServico.getIdDoctoServico());
		if (devedorDocServ != null && devedorDocServ.getCliente() != null) {
			cnpjContratante = devedorDocServ.getCliente().getPessoa().getNrIdentificacao();
		}
		
		Conhecimento conhecimento = (Conhecimento)doctoServico;
		String cnpjEmissoraConhecimentoOriginador = buscaCnpjEmissoraConhecimentoOriginador(conhecimento);
		String serieConhecimentoOriginador = buscaSerieConhecimentoOriginador(conhecimento);
		String numeroConhecimentoOriginador = buscaNumeroConhecimentoOriginador(conhecimento);
		String dataEmissaoDTFedex = buscaDataEmissaoDtFedex(conhecimento);
		String modeloDTFedex = buscaModeloDtFedex(conhecimento);
		String nomeRecebedor = buscaNomeRecebedor(doctoServico.getIdDoctoServico());
	
		List<OcorrenciaFedexOcorrenciaEntregaDTO> listOcorrenciaEntregaDTO = new ArrayList<OcorrenciaFedexOcorrenciaEntregaDTO>();
		for (Map<String, Object> mapNota : listDadosNotas) {
			OcorrenciaFedexOcorrenciaEntregaDTO ocorrenciaEntregaDTO = new OcorrenciaFedexOcorrenciaEntregaDTO();
			ocorrenciaEntregaDTO.setCnpjEmissor(doctoServico.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
			ocorrenciaEntregaDTO.setSerieNota((String)mapNota.get("dsSerie"));
			ocorrenciaEntregaDTO.setNumeroNota(((Integer)mapNota.get("nrNotaFiscal")).toString());
			ocorrenciaEntregaDTO.setFilialEmissora(doctoServico.getFilialByIdFilialOrigem().getSgFilial());
			ocorrenciaEntregaDTO.setNumeroConhecimento(doctoServico.getNrDoctoServico().toString());
			ocorrenciaEntregaDTO.setCodigoOcorrenciaFedex(cdOcorrenciaCliente);
			ocorrenciaEntregaDTO.setDataHoraOcorrencia((DateTime)dadosEvento.get("dhEvento"));
			
			Short cdEvento = (Short)dadosEvento.get("cdEvento");
			Long idFilialEvento = (Long) dadosEvento.get("idFilialEvento");
			String sgFilialEvento = (String) dadosEvento.get("sgFilialEvento");
			
			if(EventosChegadaPortaria.contains(cdEvento)
					&& idFilialEvento.equals(doctoServico.getFilialByIdFilialDestino().getIdFilial())){
				ocorrenciaEntregaDTO.setDataHoraChegadaDestino((DateTime)dadosEvento.get("dhEvento"));
			}
			ocorrenciaEntregaDTO.setCodigoObservacao(Short.valueOf("00"));
			ocorrenciaEntregaDTO.setFilialEvento(sgFilialEvento);
	
			ocorrenciaEntregaDTO.setOcorrenciaEntregaRedespacho(buildOcorrenciaEntregaRedespachoDTO(cnpjContratante, cnpjEmissoraConhecimentoOriginador, serieConhecimentoOriginador,
			numeroConhecimentoOriginador, dataEmissaoDTFedex, modeloDTFedex, nomeRecebedor));
			listOcorrenciaEntregaDTO.add(ocorrenciaEntregaDTO);
	
		}
		
		return listOcorrenciaEntregaDTO;
	}
	
	private String buscaCnpjEmissoraConhecimentoOriginador(Conhecimento conhecimento) {
		return buscaDadosComplemento(conhecimento, DC_CNPJFDX, 6, 20);
	}

	private String buscaModeloDtFedex(Conhecimento conhecimento) {
		return buscaDadosComplemento(conhecimento, DC_MODSEFAZ, 20, 22);
	}

	private String buscaDataEmissaoDtFedex(Conhecimento conhecimento) {
		String dtConh = buscaDadosComplementoInformacaoClienteByIdDoctoServicoAndTpRegistro(conhecimento.getIdDoctoServico(), DC_DTCONH);
		dtConh = JTDateTimeUtils.validateStringDate(dtConh);
		
		if (validateBuscaComplementoNrChaveDocumentoAnterior(conhecimento, dtConh)) {
			if (StringUtils.isEmpty(dtConh)) {			
				dtConh = buscaDadosComplementoTipoNrChaveDocumentoAnterior(conhecimento.getIdDoctoServico());
				
				if (StringUtils.isEmpty(dtConh)) {
					return null;
				}
				
				StringBuilder diamesano = new StringBuilder();
				diamesano.append("01");
				diamesano.append(dtConh.substring(4, 6));
				diamesano.append(dtConh.substring(2, 4));
				 		
				return DateTimeFormat.forPattern("ddMMyy").parseDateTime(diamesano.toString()).toString("ddMMyyyy");
			}
		}
		
		return dtConh;
	}

	private String buscaNumeroConhecimentoOriginador(Conhecimento conhecimento) {
		return buscaDadosComplemento(conhecimento, DC_CONHFDX, 25, 34);
	}

	private String buscaSerieConhecimentoOriginador(Conhecimento conhecimento) {
		return buscaDadosComplemento(conhecimento, DC_SERIECNH, 22, 25);
	}

	@SuppressWarnings("serial")
	private static final List<Short> EventosChegadaPortaria = new ArrayList<Short>(3){{
		add((short) 58);
		add((short) 130);
		add((short) 152);
	}};
	
	@SuppressWarnings("unchecked")
    private List<OcorrenciaFedexOcorrenciaEntregaDTO> buildListOcorrenciaEntregaDTO(Map dadosEvento, Short cdOcorrenciaCliente, DoctoServico doctoServico, List<Map<String,Object>> listNotas) {
		List<OcorrenciaFedexOcorrenciaEntregaDTO> listOcorrenciaEntregaDTO = new ArrayList<OcorrenciaFedexOcorrenciaEntregaDTO>();
		for(Map<String,Object> mapNota : listNotas) {
			OcorrenciaFedexOcorrenciaEntregaDTO ocorrenciaEntregaDTO = new OcorrenciaFedexOcorrenciaEntregaDTO();
			ocorrenciaEntregaDTO.setCnpjEmissor((String)mapNota.get("cnpjEmissor"));
			ocorrenciaEntregaDTO.setSerieNota((String)mapNota.get("dsSerie"));
			ocorrenciaEntregaDTO.setNumeroNota(((Integer)mapNota.get("nrNotaFiscal")).toString());
			ocorrenciaEntregaDTO.setDataEmissaoNota((YearMonthDay)mapNota.get("dtEmissao"));
			ocorrenciaEntregaDTO.setCodigoOcorrenciaFedex(cdOcorrenciaCliente);
			ocorrenciaEntregaDTO.setDataHoraOcorrencia((DateTime)dadosEvento.get("dhEvento"));
			ocorrenciaEntregaDTO.setCodigoObservacao(Short.valueOf("6"));
			
			listOcorrenciaEntregaDTO.add(ocorrenciaEntregaDTO);
		}
		return listOcorrenciaEntregaDTO;
	}
	
	private OcorrenciaFedexOcorrenciaEntregaRedespachoDTO buildOcorrenciaEntregaRedespachoDTO(String cnpjContratante, String cnpjEmissoraConhecimentoOriginador,
			String serieConhecimentoOriginador, String numeroConhecimentoOriginador, String dataEmissaoDTFedex, String modeloDTFedex, String nomeRecebedor) {
		OcorrenciaFedexOcorrenciaEntregaRedespachoDTO ocorrenciaEntregaRedespachoDTO = new OcorrenciaFedexOcorrenciaEntregaRedespachoDTO();
	
		ocorrenciaEntregaRedespachoDTO.setCnpjContratante(cnpjContratante);
		ocorrenciaEntregaRedespachoDTO.setCnpjEmissoraConhecimentoOriginador(cnpjEmissoraConhecimentoOriginador);
		ocorrenciaEntregaRedespachoDTO.setFilialEmissoraCTRCOriginador(null);
		ocorrenciaEntregaRedespachoDTO.setSerieConhecimentoOriginador(serieConhecimentoOriginador);
		ocorrenciaEntregaRedespachoDTO.setNumeroConhecimentoOriginador(numeroConhecimentoOriginador);
		ocorrenciaEntregaRedespachoDTO.setDataEmissaoDTFedex(dataEmissaoDTFedex);
		ocorrenciaEntregaRedespachoDTO.setModeloDTFedex(modeloDTFedex);
		ocorrenciaEntregaRedespachoDTO.setTipoDocRecebedor(null);
		ocorrenciaEntregaRedespachoDTO.setNumeroDocRecebedor(null);
		ocorrenciaEntregaRedespachoDTO.setNomeRecebedor(nomeRecebedor);
		return ocorrenciaEntregaRedespachoDTO;
	}

	public ManifestoEntregaDocumentoService getManifestoEntregaDocumentoService() {
		return manifestoEntregaDocumentoService;
	}

	public void setManifestoEntregaDocumentoService(
			ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	/*		OcorrenciaFedexOcorrenciaEntregaDTO ocorrenciaEntregaDTO = new OcorrenciaFedexOcorrenciaEntregaDTO();
			ocorrenciaEntregaDTO.setCnpjEmissor(nfc.getNrChave().substring(6, 20));
			ocorrenciaEntregaDTO.setSerieNota(nfc.getDsSerie());
			ocorrenciaEntregaDTO.setNumeroNota(nfc.getNrNotaFiscal().toString());
			ocorrenciaEntregaDTO.setDataEmissaoNota(nfc.getDtEmissao());
			ocorrenciaEntregaDTO.setCodigoOcorrenciaFedex(cdOcorrenciaCliente);
			ocorrenciaEntregaDTO.setDataHoraOcorrencia(eventoDocumentoServico.getDhEvento());
			ocorrenciaEntregaDTO.setCodigoObservacao(Short.valueOf("6"));
			
			listOcorrenciaEntregaDTO.add(ocorrenciaEntregaDTO);
		}
		return listOcorrenciaEntregaDTO;
	}*/

	

	public MonitoramentoDocEletronico findMonitoramentoDocEletronicoByIdDoctoServico(Long idDoctoServico){
		return getMonitoramentoDocEletronicoDao().findMonitoramentoDocEletronicoByIdDoctoServico(idDoctoServico);	
	}
	
	public boolean findExistsMonitoramentoDocEletronicoByIdDoctoServico(Long idDoctoServico){
		return getMonitoramentoDocEletronicoDao()
					.findExistsMonitoramentoDocEletronicoByIdDoctoServico(idDoctoServico);	
	}

	public Integer getRowCountMonitoramentoDocEletronico(TypedFlatMap criteria) {
		return getMonitoramentoDocEletronicoDao().getRowCountMonitoramentoDocEletronico(criteria);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedMonitoramentoDocEletronico(TypedFlatMap criteria){
		return getMonitoramentoDocEletronicoDao().findPaginatedMonitoramentoDocEletronico(criteria, FindDefinition.createFindDefinition(criteria));
	}
	
	//LMS-4210
	private String gerarXMLInutCTE(String chave) {
		StringBuilder sb = new StringBuilder();
		sb.append("<cteCancInut>");
		sb.append("<chCTe>");
		sb.append(chave);
		sb.append("</chCTe>");
		sb.append("<xMotivoInut>");
		sb.append(parametroGeralService.findSimpleConteudoByNomeParametro(ConstantesExpedicao.JUST_INUT_CTE));
		sb.append("</xMotivoInut>");
		sb.append("</cteCancInut>");

		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> findDocumentosRejeitados(String dtInicio, String dtFim){
		return getMonitoramentoDocEletronicoDao().findDocumentosRejeitados(dtInicio, dtFim);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void removeDocumentoRejeitado(Long idDocumento,Long idFilial, String sgFilial, Long nrDocumento,String tpDocumentoServico,Long idLocalizacaoMercadoria){
		Conhecimento conhecimento = conhecimentoService.findById(idDocumento);
		if( "P".equals(conhecimento.getTpSituacaoConhecimento().getValue()) ){
			incluirEventoRastreabilidadeInternacionalService.generateEventoDocumento(ConstantesSim.EVENTO_DOCUMENTO_CANCELADO, idDocumento, idFilial, ConhecimentoUtils.formatConhecimento(sgFilial, nrDocumento), JTDateTimeUtils.getDataHoraAtual(), null, null, tpDocumentoServico);

			getMonitoramentoDocEletronicoDao().removeDocumentoRejeitado(idDocumento, idLocalizacaoMercadoria);
			generateIntegracaoInutilizacaoGenerica(conhecimento);
		}
	}
	
	private void generateIntegracaoInutilizacaoGenerica(Conhecimento conhecimento) {

		final String chave;

		MonitoramentoDocEletronico monitoramentoDocEletronico = findMonitoramentoDocEletronicoByIdDoctoServico(conhecimento.getIdDoctoServico());
		Integer random = gerarConhecimentoEletronicoXMLService.getRandom(conhecimento);
		monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("I"));
		
		if(monitoramentoDocEletronico.getNrChave() == null){
			chave = gerarConhecimentoEletronicoXMLService.gerarChaveAcesso(conhecimento, random);
		monitoramentoDocEletronico.setNrChave(chave.substring(3));
		} else {
			chave = "CTe" + monitoramentoDocEletronico.getNrChave();
		}
		
		//LMS-4210
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		String conteudoParametro = String.valueOf(conteudoParametroFilialService.findConteudoByNomeParametroWithException(filialUsuarioLogado.getIdFilial(), "Versão_XML_CTe", false));

		boolean versao300a = conteudoParametro != null && conteudoParametro.equals(EVersaoXMLCTE.VERSAO_300a.getConteudoParametro());

		if(versao300a) {

			monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("H"));
			String xml = gerarXMLInutCTE(chave);
			TBDatabaseInputCTE tbDatabaseInput = integracaoNDDigitalService.generateIntegracaoInutilizacao(monitoramentoDocEletronico, xml);
			monitoramentoDocEletronico.setIdEnvioDocEletronicoI(tbDatabaseInput.getId());

		}

		store(monitoramentoDocEletronico);
	}

	public Map<String, Object> executeReemitir(TypedFlatMap criteria) {
		//LMS-4098 Emitir RPS
		final DoctoServico conhecimento = doctoServicoService.findById(criteria.getLong("idDoctoServico"));
		
		String tpDocumentoServico = conhecimento.getTpDocumentoServico().getValue();
		
		boolean isCTE = ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(tpDocumentoServico);
		boolean isNTE = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(tpDocumentoServico);
		boolean isNSE = ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(tpDocumentoServico);

		Map<String, Object> map = new HashMap<String, Object>();

		if(isCTE){
			map.put("tpDocumentoServico", ConstantesExpedicao.CONHECIMENTO_ELETRONICO);
			map.put("cte", integracaoNDDigitalService.findByDoctoServico(criteria.getLong("idDoctoServico")));

			Integer nrVias = null;
			
			try{
				ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(
						SessionUtils.getFilialSessao().getIdFilial(), "NRO_VIAS_CTE", false);
				if (conteudoParametroFilial != null && StringUtils.isNotEmpty(conteudoParametroFilial.getVlConteudoParametroFilial())) {
					nrVias = Integer.valueOf(conteudoParametroFilial.getVlConteudoParametroFilial());
				}
			}catch (BusinessException e) {}
			
			try{
				if (nrVias == null){
					nrVias = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("NRO_VIAS_CTE", false)).intValue();
				}
			}catch (BusinessException e) {}
			
			if (nrVias == null){
				nrVias = Integer.valueOf(1);
			}
			
			map.put("nrVias", nrVias);
			
		}else if(isNTE){
			
			if(nfeConjugadaService.isAtivaNfeConjugada(conhecimento.getFilialByIdFilialOrigem().getIdFilial())){
				map.put("tpDocumentoServico", ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA);
				ajustaParametroImpressao(criteria, map);				
			}else{
				//LMS-4098 emissão RPS-T
				map.putAll( executeMontarInfNte(criteria.getLong("idDoctoServico"), criteria.getLong("idMonitoramentoDocEletronic")) );
			}
			
		}else if(isNSE){
			if(nfeConjugadaService.isAtivaNfeConjugada(conhecimento.getFilialByIdFilialOrigem().getIdFilial())){
				map.put("tpDocumentoServico", ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA);				
				ajustaParametroImpressao(criteria, map);								
			}else{
				//LMS-4098 emissão RPS-S
				map.putAll( executeMontarInfNse(criteria.getLong("idDoctoServico"), criteria.getLong("idMonitoramentoDocEletronic")) );			
			}
		}
		
		return map;
	}

	/**
	 * @param criteria
	 * @param map
	 */
	private void ajustaParametroImpressao(TypedFlatMap criteria,
			Map<String, Object> map) {
		List<String> monitoramento = new ArrayList<String>();
		monitoramento.add(String.valueOf(criteria.get("idMonitoramentoDocEletronic")));				
		map.put("nfeConjugada", true);
		map.put("monitoramento",monitoramento );
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> executeMontarInfNse(Long idDoctoServico, Long idMonitoramentoServico) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("tpDocumentoServico", ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA);
		map.put("infRpss", notaFiscalEletronicaService.findInfoRpssByDoctoServico(idDoctoServico));
		map.put("nfeXML", new String(findXmlByDoctoServico(idMonitoramentoServico),Charset.forName("UTF-8")));
		map.put("dsObservacaoDoctoServico", montarObservacoes( idDoctoServico, (String) map.get("nfeXML") ) );
		Map<String, Boolean> isIssqnRetido = new HashMap<String, Boolean>();
		isIssqnRetido.put("isIssqnRetido", impostoServicoService.findIssRetidoByIdConhecimento(idDoctoServico));
		if(map.get("infRpss") != null){
			((Map)map.get("infRpss")).putAll(isIssqnRetido);
		}else{
			map.put("infRpss", isIssqnRetido);
		}
		
		return map;
	}

	private List<ObservacaoDoctoServico> montarObservacoes(Long idDoctoServico, String xml) {
		
		List<ObservacaoDoctoServico> observacaoDoctoServicos = new ArrayList<ObservacaoDoctoServico>();
		if(StringUtils.isNotBlank(xml)){
			InputSource source = new InputSource(new StringReader(xml));
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			Document document = null;
			
			try {
				db = dbf.newDocumentBuilder();
				document = db.parse(source);
				
				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();
				
				XPathExpression expr = xpath.compile("Rps/DadosAdic/CamposAdicionais/CampoAdicional/ValorCampo/text()");
				Object result = expr.evaluate(document, XPathConstants.NODESET);
				NodeList nodes = (NodeList) result;
				
				ObservacaoDoctoServico observacaoDoctoServico = new ObservacaoDoctoServico();
				StringBuilder observacao = new StringBuilder();
					
				for (int i = 0; i < nodes.getLength(); i++) {
					observacao.append(nodes.item(i).getNodeValue());
				}
				
				if(StringUtils.isNotBlank(observacao.toString())){
					observacaoDoctoServico.setDsObservacaoDoctoServico(observacao.toString());
					observacaoDoctoServicos.add(observacaoDoctoServico);
				}
				
			} catch (Exception e) {
				log.error(e);
			}
		}
		
		if(observacaoDoctoServicos.isEmpty()){
			List<ObservacaoDoctoServico> observacaoDoctoServicosBase = observacaoDoctoServicoService.findOrderPriorityByIdDoctoServico(idDoctoServico);
			ObservacaoDoctoServico observacaoDoctoServico = new ObservacaoDoctoServico();
			StringBuilder observacao = new StringBuilder();
			
			for (ObservacaoDoctoServico observacaoDoctoServicoBase : observacaoDoctoServicosBase) {
				if(StringUtils.isNotBlank(observacaoDoctoServicoBase.getDsObservacaoDoctoServico())){
					observacao.append(observacaoDoctoServicoBase.getDsObservacaoDoctoServico()).append("|");
				}
			}
			
			if(StringUtils.isNotBlank(observacao.toString())){
				observacao = new StringBuilder( observacao.toString().substring(0,observacao.toString().length()-1) );
				observacaoDoctoServico.setDsObservacaoDoctoServico(observacao.toString());
			}
			
			observacaoDoctoServico.setDsObservacaoDoctoServico(observacao.toString());
			observacaoDoctoServicos.add(observacaoDoctoServico);
		}
		
		
		return observacaoDoctoServicos;
	}

	public Map<String, Object> executeMontarInfNte(Long idDoctoServico, Long idMonitoramentoEletronico) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put( "tpDocumentoServico", ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA );
		map.put( "infRpst", notaFiscalEletronicaService.findInfoRpstByDoctoServico(idDoctoServico) );
		map.put( "nfeXML", new String(findXmlByDoctoServico(idMonitoramentoEletronico), Charset.forName("UTF-8")) );
		map.put( "dsObservacaoDoctoServico", montarObservacoes( idDoctoServico, (String) map.get("nfeXML") ) );
		map.put( "listNrNotas", notaFiscalConhecimentoService.findNrNotaByIdConhecimento(idDoctoServico) );
		
		return map;
	}
	
	public Map<String, Object> executeMontarInfNse(Long idDoctoServico) {
		MonitoramentoDocEletronico monitoramentoDocEletronico = findMonitoramentoDocEletronicoByIdDoctoServico(idDoctoServico);
		if(monitoramentoDocEletronico != null && monitoramentoDocEletronico.getIdMonitoramentoDocEletronic() != null){
			return executeMontarInfNse(idDoctoServico, monitoramentoDocEletronico.getIdMonitoramentoDocEletronic());
		}
		return null;
	}
	
	
	/**executeGetProximoNumeroRPS - ver LMS-8003
	 * @param idDoctoServico
	 * @return proximoNumeroRps
	 */
	public Long executeGetProximoNumeroRPS(DoctoServico doctoServico){
		Filial filialSessao = SessionUtils.getFilialSessao();
		Long proximoNumeroRps = null;
		
		ConteudoParametroFilial rpsSeqNrFiscal = conteudoParametroFilialService.findByNomeParametro(filialSessao.getIdFilial(), "RPS_SEQ_NR_FISCAL", false, true);

		if(rpsSeqNrFiscal != null && SIM.equals(rpsSeqNrFiscal.getVlConteudoParametroFilial())){

			ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(filialSessao.getIdFilial(), "PREF_WEBSERVICE", false, true);
			
			if(conteudoParametroFilial == null || SIM.equals(conteudoParametroFilial.getVlConteudoParametroFilial())){ // SIM ou NULO
				proximoNumeroRps = emitirDocumentoService.generateProximoNumeroRPS(filialSessao.getIdFilial());
			}
		}
		
		if(proximoNumeroRps != null){
			observacaoDoctoServicoService.removeByIdDoctoServicoAndDsObservacao(doctoServico.getIdDoctoServico(), DESCRICAO_DOCTO_SERVICO_REJEICAO_RPS);
			notaFiscalEletronicaService.storeObsDoctoServico(doctoServico, proximoNumeroRps);
		}
		
		return proximoNumeroRps;
	}

	public void validateNrDocumentoEletronico(Long idFilialOrigemDocumento, Long nrDocumentoEletronico) {
		MonitoramentoDocEletronico monitoramentoDocEletronico = buscaUltimoMonitoramento(idFilialOrigemDocumento);
		if(monitoramentoDocEletronico != null && monitoramentoDocEletronico.getNrDocumentoEletronico() != null){
			Long ref = monitoramentoDocEletronico.getNrDocumentoEletronico() + 1L;
			if((isObrigaNFSSubsequente(idFilialOrigemDocumento) && nrDocumentoEletronico.compareTo(ref) != 0) ||
					(!isObrigaNFSSubsequente(idFilialOrigemDocumento) && nrDocumentoEletronico.compareTo(monitoramentoDocEletronico.getNrDocumentoEletronico()) <= 0)){
					throw new BusinessException("LMS-04555");
			}
			
		}
	}

	public void validateDtAutorizacao(Long idFilialOrigemDocumento, YearMonthDay dtAutorizacao) {
		MonitoramentoDocEletronico monitoramentoDocEletronico = buscaUltimoMonitoramento(idFilialOrigemDocumento);
		if (monitoramentoDocEletronico != null && monitoramentoDocEletronico.getDhAutorizacao() != null
				&& JTDateTimeUtils.comparaData(JTDateTimeUtils.yearMonthDayToDateTime(dtAutorizacao), monitoramentoDocEletronico.getDhAutorizacao()) < 0) {
			throw new BusinessException("LMS-04556");
		}
	}
	
	private MonitoramentoDocEletronico buscaUltimoMonitoramento(Long idFilialOrigemDocumento) {
		MonitoramentoDocEletronico retorno = null;
		Long nrDoctoEletronicoMAX = this.findMaxNrDoctoEletronicoByIdFilial(idFilialOrigemDocumento);
		if(nrDoctoEletronicoMAX != null){
			retorno = this.findMonitoramentosByNrDoctoEletronicoByIdFilial(nrDoctoEletronicoMAX, idFilialOrigemDocumento);
		}
		return retorno;
	}
	
	public boolean isObrigaNFSSubsequente(Long idFilial) {
		String indicadorObrigaNFSSubsequente = (String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilial, "OBRIGA_NFS_SUBSEQ", false);
		return SIM.equals(indicadorObrigaNFSSubsequente);
    }
	
	public Long findMaxNrDoctoEletronicoByIdFilial(Long idFilial){
		return getMonitoramentoDocEletronicoDao().findMaxNrDoctoEletronicoByIdFilial(idFilial);
	}
	
	public MonitoramentoDocEletronico findMonitoramentosByNrDoctoEletronicoByIdFilial(Long nrDocumentoEletronico, Long idFilial) {
		return getMonitoramentoDocEletronicoDao().findMonitoramentosByNrDoctoEletronicoByIdFilial(nrDocumentoEletronico, idFilial);
	}

	public void updateDhEnvioEmail(Long idMonitoramento, String destinatario){
		getMonitoramentoDocEletronicoDao().updateDhEnvioEmail(idMonitoramento, destinatario);
	}
	
	public void updateEnvioCTeCliente(Long idMonitoramentoDocEletronic, DateTime dhEnvio, String dsEnvioEmail, String xml) {
		getMonitoramentoDocEletronicoDao().updateEnvioCTeCliente(idMonitoramentoDocEletronic, dhEnvio, dsEnvioEmail, xml);
	}
	
	public String updateDhEnvioEmail(Long idMonitoramento, String destinatario, DateTime dhEnvio, String xml, String chaveXml, Filial filial) {
		return getMonitoramentoDocEletronicoDao().updateDhEnvioEmail(idMonitoramento, destinatario, dhEnvio, xml, chaveXml, filial);
	}

	
	public void executeUpdateNroNfse(Long idMonitoramentoDocEletronic, Long nrDocumentoEletronico, DateTime dhAutorizacao) {
		getMonitoramentoDocEletronicoDao().executeUpdateNroNfse(idMonitoramentoDocEletronic, nrDocumentoEletronico, dhAutorizacao);
	}

	public  Map<String, Object> findDadosEnvioCTeClienteByNrChave(String nrChave){
		return getMonitoramentoDocEletronicoDao().findDadosEnvioCTeClienteByNrChave(nrChave);
	}
	
	public MonitoramentoDocEletronico findByNrChave(final String nrChave){
		return getMonitoramentoDocEletronicoDao().findByNrChave(nrChave);
	}
	
	public List<MonitoramentoDocEletronico> findMonitoramentosByControleCarga(Long idControleCarga) {
	    return getMonitoramentoDocEletronicoDao().findMonitoramentosByControleCarga(idControleCarga);
	}

	public List<MonitoramentoDocEletronico> findListByNrChave(String nrChave){
		return getMonitoramentoDocEletronicoDao().findListByNrChave(nrChave);
	}
        
        /**
        * Método responsável por gerar os dados de CT-e.
        */
        public List<DocumentoEnvioAverbacao> generateDadosMonitoramentoDocEletronico() {
            List<DocumentoEnvioAverbacao> listEnvio = new ArrayList<>();
            List<AverbacaoDoctoServico> averbacaoPendenteEnvio = averbacaoDoctoServicoService.findAberbacaoPendenteEnvio();
            for (AverbacaoDoctoServico averb : averbacaoPendenteEnvio) {
                if(averb.getTpDestino() == null){
                    averb.setTpDestino(TP_DESTINO_ATEM);
                }

                Map<String, Object> dados = conhecimentoService.findConhecimentoAberbacao(averb.getDoctoServico().getIdDoctoServico());

                if (MapUtils.isNotEmpty(dados)) {
                    MonitoramentoDocEletronico monitoramentoDocEletronico = findMonitoramentoDocEletronicoByIdDoctoServico(averb.getDoctoServico().getIdDoctoServico());

                    if (TP_WEBSERVICE_AVERBACAO_CANCELADO.equals(averb.getTpWebservice()) && !TP_WEBSERVICE_AVERBACAO_CANCELADO.equals(monitoramentoDocEletronico.getTpSituacaoDocumento().getValue())) {
                        continue;
                    }

                    DocumentoEnvioAverbacao documentoEnvioAverbacao = DocumentoEnvioAverbacaoHelper.createDocumentoEnvioAverbacao(averb.getIdAverbacaoDoctoServico(), averb.getTpWebservice(), dados);

                    if (TP_DESTINO_ATEM.equals(averb.getTpDestino())) {
                        documentoEnvioAverbacao.setXml(new String(monitoramentoDocEletronico.getDsDadosDocumento(), Charset.forName("UTF-8")));
                    }

                    documentoEnvioAverbacao.setTpDestino(averb.getTpDestino());
                    documentoEnvioAverbacao.setWebService(WEB_SERVICE_CTE_AVERBACAO);

                    averbacaoDoctoServicoService.updateDataEnvio(averb.getIdAverbacaoDoctoServico());

                    listEnvio.add(documentoEnvioAverbacao);
                }
            }
            return listEnvio;
        }
        
        public CTeDMN storeDadosCancelamentoConhecimentoAverbacao(CTeDMN cTeDMN) {
            if (cTeDMN != null && cTeDMN.getNrChave() != null && cTeDMN.getXml() != null) {
                MonitoramentoDocEletronico mde = findByNrChave(cTeDMN.getNrChave());
                if (mde != null) {
                    AverbacaoDoctoServico ads = averbacaoDoctoServicoService.findCanceladoPendenteByIdDoctoServico(mde.getDoctoServico().getIdDoctoServico());
                    if (ads != null) {
                        ads.setTpWebservice(TP_WEBSERVICE_AVERBACAO_CANCELADO);
                        averbacaoDoctoServicoService.store(ads);
                        mde.setDsDadosDocumento(cTeDMN.getXml().getBytes(Charset.forName("UTF-8")));
                        store(mde);
                    }
                }

				cTeDMN.setIdDoctoServico(mde.getDoctoServico().getIdDoctoServico());
                cTeDMN.setTipoSituacaoDocumento(mde.getTpSituacaoDocumento().getValue());
            }
			return cTeDMN;
        }

	public void setParcelaDoctoServicoService(
			ParcelaDoctoServicoService parcelaDoctoServicoService) {
		this.parcelaDoctoServicoService = parcelaDoctoServicoService;
	}

	public void setValorCustoService(ValorCustoService valorCustoService) {
		this.valorCustoService = valorCustoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setInscricaoEstadualService(
			InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public void setDadosComplementoService(
			DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}

	public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	public void setParametroGeralServiceService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
}

	public void setGerarConhecimentoEletronicoXMLService(GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService){
		this.gerarConhecimentoEletronicoXMLService = gerarConhecimentoEletronicoXMLService;
	}

	public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService){
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}
	
	public void setIncluirEventoRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventoRastreabilidadeInternacionalService) {
		this.incluirEventoRastreabilidadeInternacionalService = incluirEventoRastreabilidadeInternacionalService;
	}
	
	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	
	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public byte[] findXmlByDoctoServico(Long idMonitoramentoEletronico) {
		return getMonitoramentoDocEletronicoDao().findXmlByDoctoServico(idMonitoramentoEletronico);
	}

	public byte[] findXmlMonitByIdDoctoServico(Long idMonitoramentoEletronico) {
		return getMonitoramentoDocEletronicoDao().findXmlMonitByIdDoctoServico(idMonitoramentoEletronico);
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public void setObservacaoDoctoServicoService(
			ObservacaoDoctoServicoService observacaoDoctoServicoService) {
		this.observacaoDoctoServicoService = observacaoDoctoServicoService;
	}

	public void setImpostoServicoService(ImpostoServicoService impostoServicoService) {
		this.impostoServicoService = impostoServicoService;
	}

	public void setConfiguracoesFacadeImpl(ConfiguracoesFacadeImpl configuracoesFacadeImpl) {
		this.configuracoesFacadeImpl = configuracoesFacadeImpl;
}
	public ParametroFilialService getParametroFilialService() {
		return parametroFilialService;
	}

	public void setParametroFilialService(
			ParametroFilialService parametroFilialService) {
		this.parametroFilialService = parametroFilialService;
	}

	public EmitirDocumentoService getEmitirDocumentoService() {
		return emitirDocumentoService;
	}

	public void setEmitirDocumentoService(
			EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}

	public void setConhecimentoDAO(ConhecimentoDAO conhecimentoDAO) {
		this.conhecimentoDAO = conhecimentoDAO;
}

	public NFEConjugadaService getNfeConjugadaService() {
		return nfeConjugadaService;
	}

	public void setNfeConjugadaService(NFEConjugadaService nfeConjugadaService) {
		this.nfeConjugadaService = nfeConjugadaService;
	}

	public MoedaService getMoedaService() {
		return moedaService;
}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public PaisService getPaisService() {
		return paisService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public DensidadeService getDensidadeService() {
		return densidadeService;
	}

	public void setDensidadeService(DensidadeService densidadeService) {
		this.densidadeService = densidadeService;
	}
	
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

    public void setControleTrechoService(ControleTrechoService controleTrechoService) {
        this.controleTrechoService = controleTrechoService;
    }

    public void setFilialRotaCcService(FilialRotaCcService filialRotaCcService) {
        this.filialRotaCcService = filialRotaCcService;
    }
    
    // LMSA-6322
    public void setDispositivoUnitizacaoService (DispositivoUnitizacaoService dispositivoUnitizacaoService) {
    	this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
    }

	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	public void setAgendamentoDoctoServicoService(AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}
    
	public DoctoServico findDoctoServicoByNrChave(String nrChave){
		return getMonitoramentoDocEletronicoDao().findDoctoServicoByNrChave(nrChave);
	}

	// LMSA-7369
	public void setDoctoServicoSegurosService(DoctoServicoSegurosService doctoServicoSegurosService) {
		this.doctoServicoSegurosService = doctoServicoSegurosService;
	}
	
	public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
		this.apoliceSeguroService = apoliceSeguroService;
	}
	
	public void setAverbacaoDoctoServicoService(AverbacaoDoctoServicoService averbacaoDoctoServicoService) {
		this.averbacaoDoctoServicoService = averbacaoDoctoServicoService;
	}

	public void setSeguroClienteService(SeguroClienteService seguroClienteService) {
		this.seguroClienteService = seguroClienteService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

    public void setNaoConformidadeService(
            NaoConformidadeService naoConformidadeService) {
        this.naoConformidadeService = naoConformidadeService;
    }
    
    public void setOcorrenciaNaoConformidadeService(
            OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
        this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
    }

    @Transactional
	public List<TypedFlatMap> gerarXMLConhecimentoEletronico(Long idFatura) {
		final List<Object[]> list = getMonitoramentoDocEletronicoDao().findByIdFatura(idFatura);
		final ParserResultConhecimentoEletronico parse = new ParserResultConhecimentoEletronico();
		return parse.parseToMap(list);
	}
}
