package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneContatoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.VersaoPceService;
/**
 * @author luisfco
 *
 * @spring.bean id="lms.vendas.emitirPCEService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirPCE.jasper"
 */
public class EmitirPCEService extends ReportServiceSupport {
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneContatoService telefoneContatoService;
	private ContatoService contatoService;
	private VersaoPceService versaoPceService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;

		Long idVersaoPce = tfm.getLong("idVersaoPce");
		Long idCliente = tfm.getLong("cliente.idCliente");
		Long nrVersaoPce = tfm.getLong("nrVersaoPce");

		Pessoa pessoaCliente = pessoaService.findById(idCliente);
		String nmCliente = pessoaCliente.getNmPessoa();
		String nrIdentificacao = FormatUtils.formatIdentificacao(pessoaCliente.getTpIdentificacao(), 
							pessoaCliente.getNrIdentificacao());
		VarcharI18n tpIdentificacao = pessoaCliente.getTpIdentificacao().getDescription();

		YearMonthDay dtVigenciaInicial = tfm.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtVigenciaFinal = tfm.getYearMonthDay("dtVigenciaFinal");

		SqlTemplate sql = getSqlTemplate(idVersaoPce);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("NM_CLIENTE", nmCliente);
		parametersReport.put("NR_IDENTIFICACAO", nrIdentificacao);
		parametersReport.put("TP_IDENTIFICACAO", tpIdentificacao.getValue());
		parametersReport.put("NR_VERSAO", nrVersaoPce);

		if (dtVigenciaInicial != null)
			parametersReport.put("DT_VIGENCIA_INICIAL",JTFormatUtils.format(dtVigenciaInicial));
		if (dtVigenciaFinal != null)
			parametersReport.put("DT_VIGENCIA_FINAL",JTFormatUtils.format(dtVigenciaFinal));
		jr.setParameters(parametersReport);

