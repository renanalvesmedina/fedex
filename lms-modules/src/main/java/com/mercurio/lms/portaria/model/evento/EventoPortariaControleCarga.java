package com.mercurio.lms.portaria.model.evento;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;

/**
 * LMSA-768 - Evento relacionado ao {@link ControleCarga} gerando um
 * {@link EventoControleCarga}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class EventoPortariaControleCarga extends EventoPortaria<EventoControleCarga> {

	public static EventoPortariaControleCarga createEventoPortaria(
			Filial filial, Usuario usuario, DateTime dhEvento, ControleCarga controleCarga) {
		Validate.notNull(filial, "A filial não pode ser nula.");
		Validate.notNull(controleCarga, "O controle de carga não pode ser nulo.");
		return new EventoPortariaControleCarga(filial, usuario, dhEvento, controleCarga);
	}

	private ControleCarga controleCarga;
	private String tpEvento;
	private MeioTransporte meioTransporte;

	private EventoPortariaControleCarga(
			Filial filial, Usuario usuario, DateTime dhEvento, ControleCarga controleCarga) {
		super(filial, usuario, dhEvento);
		this.controleCarga = controleCarga;
	}

	public EventoPortariaControleCarga setTpEvento(String tpEvento) {
		this.tpEvento = tpEvento;
		return this;
	}

	public EventoPortariaControleCarga setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
		return this;
	}

	@Override
	public EventoControleCarga gerarEntidade() {
		if (entidade == null) {
			Validate.notNull(tpEvento, "O tipo do evento não pode ser nulo.");
			entidade = new EventoControleCarga();
			entidade.setFilial(filial);
			entidade.setUsuario(usuario);
			entidade.setDhEvento(dhEvento);
			entidade.setControleCarga(controleCarga);
			entidade.setTpEventoControleCarga(new DomainValue(tpEvento));
			entidade.setMeioTransporte(meioTransporte);
		}
		return entidade;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append(controleCarga)
				.append(tpEvento)
				.append(meioTransporte)
				.toString();
	}

}
