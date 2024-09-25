package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.vendas.model.ProibidoEmbarque;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ProibidoEmbarqueDAO extends BaseCrudDao<ProibidoEmbarque, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ProibidoEmbarque.class;
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idCliente
     * @param dtBloqueio
     * @param dsBloqueio
     * @return <ProibidoEmbarque>
     */
    public ProibidoEmbarque findProibidoEmbarque(Long idCliente, YearMonthDay dtBloqueio, String dsBloqueio) {
    	DetachedCriteria dc = createDetachedCriteria()
    		.add(Restrictions.eq("cliente.id", idCliente))
    		.add(Restrictions.eq("dtBloqueio", dtBloqueio));
    		if (dsBloqueio != null && dsBloqueio.length()>0){
    			dc.add(Restrictions.eq("dsBloqueio", dsBloqueio));
    		}
    	return (ProibidoEmbarque) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    
    /**
     * Obtem o embarque através do id
	 * 
     * @param id
     * @return <ProibidoEmbarque>
     */
    public ProibidoEmbarque findProibidoEmbarque(Long id) {
    	DetachedCriteria dc = createDetachedCriteria()
    		.add(Restrictions.eq("id", id));
    	return (ProibidoEmbarque) getAdsmHibernateTemplate().findUniqueResult(dc);
    }    
    
    
    /**
     * Verifica se a data de desbloqueio está informada (not null) em algum dos ProibidoEmbarque da List.<BR>
     * @author Robson Edemar Gehl
     * @param ids List de IDs de ProibidoEmbarque
     * @return se a data não é NULL
     */
    public boolean validateDataDesbloqueioInformada(List ids){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.in("idProibidoEmbarque", ids));
    	dc.add(Restrictions.isNotNull("dtDesbloqueio"));
    	dc.setProjection(Projections.count("idProibidoEmbarque"));
    	Integer count = (Integer) findByDetachedCriteria(dc).get(0);
    	return ( count.intValue() == 0 );
    }
    
    /**
     * Verifica se existe ProibidoEmbarque sem data de desbloqueio pelo Cliente
     * @author Robson Edemar Gehl
     * @param cliente Cliente do ProibidoEmbarque
     * @return true se não existe ProibidoEmbarque com dtDesbloquio null; false se existe ProibidoEmbarque com dtDesbloqueio null
     */
    public boolean validateDataDesbloqueioByCliente(ProibidoEmbarque pe){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("cliente.id", pe.getCliente().getIdCliente()));
    	dc.add(Restrictions.isNull("dtDesbloqueio"));
    	if (pe.getIdProibidoEmbarque() != null){
    		dc.add(Restrictions.ne("idProibidoEmbarque", pe.getIdProibidoEmbarque()));	
    	}
    	dc.setProjection(Projections.count("idProibidoEmbarque"));
    	//quantidade de ProibidoEmbarque com dtDesbloqueio == null
    	Integer count = (Integer) findByDetachedCriteria(dc).get(0);
    	return !( count.intValue() > 0 );
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("cliente", FetchMode.JOIN);
    	lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
    	lazyFindById.put("usuarioByIdUsuarioDesbloqueio", FetchMode.JOIN);
    	lazyFindById.put("usuarioByIdUsuarioBloqueio", FetchMode.JOIN);
    	lazyFindById.put("motivoProibidoEmbarque", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("cliente", FetchMode.JOIN);
    	lazyFindPaginated.put("cliente.pessoa", FetchMode.JOIN);    	
    	lazyFindPaginated.put("usuarioByIdUsuarioDesbloqueio", FetchMode.JOIN);
    	lazyFindPaginated.put("usuarioByIdUsuarioBloqueio", FetchMode.JOIN);
    	lazyFindPaginated.put("motivoProibidoEmbarque", FetchMode.JOIN);
    }

	public Integer getRowCountProibidoByIdCliente(Long idCliente) {
		DetachedCriteria dc = createDetachedCriteria()
    		.setProjection(Projections.count("idProibidoEmbarque"))
			.add(Restrictions.eq("cliente.id", idCliente))
    		.add(Restrictions.isNull("dtDesbloqueio"));
    	return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	public Integer getRowCountProibidoBloqueioTributarioByIdCliente(Long idCliente){
		StringBuffer sql = new StringBuffer()
    	.append("select * from proibido_embarque p ") 
		.append("where  p.DT_DESBLOQUEIO is NULL   ")
		.append("and p.id_cliente = ? ")
		.append("and p.ID_MOTIVO_PROIBIDO_EMBARQUE IN    (SELECT regexp_substr( ")
		.append("                            (SELECT DS_CONTEUDO ")
		.append("                             FROM parametro_geral ")
		.append("                             WHERE nm_parametro_geral = 'ID_MOTIVO_TRIBUT_PROIB_EMB'),'[^;]+', 1, LEVEL) ")
		.append("     FROM dual CONNECT BY regexp_substr( ")
		.append("                                          (SELECT DS_CONTEUDO ")
		.append("                                           FROM parametro_geral ")
		.append("                                           WHERE nm_parametro_geral = 'ID_MOTIVO_TRIBUT_PROIB_EMB'), '[^;]+', 1, LEVEL) IS NOT NULL)");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{idCliente});
	}
	
	public ResultSetPage findPaginatedByIdCliente(Long idCliente, FindDefinition findDefinition) {
		DetachedCriteria dc = DetachedCriteria.forClass(ProibidoEmbarque.class);
		dc.add(Restrictions.eq("cliente.id", idCliente));
		dc.setFetchMode("motivoProibidoEmbarque", FetchMode.JOIN);
		
		return super.findPaginatedByDetachedCriteria(dc, findDefinition.getCurrentPage(), findDefinition.getPageSize());
	}
    

}