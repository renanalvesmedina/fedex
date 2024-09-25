package com.mercurio.lms.configuracoes.swt.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.FormatUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.swt.manterPessoasAction"
 */

public class ManterPessoasAction extends CrudAction {
	public void setPessoa(PessoaService pessoaService) {
		this.defaultService = pessoaService;
	}
    public void removeById(java.lang.Long id) {
        ((PessoaService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((PessoaService)defaultService).removeByIds(ids);
    }

    public Map findById(java.lang.Long id) {
    	Pessoa pessoa = ((PessoaService)defaultService).findById(id);
    	TypedFlatMap map = new TypedFlatMap();

    	map.put("idPessoa", pessoa.getIdPessoa());
    	map.put("pessoa.idPessoa", pessoa.getIdPessoa());
    	map.put("pessoa.tpPessoa", pessoa.getTpPessoa() != null ? pessoa.getTpPessoa().getValue() : null);
    	map.put("pessoa.tpPessoaTmp", pessoa.getTpPessoa() != null ? pessoa.getTpPessoa().getDescription() : null);    	
    	map.put("pessoa.tpIdentificacao", pessoa.getTpIdentificacao() != null ? pessoa.getTpIdentificacao().getValue() : null);
    	map.put("pessoa.nrIdentificacao", pessoa.getNrIdentificacao() != null ?FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(),pessoa.getNrIdentificacao()) : null);
    	map.put("pessoa.nmPessoa",pessoa.getNmPessoa());
    	map.put("pessoa.dhInclusao",pessoa.getDhInclusao());
    	map.put("pessoa.dsEmail",pessoa.getDsEmail());
    	map.put("pessoa.nrRg",pessoa.getNrRg());
    	map.put("pessoa.dsOrgaoEmissorRg",pessoa.getDsOrgaoEmissorRg());
    	map.put("pessoa.dtEmissaoRg",pessoa.getDtEmissaoRg());

    	return map;
    }
    
    public Serializable store(TypedFlatMap map) {
    	Pessoa pessoa = new Pessoa();
    	if (map.getLong("idPessoa") != null){
    		pessoa = ((PessoaService)defaultService).findById(map.getLong("idPessoa"));
    	}
    	pessoa.setTpIdentificacao(map.getDomainValue("pessoa.tpIdentificacao"));
    	pessoa.setNrIdentificacao(map.getString("pessoa.nrIdentificacao"));
    	pessoa.setNrRg(map.getString("pessoa.nrRg"));
    	pessoa.setDsOrgaoEmissorRg(map.getString("pessoa.dsOrgaoEmissorRg"));
    	pessoa.setDtEmissaoRg(map.getYearMonthDay("pessoa.dtEmissaoRg"));

    	return ((PessoaService)defaultService).storeSimple(pessoa);
    }
    
    public ResultSetPage findPaginated(Map criteria) {
    	Map map = new HashMap();
    	map.put("_pageSize", criteria.get("_pageSize"));
    	map.put("_order", criteria.get("_order"));
    	map.put("_currentPage", criteria.get("_currentPage"));
    	map.put("tpIdentificacao", criteria.get("tpIdentificacao"));
    	map.put("nrIdentificacao", criteria.get("nrIdentificacao"));
    	map.put("tpPessoa", criteria.get("tpPessoa"));
    	map.put("nmPessoa", criteria.get("nmPessoa"));
    	return super.findPaginated(map);
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	Map map = new HashMap();
    	map.put("tpIdentificacao", criteria.get("tpIdentificacao"));
    	map.put("nrIdentificacao", criteria.get("nrIdentificacao"));
    	map.put("tpPessoa", criteria.get("tpPessoa"));
    	map.put("nmPessoa", criteria.get("nmPessoa"));
    	return super.getRowCount(map);
    }
    
	public Pessoa findPessoa(Map map){
		return ((PessoaService)defaultService).findByIdentificacao((String)map.get("tpIdentificacao"), 
																   (String)map.get("nrIdentificacao"));
	}     
}
