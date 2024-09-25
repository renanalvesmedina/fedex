package com.mercurio.lms.tabelaprecos.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.tabelaprecos.model.CloneClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPrecoEntity;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;


public class ReajusteTabelaPrecoDAO extends BaseCrudDao<ReajusteTabelaPrecoEntity, Long> {

	private JdbcTemplate jdbcTemplate;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	protected final Class getPersistentClass() {
		return ReajusteTabelaPrecoEntity.class;
	}

	public List<Map<String,Object>> find(Map criteria) {
		PreparedStatementDTO dto = whereFind(criteria);
		String where = dto.getQueryString();
		return jdbcTemplate.query(queryFind(where), dto.getParameters(), reajusteTabelaPrecoRowMapper());
	}
	
	public List<Map<String,Object>> listParcelas(Long idTabelaBase) {
		return jdbcTemplate.query(queryListParcelas(), new Object[]{idTabelaBase},  listParcelasRowMapper());
	}
		
	public Map<String,Object> findReajusteById(Long id){
		return (Map<String, Object>) jdbcTemplate.queryForObject(queryFindById(), new Object[]{id}, reajusteTabelaPrecoFindByIdRowMapper());
	}
	
	public Map<String,Object> findReajusteByIdTabelaNova(Long idTabelaNova) {
		return (Map<String, Object>) jdbcTemplate.queryForObject(queryFindByIdTabelaNova(), new Object[]{idTabelaNova}, reajusteTabelaPrecoFindByIdTabelaNovaRowMapper());
	}

	public List<Map<String, Object>> findSuggestTabelaBase(String subString) {
		return jdbcTemplate.query(getQueryTabelaPrecoSuggest(), new Object[]{subString.toUpperCase()+"%"}, reajusteTabelaPrecoSuggestTabelaBaseRowMapper());
	}
	
	public List<Map<String, Object>> findSuggestTabelaNova(String subString) {
		return jdbcTemplate.query(getQueryTabelaPrecoSuggest(), new Object[]{subString.toUpperCase()+"%"}, reajusteTabelaPrecoSuggestTabelaNovaRowMapper());
	}

	public List<Map<String, Object>> findSuggestTabelaNovaEfetivada(String subString) {
		return jdbcTemplate.query(getQueryTabelaPrecoEfetivadaSuggest(), new Object[]{subString.toUpperCase()+"%"}, reajusteTabelaPrecoSuggestTabelaNovaRowMapper());
	}
	
	public Long getIdTipoTabelaPreco(String tipoTabelaPreco, String versao){
		return (Long) jdbcTemplate.query(queryTipoTabelaPreco(), new Object[]{tipoTabelaPreco, versao}, getLong());
	}
		
	public Long getIdSubTipoTabelaPreco(String subtipoTabelaPreco){
		return (Long) jdbcTemplate.query(querySubTipoTabelaPreco(), new Object[]{subtipoTabelaPreco}, getLong());
	}
	
	public Long getMaxNrReajuste(Long idFilial){
		return (Long) jdbcTemplate.query(queryGetMaxNrReajuste(), new Object[]{idFilial}, getLong());
	}
	
	public Long getIdTabelaPreco(Long idTipoTabelaPreco, Long idSubtipoTabelaPreco){
		return (Long) jdbcTemplate.query(queryIdTabelaPreco(), new Object[]{idTipoTabelaPreco, idSubtipoTabelaPreco}, getLong());
	}
	
	public Long executeCountDataAgendamento(Long idReajusteTabelaPreco, Long idTabelaPrecoBase){
		StringBuilder sql = new StringBuilder().append(" select count(1) from reajuste_tabela_preco ")
											   .append(" where dt_agendamento is not null ")
											   .append("   and id_tabela_preco_base =  ").append(idTabelaPrecoBase);
				
		if(idReajusteTabelaPreco != null){		
			sql.append(" and id_reajuste_tabela_preco <> ").append(idReajusteTabelaPreco);
		}
				
		return (Long) jdbcTemplate.query(sql.toString(), getLong());
	}
	
	public TabelaPreco findTabelaPreco(Long idTabelaBase) {
		return (TabelaPreco) jdbcTemplate.queryForObject(queryTabelaPreco(), new Object[]{ idTabelaBase }, tabelaPrecoRowMapper());
	}
	
