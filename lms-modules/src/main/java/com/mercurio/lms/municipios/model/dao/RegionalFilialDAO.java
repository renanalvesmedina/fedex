package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.RegionalFilial;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegionalFilialDAO extends BaseCrudDao<RegionalFilial, Long> {
	
	
	public List findLookupRegionalFilial(TypedFlatMap criterions) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(rf.idRegionalFilial as idRegionalFilial, f.idFilial as idFilial, f.sgFilial as sgFilial, p.nmFantasia as nmFantasia, r.idRegional as idRegional  )");

		sql.addFrom(RegionalFilial.class.getName(), 
				new StringBuffer("rf ")
					.append(" join rf.regional r ")
					.append(" join rf.filial f ")
					.append(" join f.pessoa p ")
					.toString()
		);
		
		sql.addCriteria("upper(f.sgFilial)","like",criterions.get("sgFilial").toString().toUpperCase());
		if(criterions.getLong("regional.idRegional")!= null)
			sql.addCriteria("r.idRegional","=",criterions.getLong("regional.idRegional"));
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return RegionalFilial.class;
	}

	public Regional findLastRegionalVigente(Long idFilial) {
		Regional regional = null;
		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "rf");
		dc.createAlias("rf.regional", "r");
		dc.add(Restrictions.eq("rf.filial.id", idFilial));
		dc.add(Restrictions.le("rf.dtVigenciaInicial", dtToday));
		dc.add(Restrictions.or(Restrictions.gt("rf.dtVigenciaFinal", dtToday), Restrictions.isNull("rf.dtVigenciaFinal")));

		RegionalFilial regionalFilial = (RegionalFilial)getAdsmHibernateTemplate().findUniqueResult(dc);
		if(regionalFilial != null) {
			regional = regionalFilial.getRegional();
		}
		return regional;
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("regional", FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("regional", FetchMode.JOIN);
		fetchModes.put("regional.usuario", FetchMode.JOIN);
	}
	
	//Uma regional não pode possuir duas filiais sedes vigentes. Caso ocorra essa situação a seguinte mensagem deve ser exibida LMS-29009.
	public boolean verificaFiliaisSedeVigentes(RegionalFilial regionalFilial){
		DetachedCriteria dc = createDetachedCriteria();

		if(regionalFilial.getIdRegionalFilial() != null){
			dc.add(Restrictions.ne("idRegionalFilial",regionalFilial.getIdRegionalFilial()));
		}

		dc.add(Restrictions.eq("blSedeRegional", Boolean.TRUE));

		//datas de vigencia
		dc = JTVigenciaUtils.getDetachedVigencia(dc,regionalFilial.getDtVigenciaInicial(),regionalFilial.getDtVigenciaFinal());

		//regional
		DetachedCriteria dcRegional= dc.createCriteria("regional");
		dcRegional.add(Restrictions.eq("idRegional", regionalFilial.getRegional().getIdRegional()));

		return findByDetachedCriteria(dcRegional).size()>0;
	}

	//Uma filial só pode estar vigente em uma regional LMS-00003.
	public boolean verificaFilialVigente(RegionalFilial regionalFilial){
		DetachedCriteria dc = createDetachedCriteria();
		//se for uma alteracao
		if(regionalFilial.getIdRegionalFilial() != null){
			dc.add(Restrictions.ne("idRegionalFilial",regionalFilial.getIdRegionalFilial()));
		}
		//verifica as datas de vigencia
		dc = JTVigenciaUtils.getDetachedVigencia(dc,regionalFilial.getDtVigenciaInicial(),regionalFilial.getDtVigenciaFinal());

		//filial
		DetachedCriteria dcFilial = dc.createCriteria("filial");
		dcFilial.add(Restrictions.eq("idFilial", regionalFilial.getFilial().getIdFilial()));

		return findByDetachedCriteria(dcFilial).size()>0;
	}

	/**
	 * Busca a RegionalFilial Responsável, pelo Endereco da Pessoa, com MunicipioFilial e RegionalFilial vigentes.<BR>
	 * @author Robson Edemar Gehl
	 * @param tpEndereco
	 * @param blPadraoMcd
	 * @param dtVigencia
	 * @return
	 */
	public List findRegionalFilialResponsavel(Long idMunicipio, Boolean blPadraoMcd){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" rf ");

		sql.addFrom(RegionalFilial.class.getName(), 
				new StringBuffer("rf ")
					.append(" join fetch rf.regional r ")
					.append(" join fetch rf.filial f ")
					.append(" join f.municipioFiliais mf ")
					.toString()
		);

		sql.addCriteria("mf.municipio.idMunicipio","=",idMunicipio);

		sql.addCriteria("mf.blPadraoMcd","=", (blPadraoMcd != null && blPadraoMcd.booleanValue()) ? Boolean.TRUE : Boolean.FALSE);

		//Vigencia para MunicipioFilial
		sql.addCriteria("mf.dtVigenciaInicial","<=", JTDateTimeUtils.getDataAtual());
		sql.addCriteria("mf.dtVigenciaFinal",">=", JTDateTimeUtils.getDataAtual());

		//Vigencia para RegionalFilial
		sql.addCriteria("rf.dtVigenciaInicial","<=", JTDateTimeUtils.getDataAtual());
		sql.addCriteria("rf.dtVigenciaFinal",">=" , JTDateTimeUtils.getDataAtual());

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/* Não deve permitir alteração de datas de vigência da regional
	para datas fora dos intervalos dos
	registro filhos cadastrados em Regional filial. */
	public boolean findFilhosVigentesByVigenciaPai(Long idRegional, YearMonthDay dtInicioVigenciaPai,YearMonthDay dtFimVigenciaPai){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("regional.idRegional",idRegional));
		int total = findByDetachedCriteria(dc).size();
		if(total == 0)
			return false;
		if (dtInicioVigenciaPai != null)
			dc.add(Restrictions.lt("dtVigenciaInicial",dtInicioVigenciaPai));

		dc.add(Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtFimVigenciaPai)));

		return (findByDetachedCriteria(dc).size()>0);
	}

	public List findFilialByRegionalAndDate(Long idFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		StringBuffer sb = new StringBuffer();
		Object[] parameters = new Object[2];

		StringBuffer sbSub = new StringBuffer();
		sbSub.append("(SELECT MAX(HF2.idHistoricoFilial) FROM ").append(HistoricoFilial.class.getName()).append(" AS HF2 ")
				.append("WHERE HF2.filial = HF.filial)");

		sb.append("SELECT F.idFilial from ").append(Filial.class.getName()).append(" AS F ")
			.append("INNER JOIN F.historicoFiliais AS HF ")
			.append("WHERE HF.idHistoricoFilial = ").append(sbSub.toString()).append(" ")
			.append("AND F.idFilial = ? ");
		parameters[0] = idFilial;

		if (dtVigenciaInicial != null) {
			sb.append(" AND HF.dtRealOperacaoInicial < ? ");
			parameters[1] = dtVigenciaInicial;
		}

		if (dtVigenciaFinal != null) {
			sb.append(" AND HF.dtRealOperacaoFinal < ? ");
			parameters[1] = dtVigenciaFinal;
		}
		return getAdsmHibernateTemplate().find(sb.toString(),parameters);
	}

	public List findByIdPersonalizado(Long id){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom("RegionalFilial", "rf");
		hql.addCriteria("rf.idRegionalFilial", "=", id);
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findBySgFilialIdRegional(String sgFilial, Long idRegional){
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		DetachedCriteria dc = DetachedCriteria.forClass(RegionalFilial.class, "rf")
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("f.idFilial"), "idFilial")
				.add(Projections.property("f.sgFilial"), "sgFilial")
				.add(Projections.property("p.nmFantasia"), "nmFantasia")
				.add(Projections.property("r.idRegional"), "idRegional"))
		.createAlias("regional","r")
		.createAlias("filial","f")
		.createAlias("f.pessoa","p")
		.add(Restrictions.le("rf.dtVigenciaInicial", dataAtual))
		.add(Restrictions.ge("rf.dtVigenciaFinal", dataAtual))
		.add(Restrictions.ilike("f.sgFilial", sgFilial.toLowerCase(), MatchMode.ANYWHERE))
		.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		if(idRegional!=null){
			dc.add(Restrictions.eq("r.id", idRegional));
		}
		return findByDetachedCriteria(dc);
	}

	public List findBySgFilialIdRegionalUsuarioLogado(String sgFilial, Long idRegional) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		DetachedCriteria dc = DetachedCriteria.forClass(RegionalFilial.class, "rf")
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("f.idFilial"), "idFilial")
				.add(Projections.property("f.sgFilial"), "sgFilial")
				.add(Projections.property("p.nmFantasia"), "nmFantasia")
				.add(Projections.property("r.idRegional"), "idRegional"))
		.createAlias("regional","r")
		.createAlias("filial","f")
		.createAlias("f.pessoa","p")
		.add(Restrictions.le("rf.dtVigenciaInicial", dataAtual))
		.add(Restrictions.ge("rf.dtVigenciaFinal", dataAtual))
		.add(Restrictions.ilike("f.sgFilial", sgFilial.toLowerCase(), MatchMode.ANYWHERE))
		.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		if(idRegional!=null){
			dc.add(Restrictions.eq("r.id", idRegional));
		}

		List<Filial> filiais = SessionUtils.getFiliaisUsuarioLogado();
		if(filiais != null) {
			List<Long> idFiliais = new ArrayList<Long>(filiais.size());
			for(Filial filial : filiais) {
				idFiliais.add(filial.getIdFilial());
			}
			dc.add(Restrictions.in("f.idFilial", idFiliais));
		}

		return findByDetachedCriteria(dc);
	}

	public RegionalFilial findRegionalFilial(Long idFilial, YearMonthDay dtVigencia) {
		DetachedCriteria dc = DetachedCriteria.forClass(RegionalFilial.class, "rf")
		.add(Restrictions.eq("rf.filial.id", idFilial))
		.add(Restrictions.le("rf.dtVigenciaInicial", dtVigencia))
		.add(Restrictions.ge("rf.dtVigenciaFinal", dtVigencia));

		return (RegionalFilial)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef){
		SqlTemplate hql = montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria){
		SqlTemplate hql = montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}

	public SqlTemplate montaQueryPaginated(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(" +
				"rf.idRegionalFilial as idRegionalFilial, " +
				"rf.blSedeRegional as blSedeRegional, " +
				"rf.dtVigenciaInicial as dtVigenciaInicial, " +
				"rf.dtVigenciaFinal as dtVigenciaFinal, " +
				"re.sgRegional||' - '||re.dsRegional as siglaDescricaoRegional, " +
				"re.dtVigenciaInicial as dtVigenciaInicialRegional, " +
				"re.dtVigenciaFinal as dtVigenciaFinalRegional, " +
				"fil.sgFilial as sgFilial, " +
				"fil.idFilial as idFilial, " +
				"re.idRegional as idRegional, " +
				"pes.nmFantasia as nmFantasia)" );
		
		hql.addFrom("RegionalFilial rf " +
				"join rf.regional re " +
				"join rf.filial fil " +
				"join fil.pessoa pes ");

		hql.addCriteria("re.idRegional","=",criteria.getLong("regional.idRegional"));
		hql.addCriteria("fil.idFilial","=", criteria.getLong("filial.idFilial"));
		hql.addCriteria("rf.blSedeRegional", "=", criteria.getBoolean("blSedeRegional"));
		hql.addCriteria("rf.dtVigenciaInicial",">=",criteria.getYearMonthDay("dtVigenciaInicial"));
		hql.addCriteria("rf.dtVigenciaFinal","<=",criteria.getYearMonthDay("dtVigenciaFinal"));

		return hql; 
	}

	public Boolean findByIdRegionalAndIdEmpresa(java.lang.Long idRegional,java.lang.Long idEmpresa) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(RegionalFilial.class.getName() + " as rg " + 
			" join rg.regional as regional " + 
			" join rg.filial as filial " +
			" join filial.empresa as empresa " 
		);

		hql.addCriteria("rg.blSedeRegional","=",Boolean.TRUE);
		hql.addCriteria("empresa.idEmpresa","=",idEmpresa);		
		hql.addCriteria("regional.idRegional","=",idRegional);

		/* Monta os Campos da Clausula Select do HQL */
		String projection = "new Map('retorno' as retorno )";

		/* Adiciona a projection ao SqlTemplate */
		hql.addProjection(projection);	

		List result = getAdsmHibernateTemplate().find(hql.getSql(true),hql.getCriteria());
		if(result==null || result.size()==0) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	public List findByRegional(List regionais) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(RegionalFilial.class.getName() + " as rg" + 
			" join rg.regional as regional " + 
			" join rg.filial as filial "
		);

		hql = SQLUtils.joinExpressionsWithComma(regionais,hql,"regional");
		hql.addProjection(" filial ");

		List result = getAdsmHibernateTemplate().find(hql.getSql(true),hql.getCriteria());
		if (result==null || result.size()==0) {
			return null;
		} else {
			return result;
		}
	}

}