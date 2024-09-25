package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.municipios.model.HistoricoColetaEntrega;
import com.mercurio.lms.util.JTDateTimeUtils;

public class HistoricoColetaEntregaDAO extends BaseCrudDao<HistoricoColetaEntrega, Long> {

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return HistoricoColetaEntrega.class;
	}

	/**
	 * Retorna dados para gerar o arquivo CSV.
	 * @param parameters
	 * 
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> findHistoricoColetaEntrega(Map<String, Object> parameters) {		
		StringBuilder sql = new StringBuilder();		
		sql.append("SELECT");
		sql.append(" f.sg_filial AS \"Filial da Rota\",");
		sql.append(" hce.nr_rota AS \"Número da Rota\",");
		sql.append(" hce.nr_rota_ant AS \"Número da Rota anterior\",");
		sql.append(" hce.ds_rota AS \"Descrição\",");
		sql.append(" hce.nr_km AS \"KM\",");
		sql.append(" ua.nm_usuario AS \"Usuário\",");
		sql.append(" TO_CHAR(hce.dh_atualizacao, 'DD/MM/YYYY') AS \"Data da Atualização\",");
		sql.append(" TO_CHAR(hce.dt_vigencia_inicial, 'DD/MM/YYYY') AS \"Vigência Inicial da Rota\",");
		sql.append(" TO_CHAR(hce.dt_vigencia_final, 'DD/MM/YYYY') AS \"Vigência Final da Rota\",");
		sql.append(" REPLACE(hce.ds_saidas_previstas,',',' - ') AS \"Saídas previstas\"");
		sql.append(" FROM historico_coleta_entrega hce");
		sql.append("  JOIN filial f");
		sql.append("   ON f.id_filial = hce.id_filial");
		sql.append("  JOIN usuario_adsm ua");
		sql.append("   ON ua.id_usuario = hce.id_usuario");
		sql.append(" WHERE hce.id_filial = :idFilial");
		sql.append("  AND (hce.dh_atualizacao >= :dhAtualizacaoInicial AND hce.dh_atualizacao <= :dhAtualizacaoFinal)");
		
		if(parameters.containsKey("nrRotaColetaEntrega")){
			sql.append(" AND ( hce.nr_rota = :nrRotaColetaEntrega");
			sql.append(" OR hce.nr_rota_ANT = :nrRotaColetaEntrega ) " );			
		}
		
		if(parameters.containsKey("vigentes")){
			String vigentes = (String) parameters.get("vigentes");
			
			if ("S".equals(vigentes)) {
				sql.append(" AND (TRUNC(hce.dt_vigencia_inicial) <= TRUNC(:dataAtual) AND (TRUNC(hce.dt_vigencia_final) >= TRUNC(:dataAtual) OR hce.dt_vigencia_final IS NULL))");
				parameters.put("dataAtual", JTDateTimeUtils.getDataAtual());
			} else if ("N".equals(vigentes)) {
				sql.append(" AND (TRUNC(hce.dt_vigencia_inicial) > TRUNC(:dataAtual) OR TRUNC(hce.dt_vigencia_final) < TRUNC(:dataAtual))");
				parameters.put("dataAtual", JTDateTimeUtils.getDataAtual());
			}			
		}
		if(parameters.containsKey("nrRotaColetaEntrega")){
			sql.append(" ORDER BY hce.dh_atualizacao DESC");
		}else{
			sql.append(" ORDER BY hce.nr_rota ASC, hce.dh_atualizacao DESC");
		}
		
		
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Filial da Rota");
				sqlQuery.addScalar("Número da Rota");
				sqlQuery.addScalar("Número da Rota anterior");
				sqlQuery.addScalar("Descrição");
				sqlQuery.addScalar("KM");
				sqlQuery.addScalar("Usuário");
				sqlQuery.addScalar("Data da Atualização");
				sqlQuery.addScalar("Vigência Inicial da Rota");
				sqlQuery.addScalar("Vigência Final da Rota");
				sqlQuery.addScalar("Saídas previstas");
			}
		};
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parameters, csq);		
	}	
}