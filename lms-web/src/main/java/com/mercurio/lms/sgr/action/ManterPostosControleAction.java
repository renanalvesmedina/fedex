package com.mercurio.lms.sgr.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.service.ReguladoraSeguroService;
import com.mercurio.lms.sgr.model.PostoControle;
import com.mercurio.lms.sgr.model.service.PostoControleService;
import com.mercurio.lms.util.PessoaUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sgr.manterPostosControleAction"
 */

public class ManterPostosControleAction extends CrudAction {
	
	private ReguladoraSeguroService reguladoraSeguroService;
	
	public ReguladoraSeguroService getReguladoraSeguroService() {
		return reguladoraSeguroService;
	}
	public void setReguladoraSeguroService(
			ReguladoraSeguroService reguladoraSeguroService) {
		this.reguladoraSeguroService = reguladoraSeguroService;
	}
	public void setPostoControle(PostoControleService postoControleService) {
		this.defaultService = postoControleService;
	}
    public void removeById(java.lang.Long id) {
        ((PostoControleService)defaultService).removeById(id);
    }

	public ResultSetPage findPaginatedPostosControle(Map criteria) {
		ResultSetPage rsp = super.findPaginated(criteria);
		List listPostosControle = rsp.getList();
		List listRetorno = new ArrayList();
		Map map = null;
		for (Iterator iter = listPostosControle.iterator(); iter.hasNext();) {
			map = new TypedFlatMap();
			PostoControle postoControle = (PostoControle) iter.next();
			map.put("idPostoControle", postoControle.getIdPostoControle());
			map.put("nmPostoControlePassaporte", postoControle.getNmPostoControlePassaporte());
			map.put("nmLocal", postoControle.getNmLocal());
			map.put("municipio.nmMunicipio", postoControle.getMunicipio().getNmMunicipio());
			map.put("municipio.unidadeFederativa.sgUnidadeFederativa", postoControle.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			map.put("rodovia.sgRodovia", postoControle.getRodovia().getSgRodovia());
			map.put("nrKm", postoControle.getNrKm());
			map.put("tpBandeiraPosto", postoControle.getTpBandeiraPosto());
			map.put("tpSituacao", postoControle.getTpSituacao());
			listRetorno.add(map);
		}
		rsp.setList(listRetorno);
		return rsp;
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((PostoControleService)defaultService).removeByIds(ids);
    }

    public PostoControle findById(java.lang.Long id) {
    	return ((PostoControleService)defaultService).findById(id);
    }
    
    /**
     * Faz a pesquisa pela <code>ReguladoraSeguro</code>. Retira a mascara
     * de formatacao do numero de identificacao caso exista.  
     * 
     * @param criteria
     * @return
     */
    public List findLookupReguladoraSeguro(Map criteria) {
    	Map mapPessoa = (Map) criteria.get("pessoa");
    	String nrIdentificacao = PessoaUtils.validateIdentificacao((String) mapPessoa.get("nrIdentificacao"));
    	mapPessoa.put("nrIdentificacao", nrIdentificacao);

    	return this.getReguladoraSeguroService().findLookup(criteria);
    }

}
