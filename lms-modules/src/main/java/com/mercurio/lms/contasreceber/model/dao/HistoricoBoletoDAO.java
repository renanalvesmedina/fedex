package com.mercurio.lms.contasreceber.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HistoricoBoletoDAO extends BaseCrudDao<HistoricoBoleto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return HistoricoBoleto.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("boleto", FetchMode.JOIN);
    	lazyFindById.put("boleto.fatura", FetchMode.JOIN);
		lazyFindById.put("ocorrenciaBanco", FetchMode.JOIN);
		lazyFindById.put("boleto.cedente", FetchMode.JOIN);
		lazyFindById.put("boleto.cedente.agenciaBancaria", FetchMode.JOIN);
		lazyFindById.put("boleto.cedente.agenciaBancaria.banco", FetchMode.JOIN);
		lazyFindById.put("boleto.fatura.filialByIdFilial", FetchMode.JOIN);
		lazyFindById.put("boleto.fatura.filialByIdFilial.pessoa", FetchMode.JOIN);
		lazyFindById.put("boleto.fatura.cliente", FetchMode.JOIN);
		lazyFindById.put("boleto.fatura.cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("boleto.fatura.cliente.pessoa.enderecoPessoa", FetchMode.JOIN);
		lazyFindById.put("boleto.fatura.cliente.pessoa.enderecoPessoa.tipoLogradouro", FetchMode.JOIN);
		lazyFindById.put("boleto.fatura.cliente.pessoa.enderecoPessoa.municipio", FetchMode.JOIN);
		lazyFindById.put("boleto.fatura.cliente.pessoa.enderecoPessoa.municipio.unidadeFederativa", FetchMode.JOIN);
		
    	super.initFindByIdLazyProperties(lazyFindById);
    }

   
    
    /**Consulta  que retorna dados do Histórico das Movimentações de um boleto. 
     * Usado na tela:
     * Consultar Histórico das Ocorrências do Boleto
     * 
     *@author Diego Umpierre - LMS
     *@see com.mercurio.lms.contasreceber.action.ConsultarHistoricoOcorrenciasBoletoAction
     *
     *@param idBoleto identificador do boleto, findDef FindDefinition
     *
     *@return ResultSetPage com o resultado da consulta de acordo com os parametros.
     */
    public ResultSetPage findPaginatedHistMov(Long idBoleto,FindDefinition findDef) {
   
    	/* chama o metodo que monta os joins */
    	SqlTemplate sql = montaHqlJoinHistMov();
 
    	/* Projection */
    	sql.addProjection("histBoleto");
		
    	/* Critérios */
    	sql.addCriteria(" boleto.idBoleto ","=",idBoleto); 
		
    	/* Ordenação */
    	sql.addOrderBy("ocorrenciaBanco.tpOcorrenciaBanco");
    	sql.addOrderBy("histBoleto.dhOcorrencia.value");

        return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

    
    /**
     * Faz a contagem de tuplas da consulta findPaginatedHistMov
     * 
     * @param idBoleto
     * @return
     * @author Diego Umpierre - LMS
     * @see com.mercurio.lms.contasreceber.model.service.ConsultarHistoricoOcorrenciasBoletoService
     */
	public Integer getRowCountHistMov(Long idBoleto) {
		/* Chama o metodo que monta os joins */
		SqlTemplate sql = montaHqlJoinHistMov();
    	   
    	/* Critérios */
    	sql.addCriteria(" boleto.idBoleto ","=",idBoleto);
		List lstRegistros = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		return Integer.valueOf(lstRegistros.size());
	}
    
	/**
	 * Monta os joins necessarios para findPaginatedHistMov
	 * @return
	 */
	private SqlTemplate montaHqlJoinHistMov(){
		
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addInnerJoin(getPersistentClass().getName(), "histBoleto");
    	sql.addInnerJoin(" fetch histBoleto.ocorrenciaBanco", "ocorrenciaBanco");
    	sql.addInnerJoin(" fetch histBoleto.usuario", "usuario");
    	sql.addInnerJoin(" fetch histBoleto.boleto", "boleto");
    	sql.addInnerJoin(" fetch boleto.fatura", "fatura");
    	sql.addInnerJoin(" fetch fatura.itemFaturas", "itenFaturas");
    	sql.addInnerJoin(" fetch itenFaturas.devedorDocServFat", "devedorDocServFat");
    	sql.addInnerJoin(" fetch devedorDocServFat.doctoServico", "doctoServico");
   
    	return sql;
	}
	
    /**
     * Retorna a lista de HistoricoBoleto por boleto
     * 
     * @author Mickaël Jalbert
     * @since 25/04/2006
     * 
     * @param Long idBoleto
     * @return List
     * */	
	public List findByBoleto(Long idBoleto){
		SqlTemplate hql = mountHql(idBoleto);
		
		hql.addProjection("histBoleto");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	/**
     * Retorna a lista de HistoricoBoleto por boleto de acordo com a situação passada
     * 
     * @author Mickaël Jalbert
     * @since 02/03/2007
     * 
     * @param Long idBoleto
     * @return List
     * */	
	public List findByBoleto(Long idBoleto, String tpSituacao){
		
		SqlTemplate hql = mountHql(idBoleto);
		
		hql.addProjection("histBoleto");
		hql.addCriteria("histBoleto.tpSituacaoHistoricoBoleto", "=", tpSituacao);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	/**
	 * Monta os joins necessarios para findByBoleto
	 */
	private SqlTemplate mountHql(Long idBoleto){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addInnerJoin(HistoricoBoleto.class.getName(), "histBoleto");
    	hql.addInnerJoin("histBoleto.boleto", "boleto");
    	
    	hql.addCriteria("boleto.id", "=", idBoleto);
    	
    	return hql;
	}	
	
	/**
	 * Carrega históricos boleto de acordo com o nrOcorrenciaBanco, idBoleto e a situação do histórico.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 10/08/2007
	 *
	 * @param idBoleto
	 * @param tpSituacao
	 * @param nrOcorrenciaBanco
	 * @return
	 *
	 */
	public List<HistoricoBoleto> findHistoricosByBoletoAndOcorrencia(Long idBoleto, Short nrOcorrenciaBanco, String tpSituacao){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection(" hb ");
		hql.addFrom(getPersistentClass().getName(), " hb ");
		
		hql.addCriteria("hb.boleto.id", "=", idBoleto);
		hql.addCriteria("hb.ocorrenciaBanco.nrOcorrenciaBanco", "=", nrOcorrenciaBanco);
		hql.addCriteria("hb.tpSituacaoHistoricoBoleto", "=", tpSituacao);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Valida se existe um historico de abatimento com dhOcorrencia maior que a
	 * dhOcorrencia do último historico de cancelamento de abatimento.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 20/08/2007
	 *
	 * @param idBoleto
	 * @return
	 *
	 */
	public String validateGerarHistoricoCanlamentoAbatimento(Long idBoleto){
    	
    	SqlTemplate hql = new SqlTemplate();
    	SqlTemplate subHql = new SqlTemplate();
    	
    	hql.addProjection(" (CASE " +
    					  " 	WHEN dtOcorrencia.dtOcorrenciaQuatro  IS NULL " +
    					  "		  AND dtOcorrencia.dtOcorrenciaCinco IS NULL " +
    					  "		  AND " +
    					  "			(" +
    					  "				SELECT    SUM(d.vl_desconto) " +
    					  "				FROM      boleto b, fatura f, item_fatura if, devedor_doc_serv_fat ddsf, desconto d " +
    					  " 			WHERE     b.id_fatura = f.id_fatura " +
    					  "				AND       f.id_fatura = if.id_fatura " +
    					  "				AND       if.id_devedor_doc_serv_fat = ddsf.id_devedor_doc_serv_fat " +
    					  "				AND       ddsf.id_devedor_doc_serv_fat = d.id_devedor_doc_serv_fat " +
    					  "				AND       d.tp_situacao_aprovacao = 'A' " +
    					  "				AND       b.id_boleto = ?" +
    					  "			) > 0 " + 		
    					  " 	THEN  'S' " +
    					  " 	WHEN dtOcorrencia.dtOcorrenciaQuatro  IS NULL " +
    					  " 	THEN  'N' " +
    					  " 	WHEN dtOcorrencia.dtOcorrenciaCinco IS NULL " +
    					  " 	THEN  'S' " +
    					  " 	WHEN dtOcorrencia.dtOcorrenciaQuatro  > dtOcorrencia.dtOcorrenciaCinco " +
    					  " 	THEN  'S' " +
    					  " 	WHEN dtOcorrencia.dtOcorrenciaQuatro  < dtOcorrencia.dtOcorrenciaCinco " +
    					  " 	THEN  'N' " +
      		  			  " END) as geraCancelamentoAbatimento ");
		
		subHql.addProjection(" (SELECT      MAX(hb.dh_ocorrencia) " + 
		                 	 " FROM         historico_boleto hb, " +
		                 	 "				ocorrencia_banco ob" +
		                 	 " WHERE        ob.nr_ocorrencia_banco = 4 " +
		                 	 " AND 			hb.id_ocorrencia_banco = ob.id_ocorrencia_banco" +			
		                 	 " AND          hb.tp_situacao_historico_boleto = 'T' " +
		                 	 " AND          hb.id_boleto = ?) as dtOcorrenciaQuatro, " +
		                 	 " (SELECT      MAX(hb.dh_ocorrencia) " +
		                 	 " FROM         historico_boleto hb, " +
		                 	 "				ocorrencia_banco ob" +
		                 	 " WHERE        hb.id_ocorrencia_banco = ob.id_ocorrencia_banco " +
		                 	 " AND 			hb.id_ocorrencia_banco = ob.id_ocorrencia_banco" +
		                 	 " AND          ob.nr_ocorrencia_banco = 5 " + 
		                 	 " AND          hb.tp_situacao_historico_boleto <> 'C' " + 
		                   	"  AND          hb.id_boleto = ?) as dtOcorrenciaCinco ");
		
		subHql.addFrom("DUAL");
		subHql.addCriteriaValue(idBoleto);
		subHql.addCriteriaValue(idBoleto);
		subHql.addCriteriaValue(idBoleto);
		
		hql.addFrom("( " + subHql.getSql() + " )", "dtOcorrencia");
		
		return (String)getAdsmHibernateTemplate().findByIdBySql(hql.getSql(), subHql.getCriteria(), 
				new ConfigureSqlQuery(){

					public void configQuery(SQLQuery sqlQuery) {
						 sqlQuery.addScalar("geraCancelamentoAbatimento", Hibernate.STRING);
					}
				
				});
    }
	
	public void flush() {
		getAdsmHibernateTemplate().flush();
	}

	public HistoricoBoleto findByIdPendencia(Long idPendencia) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(getPersistentClass().getName(), " hb ");
		
		hql.addCriteria("hb.idPendencia", "=", idPendencia);
		
		return (HistoricoBoleto) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		
	}

	public HistoricoBoleto findLastHistoricoBoletoWithPendencia(Boleto boleto) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(getPersistentClass().getName(), " hb ");
		
		hql.addCriteria("hb.boleto.id", "=", boleto.getIdBoleto());
		hql.addCustomCriteria("hb.idPendencia != null");
		
		hql.addOrderBy("hb.dhOcorrencia", "ASC");
		
		List<HistoricoBoleto> list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (list != null && list.size() > 0) {
			return  list.get(list.size() -1);
		}
		
		return null;
	}
	
	public List<Map<String, Object>> findHistoricoBoletoByDhOcorrencias(Date dhOcorrencia, Long idCedente) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT enp.ID_HISTORICO_BOLETO         AS ID_HISTORICO_BOLETO, ");
		sql.append("	  enp.ID_BOLETO                    AS ID_BOLETO, ");
		sql.append("      enp.NR_OCORRENCIA_BANCO          AS NR_OCORRENCIA_BANCO, ");
		sql.append("      enp.NR_FATURA                    AS NR_FATURA, ");
		sql.append("      enp.NR_BOLETO                    AS NR_BOLETO, ");
		sql.append("      enp.TP_SITUACAO_BOLETO           AS TP_SITUACAO_BOLETO, ");
		sql.append("      enp.DT_EMISSAO                   AS DT_EMISSAO, ");
		sql.append("      enp.DT_VENCIMENTO                AS DT_VENCIMENTO, ");
		sql.append("      enp.VL_TOTAL                     AS VL_TOTAL, ");
		sql.append("      enp.VL_JUROS_DIA                 AS VL_JUROS_DIA, ");
		sql.append("      enp.VL_DESCONTO                  AS VL_DESCONTO, ");
		sql.append("      enp.TP_PESSOA                    AS TP_PESSOA_CLIENTE, ");
		sql.append("      enp.NR_IDENTIFICACAO             AS NR_IDENTIFICACAO, ");
		sql.append(removeTabSpace("enp.NM_PESSOA", "NM_PESSOA"));
		sql.append("      enp.ID_PESSOA                    AS ID_PESSOA, ");
		sql.append("      ce.NR_CONTA_CORRENTE             AS NR_CONTA_CORRENTE, ");
		sql.append("      ce.DS_NOME_ARQUIVO_COBRANCA      AS DS_NOME_ARQUIVO_COBRANCA, ");
		sql.append("      ab.NR_AGENCIA_BANCARIA           AS NR_AGENCIA_BANCARIA, ");
		sql.append("      ab.NR_DIGITO                     AS NR_DIGITO, ");
		sql.append("      ba.NR_BANCO                      AS NR_BANCO, ");
		sql.append("      fi.SG_FILIAL                     AS SG_FILIAL, ");
		sql.append("      pef.NR_IDENTIFICACAO             AS NR_IDENTIFICACAO_FILIAL, ");
		sql.append(removeTabSpace("enp.nr_cep", "NR_CEP"));
		sql.append(removeTabSpace("enp.DS_BAIRRO", "DS_BAIRRO"));
		sql.append(removeTabSpace("enp.NM_MUNICIPIO", "NM_MUNICIPIO"));
		sql.append("      enp.SG_UNIDADE_FEDERATIVA        AS SG_UNIDADE_FEDERATIVA, ");
		sql.append(removeTabSpace("enp.DS_COMPLEMENTO", "DS_COMPLEMENTO"));
		sql.append(removeTabSpace("enp.DS_ENDERECO", "DS_ENDERECO"));
		sql.append(removeTabSpace("enp.NR_ENDERECO", "NR_ENDERECO"));
		sql.append("      VI18N(enp.DS_TIPO_LOGRADOURO_I)  AS DS_TIPO_LOGRADOURO, ");
		
		sql.append("      enp.ID_ENDERECO                  AS ID_ENDERECO, ");
		sql.append("      enp.ID_ENDERECO_DEF              AS ID_ENDERECO_DEF, ");
		sql.append(removeTabSpace("enp.NR_CEP_DEF", "NR_CEP_DEF"));
		sql.append(removeTabSpace("enp.DS_BAIRRO_DEF", "DS_BAIRRO_DEF"));
		sql.append(removeTabSpace("enp.NM_MUNICIPIO_DEF", "NM_MUNICIPIO_DEF"));
		sql.append("      enp.SG_UNIDADE_FEDERATIVA_DEF    AS SG_UNIDADE_FEDERATIVA_DEF, ");
		sql.append(removeTabSpace("enp.DS_COMPLEMENTO_DEF", "DS_COMPLEMENTO_DEF"));
		sql.append(removeTabSpace("enp.DS_ENDERECO_DEF", "DS_ENDERECO_DEF"));
		sql.append(removeTabSpace("enp.NR_ENDERECO_DEF", "NR_ENDERECO_DEF"));
		sql.append("      VI18N(enp.DS_TIPO_LOGRADOURO_I_DEF)  AS DS_TIPO_LOGRADOURO_DEF, ");
		sql.append("      ce.SQ_COBRANCA                       AS SQ_COBRANCA, ");
		sql.append("      ce.NR_CARTEIRA                       AS NR_CARTEIRA ");
				
		sql.append("  FROM ");
		sql.append("      cedente ce, ");
		sql.append("      AGENCIA_BANCARIA ab, ");
		sql.append("      banco ba, ");
		sql.append("      filial fi, ");
		sql.append("      pessoa pef, ");
		sql.append(" ");
		sql.append("      (SELECT hb.ID_HISTORICO_BOLETO, ");
		sql.append("          ob.NR_OCORRENCIA_BANCO, ");
		sql.append("          fa.NR_FATURA, ");
		sql.append("          fa.ID_FILIAL, ");
		sql.append("          bo.ID_BOLETO, ");
		sql.append("          bo.NR_BOLETO, ");
		sql.append("          bo.DT_EMISSAO, ");
		sql.append("          bo.DT_VENCIMENTO, ");
		sql.append("          bo.VL_TOTAL, ");
		sql.append("          bo.VL_JUROS_DIA, ");
		sql.append("          bo.VL_DESCONTO, ");
		sql.append("          bo.ID_CEDENTE, ");
		sql.append("          bo.TP_SITUACAO_BOLETO, ");
		sql.append("          pc.TP_PESSOA, ");
		sql.append("          pc.NR_IDENTIFICACAO, ");
		sql.append("          pc.NM_PESSOA, ");
		sql.append("          pc.ID_PESSOA, ");
		sql.append("          ep.nr_cep, ");
		sql.append("          ep.DS_BAIRRO, ");
		sql.append("          ep.DS_ENDERECO, ");
		sql.append("          ep.NR_ENDERECO, ");
		sql.append("          ep.DS_COMPLEMENTO, ");
		sql.append("          mu.NM_MUNICIPIO, ");
		sql.append("          uf.SG_UNIDADE_FEDERATIVA, ");
		sql.append("          tl.DS_TIPO_LOGRADOURO_I, ");
		
		sql.append("          ep.id_endereco_pessoa as ID_ENDERECO, ");
        sql.append("          epd.id_endereco_pessoa as ID_ENDERECO_DEF, ");
        sql.append("          epd.nr_cep as NR_CEP_DEF, ");
        sql.append("          epd.DS_BAIRRO as DS_BAIRRO_DEF, ");
        sql.append("          epd.DS_ENDERECO as DS_ENDERECO_DEF, ");
        sql.append("          epd.NR_ENDERECO as NR_ENDERECO_DEF, ");
        sql.append("          epd.DS_COMPLEMENTO as DS_COMPLEMENTO_DEF, ");
        sql.append("          mud.NM_MUNICIPIO as NM_MUNICIPIO_DEF, ");
        sql.append("          ufd.SG_UNIDADE_FEDERATIVA as SG_UNIDADE_FEDERATIVA_DEF, ");
        sql.append("          tld.DS_TIPO_LOGRADOURO_I as DS_TIPO_LOGRADOURO_I_DEF, ");
		
		sql.append("          hb.DH_OCORRENCIA, ");
		sql.append("          EP.DT_VIGENCIA_INICIAL ");
		sql.append("      FROM historico_boleto hb, ");
		sql.append("        OCORRENCIA_BANCO ob, ");
		sql.append("        boleto bo , ");
		sql.append("        fatura fa , ");
		sql.append("        pessoa pc , ");
		sql.append("        municipio mu, ");
		sql.append("        unidade_federativa uf, ");
		sql.append("        TIPO_LOGRADOURO tl, ");
		
		sql.append("        ENDERECO_PESSOA epd, ");
		sql.append("        municipio mud, ");
		sql.append("        unidade_federativa ufd, ");
		sql.append("        TIPO_LOGRADOURO tld, ");
		
		sql.append("        (SELECT en.* ");
		sql.append("        FROM ENDERECO_PESSOA en, ");
		sql.append("          tipo_endereco_pessoa tep ");
		sql.append("        WHERE en.id_endereco_pessoa = tep.id_endereco_pessoa ");
		sql.append("        AND tep.tp_endereco         = 'COB' ");
		sql.append("        ) ep ");
		sql.append("      WHERE NVL(hb.TP_SITUACAO_APROVACAO,'A') = 'A' ");
		sql.append("      AND SYS_EXTRACT_UTC(\"DH_OCORRENCIA\")   >= :dhOcorrencia");
		sql.append("      AND ob.TP_OCORRENCIA_BANCO              ='REM' ");
		sql.append("      AND hb.id_ocorrencia_banco              = ob.id_ocorrencia_banco ");
		sql.append("      AND hb.TP_SITUACAO_HISTORICO_BOLETO     = 'A' ");
		sql.append("      AND hb.id_boleto                        = bo.id_boleto ");
		sql.append("      AND fa.id_fatura                        = bo.id_fatura ");
		sql.append("      AND pc.id_pessoa                        = fa.id_cliente ");
		sql.append("      AND pc.id_pessoa                        = ep.id_pessoa(+) ");
		sql.append("      AND ep.ID_MUNICIPIO                     = mu.ID_MUNICIPIO(+) ");
		sql.append("      AND mu.ID_UNIDADE_FEDERATIVA            = uf.ID_UNIDADE_FEDERATIVA(+) ");
		sql.append("      AND ep.ID_TIPO_LOGRADOURO               = tl.ID_TIPO_LOGRADOURO(+) ");
		
		sql.append("      AND pc.id_endereco_pessoa               = epd.id_endereco_pessoa ");
		sql.append("      AND epd.ID_MUNICIPIO                    = mud.ID_MUNICIPIO ");
		sql.append("      AND mud.ID_UNIDADE_FEDERATIVA           = ufd.ID_UNIDADE_FEDERATIVA ");
		sql.append("      AND epd.ID_TIPO_LOGRADOURO              = tld.ID_TIPO_LOGRADOURO ");

		sql.append("      )  enp ");
		sql.append(" ");
		sql.append("where ce.ID_CEDENTE = enp.ID_CEDENTE ");
		sql.append("AND ce.ID_CEDENTE = :idCedente ");
		sql.append("AND ce.ID_AGENCIA_BANCARIA = ab.ID_AGENCIA_BANCARIA ");
		sql.append("AND ab.ID_BANCO = ba.ID_BANCO ");
		sql.append("AND enp.ID_FILIAL =fi.ID_FILIAL ");
		sql.append("AND pef.ID_PESSOA = fi.ID_FILIAL ");
		sql.append(" ");
		sql.append("AND ( enp.DT_VIGENCIA_INICIAL is null or enp.DT_VIGENCIA_INICIAL = ( ");
		sql.append("SELECT min(en.DT_VIGENCIA_INICIAL) ");
		sql.append("        FROM ENDERECO_PESSOA en, ");
		sql.append("          tipo_endereco_pessoa tep ");
		sql.append("        WHERE en.id_endereco_pessoa = tep.id_endereco_pessoa ");
		sql.append("        AND tep.tp_endereco         = 'COB' ");
		sql.append("        AND en.id_pessoa  = enp.ID_PESSOA ");
		sql.append("        )) ");
		sql.append(" ");
		sql.append("AND enp.DH_OCORRENCIA   = ");
		sql.append("  (SELECT MIN(hbi.DH_OCORRENCIA) ");
		sql.append("  FROM HISTORICO_BOLETO hbi, ");
		sql.append("    OCORRENCIA_BANCO oc2 ");
		sql.append("  WHERE hbi.ID_OCORRENCIA_BANCO       =oc2.ID_OCORRENCIA_BANCO ");
		sql.append("  AND enp.ID_BOLETO          =hbi.ID_BOLETO ");
		sql.append("  AND oc2.TP_OCORRENCIA_BANCO         ='REM' ");
		sql.append("  AND SYS_EXTRACT_UTC(\"DH_OCORRENCIA\")   >= :dhOcorrencia");
		sql.append("  AND NVL(hbi.TP_SITUACAO_APROVACAO,'A') = 'A' ");
		sql.append("  AND hbi.TP_SITUACAO_HISTORICO_BOLETO='A' ");
		sql.append("  ) ");
		
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery(){

			public void configQuery(SQLQuery sqlQuery) {
				
				sqlQuery.addScalar("ID_HISTORICO_BOLETO", Hibernate.LONG);
				sqlQuery.addScalar("ID_BOLETO", Hibernate.LONG);
				sqlQuery.addScalar("NR_OCORRENCIA_BANCO", Hibernate.SHORT);
				sqlQuery.addScalar("NR_FATURA", Hibernate.LONG);
				sqlQuery.addScalar("NR_BOLETO", Hibernate.STRING);
				sqlQuery.addScalar("TP_SITUACAO_BOLETO", Hibernate.STRING);
				sqlQuery.addScalar("DT_EMISSAO", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("DT_VENCIMENTO", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("VL_TOTAL", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_JUROS_DIA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_DESCONTO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("TP_PESSOA_CLIENTE", Hibernate.STRING);
				sqlQuery.addScalar("NR_IDENTIFICACAO", Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA", Hibernate.STRING);
				sqlQuery.addScalar("ID_PESSOA", Hibernate.LONG);
				sqlQuery.addScalar("NR_CONTA_CORRENTE", Hibernate.STRING);
				sqlQuery.addScalar("DS_NOME_ARQUIVO_COBRANCA", Hibernate.STRING);
				sqlQuery.addScalar("NR_AGENCIA_BANCARIA", Hibernate.SHORT);
				sqlQuery.addScalar("NR_DIGITO", Hibernate.STRING);
				sqlQuery.addScalar("NR_BANCO", Hibernate.SHORT);
				sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
				sqlQuery.addScalar("NR_IDENTIFICACAO_FILIAL", Hibernate.STRING);
				sqlQuery.addScalar("NR_CEP", Hibernate.STRING);
				sqlQuery.addScalar("DS_BAIRRO", Hibernate.STRING);
				sqlQuery.addScalar("NM_MUNICIPIO", Hibernate.STRING);
				sqlQuery.addScalar("SG_UNIDADE_FEDERATIVA", Hibernate.STRING);
				sqlQuery.addScalar("DS_COMPLEMENTO", Hibernate.STRING);
				sqlQuery.addScalar("DS_ENDERECO", Hibernate.STRING);
				sqlQuery.addScalar("NR_ENDERECO", Hibernate.STRING);
				sqlQuery.addScalar("DS_TIPO_LOGRADOURO", Hibernate.STRING);
				
				sqlQuery.addScalar("ID_ENDERECO", Hibernate.LONG);
				sqlQuery.addScalar("ID_ENDERECO_DEF", Hibernate.LONG);
				
				sqlQuery.addScalar("NR_CEP_DEF", Hibernate.STRING);
				sqlQuery.addScalar("DS_BAIRRO_DEF", Hibernate.STRING);
				sqlQuery.addScalar("NM_MUNICIPIO_DEF", Hibernate.STRING);
				sqlQuery.addScalar("SG_UNIDADE_FEDERATIVA_DEF", Hibernate.STRING);
				sqlQuery.addScalar("DS_COMPLEMENTO_DEF", Hibernate.STRING);
				sqlQuery.addScalar("DS_ENDERECO_DEF", Hibernate.STRING);
				sqlQuery.addScalar("NR_ENDERECO_DEF", Hibernate.STRING);
				sqlQuery.addScalar("DS_TIPO_LOGRADOURO_DEF", Hibernate.STRING);
				sqlQuery.addScalar("SQ_COBRANCA", Hibernate.LONG);
				sqlQuery.addScalar("NR_CARTEIRA", Hibernate.LONG);
			}
		
		};
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("dhOcorrencia", dhOcorrencia);
		params.put("idCedente", idCedente);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, configureSqlQuery);
	}
	
	public String removeTabSpace(String field, String alias){
		StringBuilder sql = new StringBuilder();
		sql.append("      REPLACE(REPLACE(REPLACE(")
		   .append(field)
		   .append(", CHR(10)), CHR(13)), CHR(9)) AS ")
		   .append(alias)
		   .append(", ");
		return sql.toString();
	}
	
	public void executeUpdateBoleto(final Map<Long, Map<String, String>> map) {
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Connection cnn  = session.connection();
				PreparedStatement ps = cnn.prepareStatement("UPDATE boleto set TP_SITUACAO_BOLETO = ?, TP_SITUACAO_ANT_BOLETO = ? where ID_BOLETO = ?");
				
				for (Entry<Long, Map<String, String>> entry : map.entrySet()) {
					Map<String, String> tipos = entry.getValue();
					ps.setString(1, tipos.get("tpSituacaoBoleto"));
					ps.setString(2, tipos.get("tpSituacaoAntBoleto"));
					ps.setLong(3, entry.getKey());
					ps.addBatch();
				}
				
				ps.executeBatch();
				
				return null;
			}
		});
	}
	
	public void executeUpdateHistoricoBoleto(final Map<Long, String> map) {

		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Connection cnn  = session.connection();
				PreparedStatement ps = cnn.prepareStatement("UPDATE historico_boleto set TP_SITUACAO_HISTORICO_BOLETO = ? where ID_HISTORICO_BOLETO = ?");
				
				for (Entry<Long, String> entry : map.entrySet()) {
					ps.setString(1, entry.getValue());
					ps.setLong(2, entry.getKey());
					ps.addBatch();
				}
				
				ps.executeBatch();
				
				return null;
			}
		});
	}

	public List<HistoricoBoleto> findHistoricosBoletoBradesco() {
		String hql = "select hb from HistoricoBoleto hb "
				+ "where "
				+ "hb.boleto.cedente.banco.nrBanco = 237 "
				+ "and hb.ocorrenciaBanco.tpOcorrencia.value = 'REM' "
				+ "and hb.tpSituacaoHistoricoBoleto.value = 'A' "
				+ "and (hb.tpSituacaoAprovacao.value = 'A' or hb.tpSituacaoAprovacao is null)";
		return getAdsmHibernateTemplate().find(hql);
	}
	
}