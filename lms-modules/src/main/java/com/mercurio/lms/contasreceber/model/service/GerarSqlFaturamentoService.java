package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.param.SqlFaturamentoParam;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SQLUtils;

public abstract class GerarSqlFaturamentoService {

	public SqlTemplate generateSql(SqlFaturamentoParam sqlParam){
		SqlTemplate sql = new SqlTemplate();
		
		mountProjection(sql);

		mountFrom(sql);
		
		mountJoin(sql);
	
		mountCriteria(sql, sqlParam);

		mountOrderBy(sql);
		
		mountSqlSpecific(sql, sqlParam);
		
		return sql;
	}


	/**
	 * @param sql
	 */
	private void mountProjection(SqlTemplate sql) {
		sql.addProjection("dev.id_devedor_doc_serv_fat");
		sql.addProjection("dev.id_cliente");
		sql.addProjection("dev.id_filial");
		sql.addProjection("dev.id_divisao_cliente");
		sql.addProjection("serv.tp_modal");
		sql.addProjection("serv.tp_abrangencia");
		sql.addProjection("doc.tp_documento_servico");
		sql.addProjection("doc.id_moeda");
		sql.addProjection("ctrc.tp_frete");
		sql.addProjection("serv.id_servico");
		sql.addProjection("cast(doc.dh_emissao as Date)");
		sql.addProjection("cli.tp_cobranca");
		sql.addProjection("cli.id_filial_cobranca");
		sql.addProjection("cli.id_cedente");
		sql.addProjection("div.nr_qtde_docs_romaneio");
		sql.addProjection("cli.bl_agrupa_faturamento_mes");
	}
	
	/**
	 * @param sql
	 */
	private void mountFrom(SqlTemplate sql) {
		sql.addFrom("devedor_doc_serv_fat", "dev");
		sql.addFrom("docto_servico", "doc");
		sql.addFrom("cliente", "cli");
		sql.addFrom("pessoa", "pes");
		sql.addFrom("filial", "fil");
		sql.addFrom("filial", "fil_doc");//LMS-1652 conforme alteracao da ET 36.03.01.02
		sql.addFrom("servico", "serv");
		sql.addFrom("divisao_cliente", "div");
		sql.addFrom("conhecimento", "ctrc");
		sql.addFrom("nota_debito_nacional", "ndn");
		sql.addFrom("cto_internacional", "crt");
		sql.addFrom("nota_fiscal_servico", "nfs");
		sql.addFrom("monitoramento_doc_eletronico", "mde");
	}
	
	/**
	 * @param sql
	 */
	private void mountJoin(SqlTemplate sql) {
		sql.addJoin("dev.id_docto_servico", "doc.id_docto_servico");
		sql.addJoin("dev.id_cliente", "cli.id_cliente");
		sql.addJoin("cli.id_cliente", "pes.id_pessoa");
		sql.addJoin("dev.id_filial", "fil.id_filial");
		sql.addJoin("doc.id_filial_destino", "fil_doc.id_filial(+)");//LMS-1652 conforme alteracao da ET 36.03.01.02
		sql.addJoin("doc.id_servico", "serv.id_servico(+)");
		sql.addJoin("dev.id_divisao_cliente", "div.id_divisao_cliente (+)");
		sql.addJoin("doc.id_docto_servico", "ctrc.id_conhecimento (+)");
		sql.addJoin("doc.id_docto_servico", "crt.id_cto_internacional (+)");
		sql.addJoin("doc.id_docto_servico", "nfs.id_nota_fiscal_servico (+)");
		sql.addJoin("doc.id_docto_servico", "ndn.id_nota_debito_nacional (+)");
		sql.addJoin("doc.id_docto_servico", "mde.id_docto_servico(+)");
	}
	
