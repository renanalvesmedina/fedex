package com.mercurio.lms.contasreceber.model.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.DadoModeloMensagem;
import com.mercurio.lms.contasreceber.model.Email;
import com.mercurio.lms.contasreceber.model.ModeloMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.dao.DadoModeloMensagemDAO;
import com.mercurio.lms.contasreceber.model.dao.ModeloDeMensagemDAO;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class MensagemComunicacaoService extends CrudService<DadoModeloMensagem, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	private DadoModeloMensagemDAO dadoModeloMensagemDao;
	private ModeloDeMensagemDAO modeloDeMensagemDAO;
	private MonitoramentoMensagemService monitoramentoMensagemService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ParametroGeralService parametroGeralService;
	private ConfiguracoesFacade configuracoesFacade;

	public static final String SQL = "SQ";
	public static final String TEXTO = "TX";
	public static final String PARAMETRO = "PA";
	public static final String PARAMEGRO_GERAL = "PG";
	public static final String PARAMEGRO_FILIAL = "PF";
	
	public Email executeMontaMensagemGenerica(MonitoramentoMensagem monitoramentoMsg) {
		List<ModeloMensagem> modeloMensagens = modeloDeMensagemDAO.findByMonitoramentoMensagem(monitoramentoMsg);
		if(modeloMensagens.isEmpty()) {
			monitoramentoMensagemService.saveEventoMensagem(monitoramentoMsg.getIdMonitoramentoMensagem(), "X", "LMS-36309");
			return null;
		}else {
			return executeTransformaModeloMensagem(monitoramentoMsg, modeloMensagens.get(0));	
		}
	}

	public Email executeTransformaModeloMensagem(MonitoramentoMensagem monitoramentoMsg, ModeloMensagem modeloMsg) {
		Email email = new Email();
		Template corpoTemplate = getFreemarkerTemplate(modeloMsg.getDcModeloCorpo());
		Template assuntoTemplate = getFreemarkerTemplate(modeloMsg.getDcModeloAssunto());
		Map<String, Object> parametros = new HashMap<String, Object>();
		Map<String, String> mensagemParametro = parametrosUnmarshal(monitoramentoMsg);

		try {
			List<DadoModeloMensagem> modeloMensagens = dadoModeloMensagemDao.findByModeloMensagemId(modeloMsg);
			for(DadoModeloMensagem d : modeloMensagens) {
				if(SQL.equals(d.getTpDadoModeloMensagem().getValue())) {
					List dados = dadoModeloMensagemDao.findDadosCorpoDoEmail(d.getDsConteudoDadoModMens(), mensagemParametro, monitoramentoMsg);
					parametros.put(d.getNmDadoModeloMensagem(), dados != null ? dados : new ArrayList());
				} else if(TEXTO.equals(d.getTpDadoModeloMensagem().getValue())) {
					parametros.put(d.getNmDadoModeloMensagem(), d.getDsConteudoDadoModMens());
				} else if(PARAMETRO.equals(d.getTpDadoModeloMensagem().getValue())) {
					if (":0".equals(d.getDsConteudoDadoModMens())) {
						parametros.put(d.getNmDadoModeloMensagem(), monitoramentoMsg.getIdMonitoramentoMensagem());
					} else {
						parametros.put(d.getNmDadoModeloMensagem(), mensagemParametro.get(d.getDsConteudoDadoModMens()));
					}
				} else if(PARAMEGRO_GERAL.equals(d.getTpDadoModeloMensagem().getValue())) {
					ParametroGeral conteudo = parametroGeralService.findByNomeParametro(d.getDsConteudoDadoModMens());
					if (conteudo != null) 
						parametros.put(d.getNmDadoModeloMensagem(), conteudo.getDsConteudo());
				} else if(PARAMEGRO_FILIAL.equals(d.getTpDadoModeloMensagem().getValue())) {
					if(d.getDsConteudoDadoModMens() != null) {
						String[] pfParametros = d.getDsConteudoDadoModMens().split(",");
						String parametroFilial = mensagemParametro.get(pfParametros[1].trim());
						ConteudoParametroFilial conteudo = conteudoParametroFilialService.findByNomeParametro(Long.parseLong(parametroFilial), pfParametros[0], false, true);
						if (conteudo != null) {
							parametros.put(d.getNmDadoModeloMensagem(), conteudo.getVlConteudoParametroFilial());
						}else {
							throw new Exception(configuracoesFacade.getMensagem("LMS-00067", new Object[]{parametroFilial, pfParametros[0]}));
						}
					}	
				}
			}

			email.setpAssunto(parseFreemarkerTemplate(assuntoTemplate, parametros));
			email.setpCorpo(parseFreemarkerTemplate(corpoTemplate, parametros));


		}catch(Exception e) {
			monitoramentoMensagemService.saveEventoMensagem(monitoramentoMsg.getIdMonitoramentoMensagem(), "X", e.getMessage());
		}

		return email;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> parametrosUnmarshal(MonitoramentoMensagem monitoramentoMsg) {
		Map<String, String> parametro = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			parametro = mapper.readValue(monitoramentoMsg.getDsParametro(), Map.class);
		} catch (JsonParseException e) {
			log.error(e);
		} catch (JsonMappingException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return parametro;
	}

	private String parseFreemarkerTemplate(Template template, Map<String, Object> map) throws TemplateException, IOException {
		StringWriter output = new StringWriter();
		try {
			template.process(map, output);
		} finally {
			output.close();
		}
		return output.toString();
	}

	private Template getFreemarkerTemplate(String stringTemplate) {
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER); 

		Template template = null;
		try {
			template = new Template("name", new StringReader(stringTemplate), cfg);
		} catch (IOException e) {
			log.error(e);
		}
		return template;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setDadoModeloMensagemDao(DadoModeloMensagemDAO dadoModeloMensagemDao) {
		this.dadoModeloMensagemDao = dadoModeloMensagemDao;
	}

	public void setMonitoramentoMensagemService(
			MonitoramentoMensagemService monitoramentoMensagemService) {
		this.monitoramentoMensagemService = monitoramentoMensagemService;
	}

	public void setDadoModeloMensagemDAO(DadoModeloMensagemDAO dadoModeloMensagemDAO) {
		this.dadoModeloMensagemDao = dadoModeloMensagemDAO;
		setDao( dadoModeloMensagemDAO );
	}

	public void setModeloDeMensagemDAO(ModeloDeMensagemDAO modeloDeMensagemDAO) {
		this.modeloDeMensagemDAO = modeloDeMensagemDAO;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
