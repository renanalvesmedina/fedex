package com.mercurio.lms.tributos.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.tributos.model.TipoTributacaoUf;
import com.mercurio.lms.tributos.model.service.TipoTributacaoUfService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.manterTipoTributacaoExportacaoUfAction"
 */
public class ManterTipoTributacaoExportacaoUfAction extends CrudAction {
	
 	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Serializable store(TipoTributacaoUf bean) {
    	
    	/*Se a data de vigencia inicial for menor ou igual a data atual mostra a excecao*/
    	if(JTDateTimeUtils.comparaData(bean.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0){
    		throw new BusinessException("LMS-30040");
    	}
    	
    	if(bean.getDtVigenciaFinal() != null 
    			&& JTDateTimeUtils.comparaData(bean.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()) < 0){
    		throw new BusinessException("LMS-01030");
    	}
    	
        return ((TipoTributacaoUfService)this.defaultService).store(bean);
    } 
    
    public Serializable storeTipoTributacaoExportacaoUf(TypedFlatMap map){
    	
    	/*Se a data de vigencia inicial for menor ou igual a data atual mostra a excecao*/
    	if(map.getLong("idTipoTributacaoUf") == null 
    			&& JTDateTimeUtils.comparaData(map.getYearMonthDay("dtVigenciaInicial"), JTDateTimeUtils.getDataAtual()) <= 0){
    		throw new BusinessException("LMS-30040");
    	}
    	
    	if(map.getYearMonthDay("dtVigenciaFinal") != null 
    			&& JTDateTimeUtils.comparaData(map.getYearMonthDay("dtVigenciaFinal"), JTDateTimeUtils.getDataAtual()) < 0){
    		throw new BusinessException("LMS-01030");
    	}       	
    	
    	TipoTributacaoUf ttf = new TipoTributacaoUf();    	
    	ttf.setIdTipoTributacaoUf(map.getLong("idTipoTributacaoUf"));
    	
    	/*UF*/
    	UnidadeFederativa uf = new UnidadeFederativa();
    	uf.setIdUnidadeFederativa(MapUtils.getLong(map, "unidadeFederativa.idUnidadeFederativa"));    	
    	ttf.setUnidadeFederativa(uf);
    	
    	/*TipoTributacaoUf*/ 
    	TipoTributacaoIcms ttu = new TipoTributacaoIcms();
    	ttu.setIdTipoTributacaoIcms(MapUtils.getLong(map, "tipoTributacaoIcms.idTipoTributacaoIcms"));
    	ttf.setTipoTributacaoIcms(ttu);
    	
    	/*tipoFrete*/
    	ttf.setTpTipoFrete(map.getDomainValue("tpTipoFrete"));
    	
    	/*Abrangencia*/
    	ttf.setTpAbrangenciaUf(map.getDomainValue("tpAbrangenciaUf"));
    	
    	/*Contribuinte*/
    	ttf.setBlContribuinte(map.getBoolean("blContribuinte"));
    	
    	/*Vigencia*/
    	ttf.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
    	ttf.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));

