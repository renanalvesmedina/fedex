package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.ISODateTimeFormat;

import br.com.tntbrasil.integracao.domains.calendariotnt.CalendarioTntDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.edw.EdwService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ClienteTerritorio;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.dao.ClienteTerritorioDAO;
import com.mercurio.lms.vendas.model.enums.DmStatusEnum;
import com.mercurio.lms.workflow.model.EventoWorkflow;
import com.mercurio.lms.workflow.model.HistoricoWorkflow;
import com.mercurio.lms.workflow.model.Ocorrencia;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.TipoEvento;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;
import com.mercurio.lms.workflow.model.service.OcorrenciaService;
import com.mercurio.lms.workflow.model.service.PendenciaService;
import com.mercurio.lms.workflow.model.service.TipoEventoService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

public class ClienteTerritorioService extends CrudService<ClienteTerritorio, Long> {
	
	private static final String EXCEPTION_CLIENTE_NAO_INFORMADO = "LMS-01292";
	private static final String EXCEPTION_TERRITORIO_NAO_INFORMADO = "LMS-01293";
	private static final String EXCEPTION_INICIO_VIGENCIA_NAO_INFORMADA = "LMS-01294";
	private static final String EXCEPTION_PERIODO_VIGENCIA_INVALIDO = "LMS-01295";
	private static final String EXCEPTION_CONFLITO_MODAIS = "LMS-01296";
	private static final String EXCEPTION_CONFLITO_TERRITORIO = "LMS-01297";
	private static final String EXCEPTION_TRANSF_COM_REGISTRO_PENDENTE = "LMS-01320";
	private static final String EXCEPTION_SOBREPOSICAO_DE_INTERVALOS = "LMS-01297";
	private static final String EXCEPTION_NAO_PODE_SER_ALTERADO_EM_APROVACAO = "LMS-01323";
	private static final String EXCEPTION_CARENCIA_TROCA_TERRITORIO = "LMS-01324";

	private static final String MENSAGEM_INCLUSAO = "LMS-01325";
	private static final String MENSAGEM_TROCA_DE_FILIAIS = "LMS-01326";
	private static final String MENSAGEM_TROCA_DE_TERRITORIO_MESMA_FILIAL = "LMS-01327";
	private static final String MENSAGEM_TROCA_DE_MODAL = "LMS-01328";
	private static final String MENSAGEM_TROCA_DE_VIGENCIA = "LMS-01329";

	private static final String NR_DIAS_TRANSFERENCIA = "NR_DIAS_TRANSFERENCIA";
	
	private static final String TP_SITUACAO_EM_APROVACAO = "E";
	private static final String TP_SITUACAO_ATIVO = "A";
	
	private static final Short EVENTO_INCLUSAO_PRIMEIRO_PASSO = 157; // Aprovação parcial de inclusão de clientes entre filiais 
	private static final Short EVENTO_INCLUSAO_SEGUNDO_PASSO = 158; // Aprovação de inclusão de clientes entre filiais 
	
	private static final Short EVENTO_TROCA_FILIAIS_PRIMEIRO_PASSO = 156;
	private static final Short EVENTO_TROCA_FILIAIS_SEGUNDO_PASSO = 154;
	
	private static final Short EVENTO_TROCA_DADOS_MESMA_FILIAL = 153;
	
	private ExecutivoTerritorioService executivoTerritorioService;
	private TerritorioService territorioService;
	private UsuarioLMSService usuarioLMSService;
	private ClienteService clienteService;
	private PendenciaService pendenciaService;
	private TipoEventoService tipoEventoService;
	private OcorrenciaService ocorrenciaService;
	private WorkflowPendenciaService workflowPendenciaService;	
	private HistoricoWorkflowService historicoWorkflowService;
	private ParametroGeralService parametroGeralService;
	private EdwService edwService;
	private ConfiguracoesFacade configuracoesFacade;

	private boolean isMesmoTerritorio(ClienteTerritorio clienteTerritorio1, ClienteTerritorio clienteTerritorio2) {
		Territorio territorio1 = clienteTerritorio1.getTerritorio();
		Territorio territorio2 = clienteTerritorio2.getTerritorio();
		
		if (territorio1 != null && territorio2 != null) {
			return territorio1.equals(territorio2);
		}
		
		return false;
	}

	private boolean isIntersecaoEntreModais(ClienteTerritorio clienteTerritorio1, ClienteTerritorio clienteTerritorio2) {
		if (clienteTerritorio1.getTpModal() == null
				|| clienteTerritorio2.getTpModal() == null) {
			return true;
		}
		
		return clienteTerritorio1.getTpModal().equals(clienteTerritorio2.getTpModal());
	}

	private boolean isConflitoEntreVigencias(ClienteTerritorio clienteTerritorio1, ClienteTerritorio clienteTerritorio2) {
		ClienteTerritorio vigenciaMaisAntiga = null;
		ClienteTerritorio vigenciaMaisNova = null;
		
		if (clienteTerritorio1.getDtInicio().isBefore(clienteTerritorio2.getDtInicio())) {
			vigenciaMaisAntiga = clienteTerritorio1;
			vigenciaMaisNova = clienteTerritorio2;
		} else {
			vigenciaMaisAntiga = clienteTerritorio2;
			vigenciaMaisNova = clienteTerritorio1;
		}
		
		return vigenciaMaisAntiga.getDtFim() == null
				|| vigenciaMaisAntiga.getDtFim().isAfter(vigenciaMaisNova.getDtInicio())
				|| vigenciaMaisAntiga.getDtFim().isEqual(vigenciaMaisNova.getDtInicio());
	}

	private boolean isMatriz(ClienteTerritorio clienteTerritorio) {
		Territorio territorio = clienteTerritorio.getTerritorio();
		return territorio.getFilial() != null && "MTZ".equals(territorio.getFilial().getSgFilial());
	}

