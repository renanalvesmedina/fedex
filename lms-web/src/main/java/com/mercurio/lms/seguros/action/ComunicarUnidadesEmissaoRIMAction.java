package com.mercurio.lms.seguros.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.service.ProcessoSinistroService;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.seguros.comunicarUnidadesEmissaoRIMAction"
 */
public class ComunicarUnidadesEmissaoRIMAction extends CrudAction {
	private ConfiguracoesFacade configuracoesFacade;
	private ManifestoColetaService manifestoColetaService;
	private ClienteService clienteService;
	private FilialService filialService;
	private ConhecimentoService conhecimentoService;
	private DomainValueService domainValueService;
	private CtoInternacionalService ctoInternacionalService; 
	private MdaService mdaService;
	private ReciboReembolsoService reciboReembolsoService;
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private DoctoServicoService doctoServicoService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoInternacionalService manifestoInternacionalService;
	private ManifestoEntregaService manifestoEntregaService;
	
	public List findClienteLookup(Map criteria) {
		return this.getClienteService().findLookup(criteria);
	}

	public List findLookupFilial(Map criteria) {
		return this.getFilialService().findLookup(criteria);
	}
	
	/**
	 * Busca a filial baseado no documento de servi�o
	 * 
	 * @param criteria
	 * @return
	 */
	public List findLookupFilialByDocumentoServico(Map criteria) {
		FilterList filter = new FilterList(getFilialService().findLookup(criteria)) {
			@Override
			public Map filterItem(Object item) {
				Filial filial = (Filial) item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idFilial", filial.getIdFilial());
				typedFlatMap.put("sgFilial", filial.getSgFilial());
				return typedFlatMap;
	}
		};
		return (List) filter.doFilter();
	}

	private List findLookupFilialByManifesto(TypedFlatMap criteria) {
		return this.filialService.findLookupFilial(criteria);
	}

	/**
	 * Faz a pesquisa da lookup de manifestoColeta
	 * 
	 * @param TypedFlatMap
	 *            criteria
	 * @return List
	 */
	public List findLookupManifestoColeta(Map criteria) {
		return this.getManifestoColetaService().findLookup(criteria);
	}

	public List findLookupManifestoDocumentFilialEN(TypedFlatMap criteria) {
		return findLookupFilialByManifesto(criteria);
	}

	public List findLookupManifestoDocumentFilialVI(TypedFlatMap criteria) {
		return findLookupFilialByManifesto(criteria);
	}

	public List findLookupManifestoDocumentFilialVN(TypedFlatMap criteria) {
		return findLookupFilialByManifesto(criteria);
	}

