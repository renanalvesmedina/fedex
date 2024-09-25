package com.mercurio.lms.configuracoes.model.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.PropertyI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.IdiomaBean;
import com.mercurio.lms.configuracoes.model.IdiomaXMLBean;
import com.mercurio.lms.configuracoes.model.dao.mapper.AllViewsMapper;
import com.mercurio.lms.configuracoes.model.dao.mapper.IdiomaMapper;
import com.mercurio.lms.configuracoes.model.dao.mapper.IdiomaXMLMapper;
import com.mercurio.lms.configuracoes.model.dao.mapper.SistemaMapper;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */

public class DadosInternacionalizadosDAOImpl extends JdbcDaoSupport implements DadosInternacionalizadosDAO {
	private DomainValueService domainValueService;

	private RowMapper allViewsMapper = new AllViewsMapper();

	private RowMapper sistemaMapper = new SistemaMapper();

	private RowMapper idiomaMapper = new IdiomaMapper();

	/*static String COLUMN_LENGTH_SQL = "SELECT ATC.DATA_LENGTH " +
									  "FROM ALL_TAB_COLUMNS ATC, SISTEMA SIS " +
									  "WHERE ATC.TABLE_NAME = ? AND ATC.COLUMN_NAME = ? " +
											"AND ATC.OWNER = SIS.DB_OWNER AND SIS.ID_SISTEMA = ?";*/
	
	static String COLUMNS_SQL = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS ATC, SISTEMA SIS "+
								"WHERE ATC.TABLE_NAME = ? AND ATC.OWNER = SIS.DB_OWNER AND "+
								"SIS.ID_SISTEMA = ? "+
								"AND (ATC.DATA_TYPE='XMLTYPE') ORDER BY ATC.COLUMN_NAME";
	
	static String SISTEMAS_SQL = "SELECT ID_SISTEMA, NM_SISTEMA, DB_OWNER FROM SISTEMA ORDER BY NM_SISTEMA";

	public List findSistemas() {
		return getJdbcTemplate().query(SISTEMAS_SQL, sistemaMapper);
	}
	
	public ResultSetPage findDadosI18n(TypedFlatMap map) {

		FindDefinition definition = FindDefinition.createFindDefinition(map);
		final Integer pageSize = definition.getPageSize();
		final Integer currentPage = definition.getCurrentPage();
		Integer regInicial = (currentPage.intValue() - 1) * pageSize.intValue();
		Integer regFinal = (currentPage.intValue() * pageSize.intValue()) + 1;
		
		// lista para argumentos da query
		List argsList = new ArrayList();

		String sqlDados = getSqlDadosI18n(map, argsList);
		
		String sqlDadosPaginated = getSqlPaginated(sqlDados);
		// adiciona os filtros da paginacao
		argsList.add(regFinal);
		argsList.add(regInicial);
		
		List tabelasInternacionalizaveis = getJdbcTemplate().query(sqlDadosPaginated, argsList.toArray(), idiomaMapper);

		boolean hasPrior = currentPage.intValue() > 1;
		boolean hasNext = tabelasInternacionalizaveis.size() > pageSize.intValue();
		
		// remove o ultimo item da lista que foi utilizado apenas para determinar se existe uma proxima pagina de resultados
		if (hasNext) {
			tabelasInternacionalizaveis.remove(tabelasInternacionalizaveis.size() - 1);
		}

		return new ResultSetPage(currentPage, hasPrior, hasNext, tabelasInternacionalizaveis);
	}

