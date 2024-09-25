package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroFilialService;
import com.mercurio.lms.expedicao.dto.EVersaoXMLCTE;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.model.MonitoramentoDescarga;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.util.ClienteUtils;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.conhecimentoDevolucaoService"
 */

public class ConhecimentoSubstitutoService extends AbstractConhecimentoNormalService {
	private static final String ATIVO = "A";
	private static final String ROTAS_VERDES = "Rotas Verdes";
	private ConhecimentoService conhecimentoService;
	private ReciboReembolsoService reciboReembolsoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private DigitarNotaService digitarNotaService;
	private ParametroGeralService parametroGeralService;
	private ConhecimentoNormalService conhecimentoNormalService;
	private CalcularFreteService calcularFreteService;
	private DigitarDadosNotaNormalCalculoCTRCService digitarDadosNotaNormalCalculoCTRCService;
	private DevedorDocServFatService devedorDocServFatService;
	private ParametroFilialService parametroFilialService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	
	public List<Map<String, Object>> findDoctoServicoOriginalParaSubstituir(Map<String, Object> criteria) {
		
		if( criteria.get("nrDoctoServico") == null ){
			return null;
		}
		
		List<Conhecimento> conhecimentosOriginais = conhecimentoService.findByCriteria(criteria);
		
		if( conhecimentosOriginais == null || conhecimentosOriginais.isEmpty() || conhecimentosOriginais.size() > 1 ){
			return null;
		}
		
		validateDoctoServicoOriginalParaSubstituir(conhecimentosOriginais.get(0));
		return montaRetorno(conhecimentosOriginais.get(0));
	}


	private List<Map<String, Object>> montaRetorno(Conhecimento conhecimento) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();

		if(conhecimento != null) {
			Map<String, Object> doctoServico = new HashMap<String, Object>();

			doctoServico.put("conhecimento", (Conhecimento)conhecimento);
			
			doctoServico.put("idDoctoServico", conhecimento.getIdDoctoServico());
			
			doctoServico.put("nrDoctoServico", FormatUtils.formataNrDocumento(conhecimento.getNrDoctoServico().toString(), ConstantesExpedicao.CONHECIMENTO_ELETRONICO) );
			doctoServico.put("nrConhecimento", FormatUtils.formataNrDocumento(conhecimento.getNrConhecimento().toString(), ConstantesExpedicao.CONHECIMENTO_ELETRONICO) );
			
			doctoServico.put("dhEmissao", conhecimento.getDhEmissao());
			doctoServico.put("vlTotalDocServico", conhecimento.getVlTotalDocServico());
			
			Filial filialDestino = conhecimento.getFilialByIdFilialDestino();
			if(filialDestino != null){
				doctoServico.put("sgFilialDestino", filialDestino.getSgFilial());
				doctoServico.put("nmFilialDestino", filialDestino.getPessoa().getNmFantasia());
			}
			
			Cliente clienteRemetente = conhecimento.getClienteByIdClienteRemetente();
			if(clienteRemetente != null){
				//TODO: remover este comentário quando a ET for atualizada para buscar cotação por CTRC
				doctoServico.put("cotacoes", findCotacao(clienteRemetente.getIdCliente(), ConstantesExpedicao.CONHECIMENTO_NACIONAL));
				
				doctoServico.put("idClienteRemetente", clienteRemetente.getIdCliente());
				doctoServico.put("nmPessoaRemetente", clienteRemetente.getPessoa().getNmPessoa());
				doctoServico.put("nrIdentificacaoRemetente", 
						FormatUtils.formatIdentificacao(clienteRemetente.getPessoa().getTpIdentificacao().getValue(),
								clienteRemetente.getPessoa().getNrIdentificacao()));
			}

			Cliente clienteDestinatario = conhecimento.getClienteByIdClienteDestinatario();
			if(clienteDestinatario != null){
				doctoServico.put("idClienteDestinatario", clienteDestinatario.getIdCliente());
				doctoServico.put("nmPessoaDestinatario", clienteDestinatario.getPessoa().getNmPessoa());
				doctoServico.put("nrIdentificacaoDestinatario", 
						FormatUtils.formatIdentificacao(clienteDestinatario.getPessoa().getTpIdentificacao().getValue(), clienteDestinatario.getPessoa().getNrIdentificacao()) );
			}
			
			Cliente clienteConsignatario = conhecimento.getClienteByIdClienteConsignatario();
			if(clienteConsignatario != null){
				doctoServico.put("idClienteConsignatario", clienteConsignatario.getIdCliente());
			}

			doctoServico.put("blObrigaSerie", false);
			if(conhecimento.getDevedorDocServFats() != null && !conhecimento.getDevedorDocServFats().isEmpty()){
				DevedorDocServFat devedorDocServFat = conhecimento.getDevedorDocServFats().get(0);
				if(devedorDocServFat.getCliente() != null){
					doctoServico.put("blObrigaSerie", devedorDocServFat.getCliente().getBlObrigaSerie());
				}
			}
			
			result.add(doctoServico);
		}
		
