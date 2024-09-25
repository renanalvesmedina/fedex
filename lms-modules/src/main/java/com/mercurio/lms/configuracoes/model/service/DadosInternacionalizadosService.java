package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Locale;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.dao.DadosInternacionalizadosDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.manterDadosInternacionalizadosService"
 */ 

public class DadosInternacionalizadosService {

	private DadosInternacionalizadosDAO dao;
	private DomainValueService domainValueService;
	private DomainService domainService;

	
	public void setDadosInternacionalizadosDao(DadosInternacionalizadosDAO dao) {
		this.dao = dao;
	}	

	public Integer findColumnLength(String tableName, String columnName, Integer idSistema) {
		return dao.findColumnLength(tableName, columnName, idSistema);
	}
	
	public List findSistemas() {
		return dao.findSistemas();
	}
	
	public ResultSetPage findDadosI18n(TypedFlatMap criteria) {
		return dao.findDadosI18n(criteria);
	}
	
	public Integer getRowCountDadosI18n(TypedFlatMap criteria) {
		return dao.getRowCountDadosI18n(criteria);
	}

	public List findTabelasI18n(String tableName, Integer idSistema) {
		return dao.findTabelasI18n(tableName, idSistema);
	}
	
	public  ResultSetPage findTabelasI18nLookup(TypedFlatMap criteria) {
		return dao.findTabelasI18n(criteria);
	}
	
	public List findColunasTabelasI18n(String tableName, Integer idSistema) {
		return dao.findColunasTabelasI18n(tableName, idSistema);
	}
		
	public Integer getRowCountTabelasI18n(String tableName, Integer idSistema) {
		return dao.getRowCountTabelasI18n(tableName, idSistema);
	}
	
	public void storeUpdateDelete(String tableName, String columnName, Integer idSistema, Locale locale, List dadosI18n) {
		dao.storeUpdateDelete(tableName, columnName, idSistema, locale, dadosI18n);
		
		if (tableName.equalsIgnoreCase("valor_dominio")) {
			// invalida a cache de valor dominio
			domainValueService.invalidateCache();
			domainService.executeInvalidateCache();
		}else if (tableName.equalsIgnoreCase("dominio")) {
			// invalida a cache de valor dominio
			domainService.executeInvalidateCache();
		}
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}

