package com.mercurio.lms.fretecarreteiroviagem.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.ReferenciaFreteCarreteiroDAO;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteiroviagem.referenciaFreteCarreteiroService"
 */
public class ReferenciaFreteCarreteiroService extends CrudService<ReferenciaFreteCarreteiro, Long> {
	
	private EnderecoPessoaService enderecoPessoaService; 
	
	private VigenciaService vigenciaService;

	private ReferenciaTipoVeiculoService referenciaTipoVeiculoService;
	
	private FilialService filialService;
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public ReferenciaTipoVeiculoService getReferenciaTipoVeiculoService() {
		return referenciaTipoVeiculoService;
	}

	public void setReferenciaTipoVeiculoService(
			ReferenciaTipoVeiculoService referenciaTipoVeiculoService) {
		this.referenciaTipoVeiculoService = referenciaTipoVeiculoService;
	}

	protected ReferenciaFreteCarreteiro beforeStore(ReferenciaFreteCarreteiro bean) {
		ReferenciaFreteCarreteiro rfc = (ReferenciaFreteCarreteiro)bean;
		vigenciaService.validaVigenciaBeforeStore(rfc);
		return super.beforeStore(bean);
	}
	
	public Map store(ReferenciaFreteCarreteiro bean, ItemList items) {
    	
    	if((bean.getFilialByIdFilialOrigem()!= null && bean.getFilialByIdFilialDestino()== null) ||
				(bean.getFilialByIdFilialDestino()!= null && bean.getFilialByIdFilialOrigem() == null))
			throw new BusinessException("LMS-24004");
		
		if((bean.getUnidadeFederativaByIdUnidadeFederativaOrigem()!= null && bean.getUnidadeFederativaByIdUnidadeDestino()==null)
				|| (bean.getUnidadeFederativaByIdUnidadeDestino()!= null && bean.getUnidadeFederativaByIdUnidadeFederativaOrigem()== null))
			throw new BusinessException("LMS-24003");
		
		
		if(getReferenciaFreteCarreteiroDAO().findReferenciaFreteCarreteiroVigente(bean))
			throw new BusinessException("LMS-00003");
				
		if (!items.hasItems())
			throw new BusinessException("LMS-24002");
    	boolean rollbackMasterId = (bean.getIdReferenciaFreteCarreteiro() == null);			
    	try {
			this.beforeStore(bean);
			getReferenciaFreteCarreteiroDAO().store(bean,items);
		} catch (HibernateOptimisticLockingFailureException e) {
			throw new BusinessException("LMS-00012");
		} catch (RuntimeException e) {
            this.rollbackMasterState(bean,rollbackMasterId,e);
            items.rollbackItemsState();
            throw e;
		}

		
		Map rMap = new HashMap();
		rMap.put("idReferenciaFreteCarreteiro",bean.getIdReferenciaFreteCarreteiro());
		rMap.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
		return rMap;
    }

	/**
	 * Recupera uma instância de <code>ReferenciaFreteCarreteiro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ReferenciaFreteCarreteiro findById(java.lang.Long id) {
        return (ReferenciaFreteCarreteiro)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 
    public java.io.Serializable store(ReferenciaFreteCarreteiro bean) {
    	return super.store(bean);
    }*/

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setReferenciaFreteCarreteiroDAO(ReferenciaFreteCarreteiroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ReferenciaFreteCarreteiroDAO getReferenciaFreteCarreteiroDAO() {
        return (ReferenciaFreteCarreteiroDAO) getDao();
    }

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	
	public List findReferenciaFreteCarreteiroVigente(Long idUfOrigem, Long idUfDestino, Long idFilialOrigem, Long idFilialDestino, YearMonthDay dataInicio, YearMonthDay dataFim){
		return getReferenciaFreteCarreteiroDAO().findReferenciaFreteCarreteiroVigente(idUfOrigem,idUfDestino,idFilialOrigem,idFilialDestino, dataInicio, dataFim); 
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getReferenciaFreteCarreteiroDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getReferenciaFreteCarreteiroDAO().getRowCount(criteria);
	}
	
	/**
	 * Retorna o valor do frete carreteiro praticado entre unidades federativas ou entre filiais
	 * @param Long idUfOrigem, Long idUfDestino, Long idFilialOrigem, Long idFilialDestino, Long idMoeda, Long idTipoMeioTransporte, Integer qtKm
	 * @return Valor do frete carreteiro praticado entre unidades federativas ou entre filiais.
	 * 
	 */
	public BigDecimal findValorFreteCarreteiroByUfsFiliais(Long idUfOrigem, Long idUfDestino, Long idFilialOrigem, Long idFilialDestino, Long idMoeda, Long idTipoMeioTransporte, Integer qtKm){
		ReferenciaFreteCarreteiro rfc = getReferenciaFreteCarreteiroDAO().findReferenciaFreteCarreteiro(idUfOrigem,idUfDestino,idFilialOrigem,idFilialDestino,idMoeda);
		BigDecimal valorFreteCarreteiro = null;
		if (rfc != null){
			valorFreteCarreteiro = getReferenciaTipoVeiculoService().findValorFreteCarreteiroByIdReferenciaFrete(rfc.getIdReferenciaFreteCarreteiro(),idTipoMeioTransporte,qtKm);
		}
		return valorFreteCarreteiro;
	}
	
	protected void beforeRemoveByIds(List ids) {
    	ReferenciaFreteCarreteiro bean = null;
    	for(int x = 0; x < ids.size(); x++) {
    		bean = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(bean);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	ReferenciaFreteCarreteiro bean = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(bean);
    	super.beforeRemoveById(id);
    }

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	
	
   }