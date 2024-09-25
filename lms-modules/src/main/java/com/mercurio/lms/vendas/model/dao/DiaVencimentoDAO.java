package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.DiaVencimento;
import com.mercurio.lms.vendas.model.PrazoVencimento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DiaVencimentoDAO extends BaseCrudDao<DiaVencimento, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return DiaVencimento.class;
	}

	protected void initFindByIdLazyProperties(Map map) {
		map.put("servico", FetchMode.JOIN);
		map.put("divisaoCliente", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("servico", FetchMode.JOIN);
		map.put("divisaoCliente", FetchMode.JOIN);
	}

	protected void initFindListLazyProperties(Map map) {
		map.put("servico", FetchMode.JOIN);
		map.put("divisaoCliente", FetchMode.JOIN);
	}

	/**
	 * Retorna a lista de filial centralazadora da filial informada por tpModal e tpAbrangencia.
	 * 
	 * @param Long idDivisaoCliente
	 * @param String tpModal
	 * @param String tpAbrangencia
	 * @return List
	 */
	public List<DiaVencimento> findDiaVencimentoByDivisao(Long idDivisaoCliente, String tpModal, String tpAbrangencia){
		SqlTemplate hql = mountHql(idDivisaoCliente, tpModal, tpAbrangencia);
		hql.addProjection("di");
		return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Monta o Hql de pesquisa, só falta informar a projection.
	 * 
	 * Caso que tem que informar mais parametros é só sobescrever o 
	 * método passando null nos outros parametros.
	 * */
	private SqlTemplate mountHql(Long idDivisaoCliente, String tpModal, String tpAbrangencia){
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(DiaVencimento.class.getName(),"di");
		hql.addCriteria("di.divisaoCliente.id","=",idDivisaoCliente);
		hql.addCriteria("di.tpModal","=",tpModal);
		hql.addCriteria("di.tpAbrangencia","=",tpAbrangencia);		
		return hql;
	}

	/**
	 * Retorna a lista de filial centralazadora da filial informada por tpModal e tpAbrangencia.
	 * 
	 * @param Long idPrazoVencimento
	 * @param int diaMinimo
	 * @return List
	 */
	public List<DiaVencimento> findByPrazoVencimentoAndDiaMinimo(Long idPrazoVencimento, int diaMinimo){
		SqlTemplate hql = mountHqlByPrazo(idPrazoVencimento, diaMinimo);

		hql.addProjection("dv");
		hql.addOrderBy("dv.nrDiaVencimento", "asc");
		return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Monta o Hql de pesquisa, só falta informar a projection.
	 * 
	 * Caso que tem que informar mais parametros é só sobescrever o 
	 * método passando null nos outros parametros.
	 * */
	private SqlTemplate mountHqlByPrazo(Long idPrazoVencimento, int diaMinimo){
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(DiaVencimento.class.getName(),"dv");
		hql.addCriteria("dv.prazoVencimento.id","=",idPrazoVencimento);
		hql.addCriteria("dv.nrDiaVencimento",">=", Byte.valueOf(diaMinimo + ""));
		return hql;
	}

	public void removeByIdPrazoVencimento(Long id, Boolean isFlushSession){
		StringBuilder hql = new StringBuilder()
			.append("delete	").append(getPersistentClass().getName()).append("\n")
			.append("where	prazoVencimento = :id");

		PrazoVencimento prazoVencimento = new PrazoVencimento();
		prazoVencimento.setIdPrazoVencimento(id);
		getAdsmHibernateTemplate().removeById(hql.toString(), prazoVencimento);
		if(isFlushSession.booleanValue()) {
			getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
		}
	}

}