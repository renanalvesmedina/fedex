package com.mercurio.lms.vol.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vol.model.VolEquipamentos;
import com.mercurio.lms.vol.model.VolLogEnviosSms;
import com.mercurio.lms.vol.model.service.VolEquipamentosService;
import com.mercurio.lms.vol.model.service.VolGruposFrotasService;
import com.mercurio.lms.vol.model.service.VolLogEnviosSmsService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vol.envioSMSAction"
 */

@Deprecated
/**
 * funcionalidade n�o � mais utilizada
 */
public class EnvioSMSAction extends CrudAction {
	
	private VolEquipamentosService volEquipamentosService;
	private VolGruposFrotasService volGruposFrotasService;
	private MeioTransporteService meioTransporteService;
	private FilialService filialService;
	
	public void setService(VolLogEnviosSmsService serviceService) {
		this.defaultService = serviceService;
	}
	
	public VolLogEnviosSmsService getVolLogEnviosSmsService() {
		return (VolLogEnviosSmsService)super.defaultService;
	}
	
	public void removeById(java.lang.Long id) {
		getVolLogEnviosSmsService().removeById(id);
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getVolLogEnviosSmsService().removeByIds(ids);
	}
	
	public List findByIdEquipamento(java.lang.Long id) {
		return getVolLogEnviosSmsService().findByIdEquipamento(id);
	}
	
	public ResultSetPage findPaginatedMeioTransporte(TypedFlatMap criteria) {
		ResultSetPage rsp = getVolLogEnviosSmsService().findPaginatedMeioTransporte(criteria); 
		return rsp;
	}
	               
	public Integer getRowCountMeioTransporte(TypedFlatMap criteria) {
		return getVolLogEnviosSmsService().getRowCountMeioTransporte(criteria);
	}

	public void executeEnvioSms(TypedFlatMap tela) {

		List idEquipamentosList = tela.getList("idEquipamentoList");
		TypedFlatMap criteria = (TypedFlatMap)tela.getList("filtroTela").get(0);
		String tipo = tela.getString("tipo");
		
		if (StringUtils.isBlank(criteria.getString("mensagem"))) {
			throw new BusinessException("LMS-41009");
		}
		if (StringUtils.isBlank(criteria.getString("tpEvento"))) {
			throw new BusinessException("LMS-41017");
		}
		
		/* Click em 'Transmitir Todos' na grid*/
		if(tipo.equalsIgnoreCase("all")){
			idEquipamentosList = getVolLogEnviosSmsService().findMeioTransporte(criteria);
		} 
		for (Iterator iter = idEquipamentosList.iterator();iter.hasNext();) {			
			Long idEquipamento;
			if(tipo.compareTo("all") == 0){
				idEquipamento = (Long)iter.next();
			}else{
				idEquipamento = Long.valueOf(iter.next().toString());
			}
			
			VolEquipamentos volEquipamentos = getVolEquipamentosService().findById(idEquipamento);

			getVolLogEnviosSmsService().executeEnvioSms(volEquipamentos.getVolOperadorasTelefonia().getIdOperadora(),volEquipamentos.getDsNumero(),criteria.getString("mensagem"));
			
			VolLogEnviosSms volLogEnviosSms  = new VolLogEnviosSms(); 
			volLogEnviosSms.setTpEvento(criteria.getDomainValue("tpEvento"));
			volLogEnviosSms.setDhEnvio(JTDateTimeUtils.getDataHoraAtual());
			volLogEnviosSms.setVolEquipamento(volEquipamentos);
			volLogEnviosSms.setObMensagem(criteria.getString("mensagem"));
			
			getVolLogEnviosSmsService().store(volLogEnviosSms);
		}
	}
    
	public VolEquipamentosService getVolEquipamentosService() {
		return volEquipamentosService;
	}

	public void setVolEquipamentosService(
			VolEquipamentosService volEquipamentosService) {
		this.volEquipamentosService = volEquipamentosService;
	}


	/**
	 * Busca o usuario da sessao e retorna para a tela
	 * 
	 * @return
	 */
	public Map findFilialUsuarioLogado() {

		Filial filial = SessionUtils.getFilialSessao();

		Map mapFilial = new HashMap();
		mapFilial.put("idFilial", filial.getIdFilial());
		mapFilial.put("sgFilial", filial.getSgFilial());

		Map mapPessoa = new HashMap();
		mapPessoa.put("nmFantasia", filial.getPessoa().getNmFantasia());
		mapFilial.put("pessoa", mapPessoa);

		Map mapSessionObjects = new HashMap();
		mapSessionObjects.put("filial", mapFilial);

		return mapSessionObjects;
	}

	public List findLookupMeioTransporte(Map criteria) {
		List result = getMeioTransporteService().findLookup(criteria);
		return result;
	}

		
	public List findLookupFilialByUsuarioLogado(TypedFlatMap map) {
		List listFilial = this.filialService.findLookupByUsuarioLogado(map);
		
		if ( listFilial.isEmpty() ) {
			return listFilial;
		}
		
		List resultList = new ArrayList();
		
		Iterator iterator = listFilial.iterator();
		Map filial = (Map)iterator.next();
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		typedFlatMap.put("idFilial", filial.get("idFilial") );
		typedFlatMap.put("pessoa.nmFantasia", filial.get("pessoa.nmFantasia") );
		typedFlatMap.put("sgFilial", filial.get("sgFilial") );
		resultList.add(typedFlatMap);
		
		return resultList;
	}
	
	
	public List findLookupGruposFrotas(TypedFlatMap criteria) {
		List result = getVolGruposFrotasService().findGruposFrotaByUsuario(criteria);
			
		List resultList = new ArrayList();
		
		for (java.util.Iterator iter = result.iterator();iter.hasNext();) {
			TypedFlatMap resultMap = new TypedFlatMap();
			Map linha = (Map)iter.next();
			resultMap.put("idGrupoFrota", linha.get("idGrupoFrota"));
			resultMap.put("dsNome", linha.get("dsNome"));
			resultMap.put("filial.idFilial", linha.get("idFilial"));
			resultMap.put("filial.sgFilial", linha.get("sgFilial"));
			resultMap.put("filial.pessoa.nmFantasia", linha.get("nmFantasia"));
			resultList.add(resultMap);
			
		}
		
		return resultList;
	}


	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public VolGruposFrotasService getVolGruposFrotasService() {
		return volGruposFrotasService;
	}

	public void setVolGruposFrotasService(
			VolGruposFrotasService volGruposFrotasService) {
		this.volGruposFrotasService = volGruposFrotasService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public ResultSetPage findPaginatedHistorico(TypedFlatMap criteria) {
		ResultSetPage rsp = getVolLogEnviosSmsService().findPaginatedHistorico(criteria); 
		return rsp;
	}
	               
	public Integer getRowCountHistorico(TypedFlatMap criteria) {
		return getVolLogEnviosSmsService().getRowCountHistorico(criteria);
	}

}
