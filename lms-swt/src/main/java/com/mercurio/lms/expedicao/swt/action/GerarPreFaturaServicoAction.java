package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.MotivoPreFaturaServico;
import com.mercurio.lms.expedicao.model.OrdemServicoItem;
import com.mercurio.lms.expedicao.model.PreFaturaServico;
import com.mercurio.lms.expedicao.model.PreFaturaServicoItem;
import com.mercurio.lms.expedicao.model.ServicoGeracaoAutomatica;
import com.mercurio.lms.expedicao.model.service.MotivoPreFaturaServicoService;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoItemService;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class GerarPreFaturaServicoAction extends CrudAction {
	public static final DomainValue TP_SITUACAO_INFORMADO = new DomainValue("I");
	private ClienteService clienteService;
	private FilialService filialService;
	private PreFaturaServicoService preFaturaServicoService;
	private PreFaturaServicoItemService preFaturaServicoItemService;
	private MotivoPreFaturaServicoService motivoPreFaturaServicoService;

	@SuppressWarnings("unchecked")
	public TypedFlatMap storePreFatura(TypedFlatMap bean) {
		PreFaturaServico preFaturaServico = new PreFaturaServico();
		
		Boolean isReprovada = bean.getBoolean("isReprovada") == null || bean.getBoolean("isReprovada") == false? false: true;
		
		if (bean.getLong("idDivisaoCliente") != null) {
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente(bean.getLong("idDivisaoCliente"));
			preFaturaServico.setDivisaoCliente(divisaoCliente);
		}
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(bean.getLong("idCliente"));
		
		Filial filialCobranca = filialService.findById(bean.getLong("idFilialCobranca"));		
		
		preFaturaServico.setClienteTomador(cliente);
		preFaturaServico.setFilialCobranca(filialCobranca);
		preFaturaServico.setVlTotal(bean.getBigDecimal("vlTotal"));
		
		List<TypedFlatMap> itensMapped = bean.getList("itens");
		List<PreFaturaServicoItem> itens = new ArrayList<PreFaturaServicoItem>();
		
		MotivoPreFaturaServico motivoPreFaturaServico = null;
		
		if (isReprovada) {
			try {
				UsuarioLMS responsavelFinalizacao = new UsuarioLMS();
				responsavelFinalizacao.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
				preFaturaServico.setUsuarioFinalizacao(responsavelFinalizacao);
				motivoPreFaturaServico = getMotivoPreFaturaServicoService().findByDsMotivoPreFaturaServico();
			} catch (Exception e) {
				throw new BusinessException("LMS-04560");
			}
		}
		
		for (TypedFlatMap itemMapped : itensMapped) {
			PreFaturaServicoItem item = new PreFaturaServicoItem();
			item.setPreFaturaServico(preFaturaServico);
			
			if (isReprovada) {
				item.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_SITUACAO_REPROVADA));
				item.setMotivoPreFaturaServico(motivoPreFaturaServico);
			} else {
				item.setTpSituacao(TP_SITUACAO_INFORMADO);
			}
			
			if ("OS".equals(itemMapped.getString("tpDocumento"))) {
				validarPreFaturaServicoItemOrdemServicoExiste(itemMapped.getLong("idItem"));
				OrdemServicoItem osItem = new OrdemServicoItem();
				osItem.setIdOrdemServicoItem(itemMapped.getLong("idItem"));
				item.setOrdemServicoItem(osItem);
			} else {
				validarPreFaturaServicoItemServicoGeracaoAutomaticaExiste(itemMapped.getLong("idItem"));
				ServicoGeracaoAutomatica sga = new ServicoGeracaoAutomatica();
				sga.setIdServicoGeracaoAutomatica(itemMapped.getLong("idItem"));
				item.setServicoGeracaoAutomatica(sga);
			}			
			itens.add(item);
		}
		
		preFaturaServico.setPreFaturaServicoItens(itens);
		
		if (isReprovada) {
			preFaturaServico.setTpSituacao(new DomainValue("REPROVADA"));preFaturaServico.getTpSituacao();
		}
		
		preFaturaServicoService.storeGerarPreFatura(preFaturaServico);
		
		String nrPreFatura = preFaturaServico.getFilialCobranca().getSgFilial() + " " + 
				FormatUtils.formataNrDocumento(String.valueOf(preFaturaServico.getNrPreFatura()), "PFS");
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("nrPreFatura", nrPreFatura);
		return retorno;
	}

	private void validarPreFaturaServicoItemServicoGeracaoAutomaticaExiste(Long idServicoGeracaoAutomatica) {
		if (preFaturaServicoItemService.executeExistsPreFaturaServicoItemByServicoGeracaoAutomatica(
				idServicoGeracaoAutomatica, TP_SITUACAO_INFORMADO)) {
			throw new BusinessException("LMS-04560");
		}

		if (preFaturaServicoItemService.executeExistsPreFaturaServicoItemByServicoGeracaoAutomatica(
				idServicoGeracaoAutomatica, new DomainValue("R"))) {
			throw new BusinessException("LMSA-04560");
		}
	}

	private void validarPreFaturaServicoItemOrdemServicoExiste(Long idOrdemServicoItem) {
		if (preFaturaServicoItemService.executeExistsPreFaturaServicoItemByOrdemServicoItem(idOrdemServicoItem, TP_SITUACAO_INFORMADO)) {
			throw new BusinessException("LMS-04560");
		}
		
		if (preFaturaServicoItemService.executeExistsPreFaturaServicoItemByOrdemServicoItem(idOrdemServicoItem, new DomainValue("R"))) {
			throw new BusinessException("LMS-04560");
		}
	}

	public TypedFlatMap findByIdClienteTomador(Long id) {		
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		Cliente cliente = clienteService.findById(id);
		typedFlatMap.put("idCliente", cliente.getIdCliente());
		if (cliente.getPessoa() != null) {
			typedFlatMap.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
			typedFlatMap.put("nmPessoa", cliente.getPessoa().getNmPessoa());
		}
		return typedFlatMap;
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
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultSetPage<Map<String, Object>> findPaginated(Map criteria) {
		return preFaturaServicoService.findPaginatedGeracaoPreFatura(new PaginatedQuery(criteria));
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return preFaturaServicoService.getRowCountGeracaoPreFatura(criteria);
	}
	
	public ResultSetPage<Map<String, Object>> findPaginatedGeracaoPreFaturaDetalhamento(Map<String, Object> criteria) {
		return preFaturaServicoService.findPaginatedGeracaoPreFaturaDetalhamento(new PaginatedQuery(criteria));
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public PreFaturaServicoService getPreFaturaServicoService() {
		return preFaturaServicoService;
	}

	public void setPreFaturaServicoService(
			PreFaturaServicoService preFaturaServicoService) {
		this.preFaturaServicoService = preFaturaServicoService;
	}

	public void setPreFaturaServicoItemService(PreFaturaServicoItemService preFaturaServicoItemService) {
		this.preFaturaServicoItemService = preFaturaServicoItemService;
	}

	public MotivoPreFaturaServicoService getMotivoPreFaturaServicoService() {
		return motivoPreFaturaServicoService;
	}

	public void setMotivoPreFaturaServicoService(MotivoPreFaturaServicoService motivoPreFaturaServicoService) {
		this.motivoPreFaturaServicoService = motivoPreFaturaServicoService;
	}
}
