package com.mercurio.lms.franqueados.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.lms.franqueados.model.MunicipioNaoAtendidoFranqueado;
import com.mercurio.lms.municipios.model.Municipio;

public class MunicipioNaoAtendidoFranqueadoJdbcDAO extends JdbcDaoSupport {

	@SuppressWarnings("unchecked")
	public MunicipioNaoAtendidoFranqueado findMunicipioByJdbc(Long idFranquia, Long idMunicipio, YearMonthDay dtInicioCompetencia) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" 	nr_km_asfalto, ");
		sql.append(" 	nr_km_terra ");
		sql.append(" from 	");
		sql.append(" 	municipio_nao_atend_frq ");
		sql.append(" where ");
		sql.append(" 	id_franquia = ?	");
		sql.append("	and id_municipio = ? ");
		sql.append("   	and trunc(cast(dt_vigencia_inicial as date)) <= to_date(?,'yyyy-mm-dd') ");
		sql.append("   	and trunc(cast(dt_vigencia_final as date)) >= to_date(?,'yyyy-mm-dd') ");
		
    	List<Object> params = new ArrayList<Object>();
    	params.add(idFranquia);
    	params.add(idMunicipio);
		params.add(dtInicioCompetencia.toString());
		params.add(dtInicioCompetencia.toString());
		
		List<MunicipioNaoAtendidoFranqueado> municipioFiliais = (List<MunicipioNaoAtendidoFranqueado>)getJdbcTemplate().query(sql.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet municipioNaoAtendidoFranqueadoResult) throws SQLException {

				List<MunicipioNaoAtendidoFranqueado> municipioNaoAtendidoFranqueados = new ArrayList<MunicipioNaoAtendidoFranqueado>(); 

				while(municipioNaoAtendidoFranqueadoResult.next()){
					
					MunicipioNaoAtendidoFranqueado municipioNaoAtendidoFranqueado = new MunicipioNaoAtendidoFranqueado();
					municipioNaoAtendidoFranqueado.setNrKmAsfalto(municipioNaoAtendidoFranqueadoResult.getInt("nr_km_asfalto"));
					municipioNaoAtendidoFranqueado.setNrKmTerra(municipioNaoAtendidoFranqueadoResult.getInt("nr_km_terra"));
					
					municipioNaoAtendidoFranqueados.add(municipioNaoAtendidoFranqueado);
					
				}
					
				return municipioNaoAtendidoFranqueados;
			}
		});
	
		if(municipioFiliais.isEmpty()){
			return null;
		}
		return municipioFiliais.get(0) ;
	}
	
	public List<MunicipioNaoAtendidoFranqueado> findMunicipioByJdbc(Long idFranquia, YearMonthDay dtInicioCompetencia) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" 	nr_km_asfalto, ");
		sql.append(" 	nr_km_terra, ");
		sql.append("	id_municipio ");
		sql.append(" from 	");
		sql.append(" 	municipio_nao_atend_frq ");
		sql.append(" where ");
		sql.append(" 	id_franquia = ?	");
		sql.append("   	and trunc(cast(dt_vigencia_inicial as date)) <= to_date(?,'yyyy-mm-dd') ");
		sql.append("   	and trunc(cast(dt_vigencia_final as date)) >= to_date(?,'yyyy-mm-dd') ");
		
    	List<Object> params = new ArrayList<Object>();
    	params.add(idFranquia);
		params.add(dtInicioCompetencia.toString());
		params.add(dtInicioCompetencia.toString());
		
		List<MunicipioNaoAtendidoFranqueado> municipioFiliais = (List<MunicipioNaoAtendidoFranqueado>)getJdbcTemplate().query(sql.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet municipioNaoAtendidoFranqueadoResult) throws SQLException {

				List<MunicipioNaoAtendidoFranqueado> municipioNaoAtendidoFranqueados = new ArrayList<MunicipioNaoAtendidoFranqueado>(); 

				while(municipioNaoAtendidoFranqueadoResult.next()){
					
					MunicipioNaoAtendidoFranqueado municipioNaoAtendidoFranqueado = new MunicipioNaoAtendidoFranqueado();
					municipioNaoAtendidoFranqueado.setNrKmAsfalto(municipioNaoAtendidoFranqueadoResult.getInt("nr_km_asfalto"));
					municipioNaoAtendidoFranqueado.setNrKmTerra(municipioNaoAtendidoFranqueadoResult.getInt("nr_km_terra"));
					Municipio municipio = new Municipio();
					municipio.setIdMunicipio(municipioNaoAtendidoFranqueadoResult.getLong("id_municipio"));
					municipioNaoAtendidoFranqueado.setMunicipio(municipio);
					municipioNaoAtendidoFranqueados.add(municipioNaoAtendidoFranqueado);
					
}
					
				return municipioNaoAtendidoFranqueados;
			}
		});
	
		if(municipioFiliais.isEmpty()){
			return null;
		}
		return municipioFiliais ;
	}
	
}
