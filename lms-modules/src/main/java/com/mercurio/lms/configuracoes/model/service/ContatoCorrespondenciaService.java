package com.mercurio.lms.configuracoes.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ContatoCorrespondencia;
import com.mercurio.lms.configuracoes.model.dao.ContatoCorrespondenciaDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.contatoCorrespondenciaService"
 */
public class ContatoCorrespondenciaService extends CrudService<ContatoCorrespondencia, Long> {
	private PessoaService pessoaService;

	/**
	 * Recupera uma inst�ncia de <code>ContatoCorrespondencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
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
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ContatoCorrespondencia bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setContatoCorrespondenciaDAO(ContatoCorrespondenciaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ContatoCorrespondenciaDAO getContatoCorrespondenciaDAO() {
		return (ContatoCorrespondenciaDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}