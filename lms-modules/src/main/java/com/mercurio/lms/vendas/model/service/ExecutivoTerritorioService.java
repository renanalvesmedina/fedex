package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ExecutivoTerritorio;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.dao.ExecutivoTerritorioDAO;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class ExecutivoTerritorioService extends CrudService<ExecutivoTerritorio, Long> {

	public static final String EXCEPTION_CARGO_DUPLICADO = "LMS-01304"; 
	public static final String EXCEPTION_FUNCIONARIO_DUPLICADO = "LMS-01305";
	public static final String EXCEPTION_STORE_SEM_TERRITORIO = "LMS-01306"; 
	public static final String EXCEPTION_STORE_SEM_USUARIO = "LMS-01307";
	public static final String EXCEPTION_STORE_SEM_CARGO = "LMS-01308";
	public static final String EXCEPTION_STORE_SEM_VIGENCIA_INICIAL = "LMS-01309"; 
	public static final String EXCEPTION_STORE_VIGENCIA_FINAL_ANTERIOR_VIGENCIA_INICIAL = "LMS-01310"; 
	public static final String EXCEPTION_STORE_CONFLITO_VIGENCIAS = "LMS-01311";
	
	public static final String DOMINIO_TP_EXECUTIVO = "DM_TIPO_EXECUTIVO";
	private static final String VALOR_DOMINIO_EXECUTIVO_SAM = "S";

	private UsuarioLMSService usuarioService;
	private TerritorioService territorioService;
	private DomainValueService domainValueService;

	public void storeEquipeVendas(List<ExecutivoTerritorio> equipeVendas) {
		validateCargoDuplicado(equipeVendas);
		validateUsuarioDuplicado(equipeVendas);
		storeAll(equipeVendas);
	}
	
	@Override
	public void storeAll(List<ExecutivoTerritorio> listaExecutivoTerritorio) {
		for (ExecutivoTerritorio executivoTerritorio : listaExecutivoTerritorio) {
			store(executivoTerritorio);
		}
	}
	
	@Override
	public Serializable store(ExecutivoTerritorio executivoTerritorio) {
		ExecutivoTerritorio executivoTerritorioStore = createStoreEntity(executivoTerritorio);
		validateStoreEntity(executivoTerritorioStore);
		return callInheritedStore(executivoTerritorioStore);
	}
	
	/**
	 * Wrapping the inherited method calling for mocking purposes.
	 */
	private java.io.Serializable callInheritedStore(ExecutivoTerritorio executivoTerritorio) {
		return super.store(executivoTerritorio);
	}
	
	private ExecutivoTerritorio createStoreEntity(ExecutivoTerritorio executivoTerritorio) {
		DateTime dhStore = JTDateTimeUtils.getDataHoraAtual();
		ExecutivoTerritorio executivoTerritorioStore = null;
		
		if (executivoTerritorio.getIdExecutivoTerritorio() != null) {
			executivoTerritorioStore = findById(executivoTerritorio.getIdExecutivoTerritorio());
		} else {
			executivoTerritorioStore = new ExecutivoTerritorio();
			executivoTerritorioStore.setUsuarioInclusao(executivoTerritorio.getUsuarioAlteracao());
			executivoTerritorioStore.setDhInclusao(dhStore);
		}

		executivoTerritorioStore.setTerritorio(executivoTerritorio.getTerritorio());
		executivoTerritorioStore.setTpExecutivo(executivoTerritorio.getTpExecutivo());
		executivoTerritorioStore.setUsuario(executivoTerritorio.getUsuario());
		executivoTerritorioStore.setDtVigenciaInicial(executivoTerritorio.getDtVigenciaInicial());
		executivoTerritorioStore.setDtVigenciaFinal(executivoTerritorio.getDtVigenciaFinal());
		executivoTerritorioStore.setTpSituacao(DmStatusEnum.ATIVO.getDomainValue());
		executivoTerritorioStore.setUsuarioAlteracao(executivoTerritorio.getUsuarioAlteracao());
		executivoTerritorioStore.setDhAlteracao(dhStore);
		
		return executivoTerritorioStore;
	}
	
	private void validateStoreEntity(ExecutivoTerritorio executivoTerritorio) {
		if (executivoTerritorio.getTerritorio() == null) {
			throw new BusinessException(EXCEPTION_STORE_SEM_TERRITORIO);
		}
		
		if (executivoTerritorio.getUsuario() == null) {
			throw new BusinessException(EXCEPTION_STORE_SEM_USUARIO);
		}
		
		if (executivoTerritorio.getTpExecutivo() == null) {
			throw new BusinessException(EXCEPTION_STORE_SEM_CARGO);
		}
		
		if (executivoTerritorio.getDtVigenciaFinal() != null 
				&& executivoTerritorio.getDtVigenciaFinal().isBefore(executivoTerritorio.getDtVigenciaInicial())) {
			throw new BusinessException(EXCEPTION_STORE_VIGENCIA_FINAL_ANTERIOR_VIGENCIA_INICIAL);
		}
		
		Long idTerritorio = executivoTerritorio.getTerritorio().getIdTerritorio();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTerritorio", idTerritorio);
		params.put("tpExecutivo", executivoTerritorio.getTpExecutivo());
		List<ExecutivoTerritorio> listaMesmoCargoETerritorio = find(params);
		for (ExecutivoTerritorio executivoTerritorioBanco : listaMesmoCargoETerritorio) {
			if ((executivoTerritorio.getIdExecutivoTerritorio() == null) || !executivoTerritorio.equals(executivoTerritorioBanco)) {
				if (possuiConflitoEntreVigencias(executivoTerritorio, executivoTerritorioBanco)) {
					throw new BusinessException(EXCEPTION_STORE_CONFLITO_VIGENCIAS);
				}
			}
		}
	}
	
	private boolean possuiConflitoEntreVigencias(ExecutivoTerritorio executivoTerritorio1, ExecutivoTerritorio executivoTerritorio2) {
		ExecutivoTerritorio vigenciaMaisAntiga = null;
		ExecutivoTerritorio vigenciaMaisNova = null;
		
		if (executivoTerritorio1.getDtVigenciaInicial().isBefore(executivoTerritorio2.getDtVigenciaInicial())) {
			vigenciaMaisAntiga = executivoTerritorio1;
			vigenciaMaisNova = executivoTerritorio2;
		} else {
			vigenciaMaisAntiga = executivoTerritorio2;
			vigenciaMaisNova = executivoTerritorio1;
		}
		
		return vigenciaMaisAntiga.getDtVigenciaFinal() == null
				|| vigenciaMaisAntiga.getDtVigenciaFinal().isAfter(vigenciaMaisNova.getDtVigenciaInicial())
				|| vigenciaMaisAntiga.getDtVigenciaFinal().isEqual(vigenciaMaisNova.getDtVigenciaInicial());
	}

	private void validateCargoDuplicado(List<ExecutivoTerritorio> listaExecutivoTerritorio) {
		List<DomainValue> listaCargos = new ArrayList<DomainValue>();
		for (ExecutivoTerritorio executivoTerritorio : listaExecutivoTerritorio) {
			DomainValue cargo = executivoTerritorio.getTpExecutivo();
			if (listaCargos.contains(cargo)) {
				throw new BusinessException(EXCEPTION_CARGO_DUPLICADO);
			}
			listaCargos.add(cargo);
		}
	}
	
	private void validateUsuarioDuplicado(List<ExecutivoTerritorio> listaExecutivoTerritorio) {
		List<Long> listaIdFuncionario = new ArrayList<Long>();
		for (ExecutivoTerritorio executivoTerritorio : listaExecutivoTerritorio) {
			Long idUsuario = executivoTerritorio.getUsuario().getIdUsuario();
			if (listaIdFuncionario.contains(idUsuario)) {
				throw new BusinessException(EXCEPTION_FUNCIONARIO_DUPLICADO);
			}
			listaIdFuncionario.add(idUsuario);
		}
	}
	
	public List<ExecutivoTerritorio> findComVigenciaAtiva(Long idUsuario, Long idTerritorio, DomainValue tpExecutivo) {
		return getExecutivoTerritorioDao().findComVigenciaAtiva(idUsuario, idTerritorio, tpExecutivo);
	}

	public List<ExecutivoTerritorio> findEquipeVendas(Long idTerritorio) {
		return getExecutivoTerritorioDao().findEquipeVendas(idTerritorio);
	}

	public boolean findTerritoriosAlreadyContainsExecutivo(Long idTerritorio, Long idExecutivo) {
		return getExecutivoTerritorioDao().findTerritoriosAlreadyContainsExecutivo(idTerritorio, idExecutivo);
	}
	
	public boolean existsExecutivoSAM(Long idTerritorio) {
		DomainValue tpExecutivoSAM = domainValueService.findDomainValueByValue(DOMINIO_TP_EXECUTIVO, VALOR_DOMINIO_EXECUTIVO_SAM);
		return getExecutivoTerritorioDao().existsExecutivo(idTerritorio, tpExecutivoSAM);
	}
	
	public boolean existsExecutivo(Long idTerritorio) {
		return getExecutivoTerritorioDao().existsExecutivo(idTerritorio, null);
	}

	@Override
	public List<ExecutivoTerritorio> find(Map map) {
		return getExecutivoTerritorioDao().find(convertToEntity(map));
	}

	
	public ExecutivoTerritorio findByIdUsuario(Long idUsuario) {
		return getExecutivoTerritorioDao().findByIdUsuario(idUsuario);
	}
	
	@Override
	public void removeById(Long id) {
		getExecutivoTerritorioDao().removeById(id);
	}

	@Override
	public ExecutivoTerritorio findById(Long id) {
		return getExecutivoTerritorioDao().findById(id);
	}

	
	public String findCargoCpfByIdExecutivo(Long idExecutivo) {
		return getExecutivoTerritorioDao().findCargoCpfByIdExecutivo(idExecutivo);
	}
	
	
	public ExecutivoTerritorio convertToEntity(Map<String, Object> map) {
		Long idExecutivoTerritorio = (Long) map.get("idExecutivoTerritorio");
		YearMonthDay dtVigenciaInicial = (YearMonthDay) map.get("vigenciaInicial");
		YearMonthDay dtVigenciaFinal = (YearMonthDay) map.get("vigenciaFinal");
		DomainValue tpExecutivo = map.get("tpExecutivo") == null ? null : new DomainValue(map.get("tpExecutivo").toString()); 
		Territorio territorio = getTerritorio((Long) map.get("idTerritorio"));
		UsuarioLMS usuario = getFuncionario((Long) map.get("idFuncionario"));

		return new ExecutivoTerritorio(idExecutivoTerritorio, territorio, usuario, null, null, 
				tpExecutivo, dtVigenciaInicial, dtVigenciaFinal, null, null);
	}

	private Territorio getTerritorio(Long idTerritorio) {
		if (idTerritorio != null) {
			return territorioService.findById(idTerritorio);
		}
		return null;
	}

	public String getLocalizacaoFromExecutivo(String cpfExecutivo) {
		return getExecutivoTerritorioDao().getLocalizacaoFromExecutivo(cpfExecutivo);
	}
	
	private UsuarioLMS getFuncionario(Long idFuncionario) {
		if (idFuncionario != null) {
			return usuarioService.findById(idFuncionario);
		}
		return null;
	}

	public TerritorioService getTerritorioService() {
		return territorioService;
	}

	public void setTerritorioService(TerritorioService territorioService) {
		this.territorioService = territorioService;
	}

	public UsuarioLMSService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioLMSService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setExecutivoDao(ExecutivoTerritorioDAO executivoTerritorioDAO) {
		setDao(executivoTerritorioDAO);
	}

	public ExecutivoTerritorioDAO getExecutivoTerritorioDao() {
		return (ExecutivoTerritorioDAO) getDao();
	}

	public Integer findCount(Map<String, Object> map) {
		return getExecutivoTerritorioDao().findCount(convertToEntity(map));
	}

}
