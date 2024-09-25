package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.CarregamentoDescargaVolume;
import com.mercurio.lms.carregamento.model.dao.CarregamentoDescargaVolumeDAO;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;


/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.carregamentoDescargaVolumeService"
 */
public class CarregamentoDescargaVolumeService extends CrudService<CarregamentoDescargaVolume, Long> {

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @param Inst�ncia do DAO.
	 */
	public void setCarregamentoDescargaVolumeDAO(CarregamentoDescargaVolumeDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private CarregamentoDescargaVolumeDAO getCarregamentoDescargaVolumeDAO() {
		return (CarregamentoDescargaVolumeDAO) getDao();
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(CarregamentoDescargaVolume bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove o volume lido do carregamento descarga.
	 * 
	 * @param carregamentoDescarga
	 * @param volumeNotaFiscal
	 */
	public void removeByCarregamentoDescargaByVolumeNotaFiscal(CarregamentoDescarga carregamentoDescarga, VolumeNotaFiscal volumeNotaFiscal) {
    	getCarregamentoDescargaVolumeDAO().removeByCarregamentoDescargaByVolumeNotaFiscal(carregamentoDescarga, volumeNotaFiscal);
    }
	
	/**
	 * 
	 * @param carregamentoDescarga
	 */
	public void removeByCarregamentoDescargaNaFilialQuandoCarregamento(CarregamentoDescarga carregamentoDescarga) {
    	getCarregamentoDescargaVolumeDAO().removeByCarregamentoDescargaNaFilialQuandoCarregamento(carregamentoDescarga);
    }
	
	/**
	 * 
	 * @param controleCarga
	 */
	public void removeByIdControleCarga(Long idControleCarga) {
    	getCarregamentoDescargaVolumeDAO().removeByIdControleCarga(idControleCarga);
    }
	
	/**
	 * 
	 * @param idControleCarga
	 * @param idVolumeNotaFiscal
	 * @param idCarregamentoDescarga
	 */
	public List<CarregamentoDescargaVolume> findByControleCargaByVolumeNotaFiscalByCarregamentoDescarga(Long idControleCarga, Long idVolumeNotaFiscal, Long idCarregamentoDescarga, String tpOperacao) {
    	return getCarregamentoDescargaVolumeDAO().findByControleCargaByVolumeNotaFiscalByCarregamentoDescarga(idControleCarga, idVolumeNotaFiscal, idCarregamentoDescarga, tpOperacao);
    }
	
	public List<CarregamentoDescargaVolume> findByControleCargaByOperacao(Long idControleCarga, String tpOperacao) {
		return getCarregamentoDescargaVolumeDAO().findByControleCargaByOperacao(idControleCarga,  tpOperacao);
	}

	public boolean validateVolumeDescarregado(Long idCarregamentoDescarga, Long idVolumeNotaFiscal) {
		return getCarregamentoDescargaVolumeDAO().validateVolumeDescarregado(idCarregamentoDescarga,  idVolumeNotaFiscal);
	}
	
}