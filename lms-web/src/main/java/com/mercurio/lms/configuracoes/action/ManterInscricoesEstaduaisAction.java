package com.mercurio.lms.configuracoes.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.manterInscricoesEstaduaisAction"
 */

public class ManterInscricoesEstaduaisAction extends CrudAction {
	
    private ClienteService clienteService;

	public void setInscricaoEstadual(InscricaoEstadualService inscricaoEstadualService) {
		this.defaultService = inscricaoEstadualService;
	}
	
	public InscricaoEstadualService getInscricaoEstadualService(){
		return ((InscricaoEstadualService)defaultService);
	}
	
    public void removeById(java.lang.Long id) {
        ((InscricaoEstadualService)defaultService).removeById(id);
    }
    
    /**
     * Implementa��o para validar a regra que somente uma
     * Inscri��o Estadual pode ser a "Inscri��o Padr�o"
     * 
     * Verifica��es na tabela TIPO_TRIBUTACAO_IE
     * 
     * @param bean InscricaoEstadual
     * @param isentoInscricao Indica que o chackbox isento de inscri��o foi ou n�o marcado na tela
     * @return Mensagem LMS-27064 - Aviso de tributa��es cadastradas com vig�ncia futura
     */
    public Serializable store(TypedFlatMap tfm) {
    	
    	InscricaoEstadual ie = new InscricaoEstadual();
    	
    	ie.setIdInscricaoEstadual(tfm.getLong("idInscricaoEstadual"));
    	Pessoa p = new Pessoa();
    	
    	p.setIdPessoa(tfm.getLong("pessoa.idPessoa"));
    	
    	Map<String, Object> ufPessoa = getInscricaoEstadualService().findUnidadeFederativa(p.getIdPessoa());
    	if(!ufPessoa.isEmpty() && ufPessoa.get("endereco") != null){
    		String idUfInscri = tfm.getString("unidadeFederativa.idUnidadeFederativa");
    		String idUfPessoa = String.valueOf(((Map)ufPessoa.get("endereco")).get("idUnidadeFederativa"));    		
    		if(!idUfInscri.equals(idUfPessoa)){
    			throw new BusinessException("LMS-27099");
    		}
    	}
    	    	
    	ie.setPessoa(p);
    	
    	UnidadeFederativa uf = new UnidadeFederativa();
    	uf.setIdUnidadeFederativa(tfm.getLong("unidadeFederativa.idUnidadeFederativa"));
    	
    	ie.setBlIndicadorPadrao(tfm.getBoolean("blIndicadorPadrao"));
    	ie.setUnidadeFederativa(uf);
    	
    	ie.setNrInscricaoEstadual(tfm.getString("nrInscricaoEstadual"));
    	ie.setTpSituacao(tfm.getDomainValue("tpSituacao"));
    	
    	return getInscricaoEstadualService().store(ie);
    }
    
    public void validateBlAtualizacaoCountasse(TypedFlatMap tfm){
    	getInscricaoEstadualService().validateBlAtualizacaoCountasse(tfm.getLong("idPessoa"));
    }
    
    /**
     * M�todo chamado ao clicar no bot�o Limpar (novo)
     * Busca a Unidade Federativa da pessoa
     * @param inscricaoEstadual
     * @return
     */
    public Map getInitialValue(TypedFlatMap tfm){    	
    	
    	Map retorno = getInscricaoEstadualService().findUnidadeFederativa(tfm.getLong("pessoa.idPessoa"));
    	
        Cliente cliente = clienteService.findById(tfm.getLong("pessoa.idPessoa"));
        Long idRamoAtividade = cliente.getRamoAtividade().getIdRamoAtividade();

    	if( !retorno.containsKey("endereco") ){
    		retorno.put("possuiEndereco",Boolean.FALSE);
    	} else {
    		retorno.put("possuiEndereco",Boolean.TRUE);
    	}
    	
    	retorno.put("enableBlIndicadorPadrao",Boolean.valueOf(getInscricaoEstadualService().findIndicadorPadrao(tfm.getLong("idInscricaoEstadual"),tfm.getLong("pessoa.idPessoa"))));
        retorno.put("idRamoAtividade", idRamoAtividade);
    	
    	return retorno;    	
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((InscricaoEstadualService)defaultService).removeByIds(ids);
    }

    public TypedFlatMap findById(java.lang.Long id) {
    	
    	TypedFlatMap retorno = null;
    	
    	InscricaoEstadual ie = ((InscricaoEstadualService)defaultService).findById(id);
    	
    	if( ie != null ){
    		
    		retorno = new TypedFlatMap();
    		retorno.put("idInscricaoEstadual",ie.getIdInscricaoEstadual());
    		retorno.put("unidadeFederativa.idUnidadeFederativa",ie.getUnidadeFederativa().getIdUnidadeFederativa());
    		retorno.put("unidadeFederativa.sgUnidadeFederativa",ie.getUnidadeFederativa().getSgUnidadeFederativa());
    		retorno.put("unidadeFederativa.nmUnidadeFederativa",ie.getUnidadeFederativa().getNmUnidadeFederativa());
    		retorno.put("nrInscricaoEstadual",ie.getNrInscricaoEstadual());
    		retorno.put("tpSituacao.description",ie.getTpSituacao().getDescription().getValue());
    		retorno.put("tpSituacao.value",ie.getTpSituacao().getValue());
    		retorno.put("tpSituacao.id",ie.getTpSituacao().getId());
    		retorno.put("enableBlIndicadorPadrao",Boolean.valueOf(((InscricaoEstadualService)defaultService).findIndicadorPadrao(ie.getIdInscricaoEstadual(),ie.getPessoa().getIdPessoa())));
    		retorno.put("blIndicadorPadrao",ie.getBlIndicadorPadrao());
    		
    	}
    	
    	if(getInscricaoEstadualService().perfilMatrizUsuarioLogado()){
    		retorno.put("permiteManutencaoUF",Boolean.TRUE);
    	}    	
    	
    	return retorno;
    }

    public ClienteService getClienteService() {
       return clienteService;
    }
    
    public void setClienteService(ClienteService clienteService) {
       this.clienteService = clienteService;
    }


}
