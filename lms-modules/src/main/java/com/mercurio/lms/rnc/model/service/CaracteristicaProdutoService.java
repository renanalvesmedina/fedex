package com.mercurio.lms.rnc.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.CaracteristicaProduto;
import com.mercurio.lms.rnc.model.dao.CaracteristicaProdutoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.caracteristicaProdutoService"
 */
public class CaracteristicaProdutoService extends CrudService<CaracteristicaProduto, Long> {


	/**
	 * Recupera uma inst�ncia de <code>CaracteristicaProduto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public CaracteristicaProduto findById(java.lang.Long id) {
        return (CaracteristicaProduto)super.findById(id);
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
    public java.io.Serializable store(CaracteristicaProduto bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setCaracteristicaProdutoDAO(CaracteristicaProdutoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private CaracteristicaProdutoDAO getCaracteristicaProdutoDAO() {
        return (CaracteristicaProdutoDAO) getDao();
    }
    
    /**
     * Localiza uma lista de resultados a partir dos crit�rios de busca 
     * informados. Permite especificar regras de ordena��o.
     * 
     * @param criterions Crit�rios de busca.
     * @param lista com criterios de ordena��o. Deve ser uma java.lang.String no formato
     * 	<code>nomePropriedade:asc</code> ou <code>associacao_.nomePropriedade:desc</code>.
     * A utiliza��o de <code>asc</code> ou <code>desc</code> � opcional sendo o padr�o <code>asc</code>.
     * @return Lista de resultados sem pagina��o.
     */ 
    public List findListByCriteria(Map criteria, List campoOrdenacao) {
    	return getCaracteristicaProdutoDAO().findListByCriteria(criteria, campoOrdenacao);
    }
}