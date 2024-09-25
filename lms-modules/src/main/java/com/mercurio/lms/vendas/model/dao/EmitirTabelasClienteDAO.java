package com.mercurio.lms.vendas.model.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.dto.ParametroRotaDTO;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EmitirTabelasClienteDAO extends AdsmDao {	

	private void defaultReportsQuery(SqlTemplate sql, TypedFlatMap parameters){
		Boolean isTabelaNova = parameters.getBoolean("tabelaNova");

		sql.addProjection("c.id_Cliente", "idCliente");
		sql.addProjection("d.id_Divisao_Cliente", "idDivisao");
		sql.addProjection("pc.id_Parametro_Cliente", "listaParametros");
		sql.addProjection("tdc.id_Tabela_Divisao_Cliente", "idTabelaDivisao");
		sql.addProjection("pc.bl_paga_peso_excedente", "blPagaPesoExcedente");
		sql.addProjection("tp.id_Tabela_Preco", "idTabelaPreco");
		sql.addProjection("ser.id_servico", "idServico");
		
		/** Tabela Nova */
		sql.addFrom("cliente", "c");
		sql.addFrom("divisao_cliente", "d");
		sql.addFrom("tabela_divisao_cliente", "tdc");
		sql.addFrom("parametro_cliente", "pc");
		sql.addFrom("tabela_preco", "tp");
		sql.addFrom("tipo_tabela_preco", "ttp");
		sql.addFrom("subtipo_tabela_preco", "stp");
		sql.addFrom("servico", "ser");

		sql.addCustomCriteria("c.id_cliente = d.id_cliente");
		sql.addCustomCriteria("d.id_divisao_cliente = tdc.id_divisao_cliente");
		sql.addCustomCriteria("tdc.id_servico = ser.id_servico");
		sql.addCriteria("d.tp_situacao","=", "A");

		Long idUsuario = parameters.getLong("promotor.usuario.idUsuario");
		Long idContato = parameters.getLong("contato.idContato");
		if(idContato != null) {
			sql.addProjection("con.id_Contato","idContato");
			sql.addFrom("contato","con");
			sql.addCustomCriteria("con.id_pessoa = c.id_cliente");
			sql.addCriteria("con.id_contato", "=", idContato);
		}
		sql.addCustomCriteria("tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente");
		sql.addCriteria("pc.tp_situacao_parametro","=","A"); //TP_SITUACAO_PARAMETRO

		sql.addCustomCriteria("tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco");
		sql.addCustomCriteria("tp.id_subtipo_tabela_preco =  stp.id_subtipo_tabela_preco");
		sql.addCriteria("c.tp_cliente","=", ConstantesVendas.CLIENTE_ESPECIAL);
		sql.addCriteria("c.tp_situacao","=", "A");

		/** Se for Tabela Nova */
		if(Boolean.TRUE.equals(isTabelaNova)) {
			sql.addCustomCriteria("TDC.BL_ATUALIZACAO_AUTOMATICA = 'S'");
			sql.addCustomCriteria("TP.ID_TABELA_PRECO_ORIGEM = TDC.ID_TABELA_PRECO");
		}else{
			sql.addCustomCriteria("tdc.id_tabela_preco = tp.id_tabela_preco");
		}

		if (idUsuario != null) {
			sql.addFrom("promotor_cliente", "pro");
			sql.addCustomCriteria("c.id_cliente = pro.id_cliente");			
		}
		
		YearMonthDay dataInicioFormated = converStringToYearMonthDay((String) parameters.get("data.dsData"));
		
		defaultRelatoriosCriteriaSql(
			 sql
			,parameters.getLong("filial.idFilial")
			,parameters.getLong("regional.idRegional")
			,idUsuario
			,parameters.getLong("cliente.idCliente")
			,parameters.getLong("divisaoCliente.idDivisaoCliente")
			,isTabelaNova
			,dataInicioFormated);
	}
	
	private YearMonthDay converStringToYearMonthDay(String date){
		if(StringUtils.isEmpty(date)) return null;
		String[] dateSplit = date.split("-");
		return JTDateTimeUtils.convertDataStringToYearMonthDay(dateSplit[0].trim(),"dd/MM/yyyy");
	}
	
	
	public void getSqlRelatorioFreteAereoTaxaMinimaPesoExcedenteProgressivo(SqlTemplate sql, TypedFlatMap parameters){
		Boolean isTabelaNova = parameters.getBoolean("tabelaNova");
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

		sql.addProjection("c.id_Cliente", "idCliente");
		sql.addProjection("d.id_Divisao_Cliente", "idDivisao");
		sql.addProjection("tdc.id_Tabela_Divisao_Cliente", "idTabelaDivisao");
		
		/** Tabela Nova */
			sql.addProjection("tp.id_Tabela_Preco", "idTabelaPreco");

		sql.addFrom("cliente", "c");
		sql.addFrom("divisao_cliente", "d");
		sql.addFrom("tabela_divisao_cliente", "tdc");
		sql.addFrom("tabela_preco", "tp");
		sql.addFrom("tipo_tabela_preco", "ttp");
		sql.addFrom("subtipo_tabela_preco", "stp");
		sql.addFrom("servico", "ser");
		
		sql.addCustomCriteria("c.id_cliente = d.id_cliente");
		sql.addCustomCriteria("d.id_divisao_cliente = tdc.id_divisao_cliente");
		sql.addCustomCriteria("tdc.id_servico = ser.id_servico");
		sql.addCriteria("d.tp_situacao","=", "A");

		Long idUsuario = parameters.getLong("promotor.usuario.idUsuario");
		Long idContato = parameters.getLong("contato.idContato");
		if(idContato != null) {
			sql.addProjection("con.id_Contato","idContato");
			sql.addFrom("contato","con");
			sql.addCustomCriteria("con.id_pessoa = c.id_cliente");
			sql.addCriteria("con.id_contato", "=", idContato);
		}

		sql.addCustomCriteria("tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco");
		sql.addCustomCriteria("tp.id_subtipo_tabela_preco =  stp.id_subtipo_tabela_preco");
		sql.addCriteria("c.tp_situacao","=", "A");
		sql.addCriteria("c.tp_cliente","=", ConstantesVendas.CLIENTE_ESPECIAL);

		/** Se for Tabela Nova */
		if(Boolean.TRUE.equals(isTabelaNova)) {
			sql.addCustomCriteria("TDC.BL_ATUALIZACAO_AUTOMATICA = 'S'");
			sql.addCustomCriteria("TP.ID_TABELA_PRECO_ORIGEM = TDC.ID_TABELA_PRECO");
		}else{
			sql.addCustomCriteria("tdc.id_tabela_preco = tp.id_tabela_preco");
		}
		
		if (idUsuario != null) {
			sql.addFrom("promotor_cliente", "pro");
			sql.addCustomCriteria("c.id_cliente = pro.id_cliente");			
		}

		if (parameters.getLong("filial.idFilial") != null) {
			sql.addCriteria("c.id_filial_atende_comercial", "=", parameters.getLong("filial.idFilial"));
		}
		if (parameters.getLong("regional.idRegional") != null) {
			sql.addCriteria("c.id_regional_comercial", "=", parameters.getLong("regional.idRegional"));
		}
		if (idUsuario != null) {
			sql.addCriteria("pro.id_usuario", "=", idUsuario);
		}
		if (parameters.getLong("clinete.idCliente") != null) {
			sql.addCriteria("c.id_Cliente", "=", parameters.getLong("cliente.idCliente"));
		}		
		if (parameters.getLong("divisaoCliente.idDivisaoCliente") != null) {
			sql.addCriteria("d.id_divisao_cliente", "=", parameters.getLong("divisaoCliente.idDivisaoCliente"));
		}

		sql.addCriteria("tp.bl_efetivada", "=", "S");
		if (!Boolean.TRUE.equals(isTabelaNova)) {
			sql.addCriteria("tp.dt_vigencia_inicial", "<=", dataAtual);
		}else{
			sql.addCriteria("tp.dt_vigencia_inicial", ">=", dataAtual);
		}

		sql.addOrderBy("c.id_cliente ");
		sql.addOrderBy("d.id_divisao_cliente ");
		sql.addOrderBy("tdc.id_Tabela_Divisao_Cliente ");
	}

	public List findClientesParametrizados(TypedFlatMap parameters) {
		SqlTemplate sqlPrincipal = new SqlTemplate();
		SqlTemplate sqlExists = new SqlTemplate();
		
		Boolean isTabelaNova = parameters.getBoolean("tabelaNova");

		sqlPrincipal.addProjection("c.id_Cliente", "idCliente");
		sqlPrincipal.addProjection("d.id_Divisao_Cliente", "idDivisao");
		sqlPrincipal.addFrom("cliente", "c");
		sqlPrincipal.addFrom("divisao_cliente", "d");

		Long idUsuario = parameters.getLong("promotor.usuario.idUsuario");
		if (idUsuario != null) {
			sqlPrincipal.addFrom("promotor_cliente", "pro");
			sqlPrincipal.addCustomCriteria("c.id_cliente = pro.id_cliente");			
		}
		
		sqlPrincipal.addCustomCriteria("c.id_cliente = d.id_cliente");		
		sqlPrincipal.addCriteria("c.tp_cliente","=", ConstantesVendas.CLIENTE_ESPECIAL);
		sqlPrincipal.addCriteria("c.tp_situacao","=", "A");
		sqlPrincipal.addCriteria("c.id_filial_atende_comercial", "=", parameters.getLong("filial.idFilial"));
		sqlPrincipal.addCriteria("c.id_regional_comercial", "=", parameters.getLong("regional.idRegional"));
		sqlExists.addCustomCriteria("d.tp_situacao = 'A'");
		sqlPrincipal.addCriteria("pro.id_usuario", "=", idUsuario);
		sqlPrincipal.addOrderBy("c.id_cliente ");


		sqlExists.addProjection("1");
		sqlExists.addFrom("tabela_divisao_cliente", "tdc");
		sqlExists.addFrom("parametro_cliente", "pc");
		sqlExists.addFrom("tabela_preco", "tp");
		sqlExists.addFrom("tipo_tabela_preco", "ttp");
		sqlExists.addFrom("subtipo_tabela_preco", "stp");
		sqlExists.addFrom("servico", "ser");

		sqlExists.addCustomCriteria("d.id_divisao_cliente = tdc.id_divisao_cliente");
		sqlExists.addCustomCriteria("tdc.id_servico = ser.id_servico");
		sqlExists.addCustomCriteria("tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente");
		sqlExists.addCustomCriteria("pc.tp_situacao_parametro = 'A'");
		sqlExists.addCustomCriteria("tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco");
		sqlExists.addCustomCriteria("tp.id_subtipo_tabela_preco =  stp.id_subtipo_tabela_preco");

		sqlExists.addCustomCriteria("tp.bl_imprime =  'S'");

		/** Se for Tabela Nova */
		if(Boolean.TRUE.equals(isTabelaNova)) {
			sqlExists.addCustomCriteria("TDC.BL_ATUALIZACAO_AUTOMATICA = 'S'");
			sqlExists.addCustomCriteria("TP.ID_TABELA_PRECO_ORIGEM = TDC.ID_TABELA_PRECO");
		}else{
			sqlExists.addCustomCriteria("tdc.id_tabela_preco = tp.id_tabela_preco");
		}
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		sqlExists.addCustomCriteria("tp.bl_efetivada = 'S'");
		
		if (!Boolean.TRUE.equals(isTabelaNova)) {
			sqlExists.addCustomCriteria("tp.dt_vigencia_inicial <= sysdate");
		} else {
			sqlExists.addCustomCriteria("tp.dt_vigencia_inicial >= sysdate");
		}
		
		sqlExists.addCustomCriteria("pc.dt_vigencia_inicial <= sysdate");
		sqlExists.addCustomCriteria("pc.dt_vigencia_final >= sysdate");
		sqlExists.addCustomCriteria("rownum = 1");

		sqlPrincipal.addCustomCriteria("exists (" + sqlExists.getSql(true) + ")");
		
		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sqlPrincipal.getSql(), sqlPrincipal.getCriteria());
	}	
	
	/**
	 * Método que recebe os parametros selecionados para o relatorio e retorna o cliente 
	 * sua divisao e os ids dos prametros do cliente para os relatorios de minimoProgressivoPorTarifa
	 * e minimoProgressivoPorTaxaEmbutidas
	 * @param parameters
	 * @param subtipoTabela
	 * @return
	 */
	public List findRelatorioMinimoProgs(TypedFlatMap parameters, String operador, String subtipoTabela) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addProjection("pc.vl_frete_peso as vlFretePeso");
		sql.addProjection("pc.vl_perc_minimo_progr as vlPercMinimoProgr");
		sql.addProjection("pc.vl_advalorem as vlAdvalorem");
		sql.addProjection("pc.tp_indicador_frete_peso as tpIndicadorFretePeso");
		sql.addProjection("pc.tp_indicador_min_frete_peso as tpIndicadorMinFretePeso");
		sql.addProjection("pc.tp_indicador_perc_minimo_progr as tpIndicadorPercMinimoProgr");
		sql.addProjection("pc.tp_indicador_advalorem as tpIndicadorAdvalorem");
		sql.addProjection("tdc.bl_paga_frete_tonelada","pagaFreteTonelada");

		sql.addCustomCriteria(" ttp.tp_tipo_tabela_preco in(?,?,?,?)");
		sql.addCriteriaValue("M");
		sql.addCriteriaValue("R");
		sql.addCriteriaValue("E");
		sql.addCriteriaValue("T");
		sql.addCriteria("stp.tp_subtipo_tabela_preco", operador, subtipoTabela);

		sql.addCriteria("pc.tp_indicador_min_frete_peso", "=", "T");

		sql.addCustomCriteria("pc.tp_indicador_frete_peso in(?,?,?,?)");
		sql.addCriteriaValue("V");
		sql.addCriteriaValue("T");
		sql.addCriteriaValue("D");
		sql.addCriteriaValue("A");
		sql.addCriteria("pc.vl_minimo_frete_quilo", "=", "0");
		sql.addCustomCriteria("pc.id_uf_destino is null");
		sql.addCustomCriteria("pc.id_uf_origem is null");
		sql.addCustomCriteria("(pc.pc_frete_percentual is null or pc.pc_frete_percentual = ?) ");
		sql.addCriteriaValue("0");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	/**
	 * Método que recebe os parametros selecionados para o relatorio e retorna o cliente 
	 * sua divisao e os ids dos prametros do cliente para o relatorio de FretePercentual
	 * @param parameters
	 * @return
	 */
	public List findRelatorioFretePercentual(TypedFlatMap parameters, String modal) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("ser.TP_MODAL","=", modal);
		sql.addCriteria("ser.TP_ABRANGENCIA","=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		sql.addCriteria("pc.pc_frete_percentual","<>", 0);
		sql.addCustomCriteria("pc.PS_FRETE_PERCENTUAL is not null");
		sql.addCustomCriteria("pc.VL_MINIMO_FRETE_PERCENTUAL is not null"); 
		sql.addCustomCriteria("pc.VL_TONELADA_FRETE_PERCENTUAL is not null");

		return findJdbcFromHibernateTemplate(getHibernateTemplate(), sql.getSql(), sql.getCriteria());
	}

	private List findJdbcFromHibernateTemplate(HibernateTemplate hibernateTemplate,final String sSql, final Object[] criterions) {
		List listClientes = (List) hibernateTemplate.execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Connection connection = session.connection();
				DataSource ds = new SingleConnectionDataSource(connection, false);
				JdbcTemplate jdbcTemplate  = new JdbcTemplate(ds); 

				List ret = null;
				try{
					ret = jdbcTemplate.queryForList(sSql,JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, criterions));
				}finally{
					DataSourceUtils.releaseConnection(connection, ds);
				}
				return ret;
			}});
		return listClientes;
	}

	/**
	 * Método que recebe os parametros selecionados para o relatório e retorna o cliente 
	 * sua divisão e os ids dos parâmetros do cliente para o relatório de FreteMinimoPesoExcedente
	 * @param parameters
	 * @return
	 */
	public List findRelatorioFreteMinimoPesoExcedente(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("pc.tp_indicador_frete_peso ", "=", "V"); 
		sql.addCriteria("pc.tp_indicador_min_frete_peso ", "=", "P");
		sql.addCriteria("pc.vl_minimo_frete_quilo ", "<>", "0"); 
		sql.addCriteria("pc.vl_perc_minimo_progr ", "=", "0");
		sql.addCriteria("pc.bl_paga_peso_excedente ", "=", "S"); 
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia ", "=",ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public List findRelatorioFreteMinimoRota(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("pc.tp_indicador_frete_peso ", "=", "V"); 
		sql.addCriteria("pc.tp_indicador_min_frete_peso ", "=", "P");
		sql.addCustomCriteria("pc.vl_minimo_frete_quilo >= 0"); 
		sql.addCriteria("pc.vl_perc_minimo_progr ", "=", "0");
		sql.addCriteria("pc.bl_paga_peso_excedente ", "=", "N"); 
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia ", "=",ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	/**
	 * M&eacute;todo que recebe os parametros selecionados para o relat&oacute;rio e retorna o cliente 
	 * sua divisao e os ids dos parametros do cliente para o relat&oacute;rio de FreteAereoTaxaMinimaPesoExcedente
	 * @param parameters
	 * @return
	 */
	public List findRelatorioFreteAereoTaxaMinimaPesoExcedente(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);

		//specifications from ET
		sql.addCriteria("ttp.tp_tipo_tabela_preco ", "=", "A");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_AEREO);
		sql.addCriteria("ser.tp_abrangencia", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		sql.addCriteria("pc.tp_indicador_frete_peso ", "=", "V");
		sql.addCriteria("pc.tp_indicador_min_frete_peso ", "=", "P");
		sql.addCriteria("pc.vl_minimo_frete_quilo ", "<>", "0");
		sql.addCriteria("pc.vl_perc_minimo_progr ", "=", "0");
		sql.addCriteria("pc.bl_paga_peso_excedente", "=", "S");

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	/**
	 * Metodo que retorna a lista de clientes com os identificadores da divisao, tabela da divisao do cliente e 
	 * tabela de preco filtrados pelos parametros especificos e tipos de tabela, tipo modal e abrangencia
	 * 
	 * @param parameters parametros da tela 'emitir tabelas do cliente'
	 * @param tipoTabelaPreco tipo da tabela de preco
	 * @param tipoModal tipo modal
	 * @param tipoAbrangencia tipo da abrangencia
	 * @return lista dos clientes com os dados especificados no filtro e tipos
	 */
	public List findRelatorioFreteRodoviarioVolume(TypedFlatMap parameters)	{
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("ttp.tp_tipo_tabela_preco ", "=", "D");
		sql.addCriteria("ser.tp_modal ", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());		
	}

	public List findFreteMinimoProgressivoRota(TypedFlatMap parameters)	{
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);

		sql.addCustomCriteria("ttp.tp_tipo_tabela_preco in ('E', 'M', 'T')");
		sql.addCriteria("ser.tp_modal ", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		sql.addProjection("tdc.bl_paga_frete_tonelada","pagaFreteTonelada");
		sql.addCriteria("pc.tp_indicador_frete_peso ", "=", "V");
		sql.addCriteria("pc.tp_indicador_min_frete_peso ", "=", "T");
		sql.addCriteria("pc.tp_indicador_advalorem ", "=", "V");

		sql.addCustomCriteria("pc.id_uf_destino is not null");
		sql.addCustomCriteria("pc.id_uf_origem is not null");			

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());		
	}

	/**
	 * Metodo que recebe parametros selecionados e retorna lista de clientes validos
	 * para o relatorio FreteAereoTarifaEspecifica
	 * 
	 * @param parameters
	 * @return
	 */
	public List findRelatorioAereoTarifaEspecifica(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);

		sql.addProjection("pe.id_produto_especifico","idProduto");

		sql.addFrom("tabela_preco_parcela","tpp");
		sql.addFrom("faixa_progressiva","fp");
		sql.addFrom("produto_especifico","pe");

		sql.addCustomCriteria("tpp.id_tabela_preco = tp.id_tabela_preco");
		sql.addCustomCriteria("tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela");
		sql.addCustomCriteria("fp.id_produto_especifico = pe.id_produto_especifico");

		String produtoEspecifico = parameters.getString("produtoEspecifico.idProdutoEspecifico");
		sql.addCriteria("pe.id_produto_especifico", "=", produtoEspecifico);

		sql.addCriteria("ttp.tp_tipo_tabela_preco ", "=", "A");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_AEREO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		sql.addCriteria("pc.tp_tarifa_minima","=","V");
		sql.addCriteria("pc.tp_indic_vlr_tbl_especifica","=","V");
		sql.addCriteria("pc.id_aeroporto_origem", "=", parameters.get("aeroportoOrigem.idAeroporto"));
		sql.addCriteria("pc.id_aeroporto_destino", "=", parameters.get("aeroportoDestino.idAeroporto"));

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	/**
	 * Método que recebe os parametros selecionados para o relatorio e retorna o cliente 
	 * sua divisao e os ids dos prametros do cliente para o relatorio de RelatorioMinimoProgPorPesoExcedente
	 * @param parameters
	 * @return
	 */
	public List findRelatorioMinimoProgPorPesoExcedente(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("tp.tp_calculo_frete_peso ", "=", "E");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	/**
	 * Método que recebe os parametros selecionados para o relatorio e retorna o cliente 
	 * sua divisao e os ids dos prametros do cliente para o relatorio de RelatorioMinimoProgPorPesoExcedente
	 * @param parameters
	 * @return
	 */
	public List findRelatorioMinimoTarifa(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);

		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		sql.addCustomCriteria("pc.tp_indicador_frete_peso in (?,?,?,?)");
		sql.addCriteriaValue("T");
		sql.addCriteriaValue("V");
		sql.addCriteriaValue("A");
		sql.addCriteriaValue("D");

		sql.addCustomCriteria(" ttp.tp_tipo_tabela_preco in(?,?,?,?)");
		sql.addCriteriaValue("M");
		sql.addCriteriaValue("R");
		sql.addCriteriaValue("E");
		sql.addCriteriaValue("T");

		sql.addCriteria("pc.tp_indicador_min_frete_peso", "=", "P");
		sql.addCriteria("pc.vl_minimo_frete_quilo", "=", "0");
		sql.addCustomCriteria("pc.id_uf_origem is null");
		sql.addCustomCriteria("pc.id_uf_destino is null");

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}
	
	/**
	 * 
	 * @param sql
	 * @param idFilial
	 * @param idRegional
	 * @param idUsuario
	 * @param idCliente
	 * @param idDivisao
	 * @param isTabelaNova
	 */
	private void defaultRelatoriosCriteriaSql(SqlTemplate sql, Long idFilial, Long idRegional, Long idUsuario, Long idCliente, Long idDivisao, Boolean isTabelaNova, YearMonthDay dataInformada){
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		if (idFilial != null) {
			sql.addCriteria("c.id_filial_atende_comercial", "=", idFilial);
		}
		if (idRegional != null) {
			sql.addCriteria("c.id_regional_comercial", "=", idRegional);
		}
		if (idUsuario != null) {
			sql.addCriteria("pro.id_usuario", "=", idUsuario);
		}
		if (idCliente != null) {
			sql.addCriteria("c.id_Cliente", "=", idCliente);
		}		
		if (idDivisao != null) {
			sql.addCriteria("d.id_divisao_cliente", "=", idDivisao);
		}

		sql.addCriteria("tp.bl_efetivada", "=", "S");
		
		if (!Boolean.TRUE.equals(isTabelaNova)) {
			sql.addCriteria("tp.dt_vigencia_inicial", "<=", dataAtual);
		} else {
			sql.addCriteria("tp.dt_vigencia_inicial", ">=", dataAtual);
		}
		
		 
		if(dataInformada == null){
			sql.addCriteria("pc.dt_vigencia_inicial", "<=", dataAtual);
			sql.addCriteria("pc.dt_vigencia_final", ">=", dataAtual);
		} else {
			sql.addCriteria("pc.dt_vigencia_inicial", "<=", dataInformada);
			sql.addCriteria("pc.dt_vigencia_final", ">=", dataInformada);
		}

		sql.addOrderBy("c.id_cliente ");
		sql.addOrderBy("d.id_divisao_cliente ");
		sql.addOrderBy("tdc.id_Tabela_Divisao_Cliente ");
	}

	public List findRelatorioFreteAereoPadrao(TypedFlatMap parameters) {		
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("ser.tp_modal","=",ConstantesExpedicao.MODAL_AEREO);
		sql.addCriteria("ser.tp_abrangencia","=",ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		sql.addCriteria("ttp.tp_tipo_tabela_preco ", "=", "A");
		sql.addCustomCriteria(" (pc.tp_indicador_frete_peso IN ('A','D','T') OR pc.tp_indic_vlr_tbl_especifica IN ('A','D','T') )");
		sql.addCustomCriteria("pc.id_uf_destino is null");
		sql.addCustomCriteria("pc.id_uf_origem is null");		

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	/**
	 * Método que recebe os parametros selecionados para o relatorio e retorna o cliente 
	 * sua divisao e os ids dos prametros do cliente para o relatorio de RelatorioFreteAereoProgressivoPesoExcedente
	 * @param parameters
	 * @return
	 */
	public List findRelatorioFreteAereoProgressivaPesoExcedente(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("ttp.tp_tipo_tabela_preco ", "=", "D");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_AEREO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		sql.addCriteria("tp.tp_calculo_frete_peso","=","E");

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	public List findRelatorioFreteAereoVolume(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("ttp.tp_tipo_tabela_preco ", "=", "D");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_AEREO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	public List findRelatorioFreteDiferenciada(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCustomCriteria("ttp.tp_tipo_tabela_preco in ('D', '@') ");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		sql.addCriteria("tp.tp_calculo_frete_peso","=","Q");

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}
	
	public List findRelatorioFreteMinimoValor(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);

		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);
		sql.addCriteria("pc.tp_indicador_frete_peso ", "=", "V");
		sql.addCriteria("pc.tp_indicador_min_frete_peso ", "=", "V");
		sql.addCriteria("pc.vl_perc_minimo_progr ", "=", "0");
		sql.addCriteria("pc.bl_paga_peso_excedente", "=", "N");

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	public List findRelatorioFreteAereoProgressivo(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();
		defaultReportsQuery(sql,parameters);
		sql.addCriteria("ttp.tp_tipo_tabela_preco", "=", "D");
		sql.addCustomCriteria("tp.tp_calculo_frete_peso in (?,?)");
		sql.addCriteriaValue("Q");
		sql.addCriteriaValue("M");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_AEREO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	public List findRelatorioFreteAereoTaxaMinimaPesoExcedenteProgressivo(TypedFlatMap parameters) {
		SqlTemplate sql = new SqlTemplate();

		getSqlRelatorioFreteAereoTaxaMinimaPesoExcedenteProgressivo(sql,parameters);
		sql.addCriteria("ttp.tp_tipo_tabela_preco", "=", "D");
		sql.addCustomCriteria("tp.tp_calculo_frete_peso in (?,?)");
		sql.addCriteriaValue("E");
		sql.addCriteriaValue("X");
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_AEREO);
		sql.addCriteria("ser.tp_abrangencia ", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		return findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());
	}

	/**
	 * retorna true se for uma faixa progressiva por volume 
	 * @param idTabelaPreco
	 * @return
	 */
	public boolean hasFaixaPorVolume(Long idTabelaPreco){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(" * ");
		sql.addFrom("tabela_preco","tp");
		sql.addFrom("parcela_preco","pp");
		sql.addFrom("tabela_preco_parcela","tpp");
		sql.addFrom("faixa_progressiva","fp");
		sql.addCriteria("tp.id_tabela_preco","=",idTabelaPreco);
		sql.addCustomCriteria("tpp.id_tabela_preco  = tp.id_tabela_preco");
		sql.addCustomCriteria("tpp.id_parcela_preco = pp.id_parcela_preco");
		sql.addCriteria("pp.cd_parcela_preco","=", "IDFretePeso");
		sql.addCustomCriteria("fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela");
		sql.addCriteria("fp.cd_minimo_progressivo","=", "VO");

		List list = findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());		
		return !list.isEmpty();
	}

	/**
	 * retorna true se a tabela de preco possuir faixas progressivas com rota
	 * @param idTabelaPreco
	 * @return
	 */
	public boolean hasFaixaRota(Long idTabelaPreco){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(" * ");
		sql.addFrom("tabela_preco","tp");
		sql.addFrom("parcela_preco","pp");
		sql.addFrom("tabela_preco_parcela","tpp");
		sql.addFrom("faixa_progressiva","fp");
		sql.addFrom("valor_faixa_progressiva","vfp");
		sql.addFrom("rota_preco","rp");
		sql.addFrom("tarifa_preco","tap");
		sql.addFrom("tarifa_preco_rota","tpr");

		sql.addCriteria("tp.id_tabela_preco","=",idTabelaPreco);
		sql.addCustomCriteria("tpp.id_tabela_preco  = tp.id_tabela_preco");
		sql.addCustomCriteria("tpp.id_parcela_preco = pp.id_parcela_preco");
		sql.addCustomCriteria("fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela");
		sql.addCustomCriteria("vfp.id_faixa_progressiva = fp.id_faixa_progressiva ");
		sql.addCustomCriteria("vfp.id_tarifa_preco = tap.id_tarifa_preco ");
		sql.addCustomCriteria("tap.id_tarifa_preco = tpr.id_tarifa_preco");
		sql.addCustomCriteria("tpr.id_tabela_preco = tp.id_tabela_preco ");
		sql.addCustomCriteria("tpr.id_rota_preco =  rp.id_rota_preco");
		sql.addCriteria("fp.cd_minimo_progressivo","<>", "VO");

		List list = findJdbcFromHibernateTemplate(getHibernateTemplate(),sql.getSql(), sql.getCriteria());		
		return CollectionUtils.isNotEmpty(list);
	}

	/**
	 * Busca dados das Tabelas Fob e Pacotinho
	 * @param parameters
	 * @return
	 */
	public List findTabelaFobPacotinho(TypedFlatMap parameters, String tpTabelaPreco){
		Boolean isTabelaNova = parameters.getBoolean("tabelaNova");
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("c.id_Cliente", "idCliente");
		sql.addProjection("d.id_Divisao_Cliente", "idDivisao");
		sql.addProjection("tdc.id_Tabela_Divisao_Cliente", "idTabelaDivisao");
		
		/** Tabela Nova */
			sql.addProjection("tp.id_Tabela_Preco", "idTabelaPreco");

		sql.addFrom("cliente", "c");
		sql.addFrom("divisao_cliente", "d");
		sql.addFrom("tabela_divisao_cliente", "tdc");
		sql.addFrom("tabela_preco", "tp");
		sql.addFrom("tipo_tabela_preco", "ttp");
		sql.addFrom("subtipo_tabela_preco", "stp");
		sql.addFrom("servico", "ser");
		
		sql.addCustomCriteria("c.id_cliente = d.id_cliente");
		sql.addCustomCriteria("d.id_divisao_cliente = tdc.id_divisao_cliente");
		sql.addCustomCriteria("tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco");
		sql.addCustomCriteria("tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco");
		sql.addCustomCriteria("ttp.id_servico = ser.id_servico");

		sql.addCriteria("tp.bl_efetivada", "=", "S");
		sql.addCriteria("c.tp_cliente","=", ConstantesVendas.CLIENTE_ESPECIAL);
		sql.addCriteria("ser.tp_modal", "=", ConstantesExpedicao.MODAL_RODOVIARIO);
		sql.addCriteria("ser.tp_abrangencia", "=", ConstantesExpedicao.ABRANGENCIA_NACIONAL);

		/** Se for Tabela Nova */
		if(ConstantesExpedicao.TP_FRETE_FOB.equals(tpTabelaPreco)) {
			sql.addCriteria("stp.tp_subtipo_tabela_preco","=",ConstantesExpedicao.TP_FRETE_FOB);
			if(Boolean.TRUE.equals(isTabelaNova)) {
				sql.addCustomCriteria("d.TP_SITUACAO = 'A'");
				sql.addCustomCriteria("TDC.BL_ATUALIZACAO_AUTOMATICA = 'S'");
				sql.addCustomCriteria("TP.ID_TABELA_PRECO_ORIGEM = TDC.id_tabela_preco_fob");
			}else{
			sql.addCustomCriteria("tdc.id_tabela_preco_fob = tp.id_tabela_preco");
			}
		} else {
			sql.addCriteria("stp.tp_subtipo_tabela_preco","=",ConstantesExpedicao.TP_FRETE_PACOTINHO);
			if(Boolean.TRUE.equals(isTabelaNova)) {
				sql.addCustomCriteria("d.TP_SITUACAO = 'A'");
				sql.addCustomCriteria("TDC.BL_ATUALIZACAO_AUTOMATICA = 'S'");
				sql.addCustomCriteria("TP.ID_TABELA_PRECO_ORIGEM = TDC.ID_TABELA_PRECO");
			}else{
			sql.addCustomCriteria("tdc.id_tabela_preco = tp.id_tabela_preco");
		}
		}

		Long idContato = parameters.getLong("contato.idContato");
		if(idContato != null) {
			sql.addProjection("con.id_Contato","idContato");
			sql.addFrom("contato","con");
			sql.addCustomCriteria("con.id_pessoa = c.id_cliente");
			sql.addCriteria("con.id_contato", "=", idContato);
		}
		Long idUsuario = parameters.getLong("promotor.usuario.idUsuario");
		if (idUsuario != null) {
			sql.addFrom("promotor_cliente", "pro");
			sql.addCustomCriteria("c.id_cliente = pro.id_cliente");
			sql.addCriteria("pro.id_usuario", "=", idUsuario);
		}
		Long idFilial = parameters.getLong("filial.idFilial");
		if (idFilial != null) {
			sql.addCriteria("c.id_filial_atende_comercial", "=", idFilial);
		}
		Long idRegional = parameters.getLong("regional.idRegional");
		if (idRegional != null) {
			sql.addCriteria("c.id_regional_comercial", "=", idRegional);
		}
		Long idCliente = parameters.getLong("cliente.idCliente");
		if (idCliente != null) {
			sql.addCriteria("c.id_Cliente", "=", idCliente);
		}
		Long idDivisaoCliente = parameters.getLong("divisaoCliente.idDivisaoCliente");
		if (idDivisaoCliente != null) {
			sql.addCriteria("d.id_divisao_cliente", "=", idDivisaoCliente);
		}
		if (!Boolean.TRUE.equals(isTabelaNova)) {
			sql.addCriteria("tp.dt_vigencia_inicial", "<=", dataAtual);
		} else {
			sql.addCriteria("tp.dt_vigencia_inicial", ">=", dataAtual);
		}

		sql.addOrderBy("c.id_cliente");
		sql.addOrderBy("d.id_divisao_cliente");
		sql.addOrderBy("tdc.id_Tabela_Divisao_Cliente");
		return findJdbcFromHibernateTemplate(getHibernateTemplate(), sql.getSql(), sql.getCriteria());
	}

	/**
	 * LMS-4526 - Busca parâmetros do cliente para ordenamento de rotas por
	 * origem e destino. Utiliza id's das UF's, id's das filiais, tipo de
	 * localização dos municípios e descrição do grupo de região. Opcionalmente
	 * uma lista de atributos da entidade <tt>ParametroCliente</tt> pode ser
	 * incluída.
	 * 
	 *  0- cliente.pessoa.enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa
	 *  1- cliente.filialByIdFilialAtendeComercial.idFilial
	 *  2- parametroCliente.idParametroCliente
	 *  3- parametroCliente.unidadeFederativaByIdUfOrigem.idUnidadeFederativa
	 *  4- parametroCliente.unidadeFederativaByIdUfOrigem.sgUnidadeFederativa
	 *  5- parametroCliente.unidadeFederativaByIdUfDestino.idUnidadeFederativa
	 *  6- parametroCliente.unidadeFederativaByIdUfDestino.sgUnidadeFederativa
	 *  7- parametroCliente.filialByIdFilialOrigem.idFilial
	 *  8- parametroCliente.filialByIdFilialOrigem.sgFilial
	 *  9- parametroCliente.filialByIdFilialDestino.idFilial
	 * 10- parametroCliente.filialByIdFilialDestino.sgFilial
	 * 11- parametroCliente.tipoLocalizacaoMunicipioOrigem.dsTipoLocalizacaoMunicipio
	 * 12- parametroCliente.tipoLocalizacaoMunicipioDestino.dsTipoLocalizacaoMunicipio
	 * 13- parametroCliente.grupoRegiaoOrigem.dsGrupoRegiao
	 * 14- parametroCliente.grupoRegiaoDestino.dsGrupoRegiao
	 * 
	 * @param idCliente
	 *            id do cliente referente à tabela
	 * 
	 * @param listIdParametroCliente
	 *            lista de id's de parâmetros do cliente
	 * 
	 * @param parametroClienteAttrs
	 *            lista de atributos de <tt>ParametroCliente</tt>
	 * 
	 * @return lista de <tt>ParametroRotaDTO</tt> utilizados para
	 *         ordenamento de rotas
	 */
	@SuppressWarnings("unchecked")
	public List<ParametroRotaDTO> findParametroClienteRota(
			Long idCliente, List<Long> listIdParametroCliente, String... parametroClienteAttrs) {

		List<ParametroRotaDTO> result = new ArrayList<ParametroRotaDTO>();

		if(listIdParametroCliente.size() > 999) {

			List<Long>[] listIdParametroClientes = dividirListIdParametroCliente(listIdParametroCliente, 20);

			for (List<Long> itemIdParametroCliente : listIdParametroClientes) {
				List<ParametroRotaDTO> resultParcial = new ArrayList<ParametroRotaDTO>();

				StringBuilder hql = query(parametroClienteAttrs);
				hql.append(itemIdParametroCliente);

				hql.append(")");
				resultParcial = getAdsmHibernateTemplate().find(hql.toString().replaceAll("\\[", "").replaceAll("\\]", ""), idCliente);
				result.addAll(resultParcial);
			}
		} else {

			BigDecimal rounds = new BigDecimal(listIdParametroCliente.size() / 999).setScale(0);
			int resto = listIdParametroCliente.size() % 999;
			int init = 0;
			int end = listIdParametroCliente.size() > 999 ? 998 : listIdParametroCliente.size() - 1;

			if (rounds.compareTo(BigDecimal.ONE) >= 0 && resto != 0) {
				rounds = rounds.add(BigDecimal.ONE);

			} else if (rounds.compareTo(BigDecimal.ZERO) == 0 || rounds.compareTo(BigDecimal.ONE) == 0) {
				rounds = rounds.add(BigDecimal.ONE);

			}

			for (int i = 1; i <= rounds.longValue(); i++) {
			List<ParametroRotaDTO> resultParcial = new ArrayList<ParametroRotaDTO>();
				if (i < rounds.longValue() && i > 1) {
					init = end + 1;
					end = (end + 1) + end;
				} else if (i == rounds.longValue() && rounds.longValue() > 1) {
					init = end + 1;
					end = listIdParametroCliente.size() - 1;
				}

				StringBuilder hql = query(parametroClienteAttrs);
				for (int id = init; id <= end; id++) {
					hql.append(listIdParametroCliente.get(id)).append(",");
				}
				hql.setLength(hql.length() - 1);
				hql.append(")");
				resultParcial = getAdsmHibernateTemplate().find(hql.toString(), idCliente);
				result.addAll(resultParcial);
			}
		}
		return result;
	}

	private StringBuilder query(String... parametroClienteAttrs){
			StringBuilder hql = new StringBuilder()
					.append("SELECT new com.mercurio.lms.vendas.dto.ParametroRotaDTO(")
					.append("  uf_c.idUnidadeFederativa, ")
					.append("  f_c.idFilial, ")
					.append("  pc.idParametroCliente, ")
					.append("  uf_o.idUnidadeFederativa, ")
					.append("  uf_o.sgUnidadeFederativa, ")
					.append("  uf_d.idUnidadeFederativa, ")
					.append("  uf_d.sgUnidadeFederativa, ")
					.append("  f_o.idFilial, ")
					.append("  f_o.sgFilial, ")
					.append("  f_d.idFilial, ")
					.append("  f_d.sgFilial, ")
					.append("  tlm_o.dsTipoLocalizacaoMunicipio, ")
					.append("  tlm_d.dsTipoLocalizacaoMunicipio, ")
					.append("  gr_o.dsGrupoRegiao, ")
					.append("  gr_d.dsGrupoRegiao ");
			if (parametroClienteAttrs != null) {
				for (String parametroClienteAttr : parametroClienteAttrs) {
					hql.append(", pc.").append(parametroClienteAttr);
				}
			}
			hql			.append(") ")
			.append("FROM Cliente c, ParametroCliente pc ")
			.append("JOIN c.pessoa p ")
			.append("JOIN p.enderecoPessoa ep ")
			.append("JOIN ep.municipio m ")
			.append("JOIN m.unidadeFederativa uf_c ")
			.append("JOIN c.filialByIdFilialAtendeComercial f_c ")
			.append("LEFT JOIN pc.unidadeFederativaByIdUfOrigem uf_o ")
			.append("LEFT JOIN pc.unidadeFederativaByIdUfDestino uf_d ")
			.append("LEFT JOIN pc.filialByIdFilialOrigem f_o ")
			.append("LEFT JOIN pc.filialByIdFilialDestino f_d ")
			.append("LEFT JOIN pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem tlm_o ")
			.append("LEFT JOIN pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino tlm_d ")
			.append("LEFT JOIN pc.grupoRegiaoOrigem gr_o ")
			.append("LEFT JOIN pc.grupoRegiaoDestino gr_d ")
			.append("WHERE c.idCliente = ? ")
			.append("AND pc.idParametroCliente IN (");
		return hql;
		}

	public static <T> List<T>[] dividirListIdParametroCliente(List<T> input, int range) {
		List<T>[] items = new List[range];
		for (int i = 0; i < range; i++) {
			items[i] = new LinkedList<T>();
	    }

	    for (int i = 0; i < input.size(); i++) {
			items[i % range].add(input.get(i));
	    }
		return items;
	}
}
