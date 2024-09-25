package com.mercurio.lms.entrega.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.entrega.report.EmitirEficienciaEntregaClienteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.emitirEficienciaEntregaClienteAction"
 */
public class EmitirEficienciaEntregaClienteAction extends ReportActionSupport {
	
	public void setEmitirEficienciaEntregaClienteService(
			EmitirEficienciaEntregaClienteService emitirEficienciaEntregaClienteService) {
		this.reportServiceSupport = emitirEficienciaEntregaClienteService;
	}

	private ClienteService clienteService;
	private UnidadeFederativaService unidadeFederativaService;
	private ContatoService contatoService;
	private FilialService filialService;
	private EnderecoPessoaService enderecoPessoaService;
	
	@Override
	public java.io.File execute(TypedFlatMap parameters) throws Exception {
		return super.execute(parameters);
	}

	/**
	 * M�todo respons�vel por requisitar informa��es do usu�rio logado.
	 * @return
	 */
	public TypedFlatMap findInfoUsuarioLogado() {
		Filial f = SessionUtils.getFilialSessao();
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("filial.idFilial",f.getIdFilial());
		retorno.put("filial.sgFilial",f.getSgFilial());
		Pessoa pessoa = f.getPessoa();
		retorno.put("filial.pessoa.nmFantasia",pessoa.getNmFantasia());
		    	
		EnderecoPessoa ep = pessoa.getEnderecoPessoa();
		if (ep != null) {
			ep = enderecoPessoaService.findEnderecoPessoaPadrao(pessoa.getIdPessoa());
		}
		if (ep != null) {
			UnidadeFederativa uf = ep.getMunicipio().getUnidadeFederativa();
			retorno.put("unidadeFederativa.idUnidadeFederativa",uf.getIdUnidadeFederativa());
			retorno.put("unidadeFederativa.sgUnidadeFederativa",uf.getSgUnidadeFederativa());
			retorno.put("unidadeFederativa.nmUnidadeFederativa",uf.getNmUnidadeFederativa());
		}
		
		return retorno;
	}
	
	/**
     * findLookup de Unidades Federativas.
     * @param criteria
     * @return
     */
	public List findLookupUF(TypedFlatMap criteria){
		return unidadeFederativaService.findLookup(criteria);
	}

	/**
     * findLookup de Clientes.
     * @param criteria
     * @return
     */
	public List findLookupCliente(TypedFlatMap criteria){
		List clientes = clienteService.findLookupSimplificado(criteria.getString("pessoa.nrIdentificacao"),null);

		List retorno = new ArrayList();
		Iterator iter = clientes.iterator();	
		while (iter.hasNext()) {	
			Cliente cliente = (Cliente) iter.next();
			Pessoa pessoa = cliente.getPessoa();
			
			TypedFlatMap map = new TypedFlatMap();
			map.put("idCliente",cliente.getIdCliente());
			map.put("pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
			map.put("pessoa.nmPessoa",pessoa.getNmPessoa());
			map.put("pessoa.nrIdentificacaoFormatado", 
					FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao()));
			retorno.add(map);
			
			// Se o retorno j� � dois, n�o h� porqu� continuar, j� que a lookup vai desconsiderar o retorno.
			if (retorno.size() == 2)
				break;
		}
			
		return retorno;
	}
	
	/**
     * findLookup de filiais.
     * @param criteria
     * @return
     */
    public List findLookupFilial(TypedFlatMap criteria) {
		return filialService.findLookupAsPaginated(criteria);
	}

    /**
     * find da combo de contatos.
     * @param criteria
     * @return
     */
    public List findComboContato(TypedFlatMap criterions){
		return contatoService.findComboContatos(criterions);
	}
    
    
	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
}
