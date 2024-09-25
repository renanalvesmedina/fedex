package com.mercurio.lms.mobilescanapp.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.CarregamentoDescargaVolume;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.*;
import com.mercurio.lms.expedicao.dto.QtdVolumesConhecimentoDto;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.util.JTDateTimeUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractMobileScanService {

	protected VolumeNotaFiscalService volumeNotaFiscalService;
	protected PreManifestoVolumeService preManifestoVolumeService;
	protected DispositivoUnitizacaoService dispositivoUnitizacaoService;
	protected CarregamentoDescargaVolumeService carregamentoDescargaVolumeService;

	public abstract boolean isCarga();

	public List<Map<String, Object>> findConhecimentosDescargaMapped(Long idControleCarga, String tpControleCarga) {
		return findConhecimentosControleCargaMapped(idControleCarga, null, tpControleCarga);
	}

	public List<Map<String, Object>> findConhecimentosControleCargaMapped(Long idControleCarga, Long idDispositivoUnitizacao,
																		  String tpControleCarga) {
		List<Map<String, Object>> conhecimentos = new ArrayList<>();

		if (idControleCarga != null) {
			List<VolumeNotaFiscal> volumesList;
			if (isCarga()) {
				volumesList = (idDispositivoUnitizacao != null)
						? volumeNotaFiscalService.findVolumesDispositivoUnitizacao(idControleCarga, idDispositivoUnitizacao)
						: volumeNotaFiscalService.findVolumesNaoUnitizadosControleCarga(idControleCarga);
			} else {
				Boolean isViagem = "V".equals(tpControleCarga);
				volumesList = volumeNotaFiscalService.findVolumesDescargaControleCarga(idControleCarga, idDispositivoUnitizacao, isViagem);
			}
			if (volumesList != null && !volumesList.isEmpty()) {
				Set<VolumeNotaFiscal> volumeSet = new HashSet<>(volumesList);
				Set<Long> conhecimentosIds = volumeSet.stream().map(v -> v.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico()).distinct().collect(Collectors.toSet());
				Set<QtdVolumesConhecimentoDto> qtdVolumesProcessados = findQtdVolumesPorIdsConhecimento(idControleCarga, conhecimentosIds, volumesList.get(0).getDispositivoUnitizacao(), true);
				Set<QtdVolumesConhecimentoDto> qtdTotalVolumesProcessados = findQtdVolumesPorIdsConhecimento(idControleCarga, conhecimentosIds, volumesList.get(0).getDispositivoUnitizacao(), false);
				for (VolumeNotaFiscal volume : volumeSet) {
					Long nrConhecimento = volume.getNrConhecimento();
					if(nrConhecimento == null){
						if(volume.getNotaFiscalConhecimento().getConhecimento().getDoctoServicoOriginal() != null){
							nrConhecimento = volume.getNotaFiscalConhecimento().getConhecimento().getDoctoServicoOriginal().getNrDoctoServico();
						}else{
							nrConhecimento = volume.getNotaFiscalConhecimento().getConhecimento().getNrConhecimento();
						}
					}

					Map<String, Object> conhecimento = getConhecimentoMappedInList(conhecimentos, nrConhecimento);

					Map<String, Object> conhecimentoMapped = findConhecimentoMappedByVolumeAndIdControleCarga(volume, idControleCarga, nrConhecimento, qtdVolumesProcessados, qtdTotalVolumesProcessados);
					if (conhecimento.isEmpty() && !conhecimentoMapped.isEmpty()) {
						conhecimentos.add(conhecimentoMapped);
					}
				}
			}
		}

		return conhecimentos;
	}

	public Map<String, Object> findConhecimentoMappedByVolumeAndIdControleCarga(VolumeNotaFiscal volume, Long idControleCarga, Long nrConhecimento,
																				Set<QtdVolumesConhecimentoDto> qtdVolumesProcessados,
																				Set<QtdVolumesConhecimentoDto> qtdTotalVolumesProcessados) {

		Conhecimento conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
		Map<String, Object> conhecimentoMapped = new HashMap<>();

		conhecimentoMapped.put("sgFilialDocumento", conhecimento.getFilialOrigem().getSgFilial());
		conhecimentoMapped.put("nrConhecimento", nrConhecimento);
		conhecimentoMapped.put("nrCae", conhecimento.getNrCae());
		conhecimentoMapped.put("tpDoctoServico", conhecimento.getTpDoctoServico().getDescriptionAsString());

		if(idControleCarga != null) {
			Long idConhecimento = conhecimento.getIdDoctoServico();
			String key = "qtVolumesCarregados";
			Integer totalVolumesProcessados = qtdTotalVolumesProcessados.stream().filter(q -> q.getIdConhecimento().equals(idConhecimento)).map(q -> q.getQtd().intValue()).findFirst().orElse(0);
			Integer volumesProcessados = qtdVolumesProcessados.stream().filter(q -> q.getIdConhecimento().equals(idConhecimento)).map(q -> q.getQtd().intValue()).findFirst().orElse(0);

			if (!isCarga()) {
				key = "qtVolumesDescarregados";
			}

			conhecimentoMapped.put(key, volumesProcessados);
			conhecimentoMapped.put("qtVolumes", conhecimento.getQtVolumes());
			conhecimentoMapped.put("completo", totalVolumesProcessados.compareTo(conhecimento.getQtVolumes()) == 0);

			conhecimentoMapped.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volume));
		}

		return conhecimentoMapped;
	}

	public List<Map<String, Object>> findDispositivosDescarregados(Long idControleCarga, String tpControleCarga, Long idFilial) {

		return findDispositivosControleCarga(idControleCarga, tpControleCarga, idFilial);
	}

	public List<Map<String, Object>> findDispositivosControleCarga(Long idControleCarga, String tpControleCarga, Long idFilial) {
		List<Map<String, Object>> dispositivos = new ArrayList<>();
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
								volume.getDispositivoUnitizacao().getIdDispositivoUnitizacao(), idControleCarga, tpControleCarga, idFilial));
					}
				}
			}
		}

		return dispositivos;
	}

	private Map<String, Object> getConhecimentoMappedInList(List<Map<String, Object>> conhecimentos, Long nrConhecimento) {

		for (Map<String, Object> conhecimento : conhecimentos) {
			if (nrConhecimento.equals(conhecimento.get("nrConhecimento"))) {
				return conhecimento;
			}
		}

		return new HashMap<>();
	}

	private Map<String, Object> getDispositivoMapped(Long idDispositivo, Long idControleCarga, String tpControleCarga, Long idFilial) {
		DispositivoUnitizacao dispositivo = this.dispositivoUnitizacaoService.findById(idDispositivo);
		DispositivoUnitizacao dispositivoPai = dispositivo.getDispositivoUnitizacaoPai();
		Map<String, Object> dispositivoMapped = new HashMap<>();
		dispositivoMapped.put("idDispositivoUnitizacao", dispositivo.getIdDispositivoUnitizacao());
		dispositivoMapped.put("tpDispositivoUnit", dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
		dispositivoMapped.put("cdLocMercadoria", dispositivo.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
		dispositivoMapped.put("qtVolumes", volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaDispositivo(idControleCarga,
				getTpOperacao(), dispositivo));
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
			if (idDispositivo.equals(dispositivo.get("idDispositivoUnitizacao"))) {
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
	public Set<QtdVolumesConhecimentoDto> findQtdVolumesPorIdsConhecimento(Long idControleCarga, Set<Long> idsDoctoServico, DispositivoUnitizacao dispositivoUnitizacao, boolean isConhecimento) {

		String tpOperacao = "D";

		if (isCarga()) {
			tpOperacao =  "C";
		}

		return volumeNotaFiscalService.findQtdVolumesPorIdsConhecimento(idControleCarga, idsDoctoServico, tpOperacao, dispositivoUnitizacao, isConhecimento);
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
