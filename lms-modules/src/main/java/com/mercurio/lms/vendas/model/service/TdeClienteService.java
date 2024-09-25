package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DestinatarioTdeCliente;
import com.mercurio.lms.vendas.model.TdeCliente;
import com.mercurio.lms.vendas.model.dao.TdeClienteDAO;

public class TdeClienteService extends CrudService<TdeCliente, Long> {
	private DestinatarioTdeClienteService destinatarioTdeClienteService;

	public DestinatarioTdeClienteService getDestinatarioTdeClienteService() {
		return destinatarioTdeClienteService;
	}

	public void setDestinatarioTdeClienteService(
			DestinatarioTdeClienteService destinatarioTdeClienteService) {
		this.destinatarioTdeClienteService = destinatarioTdeClienteService;
	}

	public TdeClienteDAO getTdeClienteDAO() {
		return (TdeClienteDAO)getDao();
	}

	public void setTdeClienteDAO(TdeClienteDAO tdeClienteDAO) {
		this.setDao(tdeClienteDAO);
	}

	public TdeCliente findById(Long id) {
		return (TdeCliente)super.findById(id);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return getTdeClienteDAO().findPaginated((TypedFlatMap)criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getTdeClienteDAO().getRowCount(criteria);
	}

	private UsuarioLMS getUsuarioLogado() {
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		return usuarioLMS;
	}
	
	public Serializable store(TdeCliente bean) {
		if (LongUtils.hasValue(bean.getIdTdeCliente())) {
			bean.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
			bean.setUsuarioAlteracao(this.getUsuarioLogado());
		} else {
			bean.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
			bean.setUsuarioInclusao(this.getUsuarioLogado());
		}

		Serializable s = super.store(bean);		
		
		for (DestinatarioTdeCliente dtc : bean.getDestinatarioTdeClientes()) {
			if (LongUtils.hasValue(dtc.getIdDestinatarioTdeCliente())) {
				dtc.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
				dtc.setUsuarioAlteracao(this.getUsuarioLogado());
			} else {
				dtc.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				dtc.setUsuarioInclusao(this.getUsuarioLogado());
			}
			
			dtc.setTdeCliente(bean);
			
			destinatarioTdeClienteService.store(dtc);
		}
		
		return s;
	}

	public TdeCliente findTdeVigenteByDivisaoClienteAndIdCliente(Long idDivisaoCliente, Long idCliente, YearMonthDay dtVigente) {
		return getTdeClienteDAO().findByIdDivisaoCliente(idDivisaoCliente, idCliente, dtVigente);
	}
	
}
