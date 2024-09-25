package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.tabelaprecos.util.TabelaPrecoUtils;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.DivisaoParcela;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.dao.ParametroClienteDAO;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.parametroClienteService"
 */
public class ParametroClienteService extends CrudService<ParametroCliente, Long> {
	private SimulacaoService simulacaoService;
	private ClienteService clienteService;
	private DivisaoClienteService divisaoClienteService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private ConfiguracoesFacade configuracoesFacade;
	private DivisaoParcelaService divisaoParcelaService;
	private UsuarioLMSService usuarioLMSService;

	/**
	 * Método utilizado pela Integração - CQPRO00008642
	 * @author Andre Valadas
	 * 
	 * @param idsTabelaDivisaoCliente
	 */
	public Integer updateByIdTabelaDivisaoCliente(final List<Long> idsTabelaDivisaoCliente) {
		return getParametroClienteDAO().updateByIdTabelaDivisaoCliente(idsTabelaDivisaoCliente);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idTabelaDivisaoCliente
	 * @param dsEspecificacaoRota
	 * @return <ParametroCliente>
	 */
	public ParametroCliente findParametroCliente(Long idTabelaDivisaoCliente, String dsEspecificacaoRota) {
		return getParametroClienteDAO().findParametroCliente(idTabelaDivisaoCliente, dsEspecificacaoRota);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Claiton Grings
	 * 
	 * @param idTabelaDivisaoCliente
	 * @param restricaoRotaOrigem
	 * @param restricaoRotaDestino
	 * @return <ParametroCliente>
	 */
	public ParametroCliente findParametroCliente(Long idTabelaDivisaoCliente, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		return getParametroClienteDAO().findParametroCliente(idTabelaDivisaoCliente, restricaoRotaOrigem, restricaoRotaDestino);
	}

	// renomeado antes era findById
	public Map<String, Object> findByIdMap(Long id) {
		List<TypedFlatMap> result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(getParametroClienteDAO().findParametroClienteById(id));
		return formatResult(result);
	}
	
	@Override
	public ParametroCliente findById(Long id) {
		return (ParametroCliente)super.findById(id);
	}
	
	
	public ParametroCliente findByIdParametro(Long id){
		return (ParametroCliente)super.findById(id);
	}
	

	public void evict(ParametroCliente parametroCliente) {
		getParametroClienteDAO().getAdsmHibernateTemplate().evict(parametroCliente);
	}

	public Map<String, Object> findHistoricoNegociacoes(java.lang.Long id) {
		List<TypedFlatMap> result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(getParametroClienteDAO().findHistoricoNegociacoes(id));
		return formatResult(result);
	}

	/**
	 * Formata e monta descrição dos campos 
	 * @param result
	 * @return
	 */
	private TypedFlatMap formatResult(List<TypedFlatMap> result) {
		if(result.isEmpty()) {
			return null;
		}
		TypedFlatMap toReturn = result.get(0);

		VarcharI18n tpTipoTabelaPreco = toReturn.getVarcharI18n("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco.description");
		Integer nrVersao = toReturn.getInteger("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.nrVersao");
		String tpSubtipoTabelaPreco = toReturn.getString("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco");
		String dsDescricao = toReturn.getString("tabelaDivisaoCliente.tabelaPreco.dsDescricao");
		VarcharI18n dsServico = toReturn.getVarcharI18n("tabelaDivisaoCliente.servico.dsServico");
		toReturn.put("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(toReturn.getString("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.tpIdentificacao.value"), toReturn.getString("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao")));
		toReturn.put("clienteByIdClienteRedespacho.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(toReturn.getString("clienteByIdClienteRedespacho.pessoa.tpIdentificacao.value"), toReturn.getString("clienteByIdClienteRedespacho.pessoa.nrIdentificacao")));

		/** Tabela de Preço */
		StringBuffer sb = new StringBuffer();
		if (tpTipoTabelaPreco != null) {
			sb.append(TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco, dsDescricao, dsServico));
		}
		toReturn.put("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao",sb.toString());

		/** UF Origem */
		sb = new StringBuffer();
		String sgUnidadeFederativaOrigem = toReturn.getString("unidadeFederativaByIdUfOrigem.sgUnidadeFederativa");
		String nmUnidadeFederativaOrigem = toReturn.getString("unidadeFederativaByIdUfOrigem.nmUnidadeFederativa");
		if(sgUnidadeFederativaOrigem!= null) {
			sb.append(sgUnidadeFederativaOrigem);
		}
		if(nmUnidadeFederativaOrigem!=null) {
			sb.append(" - ");
			sb.append(nmUnidadeFederativaOrigem);
		}
		toReturn.put("unidadeFederativaByIdUfOrigem.siglaDescricao",sb.toString());

		/** UF Destino */
		sb = new StringBuffer();
		String sgUnidadeFederativaDestino = toReturn.getString("unidadeFederativaByIdUfDestino.sgUnidadeFederativa");
		String nmUnidadeFederativaDestino = toReturn.getString("unidadeFederativaByIdUfDestino.nmUnidadeFederativa");
		if(sgUnidadeFederativaDestino!= null) {
			sb.append(sgUnidadeFederativaDestino);
		}
		if(nmUnidadeFederativaDestino!=null) {
			sb.append(" - ");
			sb.append(nmUnidadeFederativaDestino);
		}
		toReturn.put("unidadeFederativaByIdUfDestino.siglaDescricao",sb.toString());

		return toReturn;
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		getParametroClienteDAO().removeById(id,true);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getParametroClienteDAO().removeByIds(ids,true);
	}

	/**
	 * Excluir todos parametros gerados pela tabela PROPOSTA
	 * @author Andre Valadas
	 * @param idProposta
	 */
	public void removeBySimulacao(Simulacao simulacao) {
		validateExclusao(simulacao);
		List<ParametroCliente> parametrosCliente = getParametroClienteDAO().findParametroByIdSimulacao(simulacao.getIdSimulacao());
		for (ParametroCliente parametroCliente : parametrosCliente) {
			this.removeById(parametroCliente.getIdParametroCliente());
		}
	}

	/**
	 * @param idParametroCliente
	 * @param idSimulacao
	 */
	public void removeByIdProposta(Long idParametroCliente, Long idSimulacao) {
		Simulacao simulacao = simulacaoService.findById(idSimulacao);
		validateExclusao(simulacao);
		getParametroClienteDAO().removeById(idParametroCliente, true);
		simulacaoService.storePendenciaAprovacaoProposta(simulacao, false);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsProposta(List ids, Long idSimulacao) {
		Simulacao simulacao = simulacaoService.findById(idSimulacao);
		validateExclusao(simulacao);
		getParametroClienteDAO().removeByIds(ids);
		simulacaoService.storePendenciaAprovacaoProposta(simulacao, false);
	}

	private void validateExclusao(Simulacao simulacao) {
		validateOperation(simulacao, "LMS-30035", "LMS-30036");
	}
	
	private void validateOperation(Simulacao simulacao, String filialMessage, String efetivadaMessage) {
		if (!simulacao.getFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
			throw new BusinessException(filialMessage);
		}
		if (Boolean.TRUE.equals(simulacao.getBlEfetivada())) {
			throw new BusinessException(efetivadaMessage);
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(ParametroCliente bean) {
		return super.store(bean);
	}

	/**
	 * Utilizado no Reajuste de Clientes.
	 * 	-	Store simples, que chama direto da DAO, 
	 * 		nao passando pelas validacoes do beforeStore implementados na Service;
	 * @param bean
	 */
	public void storeBasic(ParametroCliente bean) {
		getParametroClienteDAO().store(bean);
	}

	/**
	 * Fecha Parametros Ativos e Vigentes referente a TabelaDivisaoCliente passada.
	 * @param idTabelaDivisaoCliente
	 * @param dtInicioVigencia
	 */
	public void storeCloseParametroAtivo(Long idTabelaDivisaoCliente, YearMonthDay dtInicioVigencia) {
		List<ParametroCliente> parametrosVigentes = findParametrizacaoAtiva(idTabelaDivisaoCliente, dtInicioVigencia);
		for (ParametroCliente parametroClienteVigente : parametrosVigentes) {
			parametroClienteVigente.setDtVigenciaFinal(dtInicioVigencia.minusDays(1));
			storeBasic(parametroClienteVigente);
		}
	}

	public java.io.Serializable storeParametroAtivo(Long idParametroClienteVigente, Long idParametroClienteNew, Long idTabelaDivisaoCliente, YearMonthDay dtInicioVigenciaNew, Long idSimulacao, Long idDivisaoCliente) {
		//Fecha a parametrização vigente - 5
		if(idParametroClienteVigente != null) {
			ParametroCliente parametroClienteVigente = findById(idParametroClienteVigente);
			parametroClienteVigente.setDtVigenciaFinal(dtInicioVigenciaNew.minusDays(1));
			
			//Valida a rota de preço
			validaRota(parametroClienteVigente);

			//Valida os indicadores
			validaValoresParametro(parametroClienteVigente);
			
			//CQPRO00028710
			storeBasic(parametroClienteVigente);
		}

		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setBlEfetivaProposta(Boolean.TRUE);
		ParametroCliente parametroClienteOriginal = findById(idParametroClienteNew);

		//Copia do parametro original para o parametro novo
		ParametroClienteUtils.copyParametroCliente(parametroClienteOriginal, parametroCliente);

		//Ajusta o novo parametroCliente
		parametroCliente.setTpSituacaoParametro(new DomainValue("A"));
		TabelaDivisaoCliente tabelaDivisaoCliente = new TabelaDivisaoCliente();
		tabelaDivisaoCliente.setIdTabelaDivisaoCliente(idTabelaDivisaoCliente);
		parametroCliente.setTabelaDivisaoCliente(tabelaDivisaoCliente);
		parametroCliente.setDtVigenciaInicial(dtInicioVigenciaNew);

		//Grupo Região
		if(parametroClienteOriginal.getGrupoRegiaoDestino() != null){
			parametroCliente.setGrupoRegiaoDestino(parametroClienteOriginal.getGrupoRegiaoDestino());
		}
		if(parametroClienteOriginal.getGrupoRegiaoOrigem() != null){
			parametroCliente.setGrupoRegiaoOrigem(parametroClienteOriginal.getGrupoRegiaoOrigem());
		}
		
		//Atualiza a simulação.
		simulacaoService.updatePropostaEfetivadaFromSimulacao(idSimulacao, idDivisaoCliente, parametroCliente);

		//Atualiza Parametro Cliente.
		updateTbDivClienteFromParametroCliente(parametroClienteOriginal, idTabelaDivisaoCliente);

		return store(parametroCliente);
	}

	public java.io.Serializable updateTbDivClienteFromParametroCliente(ParametroCliente parametroClienteOriginal, Long idTabelaDivisaoCliente) {
		TabelaDivisaoCliente tabelaDivisaoCliente = new TabelaDivisaoCliente();
		tabelaDivisaoCliente.setIdTabelaDivisaoCliente(idTabelaDivisaoCliente);
		parametroClienteOriginal.setTabelaDivisaoCliente(tabelaDivisaoCliente);

		return store(parametroClienteOriginal);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setParametroClienteDAO(ParametroClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ParametroClienteDAO getParametroClienteDAO() {
		return (ParametroClienteDAO) getDao();
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = getParametroClienteDAO().findPaginated((TypedFlatMap)criteria, FindDefinition.createFindDefinition(criteria));
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getParametroClienteDAO().getRowCount(criteria);
	}

	public ResultSetPage findPaginatedHistoricoNegociacoes(TypedFlatMap criteria) {
		ResultSetPage rsp = getParametroClienteDAO().findPaginatedHistoricoNegociacoes(criteria, FindDefinition.createFindDefinition(criteria));
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	public Integer getRowCountHistoricoNegociacoes(TypedFlatMap criteria) {
		return getParametroClienteDAO().getRowCountHistoricoNegociacoes(criteria);
	}
	
	@Override
	protected ParametroCliente beforeInsert(ParametroCliente bean) {
		ParametroCliente pc = bean;
		if (pc.getDtVigenciaFinal()!=null) {
			if (JTDateTimeUtils.comparaData(pc.getDtVigenciaFinal(),JTDateTimeUtils.getDataAtual()) < 0) {
				throw new BusinessException("LMS-00007");
			}
		}
		return super.beforeInsert(bean);
	}
	
	@Override
	protected ParametroCliente beforeUpdate(ParametroCliente bean) {
		ParametroCliente pc = bean;
		if (pc.getDtVigenciaFinal()!=null) {
			if(JTDateTimeUtils.comparaData(pc.getDtVigenciaFinal(),JTDateTimeUtils.getDataAtual().minusDays(1)) < 0) {
				throw new BusinessException("LMS-00007");
			}
		}
		return super.beforeUpdate(bean);
	}

	@Override
	protected ParametroCliente beforeStore(ParametroCliente bean) {
		ParametroCliente pc = bean;

		//Valida a rota de preço
		validaRota(pc);

		//Valida os indicadores
		validaValoresParametro(pc);

		//Verifica se existe alguma parametrização vigente para os dados informados.
		if(BooleanUtils.isFalse(pc.getBlEfetivaProposta())){
		findParametrizacaoAtiva(pc,"LMS-01115");
		}

		return super.beforeStore(bean);
	}

	private void validaRota(ParametroCliente pc) {
		//Se for Cotacao nao valida rota 
		if("C".equals(pc.getTpSituacaoParametro().getValue())) {
			return;
		}

		RestricaoRota restricaoRotaOrigem = RotaPrecoUtils.getRestricaoRotaOrigem(pc);
		boolean isRotaOrigemValida = RotaPrecoUtils.isRotaValida(restricaoRotaOrigem);

		RestricaoRota restricaoRotaDestino = RotaPrecoUtils.getRestricaoRotaDestino(pc);
		boolean isRotaDestinoValida = RotaPrecoUtils.isRotaValida(restricaoRotaDestino);

		if (!(isRotaOrigemValida && isRotaDestinoValida)){
			throw new BusinessException("LMS-30001");
		}
	}

	private void validaValoresParametro(ParametroCliente pc) {
		String[] indicadores;

		// 4.18
		String tpIndicadorMinFretePeso = pc.getTpIndicadorMinFretePeso().getValue();
		BigDecimal vlMinFretePeso = BigDecimalUtils.defaultBigDecimal(pc.getVlMinFretePeso());
		if ("P".equals(tpIndicadorMinFretePeso) || "V".equals(tpIndicadorMinFretePeso)) {
			if (!CompareUtils.ge(vlMinFretePeso, BigDecimalUtils.ZERO)) {
				throw new BusinessException("LMS-01031");
			}
		}
		// 4.19 e 4.20
		indicadores = new String[] {"A"};
		String tpIndicadorPercMinimoProgr = pc.getTpIndicadorPercMinimoProgr().getValue();
		BigDecimal vlPercMinimoProgr = BigDecimalUtils.defaultBigDecimal(pc.getVlPercMinimoProgr());
		validaDescontoValor(indicadores, tpIndicadorPercMinimoProgr, vlPercMinimoProgr, "percentualMinimoProgressivo");
		// 4.21
		String tpIndicadorFretePeso = pc.getTpIndicadorFretePeso().getValue();
		if ("V".equals(tpIndicadorFretePeso)) {
			if (!"D".equals(tpIndicadorPercMinimoProgr) || 0.0 != vlPercMinimoProgr.doubleValue()) {
				throw new BusinessException("LMS-01034");
			}
		}
		// 4.22 e 4.23
		indicadores = new String[] {"A", "V"};
		BigDecimal vlFretePeso = BigDecimalUtils.defaultBigDecimal(pc.getVlFretePeso());
		validaDescontoValor(indicadores, tpIndicadorFretePeso, vlFretePeso, "fretePeso");
		// 4.24 e 4.25
		BigDecimal vlMinimoFreteQuilo = BigDecimalUtils.defaultBigDecimal(pc.getVlMinimoFreteQuilo());
		if (0.0 != vlMinimoFreteQuilo.doubleValue()) {
			if ("V".equals(tpIndicadorFretePeso)) {
				if (!CompareUtils.ge(vlMinimoFreteQuilo, BigDecimalUtils.ZERO)) {
					throw new BusinessException("LMS-01036");
				}
			} else {
				throw new BusinessException("LMS-01035");
			}
		}
		// 4.26 e 4.27
		BigDecimal vlFreteVolume = BigDecimalUtils.defaultBigDecimal(pc.getVlFreteVolume());
		if (0.0 != vlFreteVolume.doubleValue()) {
			if ("T".equals(tpIndicadorFretePeso) && 
					"D".equals(tpIndicadorPercMinimoProgr) &&
					CompareUtils.eq(vlPercMinimoProgr, BigDecimalUtils.ZERO)) {
				if (!CompareUtils.ge(vlFreteVolume, BigDecimalUtils.ZERO)) {
					throw new BusinessException("LMS-01038");
				}
			} else {
				throw new BusinessException("LMS-01037");
			}
		}
		// 4.28 e 4.28
		String tpTarifaMinima = pc.getTpTarifaMinima().getValue();
		BigDecimal vlTarifaMinima = BigDecimalUtils.defaultBigDecimal(pc.getVlTarifaMinima());
		validaDescontoValor(indicadores, tpTarifaMinima, vlTarifaMinima, "tarifaMinima");
		// 4.30
		Boolean blPagaPesoExcedente = pc.getBlPagaPesoExcedente();
		if (Boolean.TRUE.equals(blPagaPesoExcedente)) {
			if (CompareUtils.eq(vlMinimoFreteQuilo, BigDecimalUtils.ZERO) || !"P".equals(tpIndicadorMinFretePeso)) {
				throw new BusinessException("LMS-01040");
			}
		}
		// 4.31 e 4.32
		indicadores = new String[] {"A", "V"};
		String tpIndicadorAdvalorem = pc.getTpIndicadorAdvalorem().getValue();
		BigDecimal vlAdvalorem = BigDecimalUtils.defaultBigDecimal(pc.getVlAdvalorem());
		validaDescontoValor(indicadores, tpIndicadorAdvalorem, vlAdvalorem, "advalorem1");
		// 4.33 e 4.34
		String tpIndicadorAdvalorem2 = pc.getTpIndicadorAdvalorem2().getValue();
		BigDecimal vlAdvalorem2 = BigDecimalUtils.defaultBigDecimal(pc.getVlAdvalorem2());
		validaDescontoValor(indicadores, tpIndicadorAdvalorem2, vlAdvalorem2, "advalorem2");
		// 4.35 e 4.36
		indicadores = new String[] {"A", "V"};
		String tpIndicadorValorReferencia = pc.getTpIndicadorValorReferencia().getValue();
		BigDecimal vlValorReferencia = BigDecimalUtils.defaultBigDecimal(pc.getVlValorReferencia());
		validaDescontoValor(indicadores, tpIndicadorValorReferencia, vlValorReferencia, "valorReferencia");
		// 4.37 e 4.38
		BigDecimal pcFretePercentual = BigDecimalUtils.defaultBigDecimal(pc.getPcFretePercentual());
		if (0.0 != pcFretePercentual.doubleValue()) {
			if ("T".equals(tpIndicadorAdvalorem) && "T".equals(tpIndicadorAdvalorem2)) {
				if (!CompareUtils.between(pcFretePercentual, BigDecimalUtils.ZERO, BigDecimalUtils.HUNDRED)) {
					throw new BusinessException("LMS-01046");
				}
			} else {
				throw new BusinessException("LMS-01044");
			}
		}
		// 4.40 e 4.41
		BigDecimal vlMinimoFretePercentual = BigDecimalUtils.defaultBigDecimal(pc.getVlMinimoFretePercentual());
		if (0.0 != vlMinimoFretePercentual.doubleValue()) {
			if (CompareUtils.ne(pcFretePercentual, BigDecimalUtils.ZERO)) {
				if (!CompareUtils.ge(vlMinimoFretePercentual, BigDecimalUtils.ZERO)) {
					throw new BusinessException("LMS-01047");
				}
			} else {
				throw new BusinessException("LMS-01045");
			}
		}
		// 4.42 e 4.43
		BigDecimal vlToneladaFretePercentual = BigDecimalUtils.defaultBigDecimal(pc.getVlToneladaFretePercentual());
		if (0.0 != vlToneladaFretePercentual.doubleValue()) {
			if (CompareUtils.ne(pcFretePercentual, BigDecimalUtils.ZERO)) {
				if (!CompareUtils.ge(vlToneladaFretePercentual, BigDecimalUtils.ZERO)) {
					throw new BusinessException("LMS-01048");
				}
			} else {
				throw new BusinessException("LMS-01045");
			}
		}
		// 4.44 e 4.45
		BigDecimal psFretePercentual = BigDecimalUtils.defaultBigDecimal(pc.getPsFretePercentual());
		if (0.0 != psFretePercentual.doubleValue()) {
			if (CompareUtils.ne(pcFretePercentual, BigDecimalUtils.ZERO)) {
				if (!CompareUtils.ge(psFretePercentual, BigDecimalUtils.ZERO)) {
					throw new BusinessException("LMS-01049");
				}
			} else {
				throw new BusinessException("LMS-01045");
			}
		}
		// 4.46 e 4.47
		indicadores = new String[] {"A", "V"};
		String tpIndicadorPercentualGris = pc.getTpIndicadorPercentualGris().getValue();
		BigDecimal vlPercentualGris = BigDecimalUtils.defaultBigDecimal(pc.getVlPercentualGris());
		validaDescontoValor(indicadores, tpIndicadorPercentualGris, vlPercentualGris, "percentualGris");
		// 4.48 e 4.49
		indicadores = new String[] {"A", "V"};
		String tpIndicadorMinimoGris = pc.getTpIndicadorMinimoGris().getValue();
		BigDecimal vlMinimoGris = BigDecimalUtils.defaultBigDecimal(pc.getVlMinimoGris());
		validaDescontoValor(indicadores, tpIndicadorMinimoGris, vlMinimoGris, "minimoGris");

		// TRT
		indicadores = new String[] {"A", "V"};
		String tpIndicadorPercentualTrt = pc.getTpIndicadorPercentualTrt().getValue();
		BigDecimal vlPercentualTrt = BigDecimalUtils.defaultBigDecimal(pc.getVlPercentualTrt());
		validaDescontoValor(indicadores, tpIndicadorPercentualTrt, vlPercentualTrt, "percentualTrt");
		// TRT
		indicadores = new String[] {"A", "V"};
		String tpIndicadorMinimoTrt = pc.getTpIndicadorMinimoTrt().getValue();
		BigDecimal vlMinimoTrt = BigDecimalUtils.defaultBigDecimal(pc.getVlMinimoTrt());
		validaDescontoValor(indicadores, tpIndicadorMinimoTrt, vlMinimoTrt, "minimoTrt");

		// 4.50 e 4.51
		indicadores = new String[] {"V", "P", "F", "Q", "A"};
		String tpIndicadorPedagio = pc.getTpIndicadorPedagio().getValue();
		BigDecimal vlPedagio = BigDecimalUtils.defaultBigDecimal(pc.getVlPedagio());
		validaDescontoValor(indicadores, tpIndicadorPedagio, vlPedagio, "valorPedagio");
		
		indicadores = new String[] {"V", "A"};
		String tpIndicadorPercentualTde = pc.getTpIndicadorPercentualTde().getValue();
		BigDecimal vlPercentualTde = BigDecimalUtils.defaultBigDecimal(pc.getVlPercentualTde());
		validaDescontoValor(indicadores, tpIndicadorPercentualTde, vlPercentualTde, "percentualTde");
		
		indicadores = new String[] {"V", "A"};
		String tpIndicadorMinimoTde = pc.getTpIndicadorMinimoTde().getValue();
		BigDecimal vlMinimoTde = BigDecimalUtils.defaultBigDecimal(pc.getVlMinimoTde());
		validaDescontoValor(indicadores, tpIndicadorMinimoTde, vlMinimoTde, "minimoTde");
		
		// 4.52, 4.53 e 4.54
		String tpIndicVlrTblEspecifica = pc.getTpIndicVlrTblEspecifica().getValue();
		BigDecimal pcDescontoFreteTotal = BigDecimalUtils.defaultBigDecimal(pc.getPcDescontoFreteTotal());
		if (0.0 != pcDescontoFreteTotal.doubleValue()) {
			if ("T".equals(tpIndicadorMinFretePeso) && "T".equals(tpIndicadorFretePeso) &&
				"T".equals(tpTarifaMinima) && "T".equals(tpIndicVlrTblEspecifica) &&
				"T".equals(tpIndicadorAdvalorem) && "T".equals(tpIndicadorAdvalorem2) &&
				"T".equals(tpIndicadorValorReferencia) && "T".equals(tpIndicadorPercentualGris) &&
				"T".equals(tpIndicadorMinimoGris) &&
				CompareUtils.eq(vlPercMinimoProgr, BigDecimalUtils.ZERO) &&
				CompareUtils.eq(vlMinimoFreteQuilo, BigDecimalUtils.ZERO) &&
				CompareUtils.eq(vlFreteVolume, BigDecimalUtils.ZERO) &&
				CompareUtils.eq(pcFretePercentual, BigDecimalUtils.ZERO)) {
				if (!CompareUtils.between(pcDescontoFreteTotal, BigDecimalUtils.ZERO, BigDecimalUtils.HUNDRED)) {
					throw new BusinessException("LMS-01053");
				}
			} else {
				throw new BusinessException("LMS-01052");
			}
		}
		// 4.55
		BigDecimal pcCobrancaReentrega = BigDecimalUtils.defaultBigDecimal(pc.getPcCobrancaReentrega());
		if (0.0 != pcCobrancaReentrega.doubleValue()) {
			if (!CompareUtils.between(pcCobrancaReentrega, BigDecimalUtils.ZERO, BigDecimalUtils.HUNDRED)) {
				throw new BusinessException("LMS-01054");
			}
		}
		// 4.56
		BigDecimal pcCobrancaDevolucoes = BigDecimalUtils.defaultBigDecimal(pc.getPcCobrancaDevolucoes());
		if (0.0 != pcCobrancaDevolucoes.doubleValue()) {
			if (!CompareUtils.between(pcCobrancaDevolucoes, BigDecimalUtils.ZERO, BigDecimalUtils.HUNDRED)) {
				throw new BusinessException("LMS-01055");
			}
		}
	}
	
	private void validaDescontoValor(String[] indicadores, String indicador, BigDecimal valor, String label) {
		boolean maiorIgual = false;
		for (int i = 0; i < indicadores.length; i++) {
			if (indicador.equals(indicadores[i])) {
				maiorIgual = true;
				break;
			}
		}
		if (maiorIgual) {
			if (!CompareUtils.ge(valor, BigDecimalUtils.ZERO)) {
				throw new BusinessException("LMS-01050", new Object[]{configuracoesFacade.getMensagem(label)});
			}
		} else if (indicador.equals("D")) {
			if (!CompareUtils.between(valor, BigDecimalUtils.ZERO, BigDecimalUtils.HUNDRED)) {
				throw new BusinessException("LMS-01050", new Object[]{configuracoesFacade.getMensagem(label)});
			}
		}
	}

	public Integer getRowCountByIdCotacao(Long idCotacao) {
		return getParametroClienteDAO().getRowCountByIdCotacao(idCotacao);
	}

	public TypedFlatMap findByIdCotacao(Long idCotacao) {
		return getParametroClienteDAO().findByIdCotacao(idCotacao);
	}

	public Integer getRowCountParametrizadoRedespacho(Long idCliente) {
		return getParametroClienteDAO().getRowCountParametrizadoRedespacho(idCliente);
	}
	
	public Boolean existParametroByIdSimulacao(Long idSimulacao) {
		return getParametroClienteDAO().existParametroByIdSimulacao(idSimulacao);
	}
	
	public Boolean existParametroByIdProposta(Long idProposta) {
		return getParametroClienteDAO().existParametroByIdProposta(idProposta);
	}

	public List<Long> findIdsParametrosByIdSimulacao(Long idSimulacao) {
		return getParametroClienteDAO().findIdsParametrosByIdSimulacao(idSimulacao);
	}
	
	public List<ParametroCliente> findParametroByIdSimulacao(Long idSimulacao) {
		return getParametroClienteDAO().findParametroByIdSimulacao(idSimulacao);
	}

	public List<ParametroCliente> findParametroByIdProposta(Long idProposta) {
		return getParametroClienteDAO().findParametroByIdProposta(idProposta);
	}
	
	public List<ParametroCliente> findParametroByIdCotacao(Long idCotacao) {
		return getParametroClienteDAO().findParametroByIdCotacao(idCotacao);
	}

	public void removeById(Long idParametroCliente, boolean flushSession) {
		getParametroClienteDAO().removeById(idParametroCliente, true);
		if (flushSession) {
			getParametroClienteDAO().getSessionFactory().getCurrentSession().flush();
		}
	}

	public void validateSituacaoParametrizacao(Long idParametroCliente, String errorMessageKey){
		Map<String, Object> parametroCliente = findByIdMap(idParametroCliente);

		if(!parametroCliente.get("tpSituacaoParametro.value").equals("P")) {
			throw new BusinessException(errorMessageKey);
		}
	}

	/**
	 * Faz a cópia dos dados do cliente origem para o cliente destino.
	 * @param tpSituacaoParametro
	 * @param dtVigenciaFinal
	 * @param idClienteOrigem
	 * @param idClienteDestino
	 * @param idDivisaoCliente
	 * @param idTabelaDivisaoCliente
	 * @return
	 */
	public java.io.Serializable storeCopiaParametrizacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idClienteOrigem, Long idClienteDestino, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {

		Cliente clienteOrigem = findClienteByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idClienteOrigem, idDivisaoCliente, idTabelaDivisaoCliente);
		Cliente clienteDestino = clienteService.findByIdInitLazyProperties(idClienteDestino, false);
		if(clienteOrigem == null) {
			throw new BusinessException("LMS-01119");
		}
		
		validateDivisoes(idDivisaoCliente, clienteOrigem.getIdCliente(), idClienteDestino);
		copyDivisaoCliente(clienteDestino, tpSituacaoParametro, dtVigenciaFinal, idClienteOrigem, idDivisaoCliente, idTabelaDivisaoCliente);

		return null;
	}

	private void copyDivisaoCliente(Cliente clienteDestino, DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idClienteOrigem, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		Long idNew = Long.valueOf(0);
		Long idAnt = Long.valueOf(0);
		List<DivisaoCliente> divisaoClientes = findDivisaoClienteByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idClienteOrigem, idDivisaoCliente, idTabelaDivisaoCliente);
		for(DivisaoCliente divisaoCliente : divisaoClientes) {
			DivisaoCliente divisaoClienteNew = new DivisaoCliente();
			idNew = divisaoCliente.getIdDivisaoCliente();
			if(idNew.compareTo(idAnt) != 0) {
				copyTabelaDivisaoCliente(divisaoClienteNew, tpSituacaoParametro, dtVigenciaFinal, idClienteOrigem, divisaoCliente.getIdDivisaoCliente(), idTabelaDivisaoCliente);
				divisaoClienteNew.setDsDivisaoCliente(divisaoCliente.getDsDivisaoCliente());
				divisaoClienteNew.setCliente(clienteDestino);
				divisaoClienteNew.setTpSituacao(new DomainValue("A"));

				//Verifica se já existe uma divisão com o código da divisão que está sendo incluída para o cliente destino.
				DivisaoCliente divisaoClienteOld = divisaoClienteService.findByIdClienteCdDivisao(clienteDestino.getIdCliente(), divisaoCliente.getCdDivisaoCliente());
				if(divisaoClienteOld == null) {
					divisaoClienteNew.setCdDivisaoCliente(divisaoCliente.getCdDivisaoCliente());
				} else {
					Long newCdDivisaoCliente = divisaoClienteService.findNewCdDivisaoCliente(clienteDestino.getIdCliente());
					divisaoClienteNew.setCdDivisaoCliente(newCdDivisaoCliente);
				}
				divisaoClienteService.store(divisaoClienteNew);
				idAnt = idNew; 
			}
		}
	}

	private void validateDivisoes(Long idDivisao, Long idClienteOrigem, Long idClienteDestino){
		List<HashMap> divisoes = new ArrayList<>();
		if (idDivisao != null){
			DivisaoCliente divisao = divisaoClienteService.findById(idDivisao);
			if (divisao != null){
				HashMap map = new HashMap();
				map.put("idDivisaoCliente", divisao.getIdDivisaoCliente());
				map.put("dsDivisaoCliente", divisao.getDsDivisaoCliente());
				divisoes.add(map);
			}
		}else{
			divisoes = divisaoClienteService.findByIdCliente(idClienteOrigem);
		}
		
		List<HashMap> divisoesDestino = divisaoClienteService.findByIdCliente(idClienteDestino);
		
		for(HashMap map :divisoes){
			String dsDivisao = (String)map.get("dsDivisaoCliente");
			for(HashMap destino:divisoesDestino){
				if (dsDivisao.equalsIgnoreCase((String)destino.get("dsDivisaoCliente"))){
					throw new BusinessException("LMS-01032");
				}
			}
		}
	}
	
	private void copyTabelaDivisaoCliente(DivisaoCliente divisaoClienteNew, DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idClienteOrigem, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		Long idNew = Long.valueOf(0);
		Long idAnt = Long.valueOf(0);
		List<TabelaDivisaoCliente> destino = new ArrayList<>();
		List<TabelaDivisaoCliente> tabelaDivisaoClientes = findTabelaDivisaoClienteByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idClienteOrigem, idDivisaoCliente, idTabelaDivisaoCliente);
		for(TabelaDivisaoCliente tabelaDivisaoCliente: tabelaDivisaoClientes) {
			TabelaDivisaoCliente tabelaDivisaoClienteNew = new TabelaDivisaoCliente();
			idNew = tabelaDivisaoCliente.getIdTabelaDivisaoCliente();
			if(idNew.compareTo(idAnt) != 0) {
				copyParametroCliente(tabelaDivisaoClienteNew, tpSituacaoParametro, dtVigenciaFinal, idClienteOrigem, idDivisaoCliente, tabelaDivisaoCliente.getIdTabelaDivisaoCliente());
				tabelaDivisaoClienteNew.setPcAumento(tabelaDivisaoCliente.getPcAumento());
				tabelaDivisaoClienteNew.setBlAtualizacaoAutomatica(tabelaDivisaoCliente.getBlAtualizacaoAutomatica());
				tabelaDivisaoClienteNew.setServico(tabelaDivisaoCliente.getServico());
				tabelaDivisaoClienteNew.setTabelaPreco(tabelaDivisaoCliente.getTabelaPreco());
				tabelaDivisaoClienteNew.setDivisaoCliente(divisaoClienteNew);
				
				//Jira LMS-7209
				tabelaDivisaoClienteNew.setBlImpBaseDevolucao(tabelaDivisaoCliente.getBlImpBaseDevolucao());
				tabelaDivisaoClienteNew.setBlImpBaseReentrega(tabelaDivisaoCliente.getBlImpBaseReentrega());
				tabelaDivisaoClienteNew.setBlImpBaseRefaturamento(tabelaDivisaoCliente.getBlImpBaseRefaturamento());
				tabelaDivisaoClienteNew.setTpPesoCalculo(tabelaDivisaoCliente.getTpPesoCalculo());
				
				List<DivisaoParcela> divisaoParcelaList = getDivisaoParcelas(idNew, tabelaDivisaoClienteNew);
				if (!divisaoParcelaList.isEmpty()) {
					tabelaDivisaoClienteNew.setDivisaoParcelas(divisaoParcelaList);
				}
				
				destino.add(tabelaDivisaoClienteNew);
				idAnt = idNew; 
			}
		}
		divisaoClienteNew.setTabelaDivisaoClientes(destino);
	}
	
	private DivisaoParcela copyDivisaoParcela(Long idDivisaoParcela, TabelaDivisaoCliente tabelaDivisaoClienteNew, UsuarioLMS usuario) {
		DivisaoParcela divisaoParcela = divisaoParcelaService.findById(idDivisaoParcela);
		return new DivisaoParcela(null, tabelaDivisaoClienteNew, 
			divisaoParcela.getParcelaPreco(), 
			divisaoParcela.getParcelaPrecoCobranca(),
			divisaoParcela.getTpSituacao(),
			usuario,
			usuario,
			JTDateTimeUtils.getDataHoraAtual(),
			JTDateTimeUtils.getDataHoraAtual());
	}

	private List<DivisaoParcela> getDivisaoParcelas(Long idTabelaDivisaoCliente, TabelaDivisaoCliente tabelaDivisaoClienteNew) {
		List<DivisaoParcela> divisaoParcelaList = new ArrayList<>();
		List<Map<String, Object>> parcelasMap = divisaoParcelaService.findByTabelaDivisaoCliente(idTabelaDivisaoCliente);
		if (!parcelasMap.isEmpty()) {
			UsuarioLMS usuario = usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
			for (Map parcelas : parcelasMap) {
				divisaoParcelaList.add(copyDivisaoParcela((Long)parcelas.get("ID_DIVISAO_PARCELA"), tabelaDivisaoClienteNew, usuario));
			}
		}
		return divisaoParcelaList;
	}

	private void copyParametroCliente(TabelaDivisaoCliente tabelaDivisaoClienteNew, DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idClienteOrigem, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		List<ParametroCliente> destino = new ArrayList<>();
		List<ParametroCliente> parametroClientes = findParametroClienteByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idClienteOrigem, idDivisaoCliente, idTabelaDivisaoCliente);
		for(ParametroCliente parametroCliente : parametroClientes) {
			ParametroCliente parametroClienteNew = new ParametroCliente();

			//Verifica se há parametrização vigente.
			findParametrizacaoAtiva(parametroCliente,"LMS-01115");

			ParametroClienteUtils.copyParametroCliente(parametroCliente, parametroClienteNew);
			parametroClienteNew.setTabelaDivisaoCliente(tabelaDivisaoClienteNew);

			destino.add(parametroClienteNew);
		}
		tabelaDivisaoClienteNew.setParametroClientes(destino);
	}

	public Cliente findClienteByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		return clienteService.findClienteByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idCliente, idDivisaoCliente, idTabelaDivisaoCliente);
	}
 
	public List findDivisaoClienteByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		return divisaoClienteService.findDivisaoClienteByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idCliente, idDivisaoCliente, idTabelaDivisaoCliente);
	}

	public List findTabelaDivisaoClienteByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		return tabelaDivisaoClienteService.findTabelaDivisaoClienteByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idCliente, idDivisaoCliente, idTabelaDivisaoCliente);
	}

	public List findParametroClienteByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		return getParametroClienteDAO().findByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idCliente, idDivisaoCliente, idTabelaDivisaoCliente);
	}

	public List findByIdTabelaDivisaoCliente(Long idTabelaDivisaoCliente, DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaInicialTabela) {
		return getParametroClienteDAO().findByIdTabelaDivisaoCliente(idTabelaDivisaoCliente, tpSituacaoParametro, dtVigenciaInicialTabela);
	}

	/**
	 * Verifica se existe alguma parametrização vigente no período da parametrização que está sendo copiada.
	 * @param parametroCliente
	 * @param errorMessageKey
	 */
	public void findParametrizacaoAtiva(ParametroCliente parametroCliente, String errorMessageKey) {
		if(parametroCliente.getTpSituacaoParametro().getValue().equals("A")) {
			Long idParametroCliente = findParametrizacaoAtiva(parametroCliente);
			if (idParametroCliente != null){
				throw new BusinessException(errorMessageKey);
			}
		}
	}
	public Long findParametrizacaoAtiva(ParametroCliente parametroCliente) {
		return getParametroClienteDAO().findParametrizacaoAtiva(parametroCliente);
	}
	
	/**
	 * Verifica se ja existe uma parametrizacao com a mesma rota do parametro
	 * recebido para a mesma simulacao deste parametro.
	 * 
	 * @param parametroCliente
	 * @param tpSituacaoParametro
	 * @return
	 */
	public void validateInclusaoSimulacao(ParametroCliente parametroCliente, String tpSituacaoParametro, String errorKey) {
		Long result = getParametroClienteDAO().findParametrizacaoByTpSituacaoParametro(parametroCliente, tpSituacaoParametro);
		if (result != null) {
			throw new BusinessException(errorKey);
		}
	}

	/**
	 * Verifica se existe parametrização vigente.
	 * @param parametroClienteMap
	 * @param idTabelaDivisaoCliente
	 * @param dtInicioVigencia
	 * @return
	 */
	public Long findParametrizacaoAtivaByDtVigencia(Long idParametroCliente, Long idTabelaDivisaoCliente, YearMonthDay dtInicioVigencia){
		Map parametroClienteMap = findByIdMap(idParametroCliente);
		
		return getParametroClienteDAO().findParametrizacaoAtivaByDtVigencia(parametroClienteMap, idTabelaDivisaoCliente, dtInicioVigencia);
	}

	/**
	 * Busca Parametros Ativos referente a TabelaDivisaoCliente passada.
	 * @param idTabelaDivisaoCliente
	 * @param dtInicioVigencia
	 * @return
	 */
	public List<ParametroCliente> findParametrizacaoAtiva(Long idTabelaDivisaoCliente, YearMonthDay dtInicioVigencia){
		return getParametroClienteDAO().findParametrizacaoAtiva(idTabelaDivisaoCliente, dtInicioVigencia);
	}


	/**
	 * Obtem um Parametro Cliente vigente através da sua rota e cliente
	 * @param idCliente
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return
	 */
	public List<ParametroCliente> findParametroClienteVigenteByRota(Long idCliente, Long idParamOrigem, Long idParamDestino) {
		return getParametroClienteDAO().findParametroClienteVigenteByRota(idCliente, idParamOrigem, idParamDestino);
	}	
		
	/**
	 * Atualiza a data de vigência final em todos os parametros relacionados com
	 * a tabela divisao cliente recebida.
	 * 
	 * @param dtVigenciaFinal
	 *            data de vigência final para ser atualizada
	 * @param idTabelaDivisaoCliente
	 *            identificador da tabela divisão cliente
	 * @return numero de entidades atualizadas
	 */
	public Integer updateVigenciaFinalByIdTabelaDivisaoCliente(YearMonthDay dtVigenciaFinal, Long idTabelaDivisaoCliente) {
		return getParametroClienteDAO().updateVigenciaFinalByIdTabelaDivisaoCliente(dtVigenciaFinal, idTabelaDivisaoCliente);
	}

	public List<Map> findListParametrosByRotaFilial(Long idFilial) {		
		return getParametroClienteDAO().findListParametrosByRotaFilial(idFilial);
	}

	public void removeParametroClienteDataSuperiorReajuste(Long idTabelaDivisaoCliente){
		getParametroClienteDAO().removeParametroClienteDataSuperiorReajuste(idTabelaDivisaoCliente);
	}
	
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public boolean validateParametroCliente(ParametroCliente parametroCliente) {
		return getParametroClienteDAO().validateParametroCliente(parametroCliente);
	}

	public Simulacao findSimulacaoByIdParametroCliente(Long id) {
		return getParametroClienteDAO().findSimulacaoByIdParametroCliente(id);
	}

	public void setDivisaoParcelaService(DivisaoParcelaService divisaoParcelaService) {
		this.divisaoParcelaService = divisaoParcelaService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
	
}