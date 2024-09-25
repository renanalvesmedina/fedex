package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.RelacaoCobranca;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RelacaoCobrancaDAO extends BaseCrudDao<RelacaoCobranca, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RelacaoCobranca.class;
    }

    
    
    
    
/** Consulta que retorna as relacoes de cobranca existentes de acordo com os filtros.
 * Usado na grid da primeira aba da tela Relacoes Cobrancas
 * 
 * @author Diego Umpierre - LMS
 * @param idFilial
 * @param relacaoCobranca
 * @param findDef
 * @return
 */
    public ResultSetPage findPaginated(Long idFilial,Long relacaoCobranca ,Long idRedeco,FindDefinition findDef) {
    	/* Chama o Metodo que monta o SqlTemplate */
    	SqlTemplate sql = montaHqlJoins();    	

    	    	
    	sql.addCriteria(" filial.idFilial ","=",idFilial);
    	sql.addCriteria(" relacaoCobranca.nrRelacaoCobrancaFilial ","=",relacaoCobranca);
    	sql.addCriteria(" redeco.idRedeco ","=",idRedeco);
    	
    		
    	/* Monta os Campos da Clausula Select do HQL. */
    	String projection = "new Map(" +
    							" filial.idFilial  											as idFilial,"+
    							" filial.sgFilial  											as sgFilial,"+
    							" pessoa.nmFantasia  										as nmFantasiaFilial,"+
    							" relacaoCobranca.idRelacaoCobranca							as idRelacaoCobranca,"+
    							" relacaoCobranca.nrRelacaoCobrancaFilial 	    			as relacao_cobranca,"+
    							" sum(relacaoCobranca.faturas.qtDocumentos) as quantidade_documentos, "+
    							
    							" (relacaoCobranca.vlFrete + relacaoCobranca.vlJuros "+
    							"  - relacaoCobranca.vlDesconto - relacaoCobranca.vlTarifa) as valorTotalPago, "+
    							
    							" (relacaoCobranca.faturas.moeda.sgMoeda || ' ' || relacaoCobranca.faturas.moeda.dsSimbolo) 					as siglaMoeda, "+
    							" relacaoCobranca.tpSituacaoRelacaoCobranca 	    		as situacao )";	  

    	sql.addOrderBy("filial.sgFilial,pessoa.nmFantasia,relacaoCobranca.nrRelacaoCobrancaFilial");
    	/* Adiciona a projection ao SqlTemplate. */
    	sql.addGroupBy("relacaoCobranca.idRelacaoCobranca,"+
    				   "relacaoCobranca.nrRelacaoCobrancaFilial,"+
    				   "filial.idFilial,"+
    				   "filial.sgFilial,"+
    				   "pessoa.nmFantasia,"+
    				   "relacaoCobranca.tpSituacaoRelacaoCobranca,"+
    				   "relacaoCobranca.vlFrete,"+
    				   "relacaoCobranca.faturas.moeda.sgMoeda,"+
    				   "relacaoCobranca.faturas.moeda.dsSimbolo,"+
    				   "relacaoCobranca.vlDesconto,"+
					   "relacaoCobranca.vlJuros, 	 " +    			
					   "relacaoCobranca.vlTarifa 	 "     			
    				   );
    	
    	sql.addProjection(projection);    	
        	
        return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
    	
	}
    
   
    
    
    
    /**
	 * Metodo que monta os Joins necessarios para os hql.
	 * 
	 * @author Diego Umpierre - LMS
	 * @param  (sem parametros)
	 * @return SqlTemplate
	 */
    private SqlTemplate montaHqlJoins()
    { 
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addInnerJoin(RelacaoCobranca.class.getName(), "relacaoCobranca");
    	sql.addInnerJoin("relacaoCobranca.faturas", "fatura");
    	sql.addInnerJoin("relacaoCobranca.filial", "filial");
    	sql.addInnerJoin("filial.pessoa", "pessoa");
    	sql.addInnerJoin("fatura.moeda", "moeda"); 
    	
    	sql.addLeftOuterJoin("relacaoCobranca.redeco", "redeco");
 
    	return sql;
    }
    
    /**
	 * Metodo que monta os Joins necessarios para os hql.
	 * 
	 * @author Diego Umpierre - LMS
	 * @param  (sem parametros)
	 * @return SqlTemplate
	 */
    private SqlTemplate montaHqlFetchJoins()
    {
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addInnerJoin(RelacaoCobranca.class.getName(), "relacaoCobranca");
    	sql.addInnerJoin("relacaoCobranca.faturas", "fatura");
    	sql.addInnerJoin("fetch relacaoCobranca.filial", "filial");
    	sql.addInnerJoin("fetch filial.pessoa", "pessoa");
    	sql.addInnerJoin("fatura.moeda", "moeda");
    	
    	sql.addLeftOuterJoin("relacaoCobranca.redeco", "redeco");
 
    	return sql;
    }
    /**
     * Busca o numero de linhas para a grid de acordo com os filtros passados.
     * Usado na grid da primeira aba da tela Relacoes Cobrancas
     * 
     * @author Diego Umpierre - LMS
     * @param idFilial
     * @param relacaoCobranca
     * @return
     */
	public Integer getRowCount(Long idFilial,Long relacaoCobranca,Long idRedeco) {
		SqlTemplate sql = new SqlTemplate();    	

    	sql.addProjection("count(*)");
    	sql.addInnerJoin(RelacaoCobranca.class.getName(), "relacaoCobranca");
    	sql.addLeftOuterJoin("relacaoCobranca.redeco", "redeco");

    	sql.addCriteria(" relacaoCobranca.filial.id ","=",idFilial);
    	sql.addCriteria(" relacaoCobranca.nrRelacaoCobrancaFilial ","=",relacaoCobranca);
    	sql.addCriteria(" redeco.idRedeco ","=",idRedeco);

    	Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
    	return result.intValue();
	}

    /** Find Metodo que fáz a consulta em banco com Hibernate. Utiliza apenas o id passado.
     * 
     * 
	 * @author Diego Umpierre - LMS
	 * @param criteria
	 * @return ResultSetPage com o resultado da busca.
	 */
	public Map findMapById(Long id) { 
		SqlTemplate sql =  montaHqlJoins();
    	
		//montando os joins
    	sql.addLeftOuterJoin("redeco.filial", "redecoFilial");
    	sql.addLeftOuterJoin("redecoFilial.pessoa", "redecoFilialPessoa");
    	
    	
    	
    	sql.addCriteria(" relacaoCobranca.idRelacaoCobranca ","=",id);
		
     	/* Monta os Campos da Clausula Select do HQL. */
    	String projection = "new Map(" +
    							" relacaoCobranca.idRelacaoCobranca							as idRelacaoCobranca,"+						
    							" filial.idFilial  											as idFilial,"+
    							" pessoa.nmFantasia  										as nmFantasiaFilial,"+
    							" filial.sgFilial  											as sgFilial,"+
    							" relacaoCobranca.nrRelacaoCobrancaFilial 	    			as nrRelacaoCobrancaFilial,"+
    							" relacaoCobranca.dtLiquidacao 	    						as dtLiquidacao,"+
    							" relacaoCobranca.tpSituacaoRelacaoCobranca 	    		as tpSituacaoRelacaoCobranca,"+
    							" redeco.nmResponsavelCobranca 	    						as nmResponsavelCobranca,"+
    							" sum (fatura.qtDocumentos)	    							as quantidade_documentos,"+
    							" sum (fatura.vlTotal)	    								as valorTotalDevido,"+
    							" (moeda.sgMoeda || ' ' || moeda.dsSimbolo) 				as siglaMoeda, "+    							
    							
    							" (relacaoCobranca.vlFrete + relacaoCobranca.vlJuros "+
    							"  - relacaoCobranca.vlDesconto - relacaoCobranca.vlTarifa) as valorTotalPago, "+
    							
    							" relacaoCobranca.vlJuros 	    							as vlJuros,"+
    							" relacaoCobranca.vlDesconto 	    						as vlDesconto,"+
    							" relacaoCobranca.vlTarifa 	    							as vlTarifa,"+
    							" relacaoCobranca.dsOrigem 	    							as dsOrigem,"+
    							" redecoFilial.sgFilial 	    							as filialRedeco,"+
    							" redecoFilial.idFilial 	    							as idFilialRedeco,"+
    							" redeco.nrRedeco 	    									as nrRedeco, "+
    							" redecoFilialPessoa.nmFantasia								as redecoNmFantasia, "+
    							" redeco.idRedeco 	    									as idRedeco "+
    							")";	 

		/* Adiciona a projection ao SqlTemplate. */
		sql.addProjection(projection);    	
		
		
		sql.addGroupBy("relacaoCobranca.idRelacaoCobranca, " +			
						"filial.idFilial, " +  					
						"pessoa.nmFantasia,  " + 					
						"filial.sgFilial,  " + 					
						"relacaoCobranca.nrRelacaoCobrancaFilial, " + 	    	
						"relacaoCobranca.dtLiquidacao, 	 " +    			
						"relacaoCobranca.tpSituacaoRelacaoCobranca, 	 " +    	
						"redeco.nmResponsavelCobranca, " + 	    			
						"relacaoCobranca.vlFrete, " +
						"relacaoCobranca.vlDesconto, " +
						"relacaoCobranca.vlJuros, 	 " +    			
						"relacaoCobranca.vlDesconto,  " +	    			
						"relacaoCobranca.vlTarifa, 	 " +    			
						"relacaoCobranca.dsOrigem,  " +	    			
						"redecoFilial.sgFilial, " +		
						"moeda.sgMoeda, " +	
						"moeda.dsSimbolo, " +	
						"redeco.idRedeco ,"+
						"redecoFilial.idFilial,"+
						"redecoFilialPessoa.nmFantasia,"+						
						"redeco.nrRedeco" 	
				   );
		
		
		

		ArrayList resultado = (ArrayList)getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if(resultado != null && !resultado.isEmpty()){

			return (Map) resultado.get(0);
		}
		
		return null;
	}
	
	
	
	
	
	  /** Find Metodo que faz a consulta em banco com Hibernate. 
	 * Usado na grid da terceira aba da tela Relacoes Cobrancas
     * 
	 * @author Diego Umpierre - LMS
	 * @param criteria
	 * @return ResultSetPage com o resultado da busca.
	 * 
	 */
	public ResultSetPage findPaginatedMapGridById(Long idRelacaoCobranca, FindDefinition findDef) {
    	SqlTemplate sql = montaHqlMapJoins();
    	
    	//montando a clausula where
    	sql.addCriteria(" relacaoCobranca.idRelacaoCobranca ","=",idRelacaoCobranca);
		
     	/* Monta os Campos da Clausula Select do HQL. */
    	String projection = "new Map(" +
    							" doctoServico.tpDocumentoServico							as tpDocumentoServico,"+
    							" doctoServico.idDoctoServico								as idDoctoServico,"+
    							" relacaoCobranca.idRelacaoCobranca							as idRelacaoCobranca,"+
    							" devDocServFatFilial.sgFilial  							as sgFilial,"+
    							" devDocServFatFilialPessoa.nmFantasia  					as nmFantasiaFilial,"+
    							" relacaoCobranca.nrRelacaoCobrancaFilial 	    			as nrRelacaoCobrancaFilial,"+
    							" doctoServico.nrDoctoServico 	    						as nrDoctoServico,"+
    							" pessoa.nrIdentificacao 	    							as nrIdentificacao,"+
    							" pessoa.nmPessoa 	    									as nmPessoa,"+
    							" pessoa.tpIdentificacao 	    							as tpIdentificacao,"+
    							" devedorDocServFat.vlDevido 	    						as vlDevido,"+
    							" devDocServFatDesconto.vlDesconto 	    					as vlDesconto, " +
    							" filialOrigem.sgFilial                                     as sgFilialOrigem, "+
    							
    							" relacaoCobranca.vlJuros 	    							as vlJuros,"+
    							" relacaoCobranca.vlTarifa 	    							as vlTarifa,"+    							
    							
    							
    							" motivoDesconto.dsMotivoDesconto 	    					as dsMotivoDesconto"+
    							")";	 

		/* Adiciona a projection ao SqlTemplate. */
		sql.addProjection(projection);    	

		
		
		sql.addOrderBy("doctoServico.tpDocumentoServico, " +		
					   "filialOrigem.sgFilial, " + 		
					   "doctoServico.nrDoctoServico");
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}
	
    
	/**Conta as linhas da grid.
	 * Usado na grid da terceira aba da tela Relacoes Cobrancas 
	 * 
	 * @param id
	 * @return
	 */
	public Integer getRowCountMapGrid(Long idRelacaoCobranca) {
    	/* Chama o Metodo que monta o SqlTemplate */
    	SqlTemplate sql = montaHqlMapJoins();    	

//    	montando a clausula where
    	sql.addCriteria(" relacaoCobranca.idRelacaoCobranca ","=",idRelacaoCobranca);
  
		Integer i = getRowCountForQuery(sql.getSql(),sql.getCriteria());
		return i;	
	}
	
	
    /**
	 * Metodo que monta os Joins necessarios para os hql.
	 * Usado na grid da terceira aba da tela Relacoes Cobrancas
	 * 
	 * @author Diego Umpierre - LMS
	 * @param  (sem parametros)
	 * @return SqlTemplate
	 */
    private SqlTemplate montaHqlMapJoins(){
    	SqlTemplate sql = new SqlTemplate();
    	
		//montando os joins
    	sql.addInnerJoin(RelacaoCobranca.class.getName(), 			"relacaoCobranca");
    	sql.addInnerJoin("relacaoCobranca.faturas",		 			"fatura");
    	sql.addInnerJoin("fatura.itemFaturas", 						"itemFatura");
    	sql.addInnerJoin("itemFatura.devedorDocServFat", 			"devedorDocServFat");
    	sql.addInnerJoin("devedorDocServFat.doctoServico", 			"doctoServico");
    	sql.addInnerJoin("doctoServico.filialByIdFilialOrigem",     "filialOrigem");
    	sql.addInnerJoin("devedorDocServFat.cliente", 				"cliente");
    	sql.addInnerJoin("cliente.pessoa", 							"pessoa");
    	sql.addInnerJoin("devedorDocServFat.filial", 				"devDocServFatFilial");
    	sql.addLeftOuterJoin("devedorDocServFat.descontos", 		"devDocServFatDesconto");
    	sql.addLeftOuterJoin("devDocServFatDesconto.motivoDesconto","motivoDesconto");    	

    	//para pegar o nome fantasia
    	sql.addInnerJoin("devDocServFatFilial.pessoa", 				"devDocServFatFilialPessoa");

    	return sql;
    }
    
    
    
    /** Find Metodo que faz a consulta em banco com Hibernate. Utiliza apenas o id passado.
     * Usado na terceira aba da tela Relacoes Cobrancas
     * 
	 * @author Diego Umpierre - LMS
	 * @param criteria
	 * @return ResultSetPage com o resultado da busca.
	 * 
	 * 
	 */
	public Map findMapGridById(Long id) {
    	SqlTemplate sql = montaHqlMapJoins();
    	
    	    	
    	//montando a clausula where
    	sql.addCriteria("  doctoServico.idDoctoServico ","=",id);
		
     	/* Monta os Campos da Clausula Select do HQL. */
    	String projection = "new Map(" +
    							" doctoServico.tpDocumentoServico							as tpDocumentoServico,"+
    							" doctoServico.idDoctoServico								as idDoctoServico,"+
    							" relacaoCobranca.idRelacaoCobranca							as idRelacaoCobranca,"+
//    							" devDocServFatFilial.sgFilial  							as sgFilial,"+
    							" devDocServFatFilialPessoa.nmFantasia  					as nmFantasiaFilial,"+
    							" relacaoCobranca.nrRelacaoCobrancaFilial 	    			as nrRelacaoCobrancaFilial,"+
    							" doctoServico.nrDoctoServico 	    						as nrDoctoServico,"+
    							" pessoa.nrIdentificacao 	    							as nrIdentificacao,"+
    							" pessoa.nmPessoa 	    									as nmPessoa,"+
    							" pessoa.tpIdentificacao 	    							as tpIdentificacao,"+    							
    							" devedorDocServFat.vlDevido 	    						as vlDevido,"+
    							" devDocServFatDesconto.vlDesconto 	    					as vlDesconto,"+
    							" motivoDesconto.dsMotivoDesconto 	    					as dsMotivoDesconto, "+
    							" filialOrigem.sgFilial                                     as sgFilial "+
    							")";	 

		/* Adiciona a projection ao SqlTemplate. */
		sql.addProjection(projection);    	

		sql.addOrderBy("doctoServico.tpDocumentoServico, " +		
					   "devDocServFatFilial.sgFilial, " + 		
					   "doctoServico.nrDoctoServico," +
					   "pessoa.nrIdentificacao," + 	    		
					   "pessoa.nmPessoa");
		
	
		ArrayList resultado = (ArrayList)getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if(resultado != null && !resultado.isEmpty()){

			return (Map) resultado.get(0);
		}
		
		return null;
		
	}
	
	/**
	 * Retorna, paginado, todas a relações de cobrança do redeco informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 05/07/2006
	 * 
	 * @param Long idRedeco
	 * @param FindDefinition findDef
	 * 
	 * @return ResultsetPage
	 */
	public List findByIdRedeco(Long idRedeco){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("rel");
		
		hql.addInnerJoin(RelacaoCobranca.class.getName(), "rel");
		
		hql.addCriteria("rel.redeco.id", "=", idRedeco);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	/**
	 * Metodo responsável por buscar RelacaoCobranca de acordo com o nrRelacaoCobrancaFilial
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 27/07/2006
	 * 
	 * @param Long nrRelacaoCobranca
	 * 
	 * @return List
	 */
	public List findRelacaoCobrancaByNrRelacaoCobranca(Long nrRelacaoCobranca, String sgFilial){
		
		SqlTemplate hql = montaHqlFetchJoins();    
		
		hql.addProjection("distinct(relacaoCobranca)");
		hql.addCriteria("relacaoCobranca.nrRelacaoCobrancaFilial", "=", nrRelacaoCobranca);
		hql.addCriteria("filial.sgFilial", "=", sgFilial);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
	}

	/**
	 * FindPaginated em SQL desenvolvido para melhorar o desempenho
	 * 
	 *
	 * @author José Rodrigo Moraes
	 * @since 26/01/2007
	 *
	 * @param idFilial
	 * @param nrRelacaoCobranca
	 * @param idRedeco
	 * @param fd FindDefinition
	 */
	public ResultSetPage findPaginatedBySql(Long idFilial, Long nrRelacaoCobranca, Long idRedeco, FindDefinition fd) {
		
		SqlTemplate sql = new SqlTemplate();
		
		montaSqlJoins(sql,idFilial, nrRelacaoCobranca, idRedeco);
    	
    	sql.addProjection("F.ID_FILIAL AS ID_FILIAL, " +
    			          "F.SG_FILIAL AS SG_FILIAL, " +
    			          "PF.NM_FANTASIA AS NM_FANTASIA_FILIAL, " +
    			          "RC.ID_RELACAO_COBRANCA AS ID_RELACAO_COBRANCA, " +
    			          "RC.NR_RELACAO_COBRANCA_FILIAL AS RELACAO_COBRANCA, " +
    			          "(SELECT SUM(FAT.QT_DOCUMENTOS) FROM FATURA FAT WHERE FAT.ID_RELACAO_COBRANCA = RC.ID_RELACAO_COBRANCA) AS QUANTIDADE_DOCUMENTOS, " +
    			          "(RC.VL_FRETE + RC.VL_JUROS - RC.VL_DESCONTO - RC.VL_TARIFA) AS VALOR_TOTAL_PAGO, " +    			              			          
    			          "(SELECT MIN(M.SG_MOEDA || ' ' || M.DS_SIMBOLO) " +
    			          "		FROM MOEDA M " +
    			          "			INNER JOIN FATURA FAT ON FAT.ID_MOEDA = M.ID_MOEDA" +
    			          " WHERE fat.id_relacao_cobranca = rc.id_relacao_cobranca) AS SIGLA_MOEDA, " +
    			          PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I","SITUACAO") + ", " +
    			          "RC.TP_SITUACAO_RELACAO_COBRANCA as TP_SITUACAO_RELACAO_COBRANCA");

    	sql.addOrderBy("F.SG_FILIAL,PF.NM_FANTASIA,RC.NR_RELACAO_COBRANCA_FILIAL");
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery(){
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_FILIAL", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
				sqlQuery.addScalar("NM_FANTASIA_FILIAL",Hibernate.STRING);
				sqlQuery.addScalar("ID_RELACAO_COBRANCA",Hibernate.LONG);
				sqlQuery.addScalar("RELACAO_COBRANCA",Hibernate.LONG);
				sqlQuery.addScalar("QUANTIDADE_DOCUMENTOS",Hibernate.LONG);
				sqlQuery.addScalar("VALOR_TOTAL_PAGO",Hibernate.BIG_DECIMAL);				
				sqlQuery.addScalar("SIGLA_MOEDA",Hibernate.STRING);
				sqlQuery.addScalar("SITUACAO",Hibernate.STRING);
				sqlQuery.addScalar("TP_SITUACAO_RELACAO_COBRANCA",Hibernate.STRING);
			}
		};
    		
        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(true), 
        		                                             fd.getCurrentPage(), 
        		                                             fd.getPageSize(),
        		                                             sql.getCriteria(),
        		                                             csq);		
		
	}

	/**
	 * getRowCount em SQL desenvolvido para melhorar o desempenho da pesquisa
	 * 
	 *
	 * @author José Rodrigo Moraes
	 * @since 26/01/2007
	 *
	 * @param idFilial
	 * @param nrRelacaoCobranca
	 * @param idRedeco
	 * @param fd FindDefinition
	 */
	public Integer getRowCountBySql(Long idFilial, Long nrRelacaoCobranca, Long idRedeco) {
		
		SqlTemplate sql = new SqlTemplate();
		
		montaSqlJoins(sql,idFilial, nrRelacaoCobranca, idRedeco);
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.getSql(false),sql.getCriteria());
		
	}
	
	/**
	 * Monta os joins necessários para a pesquisa de Relações de cobrança via SQL
	 * 
	 *
	 * @author José Rodrigo Moraes
	 * @since 26/01/2007
	 * 
	 * @param idFilial
	 * @param nrRelacaoCobranca
	 * @param idRedeco
	 * @return
	 */
    private SqlTemplate montaSqlJoins(SqlTemplate sql ,Long idFilial, Long nrRelacaoCobranca, Long idRedeco){
    	
    	sql.addFrom("RELACAO_COBRANCA RC" +
    			    " INNER JOIN FILIAL F ON RC.ID_FILIAL = F.ID_FILIAL " +
    			    " INNER JOIN PESSOA PF ON F.ID_FILIAL = PF.ID_PESSOA " +
    			    " LEFT OUTER JOIN REDECO R ON R.ID_REDECO = RC.ID_REDECO," +
    			    "VALOR_DOMINIO VD" +
    			    " INNER JOIN DOMINIO D ON VD.ID_DOMINIO = D.ID_DOMINIO ");
    	
    	sql.addCriteria("F.ID_FILIAL","=",idFilial);
    	sql.addCriteria("RC.NR_RELACAO_COBRANCA_FILIAL","=",nrRelacaoCobranca);
    	sql.addCriteria("R.ID_REDECO","=",idRedeco);
    	sql.addJoin("lower(D.NM_DOMINIO)","lower('DM_STATUS_RELACAO_COBRANCA')");
    	sql.addJoin("lower(VD.VL_VALOR_DOMINIO)","lower(RC.TP_SITUACAO_RELACAO_COBRANCA)");
    	
    	return sql;
    }
    
    /**
     * Metodo responsável por buscar RelacaoCobranca de acordo com o nrRelacaoCobrancaFilial e o idFilial
     *
     * @author Hector Julian Esnaola Junior
     * @since 31/05/2007
     *
     * @param nrRelacaoCobranca
     * @param sgFilial
     * @return
     *
     */
	public RelacaoCobranca findRelacaoCobrancaByNrRelacaoCobrancaFilialAndIdFilial(Long nrRelacaoCobranca, Long idFilial){
		
		SqlTemplate hql = new SqlTemplate();    
		
		hql.addInnerJoin(getPersistentClass().getName() + " relacaoCobranca ");
		
		hql.addProjection(" relacaoCobranca ");
		
		hql.addCriteria("relacaoCobranca.nrRelacaoCobrancaFilial", "=", nrRelacaoCobranca);
		hql.addCriteria("relacaoCobranca.filial.id", "=", idFilial);
		
		return (RelacaoCobranca) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		
	}
	
}