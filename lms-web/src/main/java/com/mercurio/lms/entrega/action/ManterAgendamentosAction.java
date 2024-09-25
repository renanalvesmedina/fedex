package com.mercurio.lms.entrega.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.Turno;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.MotivoAgendamentoService;
import com.mercurio.lms.entrega.model.service.RegistrarEntregasParceirosService;
import com.mercurio.lms.entrega.model.service.TurnoService;
import com.mercurio.lms.expedicao.edi.model.service.NotaFiscalEDIService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.sim.model.service.AgendamentoMonitCCTService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;
import com.mercurio.lms.vendas.util.ConstantesEventosPCE;

/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.manterAgendamentosAction"
 */

public class ManterAgendamentosAction extends CrudAction {
	
	private FilialService filialService;
	private ClienteService clienteService;
	private TurnoService turnoService;
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	private DoctoServicoService doctoServicoService;
	private DomainValueService domainValueService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private MdaService mdaService;
	private VersaoDescritivoPceService versaoDescritivoPceService;	
	private MotivoAgendamentoService motivoAgendamentoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private NotaFiscalEDIService notaFiscalEDIService;
	private ControleCargaService controleCargaService;	
	private ManifestoEntregaService manifestoEntregaService;	
	private ManifestoService manifestoService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private UsuarioService usuarioService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private AgendamentoMonitCCTService agendamentoMonitCCTService;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private RegistrarEntregasParceirosService registrarEntregasParceirosService;

	public TypedFlatMap generateReenvioCarta(TypedFlatMap map) {
		Long idAgendamentoEntrega = map.getLong("idAgendamentoEntrega");
		agendamentoDoctoServicoService.executeSendMailAgendamento(idAgendamentoEntrega);		
		return null;
	}
	
	public TypedFlatMap findEmailsTomadorDestinatario(TypedFlatMap map) {
		return agendamentoDoctoServicoService.findEmailsTomadorDestinatario(map);
	}
	

	public void setService(AgendamentoEntregaService agendamentoEntregaService) {
		this.defaultService = agendamentoEntregaService;
	} 

	public void removeById(java.lang.Long id) {
		((AgendamentoEntregaService)defaultService).removeById(id);
	}

	@SuppressWarnings("unchecked")
	public TypedFlatMap montaParametroUrl(TypedFlatMap map) {
		List<TypedFlatMap> chaves = map.getList("nrChaves");
		List<String> parametroUrl = new ArrayList<String>();		
		
		for(TypedFlatMap chave : chaves) {
			parametroUrl.add(chave.getString("nrChave"));
		}
		
		Long idAgendamentoEntrega = map.getLong("idAgendamentoEntrega");
		map.put("parametroUrl", parametroUrl.toString().replace("[", "").replace("]", ""));
		map.put("idAgendamentoEntrega", idAgendamentoEntrega);
		
		return map;
	}
	
