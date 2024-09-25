package com.mercurio.lms.portaria.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.portaria.model.service.RegistroAuditoriaService;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.portaria.emitirResultadosAuditoriaCargaAnaliseService"
 * @spring.property name="reportName" value="com/mercurio/lms/portaria/report/emitirResultadosAuditoriaCargaAnalise.jasper"
 */
public class EmitirResultadosAuditoriaCargaAnaliseService extends ReportServiceSupport {
	
	private RegistroAuditoriaService registroAuditoriaService;
	
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("tp_resultado", "DM_RESULTADO_AUDITORIA");
		config.configDomainField("tp_controle_carga", "DM_TIPO_CONTROLE_CARGAS");
		super.configReportDomains(config);
	}
	
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap) parameters;
		
		Long idRegistroAuditoria = map.getLong("idRegistroAuditoria");
		SqlTemplate sql = montaSqlPrincipal(idRegistroAuditoria);
		
		boolean isReemitir = !registroAuditoriaService.updateDhEmissao(idRegistroAuditoria);
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		Map parametros = new HashMap();
		parametros.put("isReemitir", Boolean.valueOf(isReemitir));
		jr.setParameters(parametros);
		
		return jr;
	}
	
	

	
	public JRDataSource executeLacres(Long idRegistroAuditoria, String blOriginal) throws Exception {
				
		SqlTemplate sql = montaSqlLacre(idRegistroAuditoria, blOriginal);
		     
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();		
	}
	
	public JRDataSource executeEquipe(Long idRegistroAuditoria) throws Exception {
		
		SqlTemplate sql = montaSqlEquipe(idRegistroAuditoria);
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();		
	}
	
	public JRDataSource executeRNC(Long idRegistroAuditoria) throws Exception {
		
		SqlTemplate sql = montaSqlRNC(idRegistroAuditoria);
		     
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();		
	}
	
	/**
	 * Sql para consultar os dados da auditoria
	 * @param idRegistroAuditoria
	 * @return
	 */
	private SqlTemplate montaSqlPrincipal(Long idRegistroAuditoria){
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("ra.id_registro_auditoria");
		sql.addProjection("ra.nr_registro_auditoria");
		sql.addProjection("ra.ob_comentarios");
		sql.addProjection("ra.dh_liberacao");
		sql.addProjection("ra.dh_emissao");
		sql.addProjection("f.sg_filial", "sg_filial_auditoria");
		sql.addProjection("fil_pes.nm_fantasia", "nm_filial_auditoria");
		sql.addProjection("ra.tp_resultado");
		sql.addProjection("f_origem_cc.sg_filial", "sg_filial_origem");
		sql.addProjection("cc.nr_controle_carga");
		sql.addProjection("cc.tp_controle_carga");
		sql.addProjection("nvl2(f_destino_cc.sg_filial, f_destino_cc.sg_filial||' - '||pes_f_destino_cc.nm_fantasia, pes_f_destino_cc.nm_fantasia)", "sg_filial_destino");
		sql.addProjection("pes_f_destino_cc.nm_fantasia", "nm_filial_destino");
		sql.addProjection("pes_m.tp_identificacao");
		sql.addProjection("pes_m.nr_identificacao");
		sql.addProjection("pes_m.nm_pessoa", "nm_motorista");
		sql.addProjection("mt.nr_frota");
		sql.addProjection("mt.nr_identificador");
		sql.addProjection("reb.nr_frota", "nr_frota_reboque");
		sql.addProjection("reb.nr_identificador", "nr_identificador_reboque");	
		sql.addProjection(new StringBuffer()
							.append("(select max(dh_evento)") 
						    .append(" from evento_controle_carga") 
							.append(" where tp_evento_controle_carga = 'EM'") 
							.append(" 	    and id_controle_carga = cc.id_controle_carga)").toString(),
							"dh_emissao_cc");
		
		sql.addFrom("registro_auditoria", "ra");
		sql.addFrom("filial", "f");
		sql.addFrom("pessoa", "fil_pes");
		sql.addFrom("controle_carga", "cc");
		sql.addFrom("filial", "f_origem_cc");     
		sql.addFrom("filial", "f_destino_cc");
		sql.addFrom("pessoa", "pes_f_destino_cc");     
		sql.addFrom("pessoa", "pes_m");
		sql.addFrom("meio_transporte", "mt");
		sql.addFrom("meio_transporte", "reb");
		
		sql.addJoin("ra.id_filial", "f.id_filial");
		sql.addJoin("f.id_filial", "fil_pes.id_pessoa");
		sql.addJoin("ra.id_controle_carga", "cc.id_controle_carga");
		sql.addJoin("cc.id_filial_origem", "f_origem_cc.id_filial");
		sql.addJoin("cc.id_filial_destino", "f_destino_cc.id_filial(+)");
		sql.addJoin("pes_f_destino_cc.id_pessoa(+)", "f_destino_cc.id_filial");
		sql.addJoin("cc.id_motorista", " pes_m.id_pessoa");
		sql.addJoin("ra.id_meio_transporte", "mt.id_meio_transporte");
		sql.addJoin("ra.id_semi_reboque", "reb.id_meio_transporte (+)");
		
		sql.addCriteria("ra.id_registro_auditoria", "=", idRegistroAuditoria);
		
		return sql;
	}

	/**
	 * Sql para listar os membros da equipe da auditoria
	 * @param idRegistroAuditoria
	 * @return
	 */
	private SqlTemplate montaSqlEquipe(Long idRegistroAuditoria){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("u.nm_usuario", "nm_funcionario");
		sql.addFrom("registro_auditoria", "ra");        
		sql.addFrom("equipe_auditoria", "ea");
		sql.addFrom("usuario", "u");
		
		
		sql.addJoin("ra.id_registro_auditoria", "ea.id_registro_auditoria");            
		sql.addJoin("ea.id_usuario", "u.id_usuario");		
		sql.addCriteria("ra.id_registro_auditoria", "=", idRegistroAuditoria);
		
		return sql;
	}
	
	/**
	 * Sql para listar os lacres do controle de carga
	 * @param idRegistroAuditoria
	 * @param blOriginal
	 * @return
	 */
	private SqlTemplate montaSqlLacre(Long idRegistroAuditoria, String blOriginal){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("lcc.nr_lacres");
		sql.addFrom("lacre_registro_auditoria", "lra");
		sql.addFrom("lacre_controle_carga", "lcc");
		
		sql.addJoin("lra.id_lacre_controle_carga", "lcc.id_lacre_controle_carga");
		sql.addCriteria("lra.bl_original", "=", blOriginal);
		sql.addCriteria("lra.id_registro_auditoria", "=", idRegistroAuditoria);
				
		return sql;
	}
	
	/**
	 * Sql para listar as nao-conformidades levantadas pela auditoria
	 * @param idRegistroAuditoria
	 * @return
	 */
	private SqlTemplate montaSqlRNC(Long idRegistroAuditoria){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("nca.id_nao_conformidade_auditoria");
		sql.addProjection("nc.nr_nao_conformidade");
		sql.addProjection("f.sg_filial");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("ma_nc.ds_motivo_abertura_i"), "ds_motivo_abertura");
		sql.addProjection("ocn.ds_ocorrencia_nc");
				
		sql.addFrom("nao_conformidade_auditoria", "nca");
		sql.addFrom("filial", "f");
		sql.addFrom("nao_conformidade", "nc");
		sql.addFrom("ocorrencia_nao_conformidade", "ocn");
		sql.addFrom("motivo_abertura_nc", "ma_nc");
				
		sql.addJoin("f.id_filial", "nc.id_filial");
		sql.addJoin("nca.id_nao_conformidade", "nc.id_nao_conformidade");
		sql.addJoin("ocn.id_nao_conformidade", "nc.id_nao_conformidade");
		sql.addJoin("ma_nc.id_motivo_abertura_nc", "ocn.id_motivo_abertura_nc");
	    sql.addCriteria("nca.id_registro_auditoria", "=", idRegistroAuditoria);
		sql.addOrderBy("nca.id_nao_conformidade_auditoria");
		
		return sql;
	}


	/**
	 * @param registroAuditoriaService The registroAuditoriaService to set.
	 */
	public void setRegistroAuditoriaService(
			RegistroAuditoriaService registroAuditoriaService) {
		this.registroAuditoriaService = registroAuditoriaService;
	}
}
