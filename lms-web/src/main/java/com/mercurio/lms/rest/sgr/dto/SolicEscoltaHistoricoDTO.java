package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class SolicEscoltaHistoricoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private DomainValue tpSituacao;
	private String dsJustificativa;
	private DateTime dhInclusao;
	private String nmUsuario;

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public void setDsJustificativa(String dsJustificativa) {
		this.dsJustificativa = dsJustificativa;
	}

	public String getDsJustificativa() {
		return dsJustificativa;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

}
