package com.mercurio.lms.indenizacoes.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.EventoRim;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoRimDAO extends BaseCrudDao<EventoRim, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EventoRim.class;
    }

   
    /**
     * 
     * @param idReciboIndenizacao
     * @return ResultSetPage
     */
    public ResultSetPage findPaginatedEventoRimByIdReciboIndenizacao(Long idReciboIndenizacao, FindDefinition fd) {

		ResultSetPage rsp = findPaginatedEventoRimByIdReciboIndenizacao(idReciboIndenizacao, fd.getCurrentPage(), fd.getPageSize());
    	List retorno = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(rsp.getList());
    	rsp.setList(retorno); 
    	return rsp;
    }
    
    /**
     * 
     * @param idReciboIndenizacao
     * @param currentPage
     * @param pageSize
     * @return
     */
    public ResultSetPage findPaginatedEventoRimByIdReciboIndenizacao(Long idReciboIndenizacao, Integer currentPage, Integer pageSize) {

    	StringBuffer hql = new StringBuffer()
    	.append("select new map (")    	
    	.append("er.idEventoRim 				AS eventoRim_idEventoRim")
    	.append(", er.dhEventoRim				AS eventoRim_dhEventoRim")
    	.append(", er.tpEventoIndenizacao		AS eventoRim_tpEventoIndenizacao")
    	.append(", fi.sgFilial					AS filial_sgFilial")
    	.append(", us.nmUsuario                 AS usuario_nmUsuario")
    	.append(", mcr.dsMotivoCancelamentoRim  AS motivoCancelamentoRim_dsMotivoCancelamentoRim ) ")
    	.append(addJoinsTofindEventoRimByIdReciboIndenizacao())
    	.append(" order by er.dhEventoRim");

		List param = new ArrayList();
		param.add(idReciboIndenizacao);
		
		ResultSetPage rsp = this.getAdsmHibernateTemplate().findPaginated(hql.toString(), currentPage, pageSize ,param.toArray());
    	
    	return rsp;
    }

    /**
     * Retona o num de registros encontrados
     * 
     * @param idReciboIndenizacao
     * @return Integer
     */
    public Long findEventoRimByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	StringBuffer hql = new StringBuffer()
	    	.append("select count(er.id) as qtd ")
	    	.append("FROM " + EventoRim.class.getName() + " er ")
	    	.append("JOIN er.reciboIndenizacao 				ri ")
	    	.append("where ri.idReciboIndenizacao = ? ")
	    	.append("and er.tpEventoIndenizacao = 'PM' ");

		return (Long) this.getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idReciboIndenizacao});
	}
    
    /**
     * 
     * @param idReciboIndenizacao
     * @return Integer
     */
    public Integer getRowCountEventoRimByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	StringBuffer hql = new StringBuffer(); 
    	hql.append("select count(*) ");
    	hql.append(addJoinsTofindEventoRimByIdReciboIndenizacao());
    	
		List param = new ArrayList();
		param.add(idReciboIndenizacao);
		Long result = (Long)this.getAdsmHibernateTemplate().findUniqueResult(hql.toString(), param.toArray());
		
    	return result.intValue();
    }

   	private String addJoinsTofindEventoRimByIdReciboIndenizacao() {
    	StringBuffer hql = new StringBuffer()
    	.append("FROM "+ EventoRim.class.getName() +" 	er ")
    	.append("JOIN er.reciboIndenizacao 				ri ")
    	.append("JOIN er.filial 						fi ")
    	.append("JOIN er.usuario 						us ")
    	.append("LEFT JOIN er.motivoCancelamentoRim mcr ")
    	.append("WHERE ri.idReciboIndenizacao = ?");

    	return hql.toString();

    }


	public ResultSetPage findPaginatedEventoRim(TypedFlatMap tfm, FindDefinition fd) {
		StringBuffer hql = new StringBuffer();	
		Long idReciboIndenizacao = tfm.getLong("idReciboIndenizacao");
		
    	hql.append("Select new map (");    	    	
    	hql.append("er.dhEventoRim as dhEventoRim, ");
    	hql.append("er.tpEventoIndenizacao as tpIdenizacaoEventoRim, ");
    	hql.append("fi.sgFilial as sgFilial, ");
    	hql.append("us.nmUsuario as nmUsuario, ");
    	hql.append("mcr.dsMotivoCancelamentoRim as dsMotivoCancelamento, ");
    	hql.append("er.idEventoRim as idEventoRim) ");
    	
    	hql.append(addJoinsTofindEventoRim());
    	
    	hql.append("Where ri.idReciboIndenizacao = ?");
    	hql.append("order by er.dhEventoRim");

		List param = new ArrayList();
		param.add(idReciboIndenizacao);
		
		ResultSetPage rsp = this.getAdsmHibernateTemplate().findPaginated(hql.toString(), fd.getCurrentPage(), fd.getPageSize(), param.toArray());
		
		return rsp;
	}

	private Object addJoinsTofindEventoRim() {
		StringBuffer hql = new StringBuffer();	
		
		hql.append("From "+ EventoRim.class.getName() +" as er ");
    	hql.append("Join er.reciboIndenizacao as ri ");
    	hql.append("Join er.filial as fi ");
    	hql.append("Join er.usuario	as us ");
    	hql.append("Left Join er.motivoCancelamentoRim as mcr ");
    	
		return hql;
	}
	
	 public Integer getRowCountEventoRim(Long idReciboIndenizacao) {
		 StringBuffer hql = new StringBuffer(); 
	    
		 hql.append("select count(*) ");
		 hql.append(addJoinsTofindEventoRim());
		 hql.append("Where ri.idReciboIndenizacao = ?");
		 
		 List param = new ArrayList();
		 param.add(idReciboIndenizacao);
		 
		 Long result = (Long) this.getAdsmHibernateTemplate().findUniqueResult(hql.toString(), param.toArray());
			
		 return result.intValue();
	 }
	 
	 public List findItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
		 StringBuffer hql = new StringBuffer(); 
			
		 hql.append(addJoinsTofindEventoRim(true));
		 hql.append(" where ri.idReciboIndenizacao = ?");
		 hql.append("order by er.dhEventoRim");
			
		 return getAdsmHibernateTemplate().find(hql.toString(), idReciboIndenizacao);
	 }

	 public Integer getRowCountItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
		 StringBuffer hql = new StringBuffer(); 
			
		 hql.append(addJoinsTofindEventoRim(false));
		 hql.append(" where ri.idReciboIndenizacao = ?");
			
		 return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[] {idReciboIndenizacao});
	 }

	private String addJoinsTofindEventoRim(boolean isFetch) {
		String fetch = isFetch ? "fetch" : "";    	
		StringBuffer hql = new StringBuffer();	
			
		hql.append("From " + EventoRim.class.getName() +" as er ");
	    hql.append("Join " + fetch + " er.reciboIndenizacao as ri ");
	    hql.append("Join " + fetch + " er.filial as fi ");
	    hql.append("Join " + fetch + " er.usuario	as us ");
	    hql.append("Left Join " + fetch + " er.motivoCancelamentoRim as mcr ");
	    	
		return hql.toString();		
	}

}