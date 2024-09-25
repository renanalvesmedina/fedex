package com.mercurio.lms.gm.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.DetalheRota;
import com.mercurio.lms.gm.model.dao.DetalheRotaDAO;
import com.mercurio.lms.gm.model.dao.EmbarqueDAO;
import com.mercurio.lms.gm.model.dao.RotaEmbarqueDAO;

/**
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.gm.detalheRotaService"
 */
public class DetalheRotaService  extends CrudService<DetalheRota, Long>{
	private EmbarqueDAO embarqueDao;
	private RotaEmbarqueDAO rotaEmbarqueDao;
	private RotaEmbarqueService rotaEmbarqueService;
	private DetalheRotaDAO detalheRotaDao;
	
	
	public EmbarqueDAO getEmbarqueDao() {
		return embarqueDao;
	}

	public void setEmbarqueDao(EmbarqueDAO embarqueDao) {
		this.embarqueDao = embarqueDao;
	}

	public RotaEmbarqueDAO getRotaEmbarqueDao() {
		return rotaEmbarqueDao;
	}

	public void setRotaEmbarqueDao(RotaEmbarqueDAO rotaEmbarqueDao) {
		this.rotaEmbarqueDao = rotaEmbarqueDao;
	}

	public DetalheRotaDAO getDetalheRotaDao() {
		return detalheRotaDao;
	}

	public void setDetalheRotaDao(DetalheRotaDAO detalheRotaDao) {
		this.detalheRotaDao = detalheRotaDao;
	}

	public RotaEmbarqueService getRotaEmbarqueService() {
		return rotaEmbarqueService;
	}

	public void setRotaEmbarqueService(RotaEmbarqueService rotaEmbarqueService) {
		this.rotaEmbarqueService = rotaEmbarqueService;
	}

	/**
	 * Recupera uma inst�ncia de <code>DetalheRota</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DetalheRota findById(java.lang.Long id) {
		return (DetalheRota)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		DetalheRota detalheRota = detalheRotaDao.findDetalheRotaByid(id);
		
		List<DetalheRota> listDetalheRota = detalheRotaDao.findDetalheRotaByIdRotaEmbarque(detalheRota.getIdRotaEmbarque());
		
		if(listDetalheRota != null && listDetalheRota.size()>1){
		super.removeById(id);
		}else{
			getRotaEmbarqueService().removeById(detalheRota.getIdRotaEmbarque());
	}
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids){
			this.removeById(id);
	}
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	
	public Serializable store(DetalheRota bean) {
		DetalheRota detalheRota = detalheRotaDao.findDetalheRotaByIdSigla(bean);
		
			if(detalheRota != null){
				throw new BusinessException("LMS-04308");
			}
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDetalheRotaDAO(DetalheRotaDAO dao) {
		setDao( dao );
	}
}
