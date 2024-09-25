package com.mercurio.lms.configuracoes.model.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
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
public class TelefoneEnderecoDAO extends BaseCrudDao<TelefoneEndereco, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TelefoneEndereco.class;
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {	
		lazyFindLookup.put("pessoa", FetchMode.JOIN);		
		lazyFindLookup.put("enderecoPessoa", FetchMode.JOIN);
		lazyFindLookup.put("enderecoPessoa.tipoLogradouro", FetchMode.JOIN);		
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("enderecoPessoa", FetchMode.JOIN);
		lazyFindPaginated.put("enderecoPessoa.tipoLogradouro", FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("enderecoPessoa", FetchMode.JOIN);
		lazyFindById.put("enderecoPessoa.tipoLogradouro", FetchMode.JOIN);
		lazyFindById.put("pessoa", FetchMode.JOIN);
	}

	/**
	 * Método que busca os telefones do endereco ordenados por tipo do telefone
	 * tipo de uso do telefone e número do telefone.
	 * @param criteria Filtros da tela
	 * @param findDef Dados de paginação
	 * @return ResultSetPage Contendo dados de retorno da pesquisa e de paginação
	 */
	public ResultSetPage findPaginated(TypedFlatMap map) {
		FindDefinition findDef = FindDefinition.createFindDefinition(map);

		SqlTemplate sql = mountSqlTelaTelefone(map);

		sql.addProjection(" te ");

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd1.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd2.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy("te.nrTelefone");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

	public List<TelefoneEndereco> findByEnderecoPessoa(Long idPessoa, Long idEnderecoPessoa) {
		
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom( DomainValue.class.getName() + " vd1 ");
		sql.addFrom( DomainValue.class.getName() + " vd2 ");

		StringBuffer joins = new StringBuffer()
			.append(" join vd1.domain d1 ")
			.append(" join vd2.domain d2 ")
			.append(" inner join te.pessoa p ")
			.append(" left outer join te.enderecoPessoa ep ");

		sql.addFrom( getPersistentClass().getName() + " te " + joins.toString() );
		sql.addJoin("vd1.value","te.tpTelefone");
		sql.addJoin("vd2.value","te.tpUso");

		sql.addCriteria("d1.name","=","DM_TIPO_TELEFONE");
		sql.addCriteria("d2.name","=","DM_USO_TELEFONE");
		
		sql.addCriteria("p.id","=",idPessoa);
		if (idEnderecoPessoa == null) {
			sql.addCustomCriteria("ep is null");
		}
		sql.addCriteria("ep.id", "=",idEnderecoPessoa);
		
		sql.addProjection(" te ");
		
		
		
		
		
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd1.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd2.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy("te.nrTelefone");
		
		return getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = mountSqlTelaTelefone(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	private SqlTemplate mountSqlTelaTelefone(TypedFlatMap map) {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom( DomainValue.class.getName() + " vd1 ");
		sql.addFrom( DomainValue.class.getName() + " vd2 ");

		StringBuffer joins = new StringBuffer()
			.append(" join vd1.domain d1 ")
			.append(" join vd2.domain d2 ")
			.append(" inner join te.pessoa p ")
			.append(" left outer join te.enderecoPessoa ep ");

		sql.addFrom( getPersistentClass().getName() + " te " + joins.toString() );
		sql.addJoin("vd1.value","te.tpTelefone");
		sql.addJoin("vd2.value","te.tpUso");

		sql.addCriteria("d1.name","=","DM_TIPO_TELEFONE");
		sql.addCriteria("d2.name","=","DM_USO_TELEFONE");
		sql.addCriteria("te.tpTelefone","=",(map.getString("tpTelefone")));
		sql.addCriteria("te.tpUso","=",(map.getString("tpUso")));

		sql.addCriteria("p.id","=",map.getLong("pessoa.idPessoa"));
		sql.addCriteria("ep.id","=",map.getLong("enderecoPessoa.idEnderecoPessoa"));
		sql.addCriteria("te.nrDdi","like",map.getString("nrDdi"));
		sql.addCriteria("te.nrDdd","like",map.getString("nrDdd"));
		sql.addCriteria("te.nrTelefone","like",map.getString("nrTelefone"));
		return sql;
	}

	/**
	 * Retorna uma estância de telefone da pessoa informado, por a data informada e o tipo de endereço informado.
	 * 
	 * @param Long idPessoa
	 * @param String tpEndereco
	 * @param Date dataVigencia
	 * @return TelefoneEndereco telefoneEndereco 
	 * */
	public TelefoneEndereco findTelefoneEnderecoByEnderecoPessoa(Long idPessoa, String tpEndereco, YearMonthDay dataVigencia) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("te");
		sql.addFrom(TelefoneEndereco.class.getName()+" te join te.enderecoPessoa ep");
		sql.addFrom(TipoEnderecoPessoa.class.getName()+" tep");
		sql.addJoin("tep.enderecoPessoa","ep");
		sql.addCriteria("ep.pessoa.id","=",idPessoa);
		sql.addCriteria("tep.tpEndereco","like",tpEndereco);

		sql.addCustomCriteria("( ? >= ep.dtVigenciaInicial AND ep.dtVigenciaFinal >= ? )");
		if (dataVigencia == null) {
			dataVigencia = JTDateTimeUtils.getDataAtual();
		}

		sql.addCriteriaValue(dataVigencia);
		sql.addCriteriaValue(dataVigencia);

		sql.addOrderBy("ep.dtVigenciaInicial asc");
		
		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (!list.isEmpty()) {
			return (TelefoneEndereco) list.get(0);
		} else {
			return null;
		}
   }
   
	/**
	 * Busca um TelefoneEndereco com base no idEnderecoPessoa e tpEndereco
	 * @param idEnderecoPessoa
	 * @param tpEndereco
	 * @return
	 */
	public TelefoneEndereco findTelefoneEnderecoPedidoColeta(Long idEnderecoPessoa, String tpEndereco) {
		SqlTemplate sql = new SqlTemplate();
   
		sql.addProjection("te");
		sql.addFrom(TelefoneEndereco.class.getName() + " te");
		sql.addFrom(TipoEnderecoPessoa.class.getName() + " tep");
		sql.addFrom(EnderecoPessoa.class.getName() + " ep");
		sql.addCustomCriteria("ep.id = tep.enderecoPessoa.id");
		sql.addCustomCriteria("ep.id = te.enderecoPessoa.id");
		sql.addCriteria("ep.id", "=", idEnderecoPessoa);
		if (StringUtils.isNotBlank(tpEndereco)) {
			sql.addCriteria("tep.tpEndereco", "=", tpEndereco);
		}
		sql.addCustomCriteria("( ? >= ep.dtVigenciaInicial AND ep.dtVigenciaFinal >= ? )");
		YearMonthDay dataVigencia = JTDateTimeUtils.getDataAtual();
		sql.addCriteriaValue(dataVigencia);
		sql.addCriteriaValue(dataVigencia);
   
		sql.addOrderBy("te.id desc");

		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (!list.isEmpty()) {
			return (TelefoneEndereco) list.get(0);
		} else {
			return null;
		}
	}
   
   /**
    * Método que busca o DDD e o número do telefone, 
    * a partir da Pessoa, do tipo de telefone e o endereco fornecido
    * 
    * autor Pedro Henrique Jatobá
    * 27/12/2005
    * @param idPessoa
    * @param tpEndereco
    * @param dataVigencia
    * @return
    */
   public Map findTelefoneEnderecoByPessoaTelefoneEnderecoPessoa(Long idPessoa, String tpTelefone, Long idEnderecoPessoa) {
		ProjectionList proj = Projections.projectionList()
			.add(Projections.property("te.nrTelefone"),"nrTelefone")
			.add(Projections.property("te.nrDdd"),"nrDdd");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "te");
		dc.setProjection(proj);
		dc.add(Restrictions.eq("te.pessoa.id",idPessoa));
		if(idEnderecoPessoa != null) {
			dc.add(Restrictions.eq("te.enderecoPessoa.id",idEnderecoPessoa));
		}
		dc.add(Restrictions.eq("te.tpTelefone",tpTelefone));
		dc.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		List l = findByDetachedCriteria(dc);
		if (!l.isEmpty())
			return (Map) l.get(0);

		return Collections.EMPTY_MAP;
	}

	/**
	 * Método que busca o DDD e o número do telefone, 
	 * a partir da Pessoa, do tipo de telefone
	 * 
	 */
	public TypedFlatMap findByIdPessoaTpTelefone(Long idPessoa, String tpTelefone, String tpUso) {
		TypedFlatMap retorno = null;
		ProjectionList proj = Projections.projectionList()
			.add(Projections.property("te.nrTelefone"),"nrTelefone")
			.add(Projections.property("te.nrDdd"),"nrDdd");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "te")
			.setProjection(proj)
			.add(Restrictions.eq("te.pessoa.id",idPessoa))
			.add(Restrictions.eq("te.tpTelefone",tpTelefone))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		
		if (StringUtils.isNotBlank(tpUso)){
			dc.add(Restrictions.eq("te.tpUso",tpUso));
		}
		List l = findByDetachedCriteria(dc);
		if (!l.isEmpty())
			retorno = (TypedFlatMap) l.get(0);
		return retorno;
	}

	/**
	* Retorna o primeiro telefone da pessoa informado, ordenado por tipo de telefone e número de telefone.
	* 
	* @param Long idPessoa
	* @return TelefoneEndereco telefoneEndereco 
	* */
	public TelefoneEndereco findTelefoneEnderecoByPessoa(Long idPessoa) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" te ");

		sql.addFrom( DomainValue.class.getName() + " vd1 ");
		sql.addFrom( getPersistentClass().getName() + " te join vd1.domain d1 ");

		sql.addJoin("vd1.value","te.tpTelefone");

		sql.addCriteria("d1.name","=","DM_TIPO_TELEFONE");
		sql.addCriteria("te.pessoa.id","=",idPessoa);

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd1.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy("te.nrTelefone");

		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (!list.isEmpty()) {
			return (TelefoneEndereco) list.get(0);
		} else {
			return null;
		}
   }
   
   /**
    * Retorna a lista de telefones da pessoa informada
    * 
    * author Mickaël Jalbert
    * @since 21/08/2006
    * 
    * @param Long idPessoa
    * @return List
    * */
   public List findByIdPessoa(Long idPessoa) {
		SqlTemplate sql = new SqlTemplate();
		
        sql.addProjection("te");        

        sql.addInnerJoin(TelefoneEndereco.class.getName(), "te");

		sql.addCriteria("te.pessoa.id","=",idPessoa);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorno o telefone padrão do endereço informado com o tipo de uso.
	 * 
	 * @param Long idEnderecoPessoa
	 * @param String tpUso
	 * @param String tpTelefone
	 * @return TelefoneEndereco Telefone do endereco informado
	 * */
	public TelefoneEndereco findTelefoneEnderecoPadraoPorTpUso(Long idEnderecoPessoa, String tpUso, String tpTelefone) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("te");

		sql.addFrom(getPersistentClass().getName(),"te");

		sql.addCriteria("te.enderecoPessoa.id","=",idEnderecoPessoa);
		sql.addCriteria("te.tpUso","=", tpUso);
		if(StringUtils.isNotBlank(tpTelefone)) {
			sql.addCriteria("te.tpTelefone", "=", tpTelefone);
		}

		sql.addOrderBy("te.nrTelefone");

		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (!list.isEmpty()) {
			return (TelefoneEndereco) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Método que monta o SQL para pesquisa de telefone endereço
	 *
	 * @param criteria
	 * @return
	 */
	private SqlTemplate mountSqlTemp(TypedFlatMap criteria){
		SqlTemplate sqlTemp = new SqlTemplate();

		sqlTemp.addFrom(TelefoneEndereco.class.getName() + " te join fetch te.pessoa p " +
														" join fetch te.enderecoPessoa ep " +
														" join fetch ep.municipio m " +
														" join fetch m.unidadeFederativa uf ");

		if(criteria.getLong("pessoa.idPessoa") != null) {
			sqlTemp.addCriteria("p.id", "=", criteria.getLong("pessoa.idPessoa"));
		}
		if(criteria.getLong("enderecoPessoa.idEnderecoPessoa") != null) {
			sqlTemp.addCriteria("ep.id", "=", criteria.getLong("enderecoPessoa.idEnderecoPessoa"));
		}
		if(!criteria.getString("tpUso").equals("")) {
			sqlTemp.addCriteria("te.tpUso", "=", criteria.getString("tpUso"));
		}
		if(!criteria.getString("tpTelefone").equals("")) {
			sqlTemp.addCriteria("te.tpTelefone", "=", criteria.getString("tpTelefone"));
		}
		if(!criteria.getString("nrDdi").equals("")) {
			sqlTemp.addCriteria("te.nrDdi", "=", criteria.getString("nrDdi"));
		}
		if(!criteria.getString("nrDdd").equals("")) {
			sqlTemp.addCriteria("te.nrDdd", "=", criteria.getString("nrDdd"));
		}
		if(!criteria.getString("nrTelefone").equals("")) {
			sqlTemp.addCriteria("te.nrTelefone", "like", criteria.getString("nrTelefone"));
		}		

		return sqlTemp;
	}

	/**
	 * Método que busca a paginação de telefone endereço.
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedTelefoneEndereco(TypedFlatMap criteria, FindDefinition findDefinition) {
		SqlTemplate sqlTemp = mountSqlTemp(criteria);
		return getAdsmHibernateTemplate().findPaginated(sqlTemp.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sqlTemp.getCriteria());
	}
	
	/**
	 * Método que busca a quantidade de telefone endereço.
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountTelefoneEndereco(TypedFlatMap criteria, FindDefinition findDefinition) {
		SqlTemplate sqlTemp = mountSqlTemp(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sqlTemp.getSql(false), sqlTemp.getCriteria());	
	}

	/**
	 * Carrega o TelefoneEndereco de acordo com os filtros
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/12/2006
	 *
	 * @param idPessoa
	 * @param ddd
	 * @param nrTelefone
	 * @return
	 *
	 */
	public TelefoneEndereco findByDddNrTelefone( Long idPessoa, String ddd, String nrTelefone ){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" te ");

		hql.addInnerJoin( getPersistentClass().getName() + " te ");
		hql.addInnerJoin("te.pessoa p ");

		hql.addCriteria("p.idPessoa", "=", idPessoa);
		hql.addCriteria("te.nrDdd", "=", ddd);
		hql.addCriteria("te.nrTelefone", "=", nrTelefone);

		return (TelefoneEndereco) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public List<Map<String, Object>> findTelefonePessoa(Long idPessoa) {
		
		SqlTemplate sql = new SqlTemplate();
		
        sql.addProjection("distinct new Map(te.idTelefoneEndereco as idTelefone");
        sql.addProjection("'('|| te.nrDdd || ') ' || te.nrTelefone as dddTelefone");        
        sql.addProjection("te.nrTelefone as nrTelefone");        
        sql.addProjection("te.tpTelefone as tpTelefone");        
        sql.addProjection("te.tpUso as tpUso)");        

        sql.addFrom(TelefoneEndereco.class.getName(), "te");

		sql.addCriteria("te.pessoa.id","=",idPessoa);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	
	public TelefoneEndereco findByPessoaEnderecoVigente(Pessoa pessoa){
		String sql = "select te from TelefoneEndereco te "
				+ " where "
				+ "	te.enderecoPessoa.pessoa = :pessoa"
				+ " and te.enderecoPessoa.dtVigenciaInicial <= :dtVigenciaInicio "
				+ "	and (te.enderecoPessoa.dtVigenciaFinal >= :dtVigenciaFim or te.enderecoPessoa.dtVigenciaFinal is null)";
		
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("pessoa", pessoa);
		parameters.put("dtVigenciaInicio",JTDateTimeUtils.getDataAtual());
		parameters.put("dtVigenciaFim",JTDateTimeUtils.getDataAtual());
		
		List<TelefoneEndereco> telefonesEnderecos = getAdsmHibernateTemplate().findByNamedParam(sql, parameters);
		if (telefonesEnderecos != null && telefonesEnderecos.size()>0){
			return telefonesEnderecos.get(0);
		}
		return null;
	}
}