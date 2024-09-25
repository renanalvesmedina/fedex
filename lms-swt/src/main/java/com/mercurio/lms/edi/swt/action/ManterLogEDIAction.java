package com.mercurio.lms.edi.swt.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.edi.model.LogEDI;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialEmbarcadoraService;
import com.mercurio.lms.edi.model.service.LogEDIService;
import com.mercurio.lms.edi.model.service.LogEDIComplementoService;
import com.mercurio.lms.edi.model.service.LogEDIDetalheService;
import com.mercurio.lms.edi.model.service.LogEDIItemService;
import com.mercurio.lms.edi.model.service.LogEDIVolumeService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;

/**
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.swt.manterLogEDIAction"
 */
public class ManterLogEDIAction {

	private LogEDIService logEDIService;
	private FilialService filialService;
	private ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService;
	private LogEDIDetalheService logEDIDetalheService;
	private LogEDIComplementoService logEDIComplementoService;
	private LogEDIVolumeService logEDIVolumeService;
	private LogEDIItemService logEDIItemService;
	private ClienteService clienteService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	
	public List<Map<String, Object>> findLookupCliente(Map criteria) {
		List<Map<String, Object>> clientes = clienteEDIFilialEmbarcadoraService.findClienteByNrIdentificacao((String) criteria.get("nrIdentificacao"));
		if (clientes != null) {
			for(Map cliente : clientes) {
				cliente.remove("tpCliente");
				Map pessoa = (Map) cliente.remove("pessoa");
				if (pessoa != null) {
					cliente.put("nmPessoa", pessoa.get("nmPessoa"));
					cliente.put("nrIdentificacaoFormatado", pessoa.remove("nrIdentificacaoFormatado"));
				}
			}
		}
		return clientes;
	}
	
