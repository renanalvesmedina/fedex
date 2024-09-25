package com.mercurio.lms.sim.action;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneContatoService;
import com.mercurio.lms.sim.model.ConfiguracaoComunicacao;
import com.mercurio.lms.sim.model.ContatoComunicacao;
import com.mercurio.lms.sim.model.service.ConfiguracaoComunicacaoService;
import com.mercurio.lms.sim.model.service.ContatoComunicacaoService;
import com.mercurio.lms.util.JTVigenciaUtils;


/**
 * Generated by: ADSM ActionGenerator 
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sim.manterContatosRecebemInformacoesEventosAction"
 */

public class ManterContatosRecebemInformacoesEventosAction extends CrudAction {
	
	public void setService(ContatoComunicacaoService contatoComunicacaoService) {
		this.defaultService = contatoComunicacaoService;
	}
	private ContatoComunicacaoService contatoComunicacaoService;
	private ContatoService contatoService;
	private TelefoneContatoService telefoneContatoService;
	private ConfiguracaoComunicacaoService configuracaoComunicacaoService;
	private PessoaService pessoaService;
	
	
	// Faz a pesquisa na tela de listagem
    public List findPaginatedCustom(TypedFlatMap criteria){

    	ResultSetPage rs = contatoComunicacaoService.findPaginatedCustom(criteria);
    	List listaRetorno = new ArrayList();
    	List listaResultadoPesquisa = new ArrayList();
    	listaResultadoPesquisa = rs.getList();
    	
    	for (int i = 0; i < listaResultadoPesquisa.size(); i++) {
    		TypedFlatMap tfm = new TypedFlatMap();	
    		ContatoComunicacao contatoComunicacao = (ContatoComunicacao)listaResultadoPesquisa.get(i);

    		tfm.put("contato",contatoComunicacao.getContato().getNmContato());
	    	tfm.put("mail",contatoComunicacao.getContato().getDsEmail());
	    	tfm.put("telefone",contatoComunicacao.getTelefoneContato().getTelefoneEndereco().getDddTelefone()	);
	    	tfm.put("vigenciaFinal",contatoComunicacao.getDtVigenciaFinal());
	    	tfm.put("vigenciaInicial",contatoComunicacao.getDtVigenciaInicial());
	    	
	    	tfm.put("idContatoComunicacao",contatoComunicacao.getIdContatoComunicacao());	
	    	listaRetorno.add(tfm);
    	}

    	
    	return listaRetorno;
    }
	// Salva os dados da p�gina de detalhamento
	public TypedFlatMap storeCustom(TypedFlatMap map) {
        TypedFlatMap retorno = new TypedFlatMap();
        ContatoComunicacao contatoComunicacao = new ContatoComunicacao();
        
        // Seta idContatoComunicacao
        if ( map.getLong("idContatoComunicacao") != null ){
        	contatoComunicacao.setIdContatoComunicacao(map.getLong("idContatoComunicacao"));
        }
        
        // Salva o contato
        Contato contato = contatoService.findById(map.getLong("contato"));
        contatoComunicacao.setContato(contato);
        // Salva Telefone contato
        TelefoneContato telefoneContato = telefoneContatoService.findById(map.getLong("telefone"));
        contatoComunicacao.setTelefoneContato(telefoneContato);

        if (map.getLong("idConfiguracaoComunicacao") != null){ 
        	ConfiguracaoComunicacao configuracaoComunicacao = configuracaoComunicacaoService.findById(map.getLong("idConfiguracaoComunicacao"));
        	contatoComunicacao.setConfiguracaoComunicacao(configuracaoComunicacao);
        }
        
        contatoComunicacao.setDtVigenciaFinal(map.getYearMonthDay("dataFim"));
        contatoComunicacao.setDtVigenciaInicial(map.getYearMonthDay("dataInicio"));
        contatoComunicacao.setTpMeioTransmissao(map.getDomainValue("meioTransmissao"));
        
        Long id =(Long) contatoComunicacaoService.store(contatoComunicacao);
        // Retorno para tela 
        retorno.put("tpIdentificacao",map.getDomainValue("tpIdentificacao"));
        retorno.put("idContatoComunicacao",id );
        
        retorno.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(contatoComunicacao));

