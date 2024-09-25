package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.NotaCreditoDoctoDAO;

public class NotaCreditoDoctoService extends CrudService<NotaCreditoDocto, Long> {

	@Override
	public NotaCreditoDocto findById(Long id) {
        return (NotaCreditoDocto) super.findById(id);
    }

	public List<NotaCreditoDocto> findByIdNotaCredito(Long idNotaCredito) {
	    return parseNotaCreditoDoctos(getNotaCreditoDoctoDAO().findByIdNotaCredito(idNotaCredito));
	}

	private List<NotaCreditoDocto> parseNotaCreditoDoctos(List<NotaCreditoDocto> documentos) {
	    if (documentos == null) {
	        documentos = new ArrayList<NotaCreditoDocto>();
	    }

	    return documentos;
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
	public Long store(NotaCreditoDocto notaCreditoDocto) {
        return (Long) super.store(notaCreditoDocto);
    }

	public List<NotaCreditoDocto> findByIdDoctoServico(Long idDoctoServico) {
		return getNotaCreditoDoctoDAO().findByIdDoctoServico(idDoctoServico);
	}

	public Long findQuantidadeEntregasEfetuadasControleCarga(Long idControleCarga) {
	    Integer totalEntregas = getNotaCreditoDoctoDAO().findQuantidadeEntregasEfetuadasControleCarga(idControleCarga);

	    if (totalEntregas == null) {
	        return 0L;
	    }

	    return totalEntregas.longValue();
	}

    private NotaCreditoDoctoDAO getNotaCreditoDoctoDAO() {
        return (NotaCreditoDoctoDAO) getDao();
    }

    public void setNotaCreditoDoctoDAO(NotaCreditoDoctoDAO dao) {
        setDao(dao);
    }

	public Long findCountQtEntregasRealizadasByIdNotaCredito(Long idNotaCredito) {
		return getNotaCreditoDoctoDAO().findCountQtEntregasRealizadasByIdNotaCredito(idNotaCredito);
	}

}
