package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeTimeOfDayUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TrechoRotaIdaVoltaDAO extends BaseCrudDao<TrechoRotaIdaVolta, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TrechoRotaIdaVolta.class;
	}

	public List findTrechosByIdRotaIdaVolta(Long idRotaIdaVolta) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"TRIV");

		ProjectionList projections = Projections.projectionList()
		.add(Projections.property("TRIV.idTrechoRotaIdaVolta"), "idTrechoRotaIdaVolta")
		.add(Projections.property("FRO.idFilialRota"), "filialRotaByIdFilialRotaOrigem.idFilialRota")
		.add(Projections.property("FRD.idFilialRota"), "filialRotaByIdFilialRotaDestino.idFilialRota")
		.add(Projections.property("FRO.nrOrdem"), "filialRotaByIdFilialRotaOrigem.nrOrdem")
		.add(Projections.property("FRD.nrOrdem"), "filialRotaByIdFilialRotaDestino.nrOrdem")
		.add(Projections.property("FO.idFilial"), "filialRotaByIdFilialRotaOrigem.filial.idFilial")
		.add(Projections.property("FD.idFilial"), "filialRotaByIdFilialRotaDestino.filial.idFilial")
		.add(Projections.property("FO.sgFilial"), "filialRotaByIdFilialRotaOrigem.filial.sgFilial")
		.add(Projections.property("FD.sgFilial"), "filialRotaByIdFilialRotaDestino.filial.sgFilial")
		.add(Projections.property("PO.nmFantasia"), "filialRotaByIdFilialRotaOrigem.filial.pessoa.nmFantasia")
		.add(Projections.property("PD.nmFantasia"), "filialRotaByIdFilialRotaDestino.filial.pessoa.nmFantasia")
		.add(Projections.property("TRIV.hrSaida"), "hrSaida")
		.add(Projections.property("TRIV.nrDistancia"), "nrDistancia")
		.add(Projections.property("TRIV.nrTempoViagem"), "nrTempoViagem")
		.add(Projections.property("TRIV.nrTempoOperacao"), "nrTempoOperacao")
		.add(Projections.property("TRIV.blDomingo"), "blDomingo")
		.add(Projections.property("TRIV.blSegunda"), "blSegunda")
		.add(Projections.property("TRIV.blTerca"), "blTerca")
		.add(Projections.property("TRIV.blQuarta"), "blQuarta")
		.add(Projections.property("TRIV.blQuinta"), "blQuinta")
		.add(Projections.property("TRIV.blSexta"), "blSexta")
		.add(Projections.property("TRIV.blSabado"), "blSabado")
		.add(Projections.property("TRIV.vlRateio"), "vlRateio")
		.add(Projections.property("TRIV.versao"), "versao");

		dc.setProjection(projections);
		dc.createAlias("TRIV.filialRotaByIdFilialRotaOrigem", "FRO");
		dc.createAlias("FRO.filial", "FO");
		dc.createAlias("FO.pessoa", "PO");
		dc.createAlias("TRIV.filialRotaByIdFilialRotaDestino", "FRD");
		dc.createAlias("FRD.filial", "FD");
		dc.createAlias("FD.pessoa", "PD");
		dc.createAlias("TRIV.rotaIdaVolta", "RIV");

		dc.add(Restrictions.eq("RIV.idRotaIdaVolta",idRotaIdaVolta));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(TrechoRotaIdaVolta.class));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public Integer getRowCountByRotaIdaVolta(Long idRotaIdaVolta) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "triv");
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("triv.rotaIdaVolta.id", idRotaIdaVolta));

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Validação ao salvar trecho da rota ida.
	 * @param idTrechoRotaIdaVolta
	 * @param idRotaIdaVolta
	 * @param idFilialOrigem
	 * @param hrSaida
	 * @return trechos onde é possível alterar a rota.
	 */
	public List<TrechoRotaIdaVolta> findTrechosComHoraInformada(Long idTrechoRotaIdaVolta, Long idRotaIdaVolta, Long idFilialOrigem, TimeOfDay hrSaida) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "triv");
		dc.createAlias("triv.filialRotaByIdFilialRotaOrigem", "fro");
		dc.add(Restrictions.eq("triv.rotaIdaVolta.id", idRotaIdaVolta));
		dc.add(Restrictions.eq("fro.filial.id", idFilialOrigem));
		if(hrSaida == null) {
			dc.add(Restrictions.isNotNull("triv.hrSaida"));
		} else {
			dc.add(Restrictions.ne("triv.hrSaida", hrSaida));
		}
		if(idTrechoRotaIdaVolta != null) {
			dc.add(Restrictions.eq("triv.id", idTrechoRotaIdaVolta));
		}

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List findToConsultarRotas(Long idRotaIdaVolta) {
		StringBuffer hql = new StringBuffer()
			.append(" select new Map( ")
			.append("   TRIV.idTrechoRotaIdaVolta as idTrechoRotaIdaVolta, ")
			.append("	FiO.sgFilial as sgFilialOrigem, ")
			.append("	PO.nmFantasia as nmFilialOrigem, ")
			.append("   FiD.sgFilial as sgFilialDestino, ")
			.append("   PD.nmFantasia as nmFilialDestino, ")
			.append("   TRIV.hrSaida as hrSaida, ")
			.append("   TRIV.nrDistancia as nrDistancia, ")
			.append("   TRIV.nrTempoViagem as nrTempoViagem, ")
			.append("   TRIV.nrTempoOperacao as nrTempoOperacao, ")
			.append("   TRIV.blDomingo as blDomingo, ")
			.append("   TRIV.blSegunda as blSegunda, ")
			.append("   TRIV.blTerca as blTerca, ")
			.append("   TRIV.blQuarta as blQuarta, ")
			.append("   TRIV.blQuinta as blQuinta, ")
			.append("   TRIV.blSexta as blSexta, ")
			.append("   TRIV.blSabado as blSabado ")
			.append(" ) ")
			.append(" from " + TrechoRotaIdaVolta.class.getName() + " TRIV ")
			.append(" inner join TRIV.rotaIdaVolta as RIV ")

			.append(" inner join TRIV.filialRotaByIdFilialRotaOrigem as FO ")
			.append(" inner join FO.filial as FiO ")
			.append(" inner join FiO.pessoa as PO ")
			.append(" inner join TRIV.filialRotaByIdFilialRotaDestino as FD ")
			.append(" inner join FD.filial as FiD ")
			.append(" inner join FiD.pessoa as PD ")

			.append(" where RIV.idRotaIdaVolta = ? ")
			.append(" order by FO.nrOrdem ");

		return getAdsmHibernateTemplate().find(hql.toString(),idRotaIdaVolta);
	}

	/**
	 * Valida se trecho compreende as filiais de origem e destino da rota
	 * @param idTrechoIdaVolta
	 * @return
	 */
	public boolean validateTrechoFromWholeRota(Long idTrechoIdaVolta) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"TRIV");

		dc.setProjection(Projections.count("TRIV.idTrechoRotaIdaVolta"));
		dc.createAlias("filialRotaByIdFilialRotaOrigem", "FO");
		dc.createAlias("filialRotaByIdFilialRotaDestino", "FD");
		dc.add(Restrictions.eq("TRIV.idTrechoRotaIdaVolta", idTrechoIdaVolta));
		dc.add(Restrictions.eq("FO.blOrigemRota", Boolean.TRUE));
		dc.add(Restrictions.eq("FD.blDestinoRota", Boolean.TRUE));

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	/**
	 * @param idRotaIdaVolta (required)
	 * @param dtSaida (required)
	 * @return
	 */
	public List findToTrechosByTrechosRota(Long idRotaIdaVolta, YearMonthDay dtSaidaRota) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT ")
		.append("TRIV.ID_TRECHO_ROTA_IDA_VOLTA AS ID_TRECHO_ROTA_IDA_VOLTA, ")
		.append("TRIV.HR_SAIDA AS HR_SAIDA, ")
		.append("TRIV.NR_TEMPO_VIAGEM AS NR_TEMPO_VIAGEM, ")
		.append("TRIV.NR_TEMPO_OPERACAO AS NR_TEMPO_OPERACAO, ")
		.append("TRIV.NR_DISTANCIA AS NR_DISTANCIA, ")
		.append("FR_ORIGEM.NR_ORDEM AS NR_ORDEM_ORIGEM, ")
		.append("FR_ORIGEM.ID_FILIAL AS ID_FILIAL_ORIGEM, ")
		.append("FR_DESTINO.NR_ORDEM AS NR_ORDEM_DESTINO, ")
		.append("FR_DESTINO.ID_FILIAL AS ID_FILIAL_DESTINO, ")
		.append("NVL((SELECT trunc(? + TRIV_ANT.NR_TEMPO_VIAGEM/1440 + TRIV_ANT.NR_TEMPO_OPERACAO/1440) ")
		.append("FROM ")
		.append("TRECHO_ROTA_IDA_VOLTA TRIV_ANT, ")
		.append("FILIAL_ROTA FRO_ANT ")
		.append("WHERE ")
		.append("TRIV_ANT.ID_FILIAL_ROTA_ORIGEM = FRO_ANT.ID_FILIAL_ROTA ")
		.append("AND FRO_ANT.BL_ORIGEM_ROTA = 'S' ")
		.append("AND TRIV_ANT.ID_ROTA_IDA_VOLTA = TRIV.ID_ROTA_IDA_VOLTA ")
		.append("AND TRIV_ANT.ID_FILIAL_ROTA_DESTINO = TRIV.ID_FILIAL_ROTA_ORIGEM ")
		.append("AND ROWNUM = 1), ?) AS DT_SAIDA ")
		.append("FROM ")
		.append("TRECHO_ROTA_IDA_VOLTA TRIV ")
		.append("INNER JOIN FILIAL_ROTA FR_ORIGEM on (TRIV.ID_FILIAL_ROTA_ORIGEM = FR_ORIGEM.ID_FILIAL_ROTA) ")
		.append("INNER JOIN FILIAL_ROTA FR_DESTINO on (TRIV.ID_FILIAL_ROTA_DESTINO = FR_DESTINO.ID_FILIAL_ROTA) ")
		.append("WHERE ")
		.append("TRIV.ID_ROTA_IDA_VOLTA = ? ")
		.append("ORDER BY FR_ORIGEM.NR_ORDEM ");

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_TRECHO_ROTA_IDA_VOLTA",Hibernate.LONG);
				sqlQuery.addScalar("HR_SAIDA",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
				sqlQuery.addScalar("NR_TEMPO_VIAGEM",Hibernate.INTEGER);
				sqlQuery.addScalar("NR_TEMPO_OPERACAO",Hibernate.INTEGER);
				sqlQuery.addScalar("NR_DISTANCIA",Hibernate.INTEGER);
				sqlQuery.addScalar("NR_ORDEM_ORIGEM",Hibernate.INTEGER);
				sqlQuery.addScalar("ID_FILIAL_ORIGEM",Hibernate.LONG);
				sqlQuery.addScalar("NR_ORDEM_DESTINO",Hibernate.INTEGER);
				sqlQuery.addScalar("ID_FILIAL_DESTINO",Hibernate.LONG);
				sqlQuery.addScalar("DT_SAIDA", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
			}
		};

		List param = new ArrayList();
		param.add(dtSaidaRota);
		param.add(dtSaidaRota);
		param.add(idRotaIdaVolta);

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), csq);
		return rsp.getList();
	}

	/**
	 * Consulta trecho por filial origem e rota.
	 * @param idRotaIdaVolta
	 * @param idFilial
	 * @return
	 */
	public TrechoRotaIdaVolta findTrechoByIdRotaAndFilialOrigem(Long idRotaIdaVolta) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select triv from " + TrechoRotaIdaVolta.class.getName() + " as triv ")
		.append(" inner join triv.filialRotaByIdFilialRotaOrigem filialRotaOrigem ")
		.append(" where triv.rotaIdaVolta.id = ? ")
		.append("   and filialRotaOrigem.blOrigemRota = ? ");

		List parameters = new ArrayList();
		parameters.add(idRotaIdaVolta);
		parameters.add(Boolean.TRUE);

		List l = getAdsmHibernateTemplate().find(hql.toString(),parameters.toArray());

		return l.isEmpty() ? null : (TrechoRotaIdaVolta)l.get(0);
	}

	/**
	 * Consulta trecho correspondente a rota completa
	 * 
	 * @param idRotaIdaVolta
	 * @return
	 */
	public TrechoRotaIdaVolta findByTrechoRotaCompleta(Long idRotaIdaVolta) {
		StringBuffer sql = new StringBuffer()
		.append("select triv ")
		.append("from ") 
		.append(TrechoRotaIdaVolta.class.getName()).append(" as triv ")
		.append("inner join fetch triv.filialRotaByIdFilialRotaOrigem filialRotaOrigem ")
		.append("inner join fetch filialRotaOrigem.filial filialOrigem ")
		.append("inner join triv.filialRotaByIdFilialRotaDestino filialRotaDestino ")
		.append("where triv.rotaIdaVolta.id = ? ")
		.append("and filialRotaOrigem.blOrigemRota = ? ")
		.append("and filialRotaDestino.blDestinoRota = ? ");

		List parameters = new ArrayList();
		parameters.add(idRotaIdaVolta);
		parameters.add(Boolean.TRUE);
		parameters.add(Boolean.TRUE);

		List result = getAdsmHibernateTemplate().find(sql.toString(), parameters.toArray());
		if (result.isEmpty())
			return null;
		return (TrechoRotaIdaVolta)result.get(0);
	}

	public TrechoRotaIdaVolta findTrechoByIdRotaIdaVoltaAndFilialUsuarioLogado(Long idRotaIdaVolta) {
		long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"triv");
		dc.createAlias("filialRotaByIdFilialRotaOrigem", "fo");
		dc.createAlias("rotaIdaVolta", "riv");
		dc.createAlias("fo.filial", "filial");
		dc.setFetchMode("fo", FetchMode.JOIN);
		dc.add(Restrictions.eq("riv.id", idRotaIdaVolta));
		dc.add(Restrictions.eq("filial.id", idFilial));

		List list = getAdsmHibernateTemplate().findByCriteria(dc);
		return !list.isEmpty() ? (TrechoRotaIdaVolta)list.get(0) : null;
	}
	
	public List<TrechoRotaIdaVolta> findByTrechoRota(Long idRotaIdaVolta, Long idFilialOrigem, Long idFilialDestino) {
		StringBuffer sql = new StringBuffer()
		.append("select triv ")
		.append("from ") 
		.append(TrechoRotaIdaVolta.class.getName()).append(" as triv ")
		.append("inner join fetch triv.filialRotaByIdFilialRotaOrigem filialRotaOrigem ")
		.append("inner join fetch filialRotaOrigem.filial filialOrigem ")
		.append("inner join fetch triv.filialRotaByIdFilialRotaDestino filialRotaDestino ")
		.append("inner join fetch filialRotaDestino.filial filialDestino ")
		.append("inner join fetch triv.rotaIdaVolta rotaIdaVolta ")
		.append("where rotaIdaVolta.id = ? ")
		.append("and filialOrigem.id = ? ")
		.append("and filialDestino.id = ? ");

		List parameters = new ArrayList();
		parameters.add(idRotaIdaVolta);
		parameters.add(idFilialOrigem);
		parameters.add(idFilialDestino);

		return getAdsmHibernateTemplate().find(sql.toString(), parameters.toArray());
	}
	
}