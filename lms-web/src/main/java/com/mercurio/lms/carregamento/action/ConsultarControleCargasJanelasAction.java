package com.mercurio.lms.carregamento.action;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.carregamento.model.LocalTroca;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PagtoProprietarioCc;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.IntegranteEqOperacService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.carregamento.model.service.LocalTrocaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.MotoristaControleCargaService;
import com.mercurio.lms.carregamento.model.service.PagtoProprietarioCcService;
import com.mercurio.lms.carregamento.model.service.SemiReboqueCcService;
import com.mercurio.lms.carregamento.model.service.VeiculoControleCargaService;
import com.mercurio.lms.carregamento.report.EmitirRelatorioControleCargaManifestoColetaService;
import com.mercurio.lms.carregamento.report.EmitirRelatorioControleCargaManifestoEntregaService;
import com.mercurio.lms.carregamento.report.EmitirRelatorioControleCargaManifestoViagemService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.consultarControleCargasJanelasAction"
 */

public class ConsultarControleCargasJanelasAction {

	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private MdaService mdaService;
	private EventoControleCargaService eventoControleCargaService;
	private FilialService filialService;
	private ControleCargaService controleCargaService;
	private DoctoServicoService doctoServicoService;
	private DomainValueService domainValueService;
	private EquipeOperacaoService equipeOperacaoService;
	private IntegranteEqOperacService integranteEqOperacService;
	private LacreControleCargaService lacreControleCargaService;
	private LocalTrocaService localTrocaService;
	private ManifestoService manifestoService;
	private MotoristaControleCargaService motoristaControleCargaService;
	private PagtoProprietarioCcService pagtoProprietarioCcService;
	private SemiReboqueCcService semiReboqueCcService;
	private VeiculoControleCargaService veiculoControleCargaService;
	private ReportExecutionManager reportExecutionManager;
	private EmitirRelatorioControleCargaManifestoColetaService emitirRelatorioControleCargaManifestoColetaService;
	private EmitirRelatorioControleCargaManifestoEntregaService emitirRelatorioControleCargaManifestoEntregaService;
	private EmitirRelatorioControleCargaManifestoViagemService emitirRelatorioControleCargaManifestoViagemService;
	private ConfiguracoesFacade configuracoesFacade;
	private WorkflowPendenciaService workflowPendenciaService;
	
	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setIntegranteEqOperacService(IntegranteEqOperacService integranteEqOperacService) {
		this.integranteEqOperacService = integranteEqOperacService;
	}
	public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
		this.equipeOperacaoService = equipeOperacaoService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setLacreControleCargaService(LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}
	public void setPagtoProprietarioCcService(PagtoProprietarioCcService pagtoProprietarioCcService) {
		this.pagtoProprietarioCcService = pagtoProprietarioCcService;
	}
	public void setLocalTrocaService(LocalTrocaService localTrocaService) {
		this.localTrocaService = localTrocaService;
	}
	public void setMotoristaControleCargaService(MotoristaControleCargaService motoristaControleCargaService) {
		this.motoristaControleCargaService = motoristaControleCargaService;
	}
	public void setSemiReboqueCcService(SemiReboqueCcService semiReboqueCcService) {
		this.semiReboqueCcService = semiReboqueCcService;
	}
	public void setVeiculoControleCargaService(VeiculoControleCargaService veiculoControleCargaService) {
		this.veiculoControleCargaService = veiculoControleCargaService;
	}
	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}
	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	public EmitirRelatorioControleCargaManifestoColetaService getEmitirRelatorioControleCargaManifestoColetaService() {
		return emitirRelatorioControleCargaManifestoColetaService;
	}
	public void setEmitirRelatorioControleCargaManifestoColetaService(EmitirRelatorioControleCargaManifestoColetaService emitirRelatorioControleCargaManifestoColetaService) {
		this.emitirRelatorioControleCargaManifestoColetaService = emitirRelatorioControleCargaManifestoColetaService;
	}
	public EmitirRelatorioControleCargaManifestoEntregaService getEmitirRelatorioControleCargaManifestoEntregaService() {
		return emitirRelatorioControleCargaManifestoEntregaService;
	}
	public void setEmitirRelatorioControleCargaManifestoEntregaService(EmitirRelatorioControleCargaManifestoEntregaService emitirRelatorioControleCargaManifestoEntregaService) {
		this.emitirRelatorioControleCargaManifestoEntregaService = emitirRelatorioControleCargaManifestoEntregaService;
	}
	public EmitirRelatorioControleCargaManifestoViagemService getEmitirRelatorioControleCargaManifestoViagemService() {
		return emitirRelatorioControleCargaManifestoViagemService;
	}
	public void setEmitirRelatorioControleCargaManifestoViagemService(EmitirRelatorioControleCargaManifestoViagemService emitirRelatorioControleCargaManifestoViagemService) {
		this.emitirRelatorioControleCargaManifestoViagemService = emitirRelatorioControleCargaManifestoViagemService;
	}
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}
	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	/**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedVeiculo(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
    	ResultSetPage rsp = veiculoControleCargaService.findPaginatedByIdControleCarga(idControleCarga, FindDefinition.createFindDefinition(criteria));
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext(); ){
    		TypedFlatMap map = (TypedFlatMap)iter.next();
    		String sgFilialOrigem = map.getString("sgFilialOrigem");
    		String sgFilialDestino = map.getString("sgFilialDestino");
    		if (sgFilialOrigem != null && !sgFilialOrigem.equals("") && sgFilialDestino != null && !sgFilialDestino.equals("")) {
    			map.put("trecho", sgFilialOrigem + " - " + sgFilialDestino);
    			map.remove("sgFilialOrigem");
    			map.remove("sgFilialDestino");
    		}
    	}
    	return rsp;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountVeiculo(TypedFlatMap criteria) {
    	return veiculoControleCargaService.getRowCountFindPaginatedByIdControleCarga(criteria.getLong("controleCarga.idControleCarga"));
    }

	/**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedSemiReboque(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
    	ResultSetPage rsp = semiReboqueCcService.findPaginatedByIdControleCarga(idControleCarga, FindDefinition.createFindDefinition(criteria));
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext(); ){
    		TypedFlatMap map = (TypedFlatMap)iter.next();
    		String sgFilialOrigem = map.getString("sgFilialOrigem");
    		String sgFilialDestino = map.getString("sgFilialDestino");
    		if (sgFilialOrigem != null && !sgFilialOrigem.equals("") && sgFilialDestino != null && !sgFilialDestino.equals("")) {
    			map.put("trecho", sgFilialOrigem + " - " + sgFilialDestino);
    			map.remove("sgFilialOrigem");
    			map.remove("sgFilialDestino");
    		}
    	}
    	return rsp;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountSemiReboque(TypedFlatMap criteria) {
    	return semiReboqueCcService.getRowCountFindPaginatedByIdControleCarga(criteria.getLong("controleCarga.idControleCarga"));
    }
    
	/**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedMotorista(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
    	ResultSetPage rsp = motoristaControleCargaService.findPaginatedByIdControleCarga(idControleCarga, FindDefinition.createFindDefinition(criteria));
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext(); ){
    		TypedFlatMap map = (TypedFlatMap)iter.next();
    		String sgFilialOrigem = map.getString("sgFilialOrigem");
    		String sgFilialDestino = map.getString("sgFilialDestino");
    		if (sgFilialOrigem != null && !sgFilialOrigem.equals("") && sgFilialDestino != null && !sgFilialDestino.equals("")) {
    			map.put("trecho", sgFilialOrigem + " - " + sgFilialDestino);
    			map.remove("sgFilialOrigem");
    			map.remove("sgFilialDestino");
    		}
    		map.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(map.getString("tpIdentificacao.value"), map.getString("nrIdentificacao")) );
    		map.remove("nrIdentificacao");
    	}
    	return rsp;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountMotorista(TypedFlatMap criteria) {
    	return motoristaControleCargaService.getRowCountFindPaginatedByIdControleCarga(criteria.getLong("controleCarga.idControleCarga"));
    }

    /**
     * 
     * @param idLocalTroca
     * @return
     */
    public String findDescricaoLocalTroca(Long idLocalTroca) {
    	LocalTroca localTroca = localTrocaService.findById(idLocalTroca);
    	return localTroca.getDsTroca();
    }

	/**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedPagtoProprietario(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
    	ResultSetPage rsp = pagtoProprietarioCcService.findPaginatedPagtoProprietarioCc(idControleCarga, FindDefinition.createFindDefinition(criteria));
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext(); ){
    		TypedFlatMap map = (TypedFlatMap)iter.next();
    		if (map.getString("moeda.sgMoeda") != null && map.getBigDecimal("vlPagamento") != null) {
    			map.put("moeda.siglaSimbolo", map.getString("moeda.sgMoeda") + " " + map.getString("moeda.dsSimbolo"));
    			map.remove("moeda.sgMoeda");
    			map.remove("moeda.dsSimbolo");
    		}
    		map.put("proprietario.pessoa.nrIdentificacaoFormatado", 
    				FormatUtils.formatIdentificacao(map.getString("proprietario.pessoa.tpIdentificacao.value"), 
    						map.getString("proprietario.pessoa.nrIdentificacao")) );
    		map.remove("proprietario.pessoa.nrIdentificacao");
    	}
    	return rsp;
    }

    
    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountPagtoProprietario(TypedFlatMap criteria) {
    	return pagtoProprietarioCcService.getRowCountPagtoProprietarioCc(criteria.getLong("controleCarga.idControleCarga"));
    }


    public TypedFlatMap findByIdPagtoProprietario(Long idPagtoProprietarioCc) {
    	PagtoProprietarioCc bean = pagtoProprietarioCcService.findByIdByControleCarga(idPagtoProprietarioCc);
    	TypedFlatMap map = new TypedFlatMap();
    	map.put("idPagtoProprietarioCc", bean.getIdPagtoProprietarioCc());
    	if (bean.getControleCarga().getVlFreteCarreteiro() != null) {
	    	map.put("controleCarga.moeda.siglaSimbolo", bean.getControleCarga().getMoeda().getSiglaSimbolo());
	    	map.put("controleCarga.vlFreteCarreteiro", bean.getControleCarga().getVlFreteCarreteiro());
    	}
    	map.put("proprietario.idProprietario", bean.getProprietario().getIdProprietario());
    	map.put("proprietario.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(bean.getProprietario().getPessoa()));
    	map.put("proprietario.pessoa.nmPessoa", bean.getProprietario().getPessoa().getNmPessoa());
    	if (bean.getVeiculoControleCarga().getSolicitacaoContratacao() != null) {
    		map.put("veiculoControleCarga.solicitacaoContratacao.filial.sgFilial", bean.getVeiculoControleCarga().getSolicitacaoContratacao().getFilial().getSgFilial());
    		map.put("veiculoControleCarga.solicitacaoContratacao.nrSolicitacaoContratacao", bean.getVeiculoControleCarga().getSolicitacaoContratacao().getNrSolicitacaoContratacao());
    	}
    	if (bean.getVlPagamento() != null && bean.getMoeda() != null) {
    		map.put("moeda.siglaSimbolo", bean.getMoeda().getSiglaSimbolo());
    		map.put("vlPagamento", bean.getVlPagamento());
    	}
    	map.put("tpVinculo", bean.getVeiculoControleCarga().getMeioTransporte().getTpVinculo().getValue());
    	return map;
    }

    
	/**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedLacre(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
    	ResultSetPage rsp = lacreControleCargaService.findPaginatedByControleCarga(idControleCarga, FindDefinition.createFindDefinition(criteria));
    	return rsp;
    }

    
    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountLacre(TypedFlatMap criteria) {
    	return lacreControleCargaService.getRowCountByControleCarga(criteria.getLong("controleCarga.idControleCarga"));
    }
    

    public TypedFlatMap findByIdLacre(Long idLacreControleCarga) {
    	LacreControleCarga bean = lacreControleCargaService.findByIdByControleCarga(idLacreControleCarga);
    	TypedFlatMap map = new TypedFlatMap();
    	map.put("idLacreControleCarga", bean.getIdLacreControleCarga());
    	map.put("nrLacres", bean.getNrLacres());
    	map.put("dsLocalInclusao", bean.getDsLocalInclusao());
    	map.put("dsLocalConferencia", bean.getDsLocalConferencia());
    	map.put("obInclusaoLacre", bean.getObInclusaoLacre());
    	map.put("obConferenciaLacre", bean.getObConferenciaLacre());
    	map.put("tpStatusLacre", bean.getTpStatusLacre().getDescription().toString());
    	map.put("filialByIdFilialInclusao.sgFilial", bean.getFilialByIdFilialInclusao().getSgFilial());
    	map.put("filialByIdFilialInclusao.pessoa.nmFantasia", bean.getFilialByIdFilialInclusao().getPessoa().getNmFantasia());
    	if (bean.getFilialByIdFilialAlteraStatus() != null) {
	    	map.put("filialByIdFilialAlteraStatus.sgFilial", bean.getFilialByIdFilialAlteraStatus().getSgFilial());
	    	map.put("filialByIdFilialAlteraStatus.pessoa.nmFantasia", bean.getFilialByIdFilialAlteraStatus().getPessoa().getNmFantasia());
    	}
    	return map;
    }


	public ResultSetPage findPaginatedEquipe(TypedFlatMap criteria) {
		Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
		ResultSetPage rsp = equipeOperacaoService.findPaginatedByIdControleCarga(
				null, idControleCarga, null, Boolean.TRUE, FindDefinition.createFindDefinition(criteria));
		List retorno = new ArrayList();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			EquipeOperacao equipeOperacao = (EquipeOperacao) iter.next();
			TypedFlatMap map = new TypedFlatMap();
			map.put("idEquipeOperacao", equipeOperacao.getIdEquipeOperacao());
			map.put("equipe.dsEquipe", equipeOperacao.getEquipe().getDsEquipe());
			map.put("dhInicioOperacao", equipeOperacao.getDhInicioOperacao());
			map.put("dhFimOperacao", equipeOperacao.getDhFimOperacao());
			retorno.add(map);
		}
		rsp.setList(retorno);
		return rsp; 
	}


	public Integer getRowCountEquipe(TypedFlatMap criteria) {
		Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
		Integer integer = equipeOperacaoService.getRowCountByIdControleCarga(null, idControleCarga);
		return integer;
	}
	
	
	public ResultSetPage findPaginatedIntegranteEqOperac(TypedFlatMap criteria) {
		Long idEquipeOperacao = criteria.getLong("idEquipeOperacao");
		ResultSetPage rsp = integranteEqOperacService.findPaginatedByIdEquipeOperacao(idEquipeOperacao, FindDefinition.createFindDefinition(criteria));
		
		List retorno = new ArrayList();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			IntegranteEqOperac bean = (IntegranteEqOperac)iter.next();
			TypedFlatMap map = new TypedFlatMap();
			map.put("idIntegranteEqOperac", bean.getIdIntegranteEqOperac());
			map.put("tpIntegrante", bean.getTpIntegrante());

			if (bean.getUsuario() != null) {
				map.put("nmIntegranteEquipe", bean.getUsuario().getNmUsuario());
				map.put("usuario.nrMatricula", bean.getUsuario().getNrMatricula());
	        }
			else
			if (bean.getPessoa() != null) {
				map.put("nmIntegranteEquipe", bean.getPessoa().getNmPessoa());
				map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(bean.getPessoa()) );
				map.put("pessoa.dhInclusao", bean.getPessoa().getDhInclusao() );
	        }

			if (bean.getCargoOperacional() != null) {
	        	map.put("cargoOperacional.dsCargo", bean.getCargoOperacional().getDsCargo());
	        }
	        if (bean.getEmpresa() != null) {
	        	map.put("empresa.pessoa.nmPessoa", bean.getEmpresa().getPessoa().getNmPessoa());
	        }
			retorno.add(map);
		}
		rsp.setList(retorno);
		return rsp;
	}

	public Integer getRowCountIntegranteEqOperac(TypedFlatMap criteria){
		Long idEquipeOperacao = criteria.getLong("idEquipeOperacao");
				return integranteEqOperacService.getRowCountByIdEquipeOperacao(idEquipeOperacao);
	}

	public TypedFlatMap findByIdEquipeOperacao(Long id) {
		EquipeOperacao equipeOperacao = equipeOperacaoService.findById(id);
		TypedFlatMap map = new TypedFlatMap();
		map.put("idEquipeOperacao", equipeOperacao.getIdEquipeOperacao());
		map.put("equipe.idEquipe", equipeOperacao.getEquipe().getIdEquipe());
		map.put("equipe.dsEquipe", equipeOperacao.getEquipe().getDsEquipe());
		map.put("dhInicioOperacao", equipeOperacao.getDhInicioOperacao());
		map.put("dhFimOperacao", equipeOperacao.getDhFimOperacao());
		return map;
	}
	
    public TypedFlatMap findByIdIntegranteEqOperac(Long id) {
    	IntegranteEqOperac bean = integranteEqOperacService.findById(id);
    	TypedFlatMap map = new TypedFlatMap();
    	map.put("idIntegranteEqOperac", bean.getIdIntegranteEqOperac());
    	map.put("versao", bean.getVersao());
        map.put("tpIntegrante.value", bean.getTpIntegrante().getValue());
        map.put("tpIntegrante.description", bean.getTpIntegrante().getDescription().getValue());
        map.put("equipeOperacao.idEquipeOperacao", bean.getEquipeOperacao().getIdEquipeOperacao());

        if (bean.getTpIntegrante().getValue().equals("F")) {
              map.put("usuario.idUsuario", bean.getUsuario().getIdUsuario());
              map.put("usuario.nrMatricula", bean.getUsuario().getNrMatricula());
              map.put("usuario.nmUsuario", bean.getUsuario().getNmUsuario());
        }
        else
        	if (bean.getTpIntegrante().getValue().equals("T")) {
				map.put("pessoa.idPessoa", bean.getPessoa().getIdPessoa());
				map.put("pessoa.pessoa.nrIdentificacao", bean.getPessoa().getNrIdentificacao());
				map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(bean.getPessoa()));
				map.put("pessoa.tpIdentificacao", bean.getPessoa().getTpIdentificacao().getValue());
				map.put("pessoa.nmPessoa", bean.getPessoa().getNmPessoa());
				map.put("pessoa.dhInclusao", bean.getPessoa().getDhInclusao());
        	}  

        if (bean.getCargoOperacional() != null) {
              map.put("cargoOperacional.idCargoOperacional", bean.getCargoOperacional().getIdCargoOperacional());
              map.put("cargoOperacional.dsCargo", bean.getCargoOperacional().getDsCargo());
        }  
        if (bean.getEmpresa() != null) {
              map.put("empresa.idEmpresa", bean.getEmpresa().getIdEmpresa());
              map.put("empresa.pessoa.nmPessoa", bean.getEmpresa().getPessoa().getNmPessoa());
              map.put("empresa.pessoa.nrIdentificacao", bean.getEmpresa().getPessoa().getNrIdentificacao());
              map.put("empresa.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(bean.getEmpresa().getPessoa()));
        }
    	return map;
    }


	/**
     * 
     * @param criteria
     * @return
     */
    public List findPaginatedManifestos(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
    	List lista = manifestoService.findPaginatedManifestoByControleCarga(idControleCarga);
    	return lista;
    }

    
    public TypedFlatMap findDadosControleCargaByManifesto(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("idControleCarga");
    	ControleCarga cc = controleCargaService.findById(idControleCarga);
    	
    	TypedFlatMap map = new TypedFlatMap();
    	
    	BigDecimal valorTotalFrota = controleCargaService.findValorTotalFrota(idControleCarga);
    	map.put("vlTotalFrota", valorTotalFrota);
    	map.put("moeda.siglaSimbolo", cc.getMoeda() != null ? cc.getMoeda().getSiglaSimbolo() : 
    		SessionUtils.getMoedaSessao().getSiglaSimbolo());
    	
    	map.put("tpControleCarga", cc.getTpControleCarga().getValue());
    	if (cc.getMeioTransporteByIdTransportado() != null) {
	    	map.put("meioTransporteByIdTransportado.nrFrota", cc.getMeioTransporteByIdTransportado().getNrFrota());
	    	map.put("meioTransporteByIdTransportado.nrIdentificador", cc.getMeioTransporteByIdTransportado().getNrIdentificador());
    	}
    	
    	BigDecimal vlTotalFrete = controleCargaService.findSomaVlTotalFreteByControleCarga(cc);
    	map.put("vlTotalFrete", vlTotalFrete);
    	return map;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedDocumentos(TypedFlatMap criteria) {
    	String strDhEmissao = criteria.getString("dhEmissaoManifesto");
    	Boolean blPreManifesto = strDhEmissao == null || strDhEmissao.equals("");
    	ResultSetPage rsp = doctoServicoService.findPaginatedDoctoServicoByManifesto(
    																criteria.getString("manifesto.tpManifesto.value"),
    																blPreManifesto,
    																criteria.getLong("idManifesto"), 
    																criteria.getLong("doctoServico.idDoctoServico"),
    																criteria.getString("doctoServico.tpDocumentoServico"), 
    																criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"),
    																FindDefinition.createFindDefinition(criteria));
    	List lista = rsp.getList();
    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
    		Map map = (Map)iter.next();
    		if (map.get("vlMercadoria") == null) {
    			map.remove("sgMoeda");
    			map.remove("dsSimbolo");
    		}
    		if (map.get("vlTotalDocServico") != null) {
    			map.put("sgMoedaVlTotal", map.get("sgMoeda"));
    			map.put("dsSimboloVlTotal", map.get("dsSimbolo"));
    		}
    	}
    	rsp.setList(lista);
    	return rsp;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountDocumentos(TypedFlatMap criteria) {
    	Boolean blPreManifesto = criteria.get("dhEmissaoManifesto") == null;
    	return doctoServicoService.getRowCountDoctoServicoByManifesto(criteria.getString("manifesto.tpManifesto.value"), 
    			blPreManifesto, criteria.getLong("idManifesto"), criteria.getLong("doctoServico.idDoctoServico"), 
    			criteria.getString("doctoServico.tpDocumentoServico"), criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
    }


    /**
     * 
     * @param criteria
     * @return
     */
    public TypedFlatMap findDadosManifesto(TypedFlatMap criteria) {
    	Manifesto manifesto = manifestoService.findById(criteria.getLong("idManifesto"));

    	TypedFlatMap map = new TypedFlatMap();
    	Long nrManifesto = manifesto.getNrPreManifesto();
    	String tpManifesto = manifesto.getTpManifesto().getValue();
    	map.put("idManifesto", manifesto.getIdManifesto());
    	map.put("filialByIdFilialOrigem.sgFilial", manifesto.getFilialByIdFilialOrigem().getSgFilial());
    	map.put("dhEmissaoManifesto", manifesto.getDhEmissaoManifesto());

    	if (tpManifesto.equals("E")) {
    		map.put("tpManifesto.description", domainValueService.findDomainValueDescription("DM_TAG_MANIFESTO", "EN"));
    		if (manifesto.getDhEmissaoManifesto() != null) {
    			nrManifesto = Long.valueOf(manifesto.getManifestoEntrega().getNrManifestoEntrega().toString());
    		}
    	}
    	else {
    		tpManifesto += manifesto.getTpAbrangencia().getValue();
	    	if (tpManifesto.equals("VN")) {
	    		map.put("tpManifesto.description", domainValueService.findDomainValueDescription("DM_TAG_MANIFESTO", "VN"));
	    		if (manifesto.getDhEmissaoManifesto() != null) {
	    			nrManifesto = Long.valueOf(manifesto.getManifestoViagemNacional().getNrManifestoOrigem().toString());
	    		}
	    	}
	    	else
	    	if (tpManifesto.equals("VI")) {
	    		map.put("tpManifesto.description", domainValueService.findDomainValueDescription("DM_TAG_MANIFESTO", "VI"));
	    		if (manifesto.getDhEmissaoManifesto() != null) {
	    			nrManifesto = manifesto.getManifestoInternacional().getNrManifestoInt();
	    		}
	    	}
    	}

    	map.put("nrManifesto", nrManifesto);
    	map.put("tpManifesto.value", tpManifesto);
    	return map;
    }

	/**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedEventos(Map criteria) {
    	ResultSetPage rsp = eventoControleCargaService.findPaginated(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
    		EventoControleCarga evc = (EventoControleCarga)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idEventoControleCarga", evc.getIdEventoControleCarga());
    		tfm.put("tpEventoControleCarga", evc.getTpEventoControleCarga());
    		tfm.put("dhEvento", evc.getDhEvento());
    		
    		if(evc.getDhEventoSolicitacao() != null){
    			tfm.put("dhSolicitada", evc.getDhEventoSolicitacao());
    		}
    		if(evc.getTpSituacaoPendencia() != null){
    			tfm.put("situacaoPendencia", evc.getTpSituacaoPendencia().getDescriptionAsString());
    		}
    		if(evc.getDhEventoOriginal() != null){
    			tfm.put("dhOriginal", evc.getDhEventoOriginal());
    		}
    		if(evc.getUsuarioSolicitacao() != null){
    			tfm.put("usuarioSolicitacao", evc.getUsuarioSolicitacao().getNmUsuario());
    		}
    		 
    		tfm.put("filial.sgFilial", evc.getFilial().getSgFilial());
    		if (evc.getUsuario() != null) {
    			tfm.put("usuario.nmUsuario", evc.getUsuario().getNmUsuario());
    		}
    		retorno.add(tfm);
    	}
    	rsp.setList(retorno);
    	return rsp;
    }
    
    public EventoControleCarga findById(java.lang.Long id) {
    	return eventoControleCargaService.findById(id);
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountEventos(Map criteria) {
    	return eventoControleCargaService.getRowCount(criteria);
    }

    /**
     * M�todo que popula a combo de tipos de documento apenas com CTR, NFT, CRT, MDA.
     * 
     * @param criteria
     * @return List
     */
    public List findTipoDocumentoServico(Map criteria) {
        List dominiosValidos = new ArrayList();
        dominiosValidos.add("CTR");
        dominiosValidos.add("NFT");
        dominiosValidos.add("CRT");
        dominiosValidos.add("MDA");
        dominiosValidos.add("NTE");
        dominiosValidos.add("CTE");
        List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
        return retorno;
    }
    
    /**
     * Busca a filial baseado no documento de servi�o
     * @param criteria
     * @return
     */
    public List findLookupFilialByDocumentoServico(Map criteria) {
    	FilterList filter = new FilterList(filialService.findLookup(criteria)) {
			public Map filterItem(Object item) {
				Filial filial = (Filial)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idFilial", filial.getIdFilial());
		    	typedFlatMap.put("sgFilial", filial.getSgFilial());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }
    
    public File executeRelatorioDocumentosControleCarga(TypedFlatMap parameters) throws Exception {
		String tpManifesto = parameters.getString("tpOperacao");
		
		parameters.put("idControleCarga", parameters.getString("controleCarga.idControleCarga") );
		parameters.put("idManifesto", parameters.getString("idManifestoReport") );
		
		if(ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_COLETA.equalsIgnoreCase(tpManifesto)){
			return this.reportExecutionManager.executeReport(this.emitirRelatorioControleCargaManifestoColetaService, parameters);
		}else if(ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_ENTREGA.equalsIgnoreCase(tpManifesto)){
			return this.reportExecutionManager.executeReport(this.emitirRelatorioControleCargaManifestoEntregaService, parameters);
		}else if( ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_VIAGEM.equalsIgnoreCase(tpManifesto) ) {
			return this.reportExecutionManager.executeReport(this.emitirRelatorioControleCargaManifestoViagemService, parameters);
		}
		
		return null;
	}
    
    /**
     * Insere uma nova pend�ncia para altera��o da data de sa�da de portaria
     * @param criteria - Contendo a data da sa�da da portaria solicitada, observa��o preenchida e 
     * id do evendo controle de carga.
     */
    public Map alteraDtSaidaPortaria(TypedFlatMap criteria){
        Map<String, Object> result = new HashMap<String, Object>();
        if(criteria.getLong("idEvento") != null){
            if(criteria.getString("dtSaidaPortaria") == null || criteria.getString("dtSaidaPortaria").isEmpty()){
                throw new BusinessException("LMS-00077");
            }
            DateTime dateSolicitacao = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(criteria.getString("dtSaidaPortaria"));
            if(dateSolicitacao.isBefore(new DateTime())){
                EventoControleCarga eventoFindedById = eventoControleCargaService.findById(criteria.getLong("idEvento"));
                String horaMinutoSegundo = eventoFindedById.getDhEvento().getHourOfDay() + ":" 
                                                    +  eventoFindedById.getDhEvento().getMinuteOfHour() + ":" 
                                                    +  eventoFindedById.getDhEvento().getSecondOfMinute();
                dateSolicitacao = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(criteria.getString("dtSaidaPortaria") + " " + horaMinutoSegundo);
            
                if(isEventoSaidaPortaria(eventoFindedById) && eventoPodeTerAlteracaoDataSaida(eventoFindedById)){
                        String observacao = criteria.getString("observacao");
                        Pendencia pendencia = geraWorkflowTrocaDataSaidaPortaria(eventoFindedById, dateSolicitacao, observacao);
                        salvaEventoControleCargaComPendencia(eventoFindedById, pendencia, dateSolicitacao, observacao);
                        result.put("done", "ok");
                }
            } else {
                throw new BusinessException("LMS-26164");
            }
        }
        return result;
    }
    
    private Pendencia geraWorkflowTrocaDataSaidaPortaria(EventoControleCarga evento, DateTime dateSolicitacao, String observacao){
    	Long idFilial = evento.getFilial().getIdFilial();
    	Long idProcesso = evento.getIdEventoControleCarga();
        ControleCarga controleCarga = controleCargaService.findById(evento.getControleCarga().getIdControleCarga());
        String nrControleCarga = controleCarga.getNrControleCarga().toString();
        String filial = controleCarga.getFilialByIdFilialOrigem().getSgFilial();
        String dataOriginal = DateTimeFormat.forPattern("dd/MM/yyyy").print(evento.getDhEvento());
        String dataSolicitada = DateTimeFormat.forPattern("dd/MM/yyyy").print(dateSolicitacao);
        
        Object[] dados =  new Object[]{filial, nrControleCarga, dataOriginal, dataSolicitada, observacao};
    	String dsProcesso = this.getConfiguracoesFacade().getMensagem("LMS-26165", dados);
    	Short nrTipoEvento = 4607;
    	return getWorkflowPendenciaService().generatePendencia(idFilial, nrTipoEvento, idProcesso, dsProcesso, JTDateTimeUtils.getDataHoraAtual());
    }
    
    private void salvaEventoControleCargaComPendencia(EventoControleCarga evento, Pendencia pendencia, DateTime dateSolicitacao, String observacao){
    	evento.setTpSituacaoPendencia(pendencia.getTpSituacaoPendencia());
    	evento.setUsuarioSolicitacao(SessionUtils.getUsuarioLogado());
    	evento.setDhEventoSolicitacao(dateSolicitacao);
    	evento.setPendencia(pendencia);
    	evento.setDsObservacao(observacao);
    	eventoControleCargaService.store(evento);
    }

    /**
     * Realiza a busca da informa��es necess�rias para preencher os campos presentes
     * na tela consultarControleCargasEventos.jsp
     * @param criteria - Contendo o id do evento controle de carga
     * @return Map - Contendo dataSaidaPortaria (data do evendo), observa��o e id do evento controle de carga
     */
    public Map buscaDtSaidaPortariaById(TypedFlatMap criteria){
		Long idEventoControleCarga = criteria.getLong("id");
		EventoControleCarga eventoFindedById = eventoControleCargaService.findById(idEventoControleCarga);
		Map<String, Object> result = new HashMap<String, Object>();
		if(isEventoSaidaPortaria(eventoFindedById) && eventoPodeTerAlteracaoDataSaida(eventoFindedById)){
			ControleCarga controleCarga = controleCargaService.findById(eventoFindedById.getControleCarga().getIdControleCarga());
			if (controleCarga != null && (controleCarga.getTpStatusControleCarga().equals(new DomainValue("AD")) || controleCarga.getTpStatusControleCarga().equals(new DomainValue("TC")))) {
				result.put("dataSaidaPortaria", new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(eventoFindedById.getDhEvento().toDate()));
				result.put("observacao", eventoFindedById.getDsObservacao());
				result.put("idEvento", idEventoControleCarga);
			}
		}
		return result;
    }

    public boolean isEventoSaidaPortaria(EventoControleCarga evento){
    	return evento != null && evento.getTpEventoControleCarga().equals(new DomainValue("SP"));
    }
    
    public boolean eventoPodeTerAlteracaoDataSaida(EventoControleCarga evento){
    	return evento.getPendencia() == null || !evento.getTpSituacaoPendencia().equals(new DomainValue("E"));
    }
    
    public List findLookupServiceDocumentFilialCTR(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialNFT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCRT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialNTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }

    public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCRT(Map criteria) {
    	return ctoInternacionalService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	return mdaService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNTE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCTE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
}