	private boolean existsExecutivoSAM(ClienteTerritorio clienteTerritorio) {
		return executivoTerritorioService.existsExecutivoSAM(clienteTerritorio.getTerritorio().getIdTerritorio());
	}

	private boolean isConflitoPermitido(ClienteTerritorio clienteTerritorio1, ClienteTerritorio clienteTerritorio2) {
		return (isMatriz(clienteTerritorio1) && existsExecutivoSAM(clienteTerritorio1))
				|| (isMatriz(clienteTerritorio2) && existsExecutivoSAM(clienteTerritorio2));
	}

	private boolean isIdTerritoriosIguais(Long idTerritorio1, Long idTerritorio2) {
		if (idTerritorio1 != null) {
			return (idTerritorio1.equals(idTerritorio2));
		}
		return (idTerritorio2 == null);
	}

	private boolean isModaisIguais(DomainValue modal1, DomainValue modal2) {
		if (modal1 != null) {
			return (modal1.equals(modal2));
		}
		return (modal2 == null);
	}

	private boolean isDatasIguais(YearMonthDay data1, YearMonthDay data2) {
		if (data1 != null) {
			return (data1.equals(data2));
		}
		return (data2 == null);
	}
		
	private boolean isCamposIdenticos(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		if (clienteTerritorioAtual != null) {
			if (clienteTerritorioNovo == null) {
				return false;
			}
			
			if (!clienteTerritorioAtual.getCliente().getIdCliente().equals(clienteTerritorioNovo.getCliente().getIdCliente())) {
				return false;
			}
			
			if (!isIdTerritoriosIguais(clienteTerritorioAtual.getTerritorio().getIdTerritorio(), clienteTerritorioNovo.getTerritorio().getIdTerritorio())) {
				return false;
			}
			
			if (!isModaisIguais(clienteTerritorioAtual.getTpModal(), clienteTerritorioNovo.getTpModal())) {
				return false;
			}

			if (!isDatasIguais(clienteTerritorioAtual.getDtInicio(), clienteTerritorioNovo.getDtInicio())) {
				return false;
			}

			if (!isDatasIguais(clienteTerritorioAtual.getDtFim(), clienteTerritorioNovo.getDtFim())) {
				return false;
			}

			return true;
		}
		return false;
	}
	
	private boolean isExisteIntersecaoDeVigencias(YearMonthDay vigencia1Inicio, YearMonthDay vigencia1Fim, YearMonthDay vigencia2Inicio, YearMonthDay vigencia2Fim) {
		if ((vigencia1Inicio == null) || (vigencia2Inicio == null)) {
			return false;
		}
		if (vigencia1Fim == null) {
			if (vigencia2Fim == null) {
				return false;
			} else {
				return ((vigencia2Inicio.isBefore(vigencia1Inicio)) && (vigencia2Fim.isBefore(vigencia1Inicio)));
			}
		} else {
			if (vigencia2Fim == null) {
				return (vigencia2Inicio.isAfter(vigencia1Fim));
			} else {
				if (vigencia2Inicio.isBefore(vigencia1Inicio)) {
					return (vigencia2Fim.isBefore(vigencia1Inicio));
				} else {
					return (vigencia2Inicio.isAfter(vigencia1Fim));
				}
			}
		}
	}
	
	private boolean isVerificaTrocaFiliais(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		Filial filialNegociacao = clienteTerritorioNovo.getTerritorio().getFilial();
		Filial filialSedeExecutivo = clienteTerritorioAtual.getTerritorio().getFilial();
		return !filialNegociacao.getIdFilial().equals(filialSedeExecutivo.getIdFilial());
	}
	
	private void validateConflitos(ClienteTerritorio clienteTerritorio, ClienteTerritorio clienteTerritorioNovo) {
		
		if (clienteTerritorio.getTpSituacaoAprovacao() == null) {
			clienteTerritorio.setTpSituacaoAprovacao(DmStatusEnum.ATIVO.getDomainValue());
		}

		if (TP_SITUACAO_EM_APROVACAO.equals(clienteTerritorio.getTpSituacaoAprovacao().getValue())) {
			
			throw new BusinessException(EXCEPTION_NAO_PODE_SER_ALTERADO_EM_APROVACAO);
			
		} else if (TP_SITUACAO_ATIVO.equals(clienteTerritorio.getTpSituacaoAprovacao().getValue())) {

			if (isConflitoEntreVigencias(clienteTerritorioNovo, clienteTerritorio)) {
				if (isIntersecaoEntreModais(clienteTerritorioNovo, clienteTerritorio)
						&& !isConflitoPermitido(clienteTerritorioNovo, clienteTerritorio)) {
					throw new BusinessException(EXCEPTION_CONFLITO_MODAIS);
				}
				if (isMesmoTerritorio(clienteTerritorioNovo, clienteTerritorio)) {
					throw new BusinessException(EXCEPTION_CONFLITO_TERRITORIO);
				}
			}
			
			if (!isExisteIntersecaoDeVigencias(clienteTerritorioNovo.getDtInicio(), clienteTerritorioNovo.getDtFim(), clienteTerritorio.getDtInicio(), clienteTerritorio.getDtFim())) {
				throw new BusinessException(EXCEPTION_SOBREPOSICAO_DE_INTERVALOS);
			}

		}

	}

