package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.ServicoAdicionalCalculo;
import com.mercurio.lms.expedicao.model.ServicoGeracaoAutomatica;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalClienteDestinatario;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteDestinatarioService;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;

import br.com.tntbrasil.integracao.domains.expedicao.servicoadicional.DocumentoCalculoMensalServicoAdicionalDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

@Assynchronous
public class MonitoramentoServicosAdicionaisService extends CrudService<MonitoramentoDocEletronico, Long> {
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	private CalculoServicoAdicionalService calculoServicoAdicionalService;
	private ConfiguracoesFacade configuracoesFacade;
	private ConhecimentoService conhecimentoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private ServicoAdicionalClienteDestinatarioService servicoAdicionalClienteDestinatarioService;
	private ServicoGeracaoAutomaticaService servicoGeracaoAutomaticaService;
	private ParcelaPrecoService parcelaPrecoService;
	private ParametroGeralService parametroGeralService;
	private AgendamentoEntregaService agendamentoEntregaService;
	private IntegracaoJmsService integracaoJmsService;

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT);
	
	private static final String POR_ENTREGA = "P";
	private static final String MENSAL = "M";
	private static final String AGENDAMENTO_MENSAL = "E";
	private static final String PERMANENCIA_AGENDAMENTO = "A";
	private static final String RECEITA_PERDIDA = "R";
	private static final String AGENDAMENTO = "G";
	
	@AssynchronousMethod(name = "monitoramentoServicosAdicionaisService.executeCalculoMensalPermanencia", 
			type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeCalculoMensalPermanenciaNEW() {
		YearMonthDay dtImplantacao = getVlParametroYearMonthDay("DT_IMP_SA");
		YearMonthDay dtInicio = getVlParametroYearMonthDay("DT_EXEC_PERM_M");
		List<Conhecimento> doctos = conhecimentoService.findByCalculoMensalPermanencia(dtImplantacao, dtInicio);
		
		generateDocumentoCalculoMensalServicoAdicional(doctos, MENSAL);
		storeVlParametroYearMonthDay("DT_EXEC_PERM_M", JTDateTimeUtils.getDataAtual());
	}
	
	@AssynchronousMethod(name = "monitoramentoServicosAdicionaisService.executeCalculoMensalPermanenciaAgendamento", 
			type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeCalculoMensalPermanenciaAgendamentoNEW() {
		YearMonthDay dtInicio = getVlParametroYearMonthDay("DT_EXEC_PERM_AG_M");		
		List<Conhecimento> doctos = conhecimentoService.findByCalculoMensalPermanenciaAgendamento(dtInicio);
		
		generateDocumentoCalculoMensalServicoAdicional(doctos, AGENDAMENTO_MENSAL);
		storeVlParametroYearMonthDay("DT_EXEC_PERM_AG_M", JTDateTimeUtils.getDataAtual());
	}
	
	private void generateDocumentoCalculoMensalServicoAdicional (List<Conhecimento> conhecimentos, String tpCalculoMensal){
		if(conhecimentos != null && conhecimentos.size() > 0){
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.CALCULO_MENSAL_SERVICO_ADICIONAL);
			for(Conhecimento conhecimento : conhecimentos){
				DocumentoCalculoMensalServicoAdicionalDMN documentoCalculoDMN = new DocumentoCalculoMensalServicoAdicionalDMN();
				documentoCalculoDMN.setIdDocumento(conhecimento.getIdDoctoServico());
				documentoCalculoDMN.setTpExecucao(tpCalculoMensal);
				msg.addMsg(documentoCalculoDMN);
			}
			integracaoJmsService.storeMessage(msg);
		}
	}
	
	public void executeCalculoMensalServicosAdicionais(DocumentoCalculoMensalServicoAdicionalDMN documentoCalculoMensalDMN){
		Conhecimento conhecimento = conhecimentoService.findById(documentoCalculoMensalDMN.getIdDocumento());
		executeCalculoPermanenciaMensal(conhecimento, documentoCalculoMensalDMN.getTpExecucao(), true);
	}
	
	private void executeCalculoPermanenciaMensal(Conhecimento conhecimento, String tpExecucao, Boolean blFaturado) {
		String[] ocorrenciasInvalidas = String.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro(ConstantesFranqueado.PARAMETRO_OCORRENCIA_DOCUMENTO_CALCULO_PERMANENCIA)).split(";");
					
		String cdParcela = null;
		DateTime oldDate = null;			
		OcorrenciaPendencia ocorrenciaPendencia = null;
		List<OcorrenciaDoctoServico> ocorrencias = conhecimento.getOcorrenciaDoctoServicos();
		Boolean isOcorrenciaDocumentoValida = Boolean.TRUE;
		
		for(OcorrenciaDoctoServico ocorrencia : ocorrencias) {
			ocorrenciaPendencia = ocorrencia.getOcorrenciaPendenciaByIdOcorBloqueio();
			
			if(ArrayUtils.contains(ocorrenciasInvalidas, ocorrenciaPendencia.getCdOcorrencia().toString())){
				isOcorrenciaDocumentoValida = Boolean.FALSE;
			}
			
			if(oldDate == null || oldDate.isAfter(ocorrencia.getDhBloqueio())) {
				oldDate = ocorrencia.getDhBloqueio();
				Boolean blApreensao = ocorrenciaPendencia.getBlApreensao();
				if(blApreensao.equals(Boolean.TRUE) || ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(cdParcela)) {
					cdParcela = ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO;
				} else {
					cdParcela = ConstantesExpedicao.CD_ARMAZENAGEM;
				}
			}
		}
		
		if(AGENDAMENTO_MENSAL.equals(tpExecucao)) {
			cdParcela = ConstantesExpedicao.CD_ARMAZENAGEM;				
		}
	
		if(isOcorrenciaDocumentoValida){
			executeCalculoPermanencia(conhecimento, cdParcela, ocorrenciaPendencia, tpExecucao, blFaturado);			
		}
	}
	
	public void executeCalculoServicosAdicionais(EventoDocumentoServicoDMN eventoDocumentoServicoDMN){
		Conhecimento conhecimento = conhecimentoService.findById(eventoDocumentoServicoDMN.getIdDoctoServico());
		
		validateCalculoAgendamento(conhecimento);
		validateCalculoPermanencia(conhecimento);
		validateCalculoPermanenciaAgendamento(conhecimento);
	}
	
	public void validateCalculoAgendamento(Conhecimento conhecimento){
		String[] cdParcelasPreco = new String [] {ConstantesExpedicao.CD_AGENDAMENTO_COLETA};
		
		if(!hasServicoGeracaoAutomatica(conhecimento.getIdDoctoServico(), cdParcelasPreco, null)){	
			AgendamentoDoctoServico agendamento = agendamentoDoctoServicoService.findAgendamentoByIdDoctoServicoTpSituacao(conhecimento.getIdDoctoServico(), "F");
			if(agendamento!= null){
				Cliente consignatario = conhecimento.getClienteByIdClienteConsignatario();
				Cliente destinatario = conhecimento.getClienteByIdClienteDestinatario();
				if ((consignatario != null && Boolean.TRUE.equals(consignatario.getBlAgendamento())) ||
						(consignatario == null && Boolean.TRUE.equals(destinatario.getBlAgendamento()))) {
					if (validateDestinatario(conhecimento)) {
						executeCalcularSalvarAgendamento(conhecimento);
					}
				}	
			}
		}
	}
	
	public void validateCalculoPermanencia(Conhecimento conhecimento){
		String[] cdParcelasPreco = new String [] {ConstantesExpedicao.CD_ARMAZENAGEM, ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO};
		
		if(!hasServicoGeracaoAutomatica(conhecimento.getIdDoctoServico(), cdParcelasPreco, Boolean.TRUE)){		
			String[] ocorrenciasInvalidas = String.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro(ConstantesFranqueado.PARAMETRO_OCORRENCIA_DOCUMENTO_CALCULO_PERMANENCIA)).split(";");
			List<OcorrenciaDoctoServico> ocorrencias = conhecimento.getOcorrenciaDoctoServicos();
			
			Boolean isExecutaCalculo = Boolean.FALSE;
			OcorrenciaPendencia ultimaOcorrencia = null;
			String cdParcela = null;
			DateTime oldDate = null;
			
			for(OcorrenciaDoctoServico ocorrencia : ocorrencias) {
				YearMonthDay dtImplantacao = getVlParametroYearMonthDay("DT_IMP_SA");
				if(ocorrencia.getDhBloqueio() != null && ocorrencia.getDhBloqueio().toYearMonthDay().compareTo(dtImplantacao) >= 0){
					OcorrenciaPendencia ocorrenciaPendencia = ocorrencia.getOcorrenciaPendenciaByIdOcorBloqueio();
					if(ocorrenciaPendencia.getBlDescontaDpe()){
						AgendamentoEntrega agendamentoEntrega = agendamentoEntregaService.findAgendamentoEntregaByIdDoctoServicoDhEnvio(conhecimento.getIdDoctoServico(), ocorrencia.getDhBloqueio());
						if(!ArrayUtils.contains(ocorrenciasInvalidas, ocorrenciaPendencia.getCdOcorrencia().toString()) &&
								agendamentoEntrega == null){
							HashMap<String,Object> params = getOcorrenciapendenciaByDataCdParcela(ocorrencia, oldDate, ocorrenciaPendencia, cdParcela);
							if(params != null){
								ultimaOcorrencia = (OcorrenciaPendencia) params.get("ocorrenciaPendencia");
								cdParcela = (String) params.get("cdParcela");
								oldDate = (DateTime) params.get("oldDate");
								isExecutaCalculo = Boolean.TRUE;
							}
						}
					}else{
						isExecutaCalculo = Boolean.FALSE;
						break;
					}
				}
			}
			
			if(isExecutaCalculo){
			    List<EventoDocumentoServico> listEventoMercDisposicao = null;
			    
			    if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcela)){
			        listEventoMercDisposicao = eventoDocumentoServicoService.findEventoDoctoServico(conhecimento.getIdDoctoServico(), new Short[]{ConstantesSim.EVENTO_CARTA_MERCADORIA_DISPOSICAO});
			    }else if(ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(cdParcela)){
			        listEventoMercDisposicao = eventoDocumentoServicoService.findEventoDoctoServico(conhecimento.getIdDoctoServico(), new Short[]{ConstantesSim.EVENTO_COMUNICADO_APREENSAO_ENVIADO});
			    }
			    
			    if(CollectionUtils.isNotEmpty(listEventoMercDisposicao)){
			        executeCalculoPermanencia(conhecimento, cdParcela, ultimaOcorrencia, POR_ENTREGA, true);
			    }else{
			        executeCalculoPermanencia(conhecimento, cdParcela, ultimaOcorrencia, RECEITA_PERDIDA, false);
			    }
			}
		}
	}
	
    public void validateCalculoPermanenciaAgendamento(Conhecimento conhecimento) {
        String[] cdParcelasPreco = new String[]{ConstantesExpedicao.CD_ARMAZENAGEM};

        if (!hasServicoGeracaoAutomatica(conhecimento.getIdDoctoServico(), cdParcelasPreco, Boolean.TRUE)) {
            AgendamentoEntrega agendamentoEntrega = agendamentoEntregaService.findAgendamentoEntregaByIdDoctoServicoTpSituacaoAgendamento(conhecimento.getIdDoctoServico(), ConstantesEntrega.TP_SITUACAO_AGENDAMENTO_FECHADO);

            if (agendamentoEntrega != null) {
                executeCalculoPermanencia(conhecimento, ConstantesExpedicao.CD_ARMAZENAGEM, null, PERMANENCIA_AGENDAMENTO, true);
            }
        }
    }
	
	private boolean hasServicoGeracaoAutomatica (Long idConhecimento, String[] cdParcelasPreco, Boolean blFinalizado){
		List<ServicoGeracaoAutomatica> listServicoGercaoAutomatica = servicoGeracaoAutomaticaService.findByDoctoServicoParcelaPreco(idConhecimento, cdParcelasPreco, null);
		if(listServicoGercaoAutomatica != null && listServicoGercaoAutomatica.size() > 0){
			return true;
		}
		return false;
	}
	
	private HashMap<String,Object> getOcorrenciapendenciaByDataCdParcela (OcorrenciaDoctoServico ocorrencia, DateTime oldDate, OcorrenciaPendencia ocorrenciaPendencia, String cdParcela){
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if(oldDate == null || oldDate.isAfter(ocorrencia.getDhBloqueio())) {
			params.put("oldDate", ocorrencia.getDhBloqueio());
			params.put("ocorrenciaPendencia", ocorrenciaPendencia);
			if(ocorrenciaPendencia.getBlApreensao().equals(Boolean.TRUE) || ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(cdParcela)) {
				params.put("cdParcela", ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO);
			}else{
				params.put("cdParcela", ConstantesExpedicao.CD_ARMAZENAGEM);
			}
			return params;
		}
		return null;
	}
	
	public void executeCancelarCalculoServicoAdicional(EventoDocumentoServicoDMN eventoDocumentoServicoDMN){
		Conhecimento conhecimento = conhecimentoService.findById(eventoDocumentoServicoDMN.getIdDoctoServico());
		DomainValue[] tpExecucao = new DomainValue [] {new DomainValue(AGENDAMENTO),
				new DomainValue(POR_ENTREGA),
				new DomainValue(PERMANENCIA_AGENDAMENTO),
				new DomainValue(RECEITA_PERDIDA)};
		List<ServicoGeracaoAutomatica> listServicoGercaoAutomatica = servicoGeracaoAutomaticaService.findByDoctoServicoTpExecucao(conhecimento.getIdDoctoServico(), tpExecucao);
		if(listServicoGercaoAutomatica != null && listServicoGercaoAutomatica.size() > 0){
			if(validateExecutaCancelamentoServicoGeracaoAutomatica(conhecimento.getEventoDocumentoServicos(), eventoDocumentoServicoDMN)){
				removeServicosGeracaoAutomatica(listServicoGercaoAutomatica);
			}
		}
	}

	private boolean validateExecutaCancelamentoServicoGeracaoAutomatica(List<EventoDocumentoServico> listEventosDocumento, EventoDocumentoServicoDMN eventoDocumentoServicoDMN){
		for(EventoDocumentoServico eventoDocto: listEventosDocumento){
			if((ConstantesSim.EVENTO_ENTREGA.equals(eventoDocto.getEvento().getCdEvento())||
				ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM.equals(eventoDocto.getEvento().getCdEvento())||
				ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM.equals(eventoDocto.getEvento().getCdEvento())) &&
					eventoDocumentoServicoDMN.getDhEvento().compareTo(eventoDocto.getDhEvento()) <= 0 && 
					BooleanUtils.isFalse(eventoDocto.getBlEventoCancelado())){
				return false;
			}
		}
		return true;
	}
	
	private void removeServicosGeracaoAutomatica(List<ServicoGeracaoAutomatica> listServicoGercaoAutomatica){
		for(ServicoGeracaoAutomatica servicoGeracaoAutomatica: listServicoGercaoAutomatica){
			servicoGeracaoAutomaticaService.removeServicoGeracaoAutomatica(servicoGeracaoAutomatica);
		}
	}
	
	@AssynchronousMethod(name = "monitoramentoServicosAdicionaisService.executeCalculoDiarioAgendamento", 
			type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeCalculoDiarioAgendamento() {		
		YearMonthDay dtEvento = getVlParametroYearMonthDay("DT_EXEC_AGEND");
		YearMonthDay dtExecucao = JTDateTimeUtils.getDataAtual();
		
		List<Conhecimento> doctos = conhecimentoService.findByCalculoAgendamento(dtEvento);
		for (Conhecimento conhecimento  : doctos) {
			Cliente consignatario = conhecimento.getClienteByIdClienteConsignatario();
			Cliente destinatario = conhecimento.getClienteByIdClienteDestinatario();
			if ((consignatario != null && Boolean.TRUE.equals(consignatario.getBlAgendamento())) || (consignatario == null && Boolean.TRUE.equals(destinatario.getBlAgendamento()))) {
				if (!validateDestinatario(conhecimento)) {
					continue;
				}
				executeCalcularSalvarAgendamento(conhecimento);
			}
		}
		storeVlParametroYearMonthDay("DT_EXEC_AGEND", dtExecucao);
	}

	private void executeCalcularSalvarAgendamento(Conhecimento conhecimento) {
		ServicoAdicionalCalculo servicoCalculo = new ServicoAdicionalCalculo();
		servicoCalculo.setCdParcela(ConstantesExpedicao.CD_AGENDAMENTO_COLETA);
		servicoCalculo.setVlFrete(conhecimento.getVlTotalDocServico());
		Long idDivisao = null;
		if (conhecimento.getDivisaoCliente() != null) {
			idDivisao = conhecimento.getDivisaoCliente().getIdDivisaoCliente();
		}
		
		calculoServicoAdicionalService.executeCalculo(conhecimento.getServico().getIdServico(), idDivisao, servicoCalculo, conhecimento.getIdDoctoServico());
		
		storeServicoGeracaoAutomaticaAgendamento(servicoCalculo.getServicoAdicionalPrecificado().getIdParcelaPreco(), conhecimento, servicoCalculo.getVlCalculado(), servicoCalculo.getVlTabela());
	}
	
	private void storeServicoGeracaoAutomaticaAgendamento(Long idParcelaPreco, Conhecimento conhecimento, BigDecimal vlServicoAdicional, BigDecimal vlTabela) {
		ServicoGeracaoAutomatica servicoGeracaoAutomatica = new ServicoGeracaoAutomatica();
		ParcelaPreco parcelaPreco = new ParcelaPreco();
		parcelaPreco.setIdParcelaPreco(idParcelaPreco);
		servicoGeracaoAutomatica.setParcelaPreco(parcelaPreco);
		servicoGeracaoAutomatica.setDoctoServico(conhecimento);		
		
		Boolean hasNegociacaoCliente = servicoAdicionalClienteService.validateNegociacaoCliente("IDAgendamentoColeta", conhecimento.getDivisaoCliente());
		servicoGeracaoAutomatica.setBlFaturado(!hasNegociacaoCliente);
        servicoGeracaoAutomatica.setBlSemCobranca(!hasNegociacaoCliente);
		
		servicoGeracaoAutomatica.setBlFinalizado(Boolean.TRUE);
		servicoGeracaoAutomatica.setDhCalculo(JTDateTimeUtils.getDataHoraAtual());
		servicoGeracaoAutomatica.setVlServicoAdicional(vlServicoAdicional);
		servicoGeracaoAutomatica.setVlTabela(vlTabela);
		
		servicoGeracaoAutomatica.setQtDiasCobrados(0);
		servicoGeracaoAutomatica.setMunicipioExecucao(conhecimento.getMunicipioByIdMunicipioEntrega());
		servicoGeracaoAutomatica.setFilialExecucao(conhecimento.getFilialByIdFilialDestino());
		servicoGeracaoAutomatica.setTpExecucao(new DomainValue(AGENDAMENTO));

		servicoGeracaoAutomaticaService.store(servicoGeracaoAutomatica);
	}


	private ServicoAdicionalCliente getServicoAdicionalCliente(Conhecimento conhecimento) {
		if (conhecimento.getTabelaPreco() != null && conhecimento.getDivisaoCliente() != null) {
			Long idTabelaPreco = conhecimento.getTabelaPreco().getIdTabelaPreco();
			Long idDivisaoCliente = conhecimento.getDivisaoCliente().getIdDivisaoCliente();
			
			List<ServicoAdicionalCliente> listaServicoAdicionalCliente = servicoAdicionalClienteService.findByTabelaDivisaoCliente(idTabelaPreco, idDivisaoCliente, ConstantesExpedicao.CD_AGENDAMENTO_COLETA);
			
			if (listaServicoAdicionalCliente != null && !listaServicoAdicionalCliente.isEmpty()) {
				return listaServicoAdicionalCliente.get(0);
			}
		}
		return null;
	}
	
	@AssynchronousMethod(name = "monitoramentoServicosAdicionaisService.executeCalculoDiarioPermanencia", 
			type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeCalculoDiarioPermanencia() {
		YearMonthDay dtImplantacao = getVlParametroYearMonthDay("DT_IMP_SA");
		YearMonthDay dtInicio = getVlParametroYearMonthDay("DT_EXEC_PERM");
		YearMonthDay dtExecucao = JTDateTimeUtils.getDataAtual();
		
		List<Conhecimento> doctos = conhecimentoService.findByCalculoDiarioPermanencia(dtImplantacao, dtInicio);
		executeCalculoPermanencia(doctos, POR_ENTREGA, true);			
		storeVlParametroYearMonthDay("DT_EXEC_PERM", dtExecucao);
		
		if (isDiaExecucaoMensal(dtExecucao)) {
			executeCalculoMensalPermanencia();
		}		
	}

	public void executeCalculoMensalPermanencia() {
		YearMonthDay dtImplantacao = getVlParametroYearMonthDay("DT_IMP_SA");	
		YearMonthDay dtInicio = getVlParametroYearMonthDay("DT_EXEC_PERM_M");
		List<Conhecimento> doctos = conhecimentoService.findByCalculoMensalPermanencia(dtImplantacao, dtInicio);
		executeCalculoPermanencia(doctos, MENSAL, true);
		
		storeVlParametroYearMonthDay("DT_EXEC_PERM_M", JTDateTimeUtils.getDataAtual());
	}
	
	@AssynchronousMethod(name = "monitoramentoServicosAdicionaisService.executeCalculoPermanenciaFatPerdido", 
			type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeCalculoPermanenciaFatPerdido() {
		YearMonthDay dtImplantacao = getVlParametroYearMonthDay("DT_IMP_SA");
		YearMonthDay dtInicio = getVlParametroYearMonthDay("DT_EXEC_PERM_FAT");
		YearMonthDay dtExecucao = JTDateTimeUtils.getDataAtual();
		
		List<Conhecimento> doctos = conhecimentoService.findByCalculoFatPerdidoPermanencia(dtImplantacao, dtInicio);
		executeCalculoPermanencia(doctos, RECEITA_PERDIDA, false);			
		storeVlParametroYearMonthDay("DT_EXEC_PERM_FAT", dtExecucao);
	}
	
	@AssynchronousMethod(name = "monitoramentoServicosAdicionaisService.executeCalculoDiarioPermanenciaAgendamento", 
			type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeCalculoDiarioPermanenciaAgendamento() {
		YearMonthDay dtInicio = getVlParametroYearMonthDay("DT_EXEC_PERM_AG");
		YearMonthDay dtExecucao = JTDateTimeUtils.getDataAtual();
		
		List<Conhecimento> doctos = conhecimentoService.findByCalculoDiarioPermanenciaAgendamento(dtInicio);
		executeCalculoPermanencia(doctos, PERMANENCIA_AGENDAMENTO, true);
		storeVlParametroYearMonthDay("DT_EXEC_PERM_AG", dtExecucao);
				
		if (isDiaExecucaoMensal(dtExecucao)) {
			executeCalculoMensalPermanenciaAgendamento();
		}
	}
	
	public void executeCalculoMensalPermanenciaAgendamento() {
		YearMonthDay dtInicio = getVlParametroYearMonthDay("DT_EXEC_PERM_AG_M");
		YearMonthDay dtExecucao = JTDateTimeUtils.getDataAtual();
		
		List<Conhecimento> doctos = conhecimentoService.findByCalculoMensalPermanenciaAgendamento(dtInicio);
		executeCalculoPermanencia(doctos, AGENDAMENTO_MENSAL, true);	
		storeVlParametroYearMonthDay("DT_EXEC_PERM_AG_M", dtExecucao);
	}
	
	private void executeCalculoPermanencia(List<Conhecimento> conhecimentos, String tpExecucao, Boolean blFaturado) {
		String[] ocorrenciasValidas = String.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro(ConstantesFranqueado.PARAMETRO_OCORRENCIA_DOCUMENTO_CALCULO_PERMANENCIA)).split(";");
		
		for(Conhecimento conhecimento : conhecimentos) {			
			String cdParcela = null;
			DateTime oldDate = null;			
			OcorrenciaPendencia ocorrenciaPendencia = null;
			List<OcorrenciaDoctoServico> ocorrencias = conhecimento.getOcorrenciaDoctoServicos();
			Boolean isOcorrenciaDocumentoValida = Boolean.FALSE;
			
			for(OcorrenciaDoctoServico ocorrencia : ocorrencias) {
				ocorrenciaPendencia = ocorrencia.getOcorrenciaPendenciaByIdOcorBloqueio();
				
				if(ArrayUtils.contains(ocorrenciasValidas, ocorrenciaPendencia.getCdOcorrencia().toString())){
					isOcorrenciaDocumentoValida = Boolean.TRUE;
				}
				
				if(oldDate == null || oldDate.isAfter(ocorrencia.getDhBloqueio())) {
					oldDate = ocorrencia.getDhBloqueio();
					Boolean blApreensao = ocorrenciaPendencia.getBlApreensao();
					if(blApreensao.equals(Boolean.TRUE) || ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(cdParcela)) {
						cdParcela = ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO;
					} else {
						cdParcela = ConstantesExpedicao.CD_ARMAZENAGEM;
					}
				}
			}
			
			if(PERMANENCIA_AGENDAMENTO.equals(tpExecucao) || AGENDAMENTO_MENSAL.equals(tpExecucao)) {
				cdParcela = ConstantesExpedicao.CD_ARMAZENAGEM;				
			}					
			
			if(BooleanUtils.isFalse(isOcorrenciaDocumentoValida)){
				executeCalculoPermanencia(conhecimento, cdParcela, ocorrenciaPendencia, tpExecucao, blFaturado);			
			}
		}
	}
	
	private void executeCalculoPermanencia(Conhecimento conhecimento, String cdParcela, 
			OcorrenciaPendencia ocorrenciaPendencia, String tpExecucao, Boolean blFaturado) {
		Long idDocto = conhecimento.getIdDoctoServico();
		Long idTabelaPreco = null;
		if(conhecimento.getTabelaPreco() != null) {
			idTabelaPreco = conhecimento.getTabelaPreco().getIdTabelaPreco();
		}
		Long idDivisaoCliente =  null;
		if(conhecimento.getDivisaoCliente() != null) {
			idDivisaoCliente = conhecimento.getDivisaoCliente().getIdDivisaoCliente();
		}
		Long idServico = conhecimento.getServico().getIdServico();
		
        DateTime dtInicioLiberacao = getMaxDhCalculoAutomatico(idDocto, cdParcela);
		YearMonthDay dtInicio = toYearMonthDay(dtInicioLiberacao);
		YearMonthDay dtFim = JTDateTimeUtils.getDataAtual();
		Integer qtDias = 0;
		Integer qtDiasCobranca = 0;
		Integer qtDiasCarencia = 0;
		
		ServicoAdicionalCliente servico = null;
		if(idTabelaPreco != null && idDivisaoCliente != null) {
			servico = servicoAdicionalClienteService.findUniqueByTabelaDivisaoCliente(
					idTabelaPreco, idDivisaoCliente, cdParcela);					
		}
		
		/* Verifica se a cobranÃ§a deve ser MENSAL para o cliente tomador */
		if(MENSAL.equals(tpExecucao) && (servico == null || !MENSAL.equals(servico.getTpFormaCobranca().getValue()))) {
			return;
		}
		
        if (dtInicio == null) {
            if (PERMANENCIA_AGENDAMENTO.equals(tpExecucao) || AGENDAMENTO_MENSAL.equals(tpExecucao)) {
                dtInicioLiberacao = getMinDhEnvioAgendamento(idDocto);
                dtInicio = toYearMonthDay(dtInicioLiberacao);

                // Se não encontrar nenhum evento de agendamento válido, então cancelar o cálculo.
                if (dtInicioLiberacao == null) {
                    return;
                }
            } else {
                dtInicioLiberacao = getMinDhEnvioCartaMercDispApres(idDocto, cdParcela);

                // Se não possuir carta de mercadoria, não deve executar o cálculo.
                if (POR_ENTREGA.equals(tpExecucao) && dtInicioLiberacao == null) {
                    return;
                }

                YearMonthDay dataPrevisao = conhecimento.getDtPrevEntrega();

                if (dtInicioLiberacao == null) {
                    dtInicio = dataPrevisao;
                } else {
                    dtInicio = getMax(dataPrevisao, toYearMonthDay(dtInicioLiberacao));
                }

                dtInicioLiberacao = toDateTime(dtInicio);
            }

            qtDiasCarencia = getQtDiasCarencia(servico, cdParcela);
        }
        
		if(!AGENDAMENTO_MENSAL.equals(tpExecucao) && !MENSAL.equals(tpExecucao)) {
            if (PERMANENCIA_AGENDAMENTO.equals(tpExecucao)) {
                dtFim = toYearMonthDay(getDhEventoFinalizador(idDocto));
            } else {
                dtFim = toYearMonthDay(getMinDhOcorrenciaLiberacaoAfterDtInicio(idDocto, dtInicioLiberacao));
                dtFim = dtFim == null ? toYearMonthDay(getDhEventoFinalizador(idDocto)) : dtFim;
            }
            
            if (dtFim == null) {
                return;
            }
		}
		
		qtDias = Days.daysBetween(dtInicio, dtFim).getDays();
		
		if(qtDias.compareTo(qtDiasCarencia) > 0) {
			if(getBlCobrancaRetroativa(servico)) {
				qtDiasCobranca = qtDias;
			} else {
				qtDiasCobranca = qtDias - qtDiasCarencia;
			}
			
			YearMonthDay dtInicioOcorrencias = dtFim.minusDays(qtDiasCobranca);
			List<OcorrenciaDoctoServico> listaOcorrencias = ocorrenciaDoctoServicoService
					.findBloqueioDoctoServicoPorPeriodoLiberacao(conhecimento.getIdDoctoServico(), dtInicioOcorrencias, dtFim);
			if ((POR_ENTREGA.equals(tpExecucao) || MENSAL.equals(tpExecucao)) && listaOcorrencias != null && listaOcorrencias.size() > 1) {
				Long idFilialQuebra = null;
				Long idEmpresaMercurio = ((BigDecimal) configuracoesFacade.getValorParametro("ID_EMPRESA_MERCURIO")).longValue();

				YearMonthDay dtInicinalPermanencia = null;
				YearMonthDay dtFinalPermanencia = dtFim;
				Boolean blPagaSeguroPermanencia = Boolean.TRUE;
				Municipio municipioExecucao = null;
				Filial filialExecucao = null;
				OcorrenciaPendencia ocorrenciaPendenciaGrupo = null;
				for (OcorrenciaDoctoServico ocorrenciaDoctoServico : listaOcorrencias) {
					Filial filialBloqueio = ocorrenciaDoctoServico.getFilialByIdFilialBloqueio();
					Long idFilialBloqueio = filialBloqueio.getIdFilial();
					if (idEmpresaMercurio.compareTo(idFilialBloqueio) == 0) {
						idFilialBloqueio = conhecimento.getFilialByIdFilialDestino().getIdFilial();
					}
					
					if (idFilialQuebra == null) {
						idFilialQuebra = idFilialBloqueio;
					}
					
					if (idFilialQuebra.compareTo(idFilialBloqueio) != 0) {
						qtDiasCobranca = Days.daysBetween(dtInicinalPermanencia, dtFinalPermanencia).getDays();
						//salva a cada mudanca
						executeCalculoAndStore(conhecimento, cdParcela, ocorrenciaPendenciaGrupo, tpExecucao,
								idDivisaoCliente, idServico, qtDiasCobranca, blPagaSeguroPermanencia, municipioExecucao, 
								filialExecucao, blFaturado, dtInicinalPermanencia, dtFinalPermanencia, qtDiasCarencia);
						blPagaSeguroPermanencia = Boolean.FALSE;
						dtFinalPermanencia = dtInicinalPermanencia;
						idFilialQuebra = idFilialBloqueio;
					}
					dtInicinalPermanencia = getMax(dtInicioOcorrencias, toYearMonthDay(ocorrenciaDoctoServico.getDhBloqueio()));;
					municipioExecucao = filialBloqueio.getPessoa().getEnderecoPessoa().getMunicipio();
					filialExecucao = filialBloqueio;
					ocorrenciaPendenciaGrupo = ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio();
					
				}
				//salva o ultimo
				qtDiasCobranca = Days.daysBetween(dtInicinalPermanencia, dtFinalPermanencia).getDays();
				executeCalculoAndStore(conhecimento, cdParcela, ocorrenciaPendenciaGrupo, tpExecucao, idDivisaoCliente, 
						idServico, qtDiasCobranca, blPagaSeguroPermanencia, municipioExecucao, filialExecucao, blFaturado, 
						dtInicinalPermanencia, dtFinalPermanencia, qtDiasCarencia);
			
			} else {
				Municipio municipioExecucao = null;
				Filial filialExecucao = null;
				if (POR_ENTREGA.equals(tpExecucao) || MENSAL.equals(tpExecucao)) {
					if (listaOcorrencias != null && !listaOcorrencias.isEmpty()) {
						OcorrenciaDoctoServico ocorrenciaDoctoServico = listaOcorrencias.get(0);
						Filial filialBloqueio = ocorrenciaDoctoServico.getFilialByIdFilialBloqueio();
						municipioExecucao = filialBloqueio.getPessoa().getEnderecoPessoa().getMunicipio();
						filialExecucao = filialBloqueio;
					}
				}
                
                if (municipioExecucao == null) {
                    municipioExecucao = conhecimento.getMunicipioByIdMunicipioEntrega();
                }
                
                if (filialExecucao == null) {
                    filialExecucao = conhecimento.getFilialByIdFilialDestino();
                }
                
				executeCalculoAndStore(conhecimento, cdParcela, ocorrenciaPendencia, tpExecucao,
						idDivisaoCliente, idServico, qtDiasCobranca, Boolean.TRUE, municipioExecucao, 
						filialExecucao, blFaturado, dtInicio, dtFim, qtDiasCarencia);
			}
		}			
	}

    private DateTime getMaxDhOcorrenciaLiberacao(Long idDoctoServico) {
        List<EventoDocumentoServico> eventos = eventoDocumentoServicoService.findByDoctoServico(
				idDoctoServico,
				ConstantesSim.EVENTO_LIBERACAO);
        DateTime dhLiberacao = null;
        for(EventoDocumentoServico evento : eventos) {
			if(dhLiberacao == null || dhLiberacao.isAfter(evento.getDhEvento())) {
				dhLiberacao = evento.getDhEvento();
			}
		}
        return dhLiberacao;
    }
    
     public DateTime getMinDhOcorrenciaLiberacaoAfterDtInicio(Long idDoctoServico, DateTime dtInicio) {
        EventoDocumentoServico evento = eventoDocumentoServicoService.findFirstEventoByDoctoServicoAfterDtInicio(idDoctoServico, dtInicio, ConstantesSim.EVENTO_LIBERACAO);

        if (evento == null) {
            return null;
        }
        
        return evento.getDhEvento();
    }

	private void executeCalculoAndStore(Conhecimento conhecimento, String cdParcela,
			OcorrenciaPendencia ocorrenciaPendencia, String tpExecucao,
			Long idDivisaoCliente, Long idServico, Integer qtDiasCobranca, Boolean blPagaSeguro, 
			Municipio municipioExecucao, Filial filialExecucao, Boolean blFaturado, 
			YearMonthDay dtInicinalPermanencia, YearMonthDay dtFinalPermanencia, Integer qtDiasCarencia) {
		ServicoAdicionalCalculo calculo = executeCalculoServicoAdicional(
				idServico, idDivisaoCliente, cdParcela, qtDiasCobranca, conhecimento, blPagaSeguro);
		
		Long idParcela = calculo.getServicoAdicionalPrecificado().getIdParcelaPreco(); 
		
		storeServicoGeracaoAutomaticaPermanencia(
				idParcela, conhecimento, ocorrenciaPendencia, tpExecucao, calculo.getVlCalculado(), calculo.getVlTabela(), qtDiasCobranca, 
				municipioExecucao, filialExecucao, blFaturado, cdParcela, dtInicinalPermanencia, 
				dtFinalPermanencia, qtDiasCarencia);
	}
	
	private DateTime getDhEventoFinalizador(Long idDoctoServico) {
		List<EventoDocumentoServico> eventos = eventoDocumentoServicoService.findByDoctoServico(
				idDoctoServico,
				ConstantesSim.EVENTO_ENTREGA,
				ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
				ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM);
		
		DateTime dhEvento = null;
		
		for(EventoDocumentoServico evento : eventos) {
			if(dhEvento == null || dhEvento.isAfter(evento.getDhEvento())) {
				dhEvento = evento.getDhEvento();
			}
		}
		
		return dhEvento;
	}
	
	private DateTime getMinDhEnvioCartaMercDispApres(Long idDoctoServico, String cdParcela) {
		List<EventoDocumentoServico> eventosMercDisposicaoCartaApreensao = null;
		DateTime minDhEnvioCartaMercDis = null;

		if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcela)){
		    eventosMercDisposicaoCartaApreensao = eventoDocumentoServicoService.findByDoctoServico(
		             idDoctoServico,  new Short[]{ConstantesSim.EVENTO_CARTA_MERCADORIA_DISPOSICAO});
        }else if(ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO.equals(cdParcela)){
            eventosMercDisposicaoCartaApreensao = eventoDocumentoServicoService.findByDoctoServico(
                     idDoctoServico,  new Short[]{ConstantesSim.EVENTO_COMUNICADO_APREENSAO_ENVIADO});
        }
		 
		if (CollectionUtils.isNotEmpty(eventosMercDisposicaoCartaApreensao)) {
		    for(EventoDocumentoServico evento : eventosMercDisposicaoCartaApreensao) {
		        if(ConstantesSim.EVENTO_CARTA_MERCADORIA_DISPOSICAO.compareTo(evento.getEvento().getCdEvento()) == 0) {
		            if(minDhEnvioCartaMercDis == null ||minDhEnvioCartaMercDis.isAfter(evento.getDhEvento())) {
		                minDhEnvioCartaMercDis = evento.getDhEvento();
						break;
					}
				}
				
				if(ConstantesSim.EVENTO_COMUNICADO_APREENSAO_ENVIADO.compareTo(evento.getEvento().getCdEvento()) == 0) {
				    if(minDhEnvioCartaMercDis == null || minDhEnvioCartaMercDis.isAfter(evento.getDhEvento())) {
				        minDhEnvioCartaMercDis = evento.getDhEvento();
	                    break;
	                }
				}
			}
		}
		
		return minDhEnvioCartaMercDis;
	}
	
	private DateTime getMinDhEnvioAgendamento(Long idDoctoServico) {
		List<AgendamentoDoctoServico> agendamentos = agendamentoDoctoServicoService.findNaoCanceladosByIdDoctoServico(idDoctoServico);
		DateTime minDhAgendamento = null;
        
        /**
         * AH = Agendamento por hora
         * AT = Agendamento por turno
         */
        String[] agendamentosValidos = new String[]{"AH", "AT"};
		
		for(AgendamentoDoctoServico agendamento : agendamentos) {
			AgendamentoEntrega agendamentoEntrega = agendamento.getAgendamentoEntrega();
            String tpAgendamento = agendamentoEntrega.getTpAgendamento().getValue();
                  
            // Serão considerados apenas agendamentos válidos.
            if (ArrayUtils.contains(agendamentosValidos, tpAgendamento)) {
                if (minDhAgendamento == null || minDhAgendamento.isAfter(agendamentoEntrega.getDhEnvio())) {
                    minDhAgendamento = agendamentoEntrega.getDhEnvio();
                }
            }
		}
		
		return minDhAgendamento;
	}
	
	private DateTime getMaxDhCalculoAutomatico(Long idDoctoServico, String cdParcela) {
		List<ServicoGeracaoAutomatica> geracoes = servicoGeracaoAutomaticaService.findByDoctoServicoParcelaPreco(idDoctoServico, new String [] {cdParcela}, null);
		DateTime maxDhCalculo = null;
		
		for(ServicoGeracaoAutomatica geracao : geracoes) {
			if(maxDhCalculo == null || maxDhCalculo.isBefore(geracao.getDhCalculo())) {
				maxDhCalculo = geracao.getDhCalculo();
			}
		}		
		
		return maxDhCalculo;
	}
	
	private YearMonthDay toYearMonthDay(DateTime dt) {
		if(dt != null) {
			return new YearMonthDay(
					dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
		}
		return null;
	}
    
    private DateTime toDateTime(YearMonthDay ymd) {
        if (ymd == null) {
            return null;
        }

        return new DateTime(ymd.getYear(), ymd.getMonthOfYear(), ymd.getDayOfMonth(), 0, 0, 0, 0);
    }
    
	private YearMonthDay getMax(YearMonthDay dt1, YearMonthDay dt2) {
		if(dt1 != null && dt2 != null && dt2.isBefore(dt1)) {
			return dt1;
		} else if(dt1 != null && dt2 != null && dt1.isBefore(dt2)) {
			return dt2;
		} else  {
			return (dt1==null?dt2:dt1);
		} 
	}
	
	private Boolean validateDestinatario(Conhecimento conhecimento) {
		if (conhecimento.getTabelaPreco() != null && conhecimento.getDivisaoCliente() != null) {
			Long idTabelaPreco = conhecimento.getTabelaPreco().getIdTabelaPreco();
			Long idDivisaoCliente = conhecimento.getDivisaoCliente().getIdDivisaoCliente();
			
			List<ServicoAdicionalCliente> listaServicoAdicionalCliente = servicoAdicionalClienteService.findByTabelaDivisaoCliente(idTabelaPreco, idDivisaoCliente, ConstantesExpedicao.CD_AGENDAMENTO_COLETA);
			ServicoAdicionalCliente servicoAdicionalCliente = null;
			ServicoAdicionalClienteDestinatario servicoAdicionalClienteDestinatario = null;
			if (listaServicoAdicionalCliente != null && !listaServicoAdicionalCliente.isEmpty()) {
				servicoAdicionalCliente = listaServicoAdicionalCliente.get(0);
			
				if (Boolean.FALSE.equals(servicoAdicionalCliente.getBlPagaParaTodos())) {
					//Para Agendamento sempre deve ter um destinatrario.
					Long idClienteDestinatario = conhecimento.getClienteByIdClienteDestinatario().getIdCliente();
					if (conhecimento.getClienteByIdClienteConsignatario() != null) {
						idClienteDestinatario = conhecimento.getClienteByIdClienteConsignatario().getIdCliente();
					}
					servicoAdicionalClienteDestinatario = servicoAdicionalClienteDestinatarioService.findByIdServicoAdicionalClienteByIdClienteDestinatario(servicoAdicionalCliente.getIdServicoAdicionalCliente(), idClienteDestinatario);
					if (servicoAdicionalClienteDestinatario == null) {
						return Boolean.FALSE;
					}
				}
			}
		}
		
		return Boolean.TRUE;
	}
	
	private void storeServicoGeracaoAutomaticaPermanencia(Long idParcela, Conhecimento conhecimento, 
			OcorrenciaPendencia ocorrenciaPendencia, String tpExecucao, BigDecimal vlServicoAdicional, BigDecimal vlTabela, 
			Integer qtDiasCobranca, Municipio municipioExecucao, Filial filialExecucao, Boolean blFaturado, String cdParcela,
			YearMonthDay dtInicinalPermanencia, YearMonthDay dtFinalPermanencia, Integer qtDiasCarencia) {
		ParcelaPreco parcela = new ParcelaPreco();
		parcela.setIdParcelaPreco(idParcela);
		
		ServicoGeracaoAutomatica servicoGeracaoAutomatica = new ServicoGeracaoAutomatica();
		servicoGeracaoAutomatica.setDoctoServico(conhecimento);
		servicoGeracaoAutomatica.setParcelaPreco(parcela);
		
		if(RECEITA_PERDIDA.equals(tpExecucao)) {
			servicoGeracaoAutomatica.setBlFaturado(Boolean.TRUE);
			servicoGeracaoAutomatica.setBlSemCobranca(Boolean.TRUE);
		} else if (blFaturado){
			Boolean hasNegociacaoCliente = servicoAdicionalClienteService.validateNegociacaoCliente(cdParcela, conhecimento.getDivisaoCliente());
			servicoGeracaoAutomatica.setBlFaturado(!hasNegociacaoCliente);
	        servicoGeracaoAutomatica.setBlSemCobranca(!hasNegociacaoCliente);
			
		} else {
			servicoGeracaoAutomatica.setBlFaturado(Boolean.FALSE);
			servicoGeracaoAutomatica.setBlSemCobranca(Boolean.FALSE);
		}		
		
		if(MENSAL.equals(tpExecucao) || AGENDAMENTO_MENSAL.equals(tpExecucao)) {
			servicoGeracaoAutomatica.setBlFinalizado(Boolean.FALSE);
		} else {
			servicoGeracaoAutomatica.setBlFinalizado(Boolean.TRUE);
		}
		
		servicoGeracaoAutomatica.setDhCalculo(JTDateTimeUtils.getDataHoraAtual());
		servicoGeracaoAutomatica.setVlServicoAdicional(vlServicoAdicional);
		servicoGeracaoAutomatica.setVlTabela(vlTabela);
		servicoGeracaoAutomatica.setQtDiasCobrados(qtDiasCobranca);
		servicoGeracaoAutomatica.setMunicipioExecucao(municipioExecucao);
		servicoGeracaoAutomatica.setFilialExecucao(filialExecucao);
		servicoGeracaoAutomatica.setOcorrenciaPendencia(ocorrenciaPendencia);
		servicoGeracaoAutomatica.setTpExecucao(new DomainValue(tpExecucao));
		servicoGeracaoAutomatica.setDtInicioPermanencia(dtInicinalPermanencia);
		servicoGeracaoAutomatica.setDtFimPermanencia(dtFinalPermanencia);
		servicoGeracaoAutomatica.setNrDiasCarencia(qtDiasCarencia);
		
		servicoGeracaoAutomaticaService.store(servicoGeracaoAutomatica);
	}

	private ServicoAdicionalCalculo executeCalculoServicoAdicional(Long idServico, Long idDivisaoCliente,
			String cdParcela, Integer qtDiasCobranca, Conhecimento conhecimento, Boolean blPagaSeguro) {
		ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();
		calculo.setCdParcela(cdParcela);
		calculo.setQtDias(qtDiasCobranca);
		calculo.setPsReferencia(conhecimento.getPsReferenciaCalculo());
		calculo.setVlMercadoria(conhecimento.getVlMercadoria());
		calculo.setBlPagaSeguro(blPagaSeguro);
		calculoServicoAdicionalService.executeCalculo(idServico, idDivisaoCliente, calculo, conhecimento.getIdDoctoServico());
		return calculo;
	}
	
	protected YearMonthDay getVlParametroYearMonthDay(String parametro) {
		String date = (String) parametroGeralService.findSimpleConteudoByNomeParametro(parametro);
                try{
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(SIMPLE_DATE_FORMAT.parse(date));
                    return new YearMonthDay(gc.get(GregorianCalendar.YEAR), gc.get(GregorianCalendar.MONTH)+1, gc.get(GregorianCalendar.DAY_OF_MONTH));
                }catch(Exception e){
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e.getMessage(), e);
                }
	}
	
	private void storeVlParametroYearMonthDay(String parametro, YearMonthDay date) {				
		configuracoesFacade.storeValorParametro(parametro, date.toString(DATE_FORMAT));
	}
	
	private Integer getQtDiasCarencia(ServicoAdicionalCliente servico, String cdParcela) {
		Integer qtDiasCarencia = null;
		
		if(servico == null || servico.getNrQuantidadeDias() == null) {
			if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcela)) {
				qtDiasCarencia = ((BigDecimal) configuracoesFacade.getValorParametro("CARENCIA_ARM")).intValue();
			} else {
				qtDiasCarencia = ((BigDecimal) configuracoesFacade.getValorParametro("CARENCIA_DEP")).intValue();
			}
		} else {
			qtDiasCarencia = servico.getNrQuantidadeDias();
		}
		
		return qtDiasCarencia;
	}
	
	private Boolean getBlCobrancaRetroativa(ServicoAdicionalCliente servico) {
		if(servico == null || Boolean.TRUE.equals(servico.getBlCobrancaRetroativa())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}			
	}
	
	private Boolean isDiaExecucaoMensal(YearMonthDay dtExecucao) {
		Integer diaProcessamentoDoMes = ((BigDecimal) configuracoesFacade.getValorParametro("DIA_PROC_MES_SA")).intValue();
		if (dtExecucao.getDayOfMonth() == diaProcessamentoDoMes.intValue()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public void setAgendamentoDoctoServicoService(
			AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}
	
	public void setAgendamentoEntregaService(
			AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}

	public void setCalculoServicoAdicionalService(
			CalculoServicoAdicionalService calculoServicoAdicionalService) {
		this.calculoServicoAdicionalService = calculoServicoAdicionalService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setServicoAdicionalClienteService(
			ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}

	public void setServicoAdicionalClienteDestinatarioService(
			ServicoAdicionalClienteDestinatarioService servicoAdicionalClienteDestinatarioService) {
		this.servicoAdicionalClienteDestinatarioService = servicoAdicionalClienteDestinatarioService;
	}

	public void setServicoGeracaoAutomaticaService(
			ServicoGeracaoAutomaticaService servicoGeracaoAutomaticaService) {
		this.servicoGeracaoAutomaticaService = servicoGeracaoAutomaticaService;
	}

	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	
	public ParcelaPrecoService getParcelaPrecoService() {
		return parcelaPrecoService;
	}
	
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}
