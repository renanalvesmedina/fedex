 package com.mercurio.lms.contasreceber.model.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CedenteDAO extends BaseCrudDao<Cedente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @Override
	protected final Class getPersistentClass() {
        return Cedente.class;
    }
    
    /**
     * Find da combo padrão
     * 
     * Mickaël Jalbert - 19/10/2005
     * 
     * */    
    public List findComboPadrao() {
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("distinct ba");
    	sql.addFrom(Cedente.class.getName()+" ce join ce.agenciaBancaria ag join ag.banco ba");
    	sql.addOrderBy("ba.nmBanco");
    	
    	return getHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
    public List findCombo(Map map) {
    	return getHibernateTemplate().find(
    			"select cedente.idCedente, " +
	    			"cedente.dsCedente, " +
	    			"ban.tpSituacao, " +
	    			"ag.tpSituacao, " +
	    			"cedente.dtVigenciaInicial, " +
	    			"cedente.dtVigenciaFinal " +
    			"from com.mercurio.lms.contasreceber.model.Cedente as cedente " +
    			"INNER JOIN cedente.agenciaBancaria ag " +
    			"INNER JOIN ag.banco ban " +
    	    	"order by cedente.dsCedente"		
    	);
    }
    
    /**
     * Método responsável por retornar os Cedentes ativos para popular a combo
     * @param idCedente
     * @return List 
     */
    public List findComboByActiveValues(Long idCedente){
    	YearMonthDay ymd = JTDateTimeUtils.getDataAtual();
    	
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("new Map(cedente.idCedente as idCedente, cedente.dsCedente as dsCedente)");
    	hql.addInnerJoin(Cedente.class.getName(), "cedente");
    	hql.addInnerJoin("cedente.agenciaBancaria", "ag");
    	hql.addInnerJoin("ag.banco", "ban");
    	
    	String query = "(ag.tpSituacao = ? " +
    			       "and ban.tpSituacao = ?" +
    			       "and ? between cedente.dtVigenciaInicial and cedente.dtVigenciaFinal ";
    	
    	hql.addCriteriaValue("A");
    	hql.addCriteriaValue("A");
    	hql.addCriteriaValue(ymd);
    	
    	if( idCedente != null ){
    		query += " or (cedente.idCedente = ? ))";
    		hql.addCriteriaValue(idCedente);
    	} else {
    		query += ")";
    	}
    	
    	hql.addCustomCriteria(query);

    	hql.addOrderBy("cedente.dsCedente");
    	
    	return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    /**
     * Método responsável por retornar os Cedentes ativos ou ligado a fatura informada
     * 
     * @author Mickaël Jalbert
     * @since 24/11/2006
     * 
     * @param idFatura
     * @return List 
     */
    public List findCedenteAtivoByIdFatura(Long idFatura){
    	YearMonthDay ymd = JTDateTimeUtils.getDataAtual();
    	
    	SqlTemplate hql = mountHqlCedenteAtivoByIdFatura(idFatura, ymd);
    	
    	hql.addProjection("distinct cedente");
    	
    	return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    /**
     * Método responsável por retornar os maps de cedentes ativos ou ligado a fatura informada
     * 
     * @author Mickaël Jalbert
     * @since 24/11/2006
     * 
     * @param idFatura
     * @return List 
     */
    public List findCedenteAtivoByIdCedente(Long idCedente){
    	YearMonthDay ymd = JTDateTimeUtils.getDataAtual();

    	SqlTemplate hql = new SqlTemplate();

    	hql.addProjection("distinct new Map(cedente.idCedente as idCedente, cedente.dsCedente as comboText, (CASE WHEN (? between cedente.dtVigenciaInicial and cedente.dtVigenciaFinal) THEN 'A' ELSE 'I' END) as status)");
    	hql.addCriteriaValue(ymd);
    	
    	mountCedenteAtivoByIdCedente(hql, idCedente, ymd);
    	
    	return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
	private SqlTemplate mountCedenteAtivoByIdCedente(SqlTemplate hql, Long idCedente, YearMonthDay ymd) {
    	hql.addInnerJoin(Cedente.class.getName(), "cedente");
    	hql.addInnerJoin("cedente.agenciaBancaria", "ag");
    	hql.addInnerJoin("ag.banco", "ban");
    	
    	String query = "(cedente.agenciaBancaria.tpSituacao = ? " +
    			       "and ban.tpSituacao = ? " +
    			       "and ? between cedente.dtVigenciaInicial and cedente.dtVigenciaFinal ";
    	
    	hql.addCriteriaValue("A");
    	hql.addCriteriaValue("A");
    	hql.addCriteriaValue(ymd);
    	
    	
    	if (idCedente != null ){
    		query += " or (cedente.id = ?))";
    		hql.addCriteriaValue(idCedente);
    	} else {
    		query += ")";
    	}
    	
    	hql.addCustomCriteria(query);

    	hql.addOrderBy("cedente.dsCedente");
		return hql;
	}      

	private SqlTemplate mountHqlCedenteAtivoByIdFatura(Long idFatura, YearMonthDay ymd) {
		SqlTemplate hql = new SqlTemplate();
    	
    	hql.addInnerJoin(Cedente.class.getName(), "cedente");
    	hql.addInnerJoin("cedente.agenciaBancaria", "ag");
    	hql.addInnerJoin("ag.banco", "ban");
    	
    	String query = "(cedente.agenciaBancaria.tpSituacao = ? " +
    			       "and ban.tpSituacao = ? " +
    			       "and ? between cedente.dtVigenciaInicial and cedente.dtVigenciaFinal ";
    	
    	hql.addCriteriaValue("A");
    	hql.addCriteriaValue("A");
    	hql.addCriteriaValue(ymd);
    	
    	
    	if (idFatura != null ){
    		query += " or (cedente.id = (select fat.cedente.id from "+ Fatura.class.getName()+" fat where fat.id = ?) ))";
    		hql.addCriteriaValue(idFatura);
    	} else {
    		query += ")";
    	}
    	
    	hql.addCustomCriteria(query);

    	hql.addOrderBy("cedente.dsCedente");
		return hql;
	}    
    
    /**
     * Método que busca as Cedente de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
    public ResultSetPage findPaginatedByCedente(TypedFlatMap criteria){
    	
    	/** Define os parametros para paginação */
    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	
    	/** Resgata os parâmetros do TypedFlatMap para serem filtrados no HQL */
    	Long idBanco = criteria.getLong("agenciaBancaria.banco.idBanco");
    	Long idAgenciaBancaria = criteria.getLong("agenciaBancaria.idAgenciaBancaria");
    	YearMonthDay dataVigenciaTmp = criteria.getYearMonthDay("dataVigencia");
    	Long cdCedente = criteria.getLong("cdCedente");
    	String dsCedente = criteria.getString("dsCedente");
    	String nrContaCorrente = criteria.getString("nrContaCorrente");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( c.idCedente as idCedente, ")
    				.append(" b.nmBanco as nmBanco, ")
    				.append(" ab.nmAgenciaBancaria as nmAgenciaBancaria, ")
    				.append(" c.dsCedente as dsCedente, ")
    				.append(" c.nrContaCorrente as nrContaCorrente, ")
    				.append(" c.nrCarteira as nrCarteira, ")
    				.append(" c.dtVigenciaInicial as dtVigenciaInicial, ")
    				.append(" c.dtVigenciaFinal as dtVigenciaFinal) ")
    				.toString()
    		);
    	
    	sql.addFrom(getPersistentClass().getName() + " as c JOIN c.agenciaBancaria as ab " +
    			"JOIN ab.banco as b");
    	
    	/** Monta os filtros */
    	sql.addCriteria("ab.idAgenciaBancaria", "=", idAgenciaBancaria);
    	sql.addCriteria("b.idBanco", "=", idBanco);
		sql.addCriteria("c.cdCedente", "=", cdCedente);
		sql.addCriteria("lower(c.dsCedente)", "like", dsCedente.toLowerCase());
		sql.addCriteria("c.nrContaCorrente", "=", nrContaCorrente);
		
		if(dataVigenciaTmp != null){
			YearMonthDay dataVigencia = (criteria.getYearMonthDay("dataVigencia"));
			sql.addCustomCriteria("(? BETWEEN c.dtVigenciaInicial AND c.dtVigenciaFinal)");
			sql.addCriteriaValue(dataVigencia);
		}
		
		sql.addOrderBy("c.dsCedente");
    	
    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());

    	return rsp;
	}
    
    /**
     * Método que retorna o número de registros de acordo com os filtros passados
     * @param criteria
     * @return Integer
     */
    public Integer getRowCountByCedente(TypedFlatMap criteria) {
    	
    	/** Resgata os parâmetros do TypedFlatMap para serem filtrados no HQL */
    	Long idBanco = criteria.getLong("agenciaBancaria.banco.idBanco");
    	Long idAgenciaBancaria = criteria.getLong("agenciaBancaria.idAgenciaBancaria");
    	YearMonthDay dataVigenciaTmp = criteria.getYearMonthDay("dataVigencia");
    	Long cdCedente = criteria.getLong("cdCedente");
    	String dsCedente = criteria.getString("dsCedente");
    	String nrContaCorrente = criteria.getString("nrContaCorrente");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("count(c.idCedente)");
    	
    	sql.addFrom(getPersistentClass().getName() + " as c JOIN c.agenciaBancaria as ab " +
    		"JOIN ab.banco as b");

		/** Monta os filtros */ 
		sql.addCriteria("ab.idAgenciaBancaria", "=", idAgenciaBancaria);
		sql.addCriteria("b.idBanco", "=", idBanco);
		sql.addCriteria("c.cdCedente", "=", cdCedente);
		sql.addCriteria("lower(c.dsCedente)", "like", dsCedente.toLowerCase());
		sql.addCriteria("c.nrContaCorrente", "=", nrContaCorrente);
		if(dataVigenciaTmp != null){
			YearMonthDay dataVigencia = criteria.getYearMonthDay("dataVigencia");
			sql.addCustomCriteria("(? BETWEEN c.dtVigenciaInicial AND c.dtVigenciaFinal)");
			sql.addCriteriaValue(dataVigencia);
		}
		sql.addOrderBy("b.nmBanco, ab.nmAgenciaBancaria, c.cdCedente");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
    }

    /** Método invocado aumáticamente pelo framework, seta os atributos que estão com lazy = true para false */
    @Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("agenciaBancaria", FetchMode.JOIN);
    	lazyFindById.put("agenciaBancaria.banco", FetchMode.JOIN);
    }

    /**
	 * Método que valida se a vigência do cedente altual
	 * Se o idCendente for informado, ele pesquisa onde os cedentes são diferente daquele informado
	 * 
	 * @author Mickaël Jalbert
	 * 24/03/2006
	 * 
	 * @param YearMonthDay dtInicial
	 * @param YearMonthDay dtFinal
	 * @param Long idAgenciaBancaria
	 * @param Long idBanco
	 * @param Long idCedente
	 * @return List
	 */
    public List findCedenteByVigencia(YearMonthDay dtInicial, YearMonthDay dtFinal, Long idAgenciaBancaria, Long idBanco, Long idCedente, Short nrCarteira){
    	
    	SqlTemplate sql = mountHql(dtInicial, dtFinal, idAgenciaBancaria, idBanco, nrCarteira);
    	
    	sql.addCriteria("c.id", "!=", idCedente);
    	
    	List list = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());

    	return list;
	}
    
    private SqlTemplate mountHql(YearMonthDay dtInicial, YearMonthDay dtFinal, Long idAgenciaBancaria, Long idBanco, Short nrCarteira){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append(" c ")
    				.toString()
    		);
    	
    	sql.addFrom(getPersistentClass().getName() + " as c JOIN c.agenciaBancaria as ab " +
    			"JOIN ab.banco as b");
    	
    	/** Monta os filtros */
    	sql.addCriteria("ab.idAgenciaBancaria", "=", idAgenciaBancaria);
    	sql.addCriteria("b.idBanco", "=", idBanco);
    	sql.addCriteria("c.nrCarteira", "=", nrCarteira);
    
    	// critério de vigencia refeito        
		if (dtFinal == null)
			dtFinal = JTDateTimeUtils.MAX_YEARMONTHDAY;

    	if (dtInicial != null && dtFinal != null) {
			sql.addCustomCriteria(" ( (c.dtVigenciaFinal >= ? and c.dtVigenciaInicial <= ?) or" +
	        		" (c.dtVigenciaFinal >= ? and c.dtVigenciaInicial <= ?) or" +
	        		" (c.dtVigenciaFinal < ? and c.dtVigenciaInicial > ?))");
			
			
			 sql.addCriteriaValue(dtInicial);
		     sql.addCriteriaValue(dtInicial);
		     sql.addCriteriaValue(dtFinal);
		     sql.addCriteriaValue(dtFinal);
		     sql.addCriteriaValue(dtFinal);
		     sql.addCriteriaValue(dtInicial);
		}
			    	
        return sql;
    }
    
    /**
     * Busca os cedentes de acordo com os Critérios de Conta Corrente, Número da agencia e número do Banco
     * @param contaCorrente
     * @param nrAgencia
     * @param nrBanco
     * @return Lista de Cedentes
     */
    public List findCedentes(String contaCorrente, Long nrAgencia, Long nrBanco) {
		
		SqlTemplate sql = getQueryHqlCedentes(contaCorrente,nrAgencia,nrBanco);
		
		sql.addProjection("ced");		
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

    /**
     * Query para busca de cedentes.
     * OBS : Cada método que usar esta query deve seta a sua projection e order by
     * @param contaCorrente
     * @param nrAgencia
     * @param nrBanco
     * @return Lista de Cedentes
     */
	private SqlTemplate getQueryHqlCedentes(String contaCorrente, Long nrAgencia, Long nrBanco){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addInnerJoin(Cedente.class.getName(),"ced");
		sql.addInnerJoin("ced.agenciaBancaria","ab");
		sql.addInnerJoin("ab.banco","b");
		
		sql.addCriteria("ced.nrContaCorrente", "=", (Integer.valueOf(contaCorrente)).toString());
		sql.addCriteria("ab.nrAgenciaBancaria", "=", Short.valueOf(nrAgencia.toString()));
		sql.addCriteria("b.nrBanco","=",Short.valueOf(nrBanco.toString()));		
		
		return sql;
		
	}

	/**
	 * Retorna o cedente do cliente informado se existe, senão retorna null.
	 * 
	 * @author Mickaël Jalbert
	 * @since 29/06/2006
	 * 
	 * @param Long idCliente
	 * @return Cedente
	 * */
	public Cedente findByIdCliente(Long idCliente){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("ce");
		
		hql.addInnerJoin(Cedente.class.getName(), "ce");
		hql.addInnerJoin("ce.clientes", "c");
		if(idCliente != null){
		hql.addCriteria("c.id", "=", idCliente);
		}

		List lstCedente = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		if (lstCedente != null && !lstCedente.isEmpty() && lstCedente.size() == 1){
			return (Cedente)lstCedente.get(0);
		} else {
			return null;
		}
	}
	
	
	/**
	 * Retorna o cedente do cliente informado se existe, senão retorna null.
	 * 
	 * @author Mickaël Jalbert
	 * @since 03/07/2006
	 * 
	 * @param Long idFilial
	 * @return Cedente
	 * */
	public Cedente findByIdFilial(Long idFilial){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("ce");
		
		hql.addInnerJoin(Cedente.class.getName(), "ce");
		hql.addInnerJoin("ce.filiaisByIdCedenteBloqueto", "fil");
		
		hql.addCriteria("fil.idFilial", "=", idFilial);

		List lstCedente = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		if (lstCedente != null && !lstCedente.isEmpty() && lstCedente.size() == 1){
			return (Cedente)lstCedente.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since  17/08/2006
	 * 
	 * Se o cliente possui um cedente, retorna este para a combo "Cedente", 
	 * caso contrário, traz o cedente da filial de cobrança do cliente, ou seja utilizar 
	 * FILIAL.ID_CEDENTE_BLOQUETO onde FILIAL.ID_FILIAL = CLIENTE.ID_FILIAL_COBRANCA.
	 * 
	 * @param tfm IdCliente
	 * @return Cedente
	 */
	public Cedente findCedenteByFilialCobrancaCliente(Long idCliente) {
		    	
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("cedente");
    	hql.addInnerJoin(Cliente.class.getName(),"c");
    	hql.addInnerJoin("c.filialByIdFilialCobranca","fCob");
    	hql.addLeftOuterJoin("fCob.cedenteByIdCedenteBloqueto","cedente");
    	
    	hql.addCriteria("c.id","=",idCliente);
    	
    	List cedentes = getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	Cedente retorno = null;
    	
    	if(cedentes != null && !cedentes.isEmpty()){
    		retorno = (Cedente) cedentes.get(0);
    	}
    	
    	return retorno;
	}	
	
	 /**
	  * Método responsável por retornar os Cedentes ativos 
	  *
	  * @author Hector Julian Esnaola Junior
	  * @since 07/11/2006
	  *
	  * @return
	  *
	  */
    public List<Map> findCedentesAtivos(){
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("new Map(cedente.idCedente as idCedente)");
    	hql.addInnerJoin(Cedente.class.getName(), "cedente");
    	hql.addInnerJoin("cedente.agenciaBancaria", "ag");
    	hql.addInnerJoin("ag.banco", "ban");
    	
    	hql.addCriteria("ag.tpSituacao","=","A");
    	hql.addCriteria("ban.tpSituacao","=","A");
    	
		hql.addCustomCriteria("( (? >= cedente.dtVigenciaInicial and cedente.dtVigenciaFinal is null) OR (? >= cedente.dtVigenciaInicial and ? <= cedente.dtVigenciaFinal) )");
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());    
    	
		hql.addOrderBy("cedente.dsCedente");
    	
    	return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }    
    
	/**
	 * Retorna true se o cedente informado é ativo
	 * 
	 * @author Mickaël Jalbert
	 * @since 01/03/2007
	 * 
	 * @param idCedente
	 * @return
	 */
	public boolean isCedenteAtivo(Long idCedente){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("1");
    	hql.addInnerJoin(Cedente.class.getName(), "c");

    	
		hql.addCustomCriteria(" ? between c.dtVigenciaInicial and c.dtVigenciaFinal");
		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());		
    	
		hql.addCriteria("c.id", "=", idCedente);
    	
    	return getHibernateTemplate().find(hql.getSql(), hql.getCriteria()).size() > 0?true:false;
	}    
    
	/**
	 * Carrega um cedente de acordo com o banco, a agência, o número da conta corrente ou o código do cedente.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 03/07/2007
	 *
	 * @param contaCorrente
	 * @param cdCedente
	 * @param nrAgencia
	 * @param nrBanco
	 * @return
	 *
	 */
	public Cedente findCedenteByAgenciaAndBanco(String contaCorrente, Long cdCedente, Short nrAgencia, Short nrBanco){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection(" ced ");
		
		sql.addInnerJoin(Cedente.class.getName(),"ced");
		sql.addInnerJoin("ced.agenciaBancaria","ab");
		sql.addInnerJoin("ab.banco","b");
		
		sql.addCustomCriteria(" ( ced.nrContaCorrente = ? OR ced.cdCedente = ? ) ");
		sql.addCriteriaValue(contaCorrente);
		sql.addCriteriaValue(cdCedente);
		sql.addCriteria("ab.nrAgenciaBancaria","=",nrAgencia);
		sql.addCriteria("b.nrBanco","=",nrBanco);		
		
		return (Cedente) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		
	}
	public Long findIdCedenteAtual(){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection(" ced.idCedente ");
		sql.addFrom(Cedente.class.getName(), "ced");
		sql.addCriteria("ced.dtVigenciaInicial","<=",JTDateTimeUtils.getDataAtual());
		sql.addCriteria("ced.dtVigenciaFinal",">=", JTDateTimeUtils.getDataAtual());		
		
		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		
	}
}