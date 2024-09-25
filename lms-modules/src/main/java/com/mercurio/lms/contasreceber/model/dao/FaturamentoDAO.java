package com.mercurio.lms.contasreceber.model.dao;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.param.LinhaSqlClienteFaturamentoParam;
import com.mercurio.lms.contasreceber.model.param.LinhaSqlGroupByFaturamentoParam;
import com.mercurio.lms.contasreceber.model.param.SqlFaturamentoParam;
import com.mercurio.lms.contasreceber.util.ArrayObjectRowMapper;
import com.mercurio.lms.util.JTDateTimeUtils;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FaturamentoDAO {
	
	private JdbcTemplate jdbcTemplate;

	public List executeSql(String sql, Object[] criterias) throws SQLException {
		return (List)getJdbcTemplate().query(sql, JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), criterias), new ArrayObjectRowMapper());
	}
	
	/**
	 * Retorna o id da filial centralizadora da filial informada se existe, senão, ele retorna o id da filial informada
	 * 
	 * @param long idFilialCentralizada
	 * @param String tpModal
	 * @param String tpAbrangencia
	 * 
	 * @return long
	 * 
	 * @throws SQLException
	 */
	public long findFilialCentralizadora(long idFilialCentralizada, char tpModal, char tpAbrangencia) throws SQLException{
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("id_filial_centralizadora");
		
		sql.addFrom("centralizadora_faturamento");
		
		sql.addCriteria("id_filial_centralizada", "=", idFilialCentralizada);
		sql.addCriteria("tp_modal", "=", Character.valueOf(tpModal).toString());
		sql.addCriteria("tp_abrangencia", "=", Character.valueOf(tpAbrangencia).toString());
		
		List lst = executeSql(sql.getSql(), sql.getCriteria());
		
		if (lst != null && !lst.isEmpty() && lst.get(0) != null){
			return (Long)lst.get(0);
		} else {
			return idFilialCentralizada;
		}
	}
	
	/**
	 * Retorna o parametro bl_emite_boleto_fatura da filial informada
	 * 
	 * @param long idFilial
	 * 
	 * @return boolean
	 * 
	 * @throws SQLException
	 */
	public boolean findBlEmiteBoleto(long idFilial) throws SQLException{
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("bl_emite_boleto_faturamento");
		
		sql.addFrom("filial");
		
		sql.addCriteria("id_filial", "=", idFilial);

		List lst = executeSql(sql.getSql(), sql.getCriteria());
		
		if (lst != null && !lst.isEmpty() && lst.get(0)!= null){
			if (((String)lst.get(0)).equals("S")){
				return true;
			} else {
				return false;
			}			
		} else {
			return false;
		}
	}
	
	/**
	 * Retorna o parametro id_cedente_bloqueto da filial informada
	 * 
	 * @param long idFilial
	 * 
	 * @return long
	 * 
	 * @throws SQLException
	 */
	public long findIdCedente(long idFilial) throws SQLException{
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("id_cedente_bloqueto");
		
		sql.addFrom("filial");
		
		sql.addCriteria("id_filial", "=", idFilial);

		List lst = executeSql(sql.getSql(), sql.getCriteria());
		
		if (lst != null && !lst.isEmpty() && lst.get(0) != null){
			return (Long)lst.get(0);
		} else {
			return 0;
		}
	}	
	
	/**
	 * Retorna a última cotaca da moeda informada
	 * 
	 * @param long idFilial
	 * @param long idMoeda
	 * 
	 * @return long
	 * 
	 * @throws SQLException
	 */
	public long findIdCotacaoMoeda(long idFilial, long idMoeda) throws SQLException{
		SqlTemplate sql = new SqlTemplate();
		SqlTemplate sqlPrincipal = new SqlTemplate();
		
		sql.addProjection("cotacao_moeda.id_cotacao_moeda", "id_cotacao_moeda");
		
		sql.addFrom("cotacao_moeda");
		sql.addFrom("moeda_pais");
		sql.addFrom("moeda");
		sql.addFrom("pessoa");
		sql.addFrom("endereco_pessoa");
		sql.addFrom("municipio");
		sql.addFrom("unidade_federativa");
		sql.addFrom("pais");

		sql.addJoin("cotacao_moeda.id_moeda_pais", "moeda_pais.id_moeda_pais");
		sql.addJoin("moeda_pais.id_moeda", "moeda.id_moeda");
		sql.addJoin("pessoa.id_endereco_pessoa", "endereco_pessoa.id_endereco_pessoa");
		sql.addJoin("endereco_pessoa.id_municipio", "municipio.id_municipio");
		sql.addJoin("municipio.id_unidade_federativa", "unidade_federativa.id_unidade_federativa");
		sql.addJoin("unidade_federativa.id_pais", "pais.id_pais");
		sql.addJoin("pais.id_pais", "moeda_pais.id_pais");
		sql.addCriteria("moeda.id_moeda", "=", idMoeda);
		sql.addCriteria("pessoa.id_pessoa", "=", idFilial);
		
		sql.addOrderBy("cotacao_moeda.dt_cotacao_moeda", "desc");

		sqlPrincipal.addProjection("id_cotacao_moeda");
		
		sqlPrincipal.addFrom("(" + sql.getSql() + ")");
		
		sqlPrincipal.addCriteriaValue(sql.getCriteria());
		
		sqlPrincipal.addCriteria("rownum", "<", 2);
		
		List lst = executeSql(sqlPrincipal.getSql(), sqlPrincipal.getCriteria());
		
		if (lst != null && !lst.isEmpty() && lst.get(0) != null){
			return (Long)lst.get(0);
		} else {
			return 0;
		}
	}
	
	/**
	 * Retorna a lista dos clientes especiais
	 * 
	 * @param long idCliente
	 * @param long idDivisaoCliente
	 * @param long idAgrupamentoCliente
	 * 
	 * @return List
	 * 
	 * @throws SQLException
	 */
	public List findCliente(SqlFaturamentoParam param) throws SQLException{
		Long idCliente = param.getIdCliente();
		Long idAgrupamentoCliente = param.getIdAgrupamentoCliente();
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("distinct cli.id_cliente");
		sql.addProjection("div.id_divisao_cliente");
		sql.addProjection("form.id_forma_agrupamento");
		sql.addProjection("agr.id_agrupamento_cliente");
		sql.addProjection("form.nr_ordem_prioridade");
		
		sql.addFrom("cliente", "cli");
		sql.addFrom("divisao_cliente", "div");
		sql.addFrom("agrupamento_cliente", "agr");
		sql.addFrom("forma_agrupamento", "form");
		
		sql.addJoin("nvl(cli.id_cliente_matriz, cli.id_cliente)", "div.id_cliente");
		sql.addJoin("div.id_divisao_cliente", "agr.id_divisao_cliente");
		sql.addJoin("agr.id_forma_agrupamento", "form.id_forma_agrupamento");
		
		sql.addCriteria("cli.id_cliente", "=", idCliente);
		sql.addCriteria("agr.id_agrupamento_cliente", "=", idAgrupamentoCliente);
		
		sql.addCriteria("form.tp_situacao", "=", "A");
		sql.addCustomCriteria("(cli.tp_cobranca is null or cli.tp_cobranca in ('1','2','3','4' "+ (param.getBlFaturamentoManual() ? ",'0'" : "" ) +" ))");
		
		if (idCliente == null){
			//LMSA-3172
			YearMonthDay ymd = param.getDtFinalEmissao();
			if (ymd == null) {
				ymd = JTDateTimeUtils.getDataAtual();
			}

			sql.addCriteria("form.bl_automatico", "=", "S");

			sql.addCustomCriteria("EXISTS ( "
					+ "SELECT 1 "
					+ "FROM devedor_doc_serv_fat dev1 "
					+ "	,docto_servico doc1 "
					+ "	,monitoramento_doc_eletronico mde1 "
					+ "WHERE dev1.id_cliente = cli.id_cliente "
					+ "	AND dev1.id_divisao_cliente = div.id_divisao_cliente "
					+ "	AND dev1.id_docto_servico = doc1.id_docto_servico "
					+ "	AND doc1.id_docto_servico = mde1.id_docto_servico "
					+ "	AND trunc(doc1.dh_emissao) <= ? "
					+ "	AND ( "
					+ "		mde1.tp_situacao_documento = 'A' "
					+ "		OR mde1.tp_situacao_documento IS NULL "
					+ "		) "
					+ "	AND doc1.dh_emissao IS NOT NULL "
					+ "	AND dev1.tp_situacao_cobranca IN ('P', 'C') "
					+ "	AND rownum = 1 "
					+ ") ", ymd );
		}
		
		sql.addOrderBy("form.nr_ordem_prioridade");
		
		List lst = executeSql(sql.getSql(), sql.getCriteria());
		
		if (lst != null && !lst.isEmpty()){
			for (int i = 0; i < lst.size(); i++) {
				Object[] element = (Object[])lst.get(i);

				LinhaSqlClienteFaturamentoParam linha = new LinhaSqlClienteFaturamentoParam();
								
				linha.setIdCliente((Long)element[0]);
				linha.setIdDivisaoCliente((Long)element[1]);
				linha.setIdFormaAgrupamento((Long)element[2]);
				linha.setIdAgrupamento((Long)element[3]);
				
				lst.set(i, linha);
			}
		}
		return lst;
	}
	
	/**
	 * Retorna a lista de agrupamentos especiais da forma de Agrupamento do cliente informado.
	 * 
	 * @param Long idFormaAgrupamento
	 * @return List
	 */
	public List findAgrupamento(Long idFormaAgrupamento) throws SQLException{
		List lst = new ArrayList(0);
		if (idFormaAgrupamento != null) {
			SqlTemplate sql = new SqlTemplate();
	
			sql.addProjection("dom.tp_campo");
			sql.addProjection("dom.id_informacao_doc_servico");
			sql.addProjection("dom.id_informacao_docto_cliente");
			
			sql.addFrom("dominio_agrupamento", "dom");
			
			sql.addCriteria("dom.id_forma_agrupamento", "=", idFormaAgrupamento);
			
			sql.addOrderBy("dom.nr_ordem_prioridade");
	
			lst = executeSql(sql.getSql(), sql.getCriteria());
		}
		if (lst != null){
			for (int i = 0; i < lst.size(); i++) {
				Object[] element = (Object[])lst.get(i);
				
				LinhaSqlGroupByFaturamentoParam linha = new LinhaSqlGroupByFaturamentoParam();

				linha.setTpCampo((String)element[0]);
				
				if (element[1] != null){
					linha.setIdInformacaoDocServico((Long)element[1]);
				}
				
				if (element[2] != null){
					linha.setIdInformacaoDoctoCliente((Long)element[2]);
				}
				lst.set(i, linha);
			}
			return lst;
		} else {
			return null;
		}
	}	
	
	/**
	 * Retorna o parametro id_cedente_bloqueto da filial informada
	 * 
	 * @param long idFilial
	 * 
	 * @return long
	 * 
	 * @throws SQLException
	 */
	public long findIdTipoAgrupamento(long idAgrupamentoCliente, String cdTipoAgrupamento) throws SQLException{
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("tipo.id_tipo_agrupamento");
		
		sql.addFrom("tipo_agrupamento", "tipo");
		
		sql.addCriteria("tipo.id_agrupamento_cliente", "=", (Long)idAgrupamentoCliente);
		
		sql.addCriteria("tipo.cd_tipo_agrupamento", "=", cdTipoAgrupamento);

		List lst = executeSql(sql.getSql(), sql.getCriteria());
		
		if (lst != null && !lst.isEmpty() && lst.get(0) != null){
			return (Long)lst.get(0);
		} else {
			return 0;
		}
	}
	
	/**
	 * Retorna o parametro id_cedente_bloqueto da filial informada
	 * 
	 * @param long idFilial
	 * 
	 * @return long
	 * 
	 * @throws SQLException
	 */
	public String findMensagem(String strCodeMensagem, Object[] argumentos) throws SQLException{
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("rm.texto");
		
		sql.addFrom("recursos_mensagens", "rm");
		
		sql.addCriteria("rm.chave", "=", strCodeMensagem);
		
		List lst = executeSql(sql.getSql(), sql.getCriteria());
		
		if (lst != null && !lst.isEmpty() && lst.get(0) != null){
			if (argumentos == null) {
				argumentos = new Object[0];
			}
			String texto = (String)lst.get(0);
			String msgFormatada;
			if (StringUtils.isBlank(texto)) {
				msgFormatada = "???"+strCodeMensagem+"???";
			} else {
				msgFormatada = MessageFormat.format(texto, argumentos);
			}
	    	return msgFormatada;
		} else {
			return null;
		}
	}	
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}