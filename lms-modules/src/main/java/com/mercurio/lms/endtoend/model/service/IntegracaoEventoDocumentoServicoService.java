package com.mercurio.lms.endtoend.model.service;

import br.com.tntbrasil.integracao.domains.endToEnd.*;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoVolumeService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.*;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.edi.model.ConhecimentoFedex;
import com.mercurio.lms.edi.model.service.ConhecimentoFedexService;
import com.mercurio.lms.entrega.model.*;
import com.mercurio.lms.entrega.model.service.*;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.integracao.model.service.IMessageProducer;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IntegracaoEventoDocumentoServicoService {

	private static final String TP_SITUACAO_ENTREGUE = "EN";
	private static final String PARAMETRO_NR_ROTA_SUBCONTRATACAO = "NR_ROTA_SUBCONTRATACAO";
	private static final String PARAMETRO_NR_ROTA_ENTREGA_FAAV = "NR_ROTA_ENTREGA_FAAV";
	private static final String CD_EVENTO_DOCTO_SERVICO_IB = "CD_EVENTO_DOCTO_SERVICO_IB";
	private static final String LOGIN_USUARIO_INTEGRACAO = "integracao";
	private static final String CD_EVENTO_EMISSAO_CTE = "6";
	private static final String CD_EVENTO_RNC = "407";
	private static final Short CD_OCORRENCIA_ENTREGA_PARCIAL = new Short("102");
	private static final Short CD_OCORRENCIA_ENTREGA_REALIZADA = new Short("1");
	private static final String LIST_COD_OCORRENCIA_FDX = "LIST_COD_OCORRENCIA_FDX";
	private static final String FECH = "FECH";
	private static final String DS_OBS_INTEGRACAO_FEDEX = "Integração Fedex";
	private static final Long ID_USUARIO_INTEGRACAO = 5000L;

	public static final String TP_OCORRENCIA_OCORRENCIA_ENTREGA = "1";
	public static final String TP_OCORRENCIA_EVENTO_OPERACIONAL = "3";
	public static final String TP_OPERACAO_SUBCONTRATACAO = "SUB";
	public static final String TP_OPERACAO_MDS = "MDS";
	public static final String TP_ENTREGA_PARCIAL = "P";
	public static final String LOCALIZ_FINALIZA_INT_FDX = "LOCALIZ_FINALIZA_INT_FDX";

	private IntegracaoJmsService integracaoJmsService;
	private ParametroGeralService parametroGeralService;
	private DoctoServicoService doctoServicoService;
	private FilialService filialService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private EventoService eventoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private VolDadosSessaoService volDadosSessaoService;
	private UsuarioService usuarioService;
	private ConhecimentoFedexService conhecimentoFedexService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private ManifestoEntregaService manifestoEntregaService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private ConfiguracoesFacade configuracoesFacade;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private ControleCargaService controleCargaService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private PessoaService pessoaService;
	private MotoristaService motoristaService;
    private MeioTransporteService meioTransporteService;
    private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
    private MeioTranspProprietarioService meioTranspProprietarioService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private CancelarEntregaAlterarOcorrenciaService cancelarEntregaAlterarOcorrenciaService;
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private PreManifestoVolumeService preManifestoVolumeService;
	private EntregaNotaFiscalService entregaNotaFiscalService;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService;
	private EventoVolumeService eventoVolumeService;
    private RegistrarBaixaEntregasService registrarBaixaEntregasService;
	private ConhecimentoService conhecimentoService;

	public void executeEnvioEventoDocumentoServicoIntegracao(final EventoDocumentoServico eventoDctoServico) {

		String paramEventosDocServicoIB = (String) parametroGeralService.findConteudoByNomeParametro(CD_EVENTO_DOCTO_SERVICO_IB, false);
    	List<String> cdsEvento = Arrays.asList(paramEventosDocServicoIB.split(";"));
    	if (!cdsEvento.contains(eventoDctoServico.getEvento().getCdEvento().toString())) {
    		return;
    	}

		integracaoJmsService.sendUncompressedMonitoredMessage(Queues.EVENTO_DOCUMENTO_SERVICO, (IMessageProducer<EEventoDocumentoServico>) () -> {

			EEnvioEventoDocumentoServico eEnvioEventoDocumentoServico = new EEnvioEventoDocumentoServico(executeBuildEEventoDocumentoServico(eventoDctoServico));
			eEnvioEventoDocumentoServico.setChavePesquisa(eventoDctoServico.getDoctoServico().getTpDocumentoServico().getValue()
					+ eventoDctoServico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial()
					+ eventoDctoServico.getDoctoServico().getNrDoctoServico());

			return eEnvioEventoDocumentoServico;
		});
	}

	public EEventoDocumentoServico executeBuildEEventoDocumentoServico(EventoDocumentoServico eventoDctoServico){
	    EEventoDocumentoServico eEventoDoctoServico = new EEventoDocumentoServico();

	    eEventoDoctoServico.setIdEventoDocumentoServico(eventoDctoServico.getIdEventoDocumentoServico());
	    eEventoDoctoServico.setDhEvento(eventoDctoServico.getDhEvento());
	    eEventoDoctoServico.setDhInclusao(eventoDctoServico.getDhInclusao());
	    eEventoDoctoServico.setBlEventoCancelado(eventoDctoServico.getBlEventoCancelado());
	    eEventoDoctoServico.setNrDocumento(eventoDctoServico.getNrDocumento());
	    eEventoDoctoServico.setObComplemento(eventoDctoServico.getObComplemento());
	    eEventoDoctoServico.setDhGeracaoParceiro(eventoDctoServico.getDhGeracaoParceiro());
	    eEventoDoctoServico.setDhComunicacao(eventoDctoServico.getDhComunicacao());
	    eEventoDoctoServico.setTpDocumento(eventoDctoServico.getTpDocumento().getValue());
	    eEventoDoctoServico.setIdUsuario(eventoDctoServico.getUsuario().getIdUsuario());
	    eEventoDoctoServico.setIdDoctoServico(eventoDctoServico.getDoctoServico().getIdDoctoServico());
	    eEventoDoctoServico.setIdEvento(eventoDctoServico.getEvento().getIdEvento());
	    eEventoDoctoServico.setCdEvento(eventoDctoServico.getEvento().getCdEvento());
	    eEventoDoctoServico.setIdFilial(eventoDctoServico.getFilial().getIdFilial());

	    if(eventoDctoServico.getPedidoCompra()!= null){
	        eEventoDoctoServico.setIdPedidoCompra(eventoDctoServico.getPedidoCompra().getIdPedidoCompra());
	    }
	    if(eventoDctoServico.getOcorrenciaEntrega()!= null){
	        eEventoDoctoServico.setIdOcorrenciaEntrega(eventoDctoServico.getOcorrenciaEntrega().getIdOcorrenciaEntrega());
	    }
	    if(eventoDctoServico.getOcorrenciaPendencia()!= null){
	        eEventoDoctoServico.setIdOcorrenciaPendencia(eventoDctoServico.getOcorrenciaPendencia().getIdOcorrenciaPendencia());
	    }

	    return eEventoDoctoServico;
	}


	public void executeRecebimentoTrackingCte(ERecebimentoTrackingFedex recebimentoTrackingFedex){
		ETrackingCTE trackingCte = recebimentoTrackingFedex.getDados();

		DoctoServico doctoServico = doctoServicoService.findByTipoDoctoFilialNumero(
					trackingCte.getNrCNPJEmitente(), trackingCte.getNrDoctoServico(), trackingCte.getTpDoctoServico());

		if (doctoServico == null){
			throw new BusinessException("LMS-09161", new Object[]{trackingCte.getNrDocumento()});
		}

		List<Filial>  filiais = filialService.findFiliaisByCdFilialFedex(trackingCte.getCdFilialEvento());
		Filial filialEvento = filiais.get(0);

		setDadosSession(filialEvento);
		String tpOperacao = trackingCte.getTpOperacao();
		if (TP_OCORRENCIA_EVENTO_OPERACIONAL.equals(trackingCte.getTpOcorrencia())
				&& TP_OPERACAO_SUBCONTRATACAO.equals(tpOperacao)){
		    if(CD_EVENTO_EMISSAO_CTE.equals(trackingCte.getCdEvento())){
		        generateConhecimentoFedex(doctoServico, trackingCte);
		    }else{
		        generateEventoTrackingFedex(trackingCte, doctoServico,filialEvento);
		    }
		}else if (TP_OCORRENCIA_OCORRENCIA_ENTREGA.equals(trackingCte.getTpOcorrencia())
				&& (TP_OPERACAO_MDS.equals(tpOperacao) || TP_OPERACAO_SUBCONTRATACAO.equals(tpOperacao))){
			generateEventoOcorrenciaEntregaFedex(trackingCte, doctoServico, filialEvento);

		}
	}

	private void generateEventoOcorrenciaEntregaFedex(ETrackingCTE trackingCte, DoctoServico doctoServico, Filial filialEvento) {

		//Caso a ocorrência esteja com algum dos seguintes status de localização, desprezar registros de “não entrega” posteriores:
		String paramNaoEntrega = (String) parametroGeralService.findConteudoByNomeParametro(LOCALIZ_FINALIZA_INT_FDX, false);
		List<String> cdsEvento = Arrays.asList(paramNaoEntrega.split(";"));
		if (cdsEvento.contains(doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().toString())) {
			throw new BusinessException("LMS-09171");
		}

		Short cdOcorrenciaEntrega = Short.valueOf(findCdOcorrencia(trackingCte));
		Conhecimento conhecimento =  (Conhecimento) doctoServico;

		if(Boolean.TRUE.equals(hasOcorrenciaPendencia(trackingCte))){

			OcorrenciaPendencia ocorrenciaPendenciaLiberacao =  ocorrenciaPendenciaService.findByCodigoOcorrencia(cdOcorrenciaEntrega);

			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
			typedFlatMap.put("ocorrenciaPendencia.blApreensao", ocorrenciaPendenciaLiberacao.getBlApreensao());
			typedFlatMap.put("ocorrenciaPendencia.idOcorrenciaPendencia",ocorrenciaPendenciaLiberacao.getIdOcorrenciaPendencia());
			typedFlatMap.put("ocorrenciaPendencia.evento.idEvento",ocorrenciaPendenciaLiberacao.getEvento().getIdEvento());
			typedFlatMap.put("ocorrenciaPendencia.tpOcorrencia",ocorrenciaPendenciaLiberacao.getTpOcorrencia().getValue());
			typedFlatMap.put("dhOcorrencia", getDhOcorrencia(trackingCte));

			ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(typedFlatMap);

		}else{
			OcorrenciaEntrega ocorrenciaEntrega= ocorrenciaEntregaService.findOcorrenciaEntregaByCodigoTipo(cdOcorrenciaEntrega);
			if (isEntregaParcial(trackingCte)){
				ocorrenciaEntrega = ocorrenciaEntregaService.findOcorrenciaEntrega(CD_OCORRENCIA_ENTREGA_PARCIAL);
			}

			if (Boolean.TRUE.equals(conhecimento.getBlBloqueado()
					&& existeCodOcorrencia(LIST_COD_OCORRENCIA_FDX, ocorrenciaEntrega.getCdOcorrenciaEntrega(), ";"))
					&& !isEntregaParcial(trackingCte)){

				String nmParametroOcorrencia = "OCORRENCIA_LIBERACAO_FEDEX";

				BigDecimal cdOcorrenciaBloqueio = (BigDecimal)parametroGeralService.findConteudoByNomeParametroWithoutException(nmParametroOcorrencia, false);
				OcorrenciaPendencia ocorrencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf(cdOcorrenciaBloqueio.toString()));
				ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(conhecimento.getIdDoctoServico(),
						ocorrencia.getIdOcorrenciaPendencia(), null, JTDateTimeUtils.getDataHoraAtual(), null);
			}

			String nrRota = null;
	        if (TP_OPERACAO_SUBCONTRATACAO.equals(trackingCte.getTpOperacao())){
	            nrRota = (String)configuracoesFacade.getValorParametro(PARAMETRO_NR_ROTA_SUBCONTRATACAO);
	        }else if (TP_OPERACAO_MDS.equals(trackingCte.getTpOperacao())){
	            nrRota = (String)configuracoesFacade.getValorParametro(PARAMETRO_NR_ROTA_ENTREGA_FAAV);
	        }

	        ManifestoEntrega manifestoEntrega = manifestoEntregaService.findManifestoAbertoByDoctoServico(filialEvento,Short.valueOf(nrRota),doctoServico);
	        if (manifestoEntrega != null){
	            executeBaixaManifestoAutomatico(trackingCte, doctoServico,filialEvento, ocorrenciaEntrega, manifestoEntrega, nrRota);
	        }else{

	            manifestoEntrega  = manifestoEntregaService.findManifestoAbertoByOcorrenciaSubcontratacaoFedex(doctoServico);
	            if (manifestoEntrega != null){
	                ManifestoEntregaDocumento manifestoEntregaDocumento = getManifestoEntregaDocumentoFromManifesto(manifestoEntrega, doctoServico);

	                if (isEntregaParcial(trackingCte)){
	                    executeUpdateEntregaParcialNotas(trackingCte, manifestoEntregaDocumento);
	                    manifestoEntregaDocumento.setOcorrenciaEntrega(ocorrenciaEntrega);

	                    //Atualiza a ocorrencia de entrega igual ao baixa parceiras
		                if (manifestoEntregaDocumento!=null){
		                    executeAtualizacaoOcorrenciaManifestoEntrega(manifestoEntregaDocumento, ocorrenciaEntrega, filialEvento,trackingCte);
		                }
	                } else {
	                	executeBaixaManifestoAutomatico(trackingCte, doctoServico,filialEvento, ocorrenciaEntrega, null, nrRota);
	                }

	            }else{
	                executeBaixaManifestoAutomatico(trackingCte, doctoServico,filialEvento, ocorrenciaEntrega, null, nrRota);
	            }
	        }
		}
	}

	private void executeBaixaEntregaSemValidacoes(ManifestoEntrega manifestoEntrega, DoctoServico doctoServico, OcorrenciaEntrega ocorrenciaEntrega,
												  ETrackingCTE trackingCte, Filial filial, Usuario usuario) {

		Evento evento = this.eventoService.findByCdEvento(ocorrenciaEntrega.getEvento().getCdEvento());

		String nrDocumento = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + doctoServico.getNrDoctoServico();

		this.incluirEventosRastreabilidadeInternacionalService.gerarEventoDocumento(evento, doctoServico, filial, nrDocumento,
				getDhOcorrencia(trackingCte), DS_OBS_INTEGRACAO_FEDEX, trackingCte.getTpDocumento(), ocorrenciaEntrega.getIdOcorrenciaEntrega(),
				usuario);

		ManifestoEntregaDocumento manifestoEntregaDocumento = getManifestoEntregaDocumentoFromManifesto(manifestoEntrega, doctoServico) == null ?
		manifestoEntregaDocumentoService.findManifestoEntregaDocumento(manifestoEntrega.getIdManifestoEntrega(), doctoServico.getIdDoctoServico()) :
				getManifestoEntregaDocumentoFromManifesto(manifestoEntrega, doctoServico);

		if(manifestoEntregaDocumento == null) {
			throw new NullPointerException("Manifesto de Entrega Documento não foi encontrado.");
		}

		manifestoEntregaDocumento.setTpSituacaoDocumento(new DomainValue(FECH));
		manifestoEntregaDocumento.setUsuario(this.usuarioService.findById(ID_USUARIO_INTEGRACAO));
		manifestoEntregaDocumento.setBlRetencaoComprovanteEnt(trackingCte.getBlRetencaoComprovanteEntrega());
		manifestoEntregaDocumento.setDhOcorrencia(getDhOcorrencia(trackingCte));
		manifestoEntregaDocumento.setOcorrenciaEntrega(ocorrenciaEntrega);
		manifestoEntregaDocumento.setNmRecebedor(trackingCte.getNmRecebedor());

		this.manifestoEntregaDocumentoService.saveOrUpdate(doctoServico);

	}

	private boolean existeCodOcorrencia(String nomeParametroGeralList, Short cdOcorrencia, String regex) {
		String result = (String) parametroGeralService.findConteudoByNomeParametro(nomeParametroGeralList, false);
		List<String> codOcorrenciaList = Arrays.asList(result.split(regex));
		return codOcorrenciaList.contains(String.valueOf(cdOcorrencia));
	}

	private void executeUpdateEntregaParcialNotas(ETrackingCTE eTrackingCTE, ManifestoEntregaDocumento manifestoEntregaDocumento) {
		if (manifestoEntregaDocumento == null){
			return;
		}

		/* Busca todas as notas do manifesto com ocorrencia de entrega parcial (cdOcorrencia=102)
		 *
		 * Este trecho ficou diferente da estrutura da especificação para evitar gargalo de performance, buscando todas as notas
		 * em uma query só ao invés de buscar cada uma dentro do for
		 * */
		DoctoServico doctoServico= manifestoEntregaDocumento.getDoctoServico();
		ManifestoEntrega manifestoEntrega = manifestoEntregaDocumento.getManifestoEntrega();
		List listDadosNotas = manifestoEntregaDocumentoService.findDadosNotasEntregaParcial(manifestoEntrega.getIdManifestoEntrega(), doctoServico.getIdDoctoServico());
		for (ETrackingCTENotas eTrackingCTENotas : eTrackingCTE.getListNotas()) {
			Map<String,Object> dadosNota = findDadosNotaEntregaParcial(listDadosNotas,eTrackingCTENotas.getNrNF());
			if (dadosNota !=null){
				executeAtualizaEntregaNotaFiscal(dadosNota, eTrackingCTENotas, doctoServico, manifestoEntregaDocumento);
			}
		}

	}

	private void executeBaixaVolumes(DoctoServico doctoServico,
			ManifestoEntrega manifestoEntrega,
			ETrackingCTENotas eTrackingCTENotas) {

		Short cdOcorrenciaNova = new Short(eTrackingCTENotas.getCdOcorrencia());
		List<ManifestoEntregaVolume> manifestoEntregaVolumes = manifestoEntregaVolumeService.findByManifestoAndDoctoServico(manifestoEntrega.getIdManifestoEntrega(), doctoServico.getIdDoctoServico());

		Short cdOcorrenciaVolumes = CD_OCORRENCIA_ENTREGA_PARCIAL.equals(cdOcorrenciaNova) ? CD_OCORRENCIA_ENTREGA_REALIZADA:cdOcorrenciaNova;
		for (ManifestoEntregaVolume manifestoEntregaVolume : manifestoEntregaVolumes) {
			Integer nrNota = manifestoEntregaVolume.getVolumeNotaFiscal().getNotaFiscalConhecimento().getNrNotaFiscal();
			if (nrNota.equals(new Integer(eTrackingCTENotas.getNrNF()))){
				registrarBaixaEntregasService.storeOcorrenciaOnManifestoEntregaVolume(manifestoEntregaVolume.getIdManifestoEntregaVolume(),
						cdOcorrenciaVolumes, "N",eTrackingCTENotas.getDhOcorrencia() , false, false, manifestoEntrega.getFilial(), SessionUtils.getUsuarioLogado());
			}
		}
	}

	private void carregaDadosVolumes(List listDadosNotas,Long idManifestoEntrega, Long idDoctoServico, Short cdOcorrenciaEntrega) {
		List<Map<String, Object>> volumes = manifestoEntregaVolumeService.findDadosVolumeEntregaParcial(idManifestoEntrega, idDoctoServico, cdOcorrenciaEntrega, null);
		for (Map<String, Object> mapVolume : volumes) {
			String nrNota = (String)mapVolume.get("nrNotaFiscal");
			Map dadosNota = findDadosNotaEntregaParcial(listDadosNotas, nrNota);
			if (dadosNota != null){
				dadosNota.put("dadosVolume", mapVolume);
			}
		}
	}

	private void finalizaCte(DoctoServico doctoServico,
			ETrackingCTENotas eTrackingCTENotas) {
		Short cdOcorrenciaNova = new Short(eTrackingCTENotas.getCdOcorrencia());
		if (CD_OCORRENCIA_ENTREGA_REALIZADA.equals(cdOcorrenciaNova)){
			registrarBaixaEntregaPorNotaFiscalService.executeFinalizacaoCteOriginal(doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
		}
	}

	private void executeAtualizaEntregaNotaFiscal(Map<String, Object> dadosNota, ETrackingCTENotas eTrackingCTENotas, DoctoServico doctoServico, ManifestoEntregaDocumento manifestoEntregaDocumento) {

		Short cdOcorrenciaNova = new Short(eTrackingCTENotas.getCdOcorrencia());
		OcorrenciaEntrega ocorrenciaEntregaNova = ocorrenciaEntregaService.findOcorrenciaEntrega(cdOcorrenciaNova);
		Usuario usuarioIntegracao = usuarioService.findById(5000L);
		String observacao = configuracoesFacade.getMensagem("alteracaoEntregaNotaFiscal",
				new Object[]{
					JTDateTimeUtils.getDataHoraAtual(),
					usuarioIntegracao.getNmUsuario(),
					cdOcorrenciaNova,
					ocorrenciaEntregaNova.getDsOcorrenciaEntrega().getValue()});

		Long idNotaFiscalConhecimento = (Long)dadosNota.get("idNotaFiscalConhecimento");
		ManifestoEntrega manifestoEntrega = manifestoEntregaService.findById(manifestoEntregaDocumento.getManifestoEntrega().getIdManifestoEntrega());
		Long idEntregaNotaFiscal = entregaNotaFiscalService.findIdByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
		if (idEntregaNotaFiscal != null){
			EntregaNotaFiscal entregaNotaFiscal = entregaNotaFiscalService.findById(idEntregaNotaFiscal);
			if (CD_OCORRENCIA_ENTREGA_REALIZADA.equals(entregaNotaFiscal.getOcorrenciaEntrega().getCdOcorrenciaEntrega())){
				notaFiscalOperadaService.removeNotasFinalizadasByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
			}

			entregaNotaFiscalService.executeAlteracaoNota(idNotaFiscalConhecimento, ocorrenciaEntregaNova.getIdOcorrenciaEntrega(), observacao);
		}else{

			NotaFiscalConhecimento notaFiscalConhecimento = new NotaFiscalConhecimento();
			notaFiscalConhecimento.setIdNotaFiscalConhecimento((Long) dadosNota.get("idNotaFiscalConhecimento"));

			UsuarioLMS usuario = new UsuarioLMS();
			usuario.setIdUsuario(5000L);

			EntregaNotaFiscal entregaNotaFiscal = new EntregaNotaFiscal();
			entregaNotaFiscal.setManifesto(manifestoEntrega.getManifesto());
			entregaNotaFiscal.setManifestoEntregaDocumento(manifestoEntregaDocumento);
			entregaNotaFiscal.setNotaFiscalConhecimento(notaFiscalConhecimento);
			entregaNotaFiscal.setOcorrenciaEntrega(ocorrenciaEntregaNova);
			entregaNotaFiscal.setDhOcorrencia(eTrackingCTENotas.getDhOcorrencia());
			entregaNotaFiscal.setUsuario(usuario);

			entregaNotaFiscalService.store(entregaNotaFiscal);
		}

		Short cdOcorrenciaOriginal = (Short) dadosNota.get("cdOcorrenciaEntrega");
		Integer nrNota = (Integer)dadosNota.get("nrNotaFiscal");
		List<Map<String, Object>> volumes = manifestoEntregaVolumeService.findDadosVolumeEntregaParcial(manifestoEntrega.getIdManifestoEntrega(), doctoServico.getIdDoctoServico(), cdOcorrenciaOriginal, nrNota);
		if (CollectionUtils.isNotEmpty(volumes)){
			cancelaEventosVolume(volumes);
		}
		generateEventoVolumesIntegracao(idNotaFiscalConhecimento, ocorrenciaEntregaNova.getEvento(),manifestoEntrega.getFilial(),usuarioIntegracao, eTrackingCTENotas.getDhOcorrencia());



		//Inclui na tabela nota fiscal operada caso a ocorrencia NOVA seja de entrega parcial.
		if (CD_OCORRENCIA_ENTREGA_REALIZADA.equals(cdOcorrenciaNova)){
			NotaFiscalConhecimento notaFiscalConhecimento = new NotaFiscalConhecimento();
			notaFiscalConhecimento.setIdNotaFiscalConhecimento((Long) dadosNota.get("idNotaFiscalConhecimento"));

			NotaFiscalOperada notaOperada = new NotaFiscalOperada();
			notaOperada.setDoctoServico(doctoServico);
			notaOperada.setTpSituacao(new DomainValue(TP_SITUACAO_ENTREGUE));
			notaOperada.setNotaFiscalConhecimentoOriginal(notaFiscalConhecimento);
			notaFiscalOperadaService.store(notaOperada);

			registrarBaixaEntregaPorNotaFiscalService.executeFinalizacaoCteOriginal(doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());

		}


	}

	private void generateEventoVolumesIntegracao(Long idNotaFiscalConhecimento, Evento evento, Filial filial, Usuario usuario, DateTime dhOcorrencia) {

		List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findfindByIdNotaFiscal(idNotaFiscalConhecimento);
		eventoVolumeService.storeEventoVolumeDhOcorrencia(volumes, evento.getCdEvento(),
				"LM", filial.getIdFilial(),
				usuario, dhOcorrencia);
	}

	private void cancelaEventosVolume(List<Map<String, Object>> volumes) {
		for (Map<String, Object> map : volumes) {

			Long idEventoVolume = (Long)map.get("idEventoVolume");
			eventoVolumeService.executeCancelaEventoByIdEvento(idEventoVolume);
		}
	}

	private Map<String, Object> findDadosNotaEntregaParcial(List listDadosNotas, String nrNotaFiscal) {
		for (Object dadosNota : listDadosNotas) {
			Integer nrNota = (Integer)((Map<String,Object>)dadosNota).get("nrNotaFiscal");
			if (nrNota.toString().equals(nrNotaFiscal)){
				return (Map<String,Object>)dadosNota;
			}
		}
		return null;
	}

	private boolean isEntregaParcial(ETrackingCTE trackingCte) {
		return TP_ENTREGA_PARCIAL.equals(trackingCte.getTpEntrega());
	}

	private void generateEventoTrackingFedex(ETrackingCTE trackingCte,
			DoctoServico doctoServico, Filial filialEvento) {
		Evento evento = eventoService.findByCdEvento(Short.valueOf(trackingCte.getCdEvento()));

		if (evento == null){
		    throw new BusinessException("LMS-09162", new Object[]{trackingCte.getCdEvento()});
		}

		if (!validateAtualizaLocalizacao(doctoServico,trackingCte)){
		    eventoService.evict(evento);
		    evento.setLocalizacaoMercadoria(null);
		}

		String observacao = trackingCte.getObComplemento();
		if (CD_EVENTO_RNC.equals(trackingCte.getCdEvento())){
			observacao = configuracoesFacade.getMensagem("observacaoSip", new Object[]{trackingCte.getNrProcessoSIP(),trackingCte.getDsMotivoAberturaSip(),buildNrNotas(trackingCte.getListNotas())});
		}

		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(evento,
		        doctoServico.getIdDoctoServico(),
		        filialEvento.getIdFilial(),
		        trackingCte.getNrDocumento(),
		        trackingCte.getDhEvento(),
		        null,
		        observacao,
		        trackingCte.getTpDocumento(), null,
		        null,
		        false,
				SessionUtils.getUsuarioLogado());
	}

	private String buildNrNotas(List<ETrackingCTENotas> listNotas) {
		StringBuilder notas = new StringBuilder();
		for (ETrackingCTENotas eTrackingCTENotas : listNotas) {
			if (notas.toString().isEmpty()){
				notas.append(eTrackingCTENotas.getNrNF());
			}else{
				notas.append(", ").append(eTrackingCTENotas.getNrNF());
			}
		}
		return notas.toString();
	}

	private ManifestoEntregaDocumento getManifestoEntregaDocumentoFromManifesto(ManifestoEntrega manifestoEntrega, DoctoServico doctoServico){
		for (ManifestoEntregaDocumento manifestoEntregaDocumento : manifestoEntrega.getManifestoEntregaDocumentos()) {
			if (manifestoEntregaDocumento.getDoctoServico().equals(doctoServico)){
				return manifestoEntregaDocumento;
			}
		}
		return null;
	}

	private void executeBaixaManifestoAutomatico(ETrackingCTE trackingCte,
			DoctoServico doctoServico, Filial filialEvento,
			OcorrenciaEntrega ocorrenciaEntrega,
			ManifestoEntrega manifestoEntrega,
			String nrRota) {

		if (manifestoEntrega == null){
			manifestoEntrega =  manifestoEntregaService.findManifestoAbertoSubcontratacaoFedex(filialEvento, Short.valueOf(nrRota),
					getDateTimeAtMidnight(getDhOcorrencia(trackingCte))	, doctoServico.getIdDoctoServico());
			if (manifestoEntrega == null){
				//Chama a geração de manifesto automatico do sinergia
				manifestoEntrega = executeGenerateManifestoAutomatico(doctoServico,filialEvento, trackingCte, nrRota);
			}else{
				executeIncluirDocumentoManifestoSubcontratacao(trackingCte,doctoServico, manifestoEntrega,ocorrenciaEntrega);
			}
		}

		if(existeCodOcorrencia(LIST_COD_OCORRENCIA_FDX, ocorrenciaEntrega.getCdOcorrenciaEntrega(), ";") && !isEntregaParcial(trackingCte)) {

			Usuario usuario= SessionUtils.getUsuarioLogado();

			executeBaixaEntregaSemValidacoes(manifestoEntrega, doctoServico, ocorrenciaEntrega, trackingCte, filialEvento, usuario);

		}else {

			manifestoEntregaDocumentoService.executeBaixaDocumento(
					manifestoEntrega.getIdManifestoEntrega(),
					doctoServico.getIdDoctoServico(),
					ocorrenciaEntrega.getCdOcorrenciaEntrega(),
					"N",
					null,
					getDhOcorrencia(trackingCte),
					trackingCte.getNmRecebedor(),
					"observacao",
					true,
					true, null, null, null);

			incluirEventosRastreabilidadeInternacionalService.insereEventos(
					trackingCte.getTpDocumento(),
					doctoServico.getNrDoctoServico().toString(),
					ocorrenciaEntrega.getEvento().getCdEvento(),
					filialEvento.getIdFilial(),
					getDhOcorrencia(trackingCte),
					trackingCte.getObComplemento(),
					null);
		}

		if (isEntregaParcial(trackingCte)){ //9116945
			List listDadosNotas = manifestoEntregaDocumentoService.findDadosNotasEntregaParcial(manifestoEntrega.getIdManifestoEntrega(), doctoServico.getIdDoctoServico());
			ManifestoEntregaDocumento manifestoEntregaDocumento = manifestoEntregaDocumentoService.findLastManifestoEntregaDocumentoByIdDoctoServico(doctoServico.getIdDoctoServico());
			if (manifestoEntregaDocumento == null){
				return;
			}

			for (ETrackingCTENotas eTrackingCTENotas : trackingCte.getListNotas()) {
				Map<String,Object> dadosNota = findDadosNotaEntregaParcial(listDadosNotas,eTrackingCTENotas.getNrNF());
				if (dadosNota !=null){

					Short cdOcorrencia = new Short(eTrackingCTENotas.getCdOcorrencia());
					OcorrenciaEntrega ocorrenciaEntregaDocumento = ocorrenciaEntregaService.findOcorrenciaEntrega(cdOcorrencia);

					NotaFiscalConhecimento notaFiscalConhecimento = new NotaFiscalConhecimento();
					notaFiscalConhecimento.setIdNotaFiscalConhecimento((Long) dadosNota.get("idNotaFiscalConhecimento"));

					UsuarioLMS usuarioIntegracao = new UsuarioLMS();
					usuarioIntegracao.setIdUsuario(5000L);

					EntregaNotaFiscal entregaNotaFiscal = new EntregaNotaFiscal();
					entregaNotaFiscal.setManifesto(manifestoEntrega.getManifesto());
					entregaNotaFiscal.setManifestoEntregaDocumento(manifestoEntregaDocumento);
					entregaNotaFiscal.setNotaFiscalConhecimento(notaFiscalConhecimento);
					entregaNotaFiscal.setOcorrenciaEntrega(ocorrenciaEntregaDocumento);
					entregaNotaFiscal.setDhOcorrencia(eTrackingCTENotas.getDhOcorrencia());
					entregaNotaFiscal.setUsuario(usuarioIntegracao);

					entregaNotaFiscalService.store(entregaNotaFiscal);

					//Inclui na tabela nota fiscal operada caso a ocorrencia NOVA seja de entrega parcial.
					if (CD_OCORRENCIA_ENTREGA_REALIZADA.equals(cdOcorrencia)){
						NotaFiscalOperada notaOperada = new NotaFiscalOperada();
						notaOperada.setDoctoServico(doctoServico);
						notaOperada.setTpSituacao(new DomainValue(TP_SITUACAO_ENTREGUE));
						notaOperada.setNotaFiscalConhecimentoOriginal(notaFiscalConhecimento);
						notaFiscalOperadaService.store(notaOperada);

					}


					finalizaCte(doctoServico, eTrackingCTENotas);
					executeBaixaVolumes(doctoServico, manifestoEntrega,
							eTrackingCTENotas);
				}
			}
		}

	}

	private void executeIncluirDocumentoManifestoSubcontratacao(
			ETrackingCTE trackingCte, DoctoServico doctoServico,
			ManifestoEntrega manifestoEntrega, OcorrenciaEntrega ocorrenciaEntrega) {
		ManifestoEntregaDocumento manifestoEntregaDocumento = new ManifestoEntregaDocumento();
		manifestoEntregaDocumento.setManifestoEntrega(manifestoEntrega);
		manifestoEntregaDocumento.setDoctoServico(doctoServico);
		manifestoEntregaDocumento.setTpSituacaoDocumento(new DomainValue("PBAI"));
		manifestoEntregaDocumento.setUsuario(SessionUtils.getUsuarioLogado());
		manifestoEntregaDocumento.setBlRetencaoComprovanteEnt(trackingCte.getBlRetencaoComprovanteEntrega());
		manifestoEntregaDocumento.setDhInclusao(getDhInclusaoOcorrencia(trackingCte));

		manifestoEntregaDocumentoService.store(manifestoEntregaDocumento);

		PreManifestoDocumento preManifestoDocumento = new PreManifestoDocumento();
		preManifestoDocumento.setDoctoServico(doctoServico);
		preManifestoDocumento.setManifesto(manifestoEntrega.getManifesto());
		preManifestoDocumento.setVersao(0);
		if (CollectionUtils.isNotEmpty(manifestoEntrega.getManifestoEntregaDocumentos())){
			preManifestoDocumento.setNrOrdem(manifestoEntrega.getManifestoEntregaDocumentos().size()+1);
		}
		preManifestoDocumentoService.store(preManifestoDocumento);

		Manifesto manifesto = manifestoEntrega.getManifesto();
		ControleCarga controleCarga = manifesto.getControleCarga();

		List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findByIdConhecimento(doctoServico.getIdDoctoServico());
		List<Long> notasProcessadas = new ArrayList<Long>();

		if (CollectionUtils.isNotEmpty(volumes)){
			for (VolumeNotaFiscal volumeNotaFiscal : volumes) {
				ManifestoEntregaVolume manifestoEntregaVolume = new ManifestoEntregaVolume();
				manifestoEntregaVolume.setDoctoServico(doctoServico);
				manifestoEntregaVolume.setManifestoEntregaDocumento(manifestoEntregaDocumento);
				manifestoEntregaVolume.setManifestoEntrega(manifestoEntrega);
				manifestoEntregaVolume.setVolumeNotaFiscal(volumeNotaFiscal);

				manifestoEntregaVolumeService.store(manifestoEntregaVolume);

				PreManifestoVolume preManifestoVolume = new PreManifestoVolume();
				preManifestoVolume.setPreManifestoDocumento(preManifestoDocumento);
				preManifestoVolume.setDoctoServico(doctoServico);
				preManifestoVolume.setVolumeNotaFiscal(volumeNotaFiscal);
				preManifestoVolume.setVersao(0);
				preManifestoVolume.setManifesto(manifestoEntrega.getManifesto());
				preManifestoVolume.setTpScan(new DomainValue("LM"));

				preManifestoVolumeService.store(preManifestoVolume);

			}
		}


		if (CD_OCORRENCIA_ENTREGA_REALIZADA.equals(ocorrenciaEntrega.getCdOcorrenciaEntrega()) && isEntregaParcial(trackingCte)){
			registrarBaixaEntregaPorNotaFiscalService.executeFinalizacaoCteOriginal(manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(), SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
		}

		controleCargaService.generateAtualizacaoTotaisParaCcColetaEntrega(controleCarga, false, SessionUtils.getFilialSessao());
	}

	private ManifestoEntrega executeGenerateManifestoAutomatico(DoctoServico doctoServico, Filial filialEvento, ETrackingCTE trackingCTE, String nrRota) {

		Moeda real = new Moeda();
        real.setIdMoeda(1l);

        Usuario usuarioIntegracao = new Usuario();
        usuarioIntegracao.setIdUsuario(5000l);

        String cpfMotoristaFedex;
        String placaMeioTransporteFedex;

        cpfMotoristaFedex = (String)conteudoParametroFilialService.findConteudoByNomeParametroWithException(filialEvento.getIdFilial(),
                "MOTORISTA_FILIAL_FDX", false);
        placaMeioTransporteFedex= (String)conteudoParametroFilialService.findConteudoByNomeParametroWithException(filialEvento.getIdFilial(),
                "PLACA_VEICULO_FDX", false);

        Motorista motoristaFedex = null;
        Pessoa pessoaMotorista = pessoaService.findByNrIdentificacao(cpfMotoristaFedex);
        if (pessoaMotorista != null){
            motoristaFedex = motoristaService.findById(pessoaMotorista.getIdPessoa());
        }

        if (motoristaFedex == null){
        	throw new BusinessException("LMS-09149", new Object[]{cpfMotoristaFedex, filialEvento.getSgFilial()});
        }

        MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacao(placaMeioTransporteFedex);
        if (meioTransporte == null){
        	throw new BusinessException("LMS-09150", new Object[]{placaMeioTransporteFedex, filialEvento.getSgFilial()});

        }

        MeioTransporteRodoviario meioTransporteRodoviario = meioTransporteRodoviarioService.findById(meioTransporte.getIdMeioTransporte());
        Proprietario proprietarioFedex = meioTranspProprietarioService.findProprietarioByIdMeioTransporte(meioTransporte.getIdMeioTransporte(), JTDateTimeUtils.getDataAtual());
        if (proprietarioFedex == null){
        	throw new BusinessException("LMS-09151", new Object[]{placaMeioTransporteFedex});
        }



        RotaColetaEntrega rota = rotaColetaEntregaService.findRotaColetaEntrega(filialEvento.getIdFilial(), Short.valueOf(nrRota));
        if (rota == null){
        	throw new BusinessException("LMS-09152", new Object[]{nrRota});
        }

        ControleCarga cc = buildControleCArga(trackingCTE, real, filialEvento, meioTransporte, proprietarioFedex,
                motoristaFedex, rota);

        VeiculoControleCarga veiculoControleCarga = new VeiculoControleCarga();
        veiculoControleCarga.setControleCarga(cc);
        veiculoControleCarga.setMeioTransporte(meioTransporte);

        MotoristaControleCarga motoristaControleCarga = new MotoristaControleCarga();
        motoristaControleCarga.setMotorista(motoristaFedex);
        motoristaControleCarga.setControleCarga(cc);

        ControleQuilometragem quilometragem = new ControleQuilometragem();
        quilometragem.setFilial(filialEvento);
        quilometragem.setMeioTransporteRodoviario(meioTransporteRodoviario);

        quilometragem.setDhMedicao(getDhOcorrencia(trackingCTE));
        quilometragem.setNrQuilometragem(0);
        quilometragem.setBlVirouHodometro(Boolean.FALSE);
        quilometragem.setBlSaida(Boolean.TRUE);
        quilometragem.setUsuarioByIdUsuario(usuarioIntegracao);
        quilometragem.setControleCarga(cc);

        Manifesto manifesto = buildManifesto(trackingCTE, real, filialEvento);
        totalizaAtributosDocumentosManifesto(manifesto, doctoServico);

        manifesto.setControleCarga(cc);
        manifesto.setPreManifestoDocumentos(new ArrayList<PreManifestoDocumento>());
        manifesto.setPreManifestoVolumes(new ArrayList<PreManifestoVolume>());

        ManifestoEntrega manifestoEntrega = new ManifestoEntrega();
        manifestoEntrega.setManifesto(manifesto);
        manifestoEntrega.setFilial(filialEvento);
        manifestoEntrega.setDhEmissao(getDateTimeAtMidnight(getDhOcorrencia(trackingCTE)));
        manifestoEntrega.setObManifestoEntrega(configuracoesFacade.getMensagem("observacaoManifestoAutomatico"));
        manifestoEntrega.setManifestoEntregaDocumentos(new ArrayList<ManifestoEntregaDocumento>());
        manifestoEntrega.setManifestoEntregaVolumes(new ArrayList<ManifestoEntregaVolume>());
        manifesto.setManifestoEntrega(manifestoEntrega);

        ManifestoEntregaDocumento manifestoEntregaDocumento = new ManifestoEntregaDocumento();
        manifestoEntregaDocumento.setManifestoEntrega(manifestoEntrega);
        manifestoEntregaDocumento.setDoctoServico(doctoServico);
        manifestoEntregaDocumento.setTpSituacaoDocumento(new DomainValue("PBAI"));
        manifestoEntregaDocumento.setUsuario(usuarioIntegracao);
        manifestoEntregaDocumento.setDhInclusao(getDhOcorrencia(trackingCTE));



        manifestoEntregaDocumento.setBlRetencaoComprovanteEnt(Boolean.FALSE);
        manifestoEntrega.getManifestoEntregaDocumentos().add(manifestoEntregaDocumento);

        PreManifestoDocumento preManifestoDocumento = new PreManifestoDocumento();
        preManifestoDocumento.setManifesto(manifesto);
        preManifestoDocumento.setDoctoServico(doctoServico);
        preManifestoDocumento.setNrOrdem(1);
        preManifestoDocumento.setVersao(0);
        manifesto.getPreManifestoDocumentos().add(preManifestoDocumento);

        List<VolumeNotaFiscal> volumesConhecimento = volumeNotaFiscalService.findfindByIdConhecimento(doctoServico.getIdDoctoServico());
        for (VolumeNotaFiscal volumeNotaFiscal: volumesConhecimento){
            ManifestoEntregaVolume manifestoEntregaVolume = new ManifestoEntregaVolume();
            manifestoEntregaVolume.setManifestoEntrega(manifestoEntrega);
            manifestoEntregaVolume.setDoctoServico(doctoServico);
            manifestoEntregaVolume.setVolumeNotaFiscal(volumeNotaFiscal);
            manifestoEntregaVolume.setManifestoEntregaDocumento(manifestoEntregaDocumento);
            manifestoEntrega.getManifestoEntregaVolumes().add(manifestoEntregaVolume);

            PreManifestoVolume preManifestoVolume = new PreManifestoVolume();
            preManifestoVolume.setManifesto(manifesto);
            preManifestoVolume.setDoctoServico(doctoServico);
            preManifestoVolume.setPreManifestoDocumento(preManifestoDocumento);
            preManifestoVolume.setVolumeNotaFiscal(volumeNotaFiscal);

            preManifestoVolume.setVersao(0);
            manifesto.getPreManifestoVolumes().add(preManifestoVolume);
        }


        cc.setManifestos(new ArrayList<Manifesto>());
        cc.getManifestos().add(manifesto);

        controleCargaService.generateControleCargaManifestoAutomatico(cc,veiculoControleCarga,motoristaControleCarga,quilometragem, usuarioIntegracao, false);

		return manifestoEntrega;

	}

	private ControleCarga buildControleCArga(ETrackingCTE trackingCTE,
			Moeda real, Filial filialEvento, MeioTransporte meioTransporte,
			Proprietario proprietarioFedex, Motorista motoristaFedex,
			RotaColetaEntrega rota) {

		ControleCarga cc = new ControleCarga();
        cc.setIdControleCarga(null);
        cc.setFilialByIdFilialOrigem(filialEvento);
        cc.setFilialByIdFilialAtualizaStatus(filialEvento);
        cc.setProprietario(proprietarioFedex);
        cc.setMotorista(motoristaFedex);
        cc.setTpControleCarga(new DomainValue("C"));
        cc.setTpStatusControleCarga(new DomainValue("TC"));
        cc.setFilialByIdFilialDestino(filialEvento);
        cc.setMoeda(real);
        cc.setDhSaidaColetaEntrega(getDateTimeAtMidnight(getDhOcorrencia(trackingCTE)));
        cc.setDhGeracao(getDateTimeAtMidnight(getDhOcorrencia(trackingCTE)));
        cc.setMeioTransporteByIdTransportado(meioTransporte);
        cc.setBlEntregaDireta(Boolean.FALSE);
        cc.setRotaColetaEntrega(rota);

		return cc;
	}

	private void executeAtualizacaoOcorrenciaManifestoEntrega(
			ManifestoEntregaDocumento manifestoEntregaDocumento,
			OcorrenciaEntrega ocorrenciaEntrega, Filial filialEvento, ETrackingCTE trackingCTE) {

		OcorrenciaEntrega ocorrenciaEntregaAntiga = manifestoEntregaDocumento.getOcorrenciaEntrega();

		if (ocorrenciaEntregaAntiga != null && !ocorrenciaEntregaAntiga.getIdOcorrenciaEntrega().equals(ocorrenciaEntrega.getIdOcorrenciaEntrega())){
			TypedFlatMap parametros = new TypedFlatMap();
			parametros.put("idOcorrenciaEntrega", ocorrenciaEntregaAntiga.getIdOcorrenciaEntrega());
			parametros.put("manifestoEntregaDocumento.idManifestoEntregaDocumento", manifestoEntregaDocumento.getIdManifestoEntregaDocumento());
			parametros.put("obManifestoEntregaDocumento", manifestoEntregaDocumento.getObManifestoEntregaDocumento());
			parametros.put("dhBaixa", manifestoEntregaDocumento.getDhBaixa());

			parametros.put("ocorrenciaEntrega.cdOcorrenciaEntrega", ocorrenciaEntrega.getCdOcorrenciaEntrega());
			parametros.put("ocorrenciaEntrega.idOcorrenciaEntrega", ocorrenciaEntrega.getIdOcorrenciaEntrega());
			parametros.put("idDoctoServico", manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico());
			parametros.put("filial.idFilial", filialEvento.getIdFilial());
			parametros.put("obMotivoAlteracao", trackingCTE.getObComplemento());
			parametros.put("nmRecebedor2", trackingCTE.getNmRecebedor());
			parametros.put("dhCancelamentoOcorrenciaArquivo", getDhInclusaoOcorrencia(trackingCTE));
			parametros.put("complementoBaixaSPP", new DomainValue("44"));
			parametros.put("tipoEntregaParcial", new DomainValue());
			parametros.put("doctoServico.tpDocumentoServico", manifestoEntregaDocumento.getDoctoServico().getTpDocumentoServico().getValue());

			cancelarEntregaAlterarOcorrenciaService.executeCancelarOcorrencia(parametros);
		}


	}

	private String findCdOcorrencia(ETrackingCTE trackingCTE){
		if (trackingCTE.getListNotas() != null && !trackingCTE.getListNotas().isEmpty()){
			return trackingCTE.getListNotas().get(0).getCdOcorrencia();
		}
		return null;
	}


	private boolean hasOcorrenciaPendencia(ETrackingCTE trackingCTE){
		if (trackingCTE.getListNotas() != null && !trackingCTE.getListNotas().isEmpty()){
			return trackingCTE.getListNotas().get(0).getBlOcorrenciaPendencia();
		}
		return false;
	}


	private DateTime getDhInclusaoOcorrencia(ETrackingCTE trackingCTE){
		if (trackingCTE.getListNotas() != null && !trackingCTE.getListNotas().isEmpty()){
			return trackingCTE.getListNotas().get(0).getDhInclusaoOcorrencia();
		}
		return null;
	}

	private DateTime getDhOcorrencia(ETrackingCTE trackingCTE){
		if (trackingCTE.getListNotas() != null && !trackingCTE.getListNotas().isEmpty()){
			return trackingCTE.getListNotas().get(0).getDhOcorrencia();
		}

		return null;
	}

	private DateTime getDateTimeAtMidnight(DateTime dateTime){
		return dateTime.toYearMonthDay().toDateTimeAtMidnight();

	}


	private Manifesto buildManifesto(ETrackingCTE trackingCTE, Moeda real, Filial filialEvento) {
        Manifesto manifesto = new Manifesto();

        manifesto.setFilialByIdFilialOrigem(filialEvento);
        manifesto.setFilialByIdFilialDestino(filialEvento);
        manifesto.setDhGeracaoPreManifesto(getDateTimeAtMidnight(getDhOcorrencia(trackingCTE)));
        manifesto.setDhEmissaoManifesto(getDateTimeAtMidnight(getDhOcorrencia(trackingCTE)));
        manifesto.setTpManifesto(new DomainValue("E"));
        manifesto.setMoeda(real);
        manifesto.setTpModal(new DomainValue("R"));
        manifesto.setTpManifestoEntrega(new DomainValue("EP"));
        manifesto.setTpAbrangencia(new DomainValue("N"));
        manifesto.setTpStatusManifesto(new DomainValue("TC"));
        manifesto.setVersao(8);
        manifesto.setBlBloqueado(Boolean.FALSE);
        return manifesto;
    }

    private void totalizaAtributosDocumentosManifesto(Manifesto manifesto, DoctoServico doctoServico) {

        manifesto.setVlTotalManifesto(doctoServico.getVlMercadoria());
        manifesto.setPsTotalManifesto(doctoServico.getPsReal());
        manifesto.setPsTotalAforadoManifesto(doctoServico.getPsAforado());
        manifesto.setVlTotalManifestoEmissao(doctoServico.getVlMercadoria());
        manifesto.setPsTotalManifestoEmissao(doctoServico.getPsReal());
        manifesto.setVlTotalFreteEmissao(doctoServico.getVlTotalDocServico());
        manifesto.setQtTotalVolumesEmissao(doctoServico.getQtVolumes());

    }


	private void generateConhecimentoFedex(DoctoServico doctoServico,
			ETrackingCTE trackingCte) {

		ConhecimentoFedex conhecimentoFedex = new ConhecimentoFedex();

		conhecimentoFedex.setDoctoServico(doctoServico);
		conhecimentoFedex.setSiglaFilialOrigem(trackingCte.getSgFilialEmitenteSubcontratacao());
		conhecimentoFedex.setTipoDocumento(trackingCte.getTpDocumento());
		conhecimentoFedex.setNumeroConhecimento(trackingCte.getNrDocumentoSubcontratacao());

		conhecimentoFedex.setDataEmissao(trackingCte.getDhEmissaoDocumentoSubcontratacao().toDate());

		conhecimentoFedexService.store(conhecimentoFedex);

	}

	private void setDadosSession(Filial filial) {
        Pais brasil = new Pais();
        brasil.setIdPais(30L);

        Usuario usuarioIntegracao = usuarioService.findUsuarioByLogin(LOGIN_USUARIO_INTEGRACAO);

        volDadosSessaoService.executeDadosSessaoBanco(usuarioIntegracao, filial, brasil );
    }


	private boolean validateAtualizaLocalizacao(DoctoServico doctoServico, ETrackingCTE trackingCte){
		List<EventoDocumentoServico> eventosPosteriores = eventoDocumentoServicoService.findMaiorEvento(doctoServico, trackingCte.getDhEvento());
		return eventosPosteriores == null || eventosPosteriores.isEmpty();
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
		this.volDadosSessaoService = volDadosSessaoService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setConhecimentoFedexService(
			ConhecimentoFedexService conhecimentoFedexService) {
		this.conhecimentoFedexService = conhecimentoFedexService;
	}

	public void setOcorrenciaEntregaService(
			OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public void setManifestoEntregaDocumentoService(
			ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setManifestoEntregaVolumeService(
			ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
		this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setManifestoEntregaService(
			ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}

	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setMeioTransporteRodoviarioService(
			MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}

	public void setMeioTranspProprietarioService(
			MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}

	public void setOcorrenciaPendenciaService(
			OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	public void setCancelarEntregaAlterarOcorrenciaService(
			CancelarEntregaAlterarOcorrenciaService cancelarEntregaAlterarOcorrenciaService) {
		this.cancelarEntregaAlterarOcorrenciaService = cancelarEntregaAlterarOcorrenciaService;
	}

	public void setPreManifestoDocumentoService(
			PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}

	public void setPreManifestoVolumeService(
			PreManifestoVolumeService preManifestoVolumeService) {
		this.preManifestoVolumeService = preManifestoVolumeService;
	}

	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}

	public void setEntregaNotaFiscalService(
			EntregaNotaFiscalService entregaNotaFiscalService) {
		this.entregaNotaFiscalService = entregaNotaFiscalService;
	}

	public void setNotaFiscalOperadaService(
			NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public void setRegistrarBaixaEntregaPorNotaFiscalService(
			RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService) {
		this.registrarBaixaEntregaPorNotaFiscalService = registrarBaixaEntregaPorNotaFiscalService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public void setRegistrarBaixaEntregasService(
			RegistrarBaixaEntregasService registrarBaixaEntregasService) {
		this.registrarBaixaEntregasService = registrarBaixaEntregasService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
}
