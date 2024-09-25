package com.mercurio.lms.carregamento.action;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.workflow.model.Pendencia;

public class ConsultarControleCargasJanelasActionTest {
	
	private ConsultarControleCargasJanelasAction consultarControleCargasJanelasAction;
	private EventoControleCarga eventoControleCarga;
/**
 * 
    private boolean isEventoSaidaPortaria(EventoControleCarga evento){
    	return evento != null && evento.getTpEventoControleCarga().equals(new DomainValue("SP"));
    }
    
    private boolean eventoTemPendenciaOuPendenciaReprovada(EventoControleCarga evento){
    	return evento.getPendencia() == null || evento.getTpSituacaoPendencia().equals(new DomainValue("R"));
    }
 */
	@Before
	public void setup(){
		this.consultarControleCargasJanelasAction = new ConsultarControleCargasJanelasAction();
		this.eventoControleCarga = new EventoControleCarga();
	}
	@Test
	public void testIsEventoSaidaPortariaComEventoNulo() {
		assertFalse(consultarControleCargasJanelasAction.isEventoSaidaPortaria(null));
	}
	
	@Test
	public void testIsEventoSaidaPortariaComSaidaPortaria() {
		eventoControleCarga.setTpEventoControleCarga(new DomainValue("SP"));
		assertTrue(consultarControleCargasJanelasAction.isEventoSaidaPortaria(eventoControleCarga));
	}
	
	@Test
	public void testIsEventoSaidaPortariaComTipoDiferenteSaidaPortaria() {
		eventoControleCarga.setTpEventoControleCarga(new DomainValue("EM"));
		assertFalse(consultarControleCargasJanelasAction.isEventoSaidaPortaria(eventoControleCarga));
	}
	
	@Test
	public void testEventoNaoTemPendenciaOuPendenciaReprovadaComPendenciaNula() {
		assertTrue(consultarControleCargasJanelasAction.eventoPodeTerAlteracaoDataSaida(eventoControleCarga));
	}
	
	@Test
	public void testEventoNaoTemPendenciaOuPendenciaReprovadaComEventoPendenciaAceita() {
		eventoControleCarga.setPendencia(new Pendencia());
		eventoControleCarga.setTpSituacaoPendencia(new DomainValue("A"));
		assertTrue(consultarControleCargasJanelasAction.eventoPodeTerAlteracaoDataSaida(eventoControleCarga));
	}
	
	@Test
	public void testEventoNaoTemPendenciaOuPendenciaReprovadaComEventoPendenciaReprovada() {
		eventoControleCarga.setTpSituacaoPendencia(new DomainValue("R"));
		assertTrue(consultarControleCargasJanelasAction.eventoPodeTerAlteracaoDataSaida(eventoControleCarga));
	}

}
