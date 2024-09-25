package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;

public class TradutorChaveProgressao {

	private RotaPreco rota;
	private TarifaPreco tarifa;

	public TradutorChaveProgressao(RotaPreco rota, TarifaPreco tarifa) {
		this.rota = rota;
		this.tarifa = tarifa;
	}
	
	public TarifaPreco tarifaPreco() {
		return this.tarifa;
	}

	public RotaPreco rotaPreco() {
		return this.rota;
	}

}
