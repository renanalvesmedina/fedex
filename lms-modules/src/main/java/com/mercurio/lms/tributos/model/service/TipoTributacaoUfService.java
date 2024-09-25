package com.mercurio.lms.tributos.model.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.TipoTributacaoUf;
import com.mercurio.lms.tributos.model.dao.TipoTributacaoUfDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.tipoTributacaoUfService"
 */
public class TipoTributacaoUfService extends CrudService<TipoTributacaoUf, Long> {

	private UnidadeFederativaService unidadeFederativaService;

	private TipoTributacaoIcmsService tipoTributacaoIcmsService;
	
	
	private ConfiguracoesFacade configuracoesFacade;
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade){
		this.configuracoesFacade = configuracoesFacade;
	}

	
	
	/**
	 * Recupera uma inst�ncia de <code>Object</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public TipoTributacaoUf findById(java.lang.Long id) {
        return (TipoTributacaoUf)super.findById(id);
    }
    

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    
    
    
    
	/**
     * M�todo respons�vel por popular a combo de TipoTributacaoICMS
     * 
     * @author Diego Umpierre
     * @since 24/08/2006
     * 
     * @param notInIdsParametrosGerais
     * @return List <TipoTributacaoIcms>
     */
	public List findComboTipoTributacaoIcms(String onlyActivesValues){
		List lst = new ArrayList();
		lst.add(((BigDecimal) configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_ST")).longValue());
		lst.add(((BigDecimal) configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_NORMAL")).longValue());
		
		return tipoTributacaoIcmsService.findComboTipoTributacaoIcms(lst, onlyActivesValues, null);
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
		 return unidadeFederativaService.findLookup(criteria);
	 }

	
	/**
	 * M�todo respons�vel por buscar os dados da grid
	 * 
	 * @author Diego Umpierre
	 * @since 20/07/2006
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */	
	public ResultSetPage findPaginatedTela(TypedFlatMap criteria) {
		ResultSetPage rsp = getTipoTributacaoUfDAO().findPaginatedTela(criteria, 
				FindDefinition.createFindDefinition(criteria));
    	return  rsp;
	}
	
	

	/**
	 * M�todo respons�vel por buscar o numero de linhas da grid 
	 * 
	 * @author Diego Umpierre
	 * @since 24/08/2006
	 * 
	 * @param criteria
	 * @return Integer
	 */ 
	public Integer getRowCountTela(TypedFlatMap criteria) {
		return getTipoTributacaoUfDAO().getRowCountTela(criteria);
	}

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TipoTributacaoUf bean) {
        return super.store(bean);
    }
    
    /**
     * M�todo invocado antes do store, valida se ja existe um tipo de tributacao para uf
     * 
     * 
     *author Diego Umpierre
	 *@since 18/07/2006
	 *  
     */
	public TipoTributacaoUf beforeStore(TipoTributacaoUf bean) {
		TipoTributacaoUf ttuf = (TipoTributacaoUf) bean;
		
		String tpFrete = null;
		if(bean.getTpTipoFrete() != null){
			tpFrete = bean.getTpTipoFrete().getValue();
		}
		
		String tpAbrangencia = null;
		if(bean.getTpAbrangenciaUf() != null){
			tpAbrangencia = bean.getTpAbrangenciaUf().getValue();
		}		
		
		/** Busca registros na base com intervalos de vig�ncia iguais */
		List lst = getTipoTributacaoUfDAO().findExcecaoByVigenciaEquals(ttuf.getDtVigenciaInicial()
						, ttuf.getDtVigenciaFinal()
						, ttuf.getUnidadeFederativa().getIdUnidadeFederativa()
						, ttuf.getIdTipoTributacaoUf()
						, tpFrete
						, tpAbrangencia);						  
		
		/** Verifica se n�o j� n�o existe nenhum registro na base com o mesmo intervalo de vig�ncia */
		if(!lst.isEmpty()){
			throw new BusinessException("LMS-23022");
		}
		 
		return super.beforeStore(bean);
	}
	
    /**
     * Retorna o tipo de tributa��o UF vigente da UF informada.
     * 
     * @author Micka�l Jalbert
     * @since 11/09/2006
     * 
     * @param Long idUF
     * @param YearMonthDay dtVigencia
     * @return TipoTributacaoUf 
     */
    public TipoTributacaoUf findByIdUFVigente(Long idUF, YearMonthDay dtVigencia){
    	return getTipoTributacaoUfDAO().findByIdUFVigente(idUF, dtVigencia);
    }  
    
    /**
	 * Busca TipoTributa��o pala Situa��o Tributaria
	 * @author Andr� Valadas
	 * @since 04/05/2009
	 * 
	 * @param tpTipoFrete
	 * @param idIEResponsavel
	 * @param idUnidadeFederativaOrigem
	 * @param idUnidadeFederativaDestino
	 * @param dtVigenciaInicial
	 * @return
	 */
	public TipoTributacaoUf findTipoTributacao(
			String tpTipoFrete,
			Long idIEResponsavel,
			String tpSituacaoTributariaResponsavel,
			Long idUnidadeFederativaOrigem,
			Long idUnidadeFederativaDestino,
			YearMonthDay dtVigenciaInicial) {
		return getTipoTributacaoUfDAO().findTipoTributacao(tpTipoFrete, idIEResponsavel, tpSituacaoTributariaResponsavel, idUnidadeFederativaOrigem, idUnidadeFederativaDestino, dtVigenciaInicial);
	}
    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTipoTributacaoUfDAO(TipoTributacaoUfDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TipoTributacaoUfDAO getTipoTributacaoUfDAO() {
        return (TipoTributacaoUfDAO) getDao();
    }

	public void setTipoTributacaoIcmsService(
			TipoTributacaoIcmsService tipoTributacaoIcmsService) {
		this.tipoTributacaoIcmsService = tipoTributacaoIcmsService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
    
}