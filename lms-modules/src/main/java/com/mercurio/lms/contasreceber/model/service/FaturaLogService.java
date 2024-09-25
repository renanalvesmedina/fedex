package com.mercurio.lms.contasreceber.model.service;

import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contasreceber.model.FaturaLog;
import com.mercurio.lms.contasreceber.model.dao.FaturaLogDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.faturaLogService"
 */
public class FaturaLogService extends CrudService<FaturaLog, Long> {
	
	public ResultSetPage findPaginated(Map criteria) {
		return getFaturaLogDAO().findPaginated(criteria,
				FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCount(Map criteria) {
		return getFaturaLogDAO().getRowCount(criteria);
	}
	
	public ResultSetPage findPaginatedItems(Map m) {

		return getFaturaLogDAO().findPaginatedItems(m, FindDefinition.createFindDefinition(m));
	}
	
	public Integer getRowCountItems(Map m) {
		
		return getFaturaLogDAO().getRowCountItems(m);
	}
	
	public FaturaLogDAO getFaturaLogDAO() {

        return (FaturaLogDAO) getDao();
    }
		  
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia
	 *            do DAO.
	 */
    public void setFaturaLogDAO(FaturaLogDAO dao) {

        setDao( dao );
    }
}