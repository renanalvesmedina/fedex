package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.RegiaoGeografica;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegiaoGeograficaDAO extends BaseCrudDao<RegiaoGeografica, Long>
{
    
	//ordena a combo de regioes geograficas
	public List findListByCriteria(Map criterions) {
		List lista = new ArrayList();
		lista.add("dsRegiaoGeografica");
		// TODO Auto-generated method stub
		return super.findListByCriteria(criterions,lista);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RegiaoGeografica.class;
    }

	public List checkRegiaoCentroOesteNorteByIdFilial(UnidadeFederativa uf) {
		String hql = "select rg from "+ getPersistentClass().getName()+" rg, " +
				"	UnidadeFederativa uf " +
					"where uf.regiaoGeografica = rg " +
					"and uf = :uf " +
					"and rg.id in (:regioes)";
		String[] paramNames = new String[]{"uf","regioes"};
		Object[] paramValues =  new Object[]{uf, new Long[]{1L,4L}};
   
		return getAdsmHibernateTemplate().findByNamedParam(hql, paramNames, paramValues);
	}

    @SuppressWarnings("unchecked")
    public List<RegiaoGeografica> findByPais(Pais pais) {
       String hql = "select distinct  r from UnidadeFederativa uf "
               + "join uf.regiaoGeografica r  where uf.pais = :pais order by r.dsRegiaoGeografica"; 
        return getAdsmHibernateTemplate().findByNamedParam(hql, "pais", pais);
    }

}