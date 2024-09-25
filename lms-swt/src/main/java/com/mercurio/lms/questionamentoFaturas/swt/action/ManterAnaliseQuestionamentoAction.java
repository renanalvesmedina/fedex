package com.mercurio.lms.questionamentoFaturas.swt.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.service.MotivoDescontoService;
import com.mercurio.lms.contasreceber.model.service.MotivoOcorrenciaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;

public class ManterAnaliseQuestionamentoAction {
	private QuestionamentoFaturasService questionamentoFaturasService;
	private FilialService filialService;
	private UsuarioService usuarioService;
	private ClienteService clienteService;
	private MotivoOcorrenciaService motivoOcorrenciaService; 
	private MotivoDescontoService motivoDescontoService;

    public Map<String, Object> findByIdMapped(Long id) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	QuestionamentoFatura questionamentoFatura = questionamentoFaturasService.findById(id);
    	if(questionamentoFatura != null) {
    		map.put("idQuestionamentoFatura", questionamentoFatura.getIdQuestionamentoFatura());
			map.put("idFilialCobradora", questionamentoFatura.getFilialCobradora().getIdFilial());
			map.put("sgFilialCobradora", questionamentoFatura.getFilialCobradora().getSgFilial());
			map.put("nmFilialCobradora", questionamentoFatura.getFilialCobradora().getPessoa().getNmFantasia());

			map.put("idFilialSolicitante", questionamentoFatura.getFilialSolicitante().getIdFilial());
			map.put("sgFilialSolicitante", questionamentoFatura.getFilialSolicitante().getSgFilial());
			map.put("nmFilialSolicitante", questionamentoFatura.getFilialSolicitante().getPessoa().getNmFantasia());

			// LMS-6109 - busca dados da filial debitada
			Filial filDebitada = questionamentoFaturasService.findFilialDebitada(questionamentoFatura);
			if (filDebitada != null) {
				Filial filialDebitada = filialService.findByIdJoinPessoa(filDebitada.getIdFilial());
				map.put("idFilialDebitada", filialDebitada.getIdFilial());
				map.put("sgFilialDebitada", filialDebitada.getSgFilial());
				map.put("nmFilialDebitada", filialDebitada.getPessoa().getNmFantasia());
			}

			// LMS-6109 - busca dados das filiais de origem da fatura e dos documentos de serviço
			map.put("idFilialOrigemFatura", questionamentoFaturasService.findIdFilialOrigemFatura(questionamentoFatura));
			map.put("listIdFilialOrigemDoctoServico", questionamentoFaturasService.findListIdFilialOrigemDoctoServico(questionamentoFatura));

			map.put("idCliente", questionamentoFatura.getCliente().getIdCliente());
			Pessoa pessoa = questionamentoFatura.getCliente().getPessoa();
			map.put("nrIdentificacao", FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao()));
			map.put("nmCliente", pessoa.getNmPessoa());

			map.put("idUsuarioSolicitante", questionamentoFatura.getUsuarioSolicitante().getUsuarioADSM().getIdUsuario());
			map.put("nrMatriculaSolicitante", questionamentoFatura.getUsuarioSolicitante().getUsuarioADSM().getNrMatricula());
			map.put("nmUsuarioSolicitante", questionamentoFatura.getUsuarioSolicitante().getUsuarioADSM().getNmUsuario());
			UsuarioLMS usuarioApropriador = questionamentoFatura.getUsuarioApropriador();
			if(usuarioApropriador != null) {
				map.put("idUsuarioApropriador", usuarioApropriador.getUsuarioADSM().getIdUsuario());
				map.put("nrMatriculaApropriador", usuarioApropriador.getUsuarioADSM().getNrMatricula());
				map.put("nmUsuarioApropriador", usuarioApropriador.getUsuarioADSM().getNmUsuario());
			}
			MotivoDesconto motivoDesconto = questionamentoFatura.getMotivoDesconto();
			if(motivoDesconto != null) {
				map.put("idMotivoDesconto", motivoDesconto.getIdMotivoDesconto());
			}
			MotivoOcorrencia motivoCancelamento = questionamentoFatura.getMotivoCancelamento();
			if(motivoCancelamento != null) {
				map.put("idMotivoCancelamento", motivoCancelamento.getIdMotivoOcorrencia());
			}
			MotivoOcorrencia motivoSustacaoProtesto = questionamentoFatura.getMotivoSustacaoProtesto();
			if(motivoSustacaoProtesto != null) {
				map.put("idMotivoSustacaoProtesto", motivoSustacaoProtesto.getIdMotivoOcorrencia());
			}
			MotivoOcorrencia motivoProrrogacaoVcto = questionamentoFatura.getMotivoProrrogacaoVcto();
			if(motivoProrrogacaoVcto != null) {
				map.put("idMotivoProrrogacaoVcto", motivoProrrogacaoVcto.getIdMotivoOcorrencia());
			}
			map.put("tpDocumento", questionamentoFatura.getTpDocumento().getValue());
			
