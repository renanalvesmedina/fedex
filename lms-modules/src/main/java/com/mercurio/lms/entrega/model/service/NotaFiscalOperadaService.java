package com.mercurio.lms.entrega.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.dao.NotaFiscalOperadaDAO;

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
public class NotaFiscalOperadaService extends CrudService<NotaFiscalOperada, Long> {
	
	public NotaFiscalOperada findById(Long id) {
		return (NotaFiscalOperada)super.findById(id);
	}

	public void removeById(Long id) {
		super.removeById(id);
	}

	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public java.io.Serializable store(NotaFiscalOperada bean) {
		return super.store(bean);
	}
	
	public void setNotaFiscalOperadaDAO(NotaFiscalOperadaDAO dao) {
		setDao( dao );
	}

	private final NotaFiscalOperadaDAO getNotaFiscalOperadaDAO() {
		return (NotaFiscalOperadaDAO) getDao();
	}
	
	public List<NotaFiscalOperada> findByIdNotaFiscalConhecimentoidDoctoServicoTpSituacao(Long idNotaFiscalConhecimento, Long idDoctoServico, String tpSituacao) {
		return this.getNotaFiscalOperadaDAO().findByIdNotaFiscalConhecimentoidDoctoServicoTpSituacao(idNotaFiscalConhecimento, idDoctoServico, tpSituacao);
	}
	
	public List<NotaFiscalOperada> findByIdNotaFiscalConhecimentoFinalizada(Long idNotaFiscalConhecimento) {
		return this.getNotaFiscalOperadaDAO().findByIdNotaFiscalConhecimentoFinalizada(idNotaFiscalConhecimento);
	}
	
	public List<NotaFiscalOperada> findNotaDevolvidaOuRefatorada(Long idDoctoServico) {
		return this.getNotaFiscalOperadaDAO().findNotaDevolvidaOuRefatorada(idDoctoServico);
	}
	
	public boolean validateExistePreManifestoDocumentoPreManifestoVolume(Long idDoctoServico, Long idVolumeNotaFiscal){
	    return this.getNotaFiscalOperadaDAO().validateExistePreManifestoDocumentoPreManifestoVolume(idDoctoServico, idVolumeNotaFiscal);
	}
	
	public List<NotaFiscalOperada> findByIdDoctoServico(Long idDoctoServico) {
		return this.getNotaFiscalOperadaDAO().findByIdDoctoServico(idDoctoServico);
	}
	
	public Integer getRowCount(Map criteria) {
		return this.getNotaFiscalOperadaDAO().getRowCount(criteria);
	}
	
	public ResultSetPage<Map<String, Object>> findPaginatedMap(PaginatedQuery paginatedQuery) {
		return this.getNotaFiscalOperadaDAO().findPaginatedBySql(paginatedQuery);
	}

	public void removeNotasFinalizadasByIdNotaFiscalConhecimento(
			Long idNotaFiscalConhecimento) {
		
		this.getNotaFiscalOperadaDAO().removeNotasFinalizadasByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
	}
	
	public void removeByIdDoctoServico(Long idNotaFiscalConhecimento) {
		this.getNotaFiscalOperadaDAO().removeByIdDoctoServico(idNotaFiscalConhecimento);
	}
}
