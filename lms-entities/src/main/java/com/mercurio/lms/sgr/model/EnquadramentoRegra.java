package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.seguros.model.Seguradora;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.vendas.model.SeguroCliente;

public class EnquadramentoRegra implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idEnquadramentoRegra;
	private VarcharI18n dsEnquadramentoRegra;
	private Boolean blRequerPassaporteViagem;
	private Boolean blSeguroMercurio;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private DomainValue tpModal;
	private DomainValue tpVinculoMeioTransporte;
	private DomainValue tpAbrangencia;
	private DomainValue tpOperacao;
	private DomainValue tpGrauRiscoColetaEntrega;
	private DomainValue tpCriterioOrigem;
	private DomainValue tpCriterioDestino;
	private Moeda moeda;
	private ReguladoraSeguro reguladoraSeguro;
	private NaturezaProduto naturezaProduto;

	private List<Municipio> municipioEnquadramentosOrigem;
	private List<Municipio> municipioEnquadramentosDestino;

	private List<Pais> paisEnquadramentosOrigem;
	private List<Pais> paisEnquadramentosDestino;

	private List<UnidadeFederativa> unidadeFederativaEnquadramentosOrigem;
	private List<UnidadeFederativa> unidadeFederativaEnquadramentosDestino;

	private List<Filial> filialEnquadramentosOrigem;
	private List<Filial> filialEnquadramentosDestino;

	private List<ClienteEnquadramento> clienteEnquadramentos;

	private List<FaixaDeValor> faixaDeValors;

	// LMS-7253
	private Boolean blRegraGeral;
	// LMS-7285
	private ApoliceSeguro apoliceSeguro;
	private SeguroCliente seguroCliente;

	public Long getIdEnquadramentoRegra() {
		return idEnquadramentoRegra;
	}

	public void setIdEnquadramentoRegra(Long idEnquadramentoRegra) {
		this.idEnquadramentoRegra = idEnquadramentoRegra;
	}

	public VarcharI18n getDsEnquadramentoRegra() {
		return dsEnquadramentoRegra;
	}

	public void setDsEnquadramentoRegra(VarcharI18n dsEnquadramentoRegra) {
		this.dsEnquadramentoRegra = dsEnquadramentoRegra;
	}

	public Boolean getBlRequerPassaporteViagem() {
		return blRequerPassaporteViagem;
	}

	public void setBlRequerPassaporteViagem(Boolean blRequerPassaporteViagem) {
		this.blRequerPassaporteViagem = blRequerPassaporteViagem;
	}

	public Boolean getBlSeguroMercurio() {
		return blSeguroMercurio;
	}

	public void setBlSeguroMercurio(Boolean blSeguroMercurio) {
		this.blSeguroMercurio = blSeguroMercurio;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public DomainValue getTpVinculoMeioTransporte() {
		return tpVinculoMeioTransporte;
	}

	public void setTpVinculoMeioTransporte(DomainValue tpVinculoMeioTransporte) {
		this.tpVinculoMeioTransporte = tpVinculoMeioTransporte;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public DomainValue getTpGrauRiscoColetaEntrega() {
		return tpGrauRiscoColetaEntrega;
	}

	public void setTpGrauRiscoColetaEntrega(DomainValue tpGrauRiscoColetaEntrega) {
		this.tpGrauRiscoColetaEntrega = tpGrauRiscoColetaEntrega;
	}

	public DomainValue getTpCriterioOrigem() {
		return tpCriterioOrigem;
	}
	
	public void setTpCriterioOrigem(DomainValue tpCriterioOrigem) {
		this.tpCriterioOrigem = tpCriterioOrigem;
	}

	public DomainValue getTpCriterioDestino() {
		return tpCriterioDestino;
	}

	public void setTpCriterioDestino(DomainValue tpCriterioDestino) {
		this.tpCriterioDestino = tpCriterioDestino;
	}
	
	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public ReguladoraSeguro getReguladoraSeguro() {
		return reguladoraSeguro;
	}

	public void setReguladoraSeguro(ReguladoraSeguro reguladoraSeguro) {
		this.reguladoraSeguro = reguladoraSeguro;
	}

	public NaturezaProduto getNaturezaProduto() {
		return naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public void setMunicipios(List<Municipio> municipiosOrigem, List<Municipio> municipiosDestino) {
		setMunicipioEnquadramentosOrigem(municipiosOrigem);
		setMunicipioEnquadramentosDestino(municipiosDestino);
	}

	@ParametrizedAttribute(type = Municipio.class)
	public List<Municipio> getMunicipioEnquadramentosOrigem() {
		return municipioEnquadramentosOrigem;
	}

	public void setMunicipioEnquadramentosOrigem(List<Municipio> municipioEnquadramentosOrigem) {
		this.municipioEnquadramentosOrigem = municipioEnquadramentosOrigem;
	}

	@ParametrizedAttribute(type = Municipio.class)
	public List<Municipio> getMunicipioEnquadramentosDestino() {
		return municipioEnquadramentosDestino;
	}

	public void setMunicipioEnquadramentosDestino(List<Municipio> municipioEnquadramentosDestino) {
		this.municipioEnquadramentosDestino = municipioEnquadramentosDestino;
	}

	public void setPaises(List<Pais> paisesOrigem, List<Pais> paisesDestino) {
		setPaisEnquadramentosOrigem(paisesOrigem);
		setPaisEnquadramentosDestino(paisesDestino);
	}

	@ParametrizedAttribute(type = Pais.class)
	public List<Pais> getPaisEnquadramentosOrigem() {
		return paisEnquadramentosOrigem;
	}

	public void setPaisEnquadramentosOrigem(List<Pais> paisEnquadramentosOrigem) {
		this.paisEnquadramentosOrigem = paisEnquadramentosOrigem;
	}

	@ParametrizedAttribute(type = Pais.class)
	public List<Pais> getPaisEnquadramentosDestino() {
		return paisEnquadramentosDestino;
	}

	public void setPaisEnquadramentosDestino(List<Pais> paisEnquadramentosDestino) {
		this.paisEnquadramentosDestino = paisEnquadramentosDestino;
	}

	public void setUnidadesFederativa(List<UnidadeFederativa> unidadesFederativaOrigem, List<UnidadeFederativa> unidadesFederativaDestino) {
		setUnidadeFederativaEnquadramentosOrigem(unidadesFederativaOrigem);
		setUnidadeFederativaEnquadramentosDestino(unidadesFederativaDestino);
	}

	@ParametrizedAttribute(type = UnidadeFederativa.class)
	public List<UnidadeFederativa> getUnidadeFederativaEnquadramentosOrigem() {
		return unidadeFederativaEnquadramentosOrigem;
	}

	public void setUnidadeFederativaEnquadramentosOrigem(List<UnidadeFederativa> unidadeFederativaEnquadramentosOrigem) {
		this.unidadeFederativaEnquadramentosOrigem = unidadeFederativaEnquadramentosOrigem;
	}

	@ParametrizedAttribute(type = UnidadeFederativa.class)
	public List<UnidadeFederativa> getUnidadeFederativaEnquadramentosDestino() {
		return unidadeFederativaEnquadramentosDestino;
	}

	public void setUnidadeFederativaEnquadramentosDestino(List<UnidadeFederativa> unidadeFederativaEnquadramentosDestino) {
		this.unidadeFederativaEnquadramentosDestino = unidadeFederativaEnquadramentosDestino;
	}

	public void setFiliais(List<Filial> filiaisOrigem, List<Filial> filiaisDestino) {
		setFilialEnquadramentosOrigem(filiaisOrigem);
		setFilialEnquadramentosDestino(filiaisDestino);
	}

	@ParametrizedAttribute(type = Filial.class)
	public List<Filial> getFilialEnquadramentosOrigem() {
		return filialEnquadramentosOrigem;
	}

	public void setFilialEnquadramentosOrigem(List<Filial> filialEnquadramentosOrigem) {
		this.filialEnquadramentosOrigem = filialEnquadramentosOrigem;
	}

	@ParametrizedAttribute(type = Filial.class)
	public List<Filial> getFilialEnquadramentosDestino() {
		return filialEnquadramentosDestino;
	}

	public void setFilialEnquadramentosDestino(List<Filial> filialEnquadramentosDestino) {
		this.filialEnquadramentosDestino = filialEnquadramentosDestino;
	}

	@ParametrizedAttribute(type = ClienteEnquadramento.class)
	public List<ClienteEnquadramento> getClienteEnquadramentos() {
		return clienteEnquadramentos;
	}

	public void setClienteEnquadramentos(List<ClienteEnquadramento> clienteEnquadramentos) {
		this.clienteEnquadramentos = clienteEnquadramentos;
	}

	@ParametrizedAttribute(type = FaixaDeValor.class)
	public List<FaixaDeValor> getFaixaDeValors() {
		return faixaDeValors;
	}

	public void setFaixaDeValors(List<FaixaDeValor> faixaDeValors) {
		this.faixaDeValors = faixaDeValors;
	}

	public Boolean getBlRegraGeral() {
		return blRegraGeral;
	}

	public void setBlRegraGeral(Boolean blRegraGeral) {
		this.blRegraGeral = blRegraGeral;
	}

	public ApoliceSeguro getApoliceSeguro() {
		return apoliceSeguro;
	}

	public void setApoliceSeguro(ApoliceSeguro apoliceSeguro) {
		this.apoliceSeguro = apoliceSeguro;
	}

	public SeguroCliente getSeguroCliente() {
		return seguroCliente;
	}

	public void setSeguroCliente(SeguroCliente seguroCliente) {
		this.seguroCliente = seguroCliente;
	}

	/**
	 * LMS-7285 - Busca valor limite da apólice:
	 * <ul>
	 * <li>se existir relacionamento retorna o valor limite da
	 * {@link ApoliceSeguro} ({@code VL_LIMITE_APOLICE});
	 * <li>se existir relacionamento retorna o valor limite do
	 * {@link SeguroCliente} ({@code VL_LIMITE});
	 * <li>caso contrário retorna {@code null}.
	 * </ul>
	 * 
	 * @return Valor limite da {@link ApoliceSeguro}, do {@link SeguroCliente}
	 *         ou {@code null} se não existir.
	 */
	public BigDecimal getVlLimiteApolice() {
		if (apoliceSeguro != null) {
			return apoliceSeguro.getVlLimiteApolice();
		}
		if (seguroCliente != null) {
			return seguroCliente.getVlLimite();
		}
		return null;
	}

	/**
	 * LMS-7285 - Busca valor limite para {@link ControleCarga} da apólice:
	 * <ul>
	 * <li>se existir relacionamento retorna o valor limite para
	 * {@link ControleCarga} da {@link ApoliceSeguro}
	 * ({@code VL_LIMITE_CONTROLE_CARGA});
	 * <li>se exitir relacionamento retorna o valor limite para
	 * {@link ControleCarga} do {@link SeguroCliente}
	 * ({@code VL_LIMITE_CONTROLE_CARGA});
	 * <li>caso contrário retorna {@code null}.
	 * </ul>
	 * 
	 * @return Valor limite para o {@link ControleCarga} da apólice ou
	 *         {@code null} se não existir.
	 */
	public BigDecimal getVlLimiteControleCarga() {
		if (apoliceSeguro != null) {
			return apoliceSeguro.getVlLimiteControleCarga();
		}
		if (seguroCliente != null) {
			return seguroCliente.getVlLimiteControleCarga();
		}
		return null;
	}

	/**
	 * LMS-7285 - Atualiza valor limite para {@link ControleCarga} da apólice:
	 * <ul>
	 * <li>se existir relacionamento atualiza o valor limite da
	 * {@link ApoliceSeguro} ({@code VL_LIMITE_CONTROLE_CARGA});
	 * <li>se exitir relacionamento atualiza o valor limite do
	 * {@link SeguroCliente} ({@code VL_LIMITE_CONTROLE_CARGA}).
	 * </ul>
	 * 
	 * @param vlLimiteControleCarga
	 *            Valor limite para {@link ControleCarga} da apólice.
	 */
	public void setVlLimiteControleCarga(BigDecimal vlLimiteControleCarga) {
		if (apoliceSeguro != null) {
			apoliceSeguro.setVlLimiteControleCarga(vlLimiteControleCarga);
		}
		if (seguroCliente != null) {
			seguroCliente.setVlLimiteControleCarga(vlLimiteControleCarga);
		}
	}

	/**
	 * LMS-6850 - Busca {@link TipoSeguro} da {@link ApoliceSeguro} ou do
	 * {@link SeguroCliente}.
	 * 
	 * @return {@link TipoSeguro} da apólice relacionada ao
	 *         {@link EnquadramentoRegra}.
	 */
	public TipoSeguro getTipoSeguro() {
		if (apoliceSeguro != null) {
			return apoliceSeguro.getTipoSeguro();
		}
		if (seguroCliente != null) {
			return seguroCliente.getTipoSeguro();
		}
		return null;
	}

	/**
	 * LMS-6850 - Busca {@link Seguradora} da {@link ApoliceSeguro} ou do
	 * {@link SeguroCliente}.
	 * 
	 * @return {@link Seguradora} da apólice relacionada ao
	 *         {@link EnquadramentoRegra}.
	 */
	public Seguradora getSeguradora() {
		if (apoliceSeguro != null) {
			return apoliceSeguro.getSeguradora();
		}
		if (seguroCliente != null) {
			return seguroCliente.getSeguradora();
		}
		return null;
	}

	/**
	 * LMS-6850 - Busca número da {@link ApoliceSeguro} ou descrição do
	 * {@link SeguroCliente}.
	 * 
	 * @return Número ou descrição da apólice relacionada ao
	 *         {@link EnquadramentoRegra}.
	 */
	public String getNrApolice() {
		if (apoliceSeguro != null) {
			return apoliceSeguro.getNrApolice();
		}
		if (seguroCliente != null) {
			return seguroCliente.getDsApolice();
		}
		return null;
	}

	/**
	 * LMS-6850 - Compara as referências de {@link ApoliceSeguro} e
	 * {@link SeguroCliente} com outra instância de {@link EnquadramentoRegra}.
	 * Após verificar se a outra instância é diferente de {@code null} são
	 * comparados os atributos {@link TipoSeguro}, {@link Seguradora} e
	 * número/descrição da {@link ApoliceSeguro} ou do {@link SeguroCliente}.
	 * 
	 * @param regra
	 *            Instância de {@link EnquadramentoRegra} para comparação das
	 *            referências de {@link ApoliceSeguro} e {@link SeguroCliente}.
	 * @return {@code true} as apólices forem consideradas iguais na comparação
	 *         de seus atributos, {@code false} caso contrário.
	 */
	public boolean equalsApolice(EnquadramentoRegra regra) {
		return regra != null && new EqualsBuilder()
				.append(getTipoSeguro(), regra.getTipoSeguro())
				.append(getSeguradora(), regra.getSeguradora())
				.append(getNrApolice(), regra.getNrApolice())
				.isEquals();
	}

	public String toString() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		return new ToStringBuilder(this)
				.append(idEnquadramentoRegra)
				.append(dsEnquadramentoRegra != null ? dsEnquadramentoRegra.getValue() : null)
				.append(tpOperacao != null ? tpOperacao.getValue() : null)
				.append(blRegraGeral)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof EnquadramentoRegra)) {
			return false;
		}
		EnquadramentoRegra cast = (EnquadramentoRegra) other;
		return new EqualsBuilder()
				.append(idEnquadramentoRegra, cast.idEnquadramentoRegra)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idEnquadramentoRegra)
				.toHashCode();
	}

}
