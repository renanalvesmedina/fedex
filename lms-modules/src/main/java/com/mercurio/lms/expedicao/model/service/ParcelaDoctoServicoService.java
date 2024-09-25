package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.dao.ParcelaDoctoServicoDAO;
import com.mercurio.lms.sim.model.dao.LMFreteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.parcelaDoctoServicoService"
 */
public class ParcelaDoctoServicoService extends CrudService<ParcelaDoctoServico, Long> {
	private LMFreteDAO lmFreteDao;

	public void setLmFreteDao(LMFreteDAO lmFreteDao) {
		this.lmFreteDao = lmFreteDao;
	}

	/**
	 * Recupera uma inst�ncia de <code>ParcelaDoctoServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ParcelaDoctoServico findById(java.lang.Long id) {
		return (ParcelaDoctoServico)super.findById(id);
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
	public java.io.Serializable store(ParcelaDoctoServico bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setParcelaDoctoServicoDAO(ParcelaDoctoServicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ParcelaDoctoServicoDAO getParcelaDoctoServicoDAO() {
		return (ParcelaDoctoServicoDAO) getDao();
	}

	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		return getParcelaDoctoServicoDAO().findIdsByIdDoctoServico(idDoctoServico);
	}

	public BigDecimal findSumVlParcelasByIdDoctoServico(Long idDoctoServico) {
		return getParcelaDoctoServicoDAO().findSumVlParcelasByIdDoctoServico(idDoctoServico);
	}
	
	public Map<Long, BigDecimal> findVlParcelasByIdDoctoServicoEParcelaPreco(Long idDoctoServico) {
		return getParcelaDoctoServicoDAO().findVlParcelasByIdDoctoServicoEParcelaPreco(idDoctoServico);
	}

	public List findPaginatedParcelasPreco(Long idDoctoServico) {
		return lmFreteDao.findPaginatedParcelasPreco(idDoctoServico);
	}

	public List findPaginatedCalculoServico(Long idDoctoServico){
		return lmFreteDao.findPaginatedCalculoServico(idDoctoServico);
	}

	public List findDadosCalculoFrete(Long idDoctoServico){
		return lmFreteDao.findDadosCalculoFrete(idDoctoServico);
	}

	public List findDadosCalculoDoctoServico(Long idDoctoServico){
		return lmFreteDao.findDadosCalculoDoctoServico(idDoctoServico);
	}

	public List findValorMercadoriaReembolso(Long idDoctoServico) {
		List lista = null;
		lista = lmFreteDao.findValorReciboReembolso(idDoctoServico);
		if(lista.isEmpty()) {
			lista = lmFreteDao.findValorServAdicional(idDoctoServico);
		}
		return lista;
	}

	public List findDadosCalculoDoctoServicoInternacional(Long idDoctoServico){
		return lmFreteDao.findDadosCalculoDoctoServicoInternacional(idDoctoServico);
	}

	public List findByIdDoctoServico(Long idDoctoServico){
		return getParcelaDoctoServicoDAO().findByIdDoctoServico(idDoctoServico);
	}

	public void removeByIdDoctoServico(Long id, Boolean isFlushSession){
		getParcelaDoctoServicoDAO().removeByIdDoctoServico(id, isFlushSession);
	}

	/**
	 * Retorna apenas as parcelas de frete - sem as parcelas de Servi�os Adicionais.
	 * @param idDoctoServico
	 * @return Lista com as parcelas de frete do Documento de Servi�o informado.
	 */
	public List<ParcelaDoctoServico> findParcelasFrete(Long idDoctoServico) {
		return getParcelaDoctoServicoDAO().findParcelasFrete(idDoctoServico);
	}
}