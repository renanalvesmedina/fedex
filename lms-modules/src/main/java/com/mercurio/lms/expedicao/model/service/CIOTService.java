package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.ciot.CIOTAlteracaoIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTBuscaIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTBuscaIntegracaoPUDDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTCancelamentoIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTEncerramentoIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTSolicitacaoIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.jms.Queues;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.service.ContaBancariaService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.CIOTControleCarga;
import com.mercurio.lms.expedicao.model.dao.CIOTDAO;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe de serviço para CRUD:   
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.ciotService"
 */
public class CIOTService extends CrudService<CIOT, Long> {
	
	private static final String VIAGEM = "V";
	private static final String INDICADOR_CIOT = "INDICADOR_CIOT";
	private static final String SIM = "S";
	private static final String PROPRIO = "P";
	private static final String FISICA = "F";
	private static final String COLETA_ENTREGA = "C";
	private static final String BAIRRO_PADRAO = "Bairro_padrao";
	private static final String SOLICITACAO_REJEITADA = "R";
	private static final String ALTERACAO_REJEITADA = "D";
	private static final String ENCERRAMENTO_REJEITADO = "M";
	private static final String CANCELAMENTO_REJEITADO = "O";
	private static final String REENVIADO = "N";
	private static final String ADICIONAIS_NOME = "Pagamento Direto Pela Contratante";
	private static final String NOME_BANCO = "Nome do Banco: ";
	private static final String NUMERO_BANCO = "Número do Banco: ";
	private static final String AGENCIA = "Agência: ";
	private static final String CONTA = "Conta: ";
	
	private IntegracaoJmsService integracaoJmsService;
	private ReportExecutionManager reportExecutionManager;
	private ControleCargaService controleCargaService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ParametroGeralService parametroGeralService;
	private InscricaoEstadualService inscricaoEstadualService;
	private CIOTControleCargaService ciotControleCargaService;
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private EmpresaService empresaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ContaBancariaService contaBancariaService;
	
	public void setCIOTDAO(CIOTDAO dao) {
		setDao(dao);
	}
	
	private CIOTDAO getCIOTDAO(){
		return (CIOTDAO) getDao();
	}
	
	public Serializable store(CIOT bean) {
		return super.store(bean);
	}
	
	public CIOT findById(Long id) {
		return (CIOT) super.findById(id);
	}
	
	public String findRelatorioCIOT(TypedFlatMap criteria) {
		List<Map<String, Object>> listForCSV = this.getCIOTDAO().findRelatorioCIOT(criteria);
		return reportExecutionManager.generateReportLocator(listForCSV, Boolean.TRUE);
	}
	
	public CIOT executeReenviar(CIOTControleCarga ciotControleCarga) {
		CIOT ciot = ciotControleCarga.getCiot();
		ControleCarga controleCarga = ciotControleCarga.getControleCarga();
		
		CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO = new CIOTBuscaIntegracaoDTO();
		ciotBuscaIntegracaoDTO.setIdControleCarga(controleCarga.getIdControleCarga());

		JmsMessageSender jmsMessageSender = null;
		if(SOLICITACAO_REJEITADA.equals(ciot.getTpSituacao().getValue())){
			CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO = this.findDadosParaCiot(ciotBuscaIntegracaoDTO, true);
			ciotSolicitacaoIntegracaoDTO.setIdCiot(ciot.getIdCIOT());
            jmsMessageSender = integracaoJmsService.createMessage(Queues.CIOT_SOLICITACAO_ENVIO, ciotSolicitacaoIntegracaoDTO);
		} else if(ALTERACAO_REJEITADA.equals(ciot.getTpSituacao().getValue())){
			CIOTAlteracaoIntegracaoDTO ciotAlteracaoIntegracaoDTO = this.findDadosParaAlteracaoCiot(ciotBuscaIntegracaoDTO);
			jmsMessageSender = integracaoJmsService.createMessage(Queues.CIOT_ALTERACAO_ENVIO, ciotAlteracaoIntegracaoDTO);
			
		} else if(ENCERRAMENTO_REJEITADO.equals(ciot.getTpSituacao().getValue())){
			CIOTAlteracaoIntegracaoDTO ciotAlteracaoIntegracaoDTO = this.findDadosParaAlteracaoCiot(ciotBuscaIntegracaoDTO);
			CIOTEncerramentoIntegracaoDTO ciotEncerramentoIntegracaoDTO = new CIOTEncerramentoIntegracaoDTO();
			ciotEncerramentoIntegracaoDTO.setCnpjContratante(ciotAlteracaoIntegracaoDTO.getCnpjContratante());
			ciotEncerramentoIntegracaoDTO.setIdCIOT(ciotAlteracaoIntegracaoDTO.getIdCIOT());
			ciotEncerramentoIntegracaoDTO.setNrCIOT(ciotAlteracaoIntegracaoDTO.getNrCIOT());
			ciotEncerramentoIntegracaoDTO.setNrCodigoVerificador(ciotAlteracaoIntegracaoDTO.getNrCodigoVerificador());
			ciotEncerramentoIntegracaoDTO.setQtdCarga(ciotAlteracaoIntegracaoDTO.getQtdCarga());
			jmsMessageSender = integracaoJmsService.createMessage(Queues.CIOT_ENCERRAMENTO_ENVIO, ciotEncerramentoIntegracaoDTO);
			
		} else if(CANCELAMENTO_REJEITADO.equals(ciot.getTpSituacao().getValue())){
			CIOTCancelamentoIntegracaoDTO ciotCancelamentoIntegracaoDTO = findDadosParaCancelamentoCiot(ciotBuscaIntegracaoDTO);
			jmsMessageSender = integracaoJmsService.createMessage(Queues.CIOT_CANCELAMENTO_ENVIO, ciotCancelamentoIntegracaoDTO);
		}
		
		if(jmsMessageSender != null){
			integracaoJmsService.storeMessage(jmsMessageSender);
		}
		
		getCIOTDAO().updateTpSituacao(ciot.getIdCIOT(), REENVIADO);
		ciot.setTpSituacao(new DomainValue(REENVIADO));
		return ciot;
	}
	
