package com.mercurio.lms.rest.tabeladeprecos;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mercurio.adsm.framework.security.model.service.AuthenticationService;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.facade.reajuste.cliente.ReajusteDeClienteFacade;
import com.mercurio.lms.tabelaprecos.model.CloneClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteDivisaoCliente;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteDivisaoClienteService;
import com.mercurio.lms.vendas.model.service.ReajusteClienteAsyncService;

@Public
@Path("/tabeladeprecos/reajusteCliente")
public class ReajusteClienteRest  {
	
	private static final Log LOG = LogFactory.getLog(ReajusteClienteRest.class);
	private static final String SIM = "S";
	private static final String NAO = "N";
	private static final String ERRO_REAJUSTE_PARAMETROS = "ReajustarClienteAutomatico - IdTabelaDivisaoCliente: ";
	private static final String REAJUSTADA = "REAJUSTADA";
	private static final String CLONADA = "CLONADA";
	
	@InjectInJersey ReajusteDeClienteFacade reajusteDeClienteFacade;
	@InjectInJersey ReajusteClienteAsyncService reajusteClienteAsyncService;
	@InjectInJersey ParametroGeralService parametroGeralService;
	@InjectInJersey ReajusteDivisaoClienteService reajusteDivisaoClienteService;
	@InjectInJersey AuthenticationService authenticationService;
	
	@GET
	@Path("executeAllReajuste")
	public Response efetivar()  throws Exception {
		
		if (isNovoReajuste()) {
			
			loginUsuarioReajuste();
			
			List<CloneClienteAutomaticoDTO> listClones = reajusteDeClienteFacade.findTabelasDivisaoClienteAutomaticosParaClonarNovoReajuste();
			
			for (CloneClienteAutomaticoDTO cloneClienteAutomaticoDTO : listClones) {
				ReajusteDivisaoCliente rdc = reajusteDivisaoClienteService.findById(cloneClienteAutomaticoDTO.getIdReajusteDivisaoCliente());
				rdc.setIsProcessado(SIM);
				
				try {
					Map<String, Boolean> retorno = reajusteClienteAsyncService.executeNovoReajuste(cloneClienteAutomaticoDTO);
					
					boolean clonada = retorno.get(CLONADA);
					boolean reajustada = retorno.get(REAJUSTADA);
					
					rdc.setIsReajustada(reajustada ? SIM : NAO);
					rdc.setIsClonada(clonada ? SIM : NAO);
					
				} catch (Exception e) {
					LOG.error(ERRO_REAJUSTE_PARAMETROS + cloneClienteAutomaticoDTO.getIdTabelaDivisaoCliente(), e);
				}
				
				reajusteDivisaoClienteService.store(rdc);
			}

		} else {
			
			List<CloneClienteAutomaticoDTO> listClones = reajusteDeClienteFacade.findTabelasDivisaoClienteAutomaticosParaClonar();

			for (CloneClienteAutomaticoDTO cloneClienteAutomaticoDTO : listClones) {
				reajusteDeClienteFacade.cloneParametroCliente(cloneClienteAutomaticoDTO);
			}
			
			reajusteDeClienteFacade.reajustarClientesAutomaticos();
		}
		
		return Response.ok(System.currentTimeMillis()).build();
	}
	
	public void loginUsuarioReajuste() {
		String login = (String)parametroGeralService.findConteudoByNomeParametro("USUARIO_ENVIO_FATURAS", false);
	    authenticationService.loginAsSystem(login);
	}
	
	private boolean isNovoReajuste() {
		ParametroGeral parametro = parametroGeralService.findByNomeParametro("NOVO_REAJUSTE");
		return parametro != null && "S".equalsIgnoreCase(parametro.getDsConteudo());
	}
	
	@POST
	@Path("cloneParametroCliente")
	public boolean cloneParametroCliente(CloneClienteAutomaticoDTO reajusteClienteAutomaticoDTO){
		return reajusteDeClienteFacade.cloneParametroCliente(reajusteClienteAutomaticoDTO);
	}

	@POST
	@Path("findTabelasDivisaoClienteAutomaticosParaClonar")
	public List<CloneClienteAutomaticoDTO> findTabelasDivisaoClienteAutomaticosParaClonar() throws Exception{
		return reajusteDeClienteFacade.findTabelasDivisaoClienteAutomaticosParaClonar();
	}

	@POST
	@Path("findTabelasDivisaoClienteAutomaticosParaReajustar")
	public List<Long> findTabelasDivisaoClienteAutomaticosParaReajustar() throws Exception{
		return reajusteDeClienteFacade.findTabelasDivisaoClienteAutomaticosParaReajustar();
	}

	@POST
	@Path("reajustarClienteAutomatico")
	public boolean reajustarClienteAutomatico(ReajusteClienteAutomaticoDTO parametroCliente) throws Exception{
		return reajusteDeClienteFacade.reajustarClienteAutomatico(parametroCliente);
	}

	@POST
	@Path("reajustarClientesAutomaticos")
	public boolean reajustarClientesAutomaticos() throws Exception{
		return reajusteDeClienteFacade.reajustarClientesAutomaticos();
	}
	
	@GET
	@Path("reajustarClientesAutomaticosNovoReajuste")
	public Response reajustarClientesAutomaticosNovoReajuste() throws Exception{
		reajusteDeClienteFacade.reajustarClientesAutomaticosNovoReajuste();
		return Response.ok(System.currentTimeMillis()).build();
	}
	
	@GET
	@Path("atualizarTabelasDivisaoEHistoricoReajuste")
	public boolean atualizarTabelasDivisaoEHistoricoReajuste() throws Exception{
		return reajusteDeClienteFacade.atualizarTabelasDivisaoEHistoricoReajuste(isNovoReajuste());
	}

}
