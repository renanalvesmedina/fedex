package com.mercurio.lms.vendas.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.ValidateUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.CargoComissao;
import com.mercurio.lms.vendas.model.dao.CargoComissaoDAO;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class CargoComissaoService extends CrudService<CargoComissao, Long> {
	
	private static final String DOMINIO_TP_SITUACAO = "DM_STATUS";
	
	private static final String EXCEPTION_STORE_SEM_USUARIO = "LMS-01285";
	private static final String EXCEPTION_STORE_SEM_CPF = "LMS-01286";
	private static final String EXCEPTION_STORE_CPF_INVALIDO = "LMS-01287";
	private static final String EXCEPTION_STORE_CPF_DUPLICADO = "LMS-01282";
	private static final String EXCEPTION_STORE_SEM_CARGO = "LMS-01288";
	private static final String EXCEPTION_STORE_USUARIO_SEM_FUNCIONARIO = "LMS-01289";
	private static final String EXCEPTION_UPDATE_CG_ALTERACAO_USUARIO = "LMS-01290";
	private static final String EXCEPTION_CREATE_CG_USUARIO_REPETIDO = "LMS-01291";
	
	private UsuarioLMSService usuarioLMSService;
	private FuncionarioService funcionarioService;
	private DomainValueService domainValueService;
	
	public List<Map<String, Object>> findComissionadoSuggest(String nrIdentificacao, String nmPessoa, String tpPessoa, List<String> notINrIdentificacao) {
		return getCargoComissaoDao().findComissionadoSuggest(nrIdentificacao, nmPessoa, tpPessoa, notINrIdentificacao);
	}
	
	@Override
	protected CargoComissao beforeStore(CargoComissao cargoComissao) {
		if (cargoComissao.getUsuario() == null || cargoComissao.getUsuario().getIdUsuario() == null) {
			throw new BusinessException(EXCEPTION_STORE_SEM_USUARIO);
		}
		
		if (StringUtils.isBlank(cargoComissao.getNrCpf())) {
			throw new BusinessException(EXCEPTION_STORE_SEM_CPF);
		}
		
		if (!ValidateUtils.validateCpfOrCnpj(cargoComissao.getNrCpf())) {
			throw new BusinessException(EXCEPTION_STORE_CPF_INVALIDO);
		}
		
		if (cargoComissao.getTpCargo() == null) {
			throw new BusinessException(EXCEPTION_STORE_SEM_CARGO);
		}

		if(null == cargoComissao.getIdCargoComissao()) {
			Boolean duplicado = getCargoComissaoDao().findCargoComissaoByCpf(cargoComissao.getNrCpf());
			if (duplicado) {
				throw new BusinessException(EXCEPTION_STORE_CPF_DUPLICADO);
			}
		}
		
		return super.beforeStore(cargoComissao);
	}

	@Override
	public java.io.Serializable store(CargoComissao cargoComissao) {
		Long idUsuario = cargoComissao.getUsuario().getIdUsuario();
		UsuarioLMS usuarioLMSCargoComissao = usuarioLMSService.findById(idUsuario);
		validateUsuario(usuarioLMSCargoComissao);
		
		UsuarioLMS usuarioLMSLogado = getUsuarioLogado();
		
		DateTime dhStore = JTDateTimeUtils.getDataHoraAtual();
		CargoComissao cargoComissaoStore = new CargoComissao();

		CargoComissao cargoComissaoDoUsuario = findById(cargoComissao.getIdCargoComissao());
		if (cargoComissao.getIdCargoComissao() != null) {
			if (cargoComissaoDoUsuario == null || (!cargoComissaoDoUsuario.getIdCargoComissao().equals(cargoComissao.getIdCargoComissao()) && cargoComissaoDoUsuario.getTpSituacao().getValue().equals("A"))) {
				throw new BusinessException(EXCEPTION_UPDATE_CG_ALTERACAO_USUARIO);
			}

			cargoComissaoStore = findById(cargoComissao.getIdCargoComissao());
		} else {
			if (cargoComissaoDoUsuario != null) {
				throw new BusinessException(EXCEPTION_CREATE_CG_USUARIO_REPETIDO);
			}
			
			cargoComissaoStore.setUsuarioInclusao(usuarioLMSLogado);
			cargoComissaoStore.setDhInclusao(dhStore);
		}
		
		cargoComissaoStore.setUsuario(usuarioLMSCargoComissao);
		cargoComissaoStore.setTpCargo(cargoComissao.getTpCargo());
		cargoComissaoStore.setNrCpf(cargoComissao.getNrCpf());
		cargoComissaoStore.setDtDesligamento(cargoComissao.getDtDesligamento());
		
		cargoComissaoStore.setTpSituacao(getDomainValue(DmStatusEnum.ATIVO));
		cargoComissaoStore.setUsuarioAlteracao(usuarioLMSLogado);
		cargoComissaoStore.setDhAlteracao(dhStore);
		
		return super.store(cargoComissaoStore);
	}
	
	@Override
	public CargoComissao findById(Long id) {
		return getCargoComissaoDao().findById(id);
	}
	
	public List<Map<String, Object>> findToMappedResult(Map<String, Object> criteria){
		return getCargoComissaoDao().findToMappedResult(criteria);
	}

	public Integer findCount(Map<String, Object> criteria){
		return getCargoComissaoDao().findCount(criteria);
	}
	
	@Override
	public void removeById(Long id) {
		getCargoComissaoDao().removeById(id);
	}
	
	public void updateStatusInativo(Long id) {
		CargoComissao cargoComissao = findById(id);
		cargoComissao.setTpSituacao(getDomainValue(DmStatusEnum.INATIVO));
		super.store(cargoComissao);
	}
	
	private UsuarioLMS getUsuarioLogado() {
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		return usuarioLMS;
	}
	
	private CargoComissao findAtivoByIdUsuario(Long idUsuario) {
		CargoComissaoDAO cargoComissaoDAO = (CargoComissaoDAO) getDao();
		return cargoComissaoDAO.findAtivoByIdUsuario(idUsuario);
	}

	private void validateUsuario(UsuarioLMS usuario) {
		Funcionario funcionario = funcionarioService.findByIdUsuario(usuario.getIdUsuario());
		if (funcionario == null) {
			throw new BusinessException(EXCEPTION_STORE_USUARIO_SEM_FUNCIONARIO);
		}
	}

	public void setCargoComissaoDao(CargoComissaoDAO dao) {
		setDao(dao);
	}
	
	public CargoComissaoDAO getCargoComissaoDao() {
		return (CargoComissaoDAO) getDao();
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}
	
	private DomainValue getDomainValue(DmStatusEnum dmStatusEnum) {
		return domainValueService.findDomainValueByValue(DOMINIO_TP_SITUACAO, dmStatusEnum.getValue());
	}

}
