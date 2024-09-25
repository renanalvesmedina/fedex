package com.mercurio.lms.rnc.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.rnc.registrarFilialResponsavelNaoConformidadeAction"
 */

public class RegistrarFilialResponsavelNaoConformidadeAction {

	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private FilialService filialService;

	
	public OcorrenciaNaoConformidadeService getOcorrenciaNaoConformidadeService() {
		return ocorrenciaNaoConformidadeService;
	}
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	
	public List<TypedFlatMap> findFilialResponsavelNC(TypedFlatMap criteria) {
		List<TypedFlatMap> result = new ArrayList<TypedFlatMap>();
		
		Long idManifesto = criteria.getLong("idManifesto");
		Long idControleCarga = criteria.getLong("idControleCarga");
		if (idManifesto != null || idControleCarga != null) {
			List<Filial> filiais = getOcorrenciaNaoConformidadeService().findFilialResponsavelNC(idManifesto,idControleCarga);
			if (filiais.size() > 1) {
				Collections.sort(filiais, new FilialComparator());
	            }
		
			for (Filial filial : filiais) {
			TypedFlatMap map = new TypedFlatMap();
			map.put("idFilial", filial.getIdFilial());
			map.put("sgFilial", filial.getSgFilial());
				result.add(map);
		}
	}
		return result;
	}


    public TypedFlatMap findByIdFilial(Long id) {
    	Filial filial = getFilialService().findById(id);
    	TypedFlatMap map = new TypedFlatMap();
    	map.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
    	return map;
    }

	private class FilialComparator implements Comparator<Filial> {
		public int compare(Filial o1, Filial o2) {
			return o1.getSgFilial().compareTo(o2.getSgFilial());
		}
	}
}