        return retorno;
    }
	
    /**
     * �damo B. Azambuja
     * Este m�todo retorna apenas um registro de contato para este cliente.
     * Usado na tela de detalhamento
     */
    public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
    	
    	ResultSetPage rs = ((ContatoComunicacaoService)defaultService).findByIdDetalhamento(id);
    	List listaRetorno = new ArrayList();
    	TypedFlatMap tfm = new TypedFlatMap();
    	if (rs != null){
    		// pega uma configuracao da lista de resultado
    		ContatoComunicacao cc = (ContatoComunicacao)rs.getList().get(0);

    		tfm.put("telefone",cc.getTelefoneContato().getIdTelefoneContato());
			tfm.put("contato",cc.getContato().getIdContato());
    		tfm.put("nmContato",cc.getContato().getNmContato());
    		tfm.put("dsEmail",cc.getContato().getDsEmail());
    		tfm.put("email",cc.getContato().getDsEmail());
    		tfm.put("dataFim",cc.getDtVigenciaFinal());
    		tfm.put("dataInicio",cc.getDtVigenciaInicial());
    		
    		tfm.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(cc));
    		tfm.put("idContatoComunicacao",cc.getIdContatoComunicacao());
			listaRetorno.add(tfm);
    	}
    	return tfm;
    }
    
    /**
     * �damo B. Azambuja 
     * Este m�todo carrega o combo de telefones do contato na tela de detalhamento 
     * */
    public List findComboTelefonesContato(TypedFlatMap criteria){
 
    	List listaResultado = telefoneContatoService.findTelefoneByContato(criteria);
    	List listaRetorno = new ArrayList();
    	for (int i = 0; i < listaResultado.size(); i++) {
    		TypedFlatMap tfm = new TypedFlatMap();
    		
    		TelefoneContato telefoneContato = (TelefoneContato)listaResultado.get(i);
    		tfm.put("idTelefoneContato",telefoneContato.getIdTelefoneContato());
    		tfm.put("dsTelefoneContato",telefoneContato.getTelefoneEndereco().getDddTelefone());
    		
    		listaRetorno.add(tfm);
		}
    	return listaRetorno;
    }

    
    
    /**
     * �damo B. Azambuja
     * Busca o n�mero de registros para paginar a tela de listagem 
     * */
    public Integer getRowCount(TypedFlatMap criteria){
    	return Integer.valueOf(findPaginatedCustom(criteria).size()); 
    }
    
    /**
     * Find de contatos do cliente.
     */
    public List findComboContatosCliente(TypedFlatMap criteria) {
    	criteria.put("pessoa.idPessoa", criteria.getLong("cliente.idCliente"));
    	List lista = contatoService.find(criteria);
    	
    	List listaRetorno = new ArrayList();
    	for (int i = 0; i < lista.size(); i++) {
    		TypedFlatMap tfm = new TypedFlatMap();
    		Contato contato = (Contato) lista.get(i);
    		tfm.put("idContato", contato.getIdContato());
    		tfm.put("nmContato", contato.getNmContato());
    		tfm.put("dsEmail", contato.getDsEmail());
			listaRetorno.add(tfm);
		}
		return listaRetorno;
		
	}
	public void removeById(Long id) {
		// TODO Auto-generated method stub
		contatoComunicacaoService.removeById(id);
	}
	public void removeByIds(List ids) {
		// TODO Auto-generated method stub
		contatoComunicacaoService.removeByIds(ids);
	}
	public ContatoService getContatoService() {
		return contatoService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	public ContatoComunicacaoService getContatoComunicacaoService() {
		return contatoComunicacaoService;
	}

	public void setContatoComunicacaoService(
			ContatoComunicacaoService contatoComunicacaoService) {
		this.contatoComunicacaoService = contatoComunicacaoService;
	}
	public TelefoneContatoService getTelefoneContatoService() {
		return telefoneContatoService;
	}
	public void setTelefoneContatoService(
			TelefoneContatoService telefoneContatoService) {
		this.telefoneContatoService = telefoneContatoService;
	}
	public ConfiguracaoComunicacaoService getConfiguracaoComunicacaoService() {
		return configuracaoComunicacaoService;
	}
	public void setConfiguracaoComunicacaoService(
			ConfiguracaoComunicacaoService configuracaoComunicacaoService) {
		this.configuracaoComunicacaoService = configuracaoComunicacaoService;
	}
	public PessoaService getPessoaService() {
		return pessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
 
}
