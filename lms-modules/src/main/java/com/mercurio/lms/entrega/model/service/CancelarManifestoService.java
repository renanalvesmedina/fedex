package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.EventoManifestoService;
import com.mercurio.lms.carregamento.model.service.ManifestoNacionalVolumeService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.util.ListUtilsPlus;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.dao.ManifestoViagemNacionalDAO;
import com.mercurio.lms.expedicao.model.service.ManifestoNacionalCtoService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.CalculoReciboIRRFService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.SolicitacaoRetiradaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.cancelarManifestoService"
 */
public class CancelarManifestoService {
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private IncluirEventosRastreabilidadeInternacionalService eventosRastreabilidadeInternacionalService;
	private ManifestoService manifestoService;
	private SolicitacaoRetiradaService solicitacaoRetiradaService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoNacionalCtoService manifestoNacionalCtoService;
	private ManifestoNacionalVolumeService manifestoNacionalVolumeService;
	private DomainValueService domainValueService;
	private ManifestoEntregaService manifestoEntregaService;
	private FaturaService faturaService;
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private EventoManifestoService eventoManifestoService;
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
	private EventoVolumeService eventoVolumeService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private ManifestoViagemNacionalDAO manifestoViagemNacionalDAO;
	private CalculoReciboIRRFService calculoReciboIRRFService;
	
	/**
	 * Modo como o manifesto será cancelado.
	 */
	private enum ModoCancelamento {
		COM_APROVEITAMENTO,
		SEM_APROVEITAMENTO
	}

	/**
	 * Cancelamento de manifestos de entrega
	 * 
	 * @param manifestos List de Long com idManifestoEntrega
	 */
	public void executeCancelarManifesto(List<Long> idsManifesto) {
		//Itera por cada manifesto selecionado na tela 
		for(Long idManifestoEntrega : idsManifesto) {
			executeCancelarManifesto(idManifestoEntrega, ModoCancelamento.SEM_APROVEITAMENTO);			
		}
	}

	/**
	 * Cancelamento de manifestos de entrega com aproveitamento
	 * 
	 * @param manifestos List de Long com idManifestoEntrega
	 */
	public void executeCancelarManifestoComAproveitamento(List<Long> idsManifesto) {
		//Itera por cada manifesto da lista
		for(Long idManifestoEntrega : idsManifesto) {
			executeCancelarManifesto(idManifestoEntrega, ModoCancelamento.COM_APROVEITAMENTO);			
		}
	}

	private void executeCancelarManifesto(Long idManifestoEntrega, ModoCancelamento modoCancelamento) {
		DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
		Filial filialSessao = SessionUtils.getFilialSessao();

		//Se o manifesto estiver emitido e com saida informada, lanca excecao
		if (manifestoService.validateEmissaoSaidaManifesto(idManifestoEntrega)){
			throw new BusinessException("LMS-09054");
		}
		String dsObservacao = null;
		Short cdEventoComAproveitamento = null;
		Short cdEventoBtnCancelar = null;
		String tpManifestoEntrega = null;

		//Consulta os documentos de servico associados ao manifesto
		List<Map<String, Object>> documentos = manifestoEntregaDocumentoService.findDocumentosByManifestoEntrega(idManifestoEntrega);

		//Itera por cada documento de servico do manifesto
		for (Map<String, Object> map : documentos) {
			TypedFlatMap documento = new TypedFlatMap(map);

			Long idManifestoEntregaDocumento = documento.getLong("idManifestoEntregaDocumento");
			Long idDoctoServico = documento.getLong("idDoctoServico");
			tpManifestoEntrega = documento.getDomainValue("tpManifestoEntrega").getValue();
			String tpDocumentoServico = documento.getDomainValue("tpDocumentoServico").getValue();

			if("CR".equals(tpManifestoEntrega)){
				dsObservacao = "Cliente Retira";
			}
			
			//Numero do documento de servico
			StringBuilder nrDocumentoManifesto = new StringBuilder()
					.append(documento.getString("sgFilial"))
					.append(" ")
					.append(documento.getInteger("nrManifestoEntrega")
			);
			Short cdLocalizacaoMercadoria = documento.getShort("cdLocalizacaoMercadoria");
			if(ModoCancelamento.COM_APROVEITAMENTO.equals(modoCancelamento) && cdLocalizacaoMercadoria.intValue() == ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA.intValue() && "CR".equals(tpManifestoEntrega)){
				StringBuilder nrDoctoServico = new StringBuilder();
				nrDoctoServico.append(""+documento.getString("sgFilialOrigem"))
				.append(" ");
				nrDoctoServico.append(" " +documento.getLong("nrDoctoServico"));

				eventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("101"), idDoctoServico, SessionUtils.getFilialSessao().getIdFilial(), nrDoctoServico.toString(), new DateTime(), null, null, tpDocumentoServico);	
			}
			
			if("CR".equals(tpManifestoEntrega)){
				cdEventoComAproveitamento = 311;
				cdEventoBtnCancelar = ConstantesSim.EVENTO_MANIFESTO_CANCELAR;
				dsObservacao = "Cliente Retira";
			} else if("PR".equals(tpManifestoEntrega) || "EP".equals(tpManifestoEntrega)){
				cdEventoComAproveitamento = 309;
				cdEventoBtnCancelar = 310;
			} else {
				cdEventoComAproveitamento = ConstantesSim.EVENTO_MANIFESTO_CANCELADO;
				cdEventoBtnCancelar = ConstantesSim.EVENTO_MANIFESTO_CANCELAR;
			}
			Short cdEvento = null;
			if(ModoCancelamento.COM_APROVEITAMENTO.equals(modoCancelamento)){
				cdEvento = cdEventoComAproveitamento;
			} else {
				cdEvento = cdEventoBtnCancelar;
			}
				//Gera evento de cancelamento do manifesto de entrega
				eventosRastreabilidadeInternacionalService.generateEventoDocumento(
					cdEvento,
						idDoctoServico,
						filialSessao.getIdFilial(),
					nrDocumentoManifesto.toString(), 
						dhAtual, 
						null, 
					dsObservacao, 
					ConstantesEntrega.MANIFESTO_ENTREGA);

			
			if (!modoCancelamento.equals(ModoCancelamento.COM_APROVEITAMENTO)) {
				//Cancela o manifesto X entrega X documento
				manifestoEntregaDocumentoService.updateSituacaoManifestoEntregaDocumento(idManifestoEntregaDocumento, "CANC");
			}

		}

