package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class ParametroPropostaDestinoAereoTableDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String dsDestino;
	private Long idDestinoProposta;
	private Long idUnidadeFederativa;
	private Long idProposta;
	private Long idAeroporto;
	private Long idRotaPreco;

	private ParametroPropostaDestinoAereoColunaDTO colunaValorMinimo;
	private ParametroPropostaDestinoAereoColunaDTO colunaValoPorKg;
	private ParametroPropostaDestinoAereoColunaDTO colunaPesoMinimo;
	private ParametroPropostaDestinoAereoColunaDTO colunaProdutoEspecifico;
	private ParametroPropostaDestinoAereoColunaDTO colunaAdValorem1;
	private ParametroPropostaDestinoAereoColunaDTO colunaAdValorem2;
	private ParametroPropostaDestinoAereoColunaDTO colunaTxColUrbConv;
	private ParametroPropostaDestinoAereoColunaDTO colunaTxColUrbEm;
	private ParametroPropostaDestinoAereoColunaDTO colunaTxEntUrbConv;
	private ParametroPropostaDestinoAereoColunaDTO colunaTxEntUrbEm;
	private ParametroPropostaDestinoAereoColunaDTO colunaTxColIntConv;
	private ParametroPropostaDestinoAereoColunaDTO colunaTxColIntEm;
	private ParametroPropostaDestinoAereoColunaDTO colunaTxEntIntConv;
	private ParametroPropostaDestinoAereoColunaDTO colunaTxEntIntEm;
	

	public String getDsDestino() {
		return dsDestino;
	}

	public void setDsDestino(String dsDestino) {
		this.dsDestino = dsDestino;
	}

	public ParametroPropostaDestinoAereoColunaDTO getColunaValorMinimo() {
		return colunaValorMinimo;
	}

	public void setColunaValorMinimo(ParametroPropostaDestinoAereoColunaDTO colunaValorMinimo) {
		this.colunaValorMinimo = colunaValorMinimo;
	}

	public ParametroPropostaDestinoAereoColunaDTO getColunaValoPorKg() {
		return colunaValoPorKg;
	}

	public void setColunaValoPorKg(ParametroPropostaDestinoAereoColunaDTO colunaValoPorKg) {
		this.colunaValoPorKg = colunaValoPorKg;
	}

	public ParametroPropostaDestinoAereoColunaDTO getColunaProdutoEspecifico() {
		return colunaProdutoEspecifico;
	}

	public void setColunaProdutoEspecifico(ParametroPropostaDestinoAereoColunaDTO colunaProdutoEspecifico) {
		this.colunaProdutoEspecifico = colunaProdutoEspecifico;
	}

    public ParametroPropostaDestinoAereoColunaDTO getColunaAdValorem1() {
        return colunaAdValorem1;
    }

    public void setColunaAdValorem1(ParametroPropostaDestinoAereoColunaDTO colunaAdValorem1) {
        this.colunaAdValorem1 = colunaAdValorem1;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaAdValorem2() {
        return colunaAdValorem2;
    }

    public void setColunaAdValorem2(ParametroPropostaDestinoAereoColunaDTO colunaAdValorem2) {
        this.colunaAdValorem2 = colunaAdValorem2;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaTxColUrbConv() {
        return colunaTxColUrbConv;
    }

    public void setColunaTxColUrbConv(ParametroPropostaDestinoAereoColunaDTO colunaTxColUrbConv) {
        this.colunaTxColUrbConv = colunaTxColUrbConv;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaTxColUrbEm() {
        return colunaTxColUrbEm;
    }

    public void setColunaTxColUrbEm(ParametroPropostaDestinoAereoColunaDTO colunaTxColUrbEm) {
        this.colunaTxColUrbEm = colunaTxColUrbEm;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaTxEntUrbConv() {
        return colunaTxEntUrbConv;
    }

    public void setColunaTxEntUrbConv(ParametroPropostaDestinoAereoColunaDTO colunaTxEntUrbConv) {
        this.colunaTxEntUrbConv = colunaTxEntUrbConv;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaTxEntUrbEm() {
        return colunaTxEntUrbEm;
    }

    public void setColunaTxEntUrbEm(ParametroPropostaDestinoAereoColunaDTO colunaTxEntUrbEm) {
        this.colunaTxEntUrbEm = colunaTxEntUrbEm;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaTxColIntConv() {
        return colunaTxColIntConv;
    }

    public void setColunaTxColIntConv(ParametroPropostaDestinoAereoColunaDTO colunaTxColIntConv) {
        this.colunaTxColIntConv = colunaTxColIntConv;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaTxColIntEm() {
        return colunaTxColIntEm;
    }

    public void setColunaTxColIntEm(ParametroPropostaDestinoAereoColunaDTO colunaTxColIntEm) {
        this.colunaTxColIntEm = colunaTxColIntEm;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaTxEntIntConv() {
        return colunaTxEntIntConv;
    }

    public void setColunaTxEntIntConv(ParametroPropostaDestinoAereoColunaDTO colunaTxEntIntConv) {
        this.colunaTxEntIntConv = colunaTxEntIntConv;
    }

    public ParametroPropostaDestinoAereoColunaDTO getColunaTxEntIntEm() {
        return colunaTxEntIntEm;
    }

    public void setColunaTxEntIntEm(ParametroPropostaDestinoAereoColunaDTO colunaTxEntIntEm) {
        this.colunaTxEntIntEm = colunaTxEntIntEm;
    }

	public Long getIdDestinoProposta() {
		return idDestinoProposta;
	}

	public void setIdDestinoProposta(Long idDestinoProposta) {
		this.idDestinoProposta = idDestinoProposta;
	}

	public Long getIdUnidadeFederativa() {
		return idUnidadeFederativa;
	}

	public void setIdUnidadeFederativa(Long idUnidadeFederativa) {
		this.idUnidadeFederativa = idUnidadeFederativa;
	}

	public Long getIdProposta() {
		return idProposta;
	}

	public void setIdProposta(Long idProposta) {
		this.idProposta = idProposta;
	}

	public Long getIdAeroporto() {
		return idAeroporto;
	}

	public void setIdAeroporto(Long idAeroporto) {
		this.idAeroporto = idAeroporto;
	}

	public ParametroPropostaDestinoAereoColunaDTO getColunaPesoMinimo() {
		return colunaPesoMinimo;
	}

	public void setColunaPesoMinimo(ParametroPropostaDestinoAereoColunaDTO colunaPesoMinimo) {
		this.colunaPesoMinimo = colunaPesoMinimo;
	}

    public Long getIdRotaPreco() {
        return idRotaPreco;
    }

    public void setIdRotaPreco(Long idRotaPreco) {
        this.idRotaPreco = idRotaPreco;
    }

}