	private void validateCarenciaTrocaDeTerritorios(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		if ((clienteTerritorioAtual != null) && (!clienteTerritorioAtual.getTerritorio().getIdTerritorio().equals(clienteTerritorioNovo.getTerritorio().getIdTerritorio()))) {
			int dias = JTDateTimeUtils.getIntervalInDays(clienteTerritorioAtual.getDtInicio(), new YearMonthDay(JTDateTimeUtils.getDataHoraAtual()));
			int carencia = Integer.parseInt(parametroGeralService.findByNomeParametro(NR_DIAS_TRANSFERENCIA, false).getDsConteudo());
			if (dias < carencia) {
				throw new BusinessException(EXCEPTION_CARENCIA_TROCA_TERRITORIO);
			}
		}
	}

	private void validate(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {

		if (clienteTerritorioNovo.getCliente() == null) {
			throw new BusinessException(EXCEPTION_CLIENTE_NAO_INFORMADO);
		}
		
		if (clienteTerritorioNovo.getTerritorio() == null) {
			throw new BusinessException(EXCEPTION_TERRITORIO_NAO_INFORMADO);
		}
		
		if (clienteTerritorioNovo.getDtInicio() == null) {
			throw new BusinessException(EXCEPTION_INICIO_VIGENCIA_NAO_INFORMADA);
		}
		
		if (clienteTerritorioNovo.getDtFim() != null
				&& clienteTerritorioNovo.getDtFim().isBefore(clienteTerritorioNovo.getDtInicio())) {
			throw new BusinessException(EXCEPTION_PERIODO_VIGENCIA_INVALIDO);
		}
		
		validateCarenciaTrocaDeTerritorios(clienteTerritorioAtual, clienteTerritorioNovo);
		
		List<ClienteTerritorio> clienteTerritorioLista = getListaSemRegistroAtual(clienteTerritorioAtual, clienteTerritorioNovo);
		for (ClienteTerritorio clienteTerritorio : clienteTerritorioLista) {
			validateConflitos(clienteTerritorio, clienteTerritorioNovo);
		}

	}
	
	private List<ClienteTerritorio> getListaSemRegistroAtual(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		List<ClienteTerritorio> fullList = find(clienteTerritorioNovo.getCliente().getIdCliente(), null, null, null, null, null, null, null);
		if (clienteTerritorioAtual == null) {
			return fullList;
		}

		List<ClienteTerritorio> list = new ArrayList<ClienteTerritorio>();
		for (ClienteTerritorio ct : fullList) {
			if (!ct.getIdClienteTerritorio().equals(clienteTerritorioAtual.getIdClienteTerritorio())) {
				list.add(ct);
			}
		}
		return list;
	}

	private ClienteTerritorio buscaClienteTerritorioAtual(ClienteTerritorio clienteTerritorio) {
		if (clienteTerritorio.getIdClienteTerritorio() == null) {
			return null;
		} else {
			return findById(clienteTerritorio.getIdClienteTerritorio());
		}
	}

	private ClienteTerritorio criaClienteTerritorioATransferir(ClienteTerritorio clienteTerritorio, Territorio territorioPara, YearMonthDay dtInicio, UsuarioLMS usuarioInclusao) {
		ClienteTerritorio ct = new ClienteTerritorio();
		
		ct.setCliente(clienteTerritorio.getCliente());
		ct.setTerritorio(territorioPara);
		ct.setTpModal(clienteTerritorio.getTpModal());
		ct.setTpSituacao(clienteTerritorio.getTpSituacao());
		ct.setDtInicio(dtInicio);
		ct.setDtFim(null);
		
		ct.setUsuarioInclusao(usuarioInclusao);
		ct.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		ct.setUsuarioAlteracao(usuarioInclusao);
		ct.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		
		return ct;
	}

	private ClienteTerritorio criaClienteTerritorioNovo(ClienteTerritorio clienteTerritorio) {
		DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
		ClienteTerritorio clienteTerritorioNovo = new ClienteTerritorio();

		clienteTerritorioNovo.setUsuarioInclusao(clienteTerritorio.getUsuarioAlteracao());
		clienteTerritorioNovo.setDhInclusao(dhAtual);

		clienteTerritorioNovo.setCliente(clienteTerritorio.getCliente());
		clienteTerritorioNovo.setTerritorio(territorioService.findById(clienteTerritorio.getTerritorio().getIdTerritorio()));
		clienteTerritorioNovo.setTpModal(clienteTerritorio.getTpModal());
		clienteTerritorioNovo.setDtInicio(clienteTerritorio.getDtInicio());
		clienteTerritorioNovo.setDtFim(clienteTerritorio.getDtFim());

		clienteTerritorioNovo.setTpSituacao(DmStatusEnum.INATIVO.getDomainValue());
		clienteTerritorioNovo.setTpSituacaoAprovacao(new DomainValue(TP_SITUACAO_EM_APROVACAO));
		clienteTerritorioNovo.setUsuarioAlteracao(clienteTerritorio.getUsuarioAlteracao());
		clienteTerritorioNovo.setDhAlteracao(dhAtual);

		return clienteTerritorioNovo;
	}

	public Serializable saveWorkflow(ClienteTerritorio clienteTerritorio) {

 		ClienteTerritorio clienteTerritorioAtual = buscaClienteTerritorioAtual(clienteTerritorio);
 		ClienteTerritorio clienteTerritorioNovo = criaClienteTerritorioNovo(clienteTerritorio);
 		
 		validate(clienteTerritorioAtual, clienteTerritorioNovo);
 		
		if (clienteTerritorioAtual == null) {
			return geraWorkflowParaInclusao(clienteTerritorioNovo);
		}	
			
		if (!isCamposIdenticos(clienteTerritorioAtual, clienteTerritorioNovo)) {
			return geraWorkflowParaAlteracao(clienteTerritorioAtual, clienteTerritorioNovo);
 		} 
		
		return clienteTerritorioAtual.getIdClienteTerritorio();
 	}
 	
	private Serializable geraWorkflowParaInclusao(ClienteTerritorio clienteTerritorioNovo) {
		store(clienteTerritorioNovo);
		
		Pendencia pendencia = geraPendenciaParaInclusaoPrimeiroPasso(clienteTerritorioNovo);
		clienteTerritorioNovo.setTpSituacaoAprovacao(new DomainValue(TP_SITUACAO_EM_APROVACAO));
		clienteTerritorioNovo.setPendenciaAprovacao(pendencia);
		store(clienteTerritorioNovo);

		return clienteTerritorioNovo.getIdClienteTerritorio();
	}

	private Serializable geraWorkflowParaAlteracao(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		
		if ((clienteTerritorioAtual.getTpSituacaoAprovacao() != null) && (TP_SITUACAO_EM_APROVACAO.equals(clienteTerritorioAtual.getTpSituacaoAprovacao().getValue()))) {
			throw new BusinessException(EXCEPTION_NAO_PODE_SER_ALTERADO_EM_APROVACAO);
		}

		// Inicializa a vigência caso ela não tenha sido alterada
		if (isDatasIguais(clienteTerritorioAtual.getDtInicio(), clienteTerritorioNovo.getDtInicio()) &&
			isDatasIguais(clienteTerritorioAtual.getDtFim(), clienteTerritorioNovo.getDtFim())) {
			clienteTerritorioNovo.setDtInicio(new YearMonthDay(edwService.findCalendarioTNTByData(JTDateTimeUtils.getDataAtual()).getDataInicioMesProducao()));
			clienteTerritorioNovo.setDtFim(null);
		}
		
		store(clienteTerritorioNovo);

		Pendencia pendencia = null;
		if (isVerificaTrocaFiliais(clienteTerritorioAtual, clienteTerritorioNovo)) {
			pendencia = geraPendenciaParaTrocaDeFiliaisPrimeiroPasso(clienteTerritorioAtual, clienteTerritorioNovo);
		} else {
			Set<String> pendencias = getPendencias(clienteTerritorioAtual, clienteTerritorioNovo);
			pendencia = geraPendenciaSimples(clienteTerritorioAtual, clienteTerritorioNovo, pendencias);
		}
		
		clienteTerritorioNovo.setTpSituacaoAprovacao(new DomainValue(TP_SITUACAO_EM_APROVACAO));
		clienteTerritorioNovo.setPendenciaAprovacao(pendencia);
		store(clienteTerritorioNovo);
		
		return clienteTerritorioNovo.getIdClienteTerritorio();
	}

	private Pendencia geraPendenciaSimples(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo, Set<String> pendencias) {
		if (pendencias.contains("alteracaoTerritorio")) {
			return geraPendenciaParaTrocaDeTerritorioMesmaFilial(clienteTerritorioAtual, clienteTerritorioNovo);
		} else if (pendencias.contains("alteracaoModal")) {
			return geraPendenciaParaTrocaDeModal(clienteTerritorioAtual, clienteTerritorioNovo);
		} else if (pendencias.contains("alteracaoData")) {
			return geraPendenciaParaTrocaDeVigencia(clienteTerritorioAtual, clienteTerritorioNovo);
		} else {
			throw new IllegalStateException("Pendencia não encontrada para ser gerada");
		}
	}

	private Set<String> getPendencias(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		Set<String> pendencias = new HashSet<String>();
		
		if (!isIdTerritoriosIguais(clienteTerritorioAtual.getTerritorio().getIdTerritorio(), clienteTerritorioNovo.getTerritorio().getIdTerritorio())) {
			pendencias.add("alteracaoTerritorio");
		}
		
		if ((!isDatasIguais(clienteTerritorioAtual.getDtInicio(), clienteTerritorioNovo.getDtInicio())) 
			|| (!isDatasIguais(clienteTerritorioAtual.getDtFim(), clienteTerritorioNovo.getDtFim()))) {
			
			pendencias.add("alteracaoData");
		}
		
		if (!(clienteTerritorioAtual.getTpModal() == null && clienteTerritorioNovo.getTpModal() == null)) {
			if ((clienteTerritorioAtual.getTpModal() == null && clienteTerritorioNovo.getTpModal() != null) 
				|| (clienteTerritorioAtual.getTpModal() != null && clienteTerritorioNovo.getTpModal() == null) 
				|| !clienteTerritorioAtual.getTpModal().getValue().equals(clienteTerritorioNovo.getTpModal().getValue())) {

				pendencias.add("alteracaoModal");
			}
		}

		return pendencias;
	}

	private Pendencia geraPendenciaWorkflow(Short evento, Long idProcesso, 
			String justificativa, 
			CampoHistoricoWorkflow campoHistoricoWorkflow, String idClienteTerritorioAntigo, Long idFilial) {
		
		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(idFilial);
		pendenciaHistoricoDTO.setNrTipoEvento(evento);
		pendenciaHistoricoDTO.setIdProcesso(idProcesso);
		pendenciaHistoricoDTO.setDsProcesso(justificativa);
		pendenciaHistoricoDTO.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
		pendenciaHistoricoDTO.setCampoHistoricoWorkflow(campoHistoricoWorkflow);
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.CLIENTE_TERRITORIO);
		pendenciaHistoricoDTO.setDsVlAntigo("");
		pendenciaHistoricoDTO.setDsVlNovo(idClienteTerritorioAntigo);
		pendenciaHistoricoDTO.setDsObservacao(" - ");
 
		return workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
	}

