
package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.DoctoServicoValidateFacade;
import com.mercurio.lms.expedicao.DocumentoServicoFacade;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoNFServico;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.LiberacaoDocServ;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;


/**
 * Servico utilizado para cálculo de frete  
 * 
 * Método principal calcularFrete, o metodo retorna um mapa com todos os atributos calculados. Caso
 * ocorrer algum erro {@link BusinessException} retorna para o cliente no mapa; 
 * <br>  
 * Para ser utilizado externamente é necessário popular o objeto {@link Frete} com os 
 * parametros necessários;
 * <br>
 * 	
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.calcularFreteService"
 */
@Assynchronous(name = "CalcularFreteService")
public class CalcularFreteService {

	private static final BigDecimal ZERO = new BigDecimal("0.00");
	private static final String COTACAO = "C";
	private static final String NORMAL = "N";
	private static final String SERVICO = "S";
	private static final String ERRO = "erro";
	private static final String PARCELAS_SERVICO = "servicos";
	private static final String PARCELAS_FRETE   = "parcelasFrete";
	private static final Short CD_LOCALIZACAO_MERCADORIA = 24;

	private ConhecimentoNormalService conhecimentoNormalService;
	private ConhecimentoService conhecimentoService;
	private CalcularFreteTabelaCheiaService calcularFreteTabelaCheiaService;
	private ClienteService clienteService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private ParametroGeralService parametroGeralService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private ConfiguracoesFacade configuracoesFacade;
	
	private ParcelaPrecoService parcelaPrecoService;
	private DoctoServicoValidateFacade doctoServicoValidateFacade;
	private ConhecimentoCancelarService conhecimentoCancelarService;
	
	private DocumentoServicoFacade documentoServicoFacade;    
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private DpeService dpeService;

	private Log log = LogFactory.getLog(CalcularFreteService.class);

	private BatchLogger batchLogger;


	/**
	 * <style>body{font-family:verdana; font-size:11px;}</style>
	 * 
	 * Calcula frete
	 *  
	 *   Neste serviço foram retirados a dependencia de dados da sessão. 
	 *   Foi substituindo os dados da {@link SessionUtils} do LMS pelo atributo 
	 *   filialTransacao em {@link DoctoServicoDadosCliente}. 
	 *   
	 *   Para utilizar este serviço é obrigatório preencher filialTransacao em  {@link DoctoServicoDadosCliente}
	 * 
	 *  {@link Frete}<br>
	 *  tpFrete - C Cotação , N Normal e S Serviço<br><br>
	 *  
	 *	--- Parametros obrigatórios Frete COTAÇÃO<br>
	 *
	 *  Este serviço foi desenvolvido para os clientes da Mercúrio consultarem o valor do frete
	 *  a partir de uma origem, destino, peso mercadoria e valor do frete. Está rotina normalmente será urilizada
	 *  por chamadas WebService.   
	 *  
	 *  É possível verificar {CotacaoFreteViaWebAction} , action utilizada pelos cliente através da tela 
	 *  Cotação cliente via web 
	 *  
	 * 	
	 *  {@link Conhecimento}<br>
	 *  tpConhecimento - Tipo do conhecimento<br>
	 *  tpFrete - Tipo de frete CIF - C ou FOB F<br>
	 *  tpCalculoPreco - Tipo de cálculo preço NO - Normal<br>
	 *  tpDevedorFrete - Tipo devedor do frete R - Remetente ou D - Destinatário<br>
	 *  tpDocumentoServico - Tipo documento serviço CTR<br>
	 *  clienteByIdClienteRemetente - Necessário preencher o idCliente e o nrIdentificação somente<br>
	 *  clienteByIdClientedestinatario - Necessário preencher o idCliente e o nrIdentificação somente<br>
	 *  inscricaoEstadualRemetente - Necessário preencher o numero da IE<br>
	 *  InscricaoEstadualDestinatario - Necessário preencher o numero da IE<br>
	 *  devedorDocServs - lista com o devedor do frete - Nesta lista deve ser adicionado o devedor do frete<br>
	 *  municipioByIdMunicipioColeta - Preencher com o idMunicipio coleta<br>
	 *  municipioByIdMunicipioEntrega -Preencher com o idMunicipio entrega<br>
	 *  servico - Preencher com o serviço adequado<br>
	 *  dadosCliente Preencher os dados do cliente idClienteRemetente, idIeRemetente , idUfRemetente, tpSituacaoTributariaRemetente,<br>
	 *  idClienteDestinatario, idIeDestinatario , idUfDestinatario, tpSituacaoTributariaDestinatario <br>
	 *  informar blCotacaoRemetente como TRUE. Informar a filial da transação (Simula a filial sessão LMS) e idFilialdoUsuario<br> 
	 *  densidade - Informar a densidade IdDensidade (verificar com o analista a densidade correta) <br>
	 *  psReal - Informar o peso do frete<br>
	 *  vlMercadoria - Informar o valor da mercadoria<br>
	 *  filialOrigem - Informar o idFilial de origem <br>
	 *  filialDestino - Informar o idFilial de destino<br>
	 *  divisaoCliente - Informar o id da divisão cliente baseado no frete CIF ou FOB<br>
	 *  blCalculaParcelas - Sempre passar TRUE - Isso indica no cálculo de frete que irá cálcular as<br> 
	 *  parcelas de frete Peso, Valor, Pedágio etc... <br><br>
	 * 
	 * 	--- Parametros obrigatórios Frete NORMAL <br><br>
	 *  
	 *  Atualmente estes serviços são utilizados pelas telas de conhecimento SWT <br><br>
	 *  
	 *  Para utilizar este serviço basta utilizar o {@link Conhecimento} populado com os dados do banco <br>
	 *  e injetar este no objeto {@link Frete}. <br><br>
	 *  
	 *  É necessário preencher o atributo idDivisaoCliente do objeto {@link CalculoFrete} com o idDivisaoCliente do 
	 *  {@link Conhecimento} <br><br>
	 *   
	 *  
	 * @param  frete
	 * @return Map<String,String>
	 */
	public Map<String,Object> executeCalcularFrete(Frete frete){
		Map<String,Object> retorno = new  HashMap<String, Object>();
		try {
			/*Verifica se a filialTransacao foi adicionada*/
			if(frete.getConhecimento().getDadosCliente().getFilialTransacao() == null){
				throw new BusinessException("O atributo Frete.Conhecimento.DadosCliente.FilialTransação é obrigatório " +
				" Este atributo simula a filial de sessao do LMS.");
			}
			retorno = executaCalculoFreteByTipoFrete(frete);
		} catch (BusinessException e) {
			log.error(e);
			/*Retorna no map uma key chamada "erro" - BusinessException*/
			retorno.put(ERRO, e.getMessage());
		}
		return retorno;
	}


	/**
	 * Método utilizado no calculo de frete tanto por webservice quanto por swt
	 * 
	 * @param frete
	 * @return
	 */
	private Map<String, Object> executaCalculoFreteByTipoFrete(Frete frete) {
		Map<String,Object> retorno = new  HashMap<String, Object>();
		
		if(SERVICO.equals(frete.getTpFrete())){
			retorno = executeCalcularFreteServico(frete);
		}else if(NORMAL.equals(frete.getTpFrete())){
			validaFreteNormal(frete);				
			retorno = executeCalcularFreteNormal(frete);
		}else if(COTACAO.equals(frete.getTpFrete())){
			validaFreteCotacao(frete);
			frete.getCalculoFrete().setBlCotacao(true);
			retorno = executeCalcularFreteCotacao(frete);
		}else{
			throw new BusinessException("Tipo de cálculo inválido, o atributo Frete.tpFrete deve ser preenchido " +
					" com S - Servico, N - Normal ou C - Cotação.");
		}
		return retorno;	
	}	
		
