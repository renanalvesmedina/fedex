package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.dao.PreManifestoDocumentoDAO;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.OrdemFilialFluxoService;
import com.mercurio.lms.municipios.model.service.SubstAtendimentoFilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.SolicitacaoRetiradaService;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.preManifestoDocumentoService"
 */
public class PreManifestoDocumentoService extends CrudService<PreManifestoDocumento, Long> {
	
	private static final String IS_CONFERENCIA_EM_LINHA = "isConferenciaEmLinha";

	private ControleCargaService controleCargaService;
	private ManifestoService manifestoService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private DoctoServicoService doctoServicoService;
	private DevedorDocServService devedorDocServService;
	private PreManifestoVolumeService preManifestoVolumeService;
	private EventoVolumeService eventoVolumeService;
	private OrdemFilialFluxoService ordemFilialFluxoService;
	private SolicitacaoRetiradaService solicitacaoRetiradaService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private SubstAtendimentoFilialService substAtendimentoFilialService;
	private FilialService filialService;
	private MunicipioService municipioService;
	private AwbService awbService;
	private ConfiguracoesFacade configuracoesFacade;
	private ClienteService clienteService;
	
	public Map findDoctoServico(Map criteria) {
		Map mapDoctoServico = new HashMap();

		DoctoServico doctoServico = doctoServicoService.findDoctoServicoByTpDocumentoByIdFilialOrigemByNrDoctoServicoByIdFilialLocalizacao(
			MapUtils.getString(criteria, "tpDocumentoServico"),
			MapUtils.getLong(criteria, "idFilialOrigemDoctoServico"),
			MapUtils.getLong(criteria, "nrDoctoServico"), 
			null
		);

		// Caso não encontre nenhum registro de documento.
		if (doctoServico == null) {
			throw new BusinessException("LMS-05050");
		}
		
		//Regra 1.19
		String bloqFluxoSubcontratacao = (String)configuracoesFacade.getValorParametro(
                SessionUtils.getFilialSessao().getIdFilial(), 
                "BL_BLOQ_DOC_SUB");
		if("S".equalsIgnoreCase(bloqFluxoSubcontratacao) && BooleanUtils.isTrue(doctoServico.getBlFluxoSubcontratacao())){
		    throw new BusinessException("LMS-05425");
		}
		
		if(doctoServico.getBlBloqueado() != null &&  doctoServico.getBlBloqueado()){
			OcorrenciaDoctoServico ocorrenciaDoctoServico =  ocorrenciaDoctoServicoService.findLastOcorrenciaDoctoServicoByIdDoctoServico(doctoServico.getIdDoctoServico());
			
			if(ocorrenciaDoctoServico == null){
				throw new BusinessException("LMS-05050");
			}
			
			OcorrenciaPendencia ocorrenciaPendencia =  ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio();
			
			if(ocorrenciaPendencia.getCdOcorrencia() != (short)203){
				throw new BusinessException("LMS-05050");
			}else{
				doctoServico.setBlBloqueado(false);
			}	
		}
		
		if(!ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO.equals(doctoServico.getTpDocumentoServico().getValue())) {
			validateReentregaDocumento(doctoServico.getIdDoctoServico());		
		}

		// Somente faz os testes caso o tipo do documento seja diferente de 'RRE'.
		String tpDocumentoServico = doctoServico.getTpDocumentoServico().getValue();

		// Verifica se o documento está associado a uma solicitação de retirada para poder ser incluido em
		// um manifesto de Viagem ou Entrega, caso a filial de retirada for igual da filial localização do documento, gera excessão.
		List listSolicitacaoRetirada = solicitacaoRetiradaService.findSolicitacaoRetiradaByIdDoctoServico(doctoServico.getIdDoctoServico());
		if (!listSolicitacaoRetirada.isEmpty()) {
			for (Iterator iter = listSolicitacaoRetirada.iterator(); iter.hasNext();) {
				SolicitacaoRetirada solicitacaoRetirada = (SolicitacaoRetirada) iter.next();

				if (solicitacaoRetirada.getFilialRetirada().getIdFilial().equals(doctoServico.getFilialLocalizacao().getIdFilial()) && 
						solicitacaoRetirada.getTpSituacao().getValue().equals("A")) {
					throw new BusinessException("LMS-05119");
				}
			}
		}

		String tpManifesto = MapUtils.getString(criteria, "tpManifesto");
		String dsCodigoBarras = MapUtils.getString(criteria, "nrCodigoBarras");

		Boolean conferenciaEmLinha = MapUtils.getBoolean(criteria, IS_CONFERENCIA_EM_LINHA) != null ?
				MapUtils.getBoolean(criteria, IS_CONFERENCIA_EM_LINHA) : Boolean.FALSE;

		BusinessException msg = validateLocalizacaoMercadoria(doctoServico,	tpManifesto, dsCodigoBarras, conferenciaEmLinha);
		if( msg != null ){
			throw msg; 
			}
		
		if(tpManifesto.equals("V")) {
			if (tpDocumentoServico.equals("NFT")||tpDocumentoServico.equals("RRE")) {
				throw new BusinessException("LMS-05050");
			}

			if (tpDocumentoServico.equals("NTE")) {
				Long idMunicipioOrigem = municipioService.findIdMunicipioByIdFilial(Long.valueOf(MapUtils.getString(criteria, "idFilialOrigem")));
				Long idMunicipioDestino = municipioService.findIdMunicipioByIdFilial(Long.valueOf(MapUtils.getString(criteria, "idFilialDestino")));
		    	
		    	if(!idMunicipioOrigem.equals(idMunicipioDestino)
		    			|| !doctoServico.getFilialByIdFilialOrigem().getIdFilial().equals(Long.valueOf(MapUtils.getString(criteria, "idFilialOrigem")))){
		    		throw new BusinessException("LMS-05050");
		    	}
			}

			String tpAbrangencia = MapUtils.getString(criteria, "tpAbrangencia");
			if(tpAbrangencia.equals("N")) {
				if (!tpDocumentoServico.equals("CTR")
						&& !tpDocumentoServico.equals("MDA")
						&& !tpDocumentoServico.equals("CTE")
						&& !tpDocumentoServico.equals("NTE")
				) {
					throw new BusinessException("LMS-05050");
				}
			} else if(tpAbrangencia.equals("I")) {
				if (!tpDocumentoServico.equals("CRT")) {
					throw new BusinessException("LMS-05050");
				}
			}

			// Caso o documento não possua Fluxo de Filial valido
			List idsFluxoFilial = ordemFilialFluxoService.findIdFluxoFilialByIdFilialOrigemAndIdFilialDestino(
					SessionUtils.getFilialSessao().getIdFilial(), 
					MapUtils.getLong(criteria, "idFilialDestino")
			);

			if (doctoServico.getFluxoFilial() != null) {
				if (!idsFluxoFilial.toString().contains(doctoServico.getFluxoFilial().getIdFluxoFilial().toString())) {
					mapDoctoServico.put("error", "LMS-05113");
				}
			}
			}

		mapDoctoServico.put("dsCodigoBarras", dsCodigoBarras);
		mapDoctoServico.put("idDoctoServico", doctoServico.getIdDoctoServico());
		mapDoctoServico.put("tpDocumentoServico", tpDocumentoServico);
		mapDoctoServico.put("nrDoctoServico", doctoServico.getNrDoctoServico());
		mapDoctoServico.put("qtVolumes", doctoServico.getQtVolumes());
		mapDoctoServico.put("psReal", doctoServico.getPsReal());
		mapDoctoServico.put("vlMercadoria", doctoServico.getVlMercadoria());
		mapDoctoServico.put("dtPrevEntrega", doctoServico.getDtPrevEntrega());
		mapDoctoServico.put("dhEmissao", doctoServico.getDhEmissao());
		mapDoctoServico.put("blBloqueado", doctoServico.getBlBloqueado());
		if (doctoServico.getFilialByIdFilialOrigem() != null) {
			mapDoctoServico.put("idFilialOrigem", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
			mapDoctoServico.put("sgFilialOrigem", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
			mapDoctoServico.put("nmFantasiaFilialOrigem", doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
		}
		if (doctoServico.getFilialByIdFilialDestino() != null) {
			mapDoctoServico.put("idFilialDestino", doctoServico.getFilialByIdFilialDestino().getIdFilial());
			mapDoctoServico.put("sgFilialDestino", doctoServico.getFilialByIdFilialDestino().getSgFilial());
			mapDoctoServico.put("nmFantasiaDestino", doctoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());
		}
		if (doctoServico.getMoeda() != null) {
			mapDoctoServico.put("idMoeda", doctoServico.getMoeda().getIdMoeda());
			mapDoctoServico.put("siglaSimbolo", doctoServico.getMoeda().getSiglaSimbolo());
		}
		if (doctoServico.getServico() != null) {
			mapDoctoServico.put("idServico", doctoServico.getServico().getIdServico());
			mapDoctoServico.put("dsServico", doctoServico.getServico().getDsServico());
			mapDoctoServico.put("tpModalServico", doctoServico.getServico().getTpModal().getValue());
			mapDoctoServico.put("tpAbrangenciaServico", doctoServico.getServico().getTpAbrangencia().getValue());
			if (doctoServico.getServico().getTipoServico() != null) {
				mapDoctoServico.put("blPriorizar", doctoServico.getServico().getTipoServico().getBlPriorizar());
			}
		}
		Cliente clienteRemetente = doctoServico.getClienteByIdClienteRemetente();
		if (clienteRemetente != null) {
			mapDoctoServico.put("idClienteRemetente", clienteRemetente.getIdCliente());
			mapDoctoServico.put("nmPessoaRemetente", clienteRemetente.getPessoa().getNmPessoa());
			mapDoctoServico.put("blAgendamentoPessoaFisicaRemetente", clienteRemetente.getBlAgendamentoPessoaFisica());
			mapDoctoServico.put("blAgendamentoPessoaJuridicaRemetente", clienteRemetente.getBlAgendamentoPessoaJuridica());
		}
		Cliente clienteDestinatario = doctoServico.getClienteByIdClienteDestinatario();
		if (clienteDestinatario != null) {
			mapDoctoServico.put("idClienteDestinatario", clienteDestinatario.getIdCliente());
			mapDoctoServico.put("nmPessoaDestinatario", clienteDestinatario.getPessoa().getNmPessoa());
			mapDoctoServico.put("tpPessoaDestinatario", clienteDestinatario.getPessoa().getTpPessoa().getValue());
		}
		Cliente clienteConsignatario = doctoServico.getClienteByIdClienteConsignatario();
		if (clienteConsignatario != null) {
			mapDoctoServico.put("idClienteConsignatario", clienteConsignatario.getIdCliente());
			mapDoctoServico.put("nmPessoaConsignatario", clienteConsignatario.getPessoa().getNmPessoa());
			mapDoctoServico.put("tpPessoaConsignatario", clienteConsignatario.getPessoa().getTpPessoa().getValue());
		}
		if (doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal() != null) {
			mapDoctoServico.put("idRotaColetaEntrega", doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal().getIdRotaColetaEntrega());
		} else {
			if (doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid() != null) {
				mapDoctoServico.put("idRotaColetaEntrega", doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid().getIdRotaColetaEntrega());
			}
		}
		if (doctoServico.getPaisOrigem() != null) {
			mapDoctoServico.put("idPais", doctoServico.getPaisOrigem().getIdPais());
		}
		
		if (MapUtils.getBoolean(criteria, IS_CONFERENCIA_EM_LINHA)) {
			mapDoctoServico.put(IS_CONFERENCIA_EM_LINHA, MapUtils.getBoolean(criteria, IS_CONFERENCIA_EM_LINHA));
		}
		
		boolean haveAwb = false;  
		if(doctoServico instanceof Conhecimento){
			Conhecimento conhecimento = (Conhecimento)doctoServico;				
			if(CollectionUtils.isNotEmpty(conhecimento.getCtoAwbs()) 
					&& conhecimento.getCtoAwbs().get(0) != null 
					&& ((CtoAwb)conhecimento.getCtoAwbs().get(0)).getAwb().getFilialByIdFilialOrigem() != null 
					&& !ConstantesExpedicao.TP_STATUS_CANCELADO.equals(((CtoAwb)conhecimento.getCtoAwbs().get(0)).getAwb().getTpStatusAwb().getValue())
					&& ((CtoAwb)conhecimento.getCtoAwbs().get(0)).getAwb().getFilialByIdFilialOrigem().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())
					){
				haveAwb = true;
			}
		}
		mapDoctoServico.put("haveAwb", haveAwb);
		
		return mapDoctoServico;
	}
	

	public BusinessException validateLocalizacaoMercadoria(DoctoServico doctoServico, String tpManifesto, String nrCodigoBarras ){
		return validateLocalizacaoMercadoria(doctoServico, tpManifesto, nrCodigoBarras, null);
	}
	
	public BusinessException validateLocalizacaoMercadoria(DoctoServico doctoServico, String tpManifesto, String nrCodigoBarras, Boolean isConferenciaEmLinha){
		Short codLocalizacaoMercadoria = 0;
		Short[] listLocalizacao = null;
		Boolean isDoctoOnFilial = false;

		if( doctoServico.getLocalizacaoMercadoria() != null ){
			codLocalizacaoMercadoria = doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
		}

		if( doctoServico.getFilialLocalizacao() != null ){
			isDoctoOnFilial = doctoServico.getFilialLocalizacao().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial());
		}
		
		if(tpManifesto.equals("V")) {
			listLocalizacao = new Short[]{24,28,34};
		} else if(tpManifesto.equals("E") && !doctoServico.getTpDocumentoServico().getValue().equals("RRE")){
			listLocalizacao = new Short[]{24,33,34,35,43};
		}
		boolean codigoBarrasInformado = (nrCodigoBarras != null  && !"".equals(nrCodigoBarras)) || (doctoServico.getDsCodigoBarras() != null && !"".equals(doctoServico.getDsCodigoBarras()));
		if(!(SessionUtils.getFilialSessao().getBlSorter())){
			if (!BooleanUtils.isTrue(isConferenciaEmLinha) && !isDoctoOnFilial) {
				return new BusinessException("LMS-05088");
			
			} else if (!this.validateDoctoServicoTerminalOrEmDescarga(listLocalizacao, codLocalizacaoMercadoria)) {
				return new BusinessException("LMS-05089");
			}
		} else if(doctoServico != null && !codigoBarrasInformado){
				 
			codLocalizacaoMercadoria = doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
			isDoctoOnFilial = doctoServico.getFilialLocalizacao().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial()) ? true : false;
			
			if (!BooleanUtils.isTrue(isConferenciaEmLinha) && !isDoctoOnFilial) {
				throw new BusinessException("LMS-05088");				
			}	
			
			if (!this.validateDoctoServicoTerminalOrEmDescarga(listLocalizacao, codLocalizacaoMercadoria)) {
				return new BusinessException("LMS-05089");	
			}
		}
		return null;
	}
	
