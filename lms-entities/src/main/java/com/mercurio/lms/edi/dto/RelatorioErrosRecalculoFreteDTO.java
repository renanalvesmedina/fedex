package com.mercurio.lms.edi.dto;

import java.io.Serializable;

import com.mercurio.lms.expedicao.model.Conhecimento;

/**
 * @author JonasFE
 *
 */
public class RelatorioErrosRecalculoFreteDTO implements Serializable,
		Comparable<RelatorioErrosRecalculoFreteDTO> {
	
	private static final long serialVersionUID = 1L;
	
	private Conhecimento conhecimento;
	
	private Long nrDocumentoArquivo;
	
	private String dsMensagemErro;

	public String getDsConhecimento() {
		return nrDocumentoArquivo == null ? conhecimento
				.getFilialByIdFilialOrigem().getSgFilial()
				+ " - "
				+ conhecimento.getNrConhecimento() : String
				.valueOf(nrDocumentoArquivo);
	}

	public String getDsMensagemErro() {
		return dsMensagemErro;
	}

	public void setDsMensagemErro(String dsMensagemErro) {
		this.dsMensagemErro = dsMensagemErro;
	}

	public Conhecimento getConhecimento() {
		return conhecimento;
	}

	public void setConhecimento(Conhecimento conhecimento) {
		this.conhecimento = conhecimento;
	}

	public int compareTo(RelatorioErrosRecalculoFreteDTO o) {
		if(conhecimento == null){
			return nrDocumentoArquivo.compareTo(o.getNrDocumentoArquivo());
		} else {
			int position = conhecimento
					.getFilialByIdFilialOrigem()
					.getSgFilial()
					.compareTo(
							o.getConhecimento().getFilialByIdFilialOrigem()
									.getSgFilial());
			if(position == 0){
				return conhecimento.getNrConhecimento().compareTo(
						o.getConhecimento().getNrConhecimento());
			}
			return position;
		}
	}

	public Long getNrDocumentoArquivo() {
		return nrDocumentoArquivo;
	}

	public void setNrDocumentoArquivo(Long nrDocumentoArquivo) {
		this.nrDocumentoArquivo = nrDocumentoArquivo;
	}

}
