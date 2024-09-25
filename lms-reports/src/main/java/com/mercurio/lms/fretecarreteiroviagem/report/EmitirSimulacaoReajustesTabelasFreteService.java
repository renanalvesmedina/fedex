package com.mercurio.lms.fretecarreteiroviagem.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.fretecarreteiroviagem.model.AplicaReajusteRota;
import com.mercurio.lms.fretecarreteiroviagem.model.SimulacaoReajusteRota;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.SimulacaoReajusteRotaDAO;
import com.mercurio.lms.fretecarreteiroviagem.model.service.SimulacaoReajusteRotaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.fretecarreteiroviagem.emitirSimulacaoReajustesTabelasFreteService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteiroviagem/report/emitirSimulacaoReajustesTabelasFrete.jasper"
 */
public class EmitirSimulacaoReajustesTabelasFreteService extends 
		ReportServiceSupport {
	
	private SimulacaoReajusteRotaService simulacaoReajusteRotaService;
	private SimulacaoReajusteRotaDAO simulacaoReajusteRotaDAO;
	private MoedaPaisService moedaPaisService;

	public EmitirSimulacaoReajustesTabelasFreteService() {
		super();
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		SimulacaoReajusteRota simulacao = copyParametersToBean((TypedFlatMap)parameters);
		SqlTemplate hql = simulacaoReajusteRotaService.createHQLRotasByCriteria(simulacao,SimulacaoReajusteRotaService.VIGENTE_EM,Boolean.FALSE);
    	
		hql.addFrom(new StringBuffer(RotaIdaVolta.class.getName()).append(" RIV ")
	    		.append("INNER JOIN RIV.rota R ")
	    		.append("INNER JOIN RIV.rotaViagem RV ")
	    		.append("INNER JOIN RIV.moedaPais MP ")
	    		.append("INNER JOIN MP.moeda M ")
	    		.append("LEFT JOIN RV.tipoMeioTransporte TMT ")
	    		.append("LEFT JOIN TMT.tpCombustTpMeioTransps TCTMT ")// LIST
	    		.append("LEFT JOIN TCTMT.tipoCombustivel TICO ")
	    		.append("LEFT JOIN TICO.valorCombustiveis VACO ")//LIST
	    		.append("LEFT JOIN VACO.moedaPais MPVC ")//LIST
	    		.append("LEFT JOIN RV.meioTransporteRotaViagems MTRV ")//LIST -> PEGAR O VIGENTE
	    		.append("LEFT JOIN MTRV.meioTransporteRodoviario MTR ")
	    		.append("LEFT JOIN MTR.meioTransporte MT ")
	    		.append("LEFT JOIN MT.meioTranspProprietarios MTP ")//LIST
	    		.append("LEFT JOIN MTP.proprietario PR ")
	    		.append("LEFT JOIN PR.pessoa PE ")
	    		.toString());

		hql.addCustomCriteria(new StringBuffer("((MTRV.dtVigenciaInicial <= ? AND MTRV.dtVigenciaFinal > ?) OR MTRV.id IS NULL)").toString());
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual()); 
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		hql.addCustomCriteria(new StringBuffer("((MTP.dtVigenciaInicial <= ? AND MTP.dtVigenciaFinal > ?) OR MTP.id IS NULL)").toString());
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual()); 
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

		hql.addCustomCriteria("MPVC.idMoedaPais = MP.idMoedaPais");
		hql.addCustomCriteria("VACO.dtVigenciaInicial <= ?  AND VACO.dtVigenciaFinal >= ?");
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		 
		hql.addProjection("new Map(RIV.nrRota","NR_ROTA");
		hql.addProjection("R.dsRota","DS_ROTA");
		hql.addProjection("RV.tpRota","TP_ROTA");
		hql.addProjection("TMT.dsTipoMeioTransporte", "DS_TIPO_MEIO_TRANSPORTE");
		hql.addProjection("PE.tpIdentificacao","TP_IDENTIFICACAO");
		hql.addProjection("PE.nrIdentificacao","NR_IDENTIFICACAO");
		hql.addProjection("(M.sgMoeda || ' ' || M.dsSimbolo)","MOEDA");
		hql.addProjection("PE.nmPessoa","NM_PESSOA");
		hql.addProjection("RIV.nrDistancia","NR_DISTANCIA");
		hql.addProjection("RIV.vlFreteKm","VL_FRETE_KM");
		hql.addProjection("RV.dtVigenciaInicial","DT_VIGENCIA_INICIAL");
		hql.addProjection("RV.dtVigenciaFinal","DT_VIGENCIA_FINAL");		
		hql.addProjection(PropertyVarcharI18nProjection.createProjection("TICO.dsTipoCombustivel"),"DS_TIPO_COMBUSTIVEL");
		hql.addProjection("VACO.vlValorCombustivel","VL_VALOR_COMBUSTIVEL");
		hql.addProjection("TCTMT.qtConsumo","QT_CONSUMO");
		hql.addProjection("count(*)","QTD_MEIO_TRANSPORTE");
		hql.addProjection("PR.id","ID_PROPRIETARIO)");
		
		hql.addGroupBy("NR_ROTA");
		hql.addGroupBy("DS_ROTA");
		hql.addGroupBy("TP_ROTA");
		hql.addGroupBy("TMT.dsTipoMeioTransporte");
		hql.addGroupBy("TP_IDENTIFICACAO");		
		hql.addGroupBy("NR_IDENTIFICACAO");		
		hql.addGroupBy("(M.sgMoeda || ' ' || M.dsSimbolo)");		
		hql.addGroupBy("NM_PESSOA");		
		hql.addGroupBy("NR_DISTANCIA");
		hql.addGroupBy("VL_FRETE_KM");
		hql.addGroupBy("RV.dtVigenciaInicial");
		hql.addGroupBy("RV.dtVigenciaFinal");		
		hql.addGroupBy(""+PropertyVarcharI18nProjection.createProjection("TICO.dsTipoCombustivel")+"");
		hql.addGroupBy("VL_VALOR_COMBUSTIVEL");
		hql.addGroupBy("QT_CONSUMO");
		hql.addGroupBy("PR.id");
		
		hql.addOrderBy("TMT.dsTipoMeioTransporte");
		hql.addOrderBy("count(*)");
		hql.addOrderBy("NR_DISTANCIA");
		
		Map parametros = new HashMap();
		parametros.put("dsReajuste",simulacao.getDsSimulacaoReajusteRota());
		StringBuffer reajusteDescription = new StringBuffer();
		if (simulacao.getTpReajuste().getValue().equals("V"))
			reajusteDescription.append(moedaPaisService.findById(simulacao.getMoedaPais().getIdMoedaPais()).getMoeda().getSiglaSimbolo()).append(" ")
			.append(FormatUtils.formatDecimal("###,##0.000",simulacao.getVlReajuste()));
		else
			reajusteDescription.append(FormatUtils.formatDecimal("###,##0.00",simulacao.getVlReajuste())).append("%");
		
		parametros.put("vlReajuste",reajusteDescription.toString());
		parametros.put("dtVigenciaInicial",JTFormatUtils.format(simulacao.getDtVigenciaInicial()));
		if (simulacao.getDtVigenciaFinal() != null)
			parametros.put("dtVigenciaFinal",JTFormatUtils.format(simulacao.getDtVigenciaFinal()));
	
		List list = getSimulacaoReajusteRotaDAO().getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		for(Iterator i = list.iterator(); i.hasNext();) {
			Map row = (Map)i.next();
			if (row.get("TP_ROTA") != null)
				row.put("TP_ROTA",((DomainValue)row.get("TP_ROTA")).getValue());
			if (row.get("DT_VIGENCIA_INICIAL") != null)
				row.put("DT_VIGENCIA_INICIAL",JTFormatUtils.format((YearMonthDay)row.get("DT_VIGENCIA_INICIAL")));
			if (row.get("DT_VIGENCIA_FINAL") != null)
				row.put("DT_VIGENCIA_FINAL",JTFormatUtils.format((YearMonthDay)row.get("DT_VIGENCIA_FINAL")));
			if (row.get("DS_TIPO_MEIO_TRANSPORTE") != null)
				row.put("DS_TIPO_MEIO_TRANSPORTE",((String)row.get("DS_TIPO_MEIO_TRANSPORTE")));
			if (row.get("TP_IDENTIFICACAO") != null && row.get("NR_IDENTIFICACAO") != null)
				row.put("NR_IDENTIFICACAO",FormatUtils.formatIdentificacao(((DomainValue)row.get("TP_IDENTIFICACAO")),((String)row.get("NR_IDENTIFICACAO"))));
			else{
				row.remove("TP_IDENTIFICACAO");
				row.put("NR_IDENTIFICACAO",null);
			} 
			int scale = 5; 
			int round = BigDecimal.ROUND_UP; 
			if (row.get("VL_FRETE_KM") != null) {				
				String moeda = (String)row.get("MOEDA");
				BigDecimal vlFrete = ((BigDecimal)row.get("VL_FRETE_KM")).setScale(scale,round);
				BigDecimal nrDistancia = BigDecimal.valueOf(((Integer)row.get("NR_DISTANCIA")).longValue()).setScale(scale,round);
				BigDecimal vlFreteReajustado;
				BigDecimal vlValorCombustivel = (BigDecimal)row.get("VL_VALOR_COMBUSTIVEL");
				BigDecimal qtConsumo = (BigDecimal)row.get("QT_CONSUMO");
				
				row.put("VL_VALOR_COMBUSTIVEL",concatMoedaWithValor(moeda,vlValorCombustivel));
				
				vlValorCombustivel = vlValorCombustivel.multiply(nrDistancia).divide(qtConsumo,scale,BigDecimal.ROUND_UP);
				
				if (simulacao.getTpReajuste().getValue().equals("V"))
					vlFreteReajustado = simulacao.getVlReajuste().add(vlFrete).setScale(scale,round);
				else
					vlFreteReajustado = simulacao.getVlReajuste().divide(BigDecimal.valueOf(100),scale,BigDecimal.ROUND_UP).multiply(vlFrete).add(vlFrete).setScale(scale,round);
				
				  
				if (vlFrete.equals(new BigDecimal(0).setScale(scale, round)) || nrDistancia.equals(new BigDecimal(0).setScale(scale, round))){
					row.put("COMBUSTIVEL_FRETE_REAJUSTADO",new BigDecimal(0).setScale(scale, round));
					row.put("COMBUSTIVEL_FRETE_ATUAL",new BigDecimal(0).setScale(scale, round));
				} else {
					row.put("COMBUSTIVEL_FRETE_REAJUSTADO",vlValorCombustivel.divide(vlFreteReajustado.multiply(nrDistancia),5,BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)));
					row.put("COMBUSTIVEL_FRETE_ATUAL",vlValorCombustivel.divide(vlFrete.multiply(nrDistancia),5,BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)));
				}
				
				row.put("VL_FRETE_KM",concatMoedaWithValor(moeda,vlFrete));
				row.put("VL_FRETE_KM_R",concatMoedaWithValor(moeda,vlFreteReajustado));
				
				if (nrDistancia != null) { 
					row.put("VL_TOTAL_A",concatMoedaWithFreteValor(moeda,vlFrete.multiply(nrDistancia)));
					row.put("VL_TOTAL_R",concatMoedaWithFreteValor(moeda,vlFreteReajustado.multiply(nrDistancia)));
				}
				
				
			}else row.put("VL_FRETE_KM",null);
			 
		}
		
		parametros.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        
		JRReportDataObject jr = createReportDataObject(new JRMapCollectionDataSource(list),parametros);
		
		return jr;
	}
	
	private String concatMoedaWithValor(String moeda, BigDecimal value) {
		return new StringBuffer(moeda).append(" ").append(FormatUtils.formatDecimal("###,##0.000",value)).toString();
	}
	
	private String concatMoedaWithFreteValor(String moeda, BigDecimal value) { 
		return new StringBuffer(moeda).append(" ").append(FormatUtils.formatDecimal("###,##0.00",value)).toString();
	}

	public void setSimulacaoReajusteRotaService(
			
			SimulacaoReajusteRotaService simulacaoReajusteRotaService) {
		this.simulacaoReajusteRotaService = simulacaoReajusteRotaService;
	}

	public SimulacaoReajusteRota copyParametersToBean(TypedFlatMap parameters) {
		SimulacaoReajusteRota simulacao = new SimulacaoReajusteRota();
		simulacao.setIdSimulacaoReajusteRota(parameters.getLong("idSimulacaoReajusteRota"));
		simulacao.setDsSimulacaoReajusteRota(parameters.getString("dsSimulacaoReajusteRota"));
		simulacao.setTpReajuste(parameters.getDomainValue("tpReajuste"));
		simulacao.setVlReajuste(parameters.getBigDecimal("vlReajuste"));
		simulacao.setDtVigenciaInicial(parameters.getYearMonthDay("dtVigenciaInicial"));
		simulacao.setDtVigenciaFinal(parameters.getYearMonthDay("dtVigenciaFinal"));
		simulacao.setTpRota(parameters.getDomainValue("tpRota"));
		simulacao.setTpSituacaoRota(new DomainValue("S"));
		
		if (parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte") != null) {
			TipoMeioTransporte tipoMeioTransporte = new TipoMeioTransporte();
			tipoMeioTransporte.setIdTipoMeioTransporte(parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
			simulacao.setTipoMeioTransporte(tipoMeioTransporte);
		}
		if (parameters.getLong("regionalOrigem.idRegional") != null) {
			Regional regional = new Regional();
			regional.setIdRegional(parameters.getLong("regionalOrigem.idRegional"));
			simulacao.setRegionalOrigem(regional);
		}
		if (parameters.getLong("regionalDestino.idRegional") != null) {
			Regional regional = new Regional();
			regional.setIdRegional(parameters.getLong("regionalDestino.idRegional"));
			simulacao.setRegionalDestino(regional);
		}
		if (parameters.getLong("filialOrigem.idFilial") != null) {
			Filial filial = new Filial();
			filial.setIdFilial(parameters.getLong("filialOrigem.idFilial"));
			simulacao.setFilialOrigem(filial);
		}
		if (parameters.getLong("filialDestino.idFilial") != null) {
			Filial filial = new Filial();
			filial.setIdFilial(parameters.getLong("filialDestino.idFilial"));
			simulacao.setFilialDestino(filial);
		}
		if (parameters.getLong("meioTransporte.idMeioTransporte") != null) {
			MeioTransporte meioTransporte = new MeioTransporte();
			meioTransporte.setIdMeioTransporte(parameters.getLong("meioTransporte.idMeioTransporte"));
			simulacao.setMeioTransporte(meioTransporte);
		}
		if (parameters.getLong("proprietario.idProprietario") != null) {
			Proprietario proprietario = new Proprietario();
			proprietario.setIdProprietario(parameters.getLong("proprietario.idProprietario"));
			simulacao.setProprietario(proprietario);
		}
		
		if (parameters.getLong("moedaPais.idMoedaPais") != null) {
			MoedaPais moedaPais = new MoedaPais();
			moedaPais.setIdMoedaPais(parameters.getLong("moedaPais.idMoedaPais"));
			simulacao.setMoedaPais(moedaPais);
		}
		List list = setValuesList(parameters,"rotasT",Boolean.TRUE,simulacao);
		if (list != null)
			simulacao.setParametroSimulacaoRotas(list);
		else
			simulacao.setParametroSimulacaoRotas(setValuesList(parameters,"rotasF",Boolean.FALSE,simulacao));
		
		return simulacao;
	}

	public List setValuesList(TypedFlatMap parameters, String property, Boolean aplicar, SimulacaoReajusteRota simulacao) {
		List rotas = parameters.getList(property);
		if (rotas == null || rotas.isEmpty())
			return null;
		List result = new ArrayList();
		for(Iterator i = rotas.iterator();i.hasNext();) {
			AplicaReajusteRota reajusteRota = new AplicaReajusteRota();
			reajusteRota.setBlAplicacao(aplicar);
			reajusteRota.setSimulacaoReajusteRota(simulacao);
			RotaIdaVolta rota = new RotaIdaVolta();
			rota.setVersao(Integer.valueOf(1));
			TypedFlatMap flat = (TypedFlatMap)i.next();
			rota.setIdRotaIdaVolta(flat.getLong("rotaIdaVolta.idRotaIdaVolta"));
			rota.setNrRota(flat.getInteger("rotaIdaVolta.nrRota"));
			reajusteRota.setRotaIdaVolta(rota);
			result.add(reajusteRota);
		}
		return result;
	}

	public SimulacaoReajusteRotaDAO getSimulacaoReajusteRotaDAO() {
		return simulacaoReajusteRotaDAO;
	}

	public void setSimulacaoReajusteRotaDAO(
			SimulacaoReajusteRotaDAO simulacaoReajusteRotaDAO) {
		this.simulacaoReajusteRotaDAO = simulacaoReajusteRotaDAO;
	}

	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
}
