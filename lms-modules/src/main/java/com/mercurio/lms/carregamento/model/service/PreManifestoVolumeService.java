package com.mercurio.lms.carregamento.model.service;

 import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.dao.PreManifestoVolumeDAO;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.EventoVolumeService;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.preManifestoDocumentoService"
 */
public class PreManifestoVolumeService extends CrudService<PreManifestoVolume, Long> {
		
	private EventoVolumeService eventoVolumeService;


	/**
	 * Recupera uma instância de <code>PreManifestoVolume</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public PreManifestoVolume findById(java.lang.Long id) {
        return (PreManifestoVolume)super.findById(id);
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
     * Remove Pré-manifesto Volume a partir do id do Manifesto
     * @param idManifesto
     */
    public void removeByIdManifesto(Long idManifesto){
    	this.getPreManifestoVolumeDAO().removeByIdManifesto( idManifesto );
    }
    
    /**
     * Remove Pré-manifesto Volume a partir do id do Manifesto
     * @param idManifesto
     */
    public void removeByIdPreManifestoDocto(Long idPreManifestoDocto){
    	this.getPreManifestoVolumeDAO().removeByIdPreManifestoDocto( idPreManifestoDocto );
    } 
    
    /**
     * Retorna uma lista de preManifestoVolume que possuem PreManifestoDocumento nulo a partir do id do manifesto.
     * 
     * @param idVolume
     * @param idManifesto
     * @return PreManifestoVolume
     */
    public List<PreManifestoVolume> findVolumesSemPreManifDoctoByManifesto(Long idManifesto) {
    	return getPreManifestoVolumeDAO().findVolumesSemPreManifDoctoByManifesto(idManifesto);
    }
    
