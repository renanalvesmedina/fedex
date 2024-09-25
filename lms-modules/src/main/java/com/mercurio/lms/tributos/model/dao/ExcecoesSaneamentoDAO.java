package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.param.MCDParam;

/**
 * DAO pattern.
 * 
 * Aqui encontra-se o SQL utilizado na emissão do relatório de MCD e na consulta
 * de MCD. ATENÇÃO! Muito cuidado ao realizar uma alteração. Analisar o impacto
 * em ambas as implementações.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ExcecoesSaneamentoDAO extends AdsmDao {

	public static SqlTemplate getSqlBasic(TypedFlatMap tfm) {
		List<Object> values = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT eai.id_excecao_atualizacao_int as id_excecao_atualizacao_int \n");
		sb.append(",      eai.nm_interface as nm_interface \n");
		sb.append(",      eai.ds_ocorrencia as ds_ocorrencia \n");
		sb.append(",      eai.nm_arquivo as nm_arquivo \n");
		sb.append(",      eai.ds_conteudo_linha as ds_conteudo_linha \n");
		sb.append(",      TO_CHAR(eai.dh_inclusao,'DD/MM/YYYY HH24:MI:SS') as dh_inclusao \n");
		sb.append("FROM EXCECAO_ATUALIZACAO_INT eai \n");
		sb.append("where 1=1 ");
		sb.append(expressao("      and UPPER(nm_interface) LIKE ? \n",tfm.getBoolean("CompletaCNPJ")?"ATUALIZAÇÃO COMPLETA - PESSOA JURÍDICA%":tfm.getBoolean("SimplesNacional")?"ATUALIZAÇÃO SIMPLES NACIONAL%":tfm.getBoolean("Suframa")?"ATUALIZAÇÃO SUFRAMA%":null,values));
		sb.append(expressao("      and trunc(eai.dh_inclusao) <= ? \n",tfm.getYearMonthDay("dtDataFinal"),values));
		sb.append(expressao("      and trunc(eai.dh_inclusao) >= ? \n",tfm.getYearMonthDay("dtDataInicial"),values));
		
		sb.append("ORDER BY nm_interface, eai.dh_inclusao \n");
		System.out.println(tfm);
		
		SqlTemplate sqlTemplate = new SqlTemplate(sb.toString());
		sqlTemplate.addCriteriaValue(values.toArray());
		return sqlTemplate;
	}

	private static String expressao(String string, Object e, List<Object> values) {
		if(e!=null){
			values.add(e);
			return string;
		}else{
			return " ";
		}
	}

	public static final SqlTemplate getSqlTemplate(SqlTemplate sql, TypedFlatMap values) {
		if (sql == null) {
			throw new IllegalArgumentException("Argumento de indice 0 é obrigatório.");
		}

		SqlTemplate sqlt = getSqlBasic(values);
		sql.addCriteriaValue(sqlt.getCriteria());
		sql.add(sqlt.toString());

		return sql;
	}

}