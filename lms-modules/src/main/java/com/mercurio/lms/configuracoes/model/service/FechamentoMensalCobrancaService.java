package com.mercurio.lms.configuracoes.model.service;

import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.jms.HeaderParam;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.utils.EventoSistemaLmsType;
import br.com.tntbrasil.integracao.domains.franqueados.FechamentoCalculoDMN;

import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.JTDateTimeUtils;


public class FechamentoMensalCobrancaService  {

	private RecursoMensagemService recursoMensagemService;
	private ParametroGeralService parametroGeralService;
	private IntegracaoJmsService integracaoJmsService;
	
	private static final String LMS_27125 = "LMS-27125";
	private static final String LMS_27123 =  "LMS-27123";
	private static final String IND_FECHTO_MENSAL_FINANCEIRO = "IND_FECHTO_MENSAL_FINANCEIRO";
	private static final String COMPETENCIA_FINANCEIRO = "COMPETENCIA_FINANCEIRO";
	private static final String GRUPO_FECHTO_MENSAL_COBRANCA = "GRUPO_FECHTO_MENSAL_COBRANCA";
	private static final String REMETENTE_EMAIL_LMS = "REMETENTE_EMAIL_LMS";
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private static final String FECHAMENTO_FRANQUEADOS = "FECHAMENTO_FRANQUEADOS";


	public void executeLiberarFechamento(){
		parametroGeralService.storeValorParametro(IND_FECHTO_MENSAL_FINANCEIRO, "S");
		ParametroGeral param = parametroGeralService.findByNomeParametro(COMPETENCIA_FINANCEIRO, false);
		YearMonthDay competencia = new YearMonthDay(param.getDsConteudo());
		
		generateEventoFechamento(competencia);
		
		competencia = virarCompetencia(param, competencia);
		
		parametroGeralService.storeValorParametro(IND_FECHTO_MENSAL_FINANCEIRO, "N");
		enviarEmailParaGrupoDeUsuarios(competencia);
		
	}
	
	public Boolean isFechamentoHabilitado() {
		YearMonthDay competencia = getParametroCompetencia();
		return isLiberarAcaoFechamento(competencia);
	}

	public String getCompetencia() {
		YearMonthDay competencia = getParametroCompetencia();
		return JTDateTimeUtils.getYearMonth(competencia);
		
	}
	
	private YearMonthDay virarCompetencia(ParametroGeral param, YearMonthDay competencia){
		YearMonthDay competenciaVirada = competencia.plusMonths(1);
		param.setDsConteudo(competenciaVirada.toString("yyyy-MM-dd"));
		parametroGeralService.store(param);
		return competencia;
	}
	
	private boolean isLiberarAcaoFechamento(YearMonthDay parametroCompetencia) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		return !comparaMesAno(parametroCompetencia, dataAtual);	
	}

	private boolean comparaMesAno(YearMonthDay parametroCompetencia, YearMonthDay dataAtual) {
		return dataAtual.getYear() == parametroCompetencia.getYear() && dataAtual.getMonthOfYear() == parametroCompetencia.getMonthOfYear();
	}
	
	private YearMonthDay getParametroCompetencia() {
		String paramCompetencia = parametroGeralService.findConteudoByNomeParametro(COMPETENCIA_FINANCEIRO, false).toString();
		YearMonthDay competencia = JTDateTimeUtils.convertDataStringToYearMonthDay(paramCompetencia,"yyyy-MM-dd");
		return competencia;
	}

	private void enviarEmailParaGrupoDeUsuarios(YearMonthDay competencia){

		String strTo = parametroGeralService.findConteudoByNomeParametro(GRUPO_FECHTO_MENSAL_COBRANCA, false).toString();
		String strFrom = parametroGeralService.findConteudoByNomeParametro(REMETENTE_EMAIL_LMS, false).toString();
		StringBuilder strSubject = new StringBuilder(recursoMensagemService.findByChave(LMS_27125));
		StringBuilder body = new StringBuilder(recursoMensagemService.findByChave(LMS_27123, new Object[]{JTDateTimeUtils.getYearMonth(competencia)}));
				
		Mail mail = createMail(strTo, strFrom, strSubject, body);
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, StringBuilder strSubject, StringBuilder body){

		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject.toString());
		mail.setBody(body.toString());

		return mail;
	}

	private void generateEventoFechamento (YearMonthDay competencia){
		FechamentoCalculoDMN fechamentoCalculoDMN = new FechamentoCalculoDMN();
		fechamentoCalculoDMN.setDtCompetencia(competencia);
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.EVENTO_SISTEMA_LMS, fechamentoCalculoDMN);
		msg.addHeader(HeaderParam.EVENT_TYPE.getName(), EventoSistemaLmsType.FECHAMENTO_FINANCEIRO.getName());
		integracaoJmsService.storeMessage(msg);
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}




}
