package com.mercurio.lms.expedicao.swt.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.LiberaAWBComplementar;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.LiberaAWBComplementarService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesAwb;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;

public class ManterLiberacaoAWBComplementarAction extends CrudAction {
	private static final String SG_EMPRESA = "sgEmpresa";
	
	private FilialService filialService;
	private EmpresaService empresaService;
	private UsuarioService usuarioService;
	private AwbService awbService;
	private UsuarioLMSService usuarioLMSService; 

	@SuppressWarnings({"rawtypes", "unchecked"})
	public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List findCiaAerea(Map criteria) {
		List empresas = empresaService.findCiaAerea(criteria);
		if (empresas != null && !empresas.isEmpty()) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < empresas.size(); i++) {
				Empresa empresa = (Empresa) empresas.get(i);
				Map<String, Object> e = new HashMap<String, Object>();
				e.put("idEmpresa", empresa.getIdEmpresa());
				e.put("nmPessoa", empresa.getPessoa().getNmPessoa());
				result.add(e);
			}
			return result;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List findLookupUsuarioFuncionario(Map criteria) {
		return usuarioService.findLookupUsuarioFuncionario(
			null,
			FormatUtils.fillNumberWithZero(((Integer)criteria.get("nrMatricula")).toString(), 9),
			(Long)criteria.get("idFilial"),
			null,
			null,
			null,
			true
		);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findLookupAwb(Map criteria) {
		List listAwbs = awbService.findLookupAwb(criteria);
		List listResult = new ArrayList();
		for (Iterator iter = listAwbs.iterator(); iter.hasNext();) {
			Awb awb = (Awb) iter.next();
			Map mapAwb = populateAwbLkp(awb);
			listResult.add(mapAwb);
		}
		return listResult;
	}

	private Map populateAwbLkp(Awb awb) {		
		Map mapAwb = new HashMap();
		mapAwb.put("idAwb", awb.getIdAwb());
		if(ConstantesAwb.TP_STATUS_PRE_AWB.equals(awb.getTpStatusAwb().getValue())){
			mapAwb.put("nrAwb", awb.getIdAwb());
		} else {
			mapAwb.put("nrAwb", AwbUtils.getNrAwb(awb));
		}
		
		mapAwb.put("tpStatusAwb", awb.getTpStatusAwb().getValue());
		Empresa e = awb.getCiaFilialMercurio().getEmpresa();
		mapAwb.put(SG_EMPRESA, e.getSgEmpresa());
		if(awb.getVlFrete() != null){
			mapAwb.put("vlFrete", awb.getVlFrete());
		} else {
			mapAwb.put("vlFrete", BigDecimal.ZERO);
		}
		mapAwb.put("idEmpresa", e.getIdEmpresa());
		mapAwb.put("idEmpresaCiaAerea", e.getIdEmpresa());
		return mapAwb;
	}	
	
	@SuppressWarnings("rawtypes")
	public  Map<String, Object> storeLiberacao(Map formbean) {
		LiberaAWBComplementar liberaAWBComplementar = new LiberaAWBComplementar();
		liberaAWBComplementar.setIdLiberaAWBComplementar((Long)formbean.get("idLiberaAWBComplementar"));
		Awb awb = new Awb();
		awb.setIdAwb((Long)formbean.get("idAwb"));
		liberaAWBComplementar.setAwbOriginal(awb);
		Empresa empresa = new Empresa();
		empresa.setIdEmpresa((Long)formbean.get("idEmpresa"));
		liberaAWBComplementar.setEmpresa(empresa);
		Filial filial = new Filial();
		filial.setIdFilial((Long)formbean.get("idFilial"));
		liberaAWBComplementar.setFilialSolicitante(filial);
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario((Long)formbean.get("idUsuario"));
		liberaAWBComplementar.setUsuarioSolicitante(usuarioLMS);
		liberaAWBComplementar.setDsMotivo((String) formbean.get("motivo"));
		liberaAWBComplementar = this.getLiberaAWBComplementarService().storeLiberacao(liberaAWBComplementar);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dhLiberacao", liberaAWBComplementar.getDhLiberacao());
		
		if (liberaAWBComplementar.getUsuarioLiberador() != null) {
			usuarioLMS = usuarioLMSService.findById(liberaAWBComplementar.getUsuarioLiberador().getIdUsuario());
			map.put("nrUsuarioLiberacao", usuarioLMS.getUsuarioADSM().getNrMatricula());
			map.put("nmUsuarioLiberacao", usuarioLMS.getUsuarioADSM().getNmUsuario());
		}
		map.put("dsSenha", liberaAWBComplementar.getDsSenha());

		return map;
	}
	
	public List findLookupSgCiaAerea(Map criteria) {
		TypedFlatMap mapResult = new TypedFlatMap();
		List listResult = new ArrayList();
		
		criteria.put("tpEmpresa", ConstantesExpedicao.TP_EMPRESA_CIA_AEREA);
		criteria.put(SG_EMPRESA, criteria.get(SG_EMPRESA).toString().toUpperCase());
		
		List ciaList = empresaService.findLookupEmpresaAwb(criteria);
		if (ciaList != null && !ciaList.isEmpty()) {
			for (int i = 0; i < ciaList.size(); i++) {
				Empresa ciaAerea = (Empresa) ciaList.get(i);
				mapResult.put("idEmpresa", ciaAerea.getIdEmpresa());
				mapResult.put("idEmpresaCiaAerea", ciaAerea.getIdEmpresa());
				mapResult.put(SG_EMPRESA, ciaAerea.getSgEmpresa());
				
				listResult.add(mapResult);
			}
		}		
		
		return listResult;
	}	
	
	public void removeById(Long id) {
		this.getLiberaAWBComplementarService().removeById(id);
    }
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		this.getLiberaAWBComplementarService().removeByIds(ids);
    }
	
	@SuppressWarnings("rawtypes")
	public void validateAwbAComplementar(Map criteria) {
		this.getLiberaAWBComplementarService().validateAwbAComplementar((Long) criteria.get("idLiberaAWBComplementar"), (Long) criteria.get("idAwb"));
	}
	
	public Map<String, Object> findById(java.lang.Long id) {
		return getLiberaAWBComplementarService().findMapById(id);
	}
	
	public void setService(LiberaAWBComplementarService liberaAWBComplementarService) {
		super.defaultService = liberaAWBComplementarService;
	}
	
	public LiberaAWBComplementarService getLiberaAWBComplementarService() {
		return (LiberaAWBComplementarService) super.defaultService;
	}
	

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	
	
}
