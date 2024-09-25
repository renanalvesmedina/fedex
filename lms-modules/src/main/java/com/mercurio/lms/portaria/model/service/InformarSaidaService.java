package com.mercurio.lms.portaria.model.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.FilialRotaCcService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.ControleEntSaidaTerceiro;
import com.mercurio.lms.portaria.model.OrdemSaida;
import com.mercurio.lms.portaria.model.dao.SaidaChegadaDAO;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import org.hibernate.ObjectNotFoundException;

/**
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. 
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.informarSaidaService"
 */
public class InformarSaidaService {

	private static final String CONTROLE_CARGA = "CCA";
	private SaidaChegadaDAO dao;
	private ControleCargaService controleCargaService;
	private FilialService filialService;
	private ControleQuilometragemService controleQuilometragemService;
	private EventoMeioTransporteService eventoMeioTransporteService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private OrdemSaidaService ordemSaidaService;
	private ControleEntSaidaTerceiroService controleEntSaidaTerceiroService;
	private ConfiguracoesFacade configuracoesFacade;
	private ManifestoService manifestoService;
	private DoctoServicoService doctoServicoService;
	private FronteiraRapidaService fronteiraRapidaService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private EventoVolumeService eventoVolumeService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private AcaoIntegracaoEventosService acaoIntegracaoEventosService;
	private DomainValueService domainValueService;
	private FilialRotaCcService filialRotaCcService;
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private PedidoColetaService pedidoColetaService;
	private DetalheColetaService detalheColetaService;
	private CtoAwbService ctoAwbService;
	private IntegracaoJmsService integracaoJmsService;

	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	private static final String TEXT_HTML = "text/html; charset='utf-8'";

	/**
	 * Consulta os dados da grid de viagens
	 * @param idFilial
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> findGridViagem(Long idFilial) {
		return getDao().findGridViagem(idFilial);
	}

	/**
	 * Consulta os dados da grid de coleta/entrega
	 * @param idFilial
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> findGridColetaEntrega(Long idFilial, Long idPortaria) {

		List<Map<String, Object>> result1 = getDao().findColetaEntregaByControleCargas(idFilial);
		insereTipoColetaEntrega(result1, "coletaEntrega");

		List<Map<String, Object>> result2 = getDao().findColetaEntregaByOrdemSaida(idFilial);
		insereTipoColetaEntrega(result2, "ordemDeSaida");

		List<Map<String, Object>> result3 = getDao().findColetaEntregaByTerceiros(idFilial, idPortaria);
		insereTipoColetaEntrega(result3, "visitante");

		result1.addAll(result2);
		result1.addAll(result3);

		return result1;
	}

	private void insereTipoColetaEntrega(List<Map<String, Object>> list, String chave) {
		String texto = configuracoesFacade.getMensagem(chave);
		if (list != null) {
			for (Map<String, Object> map : list) {
				map.put("tipo", texto);
			}
		}
	}

	/**
	 * Verifica se existem manifestos não disponíveis para dar saída, vinculados ao Controle de. (LMS-697) 
	 * Verifica se o controle de carga está em trânsito ou em viagem. (LMS-766)
	 * @param parameters
	 * @return
	 */
	public boolean validateManifestoETransito(TypedFlatMap parameters) {
		String controle = parameters.getString("idControleTemp");
		String[] tokens = controle.split("\\|");

		Long idControle = Long.valueOf(tokens[0]);

		List<Map<String, Object>> listManifesto = manifestoService.findManifestosEmCarregamentoEConcluidoByIdControleCarga(idControle);

		if (listManifesto != null && listManifesto.size() > 0) {
			StringBuilder manifestos = new StringBuilder();
			StringBuilder status = new StringBuilder();

			for (Map<String, Object> map : listManifesto) {
				manifestos.append(map.get("sgFilial"));
				manifestos.append(' ');
				manifestos.append(StringUtils.leftPad(map.get("nrPreManifesto").toString(), 8, '0'));
				manifestos.append(", ");

				// há um cache dentro de domainValueService, não onera performance
				DomainValue dvStatus = domainValueService.findDomainValueByValue("DM_STATUS_MANIFESTO", ((DomainValue) map.get("tpStatusManifesto")).getValue());

				status.append(dvStatus.getDescriptionAsString());
				status.append(", ");
			}

			Object[] messageArgs = new Object[2];

			messageArgs[0] = manifestos.delete(manifestos.length() - 2, manifestos.length()).toString();
			messageArgs[1] = status.delete(status.length() - 2, status.length()).toString();

			throw new BusinessException("LMS-06037", messageArgs);
		}

		// Verifica se o controle de carga está em trânsito ou em viagem.
		ControleCarga cc;
        
        try {
            cc = controleCargaService.findById(idControle);
        } catch (ObjectNotFoundException e) {
            return true;
        }

        if (cc.getTpStatusControleCarga().getValue().equals("EV") || cc.getTpStatusControleCarga().getValue().equals("TC")) {
            throw new BusinessException("LMS-06039");
        }

        return true;
    }

