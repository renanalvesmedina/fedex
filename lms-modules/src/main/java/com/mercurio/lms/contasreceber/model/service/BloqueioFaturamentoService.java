package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contasreceber.model.BloqueioFaturamento;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.dao.BloqueioFaturamentoDAO;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.bloqueioFaturamentoService"
 */
public class BloqueioFaturamentoService extends CrudService<BloqueioFaturamento, Long> {

	private DevedorDocServFatService devedorDocServFatService;

	public Serializable store(BloqueioFaturamento bean) {
		return super.store(bean);
	}

	public BloqueioFaturamento findById(Long id) {
		return getDAO().findById(id);
	}

	public void setDAO(BloqueioFaturamentoDAO dao) {
		setDao(dao);
	}

	public BloqueioFaturamentoDAO getDAO() {
		return (BloqueioFaturamentoDAO) getDao();
	}

    public void removeById(Long id) {
        super.removeById(id);
    }

    public Integer getRowCount(Map criteria) {
		return getDAO().getRowCount(criteria);
    }
    
	public ResultSetPage<BloqueioFaturamento> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}

	public List<Map<String, Object>> find(Map criteria) {
		return getDAO().find(criteria);
	}

	public List<BloqueioFaturamento> findBloqueioFaturamentoAtivo(Long idDevedorDocFat, Long idBloqueioFaturamento) {
		return getDAO().findBloqueioFaturamentoAtivo(idDevedorDocFat, idBloqueioFaturamento);
	}

	public BloqueioFaturamento findByIdDevedorDocServFat(Long id) {
		return getDAO().findByIdDevedorDocServFat(id);
	}

	public void validateByIdDevedorDocServFat(Long id) {
		BloqueioFaturamento bloqueio = findByIdDevedorDocServFat(id);

		if (bloqueio != null) {
			DevedorDocServFat ddsf = devedorDocServFatService.findById(id);
			throw new BusinessException("LMS-36260", new Object[]{ddsf.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial(), ddsf.getDoctoServico().getNrDoctoServico().toString()});
		}
	}

	public void validateByItemFatura(List<ItemFatura> lstItemFatura) {
		for (ItemFatura item : lstItemFatura) {
			if (item.getDevedorDocServFat() == null)
				Hibernate.initialize(item.getDevedorDocServFat());
			validateByIdDevedorDocServFat(item.getDevedorDocServFat().getIdDevedorDocServFat());
		}
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

}