	public List findLookupManifestoDocumentNumberEN(TypedFlatMap criteria) {
		criteria.put("manifesto.tpManifesto", "E");
		List list = manifestoEntregaService.findLookupByTagManifesto(criteria);
		List retorno = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ManifestoEntrega manifestoEntrega = (ManifestoEntrega) iter.next();
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("idManifestoEntrega", manifestoEntrega.getIdManifestoEntrega());
			typedFlatMap.put("nrManifestoEntrega", manifestoEntrega.getNrManifestoEntrega());
			typedFlatMap.put("idFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getIdFilial());
			typedFlatMap.put("sgFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getSgFilial());
			typedFlatMap.put("nmFantasiaFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
			retorno.add(typedFlatMap);
	}
		return retorno;
	}

	public List findLookupManifestoDocumentNumberVI(Map criteria) {
		Map manifesto = (Map) criteria.get("manifesto");
		manifesto.put("tpManifesto", "V");
		manifesto.put("tpAbrangencia", "I");
		return this.manifestoInternacionalService.findLookup(criteria);
	}

	public List findLookupManifestoDocumentNumberVN(Map criteria) {
		Map manifesto = (Map) criteria.get("manifesto");
		manifesto.put("tpManifesto", "V");
		manifesto.put("tpAbrangencia", "N");
		return this.manifestoViagemNacionalService.findLookup(criteria);
	}

	public List findLookupProcessoSinistro(TypedFlatMap tfm) {
		TypedFlatMap resultData = new TypedFlatMap();
		String nrProcessoSinistro = (String) tfm.remove("nrProcessoSinistro");
		List result = new ArrayList();
		for (Iterator it = this.getService().findLookup(tfm).iterator(); it.hasNext();) {
			ProcessoSinistro processoSinistro = (ProcessoSinistro) it.next();
			resultData.put("idProcessoSinistro", processoSinistro.getIdProcessoSinistro());
			resultData.put("nrProcessoSinistro", processoSinistro.getNrProcessoSinistro());
			resultData.put("processoSinistro.tipoSeguro.idTipoSeguro", processoSinistro.getTipoSeguro().getIdTipoSeguro());
			resultData.put("dhSinistro", processoSinistro.getDhSinistro());
			resultData.put("tipoSinistro.dsTipo", processoSinistro.getTipoSinistro().getDsTipo().getValue());
			resultData.put("municipio.nmMunicipio", processoSinistro.getMunicipio().getNmMunicipio());
			resultData.put("municipio.unidadeFederativa.sgUnidadeFederativa", processoSinistro.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			resultData.put("dsSinistro", processoSinistro.getDsSinistro());
			result.add(resultData);
	}
		return result;
	}

	public List findLookupServiceDocumentFilialCRT(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialCTE(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	/**
	 * FindLookup para filial do tipo de DoctoServico Escolhido.
	 */
	public List findLookupServiceDocumentFilialCTR(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialMDA(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}
	
	public List findLookupServiceDocumentFilialNFT(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialNTE(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialRRE(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentNumberCRT(Map criteria) {
		return this.getCtoInternacionalService().findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberCTE(Map criteria) {
		return this.getConhecimentoService().findLookup(criteria);
	}

	/**
	 * FindLookup para a tag DoctoServico.
	 */
	public List findLookupServiceDocumentNumberCTR(Map criteria) {
		return this.getConhecimentoService().findLookup(criteria);
	}
	
	public List findLookupServiceDocumentNumberMDA(Map criteria) {
		return this.getMdaService().findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberNFT(Map criteria) {
		return this.getConhecimentoService().findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberNTE(Map criteria) {
		return this.getConhecimentoService().findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberRRE(Map criteria) {
		return this.getReciboReembolsoService().findLookup(criteria);
	}

	/**
	 * Popula as informacoes da grid
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage resultSetPage = this.getService().findPaginatedSinistroDoctoServico(criteria);
		if (resultSetPage!=null) {
			for (Iterator iter = resultSetPage.getList().iterator(); iter.hasNext();) {
				Map result = (Map) iter.next();
				result.put("sgMoedaPrejuizo", result.get("sgMoeda"));
				result.put("dsSimboloMoedaPrejuizo", result.get("dsSimboloMoeda"));
			}
		}
		return resultSetPage;
	}
	
	public List findPaginatedEnviarEmailRim(TypedFlatMap criteria) {
		List list = this.getService().findPaginatedEnviarEmailRim(criteria);
		List result = new ArrayList();
		
		if (list != null && !list.isEmpty()) {
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Object[] row = (Object[]) iter.next();
				
				TypedFlatMap map = new TypedFlatMap();
				map.put("idPessoa",row[0]);
				map.put("nmFantasia",row[1]);
				map.put("dsEmail",row[2]);
				
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 * Popula as informacoes da grid
	 * 
	 * @param criteria
	 * @return
	 */
	//FIXME Retirar _pageSize (lc)
	public ResultSetPage findPaginatedSelecionarDocumentos(TypedFlatMap criteria) {
		criteria.put("_currentPage", "1");
		criteria.put("_pageSize", "1000");
		ResultSetPage resultSetPage = this.getService().findPaginatedSelecionarDocumentosRim(criteria);
		return resultSetPage;
	}
	
	//######################################
	// Regras de negocio da tela...
	//######################################
	/**
	 * M�todo que popula a combo de tipos de documento apenas com CTR, CRT, MDA.
	 * 
	 * @param criteria
	 * @return List
	 */
	public List findTipoDocumentoServico(Map criteria) {
		List dominiosValidos = new ArrayList();
		dominiosValidos.add("CTR");
		dominiosValidos.add("CRT");
		dominiosValidos.add("MDA");
		dominiosValidos.add("NTE");
		dominiosValidos.add("CTE");
		List retorno = getDomainValueService().findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
		return retorno;
	}
	
	public List findTipoManifesto(Map criteria) {
		List<String> dominiosValidos = new ArrayList<String>();
		dominiosValidos.add("EN");
		dominiosValidos.add("VN");
		List retorno = this.domainValueService.findByDomainNameAndValues("DM_TAG_MANIFESTO", dominiosValidos);
		return retorno;
		}

    //###############################
    // Objetos da tela
    //###############################
	public List findTipoPrejuizo(Map criteria) {
		List dominiosValidos = new ArrayList();
		dominiosValidos.add("P");
		dominiosValidos.add("T");
		List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_PREJUIZO", dominiosValidos);
		return retorno;
    }
    
	public void generateEmailRim(TypedFlatMap criteria) {
		this.getService().generateEmailComunicarUnidadesEmissaoRIM(criteria);
    }
    
	public ClienteService getClienteService() {
		return clienteService;
    }
    
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
    }
    
    //################################
    // Metodos para a tag documents
    //################################
	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}
    
	public CtoInternacionalService getCtoInternacionalService() {
		return ctoInternacionalService;
    }
	
	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}
    	
	public DomainValueService getDomainValueService() {
		return domainValueService;
			}
    	
	public FilialService getFilialService() {
		return filialService;
    }

	public ManifestoColetaService getManifestoColetaService() {
		return manifestoColetaService;
    }

	public MdaService getMdaService() {
		return mdaService;
    }

	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
    }

	public ReciboReembolsoService getReciboReembolsoService() {
		return reciboReembolsoService;
    }  

	public Integer getRowCount(TypedFlatMap criteria) {
		return this.getService().getRowCountSinistroDoctoServico(criteria);
    }  
    
	public Integer getRowCountSelecionarDocumentos(TypedFlatMap criteria) {
		return this.getService().getRowCountSelecionarDocumentosRim(criteria);
    }

	public ProcessoSinistroService getService() {
		return (ProcessoSinistroService) this.defaultService;
    }    

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
    }

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
    }

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
    }
    
	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
     }
     
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
     }
     
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
     }
     
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
     }

	public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
		this.manifestoColetaService = manifestoColetaService;
     }

	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
     }

