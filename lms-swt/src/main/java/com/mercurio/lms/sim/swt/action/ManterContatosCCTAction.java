package com.mercurio.lms.sim.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.ContatoCCT;
import com.mercurio.lms.sim.model.EmailCCT;
import com.mercurio.lms.sim.model.service.ContatosCCTService;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class ManterContatosCCTAction extends CrudAction{
	
	private ContatosCCTService contatosCCTService;
	private ClienteService clienteService;

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public ContatosCCTService getContatosCCTService() {
		return contatosCCTService;
	}

	public void setContatosCCTService(ContatosCCTService contatosCCTService) {
		this.contatosCCTService = contatosCCTService;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> findLookupCliente(Map criteria) {
		List<Map<String, Object>> clientes = clienteService.findClienteByNrIdentificacao((String) criteria.get("nrIdentificacao"));
		if (clientes != null) {
			for(Map cliente : clientes) {
				cliente.remove("tpCliente");
				Map pessoa = (Map) cliente.remove("pessoa");
				if (pessoa != null) {
					cliente.put("nmPessoa", pessoa.get("nmPessoa"));
					cliente.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
				}
			}
		}
		return clientes;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage<Map<String, Object>> findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = contatosCCTService.findPaginated(criteria , FindDefinition.createFindDefinition(criteria));
		
		List<HashMap<String, Object>> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(Map<String, Object> hash : list){	
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("idClienteRemetente", hash.get("idClienteRemetente"));
			map.put("nrIdentificacaoRemetente", hash.get("nrIdentificacaoRemetente"));
			map.put("nmPessoaRemetente", hash.get("nmPessoaRemetente"));
			map.put("idClienteDestinatario", hash.get("idClienteDestinatario"));
			map.put("nrIdentificacaoDestinatario", hash.get("nrIdentificacaoDestinatario"));
			map.put("nmPessoaDestinatario", hash.get("nmPessoaDestinatario"));
			map.put("idContatoCCT", hash.get("idContatoCCT"));
			
			DomainValue parametrizacao = (DomainValue) hash.get("tipoParametrizacao");
			
			map.put("tipoParametrizacao", parametrizacao.getDescriptionAsString());
			retorno.add(map);	
		}
		rsp.setList(retorno);
		return rsp;	
	}
	
	public Integer getRowCount(Map parameters) {		
		TypedFlatMap criteria = new TypedFlatMap(parameters);
		return contatosCCTService.getRowCount(criteria);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
		contatosCCTService.removeByIds(ids);
    }
	
	public Map findById(java.lang.Long id) {
		ContatoCCT contatoCCT = (ContatoCCT) contatosCCTService.findById(id);
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("idContatoCCT", contatoCCT.getIdContatoCCT());
		map.put("tipoParametrizacao", contatoCCT.getTpParametrizacao().getValue());
		
		if(contatoCCT.getClienteRemetente() != null){
			map.put("idClienteRemetente", contatoCCT.getClienteRemetente().getIdCliente());
			map.put("nmPessoaRemetente", contatoCCT.getClienteRemetente().getPessoa().getNmPessoa());
			map.put("nrIdentificacaoRemetente", contatoCCT.getClienteRemetente().getPessoa().getNrIdentificacaoFormatado());
		}
		
		if(contatoCCT.getClienteDestinatario() != null){
			map.put("idClienteDestinatario", contatoCCT.getClienteDestinatario().getIdCliente());
			map.put("nmPessoaDestinatario", contatoCCT.getClienteDestinatario().getPessoa().getNmPessoa());
			map.put("nrIdentificacaoDestinatario", contatoCCT.getClienteDestinatario().getPessoa().getNrIdentificacaoFormatado());
		}
		
		List<EmailCCT> emailsCCT = new ArrayList<EmailCCT>();
		emailsCCT.addAll(getContatosCCTService().findEmailsByIdContatoCCT(id));
		map.put("emailsCCT", generateMapFromEmails(emailsCCT));
		
		return map;
    }
	
	private List<Map<String, Object>> generateMapFromEmails(List<EmailCCT> emails) {
		List<Map<String, Object>> emailsCCT = new ArrayList<Map<String,Object>>();
		
		if(emails != null) {
			for(EmailCCT email : emails){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("dsEmail", email.getDsEmail());
				emailsCCT.add(map);
			}
		}
		return emailsCCT;
	}
	
	public Map store(Map map) {
		ContatoCCT contatoCCT = new ContatoCCT();
		contatoCCT.setIdContatoCCT((Long)map.get("idContatoCCT"));
		contatoCCT.setTpParametrizacao(new DomainValue((String) map.get("tipoParametrizacao")));
		
		if(!("").equals(map.get("idClienteRemetente"))){
			Cliente remetente = new Cliente();
			remetente.setIdCliente((Long)map.get("idClienteRemetente"));
			contatoCCT.setClienteRemetente(remetente);
		}
		
		if(!("").equals(map.get("idClienteDestinatario"))){
			Cliente destinatario = new Cliente();
			destinatario.setIdCliente((Long)map.get("idClienteDestinatario"));
			contatoCCT.setClienteDestinatario(destinatario);
		}
		
		contatosCCTService.store(contatoCCT);
		contatosCCTService.removeByIdContatoCCT(contatoCCT.getIdContatoCCT());
		storeEmails(contatoCCT, (List<Map<String, Object>>)map.get("emailsCCT"));
				
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("idContatoCCT", contatoCCT.getIdContatoCCT());
    	return retorno;
    }
	
	private void storeEmails(ContatoCCT contatoCCT, List<Map<String, Object>> emails) {
		for(Map<String, Object> map :  emails){
			EmailCCT emailCCT = new EmailCCT();
			emailCCT.setDsEmail((String)map.get("dsEmail"));
			emailCCT.setContatoCCT(contatoCCT);
			contatosCCTService.storeEmail(emailCCT);
		}
	}

	public void removeById(java.lang.Long id) {
		contatosCCTService.removeById(id);
    }
	
}
