package com.mercurio.lms.carregamento.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.DocumentoMir;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.entrega.model.service.DocumentoMirService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.SubstAtendimentoFilial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.OrdemFilialFluxoService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.municipios.model.service.SubstAtendimentoFilialService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.sim.model.DocumentoServicoRetirada;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.sim.model.service.DocumentoServicoRetiradaService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.sim.model.service.SolicitacaoRetiradaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.manterGerarPreManifestoAction"
 */

public class ManterGerarPreManifestoAction extends MasterDetailAction {
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private ClienteService clienteService;
	private FilialService filialService;
	private ControleCargaService controleCargaService;
	private DomainValueService domainValueService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private RotaIdaVoltaService rotaIdaVoltaService;	
	private AwbService awbService;	
	private DoctoServicoService doctoServicoService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ConversaoMoedaService conversaoMoedaService;
	private PedidoColetaService pedidoColetaService;
	private DetalheColetaService detalheColetaService;
	private SubstAtendimentoFilialService substAtendimentoFilialService;
	private ConfiguracoesFacade configuracoesFacade;
	private PessoaService pessoaService;
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	private AgendamentoEntregaService agendamentoEntregaService;
	private DocumentoMirService documentoMirService;
	private SolicitacaoRetiradaService solicitacaoRetiradaService;
	private ControleTrechoService controleTrechoService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private MdaService mdaService;
	private ReciboReembolsoService reciboReembolsoService;
	private OrdemFilialFluxoService ordemFilialFluxoService;
	private DocumentoServicoRetiradaService documentoServicoRetiradaService;
	private EventoControleCargaService eventoControleCargaService;
	private EmpresaService empresaService;
	private CarregamentoDescargaService carregamentoDescargaService; 
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private ProprietarioService proprietarioService;
	private MunicipioService municipioService;
	/**
	 * Declaração serviço principal da Action.
	 */
	public void setManifestoService(ManifestoService manifestoService) {
		this.setMasterService(manifestoService);
	}	
	public ManifestoService getManifestoService() {
		return (ManifestoService) super.getMasterService();
	}
	
	
	/** 
	 * ResultSetPage de Manifestos
	 * @param criteria
	 * @return
	 */
    public ResultSetPage findPaginatedManifesto(TypedFlatMap criteria) {
    	return this.getManifestoService().findPaginatedManifesto(criteria);
    }

    /**
     * Quantidade de manifestos do ResultSetPage
     * @param criteria
     * @return
     */
    public Integer getRowCountManifesto(TypedFlatMap criteria) {
    	return this.getManifestoService().getRowCountManifesto(criteria);
    }
	
    
	/**
	 * ######################### INICIO DOS MÉTODOS DIVERSOS PARA A TELA ############################
	 */

	/**
	 * Busca algums dos dados do usuario logado, que está na sessão.
	 */
	public TypedFlatMap getDadosSessao() {
		TypedFlatMap dadosUsuario = new TypedFlatMap();

		dadosUsuario.put("filialSessao.idFilial", SessionUtils.getFilialSessao().getIdFilial());
		dadosUsuario.put("filialSessao.sgFilial", SessionUtils.getFilialSessao().getSgFilial());
		dadosUsuario.put("filialSessao.pessoa.nmFantasia", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());

		dadosUsuario.put("postoAvancado.idPostoAvancado", null);
		dadosUsuario.put("postoAvancado.dsPostoAvancado", "");

		return dadosUsuario;
	}	

	public List findLookupBySgFilial(Map criteria) {
		return this.getFilialService().findLookupBySgFilial((String)criteria.get("sgFilial"), null);
	}	
	
	/**
	 * Busca um controle de carga com validações.
	 */
	public List findControleCargaByNrControleByFilial(TypedFlatMap criteria) {
		TypedFlatMap mapControleCarga = new TypedFlatMap();
		Long nrControleCarga = criteria.getLong("nrControleCarga");
		Long idFilial = criteria.getLong("filialByIdFilialOrigem.idFilial");
		
		// Verifica se existe um Manifesto para o Controle de Carga em questão que seja de ENTREGA.
		List listControleCarga = this.getControleCargaService().findControleCargaByNrControleByFilial(nrControleCarga, idFilial);		
		for (Iterator iter = listControleCarga.iterator(); iter.hasNext();) {
			ControleCarga controleCarga = (ControleCarga) iter.next();
			
			// Verifica se o Controle de Carga está Cancelado.
			if (controleCarga.getTpStatusControleCarga().getValue().equals("CA")) {
				throw new BusinessException("LMS-05123");
			}
			
			// Verifica se o Controle de Carga stá com o carregamento finalizado para a filial do usuario logado.
			List listEventoControleCarga = this.getEventoControleCargaService().
												findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(
																				SessionUtils.getFilialSessao().getIdFilial(), 
																				controleCarga.getIdControleCarga(), "FC");
			if (!listEventoControleCarga.isEmpty()) {
				throw new BusinessException("LMS-05124");
			}
						
			mapControleCarga.put("idControleCarga", controleCarga.getIdControleCarga());
			mapControleCarga.put("nrControleCarga", controleCarga.getNrControleCarga());
			mapControleCarga.put("filialByIdFilialOrigem.idFilial", controleCarga.getFilialByIdFilialOrigem().getIdFilial());
			mapControleCarga.put("filialByIdFilialOrigem.sgFilial", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
			mapControleCarga.put("filialByIdFilialOrigem.pessoa.nmFantasia", controleCarga.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
			mapControleCarga.put("tpControleCarga.value", controleCarga.getTpControleCarga().getValue());
			mapControleCarga.put("tpControleCarga.description", controleCarga.getTpControleCarga().getDescription());
			mapControleCarga.put("tpControleCarga.status", controleCarga.getTpControleCarga().getStatus());
			mapControleCarga.put("blEntregaDireta", controleCarga.getBlEntregaDireta());
			
			if (controleCarga.getTpControleCarga().getValue().equals("V")) {
				if (controleCarga.getTpRotaViagem() != null && ( 
						controleCarga.getTpRotaViagem().getValue().equals("EX") || 
						controleCarga.getTpRotaViagem().getValue().equals("EC") )) {
					if (controleCarga.getRotaIdaVolta() != null) {
						mapControleCarga.put("rotaIdaVolta.idRotaIdaVolta", controleCarga.getRotaIdaVolta().getIdRotaIdaVolta());
						mapControleCarga.put("rotaIdaVolta.nrRota", controleCarga.getRotaIdaVolta().getNrRota());
						mapControleCarga.put("rotaIdaVolta.rota.dsRota", controleCarga.getRotaIdaVolta().getRota().getDsRota());
					}				
				} else {
					if (controleCarga.getRota() != null) {
						mapControleCarga.put("rotaIdaVolta.idRotaIdaVolta", controleCarga.getRota().getIdRota());
						mapControleCarga.put("rotaIdaVolta.nrRota", "");
						mapControleCarga.put("rotaIdaVolta.rota.dsRota", controleCarga.getRota().getDsRota());
					}
				}				
			}
			
			if (controleCarga.getTpControleCarga().getValue().equals("C")) {
				if (controleCarga.getRotaColetaEntrega() != null) {
					mapControleCarga.put("rotaColetaEntrega.idRotaColetaEntrega", controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega());
					mapControleCarga.put("rotaColetaEntrega.nrRota", controleCarga.getRotaColetaEntrega().getNrRota());
					mapControleCarga.put("rotaColetaEntrega.dsRota", controleCarga.getRotaColetaEntrega().getDsRota());
				}
			}
			
			if (controleCarga.getTpControleCarga().getValue().equals("C")) {
				List listManifesto = this.getManifestoService().findManifestoByIdControleCarga(controleCarga.getIdControleCarga(), null, null, null);
				for (Iterator iterator = listManifesto.iterator(); iterator.hasNext();) {
					Manifesto manifesto = (Manifesto) iterator.next();
					if (manifesto.getTpManifesto().getValue().equals("E") 
							&& !manifesto.getTpStatusManifesto().getValue().equals("CA")
							&& !manifesto.getTpStatusManifesto().getValue().equals("FE")){
						mapControleCarga.put("possuiManifestoEntrega", this.getConfiguracoesFacade().getMensagem("LMS-05076"));
					}
				}
			}
			
			listControleCarga.clear();
			listControleCarga.add(mapControleCarga);
		}

		
		return listControleCarga;
	}
	
	/**
	 * Busca um controle de carga
	 */
	public List findControleCargaByNrControleByFilialPadrao(TypedFlatMap criteria) {
		TypedFlatMap mapControleCarga = new TypedFlatMap();
		Long nrControleCarga = criteria.getLong("nrControleCarga");
		Long idFilial = criteria.getLong("filialByIdFilialOrigem.idFilial");
		
		List listControleCarga = this.getControleCargaService().findControleCargaByNrControleByFilial(nrControleCarga, idFilial);		
		for (Iterator iter = listControleCarga.iterator(); iter.hasNext();) {
			ControleCarga controleCarga = (ControleCarga) iter.next();
			
			mapControleCarga.put("idControleCarga", controleCarga.getIdControleCarga());
			mapControleCarga.put("nrControleCarga", controleCarga.getNrControleCarga());
			mapControleCarga.put("filialByIdFilialOrigem.idFilial", controleCarga.getFilialByIdFilialOrigem().getIdFilial());
			mapControleCarga.put("filialByIdFilialOrigem.sgFilial", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
			mapControleCarga.put("filialByIdFilialOrigem.pessoa.nmFantasia", controleCarga.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
			mapControleCarga.put("tpControleCarga.value", controleCarga.getTpControleCarga().getValue());
			mapControleCarga.put("tpControleCarga.description", controleCarga.getTpControleCarga().getDescription());
			mapControleCarga.put("tpControleCarga.status", controleCarga.getTpControleCarga().getStatus());
			
			listControleCarga.clear();
			listControleCarga.add(mapControleCarga);
		}
		
		return listControleCarga;
	}	

	public List findLookupRotaIdaVolta(TypedFlatMap criteria) {
		return this.getRotaIdaVoltaService().findLookupRotaIdaVolta(criteria);		
	}
	
	public List findLookupRotaColetaEntrega(Map criteria) {
		return this.getRotaColetaEntregaService().findLookup(criteria);		
	}
	
	public List findLookupFilial(Map criteria) {
		return this.getFilialService().findLookup(criteria);
	}
	
	public List findLookupSolicitacaoRetirada(Map criteria) {		
		List listSolicitacaoRetirada = this.getSolicitacaoRetiradaService().findLookup(criteria);		
		TypedFlatMap mapSolicitacaoRetirada = new TypedFlatMap();
				
		for (Iterator iter = listSolicitacaoRetirada.iterator(); iter.hasNext();) {
			SolicitacaoRetirada solicitacaoRetirada = (SolicitacaoRetirada) iter.next();
			
			mapSolicitacaoRetirada.put("idSolicitacaoRetirada", solicitacaoRetirada.getIdSolicitacaoRetirada());
			mapSolicitacaoRetirada.put("filialRetirada.idFilial", solicitacaoRetirada.getFilialRetirada().getIdFilial());
			mapSolicitacaoRetirada.put("filialRetirada.sgFilial", solicitacaoRetirada.getFilialRetirada().getSgFilial());
			mapSolicitacaoRetirada.put("filialRetirada.pessoa.nmFantasia", solicitacaoRetirada.getFilialRetirada().getPessoa().getNmFantasia());
			mapSolicitacaoRetirada.put("nrSolicitacaoRetirada", solicitacaoRetirada.getNrSolicitacaoRetirada());
			mapSolicitacaoRetirada.put("tpSituacao.value", solicitacaoRetirada.getTpSituacao().getValue());
			mapSolicitacaoRetirada.put("tpSituacao.description", solicitacaoRetirada.getTpSituacao().getDescription());
			mapSolicitacaoRetirada.put("tpSituacao.status", solicitacaoRetirada.getTpSituacao().getStatus());
			
			listSolicitacaoRetirada.clear();
			listSolicitacaoRetirada.add(mapSolicitacaoRetirada);
		}
		
		return listSolicitacaoRetirada;
	}
	
	public List findLookupCliente(Map criteria) {		
		return this.getClienteService().findLookup(criteria);
	}	
	
	public List findComboEmBranco() {
		return new ArrayList();
	}
	
	public List findComboTipoPreManifesto(TypedFlatMap criteria) {
		List listTpPreManifesto = new ArrayList();
		String tpManifesto = criteria.getString("tpManifesto");
		if (tpManifesto.equals("V")) {
			listTpPreManifesto = this.getDomainValueService().findDomainValues("DM_TIPO_MANIFESTO_VIAGEM");
		} else if (tpManifesto.equals("E")) {
			listTpPreManifesto = this.getDomainValueService().findDomainValues("DM_TIPO_MANIFESTO_ENTREGA");
		}
		
		return listTpPreManifesto;
	}

	
    /**
     * #############################
     * # Documento Servico Methods #
     * #############################
     */
    
	/**
	 * Busca os tipos de documento serviço.
	 * @param criteria
	 * @return
	 */
    public List findTipoDocumentoServico(TypedFlatMap criteria) {
    	List dominiosValidos = new ArrayList();
    	dominiosValidos.add("CTR");
    	dominiosValidos.add("CRT");
    	dominiosValidos.add("NFT");
    	dominiosValidos.add("MDA");
    	dominiosValidos.add("RRE");
    	List retorno = this.getDomainValueService().findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
    	return retorno;
    }		
 	
    /**
     * Busca a filial baseado no documento de serviço
     * @param criteria
     * @return
     */
    public List findLookupFilialByDocumentoServico(Map criteria) {
    	FilterList filter = new FilterList(getFilialService().findLookup(criteria)) {
			public Map filterItem(Object item) {
				Filial filial = (Filial)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idFilial", filial.getIdFilial());
		    	typedFlatMap.put("sgFilial", filial.getSgFilial());
		    	typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }

    
    /**
     * FindLookup para filial do tipo de DoctoServico Escolhido.
     */ 
    public List findLookupServiceDocumentFilialCTR(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }

    public List findLookupServiceDocumentFilialCRT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }

    public List findLookupServiceDocumentFilialNFT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }    
    
    public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    
    public List findLookupServiceDocumentFilialRRE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }    
    
    /**
     * FindLookup para a tag DoctoServico.
     */  
    public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	return this.getConhecimentoService().findLookup(criteria);
    }

    public List findLookupServiceDocumentNumberCRT(Map criteria) {    	
    	return this.getCtoInternacionalService().findLookup(criteria);
    }
    
    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	return this.getConhecimentoService().findLookup(criteria);
    }

    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	return this.getMdaService().findLookup(criteria);
    }    
    