	private String getModalDescription(DomainValue modal) {
		String texto = "Ambos";
		if (modal != null) {
			texto = modal.getDescriptionAsString();
		}
		return texto;
	}
	
	private String buildJustificativaInclusao(ClienteTerritorio clienteTerritorioNovo) {
		Cliente cliente = clienteService.findById(clienteTerritorioNovo.getCliente().getIdCliente());
		
		List<String> parametros = new ArrayList<String>();
		parametros.add(SessionUtils.getUsuarioLogado().getNmUsuario());
		parametros.add(SessionUtils.getUsuarioLogado().getNrMatricula());
		parametros.add(FormatUtils.formatIdentificacao(cliente.getPessoa()));
		parametros.add(cliente.getPessoa().getNmPessoa());
		parametros.add(cliente.getFilialByIdFilialAtendeComercial().getSgFilial());
		if (clienteTerritorioNovo.getTerritorio().getFilial() != null) {
			parametros.add(clienteTerritorioNovo.getTerritorio().getFilial().getSgFilial());
		} else {
			parametros.add("-");
		}
		parametros.add(cliente.getTpCliente().getDescriptionAsString());
		parametros.add(cliente.getTpSituacao().getDescriptionAsString());

		parametros.add(getModalDescription(clienteTerritorioNovo.getTpModal()));
		parametros.add(clienteTerritorioNovo.getDtInicio().toString("dd/MM/yyyy"));
		parametros.add(clienteTerritorioNovo.getTerritorio().getNmTerritorio());

		return (configuracoesFacade.getMensagem(MENSAGEM_INCLUSAO, parametros.toArray()));
	}
	
