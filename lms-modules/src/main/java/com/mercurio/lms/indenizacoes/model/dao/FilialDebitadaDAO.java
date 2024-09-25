package com.mercurio.lms.indenizacoes.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialDebitadaDAO extends BaseCrudDao<FilialDebitada, Long>
{	
	
	protected void initFindByIdLazyProperties(Map lazy) {
		lazy.put("moeda", FetchMode.JOIN);
		lazy.put("filial", FetchMode.JOIN);
		lazy.put("filial.pessoa", FetchMode.JOIN);
		lazy.put("filialReembolso", FetchMode.JOIN);
		lazy.put("filialReembolso.pessoa", FetchMode.JOIN);		
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FilialDebitada.class;
    }
    
    private SqlTemplate compileQueryFilialDebitadaReciboIndenizacao(TypedFlatMap tfm) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(FilialDebitada.class.getName(), "fd join fetch fd.filial fil join fetch fil.pessoa left join fetch fd.moeda");
    	sql.addCriteria("fd.reciboIndenizacao.id", "=", tfm.getLong("idReciboIndenizacao"));
    	sql.addOrderBy("fd.filial.sgFilial");
    	return sql;
    }
    
    public ResultSetPage findPaginatedFilialDebitadaReciboIndenizacao(TypedFlatMap tfm, FindDefinition fd) {
    	SqlTemplate sql = compileQueryFilialDebitadaReciboIndenizacao(tfm);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), fd.getCurrentPage(), fd.getPageSize(), sql.getCriteria());
    }
    
    public Integer getRowCountFilialDebitadaReciboIndenizacao(TypedFlatMap tfm) {
    	SqlTemplate sql = compileQueryFilialDebitadaReciboIndenizacao(tfm);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
    } 

    /**
     * Remove as filiais debitadas do recibo de indenizacao
     * @param idReciboIndenizacao
     */
    public void removeByIdReciboIndenizacao(Long idReciboIndenizacao) {
		getAdsmHibernateTemplate().removeById("delete " + FilialDebitada.class.getName() +
											  " where id in (select fd.id from FilialDebitada fd " +
											  				" where fd.reciboIndenizacao.id = ?)", 
								new Object[]{idReciboIndenizacao});
    }
    
    /**
     * Consulta se já existe Filial Debitada para o idReciboIndenizacao, idFilial e idDoctoServicoIndenizacao.
     * Utilizado para validar a unique key (idReciboIndenizacao, idFilial e idDoctoServicoIndenizacao) antes da inserção.
     * @param idReciboIndenizacao
     * @param idFilial
     * @param idDoctoServicoIndenizacao
     * @return True se já existe um registro.
     */
    public FilialDebitada findFilialDebitadaByReciboIndenizacaoFilialDoctoServicoIndenizacao(Long idReciboIndenizacao, Long idFilial, Long idDoctoServicoIndenizacao) {
    	StringBuffer s = new StringBuffer()
    	.append("from "+FilialDebitada.class.getName()+" fd ")
    	.append("where fd.reciboIndenizacao.id = :idReciboIndenizacao ")
    	.append("and fd.filial.id = :idFilial ");

    	Map parameters = new HashMap();
    	parameters.put("idReciboIndenizacao", idReciboIndenizacao);
    	parameters.put("idFilial", idFilial);
    	
    	if (idDoctoServicoIndenizacao==null)  
    		s.append(" and fd.doctoServicoIndenizacao is null");
    	else {
    		s.append(" and fd.doctoServicoIndenizacao.id = :idDoctoServicoIndenizacao ");
    		parameters.put("idDoctoServicoIndenizacao", idDoctoServicoIndenizacao);
    	}

    	return (FilialDebitada) getAdsmHibernateTemplate().findUniqueResult(s.toString(), parameters);    	
    }

    /**
     * Obtém as filiais debitadas do recibo de indenizacao
     * @param idReciboIndenizacao
     * @return
     */
    public List<FilialDebitada> findByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "fd");
    	dc.setFetchMode("fd.filial", FetchMode.SELECT);
    	dc.add(Restrictions.eq("fd.reciboIndenizacao.id", idReciboIndenizacao));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    /**
     * Obtém a some do percentual debitado das filiais debitadas do recibo de indenizacao
     * @param idReciboIndenizacao
     * @return
     */
    public BigDecimal findSumPcDebitado(Long idReciboIndenizacao) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "fd");
    	dc.setProjection(Projections.sum("fd.pcDebitado"));
    	dc.add(Restrictions.eq("fd.reciboIndenizacao.id", idReciboIndenizacao));

    	BigDecimal result = (BigDecimal)getAdsmHibernateTemplate().findUniqueResult(dc);
		if (result == null) {
			result = BigDecimal.ZERO;
		}
		return result;
	}

}