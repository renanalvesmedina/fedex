package com.mercurio.lms.gm.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.gm.model.dao.MonitoramentoEmbarqueDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.gm.monitoramentoEmbarqueService"
 */
public class MonitoramentoEmbarqueService extends CrudService{
	private MonitoramentoEmbarqueDAO monitoramentoEmbarqueDao;
	private MeioTransporteService meioTransporteService;
	private UsuarioLMSService usuarioLMSService;
	
	public MonitoramentoEmbarqueDAO getMonitoramentoEmbarqueDao() {
		return monitoramentoEmbarqueDao;
	}

	public void setMonitoramentoEmbarqueDao(MonitoramentoEmbarqueDAO dao) {
		setDao( dao );
	}
	
	
	public Map findPageLoad() {

		Map map = new HashMap();
		
		map.put("dataDisponibilizada", JTDateTimeUtils.getDataAtual());
		
		return map;
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		ResultSetPage result = findPaginated(criteria);
		return  result.getRowCount().intValue();
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
		ResultSetPage r = super.findPaginated(criteria);
		List<Map> lista = r.getList();
		if(lista != null && lista.size() > 0){
			for(Map m : lista){
				if("N".equals(m.get("tipoCarregamento"))){
					m.put("tipoCarregamento", "Normal");
				}else if("D".equals(m.get("tipoCarregamento"))){
					m.put("tipoCarregamento", "Direto");
				}
			}
		}
		return r;
	}

}
