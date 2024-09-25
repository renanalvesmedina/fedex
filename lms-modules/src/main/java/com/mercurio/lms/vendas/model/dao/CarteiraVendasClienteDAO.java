package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.CarteiraVendas;
import com.mercurio.lms.vendas.model.CarteiraVendasCliente;
import com.mercurio.lms.vendas.model.PromotorCliente;
/**
 * @author Vagner Huzalo
 * @spring.bean
 */
public class CarteiraVendasClienteDAO extends BaseCrudDao {
	
	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("cliente", FetchMode.SELECT);
		super.initFindListLazyProperties(lazyFindList);
	}

	@Override
	protected Class getPersistentClass() {
		return CarteiraVendasCliente.class;
	}

	/**
	 * @param criteria
	 * @return
	 */
	public Map findDataPrimeiroPromotor(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("pc.dtPrimeiroPromotor");
		hql.addFrom(PromotorCliente.class.getName(), "pc");
		hql.addCriteria("pc.cliente.idCliente", "=", criteria.getLong("idCliente"));
		hql.addCriteria("pc.tpAbrangencia", "=", criteria.getString("tpAbrangencia"));
		hql.addCriteria("pc.tpModal", "=", criteria.getString("tpModal"));
		
		return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}	

	
	/**
	 * Busca todos os registros de <code>CARTEIRA_VENDA_CLIENTE</code> que estão relacionados com a CarteiraVenda
	 * informada.
	 * 
	 * @param carteiraVendas
	 * @return
	 */
	public List<CarteiraVendasCliente> findClientesCarteiraVenda(CarteiraVendas carteiraVendas){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("cliente");
		hql.addInnerJoin(CarteiraVendasCliente.class.getName(),"cliente");
		hql.addInnerJoin("fetch cliente.cliente", "cli");
		hql.addCriteria("cliente.carteiraVendas","=",carteiraVendas);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	
	/**
	 * Busca os clientes da carteira de vendas fazendo join com PromotorCliente para retornar
	 * a data do primeiro promotor para cada cliente.
	 * 
	 * @param idCarteira
	 * @return
	 */
	public List findClientesCarteiraVenda(Long idCarteira) {
		SqlTemplate sql = mountSqlClientes(idCarteira);
		
		sql.addProjection("clienteCarteira");
		
		List list = new ArrayList();		
		
		List<CarteiraVendasCliente> result = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()); 
		for (CarteiraVendasCliente cliente :result){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("idCarteiraVendasCliente", cliente.getIdCarteiraVendasCliente());
			map.put("idCliente",cliente.getCliente().getIdCliente());
			map.put("nmPessoa",cliente.getCliente().getPessoa().getNmPessoa());
			map.put("tpCliente",cliente.getCliente().getTpCliente().getDescriptionAsString());
			map.put("nrIdentificacao",cliente.getCliente().getPessoa().getNrIdentificacao());
			map.put("tpAbrangencia",cliente.getTpAbrangencia().getValue());
			map.put("tpModal",cliente.getTpModal().getValue());
			map.put("tpComissao",cliente.getTpComissao().getValue());
			map.put("dtPromotor",cliente.getDtPromotor());
			map.put("dtCliente",cliente.getCliente().getPessoa().getDhInclusao().toYearMonthDay());
			list.add(map);
		}
		return list;
	}
	
	private SqlTemplate mountSqlClientes(Long idCarteira){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(CarteiraVendasCliente.class.getName()+" clienteCarteira "+
         "INNER JOIN  clienteCarteira.carteiraVendas as carteira "+ 
         "INNER JOIN  clienteCarteira.cliente as cliente "+ 
         "INNER JOIN  cliente.pessoa as pessoa");		
		hql.addCriteria("carteira.idCarteiraVendas", "=", idCarteira);
		return hql;
	}
	
	public Map findDtPrimeiroPromotor(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("pc.dtPrimeiroPromotor","dtPrimeiroPromotor");
		hql.addFrom(PromotorCliente.class.getName(),"pc");
		hql.addCriteria("pc.cliente.idCliente", "=", criteria.getLong("idCliente"));
		hql.addCriteria("pc.tpAbrangencia", "=", criteria.getString("tpAbrangencia"));
		hql.addCriteria("pc.tpModal", "=", criteria.getString("tpModal"));
		
		Map ret = new HashMap();
		YearMonthDay dtPrimeiroPromotor = (YearMonthDay)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		
		ret.put("dtPrimeiroPromotor",dtPrimeiroPromotor);
		ret.put("rowIndex", criteria.getLong("rowIndex"));
		
		return ret;
	}
}

