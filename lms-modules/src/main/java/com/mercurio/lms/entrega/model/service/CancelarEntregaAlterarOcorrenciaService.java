package com.mercurio.lms.entrega.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;
import org.springframework.aop.framework.AopContext;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.entrega.model.EntregaNotaFiscal;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.expedicao.DoctoServicoLookupFacade;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.cancelarEntregaAlterarOcorrenciaService"
 */
public class CancelarEntregaAlterarOcorrenciaService {
	
	private static final Short CD_OCORRENCIA_ENTREGA_REALIZADA = Short.valueOf("1");
	private static final Short CD_OCORRENCIA_ENTREGA_PARCIAL = Short.valueOf("102");
	
	private static final String TP_SITUACAO_ENTREGUE = "EN";
	private static final String TP_MANIFESTO_ENTREGA = "E";
	
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private ConfiguracoesFacade configuracoesFacade;
	private IncluirEventosRastreabilidadeInternacionalService eventosRastreabilidadeInternacionalService;
	private DoctoServicoService doctoServicoService;
	private DoctoServicoLookupFacade doctoServicoLookupFacade;
	private DadosComplementoService dadosComplementoService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	private ControleCargaService controleCargaService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private EntregaNotaFiscalService entregaNotaFiscalService;
	private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
	private EventoVolumeService eventoVolumeService;
	
	public void executeCancelarOcorrencia(TypedFlatMap parametros){
		Short cdOcorrenciaEntrega = parametros.getShort("ocorrenciaEntrega.cdOcorrenciaEntrega");
		Long idFilial = parametros.getLong("filial.idFilial");		
		Long idOcorrenciaEntrega = parametros.getLong("ocorrenciaEntrega.idOcorrenciaEntrega");	
		Long idOcorrenciaEntregaBaixa = parametros.getLong("idOcorrenciaEntrega");
		Long idDoctoServico = parametros.getLong("idDoctoServico");
		Long idManifestoEntregaDocumento = parametros.getLong("manifestoEntregaDocumento.idManifestoEntregaDocumento");		
		String obManifestoEntregaDocumento = parametros.getString("obManifestoEntregaDocumento");
		String obMotivoAlteracao = parametros.getString("obMotivoAlteracao");
		String nmRecebedor = parametros.getString("nmRecebedor2");
		BigDecimal qtTempoMaximo = (BigDecimal) configuracoesFacade.getValorParametroWithException(idFilial, "TEMPO_MAX_ALTERACAO");
		DateTime dhOcorrenciaManifesto = parametros.getDateTime("dhBaixa");
		DateTime dhCancelamentoOcorrenciaArquivo = parametros.getDateTime("dhCancelamentoOcorrenciaArquivo");
		DateTime dtAtual = JTDateTimeUtils.getDataHoraAtual();
		Usuario usuario = SessionUtils.getUsuarioLogado();	
		DomainValue vlBaixaSPP = parametros.getDomainValue("complementoBaixaSPP");
		DomainValue tpEntregaParcial = parametros.getDomainValue("tipoEntregaParcial");
                String rg = null;
                
		/*Quando não possuir ocorrencia mostra LMS-09049*/
		if (idOcorrenciaEntregaBaixa == null){
			throw new BusinessException("LMS-09049");
		}

		/*Obtem o manifesto de entrega*/
		ManifestoEntregaDocumento med = manifestoEntregaDocumentoService.findById(idManifestoEntregaDocumento);
		
		DateTime dhOcorrenciaValidar = null;
		if (dhCancelamentoOcorrenciaArquivo != null) {
			dhOcorrenciaValidar = dhCancelamentoOcorrenciaArquivo;
		} else {
			/*Obtem a data de ocorrencia através do manifesto*/
			dhOcorrenciaValidar = med.getDhOcorrencia();
			if(dhOcorrenciaValidar == null){
				dhOcorrenciaValidar = JTDateTimeUtils.getDataHoraAtual();
			}
		}
		dhOcorrenciaValidar = dhOcorrenciaValidar.plusHours(qtTempoMaximo.intValue());

		/*Verifica se a data atual é maior que a data de ocorrencia*/
		if (dtAtual.isAfter(dhOcorrenciaValidar)){			
	    	if (!controleCargaService.validateManutencaoEspecialCC(usuario)){
	    		throw new BusinessException("LMS-09053", new Object[]{qtTempoMaximo});	
	    	}
		}

		/*Ocorrencia selecionada pelo usúario*/ 
		OcorrenciaEntrega ocorrenciaAtual = ocorrenciaEntregaService.findById(idOcorrenciaEntrega);

		/*Verifica se o tipo de ocorrencia é Entrega*/
		if(ocorrenciaAtual.getTpOcorrencia() != null && "E".equals(ocorrenciaAtual.getTpOcorrencia().getValue())){
			DoctoServico doc = new DoctoServico();
			doc.setIdDoctoServico(idDoctoServico);
			dadosComplementoService.executeOcorrenciaSPPCliente(doc, vlBaixaSPP);			
		}		

		OcorrenciaEntrega ocorrenciaAnterior = null;
		if(idOcorrenciaEntregaBaixa != null){
			ocorrenciaAnterior = ocorrenciaEntregaService.findById(idOcorrenciaEntregaBaixa);
		}

		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);

