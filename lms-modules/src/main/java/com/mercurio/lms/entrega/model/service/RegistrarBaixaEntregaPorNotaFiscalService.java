package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.municipios.model.service.FilialService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;
import br.com.tntbrasil.integracao.domains.fedex.OcorrenciaFedexDTO;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.entrega.model.EntregaNotaFiscal;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.dao.RegistrarBaixaEntregaPorNotaFiscalDAO;
import com.mercurio.lms.expedicao.DoctoServicoLookupFacade;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoNacionalCtoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.registrarBaixaEntregaPorNotaFiscalService"
 */
public class RegistrarBaixaEntregaPorNotaFiscalService {
	
	private static final Short CD_OCORRENCIA_ENTREGA_REALIZADA = Short.valueOf("1");
	private static final Short CD_OCORRENCIA_ENTREGA_PARCIAL = Short.valueOf("102");
	
	private static final Short CD_EVENTO_ENTREGA_REALIZADA = Short.valueOf("1");
	private static final Short CD_EVENTO_FINALIZACAO_ENTREGA_PARCIAL = Short.valueOf("154");
	private static final String TP_MANIFESTO_ENTREGA = "E";
	private static final String TP_FINALIZACAO_CTE_ORIGINAL = "FCO";
	private static final String TP_MANIFESTO_VIAGEM = "V";
	private static final String TP_FORMA_BAIXA = "N";
	private static final int VINTE = 20;
	private static final int SEIS = 6;

	private static final String TP_SITUACAO_ENTREGUE = "EN";
	
	private RegistrarBaixaEntregaPorNotaFiscalDAO registrarBaixaEntregaPorNotaFiscalDAO;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private EntregaNotaFiscalService entregaNotaFiscalService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private ManifestoNacionalCtoService manifestoNacionalCtoService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private ManifestoService manifestoService;
	private DoctoServicoService doctoServicoService;
	private IncluirEventosRastreabilidadeInternacionalService eventosRastreabilidadeInternacionalService;
	private DoctoServicoLookupFacade doctoServicoLookupFacade;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private IntegracaoJmsService integracaoJmsService;
	private RegistrarBaixaEntregasService registrarBaixaEntregasService;
	private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
	private EventoVolumeService eventoVolumeService;
	private ConhecimentoService conhecimentoService;
	private IntegracaoJwtService integracaoJwtService;
	private FilialService filialService;
	
	public ResultSetPage<Map<String, Object>>  findPaginated(TypedFlatMap criteria){
		return registrarBaixaEntregaPorNotaFiscalDAO.findPaginated(criteria);
	}
	
	public Integer getRowCountNotasFiscais(TypedFlatMap criteria){
		return registrarBaixaEntregaPorNotaFiscalDAO.getRowCountNotasFiscais(criteria);
	}
	
	public ResultSetPage<Map<String, Object>>  findNotasFiscais(TypedFlatMap criteria){
		return registrarBaixaEntregaPorNotaFiscalDAO.findNotasFiscais(criteria);
	}

	public void setRegistrarBaixaEntregaPorNotaFiscalDAO(RegistrarBaixaEntregaPorNotaFiscalDAO registrarBaixaEntregaPorNotaFiscalDAO) {
		this.registrarBaixaEntregaPorNotaFiscalDAO = registrarBaixaEntregaPorNotaFiscalDAO;
	}
	
	public void validateQtVolumesEntregues(Long idNotaFiscalConhecimento, Integer qtVolumesEntregues) {
		NotaFiscalConhecimento notaFiscalConhecimento = notaFiscalConhecimentoService.findById(idNotaFiscalConhecimento);
		if(qtVolumesEntregues <= 0 || qtVolumesEntregues >= notaFiscalConhecimento.getQtVolumes().intValue()){
			throw new BusinessException("LMS-09164");
		}
	}
	
