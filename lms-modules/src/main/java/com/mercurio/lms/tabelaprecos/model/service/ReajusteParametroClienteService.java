package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.AtualizarReajusteDTO;
import com.mercurio.lms.tabelaprecos.model.CloneClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.ParametroClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteGeneralidadeCliente;
import com.mercurio.lms.tabelaprecos.model.ReajusteServicoAdicionalCliente;
import com.mercurio.lms.tabelaprecos.model.ReajusteTaxaCliente;
import com.mercurio.lms.tabelaprecos.model.dao.ParametroReajusteTabelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.ReajusteTabelaPrecoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.dao.GeneralidadeClienteDAO;
import com.mercurio.lms.vendas.model.dao.ParametroClienteDAO;
import com.mercurio.lms.vendas.model.dao.ServicoAdicionalClienteDAO;
import com.mercurio.lms.vendas.model.dao.TaxaClienteDAO;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

public class ReajusteParametroClienteService {

	private ParametroClienteDAO parametroClienteDAO;
	private ReajusteTabelaPrecoDAO reajusteTabelaPrecoDAO;
	private ParametroReajusteTabelaPrecoDAO parametroReajusteTabelaPrecoDAO;
	private GeneralidadeClienteDAO generalidadeClienteDAO;
	private TaxaClienteDAO taxaClienteDAO;
	private ServicoAdicionalClienteDAO servicoAdicionalClienteDAO;
	private GrupoRegiaoService grupoRegiaoService;
	
	private static final  Long    PERCENT_100 = 100L;
	private static final  Integer FIRST= 0;
	private static final  String Q = "Q";
	private static final  String X = "X";
	private static final  String O = "O";
	private static final  String P = "P";
	private static final  String F = "F";
	private static final  String D = "D";
	private static final  String DESCONTO = "D";
	private static final  String ACRESCIMO = "A";
	private static final  String TABELA = "T";
	private static final  String VALOR = "V";
	private static final  String AUTOMATICO = "A";
	private static final  String REAJUSTE_CLIENTE_MANUAL = "R";
	private static final  String[] INDICADOR_CALCULO_PEDAGIO_PARAMETRO = { DESCONTO, ACRESCIMO, TABELA, VALOR };
	private static final  String[] CALCULO_PEDAGIO_TAB_PRECO = { P, F, D, X };
	private static final  String[] INDICADOR_PEDAGIO_PARAM_CLIENTE = { Q, X, O };
	private static final  String EMPTY_PARAMS  = "Nenhum parametro cliente para idTabelaDivisaoCliente: ";
	private static final  String EMPTY_PERCENT = "Nenhum percentual reajuste para idTabelaDivisaoCliente: ";
	private static final Log LOG = LogFactory.getLog(ReajusteParametroClienteService.class);
	

	public boolean executeClone(CloneClienteAutomaticoDTO reajusteClienteAutomaticoDTO) {
		List<ParametroCliente> paramsCliente = parametroClienteDAO.findByIdTabelaDivisaoCliente(reajusteClienteAutomaticoDTO.getIdTabelaDivisaoCliente(), new DomainValue(AUTOMATICO), JTDateTimeUtils.getDataAtual());
		ParametroCliente clone  = null;
		
		if(CollectionUtils.isEmpty(paramsCliente)){
			LOG.error(EMPTY_PARAMS + reajusteClienteAutomaticoDTO.getIdTabelaDivisaoCliente());
			return Boolean.FALSE;
		}
		
		for (ParametroCliente parametroCliente : paramsCliente) {
			clone = new ParametroCliente();
			ParametroClienteUtils.copyParametroClienteCompleto(parametroCliente, clone);
			clone.setGrupoRegiaoOrigem(getGrupoRegiao(parametroCliente.getGrupoRegiaoOrigem(), reajusteClienteAutomaticoDTO.getIdTabelaNova()));
			clone.setGrupoRegiaoDestino(getGrupoRegiao(parametroCliente.getGrupoRegiaoDestino(), reajusteClienteAutomaticoDTO.getIdTabelaNova()));
			
			if(reajusteClienteAutomaticoDTO.getTipo() == null || !REAJUSTE_CLIENTE_MANUAL.equalsIgnoreCase(reajusteClienteAutomaticoDTO.getTipo())){
				parametroClienteDAO.storeEncerrandoParametro(parametroCliente, reajusteClienteAutomaticoDTO.getDataVigenciaInicial());
			}
			
			parametroClienteDAO.store(updateParamClienteClone(clone, reajusteClienteAutomaticoDTO), true);
		}
		
		return Boolean.TRUE;
	}
	
