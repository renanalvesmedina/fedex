package com.mercurio.lms.drobot.swt.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import sun.misc.BASE64Encoder;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.configuracoes.model.AgendaAtualizacaoRobo;
import com.mercurio.lms.configuracoes.model.service.AgendaAtualizacaoRoboService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;

@ServiceSecurity
public class DRobotWebAction {

	private AgendaAtualizacaoRoboService agendaAtualizacaoRoboService;
	private ConhecimentoService conhecimentoService;

	private static final String STATUS_SOM_EMITIDO = "2";
	private static final String STATUS_SOM_X = "X";
	private static final String STATUS_SOM_CANCELADO = "9";

	@MethodSecurity(processGroup = "drobot",
			processName = "drobot.findAgendamento",
			authenticationRequired=false)
	public Map<String, Object> findAgendamento(Map<String, Object> parameters) {

		Map<String, Object> result = new HashMap<String, Object>();
		BigDecimal ultimaVersao = (BigDecimal) parameters.get("ultimaVersao");
		AgendaAtualizacaoRobo agendaAtualizacaoRobo = null;
		if(ultimaVersao != null) {
			agendaAtualizacaoRobo = agendaAtualizacaoRoboService.findUltimaAtualizacao(ultimaVersao.longValue());
		}

		if(agendaAtualizacaoRobo != null) {
			result.put("versao", agendaAtualizacaoRobo.getNrVersao());
			result.put("dhAtualizacao", agendaAtualizacaoRobo.getDhAtualizacao());
			result.put("somVersion", agendaAtualizacaoRobo.getNrVersaoSOM());
			result.put("arquivoZip", new BASE64Encoder().encode(agendaAtualizacaoRobo.getArquivoZip()));
		}
		return result;
	}

	@MethodSecurity(processGroup = "drobot",
			processName = "drobot.findConhecimentosPendentesLMS",
			authenticationRequired=false)
	public List findConhecimentosPendentesLMS(Map<String, Object> parameters) {
		Long idFilial = (Long) parameters.get("idFilial");
		List list = conhecimentoService.findConhecimentosPendentesLMS(idFilial);

		List<Long> result = new ArrayList<Long>(list.size());
		for (int i=0; i<list.size(); i++) {
			Map map = (Map)list.get(i);
			result.add((Long)map.get("nrConhecimento"));
		}
		return result;
	}

	@MethodSecurity(processGroup = "drobot",
			processName = "drobot.findDadosConhecimentosPendentesLMS",
			authenticationRequired=false)
	public List<Map<String, Object>> findDadosConhecimentosPendentesLMS(Map<String, Object> parameters) {
		return conhecimentoService.findConhecimentosPendentesLMS((Long) parameters.get("idFilial"));
	}

	@MethodSecurity(processGroup = "drobot", processName = "drobot.findDadosConhecimentosToSOM", authenticationRequired=false)
	public List<Map<String, Object>> findDadosConhecimentosToSOM(Map<String, Object> parameters) {
		List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
		Long idFilial = (Long) parameters.get("idFilial");
		DateTime date = (DateTime) parameters.get("date");
		List<Long> nrConhecimentos = (List<Long>) parameters.get("ctrcs");
		List<Long> ids = conhecimentoService.findDadosConhecimentosToSOM(idFilial, nrConhecimentos, date);
		Map<String, Object> obj = null;
		for (Long id : ids) {
			obj = conhecimentoService.findDataToSOM(id, STATUS_SOM_X);
			obj.put("tpdc", 0);
			if(obj.get("tpSituacaoConhecimento" ) != null){
				if		("E".equals(obj.get("tpSituacaoConhecimento" ))) obj.put("statusConh", STATUS_SOM_EMITIDO);
				else if ("C".equals(obj.get("tpSituacaoConhecimento" ))) obj.put("statusConh", STATUS_SOM_CANCELADO);

				out.add(obj);
			}
		}

		List<Long> conhec = conhecimentoService.findConhecimentosVNFPendentesToSOM(idFilial, date);
		for (Long nrConh : conhec) {
			Map<String, Object> conhX = new HashMap<String, Object>();
			conhX.put("nrConhecimento", nrConh);
			conhX.put("statusConh", "X");
			out.add(conhX);
		}

		return out;
	}

	public AgendaAtualizacaoRoboService getAgendaAtualizacaoRoboService() {
		return agendaAtualizacaoRoboService;
	}

	public void setAgendaAtualizacaoRoboService(AgendaAtualizacaoRoboService agendaAtualizacaoRoboService) {
		this.agendaAtualizacaoRoboService = agendaAtualizacaoRoboService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
}
