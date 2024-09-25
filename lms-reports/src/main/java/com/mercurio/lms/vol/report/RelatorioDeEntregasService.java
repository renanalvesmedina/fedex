package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * 
 * @spring.bean id="lms.vol.relatorioDeEntregasService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/RelatorioDeEntregas.jasper"
 */
public class RelatorioDeEntregasService extends ReportServiceSupport {
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		Map parametersReport = new HashMap();
		
		SqlTemplate sql = null;
		
		int codRelatorio = Integer.parseInt((String)parameters.get("codRelatorio"));
		
		switch (codRelatorio){
		case 1: sql = this.montaSqlENT1(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41027"));
				break;
		case 2: sql = this.montaSqlENT2(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41028"));
				break;
		case 3: sql = this.montaSqlENT3(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41029"));
				break;
		case 4: sql = this.montaSqlENT4(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41030"));
				break;
		case 5: sql = this.montaSqlENT5(parameters);
				parametersReport.put("tituloRelatorio",getConfiguracoesFacade().getMensagem("LMS-41047"));
				break;		
		}
	
			
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		jr.setParameters(parametersReport);
        
        return jr;
	}
	
    public SqlTemplate montaSqlENT5(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" 	metr.nr_frota, ")
			.append(" fme.sg_filial || ' ' || LPAD(me.nr_manifesto_entrega,8,0) manifesto ,") 
			.append(" fds.sg_filial || ' ' || LPAD(ds.nr_docto_servico,8,0) documento, ") 
			.append("	ds.ds_endereco_entrega_real endereco, ")
			.append("	p.nr_identificacao nr_identificacao, ")
			.append("	p.tp_identificacao tp_identificacao, ")
			.append("	p.nm_pessoa cliente, ")
			.append("	nfc.qt_volumes volumes, ")
			.append("	med.dh_ocorrencia baixa, ")
			.append(PropertyVarcharI18nProjection.createProjection("oe.ds_ocorrencia_entrega_i","baixa_")).append(",")
			.append("	ds.dt_prev_entrega dpe ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" manifesto_entrega me ")
	    	.append(" 	left join filial fme ON fme.id_filial = me.id_filial ")
	    	.append(" 	left join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
	    	.append(" 	left join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
	    	.append(" 	left join meio_transporte metr on metr.id_meio_transporte = cg.id_transportado ")
	    	.append("	left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
	    	.append(" 	left join docto_servico ds on ds.id_docto_servico = med.id_docto_servico ")
	    	.append("   left join filial fds ON fds.id_filial = ds.id_filial_origem ")
	    	.append(" 	inner join pessoa p on p.id_pessoa = ds.ID_CLIENTE_DESTINATARIO ")
	    	.append(" 	left join nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = med.id_nota_fiscal_conhecimento ")
	    	.append(" 	left join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ");
		sql.addFrom(query.toString());

		sql.addCriteria("metr.id_meio_transporte","=",criteria.get("idFrota"));
		sql.addCriteria("fme.id_filial","=",criteria.get("idFilial"));
		sql.addCustomCriteria("cg.tp_status_controle_carga not in ('FE','CA')");
		sql.addCustomCriteria("med.tp_situacao_documento not in ('CANC')");
		
    	
		List l = getFilialService().findNmSgFilialByIdFilial(Long.valueOf(""+criteria.get("idFilial")));
		TypedFlatMap t = (TypedFlatMap)l.get(0);
		
		sql.addFilterSummary("filial",t.getString("sgFilial") + " - " + t.getString("pessoa.nmFantasia"));
		
		sql.addOrderBy("me.nr_manifesto_entrega, ds.nr_docto_servico");
		
		return sql;		
	}

	public SqlTemplate montaSqlENT4(Map criteria){
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" DISTINCT med.id_docto_servico, ")
			.append(" metr.nr_frota, ") 
			.append(" fme.sg_filial || ' ' || LPAD(me.nr_manifesto_entrega,8,0) manifesto ,") 
			.append(" fds.sg_filial || ' ' || LPAD(ds.nr_docto_servico,8,0) documento, ") 
			.append(" ds.ds_endereco_entrega_real endereco, ") 
			.append(" p.nr_identificacao nr_identificacao, ")
			.append(" p.tp_identificacao tp_identificacao, ")
			.append(" p.nm_pessoa cliente, ")
			.append(" nfc.qt_volumes volumes, ") 
			.append(" med.dh_ocorrencia baixa, ") 
			.append(PropertyVarcharI18nProjection.createProjection("oe.ds_ocorrencia_entrega_i","baixa_")).append(",")
			.append(" ds.dt_prev_entrega dpe ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" manifesto_entrega_documento med ")
	    	.append(" left join manifesto_entrega me ON me.id_manifesto_entrega = med.id_manifesto_entrega ")  
	    	.append(" left join filial fme ON fme.id_filial = me.id_filial ")
	    	.append(" left join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ") 
	    	.append(" left join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ") 
	    	.append(" left join meio_transporte metr on metr.id_meio_transporte = cg.id_transportado ") 
	    	.append(" left join docto_servico ds on ds.id_docto_servico = med.id_docto_servico ") 
	    	.append(" left join filial fds ON fds.id_filial = ds.id_filial_origem ")
	    	.append(" inner join pessoa p on p.id_pessoa = ds.ID_CLIENTE_DESTINATARIO ") 
	    	.append(" left join nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = med.id_nota_fiscal_conhecimento ") 
	    	.append(" left join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ");
		sql.addFrom(query.toString());
		
		StringBuffer subSelect = new StringBuffer()
			.append(" med.id_docto_servico IN ( ") 
            .append("            SELECT med.id_docto_servico ")
            .append("            FROM manifesto_entrega me ")
            .append("                inner join filial f ON f.id_filial = me.id_filial ")	
            .append("                inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")	
            .append("                inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")	
            .append("                inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")	
            .append("            WHERE ")
            .append("                f.id_filial = ? ")
            .append("                and me.dh_emissao between ? and ? ")
            .append("                AND id_transportado = ? ")
            .append("                AND med.tp_situacao_documento <> 'C' ")
            .append("            	 AND exists ( ")
            .append("                           select * ")
            .append("                           from manifesto_entrega_documento aux ")
            .append("                           where aux.id_docto_servico = med.id_docto_servico ")
            .append("                           and aux.id_manifesto_entrega_documento <> med.id_manifesto_entrega_documento ")
            .append("                     ) ")
            .append("            GROUP BY cg.id_transportado, med.id_docto_servico ")
            .append("        	) ");
		sql.addCustomCriteria(subSelect.toString());
		
		Map filial = (Map)criteria.get("filial");	
		sql.addCriteriaValue(filial.get("idFilial"));
		
		YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		
		sql.addCriteriaValue(criteria.get("idFrota"));
		
		List l = getFilialService().findNmSgFilialByIdFilial(Long.valueOf(""+filial.get("idFilial")));
		TypedFlatMap t = (TypedFlatMap)l.get(0);
		
		sql.addFilterSummary("filial",t.getString("sgFilial") + " - " + t.getString("pessoa.nmFantasia"));
		
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
		
		sql.addCriteria("cg.id_transportado","=",criteria.get("idFrota"));
		sql.addOrderBy("manifesto, documento");
		
		return sql;		
	}
	
	
	public SqlTemplate montaSqlENT3(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" 	metr.nr_frota, ")
			.append("   fme.sg_filial || ' ' || LPAD(me.nr_manifesto_entrega,8,0) manifesto ,") 
			.append("   fds.sg_filial || ' ' || LPAD(ds.nr_docto_servico,8,0) documento, ")
			.append("	ds.ds_endereco_entrega_real endereco, ")
			.append("	p.nr_identificacao nr_identificacao, ")
			.append("	p.tp_identificacao tp_identificacao, ")
			.append("	p.nm_pessoa cliente, ")
			.append("	nfc.qt_volumes volumes, ")
			.append("	med.dh_ocorrencia baixa, ")
			.append(PropertyVarcharI18nProjection.createProjection("oe.ds_ocorrencia_entrega_i","baixa_")).append(",")
			.append("	ds.dt_prev_entrega dpe ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" manifesto_entrega me ")
	    	.append(" 	left join filial fme ON fme.id_filial = me.id_filial ")
	    	.append(" 	left join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
	    	.append(" 	left join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
	    	.append(" 	left join meio_transporte metr on metr.id_meio_transporte = cg.id_transportado ")
	    	.append("	left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
	    	.append(" 	left join docto_servico ds on ds.id_docto_servico = med.id_docto_servico ")
	    	.append("   left join filial fds ON fds.id_filial = ds.id_filial_origem ")
	    	.append(" 	inner join pessoa p on p.id_pessoa = ds.ID_CLIENTE_DESTINATARIO ")
	    	.append(" 	left join nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = med.id_nota_fiscal_conhecimento ")
	    	.append(" 	left join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ");
		sql.addFrom(query.toString());
		
		sql.addCustomCriteria(" oe.id_ocorrencia_entrega IS NULL ");
		sql.addCriteria("metr.id_meio_transporte","=",criteria.get("idFrota"));
		
		Map filial = (Map)criteria.get("filial");
		sql.addCriteria("fme.id_filial","=",filial.get("idFilial"));
		
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		    	
    	sql.addCustomCriteria(" me.dh_emissao between ? and ? ");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		
		List l = getFilialService().findNmSgFilialByIdFilial(Long.valueOf(""+filial.get("idFilial")));
		TypedFlatMap t = (TypedFlatMap)l.get(0);
		
		sql.addFilterSummary("filial",t.getString("sgFilial") + " - " + t.getString("pessoa.nmFantasia"));
		
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
		
		sql.addOrderBy("me.nr_manifesto_entrega, ds.nr_docto_servico");
		
		System.out.println("\n "+sql.getSql());
		
		for (int i = 0; i < sql.getCriteria().length; i++){
			System.out.println(sql.getCriteria()[i]);
		}
		
		return sql;		
	}
	
	public SqlTemplate montaSqlENT2(Map criteria){
		
		SqlTemplate sql = createSqlTemplate(); 
		
		StringBuilder pj = new StringBuilder()
			.append(" 	metr.nr_frota, ")
			.append(" fme.sg_filial || ' ' || LPAD(me.nr_manifesto_entrega,8,0) manifesto ,") 
			.append(" fds.sg_filial || ' ' || LPAD(ds.nr_docto_servico,8,0) documento, ")
			.append("	ds.ds_endereco_entrega_real endereco, ")
			.append("	p.nr_identificacao nr_identificacao, ")
			.append("	p.tp_identificacao tp_identificacao, ")
			.append("	p.nm_pessoa cliente, ")
			.append("	nfc.qt_volumes volumes, ")
			.append("	med.dh_ocorrencia baixa, ")
			.append(PropertyVarcharI18nProjection.createProjection("oe.ds_ocorrencia_entrega_i","baixa_")).append(",")
			.append("	ds.dt_prev_entrega dpe ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" manifesto_entrega me ")
	    	.append(" 	left join filial fme ON fme.id_filial = me.id_filial ")
	    	.append(" 	left join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
	    	.append(" 	left join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
	    	.append(" 	left join meio_transporte metr on metr.id_meio_transporte = cg.id_transportado ")
	    	.append("	left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
	    	.append(" 	left join docto_servico ds on ds.id_docto_servico = med.id_docto_servico ")
	    	.append("   left join filial fds ON fds.id_filial = ds.id_filial_origem ")
	    	.append(" 	inner join pessoa p on p.id_pessoa = ds.ID_CLIENTE_DESTINATARIO ")
	    	.append(" 	left join nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = med.id_nota_fiscal_conhecimento ")
	    	.append(" 	inner join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega and oe.tp_ocorrencia = 'N'");
		sql.addFrom(query.toString());

		sql.addCriteria("metr.id_meio_transporte","=",criteria.get("idFrota"));
		
		Map filial = (Map)criteria.get("filial");
		sql.addCriteria("fme.id_filial","=",filial.get("idFilial"));
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		    	
    	sql.addCustomCriteria(" me.dh_emissao between ? and ? ");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		
		List l = getFilialService().findNmSgFilialByIdFilial(Long.valueOf(""+filial.get("idFilial")));
		TypedFlatMap t = (TypedFlatMap)l.get(0);
		
		sql.addFilterSummary("filial",t.getString("sgFilial") + " - " + t.getString("pessoa.nmFantasia"));
		
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
		
		sql.addOrderBy("me.nr_manifesto_entrega, ds.nr_docto_servico");
		
		System.out.println("\n "+sql.getSql());
		
		for (int i = 0; i < sql.getCriteria().length; i++){
			System.out.println(sql.getCriteria()[i]);
		}
		
		return sql;		
	}

	public SqlTemplate montaSqlENT1(Map criteria){
		
		SqlTemplate sql = createSqlTemplate();
		
		StringBuilder pj = new StringBuilder()
			.append(" 	metr.nr_frota, ")
			.append(" fme.sg_filial || ' ' || LPAD(me.nr_manifesto_entrega,8,0) manifesto ,") 
			.append(" fds.sg_filial || ' ' || LPAD(ds.nr_docto_servico,8,0) documento, ") 
			.append("	ds.ds_endereco_entrega_real endereco, ")
			.append("	p.nr_identificacao nr_identificacao, ")
			.append("	p.tp_identificacao tp_identificacao, ")
			.append("	p.nm_pessoa cliente, ")
			.append("	nfc.qt_volumes volumes, ")
			.append("	med.dh_ocorrencia baixa, ")
			.append(PropertyVarcharI18nProjection.createProjection("oe.ds_ocorrencia_entrega_i","baixa_")).append(",")
			.append("	ds.dt_prev_entrega dpe ");
		sql.addProjection(pj.toString());
    
		StringBuilder query = new StringBuilder()
	    	.append(" manifesto_entrega me ")
	    	.append(" 	left join filial fme ON fme.id_filial = me.id_filial ")
	    	.append(" 	left join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
	    	.append(" 	left join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
	    	.append(" 	left join meio_transporte metr on metr.id_meio_transporte = cg.id_transportado ")
	    	.append("	left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
	    	.append(" 	left join docto_servico ds on ds.id_docto_servico = med.id_docto_servico ")
	    	.append("   left join filial fds ON fds.id_filial = ds.id_filial_origem ")
	    	.append(" 	inner join pessoa p on p.id_pessoa = ds.ID_CLIENTE_DESTINATARIO ")
	    	.append(" 	left join nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = med.id_nota_fiscal_conhecimento ")
	    	.append(" 	left join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ");
		sql.addFrom(query.toString());

		sql.addCriteria("metr.id_meio_transporte","=",criteria.get("idFrota"));
		
		Map filial = (Map)criteria.get("filial");
		sql.addCriteria("fme.id_filial","=",filial.get("idFilial"));
		
    	YearMonthDay dataInicial = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataInicial"), YearMonthDay.class);
    	YearMonthDay dataFinal = (YearMonthDay) ReflectionUtils.toObject((String)criteria.get("dataFinal"), YearMonthDay.class);
		    	
    	sql.addCustomCriteria(" me.dh_emissao between ? and ? ");
		sql.addCriteriaValue(dataInicial);
		sql.addCriteriaValue(dataFinal);
		
		List l = getFilialService().findNmSgFilialByIdFilial(Long.valueOf(""+filial.get("idFilial")));
		TypedFlatMap t = (TypedFlatMap)l.get(0);
		
		sql.addFilterSummary("filial",t.getString("sgFilial") + " - " + t.getString("pessoa.nmFantasia"));
		
		sql.addFilterSummary("periodoInicial", dataInicial);
		sql.addFilterSummary("periodoFinal", dataFinal);
		
		sql.addOrderBy("me.nr_manifesto_entrega, ds.nr_docto_servico");
		
		System.out.println("\n "+sql.getSql());
		
		for (int i = 0; i < sql.getCriteria().length; i++){
			System.out.println(sql.getCriteria()[i]);
		}
		
		return sql;		
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}	
	
	
} 