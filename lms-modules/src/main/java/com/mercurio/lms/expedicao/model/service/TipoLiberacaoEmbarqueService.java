package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.TipoLiberacaoEmbarque;
import com.mercurio.lms.expedicao.model.dao.TipoLiberacaoEmbarqueDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.tipoLiberacaoEmbarqueService"
 */
public class TipoLiberacaoEmbarqueService extends CrudService<TipoLiberacaoEmbarque, Long> {

	/**
	 * Recupera uma inst�ncia de <code>TipoLiberacaoEmbarque</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public TipoLiberacaoEmbarque findById(java.lang.Long id) {
		return (TipoLiberacaoEmbarque)super.findById(id);
	}

	/**
     * M�todo utilizado pela Integra��o
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
	 * Localiza uma lista de resultados a partir dos crit�rios de busca 
	 * informados.
	 * 
	 * @param criteria Crit�rios de busca.
	 * @return Lista de resultados sem pagina��o.
	 */
	 public List find(Map criteria) {
		List order = null;
		return getTipoLiberacaoEmbarqueDAO().findListByCriteria(criteria,order);
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
	public java.io.Serializable store(TipoLiberacaoEmbarque bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setTipoLiberacaoEmbarqueDAO(TipoLiberacaoEmbarqueDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private TipoLiberacaoEmbarqueDAO getTipoLiberacaoEmbarqueDAO() {
		return (TipoLiberacaoEmbarqueDAO) getDao();
	}

}