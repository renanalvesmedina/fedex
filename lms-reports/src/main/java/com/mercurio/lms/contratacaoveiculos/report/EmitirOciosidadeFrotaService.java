package com.mercurio.lms.contratacaoveiculos.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.PontoParada;
import com.mercurio.lms.municipios.model.PontoParadaTrecho;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;





/**
 *
 * @spring.bean id="lms.contratacaoveiculos.emitirOciosidadeFrotaService"
 * @spring.property name="reportName" value="com/mercurio/lms/contratacaoveiculos/report/emitirOciosidadeFrota.jasper"
 */
public class EmitirOciosidadeFrotaService extends ReportServiceSupport {
	
    
    private EventoMeioTransporteService eventoMeioTransporteService;
	private DomainService domainService;
        
	public JRReportDataObject execute(Map criteria) throws Exception {
		
		TypedFlatMap map = (TypedFlatMap)criteria;		
		SqlTemplate sql = montaSql(map);
		
		montaFilterSummary(map, sql);	    	    
	    
		final List dados = new LinkedList();
		
		getJdbcTemplate().query(sql.getSql(true), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()), new ResultSetExtractor() {
			
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				Domain dominio = getDomainService().findByName("DM_TIPO_VINCULO_VEICULO");
				
				while (rs.next()) {
					Map linha = new HashMap();
							 			
					linha.put("id_meio_transporte", Long.valueOf(rs.getLong("id_meio_transporte")));
					linha.put("frota_identificacao", rs.getString("frota_identificacao"));
					linha.put("tp_vinculo", dominio.findDomainValueByValue(rs.getString("tp_vinculo")).getDescription().getValue());
					linha.put("sg_filial", rs.getString("sg_filial"));
					linha.put("nm_pessoa", rs.getString("nm_fantasia"));
					linha.put("dh_evento_inicial", JodaTimeUtils.tstzToString(rs,rs.getObject("dh_evento_inicial")));
					if (rs.getObject("dh_evento_final") != null) {
						linha.put("dh_evento_final",JodaTimeUtils.tstzToString(rs,rs.getObject("dh_evento_final")));
						linha.put("hr_tempo_evento", Long.valueOf(rs.getLong("intervaloHoras")));
					}
					
					EventoMeioTransporte evt = createEventoMeioTransporte(
														rs.getString("tp_situacao_meio_transporte"),
														rs.getString("sg_filial"),
														rs.getString("nm_fantasia"),
														rs.getString("nm_ponto_parada"),
														rs.getString("sg_filial_origem"),
														rs.getString("sg_filial_destino"),
														rs.getString("sg_filial_cc_origem"),					
														rs.getString("sg_filial_cc_destino"),
														rs.getString("ds_local_manutencao"),
														rs.getString("sg_filial_ct_origem"),
														rs.getString("sg_filial_ct_destino"));
					
					linha.put("ds_evento", getEventoMeioTransporteService().determinaDescEvento(evt).toString());
					
					if (rs.getString("sg_filial_cc_origem") != null && rs.getLong("nr_controle_carga_ccv") != 0) {
						linha.put("ds_controle_carga", rs.getString("sg_filial_cc_origem") + " " + StringUtils.leftPad(String.valueOf(rs.getLong("nr_controle_carga_ccv")), 6, '0'));
					} else if (rs.getString("sg_filial_cc_ccce") != null && rs.getLong("nr_controle_carga_ccce") != 0) {
						linha.put("ds_controle_carga", rs.getString("sg_filial_cc_ccce") + " " + StringUtils.leftPad(String.valueOf(rs.getLong("nr_controle_carga_ccce")), 6, '0'));
					} else linha.put("ds_controle_carga", "");
					
					dados.add(linha);
				}
				return null;
			}}
		);
				
		JRMapCollectionDataSource ds = new JRMapCollectionDataSource(dados);
				
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,criteria.get("tpFormatoRelatorio"));
		return createReportDataObject(ds, parametersReport);
	}

	private void montaFilterSummary(TypedFlatMap map, SqlTemplate sql){
		
		sql.addFilterSummary("filial", map.getString("filial.siglaNomeFilial"));
		sql.addFilterSummary("tipoVinculo", map.getString("dsTipoVinculo"));
				
	    if (StringUtils.isNotBlank(map.getString("nrIdentificador")) && 
		    	 StringUtils.isNotBlank(map.getString("nrFrota"))){
				sql.addFilterSummary("meioTransporte", map.getString("nrFrota") + " - " +  map.getString("nrIdentificador"));
		} 
	    	    
		sql.addFilterSummary("evento", map.getString("dsEvento"));
			
		sql.addFilterSummary("tempoMinimoNoEvento", map.getString("hrTempoMinimo"));
	    
		if (StringUtils.isNotBlank(map.getString("dtPeriodoInicial"))){
			sql.addFilterSummary("periodoInicial", map.getYearMonthDay("dtPeriodoInicial"));
		}
		
		if (StringUtils.isNotBlank(map.getString("dtPeriodoFinal"))){
			sql.addFilterSummary("periodoFinal", map.getYearMonthDay("dtPeriodoFinal"));
		}	    

	}
	
	private SqlTemplate montaSql(TypedFlatMap parametros){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("m.id_meio_transporte");
	    sql.addProjection("m.nr_frota || ' - ' || m.nr_identificador", "frota_identificacao");
		sql.addProjection("m.tp_vinculo");
		sql.addProjection("f.sg_filial");
		sql.addProjection("p.nm_fantasia");
		sql.addProjection("emt.id_evento_meio_transporte");
		sql.addProjection("emt.dh_evento_inicial");
		sql.addProjection("emt.dh_evento_final");
		sql.addProjection("emt.ds_local_manutencao");
		sql.addProjection("to_seconds(emt.dh_evento_final - emt.dh_evento_inicial)", "intervaloHoras");	
		sql.addProjection("emt.tp_situacao_meio_transporte");
		sql.addProjection("ppt.id_ponto_parada_trecho");
		sql.addProjection("ppt.id_ponto_parada");
		sql.addProjection("p_ppt.nm_pessoa", "nm_ponto_parada");
		sql.addProjection("ppt.id_trecho_rota_ida_volta");
		sql.addProjection("fil_origem.sg_filial", "sg_filial_origem");
		sql.addProjection("fil_destino.sg_filial", "sg_filial_destino");
		sql.addProjection("fil_cc_origem.sg_filial", "sg_filial_cc_origem");
		sql.addProjection("fil_cc_destino.sg_filial", "sg_filial_cc_destino");
		sql.addProjection("fil_ct_origem.sg_filial",   "sg_filial_ct_origem");
		sql.addProjection("fil_ct_destino.sg_filial",   "sg_filial_ct_destino");
		sql.addProjection("cc.nr_controle_carga",   "nr_controle_carga_ccv");
		//Filial controle carga coleta entrega
		sql.addProjection("fil_ccce.sg_filial",   "sg_filial_cc_ccce");
		//Nro controle carga coleta entrega
		sql.addProjection("ccce.nr_controle_carga",   "nr_controle_carga_ccce");
		
		sql.addFrom("evento_meio_transporte", "emt");
		sql.addFrom("meio_transporte", "m");
		sql.addFrom("filial", "f");
		sql.addFrom("pessoa", "p");
        sql.addFrom("ponto_parada_trecho", "ppt");     
		sql.addFrom("pessoa", "p_ppt");
		sql.addFrom("trecho_rota_ida_volta", "triv");
		sql.addFrom("filial_rota", "fr_origem");
		sql.addFrom("filial_rota", "fr_destino");
		sql.addFrom("filial", "fil_origem");
		sql.addFrom("filial", "fil_destino");
		sql.addFrom("controle_trecho", "ct");
		sql.addFrom("controle_carga", "cc");
		sql.addFrom("filial", "fil_cc_origem");
		sql.addFrom("filial", "fil_cc_destino");
		sql.addFrom("filial", "fil_ct_origem");
		sql.addFrom("filial", "fil_ct_destino");
		//Filial controle carga coleta entrega
		sql.addFrom("filial", "fil_ccce");
		//Nro controle carga coleta entrega
		sql.addFrom("controle_carga", "ccce");
		     
		sql.addJoin("f.id_filial", "p.id_pessoa");
		sql.addJoin("emt.id_filial", "f.id_filial");
		sql.addJoin("emt.id_meio_transporte", "m.id_meio_transporte");	    
		sql.addJoin("emt.id_ponto_parada_trecho", "ppt.id_ponto_parada_trecho (+)");
		sql.addJoin("ppt.id_ponto_parada", "p_ppt.id_pessoa (+)");
		sql.addJoin("ppt.id_trecho_rota_ida_volta", "triv.id_trecho_rota_ida_volta (+)");
		sql.addJoin("triv.id_filial_rota_origem", "fr_origem.id_filial_rota (+)");
		sql.addJoin("triv.id_filial_rota_destino", "fr_destino.id_filial_rota (+)");
		sql.addJoin("fr_origem.id_filial", "fil_origem.id_filial (+)");
		sql.addJoin("fr_destino.id_filial", "fil_destino.id_filial (+)");
		sql.addJoin("emt.id_controle_trecho", "ct.id_controle_trecho (+)");
		//Nro controle carga coleta entrega
		sql.addJoin("ct.id_filial_origem","fil_ct_origem.id_filial(+)");
		//Filial controle carga coleta entrega
		sql.addJoin("ct.id_filial_destino","fil_ct_destino.id_filial(+)");
		
		sql.addJoin("ct.id_controle_carga", "cc.id_controle_carga (+)");
		sql.addJoin("cc.id_filial_origem", "fil_cc_origem.id_filial (+)");
		sql.addJoin("cc.id_filial_destino", "fil_cc_destino.id_filial (+)");
		
		sql.addJoin("emt.id_controle_carga", "ccce.id_controle_carga (+)");
		sql.addJoin("ccce.id_filial_origem", "fil_ccce.id_filial (+)");
		
		sql.addCustomCriteria(" trunc(cast(emt.dh_evento_inicial as date)) >= ? ");
		sql.addCriteriaValue(parametros.getYearMonthDay("dtPeriodoInicial"));
				    
		sql.addCustomCriteria("(trunc(cast(emt.dh_evento_final as date)) <= ? OR emt.dh_evento_final is null)");
		sql.addCriteriaValue(parametros.getYearMonthDay("dtPeriodoFinal"));

	    sql.addCriteria("f.id_filial", "=", parametros.getLong("filial.idFilial"));
	    sql.addCriteria("m.tp_vinculo", "=", parametros.getString("tpVinculo"));
	    sql.addCriteria("m.id_meio_transporte", "=", parametros.getLong("meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte"));
	    sql.addCriteria("emt.tp_situacao_meio_transporte", "=", parametros.getString("tpEvento"));
	    
	    if (!"".equals(parametros.getString("hrTempoMinimo"))){
	    	String tempo = parametros.getString("hrTempoMinimo");
	    	Long tempoMin = FormatUtils.converteHorasMinutosParaMinutos(tempo, FormatUtils.ESCALA_HHH);
	    	sql.addCriteria("to_seconds(emt.dh_evento_final - emt.dh_evento_inicial)", ">=", LongUtils.multiply(tempoMin, Long.valueOf(60)));
	    }
	    	    	    
	    sql.addOrderBy("m.tp_vinculo");
	    sql.addOrderBy("m.nr_frota");
	    sql.addOrderBy("emt.dh_evento_inicial");
	    sql.addOrderBy("emt.dh_evento_final");
	    
	    return sql;
	}
	
	private EventoMeioTransporte createEventoMeioTransporte(String tpSituacaoMeioTransporte, 
															String sgFilial, 
															String nmPessoaFilial, 
															String nmPontoparada, 
															String sgFilialOrigem, 
															String sgFilialDestino, 
															String sgFilialCCOrigem, 
															String sgFilialCCDestino, String dsLocalManutencao,
															String sgFilialCTOrigem, String sgFilialCTDestino){
		
		EventoMeioTransporte evt = new EventoMeioTransporte();
		evt.setTpSituacaoMeioTransporte(new DomainValue(tpSituacaoMeioTransporte));
		evt.setDsLocalManutencao(dsLocalManutencao);
		
		Filial filial = new Filial();
		Pessoa pessoa = new Pessoa();
		 
		pessoa.setNmFantasia(nmPessoaFilial);
		filial.setSgFilial(sgFilial);
		filial.setPessoa(pessoa);
		
		evt.setFilial(filial); 
		
		PontoParadaTrecho paradaTrecho = new PontoParadaTrecho();
		PontoParada pontoParada = new PontoParada();
		Pessoa pessoaPontoParada = new Pessoa();
		pessoaPontoParada.setNmPessoa(nmPontoparada);
		pontoParada.setPessoa(pessoaPontoParada);
		paradaTrecho.setPontoParada(pontoParada);
				
		TrechoRotaIdaVolta idaVolta = new TrechoRotaIdaVolta();
		FilialRota filialRotaOrigem = new FilialRota();
		FilialRota filialRotaDestino = new FilialRota();
		Filial filialOrigem = new Filial();
		Filial filialDestino = new Filial();
		filialOrigem.setSgFilial(sgFilialOrigem);
		filialRotaOrigem.setFilial(filialOrigem);		
		filialDestino.setSgFilial(sgFilialDestino);
		filialRotaDestino.setFilial(filialDestino);
		idaVolta.setFilialRotaByIdFilialRotaOrigem(filialRotaOrigem);
		idaVolta.setFilialRotaByIdFilialRotaDestino(filialRotaDestino);		
		paradaTrecho.setTrechoRotaIdaVolta(idaVolta);
		
		evt.setPontoParadaTrecho(paradaTrecho);
		
		ControleTrecho controleTrecho = new ControleTrecho();
		
		Filial filialTrechoOrigem = new Filial();
		filialTrechoOrigem.setSgFilial(sgFilialCTOrigem);
		Filial filialTrechoDestino = new Filial();
		filialTrechoDestino.setSgFilial(sgFilialCTDestino);
		controleTrecho.setFilialByIdFilialOrigem(filialTrechoOrigem);
		controleTrecho.setFilialByIdFilialDestino(filialTrechoDestino);
		
		
		
		ControleCarga carga = new ControleCarga();		
		Filial filialCCOrigem = new Filial();
		filialCCOrigem.setSgFilial(sgFilialCCOrigem);		
		Filial filialCCDestino = new Filial();
		filialCCDestino.setSgFilial(sgFilialCCDestino);
		carga.setFilialByIdFilialOrigem(filialCCOrigem);
		carga.setFilialByIdFilialDestino(filialCCDestino);		
		controleTrecho.setControleCarga(carga);
		
		evt.setControleTrecho(controleTrecho);
		
		return evt;
	}
	
	/**
	 * @return Returns the eventoMeioTransporteService.
	 */
	public EventoMeioTransporteService getEventoMeioTransporteService() {
		return eventoMeioTransporteService;
	}


	/**
	 * @param eventoMeioTransporteService The eventoMeioTransporteService to set.
	 */
	public void setEventoMeioTransporteService(
			EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}


	/**
	 * @return Returns the domainService.
	 */
	public DomainService getDomainService() {
		return domainService;
	}


	/**
	 * @param domainService The domainService to set.
	 */
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}
}