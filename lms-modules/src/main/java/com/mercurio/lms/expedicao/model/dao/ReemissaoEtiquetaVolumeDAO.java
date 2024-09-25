package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.ReemissaoEtiquetaVolume;
import com.mercurio.lms.expedicao.model.ReemissaoEtiquetaVolumeDTO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class ReemissaoEtiquetaVolumeDAO extends
		BaseCrudDao<ReemissaoEtiquetaVolume, Long> {
	public ReemissaoEtiquetaVolume findByFilial(Long idFilial) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(getPersistentClass().getName()
				+ " reemissaoEtiquetaVolume ");
		hql.addCriteria("reemissaoEtiquetaVolume.filial.id", "=", idFilial);
		return (ReemissaoEtiquetaVolume) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public ResultSetPage<ReemissaoEtiquetaVolume> findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sqlTemplate = getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(sqlTemplate.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sqlTemplate.getCriteria());
	}
	
	public List<ReemissaoEtiquetaVolumeDTO> find(TypedFlatMap criteria) {
		List<ReemissaoEtiquetaVolumeDTO> retorno = new ArrayList<ReemissaoEtiquetaVolumeDTO>();
		SqlTemplate sqlTemplate = getSqlTemplate(criteria);
		List<HashMap<String, Object>> registros=getAdsmHibernateTemplate().find(sqlTemplate.getSql(), sqlTemplate.getCriteria());
		for(HashMap<String, Object> reg:registros){
			retorno.add(new ReemissaoEtiquetaVolumeDTO(reg));
		}
		return retorno;
	}

	public SqlTemplate getSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate sqlTemplate = this.getSqlTemplateFindPaginated(criteria);
		
		sqlTemplate.addProjection("new map ("
				+ " filialEmissao.sgFilial as sgFilialEmissao, "
				+ " usuarioEmissaoADSM.nmUsuario as nmUsuarioEmissao, "
				+ " volumeNotaFiscal.dhEmissao as dhEmissao,"
				+ " volumeNotaFiscal.dsMac as dsMacEmissao,"
				/*--*/
				+ " filialReemissao.sgFilial as sgFilialReemissao,"
				+ " usuarioReemissaoADSM.nmUsuario as nmUsuarioReemissao, "
				+ " reemissaoEtiquetaVolume.dhReemissao as dhReemissao,"
				+ " reemissaoEtiquetaVolume.dsMac as dsMacReemissao,"
				/*--*/
				+ " volumeNotaFiscal.notaFiscalConhecimento.conhecimento.filialOrigem.sgFilial as sgFilialDocumentoServico,"
				+ " volumeNotaFiscal.nrConhecimento as documentoServico,"
				+ " volumeNotaFiscal.notaFiscalConhecimento.cliente.pessoa.nmPessoa as nmCliente," 
				+ " volumeNotaFiscal.nrVolumeEmbarque as codigoBarras,"
				+ " reemissaoEtiquetaVolume.idReemissaoEtiquetaVolume as idReemissaoEtiquetaVolume"
				+ " ) ");
		sqlTemplate.addOrderBy("filialReemissao.sgFilial", "asc");
		sqlTemplate.addOrderBy("volumeNotaFiscal.nrConhecimento", "asc");
		sqlTemplate.addOrderBy("reemissaoEtiquetaVolume.dhReemissao", "asc");
		return sqlTemplate;
	}

	@Override
	protected Class getPersistentClass() {
		return ReemissaoEtiquetaVolume.class;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplateFindPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	private SqlTemplate getSqlTemplateFindPaginated(TypedFlatMap criteria) {
		StringBuffer from = new StringBuffer();
		/**
		 * from
		 */
		from.append(ReemissaoEtiquetaVolume.class.getName()
				+ " as reemissaoEtiquetaVolume ")
				.append("	left join reemissaoEtiquetaVolume.filial as filialReemissao ")
				.append("	left join reemissaoEtiquetaVolume.volumeNotaFiscal as volumeNotaFiscal ")
				.append("	left join volumeNotaFiscal.filialEmissao as filialEmissao ")
				
				.append("	left join reemissaoEtiquetaVolume.usuario as usuarioReemissao ")
				.append("	left join usuarioReemissao.usuarioADSM as usuarioReemissaoADSM ")
				.append("	left join reemissaoEtiquetaVolume.volumeNotaFiscal.usuario as usuarioEmissao ")
				.append("	left join usuarioEmissao.usuarioADSM as usuarioEmissaoADSM ")
				;
		SqlTemplate sqlTemplate = new SqlTemplate();
		sqlTemplate.addFrom(from.toString());
		/**
		 * where
		 */
		YearMonthDay periodoReemissaoInicial=criteria.getYearMonthDay("periodoReemissaoInicial");
		YearMonthDay periodoReemissaoFinal=criteria.getYearMonthDay("periodoReemissaoFinal");
		YearMonthDay periodoEmissaoInicial=criteria.getYearMonthDay("periodoEmissaoInicial");
		YearMonthDay periodoEmissaoFinal=criteria.getYearMonthDay("periodoEmissaoFinal");
		sqlTemplate.addCriteria("filialReemissao.idFilial", "=", criteria.getLong("idFilialReemissao"));
		sqlTemplate.addCriteria("usuarioReemissaoADSM.idUsuario", "=", criteria.getLong("idUsuarioReemissao"));
		sqlTemplate.addCriteria("reemissaoEtiquetaVolume.dhReemissao.value", ">=", periodoReemissaoInicial!=null?periodoReemissaoInicial.toDateTimeAtMidnight(JTDateTimeUtils.getUserDtz()):null, DateTime.class);
		sqlTemplate.addCriteria("reemissaoEtiquetaVolume.dhReemissao.value", "<=", periodoReemissaoFinal!=null?periodoReemissaoFinal.toDateTimeAtMidnight(JTDateTimeUtils.getUserDtz()).plusDays(1):null, DateTime.class);
		/*--*/
		sqlTemplate.addCriteria("filialEmissao.idFilial", "=", criteria.getLong("idFilialEmissao"));
		sqlTemplate.addCriteria("usuarioEmissaoADSM.idUsuario", "=", criteria.getLong("idUsuarioEmissao"));
		sqlTemplate.addCriteria("volumeNotaFiscal.dhEmissao.value", ">=", periodoEmissaoInicial!=null?periodoEmissaoInicial.toDateTimeAtMidnight(JTDateTimeUtils.getUserDtz()):null, DateTime.class);
		sqlTemplate.addCriteria("volumeNotaFiscal.dhEmissao.value", "<=", periodoEmissaoFinal!=null?periodoEmissaoFinal.toDateTimeAtMidnight(JTDateTimeUtils.getUserDtz()).plusDays(1):null, DateTime.class);
		/*--*/
		sqlTemplate.addCriteria("volumeNotaFiscal.notaFiscalConhecimento.conhecimento.idDoctoServico", "=", criteria.getLong("idDoctoServico"));
		sqlTemplate.addCriteria("volumeNotaFiscal.nrVolumeEmbarque", "like", criteria.getString("codigoBarras"));
		sqlTemplate.addCriteria("volumeNotaFiscal.notaFiscalConhecimento.cliente.idCliente", "=", criteria.getLong("idCliente"));
		
		return sqlTemplate;
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
	}
}
