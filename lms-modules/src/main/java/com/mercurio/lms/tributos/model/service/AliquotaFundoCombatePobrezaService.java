package com.mercurio.lms.tributos.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.tributos.model.AliquotaFundoCombatePobreza;
import com.mercurio.lms.tributos.model.dao.AliquotaFundoCombatePobrezaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class AliquotaFundoCombatePobrezaService extends CrudService<AliquotaFundoCombatePobreza, Long> {

	public void setAliquotaFundoCombatePobrezaDAO(AliquotaFundoCombatePobrezaDAO dao) {
		setDao(dao);
	}

	private AliquotaFundoCombatePobrezaDAO getAliquotaFundoCombatePobrezaDAO() {
		return (AliquotaFundoCombatePobrezaDAO) getDao();
	}

	@Override
	protected AliquotaFundoCombatePobreza beforeInsert(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		validateVigenciaInicial(aliquotaFundoCombatePobreza);
		
		UsuarioLMS usuarioInclusao = new UsuarioLMS();
		usuarioInclusao.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		aliquotaFundoCombatePobreza.setUsuarioInclusao(usuarioInclusao);
		aliquotaFundoCombatePobreza.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());

		setarDadosAlteracao(aliquotaFundoCombatePobreza);

		return super.beforeInsert(aliquotaFundoCombatePobreza);
	}

	@Override
	protected AliquotaFundoCombatePobreza beforeUpdate(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		setarDadosAlteracao(aliquotaFundoCombatePobreza);
		return super.beforeUpdate(aliquotaFundoCombatePobreza);
	}
	
	@Override
	protected AliquotaFundoCombatePobreza beforeStore(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		validateVigenciaFinal(aliquotaFundoCombatePobreza);
		validateAliquotaFundoCombatePobrezaVigente(aliquotaFundoCombatePobreza);
		return super.beforeStore(aliquotaFundoCombatePobreza);
	}

	private void validateAliquotaFundoCombatePobrezaVigente(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		if(getAliquotaFundoCombatePobrezaDAO().validateAliquotaFundoCombatePobrezaVigente(aliquotaFundoCombatePobreza)){
			throw new BusinessException("LMS-00047");
		}
	}

	private void validateVigenciaInicial(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		if(aliquotaFundoCombatePobreza.getDtVigenciaInicial().isBefore(JTDateTimeUtils.getDataAtual())
				|| aliquotaFundoCombatePobreza.getDtVigenciaInicial().isEqual(JTDateTimeUtils.getDataAtual())){
			throw new BusinessException("LMS-30040");
		}
	}
	
	private void validateVigenciaFinal(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		if(aliquotaFundoCombatePobreza.getDtVigenciaFinal() != null 
				&& aliquotaFundoCombatePobreza.getDtVigenciaFinal().isBefore(JTDateTimeUtils.getDataAtual())){
			throw new BusinessException("LMS-01030");
		}
	}

	private void setarDadosAlteracao(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		UsuarioLMS usuarioAlteracao = new UsuarioLMS();
		usuarioAlteracao.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		aliquotaFundoCombatePobreza.setUsuarioAlteracao(usuarioAlteracao);
		aliquotaFundoCombatePobreza.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
	}

	public Serializable store(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		return super.store(aliquotaFundoCombatePobreza);
	}

	@Override
	protected void beforeRemoveById(Long id) {
		validateVigencia(id);
		super.beforeRemoveById(id);
	}

	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		for (Long id : ids) {
			validateVigencia(id);
		}
		
		super.beforeRemoveByIds(ids);
	}
	
	private void validateVigencia(Long id) {
		AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza = findById(id);

		if (aliquotaFundoCombatePobreza.getDtVigenciaInicial().isBefore(JTDateTimeUtils.getDataAtual())
				|| aliquotaFundoCombatePobreza.getDtVigenciaInicial().isEqual(JTDateTimeUtils.getDataAtual())) {
			throw new BusinessException("LMS-00005");
		}
	}

	public void removeById(Long id) {
		super.removeById(id);
	}

	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getAliquotaFundoCombatePobrezaDAO().getRowCount(criteria);
	}

	public ResultSetPage<AliquotaFundoCombatePobreza> findPaginated(TypedFlatMap criteria) {
		return getAliquotaFundoCombatePobrezaDAO().findPaginated(criteria);
	}

	public AliquotaFundoCombatePobreza findById(Long id) {
		return (AliquotaFundoCombatePobreza) super.findById(id);
	}
	
	public AliquotaFundoCombatePobreza findAliquotaFundoCombatePobrezaVigenteByUf(Long idUf){
		return getAliquotaFundoCombatePobrezaDAO().findAliquotaFundoCombatePobrezaVigenteByUf(idUf);
	}
}