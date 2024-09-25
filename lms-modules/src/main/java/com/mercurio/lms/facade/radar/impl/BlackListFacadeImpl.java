package com.mercurio.lms.facade.radar.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.BlackList;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.service.BlackListService;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.facade.radar.BlackListFacade;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * @spring.bean id="lms.radar.blackListFacade"
 */
@ServiceSecurity
public class BlackListFacadeImpl implements BlackListFacade{

	private static final Logger LOGGER = LogManager.getLogger(BlackListFacadeImpl.class);
	
	private BlackListService blackListService;
	private ContatoService contatoService;

	
	/**
	 * Salva ou atualiza a tabela de blackList
	 */
	@Override
	@MethodSecurity(processGroup = "radar.blackList", processName = "storeBlackListByIdContato", authenticationRequired=false)
	public TypedFlatMap storeBlackListByIdContato(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		List<String> msgErro = new ArrayList<String>();
		Contato contato = contatoService.findById(criteria.getLong("idContato"));
		try {
			BlackList blackList = new BlackList();
			blackList.setContato(contato);
			blackList.setDtInclusao(JTDateTimeUtils.getDataAtual());
			blackListService.store(blackList);
			
		} catch (Exception ex) {
			LOGGER.error("Ocorreu problemas no store blackList: ", ex);
			msgErro.add("Problemas ao inserir o registro. Erro: "+ex.getMessage());
		}
		tfm.put("msgErro", msgErro);
		return tfm;
	}
	
	public void setBlackListService(BlackListService blackListService) {
		this.blackListService = blackListService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	
}
