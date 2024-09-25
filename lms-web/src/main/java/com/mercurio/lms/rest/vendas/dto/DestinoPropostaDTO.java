package com.mercurio.lms.rest.vendas.dto;

import java.util.List;

import com.mercurio.adsm.rest.BaseDTO;

public class DestinoPropostaDTO extends BaseDTO {

    private static final long serialVersionUID = -3865184893281864947L;

    private Long idDestinoProposta;
    private Boolean blGeraDestinoProposta;
    private String dsDestino;
    private Long idRotaPreco;
    private List<DestinoPropostaParcelaDTO> parcelas;
    private Long idSimulacao;
    private Long idAeroporto;
    private Long idUnidadeFederativa;
    private DestinoPropostaRegiaoDTO regiao;
    
    public DestinoPropostaDTO(String dsDestino) {
        super();
        this.dsDestino = dsDestino;
    }
    
    public DestinoPropostaDTO() {
    	super();
    }
    
    
    public DestinoPropostaDTO(String dsDestino, List<DestinoPropostaParcelaDTO> parcelas){
        this(dsDestino);
        this.parcelas = parcelas;
    }
    
    public String getDsDestino() {
        return dsDestino;
    }
    public void setDsDestino(String dsDestino) {
        this.dsDestino = dsDestino;
    }
    public List<DestinoPropostaParcelaDTO> getParcelas() {
        return parcelas;
    }
    public void setParcelas(List<DestinoPropostaParcelaDTO> parcelas) {
        this.parcelas = parcelas;
    }

    public Boolean getBlGeraDestinoProposta() {
        return blGeraDestinoProposta;
    }

    public void setBlGeraDestinoProposta(Boolean blGeraDestinoProposta) {
        this.blGeraDestinoProposta = blGeraDestinoProposta;
    }

    public Long getIdRotaPreco() {
        return idRotaPreco;
    }

    public void setIdRotaPreco(Long idRotaPreco) {
        this.idRotaPreco = idRotaPreco;
    }

    public Long getIdSimulacao() {
        return idSimulacao;
    }

    public void setIdSimulacao(Long idSimulacao) {
        this.idSimulacao = idSimulacao;
    }

    public Long getIdAeroporto() {
        return idAeroporto;
    }

    public void setIdAeroporto(Long idAeroporto) {
        this.idAeroporto = idAeroporto;
    }

    public Long getIdUnidadeFederativa() {
        return idUnidadeFederativa;
    }

    public void setIdUnidadeFederativa(Long idUnidadeFederativa) {
        this.idUnidadeFederativa = idUnidadeFederativa;
    }

	public Long getIdDestinoProposta() {
		return idDestinoProposta;
	}

	public void setIdDestinoProposta(Long idDestinoProposta) {
		this.idDestinoProposta = idDestinoProposta;
	}

    public DestinoPropostaRegiaoDTO getRegiao() {
        return regiao;
    }

    public void setRegiao(DestinoPropostaRegiaoDTO regiao) {
        this.regiao = regiao;
    }
    
}
