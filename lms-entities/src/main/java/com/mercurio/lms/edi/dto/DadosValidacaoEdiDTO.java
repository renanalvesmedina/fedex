package com.mercurio.lms.edi.dto;

import com.mercurio.lms.edi.enums.StatusProcessamento;
import com.mercurio.lms.expedicao.dto.NotaFiscalEdiDto;
import com.mercurio.lms.vendas.dto.ClienteDTO;

import java.io.Serializable;

public class DadosValidacaoEdiDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idFilial;
	private ClienteDTO clienteRemetente;
	private Long nrProcessamento;
	private NotaFiscalEdiDto notaFiscalEdi;
	private String cae;
	private String tpProcessamento;
	private Integer index;
	private Boolean isControladoPoliciaCivil;
	private Boolean isProdutoPerigoso;
	private Boolean isControladoExercito;
	private Boolean isControladoPoliciaFederal;
	private String processarPor;
	private Boolean validado;
	private StatusProcessamento status;
	private Long idUsuario;
	private Long idProcessamentoEdi;

	public DadosValidacaoEdiDTO(ValidarEdiDTO validarEdiDTO) {

		this.idFilial = validarEdiDTO.getIdFilial();
		this.clienteRemetente = new ClienteDTO(validarEdiDTO.getClienteRemetente());
		this.nrProcessamento = validarEdiDTO.getNrProcessamento();
		this.notaFiscalEdi = new NotaFiscalEdiDto(validarEdiDTO.getNotaFiscalEdi());
		this.cae = validarEdiDTO.getCae();
		this.tpProcessamento = validarEdiDTO.getTpProcessamento();
		this.index = validarEdiDTO.getIndex();
		this.isControladoPoliciaCivil = validarEdiDTO.getIsControladoPoliciaCivil();
		this.isProdutoPerigoso = validarEdiDTO.getIsProdutoPerigoso();
		this.isControladoExercito = validarEdiDTO.getIsControladoExercito();
		this.isControladoPoliciaFederal = validarEdiDTO.getIsControladoPoliciaFederal();
		this.processarPor = validarEdiDTO.getProcessarPor();
		this.validado = validarEdiDTO.getValidado();
		this.status = validarEdiDTO.getStatus();
		this.idUsuario = validarEdiDTO.getIdUsuario();
		this.idProcessamentoEdi = validarEdiDTO.getIdProcessamentoEdi();
	}

	public DadosValidacaoEdiDTO() {
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public ClienteDTO getClienteRemetente() {
		return clienteRemetente;
	}

	public void setClienteRemetente(ClienteDTO clienteRemetente) {
		this.clienteRemetente = clienteRemetente;
	}

	public Long getNrProcessamento() {
		return nrProcessamento;
	}

	public void setNrProcessamento(Long nrProcessamento) {
		this.nrProcessamento = nrProcessamento;
	}

	public NotaFiscalEdiDto getNotaFiscalEdi() {
		return notaFiscalEdi;
	}

	public void setNotaFiscalEdi(NotaFiscalEdiDto notaFiscalEdi) {
		this.notaFiscalEdi = notaFiscalEdi;
	}

	public String getCae() {
		return cae;
	}

	public void setCae(String cae) {
		this.cae = cae;
	}

	public String getTpProcessamento() {
		return tpProcessamento;
	}

	public void setTpProcessamento(String tpProcessamento) {
		this.tpProcessamento = tpProcessamento;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Boolean getIsControladoPoliciaCivil() {
		return isControladoPoliciaCivil;
	}

	public void setIsControladoPoliciaCivil(Boolean isControladoPoliciaCivil) {
		this.isControladoPoliciaCivil = isControladoPoliciaCivil;
	}

	public Boolean getIsProdutoPerigoso() {
		return isProdutoPerigoso;
	}

	public void setIsProdutoPerigoso(Boolean isProdutoPerigoso) {
		this.isProdutoPerigoso = isProdutoPerigoso;
	}

	public Boolean getIsControladoExercito() {
		return isControladoExercito;
	}

	public void setIsControladoExercito(Boolean isControladoExercito) {
		this.isControladoExercito = isControladoExercito;
	}

	public Boolean getIsControladoPoliciaFederal() {
		return isControladoPoliciaFederal;
	}

	public void setIsControladoPoliciaFederal(Boolean isControladoPoliciaFederal) {
		this.isControladoPoliciaFederal = isControladoPoliciaFederal;
	}

	public String getProcessarPor() {
		return processarPor;
	}

	public void setProcessarPor(String processarPor) {
		this.processarPor = processarPor;
	}

	public Boolean getValidado() {
		return validado;
	}

	public void setValidado(Boolean validado) {
		this.validado = validado;
	}

	public StatusProcessamento getStatus() {
		return status;
	}

	public void setStatus(StatusProcessamento status) {
		this.status = status;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getIdProcessamentoEdi() {
		return idProcessamentoEdi;
	}

	public void setIdProcessamentoEdi(Long idProcessamentoEdi) {
		this.idProcessamentoEdi = idProcessamentoEdi;
	}
}
