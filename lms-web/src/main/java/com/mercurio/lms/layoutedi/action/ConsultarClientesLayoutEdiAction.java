package com.mercurio.lms.layoutedi.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.edi.model.service.ClienteLayoutEDIService;
import com.mercurio.lms.edi.model.service.RegistroLayoutEDIService;
import com.mercurio.lms.layoutedi.model.BuscaArquivosClientesWSRetorno;
import com.mercurio.lms.layoutedi.model.ClienteLayoutEdiWSRetorno;

/**
 * 
 * @author ThiagoFA
 * @spring.bean id="lms.layoutedi.ConsultarClientesLayoutEdiAction"
 */
@ServiceSecurity
public class ConsultarClientesLayoutEdiAction {
	private ClienteLayoutEDIService clienteLayoutEDIService;
	private RegistroLayoutEDIService registroLayoutEDIService;

	/**
	 * Lista os clientes com alteração no layout para geração do XSL Se o parametro TODOS for verdadeiro, lista todos clientes do EDI
	 * 
	 * @param todos
	 * @return
	 */
	@MethodSecurity(processGroup = "layoutedi.buscaArquivosWebService", processName = "listaClientesComAlteracaoCadastro", authenticationRequired = false)
	public BuscaArquivosClientesWSRetorno listaClientesComAlteracaoCadastro(Boolean todos) {
		if (todos) {
			return mapeiaClientesEDI(clienteLayoutEDIService.findClientesLayoutPorTipo(), Boolean.FALSE);
		} else {
			List<ClienteLayoutEDI> listaClientes = clienteLayoutEDIService.findClientesComAlteracaoLayout();
			return mapeiaClientesEDI(listaClientes, Boolean.FALSE);
		}
	}

	/**
	 * Lista todos clientes do EDI
	 * 
	 * @return
	 */
	@MethodSecurity(processGroup = "layoutedi.buscaArquivosWebService", processName = "listaClientesEdi", authenticationRequired = false)
	public BuscaArquivosClientesWSRetorno listaClientesEdi() {
		List<ClienteLayoutEDI> listaClientes = clienteLayoutEDIService.find(null);
		return mapeiaClientesEDI(listaClientes, Boolean.FALSE);
	}

	@MethodSecurity(processGroup = "layoutedi.buscaArquivosWebService", processName = "listaClientesEdiPorTipo", authenticationRequired = false)
	public BuscaArquivosClientesWSRetorno listaClientesEdiPorTipo(final Map params) {
		final List<ClienteLayoutEDI> listaClientes = clienteLayoutEDIService.findPorTipo(params);
		return mapeiaClientesEDI(listaClientes, Boolean.TRUE);
	}

	/**
	 * Busca um cliente EDI pelo id
	 * 
	 * @param todos
	 * @return
	 */
	@MethodSecurity(processGroup = "layoutedi.buscaArquivosWebService", processName = "buscaClienteEdi", authenticationRequired = false)
	public BuscaArquivosClientesWSRetorno buscaClienteEdi(Map params) {
		Long idClienteLayoutEdi = (Long) params.get("idClienteLayoutEDI");
		ClienteLayoutEDI cliente = clienteLayoutEDIService.findById(idClienteLayoutEdi);
		List<ClienteLayoutEDI> clientes = new ArrayList<ClienteLayoutEDI>();
		clientes.add(cliente);
		return mapeiaClientesEDI(clientes, Boolean.FALSE);
	}

	private BuscaArquivosClientesWSRetorno mapeiaClientesEDI(final List<ClienteLayoutEDI> clientes, final Boolean loadQuebraArquivo) {
		List<ClienteLayoutEdiWSRetorno> listaRetorno = new ArrayList<ClienteLayoutEdiWSRetorno>();
		for (ClienteLayoutEDI clienteLayoutEDI : clientes) {
			if (clienteLayoutEDI.getTransmissaoEDI() == null) {
				clienteLayoutEDI = clienteLayoutEDIService.findById(clienteLayoutEDI.getIdClienteLayoutEDI());
			}
			listaRetorno.add(populaClienteLayoutWS(clienteLayoutEDI, loadQuebraArquivo));
		}

		final BuscaArquivosClientesWSRetorno retorno = new BuscaArquivosClientesWSRetorno();
		retorno.setClientes(listaRetorno);
		return retorno;
	}

	private ClienteLayoutEdiWSRetorno populaClienteLayoutWS(final ClienteLayoutEDI cliente, final Boolean loadQuebraArquivo) {
		ClienteLayoutEdiWSRetorno cliWS = new ClienteLayoutEdiWSRetorno();
		cliWS.setFtpCaminho(cliente.getFtpCaminho());
		cliWS.setFtpUser(cliente.getFtpUser());
		cliWS.setFtpSenha(cliente.getFtpSenha());
		cliWS.setIdTransmissaoEDI(cliente.getTransmissaoEDI().getIdTipoTransmissaoEDI());
		cliWS.setIdLayoutEDI(cliente.getLayoutEDI().getIdLayoutEdi());
		cliWS.setIdClienteLayoutEDI(cliente.getIdClienteLayoutEDI());
		cliWS.setNmPasta(cliente.getNmPasta());
		cliWS.setNomeArquivo(cliente.getNomeArquivo());
		cliWS.setFtpServidor(cliente.getFtpServidor());
		cliWS.setBlMigrado(cliente.getBlMigrado());
		if (Boolean.TRUE.equals(loadQuebraArquivo)) {
			cliWS.setQuebraArquivo(registroLayoutEDIService.findDadosQuebraArquivoMapped(cliente.getLayoutEDI().getIdLayoutEdi()));
		}
		return cliWS;
	}

	/**
	 * @param clienteLayoutEDIService
	 *            the clienteLayoutEDIService to set
	 */
	public void setClienteLayoutEDIService(ClienteLayoutEDIService clienteLayoutEDIService) {
		this.clienteLayoutEDIService = clienteLayoutEDIService;
	}

	/**
	 * @param registroLayoutEDIService
	 *            the registroLayoutEDIService to set
	 */
	public void setRegistroLayoutEDIService(RegistroLayoutEDIService registroLayoutEDIService) {
		this.registroLayoutEDIService = registroLayoutEDIService;
	}
}