package com.mercurio.lms.vendas.model.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ClienteTerritorio;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.dao.TerritorioDAO;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class TerritorioService extends CrudService<Territorio, Long> {
	
	public static final String EXCEPTION_STORE_SEM_NM_TERRITORIO = "LMS-01275";
	public static final String EXCEPTION_STORE_SEM_REGIONAL = "LMS-01298"; 
	public static final String EXCEPTION_STORE_FILIAL_COM_REGIONAL_INVALIDA = "LMS-01299";
	public static final String EXCEPTION_STORE_NATURAL_KEY_CONFLICT = "LMS-01300";
	
	private ClienteTerritorioService clienteTerritorioService;
	private ExecutivoTerritorioService executivoTerritorioService;
	private RegionalService regionalService;
	
	@Override
	public Territorio findById(Long id) {
		return getTerritorioDao().findById(id);
	}
	
	public List<Territorio> find(Long idRegional, Long idFilial, String territorio, DmStatusEnum tpSituacao) {
		return getTerritorioDao().find(idRegional, idFilial, territorio, tpSituacao);
	}

	public Territorio findByIdInitLazyProperties(Long id) {
		Territorio territorio = getTerritorioDao().findByIdInitLazyProperties(id, true);
		
		return territorio;
	}
	
	public Integer findCount(Long idRegional, Long idFilial, String territorio, DmStatusEnum tpSituacao) {
		return getTerritorioDao().findCount(idRegional, idFilial, territorio, tpSituacao);
	}
	
	public Territorio findAtivoPorNome(String nmTerritorio) {
		return getTerritorioDao().findByNomeESituacao(nmTerritorio, DmStatusEnum.ATIVO.getDomainValue());
	}
	
	public Territorio findInativoPorNome(String nmTerritorio) {
		return getTerritorioDao().findByNomeESituacao(nmTerritorio, DmStatusEnum.INATIVO.getDomainValue());
	}
	
	@Override
	public void removeById(Long id) {
		getTerritorioDao().removeById(id);
		
		super.beforeRemoveById(id);

	}

	@Override
	public java.io.Serializable store(Territorio territorio) {
		Territorio territorioStore = createTerritoStore(territorio);
		validateStore(territorioStore);
		return callInheritedStore(territorioStore);
	}

	public void updateStatusInativo(Long id) {
		validateTerritorioEmUso(id);

		Territorio territorio = findById(id);
		territorio.setTpSituacao(DmStatusEnum.INATIVO.getDomainValue());
		callInheritedStore(territorio);
	}
	
	private java.io.Serializable callInheritedStore(Territorio territorioStore) {
		return super.store(territorioStore);
	}
	
	private void validateStore(Territorio territorio) {
		if (territorio.getRegional() == null || territorio.getRegional().getIdRegional() == null) {
			throw new BusinessException(EXCEPTION_STORE_SEM_REGIONAL);
		}
		
		if (territorio.getFilial() != null && territorio.getFilial().getIdFilial() != null) {
			Regional regionalDaFilial = regionalService.findRegionalAtivaByIdFilial(territorio.getFilial().getIdFilial());
			if (regionalDaFilial == null || !regionalDaFilial.equals(territorio.getRegional())) {
				throw new BusinessException(EXCEPTION_STORE_FILIAL_COM_REGIONAL_INVALIDA);
			}
		}
		
		if (StringUtils.isBlank(territorio.getNmTerritorio())) {
			throw new BusinessException(EXCEPTION_STORE_SEM_NM_TERRITORIO);
		}
		
		Territorio territorioAtivoMesmoNome = findAtivoPorNome(territorio.getNmTerritorio());
		if (territorioAtivoMesmoNome != null) {
			if (territorio.getIdTerritorio() == null
					|| !territorioAtivoMesmoNome.getIdTerritorio().equals(territorio.getIdTerritorio())) {
				throw new BusinessException(EXCEPTION_STORE_NATURAL_KEY_CONFLICT);
			}
		}
	}
	
	private Territorio createTerritoStore(Territorio territorio) {
		DateTime dhStore = JTDateTimeUtils.getDataHoraAtual();
		Territorio territorioStore = null;
		
		if (territorio.getIdTerritorio() != null) {
			territorioStore = findById(territorio.getIdTerritorio());
		} else {
			territorioStore = findInativoPorNome(territorio.getNmTerritorio());
			if (territorioStore == null) {
				territorioStore = new Territorio();
			}
			territorioStore.setUsuarioInclusao(territorio.getUsuarioAlteracao());
			territorioStore.setDhInclusao(dhStore);
		}

		territorioStore.setFilial(territorio.getFilial());
		territorioStore.setRegional(territorio.getRegional());
		territorioStore.setNmTerritorio(StringUtils.upperCase(territorio.getNmTerritorio()));

		territorioStore.setTpSituacao(DmStatusEnum.ATIVO.getDomainValue());
		territorioStore.setUsuarioAlteracao(territorio.getUsuarioAlteracao());
		territorioStore.setDhAlteracao(dhStore);
		
		return territorioStore;
	}

	@Override
	protected Territorio beforeUpdate(Territorio territorio) {
		if (territorio.getTpSituacao().getValue().equals("I")) {
			boolean vinculado = executivoTerritorioService.existsExecutivo(territorio.getIdTerritorio());
			if (vinculado) {
				throw new BusinessException("LMS-01261", new String[] { territorio.getNmTerritorio() });
			}
		}

		return territorio;
	}

	private void validateTerritorioEmUso(Long id) {
		List<ClienteTerritorio> clienteTerritorioList = clienteTerritorioService.find(null, id, null, null, null, DmStatusEnum.ATIVO);
		if (clienteTerritorioList.size() > 0) {
			throw new BusinessException("LMS-01276");
		}
	}

	public RegionalService getRegionalService() {
		return regionalService;
	}

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}
	
	public ClienteTerritorioService getClienteTerritorioService() {
		return clienteTerritorioService;
	}

	public void setClienteTerritorioService(ClienteTerritorioService clienteTerritorioService) {
		this.clienteTerritorioService = clienteTerritorioService;
	}
	
	public void setExecutivoTerritorioService(ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}

	public void setTerritorioDao(TerritorioDAO dao) {
		setDao(dao);
	}

	public TerritorioDAO getTerritorioDao() {
		return (TerritorioDAO) getDao();
	}

}