	public void executeRegistrarBaixaEntrega(List<Map<String, Object>> listMap, Usuario usuario, Filial filial) {
		Long idDoctoServico = MapUtils.getLong(listMap.get(0), "idDoctoServico");
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		
		Long idManifesto = MapUtils.getLong(listMap.get(0), "idManifesto");
		Manifesto manifesto = manifestoService.findById(idManifesto);
		
		Short cdOcorrenciaEntrega = MapUtils.getShort(listMap.get(0), "cdOcorrenciaEntrega");
		OcorrenciaEntrega ocorrenciaEntrega = ocorrenciaEntregaService.findOcorrenciaEntrega(cdOcorrenciaEntrega);
		
		DateTime dhOcorrencia = null;
		
		ManifestoEntregaDocumento manifestoEntregaDocumento = manifestoEntregaDocumentoService.findManifestoEntregaDocumento(manifesto.getIdManifesto(), doctoServico.getIdDoctoServico());
		ManifestoNacionalCto manifestoNacionalCto = manifestoNacionalCtoService.findManifestoNacionalCto(doctoServico.getIdDoctoServico(), manifesto.getIdManifesto());

		List<NotaFiscalConhecimento> notasConhecimento = notaFiscalConhecimentoService.findByConhecimento(idDoctoServico);
		
		List<Map<String,Object>> notasFiscais = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> entregaNotaFiscalDTO : listMap) {
			Long idEntregaNotaFiscal = MapUtils.getLong(entregaNotaFiscalDTO, "idEntregaNotaFiscal");
			Long idNotaFiscal = MapUtils.getLong(entregaNotaFiscalDTO, "idNotaFiscal");
			String tpManifesto = MapUtils.getString(entregaNotaFiscalDTO, "tpManifesto");
			dhOcorrencia = entregaNotaFiscalDTO.get("dhOcorrencia") != null ? (DateTime) entregaNotaFiscalDTO.get("dhOcorrencia") : null;
			Integer qtVolumesEntregues = MapUtils.getInteger(entregaNotaFiscalDTO, "qtVolumesEntregues");
			
			Long idOcorrenciaEntregaAnterior = MapUtils.getLong(entregaNotaFiscalDTO, "idOcorrenciaEntregaAnterior");
			OcorrenciaEntrega ocorrenciaEntregaAnterior = idOcorrenciaEntregaAnterior != null ? ocorrenciaEntregaService.findById(idOcorrenciaEntregaAnterior) : null;
			
			List<NotaFiscalOperada> list = notaFiscalOperadaService.findByIdNotaFiscalConhecimentoFinalizada(idNotaFiscal);
			if(idEntregaNotaFiscal == null && CollectionUtils.isEmpty(list)){
				notasFiscais.add(buildMapNotaFiscal(notasConhecimento, idNotaFiscal));
			}
			
			this.executeRegistrarBaixaEntrega(idEntregaNotaFiscal,
					doctoServico, manifesto, manifestoEntregaDocumento,
					manifestoNacionalCto, idNotaFiscal, tpManifesto,
					ocorrenciaEntrega, ocorrenciaEntregaAnterior, dhOcorrencia,
					qtVolumesEntregues, notasConhecimento, usuario, filial);
		}
		
