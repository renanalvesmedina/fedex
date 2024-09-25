package com.mercurio.lms.carregamento.model.dao;

import java.util.List;

import org.hibernate.Session;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.OcorrenciaDoctoFilial;

public class OcorrenciaDoctoFilialDAO extends BaseCrudDao<OcorrenciaDoctoFilial, Long> {

	@Override
	protected Class getPersistentClass() {
		return OcorrenciaDoctoFilial.class;
	}
	
	public Integer getRowCount(YearMonthDay dataInicial, YearMonthDay dataFinal, Long idDoctoServico, Long idFilialOrigem, Long idFilialDestino, Long idFilialOcorrencia) {
		StringBuilder sql = new StringBuilder(); 
		
		sql.append("FROM OCORRENCIA_DOCTO_FILIAL odf ")
		   .append("WHERE 1 = 1 ");
		   
		if (idDoctoServico != null) {
			sql.append(" AND odf.ID_DOCTO_SERVICO = ").append(idDoctoServico);
		} else {
			sql.append(" and trunc(cast (odf.DH_OCORRENCIA as date)) >= to_date('").append(dataInicial).append("', 'yyyy-mm-dd') ")
			.append(" and trunc(cast (odf.DH_OCORRENCIA as date)) <= to_date('").append(dataFinal).append("', 'yyyy-mm-dd') ");
		
			if (idDoctoServico != null) {
				sql.append(" AND odf.ID_DOCTO_SERVICO = ").append(idDoctoServico);
			}
			if (idFilialOrigem != null) {
				sql.append(" AND odf.ID_FILIAL_DOCTO_ORIGEM = ").append(idFilialOrigem);
			}
			if (idFilialDestino != null) {
				sql.append(" AND odf.ID_FILIAL_DOCTO_DESTINO = ").append(idFilialDestino);
			}
			if (idFilialOcorrencia != null) {
				sql.append(" AND odf.ID_FILIAL_OCORRENCIA = ").append(idFilialOcorrencia);
			}
		}
			
		sql.append(" ORDER BY odf.ID_OCORRENCIA_DOCTO_FILIAL");

		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[] {});
	}

	public List findOcorrenciasAlteracaoFilialLocalizacaoDoctoServico(YearMonthDay dataInicial, YearMonthDay dataFinal, Long idDoctoServico, Long idFilialOrigem, Long idFilialDestino, Long idFilialOcorrencia, TypedFlatMap criteria) {

		SqlTemplate sql = getSql(dataInicial, dataFinal, idDoctoServico, idFilialOrigem,
				idFilialDestino, idFilialOcorrencia);

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}
	
	public ResultSetPage findOcorrenciasAlteracaoFilialLocalizacaoDoctoServico(TypedFlatMap criteria) {

		SqlTemplate sql = getSql(criteria.getYearMonthDay("periodoInicial"), criteria.getYearMonthDay("periodoFinal"), criteria.getLong("idDoctoServico"), criteria.getLong("idFilialO"),
				criteria.getLong("idFilialDestino"), criteria.getLong("idFilialOcorrencia"));

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());


	}

	private SqlTemplate getSql(YearMonthDay dataInicial, YearMonthDay dataFinal,
			Long idDoctoServico, Long idFilialOrigem, Long idFilialDestino,
			Long idFilialOcorrencia) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(getPersistentClass().getName() + " odf " +
				" join fetch odf.doctoServico ds " +
				" join fetch odf.filialDoctoDestino fdd " +
				" join fetch odf.filialOcorrencia fo " +
				" join fetch odf.usuarioOcorrencia uo " +
				" join fetch odf.localizacaoMercadoria lo");
		
		sql.addProjection(" odf ");
		
		if (idDoctoServico != null) {
			sql.addCriteria("odf.doctoServico.idDoctoServico","=",idDoctoServico);
		} else {
	
			sql.addCriteria("TRUNC(odf.dhOcorrencia.value)",">=", dataInicial, YearMonthDay.class);
			sql.addCriteria("TRUNC(odf.dhOcorrencia.value)","<=", dataFinal, YearMonthDay.class);
			
			
			if (idFilialOrigem != null) {
				sql.addCriteria("odf.filialDoctoOrigem.idFilial","=",idFilialOrigem);
			}
			if (idFilialDestino != null) {
				sql.addCriteria("odf.filialDoctoDestino.idFilial","=",idFilialDestino);
			}
			if (idFilialOcorrencia != null) {
				sql.addCriteria("odf.filialOcorrencia.idFilial","=",idFilialOcorrencia);
			}
		}
		
		sql.addOrderBy("odf.idOcorrenciaDoctoFilial");
		
		return sql;
	}
	
	

}
