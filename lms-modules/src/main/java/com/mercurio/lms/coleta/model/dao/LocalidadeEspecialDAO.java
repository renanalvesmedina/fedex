package com.mercurio.lms.coleta.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.lms.coleta.model.LocalidadeEspecial;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LocalidadeEspecialDAO extends BaseCrudDao<LocalidadeEspecial, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return LocalidadeEspecial.class;
    }

	protected void initFindByIdLazyProperties(Map map) {
		map.put("filial", FetchMode.JOIN);
		map.put("filial.pessoa", FetchMode.JOIN);
		map.put("unidadeFederativa", FetchMode.JOIN);
		map.put("unidadeFederativa.pais", FetchMode.JOIN);
		super.initFindByIdLazyProperties(map);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("filial", FetchMode.JOIN);
		map.put("filial.pessoa", FetchMode.JOIN);
		map.put("unidadeFederativa", FetchMode.JOIN);
		map.put("unidadeFederativa.pais", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(map);
	}
	
	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT c.id_localidade_especial as idLocalidadeEspecial, c.ds_localidade_i as dsLocalidade ");
		sql.append("  FROM localidade_especial c ");
		sql.append("  where tp_situacao = 'A' ");
		
		if (filter.get("dsLocalidade") != null) {
			sql.append(" and LOWER(c.ds_localidade_i) like LOWER(:dsLocalidade)");
			filter.put("dsLocalidade", "%" + filter.get("dsLocalidade") + "%");
		}

		
		return new ResponseSuggest(sql.toString(), filter);
	}
	
}