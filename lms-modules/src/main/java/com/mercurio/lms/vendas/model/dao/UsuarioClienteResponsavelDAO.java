package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.tools.ant.util.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.TrtCliente;
import com.mercurio.lms.vendas.model.UsuarioClienteResponsavel;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class UsuarioClienteResponsavelDAO extends BaseCrudDao<UsuarioClienteResponsavel, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return UsuarioClienteResponsavel.class;
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {	
		DetachedCriteria dc = montaDetachedCriteria(criteria);
		ResultSetPage resultSetPage = getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
		return resultSetPage;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = montaDetachedCriteria(criteria);
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc); 
	}
	
	private DetachedCriteria montaDetachedCriteria(TypedFlatMap criteria){
		ProjectionList pl =  Projections.projectionList();
    	pl.add(Projections.property("p.nrIdentificacao"), "nrIdentificacao");
    	pl.add(Projections.property("p.tpIdentificacao"), "tpIdentificacao");
    	pl.add(Projections.property("p.nmPessoa"), "nmPessoa");    	
    	pl.add(Projections.property("u.idUsuario"), "nmUsuarioResponsavel");    	
    	pl.add(Projections.property("f.sgFilial"), "sgFilial");    	
    	pl.add(Projections.property("ucr.dtVigenciaInicial"), "dtVigenciaInicial");    	
    	pl.add(Projections.property("ucr.dtVigenciaFinal"), "dtVigenciaFinal");
    	pl.add(Projections.property("ucr.idUsuarioClienteResponsavel"), "idUsuarioClienteResponsavel");
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(UsuarioClienteResponsavel.class, "ucr")
    		.setProjection(pl)
    		.createAlias("ucr.usuario", "u")
    		.createAlias("ucr.cliente", "c")
    		.createAlias("c.pessoa", "p")
    		.createAlias("c.filialByIdFilialAtendeComercial", "f");
    	
		if (criteria.getLong("cliente.idCliente") != null){
			dc.add(Restrictions.eq("c.idCliente", criteria.getLong("cliente.idCliente")));
		}
		
		if (criteria.getLong("usuarioResponsavel.idUsuario") != null){
			dc.add(Restrictions.eq("u.idUsuario", criteria.getLong("usuarioResponsavel.idUsuario")));
		}
		
		if (criteria.getYearMonthDay("usuarioResponsavel.dtVigenciaInicial") != null){
			dc.add(Restrictions.ge("ucr.dtVigenciaInicial", criteria.getYearMonthDay("usuarioResponsavel.dtVigenciaInicial")));
		}
		
		if (criteria.getYearMonthDay("usuarioResponsavel.dtVigenciaFinal") != null){
			dc.add(Restrictions.le("ucr.dtVigenciaFinal", criteria.getYearMonthDay("usuarioResponsavel.dtVigenciaFinal")));
		} 		
		
    	dc.addOrder(Order.asc("ucr.dtVigenciaInicial"));
    	dc.addOrder(Order.asc("p.nmPessoa"));
    	
    	return dc;
	}

	public List<UsuarioClienteResponsavel> findUsuariosResponsaveisByCliente(TypedFlatMap typedflatmap) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ucr")
				.add(Restrictions.eq("ucr.cliente.id", typedflatmap.getLong("idCliente")))
				.add(Restrictions.le("ucr.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()))
				.add(Restrictions.or(						 
							Restrictions.ge("ucr.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()),
							Restrictions.isNull("ucr.dtVigenciaFinal")
						));
		
		return (List<UsuarioClienteResponsavel>)getAdsmHibernateTemplate().findByCriteria(dc);
	}

	public List<UsuarioClienteResponsavel> findByUsuarioByCliente(TypedFlatMap typedflatmap) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ucr")
				.add(Restrictions.eq("ucr.cliente.id", typedflatmap.getLong("cliente.idCliente")))
				.add(Restrictions.eq("ucr.usuario.id", typedflatmap.getLong("usuarioResponsavel.idUsuario")));		
		return (List<UsuarioClienteResponsavel>)getAdsmHibernateTemplate().findByCriteria(dc);
	}
}