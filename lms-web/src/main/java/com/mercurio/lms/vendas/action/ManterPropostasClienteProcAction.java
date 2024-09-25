package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.util.TabelaPrecoUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.ProdutoCliente;
import com.mercurio.lms.vendas.model.PromotorCliente;
import com.mercurio.lms.vendas.model.Proposta;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.SimulacaoAnexo;
import com.mercurio.lms.vendas.model.service.HistoricoEfetivacaoService;
import com.mercurio.lms.vendas.model.service.ParametroClienteService;
import com.mercurio.lms.vendas.model.service.ProdutoClienteService;
import com.mercurio.lms.vendas.model.service.PromotorClienteService;
import com.mercurio.lms.vendas.model.service.PropostaService;
import com.mercurio.lms.vendas.model.service.SimulacaoAnexoService;
import com.mercurio.lms.vendas.model.service.SimulacaoService;
import com.mercurio.lms.vendas.util.SimulacaoUtils;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterPropostasClienteProcAction"
 */
public class ManterPropostasClienteProcAction extends CrudAction {
	private ManterPropostasClienteFormalidadesAction manterPropostasClienteFormalidadesAction;
	private FilialService filialService;
	private ConfiguracoesFacade configuracoesFacade;
	private ServicoService servicoService;
	private PropostaService propostaService;
	private ParametroClienteService parametroClienteService;
	private PromotorClienteService promotorClienteService;
	private ProdutoClienteService produtoClienteService;
	private DomainValueService domainValueService;

	// LMS-6172
	private SimulacaoAnexoService simulacaoAnexoService;

	// LMS-6191
	private HistoricoEfetivacaoService historicoEfetivacaoService;

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		if (!validateParameters(criteria)) {
			throw new BusinessException("LMS-00055");
		}
		criteria.put("blNovaUI", Boolean.FALSE);
		ResultSetPage rsp = getSimulacaoService().findPaginatedProc(criteria);
		List<TypedFlatMap> lista = rsp.getList();
		if (lista != null && !lista.isEmpty()) {
			for (int i = 0; i < lista.size(); i++) {
				TypedFlatMap simulacao = lista.get(i);
				Integer nrVersao = simulacao.getInteger("tipoTabelaPreco.nrVersao");
				String tpTipoTabelaPreco = simulacao.getString("tipoTabelaPreco.tpTipoTabelaPreco.value");
				String tpSubtipoTabelaPreco = simulacao.getString("subtipoTabelaPreco.tpSubtipoTabelaPreco");
				if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
					String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
					simulacao.put("tabelaPrecoString", tabelaPrecoString);
				}
				
				Integer nrVersaoFob = simulacao.getInteger("tabelaPrecoFob.tipoTabelaPreco.nrVersao");
				String tpTipoTabelaPrecoFob = simulacao.getString("tabelaPrecoFob.tipoTabelaPreco.tpTipoTabelaPreco.value");
				String tpSubtipoTabelaPrecoFob = simulacao.getString("tabelaPrecoFob.subtipoTabelaPreco.tpSubtipoTabelaPreco");
				if (StringUtils.isNotBlank(tpTipoTabelaPrecoFob)) {
					String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPrecoFob, nrVersaoFob, tpSubtipoTabelaPrecoFob);
					simulacao.put("tabelaPrecoFob.tabelaPrecoString", tabelaPrecoString);
				}

