package com.mercurio.lms.mobilescanapp.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.carregamento.model.service.*;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaVolumeService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.model.service.*;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesAwb;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.portaria.model.service.BoxService;
import com.mercurio.lms.portaria.model.service.ControleQuilometragemService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.mww.descargaMobileService"
 */
public class DescargaMobileScanService extends AbstractMobileScanService {

	private ControleCargaService controleCargaService;
	private ControleTrechoService controleTrechoService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private DoctoServicoService doctoServicoService;
	private BoxService boxService;
	private ControleQuilometragemService controleQuilometragemService;
	private LacreControleCargaService lacreControleCargaService;
	private UnitizacaoService unitizacaoService;
	private ParametroGeralService parametroGeralService;
	private ManifestoNacionalVolumeService manifestoNacionalVolumeService;
	private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
	private FilialService filialService;
	private EventoVolumeService eventoVolumeService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private ConfiguracoesFacade configuracoesFacade;
	private ManifestoService manifestoService;
	private HistoricoFilialService historicoFilialService;
	private EquipeOperacaoService equipeOperacaoService;
	private ConhecimentoService conhecimentoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private VolumeSobraService volumeSobraService;
	private UsuarioLMSService usuarioLMSService;
	private VolumeSobraFilialService volumeSobraFilialService;
	private PedidoColetaService pedidoColetaService;
	private CtoAwbService ctoAwbService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private AwbService awbService;
	private MeioTransporteService meioTransporteService;
	private IntegracaoJwtService integracaoJwtService;
	private MoedaService moedaService;
	private PaisService paisService;

	private Log log = LogFactory.getLog(DescargaMobileScanService.class);
	
	public boolean isCarga() {
		return false;
	}

	public ControleCarga findControleCargaViagem(Long idMeioTransporte, String token) {
		criaSessao(token);
		try {
			ControleCarga controleCarga = controleCargaService.findByMeioTransporteAndTpStatusControleCargaEmDescargaViagem(idMeioTransporte);

			if (controleCarga == null || (controleCarga.getTpControleCarga().getValue().equals("V") && !validateControleCarga(controleCarga))) {
				throw new BusinessException("LMS-45001");
			}

			return controleCarga;
		}finally {
			destroiSessao();
		}
	}

	/** Regra 45.03.04.04 Item 2 ii - Iniciar Descarga de Viagem
	 * Realiza validações prévias no Controle de Carga
	 * @param controleCarga
	 * @return retorna true caso o Controle de Carga seja válido
	 * 		   ou false caso contrário
	 */
	public boolean validateControleCarga(ControleCarga controleCarga){
		return isTrechoControleCarga(controleCarga ) && isFilialAtualizaStatus(controleCarga );
	}



