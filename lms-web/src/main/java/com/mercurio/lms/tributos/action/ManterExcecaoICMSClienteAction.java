package com.mercurio.lms.tributos.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.tributos.model.ExcecaoICMSCliente;
import com.mercurio.lms.tributos.model.ExcecaoICMSNatureza;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.tributos.model.service.ExcecaoICMSClienteService;
import com.mercurio.lms.tributos.model.service.ExcecaoICMSNaturezaService;
import com.mercurio.lms.tributos.model.service.TipoTributacaoIcmsService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.manterExcecaoICMSClienteAction"
 */
public class ManterExcecaoICMSClienteAction extends CrudAction {
	
	private NaturezaProdutoService naturezaProdutoService;
	private ExcecaoICMSNaturezaService excecaoICMSNaturezaService;
	private TipoTributacaoIcmsService tipoTributacaoIcmsService;
	private PessoaService pessoaService;
	
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setTipoTributacaoIcmsService(TipoTributacaoIcmsService tipoTributacaoIcmsService){
		this.tipoTributacaoIcmsService = tipoTributacaoIcmsService;
	}
	
	private ConfiguracoesFacade configuracoesFacade;
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade){
		this.configuracoesFacade = configuracoesFacade;
	}

 	public void setExcecaoICMSClienteService(ExcecaoICMSClienteService excecaoICMSClienteService){
		this.defaultService = excecaoICMSClienteService;
	}
 	
 	public ExcecaoICMSClienteService getExcecaoICMSClienteService(){
		return (ExcecaoICMSClienteService) this.defaultService;
	}

 	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Serializable store(TypedFlatMap tfm) {
    	
    	if(!LongUtils.hasValue(tfm.getLong("idExcecaoICMSCliente")) 
    			&& JTDateTimeUtils.comparaData(tfm.getYearMonthDay("dtVigenciaInicial"), JTDateTimeUtils.getDataAtual()) <= 0){
    		throw new BusinessException("LMS-30040");
    	}
    	
    	if(tfm.getYearMonthDay("dtVigenciaFinal") != null && 
    			JTDateTimeUtils.comparaData(tfm.getYearMonthDay("dtVigenciaFinal"), JTDateTimeUtils.getDataAtual()) < 0){
    		throw new BusinessException("LMS-01030");
    	}
    	    	
    	ExcecaoICMSCliente eicms = populateExcecaoIcmsCliente(tfm);
    	
    	((ExcecaoICMSClienteService)this.defaultService).storeExcecaoIcmsCliente(eicms);
    	
    	tfm.put("cdEmbLegalMastersaf", eicms.getCdEmbLegalMastersaf());
    	tfm.put("nrCNPJParcialDev", eicms.getNrCNPJParcialDev());
    	tfm.put("idExcecaoICMSCliente",eicms.getIdExcecaoICMSCliente());
    	
    	salvaListaNaturezaProduto(tfm);
    	
    	List<Map> lsExICMSNatureza = excecaoICMSNaturezaService.findICMSNaturezaByIdICMSCliente(eicms.getIdExcecaoICMSCliente());		
					
		tfm.put("naturezaProduto", lsExICMSNatureza);
    	
	    return tfm;
    }

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 26/10/2007
	 *
	 * @param tfm
	 *
	 */
	private ExcecaoICMSCliente populateExcecaoIcmsCliente(TypedFlatMap tfm) {
		ExcecaoICMSCliente eicms = new ExcecaoICMSCliente();
    	
    	UnidadeFederativa uf = new UnidadeFederativa();
    	uf.setIdUnidadeFederativa(tfm.getLong("unidadeFederativa.idUnidadeFederativa"));

    	TipoTributacaoIcms tticms = new TipoTributacaoIcms(); 
    	tticms.setIdTipoTributacaoIcms(tfm.getLong("tipoTributacaoIcms.idTipoTributacaoIcms"));
    	tticms.setDsTipoTributacaoIcms(tfm.getString("tipoTributacaoIcms.dsTipoTributacaoIcms"));
    	
    	eicms.setTipoTributacaoIcms(tticms);
    	eicms.setUnidadeFederativa(uf);
    	eicms.setBlSubcontratacao(tfm.getBoolean("blSubcontratacao"));
    	eicms.setDtVigenciaFinal(tfm.getYearMonthDay("dtVigenciaFinal"));
    	eicms.setDtVigenciaInicial(tfm.getYearMonthDay("dtVigenciaInicial"));
    	eicms.setTpFrete(tfm.getDomainValue("tpFrete"));
    	eicms.setBlObrigaCtrcSubContratante(tfm.getBoolean("blObrigaCtrcSubContratante"));
    	eicms.setIdExcecaoICMSCliente(tfm.getLong("idExcecaoICMSCliente"));
    	eicms.setDsRegimeEspecial(tfm.getString("dsRegimeEspecial"));
    	// Formata o cnpj de acordo com o tipo selecionado na tela.
    	eicms.setNrCNPJParcialDev(getExcecaoICMSClienteService()
    			.formatCnpj(tfm.getString("nrCNPJParcialDev"), tfm.getString("tipoCnpj")));
    	
    	eicms.setCdEmbLegalMastersaf(tfm.getString("cdEmbLegalMastersaf"));
    	
    	return eicms;
	}
	
	/**
	 * Salva a lista de naturezas produto da list box
	 * @param tfm
	 */
	private void salvaListaNaturezaProduto(TypedFlatMap tfm){
		List<Map> listaIcmsNatureza = tfm.getList("naturezaProduto");						
		excecaoICMSNaturezaService.removeByIdExcecaoICMSCliente(tfm.getLong("idExcecaoICMSCliente"));
				
		ExcecaoICMSNatureza excecaoICMSNatureza = null;
		NaturezaProduto	naturezaProduto = null;
		if(listaIcmsNatureza != null && !listaIcmsNatureza.isEmpty()){
			for(Map mp : listaIcmsNatureza){
				if(LongUtils.hasValue(MapUtilsPlus.getLong(mp, "idNaturezaProduto"))){
					excecaoICMSNatureza = new ExcecaoICMSNatureza();
					
					ExcecaoICMSCliente excecaoICMSCliente = new ExcecaoICMSCliente();
					excecaoICMSCliente.setIdExcecaoICMSCliente(tfm.getLong("idExcecaoICMSCliente"));				
					excecaoICMSNatureza.setExcecaoICMSCliente(excecaoICMSCliente);
					
					naturezaProduto = new NaturezaProduto();
					naturezaProduto.setIdNaturezaProduto(MapUtilsPlus.getLong(mp, "idNaturezaProduto"));				
					excecaoICMSNatureza.setNaturezaProduto(naturezaProduto);
					
					excecaoICMSNaturezaService.store(excecaoICMSNatureza);				
				}
			}	
		}		
	}
	
	
	
	/**
	 * Valida se a dtVigenciaFinal � maior ou igual a data atual.
	 * 
	 * @param tfm
	 * @return
	 */
	public Map validateDtVigenciaFinal(TypedFlatMap tfm){
		Map retorno = new HashMap<String, Boolean>();
		Boolean  maiorIgualDtAtual;
		
		YearMonthDay dtVigenciaFinal = tfm.getYearMonthDay("dtVigenciaFinal");
		if (dtVigenciaFinal == null){
			maiorIgualDtAtual = Boolean.FALSE;
		} else {
			maiorIgualDtAtual = JTDateTimeUtils.comparaData(
					dtVigenciaFinal, 
					JTDateTimeUtils.getDataAtual()) >= 0;
		}
		retorno.put("dtVigenciaFinalMaiorIgualDtAtual", maiorIgualDtAtual);

		return retorno;
	}
    
    /**
     * Remove uma ParametroSubstituicaoTrib pelo id passado mpor parametro
     * @param id
     */
    public void removeById(Long id) {

		validaExclusao(id);

		List<Long> ids = new ArrayList<Long>();
		ids.add(id);

		((ExcecaoICMSClienteService)this.defaultService).removeExcecaoICMSCliente(ids);

	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {

		for(Object id : ids){
			validaExclusao(LongUtils.getLong(id));
	}
    
		((ExcecaoICMSClienteService)this.defaultService).removeExcecaoICMSCliente(ids);
		
	}
    

	/**
	 * Busca uma ParametroSubstituicaoTrib de acordo com o id passado por parametro
	 * @param id
	 * @return
	 */
	public Serializable findById(Long id) {
		
		ExcecaoICMSCliente eic = ((ExcecaoICMSClienteService)this.defaultService).findById(id);
		
		List<Map> lsExICMSNatureza = excecaoICMSNaturezaService.findICMSNaturezaByIdICMSCliente(id);		
		
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("naturezaProduto", lsExICMSNatureza);
		
		if( eic != null ){
		
			tfm.put("tpFrete.description",eic.getTpFrete().getDescription());
			tfm.put("tpFrete.value",eic.getTpFrete().getValue());
			tfm.put("tpFrete.status",eic.getTpFrete().getStatus());
			
			tfm.put("idExcecaoICMSCliente",eic.getIdExcecaoICMSCliente());
			tfm.put("blSubcontratacao",eic.getBlSubcontratacao());
			tfm.put("blObrigaCtrcSubContrataOsnte",eic.getBlObrigaCtrcSubContratante());
			tfm.put("dsRegimeEspecial", eic.getDsRegimeEspecial());
			
			tfm.put("unidadeFederativa.tpSituacao",eic.getUnidadeFederativa().getTpSituacao());
			tfm.put("unidadeFederativa.siglaDescricao",eic.getUnidadeFederativa().getSiglaDescricao());
			tfm.put("unidadeFederativa.sgUnidadeFederativa",eic.getUnidadeFederativa().getSgUnidadeFederativa());
			tfm.put("unidadeFederativa.nmUnidadeFederativa",eic.getUnidadeFederativa().getNmUnidadeFederativa());
			tfm.put("unidadeFederativa.idUnidadeFederativa",eic.getUnidadeFederativa().getIdUnidadeFederativa());
			
			String nrCNPJParcial = StringUtils.leftPad(eic.getNrCNPJParcialDev().toString(),8,'0');
			
			tfm.put("nrCNPJParcialDev",nrCNPJParcial);
			
			List pessoas = null;
			
			try {
				pessoas = pessoaService.findNrCNPJParcialEqualNrIdentificacaoPessoa(nrCNPJParcial);				          
			} catch (Exception e) {
			}
			
			if( pessoas != null && !pessoas.isEmpty() ){
				tfm.put("nmDevedor",((Pessoa)pessoas.get(0)).getNmPessoa());
			}
			
			tfm.put("tipoTributacaoIcms.idTipoTributacaoIcms",eic.getTipoTributacaoIcms().getIdTipoTributacaoIcms());
			tfm.put("tipoTributacaoIcms.dsTipoTributacaoIcms",eic.getTipoTributacaoIcms().getDsTipoTributacaoIcms());
			
			tfm.put("dtVigenciaInicial",eic.getDtVigenciaInicial());
			tfm.put("dtVigenciaFinal",eic.getDtVigenciaFinal());
			
			if(eic.getDtVigenciaInicial() != null){
				tfm.put("comparaInicial",JTDateTimeUtils.comparaData(eic.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()));
		}
		
			if(eic.getDtVigenciaFinal() != null){
				tfm.put("comparaFinal",JTDateTimeUtils.comparaData(eic.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()));
			}
								
			tfm.put("cdEmbLegalMastersaf",eic.getCdEmbLegalMastersaf());
						
		}
		
		return tfm;
	}
	
	/**
	 * Retona a lista com Natureza do Produto
	 * @return
	 */
	public List findComboNaturezaProduto(){
		return naturezaProdutoService.findAllAtivo();
	}
		
	/**
     * M�todo respons�vel por popular a combo de TipoTributacaoICMS
     * 
     * @author HectorJ
     * @since 31/05/2006
     * 
     * @param notInIdsParametrosGerais
     * @return List <TipoTributacaoIcms>
     */
	public List findComboTipoTributacaoIcms(){
		List lst = new ArrayList();
		lst.add(((BigDecimal) configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_ST")).longValue());

		return tipoTributacaoIcmsService.findComboTipoTributacaoIcms(lst, null, null);
	}
	
	/**
     * M�todo respons�vel por popular a combo de TipoTributacaoICMS somente onde o status � ativo
     * 
     * @author HectorJ
     * @since 31/05/2006
     * 
     * @param notInIdsParametrosGerais
     * @return List <TipoTributacaoIcms>
     */
	public List findComboTipoTributacaoIcmsOnlyActivaValues(){ 
		List lst = new ArrayList();
		lst.add(((BigDecimal) configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_ST")).longValue());
		
		return tipoTributacaoIcmsService.findComboTipoTributacaoIcms(lst, "A", null);
	}
	
	/**
	 * M�todo respons�vel por buscar nrCNPJParcialDev igual as primeiras 8 posi��es do nrIdentificacao da Pessoa 
	 * 
	 * @author HectorJ
	 * @since 31/05/2006
	 * 
	 * @param nrCNPJParcialDev
	 * @return TypedFlatMap
	 */
	public TypedFlatMap findNrCNPJParcialDevEqualNrIdentificacaoPessoa(TypedFlatMap criteria){
		// Formata o cnpj de acordo com o tipo selecionado na tela.
		String nrIdentificacao = getExcecaoICMSClienteService().formatCnpj(
				criteria.getString("nrCNPJParcialDev"), 
				criteria.getString("tipoCnpj"));
		
		List pessoas = ((ExcecaoICMSClienteService)this.defaultService)
				.findPessoaByNrIdentificacao(nrIdentificacao);
		
		TypedFlatMap tfm = new TypedFlatMap();
		if( pessoas != null && !pessoas.isEmpty() ){
			Pessoa p = (Pessoa)pessoas.get(0);
			tfm.put("nmPessoa",p.getNmPessoa());
		}
		
		return tfm;
		
	}

	/**
	 * @author Jos� Rodrigo Moraes
	 * @since  18/07/2006
	 * 
	 * @param tfm Crit�rios de pesquisa
	 * @return ResultSetPage Dados resultantes da query de pesquisa e dados de pagina��o
	 * 
	 */
	public ResultSetPage findPaginated(Map tfm) {
		
		if (!"".equals(tfm.get("nrCNPJParcialDev").toString())) {
			// Formata o cnpj de acordo com o tipo selecionado na tela.
			String nrIdentificacao = getExcecaoICMSClienteService().formatCnpj(
					tfm.get("nrCNPJParcialDev").toString(), 
					tfm.get("tipoCnpj").toString());
			tfm.put("nrCNPJParcialDev", nrIdentificacao);
		}
		ResultSetPage rsp = getExcecaoICMSClienteService().findPaginated(tfm);
		List resultados = rsp.getList();
		TypedFlatMap ret = null;
		List retorno = new ArrayList();

		for (Iterator iter = resultados.iterator(); iter.hasNext();) {
			ExcecaoICMSCliente eic = (ExcecaoICMSCliente) iter.next();
			ret = new TypedFlatMap();
			ret.put("idExcecaoICMSCliente",eic.getIdExcecaoICMSCliente());
			ret.put("unidadeFederativa.sgUnidadeFederativa",eic.getUnidadeFederativa().getSgUnidadeFederativa());
			ret.put("tipoTributacaoIcms.dsTipoTributacaoIcms",eic.getTipoTributacaoIcms().getDsTipoTributacaoIcms());
			ret.put("tpFrete",eic.getTpFrete());
							
			List pessoas = null;
			Pessoa pessoa = null;
			if (eic.getNrCNPJParcialDev().length() == 14){
				pessoa = pessoaService.findByNrIdentificacao(eic.getNrCNPJParcialDev());
			}else{
				pessoa = pessoaService.findByCNPJParcial(eic.getNrCNPJParcialDev());
			}
			if (pessoa != null){
				ret.put("nrCNPJParcialDev",eic.getNrCNPJParcialDev() +" - " + pessoa.getNmPessoa());
			}else{
				ret.put("nrCNPJParcialDev",eic.getNrCNPJParcialDev());
			}
			
			ret.put("dtVigenciaInicial",eic.getDtVigenciaInicial());
			ret.put("dtVigenciaFinal",eic.getDtVigenciaFinal());
			ret.put("blSubcontratacao",eic.getBlSubcontratacao());
			
			retorno.add(ret);
			
		}
		rsp.setList(retorno);
		
		return rsp;
	}
		
	private void validaExclusao(Long id){		
		final ExcecaoICMSCliente exc = ((ExcecaoICMSClienteService)this.defaultService).findById(id);
		if(exc.getDtVigenciaFinal() != null 
				|| JTDateTimeUtils.comparaData(exc.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0){
			throw new BusinessException("LMS-00005");			
}
	}

	public void setExcecaoICMSNaturezaService(
			ExcecaoICMSNaturezaService excecaoICMSNaturezaService) {
		this.excecaoICMSNaturezaService = excecaoICMSNaturezaService;
	}

	public void setNaturezaProdutoService(
			NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

}
