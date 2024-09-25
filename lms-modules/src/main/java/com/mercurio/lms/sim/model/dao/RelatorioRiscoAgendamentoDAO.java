package com.mercurio.lms.sim.model.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.sim.model.MonitoramentoCCT;



public class RelatorioRiscoAgendamentoDAO extends BaseCrudDao<MonitoramentoCCT, Long> {
	
	public static final int FILIAL_DESTINO = 0;
	public static final int NF = 1;
	public static final int MODAL = 2;
	public static final int EMISSAO_DOCTO_SERV = 3;
	public static final int TP_DOCTO_SERV = 4;
	public static final int FILIAL_ORIGEM = 5;
	public static final int NR_DOCTO_SERV = 6;
	public static final int CIA_AEREA = 7;
	public static final int DS_SERIE = 8;
	public static final int NR_AWB = 9;
	public static final int DV_AWB = 10;
	public static final int REMENTENTE = 11;
	public static final int DESTINATARIO = 12;
	public static final int MUNICIPIO = 13;
	public static final int UF = 14;
	public static final int SITUACAO = 15;
	public static final int DPE = 16;
	public static final int LOCALIZACAO = 17;
	
	@Override
	protected final Class<MonitoramentoCCT> getPersistentClass() {
		return MonitoramentoCCT.class;
	}
	
	public ResultSetPage findPaginatedRelatorioRiscoAgendamento(TypedFlatMap criteria, FindDefinition findDefinition) {
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("filialDestino", Hibernate.STRING);
				sqlQuery.addScalar("nrNotaFilcal", Hibernate.LONG);
				sqlQuery.addScalar("modal", Hibernate.STRING);
				sqlQuery.addScalar("dhEmissaoDoctoServico", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("tpDoctoServico", Hibernate.STRING);
				sqlQuery.addScalar("filialOrigem", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("CiaAerea", Hibernate.STRING);
				sqlQuery.addScalar("dsSerie", Hibernate.STRING);
				sqlQuery.addScalar("nrAwb", Hibernate.LONG);
				sqlQuery.addScalar("dvAwb", Hibernate.INTEGER);
				sqlQuery.addScalar("nmRemetente", Hibernate.STRING);
				sqlQuery.addScalar("nmDestinatario", Hibernate.STRING);
				sqlQuery.addScalar("nmMunicipio", Hibernate.STRING);
				sqlQuery.addScalar("sgUf", Hibernate.STRING);
				sqlQuery.addScalar("situacao", Hibernate.STRING);
				sqlQuery.addScalar("dpe", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("localizacao",Hibernate.STRING);
			}
		};
		StringBuilder sql = montaQuerySqlRelatorioRiscoAgendamento(criteria);
		
		ResultSetPage rs = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(),findDefinition.getCurrentPage(),findDefinition.getPageSize(), criteria, configSql);
		return rs;
	}
	 
