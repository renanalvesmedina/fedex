package com.mercurio.lms.expedicao;

public class DoctoServicoLookupPojo {

	/*
	 * número do documento de serviço desejado
	 */
	private Long nrDoctoServico;
	/*
	 * identificador da filial de origem do documento de serviço.
	 */
	private Long idFilialOrigem;
	/*
	 * identificador da filial de destino do documento de serviço.
	 */
	private Long idFilialDestino;
	/*
	 * identificador do cliente rementente do documento de serviço.
	 */
	private Long idRemetente;
	/*
	 * identificador do cliente destinatário do documento de serviço.
	 */
	private Long idDestinatario;
	/*
	 * boolean identifica se documento de serviço está bloqueado.
	 */
	private Boolean blBloqueado;
	
	public Long getNrDoctoServico() {
		return nrDoctoServico;
	}
	public void setNrDoctoServico(Long nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}
	
	public Long getIdFilialOrigem() {
		return idFilialOrigem;
	}
	
	public void setIdFilialOrigem(Long idFilialOrigem) {
		this.idFilialOrigem = idFilialOrigem;
	}
	
	public Long getIdFilialDestino() {
		return idFilialDestino;
	}
	public void setIdFilialDestino(Long idFilialDestino) {
		this.idFilialDestino = idFilialDestino;
	}
	
	public Long getIdRemetente() {
		return idRemetente;
	}
	public void setIdRemetente(Long idRemetente) {
		this.idRemetente = idRemetente;
	}
	
	public Long getIdDestinatario() {
		return idDestinatario;
	}
	public void setIdDestinatario(Long idDestinatario) {
		this.idDestinatario = idDestinatario;
	}
	public Boolean getBlBloqueado() {
		return blBloqueado;
	}
	public void setBlBloqueado(Boolean blBloqueado) {
		this.blBloqueado = blBloqueado;
	}
	

}