	/**
	 * @param sql
	 */
	private void mountCriteria(SqlTemplate sql, SqlFaturamentoParam sqlParam) {
		
		/* Quando a filial de cobrança está preenchida significa que está gerando faturamento manual
		 * Nesse caso, deve buscar os devedores onde a filial de cobrança seja a filial da tela
		 * ou esteja centralizada na filial da tela, dependendo também do modal e abrangência do documento de serviço
		 */
		if (sqlParam.getIdFilialCobranca()!=null) {
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("dev.id_filial in ( \n");
			sb.append("	select	? \n");
			sb.append("	from	dual \n");
			sb.append("	union \n");
			sb.append("	select	f_int.id_filial \n"); 
			sb.append("	from	filial f_int, \n");
			sb.append("			centralizadora_faturamento cf \n"); 
			sb.append("	where	cf.id_filial_centralizada = f_int.id_filial \n");
			sb.append("	and		cf.id_filial_centralizadora = ? \n");
			sb.append("	and		cf.tp_modal = serv.tp_modal \n");
			sb.append("	and		cf.tp_abrangencia = serv.tp_abrangencia) \n");
			
			sql.addCustomCriteria(sb.toString());
			sql.addCriteriaValue(sqlParam.getIdFilialCobranca());
			sql.addCriteriaValue(sqlParam.getIdFilialCobranca());

		}
		
		sql.addCriteria("dev.id_cliente", "=", sqlParam.getIdCliente());
		sql.addCriteria("dev.id_divisao_cliente", "=", sqlParam.getIdDivisaoCliente());
		sql.addCriteria("ctrc.tp_frete", "=", sqlParam.getTpFrete());
		sql.addCriteria("serv.tp_modal", "=", sqlParam.getTpModal());
		sql.addCriteria("serv.tp_abrangencia", "=", sqlParam.getTpAbrangencia());
		sql.addCriteria("trunc(doc.dh_emissao)", ">=", sqlParam.getDtInicioEmissao());
		sql.addCriteria("trunc(doc.dh_emissao)", "<=", sqlParam.getDtFinalEmissao());
		
		// Se não informado na tela a data final, buscar somente os emitidos até data atual - 1
		// isso para não correr o risco de, caso o faturamento seja executado às 3 horas da manhã,
		// buscar conhecimentos emitidos da meia noite até às 3
		if (sqlParam.getDtFinalEmissao()==null) {
			sql.addCriteria("trunc(doc.dh_emissao)", "<=", JTDateTimeUtils.getDataAtual().minusDays(1));
		}

		SQLUtils.joinExpressionsWithComma(sqlParam.getLstDevedorDocServFat(),sql,"dev.id_devedor_doc_serv_fat");

		sql.addCustomCriteria("(mde.tp_situacao_documento = 'A' or mde.tp_situacao_documento is null)");
		
		//Se o cliente não foi informado (da tela), selecionar os clientes que tem cobrança de tipo:
		if (sqlParam.getIdCliente() == null){
			sql.addCustomCriteria("(cli.tp_cobranca IN ('1', '2', '3', '4') OR cli.tp_cobranca IS NULL)");			
		}
		
		sql.addCustomCriteria("doc.dh_emissao IS NOT NULL");
		sql.addCustomCriteria("dev.tp_situacao_cobranca IN ('P','C')");
		
		//Só pode ser documentos onde a filial já usa o LMS
		sql.addCriteria("fil.dt_implantacao_lms", "<=", JTDateTimeUtils.getDataAtual());
				
		sql.addCustomCriteria("(ctrc.tp_situacao_conhecimento IN ('E','B') OR ctrc.tp_situacao_conhecimento IS NULL)");
		sql.addCustomCriteria("(crt.tp_situacao_crt = 'E' OR crt.tp_situacao_crt IS NULL)");
		sql.addCustomCriteria("(nfs.tp_situacao_nf = 'E' OR nfs.tp_situacao_nf IS NULL)");
		sql.addCustomCriteria("(ndn.tp_situacao_nota_debito_nac = 'E' OR ndn.tp_situacao_nota_debito_nac IS NULL)");
		sql.addCustomCriteria("(ndn.tp_situacao_cancelamento <> 'E' OR ndn.tp_situacao_cancelamento IS NULL)");
		sql.addCustomCriteria("(ndn.tp_situacao_desconto <> 'E'	OR ndn.tp_situacao_desconto IS NULL)");
		
		mountFiltroTpFrete(sql, sqlParam);
		
		//Se não é faturamento manual
		if (!sqlParam.getBlFaturamentoManual()){
			mountFiltroDiaFaturamento(sql, sqlParam);
		}
		
		mountFiltroDesconto(sql);

		mountFiltroAgendaTransferencia(sql);
		
		mountFiltroTransferencia(sql);

		mountFiltroEntrega(sql);
		
		mountFiltroArquivoAnexo(sql);
		
		mountFiltroBloqueioFaturamento(sql);

		sql.addCustomCriteria("((cli.bl_cobranca_centralizada = 'S' AND cli.id_filial_cobranca = dev.id_filial) OR cli.bl_cobranca_centralizada = 'N')");
	}
	
