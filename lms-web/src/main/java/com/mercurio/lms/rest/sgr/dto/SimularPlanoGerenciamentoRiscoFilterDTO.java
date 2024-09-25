package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

/**
 * FIXME corrigir número do JIRA
 * 
 * LMS-???? (Tela para simulação do Plano de Gerenciamento de Risco) - DTO para
 * formulário de filtro sobre {@link ControleCarga} na tela
 * "Simular Plano de Gerenciamento de Risco".
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class SimularPlanoGerenciamentoRiscoFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private ControleCargaSuggestDTO controleCarga;
	private FilialSuggestDTO filial;
	private DateTime dhGeracaoInicial;
	private DateTime dhGeracaoFinal;

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public DateTime getDhGeracaoInicial() {
		return dhGeracaoInicial;
	}

	public void setDhGeracaoInicial(DateTime dhGeracaoInicial) {
		this.dhGeracaoInicial = dhGeracaoInicial;
	}

	public DateTime getDhGeracaoFinal() {
		return dhGeracaoFinal;
	}

	public void setDhGeracaoFinal(DateTime dhGeracaoFinal) {
		this.dhGeracaoFinal = dhGeracaoFinal;
	}

}