    /**
     * Gera eventos para os Documentos de Servico que estiverem relacionados com o Pré-Manifesto
     * 
     * @param idManifesto
     * @param idDoctoServico
     */
    public void generateEventoVolumes(List<PreManifestoVolume> listaPreManifestoVolume) {    	
		//[CQ25050] Gera eventos de volume
		for (PreManifestoVolume preManifestoVolume : listaPreManifestoVolume) {
			VolumeNotaFiscal volumeNotaFiscal = preManifestoVolume.getVolumeNotaFiscal();
			if (volumeNotaFiscal.getLocalizacaoMercadoria() != null && volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().longValue() == 1) {
				continue;
			}
			List<Short> cdEventosVolumes = new ArrayList();	
			
			Short cdLocalizacao = volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();			
			if (cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_EM_CARREGAMENTO_ENTREGA)) {
				cdEventosVolumes.add(ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_ENTREGA);
			} else if (cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_EM_CARREGAMENTO_VIAGEM)) {
				cdEventosVolumes.add(ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_VIAGEM);
			} else if (cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_AGUARDANDO_SAIDA_VIAGEM)) {
				cdEventosVolumes.add(ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_VIAGEM);
				cdEventosVolumes.add(ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_VIAGEM);
			} else if (cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_AGUARDANDO_SAIDA_ENTREGA)) {
				cdEventosVolumes.add(ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_COLETA_ENTREGA);
				cdEventosVolumes.add(ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_ENTREGA);
			}
			for (Short cdEvento : cdEventosVolumes) {
				eventoVolumeService.generateEventoVolume(volumeNotaFiscal, cdEvento, "LM");
			}
		}
		
    }
    
    /**
     * Retorna uma lista de Pré-manifesto Volume a partir do id do Manifesto
     * @param idManifesto
     * @return
     */
    public List<PreManifestoVolume> findbyIdManifesto(Long idManifesto){
    	return this.getPreManifestoVolumeDAO().findbyIdManifesto(idManifesto);
    }
    
    
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(PreManifestoVolume bean) {   
    		return super.store(bean);
    }
    
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPreManifestoVolumeDAO(PreManifestoVolumeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PreManifestoVolumeDAO getPreManifestoVolumeDAO() {
        return (PreManifestoVolumeDAO) getDao();
    }
    
    /**
     * Retorna o PreManifestoVolume a partir do id do volume e do id do manifesto.
     * @param idVolume
     * @param idManifesto
     * @return PreManifestoVolume
     */
    public PreManifestoVolume findByVolumeAndManifesto(Long idVolume, Long idManifesto) {
    	return getPreManifestoVolumeDAO().findByVolumeAndManifesto(idVolume, idManifesto);
    }
    
    /**
     * Retorna o PreManifestoVolume a partir do id do volume e do id do manifesto.
     * @param idDoctoServico
     * @param idManifesto
     * @return List<PreManifestoVolume> 
     */
    public List<PreManifestoVolume> findByDoctoServicoAndManifesto(Long idDoctoServico, Long idManifesto) {
    	return getPreManifestoVolumeDAO().findByDoctoServicoAndManifesto(idDoctoServico, idManifesto);
    }

    /**
     * Retorna uma lista PreManifestoVolume a partir do Manifest
     * Utilizado para retornar uma lista com todos os volumes pertencentes a um Manifesto.
     * @param idControleCarga
     * @return
     */
    public List<PreManifestoVolume> findByManifestoDoctoServico(Long idManifesto, Long idDoctoServico) {
    	return getPreManifestoVolumeDAO().findByManifestoDoctoServico(idManifesto, idDoctoServico);
    }
   
    /**
     * Retorna uma lista PreManifestoVolume a partir do Controle de carga
     * Utilizado para retornar uma lista com todos os volumes pertencentes ao Controle de Carga
     * @param idControleCarga
     * @return
     */
    public List<PreManifestoVolume> findByControleCarga(Long idControleCarga){
    	return getPreManifestoVolumeDAO().findByControleCarga(idControleCarga);
    }

    /**
     * Retorna uma lista PreManifestoVolume a partir do Manifesto
     * Utilizado para retornar uma lista com todos os volumes pertencentes ao Manifesto
     * 
     * @param idManifesto
     * @return
     */
    public List<PreManifestoVolume> findByManifesto(Long idManifesto){
    	return getPreManifestoVolumeDAO().findByManifesto(idManifesto);
    }

    
    /**
     * Retorna uma lista PreManifestoVolume a partir de um Manifesto
     * Utilizado para retornar uma lista com todos os volumes, não entregues, pertencentes ao Manifesto
     * @param idManifesto
     * @return
     */    
    public List<PreManifestoVolume> findVolumesNaoEntreguesByManifesto(Long idManifesto ){
    	return getPreManifestoVolumeDAO().findVolumesNaoEntreguesByManifesto(idManifesto);
    }
    
    /**
     * Retorna quantidade de registros encontrados 
     * 
     * @author André Valadas 
     * @param idControleCarga
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountPreManifestoVolume(final Long idControleCarga, final Long idDoctoServico){
    	return getPreManifestoVolumeDAO().getRowCountPreManifestoVolume(idControleCarga, idDoctoServico);
    }
    
    public void removePreManifestoVolume(final PreManifestoVolume preManifestoVolume){
    	getPreManifestoVolumeDAO().remove(preManifestoVolume);
    }
    
    /**
     * Método que retorna uma list de objetos de PreManifestoVolume<br>
     * Se withPreManifestoDocumento = false então retorna somente os que não possuem PreManifestoDocumento, ou seja
     * o idPreManifestoDocumento é null
     * @param idManifesto
     * @param withPreManifestoDocumento 
     * @return
     */
    public List findPreManifestoVolumeByIdManifesto(Long idManifesto, boolean withPreManifestoDocumento) {
    	return getPreManifestoVolumeDAO().findPreManifestoVolumeByIdManifesto(idManifesto, withPreManifestoDocumento);
    }
    
	public PreManifestoVolume findByIdVolumeAndIdControleCarga(Long idVolume, Long idControleCarga){
		return getPreManifestoVolumeDAO().findByIdVolumeAndIdControleCarga(idVolume, idControleCarga);
	}
	 
	 public List<PreManifestoVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idDoctoServico, Long idManifesto, Short cdLocalizacaoMercadoria){
		List<Short> lstCdLocMerc = new ArrayList<Short>();
		lstCdLocMerc.add(cdLocalizacaoMercadoria);
		return getPreManifestoVolumeDAO().findByDoctoServicoAndManifestoAndLocalizacaoVolume(idDoctoServico, idManifesto, lstCdLocMerc);
	 }
	 
	 public List<PreManifestoVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idDoctoServico, Long idManifesto, List<Short> cdsLocalizacaoMercadoria){
		 
		 return getPreManifestoVolumeDAO().findByDoctoServicoAndManifestoAndLocalizacaoVolume(idDoctoServico, idManifesto, cdsLocalizacaoMercadoria);
	 }

	 /**
	  * @author DiogoSB
	  * @param idManifesto
	  * @param idConhecimento
	  * @return
	  */
	public Integer validateDoctoServico(Long idManifesto, Long idConhecimento) {
		return getPreManifestoVolumeDAO().validateDoctoServico(idManifesto , idConhecimento);
	}
	
	/**
	 * Método que retorna uma list de objetos de PreManifestoVolume<br>
	 *  
	 * @param idDoctoServico
	 * @return
	 */
	public List<PreManifestoVolume> findbyIdDoctoServico(Long idDoctoServico){
		return getPreManifestoVolumeDAO().findbyIdDoctoServico(idDoctoServico);
	}

	public void removePreManifestoVolumeDoDocumentoServico(Long idDoctoServico) {
		getPreManifestoVolumeDAO().removePreManifestoVolumeDoDocumentoServico(idDoctoServico);
	}

	public PreManifestoVolume findVolumeVinculadoPreManifestoViagem(Long idManifesto, Long idVolumeNotaFiscal) {
		return getPreManifestoVolumeDAO().findVolumeVinculadoPreManifestoViagem(idManifesto, idVolumeNotaFiscal);
	}

}