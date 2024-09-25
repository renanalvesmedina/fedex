package com.mercurio.lms.configuracoes.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicaï¿½ï¿½o
 * atravï¿½s do suporte ao Hibernate em conjunto com o Spring.
 * Nï¿½o inserir documentaï¿½ï¿½o apï¿½s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
@SuppressWarnings("deprecation")
public class EnderecoPessoaDAO extends BaseCrudDao<EnderecoPessoa, Long> {
	private static final String ENDERECO_TIPO_COLETA = "COL";

	/**
	 * Busca EnderecoPessoa com os atributos especï¿½ficos para o endereï¿½o completo, utilizado na Combo.<BR>
	 * <strong>ATENCAO:</strong> alias default: tipoLogradouro as tl
	 * @author Robson Edemar Gehl
	 * @param idCliente
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findEnderecoCompletoCombo(Map map){
		
		// LMS-1276: Estava fazendo um full scan na tabela por nao possuir
		// identificador de pessoa
		if (map == null || map.get("pessoa") == null) {
			return new ArrayList<Map<String, Object>>();
		}
		if (StringUtils.isBlank((String) ((Map) map.get("pessoa")).get("idPessoa"))) {
			return new ArrayList<Map<String, Object>>();
		}
			
		DetachedCriteria dc = createDetachedCriteria();

		dc.createAlias("tipoLogradouro", "tl");

		RestrictionsBuilder rb = new RestrictionsBuilder(EnderecoPessoa.class, true);
		rb.createDefaultBuilders(map);
		rb.createCriterions(map, Collections.EMPTY_LIST, dc);

		dc.createAlias("municipio", "m");
		dc.createAlias("m.unidadeFederativa", "uf");

		//*** Atributos necessario para o Endereco Completo
		ProjectionList projections = Projections.projectionList()
			.add(Projections.property("tl.dsTipoLogradouro"), "tipoLogradouro.dsTipoLogradouro")
			.add(Projections.property("idEnderecoPessoa"), "idEnderecoPessoa")
			.add(Projections.property("dsEndereco"), "dsEndereco")
			.add(Projections.property("nrEndereco"), "nrEndereco")
			.add(Projections.property("dsComplemento"), "dsComplemento")
			.add(Projections.property("m.nmMunicipio"), "municipio.nmMunicipio")
			.add(Projections.property("uf.sgUnidadeFederativa"), "municipio.unidadeFederativa.sgUnidadeFederativa");
		dc.setProjection(projections);

		dc.addOrder(OrderVarcharI18n.asc("tl.dsTipoLogradouro", LocaleContextHolder.getLocale()));
		dc.addOrder(Order.asc("dsEndereco"));
		dc.addOrder(Order.asc("nrEndereco"));
		dc.addOrder(Order.asc("dsComplemento"));
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(EnderecoPessoa.class));

		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Busca EnderecoPessoa com os atributos especï¿½ficos para o endereï¿½o completo, 
	 * vigente e vigente no futuro, utilizado na Combo.
	 * 
	 * @param Map map
	 * @return List
	 */
	@SuppressWarnings({ "rawtypes" })
	public List findEnderecoCompletoComboVigenteFuturo(Map map){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(tl.dsTipoLogradouro","tipoLogradouro_dsTipoLogradouro");
		sql.addProjection("en.idEnderecoPessoa","idEnderecoPessoa");
		sql.addProjection("en.dsEndereco","dsEndereco");
		sql.addProjection("en.nrEndereco","nrEndereco");
		sql.addProjection("en.dsComplemento","dsComplemento");
		sql.addProjection("m.nmMunicipio","municipio_nmMunicipio");
		sql.addProjection("uf.sgUnidadeFederativa","municipio_unidadeFederativa_sgUnidadeFederativa)");

		sql.addFrom(EnderecoPessoa.class.getName(), "en join en.municipio as m " +
				"join en.tipoLogradouro as tl " +
				"join m.unidadeFederativa as uf");

		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		if (map.get("idEnderecoPessoa") != null) {
			sql.addCustomCriteria("((en.dtVigenciaFinal >= ? or en.dtVigenciaFinal is null) or (en.idEnderecoPessoa = ? ))");
			sql.addCriteriaValue(Long.valueOf((String)map.get("idEnderecoPessoa")));	
		} else {
			sql.addCustomCriteria("((en.dtVigenciaFinal >= ? or en.dtVigenciaFinal is null))");
		}
		
		if (StringUtils.isNotBlank((String)map.get("notIdEnderecoPessoa"))){
			sql.addCriteria("en.idEnderecoPessoa", "!=", Long.valueOf((String)map.get("notIdEnderecoPessoa")));
		}

		String idPessoaStr = (String)map.get("idPessoa");
		Validate.notEmpty(idPessoaStr, "idPessoa ï¿½ obrigatï¿½rio");
		sql.addCriteria("en.pessoa.id","=", idPessoaStr, Long.class);
		
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tl.dsTipoLogradouro", LocaleContextHolder.getLocale()));
		sql.addOrderBy("en.dsEndereco");
		sql.addOrderBy("en.nrEndereco");
		sql.addOrderBy("en.dsComplemento");		

		List list = this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		return new AliasToNestedBeanResultTransformer(EnderecoPessoa.class).transformListResult(list);
	}

	/**
	 * Consulta todos os endereï¿½os da pessoa informada, comparando com a quantidade mï¿½nima.<BR>
	 * @author Robson Edemar Gehl
	 * @param pessoa
	 * @param quantidadeMinima
	 * @return Boolean.TRUE se quantidade de endereï¿½o pessoa >= quantidade mï¿½nima informada; Boolean.FALSE, caso contrï¿½rio.
	 */
	public Boolean validateQuantidadeMinima(Pessoa pessoa, long quantidadeMinima){
		Validate.notNull(pessoa, "pessoa cannot be null");
		Validate.notNull(pessoa.getIdPessoa(), "idPessoa cannot be null");
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("pessoa", "p");
		dc.add(Restrictions.eq("p.id", pessoa.getIdPessoa()));
		dc.setProjection(Projections.count("idEnderecoPessoa"));
		Integer count = (Integer) findByDetachedCriteria(dc).get(0);
		return count >= quantidadeMinima;
	}

	/**
	 * Verifica se o Endereï¿½o ï¿½ vï¿½lido, pelo tipo de endereï¿½o e data de vigï¿½ncia.<BR>
	 *@author Robson Edemar Gehl
	 * @param endereco
	 * @param tpEndereco
	 * @return Boolean.TRUE, endereï¿½o com data de vigencia vï¿½lida; Boolean.TRUE, invï¿½lida, existe outro registro do mesmo tpEndereco.
	 */
	public Boolean validateEnderecoByTpEndereco(EnderecoPessoa endereco, String tpEndereco){
		Validate.notEmpty(tpEndereco, "tpEndereco cannot be null");

		DetachedCriteria dc = DetachedCriteria.forClass(TipoEnderecoPessoa.class);
		dc.setProjection(Projections.count("idTipoEnderecoPessoa"));

		dc.createAlias("enderecoPessoa","endereco");
		dc.createAlias("endereco.pessoa","pessoa");

		Criterion critVigenciaInicial = Restrictions.and(
				Restrictions.le("endereco.dtVigenciaInicial",endereco.getDtVigenciaInicial()),
				Restrictions.ge("endereco.dtVigenciaFinal",endereco.getDtVigenciaInicial())
			);

		if (endereco.getDtVigenciaFinal() != null){
			//Pode nï¿½o ter vigencia final
			Criterion critVigenciaFinal = Restrictions.and(
					Restrictions.le("endereco.dtVigenciaInicial",endereco.getDtVigenciaFinal()),
					Restrictions.ge("endereco.dtVigenciaFinal",endereco.getDtVigenciaFinal())
				);
			dc.add(Restrictions.or(critVigenciaInicial, critVigenciaFinal));

		} else {
			dc.add( Restrictions.or( 
						critVigenciaInicial, 
						Restrictions.or(
								Restrictions.ge("endereco.dtVigenciaInicial", endereco.getDtVigenciaInicial()),
								Restrictions.and(
										Restrictions.isNull("endereco.dtVigenciaFinal"),
										Restrictions.le("endereco.dtVigenciaInicial", endereco.getDtVigenciaInicial())
									)
							)
					) );
		}

		dc.add(Restrictions.eq("tpEndereco", tpEndereco));
		dc.add(Restrictions.eq("pessoa.id", endereco.getPessoa().getIdPessoa()));
		if (endereco.getIdEnderecoPessoa() != null){
			dc.add(Restrictions.ne("endereco.id", endereco.getIdEnderecoPessoa()));	
		}

		Integer count = (Integer) findByDetachedCriteria(dc).get(0);
		return (count.intValue() > 0) ? Boolean.FALSE : Boolean.TRUE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("tipoLogradouro",FetchMode.JOIN);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoLogradouro",FetchMode.JOIN);
		lazyFindById.put("municipio",FetchMode.JOIN);
		lazyFindById.put("municipio.unidadeFederativa",FetchMode.JOIN);
		lazyFindById.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
		lazyFindById.put("pessoa",FetchMode.JOIN);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("municipio",FetchMode.JOIN);
		lazyFindPaginated.put("municipio.unidadeFederativa",FetchMode.JOIN);
		lazyFindPaginated.put("municipio.unidadeFederativa.regiaoGeografica",FetchMode.JOIN);
		lazyFindPaginated.put("tipoLogradouro",FetchMode.JOIN);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("tipoLogradouro",FetchMode.JOIN);	
	}

	/**
	 * Nome da classe que o DAO ï¿½ responsï¿½vel por persistir.
	 */
	@SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
		return EnderecoPessoa.class;
	}

