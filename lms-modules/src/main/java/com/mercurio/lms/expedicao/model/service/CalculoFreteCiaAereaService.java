package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.CalculoFreteCiaAerea;
import com.mercurio.lms.expedicao.model.CalculoServico;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.dao.CalculoFreteCiaAereaDAO;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaSpot;
import com.mercurio.lms.tabelaprecos.model.service.TarifaSpotService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.calculoFreteCiaAereaService" autowire="no"
 * @spring.property name="calculoFreteCiaAereaDAO" ref="lms.expedicao.calculoFreteCiaAereaDAO"
 * @spring.property name="calculoParcelaFreteCiaAereaService" ref="lms.expedicao.calculoParcelaFreteCiaAereaService"
 * @spring.property name="calculoTributoService" ref="lms.expedicao.calculoTributoService"
 * @spring.property name="tarifaSpotService" ref="lms.tabelaprecos.tarifaSpotService"
 * @spring.property name="configuracoesFacade" ref="lms.configuracoesFacade"
 * @spring.property name="parametroGeralService" ref="lms.configuracoes.parametroGeralService" 
 */
public class CalculoFreteCiaAereaService extends CalculoServicoService {
	private TarifaSpotService tarifaSpotService;
	private ConfiguracoesFacade configuracoesFacade;
	private ParametroGeralService parametroGeralService;	

	@Override
	protected void findPesoReferencia(CalculoServico calculoServico) {
		CalculoFreteCiaAerea calculoFreteCiaAerea = (CalculoFreteCiaAerea) calculoServico;

		BigDecimal psRealCalculado = BigDecimalUtils.ZERO;
		BigDecimal psCubadoCalculado = BigDecimalUtils.ZERO;

		//normaliza o peso real
		BigDecimal psInteiro = BigDecimalUtils.round(calculoFreteCiaAerea.getPsRealInformado(), 0, BigDecimalUtils.ROUND_DOWN);
		BigDecimal psDiff = calculoFreteCiaAerea.getPsRealInformado().subtract(psInteiro);
		if(CompareUtils.eq(psDiff, BigDecimalUtils.ZERO)) {
			psRealCalculado = calculoFreteCiaAerea.getPsRealInformado();
		} else if(CompareUtils.lt(psDiff, new BigDecimal("0.5"))) {
			psRealCalculado = psInteiro.add(new BigDecimal("0.5"));
		} else {
			psRealCalculado = psInteiro.add(BigDecimalUtils.ONE);
		}

		
		//normaliza o peso cubado
		BigDecimal psCubadoInformado = calculoFreteCiaAerea.getPsCubadoInformado();
		if(psCubadoInformado == null){
			psCubadoInformado = calculoFreteCiaAerea.getPsRealInformado();
		}

		psInteiro = BigDecimalUtils.round(psCubadoInformado, 0, BigDecimalUtils.ROUND_DOWN);
		psDiff = psCubadoInformado.subtract(psInteiro);
		if(CompareUtils.eq(psDiff, BigDecimalUtils.ZERO)) {
			psCubadoCalculado = psCubadoInformado;
		} else if(CompareUtils.lt(psDiff, new BigDecimal("0.5"))) {
			psCubadoCalculado = psInteiro.add(new BigDecimal("0.5"));
		} else {
			psCubadoCalculado = psInteiro.add(BigDecimalUtils.ONE);
		}

		calculoFreteCiaAerea.setPsReferencia(CompareUtils.max(psRealCalculado, psCubadoCalculado));
	}

	@Override
	protected void findTabelaPreco(CalculoServico calculoServico) throws BusinessException {
		CalculoFreteCiaAerea calculoFreteCiaAerea = (CalculoFreteCiaAerea) calculoServico;

		TabelaPreco tabelaPreco = getCalculoFreteCiaAereaDAO().findTabelaPreco(calculoFreteCiaAerea.getIdCiaAerea(), 
																			   calculoFreteCiaAerea.getIdExpedidor(),
																		   	   calculoFreteCiaAerea.getDtEmissaoAwb(),
																		   	   calculoFreteCiaAerea.getTpServicoAwb());

		if(tabelaPreco == null) {
			tabelaPreco = getCalculoFreteCiaAereaDAO().findTabelaPreco(calculoFreteCiaAerea.getIdCiaAerea(), null,
																		calculoFreteCiaAerea.getDtEmissaoAwb(),
																	   	   calculoFreteCiaAerea.getTpServicoAwb());
		}
		if(tabelaPreco == null) {
			throw new BusinessException("tabelaPrecoNaoEncontrada");
		}
		calculoFreteCiaAerea.setTabelaPreco(tabelaPreco);
	}

