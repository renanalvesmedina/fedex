package com.mercurio.lms.services.franqueados;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.service.ProcessamentoCalculoFranqueadosService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;

import br.com.tntbrasil.integracao.domains.franqueados.CalculoFranquiaDMN;
import br.com.tntbrasil.integracao.domains.franqueados.TipoCalculoFranqueado;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

@Path("/franqueados/CalculoFranqueados")
public class CalculoFranqueadosRest {

	private static final long USUARIO_INTEGRACAO = 5000L;
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private static final String REMETENTE_EMAIL_LMS = "REMETENTE_EMAIL_LMS";
	private static final String GRUPO_FECHTO_MENSAL_FRANQUEADO = "GRUPO_FECHTO_MENSAL_FRANQUEADO";
	private static final String GRUPO_FECHTO_DIARIO_FRANQUEADO = "GRUPO_FECHTO_DIARIO_FRANQUEADO";
	private static final String LMS_46118 = "LMS-46118";
	private static final String LMS_46119 = "LMS-46119";
	private static final String LMS_46120 = "LMS-46120";
	private static final String LMS_46121 = "LMS-46121";
	private static final String LMS_46122 = "LMS-46122";

	private static final String SUCESSO = "SUCESSO";
	private static final String ERRO    = "ERRO";

	private static final Logger LOG = LogManager.getLogger(ProcessamentoCalculoFranqueadosService.LOG_NAME_CALCULO_FRANQUEADOS);

	@InjectInJersey
	private ProcessamentoCalculoFranqueadosService processamentoCalculoFranqueadosService;

	@InjectInJersey
	private UsuarioService usuario;

	@InjectInJersey
	private IntegracaoJmsService integracaoJmsService;

	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	@InjectInJersey
	private RecursoMensagemService recursoMensagemService;
	
	@InjectInJersey
	private FilialService filialService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	@POST
	@Path("franquiasCalculo")
	public Response franquiasCalculo(CalculoFranquiaDMN calculoFranquiaDMN) {
		String dataBase = DateTimeFormat.forPattern("dd/MM/yyyy").print(calculoFranquiaDMN.getDtBaseCalculo());
		logInfo("Inicio do Calculo Franqueado %s Data base %s", calculoFranquiaDMN.getTipoCalculo(), dataBase);
		executaEnvioFranquiasParaCalculo(calculoFranquiaDMN);
		return Response.ok().build();
	}
	
	@POST
	@Path("calculoFranqueado")
	public Response calculoFranqueado(CalculoFranquiaDMN calculoFranquiaDMN) {
		String sgFranquia = this.filialService.findSgFilialByIdFilial(calculoFranquiaDMN.getIdFranquia());
		try {
			logInfo("Iniciado o calculo da franquia %s, tipo de Calculo %s, base calculo %s", calculoFranquiaDMN.getIdFranquia(), calculoFranquiaDMN.getTipoCalculo(), calculoFranquiaDMN.getDtBaseCalculo());
			if (TipoCalculoFranqueado.DIARIO.equals(calculoFranquiaDMN.getTipoCalculo())) {
				Queue<Serializable> documentos = this.processamentoCalculoFranqueadosService.executeProcessamentoCalculoFranqueadosDiario(calculoFranquiaDMN.getDtBaseCalculo(), calculoFranquiaDMN.getIdFranquia());
				logInfo("Gravando o calculo da franquia %s, tipo de Calculo %s, base calculo %s", calculoFranquiaDMN.getIdFranquia(), calculoFranquiaDMN.getTipoCalculo(), calculoFranquiaDMN.getDtBaseCalculo());
				processamentoCalculoFranqueadosService.storeDocumentosFranquiaDiario(documentos);
			} else if (TipoCalculoFranqueado.MENSAL.equals(calculoFranquiaDMN.getTipoCalculo())) {
				Queue<Serializable> documentos = this.processamentoCalculoFranqueadosService.executeProcessamentoCalculoFranqueadosMensal(calculoFranquiaDMN.getDtBaseCalculo(), calculoFranquiaDMN.getIdFranquia());
				logInfo("Gravando o calculo da franquia %s, tipo de Calculo %s, base calculo %s", calculoFranquiaDMN.getIdFranquia(), calculoFranquiaDMN.getTipoCalculo(), calculoFranquiaDMN.getDtBaseCalculo());
				this.processamentoCalculoFranqueadosService.storeDocumentosFranquiaMensal(calculoFranquiaDMN.getIdFranquia(),calculoFranquiaDMN.getDtBaseCalculo(),documentos);

				this.updateParametrosTotalFranquias(calculoFranquiaDMN.getDtBaseCalculo());
			}
			logInfo("Finalizado o calculo da franquia %s, tipo de Calculo %s ",calculoFranquiaDMN.getIdFranquia(), calculoFranquiaDMN.getTipoCalculo());
			sendEmailSucessoInterno(calculoFranquiaDMN.getDtBaseCalculo(), calculoFranquiaDMN.getTipoCalculo(), sgFranquia);
		} catch (Exception e) {
			sendEmailErrorInterno(calculoFranquiaDMN.getDtBaseCalculo(), calculoFranquiaDMN.getTipoCalculo(), sgFranquia, e);
			LOG.error("Erro no processamento da franquia: "  + calculoFranquiaDMN, e);
		}
		return Response.ok().build();
	}

