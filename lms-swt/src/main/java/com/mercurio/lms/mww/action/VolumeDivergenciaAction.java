package com.mercurio.lms.mww.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.VolumeSobraService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe utilizada para retornar os volumes que seram apresentados na tela consulta volumes
 * 	em divergência no carregamento de entrega.
 * @spring.bean id="lms.mww.volumeDivergenciaAction"
 */
public class VolumeDivergenciaAction {

	private VolumeNotaFiscalService volumeNotaFiscalService;
	private VolumeSobraService volumeSobraService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;

	/**
	 * Método que recebe um map contendo o id do documento de servico
	 * @param parametros
	 * @return Mensagem com a chave concatenada
	 */
	public Map<String, Object> getVolumesConsultaDivergencia(Map parametros) {		
		Long idControleCarga = Long.parseLong(parametros.get("idControleCarga").toString());
		Long idDoctoServico = Long.parseLong(parametros.get("idDoctoServico").toString());
		
		List<Map<String,Object>> itens = volumeNotaFiscalService.findVolumesConsultaDivergencia(idDoctoServico, idControleCarga);
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		
		retorno.put("itens", itens);
		
		return retorno;
	}
	
	public Map<String, Object> findVolumesSobra(TypedFlatMap param) {
		Long idControleCarga = param.getLong("idControleCarga");
		Long idFilial = param.getLong("idFilial");
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = param.getList("volumesSobra");
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("sobras", getVolumeSobraService().findVolumesSobraByIdControleCarga(idControleCarga, idFilial));
		
		return result;
	}
	
	public Map<String, Object> abreRNCAutomatica(TypedFlatMap param) {
		Long idControleCarga = param.getLong("idControleCarga");
		//Long idFilial = param.getLong("idFilial");
		Long idFilial = getSessionIdFilial();
		String volumesSobraString = param.getString("volumesSobra");
				
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = buildListaDoctoVol(volumesSobraString);
		
		if(listaDocVolmRNCcriacaoAutomatica != null){
			getOcorrenciaNaoConformidadeService().executeCriacaoRNCautomaticaDescarregamentoComSobra(idControleCarga, idFilial, listaDocVolmRNCcriacaoAutomatica);
		}
		
		return new TypedFlatMap();
	}

	private List<Map<String, Object>> buildListaDoctoVol(String volumesSobraString) {
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = new ArrayList<Map<String,Object>>();
		
		if(volumesSobraString != null) {
			String volumes[] = volumesSobraString.split(";");
			
			for (String volume : volumes) {
				String arguments[] = volume.split("\\|");
				
				Map volumeSobra  = new HashMap<String, String>();

                volumeSobra.put("ID_DOCTO_SERVICO", arguments[0]);
				volumeSobra.put("NR_DOCTO_SERVICO", arguments[1]);
				volumeSobra.put("TP_DOCUMENTO_SERVICO", arguments[2]);
				volumeSobra.put("SG_FILIAL", arguments[3]);
				volumeSobra.put("NR_SEQUENCIA", arguments[4]);
                volumeSobra.put("QT_VOLUMES", arguments[5]);
				
				listaDocVolmRNCcriacaoAutomatica.add(volumeSobra);
			}
			
		}
		return listaDocVolmRNCcriacaoAutomatica;
	}	
	
	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public VolumeSobraService getVolumeSobraService() {
		return volumeSobraService;
	}

	public void setVolumeSobraService(VolumeSobraService volumeSobraService) {
		this.volumeSobraService = volumeSobraService;
	}

	public OcorrenciaNaoConformidadeService getOcorrenciaNaoConformidadeService() {
		return ocorrenciaNaoConformidadeService;
	}

	public void setOcorrenciaNaoConformidadeService(
			OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}

	private Long getSessionIdFilial(){
		return SessionUtils.getFilialSessao().getIdFilial();
	}

}
