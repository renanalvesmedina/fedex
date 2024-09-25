package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.dao.ManifestoNacionalVolumeDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.manifestoNacionalVolumeService"
 */
public class ManifestoNacionalVolumeService extends CrudService<ManifestoNacionalVolume, Long> {
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
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	private ManifestoNacionalVolumeDAO getManifestoNacionalVolumeDAO() {
		return (ManifestoNacionalVolumeDAO) getDao();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setManifestoNacionalVolumeDAO(ManifestoNacionalVolumeDAO dao) {
		setDao(dao);
	}
	
	
	/**
	 * Recupera uma instância de <code>Manifesto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ManifestoNacionalVolume findById(java.lang.Long id) {
        return (ManifestoNacionalVolume)super.findById(id);
    }

    public ManifestoNacionalVolume findByIdVolumeAndIdControleCarga(Long idVolume, Long idControleCarga){
		 return getManifestoNacionalVolumeDAO().findByIdVolumeAndIdControleCarga(idVolume, idControleCarga);
	 }
	 
	 public List<ManifestoNacionalVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idDoctoServico, Long idManifesto, Short cdLocalizacaoMercadoria){
		List<Short> lstCdLocMerc = new ArrayList<Short>();
		lstCdLocMerc.add(cdLocalizacaoMercadoria);
		return this.findByDoctoServicoAndManifestoAndLocalizacaoVolume(idDoctoServico, idManifesto, lstCdLocMerc);
	 }
	 
	 public List<ManifestoNacionalVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idDoctoServico, Long idManifesto, List<Short> cdsLocalizacaoMercadoria){
		 return getManifestoNacionalVolumeDAO().findByDoctoServicoAndManifestoAndLocalizacaoVolume(idDoctoServico, idManifesto, cdsLocalizacaoMercadoria);
	 }
	 
	 public List<ManifestoNacionalVolume> findByControleCargaAndFilialOrigem(Long idControleCarga, Long idFilialOrigem){
		 return getManifestoNacionalVolumeDAO().findByControleCargaAndFilialOrigem(idControleCarga, idFilialOrigem);
	 }
	 
	 public void setManifestoNacionalVolumeDao(ManifestoNacionalVolumeDAO dao) {
		 setDao( dao );
	 }
	 
	 public java.io.Serializable store(ManifestoNacionalVolume bean) {
		return super.store(bean);
    }
		
	/**
     * Retorna uma list de ManifestoNacionalVolumes
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public List findManifestoNacionalVolumes(Long idCarregamentoDescarga) {
    	return getManifestoNacionalVolumeDAO().findManifestoNacionalVolumes(idCarregamentoDescarga);
    }
    
    
    
    /**
     * Retorna o número de registros da uma list de ManifestoNacionalVolumes
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public Integer getRowCountManifestoNacionalVolumes(Long idCarregamentoDescarga) {
    	return getManifestoNacionalVolumeDAO().getRowCountManifestoNacionalVolumes(idCarregamentoDescarga);
    }     
    
    public List<ManifestoNacionalVolume> findByControleCargaAndDoctoServico(Long idControleCarga, Long idDoctoServico){
    	return getManifestoNacionalVolumeDAO().findByControleCargaAndDoctoServico(idControleCarga, idDoctoServico);
    }
    
    public List<ManifestoNacionalVolume> findByVolumeNotaFiscalAndFilialDestino(Long idVolumeNotaFiscal, Long idFilialDestino){
    	return getManifestoNacionalVolumeDAO().findByVolumeNotaFiscalAndFilialDestino(idVolumeNotaFiscal, idFilialDestino);
    }

	public ManifestoNacionalVolume findVolumeVinculadoManifestoViagem(Long idManifesto, Long idVolumeNotaFiscal) {
		return getManifestoNacionalVolumeDAO().findVolumeVinculadoManifestoViagem(idManifesto, idVolumeNotaFiscal);	
	}

	public void removeManifestoNacionalVolumeById(Long idVolumeNotaFiscal, Long idManifestoViagemNacional) {
		getManifestoNacionalVolumeDAO().removeManifestoNacionalVolumeById(idVolumeNotaFiscal, idManifestoViagemNacional);
	}

	public ManifestoNacionalVolume verificarManifestoViagemPossuiVinculoDocServico(Long idManifesto) {
		return getManifestoNacionalVolumeDAO().verificarManifestoViagemPossuiVinculoDocServico(idManifesto);
	}

	public ManifestoNacionalVolume findByIdManifestoViagemNacionalVinculadoDocServico(Long idManifestoViagemNacDescarregado, Long idVolumeNotaFiscalLido) {
		return getManifestoNacionalVolumeDAO().findByIdManifestoViagemNacionalVinculadoDocServico(idManifestoViagemNacDescarregado, idVolumeNotaFiscalLido);
	}
   
  }