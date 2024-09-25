package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.TabelaMunicipioEMEX;

public class TabelaMunicipioEMEXDAO extends BaseCrudDao<TabelaMunicipioEMEX, Long>{

	public TabelaMunicipioEMEXDAO() {
		super();
	}

	@Override
	protected Class getPersistentClass() {
		return TabelaMunicipioEMEX.class;
	}
	
	@Override
	public List findListByCriteria(Map<String, Object> criterions) {
		// TODO Auto-generated method stub
		return super.findListByCriteria(criterions);
	}
	
	
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("municipio", FetchMode.JOIN);
		lazyFindLookup.put("tabelaPreco", FetchMode.JOIN);
		super.initFindLookupLazyProperties(lazyFindLookup);
	}
	
	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("municipio", FetchMode.JOIN);
		lazyFindList.put("municipio.unidadeFederativa", FetchMode.JOIN);
		lazyFindList.put("tabelaPreco", FetchMode.JOIN);
		lazyFindList.put("tabelaPreco.tipoTabelaPreco", FetchMode.JOIN);
		lazyFindList.put("tabelaPreco.subtipoTabelaPreco", FetchMode.JOIN);
		super.initFindListLazyProperties(lazyFindList);
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("municipio", FetchMode.JOIN);
		lazyFindById.put("municipio.unidadeFederativa", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.tipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.subtipoTabelaPreco", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	@SuppressWarnings("unchecked")
	public List<TabelaMunicipioEMEX> findByIdTabelaPrecoIdMunicipio(Long idTabelaPreco, Long idMunicipio) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tme ");
		query.append("FROM TabelaMunicipioEMEX tme  ");
		query.append("JOIN FETCH tme.tabelaPreco tp ");
		query.append("JOIN FETCH tp.tipoTabelaPreco ttp ");
		query.append("JOIN FETCH tp.subtipoTabelaPreco stp ");
		query.append("JOIN FETCH tme.municipio mu ");
		query.append("JOIN FETCH mu.unidadeFederativa uf ");
		query.append("WHERE 1 = 1 ");
		if(idTabelaPreco != null){
			query.append("AND tp.idTabelaPreco = :idTabelaPreco ");
		}
		if(idMunicipio != null){
			query.append("AND mu.idMunicipio = :idMunicipio ");
		}
		query.append("ORDER BY mu.nmMunicipio ");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		if(idTabelaPreco != null){
			parametros.put("idTabelaPreco", idTabelaPreco);
		}
		if(idMunicipio != null){
			parametros.put("idMunicipio", idMunicipio);
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametros);
	}
	

}
