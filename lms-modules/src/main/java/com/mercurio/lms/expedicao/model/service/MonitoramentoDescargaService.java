package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.*;

import com.mercurio.lms.expedicao.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.dao.MonitoramentoDescargaDAO;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.gm.model.service.EmbarqueService;
import com.mercurio.lms.gm.model.service.VolumeService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rnc.model.NotaOcorrenciaNc;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.expedicao.monitoramentoDescargaService"
 */
public class MonitoramentoDescargaService extends CrudService<MonitoramentoDescarga, Long> {

	private static final Short CD_LOCALIZACAO_MERCADORIA = 24;
	private static final String TP_SITUACAO_DESCARGA = "tpSituacaoDescarga";
	private static final String NR_FROTA = "nrFrota";
	private static final String MEIO_TRANSPORTE_ID_MEIO_TRANSPORTE = "meioTransporte.idMeioTransporte";
	private static final String FILIAL_ID_FILIAL = "filial.idFilial";
	private static final String ID_MONITORAMENTO_DESCARGA = "idMonitoramentoDescarga";
	private static final String ID_DOCTO_SERVICO = "idDoctoServico";
	private static final String NR_CONHECIMENTO = "nrConhecimento";
	private static final String DT_PREVISTA_ENTREGA = "dtPrevistaEntrega";
	private static final String NR_DIAS_PREV_ENTREGA = "nrDiasPrevEntrega";
	private static final String TP_DOCTO_SERVICO = "tpDoctoServico";
	private static final String ID_FILIAL_ORIGEM = "idFilialOrigem";
	private static final String SG_RED_FILIAL_ORIGEM = "sgRedFilialOrigem";
	private static final String FECHA_MONITORAMENTO = "fechaMonitoramento";
	private static final String CTRC_LIST = "ctrcList";
	private static final String ORIGEM = "origem";
	private static final String TIPO_EMITENTE = "tipoEmitente";
	private static final String DATA_FECHAMENTO = "dataFechamento";
	private static final String EMAIL_ENDERECO = "emailEndereco";
	private static final String EMAIL_CC = "emailCC";
	private static final String EMAIL_TITULO = "emailTitulo";
	private static final String EMAIL_CORPO = "emailCorpo";
	private static final String EMAIL_IDS = "ids";
	private static final String RELATORIO_DISCREPANCIA_SERVICE = "relatorioDiscrepanciaService";
	private static final String ID_CARREGAMENTO_GM = "idCarregamentoGM";
	private static final String MANIFESTOS_GM = "manifestosGM";
	private static final String NR_PRAZO = "nrPrazo";
	private static final String DT_PRAZO_ENTREGA = "dtPrazoEntrega";
	private static final String ID_FILIAL = "idFilial";
	private static final String GENERATE_UNIQUE_NUMBER = "generateUniqueNumber";
	private static final String BL_CONTINGENCIA = "blContingencia";
	private static final String TP_SITUACAO_MONITORAMENTO = "tpSituacaoMonitoramento";
	private static final String CONHECIMENTO_EDI_LIST = "list";
	private static final int TAMANHO_MAXIMO_LINHA = 970;
	private static final String TP_RNC_AUTO_ENCERRAR = "TP_RNC_AUTO_ENCERRAR";

	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ParametroGeralService parametroGeralService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private CalcularFreteService calcularFreteService;
	private ConhecimentoService conhecimentoService;
	private FilialService filialService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private VolumeService volumeService;
	private EmbarqueService embarqueService;
	private ConhecimentoCancelarService conhecimentoCancelarService;
	private WorkflowPendenciaService workflowPendenciaService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private DoctoServicoWorkflowService doctoServicoWorkflowService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private DoctoServicoService doctoServicoService;
	private DpeService dpeService;
	private CtoAwbService ctoAwbService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private DoctoServicoPPEPadraoService doctoServicoPPEPadraoService;

	private static final Log LOGGER = LogFactory.getLog(MonitoramentoDescargaService.class);


	//LMS-2246
	//Esta rotina tem por objetivo fechar os volumes corretos,
	//calcular o frete quando todos os volumes de um pré-conhecimento estiverem checados,
	//quando os volumes foram pesados por sorter
	public void fechaCtrcSorter(Conhecimento c) {

		if (c.getTpSituacaoConhecimento() != null && !"P".equals(c.getTpSituacaoConhecimento().getValue())) {
			return;
		}

		Boolean geraRegistroRNC = false;
		Integer countRegistro = 0;
		String montaLinha = "Volume: ";
		Long nrConhecimentoVolume = null;
		List listaNotaOcorrenciaNcsv = new ArrayList<NotaOcorrenciaNc>();
		
		for (NotaFiscalConhecimento nfc : c.getNotaFiscalConhecimentos()) {
		    Boolean geraNotaOcorrenciaNc = false;
			for (VolumeNotaFiscal vnf : (List<VolumeNotaFiscal>) nfc.getVolumeNotaFiscais()) {
				if ((vnf.getTpVolume().equals(ConstantesExpedicao.TP_VOLUME_UNITARIO) || vnf.getTpVolume().equals(ConstantesExpedicao.TP_VOLUME_MESTRE)) && vnf.getPsAferido() == null) {
					geraRegistroRNC = true;
					geraNotaOcorrenciaNc = true;
					
					vnf.setPsAferido(BigDecimal.ZERO);
					vnf.setNrDimensao1Cm(0);
					vnf.setNrDimensao2Cm(0);
					vnf.setNrDimensao3Cm(0);
					vnf.setTpOrigemDimensoes(new DomainValue("A"));
					vnf.setTpOrigemPeso(new DomainValue("A"));

					carregarNrConhecimento(nfc, vnf);

					//Até aqui o nrConhecimento da tabela Conhecimento é negativo
					//Neste momento ele recebe o nrConhecimento de seus volumes e passa por parâmetro no fechaEmiteCtrc
					nrConhecimentoVolume = vnf.getNrConhecimento();

					//Conta o número de registros que tem o psAferido=null
					countRegistro++;

					//Deixa os valores com 4 dígitos, colocando zeros a esquerda
					String nrSequencia = String.format("%04d", vnf.getNrSequencia());
					String qtVolumes = String.format("%04d", c.getQtVolumes());

					//Monta linha, exemplo.: Volume: nrSequencia/qtVolumes... (para cada volume do conhecimento)
					montaLinha = montaLinha(montaLinha, nrSequencia, qtVolumes);
				}
			}
			if(geraNotaOcorrenciaNc){
                NotaOcorrenciaNc notaOcorrenciaNc = new NotaOcorrenciaNc();
                notaOcorrenciaNc.setNrNotaFiscal(nfc.getNrNotaFiscal());
                notaOcorrenciaNc.setNotaFiscalConhecimento(nfc);
                listaNotaOcorrenciaNcsv.add(notaOcorrenciaNc); 
            }
		}

		if (geraRegistroRNC) {
			geraRegistroRNC(c, countRegistro, montaLinha, nrConhecimentoVolume, listaNotaOcorrenciaNcsv);
		}
	}