	private String getSqlDadosI18n(TypedFlatMap filtrosTela, List sqlArgsList) {
		
		if (sqlArgsList==null) throw new IllegalArgumentException("sqlArgsList não pode ser null");
		if (filtrosTela==null || filtrosTela.isEmpty()) throw new IllegalArgumentException("filtrosTela não pode ser null ou vazio");
		
		String descricaoPortugues = filtrosTela.getString("descricaoPortugues");
		String descricaoTraduzida = filtrosTela.getString("descricaoTraduzida");
		boolean filtraPortugues = StringUtils.isNotBlank(descricaoPortugues);
		boolean filtraTraducao = StringUtils.isNotBlank(descricaoTraduzida);
		
		final String columnName = filtrosTela.getString("coluna");
		final String tableName = filtrosTela.getString("tabela.idTabela");
		final Integer idSistema = filtrosTela.getInteger("sistema");
		final String locale = filtrosTela.getString("locale");

		String owner = findDbOwnerByIdSistema(idSistema);
		
		StringBuffer sql = new StringBuffer()
			.append(getSqlProjecaoIdioma(tableName, columnName, getLocale(locale)))
			.append(getSqlFrom(owner, tableName))
			.append(" WHERE 1 = 1 ");
			
		if (filtraPortugues) {
			sql.append(" AND LOWER(").append(PropertyI18nProjection.createProjection("PORT." + columnName)).append(") LIKE ? ");
			sqlArgsList.add(descricaoPortugues.toLowerCase());
		}
		if (filtraTraducao) {
			sql.append(" AND LOWER(").append(PropertyI18nProjection.createProjection("PORT." + columnName)).append(") LIKE ? ");
			sqlArgsList.add(descricaoTraduzida.toLowerCase());
		}
		sql.append(" ORDER BY DS_PORT, DS_ESTR ");

		return sql.toString();
	}

	private String findDbOwnerByIdSistema(final Integer idSistema) {
		String FIND_OWNER_SISTEMA = "SELECT SIS.DB_OWNER FROM SISTEMA SIS WHERE ID_SISTEMA=?";
		
		String owner = (String) getJdbcTemplate().queryForObject(FIND_OWNER_SISTEMA, new Object[]{idSistema}, String.class);
		return owner;
	}

	public Integer getRowCountDadosI18n(TypedFlatMap map) {

		List sqlArgs = new ArrayList();
		Integer rows = getJdbcTemplate().queryForInt(getSqlRowCount(getSqlDadosI18n(map, sqlArgs)), sqlArgs.toArray());

		return rows;
	}
	
	public Integer findColumnLength(String tableName, String columnName, Integer idSistema) {
		//Integer columnLength = getJdbcTemplate().queryForInt(COLUMN_LENGTH_SQL, new Object[]{tableName, columnName, idSistema});
		Integer columnLength = Integer.valueOf(60); //TODO Criar restrição do tamanho da coluna (Com XMLType o tamanho da coluna é o tamanho do CLOB...)
		return columnLength;
	}

	public List findTabelasI18n(String tableName, Integer idSistema) {
		return getJdbcTemplate().query(getSqlTable(StringUtils.isNotBlank(tableName)), new Object[]{tableName.toUpperCase(), idSistema}, allViewsMapper);
	}

	public ResultSetPage findTabelasI18n(TypedFlatMap map) {
		FindDefinition definition = FindDefinition.createFindDefinition(map);
		String tableName = map.getString("nmTabela");
		Integer idSistema = map.getInteger("idSistema");
		if (idSistema == null) {
			throw new IllegalArgumentException("idSistema não pode ser null");
		}

		final Integer pageSize = definition.getPageSize();
		
		int regInicial = (definition.getCurrentPage().intValue() - 1)* pageSize.intValue();
		int regFinal = (definition.getCurrentPage().intValue() * pageSize.intValue()) + 1;
		
		List args = new ArrayList(4);
		if (StringUtils.isNotBlank(tableName)) {
			args.add(tableName);
		}
		args.add(idSistema);
		args.add(regFinal);
		args.add(regInicial);
		
		List tabelasInternacionalizaveis = getJdbcTemplate().query(getSqlPaginated(getSqlTable(StringUtils.isNotBlank(tableName))), args.toArray(), new ColumnMapRowMapper());
		
		boolean hasPrior = definition.getCurrentPage().intValue() > 1;
		boolean hasNext = tabelasInternacionalizaveis.size() > pageSize.intValue();
		
		if (hasNext) {
			tabelasInternacionalizaveis.remove(tabelasInternacionalizaveis.size() - 1);
		}
		
		ResultSetPage page = new ResultSetPage(definition.getCurrentPage(),	hasPrior, hasNext, tabelasInternacionalizaveis);
		return page;
	}

