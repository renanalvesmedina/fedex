package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.ControleEsteira;
import com.mercurio.lms.expedicao.model.VolumeNaoProcessado;
import com.mercurio.lms.expedicao.model.dao.VolumeNaoProcessadoDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.expedicao.volumeNaoProcessadoService"
 */
public class VolumeNaoProcessadoService extends CrudService<VolumeNaoProcessado, Long> {

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(VolumeNaoProcessado bean) {
		return super.store(bean);
	}

	public List<VolumeNaoProcessado> findVolumesByNrLote(String nrLote) {
		return this.getVolumeNaoProcessadoDAO().findVolumesByNrLote(nrLote);
	}	
	
	
	/**
	 * @return the volumeNaoProcessadoDAO
	 */
	public VolumeNaoProcessadoDAO getVolumeNaoProcessadoDAO() {
		return (VolumeNaoProcessadoDAO) getDao();
	}

	/**
	 * @param volumeNaoProcessadoDAO
	 *            the volumeNaoProcessadoDAO to set
	 */
	public void setVolumeNaoProcessadoDAO(VolumeNaoProcessadoDAO dao) {
		setDao(dao);
	}

}