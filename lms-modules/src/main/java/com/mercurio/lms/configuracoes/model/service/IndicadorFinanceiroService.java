package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.IndicadorFinanceiro;
import com.mercurio.lms.configuracoes.model.dao.IndicadorFinanceiroDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.indicadorFinanceiroService"
 */
public class IndicadorFinanceiroService extends CrudService<IndicadorFinanceiro, Long> {

	/**
	 * Recupera uma inst�ncia de <code>IndicadorFinanceiro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    /**
     * Retorna uma cole��o de Indicadores Financeiros relacionado a um Pais.
     * @param criterions
     * @return
     */
    public List findIndicadoresFinanceirosByPaisCombo(Map criterions){
    	Long idPais = Long.valueOf((String)ReflectionUtils.getNestedBeanPropertyValue(criterions,"pais.idPais"));  	
    	List list = getIndicadorFinanceiroDAO().findIndicadoresFinanceirosByPais(idPais);
    	return list;
    }
    
    public List findIndicadoresFinanceirosByPais(TypedFlatMap tfm){
    	
    	Long idPais = tfm.getLong("pais.idPais");
    	Long idIndicadorFinanceiroInativo = tfm.getLong("idIndicadorFinanceiro");
    	String tpSituacao = tfm.getString("tpSituacao");
    	
    	return getIndicadorFinanceiroDAO().findIndicadoresFinanceirosByPais(idPais, idIndicadorFinanceiroInativo, tpSituacao);
    }

	/**
	 * Recupera uma inst�ncia de <code>IndicadorFinanceiro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public IndicadorFinanceiro findById(java.lang.Long id) {
        return (IndicadorFinanceiro)super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(IndicadorFinanceiro bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setIndicadorFinanceiroDAO(IndicadorFinanceiroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private IndicadorFinanceiroDAO getIndicadorFinanceiroDAO() {
        return (IndicadorFinanceiroDAO) getDao();
    }
   }