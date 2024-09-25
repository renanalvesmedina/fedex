package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.tabelaprecos.model.ReajusteParametroParcelaDTO;

public class CalculoReajusteTabelaPrecoParametrizadoService {

	public BigDecimal calculaReajusteParcela(Long idParcela,
			BigDecimal valor,
			List<ReajusteParametroParcelaDTO> parametrosParcelas) {
		if(valor == null){
			return valor;
		}
		for(ReajusteParametroParcelaDTO parcela : parametrosParcelas){
			if(idParcela.equals(parcela.getIdTabelaPrecoParcela())){
				BigDecimal pcParcelaPercent = parcela.getPcParcela().divide(new BigDecimal("100"));
				return calcularReajusteParaOValor(valor, pcParcelaPercent);
			}
		}
		
		return valor;
	}

	public BigDecimal calculaReajusteParcelaMinimo(Long idParcela,
			BigDecimal valor,
			List<ReajusteParametroParcelaDTO> parametrosParcelas) {
		if(valor == null){
			return valor;
		}
		for(ReajusteParametroParcelaDTO parcela : parametrosParcelas){
			if(idParcela.equals(parcela.getIdTabelaPrecoParcela())){
				BigDecimal pcParcelaPercent = parcela.getPcMinParcela().divide(new BigDecimal("100"));
				return calcularReajusteParaOValor(valor, pcParcelaPercent);
			}
		}
		
		return valor;
	}

	public BigDecimal calculaReajusteParcelaPrecoFrete( Long idPrecoFrete,
			BigDecimal valor,
			List<ReajusteParametroParcelaDTO> parametrosParcelas) {
		if(valor == null){
			return valor;
		}
		for(ReajusteParametroParcelaDTO parcela : parametrosParcelas){
			if(idPrecoFrete.equals(parcela.getIdPrecoFrete())){
				BigDecimal pcParcelaPercent = parcela.getPcParcela().divide(new BigDecimal("100"));
				return calcularReajusteParaOValor(valor, pcParcelaPercent);
			}
		}
		
		return valor;
	}

	public BigDecimal calculaReajusteParcelaFaixaProgressiva(
			Long idValorFaixaProgressiva, BigDecimal valor,
			List<ReajusteParametroParcelaDTO> parametrosParcelas) {
		if(valor == null){
			return valor;
		}
		for(ReajusteParametroParcelaDTO parcela : parametrosParcelas){
			if(idValorFaixaProgressiva.equals(parcela.getIdFaixaProgressiva())){
				BigDecimal pcParcelaPercent = parcela.getPcParcela().divide(new BigDecimal("100"));
				return calcularReajusteParaOValor(valor, pcParcelaPercent);
			}
		}
		
		return valor;
	}


	public BigDecimal calcularReajusteParaOValor(BigDecimal valor,
			BigDecimal pcParcelaPercent) {
		if(valor != null){
			BigDecimal reajuste = valor.multiply(pcParcelaPercent);
			return valor.add(reajuste);
		}
		return valor;
	}
}