	private void updateParametrosTotalFranquias(YearMonthDay dtBaseCalculo) {
		logInfo("Decrementa TOTAL_FRANQUEADOS_A_PROCESSAR");
		Long totalFranqueadosAProcessar = this.configuracoesFacade.decrementaParametroSequencial(ConstantesFranqueado.PARAMETRO_TOTAL_FRANQUEADOS_A_PROCESSAR, true);
		if (totalFranqueadosAProcessar == 0L) {
			logInfo("Atualizando BL_LIBERADO_FRANQUEADO_BPEL = [%s]", ConstantesFranqueado.TP_SIM);
			logInfo("Atualizando COMPETENCIA_FRANQUEADO_BPEL = [%s]", dtBaseCalculo.toString("yyyy-MM-dd"));
			this.parametroGeralService.storeValorParametroNewSession(ConstantesFranqueado.PARAMETRO_BL_LIBERADO_FRANQUEADO_BPEL, ConstantesFranqueado.TP_SIM);
			this.parametroGeralService.storeValorParametroNewSession(ConstantesFranqueado.PARAMETRO_COMPETENCIA_FRANQUEADO_BPEL, dtBaseCalculo.toString("yyyy-MM-dd"));

			sendEmailSucessoExterno(dtBaseCalculo);
		}
	}

	private void executaEnvioFranquiasParaCalculo(CalculoFranquiaDMN calculoFranquiaDMN) {
		configuraUsuarioIntegracaoSessao();
		logInfo("Busca franquias ativas");
		List<Serializable> franquiasParaCalculo = buscaFranquiasParaCalculo(calculoFranquiaDMN);
		logInfo("Franquias: %s", franquiasParaCalculo);

		if (TipoCalculoFranqueado.MENSAL.equals(calculoFranquiaDMN.getTipoCalculo())) {
			logInfo("Atualizando TOTAL_FRANQUEADOS_A_PROCESSAR = [%s]", franquiasParaCalculo.size());
			configuracoesFacade.storeValorParametro(ConstantesFranqueado.PARAMETRO_TOTAL_FRANQUEADOS_A_PROCESSAR, new BigDecimal(franquiasParaCalculo.size()));
		}

		logInfo("Enviando [%s] franquias para Fila JMS", franquiasParaCalculo.size());
		enviaFranquiasParaCalculo(franquiasParaCalculo);
		logInfo("Franquias enviadas");
	}	
	
	private void enviaFranquiasParaCalculo(List<Serializable> franquiasParaCalculo) {
		JmsMessageSender jmsSender = integracaoJmsService.createMessage(Queues.CALCULO_FRANQUEADO);
		jmsSender.addAllMsg(franquiasParaCalculo);
		integracaoJmsService.storeMessage(jmsSender);
	}

