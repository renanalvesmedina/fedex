package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.ReajusteParametroParcelaDTO;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;

public class CalculoSimulacaoNovosPrecosParametrizadoService {
	private CalculoSimulacaoNovosPrecosService calculoSimulacaoNovosPrecosService;
	CalculoReajusteTabelaPrecoParametrizadoService calculoReajusteTabelaPrecoParametrizadoService;
	
	public List<TabelaPrecoParcela> executeReajusteGeneralidade(List<TabelaPrecoParcela> tabelaPrecoParcelas, List<TabelaPrecoParcela> excecoes, List<ReajusteParametroParcelaDTO> parametrosParcelas, Boolean isReajuste) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		if (tabelaPrecoParcelas != null) {
			for (TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();

				// verifica se essa generalidade foi reajustada nas execoes
				if (calculoSimulacaoNovosPrecosService.containsExcecao(parcelaPreco.getIdParcelaPreco(), excecoes)) {
					// em caso positivo continua para a proxima generalidade,
					// ja que as reajustadas serao gravadas diretamente
					continue;
				}
				Generalidade generalidade = tabelaPrecoParcela.getGeneralidade();

				TabelaPrecoParcela tpp = calculoSimulacaoNovosPrecosService.copyTabelaPrecoParcela(tabelaPrecoParcela);
				Generalidade g = new Generalidade();

				BigDecimal vlGeneralidade = getReajusteGeneralidade(parametrosParcelas, generalidade, isReajuste);
				g.setVlGeneralidade(vlGeneralidade);
				
				g.setPsMinimo(generalidade.getPsMinimo()); 
				
				BigDecimal vlMinimo = getReajusteGeneralidadeMinimo(parametrosParcelas, generalidade, isReajuste);
				g.setVlMinimo(vlMinimo);

				g.setTabelaPrecoParcela(tpp);
				tpp.setGeneralidade(g);
				result.add(tpp);
			}
		}
		if (excecoes != null) {
			for (TabelaPrecoParcela excecao : excecoes) {
				if (excecao.getGeneralidade() != null) { 
					excecao.getGeneralidade().setIdGeneralidade(null);
				}
			}
			result.addAll(excecoes);
		}
		return result;
	}

	private BigDecimal getReajusteGeneralidadeMinimo(List<ReajusteParametroParcelaDTO> parametrosParcelas, Generalidade generalidade, Boolean isReajuste) {
		if(isReajuste){
			return calculoReajusteTabelaPrecoParametrizadoService.calculaReajusteParcelaMinimo(generalidade.getIdGeneralidade(), generalidade.getVlMinimo(), parametrosParcelas);
		}
		return generalidade.getVlMinimo();
	}

	private BigDecimal getReajusteGeneralidade(List<ReajusteParametroParcelaDTO> parametrosParcelas, Generalidade generalidade, Boolean isReajuste) {
		if(isReajuste){
			return calculoReajusteTabelaPrecoParametrizadoService.calculaReajusteParcela(generalidade.getIdGeneralidade(), generalidade.getVlGeneralidade(), parametrosParcelas);
		}
		return generalidade.getVlGeneralidade();
	}


	public List<TabelaPrecoParcela> executeReajusteValorTaxa(List<TabelaPrecoParcela> tabelaPrecoParcelas, List<TabelaPrecoParcela> excecoes, List<ReajusteParametroParcelaDTO> parametrosParcelas, Boolean isReajuste) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();
		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();

				// verifica se essa generalidade foi reajustada nas execoes
				if (calculoSimulacaoNovosPrecosService.containsExcecao(parcelaPreco.getIdParcelaPreco(), excecoes)) {
					// em caso positivo continua para a proxima generalidade,
					// ja que as reajustadas serao gravadas diretamente
					continue;
				}
				ValorTaxa valorTaxa = tabelaPrecoParcela.getValorTaxa();

				TabelaPrecoParcela tpp = calculoSimulacaoNovosPrecosService.copyTabelaPrecoParcela(tabelaPrecoParcela);
				ValorTaxa vt = new ValorTaxa();

				BigDecimal vlTaxa = getReajusteValorTaxa(parametrosParcelas, valorTaxa, isReajuste);
				vt.setVlTaxa(vlTaxa);

				BigDecimal vlExcedente = getReajusteValorTaxaExcedente(parametrosParcelas, valorTaxa, isReajuste);
				vt.setVlExcedente(vlExcedente);

				vt.setTabelaPrecoParcela(tpp);
				vt.setPsTaxado(valorTaxa.getPsTaxado());
				tpp.setValorTaxa(vt);
				result.add(tpp);
			}
		}

		if(excecoes != null) {
			for(TabelaPrecoParcela tpp : excecoes) {
				if (tpp.getValorTaxa() != null) {
					tpp.getValorTaxa().setIdValorTaxa(null);
				}
			}
			result.addAll(excecoes);
		}

		return result;
	}

	private BigDecimal getReajusteValorTaxa(
			List<ReajusteParametroParcelaDTO> parametrosParcelas,
			ValorTaxa taxa, Boolean isReajuste) {
		if(isReajuste){
			return calculoReajusteTabelaPrecoParametrizadoService.calculaReajusteParcela(taxa.getIdValorTaxa(), taxa.getVlTaxa(), parametrosParcelas);
		}
		return taxa.getVlTaxa();
	}

	private BigDecimal getReajusteValorTaxaExcedente(
			List<ReajusteParametroParcelaDTO> parametrosParcelas,
			ValorTaxa taxa, Boolean isReajuste) {
		if(isReajuste){
			return calculoReajusteTabelaPrecoParametrizadoService.calculaReajusteParcelaMinimo(taxa.getIdValorTaxa(), taxa.getVlExcedente(), parametrosParcelas);
		}
		return taxa.getVlTaxa();
	}

	public List<TabelaPrecoParcela> executeReajusteServicoAdicional(List<TabelaPrecoParcela> tabelaPrecoParcelas, List<ReajusteParametroParcelaDTO> parametrosParcelas, Boolean isReajuste) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();
		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				ValorServicoAdicional valorServicoAdicional = tabelaPrecoParcela.getValorServicoAdicional();

				TabelaPrecoParcela tpp = calculoSimulacaoNovosPrecosService.copyTabelaPrecoParcela(tabelaPrecoParcela);
				ValorServicoAdicional vsa = new ValorServicoAdicional();

				BigDecimal vlServico = getReajusteServicoAdicional(parametrosParcelas, valorServicoAdicional, isReajuste);
				vsa.setVlServico(vlServico);

				BigDecimal vlMinimo = getReajusteServicoAdicionalMinimo(parametrosParcelas, valorServicoAdicional, isReajuste);
				vsa.setVlMinimo(vlMinimo);

				vsa.setTabelaPrecoParcela(tpp);
				tpp.setValorServicoAdicional(vsa);
				result.add(tpp);
			}
		}
		return result;
	}

	private BigDecimal getReajusteServicoAdicional(
			List<ReajusteParametroParcelaDTO> parametrosParcelas,
			ValorServicoAdicional servico, Boolean isReajuste) {
		if(isReajuste){
			return calculoReajusteTabelaPrecoParametrizadoService.calculaReajusteParcela(servico.getIdValorServicoAdicional(), servico.getVlServico(), parametrosParcelas);
		}
		return servico.getVlServico();
	}

	private BigDecimal getReajusteServicoAdicionalMinimo(
			List<ReajusteParametroParcelaDTO> parametrosParcelas,
			ValorServicoAdicional servico, Boolean isReajuste) {
		if(isReajuste){
			return calculoReajusteTabelaPrecoParametrizadoService.calculaReajusteParcelaMinimo(servico.getIdValorServicoAdicional(), servico.getVlMinimo(), parametrosParcelas);
		}
		return servico.getVlMinimo();
	}


	public List<TabelaPrecoParcela> executeReajustePrecoFrete(List<TabelaPrecoParcela> tabelaPrecoParcelas, List<ReajusteParametroParcelaDTO> parametrosParcelas, Boolean isReajuste) {
		if(CollectionUtils.isEmpty(tabelaPrecoParcelas)){
			return null;
		}
		
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();
		for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
			List<PrecoFrete> precoFretes = tabelaPrecoParcela.getPrecoFretes();

			TabelaPrecoParcela tpp = calculoSimulacaoNovosPrecosService.copyTabelaPrecoParcela(tabelaPrecoParcela);
			List<PrecoFrete> pfs = new ArrayList<PrecoFrete>();
			if (precoFretes != null) {
				for(PrecoFrete precoFrete : precoFretes) {
					PrecoFrete pf = new PrecoFrete();
					pf.setVlPrecoFrete(getReajustePrecoFrete(parametrosParcelas, precoFrete, isReajuste));
					pf.setRotaPreco(precoFrete.getRotaPreco());
					pf.setTabelaPrecoParcela(tpp);
					pf.setTarifaPreco(precoFrete.getTarifaPreco());
					pf.setPesoMinimo(precoFrete.getPesoMinimo());			
					pfs.add(pf);
				}
			}
			tpp.setPrecoFretes(pfs);
			result.add(tpp);
		}
		
		return result;
	}

	private BigDecimal getReajustePrecoFrete(List<ReajusteParametroParcelaDTO> parametrosParcelas, PrecoFrete precoFrete, Boolean isReajuste) {
		if(isReajuste){
			return calculoReajusteTabelaPrecoParametrizadoService.calculaReajusteParcelaPrecoFrete(precoFrete.getIdPrecoFrete(), precoFrete.getVlPrecoFrete(), parametrosParcelas);
		}
		return precoFrete.getVlPrecoFrete();
	}
	
	public List<TabelaPrecoParcela> executeReajusteMinimoProgressivo(List<TabelaPrecoParcela> tabelaPrecoParcelas, List<ReajusteParametroParcelaDTO> parametrosParcelas, Boolean isReajuste) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				List<FaixaProgressiva> faixasProgressivas = tabelaPrecoParcela.getFaixaProgressivas();

				TabelaPrecoParcela tpp = calculoSimulacaoNovosPrecosService.copyTabelaPrecoParcela(tabelaPrecoParcela);

				if (faixasProgressivas != null && faixasProgressivas.size() > 0) {
					List<FaixaProgressiva> faixasProgressivasNew = new ArrayList<FaixaProgressiva>();
					for (FaixaProgressiva faixaProgressiva : faixasProgressivas) {
						FaixaProgressiva faixaProgressivaNew = new FaixaProgressiva();
						List<ValorFaixaProgressiva> valoresFaixaProgressiva = faixaProgressiva.getValoresFaixasProgressivas();
						if (valoresFaixaProgressiva != null && valoresFaixaProgressiva.size() > 0) {
							List<ValorFaixaProgressiva> valoresFaixaProgressivaNew = new ArrayList<ValorFaixaProgressiva>();
							for (ValorFaixaProgressiva valorFaixaProgressiva : valoresFaixaProgressiva) {
								if (Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional())) {
									continue;
								}
								ValorFaixaProgressiva valorFaixaProgressivaNew = new ValorFaixaProgressiva();
								valorFaixaProgressivaNew.setVlFixo(getReajusteFaixaProgressivaFixo(parametrosParcelas, valorFaixaProgressiva, isReajuste));
								valorFaixaProgressivaNew.setBlPromocional(Boolean.FALSE);
								valorFaixaProgressivaNew.setDtVigenciaPromocaoFinal(null);
								valorFaixaProgressivaNew.setDtVigenciaPromocaoInicial(null);
								valorFaixaProgressivaNew.setNrFatorMultiplicacao(valorFaixaProgressiva.getNrFatorMultiplicacao());
								valorFaixaProgressivaNew.setPcDesconto(valorFaixaProgressiva.getPcDesconto());
								valorFaixaProgressivaNew.setPcTaxa(valorFaixaProgressiva.getPcTaxa());
								valorFaixaProgressivaNew.setRotaPreco(valorFaixaProgressiva.getRotaPreco());
								valorFaixaProgressivaNew.setTarifaPreco(valorFaixaProgressiva.getTarifaPreco());
								valorFaixaProgressivaNew.setVlAcrescimo(valorFaixaProgressiva.getVlAcrescimo());
								valorFaixaProgressivaNew.setFaixaProgressiva(faixaProgressivaNew);

								valoresFaixaProgressivaNew.add(valorFaixaProgressivaNew);
							}
							faixaProgressivaNew.setValoresFaixasProgressivas(valoresFaixaProgressivaNew);
						}
						faixaProgressivaNew.setUnidadeMedida(faixaProgressiva.getUnidadeMedida());
						faixaProgressivaNew.setTpIndicadorCalculo(faixaProgressiva.getTpIndicadorCalculo());
						faixaProgressivaNew.setCdMinimoProgressivo(faixaProgressiva.getCdMinimoProgressivo());
						faixaProgressivaNew.setTpSituacao(new DomainValue("A"));
						faixaProgressivaNew.setProdutoEspecifico(faixaProgressiva.getProdutoEspecifico());
						faixaProgressivaNew.setVlFaixaProgressiva(faixaProgressiva.getVlFaixaProgressiva());
						faixaProgressivaNew.setTabelaPrecoParcela(tpp);

						faixasProgressivasNew.add(faixaProgressivaNew);
					}
					tpp.setFaixaProgressivas(faixasProgressivasNew);
				}
				result.add(tpp);
			}
		}
		return result;
	}


	private BigDecimal getReajusteFaixaProgressivaFixo(
			List<ReajusteParametroParcelaDTO> parametrosParcelas,
			ValorFaixaProgressiva faixaProgressiva, Boolean isReajuste) {
		if(isReajuste){
			return calculoReajusteTabelaPrecoParametrizadoService.calculaReajusteParcelaFaixaProgressiva(faixaProgressiva.getIdValorFaixaProgressiva(), faixaProgressiva.getVlFixo(), parametrosParcelas);
		}
		return faixaProgressiva.getVlFixo();
	}

	
	public void setCalculoSimulacaoNovosPrecosService(
			CalculoSimulacaoNovosPrecosService calculoSimulacaoNovosPrecosService) {
		this.calculoSimulacaoNovosPrecosService = calculoSimulacaoNovosPrecosService;
	}

	public void setCalculoReajusteTabelaPrecoParametrizadoService(
			CalculoReajusteTabelaPrecoParametrizadoService calculoReajusteTabelaPrecoParametrizadoService) {
		this.calculoReajusteTabelaPrecoParametrizadoService = calculoReajusteTabelaPrecoParametrizadoService;
	}
	
	
}