		return result;
	}


	public void validateDoctoServicoOriginalParaSubstituir(Conhecimento conhecimentoOriginal) {
		
		if(conhecimentoOriginal.getTpSituacaoConhecimento() == null || 
				!ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO.equalsIgnoreCase(conhecimentoOriginal.getTpSituacaoConhecimento().getValue())){
			throw new BusinessException("LMS-04453");
		}
		
		if(conhecimentoOriginal.getTpConhecimento() == null && 
				(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equalsIgnoreCase(conhecimentoOriginal.getTpConhecimento().getValue())
						|| ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equalsIgnoreCase(conhecimentoOriginal.getTpConhecimento().getValue()))
						|| ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimentoOriginal.getTpConhecimento().getValue())){
			throw new BusinessException("LMS-04454");
		}

		//•	Verificar se existe algum conhecimento do tipo substituto onde o conhecimento original é igual ao conhecimento digitado
		List<Conhecimento> conhecimentosSubstitutos = conhecimentoService.findConhecimentoSubstitutoByIdConhecimentoOrigem(conhecimentoOriginal.getIdDoctoServico());
		if(conhecimentosSubstitutos != null && !conhecimentosSubstitutos.isEmpty() && conhecimentosSubstitutos.get(0).getIdDoctoServico() != null){
			StringBuilder conhSubstituto = new StringBuilder();
			
			conhSubstituto.append(conhecimentosSubstitutos.get(0).getTpDoctoServico().getDescription() + " ");
			conhSubstituto.append(conhecimentosSubstitutos.get(0).getFilialByIdFilialOrigem().getSgFilial() + " ");
			conhSubstituto.append(conhecimentosSubstitutos.get(0).getNrDoctoServico());
			
			throw new BusinessException("LMS-04433",new Object[] {conhSubstituto.toString()});
		}

		
		//•	Verificar se o documento original está pendente de faturamento:
		if(conhecimentoOriginal.getDevedorDocServFats() != null && !conhecimentoOriginal.getDevedorDocServFats().isEmpty()
				&& conhecimentoOriginal.getDevedorDocServFats().size() < 2){
			DevedorDocServFat devedorDocServFat = conhecimentoOriginal.getDevedorDocServFats().get(0);
			
			if(devedorDocServFat.getTpSituacaoCobranca() != null 
					&& !ConstantesExpedicao.TP_DEVEDOR_REDESPACHO.equalsIgnoreCase(devedorDocServFat.getTpSituacaoCobranca().getValue())
 					&& !ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO.equalsIgnoreCase(devedorDocServFat.getTpSituacaoCobranca().getValue())) {

				throw new BusinessException("LMS-04436");
			}

		}
		
		//•	Verificar se a diferença entre a data de emissão do documento original e a data atual é maior que a quantidade de dias constante em PARAMETRO_GERAL.PRAZO_SUBST_CTE
		Object prazoSubstituicao = parametroGeralService.findConteudoByNomeParametro("PRAZO_SUBST_CTE", false);
		if(prazoSubstituicao != null && prazoSubstituicao instanceof BigDecimal){
			DateTime prazoFinalSubstituicao = conhecimentoOriginal.getDhEmissao().plusDays(((BigDecimal)prazoSubstituicao).intValue());
			if(prazoFinalSubstituicao.isBefore(new DateTime())){

				throw new BusinessException("LMS-04435");
			}
		}

	}
	

	@SuppressWarnings("unchecked")
	public List<Object> findCotacao(Long idClienteRemetente, String tpDocumento) {
		List<Object> result = cotacaoService.findCotacoes(idClienteRemetente, tpDocumento);
		for (Iterator<Object> iter = result.iterator(); iter.hasNext();) {
			Map<String, Object> map = (Map<String, Object>) iter.next();
			map.put("nrCotacao", map.remove("sgFilial") + "-" + map.get("nrCotacao"));
		}
		return result;
	}
	
	/**
	 * Rotina: GravaCTRCSubstituto
	 * Esta rotina tem por objetivo gravar as informações do CTRC no banco de dados. 
	 * 
	 * @param idConhecimentoOriginal
	 * @param conhecimentoCalculo
	 * @param mapNotasFiscaisConhecimento
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Serializable storeConhecimentoSubstitutoManual(Map<String, Object> parameters) {
		
		Conhecimento conhecimentoDadosTela = (Conhecimento) parameters.get("conhecimento");
		CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		Map<String, Object> mapMeioTransporte = (HashMap<String, Object>)parameters.get("meioTransporte");
		Long idConhecimentoOriginal = conhecimentoDadosTela.getDoctoServicoOriginal().getIdDoctoServico();
		Conhecimento conhecimentoOriginal = conhecimentoService.findById(idConhecimentoOriginal);
		
		validaValorFreteIcms(calculoFrete, conhecimentoOriginal);
		
		Conhecimento conhecimentoNovo = generateConhecimentoSubstitutoByConhecimentoOriginalAndDadosTela(conhecimentoOriginal, conhecimentoDadosTela);

		//Tabela PARCELA_DOCTO_SERVICO 
		ConhecimentoUtils.copyParcelasDoctoServico(conhecimentoDadosTela, conhecimentoNovo);

		/** Busca Meio de Transporte */
		final Map<String, String> mapSituacaoDescarga = new HashMap<String, String>();
		final MeioTransporte meioTransporte = conhecimentoNormalService.createMeioTransporte(conhecimentoNovo, mapMeioTransporte, mapSituacaoDescarga);
		ExpedicaoUtils.setCalculoFreteInSession(calculoFrete);
		
		
		Serializable serializable = conhecimentoNormalService.storeConhecimento(conhecimentoNovo, meioTransporte, ConstantesExpedicao.TIPO_ICMS, mapSituacaoDescarga.get("tpSituacaoDescarga"));
		
		executeFinalizacaoStoreSubstituto(idConhecimentoOriginal, (Long)serializable);
		
		ExpedicaoUtils.removeCalculoFreteFromSession();
		
		return serializable;
	}

	public Serializable storeConhecimentoSubstituto(Map<String, Object> parameters) {
		Serializable serializable = digitarDadosNotaNormalCalculoCTRCService.storeCtrcSegundaFasePreCalculoFrete(parameters);
		
		if(parameters.get("conhecimento") instanceof Conhecimento){
			Conhecimento conhecimento = conhecimentoService.findById((Long)serializable);

			setNrCfop(conhecimento);
			conhecimento.setBlPesoAferido(true);

			Serializable idConhecimentoNovo = conhecimentoService.store(conhecimento);
			
			storeValorDevidoDevedorDocSerFat(conhecimento);
			storeValorDevidoDevedorDocSer(conhecimento);
			
			if(conhecimento.getDoctoServicoOriginal() != null){
				executeFinalizacaoStoreSubstituto(conhecimento.getDoctoServicoOriginal().getIdDoctoServico(), (Long)idConhecimentoNovo);
			}
		}
		
		return serializable;
	}


	private void storeValorDevidoDevedorDocSer(Conhecimento conhecimento) {
		if(conhecimento.getDevedorDocServs() != null && conhecimento.getDevedorDocServs().size() == 1 && conhecimento.getVlLiquido() != null){
			DevedorDocServ devedorDocServ = conhecimento.getDevedorDocServs().get(0);
			if(devedorDocServ.getVlDevido() == null || devedorDocServ.getVlDevido().compareTo(BigDecimal.ZERO) == 0){
				devedorDocServ.setVlDevido(conhecimento.getVlLiquido());
				devedorDocServService.store(devedorDocServ);
			}
		}
	}


	private void storeValorDevidoDevedorDocSerFat(Conhecimento conhecimento) {
		if(conhecimento.getDevedorDocServFats() != null && conhecimento.getDevedorDocServFats().size() == 1 && conhecimento.getVlLiquido() != null){
			DevedorDocServFat devedorDocServFat = conhecimento.getDevedorDocServFats().get(0);
			if(devedorDocServFat.getVlDevido() == null || devedorDocServFat.getVlDevido().compareTo(BigDecimal.ZERO) == 0){
				devedorDocServFat.setVlDevido(conhecimento.getVlLiquido());
				devedorDocServFatService.store(devedorDocServFat);
			}
		}
	}

	private void executeFinalizacaoStoreSubstituto(Long idConhecimentoOriginal, Long idConhecimentoNovo) {

		if(idConhecimentoOriginal != null && idConhecimentoNovo != null){
			Conhecimento conhecimentoOriginal = conhecimentoService.findById(idConhecimentoOriginal);
			Conhecimento conhecimentoNovo = conhecimentoService.findById(idConhecimentoNovo);
			
			MonitoramentoDescarga monitoramentoDescarga = createMonitoramentoDescarga(conhecimentoOriginal, conhecimentoNovo);
			executeVolumeNotaFiscal(conhecimentoNovo, monitoramentoDescarga);
			
			//Cancela Recibos de Reembolso Ativos do Conhecimento
			reciboReembolsoService.executeCancelaReciboByDoctoServico(idConhecimentoOriginal);
		}
	}

	private void validaValorFreteIcms(CalculoFrete calculoFrete, Conhecimento conhecimentoOriginal) {
		if(calculoFrete != null && calculoFrete.getDoctoServico() != null && calculoFrete.getDoctoServico().getVlTotalDocServico() != null  
				&& conhecimentoOriginal != null && conhecimentoOriginal.getVlTotalDocServico() != null){
			if(calculoFrete.getDoctoServico().getVlTotalDocServico().compareTo(conhecimentoOriginal.getVlTotalDocServico()) > 0 ){
				throw new  BusinessException("LMS-04439");
			}
		}
	}
	
	public Conhecimento generateConhecimentoSubstitutoByConhecimentoOriginalAndDadosTela(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoDadosTela) {
		Conhecimento conhecimentoNovo = new Conhecimento();
		
		Filial filial = SessionUtils.getFilialSessao();
		
//		•	ID_DOCTO_SERVICO = Gerado 
//		•	NR_DOCTO_SERVICO = <Próximo número de Pré-CTRC>
		
//		•	ID_MOEDA = conteúdo do registro do CTRC Original
		conhecimentoNovo.setMoeda(conhecimentoOriginal.getMoeda());
		
//		•	ID_CLIENTE_REMETENTE = conteúdo do campo ID_CLIENTE_REMETENTE do registro do CTRC Original
		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteRemetente());
		
