package com.mercurio.lms.recepcaodescarga.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.MdaService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.recepcaodescarga.descarregarVeiculoDocumentosAction"
 */
public class DescarregarVeiculoDocumentosAction extends CrudAction {
	private DomainValueService domainValueService;
	private FilialService filialService;
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private DoctoServicoService doctoServicoService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private ReciboReembolsoService reciboReembolsoService;
	private MdaService mdaService;


	/**
	 * Busca a Service default desta Action
	 * 
	 * @param carregamentoDescargaService
	 */
	public void setService(CarregamentoDescargaService carregamentoDescargaService) {
		this.defaultService = carregamentoDescargaService;
	}

	public CarregamentoDescargaService getService() {
		return (CarregamentoDescargaService) this.defaultService;
	}
	
	public CarregamentoDescarga findById(java.lang.Long id) {
		return ((CarregamentoDescargaService) defaultService).findById(id);
	}


	/**
	 * Busca a quantidade de dados da grid de carregamentos
	 */
	public Integer getRowCount(TypedFlatMap criteria) {
		Long idManifesto = criteria.getLong("manifesto.idManifesto");
		String tpDoctoServico = criteria.getString("doctoServico.tpDocumentoServico");
		Long idFilialOrigem = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial");
		Long idDoctoServico = criteria.getLong("doctoServico.idDoctoServico");
		String notaFiscal = criteria.getString("notaFiscal");
		
		return this.getDoctoServicoService().getRowCountDoctoServicoByIdManifesto(idManifesto, tpDoctoServico, idFilialOrigem, idDoctoServico, notaFiscal);
	}	

	/**
	 * Busca os registros da grid de carregamento.
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {		
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		Long idManifesto = criteria.getLong("manifesto.idManifesto");
		String tpDoctoServico = criteria.getString("doctoServico.tpDocumentoServico");
		Long idFilialOrigem = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial");
		Long idDoctoServico = criteria.getLong("doctoServico.idDoctoServico");
		String notaFiscal = criteria.getString("notaFiscal");
		
		ResultSetPage rsp = this.getDoctoServicoService().findPaginatedDoctoServicoByIdManifesto(idManifesto, tpDoctoServico, idFilialOrigem, idDoctoServico, notaFiscal, findDefinition);
		List listDoctoServico = rsp.getList();
		List retorno = new ArrayList();
		for (Iterator iter = listDoctoServico.iterator(); iter.hasNext();) {
			DoctoServico doctoServico = (DoctoServico) iter.next();
			retorno.add(loadMapDoctoServico(doctoServico));
		}
		rsp.setList(retorno);
		return rsp;	
	}
	
	/**
	 * Popula um TypedFlatMap a partir de um DoctoServico e retorna o mapa.
	 * 
	 * @param doctoServico
	 * @return
	 */
	private TypedFlatMap loadMapDoctoServico(DoctoServico doctoServico) {
		TypedFlatMap mapDoctoServico = new TypedFlatMap();
		
		if (doctoServico.getServico()!=null) {
			mapDoctoServico.put("sgServico", doctoServico.getServico().getSgServico());
			mapDoctoServico.put("dsServico", doctoServico.getServico().getDsServico());
		}
		if (doctoServico.getFilialByIdFilialDestino() !=null)
			mapDoctoServico.put("sgFilialDestino", doctoServico.getFilialByIdFilialDestino().getSgFilial());
		if (doctoServico.getClienteByIdClienteRemetente() !=null)
			mapDoctoServico.put("clienteRemetente", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
		if (doctoServico.getClienteByIdClienteDestinatario() !=null)
			mapDoctoServico.put("clienteDestinatario", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
		
		mapDoctoServico.put("nrDoctoServico", doctoServico.getNrDoctoServico());
		mapDoctoServico.put("tpDocumento", doctoServico.getTpDocumentoServico());
		mapDoctoServico.put("sgFilialOrigem", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
		mapDoctoServico.put("qtVolumes", doctoServico.getQtVolumes());
		mapDoctoServico.put("psReal", doctoServico.getPsReal());
		mapDoctoServico.put("sgMoeda", doctoServico.getMoeda().getSgMoeda());
		mapDoctoServico.put("dsSimboloMoeda", doctoServico.getMoeda().getDsSimbolo());
		mapDoctoServico.put("vlMercadoria", doctoServico.getVlMercadoria());
		mapDoctoServico.put("sgMoeda2", doctoServico.getMoeda().getSgMoeda());
		mapDoctoServico.put("dsSimboloMoeda2", doctoServico.getMoeda().getDsSimbolo());		
		mapDoctoServico.put("vlTotalDocServico", doctoServico.getVlTotalDocServico());
		mapDoctoServico.put("dtPrevEntrega", doctoServico.getDtPrevEntrega());
		
		return mapDoctoServico;
	}	

	/**
	 * Busca os tipos de documento serviço.
	 * @param criteria
	 * @return
	 */
    public List findTipoDocumentoServico(TypedFlatMap criteria) {
    	List dominiosValidos = new ArrayList();
        dominiosValidos.add("CTR");
        dominiosValidos.add("CRT");
        dominiosValidos.add("MDA");
        dominiosValidos.add("RRE");
        dominiosValidos.add("NFT"); 	
        dominiosValidos.add("NTE");
        dominiosValidos.add("CTE");
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
    public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialRRE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNFT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCTE(Map criteria) {
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
    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	return this.getMdaService().findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberRRE(Map criteria) {
    	return this.getReciboReembolsoService().findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	return this.getConhecimentoService().findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNTE(Map criteria) {
    	return this.getConhecimentoService().findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCTE(Map criteria) {
    	return this.getConhecimentoService().findLookup(criteria);
    }
    
    
    
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}	
	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
	}
	public void setPreManifestoDocumentoService(
			PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}	
	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
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

}