	public void setManifestoInternacionalService(ManifestoInternacionalService manifestoInternacionalService) {
		this.manifestoInternacionalService = manifestoInternacionalService;
     	}

	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
     }

	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
     }

	public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}

	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}

	public void setService(ProcessoSinistroService processoSinistroService) {
		this.defaultService = processoSinistroService;
	}

	/**
	 * Atualiza a data de geracao da carta de emissao de RIM...
	 * 
	 * @param criteria
	 */
	public void updateDataGeracaoEmissaoRIM(TypedFlatMap criteria) {
		List idsSinistroDoctoServico = criteria.getList("idsSinistroDoctoServico");
		this.getService().updateDataGeracaoCartaRim(idsSinistroDoctoServico);
	}
	
	/**
	* LMS-6144 - M�todo respons�vel por buscar todos os ids dos documentos de servico relacionados com
    *  o id do processo de sinistro passado por par�metro que tenham prejuizo 
    * 
	* @param idProcessoSinistro
	* @return
	*/
	public TypedFlatMap findIdsSinistroDoctoServicoByIdProcessoSinistroComPrejuizo(TypedFlatMap tfm) {
		TypedFlatMap retorno = new TypedFlatMap(); 
		List ids = getService().findIdsSinistroDoctoServicoByIdProcessoSinistroComPrejuizo(tfm);
		
		if(ids.isEmpty()) {
			throw new BusinessException("LMS-22050");
		}
		
		retorno.put("ids", ids);
		
		return retorno;
	}
	
}