	/**
	 * @param sql
	 */
	private void mountOrderBy(SqlTemplate sql) {
		sql.addOrderBy("pes.nm_pessoa");
		sql.addOrderBy("dev.id_cliente");
		sql.addOrderBy("dev.id_filial");
		sql.addOrderBy("dev.id_divisao_cliente");
		sql.addOrderBy("serv.tp_modal");
		sql.addOrderBy("serv.tp_abrangencia");
		sql.addOrderBy("doc.tp_documento_servico");
		sql.addOrderBy("doc.id_moeda");
		sql.addOrderBy("ctrc.tp_frete");
		sql.addOrderBy("serv.id_servico");
	}		


	/**
	 * 
	 * 
	 * @param sql
	 * @param sqlParam
	 */
	private void mountFiltroTpFrete(SqlTemplate sql, SqlFaturamentoParam sqlParam) {
		StringBuffer sqlFrete = new StringBuffer();
		
		sqlFrete.append("(\n");
		sqlFrete.append("		ctrc.tp_frete IS NULL\n");
		sqlFrete.append("	OR	(\n");
		sqlFrete.append("			cli.bl_fatura_docs_entregues = 'N'\n");
		sqlFrete.append("		AND	ctrc.tp_frete = 'F'\n");
		sqlFrete.append("		AND	dev.id_filial <> nvl(fil_doc.id_filial_responsavel, doc.ID_FILIAL_DESTINO)\n"); //LMS-1652 alterado coforme ET 36.03.01.02
		sqlFrete.append("		)\n");
		sqlFrete.append("	OR	(\n");
		sqlFrete.append("			cli.bl_fatura_docs_entregues = 'N'\n");
		sqlFrete.append("	AND		ctrc.tp_frete = 'C'\n");
		sqlFrete.append("		)\n");
		sqlFrete.append("	OR	(\n");
		sqlFrete.append("		doc.nr_dias_real_entrega IS NOT NULL\n");
		sqlFrete.append("		OR	EXISTS (\n");
		sqlFrete.append("				SELECT	evento.id_evento\n");
		sqlFrete.append("				FROM	evento_documento_servico,\n");
		sqlFrete.append("						evento\n");
		sqlFrete.append("				WHERE	evento_documento_servico.id_docto_servico = doc.id_docto_servico\n");
		sqlFrete.append("				AND		evento_documento_servico.BL_EVENTO_CANCELADO = 'N'\n");
		sqlFrete.append("				AND		evento_documento_servico.id_evento = evento.id_evento\n");
		sqlFrete.append("				AND		evento.cd_evento IN (7) \n");
		sqlFrete.append("			)\n");
		sqlFrete.append("		)\n");
		sqlFrete.append(")\n");
		
		sql.addCustomCriteria(sqlFrete.toString());
		
		sql.addCriteria("ctrc.tp_frete", "=", sqlParam.getTpFrete());
	}
	
	/**
	 * 
	 */
	private void mountFiltroDiaFaturamento(SqlTemplate sql, SqlFaturamentoParam sqlParam) {
		StringBuffer sqlDia = new StringBuffer();
		
		sqlDia.append("(");
		
		mountFiltroDiaFaturamentoSimple(sql, sqlDia);

		mountFiltroDiaFaturamentoDiario(sql, sqlDia);
		
		mountFiltroDiaFaturamentoSemanal(sql, sqlDia, sqlParam);
		
		mountFiltroDiaFaturamentoDecendial(sql, sqlDia, sqlParam);
		
		mountFiltroDiaFaturamentoQuinzenal(sql, sqlDia, sqlParam);
		
		mountFiltroDiaFaturamentoMensal(sql, sqlDia, sqlParam);	
		
		sqlDia.append(")");
		
		sql.addCustomCriteria(sqlDia.toString());
	}

