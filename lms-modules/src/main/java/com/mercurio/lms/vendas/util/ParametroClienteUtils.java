package com.mercurio.lms.vendas.util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.ValorFaixaProgressivaProposta;

public abstract class ParametroClienteUtils {

	public static ParametroCliente getParametroClientePadrao() {
		ParametroCliente parametroCliente = new ParametroCliente();
		DomainValue tpIndicadorTabela = new DomainValue("T");
		DomainValue tpIndicadorDesconto = new DomainValue("D");

		parametroCliente.setTpIndicadorMinFretePeso(tpIndicadorTabela);
		parametroCliente.setVlMinFretePeso(BigDecimalUtils.ZERO);
		parametroCliente.setTpIndicadorFretePeso(tpIndicadorTabela);
		parametroCliente.setVlFretePeso(BigDecimalUtils.ZERO);

		parametroCliente.setTpTarifaMinima(tpIndicadorTabela);
		parametroCliente.setVlTarifaMinima(BigDecimalUtils.ZERO);

		parametroCliente.setTpIndicVlrTblEspecifica(tpIndicadorTabela);
		parametroCliente.setVlTblEspecifica(BigDecimalUtils.ZERO);

		parametroCliente.setTpIndicadorAdvalorem(tpIndicadorTabela);
		parametroCliente.setVlAdvalorem(BigDecimalUtils.ZERO);

		parametroCliente.setTpIndicadorAdvalorem2(tpIndicadorTabela);
		parametroCliente.setVlAdvalorem2(BigDecimalUtils.ZERO);

		parametroCliente.setTpIndicadorValorReferencia(tpIndicadorTabela);
		parametroCliente.setVlValorReferencia(BigDecimalUtils.ZERO);
		
		parametroCliente.setTpIndicadorPercentualGris(tpIndicadorTabela);
		parametroCliente.setVlPercentualGris(BigDecimalUtils.ZERO);
		parametroCliente.setTpIndicadorMinimoGris(tpIndicadorTabela);
		parametroCliente.setVlMinimoGris(BigDecimalUtils.ZERO);

		parametroCliente.setTpIndicadorPercentualTrt(tpIndicadorTabela);
		parametroCliente.setVlPercentualTrt(BigDecimalUtils.ZERO);
		parametroCliente.setTpIndicadorMinimoTrt(tpIndicadorTabela);
		parametroCliente.setVlMinimoTrt(BigDecimalUtils.ZERO);

		parametroCliente.setTpIndicadorPercentualTde(tpIndicadorTabela);
		parametroCliente.setVlPercentualTde(BigDecimalUtils.ZERO);
		parametroCliente.setTpIndicadorMinimoTde(tpIndicadorTabela);
		parametroCliente.setVlMinimoTde(BigDecimalUtils.ZERO);

		parametroCliente.setTpIndicadorPedagio(tpIndicadorTabela);
		parametroCliente.setVlPedagio(BigDecimalUtils.ZERO);
		
		parametroCliente.setTpIndicadorPercMinimoProgr(tpIndicadorDesconto);
		parametroCliente.setVlPercMinimoProgr(BigDecimalUtils.ZERO);

		parametroCliente.setPcPagaCubagem(BigDecimalUtils.ZERO);
		parametroCliente.setBlPagaCubagem(Boolean.TRUE);

		parametroCliente.setBlPagaPesoExcedente(Boolean.FALSE);

		parametroCliente.setPcFretePercentual(BigDecimalUtils.ZERO);
		parametroCliente.setVlMinimoFretePercentual(BigDecimalUtils.ZERO);
		parametroCliente.setVlToneladaFretePercentual(BigDecimalUtils.ZERO);
		parametroCliente.setPsFretePercentual(BigDecimalUtils.ZERO);

		parametroCliente.setPcDescontoFreteTotal(BigDecimalUtils.ZERO);
		parametroCliente.setPcCobrancaDevolucoes(BigDecimalUtils.ZERO);
		parametroCliente.setPcCobrancaReentrega(BigDecimalUtils.ZERO);
		parametroCliente.setVlMinimoFreteQuilo(BigDecimalUtils.ZERO);
		parametroCliente.setVlFreteVolume(BigDecimalUtils.ZERO);

		return parametroCliente;
	}

