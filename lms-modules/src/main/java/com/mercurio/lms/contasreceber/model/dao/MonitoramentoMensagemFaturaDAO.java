package com.mercurio.lms.contasreceber.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemFatura;
import com.mercurio.lms.util.JTDateTimeUtils;

public class MonitoramentoMensagemFaturaDAO  extends BaseCrudDao<MonitoramentoMensagemFatura, Long> {

	@Override
	protected Class getPersistentClass() {
		return MonitoramentoMensagemFatura.class;
	}

	//TODO: Alterar para utilizar a mesma regra do metodo: findDoctoServicoNaoFaturadoReport() da classe: DoctoServicoDAO.java
	public List<Long> findFaturas(Long idMonitoramentoMensagem) {
		StringBuilder sql = new StringBuilder("select mm.id_fatura from MONITORAMENTO_MENSAGEM mo, MONIT_MENS_FATURA mm, FATURA f ")
		.append("where mm.ID_MONITORAMENTO_MENSAGEM = mo.ID_MONITORAMENTO_MENSAGEM ")
		.append("and f.id_fatura = mm.id_fatura ")
		.append("and f.tp_situacao_fatura = 'BL' ")
		.append("and (f.tp_situacao_aprovacao IS NULL OR f.tp_situacao_aprovacao = 'A') ")
		.append("and mo.ID_MONITORAMENTO_MENSAGEM = " + idMonitoramentoMensagem);
		
		SessionFactory sessionFactory = getAdsmHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		List mensagensDB = session.createSQLQuery(sql.toString()).list();
		
		List<Long> faturas = new ArrayList<Long>();
		
		for(int i=0; i< mensagensDB.size(); i++)
			faturas.add(((BigDecimal) mensagensDB.get(i)).longValue());
		
		return faturas;
	}
	
	//TODO: Alterar para utilizar a mesma regra do metodo: findDoctoServicoNaoFaturadoReport() da classe: DoctoServicoDAO.java
	public List<Long> getBoletos(Long idMonitoramentoMensagem) {
		StringBuilder sql = new StringBuilder("select b.id_fatura from MONITORAMENTO_MENSAGEM mo, MONIT_MENS_FATURA mm, BOLETO b, FATURA f ")
		.append("where mm.ID_MONITORAMENTO_MENSAGEM = mo.ID_MONITORAMENTO_MENSAGEM ")
		.append("and b.id_fatura = mm.id_fatura ")
		.append("and b.id_fatura = f.id_fatura ")
		.append("and b.tp_situacao_boleto not in ('CA', 'LI', 'RE') ") 
		.append("and f.tp_situacao_fatura = 'BL' ")
		.append("and (f.tp_situacao_aprovacao IS NULL OR f.tp_situacao_aprovacao = 'A') ")
		.append("and NOT EXISTS (SELECT 1 FROM historico_boleto hb WHERE b.id_boleto = hb.id_boleto AND hb.tp_situacao_aprovacao IN ('E', 'R')) ")
		.append("and mo.ID_MONITORAMENTO_MENSAGEM = " + idMonitoramentoMensagem);
		
		SessionFactory sessionFactory = getAdsmHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		List mensagensDB = session.createSQLQuery(sql.toString()).list();
		
		List<Long> boletos = new ArrayList<Long>();
		
		for(int i=0; i< mensagensDB.size(); i++) 
			boletos.add(((BigDecimal) mensagensDB.get(i)).longValue());
		
		return boletos;
	}
	
	 
}
