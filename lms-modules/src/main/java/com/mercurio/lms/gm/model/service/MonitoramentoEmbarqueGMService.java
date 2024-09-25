package com.mercurio.lms.gm.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.gm.model.dao.MonitoramentoEmbarqueGMDAO;
import com.mercurio.lms.util.JTDateTimeUtils;


/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.gm.monitoramentoEmbarqueGMService"
 */
public class MonitoramentoEmbarqueGMService extends CrudService{
	private MonitoramentoEmbarqueGMDAO monitoramentoEmbarqueGMDao;
	private MeioTransporteService meioTransporteService;
	private UsuarioLMSService usuarioLMSService;
	
	public MonitoramentoEmbarqueGMDAO getMonitoramentoEmbarqueGMDao() {
		return monitoramentoEmbarqueGMDao;
	}

	public void setMonitoramentoEmbarqueGMDao(MonitoramentoEmbarqueGMDAO dao) {
		setDao( dao );
	}
	
	
	public Map findPageLoad() {

		Map map = new HashMap();
		
		map.put("dataDisponibilizada", JTDateTimeUtils.getDataAtual());
		
		return map;
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		return ((MonitoramentoEmbarqueGMDAO) this.getDao()).getRowCount(criteria);
	}
	
	public List findLookupUsuarioFuncionarioMonitoramento(TypedFlatMap tfm) {
		ConsultarUsuarioLMSParam cup = new ConsultarUsuarioLMSParam();

		cup.setNrMatricula(tfm.getString("nrMatricula"));

        return usuarioLMSService.findLookupSistemaMonitoramento(cup);
	}
	
	public List findLookupMeioTransporte(Map map) {
    	return this.getMeioTransporteService().findLookup(map);
    }

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
	
	private MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {

		ResultSetPage rsp = ((MonitoramentoEmbarqueGMDAO) this.getDao()).findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List<Object[]> listFromResult = rsp.getList();
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listFromResult.size());

		for (final Object[] objResult : listFromResult) {
			TypedFlatMap mapResult = mountMapForListTab(objResult);
			listRetorno.add(mapResult);
		}
		
		rsp.setList(listRetorno);
		
		List<Map> lista = rsp.getList();
		if(lista != null && lista.size() > 0){
			for(Map m : lista){		
				if("D".equals(m.get("tipoCarregamento"))){
					m.put("tipoCarregamento", "Direto");
				}else{
					m.put("tipoCarregamento", "Normal");
				}
			}
		}
		return rsp;	
	}
	
	
	private TypedFlatMap mountMapForListTab(Object[] objResult) {
		TypedFlatMap mapResult = new TypedFlatMap(); 
		mapResult.put("idCarregamento", objResult[0]);
		mapResult.put("frotaVeiculo", objResult[1]);
		mapResult.put("placaVeiculo", objResult[2]);
		mapResult.put("totalVolume", objResult[3] == null ? 0 : objResult[3]);
		mapResult.put("totalPeso", objResult[4] == null ? 0 : objResult[4]);
		mapResult.put("rotaCarregamento", objResult[5]);
		mapResult.put("tipoCarregamento", objResult[6]);
		mapResult.put("codigoStatus", objResult[7]);
		mapResult.put("dtInicio", objResult[8]);
		mapResult.put("dtFim", objResult[9]);
		mapResult.put("qtdVolumes", objResult[10]);
		mapResult.put("responsavel", objResult[11]);
		mapResult.put("horarioCorte", objResult[12]);
		
		return mapResult;
	}
	
	
	/**
	 * Método que retorna o ResultSetPage da grid da aba de detalhamento da tela
	 * de Monitoramento Embarque GM Sorocaba LMS-2781
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedDetalheEmbarqueGM(Map criteria) {

		ResultSetPage rsp = ((MonitoramentoEmbarqueGMDAO) this.getDao()).findPaginatedDetalheEmbarqueGM(criteria, FindDefinition.createFindDefinition(criteria));
		List<Object[]> listFromResult = rsp.getList();
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listFromResult.size());

		for (final Object[] objResult : listFromResult) {
			TypedFlatMap mapResult = mountMapForCadTab(objResult);
			listRetorno.add(mapResult);
		}
		rsp.setList(listRetorno);
		return rsp;
	}
	
	private TypedFlatMap mountMapForCadTab(Object[] objResult) {
		TypedFlatMap mapResult = new TypedFlatMap();
		mapResult.put("mapaCarregamento", objResult[0]);
		mapResult.put("dataCriacao", objResult[1]);
		mapResult.put("dataDisponivel", objResult[2]);
		mapResult.put("totalVolume", objResult[3]);
		mapResult.put("totalPeso", objResult[4]);
		mapResult.put("qtdVolumes", objResult[5]);
		
		return mapResult;
	}

	/**
	 * Método que retorna o count da grid da aba de detalhamento  para a tela de Monitoramento Embarque GM Sorocaba
	 * LMS-2781
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountMpc(Map criteria) {
		return ((MonitoramentoEmbarqueGMDAO) this.getDao()).getRowCountMpc(criteria);
	}
	
}