			if ("R".equals(questionamentoFatura.getTpDocumento().getValue())) {
				Long nrDoctoServico = questionamentoFatura.getFatura().getNrFatura();
				String sgFilialOrigem = questionamentoFatura.getFatura().getFilialByIdFilial().getSgFilial();
				map.put("sgFilialOrigem", sgFilialOrigem);
				map.put("nrDocumento", nrDoctoServico);
			} else {
				Long nrDoctoServico = questionamentoFatura.getDoctoServico().getNrDoctoServico();
				String sgFilialOrigem = questionamentoFatura.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial();
				
				map.put("sgFilialOrigem", sgFilialOrigem);
				map.put("nrDocumento", nrDoctoServico);
			}
			
			map.put("blConcedeAbatimentoSol", questionamentoFatura.getBlConcedeAbatimentoSol());
			map.put("blProrrogaVencimentoSol", questionamentoFatura.getBlProrrogaVencimentoSol());
			map.put("blSustarProtestoSol", questionamentoFatura.getBlSustarProtestoSol());
			map.put("blBaixaTitCancelSol", questionamentoFatura.getBlBaixaTitCancelSol());
			map.put("blRecalcularFreteSol", questionamentoFatura.getBlRecalcularFreteSol());

			/** Booleanos */
			map.put("blConcedeAbatimentoAprov", BooleanUtils.toString(questionamentoFatura.getBlConcedeAbatimentoAprov(), "S", "N", null));
			map.put("blProrrogaVencimentoAprov", BooleanUtils.toString(questionamentoFatura.getBlProrrogaVencimentoAprov(), "S", "N", null));
			map.put("blSustarProtestoAprov", BooleanUtils.toString(questionamentoFatura.getBlSustarProtestoAprov(), "S", "N", null));
			map.put("blBaixaTitCancelAprov", BooleanUtils.toString(questionamentoFatura.getBlBaixaTitCancelAprov(), "S", "N", null));
			map.put("blRecalcularFreteAprov", BooleanUtils.toString(questionamentoFatura.getBlRecalcularFreteAprov(), "S", "N", null));

