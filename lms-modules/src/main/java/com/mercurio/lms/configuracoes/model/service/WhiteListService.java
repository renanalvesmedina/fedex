package com.mercurio.lms.configuracoes.model.service;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.*;
import com.mercurio.lms.configuracoes.model.Imagem;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.DispositivoContato;
import com.mercurio.lms.configuracoes.model.WhiteList;
import com.mercurio.lms.configuracoes.model.dao.WhiteListDAO;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.service.utils.EventoDoctoServicoHelper;
import com.mercurio.lms.tracking.service.LinhaTempoService;
import com.mercurio.lms.tracking.util.CharactersUtils;
import com.mercurio.lms.tracking.util.TrackingContantsUtil;
import com.mercurio.lms.util.EncryptionUtils;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.push.NotificationDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.sim.StatusLinhaTempoRastreabilidade;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.whitelistService"
 */
public class WhiteListService extends CrudService<WhiteList, Long> {

	private static final int POSICAO_LOCALIZACAO_MERCADORIA = 4;

	public static final String TEXT_HTML= "text/html; charset='utf-8'";
	private static final String DM_TP_ENVIO_WHITE_LIST = "DM_TP_ENVIO_WHITE_LIST";
	private static final String DM_STATUS = "DM_STATUS";
	private static final String DM_TP_WHITE_LIST = "DM_TP_WHITE_LIST";
	private static final String RADAR_WEB = "W";
	private static final String RADAR_MOBILE = "M";
	private static final String LOGO_FEDEX = "logoFedEx";
	private LinhaTempoService linhaTempoService;
	private DomainValueService domainValueService;
	private ContatoService contatoService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	private DoctoServicoService doctoServicoService;
	private UsuarioADSMService usuarioADSMService;
	private IntegracaoJmsService integracaoJmsService;
	private ImagemService imagemService;