	public static void copyParametroCliente(ParametroCliente parametroClienteOriginal, ParametroCliente parametroClienteNovo) {

		parametroClienteNovo.setTpIndicadorPercentualGris(parametroClienteOriginal.getTpIndicadorPercentualGris());
		parametroClienteNovo.setVlPercentualGris(parametroClienteOriginal.getVlPercentualGris());
		parametroClienteNovo.setTpIndicadorMinimoGris(parametroClienteOriginal.getTpIndicadorMinimoGris());
		parametroClienteNovo.setVlMinimoGris(parametroClienteOriginal.getVlMinimoGris());
		parametroClienteNovo.setTpIndicadorPercentualTrt(parametroClienteOriginal.getTpIndicadorPercentualTrt());
		parametroClienteNovo.setVlPercentualTrt(parametroClienteOriginal.getVlPercentualTrt());
		parametroClienteNovo.setTpIndicadorMinimoTrt(parametroClienteOriginal.getTpIndicadorMinimoTrt());
		parametroClienteNovo.setVlMinimoTrt(parametroClienteOriginal.getVlMinimoTrt());
		parametroClienteNovo.setTpIndicadorPercentualTde(parametroClienteOriginal.getTpIndicadorPercentualTde());
		parametroClienteNovo.setVlPercentualTde(parametroClienteOriginal.getVlPercentualTde());
		parametroClienteNovo.setTpIndicadorMinimoTde(parametroClienteOriginal.getTpIndicadorMinimoTde());
		parametroClienteNovo.setVlMinimoTde(parametroClienteOriginal.getVlMinimoTde());
		parametroClienteNovo.setTpIndicadorPedagio(parametroClienteOriginal.getTpIndicadorPedagio());
		parametroClienteNovo.setVlPedagio(parametroClienteOriginal.getVlPedagio());
		parametroClienteNovo.setTpIndicadorMinFretePeso(parametroClienteOriginal.getTpIndicadorMinFretePeso());
		parametroClienteNovo.setVlMinFretePeso(parametroClienteOriginal.getVlMinFretePeso());
		parametroClienteNovo.setTpIndicadorPercMinimoProgr(parametroClienteOriginal.getTpIndicadorPercMinimoProgr());
		parametroClienteNovo.setVlPercMinimoProgr(parametroClienteOriginal.getVlPercMinimoProgr());
		parametroClienteNovo.setTpIndicadorFretePeso(parametroClienteOriginal.getTpIndicadorFretePeso());
		parametroClienteNovo.setVlFretePeso(parametroClienteOriginal.getVlFretePeso());
		parametroClienteNovo.setTpIndicadorAdvalorem(parametroClienteOriginal.getTpIndicadorAdvalorem());
		parametroClienteNovo.setTpIndicadorAdvalorem2(parametroClienteOriginal.getTpIndicadorAdvalorem2());
		parametroClienteNovo.setVlAdvalorem(parametroClienteOriginal.getVlAdvalorem());
		parametroClienteNovo.setVlAdvalorem2(parametroClienteOriginal.getVlAdvalorem2());
		parametroClienteNovo.setTpIndicadorValorReferencia(parametroClienteOriginal.getTpIndicadorValorReferencia());
		parametroClienteNovo.setVlValorReferencia(parametroClienteOriginal.getVlValorReferencia());
		parametroClienteNovo.setVlMinimoFreteQuilo(parametroClienteOriginal.getVlMinimoFreteQuilo());
		parametroClienteNovo.setPcFretePercentual(parametroClienteOriginal.getPcFretePercentual());
		parametroClienteNovo.setVlMinimoFretePercentual(parametroClienteOriginal.getVlMinimoFretePercentual());
		parametroClienteNovo.setVlToneladaFretePercentual(parametroClienteOriginal.getVlToneladaFretePercentual());
		parametroClienteNovo.setPsFretePercentual(parametroClienteOriginal.getPsFretePercentual());
		parametroClienteNovo.setPcDescontoFreteTotal(parametroClienteOriginal.getPcDescontoFreteTotal());
		parametroClienteNovo.setTpIndicVlrTblEspecifica(parametroClienteOriginal.getTpIndicVlrTblEspecifica());
		parametroClienteNovo.setVlTblEspecifica(parametroClienteOriginal.getVlTblEspecifica());
		parametroClienteNovo.setVlFreteVolume(parametroClienteOriginal.getVlFreteVolume());
		parametroClienteNovo.setBlPagaCubagem(parametroClienteOriginal.getBlPagaCubagem());
		parametroClienteNovo.setPcPagaCubagem(parametroClienteOriginal.getPcPagaCubagem());
		parametroClienteNovo.setBlPagaPesoExcedente(parametroClienteOriginal.getBlPagaPesoExcedente());
		parametroClienteNovo.setTpTarifaMinima(parametroClienteOriginal.getTpTarifaMinima());
		parametroClienteNovo.setVlTarifaMinima(parametroClienteOriginal.getVlTarifaMinima());
		parametroClienteNovo.setPcCobrancaReentrega(parametroClienteOriginal.getPcCobrancaReentrega());
		parametroClienteNovo.setPcCobrancaDevolucoes(parametroClienteOriginal.getPcCobrancaDevolucoes());
		parametroClienteNovo.setPaisByIdPaisOrigem(parametroClienteOriginal.getPaisByIdPaisOrigem());
		parametroClienteNovo.setPaisByIdPaisDestino(parametroClienteOriginal.getPaisByIdPaisDestino());
		parametroClienteNovo.setMunicipioByIdMunicipioDestino(parametroClienteOriginal.getMunicipioByIdMunicipioDestino());
		parametroClienteNovo.setMunicipioByIdMunicipioOrigem(parametroClienteOriginal.getMunicipioByIdMunicipioOrigem());
		parametroClienteNovo.setZonaByIdZonaOrigem(parametroClienteOriginal.getZonaByIdZonaOrigem());
		parametroClienteNovo.setZonaByIdZonaDestino(parametroClienteOriginal.getZonaByIdZonaDestino());
		parametroClienteNovo.setAeroportoByIdAeroportoOrigem(parametroClienteOriginal.getAeroportoByIdAeroportoOrigem());
		parametroClienteNovo.setAeroportoByIdAeroportoDestino(parametroClienteOriginal.getAeroportoByIdAeroportoDestino());
		parametroClienteNovo.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(parametroClienteOriginal.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem());
		parametroClienteNovo.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(parametroClienteOriginal.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino());
		parametroClienteNovo.setFilialByIdFilialOrigem(parametroClienteOriginal.getFilialByIdFilialOrigem());
		parametroClienteNovo.setFilialByIdFilialDestino(parametroClienteOriginal.getFilialByIdFilialDestino());
		parametroClienteNovo.setUnidadeFederativaByIdUfOrigem(parametroClienteOriginal.getUnidadeFederativaByIdUfOrigem());
		parametroClienteNovo.setUnidadeFederativaByIdUfDestino(parametroClienteOriginal.getUnidadeFederativaByIdUfDestino());
		parametroClienteNovo.setDsEspecificacaoRota(parametroClienteOriginal.getDsEspecificacaoRota());
		parametroClienteNovo.setDtVigenciaInicial(parametroClienteOriginal.getDtVigenciaInicial());
		parametroClienteNovo.setDtVigenciaFinal(parametroClienteOriginal.getDtVigenciaFinal());
		parametroClienteNovo.setTpSituacaoParametro(parametroClienteOriginal.getTpSituacaoParametro());
		parametroClienteNovo.setTabelaDivisaoCliente(parametroClienteOriginal.getTabelaDivisaoCliente());
		parametroClienteNovo.setTabelaPreco(parametroClienteOriginal.getTabelaPreco());
		parametroClienteNovo.setClienteByIdClienteRedespacho(parametroClienteOriginal.getClienteByIdClienteRedespacho());
		parametroClienteNovo.setFilialByIdFilialMercurioRedespacho(parametroClienteOriginal.getFilialByIdFilialMercurioRedespacho());

		copyTaxasCliente(parametroClienteOriginal, parametroClienteNovo);
		copyGeneralidadesCliente(parametroClienteOriginal, parametroClienteNovo);
		copyFaixasProgressivasCliente(parametroClienteOriginal,parametroClienteNovo);
	}
	
