package com.mercurio.lms.rnc.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.CausaNaoConformidade;
import com.mercurio.lms.rnc.model.dao.CausaNaoConformidadeDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.rnc.causaNaoConformidadeService"
 */
public class CausaNaoConformidadeService extends CrudService<CausaNaoConformidade, Long> {


	/**
	 * Recupera uma instância de <code>CausaNaoConformidade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public CausaNaoConformidade findById(java.lang.Long id) {
        return (CausaNaoConformidade)super.findById(id);
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
    public java.io.Serializable store(CausaNaoConformidade bean) {
        return super.store(bean);
    }

    /**
     * Busca List com os Motivos Disposição relacionados com as 'Ocorrencia Não Conformidade'
     * 
     * @param criteria List de ids de 'Ocorrencia Não Conformidade'
     * @return List com os Motivos Disposição
     */
	public List findCausasNaoConformidadeByOcorrenciaNC(List criteria) {
		return this.getCausaNaoConformidadeDAO().findCausasNaoConformidadeByOcorrenciaNC(criteria);
	}
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setCausaNaoConformidadeDAO(CausaNaoConformidadeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private CausaNaoConformidadeDAO getCausaNaoConformidadeDAO() {
        return (CausaNaoConformidadeDAO) getDao();
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
    	return getCausaNaoConformidadeDAO().findListByCriteria(criteria, campoOrdenacao);
    }
    
	/**
	 * Solicitação CQPRO00005285 da integração.
	 * Método que retorna uma instancia da classe CausaNaoConformidadeconforme de acordo com a descrição passada por parâmetro.
	 * @param dsCausaNaoConformidade
	 * @return
	 */
	public CausaNaoConformidade findByDsCausaNaoConformidade(String dsCausaNaoConformidade){
		return getCausaNaoConformidadeDAO().findByDsCausaNaoConformidade(dsCausaNaoConformidade);
	}

}