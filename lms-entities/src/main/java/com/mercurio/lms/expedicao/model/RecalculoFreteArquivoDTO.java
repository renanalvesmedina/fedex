package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;

/**
 * @author JonasFE
 *
 */
public class RecalculoFreteArquivoDTO {
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_SEPARATOR = ";";
	
	private Integer qtVolumes;
	private Integer cdFrete;

	private Long nrDocumento;
	
	private BigDecimal psReal;
	private BigDecimal psAferido;
	private BigDecimal psCubado;
	private BigDecimal vlMercadoria;
	
	private String tpFrete;
	private String tpDevedorFrete;
	private String tpServico;
	private String tpModal;
	private String nrIdentificacaoRemetente;
	private String nrIdentificacaoDestinatario;
	private String tpSituacaoTributariaRemetente;
	private String tpSituacaoTributariaDestinatario;
	private String sgEstadoOrigem;
	private String sgEstadoDestino;
	private String cdIBGEMunicipioOrigem;
	private String cdIBGEMunicipioDestino;
	private String nrIERemetente;
	private String nrIEDestinatario;
	private String nmMunicipioOrigem;
	private String nmMunicipioDestino;
	private String nmRemetente;
	private String nmDestinatario;
	
	public RecalculoFreteArquivoDTO() {
		
	}
	
	public RecalculoFreteArquivoDTO(final String line) {
		this(line,DEFAULT_SEPARATOR);
	}
	
	/**
	 * Importacao através de um arquivo
	 * 
	 * @param line
	 * @param separator
	 */
	public RecalculoFreteArquivoDTO(final String line, final String separator) {
		
		if(line != null){
			
			String[] data = line.split(separator);
			
			/*Numero do documento*/
			if(data[1] != null){
				this.nrDocumento =  Long.valueOf(data[1].trim());
			}
			
			/*Peso real*/
			if(data[3] != null){
				this.psReal = new BigDecimal(data[3].trim().replace(",", "."));
			}
			
			/*Peso aferido*/
			if(data[4] != null){
				this.psAferido = new BigDecimal(data[4].trim()
						.replace(",", "."));
			}
			
			/*Peso cubado*/
			if(data[5] != null){
				this.psCubado = new BigDecimal(data[5].trim().replace(",", "."));
			}
			
			/*Valor da mercadoria*/
			if(data[6] != null){
				this.vlMercadoria = new BigDecimal(data[6].trim().replace(",",
						"."));
			}
			
			/*Quantidade de volumes*/
			if(data[7] != null){
				this.qtVolumes = Integer.valueOf(data[7].trim());
			}
			
			/*CNPJ Remetente*/
			if(data[10] != null){
				this.nrIdentificacaoRemetente = data[10].trim(); 
			}
			
			/*Situação tributaria remetente a partir da IE*/
			if(data[11] != null){
				this.setNrIERemetente(data[11].trim());
				if("ISENTO".equalsIgnoreCase(this.nrIERemetente)){
					this.tpSituacaoTributariaRemetente = "NC";					
				}else{
					this.tpSituacaoTributariaRemetente = "CO";
				}	
			}
			
			if(data[12] != null){
				this.nmRemetente = data[12].trim(); 
			}			
			
			/*Municipio de origem*/
			if(data[14] != null){
				this.nmMunicipioOrigem = data[14].trim();
			}
			
			/*Estado origem*/
			if(data[15] != null){
				this.sgEstadoOrigem = data[15].trim(); 
			}
			
			/*IBGE Origem*/
			if(data[16] != null){
				this.cdIBGEMunicipioOrigem = data[16].trim();
			}
			
			/*CNPJ Destinatario*/			
			if(data[19] != null){
				this.nrIdentificacaoDestinatario = data[19].trim();
			}	
			
			/*Situação tributaria remetente a partir da IE*/
			if(data[20] != null){
				this.setNrIEDestinatario(data[20].trim());
				if("ISENTO".equalsIgnoreCase(this.nrIEDestinatario)){
					this.tpSituacaoTributariaDestinatario = "NC";
				}else{
					this.tpSituacaoTributariaDestinatario = "CO";
				}	
			}			
						
			if(data[21] != null){
				this.setNmDestinatario(data[21].trim());
			}			
			
			/*Municipio de destino*/
			if(data[23] != null){
				this.nmMunicipioDestino = data[23].trim();
			}			
			
			/*Estado destino*/
			if(data[24] != null){
				this.sgEstadoDestino = data[24].trim(); 
			}			
			
			/*IBGE Destino*/
			if(data[25] != null){
				this.cdIBGEMunicipioDestino = data[25].trim();
			}			
			
			/*Tipo Frete 1 = CIF 2 Fob*/
			if(data[26] != null){
				this.cdFrete = Integer.valueOf(data[26].trim());
			}
			
			/*Tipo Sevico*/
			if(data[28] != null){
				if(data[28].trim().contains("AEREO")){
					this.tpServico = "ANC";
					this.tpModal = "A";
				} else {
					this.tpServico = "RNC";
					this.tpModal   = "R";
				}
			}
			
		}
	}
	
	public Integer getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public BigDecimal getPsReal() {
		return psReal;
	}

	public void setPsReal(BigDecimal psReal) {
		this.psReal = psReal;
	}

