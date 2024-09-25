package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.ComissaoGarantida;
import com.mercurio.lms.vendas.model.ExecutivoTerritorio;
import com.mercurio.lms.vendas.model.dao.ComissaoGarantidaDAO;

public class ComissaoGarantidaService extends CrudService<ComissaoGarantida, Long> {

	private UsuarioLMSService usuarioService;
	private ExecutivoTerritorioService executivoTerritorioService;

	public UsuarioLMSService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioLMSService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void storeListaComissaoGarantida(List<ComissaoGarantida> listaComissaoGarantida) {
		DateTime dhStore = JTDateTimeUtils.getDataHoraAtual();
		ComissaoGarantida comissaoGarantidaStore = null;
		UsuarioLMS usuario = usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());

		for (ComissaoGarantida comissaoGarantida : listaComissaoGarantida) {
			if (comissaoGarantida.getIdComissaoGarantida() != null) {
				comissaoGarantidaStore = findById(comissaoGarantida.getIdComissaoGarantida());
			} else {
				comissaoGarantidaStore = new ComissaoGarantida();
				comissaoGarantidaStore.setUsuarioInclusao(usuario);
				comissaoGarantidaStore.setDhInclusao(dhStore);
			}

			comissaoGarantidaStore.setExecutivoTerritorio(comissaoGarantida.getExecutivoTerritorio());
			comissaoGarantidaStore.setVlComissao(comissaoGarantida.getVlComissao());
			comissaoGarantidaStore.setDtInicio(comissaoGarantida.getDtInicio());
			comissaoGarantidaStore.setDtFim(comissaoGarantida.getDtFim());

			comissaoGarantidaStore.setUsuarioAlteracao(usuario);
			comissaoGarantidaStore.setDhAlteracao(dhStore);
			
			store(comissaoGarantidaStore);
		}
	}

	@Override
	public List<ComissaoGarantida> find(Map map) {
		return getComissaoGarantidaDao().find(createComissaoGarantidaForCreation(map));
	}

	@Override
	public void removeById(Long id) {
		getComissaoGarantidaDao().removeById(id);
	}

	@Override
	public ComissaoGarantida findById(Long id) {
		return getComissaoGarantidaDao().findById(id);
	}

	private ComissaoGarantida createComissaoGarantidaForCreation(Map<String, Object> map) {
		Long idComissaoGarantida = (Long) map.get("idComissaoGarantida");
		BigDecimal vlComissao = (BigDecimal) map.get("vlComissao");
		YearMonthDay dtInicio = (YearMonthDay) map.get("dtInicio");
		YearMonthDay dtFim = (YearMonthDay) map.get("dtFim");
		ExecutivoTerritorio executivoTerritorio = executivoTerritorioService.findById((Long) map.get("idExecutivoTerritorio"));
		UsuarioLMS usuarioInclusao = usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
		UsuarioLMS usuarioAlteracao = usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
		DateTime dhInclusao = JTDateTimeUtils.getDataHoraAtual();
		DateTime dhAlteracao = JTDateTimeUtils.getDataHoraAtual();
		return new ComissaoGarantida(idComissaoGarantida, vlComissao, dtInicio, dtFim, executivoTerritorio, usuarioInclusao, usuarioAlteracao, dhInclusao,
				dhAlteracao);
	}

	public ExecutivoTerritorioService getExecutivoTerritorioService() {
		return executivoTerritorioService;
	}

	public void setExecutivoTerritorioService(ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}

	public void setExecutivoDao(ComissaoGarantidaDAO comissaoGarantidaDAO) {
		setDao(comissaoGarantidaDAO);
	}

	public ComissaoGarantidaDAO getComissaoGarantidaDao() {
		return (ComissaoGarantidaDAO) getDao();
	}

	public Integer findCount(Map<String, Object> map) {
		return getComissaoGarantidaDao().findCount(createComissaoGarantidaForCreation(map));
	}

}
