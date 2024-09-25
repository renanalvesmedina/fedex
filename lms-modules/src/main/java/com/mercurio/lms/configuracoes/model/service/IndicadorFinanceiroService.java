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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.indicadorFinanceiroService"
 */
public class IndicadorFinanceiroService extends CrudService<IndicadorFinanceiro, Long> {

	/**
	 * Recupera uma instância de <code>IndicadorFinanceiro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    /**
     * Retorna uma coleção de Indicadores Financeiros relacionado a um Pais.
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
	 * Recupera uma instância de <code>IndicadorFinanceiro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public IndicadorFinanceiro findById(java.lang.Long id) {
        return (IndicadorFinanceiro)super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(IndicadorFinanceiro bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setIndicadorFinanceiroDAO(IndicadorFinanceiroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private IndicadorFinanceiroDAO getIndicadorFinanceiroDAO() {
        return (IndicadorFinanceiroDAO) getDao();
    }
   }