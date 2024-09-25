package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaFreteCarreteiro;
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
public class ReferenciaFreteCarreteiroDAO extends BaseCrudDao<ReferenciaFreteCarreteiro, Long>
{

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("unidadeFederativaByIdUnidadeDestino", FetchMode.JOIN);
		fetchModes.put("unidadeFederativaByIdUnidadeFederativaOrigem", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialOrigem", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialDestino", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		fetchModes.put("moedaPais", FetchMode.JOIN);
		fetchModes.put("moedaPais.moeda", FetchMode.JOIN);
		
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("unidadeFederativaByIdUnidadeDestino", FetchMode.SELECT);
		fetchModes.put("unidadeFederativaByIdUnidadeFederativaOrigem", FetchMode.SELECT);
		fetchModes.put("filialByIdFilialOrigem", FetchMode.SELECT);
		fetchModes.put("filialByIdFilialDestino", FetchMode.SELECT);
		fetchModes.put("moedaPais", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReferenciaFreteCarreteiro.class;
    }
    
    public boolean findReferenciaFreteCarreteiroVigente(ReferenciaFreteCarreteiro rfc){
    	DetachedCriteria dc = createDetachedCriteria();
    	if(rfc.getIdReferenciaFreteCarreteiro()!= null)
    		dc.add(Restrictions.ne("idReferenciaFreteCarreteiro",rfc.getIdReferenciaFreteCarreteiro()));
    	
    	if(rfc.getUnidadeFederativaByIdUnidadeFederativaOrigem()!= null)
    		dc.add(Restrictions.eq("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa",rfc.getUnidadeFederativaByIdUnidadeFederativaOrigem().getIdUnidadeFederativa()));
    	else
    		dc.add(Restrictions.isNull("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa"));
    	if(rfc.getUnidadeFederativaByIdUnidadeDestino()!= null)
    		dc.add(Restrictions.eq("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa",rfc.getUnidadeFederativaByIdUnidadeDestino().getIdUnidadeFederativa()));
    	else 
    		dc.add(Restrictions.isNull("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa"));
    	if(rfc.getFilialByIdFilialOrigem() != null)
    		dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial",rfc.getFilialByIdFilialOrigem().getIdFilial()));
    	else
    		dc.add(Restrictions.isNull("filialByIdFilialOrigem.idFilial"));
    	if(rfc.getFilialByIdFilialDestino()!= null)
    		dc.add(Restrictions.eq("filialByIdFilialDestino.idFilial",rfc.getFilialByIdFilialDestino().getIdFilial()));
    	else
    		dc.add(Restrictions.isNull("filialByIdFilialDestino.idFilial"));
    	
    	dc = JTVigenciaUtils.getDetachedVigencia(dc,rfc.getDtVigenciaInicial(),rfc.getDtVigenciaFinal());
    	    	
    	return findByDetachedCriteria(dc).size()>0;
    	
    }
    
    /**
     * Implementações referentes a DF02
     */
	public void removeById(Long id) {
		ReferenciaFreteCarreteiro bean = (ReferenciaFreteCarreteiro)findById(id);
		bean.getReferenciaTipoVeiculos().clear();
		getAdsmHibernateTemplate().delete(bean);
	}
    
    public int removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			removeById(id);
		}
		return ids.size();
	}
	
    public ReferenciaFreteCarreteiro store(ReferenciaFreteCarreteiro rfc, ItemList items) {
        super.store(rfc);
    	removeReferenciaTipoVeiculo(items.getRemovedItems());
    	storeReferenciaTipoVeiculo(items.getNewOrModifiedItems());
    	getAdsmHibernateTemplate().flush();
        return rfc;
    }


	private void storeReferenciaTipoVeiculo(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}

	private void removeReferenciaTipoVeiculo(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}
	
	public List  findReferenciaFreteCarreteiroVigente(Long idUfOrigem, Long idUfDestino, Long idFilialOrigem, Long idFilialDestino, 
			YearMonthDay dataInicio, YearMonthDay dataFim){
		DetachedCriteria dc = createDetachedCriteria();
		if(idUfOrigem != null)
			dc.add(Restrictions.eq("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa",idUfOrigem));
		else
			dc.add(Restrictions.isNull("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa"));
		
		if(idUfDestino != null)
			dc.add(Restrictions.eq("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa",idUfDestino));
		else
			dc.add(Restrictions.isNull("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa"));
		
		if(idFilialOrigem != null)
			dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial",idFilialOrigem));
		else
			dc.add(Restrictions.isNull("filialByIdFilialOrigem.idFilial"));
		
		if(idFilialDestino != null)
			dc.add(Restrictions.eq("filialByIdFilialDestino.idFilial",idFilialDestino));
		else
			dc.add(Restrictions.isNull("filialByIdFilialDestino.idFilial"));
		
		dc= JTVigenciaUtils.getDetachedVigencia(dc,dataInicio,dataFim);
		return findByDetachedCriteria(dc);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate hql = montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}
	
	public SqlTemplate montaQueryPaginated(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		StringBuffer projecao = new StringBuffer()
			.append("new Map(rfc.idReferenciaFreteCarreteiro as idReferenciaFreteCarreteiro, ")
			.append("nvl2(ufDestino.sgUnidadeFederativa,ufDestino.sgUnidadeFederativa|| '-' ||ufDestino.nmUnidadeFederativa, '')as sgUnidadeFederativaDestino, ")
			.append("nvl2(ufOrigem.sgUnidadeFederativa, ufOrigem.sgUnidadeFederativa|| '-' ||ufOrigem.nmUnidadeFederativa, '') as sgUnidadeFederativaOrigem, ")
			.append("fiOrigem.sgFilial as sgFilialOrigem, ")
			.append("pesOrigem.nmFantasia as nomeFilialOrigem, ")
			.append("fiDestino.sgFilial as sgFilialDestino, ")
			.append("pesDestino.nmFantasia as nomeFilialDestino, ")
			.append("rfc.dtVigenciaInicial as dtVigenciaInicial, ")
			.append("rfc.dtVigenciaFinal as dtVigenciaFinal)");
		
		hql.addProjection(projecao.toString());
		
		
		hql.addFrom(ReferenciaFreteCarreteiro.class.getName(), 
				new StringBuffer("rfc ")
				.append("left outer join rfc.unidadeFederativaByIdUnidadeDestino ufDestino ")
				.append("left outer join rfc.unidadeFederativaByIdUnidadeFederativaOrigem ufOrigem ")
				.append("left outer join rfc.filialByIdFilialOrigem fiOrigem ")
				.append("left outer join fiOrigem.pessoa pesOrigem ")
				.append("left outer join rfc.filialByIdFilialDestino fiDestino ")
				.append("left outer join fiDestino.pessoa pesDestino ")
				.toString()
				);
		
		hql.addCriteria("ufDestino.idUnidadeFederativa","=",criteria.getLong("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa"));
		hql.addCriteria("ufOrigem.idUnidadeFederativa","=",criteria.getLong("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa"));
		hql.addCriteria("fiOrigem.idFilial","=",criteria.getLong("filialByIdFilialOrigem.idFilial"));
		hql.addCriteria("fiDestino.idFilial","=",criteria.getLong("filialByIdFilialDestino.idFilial"));
		hql.addCriteria("rfc.dtVigenciaInicial",">=",criteria.getYearMonthDay("dtVigenciaInicial"));
		hql.addCriteria("rfc.dtVigenciaFinal","<=",criteria.getYearMonthDay("dtVigenciaFinal"));
		
		hql.addOrderBy("ufOrigem.sgUnidadeFederativa");
		hql.addOrderBy("fiOrigem.sgFilial");
		hql.addOrderBy("ufDestino.sgUnidadeFederativa");
		hql.addOrderBy("fiDestino.sgFilial");
		hql.addOrderBy("rfc.dtVigenciaInicial");
		return hql;
	}
	
	
	public ReferenciaFreteCarreteiro findReferenciaFreteCarreteiro(Long idUfOrigem, Long idUfDestino, Long idFilialOrigem, Long idFilialDestino, Long idMoeda){
		DetachedCriteria dc = createDetachedCriteria();
		
		if(idUfOrigem != null)
			dc.add(Restrictions.eq("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa", idUfOrigem));
		else dc.add(Restrictions.isNull("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa"));
		
		if(idUfDestino != null)
			dc.add(Restrictions.eq("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa",idUfDestino));
		else dc.add(Restrictions.isNull("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa"));
		
		if(idFilialOrigem != null)
			dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial",idFilialOrigem));
		else dc.add(Restrictions.isNull("filialByIdFilialOrigem.idFilial"));
		
		if (idFilialDestino != null)
			dc.add(Restrictions.eq("filialByIdFilialDestino.idFilial",idFilialDestino));
		else dc.add(Restrictions.isNull("filialByIdFilialDestino.idFilial"));
		
		dc.add(Restrictions.eq("moedaPais.idMoedaPais", idMoeda));
		
		dc.add(Restrictions.le("dtVigenciaInicial", JTDateTimeUtils.getDataAtual()));
		
		dc.add(Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		List rs = findByDetachedCriteria(dc);		
		return (rs.size() > 0) ? (ReferenciaFreteCarreteiro)findByDetachedCriteria(dc).get(0) : null;
	}
	
	

	
	
	
	
	
	
	
	

	
	
		
}