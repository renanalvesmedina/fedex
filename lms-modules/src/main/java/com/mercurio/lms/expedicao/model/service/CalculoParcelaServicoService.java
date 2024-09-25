package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.CalculoServico;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.ParcelaServicoAdicional;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.dao.CalculoParcelaNFServicoDAO;
import com.mercurio.lms.expedicao.model.dao.CalculoParcelaServicoDAO;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;

/**
 * @author Claiton Grings
 * @spring.property name="configuracoesFacade" ref="lms.configuracoesFacade"
 */
public abstract class CalculoParcelaServicoService {
	private CalculoParcelaServicoDAO calculoParcelaServicoDAO;
	private CalculoServicoService calculoServicoService;
	private ConfiguracoesFacade configuracoesFacade;
	private CalculoParcelaNFServicoDAO calculoParcelaNFServicoDAO;
	
	public CalculoParcelaNFServicoDAO getCalculoParcelaNFServicoDAO() {
		return calculoParcelaNFServicoDAO;
	}
	public void setCalculoParcelaNFServicoDAO(
			CalculoParcelaNFServicoDAO calculoParcelaNFServicoDAO) {
		this.calculoParcelaNFServicoDAO = calculoParcelaNFServicoDAO;
	}
	
	protected CalculoParcelaServicoDAO getCalculoParcelaServicoDAO() {
		return calculoParcelaServicoDAO;
	}
	protected void setCalculoParcelaServicoDAO(CalculoParcelaServicoDAO calculoParcelaServicoDAO) {
		this.calculoParcelaServicoDAO = calculoParcelaServicoDAO;
	}