	private GrupoRegiao getGrupoRegiao(GrupoRegiao grupoRegiao, Long idTabelaNova){
		if(grupoRegiao == null){
			return null;
		}
		
		return grupoRegiaoService.findByGrupoTabela(grupoRegiao.getIdGrupoRegiao(), idTabelaNova);
	}
		
	private ParametroCliente updateParamClienteClone(ParametroCliente parametroCliente, CloneClienteAutomaticoDTO reajusteClienteAutomaticoDTO) {
		parametroCliente.setDtVigenciaInicial(reajusteClienteAutomaticoDTO.getDataVigenciaInicial());
		parametroCliente.setTpSituacaoParametro(new DomainValue("I"));
		parametroCliente.setDtVigenciaFinal(null);
		return parametroCliente;
	}
	
	public List<CloneClienteAutomaticoDTO> findTabelasDivisaoClienteAutomaticosParaClonar(){
		return reajusteTabelaPrecoDAO.findTabelasDivisaoClienteAutomaticosParaClonar();
	}
	public List<CloneClienteAutomaticoDTO> findTabelasDivisaoClienteAutomaticosParaClonarNovoReajuste(){
		return reajusteTabelaPrecoDAO.findTabelasDivisaoClienteAutomaticosParaClonarNovoReajuste();
	}
	
	public List<Long> findTabelasDivisaoClienteAutomaticosParaReajustar(){
		return reajusteTabelaPrecoDAO.findTabelasDivisaoClienteAutomaticosParaReajustar();
	}
	
	public boolean executeReajustarClienteAutomatico(ReajusteClienteAutomaticoDTO parametroCliente, boolean novoReajuste) {
		LOG.info("ExecuteReajustarClienteAutomatico inicializado para Tabela_Divisao_Cliente: " + parametroCliente.getIdTabelaDivisaoCliente());
		
		List<Long> parametrosClonadosParaReajustar = reajusteTabelaPrecoDAO.findParametrosClonadosParaReajustar(parametroCliente.getIdTabelaDivisaoCliente());
		if(CollectionUtils.isEmpty(parametrosClonadosParaReajustar)){
			LOG.error(EMPTY_PARAMS + parametroCliente.getIdTabelaDivisaoCliente());
			return Boolean.FALSE;
		}

		List<ParametroCliente> listParamClientes = findAllParametros(parametrosClonadosParaReajustar);
		
		List<ParametroClienteAutomaticoDTO> percentuaisParametros = getPercentuaisParametro(parametroCliente.getIdTabelaDivisaoCliente(), parametroCliente.getTipo(), listParamClientes.get(FIRST).getDtVigenciaInicial(), novoReajuste); 
		if(CollectionUtils.isEmpty(percentuaisParametros)){
			LOG.error(EMPTY_PERCENT + parametroCliente.getIdTabelaDivisaoCliente());
			return Boolean.FALSE;
		}

		ParametroClienteAutomaticoDTO percentuais = percentuaisParametros.get(FIRST);

		reajusteServicoAdicionalCliente(parametroCliente.getIdTabelaDivisaoCliente(), percentuais.getIdReajusteTabPreco(), parametroCliente.getTipo()); 
		
		boolean retorno = reajusteParamCliente(listParamClientes, percentuais, parametroCliente.getTipo());
		
		LOG.info("ExecuteReajustarClienteAutomatico finalizado para Tabela_Divisao_Cliente: " + parametroCliente.getIdTabelaDivisaoCliente());		
		
		return retorno;
	}
	
	private List<ParametroClienteAutomaticoDTO> getPercentuaisParametro(Long idTabelaDivisaoCliente, String tipoReajuste, YearMonthDay dataVigenciaInial, boolean novoReajuste){
		if(REAJUSTE_CLIENTE_MANUAL.equalsIgnoreCase(tipoReajuste)){
			return parametroReajusteTabelaPrecoDAO.percentuaisToReajusteClienteManual(idTabelaDivisaoCliente, dataVigenciaInial);
		}
		return parametroReajusteTabelaPrecoDAO.percentuaisToReajusteClienteAutomaticos(idTabelaDivisaoCliente, novoReajuste);
	}
	
	private boolean reajusteParamCliente(List<ParametroCliente> listParamClientes, ParametroClienteAutomaticoDTO percentuaisParametros, String tipo){
		List<ParametroCliente> resultadoReajuste = reajustarParametroCliente(listParamClientes, percentuaisParametros, tipo);
		return storeReajuste(resultadoReajuste);
	}

