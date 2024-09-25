package com.mercurio.lms.tabelaprecos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicaÁ„o
 * atravÈs do suporte ao Hibernate em conjunto com o Spring.
 * N„o inserir documentaÁ„o apÛs ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class RotaPrecoDAO extends BaseCrudDao<RotaPreco, Long> {

	/**
	 * Nome da classe que o DAO È respons·vel por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return RotaPreco.class;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition def) {
		StringBuilder sqlProjection = new StringBuilder();
		sqlProjection.append("new map(rp.idRotaPreco as idRotaPreco, ");
		sqlProjection.append("zo.dsZona as zonaByIdZonaOrigem_dsZona, ");
		sqlProjection.append("zd.dsZona as zonaByIdZonaDestino_dsZona, ");
		sqlProjection.append("paiso.nmPais as paisByIdPaisOrigem_nmPais, ");
		sqlProjection.append("paisd.nmPais as paisByIdPaisDestino_nmPais, ");
		sqlProjection.append("ufo.sgUnidadeFederativa as unidadeFederativaByIdUfOrigem_sgUnidadeFederativa, ");
		sqlProjection.append("ufd.sgUnidadeFederativa as unidadeFederativaByIdUfDestino_sgUnidadeFederativa, ");
		sqlProjection.append("fo.sgFilial as filialByIdFilialOrigem_sgFilial, ");
		sqlProjection.append("fd.sgFilial as filialByIdFilialDestino_sgFilial, ");
		sqlProjection.append("mo.nmMunicipio as municipioByIdMunicipioOrigem_nmMunicipio, ");
		sqlProjection.append("md.nmMunicipio as municipioByIdMunicipioDestino_nmMunicipio, ");
		sqlProjection.append("ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto, ");
		sqlProjection.append("ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto, ");
		sqlProjection.append("tlo.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_dsTipoLocalizacaoMunicipio, ");
		sqlProjection.append("tld.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_dsTipoLocalizacaoMunicipio, ");
		sqlProjection.append("tlco.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioComercialOrigem_dsTipoLocalizacaoMunicipio, ");
		sqlProjection.append("tlcd.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioComercialDestino_dsTipoLocalizacaoMunicipio, ");
		sqlProjection.append("gro.dsGrupoRegiao as grupoRegiaoOrigem_dsGrupoRegiao,  ");
		sqlProjection.append("grd.dsGrupoRegiao as grupoRegiaoDestino_dsGrupoRegiao) ");

		StringBuilder sqlFrom = new StringBuilder();
		sqlFrom.append(RotaPreco.class.getName()).append(" as rp ");
		sqlFrom.append("left join rp.unidadeFederativaByIdUfOrigem as ufo ");
		sqlFrom.append("left join rp.filialByIdFilialOrigem as fo ");
		sqlFrom.append("left join rp.municipioByIdMunicipioOrigem as mo ");
		sqlFrom.append("left join rp.aeroportoByIdAeroportoOrigem as ao ");
		sqlFrom.append("left join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo ");
		sqlFrom.append("left join rp.tipoLocalizacaoMunicipioComercialOrigem as tlco ");
		sqlFrom.append("left join rp.unidadeFederativaByIdUfDestino as ufd ");
		sqlFrom.append("left join rp.filialByIdFilialDestino as fd ");
		sqlFrom.append("left join rp.municipioByIdMunicipioDestino as md ");
		sqlFrom.append("left join rp.aeroportoByIdAeroportoDestino as ad ");
		sqlFrom.append("left join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld ");
		sqlFrom.append("left join rp.tipoLocalizacaoMunicipioComercialDestino as tlcd ");
		sqlFrom.append("left join rp.zonaByIdZonaDestino as zd ");
		sqlFrom.append("left join rp.zonaByIdZonaOrigem as zo ");
		sqlFrom.append("left join rp.paisByIdPaisDestino as paisd ");
		sqlFrom.append("left join rp.paisByIdPaisOrigem as paiso ");
		sqlFrom.append("left join rp.grupoRegiaoOrigem  as gro ");
		sqlFrom.append("left join rp.grupoRegiaoDestino as grd ");


		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(sqlProjection.toString());

		sql.addFrom(sqlFrom.toString());

		sql.addCriteria("zo.id","=", criteria.getLong("zonaByIdZonaOrigem.idZona"));
		sql.addCriteria("paiso.id", "=", criteria.getLong("paisByIdPaisOrigem.idPais"));
		sql.addCriteria("ufo.id", "=", criteria.getLong("unidadeFederativaByIdUfOrigem.idUnidadeFederativa"));
		sql.addCriteria("fo.id", "=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("mo.id", "=", criteria.getLong("municipioByIdMunicipioOrigem.idMunicipio"));
		sql.addCriteria("ao.id", "=", criteria.getLong("aeroportoByIdAeroportoOrigem.idAeroporto"));
		sql.addCriteria("tlo.id", "=", criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"));
		sql.addCriteria("tlco.id", "=", criteria.getLong("tipoLocalizacaoMunicipioComercialOrigem.idTipoLocalizacaoMunicipio"));
		sql.addCriteria("zd.id", "=", criteria.getLong("zonaByIdZonaDestino.idZona"));
		sql.addCriteria("paisd.id", "=", criteria.getLong("paisByIdPaisDestino.idPais"));
		sql.addCriteria("ufd.id", "=", criteria.getLong("unidadeFederativaByIdUfDestino.idUnidadeFederativa"));
		sql.addCriteria("fd.id", "=", criteria.getLong("filialByIdFilialDestino.idFilial"));
		sql.addCriteria("md.id", "=", criteria.getLong("municipioByIdMunicipioDestino.idMunicipio"));
		sql.addCriteria("ad.id", "=", criteria.getLong("aeroportoByIdAeroportoDestino.idAeroporto"));
		sql.addCriteria("tld.id", "=", criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"));
		sql.addCriteria("tlcd.id", "=", criteria.getLong("tipoLocalizacaoMunicipioComercialDestino.idTipoLocalizacaoMunicipio"));
		sql.addCriteria("rp.tpSituacao","=", criteria.getString("tpSituacao"));
		sql.addCriteria("gro.id","=", criteria.getLong("grupoRegiaoOrigem.idGrupoRegiao"));
		sql.addCriteria("grd.id","=", criteria.getLong("grupoRegiaoDestino.idGrupoRegiao"));


		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zo.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("paiso.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufo.sgUnidadeFederativa");
		sql.addOrderBy("fo.sgFilial");
		sql.addOrderBy("mo.nmMunicipio");
		sql.addOrderBy("ao.sgAeroporto");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tlo.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tlco.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));
		sql.addOrderBy("gro.dsGrupoRegiao");

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zd.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("paisd.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufd.sgUnidadeFederativa");
		sql.addOrderBy("fd.sgFilial");
		sql.addOrderBy("md.nmMunicipio");
		sql.addOrderBy("ad.sgAeroporto");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tld.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tlcd.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));
		sql.addOrderBy("grd.dsGrupoRegiao");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true),def.getCurrentPage(),def.getPageSize(),sql.getCriteria());
	}

	@Override
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("zonaByIdZonaOrigem", FetchMode.JOIN);
		fetchModes.put("paisByIdPaisOrigem", FetchMode.JOIN);
		fetchModes.put("unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialOrigem", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		fetchModes.put("municipioByIdMunicipioOrigem", FetchMode.JOIN);
		fetchModes.put("aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
		fetchModes.put("aeroportoByIdAeroportoOrigem.pessoa", FetchMode.JOIN);
		fetchModes.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem", FetchMode.JOIN);
		fetchModes.put("grupoRegiaoOrigem", FetchMode.JOIN);

		fetchModes.put("zonaByIdZonaDestino", FetchMode.JOIN);
		fetchModes.put("paisByIdPaisDestino", FetchMode.JOIN);
		fetchModes.put("unidadeFederativaByIdUfDestino", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialDestino", FetchMode.JOIN);
		fetchModes.put("filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		fetchModes.put("municipioByIdMunicipioDestino", FetchMode.JOIN);
		fetchModes.put("aeroportoByIdAeroportoDestino", FetchMode.JOIN);
		fetchModes.put("aeroportoByIdAeroportoDestino.pessoa", FetchMode.JOIN);
		fetchModes.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino", FetchMode.JOIN);
		fetchModes.put("grupoRegiaoDestino", FetchMode.JOIN);
	}

	public Map findRotaById(Long idRota){
		Map map = (Map)getAdsmHibernateTemplate().findByNamedQueryAndNamedParam(RotaPreco.FIND_ROTA_PRECO_BY_ID, "idRota", idRota).get(0);
		return AliasToNestedMapResultTransformer.getInstance().transformeTupleMap(map);
	}

	/**
	 * Verifica se existe uma rotaPreco para os dados de origem/destino
	 *
	 * @param restricaoRotaOrigem
	 * @param restricaoRotaDestino
	 * @return
	 */
	public boolean checkRotaByRestricaoRotaOrigemDestino(RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino){
		StringBuilder sql = new StringBuilder();
		Map<String, Object> criteria = new HashMap<String, Object>();
		sql.append("from ").append(RotaPreco.class.getName()).append(" rp ");
		sql.append(" where 1=1 ");

		//Origem Rota
		if (restricaoRotaOrigem.getIdZona() != null){
			sql.append(" and rp.zonaByIdZonaOrigem.id = :idZonaOrigem");
			criteria.put("idZonaOrigem", restricaoRotaOrigem.getIdZona());
		}
		if(restricaoRotaOrigem.getIdPais()!= null){
			sql.append(" and (rp.paisByIdPaisOrigem.id =:idPaisOrigem OR paisByIdPaisOrigem is null)");
			criteria.put("idPaisOrigem", restricaoRotaOrigem.getIdPais());
		}
		if(restricaoRotaOrigem.getIdUnidadeFederativa()!= null){
			sql.append(" and (rp.unidadeFederativaByIdUfOrigem.id =:idUfOrigem OR rp.unidadeFederativaByIdUfOrigem is null)");
			criteria.put("idUfOrigem", restricaoRotaOrigem.getIdUnidadeFederativa());
		}
		if(restricaoRotaOrigem.getIdTipoLocalizacao()!= null){
			sql.append(" and (rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.id =:idTipoLocalizacaoOrigem OR rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem is null)");
			criteria.put("idTipoLocalizacaoOrigem", restricaoRotaOrigem.getIdTipoLocalizacao());
		}

		if(restricaoRotaOrigem.getIdFilial()!= null){
			sql.append(" and (rp.filialByIdFilialOrigem.id = :idFilialOrigem OR rp.filialByIdFilialOrigem is null)");
			criteria.put("idFilialOrigem", restricaoRotaOrigem.getIdFilial());
		}

		if(restricaoRotaOrigem.getIdMunicipio()!= null){
			sql.append(" and (rp.municipioByIdMunicipioOrigem.id = :idMunicipioOrigem OR rp.municipioByIdMunicipioOrigem is null)");
			criteria.put("idMunicipioOrigem", restricaoRotaOrigem.getIdMunicipio());
		}


		if(restricaoRotaDestino.getIdTipoLocalizacao()!= null){
			sql.append(" and rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.id =:idTipoLocalizacaoDestino  ");
			criteria.put("idTipoLocalizacaoDestino", restricaoRotaDestino.getIdTipoLocalizacao());
		}

		if(restricaoRotaDestino.getIdZona()!= null){
			sql.append(" and rp.zonaByIdZonaDestino.id = :idZonaDestino");
			criteria.put("idZonaDestino", restricaoRotaDestino.getIdZona());
		}

		if(restricaoRotaDestino.getIdPais()!= null){
			sql.append(" and (rp.paisByIdPaisDestino.id =:idPaisDestino  OR rp.paisByIdPaisDestino is null)");
			criteria.put("idPaisDestino", restricaoRotaDestino.getIdPais());
		}

		if(restricaoRotaDestino.getIdUnidadeFederativa()!= null){
			sql.append(" and (rp.unidadeFederativaByIdUfDestino.id  =:idUFDestino OR rp.unidadeFederativaByIdUfDestino is null)");
			criteria.put("idUFDestino", restricaoRotaDestino.getIdUnidadeFederativa());
		}

		if(restricaoRotaDestino.getIdFilial()!= null){
			sql.append(" and (rp.filialByIdFilialDestino.id = :idFilialDestino OR rp.filialByIdFilialDestino is null)");
			criteria.put("idFilialDestino", restricaoRotaDestino.getIdFilial());
		}

		if(restricaoRotaDestino.getIdMunicipio()!= null){
			sql.append(" and (rp.municipioByIdMunicipioDestino.id = :idMunicipioDestino OR rp.municipioByIdMunicipioDestino is null)");
			criteria.put("idMunicipioDestino", restricaoRotaDestino.getIdMunicipio());
		}

		List result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);

		return result != null && !result.isEmpty();
	}

	public RotaPreco findRotaPrecoParaImportacaoTabelaPreco(Map<String, Object> parametros) {

		Long idTabelaPreco = (Long) parametros.get("idTabelaPreco");
		Long idEmpresaMercurio = (Long) parametros.get("idEmpresaMercurio");
		Long idZonaOrigem = (Long) parametros.get("idZonaOrigem");
		Long idZonaDestino = (Long) parametros.get("idZonaDestino");
		Long idTipoLocalizacaoMunicipioOrigem = (Long) parametros.get("idTipoLocalizacaoMunicipioOrigem");
		Long idTipoLocalizacaoMunicipioDestino = (Long) parametros.get("idTipoLocalizacaoMunicipioDestino");
		Long idTipoLocalizacaoMunicipioComercialOrigem = (Long) parametros.get("idTipoLocalizacaoMunicipioComercialOrigem");
		Long idTipoLocalizacaoMunicipioComercialDestino = (Long) parametros.get("idTipoLocalizacaoMunicipioComercialDestino");

		String dsGrupoRegiaoOrigem = (String) parametros.get("dsGrupoRegiaoOrigem");
		String dsGrupoRegiaoDestino = (String) parametros.get("dsGrupoRegiaoDestino");
		String sgAeroportoOrigem = (String) parametros.get("sgAeroportoOrigem");
		String sgAeroportoDestino = (String) parametros.get("sgAeroportoDestino");
		String sgUnidadeFederativaOrigem = (String) parametros.get("sgUnidadeFederativaOrigem");
		String sgUnidadeFederativaDestino = (String) parametros.get("sgUnidadeFederativaDestino");
		String sgPaisDestino = (String) parametros.get("sgPaisDestino");
		String sgPaisOrigem = (String) parametros.get("sgPaisOrigem");
		String sgFilialOrigem = (String) parametros.get("sgFilialOrigem");
		String sgFilialDestino = (String) parametros.get("sgFilialDestino");
		String nmMunicipioOrigem = (String) parametros.get("nmMunicipioOrigem");
		String nmMunicipioDestino = (String) parametros.get("nmMunicipioDestino");

		StringBuilder sql = new StringBuilder();
		Map<String, Object> criteria = new HashMap<String, Object>();

		sql.append(" select rp ");
		sql.append(" from RotaPreco as rp ");
		if(idZonaOrigem != null){
			sql.append(" join rp.zonaByIdZonaOrigem as zo ");
		}
		if(idZonaDestino != null){
			sql.append(" join rp.zonaByIdZonaDestino as zd ");
		}
		if(idTipoLocalizacaoMunicipioOrigem != null){
			sql.append(" join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlmo ");
		}
		if(idTipoLocalizacaoMunicipioDestino != null){
			sql.append(" join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tlmd ");
		}
		if(idTipoLocalizacaoMunicipioComercialOrigem != null){
			sql.append(" join rp.tipoLocalizacaoMunicipioComercialOrigem as tlmco ");
		}
		if(idTipoLocalizacaoMunicipioComercialDestino != null){
			sql.append(" join rp.tipoLocalizacaoMunicipioComercialDestino as tlmcd ");
		}
		if(sgAeroportoOrigem != null){
			sql.append(" join rp.aeroportoByIdAeroportoOrigem as ao ");
		}
		if(sgAeroportoDestino != null){
			sql.append(" join rp.aeroportoByIdAeroportoDestino as ad ");
		}
		if(sgUnidadeFederativaOrigem != null){
			sql.append(" join rp.unidadeFederativaByIdUfOrigem as ufo ");
		}
		if(sgUnidadeFederativaDestino != null){
			sql.append(" join rp.unidadeFederativaByIdUfDestino as ufd ");
		}
		if(dsGrupoRegiaoOrigem != null && sgUnidadeFederativaOrigem != null && sgPaisOrigem != null){
			sql.append(" join rp.grupoRegiaoOrigem as gro ");
		}
		if(dsGrupoRegiaoDestino != null && sgUnidadeFederativaDestino != null && sgPaisDestino != null){
			sql.append(" join rp.grupoRegiaoDestino as grd ");
		}
		if(sgFilialOrigem != null){
			sql.append(" join rp.filialByIdFilialOrigem as fo ");
		}
		if(sgFilialDestino != null){
			sql.append(" join rp.filialByIdFilialDestino as fd ");
		}
		if(nmMunicipioOrigem != null){
			sql.append(" join rp.municipioByIdMunicipioOrigem as mo ");
		}
		if(nmMunicipioDestino != null){
			sql.append(" join rp.municipioByIdMunicipioDestino as md ");
		}
		if(sgPaisOrigem != null){
			sql.append(" join rp.paisByIdPaisOrigem as po ");
		}
		if(sgPaisDestino != null){
			sql.append(" join rp.paisByIdPaisDestino as pd ");
		}

		sql.append(" where 1 = 1 ");

		if(idZonaOrigem != null){
			sql.append(" and zo.idZona = :idZonaOrigem ");
			criteria.put("idZonaOrigem", idZonaOrigem);
		}else{
			sql.append(" and rp.zonaByIdZonaOrigem = null ");
		}
		if(idZonaDestino != null){
			sql.append(" and zd.idZona = :idZonaDestino ");
			criteria.put("idZonaDestino", idZonaDestino);
		}else{
			sql.append(" and rp.zonaByIdZonaDestino = null ");
		}
		if(idTipoLocalizacaoMunicipioOrigem != null){
			sql.append(" and tlmo.idTipoLocalizacaoMunicipio = :idTipoLocalizacaoMunicipioOrigem ");
			criteria.put("idTipoLocalizacaoMunicipioOrigem", idTipoLocalizacaoMunicipioOrigem);
		}else{
			sql.append(" and rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem = null ");
		}
		if(idTipoLocalizacaoMunicipioDestino != null){
			sql.append(" and tlmd.idTipoLocalizacaoMunicipio = :idTipoLocalizacaoMunicipioDestino ");
			criteria.put("idTipoLocalizacaoMunicipioDestino", idTipoLocalizacaoMunicipioDestino);
		}else{
			sql.append(" and rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino = null ");
		}
		if(idTipoLocalizacaoMunicipioComercialOrigem != null){
			sql.append(" and tlmco.idTipoLocalizacaoMunicipio = :idTipoLocalizacaoMunicipioComercialOrigem ");
			criteria.put("idTipoLocalizacaoMunicipioComercialOrigem", idTipoLocalizacaoMunicipioComercialOrigem);
		}else{
			sql.append(" and rp.tipoLocalizacaoMunicipioComercialOrigem = null ");
		}
		if(idTipoLocalizacaoMunicipioComercialDestino != null){
			sql.append(" and tlmcd.idTipoLocalizacaoMunicipio = :idTipoLocalizacaoMunicipioComercialDestino ");
			criteria.put("idTipoLocalizacaoMunicipioComercialDestino", idTipoLocalizacaoMunicipioComercialDestino);
		}else{
			sql.append(" and rp.tipoLocalizacaoMunicipioComercialDestino = null ");
		}
		if(sgAeroportoOrigem != null){
			sql.append(" and UPPER(ao.sgAeroporto) = :sgAeroportoOrigem ");
			criteria.put("sgAeroportoOrigem", sgAeroportoOrigem.toUpperCase());
		}else{
			sql.append(" and rp.aeroportoByIdAeroportoOrigem = null ");
		}
		if(sgAeroportoDestino != null){
			sql.append(" and UPPER(ad.sgAeroporto) = :sgAeroportoDestino ");
			criteria.put("sgAeroportoDestino", sgAeroportoDestino.toUpperCase());
		}else{
			sql.append(" and rp.aeroportoByIdAeroportoDestino = null ");
		}
		if(sgUnidadeFederativaOrigem != null){
			sql.append(" and UPPER(ufo.sgUnidadeFederativa) = :sgUnidadeFederativaOrigem ");
			criteria.put("sgUnidadeFederativaOrigem", sgUnidadeFederativaOrigem.toUpperCase());
		}else{
			sql.append(" and rp.unidadeFederativaByIdUfOrigem = null ");
		}
		if(sgUnidadeFederativaDestino != null){
			sql.append(" and UPPER(ufd.sgUnidadeFederativa) = :sgUnidadeFederativaDestino ");
			criteria.put("sgUnidadeFederativaDestino", sgUnidadeFederativaDestino.toUpperCase());
		}else{
			sql.append(" and rp.unidadeFederativaByIdUfDestino = null ");
		}
		if(dsGrupoRegiaoOrigem != null && sgUnidadeFederativaOrigem != null && sgPaisOrigem != null){
			sql.append(" and gro.dsGrupoRegiao = :dsGrupoRegiaoOrigem and gro.tabelaPreco.idTabelaPreco = :idTabelaPrecoGrO ");
			criteria.put("dsGrupoRegiaoOrigem", dsGrupoRegiaoOrigem);
			criteria.put("idTabelaPrecoGrO", idTabelaPreco);
		}else{
			sql.append(" and rp.grupoRegiaoOrigem = null ");
		}
		if(dsGrupoRegiaoDestino != null && sgUnidadeFederativaDestino != null && sgPaisDestino != null){
			sql.append(" and grd.dsGrupoRegiao = :dsGrupoRegiaoDestino and grd.tabelaPreco.idTabelaPreco = :idTabelaPrecoGrD ");
			criteria.put("dsGrupoRegiaoDestino", dsGrupoRegiaoDestino);
			criteria.put("idTabelaPrecoGrD", idTabelaPreco);
		}else{
			sql.append(" and rp.grupoRegiaoDestino = null ");
		}
		if(sgFilialOrigem != null){
			sql.append(" and UPPER(fo.sgFilial) = :sgFilialOrigem ");
			sql.append(" and fo.empresa.idEmpresa = :idEmpresaOrigem ");
			criteria.put("sgFilialOrigem", sgFilialOrigem.toUpperCase());
			criteria.put("idEmpresaOrigem", idEmpresaMercurio);
		}else{
			sql.append(" and rp.filialByIdFilialOrigem = null ");
		}
		if(sgFilialDestino != null){
			sql.append(" and UPPER(fd.sgFilial) = :sgFilialDestino ");
			sql.append(" and fd.empresa.idEmpresa = :idEmpresaDestino ");
			criteria.put("sgFilialDestino", sgFilialDestino.toUpperCase());
			criteria.put("idEmpresaDestino", idEmpresaMercurio);
		}else{
			sql.append(" and rp.filialByIdFilialDestino = null ");
		}
		if(nmMunicipioOrigem != null){
			sql.append(" and translate(upper(mo.nmMunicipio), '¡…Õ”⁄·ÈÌÛ˙«Á¿‡√„’ı¬‚ Í‘Ù', 'AEIOUaeiouCcAaAaOoAaEeOo') = translate(upper(:nmMunicipioOrigem), '¡…Õ”⁄·ÈÌÛ˙«Á¿‡√„’ı¬‚ Í‘Ù', 'AEIOUaeiouCcAaAaOoAaEeOo') ");
			criteria.put("nmMunicipioOrigem", nmMunicipioOrigem.toUpperCase());
		}else{
			sql.append(" and rp.municipioByIdMunicipioOrigem = null ");
		}
		if(nmMunicipioDestino != null){
			sql.append(" and translate(upper(md.nmMunicipio), '¡…Õ”⁄·ÈÌÛ˙«Á¿‡√„’ı¬‚ Í‘Ù', 'AEIOUaeiouCcAaAaOoAaEeOo') = translate(upper(:nmMunicipioDestino), '¡…Õ”⁄·ÈÌÛ˙«Á¿‡√„’ı¬‚ Í‘Ù', 'AEIOUaeiouCcAaAaOoAaEeOo') ");
			criteria.put("nmMunicipioDestino", nmMunicipioDestino.toUpperCase());
		}else{
			sql.append(" and rp.municipioByIdMunicipioDestino = null ");
		}
		if(sgPaisOrigem != null){
			sql.append(" and UPPER(po.sgPais) = :sgPaisOrigem ");
			criteria.put("sgPaisOrigem", sgPaisOrigem.toUpperCase());
		}else{
			sql.append(" and rp.paisByIdPaisOrigem = null ");
		}
		if(sgPaisDestino != null){
			sql.append(" and UPPER(pd.sgPais) = :sgPaisDestino ");
			criteria.put("sgPaisDestino", sgPaisDestino.toUpperCase());
		}else{
			sql.append(" and rp.paisByIdPaisDestino = null ");
		}

		List<RotaPreco> lstRotaPreco = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
		if (lstRotaPreco.size() == 1) {
			return lstRotaPreco.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<RotaPreco> findRotaPrecoParaMarkup(Long idTabelaPreco, String sgUnidadeFederativaOrigem, String sgAeroportoOrigem, String sgUnidadeFederativaDestino, String sgAeroportoDestino, boolean minimoProgressivo) {

		StringBuilder sql = new StringBuilder();
		Map<String, Object> criteria = new HashMap<String, Object>();

		sql.append(" select distinct rp ");
		sql.append(" from RotaPreco as rp ");
		sql.append(" left join fetch rp.zonaByIdZonaOrigem as zo ");
		sql.append(" left join fetch rp.zonaByIdZonaDestino as zd ");
		sql.append(" left join fetch rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlmo ");
		sql.append(" left join fetch rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tlmd ");
		sql.append(" left join fetch rp.tipoLocalizacaoMunicipioComercialOrigem as tlmco ");
		sql.append(" left join fetch rp.tipoLocalizacaoMunicipioComercialDestino as tlmcd ");
		sql.append(" left join fetch rp.aeroportoByIdAeroportoOrigem as ao ");
		sql.append(" left join fetch rp.aeroportoByIdAeroportoDestino as ad ");
		sql.append(" left join fetch rp.unidadeFederativaByIdUfOrigem as ufo ");
		sql.append(" left join fetch rp.unidadeFederativaByIdUfDestino as ufd ");
		sql.append(" left join fetch rp.grupoRegiaoOrigem as gro ");
		sql.append(" left join fetch rp.grupoRegiaoDestino as grd ");
		sql.append(" left join fetch rp.filialByIdFilialOrigem as fo ");
		sql.append(" left join fetch rp.filialByIdFilialDestino as fd ");
		sql.append(" left join fetch rp.municipioByIdMunicipioOrigem as mo ");
		sql.append(" left join fetch rp.municipioByIdMunicipioDestino as md ");
		sql.append(" left join fetch rp.paisByIdPaisOrigem as po ");
		sql.append(" left join fetch rp.paisByIdPaisDestino as pd ");
		sql.append(" where 1 = 1 ");
		
		if(minimoProgressivo){
			sql.append(" and exists ( ");
			sql.append(" 				select 1 from ValorFaixaProgressiva as v ");
			sql.append(" 					join v.faixaProgressiva as f ");
			sql.append(" 					join f.tabelaPrecoParcela as tpp ");
			sql.append(" 					join tpp.tabelaPreco as tp ");
			sql.append(" 				where tp.idTabelaPreco = :idTabelaPreco ");
			sql.append(" 				and v.rotaPreco.idRotaPreco = rp.idRotaPreco ");
			sql.append(" ) ");
		}else{
			sql.append(" and exists ( ");
			sql.append(" 				select 1 from PrecoFrete as pf ");
			sql.append(" 					join pf.tabelaPrecoParcela as tpp ");
			sql.append(" 					join tpp.tabelaPreco as tp ");
			sql.append(" 				where tp.idTabelaPreco = :idTabelaPreco ");
			sql.append(" 				and pf.rotaPreco.idRotaPreco = rp.idRotaPreco ");
			sql.append(" ) ");
		}

		if(StringUtils.isNotEmpty(sgAeroportoOrigem)){
			sql.append(" and UPPER(ao.sgAeroporto) = :sgAeroportoOrigem ");
			criteria.put("sgAeroportoOrigem", sgAeroportoOrigem.toUpperCase());
		}
		if(StringUtils.isNotEmpty(sgAeroportoDestino)){
			sql.append(" and UPPER(ad.sgAeroporto) = :sgAeroportoDestino ");
			criteria.put("sgAeroportoDestino", sgAeroportoDestino.toUpperCase());
		}
		if(StringUtils.isNotEmpty(sgUnidadeFederativaOrigem)){
			sql.append(" and UPPER(ufo.sgUnidadeFederativa) = :sgUnidadeFederativaOrigem ");
			criteria.put("sgUnidadeFederativaOrigem", sgUnidadeFederativaOrigem.toUpperCase());
		}
		if(StringUtils.isNotEmpty(sgUnidadeFederativaDestino)){
			sql.append(" and UPPER(ufd.sgUnidadeFederativa) = :sgUnidadeFederativaDestino ");
			criteria.put("sgUnidadeFederativaDestino", sgUnidadeFederativaDestino.toUpperCase());
		}
		
		criteria.put("idTabelaPreco", idTabelaPreco);

		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
	}

	public RotaPreco findRotaPrecoByIdTarifaPrecoAndIdTabelaPreco(Long idTarifaPreco, Long idTabelaPreco) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select rp from ");
		hql.append(TarifaPrecoRota.class.getName()).append(" as tpr ");
		hql.append(" join tpr.tabelaPreco as tp ");
		hql.append(" join tpr.rotaPreco as rp ");
		hql.append(" where tp.idTabelaPreco = ? ");
		hql.append(" and tpr.tarifaPreco.idTarifaPreco = ? ");

		List resultado = getAdsmHibernateTemplate().find(hql.toString(), new Object[] {idTabelaPreco, idTarifaPreco});

		return !resultado.isEmpty() ? (RotaPreco) resultado.get(0) : null;
	}

}
