package com.mercurio.lms.carregamento.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleCargaConhScan;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.ControleCargaConhScanService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.carregamento.model.service.TrechoCorporativoService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PostoAvancadoService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.service.ExigenciaGerRiscoService;
import com.mercurio.lms.sgr.model.service.PlanoGerenciamentoRiscoService;
import com.mercurio.lms.sgr.model.util.PlanoGerenciamentoRiscoUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.carregamento.swt.carregarVeiculoAction"
 */

public class CarregarVeiculoAction extends CrudAction {

	private ControleCargaService controleCargaService;
	private EventoControleCargaService eventoControleCargaService;
	private FilialService filialService;
	private MoedaService moedaService;
	private PostoAvancadoService postoAvancadoService;
	private PessoaService pessoaService;
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private ExigenciaGerRiscoService exigenciaGerRiscoService;
	private ManifestoService manifestoService;
	private ControleCargaConhScanService controleCargaConhScanService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private TrechoCorporativoService trechoCorporativoService;
//	private ConhecimentoService conhecimentoService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService;
    private PlanoGerenciamentoRiscoUtils planoUtils;
    private WorkflowPendenciaService workflowPendenciaService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    
    // LMSA-7339
    private ParametroGeralService parametroGeralService;
 
//    private static final String PA_SGR_VAL_EXIST_PGR = "SGR_VAL_EXIST_PGR";
    private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
    
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	public void setControleCargaConhScanService(ControleCargaConhScanService controleCargaConhScanService) {
		this.controleCargaConhScanService = controleCargaConhScanService;
	}
	public ControleCargaConhScanService getControleCargaConhScanService() {
		return controleCargaConhScanService;
	}
	public void setTrechoCorporativoService(TrechoCorporativoService trechoCorporativoService) {
		this.trechoCorporativoService = trechoCorporativoService;
	}
	public TrechoCorporativoService getTrechoCorporativoService() {
		return trechoCorporativoService;
	}
	public ControleCargaService getControleCargaService() {
		return this.controleCargaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}	
	public EventoControleCargaService getEventoControleCargaService() {
		return eventoControleCargaService;
	}
	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public MoedaService getMoedaService() {
		return moedaService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public PostoAvancadoService getPostoAvancadoService() {
		return postoAvancadoService;
	}
	public void setPostoAvancadoService(PostoAvancadoService postoAvancadoService) {
		this.postoAvancadoService = postoAvancadoService;
	}	
	public PessoaService getPessoaService() {
		return pessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}	
	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
	}
	public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}
	public ExigenciaGerRiscoService getExigenciaGerRiscoService() {
		return exigenciaGerRiscoService;
	}
	public void setExigenciaGerRiscoService(ExigenciaGerRiscoService exigenciaGerRiscoService) {
		this.exigenciaGerRiscoService = exigenciaGerRiscoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	
	
	/**
	 * Busca a Service default desta Action
	 * 
	 * @param carregamentoDescargaService
	 */
	public void setService(CarregamentoDescargaService carregamentoDescargaService) {
		this.defaultService = carregamentoDescargaService;
	}

	public CarregamentoDescargaService getService() {
		return (CarregamentoDescargaService) this.defaultService;
	}

	public void removeById(java.lang.Long id) {
		getCarregamentoDescargaService().removeById(id);
	}
	private CarregamentoDescargaService getCarregamentoDescargaService() {
		return ((CarregamentoDescargaService) getDefaultService());
	}

	/**
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getCarregamentoDescargaService().removeByIds(ids);
	}

	public CarregamentoDescarga findById(java.lang.Long id) {
		return getCarregamentoDescargaService().findById(id);
	}
	
	/**
	 * Busca para o objeto pessoa.
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupPessoa(TypedFlatMap criteria) {
		Map pessoa = new HashMap();
		pessoa.put("tpPessoa", criteria.getString("pessoa.tpPessoa"));
		pessoa.put("nrIdentificacao", criteria.getString("pessoa.nrIdentificacao"));
		pessoa.put("tpIdentificacao", criteria.getString("pessoa.tpIdentificacao"));

		pessoa.remove("pessoa.nrIdentificacao");
		pessoa.remove("pessoa.tpIdentificacao");

		return this.getPessoaService().findLookup(pessoa);
	}

	public void  validateControleCarga(TypedFlatMap parameters){
		controleCargaService.validateConhecimentosNaoEmitidosByControleCarga(parameters.getLong("idControleCarga"));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map verificarExisteDocumentosNaoEmitidos(TypedFlatMap parameters){
		
		Long idControleCarda = parameters.getLong("idControleCarda");
		List<Long> idsManifesto = (List<Long>) parameters.get("idsManifesto");
		
		boolean existeDocumentosNaoEmitidos = preManifestoDocumentoService.validateExisteDocumentosNaoEmitidos(idControleCarda, idsManifesto);
		
		Map map = new HashMap();
		map.put("existeDocumentosNaoEmitidos", existeDocumentosNaoEmitidos);
		
		return map;
	}

	/**
	 * Busca a quantidade de dados da grid de carregamentos
	 */
	@SuppressWarnings("rawtypes")
	public Integer getRowCount(Map criteria) {
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("idControleCarga", criteria.get("idControleCarga"));
		tfmCriteria.put("idFilial", criteria.get("idFilial"));
		tfmCriteria.put("idPostoAvancado", criteria.get("idPostoAvancado"));
    	tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
    	tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
    	tfmCriteria.put("_order", criteria.get("_order"));
		
		return this.getService().getRowCountCarregamentoDescarga(tfmCriteria, "C");
	}

	/**
	 * Busca os registros da grid de carregamento
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(Map criteria) {		
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("idControleCarga", criteria.get("idControleCarga"));
		tfmCriteria.put("idFilial", criteria.get("idFilial"));
		tfmCriteria.put("idPostoAvancado", criteria.get("idPostoAvancado"));
    	tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
    	tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
    	tfmCriteria.put("_order", criteria.get("_order"));
		
		return this.getService().findPaginatedCarregamentoDescarga(tfmCriteria, "C");
	}	
	
	/**
	 * 	Faz a chamada para o findPaginated de documentoServico.
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedExigenciasGerRisco(Map criteria) {
		Long idControleCarga = (Long)criteria.get("idControleCarga");
        return this.getService().findPaginatedExigenciasGerRisco(idControleCarga);
	}
	
	/**
	 * Busca a descri��o completa de uma exig�ncia de gerenciamento de risco a partir de seu id.
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map findDescricaoExigenciaGerRiscoById(Map criteria) {
		Long id = (Long)criteria.get("idExigenciaGerRisco");
		ExigenciaGerRisco exigenciaGerRisco = exigenciaGerRiscoService.findById(id);
    	Map map = new HashMap();
    	map.put("dsCompleta", exigenciaGerRisco.getDsCompleta().getValue());
    	return map;
    }
	
	/**
	 * Busca os dados necessarios para carregar o final a tela de 
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map loadFinalStates(Map criteria){
		CarregamentoDescarga carregamentoDescarga = this.getService().findById((Long)criteria.get("idCarregamentoDescarga"));
		
		Map returnData = new HashMap();
		returnData.put("tpStatusControleCarga", carregamentoDescarga.getControleCarga().getTpStatusControleCarga().getDescription());
		returnData.put("dhFimOperacao", carregamentoDescarga.getDhFimOperacao());
		
		return returnData;
	}
	
	/**
	 * M�todo que valida o Pr�-Manifesto.
	 */
	@SuppressWarnings("rawtypes")
	public void validatePreManifestos(Map criteria) {		
		Long idControleCarga = (Long)criteria.get("idControleCarga");
		Long idFilial = (Long)criteria.get("idFilial");
		Long idPostoAvancado = (Long)criteria.get("idPostoAvancado");
		
		controleCargaService.validateConhecimentosNaoEmitidosByControleCarga(idControleCarga);
		
		List result = this.getService().findCarregamentoDescarga(idControleCarga, idFilial, idPostoAvancado, "C");
		
		Long qtEscaneados = 0L;
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			if (map.get("dhFimCarregamento") == null) { 
				throw new BusinessException("LMS-05066");
			}
		
			DomainValue tpManifesto = (DomainValue)map.get("tpManifesto");									
			DomainValue tpManifestoViagem = (DomainValue)map.get("tpManifestoViagem");
			Long idCarregamentoDescarga = MapUtils.getLong(map, "idCarregamentoDescarga");
				
			if(tpManifesto.getValue().equals(ConstantesExpedicao.TP_MANIFESTO_ENTREGA) || (
				tpManifesto.getValue().equals(ConstantesExpedicao.TP_MANIFESTO_VIAGEM) &&
				tpManifestoViagem != null &&
				!tpManifestoViagem.getValue().equals("RV"))) {
				
				List<ControleCargaConhScan> escaneados = controleCargaConhScanService.findByCarregamentoDescarga(idCarregamentoDescarga);
				
				if(escaneados != null && escaneados.size() > 0) {
					qtEscaneados += escaneados.size();
	}
		}
	}
	}

	@SuppressWarnings("rawtypes")
	public TypedFlatMap validateDoctoServOutroManifesto(Map criteria){
		Long idControleCarga = (Long)criteria.get("idControleCarga");
		Long idCarregamentoDescarga = (Long) criteria.get("idCarregamentoDescarga");
		TypedFlatMap map = new TypedFlatMap();
		
		if (idControleCarga != null){
			map = controleCargaService.validateDoctoServOutroManifesto(idControleCarga);	
		}
		
		//LMS-5261
		if(idCarregamentoDescarga != null){
			map.put("carregamentoDescarga", (CarregamentoDescarga)carregamentoDescargaService.findById(idCarregamentoDescarga));
		}
		
		// LMSA-7339
		map.put("paramMaxDocsWorkflowCarregamento", getMaxDocumentosWorkflowCarregamento());
		
		return map;
	}
	
	/**
	 * LMS-5261 - Cancela a pendecia do workflow
	 * @param criteria
	 */
	@SuppressWarnings("rawtypes")
	public void cancelPendencia(Map criteria){
		Long idPendencia = (Long)criteria.get("idPendencia");
		
		if(idPendencia != null){
			workflowPendenciaService.cancelPendencia(idPendencia);
		}
		
	}

	// LMSA-7339
	private static final String parametroMaxDocumentosWorkflowCarregamento = "MAX_DOCS_WORKFLOW_CARREGAMENTO";
	/**
	 * Recuperar o valor par�metro para m�ximo de documentos listados no workflow de carregamento
	 * LMSA-7339
	 * @return 
	 */
	private Integer getMaxDocumentosWorkflowCarregamento() {
		String paramValue = null;
		int valor = 210;
		try {
			paramValue = (String) parametroGeralService.findConteudoByNomeParametro(parametroMaxDocumentosWorkflowCarregamento, false);
			valor = Integer.parseInt(paramValue);
		} catch (Exception e) {
			// nada para garantir que o valor default '210' seja retornado pelo metodo
		}
		return valor;
	}
	
	/**
	 * LMS-5261 - gera o workflow com a pendencia
	 * @param criteria
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void generatePendencia(Map criteria) {
		Long idControleCarga = (Long)criteria.get("idControleCarga");
		Long idCarregamentoDescarga = (Long) criteria.get("idCarregamentoDescarga");
		StringBuilder dssProcesso = new StringBuilder();
		
		if(idControleCarga != null && idCarregamentoDescarga != null) {
			ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
			CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findById(idCarregamentoDescarga);
			
			String[] mensagem = (String[]) criteria.get("doctosServicoManifestados");
			
			dssProcesso.append("O Controle de Carga ")
					.append(controleCarga.getFilialByIdFilialOrigem().getSgFilial()).append(" ") 
					.append(controleCarga.getNrControleCarga())
					.append(" necessita de aprova��o porque cont�m documentos de servi�o j� manifestados anteriormente para o mesmo destino. ")
					.append(mensagem[0]);
			
			// LMSA-7339 - garantir que sejam gravados no maximo 4000 bytes
			String descricaoProcesso = dssProcesso.toString();
			if (descricaoProcesso.length() > 3999) {
				descricaoProcesso = descricaoProcesso.substring(0,3999);
				descricaoProcesso = descricaoProcesso.substring(0, descricaoProcesso.lastIndexOf(",")-1);
			}
			
			Pendencia pendencia = workflowPendenciaService.generatePendencia(
										SessionUtils.getFilialSessao().getIdFilial(), 
										ConstantesWorkflow.NR_APROVACAO_DOCTO_MANIFESTADO, 
										idCarregamentoDescarga, 
										descricaoProcesso, // LMSA-7339 - dssProcesso.toString(), 
										JTDateTimeUtils.getDataHoraAtual()
									);
			
			carregamentoDescarga.setPendencia(pendencia);
			carregamentoDescarga.setTpStatusWorkflow(new DomainValue("E"));
			carregamentoDescargaService.store(carregamentoDescarga);
		}		
	}
	
	/**
	 * Busca os campos da tela de carregamento a partir de um controle de carga
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findCarregamentoDescargaByNrControleCarga(Map criteria) {
		Long nrControleCarga = (Long)criteria.get("nrControleCarga");
		Long idFilial = (Long)criteria.get("idFilial");
		
		List listControleCarga = this.getControleCargaService().findControleCargaByNrControleByFilial(nrControleCarga, idFilial);
		if (!listControleCarga.isEmpty()) {
		
			//TODO: Buscar posto avancado da sessao, ou do criteria decorrente de o usuario ja estar vindo da tela...
			Long idPostoAvancado = null;
						
			List result = this.getService().findCarregamentoDescargaByNrControleCarga(nrControleCarga, idFilial, idPostoAvancado, "C");
			
			//Verifica se existem dados dentro do resultado da consulta.
			if (!result.isEmpty()) {
				Map dataObject = (Map) result.get(0);
				
				//Verifica se pela descricao da rota do controleCarga possui a filial que o usuario esta logado.
				DomainValue tpControleCarga = (DomainValue) dataObject.get("tpControleCarga");
				if ((tpControleCarga != null) && (tpControleCarga.getValue().equals("V"))) {					
					Long idControleCarga = Long.valueOf(String.valueOf(dataObject.get("idControleCarga")));
					Long idFilialOrigem = SessionUtils.getFilialSessao().getIdFilial();
					
					if (!this.getControleCargaService().findControleCargaInControleTrecho(idControleCarga, idFilialOrigem)) {
						throw new BusinessException("LMS-05069");
					}
				}
				dataObject.put("tpControleCargaDescription", tpControleCarga == null ? null : tpControleCarga.getDescription()); // Sobrescrevo o tpControleCarga devido eu estar utilizando um textBox e n�o uma combobox no cadastro de CarregarVeiculoCarregamento
				dataObject.put("tpControleCargaValue", tpControleCarga == null ? null : tpControleCarga.getValue()); // Sobrescrevo o tpControleCarga devido eu estar utilizando um textBox e n�o uma combobox no cadastro de CarregarVeiculoCarregamento
				
				DomainValue tpStatusControleCarga = (DomainValue) dataObject.get("tpStatusControleCarga");
				dataObject.put("tpStatusControleCarga", tpStatusControleCarga == null ? null : tpStatusControleCarga.getDescription()); // Sobrescrevo o tpStatusControleCarga devido eu estar utilizando um textBox e n�o uma combobox no cadastro de CarregarVeiculoCarregamento
				
				Long idFilialAtual = (Long) dataObject.get("idFilialAtual");
				if (!SessionUtils.getFilialSessao().getIdFilial().equals(idFilialAtual)) {
					throw new BusinessException("LMS-05070");
				}
				
				Long idControleCarga = Long.valueOf(String.valueOf(dataObject.get("idControleCarga")));
				
				List resultEventoControleCarga = this.getEventoControleCargaService()
					.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(SessionUtils.getFilialSessao().getIdFilial(), 
																				  idControleCarga, "CP");
				
				if (!resultEventoControleCarga.isEmpty()) {
					EventoControleCarga eventoControleCarga = (EventoControleCarga) resultEventoControleCarga.get(0);
					dataObject.put("dhEvento", eventoControleCarga.getDhEvento());
				}
				
				DomainValue tpStatusOperacao = (DomainValue) dataObject.get("tpStatusOperacao");
				dataObject.put("tpStatusOperacao", tpStatusOperacao == null ? null : tpStatusOperacao.getValue());
	
				
			} else {
				throw new BusinessException("LMS-05077");
			}
			
			return result;
		}
		
		return listControleCarga;
	}

	@SuppressWarnings("rawtypes")
	public void updateValorTotalManifesto(Map criteria) {		
		Long idManifesto = (Long)criteria.get("idManifesto");
		List result = this.getPreManifestoDocumentoService().findPreManifestoDocumentoByIdManifesto(idManifesto);
		
		this.getPreManifestoDocumentoService().updateValorTotalManifesto(idManifesto, result);
	}
	
	/**
     * Verifica se o manifesto em questao ja foi ou deve iniciar o seu carregamento
     * 
     * @param criteria
     * @return
     */
    @SuppressWarnings("rawtypes")
	public void generateCarregamento(Map criteria) {
    	Long idManifesto = (Long)criteria.get("idManifesto");
    	Long idCarregamentoDescarga = (Long)criteria.get("idCarregamentoDescarga");
    	this.getService().storeIniciarCarregamentoPreManifesto(idManifesto, idCarregamentoDescarga);
    }

	/**
	 * Efetua a consulta da tela de Gerenciamento de riscos.
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getValoresManifestos(Map criteria) {
		Long idControleCarga = Long.valueOf(criteria.get("idControleCarga").toString());
		TypedFlatMap tfm = new TypedFlatMap();		
		ReflectionUtils.flatMap(tfm, this.getService().findValoresManifesto(idControleCarga));
		Map mapRetorno = new HashMap();
		
		mapRetorno.put("dsMoeda", tfm.get("moeda.dsMoeda"));
		mapRetorno.put("totalCarregado", tfm.get("moeda.totalCarregado"));
		mapRetorno.put("totalCarregadoSeguroMercurio", tfm.get("moeda.totalCarregadoSeguroMercurio"));
		mapRetorno.put("totalCarregadoSeguroCliente", tfm.get("moeda.totalCarregadoSeguroCliente"));
		return mapRetorno;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findLookupBySgFilial(Map criteria) {
		List listResult = new ArrayList();
		List listFilial = this.getFilialService().findLookupBySgFilial((String)criteria.get("sgFilial"), null);
		for (Iterator iter = listFilial.iterator(); iter.hasNext();) {
			Map mapFilial = (Map) iter.next();
			Map mapResult = new HashMap();
			mapResult.put("idFilial", mapFilial.get("idFilial"));
			mapResult.put("sgFilial", mapFilial.get("sgFilial"));
			Map mapPessoa = (Map)mapFilial.get("pessoa");
			mapResult.put("nmFantasia", mapPessoa.get("nmFantasia"));
			
			listResult.add(mapResult);
		}
		return listResult;	
	}
	
	/**
	 * Remove um dos manifestos da grid de carregarVeiculo, a partir do idManifestoInformado
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public void removeManifestoCarregamento(Map criteria) {
		Long idManifesto = (Long)criteria.get("idManifesto");			
		this.manifestoService.removeById(idManifesto);
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map validatePermiteFinalizarCarregamento(TypedFlatMap criteria) {
		boolean blPermiteFinalizar = true;
		Long idControleCarga = criteria.getLong("idControleCarga"); 
		List listaManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
		if (!listaManifestos.isEmpty()) {
			if (manifestoService.validateExisteManifestoSemCarregamentoConcluido(idControleCarga)) 
				blPermiteFinalizar = false;
		}
		Map map = new HashMap();
		map.put("blPermiteFinalizar", blPermiteFinalizar);
		return map;
	}
	
	public TypedFlatMap verificaPossibilidadeCriacaoRNCautomatica(TypedFlatMap parameters){
		TypedFlatMap retorno = new TypedFlatMap();
		if(SessionUtils.getFilialSessao().getBlRncAutomaticaCarregamento()){
			Long idControleCarga = parameters.getLong("idControleCarga");
			Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
			List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = ocorrenciaNaoConformidadeService.executeVerificacaoPossibilidadeCriacaoRNCautomatica(idControleCarga, idFilial);
			if(listaDocVolmRNCcriacaoAutomatica != null){
				retorno.put("rncAutomaticaMessage", ocorrenciaNaoConformidadeService.getRncAutomaticaMessage(listaDocVolmRNCcriacaoAutomatica));
				retorno.put("listaDocVolmRNCcriacaoAutomatica", listaDocVolmRNCcriacaoAutomatica);
			}
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public TypedFlatMap executeCriacaoRNCautomaticaCarregamento(TypedFlatMap parameters){
		TypedFlatMap retorno = new TypedFlatMap();
		if(SessionUtils.getFilialSessao().getBlRncAutomaticaCarregamento()){
			Long idControleCarga = parameters.getLong("idControleCarga");
			List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = parameters.getList("listaDocVolmRNCcriacaoAutomatica");
			Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
			ocorrenciaNaoConformidadeService.executeCriacaoRNCautomaticaCarregamento(idControleCarga, idFilial, listaDocVolmRNCcriacaoAutomatica, false);
		}
		return retorno;
	}
	
	
	/**
	 * LMS-6851 verificar se existe no PGR e fazer enquadramento das regras
	 * 
	 * @param parameters
	 * @return
	 */
	public TypedFlatMap executeVerificarEnquadramentoRegra(TypedFlatMap parameters) {
		PlanoGerenciamentoRiscoDTO plano = planoGerenciamentoRiscoService.executeVerificarEnquadramentoRegra(parameters.getLong("idControleCarga"));
		planoGerenciamentoRiscoService.generateExigenciasGerRisco(plano);
		controleCargaService.validateExigenciasFinalizarCarregamento(plano);
		
		if (!CollectionUtils.isEmpty(plano.getMensagensEnquadramento())) {
			String mensagensEnquadramento = planoUtils.join(plano.getMensagensEnquadramento());
			parameters.put("mensagensEnquadramento", mensagensEnquadramento);
		} else if ((!CollectionUtils.isEmpty(plano.getMensagensWorkflowRegras()) 
				|| !CollectionUtils.isEmpty(plano.getMensagensWorkflowExigencias()))
				&& planoGerenciamentoRiscoService.validateDeveGerarWorkflowRegras(plano)) {
			StringBuilder sb = new StringBuilder();
			if (!CollectionUtils.isEmpty(plano.getMensagensWorkflowRegras())) {
				sb.append(planoUtils.join(plano.getMensagensWorkflowRegras()));
				sb.append(LINE_SEPARATOR);
			}
			if (!CollectionUtils.isEmpty(plano.getMensagensWorkflowExigencias())) {
				sb.append(planoUtils.join(plano.getMensagensWorkflowExigencias()));
			}
			parameters.put("mensagensWorkflow", sb.toString());
		}
		
		parameters.put("blRncAutomaticaNovo", getParametroRncAutomaticaNovo(SessionUtils.getFilialSessao().getIdFilial()));
		
		return parameters;
	}
	
	private boolean getParametroRncAutomaticaNovo(Long idFilialUsuario) {
		String parametroRncAutomaticaNovo = (String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialUsuario, "RNC_AUTOMATICA_NOVO", false);
		return "S".equals(parametroRncAutomaticaNovo);
	}
	
	public void generatePendenciaWorkflowCarregamento(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga");
		String mensagens = parameters.getString("mensagens");
		String justificativa = parameters.getString("justificativa");
		planoGerenciamentoRiscoService.generatePendenciaWorkflowFinalizarCarregamento(idControleCarga, mensagens, justificativa);
	}	
	
	@SuppressWarnings("rawtypes")
	public void updateTpStatusOperacao(Map criteria) {		
		Long idCarregamentoDescarga = (Long)criteria.get("idCarregamentoDescarga");
		CarregamentoDescarga carregamentoDescarga = getCarregamentoDescargaService().findById(idCarregamentoDescarga);
		
		getCarregamentoDescargaService().executeDesfazerFinalizarCarregamentoPreManifestos(carregamentoDescarga.getControleCarga().getIdControleCarga(), idCarregamentoDescarga);
				
	}
	
	public void setCarregamentoDescargaService(
			CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}
	
//	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
//		this.conhecimentoService = conhecimentoService;
//	}
	
	public void setPlanoGerenciamentoRiscoUtils(PlanoGerenciamentoRiscoUtils planoGerenciamentoRiscoUtils) {
		this.planoUtils = planoGerenciamentoRiscoUtils;
	}
	
	public void setPlanoGerenciamentoRiscoService(PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService) {
		this.planoGerenciamentoRiscoService = planoGerenciamentoRiscoService;
	}
	
	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}
	
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
	// LMSA-7339
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
}