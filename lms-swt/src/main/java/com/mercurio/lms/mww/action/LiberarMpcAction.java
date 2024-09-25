package com.mercurio.lms.mww.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.carregamento.model.LiberaMpc;
import com.mercurio.lms.carregamento.model.service.HistoricoMpcService;
import com.mercurio.lms.carregamento.model.service.LiberarMpcService;
import com.mercurio.lms.gm.model.service.VolumeService;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.mww.liberarMpcAction"
 */
public class LiberarMpcAction extends CrudAction {

	private LiberarMpcService liberarMpcService;
	private VolumeService volumeService;
	private HistoricoMpcService historicoMpcService;

	public LiberaMpc findRejeitoMpcById(Long idLiberaMpc) {
		return (LiberaMpc) liberarMpcService.findById(idLiberaMpc);
	}

	/**
	 * Retorna o uma lista de Liberar Mpc de acordo com a situa��o
	 */
	public Map<String, Object> findListaLiberarMpc(Map param) {
		Map<String, Object> mapRoot = new HashMap<String, Object>();
		List<Map<String, Object>> liberarMpc = liberarMpcService.findLiberarMpcBySituacao(param);

		mapRoot.put("listaLiberaMpc", liberarMpc);

		return mapRoot;
	}

	/**
	 * Este metodo � chamado na tela de LiberaMpc no MWW. Ele � responsav�l por
	 * liberar um carregamento que esteja com pend�ncias. O m�todo ent�o chama
	 * os servi�os necess�rios para fazer os updates dos volumes e hist�rico
	 * volumes e creates do hist�rico MpC.
	 * 
	 * Demanda LMS-2791
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> storeVolumesLiberaMpc(Map map) {
		String usuario = (String) map.get("matriculaChefia");
		Long usuarioLogado = Long.parseLong(usuario);

		String idAutorizador = (String) map.get("idUsuarioAutorizador");
		
		Long idUsuarioAutorizador = null;
		if (idAutorizador != null && !idAutorizador.equals("")) {
			idUsuarioAutorizador = Long.parseLong(idAutorizador);
		}

		String id = (String) map.get("idCarregamento");
		Long idCarregamento = Long.parseLong(id);

		String mpc = (String) map.get("mapaCarregamento");
		Long mpcDaConferencia = null;

		String idLiberaMPC = (String) map.get("idLiberaMPC");
		Long idDescricaoLiberaMpc = Long.parseLong(idLiberaMPC);

		if (mpc != null) {
			mpcDaConferencia = Long.parseLong(mpc);
		}

		// Faz um update do status do volume e grava um hist�rico dos volumes
		Map<String, Object> carregamentoVolumes = volumeService.storeCarregamentoVolumes(idCarregamento, usuarioLogado, mpcDaConferencia, idUsuarioAutorizador);

		// Gera um log do MPC
		historicoMpcService.storeHistoricoMpc(idCarregamento, usuarioLogado, mpcDaConferencia, idUsuarioAutorizador, idDescricaoLiberaMpc);

		return carregamentoVolumes;

	}

	// / SETTER ///

	public void setLiberarMpcService(LiberarMpcService liberarMpcService) {
		this.liberarMpcService = liberarMpcService;
	}

	public void setVolumeService(VolumeService volumeService) {
		this.volumeService = volumeService;
	}

	public void setHistoricoMpcService(HistoricoMpcService historicoMpcService) {
		this.historicoMpcService = historicoMpcService;
	}
}
