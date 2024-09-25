package com.mercurio.lms.expedicao.model.dao;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.VolumeSobraFilial;
import com.mercurio.lms.util.JTDateTimeUtils;

public class VolumeSobraFilialDAO extends BaseCrudDao<VolumeSobraFilial, Long> {

	@Override
	protected Class getPersistentClass() {
		return VolumeSobraFilial.class;
	}

	/**
	 * Registros para exibição na primeira aba(Listagem) da tela ConsultarSobrasDescarga, onde os mesmos aparecem agrupados.
	 * 
	 * @param criteria
	 * @return
	 */
	private SqlTemplate getSqlTemplateConsultarSobrasDescarga(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map("
		+ "conh.idDoctoServico as idDoctoServico"
		+ ", filial.idFilial as idFilialOrigem"
		+ ", filial.sgFilial as sgFilialOrigem"
		+ ", conh.filialByIdFilialOrigem.sgFilial as sgFilialOrigemDocumento"
		+ ", conh.nrDoctoServico as nrDoctoServico"
		+ ", conh.tpDocumentoServico as tpDocumentoServico"
		+ ", conh.qtVolumes as qtVolumes"
		+ ", to_char(vsf.dhCriacao.value, 'YYYY-MM-dd') as dhCriacao"
		+ ", conh.vlMercadoria as vlMercadoria"
		+ ", count (vnf.idVolumeNotaFiscal) as totalVolumeManipulado"
		+ ", sum (vnf.psAferido) as totalPesoManipulado"
		+ ")");

		hql.addFrom(VolumeSobraFilial.class.getName() + " vsf "
		+ "inner join vsf.filial filial "
		+ "inner join vsf.volumeNotaFiscal vnf "
		+ "inner join vnf.notaFiscalConhecimento nfc "
		+ "inner join nfc.conhecimento conh ");

		hql.addOrderBy("filial.sgFilial, conh.filialByIdFilialOrigem.sgFilial, conh.nrDoctoServico");
		hql.addGroupBy("conh.idDoctoServico, filial.idFilial, filial.sgFilial, conh.filialByIdFilialOrigem.sgFilial, conh.nrDoctoServico, " +
				"conh.tpDocumentoServico, conh.qtVolumes, to_char(vsf.dhCriacao.value, 'YYYY-MM-dd'), conh.vlMercadoria");
		
		/* Critérios */
		hql.addCriteria("filial.idFilial ", "=", criteria.getLong("idFilial"));
		hql.addCriteria("conh.idDoctoServico ", "=", criteria.getLong("idDoctoServico"));
		
		YearMonthDay dataManipuladoInicial = criteria.getYearMonthDay("dataManipuladoInicial");
		if(dataManipuladoInicial != null){
			hql.addCriteria("vsf.dhCriacao.value", ">=", JTDateTimeUtils.yearMonthDayToDateTime(dataManipuladoInicial));
		}
		
		YearMonthDay dataManipuladoFinal = criteria.getYearMonthDay("dataManipuladoFinal");
		if (dataManipuladoFinal!=null) {
			hql.addCriteria("vsf.dhCriacao.value", "<", JTDateTimeUtils.yearMonthDayToDateTime(dataManipuladoFinal).plusDays(1));
		}
		
		return hql;
	}

	public ResultSetPage findPaginatedConsultarSobrasDescarga(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getSqlTemplateConsultarSobrasDescarga(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
	}

	public Integer getRowCountConsultarSobrasDescarga(TypedFlatMap criteria) {
		SqlTemplate hql = this.getSqlTemplateConsultarSobrasDescarga(criteria);
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).size();
	}


	/**
	 * Registros para exibição na segunda aba(Detalhamento) da tela ConsultarSobrasDescarga, onde os mesmos aparecem desagrupados.
	 * 
	 * @param criteria
	 * @return
	 */
	private SqlTemplate getSqlTemplateConsultarSobrasDescargaDetalhamento(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map("
		+ "vsf.dhCriacao as dataManipulacao"
		+ ",vnf.nrSequencia as nrSequencia"
		+ ",conh.qtVolumes as qtVolumes"
		+ ",vnf.psAferido as psAferido"
		+ ",conh.vlMercadoria as vlMercadoria"
		+ ")");

		hql.addFrom(VolumeSobraFilial.class.getName() + " vsf "
		+ "inner join vsf.filial filial "
		+ "inner join vsf.volumeNotaFiscal vnf "
		+ "inner join vnf.notaFiscalConhecimento nfc "
		+ "inner join nfc.conhecimento conh ");

		hql.addOrderBy("vsf.dhCriacao.value, vnf.nrSequencia");
		
		/* Critérios */
		hql.addCriteria("filial.idFilial ", "=", criteria.getLong("idFilialOrigem"));
		hql.addCriteria("conh.idDoctoServico ", "=", criteria.getLong("idDoctoServico"));
		YearMonthDay dhCriacao = criteria.getYearMonthDay("dhCriacao");
		hql.addCriteria("vsf.dhCriacao.value", ">=", JTDateTimeUtils.yearMonthDayToDateTime(dhCriacao));
		hql.addCriteria("vsf.dhCriacao.value", "<", JTDateTimeUtils.yearMonthDayToDateTime(dhCriacao).plusDays(1));
		return hql;
	}

	public ResultSetPage findPaginatedConsultarSobrasDescargaDetalhamento(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getSqlTemplateConsultarSobrasDescargaDetalhamento(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
	}

	public Integer getRowCountConsultarSobrasDescargaDetalhamento(TypedFlatMap criteria) {
		SqlTemplate hql = this.getSqlTemplateConsultarSobrasDescargaDetalhamento(criteria);
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).size();
	}
}
