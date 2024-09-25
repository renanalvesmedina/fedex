package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.DispCarregDescQtde;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.IntegranteEquipe;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contasreceber.model.ComplementoEmpresaCedente;
import com.mercurio.lms.contratacaoveiculos.model.RegraLiberacaoReguladora;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.expedicao.model.Produto;
import com.mercurio.lms.fretecarreteiroviagem.model.CriterioAplicSimulacao;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.vendas.model.MotivoProibidoEmbarque;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.TipoClassificacaoCliente;
import com.mercurio.lms.vendas.model.TipoVisita;

/** @author Hibernate CodeGenerator */
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static String TP_EMPRESA_CIA_AEREA = "C";

	/** identifier field */
	private Long idEmpresa;

	/** persistent field */
	private DomainValue tpEmpresa;

	/** nullable persistent field */
	private Long nrDac;

	/** nullable persistent field */
	private BigDecimal pcTaxaCombustivel;

	/** nullable persistent field */
	private String dsHomePage;

	private Integer cdIATA;

	/** persistent field */
	private String sgEmpresa;

	private DomainValue tpSituacao;

	/** nullable persistent field */
	private Pessoa pessoa;

	/** persistent field */
	private List<Produto> produtos;

	/** persistent field */
	private List<CriterioAplicSimulacao> criterioAplicSimulacoes;

	/** persistent field */
	private List<RegraLiberacaoReguladora> regraLiberacaoReguladoras;

	/** persistent field */
	private List<FilialCiaAerea> filialCiaAereas;

	/** persistent field */
	private List<IntegranteEquipe> integranteEquipes;

	/** persistent field */
	private List<IntegranteEqOperac> integrantesEqOperac;

	/** persistent field */
	private List<MotivoProibidoEmbarque> motivoProibidoEmbarques;

	/** persistent field */
	private List<ProcessoPce> processoPces;

	/** persistent field */
	private List<DispositivoUnitizacao> dispositivoUnitizacoes;

	/** persistent field */
	private List<TipoVisita> tipoVisitas;

	/** persistent field */
	private List<OcorrenciaNaoConformidade> ocorrenciaNaoConformidades;

	/** persistent field */
	private List<OcorrenciaEntrega> ocorrenciaEntregas;

	/** persistent field */
	private List<TipoClassificacaoCliente> tipoClassificacaoClientes;

	/** persistent field */
	private List<Filial> filiais;

	/** persistent field */
	private List<CiaFilialMercurio> ciaFilialMercurios;

	/** persistent field */
	private List<EquipeOperacao> equipeOperacoes;

	private List<EmpresaUsuario> usuariosEmpresa;

	/** persistent field */
	private List<DispCarregDescQtde> dispCarregDescQtdes;

	/** persistent field */
	private List<ComplementoEmpresaCedente> complementoEmpresaCedentes;

	/** persistent field */
	private Boolean blPodeAgendar;

	/** persistent field */
	private Filial filialTomadorFrete;
	
	/** nullable persistent field */
	private Long nrContaCorrente;

	public Empresa() {
	}

	public Empresa(Long idEmpresa, DomainValue tpEmpresa, String nmPessoa,
			String nmFantasia, DomainValue tpIdentificacao,
			String nrIdentificacao, DomainValue tpSituacao, String sgEmpresa) {
		this.idEmpresa = idEmpresa;
		this.tpEmpresa = tpEmpresa;
		this.pessoa = new Pessoa();
		this.pessoa.setNmPessoa(nmPessoa);
		this.pessoa.setNmFantasia( nmFantasia );
		this.pessoa.setTpIdentificacao(tpIdentificacao);
		this.pessoa.setNrIdentificacao(nrIdentificacao);
		this.tpSituacao = tpSituacao;
		this.sgEmpresa = sgEmpresa;
	}

	public Long getIdEmpresa() {
		return this.idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public DomainValue getTpEmpresa() {
		return this.tpEmpresa;
	}

	public void setTpEmpresa(DomainValue tpEmpresa) {
		this.tpEmpresa = tpEmpresa;
	}

	public Long getNrDac() {
		return this.nrDac;
	}

	public void setNrDac(Long nrDac) {
		this.nrDac = nrDac;
	}

	public BigDecimal getPcTaxaCombustivel() {
		return this.pcTaxaCombustivel;
	}

	public void setPcTaxaCombustivel(BigDecimal pcTaxaCombustivel) {
		this.pcTaxaCombustivel = pcTaxaCombustivel;
	}

	public String getDsHomePage() {
		return this.dsHomePage;
	}

	public void setDsHomePage(String dsHomepage) {
		this.dsHomePage = dsHomepage;
	}

	public Integer getCdIATA() {
		return cdIATA;
	}

	public void setCdIATA(Integer cdIATA) {
		this.cdIATA = cdIATA;
	}

	public String getSgEmpresa() {
		return sgEmpresa;
	}

	public void setSgEmpresa(String sgEmpresa) {
		this.sgEmpresa = sgEmpresa;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@ParametrizedAttribute(type = Produto.class) 
	public List<Produto> getProdutos() {
		return this.produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	@ParametrizedAttribute(type = CriterioAplicSimulacao.class) 
	public List<CriterioAplicSimulacao> getCriterioAplicSimulacoes() {
		return this.criterioAplicSimulacoes;
	}

	public void setCriterioAplicSimulacoes(
			List<CriterioAplicSimulacao> criterioAplicSimulacoes) {
		this.criterioAplicSimulacoes = criterioAplicSimulacoes;
	}

	@ParametrizedAttribute(type = RegraLiberacaoReguladora.class)
	public List<RegraLiberacaoReguladora> getRegraLiberacaoReguladoras() {
		return this.regraLiberacaoReguladoras;
	}

	public void setRegraLiberacaoReguladoras(
			List<RegraLiberacaoReguladora> regraLiberacaoReguladoras) {
		this.regraLiberacaoReguladoras = regraLiberacaoReguladoras;
	}

	@ParametrizedAttribute(type = FilialCiaAerea.class)
	public List<FilialCiaAerea> getFilialCiaAereas() {
		return this.filialCiaAereas;
	}

	public void setFilialCiaAereas(List<FilialCiaAerea> filialCiaAereas) {
		this.filialCiaAereas = filialCiaAereas;
	}

	@ParametrizedAttribute(type = IntegranteEquipe.class)
	public List<IntegranteEquipe> getIntegranteEquipes() {
		return this.integranteEquipes;
	}

	public void setIntegranteEquipes(List<IntegranteEquipe> integranteEquipes) {
		this.integranteEquipes = integranteEquipes;
	}

	@ParametrizedAttribute(type = IntegranteEqOperac.class)
	public List<IntegranteEqOperac> getIntegrantesEqOperac() {
		return integrantesEqOperac;
	}

	public void setIntegrantesEqOperac(
			List<IntegranteEqOperac> integrantesEqOperac) {
		this.integrantesEqOperac = integrantesEqOperac;
	}

	@ParametrizedAttribute(type = MotivoProibidoEmbarque.class)
	public List<MotivoProibidoEmbarque> getMotivoProibidoEmbarques() {
		return this.motivoProibidoEmbarques;
	}

	public void setMotivoProibidoEmbarques(
			List<MotivoProibidoEmbarque> motivoProibidoEmbarques) {
		this.motivoProibidoEmbarques = motivoProibidoEmbarques;
	}

	@ParametrizedAttribute(type = ProcessoPce.class)
	public List<ProcessoPce> getProcessoPces() {
		return this.processoPces;
	}

	public void setProcessoPces(List<ProcessoPce> processoPces) {
		this.processoPces = processoPces;
	}

	@ParametrizedAttribute(type = DispositivoUnitizacao.class)
	public List<DispositivoUnitizacao> getDispositivoUnitizacoes() {
		return this.dispositivoUnitizacoes;
	}

	public void setDispositivoUnitizacoes(
			List<DispositivoUnitizacao> dispositivoUnitizacoes) {
		this.dispositivoUnitizacoes = dispositivoUnitizacoes;
	}

	@ParametrizedAttribute(type = TipoVisita.class)
	public List<TipoVisita> getTipoVisitas() {
		return this.tipoVisitas;
	}

	public void setTipoVisitas(List<TipoVisita> tipoVisitas) {
		this.tipoVisitas = tipoVisitas;
	}

	@ParametrizedAttribute(type = OcorrenciaNaoConformidade.class)
	public List<OcorrenciaNaoConformidade> getOcorrenciaNaoConformidades() {
		return this.ocorrenciaNaoConformidades;
	}

	public void setOcorrenciaNaoConformidades(
			List<OcorrenciaNaoConformidade> ocorrenciaNaoConformidades) {
		this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
	}

	@ParametrizedAttribute(type = OcorrenciaEntrega.class)
	public List<OcorrenciaEntrega> getOcorrenciaEntregas() {
		return this.ocorrenciaEntregas;
	}

	public void setOcorrenciaEntregas(List<OcorrenciaEntrega> ocorrenciaEntregas) {
		this.ocorrenciaEntregas = ocorrenciaEntregas;
	}

	@ParametrizedAttribute(type = TipoClassificacaoCliente.class)
	public List<TipoClassificacaoCliente> getTipoClassificacaoClientes() {
		return this.tipoClassificacaoClientes;
	}

	public void setTipoClassificacaoClientes(
			List<TipoClassificacaoCliente> tipoClassificacaoClientes) {
		this.tipoClassificacaoClientes = tipoClassificacaoClientes;
	}

	@ParametrizedAttribute(type = Filial.class)
	public List<Filial> getFiliais() {
		return this.filiais;
	}

	public void setFiliais(List<Filial> filiais) {
		this.filiais = filiais;
	}

	@ParametrizedAttribute(type = CiaFilialMercurio.class)
	public List<CiaFilialMercurio> getCiaFilialMercurios() {
		return this.ciaFilialMercurios;
	}

	public void setCiaFilialMercurios(List<CiaFilialMercurio> ciaFilialMercurios) {
		this.ciaFilialMercurios = ciaFilialMercurios;
	}

	@ParametrizedAttribute(type = EquipeOperacao.class)
	public List<EquipeOperacao> getEquipeOperacoes() {
		return this.equipeOperacoes;
	}

	public void setEquipeOperacoes(List<EquipeOperacao> equipeOperacoes) {
		this.equipeOperacoes = equipeOperacoes;
	}

	@ParametrizedAttribute(type = EmpresaUsuario.class)
	public List<EmpresaUsuario> getUsuariosEmpresa() {
		return usuariosEmpresa;
	}
	
	public void setUsuariosEmpresa(List<EmpresaUsuario> usuariosEmpresa) {
		this.usuariosEmpresa = usuariosEmpresa;
	}

	@ParametrizedAttribute(type = DispCarregDescQtde.class)
	public List<DispCarregDescQtde> getDispCarregDescQtdes() {
		return dispCarregDescQtdes;
	}

	public void setDispCarregDescQtdes(
			List<DispCarregDescQtde> dispCarregDescQtdes) {
		this.dispCarregDescQtdes = dispCarregDescQtdes;
	}

	@ParametrizedAttribute(type = ComplementoEmpresaCedente.class)
	public List<ComplementoEmpresaCedente> getComplementoEmpresaCedentes() {
		return this.complementoEmpresaCedentes;
	}

	public void setComplementoEmpresaCedentes(
			List<ComplementoEmpresaCedente> complementoEmpresaCedentes) {
		this.complementoEmpresaCedentes = complementoEmpresaCedentes;
	}

	public Boolean getBlPodeAgendar() {
		return blPodeAgendar;
	}

	public void setBlPodeAgendar(Boolean blPodeAgendar) {
		this.blPodeAgendar = blPodeAgendar;
	}

	public Filial getFilialTomadorFrete() {
		return filialTomadorFrete;
	}

	public void setFilialTomadorFrete(Filial filialTomadorFrete) {
		this.filialTomadorFrete = filialTomadorFrete;
	}

	public Long getNrContaCorrente() {
		return nrContaCorrente;
	}

	public void setNrContaCorrente(Long nrContaCorrente) {
		this.nrContaCorrente = nrContaCorrente;
	}	
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idEmpresa", getIdEmpresa())
			.toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Empresa))
			return false;
		Empresa castOther = (Empresa) other;
		return new EqualsBuilder().append(this.getIdEmpresa(),
				castOther.getIdEmpresa()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdEmpresa()).toHashCode();
	}

}
