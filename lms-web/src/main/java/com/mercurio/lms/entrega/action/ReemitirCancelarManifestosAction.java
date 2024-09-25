package com.mercurio.lms.entrega.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.entrega.model.service.CancelarManifestoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.report.EmitirManifestoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.reemitirCancelarManifestosAction"
 */
 
public class ReemitirCancelarManifestosAction {
	
	private FilialService filialService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private ManifestoEntregaService manifestoEntregaService;
	private CancelarManifestoService cancelarManifestoService;
	private ControleCargaService controleCargaService;
	private EmitirManifestoService emitirManifestoService;
	private ReportExecutionManager reportExecutionManager;
	
	
	static final String CANCELAR = "botaoCancelar";
	static final String CANCELAR_COM_APROVEITAMENTO = "botaoCancelarAproveitamento";
	
	/**
	 * Insere os registros de documentos de servicos selecionados na pop-up na sessao,
	 * criando novas instancias de DocumentoServicoRetirada
	 * @param values
	 */
	public void executeCancelar(TypedFlatMap values) {
		String botao = values.getString("botaoCancelar");

		if (botao != null) {
			List<String> sids = values.getList("ids");
			List<Long> ids = new ArrayList<Long>(sids.size());
			for(String id : sids) {
				ids.add(Long.valueOf(id));
			}

			// Se for clicado na tela para cancelar:
			if (botao.equals(CANCELAR)) {
				cancelarManifestoService.executeCancelarManifesto(ids);
			// Se for clicado na tela para cancelar com aproveitamento:
			} else if (botao.equals(CANCELAR_COM_APROVEITAMENTO)) {
				cancelarManifestoService.executeCancelarManifestoComAproveitamento(ids);
			}
		}
	}
	
	public String executeManifesto(TypedFlatMap parameters) throws Exception {
		return this.reportExecutionManager.generateReportLocator(emitirManifestoService, parameters);
	}
	
	public List findLookupFilial(Map criteria){
		return filialService.findLookup(criteria);
	}
		
	public List findRotaLookup(Map criteria){
		return rotaColetaEntregaService.findLookup(criteria);
	}

	/**
	 * lookup de controle de carga 
	 * @param criteria
	 * @return
	 */
	public List findControleCarga(Map criteria) {
		List list = this.controleCargaService.findLookup(criteria);

		FilterList filter = new FilterList(list) {
			public Map filterItem(Object item) {
				ControleCarga controleCarga = (ControleCarga)item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();

				typedFlatMap.put("idControleCarga", controleCarga.getIdControleCarga());
				typedFlatMap.put("nrControleCarga", controleCarga.getNrControleCarga());
				typedFlatMap.put("filialByIdFilialOrigem.idFilial", controleCarga.getFilialByIdFilialOrigem().getIdFilial());
				typedFlatMap.put("filialByIdFilialOrigem.sgFilial", controleCarga.getFilialByIdFilialOrigem().getSgFilial());

				return typedFlatMap;
			}
		};
		return (List)filter.doFilter();
	}
	
	public TypedFlatMap findFilialUsuarioLogado(){
		Filial fil = SessionUtils.getFilialSessao();	
		TypedFlatMap retorno = new TypedFlatMap(); 
		
		boolean isUsuarioDIVOP = controleCargaService.validateManutencaoEspecialCC(SessionUtils.getUsuarioLogado());
		
		retorno.put("idFilial", fil.getIdFilial());
		retorno.put("sgFilial", fil.getSgFilial());
		retorno.put("nmFantasia", fil.getPessoa().getNmFantasia());
		retorno.put("isUsuarioDIVOP", isUsuarioDIVOP ? "S" : "N");
		
		return retorno;
	}
	
	public List findLookupManifestoEntrega(TypedFlatMap criteria){
		return manifestoEntregaService.findLookupCustom(criteria);
	}
	
	public List findLookupMeioTransporteRodoviario(Map tfm) {
		return meioTransporteRodoviarioService.findLookup(tfm);
	}

	/**
	 * RowCount da grid da tela Reemitir/Cancelar Manifesto de Entrega
	 * @param parametros
	 * @return
	 */
	public Integer getRowCountGridCancelarManifesto(TypedFlatMap parametros) {
		return manifestoEntregaService.getRowCountGridCancelarManifesto(parametros);
	}

	/**
	 * Consulta utilizada na grid da tela Reemitir/Cancelar Manifesto de Entrega
	 * @param parametros
	 * @return
	 */
	public ResultSetPage findPaginatedGridCancelarManifesto(TypedFlatMap parametros) {
		ResultSetPage rsp = manifestoEntregaService.findPaginatedGridCancelarManifesto(parametros);

		List list = rsp.getList();
		List ids = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Map item = (Map) iter.next();
			ids.add(item.get("idManifestoEntrega"));			
		}
		return rsp;
	}

	/**
	 * @param filialService The filialService to set.
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
	 * @param meioTransporteRodoviarioService The meioTransporteRodoviarioService to set.
	 */
	public void setMeioTransporteRodoviarioService(
			MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}

	/**
	 * @param rotaColetaEntregaService The rotaColetaEntregaService to set.
	 */
	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	/**
	 * @param manifestoEntregaService The manifestoEntregaService to set.
	 */
	public void setManifestoEntregaService(
			ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}


	/**
	 * @param cancelarManifestoService The cancelarManifestoService to set.
	 */
	public void setCancelarManifestoService(
			CancelarManifestoService cancelarManifestoService) {
		this.cancelarManifestoService = cancelarManifestoService;
	}


	/**
	 * @param controleCargaService The controleCargaService to set.
	 */
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	/**
	 * @param emitirManifestoService The emitirManifestoService to set.
	 */
	public void setEmitirManifestoService(EmitirManifestoService emitirManifestoService) {
		this.emitirManifestoService = emitirManifestoService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
} 