			map.put("dtEmissaoDocumento", questionamentoFatura.getDtEmissaoDocumento());
			map.put("vlDocumento", questionamentoFatura.getVlDocumento());
			DomainValue tpSituacaoBoleto = questionamentoFatura.getTpSituacaoBoleto();
			if(tpSituacaoBoleto != null) {
				map.put("tpSituacaoBoleto", tpSituacaoBoleto.getValue());
			}
			map.put("dtVencimentoDocumento", questionamentoFatura.getDtVencimentoDocumento());
			map.put("nrBoleto", questionamentoFatura.getNrBoleto());
			map.put("dhSolicitacao", questionamentoFatura.getDhSolicitacao());
			map.put("dhConclusao", questionamentoFatura.getDhConclusao());
			map.put("vlAbatimentoSolicitado", questionamentoFatura.getVlAbatimentoSolicitado());
			map.put("dtVencimentoSolicitado", questionamentoFatura.getDtVencimentoSolicitado());
			DomainValue tpSituacao = questionamentoFatura.getTpSituacao();
			if(tpSituacao != null) {
				map.put("tpSituacao", tpSituacao.getValue());
			}
			DomainValue tpSetorCausadorAbatimento = questionamentoFatura.getTpSetorCausadorAbatimento();
			if(tpSetorCausadorAbatimento != null) {
				map.put("tpSetorCausadorAbatimento", tpSetorCausadorAbatimento.getValue());
			}
			map.put("dsChaveLiberacao", questionamentoFatura.getDsChaveLiberacao());
			map.put("dsEmailRetorno", questionamentoFatura.getDsEmailRetorno());
			map.put("obAcaoCorretiva", questionamentoFatura.getObAcaoCorretiva());
			map.put("obAbatimento", questionamentoFatura.getObAbatimento());
    	}
		return map;
    }

	public Map<String, Object> store(Map<String, Object> criteria) {
		Long idQuestionamentoFatura = (Long)criteria.get("idQuestionamentoFatura");

		QuestionamentoFatura questionamentoFatura = new QuestionamentoFatura();
		questionamentoFatura.setIdQuestionamentoFatura(idQuestionamentoFatura);
		questionamentoFaturasService.store(questionamentoFatura);

		Map<String, Object> mapRetorno = new HashMap<String, Object>();
		mapRetorno.put("idQuestionamentoFatura", questionamentoFatura.getIdQuestionamentoFatura());
		return mapRetorno;
	}

	public Integer getRowCount(Map criteria) {
		return Integer.valueOf(this.findPaginated(criteria).getRowCount().intValue());
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginated(Map<String, Object> criteria) {
		String tpDocumento = MapUtils.getString(criteria, "tpDocumento");
		String nrDocumento = MapUtils.getString(criteria, "nrDocumento");
		
		String tipo = null;
		if (tpDocumento != null) {
			try {
				tipo = questionamentoFaturasService.findValorTipoDocumento(tpDocumento);
			} catch (IllegalArgumentException exception) {
			}
		}
		
		Long nrDocumentoComp = null;
		if( tipo != null && nrDocumento!= null ){
			nrDocumentoComp = Long.valueOf(nrDocumento);
		} else if(tipo == null){
			if (StringUtils.isNotBlank(nrDocumento)){
				nrDocumentoComp = Long.valueOf(nrDocumento);
			}
			if (StringUtils.isNotBlank(tpDocumento)){
				criteria.put("tpDocumento", tpDocumento);
			}
		}

		criteria.put("nrDocumento", nrDocumentoComp);

    	List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();
		ResultSetPage resultSetPage = questionamentoFaturasService.findPaginated(new PaginatedQuery(criteria));
		List<QuestionamentoFatura> result = resultSetPage.getList();
		for (QuestionamentoFatura questionamentoFatura : result) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idQuestionamentoFatura", questionamentoFatura.getIdQuestionamentoFatura());
			Pessoa pessoa = questionamentoFatura.getCliente().getPessoa();
			map.put("dsCliente", FormatUtils.formatIdentificacao(pessoa).concat(" - ").concat(pessoa.getNmPessoa()));
			map.put("dhSolicitacao", questionamentoFatura.getDhSolicitacao());
			map.put("sgFilialCobradora", questionamentoFatura.getFilialCobradora().getSgFilial());
			map.put("nmUsuarioSolicitante", questionamentoFatura.getUsuarioSolicitante().getUsuarioADSM().getNmUsuario());
			map.put("documentoFullDescription", questionamentoFaturasService.getDocumentoFullDescription(questionamentoFatura));
			map.put("tpSituacao", questionamentoFatura.getTpSituacao());
			UsuarioLMS usuarioApropriador = questionamentoFatura.getUsuarioApropriador();
			if(usuarioApropriador != null) {
				map.put("nmUsuarioApropriador", usuarioApropriador.getUsuarioADSM().getNmUsuario());
			}
			map.put("dhConclusao", questionamentoFatura.getDhConclusao());
			toReturn.add(map);
		}
		resultSetPage.setList(toReturn);
		return resultSetPage;
	}

    /**
     * Dados iniciais da tela
     * @return
     */
    public Map<String, Object> findSessionData() {
    	Map<String, Object> toReturn = new HashMap<String, Object>();
    	Filial filialSessao = SessionUtils.getFilialSessao();
		if(!SessionUtils.isFilialSessaoMatriz()) {
    		toReturn.put("idFilial", filialSessao.getIdFilial());
    		toReturn.put("sgFilial", filialSessao.getSgFilial());
    		toReturn.put("nmFilial", filialSessao.getPessoa().getNmFantasia());
    	}
    	return toReturn;
    }

    /**
     * Valida Inicio de Analise do Usuario
     * @param idQuestionamentoFatura
     */
    public Map<String, Object> storeIniciarAnalise(Long idQuestionamentoFatura) {
    	return questionamentoFaturasService.storeIniciarAnalise(idQuestionamentoFatura);
    }

    /**
     * Processo de Conclusão da Análise.
     * @param idQuestionamentoFatura
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> storeConcluirAnalise(Map<String, Object> criteria) {
    	WarningCollectorUtils.remove();
    	questionamentoFaturasService.storeValidateConcluirAnalise(criteria,JTDateTimeUtils.getDataHoraAtual());

    	TypedFlatMap toReturn = new TypedFlatMap();
		WarningCollectorUtils.putAll(toReturn);
		return toReturn;
    }

    /**
     * Verifica se Perfil do Usuario possui permissao de acesso, de acordo com a situação do Questionamento
     * @param tpSituacao
     * @return
     */
    public Map<String, Object> verifyUserAcess(String tpSituacao) {
    	Boolean userCanAccess = questionamentoFaturasService.allowUserAccess(tpSituacao);
    	Map<String, Object> toReturn = new HashMap<String, Object>();
    	toReturn.put("userCanAccess", userCanAccess);
		return toReturn;
    }

    @SuppressWarnings("rawtypes")
    public List findLookupFilial(Map criteria) {
		return filialService.findLookup(criteria); 
	}

    @SuppressWarnings("rawtypes")
    public List findLookupUsuarioFuncionario(Map criteria){
		return usuarioService.findLookupUsuarioFuncionario(
			null,
			FormatUtils.fillNumberWithZero(((Integer)criteria.get("nrMatricula")).toString(), 9),
			(Long)criteria.get("idFilial"),
			null,
			null,
			null,
			true
		);
	}

    public List<Map<String, Object>> findLookupCliente(Map<String, Object> criteria) {
		List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();
		String nrIdentificacao = MapUtils.getString(criteria, "nrIdentificacao");
		String nrIdentificacaoCustom = MapUtils.getString(criteria, "nrIdentificacaoCustom");

		List<Cliente> clientes = clienteService.findLookupClienteCustom(nrIdentificacao, nrIdentificacaoCustom);
		for (Cliente cliente : clientes) {
			Map<String, Object> mapReturn = new HashMap<String, Object>();
			Pessoa pessoa = cliente.getPessoa();

			if(StringUtils.isBlank(nrIdentificacaoCustom)) {
				mapReturn.put("idCliente", cliente.getIdCliente());
				mapReturn.put("nrIdentificacao", FormatUtils.formatIdentificacao(pessoa));
			}
			if("J".equals(pessoa.getTpPessoa().getValue())) {
				mapReturn.put("nrIdentificacaoCustom", StringUtils.left(pessoa.getNrIdentificacao(), 8));
			}
			mapReturn.put("nmCliente", pessoa.getNmPessoa());
			toReturn.add(mapReturn);

			/** Se foi feita uma consulta parcial, retorna apenas primeiro registro */
			if(StringUtils.isNotBlank(nrIdentificacaoCustom)) {
				break;
			}
		}
		return toReturn;
	}

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findMotivoOcorrenciaDeCancelamento(){
    	return this.convertMotivoBeanToMap("idMotivoCancelamento", motivoOcorrenciaService.findMotivoOcorrenciaDeCancelamento());
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findMotivoOcorrenciaDeVencimento(){
		return this.convertMotivoBeanToMap("idMotivoProrrogacaoVcto", motivoOcorrenciaService.findMotivoOcorrenciaDeVencimento());
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findMotivoOcorrenciaDeSustacao(){
    	return this.convertMotivoBeanToMap("idMotivoSustacaoProtesto", motivoOcorrenciaService.findMotivoOcorrenciaDeSustacao());
    }
    private List<Map<String, Object>> convertMotivoBeanToMap(String idProperty,  List<MotivoOcorrencia> resultBean) {
    	List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();
		for (MotivoOcorrencia motivoOcorrencia : resultBean) {
			Map<String, Object> mapReturn = new HashMap<String, Object>();
			mapReturn.put(idProperty, motivoOcorrencia.getIdMotivoOcorrencia());
			mapReturn.put("dsMotivoOcorrencia", motivoOcorrencia.getDsMotivoOcorrencia().getValue());
			toReturn.add(mapReturn);
		}
		return toReturn;
    }

    public List<MotivoDesconto> findMotivoDescontos() {
    	List<MotivoDesconto> motivosDesconto = motivoDescontoService.findMotivoDescontoByTpSituacao(ConstantesVendas.SITUACAO_ATIVO);
    	for (MotivoDesconto motivoDesconto : motivosDesconto) {
    		motivoDesconto.setDsMotivoDesconto(new VarcharI18n(motivoDesconto.getCdMotivoDesconto().concat(" - ").concat(motivoDesconto.getDsMotivoDesconto().getValue())));
		}
		return motivosDesconto;
    }

	public void setQuestionamentoFaturasService(QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setMotivoOcorrenciaService(MotivoOcorrenciaService motivoOcorrenciaService) {
		this.motivoOcorrenciaService = motivoOcorrenciaService;
	}
	public void setMotivoDescontoService(MotivoDescontoService motivoDescontoService) {
		this.motivoDescontoService = motivoDescontoService;
	}
}