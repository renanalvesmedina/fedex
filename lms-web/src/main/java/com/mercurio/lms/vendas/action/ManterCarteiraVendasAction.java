package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.CarteiraVendas;
import com.mercurio.lms.vendas.model.CarteiraVendasCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.CarteiraVendasClienteService;
import com.mercurio.lms.vendas.model.service.CarteiraVendasService;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.PromotorClienteService;

/**
 * @author Vagner Huzalo
 * 
 * @spring.bean id="lms.vendas.manterCarteiraVendasAction"
 */

public class ManterCarteiraVendasAction extends CrudAction{
	private ClienteService clienteService;
	private CarteiraVendasClienteService carteiraVendasClienteService;
	private PromotorClienteService promotorClienteService;
	
	public PromotorClienteService getPromotorClienteService() {
		return promotorClienteService;
	}

	public void setPromotorClienteService(
			PromotorClienteService promotorClienteService) {
		this.promotorClienteService = promotorClienteService;
	}

	public CarteiraVendas findById(java.lang.Long id) {
		return this.getCarteiraVendasService().findById(id);
	}

	public Serializable store(CarteiraVendas carteiraVendas){
		return super.store(carteiraVendas);
	}

	public List findClientesCarteira(TypedFlatMap map){
		return getCarteiraVendasClienteService().findClientesCarteira(map.getLong("idCarteiraVendas"));
	}
	
	public Map findDataPrimeiroPromotor(TypedFlatMap criteria){
		return getCarteiraVendasClienteService().findDataPrimeiroPromotor(criteria);
	}
	
	public void removeClientes(TypedFlatMap map){
		List<Long> ids = new ArrayList<Long>();
		for (String id: (List<String>)map.getList("ids")){
			ids.add(Long.valueOf(id));
		}
		getCarteiraVendasClienteService().removeClientesByIds(ids);
	}
	
	public Map findBasicIncludeData(){
		Usuario usuario = SessionUtils.getUsuarioLogado();
		Map data = new HashMap();
		data.put("idUsuario", usuario.getIdUsuario());
		data.put("nmUsuario", usuario.getNmUsuario());
		data.put("nrMatricula", usuario.getNrMatricula());
		data.put("dtInicio", JTDateTimeUtils.getDataAtual());
		return data;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return getCarteiraVendasService().findPaginated(criteria);
	}

	/**
	 * Valida a carteira de vendas e gera a pendência de aprovação
	 * 
	 * @param criteria Mapa com o id da carteira de vendas
	 * @throws BusinessException LMS-01057
	 * @return 
	 */
	public CarteiraVendas executeAprovacao(TypedFlatMap criteria){
		return getCarteiraVendasService().executeAprovacao(criteria.getLong("idCarteiraVendas"));
	}
	
	
	public CarteiraVendas efetivarCarteira(Long idCarteiraVendas){
		return getCarteiraVendasService().executeEfetivarCarteira(idCarteiraVendas);
	}
	
	public Serializable storeClientes(TypedFlatMap data){
		CarteiraVendas carteiraVendas = getCarteiraVendasService().findById(data.getLong("idCarteiraVendas"));
		List<TypedFlatMap> clientes = data.getList("carteiraVendasCliente");
		for (TypedFlatMap map:clientes){
			if (map.getLong("idCliente") == null) continue;
			
			Long id = map.getLong("id");
			CarteiraVendasCliente cliente = new CarteiraVendasCliente();
			cliente.setCarteiraVendas(carteiraVendas);
			cliente.setCliente(clienteService.findByIdInitLazyProperties(map.getLong("idCliente"), false));
			cliente.setDtPromotor(map.getYearMonthDay("dtPromotor"));
			cliente.setTpModal(map.getDomainValue("tpModal"));
			cliente.setTpComissao(map.getDomainValue("tpComissao"));
			cliente.setTpAbrangencia(map.getDomainValue("tpAbrangencia"));
			if (id!=null){
				cliente.setIdCarteiraVendasCliente(id);
			}
			getCarteiraVendasClienteService().store(cliente);
		}
		return carteiraVendas.getIdCarteiraVendas();
	}
	
	/**
	 * Valida os dados do cliente inserido na carteira de vendas antes de inserir novo registro
	 * 
	 * @param data
	 */
	public TypedFlatMap validaCliente(TypedFlatMap data){
		YearMonthDay dtPromotor = data.getYearMonthDay("dtPromotor");
		YearMonthDay dtAtual = new YearMonthDay();
		if (dtPromotor.compareTo(dtAtual)>0 ||
				dtPromotor.compareTo(dtAtual.minusDays(15))<0){
			throw new BusinessException("LMS-01169");
		}
		return data;
	}
	
	public TypedFlatMap findClienteByIdentificacao(TypedFlatMap map){
		Cliente cliente = getClienteService().findByNrIdentificacao(map.getString("nrIdentificacao"));
		if (cliente!=null){
			map.put("idCliente", cliente.getIdCliente());
			map.put("nmPessoa", cliente.getPessoa().getNmPessoa());
			map.put("tpCliente", cliente.getTpCliente().getDescription());
			map.put("dtCliente", cliente.getPessoa().getDhInclusao().toYearMonthDay());
			
			/**
			 *  O elemento 'exception' é necessário aqui para que a exceção lançada
			 *  não invalide o retorno, que contém o rowIndex necessário para manipular 
			 *  a grid editável.
			 */
			map.put("exception", "");
		}else{
			map.put("exception","LMS-00061");
		}
		return map;
	}

	public CarteiraVendasClienteService getCarteiraVendasClienteService() {
		return carteiraVendasClienteService;
	}

	public void setCarteiraVendasClienteService(CarteiraVendasClienteService carteiraVendasClienteService) {
		this.carteiraVendasClienteService = carteiraVendasClienteService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public CarteiraVendasService getCarteiraVendasService() {
		return (CarteiraVendasService) defaultService;
	}

	public void setCarteiraVendasService(CarteiraVendasService carteiraVendasService) {
		defaultService = carteiraVendasService;
	}
}