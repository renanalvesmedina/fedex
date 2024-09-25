/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.HistoricoAwb;
import com.mercurio.lms.expedicao.model.service.HistoricoAwbService;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de Action que será utilizada principalmente pela tela 
 * /jsp/expedicao/manterHistoricoAWBsHistorico.jsp.
 * 
 * @author Luis Carlos Poletto
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.manterHistoricoAWBsAction"
 */
public class ManterHistoricoAWBsAction extends CrudAction {
	
	
	public TypedFlatMap findById(Long id) {
		TypedFlatMap result = new TypedFlatMap();
		
		HistoricoAwb historicoAwb = (HistoricoAwb) getHistoricoAwbService().findById(id);
		result.put("idHistoricoAwb", historicoAwb.getIdHistoricoAwb());
		result.put("blGerarMensagem", historicoAwb.getBlGerarMensagem());
		result.put("dsHistoricoAwb", historicoAwb.getDsHistoricoAwb());
		result.put("dhInclusao", historicoAwb.getDhInclusao());
		
		Usuario usuario = historicoAwb.getUsuario();
		result.put("usuario.idUsuario", usuario.getIdUsuario());
		result.put("usuario.nmUsuario", usuario.getNmUsuario());
		result.put("usuario.nrMatricula", usuario.getNrMatricula());
		
		return result;
	}

	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getHistoricoAwbService().findPaginated(criteria);
	}

	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getHistoricoAwbService().getRowCount(criteria);
	}

	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}
	
	public Serializable store(TypedFlatMap data) {
		HistoricoAwb historicoAwb = new HistoricoAwb();
		Long idAwb = data.getLong("awb.idAwb");
		if (idAwb != null) {
			Awb awb = new Awb();
			awb.setIdAwb(idAwb);
			historicoAwb.setAwb(awb);
		}
		
		Long idUsuario = data.getLong("usuario.idUsuario");
		if (idUsuario != null) {
			Usuario usuario = new Usuario();
			usuario.setIdUsuario(idUsuario);
			historicoAwb.setUsuario(usuario);
		}
		
		historicoAwb.setBlGerarMensagem(data.getBoolean("blGerarMensagem"));
		historicoAwb.setDhInclusao(data.getDateTime("dhInclusao"));
		historicoAwb.setDsHistoricoAwb(data.getString("dsHistoricoAwb"));
		
		Serializable result = getHistoricoAwbService().storeHistorico(historicoAwb);
		if (Boolean.TRUE.equals(historicoAwb.getBlGerarMensagem())) {
			getHistoricoAwbService().executeSendMailHistoricoAwb(historicoAwb);
    	}
		return result;
	}


	public TypedFlatMap findDadosSessao() {
		Usuario usuario = SessionUtils.getUsuarioLogado();
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("usuario.nmUsuario", usuario.getNmUsuario());
		result.put("usuario.nrMatricula", usuario.getNrMatricula());
		result.put("usuario.idUsuario", usuario.getIdUsuario());
		return result;
	}

	/*
	 * Getters e setters
	 */
	public void setService(HistoricoAwbService serviceService) {
		super.defaultService = serviceService;
	}
	
	public HistoricoAwbService getHistoricoAwbService() {
		return (HistoricoAwbService) super.defaultService;
	}

}
