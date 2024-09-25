package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import com.mercurio.lms.municipios.model.Filial;

public class DoctoServicoDadosCliente implements Serializable {

	private static final long serialVersionUID = 2L;
	private Long idClienteRemetente;
	private Long idInscricaoEstadualRemetente;
	private Long idMunicipioRemetente;
	private Long idUfRemetente;
	private String nrCepRemetente;
	private Boolean blCotacaoRemetente;
	private Boolean blFobDirigido;

	private Long idClienteDestinatario;
	private Long idInscricaoEstadualDestinatario;
	private Long idMunicipioDestinatario;
	private Long idUfDestinatario;
	private Boolean blDificuldadeEntregaDestinatario;

	private Long idClienteRecebedor;
	private Long idInscricaoEstadualRecebedor;
	
	private Boolean blRestricaoTransporteOrigem;
	private Boolean blRestricaoTransporteDestino;

	private Long idUfConsignatario;
	private Long idUfRedespacho;

	private Long idUfFilialUsuario;
	private Long idUfFilialRemetente;

	private Long idTipoLocalizacaoOrigem;
	private Long idTipoLocalizacaoDestino;

	private String tpSituacaoTributariaRemetente;
	private String tpSituacaoTributariaDestinatario;
	private String tpSituacaoTributariaResponsavel;
	private String tpSituacaoTributariaRecebedor;

	private String nrCtrcSubcontratante;
    private Boolean blMercadoriaExportacao;

    private Filial filialTransacao;
    
	private Boolean blVeiculoDedicadoDestinatario;
	private Boolean blAgendamentoEntregaDestinatario;
	private Boolean blPaletizacaoDestinatario;
	private Boolean blCustoDescargaDestinatario;
	
	public Long getIdClienteDestinatario() {
		return idClienteDestinatario;
	}

	public void setIdClienteDestinatario(Long idClienteDestinatario) {
		this.idClienteDestinatario = idClienteDestinatario;
	}

	public Long getIdClienteRemetente() {
		return idClienteRemetente;
	}

	public void setIdClienteRemetente(Long idClienteRemetente) {
		this.idClienteRemetente = idClienteRemetente;
	}

	public Long getIdInscricaoEstadualDestinatario() {
		return idInscricaoEstadualDestinatario;
	}

	public void setIdInscricaoEstadualDestinatario(
			Long idInscricaoEstadualDestinatario) {
		this.idInscricaoEstadualDestinatario = idInscricaoEstadualDestinatario;
	}

	public Long getIdInscricaoEstadualRemetente() {
		return idInscricaoEstadualRemetente;
	}

	public void setIdInscricaoEstadualRemetente(
			Long idInscricaoEstadualRemetente) {
		this.idInscricaoEstadualRemetente = idInscricaoEstadualRemetente;
	}

	public Long getIdMunicipioDestinatario() {
		return idMunicipioDestinatario;
	}

	public void setIdMunicipioDestinatario(Long idMunicipioDestinatario) {
		this.idMunicipioDestinatario = idMunicipioDestinatario;
	}

	public Long getIdMunicipioRemetente() {
		return idMunicipioRemetente;
	}

	public void setIdMunicipioRemetente(Long idMunicipioRemetente) {
		this.idMunicipioRemetente = idMunicipioRemetente;
	}

	public Long getIdTipoLocalizacaoDestino() {
		return idTipoLocalizacaoDestino;
	}

	public void setIdTipoLocalizacaoDestino(Long idTipoLocalizacaoDestino) {
		this.idTipoLocalizacaoDestino = idTipoLocalizacaoDestino;
	}

	public Long getIdTipoLocalizacaoOrigem() {
		return idTipoLocalizacaoOrigem;
	}

	public void setIdTipoLocalizacaoOrigem(Long idTipoLocalizacaoOrigem) {
		this.idTipoLocalizacaoOrigem = idTipoLocalizacaoOrigem;
	}

	public Long getIdUfFilialUsuario() {
		return idUfFilialUsuario;
	}

	public void setIdUfFilialUsuario(Long idUfFilialUsuario) {
		this.idUfFilialUsuario = idUfFilialUsuario;
	}

	public Long getIdUfFilialRemetente() {
		return idUfFilialRemetente;
	}

	public void setIdUfFilialRemetente(Long idUfFilialRemetente) {
		this.idUfFilialRemetente = idUfFilialRemetente;
	}

	public String getNrCepRemetente() {
		return nrCepRemetente;
	}

	public void setNrCepRemetente(String nrCepRemetente) {
		this.nrCepRemetente = nrCepRemetente;
	}

	public Boolean getBlCotacaoRemetente() {
		if(this.blCotacaoRemetente == null) {
			this.blCotacaoRemetente = Boolean.FALSE;
		}
		return this.blCotacaoRemetente;
	}

	public void setBlCotacaoRemetente(Boolean blCotacaoRemetente) {
		this.blCotacaoRemetente = blCotacaoRemetente;
	}

	public Long getIdUfConsignatario() {
		return idUfConsignatario;
	}

	public void setIdUfConsignatario(Long idUfConsignatario) {
		this.idUfConsignatario = idUfConsignatario;
	}

	public Long getIdUfDestinatario() {
		return idUfDestinatario;
	}

	public void setIdUfDestinatario(Long idUfDestinatario) {
		this.idUfDestinatario = idUfDestinatario;
	}