//		•	ID_SERVICO = conteúdo do registro do CTRC Original
		Hibernate.initialize( conhecimentoOriginal.getServico().getTpAbrangencia() );
		conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
		
//		•	ID_USUARIO_INCLUSAO = Usuário logado (USUARIO.ID_USUARIO)
		conhecimentoNovo.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		
//		•	ID_FILIAL_ORIGEM = Filial do usuário logado (FILIAL.ID_FILIAL)
		conhecimentoNovo.setFilialByIdFilialOrigem(filial);
		
//		•	VL_TOTAL_PARCELAS = Valor retornado no processo de cálculo do frete
		conhecimentoNovo.setVlTotalParcelas(conhecimentoDadosTela.getVlTotalParcelas());
		
//		•	VL_TOTAL_DOC_SERVICO = Conforme Total calculado no frete
		conhecimentoNovo.setVlTotalDocServico(conhecimentoDadosTela.getVlTotalDocServico());
		
//		•	VL_DESCONTO = Conforme Desconto calculado no frete
		conhecimentoNovo.setVlDesconto(conhecimentoDadosTela.getVlDesconto());
		
//		•	VL_IMPOSTO = Valor do ICMS calculado no processo de cálculo do frete
		conhecimentoNovo.setVlImposto(conhecimentoDadosTela.getVlImposto());
		
//		•	VL_BASE_CALC_IMPOSTO = Valor Base de calculo do Imposto calculado no processo de cálculo do frete
		conhecimentoNovo.setVlBaseCalcImposto(conhecimentoDadosTela.getVlBaseCalcImposto());

//		•	PS_REFERENCIA_CALCULO = Conteúdo do campo peso de referência para cálculo definido no processo de cálculo do frete
		conhecimentoNovo.setPsReferenciaCalculo(conhecimentoDadosTela.getPsReferenciaCalculo());
		
//		•	ID_TABELA_PRECO = ID da tabela de preços (TABELA_PRECO.ID_TABELA_PRECO) resultante do processo de cálculo do frete
		conhecimentoNovo.setTabelaPreco(conhecimentoDadosTela.getTabelaPreco());
		
//		•	VL_ICMS_ST = valor da substituição tributário que retorna do cálculo do ICMS
		conhecimentoNovo.setVlIcmsSubstituicaoTributaria(conhecimentoDadosTela.getVlIcmsSubstituicaoTributaria());
		
//		•	VL_FRETE_LIQUIDO = valor do VL_DEVIDO que retorna do cálculo do ICMS
		conhecimentoNovo.setVlLiquido(conhecimentoDadosTela.getVlLiquido());
		
//		•	VL_FRETE_PESO_DECLARADO = valor do VL_DEVIDO que retorna do cálculo do ICMS
		conhecimentoNovo.setVlFretePesoDeclarado(conhecimentoDadosTela.getVlFretePesoDeclarado());
		
//		•	DH_EMISSAO = Data e hora correntes
		conhecimentoNovo.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
		
//		•	DH_INCLUSAO = Data e hora correntes
		conhecimentoNovo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		
//		•	TP_DOCUMENTO_SERVICO = ‘CTE’
		conhecimentoNovo.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
		
//		•	BL_BLOQUEADO = “N”
		conhecimentoNovo.setBlBloqueado(Boolean.FALSE);
		
//		•	DT_PREV_ENTREGA = NULL
		conhecimentoNovo.setDtPrevEntrega(null);
		
//		•	ID_IE_DESTINATARIO = conteúdo do campo ID_IE_DESTINATARIO do registro do CTRC Original
		conhecimentoNovo.setInscricaoEstadualDestinatario(conhecimentoOriginal.getInscricaoEstadualDestinatario());
		
//		•	ID_IE_REMETENTE = conteúdo do campo IE_REMETENTE do registro do CTRC Original
		conhecimentoNovo.setInscricaoEstadualRemetente(conhecimentoOriginal.getInscricaoEstadualRemetente());
		
//		•	ID_PAIS = conteúdo do campo ID_PAIS do registro do CTRC Original
		conhecimentoNovo.setPaisOrigem(conhecimentoOriginal.getPaisOrigem());
		
//		•	PC_ALIQUOTA_ICMS = Valor retornado no processo de cálculo do frete
		conhecimentoNovo.setPcAliquotaIcms(conhecimentoDadosTela.getPcAliquotaIcms());
		
//		•	ID_PARAMETRO_CLIENTE = Valor retornado no processo de cálculo do frete 
		conhecimentoNovo.setParametroCliente(conhecimentoDadosTela.getParametroCliente());
		
//		•	ID_FILIAL_DESTINO = Conforme campo Filial de Destino (FILIAL.ID_FILIAL)
		Hibernate.initialize( conhecimentoOriginal.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa() );
		conhecimentoNovo.setFilialByIdFilialDestino(conhecimentoDadosTela.getFilialByIdFilialDestino());
		
//		•	ID_FILIAL_DESTINO_OPERACIONAL = conteúdo do campo ID_FILIAL_DESTINO
		conhecimentoNovo.setFilialDestinoOperacional(conhecimentoDadosTela.getFilialDestinoOperacional());
		
//		•	ID_CLIENTE_DESTINATARIO = conteúdo do campo ID_CLIENTE_DESTINATARIO  do registro do CTRC Original
		Hibernate.initialize( conhecimentoOriginal.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa() );
		conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimentoOriginal.getClienteByIdClienteDestinatario());
		
//		•	ID_ROTA_COLETA_ENTREGA_REAL = NULL
		conhecimentoNovo.setRotaColetaEntregaByIdRotaColetaEntregaReal(null);
		
//		•	ID_ROTA_COLETA_ENTREGA_SUGERID = conteúdo do campo ID_ROTA_COLETA_ ENTREGA_SUGERID do registro do CTRC Original
		conhecimentoNovo.setRotaColetaEntregaByIdRotaColetaEntregaSugerid(conhecimentoOriginal.getRotaColetaEntregaByIdRotaColetaEntregaSugerid());
		
//		•	ID_ROTA_INTERVALO_CEP = conteúdo do campo ID_ROTA_INTERVALO_CEP do registro do CTRC Original
		conhecimentoNovo.setRotaIntervaloCep(conhecimentoOriginal.getRotaIntervaloCep());
		
//		•	ID_ENDERECO_ENTREGA = ID do endereço do Destinatário do CTRC Original
		conhecimentoNovo.setEnderecoPessoa(conhecimentoOriginal.getEnderecoPessoa());
		
//		•	ID_USUARIO_ALTERACAO = NULL
		conhecimentoNovo.setUsuarioByIdUsuarioAlteracao(null);
		
//		•	DH_ALTERACAO =  NULL
		conhecimentoNovo.setDhAlteracao(null);
		