	/**
	 * Método criado para chamar calculo por tipo de frete(executaCalculoFreteByTipoFrete) 
	 * 
	 * @param frete
	 * @return
	 */
	public Map<String, Object> executeCalcularFreteCTRC(Frete frete) {
		Map<String,Object> retorno = new  HashMap<String, Object>();

		if(SERVICO.equals(frete.getTpFrete())){
			retorno = executeCalcularFreteServico(frete);
		}else if(NORMAL.equals(frete.getTpFrete())){
			retorno = executeCalcularFreteNormal(frete);
		}else if(COTACAO.equals(frete.getTpFrete())){
			validaFreteCotacao(frete);
			retorno = executeCalcularFreteCotacao(frete);
		}else{
			throw new BusinessException("Tipo de cálculo inválido, o atributo Frete.tpFrete deve ser preenchido " +
			" com S - Servico, N - Normal ou C - Cotação.");
		}
		return retorno;
	}

	/**
	 * Executa rotinas necessárias antes de executar o frete cotação 
	 * 
	 * @param frete
	 * @throws BusinessException
	 */
	public void validaFreteCotacao(Frete frete) throws BusinessException{
		conhecimentoNormalService.setClienteBaseCalculoFreteCotacao(frete.getCalculoFrete(), frete.getConhecimento());	
	}

	/**
	 * Executa rotina necessárias para validação do frete através do conhecimento 
	 *      
	 * @param frete
	 */
	public void validaFreteNormal(Frete frete) throws BusinessException{

		Conhecimento conhecimento = frete.getConhecimento();		
		CalculoFrete calculoFrete = frete.getCalculoFrete();

		/*A validação de Remetente e Destinatario abaixo foi retirada, estas devem ser
		tratadas antes de entrar nas regras de negócio do cálculo de frete
		conhecimentoNormalService.validateIntegrantesIguais(conhecimento);*/		

		conhecimentoNormalService.validateClienteResposavel(conhecimento);		
		conhecimentoNormalService.setClienteResponsavel(conhecimento);				
		conhecimentoNormalService.setLocalEntrega(conhecimento);
		conhecimentoNormalService.setFilialAtendimentoMunicipioDestino(conhecimento);				
		conhecimentoNormalService.validateAtendimentoMunicipioDestino(conhecimento, calculoFrete);	
		conhecimentoNormalService.setRotaColetaEntregaSugerida(conhecimento);				
		conhecimentoNormalService.setTipoConhecimentoCooperacao(conhecimento);
		conhecimentoNormalService.setClienteBaseCalculoFrete(calculoFrete, conhecimento);				
		conhecimentoNormalService.setLocalColeta(conhecimento);
		conhecimentoNormalService.validateDensidadeClienteDevedorEspecial(conhecimento);				
		conhecimentoNormalService.validateCamposAdicionais(conhecimento);		
		conhecimentoNormalService.validateInscricacaoEstadualTomadorServico(conhecimento);		
		conhecimentoNormalService.validateTipoFreteCliente(conhecimento);		
		conhecimentoNormalService.validateAeroporto(conhecimento);
		conhecimentoNormalService.validateQuantidadeVolumes(conhecimento);
		conhecimentoNormalService.validateDensidadePesoCubado(conhecimento);		
		conhecimentoNormalService.validateDoctoServicoOriginal(conhecimento);
		conhecimentoNormalService.validateAtendimentoEmpresaTipoFrete(conhecimento);
		conhecimentoNormalService.validateUFPassagemDestino(conhecimento,calculoFrete);
		conhecimentoNormalService.validateCotacao(conhecimento);
		conhecimentoNormalService.validateCtrcCooperada(conhecimento);
		conhecimentoNormalService.validateCotacaoRemententeNaoInformada(conhecimento); 		
		conhecimentoNormalService.validarLiberacaoCotacao(conhecimento);				
		conhecimentoNormalService.validateNotaFiscalConhecimento(conhecimento);		


	}

