package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ClienteTerritorio;
import com.mercurio.lms.vendas.model.ComissaoConquista;
import com.mercurio.lms.vendas.model.dao.ComissaoConquistaDAO;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class ComissaoConquistaService extends CrudService<ComissaoConquista, Long> {
	
	private static String NM_SISTEMA = "LMS";

	private ClienteTerritorioService clienteTerritorioService;

	public Serializable storeComissaoConquista(UsuarioLMS usuario, Long idClienteTerritorio, YearMonthDay dtInicio, YearMonthDay dtFim) {
		ComissaoConquista comissaoConquista = findByIdClienteTerritorio(idClienteTerritorio);
		if (comissaoConquista == null) {
			comissaoConquista = new ComissaoConquista();
			comissaoConquista.setClienteTerritorio(clienteTerritorioService.findById(idClienteTerritorio));
		}

		comissaoConquista.setDtInicio(dtInicio);
		comissaoConquista.setDtFim(dtFim);
		
		if (comissaoConquista.getIdComissaoConquista() == null) {
			comissaoConquista.setUsuarioInclusao(usuario);
			comissaoConquista.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		}
		comissaoConquista.setUsuarioAlteracao(usuario);
		comissaoConquista.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		comissaoConquista.setNmSistema(NM_SISTEMA);
		comissaoConquista.setTpSituacao(DmStatusEnum.ATIVO.getDomainValue());
		
		return store(comissaoConquista);
	}
	
	@Override
	public List<ComissaoConquista> find(Map map) {
		return getComissaoConquistaDao().find(convertToEntity(map));
	}

	@Override
	public void removeById(Long id) {
		getComissaoConquistaDao().removeById(id);
	}

	@Override
	public ComissaoConquista findById(Long id) {
		return getComissaoConquistaDao().findById(id);
	}
	
	public void updateStatusInativo(Long id) {
		ComissaoConquista comissaoConquista = findById(id);
		comissaoConquista.setTpSituacao(DmStatusEnum.INATIVO.getDomainValue());
		super.store(comissaoConquista);
	}

	public void updateStatusInativoByIdClienteTerritorio(Long idClienteTerritorio) {
		ComissaoConquista comissaoConquista = findByIdClienteTerritorio(idClienteTerritorio);
		if (comissaoConquista != null) {
			updateStatusInativo(comissaoConquista.getIdComissaoConquista());
		}
	}

	private ComissaoConquista convertToEntity(Map<String, Object> map) {
		Long idComissaoConquista = (Long) map.get("idComissaoConquista");
		ClienteTerritorio clienteTerritorio = getClienteTerritorio((Long) map.get("idClienteTerritorio"));
		YearMonthDay dtInicio = (YearMonthDay) map.get("dtInicio");
		YearMonthDay dtFim = (YearMonthDay) map.get("dtFim");
		return new ComissaoConquista(idComissaoConquista, clienteTerritorio, null, null, null, null, dtInicio, dtFim, NM_SISTEMA);
	}

	private ClienteTerritorio getClienteTerritorio(Long idClienteTerritorio) {
		if (idClienteTerritorio != null) {
			return clienteTerritorioService.findById(idClienteTerritorio);
		}
		return null;
	}
	
	public ComissaoConquista findByIdClienteTerritorio(Long idClienteTerritorio) {
		return getComissaoConquistaDao().findByIdClienteTerritorio(idClienteTerritorio);
	}

	public ClienteTerritorioService getClienteTerritorioService() {
		return clienteTerritorioService;
	}

	public void setClienteTerritorioService(ClienteTerritorioService clienteTerritorioService) {
		this.clienteTerritorioService = clienteTerritorioService;
	}

	public void setComissaoConquistaDao(ComissaoConquistaDAO comissaoConquistaDAO) {
		setDao(comissaoConquistaDAO);
	}

	public ComissaoConquistaDAO getComissaoConquistaDao() {
		return (ComissaoConquistaDAO) getDao();
	}

	public Integer findCount(Map<String, Object> map) {
		return getComissaoConquistaDao().findCount(convertToEntity(map));
	}

}