package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DiferencaComissao;
import com.mercurio.lms.vendas.model.ExecutivoTerritorio;
import com.mercurio.lms.vendas.model.dao.DiferencaComissaoDAO;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;

public class DiferencaComissaoService extends CrudService<DiferencaComissao, Long> {

	private UsuarioLMSService usuarioService;
	private ExecutivoTerritorioService executivoTerritorioService;

	public UsuarioLMSService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioLMSService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void storeListaDiferencaComissao(List<DiferencaComissao> listaDiferencaComissao) {
		DateTime dhStore = JTDateTimeUtils.getDataHoraAtual();
		DiferencaComissao diferencaComissaoStore = null;
		UsuarioLMS usuario = usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		for (DiferencaComissao diferencaComissao : listaDiferencaComissao) {
			if (diferencaComissao.getIdDiferencaComissao() != null) {
				diferencaComissaoStore = findById(diferencaComissao.getIdDiferencaComissao());
			} else {
				diferencaComissaoStore = new DiferencaComissao();
				diferencaComissaoStore.setUsuarioInclusao(usuario);
				diferencaComissaoStore.setDhInclusao(dhStore);
			}
	
			diferencaComissaoStore.setExecutivoTerritorio(diferencaComissao.getExecutivoTerritorio());
			diferencaComissaoStore.setVlComissao(diferencaComissao.getVlComissao());
			diferencaComissaoStore.setDtCompetencia(diferencaComissao.getDtCompetencia());
			diferencaComissaoStore.setTpTeto(diferencaComissao.getTpTeto());
			diferencaComissaoStore.setDsObservacao(diferencaComissao.getDsObservacao());
			
			diferencaComissaoStore.setTpSituacao(DmStatusEnum.ATIVO.getDomainValue());
			diferencaComissaoStore.setUsuarioAlteracao(usuario);
			diferencaComissaoStore.setDhAlteracao(dhStore);
			
			store(diferencaComissaoStore);
		}
	}

	@Override
	public List<DiferencaComissao> find(Map map) {
		return getDiferencaComissaoDao().find(createDiferencaComissaoForCreation(map));
	}

	@Override
	public void removeById(Long id) {
		getDiferencaComissaoDao().removeById(id);
	}

	@Override
	public DiferencaComissao findById(Long id) {
		return getDiferencaComissaoDao().findById(id);
	}

	private DiferencaComissao createDiferencaComissaoForCreation(Map<String, Object> map) {
		Long idDiferencaComissao = (Long) map.get("idDiferencaComissao");
		BigDecimal vlComissao = (BigDecimal) map.get("vlComissao");
		DomainValue tpTeto = (DomainValue) map.get("tpTeto");
		YearMonthDay dtCompetencia = (YearMonthDay) map.get("dtCompetencia");
		ExecutivoTerritorio executivoTerritorio = executivoTerritorioService.findById((Long) map.get("idExecutivoTerritorio"));
		String dsObservacao = (String) map.get("dsObservacao");
		DomainValue tpSituacao = (DomainValue) map.get("tpSituacao");
		UsuarioLMS usuarioInclusao = usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
		UsuarioLMS usuarioAlteracao = usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
		DateTime dhInclusao = JTDateTimeUtils.getDataHoraAtual();
		DateTime dhAlteracao = JTDateTimeUtils.getDataHoraAtual();
		return new DiferencaComissao(idDiferencaComissao, vlComissao, tpTeto, dtCompetencia, 
			executivoTerritorio, dsObservacao, tpSituacao, usuarioInclusao, usuarioAlteracao, dhInclusao, dhAlteracao);
	}

	public ExecutivoTerritorioService getExecutivoTerritorioService() {
		return executivoTerritorioService;
	}

	public void setExecutivoTerritorioService(ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}

	public void setExecutivoDao(DiferencaComissaoDAO diferencaComissaoDAO) {
		setDao(diferencaComissaoDAO);
	}

	public DiferencaComissaoDAO getDiferencaComissaoDao() {
		return (DiferencaComissaoDAO) getDao();
	}

	public Integer findCount(Map<String, Object> map) {
		return getDiferencaComissaoDao().findCount(createDiferencaComissaoForCreation(map));
	}

}
