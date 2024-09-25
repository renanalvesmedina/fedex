package com.mercurio.lms.tabelaprecos.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tabelaprecos.model.TabelaMunicipioEMEX;
import com.mercurio.lms.tabelaprecos.model.dao.TabelaMunicipioEMEXDAO;

public class TabelaMunicipioEMEXService extends CrudService<TabelaMunicipioEMEX, Long>{
	private TabelaMunicipioEMEXDAO tabelaMunicipioEMEXDAO; 

	public TabelaMunicipioEMEXService() {
		super();
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	@Override
	public Serializable store(TabelaMunicipioEMEX bean) {
		return super.store(bean);
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}
	
	@Override
	public TabelaMunicipioEMEX findById(Long id) {
		return (TabelaMunicipioEMEX) super.findById(id);
	}
		
	public void setTabelaMunicipioEMEXDAO(TabelaMunicipioEMEXDAO tabelaMunicipioEMEXDAO) {
		this.tabelaMunicipioEMEXDAO = tabelaMunicipioEMEXDAO;
		this.setDao(tabelaMunicipioEMEXDAO);
	}
	
	public List<TabelaMunicipioEMEX> findByIdTabelaPrecoIdMunicipio(Long idTabelaPreco, Long idMunicipio) {
		return this.tabelaMunicipioEMEXDAO.findByIdTabelaPrecoIdMunicipio(idTabelaPreco, idMunicipio);
	}

	public TabelaMunicipioEMEX findByTabelaMunicipio(Long idTabelaPreco,
			Long idMunicipioEntrega) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tabelaPreco.idTabelaPreco", idTabelaPreco);
		params.put("municipio.idMunicipio", idMunicipioEntrega);
		
		List result = find(params);
		if (result != null && !result.isEmpty()){
			return (TabelaMunicipioEMEX) result.get(0);
		}
		return null;
	}
	
	public Long store(List<TabelaMunicipioEMEX> listEntity) {
		Long retorno = null;
		
		for (TabelaMunicipioEMEX tabelaMunicipioEMEX : listEntity) {
			//validar se ja existe
			TabelaMunicipioEMEX emex = this.findByTabelaMunicipio(tabelaMunicipioEMEX.getTabelaPreco().getIdTabelaPreco(), tabelaMunicipioEMEX.getMunicipio().getIdMunicipio());
			if(emex == null){
				retorno = (Long) this.store(tabelaMunicipioEMEX);
			} else {
				retorno = emex.getIdTabelaMunicipioEMEX();
			}
		}
		
		return retorno;
	}
}
