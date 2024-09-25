package com.mercurio.lms.seguros.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ProcessoSinistroDAO extends BaseCrudDao<ProcessoSinistro, Long>
{
	
	private static final String A = "A";

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ProcessoSinistro.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("tipoSinistro", FetchMode.JOIN);
    	lazyFindById.put("controleCarga", FetchMode.JOIN);
    	lazyFindById.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
    	lazyFindById.put("controleCarga.meioTransporteByIdTransportado", FetchMode.JOIN);
    	lazyFindById.put("tipoSinistro", FetchMode.JOIN);
    	lazyFindById.put("tipoSeguro", FetchMode.JOIN);
    	lazyFindById.put("reguladoraSeguro", FetchMode.JOIN);
    	lazyFindById.put("rodovia", FetchMode.JOIN);
    	lazyFindById.put("filial", FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa", FetchMode.JOIN);
    	lazyFindById.put("aeroporto", FetchMode.JOIN);
    	lazyFindById.put("aeroporto.pessoa", FetchMode.JOIN);
    	lazyFindById.put("municipio", FetchMode.JOIN);
    	lazyFindById.put("municipio.unidadeFederativa", FetchMode.JOIN);
    	lazyFindById.put("moeda", FetchMode.JOIN);
    	lazyFindById.put("usuarioAbertura", FetchMode.JOIN);
    	lazyFindById.put("usuarioFechamento", FetchMode.JOIN);
    }

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("tipoSinistro", FetchMode.JOIN);
		lazyFindLookup.put("tipoSeguro", FetchMode.JOIN);
		lazyFindLookup.put("municipio", FetchMode.JOIN);
		lazyFindLookup.put("municipio.unidadeFederativa", FetchMode.JOIN);
	}

    private SqlTemplate getFindPaginatedQuery(TypedFlatMap tfm) {
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("PS.ID_PROCESSO_SINISTRO", "idProcessoSinistro");
    	sql.addProjection("PS.NR_PROCESSO_SINISTRO", "nrProcessoSinistro");
    	sql.addProjection("MT.NR_IDENTIFICADOR", "nrIdentificador");
    	sql.addProjection("MT.NR_FROTA", "nrFrota");
    	sql.addProjection("PS.DH_SINISTRO", "dhSinistro");
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("TSI.DS_TIPO_I"), "tipoSinistro");
    	sql.addProjection("TSE.SG_TIPO", "tipoSeguro");
    	sql.addProjection("MU.NM_MUNICIPIO", "municipio");
    	sql.addProjection("UF.SG_UNIDADE_FEDERATIVA", "uf");
    	sql.addProjection("USU.NM_USUARIO", "usuarioResponsavel");
    	
    	sql.addFrom("PROCESSO_SINISTRO", "PS");
    	sql.addFrom("MUNICIPIO", "MU");
    	sql.addFrom("UNIDADE_FEDERATIVA", "UF");
    	sql.addFrom("TIPO_SINISTRO", "TSI");
    	sql.addFrom("TIPO_SEGURO", "TSE");
    	sql.addFrom("MEIO_TRANSPORTE", "MT");
    	sql.addFrom("FILIAL", "FILIAL_CONTROLE");
    	sql.addFrom("SINISTRO_DOCTO_SERVICO", "SDS");
    	sql.addFrom("DOCTO_SERVICO", "DS");
    	sql.addFrom("CLIENTE", "CLI");
    	sql.addFrom("USUARIO", "USU");
    	
    	sql.addCustomCriteria(new StringBuffer(" PS.ID_MUNICIPIO = MU.ID_MUNICIPIO AND ")  
    			.append(" MU.ID_UNIDADE_FEDERATIVA 	= UF.ID_UNIDADE_FEDERATIVA AND ")
    			.append(" TSI.ID_TIPO_SINISTRO 		= PS.ID_TIPO_SINISTRO AND ")
    			.append(" TSE.ID_TIPO_SEGURO 	    = PS.ID_TIPO_SEGURO AND ")
    			.append(" MT.ID_MEIO_TRANSPORTE(+) 	= PS.ID_MEIO_TRANSPORTE AND ")
    			.append(" FILIAL_CONTROLE.ID_FILIAL(+) = PS.ID_FILIAL AND ")
    			.append(" SDS.ID_PROCESSO_SINISTRO(+)  = PS.ID_PROCESSO_SINISTRO AND ")
    			.append(" SDS.ID_DOCTO_SERVICO 		= DS.ID_DOCTO_SERVICO(+) AND ")
    			.append(" DS.ID_CLIENTE_REMETENTE 	= CLI.ID_CLIENTE(+) AND ")
    			.append(" USU.ID_USUARIO(+) 			= PS.ID_USUARIO_ABERTURA ").toString());

    	sql.addCriteria("PS.NR_PROCESSO_SINISTRO", "like", tfm.getString("nrProcessoSinistro"));
    	if (StringUtils.isNotBlank(tfm.getDomainValue("situacaoProcessoSinistro").getValue())) {
    		if (tfm.getDomainValue("situacaoProcessoSinistro").getValue().equals(A)) {
    			sql.addCustomCriteria("PS.DH_FECHAMENTO IS NULL");
    		} else {
    			sql.addCustomCriteria("PS.DH_FECHAMENTO IS NOT NULL");
    		}
    	}
    	Long idManifesto = tfm.getLong("manifesto.idManifesto");
    	
    	if (idManifesto != null) {
    		sql.addCustomCriteria(" (EXISTS(SELECT MED.ID_MANIFESTO_ENTREGA FROM MANIFESTO_ENTREGA_DOCUMENTO MED " +
			    				  "         WHERE MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
			    				  "         AND MED.ID_MANIFESTO_ENTREGA = " + idManifesto + ") " +
			    				  "  OR EXISTS(SELECT MNC.ID_MANIFESTO_VIAGEM_NACIONAL FROM MANIFESTO_NACIONAL_CTO MNC " +
			    				  " 		WHERE MNC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO " +
			    				  " 		AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = " + idManifesto + " ) " +
			    				  "  OR EXISTS(SELECT MIC.ID_MANIFESTO_INTERNACIONAL FROM MANIFESTO_INTERNAC_CTO MIC " +
			    				  " 		WHERE MIC.ID_CTO_INTERNACIONAL = DS.ID_DOCTO_SERVICO " +
			    				  "			AND MIC.ID_MANIFESTO_INTERNACIONAL = " + idManifesto + " ))");
    	}
    	sql.addCriteria("DS.ID_DOCTO_SERVICO", "=", tfm.getLong("doctoServico.idDoctoServico"));

    	Long idControleCarga = tfm.getLong("controleCarga.idControleCarga");
    	
    	if (idControleCarga != null) {
    		sql.addCustomCriteria(" (EXISTS(SELECT M.ID_MANIFESTO FROM MANIFESTO M, MANIFESTO_ENTREGA_DOCUMENTO MED " +
				    			  "         WHERE MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " + 
				    			  "         AND M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA " +
				    			  "         AND M.ID_CONTROLE_CARGA = " + idControleCarga + ") " +
				    			  "  OR EXISTS(SELECT M.ID_MANIFESTO FROM MANIFESTO M, MANIFESTO_NACIONAL_CTO MNC " +
				    			  "			WHERE MNC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO " +
				    			  "        	AND M.ID_MANIFESTO = MNC.ID_MANIFESTO_VIAGEM_NACIONAL " +
				    			  " 		AND M.ID_CONTROLE_CARGA = " + idControleCarga + " ) " +
				    			  "  OR EXISTS(SELECT M.ID_MANIFESTO FROM MANIFESTO M, MANIFESTO_INTERNAC_CTO MIC " +
				    			  " 		WHERE MIC.ID_CTO_INTERNACIONAL = DS.ID_DOCTO_SERVICO " +
				    			  "        	AND M.ID_MANIFESTO = MIC.ID_MANIFESTO_INTERNACIONAL " +
				    			  "			AND M.ID_CONTROLE_CARGA = " + idControleCarga + " ))");
    	}
    	
    	sql.addCriteria("PS.ID_MEIO_TRANSPORTE", "=", tfm.getLong("meioTransporteRodoviario.idMeioTransporte"));
    	
    	sql.addCriteria("PS.DH_SINISTRO", ">=", tfm.getDateTime("dhSinistroInicial"));
    	sql.addCriteria("PS.DH_SINISTRO", "<=", tfm.getDateTime("dhSinistroFinal"));
    	sql.addCriteria("TSI.ID_TIPO_SINISTRO", "=", tfm.getLong("tipoSinistro.idTipoSinistro"));
    	if (tfm.getDomainValue("localSinistro") != null) {
    		sql.addCriteria("PS.TP_LOCAL_SINISTRO", "=", tfm.getDomainValue("localSinistro").getValue());
    	}
    	sql.addCriteria("TSE.ID_TIPO_SEGURO", "=", tfm.getLong("tipoSeguro.idTipoSeguro"));
    	sql.addCriteria("PS.ID_RODOVIA", "=", tfm.getLong("rodovia.idRodovia"));
    	sql.addCriteria("PS.ID_FILIAL",   "=", tfm.getLong("filialSinistro.idFilial"));
    	sql.addCriteria("PS.ID_AEROPORTO", "=", tfm.getLong("aeroporto.idAeroporto"));
    	sql.addCriteria("PS.ID_MUNICIPIO", "=", tfm.getLong("municipio.idMunicipio"));
    	sql.addCriteria("MU.ID_UNIDADE_FEDERATIVA", "=", tfm.getLong("unidadeFederativa.idUnidadeFederativa"));
    	sql.addCriteria("CLI.ID_CLIENTE", "=", tfm.getLong("cliente.idCliente"));
    	sql.addCriteria("PS.ID_USUARIO_ABERTURA", "=", tfm.getLong("usuario.idUsuario"));
    	
    	sql.addGroupBy("PS.ID_PROCESSO_SINISTRO");
    	sql.addGroupBy("PS.NR_PROCESSO_SINISTRO");
    	sql.addGroupBy("MT.NR_IDENTIFICADOR");
    	sql.addGroupBy("MT.NR_FROTA");
    	sql.addGroupBy("PS.DH_SINISTRO");
    	sql.addGroupBy(PropertyVarcharI18nProjection.createProjection("TSI.DS_TIPO_I"));
    	sql.addGroupBy("TSE.SG_TIPO");
    	sql.addGroupBy("MU.NM_MUNICIPIO");
    	sql.addGroupBy("UF.SG_UNIDADE_FEDERATIVA");
    	sql.addGroupBy("USU.NM_USUARIO");
    	
    	sql.addOrderBy("PS.NR_PROCESSO_SINISTRO");
    	
    	return sql;
    }
  
    public ResultSetPage findPaginatedCustom(FindDefinition findDef, TypedFlatMap tfm) {

    	SqlTemplate sql = getFindPaginatedQuery(tfm);
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idProcessoSinistro", Hibernate.LONG);
    			sqlQuery.addScalar("nrProcessoSinistro", Hibernate.STRING);
    			sqlQuery.addScalar("nrIdentificador", Hibernate.STRING);
    			sqlQuery.addScalar("nrFrota", Hibernate.STRING);
    			sqlQuery.addScalar("dhSinistro", Hibernate.custom(JodaTimeDateTimeUserType.class));
    			sqlQuery.addScalar("tipoSinistro",Hibernate.STRING);
    			sqlQuery.addScalar("tipoSeguro",Hibernate.STRING);
    			sqlQuery.addScalar("municipio",Hibernate.STRING);
    			sqlQuery.addScalar("uf",Hibernate.STRING);    			
    			sqlQuery.addScalar("usuarioResponsavel",Hibernate.STRING);
    		}
    	};
    	
    	return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria(), csq);
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	SqlTemplate sql = getFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().getRowCountBySql("FROM (SELECT DISTINCT PS.ID_PROCESSO_SINISTRO " + sql.getSql(false) + "\n)", sql.getCriteria());
    }
    
    /**
     * Verifica a existência de um processo de sinistro de acordo com o número informado. 
     * @param nrProcessoSinistro
     * @return True, em caso afirmativo ou false em caso negativo. 
     */
    public boolean verifyByNrProcesso(String nrProcessoSinistro) {
    	StringBuffer sb = new StringBuffer()
    	.append("  from "+ProcessoSinistro.class.getName()+" ps")
    	.append(" where ps.nrProcessoSinistro = ?");
    	Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sb.toString(), new Object[]{nrProcessoSinistro});
    	return (rowCount.intValue() > 0);
    }
    
    /**
     * Solicitação CQPRO00006044 da Integração.
     * Retorna um processo de sinistro de acordo com os números informados. 
     * @param nrProcessoSinistro
     * @return 
     */
    public ProcessoSinistro findProcessoSinistro(String nrProcessoSinistro) {
    	StringBuffer sb = new StringBuffer()
    	.append("from "+ProcessoSinistro.class.getName()+" ps ")
    	.append("where ps.nrProcessoSinistro = ? ");
    	List result =  getAdsmHibernateTemplate().find(sb.toString(), new Object[]{nrProcessoSinistro});
    	if (result.size()>0){
    		return (ProcessoSinistro)result.get(0);
    	}
    	return null;
    }
    
    /**
     * Método que retorna uma Lista de ProcessoSinistro a partir de um ID de DoctoServico.
     * 
     * @param idDoctoServico
     * @return
     */
    public List findProcessoSinistroByIdDoctoServico(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select sds.processoSinistro from ").append(SinistroDoctoServico.class.getName()).append(" sds ");
		hql.append(" join sds.doctoServico ds");
		hql.append(" where ds.id = ?");
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDoctoServico});
    }	
    
    /**
     * Find que busca apenas a entidade ProcessoSinistro (sem fetch) a partir do id
     * @param idReciboIndenizacao
     * @return
     */
    public ProcessoSinistro findProcessoSinistroById(Long idProcessoSinistro){
    	String sql = new StringBuffer()
    	.append("from "+ProcessoSinistro.class.getName()+" ps ")
    	.append("where ps.id = ?").toString();
    	Object parameters[] = {idProcessoSinistro};
    	return (ProcessoSinistro)getAdsmHibernateTemplate().findUniqueResult(sql, parameters);
    }

    public Integer getRowCountDocumentosPopup(TypedFlatMap tfm){
    	SqlTemplate sql = getDocumentosPopupQuery(tfm);
    	return getAdsmHibernateTemplate().getRowCountBySql("FROM (SELECT DISTINCT DS.ID_DOCTO_SERVICO " + sql.getSql(false) + "\n)", sql.getCriteria());
    }
    
    /**
     * LMS-6180 Retorna os registros de doctoServico com base nos filtros preenchidos
     * na popup de "Selecionar varios Documentos
     */
    public ResultSetPage findPaginatedDocumentosPopup(TypedFlatMap tfm, FindDefinition findDef){
    	
    	SqlTemplate sql = getDocumentosPopupQuery(tfm);
    	
    	sql.addProjection("DS.ID_DOCTO_SERVICO     as idDoctoServico, "
				+ "DS.TP_DOCUMENTO_SERVICO as tpDocumentoServico, "
				+ "FO.SG_FILIAL as filialOrigem, "
				+ "DS.NR_DOCTO_SERVICO as nrDoctoServico, "
				+ "FD.SG_FILIAL as filialDestino, "
				+ "TO_CHAR(DS.DH_EMISSAO,'DD/MM/YYYY HH24:MI TZH:TZM') as dhEmissao, "
				+ "DS.VL_MERCADORIA as vlMercadoria, "
				+ "DS.QT_VOLUMES as qtVolumes, "
				+ "NVL(DS.PS_AFERIDO, DS.PS_REAL) as peso, "
				+ "PR.NM_PESSOA as remetente, "
				+ "PD.NM_PESSOA as destinatario, " + "D.NM_PESSOA as devedor ");
    	
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {

    			sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
    			sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
    			sqlQuery.addScalar("filialOrigem", Hibernate.STRING);
    			sqlQuery.addScalar("nrDoctoServico",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("filialDestino",Hibernate.STRING);
    			sqlQuery.addScalar("dhEmissao",Hibernate.STRING);
    			sqlQuery.addScalar("vlMercadoria",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("qtVolumes",Hibernate.LONG);
    			sqlQuery.addScalar("peso",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("remetente",Hibernate.STRING);
    			sqlQuery.addScalar("destinatario",Hibernate.STRING);
    			sqlQuery.addScalar("devedor",Hibernate.STRING);
    			
    		}
    	};
    	
    	return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria(), csq);
    }
    
    public List findAllDocuments(TypedFlatMap tfm){
    	SqlTemplate sql = getDocumentosPopupQuery(tfm);
    	
    	sql.addProjection("DS.ID_DOCTO_SERVICO   as     idDoctoServico");
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
    		}
    	};
    	
    	return getAdsmHibernateTemplate().findBySql(sql.toString(), null, csq);
    }
    
    public SqlTemplate getDocumentosPopupQuery(TypedFlatMap tfm){
    	SqlTemplate sql = new SqlTemplate();

    	sql.addFrom("DOCTO_SERVICO", "DS");
    	sql.addFrom("FILIAL", "FO");
    	sql.addFrom("FILIAL", "FD");
    	sql.addFrom("PESSOA", "PR");
    	sql.addFrom("PESSOA", "PD");
    	sql.addFrom("DEVEDOR_DOC_SERV", "DDS");
    	sql.addFrom("PESSOA", "D");

    	sql.addCustomCriteria(new StringBuffer(" DS.ID_FILIAL_ORIGEM = FO.ID_FILIAL AND ")  
		.append(" DS.ID_FILIAL_DESTINO 			= FD.ID_FILIAL AND ")
		.append(" DS.ID_CLIENTE_REMETENTE 		= PR.ID_PESSOA AND ")
		.append(" DS.ID_CLIENTE_DESTINATARIO 	= PD.ID_PESSOA AND ")
		.append(" DS.ID_DOCTO_SERVICO 			= DDS.ID_DOCTO_SERVICO(+) AND ")
		.append(" DDS.ID_CLIENTE 				= D.ID_PESSOA(+) ").toString());

		Long idManifesto = tfm.getLong("manifesto.idManifesto");
		if (idManifesto != null) {

			sql.addCustomCriteria(" (EXISTS(SELECT MED.ID_MANIFESTO_ENTREGA FROM MANIFESTO_ENTREGA_DOCUMENTO MED "
					+ "             WHERE MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "
					+ "             AND MED.ID_MANIFESTO_ENTREGA = " + idManifesto  + ") "
					+ "      OR EXISTS(SELECT MNC.ID_MANIFESTO_VIAGEM_NACIONAL FROM MANIFESTO_NACIONAL_CTO MNC "
					+ "    WHERE MNC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO "
					+ "     AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = " + idManifesto  + ") "
					+ "      OR EXISTS(SELECT MIC.ID_MANIFESTO_INTERNACIONAL FROM MANIFESTO_INTERNAC_CTO MIC "
					+ "     WHERE MIC.ID_CTO_INTERNACIONAL = DS.ID_DOCTO_SERVICO "
					+ "    AND MIC.ID_MANIFESTO_INTERNACIONAL = " + idManifesto  + " ))");
		}

		Long idControleCarga = tfm.getLong("controleCarga.idControleCarga");
		if (idControleCarga != null) {

			sql.addCustomCriteria(" (EXISTS(SELECT M.ID_MANIFESTO FROM MANIFESTO M, MANIFESTO_ENTREGA_DOCUMENTO MED "
					+ "             WHERE MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "
					+ "             AND M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA "
					+ "             AND M.ID_CONTROLE_CARGA = " + idControleCarga + ") "
					+ "      OR EXISTS(SELECT M.ID_MANIFESTO FROM MANIFESTO M, MANIFESTO_NACIONAL_CTO MNC "
					+ "    WHERE MNC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO "
					+ "              AND M.ID_MANIFESTO = MNC.ID_MANIFESTO_VIAGEM_NACIONAL "
					+ "     AND M.ID_CONTROLE_CARGA = " + idControleCarga + ") "
					+ "      OR EXISTS(SELECT M.ID_MANIFESTO FROM MANIFESTO M, MANIFESTO_INTERNAC_CTO MIC "
					+ "     WHERE MIC.ID_CTO_INTERNACIONAL = DS.ID_DOCTO_SERVICO "
					+ "              AND M.ID_MANIFESTO = MIC.ID_MANIFESTO_INTERNACIONAL "
					+ "    AND M.ID_CONTROLE_CARGA = " + idControleCarga + " ))");
		}

		Long idDoctoServico = tfm.getLong("doctoServico.idDoctoServico");
		if (idDoctoServico != null) {
			sql.addCustomCriteria("DS.ID_DOCTO_SERVICO = " + idDoctoServico);
		}
    	
		return sql;
    }
   
    //LMS-6178
	public List findSomaValoresMercadoria(Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SUM(DS.vlMercadoria)");
		sql.addInnerJoin(SinistroDoctoServico.class.getName(), "SDS");
		sql.addInnerJoin("SDS.doctoServico", "DS");
		sql.addCriteria("SDS.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}

    //LMS-6178
	public List findSomaValoresIndenizado(Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SUM(DSI.vlIndenizado)");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addFrom(DoctoServicoIndenizacao.class.getName(), "DSI");
		sql.addFrom(ReciboIndenizacao.class.getName(),"RI");
		sql.addCustomCriteria("RI = DSI.reciboIndenizacao");
		sql.addCustomCriteria("SDS.doctoServico = DSI.doctoServico");
		sql.addCriteria("RI.tpStatusIndenizacao", "!=", "C");
		sql.addCriteria("SDS.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}
	
	/**
	 * LMS-6144 - Retorna uma lista com os documentos de sinistro_docto_servico
	 * que não tenham prejuizo com base no processo de sinisto informado
	 */
	public List findIdsSinistroDoctoServicoByIdProcessoSinistroComPrejuizo(TypedFlatMap tfm){
		SqlTemplate sql = new SqlTemplate();
		  
		sql.addProjection("SDS.idSinistroDoctoServico");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addCriteria("SDS.processoSinistro.idProcessoSinistro", "=", tfm.getLong("idProcessoSinistro"));
		sql.addCriteria("SDS.tpPrejuizo", "<>", "S");
		  
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}
	
	/**
	 * Traz os dados para monitoramento dos processos de sinistro
	 * 
	 * Jira LMS-6159
	 * 
	 * @param colunsOrderBy colunas para ordenação da query
	 * @return
	 */
	public List<Object[]> findMonitoramentoProcessoSinistro(String[] colunsOrderBy) {
		
		final StringBuilder sql = new StringBuilder()
		.append("SELECT * ")
		.append("  FROM (SELECT t.*, ")
		.append("               CASE ")
		.append("                 WHEN t.vl_prejuizo > 0 ")
		.append("                      AND t.dt_rim_sem_pagto IS NOT NULL ")
		.append("                      AND ")
		.append("                      ((t.dt_retificacao IS NOT NULL AND ")
		.append("                      (t.dt_retificacao + nvl((SELECT pg.ds_conteudo ")
		.append("                                                  FROM parametro_geral pg ")
		.append("                                                 WHERE pg.nm_parametro_geral = ")
		.append("                                                       'NR_PRAZO_PAGTO_RIM'), ")
		.append("                                                55)) <= trunc(SYSDATE)) OR ")
		.append("                      ((t.dt_rim_sem_pagto + nvl((SELECT pg.ds_conteudo ")
		.append("                                                    FROM parametro_geral pg ")
		.append("                                                   WHERE pg.nm_parametro_geral = ")
		.append("                                                         'NR_PRAZO_PAGTO_RIM'), ")
		.append("                                                  55)) <= trunc(SYSDATE))) THEN ")
		.append("                    CASE ")
		.append("                      WHEN (t.cto_sem_rim = 'S' AND ")
		.append("                           (nvl(t.dt_filial_rim, t.dt_abertura) + nvl((SELECT pg.ds_conteudo ")
		.append("                       FROM parametro_geral pg ")
		.append("                      WHERE pg.nm_parametro_geral = 'NR_PRAZO_EMISSAO_RIM'),10)) <= trunc(SYSDATE)) THEN ")
		.append("                       nvl((select rm.texto from recursos_mensagens rm where rm.chave = 'LMS-22043' and rm.idioma = 'pt_BR'),'01 - Pagamento expirado e pendente de abertura de RIM') ")
		.append("                      ELSE ")
		.append("                       nvl((select rm.texto from recursos_mensagens rm where rm.chave = 'LMS-22044' and rm.idioma = 'pt_BR'),'02 - Pagamento expirado') ")
		.append("                    END ")
		.append("                 WHEN t.vl_prejuizo > 0 ")
		.append("                      AND t.cto_sem_rim = 'S' ")
		.append("                      AND (nvl(t.dt_filial_rim, t.dt_abertura) + ")
		.append("                      nvl((SELECT pg.ds_conteudo ")
		.append("                                 FROM parametro_geral pg ")
		.append("                                WHERE pg.nm_parametro_geral = ")
		.append("                                      'NR_PRAZO_EMISSAO_RIM'), ")
		.append("                               10)) <= trunc(SYSDATE) THEN ")
		.append("                  CASE ")
		.append("                    WHEN t.dt_filial_rim IS NULL THEN ")
		.append("                     nvl((select rm.texto from recursos_mensagens rm where rm.chave = 'LMS-22045' and rm.idioma = 'pt_BR'),'03 - Pendente de abertura de RIM e de comunicado para abertura de RIM') ")
		.append("                    ELSE ")
		.append("                     nvl((select rm.texto from recursos_mensagens rm where rm.chave = 'LMS-22046' and rm.idioma = 'pt_BR'),'04 - Pendente de abertura de RIM') ")
		.append("                  END ")
		.append("                 WHEN t.dt_retificacao IS NOT NULL ")
		.append("                      AND t.vl_prejuizo > 0 ")
		.append("                      AND t.dt_filial_rim IS NULL ")
		.append("                      AND (t.dt_retificacao + nvl((SELECT pg.ds_conteudo ")
		.append("                                                    FROM parametro_geral pg ")
		.append("                                                   WHERE pg.nm_parametro_geral = ")
		.append("                                                         'NR_PRAZO_COMUNIC_RIM'), ")
		.append("                                                  1)) <= trunc(SYSDATE) THEN ")
		.append("                  nvl((select rm.texto from recursos_mensagens rm where rm.chave = 'LMS-22047' and rm.idioma = 'pt_BR'),'05 - Pendente de comunicado para abertura de RIM') ")
		.append("                 WHEN t.dt_abertura IS NOT NULL ")
		.append("                      AND t.dt_retificacao IS NULL ")
		.append("                      AND (t.dt_abertura + nvl((SELECT pg.ds_conteudo ")
		.append("                                                 FROM parametro_geral pg ")
		.append("                                                WHERE pg.nm_parametro_geral = ")
		.append("                                                      'NR_PRAZO_CONFIRM_PREJUIZO'), ")
		.append("                                               10)) <= trunc(SYSDATE) THEN ")
		.append("                  nvl((select rm.texto from recursos_mensagens rm where rm.chave = 'LMS-22048' and rm.idioma = 'pt_BR'),'06 - Pendente de confirmação dos prejuízos') ")
		.append("                 WHEN t.dt_abertura IS NOT NULL ")
		.append("                      AND t.dt_ocorrencia IS NULL ")
		.append("                      AND (t.dt_abertura + nvl((SELECT pg.ds_conteudo ")
		.append("                                                 FROM parametro_geral pg ")
		.append("                                                WHERE pg.nm_parametro_geral = ")
		.append("                                                      'NR_PRAZO_COMUNIC_CLIENTE'), ")
		.append("                                               1)) <= trunc(SYSDATE) THEN ")
		.append("                  nvl((select rm.texto from recursos_mensagens rm where rm.chave = 'LMS-22049' and rm.idioma = 'pt_BR'),'07 - Pendente de comunicado para o cliente') ")
		.append("               END tp_situacao ")
		.append("          FROM (SELECT ps.id_processo_sinistro, ")
		.append("                       ps.nr_processo_sinistro, ")
		.append("                       us.id_usuario, ")
		.append("                       us.login, ")
		.append("                       us.ds_email, ")
		.append("                       (SELECT count(*) ")
		.append("                             FROM sinistro_docto_servico sds ")
		.append("                            WHERE sds.id_processo_sinistro = ")
		.append("                                  ps.id_processo_sinistro) qt_documentos, ")
		.append("                       nvl((SELECT SUM(ds.vl_mercadoria) ")
		.append("                             FROM sinistro_docto_servico sds, docto_servico ds ")
		.append("                            WHERE sds.id_processo_sinistro = ")
		.append("                                  ps.id_processo_sinistro ")
		.append("                              AND sds.id_docto_servico = ds.id_docto_servico), ")
		.append("                           0) vl_mercadoria, ")
		.append("                       nvl((SELECT SUM(sds.vl_prejuizo) ")
		.append("                             FROM sinistro_docto_servico sds ")
		.append("                            WHERE sds.id_processo_sinistro = ")
		.append("                                  ps.id_processo_sinistro ")
		.append("                              AND sds.tp_prejuizo IN ('P', 'T')), ")
		.append("                           0) vl_prejuizo, ")
		.append("                       trunc(CAST(ps.dh_abertura AS DATE)) dt_abertura, ")
		.append("                       (SELECT trunc(CAST(nvl(sds.dh_envio_email_ocorrencia, ")
		.append("                                              sds.dh_geracao_carta_ocorrencia) AS DATE)) ")
		.append("                          FROM sinistro_docto_servico sds ")
		.append("                         WHERE sds.id_processo_sinistro = ")
		.append("                               ps.id_processo_sinistro ")
		.append("                           AND (sds.dh_envio_email_ocorrencia IS NOT NULL OR ")
		.append("                               sds.dh_geracao_carta_ocorrencia IS NOT NULL) ")
		.append("                           AND rownum = 1) dt_ocorrencia, ")
		.append("                       (SELECT trunc(CAST(nvl(sds.dh_envio_email_retificacao, ")
		.append("                                              sds.dh_geracao_carta_retificacao) AS DATE)) ")
		.append("                          FROM sinistro_docto_servico sds ")
		.append("                         WHERE sds.id_processo_sinistro = ")
		.append("                               ps.id_processo_sinistro ")
		.append("                           AND (sds.dh_envio_email_retificacao IS NOT NULL OR ")
		.append("                               sds.dh_geracao_carta_retificacao IS NOT NULL) ")
		.append("                           AND rownum = 1) dt_retificacao, ")
		.append("                       (SELECT trunc(CAST(nvl(sds.dh_envio_email_filial_rim, ")
		.append("                                              sds.dh_geracao_filial_rim) AS DATE)) ")
		.append("                          FROM sinistro_docto_servico sds ")
		.append("                         WHERE sds.id_processo_sinistro = ")
		.append("                               ps.id_processo_sinistro ")
		.append("                           AND (sds.dh_envio_email_filial_rim IS NOT NULL OR ")
		.append("                               sds.dh_geracao_filial_rim IS NOT NULL) ")
		.append("                           AND rownum = 1) dt_filial_rim, ")
		.append("                       nvl((SELECT 'S' ")
		.append("                          FROM sinistro_docto_servico sds ")
		.append("                         WHERE sds.id_processo_sinistro = ")
		.append("                               ps.id_processo_sinistro ")
		.append("                           AND sds.tp_prejuizo IN ('P', 'T') ")
		.append("                           AND sds.vl_prejuizo > 0 ")
		.append("                           AND NOT EXISTS (SELECT 1 ")
		.append("                                  FROM docto_servico_indenizacao dsi, ")
		.append("                                       recibo_indenizacao        ri ")
		.append("                                 WHERE dsi.id_recibo_indenizacao = ")
		.append("                                       ri.id_recibo_indenizacao ")
		.append("                                   AND dsi.id_docto_servico = ")
		.append("                                       sds.id_docto_servico ")
		.append("                                   AND ri.tp_status_indenizacao <> 'C' ")
		.append("                                   AND rownum = 1) ")
		.append("                           AND rownum = 1), 'N') cto_sem_rim, ")
		.append("                       (SELECT MIN(ri.dt_emissao) ")
		.append("                          FROM sinistro_docto_servico    sds, ")
		.append("                               docto_servico_indenizacao dsi, ")
		.append("                               recibo_indenizacao        ri ")
		.append("                         WHERE sds.id_processo_sinistro = ")
		.append("                               ps.id_processo_sinistro ")
		.append("                           AND dsi.id_recibo_indenizacao = ")
		.append("                               ri.id_recibo_indenizacao ")
		.append("                           AND dsi.id_docto_servico = sds.id_docto_servico ")
		.append("                           AND ri.tp_status_indenizacao NOT IN ('C', 'P')) dt_rim_sem_pagto ")
		.append("                  FROM processo_sinistro ps, ")
		.append("                       usuario           us ")
		.append("                 WHERE ps.id_usuario_abertura = us.id_usuario(+) ")
		.append("                   AND ps.dh_fechamento IS NULL) t ")
		.append("        WHERE t.qt_documentos > 0) t2 ")
		.append(" WHERE t2.tp_situacao IS NOT NULL ")
		.append(" ORDER BY ").append(StringUtils.join(colunsOrderBy, ","));
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_processo_sinistro",Hibernate.LONG);
				sqlQuery.addScalar("nr_processo_sinistro",Hibernate.STRING);
				sqlQuery.addScalar("id_usuario",Hibernate.LONG);
				sqlQuery.addScalar("login",Hibernate.STRING);
				sqlQuery.addScalar("ds_email",Hibernate.STRING);
				sqlQuery.addScalar("qt_documentos",Hibernate.LONG);
				sqlQuery.addScalar("vl_mercadoria",Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_prejuizo",Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dt_abertura",Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dt_ocorrencia",Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dt_retificacao",Hibernate.TIMESTAMP);
				sqlQuery.addScalar("dt_filial_rim",Hibernate.TIMESTAMP);
				sqlQuery.addScalar("cto_sem_rim",Hibernate.STRING);
				sqlQuery.addScalar("dt_rim_sem_pagto",Hibernate.TIMESTAMP);
				sqlQuery.addScalar("tp_situacao",Hibernate.STRING);
			}
		};

    	final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
				return query.list();
			}
		};

		return getHibernateTemplate().executeFind(hcb);
	}
}