		LocalizacaoMercadoria localizacaoMercadoriaAux = null;
		
		if (ocorrenciaAnterior != null){
			
			validateNotaDevolvidaOuRefatorada(idDoctoServico, ocorrenciaAnterior);
			
			/*Verifica se a ocorrencia ANTERIOR possui evento*/
			Evento evento = ocorrenciaAnterior.getEvento(); 

			if (evento != null){
				/*Obtem o codigo de cancelamento*/
				Short cdEvtCancelamento = eventosRastreabilidadeInternacionalService.findDescricaoEventoCancelamento(evento.getCdEvento());

				/*Obtem o tipo de documento*/
				String tpDocumento = parametros.getString("doctoServico.tpDocumentoServico");

				/*Gera evento*/
				DateTime dhCancelamentoOcorrencia = dtAtual;
				if (dhCancelamentoOcorrenciaArquivo != null) {
					dhCancelamentoOcorrencia = dhCancelamentoOcorrenciaArquivo;
				}
				Filial filialUsuario = SessionUtils.getFilialSessao();
				eventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvtCancelamento, idDoctoServico, filialUsuario.getIdFilial(), 
						doctoServicoLookupFacade.formatarDocumentoByTipo(tpDocumento, doctoServico.getFilialByIdFilialOrigem().getSgFilial(), 
								doctoServico.getNrDoctoServico().toString()),dhCancelamentoOcorrencia, null, null, tpDocumento, (Long)null, null, null, usuario);
				
				/* LMS-7596 - Gerar eventos para os volumes */
				generateEventoVolumes(idDoctoServico, cdEvtCancelamento, ocorrenciaAtual, dhCancelamentoOcorrencia);
				
				/** Altera localizacao do Servico de acordo com o tpStatusManifesto */
				String tpStatusManifesto = med.getManifestoEntrega().getManifesto().getTpStatusManifesto().getValue();
				/** Em transito de coleta/entrega */
				if("TC".equals(tpStatusManifesto)) {
					doctoServico.setLocalizacaoMercadoria(localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(Short.valueOf("41")));
					localizacaoMercadoriaAux = doctoServico.getLocalizacaoMercadoria();
				/** Aguardando descarga */
				} else if("AD".equals(tpStatusManifesto)) {
					doctoServico.setLocalizacaoMercadoria(localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(Short.valueOf("37")));
					localizacaoMercadoriaAux = doctoServico.getLocalizacaoMercadoria();
				/** Em descarga */
				} else if("ED".equals(tpStatusManifesto)) {
					doctoServico.setLocalizacaoMercadoria(localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(Short.valueOf("33")));
					localizacaoMercadoriaAux = doctoServico.getLocalizacaoMercadoria();
				/** Descarga concluida */
				} else if("DC".equals(tpStatusManifesto)) {
					doctoServico.setLocalizacaoMercadoria(localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(Short.valueOf("35")));
				}
				doctoServico.setFilialLocalizacao(filialUsuario);
				doctoServico.setObComplementoLocalizacao(filialUsuario.getSiglaNomeFilial());
				doctoServico.setNrDiasRealEntrega(null);
				doctoServicoService.store(doctoServico);
				
			}

