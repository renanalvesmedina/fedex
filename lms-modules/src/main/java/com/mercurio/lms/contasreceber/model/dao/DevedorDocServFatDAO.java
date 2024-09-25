package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DevedorDocServFatDAO extends BaseCrudDao<DevedorDocServFat, Long> {
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DevedorDocServFat.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("doctoServico", FetchMode.JOIN);
    	lazyFindById.put("doctoServico.moeda", FetchMode.JOIN);
    	lazyFindById.put("doctoServico.filialByIdFilialOrigem", FetchMode.JOIN);
    	lazyFindById.put("doctoServico.filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
    	lazyFindById.put("doctoServico.servico", FetchMode.JOIN);
    	lazyFindById.put("doctoServico.divisaoCliente", FetchMode.JOIN);
    	lazyFindById.put("descontos", FetchMode.JOIN);
    	lazyFindById.put("cliente", FetchMode.JOIN);
    	lazyFindById.put("cliente.cedente", FetchMode.JOIN);
    	lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
    	lazyFindById.put("cliente.divisaoCliente", FetchMode.JOIN);
    	lazyFindById.put("cliente.filialByIdFilialCobranca", FetchMode.JOIN);
    	lazyFindById.put("filial", FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa", FetchMode.JOIN);
    	lazyFindById.put("filial.cedenteByIdCedenteBloqueto", FetchMode.JOIN);
    	lazyFindById.put("divisaoCliente", FetchMode.JOIN);
    	
    	super.initFindByIdLazyProperties(lazyFindById);
    }
    
    public void clear() {
		getAdsmHibernateTemplate().clear();
    }
    
    
    /**
     * Retorna o devedorDocServFat com o doctoServicoFin 'fetchado' do
     * idDevedorDocServFat informado 
     * 
     * @author Mickaël Jalbert
     * @since 02/01/2007
     * 
     * @param Long idDevedorDocServFat
     * 
     * @return DevedorDocServFat
     */
	public DevedorDocServFat findByIdWithDocumentoFin(Long idDevedorDocServFat) {		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("dev");
		
		sql.addInnerJoin(DevedorDocServFat.class.getName(), "dev");
		sql.addInnerJoin("fetch dev.cliente","cli");
		sql.addInnerJoin("fetch cli.pessoa","clipes");
		sql.addLeftOuterJoin("fetch dev.descontos","des");
		
		sql.addInnerJoin("fetch dev.doctoServicoFin","doc");
		sql.addInnerJoin("fetch doc.filialByIdFilialOrigem","fil");
		sql.addInnerJoin("fetch fil.pessoa","pes");
		sql.addInnerJoin("fetch doc.moeda","moe");
		sql.addLeftOuterJoin("fetch doc.servico","ser");

		sql.addInnerJoin("fetch dev.doctoServico","doc2");
		sql.addInnerJoin("fetch doc2.filialByIdFilialOrigem","fil2");
		sql.addInnerJoin("fetch fil2.pessoa","pes");
		sql.addInnerJoin("fetch doc2.moeda","moe2");
		sql.addLeftOuterJoin("fetch doc2.servico","ser2");
		
		sql.addCriteria("dev.id","=",idDevedorDocServFat);
		
		List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if( retorno != null && !retorno.isEmpty() ){
			return (DevedorDocServFat) retorno.get(0);
		} else {
			return null;
		}
	}
    
    /**
     * Retorna o devedorDocServFat com o desconto e o doctoServico 'fetchado' do
     * idDevedorDocServFat informado 
     * 
     * @author Mickaël Jalbert
     * @since 10/07/2006
     * 
     * @param Long idDevedorDocServFat
     * 
     * @return DevedorDocServFat
     */
	public DevedorDocServFat findByIdWithDocumentoAndDesconto(Long idDevedorDocServFat) {		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("dev");
		
		sql.addInnerJoin(DevedorDocServFat.class.getName(), "dev");
		sql.addInnerJoin("fetch dev.doctoServico","doc");
		sql.addInnerJoin("fetch doc.filialByIdFilialOrigem","fil");
		sql.addInnerJoin("fetch fil.pessoa","pes");
		sql.addInnerJoin("fetch doc.moeda","moe");
		sql.addLeftOuterJoin("fetch dev.descontos","des");
		sql.addLeftOuterJoin("fetch doc.servico","ser");
		
		sql.addCriteria("dev.id","=",idDevedorDocServFat);
		
		List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if( retorno != null && !retorno.isEmpty() ){
			return (DevedorDocServFat) retorno.get(0);
		} else {
			return null;
		}
	}
	
    /**
     * Retorna o devedorDocServFat com o doctoServico 'fetchado' do
     * idDevedorDocServFat informado 
     * 
     * @author Mickaël Jalbert
     * @since 04/12/2006
     * 
     * @param Long idDevedorDocServFat
     * 
     * @return DevedorDocServFat
     */
	public DevedorDocServFat findByIdWithDocumento(Long idDevedorDocServFat) {		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("dev");
		
		sql.addInnerJoin(DevedorDocServFat.class.getName(), "dev");
		sql.addInnerJoin("fetch dev.doctoServico","doc");
		sql.addInnerJoin("fetch dev.cliente","cli");
		sql.addInnerJoin("fetch dev.filial","devfil");
		sql.addInnerJoin("fetch devfil.pessoa","devpes");
		sql.addInnerJoin("fetch cli.pessoa","pes");
		sql.addInnerJoin("fetch doc.filialByIdFilialOrigem","fil");
		sql.addInnerJoin("fetch fil.pessoa","pes");
		sql.addInnerJoin("fetch doc.moeda","moe");
		sql.addLeftOuterJoin("fetch doc.servico","ser");
		
		sql.addCriteria("dev.id","=",idDevedorDocServFat);
		
		List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if( retorno != null && !retorno.isEmpty() ){
			return (DevedorDocServFat) retorno.get(0);
		} else {
			return null;
		}
	}	
    
	/**
	 * Retorna a lista de ids de devedor por fatura informada.
	 *
	 * @author Mickaël Jalbert
	 * 11/05/2006
	 *
	 * @param Long idFatura
	 * @return List
	 */
    public List findIdsByFatura(Long idFatura) {
        SqlTemplate hql = mountHqlFindByFatura(idFatura);
        
        hql.addProjection("dev.id");
        
        return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }      
    
	/**
	 * Retorna a lista de devedor por fatura informada.
	 *
	 * @author Mickaël Jalbert
	 * 15/03/2006
	 *
	 * @param Long idFatura
	 * @return List
	 */
    public List findByFatura(Long idFatura) {
        SqlTemplate hql = mountHqlFindByFatura(idFatura);
        
        hql.addProjection("dev");
        
        return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    } 
    
    private SqlTemplate mountHqlFindByFatura(Long idFatura){
        SqlTemplate hql = new SqlTemplate();
        
        hql.addInnerJoin(DevedorDocServFat.class.getName(), "dev");
        hql.addInnerJoin("dev.itemFaturas", "item");
        hql.addInnerJoin("item.fatura", "fat");
        hql.addLeftOuterJoin("dev.descontos", "des");
        hql.addCriteria("fat.id", "=", idFatura); 
        
        return hql;
    }

	public List findIdsByIdDoctoServico(Long idDoctoServico)
	{
		String sql = "select pojo.idDevedorDocServFat " +
		"from "+ DevedorDocServFat.class.getName() + " as pojo " +
		"join pojo.doctoServico as ds " +
		"where ds.idDoctoServico = :idDoctoServico ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idDoctoServico", idDoctoServico);
	}

	public boolean findCtrcFaturado(Long idDoctoServico) {
		ProjectionList pl = Projections.projectionList().
			add(Projections.count("ddsf.idDevedorDocServFat"));

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ddsf")
			.setProjection(pl)
			.add(Restrictions.in("ddsf.tpSituacaoCobranca", new Object[]{"P", "C"}))
			.add(Restrictions.eq("ddsf.doctoServico.id", idDoctoServico));
		return (getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc).intValue() > 0);
	}
    
    /**
     * Monta a query de pesquisa dos Conhecimentos (CTRC e NFT)
     * @param tfm Critérios de Pesquisa
     * @return String da query
     */
    private SqlTemplate montaQueryConhecimento(TypedFlatMap tfm, boolean isRowCount) {
        
        SqlTemplate sql = new SqlTemplate();        
        
        if( !isRowCount ){
        	
        	sql.addProjection("new Map(ddsf.id as idDevedorDocServFat, " +
        			          "        cp.nrIdentificacao as nrIdentificacao, " +
        			          "        cp.tpIdentificacao as tpIdentificacao, " +
        			          "        cp.nmPessoa as nmPessoa, " +
        			          "		   d.dsDivisaoCliente as dsDivisaoCliente, " +
        			          "        m.idMoeda as idMoeda, " +        			          
        			          "        m.sgMoeda || ' ' || m.dsSimbolo as moeda, " +
        			          "        ddsf.vlDevido as valor," +
        			          "        ddsf.tpSituacaoCobranca as tpSituacaoCobranca," +
        			          "        con.tpSituacaoConhecimento as tpSituacaoConhecimento)");
            
        }
        
        sql.addFrom(Conhecimento.class.getName() + " as con " +
        		    "	inner join con.devedorDocServFats as ddsf " +
        		    "   inner join ddsf.cliente as c " +
        		    "   left  join ddsf.divisaoCliente as d " +
        		    "   inner join c.pessoa as cp " +
        		    "   inner join con.moeda as m " +
        		    "   inner join con.filialOrigem as f");

        sql.addCriteria("con.id","=",tfm.getLong("devedorDocServFat.doctoServico.idDoctoServico"));
        sql.addCriteria("con.tpDocumentoServico","=",tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico"));
        sql.addCriteria("f.id","=",tfm.getLong("devedorDocServFat.filialByIdFilialOrigem.idFilial"));
        
        sql.addOrderBy("cp.nrIdentificacao");
        sql.addOrderBy("cp.nmPessoa");
        sql.addOrderBy("m.dsSimbolo");
        sql.addOrderBy("ddsf.vlDevido");
        
        return sql;
    }
    
    /**
     * Monta a query de pesquisa Ctos Internacionais
     * @param tfm Critérios de Pesquisa
     * @return String da query
     */
    private SqlTemplate montaQueryCtoInternacional(TypedFlatMap tfm, boolean isRowCount) {
        
        SqlTemplate sql = new SqlTemplate();
        
        if( !isRowCount ){
        	
        	sql.addProjection("new Map(ddsf.id as idDevedorDocServFat, " +
			          "                cp.nrIdentificacao as nrIdentificacao, " +
			          "        		   cp.tpIdentificacao as tpIdentificacao, " +
			          "        		   cp.nmPessoa as nmPessoa, " +
			          "                m.idMoeda as idMoeda, " +	
			          "		  		   d.dsDivisaoCliente as dsDivisaoCliente, " +
			          "        		   m.sgMoeda || ' ' || m.dsSimbolo as moeda, " +
			          "        		   ddsf.vlDevido as valor," +
			          "        		   ddsf.tpSituacaoCobranca as tpSituacaoCobranca," +
			          "        		   cto.tpSituacaoCrt as tpSituacaoCrt," +
			          "                cto.id as idCtoInternacional)");
        }
        
		sql.addFrom(CtoInternacional.class.getName() + " as cto " +
    		    	"	inner join cto.devedorDocServFats as ddsf " +
	    		    "   inner join ddsf.cliente as c " +
	    		    "	left  join ddsf.divisaoCliente as d " +	
	    		    "   inner join c.pessoa as cp " +
	    		    "   inner join cto.moeda as m " +
	    		    "   inner join cto.filial as f");	
        
        sql.addCriteria("cto.id","=",tfm.getLong("devedorDocServFat.doctoServico.idDoctoServico"));
        sql.addCriteria("cto.tpDocumentoServico","=",tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico"));
        sql.addCriteria("f.id","=",tfm.getLong("devedorDocServFat.filialByIdFilialOrigem.idFilial"));
        
        sql.addOrderBy("cp.nrIdentificacao");
        sql.addOrderBy("cp.nmPessoa");
        sql.addOrderBy("m.dsSimbolo");
        sql.addOrderBy("ddsf.vlDevido");
        
        return sql;
        
    }
    
    /**
     * Monta a query de pesquisa das Notas Fiscais
     * @param tfm Critérios de Pesquisa
     * @return String da query
     */
    private SqlTemplate montaQueryNotasFiscais(TypedFlatMap tfm, boolean isRowCount) {
        
        SqlTemplate sql = new SqlTemplate();
        
        if( !isRowCount ){
        	
        	sql.addProjection("new Map(ddsf.id as idDevedorDocServFat, " +
			          "                cp.nrIdentificacao as nrIdentificacao, " +
			          "                cp.tpIdentificacao as tpIdentificacao, " +
			          "                cp.nmPessoa as nmPessoa, " +
			          "                m.idMoeda as idMoeda, " +
			          "		  		   d.dsDivisaoCliente as dsDivisaoCliente, " +
			          "                m.sgMoeda || ' ' || m.dsSimbolo as moeda, " +
			          "                ddsf.vlDevido as valor," +
			          "                ddsf.tpSituacaoCobranca as tpSituacaoCobranca," +
			          "                nfs.tpSituacaoNf as tpSituacaoNf)");
        }
        
        sql.addFrom(NotaFiscalServico.class.getName() + " as nfs " +
		    	"	inner join nfs.devedorDocServFats as ddsf " +
    		    "   inner join ddsf.cliente as c " +
    		    "	left  join ddsf.divisaoCliente as d " +
    		    "   inner join c.pessoa as cp " +
    		    "   inner join nfs.moeda as m " +
    		    "   inner join nfs.filial as f");
        
        sql.addCustomCriteria("ddsf.tpSituacaoCobranca <> 'L'");
        sql.addCriteria("nfs.id","=",tfm.getLong("devedorDocServFat.doctoServico.idDoctoServico"));
        sql.addCriteria("nfs.tpDocumentoServico","=",tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico"));
        sql.addCriteria("f.id","=",tfm.getLong("devedorDocServFat.filialByIdFilialOrigem.idFilial"));
        
        sql.addOrderBy("cp.nrIdentificacao");
        sql.addOrderBy("cp.nmPessoa");
        sql.addOrderBy("m.dsSimbolo");
        sql.addOrderBy("ddsf.vlDevido");
        
        return sql;
        
    }
    
    /**
     * Retorna uma lista de array da nota fiscal de conhecimento informado filtrado pelo cliente
     * 
     * @author Mickaël Jalbert
     * @since 15/05/2006
     * 
     * @param Integer nrNotaFiscal
     * @param Long idCliente
     * 
     * @return List
     * */
    public List findByNotaFiscalConhecimento(Long nrNotaFiscal, Long idCliente, YearMonthDay dataEmissao, Long idFilial){
    	SqlTemplate sql = montaQueryNotaFiscalConhecimento(nrNotaFiscal, idCliente, dataEmissao, idFilial);

		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("tpModal", Hibernate.STRING);
				sqlQuery.addScalar("tpAbrangencia", Hibernate.STRING);
				sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("nrIdentificacao", Hibernate.STRING);		
				sqlQuery.addScalar("tpSituacaoCobranca", Hibernate.STRING);
				sqlQuery.addScalar("idFilial", Hibernate.LONG);
				sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("idDevedorDocServFat", Hibernate.LONG);
				sqlQuery.addScalar("tpSituacaoConhecimento", Hibernate.STRING);
				sqlQuery.addScalar("sgFilial", Hibernate.STRING);
				sqlQuery.addScalar("tpConhecimento", Hibernate.STRING);
			}
		};
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), Integer.valueOf(1), Integer.valueOf(1000), sql.getCriteria(), configSql);
		
		return rsp.getList();
    }
    
    /**
     * Monta a query de pesquisa da nota fiscal de conhecimento
     * 
     * @param Integer nrNotaFiscal
     * @param Long idCliente
     * 
     * @return SqlTemplate
     */
    private SqlTemplate montaQueryNotaFiscalConhecimento(Long nrNotaFiscal, Long idCliente, YearMonthDay dataEmissao, Long idFilial) {
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("S.TP_MODAL", 				"tpModal");
        sql.addProjection("S.TP_ABRANGENCIA", 			"tpAbrangencia");
        sql.addProjection("DS.ID_DOCTO_SERVICO", 		"idDoctoServico");
        sql.addProjection("P.NR_IDENTIFICACAO", 		"nrIdentificacao");
        sql.addProjection("DEV.TP_SITUACAO_COBRANCA", 	"tpSituacaoCobranca");
        sql.addProjection("DEV.ID_FILIAL", 				"idFilial");
        sql.addProjection("DS.NR_DOCTO_SERVICO", 		"nrDoctoServico");
        sql.addProjection("DS.DH_EMISSAO", 				"dhEmissao");
        sql.addProjection("DEV.ID_DEVEDOR_DOC_SERV_FAT","idDevedorDocServFat");
        sql.addProjection("CO.TP_SITUACAO_CONHECIMENTO","tpSituacaoConhecimento");
        sql.addProjection("F.SG_FILIAL", 				"sgFilial");
        sql.addProjection("CO.TP_CONHECIMENTO", "tpConhecimento");
        
        sql.addFrom("DOCTO_SERVICO", 			"DS");
        sql.addFrom("FILIAL", 					"F");
        sql.addFrom("SERVICO", 					"S");
        sql.addFrom("DEVEDOR_DOC_SERV_FAT", 	"DEV");
        sql.addFrom("PESSOA", 					"P");
        sql.addFrom("CONHECIMENTO", 			"CO");
        sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "NF");
        
        sql.addJoin("DS.ID_FILIAL_ORIGEM", 	"F.ID_FILIAL");
        sql.addJoin("DS.ID_DOCTO_SERVICO", 	"CO.ID_CONHECIMENTO");
        sql.addJoin("NF.ID_CONHECIMENTO", 	"CO.ID_CONHECIMENTO");
        sql.addJoin("DS.ID_SERVICO", 		"S.ID_SERVICO");
        sql.addJoin("DS.ID_DOCTO_SERVICO", 	"DEV.ID_DOCTO_SERVICO");
        sql.addJoin("DEV.ID_CLIENTE", 		"P.ID_PESSOA");
        
        sql.addCriteria("NF.NR_NOTA_FISCAL", "=", nrNotaFiscal);
        sql.addCriteria("DEV.ID_CLIENTE", "=", idCliente);
        sql.addCriteria("DEV.ID_FILIAL", "=", idFilial);
        sql.addCriteria("NF.DT_EMISSAO", "=", dataEmissao);
        
        return sql;
        
    }    

    /**
     * Busca os dados de Conhecimento (CTRC e NFT) para a listagem
     * @param map Critérios de pesquisa
     * @param findDef Dados de paginação
     * @return ResultSetPage com os dados resultantes da pesquisa e dados de paginação
     */
	public ResultSetPage findPaginatedDevedoresByConhecimento(Map map, FindDefinition findDef) {
		
		TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stConhecimento      = montaQueryConhecimento(tfm,false);
        
        return getAdsmHibernateTemplate().findPaginated(stConhecimento.getSql(),
        		                                        findDef.getCurrentPage(),
        		                                        findDef.getPageSize(), 
        		                                        stConhecimento.getCriteria());
                 
	}

	/**
     * Busca os dados de Conhecimento (CTRC e NFT) para a listagem
     * @param map Critérios de pesquisa
     * @param findDef Dados de paginação
     * @return ResultSetPage com os dados resultantes da pesquisa e dados de paginação
     */
	public List findDevedoresByConhecimento(Map map) {
		
		TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stConhecimento      = montaQueryConhecimento(tfm,false);
        
        return getAdsmHibernateTemplate().find(
        		stConhecimento.getSql(), 
        		stConhecimento.getCriteria());
	}
	
	/**
     * Busca os dados de Cto Internacional (CTO) para a listagem
     * @param map Critérios de pesquisa
     * @param findDef Dados de paginação
     * @return ResultSetPage com os dados resultantes da pesquisa e dados de paginação
     */	
	public ResultSetPage findPaginatedDevedoresByCtoInternacional(Map map, FindDefinition findDef) {
		
		TypedFlatMap tfm = (TypedFlatMap) map;

		SqlTemplate stCtoInternacional  = montaQueryCtoInternacional(tfm,false);
		
		return getAdsmHibernateTemplate().findPaginated(stCtoInternacional.getSql(),
														findDef.getCurrentPage(),
										                findDef.getPageSize(), 
										                stCtoInternacional.getCriteria());
		
	}
	
	/**
     * Busca os dados de Cto Internacional (CTO) para a listagem
     * @param map Critérios de pesquisa
     * @param findDef Dados de paginação
     * @return ResultSetPage com os dados resultantes da pesquisa e dados de paginação
     */	
	public List findDevedoresByCtoInternacional(Map map) {
		
		TypedFlatMap tfm = (TypedFlatMap) map;

		SqlTemplate stCtoInternacional  = montaQueryCtoInternacional(tfm,false);
		
		return getAdsmHibernateTemplate().find(
				stCtoInternacional.getSql(),
				stCtoInternacional.getCriteria());
	}

	/**
     * Busca os dados de Notas Fiscais de Serviço (NFS) para a listagem
     * @param map Critérios de pesquisa
     * @param findDef Dados de paginação
     * @return ResultSetPage com os dados resultantes da pesquisa e dados de paginação
     */
	public ResultSetPage findPaginatedDevedoresByNotaFiscalServico(Map map, FindDefinition findDef) {
		
		TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stNotasFiscais      = montaQueryNotasFiscais(tfm,false);
        
        return getAdsmHibernateTemplate().findPaginated(stNotasFiscais.getSql(),
        		                                        findDef.getCurrentPage(),
        		                                        findDef.getPageSize(), 
        		                                        stNotasFiscais.getCriteria());
	}
	
	/**
     * Busca os dados de Notas Fiscais de Serviço (NFS) para a listagem
     * @param map Critérios de pesquisa
     * @param findDef Dados de paginação
     * @return ResultSetPage com os dados resultantes da pesquisa e dados de paginação
     */
	public List findDevedoresByNotaFiscalServico(Map map) {
		
		TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stNotasFiscais      = montaQueryNotasFiscais(tfm,false);
        
        return getAdsmHibernateTemplate().find(
        		stNotasFiscais.getSql(),
        		stNotasFiscais.getCriteria());
	}
	
	/**
     * Busca quantos registros serão mostrados na listagem da ET Manter Devedores do Documento de Serviço
     * @param map Critérios de pesquisa
     * @return Inteiro que indica a quantidade de registros retornados na pesquisa
     */
    public Integer getRowCountDevedoresByConhecimento(Map map) {
        
        TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stConhecimento      = montaQueryConhecimento(tfm,true);        
        
        return getAdsmHibernateTemplate().getRowCountForQuery(stConhecimento.getSql(),
        													  stConhecimento.getCriteria());
        
    }
    
    /**
     * Busca quantos registros serão mostrados na listagem da ET Manter Devedores do Documento de Serviço
     * @param map Critérios de pesquisa
     * @return Inteiro que indica a quantidade de registros retornados na pesquisa
     */
    public Integer getRowCountDevedoresByCtoInternacional(Map map) {
        
        TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stCtoInternacional      = montaQueryCtoInternacional(tfm,true);        
        
        return getAdsmHibernateTemplate().getRowCountForQuery( stCtoInternacional.getSql(),
        													   stCtoInternacional.getCriteria());
        
    }
    
    /**
     * Busca quantos registros serão mostrados na listagem da ET Manter Devedores do Documento de Serviço
     * @param map Critérios de pesquisa
     * @return Inteiro que indica a quantidade de registros retornados na pesquisa
     */
    public Integer getRowCountDevedoresByNotaFiscalServico(Map map) {
        
        TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stNotasFiscais      = montaQueryNotasFiscais(tfm,false);        
        
        return getAdsmHibernateTemplate().getRowCountForQuery(stNotasFiscais.getSql(),
        													  stNotasFiscais.getCriteria());
        
    }

    /**
     * Busca dados do devedor
     * @param id Identificador do Devedor detalhado
     * @return Mapa de dados do detalhamento do Devedor
     */
	public Map findByIdMaped(Long id) {
		
		SqlTemplate  sql = new SqlTemplate();
		
		sql.addProjection("new Map(ddsf.id as idDevedorDocServFat, " +
				          "        ddsf.vlDevido as valor, " +
				          "        filialOrigem.id as idFilialOrigem, " +
				          "        filialOrigem.sgFilial as sgFilialOrigem, " +
				          "		   ds.idDoctoServico as idDoctoServico, " +
				          "		   d.dsDivisaoCliente as dsDivisaoClienteAnterior, " +
				          "		   d.idDivisaoCliente as idDivisaoClienteAnterior, " +	
				          "		   ds.nrDoctoServico as nrDoctoServico, " +
				          "        ds.vlTotalDocServico as vlDoctoServico, " +
				          "        ds.moeda.idMoeda as idMoeda, " +
				          "        ds.moeda.sgMoeda || ' ' || ds.moeda.dsSimbolo as siglaSimbolo, " +				          
				          "        ds.tpDocumentoServico as tpDocumentoServico, " +//tipoDocumento
				          "        devedorAnterior.id as idDevedorAnterior, " +
				          "        pDevedorAnterior.nrIdentificacao as nrIdentificacaoDevedorAnterior," +
				          "        pDevedorAnterior.nmPessoa as nmPessoaDevedorAnterior, " +
				          "        pDevedorAnterior.tpIdentificacao as tpIdentificacao " +
				          ")");
		
		
		sql.addFrom(DevedorDocServFat.class.getName() + " as ddsf " +				    
				    "	inner join ddsf.doctoServico as ds " +
				    "	left  join ddsf.divisaoCliente as d " +
				    "   inner join ds.filialByIdFilialOrigem as filialOrigem " +
				    "   inner join ddsf.cliente as devedorAnterior " +
				    "   inner join devedorAnterior.pessoa as pDevedorAnterior ");
		
		sql.addCriteria("ddsf.id","=",id);
		
		List retorno = this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if( retorno != null && !retorno.isEmpty() ){
			
			Map map = (Map) retorno.get(0);
			
			DomainValue dv = (DomainValue) map.get("tpDocumentoServico");
			
			map.put("tipoDocumento", dv.getValue());			
			
			return map;
			
		}
		
		return null;
	}
	
	/**
	 * Busca o devedor associado ao Documento de Serviço e ao Cliente
	 * @param idDoctoServico Identificador do Documento de Serviço
	 * @param idCliente Identificador do Cliente
	 * @return DevedorDocServFat associado ao documento de serviço e ao cliente
	 */
	public DevedorDocServFat findDevedorByIdDoctoServicoIdCliente(Long idDoctoServico, Long idCliente){
		
		DevedorDocServFat ddsf = null;
		
		SqlTemplate sql = getQueryHqlFromDevedores(idDoctoServico,idCliente);		
		
		sql.addProjection("new Map(ddsf.id as idDevedorDocServFat)");
		
		List lista = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if( lista != null && !lista.isEmpty() ){
			
			Map map = (Map) lista.get(0);
			
			ddsf = new DevedorDocServFat();
			ddsf.setIdDevedorDocServFat((Long) map.get("idDevedorDocServFat"));
			
		}
			
		
		return ddsf;
	}

	/**
	 * Query de devedores sem projection e sem order by, cada um deve implementar
	 * seu método e adicionar os respectivos projections e order by necessários para cada caso
	 * @param idDoctoServico Identificador do Documento de Serviço
	 * @param idCliente Identificador do Cliente
	 * @return SqlTemplate
	 */
	private SqlTemplate getQueryHqlFromDevedores(Long idDoctoServico, Long idCliente) {
		
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(DevedorDocServFat.class.getName() + " as ddsf " +				    
					    "	inner join ddsf.doctoServico as ds " +
					    "	inner join ds.moeda as moeda " +
					    "   inner join ddsf.cliente as c " +
					    "   inner join c.pessoa as p " );
		
		sql.addCriteria("ds.id","=",idDoctoServico);
		sql.addCriteria("c.id","=",idCliente);
		
		return sql;
	}
	
	public List findDevedorDocServFatByDoctoServico(Long idDoctoServico){

		SqlTemplate sql = getQueryHqlFromDevedores(idDoctoServico, null);

		sql.addProjection("new Map(ddsf.idDevedorDocServFat as idDevedorDocServFat");
		sql.addProjection("moeda.sgMoeda as sgMoeda");
		sql.addProjection("moeda.dsSimbolo as dsSimbolo");
		sql.addProjection("p.nmPessoa as nmPessoa");
		sql.addProjection("p.nrIdentificacao as nrIdentificacao");
		sql.addProjection("p.tpIdentificacao as tpIdentificacao");
		sql.addProjection("ddsf.tpSituacaoCobranca as tpSituacaoCobranca");
		sql.addProjection("ddsf.vlDevido as vlDevido ) ");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Método responsável por buscar o detalhamento de um devedorDocServfat para a tela de devedores
	 * 
	 * @param idDevedorDocServFat
	 * @return
	 */
	public Object[] findDevedorDocServFatDetail(Long idDevedorDocServFat){
		
		SqlTemplate sql = new SqlTemplate();
		
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("nrIdentificacao", Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacao", Hibernate.STRING);
				sqlQuery.addScalar("nmPessoa", Hibernate.STRING);

				sqlQuery.addScalar("dtVencimento", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dtLiquidacao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("tpSituacaoCobranca", Hibernate.STRING);
				sqlQuery.addScalar("fatura", Hibernate.STRING);
				sqlQuery.addScalar("redeco", Hibernate.STRING);
				sqlQuery.addScalar("recibo", Hibernate.STRING);
				sqlQuery.addScalar("boleto", Hibernate.STRING);
				sqlQuery.addScalar("relacaoCobranca", Hibernate.STRING);
				sqlQuery.addScalar("bdm", Hibernate.STRING);
				sqlQuery.addScalar("vlDesconto", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dsMotivoDesconto", Hibernate.STRING);
				sqlQuery.addScalar("vlDevido", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("valorPago", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("tpMotivoDesconto", Hibernate.STRING);
				sqlQuery.addScalar("tpSituacaoAprovacao", Hibernate.STRING);
				sqlQuery.addScalar("nrNotaDebitoNac", Hibernate.STRING);
				sqlQuery.addScalar("idFatura", Hibernate.LONG);
				sqlQuery.addScalar("idBdm", Hibernate.LONG);
				sqlQuery.addScalar("idRelCob", Hibernate.LONG);
				sqlQuery.addScalar("finalidadeRedeco", Hibernate.STRING);
				sqlQuery.addScalar("filialCobranca", Hibernate.STRING);
				
				sqlQuery.addScalar("nrFat", Hibernate.LONG);
				sqlQuery.addScalar("sgFilialFat", Hibernate.STRING);
				sqlQuery.addScalar("nmFantasiaFat", Hibernate.STRING);

				sqlQuery.addScalar("nrRc", Hibernate.LONG);
				sqlQuery.addScalar("sgFilialRc", Hibernate.STRING);
				sqlQuery.addScalar("nmFantasiaRc", Hibernate.STRING);
				
				sqlQuery.addScalar("nrBdm", Hibernate.LONG);
				sqlQuery.addScalar("sgFilialBdm", Hibernate.STRING);
				sqlQuery.addScalar("nmFantasiaBdm", Hibernate.STRING);
				sqlQuery.addScalar("idDevedorDocServFat", Hibernate.LONG);
				
				sqlQuery.addScalar("dsDivisaoCliente", Hibernate.STRING);

				sqlQuery.addScalar("dsTipoLogradouro", Hibernate.STRING);
				sqlQuery.addScalar("dsEndereco", Hibernate.STRING);
				sqlQuery.addScalar("nrEndereco", Hibernate.STRING);
				sqlQuery.addScalar("dsComplemento", Hibernate.STRING);
				sqlQuery.addScalar("dsBairro", Hibernate.STRING);
				sqlQuery.addScalar("nmMunicipio", Hibernate.STRING);
				sqlQuery.addScalar("sgUnidadeFederativa", Hibernate.STRING);
				sqlQuery.addScalar("nmPais", Hibernate.STRING);
				sqlQuery.addScalar("nrCep", Hibernate.STRING);
				
				sqlQuery.addScalar("nrTelefone", Hibernate.STRING);
				sqlQuery.addScalar("nrDdd", Hibernate.STRING);
				sqlQuery.addScalar("nrDdi", Hibernate.STRING);
				
				sqlQuery.addScalar("idFilialRc", Hibernate.LONG);
				sqlQuery.addScalar("idFilialBdm", Hibernate.LONG);
				
				sqlQuery.addScalar("recebimentosParciais", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("saldoDevedor", Hibernate.BIG_DECIMAL);
			}
		};
		sql.addProjection("p.nr_identificacao", "nrIdentificacao");
		sql.addProjection("p.tp_identificacao", "tpIdentificacao");
		sql.addProjection("p.nm_pessoa", "nmPessoa");

		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tl.ds_tipo_logradouro_i"), "dsTipoLogradouro");
		sql.addProjection("ep.ds_Endereco", "dsEndereco");
		sql.addProjection("ep.nr_Endereco", "nrEndereco");
		sql.addProjection("ep.ds_Complemento", "dsComplemento");
		sql.addProjection("ep.ds_Bairro", "dsBairro");
		sql.addProjection("mun.nm_Municipio", "nmMunicipio");
		sql.addProjection("uf.sg_Unidade_Federativa", "sgUnidadeFederativa");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("pa.nm_pais_i"), "nmPais");
		sql.addProjection("ep.nr_Cep", "nrCep");
		
		sql.addProjection("te.nr_Telefone", "nrTelefone");
		sql.addProjection("te.nr_ddd", "nrDdd");
		sql.addProjection("te.nr_ddi", "nrDdi");

		sql.addProjection("f.dt_vencimento", "dtVencimento");
		sql.addProjection("ddsf.dt_liquidacao", "dtLiquidacao");
		sql.addProjection("ddsf.tp_situacao_cobranca", "tpSituacaoCobranca");
		sql.addProjection("DECODE((filfat.sg_filial || ' ' || f.nr_fatura), ' ', '', (filfat.sg_filial || ' ' || to_char(f.nr_fatura, '0000000000')))", "fatura");
		sql.addProjection("DECODE((filred.sg_filial || ' ' || r.nr_redeco), ' ', '',  (filred.sg_filial || ' ' || to_char(r.nr_redeco, '0000000000')))", "redeco");
		sql.addProjection("DECODE((filrec.sg_filial || ' ' || rec.nr_recibo), ' ', '',  (filrec.sg_filial || ' ' || to_char(rec.nr_recibo, '0000000000')))", "recibo");
		sql.addProjection("trim(to_char(substr(b.nr_boleto, 1, length(b.nr_boleto)-1), '000000000000') || substr(b.nr_boleto, length(b.nr_boleto), 1))", "boleto");
		sql.addProjection("DECODE((filrc.sg_filial || ' ' || rc.nr_relacao_cobranca_filial), ' ', '',  (filrc.sg_filial || ' ' || to_char(rc.nr_relacao_cobranca_filial, '0000000000')))", "relacaoCobranca");
		sql.addProjection("DECODE((bdm.sg_filial || ' ' || bdm.nr_bdm), ' ', '',  (bdm.sg_filial || ' ' || bdm.nr_bdm))", "bdm");
		sql.addProjection("d.vl_desconto", "vlDesconto");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("md.ds_motivo_desconto_i"), "dsMotivoDesconto");
		sql.addProjection("ddsf.vl_devido", "vlDevido");
		sql.addProjection("divcli.ds_divisao_cliente", "dsDivisaoCliente");
		sql.addProjection("DECODE(f.dt_liquidacao, null, 0, DECODE(d.vl_desconto, null, ddsf.vl_devido, (ddsf.vl_devido - d.vl_desconto)))", "valorPago");
		sql.addProjection("md.tp_motivo_desconto", "tpMotivoDesconto");
		sql.addProjection("d.tp_situacao_aprovacao", "tpSituacaoAprovacao");
		sql.addProjection("to_char(ndn.nr_nota_debito_nac, '0000000000')", "nrNotaDebitoNac");
		sql.addProjection("f.id_fatura", "idFatura");
		sql.addProjection("bdm.id_baixa_dev_merc", "idBdm");
		sql.addProjection("rc.id_relacao_cobranca", "idRelCob");
		sql.addProjection("r.tp_finalidade", "finalidadeRedeco");
		sql.addProjection("fildev.sg_filial", "filialCobranca");
		sql.addProjection("f.nr_fatura", "nrFat");
		sql.addProjection("filfat.sg_filial ", "sgFilialFat");
		sql.addProjection("pesfat.nm_fantasia ", "nmFantasiaFat");
		sql.addProjection("rc.nr_relacao_cobranca_filial", "nrRc");
		sql.addProjection("filrc.id_filial", "idFilialRc");
		sql.addProjection("filrc.sg_filial", "sgFilialRc");
		sql.addProjection("pesrc.nm_fantasia", "nmFantasiaRc");
		sql.addProjection("bdm.nr_bdm", "nrBdm");
		sql.addProjection("bdm.id_filial", "idFilialBdm");
		sql.addProjection("bdm.sg_filial", "sgFilialBdm");
		sql.addProjection("bdm.nm_fantasia ", "nmFantasiaBdm");
		sql.addProjection("ddsf.id_devedor_doc_serv_fat", "idDevedorDocServFat");
		
		// 2683
		sql.addProjection("(select nvl(sum(irpp.vl_pagamento),0) from item_rel_pagto_parcial irpp where ddsf.id_devedor_doc_serv_fat = irpp.id_devedor_doc_serv_fat)", "recebimentosParciais");
		
		sql.addProjection(	"(nvl(ddsf.vl_devido,0) - " +
							"nvl((select desc_tmp.vl_desconto from desconto desc_tmp where desc_tmp.id_devedor_doc_serv_fat = ddsf.id_devedor_doc_serv_fat),0) - "+
							"(select nvl(sum(irpp_tmp.vl_pagamento),0) from item_rel_pagto_parcial irpp_tmp where irpp_tmp.id_devedor_doc_serv_fat = ddsf.id_devedor_doc_serv_fat)"+
							")" , "saldoDevedor");
		
		sql.addFrom("devedor_doc_serv_fat", "ddsf");
		sql.addFrom("cliente", "c");
		sql.addFrom("filial", "fildev");
		sql.addFrom("pessoa", "p");
		sql.addFrom("endereco_pessoa", "ep");
		sql.addFrom("tipo_logradouro", "tl");
		sql.addFrom("municipio", "mun");
		sql.addFrom("unidade_federativa", "uf");
		sql.addFrom("pais", "pa");

		sql.addFrom("telefone_endereco", "te");
		
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("divisao_cliente", "divcli");
		sql.addFrom("fatura", "f");
		sql.addFrom("filial", "filfat");
		sql.addFrom("pessoa", "pesfat");
		sql.addFrom("redeco", "r");
		sql.addFrom("filial", "filred");
		sql.addFrom("recibo", "rec");
		sql.addFrom("filial", "filrec");
		sql.addFrom("boleto", "b");
		sql.addFrom("relacao_cobranca", "rc");
		sql.addFrom("filial", "filrc");
		sql.addFrom("pessoa", "pesrc");
		sql.addFrom("(select ibdm_tmp.id_devedor_doc_serv_fat, bdm_tmp.id_baixa_dev_merc, fil_tmp.sg_filial, fil_tmp.id_filial, bdm_tmp.nr_bdm, pes_tmp.nm_fantasia\n"+
	        		" from   baixa_dev_merc bdm_tmp,\n"+
	        		"        item_baixa_dev_merc ibdm_tmp,\n"+
	                "        filial fil_tmp,\n"+
	                "        pessoa pes_tmp\n"+
	                " where  bdm_tmp.id_baixa_dev_merc = ibdm_tmp.id_baixa_dev_merc\n"+
	                " and    bdm_tmp.id_filial_emissora = fil_tmp.id_filial\n"+
	                " and    fil_tmp.id_filial = pes_tmp.id_pessoa\n"+
	                " and    bdm_tmp.tp_situacao <> 'C')", "bdm");
		sql.addFrom("desconto", "d");
		sql.addFrom("motivo_desconto", "md");
		sql.addFrom("nota_debito_nacional", "ndn");
		
		sql.addJoin("ddsf.id_cliente", "c.id_cliente");
		sql.addJoin("ddsf.id_filial", "fildev.id_filial");
		sql.addJoin("c.id_cliente", "p.id_pessoa");
		sql.addJoin("ep.id_endereco_pessoa", "F_BUSCA_ENDERECO_PESSOA(p.id_pessoa, 'COB', sysdate)");
		sql.addJoin("tl.id_tipo_logradouro", "ep.id_tipo_logradouro");
		sql.addJoin("mun.id_municipio", "ep.id_municipio");
		sql.addJoin("uf.id_unidade_federativa", "mun.id_unidade_federativa");
		sql.addJoin("pa.id_pais", "uf.id_pais");
		
		sql.addJoin("te.id_endereco_pessoa(+)", "ep.id_endereco_pessoa");
		sql.addCustomCriteria("" +
				"(	te.id_endereco_pessoa is null " +
				"or te.id_telefone_endereco = (	select	min(te_tmp.id_telefone_endereco) " +
				"								from 	telefone_endereco te_tmp " +
				"								where 	te_tmp.id_endereco_pessoa = ep.id_endereco_pessoa " +
				"							  )" +
				")");
		
		sql.addJoin("ds.id_docto_servico", "ddsf.id_docto_servico");
		sql.addJoin("divcli.id_divisao_cliente(+)", "ddsf.id_divisao_cliente");
		sql.addJoin("f.id_fatura(+)", "ddsf.id_fatura");
		sql.addJoin("filfat.id_filial(+)", "f.id_filial");
		sql.addJoin("filfat.id_filial", "pesfat.id_pessoa(+)");
		sql.addJoin("r.id_redeco(+)", "f.id_redeco");
		sql.addJoin("filred.id_filial(+)", "r.id_filial");
		sql.addJoin("rec.id_recibo(+)", "f.id_recibo");
		sql.addJoin("filrec.id_filial(+)", "rec.ID_FILIAL_EMISSORA");
		sql.addJoin("b.id_boleto(+)", "f.id_boleto");
		sql.addJoin("rc.id_relacao_cobranca(+)", "f.id_relacao_cobranca");
		sql.addJoin("filrc.id_filial(+)", "rc.id_filial");
		sql.addJoin("pesrc.id_pessoa(+)", "filrc.id_filial");
		sql.addJoin("bdm.id_devedor_doc_serv_fat(+)","ddsf.id_devedor_doc_serv_fat");
		sql.addJoin("ddsf.id_devedor_doc_serv_fat", "d.id_devedor_doc_serv_fat(+)");
		sql.addJoin("md.id_motivo_desconto(+)", "d.id_motivo_desconto");
		sql.addJoin("ndn.id_nota_debito_nacional(+)", "f.id_nota_debito_nacional");
		
		if(idDevedorDocServFat != null){
			sql.addCriteria("ddsf.id_devedor_doc_serv_fat", "=", idDevedorDocServFat);
		}
		
		return (Object[])getAdsmHibernateTemplate().findByIdBySql(sql.getSql(), sql.getCriteria(), configSql);
	}
	
    /**
     * @author edenilsonf
     * @param idDoctoServico
     * @return true se existe devedor diferente de em carteira ou pendente
     */
    public boolean findDevedorSituacaoCobranca(Long idDoctoServico){
    	Validate.notNull(idDoctoServico);
		
    	SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("d");
		sql.addFrom(DevedorDocServFat.class.getName(),"d");
		sql.addCriteria("d.doctoServico.id","=",idDoctoServico);
		sql.addCriteria("d.tpSituacaoCobranca", "<>", "P");
		sql.addCriteria("d.tpSituacaoCobranca", "<>", "C");
		
		List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		return ( retorno != null && !retorno.isEmpty() );

    }

	/**
     * @author José Rodrigo Moraes
     * @since 31/05/2006
     * 
     * Retorna somente um devedorDocServFat sem nenhum outro objeto associado junto
     * @param idDevedorDocServFat Identificador do Devedor
     * @return DevedorDocServFat
     */
	public DevedorDocServFat findByIdSimplificado(Long idDevedorDocServFat) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("d");
		sql.addFrom(DevedorDocServFat.class.getName(),"d");
		sql.addCriteria("d.id","=",idDevedorDocServFat);
		
		List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		DevedorDocServFat devedorDocServFat = null;
		
		if( retorno != null && !retorno.isEmpty() ){
			devedorDocServFat = (DevedorDocServFat) retorno.get(0);
		}
		
		return devedorDocServFat;
	}
	
    /**
     * Retorna a lista dos devedores do redeco (passando por fatura ou recibo), vem o desconto
     * 'fetchado'
     * 
     * @author Mickaël Jalbert
     * @since 13/07/2006
     * 
     * @param Long idDoctoServico
     * 
     * @return List
     */
    public List findByRedeco(Long idRedeco, Boolean blInnerJoinDesconto){
    	SqlTemplate hqlRed = mountHqlByRedeco(idRedeco, blInnerJoinDesconto);
    	hqlRed.addInnerJoin("fat.itemRedecos", "itr");
    	
    	SqlTemplate hqlRec = mountHqlByRedeco(idRedeco, blInnerJoinDesconto);
    	hqlRec.addInnerJoin("fat.faturaRecibos", "fatRec");
    	hqlRec.addInnerJoin("fatRec.recibo", "rec");
    	hqlRec.addInnerJoin("rec.itemRedecos", "itr");


       List lstRedeco = getAdsmHibernateTemplate().find(hqlRed.getSql(),hqlRed.getCriteria());
       
       List lstRecibo = getAdsmHibernateTemplate().find(hqlRec.getSql(),hqlRec.getCriteria());
       
       lstRecibo.addAll(lstRedeco);
       
       return lstRecibo;
    }

	/**
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 * @return
	 */
	private SqlTemplate mountHqlByRedeco(Long idRedeco, Boolean blInnerJoinDesconto) {
		SqlTemplate hql = new SqlTemplate();
		
    	hql.addProjection("dev");
    	
        hql.addInnerJoin(DevedorDocServFat.class.getName(), "dev");
        hql.addInnerJoin("dev.doctoServico", "ds");
        hql.addInnerJoin("ds.filialByIdFilialOrigem", "filOriDs");
        hql.addInnerJoin("fetch dev.cliente", "cli");
        hql.addInnerJoin("fetch cli.pessoa", "pes");
        hql.addInnerJoin("fetch dev.cliente.filialByIdFilialCobranca", "filcob");
        hql.addInnerJoin("fetch dev.filial", "fil");
        hql.addInnerJoin("dev.itemFaturas", "itf");
        hql.addInnerJoin("itf.fatura", "fat");
        
        if (blInnerJoinDesconto){
        	hql.addInnerJoin("fetch dev.descontos", "des");
        } else {
        	hql.addLeftOuterJoin("fetch dev.descontos", "des");
        }
        hql.addCriteria("itr.redeco.id", "=", idRedeco);
        hql.addOrderBy("pes.nmPessoa, ds.tpDocumentoServico, filOriDs.sgFilial, ds.nrDoctoServico");
        
		return hql;
	}
    
    /**
     * Atualiza todos os devedores com a situação de cobrança informada da fatura informada.
     * 
     * @author Mickaël Jalbert
     * @since 20/07/2006
     * 
     * @param Long idFatura
     * @param String tpSituacaoCobranca
     */    
    public void executeUpdateSituacaoByIdFatura(Long idFatura, String tpSituacaoCobranca){
    	StringBuilder sb = new StringBuilder()

    	.append(" UPDATE 	" + DevedorDocServFat.class.getName() + " AS dev \n")
		.append(" SET 		dev.tpSituacaoCobranca = ?, dev.fatura.id = ? \n") 
		.append(" WHERE		dev.id IN ( \n") 
		.append("		SELECT 		devSub.id \n")
		.append("		FROM 		" + ItemFatura.class.getName() + " AS ifat, \n")
		.append("					" + Fatura.class.getName() + " AS fat, \n")
		.append("		 			" + DevedorDocServFat.class.getName() + " AS devSub \n")
		.append("		WHERE      	fat.id = ? \n")
		.append("		AND 	   	ifat.fatura.id = fat.id \n")
		.append("		AND 	   	ifat.devedorDocServFat.id = devSub.id \n")
		.append(" ) \n");
    
    	List lstFiltros = new ArrayList();
    	
    	lstFiltros.add(tpSituacaoCobranca);
    	lstFiltros.add(idFatura);
    	lstFiltros.add(idFatura);
    	
    	super.executeHql(sb.toString(), lstFiltros);
    }   
    
    /**
     * Liquida os devedores da fatura
     *
     * @author Hector Julian Esnaola Junior
     * @since 29/01/2007
     *
     * @param devedorDocServFat
     * @param dtLiquidacao
     * @param idFatura
     *
     */
    public void liquidaDevedorDocServFat(Long idFatura, YearMonthDay dtLiquidacao, String tpSituacaoCobranca){
    	StringBuilder sb = new StringBuilder()
	    	.append(" UPDATE 	" + DevedorDocServFat.class.getName() + " AS dev \n")
			.append(" SET 		dev.dtLiquidacao = ?, dev.tpSituacaoCobranca = ? \n") 
			.append(" WHERE		dev.id IN ( \n") 
			.append("		SELECT 		devSub.id \n")
			.append("		FROM 		" + ItemFatura.class.getName() + " AS ifat, \n")
			.append("					" + Fatura.class.getName() + " AS fat, \n")
			.append("		 			" + DevedorDocServFat.class.getName() + " AS devSub \n")
			.append("		WHERE      	fat.id = ? \n")
			.append("		AND 	   	ifat.fatura.id = fat.id \n")
			.append("		AND 	   	ifat.devedorDocServFat.id = devSub.id \n")
			.append(" ) \n");
    
    	List lstFiltros = new ArrayList();
    	
    	lstFiltros.add(dtLiquidacao);
    	lstFiltros.add(tpSituacaoCobranca);
    	lstFiltros.add(idFatura);
    	
    	super.executeHql(sb.toString(), lstFiltros);
    }
    /**
     * Retorna o devedor do manifesto de viagem informado
     * 
     * @author Mickaël Jalbert
     * @since 06/10/2006
     * 
     * @param Long idManifesto
     * @return DevedorDocServFat
     */
    public DevedorDocServFat findByManifestoNacionalCto(Long idManifestoNacionalCto){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("dev");
    	
    	hql.addInnerJoin(ManifestoNacionalCto.class.getName(), "man");
    	hql.addInnerJoin("man.conhecimento", "doc");
    	hql.addInnerJoin("man.manifestoViagemNacional", "mvn");
    	hql.addInnerJoin("mvn.manifesto", "mav");
    	hql.addInnerJoin("doc.devedorDocServFats", "dev");
    	hql.addInnerJoin("doc.servico", "ser");
    	hql.addInnerJoin("fetch dev.cliente", "cli");
    	hql.addLeftOuterJoin("fetch dev.divisaoCliente", "div");
    	
    	hql.addCriteria("man.id", "=", idManifestoNacionalCto);
    	
    	hql.addCustomCriteria("dev.filial.id = mav.filialByIdFilialOrigem.id");
    	
    	hql.addCustomCriteria("doc.tpFrete = 'F'");
    	
    	hql.addCustomCriteria("doc.tpCtrcParceria = 'M'");
    	
    	List lstDevedor = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (!lstDevedor.isEmpty()){
    		return (DevedorDocServFat)lstDevedor.get(0);
    	} else {
    		return null;
    	}
    } 
    
    
    
    /**
     * Retorna o devedore do manifesto de entrega informado
     * 
     * @author Mickaël Jalbert
     * @since 06/10/2006
     * 
     * @param Long idManifesto
     * @return DevedorDocServFat
     */
    public DevedorDocServFat findByManifestoEntregaDocumento(Long idManifestoEntregaDocumento){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("dev");
    	
    	hql.addInnerJoin(ManifestoEntregaDocumento.class.getName(), "man");
    	hql.addInnerJoin("man.manifestoEntrega", "mane");
    	hql.addInnerJoin("man.doctoServico", "doc");
    	hql.addInnerJoin("doc.devedorDocServFats", "dev");
    	hql.addInnerJoin("fetch dev.cliente", "cli");
    	hql.addLeftOuterJoin("fetch dev.divisaoCliente", "div");
    	
    	hql.addJoin("mane.filial.id", "dev.filial.id");
    	
    	hql.addCriteria("man.id", "=", idManifestoEntregaDocumento);
    	
    	List lstDevedor = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (!lstDevedor.isEmpty()){
    		return (DevedorDocServFat)lstDevedor.get(0);
    	} else {
    		return null;
    	}
    }    
    
    /**
     * Busca o devedorDocServFat de acordo com o idDoctoServico
     *
     * @author Hector Julian Esnaola Junior
     * @since 18/01/2007
     *
     * @param idDoctoServico
     * @return
     *
     */
    public DevedorDocServFat findDevedorDocServFatByIdDoctoServico(Long idDoctoServico){
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection(" ddsf ");
    	
    	hql.addInnerJoin(getPersistentClass().getName() + " ddsf ");
    	hql.addInnerJoin("ddsf.doctoServico ds");
    	
    	hql.addCriteria("ds.idDoctoServico", "=", idDoctoServico);
    	
    	return (DevedorDocServFat) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());    	
    }
    
    /**
     * Busca o devedorDocServFat de acordo com o nrDoctoServico e a filialOrigem do documento de serviço
     *
     * @author Hector Julian Esnaola Junior
     * @since 18/01/2007
     *
     * @param nrDoctoServico
     * @param idFilialOrigem
     * @return
     *
     */
    public DevedorDocServFat findDevedorDocServFatByNrDoctoServicoAndIdFilialOrigem(Long nrDoctoServico, Long idFilialOrigem){
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection(" ddsf ");
    	
    	hql.addInnerJoin(getPersistentClass().getName() + " ddsf ");
    	hql.addInnerJoin("fetch ddsf.doctoServico ds");
    	
    	hql.addCriteria("ds.nrDoctoServico", "=", nrDoctoServico);
    	hql.addCriteria("ds.filialByIdFilialOrigem.id", "=", idFilialOrigem);
		hql.addCriteriaIn("ds.tpDocumentoServico", new Object[] { "CTR", "CTE" });
    	
    	return (DevedorDocServFat) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());    	
    }
    
    
   
    
    /**
     * Busca o devedorDocServFat de acordo com o idDoctoServico
     *
     * @author Hector Julian Esnaola Junior
     * @since 18/01/2007
     *
     * @param idDoctoServico
     * @return
     *
     */
    public DevedorDocServFat findDevedorDocServFatByIntegracao(Long nrConhecimento, Long idFilial, String tpDocumentoServico){

    	StringBuilder hql = new StringBuilder()
    	.append("select ddsf ")
    	.append("from ").append(DevedorDocServFat.class.getName()).append(" as ddsf ")
    	.append("inner join fetch ddsf.cliente c ")
    	.append("inner join fetch ddsf.filial f ")
    	.append("inner join ddsf.doctoServico ds ")
    	.append("where ds.nrDoctoServico = ? ")
		.append("and ds.filialByIdFilialOrigem.id = ? ")
		.append("and ds.tpDocumentoServico = ? ");

    	
    	List result = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{nrConhecimento, idFilial, tpDocumentoServico});
    	return result.isEmpty() ? null : (DevedorDocServFat)result.get(0);
    }
    
	/**
	 * Ref.	LMS-3910
	 * 
	 * @param idDevedor
	 * @return Boolean
	 */
    public Boolean findDevedorSituCobrancaCP(Long idDevedor) {

		StringBuilder hql = new StringBuilder()
    	.append("SELECT ddsf ")
    	.append("FROM ").append(DevedorDocServFat.class.getName()).append(" AS ddsf ")
    	.append("WHERE ddsf.id = ? ")
    	.append("AND (ddsf.tpSituacaoCobranca  = 'P'  ")
    	.append("	OR ddsf.tpSituacaoCobranca = 'C') ");

		List result = getAdsmHibernateTemplate().find(hql.toString(), new Object[] { idDevedor });
		return result.isEmpty() ? false : true;
    }

}