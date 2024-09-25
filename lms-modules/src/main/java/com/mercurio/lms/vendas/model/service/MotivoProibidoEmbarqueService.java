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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.motivoProibidoEmbarqueService"
 */
public class MotivoProibidoEmbarqueService extends CrudService<MotivoProibidoEmbarque, Long> {

	/**
	 * Recupera uma inst�ncia de <code>MotivoProibidoEmbarque</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public MotivoProibidoEmbarque findById(java.lang.Long id) {
		return (MotivoProibidoEmbarque)super.findById(id);
	}

	/**
     * M�todo utilizado pela Integra��o
	 * @author Andre Valadas
	 * 
     * @param dsMotivoProibidoEmbarque
     * @return <MotivoProibidoEmbarque>
     */
	public MotivoProibidoEmbarque findMotivoProibidoEmbarque(String dsMotivoProibidoEmbarque) {
		return getMotivoProibidoEmbarqueDAO().findMotivoProibidoEmbarque(dsMotivoProibidoEmbarque);
	}

	/**
     * M�todo para pegar uma lista do Motivo Proibido Embarque
	 * 
     * @return List
     */
    public List findMotivoProibidoEmbarque(Map criteria){
        return getDao().findListByCriteria(criteria);
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

	protected MotivoProibidoEmbarque beforeStore(MotivoProibidoEmbarque bean) {
		MotivoProibidoEmbarque motivoProibidoEmbarque = (MotivoProibidoEmbarque)bean;
		Empresa emp = (Empresa)SessionContext.get(SessionKey.EMPRESA_KEY);
		motivoProibidoEmbarque.setEmpresa(emp);
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MotivoProibidoEmbarque bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setMotivoProibidoEmbarqueDAO(MotivoProibidoEmbarqueDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private MotivoProibidoEmbarqueDAO getMotivoProibidoEmbarqueDAO() {
		return (MotivoProibidoEmbarqueDAO) getDao();
	}

}