	private static void copyFaixasProgressivasCliente(ParametroCliente parametroClienteOriginal,
            ParametroCliente parametroClienteNovo) {
	    List<ValorFaixaProgressivaProposta> listaNova = new ArrayList<ValorFaixaProgressivaProposta>();
	    if (parametroClienteOriginal.getValoresFaixaProgressivaProposta() != null){
	        for (ValorFaixaProgressivaProposta valorFaixa: (List<ValorFaixaProgressivaProposta>) parametroClienteOriginal.getValoresFaixaProgressivaProposta()){
	            ValorFaixaProgressivaProposta valorNovo = new ValorFaixaProgressivaProposta();
	            valorNovo.setDestinoProposta(null);
	            valorNovo.setParametroCliente(parametroClienteNovo);
	            valorNovo.setTpIndicador(valorFaixa.getTpIndicador());
	            valorNovo.setVlFixo(valorFaixa.getVlFixo());
	            valorNovo.setPcVariacao(valorFaixa.getPcVariacao());
	            valorNovo.setFaixaProgressivaProposta(valorFaixa.getFaixaProgressivaProposta());
	            listaNova.add(valorNovo);
	        }
	        parametroClienteNovo.setValoresFaixaProgressivaProposta(listaNova);
	    }
	    
        
    }

