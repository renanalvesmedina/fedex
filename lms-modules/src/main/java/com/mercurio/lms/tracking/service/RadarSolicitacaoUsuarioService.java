package com.mercurio.lms.tracking.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.tracking.Order;
import com.mercurio.lms.tracking.model.RadarSolicitacaoUsuario;
import com.mercurio.lms.tracking.model.dao.RadarSolicitacaoUsuarioDAO;
import com.mercurio.lms.tracking.util.AliasOrder;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.thoughtworks.xstream.XStream;

@ServiceSecurity
public class RadarSolicitacaoUsuarioService extends CrudService<RadarSolicitacaoUsuario, Long> {

	public static final String TEXT_HTML= "text/html; charset='utf-8'";
	private ConfiguracoesFacade configuracoesFacade;
	private IntegracaoJmsService integracaoJmsService; 
	private FilialService filialService;
	private String MATRIZ = "MTZ";
	private String MERCURIO = "M"; 


	public void setDAO(RadarSolicitacaoUsuarioDAO dao) {
		setDao(dao);
	}

	private RadarSolicitacaoUsuarioDAO getDAO() {
		return (RadarSolicitacaoUsuarioDAO) getDao();
	}

	public Serializable store(RadarSolicitacaoUsuario bean) {
		return super.store(bean);
	}

	public RadarSolicitacaoUsuario findById(Long id) {
		return (RadarSolicitacaoUsuario) super.findById(id);
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	@MethodSecurity(processGroup = "tracking.radarSolicitacaoUsuarioService", processName = "storeOrder", authenticationRequired = false)
	public Map<String, Object> storeOrder(TypedFlatMap map) {

		RadarSolicitacaoUsuario radarSolicitacaoUsuario = createRadarSolicitacaoUsuario(map);

		super.store(radarSolicitacaoUsuario);

		generateEmailContato(radarSolicitacaoUsuario);

		return convertToXml(radarSolicitacaoUsuario);
	}

	private Map<String, Object> convertToXml(RadarSolicitacaoUsuario radarSolicitacaoUsuario) {
		Map<String, Object> map = new HashMap<String, Object>();
		Order order = new Order(radarSolicitacaoUsuario.getIdRadarSolicitacaoUsuario());
		XStream xStream = AliasOrder.createAlias();
		String xml = xStream.toXML(order);
		map.put("xml", xml);
		map.put("isXml", true);
		return map;
	}

	private RadarSolicitacaoUsuario createRadarSolicitacaoUsuario(TypedFlatMap map) {
		
		Filial filial = filialService.findBySgFilialAndTpEmpresa(MATRIZ, MERCURIO);
		SessionContext.set("FILIAL_KEY", filial);
		
		RadarSolicitacaoUsuario radarSolicitacaoUsuario = new RadarSolicitacaoUsuario();
		
		radarSolicitacaoUsuario.setNmEmpresa(map.getString("companyName"));
		radarSolicitacaoUsuario.setNmContato(map.getString("contactName"));
		radarSolicitacaoUsuario.setDsEmailContato(map.getString("email"));
		radarSolicitacaoUsuario.setNrDdd(map.getString("phoneAreaCode"));
		radarSolicitacaoUsuario.setNrTelefone(map.getString("phoneNumber"));
		radarSolicitacaoUsuario.setTpContato(new DomainValue(map.getString("contactType").toUpperCase()));
		
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(map.getLong("idCity"));
		radarSolicitacaoUsuario.setMunicipio(municipio);
		
		if(map.getLong("idService") != null){
			Servico servico = new Servico();
			servico.setIdServico(map.getLong("idService"));
			radarSolicitacaoUsuario.setServico(servico);
		}
		
		radarSolicitacaoUsuario.setTpOrigem(new DomainValue("M"));
		radarSolicitacaoUsuario.setTpStatus(new DomainValue("A"));
		radarSolicitacaoUsuario.setDhGravacao(JTDateTimeUtils.getDataHoraAtual());

		return radarSolicitacaoUsuario;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void generateEmailContato(RadarSolicitacaoUsuario radarSolicitacaoUsuario) {
		final String strTo = radarSolicitacaoUsuario.getDsEmailContato();

		if (strTo != null && !"".equals(strTo)) {
			final String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
			
			ArrayList param = new ArrayList();
			param.add(configuracoesFacade.getDomainValue("DM_TP_CONTATO_RAD_SOLICITACAO_USUARIO", radarSolicitacaoUsuario.getTpContato().getValue()).getDescriptionAsString()); 
			final StringBuilder strSubject = new StringBuilder(configuracoesFacade.getMensagem("radarAssuntoEmailSolicitacao", param.toArray()));

			param.add(radarSolicitacaoUsuario.getIdRadarSolicitacaoUsuario());
			final StringBuilder body = new StringBuilder(configuracoesFacade.getMensagem("radarConteudoEmailSolicitacao", param.toArray()));
			
			Mail mail = createMail(strTo, strFrom, strSubject, body);
			
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
			integracaoJmsService.storeMessage(msg);
			
		}
	}
	
	private Mail createMail(String strTo, String strFrom, StringBuilder strSubject, StringBuilder body){
		
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject.toString());
		mail.setBody(body.toString());
		
		return mail;
		
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}



	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


}
