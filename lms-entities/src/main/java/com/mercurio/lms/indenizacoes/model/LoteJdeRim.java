package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class LoteJdeRim implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idLoteJdeRim;
	private DateTime dhLoteJdeRim;
	private List recibosIndenizacao;

	public Long getIdLoteJdeRim() {
		return idLoteJdeRim;
	}

	public void setIdLoteJdeRim(Long idLoteJdeRim) {
		this.idLoteJdeRim = idLoteJdeRim;
	}

	public DateTime getDhLoteJdeRim() {
		return dhLoteJdeRim;
	}

	public void setDhLoteJdeRim(DateTime dhLoteJdeRim) {
		this.dhLoteJdeRim = dhLoteJdeRim;
	}

	public List getRecibosIndenizacao() {
		return recibosIndenizacao;
	}

	public void setRecibosIndenizacao(List recibosIndenizacao) {
		this.recibosIndenizacao = recibosIndenizacao;
	}

}
