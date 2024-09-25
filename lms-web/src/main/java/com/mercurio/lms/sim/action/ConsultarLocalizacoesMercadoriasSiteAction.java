package com.mercurio.lms.sim.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.FormatUtils;

@ServiceSecurity
public class ConsultarLocalizacoesMercadoriasSiteAction  {
	private ConhecimentoService conhecimentoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ParametroGeralService parametroGeralService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private DevedorDocServService devedorDocServService;
	
	@MethodSecurity(processGroup = "consultas-tnt", processName = "consultarLocalizacoesMercadoriasSiteAction.findLocalizacaoByDnApple", authenticationRequired = false)
	public Map<String, Object> findLocalizacaoByDnApple(Map param) {
		String dsValorCampo = MapUtils.getString(param, "dsValorCampo");
		String localeCountry = MapUtils.getString(param, "localeCountry");
		String localeLanguage = MapUtils.getString(param, "localeLanguage");
		ParametroGeral dnApple = parametroGeralService.findByNomeParametro("ID_INFORMACAO_DN_APPLE", false);
		Long idInformacaoDoctoCliente = Long.parseLong(dnApple.getDsConteudo());
		
		Conhecimento ctos = conhecimentoService.findByDadosComplemento(dsValorCampo, idInformacaoDoctoCliente);
		
		if(ctos == null) {
			return null;
		} else {		
		return this.findLocalizacaoAberta(ctos, this.getLocale(localeLanguage, localeCountry));
		}
	}
				
	@MethodSecurity(processGroup = "consultas-tnt", processName = "consultarLocalizacoesMercadoriasSiteAction.findLocalizacaoSimplificada", authenticationRequired = false)
	public Map<String, Object> findLocalizacaoSimplificada(Map<String, Object> param) {
		Locale locale = new Locale("pt", "BR");
		
		String tpCliente = MapUtils.getString(param, "tpCliente");
		String nrIdentificacao = MapUtils.getString(param, "nrIdentificacao");
		String tpDocumento = MapUtils.getString(param, "tpDocumento");
		String nrDocumento = MapUtils.getString(param, "nrDocumento");
		
		Map<String, Object> localizacao = 
			localizacaoMercadoriaService.findLocalizacaoSimplificada(tpCliente, nrIdentificacao, tpDocumento, nrDocumento);
		
		if(localizacao != null) {
			String nrIdentificacaoRemetente = MapUtils.getString(localizacao, "nrIdentificacaoRemetente");
			DomainValue tpIdentificacaoRemetente = (DomainValue) localizacao.get("tpIdentificacaoRemetente");
			localizacao.put("tpIdentificacaoRemetente", tpIdentificacaoRemetente.getValue());
			localizacao.put("nrIdentificacaoRemetenteFormatado", FormatUtils.formatIdentificacao(tpIdentificacaoRemetente, nrIdentificacaoRemetente));
			
			String nrIdentificacaoDestinatario = MapUtils.getString(localizacao, "nrIdentificacaoDestinatario");
			DomainValue tpIdentificacaoDestinatario = (DomainValue) localizacao.get("tpIdentificacaoDestinatario");
			localizacao.put("tpIdentificacaoDestinatario", tpIdentificacaoDestinatario.getValue());
			localizacao.put("nrIdentificacaoDestinatarioFormatado", FormatUtils.formatIdentificacao(tpIdentificacaoDestinatario, nrIdentificacaoDestinatario));
			
			String dsStatus = ((VarcharI18n)localizacao.get("dsLocalizacaoMercadoria")).getValue();
			if(localizacao.get("obComplementoLocalizacao") != null) {
				dsStatus += " " + MapUtils.getString(localizacao, "obComplementoLocalizacao");
			}																
			
			if(localizacao.get("dhEntrega") != null) {
				DateTime dt = new DateTime(((Timestamp)localizacao.get("dhEntrega")).getTime());
				localizacao.put("dhEntrega", this.formatDate(dt, locale));
			}
			
			if(localizacao.get("dhColeta") != null) {
				localizacao.put("dhColeta", this.formatDate((DateTime)localizacao.get("dhColeta"), locale));
			}
			
			localizacao.put("dhEmissao", this.formatDate((DateTime)localizacao.get("dhEmissao"), locale));
			localizacao.put("dtPrevEntrega", this.formatDate((YearMonthDay)localizacao.get("dtPrevEntrega"), locale));
			localizacao.put("dsStatus", dsStatus);
			
			Long idConhecimento = MapUtils.getLong(localizacao, "idConhecimento"); 
			
			if(idConhecimento != null) {
				localizacao.put("notasFiscais", this.findNotasFiscais(idConhecimento, locale));
				localizacao.put("eventos", this.findEventos(idConhecimento, locale));
			}
			
			DevedorDocServ devedor = devedorDocServService.findDevedorByDoctoServico(MapUtils.getLong(localizacao, "idConhecimento"));
			if(devedor != null) {
				Pessoa pess = devedor.getCliente().getPessoa();				
				localizacao.put("tpIdentificacaoDevedor", pess.getTpIdentificacao().getValue());
				localizacao.put("nrIdentificacaoDevedor", pess.getNrIdentificacao());
				localizacao.put("nrIdentificacaoDevedorFormatado", FormatUtils.formatIdentificacao(pess.getTpIdentificacao(), pess.getNrIdentificacao()));
				localizacao.put("nmPessoaDevedor", pess.getNmPessoa());
			}
		}
		
		return localizacao;
	}
	
