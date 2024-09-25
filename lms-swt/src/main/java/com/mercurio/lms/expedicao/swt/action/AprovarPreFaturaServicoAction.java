package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.MotivoPreFaturaServico;
import com.mercurio.lms.expedicao.model.OrdemServicoAnexo;
import com.mercurio.lms.expedicao.model.service.MotivoPreFaturaServicoService;
import com.mercurio.lms.expedicao.model.service.NFEConjugadaService;
import com.mercurio.lms.expedicao.model.service.OrdemServicoAnexoService;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoItemService;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoService;
import com.mercurio.lms.expedicao.report.EmitirPreFaturaServicoAnaliticoService;
import com.mercurio.lms.expedicao.report.EmitirPreFaturaServicoPlanilhaAnaliticoService;
import com.mercurio.lms.expedicao.report.EmitirPreFaturaServicoSinteticoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class AprovarPreFaturaServicoAction extends CrudAction {
	private ClienteService clienteService;
	private EmitirPreFaturaServicoPlanilhaAnaliticoService emitirPreFaturaServicoPlanilhaAnaliticoService;
	private EmitirPreFaturaServicoAnaliticoService emitirPreFaturaServicoAnaliticoService;
	private EmitirPreFaturaServicoSinteticoService emitirPreFaturaServicoSinteticoService ;
	private FilialService filialService;
	private MotivoPreFaturaServicoService motivoPreFaturaServicoService;
	private PessoaService pessoaService;
	private PreFaturaServicoItemService preFaturaServicoItemService;	
	private ReportExecutionManager reportExecutionManager;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private NFEConjugadaService nfeConjugadaService;
	
	// LMS-6538
	private OrdemServicoAnexoService ordemServicoAnexoService;
	
	public String executeReportPlanilhaAnalitica(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(emitirPreFaturaServicoPlanilhaAnaliticoService, parameters);
	}
	
	public String executeReportAnalitico(TypedFlatMap parameters) throws Exception {
		parameters.put("tpRelatorio", ConstantesExpedicao.ANALITICO_ORIGINAL);
		
		return reportExecutionManager.generateReportLocator(emitirPreFaturaServicoAnaliticoService, parameters);
	}
	
	public String executeReportAnaliticoAjustado(TypedFlatMap parameters) throws Exception {
		parameters.put("tpRelatorio", ConstantesExpedicao.ANALITICO_AJUSTADO);
		return reportExecutionManager.generateReportLocator(emitirPreFaturaServicoAnaliticoService, parameters);
	}
	
	public String executeReportSintetico(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(emitirPreFaturaServicoSinteticoService, parameters);
	}
	
	@SuppressWarnings("unchecked")
	public  Map<String, Object>  executeFinalizar(TypedFlatMap criteria) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		Long idPreFaturaServico = criteria.getLong("idPreFaturaServico");
		List<Map<String, Object>> itens = criteria.getList("itens");
		Long idFilialCobranca = criteria.getLong("idFilialCobranca");
		Long idCliente = criteria.getLong("idCliente");
		Long idDivisaoCliente = criteria.getLong("idDivisaoCliente");
		getPreFaturaServicoService().storeAprovarPreFatura(idPreFaturaServico, idFilialCobranca, idCliente, idDivisaoCliente, itens);		
		retorno.put("nfeConjugada", nfeConjugadaService.isAtivaNfeConjugada(SessionUtils.getFilialSessao().getIdFilial()));
		
		return retorno;
		
	}
	
	public Map<String, Object> executeSalvarItem(TypedFlatMap criteria) {
		Map<String, Object> r = new HashMap<String, Object>();
		Long idFilialCobranca = criteria.getLong("idFilialCobranca");
		getPreFaturaServicoService().validateFilial(idFilialCobranca);
		List<Map<String, Object>> preFaturaServicoItens = criteria.getList("itens");
		preFaturaServicoItemService.storePreFaturaServicoItem(preFaturaServicoItens);
		
		return r;
	}
	
	public Map<String, Object> findFilialLogada() {
		Filial filialLogada =  SessionUtils.getFilialSessao();
		Map<String, Object> mapFilialLogada = new HashMap<String, Object>();
		mapFilialLogada.put("sgFilial", filialLogada.getSgFilial());
		mapFilialLogada.put("idFilial", filialLogada.getIdFilial());
		Pessoa pessoa = pessoaService.findById(filialLogada.getPessoa().getIdPessoa());
		mapFilialLogada.put("nmFantasia", pessoa.getNmFantasia());
		return mapFilialLogada;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupFilial(TypedFlatMap criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	public Map<String, Object> validateFilial(TypedFlatMap criteria) {
		Long idFilialCobranca = criteria.getLong("idFilialCobranca");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isFilialValida", getPreFaturaServicoService().validateFilialUsuario(idFilialCobranca));
		return map;
	}
	
	@SuppressWarnings("unchecked" )
	public List<Map<String, Object>> findLookupCliente(Map<String, Object> criteria){
		Map<String, Object> pessoa = new HashMap<String, Object>();
		pessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));
		criteria.put("pessoa", pessoa);
		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();
		List<Cliente> clientes = clienteService.findLookup(criteria);
		for (Cliente cliente : clientes) {
			Map<String, Object> cli = new HashMap<String, Object>(); 
			cli.put("idCliente", cliente.getIdCliente());
			if (cliente.getPessoa() != null) {
				cli.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				cli.put("nmPessoa", cliente.getPessoa().getNmPessoa());
			}
			retorno.add(cli);
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findComboMotivo(Map<String, Object> criteria){
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();
		List<MotivoPreFaturaServico> motivoPreFaturaServicos = motivoPreFaturaServicoService.find(criteria);
		for (MotivoPreFaturaServico motivoPreFaturaServico : motivoPreFaturaServicos) {
			Map<String, Object> cli = new HashMap<String, Object>(); 
			cli.put("idMotivo", motivoPreFaturaServico.getIdMotivoPreFaturaServico());
			cli.put("dsMotivo", motivoPreFaturaServico.getDsMotivoPreFaturaServico());			
			cli.put("tpMotivo", motivoPreFaturaServico.getTpMotivoPreFatura().getValue());
			retorno.add(cli);
		}
		return retorno;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultSetPage<Map<String, Object>> findPaginated(Map criteria) {
		return getPreFaturaServicoService().findPaginated(new PaginatedQuery(criteria));
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getPreFaturaServicoService().getRowCount(criteria);
	}
	
	public ResultSetPage<Map<String, Object>> findPaginatedItens(Map<String, Object> criteria) {
		return preFaturaServicoItemService.findPaginatedAprovacaoPreFaturaItem(new PaginatedQuery(criteria));
	}
	
	/**
	 * LMS-6538 - Busca página de dados para preenchimento de grid de anexos da
	 * página Manter e Aprovar Pré-Fatura.
	 * 
	 * @param parameters
	 *            parâmetros para busca incluindo <tt>idPreFaturaServicoItem</tt>
	 * @return página para preenchimento da grid de anexos
	 */
	public ResultSetPage<TypedFlatMap> findPaginatedAnexos(TypedFlatMap parameters) {
		FindDefinition findDefinition = FindDefinition.createFindDefinition(parameters);
		Long idPreFaturaServicoItem = parameters.getLong("idPreFaturaServicoItem");
		ResultSetPage<OrdemServicoAnexo> page = ordemServicoAnexoService.findByPreFaturaServico(findDefinition, idPreFaturaServicoItem);
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		for (OrdemServicoAnexo ordemServicoAnexo : page.getList()) {
			TypedFlatMap map = new TypedFlatMap();
			map.put("idOrdemServicoAnexo", ordemServicoAnexo.getIdOrdemServicoAnexo());
			map.put("nmParcelaPreco", ordemServicoAnexo.getParcelaPreco().getNmParcelaPreco());
			map.put("nmArquivo", ordemServicoAnexo.getNmArquivo());
			map.put("dsAnexo", ordemServicoAnexo.getDsAnexo());
			map.put("dhInclusao", ordemServicoAnexo.getDhInclusao());
			list.add(map);
		}
		int currentPage = findDefinition.getCurrentPage();
		int pageSize = findDefinition.getPageSize();
		int rowCount = getRowCountAnexos(parameters);
		ResultSetPage<TypedFlatMap> result = new ResultSetPage<TypedFlatMap>(
				currentPage, currentPage > 1, currentPage * pageSize < rowCount, list);
		return result;
	}

	/**
	 * LMS-6538 - Busca quantidade de itens para grid de anexos da página Manter
	 * e Aprovar Pré-Fatura.
	 * 
	 * @param parameters
	 *            parâmetros para busca incluindo <tt>idPreFaturaServicoItem</tt>
	 * @return quantidade de itens para grid de anexos
	 */
	public Integer getRowCountAnexos(TypedFlatMap parameters) {
		Long idPreFaturaServicoItem = parameters.getLong("idPreFaturaServicoItem");
		return ordemServicoAnexoService.findCountByPreFaturaServico(idPreFaturaServicoItem).intValue();
	}

	private PreFaturaServicoService getPreFaturaServicoService() {
		return (PreFaturaServicoService)this.defaultService;
	}
	
	public void setPreFaturaServicoService(PreFaturaServicoService preFaturaServicoService) {
		this.defaultService = preFaturaServicoService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setEmitirPreFaturaServicoService(
			EmitirPreFaturaServicoAnaliticoService emitirPreFaturaServicoService) {
		this.emitirPreFaturaServicoAnaliticoService = emitirPreFaturaServicoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setMotivoPreFaturaServicoService(
			MotivoPreFaturaServicoService motivoPreFaturaServicoService) {
		this.motivoPreFaturaServicoService = motivoPreFaturaServicoService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setPreFaturaServicoItemService(
			PreFaturaServicoItemService preFaturaServicoItemService) {
		this.preFaturaServicoItemService = preFaturaServicoItemService;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setEmitirPreFaturaServicoSinteticoService(
			EmitirPreFaturaServicoSinteticoService emitirPreFaturaServicoSinteticoService) {
		this.emitirPreFaturaServicoSinteticoService = emitirPreFaturaServicoSinteticoService;
	}

	public void setEmitirPreFaturaServicoPlanilhaAnaliticoService(
			EmitirPreFaturaServicoPlanilhaAnaliticoService emitirPreFaturaServicoPlanilhaAnaliticoService) {
		this.emitirPreFaturaServicoPlanilhaAnaliticoService = emitirPreFaturaServicoPlanilhaAnaliticoService;
	}

	public OrdemServicoAnexoService getOrdemServicoAnexoService() {
		return ordemServicoAnexoService;
	}

	public void setOrdemServicoAnexoService(OrdemServicoAnexoService ordemServicoAnexoService) {
		this.ordemServicoAnexoService = ordemServicoAnexoService;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public NFEConjugadaService getNfeConjugadaService() {
		return nfeConjugadaService;
	}

	public void setNfeConjugadaService(NFEConjugadaService nfeConjugadaService) {
		this.nfeConjugadaService = nfeConjugadaService;
	}

}