	/**
	 * Verifica se o controle de carga está em trânsito ou em viagem.
	 * @param parameters
	 * @return
	 */
	public boolean validaTransito(TypedFlatMap parameters) {
		String controle = parameters.getString("idControleTemp");
		String[] tokens = controle.split("\\|");

		Long idControle = Long.valueOf(tokens[0]);

		ControleCarga cc = controleCargaService.findByIdInitLazyProperties(idControle, false);

		if (cc.getTpStatusControleCarga().getValue().equals("EV") || cc.getTpStatusControleCarga().getValue().equals("TC")) {
			throw new BusinessException("LMS-06039");
		}

		return true;
	}

	/**
	 * Rotina de confirmacao da saida do veiculo
	 * @param parameters
	 */
	public void executeConfirmaSaidaColetaEntrega(TypedFlatMap parameters) {
		Boolean blPortaria = Boolean.TRUE;
		Boolean blSaida = Boolean.TRUE;
		Boolean blVirouHodometro = parameters.getBoolean("blVirouHodometro");

		Integer nrQuilometragem = parameters.getInteger("nrQuilometragem");

		Long idFilial = parameters.getLong("idFilial");
		Long idMeioTransporte = parameters.getLong("idMeioTransporte");
		Long idReboque = parameters.getLong("idReboque");
		Long idControleCarga = parameters.getLong("idControleCarga");
		Long idOrdemSaida = parameters.getLong("idOrdemSaida");
		Long idControleEntSaidaTerceiro = parameters.getLong("idControleEntSaidaTerceiro");
		Long nrControleCarga = parameters.getLong("nrControleCarga");

		String obSaida = parameters.getString("obSaida");
		String sgFilialCC = parameters.getString("sgFilial");

		Filial f = filialService.findById(idFilial);
		DateTime today = JTDateTimeUtils.getDataHoraAtual();

		executeEventsAndStoreConfirmaSaidaSemGeracaoEventosVolume(blPortaria, blSaida,
				blVirouHodometro, nrQuilometragem, idFilial, idMeioTransporte,
				idReboque, idControleCarga, nrControleCarga, obSaida,
				sgFilialCC, f, today, false);		

		/* Atualiza data e ghora de saida */
		if (idOrdemSaida != null) {
			/* Atualiza data de saida da ordem de saida */
			OrdemSaida os = ordemSaidaService.findById(idOrdemSaida);
			os.setDhSaida(today);
			ordemSaidaService.store(os);
		} else if (idControleEntSaidaTerceiro != null) {
			/* Atualiza data de saida de eventual */
			ControleEntSaidaTerceiro ct = controleEntSaidaTerceiroService.findById(idControleEntSaidaTerceiro);
			ct.setDhSaida(today);
			controleEntSaidaTerceiroService.store(ct);
		}/* if */
	}
	
