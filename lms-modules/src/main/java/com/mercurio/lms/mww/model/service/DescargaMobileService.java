package com.mercurio.lms.mww.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoNacionalVolumeService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.UnitizacaoService;
import com.mercurio.lms.carregamento.model.service.VolumeSobraService;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaVolumeService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.VolumeSobraFilial;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.VolumeSobraFilialService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesAwb;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
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
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.mww.descargaMobileService"
 */
public class DescargaMobileService extends AbstractMobileService {
	private static final short CD_AGUARDANDO_AGENDAMENTO_ENTREGA = 119;
	
	private ControleCargaService controleCargaService;
	private ControleTrechoService controleTrechoService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private CarregamentoMobileService carregamentoMobileService;
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
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private AwbService awbService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	
	private Log log = LogFactory.getLog(DescargaMobileService.class);
	
	public boolean isCarga() {
		return false;
	}
	
	private Long getSessionIdFilial(){
		return SessionUtils.getFilialSessao().getIdFilial();
	}
	
	
	/** 45.03.04.04 Item 2 i - Iniciar Descarga de Viagem
	 * Retorna o Controle de Carga de Viagem do Meio de Transporte 
	 * @param idMeioTransporte
	 * @param tpControleCarga  V - viagem , C - Coleta/Entrega
	 * @param idFilialDestino
	 * @return ControleCarga
	 */
	public ControleCarga findControleCargaViagem(Long idMeioTransporte, String tpControleCarga, Long idFilialDestino ){		
		
		ControleCarga controleCarga = 
			controleCargaService.findByMeioTransporteTpControleCargaAndFilialAtualizaStatus(idMeioTransporte, tpControleCarga, idFilialDestino);

		if( controleCarga != null ){
			if(tpControleCarga.equals("V")){
			if(!validateControleCarga(controleCarga, getSessionIdFilial())){
				return null;
			}
			}else if(tpControleCarga.equals("C")){
				if(!isStatusControleCarga(controleCarga)){
					return null;
				}
			}
			
		}else
			throw new BusinessException("LMS-45001");

		return controleCarga;
	}
	
	
	/** Regra 45.03.04.04 Item 2 ii - Iniciar Descarga de Viagem
	 * Realiza validações prévias no Controle de Carga
	 * @param controleCarga
	 * @return retorna true caso o Controle de Carga seja válido
	 * 		   ou false caso contrário
	 */
	public boolean validateControleCarga(ControleCarga controleCarga, Long idFilialUsuarioLogado){
		boolean controleCargaValido = false;
		
		controleCargaValido =  isStatusControleCarga(controleCarga) && 
				isTrechoControleCarga(controleCarga, idFilialUsuarioLogado ) &&
				isFilialAtualizaStatus(controleCarga, idFilialUsuarioLogado );
		
		return controleCargaValido;
	}

	
	
	public Map<String,Object> executeDescargaVolume(VolumeNotaFiscal volumeNotaFiscal, 
			  ControleCarga controleCarga, 
			  Long idFilialUsuarioLogado, 
			  CarregamentoDescarga carregamentoDescarga){
		Map<String,Object> descargaVolumes = new HashMap<String, Object>();
		
		this.storeCarregamentoDescargaVolume(carregamentoDescarga, volumeNotaFiscal, null);
		
		if(controleCarga.getTpControleCarga().getValue().equals("V")){
			descargaVolumes = executeDescargaVolume(volumeNotaFiscal, controleCarga, idFilialUsuarioLogado, carregamentoDescarga, ConstantesSim.TP_SCAN_FISICO);
		}else if(controleCarga.getTpControleCarga().getValue().equals("C")){
			descargaVolumes = executeDescargaVolumeColetaEntrega(volumeNotaFiscal, controleCarga, ConstantesSim.TP_SCAN_FISICO);
		}
	
		return descargaVolumes;
	}


	/**
	 * Realiza da descarga individual dos volumes
	 * @param barcode
	 * @param controleCarga
	 */
	public Map<String,Object> executeDescargaVolume(VolumeNotaFiscal volumeNotaFiscal, 
									  ControleCarga controleCarga, 
									  Long idFilialUsuarioLogado, 
									  CarregamentoDescarga carregamentoDescarga,
									  String tpScan){
		
		Map<String, Object> map = new HashMap<String, Object>();
		ManifestoNacionalVolume manifestoNacionalVolume= manifestoNacionalVolumeService.findByIdVolumeAndIdControleCarga(volumeNotaFiscal.getIdVolumeNotaFiscal(),
				controleCarga.getIdControleCarga());
	
		//LMS-5382 - Se VOLUME_NOTA_FISCAL.LOCALIZACAO_FILIAL = id filial logada
		//              && LOCALIZACAO_MERCADORIA.CD_LOCALIZACAO_MERCADORIA = 34 -> VOLUME_NOTA_FISCAL.ID_LOCALIZACAO_MERCADORIA=LOCALIZACAO_MERCADORIA.ID_LOCALIZACAO_MERCADORIA
		if (volumeNotaFiscal.getLocalizacaoFilial() != null && volumeNotaFiscal.getLocalizacaoFilial().getIdFilial().equals(getSessionIdFilial()) 
				&& volumeNotaFiscal.getLocalizacaoMercadoria() != null && volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("34"))) {
			
			executeIniciarDescargaVolumePrevistoNaFilial(volumeNotaFiscal, manifestoNacionalVolume, idFilialUsuarioLogado, carregamentoDescarga, tpScan);
		}
		else {
			map = executeIniciarDescargaVolumeNaoPrevistoNaFilial(volumeNotaFiscal, manifestoNacionalVolume, tpScan);
		}