	public List findColunasTabelasI18n(String tableName, Integer idSistema) {
		Object[] args = new Object[] { tableName, idSistema };
		List colunas = getJdbcTemplate().query(COLUMNS_SQL, args, new ColumnMapRowMapper());
		return colunas;
	}

	public Integer getRowCountTabelasI18n(String tableName, Integer idSistema) {
		List args = new ArrayList(2);
		if (StringUtils.isNotBlank(tableName)) {
			args.add(tableName);
		}
		args.add(idSistema);
		return Integer.valueOf(getJdbcTemplate().queryForInt(getSqlRowCount(getSqlTable(StringUtils.isNotBlank(tableName))), args.toArray()));
	}

	public void storeUpdateDelete(String tableName, String columnName, Integer idSistema, Locale locale, List dadosI18n) {
		String owner = findDbOwnerByIdSistema(idSistema);
		try {
			IdiomaBean[] beans = new IdiomaBean[dadosI18n.size()];
			dadosI18n.toArray(beans);
			update(tableName, columnName, owner, beans);
		} catch (BadSqlGrammarException e) {
			// caso seja uma exceção de falta de privilégios no banco indica erro de permissão
			if (e.getCause().getMessage().indexOf("ORA-01031")>-1) {
				throw new BusinessException("Privilégios insuficientes no owner: "+owner);
			} else throw e;
		}
	}