	private Map<String,Object> findLocalizacaoAberta(Conhecimento ctos, Locale locale) {					
		Map<String, Object> retorno = new HashMap<String, Object>();
			retorno.put("idConhecimento", ctos.getIdDoctoServico());
			retorno.put("nrConhecimento", ctos.getNrConhecimento());
			retorno.put("sgFilialOrigem", ctos.getFilialOrigem().getSgFilial());
			
			if(ctos.getLocalizacaoMercadoria() != null) {
			String dsLocalizacao = this.getMessage(ctos
					.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria(),
					locale);
			dsLocalizacao += this.getComplementoObservacao(
					ctos.getObComplementoLocalizacao(), locale);
			dsLocalizacao = statusLocalizacaoApple(dsLocalizacao, ctos
					.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria()
					.intValue(), locale);
				retorno.put("dsLocalizacao", dsLocalizacao);
			}
						
		retorno.put("dtPrevisaoEntrega",
				this.formatDate(ctos.getDtPrevEntrega(), locale));
		retorno.put("dtPrevisaoEntrega",
				this.formatDate(ctos.getDtColeta(), locale));
			
		retorno.put("notasFiscais",
				this.findNotasFiscais(ctos.getIdDoctoServico(), locale));
		retorno.put("eventos",
				this.findEventos(ctos.getIdDoctoServico(), locale));
				
		return retorno;
	}
	
	private String statusLocalizacaoApple(String original, int codigo, Locale locale) {
		String dsLocalizacao = original;
		boolean portugues = false;
		if(locale != null && locale.toString().trim().equals("pt_BR"))
			portugues = true;
		switch (codigo) {
			case 122:
			case 123:
			case 125:
			case 128:	
			case 134:
			case 131:
			case 132:
			case 138:
			case 164:				
			case 165:
			case 209:
			case 210:
			case 211:
				dsLocalizacao = (portugues ? "Coletado" : "Picked up");
				break;
			case 129:				
			case 130:				
			case 143:  
			case 163:
			case 166:
			case 167:
			case 168:
			case 171:
			case 172:
			case 173:
			case 202:
			case 206:
			case 207:
			case 208:
			case 214:
			case 215:
			case 216:
			case 217:
			case 219:	
				dsLocalizacao = (portugues ? "Em trânsito" : "In Transit");
				break;
			case 127:				
			case 162:  
			case 213:
			dsLocalizacao = (portugues ? "Em rota para Entrega"
					: "Out for delivery");
				break;
			case 124:  
				dsLocalizacao = (portugues ? "Cancelado" : "Cancelled");
				break;
			case 126:
			case 169:
			case 170:
				dsLocalizacao = (portugues ? "Entrega Realizada" : "Delivered");
				break;
		default: // 137, 103, 108, 182, 183, 220, 135, 139, 104, 105, 106, 107,
					// 133, 141, 174, 218
				dsLocalizacao = (portugues ? "Exceções" : "Exceptions");
				break;
		}
		return dsLocalizacao;
	}

