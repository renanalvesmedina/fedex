package com.mercurio.lms.fretecarreteiroviagem.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.joda.time.DateTimeZone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.fretecarreteiroviagem.emitirTotalizacaoFreteCarreteiroService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteiroviagem/report/emitirTotalizacaoFreteCarreteiro.jasper"
 */
public class EmitirTotalizacaoFreteCarreteiroService extends ReportServiceSupport {
	
	private final String percentR = "PC_REGIONAL_";
	private final String percentF = "PC_FILIAL_";
	private final String percentE = "PC_EMPRESA_";
	
	private final BigDecimal hundred = new BigDecimal(100l);
	private final int scaleDefault = 5;
	
	private ConfiguracoesFacade configuracoesFacade;
	private ConversaoMoedaService conversaoMoedaService;
	private EnderecoPessoaService enderecoPessoaService;
	private MoedaPaisService moedaPaisService;
	private DoctoServicoService doctoServicoService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		final TypedFlatMap map = (TypedFlatMap)parameters;		
		SqlTemplate sql = createSqlTemplate(map);

		if ((map.getYearMonthDay("dtEmissaoInicial") == null || map.getYearMonthDay("dtEmissaoFinal") == null) &&
				(map.getYearMonthDay("dtFechamentoInicial") == null || map.getYearMonthDay("dtFechamentoFinal") == null))
			throw new BusinessException("LMS-00062",new Object[] {new StringBuffer(configuracoesFacade.getMensagem("periodoEmissao"))
					.append(", ").append(configuracoesFacade.getMensagem("periodoFechamento")).toString()});
		
		Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        parametersReport.put("SG_MOEDA",map.getString("moedaPais.moeda.dsSimbolo"));

        
        //Executa a SQL padrao agrupando os valores por REGIONAL e FILIAL
        sql = createSqlTemplate(map);

        sql.addProjection("R.SG_REGIONAL");
        sql.addProjection("M.ID_MANIFESTO","ID_MANIFESTO");
        sql.addProjection("R.ID_REGIONAL");
        sql.addProjection("R.DS_REGIONAL");
        sql.addProjection("F.SG_FILIAL");
        sql.addProjection("F.ID_FILIAL");
        sql.addProjection("CC.ID_MOEDA");
        sql.addProjection("CC.ID_CONTROLE_CARGA");
		sql.addProjection("NVL(M.VL_RATEIO_FRETE_CARRETEIRO,0)","VL_RATEIO_FRETE_CARRETEIRO"); 
		sql.addProjection("NVL(((M.VL_RATEIO_FRETE_CARRETEIRO * CC.PC_FRETE_MERCURIO) / 100),0)","VL_MERCURIO");
		sql.addProjection("NVL(((M.VL_RATEIO_FRETE_CARRETEIRO * CC.PC_FRETE_AGREGADO) / 100),0)","VL_AGREGADO");
		sql.addProjection("NVL(((M.VL_RATEIO_FRETE_CARRETEIRO * CC.PC_FRETE_EVENTUAL) / 100),0)","VL_EVENTUAL");
		