				String sgFilial = simulacao.getString("filial.sgFilial");
				String nrSimulacao = ""+simulacao.getLong("nrSimulacao");
				simulacao.put("nrProposta", SimulacaoUtils.formatNrSimulacao(sgFilial, nrSimulacao));
			}
		}
		return rsp;
	}

	/**
	 * Obtem os dados da proposta para montar a aba de resumo , foram reaproveitados
	 * rotinas já existente
	 *  
	 * @return
	 */
	public TypedFlatMap findResumoProposta(){

		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();

		TypedFlatMap resumo = new TypedFlatMap();

		/*Id Simulação*/
		resumo.put("simulacao.idSimulacao", simulacao.getIdSimulacao());

		/*Tela de detalhe*/
		TypedFlatMap detalhe = findById(simulacao.getIdSimulacao());
		resumo.putAll(detalhe);

		/*Tela de Formalidade*/
		TypedFlatMap formalidades = manterPropostasClienteFormalidadesAction.findDadosSessao();
		resumo.putAll(formalidades);
		
		/*Resumo Cliente*/
		if(simulacao.getClienteByIdCliente() != null){
			
			/*Segmento de mercado*/
			resumo.put("segmentoMercado.idSegmentoMercado", simulacao.getClienteByIdCliente().getSegmentoMercado().getIdSegmentoMercado());
			
			/*Faturamento previsto*/
			resumo.put("vlFaturamentoPrevisto", simulacao.getClienteByIdCliente().getVlFaturamentoPrevisto());
			
			if(simulacao.getClienteByIdCliente().getMoedaByIdMoedaFatPrev() != null){
				resumo.put("moedaByIdMoedaFatPrev.idMoeda", simulacao.getClienteByIdCliente().getMoedaByIdMoedaFatPrev().getIdMoeda());
			}
			
			/*Vendedor*/			
			List<PromotorCliente> listPc = promotorClienteService.findPromotorClienteByIdCliente(simulacao.getClienteByIdCliente().getIdCliente());
			if(listPc != null && !listPc.isEmpty()){
				PromotorCliente pc = listPc.get(0);
				resumo.put("usuarioByIdUsuario.nmUsuario", pc.getUsuario().getNmUsuario());
				resumo.put("usuarioByIdUsuario.nrMatricula", pc.getUsuario().getNrMatricula());				
			}
			
			/*Valor médio por Kg*/
			List<ProdutoCliente> produtosCliente = produtoClienteService.findByIdCliente(simulacao.getClienteByIdCliente().getIdCliente());			
			if(produtosCliente != null && !produtosCliente.isEmpty()){
				ProdutoCliente produtoCliente = produtosCliente.get(0);
				resumo.put("moeda.idMoeda", produtoCliente.getMoeda().getIdMoeda() );
				resumo.put("vlMedioProdutoKilo", produtoCliente.getVlMedioProdutoKilo());
			}
		}
				
		/*Resumo , Frete Peso , Frete Valor e Especificações*/
		Proposta proposta = propostaService.findByIdSimulacao(simulacao.getIdSimulacao());
		if(proposta != null){
			resumo.put("tpIndicadorMinFretePeso", proposta.getTpIndicadorMinFretePeso().getValue());
			resumo.put("vlMinFretePeso", proposta.getVlMinFretePeso());						
			resumo.put("tpIndicadorFreteMinimo", proposta.getTpIndicadorFreteMinimo().getValue());
			resumo.put("vlFreteMinimo", proposta.getVlFreteMinimo());						
			resumo.put("tpIndicadorFretePeso", proposta.getTpIndicadorFretePeso().getValue());
			resumo.put("vlFretePeso", proposta.getVlFretePeso());												
			resumo.put("blPagaPesoExcedente", proposta.getBlPagaPesoExcedente());
			resumo.put("pcDiferencaFretePeso", proposta.getPcDiferencaFretePeso());
			resumo.put("tpIndicadorAdvalorem", proposta.getTpIndicadorAdvalorem().getValue());
			resumo.put("vlAdvalorem", proposta.getVlAdvalorem());
			resumo.put("pcDiferencaAdvalorem", proposta.getPcDiferencaAdvalorem());
			resumo.put("blFreteRecebido", proposta.getBlFreteRecebido());
			resumo.put("blFreteExpedido", proposta.getBlFreteExpedido());
			resumo.put("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", proposta.getUnidadeFederativaByIdUfOrigem().getIdUnidadeFederativa());		
			resumo.put("pcDiferencaFretePesoParametro", proposta.getPcDiferencaFretePeso());				
			resumo.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", proposta.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem().getIdTipoLocalizacaoMunicipio());			
		}
		
		resumo.put("pendencia.idPendencia", "");
		if (simulacao.getPendenciaAprovacao() != null) {
			Long idPendencia = simulacao.getPendenciaAprovacao().getIdPendencia();
			resumo.put("pendencia.idPendencia", idPendencia);
		}

		if (simulacao.getTpSituacaoAprovacao() != null) {
			resumo.put("tpSituacaoAprovacao.description", simulacao.getTpSituacaoAprovacao().getDescriptionAsString());
			resumo.put("tpSituacaoAprovacao.value", simulacao.getTpSituacaoAprovacao().getValue());
		}

		if (simulacao.getUsuarioByIdUsuarioAprovou() != null) {
			resumo.put("usuarioByIdUsuarioAprovou.nrMatricula", simulacao.getUsuarioByIdUsuarioAprovou().getNrMatricula());
			resumo.put("usuarioByIdUsuarioAprovou.nmUsuario", simulacao.getUsuarioByIdUsuarioAprovou().getNmUsuario());
		}

		/*Se a proposta for Tarifada , não mostra os parametros do cliente*/
		DomainValue tpGeracaoProposta = simulacao.getTpGeracaoProposta();
		if(tpGeracaoProposta == null || "T".equals(tpGeracaoProposta.getValue())){
			return resumo;
		}

		/*Tela parametros da proposta*/
		List<ParametroCliente> lista = parametroClienteService.findParametroByIdSimulacao(simulacao.getIdSimulacao());
		if(lista != null && !lista.isEmpty()){
			ParametroCliente pc = lista.get(0);
			
			resumo.put("idParametroCliente", pc.getIdParametroCliente());
			
			resumo.put("tpIndicadorPercentualGris", pc.getTpIndicadorPercentualGris().getValue());
			resumo.put("vlPercentualGris", pc.getVlPercentualGris());			
			resumo.put("tpIndicadorMinimoGris", pc.getTpIndicadorMinimoGris().getValue());
			resumo.put("vlMinimoGris", pc.getVlMinimoGris());			
			resumo.put("tpIndicadorPedagio", pc.getTpIndicadorPedagio().getValue());
			resumo.put("vlPedagio", pc.getVlPedagio());
			resumo.put("tpIndicadorPercentualTrt", pc.getTpIndicadorPercentualTrt().getValue());
			resumo.put("vlPercentualTrt", pc.getVlPercentualTrt());
			resumo.put("tpIndicadorMinimoTrt", pc.getTpIndicadorMinimoTrt().getValue());
			resumo.put("vlMinimoTrt", pc.getVlMinimoTrt());
			resumo.put("tpIndicadorPercentualTde", pc.getTpIndicadorPercentualTde().getValue());
			resumo.put("vlPercentualTde", pc.getVlPercentualTde());
			resumo.put("tpIndicadorMinimoTde", pc.getTpIndicadorMinimoTde().getValue());
			resumo.put("vlMinimoTde", pc.getVlMinimoTde());				
		}	
							
		return resumo;
	}
	
	public TypedFlatMap findById(Long idSimulacao) {
		TypedFlatMap simulacao = getSimulacaoService().findDadosById(idSimulacao);
		simulacao.put("idSimulacao", idSimulacao);
		simulacao.put("simulacao.idSimulacao", idSimulacao);

		/** Tabela de Preço */
		String tpTipoTabelaPreco = simulacao.getString("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco.value");
		String tpSubtipoTabelaPreco = simulacao.getString("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco");
		Integer nrVersao = simulacao.getInteger("tabelaPreco.tipoTabelaPreco.nrVersao");
		if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
			String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
			simulacao.put("tabelaPreco.tabelaPrecoString", tabelaPrecoString);
		}
		
		/** Tabela de Preço FOB */
		String tpTipoTabelaPrecoFob = simulacao.getString("tabelaPrecoFob.tipoTabelaPreco.tpTipoTabelaPreco.value");
		String tpSubtipoTabelaPrecoFob = simulacao.getString("tabelaPrecoFob.subtipoTabelaPreco.tpSubtipoTabelaPreco");
		Integer nrVersaoFob = simulacao.getInteger("tabelaPrecoFob.tipoTabelaPreco.nrVersao");
		if (StringUtils.isNotBlank(tpTipoTabelaPrecoFob)) {
			String tabelaPrecoStringFob = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPrecoFob, nrVersaoFob, tpSubtipoTabelaPrecoFob);
			simulacao.put("tabelaPrecoFob.tabelaPrecoString", tabelaPrecoStringFob);
		}

		/** Moeda */
		String dsMoeda = simulacao.getString("tabelaPreco.moeda.sgMoeda") + " " + simulacao.getString("tabelaPreco.moeda.dsSimbolo"); 
		simulacao.put("tabelaPreco.moeda.dsMoeda", dsMoeda);
		simulacao.put("tabelaPreco.moeda.siglaSimbolo", dsMoeda);

		/** Regra para desabilitar campos das telas*/
		String disableAll = "false";
		Boolean blEfetivada = simulacao.getBoolean("blEfetivada");
		if (!Boolean.TRUE.equals(blEfetivada)) {
			simulacao.put("blEfetivada", "N");
		} else {
			disableAll = "true";
			simulacao.put("blEfetivada", "S");
		}
		Long idFilial = simulacao.getLong("filial.idFilial");
		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		if (!idFilialSessao.equals(idFilial)) {
			disableAll = "true";
		}
		simulacao.put("disableAll", disableAll);

		String sgFilial = simulacao.getString("filial.sgFilial");
		String nrSimulacao = simulacao.getLong("nrSimulacao").toString();
		simulacao.put("nrProposta", SimulacaoUtils.formatNrSimulacao(sgFilial, nrSimulacao));

		String nrIdentificacao = simulacao.getString("cliente.pessoa.nrIdentificacao");
		String tpIdentificacao = simulacao.getString("cliente.pessoa.tpIdentificacao.value");
		simulacao.put("cliente.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));

		String nrMatricula = simulacao.getString("nrMatriculaPromotor");
		String nmUsuario = simulacao.getString("nmPromotor");
		simulacao.put("usuario.nrMatricula", nrMatricula);
		simulacao.put("usuario.nmUsuario", nmUsuario);
		
		// LMS-6172 - inclui filial do usuário logado no mapa
		simulacao.put("usuario.idFilial", SessionUtils.getFilialSessao().getIdFilial());
		simulacao.put("tpSituacaoAprovacao.value", simulacao.getString("simulacao.tpSituacaoAprovacao.value"));
		
		setSimulacaoInSession(idSimulacao);
		return simulacao;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		if (validateParameters(criteria)) {
			return getSimulacaoService().getRowCountProc(criteria);
		}
		return IntegerUtils.ZERO;
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getSimulacaoService().removeByIds(ids);
	}

	public void removeById(Long id) {
		getSimulacaoService().removeById(id);
	}

	public Serializable store(TypedFlatMap data) {
		boolean isSimulacao = data.getLong("simulacao.idSimulacao") != null;

		Simulacao simulacao = null;
		if (isSimulacao) {
			simulacao = SimulacaoUtils.getSimulacaoInSession();
			getSimulacaoService().validaUpdateSimulacao(simulacao);
		} else {
			simulacao = new Simulacao();
			simulacao.setBlNovaUI(Boolean.FALSE);
		}

		Cliente cliente = new Cliente();
		cliente.setIdCliente(data.getLong("cliente.idCliente"));
		simulacao.setClienteByIdCliente(cliente);

		Long idTabelaPrecoOld = null;
		Long idTabelaPrecoNew = null;
		Long idTabelaPreco = data.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			if (isSimulacao && simulacao.getTabelaPreco() != null) {
				idTabelaPrecoOld = simulacao.getTabelaPreco().getIdTabelaPreco();
				if (!idTabelaPrecoOld.equals(idTabelaPreco)) {
					// tem alteracao nas tabelaPrecoParcela
					idTabelaPrecoNew = idTabelaPreco;
				}
			}

			TabelaPreco tabelaPreco = new TabelaPreco();
			tabelaPreco.setIdTabelaPreco(idTabelaPreco);
			simulacao.setTabelaPreco(tabelaPreco);
		} else {
			simulacao.setTabelaPreco(null);
		}

		Long idDivisaoCliente = data.getLong("divisaoCliente.idDivisaoCliente");
		if(idDivisaoCliente != null) {
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente(idDivisaoCliente);
			simulacao.setDivisaoCliente(divisaoCliente);
		} else {
			simulacao.setDivisaoCliente(null);
		}
		
		Long idTabelaPrecoFob = data.getLong("tabelaPrecoFob.idTabelaPreco");
		if (idTabelaPrecoFob != null) {
			TabelaPreco tabelaPrecoFob = new TabelaPreco();
			tabelaPrecoFob.setIdTabelaPreco(idTabelaPrecoFob);
			simulacao.setTabelaPrecoFob(tabelaPrecoFob);
		} else {
			simulacao.setTabelaPrecoFob(null);
		}

		Servico servico = new Servico();
		servico.setIdServico(data.getLong("servico.idServico"));
		simulacao.setServico(servico);

		if (!isSimulacao) {
			simulacao.setTpSimulacao(new DomainValue("S"));
			simulacao.setTpRegistro(new DomainValue("P"));
			simulacao.setTpFormaInsercao(new DomainValue("D"));
			simulacao.setDtSimulacao(JTDateTimeUtils.getDataAtual());
			simulacao.setTpIntegranteFrete(new DomainValue("R"));
			simulacao.setTpSituacaoAprovacao(new DomainValue("I"));
			simulacao.setBlCalculoPesoCubado(Boolean.FALSE);

		} else {
			String saveMode = data.getString("save.mode");
			if("complete".equals(saveMode)) {
				String tpPeriodicidadeFaturamento = data.getString("tpPeriodicidadeFaturamento");
				if (StringUtils.isNotBlank(tpPeriodicidadeFaturamento)) {
					simulacao.setTpPeriodicidadeFaturamento(new DomainValue(tpPeriodicidadeFaturamento));
				}
				simulacao.setNrDiasPrazoPagamento(data.getShort("nrDiasPrazoPagamento"));
				simulacao.setDtValidadeProposta(data.getYearMonthDay("dtValidadeProposta"));
				simulacao.setDtTabelaVigenciaInicial(data.getYearMonthDay("dtVigenciaInicial"));
				simulacao.setDtAceiteCliente(data.getYearMonthDay("dtAceiteCliente"));
				simulacao.setDtAprovacao(data.getYearMonthDay("dtAprovacao"));
				simulacao.setObProposta(data.getString("obProposta"));
			}
		}

		simulacao.setUsuarioByIdUsuario(SessionUtils.getUsuarioLogado());
		simulacao.setFilial(SessionUtils.getFilialSessao());
		simulacao.setBlEfetivada(Boolean.FALSE);
		simulacao.setBlPagaFreteTonelada(data.getBoolean("blPagaFreteTonelada"));
		simulacao.setBlEmiteCargaCompleta(data.getBoolean("blEmiteCargaCompleta"));
		simulacao.setTpGeracaoProposta(data.getDomainValue("tpGeracaoProposta"));
		simulacao.setNrFatorCubagem(data.getBigDecimal("nrFatorCubagem"));
		simulacao.setNrFatorDensidade(data.getBigDecimal("nrFatorDensidade"));
		simulacao.setNrLimiteMetragemCubica(data.getBigDecimal("nrLimiteMetragemCubica"));
		simulacao.setNrLimiteQuantVolume(data.getBigDecimal("nrLimiteQuantVolume"));

		if(StringUtils.isNotEmpty(data.getString("usuario.nrMatricula"))) {
			Funcionario promotor = new Funcionario();
			promotor.setNrMatricula(data.getString("usuario.nrMatricula").toString());
			simulacao.setPromotor(promotor);
		}

		/** Store Proposta*/
		getSimulacaoService().storeProposta(simulacao, idTabelaPrecoNew, idTabelaPrecoOld);

		/** Armazena na sessão */
		setSimulacaoInSession(simulacao.getIdSimulacao());

		/** Cria mapa de retorno */
		TypedFlatMap result = new TypedFlatMap();
		result.put("simulacao.idSimulacao", simulacao.getIdSimulacao());
		result.put("tabelaPreco.moeda.dsMoeda", "");
		result.put("simulacao.setDtTabelaVigenciaInicial", simulacao.getDtTabelaVigenciaInicial());

		if (simulacao.getUsuarioByIdUsuarioAprovou() != null) {
			result.put("usuarioByIdUsuarioAprovou.nrMatricula", simulacao.getUsuarioByIdUsuarioAprovou().getNrMatricula());
			result.put("usuarioByIdUsuarioAprovou.nmUsuario", simulacao.getUsuarioByIdUsuarioAprovou().getNmUsuario());
		}
		result.put("dtAprovacao", simulacao.getDtAprovacao());

		if (simulacao.getTpSituacaoAprovacao() != null) {
			result.put("tpSituacaoAprovacao.description", simulacao.getTpSituacaoAprovacao().getDescriptionAsString());
			result.put("tpSituacaoAprovacao.value", simulacao.getTpSituacaoAprovacao().getValue());
		}

		String sgFilial = SessionUtils.getFilialSessao().getSgFilial();
		result.put("nrProposta", SimulacaoUtils.formatNrSimulacao(sgFilial, simulacao.getNrSimulacao().toString()));
		result.put("nrSimulacao", simulacao.getNrSimulacao());
		return result;
	}


	/**
	 * Consulta a filial pela sigla informada 
	 * @param map
	 * @return
	 */
	public List findLookupFilial(Map<String, Object> map) {
		FilterList filter = new FilterList(filialService.findLookup(map)) {
			public Map<String, Object> filterItem(Object item) {
				Filial filial = (Filial)item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idFilial", filial.getIdFilial());
				typedFlatMap.put("sgFilial",  filial.getSgFilial());
				typedFlatMap.put("pessoa.nmFantasia",  filial.getPessoa().getNmFantasia());
				return typedFlatMap;
			}
		};
		return (List)filter.doFilter();
	}
	
	public TypedFlatMap findDadosSessao() {
		TypedFlatMap result = new TypedFlatMap();
		BigDecimal idServicoPadrao = (BigDecimal) configuracoesFacade.getValorParametro("SERVICO_PADRAO");
		Servico servico = servicoService.findById(idServicoPadrao.longValue());
		if (servico != null) {
			result.put("idServicoPadrao", idServicoPadrao);
			if (servico.getDsServico() != null) {
				result.put("dsServicoPadrao", servico.getDsServico().getValue());
			}
		}
		return result;
	}

	/*
	 * METODOS PRIVADOS
	 */

	private boolean validateParameters(TypedFlatMap criteria) {
		return !(
				StringUtils.isBlank(criteria.getString("nrSimulacao")) &&
			criteria.getLong("cliente.idCliente") == null &&
			criteria.getLong("divisaoCliente.idDivisaoCliente") == null &&
			criteria.getLong("tabelaPreco.idTabelaPreco") == null &&
			criteria.getLong("tabelaPrecoFob.idTabelaPreco") == null &&
			criteria.getLong("servico.idServico") == null &&
			criteria.getLong("filial.idFilial") == null &&
			StringUtils.isEmpty(criteria.getString("tpSituacaoAprovacao"))
			);
	}

	private void setSimulacaoInSession(Long idSimulacao) {
		Simulacao simulacao = getSimulacaoService().findById(idSimulacao);
		SimulacaoUtils.setSimulacaoInSession(simulacao);
	}
	
	public List<String> findComboTipoGeracao(){
		List<String> validDomains = new ArrayList<String>();
		validDomains.add("C");
		validDomains.add("T");
		validDomains.add("P");
		return domainValueService.findByDomainNameAndValues("DM_TIPO_GERACAO_PROPOSTA", validDomains);
	}

	/*
	 * GETTERS E SETTERS
	 */
	public void setSimulacaoService(SimulacaoService serviceService) {
		super.defaultService = serviceService;
	}
	private SimulacaoService getSimulacaoService() {
		return (SimulacaoService) super.defaultService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setPropostaService(PropostaService propostaService) {
		this.propostaService = propostaService;
	}
	public void setParametroClienteService(ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}
	public void setPromotorClienteService(PromotorClienteService promotorClienteService) {
		this.promotorClienteService = promotorClienteService;
	}
	public void setProdutoClienteService(ProdutoClienteService produtoClienteService) {
		this.produtoClienteService = produtoClienteService;
	}
	
	public SimulacaoAnexoService getSimulacaoAnexoService() {
		return simulacaoAnexoService;
	}

	public void setSimulacaoAnexoService(SimulacaoAnexoService simulacaoAnexoService) {
		this.simulacaoAnexoService = simulacaoAnexoService;
	}

	public HistoricoEfetivacaoService getHistoricoEfetivacaoService() {
		return historicoEfetivacaoService;
	}

	public void setHistoricoEfetivacaoService(HistoricoEfetivacaoService historicoEfetivacaoService) {
		this.historicoEfetivacaoService = historicoEfetivacaoService;
	}

	/**
	 * LMS-6172 - Busca instância de <tt>SimulacaoAnexo</tt> e retorna mapa para
	 * preenchimento de <tt>&lt;adsm:form /&gt;</tt>. O atributo
	 * <tt>dsDocumento</tt> é incluído para popular o componente
	 * <tt>&lt;adsm:textbox /&gt;</tt> para upload/download.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>idSimulacaoAnexo</tt>
	 * @return mapa para popular <tt>&lt;adsm:form /&gt;</tt>
	 */
	public TypedFlatMap findSimulacaoAnexo(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		SimulacaoAnexo simulacaoAnexo = simulacaoAnexoService.findSimulacaoAnexo(criteria);
		map.put("idSimulacaoAnexo", simulacaoAnexo.getIdSimulacaoAnexo());
		map.put("dsAnexo", simulacaoAnexo.getDsAnexo());
		map.put("dsDocumento", Base64Util.encode(simulacaoAnexo.getDsDocumento()));
		return map;
	}

	/**
	 * LMS-6172 - Busca coleção de <tt>SimulacaoAnexo</tt> relacionadas a uma
	 * <tt>Simulacao</tt> para preenchimento de <tt>&lt;adsm:grid /&gt;</tt>.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return coleção para popular <tt>&lt;adsm:grid /&gt;</tt>
	 */
	public ResultSetPage<SimulacaoAnexo> findSimulacaoAnexoList(TypedFlatMap criteria) {
		return simulacaoAnexoService.findSimulacaoAnexoList(criteria);
	}

	/**
	 * LMS-6172 - Busca quantidade de <tt>SimulacaoAnexo</tt> relacionadas a uma
	 * <tt>Simulacao</tt> para preenchimento de <tt>&lt;adsm:grid /&gt;</tt>.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return quantidade de <tt>SimulacaoAnexo</tt>
	 */
	public Integer findSimulacaoAnexoRowCount(TypedFlatMap criteria) {
		return simulacaoAnexoService.findSimulacaoAnexoRowCount(criteria);
	}

	/**
	 * LMS-6172 - Processa inclusão ou atualização de <tt>SimulacaoAnexo</tt>. O
	 * atributo <tt>simulacao.idSimulacao</tt> é obrigatório no caso de
	 * inclusão. O atributo <tt>idSimulacaoAnexo</tt> é obrigatório no caso de
	 * atualização.
	 * 
	 * @param criteria
	 *            mapa de atributos de <tt>SimulacaoAnexo</tt>
	 */
	public void storeSimulacaoAnexo(TypedFlatMap criteria) {
		simulacaoAnexoService.storeSimulacaoAnexo(criteria);
	}

	/**
	 * LMS-6172 - Processa exclusão de conjunto de <tt>SimulacaoAnexo</tt>.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>ids</tt>
	 */
	public void removeByIdSimulacaoAnexo(TypedFlatMap criteria) {
		simulacaoAnexoService.removeSimulacaoAnexo(criteria);
	}

	/**
	 * LMS-6191 - Busca coleção de <tt>HistoricoEfetivacao</tt> relacionadas a
	 * uma <tt>Simulacao</tt> para preenchimento de <tt>&lt;adsm:grid /&gt;</tt>
	 * .
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return coleção para popular <tt>&lt;adsm:grid /&gt;</tt>
	 */
	public ResultSetPage<TypedFlatMap> findHistoricoEfetivacaoList(TypedFlatMap criteria) {
		return historicoEfetivacaoService.findHistoricoEfetivacaoList(criteria);
	}

	/**
	 * LMS-6191 - Busca quantidade de <tt>HistoricoEfetivacao</tt> relacionadas a uma
	 * <tt>Simulacao</tt> para preenchimento de <tt>&lt;adsm:grid /&gt;</tt>.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return quantidade de <tt>SimulacaoAnexo</tt>
	 */
	public Integer findHistoricoEfetivacaoRowCount(TypedFlatMap criteria) {
		return historicoEfetivacaoService.findHistoricoEfetivacaoRowCount(criteria);
	}

	/**
	 * LMS-6191 - Processa exclusão de conjunto de <tt>HistoricoEfetivacao</tt>.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>ids</tt>
	 */
	public void removeByIdHistoricoEfetivacao(TypedFlatMap criteria) {
		historicoEfetivacaoService.removeHistoricoEfetivacao(criteria);
	}

	public ManterPropostasClienteFormalidadesAction getManterPropostasClienteFormalidadesAction() {
		return manterPropostasClienteFormalidadesAction;
	}

	public void setManterPropostasClienteFormalidadesAction(ManterPropostasClienteFormalidadesAction manterPropostasClienteFormalidadesAction) {
		this.manterPropostasClienteFormalidadesAction = manterPropostasClienteFormalidadesAction;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}