    public List findLookupServiceDocumentNumberRRE(Map criteria) {
    	return this.getReciboReembolsoService().findLookup(criteria);
    }     
    
    

    /**
	 * Função que chama o método que valida se o destino está dentro da 
	 * Rota de Ida e Volta do Controle de Carga.
     * @param parameter
     */
    public TypedFlatMap validaDestinoParaRotaIdaVolta(TypedFlatMap criteria) {
    	TypedFlatMap map = new TypedFlatMap();
    	String tpPreManifesto = criteria.getString("tpPreManifesto");
    	String tpControleCarga = criteria.getString("controleCarga.tpControleCarga");
    	Long idFilialDestino = criteria.getLong("filialByIdFilialDestino.idFilial");
    	Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
    	Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		if (tpControleCarga.equals("V")) { 
    		List listManifesto = this.getManifestoService().findManifestoByIdControleCarga(idControleCarga, null, null, null);
    		for (Iterator iter = listManifesto.iterator(); iter.hasNext();) {
				Manifesto manifesto = (Manifesto) iter.next();
				if ( manifesto.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialSessao) && 
						manifesto.getFilialByIdFilialDestino().getIdFilial().equals(idFilialDestino) &&
						manifesto.getTpStatusManifesto().getValue().equals("PM")) {
					if (manifesto.getTpManifestoViagem().getValue().equals("RV")) {
						if (tpPreManifesto.equals("RV")) {
							throw new BusinessException("LMS-05158");
						} else {
							throw new BusinessException("LMS-05159");
						}						
					} else if (tpPreManifesto.equals("RV")) {
						throw new BusinessException("LMS-05160");
					} else {
						map.put("confirmar", this.getConfiguracoesFacade().getMensagem("LMS-05035"));
					}
				}
			}			
			
			ControleTrecho controleTrecho = this.getControleTrechoService().findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(idControleCarga, idFilialSessao, idFilialDestino);
			if (controleTrecho == null) {
				throw new BusinessException("LMS-05071");				
			}
			map.put("controleTrecho.idControleTrecho", controleTrecho.getIdControleTrecho());
		}
		
