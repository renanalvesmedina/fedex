package com.mercurio.lms.services.expedicao;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalWrapperDMN;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIVolumeService;
import com.mercurio.lms.expedicao.edi.model.service.CCEService;
import com.mercurio.lms.expedicao.edi.model.service.NotaFiscalEDIService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.McdService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.vendas.model.service.ClienteService;
import javax.ws.rs.core.Response;

@Path("/expedicao/notaFiscal")
public class NotaFiscalRest {

	private static final String SEQUENCIAL_CAE = "SEQUENCIAL_CAE";
	private static final Short ROTA_ZERO = Short.valueOf("0");
	private final Log log = LogFactory.getLog(NotaFiscalRest.class);


	@InjectInJersey
	NotaFiscalEDIVolumeService notaFiscalEDIVolumeService;

	@InjectInJersey
	NotaFiscalEDIService notaFiscalEDIService;

	@InjectInJersey
	ClienteService clienteService;

	@InjectInJersey
	RotaColetaEntregaService rotaColetaEntregaService;

	@InjectInJersey
	ConfiguracoesFacade configuracoesFacade;

	@InjectInJersey
	McdService mcdService;
	
	@InjectInJersey
	CCEService CCEService;
	
	@InjectInJersey
	FilialService filialService;
	
	@InjectInJersey
	UsuarioService usuarioService;
    
    @InjectInJersey
    DispositivoUnitizacaoService dispositivoUnitizacaoService;

	
	@GET
	@Produces("application/json")
	public NotaFiscalWrapperDMN findNotaFiscalEdiByUltimoIdImportado(@QueryParam("idNotaFiscal")Long idNotaFiscalEdi, @QueryParam("cnpjCliente")String cnpjCliente){		
		NotaFiscalWrapperDMN notaFiscalEdiWrapperDMN = notaFiscalEDIService.executeFindNotaFiscalEdiByUltimoIdImportado(idNotaFiscalEdi, cnpjCliente);
		return notaFiscalEdiWrapperDMN;
	}
	
	@POST
	@Path("/salvarDivergencia")
	@Produces("application/json")
	public Response salvarDivergencia(NotaFiscalWrapperDMN notaFiscalWrapperDMN){		
		CCEService.storeCCE(notaFiscalWrapperDMN);
		return Response.ok().build();
	}
	

	@GET
	@Path("/proximoCCE")
	@Produces("application/json")
	public Long proximoCCE(@QueryParam("login")String login){
		Usuario usuarioSolicitante = usuarioService.findUsuarioByLogin(login);
		return CCEService.storeProximoCCE(usuarioSolicitante);
	}

	@GET
	@Produces("application/json")
	@Path("/nextCae")
	public Long findNextCae(@QueryParam("sgFilial")String sgFilial){		
		Filial filial = filialService.findBySgFilialAndTpEmpresa(sgFilial, "M");		
		return configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), SEQUENCIAL_CAE, true);
	}
	
	@GET
	@Path("/proximoDispositivo")
	@Produces("application/json")
	public String generateProximoNumeroDispositivo(@QueryParam("cnpj") String cnpj, @QueryParam("tpDispositivo") String tpDispositivo) {		
		return dispositivoUnitizacaoService.generateProximoNumeroDispositivo(cnpj, tpDispositivo);
	}


}
