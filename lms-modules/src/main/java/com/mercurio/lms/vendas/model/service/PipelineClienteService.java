package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.PipelineClienteSimulacao;
import com.mercurio.lms.vendas.model.PipelineEtapa;
import com.mercurio.lms.vendas.model.PipelineReceita;
import com.mercurio.lms.vendas.model.Visita;
import com.mercurio.lms.vendas.model.dao.PipelineClienteDAO;
import com.mercurio.lms.vendas.util.PipelineClienteHelper;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.pipelineClienteService"
 */
public class PipelineClienteService extends CrudService<PipelineCliente, Long> {
	private PipelineReceitaService pipelineReceitaService;
	private VisitaService visitaService;
	private PipelineEtapaService pipelineEtapaService;
	private PipelineClienteSimulacaoService pipelineClienteSimulacaoService;
	
	public VisitaService getVisitaService() {
		return visitaService;
	}

	public void setVisitaService(VisitaService visitaService) {
		this.visitaService = visitaService;
	}

	public PipelineEtapaService getPipelineEtapaService() {
		return pipelineEtapaService;
	}

	public void setPipelineEtapaService(PipelineEtapaService pipelineEtapaService) {
		this.pipelineEtapaService = pipelineEtapaService;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getPipelineClienteDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List list = AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList());
		rsp.setList(list);
		return rsp;
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return this.getPipelineClienteDAO().getRowCount(criteria);
	}

	/**
	 * Recupera uma instância de <code>PipelineCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public PipelineCliente findById(java.lang.Long id) {
		return (PipelineCliente)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		List<PipelineReceita> pipList = pipelineReceitaService.findPipelineReceitaByPipelineCliente(id);
		List<PipelineEtapa> pipListEtapa = pipelineEtapaService.findPipelineEtapaByIdPipelineCliente(id);
		List<PipelineClienteSimulacao> pipSimulacao = pipelineClienteSimulacaoService.findPipelineEtapaByIdPipelineCliente(id);
		
		if (CollectionUtils.isNotEmpty(pipList)) {
			for(PipelineReceita pipelineReceita : pipList) {
				   pipelineReceitaService.removeById(pipelineReceita.getIdPipelineReceita());
			}
		}
		if (CollectionUtils.isNotEmpty(pipListEtapa)) {
			for(PipelineEtapa pipelineEtapa : pipListEtapa) {
				   pipelineEtapaService.removeById(pipelineEtapa.getIdPipelineEtapa());
			}
		}
				
		if (CollectionUtils.isNotEmpty(pipSimulacao)) {
			for (PipelineClienteSimulacao pipelineClienteSimulacao : pipSimulacao) {
				pipelineClienteSimulacaoService.removeById(pipelineClienteSimulacao.getIdPipelineClienteSimulacao());
		}
		}
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
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
	public TypedFlatMap store(PipelineCliente bean) {
		TypedFlatMap mapaIds = new TypedFlatMap();
		validaDatasFechamentoPipelineCliente(bean);
		Serializable idPipelineCliente = super.store(bean);
		mapaIds.put("idPipelineCliente", idPipelineCliente);
		
		
		
		List<PipelineReceita> pipList = bean.getListPipelineReceita();
		int i=1;
		
		if (pipList != null && !pipList.isEmpty()) {
			for(PipelineReceita pipelineReceita : pipList) {
				pipelineReceita.setPipelineCliente(bean);

				//remove os servicos oferecidos se nao for uma etapa nova
				if(pipelineReceita.getIdPipelineReceita() != null) {
					pipelineReceitaService.removeById(pipelineReceita.getIdPipelineReceita());
					pipelineReceita.setIdPipelineReceita(null);
				}
				
				//salva o pipeline Receita
				if(pipelineReceita.getTpAbrangencia()!= null && pipelineReceita.getTpModal()!=null && pipelineReceita.getVlReceita()!=null){
					Serializable idPipelineReceita = pipelineReceitaService.store(pipelineReceita);
					mapaIds.put("idPipelineReceita"+i, idPipelineReceita);
				}
				
				if(pipList.size()==1 && (pipelineReceita.getTpAbrangencia()==null || pipelineReceita.getTpModal()==null || pipelineReceita.getVlReceita()==null)){
					throw new BusinessException("LMS-01053");
				}
				i++;
			}
		}
		
		return mapaIds;
	}
	
	private void validaDatasFechamentoPipelineCliente(PipelineCliente bean) {
		YearMonthDay today = JTDateTimeUtils.getDataAtual().dayOfMonth().withMinimumValue();
		
		YearMonthDay dtFechamento = PipelineClienteHelper.createYearMonthDayByFechamento(bean);
		YearMonthDay dtFechamentoAtualizado = PipelineClienteHelper.createYearMonthDayByFechamentoAtualizado(bean);

		if (bean.getIdPipelineCliente() == null && dtFechamento != null && dtFechamento.isBefore(today)) {
			throw new BusinessException("LMS-01209");
		}
		
		if ((dtFechamento != null && dtFechamentoAtualizado != null) && 
				(dtFechamentoAtualizado.isBefore(dtFechamento) || dtFechamentoAtualizado.isEqual(dtFechamento))) {
			throw new BusinessException("LMS-01210");
		}
		
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable storeEtapas(PipelineEtapa bean) {

		if(bean.getVisita().getIdVisita()!= null){
			Visita v = getVisitaService().findById(bean.getVisita().getIdVisita());
			bean.setVisita(v);
		}
		//remove os servicos oferecidos se nao for uma etapa nova
		if(bean.getIdPipelineEtapa() != null) {
			getPipelineEtapaService().removeById(bean.getIdPipelineEtapa());
			bean.setIdPipelineEtapa(null);
		}
		return getPipelineEtapaService().storePipeLineEtapa(bean);
	}

	public void storeAndCancel(PipelineCliente pipelineCliente) {
		pipelineCliente.setTpSituacao(new DomainValue("C"));
		super.store(pipelineCliente);
	}
			
	public PipelineCliente findByIdAndInitialeProperties(Long idPipelineCliente) {
		PipelineCliente pipelineCliente = findById(idPipelineCliente);
		if (pipelineCliente != null && pipelineCliente.getCliente() != null) {
			Hibernate.initialize(pipelineCliente.getCliente());
			if (pipelineCliente.getCliente().getCliente() != null) {
				Hibernate.initialize(pipelineCliente.getCliente().getCliente().getPessoa());
		}
	}
		return pipelineCliente;
	}
			
	private final PipelineClienteDAO getPipelineClienteDAO() {
		return (PipelineClienteDAO) getDao();
	}
	public void setPipelineClienteDAO(PipelineClienteDAO dao) {
		setDao( dao );
	}

	public PipelineReceitaService getPipelineReceitaService() {
		return pipelineReceitaService;
	}

	public void setPipelineReceitaService(
			PipelineReceitaService pipelineReceitaService) {
		this.pipelineReceitaService = pipelineReceitaService;
	}
	
	public void setPipelineClienteSimulacaoService(PipelineClienteSimulacaoService pipelineClienteSimulacaoService) {
		this.pipelineClienteSimulacaoService = pipelineClienteSimulacaoService;
	}

}