package com.mercurio.lms.configuracoes.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.DiaFaturamentoEmpresa;
import com.mercurio.lms.configuracoes.model.dao.DiaFaturamentoEmpresaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.diaFaturamentoEmpresaService"
 */
public class DiaFaturamentoEmpresaService extends CrudService<DiaFaturamentoEmpresa, Long> {

	private DomainValueService domainValueService ;

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	/**
	 * Recupera uma instância de <code>DiaFaturamentoEmpresa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public DiaFaturamentoEmpresa findById(java.lang.Long id) {
        return (DiaFaturamentoEmpresa)super.findById(id);
    }

    public ResultSetPage findPaginated(Map criteria) {
        ResultSetPage rsp = super.findPaginated(criteria);
        List diasSemana = getDomainValueService().findDomainValues("DM_DIAS_SEMANA");
        List diasFaturamento = rsp.getList();
        DomainValue nmSemana ; 
        for (Iterator iter = diasFaturamento.iterator(); iter.hasNext();) {
        	DiaFaturamentoEmpresa dfe = (DiaFaturamentoEmpresa) iter.next();
        	if (dfe.getDdCorte()!= null) {
        		if ("S".equals(dfe.getTpPeriodicidade().getValue())) {
        			int pos = dfe.getDdCorte().intValue() == 7 ? 0 : dfe.getDdCorte().intValue();
        			nmSemana = (DomainValue)diasSemana.get(pos);
        			dfe.setDdCorteExt(nmSemana.getDescription().getValue());  	
        		} else {
        	   	    dfe.setDdCorteExt(dfe.getDdCorte().toString());
        		}    
        	}   	
        }	
        rsp.setList(diasFaturamento);
        return rsp;
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
	 */
    public java.io.Serializable store(DiaFaturamentoEmpresa bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDiaFaturamentoEmpresaDAO(DiaFaturamentoEmpresaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DiaFaturamentoEmpresaDAO getDiaFaturamentoEmpresaDAO() {
        return (DiaFaturamentoEmpresaDAO) getDao();
    }
   }