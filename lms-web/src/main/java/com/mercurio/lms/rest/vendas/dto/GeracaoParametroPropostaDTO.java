package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.TipoLocalizacaoMunicipioDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
import com.mercurio.lms.rest.tabeladeprecos.ProdutoEspecificoDTO;
import com.mercurio.lms.rest.tabeladeprecos.ServicoSuggestDTO;
import com.mercurio.lms.rest.tabeladeprecos.TabelaPrecoSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.DestinoPropostaDTO;
import com.mercurio.lms.rest.vendas.dto.DivisaoClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaDestinoAereoTableDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaDestinoTableDTO;

public class GeracaoParametroPropostaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long idSimulacao;
	private Long idProposta;
	private Long nrSimulacao;
    private DivisaoClienteSuggestDTO divisaoCliente;
    private ServicoSuggestDTO servico;
	
	// Aba Frete Peso
	private DomainValue tpIndicadorMinFretePeso;
	private BigDecimal vlMinFretePeso;
	private DomainValue tpIndicadorFreteMinimo;
	private BigDecimal vlFreteMinimo;
	private DomainValue tpIndicadorFretePeso;
	private BigDecimal vlFretePeso;
	private boolean blPagaPesoExcedente;
	private BigDecimal pcDiferencaFretePeso;
	
	// Aba Frete Valor
	private DomainValue tpIndicadorAdvalorem;
	private BigDecimal vlAdvalorem;
	private DomainValue tpDiferencaAdvalorem;
	private BigDecimal pcDiferencaAdvalorem;
	
	// Aba Frete Percentual
	private BigDecimal pcFretePercentual;
	private BigDecimal vlMinimoFretePercentual;
	private BigDecimal vlToneladaFretePercentual;
	private BigDecimal psFretePercentual;
	
	// Aba especificacoes
	private UnidadeFederativaDTO ufOrigem;
	private TipoLocalizacaoMunicipioDTO tipoLocalizacaoMunicipioOrigem ;
	private boolean blFreteExpedido;
	private boolean blFreteRecebido;
	
	private ClienteSuggestDTO cliente;
	private AeroportoSuggestDTO aeroportoReferencia;
	private DomainValue tpGeracaoProposta;
	private TabelaPrecoSuggestDTO tabelaPreco;
	
	private boolean generate;
	private boolean disableAll;
	
	private ProdutoEspecificoDTO produtoEspecifico;
	
	private List<ParametroPropostaDestinoTableDTO> listDestinosProposta;
	private List<ParametroPropostaDestinoAereoTableDTO> listaParametroPropostaDestinoAereoTableDTO;
	private List<DestinoPropostaDTO> listaDestinoPropostaDTO;
	
	private List<DestinoPropostaDTO> listaDestinoPropostaDestinos;
	private List<DestinoPropostaDTO> listaDestinoPropostaOrigens;
	
	public Long getIdSimulacao() {
		return idSimulacao;
	}
	public void setIdSimulacao(Long idSimulacao) {
		this.idSimulacao = idSimulacao;
	}
	public Long getIdProposta() {
		return idProposta;
	}
	public void setIdProposta(Long idProposta) {
		this.idProposta = idProposta;
	}
	public DomainValue getTpIndicadorMinFretePeso() {
		return tpIndicadorMinFretePeso;
	}
	public void setTpIndicadorMinFretePeso(DomainValue tpIndicadorMinFretePeso) {
		this.tpIndicadorMinFretePeso = tpIndicadorMinFretePeso;
	}
	public BigDecimal getVlMinFretePeso() {
		return vlMinFretePeso;
	}
	public void setVlMinFretePeso(BigDecimal vlMinFretePeso) {
		this.vlMinFretePeso = vlMinFretePeso;
	}
	public DomainValue getTpIndicadorFreteMinimo() {
		return tpIndicadorFreteMinimo;
	}
	public void setTpIndicadorFreteMinimo(DomainValue tpIndicadorFreteMinimo) {
		this.tpIndicadorFreteMinimo = tpIndicadorFreteMinimo;
	}
	public BigDecimal getVlFreteMinimo() {
		return vlFreteMinimo;
	}
	public void setVlFreteMinimo(BigDecimal vlFreteMinimo) {
		this.vlFreteMinimo = vlFreteMinimo;
	}
	public DomainValue getTpIndicadorFretePeso() {
		return tpIndicadorFretePeso;
	}
	public void setTpIndicadorFretePeso(DomainValue tpIndicadorFretePeso) {
		this.tpIndicadorFretePeso = tpIndicadorFretePeso;
	}
	public BigDecimal getVlFretePeso() {
		return vlFretePeso;
	}
	public void setVlFretePeso(BigDecimal vlFretePeso) {
		this.vlFretePeso = vlFretePeso;
	}
	public boolean isBlPagaPesoExcedente() {
		return blPagaPesoExcedente;
	}
	public void setBlPagaPesoExcedente(boolean blPagaPesoExcedente) {
		this.blPagaPesoExcedente = blPagaPesoExcedente;
	}
	public BigDecimal getPcDiferencaFretePeso() {
		return pcDiferencaFretePeso;
	}
	public void setPcDiferencaFretePeso(BigDecimal pcDiferencaFretePeso) {
		this.pcDiferencaFretePeso = pcDiferencaFretePeso;
	}
	public DomainValue getTpIndicadorAdvalorem() {
		return tpIndicadorAdvalorem;
	}
	public void setTpIndicadorAdvalorem(DomainValue tpIndicadorAdvalorem) {
		this.tpIndicadorAdvalorem = tpIndicadorAdvalorem;
	}
	public BigDecimal getVlAdvalorem() {
		return vlAdvalorem;
	}
	public void setVlAdvalorem(BigDecimal vlAdvalorem) {
		this.vlAdvalorem = vlAdvalorem;
	}
	public DomainValue getTpDiferencaAdvalorem() {
		return tpDiferencaAdvalorem;
	}
	public void setTpDiferencaAdvalorem(DomainValue tpDiferencaAdvalorem) {
		this.tpDiferencaAdvalorem = tpDiferencaAdvalorem;
	}
	public BigDecimal getPcDiferencaAdvalorem() {
		return pcDiferencaAdvalorem;
	}
	public void setPcDiferencaAdvalorem(BigDecimal pcDiferencaAdvalorem) {
		this.pcDiferencaAdvalorem = pcDiferencaAdvalorem;
	}
	public BigDecimal getPcFretePercentual() {
		return pcFretePercentual;
	}
	public void setPcFretePercentual(BigDecimal pcFretePercentual) {
		this.pcFretePercentual = pcFretePercentual;
	}
	public BigDecimal getVlMinimoFretePercentual() {
		return vlMinimoFretePercentual;
	}
	public void setVlMinimoFretePercentual(BigDecimal vlMinimoFretePercentual) {
		this.vlMinimoFretePercentual = vlMinimoFretePercentual;
	}
	public BigDecimal getVlToneladaFretePercentual() {
		return vlToneladaFretePercentual;
	}
	public void setVlToneladaFretePercentual(BigDecimal vlToneladaFretePercentual) {
		this.vlToneladaFretePercentual = vlToneladaFretePercentual;
	}
	public BigDecimal getPsFretePercentual() {
		return psFretePercentual;
	}
	public void setPsFretePercentual(BigDecimal psFretePercentual) {
		this.psFretePercentual = psFretePercentual;
	}
	public UnidadeFederativaDTO getUfOrigem() {
		return ufOrigem;
	}
	public void setUfOrigem(UnidadeFederativaDTO ufOrigem) {
		this.ufOrigem = ufOrigem;
	}
	public TipoLocalizacaoMunicipioDTO getTipoLocalizacaoMunicipioOrigem() {
		return tipoLocalizacaoMunicipioOrigem;
	}
	public void setTipoLocalizacaoMunicipioOrigem(TipoLocalizacaoMunicipioDTO tipoLocalizacaoMunicipioOrigem) {
		this.tipoLocalizacaoMunicipioOrigem = tipoLocalizacaoMunicipioOrigem;
	}
	public boolean isBlFreteExpedido() {
		return blFreteExpedido;
	}
	public void setBlFreteExpedido(boolean blFreteExpedido) {
		this.blFreteExpedido = blFreteExpedido;
	}
	public boolean isBlFreteRecebido() {
		return blFreteRecebido;
	}
	public void setBlFreteRecebido(boolean blFreteRecebido) {
		this.blFreteRecebido = blFreteRecebido;
	}
	public boolean isGenerate() {
		return generate;
	}
	public void setGenerate(boolean generate) {
		this.generate = generate;
	}
	public boolean isDisableAll() {
		return disableAll;
	}
	public void setDisableAll(boolean disableAll) {
		this.disableAll = disableAll;
	}
	public List<ParametroPropostaDestinoTableDTO> getListDestinosProposta() {
		return listDestinosProposta;
	}
	public void setListDestinosProposta(List<ParametroPropostaDestinoTableDTO> listDestinosProposta) {
		this.listDestinosProposta = listDestinosProposta;
	}
	public List<ParametroPropostaDestinoAereoTableDTO> getListaParametroPropostaDestinoAereoTableDTO() {
		return listaParametroPropostaDestinoAereoTableDTO;
	}
	public void setListaParametroPropostaDestinoAereoTableDTO(List<ParametroPropostaDestinoAereoTableDTO> listaParametroPropostaDestinoAereoTableDTO) {
		this.listaParametroPropostaDestinoAereoTableDTO = listaParametroPropostaDestinoAereoTableDTO;
	}
	public List<DestinoPropostaDTO> getListaDestinoPropostaDTO() {
		return listaDestinoPropostaDTO;
	}
	public void setListaDestinoPropostaDTO(List<DestinoPropostaDTO> listaDestinoPropostaDTO) {
		this.listaDestinoPropostaDTO = listaDestinoPropostaDTO;
	}
    public ClienteSuggestDTO getCliente() {
        return cliente;
    }
    public void setCliente(ClienteSuggestDTO cliente) {
        this.cliente = cliente;
    }
    public List<DestinoPropostaDTO> getListaDestinoPropostaDestinos() {
        return listaDestinoPropostaDestinos;
    }
    public void setListaDestinoPropostaDestinos(List<DestinoPropostaDTO> listaDestinoPropostaDestinos) {
        this.listaDestinoPropostaDestinos = listaDestinoPropostaDestinos;
    }
    public List<DestinoPropostaDTO> getListaDestinoPropostaOrigens() {
        return listaDestinoPropostaOrigens;
    }
    public void setListaDestinoPropostaOrigens(List<DestinoPropostaDTO> listaDestinoPropostaOrigens) {
        this.listaDestinoPropostaOrigens = listaDestinoPropostaOrigens;
    }
    public AeroportoSuggestDTO getAeroportoReferencia() {
        return aeroportoReferencia;
    }
    public void setAeroportoReferencia(AeroportoSuggestDTO aeroportoReferencia) {
        this.aeroportoReferencia = aeroportoReferencia;
    }
    public DomainValue getTpGeracaoProposta() {
        return tpGeracaoProposta;
    }
    public void setTpGeracaoProposta(DomainValue tpGeracaoProposta) {
        this.tpGeracaoProposta = tpGeracaoProposta;
    }
    public TabelaPrecoSuggestDTO getTabelaPreco() {
        return tabelaPreco;
    }
    public void setTabelaPreco(TabelaPrecoSuggestDTO tabelaPreco) {
        this.tabelaPreco = tabelaPreco;
    }
    public ProdutoEspecificoDTO getProdutoEspecifico() {
        return produtoEspecifico;
    }
    public void setProdutoEspecifico(ProdutoEspecificoDTO produtoEspecifico) {
        this.produtoEspecifico = produtoEspecifico;
    }
    public Long getNrSimulacao() {
        return nrSimulacao;
    }
    public void setNrSimulacao(Long nrSimulacao) {
        this.nrSimulacao = nrSimulacao;
    }
    public DivisaoClienteSuggestDTO getDivisaoCliente() {
        return divisaoCliente;
    }
    public void setDivisaoCliente(DivisaoClienteSuggestDTO divisaoCliente) {
        this.divisaoCliente = divisaoCliente;
    }
    public ServicoSuggestDTO getServico() {
        return servico;
    }
    public void setServico(ServicoSuggestDTO servico) {
        this.servico = servico;
    }
}
