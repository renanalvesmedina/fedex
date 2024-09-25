package com.mercurio.lms.portaria.model.evento;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Query;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;

/**
 * LMSA-768 - Evento relacionado ao {@link MeioTransporte} gerando um novo
 * {@link EventoMeioTransporte} e atualizando o {@link EventoMeioTransporte}
 * imediatamente anterior.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * 
 */
public class EventoPortariaMeioTransporte extends EventoPortaria<EventoMeioTransporte> {

	public static EventoPortariaMeioTransporte createEventoPortaria(
			Filial filial, Usuario usuario, DateTime dhEvento, MeioTransporte meioTransporte) {
		Validate.notNull(meioTransporte, "O meio de transporte não pode ser nulo.");
		return new EventoPortariaMeioTransporte(filial, usuario, dhEvento, meioTransporte);
	}

	private MeioTransporte meioTransporte;
	private String tpSituacao;
	private ControleTrecho controleTrecho;
	private ControleCarga controleCarga;
	private Box box;

	private EventoPortariaMeioTransporte(
			Filial filial, Usuario usuario, DateTime dhEvento, MeioTransporte meioTransporte) {
		super(filial, usuario, dhEvento);
		this.meioTransporte = meioTransporte;
	}

	public EventoPortariaMeioTransporte setTpSituacao(String tpSituacao) {
		this.tpSituacao = tpSituacao;
		return this;
	}

	public EventoPortariaMeioTransporte setControleTrecho(ControleTrecho controleTrecho) {
		this.controleTrecho = controleTrecho;
		return this;
	}

	public EventoPortariaMeioTransporte setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
		return this;
	}

	public EventoPortariaMeioTransporte setBox(Box box) {
		this.box = box;
		return this;
	}

	@Override
	public EventoMeioTransporte gerarEntidade() {
		if (entidade == null) {
			Validate.notNull(tpSituacao, "A atributo situação não pode ser nulo.");
			entidade = new EventoMeioTransporte();
			entidade.setFilial(filial);
			entidade.setUsuario(usuario);
			entidade.setDhInicioEvento(dhEvento);
			entidade.setDhGeracao(getDataHoraAtual());
			entidade.setMeioTransporte(meioTransporte);
			entidade.setTpSituacaoMeioTransporte(new DomainValue(tpSituacao));
			entidade.setControleTrecho(controleTrecho);
			entidade.setControleCarga(controleCarga);
			entidade.setBox(box);
		}
		return entidade;
	}

	@Override
	public Query gerarAtualizacao(NewSaidaChegadaDAO dao, boolean parametroFilialLMSA1675) {
		Validate.notNull(entidade, "A entidade do evento deve ser persistida antes da atualização.");
		Validate.notNull(entidade.getIdEventoMeioTransporte(), "A entidade do evento deve ser persistida antes da atualização.");
		return dao.queryUpdateUltimoEventoMeioTransporte(parametroFilialLMSA1675, entidade);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append(meioTransporte)
				.append(tpSituacao)
				.append(controleTrecho)
				.append(controleCarga)
				.append(box)
				.toString();
	}

}
