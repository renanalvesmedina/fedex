package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.MunicipioTrtCliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MunicipioTrtClienteDAO extends BaseCrudDao<MunicipioTrtCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return MunicipioTrtCliente.class;
	}

	public List<MunicipioTrtCliente> findTRTVigenteParaCliente(Long idClienteDevedor) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mtc");
		dc.createAlias("mtc.trtCliente", "tc");
		dc.createAlias("tc.cliente","c");
		dc.createAlias("mtc.municipio","m");
		dc.add(Restrictions.eq("c.id", idClienteDevedor))
			.add(Restrictions.le("tc.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()))
			.add(Restrictions.ge("tc.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		return (List<MunicipioTrtCliente>) getAdsmHibernateTemplate().findByCriteria(dc);		
	}
	
	public List<MunicipioTrtCliente> findByIdTrtCliente(Long idTrtCliente){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mtc");
		dc.createAlias("mtc.trtCliente", "tc");
		dc.add(Restrictions.eq("tc.idTrtCliente", idTrtCliente));
		return (List<MunicipioTrtCliente>)getAdsmHibernateTemplate().findByDetachedCriteria(dc);	
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMunicipioTrtClienteByIdTrtCliente(Long idTrtCliente){
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(mtc.idMunicipioTrtCliente as idMunicipioTrtCliente, m.idMunicipio as idMunicipio, concat(m.nmMunicipio, ' - ', uf.sgUnidadeFederativa) as nmMunicipioComUF, mtc.blCobraTrt as blCobraTrt) ");
		hql.append("FROM ");
		hql.append(getPersistentClass().getName()).append(" mtc ");
		hql.append("INNER JOIN mtc.trtCliente tc ");
		hql.append("INNER JOIN mtc.municipio m ");
		hql.append("INNER JOIN m.unidadeFederativa uf ");
		hql.append("WHERE ");
		hql.append("tc.idTrtCliente = ? ");
		hql.append("ORDER BY m.nmMunicipio");

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idTrtCliente});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void removeMunicipioTrtClienteByIdTrtCliente(Long idTrtCliente) {
		StringBuilder hql = new StringBuilder("delete from ");
		hql.append(getPersistentClass().getName()).append(" mtc ");
    	hql.append(" where mtc.trtCliente.idTrtCliente = ? ");
		List param = new ArrayList();
    	param.add(idTrtCliente);
    	super.executeHql(hql.toString(), param);
	}

	public List<Object[]> cobraPorTabelaMunicipio(Long idMunicipioEntrega, Long tabelaPreco) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" Select MUNICIPIO_TRT_CLIENTE.BL_COBRA_TRT cobraTRT");
		sql.append(" FROM TRT_CLIENTE, MUNICIPIO_TRT_CLIENTE ");
		sql.append(" WHERE TRT_CLIENTE.ID_TABELA_PRECO = :tabelaPreco ");
		sql.append(" AND TRT_CLIENTE.DT_VIGENCIA_INICIAL <= trunc(sysdate) ");
		sql.append(" AND TRT_CLIENTE.DT_VIGENCIA_FINAL >= trunc(sysdate) ");
		sql.append(" AND MUNICIPIO_TRT_CLIENTE.ID_TRT_CLIENTE = TRT_CLIENTE.ID_TRT_CLIENTE ");
		sql.append(" AND MUNICIPIO_TRT_CLIENTE.ID_MUNICIPIO = :idMunicipioEntrega ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idMunicipioEntrega", idMunicipioEntrega);
		params.put("tabelaPreco", tabelaPreco);

		final ConfigureSqlQuery configQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("cobraTRT", Hibernate.STRING);
			}
		};
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configQuery);
	}

	public void insertMunicipio(Long idTabelaNova, Long idTabelaBase) {

		String sql = new StringBuilder()
		.append(" insert into municipio_trt_cliente ( ")
		.append("	id_municipio_trt_cliente ")
		.append(" 	,id_municipio ")
		.append(" 	,id_trt_cliente ")
		.append(" 	,bl_cobra_trt ) ")
		.append(" select municipio_trt_cliente_sq.nextval id_municipio_trt_cliente ")
		.append(" 	,id_municipio ")
		.append(" 	,( ")
		.append(" 		select id_trt_cliente ")
		.append(" 		from trt_cliente ")
		.append(" 		where id_tabela_preco = :idTabelaNova ")
		.append(" 		) id_trt_cliente ")
		.append(" 	,bl_cobra_trt ")
		.append(" from municipio_trt_cliente ")
		.append(" where id_trt_cliente IN ( ")
		.append(" 		select id_trt_cliente ")
		.append(" 		from trt_cliente ")
		.append(" 		where id_tabela_preco = :idTabelaBase ) ").toString();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTabelaNova", idTabelaNova);
		params.put("idTabelaBase", idTabelaBase);

		getAdsmHibernateTemplate().executeUpdateBySql(sql, params);
	}

	public List<Object[]> cobraPorTabela(Long tabelaPreco) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" Select MUNICIPIO_TRT_CLIENTE.BL_COBRA_TRT cobraTRT,  ");
		sql.append(" 		MUNICIPIO_TRT_CLIENTE.ID_MUNICIPIO idMunicipio");
		sql.append(" FROM TRT_CLIENTE, MUNICIPIO_TRT_CLIENTE ");
		sql.append(" WHERE TRT_CLIENTE.ID_TABELA_PRECO = :tabelaPreco ");
		sql.append(" AND TRT_CLIENTE.DT_VIGENCIA_INICIAL <= trunc(sysdate) ");
		sql.append(" AND TRT_CLIENTE.DT_VIGENCIA_FINAL >= trunc(sysdate) ");
		sql.append(" AND MUNICIPIO_TRT_CLIENTE.ID_TRT_CLIENTE = TRT_CLIENTE.ID_TRT_CLIENTE ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tabelaPreco", tabelaPreco);

		final ConfigureSqlQuery configQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("cobraTRT", Hibernate.STRING);
				sqlQuery.addScalar("idMunicipio", Hibernate.LONG);
			}
		};
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configQuery);
	}


	public List<Object[]> findTabelaPadraoModal(Long idServico) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select P.ID_TABELA_PRECO idTabela ");
		sql.append("  from TABELA_PRECO P, TIPO_TABELA_PRECO T, SUBTIPO_TABELA_PRECO STP ");
		sql.append(" where P.ID_TIPO_TABELA_PRECO = T.ID_TIPO_TABELA_PRECO ");
		sql.append("    and (T.TP_TIPO_TABELA_PRECO = 'M' or T.TP_TIPO_TABELA_PRECO = 'T' or T.TP_TIPO_TABELA_PRECO = 'A') ");
		sql.append("    and STP.ID_SUBTIPO_TABELA_PRECO = P.ID_SUBTIPO_TABELA_PRECO ");
		sql.append("    and STP.TP_SUBTIPO_TABELA_PRECO = 'X' ");
		sql.append("    and T.ID_SERVICO = :idServico ");
		sql.append("   and P.BL_EFETIVADA = 'S' ");
		sql.append("    and trunc(current_date) between P.DT_VIGENCIA_INICIAL and P.DT_VIGENCIA_FINAL ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idServico", idServico);

		final ConfigureSqlQuery configQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idTabela", Hibernate.LONG);
			}
		};
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configQuery);
	}
}
