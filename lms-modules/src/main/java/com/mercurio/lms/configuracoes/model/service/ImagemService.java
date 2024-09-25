package com.mercurio.lms.configuracoes.model.service;

import br.com.tntbrasil.integracao.domains.jms.HeaderParam;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.utils.EventoSistemaLmsDMN;
import br.com.tntbrasil.integracao.domains.utils.EventoSistemaLmsType;
import br.com.tntbrasil.integracao.domains.utils.MethodRefreshCacheLms;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Imagem;
import com.mercurio.lms.configuracoes.model.dao.ImagemDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;

/**
 * Classe de serviço para CRUD: 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.imagemService"
 */
public class ImagemService extends CrudService<Imagem, Long> {

	private IntegracaoJmsService integracaoJmsService;

	public void refreshAllServers() {
		EventoSistemaLmsDMN refreshCacheLmsDmn = new EventoSistemaLmsDMN();
		refreshCacheLmsDmn.setMethod(MethodRefreshCacheLms.PARAMETRO_GERAL.getName());
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.EVENTO_SISTEMA_LMS, refreshCacheLmsDmn);
		msg.addHeader(HeaderParam.EVENT_TYPE.getName(), EventoSistemaLmsType.REFRESH_CACHE.getName());
		integracaoJmsService.storeMessage(msg);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	private ImagemDAO getImagemDAO() {
		return (ImagemDAO) getDao();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param dao intancia do DAO.
	 */
	public  void setImagemDao(ImagemDAO dao){
		setDao(dao);
	}

	/**
	 * Recupera uma instância de <code>Imagem</code> a partir do ID.
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public Imagem findById(Long id) {
		return (Imagem) super.findById(id);
	}

	/**
	 * Busca uma Imagem través do nome da chave.
	 *
	 *  @param chave nome do parametro
	 * @param realizaLock realiza lock pessimista no registro
	 * @return o Imagem com a chave informado.
	 */
	public Imagem findByChave(String chave, boolean realizaLock) {
		Imagem imagem = getImagemDAO().findImagemByChave(chave, realizaLock);

		if (imagem == null) {
			if (log.isDebugEnabled()) {
				log.debug(new StringBuilder("Imagem não encontrada. Chave='").append(chave).append("' - retornando exceção LMS-27051.").toString());
			}
			return new Imagem();
		}

		return imagem;
	}

	/**
	 * Busca uma imagem através de uma chave.
	 * @param chave nome da imagem
	 * @return o Imagem com o nome informado.
	 */
	public Imagem findByChave(String chave) {
		return getImagemDAO().findImagemByChave(chave, false);
	}



	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}