	private void geraRegistroRNC(Conhecimento c, Integer countRegistro, String montaLinha, Long nrConhecimentoVolume,List listaNotaOcorrenciaNcsv) {
		Map<String, Object> dadosCtrc = new HashMap<String, Object>();
		dadosCtrc.put(ID_DOCTO_SERVICO, c.getIdDoctoServico());
		dadosCtrc.put(NR_CONHECIMENTO, nrConhecimentoVolume);
		dadosCtrc.put(DT_PREVISTA_ENTREGA, c.getDtPrevEntrega());
		dadosCtrc.put(NR_DIAS_PREV_ENTREGA, c.getNrDiasPrevEntrega());
		dadosCtrc.put(TP_DOCTO_SERVICO, c.getTpDoctoServico());
		calcularFreteService.fechaEmiteCTRC(dadosCtrc);

		c = (Conhecimento) dadosCtrc.get("conhecimento");

		if (validateCancelamentoDocumento(c)) {

			ParametroGeral paramChavesRncAutomatica = parametroGeralService.findByNomeParametro("CHAVES_RNC_AUTOMATICA", Boolean.FALSE);
			ParametroGeral paramMotRncSorter = parametroGeralService.findByNomeParametro("MOT_RNC_SORTER", Boolean.FALSE);

			//Separa o dsConteudo do parâmetro 'CHAVES_RNC_AUTOMARICA'
			Long primeiroCampo = Long.parseLong(paramChavesRncAutomatica.getDsConteudo().split(";")[0]);
			Long segundoCampo = Long.parseLong(paramChavesRncAutomatica.getDsConteudo().split(";")[1]);
			Long terceiroCampo = Long.parseLong(paramChavesRncAutomatica.getDsConteudo().split(";")[2]);
			Long quartoCampo = Long.parseLong(paramChavesRncAutomatica.getDsConteudo().split(";")[3]);

			String campoMotRncSorter = paramMotRncSorter.getDsConteudo();

			//Converte os valores para BigDecimal e calcula o vlOcorrenciaNC
			BigDecimal qtVolume = BigDecimal.valueOf(c.getQtVolumes());
			BigDecimal counter = BigDecimal.valueOf(countRegistro);
			BigDecimal vlOcorrenciaNC = (c.getVlMercadoria().multiply(counter)).divide(qtVolume, 2, BigDecimal.ROUND_HALF_UP);

			String modal = "R";

			List<Awb> awbs = ctoAwbService.findCtoAwbBydDoctoServico(c.getIdDoctoServico());
			if (!awbs.isEmpty()) {
				modal = "A";
			}

			//Gera um RNC
			ocorrenciaNaoConformidadeService.storeRNC(
					null, //idManifesto
					c.getIdDoctoServico(), //idDoctoServico
					segundoCampo, //idMotivoAberturaNC
					null, //idControleCarga
					null, //idEmpresa
					primeiroCampo, //idDescricaoPadraoNc
					c.getFilialByIdFilialOrigem().getIdFilial(), //idFilialByFilialResponsavel
					terceiroCampo, //idCausaNaoConformidade
					quartoCampo, //idMoeda
					montaLinha, //dsOcorrenciaNc
					false, //blCaixaReaproveitada
					null, //dsCaixaReaproveitada
					null, //dsCausaNc
					vlOcorrenciaNC, //vlOcorrenciaNC
					countRegistro, //qtVolumes
					c.getClienteByIdClienteDestinatario().getIdCliente(), //idClienteDestinatarioNc
					c.getClienteByIdClienteRemetente().getIdCliente(), //idClienteRemetenteNc
					listaNotaOcorrenciaNcsv, //listaNotaOcorrenciaNcs
					campoMotRncSorter, //dsMotivoAbertura
					null, // ItemsNFe
					null, //nrAwb
					modal, //modal
					null,
					null,
					null);
		}
	}

	private void carregarNrConhecimento(NotaFiscalConhecimento nfc, VolumeNotaFiscal vnf) {
		if (vnf.getNrConhecimento() == null) {
			for (VolumeNotaFiscal vnf2 : (List<VolumeNotaFiscal>) nfc.getVolumeNotaFiscais()) {
				if (vnf2.getNrConhecimento() != null) {
					vnf.setNrConhecimento(vnf2.getNrConhecimento());
				}
			}
		}
	}

	private String montaLinha(String montaLinha, String nrSequencia, String qtVolumes) {

		String montaLinahAux = montaLinha;

		if (!montaLinahAux.contains("...")) {
			if (montaLinahAux.length() < TAMANHO_MAXIMO_LINHA) {
				montaLinahAux = montaLinahAux.concat(nrSequencia).concat("/").concat(qtVolumes).concat(", ");
			} else {
				montaLinahAux = montaLinahAux.concat("...");
			}
		}
		return montaLinahAux;
	}

	private Boolean validateCancelamentoDocumento(Conhecimento conhecimento) {
		Boolean valido = Boolean.TRUE;

		// Verifica se o documento foi cancelado por regras anteriores.
		List<EventoDocumentoServico> eventosCancelamento = eventoDocumentoServicoService.findEventoDoctoServico(conhecimento.getIdDoctoServico(),
				ConstantesSim.EVENTO_DOCUMENTO_CANCELADO);

		if (eventosCancelamento != null && !eventosCancelamento.isEmpty()) {
			valido = Boolean.FALSE;
		} else {
			valido = conhecimentoService.executeCancelamentoPorBloqueioValores(conhecimento, false);
		}
		return valido;
	}

