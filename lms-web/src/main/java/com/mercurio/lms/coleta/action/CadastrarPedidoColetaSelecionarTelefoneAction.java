package com.mercurio.lms.coleta.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/** 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.cadastrarPedidoColetaSelecionarTelefoneAction"
 */

public class CadastrarPedidoColetaSelecionarTelefoneAction extends CrudAction {
	private ClienteService clienteService;
	private MunicipioService municipioService;
	private ConfiguracoesFacade configuracoesFacade;
	private EnderecoPessoaService enderecoPessoaService;
	

	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.defaultService = telefoneEnderecoService;		
	}
	public TelefoneEnderecoService getTelefoneEnderecoService() {
		return ((TelefoneEnderecoService) this.defaultService);		
	}
	

	public List findLookupCliente(TypedFlatMap tfm) {
		/* 
		 * A m�scara (00.000.000/0000-00) � uma forma de burlar o funcionamento padr�o
		 * da lookup, pois esta n�o executa a consulta sem possuir valor v�lido no criteria property.
		 * Isso � necess�rio quando se necessita buscar apenas pelo id do objeto, sem considerar 
		 * o criteria. Dessa forma, se nrIdentificacao for este, o find ir� desconsiderar o valor. 
		 */ 
		if ("00.000.000/0000-00".equals(tfm.getString("pessoa.nrIdentificacao")))
			tfm.remove("pessoa.nrIdentificacao");
		
		
		List list = getClienteService().findLookup(tfm);
		List newList = new ArrayList();
		for(Iterator i = list.iterator(); i.hasNext(); ) {
			Cliente cliente = (Cliente)i.next();
			TypedFlatMap map = new TypedFlatMap();
			map.put("idCliente", cliente.getIdCliente());
			map.put("pessoa.nmPessoa", cliente.getPessoa().getNmPessoa());
			map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa()));
			newList.add(map);
		}
		return newList;
	}
	
	public List findLookupEnderecoPessoa(TypedFlatMap tfm) {
		//FIXME pode-se usar o inlinequery = false
		/* 
		 * A m�scara (00.000.000/0000-00) � uma forma de burlar o funcionamento padr�o
		 * da lookup, pois esta n�o executa a consulta sem possuir valor v�lido no criteria property.
		 * Isso � necess�rio quando se necessita buscar apenas pelo id do objeto, sem considerar 
		 * o criteria. Dessa forma, se nrIdentificacao for este, o find ir� desconsiderar o valor. 
		 */ 
		if ("00.000.000/0000-00".equals(tfm.getString("pessoa.idPessoa")))
			tfm.remove("pessoa.idPessoa");
		
		List list = getEnderecoPessoaService().findLookup(tfm);
		List newList = new ArrayList();
		for (Iterator i = list.iterator(); i.hasNext(); ) {
			EnderecoPessoa enderecoPessoa = (EnderecoPessoa)i.next();			
			Municipio municipio = municipioService.findById(enderecoPessoa.getMunicipio().getIdMunicipio()); 

			TypedFlatMap map = new TypedFlatMap();
			map.put("dsTipoLogradouro", enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro().getValue());
			map.put("dsComplemento", enderecoPessoa.getDsComplemento());
			map.put("dsEndereco", enderecoPessoa.getDsEndereco());
			map.put("nrEndereco", enderecoPessoa.getNrEndereco());
			map.put("dsBairro", enderecoPessoa.getDsBairro());
			map.put("nrCep", enderecoPessoa.getNrCep());

			map = formataEndereco(map);
			map.put("idEnderecoPessoa", enderecoPessoa.getIdEnderecoPessoa());
			map.put("municipio.nmMunicipio",                           municipio.getNmMunicipio());
			map.put("municipio.unidadeFederativa.sgUnidadeFederativa", municipio.getUnidadeFederativa().getSgUnidadeFederativa());
			newList.add(map);
		}
		return newList;
	}
	
	/**
	 * Recebe o endereco em forma de TypedFlatMap e
	 * formata para mostrar no TextArea
	 * @param tfm
	 * @return
	 */
	public TypedFlatMap formataEndereco(TypedFlatMap tfm) {
		
		String complemento = configuracoesFacade.getMensagem("complemento");
		String bairro = configuracoesFacade.getMensagem("bairro");
		String cep = configuracoesFacade.getMensagem("cep");

		String dsTipoLogradouro = tfm.getString("dsTipoLogradouro");
		String dsComplemento = tfm.getString("dsComplemento");
		String dsEndereco    = tfm.getString("dsEndereco");
		String nrEndereco    = tfm.getString("nrEndereco");
		String dsBairro      = tfm.getString("dsBairro");
		String nrCep         = tfm.getString("nrCep");
		
		StringBuffer endereco = new StringBuffer()
		.append(dsTipoLogradouro==null?"":dsTipoLogradouro+" ")
		.append(dsEndereco)
		.append(", n�: ")
		.append(nrEndereco)
		.append(dsComplemento==null?"":" / "+complemento+": "+dsComplemento)
		.append(dsBairro==null?"":"\n"+bairro+": "+dsBairro)
		.append("\n"+cep+": "+nrCep);

		TypedFlatMap map = new TypedFlatMap();
		map.put("endereco", endereco.toString());
		return map;
	}


	/**
	 * M�todo que busca a pagina��o de telefone endere�o.
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedTelefoneEndereco(TypedFlatMap criteria) {
		return this.getTelefoneEnderecoService().findPaginatedTelefoneEndereco(criteria);
	}

	/**
	 * M�todo que busca a quantidade de telefone endere�o.
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountTelefoneEndereco(TypedFlatMap criteria) {
		return this.getTelefoneEnderecoService().getRowCountTelefoneEndereco(criteria);
	}	
	
	
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
