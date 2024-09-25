package com.mercurio.lms.expedicao.model.service;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.conferirVolumeService"
 */
public class ConferirVolumeService extends CrudService {
	
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private FilialService filialService;
	private ParametroGeralService parametroGeralService;
	private EventoService eventoService;
	private EventoVolumeService eventoVolumeService;
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private ConhecimentoService conhecimentoService;
	private CtoAwbService ctoAwbService;
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	
	private static final String CD_LOCALIZACAO_VOLUME_EXTRAVIADO = "CD_LOCALIZACAO_VOLUME_EXTRAVIADO";
	private static final String CD_EVENTO_VOLUME_ENCONTRADO = "CD_EVENTO_VOLUME_ENCONTRADO";
	private static final String CD_EVENTO_VOLUME_LIDO = "CD_EVENTO_VOLUME_LIDO";
	
	private static final String SCAN_FISICO = "SF";
	
	/**
	 * Localizacao da mercadoria encontra-se "No terminal"
	 */
	
	public Map<String, Object> findVolumeByBarCodeSorter(String barCode,Boolean blSorter){
	
					
			VolumeNotaFiscal volume = volumeNotaFiscalService.findVolumeByBarCodeUniqueResult(barCode);								
			
			//CRIA OBJETOS PARA BUSCA DAS INFORMAÇÕES =======
			DispositivoUnitizacao dispositivoUnitizacao = volume.getDispositivoUnitizacao();
			Conhecimento conhecimento = new Conhecimento();
			NotaFiscalConhecimento notaFiscalConhecimento = volume.getNotaFiscalConhecimento();
			if (Boolean.TRUE.equals(blSorter)){
				conhecimento = conhecimentoService.findConhecimentoAtualByIdSorter(notaFiscalConhecimento.getConhecimento().getIdDoctoServico());
			}else{
				conhecimento = conhecimentoService.findConhecimentoAtualById(notaFiscalConhecimento.getConhecimento().getIdDoctoServico());
			}
			if(conhecimento == null)
				conhecimento = notaFiscalConhecimento.getConhecimento();
			Filial filialDestinoOperacional = conhecimento.getFilialDestinoOperacional();
			Filial filialOrigem = conhecimento.getFilialOrigem();
			Filial filialAtualVolume = volume.getLocalizacaoFilial();
			Pessoa destinatario = conhecimento.getClienteByIdClienteDestinatario().getPessoa();
			Pessoa remetente = conhecimento.getClienteByIdClienteRemetente().getPessoa();
			LocalizacaoMercadoria localizacaoMercadoria = volume.getLocalizacaoMercadoria();

			RotaColetaEntrega rotaColetaEntrega = conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaReal();
			if(rotaColetaEntrega == null){
				rotaColetaEntrega = conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaSugerid();
			}
			//===============================================
			
			//INICIALIZA AS VARIÁVAEIS COM VALORES DEFAULT ==
			Long idDispositivoUnitizacao = null;
			Long idDispositivoUnitizacaoAvo = null;
			Short nrRota = 0;
			String dsRota = "";
			String dtPrevEntrega = null;
			String tpDocServico = "";
			String sDestinatario = "";
			String sRemetente = "";
			Integer notaFiscal = 0;
			String sgFilialDestino = "";
			String sgFilialDocumento = "";
			String sgFilialLocalizacao = "";
			String locMercadoria = "";
			Short cdLocMercadoria = 0;
			Boolean extraviado = false;
			Integer qtVolumes = 0;
			Integer nrSequencia = volume.getNrSequencia();
			Long nrConhecimento = volume.getNrConhecimento();
			Long idVolumeNotaFiscal = volume.getIdVolumeNotaFiscal();
			Long idAwb = null;
			//===============================================
			
			//VERIFICA SE OBTEVE ALGUM OBJETO NULO E ATRIBUI A INFORMAÇÃO A VARIAVEL	
			if (dispositivoUnitizacao != null) {
				idDispositivoUnitizacao = dispositivoUnitizacao.getIdDispositivoUnitizacao();
				DispositivoUnitizacao dispositivoUnitizacaoAvo = dispositivoUnitizacao.getDispositivoUnitizacaoPai();
				if (dispositivoUnitizacaoAvo != null) {
					idDispositivoUnitizacaoAvo = dispositivoUnitizacaoAvo.getIdDispositivoUnitizacao();
				}
			}
			if(rotaColetaEntrega != null){
				nrRota = rotaColetaEntrega.getNrRota();
				dsRota = rotaColetaEntrega.getDsRota();
			}
			
			if(conhecimento != null){
				if (conhecimento.getDtPrevEntrega() != null) {
					dtPrevEntrega = conhecimento.getDtPrevEntrega().toString();
				}
				tpDocServico = conhecimento.getTpDoctoServico().getDescriptionAsString();
				qtVolumes = conhecimento.getQtVolumes();
				
				Awb awb = getCtoAwbService().findPreAwbByIdCto(conhecimento.getIdDoctoServico());
				idAwb = (awb == null ? null : awb.getIdAwb());
			}
			
			if(destinatario != null){
				sDestinatario = destinatario.getNmPessoa();
			}
			if(remetente != null){
				sRemetente = remetente.getNmPessoa();
			}
			if(notaFiscalConhecimento != null){
				notaFiscal = notaFiscalConhecimento.getNrNotaFiscal();
			}
			if(filialDestinoOperacional != null){
				sgFilialDestino = filialDestinoOperacional.getSgFilial();
			}
			if(filialOrigem != null){
				sgFilialDocumento = filialOrigem.getSgFilial();
			}
			if(filialAtualVolume != null){
				sgFilialLocalizacao = filialAtualVolume.getSgFilial();
			}
			if(localizacaoMercadoria != null){
				locMercadoria = localizacaoMercadoria.getDsLocalizacaoMercadoria().getValue() + 
				(conhecimento.getObComplementoLocalizacao() != null ? " " + conhecimento.getObComplementoLocalizacao() : "");
				cdLocMercadoria = localizacaoMercadoria.getCdLocalizacaoMercadoria();
				extraviado = isVolumeExtraviado(cdLocMercadoria);
			}
			//=========================================================
					
			//CRIA O MAPA PARA RETORNAR COM OS VALORES OBTIDOS ========
			Map<String, Object> map = new HashMap();
			
			map.put("nrRota", nrRota);
			map.put("dsRota", dsRota);
			map.put("dtPrevEntrega", dtPrevEntrega);
			map.put("tpDocServico", tpDocServico);
			map.put("destinatario", sDestinatario);
			map.put("remetente", sRemetente);
			map.put("notaFiscal", notaFiscal);
			map.put("sgFilialDestino", sgFilialDestino);
			map.put("sgFilialDocumento", sgFilialDocumento);
			map.put("sgFilialLocalizacao", sgFilialLocalizacao);
			map.put("locMercadoria", locMercadoria);
			map.put("cdLocMercadoria", cdLocMercadoria);
			map.put("extraviado", extraviado);
			map.put("qtVolumes", qtVolumes);
			map.put("nrSequencia", nrSequencia);
			map.put("nrConhecimento", nrConhecimento);
			map.put("idVolumeNotaFiscal", idVolumeNotaFiscal);
			map.put("idDispositivoUnitizacao", idDispositivoUnitizacao);
			map.put("macroZona", volume.getMacroZona()!=null?volume.getMacroZona().getMapped():null);
			map.put("idAwb", idAwb);
			
			map.put("idDispositivoUnitizacaoAvo", idDispositivoUnitizacaoAvo);

			return map;		
		}
	
	
	
	
	/**
	 * MÉTODO QUE RECEBE UM CÓDIGO DE BARRAS DE UM VOLUME E RETORNA
	 * DADOS DO MESMO	
	 * @param barCode
	 * @return
	 */
	public Map<String, Object> findVolumeByBarCode(String barCode){
	/*
	 * Volume: O sistema considera volume, os códigos das etiquetas de roterização. 
	 * Sempre que uma etiqueta de roterização é lida, serão apresentadas as seguintes informações: 
	 * Tipo de item lido (fixo Volume), representação fracionada do volume, localização do Volume, 
	 * destino do volume, tipo de documento de serviço, filial origem do documento, número do documento, 
	 * rota de entrega, remetente, destinatário, nota fiscal.
	 * 
	 * Regras:
	 * 		
	 * 		1.	Ao ler um item, onde a sua localização seja “Volume extraviado” 
	 * 			(parâmetro geral CD_LOCALIZACAO_VOLUME_EXTRAVIADO = localização volume), 
	 * 			o sistema apresentará uma mensagem ao usuário perguntado se a localização do item pode ser ajustada.
	 * 				
	 * 				a.	Se sim, então a localização do item será ‘No Terminal’ na filial onde foi encontrado 
	 * 					(gerar Evento Volume encontrado, conforme parâmetro geral CD_EVENTO_VOLUME_ENCONTRADO).
	 * 				
	 * 				b.	Se não, então registrar que o Volume foi lido (gerar Evento Volume lido, 
	 * 					conforme parâmetro geral CD_EVENTO_VOLUME_LIDO).
	 */
				
		VolumeNotaFiscal volume = volumeNotaFiscalService.findVolumeByBarCodeUniqueResult(barCode);								
		
		//CRIA OBJETOS PARA BUSCA DAS INFORMAÇÕES =======
		DispositivoUnitizacao dispositivoUnitizacao = volume.getDispositivoUnitizacao();
		
		NotaFiscalConhecimento notaFiscalConhecimento = volume.getNotaFiscalConhecimento();
		Conhecimento conhecimento = conhecimentoService.findConhecimentoAtualById(notaFiscalConhecimento.getConhecimento().getIdDoctoServico());
		String dataAgendamento = getAgendamentoDoctoServicoService().findDataAgendamentoByIdDoctoServico(notaFiscalConhecimento.getConhecimento().getIdDoctoServico());
		if(conhecimento == null)
			conhecimento = notaFiscalConhecimento.getConhecimento();
		Filial filialDestinoOperacional = conhecimento.getFilialDestinoOperacional();
		Filial filialOrigem = conhecimento.getFilialOrigem();
		Filial filialAtualVolume = volume.getLocalizacaoFilial();
		Pessoa destinatario = conhecimento.getClienteByIdClienteDestinatario().getPessoa();
		Pessoa remetente = conhecimento.getClienteByIdClienteRemetente().getPessoa();
		LocalizacaoMercadoria localizacaoMercadoria = volume.getLocalizacaoMercadoria();

		RotaColetaEntrega rotaColetaEntrega = conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaReal();
		if(rotaColetaEntrega == null){
			rotaColetaEntrega = conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaSugerid();
		}
		//===============================================
		
		//INICIALIZA AS VARIÁVAEIS COM VALORES DEFAULT ==
		Long idDispositivoUnitizacao = null;
		Long idDispositivoUnitizacaoAvo = null;
		String nrCae = "";
		Short nrRota = 0;
		String dsRota = "";
		String dtPrevEntrega = null;
		String tpDocServico = "";
		String sDestinatario = "";
		String sRemetente = "";
		Integer notaFiscal = 0;
		String sgFilialDestino = "";
		String sgFilialDocumento = "";
		Long idFilialLocalizacao = null;
		String sgFilialLocalizacao = "";
		String locMercadoria = "";
		Short cdLocMercadoria = 0;
		Boolean extraviado = false;
		Integer qtVolumes = 0;
		Integer nrSequencia = volume.getNrSequencia();
		Long nrConhecimento = volume.getNrConhecimento();
		Long idVolumeNotaFiscal = volume.getIdVolumeNotaFiscal();
		Long idAwb = null;
		//===============================================
		
		//VERIFICA SE OBTEVE ALGUM OBJETO NULO E ATRIBUI A INFORMAÇÃO A VARIAVEL	
		if (dispositivoUnitizacao != null) {
			idDispositivoUnitizacao = dispositivoUnitizacao.getIdDispositivoUnitizacao();
			DispositivoUnitizacao dispositivoUnitizacaoAvo = dispositivoUnitizacao.getDispositivoUnitizacaoPai();
			if (dispositivoUnitizacaoAvo != null) {
				idDispositivoUnitizacaoAvo = dispositivoUnitizacaoAvo.getIdDispositivoUnitizacao();
			}
		}
		if(rotaColetaEntrega != null){
			nrRota = rotaColetaEntrega.getNrRota();
			dsRota = rotaColetaEntrega.getDsRota();
		}
		
		if(conhecimento != null){
			if (conhecimento.getDtPrevEntrega() != null) {
				dtPrevEntrega = conhecimento.getDtPrevEntrega().toString();
			}
			tpDocServico = conhecimento.getTpDoctoServico().getDescriptionAsString();
			qtVolumes = conhecimento.getQtVolumes();
			
			Awb awb = getCtoAwbService().findPreAwbByIdCto(conhecimento.getIdDoctoServico());
			idAwb = (awb == null ? null : awb.getIdAwb());
			if(conhecimento.getNrCae()!=null){
				nrCae = conhecimento.getNrCae();
			}
		}
		
		if(destinatario != null){
			sDestinatario = destinatario.getNmPessoa();
		}
		if(remetente != null){
			sRemetente = remetente.getNmPessoa();
		}
		if(notaFiscalConhecimento != null){
			notaFiscal = notaFiscalConhecimento.getNrNotaFiscal();
		}
		if(filialDestinoOperacional != null){
			sgFilialDestino = filialDestinoOperacional.getSgFilial();
		}
		if(filialOrigem != null){
			sgFilialDocumento = filialOrigem.getSgFilial();
		}
		if(filialAtualVolume != null){
			sgFilialLocalizacao = filialAtualVolume.getSgFilial();
			idFilialLocalizacao = filialAtualVolume.getIdFilial();
		}
		if(localizacaoMercadoria != null){
			locMercadoria = localizacaoMercadoria.getDsLocalizacaoMercadoria().getValue() + 
			(conhecimento.getObComplementoLocalizacao() != null ? " " + conhecimento.getObComplementoLocalizacao() : "");
			cdLocMercadoria = localizacaoMercadoria.getCdLocalizacaoMercadoria();
			extraviado = isVolumeExtraviado(cdLocMercadoria);
		}
		//=========================================================
				
		//CRIA O MAPA PARA RETORNAR COM OS VALORES OBTIDOS ========
		Map<String, Object> map = new HashMap();
		map.put("nrCae", nrCae);
		map.put("dataAgendamento", dataAgendamento);
		map.put("nrRota", nrRota);
		map.put("dsRota", dsRota);
		map.put("dtPrevEntrega", dtPrevEntrega);
		map.put("tpDocServico", tpDocServico);
		map.put("destinatario", sDestinatario);
		map.put("remetente", sRemetente);
		map.put("notaFiscal", notaFiscal);
		map.put("sgFilialDestino", sgFilialDestino);
		map.put("sgFilialDocumento", sgFilialDocumento);
		map.put("idFilialLocalizacao", idFilialLocalizacao);
		map.put("sgFilialLocalizacao", sgFilialLocalizacao);
		map.put("locMercadoria", locMercadoria);
		map.put("cdLocMercadoria", cdLocMercadoria);
		map.put("extraviado", extraviado);
		map.put("qtVolumes", qtVolumes);
		map.put("nrSequencia", nrSequencia);
		map.put("nrConhecimento", nrConhecimento);
		map.put("idVolumeNotaFiscal", idVolumeNotaFiscal);
		map.put("idDispositivoUnitizacao", idDispositivoUnitizacao);
		map.put("macroZona", volume.getMacroZona()!=null?volume.getMacroZona().getMapped():null);
		map.put("idAwb", idAwb);
		
		map.put("idDispositivoUnitizacaoAvo", idDispositivoUnitizacaoAvo);

		return map;		
	}
		