	public Map<String, Object> updateSituacaoMonitoramentoDescarga(Long idMonitoramentoDescarga, String tpSituacaoDescarga) {
		MonitoramentoDescarga monitoramentoDescarga = findById(idMonitoramentoDescarga);
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (monitoramentoDescarga != null) {
			monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(tpSituacaoDescarga));
			store(monitoramentoDescarga);
		}
		return parameters;
	}

	public Map<String, Object> updateSituacaoMonitoramento(Long idMonitoramentoDescarga, Boolean validarCalculoFreteFinalizado, Boolean confirmaMensagem, Boolean existePendenciaWorkflow) {
		MonitoramentoDescarga monitoramentoDescarga = findById(idMonitoramentoDescarga);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("lmsImplantadoFilial", filialService.lmsImplantadoFilial(SessionUtils.getFilialSessao()));
		if (monitoramentoDescarga != null) {
			if (validarCalculoFreteFinalizado != null && validarCalculoFreteFinalizado) {
				Long qtdadeVolumesEmCalculoFrete = getVolumeNotaFiscalService().countVolumesDeCTRCsEmCalculoFretePorMonitoramentoDescarga(idMonitoramentoDescarga);
				if (qtdadeVolumesEmCalculoFrete != null && qtdadeVolumesEmCalculoFrete > 0) {
					throw new BusinessException("LMS-04290", new Object[]{qtdadeVolumesEmCalculoFrete});
				}
			}

			parameters.putAll(this.executeValidateVolumeGMFinalizarDescarga(monitoramentoDescarga));

			if (existePendenciaWorkflow != null && existePendenciaWorkflow
					&& ConstantesExpedicao.TP_SITUACAO_DESCARGA_TODOS_VOLUMES_AFERIDOS.equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())) {
				monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA_ERRO));
				monitoramentoDescarga.setDhFimDescarga(JTDateTimeUtils.getDataHoraAtual());

				inutilizarDocumentoNaoAprovado(monitoramentoDescarga, "E");
				fecharDocumentosFilialSorter(monitoramentoDescarga);

			} else if (ConstantesExpedicao.TP_SITUACAO_DESCARGA_INICIADA.equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())) {
				monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA_ERRO));
				monitoramentoDescarga.setDhFimDescarga(JTDateTimeUtils.getDataHoraAtual());

				inutilizarDocumentoNaoAprovado(monitoramentoDescarga, "E");

				//Inicio LMS-2246
				if (confirmaMensagem != null && confirmaMensagem) {
					fecharDocumentosFilialSorter(monitoramentoDescarga);
				}

			} else if (ConstantesExpedicao.TP_SITUACAO_DESCARGA_TODOS_VOLUMES_AFERIDOS.equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())) {
				monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA));
				monitoramentoDescarga.setDhFimDescarga(JTDateTimeUtils.getDataHoraAtual());

				inutilizarDocumentoNaoAprovado(monitoramentoDescarga, "R");
			}

			store(monitoramentoDescarga);

		}
		return parameters;
	}

	/**
	 * @param monitoramentoDescarga
	 */
	private void fecharDocumentosFilialSorter(MonitoramentoDescarga monitoramentoDescarga) {
		if (SessionUtils.getFilialSessao().getBlSorter()) {
			// Busca conhecimento que pertencem ao monitoramento,
			// onde:
			// NFC.ID_CONHECIMENTO = C.ID_CONHECIMENTO
			// VNF.ID_NOTA_FISCAL_CONHECIMENTO =
			// NFC.D_NOTA_FISCAL_CONHECIMENTO
			// MD.ID_MONITORAMENTO_DESCARGA =
			// VNF.ID_MONITORAMENTO_DESCARGA
			// C.BL_PESO_AFERIDO != "S")
			// e que pelo menos um de seus volumes tenha o campo
			// VNF.NR_CONHECIMENTO preenchido
			List<Conhecimento> listConhecimento = this.getMonitoramentoDescargaDAO().findConhecimentoPertenceAoMonitoramento(monitoramentoDescarga);

			if (listConhecimento != null) {
				for (Conhecimento c : listConhecimento) {
					// Esta rotina tem por objetivo fechar os
					// volumes corretos, calcular o frete quando
					// os volumes de um pré-conhecimento estiverem
					// checados quando os volumes foram pesados por
					// sorter.
					fechaCtrcSorter(c);
				}
			}
		}
	}

	/**
	 * LMS-3281: Esta rotina tem por objetivo gerar a inutilização do documento
	 * de serviço que não teve o workflow aprovado (workflow reprovado ou que
	 * estava em aprovação e por opção do usuário na finalização da descarga foi
	 * reprovado).
	 *
	 * @param monitoramentoDescarga
	 * @param tpSituacaoAprovacao
	 */
	public void inutilizarDocumentoNaoAprovado(MonitoramentoDescarga monitoramentoDescarga, String tpSituacaoAprovacao) {
		Set<Long> idDoctosCancelados = new HashSet<Long>();

		List<DoctoServicoWorkflow> doctosServicoWorkflow = doctoServicoWorkflowService.findByMonitoramentoAndSituacao(monitoramentoDescarga.getIdMonitoramentoDescarga(), tpSituacaoAprovacao);

		for (DoctoServicoWorkflow doctoServicoWorkflow : doctosServicoWorkflow) {

			/*
             * Gerar evento de inutilização do documento (observar a
			 * regra de gerar apenas um evento por documento -
			 * existe a possibilidade de ter até 3 workflow
			 * diferentes para o mesmo documento).
			 */
			if (!idDoctosCancelados.contains(doctoServicoWorkflow.getDoctoServico().getIdDoctoServico())) {
				String dsObservacao = null;
				if (doctoServicoWorkflow.getPendencia() != null) {
					dsObservacao = doctoServicoWorkflow.getPendencia().getDsPendencia();
				}

				conhecimentoCancelarService.storeEventoCancelamentoBloqueioValores(doctoServicoWorkflow.getDoctoServico(), dsObservacao);
				idDoctosCancelados.add(doctoServicoWorkflow.getDoctoServico().getIdDoctoServico());
			}

			if (doctoServicoWorkflow.getTpSituacaoAprovacao() != null && doctoServicoWorkflow.getPendencia() != null
					&& "E".equals(doctoServicoWorkflow.getTpSituacaoAprovacao().getValue())) {
				workflowPendenciaService.cancelPendencia(doctoServicoWorkflow.getPendencia());
			}
		}
	}

	public void updateMonitoramentoDescargaAposPesagemVolume(final Long idVolumeNotaFiscal, final Boolean blEstacaoAutomatizada, final Boolean isVolumeGMDireto) {
		/** Busca Monitoramento do Volume Nota Fiscal */
		final MonitoramentoDescarga monitoramentoDescarga = findMonitoramentoDescargaByVolumeNotaFiscal(idVolumeNotaFiscal);
		if (monitoramentoDescarga.getTpSituacaoDescarga() != null
				&& (ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA.equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())
				|| ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA_ERRO.equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue()))) {
			/** Atualiza status na primeira pesagem */
			monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_INICIADA));
			monitoramentoDescarga.setDhInicioDescarga(JTDateTimeUtils.getDataHoraAtual());
			monitoramentoDescarga.setDhUltimaAfericao(JTDateTimeUtils.getDataHoraAtual());
			store(monitoramentoDescarga);
		}

		//LMS-3054
		if (ConstantesExpedicao.TP_SITUACAO_DESCARGA_EMISSAO_CONHECIMENTO_REALIZADA.equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())
				|| ConstantesExpedicao.TP_SITUACAO_DESCARGA_EMISSAO_CONHECIMENTO_INICIADA.equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())) {
			monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_INICIADA));
		}

        /* Verifica se NÃO é Estação Automatizada */
		if (BooleanUtils.isFalse(blEstacaoAutomatizada)) {
            /* Busca total de volumes pendentes */
			final Long qtVolumesNaoAferidos = volumeNotaFiscalService.findTotalVolumesNaoAferidos(monitoramentoDescarga.getIdMonitoramentoDescarga(), isVolumeGMDireto);
			if (qtVolumesNaoAferidos != null && qtVolumesNaoAferidos.compareTo(0L) <= 0) {
                /* Busca total de volumes Aferidos, somando quantidade de volumes de cada volume_nota_fiscal */
				final List listVolumes = getVolumeNotaFiscalService().findQtVolumesNotaFiscalByIdMonitoramentoDescarga(monitoramentoDescarga.getIdMonitoramentoDescarga());
				if (!CollectionUtils.isEmpty(listVolumes)) {
					Map volumes = (Map) listVolumes.get(0);
					Long qtVolumesTotal = (Long) volumes.get("qtVolumesTotal");
					Long qtVolumes = (Long) volumes.get("qtVolumes");
					/** Verifica se todos volumes foram Aferidos */
					if (qtVolumesTotal.compareTo(qtVolumes) <= 0) {
						monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_TODOS_VOLUMES_AFERIDOS));
						monitoramentoDescarga.setDhUltimaAfericao(JTDateTimeUtils.getDataHoraAtual());
						store(monitoramentoDescarga);
					}
				}
			}
		}
		getDao().flush();

	}

	/**
	 * Fecha os Conhecimentos do Carregamento
	 *
	 * @param idMonitoramentoDescarga
	 * @author André Valadas
	 */
	public Map updateFinalizarVeiculo(final Long idMonitoramentoDescarga) {
		String tpSituacaoMonitoramento = "D";
		Map parameters = new HashedMap();

		/** Controle de Transações para "liberar esteira" */
		@SuppressWarnings("deprecation")
		final Session newSession = getDao().getAdsmHibernateTemplate().getSessionFactory().openSession(getDao().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection());
		Transaction transaction = newSession.beginTransaction();

		final List<Conhecimento> evictCache = new ArrayList<Conhecimento>();

		/** Verifica se Carregamento GM Direto */
		final Carregamento carregamentoGmDireto = embarqueService.findCarregamentoGMDireto(idMonitoramentoDescarga);

		/** LMS-1052: Verifica se Carregamento GM Normal */
		final Carregamento carregamentoGmNormal = embarqueService.findCarregamentoGMNormal(idMonitoramentoDescarga);

		/** Busca todos conhecimento ainda não fechados do Monitoramento Descarga */
		final List<Conhecimento> conhecimentoEDIList = conhecimentoService.findDoctoServicoEDI(idMonitoramentoDescarga, null);
		for (final Conhecimento conhecimento : conhecimentoEDIList) {
		    if(validateRedespachoLiberaEtiquetaEdi(conhecimento)){
		        /** Fechamento do Conhecimento */
		        executeFechaConhecimento(conhecimento);
		        evictCache.add(conhecimento);
		        
		        /** Regra para liberar memória do hibernate durante o processamento de grande massa de dados */
		        if (evictCache.size() >= 10) {
		            /** Sincroniza parcialmente os objetos em sessão com o banco de dados */
		            super.flush();
		            transaction.commit();
		            transaction = newSession.beginTransaction();
		            /** Remove objeto de suas coleções de cache de primeiro nível */
		            getDao().getAdsmHibernateTemplate().evict(evictCache);
		            newSession.evict(evictCache);
		            evictCache.clear();
		        }
		        
		        tpSituacaoMonitoramento = "M";		        
		    }
		}

        /* Valida Monitoramento Descarga GM */
		if (carregamentoGmDireto != null) {
			parameters.put(ID_CARREGAMENTO_GM, carregamentoGmDireto.getIdCarregamento());
		}

        /* LMS-1052: Se Carregamento GM Normal, valida o Monitoramento Descarga GM */
		if (carregamentoGmNormal != null) {
			parameters.put(ID_CARREGAMENTO_GM, carregamentoGmNormal.getIdCarregamento());
		}

        /* Atualiza Veiculo */
		getMonitoramentoDescargaDAO().updateFinalizarVeiculo(idMonitoramentoDescarga, tpSituacaoMonitoramento);
		transaction.commit();
		return parameters;
	}

	/**
	 * LMS-3281: Verifica se existe pendencia de workflow para os documentos
	 * ligados ao monitoramento, ou seja se houver algum documento de serviço
	 * vinculado ao monitoramento de descarga
	 * (NOTA_FISCAL_CONHECIMENTO.ID_CONHECIMENTO = CONHECIMENTO.ID_CONHECIMENTO
	 * e VOLUME_NOTA_FISCAL.ID_NOTA_FISCAL_CONHECIMENTO = NOTA_FISCAL_CONHECIMENTO.ID_NOTA_FISCAL_CONHECIMENTO
	 * e MONITORAMENTO_DESCARGA.ID_MONITORAMENTO_DESCARGA = VOLUME_NOTA_FISCAL.ID_MONITORAMENTO_DESCARGA)
	 * com pendência de aprovação na tabela DOCTO_SERVICO_WORKFLOW
	 * (DOCTO_SERVICO_WORKFLOW.TP_SITUACAO_APROVACAO = "E" >> situação "em aprovação",
	 * para CONHECIMENTO.ID_CONHECIMENTO = DOCTO_SERVICO_WORKFLOW.ID_DOCTO_SERVICO)
	 *
	 * @param param
	 * @return
	 */
	public Map<String, Object> validatePendenciaWorkflow(TypedFlatMap param) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("existePendenciaWorkflow", Boolean.FALSE);
		Map<String, Object> criteria = null;

		MonitoramentoDescarga monitoramentoDescarga = this.getMonitoramentoDescargaDAO().findByIdEager(param.getLong(ID_MONITORAMENTO_DESCARGA));
		if (monitoramentoDescarga.getVolumesNotaFiscal() != null) {
			executeValidaPendenciaWorkflow(retorno, monitoramentoDescarga);
		}

		return retorno;
	}

	private void executeValidaPendenciaWorkflow(Map<String, Object> retorno, MonitoramentoDescarga monitoramentoDescarga) {
		Map<String, Object> criteria;
		for (VolumeNotaFiscal volumeNotaFiscal : monitoramentoDescarga.getVolumesNotaFiscal()) {
			if (volumeNotaFiscal.getNotaFiscalConhecimento() != null && volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento() != null) {
				criteria = new HashMap<String, Object>();
				criteria.put("tpSituacaoAprovacao", "E");
				criteria.put("doctoServico.idDoctoServico", volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico());
				List doctosServicoWorkflow = doctoServicoWorkflowService.find(criteria);

				if (doctosServicoWorkflow != null && !doctosServicoWorkflow.isEmpty()) {
					retorno.put("existePendenciaWorkflow", Boolean.TRUE);
					break;
				}
			}
		}
	}

	public Map<String, Object> validateTempoProcessamentoEdi(TypedFlatMap param) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put(ID_MONITORAMENTO_DESCARGA, param.getLong(ID_MONITORAMENTO_DESCARGA));
		retorno.put("tempoMaximoProcessamentoExedido", Boolean.FALSE);
		DateTime dhInclusao;

		Long idDoctoServico = doctoServicoService.findMaxIdDoctoServico(param.getLong(ID_MONITORAMENTO_DESCARGA));
		if (idDoctoServico != null) {
			DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
			dhInclusao = doctoServico.getDhInclusao();
			ParametroGeral paramTempoMaximoProcessamentoEdi = parametroGeralService.findByNomeParametro("TEMPO_MAXIMO_PROCESSAMENTO_EDI", Boolean.FALSE);
			Integer tempoMaximoProcessamentoEdi = Integer.parseInt(paramTempoMaximoProcessamentoEdi.getDsConteudo());
			dhInclusao = dhInclusao.plusMinutes(tempoMaximoProcessamentoEdi);

			if (dhInclusao.isBefore(JTDateTimeUtils.getDataHoraAtual())) {
				retorno.put("tempoMaximoProcessamentoExedido", Boolean.TRUE);
			}
		}else{
		    retorno.put("naoContemDocumentosVinculados", Boolean.TRUE);
		}

		retorno.put("blExisteDoctoServicoNegativo", doctoServicoService.findExisteDoctoServicoNegativo(param.getLong(ID_MONITORAMENTO_DESCARGA)));

		return retorno;
	}

	/**
	 * Busca os registros para Finalizar Descarga
	 *
	 * @param idMonitoramentoDescarga
	 * @return
	 * @author André Valadas
	 */
	public Map<String, Object> findConhecimentosEDI(final Long idMonitoramentoDescarga, final Boolean validateWithSOM) {
		return findConhecimentosEDI(idMonitoramentoDescarga, validateWithSOM, Boolean.FALSE);
	}

	public Map<String, Object> findConhecimentosEDI(final Long idMonitoramentoDescarga, final Boolean validateWithSOM, Boolean blContingencia) {
		/** Verifica se Carregamento GM Direto */
		final Carregamento carregamentoGmDireto = embarqueService.findCarregamentoGMDireto(idMonitoramentoDescarga);

		/** LMS-1052: Verifica se Carregamento GM Normal */
		final Carregamento carregamentoGmNormal = embarqueService.findCarregamentoGMNormal(idMonitoramentoDescarga);

		/** Busca todos conhecimento ainda não fechados do Monitoramento Descarga */
		List<Conhecimento> conhecimentoEDIList = new ArrayList<Conhecimento>();
		List<Conhecimento> conhecimentoMonitoramentoDescargaList = conhecimentoService.findDoctoServicoEDI(idMonitoramentoDescarga, null);
		
		if(CollectionUtils.isNotEmpty(conhecimentoMonitoramentoDescargaList)){
		    for (final Conhecimento conhecimento : conhecimentoMonitoramentoDescargaList) {
		        if(validateRedespachoLiberaEtiquetaEdi(conhecimento)){
		            conhecimentoEDIList.add(conhecimento);
		        }
		    }    
		}
		
		// Caso não encontre conhecimentos processados pelo EDI, busca os que foram digitados manualmente
		if (blContingencia && CollectionUtils.isEmpty(conhecimentoEDIList)) {
			conhecimentoEDIList = conhecimentoService.findDoctoServico(idMonitoramentoDescarga);
		}

		/** Configura dados para processar Conhecimentos com SOM */
		validateDataToSOM(conhecimentoEDIList, validateWithSOM);

		final Map result = new HashedMap();
		result.put(CONHECIMENTO_EDI_LIST, conhecimentoEDIList);

		/** Valida Monitoramento Descarga GM */
		if (carregamentoGmDireto != null) {
			result.put(ID_CARREGAMENTO_GM, carregamentoGmDireto.getIdCarregamento());
		}
		/** Atualiza a situacao do monitoramento para carregamento sem "Labeling" */
		if (!conhecimentoEDIList.isEmpty()) {
			result.put(TP_SITUACAO_MONITORAMENTO, ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA);
		} else {
			result.put(TP_SITUACAO_MONITORAMENTO, ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA);
		}

		/** LOG Conhecimentos */
		LOGGER.warn("[INICIANDO MONITORAMENTO DESCARGA];" + idMonitoramentoDescarga + "; TOTAL:" + conhecimentoEDIList.size());
		return result;
	}

    private boolean validateRedespachoLiberaEtiquetaEdi(final Conhecimento conhecimento) {
        return (BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario()) && BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlLiberaEtiquetaEdiLinehaul())) 
                || (!BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario()) && BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlLiberaEtiquetaEdi()));
    }


	private void validateDataToSOM(final List<Conhecimento> conhecimentoEDIList, final Boolean validateWithSOM) {
		/** Conhecimentos */
		if (Boolean.TRUE.equals(validateWithSOM)) {
            /* cache pra evitar multiplas chamadas ao banco */
			final Map<Long, String> filialCache = new HashMap<Long, String>();
			/** Carrega Filial do Sistema Legado para processos posteriores */
			for (final Conhecimento conhecimento : conhecimentoEDIList) {
				final Long idFilial = conhecimento.getFilialByIdFilialOrigem().getIdFilial();
				if (!filialCache.containsKey(idFilial)) {
					final String sgRedFilialOrigem = filialService.findSgFilialLegadoByIdFilial(idFilial);
					filialCache.put(idFilial, sgRedFilialOrigem);
				}
				conhecimento.getFilialByIdFilialOrigem().setSgFilial(filialCache.get(idFilial));
			}
		}
	}

	/**
	 * Atualiza o Monitoramento Descarga
	 *
	 * @param idMonitoramentoDescarga
	 * @param tpSituacaoMonitoramento
	 */
	public void updateMonitoramentoDescarga(final Long idMonitoramentoDescarga, final String tpSituacaoMonitoramento) {
		/** LOG Monitoramento */
		LOGGER.warn("[FINALIZANDO MONITORAMENTO DESCARGA];" + idMonitoramentoDescarga + ";" + tpSituacaoMonitoramento);
		getMonitoramentoDescargaDAO().updateFinalizarVeiculo(idMonitoramentoDescarga, tpSituacaoMonitoramento);
	}

	public void updateLimpezaTelaMonitoramentoDescarga(final Long idMonitoramentoDescarga, final String tpSituacaoMonitoramento) {
		/** LOG Monitoramento */
		LOGGER.warn("[LIMPEZA DA TELA DE MONITORAMENTO DE DESCARGA];" + idMonitoramentoDescarga + ";" + tpSituacaoMonitoramento);
		getMonitoramentoDescargaDAO().updateLimpezaTelaFinalizarVeiculo(idMonitoramentoDescarga, tpSituacaoMonitoramento);
	}


	public void updateMonitoramentoDescargaEDI(PedidoColeta pedidoColeta, Long idMonitoramentoDescarga) {
		Boolean atualizarQtVolumesTotal = Boolean.FALSE;

		String tipoSituacaoDescarga = ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_INICIADA;
		if (pedidoColeta != null && "BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())) {
			tipoSituacaoDescarga = ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA;
			atualizarQtVolumesTotal = Boolean.TRUE;
		}

		getMonitoramentoDescargaDAO().updateTpSituacaoQtVolumesMonitoramentoDescarga(idMonitoramentoDescarga, tipoSituacaoDescarga, atualizarQtVolumesTotal);
	}

	public void updateDescarcaFinalizadaMonitoramentoDescargaEDI(Long idMonitoramentoDescarga) {
		getMonitoramentoDescargaDAO().updateTpSituacaoQtVolumesMonitoramentoDescarga(idMonitoramentoDescarga, ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA, Boolean.FALSE);
	}

	public void updateDigitacaoNotasIniciadaMonitoramentoDescargaEDI(Long idMonitoramentoDescarga) {
		getMonitoramentoDescargaDAO().updateTpSituacaoQtVolumesMonitoramentoDescarga(idMonitoramentoDescarga, ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_INICIADA, Boolean.FALSE);
	}

	/**
	 * Habilita Monitoramento para Finalização de Veículos após o concluído o Processamento EDI
	 *
	 * @param nrIdentificacao
	 * @param nrNotaFiscal
	 * @param isMonitoramentoNotaZerada
	 */
	public void updateMonitoramentoEDI(final String nrIdentificacao, final String nrNotaFiscal, boolean isMonitoramentoNotaZerada) {

		if (isMonitoramentoNotaZerada) {
			getMonitoramentoDescargaDAO().updateMonitoramentoEDI(null, nrIdentificacao, nrNotaFiscal);
		} else {
			getMonitoramentoDescargaDAO().updateMonitoramentoEDINotaCheia(null, nrIdentificacao, nrNotaFiscal);
		}

	}

	public void updateMonitoramentoEDI(final PedidoColeta pedidoColeta, final String nrIdentificacao, final String nrNotaFiscal, boolean isMonitoramentoNotaZerada) {
		if (isMonitoramentoNotaZerada) {
			getMonitoramentoDescargaDAO().updateMonitoramentoEDI(pedidoColeta, nrIdentificacao, nrNotaFiscal);
		} else {
			getMonitoramentoDescargaDAO().updateMonitoramentoEDINotaCheia(pedidoColeta, nrIdentificacao, nrNotaFiscal);
		}

	}

	/**
	 * Fecha o DoctoServico
	 *
	 * @param conhecimento
	 */
	public void executeFechaConhecimento(Conhecimento conhecimento) {
		executeFechaConhecimento(conhecimento, false);
	}

	public void executeFechaConhecimentoEMonitoramentoDescarga(Conhecimento conhecimento, Boolean blContingencia, Long idMonitoramentoDescarga) {
		conhecimento = fecharConhecimento(conhecimento, blContingencia);
		updateFinalizaMonitoramentoDescarga(idMonitoramentoDescarga);
		removerObjetoDasSuasColecoesDeCacheDePrimeiroNivel(conhecimento);
	}
	
	public void executeFechaConhecimento(Conhecimento conhecimento, Boolean blContingencia) {
		conhecimento = fecharConhecimento(conhecimento, blContingencia);
		removerObjetoDasSuasColecoesDeCacheDePrimeiroNivel(conhecimento);
	}

	/**
	 * Chama a rotina FechaCTRC para deixar o ctrc pronto para ser impresso
	 * ate aqui o numero do conhecimento e negativo
	 * @param conhecimento
	 */
	private Conhecimento fecharConhecimento(Conhecimento conhecimento, Boolean blContingencia) {
		Map<String, Object> dadosCtrc = new HashMap<String, Object>();
		dadosCtrc.put(ID_DOCTO_SERVICO, conhecimento.getIdDoctoServico());
		dadosCtrc.put(NR_CONHECIMENTO, conhecimento.getNrConhecimento());
		dadosCtrc.put(DT_PREVISTA_ENTREGA, conhecimento.getDtPrevEntrega());
		dadosCtrc.put(NR_DIAS_PREV_ENTREGA, conhecimento.getNrDiasPrevEntrega());
		dadosCtrc.put(TP_DOCTO_SERVICO, conhecimento.getTpDoctoServico());
		dadosCtrc.put(GENERATE_UNIQUE_NUMBER, Boolean.TRUE);
		dadosCtrc.put(BL_CONTINGENCIA, blContingencia);

		calcularFreteService.fechaEmiteCTRC(dadosCtrc);

		//Atualiza os volumes com o numero do conhecimento ja atualizado
		MonitoramentoDescarga monitoramentoDescarga = null;
		conhecimento = (Conhecimento) dadosCtrc.get("conhecimento");
		for (NotaFiscalConhecimento nfc : conhecimento.getNotaFiscalConhecimentos()) {
			for (VolumeNotaFiscal vnf : (List<VolumeNotaFiscal>) nfc.getVolumeNotaFiscais()) {
				if (monitoramentoDescarga == null) {
					monitoramentoDescarga = vnf.getMonitoramentoDescarga();
					if (monitoramentoDescarga.getQtVolumesTotal() == null) {
						monitoramentoDescarga.setQtVolumesTotal(LongUtils.ZERO);
						store(monitoramentoDescarga);
					}
				}
				vnf.setNrConhecimento(conhecimento.getNrConhecimento());
			}
		}

		conhecimento.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
		if (blContingencia) {
			executeFechaConhecimento(conhecimento, monitoramentoDescarga);
		}
		conhecimentoService.store(conhecimento);
		return conhecimento;
	}
	
	private void removerObjetoDasSuasColecoesDeCacheDePrimeiroNivel(
			Conhecimento conhecimento) {
		super.flush();
		getDao().getAdsmHibernateTemplate().evict(conhecimento);
	}
	
	public void updateSituacaoMonitoramentoByIdMeioTransporte(Long idMonitoramentoDescarga, Long idFilial) {
		List<MonitoramentoDescarga> monitoramentos = getMonitoramentoDescargaDAO().findByDescargasFinalizadas(idFilial, idMonitoramentoDescarga);
		for (MonitoramentoDescarga monitoramentoDescarga : monitoramentos) {
			monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue("S"));
			monitoramentoDescarga.setDhEmissaoCTRC(JTDateTimeUtils.getDataHoraAtual());
			store(monitoramentoDescarga);
		}
	}

	public List findByDescargasComPreCtrc(Long idFilial, String nrPlaca, String nrFrota) {
		return getMonitoramentoDescargaDAO().findByDescargasComPreCtrc(idFilial, nrPlaca, nrFrota);
	}

	public List findDescargasFinalizadas(Long idFilial, Long idMeioTransporte, Long idMonitoramentoDescarga, String nrPlaca, String nrFrota, String tpOperacao, DateTime dhFiltro, Boolean isCancelamento) {
		return getMonitoramentoDescargaDAO().findDescargasFinalizadas(idFilial, idMeioTransporte, idMonitoramentoDescarga, nrPlaca, nrFrota, tpOperacao, dhFiltro, isCancelamento);
	}

	public List findDescargasByFilialSorter(Long idFilial, Long idMeioTransporte, Long idMonitoramentoDescarga, String nrPlaca, String nrFrota) {
		return getMonitoramentoDescargaDAO().findDescargasByFilialSorter(idFilial, idMeioTransporte, idMonitoramentoDescarga, nrPlaca, nrFrota);
	}

	/**
	 * Recupera uma instância de <code>MonitoramentoDescarga</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MonitoramentoDescarga findById(java.lang.Long id) {
		return (MonitoramentoDescarga) super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MonitoramentoDescarga bean) {
		super.store(bean);
		return bean;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param dao Instância do DAO.
	 */
	public void setMonitoramentoDescargaDAO(MonitoramentoDescargaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MonitoramentoDescargaDAO getMonitoramentoDescargaDAO() {
		return (MonitoramentoDescargaDAO) getDao();
	}

	public List findMonitoramentoDescargasByIdFilial(java.lang.Long id) {
		Map map = new HashMap();
		map.put(FILIAL_ID_FILIAL, id);
		return find(map);
	}

	public MonitoramentoDescarga findByIdFilial(java.lang.Long id) {
		Map map = new HashMap();
		map.put(ID_FILIAL, id);
		List l = find(map);
		if (!l.isEmpty()) {
			return (MonitoramentoDescarga) l.get(0);
		}
		return null;
	}

	public MonitoramentoDescarga findMonitoramentoDescargaByVolumeNotaFiscal(final Long idVolumeNotaFiscal) {
		return getMonitoramentoDescargaDAO().findMonitoramentoDescargaByVolumeNotaFiscal(idVolumeNotaFiscal);
	}

	public List<Map<String, Object>> findMonitoramentoDescargasByAfterDate(DateTime dateTime, Long idFilial) {
		return getMonitoramentoDescargaDAO().findMonitoramentoDescargasByIdFilialByAfterDate(dateTime, idFilial);
	}

	public List findMonitoramentoDescargasByNrFrota(String nrFrota) {
		Map map = new HashMap();
		map.put(NR_FROTA, nrFrota);
		return super.find(map);
	}

	/**
	 * Busca o monitoramento de descarga de acordo com os paramettros recebidos.
	 * Metodo criado para melhoria de performance no processo de criacao de
	 * conhecimentos.
	 *
	 * @param idMeioTransporte   identificador do meio de transporte
	 * @param nrFrota            numero da frota
	 * @param tpSituacaoDescarga situacao da descarga
	 * @param idFilial           identificador da filial
	 * @return monitoramento encontrado ou nulo
	 * @author Luis Carlos Poletto
	 */
	public MonitoramentoDescarga find(Long idMeioTransporte, String nrFrota, String tpSituacaoDescarga, Long idFilial) {
		return find(idMeioTransporte, nrFrota, tpSituacaoDescarga, idFilial, null);
	}

	public MonitoramentoDescarga find(Long idMeioTransporte, String nrFrota, String tpSituacaoDescarga, Long idFilial, String nrProcessamento) {
		return getMonitoramentoDescargaDAO().find(idMeioTransporte, nrFrota, tpSituacaoDescarga, idFilial, nrProcessamento);
	}

	/**
	 * nrFrota como parâmetro, por que alguns veículos não são cadastrados, por exemplo, veículos de terceiros. Sendo assim, não existindo idMeioTransporte.
	 * Também foi incluso a filial, estava ocorrendo de digitar a mesma frota em mais de uma filial. Como por exemplo: "BALCAO"
	 *
	 * @param nrFrota
	 * @param tpSituacaoDescarga
	 * @param idFilial
	 * @return
	 */
	public List findMonitoramentoDescargasByNrFrotaByTpSituacaoDescargaByIdFilial(Long idMeioTransporte, String nrFrota, String tpSituacaoDescarga, Long idFilial) {
		Map map = new HashMap();
		map.put(MEIO_TRANSPORTE_ID_MEIO_TRANSPORTE, idMeioTransporte);
		map.put(NR_FROTA, nrFrota);
		map.put(TP_SITUACAO_DESCARGA, tpSituacaoDescarga);
		map.put(FILIAL_ID_FILIAL, idFilial);
		return super.find(map);
	}

	/**
	 * nrFrota como parâmetro, por que alguns veículos não são cadastrados, por exemplo, veículos de terceiros, sendo assim, não existindo idMeioTransporte
	 *
	 * @param parameters
	 * @return
	 */
	public TypedFlatMap executeFinalizarVeiculo(Map parameters) {
		TypedFlatMap returnData = new TypedFlatMap();
		MonitoramentoDescarga monitoramentoDescarga = null;
		String nrFrota = (String) parameters.get(NR_FROTA);
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		Long idMeioTransporte = null;

		List list = findMonitoramentoDescargasByNrFrotaByTpSituacaoDescargaByIdFilial(idMeioTransporte, nrFrota, "V", idFilial);
		if (CollectionUtils.isEmpty(list)) {
			throw new BusinessException("LMS-04227");
		} else {
			monitoramentoDescarga = (MonitoramentoDescarga) list.get(0);
			parameters.put(ID_MONITORAMENTO_DESCARGA, monitoramentoDescarga.getIdMonitoramentoDescarga());
		}
		List<Map<String, Object>> ctrcList = new ArrayList<Map<String, Object>>();
		list = notaFiscalConhecimentoService.findByMonitoramentoDescargaByBlPsAferido(monitoramentoDescarga.getIdMonitoramentoDescarga(), Boolean.FALSE);
		if (!CollectionUtils.isEmpty(list)) {
			long totalVolumes = 0;
			boolean fechaMonitoramento = false;
			for (int i = 0; i < list.size(); i++) {
				NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento) list.get(i);
				Conhecimento cto = notaFiscalConhecimento.getConhecimento();
				String sgRedFilialOrigem = filialService.findSgFilialLegadoByIdFilial(cto.getFilialByIdFilialOrigem().getIdFilial());
				Map<String, Object> ctrcMap = new HashMap<String, Object>();
				ctrcMap.put(ID_DOCTO_SERVICO, cto.getIdDoctoServico());
				ctrcMap.put(NR_CONHECIMENTO, cto.getNrConhecimento());
				ctrcMap.put(DT_PREVISTA_ENTREGA, cto.getDtPrevEntrega());
				ctrcMap.put(NR_DIAS_PREV_ENTREGA, cto.getNrDiasPrevEntrega());
				ctrcMap.put(TP_DOCTO_SERVICO, cto.getTpDoctoServico());
				ctrcMap.put(ID_FILIAL_ORIGEM, cto.getFilialByIdFilialOrigem().getIdFilial());
				ctrcMap.put(SG_RED_FILIAL_ORIGEM, sgRedFilialOrigem);
				ctrcList.add(ctrcMap);
				if (Boolean.TRUE.equals(cto.getBlIndicadorEdi()) &&
						ConhecimentoUtils.isLiberaEtiquetaEdi(cto)) {
					fechaMonitoramento = true;
				}
				totalVolumes = totalVolumes + notaFiscalConhecimento.getQtVolumes();
			}
			monitoramentoDescarga.setQtVolumesTotal(totalVolumes);
			if (fechaMonitoramento) {
				returnData.put(FECHA_MONITORAMENTO, true);
				returnData.put(CTRC_LIST, ctrcList);
				//retorna sem salvar o monitoramento descarga
				return returnData;
			} else {
				monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue("D"));
				returnData.put(FECHA_MONITORAMENTO, false);
			}

			super.store(monitoramentoDescarga);

			/** LMS-1052: preparar Relatório de Discrepâncias */
			generateRelatorioDiscrepancia(parameters);
		}
		return returnData;
	}

	/**
	 * Prepara o Relatório de Discrepâncias TNT x TNT para execução
	 *
	 * @param parameters <code>Map</code>
	 * @author Sidarta Silva
	 */
	private void generateRelatorioDiscrepancia(Map parameters) {
		Long idMonitoramentoDescarga = (Long) parameters.get(ID_MONITORAMENTO_DESCARGA);

		// Verificar o carregamento GM do monitoramento
		Long idCarregamentoGM = this.getMonitoramentoDescargaDAO().findIdCarregamentoGM(idMonitoramentoDescarga);

		// Se encontrado o carregamento GM, verificar todos os mapas
		if (idCarregamentoGM != null) {
			List listMapas = this.getMonitoramentoDescargaDAO().findMapasByIdCarregamentoGM(idCarregamentoGM);

			// Se encontrados os mapas, enviar o relatório de discrepância
			if (listMapas != null && !listMapas.isEmpty()) {

				List<String> listIds = new ArrayList<String>();

				Map email = new HashedMap();
				email.put(ORIGEM, SessionUtils.getFilialSessao().getIdFilial());
				email.put(TIPO_EMITENTE, "TNT - Veículo");
				email.put(DATA_FECHAMENTO, new DateTime());
				email.put(EMAIL_ENDERECO, "iardlei.agassi@tntbrasil.com.br");
				email.put(EMAIL_CC, "iardlei.agassi@tntbrasil.com.br");
				email.put(EMAIL_TITULO, "Relatório de Discrepâncias EDI X MWW");
				email.put(EMAIL_CORPO, "Relatório de Discrepâncias EDI X MWW");
				email.put(ID_MONITORAMENTO_DESCARGA, idMonitoramentoDescarga);
				listIds.add(idCarregamentoGM.toString());
				email.put(EMAIL_IDS, listIds);

				parameters.put(RELATORIO_DISCREPANCIA_SERVICE, email);
			}
		}
	}

	public Map executeValidateVolumeGMFinalizarDescarga(MonitoramentoDescarga monitoramentoDescarga) {
		Map parameters = new HashedMap();
		parameters.put(ID_CARREGAMENTO_GM, null);
		parameters.put(TP_SITUACAO_DESCARGA, null);

		List<VolumeNotaFiscal> listVolumesNotaFiscal = volumeNotaFiscalService.findVolumesNotaFiscalByIdMonitoramento(monitoramentoDescarga.getIdMonitoramentoDescarga());

		if (!CollectionUtils.isEmpty(listVolumesNotaFiscal)) {
			atualizaVNFEMonitoramentoDescargaGM(monitoramentoDescarga, parameters, listVolumesNotaFiscal);
		}

		//é GM direto
		if (parameters.containsKey(ID_CARREGAMENTO_GM)) {
			List<Object[]> manifestos = carregamentoDescargaService.findManifestosParaSOM(monitoramentoDescarga.getIdMonitoramentoDescarga());
			parameters.put(MANIFESTOS_GM, manifestos);
		}

		if (monitoramentoDescarga.getTpSituacaoDescarga() != null) {
			if ("V".equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())) {
				monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue("D"));
			}
			parameters.put(TP_SITUACAO_DESCARGA, monitoramentoDescarga.getTpSituacaoDescarga().getValue());
		}
		return parameters;
	}

	private void atualizaVNFEMonitoramentoDescargaGM(MonitoramentoDescarga monitoramentoDescarga, Map parameters, List<VolumeNotaFiscal> listVolumesNotaFiscal) {
		Carregamento carregamento;
		for (VolumeNotaFiscal vnf : listVolumesNotaFiscal) {
			Volume volume = volumeService.findVolumeByCodigoBarras(vnf.getNrVolumeColeta());
			if (volume == null) {
				break;
			}
			carregamento = (Carregamento) embarqueService.findById(volume.getCarregamento().getIdCarregamento());
			if (carregamento != null) {
                    /*  LMS-1052: se o Carregamento for Direto ou Normal */
				if ("D".equals(carregamento.getTipoCarregamento()) || "N".equals(carregamento.getTipoCarregamento())) {
					monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue("S"));
					monitoramentoDescarga.setDhFimDescarga(JTDateTimeUtils.getDataHoraAtual());
					monitoramentoDescarga.setDhEmissaoCTRC(JTDateTimeUtils.getDataHoraAtual());
					monitoramentoDescarga.setDhChegadaVeiculo(JTDateTimeUtils.getDataHoraAtual());

					vnf.setPsAferido(BigDecimal.ZERO);
					getVolumeNotaFiscalService().store(vnf);

					parameters.put(ID_CARREGAMENTO_GM, carregamento.getIdCarregamento());
				}
			} else {
				break;
			}
		}
	}

	public void executeContinuarEmissao(Long idMonitoramentoDescarga) {
		MonitoramentoDescarga monitoramentoDescarga = findById(idMonitoramentoDescarga);
		if ("Q".equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())) {
			monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue("M"));
			store(monitoramentoDescarga);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeIniciarEmissao(Long idMonitoramentoDescarga) {
		MonitoramentoDescarga monitoramentoDescarga = findById(idMonitoramentoDescarga);
		monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue("Q"));
		store(monitoramentoDescarga);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeEmissaoRealizada(Long idMonitoramentoDescarga) {
		MonitoramentoDescarga monitoramentoDescarga = findById(idMonitoramentoDescarga);
		monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue("S"));
		store(monitoramentoDescarga);
	}

	/**
	 * Busca o monitoramento de descarga relacionado ao volume expedido
	 * representado pelo codigo de barras recebido por parametro.
	 *
	 * @param codigoBarras codigo de barras para ser usado como critério de busca
	 * @return volume encontrado ou nulo
	 * @author Luis Carlos Poletto
	 */
	public MonitoramentoDescarga findVolumeExpedido(final String codigoBarras) {
		return getMonitoramentoDescargaDAO().findVolumeExpedido(codigoBarras);
	}


	@SuppressWarnings("unchecked")
	public void executeFechaConhecimento(Conhecimento conhecimento, MonitoramentoDescarga monitoramentoDescarga) {
		conhecimento.setVlFretePesoDeclarado(conhecimento.getVlImposto());
		conhecimento.setPsAferido(conhecimento.getPsReal());
		//LMS-2353
		conhecimento.setPsCubadoAferido(conhecimento.getPsCubadoDeclarado());

		LocalizacaoMercadoria localizacaoMercadoria = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(CD_LOCALIZACAO_MERCADORIA);

		for (NotaFiscalConhecimento n : conhecimento.getNotaFiscalConhecimentos() ) {
			for (VolumeNotaFiscal vnf : (List<VolumeNotaFiscal>)n.getVolumeNotaFiscais()) {

				if (vnf.getNrVolumeEmbarque() == null || vnf.getNrVolumeEmbarque().contains("-")) {

					String nrVolumeEmbarque = Boolean.TRUE.equals(conhecimento.getBlPaletizacao()) ?
							null : volumeNotaFiscalService.buildLabelBarCode(conhecimento, vnf);

					vnf.setNrVolumeEmbarque(nrVolumeEmbarque);
				}

				if (Boolean.TRUE.equals(conhecimento.getBlPaletizacao()) &&
						ConhecimentoUtils.isLiberaEtiquetaEdi(conhecimento)) {
					vnf.setTpVolume(ConstantesExpedicao.TP_VOLUME_UNITARIO);
				}

				vnf.setLocalizacaoMercadoria(localizacaoMercadoria);
				vnf.setLocalizacaoFilial(conhecimento.getFilialByIdFilialOrigem());
				vnf.setPsAferido(BigDecimal.ZERO);
				vnf.setNrConhecimento(conhecimento.getNrConhecimento());
			}
		}
		for (NotaFiscalConhecimento n : conhecimento.getNotaFiscalConhecimentos()) {
			for (VolumeNotaFiscal vnf : (List<VolumeNotaFiscal>) n.getVolumeNotaFiscais()) {

				if (vnf.getNrVolumeEmbarque() == null || vnf.getNrVolumeEmbarque().contains("-")) {

					String nrVolumeEmbarque = Boolean.TRUE.equals(conhecimento.getBlPaletizacao()) ?
							null : volumeNotaFiscalService.buildLabelBarCode(conhecimento, vnf);

					vnf.setNrVolumeEmbarque(nrVolumeEmbarque);
				}
				vnf.setLocalizacaoMercadoria(localizacaoMercadoria);
				vnf.setLocalizacaoFilial(conhecimento.getFilialByIdFilialOrigem());
				vnf.setPsAferido(BigDecimal.ZERO);
				vnf.setNrConhecimento(conhecimento.getNrConhecimento());
			}
		}

		Map<String, Object> dataPrevisaoEntrega = dpeService.executeCalculoDPE(
				conhecimento.getClienteByIdClienteRemetente(),
				conhecimento.getClienteByIdClienteDestinatario(),
				conhecimento.getClienteByIdClienteBaseCalculo(),
				conhecimento.getClienteByIdClienteConsignatario(),
				conhecimento.getClienteByIdClienteRedespacho(),
				null,
				conhecimento.getServico().getIdServico(),
				conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio(),
				conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
				conhecimento.getFilialByIdFilialDestino().getIdFilial(),
				conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio(),
				conhecimento.getNrCepColeta(),
				conhecimento.getNrCepEntrega(),
				conhecimento.getDtColeta()
		);
		if (dataPrevisaoEntrega != null) {
			Long nrDias = (Long) dataPrevisaoEntrega.get(NR_PRAZO);
			if (nrDias != null) {
				conhecimento.setNrDiasPrevEntrega(nrDias.shortValue());
			}
			conhecimento.setDtPrevEntrega((YearMonthDay) dataPrevisaoEntrega.get(DT_PRAZO_ENTREGA));
			// LMS-8779
			doctoServicoPPEPadraoService.updateDoctoServicoPPEPadrao(conhecimento.getIdDoctoServico(), (Long) dataPrevisaoEntrega.get("nrDiasColeta"), (Long) dataPrevisaoEntrega.get("nrDiasEntrega"), (Long) dataPrevisaoEntrega.get("nrDiasTransferencia"));

		}
	}


	public void setDoctoServicoPPEPadraoService(DoctoServicoPPEPadraoService doctoServicoPPEPadraoService) {
		this.doctoServicoPPEPadraoService = doctoServicoPPEPadraoService;
	}

	/**
	 * Busca os monitoramentos de descarga associados aos volumes notas fiscais
	 * através da busca do conhecimento associado ao código de barras recebido
	 * por parametro.
	 *
	 * @param codigoBarras codigo de barras para ser usado como critério de busca
	 * @return lista de monitoramentos encontrados ou nulo
	 * @author Luis Carlos Poletto
	 */
	public List<MonitoramentoDescarga> findVolumesExpedidos(final String codigoBarras) {
		return getMonitoramentoDescargaDAO().findVolumesExpedidos(codigoBarras);
	}

	/**
	 * Retorna o MonitoramentoDescarga à partir do idConhecimento
	 *
	 * @param idConhecimento
	 * @return {@link MonitoramentoDescarga}
	 */
	public List<MonitoramentoDescarga> findByIdConhecimento(Long idConhecimento) {
		return getMonitoramentoDescargaDAO().findByIdConhecimento(idConhecimento);
	}

	/**
	 * Atualiza a situação de cada MonitoramentoDescarga de acordo com o parâmetro passado
	 *
	 * @param idFilial
	 * @param quantidadeDiasLimpeza
	 * @param novoTpSituacaoDescarga
	 */
	@Transactional
	public void limpezaTelaMonitoramentoDescarga(Long idFilial, String quantidadeDiasLimpeza, String novoTpSituacaoDescarga ) {
		BigDecimal quantidadeDiaLimpezaProcessoEdiIniciado =(new BigDecimal(parametroGeralService
														.findConteudoByNomeParametro
															(
												"QT_HORAS_LIMPEZA_PROCES_EDI_INICIADO",
													false
															).toString())).divide(new BigDecimal(24)).setScale(1);

		List idsMonitoramentoDescargas = this.getMonitoramentoDescargaDAO()
				.findByFilialByQuantidadeDiasLimpezaMonitoramentoDescarga
					(idFilial, quantidadeDiasLimpeza, quantidadeDiaLimpezaProcessoEdiIniciado);

		for (Object idMonitoramentoDescarga : idsMonitoramentoDescargas) {
			this.updateLimpezaTelaMonitoramentoDescarga((Long)idMonitoramentoDescarga, novoTpSituacaoDescarga);
		}

	}

	public List<Object[]> findMonitoramentosConcluidos() {
		return this.getMonitoramentoDescargaDAO().findMonitoramentosConcluidos();
	}

	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public VolumeService getVolumeService() {
		return volumeService;
	}

	public void setVolumeService(VolumeService volumeService) {
		this.volumeService = volumeService;
	}

	public EmbarqueService getEmbarqueService() {
		return embarqueService;
	}

	public void setEmbarqueService(EmbarqueService embarqueService) {
		this.embarqueService = embarqueService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public DpeService getDpeService() {
		return dpeService;
	}

	public void setDpeService(DpeService dpeService) {
		this.dpeService = dpeService;
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}

	public void updateMonitoramentoNotaFiscalEDI(String nrIdentificacao, String nrNotaFiscal, boolean isMonitoramentoNotaZerada) {

		List<MonitoramentoDescarga> listaMonitoramernto = null;
		if (isMonitoramentoNotaZerada) {
			listaMonitoramernto = this.getMonitoramentoDescargaDAO().findMonitoramentoEmProcessamentoEDI(nrIdentificacao, nrNotaFiscal);
		} else {
			listaMonitoramernto = this.getMonitoramentoDescargaDAO().findMonitoramentoEmProcessamentoEDINotaCheia(nrIdentificacao, nrNotaFiscal);
		}

		for (MonitoramentoDescarga monitoramento : listaMonitoramernto) {
			this.getMonitoramentoDescargaDAO().updateMonitoramentoNotaFiscalEDI(monitoramento);
		}
	}

	public void setOcorrenciaNaoConformidadeService(
			OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}

	public void updateFinalizaMonitoramentoDescarga(Long idMonitoramentoDescarga) {
		getMonitoramentoDescargaDAO().updateFinalizaMonitoramentoDescarga(idMonitoramentoDescarga);
	}

	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	public void setDoctoServicoWorkflowService(DoctoServicoWorkflowService doctoServicoWorkflowService) {
		this.doctoServicoWorkflowService = doctoServicoWorkflowService;
	}

	public void setConhecimentoCancelarService(ConhecimentoCancelarService conhecimentoCancelarService) {
		this.conhecimentoCancelarService = conhecimentoCancelarService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public boolean validateUpdateSituacaoMonitoramento(Long idMonitoramentoDescarga) {
		return getMonitoramentoDescargaDAO().validateUpdateSituacaoMonitoramento(idMonitoramentoDescarga);
	}

	public CtoAwbService getCtoAwbService() {
		return ctoAwbService;
	}

	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}

}
