package com.mercurio.lms.franqueados.model.dao;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.MunicipioNaoAtendidoFranqueado;

public class MunicipioNaoAtendidoFranqueadoDAO extends BaseCrudDao<MunicipioNaoAtendidoFranqueado, Long> {

	@Override
	protected Class<MunicipioNaoAtendidoFranqueado> getPersistentClass() {
		return MunicipioNaoAtendidoFranqueado.class;
	}

	@SuppressWarnings("unchecked")
	public MunicipioNaoAtendidoFranqueado findMunicipioVigenciaByIdFranquia(Long idFranquia, Long idMunicipio, YearMonthDay dtInicioCompetencia) {
		StringBuffer hql = new StringBuffer()
		.append("select mnaf from " + MunicipioNaoAtendidoFranqueado.class.getName() + " as mnaf	")
		.append("where mnaf.franquia.idFranquia = ?				")
		.append("and mnaf.municipio.idMunicipio = ? 		")
		.append("and mnaf.dtVigenciaInicial <= ? 				")
		.append("and mnaf.dtVigenciaFinal >= ? 				");

		List<MunicipioNaoAtendidoFranqueado> municipioNaoAtendidoFranqueadoList = (List<MunicipioNaoAtendidoFranqueado>)getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idFranquia, idMunicipio, dtInicioCompetencia, dtInicioCompetencia});
		if(municipioNaoAtendidoFranqueadoList.isEmpty()){
			return null;
		}
		return municipioNaoAtendidoFranqueadoList.get(0) ;
	}

}
