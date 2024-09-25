package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;

public class CodigoTarifaImportacao implements ChaveProgressao {

	private ValorImportacao valorCodigoTarifa;

	public CodigoTarifaImportacao(ValorImportacao codigo) {
		this.valorCodigoTarifa = codigo;
	}

	@Override
	public int linha(){
		return this.valorCodigoTarifa.linhaItem;
	}

	@Override
	public int coluna(){
		return this.valorCodigoTarifa.colunaItem;
	}

	public ValorImportacao codigo() {
		return this.valorCodigoTarifa;
	}

	@Override
	public boolean valida() {
		return StringUtils.isNotBlank(this.valorCodigoTarifa.valorString());
	}

	public String valorString() {
		return this.valorCodigoTarifa.valorString();
	}

	@Override
	public boolean estahCompleta() {
		return this.valida();
	}

	@Override
	public TipoChaveProgressao tipo() {
		return TipoChaveProgressao.TARIFA;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!o.getClass().isAssignableFrom(CodigoTarifaImportacao.class)) {
			return false;
		}
		CodigoTarifaImportacao codigo = (CodigoTarifaImportacao) o;
		if (this == codigo) {
			return true;
		}
		String codigoLocal = "";
		String codigoOutro = "";
		if (this.valorCodigoTarifa != null) {
			codigoLocal = this.valorCodigoTarifa.valorString();
		}
		if (codigo.valorCodigoTarifa != null) {
			codigoOutro = codigo.valorCodigoTarifa.valorString();
		}
		return codigoLocal.equals(codigoOutro);
	}
	
	@Override
	public int hashCode() {
		if (this.valorCodigoTarifa == null) {
			return 0;
		}
		return new HashCodeBuilder().append(this.valorCodigoTarifa.valorString()).toHashCode();
	}

	@Override
	public int compareTo(ChaveProgressao o) {
		if (!CodigoTarifaImportacao.class.isAssignableFrom(o.getClass())) {
			return -1;
		}
		CodigoTarifaImportacao codigoTarifa = (CodigoTarifaImportacao) o;
		return this.hashCode() - codigoTarifa.hashCode();
	}

}
