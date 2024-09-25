package com.mercurio.lms.vendas.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchTransactionType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.security.model.service.AuthenticationService;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.facade.reajuste.cliente.ReajusteDeClienteFacade;
import com.mercurio.lms.tabelaprecos.model.CloneClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteDivisaoCliente;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteDivisaoClienteService;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;

@Assynchronous
public class ReajusteClienteAsyncService {

	private static final String SIM = "S";
	private static final String NAO = "N";
	private static final String IND_NOVO_REAJUSTE_EXE = "IND_NOVO_REAJUSTE_EXE";
	private static final String ERRO_REAJUSTE_PARAMETROS = "ReajustarClienteAutomatico - IdTabelaDivisaoCliente: ";
	private static final String REAJUSTADA = "REAJUSTADA";
	private static final String CLONADA = "CLONADA";
	
	private static final Log LOG = LogFactory.getLog(ReajusteClienteAsyncService.class);
	
	private ReajusteClienteService service;
	private ReajusteDeClienteFacade reajusteDeClienteFacade;
	private UsuarioService usuarioService;
	private VolDadosSessaoService volDadosSessaoService;
	private ReajusteDivisaoClienteService reajusteDivisaoClienteService;
	private ParametroGeralService parametroGeralService;
	private AuthenticationService authenticationService;
	
	@AssynchronousMethod(name="reajuste.asyncEfetivacao", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ALWAYS)
	public void executeAsyncEfetivacao() {
		service.executeAsyncEfetivacao();
	}
	
	@AssynchronousMethod(name="reajuste.cloneAndReajuste", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ALWAYS, transaction = BatchTransactionType.NON_TRANSACTIONAL)
	public void cloneAndReajuste(){
		
		if(!isNovoReajusteEmExecucao()){
			
			loginUsuarioReajuste();

			parametroGeralService.storeValorParametro(IND_NOVO_REAJUSTE_EXE, SIM);
			
			List<CloneClienteAutomaticoDTO> listClones = reajusteDeClienteFacade.findTabelasDivisaoClienteAutomaticosParaClonarNovoReajuste();
			
			for (CloneClienteAutomaticoDTO cloneClienteAutomaticoDTO : listClones) {
				
				
				ReajusteDivisaoCliente rdc = reajusteDivisaoClienteService.findById(cloneClienteAutomaticoDTO.getIdReajusteDivisaoCliente());
				rdc.setIsProcessado(SIM);
				
				try {
					Map<String, Boolean> retorno = this.executeNovoReajuste(cloneClienteAutomaticoDTO);
					
					boolean clonada = retorno.get(CLONADA);
					boolean reajustada = retorno.get(REAJUSTADA);
					
					rdc.setIsReajustada(reajustada ? SIM : NAO);
					rdc.setIsClonada(clonada ? SIM : NAO);
					
				} catch (Exception e) {
					LOG.error(ERRO_REAJUSTE_PARAMETROS + cloneClienteAutomaticoDTO.getIdTabelaDivisaoCliente(), e);
				}
				
				reajusteDivisaoClienteService.store(rdc);
			}
			
			parametroGeralService.storeValorParametro(IND_NOVO_REAJUSTE_EXE, NAO);
			
		}
		
	}
	
	public void loginUsuarioReajuste() {
		String login = (String)parametroGeralService.findConteudoByNomeParametro("USUARIO_ENVIO_FATURAS", false);
	    authenticationService.loginAsSystem(login);
	}
	
	private boolean isNovoReajusteEmExecucao() {
		ParametroGeral parametro = parametroGeralService.findByNomeParametro("IND_NOVO_REAJUSTE_EXE");
		return parametro != null && SIM.equalsIgnoreCase(parametro.getDsConteudo());
	}
	
	public Map<String, Boolean> executeNovoReajuste(CloneClienteAutomaticoDTO cloneClienteAutomaticoDTO) throws Exception {
		Map<String, Boolean> retorno = new HashMap<String, Boolean>();
		
		boolean clonada = reajusteDeClienteFacade.cloneParametroCliente(cloneClienteAutomaticoDTO);
		retorno.put(CLONADA, clonada);
 		
		if(clonada){
			boolean reajustada = reajusteDeClienteFacade.reajustarClientesAutomaticosNovoReajuste();
			retorno.put(REAJUSTADA, reajustada);
		}
		
		return retorno;
	}
	
	@AssynchronousMethod(name="reajuste.executeAsyncAtualizarTabDivisao", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ALWAYS)
	public void executeAsyncAtualizarTabDivisao() throws Exception{
		Usuario usuario = usuarioService.findUsuarioByLogin("Reajuste.Cliente");	
		volDadosSessaoService.setDadosSessaoBanco(usuario.getEmpresaPadrao().getIdEmpresa(), usuario.getIdUsuario(), usuario.getLogin(), "|to>-03|tl>0|i>0|");
	
		reajusteDeClienteFacade.atualizarTabelasDivisaoEHistoricoReajuste(isNovoReajuste());
	}
	
	private boolean isNovoReajuste() {
		ParametroGeral parametro = parametroGeralService.findByNomeParametro("NOVO_REAJUSTE");
		return parametro != null && SIM.equalsIgnoreCase(parametro.getDsConteudo());
	}
	
	public void setService(ReajusteClienteService service) {
		this.service = service;
	}
		
	public void setReajusteDeClienteFacade(ReajusteDeClienteFacade reajusteDeClienteFacade) {
		this.reajusteDeClienteFacade = reajusteDeClienteFacade;
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
		this.volDadosSessaoService = volDadosSessaoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setReajusteDivisaoClienteService(ReajusteDivisaoClienteService reajusteDivisaoClienteService) {
		this.reajusteDivisaoClienteService = reajusteDivisaoClienteService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

}
