package com.mercurio.lms.contasreceber.report;

import br.com.tntbrasil.integracao.domains.dell.FaturaDellDMN;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.service.EventoFaturaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.util.session.SessionUtils;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.apache.commons.collections.MapUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável pela geração do Relatório de Faturas Nacionais
 * Especificação técnica 36.02.01.07
 * @author Miakcël Jalbert e José Rodrigo Moraes
 * 
 * @spring.bean id="lms.contasreceber.emitirFaturasNacionaisService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirFaturasNacionaisSuper.jasper"
 */

public class EmitirFaturasNacionaisService extends ReportServiceSupport {
	private EventoFaturaService eventoFaturaService;
	private FaturaService faturaService;
	private ConfiguracoesFacade configuracoesFacade;

	private static final String NM_PARAMETRO_LIMITE_IMPRESSAO = "LIMITE_IMPRESSAO_DACTE_FATURA";
	
	/**
	 * Executa o relatório
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/03/2007
	 * @param obj
	 * @return
	 */
	public JRReportDataObject execute(Map param){
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.putAll(param);
		
		boolean reemissao = criteria.get("reemitir") != null && criteria.getBoolean("reemitir");

		if (reemissao) { 
			Fatura fatura = faturaService.findById(criteria.getLong("fatura.idFatura"));
			if (fatura.getBlFaturaReemitida().booleanValue()) {
				throw new BusinessException("LMS-36105");
			}
		}
		
		/** Itera o resultSet para atualizar os recibos */
		List lstFatura = faturaService.iteratorSelectFatura(criteria); 
		
		if (lstFatura.size() > 0 && !reemissao){
			for (Map<String, Object> mapFatura: (List<Map<String, Object>>)lstFatura){
				Fatura fatura = new Fatura();
				fatura.setIdFatura((Long)mapFatura.get("ID_FATURA"));
				faturaService.generateTopicEntry(fatura);
			}
		}
		
		faturaService.updateFatura(criteria);
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(lstFatura);
		
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,criteria.get("tpFormatoRelatorio"));
		parametersReport.put("VIA", reemissao ? "2ª Via" : "");
		
		return createReportDataObject(jrMap,parametersReport);
	}

		

	public List<Long> findFaturasByCriteria(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();
		faturaService.getDefaultQuery(sql, criteria);
		sql.addProjection("f.id_fatura as ID_FATURA");
		sql.addCustomCriteria("c.bl_emite_dacte_faturamento = 'S'");
		
		List<Map> queryResult = getJdbcTemplate().queryForList(sql.toString(),sql.getCriteria());
		
		List<Long> idFaturas = new ArrayList<Long>();
		for (Map map:queryResult){
			idFaturas.add(MapUtils.getLong(map, "ID_FATURA"));
		}
		return idFaturas;
	}


	/**
	 * Executa para cada fatura
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/03/2007
	 * @param obj
	 * @return
	 */
	public JRDataSource executeSubReportDocumento(Object[] obj){
		SqlTemplate sql = faturaService.mountSqlSubFatura((Long) obj[0], (Long) obj[1], (Long) obj[2], (Long) obj[3]);
		List lstDocumento = faturaService.iteratorSubSelectFatura(sql);
		
		return new JRMapCollectionDataSource(faturaService.ordenaLista(lstDocumento));
	
			}
			
	/**
	 * Configura variáveis do relatório, para receberem valores não abreviados do domínio 
	 * Ex: situação = I  -  vai ser configurado, e exibido no relatório como Inativo
	 */
	@Override
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("MODAL", "DM_MODAL");
		config.configDomainField("ABRANGENCIA", "DM_ABRANGENCIA");
		super.configReportDomains(config);
	}

		
	public void validateLimiteImpressao(Integer qtDocumentos){
		BigDecimal qtLimite = (BigDecimal) configuracoesFacade.getValorParametro(NM_PARAMETRO_LIMITE_IMPRESSAO);
		
		if(qtDocumentos.intValue() > qtLimite.intValue()){
			throw new BusinessException("LMS-04543");
		}
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void enviaFaturaDell(TypedFlatMap parameters) {
		Long idFatura = extractIdFatura(parameters);

		FaturaDellDMN fatura = faturaService.getFaturasDell(idFatura);

		eventoFaturaService.storeMessage(fatura);
	}
	public boolean isCnpjIsIn(TypedFlatMap parameters) {
		String cnpj = extractCnpj(parameters);
		return !cnpj.isEmpty() && faturaService.isCnpjIsIn(cnpj);
	}
	private String isCnpjIsIn(String cnpj) {
		if(!cnpj.isEmpty() && faturaService.isCnpjIsIn(cnpj)){
			return cnpj;
		}
		return null;
	}
	private Long extractIdFatura(TypedFlatMap parameters) {
		return Long.valueOf(parameters.getString("idFatura"));
	}
	private String extractCnpj(TypedFlatMap parameters){
		return parameters.getString("cnpj").replaceAll("[^0-9]","");
	}

	public void setEventoFaturaService(EventoFaturaService eventoFaturaService) {
		this.eventoFaturaService = eventoFaturaService;
	}
}