	@Override
	protected void findParametroCliente(CalculoServico calculoServico) {
		// nao aplicavel
	}
	
	public List executeCalculosFreteAWB(Awb awb) {
		return this.executeCalculosFreteAWB(awb, null);
	}

	public List executeCalculosFreteAWB(Awb awb, Long idCiaAerea) {
		List calculosFrete = null;

		Long idEmpresaTarifaSpot = null;
		TarifaSpot tarifaSpot = awb.getTarifaSpot();
		if(tarifaSpot != null) {
			tarifaSpot = tarifaSpotService.findTarifaSpotById(tarifaSpot.getIdTarifaSpot());
			if(tarifaSpot.getNrUtilizacoes() != null) {
				if(tarifaSpot.getNrUtilizacoes().intValue() >= tarifaSpot.getNrPossibilidades().intValue()) {
					throw new BusinessException("LMS-30011");
				}
			}
			idEmpresaTarifaSpot = tarifaSpot.getEmpresa().getIdEmpresa();
		}

		/** Busca e valida se existem rotas */
		RestricaoRota rotaOrigem = findRestricaoRotaByAeroporto(awb.getAeroportoByIdAeroportoOrigem());
		if(rotaOrigem == null) {
			throw new BusinessException("LMS-04015", new Object[]{configuracoesFacade.getMensagem("aeroportoDeOrigem")});
		}
		RestricaoRota rotaDestino = findRestricaoRotaByAeroporto(awb.getAeroportoByIdAeroportoDestino());
		if(rotaDestino == null) {
			throw new BusinessException("LMS-04015", new Object[]{configuracoesFacade.getMensagem("aeroportoDeDestino")});
		}

		List tabelas = findCiasAereasRota(rotaOrigem, rotaDestino, idCiaAerea, awb.getTpCaracteristicaServico());
		if(tabelas != null && !tabelas.isEmpty()) {
			calculosFrete = new ArrayList<CalculoFreteCiaAerea>();
			TabelaPreco tabelaPreco = null;
			CalculoFreteCiaAerea calculoFreteCiaAerea = null;
			for (Iterator iter = tabelas.iterator(); iter.hasNext();) {
				tabelaPreco = (TabelaPreco) iter.next();

				calculoFreteCiaAerea = createCalculo(awb, tabelaPreco, Boolean.FALSE);
				calculosFrete.add(calculoFreteCiaAerea);
				/** Verifica se deve calcular Tarifa SPOT */ 
				if(tabelaPreco.getTipoTabelaPreco().getCliente() == null &&
						calculoFreteCiaAerea.getIdCiaAerea().equals(idEmpresaTarifaSpot)) {
					calculoFreteCiaAerea = createCalculo(awb, tabelaPreco, Boolean.TRUE);
					calculoFreteCiaAerea.setTarifaSpot(tarifaSpot);
					calculosFrete.add(calculoFreteCiaAerea);
				}
			}
			executeCalculosFreteAWB(calculosFrete);
		}

		return calculosFrete;
	}