	public CIOTCancelamentoIntegracaoDTO findDadosParaCancelamentoCiot(CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO) {
		ControleCarga controleCarga = controleCargaService.findDadosParaAlteracaoCiot(ciotBuscaIntegracaoDTO.getIdControleCarga());
		CIOT ciot = null;
		CIOTControleCarga ciotControleCarga = ciotControleCargaService.findByIdControleCarga(controleCarga.getIdControleCarga());
	    if(ciotControleCarga != null){
	    	ciot = ciotControleCarga.getCiot();
	    }
		
		CIOTCancelamentoIntegracaoDTO ciotCancelamentoIntegracaoDTO = new CIOTCancelamentoIntegracaoDTO();
		ciotCancelamentoIntegracaoDTO.setCnpjContratante(controleCarga.getFilialByIdFilialOrigem().getEmpresa().getPessoa().getNrIdentificacao());
		ciotCancelamentoIntegracaoDTO.setIdCIOT(ciot.getIdCIOT());
		ciotCancelamentoIntegracaoDTO.setNrCIOT(FormatUtils.fillNumberWithZero(ciot.getNrCIOT().toString(), 12));
		ciotCancelamentoIntegracaoDTO.setNrCodigoVerificador(ciot.getNrCodigoVerificador());
		
		return ciotCancelamentoIntegracaoDTO;
	}
	