	/**
	 * @param sql
	 */
	private void mountFiltroDesconto(SqlTemplate sql) {
		SqlTemplate sqlDesconto = new SqlTemplate();
		
		sqlDesconto.addProjection("desconto.id_desconto");
		sqlDesconto.addFrom("desconto");
		sqlDesconto.addJoin("desconto.id_devedor_doc_serv_fat", "dev.id_devedor_doc_serv_fat");
		sqlDesconto.addCustomCriteria("desconto.tp_situacao_aprovacao IN ('E', 'R')");
		
		sql.addCustomCriteria("NOT EXISTS("+sqlDesconto.getSql()+")");
	}	
	
	
	/**
	 * @param sql
	 */
	private void mountFiltroAgendaTransferencia(SqlTemplate sql) {
		SqlTemplate sqlAgenda = new SqlTemplate();
		
		sqlAgenda.addProjection("agenda_transferencia.id_agenda_transferencia");
		sqlAgenda.addFrom("agenda_transferencia");
		sqlAgenda.addJoin("agenda_transferencia.id_devedor_doc_serv_fat", "dev.id_devedor_doc_serv_fat");
		
		sql.addCustomCriteria("NOT EXISTS("+sqlAgenda.getSql()+")");
	}
	
	/**
	 * @param sql
	 */
	private void mountFiltroTransferencia(SqlTemplate sql) {
		SqlTemplate sqlTransferencia = new SqlTemplate();
		
		sqlTransferencia.addProjection("transferencia.id_transferencia");
		sqlTransferencia.addFrom("transferencia");
		sqlTransferencia.addFrom("item_transferencia");
		sqlTransferencia.addJoin("transferencia.id_transferencia", "item_transferencia.id_transferencia");
		sqlTransferencia.addJoin("item_transferencia.id_devedor_doc_serv_fat", "dev.id_devedor_doc_serv_fat");
		sqlTransferencia.addCustomCriteria("transferencia.tp_situacao_transferencia = 'PR'");
		sql.addCustomCriteria("NOT EXISTS("+sqlTransferencia.getSql()+")");
	}
	
	/**
	 * Retorno do canhoto da nota fiscal. Verificar que não existe registros que tem que ter entregue de 
	 * canhoto mas onde o conhoto não foi entregue ainda.
	 * 
	 * @param sql
	 */
	private void mountFiltroEntrega(SqlTemplate sql) {
		SqlTemplate sqlEntrega = new SqlTemplate();
		sqlEntrega.addProjection("documento_cliente.id_documento_cliente");
		sqlEntrega.addFrom("documento_cliente");
		sqlEntrega.addJoin("documento_cliente.id_cliente", "cli.id_cliente");
		sqlEntrega.addJoin("documento_cliente.tp_modal", "serv.tp_modal");
		sqlEntrega.addJoin("documento_cliente.tp_abrangencia", "serv.tp_abrangencia");
		sqlEntrega.addCriteria("documento_cliente.dt_vigencia_inicial", "<=", JTDateTimeUtils.getDataAtual());
		sqlEntrega.addCriteria("documento_cliente.dt_vigencia_final", ">=", JTDateTimeUtils.getDataAtual());
		sqlEntrega.addCustomCriteria("documento_cliente.bl_fatura_vinculada = 'S'");

		// Esse sub SQL existe para garantir que não sejam faturados documentos de serviço
		// de clientes bl_fatura_vinculada = 'S' sem que os mesmos tenham sido entregues 
		SqlTemplate sqlSubEntrega = new SqlTemplate();
		sqlSubEntrega.addProjection("registro_documento_entrega.id_registro_documento_entrega");
		sqlSubEntrega.addFrom("registro_documento_entrega");
		sqlSubEntrega.addJoin("registro_documento_entrega.id_docto_servico", "doc.id_docto_servico");
		sqlSubEntrega.addCustomCriteria("registro_documento_entrega.tp_situacao_registro <> 'EO'");		
		
		// Esse sub SQL existe para garantir que não sejam faturados documentos de serviço
		// de clientes bl_fatura_vinculada = 'S' e que não tenha sido gerado o manifesto de entrega
		SqlTemplate sqlSubEntregaInexistente = new SqlTemplate();
		sqlSubEntregaInexistente.addProjection("registro_documento_entrega.id_registro_documento_entrega");
		sqlSubEntregaInexistente.addFrom("registro_documento_entrega");
		sqlSubEntregaInexistente.addJoin("registro_documento_entrega.id_docto_servico", "doc.id_docto_servico");
		
		sqlEntrega.addCustomCriteria("(EXISTS ("+sqlSubEntrega.getSql()+") OR NOT EXISTS ("+sqlSubEntregaInexistente.getSql()+"))");
		
		sql.addCriteriaValue(sqlEntrega.getCriteria());

		sql.addCustomCriteria("NOT EXISTS ("+sqlEntrega.getSql()+")");
	}
	
