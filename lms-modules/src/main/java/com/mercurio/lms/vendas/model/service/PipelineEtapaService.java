package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.tabelaprecos.model.DadosTabelaPrecoDTO;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.vendas.dto.DadosEfetivacaoPipelineDTO;
import com.mercurio.lms.vendas.dto.DadosEmbarquePipelineDTO;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.PipelineEtapa;
import com.mercurio.lms.vendas.model.PipelineReceita;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.Visita;
import com.mercurio.lms.vendas.model.dao.PipelineEtapaDAO;
import com.mercurio.lms.vendas.util.PipelineEtapaHelper;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.pipelineEtapaService"
 */
public class PipelineEtapaService extends CrudService<PipelineEtapa, Long> {

	private VisitaService visitaService;
	private PipelineClienteService pipelineClienteService;
	private PipelineReceitaService pipelineReceitaService;
	private TabelaPrecoService tabelaPrecoService; 
	private SimulacaoService simulacaoService;
	private ConhecimentoService conhecimentoService;
	private PipelineClienteSimulacaoService pipelineClienteSimulacaoService;

	public VisitaService getVisitaService() {
		return visitaService;
	}

	public void setVisitaService(VisitaService visitaService) {
		this.visitaService = visitaService;
	}

	/**
	 * Recupera uma Lista de PipelineReceita a partir do idPipelineCliente
	 * @param idPipelineCliente
	 * @return
	 */
	public List findPipelineEtapaByPipelineCliente(Long idPipelineCliente){
		return getPipelineEtapaDAO().findPipelineEtapaByPipelineCliente(idPipelineCliente);
	}

	/**
	 * Recupera uma Lista de PipelineReceita a partir do idPipelineCliente
	 * @param idPipelineCliente
	 * @return
	 */
	public List findPipelineEtapaByIdPipelineCliente(Long idPipelineCliente){
		return getPipelineEtapaDAO().findPipelineEtapaByIdPipelineCliente(idPipelineCliente);
	}



