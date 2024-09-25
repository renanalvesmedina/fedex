package com.mercurio.lms.municipios.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.contratacaoveiculos.model.service.EixosTipoMeioTransporteService;
import com.mercurio.lms.municipios.model.TarifaPostoPassagem;
import com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem;
import com.mercurio.lms.municipios.model.service.TarifaPostoPassagemService;
import com.mercurio.lms.municipios.model.service.ValorTarifaPostoPassagemService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.municipios.emitirPostoPassagemService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/emitirPostosPassagem.jasper"
 */

public class EmitirPostoPassagemService extends ReportServiceSupport {
 
	private ConversaoMoedaService conversaoMoedaService;
	
	private TarifaPostoPassagemService tarifaPostoPassagemService;
	
	private ValorTarifaPostoPassagemService valorTarifaPostoPassagemService;
	
	private EixosTipoMeioTransporteService eixosTipoMeioTransporteService; 
	
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = (TypedFlatMap)criteria;
		SqlTemplate sql = createSqlTemplate(map);
		Map parametersReport = new HashMap(); 
		if (map.getLong("moeda.idMoeda") != null && map.getLong("pais.idPais") != null) {
			parametersReport.put("ID_PAIS",map.getLong("pais.idPais"));
			parametersReport.put("ID_MOEDA",map.getLong("moeda.idMoeda"));  
			parametersReport.put("dsSimbolo",map.getString("moeda.dsSimbolo"));
		}else{
			parametersReport.put("ID_PAIS",SessionUtils.getPaisSessao().getIdPais()); 
			parametersReport.put("ID_MOEDA",SessionUtils.getMoedaSessao().getIdMoeda()); 
			parametersReport.put("dsSimbolo",SessionUtils.getMoedaSessao().getDsSimbolo());
		}
		
