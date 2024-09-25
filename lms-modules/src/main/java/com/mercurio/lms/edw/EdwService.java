package com.mercurio.lms.edw;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.calendariotnt.CalendarioTntDMN;
import br.com.tntbrasil.integracao.domains.comissionamento.ComissaoWrapperDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.enums.edw.CalendarioEDWServiceEnum;
import com.mercurio.lms.util.enums.edw.ComissionamentoEDWServiceEnum;

public class EdwService {

	private static final String PARAMETRO_SERVIDOR_EDW_SERVICE = "SERVIDOR_EDW_SERVICE";
	private static final String PARAMETRO_SENHA_EDW_SERVICE = "SENHA_EDW_SERVICE";
	private static final String USUARIO_EDW_SERVICE = "edwServiceUser";
	private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
	private static final String BASIC_AUTHOTIZATION = "Basic ";
	private ParametroGeralService parametroGeralService;
	
	
	public CalendarioTntDMN findCalendarioTNTByData(YearMonthDay data) throws BusinessException{
		
		CalendarioTntDMN calendarioTntDMN = null;
			
		try {
			Client client = ClientBuilder.newClient();
						        			
			Invocation target = client
					.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
					.path(CalendarioEDWServiceEnum.FIND_BY_DIA.getPath())
					.queryParam("dia", JTFormatUtils.format(data))
					.request(MediaType.APPLICATION_JSON)
					.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
					.buildGet();
	
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			calendarioTntDMN = mapper.readValue(in, CalendarioTntDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return calendarioTntDMN;
	}
	

	
	public ComissaoWrapperDMN findDemonstrativoPagamentoByCpf(Long cargoCpf, Integer mesCalculo, 
			Integer anoCalculo, String tipoComissao, String cargos) throws BusinessException{
		
		ComissaoWrapperDMN comissaoWrapperDMN = null;
		
		try {
			Client client = ClientBuilder.newClient();
			
			Invocation target = client
					.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
					.path(ComissionamentoEDWServiceEnum.FIND_DEMONSTRATIVO_PAGAMENTO.getPath())
					
					.queryParam("cargoCpf", cargoCpf)
					.queryParam("mesCalculo", mesCalculo)
					.queryParam("anoCalculo", anoCalculo)
					.queryParam("tipoComissao", tipoComissao)
					.queryParam("tipoCargo", cargos)
					
					.request(MediaType.APPLICATION_JSON)
					.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
					.buildGet();
	
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			comissaoWrapperDMN = mapper.readValue(in, ComissaoWrapperDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return comissaoWrapperDMN;
	}

	public ComissaoWrapperDMN findDemonstrativoPagamentoByCpfComModal(Long cargoCpf, Integer mesCalculo, 
			Integer anoCalculo, String tipoModal, String cargos) throws BusinessException{
		
		ComissaoWrapperDMN comissaoWrapperDMN = null;
		
		try {
			Client client = ClientBuilder.newClient();
			
			Invocation target = client
					.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
					.path(ComissionamentoEDWServiceEnum.FIND_DEMONSTRATIVO_PAGAMENTO.getPath())
					
					.queryParam("cargoCpf", cargoCpf)
					.queryParam("mesCalculo", mesCalculo)
					.queryParam("anoCalculo", anoCalculo)
					.queryParam("modal", tipoModal)
					.queryParam("tipoCargo", cargos)
					
					.request(MediaType.APPLICATION_JSON)
					.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
					.buildGet();
	
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			comissaoWrapperDMN = mapper.readValue(in, ComissaoWrapperDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return comissaoWrapperDMN;
	}

	
	public ComissaoWrapperDMN findDemonstrativoPagamentoByTerritorio(Long idTerritorioFilial, Integer mesCalculo, 
			Integer anoCalculo, String modal, String cargos) throws BusinessException{
		
		ComissaoWrapperDMN comissaoWrapperDMN = null;
		
		try {
			Client client = ClientBuilder.newClient();
			Invocation target = client
					.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
					.path(ComissionamentoEDWServiceEnum.FIND_DEMONSTRATIVO_PAGAMENTO.getPath())
					
					.queryParam("territorioIdFilial", idTerritorioFilial)
					.queryParam("mesCalculo", mesCalculo)
					.queryParam("anoCalculo", anoCalculo)
					.queryParam("tipoComissao", "MANUTENCAO,CONQUISTA")
					.queryParam("modal", modal)
					.queryParam("tipoCargo", cargos)

					.request(MediaType.APPLICATION_JSON)
					.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
					.buildGet();
	
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			comissaoWrapperDMN = mapper.readValue(in, ComissaoWrapperDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return comissaoWrapperDMN;
	}
	
	public ComissaoWrapperDMN findTotalDemonstrativoPagamentoByCpf(Long cargoCpf, Integer mesCalculo, 
			Integer anoCalculo, String tiposComissao, String cargos) throws BusinessException {
		
		ComissaoWrapperDMN comissaoWrapperDMN = null;
		
		try {
			Client client = ClientBuilder.newClient();
			
			Invocation target;
			
			if (tiposComissao == null) {
				target = client
						.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
						.path(ComissionamentoEDWServiceEnum.FIND_TOTAL_DEMONSTRATIVO_PAGAMENTO.getPath())
						.queryParam("cargoCpf", cargoCpf)
						.queryParam("mesCalculo", mesCalculo)
						.queryParam("anoCalculo", anoCalculo)
						.queryParam("tipoCargo", cargos)
						.request(MediaType.APPLICATION_JSON)
						.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
						.buildGet();
			} else {
				target = client
						.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
						.path(ComissionamentoEDWServiceEnum.FIND_TOTAL_DEMONSTRATIVO_PAGAMENTO.getPath())
						.queryParam("cargoCpf", cargoCpf)
						.queryParam("mesCalculo", mesCalculo)
						.queryParam("anoCalculo", anoCalculo)
						.queryParam("tipoComissao", tiposComissao)
						.queryParam("tipoCargo", cargos)
						.request(MediaType.APPLICATION_JSON)
						.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
						.buildGet();
			}
			
	
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			comissaoWrapperDMN = mapper.readValue(in, ComissaoWrapperDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return comissaoWrapperDMN;
	}

	public ComissaoWrapperDMN findDemonstrativoFechamentoByCpf(Long cargoCpf, Integer mesCalculo, 
			Integer anoCalculo, String tipoComissao, String cargos) throws BusinessException{
		ComissaoWrapperDMN comissaoWrapperDMN = null;
		
		try {
			Client client = ClientBuilder.newClient();
			Invocation target;
			
			if (tipoComissao == null) {
				target = client
						.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
						.path(ComissionamentoEDWServiceEnum.FIND_DEMONSTRATIVO_FECHAMENTO.getPath())
						
						.queryParam("cargoCpf", cargoCpf)
						.queryParam("mesCalculo", mesCalculo)
						.queryParam("anoCalculo", anoCalculo)
						.queryParam("tipoCargo", cargos)
						
						.request(MediaType.APPLICATION_JSON)
						.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
						.buildGet();
				
			} else {
				target = client
						.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
						.path(ComissionamentoEDWServiceEnum.FIND_DEMONSTRATIVO_FECHAMENTO.getPath())
						
						.queryParam("cargoCpf", cargoCpf)
						.queryParam("mesCalculo", mesCalculo)
						.queryParam("anoCalculo", anoCalculo)
						.queryParam("tipoComissao", tipoComissao)
						.queryParam("tipoCargo", cargos)
						
						.request(MediaType.APPLICATION_JSON)
						.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
						.buildGet();
			}
			
	
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			comissaoWrapperDMN = mapper.readValue(in, ComissaoWrapperDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return comissaoWrapperDMN;
	}
	
	public ComissaoWrapperDMN findDemonstrativoFechamentoByTerritorio(Long idTerritorioFilial, Integer mesCalculo, 
			Integer anoCalculo, String cargos) throws BusinessException{
		ComissaoWrapperDMN comissaoWrapperDMN = null;
		
		try {
			Client client = ClientBuilder.newClient();
			
			Invocation target = client
					.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
					.path(ComissionamentoEDWServiceEnum.FIND_DEMONSTRATIVO_FECHAMENTO.getPath())
					
					.queryParam("territorioIdFilial", idTerritorioFilial)
					.queryParam("mesCalculo", mesCalculo)
					.queryParam("anoCalculo", anoCalculo)
					.queryParam("tipoCargo", cargos)
					
					.request(MediaType.APPLICATION_JSON)
					.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
					.buildGet();
	
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			comissaoWrapperDMN = mapper.readValue(in, ComissaoWrapperDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return comissaoWrapperDMN;
	}

	public ComissaoWrapperDMN findDemonstrativoFechamentoByTerritorio(Long idTerritorioFilial, Integer mesCalculo, 
			Integer anoCalculo, String tipoComissao, String cargos) throws BusinessException{
		ComissaoWrapperDMN comissaoWrapperDMN = null;
		
		try {
			Client client = ClientBuilder.newClient();
			
			Invocation target = client
					.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
					.path(ComissionamentoEDWServiceEnum.FIND_DEMONSTRATIVO_FECHAMENTO.getPath())
					
					.queryParam("territorioIdFilial", idTerritorioFilial)
					.queryParam("mesCalculo", mesCalculo)
					.queryParam("anoCalculo", anoCalculo)
					.queryParam("tipoComissao", tipoComissao)
					.queryParam("tipoCargo", cargos)
					
					.request(MediaType.APPLICATION_JSON)
					.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
					.buildGet();
	
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			comissaoWrapperDMN = mapper.readValue(in, ComissaoWrapperDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return comissaoWrapperDMN;
	}
	
	public ComissaoWrapperDMN findTotalDemonstrativoFechamentoByCpf(Long cargoCpf, Integer mesCalculo, 
			Integer anoCalculo, String tipoComissao, String cargos) throws BusinessException{
		ComissaoWrapperDMN comissaoWrapperDMN = null;
		
		try {
			Client client = ClientBuilder.newClient();
			
			Invocation target;

			if (tipoComissao == null) {
				target = client
						.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
						.path(ComissionamentoEDWServiceEnum.FIND_TOTAL_DEMONSTRATIVO_FECHAMENTO.getPath())
						
						.queryParam("cargoCpf", cargoCpf)
						.queryParam("mesCalculo", mesCalculo)
						.queryParam("anoCalculo", anoCalculo)
						.queryParam("tipoCargo", cargos)
						
						.request(MediaType.APPLICATION_JSON)
						.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
						.buildGet();
			} else {
				target = client
						.target(parametroGeralService.findByNomeParametro(PARAMETRO_SERVIDOR_EDW_SERVICE).getDsConteudo())
						.path(ComissionamentoEDWServiceEnum.FIND_TOTAL_DEMONSTRATIVO_FECHAMENTO.getPath())
						
						.queryParam("cargoCpf", cargoCpf)
						.queryParam("mesCalculo", mesCalculo)
						.queryParam("anoCalculo", anoCalculo)
						.queryParam("tipoComissao", tipoComissao)
						.queryParam("tipoCargo", cargos)
						
						.request(MediaType.APPLICATION_JSON)
						.header(AUTHORIZATION_HEADER_NAME, buildAuthorizationHeaderValue() )
						.buildGet();
			}
			
			Response response = target.invoke();
			String in = response.readEntity(String.class);
	
			ObjectMapper mapper = new ObjectMapper();
			comissaoWrapperDMN = mapper.readValue(in, ComissaoWrapperDMN.class);
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}

		return comissaoWrapperDMN;
	}
	
	
	private String buildAuthorizationHeaderValue(){
		
		String usuarioEsenha = USUARIO_EDW_SERVICE + ":" + parametroGeralService.findByNomeParametro(PARAMETRO_SENHA_EDW_SERVICE).getDsConteudo();
		
		return BASIC_AUTHOTIZATION + DatatypeConverter.printBase64Binary(usuarioEsenha.getBytes());
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
}
