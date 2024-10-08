package com.mercurio.lms.carregamento.swt.action;

import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.carregamento.swt.carregarVeiculoDocumentosVolumesAction"
 */
public class CarregarVeiculoDocumentosVolumesAction extends CrudAction {

	private VolumeNotaFiscalService volumeNotaFiscalService;
   
    /**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedVolumes(TypedFlatMap criteria) {
    	Long idDoctoServico = criteria.getLong("idDoctoServico");
    	return volumeNotaFiscalService.findPaginatedVolumesByDoctoServico(idDoctoServico, FindDefinition.createFindDefinition(criteria));
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountVolumes(TypedFlatMap criteria) {
    	Long idDoctoServico = criteria.getLong("idDoctoServico");
    	return volumeNotaFiscalService.getRowCountVolumesByDoctoServico(idDoctoServico);
    }
    
    public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}
}
