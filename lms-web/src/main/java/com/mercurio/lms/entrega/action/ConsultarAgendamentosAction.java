package com.mercurio.lms.entrega.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.consultarAgendamentosAction"
 */

public class ConsultarAgendamentosAction extends CrudAction {
	
	private FilialService filialService;	
	private ClienteService clienteService;
	private DomainValueService domainValueService;	
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private MdaService mdaService;
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;	
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}
	
	public void setAgendamentoDoctoServicoService(AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}	
	
	public List findLookupFilial(Map criteria) {		
		return filialService.findLookup(criteria);	
	}

	public List findLookupRemetente(Map criteria) {		
		return clienteService.findLookup(criteria);	
	}

	public List findLookupDestinatario(Map criteria) {		
		return clienteService.findLookup(criteria);	
	}
	
	public List findLookupServiceDocumentFilialCTE (Map criteria) {
		return filialService.findLookup(criteria);
	}
	
	public List findLookupServiceDocumentFilialCTR (Map criteria) {
		return filialService.findLookup(criteria);
	}
	
	public List findLookupServiceDocumentFilialNTE (Map criteria) {
		return filialService.findLookup(criteria);
	}

	public List findLookupServiceDocumentFilialCRT (Map criteria) {
		return filialService.findLookup(criteria);
	}

	public List findLookupServiceDocumentFilialMDA (Map criteria) {
		return filialService.findLookup(criteria);
	}

	public List findLookupNotaFiscalCliente(TypedFlatMap criteria) {
		if(criteria.getLong("remetente.idCliente")== null)
			throw new BusinessException("LMS-09111");
		return notaFiscalConhecimentoService.findLookupNotaFiscalCliente(criteria); 
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
		criteria.put("blBloqueado","N");
    	return conhecimentoService.findLookup(criteria);
    }
	public List findLookupServiceDocumentNumberCRT(Map criteria) {
		criteria.put("blBloqueado","N");
		return ctoInternacionalService.findLookup(criteria);
	}
	public List findLookupServiceDocumentNumberMDA(Map criteria) {
		criteria.put("blBloqueado","N");
		return mdaService.findLookup(criteria);
	}
	
	public List findLookupServiceDocumentNumberNFT(Map criteria) {
		criteria.put("blBloqueado","N");
		return conhecimentoService.findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberNTE(Map criteria) {
		criteria.put("blBloqueado","N");
		return conhecimentoService.findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberCTE(Map criteria) {
		criteria.put("blBloqueado","N");
		return conhecimentoService.findLookup(criteria);
	}

	public ResultSetPage findPaginatedConsultaAgendamentoEntregaDoctoServico(TypedFlatMap criteria) {
		return getAgendamentoDoctoServicoService().findPaginatedConsultaAgendamentoEntregaDoctoServico(criteria);
	}

	public AgendamentoDoctoServicoService getAgendamentoDoctoServicoService() {
		return agendamentoDoctoServicoService;
	}

	public Integer getRowCountConsultaAgendamentoEntregaDoctoServico(TypedFlatMap criteria) {
		return getAgendamentoDoctoServicoService().getRowCountConsultaAgendamentoEntregaDoctoServico(criteria);
	}
	
	public TypedFlatMap findDataSession() {
		Filial bean = SessionUtils.getFilialSessao();
		TypedFlatMap result = new TypedFlatMap();
		result.put("filial.idFilial",bean.getIdFilial());
		result.put("filial.sgFilial",bean.getSgFilial());
		result.put("filial.pessoa.nmFantasia",bean.getPessoa().getNmFantasia());
		result.put("blMatriz",Boolean.valueOf(SessionUtils.isFilialSessaoMatriz()).toString());
		Usuario usuario = SessionUtils.getUsuarioLogado();
		result.put("usuarioByIdUsuarioCriacao.nmUsuario",usuario.getNmUsuario());
		result.put("usuarioByIdUsuarioCriacao.idUsuario",usuario.getIdUsuario());
		result.put("usuarioByIdUsuarioCriacao.nrMatricula",usuario.getNrMatricula());
		result.put("dhContato",JTDateTimeUtils.getDataHoraAtual());
		return result;
	}
 
    public TypedFlatMap findByIdCustom(Long idAgendamentoDoctoServico) {
    	if (idAgendamentoDoctoServico != null ) {
    		AgendamentoDoctoServico bean = agendamentoDoctoServicoService.findByIdCustom(idAgendamentoDoctoServico);
    		
    		if (bean != null) {
    			TypedFlatMap result = new TypedFlatMap();
    			result.put("idAgendamentoEntrega", bean.getAgendamentoEntrega().getIdAgendamentoEntrega() );
    			result.put("filialAgendamento.sgFilial", bean.getAgendamentoEntrega().getFilial().getSgFilial() );
    			
    			if ( bean.getAgendamentoEntrega().getFilial() != null && bean.getAgendamentoEntrega().getFilial().getPessoa() != null ) {
    				result.put("filialAgendamento.pessoa.nmFantasia", bean.getAgendamentoEntrega().getFilial().getPessoa().getNmFantasia());
    			}
    			
    			result.put("dhContato", JTFormatUtils.format(bean.getAgendamentoEntrega().getDhContato()));
    			result.put("nmContato", bean.getAgendamentoEntrega().getNmContato());
    			result.put("nrTelefone", bean.getAgendamentoEntrega().getNrTelefone());
    			result.put("nrRamal", bean.getAgendamentoEntrega().getNrRamal());
    			
    			if (bean.getAgendamentoEntrega().getUsuarioByIdUsuarioCriacao() != null) {
    				result.put("usuarioByIdUsuarioCriacao.nmUsuario", bean.getAgendamentoEntrega().getUsuarioByIdUsuarioCriacao().getNmUsuario());
    			}
    			
    			result.put("dtAgendamento", bean.getAgendamentoEntrega().getDtAgendamento());
    			if(bean.getAgendamentoEntrega().getDtAgendamento() != null){
    			result.put("diaAgendamento", JTDateTimeUtils.getWeekdayNameFull(bean.getAgendamentoEntrega().getDtAgendamento()));
    			}
    			if (bean.getAgendamentoEntrega().getTurno() != null) {
    				result.put("turno.dsTurno", bean.getAgendamentoEntrega().getTurno().getDsTurno());
    			}        		
    			
    			result.put("tpSituacaoAgendamento", domainValueService.findDomainValueDescription("DM_SITUACAO_AGENDA", bean.getAgendamentoEntrega().getTpSituacaoAgendamento().getValue()));
    			
    			result.put("hrPreferenciaInicial", bean.getAgendamentoEntrega().getHrPreferenciaInicial());
    			result.put("hrPreferenciaFinal", bean.getAgendamentoEntrega().getHrPreferenciaFinal());
    			result.put("blCartao", bean.getAgendamentoEntrega().getBlCartao());
    			result.put("obAgendamentoEntrega", bean.getAgendamentoEntrega().getObAgendamentoEntrega());       		
    			
    			
    			result.put("idDoctoServico", bean.getDoctoServico().getIdDoctoServico());
    			result.put("nrDoctoServico", bean.getDoctoServico().getNrDoctoServico());
    			result.put("tpDocumentoServico", domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO", bean.getDoctoServico().getTpDocumentoServico().getValue()));
    			result.put("tpDocumentoServicoValue", bean.getDoctoServico().getTpDocumentoServico().getValue());

    			List list = manifestoEntregaDocumentoService.findManifestoEntregaByDoctoServico(bean.getDoctoServico().getIdDoctoServico());
    			if (!list.isEmpty()){
    				for (Iterator iter = list.iterator(); iter.hasNext();) {
						Map map = (Map) iter.next();

						if ( map.get("tpOcorrencia") != null && "E".equals(((DomainValue)map.get("tpOcorrencia")).getValue()) ) {
		    				result.put("manifestoEntrega", map.get("nrManifestoEntrega"));
		    				result.put("sgFilialManifestoEntrega", map.get("sgFilial"));
		    				
		    				result.put( "controleCarga", map.get("nrControleCarga"));    					
							result.put( "sgFilialControleCarga", map.get("sgFilialControleCarga"));
							
							break;
							
						}						
					}
    			}
    			
    			if (bean.getDoctoServico().getServico() != null) {
    				result.put("servico.dsServico", bean.getDoctoServico().getServico().getDsServico());
    			}
    			
    			if (bean.getDoctoServico().getFilialByIdFilialOrigem() != null) {
    				result.put("sgFilialOrigem", bean.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
    			}
    			
    			if (bean.getDoctoServico().getFilialDestinoOperacional() != null && bean.getDoctoServico().getFilialDestinoOperacional().getPessoa() != null) {
    				result.put("filialDestino.sgFilial", bean.getDoctoServico().getFilialDestinoOperacional().getSgFilial() );
    				result.put("filialDestino.pessoa.nmFantasia", bean.getDoctoServico().getFilialDestinoOperacional().getPessoa().getNmFantasia());
    			}
    			
    			if (bean.getDoctoServico().getClienteByIdClienteRemetente() != null) {
    				if ( bean.getDoctoServico().getClienteByIdClienteRemetente().getPessoa() != null) {
    					if ( bean.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getTpIdentificacao() != null ) {
    						result.put("remetente.pessoa.tpIdentificacao", bean.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getTpIdentificacao().getValue() );
    					}
    					result.put("remetente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(bean.getDoctoServico().getClienteByIdClienteRemetente().getPessoa()));
    					result.put("remetente.pessoa.nmPessoa", bean.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
    				}
    			}
    			
    			if (bean.getDoctoServico().getClienteByIdClienteDestinatario() != null) {
    				if (bean.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa() != null) {
    					if (bean.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa().getTpIdentificacao() != null) {
    						result.put("destinatario.pessoa.tpIdentificacao", bean.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa().getTpIdentificacao().getValue() );
    					}
    					result.put("destinatario.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(bean.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa()));
    					result.put("destinatario.pessoa.nmPessoa", bean.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
    				}
    			}
    			
    			result.put("DPE", bean.getDoctoServico().getDtPrevEntrega());
    			
    			if (bean.getDoctoServico().getManifestoEntregaDocumentos() != null ) {
    				for (Iterator iterator = bean.getDoctoServico().getManifestoEntregaDocumentos().iterator(); iterator.hasNext();) {
    					ManifestoEntregaDocumento manifestoEntregaDocumento = (ManifestoEntregaDocumento) iterator.next();
    					
    					if (manifestoEntregaDocumento.getOcorrenciaEntrega() != null && "E".equals(manifestoEntregaDocumento.getOcorrenciaEntrega().getTpOcorrencia().getValue())) {
    						
    						if ("A".equals(bean.getAgendamentoEntrega().getTpSituacaoAgendamento().getValue()) 
    								|| "F".equals(bean.getAgendamentoEntrega().getTpSituacaoAgendamento().getValue())) {
	    						result.put( "dataHora", JTFormatUtils.format(manifestoEntregaDocumento.getDhOcorrencia()) );
    						}	
    						if (manifestoEntregaDocumento.getDhOcorrencia() != null ) {    							
    							
    							TypedFlatMap criterio = new TypedFlatMap();
    							criterio.put("filial.idFilial", manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem().getIdFilial());

            										break;    										
            									}    									
            									}
        									}
        								}
    			
				List listNF = notaFiscalConhecimentoService.findNFByIdConhecimento(bean.getDoctoServico().getIdDoctoServico());
				if (listNF != null && listNF.size() > 0) {
					result.put("nrNotaFiscal", listNF.get(0));
				}
    			
    			return result;
    		}
    	}
    	
    	return new TypedFlatMap(); 
    }
    
	public List findDocumentosServico(TypedFlatMap criteria) {
		return agendamentoDoctoServicoService.findListDoctoServicoByIdAgendamentoEntrega(criteria);
	}
}
