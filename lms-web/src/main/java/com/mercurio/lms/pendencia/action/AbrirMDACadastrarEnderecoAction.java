package com.mercurio.lms.pendencia.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.service.CepService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoLogradouroService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.pendencia.abrirMDACadastrarEnderecoAction"
 */

public class AbrirMDACadastrarEnderecoAction extends CrudAction {
	private TipoLogradouroService tipoLogradouroService;
	private MunicipioService municipioService;
	private PaisService paisService;
	private CepService cepService;
	private PessoaService pessoaService;

	public void setService(EnderecoPessoaService enderecoPessoaService) {
		this.defaultService = enderecoPessoaService; 
	}
    public void removeById(java.lang.Long id) {
        ((EnderecoPessoaService)defaultService).removeById(id);
    }

	public Serializable store(Map mapBean) {
		// Popula bean EnderecoPessoa
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		enderecoPessoa.setNrCep((String) mapBean.get("nrCep"));
		enderecoPessoa.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
		enderecoPessoa.setDsEndereco((String) mapBean.get("dsEndereco"));
		
		if(mapBean.get("nrEndereco") != null){
			enderecoPessoa.setNrEndereco(mapBean.get("nrEndereco").toString().trim());
		}
		enderecoPessoa.setDsComplemento((String) mapBean.get("dsComplemento"));
		enderecoPessoa.setDsBairro((String) mapBean.get("dsBairro"));		
		
		Map mapMunicipio = (HashMap) mapBean.get("municipio");
		Municipio municipio = this.getMunicipioService().findById(Long.valueOf((String) mapMunicipio.get("idMunicipio")));
		enderecoPessoa.setMunicipio(municipio);
		
		Map mapTipoLogradouro = (HashMap) mapBean.get("tipoLogradouro");
		TipoLogradouro tipoLogradouro = this.getTipoLogradouroService().findById(Long.valueOf((String) mapTipoLogradouro.get("idTipoLogradouro")));
		enderecoPessoa.setTipoLogradouro(tipoLogradouro);
		
		Map mapPessoa = (HashMap) mapBean.get("pessoa");
		Pessoa pessoa = this.getPessoaService().findById(Long.valueOf((String) mapPessoa.get("idPessoa")));
		enderecoPessoa.setPessoa(pessoa);
		
		// Popula bean TipoEnderecoPessoa
		TipoEnderecoPessoa tipoEnderecoPessoa = new TipoEnderecoPessoa();
		tipoEnderecoPessoa.setTpEndereco(new DomainValue("MDA"));
		
		// Popula bean TelefoneEndereco
		TelefoneEndereco telefoneEndereco = new TelefoneEndereco();
		telefoneEndereco.setTpTelefone(new DomainValue((String) mapBean.get("tipoTelefone")));
		telefoneEndereco.setTpUso(new DomainValue("FO"));
		telefoneEndereco.setNrDdd((String) mapBean.get("nrDdd"));
		telefoneEndereco.setNrTelefone((String) mapBean.get("nrTelefone"));
		telefoneEndereco.setPessoa(pessoa);
				
		return ((EnderecoPessoaService)defaultService).storeCompleto(enderecoPessoa, tipoEnderecoPessoa, telefoneEndereco);
	}    
	
	/**
	 * Formata o número de identificação de acordo com o tipo de identificação. 
	 * @param parameters
	 * @return
	 */
	public String formataNrIdentificacao(TypedFlatMap parameters) {				
		return FormatUtils.formatIdentificacao(parameters.getString("tpIdentificacao"), parameters.getString("nrIdentificacao"));
	}
	
    
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((EnderecoPessoaService)defaultService).removeByIds(ids);
    }

    public EnderecoPessoa findById(java.lang.Long id) {
    	return ((EnderecoPessoaService)defaultService).findById(id);
    }
        
	public Map findPaisUsuarioLogado() {		
    	return getPaisService().findPaisUsuarioLogado();
	}    
    
	public List findLookupPais(Map criteria) {
		return this.getPaisService().findLookup(criteria);
	}
	
	public List findLookupCep(Map criteria) {
		return this.getCepService().findLookup(criteria);
	}	
    
	public List findLookupMunicipio(Map criteria) {
		return this.getMunicipioService().findLookup(criteria);
	}    
    
	public List findTipoLogradouro(Map criteria) {		
		return this.getTipoLogradouroService().find(criteria);
	}
	

	public TipoLogradouroService getTipoLogradouroService() {
		return tipoLogradouroService;
	}
	public void setTipoLogradouroService(TipoLogradouroService tipoLogradouroService) {
		this.tipoLogradouroService = tipoLogradouroService;
	}
	public MunicipioService getMunicipioService() {
		return municipioService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public PaisService getPaisService() {
		return paisService;
	}
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	public CepService getCepService() {
		return cepService;
	}
	public void setCepService(CepService cepService) {
		this.cepService = cepService;
	}
	public PessoaService getPessoaService() {
		return pessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}	    

}