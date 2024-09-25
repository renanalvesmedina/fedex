package com.mercurio.lms.integracao.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetFile implements Serializable {
	private static final long serialVersionUID = 1L;

	private String filial;
	private Calendar dtAtualizacao;

	public GetFile() {
		super();
	}

	public GetFile(long codigo, String filial, Calendar dtAtualizacao) {
		super();
		this.filial = filial;
		this.dtAtualizacao = dtAtualizacao;
	}

	public String getFilial() {
		return filial;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}

	public Calendar getDtAtualizacao() {
		return dtAtualizacao;
	}

	public void setDtAtualizacao(Calendar dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}
	
	public String getDtAtualizacaoFormat() {
		
		if( dtAtualizacao != null ){
			try {
				return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
						.format(dtAtualizacao.getTime());
			} catch (Exception e) {
				e.getMessage();
				return "";
			}
		}
		return "";
	}
	
}