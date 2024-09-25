package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.DadoModeloMensagem;
import com.mercurio.lms.contasreceber.model.ModeloMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;

public class DadoModeloMensagemDAO  extends BaseCrudDao<DadoModeloMensagem, Long> {

	@Override
	protected Class getPersistentClass() {
		return DadoModeloMensagem.class;
	}
	
	public List<DadoModeloMensagem> findByModeloMensagemId(ModeloMensagem modeloMensagem) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(DadoModeloMensagem.class.getName(), "d" +
			" JOIN fetch d.idModeloMensagem m " );
		hql.addCriteria("m.idModeloMensagem", "=", modeloMensagem.getIdModeloMensagem());

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public List findDadosCorpoDoEmail(String sql, Map<String, String> parameters, MonitoramentoMensagem monitoramentoMsg) {
		try{
			if (sql.contains(":0")) {
				sql = sql.replace(":0", monitoramentoMsg.getIdMonitoramentoMensagem()+"");
				parameters.remove(":0");
			}
			
			if (!parameters.isEmpty()) {
				for (Map.Entry<String, String> entry : parameters.entrySet()) {
					sql = sql.replace(entry.getKey(), entry.getValue());
				}
			}
		}catch(Exception e) {
			throw new IllegalArgumentException("Número de parametros informados não fecha com a quantidade esperada pela query, favor verificar." + e.getMessage());
		}
		
		SessionFactory sessionFactory = getAdsmHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		
		return session.createSQLQuery(sql).list();
	}
	
}
