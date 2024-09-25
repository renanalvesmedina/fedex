/**
 * 
 */
package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.calculoNovosPrecosCiasAereasService"
 */
public class CalculoNovosPrecosCiasAereasService {
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;

	public List<TabelaPrecoParcela> executeReajusteTarifaMinima(
			Long idTabelaBase,
			BigDecimal pcReajuste,
			List<TypedFlatMap> aeroportosExcecao,
			List<TypedFlatMap> reajustesEspecificos
	) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findByIdTabelaPrecoCdParcelaPreco(idTabelaBase, ConstantesExpedicao.CD_TARIFA_MINIMA);
		if (tabelaPrecoParcelas != null && tabelaPrecoParcelas.size() > 0) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				List<PrecoFrete> precoFretes = tabelaPrecoParcela.getPrecoFretes();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);
				List<PrecoFrete> pfs = new ArrayList<PrecoFrete>();

				if (precoFretes != null && precoFretes.size() > 0) {
					for(PrecoFrete precoFrete : precoFretes) {
						PrecoFrete pf = new PrecoFrete();
						RotaPreco rotaPreco = precoFrete.getRotaPreco();

						if (!"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue())
								&& !isAeroportoExcecao(precoFrete.getRotaPreco(), aeroportosExcecao)) {

							BigDecimal vlPrecoFrete = calculaReajuste(precoFrete.getVlPrecoFrete(), pcReajuste);
							BigDecimal pcDescontoEspecifico = findReajusteEspecifico(rotaPreco, reajustesEspecificos);
							if (pcDescontoEspecifico != null) {
								vlPrecoFrete = calculaDesconto(vlPrecoFrete, pcDescontoEspecifico);
							}
							pf.setVlPrecoFrete(vlPrecoFrete);
						} else {
							pf.setVlPrecoFrete(precoFrete.getVlPrecoFrete());
						}
						
						pf.setRotaPreco(precoFrete.getRotaPreco());
						pf.setTabelaPrecoParcela(tpp);
						pf.setTarifaPreco(precoFrete.getTarifaPreco());
						
						pfs.add(pf);
					}
				}
				
				tpp.setPrecoFretes(pfs);
				result.add(tpp);
			}
		}
		
		return result;
	}
	
	public List<TabelaPrecoParcela> executeReajusteEspecifico(
			Long idTabelaBase,
			BigDecimal pcReajuste,
			List<TypedFlatMap> aeroportosExcecao,
			List<TypedFlatMap> produtosEspecificosExcecao,
			List<TypedFlatMap> reajustesEspecificos
	) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findByIdTabelaPrecoCdParcelaPreco(idTabelaBase, ConstantesExpedicao.CD_FRETE_PESO);
		if (tabelaPrecoParcelas != null && tabelaPrecoParcelas.size() > 0) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				List<FaixaProgressiva> faixaProgressivas = tabelaPrecoParcela.getFaixaProgressivas();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);

				if (faixaProgressivas != null && faixaProgressivas.size() > 0) {
					List<FaixaProgressiva> fps = new ArrayList<FaixaProgressiva>();

					for(FaixaProgressiva faixaProgressiva : faixaProgressivas) {
						if (faixaProgressiva.getProdutoEspecifico() == null) {
							FaixaProgressiva fp = copyFaixaProgressiva(tpp, faixaProgressiva);
							fps.add(fp);
						} else {
							boolean isProdutoEspecificoExcecao = isProdutoEspecificoExcecao(faixaProgressiva.getProdutoEspecifico(), produtosEspecificosExcecao);

							FaixaProgressiva fp = new FaixaProgressiva();
							List<ValorFaixaProgressiva> valoresFaixaProgressiva = faixaProgressiva.getValoresFaixasProgressivas();
							if (valoresFaixaProgressiva != null && valoresFaixaProgressiva.size() > 0) {
								List<ValorFaixaProgressiva> vfps = new ArrayList<ValorFaixaProgressiva>();

								for(ValorFaixaProgressiva valorFaixaProgressiva : valoresFaixaProgressiva) {
									if (Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional())) {
										continue;
									}

									ValorFaixaProgressiva vfp = new ValorFaixaProgressiva();
									if (valorFaixaProgressiva.getVlFixo() != null && 
										CompareUtils.ne(valorFaixaProgressiva.getVlFixo(), BigDecimalUtils.ZERO) &&
										!Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional()) &&
										!isAeroportoExcecao(valorFaixaProgressiva.getRotaPreco(), aeroportosExcecao) &&
										!isProdutoEspecificoExcecao &&
										!"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue()) &&
										!"PC".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())) {
										
										BigDecimal vlFixo = calculaReajuste(valorFaixaProgressiva.getVlFixo(), pcReajuste);
										RotaPreco rotaPreco = valorFaixaProgressiva.getRotaPreco();
										BigDecimal pcDescontoEspecifico = findReajusteEspecifico(rotaPreco, reajustesEspecificos);
										if (pcDescontoEspecifico != null) {
											vlFixo = calculaDesconto(vlFixo, pcDescontoEspecifico);
										}
										vfp.setVlFixo(vlFixo);
									} else {
										vfp.setVlFixo(valorFaixaProgressiva.getVlFixo());
									}
									
									vfp.setBlPromocional(Boolean.FALSE);
									vfp.setDtVigenciaPromocaoFinal(null);
									vfp.setDtVigenciaPromocaoInicial(null);
									vfp.setNrFatorMultiplicacao(valorFaixaProgressiva.getNrFatorMultiplicacao());
									vfp.setPcDesconto(valorFaixaProgressiva.getPcDesconto());
									vfp.setPcTaxa(valorFaixaProgressiva.getPcTaxa());
									vfp.setRotaPreco(valorFaixaProgressiva.getRotaPreco());
									vfp.setTarifaPreco(valorFaixaProgressiva.getTarifaPreco());
									vfp.setVlAcrescimo(valorFaixaProgressiva.getVlAcrescimo());
									vfp.setFaixaProgressiva(fp);
									
									vfps.add(vfp);
								}
								
								if (vfps.size() > 0) {
									fp.setValoresFaixasProgressivas(vfps);
								}
							}
							fp.setUnidadeMedida(faixaProgressiva.getUnidadeMedida());
							fp.setTpIndicadorCalculo(faixaProgressiva.getTpIndicadorCalculo());
							fp.setCdMinimoProgressivo(faixaProgressiva.getCdMinimoProgressivo());
							fp.setTpSituacao(new DomainValue("A"));
							fp.setProdutoEspecifico(faixaProgressiva.getProdutoEspecifico());
							fp.setVlFaixaProgressiva(faixaProgressiva.getVlFaixaProgressiva());
							fp.setTabelaPrecoParcela(tpp);
							
							if (fp.getValoresFaixasProgressivas() != null) {
								fps.add(fp);
							}
						} // fim do else
					} // fim do for
					
					if (fps.size() > 0) {
						tpp.setFaixaProgressivas(fps);
					}
				}
				
				if (tpp.getFaixaProgressivas() != null) {
					result.add(tpp);
				}
			}
		}
		
		return result;
	}
	
	public List<TabelaPrecoParcela> executeReajusteGeral(
			Long idTabelaBase,
			BigDecimal pcReajuste,
			List<TypedFlatMap> aeroportosExcecao, 
			List<TypedFlatMap> reajustesEspecificos
	) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findByIdTabelaPrecoCdParcelaPreco(idTabelaBase, ConstantesExpedicao.CD_FRETE_PESO);
		if (tabelaPrecoParcelas != null && tabelaPrecoParcelas.size() > 0) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				List<FaixaProgressiva> faixaProgressivas = tabelaPrecoParcela.getFaixaProgressivas();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);
				if (faixaProgressivas != null && faixaProgressivas.size() > 0) {
					List<FaixaProgressiva> fps = new ArrayList<FaixaProgressiva>();
					for(FaixaProgressiva faixaProgressiva : faixaProgressivas) {
						if (faixaProgressiva.getVlFaixaProgressiva() == null) {
							FaixaProgressiva fp = copyFaixaProgressiva(tpp, faixaProgressiva);
							fps.add(fp);
						} else {
							FaixaProgressiva fp = new FaixaProgressiva();
							List<ValorFaixaProgressiva> valoresFaixaProgressiva = faixaProgressiva.getValoresFaixasProgressivas();
							if (valoresFaixaProgressiva != null && valoresFaixaProgressiva.size() > 0) {
								List<ValorFaixaProgressiva> vfps = new ArrayList<ValorFaixaProgressiva>();

								for(ValorFaixaProgressiva valorFaixaProgressiva : valoresFaixaProgressiva) {
									if (Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional())) {
										continue;
									}

									ValorFaixaProgressiva vfp = new ValorFaixaProgressiva();

									if (valorFaixaProgressiva.getVlFixo() != null && 
										CompareUtils.ne(valorFaixaProgressiva.getVlFixo(), BigDecimalUtils.ZERO) &&
										!Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional()) &&
										!isAeroportoExcecao(valorFaixaProgressiva.getRotaPreco(), aeroportosExcecao) &&
										!"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue()) &&
										!"PC".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())) {
										
										BigDecimal vlFixo = calculaReajuste(valorFaixaProgressiva.getVlFixo(), pcReajuste);
										RotaPreco rotaPreco = valorFaixaProgressiva.getRotaPreco();
										BigDecimal pcDescontoEspecifico = findReajusteEspecifico(rotaPreco, reajustesEspecificos);
										if (pcDescontoEspecifico != null) {
											vlFixo = calculaDesconto(vlFixo, pcDescontoEspecifico);
										}
										vfp.setVlFixo(vlFixo);
									} else {
										vfp.setVlFixo(valorFaixaProgressiva.getVlFixo());
									}
									
									vfp.setBlPromocional(Boolean.FALSE);
									vfp.setDtVigenciaPromocaoFinal(null);
									vfp.setDtVigenciaPromocaoInicial(null);
									vfp.setNrFatorMultiplicacao(valorFaixaProgressiva.getNrFatorMultiplicacao());
									vfp.setPcDesconto(valorFaixaProgressiva.getPcDesconto());
									vfp.setPcTaxa(valorFaixaProgressiva.getPcTaxa());
									vfp.setRotaPreco(valorFaixaProgressiva.getRotaPreco());
									vfp.setTarifaPreco(valorFaixaProgressiva.getTarifaPreco());
									vfp.setVlAcrescimo(valorFaixaProgressiva.getVlAcrescimo());
									vfp.setFaixaProgressiva(fp);
									
									vfps.add(vfp);
								}
								
								if (vfps.size() > 0) {
									fp.setValoresFaixasProgressivas(vfps);
								}
							}
							
							fp.setUnidadeMedida(faixaProgressiva.getUnidadeMedida());
							fp.setTpIndicadorCalculo(faixaProgressiva.getTpIndicadorCalculo());
							fp.setCdMinimoProgressivo(faixaProgressiva.getCdMinimoProgressivo());
							fp.setTpSituacao(new DomainValue("A"));
							fp.setProdutoEspecifico(faixaProgressiva.getProdutoEspecifico());
							fp.setVlFaixaProgressiva(faixaProgressiva.getVlFaixaProgressiva());
							fp.setTabelaPrecoParcela(tpp);
							
							if (fp.getValoresFaixasProgressivas() != null) {
								fps.add(fp);
							}
						}
					}
					
					if (fps.size() > 0) {
						tpp.setFaixaProgressivas(fps);
					}
				}
				
				if (tpp.getFaixaProgressivas() != null) {
					result.add(tpp);
				}
			}
		}
		
		return result;
	}
	
	public List<TabelaPrecoParcela> executeReajusteGeralEspecifico(
			Long idTabelaBase,
			BigDecimal pcReajuste,
			List<TypedFlatMap> aeroportosExcecao,
			List<TypedFlatMap> produtosEspecificosExcecao,
			List<TypedFlatMap> reajustesEspecificos
	) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findByIdTabelaPrecoCdParcelaPreco(idTabelaBase, ConstantesExpedicao.CD_FRETE_PESO);
		if (tabelaPrecoParcelas != null && tabelaPrecoParcelas.size() > 0) {
			for (int i = 0; i < tabelaPrecoParcelas.size(); i++) {
				TabelaPrecoParcela tabelaPrecoParcela = (TabelaPrecoParcela) tabelaPrecoParcelas.get(i);
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				List<FaixaProgressiva> faixaProgressivas = tabelaPrecoParcela.getFaixaProgressivas();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);

				if (faixaProgressivas != null && faixaProgressivas.size() > 0) {
					List<FaixaProgressiva> fps = new ArrayList<FaixaProgressiva>();
					
					for(FaixaProgressiva faixaProgressiva : faixaProgressivas) {
						if (faixaProgressiva.getProdutoEspecifico() != null) {
							boolean isProdutoEspecificoExcecao = isProdutoEspecificoExcecao(faixaProgressiva.getProdutoEspecifico(), produtosEspecificosExcecao);

							FaixaProgressiva fp = new FaixaProgressiva();
							List<ValorFaixaProgressiva> valoresFaixaProgressiva = faixaProgressiva.getValoresFaixasProgressivas();
							if (valoresFaixaProgressiva != null && valoresFaixaProgressiva.size() > 0) {
								List<ValorFaixaProgressiva> vfps = new ArrayList<ValorFaixaProgressiva>();
								
								for(ValorFaixaProgressiva valorFaixaProgressiva : valoresFaixaProgressiva) {
									if (Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional())) {
										continue;
									}

									ValorFaixaProgressiva vfp = new ValorFaixaProgressiva();

									if (valorFaixaProgressiva.getVlFixo() != null && 
										CompareUtils.ne(valorFaixaProgressiva.getVlFixo(), BigDecimalUtils.ZERO) &&
										!Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional()) &&
										!isAeroportoExcecao(valorFaixaProgressiva.getRotaPreco(), aeroportosExcecao) &&
										!isProdutoEspecificoExcecao &&
										!"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue()) &&
										!"PC".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())) {
										
										BigDecimal vlFixo = calculaReajuste(valorFaixaProgressiva.getVlFixo(), pcReajuste);
										RotaPreco rotaPreco = valorFaixaProgressiva.getRotaPreco();
										BigDecimal pcDescontoEspecifico = findReajusteEspecifico(rotaPreco, reajustesEspecificos);
										if (pcDescontoEspecifico != null) {
											vlFixo = calculaDesconto(vlFixo, pcDescontoEspecifico);
										}
										vfp.setVlFixo(vlFixo);
									} else {
										vfp.setVlFixo(valorFaixaProgressiva.getVlFixo());
									}
									
									vfp.setBlPromocional(Boolean.FALSE);
									vfp.setDtVigenciaPromocaoFinal(null);
									vfp.setDtVigenciaPromocaoInicial(null);
									vfp.setNrFatorMultiplicacao(valorFaixaProgressiva.getNrFatorMultiplicacao());
									vfp.setPcDesconto(valorFaixaProgressiva.getPcDesconto());
									vfp.setPcTaxa(valorFaixaProgressiva.getPcTaxa());
									vfp.setRotaPreco(valorFaixaProgressiva.getRotaPreco());
									vfp.setTarifaPreco(valorFaixaProgressiva.getTarifaPreco());
									vfp.setVlAcrescimo(valorFaixaProgressiva.getVlAcrescimo());
									vfp.setFaixaProgressiva(fp);

									vfps.add(vfp);
								}

								if (vfps.size() > 0) {
									fp.setValoresFaixasProgressivas(vfps);
								}
							}
							fp.setUnidadeMedida(faixaProgressiva.getUnidadeMedida());
							fp.setTpIndicadorCalculo(faixaProgressiva.getTpIndicadorCalculo());
							fp.setCdMinimoProgressivo(faixaProgressiva.getCdMinimoProgressivo());
							fp.setTpSituacao(new DomainValue("A"));
							fp.setProdutoEspecifico(faixaProgressiva.getProdutoEspecifico());
							fp.setVlFaixaProgressiva(faixaProgressiva.getVlFaixaProgressiva());
							fp.setTabelaPrecoParcela(tpp);
							
							if (fp.getValoresFaixasProgressivas() != null) {
								fps.add(fp);
							}
						} else if(faixaProgressiva.getVlFaixaProgressiva() != null) { // fim do if produto especifico
							FaixaProgressiva fp = new FaixaProgressiva();
							List<ValorFaixaProgressiva> valoresFaixaProgressiva = faixaProgressiva.getValoresFaixasProgressivas();
							if(valoresFaixaProgressiva != null && valoresFaixaProgressiva.size() > 0) {
								List<ValorFaixaProgressiva> vfps = new ArrayList<ValorFaixaProgressiva>();
								
								for(ValorFaixaProgressiva valorFaixaProgressiva : valoresFaixaProgressiva) {
									if (Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional())) {
										continue;
									}

									ValorFaixaProgressiva vfp = new ValorFaixaProgressiva();
									if (valorFaixaProgressiva.getVlFixo() != null && 
										CompareUtils.ne(valorFaixaProgressiva.getVlFixo(), BigDecimalUtils.ZERO)
										&& !Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional()) &&
										!isAeroportoExcecao(valorFaixaProgressiva.getRotaPreco(), aeroportosExcecao)
										&& !"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue())
										&& !"PC".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())
									) {
										BigDecimal vlFixo = calculaReajuste(valorFaixaProgressiva.getVlFixo(), pcReajuste);
										RotaPreco rotaPreco = valorFaixaProgressiva.getRotaPreco();
										BigDecimal pcDescontoEspecifico = findReajusteEspecifico(rotaPreco, reajustesEspecificos);
										if (pcDescontoEspecifico != null) {
											vlFixo = calculaDesconto(vlFixo, pcDescontoEspecifico);
										}
										vfp.setVlFixo(vlFixo);
									} else {
										vfp.setVlFixo(valorFaixaProgressiva.getVlFixo());
									}
									
									vfp.setBlPromocional(Boolean.FALSE);
									vfp.setDtVigenciaPromocaoFinal(null);
									vfp.setDtVigenciaPromocaoInicial(null);
									vfp.setNrFatorMultiplicacao(valorFaixaProgressiva.getNrFatorMultiplicacao());
									vfp.setPcDesconto(valorFaixaProgressiva.getPcDesconto());
									vfp.setPcTaxa(valorFaixaProgressiva.getPcTaxa());
									vfp.setRotaPreco(valorFaixaProgressiva.getRotaPreco());
									vfp.setTarifaPreco(valorFaixaProgressiva.getTarifaPreco());
									vfp.setVlAcrescimo(valorFaixaProgressiva.getVlAcrescimo());
									vfp.setFaixaProgressiva(fp);
									
									vfps.add(vfp);
								}
								
								if (vfps.size() > 0) {
									fp.setValoresFaixasProgressivas(vfps);
								}
							}
							
							fp.setUnidadeMedida(faixaProgressiva.getUnidadeMedida());
							fp.setTpIndicadorCalculo(faixaProgressiva.getTpIndicadorCalculo());
							fp.setCdMinimoProgressivo(faixaProgressiva.getCdMinimoProgressivo());
							fp.setTpSituacao(new DomainValue("A"));
							fp.setProdutoEspecifico(faixaProgressiva.getProdutoEspecifico());
							fp.setVlFaixaProgressiva(faixaProgressiva.getVlFaixaProgressiva());
							fp.setTabelaPrecoParcela(tpp);
							
							if (fp.getValoresFaixasProgressivas() != null) {
								fps.add(fp);
							}
						}
					} // fim do for
					
					if (fps.size() > 0) {
						tpp.setFaixaProgressivas(fps);
					}
				}
				
				if (tpp.getFaixaProgressivas() != null) {
					result.add(tpp);
				}
			}
		}
		
		return result;
	}
	
	/*
	 * Metodos privados
	 */

	private BigDecimal calculaDesconto(BigDecimal valor, BigDecimal pcDesconto) {
		if (valor != null && pcDesconto != null) {
			BigDecimal percent = BigDecimalUtils.percent(pcDesconto);
			BigDecimal result = valor.multiply(percent);
			result = valor.subtract(result);
			return result;
		}
		return null;
	}
	
	private BigDecimal findReajusteEspecifico(RotaPreco rotaPreco, List<TypedFlatMap> reajustesEspecificos) {
		BigDecimal result = null;
		
		if (reajustesEspecificos != null && reajustesEspecificos.size() > 0) {
			Long idOrigem = null;
			if (rotaPreco.getAeroportoByIdAeroportoOrigem() != null) {
				idOrigem = rotaPreco.getAeroportoByIdAeroportoOrigem().getIdAeroporto();
			}

			Long idDestino = null;
			if (rotaPreco.getAeroportoByIdAeroportoDestino() != null) {
				idDestino = rotaPreco.getAeroportoByIdAeroportoDestino().getIdAeroporto();
			}

			for (TypedFlatMap reajuste : reajustesEspecificos) {
				Long idOrigemReajuste = reajuste.getLong("aeroportoByIdAeroportoOrigem.idAeroporto");
				Long idDestinoReajuste = reajuste.getLong("aeroportoByIdAeroportoDestino.idAeroporto");
				if (idOrigemReajuste != null && idDestinoReajuste != null) {
					if (idOrigemReajuste.equals(idOrigem) && idDestinoReajuste.equals(idDestino)) {
						result = reajuste.getBigDecimal("pcDesconto");
						break;
					}
				} else if (idOrigemReajuste == null && idDestinoReajuste != null) {
					if (idDestinoReajuste.equals(idDestino)) {
						result = reajuste.getBigDecimal("pcDesconto");
					}
				} else if (idOrigemReajuste != null && idDestinoReajuste == null) {
					if (idOrigemReajuste.equals(idOrigem)) {
						result = reajuste.getBigDecimal("pcDesconto");
					}
				}
			}
		}

		return result;
	}
	
	private boolean isAeroportoExcecao(RotaPreco rotaPreco, List<TypedFlatMap> aeroportosExcecao) {
		if (aeroportosExcecao != null && aeroportosExcecao.size() > 0) {
			Long idAeroportoDestino = null;
			if (rotaPreco.getAeroportoByIdAeroportoDestino() != null) {
				idAeroportoDestino = rotaPreco.getAeroportoByIdAeroportoDestino().getIdAeroporto();
			}
			for(TypedFlatMap aeroporto : aeroportosExcecao) {
				Long idAeroporto = aeroporto.getLong("aeroportoByIdAeroportoDestino.idAeroporto");
				if (idAeroporto.equals(idAeroportoDestino)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isProdutoEspecificoExcecao(ProdutoEspecifico produtoEspecifico, List<TypedFlatMap> produtosEspecificosExcecao) {
		if (produtosEspecificosExcecao != null && produtosEspecificosExcecao.size() > 0) {
			Long idProdutoEspecifico = produtoEspecifico.getIdProdutoEspecifico();
			for(TypedFlatMap excecao : produtosEspecificosExcecao) {
				Long idExcecao = excecao.getLong("idProdutoEspecifico");
				if (idExcecao.equals(idProdutoEspecifico)) {
					return true;
				}
			}
		}
		return false;
	}

	private BigDecimal calculaReajuste(BigDecimal valor, BigDecimal pcReajuste) {
		if (valor != null && pcReajuste != null) {
			BigDecimal percent = BigDecimalUtils.percent(pcReajuste);
			BigDecimal result = valor.multiply(percent);
			result = BigDecimalUtils.add(result, valor);
			return result;
		}
		return null;
	}
	
	private TabelaPrecoParcela copyTabelaPrecoParcela(TabelaPrecoParcela original) {
		TabelaPrecoParcela result = new TabelaPrecoParcela();
		result.setParcelaPreco(original.getParcelaPreco());
		return result;
	}
	
	private FaixaProgressiva copyFaixaProgressiva(TabelaPrecoParcela tabelaPrecoParcela, FaixaProgressiva original) {
		FaixaProgressiva result = new FaixaProgressiva();
		result.setCdMinimoProgressivo(original.getCdMinimoProgressivo());
		result.setProdutoEspecifico(original.getProdutoEspecifico());
		result.setTabelaPrecoParcela(tabelaPrecoParcela);
		result.setTpIndicadorCalculo(original.getTpIndicadorCalculo());
		result.setUnidadeMedida(original.getUnidadeMedida());
		result.setVersao(original.getVersao());
		result.setVlFaixaProgressiva(original.getVlFaixaProgressiva());
		result.setTpSituacao(original.getTpSituacao());
		
		List<ValorFaixaProgressiva> vfps = original.getValoresFaixasProgressivas();
		if (vfps != null && vfps.size() > 0) {
			List<ValorFaixaProgressiva> valores = new ArrayList<ValorFaixaProgressiva>();
			for(ValorFaixaProgressiva vfp : vfps) {
				ValorFaixaProgressiva newVfp = copyValorFaixaProgressiva(result, vfp);
				valores.add(newVfp);
			}
			result.setValoresFaixasProgressivas(valores);
		}

		return result;
	}

	private ValorFaixaProgressiva copyValorFaixaProgressiva(FaixaProgressiva faixaProgressiva, ValorFaixaProgressiva original) {
		ValorFaixaProgressiva result = new ValorFaixaProgressiva();
		result.setBlPromocional(original.getBlPromocional());
		result.setDtVigenciaPromocaoFinal(original.getDtVigenciaPromocaoFinal());
		result.setDtVigenciaPromocaoInicial(original.getDtVigenciaPromocaoInicial());
		result.setFaixaProgressiva(faixaProgressiva);
		result.setNrFatorMultiplicacao(original.getNrFatorMultiplicacao());
		result.setPcDesconto(original.getPcDesconto());
		result.setPcTaxa(original.getPcTaxa());
		result.setRotaPreco(original.getRotaPreco());
		result.setTarifaPreco(original.getTarifaPreco());
		result.setVersao(original.getVersao());
		result.setVlAcrescimo(original.getVlAcrescimo());
		result.setVlFixo(original.getVlFixo());
		return result;
	}

	/*
	 * Getters e setters
	 */
	
	/**
	 * @param tabelaPrecoParcelaService The tabelaPrecoParcelaService to set.
	 */
	public void setTabelaPrecoParcelaService(
			TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}
}
