package com.mercurio.lms.portaria.model.evento;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;

/**
 * LMSA-768 - Evento relacionado ao {@link PedidoColeta} e
 * {@link OcorrenciaColeta} gerando um {@link EventoColeta}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class EventoPortariaColeta extends EventoPortaria<EventoColeta> {

	public static EventoPortariaColeta createEventoPortaria(
			Usuario usuario, DateTime dhEvento, PedidoColeta pedidoColeta) {
		Validate.notNull(usuario, "O usuário não pode ser nulo.");
		Validate.notNull(pedidoColeta, "O pedido de coleta não pode ser nulo.");
		return new EventoPortariaColeta(usuario, dhEvento, pedidoColeta);
	}

	private PedidoColeta pedidoColeta;
	private OcorrenciaColeta ocorrenciaColeta;
	private String tpEvento;
	private String dsDescricao;
	private MeioTransporteRodoviario meioTransporteRodoviario;
	private DetalheColeta detalheColeta;

	private EventoPortariaColeta(Usuario usuario, DateTime dhEvento, PedidoColeta pedidoColeta) {
		super(null, usuario, dhEvento);
		this.pedidoColeta = pedidoColeta;
	}
	
	public PedidoColeta getPedidoColeta() {
		return pedidoColeta;
	}

	public EventoPortariaColeta setOcorrenciaColeta(OcorrenciaColeta ocorrenciaColeta) {
		this.ocorrenciaColeta = ocorrenciaColeta;
		return this;
	}

	public EventoPortariaColeta setTpEvento(String tpEvento) {
		this.tpEvento = tpEvento;
		return this;
	}

	public EventoPortariaColeta setDsDescricao(String dsDescricao) {
		this.dsDescricao = dsDescricao;
		return this;
	}

	public EventoPortariaColeta setMeioTransporteRodoviario(MeioTransporteRodoviario meioTransporteRodoviario) {
		this.meioTransporteRodoviario = meioTransporteRodoviario;
		return this;
	}

	public EventoPortariaColeta setDetalheColeta(DetalheColeta detalheColeta) {
		this.detalheColeta = detalheColeta;
		return this;
	}

	@Override
	public EventoColeta gerarEntidade() {
		if (entidade == null) {
			Validate.notNull(ocorrenciaColeta, "A ocorrência de coleta não pode ser nula.");
			Validate.notNull(tpEvento, "O tipo do evento não pode ser nulo.");
			entidade = new EventoColeta();
			entidade.setUsuario(usuario);
			entidade.setDhEvento(dhEvento);
			entidade.setPedidoColeta(pedidoColeta);
			entidade.setOcorrenciaColeta(ocorrenciaColeta);
			entidade.setTpEventoColeta(new DomainValue(tpEvento));
			entidade.setDsDescricao(dsDescricao);
			entidade.setMeioTransporteRodoviario(meioTransporteRodoviario);
			entidade.setDetalheColeta(detalheColeta);                 
		}
		return entidade;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append(pedidoColeta)
				.append(ocorrenciaColeta)
				.append(tpEvento)
				.append(dsDescricao)
				.append(meioTransporteRodoviario)
				.append(detalheColeta)
				.toString();
	}

}
