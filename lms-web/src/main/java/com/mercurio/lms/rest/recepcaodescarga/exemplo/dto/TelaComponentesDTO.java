package com.mercurio.lms.rest.recepcaodescarga.exemplo.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.dto.TipoServicoDTO;
import com.mercurio.lms.rest.municipios.EmpresaDTO;
import com.mercurio.lms.rest.municipios.dto.PaisDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
 
public class TelaComponentesDTO extends BaseDTO { 
	private static final long serialVersionUID = 1L; 
	
	private YearMonthDay data;
	private DateTime dataHora;
	
	private String dsDescricao;
	private byte[] dcArquivo;
	
	private EmpresaDTO empresa;
	
	private PaisDTO pais;
	private UnidadeFederativaDTO unidadeFederativa;
	
	private DomainValue situacao;
	private DomainValue mes;
	
	private EmpresaDTO empresaFromChosen;
	private TipoServicoDTO tipoServico;
	
	private String email;
	private String descricao;
	
	private String nrIdentificacao;
	private String nrIdentificacaoCnpj;
	
	private BigDecimal valor;
	private BigDecimal cubagem;
	private BigDecimal peso;

	public YearMonthDay getData() {
		return data;
	}

	public void setData(YearMonthDay data) {
		this.data = data;
	}
	
	public String getDsDescricao() {
		return dsDescricao;
	}

	public void setDsDescricao(String dsDescricao) {
		this.dsDescricao = dsDescricao;
	}

	public DateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(DateTime dataHora) {
		this.dataHora = dataHora;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}

	public PaisDTO getPais() {
		return pais;
	}

	public void setPais(PaisDTO pais) {
		this.pais = pais;
	}

	public UnidadeFederativaDTO getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativaDTO unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public DomainValue getSituacao() {
		return situacao;
	}

	public void setSituacao(DomainValue situacao) {
		this.situacao = situacao;
	}

	public DomainValue getMes() {
		return mes;
	}

	public void setMes(DomainValue mes) {
		this.mes = mes;
	}

	public EmpresaDTO getEmpresaFromChosen() {
		return empresaFromChosen;
	}

	public void setEmpresaFromChosen(EmpresaDTO empresaFromChosen) {
		this.empresaFromChosen = empresaFromChosen;
	}

	public TipoServicoDTO getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServicoDTO tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}

	public String getNrIdentificacaoCnpj() {
		return nrIdentificacaoCnpj;
	}

	public void setNrIdentificacaoCnpj(String nrIdentificacaoCnpj) {
		this.nrIdentificacaoCnpj = nrIdentificacaoCnpj;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getCubagem() {
		return cubagem;
	}

	public void setCubagem(BigDecimal cubagem) {
		this.cubagem = cubagem;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	
} 
