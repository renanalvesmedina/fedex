package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.LiberacaoEmbarque;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class LiberacaoEmbarqueDAO extends BaseCrudDao<LiberacaoEmbarque, Long> {

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("municipio", FetchMode.JOIN);
		lazyFindById.put("municipio.unidadeFederativa", FetchMode.JOIN);
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("pendencia", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return LiberacaoEmbarque.class;
	}

	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = getSqlFindPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

	private SqlTemplate getSqlFindPaginated(Map criteria){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("li");
		sql.addFrom(LiberacaoEmbarque.class.getName()+" li join fetch li.municipio as mu join fetch mu.unidadeFederativa as uf join li.cliente as cl");
		sql.addFrom(Domain.class.getName()+" do");
		sql.addFrom(DomainValue.class.getName()+" dv");
		sql.addJoin("do.id","dv.domain.id");
		sql.addJoin("nvl(li.tpModal,'A')","dv.value");
		sql.addCriteria("do.name","=","DM_MODAL");
		Long idCliente = MapUtilsPlus.getLongOnMap(criteria, "cliente", "idCliente", null);
		if (idCliente != null) {
			sql.addCriteria("cl.idCliente","=",idCliente);
		}
		Long idMunicipio = MapUtilsPlus.getLongOnMap(criteria, "municipio", "idMunicipio", null);
		if (idMunicipio != null) {
			sql.addCriteria("mu.idMunicipio","=",idMunicipio);
		}
		String tpModal = MapUtils.getString(criteria, "tpModal");
		if(StringUtils.isNotBlank(tpModal)) {
			sql.addCriteria("li.tpModal","=",tpModal);
		}
		sql.addOrderBy("mu.nmMunicipio");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description", LocaleContextHolder.getLocale()));
		return sql;
	}

	public Integer getRowCountByIdMunicipioIdClienteModal(Long idMunicipio, Long idCliente, String modal) {
		DetachedCriteria dc = createDetachedCriteria()
				.setProjection(Projections.count("idLiberacaoEmbarque"));

		if( modal != null ){
			dc.add(Restrictions.eq("tpModal", modal));
		}else{
			dc.add(Restrictions.isNull("tpModal"));
		}

		dc.add(Restrictions.eq("cliente.id", idCliente));
		dc.add(Restrictions.eq("municipio.id", idMunicipio));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public Integer getRowCountByIdMunicipioIdClienteModalAferido(Long idMunicipio, Long idCliente, String modal) {
		DetachedCriteria dc = createDetachedCriteria()
				.setProjection(Projections.count("idLiberacaoEmbarque"));

		if( modal != null ){
			dc.add(Restrictions.eq("tpModal", modal));
		}else{
			dc.add(Restrictions.isNull("tpModal"));
		}

		dc.add(Restrictions.eq("blEfetivado",true));
		dc.add(Restrictions.eq("cliente.id", idCliente));
		dc.add(Restrictions.eq("municipio.id", idMunicipio));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		StringBuilder hql = new StringBuilder()
				.append(" DELETE ").append(getPersistentClass().getName())
				.append(" WHERE cliente.id = :id");

		getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
	}

	/**
	 * Solicitação CQPRO00005947 da Integração.
	 * @param idCliente
	 */
	public Integer getRowCountByIdCliente(Long idCliente){
		DetachedCriteria dc = DetachedCriteria.forClass(LiberacaoEmbarque.class, "le");
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("le.cliente.id", idCliente));

		return (Integer)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public void executeDesefetivarByMunicipioCliente(List<Long> idsMunicipio, Long idCliente){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE liberacao_embarque SET bl_efetivado = :nao WHERE id_municipio IN(:idsMunicipio) AND id_cliente = :idCliente AND bl_libera_grande_capital = :nao");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("nao", "N");
		parametersValues.put("idCliente", idCliente);
		parametersValues.put("idsMunicipio", idsMunicipio);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}

	public List<Long> findIdsMunicipiosComLiberacao(Cliente cliente, UnidadeFederativa uf, TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("m.idMunicipio");
		setDadosBuscaMunicipio(hql, uf, tipoLocalizacaoMunicipio);
		hql.addCustomCriteria("(SELECT count(*) FROM " + LiberacaoEmbarque.class.getName() +" le WHERE le.cliente = ? and le.municipio.id = m.id) !=0 ", cliente);
		return (List<Long>)getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	private void setDadosBuscaMunicipio(SqlTemplate hql, UnidadeFederativa uf, TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio){
		hql.addInnerJoin(OperacaoServicoLocaliza.class.getName(),"osl");
		hql.addInnerJoin("osl.municipioFilial","mf");
		hql.addInnerJoin("mf.municipio", "m");
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		hql.addCriteria("mf.dtVigenciaInicial", "<=", dataAtual);
		hql.addCriteria("mf.dtVigenciaFinal", ">=", dataAtual);
		hql.addCriteria("osl.dtVigenciaInicial", "<=", dataAtual);
		hql.addCriteria("osl.dtVigenciaFinal", ">=", dataAtual);
		hql.addCriteria("m.unidadeFederativa", "=", uf);
		hql.addCriteria("osl.tipoLocalizacaoMunicipio", "=",tipoLocalizacaoMunicipio );
		hql.addCriteria("osl.blAtendimentoGeral", "=",Boolean.TRUE );
		hql.addCriteriaIn("osl.tpOperacao",new String[]{"A","E"});
		hql.addCustomCriteria("(osl.servico.id is null or osl.servico.id = 1)");
	}

	/**
	 * Busca todos os municípios do tipo de localizacao na UF informada  que nao possuam nenhuma 
	 * liberação de embarque para o cliente informado.
	 * @param cliente
	 * @param uf
	 * @param tipoLocalizacaoMunicipio
	 * @return
	 * @return <code>List</code> 
	 * @author Vagner Huzalo
	 *
	 */
	public List<Municipio> findMunicipiosSemLiberacao(Cliente cliente, UnidadeFederativa uf, TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("m");
		setDadosBuscaMunicipio(hql, uf, tipoLocalizacaoMunicipio);
		hql.addCustomCriteria("(SELECT count(*) " +
				"FROM "+LiberacaoEmbarque.class.getName()+" le " +
				"WHERE le.cliente = ? and le.municipio.id = m.id)=0", cliente);
		return (List<Municipio>)getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public List<LiberacaoEmbarque> findLiberacaoCliente(Long idCliente, Long idMunicipio, DomainValue tpModal) {
		StringBuilder hql = new StringBuilder();
		hql.append("select le from ")
				.append(getPersistentClass().getName() )
				.append(" le where le.tpModal = ? and le.cliente.id = ? and le.municipio.id = ? ");
		return getHibernateTemplate().find(hql.toString(), new Object[]{tpModal, idCliente, idMunicipio}) ;
	}

	public LiberacaoEmbarque findLiberacaoClienteByMunicipio(Long idCliente, Long idMunicipio) {
		StringBuilder hql = new StringBuilder();
		hql.append("select le from ")
				.append(getPersistentClass().getName() )
				.append(" le where le.cliente.id = ? and le.municipio.id = ? ");
		List<LiberacaoEmbarque> liberacoes = getHibernateTemplate().find(hql.toString(), new Object[]{ idCliente, idMunicipio});
		return  liberacoes != null & !liberacoes.isEmpty() ? liberacoes.get(0) : null;
	}

	public List<LiberacaoEmbarque> findLiberacoesClienteByMunicipio(Long idCliente, Long idMunicipio) {
		StringBuilder hql = new StringBuilder();
		hql.append("select le from ")
				.append(getPersistentClass().getName() )
				.append(" le where le.cliente.id = ? and le.municipio.id = ? ");
		List<LiberacaoEmbarque> liberacoes = getHibernateTemplate().find(hql.toString(), new Object[]{ idCliente, idMunicipio});
		return  liberacoes != null & !liberacoes.isEmpty() ? liberacoes : null;
	}
}
