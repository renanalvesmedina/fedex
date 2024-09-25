package com.mercurio.lms.portaria.model.evento;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Query;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;

/**
 * LMSA-768 - Classe abstrata base para eventos do processo
 * "Informar Chegada na Portaria", incluindo atributos e operações comuns a
 * todos os eventos.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 * @param <E>
 *            Entidade referente ao evento a ser gerado.
 */
public abstract class EventoPortaria<E extends Serializable> {

	protected static DateTime getDataHoraAtual() {
		return JTDateTimeUtils.getDataHoraAtual();
	}

	protected Filial filial;
	protected Usuario usuario;
	protected DateTime dhEvento;

	protected E entidade;

	/**
	 * Construtor básico para eventos do processo "Informar Chegada na
	 * Portaria", incluindo atributos comuns a todos os eventos.
	 * 
	 * @param filial
	 * @param usuario
	 * @param dhEvento
	 */
	protected EventoPortaria(Filial filial, Usuario usuario, DateTime dhEvento) {
		Validate.notNull(dhEvento, "A data do evento não pode ser nula.");
		this.filial = filial;
		this.usuario = usuario;
		this.dhEvento = dhEvento;
	}

	public abstract E gerarEntidade();

	public Query gerarAtualizacao(NewSaidaChegadaDAO dao, boolean parametroFilialLMSA1675) {
		return null;
	}

	public VirtualTopics verificarMensagem() {
		return null;
	}

	public Serializable gerarMensagem() {
		return null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(filial)
				.append(usuario)
				.append(dhEvento)
				.toString();
	}

}
