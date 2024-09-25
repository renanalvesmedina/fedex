package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ParcelaDoctoServicoDAO extends BaseCrudDao<ParcelaDoctoServico, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<ParcelaDoctoServico> getPersistentClass() {
		return ParcelaDoctoServico.class;
	}

	public List<Long> findIdsByIdDoctoServico(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder()
		.append("select pojo.idParcelaDoctoServico ")
		.append("from ").append(ParcelaDoctoServico.class.getName())
		.append(" as  pojo ")
		.append("join pojo.doctoServico as ds ")
		.append("where ds.idDoctoServico = :idDoctoServico");

		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idDoctoServico", idDoctoServico);
	}
	
	public BigDecimal findSumVlParcelasByIdDoctoServico(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder()
		.append("select sum(nvl(pojo.vlParcela, 0)) ")
		.append("from ").append(ParcelaDoctoServico.class.getName())
		.append(" as  pojo ")
		.append("join pojo.doctoServico as ds ")
		.append("join pojo.parcelaPreco as pp ")
		.append("where ds.idDoctoServico = :idDoctoServico")
		.append("  and pp.id in (21,22,25,29,33,56,57,58,59)");

		List list = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idDoctoServico", idDoctoServico);
		BigDecimal vlParcelas = new BigDecimal(0);
		if(list != null && list.size() > 0) {
			vlParcelas = (BigDecimal) list.get(0);
		}
		return vlParcelas;
	}
	

	public Map<Long, BigDecimal> findVlParcelasByIdDoctoServicoEParcelaPreco(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder()
		.append("select new Map(pp.id as id, pojo.vlParcela as vlParcela) ")
		.append("from ").append(ParcelaDoctoServico.class.getName())
		.append(" as  pojo ")
		.append("join pojo.doctoServico as ds ")
		.append("join pojo.parcelaPreco as pp ")
		.append("where ds.idDoctoServico = :idDoctoServico")
		.append("  and pp.id in (36,38,16,24,8,23,51,12)");

		List<Map> list = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idDoctoServico", idDoctoServico);
		Map<Long, BigDecimal> vlParcelas = new HashMap<Long, BigDecimal>();
		if(list != null && list.size() > 0) {
			for (Map map : list) {
				vlParcelas.put((Long) map.get("id"), (BigDecimal) map.get("vlParcela"));
			}
		}
		return vlParcelas;
	}
	
	public List<ParcelaDoctoServico> findByIdDoctoServico(Long idDoctoServico){
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("id"), "idParcelaDoctoServico")
		.add(Projections.property("vlParcela"), "vlParcela")
		.add(Projections.property("pdspp.id"), "parcelaPreco.idParcelaPreco")
		.add(Projections.property("pdspp.nmParcelaPreco"), "parcelaPreco.nmParcelaPreco")
		.add(Projections.property("pdspp.tpParcelaPreco"), "parcelaPreco.tpParcelaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pds")
		.setProjection(pl)
		.createAlias("pds.parcelaPreco", "pdspp")
		.add(Restrictions.eq("pds.doctoServico.id", idDoctoServico))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));

		return findByDetachedCriteria(dc);
	}

	public void removeByIdDoctoServico(Long id, Boolean isFlushSession){
		StringBuilder hql = new StringBuilder()
		.append("delete	").append(getPersistentClass().getName()).append("\n")
		.append("where	doctoServico = :id");

		DoctoServico doctoServico = new CtoInternacional();
		doctoServico.setIdDoctoServico(id);

		getAdsmHibernateTemplate().removeById(hql.toString(), doctoServico);

		if(isFlushSession.booleanValue()) 
			getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}

	public List<ParcelaDoctoServico> findParcelasFrete(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pds");

		dc.createAlias("pds.parcelaPreco", "pp");

		dc.add(Restrictions.eq("pds.doctoServico.id", idDoctoServico));
		dc.add(Restrictions.ne("pp.tpParcelaPreco", ConstantesExpedicao.TP_SERVICO_ADICIONAL));

		return findByDetachedCriteria(dc);
	}

}