	 public StringBuilder montaQuerySqlRelatorioRiscoAgendamento(TypedFlatMap criteria){
		  	
		 StringBuilder sql = new StringBuilder();
		 
		 sql.append("SELECT ")
		.append("FD.SG_FILIAL AS filialDestino, ")
		.append("NFC.NR_NOTA_FISCAL AS nrNotaFilcal, ")
		.append(PropertyVarcharI18nProjection.createProjection("S.DS_SERVICO_I "))
		.append(" AS modal, ")
		.append("DS.DH_EMISSAO AS dhEmissaoDoctoServico, ")
		.append("DS.TP_DOCUMENTO_SERVICO AS tpDoctoServico, ")
		.append("FO.SG_FILIAL AS filialOrigem, ")
		.append("DS.NR_DOCTO_SERVICO AS nrDoctoServico, ")
		.append("PA.NM_PESSOA AS CiaAerea, ")
		.append("(select AWB.DS_SERIE from CTO_AWB, AWB where  DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND CTO_AWB.ID_AWB = AWB.ID_AWB(+) and rownum = 1) AS dsSerie, ")
		.append("(select AWB.NR_AWB from CTO_AWB, AWB where  DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND CTO_AWB.ID_AWB = AWB.ID_AWB(+) and rownum = 1) AS nrAwb, ")
		.append("(select AWB.DV_AWB from CTO_AWB, AWB where  DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO(+) AND CTO_AWB.ID_AWB = AWB.ID_AWB(+) and rownum = 1) AS dvAwb, ")
		.append("PR.NM_PESSOA AS nmRemetente, ")
		.append("PD.NM_PESSOA AS nmDestinatario, ")
		.append("M.NM_MUNICIPIO AS nmMunicipio, ")
		.append("UF.SG_UNIDADE_FEDERATIVA AS sgUf, ")
		.append(" (SELECT ")
		.append(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I "))
		.append(" FROM VALOR_DOMINIO VD, DOMINIO D WHERE " )
		.append(" MC.TP_SITUACAO_NF_CCT  = VD.VL_VALOR_DOMINIO " )
		.append(" AND D.ID_DOMINIO = VD.ID_DOMINIO " )
		.append(" AND D.NM_DOMINIO = 'DM_SITUACAO_NF_CCT') " )
		.append(" AS situacao, ")
		.append("DS.DT_PREV_ENTREGA AS dpe, ")
		.append(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I "))
		.append(" AS localizacao ")
		.append(" FROM DOCTO_SERVICO DS, ")
		.append("FILIAL FD, ")
		.append("FILIAL FO, ")
		.append("NOTA_FISCAL_CONHECIMENTO NFC, ")
		.append("SERVICO S, ")
		.append("CTO_AWB CTA, ")
		.append("AWB A, ")
		.append("CIA_FILIAL_MERCURIO CFM, ")
		.append("PESSOA PA, ")
		.append("PESSOA PR, ")
		.append("PESSOA PD, ")
		.append("LOCALIZACAO_MERCADORIA LM, ")
		.append("(SELECT ID_DOCTO_SERVICO,")
		.append("		 NR_CHAVE,")
		.append("		 TP_SITUACAO_NF_CCT")
		.append(" FROM MONITORAMENTO_CCT CCT WHERE ")
		.append("   EXISTS")
		.append("  (SELECT 1")
		.append("  FROM VALOR_DOMINIO VD")
		.append("  INNER JOIN DOMINIO D ON D.ID_DOMINIO  = VD.ID_DOMINIO")
		.append("  where D.NM_DOMINIO = 'DM_SITUACAO_NF_CCT'")
		.append("  AND VD.VL_VALOR_DOMINIO  = CCT.TP_SITUACAO_NF_CCT")
		.append("  )) MC, ")
		.append("PESSOA PF, ")
		.append("ENDERECO_PESSOA EP, ")
		.append("MUNICIPIO M, ")
		.append("UNIDADE_FEDERATIVA UF, ")
		.append("CLIENTE C ")
		.append("WHERE C.ID_CLIENTE                         = DS.ID_CLIENTE_REMETENTE ")
		.append("AND DS.ID_FILIAL_DESTINO = FD.ID_FILIAL(+) ")
		.append("AND DS.ID_FILIAL_ORIGEM = FO.ID_FILIAL ")
		.append("AND DS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO(+) ")
		.append("AND DS.ID_SERVICO = S.ID_SERVICO(+) ")
		.append("AND DS.ID_DOCTO_SERVICO = CTA.ID_CONHECIMENTO(+) ")
		.append("AND CTA.ID_AWB = A.ID_AWB(+) ")
		.append("AND A.ID_CIA_FILIAL_MERCURIO = CFM.ID_CIA_FILIAL_MERCURIO(+) ")
		.append("AND CFM.ID_EMPRESA = PA.ID_PESSOA(+) ")
		.append("AND DS.ID_CLIENTE_REMETENTE = PR.ID_PESSOA(+) ")
		.append("AND DS.ID_CLIENTE_DESTINATARIO = PD.ID_PESSOA(+) ")
		.append("AND DS.ID_LOCALIZACAO_MERCADORIA = LM. ID_LOCALIZACAO_MERCADORIA(+) ")
		.append("AND NFC.ID_CONHECIMENTO     = MC.ID_DOCTO_SERVICO(+) ")
		.append("AND NFC.NR_CHAVE           = MC.NR_CHAVE (+) ")
		.append("AND FD.ID_FILIAL = PF.ID_PESSOA(+) ")
		.append("AND PF.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA(+) ")
		.append("AND EP.ID_MUNICIPIO = M.ID_MUNICIPIO(+) ")
		.append("AND M.ID_UNIDADE_FEDERATIVA = UF.ID_UNIDADE_FEDERATIVA(+) ")			
		.append("AND LM.CD_LOCALIZACAO_MERCADORIA NOT IN ( "+ buscaLocalizacaoMercadoria().get(0).toString().replace(";", ",") +" ) ")
	 	.append("AND DS.DT_PREV_ENTREGA <= (sysdate+"+ criteria.getInteger("diasRisco") +") ");

		 if (criteria.getBoolean("clienteCCT")){
			 sql.append(" AND C.BL_CLIENTE_CCT = 'S' ");
		 }
		 
		 if(criteria.getLong("idCliente") != null){
			 sql.append(" AND DS.ID_CLIENTE_REMETENTE = " + criteria.getLong("idCliente"));
		 }
		 
		 sql.append(" ORDER BY DS.ID_CLIENTE_REMETENTE, FD.SG_FILIAL ");
		 
		 return sql;
	 }
	 
	 
	 public List<String> buscaLocalizacaoMercadoria(){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT   pg.dsConteudo as dsConteudo ")
			.append( " FROM " +  ParametroGeral.class.getName() + " pg " )
			.append(" WHERE  pg.nmParametroGeral = 'CD_LOCAL_REL_RISCO_AGEND' ");
		
		return getAdsmHibernateTemplate().find(sql.toString());
			
		}
	 
	

		 
		 

	
	 
		
}
