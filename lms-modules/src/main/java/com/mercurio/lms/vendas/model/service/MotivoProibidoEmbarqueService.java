package com.mercurio.lms.vendas.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.vendas.model.MotivoProibidoEmbarque;
import com.mercurio.lms.vendas.model.dao.MotivoProibidoEmbarqueDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.motivoProibidoEmbarqueService"
 */
public class MotivoProibidoEmbarqueService extends CrudService<MotivoProibidoEmbarque, Long> {

	/**
	 * Recupera uma instância de <code>MotivoProibidoEmbarque</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MotivoProibidoEmbarque findById(java.lang.Long id) {
		return (MotivoProibidoEmbarque)super.findById(id);
	}

	/**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param dsMotivoProibidoEmbarque
     * @return <MotivoProibidoEmbarque>
     */
	public MotivoProibidoEmbarque findMotivoProibidoEmbarque(String dsMotivoProibidoEmbarque) {
		return getMotivoProibidoEmbarqueDAO().findMotivoProibidoEmbarque(dsMotivoProibidoEmbarque);
	}

	/**
     * Método para pegar uma lista do Motivo Proibido Embarque
	 * 
     * @return List
     */
    public List findMotivoProibidoEmbarque(Map criteria){
        return getDao().findListByCriteria(criteria);
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

	protected MotivoProibidoEmbarque beforeStore(MotivoProibidoEmbarque bean) {
		MotivoProibidoEmbarque motivoProibidoEmbarque = (MotivoProibidoEmbarque)bean;
		Empresa emp = (Empresa)SessionContext.get(SessionKey.EMPRESA_KEY);
		motivoProibidoEmbarque.setEmpresa(emp);
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MotivoProibidoEmbarque bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMotivoProibidoEmbarqueDAO(MotivoProibidoEmbarqueDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MotivoProibidoEmbarqueDAO getMotivoProibidoEmbarqueDAO() {
		return (MotivoProibidoEmbarqueDAO) getDao();
	}

}
