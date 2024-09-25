package com.mercurio.lms.contratacaoveiculos.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.report.EmitirRelatorioSolicitacoesContratacaoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
/**
 * @author vagnerh
 * @spring.bean="lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoAction"
 */
public class EmitirRelatorioSolicitacoesContratacaoAction extends ReportActionSupport {
	private FilialService filialService;
	private UsuarioService usuarioService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private RotaIdaVoltaService rotaIdaVoltaService;

	public List findLookupRotaExpressa(TypedFlatMap criteria){
		return getRotaIdaVoltaService().findLookupRotaIdaVolta(criteria);
	}
	
	public List findLookupFilial(TypedFlatMap criteria){
		return getFilialService().findLookup(criteria);
	}
	
	public List findLookupUsuario(TypedFlatMap criteria){
		String nrMatricula = criteria.getString("nrMatricula");
		return getUsuarioService().findLookupUsuarioFuncionario(
				null, nrMatricula, null, null, null, null, true);
	}
	
	public List findComboTpMeioTransporte(TypedFlatMap criteria){
		List<TipoMeioTransporte> lista = getTipoMeioTransporteService().find(criteria);
		FilterList filter = new FilterList(lista){
			public Map filterItem(Object item){
				TipoMeioTransporte tmt = (TipoMeioTransporte)item;
				HashMap map = new HashMap();
				map.put("idTipoMeioTransporte", tmt.getIdTipoMeioTransporte());
				map.put("tpSituacao", tmt.getTpSituacao());
				map.put("dsTipoMeioTransporte", tmt.getDsTipoMeioTransporte());
				return map;
			}
		};
		return (List)filter.doFilter();
	}

	public TypedFlatMap findBasicData(){
		TypedFlatMap ret = new TypedFlatMap();
		ret.put("dtSolicitacaoInicial", new YearMonthDay().minusMonths(1).toString("dd/MM/yyyy"));
		ret.put("dtSolicitacaoFinal", new YearMonthDay().toString("dd/MM/yyyy"));
		return ret;
	}
	
	/* Getters & Setters */
	
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public TipoMeioTransporteService getTipoMeioTransporteService() {
		return tipoMeioTransporteService;
	}

	public void setTipoMeioTransporteService(
			TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}
	
	public void setReportService(EmitirRelatorioSolicitacoesContratacaoService service){
		this.reportServiceSupport = service;
	}
	
	public RotaIdaVoltaService getRotaIdaVoltaService() {
		return rotaIdaVoltaService;
	}

	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}

}