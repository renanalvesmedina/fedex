package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.PerifericoRastreador;
import com.mercurio.lms.contratacaoveiculos.model.dao.PerifericoRastreadorDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.PerifericoRastreadorService"
 */
public class PerifericoRastreadorService extends CrudService<PerifericoRastreador, Long> {
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setPerifericoRastreadorDAO(PerifericoRastreadorDAO dao) {
        setDao( dao );
    }
    
    public java.io.Serializable store(PerifericoRastreador perifericoRastreador) {
    	return super.store(perifericoRastreador);
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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