	private String buildJustificativaParaTrocaDeFiliais(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		Cliente cliente = clienteService.findById(clienteTerritorioNovo.getCliente().getIdCliente());
		
		List<String> parametros = new ArrayList<String>();
		parametros.add(SessionUtils.getUsuarioLogado().getNmUsuario());
		parametros.add(SessionUtils.getUsuarioLogado().getNrMatricula());
		parametros.add(FormatUtils.formatIdentificacao(cliente.getPessoa()));
		parametros.add(cliente.getPessoa().getNmPessoa());
		parametros.add(cliente.getFilialByIdFilialAtendeComercial().getSgFilial());
		if (clienteTerritorioNovo.getTerritorio().getFilial() != null) {
			parametros.add(clienteTerritorioNovo.getTerritorio().getFilial().getSgFilial());
		} else {
			parametros.add("-");
		}
		parametros.add(cliente.getTpCliente().getDescriptionAsString());
		parametros.add(cliente.getTpSituacao().getDescriptionAsString());
		parametros.add(getModalDescription(clienteTerritorioNovo.getTpModal()));
		parametros.add(clienteTerritorioNovo.getDtInicio().toString("dd/MM/yyyy"));
		parametros.add(clienteTerritorioAtual.getTerritorio().getNmTerritorio());
		parametros.add(clienteTerritorioNovo.getTerritorio().getNmTerritorio());
		parametros.add(clienteTerritorioAtual.getTerritorio().getFilial().getSgFilial());

		return (configuracoesFacade.getMensagem(MENSAGEM_TROCA_DE_FILIAIS, parametros.toArray()));
	}		
	
	private String buildJustificativaParaTrocaDeTerritorioMesmaFilial(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		Cliente cliente = clienteService.findById(clienteTerritorioNovo.getCliente().getIdCliente());
		
		List<String> parametros = new ArrayList<String>();
		parametros.add(SessionUtils.getUsuarioLogado().getNmUsuario());
		parametros.add(SessionUtils.getUsuarioLogado().getNrMatricula());
		parametros.add(FormatUtils.formatIdentificacao(cliente.getPessoa()));
		parametros.add(cliente.getPessoa().getNmPessoa());
		parametros.add(cliente.getFilialByIdFilialAtendeComercial().getSgFilial());
		if (clienteTerritorioNovo.getTerritorio().getFilial() != null) {
			parametros.add(clienteTerritorioNovo.getTerritorio().getFilial().getSgFilial());
		} else {
			parametros.add("-");
		}
		parametros.add(cliente.getTpCliente().getDescriptionAsString());
		parametros.add(cliente.getTpSituacao().getDescriptionAsString());
		
		parametros.add(getModalDescription(clienteTerritorioNovo.getTpModal()));
		parametros.add(clienteTerritorioNovo.getDtInicio().toString("dd/MM/yyyy"));
		parametros.add(clienteTerritorioAtual.getTerritorio().getNmTerritorio());
		parametros.add(clienteTerritorioNovo.getTerritorio().getNmTerritorio());

		return (configuracoesFacade.getMensagem(MENSAGEM_TROCA_DE_TERRITORIO_MESMA_FILIAL, parametros.toArray()));
	}		

	private String buildJustificativaParaTrocaDeModal(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		Cliente cliente = clienteService.findById(clienteTerritorioNovo.getCliente().getIdCliente());
		
		List<String> parametros = new ArrayList<String>();
		parametros.add(SessionUtils.getUsuarioLogado().getNmUsuario());
		parametros.add(SessionUtils.getUsuarioLogado().getNrMatricula());
		parametros.add(FormatUtils.formatIdentificacao(cliente.getPessoa()));
		parametros.add(cliente.getPessoa().getNmPessoa());
		parametros.add(cliente.getFilialByIdFilialAtendeComercial().getSgFilial());
		if (clienteTerritorioNovo.getTerritorio().getFilial() != null) {
			parametros.add(clienteTerritorioNovo.getTerritorio().getFilial().getSgFilial());
		} else {
			parametros.add("-");
		}
		parametros.add(cliente.getTpCliente().getDescriptionAsString());
		parametros.add(cliente.getTpSituacao().getDescriptionAsString());

		parametros.add(clienteTerritorioNovo.getDtInicio().toString("dd/MM/yyyy"));
		parametros.add(clienteTerritorioNovo.getTerritorio().getNmTerritorio());
		parametros.add(getModalDescription(clienteTerritorioAtual.getTpModal()));
		parametros.add(getModalDescription(clienteTerritorioNovo.getTpModal()));
		
		return (configuracoesFacade.getMensagem(MENSAGEM_TROCA_DE_MODAL, parametros.toArray()));
	}
	