	/**
	 * Recupera uma instância de <code>WhiteList</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public WhiteList findById(java.lang.Long id) {
		return (WhiteList) super.findById(id);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param dao .
	 */
	public void setWhiteListDAO(WhiteListDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private WhiteListDAO getWhiteListDAO() {
		return (WhiteListDAO) getDao();
	}

	public WhiteList executeAtivacaoWhiteList(Long idWhiteList, DomainValue status) {
		WhiteList whiteList = findById(idWhiteList);
		if(whiteList != null){
			whiteList.setSituacaoWhiteList(status);
			store(whiteList);
		}
		return whiteList;
	}


	/**
	 * Metodo resposavel por realizar o disparo de eventos para os clientes que acompanham a carga.
	 * @param eventoDoctoServico = Objeto contem informações do evento
	 * @return List<EventoWhiteListRastreabilidade> - contem lista de whitelist encontrado
	 * @throws Exception
	 */
	public List<WhiteList> executeWhiteListRastreabilidade(EventoDocumentoServicoDMN eventoDoctoServico) {
		List<WhiteList> listWhiteList = null;

		// verifica se o evento é para o cliente ser notificado.
		if(isNotificaCliente(eventoDoctoServico)){
			listWhiteList = getListWhiteList(eventoDoctoServico);
			List<NotaFiscalConhecimento> lstNotasFiscais = null;
			StatusLinhaTempoRastreabilidade statusLinhaTempo = null;

			if(listWhiteList != null && !listWhiteList.isEmpty()){
				statusLinhaTempo = findLinhaTempoEvento(listWhiteList, eventoDoctoServico);
				lstNotasFiscais = doctoServicoService.findNotasFiscaisByDoctoServico(listWhiteList.get(0).getDoctoServico());
			}

			for (WhiteList wl : listWhiteList) {



				if(!isNotifySigameByTipoEnvio(wl, statusLinhaTempo)){
					continue;
				}

				//so define objeto mail se o tipo de notificação for por e-mail
				if(TrackingContantsUtil.WHITE_LIST_TIPO_EMAIL.equals(wl.getTpWhiteList().getValue())){
					Mail mail = setMailWhiteList(wl, eventoDoctoServico, statusLinhaTempo, lstNotasFiscais);
					// dispara objeto Mail para fila JMS de mail.
					senderObjMailJMS(mail);

				} else if(TrackingContantsUtil.WHITE_LIST_TIPO_PUSH.equals(wl.getTpWhiteList().getValue())){
					sendPushNotification(eventoDoctoServico, wl);
				}
			}
		}
		return listWhiteList;
	}

	/**
	 * Metodo resposavel por realizar o disparo de eventos para os clientes que
	 *  acompanham a carga asim que insere um registro na ehiteList.
	 * @param listWhiteList
	 * @return List<EventoWhiteListRastreabilidade> - contem lista de whitelist encontrado
	 * @throws Exception
	 */
	public String executeWhiteListRastreabilidadeRealTime(List<WhiteList> listWhiteList){
		String emailsSucesso =  null;
		// verifica se contem whiteList a serem disparados
		if(listWhiteList!=null && !listWhiteList.isEmpty()){
			List<NotaFiscalConhecimento> lstNotasFiscais = doctoServicoService.findNotasFiscaisByDoctoServico(listWhiteList.get(0).getDoctoServico());
			StatusLinhaTempoRastreabilidade statusLinhaTempo = findLinhaTempoEvento(listWhiteList);
			for (WhiteList wl : listWhiteList) {

				if(!isNotifySigameByTipoEnvio(wl, statusLinhaTempo)){
					continue;
				}

				//so define objeto mail se o tipo de notificação for por e-mail
				if(TrackingContantsUtil.WHITE_LIST_TIPO_EMAIL.equals(wl.getTpWhiteList().getValue())){
					Mail mail = setMailWhiteList(wl, null, statusLinhaTempo, lstNotasFiscais);
					senderObjMailJMS(mail);
					String address = wl.getContato().getDsEmail();
					emailsSucesso = (emailsSucesso == null) ? address : (emailsSucesso + ", " + address);
				} else {
					sendPushNotification(null, wl);
				}
			}
		}
		return emailsSucesso;
	}

	private StatusLinhaTempoRastreabilidade findLinhaTempoEvento(List<WhiteList> listWhiteList,	EventoDocumentoServicoDMN eventoDoctoServico) {
		if( eventoDoctoServico != null && !EventoDoctoServicoHelper.getLinhaTempo(eventoDoctoServico).equals( StatusLinhaTempoRastreabilidade.NENHUMA )){
			return EventoDoctoServicoHelper.getLinhaTempo(eventoDoctoServico);
		}else{
			//se não encontrou linha do tempo no objeto (eventoDoctoServico) faz a busca na base
			return findLinhaTempoEvento(listWhiteList);
		}
	}

	/**
	 * Metodo responsavel por verificar tipo de envio siga-me condiz com status de evento do documento.
	 * @param wl
	 * @param statusLinhaTempo
	 * @return
	 */
	private boolean isNotifySigameByTipoEnvio(WhiteList wl, StatusLinhaTempoRastreabilidade statusLinhaTempo) {
		return wl.getTpEnvio().getValue().equals(TrackingContantsUtil.WHITE_LIST_TIPO_ENVIO_COMPLETO) ||
				(wl.getTpEnvio().getValue().equals(TrackingContantsUtil.WHITE_LIST_TIPO_ENVIO_SOMENTE_ENTREGA)
						&& statusLinhaTempo.equals(StatusLinhaTempoRastreabilidade.FINALIZADO));
	}

	/**
	 * Seta status da linha do tempo
	 * @param listWhiteList - <StatusLinhaTempoRastreabilidade>
	 */
	private StatusLinhaTempoRastreabilidade findLinhaTempoEvento(List<WhiteList> listWhiteList) {
		StatusLinhaTempoRastreabilidade statusLinhaTempo = null;
		if(!listWhiteList.isEmpty()){
			statusLinhaTempo = linhaTempoService.findLinhaTempoEvento(listWhiteList.get(0).getDoctoServico().getIdDoctoServico());
		}

		return statusLinhaTempo;
	}

	/**
	 * Verifica se devera notificar o cliente sobre o status atual
	 * @param eventoDoctoServico - objeto
	 * @return boolean
	 */
	public boolean isNotificaCliente(EventoDocumentoServicoDMN eventoDoctoServico){
		return eventoDoctoServico.getBlExibeCliente().equals(Boolean.TRUE) || (eventoDoctoServico.getIdWhiteList() != null && eventoDoctoServico.getIdWhiteList() != 0L);
	}


	/**
	 * Metodo responsavel por enviar obejto email para a fila de envio
	 * @param mail - contem todas as informações necessarias para enviar e-mail
	 */
	private void senderObjMailJMS(Mail mail){
		if(mail !=  null) {
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
			integracaoJmsService.storeMessage(msg);
		}
	}

	private void sendPushNotification(EventoDocumentoServicoDMN eventoDoctoServico, WhiteList whiteList) {

		DispositivoContato dispositivo = whiteList.getContato().getDispositivoContato();

		String token = dispositivo.getDsToken();

		String title = configuracoesFacade.getMensagem("RADAR-00007");
		String status = CharactersUtils.cleanPreposicaoFinalStr(CharactersUtils.cleanLocalization(getLocalizacaoAtualMercadoria(whiteList, eventoDoctoServico)));
		String msg = configuracoesFacade.getMensagem("RADAR-00008");

		List<NotaFiscalConhecimento> lstNotasFiscais = doctoServicoService.findNotasFiscaisByDoctoServico(whiteList.getDoctoServico());

		String prmMensagem;
		if(lstNotasFiscais.isEmpty()){
			prmMensagem = getNMDocServico(whiteList);
		} else {
			prmMensagem = String.valueOf(lstNotasFiscais.get(0).getNrNotaFiscal());
		}

		String message = MessageFormat.format(msg, prmMensagem, status);

		NotificationDMN notificationDMN = new NotificationDMN();
		notificationDMN.setToken(token);
		notificationDMN.setMessage(message);
		notificationDMN.setTitle(title);
		notificationDMN.setData("idDoctoServico=" + whiteList.getDoctoServico().getIdDoctoServico());
		notificationDMN.setPlatform(dispositivo.getTpPlataforma().getValue());

		JmsMessageSender jmsMsg = integracaoJmsService.createMessage(Queues.MOBILE_PUSH_NOTIFICATION_SENDER, notificationDMN);
		integracaoJmsService.storeMessage(jmsMsg);
	}

	/**
	 * Metodo resposavel por montar corpo do e-mail a ser enviado
	 * @param eventoDoctoServico
	 * @param statusLinhaTempo
	 * @param lstNotasFiscais
	 * @return Mail- objeto contendo informações de e-mail
	 */
	private Mail setMailWhiteList(WhiteList wl, EventoDocumentoServicoDMN eventoDoctoServico,
								  StatusLinhaTempoRastreabilidade statusLinhaTempo, List<NotaFiscalConhecimento> lstNotasFiscais) {

		Mail mail = new Mail();
		String strUrlRadarWeb = (String) configuracoesFacade.getValorParametro("URL_RADAR_WEB");

		//boclo informações do remetente siga-me
		String obs = (wl.getObWhiteList()!=null)?wl.getObWhiteList():"";
		String radarEmailOwnerObsWhiteList =  (obs.length() > 0)?configuracoesFacade.getMensagem("radarEmailOwnerObsWhiteList", new Object[]{}):"";
		Object[] obj = new Object[]{wl.getNmRemetente(),wl.getDsEmailRemetente(),radarEmailOwnerObsWhiteList,obs};
		String radarEmailOwnerWhiteList = configuracoesFacade.getMensagem("radarEmailOwnerWhiteList", obj);

		//bloco iformações assunto do e-mail
		obj = new Object[]{getNotaFiscal(lstNotasFiscais), getNMPessoa(lstNotasFiscais)};
		String radarAssuntoEmailWhiteList = configuracoesFacade.getMensagem("radarAssuntoEmailWhiteList", obj);

		//bloco iformações assunto TNT Mercúrio - CTE CWB7706
		String encriptIdDoctoServico = EncryptionUtils.getEncryptString(wl.getDoctoServico().getIdDoctoServico().toString());
		StringBuilder urlPublicDetailTracking = new StringBuilder(strUrlRadarWeb).append("/public/localizacaoSimplificadaDetail/detail/").append(encriptIdDoctoServico).append(".do");

		StringBuilder radarCssBtWhiteList = new StringBuilder("{").append(configuracoesFacade.getMensagem("radarCssBtWhiteList")).append("}");

		obj = new Object[]{radarCssBtWhiteList.toString()};
		String radarCssLinhaWhiteList = configuracoesFacade.getMensagem("radarCssLinhaWhiteList", obj);

		Imagem linhaTempo = imagemService.findByChave(statusLinhaTempo.toString(), Boolean.FALSE);
		obj = new Object[]{radarCssLinhaWhiteList, linhaTempo.getPicture() , urlPublicDetailTracking.toString()};
		String radarEmailLinhaWhiteList = configuracoesFacade.getMensagem("radarEmailLinhaWhiteList", obj);

		Imagem logoFedEx = imagemService.findByChave(LOGO_FEDEX, Boolean.FALSE);
		String radarEmailRodapeDefault = configuracoesFacade.getMensagem("radarEmailRodapeDefault", new Object[]{logoFedEx.getPicture()});

		//bloco rodape  links de canelemento siga-me
		String encriptIdWhiteList = Base64.encode(wl.getIdWhiteList().toString().getBytes());
		StringBuilder strUrlRadarWebCancel = new StringBuilder(strUrlRadarWeb).append("/public/notificacao/confirmarInativacao/").append(encriptIdWhiteList).append(".do");
		obj = new Object[]{strUrlRadarWebCancel.toString()};

		String radarEmailCancelWhiteList = configuracoesFacade.getMensagem("radarEmailCancelWhiteList", obj);

		String localizacaoAtualMercadoria = getLocalizacaoAtualMercadoria(wl,eventoDoctoServico);

		localizacaoAtualMercadoria = CharactersUtils.cleanPreposicaoFinalStr(CharactersUtils.cleanLocalization(localizacaoAtualMercadoria));

		//bloco monta corpo <body> do e-mail
		obj = new Object[]{getNotaFiscal(lstNotasFiscais), getNMPessoa(lstNotasFiscais), localizacaoAtualMercadoria,
				radarEmailLinhaWhiteList,getNMDocServico(wl),getNotasFiscais(lstNotasFiscais),
				radarEmailRodapeDefault,radarEmailOwnerWhiteList,radarEmailCancelWhiteList};
		String radarEmailNotificacaoWhiteList = configuracoesFacade.getMensagem("radarEmailNotificacaoWhiteList", obj);

		final String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");

		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(wl.getContato().getDsEmail());
		mail.setSubject(radarAssuntoEmailWhiteList);
		mail.setBody(radarEmailNotificacaoWhiteList);

		return mail;
	}

	private String getNotasFiscais(List<NotaFiscalConhecimento> lstNotasFiscais) {
		StringBuilder nfs = new StringBuilder();
		if(lstNotasFiscais != null && !lstNotasFiscais.isEmpty()){
			for (NotaFiscalConhecimento notaFiscalConhecimento : lstNotasFiscais) {
				nfs.append("/").append(notaFiscalConhecimento.getNrNotaFiscal());
			}
		}
		return nfs.toString().replaceFirst("/", "");
	}

	private String getNMPessoa(List<NotaFiscalConhecimento> lstNotasFiscais) {
		if(lstNotasFiscais != null && !lstNotasFiscais.isEmpty() && lstNotasFiscais.get(0) != null){
			Conhecimento conhecimento = lstNotasFiscais.get(0).getConhecimento();
			if(conhecimento != null
					&& conhecimento.getClienteByIdClienteRemetente() != null
					&& conhecimento.getClienteByIdClienteRemetente().getPessoa() != null){
				return conhecimento.getClienteByIdClienteRemetente().getPessoa().getNmPessoa();
			}
		}

		return "";
	}

	private String getNotaFiscal(List<NotaFiscalConhecimento> lstNotasFiscais) {
		if(lstNotasFiscais != null && !lstNotasFiscais.isEmpty() && lstNotasFiscais.get(0) != null){
			return lstNotasFiscais.get(0).getNrNotaFiscal().toString();
		}
		return "";
	}

	/**
	 * Retorna nome documento serviço
	 * Responsavel por
	 * @param wl
	 * @return
	 */
	public String getNMDocServico(WhiteList wl){
		String nMDocServico = "";
		if( wl.getDoctoServico().getFilialByIdFilialOrigem() != null
				&& wl.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial() != null
				&& wl.getDoctoServico().getNrDoctoServico() != null){
			nMDocServico = wl.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial();
			nMDocServico += " "+ wl.getDoctoServico().getNrDoctoServico();
		}else if(wl.getDoctoServico().getNrDoctoServico() != null){
			nMDocServico = wl.getDoctoServico().getNrDoctoServico().toString();
		}
		return nMDocServico;
	}


	private String getLocalizacaoAtualMercadoria(WhiteList wl, EventoDocumentoServicoDMN eventoDoctoServico) {
		if (eventoDoctoServico != null) {
			if (eventoDoctoServico.getDsLocalizacaoMercadoria() != null) {
				return eventoDoctoServico.getDsLocalizacaoMercadoria();
			} else if (eventoDoctoServico.getDsEvento() != null) {
				return eventoDoctoServico.getDsEvento();
			}
		} else {
			Map<String, Object> criteria = new HashMap<>();
			criteria.put("idDoctoServico", wl.getDoctoServico().getIdDoctoServico());
			Object[] retorno = filialService.findLocalizacaoFilialAtual(criteria);
			return (String) (retorno != null ? retorno[POSICAO_LOCALIZACAO_MERCADORIA] : "");
		}

		return "";
	}

	/**
	 * Metodo responsável por salvar uma lista de WhiteList e caso ocorra um erro
	 * de violação da constraint retornar a lista de objetos que deram erro.
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> insereAllAcompanhamentoMercadoria(TypedFlatMap criteria) {
		Map<String, Object> retorno = new HashMap<>();
		List<String> msgErro = new ArrayList<>();
		DoctoServico doctoServico = getDoctoServico(criteria);
		List<Contato> contatos = contatoService.createContatosWhiteList(criteria, doctoServico, msgErro);
		retorno.put("isSucesso", Boolean.FALSE);
		if(msgErro.isEmpty()){
			List<WhiteList> whiteLists = populaWhiteList(criteria, doctoServico, contatos);
			List<WhiteList> wlErro = new ArrayList<>();
			try{
				for (WhiteList bean : whiteLists) {
					saveWhiteList(wlErro, bean);
				}
				if(!wlErro.isEmpty()){
					throw new RuntimeException(configuracoesFacade.getMensagem("uniqueConstraintViolated"));
				}
				retorno.put("isSucesso", Boolean.TRUE);
				retorno.put("wlSave", whiteLists);
			}catch (RuntimeException e) {
				log.error("Erro ao salvar a lista de WhiteList", e);
			}
			retorno.put("wlErro", wlErro);
		}
		retorno.put("msgErro", msgErro);
		return retorno;
	}

	private DoctoServico getDoctoServico(TypedFlatMap criteria) {
		DoctoServico doctoServico = doctoServicoService.findByIdJoinFilial(criteria.getLong(TrackingContantsUtil.WHITE_LIST_PRM_DOCTO_SERVICO));
		if(doctoServico == null){
			throw new InvalidParameterException(configuracoesFacade.getMensagem("RADAR-00022"));
		}
		return doctoServico;
	}

	private List<WhiteList> populaWhiteList(TypedFlatMap criteria, DoctoServico doctoServico, List<Contato> listaContato) {
		List<WhiteList> listWhiteList = new ArrayList<>();
		for (Contato contato : listaContato) {
			WhiteList whiteList = createWhiteList(criteria, doctoServico);
			whiteList.setContato(contato);
			listWhiteList.add(whiteList);
		}
		return listWhiteList;
	}

	/**
	 * Get valores criteria para gerar registro whiteList/Contato
	 * @param criteria
	 * @return
	 */
	private WhiteList createWhiteList(TypedFlatMap criteria, DoctoServico doctoServico) {
		WhiteList whiteList = new WhiteList();
		whiteList.setDoctoServico(doctoServico);
		whiteList.setDsEmailRemetente(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_EMAIL_REMETENTE));
		whiteList.setNmRemetente(criteria.getString("nmRemetente"));
		whiteList.setObWhiteList(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_OBERVACAO));
		whiteList.setSituacaoWhiteList(domainValueService.findDomainValueByValue(DM_STATUS, criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_SITUACAO)));

		String tpOrigem = criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TIPO_ORIGEM);

		whiteList.setTpOrigem(domainValueService.findDomainValueByValue("DM_TP_ORIGEM_WHITE_LIST", RADAR_MOBILE.equalsIgnoreCase(tpOrigem) ? tpOrigem : RADAR_WEB));

		whiteList.setTpWhiteList(domainValueService.findDomainValueByValue(DM_TP_WHITE_LIST, criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TP_WHITE_LIST)));
		String login = criteria.getString("login");
		if(login != null){
			UsuarioADSM usuario = usuarioADSMService.findUsuarioADSMByLogin(login);
			whiteList.setUsuario(usuario);
		}
		whiteList.setTpEnvio(domainValueService.findDomainValueByValue(DM_TP_ENVIO_WHITE_LIST, criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TIPO_ENVIO)));
		return whiteList;
	}

	private void saveWhiteList(List<WhiteList> wlErro, WhiteList bean){
		WhiteList jaExistente = null;

		if(bean.getContato().getIdContato() != null){
			jaExistente = findWhiteListByDoctoServicoContatoTipo(bean.getDoctoServico().getIdDoctoServico(), bean.getContato().getIdContato(), bean.getTpWhiteList());
		}

		if(jaExistente == null){
			store(bean);
		} else if(TrackingContantsUtil.WHITE_LIST_SITUACAO_ATIVO.equals(jaExistente.getSituacaoWhiteList().getValue())){
			wlErro.add(jaExistente);
		} else if(TrackingContantsUtil.WHITE_LIST_SITUACAO_INATIVO.equals(jaExistente.getSituacaoWhiteList().getValue())){
			jaExistente.setSituacaoWhiteList(new DomainValue(TrackingContantsUtil.WHITE_LIST_SITUACAO_ATIVO));
			jaExistente.setTpEnvio(bean.getTpEnvio());
			store(jaExistente);
		}
	}

	private List<WhiteList> getListWhiteList(EventoDocumentoServicoDMN eventoDoctoServico) {
		List<WhiteList> listWhiteList;
		if( eventoDoctoServico.getIdDoctoServico() != null ){
			listWhiteList = getWhiteListDAO().findWhiteListByDoctoServico(eventoDoctoServico.getIdDoctoServico());
		} else {
			listWhiteList = getWhiteListDAO().findWhiteListById(eventoDoctoServico.getIdWhiteList());
		}
		return listWhiteList;
	}

	public WhiteList findWhiteListBy(Long idDoctoServico, String email, DomainValue tpWhiteList){
		return getWhiteListDAO().findWhiteListBy(idDoctoServico, email, tpWhiteList);
	}

	public WhiteList findWhiteListByDoctoServicoContatoTipo(Long idDoctoServico, Long idContato, DomainValue tipo){
		return getWhiteListDAO().findWhiteListByDoctoServicoContatoTipo(idDoctoServico, idContato, tipo);
	}

	public LinhaTempoService getLinhaTempoService() {
		return linhaTempoService;
	}

	public void setLinhaTempoService(LinhaTempoService linhaTempoService) {
		this.linhaTempoService = linhaTempoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacadeImpl() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacadeImpl(
			ConfiguracoesFacade configuracoesFacadeImpl) {
		this.configuracoesFacade = configuracoesFacadeImpl;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setImagemService(ImagemService imagemService) {
		this.imagemService = imagemService;
	}

}
