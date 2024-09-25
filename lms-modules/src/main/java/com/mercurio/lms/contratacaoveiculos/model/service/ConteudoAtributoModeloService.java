package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.ConteudoAtributoModelo;
import com.mercurio.lms.contratacaoveiculos.model.dao.ConteudoAtributoModeloDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.conteudoAtributoModeloService"
 */
public class ConteudoAtributoModeloService extends CrudService<ConteudoAtributoModelo, Long> {

	public void deleteConteudoAtributoModelos(Long idModeloMeioTranspAtributo){
		List conteudos = getConteudoAtributoModeloDAO().buscaConteudoAtributo(idModeloMeioTranspAtributo);
		for (Iterator iter = conteudos.iterator();iter.hasNext();){
			ConteudoAtributoModelo conteudoAtributoModelo = (ConteudoAtributoModelo)iter.next();
			getConteudoAtributoModeloDAO().remove(conteudoAtributoModelo);
		}
	}

	/**
	 * Recupera uma instância de <code>ConteudoAtributoModelo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ConteudoAtributoModelo findById(java.lang.Long id) {
		return (ConteudoAtributoModelo)super.findById(id);
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
	public java.io.Serializable store(ConteudoAtributoModelo bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setConteudoAtributoModeloDAO(ConteudoAtributoModeloDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ConteudoAtributoModeloDAO getConteudoAtributoModeloDAO() {
		return (ConteudoAtributoModeloDAO) getDao();
	}

	public List findConteudoByAtributo(Long idModeloMeioTranspAtributo) {
		return getConteudoAtributoModeloDAO().findConteudoByAtributo(idModeloMeioTranspAtributo);
	}
	
	/**
	 * Solicitação CQPRO00006052 da integração.
	 * Método que retorna uma instancia da classe ConteudoAtributoModelo de acordo com os parâmetros passados.
	 * @param idModeloMeioTranspAtributo
	 * @param dsConteudoAtributoModelo 
	 * @return
	 */
	public ConteudoAtributoModelo findConteudoAtributoTipoMeioTransporte(Long idModeloMeioTranspAtributo, String dsConteudoAtributoModelo){
		return getConteudoAtributoModeloDAO().findConteudoAtributoTipoMeioTransporte(idModeloMeioTranspAtributo, dsConteudoAtributoModelo);
	}

}