	private List<Serializable> buscaFranquiasParaCalculo(CalculoFranquiaDMN calculoFranquiaBase) {
		List<Franquia> franquiasAtivas = processamentoCalculoFranqueadosService
			.findFranquiasParaCalculo(calculoFranquiaBase.getDtBaseCalculo());
		List<Serializable> franquiasParaCalculo = new ArrayList<Serializable>();
		for(Franquia f : franquiasAtivas ){
			CalculoFranquiaDMN calculoFranquia = new CalculoFranquiaDMN();
			calculoFranquia.setIdFranquia(f.getIdFranquia());

			if (TipoCalculoFranqueado.DIARIO.equals(calculoFranquiaBase.getTipoCalculo())) {
				calculoFranquia.setDtBaseCalculo(calculoFranquiaBase.getDtBaseCalculo().minusDays(1));
			} else {
				calculoFranquia.setDtBaseCalculo(calculoFranquiaBase.getDtBaseCalculo());
			}

			calculoFranquia.setTipoCalculo(calculoFranquiaBase.getTipoCalculo());			
			franquiasParaCalculo.add(calculoFranquia);
		}
		return franquiasParaCalculo;
	}

	private void configuraUsuarioIntegracaoSessao() {
		SessionContext.setUser(usuario.findById(USUARIO_INTEGRACAO));
	}

	private String getGrupoEmailInterno() {
		return parametroGeralService.findConteudoByNomeParametro(GRUPO_FECHTO_DIARIO_FRANQUEADO, false).toString();
	}

	private String getGrupoEmailExterno() {
		return parametroGeralService.findConteudoByNomeParametro(GRUPO_FECHTO_MENSAL_FRANQUEADO, false).toString();
	}

	private void sendEmailSucessoExterno(YearMonthDay data) {
		StringBuilder body = new StringBuilder();
		body.append(recursoMensagemService.findByChave(LMS_46122, new Object[]{JTDateTimeUtils.getYearMonth(data)}));
		
		String assunto = recursoMensagemService.findByChave(LMS_46121, new Object[]{JTDateTimeUtils.getYearMonth(data)});
		Mail mail = createMail(getGrupoEmailExterno(), assunto, body.toString());
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private void sendEmailSucessoInterno(YearMonthDay data, TipoCalculoFranqueado tipoCalculoFranqueado, String sgFranquia) {
		StringBuilder body = new StringBuilder();
		body.append(recursoMensagemService.findByChave(LMS_46120, new Object[]{tipoCalculoFranqueado.name(), sgFranquia, JTDateTimeUtils.getYearMonth(data)}));
		enviaEmail(getGrupoEmailInterno(), body.toString(), tipoCalculoFranqueado, sgFranquia, SUCESSO);
	}

	private void sendEmailErrorInterno(YearMonthDay data, TipoCalculoFranqueado tipoCalculoFranqueado, String sgFranquia, Exception e) {
		StringBuilder body = new StringBuilder();
		body.append(recursoMensagemService.findByChave(LMS_46119, new Object[]{tipoCalculoFranqueado.name(), sgFranquia, JTDateTimeUtils.getYearMonth(data)}));
		body.append("<br/>");
		body.append("FullStackTrace:<br/>");
		body.append(ExceptionUtils.getStackTrace(e));
		enviaEmail(getGrupoEmailInterno(), body.toString(), tipoCalculoFranqueado, sgFranquia, ERRO);
	}
	
	private void enviaEmail(String para, String mensagem, TipoCalculoFranqueado tipoCalculoFranqueado, String sgFranquia, String status) {
		String assunto = recursoMensagemService.findByChave(LMS_46118, new Object[]{tipoCalculoFranqueado.name(), sgFranquia, status});
		Mail mail = createMail(para, assunto, mensagem);
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}
	
	private Mail createMail(String to, String subject, String body) {
		String from = parametroGeralService.findConteudoByNomeParametro(REMETENTE_EMAIL_LMS, false).toString();
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setBody(body);
		return mail;
	}

	private void logInfo(String mensagem) {
		logInfo(mensagem, new Object[0]);
	}

	private void logInfo(String mensagem, Object... parametros) {
		if (LOG.isInfoEnabled()) {
			LOG.info(String.format(mensagem, parametros));
		}
	}

}