//		•	DH_ENTRADA_SETOR_ENTREGA = NULL
		conhecimentoNovo.setDhEntradaSetorEntrega(null);
		
//		•	TP_CALCULO_PRECO = Conforme campo Tipo de cálculo
		conhecimentoNovo.setTpCalculoPreco(conhecimentoDadosTela.getTpCalculoPreco());
		
//		•	BL_PRIORIDADE_CARREGAMENTO = NULL
		conhecimentoNovo.setBlPrioridadeCarregamento(null);
		
//		•	NR_AIDF = NULL
		conhecimentoNovo.setNrAidf(null);
		
//		•	ID_DOCTO_SERVICO_ORIGINAL = Conforme campo CTRC Original (ID_DOCTO_SERVICO)
		conhecimentoNovo.setDoctoServicoOriginal(conhecimentoOriginal);
		
//		•	ID_PEDIDO_COLETA = conteúdo do registro do CTRC Original
		if(conhecimentoOriginal.getPedidoColeta() != null){
			Hibernate.initialize( conhecimentoOriginal.getPedidoColeta().getTpModoPedidoColeta() );
		}
		conhecimentoNovo.setPedidoColeta(conhecimentoOriginal.getPedidoColeta());
		
//		•	ID_CLIENTE_REDESPACHO = conteúdo do campo ID_CLIENTE_REDESPACHO do CTRC original
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
		
//		•	ID_CLIENTE_CONSIGNATARIO = conteúdo do campo ID_CLIENTE_CONSIGNATARIO  do CTRC original
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());
		
//		•	DS_ENDERECO_ENTREGA_REAL = Endereço do destinatário do novo CTRC por extenso com a seguinte formatação:
//		TIPO_LOGRADOURO.DS_TIPO_LOGRADOURO 		+ “ ” + ENDERECO_PESSOA.DS_ENDERECO		+ “, nº “ + ENDERECO_PESSOA.NR_ENDERECO		+ “ – ” + ENDERECO_PESSOA.DS_BAIRRO		+ “ - ” + MUNICIPIO.NM_MUNICIPIO		+ “/” + UNIDADE_FEDERATIVA.SG_UNIDADE_FEDERATIVA		+ “ – CEP ” + ENDERECO_PESSOA.NR_CEP
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdPessoa(conhecimentoNovo.getClienteByIdClienteDestinatario().getIdCliente());
		conhecimentoNovo.setDsEnderecoEntregaReal(ConhecimentoUtils.getEnderecoEntregaReal(enderecoPessoa));
		
//		•	BL_INCIDENCIA_ICMS_PEDAGIO = Valor retornado no processo de cálculo do frete 
		conhecimentoNovo.setBlIncideIcmsPedagio(conhecimentoDadosTela.getBlIncideIcmsPedagio());
		
//		•	ID_CLIENTE_BASE_CALCULO = Conforme definido no item 1
		if(conhecimentoOriginal.getClienteByIdClienteBaseCalculo() == null){
			conhecimentoNovo.setClienteByIdClienteBaseCalculo(conhecimentoDadosTela.getClienteByIdClienteBaseCalculo());
		}else{
			conhecimentoNovo.setClienteByIdClienteBaseCalculo(conhecimentoOriginal.getClienteByIdClienteBaseCalculo());
		}
		
//		•	ID_TARIFA_PRECO = Conforme tarifa definida no cálculo do frete
		conhecimentoNovo.setTarifaPreco(conhecimentoDadosTela.getTarifaPreco());
		
//		•	PS_AFORADO = conforme campo Peso Aforado (PS_AFORADO) gerado no processo de cálculo do frete
		conhecimentoNovo.setPsAforado(conhecimentoDadosTela.getPsAforado());
		
//		•	BL_SPECIAL_SERVICE = “N”
		conhecimentoNovo.setBlSpecialService(Boolean.FALSE);
		
//      •   TP_CONHECIMENTO = Conforme campo Tipo de CTRC 
		conhecimentoNovo.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO));
		
//		•	ID_FLUXO_FILIAL = chamar a função setFluxoCarga (conforme abaixo) enviando os seguintes parâmetros: 
		setFluxoCarga(conhecimentoNovo, Boolean.FALSE);
		
//		•	NR_CHAVE_NF_ANULACAO = Se campo Número/Chave da NF de anulação possui 44 dígitos, então conforme campo Número/Chave da NF de anulação
		conhecimentoNovo.setNrChaveNfAnulacao(conhecimentoDadosTela.getNrChaveNfAnulacao());
		
//		•	NR_NF_ANULACAO = Se campo Número/Chave da NF de anulação possui até 6 dígitos, então conforme campo Número/Chave da NF de  anulação
		conhecimentoNovo.setNrNfAnulacao(conhecimentoDadosTela.getNrNfAnulacao());
		
//		•	DS_SERIE_ANULACAO = Conforme campo Série da NF de anulação
		conhecimentoNovo.setDsSerieAnulacao(conhecimentoDadosTela.getDsSerieAnulacao());
		
//		•	DT_EMISSAO_NF_ANULACAO = Conforme campo Data de emissão da NF de anulação
		conhecimentoNovo.setDtEmissaoNfAnulacao(conhecimentoDadosTela.getDtEmissaoNfAnulacao());
		
//		•	VL_NF_ANULACAO = Conforme campo Valor da NF de anulação
		conhecimentoNovo.setVlNfAnulacao(conhecimentoDadosTela.getVlNfAnulacao());
		
		conhecimentoNovo.setNrCepColeta(conhecimentoOriginal.getNrCepColeta());
		
//		Aba Notas Fiscais:
//		•	PS_REAL = soma dos valores digitados na coluna Peso (kg)
		conhecimentoNovo.setPsReal(conhecimentoDadosTela.getPsReal());
		
//		•	PS_AFERIDO = NULL
		conhecimentoNovo.setPsAferido(conhecimentoOriginal.getPsAferido());
		
//		•	VL_MERCADORIA = soma dos valores digitados na coluna Valor
		conhecimentoNovo.setVlMercadoria(conhecimentoDadosTela.getVlMercadoria());
		
//		•	QT_VOLUMES = soma dos valores digitados na coluna Qtde de volumes
		conhecimentoNovo.setQtVolumes(conhecimentoOriginal.getQtVolumes());


// 		conhecimento

		
//		•	ID_CONHECIMENTO = Gerado
		
//		•	ID_NATUREZA_PRODUTO = conteúdo do registro do CTRC Original 
		conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());
		
//		•	ID_FILIAL_ORIGEM = Filial do usuário logado (FILIAL.ID_FILIAL)
		conhecimentoNovo.setFilialOrigem(filial);
		
//		•	ID_MUNICIPIO_COLETA = ID_MUNICIPIO_COLETA do registro do CTRC Original
		conhecimentoNovo.setMunicipioByIdMunicipioColeta(conhecimentoOriginal.getMunicipioByIdMunicipioColeta());
		
//		•	ID_DENSIDADE = conteúdo do registro do CTRC Original
		conhecimentoNovo.setDensidade(conhecimentoOriginal.getDensidade());
		
//		•	NR_CONHECIMENTO = <NR_DOCTO_SERVICO>
		
//		•	DS_ESPECIE_VOLUME = conteúdo do registro do CTRC Original //constante
		conhecimentoNovo.setDsEspecieVolume(conhecimentoOriginal.getDsEspecieVolume());
		
