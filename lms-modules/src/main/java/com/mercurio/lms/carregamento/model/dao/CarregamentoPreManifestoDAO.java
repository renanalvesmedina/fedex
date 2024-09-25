package com.mercurio.lms.carregamento.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.CarregamentoPreManifesto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CarregamentoPreManifestoDAO extends BaseCrudDao<CarregamentoPreManifesto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CarregamentoPreManifesto.class;
    }

    /**
     * Solicitação da Integração - CQPRO00005520
     * Criar um método na classe CarregamentoPreManifestoService que retorne uma instancia da classe CarregamentoPreManifesto conforme os parametros especificados.
     * Nome do método: findCarregamentoPreManifesto(long idManifesto, long idCarregamentoDescarga ) : CarregamentoPreManifesto
     * @param idManifesto
     * @param idCarregamentoDescarga
     * @return
     */
    public CarregamentoPreManifesto findCarregamentoPreManifesto(Long idManifesto, Long idCarregamentoDescarga){
   		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(CarregamentoPreManifesto.class.getName() + " cpm");
   		hql.addCriteria("cpm.manifesto.id", "=", idManifesto);
   		hql.addCriteria("cpm.carregamentoDescarga.id", "=", idCarregamentoDescarga);
   		return (CarregamentoPreManifesto)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

    public CarregamentoPreManifesto findByCarregamentoAndDestino(Long idCarregamentoDescarga, Long idFilialDestino, String tpModal){
   		DetachedCriteria dc = DetachedCriteria.forClass(CarregamentoPreManifesto.class);
   		dc.createAlias("manifesto.filialByIdFilialDestino", "filialByIdFilialDestino");
   		dc.createAlias("manifesto", "mani");
   		dc.setFetchMode("manifesto", FetchMode.JOIN);
   		dc.setFetchMode("filialByIdFilialDestino", FetchMode.JOIN);
   		dc.add(Restrictions.eq("carregamentoDescarga.id", idCarregamentoDescarga));
   		dc.add(Restrictions.eq("filialByIdFilialDestino.id", idFilialDestino));    	    	   
   		dc.add(Restrictions.eq("mani.tpModal", tpModal));
   		return (CarregamentoPreManifesto)getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
	public List<CarregamentoPreManifesto> findByIdCarregamentoDescarga(long idCarregamentoDescarga) {
		StringBuffer sql = new StringBuffer(" from ")
		.append(CarregamentoPreManifesto.class.getName())
		.append(" cpm ")
		.append(" where cpm.carregamentoDescarga.id = ?");
				
		return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idCarregamentoDescarga});
	}
    
}