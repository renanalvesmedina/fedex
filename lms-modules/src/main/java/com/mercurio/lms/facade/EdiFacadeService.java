package com.mercurio.lms.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.dao.ManifestoEntregaDocumentoDAO;
import com.mercurio.lms.entrega.model.service.CancelarEntregaAlterarOcorrenciaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;
import com.mercurio.lms.vol.model.service.VolInformarOcorrenciasService;

/**
 * 
 * Fachada para acesso a métodos comuns do EDI.
 * @author Daiane Snel
 * @spring.bean id="lms.facade.ediFacadeService"
 *
 */
@ServiceSecurity
public class EdiFacadeService {

	private static final String CD_EVENTO_CHEGADA_CLIENTE = "CD_EVENTO_CHEGADA_CLIENTE";
	private static final long CD_EVENTO_CHEGADA_NO_CLIENTE = 30;
	private static final String DATETIME_WITH_SECONDS_PATTERN = "dd/MM/yyyy HH:mm:ss";
	private static final String MOTIVO_CANCEL_OCORREN_PARCEIRO = "MOTIVO_CANCEL_OCORREN_PARCEIRO";
	private static final String CNPJ_ALTERACAO_OCORRENCIA_PARCEIRO = "CNPJ_ALTERACAO_OCORRENCIA_PARCEIRO";

