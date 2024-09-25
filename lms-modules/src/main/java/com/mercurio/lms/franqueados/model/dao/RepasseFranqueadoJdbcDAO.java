package com.mercurio.lms.franqueados.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.lms.franqueados.model.RepasseFranqueado;

public class RepasseFranqueadoJdbcDAO extends JdbcDaoSupport{

	@SuppressWarnings("unchecked")
	public List<RepasseFranqueado> findRepasseFranqueado(YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia){
		
		StringBuilder query = new StringBuilder();
		
		query.append(" select ");
		query.append(" 		id_repasse_frq,  ");
		query.append(" 		pc_aliq_pis,  ");
		query.append(" 		pc_repasse_pis,  ");
		query.append(" 		pc_aliq_cofins,  ");
		query.append(" 		pc_repasse_cofins,  ");
		query.append(" 		pc_repasse_icms  ");
		query.append(" from ");
		query.append("		repasse_frq ");
		query.append(" where ");
		query.append("		trunc(cast(dt_vigencia_inicial as date)) <= to_date(?,'yyyy-mm-dd') ");
		query.append("		and trunc(cast(dt_vigencia_final as date)) <= to_date(?,'yyyy-mm-dd') ");
		
    	List<Object> params = new ArrayList<Object>();
		params.add(dtIniCompetencia.toString());
		params.add(dtFimCompetencia.toString());
		
		List<RepasseFranqueado> repasseFranqueados = (List<RepasseFranqueado>)getJdbcTemplate().query(query.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet repasseFranqueadoResult) throws SQLException, DataAccessException {

				List<RepasseFranqueado> repasseFranqueados = new ArrayList<RepasseFranqueado>(); 

				while(repasseFranqueadoResult.next()){
					
					RepasseFranqueado repasseFranqueado = new RepasseFranqueado();
					repasseFranqueado.setIdRepasseFrq(repasseFranqueadoResult.getLong(0));
					repasseFranqueado.setPcAliqPis(repasseFranqueadoResult.getBigDecimal(1));
					repasseFranqueado.setPcRepassePis(repasseFranqueadoResult.getBigDecimal(2));
					repasseFranqueado.setPcAliqCofins(repasseFranqueadoResult.getBigDecimal(3));
					repasseFranqueado.setPcRepasseCofins(repasseFranqueadoResult.getBigDecimal(4));
					repasseFranqueado.setPcRepasseIcms(repasseFranqueadoResult.getBigDecimal(5));
					
					repasseFranqueados.add(repasseFranqueado);
					
				}
					
				return repasseFranqueados;
			}
		});
	
		return repasseFranqueados;
	}

}
