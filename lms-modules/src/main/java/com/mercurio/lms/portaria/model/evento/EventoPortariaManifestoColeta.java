package com.mercurio.lms.portaria.model.evento;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.EventoManifestoColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;

/**
 * LMSA-768 - Evento relacionado ao {@link ManifestoColeta} gerando um
 * {@link EventoManifestoColeta}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class EventoPortariaManifestoColeta extends EventoPortaria<EventoManifestoColeta> {

	public static EventoPortariaManifestoColeta createEventoPortaria(
			DateTime dhEvento, ManifestoColeta manifestoColeta) {
		Validate.notNull(manifestoColeta, "O manifeto de coleta não pode ser nulo.");
		return new EventoPortariaManifestoColeta(dhEvento, manifestoColeta);
	}

	private ManifestoColeta manifestoColeta;
	private String tpEvento;

	private EventoPortariaManifestoColeta(DateTime dhEvento, ManifestoColeta manifestoColeta) {
		super(null, null, dhEvento);
		this.manifestoColeta = manifestoColeta;
	}
	
	public ManifestoColeta getManifestoColeta() {
		return manifestoColeta;
	}

	public EventoPortariaManifestoColeta setTpEvento(String tpEvento) {
		this.tpEvento = tpEvento;
		return this;
	}

	@Override
	public EventoManifestoColeta gerarEntidade() {
		if (entidade == null) {
			Validate.notNull(tpEvento, "O tipo do evento não pode ser nulo.");
			entidade = new EventoManifestoColeta();
			entidade.setDhEvento(dhEvento);
			entidade.setManifestoColeta(manifestoColeta);
			entidade.setTpEventoManifestoColeta(new DomainValue(tpEvento));
		}
		return entidade;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append(manifestoColeta)
				.append(tpEvento)
				.toString();
	}

}
