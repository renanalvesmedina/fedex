package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoColeta;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.NotaCreditoColetaDAO;

public class NotaCreditoColetaService extends CrudService<NotaCreditoColeta, Long> {

	@Override
	public NotaCreditoColeta findById(Long id) {
        return (NotaCreditoColeta) super.findById(id);
    }

	public List<NotaCreditoColeta> findByIdNotaCredito(Long idNotaCredito) {
	    return parseNotaColetasColetasCredito(getNotaCreditoColetaDAO().findByIdNotaCredito(idNotaCredito));
	}

	@Override
	public void removeById(Long id) {
        super.removeById(id);
    }

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	@Override
	public Long store(NotaCreditoColeta notaCreditoDocto) {
        return (Long) super.store(notaCreditoDocto);
    }

	public Long findQuantidadeColetasEfetuadasControleCarga(Long idControleCarga) {
	    Integer totalColetas = getNotaCreditoColetaDAO().findQuantidadeColetasEfetuadasControleCarga(idControleCarga);

	    if (totalColetas == null) {
	        return 0L;
	    }

	    return totalColetas.longValue();
	}

    private List<NotaCreditoColeta> parseNotaColetasColetasCredito(List<NotaCreditoColeta> coletas) {
        if (coletas == null) {
            coletas = new ArrayList<NotaCreditoColeta>();
        }

        return coletas;
    }

    public NotaCreditoColetaDAO getNotaCreditoColetaDAO() {
        return (NotaCreditoColetaDAO) getDao();
    }

	public void setNotaCreditoColetaDAO(NotaCreditoColetaDAO dao) {
        setDao(dao);
    }

	public Long findCountQtColetasExecutadasByIdNotaCredito(Long idNotaCredito) {
		return getNotaCreditoColetaDAO().findCountQtColetasExecutadasByIdNotaCredito(idNotaCredito);
	}

}
