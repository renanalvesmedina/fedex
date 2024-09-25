package com.mercurio.lms.rest.expedicao;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.coleta.dto.PedidoColetaDTO;
import com.mercurio.lms.rest.configuracoes.dto.TipoServicoDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class DoctoServicoSuggestDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private Long idDoctoServico;
	private FilialSuggestDTO filialOrigem;
	private FilialSuggestDTO filialDestino;
	private Integer qtVolumes;
	private DomainValue tpDoctoServico;
	private DomainValue finalidade;
	private DomainValue modal;
	private DomainValue abrangencia;
	private Long nrDoctoServico;
	private Long nfCliente;
	private DateTime dhEmissao;
	private String dsDoctoServico;
	private ClienteSuggestDTO destinatario;
	private ClienteSuggestDTO remetente;
	private TipoServicoDTO tipoServico;
	private PedidoColetaDTO pedidoColeta; 

	public FilialSuggestDTO getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(FilialSuggestDTO filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public Integer getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}
	
	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public FilialSuggestDTO getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(FilialSuggestDTO filialDestino) {
		this.filialDestino = filialDestino;
	}

	public DomainValue getTpDoctoServico() {
		return tpDoctoServico;
	}

	public void setTpDoctoServico(DomainValue tpDoctoServico) {
		this.tpDoctoServico = tpDoctoServico;
	}

	public DomainValue getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(DomainValue finalidade) {
		this.finalidade = finalidade;
	}

	public DomainValue getModal() {
		return modal;
	}

	public void setModal(DomainValue modal) {
		this.modal = modal;
	}

	public DomainValue getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(DomainValue abrangencia) {
		this.abrangencia = abrangencia;
	}

	public Long getNrDoctoServico() {
		return nrDoctoServico;
	}

	public void setNrDoctoServico(Long nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}

	public Long getNfCliente() {
		return nfCliente;
	}

	public void setNfCliente(Long nfCliente) {
		this.nfCliente = nfCliente;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public String getDsDoctoServico() {
		return dsDoctoServico;
	}

	public void setDsDoctoServico(String dsDoctoServico) {
		this.dsDoctoServico = dsDoctoServico;
	}

	public ClienteSuggestDTO getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(ClienteSuggestDTO destinatario) {
		this.destinatario = destinatario;
	}

	public ClienteSuggestDTO getRemetente() {
		return remetente;
	}

	public void setRemetente(ClienteSuggestDTO remetente) {
		this.remetente = remetente;
	}

	public TipoServicoDTO getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServicoDTO tipoServico) {
		this.tipoServico = tipoServico;
	}

	public PedidoColetaDTO getPedidoColeta() {
		return pedidoColeta;
	}

	public void setPedidoColeta(PedidoColetaDTO pedidoColeta) {
		this.pedidoColeta = pedidoColeta;
	}

}