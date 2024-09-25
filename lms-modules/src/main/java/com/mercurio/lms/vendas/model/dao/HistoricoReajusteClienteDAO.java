package com.mercurio.lms.vendas.model.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.vendas.model.HistoricoReajusteCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;

public class HistoricoReajusteClienteDAO extends BaseCrudDao<HistoricoReajusteCliente, Long> {

	@Override
	protected Class getPersistentClass() {
		return HistoricoReajusteCliente.class;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition def) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("h.id"), "idHistoricoReajusteCliente")
			.add(Projections.property("h.dtReajuste"), "dtReajuste")
			.add(Projections.property("h.pcReajuste"), "pcReajuste")
			.add(Projections.property("h.tpFormaReajuste"), "tpFormaReajuste")
			.add(Projections.property("tdc.id"), "tabelaDivisaoCliente_idTabelaDivisaoCliente")

			.add(Projections.property("tpa.id"), "tabelaPrecoAnterior_idTabelaPreco")
			.add(Projections.property("ttpa.nrVersao"), "tabelaPrecoAnterior_tipoTabelaPreco_nrVersao")
			.add(Projections.property("ttpa.tpTipoTabelaPreco"), "tabelaPrecoAnterior_tipoTabelaPreco_tpTipoTabelaPreco")
			.add(Projections.property("stpa.tpSubtipoTabelaPreco"), "tabelaPrecoAnterior_subtipoTabelaPreco_tpSubtipoTabelaPreco")

			.add(Projections.property("tpn.id"), "tabelaPrecoNova_idTabelaPreco")
			.add(Projections.property("ttpn.nrVersao"), "tabelaPrecoNova_tipoTabelaPreco_nrVersao")
			.add(Projections.property("ttpn.tpTipoTabelaPreco"), "tabelaPrecoNova_tipoTabelaPreco_tpTipoTabelaPreco")
			.add(Projections.property("stpn.tpSubtipoTabelaPreco"), "tabelaPrecoNova_subtipoTabelaPreco_tpSubtipoTabelaPreco");

		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(pl);
		dc.addOrder(Order.desc("h.dtReajuste"));
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}
	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = this.createCriteriaPaginated(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	private DetachedCriteria createCriteriaPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "h")
			.createAlias("h.tabelaDivisaoCliente", "tdc")
			.createAlias("h.tabelaPrecoAnterior", "tpa")
			.createAlias("h.tabelaPrecoNova", "tpn")
			.createAlias("tpa.tipoTabelaPreco", "ttpa")
			.createAlias("tpn.tipoTabelaPreco", "ttpn")
			.createAlias("tpa.subtipoTabelaPreco", "stpa")
			.createAlias("tpn.subtipoTabelaPreco", "stpn");

		Long idHistoricoReajusteCliente = criteria.getLong("idHistoricoReajusteCliente");
		if (idHistoricoReajusteCliente != null) {
			dc.add(Restrictions.eq("h.id", idHistoricoReajusteCliente));
		}
		Long idTabelaDivisaoCliente = criteria.getLong("idTabelaDivisaoCliente");
		if (idTabelaDivisaoCliente != null) {
			dc.add(Restrictions.eq("tdc.id", idTabelaDivisaoCliente));
		}
		Long idTabelaPrecoAnterior = criteria.getLong("idTabelaPrecoAnterior");
		if (idTabelaPrecoAnterior != null) {
			dc.add(Restrictions.eq("tpa.id", idTabelaPrecoAnterior));
		}
		Long idTabelaPrecoNova = criteria.getLong("idTabelaPrecoNova");
		if (idTabelaPrecoNova != null) {
			dc.add(Restrictions.eq("tpn.id", idTabelaPrecoNova));
		}
		String tpFormaReajuste = criteria.getString("tpFormaReajuste");
		if (StringUtils.isNotBlank(tpFormaReajuste)) {
			dc.add(Restrictions.eq("h.tpFormaReajuste", tpFormaReajuste));
		}		
		return dc;
	}

	public Serializable store(HistoricoReajusteCliente HistoricoReajusteCliente) {
		super.store(HistoricoReajusteCliente);
		return HistoricoReajusteCliente.getIdHistoricoReajusteCliente();
	}
	
	public List<HistoricoReajusteCliente> findHistoricoReajusteClienteByIdDivisaoCliente(Long idDivisaoCliente) {
		StringBuilder from = new StringBuilder();
		
		from.append(HistoricoReajusteCliente.class.getSimpleName()).append(" hrc ");
		from.append("  join fetch hrc.tabelaDivisaoCliente ").append(" tdc ");
		from.append("  join fetch tdc.divisaoCliente ").append(" dc ");
		from.append("  join fetch hrc.tabelaPrecoAnterior ").append(" tpa ");
		from.append("  join fetch tpa.tipoTabelaPreco").append(" ttp ");
		from.append("  join fetch tdc.tabelaPreco ").append(" tp ");
		from.append("  join fetch tp.subtipoTabelaPreco").append(" stp ");
		
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(from.toString());
		hql.addCustomCriteria("hrc.dtReajuste =("+loadMaxDtReajusteHistoricoReajusteCliente()+")");
		hql.addCriteria("dc.idDivisaoCliente", "=", idDivisaoCliente);
	
		List<HistoricoReajusteCliente> toReturn = getAdsmHibernateTemplate().find(hql.toString(), hql.getCriteria());

		return toReturn;		
	}	
	
	/*
	 * (SELECT MAX(HISTORICO_REAJUSTE_CLIENTE_DT.DT_REAJUSTE) DT_REAJUSTE FROM
	 * HISTORICO_REAJUSTE_CLIENTE HISTORICO_REAJUSTE_CLIENTE_DT WHERE
	 * HISTORICO_REAJUSTE_CLIENTE_DT.ID_TABELA_DIVISAO_CLIENTE =
	 * TABELA_DIVISAO_CLIENTE.ID_TABELA_DIVISAO_CLIENTE)
	 */
	private String loadMaxDtReajusteHistoricoReajusteCliente() {
		SqlTemplate subHql = new SqlTemplate();

		subHql.addProjection("MAX(hrcDt.dtReajuste)");
		subHql.addFrom(HistoricoReajusteCliente.class.getSimpleName(), "hrcDt");
		subHql.addCustomCriteria("hrcDt.tabelaDivisaoCliente.idTabelaDivisaoCliente = tdc.idTabelaDivisaoCliente");

		return subHql.toString();
	}		
}