	private String queryTabelaPreco(){
		return new StringBuilder()
			.append(" select PC_DESCONTO_FRETE_MINIMO, PS_MINIMO, TP_CALCULO_FRETE_PESO, DS_DESCRICAO, ")
			.append("        TP_TARIFA_REAJUSTE, TP_CALCULO_PEDAGIO, TP_CATEGORIA, TP_SERVICO, DT_VIGENCIA_INICIAL, DT_VIGENCIA_FINAL, BL_ICMS_DESTACADO ")
			.append(" from TABELA_PRECO where id_tabela_preco = ? ")
			.toString();
	}
	
	private String queryIdTabelaPreco(){
		return new StringBuilder().append(" select id_tabela_preco from tabela_preco where id_tipo_tabela_preco = ? and id_subtipo_tabela_preco = ? ").toString();
	}
	
	private String querySubTipoTabelaPreco(){
		return new StringBuilder().append(" select id_subtipo_tabela_preco from subtipo_tabela_preco  where tp_subtipo_tabela_preco = ? ").toString();
	}
	
	private String queryTipoTabelaPreco(){
		return new StringBuilder().append(" select id_tipo_tabela_preco from tipo_tabela_preco ttp where ttp.tp_tipo_tabela_preco = ? and ttp.nr_versao = ? ").toString();
	}
	
	private String queryGetMaxNrReajuste(){
		return new StringBuilder().append(" select MAX(nr_reajuste) from reajuste_tabela_preco where id_filial = ? ").toString();
	}
	
	private String queryFindById(){
		return new StringBuilder()
		.append(" select fl.sg_filial, usr.nm_usuario, ttp.tp_tipo_tabela_preco as tipo, ttp.nr_versao, stp.tp_subtipo_tabela_preco as sub_tipo, ")
		.append("        rtp.id_reajuste_tabela_preco, rtp.nr_reajuste , rtp.pc_reajuste_geral, rtp.dt_geracao, rtp.dt_vigencia_inicial, ")
		.append("        rtp.dt_vigencia_final, rtp.bl_efetivado, rtp.id_tabela_preco_base, rtp.dt_agendamento, fl.id_filial, rtp.id_tabela_nova, rtp.dt_efetivacao,  ")
		.append("        ( ").append(queryTabelaPreco("rtp.id_tabela_preco_base")).append(" ) as tab_base, rtp.bl_fecha_vigencia_tabela_base ")
		.append(" from tabela_preco tp ")
		.append("	   inner join reajuste_tabela_preco rtp on tp.id_tabela_preco = rtp.id_tabela_nova ")
		.append("      inner join tipo_tabela_preco ttp on ttp.id_tipo_tabela_preco = tp.id_tipo_tabela_preco ")
		.append("      inner join subtipo_tabela_preco stp on stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco ")
		.append("      inner join usuario usr on usr.id_usuario = rtp.id_usuario ")
		.append("      inner join filial fl on fl.id_filial = rtp.id_filial ")
		.append(" where      id_reajuste_tabela_preco = ? ")
		.toString();
	}
	
	private String queryFindByIdTabelaNova() {
		return new StringBuilder()
			.append(" SELECT bl_fecha_vigencia_tabela_base, id_tabela_preco_base ")
			.append("   FROM reajuste_tabela_preco ")
			.append("  WHERE id_tabela_nova = ? ")
			.toString();
	}
	
