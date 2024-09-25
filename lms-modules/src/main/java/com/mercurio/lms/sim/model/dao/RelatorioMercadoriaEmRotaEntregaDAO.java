package com.mercurio.lms.sim.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.sim.model.MonitoramentoCCT;

public class RelatorioMercadoriaEmRotaEntregaDAO extends BaseCrudDao{

	public ResultSetPage findPaginatedRelatorioMercadoriaEmRota(TypedFlatMap criteria) {
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("idDocumento", Hibernate.LONG);
				sqlQuery.addScalar("idClienteRemetente", Hibernate.LONG);
				sqlQuery.addScalar("filialDestino", Hibernate.STRING);
				sqlQuery.addScalar("nrNotaFiscal", Hibernate.LONG);
				sqlQuery.addScalar("dhEmissaoManifesto", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("modal", Hibernate.STRING);
				sqlQuery.addScalar("tpDoctoServico", Hibernate.STRING);
				sqlQuery.addScalar("filialOrigem", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("ciaAerea", Hibernate.STRING);
				sqlQuery.addScalar("nrAwb", Hibernate.STRING);
				sqlQuery.addScalar("nmClienteRemetente", Hibernate.STRING);
				sqlQuery.addScalar("nmClienteDestinatario", Hibernate.STRING);
				sqlQuery.addScalar("nmMunicipio", Hibernate.STRING);
				sqlQuery.addScalar("sgUf", Hibernate.STRING);
				sqlQuery.addScalar("situacao", Hibernate.STRING);
				sqlQuery.addScalar("dpe", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("localizacao",Hibernate.STRING);
			}
		};
		StringBuilder sql = montaQuerySqlRelatorioMercadoriaEmRotaEntrega(criteria);
		
		ResultSetPage rs = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(),(int)1,(int)1000, new HashedMap(), configSql);
		return rs;
	}
	
