package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoTabelaFreteCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcFaixaPeso;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFreteCarreteiroCe;
import com.mercurio.lms.util.JTDateTimeUtils;

public class TabelaFreteCarreteiroCeDAO extends BaseCrudDao<TabelaFreteCarreteiroCe, Long>{

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return TabelaFreteCarreteiroCe.class;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("proprietario", FetchMode.JOIN);
		fetchModes.put("proprietario.pessoa", FetchMode.JOIN);
		fetchModes.put("usuarioCriacao", FetchMode.JOIN);
		fetchModes.put("usuarioCriacao.usuarioADSM", FetchMode.JOIN);
		fetchModes.put("usuarioAlteracao", FetchMode.JOIN);
		fetchModes.put("usuarioAlteracao.usuarioADSM", FetchMode.JOIN);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
		
		hql.addProjection("new Map(tabelaFreteCarreteiro.idTabelaFreteCarreteiroCe", "idTabelaFreteCarreteiroCe");
		hql.addProjection("tabelaFreteCarreteiro.nrTabelaFreteCarreteiroCe", "nrTabelaFreteCarreteiroCe");
		hql.addProjection("tabelaFreteCarreteiro.tpOperacao", "tpOperacao");
		hql.addProjection("tabelaFreteCarreteiro.tpVinculo", "tpVinculo");
		hql.addProjection("tabelaFreteCarreteiro.dhVigenciaInicial", "dhVigenciaInicial");
		hql.addProjection("tabelaFreteCarreteiro.dhVigenciaFinal", "dhVigenciaFinal");
		hql.addProjection("tabelaFreteCarreteiro.dtAtualizacao", "dtAtualizacao");
		hql.addProjection("filial.pessoa.nmFantasia", "nmFantasia");
		hql.addProjection("filial.sgFilial", "sgFilial)");
		
