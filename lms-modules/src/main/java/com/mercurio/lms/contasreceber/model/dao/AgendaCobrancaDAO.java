package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.AgendaCobranca;
import com.mercurio.lms.contasreceber.model.CobrancaInadimplencia;
import com.mercurio.lms.contasreceber.model.LigacaoCobranca;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AgendaCobrancaDAO extends BaseCrudDao<AgendaCobranca, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AgendaCobranca.class;
    }
    
    /**
     * Método responsável por carregar dados páginados de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage contendo o resultado do hql.
     */
	public ResultSetPage findPaginatedByAgendaCobranca(TypedFlatMap criteria) throws Exception{
		
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	
    	Long idUsuario = criteria.getLong("idUsuario");
    	Long idCliente = criteria.getLong("cobrancaInadimplencia.cliente.idCliente");
    	DateTime dtInicial = criteria.getDateTime("dhAgendaCobrancaInicial");
    	DateTime dtFinal = criteria.getDateTime("dhAgendaCobrancaFinal");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( ac.idAgendaCobranca as idAgendaCobranca, ")
    				.append(" ac.dhAgendaCobranca as dhAgendaCobranca, ")
    				.append(" ac.dsAgendaCobranca as dsAgendaCobranca, ")
    				.append(" c.nmContato as nmContato, ") 
    				.append(" lc.dhLigacaoCobranca as dhLigacaoCobranca, ")
    				.append(" u.nmUsuario as nmUsuario, ")
    				.append(" ci.blCobrancaEncerrada as blCobrancaEncerrada, ")
    				.append(" p.nmPessoa as nmPessoa ) ")
    				.toString()
    		);
    	
    	mountHqlPaginated(idUsuario, idCliente, dtInicial, dtFinal, sql);
    	
    	sql.addOrderBy("ac.dhAgendaCobranca.value, p.nmPessoa");
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

	 /**
     * Método responsável por fazer a contagem dos registros que retornam do hql.
     * @param criteria
     * @return Integer contendo o número de registros retornados.
     */
    public Integer getRowCountByAgendaCobranca(TypedFlatMap criteria) throws Exception{
    	
    	Long idUsuario = criteria.getLong("idUsuario");
    	Long idCliente = criteria.getLong("cobrancaInadimplencia.cliente.idCliente");
    	DateTime dtInicial = criteria.getDateTime("dhAgendaCobrancaInicial");
    	DateTime dtFinal = criteria.getDateTime("dhAgendaCobrancaFinal");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("count(ac.idAgendaCobranca)");
    	
    	mountHqlPaginated(idUsuario, idCliente, dtInicial, dtFinal, sql);

    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true),sql.getCriteria());
        return result.intValue();
    }
    
	/**
	 * @author Mickaël Jalbert
	 * @since 05/12/2006
	 * @param idUsuario
	 * @param idCliente
	 * @param dtInicial
	 * @param dtFinal
	 * @param sql
	 */
	private void mountHqlPaginated(Long idUsuario, Long idCliente, DateTime dtInicial, DateTime dtFinal, SqlTemplate sql) {
		sql.addFrom(AgendaCobranca.class.getName(), "ac JOIN ac.usuario as u " +
				"left outer JOIN ac.contato as c " +
				"JOIN ac.cobrancaInadimplencia as ci " +
				"JOIN ci.cliente as cli " +
				"JOIN cli.pessoa as p " + 
				"left outer JOIN ac.ligacaoCobranca as lc");
    	
    	sql.addCustomCriteria("ac.dhAgendaCobranca.value > ( select max(l.dhLigacaoCobranca.value ) from " + LigacaoCobranca.class.getName() + " l where l.cobrancaInadimplencia = ac.cobrancaInadimplencia)  ");
    	
    	sql.addCriteria("u.idUsuario", "=", idUsuario); 
    	
    	sql.addCriteria("ci.cliente.idCliente", "=", idCliente);
    	
		// Não deve trazer agendas de cobranças que já tenham sido encerradas
    	sql.addCriteria("ci.blCobrancaEncerrada", "<>", Boolean.TRUE);
    		
    	sql.addCriteria("ac.dhAgendaCobranca.value", ">=", dtInicial);
    	sql.addCriteria("ac.dhAgendaCobranca.value", "<=", dtFinal);
	}    
    
    /**
     * CArrega a descrição da agendaCobranca em questão
     * @param idAgendaCobranca
     * @return Map
     */
    public Map findDescricaoAgendaCobranca(Long idAgendaCobranca){
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( ac.dsAgendaCobranca as dsAgendaCobranca ) ")
    				.toString()
    		);
    	
    	sql.addFrom(AgendaCobranca.class.getName(), "ac");
    	
    	if(idAgendaCobranca != null && StringUtils.isNotBlank(idAgendaCobranca.toString())) {
    		sql.addCriteria("ac.idAgendaCobranca", "=", idAgendaCobranca);
    	}
    	
    	return (Map)getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria()).get(0);
    }
    
    /**
     * Busca uma instância de Agenda de Cobrança
     * @param id Identificador da Agenda de Cobrança
     * @return Mapa com os dados necessários para a tela de detalhamento do Manter Agenda Cobrança
     */
    public TypedFlatMap findByIdMap(Long id) {
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("new Map(ul.idUsuario as ligacaoCobranca_usuario_idUsuario," +
                          "        lvf.nrMatricula as ligacaoCobranca_usuario_nrMatricula," +                          
                          "        lvf.nmFuncionario as ligacaoCobranca_usuario_nmUsuario, " +
                          "        cl.idContato as ligacaoCobranca_telefoneContato_contato_idContato, " +
                          "        cl.nmContato as nomeContato, " +
                          "        te.nrTelefone as numeroTelefoneContato, " +
                          "        tc.nrRamal as numeroRamalContato, " +
                          "        lc.idLigacaoCobranca as ligacaoCobranca_idLigacaoCobranca, " +
                          "        lc.dsLigacaoCobranca as ligacaoCobranca_dsLigacaoCobranca, " +
                          "        lc.dhLigacaoCobranca as ligacaoCobranca_dhLigacaoCobranca, " +
                          "        ua.idUsuario as usuario_idUsuario, " +
                          "        avf.nrMatricula as usuario_nrMatricula," +                          
                          "        avf.nmFuncionario as usuario_nmUsuario, " +
                          "        ci.idCobrancaInadimplencia as cobrancaInadimplencia_idCobrancaInadimplencia, " +
                          "        ca.idContato as contato_idContato, " +
                          "        ca.nmContato as contato_nmContato, " +
                          "        ac.idAgendaCobranca as idAgendaCobranca, " +
                          "        ac.dhAgendaCobranca as dhAgendaCobranca, " +
                          "        ac.dsAgendaCobranca as dsAgendaCobranca)");
        
        sql.addFrom(AgendaCobranca.class.getName() + " as ac " +
                    "   inner join ac.cobrancaInadimplencia ci " +
                    "   inner join ac.usuario ua " +
                    "   left outer join ac.contato ca " +
                    "   left outer join ac.ligacaoCobranca lc " +
                    "   left outer join lc.usuario ul " +
                    "   left outer join lc.telefoneContato tc " +
                    "   left outer join tc.contato cl " +
                    "   left outer join tc.telefoneEndereco te " +
                    "   left outer join ul.vfuncionario lvf " +
                    "   left outer join ua.vfuncionario avf");
        
        sql.addCriteria("ac.id","=",(Long)id);
        
        List retorno = this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
        
        TypedFlatMap ac = null;
        List lst = null;
//        ligacaoCobranca_telefoneContato_contato_nmContato
        if( retorno !=  null && !retorno.isEmpty()){
        	lst = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(retorno);
        	
        	if( lst != null && !lst.isEmpty() ){
        		
        		ac = (TypedFlatMap) lst.get(0);
        	
	        	if( !ac.containsKey("ligacaoCobranca.telefoneContato.contato.idContato") ){
	        		ac.remove("ligacaoCobranca.telefoneContato.contato.nmContato");
	        	} else {
	        		if(ac.getString("nomeContato") != null && 
	        		   ac.getString("numeroTelefoneContato") != null && 
	        		   ac.getString("numeroRamal") != null){
	                   ac.put("ligacaoCobranca.telefoneContato.contato.nmContato",ac.getString("nomeContato") + " - " + ac.getString("numeroTelefoneContato") + " - " + ac.getString("numeroRamal"));	                	
	                }else if( ac.getString("nomeContato") != null && 
	        		          ac.getString("numeroTelefoneContato") != null && 
	        		          ac.getString("numeroRamal") == null){
	                	ac.put("ligacaoCobranca.telefoneContato.contato.nmContato",ac.getString("nomeContato") + " - " + ac.getString("numeroTelefoneContato"));	                	
	                }else{
	                	ac.put("ligacaoCobranca.telefoneContato.contato.nmContato","");
	                }
	        	}
	        	
        	}
        }
        
        return ac;
        
    }
    
    /**
     * Monta a query necessária para a pesquisa da listagem e da contagem de itens na listagem
     * @param criteria Critérios de Pesquisa
     * @return SqlTemplate com o sql e parâmetros
     */
    private SqlTemplate montaSqlsListagem(TypedFlatMap criteria){
        
        Long idCobrancaInadimplencia= criteria.getLong("cobrancaInadimplencia.idCobrancaInadimplencia");
        Long idUsuario              = criteria.getLong("usuario.idUsuario");
        Long idContato              = criteria.getLong("contato.idContato");
        DateTime dtInicial          = criteria.getDateTime("dhAgendaCobrancaInicial");
        DateTime dtFinal            = criteria.getDateTime("dhAgendaCobrancaFinal");
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addFrom(AgendaCobranca.class.getName(), " as ac " +
                    "join ac.usuario as u " +
                    "left outer join ac.contato as c " +
                    "join ac.cobrancaInadimplencia as ci " +
                    "join ci.cliente as clici " +
                    "join ci.usuario as uci " +                                        
                    "left outer join ac.ligacaoCobranca as lc");
        
        
        sql.addCriteria("ci.id","=",idCobrancaInadimplencia);
        //Filtro pelo usuario relacionado a agenda cobrança
        sql.addCriteria("u.id","=",idUsuario);
        //Filtro pelo contato relacionado a agenda cobrança
        sql.addCriteria("c.id","=",idContato);
        //Filtro pela data da agenda
        sql.addCriteria("ac.dhAgendaCobranca.value",">=",dtInicial);
        sql.addCriteria("ac.dhAgendaCobranca.value","<=",dtFinal);        
        
        sql.addOrderBy("ac.dhAgendaCobranca.value, " +
                       "c.nmContato, " +
                       "u.nmUsuario");
        
        return sql;
    }
    
    /**
     * Método responsável pela busca das informações a serem mostradas na grid
     * @param criteria Critérios de pesquisa
     * @param findDef Dados de paginação
     * @return ResultSetPage contendo o resultado do hql e dados de paginação
     */    
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
        
        SqlTemplate sql = montaSqlsListagem(criteria);

        sql.addProjection("new Map( ac.idAgendaCobranca as idAgendaCobranca, " +
                "         ac.dhAgendaCobranca as dhAgendaCobranca, " +
                "         c.nmContato as nmContato, " +
                "         u.nmUsuario as nmUsuario ) ");

        return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());   
    }
    
    /**
     * Método responsável por contar quantos itens resultara a query de listagem
     * @param criteria Critérios de pesquisa
     * @return Inteiro que representa o número de registro retornado pela query de listagem
     */ 
    public Integer getRowCount(TypedFlatMap criteria) {
        
        SqlTemplate sql = montaSqlsListagem(criteria);
        
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
        
    }
    
    /**
     * @author José Rodrigo Moraes
     * @since  22/05/2006
     *
     * Busca a Cobrança Inadimplência associada a agenda Cobrança em questão
     * @param idAgendaCobranca Identificador da agenda de cobrança
     * @return Cobranca Inadimplência associada a agenda cobrança
     */
	public CobrancaInadimplencia findCobrancaInadimplenciaByAgendaCobranca(Long idAgendaCobranca) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("ac.cobrancaInadimplencia","cobrancaInadimplencia");
		
		sql.addFrom(AgendaCobranca.class.getName(),"ac");
		sql.addCriteria("ac.id","=",idAgendaCobranca);
		
		List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if( retorno != null && !retorno.isEmpty() ){
			return (CobrancaInadimplencia) retorno.get(0);
		}
		
		return null;
		
	}

}