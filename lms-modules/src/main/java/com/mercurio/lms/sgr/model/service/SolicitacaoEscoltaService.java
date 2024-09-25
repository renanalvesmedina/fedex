package com.mercurio.lms.sgr.model.service;

import java.util.Arrays;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sgr.model.SolicEscoltaHistorico;
import com.mercurio.lms.sgr.model.SolicEscoltaNegociacao;
import com.mercurio.lms.sgr.model.SolicitacaoEscolta;
import com.mercurio.lms.sgr.model.dao.SolicitacaoEscoltaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class SolicitacaoEscoltaService extends CrudService<SolicitacaoEscolta, Long> {

	public Integer getRowCount(TypedFlatMap criteria) {
		return getSolicitacaoEscoltaDAO().getRowCountByFilter(criteria);
	}

	public List<SolicitacaoEscolta> find(TypedFlatMap criteria) {
		return getSolicitacaoEscoltaDAO().findByFilter(criteria);
	}

	@Override
	public SolicitacaoEscolta findById(Long id) {
		return (SolicitacaoEscolta) super.findById(id);
	}

	@Override
	public Long store(SolicitacaoEscolta bean) {
		if (bean.getIdSolicitacaoEscolta() == null) {
			return insert(bean);
		}
		SolicitacaoEscolta solicitacao = findById(bean.getIdSolicitacaoEscolta());
		solicitacao.setDhInicioPrevisto(bean.getDhInicioPrevisto());
		solicitacao.setDhFimPrevisto(bean.getDhFimPrevisto());
		solicitacao.setExigenciaGerRisco(bean.getExigenciaGerRisco());
		solicitacao.setControleCarga(bean.getControleCarga());
		solicitacao.setTpOrigem(bean.getTpOrigem());
		solicitacao.setFilialOrigem(bean.getFilialOrigem());
		solicitacao.setAeroportoOrigem(bean.getAeroportoOrigem());
		solicitacao.setClienteOrigem(bean.getClienteOrigem());
		solicitacao.setTpDestino(bean.getTpDestino());
		solicitacao.setFilialDestino(bean.getFilialDestino());
		solicitacao.setAeroportoDestino(bean.getAeroportoDestino());
		solicitacao.setClienteDestino(bean.getClienteDestino());
		solicitacao.setRotaColetaEntrega(bean.getRotaColetaEntrega());
		solicitacao.setNrKmSolicitacaoEscolta(bean.getNrKmSolicitacaoEscolta());
		solicitacao.setDsObservacao(bean.getDsObservacao());
		solicitacao.setMeioTransporteByIdTransportado(bean.getMeioTransporteByIdTransportado());
		solicitacao.setMeioTransporteBySemiReboque(bean.getMeioTransporteBySemiReboque());
		solicitacao.setMotorista(bean.getMotorista());
		solicitacao.setNaturezaProduto(bean.getNaturezaProduto());
		solicitacao.setClienteEscolta(bean.getClienteEscolta());
		solicitacao.setVlCargaCliente(bean.getVlCargaCliente());
		solicitacao.setVlCargaTotal(bean.getVlCargaTotal());
		solicitacao.setQtViaturas(bean.getQtViaturas());
		return (Long) super.store(solicitacao);
	}

	private Long insert(SolicitacaoEscolta bean) {
		bean.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		bean.setFilialSolicitante(new Filial(bean.getFilialSolicitante().getIdFilial()));
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		bean.setUsuarioSolicitante(usuario);
		Long nrSolicitacaoEscolta = getSolicitacaoEscoltaDAO().findNrSolicitacaoEscolta(bean.getFilialSolicitante());
		bean.setNrSolicitacaoEscolta(nrSolicitacaoEscolta == null ? 1 : nrSolicitacaoEscolta + 1);
		bean.setTpSituacao(new DomainValue("I"));
		SolicEscoltaHistorico historico = new SolicEscoltaHistorico();
		historico.setSolicitacaoEscolta(bean);
		historico.setTpSituacao(bean.getTpSituacao());
		historico.setDhInclusao(bean.getDhInclusao());
		historico.setUsuario(bean.getUsuarioSolicitante());
		Long idSolicitacaoEscolta = (Long) super.store(bean);
		getDao().store(historico);
		return idSolicitacaoEscolta;
	}

	@Override
	public void removeById(Long id) {
		removeByIds(Arrays.asList(id));
	}

	@Override
	public void removeByIds(List<Long> ids) {
		getSolicitacaoEscoltaDAO().removeSolicEscoltaHistoricoByIds(ids);
		super.removeByIds(ids);
	}

	public List<SolicEscoltaHistorico> findSolicEscoltaHistorico(Long idSolicitacaoEscolta) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idSolicitacaoEscolta", idSolicitacaoEscolta);
		return getSolicitacaoEscoltaDAO().findSolicEscoltaHistoricoByFilter(criteria);
	}

	public List<SolicEscoltaNegociacao> findSolicEscoltaNegociacao(Long idSolicitacaoEscolta) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idSolicitacaoEscolta", idSolicitacaoEscolta);
		return getSolicitacaoEscoltaDAO().findSolicEscoltaNegociacaoByFilter(criteria);
	}

	private SolicitacaoEscoltaDAO getSolicitacaoEscoltaDAO() {
		return (SolicitacaoEscoltaDAO) getDao();
	}

	public void setSolicitacaoEscoltaDAO(SolicitacaoEscoltaDAO dao) {
		setDao(dao);
	}

}
