package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.PotencialComercialCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PotencialComercialClienteDAO extends BaseCrudDao<PotencialComercialCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PotencialComercialCliente.class;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		lazyFindPaginated.put("unidadeFederativaByIdUfDestino", FetchMode.JOIN);
		lazyFindPaginated.put("moeda", FetchMode.JOIN);
		lazyFindPaginated.put("paisOrigem", FetchMode.JOIN);
		lazyFindPaginated.put("paisDestino", FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("moeda", FetchMode.JOIN);
		lazyFindById.put("paisOrigem", FetchMode.JOIN);
		lazyFindById.put("paisDestino", FetchMode.JOIN);
	}

	/**
	 * Método que monta a query de busca dos dados para a listagem 
	 * @param criteria Critérios de pesquisa
	 * @return SqlTemplate com a query e os dados
	 */
	private void montaQuery(TypedFlatMap criteria, StringBuilder sql) {
		Long idCliente = criteria.getLong("cliente.idCliente");
		String tpFrete = criteria.getString("tpFrete");
		String tpModal = criteria.getString("tpModal");
		String tpAbrangencia = criteria.getString("tpAbrangencia");

		sql.append("     from PotencialComercialCliente as pcc");
		sql.append("          inner join pcc.cliente cliente");
		sql.append("          left outer join pcc.paisOrigem paisOrigem");
		sql.append("          left outer join pcc.unidadeFederativaByIdUfOrigem unidadeFederativaByIdUfOrigem");
		sql.append("          left outer join pcc.tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem");
		sql.append("          left outer join pcc.paisDestino paisDestino");  
		sql.append("          left outer join pcc.unidadeFederativaByIdUfDestino unidadeFederativaByIdUfDestino");
		sql.append("          left outer join pcc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino");
		sql.append("          left outer join pcc.moeda moeda");
		sql.append("    where pcc.cliente.idCliente = ").append(idCliente);
		if(!StringUtils.isBlank(tpFrete)) {
			sql.append("   and pcc.tpFrete = '").append(tpFrete).append("'");
		}
		if(!StringUtils.isBlank(tpModal)) {
			sql.append("   and pcc.tpModal = '").append(tpModal).append("'");
		}
		if(!StringUtils.isBlank(tpAbrangencia)) {
			sql.append("   and pcc.tpAbrangencia = '").append(tpAbrangencia).append("'");
		}
		sql.append(" order by unidadeFederativaByIdUfOrigem.sgUnidadeFederativa,");
		sql.append("          unidadeFederativaByIdUfDestino.sgUnidadeFederativa,");
		sql.append("          pcc.tpFrete,");
		sql.append("          pcc.tpModal,");
		sql.append("          pcc.tpAbrangencia");
	}

	/**
	 * Conta quantos registros serão mostrados na grid
	 * @param criteria Critérios de Pesquisa
	 * @return Quantidade de registros que serão mostrados na grid
	 */
	public Integer getRowCount(TypedFlatMap criteria) {

		StringBuilder sql = new StringBuilder(); 
		montaQuery(criteria, sql);

		return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString());

	}

	/**
	 * Reconfiguração do findPaginated padrão.
	 * para ordernar pelas descrições dos dominios.
	 * @return ResultSetPage Contendo um map com os dados necessários para grid
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		StringBuilder sql = new StringBuilder();

		sql.append("select new Map(pcc.id as idPotencialComercialCliente,");
		sql.append("       paisOrigem.nmPais as nmPaisOrigem,");
		sql.append("       unidadeFederativaByIdUfOrigem.sgUnidadeFederativa as sgUfOrigem,");
		sql.append("       tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio as tipoLocalizacaoOrigem,");
		sql.append("       paisDestino.nmPais as nmPaisDestino,");
		sql.append("       unidadeFederativaByIdUfDestino.sgUnidadeFederativa as sgUfDestino, ");
		sql.append("       tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio as tipoLocalizacaoDestino,");
		sql.append("       pcc.tpFrete as tpFrete,");
		sql.append("       pcc.tpModal as tpModal,");
		sql.append("       pcc.tpAbrangencia as tpAbrangencia,");
		sql.append("       pcc.dsTransportadora as dsTransportadora,");
		sql.append("       pcc.pcDetencao as pcDetencao,");
		sql.append("       moeda.sgMoeda || ' ' || moeda.dsSimbolo as sgSimboloMoeda,");
		sql.append("       pcc.vlFaturamentoPotencial as vlFaturamentoPotencial");
		sql.append("       ) ");
		montaQuery(criteria, sql);

	   	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), new Object[]{});

		return rsp;
	}

}