		hql.addGroupBy("tabelaFreteCarreteiro.idTabelaFreteCarreteiroCe, tabelaFreteCarreteiro.nrTabelaFreteCarreteiroCe, tabelaFreteCarreteiro.tpOperacao, tabelaFreteCarreteiro.tpVinculo, tabelaFreteCarreteiro.dhVigenciaInicial, tabelaFreteCarreteiro.dhVigenciaFinal, tabelaFreteCarreteiro.dtAtualizacao, filial.pessoa.nmFantasia, filial.sgFilial ");	
		hql.addOrderBy("tabelaFreteCarreteiro.nrTabelaFreteCarreteiroCe DESC");
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}
	
	/**
	 * RowCount customizado para apenas trazer a parent table aos resultados da pesquisa.
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountCustom(TypedFlatMap criteria) {		
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
				
		hql.addProjection("COUNT(DISTINCT tabelaFreteCarreteiro)");
		
		Object count = getAdsmHibernateTemplate().findUniqueResult(hql.getSql(true), hql.getCriteria());
		
		// Safely cast to Long
		if(count != null){
			return (count instanceof Long ? ((Long) count).intValue() : 0);
		}
		
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findReportData(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
		
		hql.addProjection("new Map(tabelaFreteCarreteiro.idTabelaFreteCarreteiroCe", "idTabelaFreteCarreteiroCe");
		hql.addProjection("tabelaFreteCarreteiro.nrTabelaFreteCarreteiroCe", "nrTabelaFreteCarreteiroCe");
		hql.addProjection("tabelaFreteCarreteiro.tpOperacao", "tpOperacao");
		hql.addProjection("tabelaFreteCarreteiro.tpVinculo", "tpVinculo");
		hql.addProjection("tabelaFreteCarreteiro.dhVigenciaInicial", "dhVigenciaInicial");
		hql.addProjection("tabelaFreteCarreteiro.dhVigenciaFinal", "dhVigenciaFinal");
		hql.addProjection("tabelaFreteCarreteiro.dtAtualizacao", "dtAtualizacao");
		hql.addProjection("filialPessoa.nmFantasia", "nmFantasia");
		hql.addProjection("filial.sgFilial", "sgFilial");
		hql.addProjection("rotaColetaEntrega.nrRota", "nrRotaColetaEntrega");
		hql.addProjection("rotaColetaEntrega.dsRota", "dsRotaColetaEntrega");
		hql.addProjection("proprietarioPessoa.tpIdentificacao", "tpIdentificacaoProprietario");
		hql.addProjection("proprietarioPessoa.nrIdentificacao", "nrIdentificacaoProprietario");
		hql.addProjection("proprietarioPessoa.nmPessoa", "nmPessoaProprietario");
		hql.addProjection("clientePessoa.tpIdentificacao", "tpIdentificacaoCliente");
		hql.addProjection("clientePessoa.nrIdentificacao", "nrIdentificacaoCliente");
		hql.addProjection("clientePessoa.nmPessoa", "nmPessoaCliente");
		hql.addProjection("municipio.nmMunicipio", "nmMunicipio");
		hql.addProjection("tipoMeioTransporte.dsTipoMeioTransporte", "dsTipoMeioTransporte");
		hql.addProjection("meioTransporte.nrFrota", "nrFrotaMeioTransporte");
		hql.addProjection("meioTransporte.nrIdentificador", "nrIdentificadorMeioTransporte)");
		
		hql.addOrderBy("filial.sgFilial, tabelaFreteCarreteiro.nrTabelaFreteCarreteiroCe");
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}
	
	private SqlTemplate getHqlForFindPaginated(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
	
		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append(TabelaFreteCarreteiroCe.class.getName()).append(" AS tabelaFreteCarreteiro");
		hqlFrom.append(" INNER JOIN tabelaFreteCarreteiro.filial AS filial");
		hqlFrom.append(" LEFT JOIN filial.pessoa AS filialPessoa");
		hqlFrom.append(" LEFT JOIN tabelaFreteCarreteiro.listTabelaFcValores AS tabelaFcValores");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.rotaColetaEntrega AS rotaColetaEntrega");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.proprietario AS proprietario");
		hqlFrom.append(" LEFT JOIN proprietario.pessoa AS proprietarioPessoa");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.cliente AS cliente");
		hqlFrom.append(" LEFT JOIN cliente.pessoa AS clientePessoa");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.municipio AS municipio");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.tipoMeioTransporte AS tipoMeioTransporte");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.meioTransporte AS meioTransporte");		

		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("filial.idFilial","=", criteria.getLong("idFilial"));
		
		hql.addCriteria("tabelaFreteCarreteiro.nrTabelaFreteCarreteiroCe","=", criteria.getLong("nrTabelaFreteCarreteiroCe"));
		hql.addCriteria("tabelaFreteCarreteiro.tpVinculo","=", criteria.getString("tpVinculo"));
		hql.addCriteria("tabelaFreteCarreteiro.tpOperacao","=", criteria.getString("tpOperacao"));
		hql.addCriteria("tabelaFreteCarreteiro.idTabelaFreteCarreteiroCe","=", criteria.getLong("idTabelaFreteCarreteiroCe"));		
		hql.addCriteria("tabelaFreteCarreteiro.dtAtualizacao",">=", criteria.getYearMonthDay("dtAtualizacaoInicial"));
		hql.addCriteria("tabelaFreteCarreteiro.dtAtualizacao","<=", criteria.getYearMonthDay("dtAtualizacaoFinal"));
		hql.addCriteria("tabelaFreteCarreteiro.dhVigenciaInicial.value",">=", criteria.getDateTime("dhVigenciaInicial"));
		hql.addCriteria("tabelaFreteCarreteiro.dhVigenciaFinal.value","<=", criteria.getDateTime("dhVigenciaFinal"));
					
		if(criteria.getString("tpVigente") != null){
			String vigente = criteria.getString("tpVigente");
			
			if("V".equals(vigente)){
				hql.addCustomCriteria("((tabelaFreteCarreteiro.dhVigenciaInicial.value <= SYSDATE) AND (tabelaFreteCarreteiro.dhVigenciaFinal.value >= SYSDATE OR tabelaFreteCarreteiro.dhVigenciaFinal.value IS NULL))");
			} else if("N".equals(vigente)) {
				hql.addCustomCriteria("((tabelaFreteCarreteiro.dhVigenciaInicial.value > SYSDATE) OR (tabelaFreteCarreteiro.dhVigenciaInicial.value < SYSDATE AND tabelaFreteCarreteiro.dhVigenciaFinal.value < SYSDATE))");
			} else {
				hql.addCustomCriteria("(tabelaFreteCarreteiro.dhVigenciaInicial.value IS NULL AND tabelaFreteCarreteiro.dhVigenciaFinal.value IS NULL)");
			}
		}
				
		hql.addCriteria("tabelaFcValores.proprietario.idProprietario", "=", criteria.getLong("idProprietario"));		
		hql.addCriteria("tabelaFcValores.municipio.idMunicipio", "=", criteria.getLong("idMunicipio"));
		hql.addCriteria("tabelaFcValores.tipoMeioTransporte.idTipoMeioTransporte", "=", criteria.getLong("idTipoMeioTransporte"));
		hql.addCriteria("tabelaFcValores.cliente.idCliente", "=", criteria.getLong("idCliente"));
		hql.addCriteria("tabelaFcValores.meioTransporte.idMeioTransporte", "=", criteria.getLong("idMeioTransporte"));
		hql.addCriteria("tabelaFcValores.rotaColetaEntrega.idRotaColetaEntrega", "=", criteria.getLong("idRotaColetaEntrega"));
		
		return hql;
	}
	
	private SqlTemplate getHqlForFindTabelaFcValores(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(tabelaFcValores.idTabelaFcValores", "idTabelaFcValores");
		hql.addProjection("tabelaFreteCarreteiroCe.idTabelaFreteCarreteiroCe", "idTabelaFreteCarreteiroCe");
		hql.addProjection("tabelaFcValores.pcMercadoria", "pcMercadoria");
		hql.addProjection("tabelaFcValores.vlConhecimento", "vlConhecimento");
		hql.addProjection("tabelaFcValores.vlEvento", "vlEvento");
		hql.addProjection("tabelaFcValores.vlVolume", "vlVolume");
		hql.addProjection("tabelaFcValores.vlPalete", "vlPalete");
		hql.addProjection("tabelaFcValores.vlTransferencia", "vlTransferencia");
		hql.addProjection("tabelaFcValores.vlAjudante", "vlAjudante");
		hql.addProjection("tabelaFcValores.vlHora", "vlHora");
		hql.addProjection("tabelaFcValores.vlDiaria", "vlDiaria");
		hql.addProjection("tabelaFcValores.vlPreDiaria", "vlPreDiaria");
		hql.addProjection("tabelaFcValores.vlDedicado", "vlDedicado");
		hql.addProjection("tabelaFcValores.vlPernoite", "vlPernoite");
		hql.addProjection("tabelaFcValores.vlCapataziaCliente", "vlCapataziaCliente");
		hql.addProjection("tabelaFcValores.vlLocacaoCarreta", "vlLocacaoCarreta");
		hql.addProjection("tabelaFcValores.vlPremio", "vlPremio");
		hql.addProjection("tabelaFcValores.vlKmExcedente", "vlKmExcedente");
		hql.addProjection("tabelaFcValores.vlFreteMinimo", "vlFreteMinimo");
		hql.addProjection("tabelaFcValores.pcFrete", "pcFrete");
		hql.addProjection("tabelaFcValores.vlmercadoriaMinimo", "vlMercadoriaMinimo");
		hql.addProjection("tabelaFcValores.pcMercadoria", "pcMercadoria");
		hql.addProjection("tabelaFcValores.qtAjudante", "qtAjudante");
		hql.addProjection("tabelaFcValores.vlFreteMinimoLiq", "vlFreteMinimoLiq");
		hql.addProjection("tabelaFcValores.pcFreteLiq", "pcFreteLiq");
		hql.addProjection("cliente.idCliente", "idCliente");
		hql.addProjection("cpessoa.nmPessoa", "nmPessoaCliente");
		hql.addProjection("cpessoa.nrIdentificacao", "nrIdentificacaoCliente");
		hql.addProjection("cpessoa.tpIdentificacao", "tpIdentificacaoCliente");
		hql.addProjection("proprietario.idProprietario", "idProprietario");
		hql.addProjection("ppessoa.nmPessoa", "nmPessoaProprietario");
		hql.addProjection("ppessoa.nrIdentificacao", "nrIdentificacaoProprietario");
		hql.addProjection("ppessoa.tpIdentificacao", "tpIdentificacaoProprietario");
		hql.addProjection("rota.idRotaColetaEntrega", "idRotaColetaEntrega");
		hql.addProjection("rota.dsRota", "dsRota");
		hql.addProjection("meioTransporte.idMeioTransporte", "idMeioTransporte");
		hql.addProjection("meioTransporte.nrFrota", "nrFrota");
		hql.addProjection("meioTransporte.nrIdentificador", "nrIdentificador");
		hql.addProjection("tipoMeioTransporte.idTipoMeioTransporte", "idTipoMeioTransporte");
		hql.addProjection("tipoMeioTransporte.dsTipoMeioTransporte", "dsTipoMeioTransporte");
		hql.addProjection("municipio.idMunicipio", "idMunicipio");
		hql.addProjection("municipio.nmMunicipio", "nmMunicipio) ");
		
		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append(TabelaFcValores.class.getName()).append(" AS tabelaFcValores ");
		hqlFrom.append("INNER JOIN tabelaFcValores.tabelaFreteCarreteiroCe AS tabelaFreteCarreteiroCe");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.cliente AS cliente");
		hqlFrom.append(" LEFT JOIN cliente.pessoa AS cpessoa");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.proprietario AS proprietario");
		hqlFrom.append(" LEFT JOIN proprietario.pessoa AS ppessoa");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.rotaColetaEntrega AS rota");
		hqlFrom.append(" LEFT JOIN rota.filial AS frota");
		hqlFrom.append(" LEFT JOIN frota.pessoa AS frpessoa");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.municipio AS municipio");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.meioTransporte AS meioTransporte");
		hqlFrom.append(" LEFT JOIN tabelaFcValores.tipoMeioTransporte AS tipoMeioTransporte");

		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("tabelaFreteCarreteiroCe.idTabelaFreteCarreteiroCe","=", criteria.getLong("idTabelaFreteCarreteiroCe"));
		
		List<String> parans = new ArrayList<String>(); 
		if("CL".equals(criteria.getString("blTipo"))){
			parans.add("CD");
		}
		parans.add(criteria.getString("blTipo"));
		
		hql.addCriteriaIn("tabelaFcValores.blTipo", parans);
		
		return hql;
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findListTabelaFcValores(TypedFlatMap criteria, FindDefinition createFindDefinition) {
		SqlTemplate hql = this.getHqlForFindTabelaFcValores(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), createFindDefinition.getCurrentPage(), createFindDefinition.getPageSize(), hql.getCriteria());
	}

	public Integer getRowCountListTabelaFcValores(TypedFlatMap criteria) {
		SqlTemplate hql = this.getHqlForFindTabelaFcValores(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
	}
	
	private SqlTemplate getCommonProjectionTabelaFcValores(){
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(TabelaFcValores.class.getName()).append(" tabelaFcValores ");
		hqlFrom.append("INNER JOIN tabelaFcValores.tabelaFreteCarreteiroCe AS tabelaFreteCarreteiroCe ");
		hqlFrom.append("LEFT JOIN fetch tabelaFcValores.listTabelaFcFaixaPeso AS listTabelaFcFaixaPeso ");
		hqlFrom.append("LEFT JOIN fetch tabelaFcValores.cliente AS cliente ");
		hqlFrom.append("LEFT JOIN fetch cliente.pessoa AS cpessoa ");
		hqlFrom.append("LEFT JOIN fetch tabelaFcValores.proprietario AS proprietario ");
		hqlFrom.append("LEFT JOIN fetch proprietario.pessoa AS ppessoa ");
		hqlFrom.append("LEFT JOIN fetch tabelaFcValores.rotaColetaEntrega AS rotaColetaEntrega ");
		hqlFrom.append("LEFT JOIN fetch rotaColetaEntrega.filial AS rfilial ");
		hqlFrom.append("LEFT JOIN fetch rfilial.pessoa AS prfilial ");
		hqlFrom.append("LEFT JOIN fetch tabelaFcValores.municipio AS municipio ");
		hqlFrom.append("LEFT JOIN fetch municipio.unidadeFederativa AS unidadeFederativa ");
		hqlFrom.append("LEFT JOIN fetch unidadeFederativa.pais AS pais ");
		hqlFrom.append("LEFT JOIN fetch tabelaFcValores.meioTransporte AS meioTransporte ");
		hqlFrom.append("LEFT JOIN fetch tabelaFcValores.usuarioCriacao AS usuarioCriacao ");
		hqlFrom.append("LEFT JOIN fetch usuarioCriacao.usuarioADSM AS usuarioCriacaoADSM ");
				
		hql.addProjection("tabelaFcValores");
		hql.addFrom(hqlFrom.toString());
		
		return hql;
	}
	
	public TabelaFcValores findTabelaFcValoresGeral(Long idTabelaFreteCarreteiroCe) {
		SqlTemplate hql = getCommonProjectionTabelaFcValores();
		
		hql.addCriteria("tabelaFreteCarreteiroCe.idTabelaFreteCarreteiroCe", "=", idTabelaFreteCarreteiroCe);
		hql.addCustomCriteria("tabelaFcValores.blTipo = 'GE'");

		return (TabelaFcValores) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}	
	
	public TabelaFcValores findTabelaFcValores(Long id) {
		SqlTemplate hql = getCommonProjectionTabelaFcValores();
		
		hql.addCriteria("tabelaFcValores.idTabelaFcValores", "=", id);

		return (TabelaFcValores) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	
	public Boolean isTabelaFcValores(TabelaFcValores tabelaFcValores){
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(TabelaFcValores.class.getName()).append(" tabelaFcValores ");
		
		hql.addProjection("COUNT(tabelaFcValores)");
		hql.addFrom(hqlFrom.toString());
		hql.addCriteria("tabelaFcValores.idTabelaFcValores","!=", tabelaFcValores.getIdTabelaFcValores());
		hql.addCriteria("tabelaFreteCarreteiroCe.idTabelaFreteCarreteiroCe","=", tabelaFcValores.getTabelaFreteCarreteiroCe().getIdTabelaFreteCarreteiroCe());
		
		addIsTabelaFcValoresCriteria(hql, "proprietario", tabelaFcValores.getProprietario());
		addIsTabelaFcValoresCriteria(hql, "rotaColetaEntrega", tabelaFcValores.getRotaColetaEntrega());
		addIsTabelaFcValoresCriteria(hql, "meioTransporte", tabelaFcValores.getMeioTransporte());
		addIsTabelaFcValoresCriteria(hql, "tipoMeioTransporte", tabelaFcValores.getTipoMeioTransporte());
		addIsTabelaFcValoresCriteria(hql, "municipio", tabelaFcValores.getMunicipio());
		addIsTabelaFcValoresCriteria(hql, "cliente", tabelaFcValores.getCliente());
		
		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria()) > 0;
	}

	private void addIsTabelaFcValoresCriteria(SqlTemplate hql, String criteria, Object value){
		if(value == null){
			hql.addCustomCriteria(criteria + " IS NULL ");
		} else {
			hql.addCriteria(criteria,"=", value);
		}		
	}
	
	public Boolean isTabelaFreteCarreteiroCe(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(TabelaFreteCarreteiroCe.class.getName()).append(" tce ");
		
		hql.addProjection("COUNT(tce)");
		hql.addFrom(hqlFrom.toString());
		hql.addCriteria("tce.idTabelaFreteCarreteiroCe","!=", tabelaFreteCarreteiroCe.getIdTabelaFreteCarreteiroCe());
		hql.addCriteria("tce.filial","=", tabelaFreteCarreteiroCe.getFilial());
		hql.addCriteria("tce.tpOperacao","=", tabelaFreteCarreteiroCe.getTpOperacao());
		hql.addCriteria("tce.tpVinculo","=", tabelaFreteCarreteiroCe.getTpVinculo());
		
		addCustomCriteriaDiaSemana(hql, tabelaFreteCarreteiroCe);		
		addCustomCriteriaVigencia(hql, tabelaFreteCarreteiroCe);
		
		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria()) > 0;
	}

	/**
	 * Monta a query de seleção de acordo com a vigência informada.
	 * 
	 * @param tabelaFreteCarreteiroCe
	 * @param hql
	 */
	private void addCustomCriteriaVigencia(SqlTemplate hql,
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		DateTime dhVigenciaInicial = tabelaFreteCarreteiroCe.getDhVigenciaInicial();
		DateTime dhVigenciaFinal = tabelaFreteCarreteiroCe.getDhVigenciaFinal();
		
		/* Quando data final for nula. */
		StringBuilder dateRestriction = new StringBuilder();				
		dateRestriction.append("((? >= tce.dhVigenciaInicial.value OR ? <= tce.dhVigenciaInicial.value) AND (tce.dhVigenciaFinal.value IS NULL OR tce.dhVigenciaFinal.value >= ?))");		
		hql.addCriteriaValue(dhVigenciaInicial);
		hql.addCriteriaValue(dhVigenciaInicial);
		hql.addCriteriaValue(dhVigenciaInicial);
		
		/* Quando data final não for nula. */
		if(dhVigenciaFinal != null){
			dateRestriction.append("AND (? >= tce.dhVigenciaInicial.value OR ? >= tce.dhVigenciaFinal.value)");
			hql.addCriteriaValue(dhVigenciaFinal);
			hql.addCriteriaValue(dhVigenciaFinal);
		}
		
		hql.addCustomCriteria(dateRestriction.toString());
	}
	
	/**
	 * Monta a query de seleção de dias da semana de acordo com os indicadores
	 * informados, para restringir duplicidade do cadastro.
	 * 
	 * @param hql
	 * @param tabelaFreteCarreteiroCe
	 */
	private void addCustomCriteriaDiaSemana(SqlTemplate hql, TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe){
		StringBuilder criteria = new StringBuilder();
		
		addCriteria(criteria, "blSegunda", tabelaFreteCarreteiroCe.getBlSegunda());
		addCriteria(criteria, "blTerca", tabelaFreteCarreteiroCe.getBlTerca());
		addCriteria(criteria, "blQuarta", tabelaFreteCarreteiroCe.getBlQuarta());
		addCriteria(criteria, "blQuinta", tabelaFreteCarreteiroCe.getBlQuinta());
		addCriteria(criteria, "blSexta", tabelaFreteCarreteiroCe.getBlSexta());
		addCriteria(criteria, "blSabado", tabelaFreteCarreteiroCe.getBlSabado());
		addCriteria(criteria, "blDomingo", tabelaFreteCarreteiroCe.getBlDomingo());
		
		/*
		 * Caso exista alguma condição informada, adiciona a query principal.
		 */
		if(criteria.length() > 0){		
			criteria.append(")");
			hql.addCustomCriteria(criteria.toString());	
		}
	}
	
	/**
	 * Adiciona condição apenas para domains informados como 'S'.
	 * 
	 * @param criteria
	 * @param column
	 * @param domainValue
	 */
	private void addCriteria(StringBuilder criteria, String column, DomainValue domainValue){
		if(domainValue == null || "N".equals(domainValue.getValue())){
			return;
		}

		if(criteria.length() == 0){
			criteria.append("( ");
		} else {
			criteria.append("OR ");
		}
		
		/* Por exemplo constrói: OR tce.blSegunda = 'S' */
		criteria.append("tce.");
		criteria.append(column);
		criteria.append("=");
		criteria.append("'S'");
	}
	
	public void removeTabelaValoresByIds(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + TabelaFcValores.class.getName() + " WHERE idTabelaFcValores IN (:id)", ids);
	}

	public void removeTabelaFcFaixaPesoByIds(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + TabelaFcFaixaPeso.class.getName() + " WHERE tabelaFcValores.idTabelaFcValores IN (:id)", ids);		
	}

	@SuppressWarnings("unchecked")
	public List<TabelaFcValores> findByIdFilial(Long idFilial,  String[] tipoOperacao) {
		DateTime hoje = JTDateTimeUtils.getDataHoraAtual();

		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder()
		.append(TabelaFcValores.class.getName()).append(" tabelaFcValores ");
		hqlFrom.append("INNER JOIN tabelaFcValores.tabelaFreteCarreteiroCe AS tabelaFreteCarreteiroCe ");
		hql.addProjection("tabelaFcValores");
		hql.addFrom(hqlFrom.toString());
		hql.addCustomCriteria("tabelaFreteCarreteiroCe.filial.id = ? ");
		hql.addCriteriaValue(idFilial);
		hql.addCriteria("tabelaFreteCarreteiroCe." + getBlSemana(hoje.dayOfWeek().get()), "=", "S");
		hql.addCriteriaIn("tabelaFreteCarreteiroCe.tpOperacao", tipoOperacao);
		hql.addCustomCriteria(" (tabelaFreteCarreteiroCe.dhVigenciaInicial.value <= ? and (tabelaFreteCarreteiroCe.dhVigenciaFinal.value >= ? or nvl(TO_CHAR(tabelaFreteCarreteiroCe.dhVigenciaFinal.value,'yyyy-MM-dd'), '1900-01-01') = '1900-01-01')) ");
		hql.addCriteriaValue(hoje);
		hql.addCriteriaValue(hoje);

		return (List<TabelaFcValores>) getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

	private String getBlSemana(int diaSemana) {
		   switch (diaSemana) {
	        case DateTimeConstants.MONDAY: //Segunda-feira
	           return "blSegunda";
	        case DateTimeConstants.TUESDAY: //Terca-feira
	            return "blTerca";
	        case DateTimeConstants.WEDNESDAY: //Quarta-feira
	        	return "blQuarta";
	        case DateTimeConstants.THURSDAY: //Quinta-feira
	        	return "blQuinta";
	        case DateTimeConstants.FRIDAY: //Sexta-feira
	        	return "blSexta";
	        case DateTimeConstants.SATURDAY: //Sábado
	        	return "blSabado";
	        case DateTimeConstants.SUNDAY: //Domingo
	        	return "blDomingo"; 
	        default:
	            return "blDomingo";
	        }
	}

	private void executeDeleteTabelaFreteCarreteiroCe(Long idTabelaFreteCarreteiroCe){
		StringBuilder hql = new StringBuilder();					
		hql.append("DELETE " + TabelaFreteCarreteiroCe.class.getName() + " tfc");
		hql.append(" WHERE tfc.idTabelaFreteCarreteiroCe = :id");
		
		getAdsmHibernateTemplate().removeById(hql.toString(), idTabelaFreteCarreteiroCe);
	}
	
	private void executeDeleteTabelaFcValores(Long idTabelaFreteCarreteiroCe){
		StringBuilder hql = new StringBuilder();					
		hql.append("DELETE " + TabelaFcValores.class.getName() + " tfv");
		hql.append(" WHERE tfv.tabelaFreteCarreteiroCe.idTabelaFreteCarreteiroCe = :id");
				
		getAdsmHibernateTemplate().removeById(hql.toString(), idTabelaFreteCarreteiroCe);
	}
	
	private void executeDeleteTabelaFcFaixaPeso(Long idTabelaFreteCarreteiroCe){
		StringBuilder hql = new StringBuilder();					
		hql.append("DELETE " + TabelaFcFaixaPeso.class.getName() + " tfp");
		hql.append(" WHERE tfp.tabelaFcValores IN");
		hql.append(" (SELECT tfv FROM "+ TabelaFcValores.class.getName() + " tfv WHERE tfv.tabelaFreteCarreteiroCe.idTabelaFreteCarreteiroCe = :id)");
				
		getAdsmHibernateTemplate().removeById(hql.toString(), idTabelaFreteCarreteiroCe);		
	}
	
	/**
	 * Remove uma tabela de frete carreteiro padrão com todas as suas tabelas de
	 * valores relacionadas.
	 * 
	 * @param idTabelaFreteCarreteiroCe
	 */
	public void removeCascadeById(Long idTabelaFreteCarreteiroCe) {
		executeDeleteTabelaFcFaixaPeso(idTabelaFreteCarreteiroCe);
		executeDeleteTabelaFcValores(idTabelaFreteCarreteiroCe);
		executeDeleteTabelaFreteCarreteiroCe(idTabelaFreteCarreteiroCe);		
	}	
	
	@SuppressWarnings("unchecked")
	public List<TabelaFcValores> findListTabelaFcValores(Long idTabelaFreteCarreteiroCe) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT tabelaFcValores ");
		hql.append(" FROM TabelaFcValores AS tabelaFcValores");
		hql.append(" WHERE tabelaFcValores.tabelaFreteCarreteiroCe.idTabelaFreteCarreteiroCe = ?");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{ idTabelaFreteCarreteiroCe });
	}
	
	@SuppressWarnings("unchecked")
	public List<TabelaFcFaixaPeso> findListTabelaFcFaixaPeso(Long idTabelaFcValores) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT tabelaFcFaixaPeso ");
		hql.append(" FROM TabelaFcFaixaPeso AS tabelaFcFaixaPeso");
		hql.append(" WHERE tabelaFcFaixaPeso.tabelaFcValores.idTabelaFcValores = ?");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{ idTabelaFcValores });
	}
	
	@SuppressWarnings("rawtypes")
	public void removeByIdsAnexoTabelaFreteCe(List ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + AnexoTabelaFreteCe.class.getName() + " WHERE idAnexoTabelaFreteCe IN (:id)", ids);
	}

	public AnexoTabelaFreteCe findAnexoTabelaFreteCeById(Long idAnexoTabelaFreteCe) {
		return (AnexoTabelaFreteCe) getAdsmHibernateTemplate().load(AnexoTabelaFreteCe.class, idAnexoTabelaFreteCe);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAnexoTabelaFreteCeByIdTabelaFreteCarreteiro(Long idTabelaFreteCarreteiro) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoTabelaFreteCe AS idAnexoTabelaFreteCe,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.dsAnexo AS dsAnexo,");
		hql.append(" anexo.dhCriacao AS dhCriacao,");
		hql.append(" usuario.usuarioADSM.nmUsuario AS nmUsuario)");
		hql.append(" FROM AnexoTabelaFreteCe AS anexo");
		hql.append("  INNER JOIN anexo.tabelaFreteCarreteiroCe tabelaFreteCarreteiroCe");
		hql.append("  INNER JOIN anexo.usuarioLMS usuario");
		hql.append(" WHERE tabelaFreteCarreteiroCe.idTabelaFreteCarreteiroCe = ?");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idTabelaFreteCarreteiro});
	}
	
	public Integer getRowCountAnexoTabelaFreteCeByIdTabelaFreteCarreteiro(Long idTabelaFreteCarreteiro) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_tabela_frete_ce WHERE id_anexo_tabela_frete_ce = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{ idTabelaFreteCarreteiro });
	}
}