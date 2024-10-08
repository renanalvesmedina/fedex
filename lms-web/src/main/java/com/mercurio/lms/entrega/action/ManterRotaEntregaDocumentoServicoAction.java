package com.mercurio.lms.entrega.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.service.AtualizarRotaEntregaDoctoServicoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.entrega.manterRotaEntregaDocumentoServicoAction"
 */
public class ManterRotaEntregaDocumentoServicoAction extends CrudAction {
		private AtualizarRotaEntregaDoctoServicoService atualizarRotaEntregaDoctoServicoService;
		private FilialService filialService;
		private DomainValueService domainValueService;
		private RotaColetaEntregaService rotaColetaEntregaService;
		private DoctoServicoService doctoServicoService;
	
	public void confirmarAlteracaoRota(TypedFlatMap criteria) {
		Long idDoctoServico = criteria.getLong("idDoctoServico");
		Long idFilialOperacional = criteria.getLong("idFilialOper");
		Long idFilialDestino = criteria.getLong("idFilialDest");
		Long idFilial = criteria.getLong("filial.idFilial");
		Long idRotaColetaNova = criteria.getLong("rotaColetaEntrega.idRotaColetaEntrega");
		atualizarRotaEntregaDoctoServicoService.storeAlteracaoRota(idDoctoServico, idFilial, idRotaColetaNova, idFilialOperacional, idFilialDestino);
		}

	public TypedFlatMap findFilialUsuarioLogado() {
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		Filial filial = SessionUtils.getFilialSessao();
		typedFlatMap.put("filial.sgFilial", filial.getSgFilial());
		typedFlatMap.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		typedFlatMap.put("filial.idFilial", filial.getIdFilial());
		return typedFlatMap;
		}

	public List findLookupFilial(Map criteria) {
		return filialService.findLookup(criteria);
		}
	
