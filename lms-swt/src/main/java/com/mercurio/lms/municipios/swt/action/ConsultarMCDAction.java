package com.mercurio.lms.municipios.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.param.MCDParam;
import com.mercurio.lms.municipios.model.service.ConsultarMCDService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.swt.consultarMCDAction"
 */
public class ConsultarMCDAction extends CrudAction {
	private ConsultarMCDService consultarMCDService;
	private FilialService filialService;
	private ServicoService servicoService;
	private MunicipioFilialService municipioFilialService;
	
	/**
	 * Find Paginated da tela de consulta de MCD.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedCustom(Map criteria) {
		FindDefinition fDef = FindDefinition.createFindDefinition(criteria);
		ResultSetPage rsp = consultarMCDService.findPaginatedCustom(configureMCDParam(criteria),fDef.getCurrentPage(),fDef.getPageSize());
		if (rsp.getList() != null) {
			List<TypedFlatMap> mcds = rsp.getList();
			List<Map> result = new ArrayList<Map>();
			for (TypedFlatMap mcd : mcds) {
				Map<String, Object> mapMcd = new HashMap<String, Object>();
				mapMcd.put("dsServico", mcd.get("servico_dsServico"));
				mapMcd.put("qtPedagio", mcd.get("qtPedagio"));
				mapMcd.put("cdTarifa", mcd.get("tarifa_cdTarifa"));
				mapMcd.put("cdTarifaAtual", mcd.get("tarifa_cdTarifa_atual"));
				mapMcd.put("nrPpe", mcd.get("nrPpe"));
				
				mapMcd.put("blDomingoOrigem", mcd.get("blDomingoOrigem"));
				mapMcd.put("blSegundaOrigem", mcd.get("blSegundaOrigem"));
				mapMcd.put("blTercaOrigem", mcd.get("blTercaOrigem"));
				mapMcd.put("blQuartaOrigem", mcd.get("blQuartaOrigem"));
				mapMcd.put("blQuintaOrigem", mcd.get("blQuintaOrigem"));
				mapMcd.put("blSextaOrigem", mcd.get("blSextaOrigem"));
				mapMcd.put("blSabadoOrigem", mcd.get("blSabadoOrigem"));
				mapMcd.put("dsFilialOrigem", mcd.get("sgFilialOrigem") + " - " + mcd.get("nmFilialOrigem"));
				mapMcd.put("nmMunicipioOrigem", mcd.get("municipioFilialByIdMunicipioFilialOrigem_nmMunicipio"));
				mapMcd.put("blDistritoOrigem", mcd.get("blDistritoOrigem"));
				
				mapMcd.put("blDomingoDestino", mcd.get("blDomingoDestino"));
				mapMcd.put("blSegundaDestino", mcd.get("blSegundaDestino"));
				mapMcd.put("blTercaDestino", mcd.get("blTercaDestino"));
				mapMcd.put("blQuartaDestino", mcd.get("blQuartaDestino"));
				mapMcd.put("blQuintaDestino", mcd.get("blQuintaDestino"));
				mapMcd.put("blSextaDestino", mcd.get("blSextaDestino"));
				mapMcd.put("blSabadoDestino", mcd.get("blSabadoDestino"));
				mapMcd.put("dsFilialDestino", mcd.get("sgFilialDestino") + " - " + mcd.get("nmFilialDestino"));
				mapMcd.put("nmMunicipioDestino", mcd.get("municipioFilialByIdMunicipioFilialDestino_nmMunicipio"));
				mapMcd.put("blDistritoDestino", mcd.get("blDistritoDestino"));
				result.add(mapMcd);
			}
			rsp.setList(result);
		}
		return rsp;
    }
	
	/**
	 * Retorna n�mero de registros da consulta de MCD.
	 * @param criteria
	 * @return
	 */
    public Integer getRowCountCustom(Map criteria) {
    	return consultarMCDService.getRowCountCustom(configureMCDParam(criteria));
    }
    
    private MCDParam configureMCDParam(Map values) {
    	MCDParam dados = new MCDParam();
		dados.setIdMunicipioOrigem((Long) values.get("idMunicipioOrigem"));
		dados.setIdFilialOrigem((Long) values.get("idFilialOrigem"));
		dados.setIdMunicipioDestino((Long) values.get("idMunicipioDestino"));
		dados.setIdFilialDestino((Long) values.get("idFilialDestino"));
		dados.setIdServico((Long) values.get("idServico"));
		dados.setTpEmissao((String) values.get("tpEmissao"));
		dados.setDtVigencia(JTDateTimeUtils.getDataAtual());
		
		return dados;
    }

    public List findLookupFilial(Map criteria) {
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
			return result;
		}
		return null;
	}
	
	public List findComboServico(Map criteria) {
		List<Servico> servicos = servicoService.find(criteria);
		if (servicos != null) {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			for (Servico servico : servicos) {
				Map<String, Object> mapServico = new HashMap<String, Object>();
				mapServico.put("idServico", servico.getIdServico());
				mapServico.put("dsServico", servico.getDsServico().getValue());
				result.add(mapServico);
			}
			return result;
		}
		return null;
	}
	
	public List findLookupMunicipio(Map criteria) {
		Map<String, Object> municipio = new HashMap<String, Object>();
		municipio.put("nmMunicipio", criteria.remove("nmMunicipio"));
		criteria.put("municipio", municipio);
		Map<String, Object> filial = new HashMap<String, Object>();
		filial.put("idFilial", criteria.remove("idFilial"));
		criteria.put("filial", filial);
		List<MunicipioFilial> municipiosFiliais = municipioFilialService.findLookup(criteria);
		if (municipiosFiliais != null) {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			for (MunicipioFilial municipioFilial : municipiosFiliais) {
				Map<String, Object> mapMunicipioFilial =  new HashMap<String, Object>();
				mapMunicipioFilial.put("idMunicipio", municipioFilial.getMunicipio().getIdMunicipio());
				mapMunicipioFilial.put("nmMunicipio", municipioFilial.getMunicipio().getNmMunicipio());
				if (BooleanUtils.toBoolean(municipioFilial.getMunicipio().getBlDistrito())) {
					mapMunicipioFilial.put("blDistrito", "S");
				} else {
					mapMunicipioFilial.put("blDistrito", "");
				}
				mapMunicipioFilial.put("idFilial", municipioFilial.getFilial().getIdFilial());
				mapMunicipioFilial.put("sgFilial", municipioFilial.getFilial().getSgFilial());
				mapMunicipioFilial.put("nmFantasia", municipioFilial.getFilial().getPessoa().getNmFantasia());
				mapMunicipioFilial.put("sgUnidadeFederativa", municipioFilial.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
				mapMunicipioFilial.put("idUnidadeFederativa", municipioFilial.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
				mapMunicipioFilial.put("nmUnidadeFederativa", municipioFilial.getMunicipio().getUnidadeFederativa().getNmUnidadeFederativa());
				mapMunicipioFilial.put("nmPais", municipioFilial.getMunicipio().getUnidadeFederativa().getPais().getNmPais().getValue());
				mapMunicipioFilial.put("idPais", municipioFilial.getMunicipio().getUnidadeFederativa().getPais().getIdPais());
				result.add(mapMunicipioFilial);
			}
			return result;
		}
		return null;
	}
	
	/*
	 * GETTERS E SETTERS
	 */

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	public void setConsultarMCDService(ConsultarMCDService consultarMCDService) {
		this.consultarMCDService = consultarMCDService;
	}
}
