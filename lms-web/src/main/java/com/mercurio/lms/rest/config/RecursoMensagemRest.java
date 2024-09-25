package com.mercurio.lms.rest.config;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.cache.LabelsI18n;
import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import com.mercurio.adsm.core.util.LabelsI18nUtils;
import com.mercurio.lms.annotation.Public;

@Public
@Path("/config/recursoMensagem")
public class RecursoMensagemRest {
	
	private static final int MAX_AGE = 86400;

	private Map<String, String> getMensagens() {
		Map<String, Map<String, String>> recursosMensagens = RecursoMensagemCache.getRecursosMensagensByIdioma();
		if (recursosMensagens == null || recursosMensagens.isEmpty()) {
			RecursoMensagemCache.initializeCache();
			recursosMensagens = RecursoMensagemCache.getRecursosMensagensByIdioma();
		}
		if (recursosMensagens != null) {
			String idioma = LocaleContextHolder.getLocale().toString().toLowerCase();
			return recursosMensagens.get(idioma);
		}
		return new HashMap<String, String>();
	}

	@GET
	@Path("listarMensagem")
	public Response listrMensagem(){
		Map<String, String> recursosMensagens = getMensagens();
		recursosMensagens = recursosMensagens.entrySet().stream().filter(rm -> rm.getKey().startsWith("LMS-"))
								.collect(Collectors.toMap(keyMapper -> keyMapper.getKey(), valueMapper -> valueMapper.getValue()));
		return Response.ok(recursosMensagens).build();
	}

	@GET
	@Path("teste")
	public Response teste(){
		return Response.ok("Ok").build();
	}

	@GET
	@Path("getMensagens")
	public Response getMensagens(@QueryParam("chaves") String chaves, @Context final Request request) {
		return getRecurosMensagens(chaves, request, true);
	}

	@GET
	@Path("getMensagensToCamel")
	public Response getMensagensToCamel(@QueryParam("chaves") String chaves, @Context final Request request) {
		return getRecurosMensagens(chaves, request, false);
	}
	

	private Response getRecurosMensagens(String chaves, final Request request, Boolean withISO) {
		
		//Create cache control header
		CacheControl cc = new CacheControl();
		//Set max age to one day
		cc.setMaxAge(MAX_AGE);
		if(RecursoMensagemCache.getDataAtualizacao() == null){
			RecursoMensagemCache.initializeCache();
		}

		//Calcula a ETag pelo composição do idioma, código do arquivo e hash do arquivo
		EntityTag eTag = new EntityTag(RecursoMensagemCache.getDataAtualizacao().getTime()+chaves.toString().hashCode()+"");
		
		//Verify if it matched with etag available in http request
		ResponseBuilder rb = request.evaluatePreconditions(eTag);
		
		//Se for diferente de null manda um status "not modified" para a tela
        if (rb != null) {
            return rb.cacheControl(cc).tag(eTag).build();
        }
		
		Map<String, String> toReturn = new HashMap<String, String>();
		String[] ss = chaves.split(",");
		
		Map<String, String> mensagens = getMensagens();
		
		for (String s: ss) {
			String mensagem = mensagens.get(s);
			if (mensagem == null) {
				//TODO Estas mensagens são do arquivo basemessages.properties do swt, cadastrar na tabela recursos_mensagens
				if ("erSemRegistro".equals(s)) {
					mensagem = "Não há registro selecionado para exclusão!";
				} else if ("erExcluir".equals(s)) {
					mensagem = "Deseja excluir o(s) registro(s) selecionado(s)?";
				} else if ("erExcluirRegistroAtual".equals(s)) {
					mensagem = "Deseja excluir o registro atual?";
				}
			}
			if (mensagem == null) {
				mensagem = RecursoMensagemCache.getMessage(s, "pt_br");
			}
			toReturn.put(s, mensagem);

		}
		if(withISO) {
			return Response
					.ok(toReturn)
					.cacheControl(cc)
					.encoding("ISO-8859-1")
					.tag(eTag).build();
		}

		return Response
				.ok(toReturn)
				.cacheControl(cc)
				.tag(eTag).build();
	}
	
	@GET
	@Path("i18n/labels/{lang}/{codigo}/{hash}.js")
	@Produces({"application/javascript"})
	public Response getLabelsFile(@PathParam("lang") final String lang, @PathParam("codigo") final Integer codigo, @PathParam("hash") final Integer hash, @Context final Request request) {
		
		//Create cache control header
		CacheControl cc = new CacheControl();
		//Set max age to one day
		cc.setMaxAge(MAX_AGE);

		//Calcula a ETag pelo composição do idioma, código do arquivo e hash do arquivo
		EntityTag eTag = new EntityTag(lang+codigo+hash);
		
		//Verify if it matched with etag available in http request
		ResponseBuilder rb = request.evaluatePreconditions(eTag);
		
		//Se for diferente de null manda um status "not modified" para a tela
        if (rb != null) {
            return rb.cacheControl(cc).tag(eTag).build();
        }

		final Map<String, Map<Integer, LabelsI18n>> labelsI18n = RecursoMensagemCache.getLabelsI18n();
		if (labelsI18n == null || labelsI18n.get(lang) == null && labelsI18n.get(lang).get(codigo) == null || labelsI18n.get(lang).get(codigo).getRecursosMensagens().isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response
	            .ok(new StreamingOutput() {
					
					@Override
					public void write(OutputStream output) throws IOException {
						output.write("Translate.add(".getBytes("ISO-8859-1"));
						output.write(("\"" + lang + "\", ").getBytes("ISO-8859-1"));
						ObjectMapper mapper = new ObjectMapper();
						output.write(mapper.writeValueAsString(labelsI18n.get(lang).get(codigo).getRecursosMensagens()).getBytes("ISO-8859-1"));
						output.write(");".getBytes("ISO-8859-1"));
					}
				})
	            .cacheControl(cc)
	            .encoding("ISO-8859-1")
	            .tag(eTag)
	            .build();

	}
	
	@GET
	@Path("i18n/listLabels.js")
	@Produces({"application/javascript"})
	public Response getListLabels(@PathParam("lang") final String lang, @Context final Request request) {


		Locale locale = LocaleContextHolder.getLocale();
		final String idioma = locale.toString().toLowerCase();
		
		final List<String> urlsLabelsI18n = RecursoMensagemCache.getUrlsLabelsI18n(idioma);
		if (!LabelsI18nUtils.IDIOMA_PADRAO.equalsIgnoreCase(idioma)) {
			urlsLabelsI18n.addAll(RecursoMensagemCache.getUrlsLabelsI18n(LabelsI18nUtils.IDIOMA_PADRAO.toLowerCase()));
		}
		
		return Response
	            .ok(new StreamingOutput() {
					
					@Override
					public void write(OutputStream output) throws IOException {
						output.write("var listLabels = ".getBytes("ISO-8859-1"));
						ObjectMapper mapper = new ObjectMapper();
						output.write(mapper.writeValueAsString(urlsLabelsI18n).getBytes("ISO-8859-1"));
						output.write("; ".getBytes("ISO-8859-1"));
						output.write("window.Translate.lang = ".getBytes("ISO-8859-1"));
						output.write(("'"+idioma+"';").getBytes("ISO-8859-1"));
					}
				})
	            .encoding("ISO-8859-1")
	            .build();

	}

}
