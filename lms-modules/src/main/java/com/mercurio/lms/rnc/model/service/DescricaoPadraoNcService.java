package com.mercurio.lms.rnc.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.DescricaoPadraoNc;
import com.mercurio.lms.rnc.model.dao.DescricaoPadraoNcDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.rnc.descricaoPadraoNcService"
 */
public class DescricaoPadraoNcService extends CrudService<DescricaoPadraoNc, Long> {


	/**
	 * Recupera uma instância de <code>DescricaoPadraoNc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public DescricaoPadraoNc findById(java.lang.Long id) {
        return (DescricaoPadraoNc)super.findById(id);
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
    public java.io.Serializable store(DescricaoPadraoNc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDescricaoPadraoNcDAO(DescricaoPadraoNcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DescricaoPadraoNcDAO getDescricaoPadraoNcDAO() {
        return (DescricaoPadraoNcDAO) getDao();
    }
    
 
    /**
     * Localiza uma lista de resultados a partir dos critérios de busca 
     * informados. Permite especificar regras de ordenação.
     * 
     * @param criterions Critérios de busca.
     * @param lista com criterios de ordenação. Deve ser uma java.lang.String no formato
     * 	<code>nomePropriedade:asc</code> ou <code>associacao_.nomePropriedade:desc</code>.
     * A utilização de <code>asc</code> ou <code>desc</code> é opcional sendo o padrão <code>asc</code>.
     * @return Lista de resultados sem paginação.
     */ 
    public List findListByCriteria(Map criteria, List campoOrdenacao) {
    	return getDescricaoPadraoNcDAO().findListByCriteria(criteria, campoOrdenacao);
    }
    
    /**
     * Solicitação CQPRO00004872 da integração.
     * Método que retorna uma instancia da classe DescricaoPadraoNc conforme o parametro especificado.
     * Esse método retorna uma lista. Caso necessite apenas o primeiro registro, entao deve-se utilizar um
     * get(0) ou outra forma similar no código que estiver chamando este método.
     * @param idMotivoAberturaNc 
     * @param tpSituacao -> Se null, busca todos os registros independente de estarem ativos ou inativos.
     * @return
     */
    public List findDescricaoPadraoNcByIdMotivoAberturaNc(Long idMotivoAberturaNc, String tpSituacao){
    	return getDescricaoPadraoNcDAO().findDescricaoPadraoNcByIdMotivoAberturaNc(idMotivoAberturaNc, tpSituacao);
    }
    
    
}