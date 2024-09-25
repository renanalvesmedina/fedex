package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspConteudoAtrib;
import com.mercurio.lms.contratacaoveiculos.model.dao.MeioTranspConteudoAtribDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.meioTranspConteudoAtribService"
 */
public class MeioTranspConteudoAtribService extends CrudService<MeioTranspConteudoAtrib, Long> {

	/**
	 * Recupera uma instância de <code>MeioTranspConteudoAtrib</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MeioTranspConteudoAtrib findById(java.lang.Long id) {
		return (MeioTranspConteudoAtrib)super.findById(id);
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
	public java.io.Serializable store(MeioTranspConteudoAtrib bean) {
		return super.store(bean);
	}

	public List findConteudoByMeioAndModelo(Long idModeloMeioTranspAtributo,Long idMeioTransporte) {
		return getMeioTranspConteudoAtribDAO().findConteudoByMeioAndModelo(idModeloMeioTranspAtributo,idMeioTransporte);
	}

	public List findConteudoByMeioTransporte(Long idMeioTransporte) {
		return getMeioTranspConteudoAtribDAO().findConteudoByMeioTransporte(idMeioTransporte);
	}
	
	/**
     * Remove todos atributos do meio de transporte.
     * 
     * @param idMeioTransporte
     * @return valor maior que zero caso os registros tenham sido excluidos
     */
    public Integer removeByMeioTransporte(Long idMeioTransporte) {
    	return getMeioTranspConteudoAtribDAO().removeByMeioTransporte(idMeioTransporte);
    }

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMeioTranspConteudoAtribDAO(MeioTranspConteudoAtribDAO dao) {
		setDao( dao );
	}
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MeioTranspConteudoAtribDAO getMeioTranspConteudoAtribDAO() {
		return (MeioTranspConteudoAtribDAO) getDao();
	}
}