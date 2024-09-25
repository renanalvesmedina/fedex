package com.mercurio.lms.fretecarreteiroviagem.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteiroviagem.model.ValorCombustivel;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.ValorCombustivelDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteiroviagem.valorCombustivelService"
 */
public class ValorCombustivelService extends CrudService<ValorCombustivel, Long> {

	private VigenciaService vigenciaService;

	/**
	 * Recupera uma instância de <code>ValorCombustivel</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ValorCombustivel findById(Long id) {
        return (ValorCombustivel)super.findById(id);
    }
    public List findById(TypedFlatMap typedFlatMap) {
    	return getValorCombustivelDAO().findById(typedFlatMap);
    }

    protected void beforeRemoveById(Long id) {
    	ValorCombustivel bean = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(bean);    	
    	super.beforeRemoveById(id);
    }
    
    protected void beforeRemoveByIds(List ids) {
        ValorCombustivel bean = null;
    	for(int x = 0; x < ids.size(); x++) {
    		bean = findById((Long)ids.get(x));
        	JTVigenciaUtils.validaVigenciaRemocao(bean);
    	}
    	super.beforeRemoveByIds(ids);
    }
    protected ValorCombustivel beforeStore(ValorCombustivel bean) {
    	ValorCombustivel valorCombustivel = (ValorCombustivel)bean;
    	if (getValorCombustivelDAO().findBeforeStoreVigencia(valorCombustivel).size() > 0)
    		throw new BusinessException("LMS-00003");
    	return super.beforeStore(bean);
    }
    public void removeById(Long id) {
        super.removeById(id);
    }
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	return getValorCombustivelDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
    }
    public Integer getRowCount(TypedFlatMap criteria) {
    	return getValorCombustivelDAO().getRowCount(criteria);
    }
    public List findValorCombustivelByDtVigenciaTpCombustivelMoeda(Long idMoedaPais,Long tpCombustivel, YearMonthDay dtVigenciaInicial) {
    	return getValorCombustivelDAO().findValorCombustivelByDtVigenciaTpCombustivelMoeda(idMoedaPais, tpCombustivel,dtVigenciaInicial);
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
    public java.io.Serializable store(ValorCombustivel bean) {
    	vigenciaService.validaVigenciaBeforeStore(bean);
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setValorCombustivelDAO(ValorCombustivelDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ValorCombustivelDAO getValorCombustivelDAO() {
        return (ValorCombustivelDAO) getDao();
    }
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
   }