	public Map<String,Object> executeDescargaVolume(VolumeNotaFiscal volumeNotaFiscal,
													ControleCarga controleCarga,
													String tpScan){

		Map<String, Object> map = new HashMap<String, Object>();
		ManifestoNacionalVolume manifestoNacionalVolume= manifestoNacionalVolumeService.findByIdVolumeAndIdControleCarga(volumeNotaFiscal.getIdVolumeNotaFiscal(),
				controleCarga.getIdControleCarga());

		//LMS-5382 - Se VOLUME_NOTA_FISCAL.LOCALIZACAO_FILIAL = id filial logada
		//              && LOCALIZACAO_MERCADORIA.CD_LOCALIZACAO_MERCADORIA = 34 -> VOLUME_NOTA_FISCAL.ID_LOCALIZACAO_MERCADORIA=LOCALIZACAO_MERCADORIA.ID_LOCALIZACAO_MERCADORIA
		if (volumeNotaFiscal.getLocalizacaoFilial() != null && volumeNotaFiscal.getLocalizacaoFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())
				&& volumeNotaFiscal.getLocalizacaoMercadoria() != null && volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("34"))) {

			executeIniciarDescargaVolumePrevistoNaFilial(volumeNotaFiscal, manifestoNacionalVolume, tpScan);
		}
		else {
			map = executeIniciarDescargaVolumeNaoPrevistoNaFilial(volumeNotaFiscal, manifestoNacionalVolume, tpScan);
		}

		return map;
	}


	/**
	 * Realiza da descarga individual dos volumes
	 * @param controleCarga
	 */
	public Map<String,Object> executeDescargaVolume(VolumeNotaFiscal volumeNotaFiscal,
													ControleCarga controleCarga,
													CarregamentoDescarga carregamentoDescarga){
		Map<String,Object> descargaVolumes = new HashMap<>();

		this.storeCarregamentoDescargaVolume(carregamentoDescarga, volumeNotaFiscal, null);

		if(controleCarga.getTpControleCarga().getValue().equals("V")){
			descargaVolumes = executeDescargaVolume(volumeNotaFiscal, controleCarga, ConstantesSim.TP_SCAN_FISICO);
		}else if(controleCarga.getTpControleCarga().getValue().equals("C")){
			descargaVolumes = executeDescargaVolumeColetaEntrega(volumeNotaFiscal, controleCarga, ConstantesSim.TP_SCAN_FISICO);
		}

		return descargaVolumes;
	}
		
	
	
	/**
	 * Realiza da descarga individual dos volumes da coleta
	 * @param controleCarga
	 */
	public Map<String,Object> executeDescargaVolumeColetaEntrega(VolumeNotaFiscal volumeNotaFiscal,
                                                                 ControleCarga controleCarga,
                                                                 String tpScan){
		Map<String, Object> map = new HashMap<>();
		ManifestoEntregaVolume manifestoEntregaVolume= manifestoEntregaVolumeService.findByIdVolumeAndIdControleCarga(volumeNotaFiscal.getIdVolumeNotaFiscal(),
				controleCarga.getIdControleCarga());
		
		//“Em Descarga”  --> 37/38/69
		Short cdLocalizacao = volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria(); 
		if( !cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_RETORNADA_AGUARDANDO_DESCARGA) &&
				!cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_RECUSA_EM_TRANSITO) &&
				!cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_RETORNADA_EM_DESCARGA) &&
				!cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_AGUARDANDO_DESCARGA) &&
				!cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_EM_DESCARGA)){
			map = executeIniciarDescargaVolumeColetaEntregaNaoPrevistoNaFilial(volumeNotaFiscal, controleCarga, manifestoEntregaVolume, ConstantesSim.TP_SCAN_FISICO);
		}else{
			boolean isVolumePedidoColeta = pedidoColetaService.isVolumePrevistoDescargaAereo(volumeNotaFiscal.getIdVolumeNotaFiscal(), 
					controleCarga.getIdControleCarga(),
					SessionUtils.getFilialSessao().getIdFilial(),
					ConstantesSim.CD_MERCADORIA_EM_DESCARGA);
			
			if(isVolumePedidoColeta){
				this.executeIniciarDescargaVolumePedidoColeta(volumeNotaFiscal, ConstantesSim.TP_SCAN_FISICO);
			}else{
				executeIniciarDescargaVolumeColetaEntregaPrevistoNaFilial(volumeNotaFiscal, manifestoEntregaVolume, tpScan);
			}
		}
		
		return map;
	}
	
	/**
	 * Usado para buscar o pai de determinado dispositivo de unitização
	 * @param idDispositivoPai
	 * @return
	 */
	private DispositivoUnitizacao findPaiDispUnit(Long idDispositivoPai){
		DispositivoUnitizacao dispUnitPai = dispositivoUnitizacaoService.findById(idDispositivoPai);
		return dispUnitPai.getDispositivoUnitizacaoPai();
	}
	
	/**
	 * Regra 45.03.04.04 4 b
	 * @param controleCarga
	 */
	public Map<String, Object> executeDescargaByDispositivoUnitizacao(ControleCarga controleCarga,
                                                                      DispositivoUnitizacao dispositivoUnitizacao){
		Map<String, Object> map = new HashMap<>();
		//Este validateDescargaDispositivoUnitizacao possui a mesma recursividade. 
		//Mas essa chamada ocorre necessariamente dentro desse executeDescargaByDispositivoUnitizacao pois é necessaria essa validação 
		//sempre que for executar esse executeDescargaByDispositivoUnitizacao, isso garante que não será armazenado nenhum dispositivo de unitizacao
		//que possua um Volume ou outro Dispositivo de Unitizacao, que não cumpra as regras pre-definidas.
		map = validateDescargaDispositivoUnitizacao(controleCarga, dispositivoUnitizacao);
		
		if(map == null || map.isEmpty()){
			executeDescargaDispUnit(controleCarga, dispositivoUnitizacao, ConstantesSim.TP_SCAN_FISICO);
		}
		
		return map;
		
		
	}
	
	
	/**
	 * Regra 45.03.04.04 4 b
	 * @param controleCarga
	 */
	private void executeDescargaDispUnit(ControleCarga controleCarga, DispositivoUnitizacao dispositivoUnitizacao, String tpScan) {

		List<DispositivoUnitizacao> lstDispositivoUnitizacao = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		if(!lstDispositivoUnitizacao.isEmpty()){
			for (DispositivoUnitizacao dispositivoUnitizacaoFilho : lstDispositivoUnitizacao) {
				executeDescargaDispUnit(controleCarga, dispositivoUnitizacaoFilho, ConstantesSim.TP_SCAN_CASCADE);
			}
		}
		
		List<VolumeNotaFiscal> volumeNotaFiscalList =
			volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		if(!volumeNotaFiscalList.isEmpty()){

			CarregamentoDescarga carregamentoDescarga =
				carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(), "D");
			
			/**
			 * 45.03.04.04 4 b 2 b ii
			 * ii.	Caso todos os volumes sejam considerados para descarga na filial do usuário logado (conforme regras), entao:
				1.	Fazer os procedimento de descarga de volume como descrito nos procedimentos, porém, para cada volume descarregado, 
					ao gerar o evento de descarga, o tipo dele deve ser Cascade Scan e não Scan Físico.
				2.	Gerar um evento de descarga para o dispotivo do tipo Scan Físico
				3.	Alterar a localização do dispositivo para No Terminal --> 24
			 */
			for(VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscalList ){
				this.storeCarregamentoDescargaVolume(carregamentoDescarga, volumeNotaFiscal, dispositivoUnitizacao);
				
				if(controleCarga.getTpControleCarga().getValue().equals("V")) {

					this.storeIdentificarSobra(volumeNotaFiscal);
					ManifestoNacionalVolume manifestoNacionalVolume = manifestoNacionalVolumeService.findByIdVolumeAndIdControleCarga(volumeNotaFiscal.getIdVolumeNotaFiscal(), controleCarga.getIdControleCarga());
					executeIniciarDescargaVolumePrevistoNaFilial(volumeNotaFiscal, manifestoNacionalVolume, ConstantesSim.TP_SCAN_CASCADE);

				}else if(controleCarga.getTpControleCarga().getValue().equals("C")){
					
					boolean isVolumePedidoColeta = pedidoColetaService.isVolumePrevistoDescargaAereo(volumeNotaFiscal.getIdVolumeNotaFiscal(), 
							controleCarga.getIdControleCarga(),
							SessionUtils.getFilialSessao().getIdFilial(),
							ConstantesSim.CD_MERCADORIA_EM_DESCARGA);
					
					if(isVolumePedidoColeta){
						this.executeIniciarDescargaVolumePedidoColeta(volumeNotaFiscal, ConstantesSim.TP_SCAN_FISICO);
					}else{
						ManifestoEntregaVolume manifestoEntregaVolume = getManifestoEntregaVolumeService().findByIdVolumeAndIdControleCarga(volumeNotaFiscal.getIdVolumeNotaFiscal(), controleCarga.getIdControleCarga());
						executeIniciarDescargaVolumeColetaEntregaPrevistoNaFilial(volumeNotaFiscal, manifestoEntregaVolume, tpScan);
					}
				}
			}
		}

		//30 -> Evento de Descarga (Fim de descarga)
		// 31 -> Evento de Descarga (Fim de descarga - Coleta/Entrega)
		Short eventoDescarga = ConstantesSim.EVENTO_DESCARGA_CONCLUIDA;
		if ("C".equals(controleCarga.getTpControleCarga())) {
			eventoDescarga = ConstantesSim.EVENTO_FIM_DESCARGA;
		}

		eventoDispositivoUnitizacaoService.generateEventoDispositivo(
				dispositivoUnitizacao.getIdDispositivoUnitizacao(), eventoDescarga, tpScan);
	}
	
	/**
	 * Usado para validar se o Dispositivo de Unitizacao e os volumes estão passiveis de serem descarregados.
	 * 
	 */
	public Map<String, Object> validateDescargaDispositivoUnitizacao(ControleCarga controleCarga, DispositivoUnitizacao dispositivoUnitizacao){

		Map<String, Object> map = null;
		try {
			List<VolumeNotaFiscal> volumeNotaFiscalList =
					volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao());

			List<DispositivoUnitizacao> lstDispositivoUnitizacao = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());

			if (!lstDispositivoUnitizacao.isEmpty()) {
				for (DispositivoUnitizacao dispositivoUnitizacaoFilho : lstDispositivoUnitizacao) {
					validateDescargaDispositivoUnitizacao(controleCarga, dispositivoUnitizacaoFilho);
				}
			}

			//se codigo localizacao mercadoria eh 24 = no terminal, mostra msg
			if (dispositivoUnitizacao.getLocalizacaoFilial() != null
					&& dispositivoUnitizacao.getLocalizacaoFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())
					&& dispositivoUnitizacao.getLocalizacaoMercadoria() != null
					&& dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("24"))) {


				//LMS-45062 = dispositivo ja descarregado
				throw new BusinessException("LMS-45062");


			} else if (!volumeNotaFiscalList.isEmpty()) {
				//45.03.04.04 4 b 2 b
				for (VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscalList) {
					/**
					 * 1.	Omitir a regra de verificação de unitização.
					 * 2.	Se o volume não for previsto para descarga na filial do usuário logado (conforme regras),
					 *      entao não apresentar as mensagens descritas na própria regra e sim a seguinte mensagem:
					 *      LMS-45010 Atenção, um ou mais volumes não estão previstos para desembarque nesta unidade.
					 *      Favor descarregar volume a volume.
					 */
					//“Em Descarga”  --> 34  Se os volumes não estiverem em descarga significa que não pertencem aquele destino
					if (controleCarga.getTpControleCarga().getValue().equals("V")) {
						if (!volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("34"))) {

							// LMS-45010 Atenção, um ou mais volumes não estão previstos para desembarque nesta unidade.
							//      Favor descarregar volume a volume.
							throw new BusinessException("LMS-45010");
						}
					} else if (controleCarga.getTpControleCarga().getValue().equals("C") &&
							!volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("37")) &&
							volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("69")) &&
							!volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_EM_DESCARGA) &&
							!volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("33"))) {

						// LMS-45010 Atenção, um ou mais volumes não estão previstos para desembarque nesta unidade.
						//      Favor descarregar volume a volume.
						throw new BusinessException("LMS-45010");

					}
				}

			}

		} catch (BusinessException e ){
			throw new BusinessException("LMS-45010");
		}
		return map;
	}




	/**
	 * REGRA 45.03.04.04 Item 4 a ii 
	 * Caso o Volume lido NÃO ESTEJA previsto para descarga na filial
	 */
	private Map<String, Object> executeIniciarDescargaVolumeNaoPrevistoNaFilial(VolumeNotaFiscal volumeNotaFiscal,
                                                                                ManifestoNacionalVolume manifestoNacionalVolume,
                                                                                String tpScan){
		
		Map<String, Object> map = new HashMap<>();
		
		//45.03.04.04 Item 4 ii 1  - Verifica se o Volume está no Controle de Carga a partir do Pré-Manifesto
		if( manifestoNacionalVolume != null 
				&& manifestoNacionalVolume.getManifestoViagemNacional() !=null 
				&& manifestoNacionalVolume.getManifestoViagemNacional().getManifesto()!=null){
			
			if( "EF".equalsIgnoreCase( manifestoNacionalVolume.getManifestoViagemNacional().getManifesto().getTpStatusManifesto().getValue() )){

				Long milis = System.currentTimeMillis();
				eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), Short.valueOf("406"), tpScan);
				milis = System.currentTimeMillis() - milis;
				
				//O volume está previsto para desembarcar na filial {0} e não pode desembarcar nesta filial.
				throw new BusinessException("LMS-45002" ,
						new Object[]{ manifestoNacionalVolume.getManifestoViagemNacional().getManifesto().getFilialByIdFilialDestino().getSiglaNomeFilial() } );
				
			}else{
				//O destino deste volume é a filial {0}, e a localização atual no sistema é {1}. 
				//Deseja descarregar o volume na sua unidade e ajustar a localização do volume?
					map.put("message", configuracoesFacade.getMensagem("LMS-45003", new Object[]{
						manifestoNacionalVolume.getManifestoViagemNacional().getManifesto().getFilialByIdFilialDestino().getSiglaNomeFilial(), 
						SessionUtils.getFilialSessao().getSiglaNomeFilial()} ));
				map.put("key", "LMS-45003");						
				//CQPRO00026985
				executeEventoVolumeLocalizacao(volumeNotaFiscal);
				
			}
		}else if( manifestoNacionalVolume == null ){
			// LMS-45004 - O volume não pertence ao Controle de Carga. A localização atual no sistema é {1}. 
			// Deseja descarregar o volume na sua unidade e ajustar a localização do volume?
			String localizacao = configuracoesFacade.getMensagem("semLocalizacao");
			if (volumeNotaFiscal.getLocalizacaoMercadoria() != null && volumeNotaFiscal.getLocalizacaoFilial() != null) {
				localizacao = volumeNotaFiscal.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria().toString()
						+ " - " + volumeNotaFiscal.getLocalizacaoFilial().getSgFilial();
			}
			map.put("message", configuracoesFacade.getMensagem("LMS-45004", new Object[]{localizacao} ));
			map.put("key", "LMS-45004");						
			//CQPRO00026985
			
			Long milis = System.currentTimeMillis();
			eventoVolumeService.generateEventoVolume(volumeNotaFiscal, Short.valueOf("406"), tpScan, null);
			milis = System.currentTimeMillis() - milis;
			log.warn("[MWW] " + " [" + milis + "ms] idVolume = " + volumeNotaFiscal.getIdVolumeNotaFiscal().toString()
					+  " eventoVolumeService.generateEventoVolume CD_EVENTO_VOLUME_LIDO");
			
			executeEventoVolumeLocalizacao(volumeNotaFiscal);
		}
		
		
		return map;
	}


	
	
	/**
	 * Executa as validações da descarga da Coleta/Entrega
	 */
	private Map<String, Object> executeIniciarDescargaVolumeColetaEntregaNaoPrevistoNaFilial(VolumeNotaFiscal volumeNotaFiscal,
                                                                                             ControleCarga controleCarga,
                                                                                             ManifestoEntregaVolume manifestoEntregaVolume,
                                                                                             String tpScan){
		
		Map<String, Object> map = new HashMap<>();
		
		if( manifestoEntregaVolume != null 
				&& manifestoEntregaVolume.getManifestoEntrega() !=null 
				&& manifestoEntregaVolume.getManifestoEntrega().getManifesto()!=null) { 
			
			/**
			 * CQPRO00026139
			 * Ajustar o volume para assumir a localização No Terminal, na filial do usuário Logado.
			 * Se o CTRC estiver com informação de ENTREGUE ou ENTREGUE AERO, então ajustar para receber ocorrência de Entrega Parcial (cod 120), e 
			 * permanecer como Entrega Realizada, com complemento de localização (ob_localizacao_complemento), “parcial”.
			 */
			if(manifestoEntregaVolume.getOcorrenciaEntrega() != null
					&& ("E".equals(manifestoEntregaVolume.getOcorrenciaEntrega().getTpOcorrencia().getValue())
						|| "A".equals(manifestoEntregaVolume.getOcorrenciaEntrega().getTpOcorrencia().getValue()))){
				
				Conhecimento conhecimento = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento();
				if ((conhecimento.getLocalizacaoMercadoria() != null
					&& !conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA)
					&& !conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_CANCELADA)
					&& !conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesExpedicao.CD_MERCADORIA_DEVOLVIDA)
					&& !conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesExpedicao.CD_MERCADORIA_REFATURADA)
					&& conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().intValue() != 39)
					|| conhecimento.getLocalizacaoMercadoria() == null){
					conhecimento.setObComplementoLocalizacao("Parcial");					
				}
				conhecimentoService.store(conhecimento);
				
				manifestoEntregaVolume.setOcorrenciaEntrega(null);
				manifestoEntregaVolumeService.store(manifestoEntregaVolume);
	
				if ("V".equals(controleCarga.getTpControleCarga())) {
					storeDescargaVolume(volumeNotaFiscal.getIdVolumeNotaFiscal());			
				}else{
					storeDescargaEntregaVolume(volumeNotaFiscal.getIdVolumeNotaFiscal());
					
					List<VolumeNotaFiscal> volumes =
							volumeNotaFiscalService.findByIdConhecimentoAndIdLocalizacaoFilial(conhecimento.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial());

					long volumesInTerminal = volumes.stream().
							filter(obj  -> ConstantesSim.CD_MERCADORIA_NO_TERMINAL_MERCADORIA_RETORNADA.equals(obj.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()))
							.count();
					
					if(volumes.size() == volumesInTerminal){
						Awb awb = awbService.findByDoctoServicoAndTpStatusAwb(conhecimento.getIdDoctoServico(), ConstantesAwb.TP_STATUS_PRE_AWB);
						if(awb != null && awb.getFilialByIdFilialOrigem().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())){
							String nrMan = FormatUtils.formatSgFilialWithLong(manifestoEntregaVolume.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem().getSgFilial(),
									manifestoEntregaVolume.getManifestoEntrega().getNrManifestoEntrega().longValue());
							
							incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(ConstantesSim.EVENTO_FIM_DESCARGA, conhecimento.getIdDoctoServico(),
									SessionUtils.getFilialSessao().getIdFilial(), nrMan, JTDateTimeUtils.getDataHoraAtual(), null, SessionUtils.getFilialSessao().getSiglaNomeFilial(), "MAE");
							
							CtoAwb ca = ctoAwbService.findCtoAwb(conhecimento.getIdDoctoServico(), awb.getIdAwb());
							ctoAwbService.removeById(ca.getIdCtoAwb());
						}
					}
				}
							
			} else {
				executeEventoVolumeLido(volumeNotaFiscal.getIdVolumeNotaFiscal(), tpScan);
			}
		} else if(manifestoEntregaVolume == null) {
			// LMS-45004 - O volume não pertence ao Controle de Carga. A localização atual no sistema é {1}. 
			// Deseja descarregar o volume na sua unidade e ajustar a localização do volume?
			map.put("message", configuracoesFacade.getMensagem("LMS-45004", new Object[]{ 
					volumeNotaFiscal.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria()
					+ " - " + volumeNotaFiscal.getLocalizacaoFilial().getSgFilial()} ));
			map.put("key", "LMS-45004");						
			
			executeEventoVolumeLocalizacao(volumeNotaFiscal);
		}

		return map;
	}

	/**
	 * Usado unicamente para mudar a localização do volume para no terminal e gerar o evento de lido
	 * 
	 * "30" - Fim de Carregamento? No Terminal?
	 * ""-
	 * @param idVolumeNotaFiscal
	 */
	public void storeDescargaVolume(Long idVolumeNotaFiscal){
		VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findVolumeById(idVolumeNotaFiscal);
		
		// 30 Evento de Descarga
		eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, ConstantesSim.TP_SCAN_FISICO);
	}
	
	public void storeDescargaEntregaVolume(Long idVolumeNotaFiscal){
		// Verificação da existência do evento de "FIM DE DESCARGA" no volume foi removido para atender a demanda LMS-6727
		// 31 Evento de Descarga
		eventoVolumeService.generateEventoVolume(idVolumeNotaFiscal, ConstantesSim.EVENTO_FIM_DESCARGA, ConstantesSim.TP_SCAN_FISICO);
	}

	/**
	 * REGRA 45.03.04.04 Item 4 a iii 
	 * Realiza a descarga caso o Volume lido ESTEJA previsto para descarga na filial
	 */
	private void executeIniciarDescargaVolumePrevistoNaFilial( VolumeNotaFiscal volumeNotaFiscal,
														ManifestoNacionalVolume manifestoNacionalVolume,
														String tpScan){
					
		Long idManifesto = manifestoNacionalVolume.getManifestoViagemNacional().getManifesto().getIdManifesto();
		Long idDoctoServico = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico();			
		//REGRA 45.03.04.04 Item 4 a iii 3 - Descarga
		//a - Gera evento fim de descarga
		//b - alterar localização
		eventoVolumeService.generateEventoVolume(volumeNotaFiscal, ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, tpScan);
		
		//c - se não existem volumes com a situação "Em Descarga"	
		//lista de PreManifestoVolume que possuem volumes com a situação em descarga
		
		List<ManifestoNacionalVolume> lstManifestoNacionalVolume =
			manifestoNacionalVolumeService.findByDoctoServicoAndManifestoAndLocalizacaoVolume(
					idDoctoServico, idManifesto, ConstantesSim.CD_MERCADORIA_EM_DESCARGA);
		//Se todos os volumes do docto de serviço do volume lido, já foram descarregados 
		if(lstManifestoNacionalVolume == null || lstManifestoNacionalVolume.isEmpty()) {
			//REGRA 45.03.04.04 Item 4 a iii 3 c  - gera evento fim de descarga para os documentos
				DoctoServico doctoServico = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento();
				
				if ((doctoServico.getLocalizacaoMercadoria() != null 
						&& !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA)
						&& !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_CANCELADA)
						&& !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesExpedicao.CD_MERCADORIA_DEVOLVIDA)
						&& !doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesExpedicao.CD_MERCADORIA_REFATURADA)
						&& doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().intValue() != 39) 
						|| doctoServico.getLocalizacaoMercadoria() == null){
					
					//Caso o manifesto de viagem que está sendo descarregado e vinculado ao volume lido possui vínculo também com o documento de serviço deste volume,
					//então gera evento de fim de descarga para docto de serviço (evento 30).
					ManifestoNacionalVolume manifestoNacionalVolVincDocServico =
							manifestoNacionalVolumeService.findByIdManifestoViagemNacionalVinculadoDocServico(
									manifestoNacionalVolume.getManifestoViagemNacional().getIdManifestoViagemNacional(),
									manifestoNacionalVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal());						
							
					if (manifestoNacionalVolVincDocServico != null){
						
						Short cdEventoFimDescarga = ConstantesSim.EVENTO_DESCARGA_CONCLUIDA;
						Short cdLocalizacaoMercadoria = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getLocalizacaoMercadoria() != null ? volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() : null;
						
						if(!ConstantesSim.CD_MERCADORIA_EM_DESCARGA.equals(cdLocalizacaoMercadoria)
								&& !ConstantesSim.CD_MERCADORIA_RETORNADA_EM_DESCARGA.equals(cdLocalizacaoMercadoria)){
							cdEventoFimDescarga = ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO;
						}
						
						carregamentoDescargaService.generateEventoDocumentoServico(manifestoNacionalVolume.getManifestoViagemNacional().getManifesto(),
																				   volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento(),
																				   cdEventoFimDescarga);

					}
				}
		}
	}
		
	public void executeIniciarDescargaVolumeColetaEntregaPrevistoNaFilial(VolumeNotaFiscal volumeNotaFiscal,
                                                                          ManifestoEntregaVolume manifestoEntregaVolume,
                                                                          String tpScan) {

		Long idManifesto = manifestoEntregaVolume.getManifestoEntrega().getManifesto().getIdManifesto();
		DoctoServico docto = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento();
		
		// Verificação da existência do evento de "FIM DE DESCARGA" no volume foi removido para atender a demanda LMS-6727
		// 31 Evento de Descarga
		String obs = null;
		eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), ConstantesSim.EVENTO_FIM_DESCARGA, tpScan, obs);
					
		//c - se não existem volumes com a situação "Em Descarga"			
		//lista de PreManifestoVolume que possuem volumes com a situação em descarga
		List<Short> lstCdLocMerc = new ArrayList<Short>(){{
			add(ConstantesSim.CD_MERCADORIA_RETORNADA_AGUARDANDO_DESCARGA);
			add(ConstantesSim.CD_MERCADORIA_RECUSA_EM_TRANSITO);
			add(ConstantesSim.CD_MERCADORIA_RETORNADA_EM_DESCARGA);
			add(ConstantesSim.CD_MERCADORIA_AGUARDANDO_DESCARGA);
			add(ConstantesSim.CD_MERCADORIA_EM_DESCARGA);
		}};
		
		List<ManifestoEntregaVolume> lstManifestoEntregaVolume =
			manifestoEntregaVolumeService.findByDoctoServicoAndManifestoAndLocalizacaoVolume(SessionUtils.getFilialSessao().getIdFilial(), idManifesto, docto.getIdDoctoServico(), lstCdLocMerc);
		
		//CQPRO00026139 - Baixa pelo MWW
		if(manifestoEntregaVolume.getOcorrenciaEntrega()!=null
				&& "E".equals(manifestoEntregaVolume.getOcorrenciaEntrega().getTpOcorrencia().getValue())
				&& "P".equals(manifestoEntregaVolume.getOcorrenciaEntrega().getTpOcorrencia().getValue())){
			//cdOcorrenciaEntrega = 20 - pt_BR»Entrega prejudicada por horário¦
			OcorrenciaEntrega ocorrenciaEntrega = ocorrenciaEntregaService.findOcorrenciaEntrega(Short.valueOf("20"));
			
			manifestoEntregaVolume.setOcorrenciaEntrega(ocorrenciaEntrega);
			manifestoEntregaVolumeService.store(manifestoEntregaVolume);
		}
		
		//REGRA 45.03.04.04 Item 4 a iii 3 c  - gera evento fim de descarga para os documentos
		if(lstManifestoEntregaVolume.isEmpty()){
		
		List<Short> cdsLocalizacao = new ArrayList<>();
		CollectionUtils.addAll(cdsLocalizacao, new Short[]{ 
				ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA,
				ConstantesSim.CD_MERCADORIA_CANCELADA,
				ConstantesExpedicao.CD_MERCADORIA_DEVOLVIDA,
				ConstantesExpedicao.CD_MERCADORIA_REFATURADA,
				ConstantesSim.CD_MERCADORIA_CLIENTE_INDENIZADO
		});
		
			if ((docto.getLocalizacaoMercadoria() == null || !cdsLocalizacao.contains(docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()))){
				
				Short cdEventoFimDescarga = ConstantesSim.EVENTO_DESCARGA_CONCLUIDA;
				Short cdLocalizacaoMercadoria = docto.getLocalizacaoMercadoria() != null ? docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() : null;
				
				if(!ConstantesSim.CD_MERCADORIA_EM_DESCARGA.equals(cdLocalizacaoMercadoria)
						&& !ConstantesSim.CD_MERCADORIA_RETORNADA_EM_DESCARGA.equals(cdLocalizacaoMercadoria)){
					cdEventoFimDescarga = ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO;
				}

				carregamentoDescargaService.generateEventoDocumentoServico(manifestoEntregaVolume.getManifestoEntrega().getManifesto(),
						volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento(), cdEventoFimDescarga);
			}

			}
		}

	public void desunitizaDispositivo(Long idDispositivoUnitizacao, String token) {

		try {
			this.criaSessao(token);

			DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(idDispositivoUnitizacao);

			List<DispositivoUnitizacao> lstDispUnit = new ArrayList<>();

			lstDispUnit.add(dispositivoUnitizacao);

			if (dispositivoUnitizacao.getDispositivoUnitizacaoPai() != null) {

				DispositivoUnitizacao dispUnitizador = dispositivoUnitizacao.getDispositivoUnitizacaoPai();
				while (dispUnitizador != null) {
					unitizacaoService.executeEventoDispositivoDesunitizadoTpScan(dispUnitizador.getIdDispositivoUnitizacao(), ConstantesSim.TP_SCAN_CASCADE);
					dispUnitizador = findPaiDispUnit(dispUnitizador.getIdDispositivoUnitizacao());
				}

				unitizacaoService.storeDesunitizarParcial(dispositivoUnitizacao.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao(), null, lstDispUnit);
				unitizacaoService.executeEventoDispositivoDesunitizadoTpScan(dispositivoUnitizacao.getIdDispositivoUnitizacao(), ConstantesSim.TP_SCAN_FISICO);
			}
		}finally {
			this.destroiSessao();
		}
	}
	
	/**
	 * Regra 45.03.04.04 4 b i
	 * 	Veerifica se o dispositivo está vinculado ao controle de carga
	 * @param idControleCarga
	 * @param idDispositivoUnitizacao
	 * @param tpOperacao
	 * @return
	 */
	public boolean isDispositivoVinculadoControleCarga(Long idControleCarga, Long idDispositivoUnitizacao,	String tpOperacao){
		
		ControleCarga controleCarga = controleCargaService.findByIdAndTpOperacaoAndDispositivoUnitizacao(idControleCarga,	idDispositivoUnitizacao, tpOperacao);
		
		boolean hasDU = false;
		
		if( controleCarga != null ){
			hasDU = true;
		}else{
			List<PedidoColeta> pedidos = pedidoColetaService.findPedidoColetaByIdControleCargaByTpStatusColeta(idControleCarga, null);
			
			for (PedidoColeta pc : pedidos) {
				if(pc.getTpPedidoColeta() != null && "AE".equals(pc.getTpPedidoColeta().getValue())) {
					List<Long> ids = this.getIdsDispositivosFromPedidoColeta(pc);
					if(ids.contains(idDispositivoUnitizacao)){
						hasDU = true;
					}
				}
			}
		}

		return hasDU;
	}
	
	private List<Long> getIdsDispositivosFromPedidoColeta(PedidoColeta pc) {

		List<Conhecimento> ctos = pedidoColetaService.getCtosByPedidoColeta(pc);
		List<Long> ids = new ArrayList<>();

		for (Conhecimento cto : ctos) {
			List<VolumeNotaFiscal> volumeNotaFiscais = volumeNotaFiscalService.findByIdConhecimento(cto.getIdDoctoServico());
			for (VolumeNotaFiscal vnf : volumeNotaFiscais) {
				if(vnf.getDispositivoUnitizacao() != null){
					ids.add(vnf.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
					if(vnf.getDispositivoUnitizacao().getDispositivoUnitizacaoPai() != null){
						ids.add(vnf.getDispositivoUnitizacao().getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao());
					}
				}
			}
		}
		
		return ids;
	}

	/**
	 * Regra 45.03.04.04 Item 3 a i - Iniciar Descarga de Viagem
	 * Valida se extistem documentos sem ocorrencias de baixa, caso exista envia uma BusinessException
	 * @param controleCarga
	 */
	public void validateDoctoServico(ControleCarga controleCarga, Long idFilialUsuarioLogado ){
		ocorrenciaEntregaService.validateDoctoServicoComOcorrenciaVinculada( controleCarga.getIdControleCarga(), idFilialUsuarioLogado);
	}
	
	public Map storeDescargaDispositivo(String barcode, Long idControleCarga, String token) {
		criaSessao(token);
		try {
			List<Map<String, Object>> confirmacoes = new ArrayList<>();
			ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
			DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findByBarcode(barcode);
			Filial filial = filialService.findById(SessionUtils.getFilialSessao().getIdFilial());

			Map retorno = new HashMap();

			Map<String, Object> confirm = new HashMap<>();

			if (dispositivoUnitizacao != null) {
				if (dispositivoUnitizacao.getDispositivoUnitizacaoPai() == null) {
					if (isDispositivoVinculadoControleCarga(controleCarga.getIdControleCarga(), dispositivoUnitizacao.getIdDispositivoUnitizacao(), "C")) {
						CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(), "D");
						// E - Em Conferência MWW
						// O - Concluído MWW
						if (carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("O")) || carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("F"))) {
							//LMS-45050	A descarga desse carregamento já foi concluída.
							throw new BusinessException("LMS-45050");
						} else if (carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("I"))) {
							Map<String, Object> executeDescargaByDispositivoUnitizacao = executeDescargaByDispositivoUnitizacao(controleCarga, dispositivoUnitizacao);
							if (executeDescargaByDispositivoUnitizacao != null && !executeDescargaByDispositivoUnitizacao.isEmpty()) {
								confirmacoes.add(executeDescargaByDispositivoUnitizacao);
							}
						}
					} else {
						//isso significa que o controle de carga é diferente do controle de carga do dispositivo vinculado
						//LMS-45007 “O dispositivo não pertence ao Controle de Carga. A localização atual no sistema é {1}.
						//Deseja descarregar o dispositivo e seus volumes na sua unidade e ajustar a localização do volume ?”
						confirm.put("message", configuracoesFacade.getMensagem("LMS-45007",
								new Object[]{dispositivoUnitizacao.getLocalizacaoFilial() != null ?
										dispositivoUnitizacao.getLocalizacaoFilial().getSgFilial() : ""}));
						confirm.put("key", "LMS-45007");
						confirmacoes.add(confirm);

					}
					if (!confirmacoes.isEmpty()) {
						retorno.put("messages", confirmacoes);
					}
				}

				Map dispositivoMapped = unitizacaoService.findMapDispositivoUnitizacao(dispositivoUnitizacao, idControleCarga, "D");

				verificaConhecimentoCompleto(controleCarga, (List<Map>) dispositivoMapped.get("conhecimentos"));
				verificaDispositivoCompleto(controleCarga, (List<Map>) dispositivoMapped.get("dispositivos"));

				retorno.putAll(dispositivoMapped);
			} else {
				throw new BusinessException("LMS-45018");
			}
			return retorno;
		}finally {
			destroiSessao();
		}
	}
	
	private void verificaDispositivoCompleto(ControleCarga controleCarga, List<Map> list) {
		if(list != null){
			for (Map<String, Object> disp : list) {
				verificaConhecimentoCompleto(controleCarga, (List<Map>)disp.get("conhecimentos"));
				verificaDispositivoCompleto(controleCarga, (List<Map>)disp.get("dispositivos"));
			}
		}
	}
	private void verificaConhecimentoCompleto(ControleCarga controleCarga, List<Map> list) {
		if(list != null){
			for (Map<String, Object> con : list) {
				DoctoServico doctoServico = doctoServicoService.findById(Long.parseLong(con.get("idDoctoServico").toString()));
				Integer qtVolumesEmDescargaByConhecimento = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaConhecimento(controleCarga.getIdControleCarga(),
						doctoServico.getIdDoctoServico(), "D");

				con.put("qtVolumes", doctoServico.getQtVolumes());
				con.put("completo", (qtVolumesEmDescargaByConhecimento.compareTo(doctoServico.getQtVolumes()) == 0));
			}
		}
	}

	public Map<String, Object> storeDescargaVolume(String barcode, Long idControleCarga, String token) {
		this.criaSessao(token);
		try {
			ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
			VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findVolumeByBarCodeUniqueResult(barcode);
			Map<String, Object> volumeMapped = new HashMap<>();
			List<Map<String, Object>> volumes = new ArrayList<>();
			Map<String, Object> retorno = new HashMap<>();

			if (ConstantesExpedicao.TP_VOLUME_MESTRE.equals(volumeNotaFiscal.getTpVolume())) {
				throw new BusinessException("LMS-45081");
			}

			Integer totalControles = controleCargaService.countByVolumeEmDescargaFilial(volumeNotaFiscal.getIdVolumeNotaFiscal(),
					SessionUtils.getFilialSessao().getIdFilial(), idControleCarga);
			if ((Integer.valueOf(0)).compareTo(totalControles) != 0) {
				//LMS-45063 - Este volume já está descarregado.
				throw new BusinessException("LMS-45063");
			}

			volumeMapped.put("ocorrenciaEntrega", getOcorrenciaEntregaMapped(
					ocorrenciaEntregaService.findLastOcorrenciaEntregaByIdVolumeAndFilial(volumeNotaFiscal.getIdVolumeNotaFiscal(), SessionUtils.getFilialSessao().getIdFilial())));

			//Regra 45.03.04.04 Item 4 i - verifica se o Volume lido está unitizado
			if (volumeNotaFiscal.getDispositivoUnitizacao() != null) {
				volumeMapped.put("idDispositivoUnitizacao", volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
				//"34" diferente de 34 - VolumeNaoPrevistoNaFilial
				volumeMapped.put("cdLocalizacaoMercadoria", volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
				if (volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao() != null) {
					DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
					volumeMapped.put("idDispositivoUnitizacaoAvo", dispositivoUnitizacao.getDispositivoUnitizacaoPai() != null ?
							dispositivoUnitizacao.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao() : null);
				}
			} else {
				CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(), "D");

				// E - Em Conferência MWW
				// O - Concluído MWW
				List<Map<String, Object>> confirmacoes = new ArrayList<>();
				if ("O".equals(carregamentoDescarga.getTpStatusOperacao().getValue())
						|| "F".equals(carregamentoDescarga.getTpStatusOperacao().getValue())) {
					//LMS-45050	A descarga desse carregamento já foi concluída.
					throw new BusinessException("LMS-45050");
				} else if ("I".equals(carregamentoDescarga.getTpStatusOperacao().getValue())) {
					Map<String, Object> executeDescargaVolume = executeDescargaVolume(volumeNotaFiscal, controleCarga, carregamentoDescarga);
					if (!executeDescargaVolume.isEmpty()) {
						confirmacoes.add(executeDescargaVolume);
					}
				}

				if (!confirmacoes.isEmpty()) {
					retorno.put("messages", confirmacoes);
				}

				this.volumeSobraService.validateStoreVolumeSobra(volumeNotaFiscal,
						carregamentoDescarga,
						controleCarga.getIdControleCarga(),
						controleCarga.getTpControleCarga().getValue(),
						SessionUtils.getFilialSessao(),
						usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
			}

			NotaFiscalConhecimento notaFiscalConhecimento = notaFiscalConhecimentoService.findByIdConhecimento(volumeNotaFiscal.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento());

			Conhecimento docto = notaFiscalConhecimento.getConhecimento();
			Long idDoctoServico = docto.getIdDoctoServico();
			retorno.put("sgFilialDocumento", docto.getFilialOrigem().getSgFilial());
			retorno.put("nrConhecimento", volumeNotaFiscal.getNrConhecimento());
			retorno.put("tpDoctoServico", docto.getTpDoctoServico().getDescriptionAsString());

			Integer qtVolumesEmDescarga = this.countVolumesCarregamentoDescarga(idControleCarga, idDoctoServico, volumeNotaFiscal.getDispositivoUnitizacao());
			Integer qtVolumesEmDescargaByConhecimento = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga,
					idDoctoServico, "D");
			retorno.put("qtVolumesDescarregados", qtVolumesEmDescarga);

			retorno.put("qtVolumes", docto.getQtVolumes());

			if (controleCarga.getTpControleCarga().getValue().equals("V")) {
				this.storeIdentificarSobra(volumeNotaFiscal);
			}
			retorno.put("completo", qtVolumesEmDescargaByConhecimento.compareTo(docto.getQtVolumes()) == 0);
			retorno.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volumeNotaFiscal.getIdVolumeNotaFiscal()));

			if (controleCarga.getTpControleCarga().getValue().equals("C")) {
				Boolean isVolumeSobra = !this.manifestoService.findManifestoVolumeSobra(idControleCarga, volumeNotaFiscal.getIdVolumeNotaFiscal(), SessionUtils.getFilialSessao().getFilialByIdFilialResponsavel().getIdFilial(), "E");
				retorno.put("isVolumeSobra", isVolumeSobra);
			} else {
				retorno.put("isVolumeSobra", volumeNotaFiscalService.validateIsVolumeSobra(volumeNotaFiscal, controleCarga, SessionUtils.getFilialSessao().getFilialByIdFilialResponsavel()));
			}

			volumeMapped.put("nrSequencia", volumeNotaFiscal.getNrSequencia());
			volumeMapped.put("idVolumeNotaFiscal", volumeNotaFiscal.getIdVolumeNotaFiscal());
			volumes.add(volumeMapped);

			retorno.put("volumes", volumes);

			return retorno;
		}finally {
			this.destroiSessao();
		}
	}
	
	private void executeIniciarDescargaVolumePedidoColeta(VolumeNotaFiscal volumeNotaFiscal, String tpScan) {
		
		Long idDoctoServico = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico();
		
		if (!eventoVolumeService.hasEventoVolumeDescarregado(volumeNotaFiscal.getIdVolumeNotaFiscal())) {
			eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, tpScan);
		}

		List<VolumeNotaFiscal> volumes =
			volumeNotaFiscalService.findByIdConhecimentoAndIdLocalizacaoFilial(idDoctoServico, SessionUtils.getFilialSessao().getIdFilial());

		boolean hasVolumes = volumes.stream().
				anyMatch(obj -> ConstantesSim.CD_MERCADORIA_EM_DESCARGA.equals(obj.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()));
		
		List<Short> cdsLocalizacao = new ArrayList<>();
		CollectionUtils.addAll(cdsLocalizacao, new Short[]{ 
				ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA,
				ConstantesSim.CD_MERCADORIA_CANCELADA,
				ConstantesExpedicao.CD_MERCADORIA_DEVOLVIDA,
				ConstantesExpedicao.CD_MERCADORIA_REFATURADA,
				ConstantesSim.CD_MERCADORIA_CLIENTE_INDENIZADO
		});
		
		if(!hasVolumes){
			DoctoServico docto = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento();
			if ((docto.getLocalizacaoMercadoria() != null && !cdsLocalizacao.contains(docto.getLocalizacaoMercadoria()))
					|| docto.getLocalizacaoMercadoria() == null){
				
				incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
						ConstantesSim.EVENTO_DESCARGA_CONCLUIDA,
						idDoctoServico, 
						SessionUtils.getFilialSessao().getIdFilial(),
						docto.getFilialByIdFilialOrigem().getSgFilial() + " " +
								FormatUtils.formataNrDocumento(docto.getNrDoctoServico().toString(), docto.getTpDocumentoServico().getValue()),
						JTDateTimeUtils.getDataHoraAtual(),
						docto.getTpDocumentoServico().getValue());
			}				
		}
		
	}

	public Map<String, String> storeDescargaDispositivoConfirmacao(Long idDispositivoUnitizacao, Long idControleCarga, String token) {
		criaSessao(token);
		try {
			ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
			DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(idDispositivoUnitizacao);

			executeDescargaByDispositivoUnitizacao(controleCarga, dispositivoUnitizacao);

			CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(), "D");
			// E - Em Conferência MWW
			// O - Concluído MWW
			if (carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("O")) || carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("F"))) {
				//LMS-45050	A descarga desse carregamento já foi concluída.
				throw new BusinessException("LMS-45050");
			} else if (carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("E"))) {
				carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
				carregamentoDescargaService.store(carregamentoDescarga);
			}

			Map dispositivoMapped = unitizacaoService.findMapDispositivoUnitizacao(dispositivoUnitizacao, idControleCarga, "D");

			verificaConhecimentoCompleto(controleCarga, (List<Map>) dispositivoMapped.get("conhecimentos"));
			verificaDispositivoCompleto(controleCarga, (List<Map>) dispositivoMapped.get("dispositivos"));

			return new HashMap(dispositivoMapped);
		}finally {
			destroiSessao();
		}
		
	}

	private Map<String, Object> getOcorrenciaEntregaMapped(OcorrenciaEntrega ocorrenciaEntrega) {

		Map<String, Object> ocorrenciaEntregaMapped = new HashMap<>();

		if(ocorrenciaEntrega != null) {
			ocorrenciaEntregaMapped.put("idOcorrenciaEntrega", ocorrenciaEntrega.getIdOcorrenciaEntrega());
			ocorrenciaEntregaMapped.put("cdOcorrenciaEntrega", ocorrenciaEntrega.getCdOcorrenciaEntrega());
			ocorrenciaEntregaMapped.put("dsOcorrenciaEntrega", ocorrenciaEntrega.getDsOcorrenciaEntrega());
		}
		
		return ocorrenciaEntregaMapped;
	}
	
	/** Regra 45.03.04.04 - Item 2 a 2 1 a - Iniciar Descarga de Viagem
	 * verifica se o status do controle de carga está no range permitido
	 * @param controleCarga
	 * @return
	 */
	private boolean isStatusControleCarga( ControleCarga controleCarga ){
		if( "PO".equalsIgnoreCase(controleCarga.getTpStatusControleCarga().getValue()) 
				|| "AD".equalsIgnoreCase(controleCarga.getTpStatusControleCarga().getValue())
				|| "ED".equalsIgnoreCase(controleCarga.getTpStatusControleCarga().getValue())
				|| "EP".equalsIgnoreCase(controleCarga.getTpStatusControleCarga().getValue()) ){
			
			return true;
		} else {
			throw new BusinessException("LMS-05317");
		}
	}
	
	/**
	 *  Regra 45.03.04.04 - Item 2 a 2 1 b - Iniciar Descarga de Viagem
	 * verifica se a filial do usuario logado está nos trechos do controle de carga
	 * @param controleCarga
	 * @return
	 */
	private boolean isTrechoControleCarga(ControleCarga controleCarga){
		Integer controlesTrecho = controleTrechoService.getRowCountControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(controleCarga.getIdControleCarga(),
				null, SessionUtils.getFilialSessao().getIdFilial() );
		
		if (controlesTrecho.intValue() > 0) {
			return true;
		}else {
			throw new BusinessException("LMS-05069");
		}
	}
	
	
	/**
	 * Regra 45.03.04.04 - Item 2 a 2 1 c - Iniciar Descarga de Viagem
	 * valida se a filial do Usuário logado e a filial do Controle de Carga são iguais
	 */
	private boolean isFilialAtualizaStatus(ControleCarga controleCarga){
		if( controleCarga.getFilialByIdFilialAtualizaStatus().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial() )  ){
			return true;
		}else{
			throw new BusinessException("LMS-05070");
		}
	}
	
	/**
	 * Realiza a mudança de localização do volume para a filial onde o usuario esta logado, conforme solicitação
	 * CQPRO00026985
	 * @param volume
	 */
	private void executeEventoVolumeLocalizacao(VolumeNotaFiscal volume){
		Long milis = System.currentTimeMillis();
		HistoricoFilial historicoFilial = this.historicoFilialService.findUltimoHistoricoFilial(SessionUtils.getFilialSessao().getIdFilial());
		milis = System.currentTimeMillis() - milis;
		log.warn("[MWW] " + " [" + milis + "ms] idVolume = " + volume.getIdVolumeNotaFiscal().toString()
				+  " historicoFilialService.findUltimoHistoricoFilial");

		String tpFilial = historicoFilial.getTpFilial().getValue();
		//Matriz
		if (!"MA".equals(tpFilial)) {
			volume.setLocalizacaoMercadoria(localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(ConstantesSim.CD_MERCADORIA_NO_TERMINAL));
			volume.setLocalizacaoFilial(SessionUtils.getFilialSessao());
			volumeNotaFiscalService.store(volume);
		}
	}
	
	public java.io.Serializable executeEventoVolumeLido(Long idVolumeNotaFiscal, String tpScan) {
		return getEventoVolumeService().generateEventoVolume(idVolumeNotaFiscal, "CD_EVENTO_VOLUME_LIDO", tpScan);
	}

	/**
	 * Verifica e persiste a sobra de filial quando necessário.
	 */
	public void storeIdentificarSobra(VolumeNotaFiscal volumeNotaFiscal) {

		Long idVolume = volumeNotaFiscal.getIdVolumeNotaFiscal();
	
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("volumeNotaFiscal.idVolumeNotaFiscal", idVolume);
		criteria.put("filial.idFilial", SessionUtils.getFilialSessao().getIdFilial());
		criteria.put("dhCriacao", JTDateTimeUtils.getDataHoraAtual());
		List listaSobrasFilial = volumeSobraFilialService.find(criteria);
	
		if (listaSobrasFilial == null || listaSobrasFilial.isEmpty()) {
	
			List<ManifestoNacionalVolume> listaManifestoNacionalVolumes = manifestoNacionalVolumeService
					.findByVolumeNotaFiscalAndFilialDestino(idVolume, SessionUtils.getFilialSessao().getIdFilial());
	
			if (listaManifestoNacionalVolumes == null || listaManifestoNacionalVolumes.isEmpty()) {
				
				//Obs.: Caso a busca EVENTO_VOLUME.id_evento_volume retorne NULO, NÃO chamar a função IdentificacaoSobra.
				EventoVolume eventoVolume = eventoVolumeService.findUltimoEventoVolumeDeDescargaByFilial(idVolume, SessionUtils.getFilialSessao().getIdFilial());
				if (eventoVolume != null) {
					VolumeSobraFilial volumeSobraFilial = new VolumeSobraFilial();
					
					volumeSobraFilial.setFilial(SessionUtils.getFilialSessao());
					volumeSobraFilial.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
					volumeSobraFilial.setEventoVolume(eventoVolume);
					volumeSobraFilial.setVolumeNotaFiscal(volumeNotaFiscal);
					
					volumeSobraFilialService.store(volumeSobraFilial);
				}
			}
		}
	}
	
	public List<Map<String, Object>> findAwbs(Long idControleCarga, Long idFilial) {
		List<Map<String, Object>> awbMapList = new ArrayList<>();
		if (idControleCarga != null) {
			
			List<PedidoColeta> pedidos = pedidoColetaService.findPedidoColetaByIdControleCargaByTpStatusColeta(idControleCarga, null);
			
			List<Conhecimento> ctos = new ArrayList<>();
			for (PedidoColeta pedidoColeta : pedidos) {
				List<Conhecimento> ctosAux = pedidoColetaService.getCtosByPedidoColeta(pedidoColeta);
				for (Conhecimento ctoAux : ctosAux) {
					this.addCtoToList(ctoAux, ctos);
				}
			}

			if (!CollectionUtils.isEmpty(ctos)) {
				for (Conhecimento cto : ctos) {
					Awb awb = ctoAwbService.findAwbByIdCto(cto.getIdDoctoServico(), ConstantesExpedicao.TP_STATUS_AWB_EMITIDO);
					awb = (awb == null) ? ctoAwbService.findAwbByIdCto(cto.getIdDoctoServico(), ConstantesExpedicao.TP_STATUS_PRE_AWB) : awb;
					
					Map<String, Object> awbMap = findAwbMapped(awbMapList, awb);
					
					List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findByIdConhecimento(cto.getIdDoctoServico());
					
					this.addVolumesToMap(volumes, idControleCarga, awbMap, idFilial);
				}
			}
		}
		
		this.updateTotais(awbMapList);

		return awbMapList;
	}

	private void addCtoToList(Conhecimento ctoAux, List<Conhecimento> ctos) {
		if ((ConstantesSim.CD_MERCADORIA_EM_DESCARGA.equals(ctoAux.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())
				|| ConstantesSim.CD_MERCADORIA_AGUARDANDO_DESCARGA.equals(ctoAux.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())
				|| ConstantesSim.CD_MERCADORIA_NO_TERMINAL.equals(ctoAux.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()))
				&& SessionUtils.getFilialSessao().getIdFilial().equals(ctoAux.getFilialLocalizacao().getIdFilial())) {
			ctos.add(ctoAux);
		}
	}

	private void addVolumesToMap(List<VolumeNotaFiscal> volumes, Long idControleCarga, Map<String, Object> awbMap, Long idFilial) {
		for (VolumeNotaFiscal volume : volumes) {
			DispositivoUnitizacao du = volume.getDispositivoUnitizacao();
			
			if (du != null && du.getIdDispositivoUnitizacao() != null){
				Long idDisp = du.getIdDispositivoUnitizacao();				
				
				Map<String, Object> dispMap = findDispMapped(idControleCarga, awbMap, idDisp, idFilial);
				
				List<Map<String, Object>> listCon = (List<Map<String, Object>>)dispMap.get("conhecimentos");
				this.addVolumeToMap(idControleCarga, listCon, volume, idFilial);
			} else{
				List<Map<String, Object>> listCon = (List<Map<String, Object>>)awbMap.get("conhecimentos");
				this.addVolumeToMap(idControleCarga, listCon, volume, idFilial);
			}			
		}
	}

	private void updateTotais(List<Map<String, Object>> awbMapList) {
		for (Map<String, Object> awbMap : awbMapList) {
			List<Map<String, Object>> listCon = (List<Map<String, Object>>)awbMap.get("conhecimentos");
			List<Map<String, Object>> listDisp = (List<Map<String, Object>>)awbMap.get("dispositivos");
			
			int totVolumes = 0;
			for (Map<String, Object> conMap : listCon) {
				totVolumes += volumeNotaFiscalService.findByIdConhecimento((Long)conMap.get("idDoctoServico")).size();
			}
			
			awbMap.put("qtVolumes", totVolumes);
			awbMap.put("qtDispositivoUnit", listDisp.size());
		}
	}

	private void addVolumeToMap(Long idControleCarga, List<Map<String, Object>> listCon, VolumeNotaFiscal volume, Long idFilial) {

		final Conhecimento conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();

		boolean exists = listCon.stream().anyMatch(map -> conhecimento.getIdDoctoServico().equals(map.get("idDoctoServico")));
		
		if(!exists){
			listCon.add(this.getConMapped(idControleCarga, conhecimento, volume));
		}
	}

	private Map<String, Object> getConMapped(Long idControleCarga, Conhecimento con, VolumeNotaFiscal volume) {
		
		Map<String, Object> conhecimentoMapped = new HashMap<>();
		
		conhecimentoMapped.put("sgFilialDocumento", con.getFilialOrigem().getSgFilial());
		conhecimentoMapped.put("nrConhecimento", con.getNrConhecimento());
		conhecimentoMapped.put("idDoctoServico", con.getIdDoctoServico());
		
		Integer volumesProcessados = countVolumesCarregamentoDescarga(idControleCarga, con.getIdDoctoServico(), volume.getDispositivoUnitizacao());
		Integer totalVolumesProcessados = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga, con.getIdDoctoServico(),
				"D");

		conhecimentoMapped.put("qtVolumesDescarregados", volumesProcessados);
		conhecimentoMapped.put("qtVolumes", con.getQtVolumes());
		conhecimentoMapped.put("completo", totalVolumesProcessados.compareTo(con.getQtVolumes()) == 0);
		conhecimentoMapped.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volume.getIdVolumeNotaFiscal()));

		return conhecimentoMapped;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findDispMapped(Long idControleCarga, Map<String, Object> awbMap, final Long idDisp, Long idFilial) {
		
		Map<String, Object> dispMap = this.findDispOnMappedList((List<Map<String, Object>>)awbMap.get("dispositivos"), idDisp);
		
		if (dispMap == null) {
			List<Map<String, Object>> listDisp = (List<Map<String, Object>>)awbMap.get("dispositivos");
			dispMap = getDispMapped(idControleCarga, idDisp, idFilial);
			listDisp.add(dispMap);
		}
		
		dispMap = this.findDispOnMappedList((List<Map<String, Object>>)awbMap.get("dispositivos"), idDisp);
		
		return dispMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findDispOnMappedList(List<Map<String, Object>> listDisp, Long idDisp) {

		for (Map<String, Object> duMap : listDisp) {
			if (duMap.get("dispositivos") != null) {
				Map<String, Object> duAux = findDispOnMappedList((List<Map<String, Object>>)duMap.get("dispositivos"), idDisp);
				if (duAux != null) {
					return duAux;
				}
			}
			
			if (idDisp.equals((duMap).get("idDispositivoUnitizacao"))) {
				return duMap;
			}
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getDispMapped(Long idControleCarga, Long idDisp, Long idFilial) {
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findById(idDisp);
		
		Map<String, Object> dispositivoMapped = new HashMap<>();
		dispositivoMapped.put("idDispositivoUnitizacao", dispositivo.getIdDispositivoUnitizacao());
		dispositivoMapped.put("tpDispositivoUnit", dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
		dispositivoMapped.put("qtVolumes", volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaDispositivo(idControleCarga,
				getTpOperacao(), dispositivo));
		dispositivoMapped.put("qtDispositivoUnit", (dispositivo.getDispositivosUnitizacao() != null) ? dispositivo.getDispositivosUnitizacao().size() : 0);
		dispositivoMapped.put("conhecimentos", new ArrayList<Map<String, Object>>());
		dispositivoMapped.put("dispositivos", new ArrayList<Map<String, Object>>());

		DispositivoUnitizacao dispositivoPai = dispositivo.getDispositivoUnitizacaoPai();
		Map<String, Object> dispPaiMapped = null;
		if (dispositivoPai != null) {
			dispPaiMapped = getDispMapped(idControleCarga, dispositivoPai.getIdDispositivoUnitizacao(), idFilial);
			((List<Map<String, Object>>)dispPaiMapped.get("dispositivos")).add(dispositivoMapped);
		}

		return dispPaiMapped == null ? dispositivoMapped : dispPaiMapped;
	}

	private Map<String, Object> findAwbMapped(List<Map<String, Object>> awbMapList, Awb awb) {
		final Long idAwb = awb.getIdAwb();
		@SuppressWarnings("unchecked")
		Map<String, Object> awbMap = (Map<String, Object>) CollectionUtils.find(awbMapList, new Predicate() {
		    public boolean evaluate(Object obj) {
		    	return idAwb.equals(((Map<String, Object>)obj).get("idAwb"));
		    }
		});
		
		if (awbMap == null) {
			awbMap = getAwbMapped(awb);
			awbMapList.add(awbMap);
		}
		return awbMap;
	}

	private Map<String, Object> getAwbMapped(Awb awb) {
		Map<String, Object> awbMapped = new HashMap<>();

		awbMapped.put("idAwb", awb.getIdAwb());
		
		if( ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(awb.getTpStatusAwb().getValue())){
			awbMapped.put("nrAwb", this.getSgCiaAerea(awb.getCiaFilialMercurio()) + " " + AwbUtils.getNrAwbFormated(awb.getDsSerie(), awb.getNrAwb(), awb.getDvAwb()));
		}else{
			awbMapped.put("nrAwb", this.getSgCiaAerea(awb.getCiaFilialMercurio()) + " " + awb.getIdAwb().toString());
		}		
		awbMapped.put("dispositivos", new ArrayList<Map<String, Object>>());
		awbMapped.put("conhecimentos", new ArrayList<Map<String, Object>>());
		
		return awbMapped;
	}

	public MeioTransporte findMeioTransporteByCodigoBarras(String nrCodigoBarras, String token) {
		criaSessao(token);
		try {
			MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByCodigoBarras(Long.parseLong(nrCodigoBarras));

			if (meioTransporte == null) {
				//LMS-06018- Meio de transporte não encontrado.
				throw new BusinessException("LMS-06018");
			}

			return meioTransporte;
		}finally {
			destroiSessao();
		}
	}

	public Map executeMeioTransportes(String tpControleCarga, String blReabreCarregamento,
                                      MeioTransporte meioTransporte, ControleCarga controleCarga, String token) {
		criaSessao(token);
		try {
			Map<String, Object> retorno = new HashMap<>();
			retorno.put("nrFrota", meioTransporte.getNrFrota());
			retorno.put("placa", meioTransporte.getNrIdentificador());
			retorno.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());

			Map<String, Object> controleCargaMapped = new HashMap<>();

			// LMS-5444
			if ("V".equals(controleCarga.getTpControleCarga().getValue())) {

				List<Manifesto> manifesto = manifestoService.findManifestoByControleCargaAndStatusAndFilialDestino(controleCarga, SessionUtils.getFilialSessao().getIdFilial());

				if (manifesto.isEmpty()) {
					throw new BusinessException("LMS-45204");
				}
			}

			CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(), "D");

			//2.Se o Controle de Carga não possuir informação de Inicio de Descarga, então prosseguir com os procedimentos
			//descritos no item Iniciar Descarga
			if (carregamentoDescarga != null) {

				if (carregamentoDescarga.getDhInicioOperacao() == null) {
					controleCargaMapped.put("descargaIniciada", false);

					//3.Se o Controle de Carga possuir informação de Inicio de Descarga e não possuir informação de Fim de Descarga,
					//então prosseguir com os procedimentos descritos no item Durante a Descarga
				} else if (carregamentoDescarga.getDhInicioOperacao() != null && carregamentoDescarga.getDhFimOperacao() == null) {
					controleCargaMapped.put("descargaIniciada", true);

				} else if (carregamentoDescarga.getDhInicioOperacao() != null && carregamentoDescarga.getDhFimOperacao() != null) {
					//LMS-03009 - Descarga já finalizada para este veículo.
					throw new BusinessException("LMS-03009");
				}

				//Salva usuario na equipe do carregamento
				getEquipeOperacaoService().storeEquipeDescarga(controleCarga, carregamentoDescarga);

				if (carregamentoDescarga.getBox() != null) {
					Map<String, Object> boxMapped = new HashMap<>();
					boxMapped.put("idBox", carregamentoDescarga.getBox().getIdBox());
					boxMapped.put("nrBox", carregamentoDescarga.getBox().getNrBox());
					controleCargaMapped.put("box", boxMapped);
				}

				if ("O".equals(carregamentoDescarga.getTpStatusOperacao().getValue()) && "N".equals(blReabreCarregamento)) {
					throw new BusinessException("LMS-45097");
				} else if ("O".equals(carregamentoDescarga.getTpStatusOperacao().getValue()) && "S".equals(blReabreCarregamento)) {
					carregamentoDescargaService.storeReabrirCarregamentoDescarga(carregamentoDescarga.getIdCarregamentoDescarga());
				}
			}

			controleCargaMapped.put("idControleCarga", controleCarga.getIdControleCarga());
			controleCargaMapped.put("nrControleCarga", controleCarga.getNrControleCarga());
			controleCargaMapped.put("conhecimentos", this.findConhecimentosDescargaMapped(
					controleCarga.getIdControleCarga(), tpControleCarga));
			controleCargaMapped.put("dispositivos", this.findDispositivosDescarregados(
					controleCarga.getIdControleCarga(), tpControleCarga, SessionUtils.getFilialSessao().getIdFilial()));

			controleCargaMapped.put("awbs", this.findAwbs(controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial()));

			Map<String, Object> filialOrigem = new HashMap<>();
			filialOrigem.put("sgFilial", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
			controleCargaMapped.put("filialOrigem", filialOrigem);

			if (controleCarga.getTpControleCarga().getValue().equals("V")) {
				if (controleCarga.getRota() != null) {
					Map<String, Object> rotaMapped = new HashMap<>();
					rotaMapped.put("idRota", controleCarga.getRota().getIdRota());
					rotaMapped.put("dsRota", controleCarga.getRota().getDsRota());
					rotaMapped.put("nrRota", controleCarga.getRotaIdaVolta() != null ? controleCarga.getRotaIdaVolta().getNrRota() : "");
					controleCargaMapped.put("rota", rotaMapped);
				}
			} else if (controleCarga.getTpControleCarga().getValue().equals("C")) {
				if (controleCarga.getRotaColetaEntrega() != null) {
					Map<String, Object> rotaMapped = new HashMap<>();
					rotaMapped.put("idRota", controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega());
					rotaMapped.put("dsRota", controleCarga.getRotaColetaEntrega().getDsRota());
					rotaMapped.put("nrRota", controleCarga.getRotaColetaEntrega() != null ? controleCarga.getRotaColetaEntrega().getNrRota() : "");
					controleCargaMapped.put("rota", rotaMapped);
				}
				//LMS-2594
				boolean blInformaKmPortaria = filialService.findBlInformaKmPortaria(SessionUtils.getFilialSessao().getIdFilial());
				retorno.put("blInformaKmPortaria", blInformaKmPortaria);

				if (blInformaKmPortaria) {
					ControleQuilometragem controleQuilometragem = controleQuilometragemService.findControleQuilometragemByIdControleCargaByIdFilial(controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(), false);
					retorno.put("nrQuilometragem", controleQuilometragem.getNrQuilometragem());
					retorno.put("blVirouHodometro", controleQuilometragem.getBlVirouHodometro());
				}
			}
			retorno.put("controleCarga", controleCargaMapped);
			return retorno;
		}finally {
			destroiSessao();
		}
	}

	private void criaSessao(String token){
		Filial filial =  integracaoJwtService.getFilialSessao(integracaoJwtService.getIdFilialByToken(token));
		Usuario usuario = integracaoJwtService.getUsuarioSessaoByToken(token);
		Empresa empresa = integracaoJwtService.getEmpresaSessao(integracaoJwtService.getIdEmpresaByToken(token));
		SessionContext.setUser(usuario);
		SessionContext.set(SessionKey.EMPRESA_KEY, empresa);
		SessionContext.set(SessionKey.FILIAL_KEY, filial);
		SessionContext.set(SessionKey.FILIAL_DTZ, filial.getDateTimeZone());
		SessionContext.set(SessionKey.MOEDA_KEY, moedaService.findMoedaByUsuarioEmpresa(usuario, empresa));
		SessionContext.set(SessionKey.PAIS_KEY, paisService.findPaisByUsuarioEmpresa(usuario, empresa));
		SessionContext.set(SessionKey.ULT_HIST_FILIAL_KEY, historicoFilialService.findUltimoHistoricoFilial(filial.getIdFilial()));
		SessionContext.set(SessionKey.FILIAL_MATRIZ_KEY, historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial()));
	}


	private void destroiSessao(){
		SessionContext.remove(SessionKey.EMPRESA_KEY);
		SessionContext.remove(SessionKey.FILIAL_KEY);
		SessionContext.remove(SessionKey.FILIAL_DTZ);
		SessionContext.remove(SessionKey.MOEDA_KEY);
		SessionContext.remove(SessionKey.PAIS_KEY);
		SessionContext.remove("adsm.session.authenticatedUser");
		SessionContext.remove(SessionKey.ULT_HIST_FILIAL_KEY);
		SessionContext.remove(SessionKey.FILIAL_MATRIZ_KEY);
	}


	private Object getSgCiaAerea(CiaFilialMercurio ciaFilialMercurio) {
		return ciaFilialMercurio.getEmpresa().getPessoa().getNmFantasia().substring(0, 3);
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}

	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}

	public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}
	
	public void setControleQuilometragemService(
			ControleQuilometragemService controleQuilometragemService) {
		this.controleQuilometragemService = controleQuilometragemService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setLacreControleCargaService(
			LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}

	public void setUnitizacaoService(UnitizacaoService unitizacaoService) {
		this.unitizacaoService = unitizacaoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public ControleTrechoService getControleTrechoService() {
		return controleTrechoService;
	}

	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return carregamentoDescargaService;
	}

	public OcorrenciaEntregaService getOcorrenciaEntregaService() {
		return ocorrenciaEntregaService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public BoxService getBoxService() {
		return boxService;
	}

	public ControleQuilometragemService getControleQuilometragemService() {
		return controleQuilometragemService;
	}

	public LacreControleCargaService getLacreControleCargaService() {
		return lacreControleCargaService;
	}

	public UnitizacaoService getUnitizacaoService() {
		return unitizacaoService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public EventoVolumeService getEventoVolumeService() {
		return eventoVolumeService;
	}

	public LocalizacaoMercadoriaService getLocalizacaoMercadoriaService() {
		return localizacaoMercadoriaService;
	}

	public EventoDispositivoUnitizacaoService getEventoDispositivoUnitizacaoService() {
		return eventoDispositivoUnitizacaoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public HistoricoFilialService getHistoricoFilialService() {
		return historicoFilialService;
	}

	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

	public EquipeOperacaoService getEquipeOperacaoService() {
		return equipeOperacaoService;
	}

	public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
		this.equipeOperacaoService = equipeOperacaoService;
	}

	public ManifestoEntregaVolumeService getManifestoEntregaVolumeService() {
		return manifestoEntregaVolumeService;
	}

	public void setManifestoEntregaVolumeService(
			ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
		this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public ManifestoNacionalVolumeService getManifestoNacionalVolumeService() {
		return manifestoNacionalVolumeService;
	}

	public void setManifestoNacionalVolumeService(
			ManifestoNacionalVolumeService manifestoNacionalVolumeService) {
		this.manifestoNacionalVolumeService = manifestoNacionalVolumeService;
	}

	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public VolumeSobraService getVolumeSobraService() {
		return volumeSobraService;
	}

	public void setVolumeSobraService(VolumeSobraService volumeSobraService) {
		this.volumeSobraService = volumeSobraService;
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public VolumeSobraFilialService getVolumeSobraFilialService() {
		return volumeSobraFilialService;
	}
	
	public void setVolumeSobraFilialService(
			VolumeSobraFilialService volumeSobraFilialService) {
		this.volumeSobraFilialService = volumeSobraFilialService;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public CtoAwbService getCtoAwbService() {
		return ctoAwbService;
	}

	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}

	public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
		return incluirEventosRastreabilidadeInternacionalService;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
}
