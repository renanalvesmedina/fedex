package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.NotaFiscalServicoDocumento;

public class NotaFiscalServicoDocumentoDAO extends BaseCrudDao<NotaFiscalServicoDocumento, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
        return NotaFiscalServicoDocumento.class;
    }

	@SuppressWarnings("unchecked")
	public List<NotaFiscalServicoDocumento> findByIdNotaFiscalServico(Long idDoctoServico) {
		StringBuffer query = new StringBuffer();
    	query.append(" from " + getPersistentClass().getName() + " as nfsd");
    	query.append(" where ");
    	query.append(" nfsd.notaFiscalServico.idDoctoServico = :idDoctoServico");

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(),"idDoctoServico", idDoctoServico);
	}
	
}