		parametersReport.put("onlyVigentes",Boolean.valueOf(map.getString("apenasVigentes.value") != null && map.getString("apenasVigentes.value").equals("S")));
		
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,map.get("tpFormatoRelatorio"));
		JRReportDataObject jr = executeQuery(sql.getSql(),sql.getCriteria());
						   jr.setParameters(parametersReport);
		return jr; 
	}
	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) { 
		SqlTemplate sql = createSqlTemplate();
		//Projections
		sql.addProjection("M.NM_MUNICIPIO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("P.NM_PAIS_I","NM_PAIS"));
		sql.addProjection("UF.SG_UNIDADE_FEDERATIVA || ' - ' || UF.NM_UNIDADE_FEDERATIVA","NM_UNIDADE_FEDERATIVA");
		sql.addProjection("PP.TP_POSTO_PASSAGEM"); 
		sql.addProjection("PP.NR_KM");
		sql.addProjection("PP.TP_SENTIDO_COBRANCA");
		sql.addProjection("R.SG_RODOVIA || NVL2(R.DS_RODOVIA,' - ' || R.DS_RODOVIA,' ')","DS_RODOVIA");
		sql.addProjection("C.NM_PESSOA","NM_CONCESSIONARIA");
		sql.addProjection("PP.DT_VIGENCIA_INICIAL","DT_VIGENCIA_INICIAL"); 
		sql.addProjection("PP.DT_VIGENCIA_FINAL","DT_VIGENCIA_FINAL");
		sql.addProjection("PP.ID_POSTO_PASSAGEM");

		//From and Joins
		StringBuffer sb = new StringBuffer("POSTO_PASSAGEM PP \n")
			.append("INNER JOIN MUNICIPIO M ON M.ID_MUNICIPIO = PP.ID_MUNICIPIO \n")
			.append("INNER JOIN UNIDADE_FEDERATIVA UF ON UF.ID_UNIDADE_FEDERATIVA = M.ID_UNIDADE_FEDERATIVA \n")
			.append("INNER JOIN V_PAIS_I P ON P.ID_PAIS = UF.ID_PAIS \n")
			.append("INNER JOIN RODOVIA R ON R.ID_RODOVIA = PP.ID_RODOVIA \n")
			.append("INNER JOIN PESSOA C ON C.ID_PESSOA = PP.ID_CONCESSIONARIA \n");


		
		sql.addFrom(sb.toString());
		//wheres
		sql.addFilterSummary("tipoPostoPassagem",criteria.getString("tpPostoPassagem.descricao"));
		sql.addCriteria("PP.TP_POSTO_PASSAGEM","=",criteria.getString("tpPostoPassagem.value"));
		
		sql.addFilterSummary("localizacaoMunicipio",criteria.getString("municipio.nmMunicipio"));
		sql.addCriteria("M.ID_MUNICIPIO","=",criteria.getLong("municipio.idMunicipio"));

		sql.addFilterSummary("uf",criteria.getString("unidadeFederativa.sgUnidadeFederativa"));
		sql.addCriteria("UF.ID_UNIDADE_FEDERATIVA","=",criteria.getLong("unidadeFederativa.idUnidadeFederativa"));

		sql.addFilterSummary("pais",criteria.getString("pais.nmPais"));
		sql.addCriteria("P.ID_PAIS","=",criteria.getLong("pais.idPais"));
		
		if(criteria.get("rodovia.idRodovia")!= null){
			if(!criteria.getString("rodovia.dsRodovia").equals(""))
				sql.addFilterSummary("rodovia",criteria.getString("rodovia.sgRodovia").concat(" - ").concat(criteria.getString("rodovia.dsRodovia")));
			else
				sql.addFilterSummary("rodovia",criteria.getString("rodovia.sgRodovia"));
		}
		sql.addCriteria("R.ID_RODOVIA","=",criteria.getLong("rodovia.idRodovia"));

		sql.addFilterSummary("kmInicial",criteria.getString("nrKmInicial"));
		sql.addCriteria("PP.NR_KM",">=",criteria.getDouble("nrKmInicial"));

		sql.addFilterSummary("kmFinal",criteria.getString("nrKmFinal"));
		sql.addCriteria("PP.NR_KM","<=",criteria.getDouble("nrKmFinal"));

		sql.addFilterSummary("sentidoCobranca",criteria.getString("tpSentidoCobranca.descricao"));
		sql.addCriteria("PP.TP_SENTIDO_COBRANCA","=",criteria.getString("tpSentidoCobranca.value"));
		
		sql.addFilterSummary("concessionaria",criteria.getString("concessionaria.pessoa.nmPessoa"));
		sql.addCriteria("C.ID_PESSOA","=",criteria.getLong("concessionaria.idConcessionaria"));
		
		sql.addFilterSummary("converterParaMoeda",criteria.getString("moeda.siglaSimbolo"));
		
		Boolean onlyVigentes = Boolean.valueOf(criteria.getString("apenasVigentes.value") != null && criteria.getString("apenasVigentes.value").equals("S"));
		sql.addFilterSummary("apenasVigentes",criteria.getString("apenasVigentes.descricao"));
		
		if (onlyVigentes.booleanValue()) {
			sql.addCustomCriteria("PP.DT_VIGENCIA_INICIAL <= ? AND PP.DT_VIGENCIA_FINAL >= ?");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			
			sql.addCustomCriteria("EXISTS (SELECT  TPP.ID_TARIFA_POSTO_PASSAGEM FROM TARIFA_POSTO_PASSAGEM TPP WHERE TPP.DT_VIGENCIA_INICIAL <= ? AND TPP.DT_VIGENCIA_FINAL >= ? AND TPP.ID_POSTO_PASSAGEM = PP.ID_POSTO_PASSAGEM) ");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		}else{
			sql.addCustomCriteria("EXISTS (SELECT  TPP.ID_TARIFA_POSTO_PASSAGEM FROM TARIFA_POSTO_PASSAGEM TPP WHERE TPP.ID_POSTO_PASSAGEM = PP.ID_POSTO_PASSAGEM) ");
		}
		
		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("P.NM_PAIS_I"));
		sql.addOrderBy("UF.NM_UNIDADE_FEDERATIVA");
		sql.addOrderBy("NM_CONCESSIONARIA");
		sql.addOrderBy("M.NM_MUNICIPIO");
		sql.addOrderBy("PP.NR_KM");
		
		return sql;
	}
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_FORMA_COBRANCA","DM_FORMA_COBRANCA_POSTO_PASSAGEM");
		config.configDomainField("TP_SENTIDO_COBRANCA","DM_SENTIDO_COBRANCA_POSTO_PASSAGEM");
		config.configDomainField("TP_POSTO_PASSAGEM","DM_POSTO_PASSAGEM");
		super.configReportDomains(config);
	}
	 
	public JRDataSource executeSubReport(Long idPostoPassagem, Long idMoedaDestino, Long idPaisDestino, Boolean onlyVigentes) {
		List tarifas = tarifaPostoPassagemService.findByPostoPassagem(idPostoPassagem,onlyVigentes);
		List resultList = new ArrayList();
		try {
			for (Iterator i = tarifas.iterator(); i.hasNext();) {
				TarifaPostoPassagem tarifa = (TarifaPostoPassagem)i.next();
				
				if (tarifa.getTpFormaCobranca().getValue().equals("FI")) {
					TypedFlatMap result = new TypedFlatMap();
					
					result.put("DS_FORMA_COBRANCA",tarifa.getTpFormaCobranca().getDescription().getValue());
					ValorTarifaPostoPassagem valorTarifa = ((ValorTarifaPostoPassagem)tarifa.getValorTarifaPostoPassagems().get(0));
					
					result.put("VL_TARIFA",new StringBuffer(valorTarifa.getMoedaPais().getMoeda().getDsSimbolo()).append(" ").append(
												FormatUtils.formatDecimal("#,##0.00",conversaoMoedaService.findConversaoMoeda(valorTarifa.getMoedaPais().getPais().getIdPais(),
												valorTarifa.getMoedaPais().getMoeda().getIdMoeda(),idPaisDestino,idMoedaDestino,JTDateTimeUtils.getDataAtual(),valorTarifa.getVlTarifa())))
											.toString());
					
					result.put("DT_VIGENCIA_INICIAL",JTFormatUtils.format(tarifa.getDtVigenciaInicial()));
					if (tarifa.getDtVigenciaFinal() != null)
						result.put("DT_VIGENCIA_FINAL",JTFormatUtils.format(tarifa.getDtVigenciaFinal()));
					
					resultList.add(result);

				}else if (tarifa.getTpFormaCobranca().getValue().equals("EI")) {

					List tpMeiosTransp = eixosTipoMeioTransporteService.findSumEixosAllMeioTransportes();
					ValorTarifaPostoPassagem valorTarifa = ((ValorTarifaPostoPassagem)tarifa.getValorTarifaPostoPassagems().get(0));
					
					BigDecimal vlTarifa = conversaoMoedaService.findConversaoMoeda(valorTarifa.getMoedaPais().getPais().getIdPais(),
											valorTarifa.getMoedaPais().getMoeda().getIdMoeda(),idPaisDestino,idMoedaDestino,JTDateTimeUtils.getDataAtual(),valorTarifa.getVlTarifa());
					for (Iterator i2 = tpMeiosTransp.iterator(); i2.hasNext();) {
						TypedFlatMap result = new TypedFlatMap();
						
						Object[] projections = (Object[])i2.next();
						Integer qtEixos = (Integer)projections[0]; 
						String dsTipoMeioTransporte = (String)projections[1];
						String dsTipoMeioTransporteComposto = (String)projections[2];
					
						StringBuffer dsFormaCobranca = new StringBuffer(tarifa.getTpFormaCobranca().getDescription().getValue());
							dsFormaCobranca.append(" - ").append(dsTipoMeioTransporte);
						if (dsTipoMeioTransporteComposto != null)
							dsFormaCobranca.append(" + ").append(dsTipoMeioTransporteComposto);
						dsFormaCobranca.append(" (").append(qtEixos).append(")");
						
						result.put("DS_FORMA_COBRANCA",dsFormaCobranca.toString());
					
						result.put("VL_TARIFA",new StringBuffer(valorTarifa.getMoedaPais().getMoeda().getDsSimbolo()).append(" ").append(
													FormatUtils.formatDecimal("#,##0.00",vlTarifa.multiply(BigDecimal.valueOf(qtEixos.longValue()))))
												.toString());
					
						result.put("DT_VIGENCIA_INICIAL",JTFormatUtils.format(tarifa.getDtVigenciaInicial()));
						if (tarifa.getDtVigenciaFinal() != null)
							result.put("DT_VIGENCIA_FINAL",JTFormatUtils.format(tarifa.getDtVigenciaFinal()));
						resultList.add(result);
					}
				}else if (tarifa.getTpFormaCobranca().getValue().equals("TI")) {
					List valores = valorTarifaPostoPassagemService.findByTarifaPostoPassagem(tarifa.getIdTarifaPostoPassagem());
					for (Iterator i2 = valores.iterator(); i2.hasNext();) {
						ValorTarifaPostoPassagem valorTarifa = (ValorTarifaPostoPassagem)i2.next();
						TypedFlatMap result = new TypedFlatMap();
						
						StringBuffer dsFormaCobranca = new StringBuffer(tarifa.getTpFormaCobranca().getDescription().getValue());
						dsFormaCobranca.append(" - ").append(valorTarifa.getTipoMeioTransporte().getDsTipoMeioTransporte());
						if (valorTarifa.getTipoMeioTransporte().getTipoMeioTransporte() != null)
							dsFormaCobranca.append(" + ").append(valorTarifa.getTipoMeioTransporte().getTipoMeioTransporte().getDsTipoMeioTransporte());
						dsFormaCobranca.append(" (").append(valorTarifa.getQtEixos()).append(")");
						
						result.put("DS_FORMA_COBRANCA",dsFormaCobranca.toString());
						
						result.put("VL_TARIFA",new StringBuffer(valorTarifa.getMoedaPais().getMoeda().getDsSimbolo()).append(" ").append(
								FormatUtils.formatDecimal("#,##0.00",conversaoMoedaService.findConversaoMoeda(valorTarifa.getMoedaPais().getPais().getIdPais(),
								valorTarifa.getMoedaPais().getMoeda().getIdMoeda(),idPaisDestino,idMoedaDestino,JTDateTimeUtils.getDataAtual(),valorTarifa.getVlTarifa())))
							.toString());
						
						result.put("DT_VIGENCIA_INICIAL",JTFormatUtils.format(tarifa.getDtVigenciaInicial()));
						if (tarifa.getDtVigenciaFinal() != null)
							result.put("DT_VIGENCIA_FINAL",JTFormatUtils.format(tarifa.getDtVigenciaFinal()));
						
						resultList.add(result);
					}
				}
			}
		} catch (Exception e) {
			throw new InfrastructureException(e.getCause()); 
		}
		return new JRMapCollectionDataSource(resultList);
	}
	
	public String converteMoeda(Object[] parameters) {
		
		BigDecimal newVl = conversaoMoedaService.findConversaoMoeda((Long)parameters[0],(Long)parameters[1],
								(Long)parameters[2],(Long)parameters[3],JTDateTimeUtils.getDataAtual(),(BigDecimal)parameters[4]);
		
		return FormatUtils.formatDecimal("#,##0.00",newVl);
	}
	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public void setTarifaPostoPassagemService(
			TarifaPostoPassagemService tarifaPostoPassagemService) {
		this.tarifaPostoPassagemService = tarifaPostoPassagemService;
	}
	public void setValorTarifaPostoPassagemService(
			ValorTarifaPostoPassagemService valorTarifaPostoPassagemService) {
		this.valorTarifaPostoPassagemService = valorTarifaPostoPassagemService;
	}
	public void setEixosTipoMeioTransporteService(
			EixosTipoMeioTransporteService eixosTipoMeioTransporteService) {
		this.eixosTipoMeioTransporteService = eixosTipoMeioTransporteService;
	}

}
