package com.mercurio.lms.tabelaprecos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tabelaprecos.model.ReajusteDivisaoCliente;
import com.mercurio.lms.tabelaprecos.model.dao.ReajusteDivisaoClienteDAO;

public class ReajusteDivisaoClienteService extends CrudService<ReajusteDivisaoCliente, Long> {
	
	@Override
	public java.io.Serializable store(ReajusteDivisaoCliente bean) {
		return super.store(bean);
	}
	
	@Override
	public ReajusteDivisaoCliente findById(java.lang.Long id) {
		return (ReajusteDivisaoCliente) super.findById(id); 
	}
		
    private ReajusteDivisaoClienteDAO getReajusteDivisaoClienteDAO() {
        return (ReajusteDivisaoClienteDAO) getDao();
    }
    
    public void setReajusteDivisaoClienteDAO(ReajusteDivisaoClienteDAO dao) {
        setDao( dao );
    }
    
 }