	private CalculoFreteCiaAerea createCalculo(Awb awb, TabelaPreco tabelaPreco, Boolean blTarifaSpot) {
		CalculoFreteCiaAerea calculoFreteCiaAerea = new CalculoFreteCiaAerea();

		calculoFreteCiaAerea.setBlCalculaParcelas(Boolean.TRUE);

		calculoFreteCiaAerea.setIdExpedidor(awb.getClienteByIdClienteExpedidor().getIdCliente());
		calculoFreteCiaAerea.setPsRealInformado(awb.getPsTotal());
		calculoFreteCiaAerea.setPsCubadoInformado(awb.getPsCubado());
		calculoFreteCiaAerea.setTpFrete(awb.getTpFrete().getValue());
		calculoFreteCiaAerea.setIdAeroportoOrigem(awb.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		calculoFreteCiaAerea.setIdAeroportoDestino(awb.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		if(awb.getProdutoEspecifico() != null) {
			calculoFreteCiaAerea.setIdProdutoEspecifico(awb.getProdutoEspecifico().getIdProdutoEspecifico());
		}
		calculoFreteCiaAerea.setTabelaPreco(tabelaPreco);
		calculoFreteCiaAerea.setIdCiaAerea(tabelaPreco.getTipoTabelaPreco().getEmpresaByIdEmpresaCadastrada().getPessoa().getIdPessoa());
		calculoFreteCiaAerea.setBlTarifaSpot(blTarifaSpot);
		
		
		if(awb.getDhEmissao() != null){
			calculoFreteCiaAerea.setDtEmissaoAwb(new YearMonthDay(awb.getDhEmissao().getYear(), 
															  		awb.getDhEmissao().getMonthOfYear(), 
															  		awb.getDhEmissao().getDayOfMonth()));
		}
		calculoFreteCiaAerea.setTpServicoAwb(awb.getTpCaracteristicaServico());
		
		setDadosClientes(awb, calculoFreteCiaAerea);

		return calculoFreteCiaAerea;
	}

	private void executeCalculosFreteAWB(List<CalculoFreteCiaAerea> calculosFreteCiaAerea) {
		for (CalculoFreteCiaAerea calculoFreteCiaAerea : calculosFreteCiaAerea) {
			this.executeCalculosFreteAWB(calculoFreteCiaAerea);
		}
	}

	private void executeCalculosFreteAWB(CalculoFreteCiaAerea calculoFreteCiaAerea) {
		findPesoReferencia(calculoFreteCiaAerea);
		if(calculoFreteCiaAerea.getTabelaPreco() == null) {
			findTabelaPreco(calculoFreteCiaAerea);
		}

		ParcelaServico parcelaServico_FretePeso = getCalculoParcelaFreteCiaAereaService().findParcelaFretePeso(calculoFreteCiaAerea);

		ParcelaServico parcelaServico_TaxaTerrestre = getCalculoParcelaFreteCiaAereaService().findParcelaTaxaTerrestre(calculoFreteCiaAerea);

		ParcelaServico parcelaServico_TaxaCombustivel = getCalculoParcelaFreteCiaAereaService().findParcelaTaxaCombustivel(calculoFreteCiaAerea);

		//verifica se possui Taxa de Combustivel
		if(parcelaServico_TaxaCombustivel != null){

			//Se a companhia for TAM
			//subtrair o valor obtido como "taxa de combustível" (vlBrutoParcela)
			//do valor obtido para Valor Frete Peso quando o Valor do Frete peso for maior que o Valor da Tarifa Mínima.
			//Quando o Valor do Frete Peso for igual ao Valor da Tarifa Mínima, o Valor Taxa Combustível será igual a zero 
			
			ParametroGeral pg = parametroGeralService.findByNomeParametro("ID_TAM", false); 	// É TAM?
			if (calculoFreteCiaAerea.getIdCiaAerea() == Long.parseLong(pg.getDsConteudo())) {					
				BigDecimal txCombustivel = parcelaServico_TaxaCombustivel.getVlBrutoParcela();
				BigDecimal vlFretePeso = parcelaServico_FretePeso.getVlBrutoParcela();
				BigDecimal vlTarifaMinima = getCalculoParcelaFreteCiaAereaService().findValorFaixaFretePeso_TarifaMinima(calculoFreteCiaAerea); // Valor da Tarifa Mínima
				
				if (vlFretePeso.compareTo(vlTarifaMinima) > 0) { 					
					vlFretePeso = vlFretePeso.subtract(txCombustivel);
					parcelaServico_FretePeso.setVlBrutoParcela(vlFretePeso);
				} else if (vlFretePeso.compareTo(vlTarifaMinima) <= 0) {
					parcelaServico_TaxaCombustivel.setVlBrutoParcela(new BigDecimal(0));
				}
			}
			
		}
		calculoFreteCiaAerea.addParcelaGeral(parcelaServico_FretePeso);
		calculoFreteCiaAerea.addParcelaGeral(parcelaServico_TaxaTerrestre);
		calculoFreteCiaAerea.addParcelaGeral(parcelaServico_TaxaCombustivel);
		
		//Calcula valor das parcelas
		calculoFreteCiaAerea.setVlTotalParcelas();

		//Calcula Impostos
		getCalculoTributoService().calculaTributos(calculoFreteCiaAerea);

		//Calcula Totais
		calculoFreteCiaAerea.setVlTotal(calculoFreteCiaAerea.getVlTotalParcelas());
	}

	/*
	 * Se a companhia for TAM
	 * subtrair o valor obtido como "taxa de combustível" (vlBrutoParcela)
	 * do valor obtido para Valor Frete Peso quando o Valor do Frete peso for maior que o Valor da Tarifa Mínima.
	 * Quando o Valor do Frete Peso for igual ao Valor da Tarifa Mínima, o Valor Taxa Combustível será igual a zero 
	 * 
	 * Após alteração do LMS-247 aparentemente o método não está mais sendo usado
	 * 
	 */
	
	private void subtraiTaxaCombustivel_TAM(ParcelaServico pTaxaCombustivel, ParcelaServico pFretePeso,
											CalculoFreteCiaAerea calculoFreteCiaAerea) {

		ParametroGeral pg = parametroGeralService.findByNomeParametro("ID_TAM", false); 	// É TAM?
		if (calculoFreteCiaAerea.getIdCiaAerea() == Long.parseLong(pg.getDsConteudo())) {					
		
			BigDecimal txCombustivel = pTaxaCombustivel.getVlBrutoParcela();
			BigDecimal vlFretePeso = pFretePeso.getVlBrutoParcela();
			BigDecimal vlTarifaMinima = getCalculoParcelaFreteCiaAereaService().findValorFaixaFretePeso_TarifaMinima(calculoFreteCiaAerea); // Valor da Tarifa Mínima
			
			if (vlFretePeso.compareTo(vlTarifaMinima) > 0) { 					
				vlFretePeso = vlFretePeso.subtract(txCombustivel);
				pFretePeso.setVlBrutoParcela(vlFretePeso);
			} else if (vlFretePeso.compareTo(vlTarifaMinima) <= 0) {
				pTaxaCombustivel.setVlBrutoParcela(new BigDecimal(0));
			}
		}
	}
	
	private void setDadosClientes(Awb awb, CalculoFreteCiaAerea calculoFreteCiaAerea) {
		DoctoServicoDadosCliente dadosCliente = calculoFreteCiaAerea.getDadosCliente();

		EnderecoPessoa enderecoPessoa = awb.getClienteByIdClienteExpedidor().getPessoa().getEnderecoPessoa();
		dadosCliente.setIdInscricaoEstadualRemetente(awb.getInscricaoEstadualExpedidor().getIdInscricaoEstadual());
		RestricaoRota restricaoRota = calculoFreteCiaAerea.getRestricaoRotaOrigem();
		restricaoRota.setIdUnidadeFederativa(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());

		enderecoPessoa = awb.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa();
		if (awb.getInscricaoEstadualDestinatario() != null) {
			dadosCliente.setIdInscricaoEstadualDestinatario(awb.getInscricaoEstadualDestinatario().getIdInscricaoEstadual());
		}
		restricaoRota = calculoFreteCiaAerea.getRestricaoRotaDestino();
		restricaoRota.setIdUnidadeFederativa(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
	}

	private List findCiasAereasRota(RestricaoRota origem, RestricaoRota destino, Long idCiaAerea, String tpCaracteristicaServico) {
		return getCalculoFreteCiaAereaDAO().findCiasAereasRota(origem, destino, idCiaAerea, tpCaracteristicaServico);
	}

	private RestricaoRota findRestricaoRotaByAeroporto(Aeroporto aeroporto) {
		return getCalculoFreteCiaAereaDAO().findRestricaoRotaByAeroporto(aeroporto.getIdAeroporto());
	}

	public CalculoFreteCiaAereaDAO getCalculoFreteCiaAereaDAO() {
		return (CalculoFreteCiaAereaDAO) super.getCalculoServicoDAO();
	}
	public void setCalculoFreteCiaAereaDAO(CalculoFreteCiaAereaDAO calculoFreteCiaAereaDAO) {
		super.setCalculoServicoDAO(calculoFreteCiaAereaDAO);
	}

	public CalculoParcelaFreteCiaAereaService getCalculoParcelaFreteCiaAereaService() {
		return (CalculoParcelaFreteCiaAereaService) super.getCalculoParcelaServicoService();
	}
	public void setCalculoParcelaFreteCiaAereaService(CalculoParcelaFreteCiaAereaService calculoParcelaFreteCiaAereaService) {
		super.setCalculoParcelaServicoService(calculoParcelaFreteCiaAereaService);
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setTarifaSpotService(TarifaSpotService tarifaSpotService) {
		this.tarifaSpotService = tarifaSpotService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