	private String buildJustificativaParaTrocaDeVigencia(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		Cliente cliente = clienteService.findById(clienteTerritorioNovo.getCliente().getIdCliente());
		
		List<String> parametros = new ArrayList<String>();
		parametros.add(SessionUtils.getUsuarioLogado().getNmUsuario());
		parametros.add(SessionUtils.getUsuarioLogado().getNrMatricula());
		parametros.add(FormatUtils.formatIdentificacao(cliente.getPessoa()));
		parametros.add(cliente.getPessoa().getNmPessoa());
		parametros.add(cliente.getFilialByIdFilialAtendeComercial().getSgFilial());
		if (clienteTerritorioNovo.getTerritorio().getFilial() != null) {
			parametros.add(clienteTerritorioNovo.getTerritorio().getFilial().getSgFilial());
		} else {
			parametros.add("-");
		}
		parametros.add(cliente.getTpCliente().getDescriptionAsString());
		parametros.add(cliente.getTpSituacao().getDescriptionAsString());

		parametros.add(getModalDescription(clienteTerritorioNovo.getTpModal()));
		parametros.add(clienteTerritorioNovo.getTerritorio().getNmTerritorio());
		parametros.add(clienteTerritorioAtual.getDtInicio().toString("dd/MM/yyyy"));
		parametros.add(clienteTerritorioNovo.getDtInicio().toString("dd/MM/yyyy"));
		
		return (configuracoesFacade.getMensagem(MENSAGEM_TROCA_DE_VIGENCIA, parametros.toArray()));
	}
	
	private Pendencia geraPendenciaParaInclusaoPrimeiroPasso(ClienteTerritorio clienteTerritorioNovo) {
		return geraPendenciaWorkflow(EVENTO_INCLUSAO_PRIMEIRO_PASSO, clienteTerritorioNovo.getIdClienteTerritorio(), 
			buildJustificativaInclusao(clienteTerritorioNovo), 
			CampoHistoricoWorkflow.AICL, "", SessionUtils.getFilialSessao().getIdFilial());
	}

	private Pendencia geraPendenciaParaInclusaoSegundoPasso(ClienteTerritorio clienteTerritorioNovo, String dsPendencia) {
		return geraPendenciaWorkflow(EVENTO_INCLUSAO_SEGUNDO_PASSO, clienteTerritorioNovo.getIdClienteTerritorio(), 
			dsPendencia, 
			CampoHistoricoWorkflow.AICL, "", SessionUtils.getFilialSessao().getIdFilial());
	}

	private Pendencia geraPendenciaParaTrocaDeFiliaisPrimeiroPasso(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		return geraPendenciaWorkflow(EVENTO_TROCA_FILIAIS_PRIMEIRO_PASSO, clienteTerritorioNovo.getIdClienteTerritorio(), 
			buildJustificativaParaTrocaDeFiliais(clienteTerritorioAtual, clienteTerritorioNovo), 
			CampoHistoricoWorkflow.ATCF, clienteTerritorioAtual.getIdClienteTerritorio().toString(), SessionUtils.getFilialSessao().getIdFilial());
	}

	private Pendencia geraPendenciaParaTrocaDeFiliaisSegundoPasso(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo, String dsPendencia) {
		return geraPendenciaWorkflow(EVENTO_TROCA_FILIAIS_SEGUNDO_PASSO, clienteTerritorioNovo.getIdClienteTerritorio(), 
			dsPendencia,  
			CampoHistoricoWorkflow.ATCF, clienteTerritorioAtual.getIdClienteTerritorio().toString(), clienteTerritorioAtual.getTerritorio().getFilial().getIdFilial());
	}

	private Pendencia geraPendenciaParaTrocaDeTerritorioMesmaFilial(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		return geraPendenciaWorkflow(EVENTO_TROCA_DADOS_MESMA_FILIAL, clienteTerritorioNovo.getIdClienteTerritorio(), 
			buildJustificativaParaTrocaDeTerritorioMesmaFilial(clienteTerritorioAtual, clienteTerritorioNovo), 
			CampoHistoricoWorkflow.ATCL, clienteTerritorioAtual.getIdClienteTerritorio().toString(), SessionUtils.getFilialSessao().getIdFilial());
	}
	
	private Pendencia geraPendenciaParaTrocaDeModal(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		return geraPendenciaWorkflow(EVENTO_TROCA_DADOS_MESMA_FILIAL, clienteTerritorioNovo.getIdClienteTerritorio(), 
			buildJustificativaParaTrocaDeModal(clienteTerritorioAtual, clienteTerritorioNovo), 
			CampoHistoricoWorkflow.ATCL, clienteTerritorioAtual.getIdClienteTerritorio().toString(), SessionUtils.getFilialSessao().getIdFilial());
	}
	
	private Pendencia geraPendenciaParaTrocaDeVigencia(ClienteTerritorio clienteTerritorioAtual, ClienteTerritorio clienteTerritorioNovo) {
		return geraPendenciaWorkflow(EVENTO_TROCA_DADOS_MESMA_FILIAL, clienteTerritorioNovo.getIdClienteTerritorio(), 
			buildJustificativaParaTrocaDeVigencia(clienteTerritorioAtual, clienteTerritorioNovo), 
			CampoHistoricoWorkflow.ATCL, clienteTerritorioAtual.getIdClienteTerritorio().toString(), SessionUtils.getFilialSessao().getIdFilial());
	}
	
	private boolean isAprovado(List<String> tpSituacaoAprovacao) {
		boolean aprovado = true;
		for (String situacao : tpSituacaoAprovacao) {
			if (!"A".equals(situacao)) {
				aprovado = false;
			}
		}
		return aprovado;
	}
	
