package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FluxoFilialDAO extends BaseCrudDao<FluxoFilial, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return FluxoFilial.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("servico",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino.pessoa",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialReembarcadora",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialReembarcadora.pessoa",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem.empresa",FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem.empresa.pessoa",FetchMode.JOIN);
	}

	/**
	 * Cria HQL para consultar as filiais Mercúrio onde ocorrem reembarque de mercadoria.
	 * @param map
	 * @return
	 */
	private SqlTemplate getHqlFluxoReembarque(
			Long idFluxoFilial,
			Long idFilialOrigem,
			Long idFilialDestino,
			Long idFilialReembarcadora,
			Long idServico,
			YearMonthDay dtVigenciaInicial,
			YearMonthDay dtVigenciaFinal
	) {
		SqlTemplate hql = new SqlTemplate();

		StringBuffer hqlFrom = new StringBuffer()
			.append(FluxoFilial.class.getName()).append(" as ff ")
			.append("	  join fetch ff.filialByIdFilialOrigem as fo ")
			.append("	  join fetch ff.filialByIdFilialDestino as fd ")
			.append("left join fetch ff.filialByIdFilialReembarcadora as fr ")
			.append("	  join fetch ff.filialByIdFilialOrigem.pessoa as po ")
			.append("left join fetch ff.servico as se ");

		hql.addFrom(hqlFrom.toString());	

		hql.addCriteria("ff.idFluxoFilial","!=",idFluxoFilial);
		hql.addCriteria("fo.idFilial","=",idFilialOrigem);
		hql.addCriteria("fd.idFilial","=",idFilialDestino);

		if (idFilialReembarcadora != null && CompareUtils.eq(idFilialReembarcadora,-1L)) {
			hql.addCustomCriteria("fr.idFilial is null");
		} else {
			hql.addCriteria("fr.idFilial","=",idFilialReembarcadora);
		}


		if (idServico != null) {
			hql.addCustomCriteria("( se.idServico = ?  or se.idServico is NULL )", idServico);
		} else {
			hql.addCustomCriteria("se.idServico is NULL");
		}

		if (dtVigenciaInicial != null) {
			hql.addCustomCriteria("ff.dtVigenciaInicial <= ? and ff.dtVigenciaFinal >= ?");
			hql.addCriteriaValue(dtVigenciaInicial);
			hql.addCriteriaValue(JTDateTimeUtils.maxYmd(dtVigenciaFinal));
		}

		hql.addOrderBy("se.idServico", idServico != null ? "asc" : "desc");
		hql.addOrderBy("ff.idFluxoFilial", "desc");

		return hql;
	}

	/**
	 * Consulta as filiais Mercúrio onde ocorrem reembarque de mercadoria.
	 * @param map
	 * @return
	 */
	public List findFluxoReembarque(Long idFluxoFilial, Long idFilialDestino, Long idFilialReembarcadora, Long idServico, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return findFluxoReembarque(idFluxoFilial,idFilialReembarcadora, idFilialDestino, null, idServico, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Cria HQL para consultar as filiais Mercúrio onde ocorrem reembarque de mercadoria.
	 * @param map
	 * @return
	 */
	private SqlTemplate getHqlFluxosPais(Long idFluxoFilial,
			Long idFilialOrigem, Long idFilialDestino, Long idServico,
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		SqlTemplate hql = new SqlTemplate();

		StringBuffer hqlTmp = new StringBuffer()
			.append(FluxoFilial.class.getName()).append(" as Fl ")
			.append("	  join fetch Fl.filialByIdFilialOrigem as Fo ")
			.append("	  join fetch Fl.filialByIdFilialDestino as Fd ")
			.append("left join fetch Fl.filialByIdFilialReembarcadora as Fr ")
			.append("left join fetch Fl.servico as Se ")
			.append("	  join fetch Fl.filialByIdFilialOrigem.pessoa as Pe ");

		hql.addFrom(hqlTmp.toString());	

		hql.addCriteria("Fr.idFilial","=",idFilialOrigem);
		hql.addCriteria("Fd.idFilial","=",idFilialDestino);

		hql.addCriteria("Fl.idFluxoFilial","!=",idFluxoFilial);

		if (idServico != null) {
			hql.addCustomCriteria("(Se.idServico = ?)",idServico);
		} else {
			hql.addCustomCriteria("not exists(select FFAux.id from " + FluxoFilial.class.getName() + " as FFAux " +
					"where FFAux.filialByIdFilialOrigem.id = ? " +
					"  and FFAux.filialByIdFilialDestino.id = ? " +
					"  and FFAux.servico.id = Se.id " +
					"  and FFAux.dtVigenciaInicial >= ? " +
					"  and FFAux.dtVigenciaFinal <= ?" +
					"  and Fl.id != FFAux.id)");
			hql.addCriteriaValue(idFilialOrigem);
			hql.addCriteriaValue(idFilialDestino);
			hql.addCriteriaValue(dtVigenciaInicial);
			hql.addCriteriaValue(JTDateTimeUtils.maxYmd(dtVigenciaFinal));
		}

		if (dtVigenciaInicial != null) {
			hql.addCustomCriteria("Fl.dtVigenciaInicial >= ? and Fl.dtVigenciaFinal <= ?");
			hql.addCriteriaValue(dtVigenciaInicial);
			hql.addCriteriaValue(JTDateTimeUtils.maxYmd(dtVigenciaFinal));
		}

		hql.addOrderBy("Se.idServico",idServico != null ? "asc" : "desc");

		return hql;
	}

	/**
	 * Consulta as filiais Mercúrio onde ocorrem reembarque de mercadoria.
	 * @param map
	 * @return
	 */
	public List findFluxosPais(
		Long idFluxoFilial,
		Long idFilialOrigem,
		Long idFilialDestino,
		Long idServico,
		YearMonthDay dtVigenciaInicial,
		YearMonthDay dtVigenciaFinal
	) {
		SqlTemplate hql = getHqlFluxosPais(
				idFluxoFilial,
				idFilialOrigem,
				idFilialDestino,
				idServico,
				dtVigenciaInicial,
				dtVigenciaFinal
			);
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	/**
	 * Consulta as filiais Mercúrio onde ocorrem reembarque de mercadoria.
	 * @param map
	 * @return
	 */
	public List findFluxoReembarque(
		Long idFluxoFilial,
		Long idFilialOrigem,
		Long idFilialDestino,
		Long idFilialReembarcadora,
		Long idServico,
		YearMonthDay dtVigenciaInicial,
		YearMonthDay dtVigenciaFinal
	) {
		SqlTemplate hql = getHqlFluxoReembarque(
				idFluxoFilial,
				idFilialOrigem,
				idFilialDestino,
				idFilialReembarcadora,
				idServico,
				dtVigenciaInicial,
				dtVigenciaFinal
			);
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	/**
	 * Verifica se não há registros repetidos
	 * @param fluxoFilial
	 * @return
	*/ 
	public boolean verificaFluxoFilialVigentes(FluxoFilial fluxoFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.createAlias("filialByIdFilialOrigem","fo");
		dc.createAlias("filialByIdFilialDestino","fd");
		dc.setFetchMode("servico", FetchMode.JOIN);
		if (fluxoFilial.getIdFluxoFilial() != null)
			dc.add(Restrictions.ne("idFluxoFilial",fluxoFilial.getIdFluxoFilial()));
		dc.add(Restrictions.eq("fo.idFilial",fluxoFilial.getFilialByIdFilialOrigem().getIdFilial()));
		dc.add(Restrictions.eq("fd.idFilial",fluxoFilial.getFilialByIdFilialDestino().getIdFilial()));
		
		if (fluxoFilial.getServico() != null)
			dc.add(Restrictions.eq("servico.idServico",fluxoFilial.getServico().getIdServico()));
		else
			dc.add(Restrictions.isNull("servico.idServico"));
		
		dc = JTVigenciaUtils.getDetachedVigenciaFluxoFilial(dc,fluxoFilial.getDtVigenciaInicial(),fluxoFilial.getDtVigenciaFinal());
		List l = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		return l.size() > 0;
	}

	/**
	 * Procurar na tabela FLUXO FILIAL fluxos que utilizam o fluxo recebido como parâmetro.
	 * Se considerVigencia for <b>true</b>, considera apenas registros que estiverem vigentes após a data de final de
	 * vigência do fluxo recebido.
	 * 
	 * @param fluxoFilial
	 * @return boolean
	 * <b>true</b> se forem encontrados fluxos.
	 */
	public boolean verificaFluxosFilhos(FluxoFilial fluxoFilial, boolean considerVigencia) {
		Long idFilialOrigem = null;
		Long idFilialDestino = null;
		if (fluxoFilial != null) {
			Filial filialByIdFilialOrigem = fluxoFilial.getFilialByIdFilialOrigem();
			Filial filialByIdFilialDestino = fluxoFilial.getFilialByIdFilialDestino();
			if (filialByIdFilialOrigem != null) {
				idFilialOrigem = filialByIdFilialOrigem.getIdFilial();
				idFilialDestino = filialByIdFilialDestino.getIdFilial();
			}
		}
		if (idFilialOrigem == null || idFilialDestino == null)
			throw new IllegalArgumentException("O método 'verificaFluxosFilhos' deve receber uma instância de FluxoFilial com " +
					"filial de origem e filial destino.");

		SqlTemplate sql = new SqlTemplate();

		StringBuffer sqlFrom = new StringBuffer()
				.append(getPersistentClass().getName()).append(" FF ")
				.append(" inner join FF.filialByIdFilialOrigem as FO ")
				.append(" inner join FF.filialByIdFilialReembarcadora as FR ")
				.append(" inner join FF.filialByIdFilialDestino as FD ")
				.append("  left join FF.servico as S ");

		sql.addFrom(sqlFrom.toString());

		StringBuffer sqlCriteria = new StringBuffer()
				.append(" (( FR.idFilial = ? AND FD.idFilial = ? ) ")
				.append(" or ")
				.append("  ( FO.idFilial = ? AND FR.idFilial = ? ))  ");

		sql.addCustomCriteria(sqlCriteria.toString());
		sql.addCriteriaValue(idFilialOrigem);
		sql.addCriteriaValue(idFilialDestino);
		sql.addCriteriaValue(idFilialOrigem);
		sql.addCriteriaValue(idFilialDestino);

		if (considerVigencia) {
			sql.addCustomCriteria("( (FF.dtVigenciaInicial >= ? and FF.dtVigenciaInicial <= ?) or (FF.dtVigenciaFinal >= ? and FF.dtVigenciaFinal <= ?) )");
			sql.addCriteriaValue(fluxoFilial.getDtVigenciaInicial());
			sql.addCriteriaValue(JTDateTimeUtils.maxYmd(fluxoFilial.getDtVigenciaFinal()));
			
			sql.addCriteriaValue(fluxoFilial.getDtVigenciaInicial());
			sql.addCriteriaValue(JTDateTimeUtils.maxYmd(fluxoFilial.getDtVigenciaFinal()));
						
		}

		if (fluxoFilial.getServico() != null) {
			sql.addCustomCriteria("S.id = ?");
			sql.addCriteriaValue(fluxoFilial.getServico().getIdServico());
		}

		Integer count = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());

		return count.intValue() > 0;
	}

	
		
	/**
	 * Implementado HQL para ordenar resultSet por campo não obrigatório.
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = createHqlPaginated(criteria);

		sql.addProjection("new map( ff.idFluxoFilial", "idFluxoFilial");
		sql.addProjection("ff.nrDistancia", "nrDistancia");
		sql.addProjection("(ff.nrPrazo / 60)", "nrPrazoView");
		sql.addProjection("ff.blDomingo", "blDomingo");
		sql.addProjection("ff.blSegunda", "blSegunda");
		sql.addProjection("ff.blTerca", "blTerca");
		sql.addProjection("ff.blQuarta", "blQuarta");
		sql.addProjection("ff.blQuinta", "blQuinta");
		sql.addProjection("ff.blSexta", "blSexta");
		sql.addProjection("ff.blSabado", "blSabado");
		sql.addProjection("ff.nrGrauDificuldade", "nrGrauDificuldade");
		sql.addProjection("ff.dtVigenciaInicial", "dtVigenciaInicial");
		sql.addProjection("ff.dtVigenciaFinal", "dtVigenciaFinal");
		sql.addProjection("s.dsServico", "servico_dsServico");
		sql.addProjection("fo.sgFilial", "filialByIdFilialOrigem_sgFilial");
		sql.addProjection("po.nmFantasia", "filialByIdFilialOrigem_pessoa_nmFantasia");
		sql.addProjection("fd.sgFilial", "filialByIdFilialDestino_sgFilial");
		sql.addProjection("pd.nmFantasia", "filialByIdFilialDestino_pessoa_nmFantasia");
		sql.addProjection("fr.sgFilial", "filialByIdFilialReembarcadora_sgFilial");
		sql.addProjection("pr.nmFantasia", "filialByIdFilialReembarcadora_pessoa_nmFantasia");
		sql.addProjection("fp.sgFilial", "filialByIdFilialParceira_sgFilial");
		sql.addProjection("pp.nmFantasia", "filialByIdFilialParceira_pessoa_nmFantasia)");		

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

	/**
	 * HQL para o findPaginated
	 * 
	 * @param criteria
	 * @return
	 */
	private SqlTemplate createHqlPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append(FluxoFilial.class.getName()).append(" as ff ");
		hqlFrom.append(" join ff.filialByIdFilialOrigem as fo ");
		hqlFrom.append(" join fo.pessoa as po ");
		hqlFrom.append(" join ff.filialByIdFilialDestino as fd ");
		hqlFrom.append(" join fd.pessoa as pd ");
		hqlFrom.append(" left join ff.filialByIdFilialReembarcadora as fr ");
		hqlFrom.append(" left join fr.pessoa as pr ");
		hqlFrom.append(" left join ff.filialByIdFilialParceira as fp ");
		hqlFrom.append(" left join fp.pessoa as pp ");		
		hqlFrom.append(" left join ff.servico as s ");

		sql.addFrom(hqlFrom.toString());

		sql.addCriteria("s.idServico", "=", criteria.getLong("servico.idServico"));
		sql.addCriteria("fo.idFilial", "=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("fd.idFilial", "=", criteria.getLong("filialByIdFilialDestino.idFilial"));
		sql.addCriteria("fr.idFilial", "=", criteria.getLong("filialByIdFilialReembarcadora.idFilial"));
		sql.addCriteria("fp.idFilial", "=", criteria.getLong("filialByIdFilialParceira.idFilial"));

		sql.addCriteria("ff.dtVigenciaInicial", ">=", criteria.getYearMonthDay("dtVigenciaInicial"));
		sql.addCriteria("ff.dtVigenciaFinal", "<=", criteria.getYearMonthDay("dtVigenciaFinal"));

		sql.addOrderBy("s.dsServico");
		sql.addOrderBy("fo.sgFilial");
		sql.addOrderBy("fd.sgFilial");
		sql.addOrderBy("fr.sgFilial");
		sql.addOrderBy("fp.sgFilial");

		return sql;
	}

	/**
	 * Consulta os fluxos a partir dos parametros informados
	 * @param idFilial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public List findFluxoFilialByFilial(Long idFilial,YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.or(Restrictions.eq("filialByIdFilialReembarcadora.idFilial",idFilial),
				Restrictions.or(
						Restrictions.eq("filialByIdFilialDestino.idFilial",idFilial),
						Restrictions.eq("filialByIdFilialOrigem.idFilial",idFilial)
				)));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Consulta os fluxos a partir das filiais de origem e destino, alem do servico
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @return
	 */
	public List findFluxoFilialByFilialDestinoOrigemServico(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtConsulta){
		if (dtConsulta == null)
			throw new IllegalArgumentException("dtConsulta não pode ser null.");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ff.filialByIdFilialReembarcadora.idFilial");
		sql.addProjection("ff.nrPrazo");
		sql.addProjection("ff.servico.idServico");
		sql.addProjection("ff.nrPrazoTotal");
		sql.addProjection("ff.idFluxoFilial");
		sql.addProjection("ff.filialByIdFilialOrigem.idFilial");
		sql.addProjection("ff.filialByIdFilialDestino.idFilial");
		sql.addProjection("ff.blPorto");

		sql.addFrom("FluxoFilial ff");
		sql.addCriteria("ff.filialByIdFilialDestino.idFilial", "=", idFilialDestino);
		sql.addCriteria("ff.filialByIdFilialOrigem.idFilial", "=", idFilialOrigem);
		if (idServico != null)
			sql.addCustomCriteria("(ff.servico.idServico is null or ff.servico.idServico = ?)", idServico);
		else
			sql.addCustomCriteria("ff.servico.idServico is null");
		sql.addCriteria("ff.dtVigenciaInicial", "<=", dtConsulta);
		sql.addCriteria("ff.dtVigenciaFinal",">=",dtConsulta);	
		sql.addOrderBy("ff.servico.idServico",idServico != null ? "asc" : "desc");

		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}


	/**
	 * Consulta os fluxos a partir das filiais de origem e destino, alem do servico e vigência
	 * retorna um FluxoFilial completo.
	 *  
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @param dtVigenciaIni
	 * @param dtVigenciaFin
	 *
	 * @return FluxoFilial
	 */
	@SuppressWarnings("unchecked")
	public List<FluxoFilial> findFluxoFilialByFilialDestinoOrigemServicoVigencia(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtConsulta){

		StringBuilder hql = new StringBuilder();
		StringBuilder order = new StringBuilder(" order by ff.servico.idServico " + (idServico != null ? "asc" : "desc"));

		StringBuilder idServicoNotNull = new StringBuilder();
		StringBuilder idServicoNull = new StringBuilder(" and ff.servico.idServico is null ");

		hql.append("select ff from FluxoFilial ff ");
		hql.append(" where ff.filialByIdFilialDestino.idFilial = " + idFilialDestino);
		hql.append(" and ff.filialByIdFilialOrigem.idFilial =  " + idFilialOrigem);
		hql.append(" and ff.dtVigenciaInicial <= to_date('" + dtConsulta.toString() + "','yyyy/MM/dd')");
		hql.append(" and ff.dtVigenciaFinal >= to_date('" + dtConsulta.toString() + "','yyyy/MM/dd')");
		
		if(idServico != null){
			idServicoNotNull.append(" and ff.servico.idServico = " + idServico) ;
		}

		List<FluxoFilial> flfluxoFilials = new ArrayList<FluxoFilial>();
		
		if(StringUtils.isNotBlank(idServicoNotNull.toString())){
			flfluxoFilials = getAdsmHibernateTemplate().find(hql.toString() + idServicoNotNull.toString() + order.toString());
			if(flfluxoFilials != null && !flfluxoFilials.isEmpty()){
				return flfluxoFilials;
			}
		}
		
		flfluxoFilials = getAdsmHibernateTemplate().find(hql.toString() + idServicoNull.toString() + order.toString());
		return flfluxoFilials != null ? flfluxoFilials : null;
	}
	

	private Object[] queryFluxoOrigem(TypedFlatMap tfm, boolean withProjection ) {
		Long idFilial = tfm.getLong("filial.idFilial");
		Long idServico = tfm.getLong("servico.idServico");
		YearMonthDay dtVigenciaInicial = tfm.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtVigenciaFinal = tfm.getYearMonthDay("dtVigenciaFinal");

		StringBuffer projection = new StringBuffer()
			.append(" select new Map( f.idFilial as idFilial, ")
							.append(" f.sgFilial as filialByIdFilialDestino_sgFilial, ")
							.append(" p.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, ")
							.append(" f.sgFilial || ' - ' || p.nmFantasia as filialByIdFilialDestino_siglaNomeFilial) ");
		//.append(" where f.idFilial <> :idFilial ")
		StringBuffer from = new StringBuffer()
			.append(" from Filial f ") 
			.append(" inner join f.pessoa p ")
			.append(" inner join f.historicoFiliais h ") 
			.append(" inner join f.empresa em ")

			.append(" where (nvl(h.dtRealOperacaoFinal, h.dtPrevisaoOperacaoFinal) >= :dtVigenciaFinal OR h.dtPrevisaoOperacaoFinal is null) ")
			.append(" and (nvl(h.dtRealOperacaoFinal, h.dtPrevisaoOperacaoFinal) >= :dtVigenciaInicial OR h.dtPrevisaoOperacaoFinal is null) ")
			.append(" and (nvl(h.dtRealOperacaoInicial, h.dtPrevisaoOperacaoInicial) <= :dtVigenciaInicial) ")
			.append(" and h.tpFilial != :tpFilial ")
			.append(" and em.tpEmpresa = 'M' ")
			.append(" and h.id = (select max(hist.id) ")
							.append("  from HistoricoFilial hist ")
							.append("  inner join hist.filial histF ") 
							.append(" where histF.idFilial = f.idFilial) ")

							.append(" and not exists ( from " + FluxoFilial.class.getName() + " flu")
							.append(" where flu.filialByIdFilialOrigem.idFilial = :idFilial ")
							.append("   and flu.filialByIdFilialDestino.idFilial = f.idFilial ")
							.append((idServico != null) ?       " and (flu.servico.idServico = :idServico)" 
														:       " and flu.servico is null")
							.append(" AND ((flu.dtVigenciaInicial <= :dtVigenciaInicial and flu.dtVigenciaFinal >= :dtVigenciaInicial) OR ") 
							.append(" (flu.dtVigenciaInicial >= :dtVigenciaInicial and flu.dtVigenciaInicial <= :dtVigenciaFinal)) ")
								
							.append(")")

			.append(" order by f.sgFilial, p.nmFantasia");

		Map map = new HashMap();
		map.put("idFilial", idFilial);
		map.put("tpFilial","MA");
		map.put("dtVigenciaInicial", dtVigenciaInicial);
		if (idServico != null) map.put("idServico", idServico);
		map.put("dtVigenciaFinal", JTDateTimeUtils.maxYmd(dtVigenciaFinal));

		return new Object[]{(withProjection ? projection.append(from).toString() : from.toString()), map};
	}

	private Object[] queryFluxoDestino(TypedFlatMap tfm, boolean withProjection) {

		Long idFilial = tfm.getLong("filial.idFilial");
		Long idServico = tfm.getLong("servico.idServico");
		YearMonthDay dtVigenciaInicial = tfm.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtVigenciaFinal = tfm.getYearMonthDay("dtVigenciaFinal");

		StringBuffer projection = new StringBuffer()
			.append(" select new Map( f.sgFilial || ' - ' || p.nmFantasia as filialByIdFilialOrigem_siglaNomeFilial, ")
							.append(" f.sgFilial as filialByIdFilialOrigem_sgFilial, ")
							.append(" p.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, ")
							.append(" f.idFilial as idFilial) ");

		//.append("  f.idFilial <> :idFilial ")
		StringBuffer from = new StringBuffer()			
			.append(" from Filial f ")
			.append(" inner join f.pessoa p")
			.append(" inner join f.historicoFiliais h ")
			.append(" inner join f.empresa em ")
			.append(" where (nvl(h.dtRealOperacaoFinal, h.dtPrevisaoOperacaoFinal) >= :dtVigenciaFinal OR h.dtPrevisaoOperacaoFinal is null) ")
			.append(" and (nvl(h.dtRealOperacaoFinal, h.dtPrevisaoOperacaoFinal) >= :dtVigenciaInicial OR h.dtPrevisaoOperacaoFinal is null) ")
			.append(" and (nvl(h.dtRealOperacaoInicial, h.dtPrevisaoOperacaoInicial) <= :dtVigenciaInicial) ")
			.append(" and h.tpFilial != :tpFilial ")
			.append(" and em.tpEmpresa = 'M' ")
			.append(" and h.id = (select max(hist.id) ")
							.append("  from HistoricoFilial hist ")
							.append("  inner join hist.filial histF ")
							.append(" where histF.idFilial = f.idFilial) ")

			.append(" and not exists ( from " + FluxoFilial.class.getName() + " flu")
							.append(" where flu.filialByIdFilialDestino.idFilial = :idFilial ")
							.append("   and flu.filialByIdFilialOrigem.idFilial = f.idFilial ")
							.append((idServico != null) ?       " and (flu.servico.idServico = :idServico OR flu.servico is null)" 
														:       " and flu.servico is null")
							.append(" AND ((flu.dtVigenciaInicial <= :dtVigenciaInicial and flu.dtVigenciaFinal >= :dtVigenciaInicial) OR ") 
							.append(" (flu.dtVigenciaInicial >= :dtVigenciaInicial and flu.dtVigenciaInicial <= :dtVigenciaFinal)) ")
							.append(")")

			.append(" order by f.sgFilial, p.nmFantasia");

		Map map = new HashMap();
		map.put("idFilial", idFilial);
		map.put("tpFilial","MA");
		map.put("dtVigenciaInicial", dtVigenciaInicial);
		if (idServico != null) map.put("idServico", idServico);
		map.put("dtVigenciaFinal", JTDateTimeUtils.maxYmd(dtVigenciaFinal));

		return new Object[]{(withProjection ? projection.append(from).toString() : from.toString()), map};
	}

	public Integer getRowCountFluxoInicialOrigem(TypedFlatMap tfm) {
		Object[] o = queryFluxoOrigem(tfm, false);
		return getAdsmHibernateTemplate().getRowCountForQuery((String)o[0],(Map)o[1]);
	}

	public Integer getRowCountFluxoInicialDestino(TypedFlatMap tfm) {
		Object[] o = queryFluxoDestino(tfm, false);
		return getAdsmHibernateTemplate().getRowCountForQuery((String)o[0],(Map)o[1]);
	}

	public ResultSetPage findPagindatedFluxoInicialOrigem(TypedFlatMap tfm,FindDefinition fDef) {
		Object[] o = queryFluxoOrigem(tfm, true);
		return getAdsmHibernateTemplate().findPaginated((String)o[0], fDef.getCurrentPage(), fDef.getPageSize(),(Map)o[1]);
	}

	public ResultSetPage findPagindatedFluxoInicialDestino(TypedFlatMap tfm,FindDefinition fDef) {
		Object[] o = queryFluxoDestino(tfm, true);
		return getAdsmHibernateTemplate().findPaginated((String)o[0],fDef.getCurrentPage(), fDef.getPageSize(),(Map)o[1]);
	}

	/**
	 * Retorna a Fluxo vigente na data informada
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @param dtVigencia
	 * @return
	 */
	public FluxoFilial findFluxoFilial(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia, Long idServico) {

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ff");

		dc.add(Restrictions.eq("ff.filialByIdFilialDestino.id", idFilialDestino));
		dc.add(Restrictions.eq("ff.filialByIdFilialOrigem.id", idFilialOrigem));
		dc.add(Restrictions.or(Restrictions.eq("ff.servico.id", idServico), Restrictions.isNull("ff.servico.id")) );
		dc.add(Restrictions.le("ff.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("ff.dtVigenciaFinal", dtVigencia));

		dc.addOrder(Order.asc("ff.servico.id"));

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		if(!result.isEmpty())
			return (FluxoFilial) result.get(0);
		else
			return null;
	}

	/**
	 * Retorna a Fluxo vigente na data informada
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @param dtVigencia
	 * @return
	 */
	public FluxoFilial findFluxoFilialPadraoMCD(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia, Long idServico, Long idMunicipio) {

		StringBuilder hql = new StringBuilder("SELECT ff FROM FluxoFilial ff, Filial f,  MunicipioFilial mf ")
				.append("WHERE ff.filialByIdFilialDestino.idFilial = f.idFilial  ")
				.append("AND f.idFilial = mf.filial.idFilial ")
				.append("AND mf.municipio.idMunicipio = ? ")
				.append("AND mf.blPadraoMcd = 'S' ")
				.append("AND ff.filialByIdFilialOrigem.idFilial = ? ")
				.append("AND (ff.servico.idServico = ? OR ff.servico.idServico IS NULL) ")
				.append("AND ff.dtVigenciaInicial <= ? ")
				.append("AND ff.dtVigenciaFinal >= ? ")
				.append("ORDER BY ")
				.append("ff.servico.idServico ASC ");

		List<FluxoFilial> result = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idMunicipio, idFilialOrigem, idServico, dtVigencia, dtVigencia});

		if(!result.isEmpty())
			return (FluxoFilial) result.get(0);
		else
			return null;
	}

	/**
	 * Retorna a Fluxo entre Filiais 
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @param idServico
	 * @return
	 */
	public FluxoFilial findUltimoFluxoFilial(Long idFilialOrigem, Long idFilialDestino, Long idServico) {
		DetachedCriteria dcMaxVigencia = DetachedCriteria.forClass(getPersistentClass(), "ffb");
		dcMaxVigencia.setProjection(Projections.max("ffb.dtVigenciaInicial"));
		dcMaxVigencia.add(Restrictions.eqProperty("ffb.filialByIdFilialDestino.id", "ffa.filialByIdFilialDestino.id"));
		dcMaxVigencia.add(Restrictions.eqProperty("ffb.filialByIdFilialOrigem.id", "ffa.filialByIdFilialOrigem.id"));
		if(idServico != null) {
			dcMaxVigencia.add(Restrictions.eqProperty("ffb.servico.id", "ffa.servico.id"));
		} else {
			dcMaxVigencia.add(Restrictions.isNull("ffb.servico.id"));
		}

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ffa");
		dc.add(Restrictions.eq("ffa.filialByIdFilialDestino.id", idFilialDestino));
		dc.add(Restrictions.eq("ffa.filialByIdFilialOrigem.id", idFilialOrigem));
		if(idServico != null) {
			dc.add(Restrictions.eq("ffa.servico.id", idServico));
		} else {
			dc.add(Restrictions.isNull("ffa.servico.id"));
		}
		dc.add(Property.forName("ffa.dtVigenciaInicial").eq(dcMaxVigencia));

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		if(!result.isEmpty())
			return (FluxoFilial) result.get(0);
		else
			return null;
	}
	
	
	/*
	 * Busca todos os Fluxos onde a reembarcadora é a origem até o destino desejado, ou seja,
	 * verifica se esse fluxo é reembarcador e se tem fluxos que o utilizam, vigentes para o periodo passado
	 */
	public Boolean findFluxosFilhosVigentes(Long idFilialOrigem, Long idFilialDestino, Long idServico,  YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
			SqlTemplate hql = new SqlTemplate();
	
			StringBuffer hqlFrom = new StringBuffer()
					.append(getPersistentClass().getName()).append(" FF ")
					.append(" inner join FF.filialByIdFilialOrigem as FO ")
					.append(" inner join FF.filialByIdFilialReembarcadora as FR ")
					.append(" inner join FF.filialByIdFilialDestino as FD ");
	
			hql.addFrom(hqlFrom.toString());
			hql.addCriteria("FR.idFilial", "=", idFilialOrigem);
			hql.addCriteria("FD.idFilial", "=", idFilialDestino);
			hql.addCriteria("FF.dtVigenciaFinal", ">", JTDateTimeUtils.maxYmd(dtVigenciaFinal));
			
			if (idServico != null) {
				hql.addCriteria("FF.servico.id","=", idServico);
			} else {
				hql.addCustomCriteria("FF.servico = null");
			}
			
			Integer count = getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());

			return count.intValue() > 0;
			

	}
	
	/*
	 * verifica se as rota diretas do fluxo a ser salvo está vigente para o intervalo do mesmo.
	 */
	public Boolean findFluxoPaiVigente(Long idFilialOrigem, Long idFilialReembarcadora, Long idServico,  YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){

		StringBuilder idServicoNotNull = new StringBuilder();
		StringBuilder idServicoNull = new StringBuilder(" and ff.servico.idServico is null ");

		StringBuilder hql = new StringBuilder(" select ff from FluxoFilial ff");
		hql.append(" where ff.filialByIdFilialOrigem.idFilial = " + idFilialOrigem);
		hql.append(" and ff.filialByIdFilialDestino.idFilial = " + idFilialReembarcadora);
		hql.append(" and ff.dtVigenciaInicial <= to_date('" + dtVigenciaInicial.toString() + "','yyyy/MM/dd')");
		hql.append(" and ff.dtVigenciaFinal >= to_date('" + JTDateTimeUtils.maxYmd(dtVigenciaFinal) + "','yyyy/MM/dd')");
		
		if(idServico != null){
			idServicoNotNull.append(" and ff.servico.idServico = " + idServico) ;
		}
		
		int count; 

		if(StringUtils.isNotBlank(idServicoNotNull.toString())){
			count = getAdsmHibernateTemplate().getRowCountForQuery(hql.toString() + idServicoNotNull);
			if( count > 0 ){
				return true;
		}
		}
		
		count = getAdsmHibernateTemplate().getRowCountForQuery(hql.toString() + idServicoNull);
		return  count > 0;
}
	
    /**
     * Método responsável por verificar se durante a atualização do fluxo de filial foi alterada a data de vigência final.
     * 
     * @param idFluxoFilial Id do fluxo filial.
     * @param dtVigenciaFinal Data de vigência final.
     * 
     * @return Retorna true se a data de vigência final for alterada. Caso contrário, false.
     */
    public Boolean isAtualizacaoDtVigenciaFinal(Long idFluxoFilial, YearMonthDay dtVigenciaFinal) {
        if (idFluxoFilial == null) {
            return Boolean.FALSE;
        }
        
        StringBuilder sql = new StringBuilder();
        
        sql.append("select new map(ff.dtVigenciaFinal as dtVigenciaFinal) from FluxoFilial ff");
        sql.append(" where ff.idFluxoFilial = ").append(idFluxoFilial);
        
        List<Map<String, Object>> retorno = getHibernateTemplate().find(sql.toString());
        
        if (retorno == null || retorno.isEmpty()) {
            return Boolean.FALSE;
        }
        
        YearMonthDay dtVigenciaFinalOriginal = (YearMonthDay) retorno.get(0).get("dtVigenciaFinal");
        
        dtVigenciaFinalOriginal = JTDateTimeUtils.maxYmd(dtVigenciaFinalOriginal);
        dtVigenciaFinal = JTDateTimeUtils.maxYmd(dtVigenciaFinal);
        
        return dtVigenciaFinalOriginal.compareTo(dtVigenciaFinal) != 0;
    }
    
    /**
     * Método responsável por verificar se existem vigentes em uso.
     * 
     * @param idFluxoFilial Id do fluxo da filial de origem.
     * @param idServico Id do serviço.
     * @param dtVigenciaFinal Data de vigência final do fluxo da filial.
     * 
     * @return True se existirem fluxos em uso. Caso contrário, false.
     */
    public List<String> hasFluxosAnterioresEmUso(Long idFluxoFilial, Long idServico, YearMonthDay dtVigenciaFinal) {
        StringBuilder sql = mountSqlFluxosAnterioresEmUso(idServico, dtVigenciaFinal, idFluxoFilial);        
		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
        }
        
	private StringBuilder mountSqlFluxosAnterioresEmUso(Long idServico, YearMonthDay dtVigenciaFinal, Long idFluxoFilial) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT FF.DS_FLUXO_FILIAL FROM																					");
        sql.append(" (                                                                                                          	");
        sql.append("   select ',' || LISTAGG(o.ID_FILIAL, ',') WITHIN GROUP (ORDER BY o.nr_ordem) || ',' filiais,                   ");
        sql.append("   o.ID_FLUXO_FILIAL, ff.DS_FLUXO_FILIAL from LMS_PD.ORDEM_FILIAL_FLUXO o                                                       	");
        sql.append("   inner join fluxo_filial ff on ff.ID_FLUXO_FILIAL = o.ID_FLUXO_FILIAL                                     	");
        sql.append(" where 1 = 1");
        sql.append("  and ff.dt_vigencia_final > to_date('").append(JTDateTimeUtils.maxYmd(dtVigenciaFinal)).append("', 'yyyy/MM/dd')");

        if (idServico == null) {
            sql.append("  and ff.id_servico is null");
        } else {
            sql.append("  and ff.id_servico = ").append(idServico);
        }

        sql.append("   and ff.ID_FLUXO_FILIAL <> ").append(idFluxoFilial);
        sql.append("   group by o.ID_FLUXO_FILIAL, ff.DS_FLUXO_FILIAL                                                                               ");
        sql.append(" ) FF                                                                                                       ");
        sql.append(" WHERE FF.FILIAIS LIKE ").append(getSQLToFindIdFiliaisAgrupadasPorIdFluxoFilial(idFluxoFilial));
        return sql;
    }
    
	private String getSQLToFindIdFiliaisAgrupadasPorIdFluxoFilial(Long idFluxoFilial) {
		return "'%' || (select ',' || LISTAGG(o.ID_FILIAL, ',') WITHIN GROUP (ORDER BY o.nr_ordem) || ',' filiais  " + 
		       " from LMS_PD.ORDEM_FILIAL_FLUXO o where ID_FLUXO_FILIAL = " + idFluxoFilial + ") || '%'";
        }

	/*
	 * Retorna o prazo total, onde a reembarcadora é origem até o destino desejado, dentro do intervalo de vigencia.
	 */
	public List findNrPrazoTotal(Long idFilialReembarcadora, Long idFilialDestino, Long idServico, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("FF.nrPrazoTotal");

		StringBuffer hqlFrom = new StringBuffer()
		.append(getPersistentClass().getName()).append(" FF ")
		.append(" inner join FF.filialByIdFilialOrigem as FO ")
		.append(" left join FF.filialByIdFilialReembarcadora as FR ")
		.append(" inner join FF.filialByIdFilialDestino as FD ")
		.append("  left join FF.servico as S ");

		hql.addFrom(hqlFrom.toString());

		StringBuffer sqlCriteria = new StringBuffer()
			.append(" FO.idFilial=? and FD.idFilial=? and FF.dtVigenciaInicial <=? and FF.dtVigenciaFinal >= ? ");

		hql.addCustomCriteria(sqlCriteria.toString());
		hql.addCriteriaValue(idFilialReembarcadora);
		hql.addCriteriaValue(idFilialDestino);
		hql.addCriteriaValue(dtVigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(dtVigenciaFinal));

		if (idServico != null) {
			hql.addCriteria("S.id","=",idServico);
		}
						
		return getAdsmHibernateTemplate().find(hql.getSql(true), hql.getCriteria());
				
	}

	public List<FluxoFilial> findFluxoByFilialReembarcadora(Filial filialReembarcadora) {
		StringBuffer hql = new StringBuffer();
		hql.append("select flfi ");
		hql.append("from FluxoFilial as flfi ");
		hql.append("where flfi.filialByIdFilialReembarcadora = ? ");
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{filialReembarcadora});
	}

	public List findFluxoFiliaisByOrigemCopia(YearMonthDay dtVigenciaInicial, FluxoFilial fluxoFilialOrigem, FluxoFilial fluxoFilialClone) {

		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		
		hql.append("select new map(");
		hql.append("(select count( * ) ");
        hql.append("	from OrdemFilialFluxo orffo ");
        hql.append("	where orffo.fluxoFilial.idFluxoFilial = flfi.idFluxoFilial) as qtFluxo,");		
        hql.append(" flfi as keyFluxoFilial) ");
		hql.append("from FluxoFilial as flfi ");
		hql.append("join flfi.filialByIdFilialReembarcadora as fire ");
		hql.append("where :dtVigenciaInicial between flfi.dtVigenciaInicial and flfi.dtVigenciaFinal ");
		hql.append("and flfi.idFluxoFilial not in (:idFluxoFilialOrigem) ");
		params.put("idFluxoFilialOrigem", Arrays.asList(fluxoFilialOrigem.getIdFluxoFilial(), fluxoFilialClone.getIdFluxoFilial()));
		
		if (fluxoFilialOrigem.getServico() != null) {
			hql.append("and flfi.servico = :servico ");
			params.put("servico", fluxoFilialOrigem.getServico());
		} else {
			hql.append( "and flfi.servico is null ");
		}
		
		params.put("dtVigenciaInicial", dtVigenciaInicial);
		params.put("filialOrigem", fluxoFilialOrigem.getFilialByIdFilialOrigem());
		params.put("filialDestino", fluxoFilialOrigem.getFilialByIdFilialDestino());
		
		if (fluxoFilialOrigem.getFilialByIdFilialReembarcadora() != null) {
			hql.append("and exists( ");
			hql.append("		select 1 from OrdemFilialFluxo orffo ");
			hql.append("		where orffo.fluxoFilial = flfi ");
			hql.append("		and orffo.filial = :filialOrigem ");
			hql.append("		and exists ( ");
			hql.append("				select 1 from OrdemFilialFluxo orffd ");
			hql.append("				where orffd.fluxoFilial = orffo.fluxoFilial ");
			hql.append("				and orffd.filial = :filialDestino ");
			hql.append("				and orffd.nrOrdem > orffo.nrOrdem ");
			hql.append("				and exists ( ");
			hql.append("						select 1 from OrdemFilialFluxo orffr ");
			hql.append("						where orffr.fluxoFilial = orffd.fluxoFilial ");
			hql.append("						and orffr.filial = :filialReembarcadora ");
			hql.append("						and orffr.nrOrdem between orffo.nrOrdem and orffd.nrOrdem "); 
			hql.append("				) ");
			hql.append("		)  ");
			hql.append(") ");
			params.put("filialReembarcadora", fluxoFilialOrigem.getFilialByIdFilialReembarcadora());
		} else {
			hql.append(" and exists( ");
			hql.append(" 		select 1 from OrdemFilialFluxo orffo ");
			hql.append(" 		where orffo.fluxoFilial = flfi ");
			hql.append(" 		and orffo.filial = :filialOrigem ");
			hql.append(" 		and exists ( ");
			hql.append(" 				select 1 from OrdemFilialFluxo orffd ");
			hql.append(" 				where orffd.fluxoFilial = orffo.fluxoFilial ");
			hql.append(" 				and orffd.filial = :filialDestino ");
			hql.append(" 				and orffd.nrOrdem = orffo.nrOrdem + 1 ");
			hql.append(" 		) ");
			hql.append(" ) ");
		}
		
		hql.append("order by ");
		hql.append("1,flfi.dsFluxoFilial");
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}

	/**
	 * 
	 * @param idOrigem
	 * @param idDestino
	 * @param dtInicioVigencia
	 * @return FluxoFilial
	 */
	public List<FluxoFilial> findDistanciaByIdOrigemDestinoVigencia(long idOrigem, long idDestino, YearMonthDay dtInicioVigencia) {

		StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		
		sql.append(" select ff 	");
		sql.append(" FROM " + FluxoFilial.class.getName() + " ff 	");
		sql.append(" inner join ff.filialByIdFilialDestino as fd 	");
		sql.append(" inner join ff.filialByIdFilialOrigem as fo 	");
		sql.append(" WHERE fo.idFilial = ?							");
		sql.append("   and fd.idFilial= ?							");
		sql.append("   and ff.dtVigenciaInicial <= ?				");
		sql.append("   and ff.dtVigenciaFinal 	>= ?				");

		
		param.add(idOrigem);
		param.add(idDestino);
		param.add(dtInicioVigencia);
		param.add(dtInicioVigencia);

		return (List<FluxoFilial>)getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		
	}
}