			if(!BooleanUtils.isTrue(ocorrenciaAnterior.getBlOcasionadoMercurio())){
				Conhecimento conhecimento = (Conhecimento)doctoServico;
				Long nrReentrega = (conhecimento.getNrReentrega() == null) ? 0L : conhecimento.getNrReentrega();
				conhecimento.setNrReentrega(nrReentrega-1);
				doctoServicoService.store(conhecimento);
			}
		}

		
		/*Atualiza a situacao do manifesto*/
		alteraSituacaoManifestoEntregaDocumento(med);

		/*Atualiza a entrega do manifesto*/
		atualizaDadosManifestoEntregaDocumento(med, idOcorrenciaEntrega, obMotivoAlteracao);

		/* Salva o manifesto entrega */
		manifestoEntregaDocumentoService.store(med);

		ManifestoEntrega manifestoEntrega = med.getManifestoEntrega();

		/*Executa baixa do documento*/
		manifestoEntregaDocumentoService.executeBaixaDocumento(manifestoEntrega.getIdManifestoEntrega(),idDoctoServico, cdOcorrenciaEntrega, 
				"A", tpEntregaParcial.getValue(), dhOcorrenciaManifesto, nmRecebedor, obManifestoEntregaDocumento, Boolean.FALSE,Boolean.FALSE, rg, null, null);
		
		//Busca novamente o documento para garantir que não será desfeita nenhuma alteração realizada por outra "rotina" anterior.
		doctoServico = doctoServicoService.findById(doctoServico.getIdDoctoServico());
		
		if(ocorrenciaAtual.getTpOcorrencia() != null && "E".equals(ocorrenciaAtual.getTpOcorrencia().getValue())){
			localizacaoMercadoriaAux = doctoServico.getLocalizacaoMercadoria();
	}
		
		if(CD_OCORRENCIA_ENTREGA_PARCIAL.equals(ocorrenciaAnterior.getCdOcorrenciaEntrega()) && !CD_OCORRENCIA_ENTREGA_PARCIAL.equals(ocorrenciaAtual.getCdOcorrenciaEntrega())){
			deleteOcorrenciaEntregaNotaFiscal(med, ocorrenciaAtual); 
		}

		executeRegrasOcorrenciaPendencia(ocorrenciaAnterior, ocorrenciaAtual, doctoServico, med);
		
		/* Busca novamente o documento para garantir que não será desfeita nenhuma alteração realizada por outra "rotina" anterior. */
		doctoServico = doctoServicoService.findById(doctoServico.getIdDoctoServico());
		if(localizacaoMercadoriaAux != null){
			doctoServico.setLocalizacaoMercadoria(localizacaoMercadoriaAux);
			doctoServicoService.store(doctoServico);
		}
		
		// 5056 - parte 3
		volumeNotaFiscalService.updateLocalizacaoMercadoriaByDoctoServico(doctoServico.getIdDoctoServico(), doctoServico.getLocalizacaoMercadoria());
		
		if("EP".equals(med.getManifestoEntrega().getManifesto().getTpManifestoEntrega().getValue())){
			
			String recusaTpManifEP = "N";
			try {
				recusaTpManifEP = (String) configuracoesFacade.getValorParametroWithException(doctoServico.getFilialByIdFilialDestino().getIdFilial(), "RECUSA_TP_MANIF_EP");
			} catch (BusinessException e) {
			}
			if("S".equals(recusaTpManifEP) && 
					("R".equals(ocorrenciaAtual.getTpOcorrencia().getValue()) || "N".equals(ocorrenciaAtual.getTpOcorrencia().getValue())) &&
					ocorrenciaAtual.getBlOcasionadoMercurio()){
				
				LocalizacaoMercadoria localizacaoNoTerminal = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(ConstantesSim.CD_MERCADORIA_NO_TERMINAL);
				doctoServico.setLocalizacaoMercadoria(localizacaoNoTerminal);
				manifestoEntregaDocumentoService.saveOrUpdate(doctoServico);
				
				volumeNotaFiscalService.updateLocalizacaoMercadoriaByDoctoServico(doctoServico.getIdDoctoServico(), doctoServico.getLocalizacaoMercadoria());
				
				if(ocorrenciaAtual.getOcorrenciaPendencia() != null){
					
					OcorrenciaDoctoServico ocorrenciaDoctoServicoBuscado = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());
					FaseProcesso fp = new FaseProcesso();
					fp.setCdFase(ConstantesExpedicao.CD_FASE_PROCESSO);
					
					ocorrenciaDoctoServicoBuscado.setFaseProcesso(ocorrenciaDoctoServicoService.getFaseProcessoService().findByCdFaseProcesso(fp).get(0));
					ocorrenciaDoctoServicoService.store(ocorrenciaDoctoServicoBuscado);
				}
			}
		}
	}

	private void deleteOcorrenciaEntregaNotaFiscal(ManifestoEntregaDocumento med, OcorrenciaEntrega ocorrenciaAtual) {
		List<EntregaNotaFiscal> enfList = entregaNotaFiscalService.findByIdManifestoEntregaDocumento(med.getIdManifestoEntregaDocumento());
		for (EntregaNotaFiscal entregaNotaFiscal : enfList) {
			
			if(entregaNotaFiscal.getOcorrenciaEntrega().getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_REALIZADA) || entregaNotaFiscal.getOcorrenciaEntrega().getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_PARCIAL)) {
				List<NotaFiscalOperada> listDeletar = notaFiscalOperadaService.findByIdNotaFiscalConhecimentoidDoctoServicoTpSituacao(entregaNotaFiscal.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento(), med.getDoctoServico().getIdDoctoServico(), TP_SITUACAO_ENTREGUE);
				notaFiscalOperadaService.removeById(listDeletar.get(0).getIdNotaFiscalOperada());
			}
			
			entregaNotaFiscalService.removeById(entregaNotaFiscal.getIdEntregaNotaFiscal());
		}
	}
	
	private void validateNotaDevolvidaOuRefatorada(Long idDoctoServico, OcorrenciaEntrega ocorrenciaAnterior) {
		if(CD_OCORRENCIA_ENTREGA_PARCIAL.equals(ocorrenciaAnterior.getCdOcorrenciaEntrega())
				&& CollectionUtils.isNotEmpty(notaFiscalOperadaService.findNotaDevolvidaOuRefatorada(idDoctoServico))){
			throw new BusinessException("LMS-09165");
		}
	}

	/**
	 * Quando for gerado o evento de cancelamento do documento de serviço, o mesmo evento deve ser gerado para os volumes deste documento.
	 * 
	 * @param idDoctoServico do qual será obtido os volumes que receberam o evento
	 * @param cdEvento código do evento que será criado
	 * @param ocorrenciaEntrega nova ocorrencia de entrega que deve ser adicionada ao evento volume  
	 */
	private void generateEventoVolumes(Long idDoctoServico, Short cdEvento, OcorrenciaEntrega ocorrenciaEntrega, DateTime dhOcorrencia) {
		List<VolumeNotaFiscal> volumesNotaFiscal = volumeNotaFiscalService.findByIdConhecimento(idDoctoServico);
		
		for (VolumeNotaFiscal volumeNotaFiscal : volumesNotaFiscal) {
			eventosRastreabilidadeInternacionalService.generateEventoVolume(volumeNotaFiscal, cdEvento, ocorrenciaEntrega, dhOcorrencia);
		}
	}

	/**
	 * 
	 * LMS-4567
	 * @param ocorrenciaEntregaAnterior
	 * @param ocorrenciaEntregaAtual
	 * @param doctoServico
	 */
	private void executeRegrasOcorrenciaPendencia(OcorrenciaEntrega ocorrenciaEntregaAnterior, OcorrenciaEntrega ocorrenciaEntregaAtual, DoctoServico doctoServico, ManifestoEntregaDocumento med) {
		OcorrenciaPendencia ocorrenciaPendencia11 = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf("11"));
		Long idOcorrenciaCd11 = ocorrenciaPendencia11.getIdOcorrenciaPendencia();
		
		Long idOcorrenciaBlAtual = null;
		if(ocorrenciaEntregaAnterior.getOcorrenciaPendencia() != null){
			idOcorrenciaBlAtual = ocorrenciaEntregaAnterior.getOcorrenciaPendencia().getIdOcorrenciaPendencia();
		}
		
		Long idOcorrenciaBlNova = null;
		if(ocorrenciaEntregaAtual.getOcorrenciaPendencia() != null){
			idOcorrenciaBlNova = ocorrenciaEntregaAtual.getOcorrenciaPendencia().getIdOcorrenciaPendencia();
		}
		
		/* Liberação */
		if(idOcorrenciaBlAtual != null && (idOcorrenciaBlNova == null || idOcorrenciaBlNova.compareTo(idOcorrenciaBlAtual) != 0)){
			ocorrenciaDoctoServicoService.validateVerificaDePara(idOcorrenciaBlAtual, idOcorrenciaCd11);
			
			OcorrenciaDoctoServico ocorrenciaDoctoServico = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());
			if (ocorrenciaDoctoServico != null) {
				DateTime dhOcorrencia = ocorrenciaDoctoServico.getDhBloqueio();
				dhOcorrencia = dhOcorrencia.plusMinutes(1);

				ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(doctoServico.getIdDoctoServico(), idOcorrenciaCd11, null, dhOcorrencia, null);
			}
		}
		
		/* Bloqueio */
		if(idOcorrenciaBlNova != null && (idOcorrenciaBlAtual == null || idOcorrenciaBlAtual.compareTo(idOcorrenciaBlNova) != 0)){
			DateTime dhOcorrencia = null;
			
			OcorrenciaDoctoServico ocorrenciaDoctoServico = ocorrenciaDoctoServicoService.findLastOcorrenciaDoctoServicoByIdDoctoServico(doctoServico.getIdDoctoServico());
			if(ocorrenciaDoctoServico != null && ocorrenciaDoctoServico.getDhLiberacao() != null
					&& med.getDhOcorrencia() != null && ocorrenciaDoctoServico.getDhLiberacao().compareTo(med.getDhOcorrencia()) > 0 ){
				
				dhOcorrencia = ocorrenciaDoctoServico.getDhLiberacao();
				dhOcorrencia = dhOcorrencia.plusMinutes(1);
				
			} else {
				dhOcorrencia = med.getDhOcorrencia();
			}
			
			ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(doctoServico.getIdDoctoServico(), 
					idOcorrenciaBlNova, null, dhOcorrencia, null);
		}
	}

	public void alteraSituacaoManifestoEntregaDocumento(ManifestoEntregaDocumento med){
		String tpSituacaoDoc = med.getTpSituacaoDocumento().getValue();

		if (tpSituacaoDoc.equals("PRCO")){
			med.setTpSituacaoDocumento(new DomainValue("PBRC"));

		} else if (tpSituacaoDoc.equals("PREC")){
			med.setTpSituacaoDocumento(new DomainValue("PBRE"));

		} else if (tpSituacaoDoc.equals("PCOM")){
			med.setTpSituacaoDocumento(new DomainValue("PBCO"));

		} else if (tpSituacaoDoc.equals("FECH")){
			med.setTpSituacaoDocumento(new DomainValue("PBAI"));
		} 

	}
	
	
	public void atualizaDadosManifestoEntregaDocumento(ManifestoEntregaDocumento med, Long idOcorrenciaEntrega, String obMotivoAlteracao){
		OcorrenciaEntrega oe = ocorrenciaEntregaService.findById(idOcorrenciaEntrega);
		
		med.setDhOcorrencia(null);
		med.setOcorrenciaEntrega(null);
						
		StringBuffer obAlteracao = new  StringBuffer().append(configuracoesFacade.getMensagem("alteracao"))
		.append(" - ")
		.append(JTFormatUtils.format(JTDateTimeUtils.getDataHoraAtual(), JTFormatUtils.SHORT))
		.append(" - ")
		.append(SessionUtils.getUsuarioLogado().getNmUsuario())
		.append(" - ")
		.append(oe.getCdOcorrenciaEntrega())
		.append(" ")
		.append(oe.getDsOcorrenciaEntrega())
		.append("\n")
		.append(med.getObAlteracao() != null ? med.getObAlteracao() : "");
		
		med.setObAlteracao(obAlteracao.toString());
		med.setObMotivoAlteracao(obMotivoAlteracao);
				
	}
	
	/**
	 * Metodo para validar se o usuario possui permissao para alterar o manifesto 
	 *
	 */
	public void validateAcessoRestrito(){
		 
	}
	
	/**
	 * @param ocorrenciaEntregaService The ocorrenciaEntregaService to set.
	 */
	public void setOcorrenciaEntregaService(
			OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	/**
	 * @param manifestoEntregaDocumentoService The manifestoEntregaDocumentoService to set.
	 */
	public void setManifestoEntregaDocumentoService(
			ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * @param eventosRastreabilidadeInternacionalService The eventosRastreabilidadeInternacionalService to set.
	 */
	public void setEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService eventosRastreabilidadeInternacionalService) {
		this.eventosRastreabilidadeInternacionalService = eventosRastreabilidadeInternacionalService;
	}

	/**
	 * @param doctoServicoService The doctoServicoService to set.
	 */
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public CancelarEntregaAlterarOcorrenciaService getThis() {
		return (CancelarEntregaAlterarOcorrenciaService) AopContext.currentProxy();
	}

	public void setDoctoServicoLookupFacade(
			DoctoServicoLookupFacade doctoServicoLookupFacade) {
		this.doctoServicoLookupFacade = doctoServicoLookupFacade;
	}

	public DadosComplementoService getDadosComplementoService() {
		return dadosComplementoService;
	}

	public void setDadosComplementoService(
			DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}

	public InformacaoDoctoClienteService getInformacaoDoctoClienteService() {
		return informacaoDoctoClienteService;
	}

	public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
}
	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public OcorrenciaPendenciaService getOcorrenciaPendenciaService() {
		return ocorrenciaPendenciaService;
}

	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	public void setNotaFiscalOperadaService(
			NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public void setEntregaNotaFiscalService(
			EntregaNotaFiscalService entregaNotaFiscalService) {
		this.entregaNotaFiscalService = entregaNotaFiscalService;
	}

	public void setManifestoEntregaVolumeService(
			ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
		this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

}
