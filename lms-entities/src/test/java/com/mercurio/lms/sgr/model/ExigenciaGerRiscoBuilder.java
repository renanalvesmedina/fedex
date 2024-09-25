package com.mercurio.lms.sgr.model;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;

public class ExigenciaGerRiscoBuilder {

	public static ExigenciaGerRiscoBuilder newExigenciaGerRisco() {
		return new ExigenciaGerRiscoBuilder();
	}

	private ExigenciaGerRisco exigencia;

	private ExigenciaGerRiscoBuilder() {
		exigencia = new ExigenciaGerRisco();
		exigencia.setNrNivel(1L);
		exigencia.setTpSituacao(new DomainValue(ConstantesGerRisco.TP_SITUACAO_ATIVO));
		exigencia.setTpCriterioAgrupamento(new DomainValue(ConstantesGerRisco.TP_AGRUPAMENTO_ACUMULADO));
		exigencia.setBlAreaRisco(false);
	}

	public ExigenciaGerRiscoBuilder id(long id) {
		exigencia.setIdExigenciaGerRisco(id);
		return this;
	}

	public ExigenciaGerRiscoBuilder nivel(long nivel) {
		exigencia.setNrNivel(nivel);
		return this;
	}

	public ExigenciaGerRiscoBuilder inativo() {
		exigencia.setTpSituacao(new DomainValue(ConstantesGerRisco.TP_SITUACAO_INATIVO));
		return this;
	}

	public ExigenciaGerRiscoBuilder descricao(String resumida) {
		return descricao(resumida, null);
	}
	
	public ExigenciaGerRiscoBuilder descricao(String resumida, String completa) {
		exigencia.setDsResumida(new VarcharI18n(resumida));
		exigencia.setDsCompleta(new VarcharI18n(completa));
		return this;
	}

	public ExigenciaGerRiscoBuilder tipo(TipoExigenciaGerRisco tipo) {
		exigencia.setTipoExigenciaGerRisco(tipo);
		return this;
	}

	public ExigenciaGerRiscoBuilder acumulado() {
		exigencia.setTpCriterioAgrupamento(new DomainValue(ConstantesGerRisco.TP_AGRUPAMENTO_ACUMULADO));
		return this;
	}

	public ExigenciaGerRiscoBuilder maiorQuantidade() {
		exigencia.setTpCriterioAgrupamento(new DomainValue(ConstantesGerRisco.TP_AGRUPAMENTO_MAIOR_QUANTIDADE));
		return this;
	}

	public ExigenciaGerRiscoBuilder areaRisco() {
		exigencia.setBlAreaRisco(true);
		return this;
	}

	public ExigenciaGerRiscoBuilder identificador(String identificador) {
		exigencia.setCdExigenciaGerRisco(identificador);
		return this;
	}

	public ExigenciaGerRisco build() {
		return exigencia;
	}

	public static ExigenciaGerRisco ESCOLTA_PP() {
		return newExigenciaGerRisco()
				.id(1)
				.nivel(1)
				.tipo(TipoExigenciaGerRiscoBuilder.ESCOLTA())
				.maiorQuantidade()
				.build();
	}

	public static ExigenciaGerRisco ESCOLTA_ENTRADA() {
		return newExigenciaGerRisco()
				.id(2)
				.nivel(2)
				.tipo(TipoExigenciaGerRiscoBuilder.ESCOLTA())
				.maiorQuantidade()
				.areaRisco()
				.build();
	}

	public static ExigenciaGerRisco MONITORAMENTO_1() {
		return newExigenciaGerRisco()
				.id(3)
				.nivel(1)
				.tipo(TipoExigenciaGerRiscoBuilder.MONITORAMENTO())
				.build();
	}

	public static ExigenciaGerRisco MONITORAMENTO_2() {
		return newExigenciaGerRisco()
				.id(4)
				.nivel(2)
				.tipo(TipoExigenciaGerRiscoBuilder.MONITORAMENTO())
				.build();
	}

	public static ExigenciaGerRisco MOTORISTA_1() {
		return newExigenciaGerRisco()
				.id(5)
				.nivel(1)
				.tipo(TipoExigenciaGerRiscoBuilder.MOTORISTA())
				.build();
	}

	public static ExigenciaGerRisco MOTORISTA_2() {
		return newExigenciaGerRisco()
				.id(6)
				.nivel(2)
				.tipo(TipoExigenciaGerRiscoBuilder.MOTORISTA())
				.build();
	}

	public static ExigenciaGerRisco VEICULO_1() {
		return newExigenciaGerRisco()
				.id(7)
				.nivel(1)
				.tipo(TipoExigenciaGerRiscoBuilder.VEICULO())
				.build();
	}

	public static ExigenciaGerRisco VEICULO_2() {
		return newExigenciaGerRisco()
				.id(8)
				.nivel(2)
				.tipo(TipoExigenciaGerRiscoBuilder.VEICULO())
				.build();
	}

	public static ExigenciaGerRisco AVERBACAO() {
		return newExigenciaGerRisco()
				.id(9)
				.tipo(TipoExigenciaGerRiscoBuilder.AVERBACAO())
				.build();
	}

	public static ExigenciaGerRisco VIRUS() {
		return newExigenciaGerRisco()
				.id(10)
				.tipo(TipoExigenciaGerRiscoBuilder.VIRUS())
				.acumulado()
				.build();
	}

}