	private ResultSetExtractor getLong() {
		return new ResultSetExtractor() {			
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next() ? rs.getLong(1) : null;
			}
		};	
	}
	
	private RowMapper reajusteTabelaPrecoFindByIdRowMapper() {
		return new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap();
				map.put("filial", resultSet.getString(1));
				map.put("usuario", resultSet.getString(2));
				map.put("tipo", resultSet.getString(3));
				map.put("versao", resultSet.getString(4));
				map.put("subTipo", resultSet.getString(5));
				map.put("id", resultSet.getLong(6));
				map.put("nrReajuste", resultSet.getLong(7));
				map.put("percentualReajusteGeral", resultSet.getString(8));
				map.put("dtGeracao", resultSet.getDate(9) != null ? new YearMonthDay(resultSet.getDate(9).getTime()) : null);
				map.put("dtVigenciaInicial", resultSet.getDate(10) != null ? new YearMonthDay(resultSet.getDate(10).getTime()) : null);
				map.put("dtVigenciaInicialDate", resultSet.getDate(10));
				map.put("dtVigenciaFinal", resultSet.getDate(11) != null ? new YearMonthDay(resultSet.getDate(11).getTime()) : null);
				map.put("efetivado", "S".equals(resultSet.getString(12)));
				map.put("idTabelaBase", resultSet.getLong(13));
				map.put("dtAgendamento", resultSet.getDate(14) != null ? new YearMonthDay(resultSet.getDate(14).getTime()) : null);
				map.put("idFilial", resultSet.getLong(15));
				map.put("idTabelaNova", resultSet.getLong(16));
				map.put("dtEfetivacao", resultSet.getDate(17) != null ? new YearMonthDay(resultSet.getDate(17).getTime()) : null);
				map.put("tabelaBase", resultSet.getString(18));
				map.put("nrReajusteFilial", resultSet.getString(1) + " - " + resultSet.getString(7));
				map.put("fechaVigenciaTabelaBase", "S".equals(resultSet.getString(19)));
				
				return map;
			}
		};
	}

	private RowMapper reajusteTabelaPrecoFindByIdTabelaNovaRowMapper() {
		return new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<>();
				map.put("fechaVigenciaTabelaBase", "S".equals(resultSet.getString(1)));
				map.put("idTabelaBase", resultSet.getString(2));
				return map;
			}
		};
	}
	
	private RowMapper tabelaPrecoRowMapper() {
		return new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				TabelaPreco tabPreco = new TabelaPreco();
				tabPreco.setPcDescontoFreteMinimo(resultSet.getBigDecimal(1));
				tabPreco.setPsMinimo(resultSet.getBigDecimal(2));
				tabPreco.setTpCalculoFretePeso(resultSet.getString(3) != null ? new DomainValue(resultSet.getString(3)) : null);
				tabPreco.setDsDescricao(resultSet.getString(4));
				tabPreco.setTpTarifaReajuste(resultSet.getString(5) != null ? new DomainValue(resultSet.getString(5)) : null);
				tabPreco.setTpCalculoPedagio(resultSet.getString(6) != null ? new DomainValue(resultSet.getString(6)) : null);
				tabPreco.setTpCategoria(resultSet.getString(7) != null ? new DomainValue(resultSet.getString(7)) : null);
				tabPreco.setTpServico(resultSet.getString(8) != null ? new DomainValue(resultSet.getString(8)) : null);
				tabPreco.setDtVigenciaInicial(resultSet.getDate(9) != null ? new YearMonthDay(resultSet.getDate(9).getTime())  : null);
				tabPreco.setDtVigenciaFinal(resultSet.getDate(10) != null ? new YearMonthDay(resultSet.getDate(10).getTime())  : null);
				tabPreco.setBlIcmsDestacado(resultSet.getString(11) != null ? resultSet.getString(11).equals("S")  : null);
				return tabPreco;
			}
		};
	}
	
	private RowMapper reajusteTabelaPrecoSuggestTabelaNovaRowMapper() {
		return new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("tabelaPreco", resultSet.getString(1));
				map.put("idTabelaPreco", resultSet.getLong(2));
				return map;
			}
		};
	}
	
	private RowMapper reajusteTabelaPrecoSuggestTabelaBaseRowMapper() {
		return new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("idTabelaPreco", resultSet.getLong(2));
				String dtVigenciaInicial = resultSet.getDate(3) != null ? 
						JTDateTimeUtils.formatDateYearMonthDayToString(new YearMonthDay(resultSet.getDate(3).getTime()))  
						: null;
				String dtVigenciaFinal = resultSet.getDate(4) != null ? 
						JTDateTimeUtils.formatDateYearMonthDayToString(new YearMonthDay(resultSet.getDate(4).getTime())) 
						: null;
				String efetivado = "S".equals(resultSet.getString(5))?"Efetivado":"";
				
				StringBuilder tabelaPrecoStr = new StringBuilder()
				.append(resultSet.getString(1))
				.append(" (")
				.append(dtVigenciaInicial);
				if(dtVigenciaFinal!=null){
					tabelaPrecoStr
					.append("-")
					.append(dtVigenciaFinal);
				}
				tabelaPrecoStr
				.append(") ")
				.append(efetivado);
				
				map.put("tabelaPreco", tabelaPrecoStr.toString());
				return map;
			}
		};
	}
	
	private String queryFind(String clausulaWhere){
		
		return new StringBuilder().append(" select * from  ( ").append(queryReajusteTabelaPreco()).append(" ) where 1=1 ").append(clausulaWhere).toString();
	}
	
	private String queryReajusteTabelaPreco(){

		String idTabelaBase = "rtp.id_tabela_preco_base";
		String idTabelaNova = "rtp.id_tabela_nova";
		
		return new StringBuilder()
		.append(" select rtp.id_reajuste_tabela_preco, rtp.nr_reajuste , rtp.pc_reajuste_geral, rtp.dt_geracao, ")
		.append("        rtp.dt_vigencia_inicial, rtp.dt_vigencia_final, rtp.bl_efetivado, rtp.id_filial, fl.sg_filial, ")
		.append("       ( ").append(queryTabelaPreco(idTabelaBase)).append(" ) as tab_base, ") 
		.append("       ( ").append(queryIdTabelaPreco(idTabelaBase)).append(" ) as id_tab_base, ") 
		.append("       ( ").append(queryTabelaPreco(idTabelaNova)).append(" ) as tab_nova, ")
		.append("       ( ").append(queryIdTabelaPreco(idTabelaNova)).append(" ) as id_tab_nova ")
		.append(" from reajuste_tabela_preco rtp ")
		.append("      inner join filial fl on fl.id_filial = rtp.id_filial ")
		.toString();
	}
	
	private String queryTabelaPreco(String column){
		return new StringBuilder()
		.append(" select ttp.tp_tipo_tabela_preco || ttp.nr_versao || '-' || stp.tp_subtipo_tabela_preco as tabela_preco ")
		.append("    from tabela_preco tp, tipo_tabela_preco ttp, subtipo_tabela_preco stp ")
		.append("    where tp.id_tabela_preco = ").append(column)
		.append("    and   ttp.id_tipo_tabela_preco = tp.id_tipo_tabela_preco ")
		.append("    and   stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco ")
		.toString();
	}
	
	private String queryIdTabelaPreco(String column){
		return new StringBuilder()
		.append(" select  tp.id_tabela_preco")
		.append("    from tabela_preco tp, tipo_tabela_preco ttp, subtipo_tabela_preco stp ")
		.append("    where tp.id_tabela_preco = ").append(column)
		.append("    and   ttp.id_tipo_tabela_preco = tp.id_tipo_tabela_preco ")
		.append("    and   stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco ")
		.toString();
	}
	
	private String queryListParcelas(){
		return new StringBuilder()
		.append(" select")
		.append("	SUBSTR(REGEXP_SUBSTR(  parcela_preco.NM_PARCELA_PRECO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  parcela_preco.NM_PARCELA_PRECO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' (' || ")
		.append("   SUBSTR(REGEXP_SUBSTR(  vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ')' parcela_preco, ")
		.append("	tabela_preco_parcela.id_tabela_preco_parcela ")
		.append(" from tabela_preco_parcela, parcela_preco, dominio d, valor_dominio vd") 
		.append(" where tabela_preco_parcela.ID_TABELA_PRECO = ? ")
		.append("   and   d.nm_dominio = 'DM_TIPO_PARCELA' ")
		.append("   and   d.id_dominio = vd.id_dominio ") 
		.append("   and   vd.vl_valor_dominio = tp_parcela_preco ")
		.append("   and   parcela_preco.ID_PARCELA_PRECO = tabela_preco_parcela.ID_PARCELA_PRECO ")
		.append(" order by parcela_preco.id_parcela_preco ")
		.toString();
	}

	private String getQueryTabelaPrecoSuggest() {
		return new StringBuilder()
		.append(" select * from ( ")
		.append(" select ttp.tp_tipo_tabela_preco || ttp.nr_versao || '-' || stp.tp_subtipo_tabela_preco as tabela_preco, tp.id_tabela_preco, ")
		.append("    tp.DT_VIGENCIA_INICIAL, tp.DT_VIGENCIA_FINAL, tp.BL_EFETIVADA ")
		.append("    from tabela_preco tp, tipo_tabela_preco ttp, subtipo_tabela_preco stp ")
		.append("    where ttp.id_tipo_tabela_preco = tp.id_tipo_tabela_preco")
		.append("    and   stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco ")
		.append(" ) where upper(tabela_preco) like ? ")
		.toString();
	}
	
	private String getQueryTabelaPrecoEfetivadaSuggest() {
		return new StringBuilder()
		.append(" select * from ( ")
		.append(" select ttp.tp_tipo_tabela_preco || ttp.nr_versao || '-' || stp.tp_subtipo_tabela_preco as tabela_preco, tp.id_tabela_preco, ")
		.append("    tp.DT_VIGENCIA_INICIAL, tp.DT_VIGENCIA_FINAL, tp.BL_EFETIVADA ")
		.append("    from tabela_preco tp, tipo_tabela_preco ttp, subtipo_tabela_preco stp ")
		.append("    where ttp.id_tipo_tabela_preco = tp.id_tipo_tabela_preco")
		.append("    and   stp.id_subtipo_tabela_preco = tp.id_subtipo_tabela_preco ")
		.append("    and   tp.bl_efetivada = 'S' ")
		.append(" ) where upper(tabela_preco) like ? ")
		.toString();
	}
	
	private RowMapper reajusteTabelaPrecoRowMapper() {
		return new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap();
				map.put("id", resultSet.getLong(1));
				map.put("nrReajuste", resultSet.getLong(2));
				map.put("percentualReajusteGeral", resultSet.getBigDecimal(3));
				map.put("dtGeracao", resultSet.getDate(4) != null ? sdf.format(resultSet.getDate(4)) : "");
				map.put("dtVigenciaInicial", resultSet.getDate(5) != null ? sdf.format(resultSet.getDate(5)) : "");
				map.put("dtVigenciaFinal", resultSet.getDate(6) != null ? sdf.format(resultSet.getDate(6)) : "");
				map.put("efetivado", "S".equals(resultSet.getString(7)));
				map.put("idFilial", resultSet.getLong(8));
				map.put("filial", resultSet.getString(9));
				map.put("tabBase", resultSet.getString(10));
				map.put("tabNova", resultSet.getString(12));
				return map;
			}
		};
	}
	
	private RowMapper listParcelasRowMapper() {
		return new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap();
				map.put("desc", resultSet.getString(1));
				map.put("id", resultSet.getLong(2));
				return map;
			}
		};
	}
	
	private PreparedStatementDTO whereFind(Map criteria){
		StringBuilder where = new StringBuilder();
		List parameters = new ArrayList();
		
		if(containValue(criteria, "id")){
			where.append(" and id_reajuste_tabela_preco = ?");
			parameters.add(criteria.get("id"));
		}

		if(containValue(criteria, "tabelaBase.idTabelaPreco")){
			where.append(" and id_tab_base = ?");
			parameters.add(criteria.get("tabelaBase.idTabelaPreco"));
		}
		if(containValue(criteria, "tabelaNova.idTabelaPreco")){
			where.append(" and id_tab_nova = ?");
			parameters.add(criteria.get("tabelaNova.idTabelaPreco"));
		}
	
		if(containValue(criteria, "dtGeracaoInicial")){
			where.append(" and dt_geracao >= TO_DATE('")
					.append(JTFormatUtils.format(new YearMonthDay(criteria.get("dtGeracaoInicial").toString())))
					.append(" 00:00:00', 'DD/MM/YYYY HH24:MI:SS')");
		}
		
		if(containValue(criteria, "dtGeracaoFinal")){ 
			where.append(" and dt_geracao <= TO_DATE('")
					.append(JTFormatUtils.format(new YearMonthDay(criteria.get("dtGeracaoFinal").toString())))
					.append(" 23:59:59', 'DD/MM/YYYY HH24:MI:SS')");
		}
		
		if(containValue(criteria, "nrReajuste")){
			where.append(" and nr_reajuste = ?");
			parameters.add(criteria.get("nrReajuste"));
		}
		
		if(containValue(criteria, "filial.idFilial")){
			where.append(" and id_filial = ?");
			parameters.add(criteria.get("filial.idFilial"));
		}
		PreparedStatementDTO resultMap = new PreparedStatementDTO(where.toString(), parameters.toArray());
		
		return resultMap;
	}
	
	public boolean containValue(Map criteria, String key){
		return criteria.get(key) != null && StringUtils.isNotBlank(criteria.get(key).toString());
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public YearMonthDay efetivarReajuste(Long idReajuste) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		
		EfetivarReajuste efetivarReajuste = new EfetivarReajuste();
		String sql = efetivarReajuste.mountSql();
		Map<String, Object> params = efetivarReajuste.params(idReajuste,dataAtual);
		getAdsmHibernateTemplate().executeUpdateBySql(sql, params);
		
		return dataAtual;
		
	}
	static class EfetivarReajuste{
		private Map<String, Object> params( Long idReajusteTabela, YearMonthDay dataAtual) {
			Map<String, Object> namedParams = new HashMap<String, Object>();
			namedParams.put("idTabela", idReajusteTabela);
			namedParams.put("dataAtual", dataAtual);
			return namedParams;
		}

		private String mountSql() {

			StringBuilder sqlFindParcelas = new StringBuilder();

			sqlFindParcelas.append(" update REAJUSTE_TABELA_PRECO set");
			sqlFindParcelas.append(" BL_EFETIVADO = 'S', ");
			sqlFindParcelas.append(" DT_EFETIVACAO = :dataAtual ");
			sqlFindParcelas.append(" where ID_REAJUSTE_TABELA_PRECO = :idTabela ");

			return sqlFindParcelas.toString();
		}
	}
	
	public List<CloneClienteAutomaticoDTO> findTabelasDivisaoClienteAutomaticosParaClonarNovoReajuste(){

		QueryTabelasDivisaoCliente parcelasReajusteTabela = new QueryTabelasDivisaoCliente();

		String query = parcelasReajusteTabela.mountSqlNovoReajuste();

		Map<String, Object> namedParams = new HashMap<String, Object>();

		return (List<CloneClienteAutomaticoDTO>) getAdsmHibernateTemplate().findBySql(query, namedParams, 
				parcelasReajusteTabela.configureSqlQueryNovoReajuste(), new AliasToBeanResultTransformer(CloneClienteAutomaticoDTO.class));
	}

	public List<CloneClienteAutomaticoDTO> findTabelasDivisaoClienteAutomaticosParaClonar(){

		QueryTabelasDivisaoCliente parcelasReajusteTabela = new QueryTabelasDivisaoCliente();

		String query = parcelasReajusteTabela.mountSql();

		Map<String, Object> namedParams = new HashMap<String, Object>();

		return (List<CloneClienteAutomaticoDTO>) getAdsmHibernateTemplate().findBySql(query, namedParams, 
				parcelasReajusteTabela.configureSqlQuery(), new AliasToBeanResultTransformer(CloneClienteAutomaticoDTO.class));
	}
	
	static class QueryTabelasDivisaoCliente {
		
		private String mountSqlNovoReajuste() {
			return new StringBuilder().
			append(" select tdc.id_tabela_divisao_cliente as idTabelaDivisaoCliente, ").
			append(" rdc.dt_agendamento_reajuste as dataVigenciaInicial, ").
			append(" rtp.id_reajuste_tabela_preco as idReajusteTabelaPreco, ").
			append(" rtp.id_tabela_nova as idTabelaNova, ").
			append(" rdc.id_reajuste_divisao_cliente as idReajusteDivisaoCliente ").
			append(" from reajuste_divisao_cliente rdc, tabela_divisao_cliente tdc, reajuste_tabela_preco rtp, tabela_preco tp ").
			append(" where tdc.ID_TABELA_PRECO = tp.ID_TABELA_PRECO_ORIGEM ").
			append(" and tp.ID_TABELA_PRECO = rdc.id_tabela_preco_nova ").
			append(" and rdc.id_divisao_cliente = tdc.ID_DIVISAO_CLIENTE  ").
			append(" and rtp.ID_TABELA_NOVA = tp.ID_TABELA_PRECO ").
			append(" and rtp.ID_TABELA_PRECO_BASE = tp.ID_TABELA_PRECO_ORIGEM ").
			append(" and rtp.BL_EFETIVADO = 'S' ").
			append(" and rdc.dt_agendamento_reajuste is not null ").
			append(" and rdc.dt_agendamento_reajuste > trunc(sysdate) ").
			append(" and rdc.bl_processado = 'N' ").
			append(" and tdc.id_tabela_divisao_cliente not in (select hrc.id_tabela_divisao_cliente ").
			append(" 											from historico_reajuste_cliente hrc ").
			append(" 											where hrc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente ").
			append(" 											and   hrc.dt_reajuste >= rtp.dt_efetivacao) ").
			toString();
		}
	
		private ConfigureSqlQuery configureSqlQueryNovoReajuste() {
			return new ConfigureSqlQuery() {
				@Override
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("idTabelaDivisaoCliente", Hibernate.LONG);
					sqlQuery.addScalar("dataVigenciaInicial",  Hibernate.custom(JodaTimeYearMonthDayUserType.class));
					sqlQuery.addScalar("idReajusteTabelaPreco", Hibernate.LONG);
					sqlQuery.addScalar("idTabelaNova", Hibernate.LONG);
					sqlQuery.addScalar("idReajusteDivisaoCliente", Hibernate.LONG);
				}
			};
		}
		
		private String mountSql() {
			return new StringBuilder().
			append(" select tdc.id_tabela_divisao_cliente as idTabelaDivisaoCliente, rtp.dt_agendamento as dataVigenciaInicial, rtp.id_reajuste_tabela_preco as idReajusteTabelaPreco, rtp.id_tabela_nova as idTabelaNova ").
			append(" from reajuste_tabela_preco rtp, tabela_divisao_cliente tdc ").
			append(" where rtp.bl_efetivado = 'S' ").
			append(" and   rtp.dt_agendamento > trunc(sysdate) ").
			append(" and   rtp.dt_agendamento is not null ").
			
			append(" and   tdc.ID_TABELA_PRECO = rtp.ID_TABELA_PRECO_BASE ").
			append(" and   tdc.BL_ATUALIZACAO_AUTOMATICA = 'S' ").
			append(" and   tdc.id_tabela_divisao_cliente not in (select hrc.id_tabela_divisao_cliente ").
				append(" from historico_reajuste_cliente hrc ").
				append(" where hrc.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente ").
				append(" and   hrc.dt_reajuste >= rtp.dt_efetivacao) ").
				toString();
		}
	
		private ConfigureSqlQuery configureSqlQuery() {
			return new ConfigureSqlQuery() {
				@Override
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("idTabelaDivisaoCliente", Hibernate.LONG);
					sqlQuery.addScalar("dataVigenciaInicial",  Hibernate.custom(JodaTimeYearMonthDayUserType.class));
					sqlQuery.addScalar("idReajusteTabelaPreco", Hibernate.LONG);
					sqlQuery.addScalar("idTabelaNova", Hibernate.LONG);
				}
			};
		}
	}

	public List<Long> findTabelasDivisaoClienteAutomaticosParaReajustar(){
		String query = new StringBuilder().
				append(" select pc.id_tabela_divisao_cliente ").
				append(" from parametro_cliente pc ").
				append(" where pc.tp_situacao_parametro = 'I' ").
				append(" and   pc.dt_vigencia_final >= trunc(sysdate) ").
				append(" and   pc.DT_VIGENCIA_INICIAL >= trunc(sysdate) ").
				append(" group by pc.id_tabela_divisao_cliente ").
				append(" having count(pc.id_parametro_cliente) = (select count(pc1.id_parametro_cliente) ").
				         append(" from parametro_cliente pc1 ").
				         append(" where pc1.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente ").
				         append(" and   pc1.tp_situacao_parametro = 'A' ").
				         append(" and   pc1.dt_vigencia_final >= trunc(sysdate) ").
				         append(" and   pc1.DT_VIGENCIA_INICIAL <= trunc(sysdate)) ").
				toString();

		return  jdbcTemplate.query(query, listIdsRowMapper());
	}
	private RowMapper listIdsRowMapper() {
		return new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				return resultSet.getLong(1);
			}
		};
	}

	public List<Long> findParametrosClonadosParaReajustar(Long idTabelaDivisaoCliente) {
		String query =  new StringBuilder().
				append(" select pc.id_parametro_cliente as idParametro ").
				append(" from parametro_cliente pc ").
				append(" where pc.ID_TABELA_DIVISAO_CLIENTE = ? ").
				append(" and   pc.tp_situacao_parametro = 'I' ").
				append(" and   pc.dt_vigencia_final >= trunc(sysdate) ").
				append(" and   pc.DT_VIGENCIA_INICIAL >= trunc(sysdate) ").
				toString();

		return  jdbcTemplate.query(query, new Object[]{idTabelaDivisaoCliente}, listIdsRowMapper());
	}


}