	protected CalculoServicoService getCalculoServicoService() {
		return calculoServicoService;
	}
	protected void setCalculoServicoService(CalculoServicoService calculoServicoService) {
		this.calculoServicoService = calculoServicoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ParcelaPreco findParcelaPreco(Long idTabelaPreco, String cdParcelaPreco) {
		ParcelaPreco parcelaPreco = getCalculoParcelaServicoDAO().findParcelaPreco(idTabelaPreco, cdParcelaPreco);
		return parcelaPreco;
	}

	public ParcelaPreco findParcelaPreco(String cdParcelaPreco) {
		ParcelaPreco parcelaPreco = getCalculoParcelaServicoDAO().findParcelaPreco(cdParcelaPreco);
		return parcelaPreco;
	}

	public ServicoAdicionalCliente findServicoAdicionalCliente(Long idDivisaoCliente, String cdParcelaPreco) {
		return getCalculoParcelaNFServicoDAO().findServicoAdicionalCliente(idDivisaoCliente, cdParcelaPreco);
	}	
	
	/**
	 * Calcula valor dos Serviços Adicionais
	 * @param calculoServico
	 * @return
	 */
	public List findParcelasServicoAdicional(CalculoServico calculoServico) {
		if( (calculoServico.getServAdicionalDoctoServico() == null) || (calculoServico.getServAdicionalDoctoServico().size() == 0) ) {
			return null;
		}

		ServAdicionalDocServ servicoAdicionalDoctoServico = null;
		Long idServicoAdicional = null;
		TabelaPreco tabelaPreco = null;
		ParcelaPreco parcelaPreco = null;
		ParcelaServicoAdicional parcelaServicoAdicional = null;
		List result = new ArrayList();
		Iterator it = calculoServico.getServAdicionalDoctoServico().iterator();
		while(it.hasNext()) {
			servicoAdicionalDoctoServico = (ServAdicionalDocServ) it.next();
			idServicoAdicional = servicoAdicionalDoctoServico.getServicoAdicional().getIdServicoAdicional();
			tabelaPreco = calculoServico.getTabelaPreco();
			parcelaPreco = getCalculoParcelaServicoDAO().findParcelaPrecoServicoAdicional(tabelaPreco.getIdTabelaPreco(), idServicoAdicional,servicoAdicionalDoctoServico.getCdParcelaPreco());
			if(parcelaPreco == null) {
				String dsParcelaPreco = servicoAdicionalDoctoServico.getServicoAdicional().getDsServicoAdicional().getValue(SessionUtils.getUsuarioLogado().getLocale());
				throw new BusinessException("LMS-04120", new Object[]{dsParcelaPreco, tabelaPreco.getTabelaPrecoString()});
			}
			String cdParcelaPreco = parcelaPreco.getCdParcelaPreco();
			servicoAdicionalDoctoServico.setCdParcelaPreco(cdParcelaPreco);
			Long idParcelaPreco = parcelaPreco.getIdParcelaPreco();
			parcelaServicoAdicional = new ParcelaServicoAdicional(parcelaPreco);
			BigDecimal vlBrutoParcela = null;

			if(ConstantesExpedicao.CD_REEMBOLSO.equals(cdParcelaPreco)) {
				ValorFaixaProgressiva valorFaixaProgressiva = getCalculoParcelaServicoDAO().findValorFaixaProgressivaEnquadrada(tabelaPreco.getIdTabelaPreco(), idParcelaPreco, servicoAdicionalDoctoServico.getVlMercadoria(), null, null, null, null,false);
				if(valorFaixaProgressiva == null) {
					throw new BusinessException("LMS-30022", new Object[]{cdParcelaPreco});
				}
				String tpIndicadorCalculo = valorFaixaProgressiva.getFaixaProgressiva().getTpIndicadorCalculo().getValue();
				if("PC".equals(tpIndicadorCalculo)) {
					vlBrutoParcela = servicoAdicionalDoctoServico.getVlMercadoria().multiply(BigDecimalUtils.percent(valorFaixaProgressiva.getVlFixo()));
				} else if("VL".equals(tpIndicadorCalculo)) {
					vlBrutoParcela = valorFaixaProgressiva.getVlFixo();
				}
			} else {
				TabelaPreco tabelaPrecoMercurio = getCalculoServicoService().findTabelaPrecoMercurio(calculoServico);

				ValorServicoAdicional valorServicoAdicional = getCalculoParcelaServicoDAO().findValorServicoAdicional(tabelaPreco.getIdTabelaPreco(), idServicoAdicional,cdParcelaPreco);
				if(valorServicoAdicional == null) {
					valorServicoAdicional = getCalculoParcelaServicoDAO().findValorServicoAdicional(tabelaPrecoMercurio.getIdTabelaPreco(), idServicoAdicional,cdParcelaPreco);
				}
				BigDecimal vlUnitarioParcela = valorServicoAdicional.getVlServico();
				BigDecimal vlMinimo = BigDecimalUtils.ZERO;
				if((valorServicoAdicional.getVlMinimo() != null)) {
					vlMinimo = valorServicoAdicional.getVlMinimo();
				}
				parcelaServicoAdicional.addParametroAuxiliar("vlMinimo", vlMinimo);

				if(
						ConstantesExpedicao.CD_AGENDAMENTO_ENTREGA_SAB_DOM_FER.equals(cdParcelaPreco) ||
						ConstantesExpedicao.CD_AGENDAMENTO_COLETA.equals(cdParcelaPreco)
				) {
					vlBrutoParcela = vlUnitarioParcela.multiply(BigDecimalUtils.percent(servicoAdicionalDoctoServico.getVlFrete()));
					if(CompareUtils.lt(vlBrutoParcela, vlMinimo)) {
						vlBrutoParcela = vlMinimo;
					}
				} else if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcelaPreco)) {
					BigDecimal vlFator = calculoServico.getPsReferencia().add(BigDecimalUtils.getBigDecimal("99.999")).divide(BigDecimalUtils.getBigDecimal("100"), 0, BigDecimal.ROUND_DOWN);
					vlBrutoParcela = vlUnitarioParcela.multiply(vlFator);

					ValorServicoAdicional valorSeguroPermanencia = findValorServicoAdicional(tabelaPreco, ConstantesExpedicao.CD_SEGURO_CARGA_PERMANENCIA);
					vlBrutoParcela = vlBrutoParcela.add(valorSeguroPermanencia.getVlServico().multiply(BigDecimalUtils.percent(servicoAdicionalDoctoServico.getVlMercadoria())));
					if(CompareUtils.lt(vlBrutoParcela, vlMinimo)) {
						vlBrutoParcela = vlMinimo;
					}
				} else if(ConstantesExpedicao.CD_PALETIZACAO.equals(cdParcelaPreco)) {
					vlBrutoParcela = vlUnitarioParcela.multiply(BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getQtPaletes()));
				} else if(ConstantesExpedicao.CD_ESCOLTA.equals(cdParcelaPreco)) {
					BigDecimal vlSegurancaAdicional = BigDecimalUtils.ZERO;
					if(servicoAdicionalDoctoServico.getQtSegurancasAdicionais().intValue() > 0) {
						ValorServicoAdicional segurancaAdicional = findValorServicoAdicional(tabelaPreco, ConstantesExpedicao.CD_SEGURANCA_ADICIONAL);
						ParcelaServicoAdicional parcelaServicoAuxiliar = new ParcelaServicoAdicional(segurancaAdicional.getTabelaPrecoParcela().getParcelaPreco());
						parcelaServicoAuxiliar.setVlBrutoParcela(segurancaAdicional.getVlServico());
						parcelaServicoAuxiliar.addParametroAuxiliar("vlMinimo", segurancaAdicional.getVlMinimo());
						calculoServico.addServicoAdicionalAuxiliar(parcelaServicoAuxiliar);
						vlSegurancaAdicional = segurancaAdicional.getVlServico().multiply(BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getQtSegurancasAdicionais()));
					}

					vlBrutoParcela = vlUnitarioParcela.add(vlSegurancaAdicional).multiply(BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getNrKmRodado()));
				} else if(ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO.equals(cdParcelaPreco)) {
					vlBrutoParcela = BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getQtColetas()).multiply(vlUnitarioParcela);
				} else if(ConstantesExpedicao.TIPOS_ESTADIA.contains(cdParcelaPreco)) {
					vlBrutoParcela = BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getQtDias()).multiply(vlUnitarioParcela);
				}
				parcelaServicoAdicional.setVlUnitarioParcela(vlUnitarioParcela);
			}
			parcelaServicoAdicional.setVlBrutoParcela(vlBrutoParcela);
			parcelaServicoAdicional.setServAdicionalDocServ(servicoAdicionalDoctoServico);
			result.add(findParametroServicoAdicional(calculoServico, parcelaServicoAdicional));
		}
		return result;
	}

	/**
	 * Aplica parametrização dos Serviços Adicionais
	 * @param calculoServico
	 * @param parcelaServico
	 * @return
	 */
	protected ParcelaServico findParametroServicoAdicional(CalculoServico calculoServico, ParcelaServicoAdicional parcelaServico) {
		ParametroCliente parametroCliente = calculoServico.getParametroCliente();
		if(parametroCliente != null) {
			//seta valor padrao para nao precisar testar indicador "T"
			BigDecimal vlParcela = parcelaServico.getVlBrutoParcela();
			String tpIndicador = "";
			String cdParcelaPreco = parcelaServico.getParcelaPreco().getCdParcelaPreco();
			ServAdicionalDocServ servicoAdicionalDoctoServico = parcelaServico.getServAdicionalDocServ();

			ServicoAdicionalCliente servicoAdicionalCliente = findServicoAdicionalCliente(calculoServico, parcelaServico);

			if(ConstantesExpedicao.CD_ESCOLTA.equals(cdParcelaPreco)) {
				//Calcula valor de Seguranca Adicional
				BigDecimal vlSegurancaAdicional = BigDecimalUtils.ZERO;
				if(servicoAdicionalDoctoServico.getQtSegurancasAdicionais().intValue() > 0) {
					ParcelaServico parcelaSegurancaAdicional = calculoServico.getServicoAdicionalAuxiliar(ConstantesExpedicao.CD_SEGURANCA_ADICIONAL);
					vlSegurancaAdicional = parcelaSegurancaAdicional.getVlBrutoParcela();

					ServicoAdicionalCliente segurancaAdicional = findServicoAdicionalCliente(calculoServico, parcelaSegurancaAdicional);
					if(segurancaAdicional != null) {
						String tpIndicadorSegurancaAdicional = segurancaAdicional.getTpIndicador().getValue();
						if("T".equals(tpIndicadorSegurancaAdicional)) {
							vlSegurancaAdicional = parcelaSegurancaAdicional.getVlBrutoParcela();
						} else if("V".equals(tpIndicadorSegurancaAdicional)) {
							vlSegurancaAdicional = segurancaAdicional.getVlValor();
						} else if("D".equals(tpIndicadorSegurancaAdicional)) {
							vlSegurancaAdicional = BigDecimalUtils.desconto(parcelaSegurancaAdicional.getVlBrutoParcela(), segurancaAdicional.getVlValor());
						} else if("A".equals(tpIndicadorSegurancaAdicional)) {
							vlSegurancaAdicional = BigDecimalUtils.acrescimo(parcelaSegurancaAdicional.getVlBrutoParcela(), segurancaAdicional.getVlValor());
						}
					}
					vlSegurancaAdicional = vlSegurancaAdicional.multiply(BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getQtSegurancasAdicionais()));
				}
				BigDecimal nrKmRodado = BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getNrKmRodado());
				//Verifica se a parcela de Escolta esta parametrizada
				if(servicoAdicionalCliente == null) {
					vlParcela = parcelaServico.getVlUnitarioParcela().add(vlSegurancaAdicional).multiply(nrKmRodado);
					parcelaServico.getVlUnitarioParcela().add(vlSegurancaAdicional).multiply(nrKmRodado);
					BigDecimal vlMinimo = parcelaServico.getParametroAuxiliar("vlMinimo");
					if(CompareUtils.lt(vlParcela, vlMinimo)) {
						vlParcela = vlMinimo;
					}
				} else {
					tpIndicador = servicoAdicionalCliente.getTpIndicador().getValue();
					if("V".equals(tpIndicador)) {
						vlParcela = servicoAdicionalCliente.getVlValor().add(vlSegurancaAdicional).multiply(nrKmRodado);
					} else if("D".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.desconto(parcelaServico.getVlUnitarioParcela(), servicoAdicionalCliente.getVlValor()).add(vlSegurancaAdicional).multiply(nrKmRodado);
					} else if("A".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.acrescimo(parcelaServico.getVlUnitarioParcela(), servicoAdicionalCliente.getVlValor()).add(vlSegurancaAdicional).multiply(nrKmRodado);
					}
				}
			} else {
				if(servicoAdicionalCliente == null) {
					parcelaServico.setVlParcela(null);
					return parcelaServico;
				}
				tpIndicador = servicoAdicionalCliente.getTpIndicador().getValue();
				if("V".equals(tpIndicador)) {
					vlParcela = servicoAdicionalCliente.getVlValor();
				} else if("D".equals(tpIndicador)) {
					vlParcela = BigDecimalUtils.desconto(parcelaServico.getVlBrutoParcela(), servicoAdicionalCliente.getVlValor());
				} else if("A".equals(tpIndicador)) {
					vlParcela = BigDecimalUtils.acrescimo(parcelaServico.getVlBrutoParcela(), servicoAdicionalCliente.getVlValor());
				}
				BigDecimal vlMinimo = BigDecimal.ZERO;
				if(BigDecimalUtils.hasValue(servicoAdicionalCliente.getVlMinimo())) {
					vlMinimo = servicoAdicionalCliente.getVlMinimo();
				}

				if(
						ConstantesExpedicao.CD_AGENDAMENTO_ENTREGA_SAB_DOM_FER.equals(cdParcelaPreco) ||
						ConstantesExpedicao.CD_AGENDAMENTO_COLETA.equals(cdParcelaPreco)
				) {
					vlParcela = vlParcela.multiply(BigDecimalUtils.percent(servicoAdicionalDoctoServico.getVlFrete()));
					if(CompareUtils.lt(vlParcela, vlMinimo)) {
						vlParcela = vlMinimo;
					}
				} else if(ConstantesExpedicao.CD_PALETIZACAO.equals(cdParcelaPreco)) {
					vlParcela = vlParcela.multiply(BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getQtPaletes()));
				} else if(ConstantesExpedicao.CD_REEMBOLSO.equals(cdParcelaPreco)) {
					if("V".equals(tpIndicador)) {
						vlParcela = servicoAdicionalDoctoServico.getVlMercadoria().multiply(BigDecimalUtils.percent(servicoAdicionalCliente.getVlValor()));
					} else if("D".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.desconto(parcelaServico.getVlBrutoParcela(), servicoAdicionalCliente.getVlValor());
					} else if("A".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.acrescimo(parcelaServico.getVlBrutoParcela(), servicoAdicionalCliente.getVlValor());
					}
					if(CompareUtils.lt(vlParcela, vlMinimo)) {
						vlParcela = vlMinimo;
					}
				} else if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcelaPreco)) {
					BigDecimal vlFator = calculoServico.getPsReferencia().add(BigDecimalUtils.getBigDecimal("99.999")).divide(BigDecimalUtils.getBigDecimal("100"), 0, BigDecimal.ROUND_DOWN);
					vlParcela = vlParcela.multiply(vlFator);

					ServicoAdicionalCliente servicoSeguroPermanencia = getCalculoParcelaServicoDAO().findServicoAdicionalCliente(calculoServico.getIdDivisaoCliente(), ConstantesExpedicao.CD_SEGURO_CARGA_PERMANENCIA);
					vlParcela = vlParcela.add(servicoSeguroPermanencia.getVlValor().multiply(BigDecimalUtils.percent(servicoAdicionalDoctoServico.getVlMercadoria())));
				} else if(ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO.equals(cdParcelaPreco)) {
					vlParcela = BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getQtColetas()).multiply(vlParcela);
				} else if(ConstantesExpedicao.TIPOS_ESTADIA.contains(cdParcelaPreco)) {
					vlParcela = BigDecimalUtils.getBigDecimal(servicoAdicionalDoctoServico.getQtDias()).multiply(vlParcela);
				}
			}
			parcelaServico.setVlParcela(vlParcela);
		}
		return parcelaServico;
	}

	private ValorServicoAdicional findValorServicoAdicional(TabelaPreco tabelaPreco, String cdParcelaPreco) {
		ValorServicoAdicional valorServicoAdicional = getCalculoParcelaServicoDAO().findValorServicoAdicional(tabelaPreco.getIdTabelaPreco(), cdParcelaPreco);
		if(valorServicoAdicional == null) {
			throw new BusinessException("LMS-04120", new Object[]{cdParcelaPreco, tabelaPreco.getTabelaPrecoString()});
		}
		return valorServicoAdicional;
	}

	private ServicoAdicionalCliente findServicoAdicionalCliente(CalculoServico calculoServico, ParcelaServico parcelaServico) {
		ServicoAdicionalCliente servicoAdicionalCliente = null;
		if(Boolean.TRUE.equals(calculoServico.getBlRecalculoCotacao())) {
			servicoAdicionalCliente = (ServicoAdicionalCliente) parcelaServico.getParametro();
		} else {
			servicoAdicionalCliente = getCalculoParcelaServicoDAO().findServicoAdicionalCliente(calculoServico.getIdDivisaoCliente(), parcelaServico.getParcelaPreco().getIdParcelaPreco());
			parcelaServico.setParametro(servicoAdicionalCliente);
		}
		return servicoAdicionalCliente;
	}
	
	protected Generalidade findGeneralidade(Long idTabelaPreco, String cdParcelaPreco) {
		Generalidade generalidade = getCalculoParcelaServicoDAO().findGeneralidade(idTabelaPreco, cdParcelaPreco);
		if(generalidade == null) {
			throw new BusinessException("LMS-30022", new Object[]{cdParcelaPreco});
		}
		return generalidade;
	}

	protected GeneralidadeCliente findGeneralidadeCliente(CalculoServico calculoServico, ParcelaServico parcelaServico) {
		GeneralidadeCliente generalidadeCliente = null;
		if(Boolean.TRUE.equals(calculoServico.getBlRecalculoCotacao()) ) {
			generalidadeCliente = (GeneralidadeCliente) parcelaServico.getParametro();
		} 
		if( generalidadeCliente == null ){
			if(Boolean.TRUE.equals(calculoServico.getBlEditaCotacao() ) ) {
				ParametroCliente pc = calculoServico.getParametroCliente();
				generalidadeCliente = getCalculoParcelaServicoDAO().findGeneralidadeClienteByParametro(pc.getIdParametroCliente(),parcelaServico.getParcelaPreco().getIdParcelaPreco());
			}else if( (ConstantesExpedicao.CALCULO_COTACAO.equals(calculoServico.getTpCalculo())) && (calculoServico.getIdCotacao() != null) ) {
				ParametroCliente pc = calculoServico.getParametroCliente();
				generalidadeCliente = getCalculoParcelaServicoDAO().findGeneralidadeClienteByParametro(pc.getIdParametroCliente(),parcelaServico.getParcelaPreco().getIdParcelaPreco());
			}else{
				/*Ajuste relacionado ao quest 24807 , esta alterção foi solicitada
				pelo analista. Não deve ser filtrado o idtabelaPreco ao obter a generalidade*/
				generalidadeCliente = getCalculoParcelaServicoDAO().findGeneralidadeCliente(null, parcelaServico.getParcelaPreco().getIdParcelaPreco(), calculoServico.getIdDivisaoCliente(), calculoServico.getIdServico(), calculoServico.getRestricaoRotaOrigem(), calculoServico.getRestricaoRotaDestino());
			}
			parcelaServico.setParametro(generalidadeCliente);
		}
		return generalidadeCliente;
	}

	public void finalizaCalculoParcelas(CalculoServico calculoServico) {
		List parcelas = calculoServico.getParcelas();
		ParcelaServico parcelaServico = null;
		Iterator it = parcelas.iterator();
		while(it.hasNext()) {
			parcelaServico = (ParcelaServico) it.next();
			if(!BigDecimalUtils.hasValue(parcelaServico.getVlParcela())) {
				it.remove();
			}
		}

		CalculoFreteUtils.ordenaParcelas(parcelas);

		if(BigDecimalUtils.hasValue(calculoServico.getVlTotal())) {
			calculoServico.setVlTotalServicosAdicionais(BigDecimalUtils.round(calculoServico.getVlTotalServicosAdicionais()));
			calculoServico.setVlTotalParcelas(BigDecimalUtils.round(calculoServico.getVlTotalParcelas()));
			calculoServico.setVlDesconto(BigDecimalUtils.round(calculoServico.getVlDesconto()));
			calculoServico.setVlTotal(BigDecimalUtils.round(calculoServico.getVlTotal()));
		} else {
			calculoServico.setVlTotalServicosAdicionais(BigDecimalUtils.ZERO);
			calculoServico.setVlTotalParcelas(BigDecimalUtils.ZERO);
			calculoServico.setVlTotal(BigDecimalUtils.ZERO);
		}
	}

}
