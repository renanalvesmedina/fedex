package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.RotaIdaVolta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialRotaDAO extends BaseCrudDao<FilialRota, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FilialRota.class;
    }
    
    protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial",FetchMode.JOIN);
	}
    
    protected void initFindPaginatedLazyProperties(Map fetchModes) {
    	fetchModes.put("filial",FetchMode.JOIN);
	}

    protected void initFindListLazyProperties(Map fetchModes) {
    	fetchModes.put("filial",FetchMode.JOIN);
    	fetchModes.put("filial.pessoa",FetchMode.JOIN);
    	super.initFindListLazyProperties(fetchModes);
    }

	public List findByIdRota(Long idRota){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setFetchMode("filial",FetchMode.JOIN);
    	dc.add(Restrictions.eq("rota.idRota",idRota));
    	dc.addOrder(Order.asc("nrOrdem"));
    	
    	return findByDetachedCriteria(dc);
    }
	
	//retorna os ids da filialRota onde existir a filialOrigem e a filialDestino
	public List findFilialRotaOrigemDestinoByRota(Long idRota){
		DetachedCriteria dc = createDetachedCriteria();
		
		dc.setProjection(Projections.property("idFilialRota"));
		dc.add(Restrictions.or(
			Restrictions.and(
				Restrictions.eq("rota.idRota",idRota),
				Restrictions.and(
						Restrictions.eq("blOrigemRota",Boolean.TRUE),
						Restrictions.eq("blDestinoRota",Boolean.FALSE))),
			Restrictions.and(			
				Restrictions.eq("rota.idRota",idRota),
				Restrictions.and(
						Restrictions.eq("blOrigemRota",Boolean.FALSE),
						Restrictions.eq("blDestinoRota",Boolean.TRUE)))));	
		dc.addOrder(Order.asc("idFilialRota"));
						
		return findByDetachedCriteria(dc);
	}

	public List<FilialRota> findByIdFilialOrigem(Long idFilialOrigem){
		DetachedCriteria dc = DetachedCriteria.forClass(FilialRota.class);		
		dc.setFetchMode("filial", FetchMode.JOIN);
		dc.setFetchMode("rota", FetchMode.JOIN);
		dc.add(Restrictions.eq("blOrigemRota", Boolean.TRUE)); 
		dc.add(Restrictions.eq("filial.idFilial", idFilialOrigem));
		return super.findByDetachedCriteria(dc); 						
	}
	
	public List findFiliaisRotaByRota(Long idRota){
		StringBuffer hql = new StringBuffer()
			.append(" select new Map(")
			.append("	F.idFilial as filial_idFilial, ")
			.append("	F.sgFilial as filial_sgFilial, ")
			.append("	P.nmFantasia as nmFilial ")
			.append(" ) ")		
			.append(" from " + FilialRota.class.getName() + " as FR ")
			.append(" inner join FR.filial as F ")
			.append(" inner join FR.rota as R ")
			.append(" inner join F.pessoa as P ")
			.append(" where R.idRota = ? ")
			.append(" order by FR.nrOrdem ");
		
		return getAdsmHibernateTemplate().find(hql.toString(),idRota);
	}


	/**
	 * 
	 * @param idRota
	 * @param idRotaIdaVolta
	 * @return
	 */
	public List findFiliaisRotaByRotaOrRotaIdaVolta(Long idRota, Long idRotaIdaVolta){
		StringBuffer hql = new StringBuffer()
			.append(" select new Map(")
			.append("filial.idFilial as filial_idFilial, ")
			.append("filial.sgFilial as filial_sgFilial, ")
			.append("fr.idFilialRota as idFilialRota, ")
			.append("fr.nrOrdem as nrOrdem) ")		
			.append("from ").append(FilialRota.class.getName()).append(" as fr ")
			.append("inner join fr.filial as filial ")
			.append("inner join fr.rota as rota ");

		List param = new ArrayList();

		if (idRota != null) {
			hql.append("where rota.idRota = ? ");
			param.add(idRota);
		}
		else
			if (idRotaIdaVolta != null) {
				hql.append("inner join rota.rotaIdaVoltas riv ");
				hql.append("where riv.idRotaIdaVolta = ? ");
				param.add(idRotaIdaVolta);
			}

		hql.append(" order by fr.nrOrdem ");
		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
	}

    /**
     * Busca as filiais de uma rota.
     * 
     * @param idRotaIdaVolta
     * @param idRota
     * @return 
     */
    public List findFiliaisRota(Long idRotaIdaVolta, Long idRota) {
    	StringBuffer hql = new StringBuffer();
    	List parameters = new ArrayList();
    	if (idRotaIdaVolta != null) {
	    	hql.append(" select new map(fr.filial as f) from ").append(RotaIdaVolta.class.getName()).append(" as riv ")
	    		.append("inner join riv.rota rota ")
	    		.append("inner join rota.filialRotas fr ")
	    		.append("inner join fr.filial filial ")
	    		.append("where riv.id = ? ");
    	
	    	parameters.add(idRotaIdaVolta);
    	}
    	else 
    	if (idRota != null) {
        	hql.append(" select new map(fr.filial as f) from ").append(FilialRota.class.getName()).append(" as fr ")
	    		.append("inner join fr.filial filial ")
	    		.append("where fr.rota.id = ? ");
        	
        	parameters.add(idRota);
    	}
    	hql.append("order by fr.nrOrdem ");

    	List result = getAdsmHibernateTemplate().find(hql.toString(), parameters.toArray());
    	List retorno = new ArrayList();
    	for (Iterator iter = result.iterator(); iter.hasNext();) {
    		Filial f = (Filial)((HashMap)iter.next()).get("f");
    		retorno.add(f);
    	}
    	return retorno;
    }



	public FilialRota findFilialRotaOrigemByRota(Long idRota) {
		 DetachedCriteria dc = DetachedCriteria.forClass(FilialRota.class);
		dc.add(Restrictions.eq("blOrigemRota",Boolean.TRUE));
		dc.add(Restrictions.eq("rota.idRota",idRota));
		
		return (FilialRota) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public Boolean existsFilialRotaByIdControleCarga(Long idControleCarga, Long idFilial) {
		StringBuffer sql = new StringBuffer(); 
		sql.append("select 1 from filial_rota fr ")
		.append("inner join controle_carga cc on cc.id_rota = fr.id_rota ")
		.append("where cc.id_controle_carga = ? ")
		.append("and fr.id_filial = ? ");

		 Integer qtdRows= getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{idControleCarga, idFilial});
		 return (qtdRows.intValue() > 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	// método criado pela integracao retorna o id da filialRota onde a filial de origem é a filial da rota.
	public List findListFilialRotaOrigemByRota(Long idRota){
		DetachedCriteria dc = createDetachedCriteria();
		
		dc.setProjection(Projections.property("filial.idFilial"));
		dc.add(Restrictions.and(Restrictions.eq("rota.idRota", idRota),
			   Restrictions.and(
					   Restrictions.eq("blOrigemRota",Boolean.TRUE),
					   Restrictions.eq("blDestinoRota", Boolean.FALSE))));
		
		return  findByDetachedCriteria(dc);
	}
	
	 /**
     * Busca as filiais restantes da rota, a partir do id da filial atual e do id da rota
     * @param idFilialAtual
     * @param idFilialDestino
     * @param idControleCarga
     * @return
     */
    public List<FilialRota> findFiliaisRestantesByRota(Long idRota, Long idFilialAtual) {
    	StringBuilder hql = new StringBuilder()
    	.append("select filialRota \n")
    	.append("from 	").append(getPersistentClass().getName()).append(" filialRota \n")
    	.append("inner join fetch filialRota.filial filial ")
    	.append("where 	filialRota.rota.id = ? \n")    	
    	.append("and   	filialRota.nrOrdem > ( \n")
    	.append("	select filialRotaSub.nrOrdem \n")
    	.append("	from 	").append(getPersistentClass().getName()).append(" filialRotaSub \n")    	
    	.append("	where 	filialRotaSub.rota.id = filialRota.rota.id \n")
    	.append("	and   	filialRotaSub.filial.id = ? \n")
    	.append(")");    	    
    	
    	return getAdsmHibernateTemplate().find(hql.toString(), new Object[] {idRota,idFilialAtual});    	    	   
    }
	
}