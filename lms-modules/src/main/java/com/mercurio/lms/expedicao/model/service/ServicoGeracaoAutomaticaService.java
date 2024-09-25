package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.ServicoGeracaoAutomatica;
import com.mercurio.lms.expedicao.model.dao.ServicoGeracaoAutomaticaDAO;

public class ServicoGeracaoAutomaticaService  extends CrudService<ServicoGeracaoAutomatica, Long> {

	@Override
	public Serializable store(ServicoGeracaoAutomatica bean) {
		return super.store(bean);
	}
    
    @Override
    public ServicoGeracaoAutomatica findById(Long idServicoGeracaoAutomatica) {
        return (ServicoGeracaoAutomatica) super.findById(idServicoGeracaoAutomatica);
    }
	
	public List<ServicoGeracaoAutomatica> findByDoctoServicoParcelaPreco(Long idDoctoServico, String[] cdParcelaPreco, Boolean blFinalizado) {
		return getServicoGeracaoAutomaticaDAO().findByDoctoServicoParcelaPreco(idDoctoServico,cdParcelaPreco, blFinalizado);
	}
	
	public List<ServicoGeracaoAutomatica> findByDoctoServicoTpExecucao(Long idDoctoServico, DomainValue[] tpExecucao) {
		return getServicoGeracaoAutomaticaDAO().findByDoctoServicoTpExecucao(idDoctoServico,tpExecucao);
	}
	
	public List<ServicoGeracaoAutomatica> findByDoctoServico(Long idDoctoServico) {
		return getServicoGeracaoAutomaticaDAO().findByDoctoServico(idDoctoServico);
	}
	
	public void storeFaturamentoItemByIds(List<Long> ids, Boolean blFaturado, Boolean blSemCobranca) {
		if(ids != null && ids.size() > 0) {
			getServicoGeracaoAutomaticaDAO().storeFaturamentoItemByIds(ids, blFaturado, blSemCobranca);
		}
	}
	
	public void storeFaturamentoItemByIds(List<Long> ids) {
        if(ids != null && ids.size() > 0) {
            getServicoGeracaoAutomaticaDAO().storeFaturamentoItemByIds(ids);
        }
    }
	
	public void removeServicoGeracaoAutomatica(ServicoGeracaoAutomatica bean) {
		getServicoGeracaoAutomaticaDAO().remove(bean);
	}
	
	public void setServicoGeracaoAutomaticaDAO(ServicoGeracaoAutomaticaDAO dao) {
		setDao(dao);
	}
	
	private ServicoGeracaoAutomaticaDAO getServicoGeracaoAutomaticaDAO() {
		return (ServicoGeracaoAutomaticaDAO) getDao();
	}
}