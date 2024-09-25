package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.ServicoTributo;
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
public class ServicoTributoDAO extends BaseCrudDao<ServicoTributo, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoTributo.class;
    }

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("servicoOficialTributo", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("servicoOficialTributo", FetchMode.JOIN);
	}

	public boolean verificaExisteVigencia(ServicoTributo bean) {
		StringBuffer hql = new StringBuffer();
		ArrayList values = new ArrayList();

		hql.append("select count(*) ");
		hql.append("from " + ServicoTributo.class.getName() + " ai ");

		hql.append("where ai.servicoOficialTributo.id = ? ");
		values.add(bean.getServicoOficialTributo().getIdServicoOficialTributo());
		
		hql.append("and lower(trim(ai.dsServicoTributo)) = ? ");
		values.add(bean.getDsServicoTributo().trim().toLowerCase());
		
		if (bean.getIdServicoTributo() != null) {
			hql.append("and ai.id != ? ");
			values.add(bean.getIdServicoTributo());
		}

		if (bean.getDtVigenciaFinal() != null) {
			values.add(bean.getDtVigenciaInicial());
			values.add(bean.getDtVigenciaFinal());

			hql.append("and not((ai.dtVigenciaFinal < ? "
					+ "and ai.dtVigenciaFinal is not null) "
					+ "or ai.dtVigenciaInicial > ?) ");
		} else {
			values.add(bean.getDtVigenciaInicial());
			hql.append("and not(ai.dtVigenciaFinal < ? and ai.dtVigenciaFinal is not null) ");
		}
		List result = this.getAdsmHibernateTemplate().find(hql.toString(), values.toArray());
		return ((Long) result.get(0)).intValue() > 0;
	}

    /**
     * Método utilizado para ordenação do combo de Serviços Tributos (outros serviços)
     */
    public List findListByCriteria(Map criterions) {
        
        ArrayList order = new ArrayList();        
        order.add("dsServicoTributo");
        
        return super.findListByCriteria(criterions, order);
    }

    /**
     * Busca os Serviços Tributos Ativos, ou seja, com a vigência ativa
     * @param criterios Critérios de pesquisa
     * @return Lista contendo os Serviços Tributos retornados da pesquisa
     */
    public List findServicosTributosAtivos(TypedFlatMap criterios) {
        
        DetachedCriteria dc = DetachedCriteria.forClass(ServicoTributo.class);
        
        if( criterios != null && criterios.getLong("servicoTributo.idServicoTributo") != null ){
            dc.add(Restrictions.ne("idServicoTributo",criterios.getLong("servicoTributo.idServicoTributo")));
        }
        
        JTVigenciaUtils.getDetachedVigencia(dc,JTDateTimeUtils.getDataAtual(),JTDateTimeUtils.getDataAtual());
        
        dc.addOrder(Order.asc("dsServicoTributo"));
        
        return findByDetachedCriteria(dc);        
 
    }
    
    /**
     * Busca os Serviços Tributos Ativos e Futuros, ou seja, vigente ou vão ficar vigente no futuro
     * @param criterios Critérios de pesquisa
     * @return Lista contendo os Serviços Tributos retornados da pesquisa
     */
    public List findServicosTributosAtivosFuturos(TypedFlatMap tfm) {
        
        DetachedCriteria dc = DetachedCriteria.forClass(ServicoTributo.class);
        
        if( tfm != null && tfm.getLong("servicoTributo.idServicoTributo") != null ){
            dc.add(Restrictions.ne("idServicoTributo",tfm.getLong("servicoTributo.idServicoTributo")));
        }            
        
        dc.add(Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
        
        dc.addOrder(Order.asc("dsServicoTributo"));
        
        return findByDetachedCriteria(dc);        
 
    }

    /**
     * Busca o id_servico_oficial_tributo relacionado com o servico tributo
     * vigente conforme a data Base
     * @param idServicoTributo Identificador do Serviço Tributo
     * @param dtBase Data Base para teste de vigência
     * @return Identificador do Serviço Oficial Tributo relacionado ao Serviço Tributo
     */
    public Long findIdentificadorServicoOficialTributo(Long idServicoTributo, YearMonthDay dtBase) {
        
        DetachedCriteria dc = createDetachedCriteria();
        
        dc.setProjection(Projections.property("servicoOficialTributo.idServicoOficialTributo"));
        
        dc.add(Restrictions.eq("idServicoTributo",idServicoTributo));
        
        dc = JTVigenciaUtils.getDetachedVigencia(dc,
	                                               dtBase, 
	                                               dtBase); 
                
        List retorno = findByDetachedCriteria(dc);
        
        if( !retorno.isEmpty() ){
            return (Long) retorno.get(0);
        }         
        
        return null;   
        
    }    
    
    
    
    
    /**
     * Retorno uma lista para a montagem da combo de servico tributo
     * 
     * @author Diego Umpierre
     * @since 07/07/2006
     * 
     * @return List
     * */
     public List findCombo() {
     	
     	SqlTemplate sql = new SqlTemplate();
     	
     	sql.addProjection(" st.idServicoTributo, st.dsServicoTributo,st.dtVigenciaInicial, st.dtVigenciaFinal ");
     	sql.addFrom( getPersistentClass().getName() + " st");
     	sql.addOrderBy( "st.dsServicoTributo" );
     	
     	
     	return getHibernateTemplate().find(	sql.getSql() );
     }
     
     
    
   
}