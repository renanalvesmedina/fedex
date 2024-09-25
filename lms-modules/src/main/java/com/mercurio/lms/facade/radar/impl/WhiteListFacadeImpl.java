package com.mercurio.lms.facade.radar.impl;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.DispositivoContato;
import com.mercurio.lms.configuracoes.model.WhiteList;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.DispositivoContatoService;
import com.mercurio.lms.configuracoes.model.service.WhiteListService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.facade.radar.WhiteListFacade;
import com.mercurio.lms.facade.radar.impl.whitelist.ResultadoNotificacao;
import com.mercurio.lms.facade.radar.impl.whitelist.TipoNotificacao;
import com.mercurio.lms.facade.radar.impl.whitelist.TipoWhiteList;
import com.mercurio.lms.tracking.util.TrackingContantsUtil;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

/**
 * @author Celso Martins
 * @spring.bean id="lms.radar.whiteList"
 */
@ServiceSecurity
public class WhiteListFacadeImpl implements WhiteListFacade{

	private static final String RADAR_CONTATO_SEGUINDO_WHITE_LIST = "radarContatoSeguindoWhiteList";
	private static final String RADAR_CONTATO_NAO_SEGUINDO_WHITE_LIST = "radarContatoNaoSeguindoWhiteList";
	private static final Logger LOGGER = LogManager.getLogger(WhiteListFacadeImpl.class);
	private static final String DM_STATUS = "DM_STATUS";
	private static final String MSG_ERRO = "msgErro";
	private static final String COD_ERRO = "codErro";
	private static final String MSG_SUCESSO = "msgSucesso";

	private WhiteListService whiteListService;
	private DomainValueService domainValueService;
	private DoctoServicoService doctoServicoService;
	private ContatoService contatoService;
	private DispositivoContatoService dispositivoContatoService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Salva ou atualiza a tabela de whitelist
	 */
	@Override
	@MethodSecurity(processGroup = "radar.whiteList", processName = "storeWhiteList", authenticationRequired=false)
	public TypedFlatMap storeWhiteList(TypedFlatMap criteria) {
		validaParametros(criteria); 
		
		Map<String, Object> retorno = whiteListService.insereAllAcompanhamentoMercadoria(criteria);	
		
		List<String> msgErro = (List<String>) retorno.get("msgErro");
		Boolean isSucesso = (Boolean)retorno.get("isSucesso");
		List<WhiteList> listaWlErro = (List<WhiteList>)retorno.get("wlErro");
		
		TypedFlatMap tfm = new TypedFlatMap();
		if(msgErro.isEmpty()){
			if(isSucesso){
				List<WhiteList> whiteLists = (List<WhiteList>)retorno.get("wlSave");
				// Se salvou com sucesso enviar status atual da carga
				handleNotificationResult(tfm, msgErro, enviarStatusAtual(criteria, whiteLists));
			}else if(!listaWlErro.isEmpty()){
				// Caso ocorra problema de integridade violada retornar os contatos que estão já estão seguindo.
				contatosJaTemSigame(criteria, tfm, msgErro, listaWlErro);
			}else{
				// Erro generico se ocorrer algum problema.
				msgErro.add("Ocorreu um problema na gravação.");
			}
		}
			
		tfm.put(MSG_ERRO, msgErro);
		return tfm;
	}

	private ResultadoNotificacao enviarStatusAtual(TypedFlatMap request, List<WhiteList> whiteLists) {
		return TipoWhiteList.from(request).isForEmail()
				? enviarEmail(whiteLists, TipoNotificacao.from(request))
				: enviarPush(whiteLists, TipoNotificacao.from(request));
	}

	private void handleNotificationResult(TypedFlatMap msgs, List<String> errors, ResultadoNotificacao result) {
		if (result.isSuccessful() && errors.isEmpty()) {
			msgs.put(MSG_SUCESSO, result.successMessage());
		} else {
			LOGGER.error("Problemas ao enviar notificação com status atual: ", result.exception());
			errors.add(result.failureMessage());
		}
	}

