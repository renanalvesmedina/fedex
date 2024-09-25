package com.mercurio.lms.vol.model.service;

import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.vol.model.dao.VolDadosSessaoDao;

import java.util.concurrent.ExecutionException;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volDadosSessaoService"
 */
public class VolDadosSessaoService {
	private VolDadosSessaoDao volDadosSessaoDao;	
	private HistoricoFilialService historicoFilialService;
	private MoedaService moedaService;
	
	/**
	 * Correção de bug na integração quando as baixas são enviadas para o corporativo. O id da filial na sessão do banco
	 * não estava sendo atualizada.
	 * @param idFilial
	 */
	public void setDadosSessaoBanco(Usuario usuario, Filial filial, Pais pais) {
		this.setApplicationSession(usuario, filial, pais);

		String dadosSessao = "f>".concat(filial.getIdFilial().toString()).concat("|u>".concat(usuario.getIdUsuario().toString())).concat("|login>".concat(usuario.getLogin()))
		.concat("|to>-03|tl>1|i>0|");
		
		volDadosSessaoDao.setDadosSessaoBanco( dadosSessao );
	}
	
	public void executeDadosSessaoBanco(Usuario usuario, Filial filial, Pais pais) {
		setDadosSessaoBanco(usuario, filial, pais);
	}
	
	public void setDadosSessaoBanco( Long idFilial, Long idUsuario, String login, String complemento ) {
		String dadosSessao = "f>".concat(idFilial.toString()).concat("|u>".concat(idUsuario.toString())).concat("|login>".concat(login));
		if(complemento == null)
			dadosSessao = dadosSessao + "|to>-03|tl>1|i>0|";
		else
			dadosSessao = dadosSessao + complemento;
	
		volDadosSessaoDao.setDadosSessaoBanco( dadosSessao );
	}
	
	private void setApplicationSession(Usuario usuario, Filial filial, Pais pais){
		try {
			SessionContext.setUser(usuario);
			SessionContext.set("PAIS_KEY", pais);
			SessionContext.set("MOEDA_KEY", moedaService.findById(filial.getMoeda().getIdMoeda()));
			SessionContext.set("FILIAL_KEY", filial);

			SessionContext.set(SessionKey.ULT_HIST_FILIAL_KEY, getHistoricoFilialService().findUltimoHistoricoFilial(filial.getIdFilial()));
			SessionContext.set(SessionKey.PEN_HIST_FILIAL_KEY, getHistoricoFilialService().getPenultimoHistoricoFilial(filial.getIdFilial()));
			SessionContext.set(SessionKey.FILIAL_MATRIZ_KEY, getHistoricoFilialService().validateFilialUsuarioMatriz(filial.getIdFilial()));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
	
	public HistoricoFilialService getHistoricoFilialService() {
		return historicoFilialService;
	}

	public void setVolDadosSessaoDao(VolDadosSessaoDao volDadosSessaoDao) {
		this.volDadosSessaoDao = volDadosSessaoDao;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
}
