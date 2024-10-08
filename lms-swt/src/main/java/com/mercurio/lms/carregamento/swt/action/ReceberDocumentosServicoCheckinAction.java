package com.mercurio.lms.carregamento.swt.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.OrdemFilialFluxoService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.swt.receberDocumentosServicoCheckinAction"
 */

public class ReceberDocumentosServicoCheckinAction extends CrudAction {	
	
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private DoctoServicoService doctoServicoService;
	private RotaIdaVoltaService rotaIdaVoltaService;
	private DomainValueService domainValueService;
    private ConhecimentoService conhecimentoService;
    private CtoInternacionalService ctoInternacionalService;
    private MdaService mdaService;
    private FilialService filialService;
    private ManifestoService manifestoService;
    private ConversaoMoedaService conversaoMoedaService;
    private OrdemFilialFluxoService ordemFilialFluxoService;    

    /*
     * Setters and Getters...
     */
	
	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
	}
	public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}
	public RotaIdaVoltaService getRotaIdaVoltaService() {
		return rotaIdaVoltaService;
	}
	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}
    private MdaService getMdaService() {
		return mdaService;
	}

	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}

	private ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	private CtoInternacionalService getCtoInternacionalService() {
		return ctoInternacionalService;
	}

	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	private FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public ManifestoService getManifestoService() {
		return manifestoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	private DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
    public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}		
	public void setOrdemFilialFluxoService(OrdemFilialFluxoService ordemFilialFluxoService) {
		this.ordemFilialFluxoService = ordemFilialFluxoService;
	}
	
	public void setService(DoctoServicoService doctoServicoService) {
		setDefaultService( doctoServicoService );
	}	
    public void removeById(java.lang.Long id) {
        getDoctoServicoService().removeById(id);
    }    

    
    /*
     * Regras basicas...
     */

	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
		getDoctoServicoService().removeByIds(ids);
    }

    /**
     * Retorna um doctoServico para a tela de details, levando em consideracao
     * o id do manifesto da listagem.
     * 
     * @param criteria
     * @return
     */
    public TypedFlatMap findById(TypedFlatMap criteria) { 
    	Long idDoctoServico = criteria.getLong("idDoctoServico");
    	Long idManifesto = criteria.getLong("idManifesto");
    	
    	TypedFlatMap result = this.getDoctoServicoService().findDoctoServicoCheckin(idDoctoServico, idManifesto);
    	result.put("tpDocumentoServico.value", result.get("tpDoctoServico"));
    	result.put("idFilial", result.get("idFilialDoctoServico"));
    	result.put("sgFilial", result.get("sgFilialDoctoServico"));
    	result.put("update", Boolean.TRUE);
    	
    	if (result.getLong("idRotaIdaVolta") != null) {
    		List list = this.getDoctoServicoService().findSubQueryHrSaida(result.getLong("idRotaIdaVolta"));
      	  
	      	if (!list.isEmpty()){
	      		result.put("hrSaida",list.get(0));
	      	}
    	}
    	return result;
    }    
    
    public ResultSetPage findPaginatedCheckin(TypedFlatMap criteria) {
    	
    	String tpDocumentoServico = criteria.getString("doctoServico.tpDocumentoServico");
    	
    	Long idFilialOrigem = criteria.getLong("filialByIdFilialOrigem.idFilial");
    	
    	Long idManifesto = criteria.getLong("manifesto.idManifesto");
    	Long idRotaIdaVolta = criteria.getLong("rotaIdaVolta.idRotaIdaVolta");
    	
    	ResultSetPage resultSetPage = this.getDoctoServicoService().
		findPaginatedDoctoServicoByIdManifestoAndIdRotaIdaVolta(getIdDoctoServico(criteria, tpDocumentoServico), idFilialOrigem, tpDocumentoServico, idManifesto, 
				idRotaIdaVolta, FindDefinition.createFindDefinition(criteria));
    	
    	return resultSetPage; 
    }
    
	private Long getIdDoctoServico(TypedFlatMap criteria, String tpDocumentoServico) {
    	Long idDoctoServico = null;
		if (StringUtils.isNotBlank(tpDocumentoServico)){
        	if (tpDocumentoServico.equals("CTR") || tpDocumentoServico.equals("CTE") || tpDocumentoServico.equals("NTE")) {
        		idDoctoServico = criteria.getLong("conhecimento.idDoctoServico");
        	} else if (tpDocumentoServico.equals("CRT")) {
            	idDoctoServico = criteria.getLong("ctoInternacional.idDoctoServico");
        	} else if (tpDocumentoServico.equals("MDA")) {
        		idDoctoServico = criteria.getLong("c.idDoctoServico");
        	}
    	}
		return idDoctoServico;
	}
    
    public Integer getRowCountCheckin(TypedFlatMap criteria) {
    	
    	String tpDocumentoServico = criteria.getString("doctoServico.tpDocumentoServico");
    	
    	Long idFilialOrigem = criteria.getLong("filialByIdFilialOrigem.idFilial");
    	
    	Long idManifesto = criteria.getLong("manifesto.idManifesto");
    	Long idRotaIdaVolta = criteria.getLong("rotaIdaVolta.idRotaIdaVolta");
    	
    	return this.getDoctoServicoService().
    			getRowCountDoctoServicoByIdManifestoAndIdRotaIdaVolta(getIdDoctoServico(criteria, tpDocumentoServico), idFilialOrigem, tpDocumentoServico, idManifesto,idRotaIdaVolta);
    }
    
    /**
     * Realiza a soma dos valores da grid.
     * 
     * @param criteria
     * @return
     */
    public TypedFlatMap findSumDoctoServicoByIdManifestoAndIdRotaIdaVolta(TypedFlatMap criteria) {
    	
    	String tpDocumentoServico = criteria.getString("doctoServico.tpDocumentoServico");
    	
    	Long idFilialOrigem = criteria.getLong("filialByIdFilialOrigem.idFilial");
    	
    	Long idManifesto = criteria.getLong("manifesto.idManifesto");
    	Long idRotaIdaVolta = criteria.getLong("rotaIdaVolta.idRotaIdaVolta");
    	
    	List result = this.getDoctoServicoService().
    		findSumDoctoServicoByIdManifestoAndIdRotaIdaVolta(getIdDoctoServico(criteria, tpDocumentoServico), idFilialOrigem, tpDocumentoServico, idManifesto,idRotaIdaVolta);
    	
    	TypedFlatMap valoresSomados = new TypedFlatMap();
    	
    	BigDecimal volumeTotal = new BigDecimal(0);
    	BigDecimal pesoTotal = new BigDecimal(0);
    	BigDecimal valorTotal = new BigDecimal(0);
    	
    	if (result.size()>0) {
    		for (Iterator iter = result.iterator(); iter.hasNext();) {
				Map row = (Map) iter.next();
				
				Long idMoeda = (Long) row.get("idMoeda");
				
				if (!idMoeda.equals(SessionUtils.getMoedaSessao().getIdMoeda())) {
					valorTotal = valorTotal.add(this.getConversaoMoedaService().findConversaoMoeda( 
						(Long)row.get("idPais"),
						(Long)row.get("idMoeda"),
						SessionUtils.getPaisSessao().getIdPais(),
						SessionUtils.getMoedaSessao().getIdMoeda(),
						JTDateTimeUtils.getDataAtual(),
						(BigDecimal)row.get("vlMercadoriaTotal")));
				} else {
					if (row.get("vlMercadoriaTotal")!=null){
						valorTotal = valorTotal.add(new BigDecimal(String.valueOf(row.get("vlMercadoriaTotal"))));
					}
				}
				if (row.get("psRealTotal")!=null){
					pesoTotal = pesoTotal.add(new BigDecimal(String.valueOf(row.get("psRealTotal"))));
				}
				if (row.get("qtVolumesTotal")!=null){
					volumeTotal = volumeTotal.add(new BigDecimal(String.valueOf(row.get("qtVolumesTotal"))));
				}
			}
    		
    	}
		valoresSomados.put("sgMoeda", SessionUtils.getMoedaSessao().getSgMoeda());
		valoresSomados.put("dsSimbolo", SessionUtils.getMoedaSessao().getDsSimbolo());
		
		valoresSomados.put("valorTotal", valorTotal);
		valoresSomados.put("pesoTotal", pesoTotal);
		valoresSomados.put("volumeTotal", volumeTotal);
    	
    	return valoresSomados;
    }
    
    /**
     * Valida o doctoServico...
     * 
     * @param criteria
     * @return
     */
    public TypedFlatMap validateDoctoServico(TypedFlatMap criteria) {    	
     	this.getDoctoServicoService().validateCaptureDoctoServico(criteria.getLong("idDoctoServico"));
     	return findById(criteria);
    }
    
    /**
     * Faz a chamada para a service de doctoServico que contem as regras de 
     * validacao da tela.
     * 
     * @param criteria ids vindos da tela...
     * 
     * @return
     */
    public void validateDocumentosServicoCheckin(TypedFlatMap criteria) {
   
    	this.getDoctoServicoService().validateDocumentosServicoCheckin(
    			criteria.getLong("idDoctoServico"),
    			criteria.getLong("idManifesto"),
    			criteria.getLong("idRotaIdaVolta"));
    }
    
    /**
     * Faz a chamada para a service de doctoServico que contem as regras de 
     * persistencia da tela.
     * 
     * @param criteria ids vindos da tela...
     * 
     * @return
     */
    public void storeDocumentosServicoCheckin(TypedFlatMap criteria) {    	
    	this.getDoctoServicoService().storeDocumentosServicoCheckin(
    			criteria.getLong("idDoctoServico"),
    			criteria.getLong("idManifesto"),
    			criteria.getLong("idRotaIdaVolta"),
    			criteria.getBoolean("update") == null ? false : criteria.getBoolean("update").booleanValue());
    }
    
    /*
     * Comportamento para os objetos...
     */
    
    //################ Inicio Tag Documents ################

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
        List retorno = getDomainValueService().findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
        return retorno;
    }
    
    /**
     * Busca a filial baseado no documento de servi�o
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

    
    // FIND'S LOOKUP'S PARA FILIAL DO TIPO DE DOCTO DE SERVICO ESCOLHIDO
    public List findLookupServiceDocumentFilialCTR(Map criteria) {
        return findLookupFilialByDocumentoServico(criteria);
    }

     public List findLookupServiceDocumentFilialCRT(Map criteria) {
        return findLookupFilialByDocumentoServico(criteria);
    }
     
     public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	 return findLookupFilialByDocumentoServico(criteria);
     }
     
     
     //findLookup's para a tag documento de servi�o 
     public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	 return getConhecimentoService().findLookup(criteria);
     }

     public List findLookupServiceDocumentNumberCRT(Map criteria) {
    	 return getCtoInternacionalService().findLookup(criteria);
     }
    
     public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	 return getMdaService().findLookup(criteria);
     }
    
     // ################ Fim Tag Documents ################
    
      public List findLookupRotaIdaVolta(Map criteria) {
    	  Integer nrRota = (Integer)criteria.get("nrRota");
    	  List<RotaIdaVolta> list = this.getRotaIdaVoltaService().findRotaIdaVoltaInVigencia(nrRota, JTDateTimeUtils.getDataAtual());
    	  List listAux = new ArrayList();
    	  for (RotaIdaVolta rotaIdaVolta : list) {
    		  Map map = new HashMap();
    		  map.put("idRotaIdaVolta", rotaIdaVolta.getIdRotaIdaVolta());
    		  map.put("nrRota", rotaIdaVolta.getNrRota());    		  
    		  map.put("dsRota", rotaIdaVolta.getRota() == null ? null : rotaIdaVolta.getRota().getDsRota());
    		  listAux.add(map);
    	  }    	  
    	  return listAux;
      }
      
      public List findLookupPreManifesto(Map criteria) {
    	Map filialOrigem = new HashMap();
    	filialOrigem.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
    	criteria.put("filialByIdFilialOrigem", filialOrigem);
    	
		List listResult = this.getManifestoService().findLookup(criteria);		
		List listPreManifesto = new ArrayList();
		for (Iterator iter = listResult.iterator(); iter.hasNext();) {
			Manifesto manifesto = (Manifesto) iter.next();
			if (manifesto.getDhEmissaoManifesto() == null) {
				Map mapPreManifesto = new HashMap();			
				mapPreManifesto.put("idManifesto", manifesto.getIdManifesto());
				mapPreManifesto.put("nrPreManifesto", manifesto.getNrPreManifesto());
				mapPreManifesto.put("tpManifesto", manifesto.getTpManifesto().getValue());			
				listPreManifesto.add(mapPreManifesto);				
			}
		}			
		return listPreManifesto;
      }
      
      public List findLookupBySgFilial(Map criteria){
    	  List listResult = filialService.findLookupBySgFilial((String)criteria.get("sgFilial"), null);
	
    	  List listFilial = new ArrayList();
    	  for (Iterator iter = listResult.iterator(); iter.hasNext();) {
    		  Map mapResult = (Map) iter.next();
    		  Map mapFilial = new HashMap();
			
    		  mapFilial.put("idFilial", mapResult.get("idFilial"));
    		  mapFilial.put("sgFilial", mapResult.get("sgFilial"));
    		  Map pessoa = (Map)mapResult.get("pessoa");
    		  mapFilial.put("nmFantasia", pessoa.get("nmFantasia"));
			
    		  listFilial.add(mapFilial);
    	  }		
    	  return listFilial;
	}
     
     /*
      * Regras de negocio...
      */
      
      /**
       * M�todo que busca um manifesto a partir da rotaIdaVolta informada. 
       * @param Map criteria
       */
      public Map findManifestoRota(Map criteria) {
    	  Long idFilialOrigem =  SessionUtils.getFilialSessao().getIdFilial();
    	  Long idRotaIdaVolta = (Long)criteria.get("idRotaIdaVolta");
    	  Long idDoctoServico = (Long)criteria.get("idDoctoServico");
    	  
    	  DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
    	  Long idFluxoFilial = doctoServico.getFluxoFilial().getIdFluxoFilial();
    	  List ListOrdemFilialFluxo = ordemFilialFluxoService.findByIdFluxoFilial(idFluxoFilial);
    	  
    	  Integer nrOrdem = null;
    	  for (Iterator iterator = ListOrdemFilialFluxo.iterator(); iterator.hasNext();) {
    		  OrdemFilialFluxo ordemFilialFluxo = (OrdemFilialFluxo) iterator.next();
    		  if (ordemFilialFluxo.getFilial().getIdFilial().equals(idFilialOrigem)) {
    			  nrOrdem = ordemFilialFluxo.getNrOrdem().intValue();
    			  break;
    		  }			
    	  }
    	  nrOrdem = Integer.valueOf(nrOrdem + 1);
    	  OrdemFilialFluxo ordemFilialFluxo = ordemFilialFluxoService.findOrdemFilialFluxoByIdFluxoFilialByNrOrdem(idFluxoFilial, nrOrdem.byteValue());
    	      	  
    	  List results = this.getManifestoService().findPreManifestosByFilialOrigemByFilialDestinoByRotaIdaVolta(idFilialOrigem, ordemFilialFluxo.getFilial().getIdFilial(), idRotaIdaVolta);
    	  
    	  Map result = new HashMap();
    	  if (results.size() > 0) {
    		  Manifesto manifesto = (Manifesto) results.get(0);
    		  result.put("idFilial", manifesto.getFilialByIdFilialOrigem().getIdFilial());
    		  result.put("sgFilial", manifesto.getFilialByIdFilialOrigem().getSgFilial());
    		  result.put("idManifesto", manifesto.getIdManifesto());
    		  result.put("nrPreManifesto", manifesto.getNrPreManifesto());
    	  }
    	  
    	  return result;
      }
      
      /**
       * Busca informa��es basicas necessarias para a tela.
       * 
       * @param criteria
       * @return
       */
      public TypedFlatMap getBasicData(TypedFlatMap criteria) {
    	  
    	  TypedFlatMap result = new TypedFlatMap();
    	  
    	  result.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
    	  result.put("sgFilial", SessionUtils.getFilialSessao().getSgFilial());
    	  result.put("nmFantasiaFilial", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());
    	  
    	  return result;
      }
      
      /**
       * Busca informa��es basicas necessarias para a tela.
       * 
       * @param criteria
       * @return
       */
      public Map findHrSaidaRota(Map criteria) {    	  
    	  List result = this.getDoctoServicoService().findSubQueryHrSaida((Long)criteria.get("idRotaIdaVolta"));
			
    	  Map element = new HashMap();    	  
    	  if (!result.isEmpty()){
				element.put("hrSaida",result.get(0));
    	  }
    	  
    	  return element;
      }
}
