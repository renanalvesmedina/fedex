package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaDescontoRfc;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;

public class DescontoRfcDAO extends BaseCrudDao<DescontoRfc, Long>{

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return DescontoRfc.class;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("proprietario", FetchMode.JOIN);
		fetchModes.put("proprietario.pessoa", FetchMode.JOIN);
		fetchModes.put("reciboFreteCarreteiro", FetchMode.JOIN);
		fetchModes.put("reciboFreteCarreteiro.filial", FetchMode.JOIN);
		fetchModes.put("pendencia", FetchMode.JOIN);
		fetchModes.put("pendencia.tpSituacaoPendencia", FetchMode.JOIN);
		fetchModes.put("controleCarga", FetchMode.JOIN);
		fetchModes.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
		fetchModes.put("usuario", FetchMode.JOIN);
		fetchModes.put("listParcelaDescontoRfc", FetchMode.SELECT);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}
	
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
	}
	
	public void removeByIdsAnexoDescontoRfc(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + AnexoDescontoRfc.class.getName() + " WHERE idAnexoDescontoRfc IN (:id)", ids);
	}

	public AnexoDescontoRfc findAnexoDescontoRfcById(Long idAnexoDescontoRfc) {
		return (AnexoDescontoRfc) getAdsmHibernateTemplate().load(AnexoDescontoRfc.class, idAnexoDescontoRfc);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAnexoDescontoRfcByIdDescontoRfc(Long idDescontoRfc) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoDescontoRfc AS idAnexoDescontoRfc,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.dsAnexo AS dsAnexo,");
		hql.append(" anexo.dhCriacao AS dhCriacao,");
		hql.append(" usuario.usuarioADSM.nmUsuario AS nmUsuario)");
		hql.append(" FROM AnexoDescontoRfc AS anexo");
		hql.append("  INNER JOIN anexo.descontoRfc descontoRfc");
		hql.append("  INNER JOIN anexo.usuarioLMS usuario");
		hql.append(" WHERE descontoRfc.idDescontoRfc = ?");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDescontoRfc});
	}
	
	public Integer getRowCountAnexoDescontoRfcByIdDescontoRfc(Long idDescontoRfc) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_desconto_rfc WHERE id_desconto_rfc = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{idDescontoRfc});
	}
	
	private SqlTemplate getHqlForFindPaginated(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(desconto.idDescontoRfc", "idDescontoRfc");
		hql.addProjection("desconto.nrDescontoRfc", "nrDescontoRfc");
		hql.addProjection("desconto.nrReciboIndenizacao", "nrReciboIndenizacao");
		hql.addProjection("desconto.nrIdentificacaoSemiReboque", "nrIdentificacaoSemiReboque");
		hql.addProjection("desconto.vlTotalDesconto", "vlTotalDesconto");
		hql.addProjection("desconto.vlSaldoDevedor", "vlSaldoDevedor");
		hql.addProjection("desconto.dtInicioDesconto", "dtInicioDesconto");
		hql.addProjection("desconto.tpSituacao", "tpSituacao");
		hql.addProjection("desconto.dtAtualizacao", "dtAtualizacao");
		hql.addProjection("tipoDescontoRfc.dsTipoDescontoRfc", "dsTipoDescontoRfc");
		hql.addProjection("proprietario.pessoa.tpIdentificacao", "tpIdentificacao");
		hql.addProjection("proprietario.pessoa.nrIdentificacao", "nrIdentificacao");
		hql.addProjection("proprietario.tpProprietario", "tpProprietario");
		hql.addProjection("meioTransporte.nrFrota", "nrFrota");
		hql.addProjection("meioTransporte.nrIdentificador", "nrIdentificador");
		hql.addProjection("proprietario.pessoa.nmPessoa", "nmPessoa");
		hql.addProjection("filial.pessoa.nmFantasia", "nmFantasia");
		hql.addProjection("filial.sgFilial", "sgFilial)");

		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append(DescontoRfc.class.getName()).append(" AS desconto");
		hqlFrom.append(" INNER JOIN desconto.filial AS filial");
		hqlFrom.append(" INNER JOIN desconto.tipoDescontoRfc AS tipoDescontoRfc");
		hqlFrom.append(" INNER JOIN desconto.proprietario AS proprietario");
		hqlFrom.append(" INNER JOIN proprietario.pessoa AS pessoa");
		hqlFrom.append(" LEFT JOIN desconto.meioTransporte AS meioTransporte");

		hql.addFrom(hqlFrom.toString());

		hql.addCriteria("desconto.nrDescontoRfc","=", criteria.getLong("nrDescontoRfc"));
		hql.addCriteria("desconto.tpSituacao","=", criteria.getString("tpSituacao"));
		hql.addCriteria("desconto.tpOperacao","=", criteria.getString("tpOperacao"));
		hql.addCriteria("tipoDescontoRfc.idTipoDescontoRfc","=", criteria.getLong("idTipoDescontoRfc"));
		hql.addCriteria("filial.idFilial","=", criteria.getLong("idFilial"));
		hql.addCriteria("proprietario.idProprietario","=", criteria.getLong("idProprietario"));
		hql.addCriteria("meioTransporte.idMeioTransporte","=", criteria.getLong("idMeioTransporte"));
		hql.addCriteria("desconto.dtAtualizacao",">=", criteria.getYearMonthDay("dtAtualizacaoInicial"));
		hql.addCriteria("desconto.dtAtualizacao","<=", criteria.getYearMonthDay("dtAtualizacaoFinal"));
		hql.addCriteria("desconto.dtInicioDesconto",">=", criteria.getYearMonthDay("dtInicioDescontoInicial"));
		hql.addCriteria("desconto.dtInicioDesconto","<=", criteria.getYearMonthDay("dtInicioDescontoFinal"));
		
		hql.addOrderBy("desconto.nrDescontoRfc DESC");

		return hql;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAllTipoDescontoRfc() {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" tipoDescontoRfc.idTipoDescontoRfc AS idTipoDescontoRfc,");
		hql.append(" tipoDescontoRfc.dsTipoDescontoRfc AS dsTipoDescontoRfc)");
		hql.append(" FROM TipoDescontoRfc AS tipoDescontoRfc");
		hql.append(" ORDER BY tipoDescontoRfc.dsTipoDescontoRfc ASC");
				
		return getAdsmHibernateTemplate().find(hql.toString());
	}

	public DescontoRfc findDescontoByRecibo(ReciboFreteCarreteiro recibo) {
		return findDescontoByRecibo(recibo);
	}
	
	public DescontoRfc findDescontoByRecibo(ReciboFreteCarreteiro recibo, YearMonthDay dtInicioDesconto) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(DescontoRfc.class.getName()).append(" desconto ")
		.append(" inner join fetch desconto.reciboFreteCarreteiro as recibo ");
		
		hql.addProjection("desconto");
		hql.addFrom(hqlFrom.toString());
		hql.addCustomCriteria("desconto.tpSituacao IN ('A','P')");
		hql.addCriteria("recibo.idReciboFreteCarreteiro","=",recibo.getIdReciboFreteCarreteiro());
		hql.addCriteria("desconto.dtInicioDesconto",">=",dtInicioDesconto);

		return (DescontoRfc)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}
	
	public List<DescontoRfc> findDescontoByProprietario(ReciboFreteCarreteiro recibo) {
		return findDescontoByProprietario(recibo.getProprietario(), JTDateTimeUtils.getDataAtual(),recibo.getFilial());
	}
	
	
	public Boolean isDescontoByProprietarioStatus(Proprietario proprietario) {
		SqlTemplate hql = new SqlTemplate();
		
		StringBuilder hqlFrom = new StringBuilder()		
		.append(DescontoRfc.class.getName()).append(" desconto ")
		.append(" inner join desconto.proprietario as proprietario ")
		.append(" inner join desconto.filial as filial ");
		
		hql.addProjection("COUNT(desconto)");
		hql.addFrom(hqlFrom.toString());
		hql.addCustomCriteria("desconto.tpSituacao IN ('A','P','I')");
		hql.addCriteria("proprietario.idProprietario","=", proprietario.getIdProprietario());		
		hql.addCriteria("desconto.dtInicioDesconto","<=", JTDateTimeUtils.getDataAtual());

		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria()) > 0;
	}
	
	
	
	public List<DescontoRfc> findDescontoByProprietario(Proprietario proprietario, YearMonthDay dtInicioDesconto, Filial filial) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(DescontoRfc.class.getName()).append(" desconto ")
		.append(" inner join fetch desconto.proprietario as proprietario ")
		.append(" inner join desconto.filial as filial ");
		
		hql.addProjection("desconto");
		hql.addFrom(hqlFrom.toString());
		hql.addCustomCriteria("desconto.tpSituacao IN ('A','P')");
		hql.addCriteria("desconto.blPrioritario","=","N");
		hql.addCriteria("proprietario.idProprietario","=", proprietario.getIdProprietario());		
		hql.addCriteria("desconto.dtInicioDesconto","<=", dtInicioDesconto);
		hql.addCriteria("filial.idFilial", "=", filial.getIdFilial());

		return (List<DescontoRfc>)getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public DescontoRfc findDescontoByProprietarioPrioritario(Proprietario proprietario, YearMonthDay dtInicioDesconto, Filial filial) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(DescontoRfc.class.getName()).append(" desconto ")
		.append(" inner join fetch desconto.proprietario as proprietario ")
		.append(" inner join desconto.filial as filial ");
		
		hql.addProjection("desconto");
		hql.addFrom(hqlFrom.toString());
		hql.addCustomCriteria("desconto.tpSituacao IN ('A','P')");
		hql.addCriteria("desconto.blPrioritario","=","S");
		hql.addCriteria("proprietario.idProprietario","=", proprietario.getIdProprietario());		
		hql.addCriteria("desconto.dtInicioDesconto","<=", dtInicioDesconto);
		hql.addCriteria("filial.idFilial", "=", filial.getIdFilial());

		return (DescontoRfc)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}
	
	
	/**
	 * Valida se existe algum outro desconto cadastrado para o mesmo proprietário.
	 * 
	 * @param descontoRfc
	 * @return Boolean
	 */
	public Boolean isDescontoExistenteByProprietario(DescontoRfc descontoRfc) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(DescontoRfc.class.getName()).append(" desconto ");
		
		hql.addProjection("COUNT(desconto)");
		hql.addFrom(hqlFrom.toString());
		hql.addCustomCriteria("desconto.tpSituacao IN ('A','P','I')");
		hql.addCriteria("desconto.idDescontoRfc","!=", descontoRfc.getIdDescontoRfc());
		hql.addCriteria("proprietario.idProprietario","=", descontoRfc.getProprietario().getIdProprietario());
		hql.addCriteria("desconto.dtInicioDesconto","<=", descontoRfc.getDtInicioDesconto());
		hql.addCriteria("desconto.blPrioritario","=", descontoRfc.getBlPrioritario());
		

		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria()) > 0;
	}
	
	
	/**
	 * Valida se existe algum desconto cadastrado para o proprietário.
	 * 
	 * @param descontoRfc
	 * @return Boolean
	 */
	public Boolean isExistenteDescontoByProprietario(Long idProprietario) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(DescontoRfc.class.getName()).append(" desconto ");
		
		hql.addProjection("COUNT(desconto)");
		hql.addFrom(hqlFrom.toString());
		hql.addCustomCriteria("desconto.tpSituacao IN ('A','P','I')");
		hql.addCriteria("proprietario.idProprietario","=", idProprietario);
		

		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria()) > 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<ParcelaDescontoRfc> findParcelasDescontos(DescontoRfc desconto) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(ParcelaDescontoRfc.class.getName()).append(" parcela ")
		.append(" inner join fetch parcela.descontoRfc as desconto ");
		
		hql.addProjection("parcela");
		hql.addFrom(hqlFrom.toString());
		hql.addCustomCriteria("desconto.tpSituacao IN ('A','P')");
		hql.addCriteria("desconto.idDescontoRfc","=",desconto.getIdDescontoRfc());
		hql.addCustomCriteria(" parcela.reciboFreteCarreteiro IS  NULL");
		hql.addOrderBy("desconto.idDescontoRfc", "asc");
		
		return ((List<ParcelaDescontoRfc>) getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()));
	}

	/**
	 * Verifica se existe algum parcela já descontada em algum recibo.
	 * 
	 * @param descontoRfc
	 * @return Boolean
	 */
	public Boolean isParcelaDescontoRfcPaga(DescontoRfc descontoRfc) {
		SqlTemplate hql = new SqlTemplate();		
		
		StringBuilder hqlFrom = new StringBuilder()
		.append(ParcelaDescontoRfc.class.getName()).append(" parcela ")
		.append(" INNER JOIN parcela.descontoRfc AS desconto ");
		
		hql.addProjection("COUNT(parcela)");
		hql.addFrom(hqlFrom.toString());
		hql.addCriteria("desconto.idDescontoRfc","=", descontoRfc.getIdDescontoRfc());
		hql.addCustomCriteria(" parcela.reciboFreteCarreteiro IS NOT NULL");
		
		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria()) > 0;
	}
	
	public void removeAllParcelaDescontoRfc(Long idDescontoRfc) {
		getAdsmHibernateTemplate().removeById("DELETE FROM " + ParcelaDescontoRfc.class.getName() + " WHERE descontoRfc.idDescontoRfc = :id", idDescontoRfc);
	}	

	public List<ParcelaDescontoRfc> findParcelaByRecibo(ReciboFreteCarreteiro rfc) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder().append(ParcelaDescontoRfc.class.getName()).append(" parcela ");
		hql.addProjection("parcela");
		hql.addFrom(hqlFrom.toString());			
		hql.addCriteria(" parcela.reciboFreteCarreteiro" , "=", rfc);
		
		return ((List<ParcelaDescontoRfc>) getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()));
	}

	public DescontoRfc findDescontoByProprietarioPrioritario(ReciboFreteCarreteiro recibo) {
		return findDescontoByProprietarioPrioritario(recibo.getProprietario(), JTDateTimeUtils.getDataAtual(),recibo.getFilial());
		
	}
}