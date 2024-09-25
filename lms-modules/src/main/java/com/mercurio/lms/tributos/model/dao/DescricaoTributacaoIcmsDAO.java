package com.mercurio.lms.tributos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.model.DescricaoTributacaoIcms;
import com.mercurio.lms.tributos.model.ObservacaoICMS;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DescricaoTributacaoIcmsDAO extends BaseCrudDao<DescricaoTributacaoIcms, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DescricaoTributacaoIcms.class;
    }

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("unidadeFederativa",FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativa",FetchMode.JOIN);
		lazyFindById.put("tipoTributacaoIcms",FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	/**
	 * Retorno o número de registros onde a vigência inicial ou final entra
	 * en conflito com um outro registro com a mesma unidade federativa.
	 * 
	 * @param DescricaoTributacaoIcms dti
	 * @return Integer número de registro
	 * 
	 * */
	public Integer findDescricaoTributacaoIcmsVigente(DescricaoTributacaoIcms dti){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("COUNT(dti)");
		sql.addFrom(DescricaoTributacaoIcms.class.getName() + " dti");
		sql.addCriteria("dti.idDescricaoTributacaoIcms","<>",dti.getIdDescricaoTributacaoIcms());
		sql.addCriteria("dti.unidadeFederativa.id","=",dti.getUnidadeFederativa().getIdUnidadeFederativa());
		sql.addCriteria("dti.tipoTributacaoIcms.id","=",dti.getTipoTributacaoIcms().getIdTipoTributacaoIcms());
		
		sql.addCustomCriteria("((dti.dtVigenciaInicial <= ? and " +
				              "  dti.dtVigenciaFinal   >= ? ) or  " +
				              " (dti.dtVigenciaInicial between ? and ? ))");
		
		Long result = (Long)this.getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria()); 
		return result.intValue();
	}
	
    public DescricaoTributacaoIcms store(DescricaoTributacaoIcms descricaoTributacaoIcms, ItemList items) {
        super.store(descricaoTributacaoIcms);
		removeItem(items.getRemovedItems());
		storeItem(items.getNewOrModifiedItems());    
        return descricaoTributacaoIcms;
    }
    
	public void storeItem(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}
	
	public void removeItem(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
		getAdsmHibernateTemplate().flush();
	}
	
	public void removeById(Long id) {
		super.removeById(id, true);
	}
	
	public int removeByIds(List ids) {
		return super.removeByIds(ids, true);
	}
	
	public List findItem(Long idDescricaoTributacaoIcms) {
		return super.findByDetachedCriteria(getDetachedCriteria(idDescricaoTributacaoIcms));
	}
	
	public Integer getRowCountItem(Long idDescricaoTributacaoIcms) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idDescricaoTributacaoIcms)
				.setProjection(Projections.rowCount()));
	}	
	
	private DetachedCriteria getDetachedCriteria(Long idDescricaoTributacaoIcms) {
		return DetachedCriteria.forClass(ObservacaoICMS.class)
		.add(Restrictions.eq("descricaoTributacaoIcms.id", idDescricaoTributacaoIcms));
	}	
}