	public void executeEventsAndStoreConfirmaSaida(Boolean blPortaria,
			Boolean blSaida, Boolean blVirouHodometro, Integer nrQuilometragem,
			Long idFilial, Long idMeioTransporte, Long idReboque,
			Long idControleCarga, Long nrControleCarga, String obSaida, String sgFilialCC, Filial f, DateTime today, Boolean isGeracaoAutomatica) {
		executeEventsAndStoreConfirmaSaida(blPortaria, blSaida, blVirouHodometro, nrQuilometragem, idFilial, idMeioTransporte, idReboque, idControleCarga, nrControleCarga, obSaida, sgFilialCC, f, today, isGeracaoAutomatica, true);
	}
	
	public void executeEventsAndStoreConfirmaSaidaSemGeracaoEventosVolume(Boolean blPortaria,
			Boolean blSaida, Boolean blVirouHodometro, Integer nrQuilometragem,
			Long idFilial, Long idMeioTransporte, Long idReboque,
			Long idControleCarga, Long nrControleCarga, String obSaida, String sgFilialCC, Filial f, DateTime today, Boolean isGeracaoAutomatica) {
		executeEventsAndStoreConfirmaSaida(blPortaria, blSaida, blVirouHodometro, nrQuilometragem, idFilial, idMeioTransporte, idReboque, idControleCarga, nrControleCarga, obSaida, sgFilialCC, f, today, isGeracaoAutomatica, false);
	}

