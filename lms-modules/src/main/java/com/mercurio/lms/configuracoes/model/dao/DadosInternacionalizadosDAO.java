package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Locale;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *  
 */

public interface DadosInternacionalizadosDAO {

	List findSistemas();
	Integer findColumnLength(String tableName, String columnName, Integer idSistema);
	ResultSetPage findDadosI18n(TypedFlatMap map);
	Integer getRowCountDadosI18n(TypedFlatMap map);
	ResultSetPage findTabelasI18n(TypedFlatMap map);
	List findTabelasI18n(String tableName, Integer idSistema);
	List findColunasTabelasI18n(String tableName, Integer idSistema);
	Integer getRowCountTabelasI18n(String tableName, Integer idSistema);
	void storeUpdateDelete(String tableName, String columnName, Integer idSistema, Locale locale, List dadosI18n);
}
