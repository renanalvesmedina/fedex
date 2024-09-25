package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.AjusteTarifa;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class AjusteTarifaDAO extends BaseCrudDao<AjusteTarifa, Long> {

	private static final String uniqueIdentifier = "idAjusteTarifa";
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return AjusteTarifa.class;
	}
	
	public Map detail(Long id) {		
		Map map = (Map) getAdsmHibernateTemplate().find(getHQLforMap().append(" where a.").append(uniqueIdentifier).append(" = ?").toString(), new Object[]{id}).get(0);
		return AliasToNestedMapResultTransformer.getInstance().transformeTupleMap(map);
	}
	
	private StringBuffer getHQLforMap() {
		return new StringBuffer(" select new Map(")
		.append("	a.idAjusteTarifa as idAjusteTarifa,")
		.append("	a.tpAjusteFretePeso as tpAjusteFretePeso,")
		.append("	a.tpAjusteFreteValor as tpAjusteFreteValor,")
		.append("	a.tpValorFretePreso as tpValorFretePreso,")
		.append("	a.vlFretePeso as vlFretePeso,")
		.append("	a.vlFreteValor as vlFreteValor,")
		.append("	a.unidadeFederativaByIdUfOrigem.nmUnidadeFederativa as unidadeFederativaByIdUfOrigem_nmUnidadeFederativa,")
		.append("	a.unidadeFederativaByIdUfOrigem.idUnidadeFederativa as unidadeFederativaByIdUfOrigem_idUnidadeFederativa,")
		.append("	a.unidadeFederativaByIdUfOrigem.sgUnidadeFederativa as unidadeFederativaByIdUfOrigem_sgUnidadeFederativa,")
		.append("	a.municipioByIdMunicipioOrigem.idMunicipio as municipioByIdMunicipioOrigem_idMunicipio,")
		.append("	a.municipioByIdMunicipioOrigem.nmMunicipio as municipioByIdMunicipioOrigem_nmMunicipio,")
		.append("	a.filialByIdFilialOrigem.idFilial as filialByIdFilialOrigem_idFilial,")
		.append("	a.filialByIdFilialOrigem.sgFilial as filialByIdFilialOrigem_sgFilial,")
		.append("	a.filialByIdFilialOrigem.pessoa.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia,")
		.append("	a.unidadeFederativaByIdUfDestino.nmUnidadeFederativa as unidadeFederativaByIdUfDestino_nmUnidadeFederativa,")
		.append("	a.unidadeFederativaByIdUfDestino.idUnidadeFederativa as unidadeFederativaByIdUfDestino_idUnidadeFederativa,")
		.append("	a.unidadeFederativaByIdUfDestino.sgUnidadeFederativa as unidadeFederativaByIdUfDestino_sgUnidadeFederativa,")
		.append("	a.municipioByIdMunicipioDestino.idMunicipio as municipioByIdMunicipioDestino_idMunicipio,")
		.append("	a.municipioByIdMunicipioDestino.nmMunicipio as municipioByIdMunicipioDestino_nmMunicipio,")
		.append("	a.filialByIdFilialDestino.idFilial as filialByIdFilialDestino_idFilial,")
		.append("	a.filialByIdFilialDestino.sgFilial as filialByIdFilialDestino_sgFilial,")
		.append("	a.filialByIdFilialDestino.pessoa.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia,")
		.append("	a.tabelaPrecoByIdTabelaPreco.dsDescricao as tabelaPrecoByIdTabelaPreco_dsDescricao,")
		.append("	a.tabelaPrecoByIdTabelaPreco.idTabelaPreco as tabelaPrecoByIdTabelaPreco_idTabelaPreco,")
		.append("	a.tabelaPrecoByIdTabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco as tabelaPrecoByIdTabelaPreco_subtipoTabelaPreco_tpSubtipoTabelaPreco,")
		.append("	a.tabelaPrecoByIdTabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco as tabelaPrecoByIdTabelaPreco_tipoTabelaPreco_tpTipoTabelaPreco,")
		.append("	a.tabelaPrecoByIdTabelaPreco.tipoTabelaPreco.nrVersao as tabelaPrecoByIdTabelaPreco_tipoTabelaPreco_nrVersao")
		.append(" ) ")
		.append(" from ")
		.append("	AjusteTarifa a  ")
		.append("	left join a.unidadeFederativaByIdUfOrigem uf1")
		.append("	left join a.unidadeFederativaByIdUfDestino uf2")
		.append("	left join a.municipioByIdMunicipioOrigem mu1")
		.append("	left join a.municipioByIdMunicipioDestino mu2")
		.append("	left join a.filialByIdFilialOrigem fi1")
		.append("	left join a.filialByIdFilialOrigem.pessoa pe1")
		.append("	left join a.filialByIdFilialDestino fi2")
		.append("	left join a.filialByIdFilialDestino.pessoa pe2")
		.append("	join a.tabelaPrecoByIdTabelaPreco tab")
		.append("	join a.tabelaPrecoByIdTabelaPreco.tipoTabelaPreco tp")
		.append("	join a.tabelaPrecoByIdTabelaPreco.subtipoTabelaPreco subtp");
	}

	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("unidadeFederativaByIdUfOrigem",	FetchMode.JOIN);
		lazyFindPaginated.put("unidadeFederativaByIdUfOrigem",	FetchMode.JOIN);
		lazyFindPaginated.put("unidadeFederativaByIdUfDestino",	FetchMode.JOIN);
		lazyFindPaginated.put("municipioByIdMunicipioOrigem",	FetchMode.JOIN);
		lazyFindPaginated.put("municipioByIdMunicipioDestino",	FetchMode.JOIN);
		lazyFindPaginated.put("filialByIdFilialOrigem",			FetchMode.JOIN);
		lazyFindPaginated.put("filialByIdFilialDestino",		FetchMode.JOIN);
		lazyFindPaginated.put("tabelaPrecoByIdTabelaPreco",						FetchMode.JOIN);
		lazyFindPaginated.put("tabelaPrecoByIdTabelaPreco.tipoTabelaPreco",		FetchMode.JOIN);
		lazyFindPaginated.put("tabelaPrecoByIdTabelaPreco.subtipoTabelaPreco",	FetchMode.JOIN);
		
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}

	
	public AjusteTarifa findByRota(RestricaoRota origem, RestricaoRota destino, TabelaPreco tabelaPreco) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(AjusteTarifa.class, "ajuste");
		
		if(origem != null){
		dc.add(Restrictions.eq("ajuste.filialByIdFilialOrigem.id", origem.getIdFilial()));
		dc.add(Restrictions.eq("ajuste.municipioByIdMunicipioOrigem.id", origem.getIdMunicipio()));
		dc.add(Restrictions.eq("ajuste.unidadeFederativaByIdUfOrigem.id", origem.getIdUnidadeFederativa()));
		}	
		
		if(destino != null){
		dc.add(Restrictions.eq("ajuste.filialByIdFilialDestino.id", destino.getIdFilial()));
		dc.add(Restrictions.eq("ajuste.municipioByIdMunicipioDestino.id", destino.getIdMunicipio()));
		dc.add(Restrictions.eq("ajuste.unidadeFederativaByIdUfDestino.id", destino.getIdUnidadeFederativa()));
		}

		if(tabelaPreco != null){
			dc.add(Restrictions.eq("ajuste.tabelaPrecoByIdTabelaPreco.id", tabelaPreco.getIdTabelaPreco()));
		}
		
		List<AjusteTarifa> list = findByDetachedCriteria(dc);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		
		return null;
	}	
	
}