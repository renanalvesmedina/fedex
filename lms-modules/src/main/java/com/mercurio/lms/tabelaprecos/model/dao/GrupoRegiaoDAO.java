package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tabelaprecos.model.CloneGrupoRegiaoDTO;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class GrupoRegiaoDAO extends BaseCrudDao<GrupoRegiao, Long> {
	@Override
	protected Class getPersistentClass() {
		return GrupoRegiao.class;
	}

	public GrupoRegiao findByIdGrupoRegiao(Long idGrupoRegiao) {
		Map<String, Object>  paramValues = new HashMap();

		StringBuilder sql = new StringBuilder();
		sql.append(" select gr ");
		sql.append(" from ").append(getPersistentClass().getName()).append(" gr ");
		sql.append(" inner join fetch gr.tabelaPreco tp ");
		sql.append(" inner join fetch tp.tipoTabelaPreco ttp ");
		sql.append(" inner join fetch tp.subtipoTabelaPreco stp ");
		sql.append(" inner join fetch gr.unidadeFederativa uf ");
		sql.append(" inner join fetch uf.pais pais ");
		sql.append(" where gr.idGrupoRegiao = :idGR");
		paramValues.put("idGR", idGrupoRegiao);

		return (GrupoRegiao)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), paramValues);
	}

	public ResultSetPage<GrupoRegiao> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ").append(getPersistentClass().getName()).append(" as grupoRegiao ");
		sql.append(" inner join fetch grupoRegiao.tabelaPreco tabelaPreco ");
		sql.append(" inner join fetch tabelaPreco.tipoTabelaPreco tipoTabelaPreco ");
		sql.append(" inner join fetch tabelaPreco.subtipoTabelaPreco subtipoTabelaPreco ");
		sql.append(" inner join fetch grupoRegiao.unidadeFederativa unidadeFederativa ");
		sql.append(" where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();

		//2.3
		if (MapUtils.getObject(criteria, "idGrupoRegiao")!= null) {
			sql.append(" and grupoRegiao.id = :idGrupoRegiao");
		} else {
			if(MapUtils.getObject(criteria, "idUnidadeFederativa") != null) {
				sql.append(" and unidadeFederativa.id = :idUnidadeFederativa");
		}
			if (MapUtils.getObject(criteria, "idTabelaPreco") != null) {
				sql.append(" and tabelaPreco.id = :idTabelaPreco");
			}
			if(MapUtils.getObject(criteria, "dsGrupoRegiao") != null) {
				sql.append(" and grupoRegiao.dsGrupoRegiao like :dsGrupoRegiao");
		}
	}

		sql.append(" order by unidadeFederativa.nmUnidadeFederativa, grupoRegiao.dsGrupoRegiao asc ");

		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, sql.toString());
	}

	public List findGruposRegiao(){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(GrupoRegiao.class.getName());
		sql.addOrderBy("idGrupoRegiao");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public GrupoRegiao store(GrupoRegiao grupoRegiao){
		super.store(grupoRegiao);
		return grupoRegiao;
	}

	@Override
	public void removeById(Long grupoRegiaoId) {
		super.removeById(grupoRegiaoId);
	}

	public List findByUfAndTabela(Long idUnidadeFederativa, Long idTabela) {

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(GrupoRegiao.class.getName() + " as gr ");
		sql.addCriteria("gr.unidadeFederativa.id", "=" ,idUnidadeFederativa);
		sql.addCriteria("gr.tabelaPreco.id", "=" ,idTabela);

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public List findByTabelaPreco(Long idTabela) {
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(GrupoRegiao.class.getName() + " as gr ");
		sql.addCriteria("gr.tabelaPreco.id", "=" ,idTabela);

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public List<GrupoRegiao> findGruposRegiao(Long idTabelaPreco, Long idUnidadeFederativa) {

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "grupo")
		.add(Restrictions.eq("tabelaPreco.id", idTabelaPreco));

		if(idUnidadeFederativa != null){
			dc.add(Restrictions.eq("unidadeFederativa.id", idUnidadeFederativa));
		}

		return findByDetachedCriteria(dc);
	}

	public List findGrupoRegiao(Long idTabelaPreco, Long idMunicipio) {

		StringBuilder queryString = new StringBuilder()

		.append(" select  grupo_regiao.id_grupo_regiao as id_grupo_regiao  ")
		.append(" from grupo_regiao, municipio, unidade_federativa  ")
		.append(" where municipio.id_municipio = ").append(idMunicipio)
		.append(" and   grupo_regiao.id_unidade_federativa = municipio.id_unidade_federativa ")
		.append(" and   municipio.id_unidade_federativa = unidade_federativa.id_unidade_federativa ")
		.append(" and   municipio.id_municipio <> unidade_federativa.id_capital ")
		.append(" and   grupo_regiao.id_tabela_preco = ").append(idTabelaPreco)
		.append(" and   not exists (select 1 ")
		.append(" from municipio_grupo_regiao ")
		.append(" where municipio_grupo_regiao.id_grupo_regiao = grupo_regiao.id_grupo_regiao)	");

		return getSession().createSQLQuery(queryString.toString()).list();
	}

	//LMS-3626
	public GrupoRegiao findByGrupoTabela(Long idGrupoRegiao, Long idTabelaPreco) {

		Map<String, Object> paramValues = new HashMap();

		StringBuilder sql = new StringBuilder();
		sql.append(" select grNovo ");
		sql.append(" from ").append(getPersistentClass().getName()).append(" grAntigo ");
		sql.append(", ").append(getPersistentClass().getName()).append(" grNovo ");
		sql.append(" where grAntigo.id = :idGR and");
		sql.append(" grNovo.tabelaPreco.id = :idTP and");
		sql.append(" grNovo.unidadeFederativa.id = grAntigo.unidadeFederativa.id and");
		sql.append(" grNovo.dsGrupoRegiao = grAntigo.dsGrupoRegiao ");

		paramValues.put("idGR", idGrupoRegiao);
		paramValues.put("idTP", idTabelaPreco);

		return (GrupoRegiao) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), paramValues);
	}

	public GrupoRegiao findByDsGrupoTabelaUf(String dsGrupoRegiao, Long idTabelaPreco, Long idUnidadeFederativa) {

		Map<String, Object> paramValues = new HashMap();

		StringBuilder sql = new StringBuilder();
		sql.append(" select gr ");
		sql.append(" from ").append(getPersistentClass().getName()).append(" gr ");
		sql.append(" where gr.dsGrupoRegiao = :dsGR and");
		sql.append(" gr.tabelaPreco.id = :idTP and");
		sql.append(" gr.unidadeFederativa.id = :idUnidadeFederativa ");

		paramValues.put("dsGR", dsGrupoRegiao);
		paramValues.put("idTP", idTabelaPreco);
		paramValues.put("idUnidadeFederativa", idUnidadeFederativa);

		return (GrupoRegiao) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), paramValues);
	}



	public List<CloneGrupoRegiaoDTO> findRotaGruposRegiaoClonados(Long idTabelaBase, Long idTabelaNova) {
		StringBuilder sql = new StringBuilder()
		.append("SELECT tabRotas.id_rota_preco idRotaPreco, ")
        .append(" ( SELECT GRN.ID_GRUPO_REGIAO ")
        .append("   FROM   GRUPO_REGIAO GRV, GRUPO_REGIAO GRN ")
        .append("   WHERE  GRV.ID_GRUPO_REGIAO = tabRotas.id_grupo_regiao_origem ")
        .append("   AND    GRN.ID_TABELA_PRECO = :idTabelaNova ")
        .append("   AND    GRN.ID_UNIDADE_FEDERATIVA = GRV.ID_UNIDADE_FEDERATIVA ")
        .append("   AND    GRN.DS_GRUPO_REGIAO = GRV.DS_GRUPO_REGIAO ")
        .append(" ) idGrupoRegiaoOrigem, ")
        .append(" ( SELECT GRN.ID_GRUPO_REGIAO ")
        .append("   FROM   GRUPO_REGIAO GRV, GRUPO_REGIAO GRN ")
        .append("   WHERE  GRV.ID_GRUPO_REGIAO = tabRotas.id_grupo_regiao_destino ")
        .append("   AND    GRN.ID_TABELA_PRECO = :idTabelaNova ")
        .append("   AND    GRN.ID_UNIDADE_FEDERATIVA = GRV.ID_UNIDADE_FEDERATIVA ")
        .append("   AND    GRN.DS_GRUPO_REGIAO = GRV.DS_GRUPO_REGIAO ")
        .append(" ) idGrupoRegiaoDestino ")
        .append(" FROM   (  ")
        .append("   Select rp.id_rota_preco id_rota_preco, ")
        .append("   rp.id_grupo_regiao_origem id_grupo_regiao_origem, ")
        .append("   rp.id_grupo_regiao_destino id_grupo_regiao_destino ")
        .append("   from tarifa_preco_rota tpr, rota_preco rp ")
        .append("   where tpr.id_tabela_preco = :idTabelaBase ")
        .append("   and   rp.id_rota_preco = tpr.id_rota_preco ")
        .append("   and   ((rp.id_grupo_regiao_destino is not null and rp.id_grupo_regiao_origem is not null) ")
        .append("   or(rp.id_grupo_regiao_destino is not null and rp.id_grupo_regiao_origem is null) ")
        .append("   or(rp.id_grupo_regiao_destino is null and rp.id_grupo_regiao_origem is not null)) ")
        .append(" ) tabRotas ");

    	ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
    		@Override
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idRotaPreco", Hibernate.LONG);
    			sqlQuery.addScalar("idGrupoRegiaoOrigem", Hibernate.LONG);
    			sqlQuery.addScalar("idGrupoRegiaoDestino", Hibernate.LONG);
    		}
    	};

    	Map<String, Object> namedParams = new HashMap<String, Object>();
    	namedParams.put("idTabelaBase", idTabelaBase);
    	namedParams.put("idTabelaNova", idTabelaNova);
    	return (List<CloneGrupoRegiaoDTO>) getAdsmHibernateTemplate().findBySql(sql.toString(), namedParams, configSql, new AliasToBeanResultTransformer(CloneGrupoRegiaoDTO.class));
	}

	public void insertCloneGrupoRegiao(Long idOldTabelaPreco, Long idNewTabelaPreco){

		StringBuilder sql = new StringBuilder()
				.append("INSERT INTO grupo_regiao  ( ")
				.append("id_grupo_regiao, ")
				.append("ds_grupo_regiao, ")
				.append("tp_ajuste, ")
				.append("tp_valor_ajuste, ")
				.append("vl_ajuste_padrao, ")
				.append("vl_ajuste_minimo, ")
				.append("id_unidade_federativa, ")
				.append("id_tabela_preco, ")
				.append("tp_ajuste_advalorem, ")
				.append("tp_valor_ajuste_advalorem, ")
				.append("vl_ajuste_padrao_advalorem, ")
				.append("vl_ajuste_minimo_advalorem, ")
				.append("id_grupo_regiao_origem )")
				.append("SELECT  ")
				.append("grupo_regiao_sq.NEXTVAL, ")
				.append("gr.ds_grupo_regiao, ")
				.append("gr.tp_ajuste, ")
				.append("gr.tp_valor_ajuste, ")
				.append("gr.vl_ajuste_padrao, ")
				.append("gr.vl_ajuste_minimo, ")
				.append("gr.id_unidade_federativa, ")
				.append(idNewTabelaPreco).append( ", ")
				.append("gr.tp_ajuste_advalorem, ")
				.append("gr.tp_valor_ajuste_advalorem, ")
				.append("gr.vl_ajuste_padrao_advalorem, ")
				.append("gr.vl_ajuste_minimo_advalorem, ")
				.append("gr.id_grupo_regiao ")
				.append("FROM ")
				.append("grupo_regiao gr  ")
				.append("WHERE  ")
				.append("gr.id_tabela_preco = :idOldTabelaPreco");

		Map<String, Object> params = new HashMap<>();
		params.put("idOldTabelaPreco", idOldTabelaPreco);
		getAdsmHibernateTemplate().executeUpdateBySql(String.valueOf(sql), params);
	}
}
