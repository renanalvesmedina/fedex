package com.mercurio.lms.contasreceber.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.BaixaDevMerc;
import com.mercurio.lms.contasreceber.model.service.BaixaDevMercService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *
 * Emissao de BDM - Baixa por Devolucao de Mercadoria
 * @author Rafael Andrade de Oliveira
 * @since 27/03/2006
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirBDM.jasper" 
 * @spring.bean id="lms.contasreceber.emitirBDMService"
 */

public class EmitirBDMService extends ReportServiceSupport {
	
	BaixaDevMercService baixaDevMercService;
	public void setBaixaDevMercService(BaixaDevMercService baixaDevMercService) {
		this.baixaDevMercService = baixaDevMercService;
	}

	private DomainValueService domainValueService;
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public JRReportDataObject execute(Map parameters) {

		if (parameters.get("filial.idFilial") != null && parameters.get("nrBdm") != null && !parameters.get("nrBdm").toString().equals("")) {
			BaixaDevMerc baixaDevMerc = baixaDevMercService.findBaixaDevMerc((TypedFlatMap) parameters);
			
			if (baixaDevMerc != null) {
				if (baixaDevMerc.getTpSituacao().getValue().equals("C")) {
					throw new BusinessException("LMS-36060");
				}
			}
		}
		
		/* Lista de ids a atualizar no banco */
		final List idsBaixaDevMerc = new ArrayList();
		
		/* Busca SqlTemplate */
		SqlTemplate sql = getSql((TypedFlatMap) parameters);
		
		if (logger.isDebugEnabled()) {
			logger.debug("SQL Geral: \n" + sql.getSql());
		}
		
		List relatorio = getJdbcTemplate().query(sql.getSql(), sql.getCriteria(), new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map map = new HashMap();
				
				Long idBdm = Long.valueOf(rs.getLong("ID_BDM"));
				idsBaixaDevMerc.add(idBdm);
				
				map.put("NOME_FILIAL", rs.getString("NOME_FILIAL"));
				map.put("TP_IDENTIFICACAO_FILIAL", rs.getString("TP_IDENTIFICACAO_FILIAL"));
				map.put("CNPJ_FILIAL", rs.getString("CNPJ_FILIAL"));
				map.put("ORIGEM", rs.getString("ORIGEM"));
				map.put("DESTINO", rs.getString("DESTINO"));
				map.put("DATA", rs.getDate("DATA"));
				map.put("NOME_CLIENTE", rs.getString("NOME_CLIENTE"));
				map.put("ENDERECO_CLIENTE", FormatUtils.montaEnderecoCompleto(rs.getString("TP_LOGRADOURO_END_CLI"), rs.getString("DS_ENDERECO_CLI"), rs.getString("NR_ENDERECO_CLI"), rs.getString("DS_COMPLEMENTO_END_CLI")));
				map.put("CIDADE_CLIENTE", rs.getString("CIDADE_CLIENTE"));
				map.put("TP_IDENTIFICACAO_CLIENTE", rs.getString("TP_IDENTIFICACAO_CLIENTE"));
				map.put("CNPJ_CLIENTE", rs.getString("CNPJ_CLIENTE"));
				map.put("DDD_CLIENTE", rs.getString("DDD_CLIENTE"));
				map.put("DDI_CLIENTE", rs.getString("DDI_CLIENTE"));
				map.put("FONE_CLIENTE", rs.getString("FONE_CLIENTE"));
				map.put("UF_CLIENTE", rs.getString("UF_CLIENTE"));
				map.put("CEP_CLIENTE", rs.getString("CEP_CLIENTE"));
				
				String tpDocumento = domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO", rs.getString("TIPO_DOCUMENTO"));
				map.put("TIPO_DOCUMENTO", tpDocumento);
				
				String tpDocumentoNovo = domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO", rs.getString("TIPO_DOCUMENTO_NOVO"));
				map.put("TIPO_DOCUMENTO_NOVO", tpDocumentoNovo);
				
				map.put("DOCUMENTO_NOVO", rs.getString("FILIAL_NOVO") + " " + FormatUtils.formataNrDocumento(rs.getString("DOCUMENTO_NOVO"), rs.getString("TIPO_DOCUMENTO_NOVO").toString() ) );
				map.put("VALOR_NOVO", new Double(rs.getDouble("VALOR_NOVO")) );
				map.put("DOCUMENTO_ANTIGO", rs.getString("FILIAL_ANTIGO") + " " + FormatUtils.formataNrDocumento(rs.getString("DOCUMENTO_ANTIGO"),  rs.getString("TIPO_DOCUMENTO").toString() ) );
				map.put("VALOR_ANTIGO", new Double(rs.getDouble("VALOR_ANTIGO")) );
				
				return map;
			}
		});

		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(relatorio);
		
		JRReportDataObject jr = createReportDataObject(jrMap, parameters);

		Map parametersReport = new HashMap();
		
		/* Tipo do relat�rio */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		/* Parametros de pesquisa */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		/* Usuario emissor */
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        
        jr.setParameters(parametersReport);
		
        /* sql para update em propriedade do bdm */
        if (idsBaixaDevMerc != null & idsBaixaDevMerc.size() > 0) {
    		StringBuffer sqlUpdate = new StringBuffer();

			sqlUpdate.append("UPDATE BAIXA_DEV_MERC SET TP_SITUACAO = 'E' WHERE TP_SITUACAO = 'A' AND ID_BAIXA_DEV_MERC IN (");
	
			for (Iterator iter = idsBaixaDevMerc.iterator(); iter.hasNext();) {
				Long idBaixaDevMerc = (Long) iter.next();
				sqlUpdate.append(idBaixaDevMerc);
				if (iter.hasNext())
					sqlUpdate.append(", ");
			}
	
			sqlUpdate.append(")");

			if (logger.isDebugEnabled()) {
				logger.debug("SQL Update: \n" + sqlUpdate.toString());
			}
	
			/* Atualiza flag no banco */
			getJdbcTemplate().update(sqlUpdate.toString());
		}
        
		return jr;
	}
	
	public void configReportDomains(ReportDomainConfig config) {
		super.configReportDomains(config);
	}

	private SqlTemplate getSql(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection("pfo.nm_fantasia", "NOME_FILIAL");
		sql.addProjection("pfo.tp_identificacao", "TP_IDENTIFICACAO_FILIAL");
		sql.addProjection("pfo.nr_identificacao", "CNPJ_FILIAL");
		sql.addProjection("b.id_baixa_dev_merc", "ID_BDM");
		sql.addProjection("fo.sg_filial || ' ' || b.nr_bdm", "ORIGEM");
		sql.addProjection("fd.sg_filial", "DESTINO");
		sql.addProjection("b.dt_emissao", "DATA");
		sql.addProjection("pc.nm_pessoa", "NOME_CLIENTE");
		sql.addProjection("epc.ds_endereco","DS_ENDERECO_CLI");
		sql.addProjection("epc.nr_endereco","NR_ENDERECO_CLI");
		sql.addProjection("epc.ds_complemento","DS_COMPLEMENTO_END_CLI");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tlc.ds_tipo_logradouro_i"),"TP_LOGRADOURO_END_CLI");
		sql.addProjection("mc.nm_municipio", "CIDADE_CLIENTE");
		sql.addProjection("pc.tp_identificacao", "TP_IDENTIFICACAO_CLIENTE");
		sql.addProjection("pc.nr_identificacao", "CNPJ_CLIENTE");
		sql.addProjection("(SELECT te.nr_ddd from telefone_endereco te where te.id_telefone_endereco =" +
								" (SELECT MIN(tetmp.id_telefone_endereco) " +
								"  FROM telefone_endereco tetmp " +
								"  WHERE tetmp.id_endereco_pessoa = epc.id_endereco_pessoa))", "DDD_CLIENTE");
		sql.addProjection("(SELECT te.nr_ddi from telefone_endereco te where te.id_telefone_endereco =" +
								" (SELECT MIN(tetmp.id_telefone_endereco) " +
								"  FROM telefone_endereco tetmp " +
								"  WHERE tetmp.id_endereco_pessoa = epc.id_endereco_pessoa))", "DDI_CLIENTE");
		sql.addProjection("(SELECT te.nr_telefone from telefone_endereco te where te.id_telefone_endereco =" +
								" (SELECT MIN(tetmp.id_telefone_endereco) " +
								"  FROM telefone_endereco tetmp " +
								"  WHERE tetmp.id_endereco_pessoa = epc.id_endereco_pessoa))", "FONE_CLIENTE");
		sql.addProjection("ufc.sg_unidade_federativa", "UF_CLIENTE");
		sql.addProjection("epc.nr_cep", "CEP_CLIENTE");
		sql.addProjection("ds.tp_documento_servico", "TIPO_DOCUMENTO");
		sql.addProjection("dso.tp_documento_servico", "TIPO_DOCUMENTO_NOVO");
		sql.addProjection("fdso.sg_filial", "FILIAL_NOVO");
		sql.addProjection("dso.nr_docto_servico", "DOCUMENTO_NOVO");
		sql.addProjection("ddsfo.vl_devido", "VALOR_NOVO");
		sql.addProjection("fds.sg_filial", "FILIAL_ANTIGO");
		sql.addProjection("ds.nr_docto_servico", "DOCUMENTO_ANTIGO");
		sql.addProjection("ddsf.vl_devido", "VALOR_ANTIGO");

		sql.addFrom("item_baixa_dev_merc", "ib");
		sql.addFrom("baixa_dev_merc", "b");
		sql.addJoin("b.id_baixa_dev_merc", "ib.id_baixa_dev_merc");
		sql.addFrom("cliente", "c");
		sql.addJoin("c.id_cliente", "b.id_cliente");
		sql.addFrom("pessoa", "pc");
		sql.addJoin("pc.id_pessoa", "c.id_cliente");
		sql.addFrom("endereco_pessoa", "epc");
		sql.addJoin("epc.id_endereco_pessoa","pc.id_endereco_pessoa");
		sql.addFrom("municipio", "mc");
		sql.addJoin("mc.id_municipio", "epc.id_municipio");
		sql.addFrom("unidade_federativa", "ufc");
		sql.addJoin("ufc.id_unidade_federativa", "mc.id_unidade_federativa");
		
		sql.addFrom("filial", "fo"); 
		sql.addJoin("fo.id_filial", "b.id_filial_emissora");
		
		sql.addFrom("pessoa", "pfo");
		sql.addJoin("pfo.id_pessoa", "fo.id_filial");
		
		sql.addFrom("filial", "fd");
		sql.addJoin("fd.id_filial", "b.id_filial_destino");

		sql.addFrom("devedor_doc_serv_fat", "ddsf");
		sql.addJoin("ddsf.id_devedor_doc_serv_fat", "ib.id_devedor_doc_serv_fat");
		
		sql.addFrom("docto_servico", "ds");
		sql.addJoin("ds.id_docto_servico", "ddsf.id_docto_servico");
		
		sql.addFrom("filial", "fds");
		sql.addJoin("fds.id_filial", "ds.id_filial_origem");
		
		sql.addFrom("docto_servico", "dso");
		sql.addJoin("dso.id_docto_servico_original", "ds.id_docto_servico");
		
		sql.addFrom("devedor_doc_serv_fat", "ddsfo");
		sql.addJoin("ddsfo.id_docto_servico", "dso.id_docto_servico");
		
		sql.addFrom("filial", "fdso");
		sql.addJoin("fdso.id_filial", "dso.id_filial_origem");
		
		sql.addFrom("tipo_logradouro", "tlc");
		sql.addJoin("epc.id_tipo_logradouro","tlc.id_tipo_logradouro");
		
		if(tfm.getLong("nrBdm") == null){
			sql.addCriteria("b.tp_situacao", "=", "A"); // A emitir
		}
		
		sql.addCriteria("fo.id_filial", "=", tfm.getLong("filial.idFilial"));
		sql.addFilterSummary("filialOrigem", tfm.getString("sgFilial") + " - " + tfm.getString("filial.pessoa.nmFantasia"));
		
		sql.addCriteria("b.nr_bdm", "=", tfm.getLong("nrBdm"));
		sql.addFilterSummary("numero", tfm.getLong("nrBdm"));
		
		sql.addOrderBy("fo.id_filial");
		sql.addOrderBy("b.nr_bdm");
		sql.addOrderBy("fds.sg_filial");
		sql.addOrderBy("ds.nr_docto_servico");
		
		return sql;
	}
}