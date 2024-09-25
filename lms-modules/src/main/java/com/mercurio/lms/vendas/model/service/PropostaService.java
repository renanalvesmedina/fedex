package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.Proposta;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.PropostaDAO;

/**
 * @spring.bean id="lms.vendas.propostaService"
 */
public class PropostaService extends CrudService<Proposta, Long> {

	private SimulacaoService simulacaoService;

	public Proposta findByIdSimulacao(Long idSimulacao) {
		return getPropostaDAO().findByIdSimulacao(idSimulacao);
	}

	@Override
	public Proposta findById(Long id) {
		return (Proposta)super.findById(id);
	}

	@Override
	protected void removeById(Long id) {
		super.removeById(id);
	}
	
	public Serializable storeCancelWorkflow(Proposta proposta) {
		/** Caso alterado parametros gerados, verifica se deve liberar Simulacao para nova Aprovação */
		Simulacao simulacao = simulacaoService.findById(proposta.getSimulacao().getIdSimulacao());
		if(!Boolean.TRUE.equals(simulacao.getBlEfetivada())) {
			simulacaoService.storeCancelaPendenciaProposta(simulacao);
		}
		return super.store(proposta);
	}

	@Override
	public Serializable store(Proposta bean) {
		return super.store(bean);
	}

	public void removeByIdSimulacao(Long idSimulacao) {
		getPropostaDAO().removeByIdSimulacao(idSimulacao);
	}

	public void setPropostaDAO(PropostaDAO dao){
		setDao(dao);
	}
	private PropostaDAO getPropostaDAO() {
		return (PropostaDAO) getDao();
	}
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}
	
	public List<Map<String, Object>> findRelatorioPropostaPromocional2a15(Long idTabelaPreco, Long idClienteProposta, String orderBy){
		return getPropostaDAO().findRelatorioPropostaPromocional2a15(idTabelaPreco, idClienteProposta, orderBy);
	}
	
	public List<Map<String, Object>> findRelatorioPropostaPromocional16a30(Long idTabelaPreco, Long idClienteProposta, String orderBy){
		return getPropostaDAO().findRelatorioPropostaPromocional16a30(idTabelaPreco, idClienteProposta, orderBy);
	}
	
	public List<Map<String, Object>> findRelatorioPropostaPromocionalProdutoEspecifico(Long idSimulacao, Long idCliente, String orderBy){
		return getPropostaDAO().findRelatorioPropostaPromocionalProdutoEspecifico(idSimulacao, idCliente, orderBy);
	}

}