//		•	TP_FRETE= Conforme campo Tipo de frete do registro do CTRC Original
		conhecimentoNovo.setTpFrete(conhecimentoOriginal.getTpFrete());
		
//		•	BL_INDICADOR_EDI = “N’
		conhecimentoNovo.setBlIndicadorEdi(Boolean.FALSE);
		
//		•	TP_SITUACAO_CONHECIMENTO = “P”
		conhecimentoNovo.setTpSituacaoConhecimento(new DomainValue("P"));
		
//		•	BL_PERMITE_TRANSFERENCIA = “S”
		conhecimentoNovo.setBlPermiteTransferencia(Boolean.TRUE);
		
//		•	BL_REEMBOLSO = “N”
		conhecimentoNovo.setBlReembolso(Boolean.FALSE);
		
//		•	TP_DEVEDOR_FRETE = conforme TP_DEVEDOR_FRETE do CTRC Original
		conhecimentoNovo.setTpDevedorFrete(conhecimentoOriginal.getTpDevedorFrete());
		
//		•	BL_SEGURO_RR = “N” 
		conhecimentoNovo.setBlSeguroRr(Boolean.FALSE);
		
//		•	ID_FILIAL_DEPOSITO = NULL 
		conhecimentoNovo.setFilialByIdFilialDeposito(null);
		
//		•	ID_PRODUTO = conteúdo do registro do CTRC Original
		conhecimentoNovo.setProduto(conhecimentoOriginal.getProduto());
		
//		•	ID_MOTIVO_CANCELAMENTO = NULL
		conhecimentoNovo.setMotivoCancelamento(null);
		
//		•	DV_CONHECIMENTO = NULL
		conhecimentoNovo.setDvConhecimento(null);
		
//		•	NR_FORMULARIO = NULL
		conhecimentoNovo.setNrFormulario(null);
		
//		•	NR_SELO_FISCAL = NULL
		conhecimentoNovo.setNrSeloFiscal(null);
		
//		•	PS_AFORADO = conforme campo Peso Aforado (PS_AFORADO) gerado no processo de cálculo do frete
		conhecimentoNovo.setPsAforado(conhecimentoDadosTela.getPsAforado());
		
//		•	NR_DIAS_PREV_ENTREGA = NULL
		conhecimentoNovo.setNrDiasPrevEntrega(null);
		
//		•	NR_DIAS_BLOQUEIO = NULL
		conhecimentoNovo.setNrDiasBloqueio(null);
		
//		•	NR_ENTREGA = NULL
		conhecimentoNovo.setNrEntrega(null);
		
//		•	DT_PREV_ENTREGA = NULL
		conhecimentoNovo.setDtPrevEntrega(null);
		
//		•	DT_AUT_DEV_MERC = NULL
		conhecimentoNovo.setDtAutDevMerc(null);
		
//		•	DS_SERIE = NULL
		conhecimentoNovo.setDsSerie(null);
		
//		•	DS_SERIE_SELO_FISCAL = NULL
		conhecimentoNovo.setDsSerieSeloFiscal(null);
		
//		•	DS_CODIGO_COLETA = conteúdo do registro do CTRC Original
		conhecimentoNovo.setDsCodigoColeta(conhecimentoOriginal.getDsCodigoColeta());
		
//		•	DS_RESP_AUT_DEV_MERC = NULL
		conhecimentoNovo.setDsRespAutDevMerc(null);
		
//		•	DS_CTO_REDESPACHO = NULL
		conhecimentoNovo.setDsCtoRedespacho(null);
		
//		•	DS_CALCULADO_ATE = “O DESTINO”
		conhecimentoNovo.setDsCalculadoAte(ConstantesExpedicao.CALCULADO_ATE_DESTINO);
		
//		•	TP_DOCUMENTO_SERVICO = ‘CTE’
		conhecimentoNovo.setTpDoctoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
		
//		•	TP_CTRC_PARCERIA = null
		conhecimentoNovo.setTpCtrcParceria(null);
		
//		•	NR_CEP_ENTREGA = NR_CEP_ENTREGA do CTRC original
		conhecimentoNovo.setNrCepEntrega(conhecimentoOriginal.getNrCepEntrega());
		
//		•	NR_CTRC_SUBCONTRATANTE = NULL
		conhecimentoNovo.setNrCtrcSubcontratante(null);
		
//		•	DT_COLETA = conteúdo do registro do CTRC Original 
//		•	DT_COLETA_TZR = conteúdo do registro do CTRC Original 
		conhecimentoNovo.setDtColeta(conhecimentoOriginal.getDtColeta());
		
//		•	ID_MUNICIPIO_ENTREGA = ID_MUNICIPIO_ENTREGAdo registro do CTRC Original
		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimentoOriginal.getMunicipioByIdMunicipioEntrega());
		
//		•	BL_CALCULA_FRETE = conforme campo Calcula frete
		conhecimentoNovo.setBlCalculaFrete(conhecimentoDadosTela.getBlCalculaFrete());
		
//		•	BL_CALCULA_SERVICO = conforme campo Calcula serviço
		conhecimentoNovo.setBlCalculaServico(conhecimentoDadosTela.getBlCalculaServico());
		
//		•	ID_LOCALIZACAO_ORIGEM = conteúdo do campo ID_LOCALIZACAO_ORIGEM  do registro do CTRC Original
		conhecimentoNovo.setLocalizacaoOrigem(conhecimentoOriginal.getLocalizacaoOrigem());
		
//		•	ID_LOCALIZACAO_DESTINO = conteúdo do campo ID_LOCALIZACAO_DESTINO do registro do CTRC Original
		conhecimentoNovo.setLocalizacaoDestino(conhecimentoOriginal.getLocalizacaoDestino());
		
//		•	BL_PESO_AFERIDO = ‘N’
		conhecimentoNovo.setBlPesoAferido(Boolean.FALSE);
		
//		•	ID_TIPO_LOCALIZACAO_ENTREGA = conteúdo do campo ID_TIPO_LOCALIZACAO _ENTREGA do registro do CTRC Original
		//conhecimentoNovo.set não tem tabela
		
//		•	BL_PALETIZACAO = conteúdo do registro do CTRC Original
		conhecimentoNovo.setBlPaletizacao(conhecimentoOriginal.getBlPaletizacao());
		
//		•	BL_INDICADOR_FRETE_PERCENTUAL = Flag indicando Cálculo com Frete Percentual gerado no processo de cálculo do frete
		conhecimentoNovo.setBlIndicadorFretePercentual(conhecimentoDadosTela.getBlIndicadorFretePercentual());
		
//		•	ID_TIPO_TRIBUTACAO_ICMS = conforme campo Tipo de Tributação do ICMS gerado no processo de cálculo do frete
		conhecimentoNovo.setTipoTributacaoIcms(conhecimentoDadosTela.getTipoTributacaoIcms());
		
//		•	ID_AEROPORTO_ORIGEM = conteúdo do campo ID_AEROPORTO_ORIGEM do registro do CTRC Original
		conhecimentoNovo.setAeroportoByIdAeroportoOrigem(conhecimentoOriginal.getAeroportoByIdAeroportoOrigem());
		
//		•	ID_AEROPORTO_DESTINO = conteúdo do campo ID_AEROPORTO_DESTINO do registro do CTRC Original
		conhecimentoNovo.setAeroportoByIdAeroportoDestino(conhecimentoOriginal.getAeroportoByIdAeroportoDestino());
		
