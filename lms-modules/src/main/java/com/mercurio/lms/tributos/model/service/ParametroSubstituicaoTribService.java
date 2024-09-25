package com.mercurio.lms.tributos.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.ParametroSubstituicaoTrib;
import com.mercurio.lms.tributos.model.dao.ParametroSubstituicaoTribDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.parametroSubstituicaoTribService"
 */
public class ParametroSubstituicaoTribService extends CrudService<ParametroSubstituicaoTrib, Long> {


	/**
	 * Recupera uma inst�ncia de <code>Object</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ParametroSubstituicaoTrib findById(java.lang.Long id) {
        return (ParametroSubstituicaoTrib)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * M�todo invocado antes do store 
     */
	public ParametroSubstituicaoTrib beforeStore(ParametroSubstituicaoTrib bean) {
		ParametroSubstituicaoTrib pst = (ParametroSubstituicaoTrib) bean;
		
		/** Busca registros na base com intervalos de vig�ncia iguais */
		List lst = getParametroSubstituicaoTribDAO().findParametroSubstituicaoTribByVigenciaEquals(pst.getDtVigenciaInicial()
																	, pst.getDtVigenciaFinal()
																		, pst.getUnidadeFederativa().getIdUnidadeFederativa()
																			, pst.getIdParametroSubstituicaoTrib());
		
		/** Verifica se n�o j� n�o existe nenhum registro na base com o mesmo intervalo de vig�ncia */
		if(!lst.isEmpty()){
			throw new BusinessException("LMS-00047");
		}
		 
		return super.beforeStore(bean);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
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
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setParametroSubstituicaoTribDAO(ParametroSubstituicaoTribDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ParametroSubstituicaoTribDAO getParametroSubstituicaoTribDAO() {
        return (ParametroSubstituicaoTribDAO) getDao();
    }
    
    /**
	 * M�todo respons�vel por buscar ParametroSubstituicaoTrib que estejam no mesmo intervalo de vig�ncia
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