	/**
	 * Recupera uma instância de <code>PipelineReceita</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public PipelineEtapa findById(java.lang.Long id) {
		return (PipelineEtapa)super.findById(id);
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
	 * Apaga várias entidades através do IdVisita
	 */
	public void removeByIdPipelineCliente(Long idPipelineCliente){
		getPipelineEtapaDAO().removeByIdPipelineCliente(idPipelineCliente);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public TypedFlatMap storeEtapas(TypedFlatMap mapa) {
		PipelineCliente pc = new PipelineCliente();
		TypedFlatMap mapaIds= new TypedFlatMap();
		
		if(mapa.getLong("idPipelineCliente") != null) {
			pc.setIdPipelineCliente(mapa.getLong("idPipelineCliente"));
		}
		Boolean blEnableAbaPropostasTabelas = Boolean.TRUE;
		
		if(!verificaDataEventoEtapas(mapa)){
			throw new BusinessException("LMS-01219");
		}
		
		for(int i=1; i<=12; i++){
			if(PipelineEtapaHelper.hasDataFilled(mapa, i)) {
				PipelineEtapa pe = new PipelineEtapa();
				pe.setIdPipelineEtapa(mapa.getLong("idPipelineEtapa"+i));
				pe.setDtEvento(mapa.getYearMonthDay("dtEvento"+i));
				pe.setDsObservacao(mapa.getString("dsObservacao"+i));
				pe.setTpPipelineEtapa(setaTpPipelineEtapa(i));
				pe.setPipelineCliente(pc);
				
				if(mapa.getLong("visita"+i+".idVisita")!= null){
					Visita v = new Visita();
					v.setIdVisita(mapa.getLong("visita"+i+".idVisita"));
					pe.setVisita(v);
				}
				
				if(pe.getIdPipelineEtapa() != null && pe.getDtEvento() == null && StringUtils.isBlank(pe.getDsObservacao())) {
					this.removeById(pe.getIdPipelineEtapa());
				} else {
					Serializable id  = this.storePipeLineEtapa(pe);
					mapaIds.put("idPipelineEtapa"+i, id);
				}
				
				blEnableAbaPropostasTabelas = verificaVisualizacaoAbaPropostasTabelas(blEnableAbaPropostasTabelas, pe);
			}
		}
		mapaIds.put("enableAbaPropostasTabelas", blEnableAbaPropostasTabelas);
		DomainValue dmv = new DomainValue();
		pc = pipelineClienteService.findById(mapa.getLong("idPipelineCliente"));
		pc.setListPipelineReceita(null);
		if(mapa.getYearMonthDay("dtPerda")!= null){
			pc.setTpMotivoPerda(mapa.getDomainValue("tpMotivoPerda"));
			pc.setDtPerda(mapa.getYearMonthDay("dtPerda"));
			dmv.setValue("P");
			pc.setTpSituacao(dmv);
			this.pipelineClienteService.store(pc);
			mapaIds.put("tpSituacao", "P");
		} else if(mapa.getYearMonthDay("dtEvento7")!= null){
			dmv.setValue("F");
			pc.setTpSituacao(dmv);
			this.pipelineClienteService.store(pc);
			mapaIds.put("tpSituacao", "F");
		} else if(mapa.getYearMonthDay("dtEvento12")!= null){
			dmv.setValue("C");
			pc.setTpSituacao(dmv);
			this.pipelineClienteService.store(pc);
			mapaIds.put("tpSituacao", "C");
		}
		
		return mapaIds;
	}
	
	private Boolean verificaDataEventoEtapas(TypedFlatMap mapa) {
		Boolean blDatasEmBranco = Boolean.FALSE;
		
		for(int i=1; i<=12; i++){
			if(mapa.getYearMonthDay("dtEvento"+i) != null){
				blDatasEmBranco = Boolean.TRUE;
				return blDatasEmBranco;
			}
		}
		
		return blDatasEmBranco;
	}
	
	private DomainValue setaTpPipelineEtapa(int i){
		 DomainValue dm = new DomainValue();
		 if(i==1)
			 dm.setValue("05");
		 else if (i==2)
			 dm.setValue("10");
		 else if (i==3)
			 dm.setValue("15");
		 else if (i==4)
			 dm.setValue("20");
		 else if (i==5)
			 dm.setValue("25");
		 else if (i==6)
			 dm.setValue("30");
		 else if (i==7)
			 dm.setValue("35");
		 else if (i==8)
			 dm.setValue("55");
		 else if (i==9)
			 dm.setValue("40");
		 else if (i==10)
			 dm.setValue("45");
		 else if (i==11)
			 dm.setValue("50");
		 else if (i==12)
			 dm.setValue("60");
		 return dm;
	}
	
	private Boolean verificaVisualizacaoAbaPropostasTabelas(Boolean blEnableAbaPropostasTabelas, PipelineEtapa pe) {
		if (blEnableAbaPropostasTabelas) {
			if ("35".equals(pe.getTpPipelineEtapa().getValue()) || "60".equals(pe.getTpPipelineEtapa().getValue())) {
				return Boolean.FALSE;
			}
		}
		return blEnableAbaPropostasTabelas;
	}
	
	public Serializable storePipeLineEtapa(PipelineEtapa pipelineEtapa) {
		PipelineEtapa pipelineEtapaToSave = createOrLoadPipelineEtapa(pipelineEtapa);
		validaPipelineEtapa(pipelineEtapaToSave);

		if (PipelineEtapaHelper.isCancelamento(pipelineEtapaToSave)) {
			pipelineClienteService.storeAndCancel(pipelineEtapaToSave.getPipelineCliente());
		}
		if(pipelineEtapaToSave.getDtEvento()== null || StringUtils.isBlank(pipelineEtapaToSave.getDsObservacao())) {
			super.removeById(pipelineEtapaToSave.getIdPipelineEtapa());
			return null;
		} else {
			return super.store(pipelineEtapaToSave);
	}
	}

	private PipelineEtapa createOrLoadPipelineEtapa(PipelineEtapa pipelineEtapa) {
		PipelineEtapa result = null;
		
		if (pipelineEtapa.getIdPipelineEtapa() != null) {
			result = findById(pipelineEtapa.getIdPipelineEtapa());
		} else {
			result = new PipelineEtapa();
		}
		
		
		if(pipelineEtapa.getPipelineCliente().getIdPipelineCliente()!= null){
			PipelineCliente pc = getPipelineClienteService().findById(pipelineEtapa.getPipelineCliente().getIdPipelineCliente());
			result.setPipelineCliente(pc);
		}
		if(pipelineEtapa.getVisita()!= null){
			Visita v = getVisitaService().findById(pipelineEtapa.getVisita().getIdVisita());
			result.setVisita(v);
		}
		
		result.setDtEvento(pipelineEtapa.getDtEvento());
		result.setDsObservacao(pipelineEtapa.getDsObservacao());
		result.setTpPipelineEtapa(pipelineEtapa.getTpPipelineEtapa());
		
		return result;
	}

	private void validaPipelineEtapa(PipelineEtapa bean) {
		if (bean.getDtEvento() != null && StringUtils.isBlank(bean.getDsObservacao())) {
			throw new BusinessException("LMS-01211");
		}
		
		List<PipelineReceita> listaPipelineReceita = pipelineReceitaService
				.findPipelineReceitaByPipelineCliente(bean.getPipelineCliente().getIdPipelineCliente());
		
		/* A tela de cadastro de pipeline cliente, permite somente o cadastro de uma receita, por este motivo o get(0) */
		if (listaPipelineReceita.isEmpty() || !"A".equals(listaPipelineReceita.get(0).getTpModal().getValue())) {
		if (bean.getDtEvento() != null && bean.getTpPipelineEtapa() != null && PipelineEtapaHelper.isFechamentoNegocio(bean) // fechamento do negocio
				&& !pipelineClienteSimulacaoService.findExistenciaTabelaOuSimulacaoByPipelineCliente(bean.getPipelineCliente())) {
			throw new BusinessException("LMS-01212");
		}
		}
		
	}

	private boolean findExistenciaEtapaEfetivacaoByPipelineCliente(PipelineCliente pipelineCliente) {
		PipelineEtapa pipelineEtapa = getPipelineEtapaDAO().findEtapaFilledByIdPipelineClienteAndTpPipelineEtapa(pipelineCliente.getIdPipelineCliente(), "55"); // efetivacao
		return pipelineEtapa != null;
	}

	public DadosEmbarquePipelineDTO storeAndFindPrimeiroEmbarquePipeline(Long idPipelineCliente) {
		DadosEmbarquePipelineDTO result = null;

		PipelineCliente pipelineClienteLoaded = pipelineClienteService.findById(idPipelineCliente);
		PipelineEtapa pipelineEtapa = getPipelineEtapaDAO().findEtapaFilledByIdPipelineClienteAndTpPipelineEtapa(idPipelineCliente, "45");// primeir embarque
		
		if (!"F".equals(pipelineClienteLoaded.getTpSituacao().getValue()) 
				&& pipelineEtapa == null) {  

			DadosEmbarquePipelineDTO dadosByTabelasPrecos = null;
			DadosEmbarquePipelineDTO dadosBySimulacoes = null;
			
			List tabelasPrecos = tabelaPrecoService.findTabelasInPipelineClienteSimulacaoByPipelineCliente(pipelineClienteLoaded);
			if (CollectionUtils.isNotEmpty(tabelasPrecos)) {
				dadosByTabelasPrecos = conhecimentoService.findConhecimentosEmitidosByListTabelaPreco(tabelasPrecos);
			} 
			List simulacoes = simulacaoService.findSimulacoesInPipelineClienteSimulacaoByPipelineCliente(pipelineClienteLoaded);
			if (CollectionUtils.isNotEmpty(simulacoes)) {
				dadosBySimulacoes = conhecimentoService.findConhecimentosEmitidosByListSimulacao(simulacoes);
			}
			result = PipelineEtapaHelper.chooseDadosEmbrarquePipelineDto(dadosByTabelasPrecos, dadosBySimulacoes);
			if (result != null) {
				PipelineEtapa pipelineEtapaToSave = new PipelineEtapa();
				pipelineEtapaToSave.setPipelineCliente(pipelineClienteLoaded);
				pipelineEtapaToSave.setTpPipelineEtapa(new DomainValue("45"));
				pipelineEtapaToSave.setDtEvento(result.getDhEmissao().toYearMonthDay());
				pipelineEtapaToSave.setDsObservacao(result.toFormarttedConhecimento());
				store(pipelineEtapaToSave);
			}
		}
		return result;
	}
	
	public DadosEfetivacaoPipelineDTO storeAndFindEfetivacaoPipeline(Long idPipelineCliente) {
		DadosEfetivacaoPipelineDTO result = null;

		PipelineCliente pipelineClienteLoaded = pipelineClienteService.findById(idPipelineCliente);
		PipelineEtapa pipelineEtapa = getPipelineEtapaDAO().findEtapaFilledByIdPipelineClienteAndTpPipelineEtapa(idPipelineCliente, "55");// efetivacao
	
		if (pipelineEtapa == null) {

			DadosTabelaPrecoDTO dadosTabelaPreco = tabelaPrecoService.findFirstTabelaEfetivadaByIdPipelineCliente(pipelineClienteLoaded);
			Simulacao simulacao = simulacaoService.findSimulacaoEfetivadaByIdPipeLineCliente(pipelineClienteLoaded);
			
			result = PipelineEtapaHelper.createDadosEfetivacaoPipelineDTO(dadosTabelaPreco, simulacao);
			
			if (result != null) {
				PipelineEtapa pipelineEtapaToSave = new PipelineEtapa();
				pipelineEtapaToSave.setDtEvento(result.getDtEvento());
				pipelineEtapaToSave.setDsObservacao(result.getDsEvento());
				pipelineEtapaToSave.setTpPipelineEtapa(new DomainValue("55"));
				pipelineEtapaToSave.setPipelineCliente(pipelineClienteLoaded);
				store(pipelineEtapaToSave); 
			}
			
		}
		
		return result;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPipelineEtapaDAO(PipelineEtapaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PipelineEtapaDAO getPipelineEtapaDAO() {
		return (PipelineEtapaDAO) getDao();
	}

	public PipelineClienteService getPipelineClienteService() {
		return pipelineClienteService;
	}

	public PipelineReceitaService getPipelineReceitaService() {
		return pipelineReceitaService;
	}

	public void setPipelineReceitaService(
			PipelineReceitaService pipelineReceitaService) {
		this.pipelineReceitaService = pipelineReceitaService;
	}

	public void setPipelineClienteService(
			PipelineClienteService pipelineClienteService) {
		this.pipelineClienteService = pipelineClienteService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setPipelineClienteSimulacaoService(
			PipelineClienteSimulacaoService pipelineClienteSimulacaoService) {
		this.pipelineClienteSimulacaoService = pipelineClienteSimulacaoService;
	}

	public void removeEtapaPrimeiroEmbarqueAndEfetivacaoByIdPipelineClienteSimulacao(Long idPipelineClienteSimulacao) {
		List<String> tpEtapas = Arrays.asList("45", "55");
		List<Long> lstEtapas = getPipelineEtapaDAO().findEtapasByTpPipelineEtapaAndIdPipelineClienteSimulacao(tpEtapas, idPipelineClienteSimulacao);
		if (CollectionUtils.isNotEmpty(lstEtapas)) {
			removeByIds(lstEtapas);
		}
	}
	
}