	private boolean storeReajuste(List<ParametroCliente> resultadoReajuste) {
		for (ParametroCliente parametroCliente : resultadoReajuste) {
			parametroClienteDAO.store(parametroCliente);
		}
		return Boolean.TRUE;
	}
	
	public List<ParametroCliente> reajustarParametroCliente(List<ParametroCliente> listParamClientes, ParametroClienteAutomaticoDTO percentuaisParametros, String tipoSituacaoParam) {
		for(ParametroCliente parametro : listParamClientes){
			parametro.setTpSituacaoParametro(new DomainValue(tipoSituacaoParam));
			parametro.setVlMinimoFreteQuilo(calculaValorReajuste(parametro.getVlMinimoFreteQuilo(), percentuaisParametros.getPercentualMinFreteQuilo()));
			parametro.setPcReajVlMinimoFreteQuilo(percentuaisParametros.getPercentualMinFreteQuilo()); 
			parametro.setPcFretePercentual(calculaValorReajuste(parametro.getPcFretePercentual(), percentuaisParametros.getPercentualFrete()));
			reajusteMinFrete(percentuaisParametros, parametro);
			reajusteTonelada(percentuaisParametros, parametro);
			reajusteAdvalorem(percentuaisParametros, parametro);
			reajusteMinimoGris(percentuaisParametros, parametro);
			reajusteTDE(percentuaisParametros, parametro);
			reajusteMinimoProgr(percentuaisParametros, parametro);
			reajusteTRT(percentuaisParametros, parametro);
			reajusteMinimoFretePeso(percentuaisParametros, parametro);
			reajusteGris(percentuaisParametros, parametro);
			reajusteEspecifica(percentuaisParametros, parametro); 
			reajusteMinimoTDE(percentuaisParametros, parametro);
			reajusteMinimoTRT(percentuaisParametros, parametro);
			reajusteAdvalorem2(percentuaisParametros, parametro);
			reajusteFretePeso(percentuaisParametros, parametro);
			reajusteTarifaMinima(percentuaisParametros, parametro);
			reajustePedagio(percentuaisParametros, parametro);
			reajusteGeneralidadeCliente(parametro.getIdParametroCliente(), percentuaisParametros.getIdReajusteTabPreco(), tipoSituacaoParam);
			reajusteTaxaCliente(parametro.getIdParametroCliente(), percentuaisParametros.getIdReajusteTabPreco(), tipoSituacaoParam);
		}
		return listParamClientes;
	}
	
	private void reajusteGeneralidadeCliente(Long idParamCliente, Long idReajusteTabPreco, String tipoParam){
		List<GeneralidadeCliente> listGeneralidadeCliente = generalidadeClienteDAO.findGeneralidadeClienteByIdParamCliente(idParamCliente);
		
		if(CollectionUtils.isNotEmpty(listGeneralidadeCliente)) {
			List<ReajusteGeneralidadeCliente> percentuaisGeneralidade = getPercentuaisToReajusteGeneralidade(idReajusteTabPreco, idParamCliente, tipoParam);
			for (GeneralidadeCliente generalidade : listGeneralidadeCliente) {
				updateGeneralidadeCliente(generalidade, percentuaisGeneralidade);
			}
		}
	}
	
	private List<ReajusteGeneralidadeCliente> getPercentuaisToReajusteGeneralidade(Long idReajusteTabPreco, Long idParamCliente, String tipoReajuste){
		if(REAJUSTE_CLIENTE_MANUAL.equalsIgnoreCase(tipoReajuste)){
			return parametroReajusteTabelaPrecoDAO.percentuaisToReajusteManualGeneralidadeCliente(idParamCliente);
		}
		return parametroReajusteTabelaPrecoDAO.percentuaisToReajusteGeneralidadeCliente(idReajusteTabPreco, idParamCliente);
	}
	
	public void updateGeneralidadeCliente(GeneralidadeCliente generalidade, List<ReajusteGeneralidadeCliente> percentuaisGeneralidade){
		for (ReajusteGeneralidadeCliente reajusteGeneralidadeCliente : percentuaisGeneralidade) {
			if(generalidade.getIdGeneralidadeCliente().longValue() == reajusteGeneralidadeCliente.getIdGeneralidadeCliente().longValue()){
				reajusteGeneralidade(generalidade, reajusteGeneralidadeCliente);
				reajusteMinGeneralidade(generalidade, reajusteGeneralidadeCliente);
				generalidadeClienteDAO.store(generalidade);
			}
		}
	}