		if (!notasFiscais.isEmpty()){
		    if(dhOcorrencia == null){
		        dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();
		    }
		    
			Map<String, Object> dadosEvento = buildMapDadosEvento(null, null, dhOcorrencia, ocorrenciaEntrega, filial);
			OcorrenciaFedexDTO ocorrenciaFedexDTO = monitoramentoDocEletronicoService.executeBuildOcorrenciaFedexDTO(doctoServico, dadosEvento, notasFiscais);
			
			if (ocorrenciaFedexDTO != null){
				JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_OCORRENCIA_ENRIQUECIDO, ocorrenciaFedexDTO);
				integracaoJmsService.storeMessage(jmsMessageSender);
			}
		}
		
	}

	private Map<String,Object> buildMapDadosEvento(Long idEvento, Short cdEvento, DateTime dhOcorrencia,OcorrenciaEntrega ocorrenciaEntrega, Filial filial){
		Map<String,Object> dadosEvento = new HashMap<String, Object>();

		if (idEvento == null){
			OcorrenciaEntrega ocorrenciaEntregaParcial = ocorrenciaEntregaService.findOcorrenciaEntrega(CD_OCORRENCIA_ENTREGA_PARCIAL);
			Evento evento = ocorrenciaEntregaParcial.getEvento();
			idEvento = evento.getIdEvento();
			cdEvento = evento.getCdEvento();
		}
		
		dadosEvento.put("idEvento",idEvento);
		dadosEvento.put("dhEvento",dhOcorrencia);
		dadosEvento.put("cdEvento",cdEvento);
		dadosEvento.put("idFilialEvento",filial.getIdFilial());
		dadosEvento.put("sgFilialEvento",filial.getSgFilial());
		
		if (ocorrenciaEntrega != null){
			dadosEvento.put("cdOcorrenciaEntrega",ocorrenciaEntrega.getCdOcorrenciaEntrega());
			dadosEvento.put("idOcorrenciaEntrega",ocorrenciaEntrega.getIdOcorrenciaEntrega());
		}
		
		return dadosEvento;
	}
	
	private Map<String,Object> buildMapNotaFiscal(List<NotaFiscalConhecimento> notasConhecimento, Long idNotaFiscal){
		
		NotaFiscalConhecimento notaFiscalConhecimento = findNotaFromList(notasConhecimento, idNotaFiscal);
		
		Map<String,Object> mapNota = new HashMap<String, Object>();
		mapNota.put("dsSerie", notaFiscalConhecimento.getDsSerie());
		mapNota.put("nrNotaFiscal", notaFiscalConhecimento.getNrNotaFiscal());
		mapNota.put("dtEmissao", notaFiscalConhecimento.getDtEmissao());
		if (notaFiscalConhecimento.getNrChave() != null){
			mapNota.put("cnpjEmissor", notaFiscalConhecimento.getNrChave().substring(SEIS, VINTE));
		}
		
		return mapNota;
	}

	private NotaFiscalConhecimento findNotaFromList(List<NotaFiscalConhecimento> notasConhecimento, final Long idNotaFiscalConhecimento){
		
		return (NotaFiscalConhecimento)CollectionUtils.find(notasConhecimento, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return ((NotaFiscalConhecimento)object).getIdNotaFiscalConhecimento().equals(idNotaFiscalConhecimento);
			}
		});
		
	}
		
	public void executeRegistrarBaixaEntrega(Long idEntregaNotaFiscal, DoctoServico doctoServico, Manifesto manifesto, 
			ManifestoEntregaDocumento manifestoEntregaDocumento, ManifestoNacionalCto manifestoNacionalCto, 
			Long idNotaFiscalConhecimento, String tpManifesto, OcorrenciaEntrega ocorrenciaEntrega, OcorrenciaEntrega ocorrenciaEntregaAnterior,
			DateTime dhOcorrencia, Integer qtVolumesEntregues, List<NotaFiscalConhecimento> notasConhecimento, Usuario usuario, Filial filial) {
		
		NotaFiscalConhecimento notaFiscalConhecimento = new NotaFiscalConhecimento();
		notaFiscalConhecimento.setIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
		
		List<ManifestoEntregaVolume> listManifestoEntregaVolume = manifestoEntregaVolumeService.findManifestoEntregaVolumeByIdNotaFiscalConhecimentoIdManifestoEntregaDoc(idNotaFiscalConhecimento, manifestoEntregaDocumento.getIdManifestoEntregaDocumento());
		
		if (idEntregaNotaFiscal == null) {
			
			fluxoRegistroNovo(doctoServico, manifesto,
					manifestoEntregaDocumento, manifestoNacionalCto,
					tpManifesto, ocorrenciaEntrega, dhOcorrencia,
					qtVolumesEntregues, notaFiscalConhecimento,
					listManifestoEntregaVolume, usuario, filial);
			
		} else {
			
			fluxoRegistroAlteracao(idEntregaNotaFiscal, doctoServico,
					idNotaFiscalConhecimento, tpManifesto, ocorrenciaEntrega, ocorrenciaEntregaAnterior,
					qtVolumesEntregues, notasConhecimento,
					notaFiscalConhecimento, listManifestoEntregaVolume, usuario, filial);
		}
		
	}

	private void fluxoRegistroNovo(DoctoServico doctoServico,
			Manifesto manifesto,
			ManifestoEntregaDocumento manifestoEntregaDocumento,
			ManifestoNacionalCto manifestoNacionalCto, String tpManifesto,
			OcorrenciaEntrega ocorrenciaEntrega,
			DateTime dhOcorrencia,
			Integer qtVolumesEntregues,
			NotaFiscalConhecimento notaFiscalConhecimento,
			List<ManifestoEntregaVolume> listManifestoEntregaVolume, Usuario usuario, Filial filial) {

		if(TP_MANIFESTO_ENTREGA.equals(tpManifesto)){
			for(ManifestoEntregaVolume mev : listManifestoEntregaVolume){
			    registrarBaixaEntregasService.storeOcorrenciaOnManifestoEntregaVolume(
			            mev.getIdManifestoEntregaVolume(), 
			            CD_OCORRENCIA_ENTREGA_PARCIAL.equals(ocorrenciaEntrega.getCdOcorrenciaEntrega()) ? CD_OCORRENCIA_ENTREGA_REALIZADA : ocorrenciaEntrega.getCdOcorrenciaEntrega(),
		                TP_FORMA_BAIXA,
		                dhOcorrencia, 
		                true,
		                true,
						filial,
						usuario);
			}
			
		} 
		
		List<NotaFiscalOperada> list = notaFiscalOperadaService.findByIdNotaFiscalConhecimentoFinalizada(notaFiscalConhecimento.getIdNotaFiscalConhecimento());
		if(CollectionUtils.isEmpty(list)){
			EntregaNotaFiscal entregaNotaFiscal = new EntregaNotaFiscal();
			entregaNotaFiscal.setManifestoEntregaDocumento(TP_MANIFESTO_ENTREGA.equals(tpManifesto) ? manifestoEntregaDocumento : null);
			entregaNotaFiscal.setManifestoNacionalCto(TP_MANIFESTO_ENTREGA.equals(tpManifesto) ? null : manifestoNacionalCto);
			entregaNotaFiscal.setManifesto(manifesto);
			entregaNotaFiscal.setNotaFiscalConhecimento(notaFiscalConhecimento);
			entregaNotaFiscal.setOcorrenciaEntrega(ocorrenciaEntrega);
			entregaNotaFiscal.setDhOcorrencia(dhOcorrencia);
			UsuarioLMS usuarioLMS = new UsuarioLMS();
			usuarioLMS.setIdUsuario(usuario.getIdUsuario());
			entregaNotaFiscal.setUsuario(usuarioLMS);
			entregaNotaFiscal.setQtVolumesEntregues(qtVolumesEntregues);
			
			if(ocorrenciaEntrega.getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_REALIZADA) || ocorrenciaEntrega.getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_PARCIAL)) {
				this.createNotaFiscalOperadaEntregue(doctoServico, notaFiscalConhecimento);
				this.executeFinalizacaoCteOriginal(doctoServico.getIdDoctoServico(), filial, usuario);
			}
			
			entregaNotaFiscalService.store(entregaNotaFiscal);
		}
		
	}

	private void fluxoRegistroAlteracao(Long idEntregaNotaFiscal,
			DoctoServico doctoServico, Long idNotaFiscalConhecimento,
			String tpManifesto, OcorrenciaEntrega ocorrenciaEntrega, OcorrenciaEntrega ocorrenciaEntregaAnterior,
			Integer qtVolumesEntregues,
			List<NotaFiscalConhecimento> notasConhecimento,
			NotaFiscalConhecimento notaFiscalConhecimento,
			List<ManifestoEntregaVolume> listManifestoEntregaVolume,
			Usuario usuario, Filial filial) {

		EntregaNotaFiscal entregaNotaFiscal = entregaNotaFiscalService.findById(idEntregaNotaFiscal);
		
		if(entregaNotaFiscal.getOcorrenciaEntrega().getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_REALIZADA)|| entregaNotaFiscal.getOcorrenciaEntrega().getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_PARCIAL)) {
			List<NotaFiscalOperada> listDeletar = notaFiscalOperadaService.findByIdNotaFiscalConhecimentoidDoctoServicoTpSituacao(idNotaFiscalConhecimento, doctoServico.getIdDoctoServico(), TP_SITUACAO_ENTREGUE);
			notaFiscalOperadaService.removeById(listDeletar.get(0).getIdNotaFiscalOperada());
		}
		
		entregaNotaFiscal.setOcorrenciaEntrega(ocorrenciaEntrega);
		entregaNotaFiscal.setQtVolumesEntregues(ocorrenciaEntrega.getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_PARCIAL) ? qtVolumesEntregues : null);
		entregaNotaFiscal.setObAlteracao("Alteração - " 
				+ JTFormatUtils.format(new DateTime(), "dd/MM/yyyy HH:mm") + " - "
				+ SessionUtils.getUsuarioLogado().getNmUsuario() + " - "
				+ ocorrenciaEntrega.getDsOcorrenciaEntrega().getValue() + "\n" 
				+ (entregaNotaFiscal.getObAlteracao() != null ? entregaNotaFiscal.getObAlteracao() : ""));
		
		if(TP_MANIFESTO_ENTREGA.equals(tpManifesto)){

			Short cdEvtCancelamento = eventosRastreabilidadeInternacionalService.findDescricaoEventoCancelamento(ocorrenciaEntrega.getEvento().getCdEvento());
			
		    for(ManifestoEntregaVolume mev : listManifestoEntregaVolume){
		    	
		    	eventosRastreabilidadeInternacionalService.generateEventoVolume(mev.getVolumeNotaFiscal(), cdEvtCancelamento, ocorrenciaEntregaAnterior, JTDateTimeUtils.getDataHoraAtual());
		    	
		    	eventoVolumeService.storeEventoVolume(
		                mev.getVolumeNotaFiscal(), 
		                ocorrenciaEntrega.getEvento().getCdEvento(), 
		                ConstantesSim.TP_SCAN_LMS, 
		                null, 
		                ocorrenciaEntrega,
						filial,
		                entregaNotaFiscal.getDhOcorrencia(),
						usuario);
		        
		        mev.setOcorrenciaEntrega(ocorrenciaEntrega);
		        manifestoEntregaVolumeService.store(mev); 
		    }
		}
		
		if(ocorrenciaEntrega.getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_REALIZADA) || ocorrenciaEntrega.getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_PARCIAL)) {
			this.createNotaFiscalOperadaEntregue(doctoServico, notaFiscalConhecimento);
			this.executeFinalizacaoCteOriginal(doctoServico.getIdDoctoServico(), filial, usuario);
		}
		
		List<Map<String,Object>> notasFiscais = new ArrayList<Map<String,Object>>();
		notasFiscais.add(buildMapNotaFiscal(notasConhecimento, idNotaFiscalConhecimento));
		
		Map<String, Object> dadosEvento = buildMapDadosEvento(null, null, entregaNotaFiscal.getDhOcorrencia(), ocorrenciaEntrega, filial);
		OcorrenciaFedexDTO ocorrenciaFedexDTO = monitoramentoDocEletronicoService.executeBuildOcorrenciaFedexDTO(doctoServico, dadosEvento, notasFiscais);
		
		if (ocorrenciaFedexDTO != null){
		    JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FEDEX_ENVIO_OCORRENCIA_ENRIQUECIDO, ocorrenciaFedexDTO);
		    integracaoJmsService.storeMessage(jmsMessageSender);
		}
		
		entregaNotaFiscalService.store(entregaNotaFiscal);
	}
	
	private void createNotaFiscalOperadaEntregue(DoctoServico doctoServico, NotaFiscalConhecimento notaFiscalConhecimento) {
		NotaFiscalOperada notaFiscalOperada = new NotaFiscalOperada();
		notaFiscalOperada.setDoctoServico(doctoServico);
		notaFiscalOperada.setNotaFiscalConhecimentoOriginal(notaFiscalConhecimento);
		notaFiscalOperada.setTpSituacao(new DomainValue("EN"));
		notaFiscalOperadaService.store(notaFiscalOperada);
	}

	public void executeFinalizacaoCteOriginal(Long idDoctoServico, Filial filial, Usuario usuario){

		if (validateTodasNotasFinalizadas(idDoctoServico)) {
			
			DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
			Long idUltimoManifesto = manifestoService.findIdUltimoManifestoEntregaOrViagemEntregaDireta(idDoctoServico);
			Manifesto manifestoUltimo = manifestoService.findById(idUltimoManifesto);
			List<OcorrenciaEntrega> oeList = ocorrenciaEntregaService.findOcorrenciaEntregaByIdDoctoServicoCdOcorrenciaEntregaNaoCancelado(idDoctoServico, CD_OCORRENCIA_ENTREGA_PARCIAL);
			
			boolean contemOcorrenciaEntreParcial = CollectionUtils.isNotEmpty(oeList);
			if (contemOcorrenciaEntreParcial) {
				
				OcorrenciaEntrega ocorrenciaAnterior = oeList.get(0);
				Evento evento = ocorrenciaAnterior.getEvento();
				Short cdEvtCancelamento = eventosRastreabilidadeInternacionalService.findDescricaoEventoCancelamento(evento.getCdEvento());
				Filial filialUsuario = filial;
				
				if (TP_MANIFESTO_ENTREGA.equals(manifestoUltimo.getTpManifesto().getValue())){
					EventoDocumentoServico eventoDocumentoServico = eventoDocumentoServicoService.findByIdoctoServicoIdEventoIdOcorrenciaEntrega(idDoctoServico, evento.getIdEvento(), ocorrenciaAnterior.getIdOcorrenciaEntrega());
					DateTime dhEvento = eventoDocumentoServico.getDhEvento();
					
					generateEventoCancelamento(idDoctoServico, doctoServico, cdEvtCancelamento, filialUsuario, usuario);
					updateManifestoEntregaDocumento(idDoctoServico, ocorrenciaAnterior, usuario);
					updateNrReentregaConhecimento(idDoctoServico, ocorrenciaAnterior);
					executeBaixaDocumento(idDoctoServico, manifestoUltimo, dhEvento, filial, usuario);
					generateEventoFinalizacaoEntregaParcial(idDoctoServico, doctoServico, filialUsuario, usuario);
					
				} else if(TP_MANIFESTO_VIAGEM.equals(manifestoUltimo.getTpManifesto().getValue())) {
					
					generateEventoCancelamento(idDoctoServico, doctoServico, cdEvtCancelamento, filialUsuario, usuario);
					generateEventoEntregaRealizada(idDoctoServico, doctoServico, filialUsuario, usuario);
					generateEventoFinalizacaoEntregaParcial(idDoctoServico, doctoServico, filialUsuario, usuario);
				}
			}
			
			calcularDiasUteisBloqueioAgendamentoService.executeCalcularDiasUteisBloqueioAgendamento(doctoServico, Boolean.TRUE);
		}
		
	}
	
	private void updateNrReentregaConhecimento(Long idDoctoServico, OcorrenciaEntrega ocorrenciaAnterior) {
	    if(!BooleanUtils.isTrue(ocorrenciaAnterior.getBlOcasionadoMercurio())){
	        Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
	        if(LongUtils.hasValue(conhecimento.getNrReentrega())){
	            conhecimento.setNrReentrega(LongUtils.decrementValue(conhecimento.getNrReentrega()));
	        }
	    }
	}

	private void updateManifestoEntregaDocumento(Long idDoctoServico, OcorrenciaEntrega ocorrenciaAnterior, Usuario usuario) {
		Long idManifestoEntregaDocumento = manifestoEntregaDocumentoService.findIdUltimoManifestoEntregaDocByCdOcorrencia(idDoctoServico, CD_OCORRENCIA_ENTREGA_PARCIAL);
		ManifestoEntregaDocumento med = manifestoEntregaDocumentoService.findById(idManifestoEntregaDocumento);
		med.setDhOcorrencia(null);
		med.setOcorrenciaEntrega(null);
		med.setObAlteracao("Alteração - " 
				+ JTFormatUtils.format(new DateTime(), "dd/MM/yyyy HH:mm") + " - "
				+ usuario.getNmUsuario() + " - "
				+ ocorrenciaAnterior.getDsOcorrenciaEntrega().getValue() + "\n" 
				+ (med.getObAlteracao() != null ? med.getObAlteracao() : ""));
		manifestoEntregaDocumentoService.store(med);
	}

	private void executeBaixaDocumento(Long idDoctoServico, Manifesto manifestoUltimo, DateTime dhEvento, Filial filialSessao, Usuario usuarioLogado) {
	    String nmRecebedor = null;
	    ManifestoEntregaDocumento manifestoEntregaDoc = manifestoEntregaDocumentoService.findByIdDoctoServicoManifesto(idDoctoServico, manifestoUltimo.getIdManifesto());

	    if(manifestoEntregaDoc != null){
	        nmRecebedor = manifestoEntregaDoc.getNmRecebedor();
	    }
	    
	    manifestoEntregaDocumentoService.executeBaixaDocumento(manifestoUltimo.getIdManifesto()
				, idDoctoServico
				, CD_OCORRENCIA_ENTREGA_REALIZADA
				, "A"
				, TP_FINALIZACAO_CTE_ORIGINAL
				, dhEvento
				, nmRecebedor
				, null, false, false, Boolean.TRUE, null, null, null, filialSessao, usuarioLogado);
	}

	private void generateEventoFinalizacaoEntregaParcial(Long idDoctoServico, DoctoServico doctoServico, Filial filialUsuario, Usuario usuarioLogado) {
		eventosRastreabilidadeInternacionalService.generateEventoDocumento(CD_EVENTO_FINALIZACAO_ENTREGA_PARCIAL, idDoctoServico, filialUsuario.getIdFilial(), 
				doctoServicoLookupFacade.formatarDocumentoByTipo(doctoServico.getTpDocumentoServico().getValue(), doctoServico.getFilialByIdFilialOrigem().getSgFilial(), doctoServico.getNrDoctoServico().toString()),
				new DateTime(), null, null, doctoServico.getTpDocumentoServico().getValue(), (Long)null, null, null, usuarioLogado);
	}

	private void generateEventoEntregaRealizada(Long idDoctoServico, DoctoServico doctoServico, Filial filialUsuario, Usuario usuarioLogado) {
		eventosRastreabilidadeInternacionalService.generateEventoDocumento(CD_EVENTO_ENTREGA_REALIZADA, idDoctoServico, filialUsuario.getIdFilial(), 
				doctoServicoLookupFacade.formatarDocumentoByTipo(doctoServico.getTpDocumentoServico().getValue(), doctoServico.getFilialByIdFilialOrigem().getSgFilial(), doctoServico.getNrDoctoServico().toString()),
				new DateTime(), null, null, doctoServico.getTpDocumentoServico().getValue(), (Long)null, null, null, usuarioLogado);
	}

	private void generateEventoCancelamento(Long idDoctoServico, DoctoServico doctoServico, Short cdEvtCancelamento, Filial filialUsuario, Usuario usuarioLogado) {
		eventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvtCancelamento, idDoctoServico, filialUsuario.getIdFilial(), 
				doctoServicoLookupFacade.formatarDocumentoByTipo(doctoServico.getTpDocumentoServico().getValue(), doctoServico.getFilialByIdFilialOrigem().getSgFilial(), doctoServico.getNrDoctoServico().toString()),
				new DateTime(), null, null, doctoServico.getTpDocumentoServico().getValue(), (Long)null, null, null, usuarioLogado);
	}

	private boolean validateTodasNotasFinalizadas(Long idDoctoServico) {
		List<Long> idsNfcs = this.notaFiscalConhecimentoService.findIdsByIdConhecimento(idDoctoServico);
		for (Long idNfc : idsNfcs) {
			List<NotaFiscalOperada> list = notaFiscalOperadaService.findByIdNotaFiscalConhecimentoFinalizada(idNfc);
			if(CollectionUtils.isEmpty(list)){
				return false;
			}
		}
		return true;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setEntregaNotaFiscalService(
			EntregaNotaFiscalService entregaNotaFiscalService) {
		this.entregaNotaFiscalService = entregaNotaFiscalService;
	}

	public void setManifestoEntregaDocumentoService(
			ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public void setManifestoNacionalCtoService(
			ManifestoNacionalCtoService manifestoNacionalCtoService) {
		this.manifestoNacionalCtoService = manifestoNacionalCtoService;
	}

	public void setOcorrenciaEntregaService(
			OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public void setNotaFiscalOperadaService(
			NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService eventosRastreabilidadeInternacionalService) {
		this.eventosRastreabilidadeInternacionalService = eventosRastreabilidadeInternacionalService;
	}

	public void setDoctoServicoLookupFacade(
			DoctoServicoLookupFacade doctoServicoLookupFacade) {
		this.doctoServicoLookupFacade = doctoServicoLookupFacade;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setCalcularDiasUteisBloqueioAgendamentoService(
			CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService) {
		this.calcularDiasUteisBloqueioAgendamentoService = calcularDiasUteisBloqueioAgendamentoService;
	}

	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

    public void setRegistrarBaixaEntregasService(
            RegistrarBaixaEntregasService registrarBaixaEntregasService) {
        this.registrarBaixaEntregasService = registrarBaixaEntregasService;
    }

    public void setManifestoEntregaVolumeService(
            ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
        this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
    }

    public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
        this.eventoVolumeService = eventoVolumeService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}
