package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.Zona;

/** @author LMS Custom Hibernate CodeGenerator */
public class RotaPreco implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_ROTA_PRECO_BY_ID = "com.mercurio.lms.tabelaprecos.model.RotaPreco.findRotaById";

	/** identifier field */
	private Long idRotaPreco;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfDestino;

	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfOrigem;

	/** persistent field */
	private Municipio municipioByIdMunicipioOrigem;

	/** persistent field */
	private Municipio municipioByIdMunicipioDestino;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoOrigem;

	/** persistent field */
	private Aeroporto aeroportoByIdAeroportoDestino;

	/** persistent field */
	private Zona zonaByIdZonaDestino;

	/** persistent field */
	private Zona zonaByIdZonaOrigem;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioComercialDestino;

	/** persistent field */
	private TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioComercialOrigem;

	/** persistent field */
	private Pais paisByIdPaisOrigem;

	/** persistent field */
	private Pais paisByIdPaisDestino;

	/** persistent field */
	private Filial filialByIdFilialDestino;

	/** persistent field */
	private Filial filialByIdFilialOrigem;

	/** persistent field */
	private GrupoRegiao grupoRegiaoOrigem;
	
	/** persistent field */
	private GrupoRegiao grupoRegiaoDestino;
	
	private Integer indexRotaOrigem;
	
	private Integer indexRotaDestino;	
	
	public RotaPreco() {}
	
	public RotaPreco(Long idRota) {
		this.idRotaPreco = idRota;
	}

	public Long getIdRotaPreco() {
		return this.idRotaPreco;
	}

	public void setIdRotaPreco(Long idRotaPreco) {
		this.idRotaPreco = idRotaPreco;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfDestino() {
		return this.unidadeFederativaByIdUfDestino;
	}

	public void setUnidadeFederativaByIdUfDestino(
			UnidadeFederativa unidadeFederativaByIdUfDestino) {
		this.unidadeFederativaByIdUfDestino = unidadeFederativaByIdUfDestino;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfOrigem() {
		return this.unidadeFederativaByIdUfOrigem;
	}

	public void setUnidadeFederativaByIdUfOrigem(
			UnidadeFederativa unidadeFederativaByIdUfOrigem) {
		this.unidadeFederativaByIdUfOrigem = unidadeFederativaByIdUfOrigem;
	}

	public Municipio getMunicipioByIdMunicipioOrigem() {
		return this.municipioByIdMunicipioOrigem;
	}

	public void setMunicipioByIdMunicipioOrigem(
			Municipio municipioByIdMunicipioOrigem) {
		this.municipioByIdMunicipioOrigem = municipioByIdMunicipioOrigem;
	}

	public Municipio getMunicipioByIdMunicipioDestino() {
		return this.municipioByIdMunicipioDestino;
	}

	public void setMunicipioByIdMunicipioDestino(
			Municipio municipioByIdMunicipioDestino) {
		this.municipioByIdMunicipioDestino = municipioByIdMunicipioDestino;
	}

	public Aeroporto getAeroportoByIdAeroportoOrigem() {
		return this.aeroportoByIdAeroportoOrigem;
	}

	public void setAeroportoByIdAeroportoOrigem(
			Aeroporto aeroportoByIdAeroportoOrigem) {
		this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
	}

	public Aeroporto getAeroportoByIdAeroportoDestino() {
		return this.aeroportoByIdAeroportoDestino;
	}

	public void setAeroportoByIdAeroportoDestino(
			Aeroporto aeroportoByIdAeroportoDestino) {
		this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
	}

	public Zona getZonaByIdZonaDestino() {
		return this.zonaByIdZonaDestino;
	}

	public void setZonaByIdZonaDestino(Zona zonaByIdZonaDestino) {
		this.zonaByIdZonaDestino = zonaByIdZonaDestino;
	}

	public Zona getZonaByIdZonaOrigem() {
		return this.zonaByIdZonaOrigem;
	}

	public void setZonaByIdZonaOrigem(Zona zonaByIdZonaOrigem) {
		this.zonaByIdZonaOrigem = zonaByIdZonaOrigem;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() {
		return this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
	}

	public void setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino) {
		this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino = tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() {
		return this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;
	}

	public void setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem) {
		this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem = tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioComercialDestino() {
		return tipoLocalizacaoMunicipioComercialDestino;
	}

	public void setTipoLocalizacaoMunicipioComercialDestino(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioComercialDestino) {
		this.tipoLocalizacaoMunicipioComercialDestino = tipoLocalizacaoMunicipioComercialDestino;
	}

	public TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioComercialOrigem() {
		return tipoLocalizacaoMunicipioComercialOrigem;
	}

	public void setTipoLocalizacaoMunicipioComercialOrigem(
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioComercialOrigem) {
		this.tipoLocalizacaoMunicipioComercialOrigem = tipoLocalizacaoMunicipioComercialOrigem;
	}

	public Pais getPaisByIdPaisOrigem() {
		return this.paisByIdPaisOrigem;
	}

	public void setPaisByIdPaisOrigem(Pais paisByIdPaisOrigem) {
		this.paisByIdPaisOrigem = paisByIdPaisOrigem;
	}

	public Pais getPaisByIdPaisDestino() {
		return this.paisByIdPaisDestino;
	}

	public void setPaisByIdPaisDestino(Pais paisByIdPaisDestino) {
		this.paisByIdPaisDestino = paisByIdPaisDestino;
	}

	public Filial getFilialByIdFilialDestino() {
		return this.filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public Filial getFilialByIdFilialOrigem() {
		return this.filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("idRotaPreco", getIdRotaPreco()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RotaPreco))
			return false;
		RotaPreco castOther = (RotaPreco) other;
		return new EqualsBuilder().append(this.getIdRotaPreco(),
				castOther.getIdRotaPreco()).isEquals();
	}

	public boolean equals(RotaPreco rp){
		return rp != null && this.getIdRotaPreco().equals(rp.getIdRotaPreco());
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdRotaPreco()).toHashCode();
	}

	public String getDestinoString() {
		StringBuffer sb = new StringBuffer();

		if(Hibernate.isInitialized(getZonaByIdZonaDestino())
				&& getZonaByIdZonaDestino() != null
				&& getZonaByIdZonaDestino().getDsZona() != null
				&& StringUtils.isNotBlank(getZonaByIdZonaDestino().getDsZona()
						.getValue())) {
			sb.append(getZonaByIdZonaDestino().getDsZona()).append("-");
		}

		if(Hibernate.isInitialized(getPaisByIdPaisDestino())
				&& getPaisByIdPaisDestino() != null
				&& getPaisByIdPaisDestino().getNmPais() != null
				&& StringUtils.isNotBlank(getPaisByIdPaisDestino().getNmPais()
						.getValue())) {
			sb.append(getPaisByIdPaisDestino().getNmPais()).append("-");
		}

		if(Hibernate.isInitialized(getUnidadeFederativaByIdUfDestino())
				&& getUnidadeFederativaByIdUfDestino() != null
				&& StringUtils.isNotBlank(getUnidadeFederativaByIdUfDestino()
						.getSgUnidadeFederativa())) {
			sb.append(
					getUnidadeFederativaByIdUfDestino()
							.getSgUnidadeFederativa()).append("-");
		}

		if(Hibernate.isInitialized(getFilialByIdFilialDestino())
				&& getFilialByIdFilialDestino() != null
				&& StringUtils.isNotBlank(getFilialByIdFilialDestino()
						.getSgFilial())) {
			sb.append(getFilialByIdFilialDestino().getSgFilial()).append("-");
		}

		if(Hibernate.isInitialized(getMunicipioByIdMunicipioDestino())
				&& getMunicipioByIdMunicipioDestino() != null
				&& StringUtils.isNotBlank(getMunicipioByIdMunicipioDestino()
						.getNmMunicipio())) {
			sb.append(getMunicipioByIdMunicipioDestino().getNmMunicipio())
					.append("-");
		}
		if(Hibernate.isInitialized(getAeroportoByIdAeroportoDestino())
				&& getAeroportoByIdAeroportoDestino() != null
				&& StringUtils.isNotBlank(getAeroportoByIdAeroportoDestino()
						.getSgAeroporto())) {
			sb.append(getAeroportoByIdAeroportoDestino().getSgAeroporto())
					.append("-");
		}

		if (Hibernate
				.isInitialized(getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino())
				&& getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() != null
				&& getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino()
						.getDsTipoLocalizacaoMunicipio() != null
				&& StringUtils
						.isNotBlank(getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino()
								.getDsTipoLocalizacaoMunicipio().getValue())) {
			sb.append(
					getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino()
							.getDsTipoLocalizacaoMunicipio()).append("-");
		}
		
		if (Hibernate
				.isInitialized(getTipoLocalizacaoMunicipioComercialDestino())
				&& getTipoLocalizacaoMunicipioComercialDestino() != null
				&& getTipoLocalizacaoMunicipioComercialDestino()
						.getDsTipoLocalizacaoMunicipio() != null
				&& StringUtils
						.isNotBlank(getTipoLocalizacaoMunicipioComercialDestino()
								.getDsTipoLocalizacaoMunicipio().getValue())) {
			sb.append(
					getTipoLocalizacaoMunicipioComercialDestino()
							.getDsTipoLocalizacaoMunicipio()).append("-");
		}

		if(Hibernate.isInitialized(getGrupoRegiaoDestino()) 
				&& getGrupoRegiaoDestino() != null 
				&& getGrupoRegiaoDestino().getDsGrupoRegiao() != null){
			
			sb.append(getGrupoRegiaoDestino().getDsGrupoRegiao());
		}				
		
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	public String getOrigemString() {
		StringBuffer sb = new StringBuffer();

		if(Hibernate.isInitialized(getZonaByIdZonaOrigem())
				&& getZonaByIdZonaOrigem() != null
				&& getZonaByIdZonaOrigem().getDsZona() != null
				&& StringUtils.isNotBlank(getZonaByIdZonaOrigem().getDsZona()
						.getValue())) {
			sb.append(getZonaByIdZonaOrigem().getDsZona()).append("-");
		}

		if(Hibernate.isInitialized(getPaisByIdPaisOrigem())
				&& getPaisByIdPaisOrigem() != null
				&& getPaisByIdPaisOrigem().getNmPais() != null
				&& StringUtils.isNotBlank(getPaisByIdPaisOrigem().getNmPais()
						.getValue())) {
			sb.append(getPaisByIdPaisOrigem().getNmPais()).append("-");
		}

		if(Hibernate.isInitialized(getUnidadeFederativaByIdUfOrigem())
				&& getUnidadeFederativaByIdUfOrigem() != null
				&& StringUtils.isNotBlank(getUnidadeFederativaByIdUfOrigem()
						.getSgUnidadeFederativa())) {
			sb.append(
					getUnidadeFederativaByIdUfOrigem().getSgUnidadeFederativa())
					.append("-");
		}

		if(Hibernate.isInitialized(getFilialByIdFilialOrigem())
				&& getFilialByIdFilialOrigem() != null
				&& StringUtils.isNotBlank(getFilialByIdFilialOrigem()
						.getSgFilial())) {
			sb.append(getFilialByIdFilialOrigem().getSgFilial()).append("-");
		}

		if(Hibernate.isInitialized(getMunicipioByIdMunicipioOrigem())
				&& getMunicipioByIdMunicipioOrigem() != null
				&& StringUtils.isNotBlank(getMunicipioByIdMunicipioOrigem()
						.getNmMunicipio())) {
			sb.append(getMunicipioByIdMunicipioOrigem().getNmMunicipio())
					.append("-");
		}

		if(Hibernate.isInitialized(getAeroportoByIdAeroportoOrigem())
				&& getAeroportoByIdAeroportoOrigem() != null
				&& StringUtils.isNotBlank(getAeroportoByIdAeroportoOrigem()
						.getSgAeroporto())) {
			sb.append(getAeroportoByIdAeroportoOrigem().getSgAeroporto())
					.append("-");
		}

		if (Hibernate
				.isInitialized(getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem())
				&& getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() != null
				&& getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem()
						.getDsTipoLocalizacaoMunicipio() != null
				&& StringUtils
						.isNotBlank(getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem()
								.getDsTipoLocalizacaoMunicipio().getValue())) {
			sb.append(
					getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem()
							.getDsTipoLocalizacaoMunicipio()).append("-");
		}
		
		if (Hibernate
				.isInitialized(getTipoLocalizacaoMunicipioComercialOrigem())
				&& getTipoLocalizacaoMunicipioComercialOrigem() != null
				&& getTipoLocalizacaoMunicipioComercialOrigem()
						.getDsTipoLocalizacaoMunicipio() != null
				&& StringUtils
						.isNotBlank(getTipoLocalizacaoMunicipioComercialOrigem()
								.getDsTipoLocalizacaoMunicipio().getValue())) {
			sb.append(
					getTipoLocalizacaoMunicipioComercialOrigem()
							.getDsTipoLocalizacaoMunicipio()).append("-");
		}
		
		if(Hibernate.isInitialized(getGrupoRegiaoOrigem()) 
				&& getGrupoRegiaoOrigem() != null 
				&& getGrupoRegiaoOrigem().getDsGrupoRegiao() != null){
			
			sb.append(getGrupoRegiaoOrigem().getDsGrupoRegiao());
		}
		
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	public GrupoRegiao getGrupoRegiaoOrigem() {
		return grupoRegiaoOrigem;
}

	public void setGrupoRegiaoOrigem(GrupoRegiao grupoRegiaoOrigem) {
		this.grupoRegiaoOrigem = grupoRegiaoOrigem;
	}

	public GrupoRegiao getGrupoRegiaoDestino() {
		return grupoRegiaoDestino;
	}

	public void setGrupoRegiaoDestino(GrupoRegiao grupoRegiaoDestino) {
		this.grupoRegiaoDestino = grupoRegiaoDestino;
	}

	public Integer getIndexRotaOrigem() {
  		return indexRotaOrigem;
	}

	public void setIndexRotaOrigem(Integer indexRotaOrigem) {
		this.indexRotaOrigem = indexRotaOrigem;
	}

	public Integer getIndexRotaDestino() {
		return indexRotaDestino;
	}

	public void setIndexRotaDestino(Integer indexRotaDestino) {
		this.indexRotaDestino = indexRotaDestino;
	}
}