	/**
	 *
	 * @author wagnerfc
	 * @param map
	 * @return
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TypedFlatMap findDestinatarioNfe(TypedFlatMap map) {
	String chaveNFe = map.getString("chaveNfe");
	List<TypedFlatMap> chaveBox = map.getList("chaveBox");
	List cliente = new ArrayList();
	
	NotaFiscalEdi destinatario = notaFiscalEletronicaService.findNfeByNrChave(chaveNFe);

	if (destinatario == null) {
	    throw new BusinessException("LMS-09132");
	}

	String cnpjDest = notaFiscalEDIService.getValidatedNrIdentificacao(String.valueOf(destinatario.getCnpjDest()));
	cliente = clienteService.findLookupCliente(cnpjDest);

	if (chaveBox != null) {
		NotaFiscalEdi destinatarioBox;

	    for (TypedFlatMap mapChave : chaveBox) {
	    	destinatarioBox = this.notaFiscalEletronicaService.findNfeByNrChave((String) mapChave.get("nrChave"));
			if (destinatarioBox != null) {
			    if (destinatarioBox.getCnpjDest().longValue() != destinatario.getCnpjDest().longValue()) {
			throw new BusinessException("LMS-09133");
		    }
		}
	    }
	}

	map.put("cliente", cliente.isEmpty() ? null : cliente.get(0));

	return map;
    }
	
    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		((AgendamentoEntregaService)defaultService).removeByIds(ids);
	}

	/**
	 * M�todo retorna lista com as chaves dos MonitoramentosCCT que tiveram seus
	 * id�s passados no crit�rio de pesquisa. 
	 * Realizado dessa forma pois a tela
	 * que passa estes par�metros est� implementanda em SWT, e a tela que chama
	 * este m�todo est� implementanda em Web, dessa forma temos um limite de
	 * caracteres na passagem de par�metros de uma tela para outra (SWT - Web), esta solu��o
	 * visa contornar este problema.
	 * 
	 * 
	 * @param map
	 * @return
	 */
	public TypedFlatMap findDadosMonitoramentoCCT(TypedFlatMap map){	
		// As valida��es dos remententes e destinat�rios das notas est� sendo realizada antes da abertura da tela, dessa forma n�o � necess�rio replicar isto aqui.
		List chavesNfe = monitoramentoNotasFiscaisCCTService.findChavesMonitoramentoCCTByIds(map);
		map.clear();

		// como todas as notas selecionadas devem ter o mesmo destinat�rio, usa a primeira para preencher o destinat�rio na tela.
		map.put("chaveNfe", chavesNfe.get(0)); 
		map = findDestinatarioNfe(map);
		map.put("chavesNfeCCT", chavesNfe);
		
		return map;
	}
	
