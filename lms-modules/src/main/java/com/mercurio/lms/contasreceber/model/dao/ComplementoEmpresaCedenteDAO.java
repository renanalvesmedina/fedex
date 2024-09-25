package com.mercurio.lms.contasreceber.model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ComplementoEmpresaCedente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ComplementoEmpresaCedenteDAO extends BaseCrudDao<ComplementoEmpresaCedente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ComplementoEmpresaCedente.class;
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition fdef) {

    	SqlTemplate sql = new SqlTemplate();

        sql.addProjection(" new Map (ced.cdCedente as cdCedente, pes.nmFantasia as nmEmpresa, comp.idComplementoEmpresaCedente as idComplementoEmpresaCedente, ced.dsCedente as dsCedente, " +
        		"pes.nrIdentificacao as nrIdentificacao, pes.tpIdentificacao as tpIdentificacao, pes.nmPessoa as nmPessoa, comp.nrUltimoBoleto as nrUltimoBoleto) ");
        
        sql.addFrom(ComplementoEmpresaCedente.class.getName() + " comp join comp.cedente as ced " +
        		" join ced.agenciaBancaria ag join ag.banco ban join comp.empresa emp join emp.pessoa pes ");
        
		sql.addCriteria("ced.idCedente","=",criteria.getLong("cedente.idCedente"));
		sql.addCriteria("emp.idEmpresa","=",criteria.getLong("empresa.idEmpresa"));
		sql.addOrderBy(" ced.dsCedente, pes.nrIdentificacao ");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
        		sql.getSql(true), 
        		fdef.getCurrentPage(), 
        		fdef.getPageSize(),
        		sql.getCriteria());

		return rsp;
    
   }  
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("cedente", FetchMode.JOIN);
    	lazyFindById.put("empresa", FetchMode.JOIN);
    	lazyFindById.put("empresa.pessoa", FetchMode.JOIN);
    }
    
	 /** Find Metodo que fáz a consulta em banco com Hibernate.
	  * 
	 * Verifica se o intervalo que esta sendo cadastrado não está dentro de outro intervalo já cadastrado para
	 * o mesmo cedente e a mesma empresa.
	 * 
	 * @author Diego Umpierre - LMS
	 * @param criteria
	 * @return true caso exista, false se não existir.
	 */
	public boolean possuiIntervalo(Long idEmpresa, Long idCedente, Long nrIntervaloInicialBoleto, Long nrIntervaloFinalBoleto, Long idComplementoEmpresaCedente ) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(ComplementoEmpresaCedente.class.getName() + " as complemento "+
					" inner join complemento.empresa  as empresa " +
					" inner join complemento.cedente as cedente ");
		
		sql.addCriteria(" empresa.idEmpresa ","=",idEmpresa );
		sql.addCriteria(" cedente.idCedente ","=",idCedente );
	
		
		sql.addCriteria("complemento.idComplementoEmpresaCedente","<>",idComplementoEmpresaCedente);
			
		sql.addCustomCriteria(
				"  (( ? between complemento.nrIntervaloInicialBoleto and complemento.nrIntervaloFinalBoleto) or "+
			    "   ( ? between complemento.nrIntervaloInicialBoleto and complemento.nrIntervaloFinalBoleto)) ");
			sql.addCriteriaValue(nrIntervaloInicialBoleto);
			sql.addCriteriaValue(nrIntervaloFinalBoleto);			
			
		//retorna um Integer 
		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    	 
		if ( 0 == rows.intValue()  ){ return false;}
		else{ return true; }
    }
	
	/**
	 * Retorna o ComplementoEmpresaCedente da empresa e do cedente informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 24/04/2006
	 * 
	 * @param Long idEmpresa
	 * @param Long idCedente
	 * @return ComplementoEmpresaCedente
	 * */
	public ComplementoEmpresaCedente findByEmpresaCedente(Long idEmpresa, Long idCedente){
		SqlTemplate hql = mountHql(idEmpresa, idCedente);
		List lstComplementoEmpresaCedente = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (lstComplementoEmpresaCedente.size() > 0) {
			return (ComplementoEmpresaCedente)lstComplementoEmpresaCedente.get(0);
		} else {
			return null;
		}
	}
    
	private SqlTemplate mountHql(Long idEmpresa, Long idCedente){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("cpc");
		
		hql.addInnerJoin(ComplementoEmpresaCedente.class.getName(), "cpc");
		
		hql.addCriteria("cpc.empresa.id","=", idEmpresa);
		hql.addCriteria("cpc.cedente.id","=", idCedente);
		
		return hql;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Long generateNrUltimoBoleto(Long idComplementoEmpresaCedente) {
    	Long result = null;

    	String updateParametroGeralQuery = " UPDATE COMPLEMENTO_EMPRESA_CEDENTE "
    		+ " SET NR_ULTIMO_BOLETO = (CASE WHEN NR_ULTIMO_BOLETO IS NOT NULL "
    				+ "THEN NR_ULTIMO_BOLETO + 1 "
    				+ "ELSE NR_INTERVALO_INICIAL_BOLETO END) "
    		+ " WHERE ID_COMPLEMENTO_EMPRESA_CEDENTE = ? "
    		+ " RETURNING NR_ULTIMO_BOLETO INTO ? ";
    	Connection connection = null;
		try {
			connection = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection();
			connection.setAutoCommit(true);
			CallableStatement statement = connection.prepareCall("{call " + updateParametroGeralQuery + " }");
			statement.setLong(1, idComplementoEmpresaCedente);
			statement.registerOutParameter(2, Types.NUMERIC);

			statement.executeUpdate();
			result = statement.getLong(2);
		} catch (Exception e) {
			rollbackConnection(connection);
			throw new InfrastructureException(e);
		}finally{
			if (connection != null) {
                closeConnection(connection);
            }
		}
    	
    	return result;
    }
	
	private void closeConnection(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			throw new InfrastructureException(e);	
		}
	}

	private void rollbackConnection(Connection conn){
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw new InfrastructureException(e);
		}
	}
}