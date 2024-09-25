package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
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
public class ServicoAdicionalDAO extends BaseCrudDao<ServicoAdicional, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ServicoAdicional.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
	   lazyFindById.put("servicoOficialTributo",FetchMode.JOIN);	
	}

    
    
    /**
    * Retorno uma lista para a montagem da combo de servicoAdicional
    * 
    * @author Diego Umpierre
    * @since 07/07/2006
    * 
    * @return List
    * */
    public List findCombo() {
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(" sa.idServicoAdicional, sa.dsServicoAdicional,sa.dtVigenciaInicial, sa.dtVigenciaFinal ");
    	sql.addFrom( getPersistentClass().getName() + " sa");
    	sql.addOrderBy(OrderVarcharI18n.hqlOrder("sa.dsServicoAdicional", LocaleContextHolder.getLocale()));
    	
    	
    	return getHibernateTemplate().find(	sql.getSql() );
    }
    
    
	/**
     * Retorna uma lista contendo os Serviços Adicionais cuja situação esteja ativa, 
     * ou seja, Data de Vigência Inicial é menor ou igual a Data atual e a Data de Vigência Final é nula ou 
     * é maior ou igual a data atual.
     * Se nos critérios constar um idServicoAdicional este registro deve retornar juntamente
     * com a lista de serviços adicionais ativos, mesmo que este esteja inativo.
     * @return List Lista de serviços adicionais ativos
     */
    public List findServicosAdicionaisAtivos(Map criterios){
    	
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	YearMonthDay ymdAtual = JTDateTimeUtils.getDataAtual();
    	
    	if( criterios != null && criterios.get("idServicoAdicional") != null ){    	
    		dc.add(Restrictions.or(
	    			Restrictions.and(Restrictions.le("dtVigenciaInicial",ymdAtual),
	    							 Restrictions.ge("dtVigenciaFinal", ymdAtual)	         
	    						    ),
    		    	Restrictions.eq("idServicoAdicional",Long.valueOf((String)criterios.get("idServicoAdicional")))
    	    	));
    		
    	} else {    	
    		dc.add(Restrictions.and(Restrictions.le("dtVigenciaInicial",ymdAtual),
    								Restrictions.ge("dtVigenciaFinal", ymdAtual)
	    		));	
    	}
    	
    	dc.addOrder(OrderVarcharI18n.asc("dsServicoAdicional", LocaleContextHolder.getLocale()));
		
    	return findByDetachedCriteria(dc);
		
    }
    
	/**
     * Retorna uma lista contendo os Serviços Adicionais vigente ou que vão ser vigente no futuro.
     * @return List Lista de serviços adicionais ativos
     */
    public List findServicosAdicionaisAtivosFuturos(Map criterios){
    	
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	YearMonthDay ymdAtual = JTDateTimeUtils.getDataAtual();
    	
    	if( criterios != null && criterios.get("idServicoAdicional") != null ){
    		dc.add(Restrictions.eq("idServicoAdicional",Long.valueOf((String)criterios.get("idServicoAdicional"))));
    	}
    	
        dc.add(Restrictions.ge("dtVigenciaFinal", ymdAtual));
        
    	
    	dc.addOrder(OrderVarcharI18n.asc("dsServicoAdicional", LocaleContextHolder.getLocale()));
		
    	return findByDetachedCriteria(dc);
		
    }    
    
    /**
     * Ordena a lista de serviços adicionais pela descricao do servico adicional
     * @param criterions Filtros de pesquisa
     * @return List Lista contendo os serviços adicionais encontrados e ordenados pela descrição do serviço. 
     */
    public List findListByCriteria(Map criterions) {
    	
    	List order = new ArrayList();
    	
    	order.add("dsServicoAdicional");
    	
		return super.findListByCriteria(criterions,order);    	
    	
    }
    
    /**
     * Retorna uma lista contendo os Serviços Adicionais vigente ou que vão ser vigente no futuro.
     * @return List Lista de serviços adicionais ativos
     */
    public List findServicosAdicionaisByIds(List listaIds) {
    	
    	ProjectionList proj = Projections.projectionList()
    	.add(Projections.property("s.idServicoAdicional"),"idServicoAdicional")
    	.add(Projections.property("s.dsServicoAdicional"),"dsServicoAdicional");
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"s")
    	.setProjection(proj)
    	.add(Restrictions.in("s.id",listaIds))
    	.addOrder(OrderVarcharI18n.asc("s.dsServicoAdicional", LocaleContextHolder.getLocale()))
    	.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		
    	return findByDetachedCriteria(dc);
		
    }

    @SuppressWarnings("unchecked")
	public List<TabelaPrecoParcela> findServicosAdicionaisByTabelaPreco(Long idTabelaPreco) {    	
    	StringBuilder query = new StringBuilder();
		
		query.append("from " + TabelaPrecoParcela.class.getName() + " as tpp ");
		query.append("inner join fetch tpp.tabelaPreco tp ");
		query.append("inner join fetch tpp.parcelaPreco pp ");		
		query.append("inner join fetch tpp.valorServicoAdicional vsa ");
		query.append("left join fetch tpp.generalidade g ");
		query.append("left join fetch tpp.valorTaxa vt ");
		query.append("inner join fetch tp.moeda m ");
		query.append("where ");
		query.append(" 		tp.idTabelaPreco = :idTabelaPreco ");
		query.append(" and 	pp.tpParcelaPreco = :tpParcelaPreco ");
		query.append(" and 	pp.tpPrecificacao = :tpPrecificacao ");
		
		
		TypedFlatMap bindValues = new TypedFlatMap();
		bindValues.put("idTabelaPreco", idTabelaPreco);
		bindValues.put("tpParcelaPreco", "S");
		bindValues.put("tpPrecificacao", "S");		
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), bindValues);
			}

    /**
     * Busca o id_servico_oficial_tributo relacionado com o servico adicional
     * vigente conforme a data Base
     * @param idServicoAdicional Identificador do Serviço Adicional
     * @param dtBase Data Base para teste de vigência
     * @return Identificador do Serviço Oficial Tributo relacionado ao Serviço Adicional
     */
    public Long findIdentificadorServicoOficialTributo(Long idServicoAdicional, YearMonthDay dtBase) {
        
        DetachedCriteria dc = createDetachedCriteria();
        
        dc.setProjection(Projections.property("servicoOficialTributo.idServicoOficialTributo"));
        
        dc.add(Restrictions.eq("idServicoAdicional",idServicoAdicional));
        
        dc = JTVigenciaUtils.getDetachedVigencia(dc, dtBase, dtBase);
           
        List retorno = findByDetachedCriteria(dc);
        
        if( !retorno.isEmpty() ){
            return (Long) retorno.get(0);
        }         
        
        return null;  
        
    }
    
    
}