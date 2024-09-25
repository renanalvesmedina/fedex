package com.mercurio.lms.expedicao.swt.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.ReemissaoEtiquetaVolume;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ReemissaoEtiquetaVolumeService;
import com.mercurio.lms.expedicao.report.EmitirRelatorioReemissaoEtiquetaVolumeService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class ReemissaoEtiquetaVolumeAction extends CrudAction {
	private FilialService filialService;
	private UsuarioService usuarioService;
	private ClienteService clienteService;
	private ReemissaoEtiquetaVolumeService reemissaoEtiquetaVolumeService;
	private ReportExecutionManager reportExecutionManager;
	private EmitirRelatorioReemissaoEtiquetaVolumeService emitirRelatorioReemissaoEtiquetaVolumeService;

	public void setEmitirRelatorioReemissaoEtiquetaVolumeService(EmitirRelatorioReemissaoEtiquetaVolumeService emitirRelatorioReemissaoEtiquetaVolumeService) {
		this.emitirRelatorioReemissaoEtiquetaVolumeService = emitirRelatorioReemissaoEtiquetaVolumeService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public List<Map<String, Object>> findLookupCliente(Map criteria) {
		List<Map<String, Object>> clientes = clienteService.findClienteByNrIdentificacao((String) criteria.get("nrIdentificacao"));
		if (clientes != null) {
			for (Map cliente : clientes) {
				cliente.remove("tpCliente");
				Map pessoa = (Map) cliente.remove("pessoa");
				if (pessoa != null) {
					cliente.put("nmPessoa", pessoa.get("nmPessoa"));
					cliente.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
				}
			}
		}
		return clientes;
	}

	public List findLookupFilialEmissao(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilialEmissao", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	public List findLookupFilialReemissao(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilialReemissao", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	public List findLookupImprimir(Map criteria) {
		return new ArrayList();
	}

	public List findLookupUsuario(TypedFlatMap criteria) {
		return usuarioService.findLookupUsuarioFuncionario(null, criteria.getString("nrMatricula"), null, null, null, null, true);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = reemissaoEtiquetaVolumeService.findPaginated(criteria);
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return reemissaoEtiquetaVolumeService.getRowCount(criteria);
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setReemissaoEtiquetaVolumeService(ReemissaoEtiquetaVolumeService reemissaoEtiquetaVolumeService) {
		this.reemissaoEtiquetaVolumeService = reemissaoEtiquetaVolumeService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@SuppressWarnings("rawtypes")
	public String execute(Map filters) throws Exception {
		TypedFlatMap tfm = new TypedFlatMap();
		ReflectionUtils.flatMap(tfm, filters);

		if (JRReportDataObject.EXPORT_CSV.equalsIgnoreCase(tfm.getString("tpFormatoRelatorio"))) {
			File file = emitirRelatorioReemissaoEtiquetaVolumeService.executeReport(tfm);
			if (file == null) {
				throw new BusinessException("emptyReport");
			}
			return reportExecutionManager.generateReportLocator(file);
		} else {
			return this.reportExecutionManager.generateReportLocator(this.emitirRelatorioReemissaoEtiquetaVolumeService, tfm);
		}
	}
	
	public Map store(Map data) {
		ReemissaoEtiquetaVolume bean = new ReemissaoEtiquetaVolume();

		Filial filial=new Filial();
		UsuarioLMS usuario=new UsuarioLMS();
		VolumeNotaFiscal volumeNotaFiscal=new VolumeNotaFiscal();
		
		filial.setIdFilial((Long)data.get("idFilial"));
		usuario.setIdUsuario((Long)data.get("idUsuario"));
		volumeNotaFiscal.setIdVolumeNotaFiscal((Long)data.get("idVolumeNotaFiscal"));
		
		bean.setDhReemissao(new DateTime());
		bean.setDsMac((String)data.get("dsMac"));
		bean.setFilial(filial);
		bean.setUsuario(usuario);
		bean.setVolumeNotaFiscal(volumeNotaFiscal);
		
		Long idReemissaoEtiquetaVolume = (Long) this.reemissaoEtiquetaVolumeService.store(bean);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("idReemissaoEtiquetaVolume", idReemissaoEtiquetaVolume);
		return result;
		
	}
}
