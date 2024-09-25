package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.DiaVencimento;
import com.mercurio.lms.vendas.model.dao.DiaVencimentoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.diaVencimentoService"
 */
public class DiaVencimentoService extends CrudService<DiaVencimento, Long> {

	/**
	 * Recupera uma inst�ncia de <code>DiaVencimento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DiaVencimento findById(java.lang.Long id) {
		return (DiaVencimento)super.findById(id);
	}

	/**
	 * Retorna o DiaVencimento da divis�o informada por tpModal e tpAbrangencia.
	 * 
	 * @param Long idDivisaoCliente
	 * @param String tpModal
	 * @param String tpAbrangencia
	 * @return DiaVencimento
	 * */
	public DiaVencimento findDiaVencimentoByDivisao(Long idDivisaoCliente, String tpModal, String tpAbrangencia){
		List lstDiaVencimento = getDiaVencimentoDAO().findDiaVencimentoByDivisao(idDivisaoCliente, tpModal, tpAbrangencia);

		if (lstDiaVencimento.size() == 1){
			return (DiaVencimento) lstDiaVencimento.get(0);
		} else {
			return null;
		}		
	}

	/**
	 * Retorna o DiaVencimento do prazoVencimento com valor minimo o dia informado
	 * 
	 * @param Long idPrazoVencimento
	 * @param int diaMinimo
	 * @return DiaVencimento
	 * */
	public DiaVencimento findByPrazoVencimentoAndDiaMinimo(Long idPrazoVencimento, int diaMinimo){
		List lstDiaVencimento = getDiaVencimentoDAO().findByPrazoVencimentoAndDiaMinimo(idPrazoVencimento, diaMinimo);

		if (!lstDiaVencimento.isEmpty()){
			return (DiaVencimento) lstDiaVencimento.get(0);
		} else {
			return null;
		}
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
	public java.io.Serializable store(DiaVencimento bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDiaVencimentoDAO(DiaVencimentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private DiaVencimentoDAO getDiaVencimentoDAO() {
		return (DiaVencimentoDAO) getDao();
	}

	public void removeByIdPrazoVencimento(Long id, Boolean isFlushSession){
		getDiaVencimentoDAO().removeByIdPrazoVencimento(id, isFlushSession);
	}

}