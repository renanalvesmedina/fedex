package com.mercurio.lms.expedicao.swt.action;

import java.io.Serializable;
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
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.EtiquetaAfericao;
import com.mercurio.lms.expedicao.model.service.EtiquetaAfericaoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;


public class ManterEtiquetasAfericaoAction extends CrudAction {

	private FilialService filialService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private ParametroGeralService parametroGeralService;

	public Map findById(java.lang.Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		EtiquetaAfericao etiqueta = (EtiquetaAfericao) getService().findById(id);

		result.put("idEtiquetaAfericao", etiqueta.getIdEtiquetaAfericao());
		result.put("idFilialOrigem", etiqueta.getFilialOrigem().getIdFilial());
		result.put("sgFilialOrigem", etiqueta.getFilialOrigem().getSgFilial());
		result.put("nmFantasiaOrigem", etiqueta.getFilialOrigem().getPessoa().getNmFantasia());
		result.put("idFilialDestino", etiqueta.getFilialDestino().getIdFilial());
		result.put("sgFilialDestino", etiqueta.getFilialDestino().getSgFilial());
		result.put("nmFantasiaDestino", etiqueta.getFilialDestino().getPessoa().getNmFantasia());
		result.put("idRotaColetaEntrega", etiqueta.getRotaColetaEntrega().getIdRotaColetaEntrega());
		result.put("nrRota", etiqueta.getRotaColetaEntrega().getNrRota());
		result.put("dsRotaColeta", etiqueta.getRotaColetaEntrega().getDsRota());
		result.put("nrAltura", etiqueta.getNrAltura());
		result.put("nrLargura", etiqueta.getNrLargura());
		result.put("nrComprimento", etiqueta.getNrComprimento());
		result.put("nrPeso", etiqueta.getPsInformado());
		result.put("nrCodigoBarras", etiqueta.getNrCodigoBarras());

		return result;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria){
		ResultSetPage rsp = getService().findPaginatedEtiquetaAfericao(criteria);
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	public Serializable storeEtiqueta(TypedFlatMap criteria) {
		return super.store(montaEtiqueta(criteria));
	}

	private EtiquetaAfericao montaEtiqueta(TypedFlatMap criteria) {
		EtiquetaAfericao etiqueta = new EtiquetaAfericao();
		/**
		 * Os campos da tela CAD são todos obrigatórios
		 */
		etiqueta.setFilialOrigem(filialService.findById(criteria.getLong("idFilialOrigem")));
		etiqueta.setFilialDestino(filialService.findById(criteria.getLong("idFilialDestino")));
		etiqueta.setRotaColetaEntrega(rotaColetaEntregaService.findById(criteria.getLong("idRotaColetaEntrega")));
		etiqueta.setNrAltura(criteria.getLong(("nrAltura")));
		etiqueta.setNrLargura(criteria.getLong("nrLargura"));
		etiqueta.setNrComprimento(criteria.getLong("nrComprimento"));
		etiqueta.setPsInformado(new BigDecimal(criteria.getDouble("nrPeso")));
		etiqueta.setNrCodigoBarras(criteria.getString("nrCodigoBarras"));

		return etiqueta;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getService().getRowCountEtiquetaAfericao(criteria);
	}

	public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("cdFilial", filial.getCodFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}

	public List findLookupRotaColetaEntrega(Map criteria) {
		Map mapFilial = new HashMap();
		mapFilial.put("idFilial", criteria.get("idFilial"));
		criteria.put("filial", mapFilial);

		List list = rotaColetaEntregaService.findLookup(criteria);
		List retorno = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega)iter.next();
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
			typedFlatMap.put("nrRota", rotaColetaEntrega.getNrRota());
			typedFlatMap.put("dsRota", rotaColetaEntrega.getDsRota());
			retorno.add(typedFlatMap);
		}
		return retorno;
	}
	
	public TypedFlatMap findParametroGeral(Map criteria){
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("sequenceName", parametroGeralService.findSimpleConteudoByNomeParametro(criteria.get("sequenceName").toString()));
		return tfm;
	}

	public void removeById(Long id) {
		getService().removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setService(EtiquetaAfericaoService etiquetaAfericaoService) {
		this.defaultService = etiquetaAfericaoService;
	}

	public EtiquetaAfericaoService getService() {
		return (EtiquetaAfericaoService) this.defaultService;
	}

	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public RotaColetaEntregaService getRotaColetaEntregaService() {
		return rotaColetaEntregaService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}