//		•	Tabela DEVEDOR_DOC_SERV (Se existir registro na tabela DEVEDOR_DOC_SERV para o CTRC original):
		ConhecimentoUtils.copyDevedoresDoctoServico(conhecimentoOriginal, conhecimentoNovo, conhecimentoOriginal.getInscricaoEstadualRemetente());
		
		if(conhecimentoDadosTela.getLiberacaoDocServs()!= null){
			conhecimentoNovo.setTpMotivoLiberacao(conhecimentoDadosTela.getTpMotivoLiberacao());
			ConhecimentoUtils.copyLiberacaoDocServs(conhecimentoDadosTela, conhecimentoNovo);
		}
		
		if(conhecimentoDadosTela.getObservacaoDoctoServicos() == null || conhecimentoDadosTela.getObservacaoDoctoServicos().isEmpty()){
			
//		•	Tabela OBSERVACAO_DOCTO_SERVICOS: criar um registro
			String dsObservacaoGeral = (String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVACAO_GERAL);
			conhecimentoNovo.addObservacaoDoctoServico(ConhecimentoUtils.createObservacaoDocumentoServico(conhecimentoNovo, dsObservacaoGeral, Boolean.TRUE));;
			
//		•	Tabela OBSERVACAO_DOCTO_SERVICOS: criar um registro  para motivo do Substituto
			conhecimentoNovo.addObservacaoDoctoServico(ConhecimentoUtils.createObservacaoDocumentoServico(
					conhecimentoNovo,
					configuracoesFacade.getMensagem("observacaoConhecimentoSubstituto", new Object[]{
							ConhecimentoUtils.formatConhecimento(conhecimentoOriginal.getFilialByIdFilialOrigem().getSgFilial(), conhecimentoOriginal.getNrConhecimento()),
							JTFormatUtils.format(conhecimentoOriginal.getDhEmissao(), "dd/MM/yyyy"),
							conhecimentoDadosTela.getDsMotivoAnulacao()}),
							Boolean.TRUE
					));

//		•	Verifica se o cliente responsável possui observação na tabela OBSERVACAO_CONHECIMENTO.
			Cliente clienteResponsavel = ((DevedorDocServ)conhecimentoOriginal.getDevedorDocServs().get(0)).getCliente();
			generateObsDoctoClienteByObsConhecimento(clienteResponsavel, conhecimentoNovo);
			
		}else{
			conhecimentoNovo.setObservacaoDoctoServicos(conhecimentoDadosTela.getObservacaoDoctoServicos());
		}
		
//		•	Tabela COTAÇÃO
		if(conhecimentoDadosTela.getCotacoes() != null) {
			conhecimentoNovo.setCotacoes(conhecimentoDadosTela.getCotacoes());
		}

