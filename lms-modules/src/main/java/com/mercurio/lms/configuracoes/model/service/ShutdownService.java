package com.mercurio.lms.configuracoes.model.service;

import org.joda.time.DateTime;

import com.mercurio.adsm.core.web.SWTBrokerZipedServlet;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.shutdownService"
 */

public class ShutdownService {
	private static final int MIN_MINUTES_TO_SHUTDOWN = 5;
	private static final int MAX_HOURS_TO_SHUTDOWN = 24;
	private static DateTime shutdown = null;
	private static String mensagem = null;
	
	public void setScheduledSystemStop(DateTime time, String text) {
		if (time == null) {
			setShutdownTime(null);
			setShutdownMessage(null);
		} else if (time.plusMinutes(MIN_MINUTES_TO_SHUTDOWN).isBefore(JTDateTimeUtils.getDataHoraAtual())) {
			throw new BusinessException("tempominimo");
		} else if (time.isAfter(JTDateTimeUtils.getDataHoraAtual().plusHours(MAX_HOURS_TO_SHUTDOWN))) {
			throw new BusinessException("tempomaximo");
		} else {
			setShutdownTime(time);
			setShutdownMessage(text);
		}
	}
	
	
	public DateTime getScheduledSystemStop() {
		return shutdown;
	}
	
	public String getScheduledMessage() {
		return mensagem;
	}
	
	public int minutesToShutdown() {
		if (shutdown == null) {
			return -1;
		} else if (JTDateTimeUtils.getDataHoraAtual().isBefore(shutdown)) {
			return 0;
		} else {
			return JTDateTimeUtils.getIntervalInMinutes(shutdown, JTDateTimeUtils.getDataHoraAtual());
		}
	}
	
	private void setShutdownTime(DateTime time) {
		shutdown = time;
		if (shutdown == null)
			System.clearProperty(SWTBrokerZipedServlet.SHUTDOWN_TIME);
		else
			System.setProperty(SWTBrokerZipedServlet.SHUTDOWN_TIME, shutdown.toString());
	}
	
	private void setShutdownMessage(String text) {
		mensagem = text;
		System.setProperty(SWTBrokerZipedServlet.SHUTDOWN_MSG, text);
	}
}
