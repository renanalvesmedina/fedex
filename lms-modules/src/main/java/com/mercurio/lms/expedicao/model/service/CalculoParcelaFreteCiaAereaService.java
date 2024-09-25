package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.expedicao.model.CalculoFreteCiaAerea;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.dao.CalculoParcelaFreteCiaAereaDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.FilialCiaAerea;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaSpot;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.FaixaProgressivaService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.calculoParcelaFreteCiaAereaService" autowire="no"
 * @spring.property name="calculoParcelaFreteCiaAereaDAO" ref="lms.expedicao.calculoParcelaFreteCiaAereaDAO"
 * @spring.property name="calculoFreteCiaAereaService" ref="lms.expedicao.calculoFreteCiaAereaService"
 * @spring.property name="faixaProgressivaService" ref="lms.tabelaprecos.faixaProgressivaService"
 * @spring.property name="enderecoPessoaService" ref="lms.configuracoes.enderecoPessoaService"
 */
public class CalculoParcelaFreteCiaAereaService extends CalculoParcelaServicoService {
	private FaixaProgressivaService faixaProgressivaService;
	private EnderecoPessoaService enderecoPessoaService;

	public CalculoParcelaFreteCiaAereaDAO getCalculoParcelaFreteCiaAereaDAO() {
		return (CalculoParcelaFreteCiaAereaDAO) super.getCalculoParcelaServicoDAO();
	}
	public void setCalculoParcelaFreteCiaAereaDAO(CalculoParcelaFreteCiaAereaDAO calculoParcelaFreteCiaAereaDAO) {
		super.setCalculoParcelaServicoDAO(calculoParcelaFreteCiaAereaDAO);
	}

	public CalculoFreteCiaAereaService getCalculoFreteCiaAereaService() {
		return (CalculoFreteCiaAereaService) super.getCalculoServicoService();
	}
	public void setCalculoFreteCiaAereaService(CalculoFreteCiaAereaService calculoFreteCiaAereaService) {
		super.setCalculoServicoService(calculoFreteCiaAereaService);
	}