	public void executeBuscaDadosAlteracaoPUD(CIOTBuscaIntegracaoPUDDTO ciotBuscaIntegracaoPUDDTO) {
		for (Long idReciboFreteCarreteiro : ciotBuscaIntegracaoPUDDTO.getIdsReciboParaCIOT()) {
			List<Map<String, Object>> dadosPUDCIOT =  reciboFreteCarreteiroService.findDadosReciboPUDParaCIOT(idReciboFreteCarreteiro);
			
			if(!dadosPUDCIOT.isEmpty()){
				List<Serializable> ciotsAlteracao = generateCIOTAlteracaoPUD(dadosPUDCIOT); 
				JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.CIOT_ALTERACAO_ENVIO);
				jmsMessageSender.addAllMsg(ciotsAlteracao);
				integracaoJmsService.storeMessage(jmsMessageSender);
			}
		}
	}
	
	private List<Serializable> generateCIOTAlteracaoPUD(List<Map<String, Object>> dadosPUDCIOT) {
		List<Serializable> ciotsAlteracao = new ArrayList<Serializable>();
		Map<Long, List<Map<String, Object>>> dadosAgrupadosPorCIOT = agruparDadosPorCIOT(dadosPUDCIOT);
		BigDecimal qtdCiotBigDecimal = new BigDecimal(dadosAgrupadosPorCIOT.size());
		
		for (Map.Entry<Long,  List<Map<String, Object>>> entry : dadosAgrupadosPorCIOT.entrySet()) {
			List<Map<String, Object>> dadosPorCiot =  entry.getValue();
			Map<String, Object> mapDadosComuns = dadosPorCiot.get(0);
			
			CIOTAlteracaoIntegracaoDTO ciotAlteracaoIntegracaoDTO = new CIOTAlteracaoIntegracaoDTO();
			ciotAlteracaoIntegracaoDTO.setIdCIOT(entry.getKey());
			ciotAlteracaoIntegracaoDTO.setNrCIOT(FormatUtils.fillNumberWithZero(mapDadosComuns.get("nrCiot").toString(), 12));
			ciotAlteracaoIntegracaoDTO.setNrCodigoVerificador((String) mapDadosComuns.get("nrCodVerificador"));
			ciotAlteracaoIntegracaoDTO.setCnpjContratante((String) mapDadosComuns.get("nrIdentificacao"));
			this.populaContaBancaria(ciotAlteracaoIntegracaoDTO, (Long) mapDadosComuns.get("idProprietario"));
			totalizarDadosCIOTPUD(dadosPorCiot, qtdCiotBigDecimal, (BigDecimal) mapDadosComuns.get("vlLiquido"), (BigDecimal) mapDadosComuns.get("vlIrrf"),
					(BigDecimal) mapDadosComuns.get("vlInss"), ciotAlteracaoIntegracaoDTO);
			
			ciotsAlteracao.add(ciotAlteracaoIntegracaoDTO);
		}
		
		return ciotsAlteracao;
	}

	private void totalizarDadosCIOTPUD(List<Map<String, Object>> dadosPorCiot, BigDecimal qtdCiotBigDecimal, BigDecimal vlFrete, BigDecimal vlIRRF,
			BigDecimal vlINSS, CIOTAlteracaoIntegracaoDTO ciotAlteracaoIntegracaoDTO) {
		vlFrete = calcularValorPorCiot(qtdCiotBigDecimal, vlFrete);
		vlIRRF = calcularValorPorCiot(qtdCiotBigDecimal, vlIRRF);
		vlINSS = calcularValorPorCiot(qtdCiotBigDecimal, vlINSS);
		
		ciotAlteracaoIntegracaoDTO.setVlFrete(vlFrete);
		ciotAlteracaoIntegracaoDTO.setVlIRRF(vlIRRF);
		ciotAlteracaoIntegracaoDTO.setVlINSS(vlINSS);
		
		BigDecimal qtdCarga = BigDecimal.ZERO;
		for (Map<String, Object> map : dadosPorCiot) {
			qtdCarga = BigDecimalUtils.add(qtdCarga, (BigDecimal) map.get("psTotalFrota"));
		}
		ciotAlteracaoIntegracaoDTO.setQtdCarga(qtdCarga);
	}
	
	private BigDecimal calcularValorPorCiot(BigDecimal qtdCiotBigDecimal, BigDecimal valor) {
		if (valor != null) {
			if (valor.compareTo(BigDecimal.ZERO) == 0) {
				return valor;
			} else {
				return valor.divide(qtdCiotBigDecimal, RoundingMode.DOWN);
			}
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	private Map<Long, List<Map<String, Object>>> agruparDadosPorCIOT(List<Map<String, Object>> dadosPUDCIOT){
		Map<Long, List<Map<String, Object>>> dadosAgrupadosPorCIOT = new HashMap<Long, List<Map<String, Object>>>();
		
		for (Map<String, Object> map : dadosPUDCIOT) {
			Long idCiot = (Long) map.get("idCiot");
			
			if(dadosAgrupadosPorCIOT.containsKey(idCiot)){
				dadosAgrupadosPorCIOT.get(idCiot).add(map);
				
			} else {
				List<Map<String, Object>> listaPorCiot = new ArrayList<Map<String, Object>>();
				listaPorCiot.add(map);
				dadosAgrupadosPorCIOT.put(idCiot, listaPorCiot);
			}
		}
		
		return dadosAgrupadosPorCIOT;
	}
	
	public CIOTAlteracaoIntegracaoDTO findDadosParaAlteracaoCiot(CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO) {
		ControleCarga controleCarga = controleCargaService.findDadosParaAlteracaoCiot(ciotBuscaIntegracaoDTO.getIdControleCarga());
		CIOT ciot = null;
		CIOTControleCarga ciotControleCarga = ciotControleCargaService.findByIdControleCargaIdMeioTransporte(controleCarga.getIdControleCarga(), controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte());
	    if(ciotControleCarga != null){
	    	ciot = ciotControleCarga.getCiot();
	    }
		
		Boolean alteraCIOT = this.validaAlteraCiot(controleCarga, ciot, ciotBuscaIntegracaoDTO.getIdFilialLogada());
		CIOTAlteracaoIntegracaoDTO ciotAlteracaoIntegracaoDTO = new CIOTAlteracaoIntegracaoDTO();
		ciotAlteracaoIntegracaoDTO.setAlteraCIOT(alteraCIOT);
		ciotAlteracaoIntegracaoDTO.setQtdCarga(controleCarga.getPsTotalFrota());
		
		if(alteraCIOT){
			this.populaContaBancaria(ciotAlteracaoIntegracaoDTO,  controleCarga.getProprietario().getIdProprietario());
			ciotAlteracaoIntegracaoDTO.setCnpjContratante(controleCarga.getFilialByIdFilialOrigem().getEmpresa().getPessoa().getNrIdentificacao());
			ciotAlteracaoIntegracaoDTO.setIdCIOT(ciot.getIdCIOT());
			ciotAlteracaoIntegracaoDTO.setNrCIOT(FormatUtils.fillNumberWithZero(ciot.getNrCIOT().toString(), 12));
			ciotAlteracaoIntegracaoDTO.setNrCodigoVerificador(ciot.getNrCodigoVerificador());
			ciotAlteracaoIntegracaoDTO.setQtdCarga(controleCarga.getPsTotalFrota());
			this.populaValores(ciotAlteracaoIntegracaoDTO, controleCarga);
		}
		
		return ciotAlteracaoIntegracaoDTO;
	}
	
	private Boolean validaAlteraCiot(ControleCarga controleCarga, CIOT ciot, Long idFilialLogada) {
		if(idFilialLogada == null){
			return true;
		} else if(ciot == null || 
				!controleCarga.getTpControleCarga().getValue().equals(VIAGEM) ||
					!idFilialLogada.equals(controleCarga.getFilialByIdFilialDestino().getIdFilial())){
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void populaContaBancaria(CIOTAlteracaoIntegracaoDTO ciotAlteracaoIntegracaoDTO, Long idProprietario) {
		List<ContaBancaria> listContasBancarias = contaBancariaService.findContasBancariasVigentesByPessoa(idProprietario);
		
		if(!listContasBancarias.isEmpty()){
			ContaBancaria contaBancaria = listContasBancarias.get(0);
			
			StringBuilder str = new StringBuilder();
			str.append(NOME_BANCO);
			str.append(contaBancaria.getAgenciaBancaria().getBanco().getNmBanco());
			str.append(", ");
			
			str.append(NUMERO_BANCO);
			str.append(contaBancaria.getAgenciaBancaria().getBanco().getNrBanco());
			str.append(", ");
			
			str.append(AGENCIA);
			str.append(contaBancaria.getAgenciaBancaria().getNrAgenciaBancaria());
			str.append(", ");
			
			str.append(CONTA);
			str.append(contaBancaria.getNrContaBancaria());
			str.append("-");
			str.append(contaBancaria.getDvContaBancaria());
			
			ciotAlteracaoIntegracaoDTO.setAdicionaisValor(str.toString());
			ciotAlteracaoIntegracaoDTO.setAdicionaisNome(ADICIONAIS_NOME);
		}
	}

	private void populaValores(CIOTAlteracaoIntegracaoDTO ciotAlteracaoIntegracaoDTO, ControleCarga controleCarga) {
		BigDecimal vlFrete = BigDecimal.ZERO;
		BigDecimal vlINSS = BigDecimal.ZERO;
		BigDecimal vlIRRF = BigDecimal.ZERO;
		
		List<ReciboFreteCarreteiro> listReciboFC = reciboFreteCarreteiroService.findReciboFreteCarreteiroByIdControleCarga(controleCarga.getIdControleCarga());
		
		for (ReciboFreteCarreteiro reciboFreteCarreteiro : listReciboFC) {
			
			if(reciboFreteCarreteiro.getVlLiquido() != null){
				vlFrete = vlFrete.add(reciboFreteCarreteiro.getVlLiquido());
			}
			
			if(reciboFreteCarreteiro.getVlInss() != null){
				vlINSS = vlINSS.add(reciboFreteCarreteiro.getVlInss());
			}
			
			if(reciboFreteCarreteiro.getVlIrrf() != null){
				vlIRRF = vlIRRF.add(reciboFreteCarreteiro.getVlIrrf());
			}
		}
		
		ciotAlteracaoIntegracaoDTO.setVlFrete(vlFrete);
		ciotAlteracaoIntegracaoDTO.setVlINSS(vlINSS);
		ciotAlteracaoIntegracaoDTO.setVlIRRF(vlIRRF);
	}
	
	public CIOTSolicitacaoIntegracaoDTO executeBuscaDados(CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO) {
		
		CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO = this.findDadosParaCiot(ciotBuscaIntegracaoDTO, false);
		 
		if(Boolean.FALSE.equals(ciotSolicitacaoIntegracaoDTO.getExigeCIOT())){
			controleCargaService.updateExigenciaCIOTEnviadoIntegCIOT(ciotSolicitacaoIntegracaoDTO.getIdControleCarga(), ciotSolicitacaoIntegracaoDTO.getExigeCIOT(), null);
		}
		
		if(Boolean.TRUE.equals(ciotSolicitacaoIntegracaoDTO.getVerificaExigenciaCIOT())){
			controleCargaService.updateExigenciaCIOTEnviadoIntegCIOT(ciotSolicitacaoIntegracaoDTO.getIdControleCarga(), null, ciotSolicitacaoIntegracaoDTO.getVerificaExigenciaCIOT());
		}
		
		return ciotSolicitacaoIntegracaoDTO;
	}
    
    private CIOTSolicitacaoIntegracaoDTO findDadosParaCiot(CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO, boolean dadosParaReenvio) {
        ControleCarga controleCarga = controleCargaService.findDadosParaCiot(ciotBuscaIntegracaoDTO.getIdControleCarga());
        InscricaoEstadual inscricaoEstadual = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(controleCarga.getProprietario().getIdProprietario());

        CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO = new CIOTSolicitacaoIntegracaoDTO();
        ciotSolicitacaoIntegracaoDTO.setIdControleCarga(controleCarga.getIdControleCarga());

        if (dadosParaReenvio) {
            ciotSolicitacaoIntegracaoDTO.setVerificaExigenciaCIOT(true);
            this.populaDados(controleCarga, inscricaoEstadual, ciotSolicitacaoIntegracaoDTO, dadosParaReenvio);

            return ciotSolicitacaoIntegracaoDTO;
        }

        if (controleCarga.getMeioTransporteByIdTransportado() == null) {
            ciotSolicitacaoIntegracaoDTO.setVerificaExigenciaCIOT(false);
        } else if (PROPRIO.equals(controleCarga.getMeioTransporteByIdTransportado().getTpVinculo().getValue())) {
            ciotSolicitacaoIntegracaoDTO.setExigeCIOT(false);
            ciotSolicitacaoIntegracaoDTO.setVerificaExigenciaCIOT(false);
        } else {
            boolean verificaExigeCiot = verificaExigeCiot(controleCarga, ciotBuscaIntegracaoDTO.getIdFilialLogada());
            ciotSolicitacaoIntegracaoDTO.setVerificaExigenciaCIOT(verificaExigeCiot);
            if (verificaExigeCiot) {
                this.populaDados(controleCarga, inscricaoEstadual, ciotSolicitacaoIntegracaoDTO, dadosParaReenvio);
            }
        }

        return ciotSolicitacaoIntegracaoDTO;
    }

    private void populaDados(ControleCarga controleCarga, InscricaoEstadual inscricaoEstadual, CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO, boolean dadosParaReenvio) {
        if (dadosParaReenvio || VIAGEM.equals(controleCarga.getTpControleCarga().getValue())) {
            populaDadosCiotSolicitacaoIntegracao(controleCarga, ciotSolicitacaoIntegracaoDTO, inscricaoEstadual);
            return;
        }

        BigDecimal param = (BigDecimal) configuracoesFacade.getValorParametro("DIA_SEMANA_EMISSAO_RECIBO");
        YearMonthDay diaSemana = JTDateTimeUtils.getLastDay(new YearMonthDay(), param.intValue());
        CIOT ciotGerado = this.findGeradoAPartirDe(controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(), diaSemana);

        if (ciotGerado == null) {
            populaDadosCiotSolicitacaoIntegracao(controleCarga, ciotSolicitacaoIntegracaoDTO, inscricaoEstadual);
            return;
        }
        
        ciotSolicitacaoIntegracaoDTO.setVerificaExigenciaCIOT(false);
        controleCargaService.updateExigenciaCIOTEnviadoIntegCIOT(controleCarga.getIdControleCarga(), Boolean.TRUE, Boolean.TRUE);

        CIOTControleCarga ciotControleCarga = new CIOTControleCarga();
        ciotControleCarga.setCiot(ciotGerado);
        ciotControleCarga.setControleCarga(controleCarga);
        ciotControleCargaService.store(ciotControleCarga);
    }
	
	public CIOT findGeradoAPartirDe(Long idMeioTransporte, YearMonthDay data){
		CIOT ciot = null;
		CIOTControleCarga ciotControleCarga = ciotControleCargaService.findGeradoAPartirDe(idMeioTransporte, data);
	    if(ciotControleCarga != null){
	    	ciot = ciotControleCarga.getCiot();
	    }
		return ciot;
	}

	private void populaDadosCiotSolicitacaoIntegracao(ControleCarga controleCarga, CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO, InscricaoEstadual inscricaoEstadual) {
		ciotSolicitacaoIntegracaoDTO.setIdMeioTransporte(controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte());
		
		ciotSolicitacaoIntegracaoDTO.setCnpjContratante(controleCarga.getFilialByIdFilialOrigem().getEmpresa().getPessoa().getNrIdentificacao());
		
		ciotSolicitacaoIntegracaoDTO.setQuantidade(controleCarga.getMeioTransporteByIdTransportado().getNrCapacidadeKg());
		ciotSolicitacaoIntegracaoDTO.setCnpjRemetente(controleCarga.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
		ciotSolicitacaoIntegracaoDTO.setNomeRemetente(controleCarga.getFilialByIdFilialOrigem().getPessoa().getNmPessoa());
		this.populaEnderecoRemetente(ciotSolicitacaoIntegracaoDTO, controleCarga);
		
		ciotSolicitacaoIntegracaoDTO.setCnpjDestinatario(controleCarga.getFilialByIdFilialDestino().getPessoa().getNrIdentificacao());
		ciotSolicitacaoIntegracaoDTO.setNomeDestinatario(controleCarga.getFilialByIdFilialDestino().getPessoa().getNmPessoa());
		this.populaEnderecoDestinatario(ciotSolicitacaoIntegracaoDTO, controleCarga);
		
		ciotSolicitacaoIntegracaoDTO.setRNTRC(controleCarga.getProprietario().getNrAntt().toString());
		ciotSolicitacaoIntegracaoDTO.setBlProprietarioPessoaFisica(FISICA.equals(controleCarga.getProprietario().getPessoa().getTpPessoa().getValue()) ? true : false);
		ciotSolicitacaoIntegracaoDTO.setCpfTransportador(ciotSolicitacaoIntegracaoDTO.getBlProprietarioPessoaFisica() ? controleCarga.getProprietario().getPessoa().getNrIdentificacao() : null);
		ciotSolicitacaoIntegracaoDTO.setCnpjTransportador(!ciotSolicitacaoIntegracaoDTO.getBlProprietarioPessoaFisica() ? controleCarga.getProprietario().getPessoa().getNrIdentificacao() : null);
		ciotSolicitacaoIntegracaoDTO.setIdentidade(ciotSolicitacaoIntegracaoDTO.getBlProprietarioPessoaFisica() ? controleCarga.getProprietario().getPessoa().getNrRg() : null);
		ciotSolicitacaoIntegracaoDTO.setInscEstadual((!ciotSolicitacaoIntegracaoDTO.getBlProprietarioPessoaFisica() && inscricaoEstadual != null) ? inscricaoEstadual.getNrInscricaoEstadual() : null);
		ciotSolicitacaoIntegracaoDTO.setNomeTransportador(controleCarga.getProprietario().getPessoa().getNmPessoa());
		this.populaEnderecoTransportador(ciotSolicitacaoIntegracaoDTO, controleCarga);
		
		this.populaRota(ciotSolicitacaoIntegracaoDTO, controleCarga);
		
		ciotSolicitacaoIntegracaoDTO.setPlacaVeiculo(controleCarga.getMeioTransporteByIdTransportado().getNrIdentificador());
		ciotSolicitacaoIntegracaoDTO.setModeloVeiculo(controleCarga.getMeioTransporteByIdTransportado().getModeloMeioTransporte().getDsModeloMeioTransporte());
		ciotSolicitacaoIntegracaoDTO.setVlrFrete(controleCarga.getVlFreteCarreteiro() != null ? controleCarga.getVlFreteCarreteiro() : BigDecimal.ONE);
		ciotSolicitacaoIntegracaoDTO.setIrrf(BigDecimal.ZERO);
		ciotSolicitacaoIntegracaoDTO.setInss(BigDecimal.ZERO);
		ciotSolicitacaoIntegracaoDTO.setSestSenat(BigDecimal.ZERO);
		ciotSolicitacaoIntegracaoDTO.setTipoRateio(BigDecimal.ZERO);
		
		ciotSolicitacaoIntegracaoDTO.setBlTpControleCargaViagem(VIAGEM.equals(controleCarga.getTpControleCarga().getValue()));
	}
	
	private void populaRota(CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO, ControleCarga controleCarga) {
		Filial filialOrigem = controleCarga.getFilialByIdFilialOrigem();
		Filial filialDestino = null;
		if(COLETA_ENTREGA.equals(controleCarga.getTpControleCarga().getValue())){
			filialDestino = filialOrigem;
		}else{
			filialDestino = controleCarga.getFilialByIdFilialDestino();
		}
		
		StringBuilder rotaERP = new StringBuilder();
		rotaERP.append(filialOrigem.getSgFilial());
		rotaERP.append(" - ");
		rotaERP.append(filialDestino.getSgFilial());
		
		ciotSolicitacaoIntegracaoDTO.setRotaERP(rotaERP.toString());
		ciotSolicitacaoIntegracaoDTO.setNomeRota(rotaERP.toString());
		
		StringBuilder codigoIBGE1 = new StringBuilder();
		codigoIBGE1.append(filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge());
		codigoIBGE1.append(FormatUtils.fillNumberWithZero(filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge().toString(), 5));
		ciotSolicitacaoIntegracaoDTO.setCodigoIBGE1(codigoIBGE1.toString());
		
		StringBuilder codigoIBGE2 = new StringBuilder();
		codigoIBGE2.append(filialDestino.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge());
		codigoIBGE2.append(FormatUtils.fillNumberWithZero(filialDestino.getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge().toString(), 5));
		ciotSolicitacaoIntegracaoDTO.setCodigoIBGE2(codigoIBGE2.toString());
	}
	
	private void populaEnderecoTransportador(CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO, ControleCarga controleCarga) {
		ciotSolicitacaoIntegracaoDTO.setUfTransportador(controleCarga.getProprietario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		StringBuilder codigoMunicipioTransportador = new StringBuilder();
		codigoMunicipioTransportador.append(controleCarga.getProprietario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge());
		codigoMunicipioTransportador.append(FormatUtils.fillNumberWithZero(controleCarga.getProprietario().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge().toString(), 5));
		ciotSolicitacaoIntegracaoDTO.setCodigoMunicipioTransportador(codigoMunicipioTransportador.toString());
		String dsBairroTransportador = controleCarga.getProprietario().getPessoa().getEnderecoPessoa().getDsBairro();
		ciotSolicitacaoIntegracaoDTO.setBairroTransportador(dsBairroTransportador != null ? dsBairroTransportador.trim() : parametroGeralService.findSimpleConteudoByNomeParametro(BAIRRO_PADRAO));
		StringBuilder logradouroTransportador = new StringBuilder();
		logradouroTransportador.append(controleCarga.getProprietario().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro().toString());
		logradouroTransportador.append(" ");
		logradouroTransportador.append(controleCarga.getProprietario().getPessoa().getEnderecoPessoa().getDsEndereco());
		ciotSolicitacaoIntegracaoDTO.setLogradouroTransportador(logradouroTransportador.toString());
		ciotSolicitacaoIntegracaoDTO.setNumeroTransportador(controleCarga.getProprietario().getPessoa().getEnderecoPessoa().getNrEndereco());
	}

	private void populaEnderecoDestinatario(CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO, ControleCarga controleCarga) {
		Filial filial = null;
		if(COLETA_ENTREGA.equals(controleCarga.getTpControleCarga().getValue())){
			filial = controleCarga.getFilialByIdFilialOrigem();
		}else{
			filial = controleCarga.getFilialByIdFilialDestino();
		}
		
		ciotSolicitacaoIntegracaoDTO.setUfDestinatario(filial.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		StringBuilder codigoMunicipioDestinatario = new StringBuilder();
		codigoMunicipioDestinatario.append(filial.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge());
		codigoMunicipioDestinatario.append(FormatUtils.fillNumberWithZero(filial.getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge().toString(), 5));
		ciotSolicitacaoIntegracaoDTO.setCodigoMunicipioDestinatario(codigoMunicipioDestinatario.toString());
		
		String dsBairro = filial.getPessoa().getEnderecoPessoa().getDsBairro();
		ciotSolicitacaoIntegracaoDTO.setBairroDestinatario(dsBairro != null ? dsBairro.trim() : parametroGeralService.findSimpleConteudoByNomeParametro(BAIRRO_PADRAO));
		
		StringBuilder logradouroDestinatario = new StringBuilder();
		logradouroDestinatario.append(filial.getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro().toString());
		logradouroDestinatario.append(" ");
		logradouroDestinatario.append(filial.getPessoa().getEnderecoPessoa().getDsEndereco());
		ciotSolicitacaoIntegracaoDTO.setLogradouroDestinatario(logradouroDestinatario.toString());
		
		ciotSolicitacaoIntegracaoDTO.setNumeroDestinatario(filial.getPessoa().getEnderecoPessoa().getNrEndereco());
		ciotSolicitacaoIntegracaoDTO.setCepDestinatario(filial.getPessoa().getEnderecoPessoa().getNrCep());
		ciotSolicitacaoIntegracaoDTO.setComplementoDestinatario(filial.getPessoa().getEnderecoPessoa().getDsComplemento());
	}
	
	private void populaEnderecoRemetente(CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO, ControleCarga controleCarga) {
		Filial filial = controleCarga.getFilialByIdFilialOrigem();
		
		ciotSolicitacaoIntegracaoDTO.setUfRemetente(filial.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		StringBuilder codigoMunicipioRemetente = new StringBuilder();
		codigoMunicipioRemetente.append(filial.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge());
		codigoMunicipioRemetente.append(FormatUtils.fillNumberWithZero(filial.getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge().toString(), 5));
		ciotSolicitacaoIntegracaoDTO.setCodigoMunicipioRemetente(codigoMunicipioRemetente.toString());
		
		String dsBairro = filial.getPessoa().getEnderecoPessoa().getDsBairro();
		ciotSolicitacaoIntegracaoDTO.setBairroRemetente(dsBairro != null ? dsBairro.trim() : parametroGeralService.findSimpleConteudoByNomeParametro(BAIRRO_PADRAO));
		
		StringBuilder logradouroRemetente = new StringBuilder();
		logradouroRemetente.append(filial.getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro().toString());
		logradouroRemetente.append(" ");
		logradouroRemetente.append(filial.getPessoa().getEnderecoPessoa().getDsEndereco());
		ciotSolicitacaoIntegracaoDTO.setLogradouroRemetente(logradouroRemetente.toString());
		
		ciotSolicitacaoIntegracaoDTO.setNumeroRemetente(filial.getPessoa().getEnderecoPessoa().getNrEndereco());
		ciotSolicitacaoIntegracaoDTO.setCepRemetente(filial.getPessoa().getEnderecoPessoa().getNrCep());
		ciotSolicitacaoIntegracaoDTO.setComplementoRemetente(filial.getPessoa().getEnderecoPessoa().getDsComplemento());
	}
	
	private boolean verificaExigeCiot(ControleCarga controleCarga, Long idFilialLogada) {
		boolean verificaExigeCiot = false;
		
		if(idFilialLogada == null){
			verificaExigeCiot = true;
			
		} else {
			ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(idFilialLogada, INDICADOR_CIOT, false, true);
			if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())
					&& !PROPRIO.equals(controleCarga.getMeioTransporteByIdTransportado().getTpVinculo().getValue())
						&& (null == controleCarga.getBlEnviadoIntegCIOT() || !controleCarga.getBlEnviadoIntegCIOT())) {
				
				boolean isParceira = empresaService.isEmpresaParceira(controleCarga.getProprietario().getIdProprietario());
				
				if ((controleCarga.getTpControleCarga().getValue().equals(VIAGEM) && idFilialLogada.equals(controleCarga.getFilialByIdFilialOrigem().getIdFilial()))
						|| (controleCarga.getTpControleCarga().getValue().equals(COLETA_ENTREGA) && !isParceira)) {
					verificaExigeCiot = true;
				} 
			}
		} 
		
		return verificaExigeCiot;
	}
	
	/**
	 * Método responsável por criar ou alterar registro na tabela CIOT.
	 *  
	 * Este método é chamado em momentos diversos pelo serviço de integração, em decorrência
	 * disto nem todos os atributos estão preenchidos a cada chamada.
	 * 
	 * Os atributos que estiverem preenchidos devem ser refletidos na base de
	 * dados, e os atributos que estiverem nulos não devem modificar os valores
	 * que já estão persistidos.
	 * 
	 * @param ciotIntegracaoDTO
	 * @return
	 */
	public CIOTIntegracaoDTO storeFromIntegracao(CIOTIntegracaoDTO ciotIntegracaoDTO) {
		Boolean criarRelacionamento = false;
		CIOT ciot = null;

		if (ciotIntegracaoDTO.getIdCiot() != null) {
			ciot = this.findById(ciotIntegracaoDTO.getIdCiot());
		} else {
			criarRelacionamento = true;
			ciot = new CIOT();
		}

		if (ciotIntegracaoDTO.getIdMeioTransporte() != null) {
			MeioTransporte meioTransporte = new MeioTransporte();
			meioTransporte.setIdMeioTransporte(ciotIntegracaoDTO.getIdMeioTransporte());
			ciot.setMeioTransporte(meioTransporte);
		}

		if (ciotIntegracaoDTO.getNrCIOT() != null) {
			ciot.setNrCIOT(ciotIntegracaoDTO.getNrCIOT());
		}

		if (ciotIntegracaoDTO.getNrCodigoVerificador() != null) {
			ciot.setNrCodigoVerificador(ciotIntegracaoDTO.getNrCodigoVerificador());
		}

		if (ciotIntegracaoDTO.getVlFrete() != null) {
			ciot.setVlFrete(ciotIntegracaoDTO.getVlFrete());
		}

		if (ciotIntegracaoDTO.getTpSituacao() != null) {
			ciot.setTpSituacao(new DomainValue(ciotIntegracaoDTO.getTpSituacao()));
		}

		if (ciotIntegracaoDTO.getDsObservacao() != null) {
			ciot.setDsObservacao(ciotIntegracaoDTO.getDsObservacao());
		}
		
		if (ciotIntegracaoDTO.getProtocoloEncerramento() != null) {
			ciot.setNrProtocoloEncerramento(ciotIntegracaoDTO.getProtocoloEncerramento());
		}
		
		if (ciotIntegracaoDTO.getProtocoloCancelamento() != null) {
			ciot.setNrProtocoloCancelamento(ciotIntegracaoDTO.getProtocoloCancelamento());
		}

		if (ciotIntegracaoDTO.getDhGeracao() != null) {
			ciot.setDhGeracao(ciotIntegracaoDTO.getDhGeracao());
		}
		
		if (ciotIntegracaoDTO.getDhAlteracao() != null) {
			ciot.setDhAlteracao(ciotIntegracaoDTO.getDhAlteracao());
		}
		
		if (ciotIntegracaoDTO.getDhEncerramento() != null) {
			ciot.setDhEncerramento(ciotIntegracaoDTO.getDhEncerramento());
		}
		
		if (ciotIntegracaoDTO.getDhCancelamento() != null) {
			ciot.setDhCancelamento(ciotIntegracaoDTO.getDhCancelamento());
		}

		super.store(ciot);
		ciotIntegracaoDTO.setIdCiot(ciot.getIdCIOT());
		
		if (criarRelacionamento) {
			CIOTControleCarga ciotControleCarga = new CIOTControleCarga();
			ControleCarga controleCarga = new ControleCarga();
			controleCarga.setIdControleCarga(ciotIntegracaoDTO.getIdControleCarga());
			ciotControleCarga.setControleCarga(controleCarga);
			ciotControleCarga.setCiot(ciot);
			ciotControleCargaService.store(ciotControleCarga);
		}
		
		return ciotIntegracaoDTO;
	}
	
	public void executeSolicitacaoCIOT(Long idControleCarga){
		executeCIOT(idControleCarga, Queues.CIOT_BUSCA_DADOS);
	}

	public void executeAlteracaoCIOT(Long idControleCarga){
		CIOTControleCarga ciotControleCarga = ciotControleCargaService.findGeradoByIdControleCarga(idControleCarga);
		
		if(ciotControleCarga != null){
			executeCIOT(idControleCarga, Queues.CIOT_BUSCA_DADOS_ALTERACAO);
		}
	}
	
	public void executeCancelamentoCIOT(Long idControleCarga){
		boolean executarCancelamentoCiot = false;
		
		CIOTControleCarga ciotControleCarga = ciotControleCargaService.findGeradoByIdControleCarga(idControleCarga);
		if(ciotControleCarga != null){
			executarCancelamentoCiot = true;
		}
		
		ControleCarga cc = controleCargaService.findById(idControleCarga);
		if(!cc.getTpControleCarga().getValue().equals(VIAGEM) && ciotControleCarga != null){
			List<CIOTControleCarga> listCiotControleCarga = ciotControleCargaService.findByCiotDiferenteControleCarga(ciotControleCarga.getCiot().getIdCIOT(), idControleCarga);
			
			if(CollectionUtils.isNotEmpty(listCiotControleCarga)){
				executarCancelamentoCiot = false;
				ciotControleCargaService.removeById(ciotControleCarga.getIdCIOTControleCarga());
			}
		}
		
		if(executarCancelamentoCiot){
			executeCIOT(idControleCarga, Queues.CIOT_BUSCA_DADOS_CANCELAMENTO);
		}
	}
	
	private void executeCIOT(Long idControleCarga, Queues queue) {
		CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO = new CIOTBuscaIntegracaoDTO();
    	ciotBuscaIntegracaoDTO.setIdControleCarga(idControleCarga);
    	ciotBuscaIntegracaoDTO.setIdFilialLogada(SessionUtils.getFilialSessao().getIdFilial());
    	JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(queue, ciotBuscaIntegracaoDTO);
		integracaoJmsService.storeMessage(jmsMessageSender);
	}
	
	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public IntegracaoJmsService getIntegracaoJmsService() {
		return integracaoJmsService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public InscricaoEstadualService getInscricaoEstadualService() {
		return inscricaoEstadualService;
	}

	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public ReciboFreteCarreteiroService getReciboFreteCarreteiroService() {
		return reciboFreteCarreteiroService;
	}

	public void setReciboFreteCarreteiroService(
			ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public ContaBancariaService getContaBancariaService() {
		return contaBancariaService;
	}

	public void setContaBancariaService(ContaBancariaService contaBancariaService) {
		this.contaBancariaService = contaBancariaService;
	}

	public CIOTControleCargaService getCiotControleCargaService() {
		return ciotControleCargaService;
	}

	public void setCiotControleCargaService(
			CIOTControleCargaService ciotControleCargaService) {
		this.ciotControleCargaService = ciotControleCargaService;
	}

}
