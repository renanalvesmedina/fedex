package com.mercurio.lms.tributos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.tributos.model.EmbasamentoLegalIcms;
import com.mercurio.lms.tributos.model.dao.EmbasamentoLegalIcmsDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.embasamentoLegalIcmsService"
 */
public class EmbasamentoLegalIcmsService extends CrudService<EmbasamentoLegalIcms, Long> {
	
	private static final String NR_TAMANHO_EMB_LEGAL_RES = "NR_TAMANHO_EMB_LEGAL_RES";
	private ParametroGeralService parametroGeralService;
	private AliquotaIcmsService aliquotaIcmsService;
	
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public void removeById(Long id) {
		super.removeById(id);
	}	

	public java.io.Serializable store(EmbasamentoLegalIcms bean) {
		return super.store(bean);
	}
				
	public EmbasamentoLegalIcms findById(Long id) {		
		return getEmbasamentoLegalIcmsDAO().findById(id);
	}
	
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	ResultSetPage paginate = getEmbasamentoLegalIcmsDAO().findPaginated(criteria); 
    	return paginate;
    }	
	
    public List findEmbasamentoLookup(TypedFlatMap criteria){
    	return getEmbasamentoLegalIcmsDAO().findEmbasamentoLookup(criteria);
    }
    
    public String findParameterSizeEmb(){
    	return parametroGeralService.findDsConteudoByNmParametro(NR_TAMANHO_EMB_LEGAL_RES);
    }
    
    /**
     * Obtem o proximo codigo do embasamento através do CD_EMB_LEGAL_MASTERSAF
     * @return Long
     */
    public Long findCdEmbasamento(){
		return getParametroGeralService().generateValorParametroSequencial("CD_EMB_LEGAL_MASTERSAF", false, 1);
    }    
    
    public List findAlquotaByEmbasamento(Long idEmbasamento){
    	return aliquotaIcmsService.findAlquotaByEmbasamento(idEmbasamento);
    }
        
    private EmbasamentoLegalIcmsDAO getEmbasamentoLegalIcmsDAO() {
        return (EmbasamentoLegalIcmsDAO) getDao();
    }
    
    public void setEmbasamentoLegalIcmsDAO(EmbasamentoLegalIcmsDAO dao) {
        setDao(dao);
    }

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public AliquotaIcmsService getAliquotaIcmsService() {
		return aliquotaIcmsService;
	}

	public void setAliquotaIcmsService(AliquotaIcmsService aliquotaIcmsService) {
		this.aliquotaIcmsService = aliquotaIcmsService;
	}
    
}