//		•	Tabela NOTA_FISCAL_CONHECIMENTO 
		conhecimentoNovo.setNotaFiscalConhecimentos(conhecimentoDadosTela.getNotaFiscalConhecimentos());
		if(conhecimentoNovo.getNotaFiscalConhecimentos() != null){
			for(NotaFiscalConhecimento notaFiscalConhecimento : conhecimentoNovo.getNotaFiscalConhecimentos()){
				if(notaFiscalConhecimento.getPsCubado() == null){
					notaFiscalConhecimento.setPsCubado(notaFiscalConhecimento.getPsMercadoria());
				}
			}
		}
		
		validateDadosCliente(conhecimentoNovo);  
		
		return conhecimentoNovo;
	}
	
	
	private MonitoramentoDescarga createMonitoramentoDescarga(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		MonitoramentoDescarga monitoramentoDescarga = new MonitoramentoDescarga();
		monitoramentoDescarga.setFilial(SessionUtils.getFilialSessao());
		monitoramentoDescarga.setQtVolumesTotal(Long.valueOf(conhecimentoOriginal.getQtVolumes().toString()));
		monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA));
		monitoramentoDescarga.setNrFrota(ConstantesExpedicao.NR_FROTA_SUBSTITUTO);
		monitoramentoDescarga.setNrPlaca(ConstantesExpedicao.NR_FROTA_SUBSTITUTO);
		monitoramentoDescarga.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		if(conhecimentoOriginal.getPedidoColeta() != null){
			monitoramentoDescarga.setDsInfColeta(conhecimentoOriginal.getPedidoColeta().getDsInfColeta());
		}
		return (MonitoramentoDescarga) monitoramentoDescargaService.store(monitoramentoDescarga);
	}
	
	
	public Map validateDhEmissaoNFAnulacao(Map<String, Object> criteria) {
		Map map = new HashMap();
		YearMonthDay dhEmissao = new YearMonthDay(criteria.get("dhEmissao"));
		String nrNotaFiscalAnulacao = (String)criteria.get("nrNotaFiscal");
		
		if(dhEmissao.isAfter(new YearMonthDay())){
			throw new BusinessException("LMS-04019",new Object[] {nrNotaFiscalAnulacao});
		}
		
		final BigDecimal validade = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.DIAS_VALIDADE_NF);
		
		int nrDias = 0;
		if(validade != null) {
			nrDias = -(validade.intValue());
		}
		final YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		final YearMonthDay dtValidade = dtAtual.plusDays(nrDias);
		final YearMonthDay data = dhEmissao;
		
		boolean isNotaFiscalAntiga = false;
		if(data != null) {
			if(JTDateTimeUtils.comparaData(data, dtValidade) < 0) {
				isNotaFiscalAntiga = true;				
			}
		}
		
		map.put("notaFiscalAntigaMsg", isNotaFiscalAntiga);
		
		return map;
	}
	
	/**
	 * Valida Notas Fiscais Conhecimento
	 * @param criteria
	 * @return Warnings
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> validateNotasConhecimento(Map<String, Object> criteria) {
		List<Map<String, Object>> notasFiscais = (List<Map<String, Object>>) criteria.get("notasFiscais");
		List<Integer> nrNotasFiscais = new ArrayList<Integer>(notasFiscais.size());
		List<String> dsSeriesNotasFiscais = new ArrayList<String>(notasFiscais.size());
		List<YearMonthDay> datas = new ArrayList<YearMonthDay>(notasFiscais.size());
		Long idConhecimentoOriginal = (Long)criteria.get("idConhecimentoOriginal");

		WarningCollectorUtils.remove();
		
		for (Map<String, Object> notaFiscal : notasFiscais) {
			nrNotasFiscais.add((Integer) notaFiscal.get("nrNotaFiscal"));
			datas.add((YearMonthDay) notaFiscal.get("dtEmissao"));
			dsSeriesNotasFiscais.add((String)notaFiscal.get("dsSerie"));
		}
		
		Long idClienteRemetente = (Long) criteria.get("idClienteRemetente");

		validaNotasConhecimentoSubstituto(nrNotasFiscais, dsSeriesNotasFiscais, idConhecimentoOriginal, idClienteRemetente);
		
		Map<String, Object> result = new HashMap<String, Object>();
		WarningCollectorUtils.putAll(result);
		
		return result;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void validaNotasConhecimentoSubstituto(List<Integer> nrNotasFiscais,
			List<String> dsSeriesNotasFiscais, Long idConhecimentoOriginal,
			Long idClienteRemetente) {
		List result = notaFiscalConhecimentoService.findListByRemetenteNrNotasDsSerieConhecimentoOriginal(idClienteRemetente, nrNotasFiscais, dsSeriesNotasFiscais, idConhecimentoOriginal);
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

	public Map<String, Object> executePrimeiraFase(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Conhecimento conhecimentoOriginal = conhecimentoService.findByIdInitLazyProperties((Long) params.get("idDoctoServico"), false);
		
		Conhecimento conhecimentoDadosTela = (Conhecimento) params.get("conhecimento");

		String nrChave = (String) params.get("txtNrChaveAnulacao");
		if (StringUtils.isNotBlank(nrChave) && nrChave.length() < 7) {
			conhecimentoDadosTela.setNrNfAnulacao(new BigDecimal ((String) params.get("txtNrChaveAnulacao")) );
			conhecimentoDadosTela.setNrChaveNfAnulacao(null);
		} else if(StringUtils.isNotBlank(nrChave)) { 
			conhecimentoDadosTela.setNrChaveNfAnulacao(nrChave);
			conhecimentoDadosTela.setNrNfAnulacao(null);
		}
		conhecimentoDadosTela.setVlNfAnulacao((BigDecimal) params.get("txtVlNFAnulacao"));
		conhecimentoDadosTela.setDtEmissaoNfAnulacao((YearMonthDay) params.get("txtDhEmissaoNFAnulacao"));
		conhecimentoDadosTela.setDsSerieAnulacao((String) params.get("txtNrSeriaNFAnulacao"));
		conhecimentoDadosTela.setTpCalculoPreco(new DomainValue((String) params.get("tipoCalculo")));
		conhecimentoDadosTela.setDsMotivoAnulacao((String) params.get("motivo"));
		
		conhecimentoDadosTela.setFilialByIdFilialDestino(conhecimentoOriginal.getFilialByIdFilialDestino());		
		
		conhecimentoDadosTela.setDoctoServicoOriginal(conhecimentoOriginal);
		conhecimentoDadosTela.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO));
		conhecimentoDadosTela.setDensidade(conhecimentoOriginal.getDensidade());
		
		CalculoFrete calculoFrete = ExpedicaoUtils.newCalculoFrete(new DomainValue(ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
		calculoFrete.setIdServico(conhecimentoOriginal.getServico().getIdServico());
		calculoFrete.setBlCalculoFreteTabelaCheia(Boolean.FALSE);
		calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
		calculoFrete.setBlCalculaServicosAdicionais(Boolean.FALSE);
		result.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoFrete);
		
		if (params.get("idCotacao") != null) {
			Cotacao cotaco = cotacaoService.findById((Long) params.get("idCotacao"));
			List<Cotacao> cotacoes = new ArrayList<Cotacao>();
			cotacoes.add(cotaco);
			conhecimentoDadosTela.setCotacoes(cotacoes);
			calculoFrete.setIdCotacao((Long) params.get("idCotacao"));
		}
		
		Conhecimento conhecimentoNovo = generateConhecimentoSubstitutoByConhecimentoOriginalAndDadosTela(conhecimentoOriginal, conhecimentoDadosTela);
		
		Frete frete = new Frete(); 
		frete.setConhecimento(conhecimentoNovo);
		frete.setCalculoFrete(calculoFrete);
		
		
		calcularFreteService.validaFreteNormal(frete);
		
		result.put("conhecimento", conhecimentoNovo);
		result.put("idDivisaoCliente", params.get("idDivisaoCliente"));
		
		return result;
	}

	//LMS-6135
	private void setDivisaoClienteDadosTela(Map<String, Object> params, Conhecimento conhecimento) {
		Long idDivisaoCliente = MapUtils.getLong(params, "idDivisaoCliente");
		if (LongUtils.hasValue(idDivisaoCliente)) {
			DivisaoCliente divisaoClienteDadosTela = divisaoClienteService.findById(idDivisaoCliente);
			conhecimento.setDivisaoCliente(divisaoClienteDadosTela);
		} else {
			conhecimento.setDivisaoCliente(null);
		}
	}
	
	public Map<String, Object> executeSegundaFase(Map<String, Object> parameters) {

		Map<String, Object> result = new HashMap<String, Object>();

		Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		Conhecimento conhecimentoOriginal = null;
		
		if (conhecimento != null) {
			setDivisaoClienteDadosTela(parameters, conhecimento);
		}
		
		if(conhecimento != null && conhecimento.getDoctoServicoOriginal() != null){
			conhecimentoOriginal = conhecimentoService.findById(conhecimento.getDoctoServicoOriginal().getIdDoctoServico());
		}
		
		if ( conhecimentoOriginal != null ) {
			
			/*lms-6135
			if(conhecimentoOriginal.getDivisaoCliente() != null && conhecimentoOriginal.getDivisaoCliente().getIdDivisaoCliente() != null) {
				conhecimento.setDivisaoCliente(conhecimentoOriginal.getDivisaoCliente());
				((CalculoFrete)parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION)).setIdDivisaoCliente(conhecimentoOriginal.getDivisaoCliente().getIdDivisaoCliente());
			}*/		
			
			if(conhecimentoOriginal.getDimensoes() != null && !conhecimentoOriginal.getDimensoes().isEmpty()){
				conhecimento.setDimensoes(conhecimentoOriginal.getDimensoes());
			}
			
		}
				
		
		Frete frete = new Frete(); 
		frete.setConhecimento(conhecimento);
		frete.setCalculoFrete((CalculoFrete)parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION));
		calcularFreteService.calcularPesoAforado(conhecimento);
		
		//LMS-2353
		conhecimento.setPsCubadoDeclarado(conhecimento.getPsAforado());
		conhecimento.setPsCubadoAferido(null);
		
		
		conhecimento.getDadosCliente().setBlCotacaoRemetente((Boolean) parameters.get("blCotacaoRemetente"));
		
		result.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION));
		result.put("conhecimento", conhecimento);
		result.put("idFilialDestino", conhecimento.getFilialByIdFilialDestino().getIdFilial());
		result.put("sgFilialDestino", conhecimento.getFilialByIdFilialDestino().getSgFilial());
		
		WarningCollectorUtils.putAll(result);
		
		return result;
	}
	
	private void executeVolumeNotaFiscal(final Conhecimento conhecimentoNovo, final MonitoramentoDescarga monitoramentoDescarga) {
		NotaFiscalConhecimento nfc = conhecimentoNovo.getNotaFiscalConhecimentos().get(0);

		VolumeNotaFiscal vnf = new VolumeNotaFiscal();
		vnf.setNotaFiscalConhecimento(nfc);
		vnf.setNrSequencia(1);
		vnf.setNrVolumeColeta("0");
		vnf.setMonitoramentoDescarga(monitoramentoDescarga);
		vnf.setQtVolumes(1);
		vnf.setTpVolume("U");
		vnf.setPsAferido(BigDecimal.ZERO);

		List<VolumeNotaFiscal> list = new ArrayList<VolumeNotaFiscal>();
		list.add(vnf);
		nfc.setVolumeNotaFiscais(list);
		
	}
	
	@Override
	protected void executeCalculo(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		validateDadosCliente(conhecimento, calculoFrete);

		documentoServicoFacade.executeCalculoConhecimentoNacionalDevolucao(calculoFrete);
		CalculoFreteUtils.copyResult(conhecimento, calculoFrete);
	}
	
	public Map findDivisaoClienteByConhecimento(Map parameters) {
		List list = findDivisaoCliente(parameters);
		if (CollectionUtils.isEmpty(list)) { 
			return parameters;
		} 
		parameters.put("listDivisao", list);
		
		Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		
		Long idClienteBaseCalculo = conhecimento.getClienteByIdClienteBaseCalculo().getIdCliente();
		Long idUnidadeFederativaDestinatario = conhecimento.getClienteByIdClienteDestinatario()
														.getPessoa()
														.getEnderecoPessoa()
														.getMunicipio()
														.getUnidadeFederativa()
														.getIdUnidadeFederativa();
		Long idNaturezaProduto = conhecimento.getNaturezaProduto().getIdNaturezaProduto();
		
		parameters.put("idUnidadeFederativaDestinatario", idUnidadeFederativaDestinatario);
		parameters.put("idClienteBaseCalculo", idClienteBaseCalculo);
		parameters.put("idNaturezaProduto", idNaturezaProduto);
		
		if (list.size() > 1) {
			list = normalizarDivisaoCliente(parameters, list);
		}
		if (list.size() == 1) {
			Map map = (Map) list.get(0);
			parameters.put("idDivisaoCliente", (Long) map.get("idDivisaoCliente"));
		}
		return parameters;
	}

	private List findDivisaoCliente(Map parameters) {
		CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		Cliente cliente = calculoFrete.getClienteBase();
		List<Map> result = new ArrayList<Map>();
		result = getResultDivisoes(calculoFrete, cliente, result);
		return result;
	}

	private List<Map> getResultDivisoes(CalculoFrete calculoFrete, Cliente cliente, List<Map> result) {
		if(ClienteUtils.isParametroClienteEspecial(cliente.getTpCliente().getValue())) {
			List divisoes = divisaoClienteService.findDivisaoClienteByIdServico(cliente.getIdCliente(), calculoFrete.getIdServico(), ATIVO);
			if (divisoes != null && !divisoes.isEmpty()) {
				for (int i = 0; i < divisoes.size(); i++) {
					DivisaoCliente divisaoCliente = (DivisaoCliente) divisoes.get(i);
					Map<String, Object> dc = new HashMap<String, Object>();
					dc.put("idDivisaoCliente", divisaoCliente.getIdDivisaoCliente());
					dc.put("dsDivisaoCliente", divisaoCliente.getDsDivisaoCliente());
					checkNatureza(divisaoCliente, dc);
					result.add(dc);
				}
			}
		}
		return result;
	}


	private void checkNatureza(DivisaoCliente divisaoCliente, Map<String, Object> dc) {
		if( divisaoCliente.getNaturezaProduto() != null){
			dc.put("idNaturezaProduto", divisaoCliente.getNaturezaProduto().getIdNaturezaProduto());
		}
	}
	
	private List normalizarDivisaoCliente(Map parameters, List list) {
		// Verifica se o cliente para base cálculo possui divisão Rotas Verdes
		DivisaoCliente divisaoRotasVerdes = divisaoClienteService.findDivisaoClienteByClienteAndSituacao(
				(Long) parameters.get("idClienteBaseCalculo"), ROTAS_VERDES, ATIVO);
		if (divisaoRotasVerdes != null) {
			Map<String, Object> dc = new HashMap<String, Object>();
			dc.put("idDivisaoCliente", divisaoRotasVerdes.getIdDivisaoCliente());
			dc.put("dsDivisaoCliente", divisaoRotasVerdes.getDsDivisaoCliente());
			list = Arrays.asList(dc);
			// Busca o a filial do cliente destinatario e o cliente base
			// calculo e verifica se é centro-oeste ou norte
		} else {
			Long idUnidadeFederativa = (Long) parameters.get("idUnidadeFederativaDestinatario");
			if (filialService.validateFilialCentroOesteNorteByNrIdentificacaoCliente(idUnidadeFederativa)) {
				list = clienteBaseCalculoPossuiDivisaoCentroOesteNorte(list);
				if (list.size() > 1) {
					list = verifyDivisaoComMesmaNatureza(parameters, list);
				}
			}
		}
		parameters.put("listDivisao", list);
		return list;
	}

	private List verifyDivisaoComMesmaNatureza(Map parameters, List list) {
		if (parameters.containsKey("idNaturezaProduto")) {
			List divisoesComNaturezaIgual = filtraDivisaoPorNatureza(
					MapUtilsPlus.getLong(parameters, "idNaturezaProduto", null),
					MapUtilsPlus.getList(parameters, "listDivisao", null));
			if (CollectionUtils.isNotEmpty(divisoesComNaturezaIgual)) {
				list = divisoesComNaturezaIgual;
			}
		}
		return list;
	}

	// Verifica se o cliente possui divisao "Centro-Oeste e Norte"
	private List clienteBaseCalculoPossuiDivisaoCentroOesteNorte(List list) {
		for (Object obj : list) {
			Map<String, Object> mapFor = (HashMap<String, Object>) obj;
			String nmDivisao = MapUtils.getString(mapFor, "dsDivisaoCliente");
			if (nmDivisao.equals(ConstantesExpedicao.DIVISAO_CENTRO_OESTE_E_NORTE)) {
				List returnList = new ArrayList();
				returnList.add(mapFor);
				list = returnList;
			}
		}
		return list;
	}

	private List<Map<String, Object>> filtraDivisaoPorNatureza(Long idNaturezaProduto, List divisoes) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (idNaturezaProduto != null && divisoes != null) {
			for (Object obj : divisoes) {
				Map<String, Object> map = (Map) obj;
				if (map.containsKey("idNaturezaProduto")
						&& MapUtilsPlus.getLong(map, "idNaturezaProduto", null).equals(idNaturezaProduto)) {
					resultList.add(map);
				}
			}
			return resultList;
		}
		return divisoes;
	}

	public Map<String, Object> validaParametroGeralFilial() {

		Map<String, Object> resultado = new HashMap<>();

		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		String conteudoParametroFilial = String.valueOf(conteudoParametroFilialService.findConteudoByNomeParametroWithException(
				filialUsuarioLogado.getIdFilial(), "Versão_XML_CTe", false));
		boolean obrigatorio = conteudoParametroFilial != null && conteudoParametroFilial.equals(EVersaoXMLCTE.VERSAO_300a.getConteudoParametro());

		resultado.put("blNrChaveNFAnulacaoRequired", obrigatorio);

		return  resultado;
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public DigitarNotaService getDigitarNotaService() {
		return digitarNotaService;
	}

	public void setDigitarNotaService(DigitarNotaService digitarNotaService) {
		this.digitarNotaService = digitarNotaService;
	}

	public ConhecimentoNormalService getConhecimentoNormalService() {
		return conhecimentoNormalService;
	}


	public void setConhecimentoNormalService(
			ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}


	public CalcularFreteService getCalcularFreteService() {
		return calcularFreteService;
	}


	public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}


	public void setDigitarDadosNotaNormalCalculoCTRCService(
			DigitarDadosNotaNormalCalculoCTRCService digitarDadosNotaNormalCalculoCTRCService) {
		this.digitarDadosNotaNormalCalculoCTRCService = digitarDadosNotaNormalCalculoCTRCService;
	}


	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setParametroFilialService(ParametroFilialService parametroFilialService) {
		this.parametroFilialService = parametroFilialService;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

}
