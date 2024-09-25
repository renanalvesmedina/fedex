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
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.documentoAnexoService"
 */
public class DocumentoAnexoService extends CrudService<DocumentoAnexo, Long> {

	private ConfiguracoesFacade configuracoesFacade;
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * Recupera uma inst�ncia de <code>DocumentoAnexo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DocumentoAnexo findById(java.lang.Long id) {
		return (DocumentoAnexo)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(DocumentoAnexo bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDocumentoAnexoDAO(DocumentoAnexoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
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