	public BigDecimal getPsAferido() {
		return psAferido;
	}

	public void setPsAferido(BigDecimal psAferido) {
		this.psAferido = psAferido;
	}

	public BigDecimal getPsCubado() {
		return psCubado;
	}

	public void setPsCubado(BigDecimal psCubado) {
		this.psCubado = psCubado;
	}

	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}

	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}

	public Integer getCdFrete() {
		return cdFrete;
	}

	public void setCdFrete(Integer cdFrete) {
		this.cdFrete = cdFrete;
	}

	public String getTpFrete() {
		tpFrete = "C";
		tpDevedorFrete = "R";
		if(cdFrete == 2){
			tpFrete = "F";
			tpDevedorFrete = "D";
		}
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public Long getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(Long nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getTpSituacaoTributariaDestinatario() {
		return tpSituacaoTributariaDestinatario;
	}

	public void setTpSituacaoTributariaDestinatario(
			String tpSituacaoTributariaDestinatario) {
		this.tpSituacaoTributariaDestinatario = tpSituacaoTributariaDestinatario;
	}

	public String getTpSituacaoTributariaRemetente() {
		return tpSituacaoTributariaRemetente;
	}

	public void setTpSituacaoTributariaRemetente(
			String tpSituacaoTributariaRemetente) {
		this.tpSituacaoTributariaRemetente = tpSituacaoTributariaRemetente;
	}

	public String getNrIdentificacaoRemetente() {
		return nrIdentificacaoRemetente;
	}

	public void setNrIdentificacaoRemetente(String nrIdentificacaoRemetente) {
		this.nrIdentificacaoRemetente = nrIdentificacaoRemetente;
	}

	public String getNrIdentificacaoDestinatario() {
		return nrIdentificacaoDestinatario;
	}

	public void setNrIdentificacaoDestinatario(
			String nrIdentificacaoDestinatario) {
		this.nrIdentificacaoDestinatario = nrIdentificacaoDestinatario;
	}

	public String getSgEstadoOrigem() {
		return sgEstadoOrigem;
	}

	public void setSgEstadoOrigem(String sgEstadoOrigem) {
		this.sgEstadoOrigem = sgEstadoOrigem;
	}

	public String getSgEstadoDestino() {
		return sgEstadoDestino;
	}

	public void setSgEstadoDestino(String sgEstadoDestino) {
		this.sgEstadoDestino = sgEstadoDestino;
	}

	public String getCdIBGEMunicipioOrigem() {
		if (cdIBGEMunicipioOrigem != null
				&& cdIBGEMunicipioOrigem.length() == 7) {
			return cdIBGEMunicipioOrigem.substring(2);
		}
		return cdIBGEMunicipioOrigem;
	}

	public String getCdIBGEMunicipioOrigemOriginal() {
		return cdIBGEMunicipioOrigem;
	}

	public void setCdIBGEMunicipioOrigem(String cdIBGEMunicipioOrigem) {
		this.cdIBGEMunicipioOrigem = cdIBGEMunicipioOrigem;
	}

	public String getCdIBGEMunicipioDestino() {
		if (cdIBGEMunicipioDestino != null
				&& cdIBGEMunicipioDestino.length() == 7) {
			return cdIBGEMunicipioDestino.substring(2);
		}
		return cdIBGEMunicipioDestino;
	}
	
	public String getCdIBGEMunicipioDestinoOriginal() {
		return cdIBGEMunicipioDestino;
	}	

	public void setCdIBGEMunicipioDestino(String cdIBGEMunicipioDestino) {
		this.cdIBGEMunicipioDestino = cdIBGEMunicipioDestino;
	}

	public String getTpServico() {
		return tpServico;
	}

	public void setTpServico(String tpServico) {
		this.tpServico = tpServico;
	}

	public String getTpModal() {
		return tpModal;
	}

	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}

	public String getNrIERemetente() {
		return nrIERemetente;
	}

	public void setNrIERemetente(String nrIERemetente) {
		this.nrIERemetente = nrIERemetente;
	}

	public String getNrIEDestinatario() {
		return nrIEDestinatario;
	}

	public void setNrIEDestinatario(String nrIEDestinatario) {
		this.nrIEDestinatario = nrIEDestinatario;
	}

	public String getNmMunicipioOrigem() {
		return nmMunicipioOrigem;
	}

	public void setNmMunicipioOrigem(String nmMunicipioOrigem) {
		this.nmMunicipioOrigem = nmMunicipioOrigem;
	}

	public String getNmMunicipioDestino() {
		return nmMunicipioDestino;
	}

	public void setNmMunicipioDestino(String nmMunicipioDestino) {
		this.nmMunicipioDestino = nmMunicipioDestino;
	}

	public String getTpDevedorFrete() {
		return tpDevedorFrete;
	}

	public void setTpDevedorFrete(String tpDevedorFrete) {
		this.tpDevedorFrete = tpDevedorFrete;
	}

	public String getNmRemetente() {
		return nmRemetente;
	}

	public void setNmRemetente(String nmRemetente) {
		this.nmRemetente = nmRemetente;
	}

	public String getNmDestinatario() {
		return nmDestinatario;
	}
	
	public void setNmDestinatario(String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}

}
