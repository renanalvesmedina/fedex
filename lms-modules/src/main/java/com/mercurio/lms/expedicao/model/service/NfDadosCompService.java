package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.NfDadosComp;
import com.mercurio.lms.expedicao.model.dao.NfDadosCompDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.nfDadosCompService"
 */
public class NfDadosCompService extends CrudService<NfDadosComp, Long> {

	public List findByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento){
		return getNfDadosCompDAO().findByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
	}
	
	public List findByIdDadosComplemento(Long idDadosComplemento) {
		return getNfDadosCompDAO().findByIdDadosComplemento(idDadosComplemento);
	}	
	
	/**
	 * Recupera uma instância de <code>NfDadosComp</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public NfDadosComp findById(java.lang.Long id) {
		return (NfDadosComp)super.findById(id);
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
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(NfDadosComp bean) {
		return super.store(bean);
	}

	public List findIdsByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
		return getNfDadosCompDAO().findIdsByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setNfDadosCompDAO(NfDadosCompDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private NfDadosCompDAO getNfDadosCompDAO() {
		return (NfDadosCompDAO) getDao();
	}

}