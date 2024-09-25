package com.mercurio.lms.contasreceber.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * 
 */
public class EmitirExcecoesDoctoServicoService extends ReportServiceSupport {

	/**
	 * método responsável por gerar o relatório.
	 */
	public File executeReport(final Map parameters) throws Exception {
		final SqlTemplate sql = getSql(parameters);

		File file = null;
		
		SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql.getSql(), sql.getCriteria());
		
		if(rs.next()){
			file = File.createTempFile("report-emitieexcecoesdoctoservico-" + System.currentTimeMillis(), ".csv", new ReportExecutionManager().generateOutputDir());
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));
			
			addColumn(writer, getMessage("lbTpLayoutDocumento"));
			addColumn(writer, getMessage("documento"));
			addColumn(writer, getMessage("modal"));
			addColumn(writer, getMessage("dataEmissao"));
			addColumn(writer, getMessage("horaEmissao"));
			addColumn(writer, getMessage("remetente"));
			addColumn(writer, getMessage("destinatario"));
			addColumn(writer, getMessage("responsavelFreteTitulo"));
			addColumn(writer, getMessage("filialOrigem"));
			addColumn(writer, getMessage("filialDestino"));
			addColumn(writer, getMessage("cnpjRemetente"));
			addColumn(writer, getMessage("cnpjDestinatario"));
			addColumn(writer, getMessage("cnpjResponsavel"));
			addColumn(writer, getMessage("tipoFrete"));
			addColumn(writer, getMessage("pesoMercadoria"));
			addColumn(writer, getMessage("pesoCubado"));
			addColumn(writer, getMessage("pesoCalculo"));
			addColumn(writer, getMessage("qtdVolume"));
			addColumn(writer, getMessage("freteRS"));
			addColumn(writer, getMessage("impostosIcmsIss"));
			addColumn(writer, getMessage("aliquotaImposto"));
			addColumn(writer, getMessage("parcelasPreco"));
			addColumn(writer, getMessage("filialEmissoraCTRC"));
			addColumn(writer, getMessage("notasFiscaisCliente"));
			addColumn(writer, getMessage("tabelaTarifa"));
			addColumn(writer, getMessage("nomeEmissor"));
			addColumn(writer, getMessage("responsavelPelaLiberacao"));
			addColumn(writer, getMessage("data"));
			addColumn(writer, getMessage("clienteComEdi"));
			addColumn(writer, getMessage("documentoEmitidoPorEdi"));
			addColumn(writer, getMessage("tpCalculo"));
			addColumn(writer, getMessage("vlrMercadoria"), true);
			
			writer.write("\n");

			DecimalFormat duasCasas = new DecimalFormat("##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
			DecimalFormat tresCasas = new DecimalFormat("##0.000", new DecimalFormatSymbols(new Locale("pt", "BR")));
			do {
								
				addColumn(writer, rs.getString("tp_documento_servico"));
				addColumn(writer, rs.getString("nr_conhecimento"));
				addColumn(writer, rs.getString("tp_modal"));
				addColumn(writer, rs.getString("data_emissao"));
				addColumn(writer, rs.getString("hora_emissao"));
				addColumn(writer, rs.getString("nm_remetente"));
				addColumn(writer, rs.getString("nm_destinatario"));
				addColumn(writer, rs.getString("nm_responsavel"));
				addColumn(writer, rs.getString("sg_filial_origem"));
				addColumn(writer, rs.getString("sg_filial_destino"));
				addColumn(writer, rs.getString("cnpj_remetente"));
				addColumn(writer, rs.getString("cnpj_destinatario"));
				addColumn(writer, rs.getString("cnpj_responsavel"));
				addColumn(writer, rs.getString("tp_frete"));
				BigDecimal ps_real = rs.getBigDecimal("ps_real");
				if(ps_real != null){
					addColumn(writer, duasCasas.format(ps_real));
				} else {
					addColumn(writer, null);
				}
				BigDecimal ps_aforado = rs.getBigDecimal("ps_aforado");
				if(ps_aforado != null){
					addColumn(writer, tresCasas.format(ps_aforado));
				} else {
					addColumn(writer, null);
				}
				BigDecimal ps_calculo = rs.getBigDecimal("ps_calculo");
				if(ps_calculo != null){
					addColumn(writer, tresCasas.format(ps_calculo));
				} else {
					addColumn(writer, null);
				}
				addColumn(writer, rs.getString("qt_volumes"));
				
				BigDecimal vl_total_doc_servico = rs.getBigDecimal("vl_total_doc_servico");
				if(vl_total_doc_servico != null){
					addColumn(writer, duasCasas.format(vl_total_doc_servico));
				} else {
					addColumn(writer, null);
				}
				
				BigDecimal impostos = rs.getBigDecimal("impostos");
				if(impostos != null){
					addColumn(writer, duasCasas.format(impostos));
				} else {
					addColumn(writer, null);
				}
				
				addColumn(writer, rs.getString("aliquota"));
				addColumn(writer, rs.getString("parcela"));
				addColumn(writer, rs.getString("sg_filial_emissora"));
				addColumn(writer, rs.getString("nota_fiscal"));
				addColumn(writer, rs.getString("tabela_tarifa"));
				addColumn(writer, rs.getString("nome_emissor"));
				addColumn(writer, rs.getString("resp_liberacao"));
				addColumn(writer, rs.getString("data_liberacao"));
				addColumn(writer, rs.getString("cliente_edi"));
				addColumn(writer, rs.getString("bl_indicador_edi"));
				addColumn(writer, rs.getString("tp_calculo_preco"));
				
				BigDecimal vl_mercadoria = rs.getBigDecimal("vl_mercadoria");
				if(vl_mercadoria != null){
					addColumn(writer, duasCasas.format(vl_mercadoria), true);
				} else {
					addColumn(writer, null, true);
				}
				
				writer.write("\n");
			} while(rs.next());
			writer.close();
		}
		
		return file;
	}
	
	private void addColumn(BufferedWriter writer, String value) throws IOException{
		addColumn(writer, value, false);
	}
	
	private void addColumn(BufferedWriter writer, String value, boolean last) throws IOException{
		if(value != null){
			writer.write(value);
		}
		if(!last){
			writer.write(";");
		}
	}

	private SqlTemplate getSql(final Map parameters) {
		final SqlTemplate sql = createSqlTemplate();
		sql.addProjection("(select vI18n(ds_valor_dominio_i)" +
				" from dominio d," +
				" valor_dominio v" +
				" where d.id_dominio = v.id_dominio " +
				" and nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO'" +
				" and vl_valor_dominio = c.TP_DOCUMENTO_SERVICO ) as TP_DOCUMENTO_SERVICO");
		sql.addProjection(" fo.sg_filial || c.nr_conhecimento as nr_conhecimento");
		sql.addProjection(" (select vI18n(ds_valor_dominio_i) " +
				" from dominio d," +
				" valor_dominio v" +
				" where d.id_dominio = v.id_dominio " +
				" and nm_dominio = 'DM_MODAL' " +
				" and vl_valor_dominio = s.tp_modal) as tp_modal");
		sql.addProjection(" to_char(ds.dh_emissao,'dd/mm/yyyy') as  data_emissao");
		sql.addProjection(" to_char(ds.dh_emissao,'hh24:mi:ss') as  hora_emissao");
		sql.addProjection(" pr.nm_pessoa as nm_remetente");
		sql.addProjection(" pd.nm_pessoa as nm_destinatario");
		sql.addProjection(" pf.nm_pessoa as nm_responsavel");
		sql.addProjection(" fo.sg_filial as sg_filial_origem");
		sql.addProjection(" fd.sg_filial as sg_filial_destino");
		sql.addProjection(" pr.nr_identificacao as cnpj_remetente");
		sql.addProjection(" pd.nr_identificacao as cnpj_destinatario");
		sql.addProjection(" pf.nr_identificacao as cnpj_responsavel");
		sql.addProjection(" (select vI18n(ds_valor_dominio_i) " +
				" from dominio d," +
				" valor_dominio v" +
				" where d.id_dominio = v.id_dominio " +
				" and nm_dominio = 'DM_TIPO_FRETE' " +
				" and vl_valor_dominio = c.tp_frete) as tp_frete");
		sql.addProjection(" ds.PS_REAL");
		sql.addProjection(" ds.PS_AFORADO");
		sql.addProjection(" ds.PS_REAL as ps_calculo");
		sql.addProjection(" ds.QT_VOLUMES");
		sql.addProjection(" ds.VL_TOTAL_DOC_SERVICO");
		sql.addProjection(" case when ds.TP_DOCUMENTO_SERVICO in ('NFT','NTE') then ims.VL_IMPOSTO " +
				"else ds.VL_IMPOSTO end as impostos");
		sql.addProjection(" case when ds.TP_DOCUMENTO_SERVICO in ('NFT','NTE') then ims.PC_ALIQUOTA " +
				"else ds.PC_ALIQUOTA_ICMS end as aliquota");
		sql.addProjection(" (select translate(wm_concat(VI18N(pp.nm_parcela_preco_i)|| ' '|| translate(pds.vl_parcela,',','.')),',.','/,') from " +
				"PARCELA_DOCTO_SERVICO pds, " +
				"PARCELA_PRECO pp " +
				"where pds.id_parcela_preco = pp.ID_PARCELA_PRECO " +
				"and pds.id_docto_servico = ds.id_docto_servico) as parcela");
		sql.addProjection("fo.sg_filial as sg_filial_emissora");
		sql.addProjection("(select translate(wm_concat(NR_NOTA_FISCAL),',','/') from NOTA_FISCAL_CONHECIMENTO nfc where nfc.id_conhecimento = ds.id_docto_servico) as nota_fiscal");
		sql.addProjection("ttp.tp_tipo_tabela_preco || ttp.nr_versao || '-' || stp.tp_subtipo_tabela_preco || '/' || trp.CD_TARIFA_PRECO as tabela_tarifa");
		sql.addProjection("case when c.bl_indicador_edi = 'S' then '7777777' " +
				"else u.nm_usuario end as nome_emissor");
		sql.addProjection("ul.nm_usuario as resp_liberacao");
		sql.addProjection("case when lds.ID_USUARIO is not null then to_char(ds.dh_emissao,'dd/mm/yyyy') " +
				"end as data_liberacao");
		sql.addProjection(" nvl((select 'S' from CLIENTE_EDI_FILIAL_EMBARC where CLIE_PESS_ID_PESSOA = ds.id_cliente_remetente and rownum = 1),'N') as cliente_edi ");
		sql.addProjection(" c.bl_indicador_edi as bl_indicador_edi");
		sql.addProjection(" (select vI18n(ds_valor_dominio_i) " +
				" from dominio d," +
				" valor_dominio v" +
				" where d.id_dominio = v.id_dominio " +
				" and nm_dominio = 'DM_TIPO_CALCULO_FRETE' " +
				" and vl_valor_dominio = ds.tp_calculo_preco) as tp_calculo_preco");
		sql.addProjection(" ds.vl_mercadoria");
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("conhecimento", "c");
		sql.addFrom("servico", "s");
		sql.addFrom("pessoa", "pr");
		sql.addFrom("pessoa", "pd");
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "ddsf");
		sql.addFrom("pessoa", "pf");
		sql.addFrom("filial", "fo");
		sql.addFrom("filial", "fd");
		sql.addFrom("IMPOSTO_SERVICO", "ims");
		sql.addFrom("TABELA_PRECO", "tp");
		sql.addFrom("TIPO_TABELA_PRECO", "ttp");
		sql.addFrom("SUBTIPO_TABELA_PRECO", "stp");
		sql.addFrom("TARIFA_PRECO", "trp");
		sql.addFrom("LIBERACAO_DOC_SERV", "lds");
		sql.addFrom("usuario", "u");
		sql.addFrom("usuario", "ul");

		sql.addJoin("ds.id_docto_servico", "c.id_conhecimento");
		sql.addJoin("ds.id_servico", "s.id_servico");
		sql.addJoin("ds.id_cliente_remetente", "pr.id_pessoa");
		sql.addJoin("ds.id_cliente_destinatario", "pd.id_pessoa");
		sql.addJoin("ds.id_docto_servico", "ddsf.id_docto_servico");
		sql.addJoin("ddsf.id_cliente", "pf.id_pessoa");
		sql.addJoin("ds.id_filial_origem", "fo.id_filial");
		sql.addJoin("ds.id_filial_destino", "fd.id_filial");
		sql.addJoin("c.id_conhecimento", "ims.id_conhecimento(+)");
		sql.addJoin("ds.id_tabela_preco", "tp.id_tabela_preco(+)");
		sql.addJoin("tp.id_tipo_tabela_preco", "ttp.id_tipo_tabela_preco(+)");
		sql.addJoin("tp.ID_SUBTIPO_TABELA_PRECO", "stp.ID_SUBTIPO_TABELA_PRECO(+)");
		sql.addJoin("ds.ID_TARIFA_PRECO", "trp.id_tarifa_preco(+)");
		sql.addJoin("nvl(ims.TP_IMPOSTO,'IS')", "'IS'");
		sql.addJoin("ds.ID_DOCTO_SERVICO", "lds.ID_DOCTO_SERVICO(+)");
		sql.addJoin("lds.ID_USUARIO", "ul.ID_USUARIO(+)");
		sql.addJoin("u.ID_USUARIO", "ds.ID_USUARIO_INCLUSAO");

		sql.addCustomCriteria("c.TP_SITUACAO_CONHECIMENTO in ('E','B')");

		final String DATA = "(ds.dh_emissao >= to_date('%s:00','dd/mm/yyyy hh24:mi::ss') and ds.dh_emissao <= to_date('%s:59','dd/mm/yyyy hh24:mi:ss'))";
		/*
		 * rownum = 1 tem performance melhor do que distinct em bancos de dados
		 * Oracle
		 */
		final String CLIENTE_EDI = "((nvl((select 'S' from CLIENTE_EDI_FILIAL_EMBARC where CLIE_PESS_ID_PESSOA = ds.id_cliente_remetente and rownum = 1),'N') = 'S' and c.bl_indicador_edi = 'N' and 'S'='%s') or 'N'='%s')";

		final YearMonthDay data = (YearMonthDay) parameters.get("data");
		final TimeOfDay hrInicio = (TimeOfDay) parameters.get("hrInicio");
		final TimeOfDay hrFim = (TimeOfDay) parameters.get("hrFim");

		final String dataIni = data.toString("dd/MM/yyyy").concat(" ").concat(hrInicio.toString("HH:mm"));
		final String dataFim = data.toString("dd/MM/yyyy").concat(" ").concat(hrFim.toString("HH:mm"));
		final Long idFilial = MapUtils.getLong(parameters, "idFilial");

		String clienteEdi = "N";
		if(BooleanUtils.isTrue(MapUtils.getBoolean(parameters, "blClienteEdi"))) {
			clienteEdi = "S";
		}
		sql.addCustomCriteria(String.format(DATA, dataIni, dataFim));
		sql.addCustomCriteria(String.format(CLIENTE_EDI, clienteEdi, clienteEdi));
		sql.addCriteria("c.id_filial_origem", "=", idFilial);
		return sql;
	}

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		throw new IllegalStateException("Chame o metodo executeReport para esta service");
	}

}