	private void reajusteTaxaCliente(Long idParamCliente, Long idReajusteTabPreco, String tipoParam){
		List<TaxaCliente> listTaxaCliente = taxaClienteDAO.findTaxaClienteByIdParamCliente(idParamCliente);
		
		if(CollectionUtils.isNotEmpty(listTaxaCliente)) {
			List<ReajusteTaxaCliente> percentuais = getPercentuaisTaxaCliente(idParamCliente, idReajusteTabPreco, tipoParam);
			for (TaxaCliente taxa : listTaxaCliente) {
				updateTaxaCliente(taxa, percentuais);
			}
		}
	}

	private List<ReajusteTaxaCliente> getPercentuaisTaxaCliente(
			Long idParamCliente, Long idReajusteTabPreco, String tipoReajuste) {
		if(REAJUSTE_CLIENTE_MANUAL.equalsIgnoreCase(tipoReajuste)){
			return parametroReajusteTabelaPrecoDAO.percentuaisToReajusteTaxaClienteManual(idParamCliente);
		}
		return parametroReajusteTabelaPrecoDAO.percentuaisToReajusteTaxaCliente(idReajusteTabPreco, idParamCliente);
	}
	
	public void updateTaxaCliente(TaxaCliente taxa, List<ReajusteTaxaCliente> percentuais){
		for (ReajusteTaxaCliente reajusteTaxaCliente : percentuais) {
			if(taxa.getIdTaxaCliente().longValue() == reajusteTaxaCliente.getIdTaxaCliente().longValue()){
				reajusteTaxa(taxa, reajusteTaxaCliente);
				reajusteMinTaxa(taxa, reajusteTaxaCliente);
				taxaClienteDAO.store(taxa);
			}
		}
	}
	
	private void reajusteMinFrete(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		parametro.setVlMinimoFretePercentual(calculaValorReajuste(parametro.getVlMinimoFretePercentual(), percentuaisParametros.getPercentualMinFrete()));
		parametro.setPcReajVlMinimoFretePercen(percentuaisParametros.getPercentualMinFrete());
	}
	
	private void reajusteServicoAdicionalCliente(Long idTabelaDivisaoCliente, Long idReajusteTabPreco, String tipoReajuste){
		List<ReajusteServicoAdicionalCliente> reajusteServicoAdicionalCliente = parametroReajusteTabelaPrecoDAO.percentuaisToReajusteServicoAdiconalCliente(idTabelaDivisaoCliente, idReajusteTabPreco);
		if(CollectionUtils.isNotEmpty(reajusteServicoAdicionalCliente)){
			processReajusteServicoAdicionalCliente(reajusteServicoAdicionalCliente, idReajusteTabPreco, tipoReajuste);
		}
	}
	
	private boolean processReajusteServicoAdicionalCliente(List<ReajusteServicoAdicionalCliente> reajusteServicoAdicionalCliente, Long idReajusteCliente, String tipoReajuste) {
		for (ReajusteServicoAdicionalCliente reajServAdicionalCliente : reajusteServicoAdicionalCliente) {
			BigDecimal valorReajuste = reajusteValorServAdicionalCliente(reajServAdicionalCliente.getValorReajuste(), reajServAdicionalCliente.getPercentualReajuste(), reajServAdicionalCliente.getTpIndicador());
			BigDecimal valorMinReajuste = reajusteValorServAdicionalCliente(reajServAdicionalCliente.getValorMinReajuste(), reajServAdicionalCliente.getPercentualMinReajuste(), reajServAdicionalCliente.getTpIndicador());
			storeReajusteServicoAdicionalCliente(idReajusteCliente, reajServAdicionalCliente.getIdServAdicionalCliente(), valorReajuste, valorMinReajuste, reajServAdicionalCliente.getPercentualMinReajuste(), reajServAdicionalCliente.getPercentualReajuste(), tipoReajuste);
		}
		return Boolean.TRUE;
	}
	
	private void storeReajusteServicoAdicionalCliente(Long idReajusteCliente, Long idServAdicionalCliente, BigDecimal valorReajuste, BigDecimal valorMinReajuste, BigDecimal pcReajusteValMinParcela, BigDecimal pcReajusteValorParcela, String tipoReajuste){
		if(REAJUSTE_CLIENTE_MANUAL.equalsIgnoreCase(tipoReajuste)){
			servicoAdicionalClienteDAO.insertServicoAdicionalReajusteCliente(idReajusteCliente, idServAdicionalCliente, pcReajusteValMinParcela, pcReajusteValorParcela, valorMinReajuste, valorReajuste);
		} else {
			servicoAdicionalClienteDAO.updateValorAndValorMinById(idServAdicionalCliente, valorReajuste, valorMinReajuste);
		}
	}