	public EnderecoPessoa getUltimoEndereco(Long idPessoa) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ep");
		dc.createAlias("ep.municipio", "m");
		dc.createAlias("m.unidadeFederativa", "uf");
		dc.createAlias("uf.pais", "p");
		dc.add(Restrictions.eq("ep.pessoa.id", idPessoa));
		dc.add(Restrictions.le("ep.dtVigenciaInicial", dtToday));
		dc.add(Restrictions.or(Restrictions.eq("ep.dtVigenciaFinal", JTDateTimeUtils.MAX_YEARMONTHDAY), Restrictions.ge("ep.dtVigenciaFinal", dtToday)));

		return (EnderecoPessoa)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public EnderecoPessoa getPrimeiroEndereco(Long idPessoa) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		DetachedCriteria minId = createDetachedCriteria();
		minId.setProjection(Projections.min("idEnderecoPessoa"));
		minId.add(Property.forName("pessoa.id").eq(idPessoa));

		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Subqueries.propertyEq("id", minId));
		dc.createAlias("municipio", "m");
		dc.createAlias("m.unidadeFederativa", "uf");
		dc.createAlias("uf.pais", "p");
		dc.createAlias("p.zona", "z");

		return (EnderecoPessoa) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Realiza a ordenaï¿½ï¿½o da lista de endereï¿½os de Pessoas
	 * @param criterions Mapa de criterios vindo da tela
	 * @return List contendo os endereï¿½os encontrados
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findListByCriteria(Map criterions) {
		List<String> order = new ArrayList<String>();

		order.add("tipoLogradouro_.dsTipoLogradouro");
		order.add("dsEndereco");
		order.add("nrEndereco");
		order.add("dsComplemento");

		return super.findListByCriteria(criterions, order);
	}

	/**
	 * Retorna uma estï¿½ncia de endereï¿½o da pessoa informado, por a data atual e o tipo de endereï¿½o informado.
	 * 
	 * @param Long idPessoa
	 * @param String tpEndereco
	 * @return EnderecoPessoa enderecoPessoa 
	 * */
	public EnderecoPessoa findEnderecoPessoaSpecific(Long idPessoa, String tpEndereco) {
		return findEnderecoPessoaSpecific(idPessoa, tpEndereco, null);
	}

	/**
	 * Retorna uma estï¿½ncia de endereï¿½o da pessoa informado, por a data informada e o tipo de endereï¿½o informado.
	 * 
	 * @param Long idPessoa
	 * @param String tpEndereco
	 * @param Date dataVigencia
	 * @return EnderecoPessoa enderecoPessoa 
	 * */
	public EnderecoPessoa findEnderecoPessoaSpecific(Long idPessoa, String tpEndereco, YearMonthDay dataVigencia) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ep");
		sql.addFrom(TipoEnderecoPessoa.class.getName()+
				" tep join tep.enderecoPessoa ep " +
				"join fetch ep.municipio as mu " +
				"join fetch mu.unidadeFederativa as uf " +
				"join fetch uf.pais as pa " +
				"left outer join fetch pa.zona as zo " +
				"left outer join fetch ep.telefoneEnderecos as telefones " +
				"inner join fetch ep.tipoLogradouro as tipoLogradouro"
		);
		sql.addCriteria("ep.pessoa.idPessoa","=",idPessoa);
		sql.addCriteria("tep.tpEndereco","like",tpEndereco);
		sql.addCustomCriteria( " rownum < 2 " );

		if (dataVigencia == null) {
			dataVigencia = JTDateTimeUtils.getDataAtual();
		}
		sql.addCustomCriteria("( ? >= ep.dtVigenciaInicial AND ? <= ep.dtVigenciaFinal)");
		sql.addCriteriaValue(dataVigencia);
		sql.addCriteriaValue(dataVigencia);		
		sql.addOrderBy("ep.dtVigenciaInicial asc");

		return (EnderecoPessoa) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Busca o endereï¿½o que tenha vigï¿½ncia final menor que a passada por parï¿½metro, usado na exclusï¿½o
	 * @param idPessoa
	 * @param tpEndereco
	 * @param dtVigenciaFinal
	 * @return
	 */
	public EnderecoPessoa findEnderecoPessoaSubstituidoByDtVigencia(Long idPessoa, String tpEndereco, YearMonthDay dtVigencia) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("max(ep.dtVigenciaFinal)");
		sql.addFrom(TipoEnderecoPessoa.class.getName()+" tep join tep.enderecoPessoa ep ");
		sql.addCriteria("ep.pessoa.id","=",idPessoa);
		sql.addCriteria("tep.tpEndereco","like",tpEndereco);

		sql.addCriteria("ep.dtVigenciaFinal","<",dtVigencia);
		YearMonthDay dtVigenciaFinal = (YearMonthDay)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		if (dtVigenciaFinal != null) {
			sql = new SqlTemplate();

			sql.addProjection("ep");
			sql.addFrom(TipoEnderecoPessoa.class.getName()+" tep join tep.enderecoPessoa ep ");
			sql.addCriteria("ep.pessoa.id","=",idPessoa);
			sql.addCriteria("tep.tpEndereco","like",tpEndereco);

			sql.addCriteria("ep.dtVigenciaFinal","=",dtVigenciaFinal);
			return (EnderecoPessoa)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		} else return null;
	}

	/**
	 * Retorna uma estï¿½ncia de endereï¿½o da pessoa informado baseada no campo enderecoPessoa da tabela pessoa
	 * 
	 * @author Mickaï¿½l Jalbert
	 * @since 23/08/2006
	 * 
	 * @param Long idPessoa
	 * @return EnderecoPessoa enderecoPessoa 
	 * */
	public EnderecoPessoa findEnderecoPessoaPadrao(Long idPessoa) {
		SqlTemplate sql = getSqlTemplateDefault(idPessoa);
		return (EnderecoPessoa) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}

	public boolean hasEnderecoPessoaPadrao(Long idPessoa){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" count (ep)");

		sql.addInnerJoin(Pessoa.class.getName(), "pes");
		sql.addInnerJoin("pes.enderecoPessoa", "ep");
		sql.addCriteria("pes.id","=",idPessoa);
		
		Long count = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		
		return count > 0 ;
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/08/2007
	 *
	 * @param idPessoa
	 * @param sql
	 *
	 */
	private SqlTemplate getSqlTemplateDefault(Long idPessoa) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ep");

		sql.addInnerJoin(Pessoa.class.getName(), "pes");
		sql.addInnerJoin("pes.enderecoPessoa", "ep");
		sql.addInnerJoin("fetch ep.tipoLogradouro", "tipoLogradouro");
		sql.addInnerJoin("fetch ep.municipio", "mu");
		sql.addInnerJoin("fetch mu.unidadeFederativa", "uf");
		sql.addInnerJoin("fetch uf.pais", "pa");
		sql.addLeftOuterJoin("fetch pa.zona", "zo");
		sql.addLeftOuterJoin("fetch ep.telefoneEnderecos", "telefones");

		sql.addCriteria("pes.id","=",idPessoa);

		return sql;
	}	

	/**
	 * Busca o enderecoPessoa substituido.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/08/2007
	 *
	 * @param idPessoa
	 * @param dtVigenciaInicial
	 * @return
	 *
	 */
	public EnderecoPessoa findEnderecoSubstituido(Long idPessoa, YearMonthDay dtVigenciaInicial){
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		Validate.notNull(dtVigenciaInicial, "dtVigenciaInicial cannot be null");
		
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ep");

		sql.addInnerJoin(Pessoa.class.getName(), "pes");
		sql.addInnerJoin("pes.enderecoPessoas", "ep");
		sql.addInnerJoin("FETCH ep.tipoLogradouro", "tl");
		sql.addInnerJoin("ep.tipoEnderecoPessoas", "te");

		sql.addCriteria("pes.id","=",idPessoa);
		sql.addCriteriaIn("te.tpEndereco", new Object[]{"RES", "COM"});
		sql.addCriteria("trunc(ep.dtVigenciaInicial)", "<", dtVigenciaInicial);
		sql.addCustomCriteria("(ep.dtVigenciaFinal is null OR ep.dtVigenciaFinal = to_date('01/01/4000', 'dd/mm/yyyy'))");

		return (EnderecoPessoa) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		
	}

	@SuppressWarnings("rawtypes")
	public Map findEnderecoPessoaByPessoaTpEndereco(Long idPessoa, String tpEndereco) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		Validate.notEmpty(tpEndereco, "tpEndereco cannot be null");
		
		List l = AliasToNestedMapResultTransformer.getInstance()
		.transformListResult(getAdsmHibernateTemplate()
				.findByNamedQueryAndNamedParam(EnderecoPessoa.FIND_BY_ID_PESSOA_TP_ENDERECO, 
						new String[]{"idPessoa", "tpEndereco", "dataAtual"}, 
						new Object[]{idPessoa, tpEndereco, JTDateTimeUtils.getDataAtual()}));
		if(!l.isEmpty())
			return (Map)l.get(0);
		return null;	
	}

	@SuppressWarnings("rawtypes")
	public List findEnderecoPessoaByPessoaTpEnderecoLocalEntrega(List idPessoa, String tpEndereco) {
		Validate.notEmpty(idPessoa, "idPessoa cannot be null");
		Validate.notEmpty(tpEndereco, "tpEndereco cannot be null");
		
		return getAdsmHibernateTemplate()
				.findByNamedQueryAndNamedParam(EnderecoPessoa.FIND_BY_ID_PESSOA_TP_ENDERECO_LOCAL_ENTREGA, 
						new String[]{"idPessoa", "tpEndereco", "dataAtual"}, 
						new Object[]{idPessoa, tpEndereco, JTDateTimeUtils.getDataAtual()});		
	}

	@SuppressWarnings("rawtypes")
	public Map findEnderecoPessoaByPessoaPrioridade(Long idPessoa, String[] prioridade) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		StringBuffer hql = new StringBuffer("select new Map(uf.idUnidadeFederativa as municipio_unidadeFederativa_idUnidadeFederativa, ")
			.append("pais.idPais as municipio_unidadeFederativa_pais_idPais, ")
			.append("pais.nmPais as municipio_unidadeFederativa_pais_nmPais, ")
			.append("mun.nmMunicipio as municipio_nmMunicipio, ")
			.append("ep.idEnderecoPessoa as idEnderecoPessoa, ")
			.append("ep.nrCep as nrCep, ")
			.append("ep.dsEndereco as dsEndereco, ")
			.append("ep.nrEndereco as nrEndereco, ")
			.append("ep.dsBairro as dsBairro, ")
			.append("ep.dsComplemento as dsComplemento, ")
			.append("ep.tipoLogradouro.idTipoLogradouro as tipoLogradouro_idTipoLogradouro, ")
			.append("mun.idMunicipio as municipio_idMunicipio) ")
			.append("from  " + EnderecoPessoa.class.getName() + " as ep ")
			.append("left join ep.municipio as mun ")
			.append("left join mun.unidadeFederativa as uf ")
			.append("left join uf.pais as pais ")
			.append("left join ep.tipoEnderecoPessoas as tep ")
			.append("where  ep.pessoa.id = :idPessoa and tep.tpEndereco in (:tpEndereco) ")
			.append("and (ep.dtVigenciaInicial is null or ") 
			.append("( current_date() >= ep.dtVigenciaInicial AND ") 
			.append("(ep.dtVigenciaFinal IS NULL OR ep.dtVigenciaFinal >= current_date() )))");
		if(prioridade != null && prioridade.length > 0) {
			hql.append(" order by  case tep.tpEndereco");
			int i = 0;
			for (; i < prioridade.length; i++) {
				hql.append(" when '" + prioridade[i] + "' then " + i);
			}
			hql.append(" else " + i + " end");
		}

		List l = AliasToNestedMapResultTransformer.getInstance().transformListResult(getAdsmHibernateTemplate().findByNamedParam(hql.toString(), new String[]{"idPessoa", "tpEndereco"}, new Object[]{idPessoa, prioridade}));
		if(!l.isEmpty())
			return (Map)l.get(0);
		return null;	
	}

	/**
	 *  Busca os Endereï¿½os vigentes da pessoa passada por parï¿½metro.
	 *  Busca tambï¿½m um endereï¿½o nï¿½o vigente se este for passado por parï¿½metro.
	 *  @param criterios Critï¿½rios da pesquisa
	 *  @return List Lista contendo os enderecos vigentes da pessoa e/ou o endereï¿½o nï¿½o vigente se este foi passado por parï¿½metro
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findEnderecosPessoaVigentes(Map criterios) {
		DetachedCriteria dc = createDetachedCriteria();

		YearMonthDay ymdAtual = JTDateTimeUtils.getDataAtual();

		dc.createAlias("tipoLogradouro", "tl"); 

		String idEnderecoPessoa = MapUtils.getString(criterios, "idEnderecoPessoa");
		criterios.remove("idEnderecoPessoa");

		RestrictionsBuilder rb = new RestrictionsBuilder(EnderecoPessoa.class, true);
		rb.createDefaultBuilders(criterios);
		rb.createCriterions(criterios, Collections.EMPTY_LIST, dc);

		ProjectionList projections = Projections.projectionList()
		.add(Projections.property("tl.dsTipoLogradouro"), "tipoLogradouro.dsTipoLogradouro")
		.add(Projections.property("idEnderecoPessoa"), "idEnderecoPessoa")
		.add(Projections.property("dsEndereco"), "dsEndereco")
		.add(Projections.property("nrEndereco"), "nrEndereco")
		.add(Projections.property("dsComplemento"), "dsComplemento");

		dc.setProjection(projections);

		if( criterios != null && (idEnderecoPessoa != null && !idEnderecoPessoa.equals(""))){
			dc.add(Restrictions.or(
						Restrictions.and(Restrictions.le("dtVigenciaInicial", ymdAtual), Restrictions.ge("dtVigenciaFinal", ymdAtual)
					),
						Restrictions.eq("id",Long.valueOf(idEnderecoPessoa))
			));
		} else {
			dc.add(Restrictions.and(Restrictions.le("dtVigenciaInicial",ymdAtual), Restrictions.ge("dtVigenciaFinal", ymdAtual)));
		}

		dc.addOrder(OrderVarcharI18n.asc("tl.dsTipoLogradouro", LocaleContextHolder.getLocale()));
		dc.addOrder(Order.asc("dsEndereco"));
		dc.addOrder(Order.asc("nrEndereco"));
		dc.addOrder(Order.asc("dsComplemento"));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(EnderecoPessoa.class));
		return findByDetachedCriteria(dc);
	}
	
	@SuppressWarnings("rawtypes")
	public List findEnderecosVigentesByIdPessoa(Long idPessoa){
		DetachedCriteria dc = createDetachedCriteria();
		YearMonthDay ymdAtual = JTDateTimeUtils.getDataAtual();
		dc.add(Restrictions.and(Restrictions.le("dtVigenciaInicial", ymdAtual), Restrictions.ge("dtVigenciaFinal", ymdAtual)));
		dc.add(Restrictions.eq("pessoa.idPessoa", idPessoa));
		return findByDetachedCriteria(dc);
	}
	
	// LMSA 6786: LMSA-7253
	@SuppressWarnings("unchecked")
	public EnderecoPessoa findEnderecosVigentesByIdPessoaAndCep(Long idPessoa, String cep, boolean recuperarTipoEndereco){
		DetachedCriteria dc = createDetachedCriteria();
		YearMonthDay ymdAtual = JTDateTimeUtils.getDataAtual();
		
		dc.add(Restrictions.and(Restrictions.le("dtVigenciaInicial", ymdAtual), Restrictions.ge("dtVigenciaFinal", ymdAtual)));
		dc.add(Restrictions.eq("pessoa.idPessoa", idPessoa));
		dc.add(Restrictions.eq("pessoa.idPessoa", idPessoa));
		dc.add(Restrictions.eq("nrCep", cep));

		if(recuperarTipoEndereco) {
			dc.createAlias("tipoEnderecoPessoas", "tep");
			dc.add(Restrictions.eq("tep.tpEndereco", ENDERECO_TIPO_COLETA));
		}
		dc.addOrder(Order.desc("dtVigenciaInicial"));

		EnderecoPessoa result = null;
		List<EnderecoPessoa> resultList = findByDetachedCriteria(dc);
		if (resultList != null && !resultList.isEmpty()) {
			// foi regra definida que se existir mais de um endereco vigente, considerar com data de inicio da vigencia 'mais atual'
			result = resultList.get(0);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Map findByPessoaTipoEndereco(Long idPessoa, String tpEndereco) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		Validate.notEmpty(tpEndereco, "tpEndereco cannot be null");
		YearMonthDay ymd = JTDateTimeUtils.getDataAtual();
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("uf.sgUnidadeFederativa"), "endereco_sgUnidadeFederativa")
			.add(Projections.property("uf.idUnidadeFederativa"), "endereco_idUnidadeFederativa")
			.add(Projections.property("uf.nmUnidadeFederativa"), "endereco_nmUnidadeFederativa")
			.add(Projections.property("ep.nrCep"), "endereco_nrCep")
			.add(Projections.property("ep.dsEndereco"), "endereco_dsEndereco")
			.add(Projections.property("ep.dsComplemento"), "endereco_dsComplemento")
			.add(Projections.property("tl.dsTipoLogradouro"), "endereco_dsTipoLogradouro")
			.add(Projections.property("ep.nrEndereco"), "endereco_nrEndereco")
			.add(Projections.property("m.idMunicipio"), "endereco_idMunicipio")
			.add(Projections.property("m.nmMunicipio"), "endereco_nmMunicipio");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ep")
			.createAlias("ep.municipio", "m")
			.createAlias("m.unidadeFederativa", "uf")
			.createAlias("ep.tipoEnderecoPessoas", "tep")
			.createAlias("ep.tipoLogradouro", "tl")
			.setProjection(pl)
			.add(Restrictions.eq("ep.pessoa.id", idPessoa))
			.add(Restrictions.eq("tep.tpEndereco", tpEndereco))
			.add(Restrictions.le("ep.dtVigenciaInicial", ymd))
			.add(Restrictions.ge("ep.dtVigenciaFinal", ymd))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return (Map)l.get(0);
		return Collections.EMPTY_MAP;
	}
    
    public Long findIdMunicipioByIdPessoa(Long idPessoa) {
        StringBuilder sql = new StringBuilder();

        sql.append("select ep.id_municipio from pessoa p \n");
        sql.append("  inner join endereco_pessoa ep on p.id_endereco_pessoa = ep.id_endereco_pessoa \n");
        sql.append("where 1 = 1 \n");
        sql.append("  and p.id_pessoa = ").append(idPessoa).append(" \n");
        sql.append("  and rownum <= 1");
        
        Map<String, Object> retorno = this.getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), null, new ConfigureSqlQuery() {
            
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("id_municipio", Hibernate.LONG);
            }
            
        }).get(0);

        return (Long) retorno.get("id_municipio");
    }

	/**
	 * @deprecated
	 * Utilizar EnderecoPessoaDAO.findEnderecoPessoaPadrao 
	 * 
	 * @param idPessoa
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated public Map findMunicipioUfByIdPessoa(Long idPessoa) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		/** Projecao */
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(ep.nrCep","nrCep");
		hql.addProjection("m.idMunicipio","idMunicipio");
		hql.addProjection("m.nmMunicipio","nmMunicipio");
		hql.addProjection("uf.idUnidadeFederativa","idUnidadeFederativa");
		hql.addProjection("pa.idPais","idPais)");
		/** From */
		StringBuilder from = new StringBuilder()
			.append(EnderecoPessoa.class.getName()+" as ep")
			.append(" join ep.municipio as m")
			.append(" join m.unidadeFederativa as uf")
			.append(" join uf.pais as pa");
		hql.addFrom(from.toString());
		/** Where */
		hql.addCriteria("ep.pessoa.id","=",idPessoa);

		List result = AliasToNestedMapResultTransformer.getInstance().transformListResult(getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()));
		if(!result.isEmpty())
			return (Map)result.get(0);
		return Collections.EMPTY_MAP;
	}

	@SuppressWarnings("rawtypes")
	public Map findMunicipioByIdPessoaTpEndereco(Long idPessoa, String tpEndereco) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		Validate.notEmpty(tpEndereco, "tpEndereco cannot be null");
		YearMonthDay ymd = JTDateTimeUtils.getDataAtual();
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("m.idMunicipio"), "idMunicipio")
			.add(Projections.property("m.nmMunicipio"), "nmMunicipio");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ep")
			.createAlias("ep.municipio", "m")
			.createAlias("ep.tipoEnderecoPessoas", "tep")
			.setProjection(pl)
			.add(Restrictions.eq("ep.pessoa.id", idPessoa))
			.add(Restrictions.eq("tep.tpEndereco", tpEndereco))
			.add(Restrictions.le("ep.dtVigenciaInicial", ymd))
			.add(Restrictions.ge("ep.dtVigenciaFinal", ymd))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return (Map)l.get(0);
		return Collections.EMPTY_MAP;
	}

	public Serializable getRowCountByPessoaUfFronteiraRapida(Long idPessoa, Boolean blFronteiraRapida) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		Validate.notNull(blFronteiraRapida, "blFronteiraRapida cannot be null");
		YearMonthDay ymd = JTDateTimeUtils.getDataAtual();
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ep")
			.createAlias("ep.municipio", "m")
			.createAlias("m.unidadeFederativa", "uf")
			.setProjection(Projections.count("ep.idEnderecoPessoa"))
			.add(Restrictions.eq("ep.pessoa.id", idPessoa))
			.add(Restrictions.eq("uf.blFronteiraRapida", blFronteiraRapida))
			.add(Restrictions.le("ep.dtVigenciaInicial", ymd))
			.add(Restrictions.ge("ep.dtVigenciaFinal", ymd));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Método responsavel por buscar o EnderecoPessoa de uma pessoa
	 * 
	 * @param idPessoa
	 * @return EnderecoPessoa
	 */
	@SuppressWarnings("rawtypes")
	public Map findEnderecoCobrancaByPessoa(Long idPessoa){
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map( ep.dsBairro as dsBairro ");
		hql.addProjection("ep.idEnderecoPessoa as idEnderecoPessoa ");
		hql.addProjection("m.nmMunicipio as nmMunicipio ");
		hql.addProjection("uf.sgUnidadeFederativa as sgUnidadeFederativa ");
		hql.addProjection("pais.nmPais as nmPais ");
		hql.addProjection("ep.nrCep as nrCep ) ");

		hql.addFrom(TipoEnderecoPessoa.class.getName() + " tep " +
				" JOIN tep.enderecoPessoa ep " +
				" JOIN ep.municipio m " +
				" JOIN m.unidadeFederativa uf " +
				" JOIN uf.pais pais " +
				" JOIN ep.pessoa p " +
				" JOIN ep.telefoneEnderecos tel ");

		hql.addCustomCriteria("tep.tpEndereco = 'COB'");
		hql.addCriteria("p.idPessoa", "=", idPessoa);

		List list = getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if(list != null && !list.isEmpty()){
			return (Map)list.get(0);
		}
		return null;
	}

	public boolean findEnderecoByFilialCiaArea(YearMonthDay dataInicio, YearMonthDay dataFim, Long idFilialCiaAerea){
		Validate.notNull(dataInicio, "dataInicio cannot be null");
		Validate.notNull(idFilialCiaAerea, "idFilialCiaAerea cannot be null");
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("pessoa.idPessoa",idFilialCiaAerea));
		dc.add(Restrictions.or(
				Restrictions.or(
					Restrictions.and(
							Restrictions.lt("dtVigenciaInicial",dataInicio),
							Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataFim))),
					Restrictions.and(
									Restrictions.gt("dtVigenciaInicial",dataInicio),
									Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataFim)))),
				Restrictions.or(
						Restrictions.lt("dtVigenciaInicial",dataInicio),
						Restrictions.gt("dtVigenciaFinal",dataFim))));
		return findByDetachedCriteria(dc).size()>0;
	}

	/**
	 * Busca o EnderecoPessoa pelo id, fazendo fetch em TipoEnderecoPessoas
	 * @param idEnderecoPessoa
	 * @return
	 */
	public EnderecoPessoa findByIdFetchTipoEnderecoPessoas(Long idEnderecoPessoa){
		StringBuilder str = new StringBuilder();
		str.append("select ep from EnderecoPessoa ep left join fetch ep.tipoEnderecoPessoas tep where ep.id = ?");
		return (EnderecoPessoa)getAdsmHibernateTemplate().findUniqueResult(str.toString(), new Object[]{idEnderecoPessoa});
	}
	
	/**
	 * Retorna o endereï¿½o padrï¿½o (pessoa.enderecoPessoa) da pessoa informada.
	 * 
	 * @author Giuliano
	 * @since 08/08/2006
	 * 
	 * @param Long idPessoa
	 * @return EnderecoPessoa
	 */	
	public EnderecoPessoa findByIdPessoa(Long idPessoa){
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		ProjectionList pl = Projections.projectionList();

		pl.add(Projections.property("ep.idEnderecoPessoa"), "idEnderecoPessoa");
		pl.add(Projections.property("ep.nrCep"), "nrCep");
		pl.add(Projections.property("ep.nrEndereco"), "nrEndereco");
		pl.add(Projections.property("ep.dsEndereco"), "dsEndereco");
		pl.add(Projections.property("ep.dsBairro"), "dsBairro");
		pl.add(Projections.property("ep.dsComplemento"), "dsComplemento");
		pl.add(Projections.property("tl.dsTipoLogradouro"), "tipoLogradouro.dsTipoLogradouro");
		pl.add(Projections.property("epm.idMunicipio"), "municipio.idMunicipio");
		pl.add(Projections.property("epm.nmMunicipio"), "municipio.nmMunicipio");
		pl.add(Projections.property("epmuf.idUnidadeFederativa"), "municipio.unidadeFederativa.idUnidadeFederativa");
		pl.add(Projections.property("epmuf.sgUnidadeFederativa"), "municipio.unidadeFederativa.sgUnidadeFederativa");
		pl.add(Projections.property("epmuf.nmUnidadeFederativa"), "municipio.unidadeFederativa.nmUnidadeFederativa");
		pl.add(Projections.property("epmufp.idPais"), "municipio.unidadeFederativa.pais.idPais");
		pl.add(Projections.property("epmufp.nmPais"), "municipio.unidadeFederativa.pais.nmPais");
		pl.add(Projections.property("epmufp.sgResumida"), "municipio.unidadeFederativa.pais.sgResumida");

		DetachedCriteria dc = DetachedCriteria.forClass(Pessoa.class, "p");
		dc.setProjection(pl);

		dc.createAlias("p.enderecoPessoa", "ep");
		dc.createAlias("ep.tipoLogradouro", "tl");
		dc.createAlias("ep.municipio", "epm");
		dc.createAlias("epm.unidadeFederativa", "epmuf");
		dc.createAlias("epmuf.pais", "epmufp");

		dc.add(Restrictions.eq("p.idPessoa", idPessoa));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));

		return (EnderecoPessoa) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Retorna todos os endereï¿½os da pessoa informada
	 * 
	 * @author Mickaï¿½l Jalbert
	 * @since 23/08/2006
	 * 
	 * @param Long idPessoa
	 * @return List
	 */	
	@SuppressWarnings("rawtypes")
	public List findTodosByIdPessoa(Long idPessoa, YearMonthDay dtVigencia, Long idEnderecoPessoa) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		Validate.notNull(dtVigencia, "dtVigencia cannot be null");
		
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("ep");

		hql.addInnerJoin(EnderecoPessoa.class.getName(), "ep");

		hql.addCriteria("ep.pessoa.id","=",idPessoa);
		// idEnderecoPessoa ï¿½ opcional
		hql.addCriteria("ep.id","!=",idEnderecoPessoa);
		JTVigenciaUtils.getHqlVigenciaNotNull(hql, "ep.dtVigenciaInicial", "ep.dtVigenciaFinal", dtVigencia);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}		
	
	/**
	 * Realiza um count dos enderecoPessoa vigentes da pessoa de acordo com o tpEndereco passdo por parametro
	 *
	 * author Hector Julian Esnaola Junior
	 * @since 26/09/2006
	 *
	 * @param idPessoa
	 * @param endereco
	 * @return
	 */
	public Integer countEnderecoPessoaVigenteByTpEndereco(EnderecoPessoa endereco, String tpEndereco){
		Validate.notEmpty(tpEndereco, "tpEndereco cannot be null");
		DetachedCriteria dc = DetachedCriteria.forClass(TipoEnderecoPessoa.class);
		dc.setProjection(Projections.count("idTipoEnderecoPessoa"));

		dc.createAlias("enderecoPessoa","endereco");
		dc.createAlias("endereco.pessoa","pessoa");

		Criterion critVigenciaInicial = Restrictions.and(
				Restrictions.le("endereco.dtVigenciaInicial",endereco.getDtVigenciaInicial()),
				Restrictions.ge("endereco.dtVigenciaFinal",endereco.getDtVigenciaInicial())
			);

		if (endereco.getDtVigenciaFinal() != null){
			//Pode nï¿½o ter vigencia final
			Criterion critVigenciaFinal = Restrictions.and(
					Restrictions.le("endereco.dtVigenciaInicial",endereco.getDtVigenciaFinal()),
					Restrictions.ge("endereco.dtVigenciaFinal",endereco.getDtVigenciaFinal())
				);
			dc.add(Restrictions.or(critVigenciaInicial, critVigenciaFinal));
		} else {
			dc.add( Restrictions.or( 
						critVigenciaInicial, 
						Restrictions.or(
								Restrictions.ge("endereco.dtVigenciaInicial", endereco.getDtVigenciaInicial()),
								Restrictions.and(
										Restrictions.isNull("endereco.dtVigenciaFinal"),
										Restrictions.le("endereco.dtVigenciaInicial", endereco.getDtVigenciaInicial())
									)
							)
					) );
		}

		dc.add(Restrictions.eq("tpEndereco", tpEndereco));
		dc.add(Restrictions.eq("pessoa.id", endereco.getPessoa().getIdPessoa()));

		return (Integer) findByDetachedCriteria(dc).get(0);
	}

	/**
	 * Realiza um count dos enderecoPessoa da pessoa que tenham vigencia "conflitante" 
	 * com a vigencia do passada no parï¿½metro 
	 *
	 * author Hector Julian Esnaola Junior
	 * @since 26/09/2006
	 *
	 * @param idPessoa
	 * @param endereco
	 * @return
	 *
	 */
	public Integer countEnderecoPessoaVigenteByTpEndereco(Long idPessoa, String tpEndereco, YearMonthDay dtInicial, YearMonthDay dtFinal) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		Validate.notEmpty(tpEndereco, "tpEndereco cannot be null");
		Validate.notNull(dtInicial, "dtInicial cannot be null");
		
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" count(ep.id) ");

		hql.addFrom(getPersistentClass().getName() + " ep " +
				" JOIN ep.pessoa p " +
				" JOIN ep.tipoEnderecoPessoas tep");

		hql.addCriteria("p.id", "=", idPessoa); 
		hql.addCriteria("tep.tpEndereco", "=", tpEndereco);

		hql.addCustomCriteria(" (( ? BETWEEN ep.dtVigenciaInicial AND ep.dtVigenciaFinal ) OR " +
							  " (  ? BETWEEN ep.dtVigenciaInicial AND ep.dtVigenciaFinal ) OR " +
							  " (  ? < ep.dtVigenciaInicial  AND ? > ep.dtVigenciaFinal )) ");
		hql.addCriteriaValue(dtInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(dtFinal));
		hql.addCriteriaValue(dtInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(dtFinal));

		return  ((Long) getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).get(0)).intValue() ;
	}

	 @SuppressWarnings("rawtypes")
	public List findEnderecoPessoaByIdPessoa(Long idPessoa){
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(endPes.nrEndereco as nrEndereco, " +
				"tl.dsTipoLogradouro as dsTipoLogradouro, " +
				"endPes.dsEndereco as dsEndereco, " +
				"endPes.dsComplemento as dsComplemento, " +
				"tipoEndPes.tpEndereco as tpEndereco, " +
				"endPes.dtVigenciaInicial as dtVigenciaInicial, " +
				"endPes.dtVigenciaFinal as dtVigenciaFinal, " +
				"endPes.idEnderecoPessoa as idEnderecoPessoa) ");

		hql.addFrom(EnderecoPessoa.class.getName()+ " endPes " +
				"left outer join endPes.tipoEnderecoPessoas tipoEndPes " +
				"join endPes.tipoLogradouro tl " +
				"join endPes.pessoa pes ");

		hql.addCriteria("pes.idPessoa","=",idPessoa);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	 /**
	  * 
	  *
	  * author Hector Julian Esnaola Junior
	  * @since 28/08/2007
	  *
	  * @param idPessoa
	  * @param tpEnderecoPessoa
	  * @return
	  *
	  */
	 public EnderecoPessoa findMaiorEndererecoPessoa(
			 Long idPessoa, 
			 YearMonthDay dtVigenciaInicial, 
			 String tpEnderecoPessoa
	){
		 Validate.notNull(idPessoa, "idPessoa cannot be null");
		 Validate.notNull(dtVigenciaInicial, "dtVigenciaInicial cannot be null");
		 Validate.notEmpty(tpEnderecoPessoa, "tpEnderecoPessoa cannot be null");
		 SqlTemplate hql = new SqlTemplate();

		 hql.addProjection(" ep ");
		 hql.addFrom(getPersistentClass().getName() + " ep " + 
				 "INNER JOIN ep.tipoEnderecoPessoas", "tep");
		 hql.addCriteria("ep.pessoa.id", "=", idPessoa);
		 hql.addCriteria("tep.tpEndereco", "=", tpEnderecoPessoa);

		 hql.addCustomCriteria("(ep.dtVigenciaInicial = " +
				"(SELECT MIN(epTmp.dtVigenciaInicial) " +
				"  FROM " + getPersistentClass().getName() + " epTmp " +
				" INNER JOIN epTmp.tipoEnderecoPessoas as tepTmp " +
				" WHERE epTmp.pessoa.id = ? " +
		 		"   AND epTmp.dtVigenciaInicial > ?" +
		 		"   AND tepTmp.tpEndereco = ?)) ");
		hql.addCriteriaValue(idPessoa);
		hql.addCriteriaValue(dtVigenciaInicial);
		hql.addCriteriaValue(tpEnderecoPessoa);

		return (EnderecoPessoa)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	 /**
	  * Retorna o endereï¿½o comercial/residencial vigente para uma pessoa.
	  * 
	  * @param idPessoa
	  * @param dtVigenciaInicial
	  * 
	  * @return EnderecoPessoa
	  */
	public EnderecoPessoa findEnderecoPessoaSubstituir(Long idPessoa,
			YearMonthDay dtVigenciaInicial) {		
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ep");

		sql.addInnerJoin(Pessoa.class.getName(), "pes");
		sql.addInnerJoin("pes.enderecoPessoas", "ep");
		sql.addInnerJoin("ep.tipoEnderecoPessoas", "te");
		sql.addInnerJoin("FETCH ep.tipoLogradouro", "tl");		
		sql.addInnerJoin("FETCH ep.municipio", "m");
		sql.addInnerJoin("FETCH m.unidadeFederativa", "u");
		sql.addInnerJoin("FETCH u.pais", "p");		

		sql.addCriteria("pes.id","=",idPessoa);
		sql.addCriteriaIn("te.tpEndereco", new Object[]{"RES", "COM"});
		sql.addCriteria("trunc(ep.dtVigenciaInicial)", "<", dtVigenciaInicial);
		sql.addCustomCriteria("(ep.dtVigenciaFinal is null OR ep.dtVigenciaFinal = to_date('01/01/4000', 'dd/mm/yyyy'))");

		return (EnderecoPessoa) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findEnderecosPessoa(Long idPessoa) {
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("distinct new Map( ep.idEnderecoPessoa as idEnderecoPessoa ");
		hql.addProjection("m.nmMunicipio as nmMunicipio ");
		hql.addProjection("uf.sgUnidadeFederativa as sgUnidadeFederativa ");
		hql.addProjection("uf.nmUnidadeFederativa as nmUnidadeFederativa ");
		hql.addProjection("pais.nmPais as nmPais ");
		hql.addProjection("ep.dsEndereco as dsEndereco ");
		hql.addProjection("ep.dsBairro as dsBairro ");
		hql.addProjection("ep.nrEndereco as nrEndereco ");
		hql.addProjection("ep.dtVigenciaFinal as dtVigenciaFinal ");
		hql.addProjection("ep.dtVigenciaInicial as dtVigenciaInicial  ");
		hql.addProjection("tl.dsTipoLogradouro as tpLogradouro ) ");

		hql.addFrom(TipoEnderecoPessoa.class.getName() + " tep " +
				" JOIN tep.enderecoPessoa ep " +
				" JOIN ep.municipio m " +
				" JOIN m.unidadeFederativa uf " +
				" JOIN uf.pais pais " +
				" JOIN ep.pessoa p "
				+ "JOIN ep.tipoLogradouro tl") ;
				

		hql.addCriteria("p.idPessoa", "=", idPessoa);

		return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	
	
	@SuppressWarnings("unchecked")
	public Long findEnderecoPessoaCobranca(Long idPessoa) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT F_BUSCA_ENDERECO_PESSOA(?, 'COB', sysdate) as ID_ENDERECO_PESSOA from dual");
   
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("ID_ENDERECO_PESSOA", Hibernate.LONG);
			}
		};

		List<Long> param = new ArrayList<Long>();
		param.add(idPessoa);

		List<Long> result = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery).getList();
		if (result.isEmpty() || result.get(0) == null)
			return null;

		return result.get(0);		
	}

	@SuppressWarnings("unchecked")
	public EnderecoPessoa findEnderecoPeriodo(Long idPessoa, YearMonthDay dtVigenciaInicial){
		Validate.notNull(idPessoa, "idPessoa cannot be null");
		Validate.notNull(dtVigenciaInicial, "dtVigenciaInicial cannot be null");
		
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ep");

		sql.addInnerJoin(Pessoa.class.getName(), "pes");
		sql.addInnerJoin("pes.enderecoPessoas", "ep");
		sql.addInnerJoin("FETCH ep.tipoLogradouro", "tl");
		sql.addInnerJoin("ep.tipoEnderecoPessoas", "te");

		sql.addCriteria("pes.id","=",idPessoa);
		sql.addCriteriaIn("te.tpEndereco", new Object[]{ENDERECO_TIPO_COLETA});
		sql.addCriteria("trunc(ep.dtVigenciaInicial)", "<=", dtVigenciaInicial);
		sql.addCriteria("trunc(ep.dtVigenciaFinal)", ">=", dtVigenciaInicial);

		
		List<EnderecoPessoa> enderecos =  getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (enderecos.isEmpty() || enderecos.get(0) == null || enderecos.size() > 1){
			return null;
		}
		return  enderecos.get(0);		
	}
	
	public EnderecoPessoa findUltimoEnderecoValidoByIdPessoa(Long idPessoa) {
		StringBuilder hql = new StringBuilder();
		
		hql
		.append("SELECT ep ")
		.append("FROM ").append(EnderecoPessoa.class.getSimpleName()).append(" ep ")
		.append("JOIN ep.pessoa p ")
		.append("WHERE ")
		.append("	 p.idPessoa =:idPessoa ")
		.append("AND ROWNUM = 1 ")
		.append("ORDER BY ep.dtVigenciaFinal Desc ")
		;
		
		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("idPessoa", idPessoa);
		
		return (EnderecoPessoa)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), namedParams);
	}
	
	public String getEnderecoCompleto(Long idPessoa) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT ep ")
			.append("FROM ").append(EnderecoPessoa.class.getSimpleName()).append(" ep ")
			.append("JOIN ep.pessoa p ")
			.append("WHERE p.idPessoa =:idPessoa ")
			.append("AND ROWNUM = 1 ")
			.append("ORDER BY ep.dtVigenciaFinal Desc ");

		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("idPessoa", idPessoa);

		EnderecoPessoa enderecoPessoa = (EnderecoPessoa)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), namedParams);
		StringBuilder strEndereco = new StringBuilder();

		if (enderecoPessoa != null) {
			if (enderecoPessoa.getTipoLogradouro() != null) {
				strEndereco.append(enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro()).append(" ");
			}

			strEndereco.append(enderecoPessoa.getDsEndereco()).append(", ");

			if (enderecoPessoa.getNrEndereco() != null) {
				strEndereco.append(enderecoPessoa.getNrEndereco().trim());
			} else {
				strEndereco.append("N/D");
			}

			if (enderecoPessoa.getDsComplemento() != null) {
				strEndereco.append(" - ").append(enderecoPessoa.getDsComplemento());
			}
		}

		return strEndereco.toString();
	}

	public String getEnderecoCompletoByEnderecoIdPessoa(Long idEnderecoPessoa) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT ep ")
				.append("FROM ").append(EnderecoPessoa.class.getSimpleName()).append(" ep ")
				.append("JOIN ep.pessoa p ")
				.append("WHERE ep.idEnderecoPessoa =:idEnderecoPessoa ")
				.append("AND ROWNUM = 1 ")
				.append("ORDER BY ep.dtVigenciaFinal Desc ");

		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("idEnderecoPessoa", idEnderecoPessoa);

		EnderecoPessoa enderecoPessoa = (EnderecoPessoa)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), namedParams);
		StringBuilder strEndereco = new StringBuilder();

		if (enderecoPessoa != null) {
			if (enderecoPessoa.getTipoLogradouro() != null) {
				strEndereco.append(enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro()).append(" ");
			}

			strEndereco.append(enderecoPessoa.getDsEndereco()).append(", ");

			if (enderecoPessoa.getNrEndereco() != null) {
				strEndereco.append(enderecoPessoa.getNrEndereco().trim());
			} else {
				strEndereco.append("N/D");
			}

			if (enderecoPessoa.getDsComplemento() != null) {
				strEndereco.append(" - ").append(enderecoPessoa.getDsComplemento());
			}
		}

		return strEndereco.toString();
	}
}