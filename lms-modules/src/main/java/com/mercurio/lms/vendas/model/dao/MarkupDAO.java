package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Markup;
import com.mercurio.lms.vendas.model.MarkupFaixaProgressiva;
import com.mercurio.lms.vendas.model.ValorMarkupFaixaProgressiva;
import com.mercurio.lms.vendas.model.ValorMarkupPrecoFrete;

public class MarkupDAO extends BaseCrudDao<Markup, Long> {
	
	@Override
	protected Class<Markup> getPersistentClass() {
		return Markup.class;
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("parcelaPreco", FetchMode.JOIN);
		lazyFindById.put("parcelaPreco.tpPrecificacao", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("parcelaPreco", FetchMode.JOIN);
		super.initFindListLazyProperties(lazyFindList);
	}
	
	public Markup findMarkupGeral(Long idTabelaPreco) {
		StringBuilder query = new StringBuilder()
		.append(" SELECT m ")
		.append(" FROM " + Markup.class.getName() + " m  ")
		.append(" left join fetch m.parcelaPreco pp")
		.append(" WHERE m.tabelaPreco.idTabelaPreco = :idTabelaPreco ")
		.append(" AND pp is null ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTabelaPreco", idTabelaPreco);

		return (Markup) getAdsmHibernateTemplate().findUniqueResult(query.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Markup> findMarkupsGeneralidade(Long idTabelaPreco, boolean vigentes, boolean notVigentes) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder()
			.append(" SELECT m ")
			.append(" FROM " + Markup.class.getName() + " m  ")
			.append(" left join fetch m.parcelaPreco pp")
			.append(" WHERE m.tabelaPreco.idTabelaPreco = :idTabelaPreco ")
			.append(" AND pp is not null ");
			
		if(vigentes){
			query.append(" AND m.dtVigenciaInicial <= :hoje ")
			.append(" AND ( ")
			.append("			m.dtVigenciaFinal is null ")
			.append("		OR 	m.dtVigenciaFinal >= :hoje) ");
			params.put("hoje", JTDateTimeUtils.getDataAtual());
		}
		
		if(notVigentes){
			query.append(" AND (  ")
				 .append(" (m.dtVigenciaInicial > :hoje AND m.dtVigenciaFinal > :hoje) ")
				 .append(" OR ")
				 .append(" (m.dtVigenciaInicial < :hoje AND m.dtVigenciaFinal < :hoje) ")
				 .append("    ) ");
			
			if(!vigentes){
				params.put("hoje", JTDateTimeUtils.getDataAtual());
			}
		}
		
		query.append(" ORDER BY pp.nmParcelaPreco, m.dtVigenciaInicial, m.dtVigenciaFinal ");

		params.put("idTabelaPreco", idTabelaPreco);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<ValorMarkupPrecoFrete> findValorMarkupPrecoFrete(Long idTabelaPreco, boolean vigentes, boolean notVigentes) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder()
		.append(" SELECT vmpf ")
		.append(" FROM " + ValorMarkupPrecoFrete.class.getName() + " vmpf ")
		.append(" left join fetch vmpf.precoFrete pf")
		.append(" left join fetch pf.tabelaPrecoParcela tpp")
		.append(" left join fetch tpp.parcelaPreco pp")
		.append(" left join fetch vmpf.rotaPreco rp")
		.append(" left join fetch vmpf.tarifaPreco tp")
		.append(" left join fetch rp.zonaByIdZonaOrigem rpzo")
		.append(" left join fetch rp.paisByIdPaisOrigem rppo")
		.append(" left join fetch rp.unidadeFederativaByIdUfOrigem rpufo")
		.append(" left join fetch rp.filialByIdFilialOrigem rpfo")
		.append(" left join fetch rp.municipioByIdMunicipioOrigem rpmo")
		.append(" left join fetch rp.aeroportoByIdAeroportoOrigem rpao")
		.append(" left join fetch rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem rptlo")
		.append(" left join fetch rp.tipoLocalizacaoMunicipioComercialOrigem rpmco")
		.append(" left join fetch rp.grupoRegiaoOrigem rpgro")
		.append(" left join fetch rp.zonaByIdZonaDestino rpzd")
		.append(" left join fetch rp.paisByIdPaisDestino rppd")
		.append(" left join fetch rp.unidadeFederativaByIdUfDestino rpufd")
		.append(" left join fetch rp.filialByIdFilialDestino rpfd")
		.append(" left join fetch rp.municipioByIdMunicipioDestino rpmd")
		.append(" left join fetch rp.aeroportoByIdAeroportoDestino rpad")
		.append(" left join fetch rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino rptld")
		.append(" left join fetch rp.tipoLocalizacaoMunicipioComercialDestino rpmcd")
		.append(" left join fetch rp.grupoRegiaoDestino rpgrd")
		.append(" WHERE ")
		.append(" tpp.tabelaPreco.idTabelaPreco = :idTabelaPreco ");
		
		if(vigentes){
			query.append(" AND vmpf.dtVigenciaInicial <= :hoje ")
			.append(" AND ( ")
			.append("			vmpf.dtVigenciaFinal is null ")
			.append("		OR 	vmpf.dtVigenciaFinal >= :hoje) ");
			params.put("hoje", JTDateTimeUtils.getDataAtual());
		}
		
		if(notVigentes){
			query.append(" AND (  ")
				 .append(" (vmpf.dtVigenciaInicial > :hoje AND vmpf.dtVigenciaFinal > :hoje) ")
				 .append(" OR ")
				 .append(" (vmpf.dtVigenciaInicial < :hoje AND vmpf.dtVigenciaFinal < :hoje) ")
				 .append("    ) ");
			
			if(!vigentes){
				params.put("hoje", JTDateTimeUtils.getDataAtual());
			}
		}
		
		query.append(" ORDER BY rpufo.sgUnidadeFederativa, rpao.sgAeroporto, rpufd.sgUnidadeFederativa, rpad.sgAeroporto, vmpf.dtVigenciaInicial, vmpf.dtVigenciaFinal ");
		
		params.put("idTabelaPreco", idTabelaPreco);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<ValorMarkupFaixaProgressiva> findValorMarkupFaixaProgressiva(Long idTabelaPreco, boolean vigentes, boolean notVigentes) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder()
		.append(" SELECT vmfp ")
		.append(" FROM " + ValorMarkupFaixaProgressiva.class.getName() + " vmfp ")
		.append(" left join fetch vmfp.markupFaixaProgressiva mfp")
		.append(" left join fetch vmfp.faixaProgressiva fp")
		.append(" left join fetch vmfp.faixaProgressiva.produtoEspecifico produtoEspecifico")
		.append(" left join fetch fp.tabelaPrecoParcela tpp")
		.append(" left join fetch tpp.parcelaPreco pp")
		.append(" left join fetch mfp.rotaPreco rp")
		.append(" left join fetch mfp.tarifaPreco tp")
		.append(" left join fetch rp.zonaByIdZonaOrigem rpzo")
		.append(" left join fetch rp.paisByIdPaisOrigem rppo")
		.append(" left join fetch rp.unidadeFederativaByIdUfOrigem rpufo")
		.append(" left join fetch rp.filialByIdFilialOrigem rpfo")
		.append(" left join fetch rp.municipioByIdMunicipioOrigem rpmo")
		.append(" left join fetch rp.aeroportoByIdAeroportoOrigem rpao")
		.append(" left join fetch rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem rptlo")
		.append(" left join fetch rp.tipoLocalizacaoMunicipioComercialOrigem rpmco")
		.append(" left join fetch rp.grupoRegiaoOrigem rpgro")
		.append(" left join fetch rp.zonaByIdZonaDestino rpzd")
		.append(" left join fetch rp.paisByIdPaisDestino rppd")
		.append(" left join fetch rp.unidadeFederativaByIdUfDestino rpufd")
		.append(" left join fetch rp.filialByIdFilialDestino rpfd")
		.append(" left join fetch rp.municipioByIdMunicipioDestino rpmd")
		.append(" left join fetch rp.aeroportoByIdAeroportoDestino rpad")
		.append(" left join fetch rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino rptld")
		.append(" left join fetch rp.tipoLocalizacaoMunicipioComercialDestino rpmcd")
		.append(" left join fetch rp.grupoRegiaoDestino rpgrd")
		.append(" WHERE ")
		.append(" tpp.tabelaPreco.idTabelaPreco = :idTabelaPreco ");
		
		if(vigentes){
			query.append(" AND mfp.dtVigenciaInicial <= :hoje ")
				 .append(" AND ( ")
				 .append("			mfp.dtVigenciaFinal is null ")
				 .append("		OR 	mfp.dtVigenciaFinal >= :hoje) ");
			params.put("hoje", JTDateTimeUtils.getDataAtual());
		}
		
		if(notVigentes){
			query.append(" AND (  ")
				 .append(" (mfp.dtVigenciaInicial > :hoje AND mfp.dtVigenciaFinal > :hoje) ")
				 .append(" OR ")
				 .append(" (mfp.dtVigenciaInicial < :hoje AND mfp.dtVigenciaFinal < :hoje) ")
				 .append("    ) ");
			
			if(!vigentes){
				params.put("hoje", JTDateTimeUtils.getDataAtual());
			}
		}
		
		query.append(" ORDER BY rpufo.sgUnidadeFederativa, rpao.sgAeroporto, rpufd.sgUnidadeFederativa, rpad.sgAeroporto, mfp.dtVigenciaInicial, mfp.dtVigenciaFinal ");
		
		params.put("idTabelaPreco", idTabelaPreco);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<MarkupFaixaProgressiva> findMarkupFaixaProgressivaByIds(List<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder()
		.append(" SELECT mfp ")
		.append(" FROM " + MarkupFaixaProgressiva.class.getName() + " mfp ")
		.append(" WHERE ")
		.append(" mfp.idMarkupFaixaProgressiva in (:ids) ");
		
		params.put("ids", ids);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<MarkupFaixaProgressiva> findMarkupFaixaProgressivaByIdsValorMarkupFaixaProgressiva(List<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder()
		.append(" SELECT DISTINCT mfp ")
		.append(" FROM " + MarkupFaixaProgressiva.class.getName() + " mfp ")
		.append(" LEFT JOIN FETCH mfp.valoresMarkupFaixaProgressiva vmfp ")
		.append(" WHERE ")
		.append(" vmfp.idValorMarkupFaixaProgressiva in (:ids) ");
		
		params.put("ids", ids);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}

	public void removeAllMarkupFaixaProgressiva(List<MarkupFaixaProgressiva> listMarkupFaixaProgressiva) {
		getAdsmHibernateTemplate().deleteAll(listMarkupFaixaProgressiva);
	}

	public void removeValorMarkupFaixaProgressiva(List<Long> ids) {
		StringBuilder query = new StringBuilder("delete from ").append(ValorMarkupFaixaProgressiva.class.getName()).append(" as m ")
			.append(" where m.idValorMarkupFaixaProgressiva in (:id)");
		getAdsmHibernateTemplate().removeByIds(query.toString(), ids);
		
	}
	
	public void removePrecosFrete(List<Long> ids) {
		StringBuilder query = new StringBuilder("delete from ").append(ValorMarkupPrecoFrete.class.getName()).append(" as m ")
			.append(" where m.idValorMarkupPrecoFrete in (:id)");
		getAdsmHibernateTemplate().removeByIds(query.toString(), ids);
		
	}

	public void storeAllPrecoFrete(List<ValorMarkupPrecoFrete> entidadesPrecoFrete) {
		for (ValorMarkupPrecoFrete valorMarkupPrecoFrete : entidadesPrecoFrete) {
			if(valorMarkupPrecoFrete.getDtVigenciaFinal() == null){
				valorMarkupPrecoFrete.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
			}
		}
		getAdsmHibernateTemplate().saveOrUpdateAll(entidadesPrecoFrete);
	}
	
	public void storeAllFaixaProgressiva(List<MarkupFaixaProgressiva> entidadesFaixaProgressiva) {
		for (MarkupFaixaProgressiva markupFaixaProgressiva : entidadesFaixaProgressiva) {
			if(markupFaixaProgressiva.getDtVigenciaFinal() == null){
				markupFaixaProgressiva.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
			}
		}
		getAdsmHibernateTemplate().saveOrUpdateAll(entidadesFaixaProgressiva);
	}

}