	private void reajusteTonelada(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		parametro.setVlToneladaFretePercentual(calculaValorReajuste(parametro.getVlToneladaFretePercentual(), percentuaisParametros.getPercentualTonelada()));
		parametro.setPcReajVlToneladaFretePerc(percentuaisParametros.getPercentualTonelada());
	}

	private void reajustePedagio(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		BigDecimal percentualPedagioPadrao = getPercentualPedagio(parametro, percentuaisParametros);
		parametro.setVlPedagio(calcularPedagio(parametro.getTpIndicadorPedagio(), percentuaisParametros.getValorTabelaPedagio(), parametro.getVlPedagio(), percentualPedagioPadrao));
		parametro.setPcReajPedagio(percentualPedagioPadrao);
		
		if(isIndicadorCalculoPedagioParametro(parametro.getTpIndicadorPedagio())){
			parametro.setTpIndicadorPedagio(new DomainValue(VALOR));
		}
	}
	
	private BigDecimal calcularPedagio(DomainValue tpIndicador, BigDecimal valorTabelaPedagio, BigDecimal valorPedagio, BigDecimal percentualPedagio){
		if(isIndicadorCalculoPedagioParametro(tpIndicador)){ 
			return calculoValorGeneralidadeByTpIndicador(tpIndicador, valorTabelaPedagio, valorPedagio, percentualPedagio);
		}
		return calculaValorReajuste(valorPedagio, percentualPedagio);
	}
	
	private boolean isIndicadorCalculoPedagioParametro(DomainValue tpIndicador){
		return tpIndicador != null && Arrays.asList(INDICADOR_CALCULO_PEDAGIO_PARAMETRO).contains(tpIndicador.getValue().toUpperCase());
	}
	
	private BigDecimal reajusteValorServAdicionalCliente(BigDecimal valor, BigDecimal percentual, String tpIndicador){
		if(VALOR.equals(tpIndicador)){
			return calculaValorReajuste(valor, percentual);
		}
		return valor;
	}

	private void reajusteTarifaMinima(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		if(deveReajustar(parametro.getTpTarifaMinima())){
			parametro.setVlTarifaMinima(calculaValorReajuste(parametro.getVlTarifaMinima(), percentuaisParametros.getPercentualTarifaMinima()));
		}
		parametro.setPcReajTarifaMinima(percentuaisParametros.getPercentualTarifaMinima());
	}
	
	private void reajusteEspecifica(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		if(deveReajustar(parametro.getTpIndicVlrTblEspecifica())){
			parametro.setVlTblEspecifica(calculaValorReajuste(parametro.getVlTblEspecifica(), percentuaisParametros.getPercentualEspecifica()));
		}
		parametro.setPcReajVlTarifaEspecifica(percentuaisParametros.getPercentualEspecifica());
	}

	private void reajusteFretePeso(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		if(deveReajustar(parametro.getTpIndicadorFretePeso())){
			parametro.setVlFretePeso(calculaValorReajuste(parametro.getVlFretePeso(), percentuaisParametros.getPercentualFretePeso()));
		}
		parametro.setPcReajFretePeso(percentuaisParametros.getPercentualFretePeso());
	}

	private void reajusteAdvalorem2(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		if(deveReajustar(parametro.getTpIndicadorAdvalorem2())){
			parametro.setVlAdvalorem2(calculaValorReajuste(parametro.getVlAdvalorem2(), percentuaisParametros.getPercentualAdvalorem2()));
		}
		parametro.setPcReajAdvalorem2(percentuaisParametros.getPercentualAdvalorem2());
	}

	private void reajusteMinimoTRT(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		BigDecimal valor = calculoValorGeneralidadeByTpIndicador(parametro.getTpIndicadorMinimoTrt(), percentuaisParametros.getValorMinTabelaTRT(), parametro.getVlMinimoTrt(), percentuaisParametros.getPercentualMinTRT());
		parametro.setVlMinimoTrt(valor);
		parametro.setPcReajMinimoTrt(percentuaisParametros.getPercentualMinTRT());
		parametro.setTpIndicadorMinimoTrt(new DomainValue(VALOR)); 
	}

	private void reajusteMinimoTDE(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		BigDecimal valor = calculoValorGeneralidadeByTpIndicador(parametro.getTpIndicadorMinimoTde(), percentuaisParametros.getValorMinTabelaTDE(), parametro.getVlMinimoTde(), percentuaisParametros.getPercentualMinTDE());
		parametro.setVlMinimoTde(valor);
		parametro.setPcReajMinimoTde(percentuaisParametros.getPercentualMinTDE());
		parametro.setTpIndicadorMinimoTde(new DomainValue(VALOR));
	}

