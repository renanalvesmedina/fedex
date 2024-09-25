package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.AnexoProprietario;
import com.mercurio.lms.contratacaoveiculos.model.BeneficiarioProprietario;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ProprietarioDAO extends BaseCrudDao<Proprietario, Long>
{
	
	private static final String NR_IDENTIFICACAO = "nrIdentificacao";
	private static final String ID_PROPRIETARIO2 = "idProprietario";
	private static final String NM_PESSOA = "nmPessoa";
	private static final String TP_PROPRIETARIO = "tpProprietario";
	private static final int ID_PROPRIETARIO = 0;
	private static final int NUMERO_IDENTIFICACAO = 1;
	private static final int TIPO_PROPRIETARIO = 3;
	private static final int NOME_PESSOA = 2;
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Proprietario.class;
    }

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("pessoa", FetchMode.JOIN);
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("municipio", FetchMode.JOIN);
		fetchModes.put("telefoneEndereco", FetchMode.JOIN);
		fetchModes.put("pessoa.contaBancarias", FetchMode.SELECT);
		fetchModes.put("pessoa.contaBancarias.agenciaBancaria", FetchMode.JOIN);
		fetchModes.put("pessoa.unidadeFederativaExpedicaoRg", FetchMode.JOIN);
		fetchModes.put("pessoa.contaBancarias.agenciaBancaria.banco", FetchMode.JOIN);
		fetchModes.put("usuario", FetchMode.JOIN);
		fetchModes.put("pendencia", FetchMode.JOIN);
		fetchModes.put("pendencia.ocorrencia", FetchMode.JOIN);
		fetchModes.put("pendencia.ocorrencia.eventoWorkflow", FetchMode.JOIN);
		fetchModes.put("pendencia.ocorrencia.eventoWorkflow.tipoEvento", FetchMode.JOIN);
	}

	
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),
				findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
	}
	
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}
	
	private SqlTemplate getHqlForFindPaginated(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new map(PROP.id",ID_PROPRIETARIO2);
        hql.addProjection("FIL.sgFilial","filial_sgFilial");
        hql.addProjection("FIL.id","filial_idFilial");
        hql.addProjection("P_FIL.nmFantasia","filial_pessoa_nmFantasia");
        hql.addProjection("P_PROP.tpIdentificacao","pessoa_tpIdentificacao");
        hql.addProjection("P_PROP.nrIdentificacao","pessoa_nrIdentificacao");
        hql.addProjection("P_PROP.nmPessoa","pessoa_nmPessoa");
        hql.addProjection("PROP.tpSituacao","tpSituacao");
        hql.addProjection("PROP.tpPeriodoPagto","tpPeriodoPagto"); 
        hql.addProjection("PROP.diaSemana","diaSemana");
        hql.addProjection("PROP.tpProprietario","tpProprietario");
        hql.addProjection("PROP.blCooperado","blCooperado");
        hql.addProjection("PROP.blMei","blMei)");        
        
        StringBuilder hqlFrom = new StringBuilder()
        		.append(Proprietario.class.getName()).append(" as PROP")
        		.append(" inner join PROP.pessoa as P_PROP ")
        		.append(" inner join PROP.filial as FIL ")
        		.append(" inner join FIL.pessoa as P_FIL ");
        
        hql.addFrom(hqlFrom.toString());
        
        hql.addCriteria("PROP.tpProprietario","=",criteria.getString(TP_PROPRIETARIO));
        hql.addCriteria("FIL.id","=",criteria.getLong("filial.idFilial"));
        
        hql.addCriteria("P_PROP.tpPessoa","=",criteria.getString("pessoa.tpPessoa"));
        hql.addCriteria("P_PROP.tpIdentificacao","=",criteria.getString("pessoa.tpIdentificacao"));
        
        String nrIdentificacao = PessoaUtils.clearIdentificacao(criteria.getString("pessoa.nrIdentificacao"));
        if (StringUtils.isNotBlank(nrIdentificacao)) {
        	hql.addCriteria("P_PROP.nrIdentificacao", "LIKE", nrIdentificacao.concat("%"));
        }

        String nmPessoa = criteria.getString("pessoa.nmPessoa");
        if (StringUtils.isNotBlank(nmPessoa)) {
        	hql.addCriteria("lower(P_PROP.nmPessoa)","LIKE",nmPessoa.toLowerCase().concat("%"));
        }

        hql.addCriteria("PROP.tpPeriodoPagto","=",criteria.getString("tpPeriodoPagto"));
	    hql.addCriteria("PROP.diaSemana","=",criteria.getString("diaSemana"));
	    hql.addCriteria("PROP.tpSituacao","=",criteria.getString("tpSituacao"));
	    
	    hql.addCriteria("P_PROP.nrRg","=",criteria.getString("nrRg"));
	    hql.addCriteria("PROP.blMei","=",criteria.getString("blMei"));
	    hql.addCriteria("PROP.blCooperado","=",criteria.getString("blCooperado"));
	    
	    hql.addCriteria("PROP.dtAtualizacao",">=",criteria.getYearMonthDay("dtAtualizacaoInicial"));
	    hql.addCriteria("PROP.dtAtualizacao","<=",criteria.getYearMonthDay("dtAtualizacaoFinal"));
        
	    //se passar o beneficiario, busca pelos proprietários daquele beneficiario, caso esta relação esteja vigente
	    if (criteria.getLong("beneficiario.idBeneficiario") != null){
			    hql.addCustomCriteria("exists (select p.idProprietario from "+BeneficiarioProprietario.class.getName()+" as bp " +
			    									   " inner join bp.beneficiario as b " +
			    									   " inner join bp.proprietario as p " +
			    									   " where p.idProprietario = PROP.idProprietario " +
			    									   " and bp.dtVigenciaInicial <= ? " +
			    									   " and bp.dtVigenciaFinal >= ? " +
			    									   " and b.idBeneficiario = ? )");
			    hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			    hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			    hql.addCriteriaValue(criteria.getLong("beneficiario.idBeneficiario"));
	    }
	     
        hql.addOrderBy("FIL.sgFilial");
        hql.addOrderBy("P_PROP.nmPessoa");
        
		return hql;
	}
	
	
	/**
	 * Retorna os beneficiarios de determinado proprietario
	 * @param proprietario Proprietario
	 * @return Uma list
	 */
    public List findBeneficiariosByProprietario(Long idProprietario) {

		StringBuffer hql = new StringBuffer()
		//.append(" select bp.beneficiario ")
		.append(" from " + BeneficiarioProprietario.class.getName() + " as bp")
		.append(" join fetch bp.beneficiario")
		.append(" join fetch bp.beneficiario.pessoa ")
		.append(" join fetch bp.proprietario")
		.append(" where bp.proprietario.idProprietario = ?");
		
    	List list = getAdsmHibernateTemplate().find(hql.toString(), idProprietario);
    	return list;
    }

    
	public List findLookupProprietario(String nrIdentificacao,String tpSituacao, String tpPessoa) {
		
		ProjectionList pl = Projections.projectionList()
						 .add(Projections.alias(Projections.property("P.nmPessoa"),"pessoa_nmPessoa"))
						 .add(Projections.alias(Projections.property("P.nrIdentificacao"),"pessoa_nrIdentificacao"))
						 .add(Projections.alias(Projections.property("P.tpIdentificacao"),"pessoa_tpIdentificacao"))
						 .add(Projections.alias(Projections.property(ID_PROPRIETARIO2),ID_PROPRIETARIO2));
		
		DetachedCriteria dc = createDetachedCriteria()
						   .setProjection(pl)
						   .createAlias("pessoa","P")
						   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		
		if (nrIdentificacao != null && !"".equals(nrIdentificacao))
			dc.add(Restrictions.ilike("P.nrIdentificacao",nrIdentificacao));
		
		if (tpSituacao != null && !"".equals(tpSituacao) )
			dc.add(Restrictions.eq("tpSituacao",tpSituacao));
		
		if( tpPessoa != null && !"".equals(tpPessoa) ){
			dc.add(Restrictions.eq("P.tpPessoa",tpPessoa));
		}
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
    
	
    /**
     * 
     * @param idProprietario
     * @return
     */
    public List findDadosBancariosByIdProprietario(Long idProprietario){
    	StringBuffer sql = new StringBuffer()
    	.append("select new Map(cb.nrContaBancaria as nrContaBancaria, ")
    	.append("cb.dvContaBancaria as dvContaBancaria, ")
    	.append("ab.nrAgenciaBancaria as nrAgenciaBancaria, ")
    	.append("ab.nmAgenciaBancaria as nmAgenciaBancaria, ")
    	.append("pessoaProprietario.idPessoa as idPessoa, ")
    	.append("banco.nrBanco as nrBanco, ")
    	.append("banco.nmBanco as nmBanco ")
    	.append(") ")
    	.append("from ")
    	.append(getPersistentClass().getName()).append(" prop ")
    	.append("inner join prop.pessoa pessoaProprietario ")
    	.append("left join pessoaProprietario.contaBancarias cb ")
    	.append("left join cb.agenciaBancaria ab ")
    	.append("left join ab.banco banco ")
    	.append("where ")
    	.append("prop.id = ? ")
    	.append("and ? between cb.dtVigenciaInicial and cb.dtVigenciaFinal ");

    	List param = new ArrayList();
    	param.add(idProprietario);
    	param.add(JTDateTimeUtils.getDataAtual());

    	List retorno = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    	return retorno;
    }

	/**
	 * Retorna 'true' se a pessoa informada é um proprietario ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isProprietario(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("count(pr.id)");
		
		hql.addInnerJoin(Proprietario.class.getName(), "pr");
		
		hql.addCriteria("pr.id", "=", idPessoa);
		hql.addCriteria("pr.tpSituacao", "=", "A");
		
		List lstProprietario = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (((Long)lstProprietario.get(0)) > 0){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Busca proprietário cadastrado com o número de PIS informado.
	 * @param nrPis número do PIS para ser usado como critério
	 * @return intância de Proprietario
	 */
	public Proprietario findProprietarioByPIS(Long nrPis) {
		if (nrPis == null) {
			throw new IllegalArgumentException("nrPis é parâmetro obrigatório");
		}
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("nrPis",nrPis));
		
		return (Proprietario) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * O método findById retorno erro caso não exista, este deve retornar null
	 * 
	 * @param idProprietario
	 * @return
	 */
	public Proprietario findByIdReturnNull(Long idProprietario){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq(ID_PROPRIETARIO2,idProprietario));
		
		return (Proprietario) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Retorna o resultado da pesquisa para uma suggest de proprietários.
	 * 
	 * @param nrIdentificador
	 * @param nmPessoa
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findProprietarioSuggest(
			final String nrIdentificador, final String nmPessoa,
			final Integer limiteRegistros) {	
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
				
		final StringBuilder sql = getQueryProprietarioSuggest(nrIdentificador, nmPessoa, limiteRegistros);
		
		final ConfigureSqlQuery csq = getProjectionProprietarioSuggest();
		
		final HibernateCallback hcb = getCallbackProprietarioSuggest(nrIdentificador, nmPessoa, limiteRegistros, sql, csq);
				
		List<Object[]> list = getHibernateTemplate().executeFind(hcb);
	
		for (Object[] o: list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(ID_PROPRIETARIO2, o[ID_PROPRIETARIO]);
			map.put(NR_IDENTIFICACAO, o[NUMERO_IDENTIFICACAO]);
			map.put(NM_PESSOA, o[NOME_PESSOA]);
			map.put(TP_PROPRIETARIO, o[TIPO_PROPRIETARIO]);
			toReturn.add(map);			
		}
		
		return toReturn;
	}
	
	/**
	 * LMS-5590
	 * 
	 * Retorna o resultado da pesquisa para uma suggest de proprietários somente com os que possuem CPF
	 * 
	 * @param nrIdentificador
	 * @param nmPessoa
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findProprietarioSuggestCpf(
			final String nrIdentificador, final String nmPessoa,
			final Integer limiteRegistros) {	
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
				
		final StringBuilder sql = getQueryProprietarioSuggestCpf(nrIdentificador, nmPessoa, limiteRegistros);
		
		final ConfigureSqlQuery csq = getProjectionProprietarioSuggest();
		
		final HibernateCallback hcb = getCallbackProprietarioSuggest(nrIdentificador, nmPessoa, limiteRegistros, sql, csq);
				
		List<Object[]> list = getHibernateTemplate().executeFind(hcb);
	
		for (Object[] o: list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(ID_PROPRIETARIO2, o[ID_PROPRIETARIO]);
			map.put(NR_IDENTIFICACAO, o[NUMERO_IDENTIFICACAO]);
			map.put(NM_PESSOA, o[NOME_PESSOA]);
			map.put(TP_PROPRIETARIO, o[TIPO_PROPRIETARIO]);
			toReturn.add(map);			
		}
		
		return toReturn;
	}

	private HibernateCallback getCallbackProprietarioSuggest(
			final String nrIdentificador, final String nmPessoa,
			final Integer limiteRegistros, final StringBuilder sql,
			final ConfigureSqlQuery csq) {
		return new HibernateCallback() {
			public Object doInHibernate(Session session){
				SQLQuery query = session.createSQLQuery(sql.toString());
				
				if(StringUtils.isNotBlank(nrIdentificador)){
					query.setString("nrIdentificador", nrIdentificador.trim());
				}
				
				if(StringUtils.isNotBlank(nmPessoa)){
					query.setString(NM_PESSOA, "%".concat(nmPessoa.trim()).concat("%"));			
				}
				
				if(limiteRegistros != null){
					query.setInteger("limiteRegistros", limiteRegistros);			
				}
					
            	csq.configQuery(query);
				return query.list();
			}
		};
	}

	private ConfigureSqlQuery getProjectionProprietarioSuggest() {
		return new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_proprietario", Hibernate.LONG);
				sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);
				sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
				sqlQuery.addScalar("tp_proprietario", Hibernate.STRING);
			}
		};
	}

	private StringBuilder getQueryProprietarioSuggest(
			final String nrIdentificador, final String nmPessoa,
			final Integer limiteRegistros) {
		final StringBuilder sql = new StringBuilder();
				
		sql.append("SELECT pr.id_proprietario,");
		sql.append("       pe.nr_identificacao,"); 
		sql.append("       pe.nm_pessoa,");
		sql.append("       (SELECT VI18N(vdm.ds_valor_dominio_i)");
		sql.append("         FROM valor_dominio vdm");
		sql.append("          INNER JOIN dominio dm");
		sql.append("           ON dm.id_dominio = vdm.id_dominio");
		sql.append("           WHERE vl_valor_dominio = pr.tp_proprietario");
		sql.append("            AND dm.nm_dominio = 'DM_TIPO_PROPRIETARIO') AS tp_proprietario");		
		sql.append(" FROM proprietario pr");
		sql.append("  JOIN pessoa pe");
		sql.append("   ON pr.id_proprietario = pe.id_pessoa");
		sql.append(" WHERE 1=1");
		
		if(StringUtils.isNotBlank(nrIdentificador)){
			sql.append(" AND pe.nr_identificacao = :nrIdentificador");
		}
		
		if(StringUtils.isNotBlank(nmPessoa)){
			sql.append(" AND UPPER(pe.nm_pessoa) LIKE UPPER(:nmPessoa)");
		}
		
		if (limiteRegistros != null) {
			sql.append(" AND rownum <= :limiteRegistros");
		}
		
		return sql;
	}
	
	/**
	 * LMS-5590
	 * 
	 * Retorna os proprietários com o tipo de identificação CPF
	 * 
	 * @param nrIdentificador
	 * @param nmPessoa
	 * @param limiteRegistros
	 * @return
	 */
	private StringBuilder getQueryProprietarioSuggestCpf(
			final String nrIdentificador, final String nmPessoa,
			final Integer limiteRegistros) {
		final StringBuilder sql = new StringBuilder();
				
		sql.append("SELECT pr.id_proprietario,");
		sql.append("       pe.nr_identificacao,"); 
		sql.append("       pe.nm_pessoa,");
		sql.append("       (SELECT VI18N(vdm.ds_valor_dominio_i)");
		sql.append("         FROM valor_dominio vdm");
		sql.append("          INNER JOIN dominio dm");
		sql.append("           ON dm.id_dominio = vdm.id_dominio");
		sql.append("           WHERE vl_valor_dominio = pr.tp_proprietario");
		sql.append("            AND dm.nm_dominio = 'DM_TIPO_PROPRIETARIO') AS tp_proprietario");		
		sql.append(" FROM proprietario pr");
		sql.append("  JOIN pessoa pe");
		sql.append("   ON pr.id_proprietario = pe.id_pessoa");
		sql.append(" WHERE 1=1");
		sql.append(" AND pe.tp_identificacao like 'CPF'");
		
		if(StringUtils.isNotBlank(nrIdentificador)){
			sql.append(" AND pe.nr_identificacao LIKE :nrIdentificador");
		}
		
		if(StringUtils.isNotBlank(nmPessoa)){
			sql.append(" AND UPPER(pe.nm_pessoa) LIKE UPPER(:nmPessoa)");
		}
		
		if (limiteRegistros != null) {
			sql.append(" AND rownum <= :limiteRegistros");
		}
		
		return sql;
	}
	
	public Boolean hasPIS(Long idProprietario){
		StringBuffer sql = new StringBuffer();
		
		sql.append(" from Proprietario" );
		sql.append(" where 	idProprietario  = :idProprietario" );
		sql.append(" and    nrPis IS NULL" );
		sql.append(" and    tpSituacao = 'A' " );
		sql.append(" and    pessoa.tpPessoa = 'F' " );
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ID_PROPRIETARIO2, idProprietario);
		
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params).isEmpty();
	}

	public Proprietario findByIdProcesso(Long id) {
		StringBuilder hql = new StringBuilder();
		hql.append("  SELECT p " );
		hql.append("  FROM "+getPersistentClass().getName()+" p" );
		hql.append("  JOIN FETCH p.pessoa pe");
		hql.append("  JOIN FETCH p.filial fi");
		hql.append("  JOIN FETCH fi.pessoa pef");
		hql.append("  JOIN FETCH p.telefoneEndereco te");
		hql.append("  LEFT JOIN FETCH pe.unidadeFederativaExpedicaoRg uf");
		hql.append("  LEFT JOIN FETCH p.usuario u");
		hql.append("  LEFT JOIN FETCH p.pendencia pe");
		hql.append("  LEFT JOIN FETCH pe.ocorrencia oc");
		hql.append("  LEFT JOIN FETCH oc.eventoWorkflow ev");
		hql.append("  LEFT JOIN FETCH ev.tipoEvento tp");
		hql.append(" WHERE pe.idProcesso  = :idProcesso" );		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("idProcesso", id);
		
		Object result =  getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params);
		
		return (Proprietario)result;
	}
	
	public void removeByIdsAnexoProprietario(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + AnexoProprietario.class.getName() + " WHERE idAnexoProprietario IN (:id)", ids);
	}

	public AnexoProprietario findAnexoProprietarioById(Long idAnexoProprietario) {
		return (AnexoProprietario) getAdsmHibernateTemplate().load(AnexoProprietario.class, idAnexoProprietario);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAnexoProprietarioByIdProprietario(Long idProprietario) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoProprietario AS idAnexoProprietario,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.dsAnexo AS dsAnexo,");
		hql.append(" anexo.dhCriacao AS dhCriacao,");
		hql.append(" usuario.usuarioADSM.nmUsuario as nmUsuario)");
		hql.append(" FROM AnexoProprietario AS anexo");
		hql.append("  INNER JOIN anexo.proprietario proprietario");
		hql.append("  INNER JOIN anexo.usuario usuario");
		hql.append(" WHERE proprietario.idProprietario = ?");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idProprietario});
	}
	
	public Integer getRowCountAnexoProprietarioByIdProprietario(Long idProprietario) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_proprietario WHERE id_proprietario = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{idProprietario});
	}
}
