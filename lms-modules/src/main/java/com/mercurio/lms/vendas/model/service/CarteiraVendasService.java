package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.CarteiraVendas;
import com.mercurio.lms.vendas.model.CarteiraVendasCliente;
import com.mercurio.lms.vendas.model.PromotorCliente;
import com.mercurio.lms.vendas.model.dao.CarteiraVendasDAO;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;
/**
 * 
 * @author vagnerh
 * @spring.bean id="lms.vendas.carteiraVendasService"
 */
public class CarteiraVendasService extends CrudService <CarteiraVendas,Long>{
	private PromotorClienteService promotorClienteService;
	private CarteiraVendasClienteService carteiraVendasClienteService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public PromotorClienteService getPromotorClienteService() {
		return promotorClienteService;
	}

	public void setPromotorClienteService(
			PromotorClienteService promotorClienteService) {
		this.promotorClienteService = promotorClienteService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public CarteiraVendasClienteService getCarteiraVendasClienteService() {
		return carteiraVendasClienteService;
	}

	public void setCarteiraVendasClienteService(
			CarteiraVendasClienteService carteiraVendasClienteService) {
		this.carteiraVendasClienteService = carteiraVendasClienteService;
	}

	@Override
	public Integer getRowCount(Map criteria) {
		return getCarteiraVendasDAO().getRowCount(criteria);
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return getCarteiraVendasDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public CarteiraVendas findById(java.lang.Long id){
		return (CarteiraVendas) super.findById(id);
	}
	
	public void setCarteiraVendasDAO(CarteiraVendasDAO dao){
		setDao(dao);
	}
	
	public CarteiraVendasDAO getCarteiraVendasDAO(){
		return (CarteiraVendasDAO) getDao();
	}
	
	public CarteiraVendas executeAprovacao(Long idCarteiraVendas){
		//Verifica se existem registros de clientes para esta carteira
		if (getCarteiraVendasClienteService().findClientesCarteira(idCarteiraVendas).size() == 0){
			throw new BusinessException("LMS-01057");
		}
		
		//Gera a pendencia de aprovação
		CarteiraVendas carteira = findById(idCarteiraVendas);
		Pendencia pendencia = geraPendenciaAprovacaoCarteira(carteira);
		
		//Atualiza a carteira de vendas
		carteira.setPendencia(pendencia);
		carteira.setTpSituacaoAprovacao(pendencia.getTpSituacaoPendencia());
		store(carteira);
		return carteira;
	}
	/**
	 * Gera uma pendencia para a atualização da aprovação da carteira de vendas informada
	 * 
	 * @param carteiraVendas
	 * @return
	 */
	private Pendencia geraPendenciaAprovacaoCarteira(CarteiraVendas carteiraVendas){
		Object[] args = new Object[]{carteiraVendas.getUsuario().getNmUsuario(),
				carteiraVendas.getDtInicioLote(),carteiraVendas.getDtFimLote()};
		Pendencia pendencia = workflowPendenciaService.generatePendencia(
				SessionUtils.getFilialSessao().getIdFilial(),
				ConstantesWorkflow.NR103_APROVACAO_CARTEIRA_VENDAS,
				carteiraVendas.getIdCarteiraVendas(),
				configuracoesFacade.getMensagem("aprovacaoCarteiraVenda", args),
				new DateTime());
		
		return pendencia;
	}
	
	public String executeWorkflow(List<Long> listIds,List<String> listTpSituacao){
		Iterator<String> situacaoIt = listTpSituacao.iterator();
		for(Long id:listIds){
			CarteiraVendas carteira = findById(id);
			if (carteira != null){
				carteira.setTpSituacaoAprovacao(new DomainValue(situacaoIt.next()));
				store(carteira);
			}
		}
		return null;
	}
	
	public CarteiraVendas executeEfetivarCarteira(Long idCarteiraVendas){
		CarteiraVendas carteiraVendas = findById(idCarteiraVendas);
		List<CarteiraVendasCliente> clientes = getCarteiraVendasClienteService().findClientesCarteira(carteiraVendas);
		if (!carteiraVendas.getBlEfetivadoNivel1()){
			for (CarteiraVendasCliente cliente: clientes){
				if (!cliente.getTpComissao().getValue().equals("M")){
					continue;
				}

				/*
				 * Busca o registro de PROMOTOR_CLIENTE para a modal e abrangência do cliente,
				 * se não possui, cadastra.		// 
				 */
				Map criteria = new HashMap();
				criteria.put("cliente.idCliente", cliente.getCliente().getIdCliente());
				criteria.put("tpModal", cliente.getTpModal());
				criteria.put("tpAbrangencia",cliente.getTpAbrangencia());
				criteria.put("usuario.idUsuario",cliente.getCarteiraVendas().getUsuario().getIdUsuario());
				List<PromotorCliente> promotores = getPromotorClienteService().find(criteria);
				if (promotores == null || promotores.size() == 0){
					//Cadastra novo promotor cliente
					PromotorCliente pc = fillNewBasicPromotor(cliente);
					pc.setDtInclusao(new YearMonthDay());
					promotorClienteService.store(pc);
				}else{
					//atualiza
					PromotorCliente pc = promotores.get(0);
					pc.setUsuario(cliente.getCarteiraVendas().getUsuario());
					pc.setDtInclusao(new YearMonthDay());
					pc.setPcRateioComissao(new BigDecimal("100"));
					pc.setDtInicioPromotor(cliente.getDtPromotor());
					promotorClienteService.store(pc);
				}
				
				carteiraVendasClienteService.removeById(cliente.getIdCarteiraVendasCliente());
			}
			
			Pendencia pendencia = geraPendenciaAprovacaoCarteira(carteiraVendas); 
			carteiraVendas.setPendencia(pendencia);
			carteiraVendas.setTpSituacaoAprovacao(pendencia.getTpSituacaoPendencia());
			carteiraVendas.setBlEfetivadoNivel1(Boolean.TRUE);
			
			store(carteiraVendas);
		}else{
			for (CarteiraVendasCliente cliente:clientes){
				String tpComissao = cliente.getTpComissao().getValue();
				
				Map criteria = new HashMap();
				criteria.put("cliente.idCliente", cliente.getCliente().getIdCliente());
				criteria.put("tpModal", cliente.getTpModal());
				criteria.put("tpAbrangencia",cliente.getTpAbrangencia());
				criteria.put("usuario.idUsuario",cliente.getCarteiraVendas().getUsuario().getIdUsuario());
				List<PromotorCliente> promotores = getPromotorClienteService().find(criteria);
				
				if (promotores == null || promotores.size() == 0){
					PromotorCliente pc = fillNewBasicPromotor(cliente);
					pc.setDtInclusao(new YearMonthDay());
					pc.setDtReconquista(cliente.getDtPromotor());
					promotorClienteService.store(pc);
				}else{
					PromotorCliente pc = promotores.get(0);
					pc.setUsuario(cliente.getCarteiraVendas().getUsuario());
					pc.setDtInicioPromotor(cliente.getDtPromotor().minusDays(1));
					pc.setPcRateioComissao(new BigDecimal("100"));
					pc.setDtInclusao(new YearMonthDay());
					pc.setDtReconquista(cliente.getDtPromotor());
					pc.setTpAbrangencia(cliente.getTpAbrangencia());
					pc.setTpModal(cliente.getTpModal());
					promotorClienteService.store(pc);
					
				}
				carteiraVendasClienteService.removeById(cliente.getIdCarteiraVendasCliente());
			}
			carteiraVendas.setBlEfetivadoNivel2(Boolean.TRUE);
			store(carteiraVendas);
		}
		return carteiraVendas;
	}

	private PromotorCliente fillNewBasicPromotor(CarteiraVendasCliente cliente) {
		PromotorCliente pc = new PromotorCliente();
		pc.setCliente(cliente.getCliente());
		pc.setUsuario(cliente.getCarteiraVendas().getUsuario());
		pc.setDtPrimeiroPromotor(cliente.getCliente().getPessoa().getDhInclusao().toYearMonthDay());
		pc.setDtInicioPromotor(cliente.getDtPromotor().minusDays(1));
		pc.setPcRateioComissao(new BigDecimal("100"));
		pc.setTpAbrangencia(cliente.getTpAbrangencia());
		pc.setTpModal(cliente.getTpModal());
		return pc;
	}
}