	private Logger log = LogManager.getLogger(this.getClass());
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private UsuarioService usuarioService;
	private PaisService paisService;
	private VolDadosSessaoService volDadosSessaoService;
	private ManifestoService manifestoService;
	private ManifestoEntregaDocumentoDAO manifestoEntregaDocumentoDAO;
	private ConhecimentoService conhecimentoService;
	private FilialService filialService;
	private EmpresaService empresaService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private ControleCargaService controleCargaService;
	private ManifestoEntregaService manifestoEntregaService;
	private ConfiguracoesFacade configuracoesFacade;
	private VolInformarOcorrenciasService volInformarOcorrenciasService;
	private CancelarEntregaAlterarOcorrenciaService cancelarEntregaAlterarOcorrenciaService;
	private ParametroGeralService parametroGeralService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private DoctoServicoService doctoServicoService;
	
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "getCdEventoChegadaCliente", authenticationRequired=false)
	public Map<String, Object> getCdEventoChegadaCliente() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(CD_EVENTO_CHEGADA_CLIENTE, configuracoesFacade.getValorParametro(CD_EVENTO_CHEGADA_CLIENTE));
		return m;
	}
	
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "baixaParceiros", authenticationRequired=false)
	public Map<String, List<String>> baixaParceiros(Map<String, List<Map<String, Object>>> dadosParceiros){
		List<String> errosLMS = new ArrayList<String>();		
		for (Map<String, Object> parceiro : dadosParceiros.get("baixaParceiros")) {
			String error = "";
			try {
				
				if ("S".equals(parceiro.get("indicadorOcorrenciaPendencia"))){
					error = executeRegistrarOcorrenciaPendencia(parceiro);
					
				} else if (parceiro.get("cdOcorrenciaEntrega").toString().equals((String) configuracoesFacade.getValorParametro(CD_EVENTO_CHEGADA_CLIENTE))) {
					error = executeOcorrencia(parceiro);
					
				} else if ("S".equals(parceiro.get("indicadorAlteracaoOcorrencia"))){
					error = executeCancelarOcorrencia(parceiro);
					
				} else {
					error = executeBaixaDocumentos(parceiro);
					
				}
				if(error!="" && !error.isEmpty()){
					errosLMS.add(error);
				}
			} catch (Exception e) {
				error = e.getMessage();
				errosLMS.add(error);
				log.error(e);
			}
		}	
		Map<String, List<String>> mapError = new HashMap<String, List<String>>();
		mapError.put("errosLMS", errosLMS);
		return mapError;
	}
	
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "findManifestoEntregaAndOcorrenciaByIdDoctoServico", authenticationRequired=false)
	public List<Map<String, Object>> findManifestoEntregaAndOcorrenciaByIdDoctoServico(Map<String, Object> parameters) {
		List<Map<String, Object>> l = (List<Map<String, Object>>)parameters.get("listBaixaParceiro");
		
		for (Map<String, Object> mapArquivo : l) {
			Long idDoctoServico = (Long)mapArquivo.get("idDoctoServico");
			DateTime dhOcorrencia = (DateTime) mapArquivo.get("dhOcorrencia");
			
			List result = manifestoEntregaDocumentoService.findManifestoEntregaByDoctoServicoAndTpStatusManifesto(idDoctoServico);
			if(CollectionUtils.isNotEmpty(result)){
				Map<String, Object> mapResult = (Map<String, Object>) result.get(0);
				Long idOcorrenciaEntrega = (Long) mapResult.get("idOcorrenciaEntrega");
				DateTime dhBaixa = (DateTime) mapResult.get("dhBaixa");
				
				if (LongUtils.hasValue(idOcorrenciaEntrega) && dhOcorrencia.isAfter(dhBaixa)) {
					mapArquivo.put("alteracaoOcorrencia", "S");
				} else {
					mapArquivo.put("alteracaoOcorrencia", "N");
				}
			} else {
				mapArquivo.put("alteracaoOcorrencia", "N");
			}
		}
		
		return l;
	}
	
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "findParametroCNPJAlteracaoOcorrenciaParceiro", authenticationRequired=false)
	public Map<String, Object> findParametroCNPJAlteracaoOcorrenciaParceiro(Map<String, Object> parameters) {
		String[] cnpjs = parametroGeralService.findByNomeParametro(CNPJ_ALTERACAO_OCORRENCIA_PARCEIRO, false).getDsConteudo().split(";");
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("cnpjs", cnpjs);
		return retorno;
	}
	
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "findParametroEmailsErroFedexByCdFilial", authenticationRequired=false)
	public Map<String, Object> findParametroEmailsErroFedexByCdFilial(Map<String, Object> parameters) {
		
		List<String> emailsList = new ArrayList<String>();
		
		Filial filialFedex = conteudoParametroFilialService.findFilialByConteudoParametro("CD_FILIAL_FEDEX", (String) parameters.get("cdFilial"));
		
        if (filialFedex != null){
        	String[] emails = ((String) conteudoParametroFilialService.findConteudoByNomeParametro(filialFedex.getIdFilial(), "DEST_EMAIL_ERROS_FDX", false)).split(";");
        	emailsList = Arrays.asList(emails);
        }

		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("emails", emailsList);
		return retorno;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private String executeRegistrarOcorrenciaPendencia(Map<String, Object> parceiro) {

		String error = "";
		Session session = null;
		TypedFlatMap tfmParceiro = new TypedFlatMap(parceiro);
		
		Long idDoctoServicoArquivo = tfmParceiro.getLong("idDoctoServico");
		Short cdOcorrenciaEntregaArquivo = tfmParceiro.getShort("cdOcorrenciaEntrega");
		DateTime dhOcorrenciaArquivo = tfmParceiro.getDateTime("dhOcorrencia");

		Long nrConhecimentoArquivo = tfmParceiro.getLong("nrConhecimento");
		String sgFilialArquivo = tfmParceiro.getString("sgFilial");
		
		try {
			
			DoctoServico doctoServico = doctoServicoService.findById(idDoctoServicoArquivo);
			
			session = setDadosSessao(doctoServico);
			
			OcorrenciaPendencia ocorrenciaPendencia =  ocorrenciaPendenciaService.findByCodigoOcorrencia(cdOcorrenciaEntregaArquivo);

			
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
			typedFlatMap.put("ocorrenciaPendencia.blApreensao", ocorrenciaPendencia.getBlApreensao());
			typedFlatMap.put("ocorrenciaPendencia.idOcorrenciaPendencia",ocorrenciaPendencia.getIdOcorrenciaPendencia());
			typedFlatMap.put("ocorrenciaPendencia.evento.idEvento",ocorrenciaPendencia.getEvento().getIdEvento());
			typedFlatMap.put("ocorrenciaPendencia.tpOcorrencia",ocorrenciaPendencia.getTpOcorrencia().getValue());
			typedFlatMap.put("dhOcorrencia",dhOcorrenciaArquivo);
			
			ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(typedFlatMap);
			
			session.getTransaction().commit();
		} catch (BusinessException be) {
			error = "Filial: "+sgFilialArquivo+";Conhecimento: "+nrConhecimentoArquivo+ ";" + configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments());
		} catch (Exception ex) {
			error = "Filial: "+sgFilialArquivo+";Conhecimento: "+nrConhecimentoArquivo+ ";" + ex.getMessage();
		} 
		finally {
			if(session!=null){
				session.flush();
				session.close();
			}
		}
		return error;
	}

	private Session setDadosSessao(DoctoServico doctoServico) {
		Session session;
		session = manifestoEntregaDocumentoDAO.getAdsmHibernateTemplate().getSessionFactory().openSession();			
		session.beginTransaction();
		Usuario usuario = usuarioService.findUsuarioByLogin("integracao");
		Filial filial = doctoServico.getFilialByIdFilialDestino();
		Pais pais = paisService.findByIdPessoa(filial.getIdFilial());			
		volDadosSessaoService.executeDadosSessaoBanco(usuario, filial, pais);
		return session;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private String executeCancelarOcorrencia(Map<String, Object> parceiro) {

		String error = "";
		Session session = null;
		TypedFlatMap tfmParceiro = new TypedFlatMap(parceiro);
		
		Long idDoctoServicoArquivo = tfmParceiro.getLong("idDoctoServico");
		Short cdOcorrenciaEntregaArquivo = tfmParceiro.getShort("cdOcorrenciaEntrega");
		
		DateTime dhOcorrenciaArquivo = tfmParceiro.getDateTime("dhOcorrencia");
		DateTime dhCancelamentoOcorrenciaArquivo = dhOcorrenciaArquivo;
		
		String nmRecebedorArquivo = tfmParceiro.getString("nmRecebedor");
		String motivoOcorrenciaCanceladaArquivo = parametroGeralService.findByNomeParametro(MOTIVO_CANCEL_OCORREN_PARCEIRO, false).getDsConteudo();
		String sgFilialArquivo = tfmParceiro.getString("sgFilial");
		String tpDocumentoServicoArquivo = tfmParceiro.getString("tpDocumentoServico");
		Long nrConhecimentoArquivo = tfmParceiro.getLong("nrConhecimento");
		
		Long idManifestoEntregaDocumentoUltimo = null;
		Long idManifestoEntregaUltimo = null;
		Long idOcorrenciaEntregaUltimo = null;
		String obManifestoEntregaDocumentoUltimo = null;
		DateTime dhOcorrenciaUltimo = null;
		
		try {
			
			List result = manifestoEntregaDocumentoService.findManifestoEntregaByDoctoServicoAndTpStatusManifesto(idDoctoServicoArquivo);
			Map map = (Map) result.get(0);
			idManifestoEntregaUltimo = (Long)map.get("idManifestoEntrega");
			idManifestoEntregaDocumentoUltimo = (Long)map.get("idManifestoEntregaDocumento");
			idOcorrenciaEntregaUltimo = (Long)map.get("idOcorrenciaEntrega");
			obManifestoEntregaDocumentoUltimo = (String)map.get("obManifestoEntregaDocumento");
			dhOcorrenciaUltimo = (DateTime)map.get("dhBaixa");
			
			session = manifestoEntregaDocumentoDAO.getAdsmHibernateTemplate().getSessionFactory().openSession();			
			session.beginTransaction();
			Usuario usuario = usuarioService.findUsuarioByLogin("integracao");
			Manifesto manifesto = (Manifesto) manifestoService.findById(idManifestoEntregaUltimo);   
			Filial filial = manifesto.getControleCarga().getFilialByIdFilialOrigem();
			Pais pais = paisService.findByIdPessoa(filial.getIdFilial());			
			volDadosSessaoService.executeDadosSessaoBanco(usuario, filial, pais);
			
			OcorrenciaEntrega ocorrenciaEntregaArquivo = ocorrenciaEntregaService.findOcorrenciaEntregaByCodigoTipo(cdOcorrenciaEntregaArquivo);
			
			TypedFlatMap parametros = new TypedFlatMap();
			parametros.put("idOcorrenciaEntrega", idOcorrenciaEntregaUltimo);
			parametros.put("manifestoEntregaDocumento.idManifestoEntregaDocumento", idManifestoEntregaDocumentoUltimo);
			parametros.put("obManifestoEntregaDocumento", obManifestoEntregaDocumentoUltimo);
			parametros.put("dhBaixa", dhOcorrenciaUltimo);
			
			parametros.put("ocorrenciaEntrega.cdOcorrenciaEntrega", ocorrenciaEntregaArquivo.getCdOcorrenciaEntrega());
			parametros.put("ocorrenciaEntrega.idOcorrenciaEntrega", ocorrenciaEntregaArquivo.getIdOcorrenciaEntrega());
			parametros.put("idDoctoServico", idDoctoServicoArquivo);
			parametros.put("filial.idFilial", filial.getIdFilial());
			parametros.put("obMotivoAlteracao", motivoOcorrenciaCanceladaArquivo);
			parametros.put("nmRecebedor2", nmRecebedorArquivo);
			parametros.put("dhCancelamentoOcorrenciaArquivo", dhCancelamentoOcorrenciaArquivo);
			parametros.put("complementoBaixaSPP", new DomainValue("44"));
			parametros.put("tipoEntregaParcial", new DomainValue());
			parametros.put("doctoServico.tpDocumentoServico", tpDocumentoServicoArquivo);
			
			cancelarEntregaAlterarOcorrenciaService.executeCancelarOcorrencia(parametros);
			
			session.getTransaction().commit();
		} catch (BusinessException be) {
			error = "Filial: "+sgFilialArquivo+";Conhecimento: "+nrConhecimentoArquivo+";Manifesto: "+idManifestoEntregaUltimo + ";" + configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments());
		} catch (Exception ex) {
			error = "Filial: "+sgFilialArquivo+";Conhecimento: "+nrConhecimentoArquivo+";Manifesto: "+idManifestoEntregaUltimo + ";" + ex.getMessage();
		} 
		finally {
			if(session!=null){
				session.flush();
				session.close();
			}
		}
		return error;
	}
	
	private String executeOcorrencia(Map<String, Object> parceiro){
		String error = "";
		String sgFilial = (String) parceiro.get("sgFilial");
		Long nrConhecimento = (Long) parceiro.get("nrConhecimento");
		
		try {
			Long idDoctoServico = (Long)parceiro.get("idDoctoServico");
			List result = manifestoEntregaDocumentoService.findManifestoEntregaByDoctoServicoAndTpStatusManifesto(idDoctoServico);
			
			Map map = (Map) result.get(0);
			Long idManifestoEntregaUltimo = (Long) map.get("idManifestoEntrega");
			Long idManifestoEntregaDocumentoUltimo = (Long)map.get("idManifestoEntregaDocumento");
			
			ManifestoEntregaDocumento med = manifestoEntregaDocumentoService.findById(idManifestoEntregaDocumentoUltimo);
			ManifestoEntrega manifestoEntrega = manifestoEntregaService.findById(idManifestoEntregaUltimo);
			Manifesto manifesto = (Manifesto) manifestoService.findById(manifestoEntrega.getManifesto().getIdManifesto());   
			Long idControleCarga = manifesto.getControleCarga().getIdControleCarga();
			ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
			Long idMeioTransporte = controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte();
			Long idFilial = controleCarga.getFilialByIdFilialOrigem().getIdFilial();
			DateTime dhOcorrencia =  (DateTime)parceiro.get("dhOcorrencia");
			
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idMeioTransporte", idMeioTransporte);
			tfm.put("idControleCarga", idControleCarga);
			tfm.put("idConhecimento", idDoctoServico);
			tfm.put("cdEvento", CD_EVENTO_CHEGADA_NO_CLIENTE);
			tfm.put("idFilial", idFilial);
			tfm.put("dhSolicitacao", JTDateTimeUtils.formatDateTimeToString(dhOcorrencia, DATETIME_WITH_SECONDS_PATTERN));
			tfm.put("usuarioSessao", "integracao");
		
			volInformarOcorrenciasService.executeInformarOcorrenciaPorEDIParaBaixaParceiro(tfm);
		} catch (BusinessException be) {
			error = "Filial: "+sgFilial+";Conhecimento: "+nrConhecimento + ";" + configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments());
		} catch (Exception ex) {
			error = "Filial: "+sgFilial+";Conhecimento: "+nrConhecimento + ";" + ex.getMessage();
		} 
		
		return error;
	}	
	

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private String executeBaixaDocumentos(Map<String, Object> parceiro) {
		String error = "";
		Session session = null;
		TypedFlatMap tfm = new TypedFlatMap(parceiro);
		Long idManifesto = tfm.getLong("idManifesto");
		Long idDoctoServico = tfm.getLong("idDoctoServico");
		Short cdOcorrenciaEntrega = tfm.getShort("cdOcorrenciaEntrega");
		String tpFormaBaixa = tfm.getString("tpFormaBaixa");
		String tpEntregaParcial = tfm.getString("tpEntregaParcial");
		DateTime dhOcorrencia = tfm.getDateTime("dhOcorrencia");
		String nmRecebedor = tfm.getString("nmRecebedor");
		String rgRecebedor = tfm.getString("rgRecebedor");
		String obManifesto = tfm.getString("obManifesto");
		Boolean isValidExistenciaPceRemetente = tfm.getBoolean("isValidExistenciaPceRemetente");
		Boolean isValidExistenciaPceDestinatario = tfm.getBoolean("isValidExistenciaPceDestinatario");
		Boolean isDocumentoFisico = tfm.getBoolean("isDocumentoFisico");
		String sgFilial = tfm.getString("sgFilial");
		Long nrConhecimento = tfm.getLong("nrConhecimento");
		try {
			session = manifestoEntregaDocumentoDAO.getAdsmHibernateTemplate().getSessionFactory().openSession();			
			session.beginTransaction();
			Usuario usuario = usuarioService.findUsuarioByLogin("integracao");
			Manifesto manifesto = (Manifesto) manifestoService.findById(idManifesto);   
			Filial filial = manifesto.getControleCarga().getFilialByIdFilialOrigem();
			if (filial.getEmpresa() != null) {
				filial.setEmpresa(empresaService.findById(filial.getEmpresa().getIdEmpresa()));
			}
			Pais pais = paisService.findByIdPessoa(filial.getIdFilial());			
			volDadosSessaoService.executeDadosSessaoBanco(usuario, filial, pais);
			manifestoEntregaDocumentoService.executeBaixaDocumento(idManifesto, idDoctoServico, cdOcorrenciaEntrega, tpFormaBaixa, tpEntregaParcial, dhOcorrencia, nmRecebedor, obManifesto,
					isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario, isDocumentoFisico, rgRecebedor, null, null, SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
			session.getTransaction().commit();
		} catch (BusinessException be) {
			error = "Filial: "+sgFilial+";Conhecimento: "+nrConhecimento+";Manifesto: "+idManifesto + ";" + configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments());
		} catch (Exception ex) {
			error = "Filial: "+sgFilial+";Conhecimento: "+nrConhecimento+";Manifesto: "+idManifesto + ";" + ex.getMessage();
		} finally {
			if(session!=null){
				session.flush();
				session.close();
			}
		}
		return error;
	}
	
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "findManifestoEntregaByIdDoctoServico", authenticationRequired=false)
	public List<Map<String, Object>> findManifestoEntregaByIdDoctoServico(Map<String, List<Long>> doctosServico){
		List<Map<String, Object>> manifestos = new ArrayList<Map<String,Object>>();
		List<Long> listDoctosSevico = doctosServico.get("doctosServico");		
		for (Long doctoServico : listDoctosSevico) {
			Map<String, Object> manifesto = findManifestoEntrega(doctoServico);
			if(manifesto!=null){
				manifestos.add(manifesto);
			}
		}
		return manifestos;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> findManifestoEntrega(Long doctoServico){
		Map<String, Object> manifesto = new HashMap<String, Object>();
		try {
			manifesto = manifestoEntregaDocumentoService.findManifestoByIdDoctoServicoSemOcorrenciaEntregaEPendenteDeBaixa(doctoServico);
		} catch (Exception ex) {
			manifesto.put("idDoctoServico", doctoServico);
			manifesto.put("divergencia", ex.getMessage());
		}
		return manifesto;
	}
	
	@SuppressWarnings("unchecked")
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "findSiglaFilialByCNPJ", authenticationRequired=false)
	public Map<String, Object> findSiglaFilialByCNPJ(Map<String, Object> parametros){
		Map<String, Object> mapCnpjs = new HashMap<String, Object>();
		List<String> siglasFilial = new ArrayList<String>();				
		List<Map<String, Object>> cnpjsFilial = (List<Map<String, Object>>) parametros.get("cnpjs");
		for (Map<String, Object> cnpjFilial : cnpjsFilial) {
			Long cnpjEmissoraDoc=0L;
			if(cnpjFilial.get("cnpjEmissoraDoc")!=null){
				cnpjEmissoraDoc = (Long) cnpjFilial.get("cnpjEmissoraDoc");
			}		
			if(cnpjEmissoraDoc != 0){
				
				List<String> identificacoes = new ArrayList<String>();
				identificacoes.add(cnpjEmissoraDoc.toString());
				
				List<Filial> filiais = filialService.findFilialByArrayNrIdentificacaoPessoa(identificacoes);
				
				if (!filiais.isEmpty()) {
					siglasFilial.add(((Filial) filiais.get(0)).getSgFilial());
				}
			}
		}
		mapCnpjs.put("siglasFilial", siglasFilial);	
		return mapCnpjs;		
	}
		
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "findDoctoServicoByNrConhecimentoIdFilial", authenticationRequired=false)
	public List<Map<String, Object>> findDoctoServicoByNrConhecimentoIdFilial(Map<String, List<Long>> parametros){
		List<Map<String, Object>> retorno = conhecimentoService.findConhecimentoByNrConhecimentoIdFilial(parametros);
		return retorno;
	}
		
	@SuppressWarnings("unchecked")
	@MethodSecurity(processGroup = "edi.ediFacadeService", processName = "findCdOcorrenciaEntregaAtiva", authenticationRequired=false)
	public List<Map<String, Object>> findCdOcorrenciaEntregaAtiva(){
		List<Map<String, Object>> listOcorrencias =  ocorrenciaEntregaService.findCdOcorrenciaEntregaAtiva();
		
		String cdEventoChegadaCliente = (String) configuracoesFacade.getValorParametro(CD_EVENTO_CHEGADA_CLIENTE);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cdOcorrenciaEntrega", Short.valueOf(cdEventoChegadaCliente));
		
		listOcorrencias.add(map);
		
		return listOcorrencias;
	}
	
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
		this.volDadosSessaoService = volDadosSessaoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public void setManifestoEntregaDocumentoDAO(ManifestoEntregaDocumentoDAO manifestoEntregaDocumentoDAO) {
		this.manifestoEntregaDocumentoDAO = manifestoEntregaDocumentoDAO;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setVolInformarOcorrenciasService(
			VolInformarOcorrenciasService volInformarOcorrenciasService) {
		this.volInformarOcorrenciasService = volInformarOcorrenciasService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setManifestoEntregaService(
			ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}

	public void setCancelarEntregaAlterarOcorrenciaService(CancelarEntregaAlterarOcorrenciaService cancelarEntregaAlterarOcorrenciaService) {
		this.cancelarEntregaAlterarOcorrenciaService = cancelarEntregaAlterarOcorrenciaService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}
	
	
}
