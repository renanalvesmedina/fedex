package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.CarteiraVendas;
import com.mercurio.lms.vendas.model.CarteiraVendasCliente;
import com.mercurio.lms.vendas.model.dao.CarteiraVendasClienteDAO;
/**
 * 
 * @author vagnerh
 * @spring.bean id="lms.vendas.carteiraVendasClienteService"
 */
public class CarteiraVendasClienteService extends CrudService {
	
	public List<Map<String, Object>> findClientesCarteira(long idCarteira){
		return getCarteiraVendasClienteDAO().findClientesCarteiraVenda(idCarteira);
	}
	
	public List<CarteiraVendasCliente> findClientesCarteira(CarteiraVendas carteiraVendas){
		return getCarteiraVendasClienteDAO().findClientesCarteiraVenda(carteiraVendas);
	}
	
	public void removeClientesByIds(List ids){
		getCarteiraVendasClienteDAO().removeByIds(ids);
	}
	
	public Map findDataPrimeiroPromotor(TypedFlatMap criteria){
		return getCarteiraVendasClienteDAO().findDtPrimeiroPromotor(criteria);
	}
	
	@Override
	public void removeById(Serializable id) {
		super.removeById(id);
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}

	@Override
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}
	
	public Serializable store(Serializable bean) {
		return super.store(bean);
	}

	public void setCarteiraVendasClienteDAO(CarteiraVendasClienteDAO dao){
		this.setDao(dao);
	}
	
	public CarteiraVendasClienteDAO getCarteiraVendasClienteDAO(){
		return (CarteiraVendasClienteDAO) this.getDao();
	}

}
