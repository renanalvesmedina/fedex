package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.DocumentoAnexo;
import com.mercurio.lms.expedicao.model.dao.DocumentoAnexoDAO;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.documentoAnexoService"
 */
public class DocumentoAnexoService extends CrudService<DocumentoAnexo, Long> {

	private ConfiguracoesFacade configuracoesFacade;
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * Recupera uma instância de <code>DocumentoAnexo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public DocumentoAnexo findById(java.lang.Long id) {
		return (DocumentoAnexo)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(DocumentoAnexo bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setDocumentoAnexoDAO(DocumentoAnexoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private DocumentoAnexoDAO getDocumentoAnexoDAO() {
		return (DocumentoAnexoDAO) getDao();
	}

	public List<Map<String, Object>> findByIdDoctoServico(java.lang.Long idDoctoServico){
		return getDocumentoAnexoDAO().findByIdDoctoServico(idDoctoServico);
	}

	public void removeByIdDoctoServico(Long id, Boolean isFlushSession){
		getDocumentoAnexoDAO().removeByIdDoctoServico(id, isFlushSession);
	}

	public List<Map<String, Object>> findDoctoServicoByFaturaPedido(Long idClienteDest,Long idClienteRem, String fatura){
		Object identificacaoFatura = (Object)configuracoesFacade.getValorParametro("IDENTIFICA_FATURA");
		VarcharI18n identificaFatura = new VarcharI18n(identificacaoFatura.toString());

		return getDocumentoAnexoDAO().findDoctoServicoByFaturaPedido(idClienteDest,idClienteRem, fatura, identificaFatura);
	}
}
