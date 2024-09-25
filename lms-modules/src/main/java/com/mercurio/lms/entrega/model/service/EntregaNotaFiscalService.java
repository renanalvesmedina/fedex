package com.mercurio.lms.entrega.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.EntregaNotaFiscal;
import com.mercurio.lms.entrega.model.dao.EntregaNotaFiscalDAO;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.entrega.entregaNotaFiscalService"
 */
public class EntregaNotaFiscalService extends CrudService<EntregaNotaFiscal, Long> {
	
	public EntregaNotaFiscal findById(Long id) {
		return (EntregaNotaFiscal)super.findById(id);
	}

	public void removeById(Long id) {
		super.removeById(id);
	}

	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public java.io.Serializable store(EntregaNotaFiscal bean) {
		return super.store(bean);
	}
	
	public void setEntregaNotaFiscalDAO(EntregaNotaFiscalDAO dao) {
		setDao( dao );
	}

	private final EntregaNotaFiscalDAO getEntregaNotaFiscalDAO() {
		return (EntregaNotaFiscalDAO) getDao();
	}
	
    public List<Long> findNotasComOcorrenciaEntrega(Long idManifesto, List<Long> idsNotasFiscaisConhecimento ) {
    	return getEntregaNotaFiscalDAO().findNotasComOcorrenciaEntrega(idManifesto, idsNotasFiscaisConhecimento);
    }
    
    public Long findIdByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
    	return getEntregaNotaFiscalDAO().findIdByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
    }
	public void executeAlteracaoNota(Long idNotaFiscalConhecimento,
			Long idOcorrenciaEntrega, String observacao) {
		getEntregaNotaFiscalDAO().executeAlteracaoNota(idNotaFiscalConhecimento,idOcorrenciaEntrega,observacao);
	}

	public List<EntregaNotaFiscal> findByIdManifestoEntregaDocumento(Long idManifestoEntregaDocumento) {
		return getEntregaNotaFiscalDAO().findByIdManifestoEntregaDocumento(idManifestoEntregaDocumento);
	}
	
	public boolean existsNotaFiscalSemEntregaByIdsDoctoServico(List<Long> idsDoctoServico, Long idControleCarga) {
		return getEntregaNotaFiscalDAO().existsNotaFiscalSemEntregaByIdsDoctoServico(idsDoctoServico, idControleCarga);
	}
}