		boolean comAproveitamento = modoCancelamento.equals(ModoCancelamento.COM_APROVEITAMENTO);
		Short cdEvento = null;
		if (comAproveitamento) {
			cdEvento = cdEventoComAproveitamento;
		} else if (modoCancelamento.equals(ModoCancelamento.SEM_APROVEITAMENTO)){
			cdEvento = cdEventoBtnCancelar;
		}
		eventoVolumeService.generateEventoCancelamentoManifestoByManifesto(idManifestoEntrega, tpManifestoEntrega, cdEvento, 
																		   ConstantesSim.TP_SCAN_LMS, comAproveitamento);
		eventoDispositivoUnitizacaoService.generateEventoCancelamentoManifestoByManifesto(idManifestoEntrega, cdEvento, dsObservacao, 
																						  ConstantesSim.TP_SCAN_LMS, comAproveitamento);	
		
		/** Chamada para retornar os dispositivos de unitização para a filial */
		dispositivoUnitizacaoService.executeVoltarDispositivosUnitizacaoCarregados(idManifestoEntrega);

		if (modoCancelamento.equals(ModoCancelamento.COM_APROVEITAMENTO)) {
			// Remove ManifestoEntregaVolume para o manifesto indicado.
			manifestoEntregaVolumeService.removeByIdManifestoEntrega(idManifestoEntrega);		
			// Remove ManifestoEntregaDocumento para o manifesto indicado.
			manifestoEntregaDocumentoService.removeByIdManifestoEntrega(idManifestoEntrega);
			// Todas as faturas associadas são atualizadas para não serem vinculadas ao manifesto.
			faturaService.updateManifestoFatura(idManifestoEntrega);
			// Remove enfim o MANIFESTO DE ENTREGA
			manifestoEntregaService.removeById(idManifestoEntrega);
			// Remove o evento de manifesto emitido
			eventoManifestoService.removeByManifesto(idManifestoEntrega, ConstantesEntrega.EVENTO_MANIFESTO_EMITIDO);
			// Atualiza o status do MANIFESTO
			manifestoService.updateSituacaoManifesto(idManifestoEntrega, ConstantesEntrega.STATUS_CARREGAMENTO_CONCLUIDO, null);
		} else if (modoCancelamento.equals(ModoCancelamento.SEM_APROVEITAMENTO)) {
			// Gera evento de manifesto cancelado
			Manifesto manifesto = manifestoService.findById(idManifestoEntrega);
			eventoManifestoService.generateEventoManifesto(manifesto, SessionUtils.getFilialSessao(), ConstantesEntrega.EVENTO_MANIFESTO_CANCELADO);

			//Cancela o manifesto
			manifestoService.updateSituacaoManifestoParaCancelado(idManifestoEntrega);

			// LMS-2077: Reabre a Solicitação de Retirada em Manifesto de entrega definido como "Cliente retira". 
			if (manifesto.getSolicitacaoRetirada() != null 
					&& (new DomainValue("CR")).equals(manifesto.getTpManifestoEntrega())) {
				manifesto.getSolicitacaoRetirada().setTpSituacao(new DomainValue("A"));
				solicitacaoRetiradaService.store(manifesto.getSolicitacaoRetirada());
		}
	}
	}

	/**
	 * Cancelamento de manifestos de Viagem
	 * @autor Andre Valadas
	 * 
	 * @param idManifestoViagemNacional
	 * @param blReaproveitarDados
	 * @param blForceCancel
	 * @return <b>status do Reaproveitamento dos Dados</b>
	 */
	public TypedFlatMap executeCancelarManifestoViagem(Long idManifestoViagemNacional, Boolean blReaproveitarDados, Boolean blForceCancel) {
		ModoCancelamento modoCancelamento = (blReaproveitarDados) ? ModoCancelamento.COM_APROVEITAMENTO : ModoCancelamento.SEM_APROVEITAMENTO;

		ManifestoViagemNacional manifestoViagemNacionalOrigem = manifestoViagemNacionalService.findById(idManifestoViagemNacional);
		Manifesto manifestoOrigem = manifestoViagemNacionalOrigem.getManifesto();

		/** Manifesto deve estar EMITIDO para o Cancelamento */
		if (!ConstantesEntrega.STATUS_MANIFESTO_EMITIDO.equals(manifestoOrigem.getTpStatusManifesto().getValue())) {
			throw new BusinessException("LMS-04169");
		}

		/** Filial Origem dever ser a MESMA do Usuario Logado */
		Filial filialSessao = SessionUtils.getFilialSessao();
		Long idFilialSessao = filialSessao.getIdFilial();
		if (!idFilialSessao.equals(manifestoViagemNacionalOrigem.getFilial().getIdFilial())) {
			throw new BusinessException("LMS-04170");
		}

		/** 
		 * Cancelar, com reaproveitamento ou não, todos os manifestos que tenham a mesma origem e destino do
		 * manifesto que está sendo cancelado e ainda não foram emitidos
		 */
		List<Manifesto> listManifestos = manifestoService.findManifestosByTrecho(
			ConstantesEntrega.TP_MANIFESTO_VIAGEM, 
			ConstantesEntrega.STATUS_MANIFESTO_EMITIDO,
			manifestoOrigem.getControleCarga().getIdControleCarga(),
			manifestoOrigem.getFilialByIdFilialOrigem().getIdFilial(),
			manifestoOrigem.getFilialByIdFilialDestino().getIdFilial());

		/** Verifica se deve exibir confirmação do concelamento */
		if(Boolean.TRUE.equals(blForceCancel)) {
			// Valida se existe recibos a cancelar entre os manifestos
			for (Manifesto manifesto : listManifestos) {
				if(!reciboFreteCarreteiroService.findReciboFreteCarreteiroByIdManifesto(manifesto.getIdManifesto()).isEmpty()) {
					throw new BusinessException("LMS-04211");
				}
			}
		}

		for (Manifesto manifesto : listManifestos) {
			ManifestoViagemNacional manifestoViagemNacional = manifesto.getManifestoViagemNacional();

			// Chamada para retornar os dispositivos de unitização para a filial
			dispositivoUnitizacaoService.executeVoltarDispositivosUnitizacaoCarregados(manifestoViagemNacional.getIdManifestoViagemNacional());

			if (modoCancelamento.equals(ModoCancelamento.COM_APROVEITAMENTO)) {
				// Atualiza Campos na Tabela Manifesto
				manifesto.setDhEmissaoManifesto(null);
				manifesto.setTpStatusManifesto(new DomainValue(ConstantesEntrega.STATUS_CARREGAMENTO_CONCLUIDO));
				manifesto.setVlTotalManifestoEmissao(null);
				manifesto.setPsTotalManifestoEmissao(null);
				manifesto.setQtTotalVolumesEmissao(null);
				manifesto.setVlTotalFreteCifEmissao(null);
				manifesto.setVlTotalFreteFobEmissao(null);
				manifesto.setVlTotalFreteEmissao(null);
				manifestoService.storeManifesto(manifesto);

				// Gera Evento de Cancelamento para o Manifesto de Viagem 
				if(!"RV".equals(manifesto.getTpManifestoViagem().getValue())) {
					eventoVolumeService.generateEventoByManifesto(
							manifestoViagemNacional.getIdManifestoViagemNacional(), 
							ConstantesSim.EVENTO_MANIFESTO_CANCELADO, 
							ConstantesSim.TP_SCAN_LMS);
					
					eventoDispositivoUnitizacaoService.generateEventoByManifesto(
							manifestoViagemNacional.getIdManifestoViagemNacional(), 
							ConstantesSim.EVENTO_MANIFESTO_CANCELADO,
							ConstantesSim.TP_SCAN_LMS, null);		
					
					// Codigo do evento de cancelamento
					eventosRastreabilidadeInternacionalService.insereEventos(
						ConstantesEntrega.MANIFESTO_VIAGEM,
						manifestoViagemNacional.getNrManifestoOrigem().toString(),
						ConstantesSim.EVENTO_MANIFESTO_CANCELADO,
						manifesto.getFilialByIdFilialOrigem().getIdFilial(),
						JTDateTimeUtils.getDataHoraAtual().toDateTime(manifesto.getFilialByIdFilialOrigem().getDateTimeZone()),
						null,
						null
					);
				}
				
				// Remove o recibo frete carreteiro do adiantamento trecho
				reciboFreteCarreteiroService.removeReciboFreteCarreteiroInAdiantamentoTrecho(manifestoViagemNacional.getIdManifestoViagemNacional());
				// Exclui os Recibos
				List<ReciboFreteCarreteiro> recibos = reciboFreteCarreteiroService.findReciboFreteCarreteiroByIdManifesto(manifestoViagemNacional.getIdManifestoViagemNacional());
				for (ReciboFreteCarreteiro recibo : recibos){
				    calculoReciboIRRFService.executeEstornoReciboIRRF(recibo);
				}
				reciboFreteCarreteiroService.removeRecibos(manifestoViagemNacional.getIdManifestoViagemNacional());
				
				/** Exclui Registros de ManifestoNacionalCto */
				removeManifestoNacionalCtos(manifestoViagemNacional);
				
				// Exclui Registro de ManifestoViagemNacionalCto
				manifestoViagemNacionalService.removeById(manifestoViagemNacional.getIdManifestoViagemNacional());
				// Remove o evento de manifesto emitido
				eventoManifestoService.removeByManifesto(manifestoViagemNacional.getIdManifestoViagemNacional(), ConstantesEntrega.EVENTO_MANIFESTO_EMITIDO);

			} else {
				
				manifesto.setControleCarga(null);
				manifesto.setIdControleCarga(null);
				manifestoService.storeManifesto(manifesto);
				
				// Gera Evento de Cancelamento para o Manifesto de Viagem 
				if(!"RV".equals(manifesto.getTpManifestoViagem().getValue())) {
					eventoVolumeService.generateEventoByManifesto(
							manifestoViagemNacional.getIdManifestoViagemNacional(), 
							ConstantesSim.EVENTO_MANIFESTO_CANCELAR,
							ConstantesSim.TP_SCAN_LMS);
					
					eventoDispositivoUnitizacaoService.generateEventoByManifesto(
							manifestoViagemNacional.getIdManifestoViagemNacional(), 
							ConstantesSim.EVENTO_MANIFESTO_CANCELAR,
							ConstantesSim.TP_SCAN_LMS, null);		
					
					eventosRastreabilidadeInternacionalService.insereEventos(
						ConstantesEntrega.MANIFESTO_VIAGEM,
						manifestoViagemNacional.getNrManifestoOrigem().toString(),
						ConstantesSim.EVENTO_MANIFESTO_CANCELAR,
						manifesto.getFilialByIdFilialOrigem().getIdFilial(),
						JTDateTimeUtils.getDataHoraAtual().toDateTime(manifesto.getFilialByIdFilialOrigem().getDateTimeZone()),
						null,
						null
					);
				}

				/** Exclui Registros de ManifestoNacionalCto */
				removeManifestoNacionalCtos(manifestoViagemNacional);
				
				// Cancelamento dos Recibos
				reciboFreteCarreteiroService.generateCancelamentoReciboFreteCarreteiro(manifestoViagemNacional.getIdManifestoViagemNacional());
				// Inclui evento de manifesto cancelado
				eventoManifestoService.generateEventoManifesto(manifesto, filialSessao, ConstantesEntrega.STATUS_MANIFESTO_CANCELADO);
				// Altera a situação do Manifesto para cancelado
				manifestoService.updateSituacaoManifesto(manifestoViagemNacional.getIdManifestoViagemNacional(), ConstantesEntrega.STATUS_MANIFESTO_CANCELADO);
			}
		}
		TypedFlatMap returnMap = new TypedFlatMap();
		if (modoCancelamento.equals(ModoCancelamento.SEM_APROVEITAMENTO)) {
			returnMap.put("tpStatusManifesto", domainValueService.findDomainValueByValue("DM_STATUS_MANIFESTO", ConstantesEntrega.STATUS_MANIFESTO_CANCELADO).getDescription());
		}
		/** Atualização da tela depende do tipo de Operação */
		returnMap.put("blReaproveitarDados", String.valueOf(blReaproveitarDados));
		return returnMap;
	}

	/** Exclui Registros de ManifestoNacionalCto */
	private void removeManifestoNacionalCtos(ManifestoViagemNacional manifestoViagemNacional) {	
		this.removeManifestoNacionalVolume(manifestoViagemNacional);
		
		List<ManifestoNacionalCto> manifestos = manifestoViagemNacional.getManifestoNacionalCtos();
		if (!manifestos.isEmpty()) {
			List<Long> IdsManifesto = new ArrayList<Long>();

			// Inicio alteracao do Jira LMS-294.
			for (ManifestoNacionalCto cto : manifestos) {
				IdsManifesto.add(cto.getIdManifestoNacionalCto());
			}
			List<List> listIdsManifesto = (List)ListUtilsPlus.chuncks(IdsManifesto, 999);
			for (List listIds : listIdsManifesto) {
				manifestoNacionalCtoService.removeByIds(listIds);
		}		
            // Fim alteracao do Jira LMS-294.
	}
	}
	
	/** Exclui Registros de ManifestoNacionalVolume */
	public void removeManifestoNacionalVolume(ManifestoViagemNacional manifestoViagemNacional) {	
		List<ManifestoNacionalVolume> manifestos = manifestoViagemNacional.getManifestoNacionalVolumes();
		if (!manifestos.isEmpty()) {
			List<Long> IdsManifesto = new ArrayList<Long>();

			// Inicio alteracao do Jira LMS-294.
			for (ManifestoNacionalVolume vol : manifestos) {
				IdsManifesto.add(vol.getIdManifestoNacionalVolume());
			}
			List<List> listIdsManifesto = (List)ListUtilsPlus.chuncks(IdsManifesto, 999);
			for (List listIds : listIdsManifesto) {
				manifestoNacionalVolumeService.removeByIds(listIds);
		}		
            // Fim alteracao do Jira LMS-294.
	}
	
		/** Exclui Registros de IntManifestoViagemDestino */
		manifestoViagemNacionalDAO.removeIntManifestoViagemDestino(manifestoViagemNacional.getIdManifestoViagemNacional());
	}
	
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	
	
	public void setManifestoViagemNacionalDAO(
			ManifestoViagemNacionalDAO manifestoViagemNacionalDAO) {
		this.manifestoViagemNacionalDAO = manifestoViagemNacionalDAO;
	}

	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}
	public void setEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService eventosRastreabilidadeInternacionalService) {
		this.eventosRastreabilidadeInternacionalService = eventosRastreabilidadeInternacionalService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}
	public void setManifestoNacionalCtoService(ManifestoNacionalCtoService manifestoNacionalCtoService) {
		this.manifestoNacionalCtoService = manifestoNacionalCtoService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}
	public void setEventoManifestoService(EventoManifestoService eventoManifestoService) {
		this.eventoManifestoService = eventoManifestoService;
	}
	public void setReciboFreteCarreteiroService(ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}
	public void setManifestoEntregaVolumeService(ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
		this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
	}

	public void setManifestoNacionalVolumeService(ManifestoNacionalVolumeService manifestoNacionalVolumeService) {
		this.manifestoNacionalVolumeService = manifestoNacionalVolumeService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}	
	
	public void setSolicitacaoRetiradaService(SolicitacaoRetiradaService solicitacaoRetiradaService) {
		this.solicitacaoRetiradaService = solicitacaoRetiradaService;
	}

    public void setCalculoReciboIRRFService(CalculoReciboIRRFService calculoReciboIRRFService) {
        this.calculoReciboIRRFService = calculoReciboIRRFService;
    }
}