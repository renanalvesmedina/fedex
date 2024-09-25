package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.MotoristaControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LiberacaoReguladoraDAO extends BaseCrudDao<LiberacaoReguladora, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return LiberacaoReguladora.class;
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		super.initFindPaginatedLazyProperties(fetchModes);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition findDef) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT new map( ")
		.append("RSP.nmPessoa AS reguladoraSeguro_pessoa_nmPessoa, ")
		.append("LR.nrLiberacao AS nrLiberacao, ")
		.append("LR.tpOperacao AS tpOperacao, ")
		.append("FEP.nmPessoa AS filial_empresa_pessoa_nmPessoa, ")
		.append("FP.nmFantasia AS filial_pessoa_nmFantasia, ")
		.append("F.sgFilial AS filial_sgFilial, ")
		.append("LR.idLiberacaoReguladora AS idLiberacaoReguladora, ")
		.append("M.tpVinculo AS motorista_tpVinculo, ")
		.append("LR.dtLiberacao AS dtLiberacao, ")
		.append("LR.dtVencimento AS dtVencimento, ")
		.append("MP.nmPessoa AS motorista_pessoa_nmPessoa, ")
		.append("MP.tpIdentificacao AS motorista_pessoa_tpIdentificacao, ")
		.append("MP.nrIdentificacao AS motorista_pessoa_nrIdentificacao, ")
		.append("PP.nmPessoa AS proprietario_pessoa_nmPessoa, ")
		.append("PP.tpIdentificacao AS proprietario_pessoa_tpIdentificacao, ")
		.append("PP.nrIdentificacao AS proprietario_pessoa_nrIdentificacao, ")
		.append("MT.nrFrota AS meioTransporte_nrFrota, ")
		.append("MT.nrIdentificador AS meioTransporte_nrIdentificador ")
		.append(") ");
		Object sql[] = createSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(sbSql.append((String)sql[0]).toString(),findDef.getCurrentPage(),findDef.getPageSize(),(Map)sql[1]);
	}

	public Map findLiberacaoReguladoraById(Long id) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT new map( ")
		.append("RSP.nmPessoa AS reguladoraSeguro_pessoa_nmPessoa, ")
		.append("RS.idReguladora AS reguladoraSeguro_idReguladora, ")
		.append("LR.nrLiberacao AS nrLiberacao, ")
		.append("FP.nmFantasia AS filial_pessoa_nmFantasia, ")
		.append("F.sgFilial AS filial_sgFilial, ")
		.append("F.idFilial AS filial_idFilial, ")
		.append("LR.dtLiberacao AS dtLiberacao, ")
		.append("LR.idLiberacaoReguladora AS idLiberacaoReguladora, ")
		.append("LR.dtVencimento AS dtVencimento, ")
		.append("LR.tpOperacao AS tpOperacao, ")
		.append("M.idMotorista AS motorista_idMotorista, ")
		.append("M.tpVinculo AS motorista_tpVinculo, ")
		.append("MP.nmPessoa AS motorista_pessoa_nmPessoa, ")
		.append("MP.tpIdentificacao AS motorista_pessoa_tpIdentificacao, ")
		.append("MP.nrIdentificacao AS motorista_pessoa_nrIdentificacao, ")
		.append("P.idProprietario AS proprietario_idProprietario, ")
		.append("PP.nmPessoa AS proprietario_pessoa_nmPessoa, ")
		.append("PP.tpIdentificacao AS proprietario_pessoa_tpIdentificacao, ")
		.append("PP.nrIdentificacao AS proprietario_pessoa_nrIdentificacao, ")
		.append("MT.id AS meioTransporte_idMeioTransporte, ")
		.append("MT.id AS meioTransporte2_idMeioTransporte, ")
		.append("MT.nrFrota AS meioTransporte2_nrFrota, ")
		.append("MT.nrIdentificador AS meioTransporte_nrIdentificador ")
		.append(") ");

		sbSql.append("FROM ")
		.append(LiberacaoReguladora.class.getName()).append(" AS LR ")
		.append("INNER JOIN LR.reguladoraSeguro RS ")
		.append("INNER JOIN RS.pessoa RSP ")
		.append("INNER JOIN LR.filial F ")
		.append("INNER JOIN F.empresa FE ")
		.append("INNER JOIN FE.pessoa FEP ")
		.append("INNER JOIN F.pessoa FP ")
		.append("LEFT  JOIN LR.motorista M ")
		.append("LEFT  JOIN M.pessoa MP ")
		.append("LEFT  JOIN LR.meioTransporte MT ")
		.append("LEFT  JOIN MT.meioTranspProprietarios MTP ")
		.append("LEFT  JOIN MTP.proprietario P ")
		.append("LEFT  JOIN P.pessoa PP ")
		.append("WHERE LR.idLiberacaoReguladora = ? ");

		sbSql.append("AND (MTP.id = (SELECT max(MTP2.id) FROM ").append(MeioTranspProprietario.class.getName()).append(" AS MTP2 WHERE ")
		.append("MTP2.meioTransporte.id = MT.id and MTP2.dtVigenciaInicial <= ? and MTP2.dtVigenciaFinal >= ?) OR MTP.id is null)");

		List rs = getAdsmHibernateTemplate().find(sbSql.toString(),new Object[]{id,JTDateTimeUtils.getDataAtual(),JTDateTimeUtils.getDataAtual()});
		return (Map)rs.get(0);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		Object sql[] = createSqlTemplate(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery((String)sql[0],(Map)sql[1]);			
	}

	public LiberacaoReguladora findLastLiberacaoReguladora(
			Long idMotorista,
			String tpOperacao,
			Long idLiberacaoReguladora
	) {

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "lr");
		dc.add(Restrictions.eq("lr.motorista.id", idMotorista));
		dc.add(Restrictions.eq("lr.tpOperacao", tpOperacao));
		if(idLiberacaoReguladora != null) {
			dc.add(Restrictions.ne("lr.id", idLiberacaoReguladora));
		}
		dc.addOrder(Order.desc("lr.dtLiberacao"));

		List result = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE).getList();
		if(!result.isEmpty()) {
			return (LiberacaoReguladora) result.get(0);
		}

		return null;
	}

	public Integer getCountLiberacaoMotorista(
			Long idMotorista,
			Long idSeguradoraReguladora,
			YearMonthDay dtInicial,
			YearMonthDay dtFinal,
			String tpOperacao
	) {
		DetachedCriteria dc = createDetachedCriteria()
		.setProjection(Projections.rowCount())

		.add(Restrictions.ge("dtLiberacao", dtInicial))
		.add(Restrictions.le("dtLiberacao", dtFinal))
		.add(Restrictions.eq("tpOperacao", tpOperacao))
		.add(Restrictions.isNull("dtVencimento"))
		.add(Restrictions.eq("motorista.id", idMotorista))
		.add(Restrictions.eq("reguladoraSeguro.id", idSeguradoraReguladora));

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	private Object[] createSqlTemplate(TypedFlatMap criteria) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("FROM ")
		.append(LiberacaoReguladora.class.getName()).append(" AS LR ")
		.append("INNER JOIN LR.reguladoraSeguro RS ")
		.append("INNER JOIN RS.pessoa RSP ")
		.append("INNER JOIN LR.filial F ")
		.append("INNER JOIN F.empresa FE ")
		.append("INNER JOIN FE.pessoa FEP ")
		.append("INNER JOIN F.pessoa FP ")
		.append("LEFT  JOIN LR.motorista M ")
		.append("LEFT  JOIN M.pessoa MP ")
		.append("LEFT  JOIN LR.meioTransporte MT ")
		.append("LEFT  JOIN MT.meioTranspProprietarios MTP ")
		.append("LEFT  JOIN MTP.proprietario P ")
		.append("LEFT  JOIN P.pessoa PP ");

		Map parameters = new HashMap();
		StringBuffer sbWhere = new StringBuffer();
		if (!StringUtils.isBlank(criteria.getString("reguladoraSeguro.idReguladora"))) {
			AndOrWhere(sbWhere).append("RS.idReguladora = :idReguladora ");
			parameters.put("idReguladora",criteria.getLong("reguladoraSeguro.idReguladora"));
		}
		if (!StringUtils.isBlank(criteria.getString("nrLiberacao"))) {
			AndOrWhere(sbWhere).append("LR.nrLiberacao like :nrLiberacao ");
			parameters.put("nrLiberacao",criteria.getString("nrLiberacao"));
		}
		if (!StringUtils.isBlank(criteria.getString("filial.idFilial"))) {
			AndOrWhere(sbWhere).append("F.idFilial = :idFilial ");
			parameters.put("idFilial",criteria.getLong("filial.idFilial"));
		}
		if (!StringUtils.isBlank(criteria.getString("proprietario.idProprietario"))) {
			AndOrWhere(sbWhere).append("P.idProprietario = :idProprietario ");
			parameters.put("idProprietario",criteria.getLong("proprietario.idProprietario"));
		}
		if (!StringUtils.isBlank(criteria.getString("motorista.idMotorista"))) {
			AndOrWhere(sbWhere).append("M.idMotorista = :idMotorista ");
			parameters.put("idMotorista",criteria.getLong("motorista.idMotorista"));
		}

		if (!StringUtils.isBlank(criteria.getString("meioTransporte.idMeioTransporte"))) {
			AndOrWhere(sbWhere).append("MT.id = :idMeioTransporte ");
			parameters.put("idMeioTransporte",criteria.getLong("meioTransporte.idMeioTransporte"));
		}

		if (!StringUtils.isBlank(criteria.getString("dtLiberacaoInicial"))) {
			AndOrWhere(sbWhere).append("LR.dtLiberacao >= :dtLiberacaoInicial ");
			parameters.put("dtLiberacaoInicial",criteria.getYearMonthDay("dtLiberacaoInicial"));
		}
		if (!StringUtils.isBlank(criteria.getString("dtLiberacaoFinal"))) {
			AndOrWhere(sbWhere).append("LR.dtLiberacao <= :dtLiberacaoFinal ");
			parameters.put("dtLiberacaoFinal",criteria.getYearMonthDay("dtLiberacaoFinal"));
		}

		if (!StringUtils.isBlank(criteria.getString("dtVencimentoInicial"))) {
			AndOrWhere(sbWhere).append("LR.dtVencimento >= :dtVencimentoInicial ");
			parameters.put("dtVencimentoInicial",criteria.getYearMonthDay("dtVencimentoInicial"));
		}
		if (!StringUtils.isBlank(criteria.getString("dtVencimentoFinal"))) {
			AndOrWhere(sbWhere).append("LR.dtVencimento <= :dtVencimentoFinal ");
			parameters.put("dtVencimentoFinal",criteria.getYearMonthDay("dtVencimentoFinal"));
		}

		parameters.put("dateNow",JTDateTimeUtils.getDataAtual());
		AndOrWhere(sbWhere).append("(MTP.id = (SELECT max(MTP2.id) FROM ").append(MeioTranspProprietario.class.getName()).append(" AS MTP2 WHERE ")
			.append("MTP2.meioTransporte.id = MT.id and MTP2.dtVigenciaInicial <= :dateNow and MTP2.dtVigenciaFinal >= :dateNow) OR MTP.id is null)");

		sbSql.append(sbWhere);
		sbSql.append(" ORDER BY MT.nrFrota, MT.nrIdentificador, LR.dtLiberacao DESC");
		return new Object[] {sbSql.toString(),parameters};
	}
 
	private StringBuffer AndOrWhere(StringBuffer sb) {
		if (sb.length() == 0)
			sb.append(" WHERE ");
		else
			sb.append(" AND ");
		return sb;
	}

	/**
	 * Retorna número de liberação para motorista e proprietário informados se possuir data de vencimento
	 * maior que a data atual.
	 * 
	 * @author Felipe Ferreira
	 * @param idMotorista
	 * @param dtVencimento
	 * @param tpOperacao
	 * @return
	 */
	public LiberacaoReguladora findLiberacaoMotorista(Long idMotorista, YearMonthDay dtVencimento, String tpOperacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "lr");

		dc.createAlias("lr.motorista", "m");

		dc.add(Restrictions.eq("lr.tpOperacao", tpOperacao));
		dc.add(Restrictions.eq("m.id", idMotorista));
		dc.add(Restrictions.ge("lr.dtVencimento", dtVencimento));

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result.isEmpty())
			return null;

		return (LiberacaoReguladora)result.get(result.size()-1);
	}
	
	
	/**
	* Retorna registro sem data de vencimento
	*
	* @param idMotorista
	* @param tpOperacao
	* @return
	*/
	public LiberacaoReguladora findLiberacaoReguladoraSemVencimento(Long idMotorista, String tpOperacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "lr");

		dc.add(Restrictions.eq("lr.motorista.id", idMotorista))
		.add(Restrictions.eq("lr.tpOperacao", tpOperacao))
		.add(Restrictions.isNull("lr.dtVencimento"));

		return (LiberacaoReguladora) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	* Retorna número de registros relacionados a um Controle de Cargas de status !{cancelado,
	* fechado} (LMS-3750)
	*
	* @param idLiberacaoReguladora
	* @return
	*/
	public Integer getRowCountLiberacaoReguladoraControleCarga(Long idLiberacaoReguladora) {
		//LMS-3750
		ArrayList<String> statusCC = new ArrayList<String>();
		statusCC.add("FE"); 
		statusCC.add("CA"); 

		DetachedCriteria dc = DetachedCriteria.forClass(MotoristaControleCarga.class, "mcc")
		.setProjection(Projections.rowCount())
		.createAlias("mcc.controleCarga", "cc")
		.add(Restrictions.eq("mcc.liberacaoReguladora.id", idLiberacaoReguladora))
		//LMS-3750
		.add(Restrictions.not(Restrictions.in("cc.tpStatusControleCarga", statusCC)));

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

}