	public Long getIdUfRedespacho() {
		return idUfRedespacho;
	}

	public void setIdUfRedespacho(Long idUfRedespacho) {
		this.idUfRedespacho = idUfRedespacho;
	}

	public Long getIdUfRemetente() {
		return idUfRemetente;
	}

	public void setIdUfRemetente(Long idUfRemetente) {
		this.idUfRemetente = idUfRemetente;
	}

	public String getTpSituacaoTributariaRemetente() {
		return tpSituacaoTributariaRemetente;
	}

	public void setTpSituacaoTributariaRemetente(
			String tpSituacaoTributariaRemetente) {
		this.tpSituacaoTributariaRemetente = tpSituacaoTributariaRemetente;
	}

	public String getTpSituacaoTributariaDestinatario() {
		return tpSituacaoTributariaDestinatario;
	}

	public void setTpSituacaoTributariaDestinatario(
			String tpSituacaoTributariaDestinatario) {
		this.tpSituacaoTributariaDestinatario = tpSituacaoTributariaDestinatario;
	}

	public String getTpSituacaoTributariaResponsavel() {
		return tpSituacaoTributariaResponsavel;
	}

	public void setTpSituacaoTributariaResponsavel(
			String tpSituacaoTributariaResponsavel) {
		this.tpSituacaoTributariaResponsavel = tpSituacaoTributariaResponsavel;
	}

	public String getNrCtrcSubcontratante() {
		return nrCtrcSubcontratante;
	}

	public void setNrCtrcSubcontratante(String nrCtrcSubcontratante) {
		this.nrCtrcSubcontratante = nrCtrcSubcontratante;
	}

	public Boolean getBlMercadoriaExportacao() {
		return blMercadoriaExportacao;
	}

	public void setBlMercadoriaExportacao(Boolean blMercadoriaExportacao) {
		this.blMercadoriaExportacao = blMercadoriaExportacao;
	}

	public Boolean getBlDificuldadeEntregaDestinatario() {
		return blDificuldadeEntregaDestinatario;
	}

	public void setBlDificuldadeEntregaDestinatario(
			Boolean blDificuldadeEntregaDestinatario) {
		this.blDificuldadeEntregaDestinatario = blDificuldadeEntregaDestinatario;
	}

	public Boolean getBlFobDirigido() {
		return Boolean.TRUE.equals(blFobDirigido);
	}

	public void setBlFobDirigido(Boolean blFobDirigido) {
		this.blFobDirigido = blFobDirigido;
	}

	public Boolean getBlRestricaoTransporteOrigem() {
		return blRestricaoTransporteOrigem;
	}

	public void setBlRestricaoTransporteOrigem(
			Boolean blRestricaoTransporteOrigem) {
		this.blRestricaoTransporteOrigem = blRestricaoTransporteOrigem;
	}

	public Boolean getBlRestricaoTransporteDestino() {
		return blRestricaoTransporteDestino;
	}

	public void setBlRestricaoTransporteDestino(
			Boolean blRestricaoTransporteDestino) {
		this.blRestricaoTransporteDestino = blRestricaoTransporteDestino;
	}

	public Filial getFilialTransacao() {
		return filialTransacao;
}

	public void setFilialTransacao(Filial filialTransacao) {
		this.filialTransacao = filialTransacao;
	}

	public Long getIdClienteRecebedor() {
		return idClienteRecebedor;
	}

	public void setIdClienteRecebedor(Long idClienteRecebedor) {
		this.idClienteRecebedor = idClienteRecebedor;
	}

	public Long getIdInscricaoEstadualRecebedor() {
		return idInscricaoEstadualRecebedor;
	}

	public void setIdInscricaoEstadualRecebedor(
			Long idInscricaoEstadualRecebedor) {
		this.idInscricaoEstadualRecebedor = idInscricaoEstadualRecebedor;
	}

	public String getTpSituacaoTributariaRecebedor() {
		return tpSituacaoTributariaRecebedor;
	}

	public void setTpSituacaoTributariaRecebedor(
			String tpSituacaoTributariaRecebedor) {
		this.tpSituacaoTributariaRecebedor = tpSituacaoTributariaRecebedor;
	}

	public Boolean getBlVeiculoDedicadoDestinatario() {
		return blVeiculoDedicadoDestinatario;
}

	public void setBlVeiculoDedicadoDestinatario(Boolean blVeiculoDedicadoDestinatario) {
		this.blVeiculoDedicadoDestinatario = blVeiculoDedicadoDestinatario;
	}

	public Boolean getBlAgendamentoEntregaDestinatario() {
		return blAgendamentoEntregaDestinatario;
	}

	public void setBlAgendamentoEntregaDestinatario(Boolean blAgendamentoEntregaDestinatario) {
		this.blAgendamentoEntregaDestinatario = blAgendamentoEntregaDestinatario;
	}

	public Boolean getBlPaletizacaoDestinatario() {
		return blPaletizacaoDestinatario;
	}

	public void setBlPaletizacaoDestinatario(Boolean blPaletizacaoDestinatario) {
		this.blPaletizacaoDestinatario = blPaletizacaoDestinatario;
	}

	public Boolean getBlCustoDescargaDestinatario() {
		return blCustoDescargaDestinatario;
	}

	public void setBlCustoDescargaDestinatario(Boolean blCustoDescargaDestinatario) {
		this.blCustoDescargaDestinatario = blCustoDescargaDestinatario;
	}
}
