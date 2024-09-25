package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.DestinoProposta;
import com.mercurio.lms.vendas.model.DiferencaCapitalInterior;


/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class DiferencaCapitalInteriorDAO extends BaseCrudDao<DiferencaCapitalInterior, Long> {

	protected final Class getPersistentClass() {
		return DiferencaCapitalInterior.class;
	}

    protected void initFindByIdLazyProperties(Map map) {
        map.put("ufOrigem", FetchMode.JOIN);
        map.put("ufDestino", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) {
        map.put("ufOrigem", FetchMode.JOIN);
        map.put("ufDestino", FetchMode.JOIN);
    }	
	
	public DiferencaCapitalInterior findByUF(Long idUFOrigem, Long idUFDestino) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DiferencaCapitalInterior.class,"dci");
		
		if(idUFOrigem != null){
			criteria.add(Restrictions.eq("ufOrigem.id", idUFOrigem));
		}else{
			criteria.add(Restrictions.isNull("ufOrigem.id"));
		}
		
		if(idUFDestino != null){
			criteria.add(Restrictions.eq("ufDestino.id", idUFDestino));
		}
		
		List<DiferencaCapitalInterior> list = findByDetachedCriteria(criteria); 
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}		
		return null;
	}

	public Map<String, Object> findByIdSimulacao(Long idDestinoProposta, Long idUnidadeFederativa) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new map(dci.pcDiferencaMinima as pcDiferencaMinima," +
				" dci.pcDiferencaMinimaAdvalorem as pcDiferencaMinimaAdvalorem)");
		
		sql.addFrom(DiferencaCapitalInterior.class.getName(), "dci");
		sql.addFrom(DestinoProposta.class.getName(), "dp");

		sql.addCustomCriteria("dp.tipoLocalizacaoMunicipio.id = 3");
		sql.addCustomCriteria("dci.ufDestino = dp.unidadeFederativa");
		
		sql.addCriteria("dp.id", "=", idDestinoProposta);
		
		if (idUnidadeFederativa != null){
			sql.addCriteria("dci.ufOrigem.id", "=", idUnidadeFederativa);
		}else{
			sql.addCustomCriteria("dci.ufOrigem is null");
}
		
		
		List result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (result != null && result.size() > 0){
			return (Map<String, Object>)result.get(0);
		}
		return null;
	}
}
