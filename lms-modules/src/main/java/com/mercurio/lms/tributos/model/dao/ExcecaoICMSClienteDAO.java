package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tributos.model.ExcecaoICMSCliente;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ExcecaoICMSClienteDAO extends BaseCrudDao<ExcecaoICMSCliente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ExcecaoICMSCliente.class;
    }
    
    /**
     * Método que inicializa os relacionamentos do pojo
     */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativa", FetchMode.JOIN);
		lazyFindById.put("tipoTributacaoIcms", FetchMode.JOIN);
	} 
	
	/**
	 * Método responsável por buscar ParametroSubstituicaoTrib que estejam no mesmo intervalo de vigência
	 * 
	 * @author HectorJ
	 * @since 31/05/20006
	 * 
	 * @param vigenciaInicial
	 * @param vigenciaFinal
	 * @return List <ExcecaoICMSCliente>
	 */
	public List findExcecaoICMSClienteByVigenciaEquals(
					  YearMonthDay vigenciaInicial
					, YearMonthDay vigenciaFinal
					, Long idUnidadeFederativa
					, Long idTipoTributacao
					, String tpFrete
					, String nrCnpjParcialDev
					, Long idExcecaoICMSCliente){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("eic");
		
		hql.addFrom(getPersistentClass().getName() + " eic ");
		
		/** Criteria para buscar registros no mesmo intervalo de vigência */ 
		hql.addCustomCriteria("( (? between eic.dtVigenciaInicial and eic.dtVigenciaFinal) " +
							  " OR (? between eic.dtVigenciaInicial and eic.dtVigenciaFinal) " +
							  " OR (? < eic.dtVigenciaInicial  AND ? > eic.dtVigenciaFinal) )");
		
		hql.addCriteriaValue(vigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
		hql.addCriteriaValue(vigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
		
		hql.addCriteria("eic.unidadeFederativa.idUnidadeFederativa", "=", idUnidadeFederativa);
		hql.addCriteria("eic.tpFrete", "=", tpFrete);
		
		if (nrCnpjParcialDev.length() == 8) {
			hql.addCriteria("SUBSTR(eic.nrCNPJParcialDev, 1, 8)", "=", nrCnpjParcialDev);
		} else {
			hql.addCustomCriteria("(eic.nrCNPJParcialDev = ? OR " +
					"eic.nrCNPJParcialDev = ?)");
			hql.addCriteriaValue(nrCnpjParcialDev.substring(0, 8));
			hql.addCriteriaValue(nrCnpjParcialDev);
		}
		
		hql.addCriteria("eic.idExcecaoICMSCliente", "!=", idExcecaoICMSCliente);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
    /**
     * Retorna uma ExcecaoICMSCliente a partir dos filtros informado
     * 
     * @author Mickaël Jalbert
     * @since 01/06/2006
     * 
     * @param String nrIdentificacaoDev
     * @param String nrIdentificacaoRem
     * @param String tpFrete
     * @param Long idUfOrigem
     * @param YearMonthDay dtVigencia
     * 
     * @return ExcecaoICMSCliente
     * */
    public ExcecaoICMSCliente findByUK(String nrIdentificacaoDev, String nrIdentificacaoRem, String tpFrete, Long idUfOrigem, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();

    	hql.addProjection("exc");

    	hql.addInnerJoin(ExcecaoICMSCliente.class.getName(), "exc");
    	hql.addLeftOuterJoin("exc.remetentesExcecaoICMSCli", "rem");
    	
    	hql.addCustomCriteria("( exc.nrCNPJParcialDev = ? OR " +
				"exc.nrCNPJParcialDev = ? )");
    	hql.addCriteriaValue(nrIdentificacaoDev.substring(0, 8));
		hql.addCriteriaValue(nrIdentificacaoDev);
		
		hql.addCustomCriteria("( rem.nrCnpjParcialRem = ? OR " +
				"rem.nrCnpjParcialRem = ? OR  " +
				"rem.nrCnpjParcialRem IS NULL)");
		hql.addCriteriaValue(nrIdentificacaoRem.substring(0, 8));
		hql.addCriteriaValue(nrIdentificacaoRem);

    	hql.addCriteria("exc.tpFrete", "=", tpFrete);
    	hql.addCriteria("exc.unidadeFederativa.id", "=", idUfOrigem);
    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "exc.dtVigenciaInicial", "exc.dtVigenciaFinal", dtVigencia);
        JTVigenciaUtils.getHqlVigenciaNotNull(hql, "NVL(rem.dtVigenciaInicial,trunc(sysdate)-1)", "NVL(rem.dtVigenciaFinal,trunc(sysdate)+1)", dtVigencia);

    	return (ExcecaoICMSCliente) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

    /**
     * Busca Excecoes ICMS Cliente via constulta SQL
     * 
     * @author André Valadas
     * @since 05/05/2009
     * 
     * @param nrIdentificacaoDev
     * @param nrIdentificacaoRem
     * @param tpFrete
     * @param idUfFilialOrigem
     * @param idUfDestino
     * @param idFilialOrigem
     * @param idNaturezaProduto
     * @param vlParametroTributacaoDevido
     * @param dtVigencia
     * @return 
     */
    public List<ExcecaoICMSCliente> findExcecaoICMSCliente(
    		String nrIdentificacaoDev,
    		String nrIdentificacaoRem,
    		String tpFrete,
    		Long idUfFilialOrigem,
    		Long idUfDestino,
    		Long idFilialOrigem,
    		Long idNaturezaProduto,
    		Long vlParametroTributacaoDevido,
    		YearMonthDay dtVigencia) {

    	//Foi adicionado um hint em pessoa /*+ index(pes PESS_02_UK)*/
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT EIC.BL_SUBCONTRATACAO AS blSubcontratacao");
		query.append("		,EIC.ID_TIPO_TRIBUTACAO_ICMS AS idTipoTributacaoIcms");
		query.append("		,EIC.DS_REGIME_ESPECIAL AS dsRegimeEspecial");
		query.append("		,EIC.CD_EMB_LEGAL_MASTERSAF AS cdEmbLegalMastersaf");
		query.append("		,EIC.ID_EXCECAO_ICMS_CLIENTE AS idExcecaoIcmsCliente");
		query.append(" FROM EXCECAO_ICMS_CLIENTE EIC");
		query.append("		,REMETENTE_EXCECAO_ICMS_CLI REIC");
		query.append("		,PESSOA PES");
		query.append("		,CLIENTE CLI");
		query.append("		,EXCECAO_ICMS_NATUREZA EIN");
		query.append(" WHERE EIC.ID_EXCECAO_ICMS_CLIENTE = REIC.ID_EXCECAO_ICMS_CLIENTE(+)");
		query.append("	AND EIC.ID_EXCECAO_ICMS_CLIENTE = EIN.ID_EXCECAO_ICMS_CLIENTE(+)");
		query.append("	AND CLI.ID_CLIENTE = PES.ID_PESSOA");
		// Devedor
		query.append("	AND ((EIC.NR_CNPJ_PARCIAL_DEV = :nrIdentificacaoDev AND PES.NR_IDENTIFICACAO = :nrIdentificacaoDev)");
		query.append("		OR (EIC.NR_CNPJ_PARCIAL_DEV = :nrIdentificacaoDevParcial AND SUBSTR(PES.NR_IDENTIFICACAO, 1, 8) = :nrIdentificacaoDevParcial)");
		query.append("		AND PES.TP_IDENTIFICACAO = :tpIdentificacao)");
		// SQL sugerido por Joelson
		query.append("	AND CLI.ID_FILIAL_ATENDE_COMERCIAL = DECODE(EIC.ID_TIPO_TRIBUTACAO_ICMS, :vlParametroTributacaoDevido, :idFilialOrigem, CLI.ID_FILIAL_ATENDE_COMERCIAL)");
		// Natureza do Produto
		query.append("	AND (EIN.ID_EXCECAO_ICMS_NATUREZA IS NULL");
		query.append("		OR (:idNaturezaProduto <> 0 AND :idNaturezaProduto = EIN.ID_NATUREZA_PRODUTO))");
		
		// Remetente
		
		/*Alteração do SQL abaixo foi solicitada pelo Joelson*/
		/*--02/10/2009--*/
		query.append("	AND (REIC.NR_CNPJ_PARCIAL_REM IS NULL");
		query.append("		OR (REIC.NR_CNPJ_PARCIAL_REM = :nrIdentificacaoRem ");
		query.append("		OR REIC.NR_CNPJ_PARCIAL_REM  = :nrIdentificacaoRemParcial ");
		
		query.append("		AND EXISTS (SELECT 1 ");
		query.append("		FROM PESSOA PES2 ");
		query.append("		WHERE ( PES2.NR_IDENTIFICACAO = :nrIdentificacaoRem OR SUBSTR(PES2.NR_IDENTIFICACAO,1,8) = :nrIdentificacaoRemParcial ) ");
		query.append("		AND PES2.TP_IDENTIFICACAO = :tpIdentificacao)))");
		/*--02/10/2009--*/
		
		// Frete
		query.append("	AND EIC.TP_FRETE = :tpFrete");
		query.append("	AND EIC.ID_UNIDADE_FEDERATIVA = :idUfFilialOrigem");
		// Vigencias
		query.append("	AND (EIC.DT_VIGENCIA_INICIAL <= :dtVigencia AND EIC.DT_VIGENCIA_FINAL >= :dtVigencia)");
		query.append("	AND ((REIC.DT_VIGENCIA_INICIAL IS NULL OR REIC.DT_VIGENCIA_INICIAL <= :dtVigencia)");
		query.append("		AND (REIC.DT_VIGENCIA_FINAL IS NULL OR REIC.DT_VIGENCIA_FINAL >= :dtVigencia))");

		/** Alias dos filtros */
		Map criteria = new HashMap();
		criteria.put("nrIdentificacaoDevParcial", StringUtils.substring(nrIdentificacaoDev, 0, 8));
		criteria.put("nrIdentificacaoDev", nrIdentificacaoDev);
		criteria.put("nrIdentificacaoRemParcial", StringUtils.substring(nrIdentificacaoRem, 0, 8));
		criteria.put("nrIdentificacaoRem", nrIdentificacaoRem);
		criteria.put("tpIdentificacao", "CNPJ");
		criteria.put("idFilialOrigem", idFilialOrigem);
		criteria.put("idUfFilialOrigem", idUfFilialOrigem);
		criteria.put("idUfDestino", idUfDestino);
		criteria.put("idNaturezaProduto", LongUtils.hasValue(idNaturezaProduto) ? idNaturezaProduto : LongUtils.ZERO);
		criteria.put("tpFrete", tpFrete);
		criteria.put("tpFreteCIF", ConstantesExpedicao.TP_FRETE_CIF);
		criteria.put("tpFreteFOB", ConstantesExpedicao.TP_FRETE_FOB);
		criteria.put("vlParametroTributacaoDevido", vlParametroTributacaoDevido);
		criteria.put("dtVigencia", dtVigencia);

		/** Configura o retorno da consulta */
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("blSubcontratacao", Hibernate.STRING);
				sqlQuery.addScalar("idTipoTributacaoIcms", Hibernate.LONG);
				sqlQuery.addScalar("dsRegimeEspecial", Hibernate.STRING);
				sqlQuery.addScalar("cdEmbLegalMastersaf", Hibernate.STRING);
				sqlQuery.addScalar("idExcecaoIcmsCliente", Hibernate.LONG);
			}
		};
		List<Object[]> result = getAdsmHibernateTemplate().findPaginatedBySql(query.toString(), Integer.valueOf(1), Integer.valueOf(1), criteria, configSql).getList();

		List<ExcecaoICMSCliente> toReturn = new ArrayList<ExcecaoICMSCliente>(result.size());
		for(Object[] data : result) {
			ExcecaoICMSCliente eic = new ExcecaoICMSCliente();
			eic.setIdExcecaoICMSCliente(LongUtils.getLong(data[4]));
			eic.setBlSubcontratacao(BooleanUtils.toBoolean(String.valueOf(data[0]), "S", "N"));

			TipoTributacaoIcms tributacaoIcms = new TipoTributacaoIcms();
			tributacaoIcms.setIdTipoTributacaoIcms(LongUtils.getLong(data[1]));
			eic.setTipoTributacaoIcms(tributacaoIcms);
			eic.setDsRegimeEspecial(String.valueOf(data[2]));
			eic.setCdEmbLegalMastersaf(String.valueOf(data[3]));

			toReturn.add(eic);
		}
		return toReturn;
	}
}