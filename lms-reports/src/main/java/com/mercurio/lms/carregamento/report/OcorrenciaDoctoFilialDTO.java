package com.mercurio.lms.carregamento.report;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

/**
 * @author JonasFE
 *
 */
public class OcorrenciaDoctoFilialDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String TP_DOCTO_SERVICO;
	private String FILIAL_DOCTO_SERVICO;
	private String NR_DOCTO_SERVICO;
	private String FILIAL_LOCALIZACAO;
	private String FILIAL_OCORRENCIA;
	private String FILIAL_DESTINO;
	private String DS_LOGIN;
	private Date DT_OCORRENCIA;
	
	
	
	
	public String getTP_DOCTO_SERVICO() {
		return TP_DOCTO_SERVICO;
	}
	public void setTP_DOCTO_SERVICO(String tP_DOCTO_SERVICO) {
		TP_DOCTO_SERVICO = tP_DOCTO_SERVICO;
	}
	public String getFILIAL_DOCTO_SERVICO() {
		return FILIAL_DOCTO_SERVICO;
	}
	public void setFILIAL_DOCTO_SERVICO(String fILIAL_DOCTO_SERVICO) {
		FILIAL_DOCTO_SERVICO = fILIAL_DOCTO_SERVICO;
	}
	public String getNR_DOCTO_SERVICO() {
		return NR_DOCTO_SERVICO;
	}
	public void setNR_DOCTO_SERVICO(String nR_DOCTO_SERVICO) {
		NR_DOCTO_SERVICO = nR_DOCTO_SERVICO;
	}
	public String getFILIAL_LOCALIZACAO() {
		return FILIAL_LOCALIZACAO;
	}
	public void setFILIAL_LOCALIZACAO(String fILIAL_LOCALIZACAO) {
		FILIAL_LOCALIZACAO = fILIAL_LOCALIZACAO;
	}
	public String getFILIAL_OCORRENCIA() {
		return FILIAL_OCORRENCIA;
	}
	public void setFILIAL_OCORRENCIA(String fILIAL_OCORRENCIA) {
		FILIAL_OCORRENCIA = fILIAL_OCORRENCIA;
	}
	public String getFILIAL_DESTINO() {
		return FILIAL_DESTINO;
	}
	public void setFILIAL_DESTINO(String fILIAL_DESTINO) {
		FILIAL_DESTINO = fILIAL_DESTINO;
	}
	public String getDS_LOGIN() {
		return DS_LOGIN;
	}
	public void setDS_LOGIN(String dS_LOGIN) {
		DS_LOGIN = dS_LOGIN;
	}
	public Date getDT_OCORRENCIA() {
		return DT_OCORRENCIA;
	}
	public void setDT_OCORRENCIA(Date dT_OCORRENCIA) {
		DT_OCORRENCIA = dT_OCORRENCIA;
	}

}
