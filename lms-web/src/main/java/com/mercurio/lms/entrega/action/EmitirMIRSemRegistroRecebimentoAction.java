package com.mercurio.lms.entrega.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.entrega.model.service.MirService;
import com.mercurio.lms.entrega.report.EmitirMIRSemRegistroRecebimentoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.emitirMIRSemRegistroRecebimentoAction"
 */

public class EmitirMIRSemRegistroRecebimentoAction extends ReportActionSupport {

	private EmitirMIRSemRegistroRecebimentoService emitirMIRSemRegistroRecebimentoService;
	
	public EmitirMIRSemRegistroRecebimentoService getEmitirMIRSemRegistroRecebimentoService() {
		return emitirMIRSemRegistroRecebimentoService;
	}

	public void setEmitirMIRSemRegistroRecebimentoService(EmitirMIRSemRegistroRecebimentoService emitirMIRSemRegistroRecebimentoService) {
		this.reportServiceSupport = emitirMIRSemRegistroRecebimentoService;
	}
	
	private FilialService filialService;
	private UsuarioService usuarioService;
	private ClienteService clienteService;
	private MirService mirService;
	
	@Override
	public java.io.File execute(TypedFlatMap parameters) throws Exception {
		
		Long idFilialOrigem = parameters.getLong("filialByIdFilialOrigem.idFilial");
		Long idFilialDestino = parameters.getLong("filialByIdFilialDestino.idFilial");
		Filial filialSessao = SessionUtils.getFilialSessao();
		Long idFilialSessao = filialSessao.getIdFilial();
		
		
		if (idFilialOrigem == null) idFilialOrigem = Long.valueOf(-1);
		if (idFilialDestino == null) idFilialDestino = Long.valueOf(-1);
		
		//- Caso a filial padr�o do usu�rio n�o for matriz (FILIAL.TP_FILIAL -> HISTORICO_FILIAL <> 'MA'):
		//	- Caso preenchida a MIR verificar se a MIR informada � originada ou destinada a filial padr�o do usu�rio logado. Se n�o for mostrar mensagem LMS-09037 e abortar a opera��o
		if (! SessionUtils.isFilialSessaoMatriz()) {
			
			if (!idFilialOrigem.equals(idFilialSessao) && !idFilialDestino.equals(idFilialSessao)) {
				throw new BusinessException("LMS-09037");
			}
		}
		
		return super.execute(parameters);
	}
	
	/**
	 * find da combo mir.
	 * @param criteria
	 * @return List com combos encontradas.
	 */
	public List findLookupMir(Map criteria) {
		return mirService.findLookup(criteria);
	}
	
	/**
	 * find da lookup de filial.
	 * @param criteria
	 * @return List com filiais encontradas.
	 */
	public List findLookupFilial(Map criteria) {
		return filialService.findLookupFilial(criteria);
	}
	
	/**
	 * find da lookup de filial.
	 * @param criteria
	 * @return List com filiais encontradas.
	 */
	public List findLookupCliente(Map criteria) {
		return clienteService.findLookup(criteria);
	}
	
	
	/**
	 * Find da lookup de funcion�rio.
	 * @param criteria
	 * @return List com usu�rios encontrados.
	 */
	public List findLookupUsuarioFuncionario(TypedFlatMap criteria) {
		Long idUsuario = criteria.getLong("idUsuario");
		String nrMatricula = criteria.getString("nrMatricula");
		Long idFilial = criteria.getLong("filial.idFilial");
		
		return usuarioService.findLookupUsuarioFuncionario(idUsuario,nrMatricula,idFilial,null,null,null,true);
	}

	
	public TypedFlatMap findFilialUsuarioLogado() {
    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	Filial f = SessionUtils.getFilialSessao();
    	retorno.put("idFilial",f.getIdFilial());
    	retorno.put("sgFilial",f.getSgFilial());
    	retorno.put("pessoa.nmFantasia",f.getPessoa().getNmFantasia());
    	
    	return retorno;
    }
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setMirService(MirService mirService) {
		this.mirService = mirService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

}
