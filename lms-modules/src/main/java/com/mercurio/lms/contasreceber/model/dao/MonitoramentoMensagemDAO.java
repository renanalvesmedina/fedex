package com.mercurio.lms.contasreceber.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemDetalhe;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemEvento;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemFatura;
import com.mercurio.lms.util.JTDateTimeUtils;

public class MonitoramentoMensagemDAO  extends BaseCrudDao<MonitoramentoMensagem, Long> {

	@Override
	protected Class getPersistentClass() {
		return MonitoramentoMensagem.class;
	}

	@SuppressWarnings("unchecked")
	public List<MonitoramentoMensagem> findByDhProcessamentoNull(){
		String sql = " select * \n"
				+ " FROM MONITORAMENTO_MENSAGEM mm \n"
				+ " WHERE nvl(TO_CHAR(mm.dh_processamento, 'dd-MM-yyyy'), '01-01-1900') = '01-01-1900' \n"
				+ " 	AND ( \n"
				+ " 		mm.tp_modelo_mensagem <> 'FA' \n"
				+ " 		OR mm.tp_modelo_mensagem = 'FA' \n"
				+ " 		AND (SELECT COUNT(c.id_cliente) FROM \n"
				+ " 				monit_mens_fatura mmf \n"
				+ " 				,fatura f, cliente c \n"
				+ " 			WHERE mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem \n"
				+ " 			 AND mmf.id_fatura = f.id_fatura \n"
				+ " 			 AND F.ID_CLIENTE = C.ID_CLIENTE \n"
				+ " 			 AND C.bl_envia_dacte_xml_fat = 'N' \n"
				+ " 			 AND c.BL_ENVIA_DOCS_FATURAMENTO_NAS  = 'N' \n"
				+ " 			 AND rownum = 1) = 1 \n"
				+ " 		)";

		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(MonitoramentoMensagem.class).list();

	}