	public FaixaProgressivaService getFaixaProgressivaService() {
		return faixaProgressivaService;
	}
	public void setFaixaProgressivaService(FaixaProgressivaService faixaProgressivaService) {
		this.faixaProgressivaService = faixaProgressivaService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public ParcelaServico findParcelaFretePeso(CalculoFreteCiaAerea calculoFreteCiaAerea) {
		Long idTabelaPreco = calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco();
		ParcelaPreco parcelaPreco = findParcelaPreco(idTabelaPreco, ConstantesExpedicao.CD_FRETE_PESO);
		BigDecimal vlBrutoParcela = null;
		BigDecimal vlTarifaMinima = BigDecimal.ZERO;
		BigDecimal vlParcelaAux = null;

		BigDecimal vlUnitarioParcela = findValorFaixaFretePeso(calculoFreteCiaAerea, ConstantesExpedicao.CD_FRETE_PESO);

		if(isServicoProximoDiaOuProximoVoo(calculoFreteCiaAerea)){
			BigDecimal vlQuiloExcedente = findValorFaixaFretePeso(calculoFreteCiaAerea, ConstantesExpedicao.CD_FRETE_QUILO);
			vlParcelaAux = vlUnitarioParcela.add(calculoFreteCiaAerea.getPsQuiloExcedente().multiply(vlQuiloExcedente));
		} else {
			vlTarifaMinima = findValorFaixaFretePeso(calculoFreteCiaAerea, ConstantesExpedicao.CD_TARIFA_MINIMA);
			vlParcelaAux = vlUnitarioParcela.multiply(calculoFreteCiaAerea.getPsReferencia());
		}

		if( (vlTarifaMinima != null) && (vlParcelaAux == null) ) {
			vlBrutoParcela = vlTarifaMinima;
		} else if( (vlTarifaMinima == null) && (vlParcelaAux != null) ) {
			vlBrutoParcela = vlParcelaAux;
		} else if( (vlTarifaMinima != null) && (vlParcelaAux != null) ) {
			vlBrutoParcela = CompareUtils.max(vlParcelaAux, vlTarifaMinima);
		}
		vlBrutoParcela = CompareUtils.max(vlBrutoParcela, vlTarifaMinima);
		return new ParcelaServico(parcelaPreco, null, vlBrutoParcela);
	}

	public BigDecimal findValorFaixaFretePeso_TarifaMinima(CalculoFreteCiaAerea calculoFreteCiaAerea) {
		return findValorFaixaFretePeso(calculoFreteCiaAerea, ConstantesExpedicao.CD_TARIFA_MINIMA);
	}
	
	private BigDecimal findValorFaixaFretePeso(CalculoFreteCiaAerea calculoFreteCiaAerea, String cdParcelaPreco) {
		ParcelaPreco parcelaPreco = findParcelaPreco(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), cdParcelaPreco);
		BigDecimal vlFreteParcela = BigDecimalUtils.ZERO;

		Long idUFAeroportoOrigem = getIdUFEnderecoPessoa(calculoFreteCiaAerea.getIdAeroportoOrigem());
		Long idUFAeroportoDestino = getIdUFEnderecoPessoa(calculoFreteCiaAerea.getIdAeroportoDestino());
		
		RotaPreco rotaPreco = null;
		if(ConstantesExpedicao.CD_FRETE_PESO.equals(cdParcelaPreco)) {
			rotaPreco = getCalculoParcelaFreteCiaAereaDAO().findRotaPrecoFretePeso(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), calculoFreteCiaAerea.getIdAeroportoOrigem(), calculoFreteCiaAerea.getIdAeroportoDestino());
			if(rotaPreco != null) {
				if(Boolean.TRUE.equals(calculoFreteCiaAerea.getBlTarifaSpot())) {
					TarifaSpot tarifaSpot = calculoFreteCiaAerea.getTarifaSpot();
					if(tarifaSpot != null) {
						vlFreteParcela = tarifaSpot.getVlTarifaSpot();
					}
					return vlFreteParcela;
				}
			}
		} else if(ConstantesExpedicao.CD_TARIFA_MINIMA.equals(cdParcelaPreco) || ConstantesExpedicao.CD_FRETE_QUILO.equals(cdParcelaPreco)) {
			rotaPreco = getCalculoParcelaFreteCiaAereaDAO().findRotaPrecoTaxaMinima(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), calculoFreteCiaAerea.getIdAeroportoOrigem(), calculoFreteCiaAerea.getIdAeroportoDestino(),idUFAeroportoOrigem,idUFAeroportoDestino);
		}

		if(rotaPreco != null) {
			if(parcelaPreco.getTpPrecificacao().getValue().equals("M")) {
				vlFreteParcela = findVlMinimoProgressivo(calculoFreteCiaAerea, parcelaPreco.getIdParcelaPreco(), cdParcelaPreco, rotaPreco.getIdRotaPreco());
			} else if(parcelaPreco.getTpPrecificacao().getValue().equals("P")) {
				vlFreteParcela = getCalculoParcelaFreteCiaAereaDAO().findPrecoFrete(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), rotaPreco.getIdRotaPreco()).getVlPrecoFrete();
			}
		}
		return vlFreteParcela;
	}

	private BigDecimal findVlMinimoProgressivo(CalculoFreteCiaAerea calculoFreteCiaAerea, Long idParcelaPreco, String cdParcelaPreco, Long idRotaPreco) {
		BigDecimal vlFixo = null;
		ValorFaixaProgressiva valorFaixaProgressiva = null;
		Long idTabelaPreco = calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco();

		Long idProdutoEspecifico = calculoFreteCiaAerea.getIdProdutoEspecifico();
		Boolean isServicoProximoDiaOuProximoVoo = isServicoProximoDiaOuProximoVoo(calculoFreteCiaAerea);
		if( (idProdutoEspecifico != null) && (idProdutoEspecifico.longValue() > 0) && Boolean.FALSE.equals(isServicoProximoDiaOuProximoVoo)) {
			// se informou Produto Especifico
			valorFaixaProgressiva = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressivaProdutoEspecifico(idTabelaPreco, idParcelaPreco, idProdutoEspecifico, idRotaPreco);
			vlFixo = valorFaixaProgressiva.getVlFixo();
		} else {
			if(Boolean.TRUE.equals(isServicoProximoDiaOuProximoVoo)){
				BigDecimal psQuiloExcedente = BigDecimal.ZERO;
				valorFaixaProgressiva = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, calculoFreteCiaAerea.getPsReferencia(), null, idRotaPreco, null, JTDateTimeUtils.getDataAtual(),false);
				
				if(valorFaixaProgressiva == null){
					BigDecimal vlFaixaProgressivaMaximo =  getCalculoParcelaFreteCiaAereaDAO().getValorReferenciaFaixaProgressiva(idTabelaPreco, idParcelaPreco, null, null, idRotaPreco, null, false, Boolean.TRUE);
					valorFaixaProgressiva = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressivaEnquadradaMaxima(vlFaixaProgressivaMaximo, idTabelaPreco, idParcelaPreco, idRotaPreco, JTDateTimeUtils.getDataAtual());
					psQuiloExcedente = calculoFreteCiaAerea.getPsReferencia().subtract(vlFaixaProgressivaMaximo);
				}
				
				calculoFreteCiaAerea.setPsQuiloExcedente(psQuiloExcedente);
				
			} else {
				// se nao informou Produto Especifico
				valorFaixaProgressiva = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, calculoFreteCiaAerea.getPsReferencia(), null, idRotaPreco, null, JTDateTimeUtils.getDataAtual(),true);
	
				if(valorFaixaProgressiva == null) {
					valorFaixaProgressiva = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, calculoFreteCiaAerea.getPsReferencia(), null, idRotaPreco, null, JTDateTimeUtils.getDataAtual(),false);
				}
				
				if(valorFaixaProgressiva == null) {
					valorFaixaProgressiva = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, calculoFreteCiaAerea.getPsReferencia(), null, idRotaPreco, null, null,false);
				}
			}
			
			
			if(valorFaixaProgressiva == null) {
				throw new BusinessException("LMS-30030", new Object[]{cdParcelaPreco});
			}
			vlFixo = valorFaixaProgressiva.getVlFixo();
			if(valorFaixaProgressiva.getPcDesconto() != null) {
				BigDecimal vlDesconto = vlFixo.multiply(valorFaixaProgressiva.getPcDesconto().divide(BigDecimalUtils.HUNDRED, 4, BigDecimal.ROUND_UNNECESSARY));
				vlFixo = vlFixo.subtract(vlDesconto);
			}
			if(valorFaixaProgressiva.getVlAcrescimo() != null) {
				vlFixo = vlFixo.add(valorFaixaProgressiva.getVlAcrescimo());
			}
		}
		return vlFixo;
	}
	
	private boolean isServicoProximoDiaOuProximoVoo(CalculoFreteCiaAerea calculoFreteCiaAerea) {
		return calculoFreteCiaAerea.getTabelaPreco().getTpServico() != null
				&& calculoFreteCiaAerea.getTabelaPreco().getTpServico().getValue() != null
				&& (ConstantesTabelaPrecos.TP_SERVICO_PROXIMO_DIA.equals(calculoFreteCiaAerea.getTabelaPreco().getTpServico().getValue()) 
						|| ConstantesTabelaPrecos.TP_SERVICO_PROXIMO_VOO.equals(calculoFreteCiaAerea.getTabelaPreco().getTpServico().getValue()));
	}

	public ParcelaServico findParcelaTaxaTerrestre(CalculoFreteCiaAerea calculoFreteCiaAerea) {
		FilialCiaAerea filialCiaAerea = getCalculoParcelaFreteCiaAereaDAO().findFilialCiaAerea(calculoFreteCiaAerea.getIdAeroportoDestino(), calculoFreteCiaAerea.getIdCiaAerea());
		if( (filialCiaAerea == null) || Boolean.FALSE.equals(filialCiaAerea.getBlTaxaTerrestre()) ) {
			return null;
		}

		ParcelaPreco parcelaPreco = findParcelaPreco(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAXA_TERRESTRE);
		BigDecimal vlBrutoParcela = null;

		//pega a faixa de menor peso
		FaixaProgressiva faixaProgressiva = getFaixaProgressivaService().findFaixaProgressivaMenorPeso(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco());

		BigDecimal vlPesoRestante;
		if(CompareUtils.lt(calculoFreteCiaAerea.getPsReferencia(), faixaProgressiva.getVlFaixaProgressiva())) {
			vlBrutoParcela = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressiva(faixaProgressiva.getIdFaixaProgressiva()).getVlFixo();
		} else {
			BigDecimal vlFaixaMenor = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressiva(faixaProgressiva.getIdFaixaProgressiva()).getVlFixo();
			vlPesoRestante = calculoFreteCiaAerea.getPsReferencia().subtract(faixaProgressiva.getVlFaixaProgressiva());
			//pega faixa enquadrada
			faixaProgressiva = getFaixaProgressivaService().findFaixaProgressivaEnquadrada(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), vlPesoRestante);
			BigDecimal vlFixoEnquadrado = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressiva(faixaProgressiva.getIdFaixaProgressiva()).getVlFixo();
			BigDecimal vlFaixaEnquadrada = vlPesoRestante.multiply(vlFixoEnquadrado);

			vlBrutoParcela = vlFaixaMenor.add(vlFaixaEnquadrada);
		}
		if(vlBrutoParcela == null) {
			return null;
		}
		return new ParcelaServico(parcelaPreco, null, vlBrutoParcela);
	}

	public ParcelaServico findParcelaTaxaCombustivel(CalculoFreteCiaAerea calculoFreteCiaAerea) {
		ParcelaPreco parcelaPreco = findParcelaPreco(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAXA_COMBUSTIVEL);
		if(parcelaPreco == null) {
			return null;
		}

		Long idUFAeroportoOrigem = getIdUFEnderecoPessoa(calculoFreteCiaAerea.getIdAeroportoOrigem());
		Long idUFAeroportoDestino = getIdUFEnderecoPessoa(calculoFreteCiaAerea.getIdAeroportoDestino());
		RotaPreco rotaPreco = getCalculoParcelaFreteCiaAereaDAO().findRotaPrecoTaxaCombustivel(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), calculoFreteCiaAerea.getIdAeroportoOrigem(), idUFAeroportoOrigem, calculoFreteCiaAerea.getIdAeroportoDestino(), idUFAeroportoDestino);
		if(rotaPreco == null) {
			rotaPreco = getCalculoParcelaFreteCiaAereaDAO().findRotaPrecoTaxaCombustivel(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), calculoFreteCiaAerea.getIdAeroportoOrigem(), idUFAeroportoOrigem, null, idUFAeroportoDestino);
			if(rotaPreco == null) {
				rotaPreco = getCalculoParcelaFreteCiaAereaDAO().findRotaPrecoTaxaCombustivel(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), null, idUFAeroportoOrigem, null, idUFAeroportoDestino);
			}
		}

		BigDecimal vlBrutoParcela = null;
		if(rotaPreco != null) {
			ValorFaixaProgressiva valorFaixaProgressiva = getCalculoParcelaFreteCiaAereaDAO().findValorFaixaProgressivaEnquadrada(calculoFreteCiaAerea.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), calculoFreteCiaAerea.getPsReferencia(), null, rotaPreco.getIdRotaPreco(), null,null,false);
			if(valorFaixaProgressiva != null) {
				vlBrutoParcela = valorFaixaProgressiva.getVlFixo();
			}
			if(vlBrutoParcela != null) {
				vlBrutoParcela = calculoFreteCiaAerea.getPsReferencia().multiply(vlBrutoParcela);
			}
		}
		if(vlBrutoParcela == null) {
			return null;
		}
		
		return new ParcelaServico(parcelaPreco, null, vlBrutoParcela);
	}

	private Long getIdUFEnderecoPessoa(Long idPessoa) {
		EnderecoPessoa enderecoPessoa = getEnderecoPessoaService().findEnderecoPessoaPadrao(idPessoa);
		return enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
	}

}
