package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.dao.DoctoServicoFranqueadoDAO;

public class DoctoServicoFranqueadoService extends CrudService<DoctoServicoFranqueado, Long> {
	
	private DoctoServicoFranqueadoDAO getDoctoServicoFranqueado() {
		return (DoctoServicoFranqueadoDAO) getDao();
	}
	
	public void setDoctoServicoFranqueadoDAO(DoctoServicoFranqueadoDAO doctoServicoFranqueadoDAO) {
        setDao(doctoServicoFranqueadoDAO);
    }
	
	public void removeByCompetencia(final YearMonthDay dtCompetencia, final Long idFranquia){
		getDoctoServicoFranqueado().removeByCompetencia(dtCompetencia, idFranquia);
	}
	
	
	@Override
	public Serializable store(DoctoServicoFranqueado bean) {
		return super.store(bean);
	}
	
	public List<DoctoServicoFranqueado> findServicosAdicionais(YearMonthDay dtCompetencia, Long idFranquia) {
		return getDoctoServicoFranqueado().findServicosAdicionais(dtCompetencia, idFranquia);
	}

	@Override
	public void storeAll(List<DoctoServicoFranqueado> list) {
		super.storeAll(list);
	}
 
	public List<Map<String,Object>> findRelatorioContabilLancamentosAnalitico(Map<String,Object> parameters){
		return getDoctoServicoFranqueado().findRelatorioContabilLancamentosAnalitico(parameters);
	}
	
	public List<Map<String,Object>> findRelatorioAnaliticoDocumentos(boolean filtraFranquia, boolean isCSV, Map<String,Object> parameters){
		return getDoctoServicoFranqueado().findRelatorioAnaliticoDocumentos(filtraFranquia, isCSV, parameters);
	}

	public List<Map<String,Object>> findRelatorioAnaliticoFretesLocal(boolean filtraFranquia, boolean isCSV ,Map<String,Object> parameters) {
		return getDoctoServicoFranqueado().findRelatorioAnaliticoFretesLocal(filtraFranquia, isCSV, parameters);
	}
	
	public List<Map<String,Object>> findRelatorioAnaliticoBDM(boolean filtraFranquia, boolean isCSV,Map<String,Object> parameters) {
		return getDoctoServicoFranqueado().findRelatorioAnaliticoBDM(filtraFranquia, isCSV, parameters);
	}
	
	public List<Map<String,Object>> findRelatorioAnaliticoServicosAdicionais(boolean filtraFranquia, boolean isCSV, Map<String,Object> parameters) {
		return getDoctoServicoFranqueado().findRelatorioAnaliticoServicosAdicionais(filtraFranquia, isCSV, parameters);
	}
	
	public List<Map<String,Object>> findRelatorioAnaliticoDocumentosCompetenciaAnterior(boolean filtraFranquia, boolean isCSV, Map<String,Object> parameters) {
		return getDoctoServicoFranqueado().findRelatorioAnaliticoDocumentosCompetenciaAnterior(filtraFranquia, isCSV, parameters);
	}
	
	public List<Map<String,Object>> findRelatorioBaixaCessaoCredito(boolean filtraFranquia, Map<String,Object> parameters){
		return getDoctoServicoFranqueado().findRelatorioBaixaCessaoCredito(filtraFranquia, parameters);
	}
	
	public List<Map<String,Object>> findRelatorioPendenciaPagamento(boolean filtraFranquia, Map<String,Object> parameters){
		return getDoctoServicoFranqueado().findRelatorioPendenciaPagamento(filtraFranquia, parameters);
	}

	public List<Map<String,Object>> findRelatorioSinteticoDefault(Map<String, Object> parameters, boolean sum) {
		return getDoctoServicoFranqueado().findRelatorioSinteticoDefault(parameters,sum);
	}
	
	public List<Map<String,Object>> findRelatorioSinteticoColetaEntrega(Map<String, Object> parameters) {
		return getDoctoServicoFranqueado().findRelatorioSinteticoColetaEntrega(parameters);
	}
	
	public List<Map<String,Object>> findRelatorioSinteticoServicoAdicional(Map<String, Object> parameters) {
		return getDoctoServicoFranqueado().findRelatorioSinteticoServicoAdicional(parameters);
	}

	public List<Map<String,Object>> findRelatorioSinteticoLancamento(Map<String, Object> parameters) {
		return getDoctoServicoFranqueado().findRelatorioSinteticoLancamento(parameters);
	}
	
	public List<Map<String,Object>> findRelatorioSinteticoDocumentosFiscais(Map<String, Object> parameters) {
		return getDoctoServicoFranqueado().findRelatorioSinteticoDocumentosFiscais(parameters);
	}
	
	public List<Map<String,Object>> findRelatorioConsolidadoGeral(Map<String,Object> parameters){
		return getDoctoServicoFranqueado().findRelatorioConsolidadoGeral(parameters);
	}
	
	public List<Map<String,Object>> findRelatorioSinteticoGeralTipoFrete(Map<String,Object> parameters){
		return getDoctoServicoFranqueado().findRelatorioSinteticoGeralTipoFrete(parameters);
	}
}
