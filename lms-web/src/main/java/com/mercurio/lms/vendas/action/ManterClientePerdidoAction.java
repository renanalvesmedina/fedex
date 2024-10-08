package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.RamoAtividadeService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.ClientePerdido;
import com.mercurio.lms.vendas.model.service.ClientePerdidoService;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterClientePerdidoAction"
 */
public class ManterClientePerdidoAction extends CrudAction{
	private FilialService filialService;
	private ClienteService clienteService;
	private RegionalFilialService regionalFilialService;
	private EnderecoPessoaService enderecoPessoaService;
	private ConfiguracoesFacade configuracoesFacade;
	private RamoAtividadeService ramoAtividadeService;
	
	public void setRamoAtividadeService(RamoAtividadeService ramoAtividadeService) {
		this.ramoAtividadeService = ramoAtividadeService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}

	public void setService(ClientePerdidoService clientePerdidoService) {
		this.setDefaultService(clientePerdidoService);
	}
	
	public ClientePerdidoService getService() {
		return (ClientePerdidoService) this.getDefaultService();
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public List findLookupFilial(TypedFlatMap criteria) {
    	List listFilial = filialService.findLookupAsPaginated(criteria);
    	return listFilial;
    }
	
	public List findLookupCliente(Map criteria) {		
		return clienteService.findLookup(criteria);
	}
	
/////////////////////
	// DADOS DA SESS�O //
	/////////////////////
	public Map getBasicData() {
		TypedFlatMap filial = new TypedFlatMap();
		Filial filialUsuario = SessionUtils.getFilialSessao();
		Long idFilial = filialUsuario.getIdFilial();
		filial.put("idFilial", idFilial);
		filial.put("sgFilial", filialUsuario.getSgFilial());
				
		TypedFlatMap pessoa = new TypedFlatMap();
		pessoa.put("nmFantasia", filialUsuario.getPessoa().getNmFantasia());
		filial.put("pessoa", pessoa);

		TypedFlatMap regional = new TypedFlatMap();
		Regional reg = regionalFilialService.findLastRegionalVigente(idFilial);
		regional.put("idRegional", reg.getIdRegional());
		regional.put("siglaDescricao", reg.getSiglaDescricao());	
		
		TypedFlatMap dadosUsuario = new TypedFlatMap();
		
		dadosUsuario.put("filial", filial);
		dadosUsuario.put("regional", regional);			
		
		return dadosUsuario;
	}
	

	public Map getRegionalByFilial(Long idFilial) {
		TypedFlatMap regional = new TypedFlatMap();
		Regional reg = regionalFilialService.findLastRegionalVigente(idFilial);
		regional.put("siglaDescricao", reg.getSiglaDescricao());
		TypedFlatMap regionalFilial = new TypedFlatMap();
		regionalFilial.put("regional", regional);		
		return regionalFilial;
	}
	
	
	public Serializable store(ClientePerdido bean) {
		return getService().store(bean);
	}
	
	public List findComboRamoAtividade(Map criteria) {
		return ramoAtividadeService.findCombo(criteria);
	}

	public List findComboMoeda(TypedFlatMap criteria) {
		Filial filialUsuario = SessionUtils.getFilialSessao();
		EnderecoPessoa enderecoPessoa = this.enderecoPessoaService.findEnderecoPessoaPadrao(filialUsuario.getIdFilial());
		Long idPais = enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
		return configuracoesFacade.getMoeda(idPais, Boolean.TRUE);
	}

	
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		if(!validateFindPaginated(criteria)) {
			throw new BusinessException("LMS-00055");
		}
		return getService().findPaginatedCustom(criteria);
	}

	
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		if (!validateFindPaginated(criteria)) {
			return IntegerUtils.ZERO;
		}
		return getService().getRowCountCustom(criteria);
	}

	private Boolean validateFindPaginated(TypedFlatMap criteria){
		if(criteria.getYearMonthDay("dtInicial") == null 
				&& criteria.getYearMonthDay("dtFinal") == null && 
				criteria.getLong("cliente.idCliente") == null && 
				criteria.getLong("filial.idFilial") == null	 && 
				criteria.getDomainValue("tpAbrangencia").getValue().equals("") && 
				criteria.getDomainValue("tpMotivoPerda").getValue().equals("")&& 
				criteria.getLong("segmentoMercado.idSegmentoMercado")== null)
		{
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public Serializable findById(Long id) {
		return super.findById(id);
	}


	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Remove v�rios registros master e filhos
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		// TODO Auto-generated method stub
		super.removeByIds(ids);
	} 	
	
	
}
