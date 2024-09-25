package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.dto.PipelineClienteSimulacaoDTO;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.PipelineClienteSimulacao;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.PipelineClienteSimulacaoDAO;

public class PipelineClienteSimulacaoService extends CrudService<PipelineClienteSimulacao, Long>{
	
	private PipelineClienteService pipelineClienteService;
	private TabelaPrecoService tabelaPrecoService;
	private SimulacaoService simulacaoService;
	private PipelineEtapaService pipelineEtapaService;
	
	public void setPipelineClienteSimulacaoDao(PipelineClienteSimulacaoDAO pipelineClienteSimulacaoDAO) {
		setDao(pipelineClienteSimulacaoDAO);
	}
	
	private PipelineClienteSimulacaoDAO getPipelineClienteSimulacaoDao() {
		return (PipelineClienteSimulacaoDAO) getDao();
	}
	
	public Boolean findExistenciaTabelaOuSimulacaoByPipelineCliente(PipelineCliente pipelineCliente) {
		return getPipelineClienteSimulacaoDao().findExistenciaTabelaOuSimulacaoByPipelineCliente(pipelineCliente);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getPipelineClienteSimulacaoDao().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List list = AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList());
		rsp.setList(list);
		return rsp;
	}

	public void storeByMap(TypedFlatMap map) {
		PipelineCliente pipelineClienteLoaded = pipelineClienteService.findById(map.getLong("idPipelineCliente"));
		TabelaPreco tabelaPrecoLoaded = carregaTabelaPreco(map.getLong("tabelaPreco.idTabelaPreco"));
		Simulacao simulacao = findByNrPropostaAndClienteAndIdFilial(map.getLong("nrProposta")
				,map.getLong("cliente.idCliente"), map.getLong("filial.idFilial"));
		Long idPipelineClienteSimulacao = map.getLong("idPipelineClienteSimulacao");
		
		PipelineClienteSimulacao pipelineClienteSimulacao = new PipelineClienteSimulacao();
		pipelineClienteSimulacao.setIdPipelineClienteSimulacao(idPipelineClienteSimulacao);
		pipelineClienteSimulacao.setPipelineCliente(pipelineClienteLoaded);
		pipelineClienteSimulacao.setTabelaPreco(tabelaPrecoLoaded);
		pipelineClienteSimulacao.setSimulacao(simulacao);
		
		storePipelineClienteSimulacao(pipelineClienteSimulacao);
	}

	private TabelaPreco carregaTabelaPreco(Long idTabelaPreco) {
		if (idTabelaPreco == null) {
			return null;
		}
		return tabelaPrecoService.findByIdTabelaPreco(idTabelaPreco);
	}

	private void storePipelineClienteSimulacao(PipelineClienteSimulacao pipelineClienteSimulacao) {
		validaPipelineClienteSimulacao(pipelineClienteSimulacao);
		super.store(pipelineClienteSimulacao);
	}

	private void validaPipelineClienteSimulacao(PipelineClienteSimulacao pipelineClienteSimulacao) {
		if (pipelineClienteSimulacao.getSimulacao() == null && pipelineClienteSimulacao.getTabelaPreco() == null) {
			throw new BusinessException("LMS-01213");
		}
		
		if (pipelineClienteSimulacao.getTabelaPreco() != null 
				&& !"D".equals(pipelineClienteSimulacao.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
				&& !"F".equals(pipelineClienteSimulacao.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco())) {
			throw new BusinessException("LMS-01214");
		}
	}

	private Simulacao findByNrPropostaAndClienteAndIdFilial(Long nrProposta, Long idCliente, Long idFilial) {
		if (nrProposta != null && idCliente != null && idFilial != null) {
			Simulacao simulacao = simulacaoService.findByNrPropostaAndClienteAndIdFilial(nrProposta, idCliente, idFilial);
			if (simulacao == null) {
				throw new BusinessException("LMS-01215");
			}
			return simulacao;
		}
		return null;
	}
	
	public void removeByIds(List ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			Long idPipelineClienteSimulacao = (Long) ids.get(0);
			pipelineEtapaService.removeEtapaPrimeiroEmbarqueAndEfetivacaoByIdPipelineClienteSimulacao(idPipelineClienteSimulacao);
			for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
				Long id = (Long) iterator.next();
				this.removeById(id);
			}
		}
	}
	
	public void removeById(Long idPipelineClienteSimulacao) {
		super.removeById(idPipelineClienteSimulacao);
	}
	
	public List findPipelineEtapaByIdPipelineCliente(Long idPipelineCliente) {
		return getPipelineClienteSimulacaoDao().findPipelineEtapaByIdPipelineCliente(idPipelineCliente);
	}
	
	public PipelineClienteSimulacaoDTO findToEdit(Long idPipelineClienteSimulacao) {
		return getPipelineClienteSimulacaoDao().findToEdit(idPipelineClienteSimulacao);
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getPipelineClienteSimulacaoDao().getRowCountCustom(criteria);
		return rowCountCustom;
	}
	
	public void setPipelineClienteService(
			PipelineClienteService pipelineClienteService) {
		this.pipelineClienteService = pipelineClienteService;
	}
	
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}
	
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	
	public void setPipelineEtapaService(PipelineEtapaService pipelineEtapaService) {
		this.pipelineEtapaService = pipelineEtapaService;
	}

}
