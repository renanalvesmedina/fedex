package com.mercurio.lms.expedicao.model.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.type.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.core.util.YearMonthDayConverter;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.ControleEsteira;
import com.mercurio.lms.expedicao.model.HistoricoAfericao;
import com.mercurio.lms.util.session.SessionUtils;

public class HistoricoAfericaoSorterDAO extends
BaseCrudDao<HistoricoAfericao, Long> {
	public HistoricoAfericao findByFilial(Long idFilial) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(getPersistentClass().getName()
				+ " historicoAfericao ");
		hql.addCriteria("historicoAfericao.filial.id", "=", idFilial);
		return (HistoricoAfericao) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedHistoricoAfericaoSorter(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sqlTemplate = this.getSqlTemplateFindPaginatedHistoricoAfericaoSorter(criteria);

		sqlTemplate.addProjection("new map (" +
				" historicoAfericao.id as idHistoricoAfericao," +
				" etiquetaAfericao.id as idEtiquetaAfericao," +
				" filialAfericao.sgFilial as sgFilialAfericao," +
				" filialOrigem.sgFilial as sgFilialOrigem," +
				" filialDestino.sgFilial as sgFilialDestino," +
				" (rota.nrRota||' - '||rota.dsRota) as rota," +
				" (historicoAfericao.nrComprimento||' X '||historicoAfericao.nrLargura||' X '||historicoAfericao.nrAltura) as dimensoesAferidas," +
				" (etiquetaAfericao.nrComprimento||' X '||etiquetaAfericao.nrLargura||' X '||etiquetaAfericao.nrAltura) as dimensoesPadrao," +
				" historicoAfericao.psAferido as pesoAferido," +
				" etiquetaAfericao.psInformado as pesoPadrao," +
				" historicoAfericao.dhAfericao as dhEmissao," +
				" etiquetaAfericao.nrCodigoBarras as nrCodBarras) " );

		sqlTemplate.addOrderBy("historicoAfericao.dhAfericao", "desc");
		sqlTemplate.addOrderBy("filialOrigem.sgFilial", "asc");
		sqlTemplate.addOrderBy("filialDestino.sgFilial", "asc");
		sqlTemplate.addOrderBy("rota.dsRota", "asc");

		return getAdsmHibernateTemplate().findPaginated(sqlTemplate.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sqlTemplate.getCriteria());
	}

	private SqlTemplate getSqlTemplateFindPaginatedHistoricoAfericaoSorter(TypedFlatMap criteria){
		StringBuffer from = new StringBuffer();
		from.append(HistoricoAfericao.class.getName() + " as historicoAfericao ");
		from.append(" join historicoAfericao.filial as filialAfericao ");
		from.append(" join historicoAfericao.etiquetaAfericao as etiquetaAfericao");
		from.append(" join etiquetaAfericao.filialOrigem as filialOrigem ");
		from.append(" join etiquetaAfericao.filialDestino as filialDestino ");
		from.append(" join etiquetaAfericao.rotaColetaEntrega as rota ");

		SqlTemplate sqlTemplate = new SqlTemplate();
		sqlTemplate.addFrom( from.toString() );

		if(criteria.getLong("idFilialAfericao") != null ){
			sqlTemplate.addCriteria("historicoAfericao.filial.id", "=", criteria.getLong("idFilialAfericao"));
		}

		if(criteria.getLong("idFilialOrigem") != null ){
			sqlTemplate.addCriteria("etiquetaAfericao.filialOrigem.id", "=", criteria.getLong("idFilialOrigem"));
		}

		if(criteria.getLong("idFilialDestino") != null ){
			sqlTemplate.addCriteria("etiquetaAfericao.filialDestino.id", "=", criteria.getLong("idFilialDestino"));
		}

		if(criteria.getLong("idRotaColetaEntrega") != null ){
			sqlTemplate.addCriteria("etiquetaAfericao.rotaColetaEntrega.id", "=", criteria.getLong("idRotaColetaEntrega"));
		}
		
		if(criteria.getYearMonthDay("periodoAfericaoInicial") != null ){
			sqlTemplate.addCriteria("TRUNC(CAST(historicoAfericao.dhAfericao.value AS date))", ">=", criteria.getYearMonthDay("periodoAfericaoInicial"));
		}
		
		if(criteria.getYearMonthDay("periodoAfericaoFinal") != null ){
			sqlTemplate.addCriteria("TRUNC(CAST(historicoAfericao.dhAfericao.value AS date))", "<=", criteria.getYearMonthDay("periodoAfericaoFinal"));
		}

		return sqlTemplate;
	}

	public Integer getRowCountHistoricoAfericaoSorter(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplateFindPaginatedHistoricoAfericaoSorter(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	/**
	 * @return
	 * @author WagnerFC
	 */
	@SuppressWarnings("unchecked")
	public List<ControleEsteira> findControleEsteiraMaiorAfericaoForBach() {
		
		DateTime maxDhAfericao = findMaxDhAfericaoHistoricoAfericao();
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ce");
		hql.append(" FROM ").append(ControleEsteira.class.getName()).append(" ce ");
		hql.append(" WHERE substring(ce.codBarras, 0, 2) = '03'");
		if (maxDhAfericao!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
			String dt = sdf.format(maxDhAfericao.toDate());
			hql.append(" AND to_timestamp_TZ(ce.data||' '||ce.hora , 'dd/mm/yy HH24:MI') > to_timestamp_TZ( ? , 'dd/mm/yy HH24:MI')");
			return (List<ControleEsteira>) getAdsmHibernateTemplate().find(hql.toString(), dt);
		}else{
			return (List<ControleEsteira>) getAdsmHibernateTemplate().find(hql.toString());
		}
	}
	
	public DateTime findMaxDhAfericaoHistoricoAfericao() {
		DetachedCriteria dc = DetachedCriteria.forClass(HistoricoAfericao.class, "historicoAfericao");
		dc.setProjection(Projections.sqlProjection("MAX(DH_AFERICAO) AS dhAfericao", new String[]{"dhAfericao"}, new Type[]{Hibernate.TIMESTAMP}));
		
		List list = getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		if (list != null && !list.isEmpty()){
			Timestamp ts = (Timestamp)list.get(0);
			return new DateTime(ts.getTime());
		}
		return  null;
	}
	

	@Override
	protected Class<HistoricoAfericao> getPersistentClass() {
		return HistoricoAfericao.class;
	}


	@Override
	@SuppressWarnings("rawtypes")
	protected void initFindByIdLazyProperties(Map lazyFindById) {
	}
}
