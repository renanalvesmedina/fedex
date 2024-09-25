package com.mercurio.lms.portaria.model.evento;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;

/**
 * LMSA-768 - Evento relacionado ao {@link DispositivoUnitizacao} gerando um
 * {@link EventoDispositivoUnitizacao}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class EventoPortariaDispositivoUnitizacao extends EventoPortaria<EventoDispositivoUnitizacao> {

	private static final DomainValue TP_SCAN = new DomainValue(ConstantesPortaria.TP_SCAN_LMS);

	public static EventoPortariaDispositivoUnitizacao createEventoPortaria(
			Filial filial, Usuario usuario, DateTime dhEvento, DispositivoUnitizacao dispositivoUnitizacao) {
		Validate.notNull(filial, "A filial não pode ser nula.");
		Validate.notNull(usuario, "O usuário não pode ser nulo.");
		Validate.notNull(dispositivoUnitizacao, "O dispositivo de unitização não pode ser nulo.");
		return new EventoPortariaDispositivoUnitizacao(filial, usuario, dhEvento, dispositivoUnitizacao);
	}

	private DispositivoUnitizacao dispositivoUnitizacao;
	private Evento evento;

	private EventoPortariaDispositivoUnitizacao(
			Filial filial, Usuario usuario, DateTime dhEvento, DispositivoUnitizacao dispositivoUnitizacao) {
		super(filial, usuario, dhEvento);
		this.dispositivoUnitizacao = dispositivoUnitizacao;
	}

	public DispositivoUnitizacao getDispositivoUnitizacao() {
		return dispositivoUnitizacao;
	}

	public Evento getEvento() {
		return evento;
	}
	
	public EventoPortariaDispositivoUnitizacao setEvento(Evento evento) {
		this.evento = evento;
		return this;
	}

	@Override
	public EventoDispositivoUnitizacao gerarEntidade() {
		if (entidade == null) {
			Validate.notNull(evento, "O atributo evento não pode ser nulo.");
			entidade = new EventoDispositivoUnitizacao();
			entidade.setFilial(filial);
			entidade.setUsuario(usuario);
			entidade.setDhEvento(dhEvento);
			entidade.setDhInclusao(getDataHoraAtual());
			entidade.setDispositivoUnitizacao(dispositivoUnitizacao);
			entidade.setEvento(evento);
			entidade.setTpScan(TP_SCAN);
			entidade.setBlEventoCancelado(false);
		}
		return entidade;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append(dispositivoUnitizacao)
				.append(evento)
				.toString();
	}

}