	private void update(String tableName, String columnName, String owner, IdiomaBean[] idiomaBeans) {

		if (idiomaBeans.length > 0) {
			if(StringUtils.isBlank(tableName)) throw new IllegalArgumentException("tableName não pode ser vazio");
			if(StringUtils.isBlank(columnName)) throw new IllegalArgumentException("columnName não pode ser vazio");
			String ids = "";
			for (int i = 0; i < idiomaBeans.length; i++) {
				if(idiomaBeans[i].getIdPortugues()==null) throw new IllegalArgumentException("idIdiomaPortugues não pode ser vazio");
				if(idiomaBeans[i].getLocale()==null) throw new IllegalArgumentException("locale não pode ser vazio");
				//Grava uma string com os ids separados por virgula para usar em restrições adiante.
				if (!"".equals(ids)) {
					ids += ",";
				}
				ids += idiomaBeans[i].getIdPortugues();
	
				if (! StringUtils.isEmpty(idiomaBeans[i].getDescricaoTraduzida())) {
					//Verifica se existem traduções duplicadas dentro da lista (outro registro com a mesma tradução que o registro iterado).
					for (int ix = 0; ix < idiomaBeans.length; ix++) {
						if (ix != i && idiomaBeans[i].getDescricaoTraduzida().equals(idiomaBeans[ix].getDescricaoTraduzida())) {
							throw new BusinessException("traducaoJaCadastrada", new Object[]{idiomaBeans[i].getDescricaoTraduzida(), 
									getLocaleName(idiomaBeans[i].getLocale()), 
									findById(idiomaBeans[ix].getIdPortugues(), 
											owner, 
											tableName, 
											columnName, 
											idiomaBeans[ix].getLocale()).getDescricaoPortugues()});
						}
					}
				}
			}
	
			final List toUpdate = new ArrayList();
		
			IdiomaBean idiomaBean;
			IdiomaXMLBean idiomaXMLBean;
			List dadosXML = findDadosI18nXML(ids, owner, tableName, columnName);
			for (int i = 0; i < idiomaBeans.length; i++) {
				idiomaBean = idiomaBeans[i];
				int idxXMLBean = -1;
				//Busca o idiomaXMLBean com o mesmo id do idiomaBean
				for (int ix = 0; ix < dadosXML.size(); ix++) {
					if (((IdiomaXMLBean)dadosXML.get(ix)).getId().equals(idiomaBean.getIdPortugues())) {
						idxXMLBean = ix;
					}
				}
				if (idxXMLBean > -1) {
					idiomaXMLBean = (IdiomaXMLBean)dadosXML.get(idxXMLBean);
					if (StringUtils.isEmpty(idiomaBean.getDescricaoTraduzida())) {
						//Exclui o locale
						if (idiomaXMLBean.getDs().removeValue(idiomaBean.getLocale()) != null) {
							toUpdate.add(idiomaXMLBean);
						}
					} else {
						//Adiciona/atualiza o locale
						//Valida apenas os registros que não estão na lista que veio da tela, pois esta já foi verificada quanto a duplicidade de traduções.
						if (validateTraducaoDuplicada(ids, idiomaBean.getDescricaoTraduzida(), owner, tableName, columnName, idiomaBean.getLocale())) {
							//Caso encontrou algum registro duplicado, busca a lista e mostra o a descrição do primeiro para o usuário corrigir.
							List duplicados = findDescricaoPortTraducaoDuplicada(ids, idiomaBean.getDescricaoTraduzida(), owner, tableName, columnName, idiomaBean.getLocale());
							if (duplicados.size() > 0) {
								IdiomaBean duplicado = (IdiomaBean)duplicados.get(0);
								throw new BusinessException("traducaoJaCadastrada", new Object[]{idiomaBean.getDescricaoTraduzida(), getLocaleName(idiomaBean.getLocale()), duplicado.getDescricaoPortugues()});
							}
						}
						idiomaXMLBean.getDs().setValue(idiomaBean.getDescricaoTraduzida(), idiomaBean.getLocale());
						toUpdate.add(idiomaXMLBean);
					}
				}
			}

			//Batch da atualização dos registros que sofreram alterações.
			StringBuffer sql = new StringBuffer()
				.append(" update ").append(owner).append(".").append(tableName)
				.append(" set ").append(columnName).append(" = ?")
				.append(" where id_").append(tableName).append(" = ?");

			getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {

				public void setValues(PreparedStatement ps, int i) throws SQLException {
					IdiomaXMLBean dado = (IdiomaXMLBean)toUpdate.get(i);
					
					ps.setObject(1, dado.getDs());					
					ps.setInt(2, dado.getId());
				}

				public int getBatchSize() {
					return toUpdate.size();
				}
			});
		}
	}
	
	/**
	 * Retorna uma lista com o id e o campo de descrição no formato i18n com todos os locales, 
	 * restrito aos ids passados por parâmetro
	 * @param ids
	 * @param tableName
	 * @param columnName
	 * @param owner
	 * @return
	 */
	private List findDadosI18nXML(String ids, String owner, String tableName, String columnName) {
		StringBuffer sql = new StringBuffer()
			.append(" select PORT.ID_").append(tableName).append(" id")
			.append("       ,PORT.").append(columnName).append(".getClobVal() ds")
			.append(getSqlFrom(owner, tableName))
			.append(" where ID_").append(tableName).append(" in (").append(ids).append(")");
		
		List toReturn = getJdbcTemplate().query(sql.toString(), new IdiomaXMLMapper());
		return toReturn;
	}
	
	/**
	 * Retorna se existe algum registro com a mesma tradução e mesmo locale, mas que não esteja na lista de ids informada
	 * @return
	 */
	private boolean validateTraducaoDuplicada(String ids, String traducao, String owner, String tableName, String columnName, Locale locale) {
		StringBuffer sql = new StringBuffer()
			.append(" SELECT count(*) as qt ")
			.append(getSqlTraducaoDuplicada(ids, traducao, owner, tableName, columnName, locale));
		
		return getJdbcTemplate().queryForInt(sql.toString(), new Object[] {traducao.toLowerCase()}) > 0;
	}
	
	private List findDescricaoPortTraducaoDuplicada(String ids, String traducao, String owner, String tableName, String columnName, Locale locale) {
		String sql =
			getSqlProjecaoIdioma(tableName, columnName, locale) +
			getSqlTraducaoDuplicada(ids, traducao, owner, tableName, columnName, locale);
		
		return getJdbcTemplate().query(sql, new Object[] {traducao.toLowerCase()}, idiomaMapper);
	}
	
	private IdiomaBean findById(Integer id, String owner, String tableName, String columnName, Locale locale) {
		StringBuffer sql = new StringBuffer()
			.append(getSqlProjecaoIdioma(tableName, columnName, locale))
			.append(getSqlFrom(owner, tableName))
			.append(" where id_" + tableName + " = ? ");

		return (IdiomaBean)getJdbcTemplate().queryForObject(sql.toString(), new Object[] {id}, idiomaMapper);
    }
	
	private String getSqlProjecaoIdioma(String tableName, String columnName, Locale locale) {
		StringBuffer sql = new StringBuffer()
			.append(" SELECT PORT.ID_").append(tableName).append(" as ID_PORT ")
			.append(" ,").append(PropertyI18nProjection.createProjection("PORT." + columnName, "DS_PORT"))
			.append(" ,").append(PropertyI18nProjection.createProjection("PORT."+columnName, "DS_ESTR", locale))
			.append(" ,60 COLUMN_LENGTH "); //TODO Criar restrição do tamanho da coluna (Com XMLType o tamanho da coluna é o tamanho do CLOB...)
		return sql.toString();
	}
	
	private String getSqlFrom(String owner, String tableName) {
		return " from "+owner+"."+tableName+" PORT ";
	}
	
	private String getSqlTraducaoDuplicada(String ids, String traducao, String owner, String tableName, String columnName, Locale locale) {
		StringBuffer sql = new StringBuffer()
			.append(getSqlFrom(owner, tableName))
			.append(" WHERE not port.id_").append(tableName).append(" in (").append(ids).append(") ")
			.append("   and LOWER(").append(PropertyI18nProjection.createProjection("PORT." + columnName)).append(") = ? ");
		return sql.toString();
	}

	private String getSqlTable(boolean filterByTableName) {
		
		String TABLES_SQL = "SELECT at.table_name view_name "+
							"FROM all_tables at, "+
							"sistema sis "+
								"WHERE {0} "+ // placeholder para o filtro por table
							" at.owner = sis.db_owner "+
								"AND sis.id_sistema = ? "+
								"and exists (select 1 from all_tab_columns atc "+ 
										"where atc.table_name=at.table_name and "+ 
										"data_type='''XMLTYPE''' and  "+
											"atc.owner=sis.db_owner) "+
							"ORDER BY at.table_name ";
		
		String tableName = "";
		if (filterByTableName) {
			tableName = " UPPER(AT.TABLE_NAME) LIKE UPPER(?) AND ";
		}

		return MessageFormat.format(TABLES_SQL, new Object[]{tableName});
	}

	/**
	 * Adiciona um wrapper SQL na query fornecida para permitir obtenção da quantidade de registros
	 * 
	 * @param sql
	 * @return
	 */
	public String getSqlRowCount(String sql) {
		
		return "SELECT COUNT(*) FROM(" + sql + ")";
	}

	/**
	 * Adiciona um wrapper SQL na query fornecida para permitir paginação da consulta
	 * @param tableFilter
	 * @return
	 */
	public static String getSqlPaginated(String listQuery) {
		
		StringBuffer query = new StringBuffer();
		query.append("SELECT * FROM ( SELECT ROW_.*, ");
		query.append("ROWNUM ROWNUM_ FROM (");
		query.append(listQuery);
		query.append(") ROW_ ) WHERE ROWNUM_ <= ? ");
		query.append("AND ROWNUM_ > ?");
		return query.toString();
		
	}

	private static Locale getLocale(String locale) {
		return new Locale(locale.substring(0, 2), locale.substring(3));
	}

	/**
	 * Retorna a descrição do locale
	 * @param locale
	 * @return
	 */
	private String getLocaleName(Locale locale) {
		return domainValueService.findDomainValueDescription("TP_LINGUAGEM", locale.toString());
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
}
