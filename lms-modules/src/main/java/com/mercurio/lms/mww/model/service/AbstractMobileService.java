package com.mercurio.lms.mww.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.CarregamentoDescargaVolume;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaVolumeService;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoVolumeService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public abstract class AbstractMobileService {
	private boolean isDescargaEntrega = false;
	protected VolumeNotaFiscalService volumeNotaFiscalService;
	protected PreManifestoVolumeService preManifestoVolumeService;
	protected DispositivoUnitizacaoService dispositivoUnitizacaoService;
	protected CarregamentoDescargaVolumeService carregamentoDescargaVolumeService;

	public abstract boolean isCarga();

	public List<Map<String, Object>> findConhecimentosDescargaMapped(Long idControleCarga, String tpControleCarga) {
		isDescargaEntrega = "C".equals(tpControleCarga);
		return findConhecimentosControleCargaMapped(idControleCarga, tpControleCarga);
	}

	public List<Map<String, Object>> findConhecimentosControleCargaMapped(Long idControleCarga, String tpControleCarga) {
		return findConhecimentosControleCargaMapped(idControleCarga, null, tpControleCarga);
	}

	public List<Map<String, Object>> findConhecimentosControleCargaMapped(Long idControleCarga, Long idDispositivoUnitizacao, String tpControleCarga) {
		List<Map<String, Object>> conhecimentos = new ArrayList<Map<String, Object>>();

		if (idControleCarga != null) {
			List<VolumeNotaFiscal> volumes = new ArrayList<VolumeNotaFiscal>(); 
			if (isCarga()) {
				volumes = (idDispositivoUnitizacao != null)
						? volumeNotaFiscalService.findVolumesDispositivoUnitizacao(idControleCarga, idDispositivoUnitizacao)
								: volumeNotaFiscalService.findVolumesNaoUnitizadosControleCarga(idControleCarga);
			} else {
				Boolean isViagem = "V".equals(tpControleCarga);
				volumes = volumeNotaFiscalService.findVolumesDescargaControleCarga(idControleCarga, idDispositivoUnitizacao, isViagem);
			}

			if (volumes != null) {
				for (VolumeNotaFiscal volume : volumes) {
					Long nrConhecimento = volume.getNrConhecimento();
					if(nrConhecimento == null){
						if(volume.getNotaFiscalConhecimento().getConhecimento().getDoctoServicoOriginal() != null){
							nrConhecimento = volume.getNotaFiscalConhecimento().getConhecimento().getDoctoServicoOriginal().getNrDoctoServico();
						}else{
							nrConhecimento = volume.getNotaFiscalConhecimento().getConhecimento().getNrConhecimento();
						}
					}
					
					Map<String, Object> conhecimento = getConhecimentoMappedInList(conhecimentos, nrConhecimento);

					if (conhecimento == null) {
						if(!findConhecimentoMappedByVolumeAndIdControleCarga(volume, idControleCarga, nrConhecimento).isEmpty()){
							conhecimentos.add(findConhecimentoMappedByVolumeAndIdControleCarga(volume, idControleCarga, nrConhecimento));
						}
					}
				}
			}
		}

		return conhecimentos;
	}

	public Map<String, Object> findConhecimentoMappedByVolumeAndIdControleCarga(VolumeNotaFiscal volume, Long idControleCarga, Long nrConhecimento) {
		Conhecimento conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
		Map<String, Object> conhecimentoMapped = new HashMap<String, Object>();
		
		conhecimentoMapped.put("sgFilialDocumento", conhecimento.getFilialOrigem().getSgFilial());
		conhecimentoMapped.put("nrConhecimento", nrConhecimento);
		conhecimentoMapped.put("nrCae", conhecimento.getNrCae());
		conhecimentoMapped.put("tpDoctoServico", conhecimento.getTpDoctoServico().getDescriptionAsString());

		if(idControleCarga != null) {
			String key = "qtVolumesCarregados";
			String tpOperacao = "C";
			Integer volumesProcessados = 0;
			Integer totalVolumesProcessados = 0;
			
			volumesProcessados = countVolumesCarregamentoDescarga(idControleCarga, conhecimento.getIdDoctoServico(), volume.getDispositivoUnitizacao());
			if (!isCarga()) {
				key = "qtVolumesDescarregados";
				tpOperacao = "D";
			}
			totalVolumesProcessados = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga, conhecimento.getIdDoctoServico(), tpOperacao);

			conhecimentoMapped.put(key, volumesProcessados);
			conhecimentoMapped.put("qtVolumes", conhecimento.getQtVolumes());
			conhecimentoMapped.put("completo", totalVolumesProcessados.compareTo(conhecimento.getQtVolumes()) == 0);
			conhecimentoMapped.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volume.getIdVolumeNotaFiscal()));
		}

		return conhecimentoMapped;
	}

	public List<Map<String, Object>> findDispositivosDescarregados(Long idControleCarga, String tpControleCarga) {
		isDescargaEntrega = "C".equals(tpControleCarga);
		return findDispositivosControleCarga(idControleCarga, tpControleCarga);
	}

	public List<Map<String, Object>> findDispositivosControleCarga(Long idControleCarga, String tpControleCarga) {
		List<Map<String, Object>> dispositivos = new ArrayList<Map<String,Object>>();
		if (idControleCarga != null) {

			List<VolumeNotaFiscal> volumes;
			if (isCarga()) {
				volumes =  volumeNotaFiscalService.findVolumesUnitizadosControleCarga(idControleCarga);
			} else {
				Boolean isViagem = "V".equals(tpControleCarga);
				volumes = volumeNotaFiscalService.findVolumesUnitizadosDescargaControleCarga(idControleCarga, isViagem);
			}

			if (volumes != null) {
				for (VolumeNotaFiscal volume : volumes) {
					if (!hasDispositivoInList(dispositivos, volume.getDispositivoUnitizacao().getIdDispositivoUnitizacao())) {
						dispositivos.add(getDispositivoMapped(
								volume.getDispositivoUnitizacao().getIdDispositivoUnitizacao(), idControleCarga, tpControleCarga));
					}
				}
			}
		}

		return dispositivos;
	}

	private Map<String, Object> getConhecimentoMappedInList(List<Map<String, Object>> conhecimentos, Long nrConhecimento) {
		for (Map<String, Object> conhecimento : conhecimentos) {
			if (nrConhecimento.equals((Long) conhecimento.get("nrConhecimento"))) {
				return conhecimento;
			}
		}

		return null;
	}

	private Map<String, Object> getDispositivoMapped(Long idDispositivo, Long idControleCarga, String tpControleCarga) {
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findById(idDispositivo);
		DispositivoUnitizacao dispositivoPai = dispositivo.getDispositivoUnitizacaoPai();
		Map<String, Object> dispositivoMapped = new HashMap<String, Object>();
		dispositivoMapped.put("idDispositivoUnitizacao", dispositivo.getIdDispositivoUnitizacao());
		dispositivoMapped.put("tpDispositivoUnit", dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
		dispositivoMapped.put("qtVolumes", volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaDispositivo(idControleCarga, getTpOperacao(), dispositivo));
		dispositivoMapped.put("qtDispositivoUnit", (dispositivo.getDispositivosUnitizacao() != null) ? dispositivo.getDispositivosUnitizacao().size() : 0);
		dispositivoMapped.put("conhecimentos", findConhecimentosControleCargaMapped(idControleCarga, idDispositivo, tpControleCarga));

		if (dispositivoPai != null) {
			DispositivoUnitizacao dispositivoAvo = dispositivoPai.getDispositivoUnitizacaoPai();
			dispositivoMapped.put("idDispositivoUnitizacaoPai", dispositivoPai.getIdDispositivoUnitizacao());

			if (dispositivoAvo != null) {
				dispositivoMapped.put("idDispositivoUnitizacaoAvo", dispositivoAvo.getIdDispositivoUnitizacao());
			}
		}

		return dispositivoMapped;
	}

	private boolean hasDispositivoInList(List<Map<String, Object>> dispositivos, Long idDispositivo) {
		for (Map<String, Object> dispositivo : dispositivos) {
			if (idDispositivo.equals((Long) dispositivo.get("idDispositivoUnitizacao"))) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Salva um carregamento descarga no dia para tipo SF.
	 * Rotina: Gravar Carregamento/Descarga Volume
	 * 
	 * @param carregamentoDescarga
	 * @param volumeNotaFiscal
	 * @param dispositivoUnitizacao
	 */
	public void storeCarregamentoDescargaVolume(CarregamentoDescarga carregamentoDescarga,
			VolumeNotaFiscal volumeNotaFiscal, DispositivoUnitizacao dispositivoUnitizacao) {
		CarregamentoDescargaVolume carregamentoDescargaVolume = new CarregamentoDescargaVolume();

		carregamentoDescargaVolume.setCarregamentoDescarga(carregamentoDescarga);
		carregamentoDescargaVolume.setVolumeNotaFiscal(volumeNotaFiscal);
		carregamentoDescargaVolume.setDispositivoUnitizacao(dispositivoUnitizacao);
		carregamentoDescargaVolume.setDhOperacao(JTDateTimeUtils.getDataHoraAtual());
		carregamentoDescargaVolume.setTpScan(new DomainValue("SF"));
		carregamentoDescargaVolume.setQtVolumes(volumeNotaFiscal.getQtVolumes());

		carregamentoDescargaVolumeService.store(carregamentoDescargaVolume);
	}

	private Integer totalVolumesCarregadosNaoUnitizados(Long idControleCarga, Long idDoctoServico) {
		return volumeNotaFiscalService.countVolumesCarregadosNaoUnitizados(idControleCarga, idDoctoServico);
	}

	private Integer totalVolumesCarregados(Long idControleCarga, Long idDoctoServico) {
		return volumeNotaFiscalService.countVolumesCarregadosTotal(idControleCarga, idDoctoServico);
	}

	private Integer totalVolumesDescarregadosNaoUnitizados(Long idControleCarga, Long idDoctoServico) {
		return volumeNotaFiscalService.countVolumesDescarregadosNaoUnitizados(idControleCarga, idDoctoServico, isDescargaEntrega);
	}

	private Integer totalVolumesDescarregados(Long idControleCarga, Long idDoctoServico) {
		return volumeNotaFiscalService.countVolumesDescarregadosTotal(idControleCarga, idDoctoServico, isDescargaEntrega);
	}

	public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
		this.preManifestoVolumeService = preManifestoVolumeService;
	}

	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}
	
	public Integer countVolumesCarregamentoDescarga(Long idControleCarga, Long idDoctoServico, DispositivoUnitizacao dispositivoUnitizacao) {
		String tpOperacao = "D";
		if (isCarga()) {
			tpOperacao =  "C";
		}
		return volumeNotaFiscalService.findRowCountVolumesCarregamentoDescarga(idControleCarga, idDoctoServico, tpOperacao, dispositivoUnitizacao);
	}
	
	public String getTpOperacao() {
		if (isCarga()) {
			return "C";
		}
		return "D";
	}
	
	public void setCarregamentoDescargaVolumeService(
			CarregamentoDescargaVolumeService carregamentoDescargaVolumeService) {
		this.carregamentoDescargaVolumeService = carregamentoDescargaVolumeService;
	}
}