	private void reajusteGris(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		BigDecimal valor = calculoValorGeneralidadeByTpIndicador(parametro.getTpIndicadorPercentualGris(), percentuaisParametros.getValorTabelaGris(), parametro.getVlPercentualGris(), percentuaisParametros.getPercentualGris());
		parametro.setVlPercentualGris(valor);
		parametro.setPcReajPercentualGris(percentuaisParametros.getPercentualGris());
		parametro.setTpIndicadorPercentualGris(new DomainValue(VALOR)); 
	}

	private void reajusteMinimoFretePeso(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		if(deveReajustar(parametro.getTpIndicadorMinFretePeso())){
			parametro.setVlMinFretePeso(calculaValorReajuste(parametro.getVlMinFretePeso(), percentuaisParametros.getPercentualMinFretePeso()));
		}
		parametro.setPcReajVlMinimoFretePeso(percentuaisParametros.getPercentualMinFretePeso()); 
	}

	private void reajusteTRT(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		BigDecimal valor = calculoValorGeneralidadeByTpIndicador(parametro.getTpIndicadorPercentualTrt(), percentuaisParametros.getValorTabelaTRT(), parametro.getVlPercentualTrt(), percentuaisParametros.getPercentualTRT());
		parametro.setVlPercentualTrt(valor);
		parametro.setPcReajPercentualTRT(percentuaisParametros.getPercentualTRT());
		parametro.setTpIndicadorPercentualTrt(new DomainValue(VALOR)); 
	}

	private void reajusteMinimoProgr(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		if(deveReajustar(parametro.getTpIndicadorPercMinimoProgr())){
			parametro.setVlPercMinimoProgr(calculaValorReajuste(parametro.getVlPercMinimoProgr(), percentuaisParametros.getPercentualMinProgr()));
		}
		parametro.setPcReajVlMinimoProg(percentuaisParametros.getPercentualMinProgr()); 
	}

	private void reajusteTDE(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		BigDecimal valor = calculoValorGeneralidadeByTpIndicador(parametro.getTpIndicadorPercentualTde(), percentuaisParametros.getValorTabelaTDE(), parametro.getVlPercentualTde(), percentuaisParametros.getPercentualTDE());
		parametro.setVlPercentualTde(valor);
		parametro.setPcReajPercentualTDE(percentuaisParametros.getPercentualTDE());
		parametro.setTpIndicadorPercentualTde(new DomainValue(VALOR)); 
	}

	private void reajusteMinimoGris(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		BigDecimal valor = calculoValorGeneralidadeByTpIndicador(parametro.getTpIndicadorMinimoGris(), percentuaisParametros.getValorMinTabelaGris(), parametro.getVlMinimoGris(), percentuaisParametros.getPercentualMinGris());
		parametro.setVlMinimoGris(valor);
		parametro.setPcReajMinimoGris(percentuaisParametros.getPercentualMinGris());
		parametro.setTpIndicadorMinimoGris(new DomainValue(VALOR)); 
	}

	private void reajusteAdvalorem(ParametroClienteAutomaticoDTO percentuaisParametros, ParametroCliente parametro) {
		if(deveReajustar(parametro.getTpIndicadorAdvalorem())){
			parametro.setVlAdvalorem(calculaValorReajuste(parametro.getVlAdvalorem(), percentuaisParametros.getPercentualAdvalorem()));
		}
		parametro.setPcReajAdvalorem(percentuaisParametros.getPercentualAdvalorem());
	}
	
	private void reajusteMinGeneralidade(GeneralidadeCliente generalidade, ReajusteGeneralidadeCliente reajusteGeneralidadeCliente) {
		BigDecimal valor = calculoValorGeneralidadeByTpIndicador(generalidade.getTpIndicadorMinimo(), reajusteGeneralidadeCliente.getValorMinTabela(), generalidade.getVlMinimo(), reajusteGeneralidadeCliente.getPercentualMinReajuste());
		generalidade.setPcReajMinimo(reajusteGeneralidadeCliente.getPercentualMinReajuste() == null ? BigDecimal.ZERO : reajusteGeneralidadeCliente.getPercentualMinReajuste());
		generalidade.setVlMinimo(valor);
		generalidade.setTpIndicadorMinimo(new DomainValue(VALOR));
	}

