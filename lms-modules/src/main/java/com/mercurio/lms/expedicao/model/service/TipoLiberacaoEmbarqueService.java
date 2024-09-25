package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.TipoLiberacaoEmbarque;
import com.mercurio.lms.expedicao.model.dao.TipoLiberacaoEmbarqueDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.tipoLiberacaoEmbarqueService"
 */
public class TipoLiberacaoEmbarqueService extends CrudService<TipoLiberacaoEmbarque, Long> {

	/**
	 * Recupera uma instância de <code>TipoLiberacaoEmbarque</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TipoLiberacaoEmbarque findById(java.lang.Long id) {
		return (TipoLiberacaoEmbarque)super.findById(id);
	}

	/**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param dsTipoLiberacaoEmbarque
     * @param tpSituacao
     * @return <TipoLiberacaoEmbarque>
     */
    public TipoLiberacaoEmbarque findTipoLiberacaoEmbarque(String dsTipoLiberacaoEmbarque, DomainValue tpSituacao) {
    	return getTipoLiberacaoEmbarqueDAO().findTipoLiberacaoEmbarque(dsTipoLiberacaoEmbarque, tpSituacao);
    }

	/**
	 * Localiza uma lista de resultados a partir dos critérios de busca 
	 * informados.
	 * 
	 * @param criteria Critérios de busca.
	 * @return Lista de resultados sem paginação.
	 */
	 public List find(Map criteria) {
		List order = null;
		return getTipoLiberacaoEmbarqueDAO().findListByCriteria(criteria,order);
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
	public java.io.Serializable store(TipoLiberacaoEmbarque bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTipoLiberacaoEmbarqueDAO(TipoLiberacaoEmbarqueDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TipoLiberacaoEmbarqueDAO getTipoLiberacaoEmbarqueDAO() {
		return (TipoLiberacaoEmbarqueDAO) getDao();
	}

}