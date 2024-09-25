package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteTabelaClienteService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.util.TabelaPrecoUtils;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.HistoricoReajusteCliente;
import com.mercurio.lms.vendas.model.ReajusteCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.HistoricoReajusteClienteService;
import com.mercurio.lms.vendas.model.service.ReajusteClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.SimulacaoUtils;

/**
 * @author André Valadas
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterReajusteClienteAction"
 */
public class ManterReajusteClienteAction extends CrudAction {
	private FilialService filialService;
	private DivisaoClienteService divisaoClienteService;
	private TabelaPrecoService tabelaPrecoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private ClienteService clienteService;
	private ReajusteTabelaClienteService reajusteTabelaClienteService;
	private HistoricoFilialService historicoFilialService;
	private HistoricoReajusteClienteService historicoReajusteClienteService;

	/**
	 * Verifica se Filial da Sessao é Matriz, caso contrário desabilita Campos da Tela.
	 * @return
	 */
	public TypedFlatMap validateSession() {
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("isMatriz", Boolean.TRUE);
		Filial filialSessao = SessionUtils.getFilialSessao();
		Filial filialMatriz = historicoFilialService.findFilialMatriz(SessionUtils.getEmpresaSessao().getIdEmpresa());
		if(filialMatriz == null || !filialMatriz.equals(filialSessao)) {
			toReturn.put("idFilial", filialSessao.getIdFilial());
			toReturn.put("sgFilial",  filialSessao.getSgFilial());
			toReturn.put("pessoa.nmFantasia",  filialSessao.getPessoa().getNmFantasia());
			toReturn.put("isMatriz", Boolean.FALSE);
		}
		return toReturn;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getReajusteClienteService().findPaginated(criteria);
		List<TypedFlatMap> lista = rsp.getList();
		if (lista != null && !lista.isEmpty()) {
			for (int i = 0; i < lista.size(); i++) {
				TypedFlatMap reajusteCliente = lista.get(i);
				Integer nrVersao = reajusteCliente.getInteger("tipoTabelaPreco.nrVersao");
				String tpTipoTabelaPreco = reajusteCliente.getString("tipoTabelaPreco.tpTipoTabelaPreco.value");
				String tpSubtipoTabelaPreco = reajusteCliente.getString("subtipoTabelaPreco.tpSubtipoTabelaPreco");
				if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
					String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
					reajusteCliente.put("tabelaPrecoString", tabelaPrecoString);
				}

				String sgFilial = reajusteCliente.getString("filial.sgFilial");
				String nrReajuste = ""+reajusteCliente.getLong("nrReajuste");
				reajusteCliente.put("nrReajuste", SimulacaoUtils.formatNrSimulacao(sgFilial, nrReajuste));
			}
		}
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getReajusteClienteService().getRowCount(criteria);
	}

	public ReajusteCliente findByIdPadrao(Long idReajusteCliente) {
		return (ReajusteCliente)super.findById(idReajusteCliente);
	}

	public TypedFlatMap findById(Long idReajusteCliente) {
		TypedFlatMap reajusteCliente = getReajusteClienteService().findDadosById(idReajusteCliente);
		reajusteCliente.put("idReajusteCliente", idReajusteCliente);

		/** Tabela de Preço */
		String tpTipoTabelaPreco = reajusteCliente.getString("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco.value");
		String tpSubtipoTabelaPreco = reajusteCliente.getString("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco");
		Integer nrVersao = reajusteCliente.getInteger("tabelaPreco.tipoTabelaPreco.nrVersao");
		if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
			String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
			reajusteCliente.put("tabelaPreco.tabelaPrecoString", tabelaPrecoString);
		}

		Boolean blEfetivada = reajusteCliente.getBoolean("blEfetivado");
		if (!Boolean.TRUE.equals(blEfetivada)) {
			reajusteCliente.put("blEfetivado", "N");
		} else {
			reajusteCliente.put("blEfetivado", "S");
		}

		String sgFilial = reajusteCliente.getString("filial.sgFilial");
		String nrReajuste = reajusteCliente.getLong("nrReajuste").toString();
		reajusteCliente.put("nrReajuste", SimulacaoUtils.formatNrSimulacao(sgFilial, nrReajuste));

		String nrIdentificacao = reajusteCliente.getString("cliente.pessoa.nrIdentificacao");
		String tpIdentificacao = reajusteCliente.getString("cliente.pessoa.tpIdentificacao.value");
		reajusteCliente.put("cliente.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));

		Boolean usuarioPerfilComercialParametrizacao = getReajusteClienteService().validateUsuarioPerfilComercialParametrizacao(SessionUtils.getUsuarioLogado());
		
		reajusteCliente.put("usuarioPerfilComercialParametrizacao", Boolean.TRUE.equals(usuarioPerfilComercialParametrizacao) ? "S" : "N");

		reajusteCliente = getValueBoolean("blReajustaPercTde", reajusteCliente);
		reajusteCliente = getValueBoolean("blReajustaPercTrt", reajusteCliente);
		reajusteCliente = getValueBoolean("blReajustaPercGris", reajusteCliente);
		reajusteCliente = getValueBoolean("blReajustaAdValorEm", reajusteCliente);
		reajusteCliente = getValueBoolean("blReajustaAdValorEm2", reajusteCliente);
		reajusteCliente = getValueBoolean("blReajustaFretePercentual", reajusteCliente);
		reajusteCliente = getValueBoolean("blGerarSomenteMarcados", reajusteCliente);
		
		return reajusteCliente;
	}

	private TypedFlatMap getValueBoolean(String key, TypedFlatMap map) {
		Boolean blParam = map.getBoolean(key);
		
		if (blParam == null || !Boolean.TRUE.equals(blParam)) {
			map.put(key, "N");
		} else {
			map.put(key, "S");
		}
		
		return map;
	}

	public void removeById(Long id) {
		getReajusteClienteService().removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getReajusteClienteService().removeByIds(ids);
	}

	public Serializable store(TypedFlatMap data) {
		ReajusteCliente reajusteCliente = new ReajusteCliente();
		Long idReajusteCliente = data.getLong("idReajusteCliente");
		if(idReajusteCliente != null) {
			reajusteCliente = this.findByIdPadrao(idReajusteCliente);
		} else {
			reajusteCliente.setIdReajusteCliente(idReajusteCliente);
			reajusteCliente.setFilial(SessionUtils.getFilialSessao());
		}

		Cliente cliente = new Cliente();
		cliente.setIdCliente(data.getLong("cliente.idCliente"));
		reajusteCliente.setCliente(cliente);

		DivisaoCliente divisaoCliente = new DivisaoCliente();
		divisaoCliente.setIdDivisaoCliente(data.getLong("divisaoCliente.idDivisaoCliente"));
		reajusteCliente.setDivisaoCliente(divisaoCliente);

		TabelaDivisaoCliente tabelaDivisaoCliente = new TabelaDivisaoCliente();
		tabelaDivisaoCliente.setIdTabelaDivisaoCliente(data.getLong("tabelaDivisaoCliente.idTabelaDivisaoCliente"));
		reajusteCliente.setTabelaDivisaoCliente(tabelaDivisaoCliente);

		TabelaPreco tabelaPreco = new TabelaPreco();
		tabelaPreco.setIdTabelaPreco(data.getLong("tabelaPreco.idTabelaPreco"));
		reajusteCliente.setTabelaPreco(tabelaPreco);

		reajusteCliente.setDtInicioVigencia(data.getYearMonthDay("dtInicioVigencia"));
		reajusteCliente.setDsJustificativa(data.getString("dsJustificativa"));
		reajusteCliente.setBlEfetivado(data.getBoolean("blEfetivado"));
		reajusteCliente.setPcReajusteSugerido(data.getBigDecimal("pcReajusteSugerido"));
		reajusteCliente.setPcReajusteAcordado(data.getBigDecimal("pcReajusteAcordado"));
		reajusteCliente.setBlReajustaPercTde(data.getBoolean("blReajustaPercTde"));
		reajusteCliente.setBlReajustaPercTrt(data.getBoolean("blReajustaPercTrt"));
		reajusteCliente.setBlReajustaPercGris(data.getBoolean("blReajustaPercGris"));
		reajusteCliente.setBlReajustaAdValorEm(data.getBoolean("blReajustaAdValorEm"));
		reajusteCliente.setBlReajustaAdValorEm2(data.getBoolean("blReajustaAdValorEm2"));
		reajusteCliente.setBlReajustaFretePercentual(data.getBoolean("blReajustaFretePercentual"));
		reajusteCliente.setBlGerarSomenteMarcados(data.getBoolean("blGerarSomenteMarcados"));

		/** Store ReajusteCliente */
		getReajusteClienteService().storeReajusteCliente(reajusteCliente);

		/** Cria mapa de retorno */
		TypedFlatMap result = configureDataToReturn(reajusteCliente);
		result.put("idReajusteCliente", reajusteCliente.getIdReajusteCliente());

		String sgFilial = SessionUtils.getFilialSessao().getSgFilial();
		result.put("nrReajuste", SimulacaoUtils.formatNrSimulacao(sgFilial, reajusteCliente.getNrReajuste().toString()));
		return result;
	}

	/**
	 * Cria Mapa de retorno
	 * @param reajusteCliente
	 * @return
	 */
	private TypedFlatMap configureDataToReturn(ReajusteCliente reajusteCliente) {
		TypedFlatMap result = new TypedFlatMap();
		if(reajusteCliente.getTpSituacaoAprovacao() != null) {
			result.put("tpSituacaoAprovacao.description", reajusteCliente.getTpSituacaoAprovacao().getDescription());
			result.put("tpSituacaoAprovacao.value", reajusteCliente.getTpSituacaoAprovacao().getValue());
		}
		if(reajusteCliente.getPendenciaAprovacao() != null) {
			result.put("pendenciaAprovacao.idPendencia", reajusteCliente.getPendenciaAprovacao().getIdPendencia());
		}
		result.put("blEfetivado", reajusteCliente.getBlEfetivado());
		return result;
	}

	/**
	 * Gera Aprovação ou Pendencia do Reajuste
	 * @param idReajusteCliente
	 * @return
	 */
	public TypedFlatMap storePendenciaAprovacao(Long idReajusteCliente) {
		ReajusteCliente reajusteCliente = findByIdPadrao(idReajusteCliente);
		getReajusteClienteService().storePendenciaAprovacao(reajusteCliente);

		return configureDataToReturn(reajusteCliente);
	}

	public TypedFlatMap efetuarReajusteCliente(Long idReajusteCliente) {
		ReajusteCliente reajusteCliente = reajusteTabelaClienteService.executeEfetuarReajusteCliente(idReajusteCliente);

		return configureDataToReturn(reajusteCliente);
	}

	public TypedFlatMap agendarReajusteCliente(TypedFlatMap criteria) {
		Long idReajusteCliente = criteria.getLong("idReajusteCliente");
		YearMonthDay dtInicioVigencia = criteria.getYearMonthDay("dtInicioVigencia");

		reajusteTabelaClienteService.executeAgendamentoJobEfetuarReajusteCliente(idReajusteCliente, dtInicioVigencia);
		
		ReajusteCliente reajusteCliente = findByIdPadrao(idReajusteCliente);
		return configureDataToReturn(reajusteCliente);
	}

	public TypedFlatMap calculaPcReajusteSugerido(TypedFlatMap criteria) {
		String tpTipoTabelaPreco = criteria.getString("tpTipoTabelaPreco");
		Integer nrVersao = criteria.getInteger("nrVersao");
		Long idTabelaPrecoOrigem = criteria.getLong("idTabelaPreco");

		BigDecimal pcReajusteSugerido = BigDecimal.ZERO;
		List<TabelaPreco> result = tabelaPrecoService.findTabelaPrecoByNrVersao(tpTipoTabelaPreco, nrVersao, idTabelaPrecoOrigem);
		for (TabelaPreco tabelaPreco : result) {
			if(BigDecimalUtils.hasValue(tabelaPreco.getPcReajuste())) {
				if(!BigDecimalUtils.hasValue(pcReajusteSugerido)) {
					pcReajusteSugerido = tabelaPreco.getPcReajuste().divide(BigDecimalUtils.HUNDRED).add(BigDecimal.ONE);
				} else {
					pcReajusteSugerido = BigDecimalUtils.acrescimo(pcReajusteSugerido, tabelaPreco.getPcReajuste());
				}
			}
		}

		TypedFlatMap toReturn = new TypedFlatMap();
		if(BigDecimalUtils.gtZero(pcReajusteSugerido)) {
			pcReajusteSugerido = pcReajusteSugerido.add(BigDecimalUtils.getBigDecimal(-1)).multiply(BigDecimalUtils.HUNDRED);
		}
		toReturn.put("pcReajusteSugerido", pcReajusteSugerido);
		return toReturn;
	}

	/**
	 * Consulta a filial pela sigla informada 
	 * @param map
	 * @return
	 */
	public List findFilialLookup(Map<String, Object> map) {
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

	public List findTabelaPrecoLookup(Map criteria) {
		FilterList filter = new FilterList(tabelaPrecoService.findLookup(criteria)) {
			public Map<String, Object> filterItem(Object item) {
				TabelaPreco tabelaPreco = (TabelaPreco)item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idTabelaPreco", tabelaPreco.getIdTabelaPreco());
				typedFlatMap.put("tabelaPrecoString", tabelaPreco.getTabelaPrecoString()); 
				typedFlatMap.put("dsDescricao", tabelaPreco.getDsDescricao());
				typedFlatMap.put("pcReajuste", BigDecimalUtils.defaultBigDecimal(tabelaPreco.getPcReajuste()));

				typedFlatMap.put("tipoTabelaPreco.nrVersao", tabelaPreco.getTipoTabelaPreco().getNrVersao());
				typedFlatMap.put("tipoTabelaPreco.tpTipoTabelaPreco", tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco());
				typedFlatMap.put("subtipoTabelaPreco.tpSubtipoTabelaPreco", tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
				return typedFlatMap;
			}
		};
		return (List)filter.doFilter();
	}

	public List findTabelaDivisaoCliente(TypedFlatMap criteria) {
		Long idReajuste = criteria.getLong("idReajusteCliente");
		Boolean blEfetivado = criteria.getBoolean("blEfetivado");
		
		if(LongUtils.hasValue(idReajuste) && Boolean.TRUE.equals(blEfetivado)){
			List listHistoricoTabela = findHistoricoReajusteClienteByIdDivisaoCliente(criteria);
			if(!listHistoricoTabela.isEmpty()) {
				return listHistoricoTabela;
		}
		}
		
		return findTabelaDivisaoClienteByIdDivisaoClienteComboDetail(criteria);
	}
	
	public List findHistoricoReajusteClienteByIdDivisaoCliente(TypedFlatMap criteria) {
		
		List<HistoricoReajusteCliente> list = historicoReajusteClienteService.findHistoricoReajusteClienteByIdDivisaoCliente(criteria.getLong("divisaoCliente.idDivisaoCliente"));
		
		if(list.isEmpty()){
			return list;
		} else {
		return populateFieldTabela(list.get(0).getTabelaPrecoAnterior(), list.get(0).getTabelaDivisaoCliente().getIdTabelaDivisaoCliente());
	}	
	}	
	
	private List populateFieldTabela(TabelaPreco tabelaPreco, Long idTabelaDivisaoCliente) {
		List toReturn = new ArrayList();
		
		String aux = tabelaPreco.getTabelaPrecoString();
		if(tabelaPreco.getDsDescricao()!=null && !"".equals(tabelaPreco.getDsDescricao())) {
			aux = aux + " - " + tabelaPreco.getDsDescricao();
		}

		Map tdcMap = new HashMap();
		tdcMap.put("idTabelaDivisaoCliente",idTabelaDivisaoCliente);
		// TabelaPreco
		Map tpMap = new HashMap();
		tpMap.put("idTabelaPreco",tabelaPreco.getIdTabelaPreco());
		tpMap.put("tabelaPrecoStringDescricao",aux);
		// SubTipoTabelaPreco
		Map stpMap = new HashMap();
		stpMap.put("idSubtipoTabelaPreco",tabelaPreco.getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());
		stpMap.put("tpSubtipoTabelaPreco",tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
		tpMap.put("subtipoTabelaPreco",stpMap);
		// TipoTabelaPreco
		Map ttpMap = new HashMap();
		ttpMap.put("tpTipoTabelaPreco",tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco());
		ttpMap.put("nrVersao",tabelaPreco.getTipoTabelaPreco().getNrVersao());
		tpMap.put("tipoTabelaPreco",ttpMap);

		tdcMap.put("tabelaPreco",tpMap);
		toReturn.add(tdcMap);
		
		return toReturn;		
	}
	
	public List findTabelaDivisaoClienteCombo(TypedFlatMap criteria) {
		List toReturn = new ArrayList();
		List<TabelaDivisaoCliente> list = tabelaDivisaoClienteService.findTabelaDivisaoClienteComboByDivisaoWithServico(criteria.getLong("divisaoCliente.idDivisaoCliente"));
		for (TabelaDivisaoCliente tdc : list) {
			String aux = tdc.getTabelaPreco().getTabelaPrecoString();
			if(tdc.getTabelaPreco().getDsDescricao()!=null && !"".equals(tdc.getTabelaPreco().getDsDescricao())) {
				aux = aux + " - " + tdc.getTabelaPreco().getDsDescricao();
			}
			if(tdc.getServico()!=null && tdc.getServico().getDsServico()!=null && !"".equals(tdc.getServico().getDsServico().toString())) {
				aux = aux + " - " + tdc.getServico().getDsServico();
			}
			Map tdcMap = new HashMap();
			tdcMap.put("idTabelaDivisaoCliente",tdc.getIdTabelaDivisaoCliente());
			// TabelaPreco
			Map tpMap = new HashMap();
			tpMap.put("idTabelaPreco",tdc.getTabelaPreco().getIdTabelaPreco());
			tpMap.put("tabelaPrecoStringDescricao",aux);
			// SubTipoTabelaPreco
			Map stpMap = new HashMap();
			stpMap.put("idSubtipoTabelaPreco",tdc.getTabelaPreco().getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());
			stpMap.put("tpSubtipoTabelaPreco",tdc.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
			tpMap.put("subtipoTabelaPreco",stpMap);
			// TipoTabelaPreco
			Map ttpMap = new HashMap();
			ttpMap.put("tpTipoTabelaPreco",tdc.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco());
			ttpMap.put("nrVersao",tdc.getTabelaPreco().getTipoTabelaPreco().getNrVersao());
			tpMap.put("tipoTabelaPreco",ttpMap);

			tdcMap.put("tabelaPreco",tpMap);
			toReturn.add(tdcMap);
		}
		return toReturn;
	}

	public List findTabelaDivisaoClienteByIdDivisaoClienteComboDetail(TypedFlatMap criteria) {
		List<TabelaDivisaoCliente> list = tabelaDivisaoClienteService.findTabelaDivisaoClienteByIdDivisaoClienteComboDetail(criteria.getLong("divisaoCliente.idDivisaoCliente"));
		return populateFieldTabela(list.get(0).getTabelaPreco(), list.get(0).getIdTabelaDivisaoCliente());
	}

	public List findClienteLookup(TypedFlatMap criteria) {
		return clienteService.findLookupCliente(criteria.getString("pessoa.nrIdentificacao"));
	}

	public List findDivisaoClienteCombo(TypedFlatMap criteria){
		return divisaoClienteService.findLookupDivisoesCliente(
			 criteria.getLong("cliente.idCliente")
			,ConstantesVendas.SITUACAO_ATIVO);
	}

	public void setReajusteClienteService(ReajusteClienteService serviceService) {
		super.defaultService = serviceService;
	}
	public ReajusteClienteService getReajusteClienteService() {
		return (ReajusteClienteService) super.defaultService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public void setReajusteTabelaClienteService(
			ReajusteTabelaClienteService reajusteTabelaClienteService) {
		this.reajusteTabelaClienteService = reajusteTabelaClienteService;
	}

	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
	
	public void setHistoricoReajusteClienteService(HistoricoReajusteClienteService historicoReajusteClienteService) {
		this.historicoReajusteClienteService = historicoReajusteClienteService;
	}
	
}