	 public StringBuilder montaQuerySqlRelatorioMercadoriaEmRotaEntrega(TypedFlatMap criteria){
		  	
		 StringBuilder sql = new StringBuilder();
		 
		 String delimitador = "";
		 
		 sql.append("SELECT ")
		 .append(" DS.ID_DOCTO_SERVICO           											AS idDocumento, ")
		 .append(" DS.ID_CLIENTE_REMETENTE													AS idClienteRemetente, ")
		 .append(" FD.SG_FILIAL 															AS filialDestino, ")
		 .append(" NFC.NR_NOTA_FISCAL 														AS nrNotaFiscal, ")
		 .append(" MAN.DH_EMISSAO_MANIFESTO													AS dhEmissaoManifesto, ")
		 .append(PropertyVarcharI18nProjection.createProjection("S.DS_SERVICO_I")).append(" AS modal,")
		 .append(" DS.TP_DOCUMENTO_SERVICO 													AS tpDoctoServico, ")
		 .append(" FO.SG_FILIAL 															AS filialOrigem, ")
		 .append(" DS.NR_DOCTO_SERVICO 														AS nrDoctoServico, ")
		 .append(" PA.NM_PESSOA 															AS ciaAerea, ")
		 .append(" (SELECT AWB.DS_SERIE || '/' || AWB.NR_AWB || '-' || AWB.DV_AWB  FROM CTO_AWB, AWB WHERE DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND CTO_AWB.ID_AWB = AWB.ID_AWB(+)AND rownum = 1) AS nrAwb, ")
		 .append(" PR.NM_PESSOA 															AS nmClienteRemetente, ")
		 .append(" PD.NM_PESSOA 															AS nmClienteDestinatario, ")
		 .append(" M.NM_MUNICIPIO 															AS nmMunicipio, ")
		 .append(" UF.SG_UNIDADE_FEDERATIVA 												AS sgUf, ")
		 .append(" MC.TP_SITUACAO_NF_CCT 													AS situacao,")
		 .append(" DS.DT_PREV_ENTREGA 														AS dpe, ")
		 .append(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I")).append("AS localizacao")
		 
		 .append(" FROM DOCTO_SERVICO DS ")
		 .append(" LEFT JOIN FILIAL FD on DS.ID_FILIAL_DESTINO = FD.ID_FILIAL ")
		 .append(" JOIN FILIAL FO on DS.ID_FILIAL_ORIGEM = FO.ID_FILIAL ")
		 .append(" JOIN NOTA_FISCAL_CONHECIMENTO NFC on DS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO ")
		 .append(" LEFT JOIN SERVICO S on DS.ID_SERVICO = S.ID_SERVICO ")
		 .append(" LEFT JOIN CTO_AWB CTA on DS.ID_DOCTO_SERVICO = CTA.ID_CONHECIMENTO ")
		 .append(" LEFT JOIN AWB ON CTA.ID_AWB = AWB.ID_AWB ")
		 .append(" LEFT JOIN CIA_FILIAL_MERCURIO CFM on AWB.ID_CIA_FILIAL_MERCURIO = CFM.ID_CIA_FILIAL_MERCURIO ")
		 .append(" LEFT JOIN PESSOA PA on CFM.ID_EMPRESA = PA.ID_PESSOA ")
		 .append(" JOIN PESSOA PR on DS.ID_CLIENTE_REMETENTE = PR.ID_PESSOA ")
		 .append(" LEFT JOIN PESSOA PD on DS.ID_CLIENTE_DESTINATARIO = PD.ID_PESSOA ")
		 .append(" LEFT JOIN LOCALIZACAO_MERCADORIA LM on DS.ID_LOCALIZACAO_MERCADORIA = LM. ID_LOCALIZACAO_MERCADORIA ")
		 .append(" LEFT JOIN MONITORAMENTO_CCT MC on DS.ID_DOCTO_SERVICO = MC.ID_DOCTO_SERVICO AND NFC.NR_CHAVE = MC.NR_CHAVE ")
		 .append(" AND (SELECT COUNT(*) FROM VALOR_DOMINIO VD INNER JOIN DOMINIO D ON D.ID_DOMINIO = VD.ID_DOMINIO AND D.NM_DOMINIO = 'DM_SITUACAO_NF_CCT' WHERE VD.VL_VALOR_DOMINIO = MC.TP_SITUACAO_NF_CCT) >= 1 ")
		 .append(" LEFT JOIN PESSOA PF on FD.ID_FILIAL = PF.ID_PESSOA ")
		 .append(" LEFT JOIN ENDERECO_PESSOA EP on PF.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA ")
		 .append(" LEFT JOIN MUNICIPIO M on EP.ID_MUNICIPIO = M.ID_MUNICIPIO ")
		 .append(" LEFT JOIN UNIDADE_FEDERATIVA UF on M.ID_UNIDADE_FEDERATIVA = UF.ID_UNIDADE_FEDERATIVA ")
		 .append(" INNER JOIN MANIFESTO_ENTREGA_DOCUMENTO MED ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
		 .append(" INNER JOIN MANIFESTO MAN ON MED.ID_MANIFESTO_ENTREGA = MAN.ID_MANIFESTO ")
		 .append(" INNER JOIN CLIENTE CL on DS.ID_CLIENTE_REMETENTE = CL.ID_CLIENTE ");

		 delimitador = " WHERE ";
		 
		if(criteria.getLong("idCliente") != null){
			sql.append(delimitador + " DS.ID_CLIENTE_REMETENTE = ").append(criteria.get("idCliente"));
			delimitador = " AND ";
			
			sql.append(delimitador + " DS.ID_CLIENTE_REMETENTE IS NOT NULL ");
		}
		if(criteria.getBoolean("blClienteCCT")){
			sql.append(delimitador + " CL.BL_CLIENTE_CCT = 'S' ");
			delimitador = " AND ";
		}
		if(criteria.getString("dtInicio")!= null && criteria.getString("dtFim") != null){
			sql.append(delimitador + " TRUNC(MAN.DH_EMISSAO_MANIFESTO) BETWEEN to_date('").append(criteria.getString("dtInicio")).append("', 'dd/MM/yyyy') ");
			sql.append("                                                   AND to_date('").append(criteria.getString("dtFim")).append("', 'dd/MM/yyyy') ");
			delimitador = " AND ";
		}
			
		 sql.append(" ORDER BY DS.ID_CLIENTE_REMETENTE, FD.SG_FILIAL ");
		 
		 return sql;
	 }

	@Override
	protected Class getPersistentClass() {
		return null;
	}
	
	public List<MonitoramentoCCT> findMonitoramentosbyIdDoctoServico(Long idDoctoServico, String nrChave){
		List<Object> param = new ArrayList<Object>();	
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT mcct ")
		.append(" FROM " +  MonitoramentoCCT.class.getName() + " mcct ")
		.append(" inner join mcct.doctoServico ds, ")
		.append(NotaFiscalConhecimento.class.getName() + " nfc" )
		.append(" WHERE ds.idDoctoServico = ? ")
		.append(" and nfc.nrChave = mcct.nrChave ")
		.append(" and nfc.nrChave = ? ");
		param.add(idDoctoServico);	
		param.add(nrChave);	
		List<MonitoramentoCCT> lista = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		return lista;
	}

	public Map<String, Object> findDtObAgendamentoByMonitoramentos(Long idDoctoServico) {
		List<Long> param = new ArrayList<Long>();	
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT new Map(ae.dtAgendamento as dtAgendamento, ae.obAgendamentoEntrega as obAgendamentoEntrega ) ")
		.append(" FROM " +  AgendamentoMonitCCT.class.getName() + " amcct ")
		.append(" inner join amcct.agendamentoEntrega ae")
		.append(" inner join amcct.monitoramentoCCT mcct ")
		.append(" inner join mcct.doctoServico ds ")
		.append(" WHERE ds.idDoctoServico = ? ")
		.append(" and ae.tpSituacaoAgendamento not in ('C','R') ")
		.append(" and ae.tpAgendamento <> 'TA' ")
		.append(" ORDER BY ae.idAgendamentoEntrega DESC " );
		param.add(idDoctoServico);		
		List<Map<String, Object>> lista = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		if(!lista.isEmpty()){
			return lista.get(0);
		}else{
			return new HashedMap();
		}
	}

	public Map<String, Object> findDtObAgendamentoByDocumentoServico(Long idDoctoServico) {
		List<Long> param = new ArrayList<Long>();	
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT new Map(ae.dtAgendamento as dtAgendamento, ae.obAgendamentoEntrega as obAgendamentoEntrega ) ")
		.append(" FROM " +  AgendamentoDoctoServico.class.getName() + " ads ")
		.append(" inner join ads.agendamentoEntrega ae")
		.append(" inner join ads.doctoServico ds ")
		.append(" WHERE ds.idDoctoServico = ? ")
		.append(" and ae.tpSituacaoAgendamento not in ('C','R') ")
		.append(" and ae.tpAgendamento <> 'TA' ")
		.append(" ORDER BY ae.idAgendamentoEntrega DESC " );
		param.add(idDoctoServico);		
		List<Map<String, Object>> lista = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		if(!lista.isEmpty()){
			return lista.get(0);
		}else{
			return new HashedMap();
		}
	}

}