	private ClienteTerritorio getClienteTerritorioAprovado(ClienteTerritorio clienteTerritorio) {
		clienteTerritorio.setTpSituacao(DmStatusEnum.ATIVO.getDomainValue());
		clienteTerritorio.setTpSituacaoAprovacao(new DomainValue("A"));
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		if (clienteTerritorio.getDtInicio().getMonthOfYear() != dataAtual.getMonthOfYear()) {
			CalendarioTntDMN calendarioTntDMN = edwService.findCalendarioTNTByData(dataAtual);
			clienteTerritorio.setDtInicio(new YearMonthDay(ISODateTimeFormat.dateTimeParser().parseDateTime(calendarioTntDMN.getDataInicioMesProducao())));
		}
		
		return clienteTerritorio;
	}

	public void executeWorkflow(List<Long> idProcesso, List<String> tpSituacaoAprovacao) {
		Long idClienteTerritorio = idProcesso.iterator().next();
		ClienteTerritorio clienteTerritorioNovo = findById(idClienteTerritorio);
		
		if (!isAprovado(tpSituacaoAprovacao)) {
			clienteTerritorioNovo.setTpSituacaoAprovacao(new DomainValue("R"));
		
		} else {
			
			Long idPendencia = clienteTerritorioNovo.getPendenciaAprovacao().getIdPendencia();
			Pendencia pendencia = pendenciaService.findById(idPendencia);
			
			Ocorrencia ocorrencia = ocorrenciaService.findById(pendencia.getOcorrencia().getIdOcorrencia());
			EventoWorkflow eventoWorkflow = ocorrencia.getEventoWorkflow();
			
			TipoEvento tipoEvento = tipoEventoService.findById(eventoWorkflow.getIdEventoWorkflow());

			if (tipoEvento.getNrTipoEvento().equals(EVENTO_INCLUSAO_PRIMEIRO_PASSO)) {
				
				// Gera Segundo Passo no Workflow de Inclusão
				clienteTerritorioNovo.setPendenciaAprovacao(geraPendenciaParaInclusaoSegundoPasso(clienteTerritorioNovo, pendencia.getDsPendencia()));
				
			} else if (tipoEvento.getNrTipoEvento().equals(EVENTO_INCLUSAO_SEGUNDO_PASSO)) {
				
				// Ativa o Registro Novo
				clienteTerritorioNovo = getClienteTerritorioAprovado(clienteTerritorioNovo);

			} else if (tipoEvento.getNrTipoEvento().equals(EVENTO_TROCA_FILIAIS_PRIMEIRO_PASSO)) {
	
				// Gera Segundo Passo no Workflow de Troca de Filiais
				List<HistoricoWorkflow> historicoWorkflow = historicoWorkflowService.findByIdPendencia(pendencia.getIdPendencia());
				ClienteTerritorio clienteTerritorioAtual = findById(Long.parseLong(historicoWorkflow.iterator().next().getDsVlNovo()));
				clienteTerritorioNovo.setPendenciaAprovacao(geraPendenciaParaTrocaDeFiliaisSegundoPasso(clienteTerritorioAtual, clienteTerritorioNovo, pendencia.getDsPendencia()));
			
			} else if (tipoEvento.getNrTipoEvento().equals(EVENTO_TROCA_FILIAIS_SEGUNDO_PASSO) 
					|| tipoEvento.getNrTipoEvento().equals(EVENTO_TROCA_DADOS_MESMA_FILIAL)) {
	
				// Inativa o Registro Atual
				List<HistoricoWorkflow> historicoWorkflow = historicoWorkflowService.findByIdPendencia(pendencia.getIdPendencia());
				ClienteTerritorio clienteTerritorioAtual = findById(Long.parseLong(historicoWorkflow.iterator().next().getDsVlNovo()));
				clienteTerritorioAtual.setTpSituacao(DmStatusEnum.INATIVO.getDomainValue());
				store(clienteTerritorioAtual);
	
				// Ativa o Registro Novo
				clienteTerritorioNovo = getClienteTerritorioAprovado(clienteTerritorioNovo);
			}
			
		}
		
		store(clienteTerritorioNovo);
	}
	
	public void updateStatusInativo(Long id) {
		ClienteTerritorio clienteTerritorio = findById(id);
		if ((clienteTerritorio.getTpSituacaoAprovacao() != null) && (TP_SITUACAO_EM_APROVACAO.equals(clienteTerritorio.getTpSituacaoAprovacao().getValue()))) {
			throw new BusinessException(EXCEPTION_NAO_PODE_SER_ALTERADO_EM_APROVACAO);
		}
		clienteTerritorio.setTpSituacao(DmStatusEnum.INATIVO.getDomainValue());
		store(clienteTerritorio);
	}
	
	public void setClienteTerritorioDao(ClienteTerritorioDAO dao) {
		setDao(dao);
	}

	public ClienteTerritorioDAO getClienteTerritorioDao() {
		return (ClienteTerritorioDAO) getDao();
	}

	public List<ClienteTerritorio> find(
			Long idCliente, 
			Long idTerritorio, 
			DomainValue tpModal, 
			YearMonthDay dtInicio, 
			YearMonthDay dtFim, 
			DmStatusEnum tpSituacao,
			Long idFilial,
			Long idRegional) {
		return getClienteTerritorioDao().find(idCliente, idTerritorio, tpModal, dtInicio, dtFim, tpSituacao, idFilial, idRegional);
	}

	public List<ClienteTerritorio> find(
			Long idCliente, 
			Long idTerritorio, 
			DomainValue tpModal, 
			YearMonthDay dtInicio, 
			YearMonthDay dtFim, 
			DmStatusEnum tpSituacao) {
		return find(idCliente, idTerritorio, tpModal, dtInicio, dtFim, tpSituacao, null, null);
	}
	
