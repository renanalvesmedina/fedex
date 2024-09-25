package com.mercurio.lms.rest.vendas.dto;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ClienteSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String nmPessoa;
	private String tpCliente;
	private String sgUnidadeFederativa;
	private Long nrConta;
	private String nmMunicipio;
	private String nmFantasia;
	private String nrIdentificacao;
	private List<InformacaoDoctoClienteDTO> informacoesDoctoCliente;
	private String modal;
	private String abrangencia;

	private DomainValue tpIdentificacao;

	public ClienteSuggestDTO() {
		super();
	}

	public ClienteSuggestDTO(Long idCliente) {
		this();
		setId(idCliente);
	}

	public ClienteSuggestDTO(Long idCliente, String nmPessoa, String nrIdentificacao) {
		this(idCliente);
		this.nmPessoa = nmPessoa;
		this.nrIdentificacao = nrIdentificacao;
	}

	public ClienteSuggestDTO(Long idCliente, String nmPessoa, String tpCliente, String sgUnidadeFederativa, Long nrConta,
			String nmMunicipio, String nmFantasia, String nrIdentificacao, String modal, String abrangencia) {
		this(idCliente);
		this.nmPessoa = nmPessoa;
		this.tpCliente = tpCliente;
		this.sgUnidadeFederativa = sgUnidadeFederativa;
		this.nrConta = nrConta;
		this.nmMunicipio = nmMunicipio;
		this.nmFantasia = nmFantasia;
		this.nrIdentificacao = nrIdentificacao;
		this.modal = modal;
		this.abrangencia = abrangencia;
	}

	public Long getIdCliente() {
		return getId();
	}

	public void setIdCliente(Long idCliente) {
		setId(idCliente);
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public String getTpCliente() {
		return tpCliente;
	}

	public void setTpCliente(String tpCliente) {
		this.tpCliente = tpCliente;
	}

	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}

	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}

	public Long getNrConta() {
		return nrConta;
	}

	public void setNrConta(Long nrConta) {
		this.nrConta = nrConta;
	}

	public String getNmMunicipio() {
		return nmMunicipio;
	}

	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}

	public String getNmFantasia() {
		return nmFantasia;
	}

	public void setNmFantasia(String nmFantasia) {
		this.nmFantasia = nmFantasia;
	}

	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}

	public List<InformacaoDoctoClienteDTO> getInformacoesDoctoCliente() {
		return informacoesDoctoCliente;
	}

	public void setInformacoesDoctoCliente(List<InformacaoDoctoClienteDTO> informacoesDoctoCliente) {
		this.informacoesDoctoCliente = informacoesDoctoCliente;
	}

	public void addInformacaoDoctoCliente(InformacaoDoctoClienteDTO informacao) {
		if (this.informacoesDoctoCliente == null) {
			this.informacoesDoctoCliente = new ArrayList<InformacaoDoctoClienteDTO>();
		}
		this.informacoesDoctoCliente.add(informacao);
	}

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}

	public String getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(String abrangencia) {
		this.abrangencia = abrangencia;
	}

	public DomainValue getTpIdentificacao() {
		return tpIdentificacao;
	}

	public void setTpIdentificacao(DomainValue tpIdentificacao) {
		this.tpIdentificacao = tpIdentificacao;
	}

}
