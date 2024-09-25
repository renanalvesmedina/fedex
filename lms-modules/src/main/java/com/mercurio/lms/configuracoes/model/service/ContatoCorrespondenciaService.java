package com.mercurio.lms.configuracoes.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ContatoCorrespondencia;
import com.mercurio.lms.configuracoes.model.dao.ContatoCorrespondenciaDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.contatoCorrespondenciaService"
 */
public class ContatoCorrespondenciaService extends CrudService<ContatoCorrespondencia, Long> {
	private PessoaService pessoaService;

	/**
	 * Recupera uma instância de <code>ContatoCorrespondencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ContatoCorrespondencia findById(java.lang.Long id) {
		return (ContatoCorrespondencia)super.findById(id);
	}

	@Override
	protected ContatoCorrespondencia beforeStore(ContatoCorrespondencia bean) {
		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(pessoaService.findIdPessoaByIdContato(((ContatoCorrespondencia)bean).getContato().getIdContato()));
		return super.beforeStore(bean);
	}

	@Override
	protected void beforeRemoveById(Long id) {
		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(pessoaService.findIdPessoaByIdContatoCorrespondencia((Long)id));
		super.beforeRemoveById(id);
	}

	@Override
	protected void beforeRemoveByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			beforeRemoveById(id);
		}
		super.beforeRemoveByIds(ids);
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
	public java.io.Serializable store(ContatoCorrespondencia bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setContatoCorrespondenciaDAO(ContatoCorrespondenciaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ContatoCorrespondenciaDAO getContatoCorrespondenciaDAO() {
		return (ContatoCorrespondenciaDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}