		return jr; 
	}

	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("DS_TP_ACAO", "DM_ACOES_PCE"); 
		config.configDomainField("DS_TP_FORMA_COMUNICACAO", "DM_FORMA_COMUNICACAO_PCE");
	} 

	private SqlTemplate getSqlTemplate(Long idVersaoPCE) {
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PPCE.DS_PROCESSO_PCE_I"),"DS_PROCESSO_PCE");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("EPCE.DS_EVENTO_PCE_I"),"DS_EVENTO_PCE");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("OPCE.DS_OCORRENCIA_PCE_I"),"DS_OCORRENCIA_PCE");
		sql.addProjection("DPCE.TP_ACAO","TP_ACAO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("DPCE.DS_DESCRITIVO_PCE_I"), "DS_DESCRITIVO_PCE");
		sql.addProjection("DPCE.BL_INDICADOR_AVISO");
		sql.addProjection("CON.NM_CONTATO");
		sql.addProjection("CON.ID_CONTATO");
		sql.addProjection("VCPCE.TP_FORMA_COMUNICACAO");
		sql.addProjection("NVL(VCPCE.DS_REGIAO_ATUACAO, '')", "DS_REGIAO_ATUACAO");

		sql.addProjection("DPCE.TP_ACAO", "DS_TP_ACAO");

		sql.addProjection("NVL(VCPCE.TP_FORMA_COMUNICACAO, '')", "DS_TP_FORMA_COMUNICACAO");

		sql.addFrom("PROCESSO_PCE", "PPCE");
		sql.addFrom("EVENTO_PCE", "EPCE");		
		sql.addFrom("OCORRENCIA_PCE", "OPCE");
		sql.addFrom("DESCRITIVO_PCE", "DPCE");		
		sql.addFrom("VERSAO_DESCRITIVO_PCE", "VDPCE");
		sql.addFrom("VERSAO_PCE", "VPCE");		
		sql.addFrom("VERSAO_CONTATO_PCE", "VCPCE");
		sql.addFrom("CONTATO", "CON");		

		sql.addJoin("PPCE.ID_PROCESSO_PCE", "EPCE.ID_PROCESSO_PCE");
		sql.addJoin("EPCE.ID_EVENTO_PCE", "OPCE.ID_EVENTO_PCE");		
		sql.addJoin("OPCE.ID_OCORRENCIA_PCE", "DPCE.ID_OCORRENCIA_PCE");
		sql.addJoin("VDPCE.ID_DESCRITIVO_PCE", "DPCE.ID_DESCRITIVO_PCE");		
		sql.addJoin("VDPCE.ID_VERSAO_PCE", "VPCE.ID_VERSAO_PCE");
		sql.addJoin("VCPCE.ID_VERSAO_DESCRITIVO_PCE(+)", "VDPCE.ID_VERSAO_DESCRITIVO_PCE");		
		sql.addJoin("VCPCE.ID_CONTATO", "CON.ID_CONTATO(+)");

		sql.addCriteria("VPCE.ID_VERSAO_PCE", "=", idVersaoPCE);

		sql.addOrderBy("DS_PROCESSO_PCE");
		sql.addOrderBy("DS_EVENTO_PCE");
		sql.addOrderBy("DS_OCORRENCIA_PCE");

		sql.addOrderBy("DS_DESCRITIVO_PCE");
		sql.addOrderBy("CON.NM_CONTATO");
		sql.addOrderBy("VCPCE.DS_REGIAO_ATUACAO");
		sql.addOrderBy("DS_TP_FORMA_COMUNICACAO");

		return sql;
	}
	

	public JRDataSource executeSubReport(Long idContato,String tpFormaComunicacao) throws Exception {
		List values = new ArrayList();
		StringBuffer sb;
		if (idContato == null || tpFormaComunicacao == null)
			return new JRMapCollectionDataSource(values);
		if (tpFormaComunicacao.equalsIgnoreCase("T")) {
			//TELEFONE
			List rsC = telefoneContatoService.findTelefoneByContatoAndTpUso(idContato,new String[] {"FO","FF"});
			for(Iterator ie = rsC.iterator(); ie.hasNext();) {
				Object[] pC = (Object[])ie.next();
				Map valuesLine = new HashMap();
				sb = new StringBuffer();
				if (pC[0] != null)
					sb.append("(").append(pC[0]).append(")");
				if (pC[1] != null)
					sb.append("(").append(pC[1]).append(")");
				if (pC[2] != null)
					sb.append(" - ").append(pC[2]);
				valuesLine.put("DS_DETALHE",sb.toString());
				values.add(valuesLine);
			}
		} else if (tpFormaComunicacao.equalsIgnoreCase("F")) {
			//FAX
			List rsC = telefoneContatoService.findTelefoneByContatoAndTpUso(idContato,new String[] {"FA","FF"});
			for(Iterator ie = rsC.iterator(); ie.hasNext();) {
				Object[] pC = (Object[])ie.next();
				sb = new StringBuffer();
				Map valuesLine = new HashMap();
				if (pC[0] != null)
					sb.append("(").append(pC[0]).append(")");
				if (pC[1] != null)
					sb.append("(").append(pC[1]).append(")");
				if (pC[2] != null)
					sb.append(" - ").append(pC[2]);
				valuesLine.put("DS_DETALHE",sb.toString());
				values.add(valuesLine);
			}
		} else if (tpFormaComunicacao.equalsIgnoreCase("C")) {
			//CORRESPONDENCIA
			EnderecoPessoa ep = enderecoPessoaService.findEnderecoPessoaPadrao(contatoService.findById(idContato).getPessoa().getIdPessoa());
			if (ep != null) {
				sb = new StringBuffer();
				Map valuesLine = new HashMap();
				valuesLine.put("DS_DETALHE",enderecoPessoaService.formatEnderecoPessoa(ep));
				values.add(valuesLine);
			}
		} else if (tpFormaComunicacao.equalsIgnoreCase("E")) {
			//EMAIL
			Contato c = contatoService.findById(idContato);
			Map valuesLine = new HashMap();
			sb = new StringBuffer();
			if (c.getDsEmail() != null)
				sb.append(c.getDsEmail());
			valuesLine.put("DS_DETALHE",sb.toString());
			values.add(valuesLine);
		} else if (tpFormaComunicacao.equalsIgnoreCase("S")) {
			//SMS - CELULAR
			List rsC = versaoPceService.findTelefoneContatoByTpTelefone(idContato,new String[] {"E"});
			if (rsC.size() > 0) {
				TelefoneEndereco bean = ((TelefoneContato)rsC.get(0)).getTelefoneEndereco();
				HashMap valuesLine = new HashMap();
				sb = new StringBuffer();
				if (StringUtils.isNotBlank(bean.getNrDdi()))
					sb.append("(").append(bean.getNrDdi()).append(")");
				if (StringUtils.isNotBlank(bean.getNrDdd()))
					sb.append("(").append(bean.getNrDdd()).append(")");
				if (StringUtils.isNotBlank(bean.getNrTelefone()))
					sb.append(" - ").append(bean.getNrTelefone());
				
				valuesLine.put("DS_DETALHE",sb.toString());
				values.add(valuesLine);
			}
		}

		return new JRMapCollectionDataSource(values);
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setTelefoneContatoService(TelefoneContatoService telefoneContatoService) {
		this.telefoneContatoService = telefoneContatoService;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	public void setVersaoPceService(VersaoPceService versaoPceService) {
		this.versaoPceService = versaoPceService;
	}

}
