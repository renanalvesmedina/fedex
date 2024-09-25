package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.vendas.model.PrazoEntregaCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PrazoEntregaClienteDAO extends BaseCrudDao<PrazoEntregaCliente, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PrazoEntregaCliente.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		lazyFindById.put("unidadeFederativaByIdUfDestino", FetchMode.JOIN);
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("municipioByIdMunicipioOrigem", FetchMode.JOIN);
		lazyFindById.put("municipioByIdMunicipioDestino", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoDestino", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoDestino.pessoa", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoOrigem.pessoa", FetchMode.JOIN);
		lazyFindById.put("servico", FetchMode.JOIN);
		lazyFindById.put("paisByIdPaisDestino", FetchMode.JOIN);
		lazyFindById.put("paisByIdPaisOrigem", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		lazyFindById.put("zonaByIdZonaDestino", FetchMode.JOIN);
		lazyFindById.put("zonaByIdZonaOrigem", FetchMode.JOIN);
		lazyFindById.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino", FetchMode.JOIN);
		lazyFindById.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem", FetchMode.JOIN);		  		
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		StringBuilder sqlFrom = new StringBuilder();
		sqlFrom.append(PrazoEntregaCliente.class.getName()).append(" rp join rp.servico ser join rp.cliente cli ");
		sqlFrom.append(" left join rp.unidadeFederativaByIdUfOrigem as ufo ");
		sqlFrom.append(" left join fetch rp.filialByIdFilialOrigem as fo ");
		sqlFrom.append(" left join rp.municipioByIdMunicipioOrigem as mo ");
		sqlFrom.append(" left join rp.aeroportoByIdAeroportoOrigem as ao ");
		sqlFrom.append(" left join ao.pessoa as po ");
		sqlFrom.append(" left join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as  tlo ");
		sqlFrom.append(" left join rp.unidadeFederativaByIdUfDestino as ufd ");
		sqlFrom.append(" left join fetch rp.filialByIdFilialDestino as fd ");
		sqlFrom.append(" left join rp.municipioByIdMunicipioDestino as md ");
		sqlFrom.append(" left join rp.aeroportoByIdAeroportoDestino as ad ");
		sqlFrom.append(" left join ad.pessoa as pd ");
		sqlFrom.append(" left join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld ");
		sqlFrom.append(" left join rp.zonaByIdZonaDestino as zd ");
		sqlFrom.append(" left join rp.zonaByIdZonaOrigem as zo ");
		sqlFrom.append(" left join rp.paisByIdPaisDestino as paisd ");
		sqlFrom.append(" left join rp.paisByIdPaisOrigem as paiso ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("rp");
		sql.addFrom(sqlFrom.toString());

		sql.addCriteria("rp.servico.id","=", criteria.getLong("servico.idServico"));
		sql.addCriteria("rp.nrPrazo","=", criteria.getLong("nrPrazo"));
		final Long idCliente = criteria.getLong("cliente.idCliente");
		Validate.notNull(idCliente, "idCliente não pode ser nulo");
		sql.addCriteria("rp.cliente.id","=", idCliente);
		//Origem
		sql.addCriteria("rp.zonaByIdZonaOrigem.idZona","=", criteria.getLong("zonaByIdZonaOrigem.idZona"));
		sql.addCriteria("rp.paisByIdPaisOrigem.idPais","=", criteria.getLong("paisByIdPaisOrigem.idPais"));
		sql.addCriteria("ufo.idUnidadeFederativa","=", criteria.getLong("unidadeFederativaByIdUfOrigem.idUnidadeFederativa"));
		sql.addCriteria("fo.idFilial","=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("mo.idMunicipio","=", criteria.getLong("municipioByIdMunicipioOrigem.idMunicipio"));
		sql.addCriteria("ao.idAeroporto","=", criteria.getLong("aeroportoByIdAeroportoOrigem.idAeroporto"));
		sql.addCriteria("tlo.idTipoLocalizacaoMunicipio","=", criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"));
		// Destino
		sql.addCriteria("rp.zonaByIdZonaDestino.idZona","=", criteria.getLong("zonaByIdZonaDestino.idZona"));
		sql.addCriteria("rp.paisByIdPaisDestino.idPais","=", criteria.getLong("paisByIdPaisDestino.idPais"));
		sql.addCriteria("ufd.idUnidadeFederativa","=", criteria.getLong("unidadeFederativaByIdUfDestino.idUnidadeFederativa"));
		sql.addCriteria("fd.idFilial","=", criteria.getLong("filialByIdFilialDestino.idFilial"));
		sql.addCriteria("md.idMunicipio","=", criteria.getLong("municipioByIdMunicipioDestino.idMunicipio"));
		sql.addCriteria("ad.idAeroporto","=", criteria.getLong("aeroportoByIdAeroportoDestino.idAeroporto"));
		sql.addCriteria("tld.idTipoLocalizacaoMunicipio","=", criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"));

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zo.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("paiso.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufo.sgUnidadeFederativa");
		sql.addOrderBy("fo.sgFilial");
		sql.addOrderBy("mo.nmMunicipio");
		sql.addOrderBy("po.nmPessoa");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tlo.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zd.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("paisd.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufd.sgUnidadeFederativa");
		sql.addOrderBy("fd.sgFilial");
		sql.addOrderBy("md.nmMunicipio");
		sql.addOrderBy("pd.nmPessoa");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tld.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));

		final ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
		List<PrazoEntregaCliente> resultado = rsp.getList();
		for (PrazoEntregaCliente prazoEntregaCliente : resultado) {
			Hibernate.initialize(prazoEntregaCliente.getServico());
			Hibernate.initialize(prazoEntregaCliente.getZonaByIdZonaOrigem());
			Hibernate.initialize(prazoEntregaCliente.getZonaByIdZonaDestino());
			Hibernate.initialize(prazoEntregaCliente.getPaisByIdPaisOrigem());
			Hibernate.initialize(prazoEntregaCliente.getPaisByIdPaisDestino());
			Hibernate.initialize(prazoEntregaCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem());
			Hibernate.initialize(prazoEntregaCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino());
			Hibernate.initialize(prazoEntregaCliente.getMunicipioByIdMunicipioOrigem());
			Hibernate.initialize(prazoEntregaCliente.getMunicipioByIdMunicipioDestino());
			Hibernate.initialize(prazoEntregaCliente.getAeroportoByIdAeroportoOrigem());
			if (prazoEntregaCliente.getAeroportoByIdAeroportoOrigem() != null) {
				Hibernate.initialize(prazoEntregaCliente.getAeroportoByIdAeroportoOrigem().getPessoa());
			}
			Hibernate.initialize(prazoEntregaCliente.getAeroportoByIdAeroportoDestino());
			if (prazoEntregaCliente.getAeroportoByIdAeroportoDestino() != null) {
				Hibernate.initialize(prazoEntregaCliente.getAeroportoByIdAeroportoDestino().getPessoa());
			}
			Hibernate.initialize(prazoEntregaCliente.getUnidadeFederativaByIdUfOrigem());
			Hibernate.initialize(prazoEntregaCliente.getUnidadeFederativaByIdUfDestino());
		}
		return rsp;
	}
	
	public PrazoEntregaCliente findPrazoEntregaCliente(Long idCliente, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		return findPrazoEntregaClienteRecursivo(idCliente, idServico, restricaoRotaOrigem, restricaoRotaDestino, 0);
	}

	private PrazoEntregaCliente findPrazoEntregaClienteRecursivo(Long idCliente, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino, Integer cont) {
		StringBuilder sqlFrom = new StringBuilder();
		sqlFrom.append(PrazoEntregaCliente.class.getName()).append(" rp join rp.servico ser join rp.cliente cli ");
		sqlFrom.append(" left join rp.unidadeFederativaByIdUfOrigem as ufo ");
		sqlFrom.append(" left join fetch rp.filialByIdFilialOrigem as fo ");
		sqlFrom.append(" left join rp.municipioByIdMunicipioOrigem as mo ");
		sqlFrom.append(" left join rp.aeroportoByIdAeroportoOrigem as ao ");
		sqlFrom.append(" left join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as  tlo ");
		sqlFrom.append(" left join rp.unidadeFederativaByIdUfDestino as ufd ");
		sqlFrom.append(" left join fetch rp.filialByIdFilialDestino as fd ");
		sqlFrom.append(" left join rp.municipioByIdMunicipioDestino as md ");
		sqlFrom.append(" left join rp.aeroportoByIdAeroportoDestino as ad ");
		sqlFrom.append(" left join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld ");
		sqlFrom.append(" left join rp.zonaByIdZonaDestino as zd ");
		sqlFrom.append(" left join rp.zonaByIdZonaOrigem as zo ");
		sqlFrom.append(" left join rp.paisByIdPaisDestino as paisd ");
		sqlFrom.append(" left join rp.paisByIdPaisOrigem as paiso ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("rp");
		sql.addFrom(sqlFrom.toString());

		sql.addCriteria("rp.servico.id","=", idServico);
		Validate.notNull(idCliente, "idCliente não pode ser nulo");
		sql.addCriteria("rp.cliente.id","=", idCliente);

		//Origem
		sql.add("AND(rp.zonaByIdZonaOrigem.idZona is null OR rp.zonaByIdZonaOrigem.idZona = " + restricaoRotaOrigem.getIdZona() + ")");
		sql.add("AND(rp.paisByIdPaisOrigem.idPais is null OR rp.paisByIdPaisOrigem.idPais = " + restricaoRotaOrigem.getIdPais() + ")");
		sql.add("AND(ufo.idUnidadeFederativa is null OR ufo.idUnidadeFederativa = " + restricaoRotaOrigem.getIdUnidadeFederativa() + ")");
		sql.add("AND(fo.idFilial is null OR fo.idFilial = " + restricaoRotaOrigem.getIdFilial() + ")");
		sql.add("AND(mo.idMunicipio is null OR mo.idMunicipio = " + restricaoRotaOrigem.getIdMunicipio() + ")");
		sql.add("AND(ao.idAeroporto is null OR ao.idAeroporto = " + restricaoRotaOrigem.getIdAeroporto() + ")");
		sql.add("AND(tlo.idTipoLocalizacaoMunicipio is null OR tlo.idTipoLocalizacaoMunicipio = " + restricaoRotaOrigem.getIdTipoLocalizacao() + ")");

		// Destino
		if (cont == 0) {//Municipio
			sql.addCriteria("rp.zonaByIdZonaDestino.idZona","=", restricaoRotaDestino.getIdZona());
			sql.addCriteria("rp.paisByIdPaisDestino.idPais","=", restricaoRotaDestino.getIdPais());
			sql.addCriteria("ufd.idUnidadeFederativa","=", restricaoRotaDestino.getIdUnidadeFederativa());
			sql.addCriteria("md.idMunicipio","=", restricaoRotaDestino.getIdMunicipio());
		} else if (cont == 1) {//Filial
			sql.addCriteria("rp.zonaByIdZonaDestino.idZona","=", restricaoRotaDestino.getIdZona());
			sql.addCriteria("rp.paisByIdPaisDestino.idPais","=", restricaoRotaDestino.getIdPais());
			sql.addCriteria("ufd.idUnidadeFederativa","=", restricaoRotaDestino.getIdUnidadeFederativa());
			sql.addCriteria("fd.idFilial","=", restricaoRotaDestino.getIdFilial());
			sql.add(" AND md.idMunicipio is null");
		} else if (cont == 2) { // Tipo Localização
			sql.addCriteria("rp.zonaByIdZonaDestino.idZona","=", restricaoRotaDestino.getIdZona());
			sql.addCriteria("rp.paisByIdPaisDestino.idPais","=", restricaoRotaDestino.getIdPais());
			sql.addCriteria("ufd.idUnidadeFederativa","=", restricaoRotaDestino.getIdUnidadeFederativa());
			sql.addCriteria("tld.idTipoLocalizacaoMunicipio","=", restricaoRotaDestino.getIdTipoLocalizacao());
			sql.add(" AND md.idMunicipio is null");
			sql.add(" AND fd.idFilial is null");
		} else if (cont == 3) { //Unidade federativa - estado
			sql.addCriteria("rp.zonaByIdZonaDestino.idZona","=", restricaoRotaDestino.getIdZona());
			sql.addCriteria("rp.paisByIdPaisDestino.idPais","=", restricaoRotaDestino.getIdPais());
			sql.addCriteria("ufd.idUnidadeFederativa","=", restricaoRotaDestino.getIdUnidadeFederativa());
			sql.add(" AND md.idMunicipio is null");
			sql.add(" AND tld.idTipoLocalizacaoMunicipio is null");	
			sql.add(" AND fd.idFilial is null");
		} else if (cont == 4) { //País
			sql.addCriteria("rp.zonaByIdZonaDestino.idZona","=", restricaoRotaDestino.getIdZona());
			sql.addCriteria("rp.paisByIdPaisDestino.idPais","=", restricaoRotaDestino.getIdPais());
			sql.add(" AND ufd.idUnidadeFederativa is null");
			sql.add(" AND md.idMunicipio is null");
			sql.add(" AND tld.idTipoLocalizacaoMunicipio is null");
			sql.add(" AND fd.idFilial is null");
		} else if (cont == 5) { //Zona
			sql.addCriteria("rp.zonaByIdZonaDestino.idZona","=", restricaoRotaDestino.getIdZona());
			sql.add(" AND rp.paisByIdPaisDestino.idPais is null");
			sql.add(" AND ufd.idUnidadeFederativa is null");
			sql.add(" AND md.idMunicipio is null");
			sql.add(" AND tld.idTipoLocalizacaoMunicipio is null");
			sql.add(" AND fd.idFilial is null");
		}

		if (cont < 6) {
			final List rsp = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

			if (rsp != null && !rsp.isEmpty()) {
				return (PrazoEntregaCliente) rsp.get(0);
			} else {
				cont++;
				return findPrazoEntregaClienteRecursivo(idCliente, idServico, restricaoRotaOrigem, restricaoRotaDestino, cont);
		}
		}
		return null;
	}
}