    public static void copyParametroClienteCompleto(ParametroCliente parametroClienteOriginal, ParametroCliente parametroClienteNovo) {
		copyParametroCliente(parametroClienteOriginal, parametroClienteNovo);
		/** Percentuais de Reajuste */
		parametroClienteNovo.setPcReajAdvalorem(parametroClienteOriginal.getPcReajAdvalorem());
		parametroClienteNovo.setPcReajAdvalorem2(parametroClienteOriginal.getPcReajAdvalorem2());
		parametroClienteNovo.setPcReajFretePeso(parametroClienteOriginal.getPcReajFretePeso());
		parametroClienteNovo.setPcReajMinimoGris(parametroClienteOriginal.getPcReajMinimoGris());
		parametroClienteNovo.setPcReajMinimoTrt(parametroClienteOriginal.getPcReajMinimoTrt());
		parametroClienteNovo.setPcReajMinimoTde(parametroClienteOriginal.getPcReajMinimoTde());
		parametroClienteNovo.setPcReajPedagio(parametroClienteOriginal.getPcReajPedagio());
		parametroClienteNovo.setPcReajTarifaMinima(parametroClienteOriginal.getPcReajTarifaMinima());
		parametroClienteNovo.setPcReajVlFreteVolume(parametroClienteOriginal.getPcReajVlFreteVolume());
		parametroClienteNovo.setPcReajVlMinimoFretePercen(parametroClienteOriginal.getPcReajVlMinimoFretePercen());
		parametroClienteNovo.setPcReajVlMinimoFreteQuilo(parametroClienteOriginal.getPcReajVlMinimoFreteQuilo());
		parametroClienteNovo.setPcReajVlTarifaEspecifica(parametroClienteOriginal.getPcReajVlTarifaEspecifica());
		parametroClienteNovo.setPcReajVlToneladaFretePerc(parametroClienteOriginal.getPcReajVlToneladaFretePerc());
		parametroClienteNovo.setPcReajReferencia(parametroClienteOriginal.getPcReajReferencia());
		parametroClienteNovo.setPcReajVlMinimoFretePeso(parametroClienteOriginal.getPcReajVlMinimoFretePeso());
		parametroClienteNovo.setPcReajVlMinimoProg(parametroClienteOriginal.getPcReajVlMinimoProg());
	}

	public static void copyTaxasCliente(ParametroCliente parametroClienteOriginal, ParametroCliente parametroClienteNovo) {
		List<TaxaCliente> origem = parametroClienteOriginal.getTaxaClientes();
		List<TaxaCliente> destino = new ArrayList<TaxaCliente>(origem.size());
		for(TaxaCliente taxaCliente : origem){
			destino.add(copyTaxaCliente(taxaCliente, parametroClienteNovo));
		}
		parametroClienteNovo.setTaxaClientes(destino);
	}

