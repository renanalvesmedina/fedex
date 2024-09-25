package com.mercurio.lms.municipios.model.dao;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.McdMunicipioFilial;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class McdMunicipioFilialDAO extends BaseCrudDao<McdMunicipioFilial, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return McdMunicipioFilial.class;
    }

    public ResultSetPage findPaginatedToConsultarMcd(TypedFlatMap criteria, FindDefinition fdef) {
    	SqlTemplate sql = getSqlTemplateToConsultarMcd(criteria);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),fdef.getCurrentPage(),fdef.getPageSize(),sql.getCriteria());
    }
    
    public Integer getRowCountToConsultarMcd(TypedFlatMap criteria) {
    	SqlTemplate sql = getSqlTemplateToConsultarMcd(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    }

    private SqlTemplate getSqlTemplateToConsultarMcd(TypedFlatMap criteria) {
    	SqlTemplate sql = new SqlTemplate();
    	
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	
    	StringBuffer hqlProjection = new StringBuffer()
    		.append("new Map(")
    		.append("	S.dsServico as servico_dsServico, ")
    		.append("	MO.nmMunicipio as nmMunicipioOrigem, ")
    		.append("	MO.blDistrito as blDistritoOrigem, ")
    		.append("	UFO.sgUnidadeFederativa as sgUnidadeFederativaOrigem, ")	
    		.append("	PO.nmPais as nmPaisOrigem, ")
    		
    		.append("	FO.sgFilial as sgFilialOrigem, ")
    		.append("	PEO.nmFantasia as nmFilialOrigem, ")
    		
    		.append("	mcdMF.blDomingoOrigem as blDomingoOrigem, ")
    		.append("	mcdMF.blSegundaOrigem as blSegundaOrigem, ")
    		.append("	mcdMF.blTercaOrigem as blTercaOrigem, ")
    		.append("	mcdMF.blQuartaOrigem as blQuartaOrigem, ")
    		.append("	mcdMF.blQuintaOrigem as blQuintaOrigem, ")
    		.append("	mcdMF.blSextaOrigem as blSextaOrigem, ")
    		.append("	mcdMF.blSabadoOrigem as blSabadoOrigem, ")
    		
    		.append("	MD.nmMunicipio as nmMunicipioDestino, ")
    		.append("	MD.blDistrito as blDistritoDestino, ")
    		.append("	UFD.sgUnidadeFederativa as sgUnidadeFederativaDestino, ")
    		.append("	PD.nmPais as nmPaisDestino, ")
    		
    		.append("	FD.sgFilial as sgFilialDestino, ")
    		.append("	PED.nmFantasia as nmFilialDestino, ")
    		
    		.append("	mcdMF.blDomingoDestino as blDomingoDestino, ")
    		.append("	mcdMF.blSegundaDestino as blSegundaDestino, ")
    		.append("	mcdMF.blTercaDestino as blTercaDestino, ")
    		.append("	mcdMF.blQuartaDestino as blQuartaDestino, ")
    		.append("	mcdMF.blQuintaDestino as blQuintaDestino, ")
    		.append("	mcdMF.blSextaDestino as blSextaDestino, ")
    		.append("	mcdMF.blSabadoDestino as blSabadoDestino, ")
    		
    		.append("	TP.cdTarifaPreco as tarifa_cdTarifa, ")
    		.append("	mcdMF.qtPedagio as qtPedagio, ");
    	
    		if (criteria.getString("tpEmissao").equals("D")) 
    			hqlProjection.append(" (ceil(mcdMF.nrPpe / 24)) as nrPpe");
    		else
    			hqlProjection.append("mcdMF.nrPpe as nrPpe");

    		hqlProjection.append(")");
    	
    	sql.addProjection(hqlProjection.toString());
    	
    	StringBuffer hqlFrom = new StringBuffer()
    		.append(McdMunicipioFilial.class.getName() + " mcdMF ")
    		.append(" inner join mcdMF.mcd as mcd ")
    		.append(" inner join mcdMF.municipioFilialByIdMunicipioFilialOrigem as MFO ")
    		.append(" inner join MFO.filial as FO ")
    		.append(" inner join FO.pessoa as PEO ")
    		.append(" inner join MFO.municipio as MO ")
    		.append(" inner join MO.unidadeFederativa as UFO ")
    		.append(" inner join UFO.pais as PO ")
    		
    		.append(" inner join mcdMF.municipioFilialByIdMunicipioFilialDestino as MFD ")
    		.append(" inner join MFD.filial as FD ")
    		.append(" inner join FD.pessoa as PED ")
    		.append(" inner join MFD.municipio as MD ")
    		.append(" inner join MD.unidadeFederativa as UFD ")
    		.append(" inner join UFD.pais as PD ")
    		
    		.append(" inner join mcdMF.servico as S")
    		.append(" inner join mcdMF.tarifaPreco as TP ");
    	
    	sql.addFrom(hqlFrom.toString()); 
    	
    	sql.addCriteria("mcd.dtVigenciaInicial","<=",dataAtual);
    	sql.addCriteria("mcd.dtVigenciaFinal",">=",dataAtual);
    	
    	sql.addCriteria("S.idServico","=",criteria.getLong("servico.idServico"));
    	sql.addCriteria("MO.idMunicipio","=",criteria.getLong("municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio"));
    	sql.addCriteria("FO.idFilial","=",criteria.getLong("municipioFilialByIdMunicipioFilialOrigem.filial.idFilial"));
    	sql.addCriteria("MD.idMunicipio","=",criteria.getLong("municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio"));
    	sql.addCriteria("FD.idFilial","=",criteria.getLong("municipioFilialByIdMunicipioFilialDestino.filial.idFilial"));
    	
	   	sql.addOrderBy("MO.nmMunicipio");
    	sql.addOrderBy("MD.nmMunicipio");
    	
    	return sql;
    }
    
    public void deleteByIdMcd(Serializable idMcd){
    	String sql = new StringBuffer("delete from ")
    	  				.append(getPersistentClass().getName())
    	  				.append(" as mmf ")
    	  				.append("where ")
    	  				.append("mmf.mcd.id = :id").toString();

    	getAdsmHibernateTemplate().removeById(sql, idMcd);
    }
}