	public TypedFlatMap findById(java.lang.Long id) {
		AgendamentoEntrega agendamentoEntrega = ((AgendamentoEntregaService)defaultService).findById(id);
		
		TypedFlatMap map = new TypedFlatMap();

		map.put("idAgendamentoEntrega", agendamentoEntrega.getIdAgendamentoEntrega());
		
		if (agendamentoEntrega.getTurno() != null) {
			map.put("turno.idTurno", agendamentoEntrega.getTurno().getIdTurno());
			map.put("turno.dsTurno", agendamentoEntrega.getTurno().getDsTurno());
			map.put("turno.HrTurnoInicial", agendamentoEntrega.getTurno().getHrTurnoInicial());
			map.put("turno.hrTurnoFinal", agendamentoEntrega.getTurno().getHrTurnoFinal());
			map.put("turno.filial.sgFilial", agendamentoEntrega.getTurno().getFilial().getSgFilial());
	    map.put("turno.filial.pessoa.nmFantasia", agendamentoEntrega.getTurno().getFilial().getPessoa()
		    .getNmFantasia());
			map.put("turno.filial.pessoa.nmPessoa", agendamentoEntrega.getTurno().getFilial().getPessoa().getNmPessoa());
		}
		map.put("filial.pessoa.nmFantasia", agendamentoEntrega.getFilial().getPessoa().getNmFantasia());
		map.put("filial.pessoa.nmPessoa", agendamentoEntrega.getFilial().getPessoa().getNmPessoa());
		map.put("filial.sgFilial", agendamentoEntrega.getFilial().getSgFilial());
		map.put("filial.idFilial", agendamentoEntrega.getFilial().getIdFilial());

		map.put("usuarioByIdUsuarioCriacao.idUsuario", agendamentoEntrega.getUsuarioByIdUsuarioCriacao().getIdUsuario());
		map.put("usuarioByIdUsuarioCriacao.nmUsuario", agendamentoEntrega.getUsuarioByIdUsuarioCriacao().getNmUsuario());
		map.put("tpAgendamento", agendamentoEntrega.getTpAgendamento().getValue());
		map.put("dhContato", agendamentoEntrega.getDhContato());
		map.put("dtAgendamento", agendamentoEntrega.getDtAgendamento());
		map.put("nmContato", agendamentoEntrega.getNmContato());
		map.put("nrDdd", agendamentoEntrega.getNrDdd());
		map.put("nrRamal", agendamentoEntrega.getNrRamal());
		map.put("nrTelefone", agendamentoEntrega.getNrTelefone());
		map.put("tpSituacaoAgendamento", agendamentoEntrega.getTpSituacaoAgendamento().getValue());
		map.put("obAgendamentoEntrega", agendamentoEntrega.getObAgendamentoEntrega());
		map.put("obCancelamento", agendamentoEntrega.getObCancelamento());
		map.put("obTentativa", agendamentoEntrega.getObTentativa());
		map.put("hrPreferenciaInicial", agendamentoEntrega.getHrPreferenciaInicial());
		map.put("hrPreferenciaFinal", agendamentoEntrega.getHrPreferenciaFinal());
		map.put("blCartao", agendamentoEntrega.getBlCartao());

		map.put("dhEnvio", agendamentoEntrega.getDhEnvio());
		map.put("dsEmailTomador", agendamentoEntrega.getDsEmailTomador());
		map.put("dsEmailDestinatario", agendamentoEntrega.getDsEmailDestinatario());
		map.put("apagarEmails", "N");
		

		map.put("tipoEmpresa", SessionUtils.getEmpresaSessao().getTpEmpresa().getValue());

		if (agendamentoEntrega.getMotivoAgendamentoByIdMotivoCancelamento() != null) {
	    map.put("motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento", agendamentoEntrega
		    .getMotivoAgendamentoByIdMotivoCancelamento().getIdMotivoAgendamento());
	    map.put("motivoAgendamentoByIdMotivoCancelamento.dsMotivoAgendamento", agendamentoEntrega
		    .getMotivoAgendamentoByIdMotivoCancelamento().getDsMotivoAgendamento());
		}
		
		if (agendamentoEntrega.getMotivoAgendamentoByIdMotivoReagendamento() != null) {
	    map.put("motivoAgendamentoByIdMotivoReagendamento.idMotivoAgendamento", agendamentoEntrega
		    .getMotivoAgendamentoByIdMotivoReagendamento().getIdMotivoAgendamento());
	    map.put("motivoAgendamentoByIdMotivoReagendamento.dsMotivoAgendamento", agendamentoEntrega
		    .getMotivoAgendamentoByIdMotivoReagendamento().getDsMotivoAgendamento());
		}
		
		if (agendamentoEntrega.getReagendamento() != null) {
			
			if (agendamentoEntrega.getReagendamento().getTurno() != null) {
				map.put("reagendamento.idTurno", agendamentoEntrega.getReagendamento().getTurno().getIdTurno());
			}
			if (agendamentoEntrega.getMotivoAgendamentoByIdMotivoReagendamento() != null) {
		map.put("reagendamento.idMotivoReagendamento", agendamentoEntrega
			.getMotivoAgendamentoByIdMotivoReagendamento().getIdMotivoAgendamento());
			}
	    map.put("reagendamento.idAgendamentoEntrega", agendamentoEntrega.getReagendamento()
		    .getIdAgendamentoEntrega());
	    map.put("reagendamento.hrPreferenciaInicial", agendamentoEntrega.getReagendamento()
		    .getHrPreferenciaInicial());
			map.put("reagendamento.hrPreferenciaFinal", agendamentoEntrega.getReagendamento().getHrPreferenciaFinal());
			map.put("reagendamento.dtAgendamento", agendamentoEntrega.getReagendamento().getDtAgendamento());
			map.put("reagendamento.blCartao", agendamentoEntrega.getReagendamento().getBlCartao());
	    map.put("reagendamento.obAgendamentoEntrega", agendamentoEntrega.getReagendamento()
		    .getObAgendamentoEntrega());
		    map.put("reagendamento.nmContato", agendamentoEntrega.getReagendamento().getNmContato());
		    map.put("reagendamento.nrDdd", agendamentoEntrega.getReagendamento().getNrDdd());
		    map.put("reagendamento.nrTelefone", agendamentoEntrega.getReagendamento().getNrTelefone());
		    map.put("reagendamento.nrRamal", agendamentoEntrega.getReagendamento().getNrRamal());
		}
		
		if (agendamentoEntrega.getAgendamentoDoctoServicos() != null) {
			
			List list = new ArrayList();
			
			for (Iterator iter = agendamentoEntrega.getAgendamentoDoctoServicos().iterator(); iter.hasNext();) {
				AgendamentoDoctoServico agendamentoDoctoServico = (AgendamentoDoctoServico) iter.next();
				
				TypedFlatMap map2 = new TypedFlatMap();
				
				map2.put("idAgendamentoDoctoServico", agendamentoDoctoServico.getIdAgendamentoDoctoServico());
				
				DoctoServico doctoServico = agendamentoDoctoServico.getDoctoServico(); 

				map2.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
				map2.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
				map2.put("doctoServico.tpDocumentoServico.value", doctoServico.getTpDocumentoServico().getValue());
				map2.put("doctoServico.tpDocumentoServico.description", doctoServico.getTpDocumentoServico().getDescription());
				map2.put("doctoServico.filialByIdFilialOrigem.idFilial", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
				map2.put("doctoServico.filialByIdFilialOrigem.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
				map2.put("doctoServico.filialByIdFilialOrigem.nmFilial", doctoServico.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
				map2.put("agendamentoEntrega.filial.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
				
				if (doctoServico.getFilialDestinoOperacional() != null) {
					map.put("idFilialDestinoOperacional", doctoServico.getFilialDestinoOperacional().getIdFilial());
					
					map.put("sgFilialDestinoOperacional", doctoServico.getFilialDestinoOperacional().getSgFilial());
		    map.put("nmFilialDestinoOperacional", doctoServico.getFilialDestinoOperacional().getPessoa()
			    .getNmFantasia());

					map.put("filialDestino.idFilial", doctoServico.getFilialDestinoOperacional().getIdFilial());
					
					map.put("filialDestino.sgFilial", doctoServico.getFilialDestinoOperacional().getSgFilial());
		    map.put("filialDestino.pessoa.nmFantasia", doctoServico.getFilialDestinoOperacional().getPessoa()
			    .getNmFantasia());
					
				} 
				
				if (doctoServico.getClienteByIdClienteDestinatario() != null) {
					map.put("destinatario.idCliente", doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
		    map.put("destinatario.pessoa.nrIdentificacao", doctoServico.getClienteByIdClienteDestinatario()
			    .getPessoa().getNrIdentificacao());
		    map.put("destinatario.pessoa.nrIdentificacaoFormatado", FormatUtils
			    .formatIdentificacao(doctoServico.getClienteByIdClienteDestinatario().getPessoa()));
		    map.put("destinatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteDestinatario()
			    .getPessoa().getNmPessoa());
					
		    map2.put("doctoServico.clienteByIdClienteDestinatario.idCliente", doctoServico
			    .getClienteByIdClienteDestinatario().getIdCliente());
		    map2.put("doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao", doctoServico
			    .getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao());
		    map2.put("doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado", FormatUtils
			    .formatIdentificacao(doctoServico.getClienteByIdClienteDestinatario().getPessoa()));
		    map2.put("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa", doctoServico
			    .getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
				}
				
				if (doctoServico.getLocalizacaoMercadoria() != null) {
		    map.put("cdLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria()
			    .getCdLocalizacaoMercadoria());
				}
				
				list.add(map2);
				
			}

	    List<AgendamentoMonitCCT> agendamentoMonitCCTs = this.agendamentoMonitCCTService
		    .findAgendamentoMonitCCTByAgendamentoEntrega(id);
	    List<String> chavesNfe = new ArrayList<String>();

	    if (agendamentoMonitCCTs.isEmpty()) {
		map.put("tpDocumento", "CTRC");
	    }
	    else {
		map.put("tpDocumento", "NFE");

		for (AgendamentoMonitCCT agCct : agendamentoMonitCCTs) {
		    chavesNfe.add(agCct.getMonitoramentoCCT().getNrChave());
		}

		// Busca o destinatario pela chave nfe
		if (!chavesNfe.isEmpty()) {
			NotaFiscalEdi notaFiscalEdi = this.notaFiscalEletronicaService.findNfeByNrChave(chavesNfe.get(0));
			
		    String destinatario = null;
		    if(notaFiscalEdi != null && notaFiscalEdi.getCnpjDest() != null){
		    	destinatario = notaFiscalEDIService.getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString());
		    }
		    List<Map> cliente = clienteService.findLookupCliente(destinatario);
		    
		    if(!cliente.isEmpty()) {
			map.put("destinatario.idCliente", cliente.get(0).get("idCliente"));
			
			Map pessoa = (Map) cliente.get(0).get("pessoa");
			if(pessoa != null) {
			    map.put("destinatario.pessoa.nrIdentificacao", pessoa.get("nrIdentificacao"));
			    map.put("destinatario.pessoa.nrIdentificacaoFormatado", pessoa.get("nrIdentificacaoFormatado"));
			    map.put("destinatario.pessoa.nmPessoa", pessoa.get("nmPessoa"));
			}
		    }
		}
	    }

	    map.put("chavesNfe", chavesNfe);
			map.put("agendamentoDoctoServicos", list);			
		}
		
		return map;
	}

	public List findTurnoByIdFilial(Map criteria) {
		return insereVigenciaTurno(turnoService.find(criteria));
	}

	private List insereVigenciaTurno(List listTurnoVigencia) {
		for (Iterator iter = listTurnoVigencia.iterator(); iter.hasNext();) {
			Turno turno = (Turno) iter.next();
			turno.setFilial(null);
			if (turno.getHrTurnoInicial() != null) {
				turno.setDsTurno(turno.getDsTurno() + " [ " + JTFormatUtils.format(turno.getHrTurnoInicial()) + " - " );
			} else {
				turno.setDsTurno(turno.getDsTurno() + " [ ... - ");
			}
			
			if (turno.getHrTurnoFinal() != null) {
				turno.setDsTurno(turno.getDsTurno() + JTFormatUtils.format(turno.getHrTurnoFinal()) + " ]");
			} else {
				turno.setDsTurno(turno.getDsTurno() + " ... ]");
			}
			
		}
		
		return listTurnoVigencia;
	}

	public List findTurnosVigentes(TypedFlatMap criteria) {
		return insereVigenciaTurno(turnoService.findTurnosVigentes(criteria.getLong("filial.idFilial")));
	}

	public List findTurno(Map criteria) {
		return turnoService.find(criteria);
	}

	public List findMotivoAgendamento(Map criteria) {
		return motivoAgendamentoService.find(criteria);
	}

	public List findLookupFilial(Map criteria) {		
		return filialService.findLookup(criteria);	
	}

	public List findLookupLocalizacaoMercadoria(Map criteria) {		
		return localizacaoMercadoriaService.findLookup(criteria);	
	}
	
	public List findLookupConhecimento(Map criteria) {
		return conhecimentoService.findLookup(criteria);
	}
	
	public List findLookupFuncionario(Map criteria) {		
		return usuarioService.findLookup(criteria);	
	}

	public List findLookupRemetente(Map criteria) {		
		return clienteService.findLookup(criteria);	
	}
	
	public List findLookupDestinatario(Map criteria) {		
		return clienteService.findLookup(criteria);	
	}

	public ResultSetPage findPaginatedAgendamentoEntregaDoctoServico(TypedFlatMap criteria) {
		return agendamentoDoctoServicoService.findPaginatedAgendamentoEntregaDoctoServico(criteria);
	}
	
	public Integer getRowCountAgendamentoEntregaDoctoServico(TypedFlatMap criteria) {
		return agendamentoDoctoServicoService.getRowCountAgendamentoEntregaDoctoServico(criteria);
	}
	
	///AQUI PRA BAIXO S� DOCTOSERVICO
	//LISTA OS VALORES NA COMBO
	public List findTpDoctoServico() {
		List dominiosValidos = new ArrayList();
		dominiosValidos.add("CTR");
		dominiosValidos.add("CRT");
		dominiosValidos.add("MDA");
		dominiosValidos.add("NFT");
		dominiosValidos.add("NTE");
		dominiosValidos.add("CTE");
		List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO",dominiosValidos);
		return retorno;
	}
	
	//FINDS LOOKUP NUMBERS
	public List findLookupServiceDocumentNumberCTR(Map criteria) {
		return conhecimentoService.findLookup(criteria);
	}
	public List findLookupServiceDocumentNumberCRT(Map criteria) {
		return ctoInternacionalService.findLookup(criteria);
	}
	public List findLookupServiceDocumentNumberMDA(Map criteria) {
		return mdaService.findLookup(criteria);
	}
	
	public List findLookupServiceDocumentNumberNFT(Map criteria) {
		return conhecimentoService.findLookup(criteria);
	}
	
	public List findLookupServiceDocumentNumberNTE(Map criteria) {
		return conhecimentoService.findLookup(criteria);
	}
	
	public List findLookupServiceDocumentNumberCTE(Map criteria) {
		return conhecimentoService.findLookup(criteria);
	}


	/*
	 * Metodos de consulta da filial da tag documento de servico
	 */
	public List findLookupServiceDocumentFilialRRE(Map criteria){
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialCTR(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);		
	}

	public List findLookupServiceDocumentFilialCRT(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialMDA(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialNFT(Map criteria) {
		return this.findLookupFilialByDocumentoServico(criteria);
	}	

	public List findLookupServiceDocumentFilialNTE(Map criteria) {
		return this.findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialCTE(Map criteria) {
		return this.findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupFilialByDocumentoServico(Map criteria) {
		List list = filialService.findLookup(criteria);
		List retorno = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Filial filial = (Filial)iter.next();
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("idFilial", filial.getIdFilial());
			typedFlatMap.put("sgFilial", filial.getSgFilial());
			typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
			retorno.add(typedFlatMap);
		}
		return retorno;
	}

	public List confirmarDocumentosServicos(List list) {
		return new ArrayList();
	}

	public List findPaginatedDoctoServico(TypedFlatMap criteria) {		
		return agendamentoDoctoServicoService.findPaginatedDoctoServico(criteria);
	}

	public List verificaExistenciaPce(HashMap map) {
		LinkedList<Map<String, Object>> list = (LinkedList<Map<String, Object>>)map.get("list");
		List<Map<String, Object>> listRetorno = new ArrayList<Map<String, Object>>();

		if (list != null && list.size() > 0) {
			boolean entrou = false;
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Map mapRetorno = new HashMap();
				mapRetorno = (HashMap) iter.next();
				Long idClienteDestinatario = null;
				Long idClienteRemetente = null;
				if (map.get("idClienteDestinatario") == null || map.get("idClienteRemetente") == null) {
					DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(Long.valueOf((String)mapRetorno.get("idDoctoServico")));

					idClienteDestinatario = doctoServico.getClienteByIdClienteDestinatario().getIdCliente();

					mapRetorno.put("idClienteDestinatario", doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
					mapRetorno.put("nrIdentificacaoDestinatario", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao());
					mapRetorno.put("nrIdentificacaoFormatadoDestinatario", FormatUtils.formatIdentificacao(doctoServico.getClienteByIdClienteDestinatario().getPessoa()));
					mapRetorno.put("nmPessoaDestinatario", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
					
					idClienteRemetente = doctoServico.getClienteByIdClienteRemetente().getIdCliente();
					
					if (doctoServico.getFilialDestinoOperacional() != null) {
						mapRetorno.put("idFilialDestinoOperacional", doctoServico.getFilialDestinoOperacional().getIdFilial());
					}
				} else {
					idClienteDestinatario = Long.valueOf((String)mapRetorno.get("idClienteDestinatario"));
					idClienteRemetente = Long.valueOf((String)mapRetorno.get("idClienteRemetente"));
				}				

				Long idRetornoPCEDestinatario = versaoDescritivoPceService.validateifExistPceByCriteria(idClienteDestinatario,ConstantesEventosPCE.CD_PROCESSO_AGENDAMENTO,ConstantesEventosPCE.CD_EVENTO_AGENDAMENTO,ConstantesEventosPCE.CD_OCORRENCIA_AGENDAMENTO);
				Long idRetornoPCERemetente = versaoDescritivoPceService.validateifExistPceByCriteria(idClienteRemetente,ConstantesEventosPCE.CD_PROCESSO_AGENDAMENTO,ConstantesEventosPCE.CD_EVENTO_AGENDAMENTO,ConstantesEventosPCE.CD_OCORRENCIA_AGENDAMENTO);

				mapRetorno.put("idRetornoPCEDestinatario", idRetornoPCEDestinatario);
				mapRetorno.put("idRetornoPCERemetente", idRetornoPCERemetente);

				mapRetorno.put("idDoctoServico", mapRetorno.get("idDoctoServico"));
				mapRetorno.put("nrDoctoServico", mapRetorno.get("nrDoctoServico"));

				mapRetorno.put("tpDocumentoServico", mapRetorno.get("tpDocumentoServico"));
				mapRetorno.put("filialByIdFilialOrigem.sgFilial", mapRetorno.get("filialByIdFilialOrigem.sgFilial"));

				mapRetorno.put("idFilialDestinoOperacional", mapRetorno.get("idFilialDestinoOperacional"));

				if (mapRetorno.get("idFilialDestinoOperacional") != null && !entrou) {
					Filial filialDestino = this.filialService.findById( (Long)mapRetorno.get("idFilialDestinoOperacional") );

					mapRetorno.put("sgFilialDestinoOperacional", filialDestino.getSgFilial());
					mapRetorno.put("nmFilialDestinoOperacional", filialDestino.getPessoa().getNmFantasia());

					entrou = true;
				}
				
				// Verifica se DoctoServico est� entregue, se tiver, mostrar msg LMS-09105
				if (doctoServicoEntregue(Long.valueOf((String)mapRetorno.get("idDoctoServico")))) {
					mapRetorno.put("erro", "LMS-09105");
				} 
				
				// LMS - 4599  -- Se o usu�rio logado for diferente de uma empresa parceira  fa�a os testes abaixo
				String tipoEmpresa = SessionUtils.getEmpresaSessao().getTpEmpresa().getValue();
				if(!tipoEmpresa.equals("P")){
					
					if (doctoServicoFiliaisDiferenteLogada(Long.valueOf((String)mapRetorno.get("idDoctoServico")), Long.valueOf((String)mapRetorno.get("idFilialAgendamento")))) { // Verifica se Filial que est� agendando n�o for a filial origem ou filial destinoOperacional, se n�o for mostrar LMS-09106
					mapRetorno.put("erro", "LMS-09106");
				} else if (doctoServicoManifestadoEntrega(Long.valueOf((String)mapRetorno.get("idDoctoServico")))){
					mapRetorno.put("erro", "LMS-09128");
				}
				}
				
				listRetorno.add(mapRetorno);				
			}
		}
		return listRetorno;
	}	

	private boolean doctoServicoEntregue(Long idDoctoServico) {
		if (idDoctoServico != null) {
			DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
			if (doctoServico != null && doctoServico.getLocalizacaoMercadoria() != null && doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("1"))) {
				return true;
			}
		}
		return false; 
	}
	
	private boolean doctoServicoFiliaisDiferenteLogada(Long idDoctoServico, Long idFilialAgendamento) {
		if (idDoctoServico != null) {
			DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
			if (doctoServico != null) {
				if (doctoServico.getFilialByIdFilialOrigem() != null && 
					doctoServico.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialAgendamento))
					return false;
				
				if (doctoServico.getFilialDestinoOperacional() != null && 
					doctoServico.getFilialDestinoOperacional().getIdFilial().equals(idFilialAgendamento)) 
					return false;				
			}
		}
		return true;
	}
	
	private boolean doctoServicoManifestadoEntrega(Long idDoctoServico){
		String tipoEmpresa = SessionUtils.getEmpresaSessao().getTpEmpresa().getValue();
		if(idDoctoServico != null){
			List<ManifestoEntregaDocumento> listManifestos = manifestoEntregaDocumentoService.findManifestoByIdDoctoServico(idDoctoServico);
			for(ManifestoEntregaDocumento manifesto : listManifestos){
				// LMS - 4599
				if(!manifesto.getTpSituacaoDocumento().getValue().equals("FECH") && 
						!manifesto.getTpSituacaoDocumento().getValue().equals("CANC") &&
						!tipoEmpresa.equals("P")){
					return true;
				}
			}
		}
		return false;
	}

	public TypedFlatMap findDataSession(TypedFlatMap data) {
		Filial bean = SessionUtils.getFilialSessao();
		TypedFlatMap result = new TypedFlatMap();
		result.put("filial.idFilial",bean.getIdFilial());
		result.put("filial.sgFilial",bean.getSgFilial());
		result.put("filial.pessoa.nmFantasia",bean.getPessoa().getNmFantasia());
		Usuario usuario = SessionUtils.getUsuarioLogado();
		result.put("usuarioByIdUsuarioCriacao.nmUsuario",usuario.getNmUsuario());
		result.put("usuarioByIdUsuarioCriacao.idUsuario",usuario.getIdUsuario());
		result.put("usuarioByIdUsuarioCriacao.nrMatricula",usuario.getNrMatricula());
		result.put("dhContato",JTDateTimeUtils.getDataHoraAtual());
		
		
		result.put("tipoEmpresa", SessionUtils.getEmpresaSessao().getTpEmpresa().getValue());
		
		if(data != null && data.containsKey("tpEmpresaSessao") && data.getString("tpEmpresaSessao").equals("P")){
			result.putAll(registrarEntregasParceirosService.findDadosAgendamentoParceira(data));
		}
		
		
		return result;
	}

	// LMS -4599 -- retorna o tipo da empresa logada do usuario
	public TypedFlatMap buscaTipoEmpresaSessao() {
		TypedFlatMap result = new TypedFlatMap();
		result.put("tipoEmpresaSessao", SessionUtils.getEmpresaSessao().getTpEmpresa().getValue());
		return result;
	}

	public TypedFlatMap store(TypedFlatMap map) {
		return agendamentoDoctoServicoService.storeCustom(map);
	}

	public List findLookupControleCarga(Map criteria) {
		return controleCargaService.findLookup(criteria);
	}

	public List findLookupManifestoEntrega(Map criteria) {
		return manifestoEntregaService.findLookup(criteria);
	}
	
	public TypedFlatMap findControleCargaByManifesto(Long idManifesto) {
		Manifesto manifesto = manifestoService.findById(idManifesto);
		TypedFlatMap result = new TypedFlatMap();
		if (manifesto.getControleCarga() != null) {
			ControleCarga controleCarga = manifesto.getControleCarga();
			result.put("controleCarga.nrControleCarga",controleCarga.getNrControleCarga());
			result.put("controleCarga.idControleCarga",controleCarga.getIdControleCarga());
		}
		return result;
	}	
	
	public List findDocumentosServico(TypedFlatMap criteria) {
		return agendamentoDoctoServicoService.findListDoctoServicoByIdAgendamentoEntrega(criteria);
	}

	public List findLookupNotaFiscalCliente(TypedFlatMap criteria) {
		if(criteria.getLong("destinatario.idCliente") == null)
			throw new BusinessException("LMS-09115");
		return notaFiscalConhecimentoService.findLookupNotaFiscalCliente(criteria); 
	}

	// Setters para inje��o pelo Spring.
	public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setMotivoAgendamentoService(MotivoAgendamentoService motivoAgendamentoService) {
		this.motivoAgendamentoService = motivoAgendamentoService;
	}

	public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}
	
	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setAgendamentoDoctoServicoService(AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}
	
	public void setTurnoService(TurnoService turnoService) {
		this.turnoService = turnoService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}
	
	public void setNotaFiscalEDIService(NotaFiscalEDIService notaFiscalEDIService) {
		this.notaFiscalEDIService = notaFiscalEDIService;
	}

	public void setAgendamentoMonitCCTService(AgendamentoMonitCCTService agendamentoMonitCCTService) {
		this.agendamentoMonitCCTService = agendamentoMonitCCTService;
	}
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public RegistrarEntregasParceirosService getRegistrarEntregasParceirosService() {
		return registrarEntregasParceirosService;
}

	public void setRegistrarEntregasParceirosService(
			RegistrarEntregasParceirosService registrarEntregasParceirosService) {
		this.registrarEntregasParceirosService = registrarEntregasParceirosService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}
	
}