		return map;
    }    
    
    /**
     * Método que valida se a filial da solicitação de retirada pertence a filial do usuario logado.
     * Caso verdadeiro, busca os documentos referentes a solicitação de retirada informada.
     * @param criteria
     */
    public void validaSolicitacaoRetiradaAndGetDocumentos(TypedFlatMap criteria) {
    	List listManifesto = this.getManifestoService().
    						findManifestoByIdSolicitacaoRetirada(criteria.getLong("solicitacaoRetirada.idSolicitacaoRetirada"));
    	if (!listManifesto.isEmpty()) {
			throw new BusinessException("LMS-05151");
		}
    	
    	if (!criteria.getString("solicitacaoRetirada.tpSituacao").equals("A")) {
			throw new BusinessException("LMS-05135");
		}
    	
    	Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
    	if (!idFilialSessao.equals(criteria.getLong("solicitacaoRetirada.filialRetirada.idFilial"))) {
			throw new BusinessException("LMS-05114");
		}
    	
    	List listDocumentosServicoRetirada = this.getSolicitacaoRetiradaService().
    							findDocsBySolicitacaoRetirada(criteria.getLong("solicitacaoRetirada.idSolicitacaoRetirada")); 

    	StringBuffer localizacoes = new StringBuffer();
    	localizacoes.append("24");
    	localizacoes.append("34");
    	localizacoes.append("35");
    	localizacoes.append("43");
    	localizacoes.append("28");
		
    	for (Iterator iter = listDocumentosServicoRetirada.iterator(); iter.hasNext();) {
			DocumentoServicoRetirada documentoServicoRetirada = (DocumentoServicoRetirada) iter.next();
			DoctoServico doctoServico = documentoServicoRetirada.getDoctoServico();
			
			if ( !doctoServico.getFilialLocalizacao().getIdFilial().equals(idFilialSessao) && 
				 !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().toString().contentEquals(localizacoes) ) {
				throw new BusinessException("LMS-05115");
			}
			
			TypedFlatMap mapItem = new TypedFlatMap();
			mapItem.put("idDoctoServico", doctoServico.getIdDoctoServico().toString());
			mapItem.put("masterId", criteria.getString("idManifesto") );
			
			savePreManifestoDocumento(mapItem);			
		}
    }
    
        
	/**
	 * ########################### FIM DOS MÉTODOS DIVERSOS PARA A TELA	############################
	 */
    

    
	/**
	 * ############################## INICIO DOS MÉTODOS PARA TELA DE DF2 ###########################
	 */
    
    public TypedFlatMap store(TypedFlatMap bean) {
    	
    	// Validações para Pré-Manifesto
    	ControleCarga controleCarga = null;
    	if (bean.getLong("controleCarga.idControleCarga") != null) {
			controleCarga = this.getControleCargaService().findById(bean.getLong("controleCarga.idControleCarga"));
			if (controleCarga.getTpControleCarga().getValue().equals("V")) {
	    		if (bean.getString("tpPreManifesto").equals("RV")) {
	    			
	    			// LMS-1991
					Filial filial = new Filial();
					if(bean.get("idFilialOrigem") != null) {
						filial = filialService.findById((Long)bean.get("idFilialOrigem"));
					} else {
						filial = SessionUtils.getFilialSessao();
					}
					
	    			if(filial.getBlGeraContratacaoRetornoVazio() == Boolean.FALSE){
	    			
	    			if (controleCarga.getMeioTransporteByIdTransportado() != null) {
		    			if (controleCarga.getMeioTransporteByIdTransportado().getTpVinculo().getValue().equals("E")) {
		    				throw new BusinessException("LMS-05036");
		    			}	    				
					} else {
						throw new BusinessException("LMS-05111");
					}
	    			
	    			if (controleCarga.getTpRotaViagem() != null && 
	    					!controleCarga.getTpRotaViagem().getValue().equals("EX")) {
	    				throw new BusinessException("LMS-05037");
	    			}

	    		}
			}
    	}
    	}
    			
    	MasterEntry entry = getMasterFromSession(bean.getLong("idManifesto"), true);
    	Manifesto manifesto = (Manifesto) entry.getMaster();
    	
    	manifesto.setIdManifesto(bean.getLong("idManifesto"));
    	manifesto.setNrPreManifesto(bean.getLong("nrPreManifesto"));
    	manifesto.setDhGeracaoPreManifesto(JTDateTimeUtils.getDataHoraAtual());
    	manifesto.setTpManifesto(bean.getDomainValue("tpManifesto"));
    	if (bean.getString("tpManifesto").equals("V")) {
    		manifesto.setTpManifestoViagem(bean.getDomainValue("tpPreManifesto"));
    	} else if (bean.getString("tpManifesto").equals("E")) {
    		manifesto.setTpManifestoEntrega(bean.getDomainValue("tpPreManifesto"));
    	}
    	manifesto.setTpModal(bean.getDomainValue("tpModal"));
    	manifesto.setTpAbrangencia(bean.getDomainValue("tpAbrangencia"));
    	manifesto.setTpStatusManifesto(new DomainValue("PM"));
    	manifesto.setObManifesto(bean.getString("obManifesto"));
    	manifesto.setBlBloqueado(Boolean.FALSE);
    	
    	if(bean.getLong("filialByIdFilialOrigem.idFilial") != null) {
    		manifesto.setFilialByIdFilialOrigem(this.getFilialService().findById(bean.getLong("filialByIdFilialOrigem.idFilial")));
    	} else {
    		manifesto.setFilialByIdFilialOrigem(SessionUtils.getFilialSessao());
    	}
    	manifesto.setFilialByIdFilialDestino(this.getFilialService().findById(bean.getLong("filialByIdFilialDestino.idFilial")));
    	manifesto.setControleCarga(controleCarga);

    	if (bean.getLong("manifesto.controleTrecho.idControleTrecho") != null) {
    		manifesto.setControleTrecho(this.getControleTrechoService().
    				findById(bean.getLong("manifesto.controleTrecho.idControleTrecho")));
    	}
    	
    	manifesto.setMoeda(SessionUtils.getMoedaSessao());
    	
    	if (bean.getLong("solicitacaoRetirada.idSolicitacaoRetirada") != null) {
        	manifesto.setSolicitacaoRetirada(this.getSolicitacaoRetiradaService().findById(
																bean.getLong("solicitacaoRetirada.idSolicitacaoRetirada")));
		}
    	if (bean.getLong("cliente.idCliente") != null) {
    		manifesto.setCliente(this.getClienteService().findById(bean.getLong("cliente.idCliente")));
		}
    		
    	ItemList items = getItemsFromSession(entry, "preManifestoDocumento");
    	ItemListConfig itemsConfig = getMasterConfig().getItemListConfig("preManifestoDocumento");    	
    	
    	// Faz a conversão de valores do DoctoServico para o valor da moeda do usuário logado.
    	BigDecimal vlTotalManifesto = BigDecimalUtils.ZERO;
    	BigDecimal psTotalManifesto = BigDecimalUtils.ZERO;
    	BigDecimal psTotalAforadoManifesto = BigDecimalUtils.ZERO;
    	for(Iterator iter = items.iterator(manifesto.getIdManifesto(), itemsConfig); iter.hasNext();) {
    		PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
    		DoctoServico doctoServico = this.getDoctoServicoService().findDoctoServicoById(preManifestoDocumento.getDoctoServico().getIdDoctoServico());

    		if (doctoServico.getVlMercadoria() != null) {
    			BigDecimal vlTotalManifestoConvertido = conversaoMoedaService.findConversaoMoeda(
   					 doctoServico.getPaisOrigem().getIdPais(),
   					 doctoServico.getMoeda().getIdMoeda(),
   					 SessionUtils.getPaisSessao().getIdPais(),
   					 SessionUtils.getMoedaSessao().getIdMoeda(),
   					 JTDateTimeUtils.getDataAtual(),
   					 doctoServico.getVlMercadoria());
   			
    			vlTotalManifesto = vlTotalManifesto.add(vlTotalManifestoConvertido);				
			}
    		
    		if (doctoServico.getPsReal() != null) {
    			psTotalManifesto = psTotalManifesto.add(doctoServico.getPsReal());
			}
    		
    		if (doctoServico.getPsAforado() != null) {
    			psTotalAforadoManifesto = psTotalAforadoManifesto.add(doctoServico.getPsAforado());
			}    		
    	}
    	
		manifesto.setVlTotalManifesto(vlTotalManifesto);
		manifesto.setPsTotalManifesto(psTotalManifesto);
		manifesto.setPsTotalAforadoManifesto(psTotalAforadoManifesto);
    	
    	TypedFlatMap mapBeanStored = this.getManifestoService().storeAll(manifesto, items, itemsConfig);		    	
    	items.resetItemsState(); 
    	updateMasterInSession(entry);
    	
    	return mapBeanStored;
    }    
    
    
	/**
	 * Salva a referencia do objeto Master na sessão.
	 * não devem ser inicializadas as coleções que representam os filhos
	 * já que o usuário pode vir a não utilizar a aba de filhos, evitando assim
	 * a carga desnecessária de objetos na sessão e a partir do banco de dados.
	 * 
	 * @param id
	 */
    public Object findById(java.lang.Long id) {
		Object masterObj = this.getManifestoService().findManifestoById(id);
		putMasterInSession(masterObj); 		
		Manifesto manifesto = (Manifesto) masterObj;
		
		TypedFlatMap mapManifesto = new TypedFlatMap();
		
		mapManifesto.put("idManifesto", manifesto.getIdManifesto());
		mapManifesto.put("filialByIdFilialOrigem.idFilial", manifesto.getFilialByIdFilialOrigem().getIdFilial());
		mapManifesto.put("filialByIdFilialOrigem.sgFilial", manifesto.getFilialByIdFilialOrigem().getSgFilial());		
		mapManifesto.put("nrPreManifesto", manifesto.getNrPreManifesto());
		mapManifesto.put("dhGeracaoPreManifesto", manifesto.getDhGeracaoPreManifesto());
		mapManifesto.put("tpManifesto.description", manifesto.getTpManifesto().getDescription());
		mapManifesto.put("tpManifesto.value", manifesto.getTpManifesto().getValue());
		mapManifesto.put("tpManifesto.status", manifesto.getTpManifesto().getStatus());		
		mapManifesto.put("sgFilialNrPreManifesto", FormatUtils.formatSgFilialWithLong(
										manifesto.getFilialByIdFilialOrigem().getSgFilial(), manifesto.getNrPreManifesto())); 
				
		if (manifesto.getTpManifestoViagem() != null) {
			mapManifesto.put("tpPreManifesto.description", manifesto.getTpManifestoViagem().getDescription());
			mapManifesto.put("tpPreManifesto.value", manifesto.getTpManifestoViagem().getValue());
			mapManifesto.put("tpPreManifesto.status", manifesto.getTpManifestoViagem().getStatus());
						
			if (manifesto.getControleCarga() != null) {
				if (manifesto.getControleCarga().getTpRotaViagem() != null && 
						( manifesto.getControleCarga().getTpRotaViagem().getValue().equals("EX") || 
						  manifesto.getControleCarga().getTpRotaViagem().getValue().equals("EC") )) {
					if (manifesto.getControleCarga().getRotaIdaVolta() != null){
						mapManifesto.put("controleCarga.rotaIdaVolta.idRotaIdaVolta", manifesto.getControleCarga().getRotaIdaVolta().getIdRotaIdaVolta());
						RotaIdaVolta rotaIdaVolta = this.getRotaIdaVoltaService().findById(manifesto.getControleCarga().getRotaIdaVolta().getIdRotaIdaVolta());
						mapManifesto.put("controleCarga.rotaIdaVolta.nrRota", rotaIdaVolta.getNrRota());			
						mapManifesto.put("controleCarga.rotaIdaVolta.rota.dsRota", rotaIdaVolta.getRota().getDsRota());
					}					
				} else {
					if (manifesto.getControleCarga().getRota() != null) {
						mapManifesto.put("controleCarga.rotaIdaVolta.idRotaIdaVolta", manifesto.getControleCarga().getRota().getIdRota());
						mapManifesto.put("controleCarga.rotaIdaVolta.nrRota", "");
						mapManifesto.put("controleCarga.rotaIdaVolta.rota.dsRota", manifesto.getControleCarga().getRota().getDsRota());
					}
				}
			}
			
		} else if (manifesto.getTpManifestoEntrega() != null) {
			mapManifesto.put("tpPreManifesto.description", manifesto.getTpManifestoEntrega().getDescription());
			mapManifesto.put("tpPreManifesto.value", manifesto.getTpManifestoEntrega().getValue());
			mapManifesto.put("tpPreManifesto.status", manifesto.getTpManifestoEntrega().getStatus());		
			if (manifesto.getControleCarga() !=  null && manifesto.getControleCarga().getRotaColetaEntrega() != null){
				mapManifesto.put("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", manifesto.getControleCarga().getRotaColetaEntrega().getIdRotaColetaEntrega());
				RotaColetaEntrega rotaColetaEntrega = this.getRotaColetaEntregaService().findById(manifesto.getControleCarga().getRotaColetaEntrega().getIdRotaColetaEntrega());
				mapManifesto.put("controleCarga.rotaColetaEntrega.nrRota", rotaColetaEntrega.getNrRota());
				mapManifesto.put("controleCarga.rotaColetaEntrega.dsRota", rotaColetaEntrega.getDsRota());
			}
		}

		if (manifesto.getTpModal() != null) {
			mapManifesto.put("tpModal.description", manifesto.getTpModal().getDescription());
			mapManifesto.put("tpModal.value", manifesto.getTpModal().getValue());
			mapManifesto.put("tpModal.status", manifesto.getTpModal().getStatus());			
		}

		if (manifesto.getTpAbrangencia() != null) {
			mapManifesto.put("tpAbrangencia.description", manifesto.getTpAbrangencia().getDescription());
			mapManifesto.put("tpAbrangencia.value", manifesto.getTpAbrangencia().getValue());
			mapManifesto.put("tpAbrangencia.status", manifesto.getTpAbrangencia().getStatus());			
		}

		if (manifesto.getControleCarga() != null) {
			// Verifica se o Controle de Carga está Cancelado.
			if (manifesto.getControleCarga().getTpStatusControleCarga().getValue().equals("CA")) {
				mapManifesto.put("desabilitaControleCarga", Boolean.TRUE);
			}

			// Verifica se o Controle de Carga stá com o carregamento finalizado para a filial do usuario logado.
			List listEventoControleCarga = this.getEventoControleCargaService().
												findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(
																	SessionUtils.getFilialSessao().getIdFilial(), 
																	manifesto.getControleCarga().getIdControleCarga(), "FC");
			if (!listEventoControleCarga.isEmpty()) {
				mapManifesto.put("desabilitaControleCarga", Boolean.TRUE);
			}			

			mapManifesto.put("controleCarga.tpControleCarga", manifesto.getControleCarga().getTpControleCarga().getValue());
			mapManifesto.put("controleCarga.filialByIdFilialOrigem.idFilial", manifesto.getControleCarga().getFilialByIdFilialOrigem().getIdFilial());
			mapManifesto.put("controleCarga.filialByIdFilialOrigem.sgFilial", manifesto.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
			mapManifesto.put("controleCarga.idControleCarga", manifesto.getControleCarga().getIdControleCarga());
			mapManifesto.put("controleCarga.nrControleCarga", manifesto.getControleCarga().getNrControleCarga());
		}
		
		mapManifesto.put("tpStatusManifesto.description", manifesto.getTpStatusManifesto().getDescription());
		mapManifesto.put("tpStatusManifesto.value", manifesto.getTpStatusManifesto().getValue());
		mapManifesto.put("tpStatusManifesto.status", manifesto.getTpStatusManifesto().getStatus());

		mapManifesto.put("filialByIdFilialDestino.idFilial", manifesto.getFilialByIdFilialDestino().getIdFilial());
		mapManifesto.put("filialByIdFilialDestino.sgFilial", manifesto.getFilialByIdFilialDestino().getSgFilial());
		Pessoa pessoaFilial = this.getPessoaService().findById(manifesto.getFilialByIdFilialDestino().getIdFilial());
		mapManifesto.put("filialByIdFilialDestino.pessoa.nmFantasia", pessoaFilial.getNmFantasia());
		mapManifesto.put("obManifesto", manifesto.getObManifesto());

		if(manifesto.getSolicitacaoRetirada() != null) {
			mapManifesto.put("solicitacaoRetirada.idSolicitacaoRetirada", manifesto.getSolicitacaoRetirada().getIdSolicitacaoRetirada());
			mapManifesto.put("solicitacaoRetirada.filialRetirada.idFilial", manifesto.getSolicitacaoRetirada().getFilial().getIdFilial());
			mapManifesto.put("solicitacaoRetirada.filialRetirada.sgFilial", manifesto.getSolicitacaoRetirada().getFilial().getSgFilial());
			mapManifesto.put("solicitacaoRetirada.filialRetirada.pessoa.nmFantasia", manifesto.getSolicitacaoRetirada().getFilial().getPessoa().getNmFantasia());			
			mapManifesto.put("solicitacaoRetirada.nrSolicitacaoRetirada", manifesto.getSolicitacaoRetirada().getNrSolicitacaoRetirada());
		}

		if (manifesto.getCliente() != null) {
			mapManifesto.put("cliente.idCliente", manifesto.getCliente().getIdCliente());
			mapManifesto.put("cliente.pessoa.idPessoa", manifesto.getCliente().getPessoa().getIdPessoa());
			mapManifesto.put("cliente.pessoa.tpIdentificacao", manifesto.getCliente().getPessoa().getTpIdentificacao().getValue());
			mapManifesto.put("cliente.pessoa.nrIdentificacao", manifesto.getCliente().getPessoa().getNrIdentificacao());			
			mapManifesto.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(manifesto.getCliente().getPessoa().getTpIdentificacao(), 
																										manifesto.getCliente().getPessoa().getNrIdentificacao()));
			mapManifesto.put("cliente.pessoa.nmPessoa", manifesto.getCliente().getPessoa().getNmPessoa());				
		}
		return mapManifesto;
    }
    
    /**
     * Remoção de um conjunto de registros Master.
     * 
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	this.getManifestoService().removeByIds(ids);
    }

    /**
     * Remoção de um registro Master.
     */    
    public void removeById(java.lang.Long id) {
    	this.getManifestoService().removeById(id);
		newMaster();
    }     
    
    /**
     * Salva um item Descrição Padrão na sessão.
     */
    public Serializable savePreManifestoDocumento(TypedFlatMap parameters) {
		List listIdsDocumentos = new ArrayList();
		
		if (parameters.getList("ids") != null) {
			listIdsDocumentos = parameters.getList("ids");
		} else if (parameters.getString("idDoctoServico") != null) {
			Long masterId = getMasterId(parameters);
			List idsDoctoServico = getIdsDoctoServicoFromSession(masterId);
			for (Iterator iter = idsDoctoServico.iterator(); iter.hasNext();) {
				Long idDoctoServico = (Long) iter.next();
				if (idDoctoServico.equals(parameters.getLong("idDoctoServico"))) {
					throw new BusinessException("LMS-05097");
				}				
			}
			listIdsDocumentos.add(parameters.getString("idDoctoServico"));
		}

		for (Iterator iter = listIdsDocumentos.iterator(); iter.hasNext();) {
			String idDoctoServico = (String) iter.next();
			TypedFlatMap mapPreManifestoDocumento = new TypedFlatMap();
			mapPreManifestoDocumento.put("masterId", parameters.getString("masterId"));
			mapPreManifestoDocumento.put("doctoServico.idDoctoServico", idDoctoServico);
    		saveItemInstance(mapPreManifestoDocumento, "preManifestoDocumento");
		}
		return null;
    }

    /**
     * Persiste o Pre-manifesto, para a tela de carregamentoDescargaDocumentos
     */
    public Serializable savePreManifestoDocumentoToCarregamento(TypedFlatMap parameters) {
		List listIdsDocumentos = new ArrayList();
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
		
		if (parameters.getList("ids") != null) {
			listIdsDocumentos = parameters.getList("ids");
		} else if (parameters.getString("doctoServico.idDoctoServico") != null) {
			listIdsDocumentos.add(parameters.getString("doctoServico.idDoctoServico"));
		}
		
		Manifesto manifesto = this.getManifestoService().findByIdInitLazyProperties(parameters.getLong("masterId"), false);
		
		for (Iterator iter = listIdsDocumentos.iterator(); iter.hasNext();) {
			String idDoctoServico = (String)iter.next();
			DoctoServico doctoServico = this.getDoctoServicoService().findById(Long.valueOf(idDoctoServico));
			ControleCarga controleCarga = this.getControleCargaService().findByIdInitLazyProperties(manifesto.getControleCarga().getIdControleCarga(), false);
			
			PreManifestoDocumento preManifestoDocumento = new PreManifestoDocumento();
			preManifestoDocumento.setManifesto(manifesto);
			preManifestoDocumento.setDoctoServico(doctoServico);
			this.getPreManifestoDocumentoService().store(preManifestoDocumento);
			
			String tpDocumento = null;
			String strPreManifesto = preManifestoDocumento.getManifesto().getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(preManifestoDocumento.getManifesto().getNrPreManifesto().toString(), 8, '0');
	    	if (controleCarga.getTpControleCarga().getValue().equals("V")) {
	    		tpDocumento = "PMV"; //Pre-Manifesto Viagem
	    		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("61"), doctoServico.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHoraAtual,	null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
	        	if (manifesto.getTpStatusManifesto().getValue().equals("CC") || manifesto.getTpStatusManifesto().getValue().equals("EC")){
	        		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("25"), doctoServico.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHoraAtual,	null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
	        	}
	        	if (manifesto.getTpStatusManifesto().getValue().equals("CC")){
	        		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("26"), doctoServico.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHoraAtual,	null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
	        	}
	    	} else { 
	    		tpDocumento = "PME"; //Pre-Manifesto Entrega
	    		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("62"), doctoServico.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHoraAtual,	null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
	    		if (manifesto.getTpStatusManifesto().getValue().equals("CC") || manifesto.getTpStatusManifesto().getValue().equals("EC")){
	    			incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("24"), doctoServico.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHoraAtual,	null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
	    		}
	        	if (manifesto.getTpStatusManifesto().getValue().equals("CC")){
	        		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("27"), doctoServico.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHoraAtual,	null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
	        	}
	    	}
		}
		return null;
    }  
    
    /**
     * Remove uma lista de registros items.
	 *  
	 * @param ids ids dos desciçoes item a serem removidos.
	 * 
	 * 
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsPreManifestoDocumento(List ids) {
    	super.removeItemByIds(ids, "preManifestoDocumento");
    }     
    
    /**
     * Remove uma lista de documentos do pre-manifesto.
     */
    public void removeDocumentosPreManifesto(TypedFlatMap criteria) {
    	Long masterId = getMasterId(criteria);
        MasterEntry masterEntry = getMasterFromSession(masterId, true);
        ItemList itemList = masterEntry.getItems("preManifestoDocumento");
        ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("preManifestoDocumento");
        
        List listIds = new ArrayList();
        for (Iterator iter = itemList.iterator(masterId, itemListConfig); iter.hasNext();) {
			PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
			listIds.add(preManifestoDocumento.getIdPreManifestoDocumento());
		}
        
        if(!listIds.isEmpty()) {
        	this.removeByIdsPreManifestoDocumento(listIds);
        }        
    }
    
    /**
     * Valida remoção de lista de documentos do pre-manifesto.
     */
    public void validateRemoveDocumentosPreManifesto(TypedFlatMap criteria) {
    	Long masterId = getMasterId(criteria);
    	if (masterId != null) {
	        MasterEntry masterEntry = getMasterFromSession(masterId, true);
	        ItemList itemList = masterEntry.getItems("preManifestoDocumento");	        
	        List ids = criteria.getList("ids");
	        
			if (itemList.size() == ids.size()) {
				throw new BusinessException("LMS-05052");
			}
    	}
    }    
    
    
	/**
	 * FindPaginated para grid de PreManifestoDocumento
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedPreManifestoDocumento(TypedFlatMap criteria) {
    	ResultSetPage rspPreManifestoDocumento = findPaginatedItemList(criteria, "preManifestoDocumento");
    	
    	List listPreManifestoDocumento = new ArrayList();
    	for(int i=0; i< rspPreManifestoDocumento.getList().size(); i++) {
    		PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) rspPreManifestoDocumento.getList().get(i);
    		DoctoServico doctoServico = this.getDoctoServicoService().findDoctoServicoById(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
    		TypedFlatMap mapPreManifestoDocumento = new TypedFlatMap();
    		
        	mapPreManifestoDocumento.put("idPreManifestoDocumento", preManifestoDocumento.getIdPreManifestoDocumento());
        	mapPreManifestoDocumento.put("nrOrdem", preManifestoDocumento.getNrOrdem());
        	
    		mapPreManifestoDocumento.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());    		
    		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.description", doctoServico.getTpDocumentoServico().getDescription());
    		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.value", doctoServico.getTpDocumentoServico().getValue());
    		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.status", doctoServico.getTpDocumentoServico().getStatus());    		
    		mapPreManifestoDocumento.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
    		mapPreManifestoDocumento.put("doctoServico.qtVolumes", doctoServico.getQtVolumes());
    		mapPreManifestoDocumento.put("doctoServico.psReal", doctoServico.getPsReal());
    		mapPreManifestoDocumento.put("doctoServico.vlMercadoria", doctoServico.getVlMercadoria());
    		mapPreManifestoDocumento.put("doctoServico.vlTotalDocServico", doctoServico.getVlTotalDocServico());
    		mapPreManifestoDocumento.put("doctoServico.dtPrevEntrega", doctoServico.getDtPrevEntrega());
    		mapPreManifestoDocumento.put("doctoServico.dhEmissao", doctoServico.getDhEmissao());
    		
   			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialOrigem.idFilial", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialOrigem.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());	
    		
    		if (doctoServico.getFilialByIdFilialDestino() != null) {
    			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.idFilial", doctoServico.getFilialByIdFilialDestino().getIdFilial());
    			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.sgFilial", doctoServico.getFilialByIdFilialDestino().getSgFilial());
    			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.pessoa.nmFantasia", doctoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());	
    		}
    		if (doctoServico.getMoeda() != null) {
    			mapPreManifestoDocumento.put("doctoServico.moeda.idMoeda", doctoServico.getMoeda().getIdMoeda());
    			mapPreManifestoDocumento.put("doctoServico.moeda.sgMoeda", doctoServico.getMoeda().getSgMoeda());
    			mapPreManifestoDocumento.put("doctoServico.moeda.dsSimbolo", doctoServico.getMoeda().getDsSimbolo());
    			
    			mapPreManifestoDocumento.put("doctoServico.moeda2.sgMoeda", doctoServico.getMoeda().getSgMoeda());
    			mapPreManifestoDocumento.put("doctoServico.moeda2.dsSimbolo", doctoServico.getMoeda().getDsSimbolo());    			
    		}
    		if (doctoServico.getServico() != null) {
    			mapPreManifestoDocumento.put("doctoServico.servico.idServico", doctoServico.getServico().getIdServico());
    			mapPreManifestoDocumento.put("doctoServico.servico.sgServico", doctoServico.getServico().getSgServico());
    			mapPreManifestoDocumento.put("doctoServico.servico.dsServico", doctoServico.getServico().getDsServico());
    			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.description", doctoServico.getServico().getTpModal().getDescription());
    			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.value", doctoServico.getServico().getTpModal().getValue());
    			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.status", doctoServico.getServico().getTpModal().getStatus());
    			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.description", doctoServico.getServico().getTpAbrangencia().getDescription());
    			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.value", doctoServico.getServico().getTpAbrangencia().getValue());
    			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.status", doctoServico.getServico().getTpAbrangencia().getStatus());
    			if (doctoServico.getServico().getTipoServico() != null) {
    				mapPreManifestoDocumento.put("doctoServico.servico.tipoServico.blPriorizar", doctoServico.getServico().getTipoServico().getBlPriorizar());
    			}
    		}
    		if (doctoServico.getLocalizacaoMercadoria() != null) {
    			mapPreManifestoDocumento.put("doctoServico.localizacaoMercadoria.idLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
    			mapPreManifestoDocumento.put("doctoServico.localizacaoMercadoria.dsLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
    		}
    		if (doctoServico.getClienteByIdClienteRemetente() != null) {
    			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteRemetente.idCliente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
    			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
    		}
    		if (doctoServico.getClienteByIdClienteDestinatario() != null) {
    			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteDestinatario.idCliente", doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
    			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
    		}

    		
    		listPreManifestoDocumento.add(mapPreManifestoDocumento);
    	}
    	
    	rspPreManifestoDocumento.setList(listPreManifestoDocumento);
    	
    	return rspPreManifestoDocumento;
	}
	
	/**
	 * GetRowCount para grid de PreManifestoDocumento
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountPreManifestoDocumento(TypedFlatMap criteria) {
		return getRowCountItemList(criteria, "preManifestoDocumento");
	}
	
    public Object findByIdPreManifestoDocumento(MasterDetailKey key) {
    	PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) findItemById(key, "preManifestoDocumento");
    	DoctoServico doctoServico = preManifestoDocumento.getDoctoServico();
    	TypedFlatMap mapPreManifestoDocumento = new TypedFlatMap();
    	
    	mapPreManifestoDocumento.put("idPreManifestoDocumento", preManifestoDocumento.getIdPreManifestoDocumento());
    	
		mapPreManifestoDocumento.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());    		
		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.description", doctoServico.getTpDocumentoServico().getDescription());
		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.value", doctoServico.getTpDocumentoServico().getValue());
		mapPreManifestoDocumento.put("doctoServico.tpDocumentoServico.status", doctoServico.getTpDocumentoServico().getStatus());    		
		mapPreManifestoDocumento.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
		mapPreManifestoDocumento.put("doctoServico.qtVolumes", doctoServico.getQtVolumes());
		mapPreManifestoDocumento.put("doctoServico.psReal", doctoServico.getPsReal());
		mapPreManifestoDocumento.put("doctoServico.vlMercadoria", doctoServico.getVlMercadoria());
		mapPreManifestoDocumento.put("doctoServico.vlTotalDocServico", doctoServico.getVlTotalDocServico());
		mapPreManifestoDocumento.put("doctoServico.dtPrevEntrega", doctoServico.getDtPrevEntrega());
		mapPreManifestoDocumento.put("doctoServico.dhEmissao", doctoServico.getDhEmissao());
		if (doctoServico.getFilialByIdFilialDestino() != null) {
			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.idFilial", doctoServico.getFilialByIdFilialDestino().getIdFilial());
			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.sgFilial", doctoServico.getFilialByIdFilialDestino().getSgFilial());
			mapPreManifestoDocumento.put("doctoServico.filialByIdFilialDestino.pessoa.nmFantasia", doctoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());	
		}
		if (doctoServico.getMoeda() != null) {
			mapPreManifestoDocumento.put("doctoServico.moeda.idMoeda", doctoServico.getMoeda().getIdMoeda());
			mapPreManifestoDocumento.put("doctoServico.moeda.sgMoeda", doctoServico.getMoeda().getSgMoeda());
			mapPreManifestoDocumento.put("doctoServico.moeda.dsSimbolo", doctoServico.getMoeda().getDsSimbolo());
		}
		if (doctoServico.getServico() != null) {
			mapPreManifestoDocumento.put("doctoServico.servico.idServico", doctoServico.getServico().getIdServico());
			mapPreManifestoDocumento.put("doctoServico.servico.dsServico", doctoServico.getServico().getDsServico());
			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.description", doctoServico.getServico().getTpModal().getDescription());
			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.value", doctoServico.getServico().getTpModal().getValue());
			mapPreManifestoDocumento.put("doctoServico.servico.tpModal.status", doctoServico.getServico().getTpModal().getStatus());
			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.description", doctoServico.getServico().getTpAbrangencia().getDescription());
			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.value", doctoServico.getServico().getTpAbrangencia().getValue());
			mapPreManifestoDocumento.put("doctoServico.servico.tpAbrangencia.status", doctoServico.getServico().getTpAbrangencia().getStatus());
			if (doctoServico.getServico().getTipoServico() != null) {
				mapPreManifestoDocumento.put("doctoServico.servico.tipoServico.blPriorizar", doctoServico.getServico().getTipoServico().getBlPriorizar());
			}
		}
		if (doctoServico.getLocalizacaoMercadoria() != null) {
			mapPreManifestoDocumento.put("doctoServico.localizacaoMercadoria.idLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
			mapPreManifestoDocumento.put("doctoServico.localizacaoMercadoria.dsLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
		}
		if (doctoServico.getClienteByIdClienteRemetente() != null) {
			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteRemetente.idCliente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
		}
		if (doctoServico.getClienteByIdClienteDestinatario() != null) {
			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteRemetente.idCliente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
			mapPreManifestoDocumento.put("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
		}
    	
    	return mapPreManifestoDocumento;    	
    }    	
    
    
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		/**
		 * Declaracao da classe pai
		 */		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(Manifesto.class);
		
		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
    	Comparator descComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				PreManifestoDocumento preManifestoDocumento1 = (PreManifestoDocumento)obj1;
				PreManifestoDocumento preManifestoDocumento2 = (PreManifestoDocumento)obj2;
					
				if (preManifestoDocumento1.getNrOrdem() != null && preManifestoDocumento2.getNrOrdem() != null)
					return preManifestoDocumento1.getNrOrdem().compareTo(preManifestoDocumento2.getNrOrdem());
				return -1;
			}
    	};		
		
    	
    	/**
    	 * Esta instancia é responsavel por carregar os 
    	 * items filhos na sessão a partir do banco de dados.
    	 */
    	ItemListConfig itemInit = new ItemListConfig() {
 
    		/**
    		 * Find paginated do filho
    		 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
    		 * 
    		 * @param masterId id do pai
    		 * @param parameters todos os parametros vindo da tela pai
    		 */    		
			public List initialize(Long masterId, Map parameters) {				
				return getPreManifestoDocumentoService().findPreManifestoDocumentoByIdManifesto(masterId);
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */			
			public Integer getRowCount(Long masterId, Map parameters) {				
				return getPreManifestoDocumentoService().getRowCountPreManifestoDocumento(masterId);				
			}
			
			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object bean) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) bean;				
				TypedFlatMap param = (TypedFlatMap) parameters;
				
				if (param.getLong("masterId") !=null) {
					preManifestoDocumento.setManifesto(getManifestoService().findById(param.getLong("masterId")));
				}				
				if (param.getLong("doctoServico.idDoctoServico") != null) {
					preManifestoDocumento.setDoctoServico(getDoctoServicoService().findDoctoServicoById(param.getLong("doctoServico.idDoctoServico")));
				}				
				if (param.getInteger("nrOrdem") != null) {
					preManifestoDocumento.setNrOrdem(param.getInteger("nrOrdem"));
				}
				return preManifestoDocumento;
			}			
    	};    	

    	config.addItemConfig("preManifestoDocumento", PreManifestoDocumento.class, itemInit, descComparator);
		return config;
	}
	
	/**
	 * ############################# FIM DOS MÉTODOS PARA TELA DE DF2 ###############################
	 */	

	
	
	
	/**
	 * ################## INICIO DOS MÉTODOS PARA A POP-UP DE ADICIONAR DOCUMENTOS ##################
	 */

	/**
	 * Utilizado pela combo de cias aereas.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findCiaAerea(TypedFlatMap criteria) {
		return this.getEmpresaService().findCiaAerea(criteria);
	}
	
	public List findLookupAwb(Map criteria) {
		return this.getAwbService().findLookup(criteria);
	}
	
	public List findLookupProprietario(Map criteria) {
   		Map mapPessoa = new HashMap();
   		mapPessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));

   		Map map = new HashMap();
   		map.put("pessoa", mapPessoa);
   		map.put("tpSituacao", criteria.get("tpSituacao"));

    	List list = proprietarioService.findLookup(map);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Proprietario proprietario = (Proprietario)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idProprietario", proprietario.getIdProprietario());
    		typedFlatMap.put("tpIdentificacao", proprietario.getPessoa().getTpIdentificacao());
    		typedFlatMap.put("nrIdentificacao", FormatUtils.formatIdentificacao(proprietario.getPessoa()));
    		typedFlatMap.put("nmPessoa", proprietario.getPessoa().getNmPessoa());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
	
	/**
	 * Obtém os ids dos Documentos de Servico da Sessão.
	 * @param masterId
	 * @return
	 */
	private List getIdsDoctoServicoFromSession(Long masterId) {
		MasterEntry master = getMasterFromSession(masterId, true);
		ItemList itens = getItemsFromSession(master, "preManifestoDocumento");
		ItemListConfig itensConfig = getMasterConfig().getItemListConfig("preManifestoDocumento");

		List list = new ArrayList();
		if (itens.isInitialized()) {
			for (Iterator it = itens.iterator(masterId, itensConfig); it.hasNext(); ) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento)it.next();
				list.add(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
			} 
		}
		return list;
	}
	
	/**
	 * Busca doctoServico para a grid da aba "Terminal" da pop-up de abrir 
	 * doctoServicoPreManifesto.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findListTerminalNovo(TypedFlatMap criteria) {
		Long idManifesto = criteria.getLong("masterId");
		Long idSolicitacaoRetirada = criteria.getLong("solicitacaoRetirada.idSolicitacaoRetirada");
		Long idRotaColetaEntrega = criteria.getLong("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega");
		Long idFilialDestinoManifesto = criteria.getLong("manifesto.filialByIdFilialDestino.idFilial");
		Long idConsignatario = criteria.getLong("doctoServico.clienteByIdClienteConsignatario.idCliente");
		Long idClienteRemetente = criteria.getLong("doctoServico.clienteByIdClienteRemetente.idCliente");
		Long idClienteDestinatario = criteria.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente");
		
		String tpDoctoServico = criteria.getString("doctoServico.tpDocumentoServico");
		Long idFilialOrigemDoctoServico = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial");
		Long idFilialDestinoDoctoServico = criteria.getLong("doctoServico.filialByIdFilialDestino.idFilial");
		Long idDoctoServico = criteria.getLong("doctoServico.idDoctoServico");
		
		Long idAwb = criteria.getLong("doctoServico.ctoAwbs.idAwb");
		String tpManifesto = criteria.getString("manifesto.tpManifesto");
		String tpAbrangencia = criteria.getString("manifesto.tpAbrangencia");
		String tpPreManifesto = criteria.getString("manifesto.tpPreManifesto");
		
		//Caso a tela esteja sendo chamada pela tela de carregamento...
    	List idsDoctoServico = new ArrayList();
    	if ((criteria.getString("from")!=null) && (criteria.getString("from").equals("carregamento"))) {
    		List preManifestoDocumentos = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifesto(criteria.getLong("masterId"));
    		for (Iterator iter = preManifestoDocumentos.iterator(); iter.hasNext();) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
	    		idsDoctoServico.add(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
			} 
    	} else {
    		idsDoctoServico = getIdsDoctoServicoFromSession(idManifesto);
    	}
    	
    	// Localização do documento.
    	String localizacaoDocumento = "terminal";
		
		return this.getDoctoServicoService().findAdicionarDoctoServicoPreManifesto( 
				idManifesto,
	    		idSolicitacaoRetirada,
	    		idRotaColetaEntrega,
	    		idFilialDestinoManifesto,
	    		idConsignatario,
	    		idClienteRemetente,
	    		idClienteDestinatario,
	    		tpDoctoServico,
	    		idFilialOrigemDoctoServico,
	    		idFilialDestinoDoctoServico,
	    		idDoctoServico,
	    		idAwb,
	    		tpManifesto,
	    		tpAbrangencia,
	    		tpPreManifesto,
	    		idsDoctoServico,
	    		localizacaoDocumento,
	    		null,
	    		null);
	}
	
	/**
	 * Listagem para a grid de Descarga
	 * @param criteria
	 * @return
	 */
	public List findListDescarga(TypedFlatMap criteria) {
		// Busca a Localização de mercadoria atraves do Código
		List idsLocalizacao = new ArrayList();
		LocalizacaoMercadoria localizacaoMercadoria = this.getLocalizacaoMercadoriaService().findLocalizacaoMercadoriaByCodigo(Short.valueOf("33"));
		LocalizacaoMercadoria localizacaoMercadoria2 = this.getLocalizacaoMercadoriaService().findLocalizacaoMercadoriaByCodigo(Short.valueOf("34"));
		idsLocalizacao.add(localizacaoMercadoria.getIdLocalizacaoMercadoria());
		idsLocalizacao.add(localizacaoMercadoria2.getIdLocalizacaoMercadoria());

		
		
		// Caso o tipo do manifesto seja de viagem, testa se existe fluxo de filial da filial 
		// de origem para a filial de destino.
		if(criteria.getString("manifesto.tpManifesto").equals("V")) {
			
			Long idFilialDestinoManifesto = criteria.getLong("manifesto.filialByIdFilialDestino.idFilial");
			if (criteria.getLong("masterId")!=null){
				//Então pre-manifesto já está persistido no banco.
				Manifesto manifesto = getManifestoService().findByIdInitLazyProperties(criteria.getLong("masterId"), false);
				idFilialDestinoManifesto = manifesto.getFilialByIdFilialDestino().getIdFilial();
			}
			
			List idsFluxoFilial = new ArrayList();
			idsFluxoFilial.addAll(this.getOrdemFilialFluxoService().findIdFluxoFilialByIdFilialOrigemAndIdFilialDestino(SessionUtils.getFilialSessao().getIdFilial(), idFilialDestinoManifesto));
			// Adicionando as substitutas...
			List listSubstAtendimentoFilial = substAtendimentoFilialService.findSubstAtendimentoFilialByIdFilialSubstituta(idFilialDestinoManifesto);
			for (Iterator iter = listSubstAtendimentoFilial.iterator(); iter.hasNext();) {
				SubstAtendimentoFilial substAtendimentoFilial = (SubstAtendimentoFilial) iter.next();
				idsFluxoFilial.addAll(this.getOrdemFilialFluxoService().findIdFluxoFilialByIdFilialOrigemAndIdFilialDestino(SessionUtils.getFilialSessao().getIdFilial(), substAtendimentoFilial.getFilialByIdFilialDestino().getIdFilial()));	
			}			
			if (!idsFluxoFilial.isEmpty()) {
				criteria.put("idsFluxoFilial", idsFluxoFilial);
			} else {
				return new ArrayList();
			}
		}
		
		//Pesquisa os documentos.
		Long masterId = getMasterId(criteria);
    	List listDoctoServico = new ArrayList();
    	
    	
    	//Caso a tela esteja sendo chamada pela tela de carregamento...
    	List idsDoctoServico = new ArrayList();
    	if ((criteria.getString("from")!=null) && (criteria.getString("from").equals("carregamento"))) {
    		List preManifestoDocumentos = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifesto(criteria.getLong("masterId"));
    		for (Iterator iter = preManifestoDocumentos.iterator(); iter.hasNext();) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
	    		idsDoctoServico.add(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
			} 
    	} else {
    		idsDoctoServico = getIdsDoctoServicoFromSession(masterId);
    	}
    	
		List listDoctos = this.getDoctoServicoService().findListDoctoServicoByLocalizacaoMercadoria(idsLocalizacao,	Boolean.FALSE, criteria, idsDoctoServico);
		for (Iterator iter = listDoctos.iterator(); iter.hasNext();) {
			DoctoServico doctoServico = (DoctoServico) iter.next();
    		TypedFlatMap mapDoctoServico = new TypedFlatMap();    		
    		
    		mapDoctoServico.put("idDoctoServico", doctoServico.getIdDoctoServico());    		
    		mapDoctoServico.put("tpDocumentoServico.description", doctoServico.getTpDocumentoServico().getDescription());
    		mapDoctoServico.put("tpDocumentoServico.value", doctoServico.getTpDocumentoServico().getValue());
    		mapDoctoServico.put("tpDocumentoServico.status", doctoServico.getTpDocumentoServico().getStatus());    		
    		mapDoctoServico.put("nrDoctoServico", doctoServico.getNrDoctoServico());
    		mapDoctoServico.put("qtVolumes", doctoServico.getQtVolumes());
    		mapDoctoServico.put("psReal", doctoServico.getPsReal());
    		mapDoctoServico.put("vlMercadoria", doctoServico.getVlMercadoria());
    		mapDoctoServico.put("dtPrevEntrega", doctoServico.getDtPrevEntrega());
    		mapDoctoServico.put("dhEmissao", doctoServico.getDhEmissao());
    		mapDoctoServico.put("blBloqueado", doctoServico.getBlBloqueado());
    		if (doctoServico.getFilialByIdFilialOrigem() != null) {
    			mapDoctoServico.put("filialByIdFilialOrigem.idFilial", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
    			mapDoctoServico.put("filialByIdFilialOrigem.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
    			mapDoctoServico.put("filialByIdFilialOrigem.pessoa.nmFantasia", doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());	
			}    		
    		if (doctoServico.getFilialByIdFilialDestino() != null) {
    			mapDoctoServico.put("filialByIdFilialDestino.idFilial", doctoServico.getFilialByIdFilialDestino().getIdFilial());
    			mapDoctoServico.put("filialByIdFilialDestino.sgFilial", doctoServico.getFilialByIdFilialDestino().getSgFilial());
    			mapDoctoServico.put("filialByIdFilialDestino.pessoa.nmFantasia", doctoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());	
			}
    		if (doctoServico.getMoeda() != null) {
    			mapDoctoServico.put("moeda.idMoeda", doctoServico.getMoeda().getIdMoeda());
    			mapDoctoServico.put("moeda.sgMoeda", doctoServico.getMoeda().getSgMoeda());
    			mapDoctoServico.put("moeda.dsSimbolo", doctoServico.getMoeda().getDsSimbolo());
			}
    		if (doctoServico.getServico() != null) {
    			mapDoctoServico.put("servico.idServico", doctoServico.getServico().getIdServico());
    			mapDoctoServico.put("servico.sgServico", doctoServico.getServico().getSgServico());
    			mapDoctoServico.put("servico.tpModal.description", doctoServico.getServico().getTpModal().getDescription());
    			mapDoctoServico.put("servico.tpModal.value", doctoServico.getServico().getTpModal().getValue());
    			mapDoctoServico.put("servico.tpModal.status", doctoServico.getServico().getTpModal().getStatus());
    			mapDoctoServico.put("servico.tpAbrangencia.description", doctoServico.getServico().getTpAbrangencia().getDescription());
    			mapDoctoServico.put("servico.tpAbrangencia.value", doctoServico.getServico().getTpAbrangencia().getValue());
    			mapDoctoServico.put("servico.tpAbrangencia.status", doctoServico.getServico().getTpAbrangencia().getStatus());    			
    			if (doctoServico.getServico().getTipoServico() != null) {
    				mapDoctoServico.put("servico.tipoServico.blPriorizar", doctoServico.getServico().getTipoServico().getBlPriorizar());
				}
			}
    		if (doctoServico.getClienteByIdClienteRemetente() != null) {
    			mapDoctoServico.put("clienteByIdClienteRemetente.idCliente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
    			mapDoctoServico.put("clienteByIdClienteRemetente.pessoa.nmPessoa", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
			}
    		if (doctoServico.getClienteByIdClienteDestinatario() != null) {
    			mapDoctoServico.put("clienteByIdClienteDestinatario.idCliente", doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
    			mapDoctoServico.put("clienteByIdClienteDestinatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
			}
    		if (doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal() != null) {
    			mapDoctoServico.put("rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega", doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal().getIdRotaColetaEntrega());
			} else {
				if (doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid() != null) {
					mapDoctoServico.put("rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega", doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid().getIdRotaColetaEntrega());
				}
			}
    		if (doctoServico.getPaisOrigem() != null) {
    			mapDoctoServico.put("paisOrigem.idPais", doctoServico.getPaisOrigem().getIdPais());
    		}
    		
    		/*
    		 * Verifica se dtPrevEntrega é menos que a data atual, se o tpModal é igual a 'A'(Áereo)
    		 * e se o tipo de serviço possui blPriorizar = 'S'. Caso essas cláusulas sejam verdadeiras,
    		 * colocar o registro em destaque na grid.
    		 */
    		if ( (doctoServico.getDtPrevEntrega() != null && 
    						doctoServico.getDtPrevEntrega().compareTo(JTDateTimeUtils.getDataAtual()) < 0) ||
    		    doctoServico.getServico().getTpModal().getValue().equals("A") ||
    		    doctoServico.getServico().getTipoServico().getBlPriorizar().booleanValue() ) {    			
    			mapDoctoServico.put("emDestaque", Boolean.TRUE);
    		} else {
    			mapDoctoServico.put("emDestaque", Boolean.FALSE);
    		}
    			    		
    		listDoctoServico.add(mapDoctoServico);
		}
    	
		return listDoctoServico;
	}	
	
		
	/**
	 * FindPaginated para a grid de Descarga
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedChegadas(TypedFlatMap criteria) {
		List listTpStatusColeta = new ArrayList();
		listTpStatusColeta.add("AD");
		listTpStatusColeta.add("ED");
		listTpStatusColeta.add("NT");
		
		List listPedidoColeta = this.getPedidoColetaService().findPedidoColetaByTpStatusColetaByIdFilialResponsavel(
																			listTpStatusColeta, 
																			SessionUtils.getFilialSessao().getIdFilial());
		
		List listPedidosDetalhes = new ArrayList();
    	for(int i=0; i< listPedidoColeta.size(); i++) {
    		PedidoColeta pedidoColeta = (PedidoColeta) listPedidoColeta.get(i);
    		TypedFlatMap mapPedidoColeta = new TypedFlatMap();  
    		
    		List listDetalheColeta = this.getDetalheColetaService().findDetalheColeta(pedidoColeta.getIdPedidoColeta());
    		for (Iterator iter = listDetalheColeta.iterator(); iter.hasNext();) {
				DetalheColeta detalheColeta = (DetalheColeta) iter.next();    		
    			
				Long idFilialDestino = null;
				if (criteria.getLong("doctoServico.filialByIdFilialDestino.idFilial") != null) {
					idFilialDestino = criteria.getLong("doctoServico.filialByIdFilialDestino.idFilial");
				} else {
					idFilialDestino = criteria.getLong("manifesto.filialByIdFilialDestino.idFilial");
					if (criteria.getLong("masterId")!=null){
						Manifesto manifesto = getManifestoService().findByIdInitLazyProperties(criteria.getLong("masterId"), false);
						idFilialDestino = manifesto.getFilialByIdFilialDestino().getIdFilial();
					}
				}
				
				if (detalheColeta.getFilial() != null && detalheColeta.getFilial().getIdFilial().equals(idFilialDestino)) {	
					mapPedidoColeta.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());
		    		mapPedidoColeta.put("nrColeta", pedidoColeta.getNrColeta());
		    		    		
		     		if (pedidoColeta.getFilialByIdFilialResponsavel() != null) {
		     			mapPedidoColeta.put("filialByIdFilialResponsavel.sgFilial", pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
					}
		    		if (pedidoColeta.getCliente() != null) {
		    			mapPedidoColeta.put("cliente.pessoa.nmPessoa", pedidoColeta.getCliente().getPessoa().getNmPessoa());
					}
					
					mapPedidoColeta.put("psReal", detalheColeta.getPsMercadoria());
					mapPedidoColeta.put("qtVolumes", detalheColeta.getQtVolumes());
					mapPedidoColeta.put("vlMercadoria", detalheColeta.getVlMercadoria());	
					if (detalheColeta.getServico() != null) {
						mapPedidoColeta.put("servico.sgServico", detalheColeta.getServico().getSgServico());
					}
					if (detalheColeta.getNaturezaProduto() != null) {
						mapPedidoColeta.put("naturezaProduto.dsNaturezaProduto", detalheColeta.getNaturezaProduto().getDsNaturezaProduto());
					}
		    		if (detalheColeta.getMoeda() != null) {
		    			mapPedidoColeta.put("moeda.idMoeda", detalheColeta.getMoeda().getIdMoeda());
		    			mapPedidoColeta.put("moeda.sgMoeda", detalheColeta.getMoeda().getSgMoeda());
		    			mapPedidoColeta.put("moeda.dsSimbolo", detalheColeta.getMoeda().getDsSimbolo());
					}
					if (detalheColeta.getFilial() != null) {
						mapPedidoColeta.put("filialDetalhe.pessoa.nmFantasia", detalheColeta.getFilial().getPessoa().getNmFantasia());
					}
		    		if (detalheColeta.getMunicipio() != null) {
		    			mapPedidoColeta.put("paisOrigem.idPais", detalheColeta.getMunicipio().getUnidadeFederativa().getPais().getIdPais());
		    		}
					
					listPedidosDetalhes.add(mapPedidoColeta);				
				}
			}
		}
    	
    	ResultSetPage rsp = new ResultSetPage(Integer.valueOf(1), false, false, Collections.EMPTY_LIST);
    	rsp.setCurrentPage(criteria.getInteger("_currentPage"));
    	rsp.setList(listPedidosDetalhes);

		return rsp;
	}
	
	/**
	 * FindPaginated para a grid de Descarga
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedExecutadas(TypedFlatMap criteria) {
		List listTpStatusColeta = new ArrayList();
		listTpStatusColeta.add("EX");
		
		List listPedidoColeta = this.getPedidoColetaService().findPedidoColetaByTpStatusColetaByIdFilialResponsavel(
																			listTpStatusColeta, 
																			SessionUtils.getFilialSessao().getIdFilial());
		
		List listPedidosDetalhes = new ArrayList();
    	for(int i=0; i< listPedidoColeta.size(); i++) {
    		PedidoColeta pedidoColeta = (PedidoColeta) listPedidoColeta.get(i);
			TypedFlatMap mapPedidoColeta = new TypedFlatMap();  
		
    		List listDetalheColeta = this.getDetalheColetaService().findDetalheColeta(pedidoColeta.getIdPedidoColeta());
    		for (Iterator iter = listDetalheColeta.iterator(); iter.hasNext();) {
				DetalheColeta detalheColeta = (DetalheColeta) iter.next();    		
    			
				Long idFilialDestino = null;
				if (criteria.getLong("doctoServico.filialByIdFilialDestino.idFilial") != null) {
					idFilialDestino = criteria.getLong("doctoServico.filialByIdFilialDestino.idFilial");
				} else {
					idFilialDestino = criteria.getLong("manifesto.filialByIdFilialDestino.idFilial");
					if (criteria.getLong("masterId")!=null){
						Manifesto manifesto = getManifestoService().findByIdInitLazyProperties(criteria.getLong("masterId"), false);
						idFilialDestino = manifesto.getFilialByIdFilialDestino().getIdFilial();
					}
				}
				
				if (detalheColeta.getFilial() != null && detalheColeta.getFilial().getIdFilial().equals(idFilialDestino)) {				
					mapPedidoColeta.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());
		    		mapPedidoColeta.put("nrColeta", pedidoColeta.getNrColeta());
		    		    		
		     		if (pedidoColeta.getFilialByIdFilialResponsavel() != null) {
		     			mapPedidoColeta.put("filialByIdFilialResponsavel.sgFilial", pedidoColeta.getFilialByIdFilialResponsavel()
		     																					.getSgFilial());
					}
		    		if (pedidoColeta.getCliente() != null) {
		    			mapPedidoColeta.put("cliente.pessoa.nmPessoa", pedidoColeta.getCliente().getPessoa().getNmPessoa());
					}
					
					mapPedidoColeta.put("psReal", detalheColeta.getPsMercadoria());
					mapPedidoColeta.put("qtVolumes", detalheColeta.getQtVolumes());
					mapPedidoColeta.put("vlMercadoria", detalheColeta.getVlMercadoria());	
					if (detalheColeta.getServico() != null) {
						mapPedidoColeta.put("servico.sgServico", detalheColeta.getServico().getSgServico());
					}
					if (detalheColeta.getNaturezaProduto() != null) {
						mapPedidoColeta.put("naturezaProduto.dsNaturezaProduto", detalheColeta.getNaturezaProduto()
																							  .getDsNaturezaProduto());
					}
		    		if (detalheColeta.getMoeda() != null) {
		    			mapPedidoColeta.put("moeda.idMoeda", detalheColeta.getMoeda().getIdMoeda());
		    			mapPedidoColeta.put("moeda.sgMoeda", detalheColeta.getMoeda().getSgMoeda());
		    			mapPedidoColeta.put("moeda.dsSimbolo", detalheColeta.getMoeda().getDsSimbolo());
					}
					if (detalheColeta.getFilial() != null) {
						mapPedidoColeta.put("filialDetalhe.pessoa.nmFantasia", detalheColeta.getFilial().getPessoa()
																										.getNmFantasia());
					}
		    		if (detalheColeta.getMunicipio() != null) {
		    			mapPedidoColeta.put("paisOrigem.idPais", detalheColeta.getMunicipio().getUnidadeFederativa().getPais()
		    																										.getIdPais());
		    		}
					
					listPedidosDetalhes.add(mapPedidoColeta);	
				}
			}
		}

    	ResultSetPage rsp = new ResultSetPage(Integer.valueOf(1), false, false, Collections.EMPTY_LIST);
    	rsp.setCurrentPage(criteria.getInteger("_currentPage"));
    	rsp.setList(listPedidosDetalhes);

		return rsp;
	}			
		
		
	/**
	 * Método que calcula os totais de peso, volume, valor mercadoria e quantidade de documentos.
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap getCalculaTotais(TypedFlatMap criteria) {		
		TypedFlatMap totais = new TypedFlatMap();
		
		BigDecimal totalMercadoria = BigDecimalUtils.ZERO;
		BigDecimal totalPeso = BigDecimalUtils.ZERO;		
		Integer totalVolumes = Integer.valueOf(0);
		Integer totalDocumentos = Integer.valueOf(0);
				
		List listObjeto = criteria.getList("list");
		if (listObjeto != null) {
			for (Iterator iter = listObjeto.iterator(); iter.hasNext();) {
				TypedFlatMap mapObjeto = (TypedFlatMap) iter.next();
				
				if (mapObjeto.getBigDecimal("vlMercadoria") != null) {
					totalMercadoria = totalMercadoria.add( this.getConversaoMoedaService().findConversaoMoeda(
																	 SessionUtils.getPaisSessao().getIdPais(),
																	 mapObjeto.getLong("moeda.idMoeda"),
																	 SessionUtils.getPaisSessao().getIdPais(),
																	 SessionUtils.getMoedaSessao().getIdMoeda(),
																	 JTDateTimeUtils.getDataAtual(),
																	 mapObjeto.getBigDecimal("vlMercadoria")) );
				}
	
				if (mapObjeto.getBigDecimal("psReal") != null) {
					totalPeso = totalPeso.add(mapObjeto.getBigDecimal("psReal"));
				}
				
				if (mapObjeto.getInteger("qtVolumes") != null) {
					totalVolumes = Integer.valueOf(totalVolumes.intValue() + mapObjeto.getInteger("qtVolumes").intValue());
				}
			}
			totalDocumentos = Integer.valueOf(listObjeto.size());
		}
		totais.put("totalVolumes", totalVolumes);
		totais.put("totalPeso", FormatUtils.formatDecimal("#,###,##0.000", totalPeso));
		totais.put("totalMercadoria", SessionUtils.getMoedaSessao().getSiglaSimbolo() + " " +
									  FormatUtils.formatDecimal("#,###,###,###,##0.00", totalMercadoria));
		totais.put("totalDocumentos", totalDocumentos);
				
		return totais;
	}
	
	
	/**
	 * Método chamado ao selecionar TODOS os registros na grid de DoctoServico para validação de situações.
	 * @param criteria
	 * @return
	 */
	public List getValidacaoDoctoServicoSelectAll(TypedFlatMap criteria) {
		List listValidacoes = new ArrayList();
		List list = criteria.getList("list");		
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			TypedFlatMap mapParam = (TypedFlatMap) iter.next();
			TypedFlatMap resultMap = this.getValidacaoDoctoServico(mapParam);
			if (resultMap.getLong("idDoctoServico") != null) {
				listValidacoes.add(resultMap);
			}			
		}
		
		return listValidacoes;
	}
	
	
	/**
	 * Método chamado ao selecionar UM registro na grid de DoctoServico para validação de situações
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap getValidacaoDoctoServico(TypedFlatMap criteria) {
		TypedFlatMap mapResult = new TypedFlatMap();
		mapResult.put("rowIndex", criteria.getLong("rowIndex"));
		
		//Caso a tela 'chamadora' seja a tela de carregamento...
		if ((criteria.getString("from")!=null) && (criteria.getString("from").equals("carregamento"))) {
			Manifesto manifesto = this.getManifestoService().findById(criteria.getLong("masterId"));
					
			if (manifesto!=null) {
				criteria.put("manifesto.tpManifesto", manifesto.getTpManifesto().getValue());
				criteria.put("manifesto.filialByIdFilialDestino.idFilial", manifesto.getFilialByIdFilialDestino().getIdFilial());
				if (manifesto.getTpManifesto().getValue().equals("V")) {
					criteria.put("manifesto.tpPreManifesto", manifesto.getTpManifestoViagem().getValue());
				} else {
					criteria.put("manifesto.tpPreManifesto", manifesto.getTpManifestoEntrega().getValue());
				}
				
				criteria.put("manifesto.tpModal", manifesto.getTpModal().getValue());
				
				if ((manifesto.getControleCarga()!=null) && (manifesto.getControleCarga().getRotaColetaEntrega()!=null)) { 
					criteria.put("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", 
							manifesto.getControleCarga().getRotaColetaEntrega().getIdRotaColetaEntrega());;
				}
				
				if (manifesto.getFilialByIdFilialDestino()!=null) {
					criteria.put("filialByIdFilialDestino.idFilial", manifesto.getFilialByIdFilialDestino().getIdFilial());
				}
				
				if (manifesto.getCliente()!=null) {
					criteria.put("manifesto.cliente.idCliente", manifesto.getCliente().getIdCliente());
				}
			}
		}
		
		// Se for to tipo 'Entrega'
		if (criteria.getString("manifesto.tpManifesto").equals("E")) {
			
			// Verifica se o Documento é do tipo 'RRE' e permite incluir no manifesto somente se
			// for vinculada a uma MIR do tipo 'Administrativo para entrega'.
			if (criteria.getString("doctoServico.tpDocumentoServico.value").equals("RRE")) {
				DocumentoMir documentoMir = this.getDocumentoMirService().findDocumentoMirByIdReciboReembolso(
																				criteria.getLong("doctoServico.idDoctoServico"), "AE");			
				if (documentoMir == null) {
					if (!criteria.getBoolean("checkedAll").booleanValue()) {
						mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05046"));
					} else {
						mapResult.put("idDoctoServico", criteria.getLong("doctoServico.idDoctoServico"));
					}
					return mapResult;
				}
			} else {			
				
				// Verifica se o documento possui agendamento Ativo
				AgendamentoDoctoServico agendamentoDoctoServico = null;
								
				List listAgendamentoDoctoServico = this.getAgendamentoDoctoServicoService().
											findAgendamentoByIdDoctoServico(criteria.getLong("doctoServico.idDoctoServico"));
				for (Iterator iter = listAgendamentoDoctoServico.iterator(); iter.hasNext();) {
					AgendamentoDoctoServico agendamento = (AgendamentoDoctoServico) iter.next();

					if (agendamento.getTpSituacao().getValue().equals("A") && 
							agendamento.getAgendamentoEntrega().getTpSituacaoAgendamento().getValue().equals("A")) {
						agendamentoDoctoServico = agendamento;						
					}
				}
				
				if (agendamentoDoctoServico != null) {
					AgendamentoEntrega agendamentoEntrega = agendamentoDoctoServico.getAgendamentoEntrega();
					// Testa se data do Agendamento é maior ou menos que a data atual
					if (agendamentoEntrega.getDtAgendamento().compareTo(JTDateTimeUtils.getDataAtual()) > 0 ) {		    			
						String dataAgendamento = JTFormatUtils.format(agendamentoEntrega.getDtAgendamento(), JTFormatUtils.SHORT);
						if (!criteria.getBoolean("checkedAll").booleanValue()) {
							mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05044") + dataAgendamento);
						} else {
							mapResult.put("idDoctoServico", criteria.getLong("doctoServico.idDoctoServico"));
						}
						return mapResult;
	
					} else if (agendamentoEntrega.getDtAgendamento().compareTo(JTDateTimeUtils.getDataAtual()) < 0 ) {
						if (!criteria.getBoolean("checkedAll").booleanValue()) {
							mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05045"));
						} else {
							mapResult.put("idDoctoServico", criteria.getLong("doctoServico.idDoctoServico"));
						}
						return mapResult;
					}
	
				} else {
					// Verifica se cliente Remetente do Documento em questão possui necessidade de Agendamento 
					// e se o Documento já foi agendado.			
					Boolean blAgendamentoPessoaFisicaRemetente = criteria.getBoolean("doctoServico.clienteByIdClienteRemetente.blAgendamentoPessoaFisica");
					Boolean blAgendamentoPessoaJuridicaRemetente = criteria.getBoolean("doctoServico.clienteByIdClienteRemetente.blAgendamentoPessoaJuridica");
					String tpPessoaDestinatario = criteria.getString("doctoServico.clienteByIdClienteDestinatario.tpPessoa");
					
					if ( (blAgendamentoPessoaFisicaRemetente.booleanValue() && tpPessoaDestinatario.equals("F")) ||
						 (blAgendamentoPessoaJuridicaRemetente.booleanValue() && tpPessoaDestinatario.equals("J")) ) {
						if (!criteria.getBoolean("checkedAll").booleanValue()) {
							mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05043"));
						} else {
							mapResult.put("idDoctoServico", criteria.getLong("doctoServico.idDoctoServico"));
						}
						return mapResult;
					}				
				}
				
				// Verifica se a rota de entrega do documento é diferente da rota de entrega 
				// do Controle de Carga no Manifesto em questão
				if (criteria.getLong("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega") != null && 
						criteria.getLong("doctoServico.rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega") != null) {
					if (!criteria.getLong("doctoServico.rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega").equals(
							criteria.getLong("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega")) ) {
						if (!criteria.getBoolean("checkedAll").booleanValue()) {
							mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05047"));
							mapResult.put("confirmar", Boolean.TRUE);
						} else {
							mapResult.put("idDoctoServico", criteria.getLong("doctoServico.idDoctoServico"));
						}
						return mapResult;
					}
				}
				
				// Verifica se o consignatario escolhido no manifesto é igual ao consignatario do documento para o
				// tipo de pre-manifesto 'parceira retira'.
				if (criteria.getString("manifesto.tpPreManifesto").equals("EP")) {
					if (criteria.getLong("doctoServico.clienteByIdClienteConsignatario.idCliente") == null) {
						mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05117"));
					} 
					if (criteria.getLong("doctoServico.clienteByIdClienteConsignatario.idCliente") != null && 
							!criteria.getLong("doctoServico.clienteByIdClienteConsignatario.idCliente").equals(criteria.getLong("manifesto.cliente.idCliente"))) {
						mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05118"));
					}
				}
			}			
		}
		
		if (!criteria.getString("doctoServico.tpDocumentoServico.value").equals("RRE")) {
			// Verifica se o documetno está bloqueado.
			if (criteria.getBoolean("doctoServico.blBloqueado").booleanValue()) {
				if (!criteria.getBoolean("checkedAll").booleanValue()) {
					mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05048"));
				} else {
					mapResult.put("idDoctoServico", criteria.getLong("doctoServico.idDoctoServico"));
				}
				return mapResult;
			}
			
			// Verifica se o modal do documento é diferente do modal do Manifesto em questão
			String tpModalDoctoServico = criteria.getString("doctoServico.servico.tpModal");
			if (StringUtils.isBlank(tpModalDoctoServico))
				tpModalDoctoServico = criteria.getString("doctoServico.servico.tpModal.value");

			if (tpModalDoctoServico != null && !tpModalDoctoServico.equals(criteria.getString("manifesto.tpModal")) ) {
				if (!criteria.getBoolean("checkedAll").booleanValue()) {
					mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05049"));
					mapResult.put("confirmar", Boolean.TRUE);
				} else {
					mapResult.put("idDoctoServico", criteria.getLong("doctoServico.idDoctoServico"));
				}
				return mapResult;			
			}
			
			// Verificação de documentos com filial de destino diferente do destino do manifesto.
			if (criteria.getLong("filialByIdFilialDestino.idFilial") != null) {
				Long idDoctoServico = criteria.getLong("doctoServico.idDoctoServico");
				Long idFilialDestinoDocto = criteria.getLong("doctoServico.filialByIdFilialDestino.idFilial");
				Long idFilialDestinoPreManifesto = criteria.getLong("filialByIdFilialDestino.idFilial");
			
				if (idFilialDestinoDocto.equals(idFilialDestinoPreManifesto)) {
					List listaSubstAtendimentoFilial = this.getSubstAtendimentoFilialService().
							findSubstAtendimentoFilialByIdFilialDestino(idFilialDestinoPreManifesto,null);
					
					if (!listaSubstAtendimentoFilial.isEmpty()) {
						Filial filialDestinoSubstituta = 
							this.getSubstAtendimentoFilialService().findFilialDestinoDoctoServico(idDoctoServico, null, null, null);						
			
						if (!idFilialDestinoPreManifesto.equals(filialDestinoSubstituta.getIdFilial())) {
							if (!criteria.getBoolean("checkedAll").booleanValue()) {
								Pessoa pessoa = this.getPessoaService().findById(filialDestinoSubstituta.getIdFilial());
								mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05051") + pessoa.getNmFantasia());
							} else {
								mapResult.put("idDoctoServico", idDoctoServico);
							}
							return mapResult;
						}						
					}
					
				} else {
					/*
					 * Corrigido erro de unique result, pois estava buscando mais de 1 resultado na consulta(hotfix)
					 */
					List<SubstAtendimentoFilial> substAtendimentoFilial = this.getSubstAtendimentoFilialService().
							findSubstAtendimentoFilialByIdFilialDestinoAndFilialSubstituta(idFilialDestinoDocto, 
																						   idFilialDestinoPreManifesto);				

					if (substAtendimentoFilial != null && !substAtendimentoFilial.isEmpty()) {					
						Filial filialDestinoSubstituta = 
							this.getSubstAtendimentoFilialService().findFilialDestinoDoctoServico(idDoctoServico, null, null, null);						

						if (!idFilialDestinoPreManifesto.equals(filialDestinoSubstituta.getIdFilial())) {
							if (!criteria.getBoolean("checkedAll").booleanValue()) {
								Pessoa pessoa = this.getPessoaService().findById(filialDestinoSubstituta.getIdFilial());
								mapResult.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05051") + pessoa.getNmFantasia());
							} else {
								mapResult.put("idDoctoServico", idDoctoServico);
							}
							return mapResult;
						}
					}
					
				}				
			}
			
		}
								
		return mapResult;
	}
	
	
	/**
	 * Método que retorna um TypedFlatMap com o id do DoctoServico pesquisado atraves do Tipo de Documento, 
	 * da Filial de Origem e do Número.
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap findDoctoServico(TypedFlatMap criteria) {
		TypedFlatMap mapDoctoServico = new TypedFlatMap();	
		
		if ((criteria.getString("from")!=null) && (criteria.getString("from").equals("carregamento"))) {
			Manifesto manifesto = this.getManifestoService().findById(criteria.getLong("masterId"));
					
			criteria.put("manifesto.tpManifesto", manifesto.getTpManifesto().getValue());
			criteria.put("manifesto.filialByIdFilialDestino.idFilial", manifesto.getFilialByIdFilialDestino().getIdFilial());
			criteria.put("manifesto.filialByIdFilialOrigem.idFilial", manifesto.getFilialByIdFilialOrigem().getIdFilial());
			if (manifesto.getTpManifesto().getValue().equals("V")) {
				criteria.put("manifesto.tpPreManifesto", manifesto.getTpManifestoViagem().getValue());
			} else {
				criteria.put("manifesto.tpPreManifesto", manifesto.getTpManifestoEntrega().getValue());
			}
		}
		
		DoctoServico doctoServico = this.getDoctoServicoService()
				.findDoctoServicoByTpDocumentoByIdFilialOrigemByNrDoctoServicoByIdFilialLocalizacao(
																criteria.getString("doctoServico.tpDocumento"),
																criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"),
																criteria.getLong("doctoServico.nrDoctoServico"), 
																null);
		
		// Caso não encontre nenhum registro de documento.
		if (doctoServico == null) {
			throw new BusinessException("LMS-05050");
		}
		
		// Somente faz os testes caso o tipo do documento seja diferente de 'RRE'.
		if (!doctoServico.getTpDocumentoServico().getValue().equals("RRE")) {
			// Caso o documento não esteja na filial do usuario logado.
			if (doctoServico.getFilialLocalizacao()==null || !doctoServico.getFilialLocalizacao().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
				throw new BusinessException("LMS-05088");
			}
		}
		
		// Verifica se o documento está associado a uma solicitação de retirada para poder ser incluido em
		// um manifesto de Viagem ou Entrega, caso a filial de retirada for igual da filial localização do documento, gera excessão.
		List listSolicitacaoRetirada = this.getSolicitacaoRetiradaService().
												findSolicitacaoRetiradaByIdDoctoServico(doctoServico.getIdDoctoServico());
		if (!listSolicitacaoRetirada.isEmpty()) {
			for (Iterator iter = listSolicitacaoRetirada.iterator(); iter.hasNext();) {
				SolicitacaoRetirada solicitacaoRetirada = (SolicitacaoRetirada) iter.next();
				
				if (solicitacaoRetirada.getFilialRetirada().getIdFilial().equals(doctoServico.getFilialLocalizacao().getIdFilial()) && 
						solicitacaoRetirada.getTpSituacao().getValue().equals("A")) {
					throw new BusinessException("LMS-05119");
				}				
			}			
		}		
				
		if(criteria.getString("manifesto.tpManifesto").equals("V")) {
			if (doctoServico.getTpDocumentoServico().getValue().equals("NFT")||doctoServico.getTpDocumentoServico().getValue().equals("RRE")) {
				throw new BusinessException("LMS-05050");
			}
			
			if (doctoServico.getTpDocumentoServico().getValue().equals("NTE")) {
				Municipio municipioFilialOrigem = municipioService.findMunicipioByFilial(Long.valueOf(MapUtils.getString(criteria, "manifesto.filialByIdFilialOrigem.idFilial")));
		    	Municipio municipioFilialDestino = municipioService.findMunicipioByFilial(Long.valueOf(MapUtils.getString(criteria, "manifesto.filialByIdFilialDestino.idFilial")));
		    	
		    	if(!municipioFilialOrigem.getIdMunicipio().equals(municipioFilialDestino.getIdMunicipio())
		    			|| !doctoServico.getFilialByIdFilialOrigem().getIdFilial().equals(Long.valueOf(MapUtils.getString(criteria, "manifesto.filialByIdFilialOrigem.idFilial")))){
		    		throw new BusinessException("LMS-05050");
		    	}
		    	
			}
			
			if(criteria.getString("manifesto.tpAbrangencia").equals("N")) {
				if (!doctoServico.getTpDocumentoServico().getValue().equals("CTR") && 
						!doctoServico.getTpDocumentoServico().getValue().equals("MDA") &&
						!doctoServico.getTpDocumentoServico().getValue().equals("CTE") &&
						!doctoServico.getTpDocumentoServico().getValue().equals("NTE")) {
					throw new BusinessException("LMS-05050");
				}
			}
			
			if(criteria.getString("manifesto.tpAbrangencia").equals("I")) {
				if (!doctoServico.getTpDocumentoServico().getValue().equals("CRT")) {
					throw new BusinessException("LMS-05050");
				}				
			}			

			// Caso o documento não esteja 'No Terminal' e 'Em Descarga'.
			if ( !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("24")) &&
				 !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("28")) &&
				 !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("34")) ) {				
					throw new BusinessException("LMS-05089");
			}			
			
			// Caso o documento não possua Fluxo de Filial valido
			List idsFluxoFilial = this.getOrdemFilialFluxoService().
			findIdFluxoFilialByIdFilialOrigemAndIdFilialDestino(SessionUtils.getFilialSessao().getIdFilial(), 
												criteria.getLong("manifesto.filialByIdFilialDestino.idFilial"));
			
			if (doctoServico.getFluxoFilial() != null) {
				if (!idsFluxoFilial.toString().contains(doctoServico.getFluxoFilial().getIdFluxoFilial().toString())) {
					mapDoctoServico.put("error", this.getConfiguracoesFacade().getMensagem("LMS-05113"));
				}
			}
			
		} else if(criteria.getString("manifesto.tpManifesto").equals("E") && 
				!doctoServico.getTpDocumentoServico().getValue().equals("RRE")) {
			// Caso o documento não esteja 'No Terminal' e 'Em Descarga'.
			if ( doctoServico.getLocalizacaoMercadoria()==null ||
				(!doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("24")) &&
				 !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("35")) &&
				 !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("43")) && 
				 !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("33")) &&
				 !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("34")) )) {				
					throw new BusinessException("LMS-05089");
			}	
		}

		
		/*CQPRO00023462 - Valida informações de reentrega*/
		if(!ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO.equals(doctoServico.getTpDocumentoServico().getValue())) {
			preManifestoDocumentoService.validateReentregaDocumento(doctoServico.getIdDoctoServico());	
		}
						
		mapDoctoServico.put("idDoctoServico", doctoServico.getIdDoctoServico());    		
		mapDoctoServico.put("tpDocumentoServico.description", doctoServico.getTpDocumentoServico().getDescription());
		mapDoctoServico.put("tpDocumentoServico.value", doctoServico.getTpDocumentoServico().getValue());
		mapDoctoServico.put("tpDocumentoServico.status", doctoServico.getTpDocumentoServico().getStatus());    		
		mapDoctoServico.put("nrDoctoServico", doctoServico.getNrDoctoServico());
		mapDoctoServico.put("qtVolumes", doctoServico.getQtVolumes());
		mapDoctoServico.put("psReal", doctoServico.getPsReal());
		mapDoctoServico.put("vlMercadoria", doctoServico.getVlMercadoria());
		mapDoctoServico.put("dtPrevEntrega", doctoServico.getDtPrevEntrega());
		mapDoctoServico.put("dhEmissao", doctoServico.getDhEmissao());
		mapDoctoServico.put("blBloqueado", doctoServico.getBlBloqueado());
		if (doctoServico.getFilialByIdFilialOrigem() != null) {
			mapDoctoServico.put("filialByIdFilialOrigem.idFilial", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
			mapDoctoServico.put("filialByIdFilialOrigem.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
			mapDoctoServico.put("filialByIdFilialOrigem.pessoa.nmFantasia", doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());	
		}
		if (doctoServico.getFilialByIdFilialDestino() != null) {
			mapDoctoServico.put("filialByIdFilialDestino.idFilial", doctoServico.getFilialByIdFilialDestino().getIdFilial());
			mapDoctoServico.put("filialByIdFilialDestino.sgFilial", doctoServico.getFilialByIdFilialDestino().getSgFilial());
			mapDoctoServico.put("filialByIdFilialDestino.pessoa.nmFantasia", doctoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());	
		}
		if (doctoServico.getMoeda() != null) {
			mapDoctoServico.put("moeda.idMoeda", doctoServico.getMoeda().getIdMoeda());
			mapDoctoServico.put("moeda.sgMoeda", doctoServico.getMoeda().getSgMoeda());
			mapDoctoServico.put("moeda.dsSimbolo", doctoServico.getMoeda().getDsSimbolo());
		}
		if (doctoServico.getServico() != null) {
			mapDoctoServico.put("servico.idServico", doctoServico.getServico().getIdServico());
			mapDoctoServico.put("servico.dsServico", doctoServico.getServico().getDsServico());
			mapDoctoServico.put("servico.tpModal.description", doctoServico.getServico().getTpModal().getDescription());
			mapDoctoServico.put("servico.tpModal.value", doctoServico.getServico().getTpModal().getValue());
			mapDoctoServico.put("servico.tpModal.status", doctoServico.getServico().getTpModal().getStatus());
			mapDoctoServico.put("servico.tpAbrangencia.description", doctoServico.getServico().getTpAbrangencia().getDescription());
			mapDoctoServico.put("servico.tpAbrangencia.value", doctoServico.getServico().getTpAbrangencia().getValue());
			mapDoctoServico.put("servico.tpAbrangencia.status", doctoServico.getServico().getTpAbrangencia().getStatus());
			if (doctoServico.getServico().getTipoServico() != null) {
				mapDoctoServico.put("servico.tipoServico.blPriorizar", doctoServico.getServico().getTipoServico().getBlPriorizar());
			}
		}
		if (doctoServico.getClienteByIdClienteRemetente() != null) {
			mapDoctoServico.put("clienteByIdClienteRemetente.idCliente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
			mapDoctoServico.put("clienteByIdClienteRemetente.pessoa.nmPessoa", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
			mapDoctoServico.put("clienteByIdClienteRemetente.blAgendamentoPessoaFisica", doctoServico.getClienteByIdClienteRemetente().getBlAgendamentoPessoaFisica());
			mapDoctoServico.put("clienteByIdClienteRemetente.blAgendamentoPessoaJuridica", doctoServico.getClienteByIdClienteRemetente().getBlAgendamentoPessoaJuridica());
		}
		if (doctoServico.getClienteByIdClienteDestinatario() != null) {
			mapDoctoServico.put("clienteByIdClienteDestinatario.idCliente", doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
			mapDoctoServico.put("clienteByIdClienteDestinatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
			mapDoctoServico.put("clienteByIdClienteDestinatario.tpPessoa", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getTpPessoa().getValue());			
		}
		if (doctoServico.getClienteByIdClienteConsignatario() != null) {
			mapDoctoServico.put("clienteByIdClienteConsignatario.idCliente", doctoServico.getClienteByIdClienteConsignatario().getIdCliente());
			mapDoctoServico.put("clienteByIdClienteConsignatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteConsignatario().getPessoa().getNmPessoa());
			mapDoctoServico.put("clienteByIdClienteConsignatario.tpPessoa", doctoServico.getClienteByIdClienteConsignatario().getPessoa().getTpPessoa().getValue());
			
		}		
		if (doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal() != null) {
			mapDoctoServico.put("rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega", doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal().getIdRotaColetaEntrega());
		} else {
			if (doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid() != null) {
				mapDoctoServico.put("rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega", doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid().getIdRotaColetaEntrega());	
			}					
		}
		if (doctoServico.getPaisOrigem() != null) {
			mapDoctoServico.put("paisOrigem.idPais", doctoServico.getPaisOrigem().getIdPais());
		}		
		
		return mapDoctoServico;
	}
	
	/**
	 * ################# FIM DOS MÉTODOS PARA A POP-UP DE ADICIONAR DOCUMENTOS ######################
	 */
	
	/**
	 * Carrega informacoes basicas do pre-manifesto.
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap findManifesto(TypedFlatMap criteria) {
		return this.getManifestoService().findManifestoToCarregamentoPopUp(criteria);
	}
	
	/**
	 * Faz a validacao do PCE.
	 *  
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validatePCE(TypedFlatMap criteria) {
		
		//Busca o pai a partir da sessao...
		MasterEntry entry = getMasterFromSession(criteria.getLong("idManifesto"), true);
    	Manifesto manifesto = (Manifesto) entry.getMaster();
    	
    	//Busca o itemList a partir da sessao...
    	ItemList items = getItemsFromSession(entry, "preManifestoDocumento");
    	ItemListConfig itemsConfig = getMasterConfig().getItemListConfig("preManifestoDocumento"); 
    	
    	//Inicia a captura dos doctoServico que estao vinculados ao preManifestoDocto da sessao...
    	List doctosServicos = new ArrayList();
    	for(Iterator iter = items.iterator(manifesto.getIdManifesto(), itemsConfig); iter.hasNext();) {
    		PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
    		DoctoServico doctoServico = this.getDoctoServicoService().findDoctoServicoById(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
    		doctosServicos.add(doctoServico);
    	}
		
    	TypedFlatMap mapRetorno = new TypedFlatMap();
		mapRetorno.put("codigos", this.getManifestoService().validatePCE(doctosServicos));
		return mapRetorno;
	}
	
	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
	}
	public void setPreManifestoDocumentoService(
			PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}	
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public RotaColetaEntregaService getRotaColetaEntregaService() {
		return rotaColetaEntregaService;
	}
	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	public RotaIdaVoltaService getRotaIdaVoltaService() {
		return rotaIdaVoltaService;
	}
	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}
	public AwbService getAwbService() {
		return awbService;
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public EventoDocumentoServicoService getEventoDocumentoServicoService() {
		return eventoDocumentoServicoService;
	}
	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public LocalizacaoMercadoriaService getLocalizacaoMercadoriaService() {
		return localizacaoMercadoriaService;
	}
	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}
	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public DetalheColetaService getDetalheColetaService() {
		return detalheColetaService;
	}
	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}
	public SubstAtendimentoFilialService getSubstAtendimentoFilialService() {
		return substAtendimentoFilialService;
	}
	public void setSubstAtendimentoFilialService(
			SubstAtendimentoFilialService substAtendimentoFilialService) {
		this.substAtendimentoFilialService = substAtendimentoFilialService;
	}
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public PessoaService getPessoaService() {
		return pessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public AgendamentoDoctoServicoService getAgendamentoDoctoServicoService() {
		return agendamentoDoctoServicoService;
	}
	public void setAgendamentoDoctoServicoService(
			AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}
	public AgendamentoEntregaService getAgendamentoEntregaService() {
		return agendamentoEntregaService;
	}
	public void setAgendamentoEntregaService(
			AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}
	public DocumentoMirService getDocumentoMirService() {
		return documentoMirService;
	}
	public void setDocumentoMirService(DocumentoMirService documentoMirService) {
		this.documentoMirService = documentoMirService;
	}
	public SolicitacaoRetiradaService getSolicitacaoRetiradaService() {
		return solicitacaoRetiradaService;
	}
	public void setSolicitacaoRetiradaService(
			SolicitacaoRetiradaService solicitacaoRetiradaService) {
		this.solicitacaoRetiradaService = solicitacaoRetiradaService;
	}
	public ControleTrechoService getControleTrechoService() {
		return controleTrechoService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public CtoInternacionalService getCtoInternacionalService() {
		return ctoInternacionalService;
	}
	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}
	public MdaService getMdaService() {
		return mdaService;
	}
	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}
	public ReciboReembolsoService getReciboReembolsoService() {
		return reciboReembolsoService;
	}
	public void setReciboReembolsoService(
			ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}
	public OrdemFilialFluxoService getOrdemFilialFluxoService() {
		return ordemFilialFluxoService;
	}
	public void setOrdemFilialFluxoService(
			OrdemFilialFluxoService ordemFilialFluxoService) {
		this.ordemFilialFluxoService = ordemFilialFluxoService;
	}
	public DocumentoServicoRetiradaService getDocumentoServicoRetiradaService() {
		return documentoServicoRetiradaService;
	}
	public void setDocumentoServicoRetiradaService(
			DocumentoServicoRetiradaService documentoServicoRetiradaService) {
		this.documentoServicoRetiradaService = documentoServicoRetiradaService;
	}
	public EventoControleCargaService getEventoControleCargaService() {
		return eventoControleCargaService;
	}
	public void setEventoControleCargaService(
			EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	public EmpresaService getEmpresaService() {
		return empresaService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return carregamentoDescargaService;
	}
	public void setCarregamentoDescargaService(
			CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}
	public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
		return incluirEventosRastreabilidadeInternacionalService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

}
