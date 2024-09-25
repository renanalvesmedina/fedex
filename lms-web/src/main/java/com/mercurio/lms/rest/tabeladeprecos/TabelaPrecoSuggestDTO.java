package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.MoedaDTO;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.JTFormatUtils;

public class TabelaPrecoSuggestDTO extends BaseDTO{

	private static final long serialVersionUID = 1L;
	
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	
	private Long idTabelaPreco;
	private String tpTipoTabelaPreco;
	private Integer nrVersao;
	private String tpSubtipoTabelaPreco;
	private String descricao;
	private String nomeTabela;
	private String nomeTabelaAux;
    private DomainValue tpModal; // tabelaPreco.tipoTabelaPreco.servico
    private DomainValue tpAbrangencia;// tabelaPreco.tipoTabelaPreco.servico
    
    private MoedaDTO moeda;

    public TabelaPrecoSuggestDTO(){
    }

	public TabelaPrecoSuggestDTO(TabelaPreco o){
		this.idTabelaPreco = o.getIdTabelaPreco();
		this.tpTipoTabelaPreco = o.getTipoTabelaPreco().getTpTipoTabelaPreco().getDescription().toString();
		this.nrVersao = o.getTipoTabelaPreco().getNrVersao();
		this.tpSubtipoTabelaPreco = o.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco();
		this.descricao = o.getDsDescricao();
		this.nomeTabela = o.getTabelaPrecoString();
		this.nomeTabelaAux = montaNomeTabelaAux(o);
		this.tpModal = o.getTipoTabelaPreco().getServico().getTpModal();
		this.tpAbrangencia = o.getTipoTabelaPreco().getServico().getTpAbrangencia();
		
		if(o.getMoeda() != null){
			this.moeda = new MoedaDTO(o.getMoeda().getIdMoeda(), o.getMoeda().getSgMoeda(), o.getMoeda().getSiglaSimbolo());
		}
	}
	
	private String montaNomeTabelaAux(TabelaPreco tabelaPreco){
		StringBuilder retorno = new StringBuilder(tabelaPreco.getTabelaPrecoStringCompleto());
		if (tabelaPreco.getDtVigenciaInicial() != null){
		    retorno.append(" | ");
	        retorno.append(JTFormatUtils.format(tabelaPreco.getDtVigenciaInicial(), DD_MM_YYYY));
	        retorno.append(tabelaPreco.getDtVigenciaFinal() != null ? " à "+JTFormatUtils.format(tabelaPreco.getDtVigenciaFinal(), DD_MM_YYYY) : "");
		}
		
		return retorno.toString();
	}

	public Long getIdTabelaPreco() {
		return idTabelaPreco;
	}

	public void setIdTabelaPreco(Long idTabelaPreco) {
		this.idTabelaPreco = idTabelaPreco;
	}

	public String getTpTipoTabelaPreco() {
		return tpTipoTabelaPreco;
	}

	public void setTpTipoTabelaPreco(String tpTipoTabelaPreco) {
		this.tpTipoTabelaPreco = tpTipoTabelaPreco;
	}

	public Integer getNrVersao() {
		return nrVersao;
	}

	public void setNrVersao(Integer nrVersao) {
		this.nrVersao = nrVersao;
	}

	public String getTpSubtipoTabelaPreco() {
		return tpSubtipoTabelaPreco;
	}

	public void setTpSubtipoTabelaPreco(String tpSubtipoTabelaPreco) {
		this.tpSubtipoTabelaPreco = tpSubtipoTabelaPreco;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeTabela() {
		return nomeTabela;
	}

	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}

	public String getNomeTabelaAux() {
		return nomeTabelaAux;
	}

	public void setNomeTabelaAux(String nomeTabelaAux) {
		this.nomeTabelaAux = nomeTabelaAux;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public MoedaDTO getMoeda() {
		return moeda;
	}

	public void setMoeda(MoedaDTO moeda) {
		this.moeda = moeda;
	}

}