	private void executeEventsAndStoreConfirmaSaida(Boolean blPortaria,
			Boolean blSaida, Boolean blVirouHodometro, Integer nrQuilometragem,
			Long idFilial, Long idMeioTransporte, Long idReboque,
			Long idControleCarga, Long nrControleCarga, String obSaida, String sgFilialCC, Filial f, DateTime today, Boolean isGeracaoAutomatica, boolean geraEventosVolume) {
		//LMS-4124
		if (idControleCarga != null) {
			ControleCarga cc = controleCargaService.findById(idControleCarga);

			if (!cc.getTpStatusControleCarga().getValue().equals("AE") && !cc.getTpStatusControleCarga().getValue().equals("AV")) {
				// Msg que será checada na tela para fazer o que o jira solicita
				throw new BusinessException("ERRO");
			}
		}
		//

		/*Coleta/Entrega e filial configurada para solicitar quilometragem na portaria */

		if (idControleCarga != null && !isGeracaoAutomatica) {
			//TODO - LMS-4572
			//Validar se já existe lançamento de saída na portaria, caso não retorne registros, o método storeInformarQuilometragemMeioTransporte deverá ser utilizado
			boolean isExisteLancamentoQuilometragem = controleCargaService.findLancamentoQuilometragem(idControleCarga, idFilial, Boolean.TRUE);
			
			if (!isExisteLancamentoQuilometragem){
				if (BooleanUtils.isTrue(f.getBlInformaKmPortaria())){
					controleQuilometragemService.storeInformarQuilometragemMeioTransporte(idFilial, blPortaria, idMeioTransporte, blSaida,	nrQuilometragem, blVirouHodometro, idControleCarga, null, obSaida);
				}	
			}			
		}

		/* Veiculos associados a controle de carga */
		boolean ajusteTMS = false;
		if (idControleCarga != null) {
			String controleCarga = FormatUtils.formatSgFilialWithLong(sgFilialCC, nrControleCarga);

			// Eventos de coleta Aero
			pedidoColetaService.generateEventosColetaAeroByIdControleCarga(idControleCarga, ConstantesSim.EVENTO_ROTA_COLETA_AERO, (Short[]) null, f.getIdFilial(), controleCarga, CONTROLE_CARGA);

			// Geracao de evento de meio de transporte
			EventoMeioTransporte emt = new EventoMeioTransporte();

			ControleCarga cc = new ControleCarga();
			cc.setIdControleCarga(idControleCarga);
			emt.setControleCarga(cc);

			MeioTransporte mt = new MeioTransporte();
			mt.setIdMeioTransporte(idMeioTransporte);
			emt.setMeioTransporte(mt);

			emt.setTpSituacaoMeioTransporte(new DomainValue("EMCE"));

			emt.setDhInicioEvento(today);
			emt.setFilial(f);

			// cria evento do meio de transporte
			eventoMeioTransporteService.generateEvent(emt);

			// cria evento do semi-reboque
			if (idReboque != null) {
				mt.setIdMeioTransporte(idReboque);
				eventoMeioTransporteService.generateEvent(emt);
			}

			// cria evento para p/ controle de carga
			createRegistroEventoControleCarga(idControleCarga, idFilial, idMeioTransporte);

			String filialDestino = null;
			String tpNovoStatusManifesto = null;

			tpNovoStatusManifesto = "TC";

			// Manifestos associados ao controle de carga
			List<Manifesto> manifestos = findManifestoEntrega(idControleCarga);
			for (Manifesto manifesto : manifestos) {
				String tpStatusManifesto = manifesto.getTpStatusManifesto().getValue();
				if (!(tpStatusManifesto.equals("ME") || tpStatusManifesto.equals("EF"))) {
					continue;
				}

				// LMS-4909
				if (ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM.equals(manifesto.getTpManifesto().getValue()) && "ME".equals(tpStatusManifesto)) {
					generaterPreAlertaManifesto(manifesto);
				}

				// Código do evento
				Short cdEvento;
				if (manifesto.getTpManifesto().getValue().equals("V")) {
					cdEvento = ConstantesSim.EVENTO_SAIDA_PORTARIA_DESL_EMBAQ;
				} else {
					cdEvento = ConstantesSim.EVENTO_SAIDA_PORTARIA_EMROTA;
				}

				/** Otimização LMS-817 */
	    		final ProjectionList projection = Projections.projectionList()
			    		.add(Projections.property("ds.id"), "idDoctoServico");
					// Documentos de servico associados ao manifestos
				final List<DoctoServico> doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection);
				for (DoctoServico doctoServico : doctoServicoList) {
					if (validateObservacaoAereo(doctoServico, manifesto)) {
						filialDestino = configuracoesFacade.getMensagem("aeroporto");
					}

					// gera evento para o documento
					// LMSA-6607: ernani.brandao
					incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
							cdEvento,
							doctoServico.getIdDoctoServico(),
							idFilial,
							controleCarga,
							today,
							null,
							filialDestino,
							CONTROLE_CARGA
					);
				}
				
				if(geraEventosVolume) {
					eventoVolumeService.generateEventoByManifesto(manifesto.getIdManifesto(), cdEvento, ConstantesSim.TP_SCAN_LMS); 
					eventoDispositivoUnitizacaoService.generateEventoByManifesto(manifesto.getIdManifesto(), cdEvento, ConstantesSim.TP_SCAN_LMS, null);
				}

				/*Atualiza o status do manifesto para FE caso o mesmo possua a filial de destino TMS*/
				String tpStatus = tpNovoStatusManifesto;
				if (manifesto.getFilialByIdFilialDestino().getTpSistema() != null && manifesto.getFilialByIdFilialDestino().getTpSistema().getValue().equals("TMS")) {
					ajusteTMS = true;
					tpStatus = "FE";
				}
				manifesto.setTpStatusManifesto(new DomainValue(tpStatus));
				manifestoService.storeManifesto(manifesto);

			}

		}

		// Meio de transporte associado a um controle de carga de coleta/entrega
		if (idControleCarga != null) {
			ControleCarga cc = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

			/* Lista de manifestos vinculados ao controle de carga */
			List<Manifesto> manifestos = findManifestoEntrega(idControleCarga);

			/*Verifica se os manifestos do controle de carga possuem o status FECHADO
			para após alterar o status do controle de carga*/
			boolean fechaControleCarga = true;
			for (Manifesto manifesto : manifestos) {
				if (!manifesto.getTpStatusManifesto().getValue().equals("FE")) {
					fechaControleCarga = false;
					break;
				}
			}

			/*Altera o status do contole de carga para FE - Fechado quando possuir manifestos com destino
			a filiais TMS . Todos os manifestos devem possuir o status FE*/
			if (fechaControleCarga && ajusteTMS) {
				if (cc.getFilialByIdFilialDestino().getTpSistema() != null && cc.getFilialByIdFilialDestino().getTpSistema().getValue().equals("TMS")) {
					cc.setTpStatusControleCarga(new DomainValue("FE"));
				}
			}

			cc.setDhSaidaColetaEntrega(today);

			// Se todos os manifestos de entrega vinculados ao Controle de Cargas forem do tipo Entrega Parceira e não estejam cancelados
			boolean encerraMDFE = true;
			for (Manifesto manifesto:manifestos){
				if (!manifesto.getTpManifestoEntrega().getValue().equals("EP") || 
						manifesto.getTpStatusManifesto().getValue().equals("CA")){
					encerraMDFE = false;
					break;
				}
			}

			if (encerraMDFE) {
				controleCargaService.executeEncerrarMdfesAutorizados(idControleCarga);
			}

			// LMS-5184
			// verifica se existe algum manifesto do tipo rodoviário no controle de carga
			for (Manifesto manifesto : manifestos) {
				if (manifesto.getTpModal().getValue().equals("R")) {
					// LMS-4463
					if (filialRotaCcService.verificaFilialAntecedePorto(cc.getIdControleCarga(), idFilial)) {
						controleCargaService.generateEventoChangeStatusControleCarga(cc.getIdControleCarga(), idFilial, "TP");

					}
					// LMS-8141
					break;

				}
			}

			/* Salva o controle de carga */
			controleCargaService.store(cc);
		}
	}

	private Boolean validateObservacaoAereo(DoctoServico doctoServico, Manifesto manifesto) {
		List<Manifesto> l = manifestoService.findManifestoEntregaAereoFilialOrigem(doctoServico.getIdDoctoServico(), manifesto.getIdManifesto());
		if (!l.isEmpty()) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	/**
	 * Rotina gerarPreAlertaManifesto
	 * @param manifesto
	 */
	@SuppressWarnings("unchecked")
	private void generaterPreAlertaManifesto(Manifesto manifesto) {

		List<DoctoServico> doctoServicoAereoList = new ArrayList<DoctoServico>();

		List<PreManifestoDocumento> documentos = preManifestoDocumentoService.findPreManifestoDocumentos(manifesto.getIdManifesto(), ConstantesExpedicao.CONHECIMENTO_NACIONAL, ConstantesExpedicao.CONHECIMENTO_ELETRONICO);
		for (PreManifestoDocumento preManifestoDocumento : documentos) {
			if(preManifestoDocumento.getDoctoServico().getServico().getTpModal() != null && 
					preManifestoDocumento.getDoctoServico().getServico().getTpModal().getValue().equals(ConstantesExpedicao.MODAL_AEREO)){
				doctoServicoAereoList.add(preManifestoDocumento.getDoctoServico());
			}
		}

		if (!doctoServicoAereoList.isEmpty()) {
			ordenarListaDoctoServicoAereo(doctoServicoAereoList);
			gerarEmailContatosAereosFilialDestinoManifesto(manifesto, manifesto.getManifestoViagemNacional(), doctoServicoAereoList);
		}
	}

	@SuppressWarnings("unchecked")
	private void gerarEmailContatosAereosFilialDestinoManifesto(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional, 
			List<DoctoServico> doctoServicoAereoList) {
		
		List<String> contatosAereos = new ArrayList<String>();
		if (manifesto.getFilialByIdFilialDestino().getPessoa() != null && manifesto.getFilialByIdFilialDestino().getPessoa().getContatosByIdPessoaContatado() != null) {
			contatosAereos = montarContatosAereos(manifesto.getFilialByIdFilialDestino().getPessoa().getContatosByIdPessoaContatado());
		}

		if(!contatosAereos.isEmpty()){
			sendEmail(getRemetenteLms(), contatosAereos, montarAssunto(manifesto, manifestoViagemNacional), 
					montarMensagemDestinoManifesto(manifesto, manifestoViagemNacional, doctoServicoAereoList));
		}
	}

	private void sendEmail(final String remetenteLms, final List<String> dsEmails, final String assunto, final String mensagem) {

		Mail mail = createMail(StringUtils.join(dsEmails.toArray(), ";"), remetenteLms, assunto, mensagem);

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {

		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);

		return mail;
	}

	private String getRemetenteLms() {
		String remetenteLms = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		return remetenteLms;
	}

	private List<String> montarContatosAereos(List<Contato> contatosByIdPessoaContatado) {
		List<String> contatosAereos = new ArrayList<String>();

		for (Contato contato : contatosByIdPessoaContatado) {
			if (ConstantesExpedicao.TIPO_CONTATO_AEREO.equalsIgnoreCase(contato.getTpContato().getValue())) {
				if (StringUtils.isNotBlank(contato.getDsEmail())) {
					contatosAereos.add(contato.getDsEmail());
				}
			}
		}
		return contatosAereos;
	}

	private void ordenarListaDoctoServicoAereo(List<DoctoServico> doctoServicoAereoList) {
		Collections.sort(doctoServicoAereoList, new Comparator<DoctoServico>() {
			@Override
			public int compare(DoctoServico ds1, DoctoServico ds2) {
				int valor = ds1.getFilialByIdFilialDestino().getSgFilial().compareTo(ds2.getFilialByIdFilialDestino().getSgFilial());
				if (valor == 0) {
					valor = ds1.getFilialByIdFilialOrigem().getSgFilial().compareTo(ds2.getFilialByIdFilialOrigem().getSgFilial());
					if (valor == 0) {
						valor = ds1.getNrDoctoServico().compareTo(ds2.getNrDoctoServico());
					}
				}
				return valor;
			}
		});
	}

	private String montarAssunto(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional) {
		StringBuilder assunto = new StringBuilder(configuracoesFacade.getMensagem("preAlertaEmbarqueCargaAerea"));
		assunto.append(" - ").append(configuracoesFacade.getMensagem("referenteManifesto")).append(" ").append(manifesto.getFilialByIdFilialOrigem().getSgFilial());
		assunto.append(" ").append(formatNrManifestoViagem(manifestoViagemNacional.getNrManifestoOrigem().longValue()));
		return assunto.toString();
	}

	private String montarMensagemDestinoManifesto(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional, List<DoctoServico> doctoServicoAereoList) {
		return montarMensagem(manifesto, manifestoViagemNacional, doctoServicoAereoList, "M");
	}

	private String montarMensagem(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional, List<DoctoServico> doctoServicoAereoList, String destino) {
		String textoEmailManifestoAereoTabela = "";
		String rodape = "";

		if ("D".equalsIgnoreCase(destino)) {
			textoEmailManifestoAereoTabela = "textoEmailManifestoAereoTabelaFilial";
			rodape = "avisoEmailDocumentoAereo";
		} else if ("M".equalsIgnoreCase(destino)) {
			textoEmailManifestoAereoTabela = "textoEmailManifestoAereoTabela";
			rodape = "avisoEmailManifestoAereo";
		}

		List<String> argumentosTextoTabela = new ArrayList<String>();
		argumentosTextoTabela.add(manifesto.getFilialByIdFilialOrigem().getSgFilial());
		argumentosTextoTabela.add(formatNrManifestoViagem(manifestoViagemNacional.getNrManifestoOrigem().longValue()));
		argumentosTextoTabela.add(manifesto.getFilialByIdFilialDestino().getSgFilial());

		StringBuilder assunto = new StringBuilder(configuracoesFacade.getMensagem("logisticsManagementSystem"));
		assunto.append(LINE_SEPARATOR);
		assunto.append(configuracoesFacade.getMensagem("preAlertaEmbarqueCargaAerea"));
		assunto.append(LINE_SEPARATOR);
		assunto.append(configuracoesFacade.getMensagem("data")).append(": ").append(JTDateTimeUtils.formatDateYearMonthDayToString(JTDateTimeUtils.getDataAtual()));

		assunto.append(LINE_SEPARATOR).append(LINE_SEPARATOR);

		assunto.append(configuracoesFacade.getMensagem(textoEmailManifestoAereoTabela, argumentosTextoTabela.toArray()));

		assunto.append(LINE_SEPARATOR).append(LINE_SEPARATOR);

		assunto.append(gerarTabelaDocumentos(doctoServicoAereoList));

		assunto.append(LINE_SEPARATOR).append(LINE_SEPARATOR);

		assunto.append(configuracoesFacade.getMensagem(rodape));

		return assunto.toString();
	}

	private String gerarTabelaDocumentos(List<DoctoServico> doctoServicoAereoList) {
		Integer colunaDocumento = 16;
		Integer colunaOtd = 10;
		Integer colunaVolume = 8;
		Integer colunaPeso = 10;
		Integer colunaServico = 8;

		for (DoctoServico doctoServico : doctoServicoAereoList) {
			if (doctoServico.getPsReal() != null && doctoServico.getPsReal().toString().length() > colunaPeso) {
				colunaPeso = doctoServico.getPsReal().toString().length();
			}
			if (doctoServico.getServico() != null && doctoServico.getServico().getDsServico().toString().length() > colunaServico) {
				colunaServico = doctoServico.getServico().getDsServico().toString().length();
			}
		}

		String texto;
		StringBuilder tabela = new StringBuilder();

		texto = configuracoesFacade.getMensagem("documento") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaDocumento));

		texto = configuracoesFacade.getMensagem("dpe") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaOtd));

		texto = configuracoesFacade.getMensagem("volumes") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaVolume));

		texto = configuracoesFacade.getMensagem("pesoKG") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaPeso));

		texto = configuracoesFacade.getMensagem("servico") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaServico));

		tabela.append(LINE_SEPARATOR);

		for (DoctoServico doctoServico : doctoServicoAereoList) {

			texto = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + formatNrDoctoServico(doctoServico.getNrDoctoServico()) 
					+ " " + doctoServico.getFilialByIdFilialDestino().getSgFilial() ;
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaDocumento));

			texto = JTDateTimeUtils.formatDateYearMonthDayToString(doctoServico.getDtPrevEntrega());
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaOtd));

			texto = alinharDireita(doctoServico.getQtVolumes().toString(), colunaVolume);
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaVolume));

			texto = alinharDireita(FormatUtils.formatDecimal("##,###.000", doctoServico.getPsReal(), true), colunaPeso);
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaPeso));

			texto = doctoServico.getServico().getDsServico().toString();
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaServico));

			tabela.append(LINE_SEPARATOR);

		}

		return tabela.toString();
	}

	private String alinharDireita(String formatQtdVolumes, Integer colunaVolume) {
		String texto = formatQtdVolumes;
		if (formatQtdVolumes.length() < colunaVolume) {
			for (int i = 0; i < colunaVolume - formatQtdVolumes.length(); i++) {
				texto = " " + texto;
			}
		}
		return texto;
	}

	private Object tabulacoesPorColuna(Integer colunaAtual, Integer maxColuna) {
		Integer paradaTab = (maxColuna + 8) / 8 * 8;
		Integer quantidadeTabs = (paradaTab / 8) - (colunaAtual / 8);

		StringBuilder tabs = new StringBuilder();
		for (int i = 0; i < quantidadeTabs; i++) {
			tabs.append("\t");
		}
		return tabs.toString();
	}

	private String formatNrManifestoViagem(Long nrManifestoOrigem) {
		DecimalFormat df = new DecimalFormat("00000000");
		return df.format(nrManifestoOrigem);
	}

	private String formatNrDoctoServico(Long nrDoctoServico) {
		DecimalFormat df = new DecimalFormat("00000000");
		return df.format(nrDoctoServico);
	}

	public void createRegistroEventoControleCarga(Long idControleCarga, Long idFilial, Long idMeioTransporte) {
		controleCargaService.generateEventoChangeStatusControleCarga(idControleCarga, idFilial, "SP", null, idMeioTransporte, null, null, null, null, null, null, null, null, null);
	}

	private List<Manifesto> findManifestoEntrega(Long idControleCarga) {
		return manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
	}

	/**
	 * @return Returns the dao.
	 */
	public SaidaChegadaDAO getDao() {
		return dao;
	}

	/**
	 * @param dao The dao to set.
	 */
	public void setDao(SaidaChegadaDAO dao) {
		this.dao = dao;
	}

	/**
	 * @param controleCargaService The controleCargaService to set.
	 */
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	/**
	 * @param filialService The filialService to set.
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
	 * @param controleQuilometragemService The controleQuilometragemService to set.
	 */
	public void setControleQuilometragemService(ControleQuilometragemService controleQuilometragemService) {
		this.controleQuilometragemService = controleQuilometragemService;
	}

	/**
	 * @param eventoMeioTransporteService The eventoMeioTransporteService to set.
	 */
	public void setEventoMeioTransporteService(EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}

	/**
	 * @param incluirEventosRastreabilidadeInternacionalService  The incluirEventosRastreabilidadeInternacionalService to set.
	 */
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	/**
	 * @param ordemSaidaService The ordemSaidaService to set.
	 */
	public void setOrdemSaidaService(OrdemSaidaService ordemSaidaService) {
		this.ordemSaidaService = ordemSaidaService;
	}

	/**
	 * @param controleEntSaidaTerceiroService The controleEntSaidaTerceiroService to set.
	 */
	public void setControleEntSaidaTerceiroService(ControleEntSaidaTerceiroService controleEntSaidaTerceiroService) {
		this.controleEntSaidaTerceiroService = controleEntSaidaTerceiroService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * @param manifestoService The manifestoService to set.
	 */
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	/**
	 * @param doctoServicoService The doctoServicoService to set.
	 */
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setFronteiraRapidaService(FronteiraRapidaService fronteiraRapidaService) {
		this.fronteiraRapidaService = fronteiraRapidaService;
	}

	public FronteiraRapidaService getFronteiraRapidaService() {
		return fronteiraRapidaService;
	}

	/**
	 * @param volumeNotaFiscalService the volumeNotaFiscalService to set
	 */
	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	/**
	 * @return the volumeNotaFiscalService
	 */
	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
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

	/**
	 * @param eventoVolumeService the eventoVolumeService to set
	 */
	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	/**
	 * @return the eventoVolumeService
	 */
	public EventoVolumeService getEventoVolumeService() {
		return eventoVolumeService;
	}

	/**
	 * @param eventoDispositivoUnitizacaoService the eventoDispositivoUnitizacaoService to set
	 */
	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	/**
	 * @return the eventoDispositivoUnitizacaoService
	 */
	public EventoDispositivoUnitizacaoService getEventoDispositivoUnitizacaoService() {
		return eventoDispositivoUnitizacaoService;
	}

	public AcaoIntegracaoEventosService getAcaoIntegracaoEventosService() {
		return acaoIntegracaoEventosService;
	}

	public void setAcaoIntegracaoEventosService(
			AcaoIntegracaoEventosService acaoIntegracaoEventosService) {
		this.acaoIntegracaoEventosService = acaoIntegracaoEventosService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setFilialRotaCcService(FilialRotaCcService filialRotaCcService) {
		this.filialRotaCcService = filialRotaCcService;
	}

	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
	}

	public void setPreManifestoDocumentoService(
			PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public DetalheColetaService getDetalheColetaService() {
		return detalheColetaService;
	}

	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}

	public CtoAwbService getCtoAwbService() {
		return ctoAwbService;
	}

	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

}
