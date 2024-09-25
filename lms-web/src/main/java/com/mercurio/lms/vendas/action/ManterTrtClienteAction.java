package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.TrtCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.MunicipioTrtClienteService;
import com.mercurio.lms.vendas.model.service.TrtClienteService;

public class ManterTrtClienteAction extends CrudAction {
	
	private TrtClienteService trtClienteService;
	private ConfiguracoesFacade configuracoesFacade;
	private ClienteService clienteService;
	private MunicipioTrtClienteService municipioTrtClienteService; 
	private MunicipioFilialService municipioFilialService;
	private MunicipioService municipioService;
	private PessoaService pessoaService;
	
	public void setTrtCliente(TrtClienteService trtClienteService) {
		this.defaultService = trtClienteService;
	}

    public TrtCliente findById(java.lang.Long id) {
    	return ((TrtClienteService)defaultService).findById(id);
    }      
    
    public TypedFlatMap findByIdDetalhado(java.lang.Long id){
    	TypedFlatMap retorno = new TypedFlatMap();
    	TrtCliente trtCliente = trtClienteService.findById(id);
    	
    	if(trtCliente.getCliente() != null){
    	   	Pessoa pessoa = pessoaService.findById(trtCliente.getCliente().getIdCliente());   	
    	   	retorno.put("cliente.idCliente", trtCliente.getCliente().getIdCliente());
    	   	retorno.put("cliente.pessoa.nmPessoa", pessoa.getNmPessoa());		
    	   	retorno.put("cliente.pessoa.nrIdentificacao", pessoa.getNrIdentificacao());	
    	   	retorno.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pessoa));
    	}
    	
    	retorno.put("idTabelaPreco", trtCliente.getIdTabelaPreco());
		retorno.put("dtVigenciaInicial", JTDateTimeUtils.getNullMaxYearMonthDay(trtCliente.getDtVigenciaInicial()));
		retorno.put("dtVigenciaFinal", JTDateTimeUtils.getNullMaxYearMonthDay(trtCliente.getDtVigenciaFinal()));
		retorno.put("dtVigenciaInicialSolicitada", trtCliente.getDtVigenciaInicialSolicitada());
		retorno.put("dtVigenciaFinalSolicitada", JTDateTimeUtils.getNullMaxYearMonthDay(trtCliente.getDtVigenciaFinalSolicitada()));
		
		if(trtCliente.getTpSituacaoAprovacao() != null){
			retorno.put("tpSituacaoAprovacao", trtCliente.getTpSituacaoAprovacao().getValue());
		}
		
		retorno.put("municipioCobraTRT", findMunicipioTrtClienteByIdTrtCliente(id));
		retorno.put("possuiFilhoEmAprovacao", trtClienteService.validateRegistroFilhoEmAprovacao(id));
		retorno.put("idTrtCliente", trtCliente.getIdTrtCliente());
    	return retorno;
    }
    
    private List<TypedFlatMap> findMunicipioTrtClienteByIdTrtCliente(Long id){
		List<Map<String, Object>> listMapTemp = municipioTrtClienteService.findMunicipioTrtClienteByIdTrtCliente(id);

		List<TypedFlatMap> listMunicipios = new ArrayList<TypedFlatMap>();
		for (Map<String, Object> map : listMapTemp) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idMunicipioTrtCliente", map.get("idMunicipioTrtCliente"));
			tfm.put("municipio.idMunicipio", map.get("idMunicipio"));
			tfm.put("municipio.nmMunicipioComUF", map.get("nmMunicipioComUF"));
			tfm.put("blCobraTrt.value", map.get("blCobraTrt"));
			tfm.put("blCobraTrt.description", (Boolean) map.get("blCobraTrt") ? configuracoesFacade.getMensagem("sim") : configuracoesFacade.getMensagem("nao"));
			listMunicipios.add(tfm);
		}
		
		return listMunicipios;
    }
    
    public Serializable store(TypedFlatMap bean) {
		return municipioTrtClienteService.store(bean);
    }
    
    public void validateTrtCliente(TypedFlatMap map){
    	if(map.getLong("idTabelaPreco") != null){
    		trtClienteService.validateTrtIdTabela(map);
    	} else {
    		trtClienteService.validateTrtCliente(map);
    	}
    }
    
    public void validateDtVigenciaInicial(TypedFlatMap criteria){
    	YearMonthDay dtVigenciaInicialSolicitada = criteria.getYearMonthDay("dtVigenciaInicialSolicitada");
    	if(dtVigenciaInicialSolicitada != null){
    	
	    	if (dtVigenciaInicialSolicitada.isBefore(JTDateTimeUtils.getDataAtual())){
	    		throw new BusinessException("LMS-00006");    		
	    	}
	    	
	    	if (isDataInvalida(dtVigenciaInicialSolicitada, criteria.getYearMonthDay("dtVigenciaInicial"))){
	    		throw new BusinessException("LMS-29174");    		
	    	}
	    	
	    	if (isDataInvalida(dtVigenciaInicialSolicitada, criteria.getYearMonthDay("dtVigenciaFinal"))){
	    		throw new BusinessException("LMS-01259");    		
	    	}
    	}
    }

    private Boolean isDataInvalida(YearMonthDay dtVigenciaInicialSolicitada, YearMonthDay dtVigencia){
    	return dtVigencia != null && (dtVigenciaInicialSolicitada.isBefore(dtVigencia) || dtVigenciaInicialSolicitada.isEqual(dtVigencia));
    }
    
    public void validateDtVigenciaFinal(TypedFlatMap criteria){
    	YearMonthDay dtVigenciaFinalSolicitada = criteria.getYearMonthDay("dtVigenciaFinalSolicitada");
    	YearMonthDay dtVigenciaInicialSolicitada = criteria.getYearMonthDay("dtVigenciaInicialSolicitada");
    	if (
    		(dtVigenciaFinalSolicitada != null && dtVigenciaFinalSolicitada.isBefore(JTDateTimeUtils.getDataAtual()) 
    			&& dtVigenciaInicialSolicitada != null && dtVigenciaInicialSolicitada.isBefore(dtVigenciaFinalSolicitada))
    			|| dtVigenciaInicialSolicitada == null && dtVigenciaFinalSolicitada != null && dtVigenciaFinalSolicitada.isBefore(JTDateTimeUtils.getDataAtual())){
    		throw new BusinessException("LMS-00007");    		
    	}
    }
    
    public void removeById(Long id){
    	municipioTrtClienteService.removeById(id);
    }
    
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	municipioTrtClienteService.removeByIds(ids);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List findListMunicipiosComUF(){
    	List municipios = getMunicipioFilialService().findListMunicipiosFilialComUF();
    	List<Map<String, Object>> retorno = new ArrayList();
    	for(Iterator ie = municipios.iterator(); ie.hasNext();) {
    		Object[] projections = (Object[])ie.next();
    		Map map = new HashMap();
    		map.put("idMunicipio",((Long)projections[0]).toString());
    		map.put("nmMunicipioComUF",projections[1].toString() + " - " + projections[2].toString());
    		retorno.add(map);
    	}
		return retorno;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List findLookupCliente(Map criteria){
    	List clientes = this.getClienteService().findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = clientes.iterator(); iter.hasNext();) {
			Cliente cliente = (Cliente) iter.next();
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("idCliente", cliente.getIdCliente());
			typedFlatMap.put("tpCliente", cliente.getTpCliente().getValue());
			typedFlatMap.put("pessoa.nmPessoa", cliente.getPessoa().getNmPessoa());
			typedFlatMap.put("pessoa.nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
			typedFlatMap.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa()));
			retorno.add(typedFlatMap);
		}
    	return retorno;
    }
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findMunicipiosComRestricaoTransporte(TypedFlatMap criteria) {
    	List<Object[]> list = municipioFilialService.findMunicipiosComRestricaoTransporte(criteria);
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		for (Object[] obj : list) {
			TypedFlatMap map = new TypedFlatMap();
			map.put("municipio.idMunicipio", obj[0]);
			map.put("municipio.nmMunicipioComUF", obj[1] + " - " + obj[3]);
			map.put("blCobraTrt.value", obj[2]);
			map.put("blCobraTrt.description", (Boolean) obj[2] ? configuracoesFacade.getMensagem("sim") : configuracoesFacade.getMensagem("nao"));
			newList.add(map);
		}
		return newList;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = trtClienteService.findPaginated(criteria);
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		List<Object[]> list = rsp.getList();
		for (Object[] obj : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idTrtCliente", obj[0]);
			map.put("dtVigenciaInicial", getDataFormatada((YearMonthDay) obj[1]));
			map.put("dtVigenciaFinal", getDataFormatada((YearMonthDay) obj[2]));
			map.put("dtVigenciaInicialSolicitada", getDataFormatada((YearMonthDay) obj[3]));
			map.put("dtVigenciaFinalSolicitada", getDataFormatada((YearMonthDay) obj[4]));
			map.put("tpSituacaoAprovacao", obj[5]);
			newList.add(map);
		}
		rsp.setList(newList);
		return rsp;
	}

	private String getDataFormatada(YearMonthDay data) {
		String dataFormatada = null;
		if (!JTDateTimeUtils.MAX_YEARMONTHDAY.equals(data)) {
			return JTDateTimeUtils.formatDateYearMonthDayToString(data);
		}
		return dataFormatada;
	}
    
	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public TrtClienteService getTrtClienteService() {
		return trtClienteService;
	}

	public void setTrtClienteService(TrtClienteService trtClienteService) {
		this.trtClienteService = trtClienteService;
	}

	public MunicipioTrtClienteService getMunicipioTrtClienteService() {
		return municipioTrtClienteService;
	}

	public void setMunicipioTrtClienteService(
			MunicipioTrtClienteService municipioTrtClienteService) {
		this.municipioTrtClienteService = municipioTrtClienteService;
	}

	public MunicipioFilialService getMunicipioFilialService() {
		return municipioFilialService;
	}

	public void setMunicipioFilialService(
			MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}  
}
