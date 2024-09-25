package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.PerifericoRastreador;
import com.mercurio.lms.contratacaoveiculos.model.dao.PerifericoRastreadorDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.PerifericoRastreadorService"
 */
public class PerifericoRastreadorService extends CrudService<PerifericoRastreador, Long> {
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPerifericoRastreadorDAO(PerifericoRastreadorDAO dao) {
        setDao( dao );
    }
    
    public java.io.Serializable store(PerifericoRastreador perifericoRastreador) {
    	return super.store(perifericoRastreador);
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PerifericoRastreadorDAO getPerifericoRastreadorDAO() {
        return (PerifericoRastreadorDAO) getDao();
    }
    
    public PerifericoRastreador findById(Long id) {
    	return (PerifericoRastreador)super.findById(id);
    }
    
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	@SuppressWarnings("unchecked")
	public List<PerifericoRastreador> findPerifericosRastreador(Long idExigenciaGerRisco) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT pegr.perifericoRastreador ")
				.append("FROM ExigenciaGerRisco egr ")
				.append("JOIN egr.perifericosRastreador pegr ")
				.append("WHERE egr.idExigenciaGerRisco = ? ")
				.append("ORDER BY pegr.perifericoRastreador.dsPerifericoRastreador ");
		return getDao().getAdsmHibernateTemplate().find(hql.toString(), idExigenciaGerRisco);
	}
    
}