	/**
	 * Calculo do peso aforado: <br>
		Se forem informadas dimensões na aba dimensões: <br>
		- Somar as Multiplicações dos valores informados (Altura, Largura, Comprimento e Quantidade) nas linhas da grid da popup dimensões; <br>
		- Se o cliente responsável identificado no item 3 for “Especial” ou “Filial de cliente especial”, verificar se para a tabela_divisão_cliente <br> 
			da divisão que deverá ser utilizada no cálculo do frete o campo NR_FATOR_CUBAGEM está preenchido com valores diferentes de zero,  <br>
			se estiver multiplicar a soma obtida acima pelo conteúdo do banco NR_FATOR_CUBAGEM e dividir por 1.000.000,  <br>
			o resultado obtido será o peso aforado, caso contrário fazer conforme abaixo. <br>
		- Se o modal do serviço que foi selecionado no campo Serviço for Rodoviário dividir o valor obtido <br> 
			pelo conteúdo do PARAMETRO_GERAL.NM_PARAMETRO_GERAL = “PESO_METRAGEM_ CUBICA_RODOVIARIO”. <br>
		- Se o modal do serviço que foi selecionado no campo Serviço for Aéreo dividir o valor obtido  <br>
			pelo conteúdo do PARAMETRO_GERAL.NM_PARAMETRO_GERAL = “PESO_METRAGEM_ CUBICA_AEREO”. <br>
		- O resultado das operações acima será o peso aforado. <br>
		 <br>
		Se não forem informadas as dimensões na popup Dimensões: <br>
		- Peso aforado será igual à soma dos pesos aforados (PsCubado) das notas fiscais. <br>
	 * @param conhecimento
	 */
	public void calcularPesoAforado(Conhecimento conhecimento) {
		List<Dimensao> dimensoes = conhecimento.getDimensoes();
		Servico servico = conhecimento.getServico();
		if(dimensoes != null && dimensoes.size() > 0) {
			BigDecimal sum = new BigDecimal(0);
			for (Dimensao dimensao : dimensoes) {
				sum = sum.add(new BigDecimal(dimensao.getNrAltura() * dimensao.getNrLargura() * dimensao.getNrComprimento() * dimensao.getNrQuantidade()));
			}
			Cliente clienteResponsavel = clienteService.findByIdInitLazyProperties(((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente().getIdCliente(), false);
			if("S".equals(clienteResponsavel.getTpCliente().getValue()) || "F".equals(clienteResponsavel.getTpCliente().getValue())) {
				DivisaoCliente divisaoCliente = conhecimento.getDivisaoCliente();
				if(divisaoCliente != null && servico != null) {
					TabelaDivisaoCliente tabelaDivisaoCliente = tabelaDivisaoClienteService.findTabelaDivisaoCliente(divisaoCliente.getIdDivisaoCliente(), conhecimento.getServico().getIdServico());
					if(tabelaDivisaoCliente != null && tabelaDivisaoCliente.getNrFatorCubagem() != null && tabelaDivisaoCliente.getNrFatorCubagem().compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal psAforado = sum.multiply(tabelaDivisaoCliente.getNrFatorCubagem()).divide(new BigDecimal(1000000), 2, BigDecimal.ROUND_HALF_UP);
						conhecimento.setPsAforado(psAforado);
						return;

					} else if(tabelaDivisaoCliente != null && tabelaDivisaoCliente.getNrFatorCubagem() != null  && (BigDecimalUtils.isZero(tabelaDivisaoCliente.getNrFatorCubagem()))){
						BigDecimal psAforado = null;
						if( conhecimento.getPsReal() != null && conhecimento.getPsAferido() != null && CompareUtils.gt(conhecimento.getPsReal(),conhecimento.getPsAferido()) ){
							psAforado = conhecimento.getPsReal();
						}else{
							if ( BigDecimalUtils.hasValue(conhecimento.getPsAferido()) ) {
								psAforado = BigDecimalUtils.round(conhecimento.getPsAferido(), BigDecimal.ROUND_HALF_UP);
							} else if ( BigDecimalUtils.hasValue(conhecimento.getPsReal()) ) {
								psAforado = BigDecimalUtils.round(conhecimento.getPsReal(), BigDecimal.ROUND_HALF_UP);
					}
				}
						conhecimento.setPsAforado(psAforado);
						return;
			}
			}
			}
			if(servico != null && servico.getTpModal() != null) {
				BigDecimal metragemCubica = null;
				if (ConstantesExpedicao.MODAL_AEREO.equalsIgnoreCase(servico.getTpModal().getValue())) {
					metragemCubica = BigDecimalUtils.getBigDecimal(parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_AEREO, false));
				} else {
					metragemCubica = BigDecimalUtils.getBigDecimal(parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_RODOVIARIO, false));
				}
				if(metragemCubica != null && metragemCubica.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal psAforado = sum.divide(metragemCubica, 2, BigDecimal.ROUND_HALF_UP);
					conhecimento.setPsAforado(psAforado);
					return;
				}
			}
		} else {
			// Peso aforado nessa situação é a soma dos ps_cubado das notasFiscais
			List listNotasFiscais = conhecimento.getNotaFiscalConhecimentos();
			BigDecimal psCubado = BigDecimal.ZERO;
			if (listNotasFiscais != null && listNotasFiscais.size() > 0) {
				for (int i = 0; i < listNotasFiscais.size(); i++) {
					NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento)listNotasFiscais.get(i);
					psCubado = psCubado.add(notaFiscalConhecimento.getPsCubado());
				}
			}
			conhecimento.setPsAforado(psCubado);
		}
	}

	public void validateCotMac(Frete frete) {
		Conhecimento conhecimento = frete.getConhecimento();		
		CalculoFrete calculoFrete = frete.getCalculoFrete();

		conhecimentoNormalService.validateCotMac(conhecimento, calculoFrete);
	}

	@AssynchronousMethod(
			name="calcularFreteService.executarCalculoFreteTabelaCheia",
			type=BatchType.BATCH_SERVICE,
			feedback=BatchFeedbackType.ON_ERROR
	)
	public void executarCalculoFreteTabelaCheia() {

		// TODO - CJS 18/01/2010 -> Sugiro que essa rotina não seja chamada por BATCH, mas sim, ser executada junto com o Cálculo de Frete do CTO,
		// assim que se calcular o frete, na sequência já calcular essa rotina, assim evita-se um processamento em massa em outro horário.

		// Número que representa o lote de conhecimento que irá buscar. Busca-se de 20 em 20 por questões de performance...
		// A idéia é processar aos poucos e não trabalhar com um consumo de memória muito grande, já que serão milhares de CTOs a serem processados diariamente...
		Long rownum = Long.valueOf(20); 

		// Busca uma lista de IDs conhecimentos...
		List<Long> conhecimentoList = conhecimentoService.findConhecimentosCalculoFreteTabelaCheiaByRownum(rownum);

		//Percorro lista de IDs conhecimentos até que não tenha mais conhecimentos a serem processados tabela cheia
		while (conhecimentoList != null && conhecimentoList.size() > 0) {

			// Faço um for nos conhecimentos que foi buscado, assim, não é necessário fazer um select a acada iteração 
			// para saber se existe mais conhecimentos a serem processados...
			for (Long idConhecimento : conhecimentoList) {

				try {
					calcularFreteTabelaCheiaService.executeConhecimentoCalculoFreteTabelaCheia(idConhecimento);
				} catch (Exception e) {
					log.error(e);
					// FIXME Eri, o que fazer quando dar erro?
					// Por em quanto estou ignorando e continuo o calculo com os demais CTOs...
					// Mesmo se ouver erro em algum dos calculos, o que for processado OK já está comitado 
					// e não aparecerá para um próximo processo.
				}

			}
			//Após o "for" processado, faço novamente mais uma busca do lote de cto a ser processado.
			conhecimentoList = conhecimentoService.findConhecimentosCalculoFreteTabelaCheiaByRownum(rownum);			
		}		
	}

	/**
	 * Calcula os fretes normal através do Conhecimento 
	 * 
	 * Este método pode ser utilizado externamente. Não possue nenhuma
	 * depêndencia da sessão LMS 
	 * 
	 * @param  frete
	 * @return Retorna o map com todos os valores calculados 
	 * 
	 */
	public Map<String,Object> executeCalcularFreteNormal(Frete frete){

		// Seto cliente devedor responsável, somente se o cliente devedor responsável for do tipo DEVEDOR_OUTRO_RESPONSAVEL
		String responsavel = frete.getConhecimento().getTpDevedorFrete().getValue();
		if(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(responsavel)) {
			conhecimentoNormalService.setClienteResponsavel(frete.getConhecimento());
		}

		/*Verifica através do cadastrado do cliente se o mesmo obriga a ter Dimensões*/
		//conhecimentoNormalService.validateObrigatoriedaDimensoesClienteBase(frete.getCalculoFrete(),frete.getConhecimento());

		/*Calcula o peso aforado através das dimensões do conhecimento*/
		conhecimentoNormalService.calculaPesoAforadoDimensoes(frete.getConhecimento(), frete.getCalculoFrete());

		/*Configura os dados do calculo de frete atraves do conhecimento*/
		conhecimentoNormalService.configureCalculoFrete(frete.getConhecimento(),frete.getCalculoFrete());

		/*Executa o cálculo do frete*/			
		documentoServicoFacade.executeCalculoConhecimentoNacionalNormal(frete.getCalculoFrete());		

		/*Copia os dados do calculo de frete para o conhecimento*/
		CalculoFreteUtils.copyResult(frete.getConhecimento(),frete.getCalculoFrete());

		/*Retorna o map contendo todos os valores calculados do frete*/
		Map map = montarParcelasCalculo(frete.getConhecimento(),frete.getCalculoFrete());
		map.put("frete", frete);
		return map;		
	}

	/**
	 * Calcula o frete cotação
	 * 
	 * Utilizado pelos clientes através da cotação via web
	 * 
	 * Este método pode ser utilizado externamente. Não possue nenhuma
	 * depêndencia da sessão LMS
	 * 
	 * @param frete
	 * @return
	 */
	public Map<String,Object> executeCalcularFreteCotacao(Frete frete){

		/*Calcula o peso aforado através das dimensões do conhecimento*/
		conhecimentoNormalService.calculaPesoAforadoDimensoes(frete.getConhecimento(), frete.getCalculoFrete());

		/*Configura os dados do calculo de frete atraves do objeto conhecimento*/
		conhecimentoNormalService.configurarFreteCotacao(frete.getConhecimento(),frete.getCalculoFrete());

		/*Executa o cálculo do frete*/			
		documentoServicoFacade.executeCalculoConhecimentoNacionalNormal(frete.getCalculoFrete());		

		/*Copia os dados do calculo de frete para o conhecimento*/
		CalculoFreteUtils.copyResult(frete.getConhecimento(),frete.getCalculoFrete());

		/*Retorna o map contendo todos os valores calculados do frete*/
		return montarParcelasCalculo(frete.getConhecimento(),frete.getCalculoFrete());		
	}

	/**
	 * Retorna um mapa com todas as parcelas cálculadas
	 * 
	 * @param  conhecimento
	 * @param  calculoFrete
	 * @return Retorna um mapa com todos os valores calculados
	 */
	public Map<String,Object> montarParcelasCalculo(Conhecimento conhecimento, CalculoFrete calculoFrete) {

		Map<String,Object> retorno = new HashMap<String,Object>();
		List<ParcelaDoctoServico> parcelasDoctoServ = new ArrayList<ParcelaDoctoServico>();

		/*Parcelas*/
		if(calculoFrete.getBlCalculaParcelas() != null  && calculoFrete.getBlCalculaParcelas().booleanValue() && calculoFrete.getParcelas() != null) {
			/*Ordena as parcelas do calculo de frete*/
			CalculoFreteUtils.ordenaParcelas(calculoFrete.getParcelas());
			retorno.put(PARCELAS_FRETE, obterListaParcelas(conhecimento,calculoFrete,parcelasDoctoServ,ConstantesExpedicao.TP_GENERALIDADE));
		}

		/*Servicos*/
		if(calculoFrete.getBlCalculaServicosAdicionais() != null && calculoFrete.getBlCalculaServicosAdicionais().booleanValue() && calculoFrete.getServicosAdicionais() != null) {					
			retorno.put(PARCELAS_SERVICO, obterListaParcelas(conhecimento,calculoFrete,parcelasDoctoServ,ConstantesExpedicao.TP_SERVICO_ADICIONAL));
		}

		/*Adiciona os atributos com os valores totais*/
		retorno.put("vlTotalFrete", BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlTotalParcelas(), ZERO));
		retorno.put("vlTotalServico", BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlTotalServicosAdicionais(), ZERO));
		retorno.put("vlTotalCtrc", BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlTotal(), ZERO));
		retorno.put("vlDesconto", BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlDesconto(), ZERO));
		retorno.put("vlImposto", BigDecimalUtils.defaultBigDecimal(calculoFrete.getTributo().getVlImposto(), ZERO));
		retorno.put("vlICMSubstituicaoTributaria", BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlRetensaoSituacaoTributaria(), ZERO));
		retorno.put("vlTotalTributos", BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlTotalTributos(), ZERO));
		retorno.put("vlDevido", BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlDevido(), ZERO));
		retorno.put("blIncidenciaIcmsPedagio", calculoFrete.getBlIncideIcmsPedagio());

		/*Adiciona a lista de ParcelaDoctoServico ao Conhecimento*/
		conhecimento.setParcelaDoctoServicos(parcelasDoctoServ);

		validaCalculoFreteZerado(calculoFrete);

		return retorno;
	}

	/**
	 * Metodo para validar calculo de frete zerado 
	 * LMS-119 - Calculo do frete com resultado zero.
	 * @param calculoFrete
	 */
	private void validaCalculoFreteZerado(CalculoFrete calculoFrete) {
		if(calculoFrete.getVlTotal() == null || BigDecimalUtils.isZero(calculoFrete.getVlTotal())) {
			throw new BusinessException("LMS-04343");
		}
	}
	
	/**
	 * Obtem a lista de parcelas calculadas 
	 * 
	 * @param  conhecimento
	 * @param  calculoFrete
	 * @param  parcelasDoctoServ
	 * @param  tpParcela
	 * @return Retorna a lista de maps com as parcelas calculadas 
	 */
	private List<Map<String,Object>> obterListaParcelas(Conhecimento conhecimento, CalculoFrete calculoFrete, List<ParcelaDoctoServico> parcelasDoctoServ, String tpParcela){		
		List<Map<String,Object>> parcelas = new ArrayList<Map<String,Object>>();

		/*Adiciona todas as ParcelaPreco a lista*/
		for(ParcelaServico parcelaServico : calculoFrete.getParcelas()){
			parcelas.add(obterParcelaCalculada(parcelaServico,tpParcela));
			parcelasDoctoServ.add(new ParcelaDoctoServico(parcelaServico, conhecimento));
		}
		return parcelas;
	}

	/**
	 * Obtem o Map com os parametros da parcela PARCELA_SERVICO ou PARCELA_FRETE
	 * 
	 * @param  parcelaServico
	 * @param  tpParcela
	 * @return Map<String,Object>
	 */
	private Map<String,Object> obterParcelaCalculada(ParcelaServico parcelaServico, String tpParcela){
		Map<String,Object> parcela = new HashMap<String,Object>();

		/*Através do tpParcela adiciona o atributo adequado para a parcela*/
		if(ConstantesExpedicao.TP_SERVICO_ADICIONAL.equals(tpParcela)){
			parcela.put("vlServico", parcelaServico.getVlParcela());
			parcela.put("dsServico", parcelaServico.getParcelaPreco().getDsParcelaPreco());
		}else{
			parcela.put("vlParcela", parcelaServico.getVlParcela());
			parcela.put("nmParcelaPreco", parcelaServico.getParcelaPreco().getDsParcelaPreco());
		}
	
		parcela.put("idParcelaPreco", parcelaServico.getParcelaPreco() != null ? parcelaServico.getParcelaPreco().getIdParcelaPreco() : null);
		parcela.put("vlParcelaBruto", parcelaServico.getVlBrutoParcela());
		
		return parcela;
	}


	/**
	 * Calcula a NF de Serviço
	 * 
	 * 
	 * @param  frete
	 * @return Map com todos os parametros calculados
	 */
	public Map<String,Object> executeCalcularFreteServico(Frete frete){

		/*Seta os atributos necessários no objeto CalculoNFServico*/
		prepararCalculoNFServico(frete.getCalculoNFServico(), frete.getNotaFiscalServico());

		/*Executa os calculos da nota*/
		documentoServicoFacade.executeCalculoNotaFiscalServico(frete.getCalculoNFServico());

		CalculoFreteUtils.copyResult(frete.getNotaFiscalServico(), frete.getCalculoNFServico());

		List<Map<String, Object>> tributos = new ArrayList<Map<String, Object>>();
		if(frete.getCalculoNFServico().getTributos() != null) {
			for(ImpostoServico impostoServico : frete.getCalculoNFServico().getTributos()){
				Map data = new HashMap();
				data.put("nome",  impostoServico.getTpImposto().getDescription().toString());
				data.put("valor", impostoServico.getVlImposto());
				tributos.add(data);
			}
		}

		ParcelaServico parcelaServico = (ParcelaServico) frete.getCalculoNFServico().getServicosAdicionais().get(0);

		Map<String,Object> retorno = new HashMap<String,Object>();
		retorno.put("tributos", tributos);
		retorno.put("totalTributos", frete.getCalculoNFServico().getVlTotalTributos());
		retorno.put("valorTotal", frete.getCalculoNFServico().getVlTotal());
		retorno.put("valor", parcelaServico.getVlParcela());

		return retorno;

	}


	/**
	 * Adiciona os atributos necessários ao objeto CalculoNFServico através 
	 * de NotaFiscalServico
	 *  
	 *  O atributo CalculoNFServico > RestricaoRotaOrigem > IdMunicipio deve possuir
	 *  um valor válido para o id da filial do usuario logado
	 *  	
	 * @param calculoNFServico
	 * @param notaFiscalServico
	 */
	public void prepararCalculoNFServico(CalculoNFServico calculoNFServico, NotaFiscalServico notaFiscalServico) {

		calculoNFServico.setTpModal(notaFiscalServico.getServico().getTpModal().getValue());
		calculoNFServico.setTpAbrangencia(notaFiscalServico.getServico().getTpAbrangencia().getValue());
		calculoNFServico.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_NORMAL);
		calculoNFServico.setTpCalculo(notaFiscalServico.getTpCalculoPreco().getValue());
		calculoNFServico.setBlCalculaParcelas(Boolean.FALSE);
		calculoNFServico.setBlCalculaServicosAdicionais(Boolean.TRUE);
		calculoNFServico.setClienteBase(notaFiscalServico.getClienteByIdClienteDestinatario());

		if (notaFiscalServico.getDivisaoCliente() != null) {
			calculoNFServico.setIdDivisaoCliente(notaFiscalServico.getDivisaoCliente().getIdDivisaoCliente());
		}

		List cotacoes = notaFiscalServico.getCotacoes();
		if (cotacoes != null && !cotacoes.isEmpty()) {
			Cotacao cotacao = (Cotacao)cotacoes.get(0);
			calculoNFServico.setIdCotacao(cotacao.getIdCotacao());
		}
		calculoNFServico.setIdServico(notaFiscalServico.getServico().getIdServico());
		calculoNFServico.setPsReferencia(notaFiscalServico.getPsReferenciaCalculo());
		calculoNFServico.setServAdicionalDoctoServico(notaFiscalServico.getServAdicionalDocServs());

		RestricaoRota restricaoRotaDestino = calculoNFServico.getRestricaoRotaDestino();
		restricaoRotaDestino.setIdMunicipio(notaFiscalServico.getMunicipio().getIdMunicipio());

		calculoNFServico.setDoctoServico(notaFiscalServico);
	}


    public void fechaEmiteCTRC(Map<String, Object> map) {
    	executeCalculoFrete(map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map executeCalculoFrete(Map parameters) {
		long starttime = System.currentTimeMillis();
		final Conhecimento conhecimento = conhecimentoService.findByIdEager((Long) parameters.get("idDoctoServico"));
		conhecimento.setGenerateUniqueNumber(MapUtilsPlus.getBoolean(parameters, "generateUniqueNumber", Boolean.FALSE));

		/** Busca Volumes Notas Fiscais, para Calcular Peso Aferido */
		BigDecimal totalPesoAferidoConhecimento = BigDecimal.ZERO;
		final List<NotaFiscalConhecimento> notaFiscalConhecimentos = conhecimento.getNotaFiscalConhecimentos();
		for (final NotaFiscalConhecimento notaFiscalConhecimento : notaFiscalConhecimentos) {
			final BigDecimal totalPsAferido = BigDecimalUtils.defaultBigDecimal(volumeNotaFiscalService.findTotalPsAferidoByIdNotaFiscalConhecimento(notaFiscalConhecimento.getIdNotaFiscalConhecimento()));
			if(BigDecimalUtils.hasValue(totalPsAferido)) {
				totalPesoAferidoConhecimento = totalPesoAferidoConhecimento.add(totalPsAferido);
				notaFiscalConhecimento.setPsAferido(totalPsAferido);
			}
		}

		/** Seta PEso aferido para calculo do Frete */
		if(totalPesoAferidoConhecimento.compareTo(BigDecimal.ZERO) <= 0) {
			conhecimento.setPsAferido(conhecimento.getPsReal());
		} else {
			conhecimento.setPsAferido(totalPesoAferidoConhecimento);
		}

		/** Valida dados pré-calculo Frete */
		validateExecuteCalculoFrete(conhecimento, parameters);

		conhecimento.setNrConhecimento((Long)parameters.get("nrConhecimento"));
		conhecimento.setNrDoctoServico((Long)parameters.get("nrConhecimento"));
		conhecimento.setBlPesoAferido(Boolean.TRUE);
		conhecimento.setDtPrevEntrega((YearMonthDay)parameters.get("dtPrevistaEntrega"));
    	/*Informa o tipo de objeto correto para cálculo*/
    	CalculoFrete calculoFrete = ExpedicaoUtils.newCalculoFrete(conhecimento.getTpDocumentoServico());
    	if(calculoFrete.isCalculoNotaTransporte()){
    		calculoFrete.setBlCalculaImpostoServico(Boolean.TRUE);
    	}
		if(conhecimento.getNrDiasPrevEntrega() == null){
			conhecimento.setNrDiasPrevEntrega(parameters.get("nrDiasPrevEntrega") == null ? null : Short.valueOf(((Long)parameters.get("nrDiasPrevEntrega")).toString()));
		}
		
		calculoFrete.setClienteBase(conhecimento.getClienteByIdClienteBaseCalculo());
		calculoFrete.setIdDivisaoCliente(conhecimento.getDivisaoCliente() != null ? conhecimento.getDivisaoCliente().getIdDivisaoCliente() : null);
		calculoFrete.setBlCalculoFreteTabelaCheia(Boolean.FALSE);
		calculoFrete.setIdDoctoServico(conhecimento.getIdDoctoServico());
		/** Valida se deve executar Calculo do Frete */
		boolean calculoManual = ConstantesExpedicao.CALCULO_MANUAL.equals(conhecimento.getTpCalculoPreco().getValue());
		if (!calculoManual && !ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO.equals(conhecimento.getTpConhecimento().getValue())) {
			calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
			calculoFrete.setBlCalculaServicosAdicionais(conhecimento.getBlServicosAdicionais());
			calculoFrete.setIdServico(conhecimento.getServico().getIdServico());

			/** Configura Entidade para Calculo do Frete */
			final Frete frete = new Frete();
			frete.setConhecimento(conhecimento);
			frete.setCalculoFrete(calculoFrete);
			frete.setTpFrete(conhecimento.getTpCalculoPreco().getValue());
			
			/** Executa Calculo do Frete */
			final Map calculoFreteResult = executeCalcularFreteCTRC(frete);
			parameters.putAll(calculoFreteResult);
		} else {
			/** LMS-818 Caso conhecimento EDI, valida se número foi informado */
			if(CompareUtils.le(conhecimento.getNrConhecimento(), LongUtils.ZERO)) {
				/** Verifica Data Previsão de Entrega */
				if (conhecimento.getDtPrevEntrega() == null) {
					final Map dataPrevisaoEntrega = dpeService.executeCalculoDPE(conhecimento.getClienteByIdClienteRemetente(),
							conhecimento.getClienteByIdClienteDestinatario(), 
							conhecimento.getClienteByIdClienteBaseCalculo(), 
							conhecimento.getClienteByIdClienteConsignatario(), 
							conhecimento.getClienteByIdClienteRedespacho(), 
							null, 
							conhecimento.getServico().getIdServico(), 
							conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio(), 
							conhecimento.getFilialByIdFilialOrigem().getIdFilial(), 
							conhecimento.getFilialByIdFilialDestino().getIdFilial(), 
							conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio(), 
							conhecimento.getNrCepColeta(), 
							conhecimento.getNrCepEntrega(), 
							conhecimento.getDtColeta());
					if (dataPrevisaoEntrega != null){
						final Long nrDias = (Long)dataPrevisaoEntrega.get("nrPrazo");
						if (nrDias != null){
							conhecimento.setNrDiasPrevEntrega(Short.valueOf(nrDias.shortValue()));
						}
						conhecimento.setDtPrevEntrega((YearMonthDay)dataPrevisaoEntrega.get("dtPrazoEntrega"));
					}
				}

				/** Atualiza dados dos Volumes para Fechamento do Conhecimento */
				final LocalizacaoMercadoria localizacaoMercadoria = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(CD_LOCALIZACAO_MERCADORIA);
				for (NotaFiscalConhecimento nfc : conhecimento.getNotaFiscalConhecimentos()){
					for (VolumeNotaFiscal vnf : (List<VolumeNotaFiscal>)nfc.getVolumeNotaFiscais()){
						String nrVolumeEmbarque = vnf.getNrVolumeColeta();

						if (Boolean.TRUE.equals(parameters.get("blContingencia"))) {
							if (Boolean.TRUE.equals(conhecimento.getBlPaletizacao())) {
								nrVolumeEmbarque = null;
							} else {
								nrVolumeEmbarque = volumeNotaFiscalService.buildLabelBarCode(conhecimento, vnf);
							}
						}
						
						vnf.setNrVolumeEmbarque(nrVolumeEmbarque);
						vnf.setLocalizacaoMercadoria(localizacaoMercadoria);
						vnf.setLocalizacaoFilial(conhecimento.getFilialByIdFilialOrigem());
						vnf.setPsAferido(BigDecimal.ZERO);
					}
				}
			} else {
				//Gera digito verificador
				if(ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(conhecimento.getTpDocumentoServico().getValue()) || ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL.equals(conhecimento.getTpDocumentoServico().getValue()) ){
					conhecimento.setDvConhecimento(ConhecimentoUtils.getDigitoVerificador(conhecimento.getNrConhecimento()));
				}
			}
		}
		
		setTpCargaDocumento(conhecimento);

		parameters.put("conhecimento", conhecimento);
		parameters.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoFrete);
		
		/** Grava Conhecimento */
		gravaCtrcPrimeiraFase(parameters);

		/** LOG do Conhecimento Calculado */
		long seconds = System.currentTimeMillis() - starttime;
		log.debug("[TIMELOG CALCULO FRETE];" + conhecimento.getIdDoctoServico() + ";" + JTDateTimeUtils.getDataHoraAtual() + ";" + seconds + ";" + SessionUtils.getFilialSessao().getSgFilial());
		return parameters;		
	}

    private void setTpCargaDocumento(final Conhecimento conhecimento) {
        
        if(!BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlLiberaEtiquetaEdi())){
        	
        	boolean blPaletizacao = BooleanUtils.isTrue(conhecimento.getBlPaletizacao());
        	if(blPaletizacao){
        		conhecimento.setTpCargaDocumento(new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT));
        		return;
        	}
        	
        	BigDecimal maxPsAferidoVolDocto = volumeNotaFiscalService.findMaxPsAferidoByIdDoctoServico(conhecimento.getIdDoctoServico());
            if(BigDecimalUtils.hasValue(maxPsAferidoVolDocto)){
            	BigDecimal vlLimitePesoVol = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VL_LIMITE_PESO_VOL);
                if(maxPsAferidoVolDocto.compareTo(vlLimitePesoVol) > 0){
                    conhecimento.setTpCargaDocumento(new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT)); 
                    return;
                }
            }
            
            BigDecimal fatorCubagemPadrao = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_RODO);
            if (ConstantesExpedicao.MODAL_AEREO.equalsIgnoreCase(conhecimento.getServico().getTpModal().getValue())) {
            	fatorCubagemPadrao = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_AEREO);
            }
            BigDecimal vlLimitePesoCubVol = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VL_LIMITE_PS_CUB_VOL);
            for (NotaFiscalConhecimento nfc : conhecimento.getNotaFiscalConhecimentos()){
				for (VolumeNotaFiscal vnf : (List<VolumeNotaFiscal>)nfc.getVolumeNotaFiscais()){
					if(IntegerUtils.hasValue(vnf.getNrDimensao1Cm()) && IntegerUtils.hasValue(vnf.getNrDimensao2Cm()) && IntegerUtils.hasValue(vnf.getNrDimensao3Cm())){
						double vlPesoCubado = (vnf.getNrDimensao1Cm().doubleValue() * vnf.getNrDimensao2Cm().doubleValue() * vnf.getNrDimensao3Cm().doubleValue()) / 1000000 * fatorCubagemPadrao.doubleValue();
						if(vlPesoCubado > vlLimitePesoCubVol.doubleValue()){
							conhecimento.setTpCargaDocumento(new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT));
							return;
						}
					}
				}
            }
            
            BigDecimal vlLimiteQtdeVol = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VL_LIMITE_QTDE_VOL);
            if(conhecimento.getQtVolumes().compareTo(vlLimiteQtdeVol.intValue()) > 0){
            	conhecimento.setTpCargaDocumento(new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT)); 
                return;
            }
            
            if(BigDecimalUtils.hasValue(conhecimento.getPsCubadoAferido())){
            	BigDecimal vlLimitePesoCub = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VL_LIMITE_PESO_CUB);
                if(conhecimento.getPsCubadoAferido().compareTo(vlLimitePesoCub) > 0){
                    conhecimento.setTpCargaDocumento(new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT));
                    return;
                }
            }
            
            conhecimento.setTpCargaDocumento(new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_PARCEL)); 
            
        }    
    }
	
	/**
	 * Valida dados pré-calculo Frete
	 * @author André Valadas
	 * 
	 * @param conhecimento
	 * @param parameters
	 */
	private void validateExecuteCalculoFrete(final Conhecimento conhecimento, final Map parameters) {
		//se nao for automatizada e o peso > 1 verificar o peso
		Boolean blEstacaoAutomatizada = parameters.get("blEstacaoAutomatizada") != null ? (Boolean)parameters.get("blEstacaoAutomatizada") : false;	

		/** [LMS-414] - Valida o LIMITE MAXIMO DE VOLUMES PARA CALCULAR FRETE */
		validateLimitePesoVolume(conhecimento.getIdDoctoServico(), conhecimento.getPsAferido(), blEstacaoAutomatizada);

		if (BooleanUtils.isFalse(blEstacaoAutomatizada) && (CompareUtils.gt(conhecimento.getPsReal(),new BigDecimal(1))) ) {
			/** Verificar se o peso aferido ficou superior ao peso limite */ 
			List<LiberacaoDocServ> liberacaoDocServs = (List<LiberacaoDocServ>) parameters.get("liberacaoDocServs");
			if (!ConhecimentoUtils.verifyLiberacaoEmbarque(liberacaoDocServs, ConstantesExpedicao.LIBERACAO_PESO_LIMITE, blEstacaoAutomatizada)) {
				BigDecimal limite = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_CTRC);
				if(limite != null && conhecimento.getPsAferido() != null) {
					if((CompareUtils.gt(conhecimento.getPsAferido(), limite))) { 
						//CQPRO00030818
						String limiteFormatado = FormatUtils.formatDecimal("#,###,##0.00", limite); 
						String valorFormatado = FormatUtils.formatDecimal("#,###,##0.00", conhecimento.getPsAferido());

						throw new BusinessException("LMS-04336",new Object[] {valorFormatado,limiteFormatado});
					}
				} else {
					log.error("O parametro geral " + ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_CTRC + " nao esta configurado. Verificar!");
				}
			} 

			/** Verificar se o peso aferido ficou fora do range permitido */
			if (!ConhecimentoUtils.verifyLiberacaoEmbarque(liberacaoDocServs, ConstantesExpedicao.LIBERACAO_DIFERENCA_PESO, blEstacaoAutomatizada)) {
				BigDecimal limite = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_DIFERENCA_PESO);
				if(limite != null && conhecimento.getPsAferido() != null) {
					BigDecimal limiteSuperior = limite.multiply(conhecimento.getPsReal());
					BigDecimal limiteInferior = com.mercurio.lms.util.BigDecimalUtils.divide(conhecimento.getPsReal(),limite);
					if( (CompareUtils.gt(conhecimento.getPsAferido(), limiteSuperior)) //caso psAferido > psReal * limite
							|| (CompareUtils.lt(conhecimento.getPsAferido(), limiteInferior)) //caso psAferido < psReal / limite
					) {
						throw new BusinessException("LMS-04300");
					}
				} else {
					log.error("O parametro geral " + ConstantesExpedicao.NM_PARAMETRO_LIMITE_DIFERENCA_PESO + " nao esta configurado. Verificar!");
				}
			}

			//CQPRO00030818
			if (!ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_VALOR_MERCADORIA ,blEstacaoAutomatizada)) {
				BigDecimal limite = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_VALOR_MERCADORIA);
				if(limite != null) {
					if (CompareUtils.gt(conhecimento.getVlMercadoria(), limite)) {
						String limiteFormatado = FormatUtils.formatDecimal("#,###,##0.00", limite); 
						String valorFormatado = FormatUtils.formatDecimal("#,###,##0.00", conhecimento.getVlMercadoria());

						throw new BusinessException("LMS-04051",new Object[] {valorFormatado,limiteFormatado});					
					}
				}
			}

		}
	}
	
	/**
	 * [LMS-414] - Valida o LIMITE MAXIMO DE VOLUMES PARA CALCULAR FRETE
	 * @author André Valadas
	 * 
	 * @param idConhecimento
	 * @param psTotalVolumesInformados
	 * @param blEstacaoAutomatizada
	 */
	private void validateLimitePesoVolume(final Long idConhecimento, final BigDecimal psTotalVolumesInformados, final Boolean blEstacaoAutomatizada) {
		final BigDecimal limitePesoVolume = doctoServicoValidateFacade.findLimitePesoVolumeCalculoFrete();
		if (BooleanUtils.isFalse(doctoServicoValidateFacade.validateLimitePeso(psTotalVolumesInformados, limitePesoVolume))) {

			if(BooleanUtils.isFalse(blEstacaoAutomatizada)) {
				throw new BusinessException("LMS-04354", new Object[] {FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_3_CASAS, psTotalVolumesInformados), FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_3_CASAS, limitePesoVolume)});
			} else {
				/** Busca Parametros de validacao */
				final String dsObservacao = configuracoesFacade.getMensagem("LMS-04355", new Object[] {FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_3_CASAS, limitePesoVolume)});

				/** Gera Evento de Cancelamento no CTRC */
				conhecimentoCancelarService.storeEventoCancelamentoLimiteCalculoFrete(idConhecimento, dsObservacao);
			}
		}
	}

	/**
	 * Chama rotina gravaCtrcPrimeiraFata da action DigitarDadosNotaNormalCalculoCTRCAction, 
	 * apenas materializando o conhecimento.
	 * @return
	 */
	public Serializable gravaCtrcPrimeiraFase(Map parameters) {
		Conhecimento conhecimento = (Conhecimento)parameters.get("conhecimento");
		if (conhecimento == null) {
			conhecimento = (Conhecimento) conhecimentoService.findById((Long)parameters.get("idDoctoServico"));
			parameters.put("conhecimento", conhecimento);
		}
	
		return gravaCtrcSegundaFase(parameters);
		
	}

	/**
	 * Grava CTRC 2º Fase.
	 * @return
	 */
	public Serializable gravaCtrcSegundaFase(Map parameters) {
		Conhecimento conhecimento = (Conhecimento)parameters.get("conhecimento");
		boolean calculoManual = ConstantesExpedicao.CALCULO_MANUAL.equals(conhecimento.getTpCalculoPreco().getValue());
		if (!calculoManual && !ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO.equals(conhecimento.getTpConhecimento().getValue())) {
			CalculoFrete calculoFrete = (CalculoFrete)parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
			ExpedicaoUtils.setCalculoFreteInSession(calculoFrete);
			Serializable result = conhecimentoNormalService.storeConhecimento(conhecimento, ConstantesExpedicao.TIPO_ICMS); 
			ExpedicaoUtils.removeCalculoFreteFromSession();
			return result;
		} else return conhecimentoService.store(conhecimento);
	}	

	public void executeCalcularFreteTabelaDiferenciada(List<Long> listConhecimentos) {
		long starttime = System.currentTimeMillis();

		if (listConhecimentos != null) {
			for (Long idConhecimento : listConhecimentos) {
				try {
					BigDecimal vlTotalFrete = calculaVlFreteTabelaDiferenciada(idConhecimento);

					Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);
					conhecimento.setVlFreteTabelaCheia(vlTotalFrete);
					conhecimentoService.store(conhecimento);

				} catch (BusinessException be) {
					log.warn("[CALC FRETE-TAB DIFERENCIADA] idConhecimento = " + idConhecimento.longValue() + " - " + be.getMessage());
					log.error(be);
				} catch (Exception e) {
					log.warn("[CALC FRETE-TAB DIFERENCIADA] idConhecimento = " + idConhecimento.longValue() + " - " + e.getMessage());
					log.error(e);
				}
			}
		}
		long endtime = System.currentTimeMillis() - starttime;
		log.warn("[CALC FRETE-TAB DIFERENCIADA] Tempo de execucao = " + endtime);
	}


	/**
	 * Calculo frete tabela diferenciada
	 * 
	 * @param idConhecimento
	 * @return
	 */
	private BigDecimal calculaVlFreteTabelaDiferenciada(Long idConhecimento) {
		// Sessão para aplicar EVICT no CTO modificado...
		Session session = conhecimentoService.getConhecimentoDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();

		Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);

		/*Monta o objeto conhecimento para cálculo*/
		Conhecimento conhecimentoNovo =	ConhecimentoUtils.cloneConhecimento(conhecimento);
		conhecimentoNovo.setTpCalculoPreco(new DomainValue(ConstantesExpedicao.CALCULO_NORMAL));
		conhecimentoNovo.getClienteByIdClienteRemetente().setTpCliente(new DomainValue(ConstantesVendas.CLIENTE_ESPECIAL));

		/*Monta o objeto para calcular o frete*/
		CalculoFrete calculoFrete = new CalculoFrete();
		calculoFrete.setRecalculoFrete(Boolean.TRUE);
		calculoFrete.setTabelaPrecoRecalculo(conhecimento.getTabelaPreco());
		calculoFrete.setIdServico(conhecimento.getServico().getIdServico());
		calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
		calculoFrete.setBlCalculoFreteTabelaCheia(Boolean.FALSE);
		calculoFrete.setBlCalculaServicosAdicionais(conhecimento.getBlServicosAdicionais());
		calculoFrete.setClienteBase(conhecimento.getClienteByIdClienteBaseCalculo());
		calculoFrete.setIdDivisaoCliente(conhecimento.getDivisaoCliente() != null ? conhecimento.getDivisaoCliente().getIdDivisaoCliente() : null);


		/*Monta o objeto Frete para calculo*/
		Frete frete = new Frete(); 
		frete.setConhecimento(conhecimentoNovo);
		frete.setCalculoFrete(calculoFrete);

		/*Configura os dados do calculo de frete atraves do conhecimento*/
		conhecimentoNormalService.configureCalculoFrete(frete.getConhecimento(),frete.getCalculoFrete());

		/*Executa o cálculo do frete*/			
		documentoServicoFacade.executeCalculoConhecimentoNacionalNormal(frete.getCalculoFrete());		

		/*Copia os dados do calculo de frete para o conhecimento*/
		CalculoFreteUtils.copyResult(frete.getConhecimento(), frete.getCalculoFrete());

		/*Retorna o map contendo todos os valores calculados do frete*/
		Map map = montarParcelasCalculo(frete.getConhecimento(),frete.getCalculoFrete());
		BigDecimal vlTotalFrete = (BigDecimal)map.get("vlTotalFrete");

		session.evict(conhecimento);

		return vlTotalFrete;
	}


	

	/**
	 * Busca as parcelas de recalculo
	 * 
	 * @param frete
	 * @param idDoctoRecalculo
	 */
	public List<Map> findParcelasRecalculo(Frete frete, Long idDoctoRecalculo){

		List<Map> parcelas = new ArrayList<Map>();
		if(frete != null && frete.getConhecimento() != null && CollectionUtils.isNotEmpty(frete.getConhecimento().getParcelaDoctoServicos())){

			for(ParcelaDoctoServico pds : frete.getConhecimento().getParcelaDoctoServicos()){
				Map<String, Object> parcela = new HashMap<String, Object>();
				parcela.put("idDoctoServico", idDoctoRecalculo);
				parcela.put("idParcelaPreco", pds.getParcelaPreco().getIdParcelaPreco());
				parcela.put("vlParcela", pds.getVlParcela());
				parcelas.add(parcela);
			}

			/*Adiciona o desconto como parcela*/
			if(BigDecimalUtils.hasValue(frete.getCalculoFrete().getVlDesconto())){

				ParcelaPreco parcelaPrecoDesconto = parcelaPrecoService.findByCdParcelaPreco("IDDesconto");

				Map<String, Object> parcela = new HashMap<String, Object>();
				parcela.put("idDoctoServico", idDoctoRecalculo);
				parcela.put("idParcelaPreco", parcelaPrecoDesconto.getIdParcelaPreco());
				parcela.put("vlParcela", frete.getCalculoFrete().getVlDesconto());
				parcelas.add(parcela);

			}
		}			
		return parcelas;
	}

	/**
	 * Recalculo de frete 
	 * 
	 * @param idConhecimento
	 * @return
	 */
	public Frete executeRecalculoFrete(final Long idConhecimento){

		Session session = conhecimentoService.getConhecimentoDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();

		Frete frete = null;
		try {
			final Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);

			frete = executeRecalculoFreteTabela(conhecimento, true);

			if(frete != null){
				montarParcelasCalculo(frete.getConhecimento(),frete.getCalculoFrete());
			}

			session.evict(conhecimento);

		} catch (BusinessException be) {
			log.warn("[RECALCULO FRETE TABELA Y] idConhecimento = " + idConhecimento + " - " + be.getMessage());
		} catch (Exception e) {
			log.warn("[RECALCULO FRETE TABELA Y] idConhecimento = " + idConhecimento + " - " + e.getMessage());
		}

		return frete;		
	}

	public void storeValorRecalculo(Long idConhecimento, BigDecimal vlTotalFrete){
		Session session = conhecimentoService.getConhecimentoDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();
		final Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);
		conhecimento.setVlFreteTabelaCheia(BigDecimalUtils.defaultBigDecimal(vlTotalFrete));
		conhecimentoService.store(conhecimento);
		session.flush();
	}

	
	/**
	 * Calculo frete tabela
	 * 
	 * @param  Conhecimento
	 * @param  recalculoLMS //--> Caso "TRUE" indica que o calculo de frete vai ser feito através 
	 * de um CTRC ja calculado no LMS. Vai utilizar dados de TarifaPreco , ParametroCliente e TabelaPreco
	 * do próprio CTRC na base LMS, caso contrário "FALSE" vai calcular normalmente pois, o CTRC
	 * não existe na base do LMS  (Calculo de frete através de arquivo)
	 * 
	 * @return Frete
	 */
	public Frete executeRecalculoFreteTabela(final Conhecimento conhecimento, final Boolean recalculoLMS) {

		/*Monta o objeto conhecimento para cálculo*/
		Conhecimento conhecimentoNovo =	ConhecimentoUtils.cloneConhecimento(conhecimento);
		conhecimentoNovo.setTpCalculoPreco(new DomainValue(ConstantesExpedicao.CALCULO_NORMAL));
		conhecimentoNovo.getClienteByIdClienteRemetente().setTpCliente(new DomainValue(ConstantesVendas.CLIENTE_ESPECIAL));

		/*Monta o objeto para calcular o frete*/
		CalculoFrete calculoFrete = new CalculoFrete();
		calculoFrete.setRecalculoFrete(recalculoLMS);
		calculoFrete.setDhEmissaoDocRecalculo(conhecimento.getDhEmissao());
		calculoFrete.setPsReferenciaCalculo(conhecimento.getPsReferenciaCalculo());
		calculoFrete.setTarifaPreco(conhecimento.getTarifaPreco());
		calculoFrete.setParametroCliente(conhecimento.getParametroCliente());
		calculoFrete.setTabelaPrecoRecalculo(conhecimento.getTabelaPreco());
		calculoFrete.setIdServico(conhecimento.getServico().getIdServico());
		calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
		calculoFrete.setBlCalculoFreteTabelaCheia(Boolean.FALSE);
		calculoFrete.setBlCalculaServicosAdicionais(conhecimento.getBlServicosAdicionais());
		calculoFrete.setClienteBase(conhecimento.getClienteByIdClienteBaseCalculo());
		calculoFrete.setIdDivisaoCliente(conhecimento.getDivisaoCliente() != null ? conhecimento.getDivisaoCliente().getIdDivisaoCliente() : null);
		calculoFrete.setClienteBase(conhecimento.getClienteByIdClienteBaseCalculo());


		/*Monta o objeto Frete para calculo*/
		Frete frete = new Frete(); 
		frete.setConhecimento(conhecimentoNovo);
		frete.setCalculoFrete(calculoFrete);

		/*Configura os dados do calculo de frete atraves do conhecimento*/
		conhecimentoNormalService.configureCalculoFrete(frete.getConhecimento(), frete.getCalculoFrete());

		/*Executa o cálculo do frete*/			
		documentoServicoFacade.executeCalculoConhecimentoNacionalNormal(frete.getCalculoFrete());		

		/*Copia os dados do calculo de frete para o conhecimento*/
		CalculoFreteUtils.copyResult(frete.getConhecimento(), frete.getCalculoFrete());

		return frete;
	}	

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}
	public void setDocumentoServicoFacade(DocumentoServicoFacade documentoServicoFacade) {
		this.documentoServicoFacade = documentoServicoFacade;
	}
	public void setCalcularFreteTabelaCheiaService(CalcularFreteTabelaCheiaService calcularFreteTabelaCheiaService) {
		this.calcularFreteTabelaCheiaService = calcularFreteTabelaCheiaService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}
	public void setBatchLogger(BatchLogger batchLogger) {
		this.batchLogger = batchLogger;
		this.batchLogger.logClass(CalcularFreteService.class);
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	public void setDoctoServicoValidateFacade(DoctoServicoValidateFacade doctoServicoValidateFacade) {
		this.doctoServicoValidateFacade = doctoServicoValidateFacade;
	}
	public void setConhecimentoCancelarService(ConhecimentoCancelarService conhecimentoCancelarService) {
		this.conhecimentoCancelarService = conhecimentoCancelarService;
	}
	public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}
	public void setDpeService(DpeService dpeService) {
		this.dpeService = dpeService;
	}
}
