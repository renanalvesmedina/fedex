package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.sgr.model.ClienteEnquadramento;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.dao.ClienteEnquadramentoDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sgr.clienteEnquadramentoService"
 */
public class ClienteEnquadramentoService extends CrudService<ClienteEnquadramento, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ClienteEnquadramento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public ClienteEnquadramento findById(java.lang.Long id) {
        return (ClienteEnquadramento)super.findById(id);
    }
	
	/**
	 * Procura todos os municipios de destino atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findClientesById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List clientesEnquadramentos = super.find(criteria);

		// LMS-LMS-7253 - busca relacionamentos de cliente com PGR
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		for (Object object : clientesEnquadramentos) {
			ClienteEnquadramento clienteEnquadramento = (ClienteEnquadramento) object;
			Cliente cliente = clienteEnquadramento.getCliente();
			Pessoa pessoa = cliente.getPessoa();
			TypedFlatMap map = new TypedFlatMap();
			map.put("idClienteEnquadramento", clienteEnquadramento.getIdClienteEnquadramento());
			map.put("cliente.idCliente", cliente.getIdCliente());
			map.put("nrIdentificacao", FormatUtils.formatIdentificacao(pessoa));
			map.put("cliente.pessoa.nmPessoa", pessoa.getNmPessoa());
			map.put("tpIntegranteFrete", clienteEnquadramento.getTpIntegranteFrete());
			list.add(map);
		}
		return list;
	}

	/**
	 * Apaga uma entidade atrav�s do IdEnquadramentoRegra
	 *
	 * @param id indica o enquadramentoRegra
	 */
    public void removeByIdEnquadramentoRegra(java.lang.Long id) {
        Map criteria = new HashMap();
 		criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);		
    	List clientesEnquadramentos = find(criteria);
    	if (clientesEnquadramentos != null && clientesEnquadramentos.size() >0) {
    		List ids = new ArrayList();
    		for (Iterator iterator = clientesEnquadramentos.iterator(); iterator.hasNext();) {
    			ClienteEnquadramento clienteEnquadramento = (ClienteEnquadramento) iterator.next();
				ids.add(clienteEnquadramento.getIdClienteEnquadramento());
    	}
     		removeByIds(ids);    		
    }
    }
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 */
    public void storeClientes(List<ClienteEnquadramento> clienteEnquadramentos, EnquadramentoRegra bean) {
    	removeByIdEnquadramentoRegra(bean.getIdEnquadramentoRegra());
		if (clienteEnquadramentos != null && clienteEnquadramentos.size() > 0) {
			// LMS-7253 - processa relacionamento com ClienteEnquadramento
			for (ClienteEnquadramento clienteEnquadramento : clienteEnquadramentos) {
				clienteEnquadramento.setIdClienteEnquadramento(null);
				// trata mapeamento de tpIntegranteFrete.value substituindo por DomainValue correto
				String value = clienteEnquadramento.getTpIntegranteFrete().getValue();
				DomainValue domainValue = new DomainValue(value.replaceAll(".*value=(.).*", "$1"));
				clienteEnquadramento.setTpIntegranteFrete(domainValue);
			}
			storeAll(clienteEnquadramentos);
 		}
    }
    
	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ClienteEnquadramento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setClienteEnquadramentoDAO(ClienteEnquadramentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ClienteEnquadramentoDAO getClienteEnquadramentoDAO() {
        return (ClienteEnquadramentoDAO) getDao();
    }
   }