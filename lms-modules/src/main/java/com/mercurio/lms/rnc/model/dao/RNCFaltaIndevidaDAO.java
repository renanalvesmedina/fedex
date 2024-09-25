package com.mercurio.lms.rnc.model.dao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.VolumeSobraFilial;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RNCFaltaIndevidaDAO extends BaseCrudDao<OcorrenciaNaoConformidade, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return OcorrenciaNaoConformidade.class;
    }

	@SuppressWarnings("unchecked")
	public ResultSetPage findFaltaIndevidaPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = getSqlFalta(criteria);

		return getAdsmHibernateTemplate()
				.findPaginated(sql.getSql(), findDef.getCurrentPage(),
						findDef.getPageSize(), sql.getCriteria());
    }
	
	@Override
	public Integer getRowCount(Map criteria) {
		SqlTemplate sql = getSqlFalta(criteria);

		return getAdsmHibernateTemplate()
				.getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }

	private SqlTemplate getSqlFalta(Map criteria) {
		StringBuffer projecao = new StringBuffer()
		.append("SELECT oncSobra from ")
		.append(OcorrenciaNaoConformidade.class.getName())
		.append(" as oncSobra ")
		.append("WHERE ")
		.append("oncSobra.naoConformidade.idNaoConformidade = onc.naoConformidade.idNaoConformidade")
		.append(" AND oncSobra.naoConformidade.tpStatusNaoConformidade = 'RNC'")
		.append(" AND oncSobra.motivoAberturaNc.tpMotivo = 'SV'")
		.append(" AND oncSobra.tpStatusOcorrenciaNc = 'A'");
		
		StringBuffer from = new StringBuffer()
		.append(OcorrenciaNaoConformidade.class.getName())
		.append(" as onc ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("onc");
		sql.addFrom(from.toString());
		sql.addOrderBy("onc.filialByIdFilialAbertura.sgFilial, onc.naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial, onc.naoConformidade.doctoServico.nrDoctoServico, onc.naoConformidade.doctoServico.filialByIdFilialDestino.sgFilial");
		
		sql.addCriteria("onc.naoConformidade.tpStatusNaoConformidade", "=", "RNC");
		sql.addCriteria("onc.motivoAberturaNc.tpMotivo", "=", "FV");
		sql.addCriteria("onc.tpStatusOcorrenciaNc", "=", "A");
		
		filtros(criteria, sql);
		if (criteria.get("idFilialSobra") != null) {
			sql.addCustomCriteria(" (exists ("
					+ projecao
					+ " AND oncSobra.filialByIdFilialAbertura.idFilial = "
					+ criteria.get("idFilialSobra")
					+ " ) or exists (select 1 from "
					+ VolumeSobraFilial.class.getName()
					+ " as vsf WHERE vsf.filial.idFilial = "
					+ criteria.get("idFilialSobra")
					+ " and vsf.volumeNotaFiscal.notaFiscalConhecimento.conhecimento.idDoctoServico = onc.naoConformidade.doctoServico.idDoctoServico ))");
		}
		if (criteria.get("idFilialFalta") != null) {
			sql.addCriteria("onc.filialByIdFilialAbertura.idFilial", "=", criteria.get("idFilialFalta"));
		}
		if (criteria.get("idFilialResponsavel") != null) {
			sql.addCriteria("onc.filialByIdFilialResponsavel.idFilial", "=", criteria.get("idFilialResponsavel"));
		}
		if (criteria.get("dtCriacaoFaltaInicial") != null && criteria.get("dtCriacaoFaltaFinal") != null) {
			sql.addCriteria("TRUNC(onc.dhInclusao.value)",">=", criteria.get("dtCriacaoFaltaInicial"));
			sql.addCriteria("TRUNC(onc.dhInclusao.value)","<=", criteria.get("dtCriacaoFaltaFinal"));
		}
		return sql;
	}
	
	
	public List<OcorrenciaNaoConformidade> findSobra(Map criteria) {
		
		StringBuffer from = new StringBuffer()
		.append(OcorrenciaNaoConformidade.class.getName())
		.append(" as onc ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("onc");
		sql.addFrom(from.toString());
		
		sql.addCriteria("onc.naoConformidade.tpStatusNaoConformidade", "=", "RNC");
		sql.addCriteria("onc.motivoAberturaNc.tpMotivo", "=", "SV");
		sql.addCriteria("onc.tpStatusOcorrenciaNc", "=", "A");
		
		filtros(criteria, sql);
		if (criteria.get("idDoctoServico") != null) {
			sql.addCriteria("onc.naoConformidade.doctoServico.idDoctoServico", "=", criteria.get("idDoctoServico"));
		}
		
		return (List<OcorrenciaNaoConformidade>)getAdsmHibernateTemplate()
				.find(sql.getSql(), sql.getCriteria());
    }

	private void filtros(Map criteria, SqlTemplate sql) {
		if (criteria.get("nrRNC") != null) {
			sql.addCriteria("onc.naoConformidade.nrNaoConformidade", "=", criteria.get("nrRNC"));
		}
	}
	
	public List<VolumeSobraFilial> findVolumesSobraDescarga(Map criteria) {
		
		StringBuffer from = new StringBuffer()
		.append(VolumeSobraFilial.class.getName())
		.append(" as vsf ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("vsf");
		sql.addFrom(from.toString());
		
		if (criteria.get("idDoctoServico") != null) {
			sql.addCriteria("vsf.volumeNotaFiscal.notaFiscalConhecimento.conhecimento.idDoctoServico", "=", criteria.get("idDoctoServico"));
		}
		if (criteria.get("idFilialSobra") != null) {
			sql.addCriteria("vsf.filial.idFilial", "=", criteria.get("idFilialSobra"));
		}
		
		return (List<VolumeSobraFilial>) getAdsmHibernateTemplate()
				.find(sql.getSql(), sql.getCriteria());
		
		
    }

}