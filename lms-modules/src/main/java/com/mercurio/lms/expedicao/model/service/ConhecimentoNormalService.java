package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.ParcelaServicoAdicional;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.WarningCollector;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para CRUD:   
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.conhecimentoNormalService"
 */
public class ConhecimentoNormalService extends AbstractConhecimentoNormalService {
	private ParcelaPrecoService parcelaPrecoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ConteudoParametroFilialService conteudoParametroFilialService;

	/**
	 * Seta dados necessarios ao conhecimento 
	 * 
	 * @author Andre Valadas
	 * @param conhecimento
	 */
	public void validateCtrcPrimeiraFase(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		validatePrimeiraFase(conhecimento, calculoFrete);
		validateCTRCSubcontratante(conhecimento);
	}

	/**
	 * Validações
	 * 
	 * @author Andre Valadas
	 * @param conhecimento
	 */
	public void validateCtrcSegundaFase(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		validateSegundaFase(conhecimento, calculoFrete);
	}

	/*******************************************************************
	 * Métodos relacionados ao calculo do frete do conhecimento (CTRC) *
	 *******************************************************************
	 * Método que executa o calculo manual do frete.
	 * - Verificar se o valor calculado como Frete Peso está menor que o valor mínimo de 
	 * 	frete (PARAMETRO_GERAL.DS_CONTEUDO) cadastrado na tabela PARAMETRO_GERAL 
	 * (PARAMETRO_ GERAL.NM_PARAMETRO_GERAL = "VLR_MINIMO_FRETE"), se for menor visualizar mensagem LMS-04081;
	 * - Calcular o total do frete com o mesmo valor visualizado na linha da GRID Cálculo do frete;
	 * - Calcular o total dos serviços como o somatório do campo Valor da Grid Serviço Adicional;
	 * - Apresentar o total do CTRC como a soma dos valores do total do frete e do total dos serviços;
	 * 
	 * autor Julio Cesar Fernandes Corrêa
	 * @since 19/12/2005
	 * @param calculoFrete
	 * @param conhecimento
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	public void validateCalculoFreteManual(CalculoFrete calculoFrete, Conhecimento conhecimento, Map<String, Object> parameters) {
		calculoFrete.resetParcelas();
		calculoFrete.resetParcelasGerais();
		calculoFrete.resetServicosAdicionais();
		calculoFrete.resetValores();
		
		validateClearObservacaoDoctoServicos(calculoFrete, conhecimento);
		
		calculoFrete.clearObservacoes();

		TypedFlatMap param = new TypedFlatMap(parameters);
		
		/*Parcela Serviço*/
		List<Map<String, Object>> parcelas = param.getList("parcela");
		if(parcelas != null && !parcelas.isEmpty()){
			for(Map<String, Object> parc : parcelas){
				
				/*Valor total do frete*/
				BigDecimal vlParcela = BigDecimal.ZERO;
				if (parc.get("vlParcela") != null) {
					vlParcela = BigDecimalUtils.getBigDecimal(parc.get("vlParcela"));
				}
				
				/*id Parcela preço*/
				Long idParcelaPreco = LongUtils.getLong(parc.get("id"));
				
				/*Obtem Parcela preço*/
				ParcelaPreco parcelaPreco = parcelaPrecoService.findById(idParcelaPreco);
				if(ConstantesExpedicao.CD_DESCONTO.equals(parcelaPreco.getCdParcelaPreco())){
					calculoFrete.setVlDesconto(vlParcela);
				}else{				
					/*Adiciona a parcela servico ao calculo de frete*/
					ParcelaServico parcelaServico = new ParcelaServico(parcelaPreco, vlParcela, vlParcela);
			calculoFrete.addParcelaGeral(parcelaServico);
		}
			}
		}
		
		/*Parcelas Servico Adicional*/
		List<Map<String, Object>> servicos = param.getList("servico");
		if(servicos != null && !servicos.isEmpty()){
			for(Map<String, Object> serv : servicos) {
				
				/*Valor do serviço*/
				BigDecimal vlServico = BigDecimalUtils.getBigDecimal(serv.get("vlServico"));
								
				/*Id parcela preço*/
				Long idServicoAdicional = LongUtils.getLong(serv.get("id"));
				
				/*Obtem Parcela preço*/
				ParcelaPreco parcelaPreco = parcelaPrecoService.findByIdServicoAdicional(idServicoAdicional);
				
				/*Adiciona a parcela servico ao calculo de frete*/
				ParcelaServicoAdicional parcelaServicoAdicional = new ParcelaServicoAdicional(parcelaPreco);
				parcelaServicoAdicional.setVlUnitarioParcela(vlServico);
				parcelaServicoAdicional.setVlBrutoParcela(vlServico);
				calculoFrete.addServicoAdicional(parcelaServicoAdicional);
			}
		}
		