	private List<Map<String, Object>> findNotasFiscais(Long idConhecimento,
			Locale locale) {
		/* Busca as notas fiscais do documento de serviço para retorno */
		List<NotaFiscalConhecimento> notasFiscais = notaFiscalConhecimentoService
				.findByConhecimento(idConhecimento);
		List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();		
		
		if(notasFiscais != null) {
			for(NotaFiscalConhecimento nota : notasFiscais) {
					Map<String, Object> notaMapped = new HashMap<String, Object>();
					notaMapped.put("nrNotaFiscal", nota.getNrNotaFiscal());
				notaMapped.put("dtEmissao",
						this.formatDate(nota.getDtEmissao(), locale));
					notaMapped.put("qtVolumes", nota.getQtVolumes());
					notaMapped.put("psMercadoria", nota.getPsMercadoria());
				
				retorno.add(notaMapped);
				}
			}
			
		return retorno;
	}
	
	private List<Map<String, Object>> findEventos(Long idConhecimento,
			Locale locale) {
			/* Busca os eventos do documento de serviço para retorno */
		List<EventoDocumentoServico> eventos = eventoDocumentoServicoService
				.findByDocumentoServico(idConhecimento);
		List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();		
		
			if(eventos != null && eventos.size() > 0) {
				for(EventoDocumentoServico evento : eventos) {
				/*
				 * Verifica se deve exibir o evento ao cliente e se é um evento
				 * Realizado e não Previsto
				 */
				if (evento.getEvento().getBlExibeCliente()
						&& evento.getEvento().getTpEvento().getValue()
								.equals("R")) {
					/*
					 * Monta mapa com as informações dos eventos que serão
					 * necessárias
					 */
					Map<String, Object> mapEvento = new HashMap<String, Object>();
					
					mapEvento.put("dhEvento", 
							this.formatDate(evento.getDhEvento(), locale));
					
					mapEvento.put("dtEvento", 
							this.formatDate(new YearMonthDay(evento.getDhEvento()), locale));

					String dsEvento = "";
					if(evento.getOcorrenciaEntrega() != null) {
						dsEvento = this.getMessage(evento
								.getOcorrenciaEntrega()
								.getDsOcorrenciaEntrega(), locale);
					} else if(evento.getOcorrenciaPendencia() != null) {
						dsEvento = this.getMessage(evento
								.getOcorrenciaPendencia().getDsOcorrencia(),
								locale);
						} else if(evento.getEvento().getLocalizacaoMercadoria() != null) {
						dsEvento = this.getMessage(evento.getEvento()
								.getLocalizacaoMercadoria()
								.getDsLocalizacaoMercadoria(), locale);
					} else { 
						dsEvento = this.getMessage(evento.getEvento()
								.getDescricaoEvento().getDsDescricaoEvento(),
								locale);
					}
					
					
					mapEvento.put("dsEventoSimples", dsEvento);
					dsEvento += getComplementoObservacao(
							evento.getObComplemento(), locale);									
					mapEvento.put("dsEvento", dsEvento);
					mapEvento.put("sgFilial", evento.getFilial().getSgFilial());
					mapEvento.put("nmFilial", evento.getFilial().getPessoa().getNmFantasia());
					retorno.add(mapEvento);
								}
					}
						}
		
		return retorno;
				}

	private String getMessage(VarcharI18n message, Locale locale) {
		if(locale != null) {
			return message.getValue(locale);
		} else {
			return message.getValue();
		}
	}
	
	private Locale getLocale(String language, String country) {
		Locale locale = null;
		if(!language.equalsIgnoreCase("PT")) {			
			locale = new Locale("en","US");
		} else {
			locale = new Locale("pt","BR");
		}
				
		return locale;
			}			
	
	private String getComplementoObservacao(String complementoObservacao,
			Locale locale) {
		String retorno = "";
		if(complementoObservacao != null) {
			if (complementoObservacao.toUpperCase().contains(
					"BAIXA POR CELULAR")) {
				if(locale!=null && locale == Locale.US) {
					retorno = " (mobile activity)";
				} else {
					retorno += " " + complementoObservacao.toLowerCase();
		}
			} else {
				retorno += " " + complementoObservacao;
			}
		}
		return retorno;
	}

	public String formatDate(YearMonthDay date, Locale locale) {
		if(date != null) { 
			if(locale != null && locale.toString().trim().equals("pt_BR")) {
				return date.toString("dd/MM/yyyy");
			} else {
				return date.toString("MM/dd/yyyy");
			}
		} else {
			return null;
		}
	}
	
	public String formatDate(DateTime date, Locale locale) {
		if(date != null) { 
			if(locale != null && locale.toString().trim().equals("pt_BR")) {
				return date.toString("dd/MM/yyyy HH:mm");
			} else {
				return date.toString("MM/dd/yyyy hh:mm a");
			}
		} else {
			return null;
		}
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}		

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
}

	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}
}
