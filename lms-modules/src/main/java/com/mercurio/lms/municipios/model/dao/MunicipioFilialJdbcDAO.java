package com.mercurio.lms.municipios.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;

public class MunicipioFilialJdbcDAO extends JdbcDaoSupport {

	@SuppressWarnings("unchecked")
	public MunicipioFilial findMunicipioByJdbcForFranqueado(long idFranquia, long idMunicipioColetaEntrega, YearMonthDay dtInicioCompetencia) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" nr_distancia_asfalto, ");
		sql.append(" nr_distancia_chao ");
		sql.append(" from 	");
		sql.append(" 	municipio_filial	");
		sql.append(" where ");
		sql.append(" 	id_filial = ?	");
		sql.append("	and ID_MUNICIPIO = ? ");
		sql.append("   	and to_date(?,'yyyy-mm-dd') between trunc(cast(dt_vigencia_inicial as date)) and trunc(cast(dt_vigencia_final as date)) ");
		
    	List<Object> params = new ArrayList<Object>();
    	params.add(idFranquia);
    	params.add(idMunicipioColetaEntrega);
		params.add(dtInicioCompetencia.toString());
		
		List<MunicipioFilial> municipioFiliais = (List<MunicipioFilial>)getJdbcTemplate().query(sql.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet municipioFilialResult) throws SQLException {

				List<MunicipioFilial> municipioFiliais = new ArrayList<MunicipioFilial>(); 

				while(municipioFilialResult.next()){
					
					MunicipioFilial municipioFilial = new MunicipioFilial();
					municipioFilial.setNrDistanciaAsfalto(municipioFilialResult.getInt("nr_distancia_asfalto"));
					municipioFilial.setNrDistanciaChao(municipioFilialResult.getInt("nr_distancia_chao"));
					
					municipioFiliais.add(municipioFilial);
					
				}
					
				return municipioFiliais;
			}
		});
	
		if(municipioFiliais.isEmpty()){
			return null;
		}
		return municipioFiliais.get(0) ;
	}
	
	public List<MunicipioFilial> findMunicipioByJdbcForFranqueado(long idFranquia, YearMonthDay dtInicioCompetencia) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" nr_distancia_asfalto, ");
		sql.append(" nr_distancia_chao, ");
		sql.append(" ID_MUNICIPIO ");
		sql.append(" from 	");
		sql.append(" 	municipio_filial	");
		sql.append(" where ");
		sql.append(" 	id_filial = ?	");
		sql.append("   	and to_date(?,'yyyy-mm-dd') between trunc(cast(dt_vigencia_inicial as date)) and trunc(cast(dt_vigencia_final as date)) ");
		
    	List<Object> params = new ArrayList<Object>();
    	params.add(idFranquia);
		params.add(dtInicioCompetencia.toString());
		
		@SuppressWarnings("unchecked")
		List<MunicipioFilial> municipioFiliais = (List<MunicipioFilial>)getJdbcTemplate().query(sql.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet municipioFilialResult) throws SQLException {

				List<MunicipioFilial> municipioFiliais = new ArrayList<MunicipioFilial>(); 

				while(municipioFilialResult.next()){
					
					MunicipioFilial municipioFilial = new MunicipioFilial();
					municipioFilial.setNrDistanciaAsfalto(municipioFilialResult.getInt("nr_distancia_asfalto"));
					municipioFilial.setNrDistanciaChao(municipioFilialResult.getInt("nr_distancia_chao"));
					Municipio municipio = new Municipio();
					municipio.setIdMunicipio(municipioFilialResult.getLong("ID_MUNICIPIO"));
					municipioFilial.setMunicipio(municipio);
					municipioFiliais.add(municipioFilial);
					
				}
					
				return municipioFiliais;
			}
		});
	
		if(municipioFiliais.isEmpty()){
			return null;
		}
		return municipioFiliais;
	}
	
	// LMSA-6786
	public Long findIdMunicipioFromViewV_CEP(String numeroCEP) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select id_municipio from v_cep where vl_cep = :numeroCEP");
		
		Long idMunicipio = null;
    	try {
    		idMunicipio = getJdbcTemplate().queryForLong(sql.toString(), new Object[] {numeroCEP});
    	} catch (Exception e) {
    		// nao tomar nenhuma atitude para que NULL seja retornado
    	}
	
		return idMunicipio;
	}

	
}