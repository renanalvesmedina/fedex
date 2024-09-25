package com.mercurio.lms.recepcaodescarga.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.EventoDescargaVeiculo;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.ControleCargaConhScanService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;


/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.recepcaodescarga.descarregarVeiculoAction"
 */

public class DescarregarVeiculoAction extends CrudAction {
	private ControleCargaService controleCargaService;
	private ServicoService servicoService;
	private FilialService filialService;
	private DoctoServicoService doctoServicoService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private EventoControleCargaService eventoControleCargaService;
	private ControleTrechoService controleTrechoService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private ConhecimentoService conhecimentoService;
	private ControleCargaConhScanService controleCargaConhScanService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private ConteudoParametroFilialService conteudoParametroFilialService;


	public void setService(CarregamentoDescargaService carregamentoDescargaService) {
		this.defaultService = carregamentoDescargaService;
	}

	private CarregamentoDescargaService getService() {
		return (CarregamentoDescargaService) this.defaultService;
	}

	public void removeById(java.lang.Long id) {
		this.getService().removeById(id);
	}

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		this.getService().removeByIds(ids);
	}

	public CarregamentoDescarga findById(Long id) {
		return this.getService().findById(id);
	}

	/**
	 * Busca os registros da grid de descarga
	 */
	public ResultSetPage findPaginatedCarregamentoDescarga(TypedFlatMap criteria) {
		Long idControleCarga = criteria.getLong("idControleCarga");
		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		String tpControleCarga = criteria.getString("tpControleCarga");
		
		return this.getService().findPaginatedManifestoByIdControleCarga(idControleCarga, tpControleCarga, idFilialSessao, FindDefinition.createFindDefinition(criteria));
	}

	/**
	 * Busca a quantidade de dados da grid de descarga
	 */
	public Integer getRowCountCarregamentoDescarga(TypedFlatMap criteria) {
		Long idControleCarga = criteria.getLong("idControleCarga");
		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		String tpControleCarga = criteria.getString("tpControleCarga");
		return this.getService().getRowCountManifestoByIdControleCarga(idControleCarga, tpControleCarga, idFilialSessao);
	}

	public List findServico(TypedFlatMap criteria) {
		List retorno = new ArrayList();
		List listServicos = servicoService.find(criteria);
		for (Iterator iter = listServicos.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			Servico servico = (Servico) iter.next();
			map.put("idServico", servico.getIdServico());
			map.put("dsServico", servico.getDsServico());
			retorno.add(map);
		}
		return retorno;
	} 

	public List findLookupBySgFilial(TypedFlatMap criteria) {
		return this.filialService.findLookup(criteria);
	}	

	/**
	 * M�todo que busca os Documentos Servi�o do Manifesto que possuem Data Prevista para Entrega
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedDoctoServico(TypedFlatMap criteria) {
		return carregamentoDescargaService.findPaginatedDoctoServico(criteria);
	}

	/**
	 * M�todo RowCount da busca de Documentos Servi�o do manifesto
	 */
	public Integer getRowCountDoctoServico(TypedFlatMap criteria) {
		return carregamentoDescargaService.getRowCountDoctoServico(criteria);
	}

	/**
	 * Busca os campos da tela de carregamento a partir de um controle de carga
	 * 
	 * @param criteria
	 * @return
	 */
	public List findCarregamentoDescargaByNrControleCarga(TypedFlatMap criteria) {
		Long nrControleCarga = criteria.getLong("nrControleCarga");
		Long idFilial = criteria.getLong("filialByIdFilialOrigem.idFilial");
		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		List listControleCarga = controleCargaService.findControleCargaByNrControleByFilial(nrControleCarga, idFilial);
		if (!listControleCarga.isEmpty()) {
			//TODO - Buscar posto avancado da sessao, ou do criteria decorrente de o usuario ja estar vindo da tela... 
			Long idPostoAvancado = null;
			
			List result = this.getService().findCarregamentoDescargaByNrControleCarga(
					nrControleCarga,
					idFilial,
					idPostoAvancado,
					"D"
			);
			
			if (!result.isEmpty()) {
				Map dataObject = (Map) result.get(0);
				Long idControleCarga = Long.valueOf(String.valueOf(dataObject.get("idControleCarga")));	
				
				if (dataObject.get("nrIdentificadorTransporte") == null) { 
					throw new BusinessException("LMS-02069"); 
				}
				
				// Verifica se o tipo do Controle de carga � de 'Viagem' e 
				// pela descricao da rota do controleCarga verifica se possui a filial que o usuario esta logado.
				if (((DomainValue)dataObject.get("tpControleCarga")).getValue().equals("V")) {
					Integer controlesTrecho = controleTrechoService.getRowCountControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(idControleCarga, null,	idFilialSessao);
					if (controlesTrecho.intValue()==0) {
						throw new BusinessException("LMS-05069");
					}				
				}

				Long idFilialAtual = (Long)dataObject.get("idFilialAtual");
				if (!idFilialSessao.equals(idFilialAtual)) {
					throw new BusinessException("LMS-05070");
				}

				List resultEventoControleCarga = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(idFilialSessao, idControleCarga, "CP");				
				if (!resultEventoControleCarga.isEmpty()) {
					EventoControleCarga eventoControleCarga = (EventoControleCarga) resultEventoControleCarga.get(0);
					dataObject.put("dhEvento", eventoControleCarga.getDhEvento());
				}				
				
				
				dataObject.put("onlyRemetenteComEtiquetaEDI", controleCargaService.findExistsOnlyClienteRemetenteComEtiquetaEDI(idControleCarga));
				dataObject.put("filialColetorDadoScan", SessionUtils.getFilialSessao().getBlColetorDadoScan());
				
			} else {
				throw new BusinessException("LMS-05077");
			}

			return result;
		}

		return listControleCarga;
	}
	
	/**
	 * M�todo que valida se os Documentos relacionados ao Manifesto do 
	 * Controle de Carga possuem Ocorencias Vinculadas.
	 * @param criteria
	 */
	public TypedFlatMap validateDoctoServicoComOcorrenciaVinculada(TypedFlatMap criteria) {
		ocorrenciaEntregaService.validateDoctoServicoComOcorrenciaVinculada(criteria.getLong("idControleCarga"), SessionUtils.getFilialSessao().getIdFilial());
		controleCargaService.validateControleCargaComOcorrenciaVinculada(criteria.getLong("idControleCarga"));
		
		TypedFlatMap toReturn = new TypedFlatMap();
		WarningCollectorUtils.putAll(toReturn);
		return toReturn;
	}

	
	public Map validateControleCargaIniciarDescarga(TypedFlatMap criteria) {
		ocorrenciaEntregaService.validateDoctoServicoComOcorrenciaVinculada(criteria.getLong("idControleCarga"), SessionUtils.getFilialSessao().getIdFilial());
		controleCargaService.validateControleCargaComOcorrenciaVinculada(criteria.getLong("idControleCarga"));
		
		carregamentoDescargaService.storeAndValidateDescargaVeiculo(criteria.getLong("idControleCarga"), EventoDescargaVeiculo.INICIAR_DESCARGAR);
		
		TypedFlatMap toReturn = new TypedFlatMap();
		WarningCollectorUtils.putAll(toReturn);
		return toReturn;
	}

	/**
	 * Busca algums dos dados do usuario logado, que est� na sess�o.
	 * 
	 * @return map
	 */
	public Map getBasicData() {

		// TODO Buscar posto avancado para ser carregado na tela do usuario

		Map pessoa = new HashMap();
		pessoa.put("nmFantasia", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());

		Map filial = new HashMap();
		filial.put("blInformaKmPortaria", SessionUtils.getFilialSessao().getBlInformaKmPortaria());
		filial.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
		filial.put("sgFilial", SessionUtils.getFilialSessao().getSgFilial());
		filial.put("pessoa", pessoa);

		Map dadosUsuario = new HashMap();
		dadosUsuario.put("filial", filial);
		dadosUsuario.put("blRncAutomaticaNovo", getParametroRncAutomaticaNovo(SessionUtils.getFilialSessao().getIdFilial()));

		return dadosUsuario;
	}
	
	private boolean getParametroRncAutomaticaNovo(Long idFilialUsuario) {
		String parametroRncAutomaticaNovo = (String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialUsuario, "RNC_AUTOMATICA_NOVO", false);
		return "S".equals(parametroRncAutomaticaNovo);
	}
	
	/**
	 * Faz a validacao do pce. Sempre que validar um pce e este nao for valido,
	 * interrompe o processo de validacao do mesmo.
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validatePCE(TypedFlatMap criteria) {
		Long cdProcesso;
		Long cdEvento;
		Long cdOcorrencia;

		if (criteria.getString("tpControleCarga").equals("C")) {
			cdProcesso = Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_RECEPCAO);
			cdEvento = Long.valueOf(EventoPce.ID_EVENTO_PCE_INICIO_DESCARGA_RECEPCAO);
			cdOcorrencia = Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_INIC_DESCARGA_INICIO_DESCARGA_RECEPCAO);
		} else {
			cdProcesso = Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_DESCARGA);
			cdEvento = Long.valueOf(EventoPce.ID_EVENTO_PCE_INICIO_DESCARGA);
			cdOcorrencia = Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_INIC_DESCARGA_INICIO_DESCARGA);
		}

		List doctosServicos = new ArrayList();
		List list = criteria.getList("list");
		for (int i = 0; i < list.size(); i++) {
			TypedFlatMap row = (TypedFlatMap) list.get(i);
			/** Otimiza��o LMS-815 */
    		final ProjectionList projection = Projections.projectionList()
	    		.add(Projections.property("ds.id"), "idDoctoServico")
	    		.add(Projections.property("cr.id"), "clienteByIdClienteRemetente.idCliente")
	    		.add(Projections.property("cd.id"), "clienteByIdClienteDestinatario.idCliente");

    		final Map<String, String> alias = new HashMap<String, String>();
    		alias.put("ds.clienteByIdClienteRemetente", "cr");
    		alias.put("ds.clienteByIdClienteDestinatario", "cd");

			//Busca os doctoServicos do manifesto em questao e adiciona tudo num list �nico...
    		final List<DoctoServico> doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(row.getLong("idManifesto"), projection, alias);
			doctosServicos.addAll(doctoServicoList);
		}			

		TypedFlatMap mapRetorno = new TypedFlatMap();
		mapRetorno.put("codigos", carregamentoDescargaService.validatePCE(doctosServicos, cdProcesso, cdEvento, cdOcorrencia));
		return mapRetorno;
	}
	
	public List findLookupConhecimento(Map criteria) {
		List findLookup = getConhecimentoService().findLookup(criteria);
		return findLookup;
	}

	/**
     * Salva um item Descri��o Padr�o na sess�o.
     */
    public void storeControleCargaConhecimento(Map parameters) {
    	
		Long idControleCarga = Long.parseLong(parameters.get("idControleCarga").toString());
		String nrCodigoBarras = parameters.get("nrCodigoBarras").toString();
		
		CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(idControleCarga,SessionUtils.getFilialSessao().getIdFilial(), "D");
		
		if(carregamentoDescarga != null){
		getControleCargaConhScanService().store(idControleCarga, nrCodigoBarras, carregamentoDescarga.getIdCarregamentoDescarga() , "D");
		}else{
		
			List eventos = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(SessionUtils.getFilialSessao().getIdFilial(),idControleCarga, "CP");
			
			if(!eventos.isEmpty()) {
				getControleCargaConhScanService().store(idControleCarga, nrCodigoBarras);				
			} else {
				throw new BusinessException("LMS-45094");
    }   
    
		}
    }   
    
    
    public void reabrirDescarga(Map parameters) {
    	Long idCarregamentoDescarga = Long.parseLong(parameters.get("idCarregamentoDescarga").toString());
	
		this.carregamentoDescargaService.storeReabrirCarregamentoDescarga(idCarregamentoDescarga);
		throw new BusinessException("LMS-03020");
    }  
	
	/**
	 * Apaga v�rias entidades atrav�s do Id. E para os mesmos gera um e
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsDocumento(List ids) {
		controleCargaConhScanService.removeByIds(ids);				
	}

	
	
	
	/**
	 * Busca a quantidade de dados da grid de carregamentos
	 */
	public Integer getRowCountDocumento(Map criteria) {
		Long idControleCarga = Long.parseLong(criteria.get("idControleCarga").toString());
		
		CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(idControleCarga, 
				SessionUtils.getFilialSessao().getIdFilial(), "D");
		Long idCarregamentoDescarga = (carregamentoDescarga != null ? carregamentoDescarga.getIdCarregamentoDescarga() : -1);//TODO Ver melhor solu��o

		
		return controleCargaConhScanService.getRowCountByControleCargaAndCarregamentoDescarga(idControleCarga, 
				idCarregamentoDescarga);
	}
	
	
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		findPaginatedDocumento(criteria);
		return super.findPaginated(criteria);
	}
	
	/**
	 * Busca os registros da grid de carregamento.
	 * 
	 */
	public ResultSetPage findPaginatedDocumento(Map criteria) {
		FindDefinition createFindDefinition = FindDefinition.createFindDefinition(criteria);
		Long idControleCarga = Long.parseLong(criteria.get("idControleCarga").toString());
		
		CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(idControleCarga, 
				SessionUtils.getFilialSessao().getIdFilial(), "D");
		
		Long idCarregamentoDescarga = null;
		
		if (carregamentoDescarga != null) {
			idCarregamentoDescarga = carregamentoDescarga.getIdCarregamentoDescarga();
		}

		return controleCargaConhScanService.findPaginatedByControleCargaAndCarregamentoDescarga(idControleCarga, 
				idCarregamentoDescarga, createFindDefinition);
	}
	
	/**
	 * Busca documentos scaneados
	 */
	public ResultSetPage findControleCargaConhScanByIdDoctoServico(Map criteria){
		return controleCargaConhScanService.findPaginated(criteria);
	}
	
	public TypedFlatMap verificaPossibilidadeCriacaoRNCautomatica(TypedFlatMap parameters){
		TypedFlatMap retorno = new TypedFlatMap();
		if(SessionUtils.getFilialSessao().getBlRncAutomaticaDescarga() && "V".equals(parameters.getString("tpControleCarga"))){
			Long idControleCarga = parameters.getLong("idControleCarga");
			Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
			List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = ocorrenciaNaoConformidadeService.executeVerificacaoPossibilidadeCriacaoRNCautomaticaDescarga(idControleCarga, idFilial);
			if(listaDocVolmRNCcriacaoAutomatica != null){
				retorno.put("rncAutomaticaMessage", ocorrenciaNaoConformidadeService.getRncAutomaticaMessage(listaDocVolmRNCcriacaoAutomatica));
				retorno.put("listaDocVolmRNCcriacaoAutomatica", listaDocVolmRNCcriacaoAutomatica);
			}
		}
		return retorno;
	}
	
	public TypedFlatMap abreRNCautomatica(TypedFlatMap parameters){
		TypedFlatMap retorno = new TypedFlatMap();
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = parameters.getList("listaDocVolmRNCcriacaoAutomatica");
		Long idControleCarga = parameters.getLong("idControleCarga");
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		
		if(listaDocVolmRNCcriacaoAutomatica != null){
			ocorrenciaNaoConformidadeService.executeCriacaoRNCautomaticaCarregamento(idControleCarga, idFilial, listaDocVolmRNCcriacaoAutomatica, true);
		}
		return retorno;
	}
	
	/**
	 *	M�todos Get e Set das Classes Services 
	 */
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}
	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public ControleCargaConhScanService getControleCargaConhScanService() {
		return controleCargaConhScanService;
	}

	public void setControleCargaConhScanService(ControleCargaConhScanService controleCargaConhScanService) {
		this.controleCargaConhScanService = controleCargaConhScanService;
	}
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

}
