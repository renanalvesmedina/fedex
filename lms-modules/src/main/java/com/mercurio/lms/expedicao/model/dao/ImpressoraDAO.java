package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.lms.expedicao.model.Impressora;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ImpressoraDAO extends BaseCrudDao<Impressora, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Impressora.class;
	}

	/**
	 * Busca os dados específicos para serem visualizados na combobox.
	 *@author Robson Edemar Gehl
	 * @return
	 */
	public List findCombo(Map map) {
		DetachedCriteria dc = createDetachedCriteria();

		RestrictionsBuilder rb = new RestrictionsBuilder(getPersistentClass(), true);
		rb.createDefaultBuilders(map);
		rb.createCriterions(map, Collections.EMPTY_LIST, dc);

		ProjectionList projections = Projections.projectionList()
			.add(Projections.alias(Projections.property("idImpressora"),"idImpressora"))
			.add(Projections.alias(Projections.property("dsLocalizacao"),"dsLocalizacao"))
			.add(Projections.alias(Projections.property("dsCheckIn"),"dsCheckIn"));

		dc.setProjection(projections);
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		List list = findByDetachedCriteria(dc);
		
		return list;
	}

	public List findListByCriteria(Map criterions, List order) {
		order = new ArrayList(1);
		order.add("dsLocalizacao");
		return super.findListByCriteria(criterions, order);
	}

	protected void initFindByIdLazyProperties(Map arg0) {
		arg0.put("filial", FetchMode.JOIN);
		arg0.put("filial.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map arg0) {
		arg0.put("filial", FetchMode.JOIN);
	}

	public List findImpressoraUsuario(Long idFilial, String dsEnderecoFisico, List<String> tpImpressoras) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select i from Impressora i, ImpressoraComputador ic");
		hql.append(" where i.filial.id = ").append(idFilial);
		hql.append("   and ic.impressora.idImpressora = i.idImpressora");
		if (tpImpressoras != null && tpImpressoras.size() > 0) {
			hql.append("   and ic.impressora.tpImpressora in (:tpImpressoras)");
		}
		hql.append("   and ic.dsMac = '").append(dsEnderecoFisico.toUpperCase()).append("'");

		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "tpImpressoras", tpImpressoras);
		
		return result;
		}

	public List findDispositivoComputadorUsuario(Long idFilial, String dsEnderecoFisico, List<String> tpImpressoras) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select new map(i as impressora, ic as impressoraComputador) from Impressora i, ImpressoraComputador ic");
		hql.append(" where i.filial.id = ").append(idFilial);
		hql.append("   and ic.impressora.idImpressora = i.idImpressora");
		if (tpImpressoras != null && tpImpressoras.size() > 0) {
			hql.append("   and ic.impressora.tpImpressora in (:tpImpressoras)");
		}
		hql.append("   and ic.dsMac = '").append(dsEnderecoFisico.toUpperCase()).append("'");

		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "tpImpressoras", tpImpressoras);
		
		return result;
	}

	public List findValidateIpInformadoImpressora(Long idImpressora, Long nrIp) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select i from Impressora i ");
		hql.append("  where i.nrIp = :nrIp ");
		if(idImpressora != null) {
			hql.append("    and i.idImpressora <> :idImpressora ");
			namedParams.put("idImpressora", idImpressora);
	}
		namedParams.put("nrIp", nrIp);
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), namedParams);

		return result;
	}
}