	public ResultSetPage<EventoVolume> findPaginatedEventoVolumeByIdVolume(Long idVolumeNotaFiscal, FindDefinition findDef){		
		return eventoVolumeService.findPaginatedByIdVolume(idVolumeNotaFiscal, findDef);
	}

	@Deprecated
	public java.io.Serializable executeGeraEventoVolumeEncontrado(Long idVolumeNotaFiscal){
		return getEventoVolumeService().generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_ENCONTRADO, SCAN_FISICO);//SCAN FÍSICO
	}

	@Deprecated
	public java.io.Serializable executeGeraEventoVolumeEncontradoTpScan(Long idVolumeNotaFiscal, String tpScan){
		return getEventoVolumeService().generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_ENCONTRADO, tpScan);
	}

	@Deprecated
	public java.io.Serializable executeEventoVolumeLido(Long idVolumeNotaFiscal) {
		return getEventoVolumeService().generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_LIDO, SCAN_FISICO);//SCAN FÍSICO
	}

	@Deprecated
	public java.io.Serializable executeEventoVolumeLidoTpScan(Long idVolumeNotaFiscal, String tpScan) {
		return getEventoVolumeService().generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_LIDO, tpScan);
	}
	
	public boolean isVolumeExtraviado(Short cdLocMercadoria){
		int cdLocalizacaoMercadoriaExtraviada = 0;
		
		String param = getParametroGeral(CD_LOCALIZACAO_VOLUME_EXTRAVIADO);
		
		if (param != null &&  !"".equals(param)){
			try {
				cdLocalizacaoMercadoriaExtraviada = Integer.parseInt(param);
			} catch (Exception e) {
				// LMS-45035 - Erro na conversão do tipo {0} para o tipo {1}.
				throw new BusinessException("LMS-45035", new Object[]{"String", "Integer"}, e);
			}
		}	
		
		return cdLocMercadoria == cdLocalizacaoMercadoriaExtraviada;
	}
	
	private String getParametroGeral(String nmParam){
		ParametroGeral param = parametroGeralService.findByNomeParametro(nmParam, Boolean.FALSE);
		if (param!=null) {
			return param.getDsConteudo();
		}else{
			return null;
		}
	}
	
	
	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {		
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}
	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}
	
	public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {		
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}
	public LocalizacaoMercadoriaService getLocalizacaoMercadoriaService() {
		return localizacaoMercadoriaService;
	}
	
	public void setFilialService(FilialService filialService) {		
		this.filialService = filialService;
	}
	public FilialService getFilialService() {
		return filialService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}

	public EventoService getEventoService() {
		return eventoService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public EventoVolumeService getEventoVolumeService() {
		return eventoVolumeService;
	}

	/**
	 * @param dispositivoUnitizacaoService the dispositivoUnitizacaoService to set
	 */
	public void setDispositivoUnitizacaoService(
			DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	/**
	 * @return the dispositivoUnitizacaoService
	 */
	public DispositivoUnitizacaoService getDispositivoUnitizacaoService() {
		return dispositivoUnitizacaoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
}

	public AgendamentoDoctoServicoService getAgendamentoDoctoServicoService() {
		return agendamentoDoctoServicoService;
	}

	public void setAgendamentoDoctoServicoService(
			AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}




	public CtoAwbService getCtoAwbService() {
		return ctoAwbService;
	}




	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}


}