	public static TaxaCliente copyTaxaCliente(TaxaCliente taxaClienteOriginal, ParametroCliente parametroCliente) {
		TaxaCliente taxaClienteNovo = new TaxaCliente();

		taxaClienteNovo.setDsSimbolo(taxaClienteOriginal.getDsSimbolo());
		taxaClienteNovo.setParcelaPreco(taxaClienteOriginal.getParcelaPreco());
		taxaClienteNovo.setPcReajTaxa(taxaClienteOriginal.getPcReajTaxa());
		taxaClienteNovo.setPcReajVlExcedente(taxaClienteOriginal.getPcReajVlExcedente());
		taxaClienteNovo.setPsMinimo(taxaClienteOriginal.getPsMinimo());
		taxaClienteNovo.setTpTaxaIndicador(taxaClienteOriginal.getTpTaxaIndicador());
		taxaClienteNovo.setVlExcedente(taxaClienteOriginal.getVlExcedente());
		taxaClienteNovo.setVlTaxa(taxaClienteOriginal.getVlTaxa());
		taxaClienteNovo.setParametroCliente(parametroCliente);
		return taxaClienteNovo;
	}

	public static void copyGeneralidadesCliente(ParametroCliente parametroClienteOriginal, ParametroCliente parametroClienteNovo) {
		List<GeneralidadeCliente> origem = parametroClienteOriginal.getGeneralidadeClientes();
		List<GeneralidadeCliente> destino = new ArrayList<GeneralidadeCliente>(origem.size());
		for(GeneralidadeCliente generalidadeCliente : origem) {
			destino.add(copyGeneralidadeCliente(generalidadeCliente, parametroClienteNovo));
		}
		parametroClienteNovo.setGeneralidadeClientes(destino);
	}

	public static GeneralidadeCliente copyGeneralidadeCliente(GeneralidadeCliente generalidadeClienteOriginal, ParametroCliente parametroCliente) {
		GeneralidadeCliente generalidadeCliente = new GeneralidadeCliente();

		generalidadeCliente.setDsSimbolo(generalidadeClienteOriginal.getDsSimbolo());
		generalidadeCliente.setParcelaPreco(generalidadeClienteOriginal.getParcelaPreco());
		generalidadeCliente.setPcReajGeneralidade(generalidadeClienteOriginal.getPcReajGeneralidade());
		generalidadeCliente.setTpIndicador(generalidadeClienteOriginal.getTpIndicador());
		generalidadeCliente.setValorIndicador(generalidadeClienteOriginal.getValorIndicador());
		generalidadeCliente.setVlGeneralidade(generalidadeClienteOriginal.getVlGeneralidade());
		generalidadeCliente.setTpIndicadorMinimo(generalidadeClienteOriginal.getTpIndicadorMinimo());
		generalidadeCliente.setVlMinimo(generalidadeClienteOriginal.getVlMinimo());
		generalidadeCliente.setPcReajMinimo(generalidadeClienteOriginal.getPcReajMinimo());
		generalidadeCliente.setParametroCliente(parametroCliente);

		return generalidadeCliente;
	}

