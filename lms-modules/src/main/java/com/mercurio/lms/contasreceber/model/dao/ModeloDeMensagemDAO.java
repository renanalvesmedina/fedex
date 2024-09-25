package com.mercurio.lms.contasreceber.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contasreceber.model.ModeloMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;

public class ModeloDeMensagemDAO  extends BaseCrudDao<ModeloMensagem, Long> {

	@Override
	protected Class getPersistentClass() {
		return ModeloMensagem.class;
	}
	
	public List<ModeloMensagem> find(YearMonthDay dataInicial,DomainValue tipo){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(getPersistentClass().getName() + " mls ");
		sql.addProjection(" mls ");
		if ( tipo != null ){
			sql.addCriteria("tpModeloMensagem", "=", tipo.getValue());
		}
		if ( dataInicial != null ){			
			sql.addCustomCriteria("(dtVigenciaInicial <= ? AND dtVigenciaFinal >= ?) or dtVigenciaFinal is null");
			sql.addCriteriaValue(dataInicial);
			sql.addCriteriaValue(dataInicial);
		}
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
	}
	
	//TODO: Alterar para utilizar a mesma regra do metodo: findDoctoServicoNaoFaturadoReport() da classe: DoctoServicoDAO.java
	public List<ModeloMensagem> findByMonitoramentoMensagem(MonitoramentoMensagem monitMsg) {
		StringBuilder sql = new StringBuilder("select distinct m.id_modelo_mensagem, m.id_usuario, m.ds_modelo_mensagem, m.tp_modelo_mensagem, m.dt_vigencia_inicial, m.dt_vigencia_final ")
		.append(",dbms_lob.substr(m.dc_modelo_assunto,4000,1)")
		.append(",dbms_lob.substr(m.dc_modelo_corpo,4000,1) ")
		.append("from MODELO_MENSAGEM m, MONITORAMENTO_MENSAGEM mo ")
		.append("where m.tp_modelo_mensagem = mo.tp_modelo_mensagem and ")
		.append("TRUNC(SYSDATE) between m.dt_vigencia_inicial and NVL(DT_VIGENCIA_FINAL, TRUNC(SYSDATE)) ")
		.append("and mo.id_monitoramento_mensagem = " + monitMsg.getIdMonitoramentoMensagem());
		
		SessionFactory sessionFactory = getAdsmHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		List mensagensDB = session.createSQLQuery(sql.toString()).list();
		
		List<ModeloMensagem> modeloMensagens = new ArrayList<ModeloMensagem>();
		if(!mensagensDB.isEmpty()){
			for(int i=0; i< mensagensDB.size(); i++) {
				ModeloMensagem modeloMensagem = new ModeloMensagem();
				Object[] msgArray = (Object[])mensagensDB.get(i);
				modeloMensagem.setIdModeloMensagem(((BigDecimal) msgArray[0]).longValue());
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(((BigDecimal) msgArray[1]).longValue());
				modeloMensagem.setUsuario(usuario);
				modeloMensagem.setDsModeloMensagem((String)msgArray[2]);
				DomainValue tpModeloMsg = new DomainValue(); 
				tpModeloMsg.setValue((String)msgArray[3]);
				modeloMensagem.setTpModeloMensagem(tpModeloMsg);
				modeloMensagem.setDtVigenciaInicial(new YearMonthDay(msgArray[4]));
				modeloMensagem.setDtVigenciaFinal(new YearMonthDay(msgArray[5]));
				modeloMensagem.setDcModeloAssunto((String)msgArray[6]);
				modeloMensagem.setDcModeloCorpo((String)msgArray[7]);
				modeloMensagens.add(modeloMensagem);
			}
		}

		return modeloMensagens;
	}

}