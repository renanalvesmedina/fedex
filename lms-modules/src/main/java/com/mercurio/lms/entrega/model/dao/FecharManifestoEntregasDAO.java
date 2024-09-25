package com.mercurio.lms.entrega.model.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.RegistroDocumentoEntrega;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FecharManifestoEntregasDAO extends AdsmDao {

	public ResultSetPage findPaginated(TypedFlatMap criterias, FindDefinition findDef) {
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("F.SG_FILIAL","MANI_SG_FILIAL");
		sql.addProjection("ME.NR_MANIFESTO_ENTREGA","MANI_NR_MANIFESTO");
		sql.addProjection("M.ID_MANIFESTO","MANI_ID_MANIFESTO");
		sql.addProjection("P.NM_FANTASIA","MANI_NM_FILIAL");
		sql.addProjection("P.ID_PESSOA","MANI_ID_FILIAL");
		sql.addProjection("M.DH_EMISSAO_MANIFESTO");
		sql.addProjection("RCE.ID_ROTA_COLETA_ENTREGA","ROTA_ID");
		sql.addProjection("RCE.NR_ROTA","ROTA_NR");
		sql.addProjection("RCE.DS_ROTA","ROTA_DS");
		sql.addProjection("CC.DH_CHEGADA_COLETA_ENTREGA");
		sql.addProjection("MT.NR_IDENTIFICADOR","MT_NR_IDENT");
		sql.addProjection("MT.NR_FROTA","MT_NR_FROTA");
		sql.addProjection("MT.ID_MEIO_TRANSPORTE","MT_ID");
		sql.addProjection("MTSR.NR_IDENTIFICADOR","MTSR_NR_IDENT");
		sql.addProjection("MTSR.NR_FROTA","MTSR_NR_FROTA");
		sql.addProjection("MTSR.ID_MEIO_TRANSPORTE","MTSR_ID");
		sql.addProjection("CC.ID_CONTROLE_CARGA","CC_ID_CC");
		sql.addProjection("CC.NR_CONTROLE_CARGA","CC_NR");
		sql.addProjection("CC_F.ID_FILIAL","CC_FI_ID");
		sql.addProjection("CC_F.SG_FILIAL","CC_FI_SG");
		sql.addProjection("CC_P.NM_FANTASIA","CC_FI_NM");
		sql.addProjection(new StringBuilder()
				.append("(")
				.append(" select count(*)")
				.append("   from MANIFESTO_ENTREGA_DOCUMENTO MED inner join DOCTO_SERVICO DS on (MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO)")
				.append("  where MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA")
				.append("    and (MED.TP_SITUACAO_DOCUMENTO not in (?, ?) or")
				.append("         (MED.TP_SITUACAO_DOCUMENTO = ? and MED.BL_RETENCAO_COMPROVANTE_ENT is null))")
				.append("    and DS.BL_BLOQUEADO = ?")
				.append(")")
				.toString(),"PENDENCIAS");
		sql.addProjection("M.TP_MANIFESTO_ENTREGA","MANI_TP_MANIFESTO_ENTREGA");

		sql.addProjection("C.TP_IDENTIFICACAO", "CLI_TP");
		sql.addProjection("C.NR_IDENTIFICACAO", "CLI_NR");
		sql.addProjection("C.NM_PESSOA", "CLI_NOME");

		sql.addProjection("TP_STATUS_MANIFESTO");
		sql.addProjection("TP_MANIFESTO_ENTREGA","TP_MANIFESTO");

		sql.addCriteriaValue("CA");
		sql.addCriteriaValue("FE");
		sql.addCriteriaValue("FE");
		sql.addCriteriaValue("N");

		montaSqlPaginated(criterias,sql);

		sql.addOrderBy("DH_EMISSAO_MANIFESTO");

		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("MANI_SG_FILIAL",Hibernate.STRING);
				sqlQuery.addScalar("MANI_NR_MANIFESTO",Hibernate.INTEGER);
				sqlQuery.addScalar("MANI_ID_MANIFESTO",Hibernate.LONG);
				sqlQuery.addScalar("MANI_NM_FILIAL",Hibernate.STRING);
				sqlQuery.addScalar("MANI_ID_FILIAL",Hibernate.LONG);
				sqlQuery.addScalar("DH_EMISSAO_MANIFESTO",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("ROTA_ID",Hibernate.LONG);
				sqlQuery.addScalar("ROTA_NR",Hibernate.SHORT);
				sqlQuery.addScalar("ROTA_DS",Hibernate.STRING);
				sqlQuery.addScalar("DH_CHEGADA_COLETA_ENTREGA",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("MT_NR_IDENT",Hibernate.STRING);
				sqlQuery.addScalar("MT_NR_FROTA",Hibernate.STRING);
				sqlQuery.addScalar("MT_ID",Hibernate.LONG);
				sqlQuery.addScalar("MTSR_NR_IDENT",Hibernate.STRING);
				sqlQuery.addScalar("MTSR_NR_FROTA",Hibernate.STRING);
				sqlQuery.addScalar("MTSR_ID",Hibernate.LONG);
				sqlQuery.addScalar("CC_ID_CC",Hibernate.LONG);
				sqlQuery.addScalar("CC_NR",Hibernate.LONG);
				sqlQuery.addScalar("CC_FI_ID",Hibernate.LONG);
				sqlQuery.addScalar("CC_FI_SG",Hibernate.STRING);
				sqlQuery.addScalar("CC_FI_NM",Hibernate.STRING);
				sqlQuery.addScalar("PENDENCIAS",Hibernate.INTEGER);
				sqlQuery.addScalar("MANI_TP_MANIFESTO_ENTREGA",Hibernate.STRING);
				sqlQuery.addScalar("CLI_TP",Hibernate.STRING);
				sqlQuery.addScalar("CLI_NR",Hibernate.STRING);
				sqlQuery.addScalar("CLI_NOME",Hibernate.STRING);
				sqlQuery.addScalar("TP_STATUS_MANIFESTO",Hibernate.STRING);
				sqlQuery.addScalar("TP_MANIFESTO",Hibernate.STRING);
			}
	 	 };
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria(),confSql);
	}

	public Integer getRowCount(TypedFlatMap criterias) {
		SqlTemplate sql = new SqlTemplate();
		montaSqlPaginated(criterias,sql);
		return getAdsmHibernateTemplate().getRowCountBySql(sql.getSql(),sql.getCriteria());
	}

	private void montaSqlPaginated(TypedFlatMap criterias,SqlTemplate sql) {
		sql.addFrom(new StringBuilder("MANIFESTO M ")
			.append("INNER JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = M.ID_MANIFESTO ")
			.append("INNER JOIN FILIAL F ON F.ID_FILIAL = ME.ID_FILIAL ")
			.append("INNER JOIN PESSOA P ON P.ID_PESSOA = F.ID_FILIAL ")
			.append("LEFT JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
			.append("LEFT JOIN FILIAL CC_F ON CC_F.ID_FILIAL = CC.ID_FILIAL_ORIGEM ")
			.append("LEFT JOIN PESSOA CC_P ON CC_P.ID_PESSOA = CC_F.ID_FILIAL ")
			.append("LEFT JOIN ROTA_COLETA_ENTREGA RCE ON RCE.ID_ROTA_COLETA_ENTREGA = CC.ID_ROTA_COLETA_ENTREGA ")
			.append("LEFT JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ")
			.append("LEFT  JOIN PESSOA C ON C.ID_PESSOA = M.ID_CLIENTE_CONSIG ")
			.append("LEFT  JOIN MEIO_TRANSPORTE MTSR ON MTSR.ID_MEIO_TRANSPORTE = CC.ID_SEMI_REBOCADO ").toString());

		sql.addCustomCriteria("( M.TP_STATUS_MANIFESTO IN (?,?) OR (M.TP_MANIFESTO_ENTREGA IN (? ,?) AND M.TP_STATUS_MANIFESTO = ?))");
		sql.addCriteriaValue("DC");
		sql.addCriteriaValue("ED");

		sql.addCriteriaValue("PR");
		sql.addCriteriaValue("CR");
		sql.addCriteriaValue("ME");

		sql.addCriteria("F.ID_FILIAL","=",criterias.getLong("filial.idFilial"));

		if (StringUtils.isNotBlank(criterias.getString("tpManifesto")))
			sql.addCriteria("M.TP_MANIFESTO_ENTREGA","=",criterias.getString("tpManifesto"));

		sql.addCriteria("CC.ID_CONTROLE_CARGA","=",criterias.getLong("controleCarga.idControleCarga"));
		sql.addCriteria("M.ID_MANIFESTO","=",criterias.getLong("manifestoEntrega.idManifestoEntrega"));
		sql.addCriteria("RCE.ID_ROTA_COLETA_ENTREGA","=",criterias.getLong("rotaColetaEntrega.idRotaColetaEntrega"));
		sql.addCriteria("MT.ID_MEIO_TRANSPORTE","=",criterias.getLong("meioTransporte.idMeioTransporte"));
		sql.addCriteria("MTSR.ID_MEIO_TRANSPORTE","=",criterias.getLong("meioTransporteSemiReboque.idMeioTransporte"));
	}

	public AgendamentoEntrega findAgendamentoIfAllClosed(Long idManifesto) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("AE");

		hql.addFrom(new StringBuffer(ManifestoEntregaDocumento.class.getName()).append(" AS MED ")
			.append("INNER JOIN MED.ocorrenciaEntrega OE ")
			.append("INNER JOIN MED.doctoServico DS ")
			.append("INNER JOIN DS.agendamentoDoctoServicos ADS ")
			.append("INNER JOIN ADS.agendamentoEntrega AE ").toString());

		hql.addCriteria("OE.tpOcorrencia","=","E");
		hql.addCriteria("AE.tpSituacaoAgendamento","=","A");
		hql.addCriteria("MED.manifestoEntrega.id","=",idManifesto);

		hql.addCustomCriteria(new StringBuffer("NOT EXISTS (SELECT AE2.id FROM ").append(AgendamentoEntrega.class.getName()).append(" AS AE2 ")
			.append("INNER JOIN AE2.agendamentoDoctoServicos ADS2 ")
			.append("INNER JOIN ADS2.doctoServico DS2 ")
			.append("INNER JOIN DS2.localizacaoMercadoria LM2 ")
			.append("WHERE LM2.cdLocalizacaoMercadoria <> ? AND AE2.id = AE.id) ").toString());
		hql.addCriteriaValue(Short.valueOf("1"));

		List<AgendamentoEntrega> rs = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		if (rs.size() > 0)
			return rs.get(0);
		return null;
	}

	public ResultSetPage findPaginatedPendencias(Long idManifesto,FindDefinition findDef) {
		SqlTemplate hql = montaHqlPendencia(idManifesto);
		hql.addProjection("new Map(DS.tpDocumentoServico","DS_TP");
		hql.addProjection("DS.nrDoctoServico","DS_NR");
		hql.addProjection("DS.id","idDoctoServico");
		hql.addProjection("F.sgFilial","DS_FI");
		hql.addProjection("MED.tpSituacaoDocumento","PENDENCIA");
		hql.addProjection("MED.blRetencaoComprovanteEnt","blRetencaoComprovanteEnt");		
		hql.addProjection("MED.id","idManifestoEntregaDocumento");
		hql.addProjection("PR.id","CR_ID");
		hql.addProjection("PR.nrIdentificacao","CR_NR");
		hql.addProjection("PR.tpIdentificacao","CR_TP");
		hql.addProjection("PR.nmPessoa","CR_NM");
		hql.addProjection("PD.id","C_ID");
		hql.addProjection("PD.nrIdentificacao","C_NR");
		hql.addProjection("PD.tpIdentificacao","C_TP");
		hql.addProjection("PD.nmPessoa","C_NM)");
		hql.addOrderBy("PMD.nrOrdem");

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCountPendencias(Long idManifesto) {
		SqlTemplate hql = montaHqlPendencia(idManifesto);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}
	
	private SqlTemplate montaHqlPendencia(Long idManifesto) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(new StringBuffer(ManifestoEntregaDocumento.class.getName()).append(" AS MED ")
			.append("INNER JOIN MED.manifestoEntrega ME ")
			.append("INNER JOIN ME.manifesto MA ")
			.append("INNER JOIN MA.preManifestoDocumentos PMD ")
			.append("INNER JOIN PMD.doctoServico PMDS ")
			.append("INNER JOIN MED.doctoServico DS ")
			.append("INNER JOIN DS.filialByIdFilialOrigem F ")
			.append("INNER JOIN DS.clienteByIdClienteRemetente CR ")
			.append("INNER JOIN CR.pessoa PR ")
			.append("INNER JOIN DS.clienteByIdClienteDestinatario CD ")
			.append("INNER JOIN CD.pessoa PD ").toString());
		
		hql.addCustomCriteria(new StringBuilder()
				.append("(MED.tpSituacaoDocumento not in (?,?)")
				.append(" OR MED.blRetencaoComprovanteEnt is null)")
				.toString());
		hql.addCriteriaValue("CANC");
		hql.addCriteriaValue("FECH");
		
		hql.addCustomCriteria("DS.idDoctoServico = PMDS.idDoctoServico");

		hql.addCriteria("ME.id","=",idManifesto);
		return hql;
	}
	
	
	/**
	 * Esse metodo deveria retornar apenas um registro mas como não tenho certeza que não a furo vou tratar isso na service
	 * @param idDoctoServico
	 * @return
	 */
	public List findRegistroDocumentoEntregaByIdDoctoServico(Long idDoctoServico) {
		return getAdsmHibernateTemplate().findByDetachedCriteria(DetachedCriteria.forClass(RegistroDocumentoEntrega.class,"RDE")
				.add(Restrictions.eq("RDE.doctoServico.id",idDoctoServico)));
	}
	/**
	 * CONSULTA PARA A REGRA 1 DE FECHAR MANIFESTO
	 * @param idManifestoEntrega
	 * @return
	 */
	public Integer getRowCountManifestoEntregaDocumentoPendenteBaixa(Long idManifestoEntrega) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(DetachedCriteria.forClass(ManifestoEntregaDocumento.class,"MED")
				.setProjection(Projections.rowCount())
				.add(Restrictions.eq("MED.manifestoEntrega.id",idManifestoEntrega))
				.add(Restrictions.or(
						Restrictions.in("MED.tpSituacaoDocumento",new Object[]{"PBRC","PBRE","PBCO","PBAI"}),
						Restrictions.isNull("MED.blRetencaoComprovanteEnt")
					)
				));
		
	}
	/**
	 * CONSULTA PARA A REGRA 2 DE FECHAR MANIFESTO
	 * @param idManifestoEntrega
	 * @return
	 */
	public Integer getRowCountRegistroDocumentoEntrega(Long idManifestoEntrega) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(DetachedCriteria.forClass(RegistroDocumentoEntrega.class,"RDE")
				.setProjection(Projections.rowCount())
				.createAlias("RDE.doctoServico","DS")
				.createAlias("DS.manifestoEntregaDocumentos","MED")
				.createAlias("MED.ocorrenciaEntrega","OE")
				.add(Restrictions.eq("MED.manifestoEntrega.id",idManifestoEntrega))
				.add(Restrictions.eq("RDE.blComprovanteRecolhido",Boolean.FALSE))
				.add(Restrictions.eq("OE.tpOcorrencia","E"))
				.add(Restrictions.isNull("RDE.obComprovante")));
	}
	
	/**
	 * CONSULTA PARA A REGRA 3 DE FECHAR MANIFESTO
	 * @param idManifestoEntrega
	 * @return
	 */
	public Integer getRowCountReciboReembolso(Long idManifestoEntrega) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(RegistroDocumentoEntrega.class.getName(),"RDE");
		hql.addFrom(ReciboReembolso.class.getName(),"RR");
		hql.addFrom(ManifestoEntregaDocumento.class.getName(),"MED");
		hql.addFrom(OcorrenciaEntrega.class.getName(),"OE");
		hql.addJoin("RDE.doctoServico.id","RR.id");
		hql.addJoin("MED.doctoServico.id","RR.id");
		hql.addJoin("MED.ocorrenciaEntrega.id","OE.id");
		
		hql.addCriteria("MED.manifestoEntrega.id","=",idManifestoEntrega);
		hql.addCriteria("OE.tpOcorrencia","=","E");
		hql.addCustomCriteria("RR.obRecolhimento IS NULL");
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}
	
	/**
	 * CONSULTA PARA A REGRA 4 DE FECHAR MANIFESTO
	 * @param idManifestoEntrega
	 * @return
	 */
	public Integer getRowCountManifestoEntregaDocumentoJustificado(Long idManifestoEntrega) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(DetachedCriteria.forClass(ManifestoEntregaDocumento.class,"MED")
				.setProjection(Projections.rowCount())
				.add(Restrictions.eq("MED.manifestoEntrega.id",idManifestoEntrega))
				.add(Restrictions.in("MED.tpSituacaoDocumento",new Object[]{"PRCO","PREC","PCOM"})));
	}

	/**
	 * CONSULTA PARA A REGRA 5 DE FECHAR MANIFESTO
	 * @param idManifestoEntrega
	 * @return
	 */
	public List findManifestoEntregaDocumento(Long idManifestoEntrega) {
		return getAdsmHibernateTemplate().findByDetachedCriteria(DetachedCriteria.forClass(ManifestoEntregaDocumento.class,"MED")
				.createAlias("MED.manifestoEntrega","ME")
				.createAlias("ME.filial","F")
				.add(Restrictions.eq("ME.id",idManifestoEntrega)));
	}
	
	/**
	 * CONSULTA PARA A REGRA 5 DE FECHAR MANIFESTO
	 * @param idManifestoEntrega
	 * @return
	 */
	public List findManifestoEntregaDocumentoNaoEntrega(Long idManifestoEntrega) {
		return getAdsmHibernateTemplate().findByDetachedCriteria(DetachedCriteria.forClass(ManifestoEntregaDocumento.class,"MED")
				.createAlias("MED.manifestoEntrega","ME")
				.createAlias("ME.filial","F")
				.createAlias("MED.ocorrenciaEntrega","OE")
				.add(Restrictions.eq("ME.id",idManifestoEntrega))
				.add(Restrictions.ne("OE.tpOcorrencia","E")));
	}

	public List findReciboReembolso(Long idManifestoEntrega) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("RRR");
		hql.addFrom(ReciboReembolso.class.getName(),"RR");
		hql.addFrom(ManifestoEntregaDocumento.class.getName(),"MED");
		hql.addFrom(ReciboReembolso.class.getName(),"RRR");
		hql.addJoin("MED.doctoServico.id","RR.id");
		hql.addJoin("RRR.id","RR.doctoServicoByIdDoctoServReembolsado.id");
		
		hql.addCriteria("MED.manifestoEntrega.id","=",idManifestoEntrega);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	/**
	 * Consulta endereco do documento de Serviço ou conhecimento.
	 * @param deve ser com.mercurio.lms.expedicao.modelDoctoServico ou com.mercurio.lms.expedicao.modelConhecimento
	 * @param idDoctoServico
	 * @return EnderecoPessoa
	 */
	public Municipio findMunicipioByDoctoServico(Class clazz, Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("M");
		
		String joinMunicipio = null;
		if (clazz.equals(DoctoServico.class)) {
			joinMunicipio =
				" INNER JOIN DS.enderecoPessoa AS EP " +
				" INNER JOIN EP.municipio AS M ";
		} else if (clazz.equals(Conhecimento.class)) {
			joinMunicipio =
				" INNER JOIN DS.municipioByIdMunicipioEntrega AS M ";
		} else {
			throw new IllegalArgumentException(
					"clazz deve ser com.mercurio.lms.expedicao.modelDoctoServico ou com.mercurio.lms.expedicao.modelConhecimento");
		}
		
		hql.addFrom(new StringBuffer(clazz.getName()).append(" DS ")
			.append(joinMunicipio)
			.append(" INNER JOIN FETCH M.unidadeFederativa ")
			.toString());
		hql.addCriteria("DS.id","=",idDoctoServico);
		
		return (Municipio)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}
	
	public ReciboReembolso findByIdReciboReembolsado(Long idDoctoServico) {
		return (ReciboReembolso)getAdsmHibernateTemplate().findUniqueResult(DetachedCriteria.forClass(ReciboReembolso.class,"RR")
			.add(Restrictions.eq("RR.doctoServicoByIdDoctoServReembolsado.id",idDoctoServico)));
	}
	
	/**
	 * LMS-3169
	 * Regra 5
	 * @param idManifestoEntrega
	 * @return
	 */
	public List listRNCByManifesto(Long idManifestoEntrega) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(OcorrenciaEntrega.class.getName(),"OE");
		hql.addFrom(ManifestoEntregaDocumento.class.getName(),"MED");
		hql.addJoin("OE.idOcorrenciaEntrega", "MED.ocorrenciaEntrega.idOcorrenciaEntrega");
			
		hql.addCriteria("MED.manifestoEntrega.idManifestoEntrega","=",idManifestoEntrega);
		hql.addCustomCriteria("MED.tpEntregaParcial IS NOT NULL"); 
		hql.addCustomCriteria("MED.ocorrenciaEntrega.idOcorrenciaEntrega IS NOT NULL");
		hql.addCustomCriteria("OE.tpOcorrencia = 'E'");		
		
		hql.addCustomCriteria(" not exists (select 1 from " + ManifestoEntregaDocumento.class.getName() + " as m, " +
								 NaoConformidade.class.getName() + " as n, " + OcorrenciaNaoConformidade.class.getName() + " as onc " +
								"where m.doctoServico.idDoctoServico = n.doctoServico.idDoctoServico " +
								"and onc.naoConformidade.idNaoConformidade = n.idNaoConformidade " +
								"and n.tpStatusNaoConformidade IN ('AGP', 'RRC', 'RNC', 'ROI') " +
								"and onc.tpStatusOcorrenciaNc = 'A' " +
								"and m.idManifestoEntregaDocumento = MED.idManifestoEntregaDocumento)");
										
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
}
	
}
