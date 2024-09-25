package com.mercurio.lms.sim.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeTimeOfDayUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ItemNfCto;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LocalizacaoMercadoriaDAO extends BaseCrudDao<LocalizacaoMercadoria, Long> {
   public static final int ID_DOCTO_SERVICO = 0;
   public static final int TP_DOCUMENTO_SERVICO = 1;
   public static final int NR_DOCTO_SERVICO = 2;
   public static final int SG_FILIAL_OR = 3;
   public static final int TP_CONHECIMENTO = 4;
   public static final int SG_FILIAL_DEST = 5;
   public static final int DH_EMISSAO = 6;
   public static final int LOCALIZACAO = 7;
   public static final int TP_IDENT_REM = 8;
   public static final int NR_IDENT_REM = 9;
   public static final int NM_PESSOA_REM = 10;
   public static final int TP_IDENT_DEST = 11;
   public static final int NR_IDENT_DEST = 12;
   public static final int NM_PESSOA_DEST = 13;
   public static final int TP_IDENT_CONSI = 14;
   public static final int NR_IDENT_CONSI = 15;
   public static final int NM_PESSOA_CONSI = 16;
   public static final int DT_AGEN = 17;
   public static final int TP_IDENT_DEV = 18;
   public static final int NR_IDENT_DEV = 19;
   public static final int NM_PESSOA_DEV = 20;
   public static final int DT_ENTREGA = 21;
   public static final int NF = 22;
   public static final int DT_PREV_ENTREGA = 23;
   public static final int TP_IDENT_REDES = 24;
   public static final int NR_IDENT_REDES = 25;
   public static final int NM_PESSOA_REDES = 26;
   public static final int NR_FORMULARIO = 27;
   public static final int DS_SERIE_AWB = 28;
   public static final int NR_AWB = 29;
   public static final int DV_AWB = 30;
   public static final int SG_CIA_AEREA_AWB = 31;
   public static final int DT_SAIDA_VIAGEM = 32;
   public static final int DT_CHEGADA_VIAGEM = 33;
   public static final int DT_SAIDA_ENT_MAN = 34;
   public static final int DT_AGEND_MONIT = 35;
   public static final int ID_AWB = 36;
   public static final int NR_CAE = 37;
   
   //CONSTANTES DO DETALHAMENTO DA ABA PRINCIPAL (Consultar Localizações Mercadorias)
   public static final int DET_QT_VOLUMES = 0;
   public static final int DET_PS_REAL = 1;
   public static final int DET_PS_AFORADO = 2;
   public static final int DET_PS_REFERENCIAL = 3;
   public static final int DET_DS_END_ENTREGA = 4;
   public static final int DET_SG_FIL_OR = 5;
   public static final int DET_NR_DOCTO_OR = 6;
   public static final int DET_SG_FIL_PEDCOL = 7;
   public static final int DET_NR_COL = 8;
   public static final int DET_NM_PESSOA_REM = 9;
   public static final int DET_NR_IDENT_REM = 10;
   public static final int DET_NM_PESSOA_DEST = 11;
   public static final int DET_NR_IDENT_DEST = 12;
   public static final int DET_NR_NF = 13;
   public static final int DET_QT_NF = 14;
   public static final int DET_TP_DENSIDADE = 15;
   public static final int DET_DS_NATUREZA = 16;
   public static final int DET_NR_AWB = 17;
   public static final int DET_DV_AWB = 18;
   public static final int DET_NM_EMP_CIA = 19;
   public static final int DET_DT_PREV_ENTREGA = 20;
   public static final int DET_NR_DIAS_ENTREGA = 21;
   public static final int DET_NR_DIAS_BLOQUEIO = 22;
   public static final int DET_NM_RECEBEDOR = 23;
   public static final int DET_DS_LOCAL_MERC = 24;
   public static final int DET_DT_AGEND = 25;
   public static final int DET_DS_TURNO = 26;
   public static final int DET_ED_COLETA = 27;
   public static final int DET_MUN_COLETA = 28;
   public static final int DET_MUN_UF_COL = 29;
   public static final int DET_SG_UF_COL = 30;
   public static final int DET_PAIS_COL = 31;
   public static final int DET_DH_BAIXA = 32;
   public static final int DET_TP_IDENT_REM = 33;
   public static final int DET_TP_IDENT_DEST = 34;
   public static final int DET_VL_MERC = 35;
   public static final int DET_DS_SIMBOLO_M0EDA = 36;
   public static final int DET_HR_INICIAL = 37;
   public static final int DET_HR_FINAL = 38;
   public static final int DET_SOLICITACAO_PRIORIZACAO = 39;
   public static final int DET_SOLICITACAO_RETIRADA = 40;
   public static final int DET_DS_ROTA = 41;
   public static final int DET_NR_DIAS_ENTREGA_REAL = 42;
   public static final int DET_NR_FORMULARIO = 43;
   public static final int DET_NR_FATOR_DENSIDADE = 44;
   public static final int DET_NR_DOCUMENTO_ELETRONICO = 45;
   public static final int DET_TP_DOCUMENTO_SERVICO = 46;
   public static final int DET_PS_AFERIDO = 47;
   public static final int DET_MUN_ENTREGA = 48;
   public static final int DET_MUN_UF_ENT = 49;
   public static final int DET_SG_UF_ENT = 50;
   public static final int DET_PAIS_ENT = 51;
   public static final int DET_ID_AWB = 52;
   public static final int DET_DS_SERIE = 53;
   public static final int DET_SG_EMP_CIA = 54;
   public static final int DET_TP_STATUS_AWB = 55;
   public static final int DET_DT_NOVO_DPE_DOCTO_SERVICO = 56;
   
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return LocalizacaoMercadoria.class;
	}
	
	/**
	 * Método que busca LocalizacaoMercadoria que possui o código informado
	 * @param cdLocalizacaoMercadoria
	 * @return
	 */
	public LocalizacaoMercadoria findLocalizacaoMercadoriaByCodigo(Short cdLocalizacaoMercadoria) {
		String query = new StringBuffer()
		.append("from " + LocalizacaoMercadoria.class.getName() + " lm ")
		.append("where lm.cdLocalizacaoMercadoria = ? ").toString();		
				
		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(query);
		q.setParameter(0, cdLocalizacaoMercadoria);
		return (LocalizacaoMercadoria) q.uniqueResult();
	}


	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List findByCodigosLocalizacaoMercadoria(List codigos, String tpSituacao) {
		StringBuffer sql = new StringBuffer()
			.append("from " + LocalizacaoMercadoria.class.getName() + " lm ")
			.append("where lm.cdLocalizacaoMercadoria in ( ");
			
		for (int i=0; i<codigos.size(); i++) {
			if (i != 0) {
				sql.append(",");
			}
			sql.append((String)codigos.get(i));	
		}
		sql.append(") ");
		
		List param = new ArrayList();
		if (tpSituacao != null) {
			sql.append("and lm.tpSituacao = ? ");
			param.add(tpSituacao);
		}

		sql.append("order by ")
		.append(OrderVarcharI18n.hqlOrder("lm.dsLocalizacaoMercadoria", LocaleContextHolder.getLocale())).toString();

		List result = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		return result;
	}

	
	public Integer getRowCountConsultaLocalizacaoMercadoria(TypedFlatMap criteria) {
		SqlTemplate sql = montaQuerySqlConsultaLocalizacaoMercadoria(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountBySql(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;
	}

	public ResultSetPage findPaginatedConsultaLocalizacaoMercadoria(TypedFlatMap criteria, FindDefinition findDef) {
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("TP_DOCUMENTO_SERVICO", Hibernate.STRING);
				sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_OR", Hibernate.STRING);
				sqlQuery.addScalar("TP_CONHECIMENTO", Hibernate.STRING);
				sqlQuery.addScalar("SG_FILIAL_DEST", Hibernate.STRING);
				sqlQuery.addScalar("DH_EMISSAO", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("LOCALIZACAO", Hibernate.STRING);
				sqlQuery.addScalar("TP_IDENT_REM", Hibernate.STRING);
				sqlQuery.addScalar("NR_IDENT_REM", Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA_REM", Hibernate.STRING);
				sqlQuery.addScalar("TP_IDENT_DEST", Hibernate.STRING);
				sqlQuery.addScalar("NR_IDENT_DEST", Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA_DEST", Hibernate.STRING);
				sqlQuery.addScalar("TP_IDENT_CONSI", Hibernate.STRING);
				sqlQuery.addScalar("NR_IDENT_CONSI", Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA_CONSI", Hibernate.STRING);
				sqlQuery.addScalar("DT_AGEN",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("TP_IDENT_DEV",Hibernate.STRING);
				sqlQuery.addScalar("NR_IDENT_DEV",Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA_DEV",Hibernate.STRING);
				sqlQuery.addScalar("DT_ENTREGA",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("NF",Hibernate.INTEGER);
				sqlQuery.addScalar("DT_PREV_ENTREGA",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("TP_IDENT_REDES", Hibernate.STRING);
				sqlQuery.addScalar("NR_IDENT_REDES", Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA_REDES", Hibernate.STRING);
				sqlQuery.addScalar("NR_FORMULARIO", Hibernate.LONG);
				
				sqlQuery.addScalar("DS_SERIE_AWB", Hibernate.STRING);
				sqlQuery.addScalar("NR_AWB", Hibernate.LONG);
				sqlQuery.addScalar("DV_AWB", Hibernate.INTEGER);
				sqlQuery.addScalar("SG_CIA_AEREA_AWB", Hibernate.STRING);
			
				sqlQuery.addScalar("DT_SAIDA_VIAGEM",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DT_CHEGADA_VIAGEM",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DT_SAIDA_ENT_MAN",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DT_AGEND_MONIT",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				
				sqlQuery.addScalar("ID_AWB", Hibernate.LONG);
				sqlQuery.addScalar("NR_CAE", Hibernate.STRING);
				
			}
		};
		SqlTemplate sql = montaQuerySqlConsultaLocalizacaoMercadoria(criteria);
		ResultSetPage rs = getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria(),configSql);
		return rs;
	}
	
	 public SqlTemplate montaQuerySqlConsultaLocalizacaoMercadoria(TypedFlatMap criteria){
		  	
			SqlTemplate sql = new SqlTemplate();
				
			sql.addProjection("DS.ID_DOCTO_SERVICO","ID_DOCTO_SERVICO");
			sql.addProjection("DS.TP_DOCUMENTO_SERVICO","TP_DOCUMENTO_SERVICO");
			sql.addProjection("DS.NR_DOCTO_SERVICO","NR_DOCTO_SERVICO");
			sql.addProjection("FILOR.SG_FILIAL","SG_FILIAL_OR");
			sql.addProjection("CO.TP_CONHECIMENTO","TP_CONHECIMENTO");
			sql.addProjection("FILDEST.SG_FILIAL","SG_FILIAL_DEST");
			sql.addProjection("DS.DH_EMISSAO","DH_EMISSAO");
			sql.addProjection("DS.DT_PREV_ENTREGA","DT_PREV_ENTREGA");
			sql.addProjection(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I")+"||' '||DS.OB_COMPLEMENTO_LOCALIZACAO","LOCALIZACAO");
			sql.addProjection("PESREM.TP_IDENTIFICACAO","TP_IDENT_REM");
			sql.addProjection("PESREM.NR_IDENTIFICACAO","NR_IDENT_REM");
			sql.addProjection("PESREM.NM_PESSOA","NM_PESSOA_REM");
			sql.addProjection("PESDEST.TP_IDENTIFICACAO","TP_IDENT_DEST");
			sql.addProjection("PESDEST.NR_IDENTIFICACAO","NR_IDENT_DEST");
			sql.addProjection("PESDEST.NM_PESSOA","NM_PESSOA_DEST");
			sql.addProjection("PESCONSI.TP_IDENTIFICACAO","TP_IDENT_CONSI");
			sql.addProjection("PESCONSI.NR_IDENTIFICACAO","NR_IDENT_CONSI");
			sql.addProjection("PESCONSI.NM_PESSOA","NM_PESSOA_CONSI");
			
		   	sql.addProjection("(SELECT Max(AE.DT_AGENDAMENTO) FROM AGENDAMENTO_DOCTO_SERVICO ADS, AGENDAMENTO_ENTREGA AE WHERE ADS.ID_AGENDAMENTO_ENTREGA=AE.ID_AGENDAMENTO_ENTREGA AND ADS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND AE.TP_SITUACAO_AGENDAMENTO NOT IN('C','R')) AS DT_AGEN");
			sql.addProjection("(SELECT Max(PESDEV.TP_IDENTIFICACAO) FROM DEVEDOR_DOC_SERV_FAT DDSF, CLIENTE CLI, PESSOA PESDEV WHERE DDSF.ID_DOCTO_SERVICO=DS.ID_DOCTO_SERVICO AND DDSF.ID_CLIENTE = CLI.ID_CLIENTE AND CLI.ID_CLIENTE = PESDEV.ID_PESSOA(+)) AS TP_IDENT_DEV");
			sql.addProjection("(SELECT Max(PESDEV.NR_IDENTIFICACAO) FROM DEVEDOR_DOC_SERV_FAT DDSF, CLIENTE CLI, PESSOA PESDEV WHERE DDSF.ID_DOCTO_SERVICO=DS.ID_DOCTO_SERVICO AND DDSF.ID_CLIENTE = CLI.ID_CLIENTE AND CLI.ID_CLIENTE = PESDEV.ID_PESSOA(+)) AS NR_IDENT_DEV");
			sql.addProjection("(SELECT Max(PESDEV.NM_PESSOA) FROM DEVEDOR_DOC_SERV_FAT DDSF, CLIENTE CLI, PESSOA PESDEV WHERE DDSF.ID_DOCTO_SERVICO=DS.ID_DOCTO_SERVICO AND DDSF.ID_CLIENTE = CLI.ID_CLIENTE AND CLI.ID_CLIENTE = PESDEV.ID_PESSOA(+)) AS NM_PESSOA_DEV");
			
			sql.addProjection("(SELECT Max(EDS.DH_EVENTO) FROM EVENTO_DOCUMENTO_SERVICO EDS, EVENTO EV WHERE EDS.ID_EVENTO = EV.ID_EVENTO AND EDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND EDS.BL_EVENTO_CANCELADO='N' AND EV.CD_EVENTO=21) AS DT_ENTREGA");
			
			sql.addProjection("(SELECT Min(NFC.NR_NOTA_FISCAL) FROM NOTA_FISCAL_CONHECIMENTO NFC  WHERE NFC.ID_CONHECIMENTO = CO.ID_CONHECIMENTO) AS NF");
			
			sql.addProjection("PESREDES.TP_IDENTIFICACAO","TP_IDENT_REDES");
			sql.addProjection("PESREDES.NR_IDENTIFICACAO","NR_IDENT_REDES");
			sql.addProjection("PESREDES.NM_PESSOA","NM_PESSOA_REDES");
			sql.addProjection("CO.NR_FORMULARIO","NR_FORMULARIO");
			
			//AWB
			sql.addProjection("(select AWB.DS_SERIE from AWB where AWB.ID_AWB = (SELECT MAX(AW.ID_AWB) FROM CTO_AWB CA JOIN AWB AW ON CA.ID_AWB = AW.ID_AWB AND AW.TP_STATUS_AWB <> 'C' WHERE DS.ID_DOCTO_SERVICO = CA.ID_CONHECIMENTO) )", "DS_SERIE_AWB");
			sql.addProjection("(select AWB.NR_AWB from AWB where AWB.ID_AWB = (SELECT MAX(AW.ID_AWB) FROM CTO_AWB CA JOIN AWB AW ON CA.ID_AWB = AW.ID_AWB AND AW.TP_STATUS_AWB <> 'C' WHERE DS.ID_DOCTO_SERVICO = CA.ID_CONHECIMENTO) )", "NR_AWB");
			sql.addProjection("(select AWB.DV_AWB from AWB where AWB.ID_AWB = (SELECT MAX(AW.ID_AWB) FROM CTO_AWB CA JOIN AWB AW ON CA.ID_AWB = AW.ID_AWB AND AW.TP_STATUS_AWB <> 'C' WHERE DS.ID_DOCTO_SERVICO = CA.ID_CONHECIMENTO) )", "DV_AWB");
			sql.addProjection("(select EMP_AWB.SG_EMPRESA from AWB, CIA_FILIAL_MERCURIO CFM, EMPRESA EMP_AWB where AWB.ID_CIA_FILIAL_MERCURIO = CFM.ID_CIA_FILIAL_MERCURIO AND CFM.ID_EMPRESA = EMP_AWB.ID_EMPRESA and AWB.ID_AWB = (SELECT MAX(AW.ID_AWB) FROM CTO_AWB CA JOIN AWB AW ON CA.ID_AWB = AW.ID_AWB AND AW.TP_STATUS_AWB <> 'C' WHERE DS.ID_DOCTO_SERVICO = CA.ID_CONHECIMENTO) )", "SG_CIA_AEREA_AWB");
			
			//Data de saída viagem
			StringBuilder sqlDataSaidaViagem = new StringBuilder("(SELECT CT.DH_SAIDA FROM ");
			sqlDataSaidaViagem.append("MANIFESTO_NACIONAL_CTO MN, ");
			sqlDataSaidaViagem.append("MANIFESTO_VIAGEM_NACIONAL MVN, ");
			sqlDataSaidaViagem.append("MANIFESTO MAN, "); 
			sqlDataSaidaViagem.append("CONTROLE_CARGA CC, ");
			sqlDataSaidaViagem.append("CONTROLE_TRECHO CT ");
			sqlDataSaidaViagem.append("WHERE CO.ID_CONHECIMENTO = MN.ID_CONHECIMENTO ");
			sqlDataSaidaViagem.append("AND MN.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL ");
			sqlDataSaidaViagem.append("AND MVN.ID_MANIFESTO_VIAGEM_NACIONAL = MAN.ID_MANIFESTO ");
			sqlDataSaidaViagem.append("AND MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA(+) ");
			sqlDataSaidaViagem.append("AND CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ");
			sqlDataSaidaViagem.append("AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM ");
			sqlDataSaidaViagem.append("AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO ");
			sqlDataSaidaViagem.append("AND DS.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM  AND ROWNUM =1 ) ");
			sql.addProjection(sqlDataSaidaViagem.toString(), "DT_SAIDA_VIAGEM");
			
			//Data de chegada viagem
			StringBuilder sqlDataChegadaViagem = new StringBuilder("(SELECT CT.DH_CHEGADA ");
			sqlDataChegadaViagem.append("FROM ");
			sqlDataChegadaViagem.append("MANIFESTO_NACIONAL_CTO MN, ");
			sqlDataChegadaViagem.append("MANIFESTO_VIAGEM_NACIONAL MVN, ");
			sqlDataChegadaViagem.append("MANIFESTO MAN, ");
			sqlDataChegadaViagem.append("CONTROLE_CARGA CC, ");
			sqlDataChegadaViagem.append("CONTROLE_TRECHO CT ");
			sqlDataChegadaViagem.append("WHERE CO.ID_CONHECIMENTO = MN.ID_CONHECIMENTO ");
			sqlDataChegadaViagem.append("AND MN.ID_MANIFESTO_VIAGEM_NACIONAL  = MVN.ID_MANIFESTO_VIAGEM_NACIONAL ");
			sqlDataChegadaViagem.append("AND MVN.ID_MANIFESTO_VIAGEM_NACIONAL = MAN.ID_MANIFESTO ");
			sqlDataChegadaViagem.append("AND MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA(+) ");
			sqlDataChegadaViagem.append("AND CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ");
			sqlDataChegadaViagem.append("AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM ");
			sqlDataChegadaViagem.append("AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO ");
			sqlDataChegadaViagem.append("AND DS.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO AND ROWNUM =1 )  "); 
			sql.addProjection(sqlDataChegadaViagem.toString(), "DT_CHEGADA_VIAGEM");
			
			//Data saída entrega manifesto
			StringBuilder sqlDataEntregaManifesto = new StringBuilder("(SELECT MAX(CC.DH_SAIDA_COLETA_ENTREGA) ");
 			sqlDataEntregaManifesto.append("FROM ");
 			sqlDataEntregaManifesto.append("MANIFESTO_ENTREGA_DOCUMENTO MED, ");
 			sqlDataEntregaManifesto.append("MANIFESTO_ENTREGA ME, ");
			sqlDataEntregaManifesto.append("MANIFESTO M, ");
			sqlDataEntregaManifesto.append("CONTROLE_CARGA CC ");
 			sqlDataEntregaManifesto.append("WHERE ");
 			sqlDataEntregaManifesto.append("MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
 			sqlDataEntregaManifesto.append("AND MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA ");
			sqlDataEntregaManifesto.append("AND ME.ID_MANIFESTO_ENTREGA = M.ID_MANIFESTO ");
			sqlDataEntregaManifesto.append("AND CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA) ");
 			sql.addProjection(sqlDataEntregaManifesto.toString(), "DT_SAIDA_ENT_MAN");
			
			//Data agendamento monitoramento
			StringBuilder sqlDataAgendamentoMonitoramento = new StringBuilder("(select MAX(AGENDAMENTO_ENTREGA.DT_AGENDAMENTO) ");
			sqlDataAgendamentoMonitoramento.append("from NOTA_FISCAL_CONHECIMENTO, MONITORAMENTO_CCT, AGENDAMENTO_MONIT_CCT, AGENDAMENTO_ENTREGA ");
			sqlDataAgendamentoMonitoramento.append("where ");
			sqlDataAgendamentoMonitoramento.append("NOTA_FISCAL_CONHECIMENTO.ID_CONHECIMENTO = CO.ID_CONHECIMENTO ");
			sqlDataAgendamentoMonitoramento.append("and AGENDAMENTO_ENTREGA.TP_SITUACAO_AGENDAMENTO NOT IN('C','R') ");
			sqlDataAgendamentoMonitoramento.append("and NOTA_FISCAL_CONHECIMENTO.NR_CHAVE = MONITORAMENTO_CCT.NR_CHAVE ");
			sqlDataAgendamentoMonitoramento.append("and MONITORAMENTO_CCT.ID_MONITORAMENTO_CCT = AGENDAMENTO_MONIT_CCT.ID_MONITORAMENTO_CCT ");
			sqlDataAgendamentoMonitoramento.append("and AGENDAMENTO_MONIT_CCT.ID_AGENDAMENTO_ENTREGA = AGENDAMENTO_ENTREGA.ID_AGENDAMENTO_ENTREGA)");
			sql.addProjection(sqlDataAgendamentoMonitoramento.toString(), "DT_AGEND_MONIT");
			
			sql.addProjection("(select AWB.ID_AWB from CTO_AWB, AWB where  DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND CTO_AWB.ID_AWB = AWB.ID_AWB(+) and rownum = 1 AND AWB.TP_STATUS_AWB <> 'C')", "ID_AWB");
			
			sql.addProjection("CO.NR_CAE","NR_CAE");
			
			sql.addFrom("DOCTO_SERVICO","DS");
			sql.addFrom("FILIAL", "FILOR");
			sql.addFrom("FILIAL","FILDEST");
			sql.addFrom("CONHECIMENTO","CO");
			sql.addFrom("CLIENTE","REME");
			sql.addFrom("PESSOA","PESREM");
			sql.addFrom("CLIENTE","DEST");
			sql.addFrom("PESSOA","PESDEST");
			sql.addFrom("CLIENTE","CONSI");
			sql.addFrom("PESSOA","PESCONSI");
			sql.addFrom("CLIENTE","REDES");
			sql.addFrom("PESSOA","PESREDES");
			sql.addFrom("LOCALIZACAO_MERCADORIA","LM");
			
			if(criteria.getLong("awb.idAwb")!= null){
				sql.addJoin("DS.ID_DOCTO_SERVICO" , "CO.ID_CONHECIMENTO");
			}else{
				sql.addJoin("DS.ID_DOCTO_SERVICO" , "CO.ID_CONHECIMENTO(+)");
			}
			
			sql.addJoin("DS.ID_FILIAL_ORIGEM" ,"FILOR.ID_FILIAL");
			sql.addJoin("DS.ID_CLIENTE_CONSIGNATARIO" , "CONSI.ID_CLIENTE(+)");
			sql.addJoin("CONSI.ID_CLIENTE" , "PESCONSI.ID_PESSOA(+)");
			sql.addJoin("DS.ID_CLIENTE_REDESPACHO" , "REDES.ID_CLIENTE(+)");
			sql.addJoin("REDES.ID_CLIENTE" , "PESREDES.ID_PESSOA(+)");
			sql.addJoin("DS.ID_LOCALIZACAO_MERCADORIA" , "LM.ID_LOCALIZACAO_MERCADORIA(+)");
			
			if(criteria.getLong("empresa.idEmpresa")!= null){
				sql.addFrom("EMPRESA","EMP");
				sql.addJoin("FILOR.ID_EMPRESA","EMP.ID_EMPRESA");
				sql.addCriteria("EMP.ID_EMPRESA","=", criteria.getLong("empresa.idEmpresa"));
			}
			
			if(criteria.getLong("pedidoColeta.idPedidoColeta")!= null){
				sql.addFrom("PEDIDO_COLETA","PC");
				sql.addJoin("DS.ID_PEDIDO_COLETA","PC.ID_PEDIDO_COLETA");
				sql.addCriteria("PC.ID_PEDIDO_COLETA","=",criteria.getLong("pedidoColeta.idPedidoColeta"));
			}
			
			if(criteria.getInteger("nrCotacao")!= null){
				sql.addFrom("COTACAO","COT");
				sql.addFrom("FILIAL","FILCOT");
				
				sql.addJoin("DS.ID_DOCTO_SERVICO" , "COT.ID_DOCTO_SERVICO");
				sql.addJoin("FILCOT.ID_FILIAL" , "COT.ID_FILIAL");
				sql.addCriteria("COT.NR_COTACAO", "=", criteria.getInteger("nrCotacao"));
				sql.addCriteria("FILCOT.ID_FILIAL", "=", criteria.getLong("filial.idFilial"));
			}
			
			if(criteria.getLong("cotacao.idCotacao")!= null){
				sql.addFrom("COTACAO","COT");
				sql.addFrom("FILIAL","FILCOT");
				
				sql.addJoin("DS.ID_DOCTO_SERVICO" , "COT.ID_DOCTO_SERVICO");
				sql.addJoin("FILCOT.ID_FILIAL" , "COT.ID_FILIAL");
				sql.addCriteria("COT.ID_COTACAO", "=", criteria.getLong("cotacao.idCotacao"));
			}
			
			if(StringUtils.isNotEmpty(criteria.getString("modal")) || StringUtils.isNotEmpty(criteria.getString("abrangencia")) || criteria.getLong("tipoServico.idTipoServico")!= null){
				sql.addFrom("SERVICO","SER");
				sql.addJoin("DS.ID_SERVICO","SER.ID_SERVICO(+)");
				if(criteria.getLong("tipoServico.idTipoServico")!= null){
					sql.addFrom("TIPO_SERVICO","TS");
					sql.addJoin("SER.ID_TIPO_SERVICO","TS.ID_TIPO_SERVICO(+)");
					sql.addCriteria("TS.ID_TIPO_SERVICO","=",criteria.getLong("tipoServico.idTipoServico"));
				}
				sql.addCriteria("SER.TP_MODAL","=", criteria.getString("modal"));
				sql.addCriteria("SER.TP_ABRANGENCIA","=", criteria.getString("abrangencia"));
			}
			sql.addCriteria("FILOR.ID_FILIAL","=", criteria.getLong("filialOrigem.idFilial"));
			
			if(criteria.getLong("filialDestino.idFilial")!= null){
				sql.addJoin("DS.ID_FILIAL_DESTINO" , "FILDEST.ID_FILIAL");
				sql.addCriteria("FILDEST.ID_FILIAL","=", criteria.getLong("filialDestino.idFilial"));
			}else{
				sql.addJoin("DS.ID_FILIAL_DESTINO" , "FILDEST.ID_FILIAL(+)");
			}	  
			 
			sql.addCriteria("CO.TP_CONHECIMENTO", "=", criteria.getString("finalidade"));
			sql.addCriteria("DS.ID_DOCTO_SERVICO","=", criteria.getLong("idDoctoServico"));
			
			sql.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN ('CTR', 'CRT', 'NFT', 'RRE','CTE','NTE')");
			 
			//Foi adicionado esse filtro para funcionar com a tela 'Consultar localização de mercadoria'
			sql.addCriteria("DS.ID_DOCTO_SERVICO","=", criteria.getLong("doctoServico.idDoctoServico"));
			sql.addCriteria("CO.NR_FORMULARIO", "=", criteria.getLong("nrFormulario"));
			
			if(criteria.getInteger("nfCliente")!= null){
				sql.addFrom("NOTA_FISCAL_CONHECIMENTO","NFC");
				sql.addJoin("NFC.ID_CONHECIMENTO","CO.ID_CONHECIMENTO");
				sql.addCriteria("NFC.NR_NOTA_FISCAL","=",criteria.getInteger("nfCliente"));
			}
			
			if (criteria.getString("cae") != null) {
				sql.addCriteria("CO.NR_CAE", "=", Long.valueOf(criteria.getString("cae")).toString());
				sql.addCriteria("CO.ID_FILIAL_ORIGEM", "=" , criteria.getLong("caeIdFilial"));
			}
			
			if(criteria.getLong("manifestoColeta.idManifestoColeta")== null && criteria.getLong("controleCarga.idControleCarga")== null && criteria.getLong("pedidoColeta.idPedidoColeta")== null && criteria.getLong("manifesto.idManifesto")== null && criteria.getLong("mir.idMir")== null && criteria.getLong("manifestoEntrega.idManifestoEntrega")== null && criteria.getLong("idDoctoServico")== null && criteria.getLong("doctoServico.idDoctoServico")== null && criteria.getLong("awb.idAwb")== null && criteria.getLong("idAgendamentoEntrega") == null){
				
				if(criteria.get("volumeColeta") == null){
				if(criteria.getYearMonthDay("periodoInicial")== null && criteria.getYearMonthDay("periodoFinal")== null){
					sql.addCustomCriteria("DS.DH_EMISSAO IS NULL");
				}
			}	
			}
			
			if (criteria.getYearMonthDay("periodoInicial") != null){
				sql.addCriteria("TRUNC(CAST(DS.DH_EMISSAO as DATE))", ">=", criteria.getYearMonthDay("periodoInicial"));
			}
			if (criteria.getYearMonthDay("periodoFinal") != null){
				sql.addCriteria("TRUNC(CAST(DS.DH_EMISSAO as DATE))", "<=", criteria.getYearMonthDay("periodoFinal"));
			}
			
			if(criteria.getLong("remetente.idCliente")!= null){
				sql.addJoin("DS.ID_CLIENTE_REMETENTE" , "REME.ID_CLIENTE");
				sql.addJoin("REME.ID_CLIENTE" , "PESREM.ID_PESSOA");
				sql.addCriteria("REME.ID_CLIENTE","=",criteria.getLong("remetente.idCliente"));
			}else{
				sql.addJoin("DS.ID_CLIENTE_REMETENTE" , "REME.ID_CLIENTE(+)");
				sql.addJoin("REME.ID_CLIENTE" , "PESREM.ID_PESSOA(+)");
			}
			
			if(StringUtils.isNotEmpty(criteria.getString("nrDocumentoCliente")) && criteria.getLong("informacaoDoctoCliente.idInformacaoDoctoCliente")!= null){
				sql.addFrom("DADOS_COMPLEMENTO","DC");
				sql.addFrom("INFORMACAO_DOCTO_CLIENTE","IDC");
				sql.addJoin("DC.ID_CONHECIMENTO","CO.ID_CONHECIMENTO");
				sql.addJoin("DC.ID_INFORMACAO_DOCTO_CLIENTE","IDC.ID_INFORMACAO_DOCTO_CLIENTE");
				sql.addCriteria("IDC.ID_INFORMACAO_DOCTO_CLIENTE","=",criteria.getLong("informacaoDoctoCliente.idInformacaoDoctoCliente"));
				sql.addCriteria("UPPER(DC.DS_VALOR_CAMPO)","LIKE",criteria.getString("nrDocumentoCliente").toUpperCase());
				
			}
			
			if(criteria.getLong("destinatario.idCliente")!= null){
				sql.addJoin("DS.ID_CLIENTE_DESTINATARIO" , "DEST.ID_CLIENTE");
				sql.addJoin("DEST.ID_CLIENTE" , "PESDEST.ID_PESSOA");
				sql.addCriteria("DEST.ID_CLIENTE","=", criteria.getLong("destinatario.idCliente"));
			 }else{
				 sql.addJoin("DS.ID_CLIENTE_DESTINATARIO" , "DEST.ID_CLIENTE(+)");
				 sql.addJoin("DEST.ID_CLIENTE" , "PESDEST.ID_PESSOA(+)");
			 }
			
			sql.addCriteria("CONSI.ID_CLIENTE","=", criteria.getLong("consignatario.idCliente"));
			sql.addCriteria("REDES.ID_CLIENTE","=", criteria.getLong("redespacho.idCliente"));
			
			if(criteria.getLong("responsavelFrete.idCliente")!= null){
				sql.addFrom("DEVEDOR_DOC_SERV_FAT","DDSF");
				sql.addFrom("CLIENTE","RESP");
				sql.addJoin("DDSF.ID_DOCTO_SERVICO","DS.ID_DOCTO_SERVICO");
				sql.addJoin("DDSF.ID_CLIENTE","RESP.ID_CLIENTE");
				sql.addCriteria("RESP.ID_CLIENTE","=",criteria.getLong("responsavelFrete.idCliente"));
			}
			if(criteria.getLong("controleCarga.idControleCarga")!= null){
				
				sql.addFrom("CONTROLE_CARGA","CC");
				sql.addFrom("PRE_MANIFESTO_DOCUMENTO","PMD");
				sql.addFrom("MANIFESTO","MAN");
				sql.addJoin("PMD.ID_MANIFESTO"," MAN.ID_MANIFESTO");
				sql.addJoin("MAN.ID_CONTROLE_CARGA "," CC.ID_CONTROLE_CARGA");
				sql.addJoin("DS.ID_DOCTO_SERVICO ","PMD.ID_DOCTO_SERVICO");
				sql.addCriteria("CC.ID_CONTROLE_CARGA", "=", criteria.getLong("controleCarga.idControleCarga"));
				sql.addCriteria("CC.TP_STATUS_CONTROLE_CARGA","<>", "CA");
			}
			if(criteria.getLong("manifestoColeta.idManifestoColeta")!= null){
				
				if(criteria.getLong("pedidoColeta.idPedidoColeta")== null){
					sql.addFrom("PEDIDO_COLETA","PC");
					sql.addJoin("DS.ID_PEDIDO_COLETA","PC.ID_PEDIDO_COLETA");
				}
				sql.addFrom("MANIFESTO_COLETA","MC");
				sql.addJoin("PC.ID_MANIFESTO_COLETA","MC.ID_MANIFESTO_COLETA");
				sql.addCriteria("MC.ID_MANIFESTO_COLETA","=",criteria.getLong("manifestoColeta.idManifestoColeta"));
			}
			
			if(criteria.getLong("manifesto.idManifesto")!= null){
				if(criteria.getLong("controleCarga.idControleCarga")== null){
					sql.addFrom("PRE_MANIFESTO_DOCUMENTO","PMD");
					sql.addFrom("MANIFESTO","MAN");
					sql.addJoin("PMD.ID_MANIFESTO","MAN.ID_MANIFESTO");
					sql.addJoin("DS.ID_DOCTO_SERVICO","PMD.ID_DOCTO_SERVICO");
				}
				sql.addCriteria("MAN.ID_MANIFESTO","=",criteria.getLong("manifesto.idManifesto"));
			}
			
			if(criteria.getLong("manifestoEntrega.idManifestoEntrega")!= null){
				sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO","MED");
				sql.addFrom("MANIFESTO_ENTREGA","ME");
				sql.addJoin("DS.ID_DOCTO_SERVICO","MED.ID_DOCTO_SERVICO");
				sql.addJoin("MED.ID_MANIFESTO_ENTREGA","ME.ID_MANIFESTO_ENTREGA");
				sql.addCriteria("ME.ID_MANIFESTO_ENTREGA","=",criteria.getLong("manifestoEntrega.idManifestoEntrega"));
			}
		
			if(criteria.getLong("mir.idMir")!= null){
				
				sql.addFrom("DOCUMENTO_MIR");
				sql.addFrom("MIR","MIR");
				
				if (criteria.getString("tpDocumentoMir").equals("R"))
					sql.addFrom("RECIBO_REEMBOLSO","REEMB"); 
				else sql.addFrom("REGISTRO_DOCUMENTO_ENTREGA","RDE");
				
				sql.addJoin("MIR.ID_MIR","DOCUMENTO_MIR.ID_MIR");
				sql.addCriteria("DOCUMENTO_MIR.ID_MIR","=",criteria.getLong("mir.idMir"));
				
				if (criteria.getString("tpDocumentoMir").equals("R")){
					sql.addCustomCriteria("REEMB.id_recibo_reembolso = DOCUMENTO_MIR.ID_RECIBO_REEMBOLSO ");
					sql.addCustomCriteria("REEMB.ID_DOCTO_SERV_REEMBOLSADO = ds.id_docto_servico ");
				} else {
					sql.addCustomCriteria("DOCUMENTO_MIR.id_REGISTRO_DOCUMENTO_ENTREGA = RDE.id_REGISTRO_DOCUMENTO_ENTREGA ");
					sql.addCustomCriteria("RDE.id_docto_servico = ds.id_docto_servico ");
				}
				
			}
			if(criteria.getLong("awb.idAwb")!= null){
				sql.addFrom("CTO_AWB","CTOA");
				sql.addFrom("AWB","AWB");
				sql.addJoin("CTOA.ID_CONHECIMENTO","CO.ID_CONHECIMENTO");
				sql.addJoin("CTOA.ID_AWB","AWB.ID_AWB");
				sql.addCriteria("AWB.ID_AWB","=", criteria.getLong("awb.idAwb"));
			}
			if(criteria.getLong("idAgendamentoEntrega") != null){
				sql.addFrom("AGENDAMENTO_DOCTO_SERVICO","ADS");
				sql.addFrom("AGENDAMENTO_ENTREGA","AE");
				sql.addJoin("ADS.ID_DOCTO_SERVICO","DS.ID_DOCTO_SERVICO");
				sql.addJoin("ADS.ID_AGENDAMENTO_ENTREGA","AE.ID_AGENDAMENTO_ENTREGA");
				sql.addCriteria("ADS.ID_AGENDAMENTO_ENTREGA","=",criteria.getLong("idAgendamentoEntrega"));
			}
			
			// LMS 2782
			if(criteria.get("volumeColeta") != null){				
				sql.addFrom("VOLUME_NOTA_FISCAL", "VNF");
				sql.addJoin("NFC.ID_CONHECIMENTO","CO.ID_CONHECIMENTO");
				sql.addJoin("NFC.ID_NOTA_FISCAL_CONHECIMENTO","VNF.ID_NOTA_FISCAL_CONHECIMENTO");
				sql.addCriteria("VNF.NR_VOLUME_COLETA", "=", criteria.getString("volumeColeta"));
			}
			
			sql.addOrderBy("DS.TP_DOCUMENTO_SERVICO");
			sql.addOrderBy("FILOR.SG_FILIAL");
			sql.addOrderBy("DS.NR_DOCTO_SERVICO");
			
			return sql;
			
		}
	
	public SqlTemplate montaQueryConsultaLocalizacaoMercadoria(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(" +
				"ds.tpDocumentoServico as dsTpDocumentoServico, " +
				"ds.nrDoctoServico as dsNrDoctoServico, " +
				"ds.idDoctoServico as idDoctoServico, " +
				"ds.dhEmissao as dsDhEmissao, " +
				
				"filialOrigem.sgFilial as dsSgFilialOrigem, " +
				"filialDestino.sgFilial as dsSgFilialDestino, " +
								
				"pesRem.nrIdentificacao as remNrIdentificacao, " +
				"pesRem.nmPessoa as remNmPessoa, " +
				"pesRem.tpIdentificacao as remTpIdentificacao, " +
				
				"pesDest.nrIdentificacao as destNrIdentificacao, " +
				"pesDest.nmPessoa as destNmPessoa, " +
				"pesDest.tpIdentificacao as destTpIdentificacao, " +
				
				"locMercadoria.dsLocalizacaoMercadoria as dsLocMerc, " +
				
				   			
				"pesCons.nrIdentificacao as consNrIdentificacao, " +
				"pesCons.nmPessoa as consNmPessoa, " +
				"pesCons.tpIdentificacao as consTpIdentificacao) ");
				
		
		hql.addFrom("DoctoServico ds " +
				"join ds.filialByIdFilialOrigem filialOrigem " +
				"join filialOrigem.empresa empresa " +
				"join ds.filialByIdFilialDestino filialDestino " +
				
				"left outer join ds.clienteByIdClienteRemetente clienteRemetente " +
				"left outer join clienteRemetente.pessoa pesRem " +
				
				"left outer join ds.clienteByIdClienteDestinatario clienteDestinatario " +
				"left outer join clienteDestinatario.pessoa pesDest " +
				
				"left outer join ds.clienteByIdClienteConsignatario clienteConsignatario " +
				"left outer join clienteConsignatario.pessoa pesCons " +
				
				"left outer join ds.localizacaoMercadoria locMercadoria " +
								
				"left outer join ds.servico servico " +
				"left outer join servico.tipoServico tipoServico " +
				
				"left outer join ds.pedidoColeta pedCol " +
				"left outer join pedCol.manifestoColeta manCol ");
		
		hql.addCriteria("empresa.idEmpresa","=",criteria.getLong("empresa.idEmpresa"));
		hql.addCriteria("servico.tpModal","=",criteria.getString("modal"));
		hql.addCriteria("servico.tpAbrangencia","=",criteria.getString("abrangencia"));
	   	hql.addCriteria("tipoServico.idTipoServico","=",criteria.getLong("tipoServico.idTipoServico"));
		hql.addCriteria("filialOrigem.idFilial","=",criteria.getLong("filialOrigem.idFilial"));
		hql.addCriteria("filialDestino.idFilial","=",criteria.getLong("filialDestino.idFilial"));
		hql.addCriteria("ds.idDoctoServico","=",criteria.getLong("idDoctoServico"));
		hql.addCriteria("pedCol.idPedidoColeta","=",criteria.getLong("pedidoColeta.idPedidoColeta"));
		hql.addCriteria("ds.dhEmissao.value",">=",criteria.getDateTime("periodoInicial"));
		hql.addCriteria("ds.dhEmissao.value","<=",criteria.getDateTime("periodoFinal"));
		hql.addCriteria("clienteRemetente.idCliente","=",criteria.getLong("remetente.idCliente"));
		hql.addCriteria("clienteDestinatario.idCliente","=",criteria.getLong("destinatario.idCliente"));
		hql.addCriteria("clienteConsignatario.idCliente","=",criteria.getLong("consignatario"));
	
		hql.addCriteria("manCol.idManifestoColeta","=", criteria.getLong("manifestoColeta.idManifestoColeta"));
		
		
		//NOTA FISCAL DO CLIENTE
		if(criteria.getInteger("nfCliente")!= null)
			hql.addCustomCriteria("exists(" +
					"select nfc.idNotaFiscalConhecimento " +
					"from NotaFiscalConhecimento as nfc " +
					"join nfc.conhecimento conh " +
					"where nfc.nrNotaFiscal = ? and conh.idDoctoServico = ds.idDoctoServico)",criteria.getInteger("nfCliente"));
		
		//DOCUMENTO DO CLIENTE
		if(criteria.getLong("informacaoDoctoCliente.idInformacaoDoctoCliente")!=null){
			if(StringUtils.isNotBlank(criteria.getString("nrDocumentoCliente"))){
				hql.addCustomCriteria("exists(select dc.idDadosComplemento " +
						"from DadosComplemento dc " +
						"left outer join dc.informacaoDoctoCliente idc " +
						"left outer join dc.conhecimento conh " +
						"where idc.idInformacaoDoctoCliente=? and upper(dc.dsValorCampo) like ? and conh.idDoctoServico = ds.idDoctoServico)");
				hql.addCriteriaValue(criteria.getLong("informacaoDoctoCliente.idInformacaoDoctoCliente"));
				hql.addCriteriaValue(criteria.getString("nrDocumentoCliente").toUpperCase());
			}else
				hql.addCustomCriteria("exists(select dc.idDadosComplemento " +
						"from DadosComplemento dc " +
						"left outer join dc.informacaoDoctoCliente idc " +
						"left outer join dc.conhecimento conh " +
						"where idc.idInformacaoDoctoCliente=? and conh.idDoctoServico = ds.idDoctoServico)",criteria.getLong("informacaoDoctoCliente.idInformacaoDoctoCliente"));
		}
		//RESPONSAVEL FRETE
		if(criteria.getLong("responsavelFrete.idCliente")!=null)
			hql.addCustomCriteria("exists(select ddsf.idDevedorDocServFat " +
					"from DevedorDocServFat ddsf " +
					"join ddsf.cliente cli " +
					"join ddsf.doctoServico doc " +
					"where cli.idCliente = ? and doc.idDoctoServico = ds.idDoctoServico)",criteria.getLong("responsavelFrete.idCliente"));
		
		//CONTROLE CARGA
		if(criteria.getLong("controleCarga.idControleCarga")!= null)
			hql.addCustomCriteria("exists(select pmd.idPreManifestoDocumento " +
					"from PreManifestoDocumento pmd " +
					"join pmd.manifesto man " +
					"join man.controleCarga cc " +
					"join pmd.doctoServico doc " +
					"where cc.idControleCarga = ? and doc.idDoctoServico = ds.idDoctoServico)" ,criteria.getLong("controleCarga.idControleCarga"));
		
		//MANIFESTO DE ENTREGA
		if(criteria.getLong("manifestoEntrega.idManifestoEntrega")!=null)
			hql.addCustomCriteria("exists(select med.idManifestoEntregaDocumento from ManifestoEntregaDocumento med " +
					"join med.manifestoEntrega me " +
					"join med.doctoServico doc " +
					"where me.idManifestoEntrega = ? and doc.idDoctoServico = ds.idDoctoServico)", criteria.getLong("manifestoEntrega.idManifestoEntrega"));
		
		if(criteria.getLong("mir.idMir")!= null)
			hql.addCustomCriteria("exists(select dm.idDocumentoMir " +
					"from DocumentoMir dm " +
					"join dm.mir mir " +
					"left outer join dm.reciboReembolso rr " +
					//"left outer join dm.registroDocumentoEntrega rde " +
					//"left outer join rde.doctoServico doc " +
					"where mir.idMir = ? and rr.idDoctoServico = ds.idDoctoServico)",criteria.getLong("mir.idMir"));
		
		if(StringUtils.isNotBlank(criteria.getString("finalidade")))
			hql.addCustomCriteria("exists(select co.idDoctoServico from Conhecimento co " +
					"where co.tpConhecimento = ? and co.idDoctoServico = ds.idDoctoServico) ",criteria.getString("finalidade"));
		
		if(criteria.getLong("awb.idAwb")!= null)
			hql.addCustomCriteria("exists(select ca.idAwb from Awb ca " +
					"join ca.conhecimento conh " +
					"join ca.awb awb where awb.idAwb = ? and conh.idDoctoServico = ds.idDoctoServico )",criteria.getLong("awb.idAwb"));
		
		return hql;
	}
	
	public Object findByIdDSByLocalizacaoMercadoria(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();
		
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("dsDhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("vlTotalParcelas", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dsSgFilial", Hibernate.STRING);
				sqlQuery.addScalar("dsServico", Hibernate.STRING);
				sqlQuery.addScalar("tpConhecimento", Hibernate.STRING);
				sqlQuery.addScalar("tpFrete", Hibernate.STRING);
				sqlQuery.addScalar("filialDestino", Hibernate.STRING);
				sqlQuery.addScalar("dsSimbolo", Hibernate.STRING);
				sqlQuery.addScalar("nmFantasiaDestino", Hibernate.STRING);
				sqlQuery.addScalar("dvConhecimento", Hibernate.INTEGER);
				sqlQuery.addScalar("dsLocalMercadoria", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoReme", Hibernate.STRING);
				sqlQuery.addScalar("nmPessoaReme", Hibernate.STRING);
				sqlQuery.addScalar("idPessoaReme", Hibernate.LONG);
				sqlQuery.addScalar("nrIdentificacaoDest", Hibernate.STRING);
				sqlQuery.addScalar("nmPessoaDest", Hibernate.STRING);
				sqlQuery.addScalar("idPessoaDest", Hibernate.LONG);
				sqlQuery.addScalar("nrIdentificacaoConsi", Hibernate.STRING);
				sqlQuery.addScalar("nmPessoaConsi", Hibernate.STRING);
				sqlQuery.addScalar("idPessoaConsi", Hibernate.LONG);
				sqlQuery.addScalar("idFilial", Hibernate.LONG);				
				sqlQuery.addScalar("tpIdentificacaoReme", Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoDest", Hibernate.STRING);
				sqlQuery.addScalar("tpSituacaoConhecimento", Hibernate.STRING);
				sqlQuery.addScalar("cdFilial", Hibernate.LONG);
				sqlQuery.addScalar("tpSituacaoDocumento", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialLocalizacao", Hibernate.STRING);
				sqlQuery.addScalar("nmFilialLocalizacao", Hibernate.STRING);
				sqlQuery.addScalar("numeroCae", Hibernate.STRING);
				sqlQuery.addScalar("idEmpresaFilialOrigem", Hibernate.LONG);
			}
		};
		
		sql.addProjection("DS.TP_DOCUMENTO_SERVICO" , "tpDocumentoServico");
		sql.addProjection("DS.NR_DOCTO_SERVICO","nrDoctoServico ");
		sql.addProjection("DS.ID_DOCTO_SERVICO" ,"idDoctoServico " );
		sql.addProjection("DS.DH_EMISSAO","dsDhEmissao" );
		sql.addProjection("DS.VL_FRETE_LIQUIDO","vlTotalParcelas");
		sql.addProjection("MO.DS_SIMBOLO","dsSimbolo");
		sql.addProjection("FILORIGEM.SG_FILIAL" , "dsSgFilial " );
		sql.addProjection("FILORIGEM.ID_FILIAL" , "idFilial " );
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("SERV.DS_SERVICO_I"), "dsServico" );
		sql.addProjection("CONH.TP_CONHECIMENTO" ,"tpConhecimento " );
		sql.addProjection("CONH.DV_CONHECIMENTO" ,"dvConhecimento " );
		sql.addProjection("CONH.TP_FRETE" ,"tpFrete " );
		sql.addProjection("FILDESTINO.SG_FILIAL","filialDestino");
		sql.addProjection("PESDESTINO.NM_FANTASIA","nmFantasiaDestino");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LOCMERC.DS_LOCALIZACAO_MERCADORIA_I")+"||' '||DS.OB_COMPLEMENTO_LOCALIZACAO", "dsLocalMercadoria");
		sql.addProjection("PES_REM.NR_IDENTIFICACAO","nrIdentificacaoReme");
		sql.addProjection("PES_REM.TP_IDENTIFICACAO","tpIdentificacaoReme");
		sql.addProjection("PES_REM.NM_PESSOA","nmPessoaReme");
		sql.addProjection("PES_REM.ID_PESSOA","idPessoaReme");
		sql.addProjection("PES_DEST.NR_IDENTIFICACAO","nrIdentificacaoDest");
		sql.addProjection("PES_DEST.TP_IDENTIFICACAO","tpIdentificacaoDest");
		sql.addProjection("PES_DEST.NM_PESSOA","nmPessoaDest");
		sql.addProjection("PES_DEST.ID_PESSOA","idPessoaDest");
		sql.addProjection("PES_CONSI.NR_IDENTIFICACAO","nrIdentificacaoConsi");
		sql.addProjection("PES_CONSI.NM_PESSOA","nmPessoaConsi");
		sql.addProjection("PES_CONSI.ID_PESSOA","idPessoaConsi");
		sql.addProjection("CONH.TP_SITUACAO_CONHECIMENTO","tpSituacaoConhecimento");
		sql.addProjection("FILORIGEM.CD_FILIAL" , "cdFilial " );
		sql.addProjection("MON_DOC_ELET.TP_SITUACAO_DOCUMENTO" ,"tpSituacaoDocumento " );
		sql.addProjection("FILLOCAL.SG_FILIAL" , "sgFilialLocalizacao" );
		sql.addProjection("PESLOCAL.NM_FANTASIA","nmFilialLocalizacao");
		sql.addProjection("CONH.NR_CAE","numeroCae");
		sql.addProjection("FILORIGEM.ID_EMPRESA" , "idEmpresaFilialOrigem" );
		
		sql.addFrom("CONHECIMENTO","CONH");
		
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("FILIAL","FILORIGEM");
		sql.addFrom("FILIAL","FILDESTINO");
		sql.addFrom("PESSOA","PESDESTINO");
		sql.addFrom("SERVICO","SERV");
		sql.addFrom("MOEDA", "MO");
		sql.addFrom("CLIENTE","CLI_REM");
		sql.addFrom("CLIENTE","CLI_DEST");
		sql.addFrom("CLIENTE","CLI_CONSI");
		sql.addFrom("PESSOA","PES_REM");
		sql.addFrom("PESSOA","PES_DEST");
		sql.addFrom("PESSOA","PES_CONSI");
		sql.addFrom("MONITORAMENTO_DOC_ELETRONICO","MON_DOC_ELET");
		
		sql.addFrom("LOCALIZACAO_MERCADORIA","LOCMERC");
		sql.addJoin("LOCMERC.ID_LOCALIZACAO_MERCADORIA(+)","DS.ID_LOCALIZACAO_MERCADORIA");
		
		sql.addFrom("FILIAL","FILLOCAL");
		sql.addFrom("PESSOA","PESLOCAL");
		sql.addJoin("DS.ID_FILIAL_LOCALIZACAO","FILLOCAL.ID_FILIAL(+)");
		sql.addJoin("FILLOCAL.ID_FILIAL","PESLOCAL.ID_PESSOA(+)");
		
		sql.addJoin("DS.ID_CLIENTE_REMETENTE","CLI_REM.ID_CLIENTE(+)");
		sql.addJoin("DS.ID_CLIENTE_DESTINATARIO","CLI_DEST.ID_CLIENTE(+)");
		sql.addJoin("DS.ID_CLIENTE_CONSIGNATARIO","CLI_CONSI.ID_CLIENTE(+)");
		sql.addJoin("CLI_DEST.ID_CLIENTE","PES_DEST.ID_PESSOA(+)");
		sql.addJoin("CLI_REM.ID_CLIENTE","PES_REM.ID_PESSOA(+)");
		sql.addJoin("CLI_CONSI.ID_CLIENTE","PES_CONSI.ID_PESSOA(+)");
		
		sql.addJoin("CONH.ID_CONHECIMENTO(+)","DS.ID_DOCTO_SERVICO");
		sql.addJoin("DS.ID_FILIAL_ORIGEM","FILORIGEM.ID_FILIAL");
		sql.addJoin("DS.ID_FILIAL_DESTINO","FILDESTINO.ID_FILIAL(+)");
		sql.addJoin("FILDESTINO.ID_FILIAL","PESDESTINO.ID_PESSOA(+)");
		sql.addJoin("DS.ID_SERVICO","SERV.ID_SERVICO(+)");
		sql.addJoin("MON_DOC_ELET.ID_DOCTO_SERVICO(+)","DS.ID_DOCTO_SERVICO");
		sql.addJoin("DS.ID_MOEDA","MO.ID_MOEDA");
			
		sql.addCriteria("DS.ID_DOCTO_SERVICO","=",criteria.getLong("idDoctoServico"));
		//Garante que não retorne um registro que a empresa parceira não tem permissão de visualizar
		sql.addCriteria("FILORIGEM.id_empresa","=",criteria.getLong("idEmpresa"));
		
		return getAdsmHibernateTemplate().findByIdBySql(sql.getSql(),sql.getCriteria(), configSql);
	}
	
	public Object findByIdDSByLocalizacaoMercadoriaPrincipal(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("qtVolumes", Hibernate.INTEGER);
				sqlQuery.addScalar("psReal", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("psAforado", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("psReferenciaCalculo", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dsEnderecoEntrega", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialOriginal", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServicoOriginal", Hibernate.LONG);
				sqlQuery.addScalar("sgFilialPedCol", Hibernate.STRING);
				sqlQuery.addScalar("nrColeta", Hibernate.LONG);
				sqlQuery.addScalar("nmPessoRem", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoRem", Hibernate.STRING);
				sqlQuery.addScalar("nmPessoDest", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoDest", Hibernate.STRING);
				sqlQuery.addScalar("nrNotaFiscal", Hibernate.INTEGER);
				sqlQuery.addScalar("qtNotaFiscal", Hibernate.INTEGER);
				sqlQuery.addScalar("tpDensidade", Hibernate.STRING);
				sqlQuery.addScalar("dsNaturezaProduto", Hibernate.STRING);
				sqlQuery.addScalar("nrAwb", Hibernate.LONG);
				sqlQuery.addScalar("dvAwb", Hibernate.INTEGER);
				sqlQuery.addScalar("nmEmpresaCiaAerea", Hibernate.STRING);
				
				sqlQuery.addScalar("dtPrevEntrega", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				
				sqlQuery.addScalar("nrDiasEntrega", Hibernate.SHORT);
				sqlQuery.addScalar("nrDiasBloqueio", Hibernate.SHORT);
				sqlQuery.addScalar("nmRecebedor", Hibernate.STRING);
				sqlQuery.addScalar("dsLocalMercadoria", Hibernate.STRING);
				sqlQuery.addScalar("dtAgendamento",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dsTurno", Hibernate.STRING);
				
				sqlQuery.addScalar("edColeta", Hibernate.STRING);
				sqlQuery.addScalar("municipioColeta", Hibernate.STRING);
				sqlQuery.addScalar("nmUfColeta", Hibernate.STRING);
				sqlQuery.addScalar("sgUfColeta", Hibernate.STRING);
				sqlQuery.addScalar("paisColeta", Hibernate.STRING);
				sqlQuery.addScalar("dhBaixa", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("tpIdentificacaoRem", Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoDest", Hibernate.STRING);
				sqlQuery.addScalar("vlMercadoria", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dsSimboloMoeda", Hibernate.STRING);
				sqlQuery.addScalar("hrPreferenciaInicial", Hibernate.custom(JodaTimeTimeOfDayUserType.class));
				sqlQuery.addScalar("hrPreferenciaFinal",  Hibernate.custom(JodaTimeTimeOfDayUserType.class));
				sqlQuery.addScalar("solicitacaoPriorizacao",  Hibernate.STRING);
				sqlQuery.addScalar("solicitacaoRetirada",  Hibernate.STRING);
				sqlQuery.addScalar("dsRota",  Hibernate.STRING);
				sqlQuery.addScalar("nrDiasRealEntrega", Hibernate.SHORT);
				sqlQuery.addScalar("nrFormulario", Hibernate.LONG);
				sqlQuery.addScalar("nrFatorDensidade", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("nrDocumentoEletronico", Hibernate.LONG);
				sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
				sqlQuery.addScalar("psAferido", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("municipioEntrega", Hibernate.STRING);
				sqlQuery.addScalar("nmUfEntrega", Hibernate.STRING);
				sqlQuery.addScalar("sgUfEntrega", Hibernate.STRING);
				sqlQuery.addScalar("paisEntrega", Hibernate.STRING);
				
				sqlQuery.addScalar("idAwb", Hibernate.LONG);
				sqlQuery.addScalar("dsSerie", Hibernate.STRING);
				sqlQuery.addScalar("sgEmpresaCiaAerea", Hibernate.STRING);
				sqlQuery.addScalar("tpStatusAwb", Hibernate.STRING);
                sqlQuery.addScalar("dtNovaPrevisaoEntrega", Hibernate.STRING);
			}
		};
		
		sql.addProjection("CONH.NR_FORMULARIO" , "nrFormulario");

		//PROJECAO DOCUMENTO DE SERVICO
		sql.addProjection("DS.QT_VOLUMES" , "qtVolumes");
		sql.addProjection("DS.PS_REAL","psReal");
		sql.addProjection("DS.PS_AFORADO" ,"psAforado " );
		sql.addProjection("DS.PS_REFERENCIA_CALCULO","psReferenciaCalculo" );
		sql.addProjection("DS.VL_MERCADORIA","vlMercadoria");
		sql.addProjection("DS.DS_ENDERECO_ENTREGA_REAL","dsEnderecoEntrega");
		sql.addProjection("DS.NR_FATOR_DENSIDADE","nrFatorDensidade");
		sql.addProjection("DS.TP_DOCUMENTO_SERVICO","tpDocumentoServico");
		sql.addProjection("DS.PS_AFERIDO" ,"psAferido " );
		
		//PROJECAO ROTA
		sql.addProjection("nvl2(RCE.NR_ROTA, RCE.NR_ROTA||' - '||RCE.DS_ROTA, RCE.DS_ROTA)", "dsRota");
		
		//PROJECAO DE MOEDA
		sql.addProjection("MO.DS_SIMBOLO","dsSimboloMoeda");
		
		//PROJECAO DOCTO ORIGINAL
		sql.addProjection("FILORIGINAL.SG_FILIAL" , "sgFilialOriginal" );
		sql.addProjection("DSORIGINAL.NR_DOCTO_SERVICO" , "nrDoctoServicoOriginal" );
		
		//PROJECAO PEDIDO COLETA
		sql.addProjection("FILCOL.SG_FILIAL","sgFilialPedCol");
		sql.addProjection("PEDCOL.NR_COLETA","nrColeta");
		
		   	
		// PROJECAO REMETENTE
		sql.addProjection("PESREM.NM_PESSOA","nmPessoRem");
		sql.addProjection("PESREM.NR_IDENTIFICACAO","nrIdentificacaoRem");
		sql.addProjection("PESREM.TP_IDENTIFICACAO","tpIdentificacaoRem");
		
		 // PROJECAO DESTINATARIO
		sql.addProjection("PESDEST.NM_PESSOA","nmPessoDest");
		sql.addProjection("PESDEST.NR_IDENTIFICACAO","nrIdentificacaoDest");
		sql.addProjection("PESDEST.TP_IDENTIFICACAO","tpIdentificacaoDest");
		
		//PROJECAO NOTA FISCAL
		sql.addProjection("(SELECT MIN(NFC.NR_NOTA_FISCAL) FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = CONH.ID_CONHECIMENTO)","nrNotaFiscal");

		sql.addProjection("(SELECT 'Sim' FROM REGISTRO_PRIORIZACAO_DOCTO RPD, REGISTRO_PRIORIZACAO_EMBARQ RPE WHERE RPD.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND RPD.ID_REGISTRO_PRIORIZACAO_EMBARQ = RPE.ID_REGISTRO_PRIORIZACAO_EMBARQ AND RPE.DH_CANCELAMENTO IS NULL)","solicitacaoPriorizacao");

		//PROJECAO QUANTIDADE DE NOTAS
		sql.addProjection("(SELECT COUNT(*) FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = CONH.ID_CONHECIMENTO)","qtNotaFiscal");

		//PROJECAO DENSIDADE
		sql.addProjection("DENSCONH.TP_DENSIDADE","tpDensidade");

		//PROJECAO MONITORAMENTO - nrDocumentoEletronico
		sql.addProjection("MON_DOC_ELET.NR_DOCUMENTO_ELETRONICO","nrDocumentoEletronico");
		
		//PROJECAO NATUREZA PRODUTO
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("NATCONH.DS_NATUREZA_PRODUTO_I"),"dsNaturezaProduto");

		//PROJECAO AWB
		sql.addProjection("V_AWB.NR_AWB","nrAwb");
		sql.addProjection("V_AWB.DV_AWB","dvAwb");

		//PROJECAO CIA AEREA
		sql.addProjection("V_AWB.NM_PESSOA","nmEmpresaCiaAerea");
		
		//PROJECAO ENTREGA
		sql.addProjection("DS.DT_PREV_ENTREGA","dtPrevEntrega");
		sql.addProjection("DS.NR_DIAS_PREV_ENTREGA","nrDiasEntrega");
		sql.addProjection("DS.NR_DIAS_REAL_ENTREGA", "nrDiasRealEntrega");
		sql.addProjection("DS.NR_DIAS_BLOQUEIO","nrDiasBloqueio");

		sql.addProjection("(SELECT 'Sim' FROM DOCUMENTO_SERVICO_RETIRADA DSR, SOLICITACAO_RETIRADA SR WHERE DSR.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND DSR.ID_SOLICITACAO_RETIRADA = SR.ID_SOLICITACAO_RETIRADA AND SR.TP_SITUACAO <> 'C')","solicitacaoRetirada");

		sql.addProjection("(SELECT MAX(MED.NM_RECEBEDOR) FROM " +
				"MANIFESTO_ENTREGA_DOCUMENTO MED,OCORRENCIA_ENTREGA OE " +
				"WHERE MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND " +
				"MED.ID_OCORRENCIA_ENTREGA=OE.ID_OCORRENCIA_ENTREGA(+) AND " +
				"OE.TP_OCORRENCIA= 'E')","nmRecebedor");
		
		sql.addProjection("(SELECT MAX(EDS.DH_EVENTO) FROM EVENTO_DOCUMENTO_SERVICO EDS, EVENTO EV WHERE EDS.ID_EVENTO = EV.ID_EVENTO AND EDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND EDS.BL_EVENTO_CANCELADO='N' AND EV.CD_EVENTO=21)","dhBaixa");
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LOCMERC.DS_LOCALIZACAO_MERCADORIA_I"), "dsLocalMercadoria");
		
		//PROJECAO PROGRAMACAO
		sql.addProjection("V_PROG.DT_AGENDAMENTO","dtAgendamento");
		sql.addProjection("V_PROG.DS_TURNO","dsTurno");
		sql.addProjection("V_PROG.HR_PREFERENCIA_INICIAL","hrPreferenciaInicial");
		sql.addProjection("V_PROG.HR_PREFERENCIA_FINAL","hrPreferenciaFinal");
		
		//PROJECAO COLETA
		sql.addProjection("NVL2(PEDCOL.NR_ENDERECO,PEDCOL.ED_COLETA||', '||PEDCOL.NR_ENDERECO,PEDCOL.ED_COLETA)","edColeta");
		sql.addProjection("MUNCOL.NM_MUNICIPIO","municipioColeta");
		sql.addProjection("UFCOL.NM_UNIDADE_FEDERATIVA","nmUfColeta");
		sql.addProjection("UFCOL.SG_UNIDADE_FEDERATIVA","sgUfColeta");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAISCOL.NM_PAIS_I"),"paisColeta");
		
		//Municipio de entrega
		sql.addProjection("MUNENT.NM_MUNICIPIO","municipioEntrega");
		sql.addProjection("UFENT.NM_UNIDADE_FEDERATIVA","nmUfEntrega");
		sql.addProjection("UFENT.SG_UNIDADE_FEDERATIVA","sgUfEntrega");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAISENT.NM_PAIS_I"),"paisEntrega");

		
		//PROJECAO AWB CONT.
				sql.addProjection("V_AWB.ID_AWB","idAwb");
				sql.addProjection("V_AWB.DS_SERIE","dsSerie");
				sql.addProjection("V_AWB.SG_EMPRESA","sgEmpresaCiaAerea");
				sql.addProjection("V_AWB.TP_STATUS_AWB","tpStatusAwb");
		
        sql.addProjection("(select to_char(ndds.NOVO_DT_PREV_ENTREGA, 'DD/MM/YYYY') from NOVO_DPE_DOCTO_SERVICO ndds where ndds.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO and BL_ATENDIDO = 'S')","dtNovaPrevisaoEntrega");
		
		//*******************************************FROM*************************************************************
		
		//DOCUMENTO DE SERVICO
		sql.addFrom("DOCTO_SERVICO", "DS");
		
		sql.addFrom("MOEDA", "MO");
		sql.addJoin("DS.ID_MOEDA","MO.ID_MOEDA");
		
		//ROTA COLETA ENTREGA
		sql.addFrom("ROTA_COLETA_ENTREGA", "RCE");
		sql.addJoin("DS.ID_ROTA_COLETA_ENTREGA_REAL","RCE.ID_ROTA_COLETA_ENTREGA(+)");
		
		//CONHECIMENTO
		sql.addFrom("CONHECIMENTO","CONH");
		sql.addJoin("DS.ID_DOCTO_SERVICO","CONH.ID_CONHECIMENTO(+)");
		
		//DENSIDADE CONHECIMENTO
		sql.addFrom("DENSIDADE","DENSCONH");
		sql.addJoin("CONH.ID_DENSIDADE","DENSCONH.ID_DENSIDADE(+)");
		
		// MONITORAMENTO DOCUMENTO ELETRONICO (restrição de banco, garante relacionamento 1-1)
		sql.addFrom("MONITORAMENTO_DOC_ELETRONICO", "MON_DOC_ELET");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "MON_DOC_ELET.ID_DOCTO_SERVICO(+)");
		
		//NATUREZA CONHECIMENTO
		sql.addFrom("NATUREZA_PRODUTO","NATCONH");
		sql.addJoin("CONH.ID_NATUREZA_PRODUTO","NATCONH.ID_NATUREZA_PRODUTO(+)");
		
		//DOCUMENTO DE SERVICO ORIGINAL
		sql.addFrom("DOCTO_SERVICO", "DSORIGINAL");
		sql.addFrom("FILIAL","FILORIGINAL");
		sql.addJoin("DS.ID_DOCTO_SERVICO_ORIGINAL","DSORIGINAL.ID_DOCTO_SERVICO(+)");
		sql.addJoin("DSORIGINAL.ID_FILIAL_ORIGEM","FILORIGINAL.ID_FILIAL(+)");
		
		//PEDIDO DE COLETA
		sql.addFrom("PEDIDO_COLETA","PEDCOL");
		sql.addFrom("FILIAL","FILCOL");
		sql.addFrom("MUNICIPIO","MUNCOL");
		sql.addFrom("UNIDADE_FEDERATIVA","UFCOL");
		sql.addFrom("PAIS","PAISCOL");
		sql.addJoin("DS.ID_PEDIDO_COLETA","PEDCOL.ID_PEDIDO_COLETA(+)");
		sql.addJoin("PEDCOL.ID_FILIAL_RESPONSAVEL","FILCOL.ID_FILIAL(+)");
		sql.addJoin("PEDCOL.ID_MUNICIPIO","MUNCOL.ID_MUNICIPIO(+)");
		sql.addJoin("MUNCOL.ID_UNIDADE_FEDERATIVA","UFCOL.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("UFCOL.ID_PAIS","PAISCOL.ID_PAIS(+)");
		
		//REMETENTE
		sql.addFrom("CLIENTE","REM");
		sql.addFrom("PESSOA","PESREM");
		sql.addJoin("DS.ID_CLIENTE_REMETENTE","REM.ID_CLIENTE(+)");
		sql.addJoin("REM.ID_CLIENTE","PESREM.ID_PESSOA(+)");
		
	   //DESTINATARIO
		sql.addFrom("CLIENTE","DEST");
		sql.addFrom("PESSOA","PESDEST");
		sql.addJoin("DS.ID_CLIENTE_DESTINATARIO","DEST.ID_CLIENTE(+)");
		sql.addJoin("DEST.ID_CLIENTE","PESDEST.ID_PESSOA(+)");
		
		//AWB
		sql.addJoin("V_AWB.ID_CONHECIMENTO(+)","CONH.ID_CONHECIMENTO");

		//ENTREGA
		sql.addFrom("MUNICIPIO","MUNENT");
		sql.addFrom("UNIDADE_FEDERATIVA","UFENT");
		sql.addFrom("PAIS","PAISENT");
		sql.addJoin("CONH.ID_MUNICIPIO_ENTREGA","MUNENT.ID_MUNICIPIO(+)");
		sql.addJoin("MUNENT.ID_UNIDADE_FEDERATIVA","UFENT.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("UFENT.ID_PAIS","PAISENT.ID_PAIS(+)");
		//**************ENTREGA************************************

		//LOCALIZACAO
		sql.addFrom("LOCALIZACAO_MERCADORIA","LOCMERC");
		sql.addJoin("LOCMERC.ID_LOCALIZACAO_MERCADORIA(+)","DS.ID_LOCALIZACAO_MERCADORIA");

		//PROGRAMACAO
		sql.addJoin("V_PROG.ID_DOCTO_SERVICO(+)","DS.ID_DOCTO_SERVICO");

		sql.addCriteria("DS.ID_DOCTO_SERVICO","=",criteria.getLong("idDoctoServico"));

		//SUB-SELECT AWB
		SqlTemplate sqlAwb = new SqlTemplate();
		sqlAwb.addProjection("AWB.ID_AWB");
		sqlAwb.addProjection("AWB.DS_SERIE");
		sqlAwb.addProjection("AWB.NR_AWB");
		sqlAwb.addProjection("AWB.DV_AWB");
		sqlAwb.addProjection("AWB.TP_STATUS_AWB");
		sqlAwb.addProjection("EMP.SG_EMPRESA");
		sqlAwb.addProjection("PESEMP.NM_PESSOA");
		sqlAwb.addProjection("CTO.ID_CONHECIMENTO");

		sqlAwb.addFrom("AWB","AWB");
		sqlAwb.addFrom("CTO_AWB","CTO");
		sqlAwb.addFrom("CIA_FILIAL_MERCURIO","CIA");
		sqlAwb.addFrom("EMPRESA","EMP");
		sqlAwb.addFrom("PESSOA","PESEMP");
		sqlAwb.addFrom("CONHECIMENTO", "CO");
		sqlAwb.addFrom("DOCTO_SERVICO", "DO");

		sqlAwb.addJoin("DO.ID_DOCTO_SERVICO","CO.ID_CONHECIMENTO(+)");
		sqlAwb.addJoin("CTO.ID_AWB","AWB.ID_AWB(+)");
		sqlAwb.addJoin("AWB.ID_CIA_FILIAL_MERCURIO","CIA.ID_CIA_FILIAL_MERCURIO(+)");
		sqlAwb.addJoin("CIA.ID_EMPRESA","EMP.ID_EMPRESA(+)");
		sqlAwb.addJoin("CIA.ID_EMPRESA","PESEMP.ID_PESSOA(+)");



		sqlAwb.addCustomCriteria("DO.ID_DOCTO_SERVICO = ?");
		sql.addCriteriaValue(criteria.getLong("idDoctoServico"));
		sqlAwb.addCustomCriteria("AWB.ID_AWB = " +
				"(SELECT MAX(AWB1.ID_AWB) FROM AWB AWB1 , CTO_AWB CTO1, CONHECIMENTO CO1 " +
				"WHERE AWB1.TP_STATUS_AWB <> 'C' AND " +
				"DO.ID_DOCTO_SERVICO = CO1.ID_CONHECIMENTO(+) AND " +
				"CTO1.ID_CONHECIMENTO = CO1.ID_CONHECIMENTO AND " +
				"CTO1.ID_AWB = AWB1.ID_AWB))V_AWB");

		//SUB-SELECT PROGRAMACAO
		SqlTemplate sqlProgramacao = new SqlTemplate();

		sqlProgramacao.addProjection("AGENT.DT_AGENDAMENTO");
		sqlProgramacao.addProjection("TURNO.DS_TURNO");

		sqlProgramacao.addProjection("AGENT.HR_PREFERENCIA_INICIAL","HR_PREFERENCIA_INICIAL");
		sqlProgramacao.addProjection("AGENT.HR_PREFERENCIA_FINAL","HR_PREFERENCIA_FINAL");
		sqlProgramacao.addProjection("ADS.ID_DOCTO_SERVICO");

		sqlProgramacao.addFrom("AGENDAMENTO_DOCTO_SERVICO","ADS");
		sqlProgramacao.addFrom("AGENDAMENTO_ENTREGA","AGENT");
		sqlProgramacao.addFrom("TURNO","TURNO");

		sqlProgramacao.addJoin("ADS.ID_AGENDAMENTO_ENTREGA","AGENT.ID_AGENDAMENTO_ENTREGA");
		sqlProgramacao.addJoin("AGENT.ID_TURNO", "TURNO.ID_TURNO(+)");

		sqlProgramacao.addCustomCriteria("ADS.ID_DOCTO_SERVICO = ?");
		sql.addCriteriaValue(criteria.getLong("idDoctoServico"));
		sqlProgramacao.addCustomCriteria("AGENT.TP_AGENDAMENTO <> 'TA'");
		sqlProgramacao.addCustomCriteria("AGENT.TP_SITUACAO_AGENDAMENTO NOT IN ('C','R','T') order by AGENT.ID_AGENDAMENTO_ENTREGA DESC) where rownum = 1)V_PROG");

		sql.addFrom("("+sqlAwb.getSql());
		sql.addFrom("(select * from ("+sqlProgramacao.getSql());

		return getAdsmHibernateTemplate().findByIdBySql(sql.getSql(),sql.getCriteria(), configSql);
	}

	public List findPaginatedItemNFC(Long idNFC){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(infc.cdItemNfCto as cdItemNfCto, " +
				"infc.dsItemNfCto as dsItemNfCto, " +
				"infc.qtVolumes as qtVolumes, " +
				"infc.dsEmbalagem as dsEmbalagem, " +
				"infc.vlUnitario as vlUnitario, " +
				"moeda.dsSimbolo as dsSimbolo, " +
				"moeda.sgMoeda as sgMoeda, " +
				"moeda.dsSimbolo as dsSimbolo2, " +
				"moeda.sgMoeda as sgMoeda2, " +
				"(infc.qtVolumes * infc.vlUnitario) as total)");
		
		hql.addFrom(ItemNfCto.class.getName()+ " infc " +
				"join infc.notaFiscalConhecimento nfc " +
				"join nfc.conhecimento conh " +
				"join conh.moeda moeda");
		
		hql.addCriteria("nfc.idNotaFiscalConhecimento","=",idNFC);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
	}
	
	public Map<String, Object> findLocalizacaoSimplificada(String tpCliente, String nrIdentificacao, String tpDocumento, String nrDocumento) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT new Map( ");
		sb.append("ctrc.id as idConhecimento, ");
		sb.append("ctrc.dhEmissao as dhEmissao, ");
		sb.append("reme.tpIdentificacao as tpIdentificacaoRemetente, ");
		sb.append("reme.nrIdentificacao as nrIdentificacaoRemetente, ");
		sb.append("reme.nmPessoa as nmPessoaRemetente, ");
		sb.append("dest.tpIdentificacao as tpIdentificacaoDestinatario, ");
		sb.append("dest.nrIdentificacao as nrIdentificacaoDestinatario, ");
		sb.append("dest.nmPessoa as nmPessoaDestinatario, ");
		sb.append("fili.sgFilial as sgFilial, ");
		sb.append("ctrc.nrConhecimento as nrConhecimento, ");
		sb.append("muni.nmMunicipio as nmMunicipioEntrega, ");
		sb.append("unif.sgUnidadeFederativa as sgUFEntrega, ");
		sb.append("ctrc.dtPrevEntrega as dtPrevEntrega, ");
		sb.append("(	select sbqr.dhEvento.value ");
		sb.append("		from " + EventoDocumentoServico.class.getName() + " as sbqr ");
		sb.append(" 	where sbqr.blEventoCancelado = 'N' and  ");
		sb.append("		sbqr.ocorrenciaEntrega.idOcorrenciaEntrega = 5 and ");
		sb.append("		sbqr.evento.idEvento = 348 and ");
		sb.append("		sbqr.doctoServico.idDoctoServico = ctrc.idDoctoServico ");
		sb.append(") as dhEntrega, ");
		sb.append("ctrc.dtColeta as dhColeta, ");		
		sb.append("lome.dsLocalizacaoMercadoria as dsLocalizacaoMercadoria, ");
		sb.append("ctrc.obComplementoLocalizacao as obComplementoLocalizacao) ");
		sb.append("FROM ");
		sb.append(Conhecimento.class.getName() + " ctrc ");
		sb.append("JOIN ctrc.clienteByIdClienteRemetente clre ");
		sb.append("JOIN clre.pessoa reme ");
		sb.append("JOIN ctrc.clienteByIdClienteDestinatario clde ");
		sb.append("JOIN clde.pessoa dest ");
		sb.append("JOIN ctrc.localizacaoMercadoria lome ");
		sb.append("JOIN ctrc.filialByIdFilialOrigem fili ");
		sb.append("JOIN ctrc.municipioByIdMunicipioEntrega muni ");
		sb.append("JOIN muni.unidadeFederativa unif ");
		if("P".equals(tpDocumento)) { 
			sb.append("JOIN ctrc.dadosComplementos daco ");
		} else {
			sb.append("JOIN ctrc.notaFiscalConhecimentos nota ");	
		}
		sb.append("WHERE ");
	
		Object param1;
		
		if("P".equals(tpDocumento)) {
			sb.append("   daco.dsValorCampo = ? ");
			param1 = nrDocumento;
		} else {
			sb.append("   nota.nrNotaFiscal = ? ");
			param1 = Integer.parseInt(nrDocumento);
		}
		if("R".equals(tpCliente)) {
			sb.append("   AND reme.nrIdentificacao = ? ");
		} else {
			sb.append("   AND dest.nrIdentificacao = ? ");
		}
		sb.append("ORDER BY ctrc.dhEmissao.value DESC ");			
		
		List localizacao = getAdsmHibernateTemplate().find(sb.toString(), new Object[] {param1, nrIdentificacao});
		
		if(localizacao != null && localizacao.size() > 0) {
			return (Map<String, Object>) localizacao.get(0);
		}
		
		return null;
	}

	public Long findIdLocalizacaoMercadoriaByCdLocalizacaoMercadoria(Short cdLocalizacaoMercadoria){
		StringBuffer sql = new StringBuffer();
		sql.append("select lm.idLocalizacaoMercadoria from " + LocalizacaoMercadoria.class.getName() + " lm ");
		sql.append("where lm.tpSituacao = 'A' and cdLocalizacaoMercadoria = :cdLocalizacaoMercadoria");

		Map criteria = new HashMap();
		criteria.put("cdLocalizacaoMercadoria", cdLocalizacaoMercadoria);

		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), criteria);
	}
}