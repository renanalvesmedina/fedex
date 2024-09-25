package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.PostoAvancado;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PostoAvancadoDAO extends BaseCrudDao<PostoAvancado, Long>
{

	private Logger log = LogManager.getLogger(this.getClass());
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PostoAvancado.class;
    }
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("cliente",FetchMode.JOIN);
    	lazyFindPaginated.put("cliente.pessoa",FetchMode.JOIN);
    	lazyFindPaginated.put("filial",FetchMode.JOIN);
    	lazyFindPaginated.put("filial.pessoa",FetchMode.JOIN);
    	lazyFindPaginated.put("usuario",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(lazyFindPaginated);
    }
    protected void initFindByIdLazyProperties(Map lazy) {
    	lazy.put("cliente",FetchMode.JOIN);
    	lazy.put("cliente.pessoa",FetchMode.JOIN);
    	lazy.put("filial",FetchMode.JOIN);
    	lazy.put("filial.pessoa",FetchMode.JOIN);
    	lazy.put("usuario",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazy);
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	Object[] obj = createHqlPaginated(criteria);
    	return getAdsmHibernateTemplate().findPaginated((String)obj[0],findDef.getCurrentPage(),findDef.getPageSize(),(Object[])obj[1]);
    }
    public Integer getRowCount(TypedFlatMap criteria) {
    	return super.getRowCount(criteria);
    }
    
    public List findPostoAvancadoByFilial(Long idFilial,YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("filial.idFilial",idFilial));
    	dc.add(Restrictions.le("dtVigenciaInicial",JTDateTimeUtils.getDataAtual()));
    	dc.add(Restrictions.or(Restrictions.isNull("dtVigenciaFinal"),Restrictions.gt("dtVigenciaFinal",dtVigenciaFinal)));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
   
    private Object[] createHqlPaginated(TypedFlatMap criteria) {
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(PostoAvancado.class.getName() + " as PA " +
    					"inner join fetch PA.filial  as FI " +
    					"left  join fetch PA.cliente as CL " +
    					"left  join fetch CL.pessoa  as CL_PESS " +
    					"left  join fetch PA.usuario as US ");
    	try {
    		sql.addCriteria("upper(PA.dsPostoAvancado)","like",criteria.getString("dsPostoAvancado").toUpperCase());
    		sql.addCriteria("PA.dtVigenciaInicial",">=",criteria.getYearMonthDay("dtVigenciaInicial"));
    		sql.addCriteria("PA.dtVigenciaFinal","<=",criteria.getYearMonthDay("dtVigenciaFinal"));
    		sql.addCriteria("FI.idFilial","=",criteria.getLong("filial.idFilial"));
    		sql.addCriteria("CL.idCliente","=",criteria.getLong("cliente.idCliente"));
    		sql.addCriteria("US.idUsuario","=",criteria.getLong("usuario.idUsuario"));
    		
	    	sql.addOrderBy("FI.sgFilial,CL_PESS.nmPessoa,PA.dsPostoAvancado");
    	}catch (Exception e) {
			log.error(e);
		}
		return new Object[]{sql.getSql(),sql.getCriteria()};
	}
	
    public boolean verificaVigenciaPostoAvancado(PostoAvancado bean){ 
    	DetachedCriteria dc = this.createDetachedCriteria();
    	
    	if (bean.getIdPostoAvancado() != null)
    		dc.add(Restrictions.ne("idPostoAvancado", bean.getIdPostoAvancado()));
    	    	
    	dc.add(Restrictions.ilike("dsPostoAvancado", bean.getDsPostoAvancado().toLowerCase()));
    	dc.add(Restrictions.eq("filial.idFilial", bean.getFilial().getIdFilial()));
    	
    	JTVigenciaUtils.getDetachedVigencia(dc, bean.getDtVigenciaInicial(), bean.getDtVigenciaFinal());
    	dc.setProjection( Projections.rowCount());
    	
    	List result = this.getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	
    	Integer count = (Integer) result.get(0);
    	
    	return count.intValue() > 0;
    }
    
}