	public ResultSetPage<Map<String, Object>> findPaginatedLog(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindCriteria(criteria);
		ResultSetPage rsp = this.logEDIService.findPaginatedLog(tfmCriteria);
		List<LogEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());		
		for(LogEDI log : list){
 			retorno.add(this.getMapped(log));
		}
		rsp.setList(retorno);
		return rsp;		
	}
	public Integer getRowCountLog(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindCriteria(criteria);
		return this.logEDIService.getRowCountLog(tfmCriteria);
	}
	private Map<String, Object> getMapped(LogEDI log){
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("idLog", log.getIdLogEdi());		
		param.put("data", log.getData());
		param.put("observacao", log.getObservacao());
		param.put("horaInicio", log.getHoraInicio());
		param.put("horaFim", log.getHoraFim());
		param.put("status", log.getStatus());
		param.put("nome", log.getNome());
		if(log.getClienteEDIFilialEmbarcadora() != null){
			ClienteEDIFilialEmbarcadora clienteEDIFilialEmbarcadora = this.clienteEDIFilialEmbarcadoraService.findById(log.getClienteEDIFilialEmbarcadora().getIdClienteEDIFilialEmbarcadora());
			Cliente cliente = clienteService.findById(clienteEDIFilialEmbarcadora.getClienteEmbarcador().getIdCliente());
			param.put("nomeCliente",cliente.getPessoa().getNmPessoa());
		}
		if(log.getFilial() != null){
			Filial filial = this.filialService.findById(log.getFilial().getIdFilial());
			param.put("nomeFilial",filial.getSgFilial());			
		}	
		
		if(log.getStatus() != null && !"Status Arquivado".equalsIgnoreCase(log.getStatus()) && !"Status Não Processado".equalsIgnoreCase(log.getStatus())){
			param.put("erro", true);
			param.put("todas", true);
		}
		
		return param;
	}

	private TypedFlatMap createFindCriteria(Map criteria) {		
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("idFilial", criteria.get("idFilial"));
		tfmCriteria.put("idClienteEDIFilialEmbarcadora", criteria.get("idClienteEDIFilialEmbarcadora"));
		tfmCriteria.put("nrNotaFiscal", criteria.get("nrNotaFiscal"));
		if(criteria.get("dataInicio") != null){
			tfmCriteria.put("dataInicio", criteria.get("dataInicio").toString());
		}
		if(criteria.get("dataFim") != null){
			tfmCriteria.put("dataFim", criteria.get("dataFim").toString());
		}
		tfmCriteria.put("idEmpresa", criteria.get("idEmpresa"));
		tfmCriteria.put("filialMatriz", criteria.get("filialMatriz"));
    	tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
    	tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
    	tfmCriteria.put("_order", criteria.get("_order"));
    	if(criteria.get("cliente") != null){
    		tfmCriteria.put("nro_doc_cliente", PessoaUtils.clearIdentificacao(criteria.get("cliente").toString()));
    	}
    	if(criteria.get("idInformacaoDoctoCliente") != null){
    	    tfmCriteria.put("idInformacaoDoctoCliente", criteria.get("idInformacaoDoctoCliente").toString());
        }
    	if(criteria.get("dsCampo") != null){
    	    tfmCriteria.put("dsCampo", criteria.get("dsCampo").toString());
        }
    	if(criteria.get("valorComplemento") != null){
            tfmCriteria.put("valorComplemento", criteria.get("valorComplemento").toString());
        }
    	return tfmCriteria;
	}

	
	public List findLookupFilialByUsuarioLogado(TypedFlatMap map) {
		List listFilial = filialService.findLookupByUsuarioLogado(map);
		if ( listFilial.isEmpty() ) {
			return listFilial;
		}
		
		List resultList = new ArrayList();
		
		Iterator iterator = listFilial.iterator();
		Map filial = (Map)iterator.next();
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		typedFlatMap.put("idFilial", filial.get("idFilial") );
		typedFlatMap.put("sgFilial", filial.get("sgFilial") );
		typedFlatMap.put("nmFantasia", filial.get("pessoa.nmFantasia") );
		resultList.add(typedFlatMap);
		
		return resultList;
	}

    public List<Map<String, Object>> findComboDoctoCliente(TypedFlatMap criteria){
        criteria.put("cliente.idCliente", criteria.remove("idCliente"));
        List<InformacaoDoctoCliente> informacoes = informacaoDoctoClienteService.find(criteria);
        if (informacoes != null) {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (InformacaoDoctoCliente doctoCliente : informacoes) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("idInformacaoDoctoCliente", doctoCliente.getIdInformacaoDoctoCliente());
                map.put("dsCampo", doctoCliente.getDsCampo());
                map.put("tpCampo", doctoCliente.getTpCampo().getValue());
                map.put("dsFormatacao", doctoCliente.getDsFormatacao());
                map.put("nrTamanho", doctoCliente.getNrTamanho());
                map.put("blOpcional", doctoCliente.getBlOpcional());
                map.put("dsValorPadrao", doctoCliente.getDsValorPadrao());
                map.put("blValorFixo", doctoCliente.getBlValorFixo());
                result.add(map);
            }
            return result;
        }
        return null;
    }

	public FilialService getFilialService() {
		return filialService;
	}
	public LogEDIService getLogEDIService() {
		return logEDIService;
	}
	public void setLogEDIService(LogEDIService logEDIService) {
		this.logEDIService = logEDIService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public ClienteEDIFilialEmbarcadoraService getClienteEDIFilialEmbarcadoraService() {
		return clienteEDIFilialEmbarcadoraService;
	}
	public void setClienteEDIFilialEmbarcadoraService(ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService) {
		this.clienteEDIFilialEmbarcadoraService = clienteEDIFilialEmbarcadoraService;
	}
	public LogEDIDetalheService getLogEDIDetalheService() {
		return logEDIDetalheService;
	}
	public void setLogEDIDetalheService(LogEDIDetalheService logEDIDetalheService) {
		this.logEDIDetalheService = logEDIDetalheService;
	}
	public LogEDIComplementoService getLogEDIComplementoService() {
		return logEDIComplementoService;
	}
	public void setLogEDIComplementoService(LogEDIComplementoService logEDIComplementoService) {
		this.logEDIComplementoService = logEDIComplementoService;
	}
	public LogEDIVolumeService getLogEDIVolumeService() {
		return logEDIVolumeService;
	}
	public void setLogEDIVolumeService(LogEDIVolumeService logEDIVolumeService) {
		this.logEDIVolumeService = logEDIVolumeService;
	}
	public LogEDIItemService getLogEDIItemService() {
		return logEDIItemService;
	}
	public void setLogEDIItemService(LogEDIItemService logEDIItemService) {
		this.logEDIItemService = logEDIItemService;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
        this.informacaoDoctoClienteService = informacaoDoctoClienteService;
    }
}
