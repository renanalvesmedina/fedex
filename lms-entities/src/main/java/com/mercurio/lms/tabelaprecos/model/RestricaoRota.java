package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.Zona;

public class RestricaoRota implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idZona;
	private Long idPais;
	private Long idUnidadeFederativa;
	private Long idFilial;
	private Long idMunicipio;
	private Long idTipoLocalizacao;
	private Long idTipoLocalizacaoTarifa;
	private Long idTipoLocalizacaoComercial;
	private Long idAeroporto;

	private String  sbTabelaPreco;	
	private Boolean isLocalComercialCapital;
	private Long idGrupoRegiao;
		
	private Boolean verificarLocalizacaoRota;
	
	private List<TipoLocalizacaoMunicipio> listLocalOperacional;
	
	private String dsTipoLocalizacao;
	
	public Long getIdAeroporto() {
		return idAeroporto;
	}

	public void setIdAeroporto(Long idAeroporto) {
		this.idAeroporto = idAeroporto;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public Long getIdPais() {
		return idPais;
	}

	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}

	public Long getIdTipoLocalizacao() {
		return idTipoLocalizacao;
	}

	public void setIdTipoLocalizacao(Long idTipoLocalizacao) {
		this.idTipoLocalizacao = idTipoLocalizacao;
	}

	public Long getIdUnidadeFederativa() {
		return idUnidadeFederativa;
	}

	public void setIdUnidadeFederativa(Long idUnidadeFederativa) {
		this.idUnidadeFederativa = idUnidadeFederativa;
	}

	public Long getIdZona() {
		return idZona;
	}

	public void setIdZona(Long idZona) {
		this.idZona = idZona;
	}

	public Long getIdTipoLocalizacaoComercial() {
		return idTipoLocalizacaoComercial;
}

	public void setIdTipoLocalizacaoComercial(Long idTipoLocalizacaoComercial) {
		this.idTipoLocalizacaoComercial = idTipoLocalizacaoComercial;
	}

	public String getSbTabelaPreco() {
		return sbTabelaPreco;
	}

	public void setSbTabelaPreco(String sbTabelaPreco) {
		this.sbTabelaPreco = sbTabelaPreco;
	}

	public Boolean getIsLocalComercialCapital() {
		return isLocalComercialCapital;
	}

	public void setIsLocalComercialCapital(Boolean isLocalComercialCapital) {
		this.isLocalComercialCapital = isLocalComercialCapital;
	}

	public Long getIdGrupoRegiao() {
		return idGrupoRegiao;
}

	public void setIdGrupoRegiao(Long idGrupoRegiao) {
		this.idGrupoRegiao = idGrupoRegiao;
	}

	public Long getIdTipoLocalizacaoTarifa() {
		return idTipoLocalizacaoTarifa;
}

	public void setIdTipoLocalizacaoTarifa(Long idTipoLocalizacaoTarifa) {
		this.idTipoLocalizacaoTarifa = idTipoLocalizacaoTarifa;
	}
	
	public enum TIPOROTA {
		MUNICIPIO(1), GRUPOREGIAO(2), FILIAL(3), TIPOLOCALIZACAO(4), AEROPORTO(
				5), UNIDADEFEDERATIVA(6), PAIS(7), ZONA(8);
		
		private Integer index;
		
		TIPOROTA(Integer index){
			this.index = index;
}
		
		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

	};
	
	public static Integer indexRota(Zona zona, Pais pais, Filial filial,
			UnidadeFederativa uf, Municipio municipio, GrupoRegiao grupo,
			TipoLocalizacaoMunicipio tipo, Aeroporto aeroporto) {

		Integer indexRota = null;

		Boolean existZona   	= zona 		!= null && zona.getIdZona() 					!= null;
		Boolean existPais   	= pais 		!= null && pais.getIdPais() 					!= null;
		Boolean existUF     	= uf   		!= null && uf.getIdUnidadeFederativa() 			!= null;
		Boolean existFilial 	= filial 	!= null && filial.getIdFilial() 				!= null;
		Boolean existMunicipio = municipio != null
				&& municipio.getIdMunicipio() != null;
		Boolean existGrupo 		= grupo 	!= null && grupo.getIdGrupoRegiao() 			!= null;
		Boolean existTipo = tipo != null
				&& tipo.getIdTipoLocalizacaoMunicipio() != null;
		Boolean existAeroporto = aeroporto != null
				&& aeroporto.getIdAeroporto() != null;

		if(existZona && existPais && existUF && existFilial && existMunicipio){
			indexRota = TIPOROTA.MUNICIPIO.getIndex();
		}else if(existZona && existPais && existUF && existGrupo){
			indexRota = TIPOROTA.GRUPOREGIAO.getIndex();
		}else if(existZona && existPais && existUF && existFilial){
			indexRota = TIPOROTA.FILIAL.getIndex();
		}else if(existZona && existPais && existUF && existTipo){
			indexRota = TIPOROTA.TIPOLOCALIZACAO.getIndex();
		}else if(existZona && existPais && existUF && existAeroporto){
			indexRota = TIPOROTA.AEROPORTO.getIndex();
		}else if(existZona && existPais && existUF){
			indexRota = TIPOROTA.UNIDADEFEDERATIVA.getIndex();
		}else if(existZona && existPais){
			indexRota = TIPOROTA.PAIS.getIndex();
		}else if(existZona){
			indexRota = TIPOROTA.ZONA.getIndex();
		}else{
			indexRota = 0;
		}

		return indexRota;
	}

	public List<TipoLocalizacaoMunicipio> getListLocalOperacional() {
		return listLocalOperacional;
	}
	
	public void setListLocalOperacional(
			List<TipoLocalizacaoMunicipio> listLocalOperacional) {
		this.listLocalOperacional = listLocalOperacional;
	}
	
	public String getDsTipoLocalizacao() {
		
		if (getIdTipoLocalizacao() != null && listLocalOperacional != null
				&& !listLocalOperacional.isEmpty()) {
			for (TipoLocalizacaoMunicipio tlo : listLocalOperacional) {
				if (getIdTipoLocalizacao().equals(
						tlo.getIdTipoLocalizacaoMunicipio())) {
					return tlo.getDsTipoLocalizacaoMunicipio().getValue();
				}
			}
		}
		
		return dsTipoLocalizacao;
	}
	
	public void setDsTipoLocalizacao(String dsTipoLocalizacao) {
		this.dsTipoLocalizacao = dsTipoLocalizacao;
	}

	public Boolean getVerificarLocalizacaoRota() {
		if(verificarLocalizacaoRota == null){
			verificarLocalizacaoRota = Boolean.FALSE;
		}
		return verificarLocalizacaoRota;
	}

	public void setVerificarLocalizacaoRota(Boolean verificarLocalizacaoRota) {
		this.verificarLocalizacaoRota = verificarLocalizacaoRota;
	}

	public Long getIdByDsTipoLocalizacao(String dsTipoLocalizacao){
		
		if (getIdTipoLocalizacao() != null && listLocalOperacional != null
				&& !listLocalOperacional.isEmpty()) {
			for (TipoLocalizacaoMunicipio tlo : listLocalOperacional) {
				if (tlo.getDsTipoLocalizacaoMunicipio().getValue()
						.equals(dsTipoLocalizacao)) {
					return tlo.getIdTipoLocalizacaoMunicipio(); 				
				}
			}
		}
		return null;
	}
}
