package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.dao.ManifestoEntregaVolumeDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.manifestoEntregaVolumeService"
 */
public class ManifestoEntregaVolumeService extends CrudService<ManifestoEntregaVolume, Long> {

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	private final ManifestoEntregaVolumeDAO getManifestoEntregaVolumeDAO() {
		return (ManifestoEntregaVolumeDAO) getDao();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setManifestoEntregaVolumeDAO(ManifestoEntregaVolumeDAO dao) {
		setDao(dao);
	}

	
	/**
	 * Recupera uma instância de <code>ManifestoEntregaVolume</code> a partir do ID.
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ManifestoEntregaVolume findById(java.lang.Long id) {
		return (ManifestoEntregaVolume)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ManifestoEntregaVolume bean) {
		return super.store(bean);
	}

	/**
	 * Busca os manifestoEntregaVolume associados ao manifesto.
	 * 
	 * @param idManifesto
	 * @return
	 */
	public List<ManifestoEntregaVolume> findByManifesto(Long idManifesto) {
		return getManifestoEntregaVolumeDAO().findByManifesto(idManifesto);
	}

	/**
	 * Busca os manifestoEntregaVolume pendentes associados ao manifesto e docto servico.
	 * 
	 * @param idManifesto
	 * @param idDoctoServico
	 * @return
	 */
	public List<ManifestoEntregaVolume> findManifestoEntregaVolumePendentesByManifestoDoctoServico(Long idManifesto, Long idDoctoServico) {
		return getManifestoEntregaVolumeDAO().findManifestoEntregaVolumePendentesByManifestoDoctoServico(idManifesto, idDoctoServico);
	}

	/**
	 * Busca os manifestoEntregaVolume associados ao manifesto e docto servico.
	 * 
	 * @param idManifesto
	 * @param idDoctoServico
	 * @return
	 */
	public List<ManifestoEntregaVolume> findManifestoEntregaVolumeByManifestoDoctoServico(Long idManifesto, Long idDoctoServico) {
		return getManifestoEntregaVolumeDAO().findManifestoEntregaVolumeByManifestoDoctoServico(idManifesto, idDoctoServico);
	}
	
	
	/**
	 * Remove os registros associados ao manifesto de entrega recebido por parâmetro.
	 * 
	 * @param idManifestoEntrega
	 */
	public void removeByIdManifestoEntrega(Long idManifestoEntrega){
		getManifestoEntregaVolumeDAO().removeByIdManifestoEntrega(idManifestoEntrega);
	}
	
	public List<ManifestoEntregaVolume> findManifestoEntregaVolumesPendentes(Long idControleCarga) {
		return getManifestoEntregaVolumeDAO().findManifestoEntregaVolumesPendentes(idControleCarga);							
	}
	
	
	/**
	 * Realiza uma busca paginada a partir do id do controle de carga informado
	 * @param idControleCarga
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage<ManifestoEntregaVolume> findPaginatedEntregasVolumesPendentes(Long idControleCarga, FindDefinition findDefinition) {
		return getManifestoEntregaVolumeDAO().findPaginatedEntregasVolumesPendentes(idControleCarga, findDefinition);							
	}
	
	public Integer getRowCountEntregasVolumesPendentes( Long idControleCarga ){
		return getManifestoEntregaVolumeDAO().getRowCountEntregasVolumesPendentes(idControleCarga);
	}
	
	
	
	
	/**
     * Retorna uma list de ManifestoEntregaVolumes
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public List findManifestoEntregaVolumes(Long idCarregamentoDescarga) {
    	return this.getManifestoEntregaVolumeDAO().findManifestoEntregaVolumes(idCarregamentoDescarga);
    }
    
    
    
    
    /**
     * Retorna o número de registros da uma list de ManifestoEntregaVolumes
     * 
     * @param idCarregamentoDescarga
     * @return
     */
    public Integer getRowCountManifestoEntregaVolumes(Long idCarregamentoDescarga) {
    	return this.getManifestoEntregaVolumeDAO().getRowCountManifestoEntregaVolumes(idCarregamentoDescarga);
    }  
    
    
    
	 public List<ManifestoEntregaVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idFilial, Long idManifesto, Long idDoctoServico, Short cdLocalizacaoMercadoria){
		List<Short> lstCdLocMerc = new ArrayList<Short>();
		lstCdLocMerc.add(cdLocalizacaoMercadoria);
		return getManifestoEntregaVolumeDAO().findByDoctoServicoAndManifestoAndLocalizacaoVolume(idFilial, idManifesto, idDoctoServico, lstCdLocMerc);
	 }
		 
	 
	 public List<ManifestoEntregaVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idFilial, Long idManifesto, Long idDoctoServico, List<Short> cdsLocalizacaoMercadoria){
		 return getManifestoEntregaVolumeDAO().findByDoctoServicoAndManifestoAndLocalizacaoVolume(idFilial, idManifesto, idDoctoServico, cdsLocalizacaoMercadoria);
	 }
		 
	 
	 public List<ManifestoEntregaVolume> findByControleCargaAndFilialOrigem(Long idControleCarga, Long idFilial){
		 return getManifestoEntregaVolumeDAO().findByControleCargaAndFilialOrigem(idControleCarga, idFilial);
	 }		 
		 
	 public List<ManifestoEntregaVolume> findByManifestoAndDoctoServico(
				Long idManifesto, Long idDoctoServico) {
		 return getManifestoEntregaVolumeDAO().findByManifestoAndDoctoServico(idManifesto, idDoctoServico);
	 }
    
    
    /**
     * Busca Volumes descarregados na entrega apartir do volume e do controle de carga
     * @param idVolume
     * @param idControleCarga
     * @return
     */
    public ManifestoEntregaVolume findByIdVolumeAndIdControleCarga(Long idVolume, Long idControleCarga){
		 return getManifestoEntregaVolumeDAO().findByIdVolumeAndIdControleCarga(idVolume, idControleCarga);
	}
    
    public List<ManifestoEntregaVolume> findByControleCargaAndDoctoServico(Long idControleCarga, Long idDoctoServico){
    	return getManifestoEntregaVolumeDAO().findByControleCargaAndDoctoServico(idControleCarga, idDoctoServico);
    }
    
    public List<ManifestoEntregaVolume> findManifestoEntregaVolumeByIdNotaFiscalConhecimentoIdManifestoEntregaDoc(Long idNotaFiscalConhecimento, Long idManifestoEntregaDocumento){
        return getManifestoEntregaVolumeDAO().findManifestoEntregaVolumeByIdNotaFiscalConhecimentoIdManifestoEntregaDoc(idNotaFiscalConhecimento, idManifestoEntregaDocumento);
    }

	public List<Map<String, Object>> findDadosVolumeEntregaParcial(
			Long idManifestoEntrega, Long idDoctoServico,
			Short cdOcorrenciaEntrega, Integer nrNota) {
		// TODO Auto-generated method stub
		return getManifestoEntregaVolumeDAO().findDadosVolumeEntregaParcial(idManifestoEntrega,idDoctoServico,cdOcorrenciaEntrega,nrNota);
	}
}