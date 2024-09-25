package com.mercurio.lms.rnc.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeSobraFilial;
import com.mercurio.lms.expedicao.model.service.VolumeSobraFilialService;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.dao.RNCFaltaIndevidaDAO;
import com.mercurio.lms.util.FormatUtils;

public class RNCFaltaIndevidaService extends CrudService<OcorrenciaNaoConformidade, Long> {
	
	private VolumeSobraFilialService volumeSobraFilialService;
	
	public VolumeSobraFilialService getVolumeSobraFilialService() {
		return volumeSobraFilialService;
	}
	public void setVolumeSobraFilialService(
			VolumeSobraFilialService volumeSobraFilialService) {
		this.volumeSobraFilialService = volumeSobraFilialService;
	}
	public void setRNCFaltaIndevidaDao(RNCFaltaIndevidaDAO dao) {
		setDao( dao );
	}
	public ResultSetPage findFaltaIndevidaPaginated(Map<String, Long> criteria) {
		ResultSetPage tfm = ((RNCFaltaIndevidaDAO) getDao()).findFaltaIndevidaPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List<TypedFlatMap> lista = new ArrayList<TypedFlatMap>();
		for (Object objeto : tfm.getList()) {
			OcorrenciaNaoConformidade ocorrenciaNaoConformidadeFalta = (OcorrenciaNaoConformidade) objeto;

			TypedFlatMap map = new TypedFlatMap();
			map.put("alertaFaltaReal", ocorrenciaNaoConformidadeFalta.getQtVolumes());
			DoctoServico doctoServico =  ocorrenciaNaoConformidadeFalta.getNaoConformidade().getDoctoServico();
			if (doctoServico != null) { 
				map.put("documentoDeServico", doctoServico
						.getFilialByIdFilialOrigem().getSgFilial()
						+ "-" + FormatUtils.fillNumberWithZero(doctoServico.getNrDoctoServico().toString(), 8)
						+ "-" + doctoServico.getFilialByIdFilialDestino()
								.getSgFilial());
			}
			map.put("filialFalta", ocorrenciaNaoConformidadeFalta.getFilialByIdFilialAbertura().getSgFilial());
			map.put("rnc", ocorrenciaNaoConformidadeFalta.getNaoConformidade().getNrNaoConformidade());
			map.put("volumesOcorFalta", ocorrenciaNaoConformidadeFalta.getQtVolumes());
			
			String filialSobra = "";
			Integer volumesOcorrSobra = 0;
			Integer volumesSobraDesc = 0;
			criteria.put("idDoctoServico", ocorrenciaNaoConformidadeFalta.getNaoConformidade().getDoctoServico().getIdDoctoServico());
			List<OcorrenciaNaoConformidade> ocorrenciaNaoConformidadeSobras = ((RNCFaltaIndevidaDAO) getDao()).findSobra(criteria);
			
			for (OcorrenciaNaoConformidade ocorrenciaNaoConformidadeSobra : ocorrenciaNaoConformidadeSobras) {
				volumesOcorrSobra += ocorrenciaNaoConformidadeSobra.getQtVolumes();
				if (!filialSobra.contains(ocorrenciaNaoConformidadeSobra.getFilialByIdFilialAbertura().getSgFilial())) {
					filialSobra += "," + ocorrenciaNaoConformidadeSobra.getFilialByIdFilialAbertura().getSgFilial();
				}
			}

			List<VolumeSobraFilial> volumesSobraFilial = ((RNCFaltaIndevidaDAO) getDao()).findVolumesSobraDescarga(criteria);
			volumesSobraDesc += volumesSobraFilial.size();
			for (VolumeSobraFilial volumeSobraFilial : volumesSobraFilial) {
				if (!filialSobra.contains(volumeSobraFilial.getFilial().getSgFilial())) {
					filialSobra += "," + volumeSobraFilial.getFilial().getSgFilial();
				}
			}

			if (!filialSobra.isEmpty())
				filialSobra = filialSobra.substring(1);

			map.put("filialSobra", filialSobra);
			map.put("volumesOcorrSobra", volumesOcorrSobra);
			map.put("volumesSobraDesc", volumesSobraDesc);
			if (ocorrenciaNaoConformidadeFalta.getQtVolumes().compareTo(volumesOcorrSobra + volumesSobraDesc) > 0) {
				map.put("volumesFaltaReal", ocorrenciaNaoConformidadeFalta.getQtVolumes() - (volumesOcorrSobra + volumesSobraDesc));
			} else {
				map.put("volumesFaltaReal", 0);
			}
			map.put("totalVolumesDocServico", ocorrenciaNaoConformidadeFalta.getNaoConformidade().getDoctoServico().getQtVolumes());
			
			String value 	= "/image/bola_verde.gif";
			if (((Integer) map.get("volumesFaltaReal")).compareTo(0) == 0) {
				value 	= "/image/bola_vermelha.gif";
			}
			map.put("alertaFaltaReal", value);
			
			
			lista.add(map);
		}
		tfm.setList(lista);
		return tfm;
    }
}