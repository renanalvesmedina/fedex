package com.mercurio.lms.sgr.model;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;

public class TipoExigenciaGerRiscoBuilder {

	public static TipoExigenciaGerRiscoBuilder newTipoExigenciaGerRisco() {
		return new TipoExigenciaGerRiscoBuilder();
	}

	private TipoExigenciaGerRisco tipo;

	private TipoExigenciaGerRiscoBuilder() {
		tipo = new TipoExigenciaGerRisco();
		tipo.setTpSituacao(new DomainValue(ConstantesGerRisco.TP_SITUACAO_ATIVO));
		tipo.setBlRestrito(false);
		tipo.setBlExigeQuantidade(false);
		tipo.setBlControleNivel(false);
		tipo.setBlTravaSistema(false);
	}

	public TipoExigenciaGerRiscoBuilder id(long id) {
		tipo.setIdTipoExigenciaGerRisco(id);
		return this;
	}

	public TipoExigenciaGerRiscoBuilder descricao(String descricao) {
		tipo.setDsTipoExigenciaGerRisco(new VarcharI18n(descricao));
		return this;
	}

	public TipoExigenciaGerRiscoBuilder inativo() {
		tipo.setTpSituacao(new DomainValue(ConstantesGerRisco.TP_SITUACAO_INATIVO));
		return this;
	}

	public TipoExigenciaGerRiscoBuilder escolta() {
		tipo.setTpExigencia(new DomainValue(ConstantesGerRisco.TP_EXIGENCIA_ESCOLTA));
		return this;
	}

	public TipoExigenciaGerRiscoBuilder monitoramento() {
		tipo.setTpExigencia(new DomainValue(ConstantesGerRisco.TP_EXIGENCIA_MONITORAMENTO));
		return this;
	}

	public TipoExigenciaGerRiscoBuilder motorista() {
		tipo.setTpExigencia(new DomainValue(ConstantesGerRisco.TP_EXIGENCIA_MOTORISTA));
		return this;
	}

	public TipoExigenciaGerRiscoBuilder veiculo() {
		tipo.setTpExigencia(new DomainValue(ConstantesGerRisco.TP_EXIGENCIA_VEICULO));
		return this;
	}

	public TipoExigenciaGerRiscoBuilder averbacao() {
		tipo.setTpExigencia(new DomainValue(ConstantesGerRisco.TP_EXIGENCIA_AVERBACAO));
		return this;
	}

	public TipoExigenciaGerRiscoBuilder virus() {
		tipo.setTpExigencia(new DomainValue(ConstantesGerRisco.TP_EXIGENCIA_VIRUS));
		return this;
	}

	public TipoExigenciaGerRiscoBuilder restrito() {
		tipo.setBlRestrito(true);
		return this;
	}

	public TipoExigenciaGerRiscoBuilder exigeQuantidade() {
		tipo.setBlExigeQuantidade(true);
		return this;
	}

	public TipoExigenciaGerRiscoBuilder controleNivel() {
		tipo.setBlControleNivel(true);
		return this;
	}

	public TipoExigenciaGerRiscoBuilder travaSistema() {
		tipo.setBlTravaSistema(true);
		return this;
	}

	public TipoExigenciaGerRisco build() {
		return tipo;
	}

	public static TipoExigenciaGerRisco ESCOLTA() {
		return newTipoExigenciaGerRisco()
				.id(1)
				.escolta()
				.exigeQuantidade()
				.controleNivel()
				.travaSistema()
				.build();
	}

	public static TipoExigenciaGerRisco MONITORAMENTO() {
		return newTipoExigenciaGerRisco()
				.id(2)
				.monitoramento()
				.controleNivel()
				.travaSistema()
				.build();
	}

	public static TipoExigenciaGerRisco MOTORISTA() {
		return newTipoExigenciaGerRisco()
				.id(3)
				.motorista()
				.controleNivel()
				.travaSistema()
				.build();
	}

	public static TipoExigenciaGerRisco VEICULO() {
		return newTipoExigenciaGerRisco()
				.id(4)
				.veiculo()
				.controleNivel()
				.travaSistema()
				.build();
	}

	public static TipoExigenciaGerRisco AVERBACAO() {
		return newTipoExigenciaGerRisco()
				.id(5)
				.averbacao()
				.travaSistema()
				.build();
	}

	public static TipoExigenciaGerRisco VIRUS() {
		return newTipoExigenciaGerRisco()
				.id(6)
				.virus()
				.restrito()
				.exigeQuantidade()
				.travaSistema()
				.build();
	}

}