		List result = (List)getJdbcTemplate().query(sql.getSql(),JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()),new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				java.util.List resultList = new ArrayList();
				if (rs.next()) {
					TypedFlatMap temp = createEmptyMap(rs);
					
					long idFilial = rs.getLong("ID_FILIAL");
					do{
						if (idFilial != rs.getLong("ID_FILIAL")) {
							resultList.add(temp);
							temp = createEmptyMap(rs);
						}
						
						Long idMoedaOrigem = Long.valueOf(rs.getLong("ID_MOEDA"));
				        Long idFilialOrigem = Long.valueOf(rs.getLong("ID_FILIAL"));
				        EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idFilialOrigem);
				        Long idPaisOrigem = enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
				        MoedaPais moedaPais = moedaPaisService.findById(map.getLong("moedaPais.idMoedaPais"));
				        
						temp.put("VL_RATEIO_FRETE_CARRETEIRO",conversaoMoedaService.findConversaoMoeda(idPaisOrigem,idMoedaOrigem,moedaPais.getPais().getIdPais(),
				        		moedaPais.getMoeda().getIdMoeda(),JTDateTimeUtils.getDataAtual(),rs.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO"))
				        		.add(temp.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO")).setScale(scaleDefault,BigDecimal.ROUND_UP)); 
						temp.put("VL_MERCURIO",conversaoMoedaService.findConversaoMoeda(idPaisOrigem,idMoedaOrigem,moedaPais.getPais().getIdPais(),
				        		moedaPais.getMoeda().getIdMoeda(),JTDateTimeUtils.getDataAtual(),rs.getBigDecimal("VL_MERCURIO"))
				        		.add(temp.getBigDecimal("VL_MERCURIO")).setScale(scaleDefault,BigDecimal.ROUND_UP));
						temp.put("VL_AGREGADO",conversaoMoedaService.findConversaoMoeda(idPaisOrigem,idMoedaOrigem,moedaPais.getPais().getIdPais(),
				        		moedaPais.getMoeda().getIdMoeda(),JTDateTimeUtils.getDataAtual(),rs.getBigDecimal("VL_AGREGADO"))
				        		.add(temp.getBigDecimal("VL_AGREGADO")).setScale(scaleDefault,BigDecimal.ROUND_UP));
						temp.put("VL_EVENTUAL",conversaoMoedaService.findConversaoMoeda(idPaisOrigem,idMoedaOrigem,moedaPais.getPais().getIdPais(),
				        		moedaPais.getMoeda().getIdMoeda(),JTDateTimeUtils.getDataAtual(),rs.getBigDecimal("VL_EVENTUAL"))
				        		.add(temp.getBigDecimal("VL_EVENTUAL")).setScale(scaleDefault,BigDecimal.ROUND_UP));
						
						/** Otimização */
			    		final ProjectionList projection = Projections.projectionList()
				    		.add(Projections.property("ds.id"), "idDoctoServico")
				    		.add(Projections.property("ds.qtVolumes"), "qtVolumes")
				    		.add(Projections.property("ds.psAferido"), "psAferido");
						
			    		//Documentos de servico associados ao manifestos
			    		final List<DoctoServico> doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(rs.getLong("ID_MANIFESTO"), projection);
						Integer volume = Integer.valueOf(0);
						BigDecimal peso = BigDecimal.ZERO;
						
						for (DoctoServico docto : doctoServicoList) {
							volume = volume + docto.getQtVolumes();
							peso = peso.add((docto.getPsAferido()!=null)?docto.getPsAferido():BigDecimal.ZERO);
						}
						
						temp.put("PC_VOLUME", volume);
						temp.put("PC_PESO", peso);
						temp.put("VL_CTRC", Integer.valueOf(doctoServicoList.size()));
						
						idFilial = rs.getLong("ID_FILIAL");
					}while(rs.next());
					resultList.add(temp);
				}
				return resultList;
			}});
		
		
		sql = createSqlTemplate(map);

        sql.addProjection("R.SG_REGIONAL");
        sql.addProjection("R.ID_REGIONAL");
        sql.addProjection("R.DS_REGIONAL");
        sql.addProjection("F.SG_FILIAL");
        sql.addProjection("F.ID_FILIAL");
		sql.addProjection("CC.ID_MOEDA");
		sql.addProjection("NVL(M.VL_RATEIO_FRETE_CARRETEIRO,0)","VL_RATEIO_FRETE_CARRETEIRO"); 
		sql.addProjection("NVL(CC.PC_FRETE_MERCURIO,0)","PC_MERCURIO");
		sql.addProjection("NVL(CC.PC_FRETE_AGREGADO,0)","PC_AGREGADO");
		sql.addProjection("NVL(CC.PC_FRETE_EVENTUAL,0)","PC_EVENTUAL");
		 	
		TypedFlatMap groupByRegional = (TypedFlatMap)getJdbcTemplate().query(sql.getSql(),JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()),new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				TypedFlatMap groupByRegional = new TypedFlatMap();
				BigDecimal zero = new BigDecimal(0);
				groupByRegional.put("VL_TOTAL",zero);
				groupByRegional.put("VL_MERCURIO",zero);
				groupByRegional.put("VL_AGREGADO",zero);
				groupByRegional.put("VL_EVENTUAL",zero);
				if (rs.next()) {
					TypedFlatMap temp = createEmptyMap(rs);
					long idRegional = rs.getLong("ID_REGIONAL");
					temp.put("VL_MERCURIO",zero);
					temp.put("VL_AGREGADO",zero);
					temp.put("VL_EVENTUAL",zero);
					groupByRegional.put("PC_MERCURIO",zero);
					groupByRegional.put("PC_AGREGADO",zero);
					groupByRegional.put("PC_EVENTUAL",zero);
					do{
						if (idRegional != rs.getLong("ID_REGIONAL")) {
							groupByRegional(groupByRegional,temp);
							temp = createEmptyMap(rs);
							temp.put("VL_MERCURIO",zero);
							temp.put("VL_AGREGADO",zero);
							temp.put("VL_EVENTUAL",zero);
						}
						
						Long idMoedaOrigem = Long.valueOf(rs.getLong("ID_MOEDA"));
				        Long idFilialOrigem = Long.valueOf(rs.getLong("ID_FILIAL"));
				        EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idFilialOrigem);
				        Long idPaisOrigem = enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
				        MoedaPais moedaPais = moedaPaisService.findById(map.getLong("moedaPais.idMoedaPais"));
				          
				        BigDecimal vlRateioFreteCarreteiro = conversaoMoedaService.findConversaoMoeda(idPaisOrigem,idMoedaOrigem,moedaPais.getPais().getIdPais(),
				        		moedaPais.getMoeda().getIdMoeda(),JTDateTimeUtils.getDataAtual(),rs.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO")); 
						temp.put("VL_RATEIO_FRETE_CARRETEIRO",vlRateioFreteCarreteiro.add(temp.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO")).setScale(scaleDefault,BigDecimal.ROUND_UP)); 
						
						if (rs.getBigDecimal("PC_MERCURIO").longValue() != 0)
							temp.put("VL_MERCURIO",temp.getBigDecimal("VL_MERCURIO").add(vlRateioFreteCarreteiro.multiply(rs.getBigDecimal("PC_MERCURIO").divide(hundred,scaleDefault,BigDecimal.ROUND_UP))));
						if (rs.getBigDecimal("PC_AGREGADO").longValue() != 0)
							temp.put("VL_AGREGADO",temp.getBigDecimal("VL_AGREGADO").add(vlRateioFreteCarreteiro.multiply(rs.getBigDecimal("PC_AGREGADO").divide(hundred,scaleDefault,BigDecimal.ROUND_UP))));
						if (rs.getBigDecimal("PC_EVENTUAL").longValue() != 0)
							temp.put("VL_EVENTUAL",temp.getBigDecimal("VL_EVENTUAL").add(vlRateioFreteCarreteiro.multiply(rs.getBigDecimal("PC_EVENTUAL").divide(hundred,scaleDefault,BigDecimal.ROUND_UP))));
						
						idRegional = rs.getLong("ID_REGIONAL");
					}while(rs.next());
					groupByRegional(groupByRegional,temp);
				}
				return groupByRegional;
			}});

		
        sql.addFilterSummary("regional",map.getString("regional.siglaDescricao"));
		if (map.getLong("filial.idFilial") != null)
			sql.addFilterSummary("filial",new StringBuffer(map.getString("filial.sgFilial")).append(" - ").append(map.getString("filial.pessoa.nmFantasia")).toString());

		sql.addFilterSummary("converterParaMoeda",map.getString("moedaPais.moeda.siglaSimbolo"));

		sql.addFilterSummary("periodoEmissaoInicial",map.getYearMonthDay("dtEmissaoInicial"));
		sql.addFilterSummary("periodoEmissaoFinal",map.getYearMonthDay("dtEmissaoFinal"));
		sql.addFilterSummary("periodoFechamentoInicial",map.getYearMonthDay("dtFechamentoInicial"));
		sql.addFilterSummary("periodoFechamentoFinal",map.getYearMonthDay("dtFechamentoFinal"));
		
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());

		return createReportDataObject(new JRMapCollectionDataSource(executeCalcule(result,groupByRegional)),parametersReport);
	}
	
	private void groupByRegional(TypedFlatMap groupByRegional,TypedFlatMap tempRegional) {
		groupByRegional.put(tempRegional.getString("SG_REGIONAL"),tempRegional.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO").setScale(scaleDefault,BigDecimal.ROUND_UP));
		groupByRegional.put("VL_TOTAL",groupByRegional.getBigDecimal("VL_TOTAL").add(tempRegional.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO").setScale(scaleDefault,BigDecimal.ROUND_UP)));
		if (isNullOrZero(tempRegional.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO"))) {
			groupByRegional.put(new StringBuffer(tempRegional.getString("SG_REGIONAL")).append("_PC_MERCURIO").toString(),BigDecimal.ZERO);
			groupByRegional.put(new StringBuffer(tempRegional.getString("SG_REGIONAL")).append("_PC_AGREGADO").toString(),BigDecimal.ZERO);
			groupByRegional.put(new StringBuffer(tempRegional.getString("SG_REGIONAL")).append("_PC_EVENTUAL").toString(),BigDecimal.ZERO);
		}else{
			groupByRegional.put(new StringBuffer(tempRegional.getString("SG_REGIONAL")).append("_PC_MERCURIO").toString(),tempRegional.getBigDecimal("VL_MERCURIO").multiply(hundred).divide(tempRegional.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO"),scaleDefault,BigDecimal.ROUND_UP));
			groupByRegional.put(new StringBuffer(tempRegional.getString("SG_REGIONAL")).append("_PC_AGREGADO").toString(),tempRegional.getBigDecimal("VL_AGREGADO").multiply(hundred).divide(tempRegional.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO"),scaleDefault,BigDecimal.ROUND_UP));
			groupByRegional.put(new StringBuffer(tempRegional.getString("SG_REGIONAL")).append("_PC_EVENTUAL").toString(),tempRegional.getBigDecimal("VL_EVENTUAL").multiply(hundred).divide(tempRegional.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO"),scaleDefault,BigDecimal.ROUND_UP));
		}
		groupByRegional.put("VL_MERCURIO",tempRegional.getBigDecimal("VL_MERCURIO").add(groupByRegional.getBigDecimal("VL_MERCURIO")));
		groupByRegional.put("VL_AGREGADO",tempRegional.getBigDecimal("VL_AGREGADO").add(groupByRegional.getBigDecimal("VL_AGREGADO")));
		groupByRegional.put("VL_EVENTUAL",tempRegional.getBigDecimal("VL_EVENTUAL").add(groupByRegional.getBigDecimal("VL_EVENTUAL")));
		
	}
	
	private TypedFlatMap createEmptyMap(ResultSet rs) throws SQLException {
		TypedFlatMap sum = new TypedFlatMap();
		sum.put("VL_RATEIO_FRETE_CARRETEIRO",new BigDecimal(0));
		sum.put("VL_MERCURIO",new BigDecimal(0));
		sum.put("VL_AGREGADO",new BigDecimal(0));
		sum.put("VL_EVENTUAL",new BigDecimal(0));
		
		sum.put("SG_FILIAL",rs.getString("SG_FILIAL"));
		sum.put("SG_REGIONAL",rs.getString("SG_REGIONAL"));
		sum.put("DS_REGIONAL",rs.getString("DS_REGIONAL"));
		
		return sum;
	}
	
	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate sql = createSqlTemplate();  
		sql.addFrom(new StringBuffer("CONTROLE_CARGA CC ")
		     .append("INNER JOIN MANIFESTO M ON M.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
		     .append("INNER JOIN FILIAL F ON F.ID_FILIAL = M.ID_FILIAL_ORIGEM ")
		     .append("INNER JOIN REGIONAL_FILIAL RF ON RF.ID_FILIAL = F.ID_FILIAL ")
		     .append("INNER JOIN REGIONAL R ON R.ID_REGIONAL = RF.ID_REGIONAL ")
		     .append("INNER JOIN EVENTO_CONTROLE_CARGA ECC ON ECC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ").toString());
		
		
			sql.addCustomCriteria("RF.DT_VIGENCIA_INICIAL <= ? AND RF.DT_VIGENCIA_FINAL >= ?");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCustomCriteria("ECC.ID_EVENTO_CONTROLE_CARGA = (SELECT MAX(ECC2.ID_EVENTO_CONTROLE_CARGA) FROM EVENTO_CONTROLE_CARGA ECC2 WHERE ECC2.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA)");
			sql.addCriteria("CC.TP_STATUS_CONTROLE_CARGA","=","FE");
			sql.addCriteria("CC.TP_CONTROLE_CARGA", "=", "V");
			
			sql.addCriteria("F.ID_FILIAL","=",criteria.getLong("filial.idFilial"));
			
			sql.addCriteria("R.ID_REGIONAL","=",criteria.getLong("regional.idRegional"));

			DateTimeZone dateTimeZone = SessionUtils.getFilialSessao().getDateTimeZone();
			if (criteria.getYearMonthDay("dtEmissaoInicial") != null)
				sql.addCriteria("M.DH_EMISSAO_MANIFESTO",">=",criteria.getYearMonthDay("dtEmissaoInicial").toDateTimeAtMidnight(dateTimeZone));
			if (criteria.getYearMonthDay("dtEmissaoFinal") != null)
				sql.addCriteria("M.DH_EMISSAO_MANIFESTO","<",criteria.getYearMonthDay("dtEmissaoFinal").plusDays(1).toDateTimeAtMidnight(dateTimeZone));
			if (criteria.getYearMonthDay("dtFechamentoInicial") != null)
				sql.addCriteria("ECC.DH_EVENTO",">=",criteria.getYearMonthDay("dtFechamentoInicial").toDateTimeAtMidnight(dateTimeZone));
			if (criteria.getYearMonthDay("dtFechamentoFinal") != null)
				sql.addCriteria("ECC.DH_EVENTO","<",criteria.getYearMonthDay("dtFechamentoFinal").plusDays(1).toDateTimeAtMidnight(dateTimeZone));
			
			if (criteria.getYearMonthDay("dtFechamentoInicial") != null || criteria.getYearMonthDay("dtFechamentoFinal") != null) {
				sql.addCriteria("ECC.TP_EVENTO_CONTROLE_CARGA","=","FD");
			} 

			
			
		sql.addOrderBy("R.SG_REGIONAL");
		sql.addOrderBy("F.SG_FILIAL");
		return sql;
	}

	
	private List executeCalcule(List resultList,TypedFlatMap groupByRegional) {
		final String mercurio = "MERCURIO";
		final String agregado = "AGREGADO";
		final String eventual = "EVENTUAL";
		final String total = "TOTAL";

		BigDecimal valueTotalEmpresa = groupByRegional.getBigDecimal("VL_TOTAL");
		//Aqui vai ficar os calculos
		Iterator i = resultList.iterator();
		if (i.hasNext()) {
			TypedFlatMap result = (TypedFlatMap)i.next();
			String sgRegional = result.getString("SG_REGIONAL");
			do {
				result.put("GROUP_GERAL",Boolean.TRUE);
				BigDecimal valueMercurio = result.getBigDecimal("VL_MERCURIO");
				BigDecimal valueAgregado = result.getBigDecimal("VL_AGREGADO");
				BigDecimal valueEventual = result.getBigDecimal("VL_EVENTUAL");
				
				BigDecimal valueTotalFilial = result.getBigDecimal("VL_RATEIO_FRETE_CARRETEIRO"); 
				BigDecimal valueTotalRegional = groupByRegional.getBigDecimal(result.getString("SG_REGIONAL"));
				
				calculePercent(result,valueMercurio,valueTotalFilial,valueTotalRegional,valueTotalEmpresa,mercurio);
				calculePercent(result,valueAgregado,valueTotalFilial,valueTotalRegional,valueTotalEmpresa,agregado);
				calculePercent(result,valueEventual,valueTotalFilial,valueTotalRegional,valueTotalEmpresa,eventual);
				calculePercent(result,valueTotalFilial,valueTotalRegional,valueTotalEmpresa,total);
				if (i.hasNext()) {
					TypedFlatMap resultNew = (TypedFlatMap)i.next();
					if (!sgRegional.equals(resultNew.getString("SG_REGIONAL")))
						organizeTotais(result,groupByRegional);
					result = resultNew;
				}else{
					organizeTotais(result,groupByRegional);
					break;
				}
			}while(true);
			result.put("VL_TOTAL",valueTotalEmpresa);
			result.put("VL_MERCURIO_GERAL",groupByRegional.getBigDecimal("VL_MERCURIO"));
			result.put("VL_AGREGADO_GERAL",groupByRegional.getBigDecimal("VL_AGREGADO"));
			result.put("VL_EVENTUAL_GERAL",groupByRegional.getBigDecimal("VL_EVENTUAL"));
			if (isNullOrZero(valueTotalEmpresa)) {
				result.put("PC_EMPRESA_MERCURIO_GERAL",BigDecimal.ZERO);
				result.put("PC_EMPRESA_AGREGADO_GERAL",BigDecimal.ZERO);
				result.put("PC_EMPRESA_EVENTUAL_GERAL",BigDecimal.ZERO);
			}else{
				result.put("PC_EMPRESA_MERCURIO_GERAL",groupByRegional.getBigDecimal("VL_MERCURIO").multiply(hundred).divide(valueTotalEmpresa,scaleDefault,BigDecimal.ROUND_UP));
				result.put("PC_EMPRESA_AGREGADO_GERAL",groupByRegional.getBigDecimal("VL_AGREGADO").multiply(hundred).divide(valueTotalEmpresa,scaleDefault,BigDecimal.ROUND_UP));
				result.put("PC_EMPRESA_EVENTUAL_GERAL",groupByRegional.getBigDecimal("VL_EVENTUAL").multiply(hundred).divide(valueTotalEmpresa,scaleDefault,BigDecimal.ROUND_UP));
			}
		}
		return resultList;
	}
	
	private void organizeTotais(TypedFlatMap result,TypedFlatMap groupByRegional) {
		final String mercurio = "MERCURIO_TOT";
		final String agregado = "AGREGADO_TOT";
		final String eventual = "EVENTUAL_TOT";
		final String total = "TOTAL_TOT";

		String sgRegional = result.getString("SG_REGIONAL");
		BigDecimal valueTotalEmpresa = groupByRegional.getBigDecimal("VL_TOTAL");
		BigDecimal valueTotalRegional = groupByRegional.getBigDecimal(sgRegional);
		
		BigDecimal vlMercurio = groupByRegional.getBigDecimal(new StringBuffer(sgRegional).append("_PC_MERCURIO").toString());
		BigDecimal vlAgregado = groupByRegional.getBigDecimal(new StringBuffer(sgRegional).append("_PC_AGREGADO").toString());
		BigDecimal vlEventual = groupByRegional.getBigDecimal(new StringBuffer(sgRegional).append("_PC_EVENTUAL").toString());
		
		calculeTotais(result,vlMercurio,valueTotalRegional,valueTotalEmpresa,mercurio);
		calculeTotais(result,vlAgregado,valueTotalRegional,valueTotalEmpresa,agregado);
		calculeTotais(result,vlEventual,valueTotalRegional,valueTotalEmpresa,eventual);
		calculeTotais(result,hundred,valueTotalRegional,valueTotalEmpresa,total);
		
		result.put("VL_RATEIO_FRETE_CARRETEIRO_TOT",valueTotalRegional);
	}
	
	private void calculeTotais(TypedFlatMap result, BigDecimal pcValue, BigDecimal valueTotalRegional, BigDecimal valuesTotalEmpresa, String tpVinculo) {
		//Valor por tpVinculo
		BigDecimal value = pcValue.multiply(valueTotalRegional).divide(hundred,scaleDefault,BigDecimal.ROUND_UP);
		result.put(new StringBuffer("VL_").append(tpVinculo).toString(),value);
		
		//Percentual Regional
		if (isNullOrZero(valueTotalRegional))
			result.put(new StringBuffer(percentR).append(tpVinculo).toString(),BigDecimal.ZERO);
		else
			result.put(new StringBuffer(percentR).append(tpVinculo).toString(),value.multiply(hundred).divide(valueTotalRegional,scaleDefault,BigDecimal.ROUND_UP));
		
		//Percentual Empresa
		BigDecimal pcE;
		if (isNullOrZero(valuesTotalEmpresa))
			pcE = BigDecimal.ZERO;
		else
			pcE = value.multiply(hundred).divide(valuesTotalEmpresa,scaleDefault,BigDecimal.ROUND_UP);
		result.put(new StringBuffer(percentE).append(tpVinculo).toString(),pcE);
	}
	
	/**
	 * Essa função calcula os percentuais do relatorio, para agregados,Mercurio,Eventual deve se passar todos os parametros, porem quando
	 * for o calculo de totais deve se passar o parametro <b>value</b> como null, para que ele saiba interpretar
	 * @param result
	 * @param value
	 * @param valueTotalFilial
	 * @param valueTotalRegional
	 * @param valuesTotalEmpresa
	 * @param tpVinculo
	 */
	private void calculePercent(TypedFlatMap result, BigDecimal value, BigDecimal valueTotalFilial, BigDecimal valueTotalRegional, BigDecimal valuesTotalEmpresa, String tpVinculo) {
		//Percentual Filial
		BigDecimal valueMultiplyHundred;
		if (value != null) {
			valueMultiplyHundred = value.multiply(hundred);
			BigDecimal pcF;
			if (isNullOrZero(valueTotalFilial))
				pcF = BigDecimal.ZERO;
			else
				pcF = valueMultiplyHundred.divide(valueTotalFilial,scaleDefault,BigDecimal.ROUND_UP);
			
			result.put(new StringBuffer(percentF).append(tpVinculo).toString(),pcF);
		}else
			valueMultiplyHundred = valueTotalFilial.multiply(hundred);
		
		//Percentual Regional
		BigDecimal pcR;
		if (isNullOrZero(valueTotalRegional))
			pcR = BigDecimal.ZERO;
		else
			pcR = valueMultiplyHundred.divide(valueTotalRegional,scaleDefault,BigDecimal.ROUND_UP);
		
		result.put(new StringBuffer(percentR).append(tpVinculo).toString(),pcR);
		//Percentual Empresa
		BigDecimal pcE;
		if (isNullOrZero(valuesTotalEmpresa))
			pcE = BigDecimal.ZERO;
		else
			pcE = valueMultiplyHundred.divide(valuesTotalEmpresa,scaleDefault,BigDecimal.ROUND_UP);
		
		result.put(new StringBuffer(percentE).append(tpVinculo).toString(),pcE);
	}
	
	private void calculePercent(TypedFlatMap result, BigDecimal valueTotalFilial, BigDecimal valueTotalRegional, BigDecimal valuesTotalEmpresa, String tpVinculo) {
		calculePercent(result,null,valueTotalFilial,valueTotalRegional,valuesTotalEmpresa,tpVinculo);
	}
	
	private boolean isNullOrZero(BigDecimal value) {
		return (value == null || value.setScale(0, RoundingMode.DOWN).equals(BigDecimal.ZERO));
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}
}