		calculoFrete.setBlCalculaImpostoServico(true);
		executeCalculo(conhecimento, calculoFrete);
		
		//LMS-3715
		conhecimento.setVlImpostoPesoDeclarado(conhecimento.getVlImposto());
		conhecimento.setVlIcmsSubstituicaoTributariaPesoDeclarado(conhecimento.getVlIcmsSubstituicaoTributaria());
	}

	public void validateClearObservacaoDoctoServicos(CalculoFrete calculoFrete,	Conhecimento conhecimento) {

		ArrayList<ObservacaoDoctoServico> remover = new ArrayList<ObservacaoDoctoServico>();
		
		if(conhecimento.getObservacaoDoctoServicos() != null && calculoFrete.getObservacoes() != null){
			for (ObservacaoDoctoServico obs : conhecimento.getObservacaoDoctoServicos()) {
				if (calculoFrete.getObservacoes().contains(obs.getDsObservacaoDoctoServico())) {
					remover.add(obs);
				}
			}
			
			conhecimento.getObservacaoDoctoServicos().removeAll(remover);
		}else{
			return;
		}
		
	}
	

	/**
	 * Verifica Notas Fiscais Conhecimento
	 * 
	 * @author Andre Valadas
	 * @param idClienteRemetente
	 * @param nrNotasFiscais
	 * @param datas
	 * @param chaves 
	 */
	public void verifyNotasConhecimento(Long idClienteRemetente, List<Integer> nrNotasFiscais, List<YearMonthDay> datas, List<String> dsSerieNotas, List<String> chaves, List<String> tipoDoc, Boolean redespachoIntermediario) {
		// LMSA-6598
		if (!Boolean.TRUE.equals(redespachoIntermediario)) {
		String validaChaveNFE = (String) conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "VALIDA_CHAVE_NFE", false);
		if("S".equals(validaChaveNFE)){
			verifyChavesNotasFiscais(chaves, tipoDoc);
		}else{
			verifyNrNotasFiscais(idClienteRemetente, nrNotasFiscais, dsSerieNotas, tipoDoc);
			verifyNrNotasFiscaisAndSeries(idClienteRemetente, nrNotasFiscais, dsSerieNotas, tipoDoc);
		}
		verifyDataEmissaoNotasFiscais(nrNotasFiscais, datas);
		} else {
			verificarChavesNotaUtilizadaOutroCte(chaves, idClienteRemetente);
	}
	}

	
	/**
	 * Verifica se as chaves NFE contidas na Lista existem na base de dados.
	 * Caso existam lança BusinessException 'LMS-04498'.
	 * 
	 * @author Ícaro Franco Damiani
	 * @param chaves
	 */
	@SuppressWarnings("rawtypes")
	private boolean verifyChavesNotasFiscais(List<String> chaves, List<String> tipoDocs) {
	    boolean chaveValida = true;
		if(chaves != null && !chaves.isEmpty()){
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < chaves.size(); i++) {
				String chave = chaves.get(i);
				String tipoDoc = tipoDocs.get(i);
				if (!"01".equals(tipoDoc)){
					continue;
				}
				
				List returned = notaFiscalConhecimentoService.findByNrChave(chave);
				if(returned != null && !returned.isEmpty()){
					Conhecimento conhecimento = (Conhecimento)((Object[])returned.get(0))[1];
					
					String errorKey = "LMS-04498";
					
					Filial origem = null;
					DomainValue tpDocumento = null;
					Long nrDocumento = null;
					if(conhecimento != null){
						tpDocumento = conhecimento.getTpDoctoServico();
						origem = conhecimento.getFilialOrigem();
						nrDocumento = conhecimento.getNrDoctoServico();
						
						if (origem.equals(SessionUtils.getFilialSessao())){
						    errorKey = "LMS-04565";
						}
						
					}
					
					if(tpDocumento != null){
						builder.append(tpDocumento.getValue());
					}
					
					if(origem != null){
						builder.append(" ");
						builder.append(origem.getSgFilial());
					}
					
					if(nrDocumento != null){
						builder.append(" ");
						builder.append(nrDocumento);
					}
		
					if(i<chaves.size()){
						builder.append(VMProperties.LINE_SEPARATOR.getValue());
					}
					
					
					if ("LMS-04565".equals(errorKey)){
					    new WarningCollector("LMS-04565: "+ configuracoesFacade.getMensagem("LMS-04565",new String[]{builder.toString()}));
					    chaveValida = false;
					}else{
					    throw new BusinessException(errorKey, new Object[]{builder.toString()});
					}
				}
			}
		}
		return chaveValida;
	}

	// LMSA-6598: LMSA-7137
	@SuppressWarnings("rawtypes")
	private void verificarChavesNotaUtilizadaOutroCte(List<String> chaves, Long idClienteRemetente) {
		if(chaves != null && !chaves.isEmpty()) {
			for (int i = 0; i < chaves.size(); i++) {
				String chave = chaves.get(i);
//				List result = notaFiscalConhecimentoService.findByNrChave(chave);
				List result = notaFiscalConhecimentoService.findByNrChaveEClienteRemetente(chave, idClienteRemetente);
				if(result != null && !result.isEmpty()) {
					throw new BusinessException("LMS-04202", new Object[]{chave});
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void verifyNrNotasFiscaisAndSeries(Long idRemetente, List<Integer> nrNotasFiscais, List<String> dsSerieNotasFiscais, List<String> tipoDoc) {
		if (idRemetente == null) {
			return;
		}
		
		if(!containsNF(tipoDoc))  {
			return;
		}
		
		List result = notaFiscalConhecimentoService.findListByRemetenteNrNotasDsSeriePaddingSeries(idRemetente, nrNotasFiscais, dsSerieNotasFiscais, "01");
		if (result != null && !result.isEmpty()) {
			StringBuilder param = new StringBuilder();
			StringBuilder param2 = new StringBuilder();
			
			for (Iterator<Object[]> iter = result.iterator(); iter.hasNext();) {
				Object[] next = iter.next();
				
				Integer nrNota = (Integer) next[0];
				String nrSerie = (String) next[1];

				param.append(nrNota);
				param2.append(nrSerie);
				
				if (iter.hasNext()) {
					param.append("/");
					param2.append("/");
				}
			}
			
			throw new BusinessException("LMS-04525", new Object[]{param.toString(), param2.toString()});
		}
	}
	
	/**
	 * Verifica se existe notas fiscais com os numeros digitados para o mesmo remetente
	 * Caso exista, adiciona a mensagem de alerta a sessao "warnings"
	 * 
	 * @author Andre Valadas
	 * @param idRemetente
	 * @param tipoDoc 
		 * @param datas 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void verifyNrNotasFiscais(Long idRemetente, List<Integer> nrNotasFiscais, List<String> dsSerieNotasFiscais, List<String> tipoDoc) {
		if (idRemetente == null) {
			return;
		}
		
		if(!containsNF(tipoDoc))  {
			return;
		}
		
		List result = notaFiscalConhecimentoService.findListByRemetenteNrNotasDsSerieTipoDoc(idRemetente, nrNotasFiscais, dsSerieNotasFiscais, "01");
		if (result != null && !result.isEmpty()) {
			StringBuilder param = new StringBuilder();
			for (Iterator<Object[]> iter = result.iterator(); iter.hasNext();) {
				Integer nrNota = (Integer) iter.next()[0];
				param.append(nrNota);
				if (iter.hasNext()) {
					param.append("/");
				}
			}
			// TODO LMSA-7137
			throw new BusinessException("LMS-04202", new Object[]{param.toString()});
		}
		
	}

	private boolean containsNF(List<String> tipoDoc) {
		return tipoDoc.contains("01");
	}

	/**
	 * Verifica datas de emissao
	 * Caso exista excecoes, adiciona a mensagem de alerta a sessao "warnings"
	 * 
	 * @author Andre Valadas
	 * @param nrNotasFiscais
	 * @param datas
	 */
	private void verifyDataEmissaoNotasFiscais(List<Integer> nrNotasFiscais, List<YearMonthDay> datas) {
		BigDecimal validade = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.DIAS_VALIDADE_NF);
		int nrDias = 0;
		if(validade != null) {
			nrDias = -(validade.intValue());
		}
		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		YearMonthDay dtValidade = dtAtual.plusDays(nrDias);
		for (int i = 0; i < datas.size(); i++) {
			YearMonthDay data = datas.get(i);
			if(data != null) {
				if(JTDateTimeUtils.comparaData(data, dtValidade) < 0) {
					new WarningCollector("LMS-04020: " + configuracoesFacade.getMensagem("LMS-04020"));
					break;
				}
			}
		}
	}

	public void verifyMiscellaneousDocuments(List<Map<String, Object>> notasFiscais) {
		boolean hasNfe = false;
		boolean hasVlTotalProdutos = false;
		for (Map<String, Object> nota : notasFiscais) {
			if(nota.get("nrChave") != null){
				hasNfe = true;
			}
			if(nota.get("vlTotalProdutos") != null){
				hasVlTotalProdutos = true;
			}
		}
		for (Map<String, Object> nota : notasFiscais) {
			if((hasNfe && nota.get("nrChave") == null) || (hasVlTotalProdutos && nota.get("vlTotalProdutos") == null)){
				throw new BusinessException("LMS-04366");
			}
		}
		
	}
	
	public Cliente getClienteBaseCalculo(Conhecimento conhecimento) {
		return super.getClienteBaseCalculo(conhecimento);
	}
	
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	
	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
}