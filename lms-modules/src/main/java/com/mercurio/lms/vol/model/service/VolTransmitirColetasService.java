package com.mercurio.lms.vol.model.service;

import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.vol.utils.VolFomatterUtil;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volTransmitirColetasService"
 */
public class VolTransmitirColetasService {
	private ManifestoColetaService manifestoColetaService;
	private VolLogEnviosSmsService volLogEnviosSmsService;
	private FilialService filialService;
	private UsuarioService usuarioService;
	private PaisService paisService;
	private VolDadosSessaoService volDadosSessaoService;
	private PedidoColetaService pedidoColetaService;
	
	public Map findColetas(TypedFlatMap map){
		Long idControleCarga = map.getLong("idControleCarga");
		Long idEquipamento = map.getLong("idEquipamento");
		
		Map retorno =manifestoColetaService.findColetasToMobile(idControleCarga);
		volLogEnviosSmsService.updatePendentes(idEquipamento);
		return retorno;
	}
	
	
	public void storeConfirmaRecebimentoColeta(TypedFlatMap map){
		
    	/**
    	 * seta os dados na sessão
    	 */
		Filial filial = filialService.findById(map.getLong("idFilial"));
		Usuario usuario = usuarioService.findUsuarioByLogin("vol");
		Pais pais = paisService.findByIdPessoa( filial.getIdFilial() );
		volDadosSessaoService.setDadosSessaoBanco(usuario, filial, pais);
		
		PedidoColeta pedidoColeta = pedidoColetaService.findById(map.getLong("idPedidoColeta"));
		pedidoColeta.setBlConfirmacaoVol(true);
		
		DateTime dhConfirmacaoVol =  VolFomatterUtil.formatStringToDateTime(map.getString("dhConfirmacaoVol"));
		pedidoColeta.setDhConfirmacaoVol(dhConfirmacaoVol);
		
		pedidoColetaService.store(pedidoColeta);
		
	}
	
	
	public void setManifestoColetaService(
			ManifestoColetaService manifestoColetaService) {
		this.manifestoColetaService = manifestoColetaService;
	}
	public void setVolLogEnviosSmsService(
			VolLogEnviosSmsService volLogEnviosSmsService) {
		this.volLogEnviosSmsService = volLogEnviosSmsService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
}


	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}


	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}


	public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
		this.volDadosSessaoService = volDadosSessaoService;
	}


	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
}
