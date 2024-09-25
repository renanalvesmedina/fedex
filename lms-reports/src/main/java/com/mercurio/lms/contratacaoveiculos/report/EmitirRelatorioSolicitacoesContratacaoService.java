package com.mercurio.lms.contratacaoveiculos.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReferenciaFreteCarreteiroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.municipios.model.service.RotaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
*
* @spring.bean id="lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoService"
* @spring.property name="reportName" value="com/mercurio/lms/contratacaoveiculos/report/emitirRelatorioSolicitacoesContratacao.jasper"
*/

public class EmitirRelatorioSolicitacoesContratacaoService extends	ReportServiceSupport {
	private RotaService rotaService;
	private MoedaPaisService moedaPaisService;
	private FluxoFilialService fluxoFilialService;
	private RotaIdaVoltaService rotaIdaVoltaService;
	private ReferenciaFreteCarreteiroService referenciaFreteCarreteiroService;
	private DomainValueService domainValueService;
	
	public JRReportDataObject execute(Map arg0) throws Exception {
		TypedFlatMap criteria = (TypedFlatMap) arg0;
		
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("SC.NR_SOLICITACAO_CONTRATACAO", "NR_SOLICITACAO_CONTRATACAO");
		sql.addProjection("SC.DT_CRIACAO", "DT_CRIACAO");
		sql.addProjection("SC.TP_ROTA_SOLICITACAO", "TP_ROTA_SOLICITACAO");
		sql.addProjection("SC.TP_SOLICITACAO_CONTRATACAO", "TP_SOLICITACAO_CONTRATACAO");
		sql.addProjection("SC.ID_ROTA", "ID_ROTA");
		sql.addProjection("SC.ID_ROTA_IDA_VOLTA", "ID_ROTA_IDA_VOLTA");
		sql.addProjection("SC.VL_FRETE_SUGERIDO", "VL_FRETE_SUGERIDO");
		sql.addProjection("SC.VL_FRETE_MAXIMO_AUTORIZADO", "VL_FRETE_MAXIMO_AUTORIZADO");
		sql.addProjection("SC.NR_IDENTIFICACAO_MEIO_TRANSP", "NR_IDENTIFICACAO_MEIO_TRANSP");
		sql.addProjection("SC.NR_IDENTIFICACAO_SEMI_REBOQUE", "NR_IDENTIFICACAO_SEMI_REBOQUE");
		sql.addProjection("SC.TP_VINCULO_CONTRATACAO", "TP_VINCULO_CONTRATACAO");
		sql.addProjection("SC.TP_SITUACAO_CONTRATACAO", "TP_SITUACAO_CONTRATACAO");
		sql.addProjection("SC.OB_OBSERVACAO", "OB_OBSERVACAO");
		sql.addProjection("SC.ID_MOEDA_PAIS", "ID_MOEDA_PAIS");
		
		
		sql.addProjection("SC_FILIAL.SG_FILIAL", "SG_FILIAL");
		sql.addProjection("SC_USUARIO.NM_USUARIO", "NM_SOLICITANTE");
		sql.addProjection("SC_TIPO_MEIO_TRANSPORTE.DS_TIPO_MEIO_TRANSPORTE", "DS_TIPO_MEIO_TRANSPORTE");
		sql.addProjection("SC_TIPO_MEIO_TRANSPORTE.ID_TIPO_MEIO_TRANSPORTE", "ID_TIPO_MEIO_TRANSPORTE");
		
		sql.addFrom("SOLICITACAO_CONTRATACAO","SC");
		sql.addFrom("FILIAL","SC_FILIAL");
		sql.addFrom("USUARIO","SC_USUARIO");
		sql.addFrom("TIPO_MEIO_TRANSPORTE","SC_TIPO_MEIO_TRANSPORTE");
		
		sql.addJoin("SC.ID_FILIAL", "SC_FILIAL.ID_FILIAL");
		sql.addJoin("SC.ID_FUNCIONARIO_SOLICITADOR", "SC_USUARIO.ID_USUARIO");
		sql.addJoin("SC.ID_TIPO_MEIO_TRANSPORTE", "SC_TIPO_MEIO_TRANSPORTE.ID_TIPO_MEIO_TRANSPORTE");
		
		
		mountSimpleFilters(criteria, sql);
		
		//Filtro para rota eventual
		String tpRota = criteria.getString("tpRotaSolicitacao");
		if (tpRota != null && tpRota.equals("EV")){
			List filiaisRota = criteria.getList("rotaEventual.filiaisRota");
			String dsRota = "";
			if (filiaisRota != null && filiaisRota.size()>0){
				for (Iterator i =filiaisRota.iterator();i.hasNext();){
					TypedFlatMap map = (TypedFlatMap)i.next();
					String sgFilial = map.getString("filialRota.sgFilial"); 
					dsRota = dsRota.concat(StringUtils.isEmpty(dsRota)?sgFilial:"-".concat(sgFilial));
				}
				Long idRota = getRotaService().findIdRotaByDescricao(dsRota);
				if (idRota == null){
					throw new BusinessException("emptyReport");
				}else{
					sql.addCriteria("SC.ID_ROTA", "=", idRota);
					sql.addFilterSummary("rotaEventual", dsRota);
				}
			}
		}
		
		
		final LinkedList dados = new LinkedList();
		Object[] args = JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria());
		getJdbcTemplate().query(sql.getSql(), args, new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException,	DataAccessException {
				while(rs.next()){
					Map row = new HashMap();
					if (rs.getString("NR_SOLICITACAO_CONTRATACAO")!=null){
						String nrSolicitacao = FormatUtils.fillNumberWithZero(rs.getString("NR_SOLICITACAO_CONTRATACAO"), 6);
						row.put("NR_SOLICITACAO", rs.getString("SG_FILIAL").concat(nrSolicitacao));
					}
					row.put("NM_SOLICITANTE", rs.getString("NM_SOLICITANTE"));
					row.put("DS_TIPO_MEIO_TRANSPORTE", rs.getString("DS_TIPO_MEIO_TRANSPORTE"));
					row.put("TP_ROTA_SOLICITACAO", rs.getString("TP_ROTA_SOLICITACAO"));
					row.put("TP_SOLICITACAO_CONTRATACAO", rs.getString("TP_SOLICITACAO_CONTRATACAO"));
					row.put("VL_FRETE_SUGERIDO", rs.getBigDecimal("VL_FRETE_SUGERIDO"));
					row.put("VL_FRETE_MAXIMO_AUTORIZADO", rs.getBigDecimal("VL_FRETE_MAXIMO_AUTORIZADO"));
					row.put("NR_IDENTIFICACAO_MEIO_TRANSP", rs.getString("NR_IDENTIFICACAO_MEIO_TRANSP"));
					row.put("NR_IDENTIFICACAO_SEMI_REBOQUE", rs.getString("NR_IDENTIFICACAO_SEMI_REBOQUE"));
					row.put("TP_VINCULO_CONTRATACAO", rs.getString("TP_VINCULO_CONTRATACAO"));
					String situacao = domainValueService.findDomainValueByValue("DM_SITUACAO_SOLICITACAO_CONTRATACAO", rs.getString("TP_SITUACAO_CONTRATACAO")).getDescriptionAsString();
					row.put("TP_SITUACAO_CONTRATACAO", situacao);
					row.put("OB_OBSERVACAO", rs.getString("OB_OBSERVACAO"));
					row.put("DT_CRIACAO", rs.getDate("DT_CRIACAO"));
					
					BigDecimal vlReferencia;
					String dsRota;
					Integer nrDistancia;
					//Processamentos para rotas eventuais
					if (rs.getString("TP_ROTA_SOLICITACAO").equalsIgnoreCase("EV")){
						Rota rota = rotaService.findById(rs.getLong("ID_ROTA"));
						List<FilialRota> filiaisRota = rota.getFilialRotas();
						dsRota = rota.getDsRota();							
						
						//Calcula a distancia entre as filiais da rota
						ArrayList<Filial> filiais = new ArrayList<Filial>();
						for (FilialRota fr:filiaisRota){
							filiais.add(fr.getFilial());
						}
						nrDistancia = fluxoFilialService.findDistanciaTotalFluxoFilialOrigemDestino(filiais,new YearMonthDay());
						
						//Busca o valor de referencia 
						Long idMoeda = moedaPaisService.findById(rs.getLong("ID_MOEDA_PAIS")).getIdMoedaPais();
						Long idUFOrigem = filiaisRota.get(0).getFilial().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
						Long idUFDestino = filiaisRota.get(filiaisRota.size()-1).getFilial().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
						
						//Tenta pelas filiais de origem e destino
						vlReferencia = referenciaFreteCarreteiroService.findValorFreteCarreteiroByUfsFiliais(
								null,null, 
								filiaisRota.get(0).getFilial().getIdFilial(), 
								filiaisRota.get(filiaisRota.size()-1).getFilial().getIdFilial(), 
								idMoeda, 
								rs.getLong("ID_TIPO_MEIO_TRANSPORTE"), 
								null);
						//Se não encontrou, tenta pelas UF`s de origem e destino
						if (vlReferencia==null){
							vlReferencia = referenciaFreteCarreteiroService.findValorFreteCarreteiroByUfsFiliais(
								idUFOrigem,idUFDestino,null,null,idMoeda,rs.getLong("ID_TIPO_MEIO_TRANSPORTE"),null);
							
						}
						//Se ainda não encontrou tenta somente pelo tipo de transporte
						if (vlReferencia == null){
							vlReferencia = referenciaFreteCarreteiroService.findValorFreteCarreteiroByUfsFiliais(
									null,null,null,null,idMoeda,rs.getLong("ID_TIPO_MEIO_TRANSPORTE"),null);
						}
						//Mas também, se aqui ainda não encontrou, que se dane! Vai em branco mesmo!
						if (vlReferencia==null){
							vlReferencia = new BigDecimal(0);
						}
						
					//Processamento para rotas expressas	
					}else{
						RotaIdaVolta rotaIdaVolta = rotaIdaVoltaService.findById(rs.getLong("ID_ROTA_IDA_VOLTA"));
						dsRota = rotaIdaVolta.getRota().getDsRota();
						nrDistancia = rotaIdaVolta.getNrDistancia();
						vlReferencia = rotaIdaVolta.getVlFreteKm().multiply(new BigDecimal(nrDistancia.intValue()));
					}
					
					row.put("VL_REFERENCIA", vlReferencia);
					row.put("NR_DISTANCIA", nrDistancia);
					row.put("DS_ROTA", dsRota);
					double vlAutorizado;
					if (rs.getBigDecimal("VL_FRETE_MAXIMO_AUTORIZADO")!=null){
						vlAutorizado = rs.getBigDecimal("VL_FRETE_MAXIMO_AUTORIZADO").doubleValue();
					}else{
						vlAutorizado = 0;
					}
					row.put("VL_FRETE_KM",vlAutorizado / nrDistancia.intValue());
					
					dados.add(row);
				}
				return null;
			}
		});
		
		JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dados);
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,JRReportDataObject.EXPORT_PDF);
		return createReportDataObject(dataSource, parametersReport);
	}

	private void mountSimpleFilters(TypedFlatMap criteria, SqlTemplate sql) {

		sql.addCriteria("SC.ID_FILIAL", "=", criteria.getLong("filial.idFilial"));
		sql.addFilterSummary("filial", criteria.getString("filial.nmFantasia"));

		sql.addCriteria("SC.ID_FUNCIONARIO_SOLICITADOR", "=", criteria.getLong("usuario.idUsuario"));
		sql.addFilterSummary("solicitante", criteria.getString("usuario.nmUsuario"));
		
		sql.addCriteria("SC.ID_TIPO_MEIO_TRANSPORTE", "=", criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		sql.addFilterSummary("tipoVeiculo", criteria.getString("tipoMeioTransporte.idTipoMeioTransporte"));

		if(criteria.getString("tpSolicitacaoContratacao")!=null && criteria.getString("tpSolicitacaoContratacao").length()>0){
			sql.addCriteria("SC.TP_SOLICITACAO_CONTRATACAO", "=", criteria.getString("tpSolicitacaoContratacao"));
			String tipoSolicitacao = domainValueService.findDomainValueByValue("DM_TIPO_SOLICITACAO_CONTRATACAO", 
					criteria.getString("tpSolicitacaoContratacao")).getDescriptionAsString();
			sql.addFilterSummary("tipoSolicitacao", tipoSolicitacao);
		}

		sql.addCriteria("SC.TP_SITUACAO_CONTRATACAO", "=", criteria.getString("tpSituacaoContratacao"));
		sql.addFilterSummary("situacao", criteria.getString("tpSituacaoContratacao"));
		
		sql.addCriteria("SC.ID_TIPO_MEIO_TRANSPORTE", "=", criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		sql.addFilterSummary("tipoMeioTransporte", criteria.getString("tipoMeioTransporte.idTipoMeioTransporte"));
		
		if(criteria.getString("tpVinculoContratacao")!=null && criteria.getString("tpVinculoContratacao").length()>0){
			sql.addCriteria("SC.TP_VINCULO_CONTRATACAO", "=", criteria.getString("tpVinculoContratacao"));
			String tipoVinculo = domainValueService.findDomainValueByValue("DM_TIPO_VINCULO_VEICULO", 
					criteria.getString("tpVinculoContratacao")).getDescriptionAsString();
			sql.addFilterSummary("tipoVinculo", tipoVinculo);
		}
		
		if(criteria.getString("tpRotaSolicitacao")!=null && criteria.getString("tpRotaSolicitacao").length()>0){
			sql.addCriteria("SC.TP_ROTA_SOLICITACAO", "=", criteria.getString("tpRotaSolicitacao"));
			String tipoRota = domainValueService.findDomainValueByValue("DM_TIPO_ROTA_SOLICITACAO", 
					criteria.getString("tpRotaSolicitacao")).getDescriptionAsString(); 
			sql.addFilterSummary("tipoRota",tipoRota);
		}
		

		sql.addCriteria("SC.ID_ROTA_IDA_VOLTA","=",criteria.getString("rotaIdaVolta.idRotaIdaVolta"));
		sql.addFilterSummary("rotaExpressa",criteria.getString("rotaIdaVolta.dsRota"));

		sql.addCustomCriteria("SC.DT_CRIACAO >= ?", criteria.getYearMonthDay("dtSolicitacaoInicial"));
		sql.addFilterSummary("periodoInicial", criteria.getYearMonthDay("dtSolicitacaoInicial"));
		
		sql.addCustomCriteria("SC.DT_CRIACAO <= ?", criteria.getYearMonthDay("dtSolicitacaoFinal"));
		sql.addFilterSummary("periodoFinal", criteria.getYearMonthDay("dtSolicitacaoFinal"));

	}
	
	@Override
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_ROTA_SOLICITACAO", "DM_TIPO_ROTA_SOLICITACAO");
		config.configDomainField("TP_VINCULO_CONTRATACAO", "DM_TIPO_VINCULO_VEICULO");
		config.configDomainField("TP_SITUACAO_CONTRATACAO", "DM_TIPO_SITUACAO_SOLICITACAO_CONTRATACAO");
		config.configDomainField("TP_SOLICITACAO_CONTRATACAO", "DM_TIPO_SOLICITACAO_CONTRATACAO");
	}
	
	public RotaService getRotaService() {
		return rotaService;
	}


	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
	}

	public ReferenciaFreteCarreteiroService getReferenciaFreteCarreteiroService() {
		return referenciaFreteCarreteiroService;
	}

	public void setReferenciaFreteCarreteiroService(
			ReferenciaFreteCarreteiroService referenciaFreteCarreteiroService) {
		this.referenciaFreteCarreteiroService = referenciaFreteCarreteiroService;
	}

	public MoedaPaisService getMoedaPaisService() {
		return moedaPaisService;
	}

	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}

	public RotaIdaVoltaService getRotaIdaVoltaService() {
		return rotaIdaVoltaService;
	}

	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}

	public FluxoFilialService getFluxoFilialService() {
		return fluxoFilialService;
	}

	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}
