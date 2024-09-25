package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LacreControleCargaDAO extends BaseCrudDao<LacreControleCarga, Long>
{
	   
	private final String queryLacresFechadosByIdControleCarga = new StringBuffer()
	.append("select new Map(lcc.nrLacres as nrLacres, lcc.id as idLacre, lcc.versao as versao) ")
	.append("from " + LacreControleCarga.class.getName() + " lcc ")
	.append("where lcc.controleCarga.id = ? ")
	.append("and lcc.tpStatusLacre = ? ").toString();
	
	private final String queryLacreControleCargaByIdControleCargaAndNrLacre = new StringBuffer()
	.append("from " + LacreControleCarga.class.getName() + " lcc ")
	.append("where lcc.controleCarga.id = ? ")
	.append("and   lcc.nrLacres = ? ").toString();
	
	private final String queryLacreControleCargaByIdControleCargaAndTpStatus = new StringBuffer()
	.append("from " + LacreControleCarga.class.getName() + " lcc ")
	.append("where lcc.controleCarga.id = ? ")
	.append("and   lcc.tpStatusLacre = ? ").toString();
	
	
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return LacreControleCarga.class;
    }

   public List findByControleCarga(Long idControleCarga){
	   DetachedCriteria dc = createDetachedCriteria();
	   ProjectionList pl = Projections.projectionList()
	   						.add(Projections.property("idLacreControleCarga"), "idLacreControleCarga")
	   						.add(Projections.property("nrLacres"), "nrLacres");
	   dc.setProjection(pl);
	   dc.createAlias("controleCarga", "cc");	   
	   dc.add(Restrictions.eq("cc.idControleCarga", idControleCarga));
	   dc.add(Restrictions.eq("tpStatusLacre", "FE"));
	   dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());	   
	   
	   return findByDetachedCriteria(dc);
   }
   
   /**
    * Retorna todos os lacres cujo status = FE, 
    * de acordo com o id do controle de carga.
    * @param idControleCarga
    * @return
    * @author luisfco
    */
   public List findLacresFechadosByIdControleCarga(Long idControleCarga) {
	   return getAdsmHibernateTemplate().find(queryLacresFechadosByIdControleCarga.toString(), new Object[]{idControleCarga, "FE"});
   }
   
   /**
    * Retorna o lacre de controle de cargas identificado pelo número do lacre e pelo idControleCarga.
    * @param idControleCarga
    * @param nrLacre
    * @return
    * @author luisfco
    */
   public LacreControleCarga findLacreControleCargaByIdControleCargaAndNrLacre(Long idControleCarga, String nrLacres) {
	   Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryLacreControleCargaByIdControleCargaAndNrLacre);
	   q.setParameter(0, idControleCarga);
	   q.setParameter(1, nrLacres);
	   return (LacreControleCarga) q.uniqueResult();
   }
   
	private DetachedCriteria getDetachedCriteria(Long idControleCarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(LacreControleCarga.class);
		dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
		
		return dc;
	}
	
	 public List findLacreControleCargaByIdControleCargaAndTpStatus(Long idControleCarga, String tpStatus) {
		   Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryLacreControleCargaByIdControleCargaAndTpStatus);
		   q.setParameter(0, idControleCarga);
		   q.setParameter(1, tpStatus);
		   return q.list();
	   }
	   
		
	
	public List findLacreControleCargaByIdControleCarga(Long idControleCarga) {
		return super.findByDetachedCriteria(getDetachedCriteria(idControleCarga));		
	}	
	
	public Integer getRowCountLacreControleCargaByIdControleCarga(Long idControleCarga) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idControleCarga)
				.setProjection(Projections.rowCount()));
	}	

    /**
     * Salva os lacres de um controle de carga
     * 
     * @param listLacreControleCarga
     */
    public void storeLacresControleCarga(List listLacreControleCarga) {
		getAdsmHibernateTemplate().saveOrUpdateAll(listLacreControleCarga);
	}	
	
    
    
    /**
     * 
     * @param map
     * @param param
     * @param isRowCount
     * @return
     */
    private String addSqlByControleCarga(Long idControleCarga, List param, boolean isRowCount) {
    	StringBuffer sql = new StringBuffer();
		if (!isRowCount) {
	    	sql.append("select new map(")
	    	.append("lcc.idLacreControleCarga as idLacreControleCarga, ")
	    	.append("lcc.nrLacre as nrLacre, ")
	    	.append("lcc.nrLacres as nrLacres, ")
			.append("lcc.dhInclusao as dhInclusao, ")
			.append("lcc.tpStatusLacre as tpStatusLacre, ")
			.append("lcc.obInclusaoLacre as obInclusaoLacre, ")
			.append("lcc.dhAlteracao as dhAlteracao, ")
			.append("lcc.obConferenciaLacre as obConferenciaLacre, ")
			.append("filialInclusao.sgFilial as filialByIdFilialInclusao_sgFilial, ")
			.append("filialAlteraStatus.sgFilial as filialByIdFilialAlteraStatus_sgFilial, ")
			.append("usuarioInclusao.nmUsuario as usuarioByIdFuncInclusao_nmUsuario, ")
			.append("usuarioAlteraStatus.nmUsuario as usuarioByIdFuncAlteraStatus_nmUsuario) ");
		}
		sql.append("from ")
		.append(LacreControleCarga.class.getName()).append(" as lcc ")
		.append("inner join lcc.controleCarga as cc ")
		.append("inner join lcc.filialByIdFilialInclusao as filialInclusao ")
		.append("inner join lcc.usuarioByIdFuncInclusao usuarioInclusao ")
		.append("left join lcc.filialByIdFilialAlteraStatus as filialAlteraStatus ")
		.append("left join lcc.usuarioByIdFuncAlteraStatus as usuarioAlteraStatus ")
		.append("where ")
		.append("cc.id = ? ")
		.append("order by lcc.dhInclusao.value desc ");
		
		param.add(idControleCarga);
		return sql.toString();
    }


    /**
     * 
     * @param map
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedByControleCarga(Long idControleCarga, FindDefinition findDefinition) {
		List param = new ArrayList();
		String sql = addSqlByControleCarga(idControleCarga, param, false);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
	    return rsp;
	    	
    }


    /**
     * 
     * @param map
     * @return
     */
    public Integer getRowCountByControleCarga(Long idControleCarga) {
		List param = new ArrayList();
		String sql = addSqlByControleCarga(idControleCarga, param, true);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql, param.toArray());
    }


    /**
     * 
     * @param idPagtoProprietarioCc
     * @return
     */
    public Map findByIdByControleCarga(Long idLacreControleCarga) {
    	StringBuffer sql = new StringBuffer()
    		.append("select new map(")
	    	.append("lcc.idLacreControleCarga as idLacreControleCarga, ")
	    	.append("lcc.nrLacre as nrLacre, ")
	    	.append("lcc.nrLacres as nrLacres, ")
	    	.append("lcc.dsLocalInclusao as dsLocalInclusao, ")
	    	.append("lcc.dsLocalConferencia as dsLocalConferencia, ")
			.append("lcc.dhInclusao as dhInclusao, ")
			.append("lcc.tpStatusLacre as tpStatusLacre, ")
			.append("lcc.obInclusaoLacre as obInclusaoLacre, ")
			.append("lcc.dhAlteracao as dhAlteracao, ")
			.append("lcc.obConferenciaLacre as obConferenciaLacre, ")
			.append("filialInclusao.idFilial as filialByIdFilialInclusao_idFilial, ")
			.append("filialInclusao.sgFilial as filialByIdFilialInclusao_sgFilial, ")
			.append("pessoaFilialInclusao.nmFantasia as filialByIdFilialInclusao_pessoa_nmFantasia, ")
			.append("filialAlteraStatus.idFilial as filialByIdFilialAlteraStatus_idFilial, ")
			.append("filialAlteraStatus.sgFilial as filialByIdFilialAlteraStatus_sgFilial, ")
			.append("pessoaFilialAlteraStatus.nmFantasia as filialByIdFilialAlteraStatus_pessoa_nmFantasia) ")
			.append("from ")
			.append(LacreControleCarga.class.getName()).append(" as lcc ")
			.append("inner join lcc.controleCarga as cc ")
			.append("inner join lcc.filialByIdFilialInclusao as filialInclusao ")
			.append("inner join filialInclusao.pessoa as pessoaFilialInclusao ")
			.append("left join lcc.filialByIdFilialAlteraStatus as filialAlteraStatus ")
			.append("left join filialAlteraStatus.pessoa as pessoaFilialAlteraStatus ")
			.append("where ")
			.append("lcc.id = ? ");

		List param = new ArrayList();
		param.add(idLacreControleCarga);

		List lista = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	    return (Map)lista.get(0);
    }
}