	/**
	 * Selecionar só os documentos que tem arquivos em anexos ou que o cliente não pode ter referencias
	 * 
	 * @param sql
	 */
	private void mountFiltroArquivoAnexo(SqlTemplate sql) {
		StringBuffer strArquivoAnexo = new StringBuffer();
		strArquivoAnexo.append("(cli.bl_fatura_doc_referencia = 'N' \n ");
		strArquivoAnexo.append("OR \n ");
		strArquivoAnexo.append("doc.tp_documento_servico != 'CRT' \n ");
		strArquivoAnexo.append("OR \n ");
		strArquivoAnexo.append("EXISTS (select  docan.id_documento_anexo \n ");
		strArquivoAnexo.append("from    documento_anexo docan, \n ");
		strArquivoAnexo.append("        parametro_geral parge, \n ");
		strArquivoAnexo.append("        pessoa pe, \n ");
		strArquivoAnexo.append("        endereco_pessoa endpe, \n ");
		strArquivoAnexo.append("        municipio mun, \n ");
		strArquivoAnexo.append("        unidade_federativa uf, \n ");
		strArquivoAnexo.append("        permisso_empresa_pais perep \n ");
		strArquivoAnexo.append("where   docan.id_cto_internacional = crt.id_cto_internacional \n ");
		strArquivoAnexo.append("and     docan.id_anexo_docto_servico = to_number(parge.ds_conteudo) \n ");
		strArquivoAnexo.append("and     parge.nm_parametro_geral = 'ID_REFERENCIA_BOSCH' \n ");
		strArquivoAnexo.append("and     pe.id_pessoa = cli.id_cliente \n ");
		strArquivoAnexo.append("and     pe.id_endereco_pessoa = endpe.id_endereco_pessoa \n ");
		strArquivoAnexo.append("and     endpe.id_municipio = mun.id_municipio \n ");
		strArquivoAnexo.append("and     mun.id_unidade_federativa = uf.id_unidade_federativa \n ");
		strArquivoAnexo.append("and     crt.id_permisso_empresa_pais = perep.id_permisso_empresa_pais \n ");
		strArquivoAnexo.append("and     uf.id_pais != perep.id_pais_origem \n ");
		strArquivoAnexo.append("and     cli.bl_fatura_doc_referencia = 'S') \n ");
		strArquivoAnexo.append("OR \n ");
		strArquivoAnexo.append("EXISTS (select  perep.id_permisso_empresa_pais \n ");
		strArquivoAnexo.append("from    pessoa pe, \n ");
		strArquivoAnexo.append("        endereco_pessoa endpe, \n ");
		strArquivoAnexo.append("        municipio mun, \n ");
		strArquivoAnexo.append("        unidade_federativa uf, \n ");
		strArquivoAnexo.append("        permisso_empresa_pais perep \n ");
		strArquivoAnexo.append("where   pe.id_pessoa = cli.id_cliente \n ");
		strArquivoAnexo.append("and     pe.id_endereco_pessoa = endpe.id_endereco_pessoa \n ");
		strArquivoAnexo.append("and     endpe.id_municipio = mun.id_municipio \n ");
		strArquivoAnexo.append("and     mun.id_unidade_federativa = uf.id_unidade_federativa \n ");
		strArquivoAnexo.append("and     crt.id_permisso_empresa_pais = perep.id_permisso_empresa_pais \n ");
		strArquivoAnexo.append("and     uf.id_pais = perep.id_pais_origem))");
		sql.addCustomCriteria(strArquivoAnexo.toString());
	}	
	
	protected abstract void mountSqlSpecific(SqlTemplate sql, SqlFaturamentoParam sqlParam);

	/**
	 * @param sql
	 * @param sqlDia
	 */
	private void mountFiltroDiaFaturamentoSimple(SqlTemplate sql, StringBuffer sqlDia) {
		sqlDia.append("NOT EXISTS \n");
		SqlTemplate sqlDiaFaturamento;
		sqlDiaFaturamento = getSqlDiaFaturamento();
		sql.addCriteriaValue(sqlDiaFaturamento.getCriteria());
		sqlDia.append("("+sqlDiaFaturamento.getSql()+")\n");
	}

