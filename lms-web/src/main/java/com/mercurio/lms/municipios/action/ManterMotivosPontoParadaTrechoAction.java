package com.mercurio.lms.municipios.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.MotivoParada;
import com.mercurio.lms.municipios.model.MotivoParadaPontoTrecho;
import com.mercurio.lms.municipios.model.service.MotivoParadaPontoTrechoService;
import com.mercurio.lms.municipios.model.service.MotivoParadaService;
import com.mercurio.lms.util.JTVigenciaUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterMotivosPontoParadaTrechoAction"
 */

public class ManterMotivosPontoParadaTrechoAction extends CrudAction {
	
	public void setMotivoParadaPontoTrechoService(MotivoParadaPontoTrechoService motivoParadaPontoTrechoService) {
		this.defaultService = motivoParadaPontoTrechoService;
	}
	
	public MotivoParadaPontoTrechoService getMotivoParadaPontoTrechoService() {
		return (MotivoParadaPontoTrechoService)defaultService;
	}

	private MotivoParadaService motivoParadaService;
	
	
    public void removeById(java.lang.Long id) {
        getMotivoParadaPontoTrechoService().removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getMotivoParadaPontoTrechoService().removeByIds(ids);
    }

    public Map findById(java.lang.Long id) {
    	MotivoParadaPontoTrecho motivoParadaPontoTrecho = (MotivoParadaPontoTrecho)getMotivoParadaPontoTrechoService().findById(id);
    	MotivoParada motivoParada = motivoParadaPontoTrecho.getMotivoParada();

    	TypedFlatMap retorno = new TypedFlatMap();
    	retorno.put("idMotivoParadaPontoTrecho",motivoParadaPontoTrecho.getIdMotivoParadaPontoTrecho());
    	retorno.put("motivoParada.idMotivoParada",motivoParada.getIdMotivoParada());
    	retorno.put("dtVigenciaInicial",motivoParadaPontoTrecho.getDtVigenciaInicial());
    	retorno.put("dtVigenciaFinal",motivoParadaPontoTrecho.getDtVigenciaFinal());

    	retorno.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(motivoParadaPontoTrecho));
    	
    	return retorno;
    }

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Serializable store(Map bean) {
    	MotivoParadaPontoTrecho motivoParadaPontoTrecho = new MotivoParadaPontoTrecho();

        ReflectionUtils.copyNestedBean(motivoParadaPontoTrecho,bean);

        getMotivoParadaPontoTrechoService().store(motivoParadaPontoTrecho);
        TypedFlatMap retorno = new TypedFlatMap();
        retorno.put("idMotivoParadaPontoTrecho",motivoParadaPontoTrecho.getIdMotivoParadaPontoTrecho());
        retorno.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(motivoParadaPontoTrecho));
        return retorno;
    }
    
    public List findMotivoParadaCombo(Map criteria) {
    	return getMotivoParadaService().find(criteria);
    }

	public MotivoParadaService getMotivoParadaService() {
		return motivoParadaService;
	}

	public void setMotivoParadaService(MotivoParadaService motivoParadaService) {
		this.motivoParadaService = motivoParadaService;
	}

}
