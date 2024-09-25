package com.mercurio.lms.municipios.model.param;

import org.joda.time.YearMonthDay;

public class MCDParam {

	/**
	 * Quando alterado algum dos indices abaixo deve ser verificado as seguintes
	 * classes, as duas devem refletir as mesmas posições.
	 * 
	 * com.mercurio.lms.municipios.model.param.MCDParam
	 * com.mercurio.lms.municipios.model.dao.ConsultarMCDDAO
	 */
	public static final int BL_DISTRITO_DESTINO = 0;
	public static final int BL_DISTRITO_ORIGEM = 1;
	public static final int BL_DOMINGO_DESTINO = 2;
	public static final int BL_DOMINGO_ORIGEM = 3;
	public static final int BL_QUARTA_DESTINO = 4;
	public static final int BL_QUARTA_ORIGEM = 5;
	public static final int BL_QUINTA_DESTINO = 6;
	public static final int BL_QUINTA_ORIGEM = 7;
	public static final int BL_SABADO_DESTINO = 8;
	public static final int BL_SABADO_ORIGEM = 9;
	public static final int BL_SEGUNDA_DESTINO = 10;
	public static final int BL_SEGUNDA_ORIGEM = 11;
	public static final int BL_SEXTA_DESTINO = 12;
	public static final int BL_SEXTA_ORIGEM = 13;
	public static final int BL_TERCA_DESTINO = 14;
	public static final int BL_TERCA_ORIGEM = 15;
	public static final int CD_IBGE_DESTINO = 16;
	public static final int CD_IBGE_ORIGEM = 17;
	public static final int CD_TARIFA_PRECO = 18;
	public static final int CD_TARIFA_PRECO_ATUAL = 19;
	public static final int DISTANCIA_TOTAL = 20;
	public static final int DS_SERVICO = 21;
	public static final int DT_VIGENCIA_FINAL = 22;
	public static final int DT_VIGENCIA_INICIAL = 23;
	public static final int ID_FILIAL_DESTINO = 24;
	public static final int ID_FILIAL_ORIGEM = 25;
	public static final int ID_FILIAL_REEMBARCADORA = 26;
	public static final int ID_MUNICIPIO_DESTINO = 27;
	public static final int ID_MUNICIPIO_FILIAL_DESTINO = 28;
	public static final int ID_MUNICIPIO_FILIAL_ORIGEM = 29;
	public static final int ID_MUNICIPIO_ORIGEM = 30;
	public static final int ID_PAIS_DESTINO = 31;
	public static final int ID_PAIS_ORIGEM = 32;
	public static final int ID_SERVICO = 33;
	public static final int ID_TIPO_LOCALIZACAO_DESTINO = 34;
	public static final int ID_TIPO_LOCALIZACAO_ORIGEM = 35;
	public static final int ID_UNIDADE_FEDERATIVA_DESTINO = 36;
	public static final int ID_UNIDADE_FEDERATIVA_ORIGEM = 37;
	public static final int ID_ZONA_DESTINO = 38;
	public static final int ID_ZONA_ORIGEM = 39;
	public static final int NM_FILIAL_DESTINO = 40;
	public static final int NM_FILIAL_ORIGEM = 41;
	public static final int NM_FILIAL_REEMBARCADORA = 42;
	public static final int NM_MUNICIPIO_DESTINO = 43;
	public static final int NM_MUNICIPIO_ORIGEM = 44;
	public static final int NM_PAIS_DESTINO = 45;
	public static final int NM_PAIS_ORIGEM = 46;
	public static final int NM_UNIDADE_FEDERATIVA_DESTINO = 47;
	public static final int NM_UNIDADE_FEDERATIVA_ORIGEM = 48;
	public static final int NR_DIAS_ATENDIDOS_DESTINO = 49;
	public static final int NR_DIAS_ATENDIDOS_ORIGEM = 50;
	public static final int NR_PPE = 51;
	public static final int NR_PRAZO_TOTAL = 52;
	public static final int NR_TEMPO_COLETA = 53;
	public static final int NR_TEMPO_ENTREGA_DESTINO = 54;
	public static final int NR_TEMPO_ENTREGA_ORIGEM = 55;
	public static final int QT_PEDAGIO = 56;
	public static final int SG_FILIAL_DESTINO = 57;
	public static final int SG_FILIAL_ORIGEM = 58;
	public static final int SG_REGIONAL_DESTINO = 59;
	public static final int SG_REGIONAL_ORIGEM = 60;
	public static final int SG_FILIAL_REEMBARCADORA = 61;
	public static final int SG_UNIDADE_FEDERATIVA_DESTINO = 62;


	private Long idMunicipioOrigem;
	private Long idFilialOrigem;
	private Long idUnidadeFederativaOrigem;

	private Long idFilialDestino;
	private Long idMunicipioDestino;
	private Long idUnidadeFederativaDestino;

	private Long idServico;
	private String tpEmissao;
	private YearMonthDay dtVigencia;
	private Long idCliente;

	private Long idRegionalOrigem;
	private Long idRegionalDestino;

	public Long getIdRegionalOrigem() {
		return idRegionalOrigem;
	}

	public void setIdRegionalOrigem(Long idRegionalOrigem) {
		this.idRegionalOrigem = idRegionalOrigem;
	}

	public Long getIdRegionalDestino() {
		return idRegionalDestino;
	}

	public void setIdRegionalDestino(Long idRegionalDestino) {
		this.idRegionalDestino = idRegionalDestino;
	}

	public YearMonthDay getDtVigencia() {
		return dtVigencia;
	}

	public void setDtVigencia(YearMonthDay dtVigencia) {
		this.dtVigencia = dtVigencia;
	}

	public Long getIdFilialDestino() {
		return idFilialDestino;
	}

	public void setIdFilialDestino(Long idFilialDestino) {
		this.idFilialDestino = idFilialDestino;
	}

	public Long getIdFilialOrigem() {
		return idFilialOrigem;
	}

	public void setIdFilialOrigem(Long idFilialOrigem) {
		this.idFilialOrigem = idFilialOrigem;
	}

	public Long getIdMunicipioDestino() {
		return idMunicipioDestino;
	}

	public void setIdMunicipioDestino(Long idMunicipioDestino) {
		this.idMunicipioDestino = idMunicipioDestino;
	}

	public Long getIdMunicipioOrigem() {
		return idMunicipioOrigem;
	}

	public void setIdMunicipioOrigem(Long idMunicipioOrigem) {
		this.idMunicipioOrigem = idMunicipioOrigem;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public Long getIdUnidadeFederativaDestino() {
		return idUnidadeFederativaDestino;
	}

	public void setIdUnidadeFederativaDestino(Long idUnidadeFederativaDestino) {
		this.idUnidadeFederativaDestino = idUnidadeFederativaDestino;
	}

	public Long getIdUnidadeFederativaOrigem() {
		return idUnidadeFederativaOrigem;
	}

	public void setIdUnidadeFederativaOrigem(Long idUnidadeFederativaOrigem) {
		this.idUnidadeFederativaOrigem = idUnidadeFederativaOrigem;
	}

	public String getTpEmissao() {
		return tpEmissao;
	}

	public void setTpEmissao(String tpEmissao) {
		this.tpEmissao = tpEmissao;
	}

	public Long getIdCliente() {
		return idCliente;
}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

}