	private ResultadoNotificacao enviarEmail(List<WhiteList> whiteLists, TipoNotificacao tipoNotificacao) {
		try {
			if(tipoNotificacao.isCompleto()){
				String emails = whiteListService.executeWhiteListRastreabilidadeRealTime(whiteLists);
				return ResultadoNotificacao.success(configuracoesFacade.getMensagem("radarSucessoEmailConfirmacaoWhiteList", new String[]{emails}));
			} else {
				StringBuilder emailsSucesso = new StringBuilder();
				for (WhiteList wl : whiteLists) {
					String address = wl.getContato().getDsEmail();
					emailsSucesso.append(emailsSucesso.length() == 0 ? address : ", " + address);
				}
				
				return ResultadoNotificacao.success(configuracoesFacade.getMensagem("RADAR-00012", new String[]{emailsSucesso.toString()}));
			}
        } catch (Exception e) {
            return ResultadoNotificacao.failure(configuracoesFacade.getMensagem("radarErroEmailConfirmacaoWhiteList"), e);
        }
	}

	private ResultadoNotificacao enviarPush(List<WhiteList> whiteLists, TipoNotificacao tipoNotificacao) {
		try {
			String chaveMensagem;

			if(tipoNotificacao.isCompleto()){
				chaveMensagem = "RADAR-00001";
				whiteListService.executeWhiteListRastreabilidadeRealTime(whiteLists);
			} else {
				chaveMensagem = "RADAR-00002";
			}
			
			return ResultadoNotificacao.success(configuracoesFacade.getMensagem(chaveMensagem));
		} catch (Exception e) {
			return ResultadoNotificacao.failure(configuracoesFacade.getMensagem("RADAR-00003"), e);
		}
	}

	private void contatosJaTemSigame(TypedFlatMap request, TypedFlatMap tfm, List<String> msgErro, List<WhiteList> listaWlErro) {
		if (TipoWhiteList.from(request).isForEmail()) {
			contatosJaTemSigame(tfm, msgErro, listaWlErro);
		} else {
			String codErro = "RADAR-00005";
			msgErro.add(configuracoesFacade.getMensagem(codErro));
			tfm.put(COD_ERRO, codErro);
			tfm.put(MSG_ERRO, msgErro);
		}
	}

