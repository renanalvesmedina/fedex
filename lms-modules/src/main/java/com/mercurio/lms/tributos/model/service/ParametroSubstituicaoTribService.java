package com.mercurio.lms.tributos.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.ParametroSubstituicaoTrib;
import com.mercurio.lms.tributos.model.dao.ParametroSubstituicaoTribDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.parametroSubstituicaoTribService"
 */
public class ParametroSubstituicaoTribService extends CrudService<ParametroSubstituicaoTrib, Long> {


	/**
	 * Recupera uma instância de <code>Object</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ParametroSubstituicaoTrib findById(java.lang.Long id) {
        return (ParametroSubstituicaoTrib)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * Método invocado antes do store 
     */
	public ParametroSubstituicaoTrib beforeStore(ParametroSubstituicaoTrib bean) {
		ParametroSubstituicaoTrib pst = (ParametroSubstituicaoTrib) bean;
		
		/** Busca registros na base com intervalos de vigência iguais */
		List lst = getParametroSubstituicaoTribDAO().findParametroSubstituicaoTribByVigenciaEquals(pst.getDtVigenciaInicial()
																	, pst.getDtVigenciaFinal()
																		, pst.getUnidadeFederativa().getIdUnidadeFederativa()
																			, pst.getIdParametroSubstituicaoTrib());
		
		/** Verifica se não já não existe nenhum registro na base com o mesmo intervalo de vigência */
		if(!lst.isEmpty()){
			throw new BusinessException("LMS-00047");
		}
		 
		return super.beforeStore(bean);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ParametroSubstituicaoTrib bean) {
        return super.store(bean);
    }
    
    public ParametroSubstituicaoTrib findVigenteByUf(Long idUf, YearMonthDay dtVigencia){
    	return getParametroSubstituicaoTribDAO().findVigenteByUf(idUf, dtVigencia);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setParametroSubstituicaoTribDAO(ParametroSubstituicaoTribDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ParametroSubstituicaoTribDAO getParametroSubstituicaoTribDAO() {
        return (ParametroSubstituicaoTribDAO) getDao();
    }
    
    /**
	 * Método responsável por buscar ParametroSubstituicaoTrib que estejam no mesmo intervalo de vigência
	 * 
	 * @param vigenciaInicial
	 * @param vigenciaFinal
	 * @return List 
	 */
    public List findParametroSubstituicaoTribByVigenciaEquals(YearMonthDay dtInicial
    			, YearMonthDay dtFinal
    				, Long idUnidadeFederativa
    						, Long idParametroSubstituicaoTrib){
    	return findParametroSubstituicaoTribByVigenciaEquals(dtInicial
    				, dtFinal
    					, idUnidadeFederativa
    						, idParametroSubstituicaoTrib);
    }
}