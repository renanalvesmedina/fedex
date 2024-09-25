package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParcelaPrecoDAO extends BaseCrudDao<ParcelaPreco, Long> {

	@Override
	public List findListByCriteria(Map criterions) {
		List<String> listaOrder = new ArrayList<String>();
		listaOrder.add("nmParcelaPreco:asc");
		return super.findListByCriteria(criterions,listaOrder);
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("servicoAdicional", FetchMode.JOIN);
		lazyFindById.put("unidadeMedida", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return ParcelaPreco.class;
	}

	public ParcelaPreco findByCdParcelaPreco(String cdParcelaPreco) {		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "p")
			.add(Restrictions.ilike("p.cdParcelaPreco", cdParcelaPreco));
		return (ParcelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public Map<String, Object> findParcelaByCdParcelaPreco(String cdParcelaPreco) {		
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("p.idParcelaPreco"), "idParcelaPreco")
			.add(Projections.property("p.nmParcelaPreco"), "nmParcelaPreco")
			.add(Projections.property("p.servicoAdicional.idServicoAdicional"), "idServicoAdicional");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "p")
			.setProjection(pl)
			.add(Restrictions.ilike("p.cdParcelaPreco", cdParcelaPreco))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return l.get(0);
		return null;
	}

	public List findParcelaByTpParcelaPreco(Long idTabelaPreco, String tpParcelaPreco) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("pp.idParcelaPreco"), "idParcelaPreco")
			.add(Projections.property("pp.nmParcelaPreco"), "nmParcelaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPrecoParcela.class, "tpp")
			.setProjection(pl)
			.createAlias("tpp.parcelaPreco", "pp")
			.createAlias("tpp.tabelaPreco", "tp")
			.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco))
			.add(Restrictions.eq("pp.tpParcelaPreco", tpParcelaPreco))
			.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	public ParcelaPreco findByIdServicoAdicional(Long idServicoAdicional) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("p.idParcelaPreco"), "idParcelaPreco")
			.add(Projections.property("p.nmParcelaPreco"), "nmParcelaPreco")
			.add(Projections.property("p.dsParcelaPreco"), "dsParcelaPreco")
			.add(Projections.property("p.cdParcelaPreco"), "cdParcelaPreco");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "p")
			.setProjection(pl)
			.add(Restrictions.eq("p.servicoAdicional.id", idServicoAdicional))
			.setResultTransformer(new AliasToBeanResultTransformer(ParcelaPreco.class));
		List<ParcelaPreco> l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return l.get(0);
		return null;
	}

	public List findGeneralidadesExcluindoAlgunsTipos(Long idTabelaPreco, Long[] idsAExcluir) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("pp.idParcelaPreco"), "idParcelaPreco")
			.add(Projections.property("pp.cdParcelaPreco"), "cdParcelaPreco")
			.add(Projections.property("pp.nmParcelaPreco"), "nmParcelaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPrecoParcela.class, "tpp")
			.setProjection(pl)
			.createAlias("tpp.parcelaPreco", "pp")
			.createAlias("tpp.tabelaPreco", "tp")
			.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco))
			.add(Restrictions.eq("pp.tpParcelaPreco", "G"));

		for(int i=0;i<idsAExcluir.length;i++) {
			dc.add(Restrictions.ne("pp.idParcelaPreco", idsAExcluir[i]));
		}
		dc.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()));
		dc.setResultTransformer(new AliasToBeanResultTransformer(ParcelaPreco.class));

		return findByDetachedCriteria(dc);
	}

	public List findServicosAdicionaisParcela() {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("p.idParcelaPreco"), "idParcelaPreco")
			.add(Projections.property("s.idServicoAdicional"), "idServicoAdicional")
			.add(Projections.property("p.dsParcelaPreco"), "dsServicoAdicional")
			.add(Projections.property("p.cdParcelaPreco"), "cdParcelaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "p")
			.createAlias("p.servicoAdicional", "s")
			.setProjection(pl)
			.add(Restrictions.eq("p.tpParcelaPreco", "S"))
			.add(Restrictions.eq("p.tpSituacao", "A"))
			.add(Restrictions.ne("p.cdParcelaPreco", ConstantesExpedicao.CD_SEGURANCA_ADICIONAL))
			.add(Restrictions.ne("p.cdParcelaPreco", ConstantesExpedicao.CD_SEGURO_CARGA_PERMANENCIA))
			.add(Restrictions.and(Restrictions.le("s.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()),
					(Restrictions.ge("s.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()))))
			.addOrder(OrderVarcharI18n.asc("p.dsParcelaPreco", LocaleContextHolder.getLocale()))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	public List findParcelasAtivas() {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("p.idParcelaPreco"), "idParcelaPreco")
			.add(Projections.property("p.nmParcelaPreco"), "nmParcelaPreco")
			.add(Projections.property("p.tpParcelaPreco"), "tpParcelaPreco")
			.add(Projections.property("p.tpIndicadorCalculo"), "tpIndicadorCalculo")
			.add(Projections.property("p.tpPrecificacao"), "tpPrecificacao");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "p")
			.setProjection(pl)
			.add(Restrictions.eq("p.tpSituacao", "A"))
			.addOrder(OrderVarcharI18n.asc("p.nmParcelaPreco", LocaleContextHolder.getLocale()))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	public List<Map<String, Object>> findServicosAdicionaisFrete(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(pp.cdParcelaPreco as cdParcelaPreco, "+PropertyVarcharI18nProjection.createProjection("pp.nmParcelaPreco")+" as nmParcelaPreco, " +
				"sadc.qtDias as qtDias, " +
				"sadc.qtColetas as qtColetas, " +
				"sadc.qtPaletes as qtPaletes, " +
				"sadc.nrKmRodado as nrKmRodado, " +
				"sadc.qtSegurancasAdicionais as qtSegurancasAdicionais, " +
				"sadc.vlMercadoria as vlMercadoria, " +
				"sadc.qtCheques as qtCheques, " +
				"sadc.dtPrimeiroCheque as dtPrimeiroCheque)");

		hql.addFrom(ParcelaPreco.class.getName()+" pp " +
				"join pp.servicoAdicional sa " +
				"join sa.servAdicionalDocServs sadc " );

		hql.addCriteria("sadc.doctoServico.id","=", idDoctoServico);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public ParcelaPreco findParcelaPrecoByDescParcela(String nmParcelaPreco) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(ParcelaPreco.class.getName());
		hql.addCriteria("i18n(nmParcelaPreco)", "=", nmParcelaPreco);
		List result = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		if(result!=null){
			return (ParcelaPreco)result.get(0);
		}
		return null ;
	}
	
	@SuppressWarnings("unchecked")
	public List<ParcelaPreco> findParcelaPrecoByIdTabelaPrecoParaMarkup(Long idTabelaPreco, String tpPrecificacao){
		StringBuilder query = new StringBuilder()
		.append(" SELECT distinct pp ")
		.append(" FROM ").append(TabelaPrecoParcela.class.getName()).append(" tpp ")
		.append(" JOIN tpp.tabelaPreco tp ")
		.append(" JOIN tpp.parcelaPreco pp ")
		.append(" WHERE	tp.idTabelaPreco = :idTabelaPreco ");
		if (StringUtils.isNotBlank(tpPrecificacao)) {
			query.append(" AND pp.tpPrecificacao = :tpPrecificacao ");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTabelaPreco", idTabelaPreco);
		params.put("tpPrecificacao", tpPrecificacao);
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
	
}