	private void contatosJaTemSigame(TypedFlatMap tfm, List<String> msgErro,
			List<WhiteList> listaWlErro) {
		String whiteListJaCadastradosAtivos = "";
		String whiteListJaCadastradosInativos = "";
		for (WhiteList wl : listaWlErro) {
			Contato contato = contatoService.findById(wl.getContato().getIdContato());
			if(wl.getSituacaoWhiteList().getValue().equalsIgnoreCase(TrackingContantsUtil.WHITE_LIST_SITUACAO_ATIVO)){
				whiteListJaCadastradosAtivos += contato.getDsEmail() + ", ";
			}else{
				whiteListJaCadastradosInativos += contato.getDsEmail() + ", ";
			}
		}
		if(whiteListJaCadastradosAtivos != ""){
			msgErro.add(configuracoesFacade.getMensagem(RADAR_CONTATO_SEGUINDO_WHITE_LIST, new String[]{removeUltimaVirgulaTexto(whiteListJaCadastradosAtivos)}));
		}
		if(whiteListJaCadastradosInativos != ""){
			msgErro.add(configuracoesFacade.getMensagem(RADAR_CONTATO_NAO_SEGUINDO_WHITE_LIST, new String[]{removeUltimaVirgulaTexto(whiteListJaCadastradosInativos)}));
		}
		tfm.put(MSG_ERRO, msgErro);
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.whiteList", processName = "executeSendWhiteListStatusAtual", authenticationRequired=false)
	public TypedFlatMap executeSendWhiteListStatusAtual(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		try{
			EventoDocumentoServicoDMN evtDoctoServRast = new EventoDocumentoServicoDMN();
			evtDoctoServRast.setIdWhiteList(criteria.getLong(TrackingContantsUtil.WHITE_LIST_PRM_ID_WL));
			List<WhiteList> lstReturn = whiteListService.executeWhiteListRastreabilidade(evtDoctoServRast);
			tfm.put("qtd", lstReturn.size());
			
		}catch(Exception ex){
			LOGGER.error("Problemas na ativação da WhiteList: ", ex);
			tfm.put("retornoSend", "Ocorreu um erro na notificação de ativação de status.");
		}
		return tfm;
	}
	
	
	@Override
	@MethodSecurity(processGroup = "radar.whiteList", processName = "updateStatusWhiteList", authenticationRequired=false)
	public TypedFlatMap updateStatusWhiteList(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		String status = criteria.getString("status");
		DomainValue ativo = domainValueService.findDomainValueByValue(DM_STATUS, status);
		List<String> msgErro = new ArrayList<String>();
		try{
			WhiteList whiteList = whiteListService.executeAtivacaoWhiteList(criteria.getLong(TrackingContantsUtil.WHITE_LIST_PRM_ID_WL), ativo);
			DoctoServico doctoServico = doctoServicoService.findByIdJoinFilial(whiteList.getDoctoServico().getIdDoctoServico());
			tfm.put("nrDocumento", doctoServico.getFilialByIdFilialOrigem().getSgFilial()+"-"+ doctoServico.getNrDoctoServico());
			
		}catch(Exception ex){
			LOGGER.error("Problemas na ativação da WhiteList: ", ex);
			msgErro.add("Ocorreu um erro na ativação, erro: "+ex.getMessage());
		}
		tfm.put("msgErro", msgErro);
		return tfm;
	}
	
	
	@Override
	public TypedFlatMap disableWhiteList(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		try{
			if(TrackingContantsUtil.WHITE_LIST_TIPO_PUSH.equals(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TP_WHITE_LIST))){
				disableWhiteListPush(criteria, tfm);
			} else if(TrackingContantsUtil.WHITE_LIST_TIPO_EMAIL.equals(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TP_WHITE_LIST))){
				disableWhiteListEmail(criteria);
			}
			
		}catch(Exception ex){
			LOGGER.error("Problemas na ativação da WhiteList: ", ex);
			tfm.put(MSG_ERRO, "Ocorreu um erro na ativação, erro: "+ex.getMessage());
		}

		
		return tfm;
	}

	private void disableWhiteListEmail(TypedFlatMap criteria) {
		criteria.put("status", TrackingContantsUtil.WHITE_LIST_SITUACAO_INATIVO);
		if(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_ID_WL) == null){
			String msg = MessageFormat.format(configuracoesFacade.getMensagem("RADAR-00023"), TrackingContantsUtil.WHITE_LIST_PRM_ID_WL);	
			throw new InvalidParameterException(msg);		
		}
		updateStatusWhiteList(criteria);
	}

	private void disableWhiteListPush(TypedFlatMap criteria, TypedFlatMap tfm)  {
		String token = criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TOKEN);
		DispositivoContato dispositivo = dispositivoContatoService.findDispositivoByToken(token);
		if(dispositivo == null){
			String codErro = "RADAR-00025";
			tfm.put(COD_ERRO, codErro);
			tfm.put(MSG_ERRO, configuracoesFacade.getMensagem(codErro));
		} else {
			Long idDoctoServico = criteria.getLong(TrackingContantsUtil.WHITE_LIST_PRM_DOCTO_SERVICO);
			String tipo = criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TP_WHITE_LIST);
			if(idDoctoServico != null && dispositivo.getContato() != null && tipo != null){
				WhiteList whiteList = whiteListService.findWhiteListByDoctoServicoContatoTipo(idDoctoServico, dispositivo.getContato().getIdContato(), new DomainValue(tipo));
				if(whiteList == null){
					String codErro = "RADAR-00025";
					tfm.put(COD_ERRO, codErro);
					tfm.put(MSG_ERRO, configuracoesFacade.getMensagem(codErro));
				} else if(TrackingContantsUtil.WHITE_LIST_SITUACAO_INATIVO.equals(whiteList.getSituacaoWhiteList().getValue())){
					String codErro = "RADAR-00026";
					tfm.put(COD_ERRO, codErro);
					tfm.put(MSG_ERRO, configuracoesFacade.getMensagem(codErro));
				} else {
					DomainValue novoStatus = new DomainValue(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_SITUACAO));
					whiteListService.executeAtivacaoWhiteList(whiteList.getIdWhiteList(), novoStatus);
					tfm.put(MSG_SUCESSO, ResultadoNotificacao.success(configuracoesFacade.getMensagem("RADAR-00021")).successMessage());
				}
			}	
		}
	}

	private String removeUltimaVirgulaTexto(String emailsSucesso) {
		if(emailsSucesso.length() > 0){
			emailsSucesso = emailsSucesso.trim();
			emailsSucesso = emailsSucesso.substring(0, emailsSucesso.length() - 1);
		}
		return emailsSucesso;
	}
	
	private void validaParametros(TypedFlatMap criteria){
		
		String[] parametrosObrigatorios = {TrackingContantsUtil.WHITE_LIST_PRM_DOCTO_SERVICO, TrackingContantsUtil.WHITE_LIST_PRM_TP_WHITE_LIST, TrackingContantsUtil.WHITE_LIST_PRM_SITUACAO, TrackingContantsUtil.WHITE_LIST_PRM_TIPO_ENVIO};
		
		List<String> lstParametros = new ArrayList<String>(Arrays.asList(parametrosObrigatorios));

		if(TrackingContantsUtil.WHITE_LIST_TIPO_PUSH.equals(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TP_WHITE_LIST))){
			lstParametros.add(TrackingContantsUtil.WHITE_LIST_PRM_TOKEN);
			 
		} else if(TrackingContantsUtil.WHITE_LIST_TIPO_EMAIL.equals(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_TP_WHITE_LIST))){
			lstParametros.add(TrackingContantsUtil.WHITE_LIST_PRM_LISTA_EMAILS);
		}
		
		List<String> camposNaoInformados = new ArrayList<String>();
		for(String prm : lstParametros){
			if(criteria.getString(prm) == null){
				camposNaoInformados.add(prm);
			}
		}
		
		if(!camposNaoInformados.isEmpty()){
			String msg = MessageFormat.format(configuracoesFacade.getMensagem("RADAR-00023"), camposNaoInformados);
			throw new InvalidParameterException(msg);			
		}
		
		validaTamanhoParametro(criteria, TrackingContantsUtil.WHITE_LIST_PRM_OBERVACAO, 255);
		validaTamanhoParametro(criteria, TrackingContantsUtil.WHITE_LIST_PRM_EMAIL_REMETENTE, 60);
		validaTamanhoParametro(criteria, TrackingContantsUtil.WHITE_LIST_PRM_NOME_REMETENTE, 60);
		validaTamanhoParametro(criteria, TrackingContantsUtil.WHITE_LIST_PRM_TOKEN, 255);
		validaTamanhoParametro(criteria, TrackingContantsUtil.WHITE_LIST_PRM_DDD, 5);
		validaTamanhoParametro(criteria, TrackingContantsUtil.WHITE_LIST_PRM_TELEFONE, 10);
		validaTamanhoParametro(criteria, TrackingContantsUtil.WHITE_LIST_PRM_VERSAO, 60);
		
		if(criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_LISTA_EMAILS) != null){
			String[] emails = criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_LISTA_EMAILS).split(",");
			for(String email : emails){
				validaTamanhoParametro("E-mail", 60, email);
			}
		}
	}
	
	private void validaTamanhoParametro(TypedFlatMap criteria, String parametro, Integer tamanho){
		String vlParametro = criteria.getString(parametro);
		validaTamanhoParametro(parametro, tamanho, vlParametro);
	}

	private void validaTamanhoParametro(String parametro, Integer tamanho, String vlParametro) {
		if(vlParametro != null && vlParametro.length() > tamanho){
			String msg = MessageFormat.format(configuracoesFacade.getMensagem("RADAR-00024"), parametro, tamanho);
			throw new InvalidParameterException(msg);
		}
	}	

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	public void setDispositivoContatoService(DispositivoContatoService dispositivoContatoService) {
		this.dispositivoContatoService = dispositivoContatoService;
	}

	public void setWhiteListService(WhiteListService whiteListService) {
		this.whiteListService = whiteListService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
