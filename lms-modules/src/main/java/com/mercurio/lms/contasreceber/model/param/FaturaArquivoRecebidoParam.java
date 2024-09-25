package com.mercurio.lms.contasreceber.model.param;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemPreFatura;
import com.mercurio.lms.contasreceber.model.OcorrenciaPreFatura;
import com.mercurio.lms.contasreceber.model.PreFatura;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;



public class FaturaArquivoRecebidoParam {
	
	private Fatura fatura;
	
	private PreFatura preFatura;
	
	private Cliente cliente;
	
	private Cedente cedente;
	
	private DivisaoCliente divisaoCliente;
	
	private YearMonthDay dtEmissao;
	
	private YearMonthDay dtVencimento;
	
	private Boolean blGerarBoleto;
	
	private Long nrPreFatura;
	
	private String nmArquivo;
	
	private OcorrenciaPreFatura ocorrenciaPreFatura;
	
	private List idsDevedorDocServFat;
	
	private List itensPreFatura;
	
	private List idsFatura;

	private Boolean blClientDHL = Boolean.FALSE;

	public Boolean getBlGerarBoleto() {
		return blGerarBoleto;
	}

	public void setBlGerarBoleto(Boolean blGerarBoleto) {
		this.blGerarBoleto = blGerarBoleto;
	}

	public Cedente getCedente() {
		return cedente;
	}

	public void setCedente(Cedente cedente) {
		this.cedente = cedente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public YearMonthDay getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public Long getNrPreFatura() {
		return nrPreFatura;
	}

	public void setNrPreFatura(Long nrPreFatura) {
		this.nrPreFatura = nrPreFatura;
	}

	public List getIdsDevedorDocServFat() {
		return idsDevedorDocServFat;
	}
	
	public void addIdsDevedorDocServFat(Long idDevedorDocServFat) {
		this.idsDevedorDocServFat.add(idDevedorDocServFat);
	}
	
	public void setIdsDevedorDocServFat(List idsDevedorDocServFat){
		this.idsDevedorDocServFat = idsDevedorDocServFat;
	}	

	public List getIdsFatura() {
		return idsFatura;
	}

	public void addIdsFatura(Long idFatura) {
		this.idsFatura.add(idFatura);
	}

	public List getItensPreFatura() {
		return itensPreFatura;
	}
	
	public void setItenPreFatura(List itensPreFatura){
		this.itensPreFatura = itensPreFatura;
	}

	public void addItemPreFatura(ItemPreFatura itemPreFatura) {
		this.itensPreFatura.add(itemPreFatura);
	}	
	
	public PreFatura getPreFatura() {
		return preFatura;
	}

	public void setPreFatura(PreFatura preFatura) {
		this.preFatura = preFatura;
	}

	public OcorrenciaPreFatura getOcorrenciaPreFatura() {
		return ocorrenciaPreFatura;
	}

	public void setOcorrenciaPreFatura(OcorrenciaPreFatura ocorrenciaPreFatura) {
		this.ocorrenciaPreFatura = ocorrenciaPreFatura;
	}

	public Boolean getBlClientDHL() {
		return blClientDHL;
	}
	
	public void setBlClientDHL(Boolean blClientDHL) {
		this.blClientDHL = blClientDHL;
	}
	
	public boolean isClienteDHL() {
		return Boolean.TRUE.equals(blClientDHL);
	}

	public FaturaArquivoRecebidoParam() {
		super();
		this.idsFatura = new ArrayList();
		this.itensPreFatura = new ArrayList();
		this.idsDevedorDocServFat = new ArrayList();
	}
	
	

	
}