    	return ((TipoTributacaoUfService)this.defaultService).store(ttf);
    }
    
    
    /**
     * Remove uma ParametroSubstituicaoTrib pelo id passado mpor parametro
     * @param id
     */
    public void removeById(Long id) {
    	validaExclusao(id);
		((TipoTributacaoUfService)this.defaultService).removeById(id);
	} 
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for(Object id : ids){
			validaExclusao(LongUtils.getLong(id));
		}
		((TipoTributacaoUfService)this.defaultService).removeByIds(ids);
	} 
	
	/**
	 * Valida a exclusao do registro TipoTributacaoUf
	 * 
	 * Se a data de vigencia final estiver informada ou a data de vigencia inicial for menor ou igual 
	 * a data arual mostra a excecao LMS-0005
	 * 
	 */
	private void validaExclusao(Long id){
		TipoTributacaoUf tributacaoUf = ((TipoTributacaoUfService)this.defaultService).findById(id);
		if(tributacaoUf.getDtVigenciaFinal() != null 
				|| JTDateTimeUtils.comparaData(tributacaoUf.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0){
			throw new BusinessException("LMS-00005");
		}
	}
	
	
	/**
     * Método responsável por popular a combo de TipoTributacaoIcms
     * 
     * @author Diego Umpierre
     * @since 24/08/2006
     * 
     * @return List <TipoTributacaoIcms>
     */
	public List findComboTipoTributacaoIcms( TypedFlatMap criteria ){
		String onlyActivesValues = null;
		
		if ( criteria != null &&  "A".equalsIgnoreCase(criteria.getString("tpSituacao")) ){
			onlyActivesValues = "A";
		}
		
		return ((TipoTributacaoUfService)this.defaultService).findComboTipoTributacaoIcms(onlyActivesValues);
	} 
	
	
	
	/**	Busca a lookup de UF
	 * 	
	 * @author Diego Umpierre
	 * @since 24/08/2006
	 * 
	 * @param criteria
	 * @return
	 */
	 public List findLookupUF(Map criteria) {
		 return ((TipoTributacaoUfService)this.defaultService).findLookupUF(criteria);
	 } 
	 
	 
	 
	 

	/**
	 * Método responsável por buscar os dados da grid 
	 * 
	 * @author Diego Umpierre
	 * @since 24/08/2006
	 * 
	 * @param tfm Critérios de pesquisa
	 * @return ResultSetPage Dados resultantes da query de pesquisa e dados de paginação
	 */
	public ResultSetPage findPaginatedTela(TypedFlatMap criteria) {
		
		
		ResultSetPage rsp = ((TipoTributacaoUfService)this.defaultService).findPaginatedTela(criteria);
		
		List resultados = rsp.getList();
		
		TypedFlatMap ret = null;
		List retorno = new ArrayList();
		
		for (Iterator iter = resultados.iterator(); iter.hasNext();) {
			
			TipoTributacaoUf tipoTributacaoUf = (TipoTributacaoUf) iter.next();
			
			ret = new TypedFlatMap();
						
			ret.put("idTipoTributacaoUf",tipoTributacaoUf.getIdTipoTributacaoUf());
			
			ret.put("unidadeFederativa.sgUnidadeFederativa",tipoTributacaoUf.getUnidadeFederativa().getSgUnidadeFederativa()
					+" - "+
					tipoTributacaoUf.getUnidadeFederativa().getNmUnidadeFederativa()
			);
			
			
			ret.put("dtVigenciaInicial",tipoTributacaoUf.getDtVigenciaInicial());
			ret.put("dtVigenciaFinal",tipoTributacaoUf.getDtVigenciaFinal());
			
			ret.put("tipoTributacaoIcms",tipoTributacaoUf.getTipoTributacaoIcms().getDsTipoTributacaoIcms());
			
			
			retorno.add(ret);
			
		}
		
		rsp.setList(retorno);
		
		return rsp;
	} 
	
	
		
	/**
	 * Método responsável por buscar o número de linhas da grid 
	 * 
	 * @author Diego Umpierre
	 * @since 18/07/2006
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountTela(TypedFlatMap criteria) {
		return ((TipoTributacaoUfService)this.defaultService).getRowCountTela(criteria);
	}

	
	
	
	
	
	/**
	 * Busca um TipoTributacaoUf de acordo com o id passado por parametro
	 * 
	 * @author Diego Umpierre
	 * @since 18/07/2006
	 * 
	 * @param id
	 * @return
	 */
	public Serializable findById(Long id) {
		
		TipoTributacaoUf ttuf = ((TipoTributacaoUfService)this.defaultService).findById(id);
		
		TypedFlatMap tfm = new TypedFlatMap();
		
		if( ttuf != null ){
		
			tfm.put("idTipoTributacaoUf",ttuf.getIdTipoTributacaoUf());
			
			tfm.put("unidadeFederativa.tpSituacao",ttuf.getUnidadeFederativa().getTpSituacao());
			tfm.put("unidadeFederativa.siglaDescricao",ttuf.getUnidadeFederativa().getSiglaDescricao());
			tfm.put("unidadeFederativa.sgUnidadeFederativa",ttuf.getUnidadeFederativa().getSgUnidadeFederativa());
			tfm.put("unidadeFederativa.nmUnidadeFederativa",ttuf.getUnidadeFederativa().getNmUnidadeFederativa());
			tfm.put("unidadeFederativa.idUnidadeFederativa",ttuf.getUnidadeFederativa().getIdUnidadeFederativa());
			
			tfm.put("tipoTributacaoIcms.idTipoTributacaoIcms",ttuf.getTipoTributacaoIcms().getIdTipoTributacaoIcms());
			tfm.put("tipoTributacaoIcms.dsTipoTributacaoIcms",ttuf.getTipoTributacaoIcms().getDsTipoTributacaoIcms());
			
			tfm.put("dtVigenciaInicial",ttuf.getDtVigenciaInicial());
			tfm.put("dtVigenciaFinal",ttuf.getDtVigenciaFinal());
					
			if(ttuf.getTpTipoFrete() != null){
				tfm.put("tpTipoFrete",ttuf.getTpTipoFrete().getValue());
		}
			if(ttuf.getTpAbrangenciaUf() != null){
				tfm.put("tpAbrangenciaUf",ttuf.getTpAbrangenciaUf().getValue());
			}	
			if(ttuf.getBlContribuinte()){
				tfm.put("blContribuinte","S");
			}else{
				tfm.put("blContribuinte","N");
			}
		
			if(ttuf.getDtVigenciaInicial() != null){
				tfm.put("comparaInicial",JTDateTimeUtils.comparaData(ttuf.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()));
			}
			
			if(ttuf.getDtVigenciaFinal() != null){
				tfm.put("comparaFinal",JTDateTimeUtils.comparaData(ttuf.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()));
			}			
					
		}
		
		return tfm;
	}

	
	/** Seta a service padrão
	 * 
	 * @param tipoTributacaoUfService
	 */
	public void setTipoTributacaoUfService(
			TipoTributacaoUfService tipoTributacaoUfService) {
		this.defaultService = tipoTributacaoUfService;
	}
}