	@Override
	public ClienteTerritorio findById(Long id) {
		return getClienteTerritorioDao().findById(id);
	}

	@Override
	public void removeById(Long id) {
		getClienteTerritorioDao().removeById(id);
	}

	public Integer findCount(
			Long idCliente, 
			Long idTerritorio, 
			DomainValue tpModal, 
			YearMonthDay dtInicio, 
			YearMonthDay dtFim, 
			DmStatusEnum tpSituacao) {
		return getClienteTerritorioDao().findCount(idCliente, idTerritorio, tpModal, dtInicio, dtFim, tpSituacao);
	}

	// Verifica se o cliente já não foi transferido
	private boolean isClienteNaoFoiTransferido(Long idTerritorioPara, List<ClienteTerritorio> clienteTerritorioParaList, Long idCliente) {
		for (ClienteTerritorio clienteTerritorioPara : clienteTerritorioParaList) {
			if (clienteTerritorioPara.getCliente().getIdCliente().equals(idCliente) && clienteTerritorioPara.getTerritorio().getIdTerritorio().equals(idTerritorioPara)) {
				return false;
			}
		}
		return true;
	}
	
	// Verifica se existe vigência aberta e até a data de fechamento
	private boolean isVigenciaAberta(ClienteTerritorio clienteTerritorioDe, YearMonthDay dtFechamento) {
		if ((clienteTerritorioDe.getDtFim() != null) && (clienteTerritorioDe.getDtFim().isBefore(dtFechamento))) {
			return false;
		}
		return true;
	}
	
	private void validateTransferirCarteira(List<ClienteTerritorio> clienteTerritorioDeList) {
		for (ClienteTerritorio clienteTerritorioDe : clienteTerritorioDeList) {
			if ((clienteTerritorioDe.getTpSituacao() != null) && ("I".equals(clienteTerritorioDe.getTpSituacao().getValue()))) {
				throw new BusinessException(EXCEPTION_TRANSF_COM_REGISTRO_PENDENTE);
			}
			if ((clienteTerritorioDe.getTpSituacaoAprovacao() != null) && (TP_SITUACAO_EM_APROVACAO.equals(clienteTerritorioDe.getTpSituacaoAprovacao().getValue()))) {
				throw new BusinessException(EXCEPTION_TRANSF_COM_REGISTRO_PENDENTE);
			}
		}
	}

	public void executeTransferirCarteira(Long territorioDeId, Long territorioParaId,
			YearMonthDay dtFechamento, YearMonthDay dtInicioInformada) {
		
		List<ClienteTerritorio> clienteTerritorioDeList = getClienteTerritorioDao().findByTerritorio(territorioDeId);
		validateTransferirCarteira(clienteTerritorioDeList);

		// Data de início da vigência deve ser dia encerramento + 1
		if (dtInicioInformada.equals(dtFechamento)) {
			dtInicioInformada = dtInicioInformada.plusDays(1);
		}

		UsuarioLMS usuarioInclusao = usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
		Territorio territorioPara = territorioService.findById(territorioParaId);

		List<ClienteTerritorio> clienteTerritorioParaList = getClienteTerritorioDao().findByTerritorio(territorioParaId);
		List<ClienteTerritorio> clienteTerritorioNovoList = new ArrayList<ClienteTerritorio>();		

		for (ClienteTerritorio clienteTerritorioDe : clienteTerritorioDeList) {
			if (isClienteNaoFoiTransferido(territorioParaId, clienteTerritorioParaList, clienteTerritorioDe.getCliente().getIdCliente()) &&
				(isVigenciaAberta(clienteTerritorioDe, dtFechamento))) {

				clienteTerritorioDe.setDtFim(dtFechamento);
				clienteTerritorioDe.setUsuarioAlteracao(usuarioInclusao);
				clienteTerritorioDe.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
				
				YearMonthDay dtInicio = new YearMonthDay(dtInicioInformada);
				if (dtInicioInformada.isBefore(clienteTerritorioDe.getDtInicio())) {
					dtInicio = clienteTerritorioDe.getDtInicio();
				}
				
				clienteTerritorioNovoList.add(criaClienteTerritorioATransferir(clienteTerritorioDe, territorioPara, dtInicio, usuarioInclusao));
			}
		}

		storeAll(clienteTerritorioDeList);
		storeAll(clienteTerritorioNovoList);
	}

	public List<ClienteTerritorio> findRegistrosEmAprovacaoDoCliente(Long idCliente) {
		List<ClienteTerritorio> list = new ArrayList<ClienteTerritorio>();
		List<ClienteTerritorio> fullList = find(idCliente, null, null, null, null, null, null, null);
		for (ClienteTerritorio ct : fullList) {
			if ((ct.getTpSituacaoAprovacao() != null) && (TP_SITUACAO_EM_APROVACAO.equals(ct.getTpSituacaoAprovacao().getValue()))) {
				list.add(ct);
			}
		}
		return list;
	}

	public void setExecutivoTerritorioService(
			ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}

	public void setTerritorioService(TerritorioService territorioService) {
		this.territorioService = territorioService;
	}
	
	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public void setTipoEventoService(TipoEventoService tipoEventoService) {
		this.tipoEventoService = tipoEventoService;
	}
	
	public void setOcorrenciaService(OcorrenciaService ocorrenciaService) {
		this.ocorrenciaService = ocorrenciaService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setHistoricoWorkflowService(
			HistoricoWorkflowService historicoWorkflowService) {
		this.historicoWorkflowService = historicoWorkflowService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setEdwService(EdwService edwService) {
		this.edwService = edwService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
