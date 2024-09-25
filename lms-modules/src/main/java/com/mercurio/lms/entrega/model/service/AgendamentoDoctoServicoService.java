package com.mercurio.lms.entrega.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;
import com.mercurio.lms.entrega.model.MotivoAgendamento;
import com.mercurio.lms.entrega.model.Turno;
import com.mercurio.lms.entrega.model.dao.AgendamentoDoctoServicoDAO;
import com.mercurio.lms.entrega.model.dao.AgendamentoEntregaDAO;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.dao.DoctoServicoDAO;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.sim.model.dao.AgendamentoMonitCCTDAO;
import com.mercurio.lms.sim.model.dao.EventoDocumentoServicoDAO;
import com.mercurio.lms.sim.model.service.AgendamentoMonitCCTService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;
import com.mercurio.lms.vendas.util.ConstantesEventosPCE;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.entrega.agendamentoDoctoServicoService"
 */
public class AgendamentoDoctoServicoService extends
		CrudService<AgendamentoDoctoServico, Long> {
	
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private static final String DT_MAX_AGENDAMENTO = "DT_MAX_AGENDAMENTO";
	
	private FilialService filialService;
	private DomainValueService domainValueService;
	private ParcelaPrecoService parcelaPrecoService;
	private AgendamentoEntregaService agendamentoEntregaService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private DoctoServicoService doctoServicoService;
	private ParametroGeralService parametroGeralService;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
	private AgendamentoMonitCCTService agendamentoMonitCCTService;
	private ClienteService clienteService;
	private CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService;
	private ConfiguracoesFacade configuracoesFacade;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private DevedorDocServService devedorDocServService;
	private ContatoService contatoService;
	private TurnoService turnoService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private EnderecoPessoaService enderecoPessoaService;
	private EventoService eventoService;
	private EventoDocumentoServicoDAO eventoDocumentoServicoService;
	private AgendamentoEntregaDAO agendamentoEntregaDao;
	private DoctoServicoDAO doctoServicoDao;
	private AgendamentoMonitCCTDAO agendamentoMonitCCTDAO;
	private IntegracaoJmsService integracaoJmsService; 
	
	/**
	 * Recupera uma instância de <code>AgendamentoDoctoServico</code> a partir
	 * do ID.
	 *
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public AgendamentoDoctoServico findById(Long id) {
		return (AgendamentoDoctoServico)super.findById(id);
	}

	public AgendamentoDoctoServico findByIdCustom(Long id) {
		return this.getAgendamentoDoctoServicoDAO().findByIdCustom(id);
	}

	/**
	 * Método utilizado pela Integração
	 * 
	 * @author Andre Valadas
	 * 
	 * @param idDoctoServico
	 * @return <AgendamentoDoctoServico>
	 */
	public List findAgendamentosNaoAbertos(Long idDoctoServico) {
		return getAgendamentoDoctoServicoDAO().findAgendamentosNaoAbertos(
				idDoctoServico);
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 *
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(AgendamentoDoctoServico bean) {
		return super.store(bean);
	}

	public void storeOrUpdate(MonitoramentoCCT monitoramento, Boolean status) {

		AgendamentoMonitCCT agendamentoMonitCCT = getAgendamentoMonitCCTDAO().findAgendamentoMonitCCTByNrChave(monitoramento.getNrChave());
		String nrDocumento = ConhecimentoUtils.formatConhecimento(monitoramento.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial(), monitoramento.getDoctoServico().getNrDoctoServico());
		
		if(agendamentoMonitCCT != null){
			if(status){
				if(agendamentoMonitCCT.getMonitoramentoCCT().getDoctoServico().getAgendamentoDoctoServicos() == null ||
						agendamentoMonitCCT.getMonitoramentoCCT().getDoctoServico().getAgendamentoDoctoServicos().size() <= 0){
					AgendamentoDoctoServico newAgendamentoDoc = new AgendamentoDoctoServico();   
					
					newAgendamentoDoc.setAgendamentoEntrega(agendamentoMonitCCT.getAgendamentoEntrega());
					newAgendamentoDoc.setTpSituacao(new DomainValue(ConstantesSim.DOCTO_ATIVO));
					newAgendamentoDoc.setDoctoServico(agendamentoMonitCCT.getMonitoramentoCCT().getDoctoServico());
					
					getAgendamentoDoctoServicoDAO().store(newAgendamentoDoc);
					storeEventoDocumentoServico(getEventoService().findByCdEvento(ConstantesSim.EVENTO_AGENDAMENTO),
							agendamentoMonitCCT.getMonitoramentoCCT().getDoctoServico(), 
							agendamentoMonitCCT.getAgendamentoEntrega().getFilial(), 
							nrDocumento,
							agendamentoMonitCCT.getAgendamentoEntrega().getDtAgendamento().toDateTimeAtMidnight(),
							agendamentoMonitCCT.getAgendamentoEntrega().getFilial().getSiglaNomeFilial(),
							monitoramento.getDoctoServico().getTpDocumentoServico().getValue()
							);
				}
			}else{
				AgendamentoDoctoServico agendamento = getAgendamentoDoctoServicoDAO().findAgendamentoDoctoServicoByIdAgendamentoEntregaAndIdDoctoServico(
						agendamentoMonitCCT.getAgendamentoEntrega().getIdAgendamentoEntrega(), 
						agendamentoMonitCCT.getMonitoramentoCCT().getDoctoServico().getIdDoctoServico());
				if(agendamento != null){			
					getAgendamentoDoctoServicoDAO().remove(agendamento);
					storeEventoDocumentoServico(getEventoService().findByCdEvento(ConstantesSim.EVENTO_CANCELAMENTO),
							agendamento.getDoctoServico(), 
							agendamento.getAgendamentoEntrega().getFilial(), 
							nrDocumento,
							JTDateTimeUtils.getDataHoraAtual(),
							null,
							monitoramento.getDoctoServico().getTpDocumentoServico().getValue()
							);
				}
			}
		}
	}

	/**
	 * Fix para recuperar o DoctoServico da sessão, pois o update ainda não foi
	 * executado no banco.
	 * 
	 * LMS-6744
	 * 
	 * @param monitoramento
	 * @param agendamentoInfos
	 * @return Long
	 */
	private Long getIdDoctoServico(MonitoramentoCCT monitoramento, Map<String, Long> agendamentoInfos) {
		Long idDoctoServico = agendamentoInfos.get("idDoctoServico");
		
		if(idDoctoServico == null){
			idDoctoServico = monitoramento.getDoctoServico().getIdDoctoServico();
		}
		
		return idDoctoServico;
	}	
	
	private void storeEventoDocumentoServico(Evento evento, DoctoServico doctoServico, Filial filial,
			String nrDoctoServico, DateTime dhEvento, String dsObservacao, String tpDocumento){		
			incluirEventosRastreabilidadeInternacionalService.gerarEventoDocumento(
					evento,
					doctoServico,
					filial,
					nrDoctoServico,
					dhEvento,
					dsObservacao,
					tpDocumento 
			);
	}

	public TypedFlatMap storeCustom(TypedFlatMap map) {
		Boolean reagendamentoParcial = false;
		
		if ("CTRC".equals(map.get("tpDocumento"))
				&& (map.get("dsEmailTomador") == null	|| map.get("dsEmailTomador").toString().isEmpty()
						|| map.get("dsEmailDestinatario") == null || map.get("dsEmailDestinatario").toString().isEmpty())) {
			throw new BusinessException("LMS-04457");
		}
		
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		if (map.getString("tpAgendamento") != null
				&& !"TA".equals(map.getString("tpAgendamento").trim())
				&& (map.getString("nmContato") == null || map
						.getString("nmContato").trim().equals(""))) {
			throw new BusinessException("LMS-09080");
		}

		if(map.getString("dtAgendamento") != null && !map.getString("dtAgendamento").isEmpty()){
		DateTime dtAgendamento = new DateTime(map.getString("dtAgendamento"));
			if(dtAgendamento.isAfter(JTDateTimeUtils.getDataHoraAtual())){
				validaDataAgendamento(dtAgendamento);
			}
		}		
		
		AgendamentoEntrega agendamentoEntrega = new AgendamentoEntrega();
		if ( map.getLong("idAgendamentoEntrega") != null ) {
			agendamentoEntrega = agendamentoEntregaService.findById(map
					.getLong("idAgendamentoEntrega"));
		}

		String tpAgendamento = agendamentoEntrega.getTpAgendamento() != null ? agendamentoEntrega
				.getTpAgendamento().getValue() : map.getString("tpAgendamento")
				.trim();
		
		// Se for reagendamento ...
		if ((map.getBoolean("blReagendamento") != null)
				&& (map.getBoolean("blReagendamento").equals(Boolean.TRUE))) {
			if ( map.getLong("idAgendamentoEntrega") != null ) {
				
		ArrayList<TypedFlatMap> doctoServicoAgendamento = (ArrayList<TypedFlatMap>) map
			.get("documentoServicoAgendamento");
				
				List<AgendamentoDoctoServico> documentosReagendados = new ArrayList<AgendamentoDoctoServico>();

		// Separa os documentos que serao reagendados e os documentos
		// que serao mantidos com a mesma data de entrega
		for (AgendamentoDoctoServico agendamentoDoctoServicoAntigo : (List<AgendamentoDoctoServico>) agendamentoEntrega
			.getAgendamentoDoctoServicos()) {
					boolean reagenda = false;

					for (TypedFlatMap doctoAgendamento:doctoServicoAgendamento){
						Long idDoctoServico = doctoAgendamento
								.getLong("doctoServico.idDoctoServico");

						if (agendamentoDoctoServicoAntigo.getDoctoServico()
								.getIdDoctoServico().equals(idDoctoServico)) {
						AgendamentoDoctoServico agendamentoDoctoServicoNovo = new AgendamentoDoctoServico();
							BeanUtils.copyProperties(
									agendamentoDoctoServicoAntigo,
									agendamentoDoctoServicoNovo);
							agendamentoDoctoServicoNovo
									.setIdAgendamentoDoctoServico(null);
							documentosReagendados
									.add(agendamentoDoctoServicoNovo);
							break;
					}
				}
				}

				if (documentosReagendados.size() < agendamentoEntrega.getAgendamentoDoctoServicos().size()){
					reagendamentoParcial = true;
					
					//Remove do agendamento original os documentos reagendados
					for (AgendamentoDoctoServico ads: documentosReagendados){
						for (AgendamentoDoctoServico agendamentoDoctoServicoAntigo : (List<AgendamentoDoctoServico>) agendamentoEntrega.getAgendamentoDoctoServicos()) {
							if (agendamentoDoctoServicoAntigo.getDoctoServico().getIdDoctoServico().equals(ads.getDoctoServico().getIdDoctoServico())) {
								removeById(agendamentoDoctoServicoAntigo.getIdAgendamentoDoctoServico());
				}
				}
					}

		    // gera o reagendamento com os documentos que foram
		    // selecionados
					AgendamentoEntrega agendamentoEntregaNovo = clonaAgendamentoReagendamento(agendamentoEntrega, documentosReagendados, map);
		    agendamentoEntregaNovo.setAgendamentoEntregaOriginal(agendamentoEntrega);
					agendamentoEntregaNovo.setUsuarioByIdUsuarioCriacao(SessionUtils.getUsuarioLogado());
					agendamentoEntregaNovo.setDhEnvio(JTDateTimeUtils.getDataHoraAtual());
					map.put("dhEnvio", agendamentoEntregaNovo.getDhEnvio());
					agendamentoEntregaService.store(agendamentoEntregaNovo);

					map.put("list", verificaExistenciaPce(agendamentoEntregaNovo));
					map.put("idAgendamentoEntrega", agendamentoEntregaNovo.getIdAgendamentoEntrega());
					executeSendMailAgendamento((Long)map.get("idAgendamentoEntrega"), Boolean.TRUE);
				} else if (documentosReagendados.size() >= agendamentoEntrega.getAgendamentoDoctoServicos().size() && documentosReagendados.size() != 0){
		    // gera o reagendamento com os documentos que foram
		    // selecionados
					AgendamentoEntrega agendamentoEntregaNovo = clonaAgendamentoReagendamento(agendamentoEntrega, documentosReagendados, map);
						
				agendamentoEntrega.setTpSituacaoAgendamento(new DomainValue("R"));
		    agendamentoEntrega.setDhFechamento(JTDateTimeUtils.getDataHoraAtual());
				agendamentoEntrega.setReagendamento(agendamentoEntregaNovo);
					agendamentoEntregaNovo.setUsuarioByIdUsuarioCriacao(SessionUtils.getUsuarioLogado());
				
				agendamentoEntregaNovo.setDhEnvio(JTDateTimeUtils.getDataHoraAtual());
				map.put("dhEnvio", agendamentoEntregaNovo.getDhEnvio());
				agendamentoEntregaService.store(agendamentoEntrega);

				map.put("list", verificaExistenciaPce(agendamentoEntregaNovo));
					map.put("idAgendamentoEntrega", agendamentoEntregaNovo.getIdAgendamentoEntrega());
					executeSendMailAgendamento((Long)map.get("idAgendamentoEntrega"), Boolean.TRUE);
				}
				
				//LMS-5949 : reagendamento por NF-e 
				if (map.containsKey("nrChaveNfe")) {
					List<TypedFlatMap> nrChaveNfes = map.getList("nrChaveNfe");

					if (nrChaveNfes != null && !nrChaveNfes.isEmpty()) {
										    	
				    	//Criar um novo registro na tabela AGENDAMENTO_ENTREGA.
				    	AgendamentoEntrega agendamentoEntregaNovo = clonaAgendamentoReagendamento(agendamentoEntrega, null ,map);
					    agendamentoEntregaNovo.setAgendamentoEntregaOriginal(agendamentoEntrega);
						agendamentoEntregaNovo.setUsuarioByIdUsuarioCriacao(SessionUtils.getUsuarioLogado());
						agendamentoEntregaNovo.setDhEnvio(JTDateTimeUtils.getDataHoraAtual());
						map.put("dhEnvio", agendamentoEntregaNovo.getDhEnvio());
						agendamentoEntregaService.store(agendamentoEntregaNovo);
						
						// Gravar no AGENDAMENTO_ENTREGA original:(ID_MOTIVO_REAGENDAMENTO = motivo informado na tela, ID_REAGENDAMENTO = ID_AGENDAMENTO_ENTREGA do novo registro criado., TP_SITUACAO_AGENDAMENTO = "R", DH_FECHAMENTO = Data/hora atual
						
						if ( map.getLong("motivo") != null ) {
							MotivoAgendamento motivoAgendamento = new MotivoAgendamento();
							motivoAgendamento.setIdMotivoAgendamento( map.getLong("motivo") );
							agendamentoEntrega.setMotivoAgendamentoByIdMotivoReagendamento(motivoAgendamento);
						}
						
						agendamentoEntrega.setReagendamento(agendamentoEntregaNovo);
						agendamentoEntrega.setTpSituacaoAgendamento(new DomainValue("R"));
						agendamentoEntrega.setDhFechamento(JTDateTimeUtils.getDataHoraAtual());

						map.put("idAgendamentoEntrega", agendamentoEntregaNovo.getIdAgendamentoEntrega());
						
						for (TypedFlatMap flatMap : nrChaveNfes) {
							MonitoramentoCCT monitoramentoCCT;
							String nfe = flatMap.getString("nrChave");
							Cliente remetente = findClienteRemetente(nfe);
									
							this.monitoramentoNotasFiscaisCCTService.storeEvento("RA", nfe, remetente, null, null, agendamentoEntregaNovo.getObAgendamentoEntrega(), null, SessionUtils.getUsuarioLogado());

							monitoramentoCCT = this.monitoramentoNotasFiscaisCCTService
									.findMonitoramentoCCTByNrChave(nfe);
							
							AgendamentoMonitCCT agendamentoMonitCCT = new AgendamentoMonitCCT();
							agendamentoMonitCCT.setAgendamentoEntrega(agendamentoEntregaNovo);
							agendamentoMonitCCT.setMonitoramentoCCT(monitoramentoCCT);
							this.agendamentoMonitCCTService.store(agendamentoMonitCCT);
						}
						agendamentoEntregaService.store(agendamentoEntrega);	
						map.put("agendamentoPorNFe", true);
					}
				}
				return map;
			}

			return null;
		} else if ((map.getBoolean("blCancelamento") != null)
				&& (map.getBoolean("blCancelamento").equals(Boolean.TRUE))) {
			return storeCancelarAgendamento(map, filialUsuarioLogado);
		} else {
	    // verifico se existe Agendamento de entrega Cancelado relacionado
	    // ao Docto Serviço
			if ( map.getLong("idAgendamentoEntrega") != null ) {
				if (agendamentoEntrega.getAgendamentoDoctoServicos() != null) {
					for (Iterator iter = agendamentoEntrega
							.getAgendamentoDoctoServicos().iterator(); iter
							.hasNext();) {
						AgendamentoDoctoServico agendamentoDoctoServico = (AgendamentoDoctoServico) iter
								.next();

			if (agendamentoDoctoServico != null
								&& !agendamentoDoctoServico
										.getAgendamentoEntrega()
										.getTpSituacaoAgendamento().getValue()
										.equals("C")
								&& !agendamentoDoctoServico
										.getAgendamentoEntrega()
										.getIdAgendamentoEntrega()
										.equals(map
												.getLong("idAgendamentoEntrega"))) {
							throw new BusinessException(
									"LMS-09079",
									new String[] { agendamentoDoctoServico
											.getDoctoServico()
											.getTpDocumentoServico().getValue()
				    + " "
											+ agendamentoDoctoServico
													.getAgendamentoEntrega()
													.getFilial().getSgFilial()
				    + " "
											+ zeroEsquerda(agendamentoDoctoServico
													.getDoctoServico()
													.getNrDoctoServico()
					    .toString()) });
						}
					}
				}
			} else {
				List list = map.getList("documentoServico");
				if (list != null && list.size() > 0) {
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						TypedFlatMap typedFlatMap = (TypedFlatMap) iter.next();

						Long idDoctoServico = null;
						if (typedFlatMap.getLong("idDoctoServico") != null) {
							idDoctoServico = typedFlatMap.getLong("idDoctoServico");
						} else if (typedFlatMap.getLong("doctoServico.idDoctoServico") != null) {
							idDoctoServico = typedFlatMap.getLong("doctoServico.idDoctoServico");
						}

						AgendamentoDoctoServico agendamentoDoctoServico = this.findAgendamentoByIdDoctoServico(idDoctoServico, "A");

						if (agendamentoDoctoServico != null) {
							throw new BusinessException("LMS-09079", new String[] { typedFlatMap.getString("doctoServico.nrDoctoServico") });
						}
					}
				}
			}

			//novo agendamento.
			if (!"TA".equals(tpAgendamento)) {
				map.put("obTentativa", "");
			} else {
				map.put("dtAgendamento", "");
				// CQPRO00026815 - correção para evitar erro de cast
				map.put("turno", new HashMap<String, Object>());
				((Map)map.get("turno")).put("idTurno", "");
				map.put("hrPreferenciaInicial", "");
				map.put("hrPreferenciaFinal", "");
				map.put("blCartao", "N");
			}

			agendamentoEntrega.setIdAgendamentoEntrega( map.getLong("idAgendamentoEntrega") );

			Filial filial = filialService.findById(map.getLong("filial.idFilial"));
			agendamentoEntrega.setFilial( filial );
			agendamentoEntrega.setTpAgendamento(map.getDomainValue("tpAgendamento"));
			agendamentoEntrega.setDhContato(map.getDateTime("dhContato"));
			agendamentoEntrega.setTpSituacaoAgendamento(map.getDomainValue("tpSituacaoAgendamento"));
			agendamentoEntrega.setNmContato(map.getString("nmContato"));
			agendamentoEntrega.setNrTelefone(map.getString("nrTelefone"));

			if (map.getLong("turno.idTurno") != null) {
				Turno turno = new Turno();
				turno.setIdTurno(map.getLong("turno.idTurno"));
				agendamentoEntrega.setTurno(turno);
			}

			Usuario usuarioCriacao = new Usuario();
			usuarioCriacao.setIdUsuario( map.getLong("usuarioByIdUsuarioCriacao.idUsuario") );

			agendamentoEntrega.setUsuarioByIdUsuarioCriacao( usuarioCriacao );
			agendamentoEntrega.setBlCartao( map.getBoolean("blCartao") );
			agendamentoEntrega.setDtAgendamento( map.getYearMonthDay("dtAgendamento") );
			agendamentoEntrega.setHrPreferenciaInicial( map.getTimeOfDay("hrPreferenciaInicial") );
			agendamentoEntrega.setHrPreferenciaFinal(map.getTimeOfDay("hrPreferenciaFinal") );
			agendamentoEntrega.setNrDdd( map.getString("nrDdd") );
			agendamentoEntrega.setNrRamal( map.getString("nrRamal") );
			agendamentoEntrega.setDhCancelamento(map.getDateTime("dhCancelamento") );

			if( map.getLong("motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento") != null ) {
				MotivoAgendamento motivoCancelamento = new MotivoAgendamento();
				motivoCancelamento.setIdMotivoAgendamento(map.getLong("motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento"));
				agendamentoEntrega.setMotivoAgendamentoByIdMotivoCancelamento( motivoCancelamento );
			}

			if (map.getLong("motivoAgendamentoByIdMotivoReagendamento.idMotivoAgendamento") != null	&& !reagendamentoParcial) {
				MotivoAgendamento motivoReagendamento = new MotivoAgendamento();
				motivoReagendamento.setIdMotivoAgendamento(map.getLong("motivoAgendamentoByIdMotivoReagendamento.idMotivoAgendamento"));
				agendamentoEntrega.setMotivoAgendamentoByIdMotivoReagendamento( motivoReagendamento );
			}

			if( map.getLong("usuarioByIdUsuarioCancelamento.idUsuario") != null ) {
				Usuario usuarioCancelamento = new Usuario();
				usuarioCancelamento.setIdUsuario(map.getLong("usuarioByIdUsuarioCancelamento.idUsuario") );
				agendamentoEntrega.setUsuarioByIdUsuarioCancelamento( usuarioCancelamento );
			}

			agendamentoEntrega.setObAgendamentoEntrega( map.getString("obAgendamentoEntrega") );
			agendamentoEntrega.setObCancelamento( map.getString("obCancelamento") );
			agendamentoEntrega.setObTentativa( map.getString("obTentativa") );

			if (agendamentoEntrega.getAgendamentoDoctoServicos() != null && agendamentoEntrega.getAgendamentoDoctoServicos().size() > 0) {
				this.removeByIds(agendamentoEntrega.getAgendamentoDoctoServicos());
			}
			agendamentoEntrega.setDsEmailDestinatario(map.getString("dsEmailDestinatario"));
			agendamentoEntrega.setDsEmailTomador(map.getString("dsEmailTomador"));
			Serializable retorno = agendamentoEntregaService.store(agendamentoEntrega);

			List list = map.getList("documentoServico");
			
			if (list != null && list.size() > 0) {
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					TypedFlatMap typedFlatMap = (TypedFlatMap) iter.next();

					DoctoServico doctoServico = new DoctoServico();
					doctoServico.setIdDoctoServico(typedFlatMap
							.getLong("doctoServico.idDoctoServico"));
					
		    AgendamentoDoctoServico agendamentoDoctoServico = new AgendamentoDoctoServico();
					agendamentoDoctoServico.setAgendamentoEntrega(agendamentoEntrega);
					agendamentoDoctoServico.setDoctoServico(doctoServico);
					agendamentoDoctoServico.setTpSituacao(new DomainValue("A"));

					this.store(agendamentoDoctoServico);

		    incluirEventosRastreabilidadeInternacionalService
			    .generateEventoDocumento(
				    ConstantesSim.EVENTO_AGENDAMENTO,
						doctoServico.getIdDoctoServico(), 
						filialUsuarioLogado.getIdFilial(), 
									typedFlatMap
											.getString("doctoServico.filialByIdFilialOrigem.sgFilial")
					    + " "
					    + StringUtils.leftPad(
													typedFlatMap
															.getString("doctoServico.nrDoctoServico"),
													8, '0'),
									JTDateTimeUtils.getDataHoraAtual(),
									null,
									null,
									typedFlatMap
											.getString("doctoServico.tpDocumentoServico.value"));
					
				}
				agendamentoEntrega.setDhEnvio(JTDateTimeUtils.getDataHoraAtual());
				map.put("dhEnvio", agendamentoEntrega.getDhEnvio());
				retorno = agendamentoEntregaService.store(agendamentoEntrega);
				
				executeSendMailAgendamento((Long)retorno);
			}

	    // LMS-3252
	    if (map.containsKey("nrChaveNfe")) {
		List<TypedFlatMap> nrChaveNfes = map.getList("nrChaveNfe");

		if (nrChaveNfes != null && !nrChaveNfes.isEmpty()) {
		    MonitoramentoCCT monitoramentoCCT;

		    for (TypedFlatMap flatMap : nrChaveNfes) {
			String nfe = flatMap.getString("nrChave");

			Cliente remetente = findClienteRemetente(nfe);

			if ("AT".equals(tpAgendamento) || "AH".equals(tpAgendamento) || "DP".equals(tpAgendamento)) {
			/* Chama rotina de gerar evento “Rotina Gravar Evento(“AG”, AGENDAMENTO_NFE.NR_CHAVE)” E.T. (10.01.01.02 Monitoramento NFe CCT) */
				this.monitoramentoNotasFiscaisCCTService.storeEvento("AG", nfe, remetente, null, null,
						agendamentoEntrega.getObAgendamentoEntrega(), null, SessionUtils.getUsuarioLogado());

			} else 	if ("TA".equals(tpAgendamento)){
				this.monitoramentoNotasFiscaisCCTService.storeEvento("TA", nfe, remetente, null, null,
						agendamentoEntrega.getObAgendamentoEntrega(), null, SessionUtils.getUsuarioLogado());
			}

			monitoramentoCCT = this.monitoramentoNotasFiscaisCCTService.findMonitoramentoCCTByNrChave(nfe);

			AgendamentoMonitCCT agendamentoMonitCCT = new AgendamentoMonitCCT();
			agendamentoMonitCCT.setAgendamentoEntrega(agendamentoEntrega);
			agendamentoMonitCCT.setMonitoramentoCCT(monitoramentoCCT);

			this.agendamentoMonitCCTService.store(agendamentoMonitCCT);
			}
		    map.put("blNFE", "true");
				} else {
		    map.put("blNFE", "false");
		}
			} else {
		map.put("blNFE", "false");
	    }

			agendamentoEntrega.setAgendamentoDoctoServicos(list);
			map.put("idAgendamentoEntrega", ((Long)retorno));

			return map;
		}	
	}

	private void validaDataAgendamento(DateTime dtAgendamento) {
		ParametroGeral parametro= parametroGeralService.findByNomeParametro(DT_MAX_AGENDAMENTO);
		
		if(parametro != null ){
			Pattern p = Pattern.compile("\\d");
			if(parametro.getDsConteudo() != null && p.matcher(parametro.getDsConteudo()).find()){
				int valorParametro = Integer.parseInt(parametro.getDsConteudo());
				
				DateTime dataLimite = JTDateTimeUtils.getDataHoraAtual().plusDays(valorParametro);
				
				if(dtAgendamento.isAfter(dataLimite)){
					throw new BusinessException("LMS-09140",new Object[]{ parametro.getDsConteudo()});
				}
			} else {
				throw new BusinessException("LMS-27051", new Object[]{DT_MAX_AGENDAMENTO});
			} 
		} else {
			throw new BusinessException("LMS-27051", new Object[]{DT_MAX_AGENDAMENTO});
		}
	}

	public TypedFlatMap findEmailsTomadorDestinatario(TypedFlatMap map) {
		
		DevedorDocServ devedorDocServ =devedorDocServService.findDevedorByDoctoServico(map.getLong("idDoctoServico"));
		
		Pessoa pessoaTomador = devedorDocServ.getCliente().getPessoa();
		List<Contato> contatosTomador = contatoService.findContatosByIdPessoaTpContato(pessoaTomador.getIdPessoa(), "AD");
		if (contatosTomador != null  && !contatosTomador.isEmpty()) {
			Contato contatoTomador = contatosTomador.get(0);
			map.put("dsEmailTomador", contatoTomador.getDsEmail());
		}

		Pessoa pessoaDestinatario = devedorDocServ.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa();

		List<Contato> contatosDestinatario = contatoService.findContatosByIdPessoaTpContato(pessoaDestinatario.getIdPessoa(), "AD");
		if (contatosDestinatario != null && !contatosDestinatario.isEmpty()) {
			Contato contatoDestinatario = contatosDestinatario.get(0);
			map.put("dsEmailDestinatario", contatoDestinatario.getDsEmail());
		}

		return map;
	}

	private Cliente findClienteRemetente(String chaveNfe) {
		String cpfCnpj = chaveNfe.substring(6, 20);
		Cliente cliente = this.clienteService.findByNrIdentificacao(cpfCnpj);
		return cliente;
	}
	
	private AgendamentoEntrega clonaAgendamentoReagendamento(AgendamentoEntrega agendamentoEntregaOriginal, 
			List<AgendamentoDoctoServico> agendamentoDoctoServicosReagendados, TypedFlatMap map){
		
		AgendamentoEntrega agendamentoEntregaNovo = new AgendamentoEntrega();
		BeanUtils.copyProperties(agendamentoEntregaOriginal, agendamentoEntregaNovo);
		
		if (agendamentoEntregaNovo.getAgendamentoDoctoServicos() != null) {
			agendamentoEntregaNovo.getAgendamentoDoctoServicos().clear();
		}
		agendamentoEntregaNovo.setAgendamentoDoctoServicos(agendamentoDoctoServicosReagendados);

		agendamentoEntregaNovo.setIdAgendamentoEntrega(null);
		agendamentoEntregaNovo.setReagendamento(null);
		agendamentoEntregaNovo.setDtAgendamento(map.getYearMonthDay("dtAgendamento"));
		
		agendamentoEntregaNovo.setNrTelefone(map.getString("nrTelefone"));
		agendamentoEntregaNovo.setNrDdd(map.getString("nrDdd"));
		agendamentoEntregaNovo.setNrRamal(map.getString("nrRamal"));
		agendamentoEntregaNovo.setNmContato(map.getString("nmContato"));
		
		agendamentoEntregaNovo.setHrPreferenciaInicial( map.getTimeOfDay("hrPreferenciaInicial") );
		agendamentoEntregaNovo.setHrPreferenciaFinal(map.getTimeOfDay("hrPreferenciaFinal") );

		agendamentoEntregaNovo.setBlCartao( map.getBoolean("blCartao") );
		agendamentoEntregaNovo.setObAgendamentoEntrega( map.getString("obAgendamentoEntrega") );

		if ( map.getLong("turno.idTurno") != null) {
			Turno turno = new Turno();
			turno.setIdTurno( map.getLong("turno.idTurno") );
			agendamentoEntregaNovo.setTurno(turno);
		}

		if ( map.getLong("motivo") != null ) {
			MotivoAgendamento motivoAgendamento = new MotivoAgendamento();
			motivoAgendamento.setIdMotivoAgendamento( map.getLong("motivo") );
			agendamentoEntregaOriginal.setMotivoAgendamentoByIdMotivoReagendamento(motivoAgendamento);
		}
		agendamentoEntregaNovo.setTpSituacaoAgendamento(new DomainValue("A"));

		Serializable retorno = agendamentoEntregaService.store(agendamentoEntregaNovo);

		String tpAgendamento = agendamentoEntregaOriginal.getTpAgendamento() != null ? 
				agendamentoEntregaOriginal.getTpAgendamento().getValue() : map.getString("tpAgendamento").trim();
		
		if (agendamentoEntregaNovo.getAgendamentoDoctoServicos() != null && agendamentoEntregaNovo.getAgendamentoDoctoServicos().size() > 0) {
			for (Iterator iter = agendamentoEntregaNovo.getAgendamentoDoctoServicos().iterator(); iter.hasNext();) {
				AgendamentoDoctoServico agendamentoDoctoServico = (AgendamentoDoctoServico) iter.next();
				agendamentoDoctoServico.setAgendamentoEntrega(agendamentoEntregaNovo);

				this.store(agendamentoDoctoServico);

				incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
					ConstantesSim.EVENTO_REAGENDADO, 
					agendamentoDoctoServico.getDoctoServico().getIdDoctoServico(), 
					SessionUtils.getFilialSessao().getIdFilial(), 
					agendamentoDoctoServico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(agendamentoDoctoServico.getDoctoServico().getNrDoctoServico().toString(), 8, '0'), 
					JTDateTimeUtils.getDataHoraAtual(),
					null, 
					null, 
					agendamentoDoctoServico.getDoctoServico().getTpDocumentoServico().getValue(), Boolean.TRUE
				);
				
				}
			}

		return agendamentoEntregaNovo;
	}

	private void desbloqueiaDocto(Long doctoServicoId) {
		DoctoServico doctoServico = doctoServicoService
				.findById(doctoServicoId);
		if (doctoServico.getBlBloqueado() != null
				&& doctoServico.getBlBloqueado()) {
			OcorrenciaPendencia ocorrenciaPendenciaLiberacao = ocorrenciaPendenciaService
					.findByCodigoOcorrencia((short) 204);
			TypedFlatMap typedFlatMap = new TypedFlatMap(); 
			typedFlatMap.put("doctoServico.idDoctoServico",
					doctoServico.getIdDoctoServico());
			typedFlatMap.put("ocorrenciaPendencia.blApreensao",
					ocorrenciaPendenciaLiberacao.getBlApreensao());
			typedFlatMap.put("ocorrenciaPendencia.idOcorrenciaPendencia",
					ocorrenciaPendenciaLiberacao.getIdOcorrenciaPendencia());
			typedFlatMap.put("ocorrenciaPendencia.evento.idEvento",
					ocorrenciaPendenciaLiberacao.getEvento().getIdEvento());
			typedFlatMap.put("ocorrenciaPendencia.tpOcorrencia",
					ocorrenciaPendenciaLiberacao.getTpOcorrencia().getValue());
			ocorrenciaDoctoServicoService
					.executeRegistrarOcorrenciaDoctoServico(typedFlatMap);
		}
	}

	private void bloqueiaDocto(TypedFlatMap typedFlatMap) {
		//lms-173
		OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService
				.findByCodigoOcorrencia((short) 203);
		typedFlatMap.put("ocorrenciaPendencia.blApreensao",
				ocorrenciaPendencia.getBlApreensao());
		typedFlatMap.put("ocorrenciaPendencia.idOcorrenciaPendencia",
				ocorrenciaPendencia.getIdOcorrenciaPendencia());
		typedFlatMap.put("ocorrenciaPendencia.evento.idEvento",
				ocorrenciaPendencia.getEvento().getIdEvento());
		typedFlatMap.put("ocorrenciaPendencia.tpOcorrencia",
				ocorrenciaPendencia.getTpOcorrencia().getValue());
		ocorrenciaDoctoServicoService
				.executeRegistrarOcorrenciaDoctoServico(typedFlatMap);
	}

	private TypedFlatMap storeCancelarAgendamento(TypedFlatMap map,
			Filial filialUsuarioLogado) {
		AgendamentoEntrega agendamentoEntrega = new AgendamentoEntrega();
		agendamentoEntrega = agendamentoEntregaService.findById(map
				.getLong("idAgendamentoEntrega"));
		String tpAgendamento = agendamentoEntrega.getTpAgendamento() != null ? agendamentoEntrega
				.getTpAgendamento().getValue() : map.getString("");

		if ( map.getLong("motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento") == null ) {
			throw new BusinessException("LMS-09065");
		} else {
			MotivoAgendamento motivoCancelamento = (MotivoAgendamento) getDao()
					.getAdsmHibernateTemplate()
					.load(MotivoAgendamento.class,
							map.getLong("motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento"));
			agendamentoEntrega
					.setMotivoAgendamentoByIdMotivoCancelamento(motivoCancelamento);
		}

		if (agendamentoEntrega.getAgendamentoDoctoServicos() != null
				&& agendamentoEntrega.getAgendamentoDoctoServicos().size() > 0) {
			for (Iterator iter = agendamentoEntrega
					.getAgendamentoDoctoServicos().iterator(); iter.hasNext();) {
				AgendamentoDoctoServico agendamentoDoctoServico = (AgendamentoDoctoServico) iter
						.next();
				Long idDoctoServico = agendamentoDoctoServico.getDoctoServico()
						.getIdDoctoServico();
				if (this.findManifestoSemOcorrenciaLancada(idDoctoServico)
						.booleanValue()) {
					throw new BusinessException("LMS-09064");
				}		
			}
		}

		agendamentoEntrega.setTpSituacaoAgendamento(new DomainValue("C"));
		agendamentoEntrega.setObCancelamento(map.getString("obCancelamento"));
		agendamentoEntrega
				.setDhCancelamento(JTDateTimeUtils.getDataHoraAtual());
		agendamentoEntrega.setUsuarioByIdUsuarioCancelamento(SessionUtils
				.getUsuarioLogado());
		agendamentoEntrega.setDhFechamento(JTDateTimeUtils.getDataHoraAtual());
		agendamentoEntregaService.store(agendamentoEntrega);

		if (agendamentoEntrega.getAgendamentoDoctoServicos() != null
				&& agendamentoEntrega.getAgendamentoDoctoServicos().size() > 0) {
			for (Iterator iter = agendamentoEntrega
					.getAgendamentoDoctoServicos().iterator(); iter.hasNext();) {
				AgendamentoDoctoServico agendamentoDoctoServico = (AgendamentoDoctoServico) iter
						.next();

				Long idDoctoServico = agendamentoDoctoServico.getDoctoServico()
						.getIdDoctoServico();
				if (!incluirEventosRastreabilidadeInternacionalService
						.generateEventoDocumento(
						ConstantesSim.EVENTO_CANCELAMENTO, 
						idDoctoServico, 
						filialUsuarioLogado.getIdFilial(), 
								agendamentoDoctoServico.getDoctoServico()
										.getFilialByIdFilialOrigem()
										.getSgFilial()
										+ " "
										+ StringUtils.leftPad(
												agendamentoDoctoServico
														.getDoctoServico()
														.getNrDoctoServico()
														.toString(), 8, '0'),
						JTDateTimeUtils.getDataHoraAtual(),
						null, 
						null, 
								agendamentoDoctoServico.getDoctoServico()
										.getTpDocumentoServico().getValue(),
								Boolean.TRUE).booleanValue()) {
					incluirEventosRastreabilidadeInternacionalService
							.generateEventoDocumento(
						ConstantesSim.EVENTO_CANCELAMENTO_AGENDAMENTO,
						idDoctoServico,
						filialUsuarioLogado.getIdFilial(),
									agendamentoDoctoServico.getDoctoServico()
											.getFilialByIdFilialOrigem()
											.getSgFilial()
											+ " "
											+ StringUtils
													.leftPad(
															agendamentoDoctoServico
																	.getDoctoServico()
																	.getNrDoctoServico()
																	.toString(),
															8, '0'),
						JTDateTimeUtils.getDataHoraAtual(),
						null,
									agendamentoDoctoServico.getDoctoServico()
											.getTpDocumentoServico().getValue(),
									null);
				}
		}		
			
		}
		
		//Se for agendamento por NF-e, para cada Nota Fiscal do agendamento:
		List<String> nrChaveNfes = agendamentoEntregaService.findNotasMonitoramentoCCTByIdAgendamentoEntrega(agendamentoEntrega.getIdAgendamentoEntrega());

		if (nrChaveNfes != null && !nrChaveNfes.isEmpty() && nrChaveNfes.size() > 0) {
			
		    for (String chave : nrChaveNfes) {
		    	Cliente remetente = findClienteRemetente(chave);
				this.monitoramentoNotasFiscaisCCTService.storeEvento("AC", chave, remetente, null, null, map.getString("obCancelamento"), null, SessionUtils.getUsuarioLogado());
		    }
		}
		map.put("tpSituacaoAgendamento", agendamentoEntrega
				.getTpSituacaoAgendamento().getValue());
		return map;
	}

	private List verificaExistenciaPce(AgendamentoEntrega agendamentoEntrega) {
		if( agendamentoEntrega != null
			&& agendamentoEntrega.getAgendamentoDoctoServicos() != null
				&& agendamentoEntrega.getAgendamentoDoctoServicos().size() > 0) {
			List listRetorno = new ArrayList();

			boolean entrou = false;
			for (Iterator iter = agendamentoEntrega
					.getAgendamentoDoctoServicos().iterator(); iter.hasNext();) {
				Map mapRetorno = new HashMap();
				AgendamentoDoctoServico agendamentoDoctoServico = (AgendamentoDoctoServico) iter
						.next();

				Long idClienteDestinatario = null;
				if (agendamentoDoctoServico.getDoctoServico()
						.getClienteByIdClienteDestinatario() != null) {
					idClienteDestinatario = agendamentoDoctoServico
							.getDoctoServico()
							.getClienteByIdClienteDestinatario().getIdCliente();
				}

				Long idClienteRemetente = null;
				if (agendamentoDoctoServico.getDoctoServico()
						.getClienteByIdClienteRemetente() != null) {
					idClienteRemetente = agendamentoDoctoServico
							.getDoctoServico().getClienteByIdClienteRemetente()
							.getIdCliente();
				}

				Long idRetornoPCEDestinatario = versaoDescritivoPceService
						.validateifExistPceByCriteria(idClienteDestinatario,
								ConstantesEventosPCE.CD_PROCESSO_AGENDAMENTO,
								ConstantesEventosPCE.CD_EVENTO_AGENDAMENTO,
								ConstantesEventosPCE.CD_OCORRENCIA_AGENDAMENTO);
				Long idRetornoPCERemetente = versaoDescritivoPceService
						.validateifExistPceByCriteria(idClienteRemetente,
								ConstantesEventosPCE.CD_PROCESSO_AGENDAMENTO,
								ConstantesEventosPCE.CD_EVENTO_AGENDAMENTO,
								ConstantesEventosPCE.CD_OCORRENCIA_AGENDAMENTO);

				mapRetorno.put("idRetornoPCEDestinatario",
						idRetornoPCEDestinatario);
				mapRetorno.put("idRetornoPCERemetente", idRetornoPCERemetente);
				mapRetorno.put("idDoctoServico", agendamentoDoctoServico
						.getDoctoServico().getIdDoctoServico());
				mapRetorno.put("nrDoctoServico", agendamentoDoctoServico
						.getDoctoServico().getNrDoctoServico());
				mapRetorno.put("tpDocumentoServico", agendamentoDoctoServico
						.getDoctoServico().getTpDocumentoServico());
				if (agendamentoDoctoServico.getDoctoServico()
						.getFilialByIdFilialOrigem() != null) {
					mapRetorno.put("filialByIdFilialOrigem.sgFilial",
							agendamentoDoctoServico.getDoctoServico()
									.getFilialByIdFilialOrigem().getSgFilial());
				}
				if (agendamentoDoctoServico.getDoctoServico()
						.getFilialDestinoOperacional() != null) {
					mapRetorno.put("idFilialDestinoOperacional",
							agendamentoDoctoServico.getDoctoServico()
									.getFilialDestinoOperacional()
									.getIdFilial());
				}
				if (agendamentoDoctoServico.getDoctoServico()
						.getFilialDestinoOperacional() != null && !entrou) {
					Filial filialDestino = this.filialService
							.findById(agendamentoDoctoServico.getDoctoServico()
									.getFilialDestinoOperacional()
									.getIdFilial());
					mapRetorno.put("sgFilialDestinoOperacional",
							filialDestino.getSgFilial());
					mapRetorno.put("nmFilialDestinoOperacional", filialDestino
							.getPessoa().getNmFantasia());
					entrou = true;
				}
				listRetorno.add(mapRetorno);
			}
			return listRetorno;
		}
		return null;
	}
	
	public void fechaAgendamentosDoctoServico(
			List<DoctoServico> documentosManifesto) {
		for (DoctoServico doc : documentosManifesto){
			AgendamentoDoctoServico agendamento = findAgendamentoByIdDoctoServico(
					doc.getIdDoctoServico(), "A");
			if (agendamento != null){
				List<AgendamentoDoctoServico> agendamentos = agendamento
						.getAgendamentoEntrega().getAgendamentoDoctoServicos();
			
				for (AgendamentoDoctoServico agendamentoDoctoServico:agendamentos){
					if (!documentosManifesto.contains(agendamentoDoctoServico
							.getDoctoServico())) {
						throw new BusinessException(
								"LMS-09125",
								new Object[] { agendamentoDoctoServico
										.getDoctoServico()
										.getFilialByIdFilialOrigem()
										.getSgFilial()
										+ " "
										+ agendamentoDoctoServico
												.getDoctoServico()
												.getNrDoctoServico().toString() });
					}
				}
				
				agendamento.getAgendamentoEntrega().setTpSituacaoAgendamento(new DomainValue("F"));
				agendamento.getAgendamentoEntrega().setDhFechamento(JTDateTimeUtils.yearMonthDayToDateTime(agendamento.getAgendamentoEntrega().getDtAgendamento()));
				//LMS-4187
				calcularDiasUteisBloqueioAgendamentoService.executeCalcularDiasUteisBloqueioAgendamento(doc, Boolean.TRUE);
				agendamentoEntregaService.store(agendamento.getAgendamentoEntrega());
			}
		}
	    
    }
	
	/**
	 * Método que retorna um List de AgendamentoDoctoServico usando como filtr o
	 * ID do DoctoServico
	 * 
	 * @param idDoctoServico
	 * @return List
	 */
	public List findAgendamentoByIdDoctoServico(Long idDoctoServico) {
		return this.getAgendamentoDoctoServicoDAO()
				.findAgendamentoByIdDoctoServico(idDoctoServico);
	}

	/**
	 * Método que retorna um List de AgendamentoDoctoServico Ativos e Abertos do
	 * DoctoServico
	 * 
     * @author André Valadas
     * 
     * @param idDoctoServico
     * @return
     */
	public List<AgendamentoDoctoServico> findAgendamentosAtivos(
			final Long idDoctoServico) {
		return getAgendamentoDoctoServicoDAO().findAgendamentosAtivos(
				idDoctoServico);
    }
	
	/**
	 * Método que retorna um AgendamentoDoctoServico usando como filtro ID do
	 * DoctoServico e tpSituacao
	 * 
	 * @param idDoctoServico
	 * @param tpSituacao
	 * @return AgendamentoDoctoServico
	 */
	public AgendamentoDoctoServico findAgendamentoByIdDoctoServico(
			Long idDoctoServico, String tpSituacao) {
		return this.getAgendamentoDoctoServicoDAO()
				.findAgendamentoByIdDoctoServico(idDoctoServico, tpSituacao);
	}

	public AgendamentoDoctoServico findAgendamentoByIdDoctoServicoTpSituacao(
			Long idDoctoServico, String tpSituacao) {
		return this.getAgendamentoDoctoServicoDAO()
				.findAgendamentoByIdDoctoServicoTpSituacao(idDoctoServico, tpSituacao);
	}
	
	
	

	public List<AgendamentoDoctoServico> findAgendamentoByIdDoctoServicoAndTpSituacao(
			Long idDoctoServico, String... tpSituacoes) {
		return this.getAgendamentoDoctoServicoDAO()
				.findAgendamentoByIdDoctoServicoAndTpSituacao(idDoctoServico,
						tpSituacoes);
	}
	
	public ResultSetPage findPaginatedAgendamentoEntregaDoctoServico(
			TypedFlatMap criteria) {
		ResultSetPage rsp = getAgendamentoDoctoServicoDAO()
				.findPaginatedAgendamentoEntregaDoctoServico(criteria,
						FindDefinition.createFindDefinition(criteria));

		List newList = new ArrayList();

		TypedFlatMap tpDoctoServico = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues(
				"DM_TIPO_DOCUMENTO_SERVICO").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpDoctoServico.put(domain.getValue(),domain);
		}

		TypedFlatMap tpSituacaoAgenda = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues(
				"DM_SITUACAO_AGENDA").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpSituacaoAgenda.put(domain.getValue(),domain);
		}

		TypedFlatMap tpIdentificacao = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues(
				"DM_TIPO_IDENTIFICACAO").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpIdentificacao.put(domain.getValue(),domain);
		}

		TypedFlatMap tpAgendamento = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues(
				"DM_TIPO_AGENDAMENTO").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpAgendamento.put(domain.getValue(),domain);
		}

		for(Iterator i = rsp.getList().iterator(); i.hasNext();) {
			Object[] projections = (Object[])i.next();
			TypedFlatMap result = new TypedFlatMap();

			result.put("sgFilial",projections[0]);
			result.put("nmFantasia",projections[1]);
			result.put("dsTipoServico",projections[2]);
			result.put("sgFilialOrigem",projections[3]);
			result.put("nrDoctoServico",projections[4]);
			result.put("tpDocumentoServico", projections[5] == null ? "" : tpDoctoServico.getDomainValue((String)projections[5]).getDescription());
			result.put("sgFilialDestino",projections[6]);
			result.put("nmFantasiaDestino",projections[7]);
			result.put("tpIdentificacaoRemetente", projections[9] == null ? "" : tpIdentificacao.getDomainValue((String)projections[9]).getDescription());
			result.put("nrIdentificacaoRemetente",FormatUtils.formatIdentificacao((String)projections[9],(String)projections[8]));
			result.put("remetente",projections[10]);
			result.put("tpIdentificacaoDestinatario", projections[12] == null ? "" : tpIdentificacao.getDomainValue((String)projections[12]).getDescription());
			result.put("nrIdentificacaoDestinatario",FormatUtils.formatIdentificacao((String)projections[12],(String)projections[11]));
			result.put("destinatario",projections[13]);
			result.put("tpAgendamento", projections[14] == null ? "" : tpAgendamento.getDomainValue((String)projections[14]).getDescription());
			result.put("dtAgendamento",projections[15]);
			result.put("horarioInicial",projections[16]);
			result.put("horarioFinal",projections[17]);
			result.put("tpSituacaoAgendamento", projections[18] == null ? "" : tpSituacaoAgenda.getDomainValue((String)projections[18]).getDescription());
			result.put("dsTurno",projections[19]);
			result.put("agendadoPor",projections[20]);
			result.put("nrNotaFiscal",projections[21]);
			result.put("idAgendamentoEntrega",projections[22]);
			result.put("dhContato",projections[23]);
			result.put("nrChave",projections[24]);

			newList.add(result);
		}
		rsp.setList(newList);
		
		return rsp;
	}

	public Integer getRowCountAgendamentoEntregaDoctoServico(
			TypedFlatMap criteria) {
		return getAgendamentoDoctoServicoDAO()
				.getRowCountAgendamentoEntregaDoctoServico(criteria);
	}
	
	public ResultSetPage findPaginatedConsultaAgendamentoEntregaDoctoServico(
			TypedFlatMap criteria) {
		ResultSetPage rsp = getAgendamentoDoctoServicoDAO()
				.findPaginatedConsultaAgendamentoEntregaDoctoServico(criteria,
						FindDefinition.createFindDefinition(criteria));

		List newList = new ArrayList();

		TypedFlatMap tpDoctoServico = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues(
				"DM_TIPO_DOCUMENTO_SERVICO").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpDoctoServico.put(domain.getValue(),domain);
		}

		TypedFlatMap tpSituacaoAgenda = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues(
				"DM_SITUACAO_AGENDA").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpSituacaoAgenda.put(domain.getValue(),domain);
		}

		TypedFlatMap tpIdentificacao = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues(
				"DM_TIPO_IDENTIFICACAO").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpIdentificacao.put(domain.getValue(),domain);
		}

		TypedFlatMap tpAgendamento = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues(
				"DM_TIPO_AGENDAMENTO").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpAgendamento.put(domain.getValue(),domain);
		}

		TypedFlatMap tpDestinatario = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues("DM_TIPO_PESSOA")
				.iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpDestinatario.put(domain.getValue(),domain);
		}

		for(Iterator i = rsp.getList().iterator(); i.hasNext();) {
			Object[] projections = (Object[])i.next();
			TypedFlatMap result = new TypedFlatMap();

			result.put("sgFilial",projections[0]);
			result.put("nmFantasia",projections[1]);
			result.put("dsTipoServico",projections[2]);
			result.put("sgFilialOrigem",projections[3]);
			result.put("nrDoctoServico",projections[4]);

			result.put("tpDocumentoServico",
					tpDoctoServico.getDomainValue((String) projections[5])
							.getDescription());

			result.put("sgFilialDestino",projections[6]);
			result.put("nmFantasiaDestino",projections[7]);

			result.put("tpIdentificacaoRemetente", tpIdentificacao
					.getDomainValue((String) projections[9]).getDescription());
			result.put("nrIdentificacaoRemetente", FormatUtils
					.formatIdentificacao((String) projections[9],
							(String) projections[8]));
			result.put("remetente",projections[10]);

			result.put("tpIdentificacaoDestinatario", tpIdentificacao
					.getDomainValue((String) projections[12]).getDescription());
			result.put("nrIdentificacaoDestinatario", FormatUtils
					.formatIdentificacao((String) projections[12],
							(String) projections[11]));
			result.put("destinatario",projections[13]);

			result.put("tpAgendamento",
					tpAgendamento.getDomainValue((String) projections[14])
							.getDescription());

			result.put("dtAgendamento", projections[15]);
			result.put("horarioInicial", projections[16]);
			result.put("horarioFinal", projections[17]);
			result.put("tpSituacaoAgendamento", tpSituacaoAgenda
					.getDomainValue((String) projections[18]).getDescription());
			result.put("dsTurno", projections[19]);
			result.put("agendadoPor", projections[20]);
			result.put("nrNotaFiscal", projections[21]);
			result.put("idAgendamentoEntrega", projections[22]);

			result.put("dtPrevEntrega",projections[23]);
			result.put("tpDestinatario",
					tpDestinatario.getDomainValue((String) projections[24])
							.getDescription());
			result.put("obAgendamentoEntrega",projections[25]);
			result.put("dhBaixa",projections[26]);
			result.put("semiReboqueNrFrota",projections[27]);
			result.put("semiReboqueNrIdentificador",projections[28]);
			result.put("meioTransporteNrFrota",projections[29]);
			result.put("meioTransporteNrIdentificador",projections[30]);
			result.put("idAgendamentoDoctoServico",projections[31]);
			result.put("idDoctoServico", projections[32]);

			result.put("dataEntrega", projections[33]);
			result.put("horaEntrega", projections[34]);

			result.put("blCartao", projections[35]);
			result.put("controleCarga", projections[36]);
			result.put("sgFilialControleCarga", projections[37]);
			result.put("manifestoEntrega", projections[38]);
			result.put("sgFilialManifestoEntrega", projections[39]);

			newList.add(result);
		}
		rsp.setList(newList);
		return rsp;
	}

	public Integer getRowCountConsultaAgendamentoEntregaDoctoServico(
			TypedFlatMap criteria) {
		return getAgendamentoDoctoServicoDAO()
				.getRowCountConsultaAgendamentoEntregaDoctoServico(criteria);
	}	

	public List findPaginatedDoctoServico(TypedFlatMap criteria) {
		getIdServicoAdicional(criteria);

		List rsp = this.getAgendamentoDoctoServicoDAO()
				.findPaginatedDoctoServico(criteria);

		// Verifico se existe filialDestino diferentes ...	
		if (rsp != null && rsp.size() > 0) {
			List newList = new ArrayList();

			TypedFlatMap tpDoctoServico = new TypedFlatMap();
			for (Iterator i = domainValueService.findDomainValues(
					"DM_TIPO_DOCUMENTO_SERVICO").iterator(); i.hasNext();) {
				DomainValue domain = (DomainValue)i.next();
				tpDoctoServico.put(domain.getValue(),domain.getDescription());
			}		

			TypedFlatMap tpIdentificacao = new TypedFlatMap();
			for (Iterator i = domainValueService.findDomainValues(
					"DM_TIPO_IDENTIFICACAO").iterator(); i.hasNext();) {
				DomainValue domain = (DomainValue)i.next();
				tpIdentificacao.put(domain.getValue(), domain.getDescription()
						.toString());
			}			

			for (Iterator iter = rsp.iterator(); iter.hasNext();) {
				Object[] projections = (Object[])iter.next();
				TypedFlatMap result = new TypedFlatMap();

				result.put("idDoctoServico",projections[0]);
				result.put("doctoServico.tpDocumentoServico",
						tpDoctoServico.get((String) projections[1]));
				result.put("doctoServico.tpDocumentoServicoValue",
						(String) projections[1]);
				result.put("doctoServico.filialByIdFilialOrigem.sgFilial",
						projections[2]);
				result.put("doctoServico.nrDoctoServico",projections[3]);

				if (projections[4] != null) {
					result.put(
							"doctoServico.clienteByIdClienteRemetente.pessoa.tpIdentificacao",
							tpIdentificacao.get((String) projections[4]));
					result.put(
							"doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado",
							FormatUtils.formatIdentificacao(
									(String) tpIdentificacao
											.get((String) projections[4]),
									(String) projections[5]));
				}
				result.put(
						"doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa",
						projections[6]);

				if (projections[7] != null) {
					result.put(
							"doctoServico.clienteByIdClienteDestinatario.pessoa.tpIdentificacao",
							tpIdentificacao.get((String) projections[7]));
					result.put(
							"doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado",
							FormatUtils.formatIdentificacao(
									(String) tpIdentificacao
											.get((String) projections[7]),
									(String) projections[8]));
				}
				result.put(
						"doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa",
						projections[9]);
				result.put("doctoServico.dtPrevEntrega",projections[10]);
				result.put("doctoServico.idFilialDestino",projections[11]);

				result.put("idClienteRemetente",projections[12]);
				result.put("idClienteDestinatario",projections[13]);

				result.put("idFilialDestinoOperacional",projections[14]);

				result.put("doctoServico.filialDestinoOperacional.sgFilial",
						projections[15]);
				result.put(
						"doctoServico.filialDestinoOperacional.pessoa.nmFantasia",
						projections[16]);
				result.put("nrNotaFiscal",projections[17]);

				result.put("lome.idLocalizacaoMercadoria",projections[18]);
				result.put("lome.dsLocalizacaoMercadoria",projections[19]);

				newList.add(result);
			}
			rsp = newList;
		}
		return rsp;
	}

	private void getIdServicoAdicional(TypedFlatMap criteria) {
		// Veririficar se existe uma parcela de preço com código de agendamento
		// (tabela PARCELA_PRECO)
		// onde código seja de agendamento (CD_PARCELA_PRECO =
		// 'IDAGENDAMENTOENTREGA').
		// Se houver: Guardar código do serviço adicional na variável ADIC_SERV
		// Caso Contrário: Mostrar mensagem LMS-09066 e abortar a operação
		Map map = parcelaPrecoService
				.findParcelaByCdParcelaPreco(ConstantesExpedicao.CD_AGENDAMENTO_ENTREGA); // "IDAgendamentoEntrega"
		if (map != null && map.get("idServicoAdicional") != null) {
			criteria.put("ADIC_SERV", (Long)map.get("idServicoAdicional"));
		} else {
			throw new BusinessException("LMS-09066");
		}
	}

	public Boolean findManifestoSemOcorrenciaLancada(Long idDoctoServico) {
		return this.getAgendamentoDoctoServicoDAO()
				.findManifestoSemOcorrenciaLancada(idDoctoServico);
	}

	private String zeroEsquerda(String nrDoctoServico) {
		if (nrDoctoServico.length() < 8) {
			int j = 0;
			int tamanho = 8 - nrDoctoServico.length();
			for (j = 0; j < tamanho; j++) {
				nrDoctoServico = "0" + nrDoctoServico;
			}
		}
		return nrDoctoServico;
	}	
	
	public List findListDoctoServicoByIdAgendamentoEntrega(TypedFlatMap criteria) {
		List rsp = this.getAgendamentoDoctoServicoDAO()
				.findListDoctoServicoByIdAgendamentoEntrega(criteria);
		if (rsp != null && rsp.size() > 0) {
			List newList = new ArrayList();
			TypedFlatMap tpDoctoServico = new TypedFlatMap();
			for (Iterator i = domainValueService.findDomainValues(
					"DM_TIPO_DOCUMENTO_SERVICO").iterator(); i.hasNext();) {
				DomainValue domain = (DomainValue)i.next();
				tpDoctoServico.put(domain.getValue(),domain.getDescription());
			}		

			for (Iterator iter = rsp.iterator(); iter.hasNext();) {
				Object[] projections = (Object[])iter.next();
				TypedFlatMap result = new TypedFlatMap();

				result.put("idDoctoServico",projections[0]);
				result.put("tpDocumentoServico",
						tpDoctoServico.get((String) projections[1]));
				result.put("sgFilialOrigem",projections[2]);
				result.put("nrDoctoServico",projections[3]);
				result.put("nrNotaFiscal",projections[4]);
				result.put("nrControleCarga",projections[5]);
				result.put("nrManifestoEntrega",projections[6]);
				result.put("dhOcorrencia",projections[7]);
				result.put("filial_origem_coca_sgfilial",projections[8]);
				result.put("filial_maen_sgfilial",projections[9]);

				newList.add(result);
			}
			rsp = newList;
		}
		return rsp;
	}

	/**
	 * Busca um Agendamento de Documento de Serviço a partir dos parâmtetros
	 * informados. Método utilizado pela Integração
	 * 
	 * @author Felipe Ferreira
	 * 
	 * @param idDoctoServico
	 *            Identificador do Documento de Serviço.
	 * @param tpAgendamento
	 *            Tipo de Agendamento
	 * @param dhContato
	 *            Data/hora do contato. Este parâmetro é opcional.
	 * @return uma instância de AgendamentoDoctoServico caso encontrado. Senão,
	 *         retora null.
	 */
	public AgendamentoDoctoServico findAgendamentoDoctoServico(
			Long idDoctoServico, String tpAgendamento, DateTime dhContato) {
		return getAgendamentoDoctoServicoDAO().findAgendamentoDoctoServico(
				idDoctoServico, tpAgendamento, dhContato);
	}
	
	public List<AgendamentoDoctoServico> findAgendamentoByIdDoctoServicoJoinFilial(
			Long idDoctoServico) {
		return this.getAgendamentoDoctoServicoDAO()
				.findAgendamentoByIdDoctoServicoJoinFilial(idDoctoServico);
	 }
	
	public List<AgendamentoDoctoServico> findNaoCanceladosByIdDoctoServico(Long idDoctoServico) {
		return getAgendamentoDoctoServicoDAO().findNaoCanceladosByIdDoctoServico(idDoctoServico);
	}

	
	public void executeSendMailAgendamento(Long idAgendamentoEntrega) {
		executeSendMailAgendamento(idAgendamentoEntrega, Boolean.FALSE);
	}
	
	public void executeSendMailAgendamento(Long idAgendamentoEntrega, Boolean isReagendamento) {
		AgendamentoEntrega agendamentoEntrega = agendamentoEntregaService.findByIdDefault(idAgendamentoEntrega);
				
		String doctos = "";
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("agendamentoEntrega.idAgendamentoEntrega", idAgendamentoEntrega);
		List<AgendamentoDoctoServico> agendamentoDoctos = find(criteria);
		List<NotaFiscalConhecimento> notasFiscaisCtrc = new ArrayList<NotaFiscalConhecimento>();
		List<DoctoServico> doctosServico = new ArrayList<DoctoServico>();
		
		if(agendamentoDoctos == null) {			
			throw new BusinessException("LMS-04506");
		}
		
		for(AgendamentoDoctoServico agendamentoDocto : agendamentoDoctos) {
			Long idCtrc = agendamentoDocto.getDoctoServico().getIdDoctoServico();
			DoctoServico doctoServico = doctoServicoService.findById(idCtrc);			
			Long nrCtrc = doctoServico.getNrDoctoServico();
			String sgFilial = doctoServico.getFilialByIdFilialOrigem().getSgFilial();
			doctos += sgFilial + " " + FormatUtils.completaDados(nrCtrc, "0", 9, 0, true) + " ";
			doctosServico.add(doctoServico);
			notasFiscaisCtrc.addAll(notaFiscalConhecimentoService.findByConhecimento(idCtrc));
		}
		
		/* Segundo o analista, sempre haverá um conhecimento vinculado */
		String valorFormatado = doctosServico.get(0).getMoeda().getSgMoeda() + " " + doctosServico.get(0).getMoeda().getDsSimbolo() + " ";
		Pessoa pessoaDestinatario = doctosServico.get(0).getClienteByIdClienteDestinatario().getPessoa();
		String destinatario = pessoaDestinatario.getNmPessoa();
		Municipio municipioPessoaDestinatario = pessoaDestinatario.getEnderecoPessoa().getMunicipio();
		String municipio = municipioPessoaDestinatario.getNmMunicipio();
		String uf = municipioPessoaDestinatario.getUnidadeFederativa().getSgUnidadeFederativa();
		Filial filialDestino = doctosServico.get(0).getFilialByIdFilialDestino();
		EnderecoPessoa endFilialDestino = filialDestino.getPessoa().getEnderecoPessoa();
		String filialDepositaria = filialDestino.getSgFilial();
		String telefoneFilialDepositaria = getTelefoneEndereco(endFilialDestino, "FO");
		String faxFilialDepositaria = getTelefoneEndereco(endFilialDestino, "FF");
		
		String notas = "";
		BigDecimal valor = new BigDecimal(0l);
		BigDecimal peso = new BigDecimal(0l);
		Integer qtd = 0;
		for(NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisCtrc) {
			notas += FormatUtils.completaDados(notaFiscalConhecimento.getNrNotaFiscal(), "0", 9, 0, true) + " ";
			peso = peso.add(notaFiscalConhecimento.getPsMercadoria());
			valor = valor.add(notaFiscalConhecimento.getVlTotal());
			qtd += notaFiscalConhecimento.getQtVolumes();
		}
		
		valorFormatado += FormatUtils.formatDecimal("#,##0.00", valor, true);
		String pesoFormatado = FormatUtils.formatPeso(peso, true);
		
		final String dsRemetente = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		String tmpAssunto = configuracoesFacade.getMensagem("comunicacaoAgendamentoEntrega");
		if(isReagendamento) {
			tmpAssunto = configuracoesFacade.getMensagem("comunicacaoReagendamentoEntrega");
		}
		final String dsAssunto = tmpAssunto;
		
		final String dsEmailDestinatario = agendamentoEntrega.getDsEmailDestinatario();
		String dsEmailTomador = agendamentoEntrega.getDsEmailTomador();	
		final String[] dsEmailsCopia = {dsEmailTomador, SessionUtils.getUsuarioLogado().getDsEmail()};						
				
		DateTimeFormatter fmt = DateTimeFormat
				.forPattern("EEEE, dd 'de' MMMM 'de' yyyy HH:mm ZZ")
				.withLocale(VarcharI18n.DEFAULT_LOCALE);

		String dhEnvio = agendamentoEntrega.getDhEnvio().toString(fmt);
		
		String decursoPrazo = configuracoesFacade.getValorParametro("DEC_PRAZO").toString();
		
		String turno = "&nbsp;";
		if(agendamentoEntrega.getTurno() != null) {
			Turno turnoOb = turnoService.findById(agendamentoEntrega.getTurno().getIdTurno());
			turno = turnoOb.getDsTurno();
		}
		
		String dtAgendamento = "";
		if(agendamentoEntrega.getDtAgendamento() != null) {		
			dtAgendamento = JTFormatUtils.format(agendamentoEntrega.getDtAgendamento());
		}
		
		String contato = agendamentoEntrega.getNmContato();
		
		String prefInicial = "";
		String prefFinal = "";
		if(agendamentoEntrega.getHrPreferenciaInicial() != null) {
			prefInicial = agendamentoEntrega.getHrPreferenciaInicial().toString(DateTimeFormat.forPattern("HH:mm"));
		}
		if(agendamentoEntrega.getHrPreferenciaFinal() != null) {
			prefFinal = agendamentoEntrega.getHrPreferenciaFinal().toString(DateTimeFormat.forPattern("HH:mm"));
		}
		
		String telefoneFormatado = FormatUtils.formatTelefone(agendamentoEntrega.getNrTelefone(), agendamentoEntrega.getNrDdd(), "");
		String ramal = (agendamentoEntrega.getNrRamal() == null || "".equals(agendamentoEntrega.getNrRamal()) ? 
				"&nbsp;" : agendamentoEntrega.getNrRamal());
		String observacoes = (agendamentoEntrega.getObAgendamentoEntrega() == null || "".equals(agendamentoEntrega.getObAgendamentoEntrega()) ? 
				"&nbsp;" : agendamentoEntrega.getObAgendamentoEntrega());
				
		String nmEmpresa = SessionUtils.getFilialSessao().getPessoa().getNmPessoa();
		String nmUsuario = SessionUtils.getUsuarioLogado().getNmUsuario();		
		EnderecoPessoa endFilialUsuario = SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa();
		Municipio municipioFilialUsuario = endFilialUsuario.getMunicipio();
		String telefoneUsuario = getTelefoneEndereco(endFilialUsuario, "FO");
			
		endFilialUsuario = enderecoPessoaService.findById(endFilialUsuario.getIdEnderecoPessoa());
		String enderecoFilialFormatado = FormatUtils.formatEnderecoPessoa(
				endFilialUsuario.getTipoLogradouro().getDsTipoLogradouro().getValue(), 
				endFilialUsuario.getDsEndereco(), endFilialUsuario.getNrEndereco(), endFilialUsuario.getDsComplemento(), 
				endFilialUsuario.getDsBairro(), municipioFilialUsuario.getNmMunicipio(), 
				municipioFilialUsuario.getUnidadeFederativa().getSgUnidadeFederativa());
				
		if(endFilialUsuario.getNrCep() != null && !"".equals(endFilialUsuario.getNrCep())) {
			enderecoFilialFormatado += " - CEP " + FormatUtils.formatCep("BRA", endFilialUsuario.getNrCep());	
		}
		
		if(telefoneUsuario != null && !"".equals(telefoneUsuario)) {
			enderecoFilialFormatado += " - " + configuracoesFacade.getMensagem("telefone") + " " + telefoneUsuario;
		}
		
		StringBuilder html = new StringBuilder();
		html.append(configuracoesFacade.getMensagem("MAIL_AGENDAMENTO_HEADER"));
		
		html.append(configuracoesFacade
				.getMensagem("MAIL_AGENDAMENTO_SESSAO_1", new Object[]{
						dhEnvio, dsEmailDestinatario, dsEmailTomador}));
		
		html.append(configuracoesFacade.getMensagem("MAIL_AGENDAMENTO_SESSAO_2", new Object[]{
			notas,
			valorFormatado,
			pesoFormatado,
			qtd,
			doctos,
			destinatario,
			municipio,
			uf,
			turno
		}));
		
		html.append(configuracoesFacade.getMensagem("MAIL_AGENDAMENTO_SESSAO_3", new Object[]{
			dtAgendamento,
			contato,
			prefInicial,
			prefFinal,
			telefoneFormatado,
			ramal,
			filialDepositaria,
			telefoneFilialDepositaria,			
			faxFilialDepositaria,
			observacoes
		}));

		String texto = configuracoesFacade.getMensagem("agendadas");
		if (isReagendamento) {
			texto = configuracoesFacade.getMensagem("reagendadas");
		}
		html.append(configuracoesFacade.getMensagem(
				"MAIL_AGENDAMENTO_SESSAO_4", new Object[] { texto }));

		html.append(configuracoesFacade
				.getMensagem("MAIL_AGENDAMENTO_SESSAO_4_1"));
		if (isReagendamento) {
			html.append(configuracoesFacade
					.getMensagem("MAIL_AGENDAMENTO_SESSAO_4_2"));
		}
		html.append(configuracoesFacade
				.getMensagem("MAIL_AGENDAMENTO_SESSAO_4_3"));
		html.append(configuracoesFacade
				.getMensagem("MAIL_AGENDAMENTO_SESSAO_4_4"));
		html.append(configuracoesFacade.getMensagem(
				"MAIL_AGENDAMENTO_SESSAO_5"));

		html.append(configuracoesFacade.getMensagem("MAIL_AGENDAMENTO_FOOTER", new Object[] {
				nmUsuario,
				nmEmpresa,
				enderecoFilialFormatado
		}));

		final String htmlMail = replaceRecursosMensagens(html.toString());
		
		Mail mail = createMail(dsEmailDestinatario, StringUtils.join(dsEmailsCopia, ";"), dsRemetente, dsAssunto, htmlMail);

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
		
			}
		
	private Mail createMail(String strTo, String cc, String strFrom, String strSubject, String body) {

		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setCc(cc);
		mail.setSubject(strSubject);
		mail.setBody(body);

		return mail;
	}
	
	private String getTelefoneEndereco(EnderecoPessoa endereco, String tpUso) {
		if(endereco != null) {
			TelefoneEndereco telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(endereco.getIdEnderecoPessoa(), tpUso, ConstantesConfiguracoes.TP_TELEFONE_COMERCIAL);
			if(telefoneEndereco != null) {
				return FormatUtils.formatTelefone(telefoneEndereco.getNrTelefone(), telefoneEndereco.getNrDdd(), "");
			}
		}
		return "&nbsp;";
	}

	private String replaceRecursosMensagens(String mail) {
		while (mail.indexOf("##") > -1) {
			int startPos = mail.indexOf("##") + 2;
			int endPos = mail.indexOf("##", startPos + 2);
			String chave = mail.substring(startPos, endPos);
			String texto = configuracoesFacade.getMensagem(chave);
			mail = mail.replace("##" + chave + "##", texto);
		}
		return mail;
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setAgendamentoDoctoServicoDAO(AgendamentoDoctoServicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final AgendamentoDoctoServicoDAO getAgendamentoDoctoServicoDAO() {
		return (AgendamentoDoctoServicoDAO) getDao();
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setVersaoDescritivoPceService(
			VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setAgendamentoEntregaService(
			AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public AgendamentoDoctoServico findAgendamentoByIdDoctoServicoAndTipoAgendamento(
			Long idDoctoServico, String tpAgendamento) {
		return getAgendamentoDoctoServicoDAO()
				.findAgendamentoByIdDoctoServicoAndTipoAgendamento(
						idDoctoServico, tpAgendamento);
	}
	
	public String findDataAgendamentoByIdDoctoServico(Long idDoctoServico) {
		return getAgendamentoDoctoServicoDAO().findDataAgendamentoByIdDoctoServico(idDoctoServico);
	}

	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	
	public void setOcorrenciaPendenciaService(
			OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(
			MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}

	public void setAgendamentoMonitCCTService(
			AgendamentoMonitCCTService agendamentoMonitCCTService) {
		this.agendamentoMonitCCTService = agendamentoMonitCCTService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setCalcularDiasUteisBloqueioAgendamentoService(
			CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService) {
		this.calcularDiasUteisBloqueioAgendamentoService = calcularDiasUteisBloqueioAgendamentoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public DevedorDocServService getDevedorDocServService() {
		return devedorDocServService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public ContatoService getContatoService() {
		return contatoService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	
	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public TurnoService getTurnoService() {
		return turnoService;
	}

	public void setTurnoService(TurnoService turnoService) {
		this.turnoService = turnoService;
	}

	public TelefoneEnderecoService getTelefoneEnderecoService() {
		return telefoneEnderecoService;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public AgendamentoEntregaDAO getAgendamentoEntregaDao() {
		return agendamentoEntregaDao;
	}

	public void setAgendamentoEntregaDao(AgendamentoEntregaDAO agendamentoEntregaDao) {
		this.agendamentoEntregaDao = agendamentoEntregaDao;
	}

	public DoctoServicoDAO getDoctoServicoDao() {
		return doctoServicoDao;
	}

	public void setDoctoServicoDao(DoctoServicoDAO doctoServicoDao) {
		this.doctoServicoDao = doctoServicoDao;
	}
	
	public EventoService getEventoService() {
		return eventoService;
	}

	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}

	public EventoDocumentoServicoDAO getEventoDocumentoServicoService() {
		return eventoDocumentoServicoService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoDAO eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public AgendamentoMonitCCTDAO getAgendamentoMonitCCTDAO() {
		return agendamentoMonitCCTDAO;
	}

	public void setAgendamentoMonitCCTDAO(AgendamentoMonitCCTDAO agendamentoMonitCCTDAO) {
		this.agendamentoMonitCCTDAO = agendamentoMonitCCTDAO;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

}
