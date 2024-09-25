package com.mercurio.lms.rnc.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.DescricaoPadraoNc;
import com.mercurio.lms.rnc.model.dao.DescricaoPadraoNcDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.descricaoPadraoNcService"
 */
public class DescricaoPadraoNcService extends CrudService<DescricaoPadraoNc, Long> {


	/**
	 * Recupera uma inst�ncia de <code>DescricaoPadraoNc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public DescricaoPadraoNc findById(java.lang.Long id) {
        return (DescricaoPadraoNc)super.findById(id);
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
    public java.io.Serializable store(DescricaoPadraoNc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setDescricaoPadraoNcDAO(DescricaoPadraoNcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private DescricaoPadraoNcDAO getDescricaoPadraoNcDAO() {
        return (DescricaoPadraoNcDAO) getDao();
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
    	return getDescricaoPadraoNcDAO().findListByCriteria(criteria, campoOrdenacao);
    }
    
    /**
     * Solicita��o CQPRO00004872 da integra��o.
     * M�todo que retorna uma instancia da classe DescricaoPadraoNc conforme o parametro especificado.
     * Esse m�todo retorna uma lista. Caso necessite apenas o primeiro registro, entao deve-se utilizar um
     * get(0) ou outra forma similar no c�digo que estiver chamando este m�todo.
     * @param idMotivoAberturaNc 
     * @param tpSituacao -> Se null, busca todos os registros independente de estarem ativos ou inativos.
     * @return
     */
    public List findDescricaoPadraoNcByIdMotivoAberturaNc(Long idMotivoAberturaNc, String tpSituacao){
    	return getDescricaoPadraoNcDAO().findDescricaoPadraoNcByIdMotivoAberturaNc(idMotivoAberturaNc, tpSituacao);
    }
    
    
}