	private boolean validateDoctoServicoTerminalOrEmDescarga(Short[] listLocalizacao, Short codLocalizacaoMercadoria) {
		return ArrayUtils.contains(listLocalizacao, codLocalizacaoMercadoria);
	}
	
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public ManifestoService getManifestoService() {
		return manifestoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}	
	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return carregamentoDescargaService;
	}
	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}
	public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
		return incluirEventosRastreabilidadeInternacionalService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	
	/**
	 * Recupera uma instância de <code>PreManifestoDocumento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public PreManifestoDocumento findById(java.lang.Long id) {
        return (PreManifestoDocumento)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(final java.lang.Long id) {
    	final PreManifestoDocumento preManifestoDocumento = findById(id);
    	//final Manifesto manifesto = preManifestoDocumento.getManifesto();
    	if(preManifestoDocumento != null) {
    		/** LMS-647 - Exclui os volumes do Docto que foi removido do Manifesto */
    		preManifestoVolumeService.removeByIdPreManifestoDocto(preManifestoDocumento.getIdPreManifestoDocumento());
    	}
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(final List<Long> ids) {
    	for (final Long id : ids) {
			this.removeById(id);
    }
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(PreManifestoDocumento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPreManifestoDocumentoDAO(PreManifestoDocumentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PreManifestoDocumentoDAO getPreManifestoDocumentoDAO() {
        return (PreManifestoDocumentoDAO) getDao();
    }
   
    /**
     * Método que retorna uma list de objetos de PreManifestoDocumento pelo ID do DoctoServico
     * @param idDoctoServico
     * @return
     */
    public List findPreManifestoDocumentoByIdDoctoServico(Long idDoctoServico) {
    	return this.getPreManifestoDocumentoDAO().findPreManifestoDocumentoByIdDoctoServico(idDoctoServico);
    }     
    /**
     * Busca os Pre manifestos por documento e manifesto
     * @param idManifesto
     * @param idDoctoServico
     * @return
     */
    public PreManifestoDocumento findPreManifestoDocumentoByIdManifestoDocto(Long idManifesto,Long idDoctoServico) {
    	return getPreManifestoDocumentoDAO().findPreManifestoDocumentoByIdManifestoDocto(idManifesto,idDoctoServico);
    }
    /**
     * Método que retorna uma list de objetos de PreManifestoDocumento pelo ID do Manifesto
     * @param idManifesto
     * @return
     */
    public List findPreManifestoDocumentoByIdManifesto(Long idManifesto) {
    	return this.getPreManifestoDocumentoDAO().findPreManifestoDocumentoByIdManifesto(idManifesto);
    }
    
    /**
     * Busca quantidade de documentos
     * 
     * @author André Valadas
     * @param idManifesto
     * @param tpDocumentoServico
     * @return
     */
    public Integer getRowCountPreManifestoDocumento(final Long idManifesto, final String tpDocumentoServico) {
    	return getPreManifestoDocumentoDAO().getRowCountPreManifestoDocumento(idManifesto, tpDocumentoServico);
    }

    /**
     * Busca quantidade de documentos
     * 
     * @author André Valadas
     * @param idManifesto
     * @param tpDocumentoServico
     * @return
     */
    public Integer getRowCountPreManifestoDocumento(final Long idManifesto) {
    	return this.getRowCountPreManifestoDocumento(idManifesto, null);
    }
    
    /**
     * Retorna Documentos relacionados ao Pre-Manifesto.
	 * @author Andre Valadas
     * 
     * @param idManifesto
     * @param tpDocumentoServico
     * @return
     */
	public List findPreManifestoDocumentos(Long idManifesto, String... tpDocumentoServico) {
		return this.getPreManifestoDocumentoDAO().findPreManifestoDocumentoByIdManifesto(idManifesto, tpDocumentoServico);
	}
    /**
     * Retorna o pre manifesto que atendam os parametros passados
     * @param idManifesto
     * @param nrOrdem
     * @return
     */
    public PreManifestoDocumento findPreManifestoDocumentoByIdManifesto(Long idManifesto,Integer nrOrdem) {
    	return getPreManifestoDocumentoDAO().findPreManifestoDocumentoByIdManifesto(idManifesto,nrOrdem);
    }
    
    /**
     * Busca a quantidade de todos os manifestosDoctoServico relacionados com um manifesto em questao.
     * 
     * @param idManifesto
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountPreManifestoDoctoServicoByIdManifesto(Long idManifesto, Long idDoctoServico) {
    	return this.getPreManifestoDocumentoDAO().getRowCountPreManifestoDoctoServicoByIdManifesto(idManifesto, idDoctoServico, null, null);
	}
    
    public Integer getRowCountPreManifestosDoctoServicoByidManifestoSQL( 
    		Long idManifesto, 
    		Long idDoctoServico,
    		String tpDoctoServico ){
    	
    	return this.getPreManifestoDocumentoDAO().getRowCountPreManifestosDoctoServicoByidManifestoSQL(idManifesto, idDoctoServico,
    			tpDoctoServico);
    	
    }
    
    /**
     * Busca a quantidade de todos os manifestosDoctoServico relacionados com um manifesto em questao.
     * 
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDoctoServico
     * @param idFilialDoctoServico
     * @return
     */
    public Integer getRowCountPreManifestoDoctoServicoByIdManifesto(Long idManifesto, Long idDoctoServico, String tpDoctoServico, Long idFilialDoctoServico) {
    	return this.getPreManifestoDocumentoDAO().getRowCountPreManifestoDoctoServicoByIdManifesto(idManifesto, idDoctoServico, tpDoctoServico, idFilialDoctoServico);
	}
    
    /**
     * Busca todos os manifestosDoctoServico relacionados com um manifesto em questao.
     * 
     * @param idManifesto
     * @param idDoctoServico
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedPreManifestosDoctoServicoByidManifesto(Long idManifesto, Long idDoctoServico, FindDefinition findDefinition) {
    	return this.getPreManifestoDocumentoDAO().findPaginatedPreManifestosDoctoServicoByidManifesto(idManifesto, idDoctoServico, null, null, findDefinition);
    }   
    
    /**
     * Busca todos os manifestosDoctoServico relacionados com um manifesto em questao.
     * 
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDoctoServico
     * @param idFilialDoctoServico
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedPreManifestosDoctoServicoByidManifesto(Long idManifesto, Long idDoctoServico, String tpDoctoServico, Long idFilialDoctoServico, FindDefinition findDefinition) {
    	return this.getPreManifestoDocumentoDAO().findPaginatedPreManifestosDoctoServicoByidManifesto(idManifesto, idDoctoServico, tpDoctoServico, idFilialDoctoServico, findDefinition);
    }  
    
    
    /**
     * Busca todos os manifestosDoctoServico relacionados com um manifesto em questao, com o dvDoctoServico formatado,
     * caso do documento servico seja um CTR.
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDoctoServico
     * @param idFilialDoctoServico
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedPreManifestosDoctoServicoByidManifestoSQL( 
    		Long idManifesto, 
    		Long idDoctoServico,
    		String tpDoctoServico, 
    		FindDefinition findDefinition){
    	
    	return this.getPreManifestoDocumentoDAO().findPaginatedPreManifestosDoctoServicoByidManifestoSQL(idManifesto, idDoctoServico,
    			tpDoctoServico, findDefinition);
    	
    }
    
    
    /**
     * Atualiza o valor do manifesto dos <code>preManifestoDocumentos</code> informados.<br>
     * 
     * @param List de ids dos preManifestoDocumentos.
     */
    public void updateValorTotalManifesto(Long idManifesto, List preManifestoDocumentos) {
    	
    	BigDecimal vlPreManifestoDocumento = new BigDecimal(0);
    		
    	//Busca todos os valores dos preManifestoDocumentos em questao.
    	for (Iterator iter = preManifestoDocumentos.iterator(); iter.hasNext();) {
    		PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
			if(preManifestoDocumento.getDoctoServico().getVlMercadoria()!=null){
				vlPreManifestoDocumento = vlPreManifestoDocumento.add(preManifestoDocumento.getDoctoServico().getVlMercadoria());
			}
		}
    	
    	Manifesto manifesto = this.getManifestoService().findByIdInitLazyProperties(idManifesto, false);
    	manifesto.setVlTotalManifesto(vlPreManifestoDocumento);
    	this.getManifestoService().storeBasic(manifesto);
    }
    
    /**
     * Gera eventos para os Documentos de Servico que estiverem relacionados com o Pré-Manifesto
     * 
     * @param idManifesto
     * @param idDoctoServico
     */
    public void generateEventoDoctoServicoByIdManifestoByIdDoctoServico(Long idManifesto, Long idDoctoServico) {    	
    	Manifesto manifesto = manifestoService.findById(idManifesto);
		ControleCarga controleCarga = manifesto.getControleCarga();
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
				
		//Gera um evento doctoServico para o doctoServico em questao...
		List<Short> cdEventos = new ArrayList();	
		if (doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("29"))) {
			cdEventos.add(Short.valueOf("123"));
		} else if (doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("30"))) {
			cdEventos.add(Short.valueOf("124"));
		} else if (doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("31"))) {
			cdEventos.add(Short.valueOf("121"));
			cdEventos.add(Short.valueOf("124"));
		} else if (doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("32"))) {
			cdEventos.add(Short.valueOf("122"));
			cdEventos.add(Short.valueOf("123"));
		}
		
		//[CQ25050] Gera eventos de volume
		List<PreManifestoVolume> listaPreManifestoVolume = preManifestoVolumeService.findByManifestoDoctoServico(idManifesto, idDoctoServico);
		for (PreManifestoVolume preManifestoVolume : listaPreManifestoVolume) {
			VolumeNotaFiscal volumeNotaFiscal = preManifestoVolume.getVolumeNotaFiscal();
			if (volumeNotaFiscal.getLocalizacaoMercadoria() != null && volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().longValue() == 1) {
				continue;
			}
			List<Short> cdEventosVolumes = new ArrayList();
			if (volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("29"))) {
				cdEventosVolumes.add(Short.valueOf("123"));
			} else if (volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("30"))) {
				cdEventosVolumes.add(Short.valueOf("124"));
			} else if (volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("31"))) {
				cdEventosVolumes.add(Short.valueOf("121"));
				cdEventosVolumes.add(Short.valueOf("124"));
			} else if (volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("32"))) {
				cdEventosVolumes.add(Short.valueOf("122"));
				cdEventosVolumes.add(Short.valueOf("123"));
			}
			for (Short cdEvento : cdEventosVolumes) {
				eventoVolumeService.generateEventoVolume(volumeNotaFiscal, cdEvento, "LM");
			}
		}
		
		String tpDocumento = null;
		if(controleCarga != null && controleCarga.getTpControleCarga().getValue().equals("V")) {
			Short[] codigosEvento = new Short[]{Short.valueOf("125")};
			List listEventoDocumentoServico = eventoDocumentoServicoService.findEventoDoctoServico(doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial(), codigosEvento, false, null, null);	    			
			if (!listEventoDocumentoServico.isEmpty()) {
				cdEventos.add(Short.valueOf("129"));
			} else {
				cdEventos.add(Short.valueOf("63"));
			}
			tpDocumento = "PMV"; //Pre-Manifesto Viagem
		} else {
			Short[] codigosEvento = new Short[]{Short.valueOf("125")};
			List listEventoDocumentoServico = eventoDocumentoServicoService.findEventoDoctoServico(doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial(), codigosEvento, false, null, null);	    			
			if (!listEventoDocumentoServico.isEmpty()) {
				cdEventos.add(Short.valueOf("128"));
			} else {
				cdEventos.add(Short.valueOf("86"));
			}
			tpDocumento = "PME"; //Pre-Manifesto Entrega
		}				
		
        String strPreManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(manifesto.getNrPreManifesto().toString(), 8, '0');
        
        for (Iterator iter = cdEventos.iterator(); iter.hasNext();) {
			Short cdEvento = (Short) iter.next();
			
	        incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
	        		cdEvento, 
	        		doctoServico.getIdDoctoServico(),
	        		SessionUtils.getFilialSessao().getIdFilial(),
	        		strPreManifesto, 
	        		JTDateTimeUtils.getDataHoraAtual(),
	        		null,
	        		SessionUtils.getFilialSessao().getSiglaNomeFilial(),
	        		tpDocumento
	        		);							        	
		}	
    }    
    
    /**
	 * LMS-1261 - Devolve DoctoServico para o TERMINAL
	 * 
	 * @author André Valadas
	 * @param idDoctoServico
	 * @param localizacaoTerminal
	 */
	public void executeReleaseDoctoServico(final Long idDoctoServico, final LocalizacaoMercadoria localizacaoTerminal) {
		final DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		doctoServico.setLocalizacaoMercadoria(localizacaoTerminal);
		doctoServicoService.store(doctoServico);
	}

	/**
     * Verifica a possibilidade de remover os doctoServico. Caso o numero de doctoServicos
     * seja igual  ao numero de doctoServico anexados ao preManifesto ele anula a delecao.
     * 
     * @param idsPreManifestoDocumentos
     */
    public boolean validateRemoveByIds(List idsPreManifestoDocumentos){
    	if (idsPreManifestoDocumentos.size()>0) {
    		Long idPreManifestoDocumento = (Long) idsPreManifestoDocumentos.get(0);
    		PreManifestoDocumento preManifestoDocumento = this.getPreManifestoDocumentoDAO().findPreManifestoDocumento(idPreManifestoDocumento);

    		Manifesto manifesto = preManifestoDocumento.getManifesto();
    		if (manifesto.getPreManifestoDocumentos().size() == idsPreManifestoDocumentos.size()) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * verifica se existe algum Controle carga para o documento de servico 
     * @param idDoctoServico
     * @return
     */

    public boolean findCCByIdDoctoServico(Long idDoctoServico) {
    	return getPreManifestoDocumentoDAO().findCCByIdDoctoServico(idDoctoServico);
    }


    /**
     * 
     * @param idManifesto
     * @return
     */
    public Integer findMaxNrOrdemByIdManifesto(Long idManifesto){
    	return getPreManifestoDocumentoDAO().findMaxNrOrdemByIdManifesto(idManifesto);
    }
    
    
    /**
     * Remove as instâncias do pojo de acordo com os parâmetros recebidos.
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	getPreManifestoDocumentoDAO().removeByIdControleCarga(idControleCarga);
    }
    
    
    public void storePreManifestoDocumentoToCarregamento(Long idManifesto, List listIdsDocumentos) {
		Manifesto manifesto = this.getManifestoService().findByIdInitLazyProperties(idManifesto, false);
		controleCargaService.validateControleCargaByInclusaoDocumentos(manifesto.getControleCarga().getIdControleCarga());

		for (Iterator iter = listIdsDocumentos.iterator(); iter.hasNext();) {
			Long idDoctoServico = (Long)iter.next();
			DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
			
			PreManifestoDocumento preManifestoDocumento = new PreManifestoDocumento();
			preManifestoDocumento.setManifesto(manifesto);
			preManifestoDocumento.setDoctoServico(doctoServico);
			store(preManifestoDocumento);

			String strPreManifesto = preManifestoDocumento.getManifesto().getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(preManifestoDocumento.getManifesto().getNrPreManifesto().toString(), 8, '0');
			if(manifesto.getTpManifesto().getValue().equals("V")) {
				Short[] cdEvento = null;
				if (manifesto.getTpStatusManifesto().getValue().equals("PM")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO};
				} else if (manifesto.getTpStatusManifesto().getValue().equals("EC")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO, Short.valueOf("25")};
				} else if (manifesto.getTpStatusManifesto().getValue().equals("CC")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO, Short.valueOf("25"), Short.valueOf("26")};
				}
				for (int i = 0; i < cdEvento.length; i++) {
					incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
							cdEvento[i], doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial(), 
							strPreManifesto, JTDateTimeUtils.getDataHoraAtual(), null, SessionUtils.getFilialSessao().getSiglaNomeFilial(), 
							"PMV");
				}
			} else if(manifesto.getTpManifesto().getValue().equals("E")) {
				Short[] cdEvento = null;
				if (manifesto.getTpStatusManifesto().getValue().equals("PM")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO};
				} else if (manifesto.getTpStatusManifesto().getValue().equals("EC")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO, Short.valueOf("24")};
				} else if (manifesto.getTpStatusManifesto().getValue().equals("CC")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO, Short.valueOf("24"), Short.valueOf("27")};
				}
				for (int i = 0; i < cdEvento.length; i++) {
					incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
							cdEvento[i], doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial(), 
							strPreManifesto, JTDateTimeUtils.getDataHoraAtual(), null, SessionUtils.getFilialSessao().getSiglaNomeFilial(), 
							"PME");
				}
			}
		}
    }
    
    /**
     * Método verifica se CLIENTE.NR_REENTREGAS_COBRANCA for igual ao CONHECIMENTO.NR_REENTREGA, 
     * exibir a mensagem LMS-05318 e não permitir a inclusão do documento do manifesto
     * 
     * @param doctoServico
     */
    public void validateReentregaDocumento(Long idDoctoServico){
    	
    	Cliente devedor = devedorDocServService.findByIdDoctoServico(idDoctoServico);
    	devedor = clienteService.findById(devedor.getIdCliente());
    	
    	/*Não permitir a inclusão de documentos e mostra LMS-05318*/ 			 		
    	if(devedor != null){

    		/*Numero de reentrega de cobranca*/
    		Long nrReentregaCobranca = (devedor.getNrReentregasCobranca() == null) ? 1L : devedor.getNrReentregasCobranca();

    		/*Obtem o conhecimento*/
    		Conhecimento con = (Conhecimento) doctoServicoService.findById(idDoctoServico);
    		
    		Integer qtReentregasConhecimento = doctoServicoService.getCountDoctosServicoReentregaByIdDoctoServico(idDoctoServico);

    		nrReentregaCobranca = LongUtils.add(nrReentregaCobranca, qtReentregasConhecimento.longValue());
    		
    		if(con != null && con.getNrReentrega() != null && (nrReentregaCobranca < con.getNrReentrega() || nrReentregaCobranca.equals(con.getNrReentrega()))) {
    		    throw new BusinessException("LMS-05318");
    		}				
    	}    	
    }
    
	public DevedorDocServService getDevedorDocServService() {
		return devedorDocServService;
	}
	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}    
	
	 
    /**
     * Método que retorna uma list de objetos de PreManifestoDocumento pelo ID do Controle de Carga
     * @param idControleCarga
     * @return
     */
    public List<PreManifestoDocumento> findByControleCarga(Long idControleCarga) {
    	return getPreManifestoDocumentoDAO().findByControleCarga(idControleCarga);
    }
    
    /**
     * LMS-5673
     * Método que retorna uma lista de documentos "relacionados" que estão pendentes de carragamento
     * @param idControleCarga
     * @return
     */
    public List<Map> findDoctoServicoDivergenteEntrega(Long idControleCarga) {
		return getPreManifestoDocumentoDAO().findDoctoServicoDivergenteEntrega(idControleCarga);
	}
    
    /**
     * LMS-2505
     * Verifica se existe Documento de Servico
     * @author DiogoSB
     * @param idControleCarga , idConhecimento
     */
    public Integer validateDoctoServico(Long idManifesto , Long idConhecimento) {
    	return getPreManifestoDocumentoDAO().validateDoctoServico(idManifesto , idConhecimento);
    }
    
    /**
     * LMS-2337
     * 
     * valida a inclusao de documentos no manisfesto
     * 
     * */
    
    public Boolean validateDocumentoServicoManifesto(Long idManifesto , Long idDoctoServico) {
    	Manifesto manifesto = manifestoService.findById(idManifesto);
    	return validateDocumentoServicoManifesto(manifesto.getFilialByIdFilialDestino().getIdFilial(), manifesto.getTpManifesto().getValue(), idDoctoServico);
    }
    
    public Boolean validateDocumentoServicoManifesto(Long idFilialDestinoManifesto, String tpManifesto, Long idDoctoServico) {
    	Boolean result = Boolean.TRUE;
    	Filial filialSessao = SessionUtils.getFilialSessao();
    	Filial filialDestinoManifesto = filialService.findById(idFilialDestinoManifesto);
    	Filial filialDesvioRota;
    	
    	DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
    	
    	if(filialSessao.getBlRestrEntrOutrasFiliais()) {
    		
    		if ("E".equals(tpManifesto)) {
				
				Awb awb = awbService.findByIdDoctoServicoAndFilialOrigem(idDoctoServico, filialSessao.getIdFilial());
    			
    			if(awb != null){
    				result = Boolean.TRUE;
    			} else if (!doctoServico.getFilialByIdFilialDestino().equals(filialSessao)) {
    				
    				Boolean existeSolicitacaoRetirada = solicitacaoRetiradaService.findExistenciaSolicitacaoRetirada(doctoServico.getIdDoctoServico(), "A");
    				if (existeSolicitacaoRetirada) {
						result = Boolean.TRUE;
					} else if((filialDesvioRota = substAtendimentoFilialService.findMatchSubstAtendimentoFilialByIdFilialDestino(doctoServico.getFilialByIdFilialDestino().getIdFilial())) != null
							&& filialDesvioRota.getIdFilial().equals(filialSessao.getIdFilial())) {
						result = Boolean.TRUE;
					} else if(filialSessao.getFilialByIdFilialResponsavel() != null &&
							filialSessao.getFilialByIdFilialResponsavel().equals(doctoServico.getFilialByIdFilialDestino())) {
						result = Boolean.TRUE;
					} else {
						result = Boolean.FALSE;
					}
    				
				} else {
					result = Boolean.TRUE;
				}
    			
			} else if("V".equals(tpManifesto)) {

				YearMonthDay dataAtual = new YearMonthDay(new Date());
				Boolean vigenteLoja = false;
				
				buscaTipoFilial : for (HistoricoFilial historicoFilial : filialDestinoManifesto.getHistoricoFiliais()) {
					int resultDataInicial = historicoFilial.getDtRealOperacaoInicial() != null ? historicoFilial.getDtRealOperacaoInicial().compareTo(dataAtual) : 0;
					int resultDataFinal = historicoFilial.getDtRealOperacaoFinal() != null ? historicoFilial.getDtRealOperacaoFinal().compareTo(dataAtual) : 0;
					
					if(resultDataInicial < 1 && resultDataFinal > -1){
						if(historicoFilial.getTpFilial().getValue().equals("LO")){
							vigenteLoja = true;
						}
						break buscaTipoFilial;
					}
				}
				
				if( vigenteLoja && filialDestinoManifesto.getFilialByIdFilialResponsavel() != null ){
					
					if (!filialDestinoManifesto.equals(doctoServico.getFilialByIdFilialDestino())) {
						if(doctoServico.getFilialByIdFilialDestino().equals(
								filialDestinoManifesto.getFilialByIdFilialResponsavel())) {
							result = Boolean.TRUE;
						} else {
							result = Boolean.FALSE;
						}
					} else {
						result = Boolean.TRUE;
					}
					
				} else {
					result = Boolean.TRUE;
				}
				
			} else {
				result = Boolean.TRUE;
			}
    	}
    	
    	return result;
    }
    
    public BigDecimal findPsRealTotalByIdControleCarga(Long idControleCarga) {
    	return getPreManifestoDocumentoDAO().findPsRealTotalByIdControleCarga(idControleCarga);
    }
    
    public void removePreManifestoDocumento(PreManifestoDocumento preManifestoDocumento){
    	getPreManifestoDocumentoDAO().removePreManifestoDocumento(preManifestoDocumento);
    }
    public void storePreManifestoDocumento( PreManifestoDocumento preManifestoDocumento ){
    	getPreManifestoDocumentoDAO().storePreManifestoDocumento(preManifestoDocumento);
    }
	public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
		this.preManifestoVolumeService = preManifestoVolumeService;
	}
	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}
	public void setOrdemFilialFluxoService(OrdemFilialFluxoService ordemFilialFluxoService) {
		this.ordemFilialFluxoService = ordemFilialFluxoService;
	}
	public void setSolicitacaoRetiradaService(SolicitacaoRetiradaService solicitacaoRetiradaService) {
		this.solicitacaoRetiradaService = solicitacaoRetiradaService;
	}
	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	public void setSubstAtendimentoFilialService(SubstAtendimentoFilialService substAtendimentoFilialService) {
		this.substAtendimentoFilialService = substAtendimentoFilialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public boolean validateExisteDocumentosNaoEmitidos(Long idControleCarda, List<Long> idsManifesto){
		return getPreManifestoDocumentoDAO().validateExisteDocumentosNaoEmitidos(idControleCarda, idsManifesto);
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }
    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
}