	private void reajusteGeneralidade(GeneralidadeCliente generalidade, ReajusteGeneralidadeCliente reajusteGeneralidadeCliente) {
		BigDecimal valor = calculoValorGeneralidadeByTpIndicador(generalidade.getTpIndicador(), reajusteGeneralidadeCliente.getValorTabela(), generalidade.getVlGeneralidade(), reajusteGeneralidadeCliente.getPercentualReajuste());
		generalidade.setPcReajGeneralidade(reajusteGeneralidadeCliente.getPercentualReajuste());
		generalidade.setVlGeneralidade(valor);
		generalidade.setTpIndicador(new DomainValue(VALOR)); 
	}
	
	public BigDecimal calculoValorGeneralidadeByTpIndicador(DomainValue tpIndicador, BigDecimal valorTabela, BigDecimal valorGeneralidade, BigDecimal percentualGeneralidade){
		BigDecimal vlTabela = getValidValorTabela(valorTabela);
		
		if(tpIndicador == null){
			return BigDecimal.ZERO;
		}
		
		if(DESCONTO.equalsIgnoreCase(tpIndicador.getValue())){
			return calculaValorReajuste( calculaDescontoValorReajuste(vlTabela, valorGeneralidade), percentualGeneralidade );
		}
		
		if(ACRESCIMO.equalsIgnoreCase(tpIndicador.getValue())){
			return calculaValorReajuste( calculaValorReajuste(vlTabela, valorGeneralidade), percentualGeneralidade );
		}
		
		if(TABELA.equalsIgnoreCase(tpIndicador.getValue())){
			return calculaValorReajuste( vlTabela, percentualGeneralidade );
		}
		
		if(VALOR.equalsIgnoreCase(tpIndicador.getValue())){
			return calculaValorReajuste(valorGeneralidade, percentualGeneralidade);
		}
		
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getValidValorTabela(BigDecimal valorTabela){
		return valorTabela == null ? BigDecimal.ZERO : valorTabela;
	}

	private void reajusteTaxa(TaxaCliente taxa, ReajusteTaxaCliente reajusteTaxaCliente) {
		if(deveReajustar(taxa.getTpTaxaIndicador())){
			taxa.setVlTaxa(calculaValorReajuste(taxa.getVlTaxa(), reajusteTaxaCliente.getPercentualReajuste()));
		}
		taxa.setPcReajTaxa(reajusteTaxaCliente.getPercentualReajuste());
	}
	
	private void reajusteMinTaxa(TaxaCliente taxa, ReajusteTaxaCliente reajusteTaxaCliente) {
		if(deveReajustar(taxa.getTpTaxaIndicador())){
			taxa.setVlExcedente(calculaValorReajuste(taxa.getVlExcedente(), reajusteTaxaCliente.getPercentualMinReajuste())); 
		}
		taxa.setPcReajVlExcedente(reajusteTaxaCliente.getPercentualMinReajuste()); 
	}
	
	public boolean deveReajustar(DomainValue domain){
		return domain != null && VALOR.equalsIgnoreCase(domain.getValue());
	}
	
	public BigDecimal getPercentualPedagio(ParametroCliente parametro, ParametroClienteAutomaticoDTO percentuaisParametros){
		if(isIndicadorPedagioParamCliente(parametro.getTpIndicadorPedagio())){
			return getIndicadorPedagioParamCliente(parametro.getTpIndicadorPedagio().getValue(), percentuaisParametros);
		} 
		
		if(isCalculoPedagioTabPreco(percentuaisParametros.getTpPedagioPadrao())){
			return getCalculoPedagioTabPreco(percentuaisParametros);
		}
		
		return null;
	}
	
	public boolean isIndicadorPedagioParamCliente(DomainValue typeDomain){
		return typeDomain != null && Arrays.asList(INDICADOR_PEDAGIO_PARAM_CLIENTE).contains(typeDomain.getValue().toUpperCase());
	}
	
	public BigDecimal getIndicadorPedagioParamCliente(String type, ParametroClienteAutomaticoDTO percentuaisParametros){
		if(Q.equalsIgnoreCase(type)){
			return percentuaisParametros.getPercentualPedagioFracao();
		}
		
		if(X.equalsIgnoreCase(type)){
			return percentuaisParametros.getPercentualPedagioFaixaPeso();
		}
			
		return percentuaisParametros.getPercentualPedagioPostoFracao();
	}

	public boolean isCalculoPedagioTabPreco(String type){
		return type != null && Arrays.asList(CALCULO_PEDAGIO_TAB_PRECO).contains(type.toUpperCase());
	}
	
	public BigDecimal getCalculoPedagioTabPreco(ParametroClienteAutomaticoDTO percentuaisParametros){
		if(P.equalsIgnoreCase(percentuaisParametros.getTpPedagioPadrao())){
			return percentuaisParametros.getPercentualPedagioPostoFracao();
		}
		
		if(F.equalsIgnoreCase(percentuaisParametros.getTpPedagioPadrao())){
			return percentuaisParametros.getPercentualPedagioFracao();
		}
		
		if(D.equalsIgnoreCase(percentuaisParametros.getTpPedagioPadrao())){
			return percentuaisParametros.getPercentualPedagioDocumento();
		}
		
		return percentuaisParametros.getPercentualPedagioFaixaPeso();
	}
	
	public BigDecimal calculaValorReajuste(BigDecimal valor, BigDecimal percentual) {
		if(valor == null || percentual == null){
			return valor;
		}
		
		return valor.add((valor.multiply(percentual)).divide(BigDecimal.valueOf(PERCENT_100)));
	}
	
	public BigDecimal calculaDescontoValorReajuste(BigDecimal valor, BigDecimal percentual) {
		if(valor == null || percentual == null){
			return valor;
		}
		
		return valor.subtract((valor.multiply(percentual)).divide(BigDecimal.valueOf(PERCENT_100)));
	}

	private List<ParametroCliente> findAllParametros(List<Long> parametrosClonadosParaReajustar) {
		List<ParametroCliente> list = new ArrayList<ParametroCliente>();
		for (Long id : parametrosClonadosParaReajustar) {
			list.add(parametroClienteDAO.findParametroById(id));
		}
		return list;
	}

	public Boolean updateTabelasDivisaoEHistoricoReajuste(boolean isNovoReajuste) {
		List<AtualizarReajusteDTO> tabelasDivisao = parametroReajusteTabelaPrecoDAO.findTabelasDivisaoComReajusteParaADataAtual(isNovoReajuste);
		updateTabelasDivisao(tabelasDivisao);

		for(AtualizarReajusteDTO tabelaDivisao : tabelasDivisao){
			parametroReajusteTabelaPrecoDAO.insertHistoricoReajuste(tabelaDivisao, AUTOMATICO);
		}

		return true;
	}

	public void updateTabelasDivisao(List<AtualizarReajusteDTO> tabelasDivisao) {
		for(AtualizarReajusteDTO tabelaDivisao : tabelasDivisao){
			if(tabelaDivisao.getIdTabelaFob() != null){
				parametroReajusteTabelaPrecoDAO.updateTabelaDivisaoFOB(tabelaDivisao);
			}else{
				parametroReajusteTabelaPrecoDAO.updateTabelaDivisao(tabelaDivisao);
			}
		}
	}
	
	public void saveHistorico(Long id){
		List<AtualizarReajusteDTO> reajustesDTO = parametroReajusteTabelaPrecoDAO.findDadosAtualizarHistoricoReajuste(id);
		if(CollectionUtils.isNotEmpty(reajustesDTO)){
			AtualizarReajusteDTO reajusteDTO = reajustesDTO.get(0);
			reajusteDTO.setIdTabelaDivisao(id);
			parametroReajusteTabelaPrecoDAO.insertHistoricoReajuste(reajusteDTO, "P");
		}
	}
	
	public void setParametroClienteDAO(ParametroClienteDAO parametroClienteDAO) {
		this.parametroClienteDAO = parametroClienteDAO;
	}
		
	public void setReajusteTabelaPrecoDAO(ReajusteTabelaPrecoDAO reajusteTabelaPrecoDAO) {
		this.reajusteTabelaPrecoDAO = reajusteTabelaPrecoDAO;
	}
	
	public void setParametroReajusteTabelaPrecoDAO(ParametroReajusteTabelaPrecoDAO parametroReajusteTabelaPrecoDAO) {
		this.parametroReajusteTabelaPrecoDAO = parametroReajusteTabelaPrecoDAO;
	}
	
	public void setGeneralidadeClienteDAO(GeneralidadeClienteDAO generalidadeClienteDAO) {
		this.generalidadeClienteDAO = generalidadeClienteDAO;
	}

	public void setTaxaClienteDAO(TaxaClienteDAO taxaClienteDAO) {
		this.taxaClienteDAO = taxaClienteDAO;
	}
	
	public void setServicoAdicionalClienteDAO(ServicoAdicionalClienteDAO servicoAdicionalClienteDAO) {
		this.servicoAdicionalClienteDAO = servicoAdicionalClienteDAO;
	}
	
	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}
}
