package com.mercurio.lms.expedicao.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ContatoCorrespondencia;
import com.mercurio.lms.expedicao.model.HistoricoAwb;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HistoricoAwbDAO extends BaseCrudDao<HistoricoAwb, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return HistoricoAwb.class;
    }

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("dhInclusao.value"), "dhInclusao");
		pl.add(Projections.property("idHistoricoAwb"), "idHistoricoAwb");
		pl.add(Projections.property("blGerarMensagem"), "blGerarMensagem");
		pl.add(Projections.property("dsHistoricoAwb"), "dsHistoricoAwb");
		
		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(pl);
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(Projections.rowCount());
		
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	/**
	 * findEmailsContadosByFiliaisDestinoAWB 
	 * @author Andre Valadas
	 * 
	 * @param Long idPreAlerta
	 * @return List<Contato>
	 */
	public List findEmailsContatosByFiliaisDestinoAWB(Long idAwb) {

		/** SubSelect para Buscar Filial */
		DetachedCriteria dcFilialAwb = DetachedCriteria.forClass(getPersistentClass(), "ha")
			.setProjection(Projections.property("p.id"))
			.createAlias("ha.awb", "awb")
			.createAlias("awb.aeroportoByIdAeroportoDestino", "ad")
			.createAlias("ad.filial", "f")
			.createAlias("f.pessoa", "p")
			.add(Restrictions.eq("awb.id", idAwb));
		List subSelect = getAdsmHibernateTemplate().findByDetachedCriteria(dcFilialAwb);
		
		if (subSelect.isEmpty()) {
			return subSelect;
		}

    	DetachedCriteria dc = DetachedCriteria.forClass(ContatoCorrespondencia.class, "cc")
    		.setProjection(Projections.property("c.dsEmail"))
			.createAlias("cc.contato", "c")
			.createAlias("c.pessoa", "pes")
			.add(Restrictions.eq("cc.tpCorrespondencia", "P"))
			.add(Restrictions.isNotNull("c.dsEmail"))
			.add(Property.forName("pes.id").in(subSelect));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuario", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	/*
	 * Metodos privados
	 */
	private DetachedCriteria createCriteriaPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ha");
		dc.add(Restrictions.eq("ha.awb.id", criteria.getLong("awb.idAwb")));
		dc.addOrder(Order.desc("ha.dhInclusao.value"));
		return dc;
	}
   


}