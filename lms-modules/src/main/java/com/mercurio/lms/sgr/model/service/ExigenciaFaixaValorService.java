package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;
import com.mercurio.lms.sgr.model.dao.ExigenciaFaixaValorDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sgr.exigenciaFaixaValorService"
 */
public class ExigenciaFaixaValorService extends CrudService<ExigenciaFaixaValor, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ExigenciaFaixaValor</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public ExigenciaFaixaValor findById(java.lang.Long id) {
        return (ExigenciaFaixaValor)super.findById(id);
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
	 * Apaga uma entidade atrav�s do IdEnquadramentoRegra
	 *
	 * @param id indica o enquadramentoRegra
	 */
    public void removeByIdFaixaDeValor(java.lang.Long id) {
        Map criteria = new HashMap();
 		criteria.put("faixaDeValor.idFaixaDeValor", id);		
    	List exigenciaFaixadeValor = find(criteria);
    	if (exigenciaFaixadeValor != null && exigenciaFaixadeValor.size() >0) {
    		List ids = new ArrayList();
    		for (Iterator iterator = exigenciaFaixadeValor.iterator(); iterator.hasNext();) {
    			ExigenciaFaixaValor exigenciaFaixaValor = (ExigenciaFaixaValor) iterator.next();
    			ids.add(exigenciaFaixaValor.getIdExigenciaFaixaValor());
    	}
     		removeByIds(ids);    		
    }
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ExigenciaFaixaValor bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setExigenciaFaixaValorDAO(ExigenciaFaixaValorDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ExigenciaFaixaValorDAO getExigenciaFaixaValorDAO() {
        return (ExigenciaFaixaValorDAO) getDao();
    }

    /**
     * 
     * @param idFaixaDeValor
     * @return List
     */
    public List findByFaixaDeValor(Long idFaixaDeValor) {
    	return getExigenciaFaixaValorDAO().findByFaixaDeValor(idFaixaDeValor);
    }
}