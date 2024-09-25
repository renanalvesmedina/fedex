package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RotaIntervaloCepDAO extends BaseCrudDao<RotaIntervaloCep, Long> {

	protected final Class getPersistentClass() {
		return RotaIntervaloCep.class;
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		SqlTemplate hql = montaHql(criteria);

		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);

		hql.addOrderBy("RCE.nrRota");
		hql.addOrderBy("RCE.dsRota");
		hql.addOrderBy("RIC.nrOrdemOperacao");
		hql.addOrderBy("RIC.nrCepInicial");

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDefinition.getCurrentPage(),
				findDefinition.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate hql = montaHql(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}

	private SqlTemplate montaHql(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(new StringBuilder(getPersistentClass().getName()).append(" AS RIC ")
				.append("LEFT JOIN FETCH RIC.tipoDificuldadeAcesso AS TDA ")
				.append("LEFT JOIN FETCH RIC.rotaColetaEntrega AS RCE ")
				.append("LEFT JOIN FETCH RIC.cliente AS C ")
				.append("LEFT JOIN FETCH C.pessoa AS CP ")
				.append("LEFT JOIN FETCH RIC.enderecoPessoa AS EP ")
				.append("LEFT JOIN FETCH EP.tipoLogradouro AS TL ")
				.append("LEFT JOIN FETCH RIC.municipio AS M ").toString());

		hql.addCriteria("RIC.id","=",criteria.getLong("idRotaIntervaloCep"));
		hql.addCriteria("RCE.id","=",criteria.getLong("rotaColetaEntrega.idRotaColetaEntrega"));
		hql.addCriteria("RCE.filial.id","=",criteria.getLong("rotaColetaEntrega.filial.idFilial"));

		hql.addCriteria("RIC.municipio.id","=",criteria.getLong("municipio.idMunicipio"));

		if (criteria.get("nrCep") != null && StringUtils.isNotBlank(criteria.get("nrCep").toString())) {
			hql.addCriteria("RIC.nrCepInicial", "<=", StringUtils.remove(criteria.getString("nrCep"), "%"));
			hql.addCriteria("RIC.nrCepFinal", ">=", StringUtils.remove(criteria.getString("nrCep"), "%"));
		}

		hql.addCriteria("C.id","=",criteria.getLong("cliente.idCliente"));

		hql.addCriteria("EP.id","=",criteria.getLong("enderecoCliente"));

		if (StringUtils.isNotBlank(criteria.getString("tpGrauRisco"))) 
			hql.addCriteria("RIC.tpGrauRisco","=",criteria.getString("tpGrauRisco"));

		hql.addCriteria("TDA.id","=",criteria.getLong("tipoDificuldadeAcesso.idTipoDificuldadeAcesso"));

		return hql;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {	
		lazyFindPaginated.put("cliente", FetchMode.JOIN);
		lazyFindPaginated.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("enderecoPessoa", FetchMode.JOIN); 
		lazyFindPaginated.put("enderecoPessoa.tipoLogradouro", FetchMode.JOIN); 
		lazyFindPaginated.put("municipio", FetchMode.JOIN);
		lazyFindPaginated.put("tipoDificuldadeAcesso", FetchMode.JOIN);						
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("rotaColetaEntrega", FetchMode.JOIN);
		lazyFindById.put("rotaColetaEntrega.filial", FetchMode.JOIN);
		lazyFindById.put("rotaColetaEntrega.filial.pessoa", FetchMode.JOIN);
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("enderecoPessoa", FetchMode.JOIN);
		lazyFindById.put("municipio", FetchMode.JOIN);
		lazyFindById.put("tipoDificuldadeAcesso", FetchMode.JOIN);
	}

	/**
	 * Verifica se o intervalo de cep informado ja nao esta cadastrado para outra rota
	 * @param idRotaIntervaloCep
	 * @param nrCepInicial
	 * @param nrCepFinal
	 * @return TRUE se o intervalo ja esta cadastrado, FALSE caso contrario
	 */
	public boolean verificaIntervaloCep(Long idRotaIntervaloCep, String nrCepInicial, String nrCepFinal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();		

		if (idRotaIntervaloCep != null)
			dc.add(Restrictions.ne("idRotaIntervaloCep", idRotaIntervaloCep));

		dc.add(
				Restrictions.or(
						Restrictions.and(
								Restrictions.le("nrCepInicial", nrCepInicial),
								Restrictions.ge("nrCepFinal", nrCepFinal)
							),
						Restrictions.or(
								Restrictions.between("nrCepInicial", nrCepInicial, nrCepFinal),
								Restrictions.between("nrCepFinal", nrCepInicial, nrCepFinal)
						)
				)
		);

		dc.add(Restrictions.eq("blAtendimentoTemporario", Boolean.FALSE));
		
		JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

		dc.setProjection(Projections.rowCount());

		List result = findByDetachedCriteria(dc);

		return ((Integer)result.get(0)).intValue() > 0;
	}
	
	/**
	 * Verifica se o cliente é atendido em outro intervalo de CEP sem endereço e que esteja vigente no período selecionado
	 * @param idRotaIntervaloCep
	 * @param nrCepInicial
	 * @param nrCepFinal
	 * @param cliente
	 * @return TRUE se o intervalo ja esta cadastrado, FALSE caso contrario
	 */
	public boolean verificaIntervaloCep(Long idRotaIntervaloCep, String nrCepInicial, String nrCepFinal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Cliente cliente){
		DetachedCriteria dc = createDetachedCriteria();		

		if (idRotaIntervaloCep != null)
			dc.add(Restrictions.ne("idRotaIntervaloCep", idRotaIntervaloCep));

		//usa o critério do cep somente quando ele existir
		if (!StringUtils.isBlank(nrCepInicial)){ 
			dc.add(
					Restrictions.or(
							Restrictions.and(
									Restrictions.le("nrCepInicial", nrCepInicial),
									Restrictions.ge("nrCepFinal", nrCepFinal)
								),
							Restrictions.or(
									Restrictions.between("nrCepInicial", nrCepInicial, nrCepFinal),
									Restrictions.between("nrCepFinal", nrCepInicial, nrCepFinal)
							)
					)
			);
		}

		JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

		if (cliente != null) {
			dc.add(Restrictions.eq("cliente.idCliente", cliente.getIdCliente()));
			dc.add(Restrictions.isNull("enderecoPessoa"));
		}

		dc.setProjection(Projections.rowCount());

		List result = findByDetachedCriteria(dc);

		return ((Integer)result.get(0)).intValue() > 0;
	}

	/**
	 * Verifica se o cliente e o endereço são atendidos em outro intervalo de CEP e que esteja vigente no período selecionado
	 * @param idRotaIntervaloCep
	 * @param nrCepInicial
	 * @param nrCepFinal
	 * @param cliente
	 * @param enderecoPessoa
	 * @return TRUE se o intervalo ja esta cadastrado, FALSE caso contrario
	 */
	public boolean verificaIntervaloCep(Long idRotaIntervaloCep, String nrCepInicial, String nrCepFinal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Cliente cliente, EnderecoPessoa enderecoPessoa){
		DetachedCriteria dc = createDetachedCriteria();		

		if (idRotaIntervaloCep != null)
			dc.add(Restrictions.ne("idRotaIntervaloCep", idRotaIntervaloCep));

		//usa o critério do cep somente quando ele existir
		if (!StringUtils.isBlank(nrCepInicial)){ 
			dc.add(
					Restrictions.or(
							Restrictions.and(
									Restrictions.le("nrCepInicial", nrCepInicial),
									Restrictions.ge("nrCepFinal", nrCepFinal)
								),
							Restrictions.or(
									Restrictions.between("nrCepInicial", nrCepInicial, nrCepFinal),
									Restrictions.between("nrCepFinal", nrCepInicial, nrCepFinal)
							)
					)
			);
		}

		JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

		dc.add(Restrictions.eq("cliente.idCliente", cliente.getIdCliente()));
		dc.add(Restrictions.eq("enderecoPessoa.idEnderecoPessoa", enderecoPessoa.getIdEnderecoPessoa()));

		dc.setProjection(Projections.rowCount());

		List result = findByDetachedCriteria(dc);

		return ((Integer)result.get(0)).intValue() > 0;
	}
	
	/**
	 * Verifica se ja existe um intervalo de cep com a mesma ordem vigente para a rota informada
	 * @param rotaIntervaloCep
	 * @return
	 */
	public boolean validateNrOrdemVigente(RotaIntervaloCep rotaIntervaloCep){
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());

		if (rotaIntervaloCep.getIdRotaIntervaloCep() != null){
			dc.add(Restrictions.ne("id", rotaIntervaloCep.getIdRotaIntervaloCep()));
		}

		dc.add(Restrictions.eq("nrOrdemOperacao", rotaIntervaloCep.getNrOrdemOperacao()));
		dc.add(Restrictions.eq("rotaColetaEntrega.id", rotaIntervaloCep.getRotaColetaEntrega().getIdRotaColetaEntrega()));

		JTVigenciaUtils.getDetachedVigencia(dc, rotaIntervaloCep.getDtVigenciaInicial(), rotaIntervaloCep.getDtVigenciaFinal());

		List result = findByDetachedCriteria(dc);

		return ((Integer)result.get(0)).intValue() > 0;
	}

	 /**
	 * Verifica se existe alguma rota de coleta/entrega que atende o cep informado
	 * @param nrCep
	 * @param idCliente
	 * @param idEnderecoPessoa
	 * @param idFilial
	 * @param data
	 * @return 
	 */
	public List findRotaIntervaloCepAtendimento(String nrCep, Long idCliente, Long idEnderecoPessoa, Long idFilial, YearMonthDay data){
		DetachedCriteria dc = DetachedCriteria.forClass(RotaIntervaloCep.class);
		dc.createAlias("rotaColetaEntrega", "rce");

		JTVigenciaUtils.getDetachedVigencia(dc, data, data);

		if (idCliente != null) {
			if (idEnderecoPessoa != null) {
				Object parameters[] = new Object [] {idCliente, idEnderecoPessoa, idFilial};
				Type type[] = new Type[] {Hibernate.LONG, Hibernate.LONG, Hibernate.LONG};

				dc.add(Restrictions.sqlRestriction(
						" ( exists ( select 1 " +
						" from ROTA_INTERVALO_CEP roic2 " +
						"    ,rota_coleta_entrega rce " +
						"	 ,filial f " +
						" where roic2.id_Rota_Intervalo_Cep = {alias}.id_Rota_Intervalo_Cep" +
						" and roic2.id_Cliente = ? " + 
						" and roic2.id_Endereco_Pessoa = ?" +
						" and roic2.id_rota_coleta_entrega = rce.id_rota_coleta_entrega " +
						" and rce.id_filial = f.id_filial " +
						" and f.id_filial = ? " +
						" ) ) " , parameters, type ));
			} else {
				dc.add(Restrictions.eq("cliente.idCliente", idCliente));
				dc.add(Restrictions.sqlRestriction("{alias}.id_Endereco_Pessoa is null"));
				dc.add(Restrictions.eq("rce.filial.id", idFilial));
			}
		} else {
			dc.add(
					Restrictions.or(
							Restrictions.and(
									Restrictions.le("nrCepInicial", nrCep),
									Restrictions.ge("nrCepFinal", nrCep)
								),
							Restrictions.or(
									Restrictions.between("nrCepInicial", nrCep, nrCep),
									Restrictions.between("nrCepFinal", nrCep, nrCep)
							)
					)
			);
			dc.add(Restrictions.eq("rce.filial.id", idFilial));
		}

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	// LMSA-6786
	@SuppressWarnings("unchecked")
	public List<RotaIntervaloCep> findRotaIntervaloCepByCep(String nrCep, Long idFilial, YearMonthDay data){
		DetachedCriteria dc = DetachedCriteria.forClass(RotaIntervaloCep.class);
		dc.createAlias("rotaColetaEntrega", "rce");

		JTVigenciaUtils.getDetachedVigencia(dc, data, data);
		
		dc.add(
				Restrictions.or(
						Restrictions.and(
								Restrictions.le("nrCepInicial", nrCep),
								Restrictions.ge("nrCepFinal", nrCep)
							),
						Restrictions.or(
								Restrictions.between("nrCepInicial", nrCep, nrCep),
								Restrictions.between("nrCepFinal", nrCep, nrCep)
						)
				)
		);
		dc.add(Restrictions.eq("rce.filial.id", idFilial));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}


	
	
}