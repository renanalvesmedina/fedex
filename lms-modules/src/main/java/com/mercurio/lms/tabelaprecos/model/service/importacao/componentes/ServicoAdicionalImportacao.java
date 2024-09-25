package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.math.BigDecimal;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class ServicoAdicionalImportacao implements ComponenteImportacao {

	private ValorImportacao valorServicoAdicional;
	private ValorImportacao valorMinimoServicoAdicional;

	public ServicoAdicionalImportacao(ValorImportacao valor, ValorImportacao valorMinimo) {
		this.valorServicoAdicional = valor;
		this.valorMinimoServicoAdicional = valorMinimo;
	}

	@Override
	public String valorTagPrincipal() {
		valida();
		return this.valorServicoAdicional.tag();
	}

	@Override
	public TipoComponente tipo() {
		return TipoComponente.SERVICO_ADICIONAL;
	}

	public BigDecimal valor() {
		valida();
		return this.valorServicoAdicional.valorBigDecimal(BigDecimal.ZERO);
	}

	private void valida()  {
		if (this.valorServicoAdicional == null) {
			ValorImportacao valorNaoNulo = this.valorMinimoServicoAdicional;
			throw new ImportacaoException(valorNaoNulo.colunaItem, valorNaoNulo.linhaItem, String.format("Não foi encontrada a tag principal da tag %s.", valorNaoNulo.tag()));
		}
	}

	public BigDecimal valorMinimo() {
		if (this.valorMinimoServicoAdicional == null) {
			return null;
		}
		return this.valorMinimoServicoAdicional.valorBigDecimal(BigDecimal.ZERO);
	}

	@Override
	public TagImportacao tagPrincipal() {
		valida();
		return this.valorServicoAdicional.tagReferancia();
	}


}
