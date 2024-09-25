package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.calculoSimulacaoNovosPrecosService"
 */
public class CalculoSimulacaoNovosPrecosService {

	public List<TabelaPrecoParcela> executeReajusteGeneralidade(List<TabelaPrecoParcela> tabelaPrecoParcelas, BigDecimal pcReajuste, List<TabelaPrecoParcela> excecoes) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		if (tabelaPrecoParcelas != null) {
			for (TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();

				// verifica se essa generalidade foi reajustada nas execoes
				if (containsExcecao(parcelaPreco.getIdParcelaPreco(), excecoes)) {
					// em caso positivo continua para a proxima generalidade,
					// ja que as reajustadas serao gravadas diretamente
					continue;
				}
				Generalidade generalidade = tabelaPrecoParcela.getGeneralidade();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);
				Generalidade g = new Generalidade();

				if (!"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue()) 
						&& !"PF".equals(parcelaPreco.getTpIndicadorCalculo().getValue())){
					BigDecimal vlGeneralidade = calculaReajuste(generalidade.getVlGeneralidade(), pcReajuste);
					g.setVlGeneralidade(vlGeneralidade);
				} else {
					g.setVlGeneralidade(generalidade.getVlGeneralidade());
				}

				BigDecimal vlMinimo = calculaReajuste(generalidade.getVlMinimo(), pcReajuste);
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

	public List<TabelaPrecoParcela> executeReajusteValorTaxa(List<TabelaPrecoParcela> tabelaPrecoParcelas, BigDecimal pcReajuste, List<TabelaPrecoParcela> excecoes) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();
		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();

				// verifica se essa generalidade foi reajustada nas execoes
				if (containsExcecao(parcelaPreco.getIdParcelaPreco(), excecoes)) {
					// em caso positivo continua para a proxima generalidade,
					// ja que as reajustadas serao gravadas diretamente
					continue;
				}
				ValorTaxa valorTaxa = tabelaPrecoParcela.getValorTaxa();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);
				ValorTaxa vt = new ValorTaxa();

				if (!"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue())) {
					BigDecimal vlTaxa = calculaReajuste(valorTaxa.getVlTaxa(), pcReajuste);		
					vt.setVlTaxa(vlTaxa);
				} else {
					vt.setVlTaxa(valorTaxa.getVlTaxa());
				}

				BigDecimal vlExcedente = calculaReajuste(valorTaxa.getVlExcedente(), pcReajuste);
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

	public List<TabelaPrecoParcela> executeReajusteServicoAdicional(List<TabelaPrecoParcela> tabelaPrecoParcelas, BigDecimal pcReajuste) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();
		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				ValorServicoAdicional valorServicoAdicional = tabelaPrecoParcela.getValorServicoAdicional();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);
				ValorServicoAdicional vsa = new ValorServicoAdicional();

				if (!"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue())) {
					BigDecimal vlServico = calculaReajuste(valorServicoAdicional.getVlServico(), pcReajuste);

					vsa.setVlServico(vlServico);
				} else {
					vsa.setVlServico(valorServicoAdicional.getVlServico());
				}

				BigDecimal vlMinimo = calculaReajuste(valorServicoAdicional.getVlMinimo(), pcReajuste);
				vsa.setVlMinimo(vlMinimo);

				vsa.setTabelaPrecoParcela(tpp);
				tpp.setValorServicoAdicional(vsa);
				result.add(tpp);
			}
		}
		return result;
	}

	public List<TabelaPrecoParcela> executeReajustePrecoFrete(List<TabelaPrecoParcela> tabelaPrecoParcelas, BigDecimal pcReajuste) {
		if (tabelaPrecoParcelas != null && tabelaPrecoParcelas.size() > 0) {
			List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				List<PrecoFrete> precoFretes = tabelaPrecoParcela.getPrecoFretes();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);
				List<PrecoFrete> pfs = new ArrayList<PrecoFrete>();
				if (precoFretes != null) {
					for(PrecoFrete precoFrete : precoFretes) {
						PrecoFrete pf = new PrecoFrete();
						if(!"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue())) {
							BigDecimal vlPrecoFrete = calculaReajuste(precoFrete.getVlPrecoFrete(), pcReajuste);
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
			return result;
		}
		return null;
	}

	public List<TabelaPrecoParcela> executeReajusteMinimoProgressivo(List<TabelaPrecoParcela> tabelaPrecoParcelas, BigDecimal pcReajuste) {
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tabelaPrecoParcela : tabelaPrecoParcelas) {
				ParcelaPreco parcelaPreco = tabelaPrecoParcela.getParcelaPreco();
				List<FaixaProgressiva> faixasProgressivas = tabelaPrecoParcela.getFaixaProgressivas();

				TabelaPrecoParcela tpp = copyTabelaPrecoParcela(tabelaPrecoParcela);

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
								if(valorFaixaProgressiva.getVlFixo() != null
									&& CompareUtils.ne(valorFaixaProgressiva.getVlFixo(), BigDecimalUtils.ZERO)
									&& !Boolean.TRUE.equals(valorFaixaProgressiva.getBlPromocional())
									&& !"PC".equals(parcelaPreco.getTpIndicadorCalculo().getValue())
									&& !"PC".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())
								) {
									BigDecimal vlFixo = calculaReajuste(valorFaixaProgressiva.getVlFixo(), pcReajuste);
									valorFaixaProgressivaNew.setVlFixo(vlFixo);
								} else {
									valorFaixaProgressivaNew.setVlFixo(valorFaixaProgressiva.getVlFixo());
								}
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

	public BigDecimal calculaReajuste(BigDecimal valor, BigDecimal pcReajuste) {
		if (valor != null && pcReajuste != null) {
			BigDecimal percent = BigDecimalUtils.percent(pcReajuste);
			BigDecimal result = valor.multiply(percent);
			result = BigDecimalUtils.add(result, valor);
			return result;
		}
		return null;
	}

	public boolean containsExcecao(Long idParcelaPreco, List<TabelaPrecoParcela> excecoes) {
		if (excecoes != null) {
			for(TabelaPrecoParcela tpp : excecoes) {
				if (idParcelaPreco.equals(tpp.getParcelaPreco().getIdParcelaPreco())) {
					return true;
				}
			}
		}
		return false;
	}

	public TabelaPrecoParcela copyTabelaPrecoParcela(TabelaPrecoParcela original) {
		TabelaPrecoParcela result = new TabelaPrecoParcela();
		result.setParcelaPreco(original.getParcelaPreco());
		return result;
	}

}