	public List findLookupFilialByDocumentoServico(Map criteria) {
		List list = filialService.findLookup(criteria);
		List retorno = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Filial filial = (Filial) iter.next();
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("idFilial", filial.getIdFilial());
			typedFlatMap.put("sgFilial", filial.getSgFilial());
			typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
			retorno.add(typedFlatMap);
		}
		return retorno;
		}
		
	public List findLookupRotaEntrega(TypedFlatMap criteria) {
		return rotaColetaEntregaService.findLookup(criteria);
		}
		
	public List findLookupServiceDocumentFilialCRT(Map criteria) {
			return findLookupFilialByDocumentoServico(criteria);
		}
		
	public List findLookupServiceDocumentFilialCTE(Map criteria) {
		   	return findLookupFilialByDocumentoServico(criteria);
		 }

	public List findLookupServiceDocumentFilialCTR(Map criteria) {
		   	return findLookupFilialByDocumentoServico(criteria);
		 }

		public List findLookupServiceDocumentFilialMDA(Map criteria) {
		   	return findLookupFilialByDocumentoServico(criteria);
		}
		
		public List findLookupServiceDocumentFilialNFS(Map criteria) {
		   	return findLookupFilialByDocumentoServico(criteria);
		}
		
	public List findLookupServiceDocumentFilialNTE(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
		    	}

	public List findLookupServiceDocumentFilialRRE(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
		  }

	public List findLookupServiceDocumentNumberCRT(TypedFlatMap criteria) {
			 criteria.put("blBloqueado","N");
		criteria.put("nrDoctoServico", criteria.get("nrCrt"));
			 List lista = doctoServicoService.findLookupCustomRotaColetaEnt(criteria);
			 if(!lista.isEmpty()){
			lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
			TypedFlatMap map = (TypedFlatMap) lista.get(0);
			if (map.get("nrDoctoServico") != null) {
				map.put("nrCrt", map.get("nrDoctoServico"));
			}
				 if(map.get("nrRota")!= null){
				if (map.get("dsRota") != null) {
					map.put("nrRota", map.get("nrRota").toString().concat(" - ").concat(map.getString("dsRota")));
				 }
			 }
		}
			 return lista;
		 }
		
	public List findLookupServiceDocumentNumberCTE(TypedFlatMap criteria) {
		criteria.put("nrDoctoServico", criteria.get("nrConhecimento"));
			 criteria.put("blBloqueado","N");
			 List lista = doctoServicoService.findLookupCustomRotaColetaEnt(criteria);
			 if(!lista.isEmpty()){
				 lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
				 TypedFlatMap map = (TypedFlatMap)lista.get(0);
			if (map.get("nrDoctoServico") != null) {
				map.put("nrConhecimento", map.getLong("nrDoctoServico"));
			}
				 if(map.get("nrRota")!= null){
				if (map.get("dsRota") != null) {
						 map.put("nrRota",map.get("nrRota").toString().concat(" - ").concat(map.getString("dsRota")));
				 }
			 }	 
		}
			 return lista;
		 }
		 
		 public List findLookupServiceDocumentNumberCTR(TypedFlatMap criteria) {
			 criteria.put("nrDoctoServico",criteria.get("nrConhecimento"));
			 criteria.put("blBloqueado","N");
			 List lista = doctoServicoService.findLookupCustomRotaColetaEnt(criteria);
			 if(!lista.isEmpty()){
				 lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
				 TypedFlatMap map = (TypedFlatMap)lista.get(0);
			if (map.get("nrDoctoServico") != null) {
					 map.put("nrConhecimento",map.getLong("nrDoctoServico"));
			}
			if (map.get("nrRota") != null) {
				if (map.get("dsRota") != null) {
					map.put("nrRota", map.get("nrRota").toString().concat(" - ").concat(map.getString("dsRota")));
				}
			}
		}
		return lista;
	}
				 
	public List findLookupServiceDocumentNumberMDA(TypedFlatMap criteria) {
		criteria.put("blBloqueado", "N");
		List lista = doctoServicoService.findLookupCustomRotaColetaEnt(criteria);
		if (!lista.isEmpty()) {
			lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
			TypedFlatMap map = (TypedFlatMap) lista.get(0);
			if (map.get("nrDoctoServico") != null) {
				map.put("nrDoctoServico", map.get("nrDoctoServico"));
			}
				 if(map.get("nrRota")!= null){
				if (map.get("dsRota") != null) {
						 map.put("nrRota",map.get("nrRota").toString().concat(" - ").concat(map.getString("dsRota")));
				 }
			}
		}
		return lista;
	}
					 
	public List findLookupServiceDocumentNumberNFT(TypedFlatMap criteria) {
		criteria.put("blBloqueado", "N");
		criteria.put("nrDoctoServico", criteria.get("nrNotaFiscalServico"));
		List lista = doctoServicoService.findLookupCustomRotaColetaEnt(criteria);
		if (!lista.isEmpty()) {
			Map map = (Map) lista.get(0);
			if (map.get("nrRota") != null) {
				if (map.get("dsRota") != null) {
					map.put("nrRota", map.get("nrRota").toString().concat(" - ").concat(map.get("dsRota").toString()));
			 }	 
			}
		}
			 return lista;
		 }

	public List findLookupServiceDocumentNumberNTE(TypedFlatMap criteria) {
		criteria.put("nrDoctoServico", criteria.get("nrConhecimento"));
			    criteria.put("blBloqueado","N");
			    List lista = doctoServicoService.findLookupCustomRotaColetaEnt(criteria);
			    if(!lista.isEmpty()){
					 lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
					 TypedFlatMap map = (TypedFlatMap)lista.get(0);
			if (map.get("nrDoctoServico") != null) {
				map.put("nrConhecimento", map.getLong("nrDoctoServico"));
			}
					 if(map.get("nrRota")!= null){
				if (map.get("dsRota") != null) {
							 map.put("nrRota",map.get("nrRota").toString().concat(" - ").concat(map.getString("dsRota")));
					 }
				 }	 
		}
				return lista;
		 }

	public List findLookupServiceDocumentNumberRRE(TypedFlatMap criteria) {
			    criteria.put("blBloqueado","N");
		criteria.put("nrDoctoServico", criteria.get("nrReciboReembolso"));
			    List lista = doctoServicoService.findLookupCustomRotaColetaEnt(criteria);
			    if(!lista.isEmpty()){
					 lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
					 TypedFlatMap map = (TypedFlatMap)lista.get(0);
			if (map.getLong("nrDoctoServico") != null) {
				map.put("nrReciboReembolso", map.getLong("nrDoctoServico"));
			}
					 if(map.get("nrRota")!= null){
				if (map.get("dsRota") != null) {
							 map.put("nrRota",map.get("nrRota").toString().concat(" - ").concat(map.getString("dsRota")));
					 }
				 }	 
		}
				return lista;
		 }
		 
		 public TypedFlatMap findRetornoPopPupDoctoServico(TypedFlatMap criteria){
				List lista = doctoServicoService.findLookupCustomRotaColetaEnt(criteria);
				TypedFlatMap map = null;
				if(!lista.isEmpty()){
					 lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
					 map = (TypedFlatMap)lista.get(0);
					 map.put("idDoctoServico",map.getLong("doctoServico.idDoctoServico"));
					 if(map.get("nrRota")!= null){
				if (map.get("dsRota") != null) {
							 map.put("nrRota",map.get("nrRota").toString().concat(" - ").concat(map.getString("dsRota")));
					 }
				}		
		}
				return map;
			}

		 public List findTipoDocumentoServico(Map criteria) {
		    	List dominiosValidos = new ArrayList();
		    	dominiosValidos.add("CTR");
		    	dominiosValidos.add("CRT");
		dominiosValidos.add("MDA");
		dominiosValidos.add("CTE");
		dominiosValidos.add("NTE");
		    	dominiosValidos.add("NFT");
		    	List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO",dominiosValidos);
		    	return retorno;
		  }
		 
	public void setAtualizarRotaEntregaDoctoServicoService(AtualizarRotaEntregaDoctoServicoService atualizarRotaEntregaDoctoServicoService) {
		this.atualizarRotaEntregaDoctoServicoService = atualizarRotaEntregaDoctoServicoService;
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

	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
}