	public static void addParametroClienteRestricaoRotaWithoutOrder(
			DetachedCriteria detachedCriteria,
			RestricaoRota restricaoRotaOrigem,
			RestricaoRota restricaoRotaDestino,
			Boolean forceNull
	) {


		detachedCriteria.add(Restrictions.le("pc.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()));
		detachedCriteria.add(Restrictions.ge("pc.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		detachedCriteria.add(Restrictions.eq("pc.tpSituacaoParametro", "A"));

		// Origem
		detachedCriteria.add(Restrictions.eq("pc.zonaByIdZonaOrigem.id", restricaoRotaOrigem.getIdZona()));
		if(forceNull) {
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.paisByIdPaisOrigem.id", restricaoRotaOrigem.getIdPais()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.unidadeFederativaByIdUfOrigem.id", restricaoRotaOrigem.getIdUnidadeFederativa()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.aeroportoByIdAeroportoOrigem.id", restricaoRotaOrigem.getIdAeroporto()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.filialByIdFilialOrigem.id", restricaoRotaOrigem.getIdFilial()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.municipioByIdMunicipioOrigem.id", restricaoRotaOrigem.getIdMunicipio()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.grupoRegiaoOrigem.id", restricaoRotaOrigem.getIdGrupoRegiao()));
			RotaPrecoUtils.addTipoLocalizacaoMunicipio(restricaoRotaOrigem, detachedCriteria,"pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.id");
			
		} else {
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.paisByIdPaisOrigem.id", restricaoRotaOrigem.getIdPais()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.unidadeFederativaByIdUfOrigem.id", restricaoRotaOrigem.getIdUnidadeFederativa()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.aeroportoByIdAeroportoOrigem.id", restricaoRotaOrigem.getIdAeroporto()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.filialByIdFilialOrigem.id", restricaoRotaOrigem.getIdFilial()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.municipioByIdMunicipioOrigem.id", restricaoRotaOrigem.getIdMunicipio()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.grupoRegiaoOrigem.id", restricaoRotaOrigem.getIdGrupoRegiao()));
			RotaPrecoUtils.addTipoLocalizacaoMunicipio(restricaoRotaOrigem, detachedCriteria,"pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.id");
			
		}
		

		// Destino
		detachedCriteria.add(Restrictions.eq("pc.zonaByIdZonaDestino.id", restricaoRotaDestino.getIdZona()));
		if(forceNull) {
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.paisByIdPaisDestino.id", restricaoRotaDestino.getIdPais()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.unidadeFederativaByIdUfDestino.id", restricaoRotaDestino.getIdUnidadeFederativa()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.aeroportoByIdAeroportoDestino.id", restricaoRotaDestino.getIdAeroporto()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.filialByIdFilialDestino.id", restricaoRotaDestino.getIdFilial()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.municipioByIdMunicipioDestino.id", restricaoRotaDestino.getIdMunicipio()));
			detachedCriteria.add(RotaPrecoUtils.getCriterionForceNull("pc.grupoRegiaoDestino.id", restricaoRotaDestino.getIdGrupoRegiao()));
			RotaPrecoUtils.addTipoLocalizacaoMunicipio(restricaoRotaDestino, detachedCriteria,"pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.id");
			
		} else {
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.paisByIdPaisDestino.id", restricaoRotaDestino.getIdPais()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.unidadeFederativaByIdUfDestino.id", restricaoRotaDestino.getIdUnidadeFederativa()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.aeroportoByIdAeroportoDestino.id", restricaoRotaDestino.getIdAeroporto()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.filialByIdFilialDestino.id", restricaoRotaDestino.getIdFilial()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.municipioByIdMunicipioDestino.id", restricaoRotaDestino.getIdMunicipio()));
			detachedCriteria.add(RotaPrecoUtils.getCriterion("pc.grupoRegiaoDestino.id", restricaoRotaDestino.getIdGrupoRegiao()));
			RotaPrecoUtils.addTipoLocalizacaoMunicipio(restricaoRotaDestino, detachedCriteria,"pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.id");
			
		}

	}

	public static void addParametroClienteRestricaoRota(DetachedCriteria detachedCriteria, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		addParametroClienteRestricaoRotaWithoutOrder(detachedCriteria, restricaoRotaOrigem, restricaoRotaDestino, Boolean.FALSE);

		// Origem
		detachedCriteria.addOrder(Order.asc("pc.zonaByIdZonaOrigem.id"));
		detachedCriteria.addOrder(Order.asc("pc.paisByIdPaisOrigem.id"));
		detachedCriteria.addOrder(Order.asc("pc.unidadeFederativaByIdUfOrigem.id"));
		detachedCriteria.addOrder(Order.asc("pc.filialByIdFilialOrigem.id"));
		detachedCriteria.addOrder(Order.asc("pc.municipioByIdMunicipioOrigem.id"));
		detachedCriteria.addOrder(Order.asc("pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.id"));
		detachedCriteria.addOrder(Order.asc("pc.aeroportoByIdAeroportoOrigem.id"));

		// Destino
		detachedCriteria.addOrder(Order.asc("pc.zonaByIdZonaDestino.id"));
		detachedCriteria.addOrder(Order.asc("pc.paisByIdPaisDestino.id"));
		detachedCriteria.addOrder(Order.asc("pc.unidadeFederativaByIdUfDestino.id"));
		detachedCriteria.addOrder(Order.asc("pc.filialByIdFilialDestino.id"));
		detachedCriteria.addOrder(Order.asc("pc.municipioByIdMunicipioDestino.id"));
		detachedCriteria.addOrder(Order.asc("pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.id"));
		detachedCriteria.addOrder(Order.asc("pc.aeroportoByIdAeroportoDestino.id"));
	}

}
