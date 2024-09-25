package com.mercurio.lms.carregamento.model.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.Equipe;
import com.mercurio.lms.carregamento.model.IntegranteEquipe;
import com.mercurio.lms.municipios.model.Filial;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EquipeDAO extends BaseCrudDao<Equipe, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Equipe.class;
    }

	protected void initFindByIdLazyProperties(Map map) {
		map.put("filial", FetchMode.JOIN);
		map.put("filial.pessoa", FetchMode.JOIN);
		map.put("setor", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("filial", FetchMode.JOIN);
		map.put("setor", FetchMode.JOIN);
	}

	private  void storeIntegranteEquipe(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}

	private  void removeIntegranteEquipe(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}

   public Equipe store(Equipe m, ItemList items) {
       super.store(m);
       getAdsmHibernateTemplate().flush(); 
	   removeIntegranteEquipe(items.getRemovedItems());
	   storeIntegranteEquipe(items.getNewOrModifiedItems());
	   getAdsmHibernateTemplate().flush(); 
	   return m;
   }
	
   /**
    * Se o <code>motivoNcId</code> for null retorna <code>Collections.EMPTY_LIST</code>.
    * @param motivoNcId
    * @return
    */
	public List findIntegranteEquipeByEquipeId(Long motivoNcId) {
	   if (motivoNcId == null) {
		   return Collections.EMPTY_LIST;
	   }
	   return getAdsmHibernateTemplate().find("from "+IntegranteEquipe.class.getName()+ " as IE " +			   
			   		"left join fetch IE.pessoa as PE " +
			   		"left join fetch IE.cargoOperacional as CA " +
			   		"left join fetch IE.usuario as US " +
			   		"left join fetch IE.usuario.vfuncionario as FS " +
			   		"left join fetch IE.empresa as EM " +
			   		"left join fetch IE.empresa.pessoa as EMP " +
			   		" where IE.equipe.id = ? order by IE.tpIntegrante ", motivoNcId);
	}
	
	
	public Equipe findByDsEquipeFilial(String dsEquipe, Filial filial){
		
		String hql = "select e from Equipe e where e.dsEquipe = :dsEquipe and e.filial = :filial";
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("filial", filial);
		param.put("dsEquipe", dsEquipe);
		
		return (Equipe) getAdsmHibernateTemplate().findUniqueResult(hql, param);
		
	}
	
	public List findIntegrantesEmEquipes(Long idIntegranteEquipe, Boolean isTerceiro, List ids) {
		DetachedCriteria dc = DetachedCriteria.forClass(IntegranteEquipe.class, "ie");
		dc.createAlias("equipe", "equipe");
		if (isTerceiro.booleanValue()) { 
			dc.createAlias("pessoa", "pessoa")
        	.add(Restrictions.in("pessoa.id", ids));
		}
		else {
			dc.createAlias("usuario", "usuario")
        	.add(Restrictions.in("usuario.id", ids));
		}
		if (idIntegranteEquipe != null) {
			dc.add(Restrictions.ne("id", idIntegranteEquipe));
		}
		dc.add(Restrictions.eq("equipe.tpSituacao", "A"));
		List result = super.findByDetachedCriteria(dc);
        return result;
	}

	public Equipe findByDescricao(String descricao) {
	DetachedCriteria dc = DetachedCriteria.forClass(Equipe.class, "e");
	dc.add(Restrictions.eq("e.dsEquipe", descricao).ignoreCase());
	List result = findByDetachedCriteria(dc);
	if (result.isEmpty()){
		return null;
	}
	return (Equipe)result.get(0);
}
}