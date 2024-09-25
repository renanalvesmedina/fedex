package com.mercurio.lms.tributos.model.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.tributos.model.DescricaoTributacaoIcms;
import com.mercurio.lms.tributos.model.ObservacaoICMS;
import com.mercurio.lms.tributos.model.dao.DescricaoTributacaoIcmsDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.descricaoTributacaoIcmsService"
 */
public class DescricaoTributacaoIcmsService extends CrudService<DescricaoTributacaoIcms, Long> {


	private ParametroGeralService parametroGeralService;
	
	/**
	 * Recupera uma inst�ncia de <code>DescricaoTributacaoIcms</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public DescricaoTributacaoIcms findById(java.lang.Long id) {
        return (DescricaoTributacaoIcms)super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(DescricaoTributacaoIcms bean) {
        return super.store(bean);
    }
    
    public DescricaoTributacaoIcms store(DescricaoTributacaoIcms bean, ItemList items) {
    	
    	boolean rollbackMasterId = bean.getIdDescricaoTributacaoIcms() == null;
    	
    	try {
    		
    		List<ObservacaoICMS> obsList = items.getNewOrModifiedItems();    		
    		for(ObservacaoICMS ob : obsList){
    			if(StringUtils.isBlank(ob.getCdEmbLegalMastersaf())){
    				ob.setCdEmbLegalMastersaf(getCodigoEmbasamento().toString());
    			}
    		}
    		    		
			if (!items.hasItems())
				throw new BusinessException("LMS-23018");
			
			this.beforeStore(bean);
			bean = this.getDescricaoTributacaoIcmsDAO().store(bean,items);
			
		} catch (RuntimeException e) {
			this.rollbackMasterState(bean, rollbackMasterId, e);
            items.rollbackItemsState();
            throw e;			
		}
		return bean;
    }
    
    
	/**
	 * Obtem o codigo do embsamento Atualizado
	 * @return
	 */
	private Long getCodigoEmbasamento(){
		return getParametroGeralService().generateValorParametroSequencial("CD_EMB_LEGAL_MASTERSAF", false, 1);
	}
    
    
	public List findItem(Long idDescricaoTributacaoIcms) {
		return getDescricaoTributacaoIcmsDAO().findItem(idDescricaoTributacaoIcms);
	}
	
	public Integer getRowCountItem(Long idDescricaoTributacaoIcms){
		return getDescricaoTributacaoIcmsDAO().getRowCountItem(idDescricaoTributacaoIcms);
	}

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setDescricaoTributacaoIcmsDAO(DescricaoTributacaoIcmsDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private DescricaoTributacaoIcmsDAO getDescricaoTributacaoIcmsDAO() {
        return (DescricaoTributacaoIcmsDAO) getDao();
    }
    
    public void validateVigenciaItens(ObservacaoICMS obICMS, Iterator iter) {
    	for (;iter.hasNext();) {
    		ObservacaoICMS obICMSOld = (ObservacaoICMS)iter.next();
    		if (obICMS.getTpObservacaoICMS().getValue().equals(obICMSOld.getTpObservacaoICMS().getValue()) &&
    			obICMS.getNrOrdemImpressao().equals(obICMSOld.getNrOrdemImpressao()) && 
    			!obICMSOld.getIdObservacaoICMS().equals(obICMS.getIdObservacaoICMS())) {
    			YearMonthDay dtInicialNew = obICMS.getDtVigenciaInicial();
    			YearMonthDay dtInicialOld = obICMSOld.getDtVigenciaInicial();
    			YearMonthDay dtFinalNew = JTDateTimeUtils.maxYmd(obICMS.getDtVigenciaFinal());
    			YearMonthDay dtFinalOld = JTDateTimeUtils.maxYmd(obICMSOld.getDtVigenciaFinal());
    			if ((dtInicialNew.isAfter(dtInicialOld) && dtInicialNew.isBefore(dtFinalOld)) ||
    				(dtFinalNew.isAfter(dtInicialOld) && dtFinalNew.isBefore(dtFinalOld)) ||
    				(dtInicialNew.isBefore(dtInicialOld) && dtFinalNew.isAfter(dtFinalOld)) ||
    				(dtInicialNew.isEqual(dtInicialOld)) || (dtFinalNew.isEqual(dtFinalOld))){
    				throw new BusinessException("LMS-23019");
    			}
    		}
    	}
    }
    
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
    
   }