	/**
	 * @param sql
	 * @param sqlDia
	 */
	private void mountFiltroDiaFaturamentoDiario(SqlTemplate sql, StringBuffer sqlDia) {
		sqlDia.append("OR EXISTS \n");
		SqlTemplate sqlDiaFaturamento;
		sqlDiaFaturamento = getSqlDiaFaturamento("D");
		sqlDia.append("("+sqlDiaFaturamento.getSql()+")\n");
		sql.addCriteriaValue(sqlDiaFaturamento.getCriteria());
	}
	

	private void mountFiltroBloqueioFaturamento(SqlTemplate sql) {
		SqlTemplate sqlBloqueio = new SqlTemplate();

		sqlBloqueio.addProjection("bqf.id_bloqueio_faturamento");
		sqlBloqueio.addFrom("bloqueio_faturamento", "bqf");
		sqlBloqueio.addJoin("bqf.id_devedor_doc_serv_fat", "dev.id_devedor_doc_serv_fat");
		sqlBloqueio.addCustomCriteria("bqf.dh_desbloqueio IS NULL");

		sql.addCustomCriteria("NOT EXISTS(" + sqlBloqueio.getSql() + ")");
	}


	/**
	 * @param sql
	 * @param sqlParam
	 */
	private void mountFiltroDiaFaturamentoSemanal(SqlTemplate sql, StringBuffer sqlDia, SqlFaturamentoParam sqlParam) {
		List lst = new ArrayList(1);
		
		lst.add(sqlParam.getTpDiaSemana());
		
		sqlDia.append("OR EXISTS \n");
		SqlTemplate sqlDiaFaturamento;
		sqlDiaFaturamento = getSqlDiaFaturamento("S");
		sqlDiaFaturamento.addCustomCriteria("(dia.tp_dia_semana = ? " +
				"\nOR EXISTS (("+getSqlDiaFaturamentoEmpresa(lst).getSql());
		sqlDiaFaturamento.addCustomCriteria("dia.tp_dia_semana IS NULL))");
		
		String diaCorteFaturamentoSemanal = " OR EXISTS \n (select 1 from dia_corte_faturamento dcf "
				+ "where dcf.dt_corte = ? and dcf.bl_semanal = 'S' and rownum = 1)) ";
		
		sqlDia.append("("+sqlDiaFaturamento.getSql() + diaCorteFaturamentoSemanal+")\n");
		
		sql.addCriteriaValue(sqlDiaFaturamento.getCriteria());
		sql.addCriteriaValue(sqlParam.getTpDiaSemana());
		sql.addCriteriaValue(sqlParam.getDtEmissao());
	}
	
	/**
	 * @param sql
	 * @param sqlParam
	 */
	private void mountFiltroDiaFaturamentoDecendial(SqlTemplate sql, StringBuffer sqlDia, SqlFaturamentoParam sqlParam) {
		if (!sqlParam.getNrDiaDecendio().isEmpty()){
			sqlDia.append("OR EXISTS \n");
			SqlTemplate sqlDiaFaturamento;
			sqlDiaFaturamento = getSqlDiaFaturamento("E");
	
			sqlDiaFaturamento.addCustomCriteria("(dia.nr_dia_faturamento IN "+SQLUtils.joinExpressionsWithComma(sqlParam.getNrDiaDecendio())+" " +
					"\nOR EXISTS (("+getSqlDiaFaturamentoEmpresa(sqlParam.getNrDiaDecendio()).getSql());
			sqlDiaFaturamento.addCustomCriteria("dia.nr_dia_faturamento IS NULL))");
			
			String diaCorteFaturamentoDecendial = " OR EXISTS \n (select 1 from dia_corte_faturamento dcf "
					+ "where dcf.dt_corte = ? and dcf.bl_decendial = 'S' and rownum = 1)) ";
			
			sqlDia.append("("+sqlDiaFaturamento.getSql() + diaCorteFaturamentoDecendial+")\n");
			
			sql.addCriteriaValue(sqlDiaFaturamento.getCriteria());
			sql.addCriteriaValue(sqlParam.getDtEmissao());
		}
	}
	