		return map;
	}
		
	
	
	/**
	 * Realiza da descarga individual dos volumes da coleta
	 * @param barcode
	 * @param controleCarga
	 */
	public Map<String,Object> executeDescargaVolumeColetaEntrega(VolumeNotaFiscal volumeNotaFiscal, 
												    ControleCarga controleCarga,
												    String tpScan){
		Map<String, Object> map = new HashMap<String, Object>();
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
	 * @param barcode
	 */
	public Map<String, Object> executeDescargaByDispositivoUnitizacao(ControleCarga controleCarga, 
																	  Long idFilialUsuarioLogado, 
																	  DispositivoUnitizacao dispositivoUnitizacao){
		Map<String, Object> map = new HashMap<String, Object>(); 
		//Este validateDescargaDispositivoUnitizacao possui a mesma recursividade. 
		//Mas essa chamada ocorre necessariamente dentro desse executeDescargaByDispositivoUnitizacao pois é necessaria essa validação 
		//sempre que for executar esse executeDescargaByDispositivoUnitizacao, isso garante que não será armazenado nenhum dispositivo de unitizacao
		//que possua um Volume ou outro Dispositivo de Unitizacao, que não cumpra as regras pre-definidas.
		map = validateDescargaDispositivoUnitizacao(controleCarga, idFilialUsuarioLogado, dispositivoUnitizacao);
		
		if(map == null || map.isEmpty()){
			executeDescargaDispUnit(controleCarga, idFilialUsuarioLogado, dispositivoUnitizacao, ConstantesSim.TP_SCAN_FISICO);
		}
		
		return map;
		
		
	}
	
	
	/**
	 * Regra 45.03.04.04 4 b
	 * @param controleCarga
	 * @param barcode
	 */
	private void executeDescargaDispUnit(ControleCarga controleCarga, Long idFilialUsuarioLogado, DispositivoUnitizacao dispositivoUnitizacao, String tpScan){
		List<DispositivoUnitizacao> lstDispositivoUnitizacao = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		if(!lstDispositivoUnitizacao.isEmpty()){
			for (DispositivoUnitizacao dispositivoUnitizacaoFilho : lstDispositivoUnitizacao) {
				executeDescargaDispUnit(controleCarga, idFilialUsuarioLogado, dispositivoUnitizacaoFilho, ConstantesSim.TP_SCAN_CASCADE);
			}
		}
		
		List<VolumeNotaFiscal> volumeNotaFiscalList = 
			volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		if(!volumeNotaFiscalList.isEmpty()){

			CarregamentoDescarga carregamentoDescarga = 
				carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), idFilialUsuarioLogado, "D");
			
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
				
				if(controleCarga.getTpControleCarga().getValue().equals("V")){
					this.storeIdentificarSobra(volumeNotaFiscal);
					ManifestoNacionalVolume manifestoNacionalVolume = manifestoNacionalVolumeService.findByIdVolumeAndIdControleCarga(volumeNotaFiscal.getIdVolumeNotaFiscal(), controleCarga.getIdControleCarga());
					executeIniciarDescargaVolumePrevistoNaFilial(volumeNotaFiscal, manifestoNacionalVolume, idFilialUsuarioLogado, carregamentoDescarga, ConstantesSim.TP_SCAN_CASCADE);
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
	public Map<String, Object> validateDescargaDispositivoUnitizacao(ControleCarga controleCarga, Long idFilialUsuarioLogado, DispositivoUnitizacao dispositivoUnitizacao){

		Map<String, Object> map = null; 
		List<VolumeNotaFiscal> volumeNotaFiscalList = 
			volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao());

		List<DispositivoUnitizacao> lstDispositivoUnitizacao = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		if(!lstDispositivoUnitizacao.isEmpty()){
			for (DispositivoUnitizacao dispositivoUnitizacaoFilho : lstDispositivoUnitizacao) {
				validateDescargaDispositivoUnitizacao(controleCarga, idFilialUsuarioLogado, dispositivoUnitizacaoFilho);
			}
		}
		
		//se codigo localizacao mercadoria eh 24 = no terminal, mostra msg
		if(dispositivoUnitizacao.getLocalizacaoFilial()!=null 
				&& dispositivoUnitizacao.getLocalizacaoFilial().getIdFilial().equals(getSessionIdFilial()) 
				&& dispositivoUnitizacao.getLocalizacaoMercadoria()!=null 
				&& dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("24"))){
			
			
			//LMS-45062 = dispositivo ja descarregado
			throw new BusinessException("LMS-45062");
			
			
		}else if(!volumeNotaFiscalList.isEmpty()){
				//45.03.04.04 4 b 2 b
				for(VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscalList ){
					/**
					 * 1.	Omitir a regra de verificação de unitização.
					 * 2.	Se o volume não for previsto para descarga na filial do usuário logado (conforme regras), 
					 *      entao não apresentar as mensagens descritas na própria regra e sim a seguinte mensagem: 
					 *      LMS-45010 Atenção, um ou mais volumes não estão previstos para desembarque nesta unidade. 
					 *      Favor descarregar volume a volume.
					 */
					//“Em Descarga”  --> 34  Se os volumes não estiverem em descarga significa que não pertencem aquele destino
					if(controleCarga.getTpControleCarga().getValue().equals("V")){
						if( !volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("34")) ){
							// LMS-45010 Atenção, um ou mais volumes não estão previstos para desembarque nesta unidade.
							//      Favor descarregar volume a volume.
							throw new BusinessException("LMS-45010");
						}
					}else if(controleCarga.getTpControleCarga().getValue().equals("C")){
						
						
						if( !volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("37")) &&
								!volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("69")) &&
								!volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_EM_DESCARGA) &&
								!volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("33"))){
							// LMS-45010 Atenção, um ou mais volumes não estão previstos para desembarque nesta unidade.
							//      Favor descarregar volume a volume.
							throw new BusinessException("LMS-45010");
						}
					}
				}

			}
			
		return map;
	}
	


	
	/**
	 * REGRA 45.03.04.04 Item 4 a ii 
	 * Caso o Volume lido NÃO ESTEJA previsto para descarga na filial
	 */
	private Map<String, Object> executeIniciarDescargaVolumeNaoPrevistoNaFilial(VolumeNotaFiscal volumeNotaFiscal, ManifestoNacionalVolume manifestoNacionalVolume, String tpScan){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//45.03.04.04 Item 4 ii 1  - Verifica se o Volume está no Controle de Carga a partir do Pré-Manifesto
		if( manifestoNacionalVolume != null 
				&& manifestoNacionalVolume.getManifestoViagemNacional() !=null 
				&& manifestoNacionalVolume.getManifestoViagemNacional().getManifesto()!=null){
			
			if( "EF".equalsIgnoreCase( manifestoNacionalVolume.getManifestoViagemNacional().getManifesto().getTpStatusManifesto().getValue() )){
				Long milis = System.currentTimeMillis();
				//eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), "CD_EVENTO_VOLUME_LIDO", tpScan);
				eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), Short.valueOf("406"), tpScan, null);
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
			//eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), "CD_EVENTO_VOLUME_LIDO", tpScan);
			eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), Short.valueOf("406"), tpScan, null);
			milis = System.currentTimeMillis() - milis;
			log.warn("[MWW] " + " [" + String.valueOf(milis) + "ms] idVolume = " + volumeNotaFiscal.getIdVolumeNotaFiscal().toString() 
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
		
		Map<String, Object> map = new HashMap<String, Object>();
		
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
					
					List<VolumeNotaFiscal> lstVolumes = 
							volumeNotaFiscalService.findByIdConhecimentoAndIdLocalizacaoFilial(conhecimento.getIdDoctoServico(), getSessionIdFilial());
					
					int volumesInTerminal = CollectionUtils.countMatches(lstVolumes, new Predicate() {
					    public boolean evaluate(Object obj) {
					    	return ConstantesSim.CD_MERCADORIA_NO_TERMINAL_MERCADORIA_RETORNADA.equals(((VolumeNotaFiscal)obj).getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
					    }
					});
					
					if(lstVolumes.size() == volumesInTerminal){
						Awb awb = awbService.findByDoctoServicoAndTpStatusAwb(conhecimento.getIdDoctoServico(), ConstantesAwb.TP_STATUS_PRE_AWB);
						if(awb != null && awb.getFilialByIdFilialOrigem().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())){							
							String nrMan = FormatUtils.formatSgFilialWithLong(manifestoEntregaVolume.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem().getSgFilial(), 
									manifestoEntregaVolume.getManifestoEntrega().getNrManifestoEntrega().longValue());
							
							incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(ConstantesSim.EVENTO_FIM_DESCARGA, conhecimento.getIdDoctoServico(), 
									getSessionIdFilial(), nrMan, JTDateTimeUtils.getDataHoraAtual(), null, SessionUtils.getFilialSessao().getSiglaNomeFilial(), "MAE");
							
							CtoAwb ca = ctoAwbService.findCtoAwb(conhecimento.getIdDoctoServico(), awb.getIdAwb());
							ctoAwbService.removeById(ca.getIdCtoAwb());
						}
					}
				}
							
			}else{
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
	 * Usado para gerar evento na lista de volumes e dispositivos 
	 * 3.	Será gerado um evento “29” = “Inicio de Descarga” para cada volume vinculado a os manifestos.
		a.	A Localização será “34” = “Em Descarga”
		ii.	Os outros manifestos (com exceção dos “FE” = “Fechados”) assumirão automaticamente o status de “EF” = “Em Escala na Filial”. 
		(regra 5.3 da ET 03.01.01.01 Informar início descarga – Sub-evento Iniciar Descarga)
		a.	Os volumes também receberam o status “EF” = “Em Escala na Filial”

	 * @param idControleCarga
	 */
	public void generateEventoInicioDescarga(Long idControleCarga){
		List<Manifesto> listManifesto = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, ConstantesExpedicao.TP_STATUS_MANIFESTO_AGUARDANDO_DESCARGA, ConstantesExpedicao.TP_MANIFESTO_VIAGEM);
		for(Manifesto manifesto : listManifesto) {
			if (!"FE".equals(manifesto.getTpStatusManifesto().getValue()) && !"CA".equals(manifesto.getTpStatusManifesto().getValue())) {
				if(manifesto.getFilialByIdFilialDestino().getIdFilial().equals(getSessionIdFilial())) {
					eventoVolumeService.generateEventoByManifesto(
							manifesto.getIdManifesto(), 
							ConstantesSim.EVENTO_INICIO_DESCARGA_VIAGEM, 
							ConstantesSim.TP_SCAN_LMS);
					
					eventoDispositivoUnitizacaoService.generateEventoByManifesto(
							manifesto.getIdManifesto(), 
							ConstantesSim.EVENTO_INICIO_DESCARGA_VIAGEM, 
							ConstantesSim.TP_SCAN_LMS, null);
				}
			}
		}
	}
					
										
	/**
	 * 
	 * @param idControleCarga
	 */
	public void generateEventoInicioDescargaColetaEntrega(Long idControleCarga){
		List<Manifesto> listManifesto = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, ConstantesExpedicao.TP_STATUS_MANIFESTO_AGUARDANDO_DESCARGA, ConstantesExpedicao.TP_MANIFESTO_ENTREGA);
		for(Manifesto manifesto : listManifesto) {
			if (!"FE".equals(manifesto.getTpStatusManifesto().getValue()) && !"CA".equals(manifesto.getTpStatusManifesto().getValue())) {
				if(manifesto.getFilialByIdFilialDestino().getIdFilial().equals(getSessionIdFilial())) {
					eventoVolumeService.generateEventoByManifesto(
							manifesto.getIdManifesto(), ConstantesSim.EVENTO_INICIO_DESCARGA_ENTREGA, ConstantesSim.TP_SCAN_FISICO);
					
					eventoDispositivoUnitizacaoService.generateEventoByManifesto(
							manifesto.getIdManifesto(), ConstantesSim.EVENTO_INICIO_DESCARGA_ENTREGA, ConstantesSim.TP_SCAN_FISICO, null );
				}
			}
		}
	}

	/**
	 * REGRA 45.03.04.04 Item 4 a iii 
	 * Realiza a descarga caso o Volume lido ESTEJA previsto para descarga na filial
	 */
	private void executeIniciarDescargaVolumePrevistoNaFilial( VolumeNotaFiscal volumeNotaFiscal, 
														ManifestoNacionalVolume manifestoNacionalVolume, 
														Long idFilialUsuarioLogado, 
														CarregamentoDescarga carregamentoDescarga,
														String tpScan){
					
		Long idManifesto = manifestoNacionalVolume.getManifestoViagemNacional().getManifesto().getIdManifesto();
		Long idDoctoServico = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico();			
		//REGRA 45.03.04.04 Item 4 a iii 3 - Descarga
		//a - Gera evento fim de descarga
		//b - alterar localização
		eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, tpScan);		
		
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
						//Apoyo 201908086 LMSA-8359
						ocorrenciaDoctoServicoService.validateAgendamentoEntrega(idFilialUsuarioLogado, doctoServico);
						
						
					}
				}
		
				//PCE descarga
				List<DoctoServico> doctos = new ArrayList<DoctoServico>();
				doctos.add(volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento());	
				carregamentoDescargaService.validatePCE(doctos, 
						ProcessoPce.ID_PROCESSO_PCE_DESCARGA,
						EventoPce.ID_EVENTO_PCE_TERMINO_DESCARGA, 
						OcorrenciaPce.ID_OCORR_PCE_TERM_DESCARGA_TERMINO_DESCARGA_RECEPCAO );
		}
	}
		
	public void executeIniciarDescargaVolumeColetaEntregaPrevistoNaFilial( VolumeNotaFiscal volumeNotaFiscal, 
														ManifestoEntregaVolume manifestoEntregaVolume, 
														String tpScan){
		Long idManifesto = manifestoEntregaVolume.getManifestoEntrega().getManifesto().getIdManifesto();
		DoctoServico docto = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento();
		
		// Verificação da existência do evento de "FIM DE DESCARGA" no volume foi removido para atender a demanda LMS-6727
		// 31 Evento de Descarga
		eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), ConstantesSim.EVENTO_FIM_DESCARGA, tpScan);
					
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
			manifestoEntregaVolumeService.findByDoctoServicoAndManifestoAndLocalizacaoVolume(
				getSessionIdFilial(), idManifesto, docto.getIdDoctoServico(), lstCdLocMerc);
		
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
		
		List<Short> cdsLocalizacao = new ArrayList<Short>();		
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
			//PCE descarga
			List<DoctoServico> doctos = new ArrayList<DoctoServico>();
			doctos.add(volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento());			
			carregamentoDescargaService.validatePCE(doctos,
					ProcessoPce.ID_PROCESSO_PCE_RECEPCAO, 
					EventoPce.ID_EVENTO_PCE_INICIO_DESCARGA_RECEPCAO, 
					OcorrenciaPce.ID_OCORR_PCE_INIC_DESCARGA_INICIO_DESCARGA_RECEPCAO );					
			}
		}

	public void desunitizaDispositivo(DispositivoUnitizacao dispositivoUnitizacao){
		List<DispositivoUnitizacao> lstDispUnit = new ArrayList<DispositivoUnitizacao>();
		
		lstDispUnit.add(dispositivoUnitizacao);
		
		if(dispositivoUnitizacao.getDispositivoUnitizacaoPai() != null){
			
			DispositivoUnitizacao dispUnitizador = dispositivoUnitizacao.getDispositivoUnitizacaoPai(); 
			while (dispUnitizador!=null) {
				unitizacaoService.executeEventoDispositivoDesunitizadoTpScan(dispUnitizador.getIdDispositivoUnitizacao(), ConstantesSim.TP_SCAN_CASCADE);
				dispUnitizador = findPaiDispUnit(dispUnitizador.getIdDispositivoUnitizacao());
			}
			
			unitizacaoService.storeDesunitizarParcial(dispositivoUnitizacao.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao(), null, lstDispUnit);
			unitizacaoService.executeEventoDispositivoDesunitizadoTpScan(dispositivoUnitizacao.getIdDispositivoUnitizacao(), ConstantesSim.TP_SCAN_FISICO);
		}
	}
	
	public void desunitizaVolume(VolumeNotaFiscal volumeNotaFiscal){
		List<VolumeNotaFiscal> lstVolume = new ArrayList<VolumeNotaFiscal>();
		lstVolume.add(volumeNotaFiscal);
		unitizacaoService.storeDesunitizarParcial(volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao(), lstVolume, null);
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
		List<Long> ids = new ArrayList<Long>();
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
	public void validateDoctoServico( ControleCarga controleCarga, Long idFilialUsuarioLogado ){
		ocorrenciaEntregaService.validateDoctoServicoComOcorrenciaVinculada( controleCarga.getIdControleCarga(), idFilialUsuarioLogado);
	}
	
	
	/**
	 * Regra 45.03.04.04 Item 3 a ii - - Iniciar Descarga de Viagem
	 * Faz a validação do PCE (Pedido Cliente Especial)
	 * 
	 * @param controleCarga
	 * @param cdProcesso
	 * @param cdEvento
	 * @param cdOcorrencia
	 * @return list com os códigos PCE
	 */
	public List<Long> validatePCE(ControleCarga controleCarga, Long cdProcesso, Long cdEvento, Long cdOcorrencia){
		final List doctosServicosList = new ArrayList();
		final List<Manifesto> listManifesto = manifestoService.findManifestoByIdControleCarga(controleCarga.getIdControleCarga(), null, null, null);
		for(final Manifesto manifesto : listManifesto){
			/** Otimização LMS-815 */
    		final ProjectionList projection = Projections.projectionList()
    			.add(Projections.property("ds.id"), "idDoctoServico")
    			.add(Projections.property("cr.id"), "clienteByIdClienteRemetente.idCliente")
    			.add(Projections.property("cd.id"), "clienteByIdClienteDestinatario.idCliente");
		
    		final Map<String, String> alias = new HashMap<String, String>();
    		alias.put("ds.clienteByIdClienteRemetente", "cr");
    		alias.put("ds.clienteByIdClienteDestinatario", "cd");
		
    		final List<DoctoServico> doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection, alias);
			doctosServicosList.addAll(doctoServicoList);
		}
	
		return carregamentoDescargaService.validatePCE(doctosServicosList, cdProcesso, cdEvento, cdOcorrencia);
	}

	/**
	 * Regra Regra 45.03.04.04 Item 3 b i - Iniciar Descarga de Viagem
	 * Retorna os box disponíveis na filial
	 *  
	 * @return
	 */
	public List findBoxVigente( Long idFilialUsuarioLogado ){
		//Retorno de box default - Similiar ao IniciarDescargaAction.getBoxFromCarregamentoDescarga(TypedFlatMap criteria)
		return boxService.findBoxVigentePorFilial( idFilialUsuarioLogado );
	}
	
	/**
	 * Regra 45.03.04.04 Item 3 c i - Iniciar Descarga de Viagem
	 * Verifica se a quilometragem deve ser informada na portaria.
	 * 
	 * Caso positivo retorna a quilometragem informada.
	 * Caso negativo o usuário deve informar a quilometragem na interface.
	 * 
	 * @param controleCarga
	 * @return ControleQuilometragem
	 */
	public ControleQuilometragem findControleQuilometragem( ControleCarga controleCarga, boolean informaKmPortaria ){
		ControleQuilometragem controleQuilometragem = null;
		
		if( informaKmPortaria ){
			controleQuilometragem = controleQuilometragemService.findControleQuilometragemByIdControleCargaByIdFilial(
									controleCarga.getIdControleCarga(),	getSessionIdFilial(), Boolean.FALSE);
			if (controleQuilometragem == null) {
				throw new BusinessException("LMS-05103");
			}
			
		}
		return controleQuilometragem;
	}
	
	/**
	 * 45.03.04.04 Item 3 e 
	 * Inicia a descarga caso o Controle de Carga não possua informação de Início de Descarga
	 * @param controleCarga
	 */
	public void storeIniciarDescarga(ControleCarga controleCarga, TypedFlatMap parameters, List<LacreControleCarga> lacreControleCargaList ){	
		
		CarregamentoDescarga carregamentoDescarga = 
			carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), getSessionIdFilial(), "D");
	
		if( (carregamentoDescarga!=null) && (carregamentoDescarga.getDhInicioOperacao()!=null && carregamentoDescarga.getDhFimOperacao()==null)){
			//Está descarga já foi iniciada.
			throw new BusinessException("LMS-45069");
		}
		//45.03.04.04 Item 3 - Iniciar Descarga de Viagem
		// 45.03.04.04 Item 3 a i  
		validateDoctoServico(controleCarga, getSessionIdFilial());
			
		carregamentoDescargaService.storeInicioDescargaByMWW(parameters, JTDateTimeUtils.getDataHoraAtual(), lacreControleCargaList);
	}
	
	public Map storeDescargaDispositivo(String barcode, Long idControleCarga){
		List<Map<String,Object>> confirmacoes = new ArrayList<Map<String,Object>>();
	
		ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
	
		DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findByBarcode(barcode);
		
		Map retorno = new HashMap();
		
		Map<String, Object> confirm = new HashMap<String, Object>();
		if(dispositivoUnitizacao!=null){
			if(dispositivoUnitizacao.getDispositivoUnitizacaoPai() == null ){
				if(isDispositivoVinculadoControleCarga(controleCarga.getIdControleCarga(), dispositivoUnitizacao.getIdDispositivoUnitizacao(), "C")){
					CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), getSessionIdFilial(), "D");
					// E - Em Conferência MWW
					// O - Concluído MWW
					if(carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("O")) || carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("F"))){
						//LMS-45050	A descarga desse carregamento já foi concluída.
						throw new BusinessException("LMS-45050");
					}else if(carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("I"))){
						Map<String, Object> executeDescargaByDispositivoUnitizacao = executeDescargaByDispositivoUnitizacao(controleCarga, getSessionIdFilial(), dispositivoUnitizacao);
						if(executeDescargaByDispositivoUnitizacao!=null && !executeDescargaByDispositivoUnitizacao.isEmpty()){
							confirmacoes.add(executeDescargaByDispositivoUnitizacao);
						}
					}	
				}else{
					//isso significa que o controle de carga é diferente do controle de carga do dispositivo vinculado
					//LMS-45007 “O dispositivo não pertence ao Controle de Carga. A localização atual no sistema é {1}. 
					//Deseja descarregar o dispositivo e seus volumes na sua unidade e ajustar a localização do volume ?”
					confirm.put("message", configuracoesFacade.getMensagem("LMS-45007", 
							new Object []{dispositivoUnitizacao.getLocalizacaoFilial()!=null?
									dispositivoUnitizacao.getLocalizacaoFilial().getSgFilial():""}));
					confirm.put("key", "LMS-45007");			
					confirmacoes.add(confirm);	
					
				}
				if(!confirmacoes.isEmpty()){
					retorno.put("messages", confirmacoes);
				}
			}
			
			Map dispositivoMapped = unitizacaoService.findMapDispositivoUnitizacao(dispositivoUnitizacao, idControleCarga, "D");
			
			verificaConhecimentoCompleto(controleCarga, (List<Map>)dispositivoMapped.get("conhecimentos"));
			verificaDispositivoCompleto(controleCarga, (List<Map>)dispositivoMapped.get("dispositivos"));
			
			retorno.putAll(dispositivoMapped);
		}else{
			throw new BusinessException("LMS-45018");
		}
		return retorno;
	}
	
	private void verificaDispositivoCompleto(ControleCarga controleCarga,	List<Map> list) {
		if(list != null){
			for (Map<String, Object> disp : list) {
				verificaConhecimentoCompleto(controleCarga, (List<Map>)disp.get("conhecimentos"));
				verificaDispositivoCompleto(controleCarga, (List<Map>)disp.get("dispositivos"));
			}
		}
	}
	private void verificaConhecimentoCompleto(ControleCarga controleCarga,	List<Map> list) {
		if(list != null){
			for (Map<String, Object> con : list) {
				DoctoServico doctoServico = doctoServicoService.findById(Long.parseLong(con.get("idDoctoServico").toString()));
				Integer qtVolumesEmDescargaByConhecimento = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaConhecimento(controleCarga.getIdControleCarga(), doctoServico.getIdDoctoServico(), "D");

				con.put("qtVolumes", doctoServico.getQtVolumes());
				con.put("completo", (qtVolumesEmDescargaByConhecimento.compareTo(doctoServico.getQtVolumes()) == 0));
			}
		}
	}

	public Map<String, Object> storeDescargaVolume(String barcode, Long idControleCarga){
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
		VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findVolumeByBarCodeUniqueResult(barcode);
		Map<String, Object> volumeMapped = new HashMap<String, Object>();
		List<Map<String,Object>> volumes = new ArrayList<Map<String,Object>>();
		Map<String, Object> retorno = new HashMap<String, Object>();
		
		if(ConstantesExpedicao.TP_VOLUME_MESTRE.equals(volumeNotaFiscal.getTpVolume())){
			throw new BusinessException("LMS-45081");
		}
		
		Integer totalControles = controleCargaService.countByVolumeEmDescargaFilial(volumeNotaFiscal.getIdVolumeNotaFiscal(), 
																	SessionUtils.getFilialSessao().getIdFilial(), idControleCarga);
		if ((Integer.valueOf(0)).compareTo(totalControles) !=  0) {
			//LMS-45063 - Este volume já está descarregado.
			throw new BusinessException("LMS-45063");
		}
		
		volumeMapped.put("ocorrenciaEntrega", getOcorrenciaEntregaMapped(
				ocorrenciaEntregaService.findLastOcorrenciaEntregaByIdVolumeAndFilial(volumeNotaFiscal.getIdVolumeNotaFiscal(), SessionUtils.getFilialSessao().getIdFilial())));

		//Regra 45.03.04.04 Item 4 i - verifica se o Volume lido está unitizado
 		if( volumeNotaFiscal.getDispositivoUnitizacao() != null ){
			volumeMapped.put("idDispositivoUnitizacao", volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
			//"34" diferente de 34 - VolumeNaoPrevistoNaFilial
			volumeMapped.put("cdLocalizacaoMercadoria", volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
			if(volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao()!=null){
				DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao()); 
				volumeMapped.put("idDispositivoUnitizacaoAvo", dispositivoUnitizacao.getDispositivoUnitizacaoPai()!=null?
						dispositivoUnitizacao.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao():null);
			}
		} else {
			CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), getSessionIdFilial(), "D");
			
			// E - Em Conferência MWW
			// O - Concluído MWW
			List<Map<String,Object>> confirmacoes = new ArrayList<Map<String,Object>>();
			if ("O".equals(carregamentoDescarga.getTpStatusOperacao().getValue()) 
					|| "F".equals(carregamentoDescarga.getTpStatusOperacao().getValue())) {
				//LMS-45050	A descarga desse carregamento já foi concluída.
				throw new BusinessException("LMS-45050");
			} else if ("I".equals(carregamentoDescarga.getTpStatusOperacao().getValue())) {
				Map<String, Object> executeDescargaVolume = executeDescargaVolume(volumeNotaFiscal, controleCarga, getSessionIdFilial(), carregamentoDescarga);
				if(!executeDescargaVolume.isEmpty()){
					confirmacoes.add(executeDescargaVolume);
				}
			}

			if(!confirmacoes.isEmpty()){
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
		
		Integer qtVolumesEmDescarga = this.countVolumesCarregamentoDescarga(idControleCarga, idDoctoServico, volumeNotaFiscal.getDispositivoUnitizacao());
		Integer qtVolumesEmDescargaByConhecimento = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga, idDoctoServico, "D");
		retorno.put("qtVolumesDescarregados", qtVolumesEmDescarga);

		retorno.put("qtVolumes", docto.getQtVolumes());
		
		if(controleCarga.getTpControleCarga().getValue().equals("V")){
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
	}
	
	public Map storeDescargaVolumeConfirmacao(Long idVolumeNotaFiscal, Long idControleCarga){
		Map retorno = new HashMap();

		//Gera Evento Descarga
		//30 Evento de Descarga
		//Altera Localizacao para NO TERMINAL 24
		VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findVolumeById(idVolumeNotaFiscal);
		storeDescargaVolume(idVolumeNotaFiscal);
		Long idDoctoServico = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico();
		Conhecimento docto = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento();
		
		ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
		CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), getSessionIdFilial(), "D");

		if(carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("O")) || carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("F"))){
			//LMS-45050	A descarga desse carregamento já foi concluída.
			throw new BusinessException("LMS-45050");
		}else if(carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("E"))){
			carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
			carregamentoDescargaService.store(carregamentoDescarga);
		}	
		
		
		if(controleCarga.getTpControleCarga().getValue().equals("V")) {
			ManifestoNacionalVolume manifestoNacionalVolume = manifestoNacionalVolumeService.findByIdVolumeAndIdControleCarga(volumeNotaFiscal.getIdVolumeNotaFiscal(),controleCarga.getIdControleCarga());
			if(carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("I"))) {
				if(manifestoNacionalVolume!=null){
					executeIniciarDescargaVolumePrevistoNaFilial(volumeNotaFiscal, manifestoNacionalVolume, getSessionIdFilial(), carregamentoDescarga, "SF");
				}else{
					executeIniciarDescargaSemManifesto(volumeNotaFiscal.getIdVolumeNotaFiscal());
				}
			}
			
			if(manifestoNacionalVolume != null){
				Integer qtVolume = docto.getQtVolumes();
				Integer qtVolumesEmDescarga = this.countVolumesCarregamentoDescarga(idControleCarga, idDoctoServico, volumeNotaFiscal.getDispositivoUnitizacao());
				retorno.put("qtVolumesDescarregados", qtVolumesEmDescarga);	
				retorno.put("qtVolumes", qtVolume);	
				retorno.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volumeNotaFiscal.getIdVolumeNotaFiscal()));
			}
		
		} else if(controleCarga.getTpControleCarga().getValue().equals("C")){
			ManifestoEntregaVolume manifestoEntregaVolume = getManifestoEntregaVolumeService().findByIdVolumeAndIdControleCarga(volumeNotaFiscal.getIdVolumeNotaFiscal(),controleCarga.getIdControleCarga());
			
			boolean isVolumePedidoColeta = pedidoColetaService.isVolumePrevistoDescargaAereo(volumeNotaFiscal.getIdVolumeNotaFiscal(), 
																					controleCarga.getIdControleCarga(),
																					SessionUtils.getFilialSessao().getIdFilial(),
																					ConstantesSim.CD_MERCADORIA_EM_DESCARGA);
			
			if(carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("I"))){
				if(manifestoEntregaVolume!=null){
					this.executeIniciarDescargaVolumeColetaEntregaPrevistoNaFilial(volumeNotaFiscal, manifestoEntregaVolume, ConstantesSim.TP_SCAN_FISICO);
				}else{
					this.executeDescargaPedidoColeta(volumeNotaFiscal, isVolumePedidoColeta);
				}
			}
			
			if(manifestoEntregaVolume != null || isVolumePedidoColeta){
				Integer qtVolume = docto.getQtVolumes();
				Integer qtVolumesEmDescarga = this.countVolumesCarregamentoDescarga(idControleCarga, idDoctoServico, volumeNotaFiscal.getDispositivoUnitizacao());
				Integer qtVolumesEmDescargaByConhecimento = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga, idDoctoServico, "D");
				retorno.put("qtVolumesDescarregados", qtVolumesEmDescarga);	
				retorno.put("qtVolumes", qtVolume);	
				retorno.put("completo", qtVolume.compareTo(qtVolumesEmDescargaByConhecimento) == 0);
				retorno.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volumeNotaFiscal.getIdVolumeNotaFiscal()));
			}
		}
		
		this.volumeSobraService.validateStoreVolumeSobra(volumeNotaFiscal,
				carregamentoDescarga,
				controleCarga.getIdControleCarga(),
				controleCarga.getTpControleCarga().getValue(),
				SessionUtils.getFilialSessao(),
				usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
				
		if( volumeNotaFiscal.getDispositivoUnitizacao() != null ){
			retorno.put("idDispositivoUnitizacao", volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
		}

		if( volumeNotaFiscal.getLocalizacaoMercadoria()!=null){
			retorno.put("cdLocalizacaoMercadoria", volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
		}
		
		retorno.put("sgFilialDocumento", docto.getFilialOrigem().getSgFilial());
		retorno.put("nrConhecimento", docto.getNrConhecimento());		
		
		Map<String, Object> volumeMapped = new HashMap<String, Object>();
		volumeMapped.put("nrSequencia", volumeNotaFiscal.getNrSequencia());
		volumeMapped.put("idVolumeNotaFiscal", volumeNotaFiscal.getIdVolumeNotaFiscal());		
		
		List<Map<String,Object>> volumes = new ArrayList<Map<String,Object>>();
		volumes.add(volumeMapped);
		
		retorno.put("volumes", volumes);
		
		return retorno;
	}
	
	private void executeDescargaPedidoColeta(VolumeNotaFiscal volumeNotaFiscal, boolean isVolumePedidoColeta) {
		if(isVolumePedidoColeta){
			this.executeIniciarDescargaVolumePedidoColeta(volumeNotaFiscal, ConstantesSim.TP_SCAN_FISICO);
		}else{
			this.executeIniciarDescargaSemManifesto(volumeNotaFiscal.getIdVolumeNotaFiscal());
		}
	}

	private void executeIniciarDescargaVolumePedidoColeta(VolumeNotaFiscal volumeNotaFiscal, String tpScan) {
		
		Long idDoctoServico = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico();
		
		if (!eventoVolumeService.hasEventoVolumeDescarregado(volumeNotaFiscal.getIdVolumeNotaFiscal())) {
			eventoVolumeService.generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, tpScan);
		}

		List<VolumeNotaFiscal> lstVolumes = 
			volumeNotaFiscalService.findByIdConhecimentoAndIdLocalizacaoFilial(idDoctoServico, getSessionIdFilial());
		
		boolean hasVolumes = CollectionUtils.exists(lstVolumes, new Predicate() {
		    public boolean evaluate(Object obj) {
		    	return ConstantesSim.CD_MERCADORIA_EM_DESCARGA.equals(((VolumeNotaFiscal)obj).getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
		    }
		});
		
		List<Short> cdsLocalizacao = new ArrayList<Short>();		
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
						getSessionIdFilial(), 
						docto.getFilialByIdFilialOrigem().getSgFilial() + " " + 
								FormatUtils.formataNrDocumento(docto.getNrDoctoServico().toString(), docto.getTpDocumentoServico().getValue()), 
						JTDateTimeUtils.getDataHoraAtual(), 
						docto.getTpDocumentoServico().getValue());
			}				
		}
		
	}

	public void storeDescargaDispositivoConfirmacao(Long idDispositivoUnitizacao, Long idControleCarga){
		ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
		
		DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(idDispositivoUnitizacao);
		
		executeDescargaByDispositivoUnitizacao(controleCarga, getSessionIdFilial(), dispositivoUnitizacao);
		
		CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), getSessionIdFilial(), "D");
		// E - Em Conferência MWW
		// O - Concluído MWW
		if(carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("O")) || carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("F"))){
			//LMS-45050	A descarga desse carregamento já foi concluída.
			throw new BusinessException("LMS-45050");
		}else if(carregamentoDescarga.getTpStatusOperacao().equals(new DomainValue("E"))){
			carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
			carregamentoDescargaService.store(carregamentoDescarga);
		}
		
	}	
	
	private int findQtVolumesCarregadosViagem(Long idControleCarga, Long idDoctoServico) {
		return manifestoNacionalVolumeService.findByControleCargaAndDoctoServico(idControleCarga, idDoctoServico).size();								
	}
	
	private int findQtVolumesCarregadosEntrega(Long idControleCarga, Long idDoctoServico) {
		return manifestoEntregaVolumeService.findByControleCargaAndDoctoServico(idControleCarga, idDoctoServico).size();								
	}
	
	private int findVolumesEmDescargaByIdDoctoAndIdFilial(Long idDoctoServico, Long idFilial, DomainValue tpControleCarga) {
		return volumeNotaFiscalService.findVolumesEmDescargaByIdDoctoAndIdFilial(idDoctoServico, idFilial, tpControleCarga).size();								
	}
	
	private int findQtVolumesTotalDescarregadosViagem(Long idControleCarga, Long idDoctoServico) {
		return volumeNotaFiscalService.findVolumesTotalDescarregadosViagem(idControleCarga, idDoctoServico).size();								
	}
	
	private int findQtVolumesDescarregadosEntrega(Long idControleCarga, Long idDoctoServico) {
		return volumeNotaFiscalService.findVolumesDescarregadosEntrega(idControleCarga, idDoctoServico).size();								
	}
	
	private int findQtVolumesTotalDescarregadosEntrega(Long idControleCarga, Long idDoctoServico) {
		return volumeNotaFiscalService.findVolumesTotalDescarregadosEntrega(idControleCarga, idDoctoServico).size();								
	}
	
	
	private int findQtVolumesDescarregadosViagem(Long idControleCarga, Long idDoctoServico) {
		return volumeNotaFiscalService.findVolumesDescarregadosViagem(idControleCarga, idDoctoServico).size();								
	}
	
	private int findVolumesByIdDoctoAndIdFilial(Long idDoctoServico, Long idFilial) {
		return volumeNotaFiscalService.findVolumesByIdDoctoAndIdFilial(idDoctoServico, idFilial).size();								
	}
	
	
	
	
	
	private Map<String, Object> getOcorrenciaEntregaMapped(OcorrenciaEntrega ocorrenciaEntrega) {
		if(ocorrenciaEntrega == null) return null;
		
		Map<String, Object> ocorrenciaEntregaMapped = new HashMap<String, Object>();
		
		ocorrenciaEntregaMapped.put("idOcorrenciaEntrega", ocorrenciaEntrega.getIdOcorrenciaEntrega());
		ocorrenciaEntregaMapped.put("cdOcorrenciaEntrega", ocorrenciaEntrega.getCdOcorrenciaEntrega());
		ocorrenciaEntregaMapped.put("dsOcorrenciaEntrega", ocorrenciaEntrega.getDsOcorrenciaEntrega());
		
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
	 * @param filialUsurioLogado
	 * @return
	 */
	private boolean isTrechoControleCarga( ControleCarga controleCarga, Long idFilialUsuarioLogado ){
		Integer controlesTrecho = controleTrechoService.getRowCountControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(controleCarga.getIdControleCarga(),
				null, idFilialUsuarioLogado );
		
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
	private boolean isFilialAtualizaStatus( ControleCarga controleCarga, Long idFilialUsuarioLogado ){
		if( controleCarga.getFilialByIdFilialAtualizaStatus().getIdFilial().equals( idFilialUsuarioLogado )  ){
			return true;
		}else{
			throw new BusinessException("LMS-05070");
		}
	}
	
	
	/**
	 * Regra 45.03.04.05    2 a ii 2 
	 * Preenche a data/hora de fim de operação para o CarregamentoDescarga referente ao Controle de Carga
	 * @param idFilialUsuarioLogado
	 * @param idControleCarga
	 * @param dhFimOperacao
	 */
	public void storeFimDescargaCarregamentoDescarga(Long idFilialUsuarioLogado, ControleCarga controleCarga){
		
		List<CarregamentoDescarga> listCarregamentoDescarga = 
			carregamentoDescargaService.findCarregamentoDescargaByIdFilialByIdControleCarga(idFilialUsuarioLogado, controleCarga.getIdControleCarga());
		
		/*
		 E - Em Conferência MWW
		 O - Concluído MWW
		 I - Iniciado
		 */

		if(listCarregamentoDescarga != null) {
			for(CarregamentoDescarga carregamentoDescarga : listCarregamentoDescarga ){
				if( "D".equalsIgnoreCase(carregamentoDescarga.getTpOperacao().getValue()) 
						&& "I".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()) ){
					carregamentoDescarga.setTpStatusOperacao(new DomainValue("O"));
					if(carregamentoDescarga.getBox() != null){
						carregamentoDescarga.getBox().setTpSituacaoBox(new DomainValue("L"));
						if(carregamentoDescarga.getBox().getDoca() != null){
							carregamentoDescarga.getBox().getDoca().setTpSituacaoDoca(new DomainValue("L"));
						}
					}
					carregamentoDescargaService.store(carregamentoDescarga);
					//Finaliza a equipe de operação
					equipeOperacaoService.storeEquipeOperacaoFimDescarga(controleCarga, carregamentoDescarga);
					
				}else if( "D".equalsIgnoreCase(carregamentoDescarga.getTpOperacao().getValue()) 
						&& ("O".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()) 
							 ||("F".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()))) ){
					throw new BusinessException("LMS-45050");
				}
			}
		}
	}
	
	/**
	 * Conclui a descarga mas como há divergencias valida se ela não foi reiniciada por outro processo
	 * @param idFilialUsuarioLogado
	 * @param idControleCarga
	 */
	public void storeFimDescargaCarregamentoDescargaComDivergencia(Long idFilialUsuarioLogado, ControleCarga controleCarga){
		
		List<CarregamentoDescarga> listCarregamentoDescarga = 
			carregamentoDescargaService.findCarregamentoDescargaByIdFilialByIdControleCarga(idFilialUsuarioLogado, controleCarga.getIdControleCarga());
		
		
		if(listCarregamentoDescarga != null) {
			for(CarregamentoDescarga carregamentoDescarga : listCarregamentoDescarga ){
				if( "D".equalsIgnoreCase(carregamentoDescarga.getTpOperacao().getValue()) 
						&& "I".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()) ){
					//Esta descarga não pode ser concluída pois foi reiniciada.
					throw new BusinessException("LMS-45051");
				}else if( "D".equalsIgnoreCase(carregamentoDescarga.getTpOperacao().getValue()) 
						&& "E".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()) ){
					carregamentoDescarga.setTpStatusOperacao(new DomainValue("O"));
					if(carregamentoDescarga.getBox() != null){
						carregamentoDescarga.getBox().setTpSituacaoBox(new DomainValue("L"));
						if(carregamentoDescarga.getBox().getDoca() != null){
							carregamentoDescarga.getBox().getDoca().setTpSituacaoDoca(new DomainValue("L"));
						}
					}
					carregamentoDescargaService.store(carregamentoDescarga);
					//Finaliza a equipe de operação
					equipeOperacaoService.storeEquipeOperacaoFimDescarga(controleCarga, carregamentoDescarga);
					
					
				}else if( "D".equalsIgnoreCase(carregamentoDescarga.getTpOperacao().getValue()) 
						&& ("O".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()) 
								||("F".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()))) ){
					//A descarga desse carregamento já foi concluída.
					throw new BusinessException("LMS-45050");
				}
			}
		}
		
	}
	
	
	/**
	 * Metodo para a troca de status de operacao no carregamento de descarga, utilizado basicamente para alterar o status de 
	 * iniciado para em conferencia e de em conferencia para concluido 
	 * @param idFilialUsuarioLogado
	 * @param idControleCarga
	 * @param tpStatusOperacao
	 */
	public void executeTpStatusOperacaoCarregamentoDescarga(Long idFilialUsuarioLogado, ControleCarga controleCarga){
		
		List<CarregamentoDescarga> listCarregamentoDescarga = 
			carregamentoDescargaService.findCarregamentoDescargaByIdFilialByIdControleCarga(idFilialUsuarioLogado, controleCarga.getIdControleCarga());
		
		if(listCarregamentoDescarga != null) {
			for(CarregamentoDescarga carregamentoDescarga : listCarregamentoDescarga ){
				if( "D".equalsIgnoreCase(carregamentoDescarga.getTpOperacao().getValue()) 
						&& ("O".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()) 
								 ||("F".equalsIgnoreCase(carregamentoDescarga.getTpStatusOperacao().getValue()))) ) {
						throw new BusinessException("LMS-45050");
				}
			}
		}
	}
	
	public void executeChangeTPStatusControleCarga(Long idControleCarga, String tpStatusControleCarga){
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
		controleCarga.setTpStatusControleCarga(new DomainValue(tpStatusControleCarga));
		controleCargaService.store(controleCarga);
	}
	

	/**
	 * Realiza a mudança de localização do volume para a filial onde o usuario esta logado, conforme solicitação
	 * CQPRO00026985
	 * @param volume
	 */
	private void executeEventoVolumeLocalizacao(VolumeNotaFiscal volume){
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		if(filialUsuarioLogado!=null){
			Long milis = System.currentTimeMillis();
			HistoricoFilial historicoFilial = SessionUtils.getUltimoHistoricoFilialSessao();
			milis = System.currentTimeMillis() - milis;
			log.warn("[MWW] " + " [" + String.valueOf(milis) + "ms] idVolume = " + volume.getIdVolumeNotaFiscal().toString()
					+  " historicoFilialService.findUltimoHistoricoFilial");

			String tpFilial = historicoFilial.getTpFilial().getValue();
			//Matriz
			if (tpFilial != "MA") {
				volume.setLocalizacaoMercadoria(localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(ConstantesSim.CD_MERCADORIA_NO_TERMINAL));
				volume.setLocalizacaoFilial(filialUsuarioLogado);
				volumeNotaFiscalService.store(volume);
			}
		}
	}
	
	public java.io.Serializable executeEventoVolumeLido(Long idVolumeNotaFiscal, String tpScan) {
		return getEventoVolumeService().generateEventoVolume(idVolumeNotaFiscal, "CD_EVENTO_VOLUME_LIDO", tpScan);
	}
	
	public void executeIniciarDescargaSemManifesto(Long idVolume){
		eventoVolumeService.generateEventoVolume(idVolume, ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, ConstantesSim.TP_SCAN_CASCADE);
	}
	
	
	
	
	/**
	 * Regra 45.03.04.05   2 a i 1
	 * Verifica se o controle de carga está em descarga
	 * @param controleCarga
	 * @return true se estiver com a situação em descarga "ED"
	 * 			BusinessException caso contrário
	 */
	public boolean  isControleCargaEmDescarga( ControleCarga controleCarga ){
		// “ED” (Em Descarga)
		// “EP” (Em Descarga Parcial)
		if( !"ED".equalsIgnoreCase(controleCarga.getTpStatusControleCarga().getValue()) && !"EP".equalsIgnoreCase(controleCarga.getTpStatusControleCarga().getValue() )){
			throw new BusinessException("LMS-45005");
		}
		return true;
	}
	
	
	/**
	 * Valida se o box é diferente do box sugerido se for desocupa box sugerido e ocupa box
	 * @param idControleCarga
	 * @param idBox
	 * @return
	 */
	public Long storeOcuparDesocuparBox(Long idControleCarga, Long idBox){
		CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(idControleCarga, getSessionIdFilial(), "D");
		
		if (carregamentoDescarga!=null) {
			if (carregamentoDescarga.getTpOperacao().getValue().equals("D")	&& carregamentoDescarga.getTpStatusOperacao().getValue().equals("P")) {
				Long idBoxSugerido = carregamentoDescarga.getBox().getIdBox();
				if(!idBoxSugerido.equals(idBox)){
					getBoxService().storeDesocuparBox(idBoxSugerido);
					getBoxService().storeOcuparBox(idBox);
				}
			}
		}
		return idBox;
	}
	
	/**
	 * Verifica e persiste a sobra de filial quando necessário.
	 */
	public void storeIdentificarSobra(VolumeNotaFiscal volumeNotaFiscal) {
		Filial filialLogada = SessionUtils.getFilialSessao();
		Long idVolume = volumeNotaFiscal.getIdVolumeNotaFiscal();
	
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("volumeNotaFiscal.idVolumeNotaFiscal", idVolume);
		criteria.put("filial.idFilial", filialLogada.getIdFilial());
		criteria.put("dhCriacao", JTDateTimeUtils.getDataHoraAtual());
		List listaSobrasFilial = volumeSobraFilialService.find(criteria);
	
		if (listaSobrasFilial == null || listaSobrasFilial.isEmpty()) {
	
			List<ManifestoNacionalVolume> listaManifestoNacionalVolumes = manifestoNacionalVolumeService
					.findByVolumeNotaFiscalAndFilialDestino(idVolume, filialLogada.getIdFilial());
	
			if (listaManifestoNacionalVolumes == null || listaManifestoNacionalVolumes.isEmpty()) {
				
				//Obs.: Caso a busca EVENTO_VOLUME.id_evento_volume retorne NULO, NÃO chamar a função IdentificacaoSobra.
				EventoVolume eventoVolume = eventoVolumeService.findUltimoEventoVolumeDeDescargaByFilial(idVolume, filialLogada.getIdFilial());
				if (eventoVolume != null) {
					VolumeSobraFilial volumeSobraFilial = new VolumeSobraFilial();
					
					volumeSobraFilial.setFilial(filialLogada);
					volumeSobraFilial.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
					volumeSobraFilial.setEventoVolume(eventoVolume);
					volumeSobraFilial.setVolumeNotaFiscal(volumeNotaFiscal);
					
					volumeSobraFilialService.store(volumeSobraFilial);
				}
			}
		}
	}
	
	public List<Map<String, Object>> findAwbs(Long idControleCarga) {
		List<Map<String, Object>> awbMapList = new ArrayList<Map<String,Object>>();
		if (idControleCarga != null) {
			
			List<PedidoColeta> pedidos = pedidoColetaService.findPedidoColetaByIdControleCargaByTpStatusColeta(idControleCarga, null);
			
			List<Conhecimento> ctos = new ArrayList<Conhecimento>();
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
					
					this.addVolumesToMap(volumes, idControleCarga, awbMap);
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

	private void addVolumesToMap(List<VolumeNotaFiscal> volumes, Long idControleCarga, Map<String, Object> awbMap) {
		for (VolumeNotaFiscal volume : volumes) {
			DispositivoUnitizacao du = volume.getDispositivoUnitizacao();
			
			if (du != null && du.getIdDispositivoUnitizacao() != null){
				Long idDisp = du.getIdDispositivoUnitizacao();				
				
				Map<String, Object> dispMap = findDispMapped(idControleCarga, awbMap, idDisp);
				
				List<Map<String, Object>> listCon = (List<Map<String, Object>>)dispMap.get("conhecimentos");
				this.addVolumeToMap(idControleCarga, listCon, volume);
			} else{
				List<Map<String, Object>> listCon = (List<Map<String, Object>>)awbMap.get("conhecimentos");
				this.addVolumeToMap(idControleCarga, listCon, volume);
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

	private void addVolumeToMap(Long idControleCarga, List<Map<String, Object>> listCon, VolumeNotaFiscal volume) {
		final Conhecimento con = volume.getNotaFiscalConhecimento().getConhecimento();
		
		@SuppressWarnings("unchecked")
		Boolean exists = CollectionUtils.exists(listCon, new Predicate() {
		    public boolean evaluate(Object obj) {
		    	return con.getIdDoctoServico().equals(((Map<String, Object>)obj).get("idDoctoServico"));
		    }
		});
		
		if(!exists){
			listCon.add(this.getConMapped(idControleCarga, con, volume));
		}
	}

	private Map<String, Object> getConMapped(Long idControleCarga, Conhecimento con, VolumeNotaFiscal volume) {
		
		Map<String, Object> conhecimentoMapped = new HashMap<String, Object>();
		
		conhecimentoMapped.put("sgFilialDocumento", con.getFilialOrigem().getSgFilial());
		conhecimentoMapped.put("nrConhecimento", con.getNrConhecimento());
		conhecimentoMapped.put("idDoctoServico", con.getIdDoctoServico());
		
		Integer volumesProcessados = countVolumesCarregamentoDescarga(idControleCarga, con.getIdDoctoServico(), volume.getDispositivoUnitizacao());
		Integer totalVolumesProcessados = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga, con.getIdDoctoServico(), "D");

		conhecimentoMapped.put("qtVolumesDescarregados", volumesProcessados);
		conhecimentoMapped.put("qtVolumes", con.getQtVolumes());
		conhecimentoMapped.put("completo", totalVolumesProcessados.compareTo(con.getQtVolumes()) == 0);
		conhecimentoMapped.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volume.getIdVolumeNotaFiscal()));

		return conhecimentoMapped;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findDispMapped(Long idControleCarga, Map<String, Object> awbMap, final Long idDisp) {
		
		Map<String, Object> dispMap = this.findDispOnMappedList((List<Map<String, Object>>)awbMap.get("dispositivos"), idDisp);
		
		if (dispMap == null) {
			List<Map<String, Object>> listDisp = (List<Map<String, Object>>)awbMap.get("dispositivos");
			dispMap = getDispMapped(idControleCarga, idDisp);
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
			
			if (idDisp.equals(((Map<String, Object>)duMap).get("idDispositivoUnitizacao"))) {
				return duMap;
			}
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getDispMapped(Long idControleCarga, Long idDisp) {
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findById(idDisp);
		
		Map<String, Object> dispositivoMapped = new HashMap<String, Object>();
		dispositivoMapped.put("idDispositivoUnitizacao", dispositivo.getIdDispositivoUnitizacao());
		dispositivoMapped.put("tpDispositivoUnit", dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
		dispositivoMapped.put("qtVolumes", volumeNotaFiscalService.findRowCountVolumesCarregamentoDescargaDispositivo(idControleCarga, getTpOperacao(), dispositivo));
		dispositivoMapped.put("qtDispositivoUnit", (dispositivo.getDispositivosUnitizacao() != null) ? dispositivo.getDispositivosUnitizacao().size() : 0);
		dispositivoMapped.put("conhecimentos", new ArrayList<Map<String, Object>>());
		dispositivoMapped.put("dispositivos", new ArrayList<Map<String, Object>>());

		DispositivoUnitizacao dispositivoPai = dispositivo.getDispositivoUnitizacaoPai();
		Map<String, Object> dispPaiMapped = null;
		if (dispositivoPai != null) {
			dispPaiMapped = getDispMapped(idControleCarga, dispositivoPai.getIdDispositivoUnitizacao());
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
		Map<String, Object> awbMapped = new HashMap<String, Object>();

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

	public CarregamentoMobileService getCarregamentoMobileService() {
		return carregamentoMobileService;
	}

	public void setCarregamentoMobileService(
			CarregamentoMobileService carregamentoMobileService) {
		this.carregamentoMobileService = carregamentoMobileService;
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

	public void setManifestoEntregaDocumentoService(
			ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}

	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}
}