	/**
	 * Método que gerará os dados que alimentarão o microservice de automação do Financeiro
	 * LMSA-3322/3325
	 *
	 * @return List<MonitoramentoMensagem>
	 */
	@SuppressWarnings("unchecked")
	public List<MonitoramentoMensagem> findMonitoramentosFaturar() {
		StringBuilder sql = new StringBuilder("select * ")
				.append("FROM MONITORAMENTO_MENSAGEM mm ")
				.append("WHERE nvl(TO_CHAR(mm.dh_processamento, 'dd-MM-yyyy'), '01-01-1900') = '01-01-1900' ")
				.append("AND ( mm.tp_modelo_mensagem in ('FA', 'SE') ")
				.append("AND nvl((SELECT c.bl_envia_docs_faturamento_nas ")
				.append("FROM monit_mens_fatura mmf ")
				.append(",fatura f ")
				.append(",cliente c ")
				.append("WHERE mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem ")
				.append("AND mmf.id_fatura = f.id_fatura ")
				.append("AND F.ID_CLIENTE = C.ID_CLIENTE ")
				.append("AND rownum = 1 ")
				.append("), 'N') = 'S') ");

		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).addEntity(MonitoramentoMensagem.class).list();
	}

	@SuppressWarnings("unchecked")
	public List<MonitoramentoMensagem> findMonitoramentoMensagemByIdFatura(Long idFatura) {
		String sql = "SELECT mm.* FROM MONITORAMENTO_MENSAGEM mm \n" +
				"INNER JOIN MONIT_MENS_FATURA mmf \n" +
				"ON mm.ID_MONITORAMENTO_MENSAGEM = mmf.ID_MONITORAMENTO_MENSAGEM \n" +
				"WHERE mmf.ID_FATURA = "+idFatura.longValue()+" \n" +
				"ORDER BY mm.ID_MONITORAMENTO_MENSAGEM DESC \n";

		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(MonitoramentoMensagem.class).list();
	}

	//TODO: validar esta query
	public List<MonitoramentoMensagem> getMonitoramentoMensagemByIdFatura(Long idFatura) {
		String sql = " select mm.* \n"
				+ " from MONITORAMENTO_MENSAGEM MM 	\n"
				+ " 	,MONIT_MENS_FATURA MMF 		\n"
				+ " 	,FATURA F 	\n"
				+ " where MM.ID_MONITORAMENTO_MENSAGEM = MMF.ID_MONITORAMENTO_MENSAGEM \n"
				+ " 	and F.ID_FATURA =  	\n" + idFatura
				+ " 	and rownum = 1		\n"
				+ " order by MM.ID_MONITORAMENTO_MENSAGEM desc	\n";

		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(MonitoramentoMensagem.class).list();

	}

	public void updateDhProcessamentoMonitoramentoMensagem(MonitoramentoMensagem mm) {
		MonitoramentoMensagem monitoramentoMensagem = this.findById(mm.getIdMonitoramentoMensagem());
		if (monitoramentoMensagem != null) {
			monitoramentoMensagem.setDhProcessamento(JTDateTimeUtils.getDataHoraAtual());
			store(monitoramentoMensagem);
		}
	}

	public MonitoramentoMensagem findByIdMonitoramentoMensagem(Long idMonitoramentoMensagem){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(MonitoramentoMensagem.class.getName());
		sql.addCriteria("idMonitoramentoMensagem", "=", idMonitoramentoMensagem);
		List list = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

		if(list != null && !list.isEmpty()){
			return (MonitoramentoMensagem) list.get(0);
		}else{
			return null;
		}


	}

	public MonitoramentoMensagemDetalhe findMonitoramentoDetalhe(Long idMonitoramentoMensagem){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(MonitoramentoMensagemDetalhe.class.getName());
		sql.addCriteria("monitoramentoMensagem.idMonitoramentoMensagem", "=", idMonitoramentoMensagem);
		@SuppressWarnings("unchecked")
		List<MonitoramentoMensagemDetalhe> result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if ( result.isEmpty() ){
			return null;
		}
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<MonitoramentoMensagemEvento> findMonitoramentoEventos(Long idMonitoramentoMensagem){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(MonitoramentoMensagemEvento.class.getName());
		sql.addCriteria("monitoramentoMensagem.idMonitoramentoMensagem", "=", idMonitoramentoMensagem);
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	@SuppressWarnings("unchecked")
	public List<MonitoramentoMensagem> findHistoricoMensagensByFaturaId(Long faturaId) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("distinct mmf.monitoramentoMensagem");
		sql.addFrom(MonitoramentoMensagemFatura.class.getName()+" mmf");
		sql.addCriteria("mmf.fatura.idFatura", "=", faturaId);
		sql.addOrderBy("mmf.monitoramentoMensagem.dhInclusao desc");
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	public List findHistoricoMensagensByLoteCobrancaId(Long idLoteCobrancaTerceira) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("distinct mm");
		sql.addFrom(MonitoramentoMensagem.class.getName()+" mm");
		sql.addCriteria("mm.dsParametro", "like", "%:\""+idLoteCobrancaTerceira+" \"% ".trim());
		sql.addCriteria("mm.tpModeloMensagem", "=", "GL");
		sql.addOrderBy("mm.idMonitoramentoMensagem desc");
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	public List<Object[]> findMonitoramentoMensagem(Long idFatura, String dataInicial, String dataFinal) {
		/**
		 *    BETWEEN TO_DATE('23/02/2021 10:30:48', 'dd/MM/yyyy HH24:MI:SS') 
AND TO_DATE('24/02/2021 10:30:48', 'dd/MM/yyyy HH24:MI:SS');
		 * */
		StringBuilder sql = new StringBuilder("SELECT mm.ID_MONITORAMENTO_MENSAGEM FROM MONITORAMENTO_MENSAGEM mm ")
				.append("INNER JOIN MONIT_MENS_FATURA mmf")
				.append(" ON mm.ID_MONITORAMENTO_MENSAGEM = mmf.ID_MONITORAMENTO_MENSAGEM ")
				.append("INNER JOIN FATURA f")
				.append(" ON mmf.ID_FATURA = f.ID_FATURA AND f.ID_FATURA = :idFatura ")
				.append(" WHERE ")
				.append("TO_DATE(TO_CHAR(mm.DH_INCLUSAO, 'dd/MM/yyyy HH24:MI:SS'), 'dd/MM/yyyy HH24:MI:SS') ")
				.append("BETWEEN TO_DATE(:datainicio, 'dd/MM/yyyy HH24:MI:SS') ")
				.append(" AND TO_DATE(:dataFinal, 'dd/MM/yyyy HH24:MI:SS')");
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("idFatura", idFatura);
		parameters.put("datainicio", dataInicial);
		parameters.put("dataFinal", dataFinal);
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, configureSqlQueryIdMonitoramentoMensagem);
		
	}
	
	private ConfigureSqlQuery configureSqlQueryIdMonitoramentoMensagem = new ConfigureSqlQuery() {
	        @Override
	        public void configQuery(SQLQuery sqlQuery) {
	        	sqlQuery.addScalar("ID_MONITORAMENTO_MENSAGEM", Hibernate.LONG);
	        }
	 };
}