	/**
	 * @param sql
	 * @param sqlParam
	 */
	private void mountFiltroDiaFaturamentoQuinzenal(SqlTemplate sql, StringBuffer sqlDia, SqlFaturamentoParam sqlParam) {
		if (!sqlParam.getNrDiaQuinzenal().isEmpty()){
			sqlDia.append("OR EXISTS \n");
			SqlTemplate sqlDiaFaturamento;
			sqlDiaFaturamento = getSqlDiaFaturamento("Q");
			sqlDiaFaturamento.addCustomCriteria("(dia.nr_dia_faturamento IN "+SQLUtils.joinExpressionsWithComma(sqlParam.getNrDiaQuinzenal())+" " +
					"\nOR EXISTS (("+getSqlDiaFaturamentoEmpresa(sqlParam.getNrDiaQuinzenal()).getSql());
			sqlDiaFaturamento.addCustomCriteria("dia.nr_dia_faturamento IS NULL))");
			
			String diaCorteFaturamentoQuinzenal = " OR EXISTS \n (select 1 from dia_corte_faturamento dcf "
					+ "where dcf.dt_corte = ? and dcf.bl_quinzenal = 'S' and rownum = 1)) ";
			
			sqlDia.append("("+sqlDiaFaturamento.getSql() + diaCorteFaturamentoQuinzenal+")\n");
			
			sql.addCriteriaValue(sqlDiaFaturamento.getCriteria());
			sql.addCriteriaValue(sqlParam.getDtEmissao());
		}
	}
	
	/**
	 * @param sql
	 * @param sqlParam
	 */
	private void mountFiltroDiaFaturamentoMensal(SqlTemplate sql, StringBuffer sqlDia, SqlFaturamentoParam sqlParam) {
		if (!sqlParam.getNrDiaMes().isEmpty()){
			sqlDia.append("OR EXISTS \n");
			SqlTemplate sqlDiaFaturamento;
			sqlDiaFaturamento = getSqlDiaFaturamento("M");
			sqlDiaFaturamento.addCustomCriteria("(dia.nr_dia_faturamento IN "+SQLUtils.joinExpressionsWithComma(sqlParam.getNrDiaMes())+" " +
					"\nOR EXISTS (("+getSqlDiaFaturamentoEmpresa(sqlParam.getNrDiaMes()).getSql());
			sqlDiaFaturamento.addCustomCriteria("dia.nr_dia_faturamento IS NULL))");
			
			String diaCorteFaturamentoMensal = " OR EXISTS \n (select 1 from dia_corte_faturamento dcf "
					+ "where dcf.dt_corte = ? and dcf.bl_mensal = 'S' and rownum = 1)) ";
			
			sqlDia.append("("+sqlDiaFaturamento.getSql() + diaCorteFaturamentoMensal+")\n");
			
			sql.addCriteriaValue(sqlDiaFaturamento.getCriteria());
			sql.addCriteriaValue(sqlParam.getDtEmissao());
		}
	}	
	
	
	/**
	 * @param sql
	 */
	private SqlTemplate getSqlDiaFaturamento() {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("dia.id_dia_faturamento");
		sql.addFrom("dia_faturamento", "dia");
		sql.addJoin("dia.id_divisao_cliente", "div.id_divisao_cliente");
		sql.addCustomCriteria("(dia.tp_frete = ctrc.tp_frete OR dia.tp_frete IS NULL OR ctrc.tp_frete IS NULL)");
		sql.addCustomCriteria("(dia.tp_modal = serv.tp_modal OR dia.tp_modal IS NULL OR serv.tp_modal IS NULL)");
		sql.addCustomCriteria("(dia.tp_abrangencia = serv.tp_abrangencia OR dia.tp_abrangencia IS NULL OR serv.tp_abrangencia IS NULL)");
		sql.addCustomCriteria("(dia.id_servico = serv.id_servico OR dia.id_servico IS NULL OR serv.id_servico IS NULL)");
		
		return sql;
	}		
	
	/**
	 * @param sql
	 */
	private SqlTemplate getSqlDiaFaturamento(String tpPeriodicidade) {
		SqlTemplate sql = getSqlDiaFaturamento();
		sql.addCriteria("dia.tp_periodicidade", "=", tpPeriodicidade);
		return sql;
	}
	
	/**
	 * @param sql
	 */
	private SqlTemplate getSqlDiaFaturamentoEmpresa(List lst) {
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("1");
		sql.addFrom("dias_faturamento_empresa");
		sql.addJoin("dias_faturamento_empresa.tp_periodicidade", "dia.tp_periodicidade");
		sql.addCustomCriteria("dias_faturamento_empresa.tp_situacao = 'A'");
		sql.addCustomCriteria("dd_corte IN "+SQLUtils.joinExpressionsWithComma(lst)+"");
		
		return sql;
	}	
			
}