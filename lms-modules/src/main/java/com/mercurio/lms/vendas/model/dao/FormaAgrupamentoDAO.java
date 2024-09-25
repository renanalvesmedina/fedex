package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.LikeVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.vendas.model.FormaAgrupamento;
import com.mercurio.lms.vendas.model.FormaAgrupamentoListBoxElement;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FormaAgrupamentoDAO extends BaseCrudDao<FormaAgrupamento, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return FormaAgrupamento.class;
	}

	protected void initFindByIdLazyProperties(Map arg0) {
		arg0.put("cliente", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map arg0) {
		arg0.put("cliente", FetchMode.JOIN);
	}

	public List findFormasAgrupamentoDomain(Map map){
		 //Buscar valores possíveis do domínio do campo TP_CAMPO da tabela
		 //DOMINIO_AGRUPAMENTO (com exceção do valor 'CS')
		StringBuffer query = new StringBuffer()		
		.append(" from ").append(DomainValue.class.getName()).append(" as vd")
		.append(" where vd.domain.name = 'DM_FORMA_AGRUPAMENTO' ")
		.append(" and vd.value != 'CS' ");

		List list = getAdsmHibernateTemplate().find(query.toString());   	

		return list;
	}

	public DomainValue findDomainValueByNameAndValue(String name, String value){

		DomainValue retorno = null;

		StringBuilder query = new StringBuilder()		
		.append(" from ").append(DomainValue.class.getName()).append(" vd")
		.append(" where vd.domain.name = '").append(name).append("'")
		.append(" and vd.value = '").append(value).append("'");

		List list = getAdsmHibernateTemplate().find(query.toString());
		if( list != null && !list.isEmpty() ){
			retorno = (DomainValue) list.iterator().next();
		}
		return retorno;
	}

	public List findFormasAgrupamentoInformacaoDocServico(Map criterions) {
		//Buscar valores dos campos cadastrados na tabela INFORMACAO_DOC_SERVICO
		StringBuilder query = new StringBuilder()
		.append(" select new ").append(FormaAgrupamentoListBoxElement.class.getName()).append("(ids.idInformacaoDocServico, 'CS', ids.dsCampo, 'info_dcto_servico')")
		.append(" from ").append(InformacaoDocServico.class.getName()).append(" as ids");

		List list = getAdsmHibernateTemplate().find(query.toString());  

		return list;
	}

	public List findFormasAgrupamentoInformacaoDocCliente(Map criterions) {
		Long idCliente = null;
    	if( criterions != null ){    		
    		if( criterions.get("idCliente") != null ){
    			idCliente = Long.valueOf((String)criterions.get("idCliente"));
    		}
    	} else {
    		return null;
		}
		//Buscar valores dos campos cadastrados na tabela INFORMACAO_DOCTO_CLIENTE
		StringBuilder query = new StringBuilder()
		.append(" select new ").append(FormaAgrupamentoListBoxElement.class.getName()).append("(idc.idInformacaoDoctoCliente, 'CS', idc.dsCampo, 'info_dcto_cliente')")
		.append(" from ").append(InformacaoDoctoCliente.class.getName()).append(" as idc")
		.append(" where idc.cliente.idCliente = " + idCliente);

		List list = getAdsmHibernateTemplate().find(query.toString());  

		return list;
	}
   
	/**
	 * FindPaginated para manter Formas Agrupamento
	 *
	 * @author José Rodrigo Moraes
	 * @since 03/11/2006
	 *
	 * @param idCliente
	 * @param situacao
	 * @param dsFormaAgrupamento
	 * @param blAutomatico
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedSpecific(Long idCliente, String situacao, String dsFormaAgrupamento, String blAutomatico, FindDefinition findDef) {
		SqlTemplate hql = new SqlTemplate();

		getHqlDefault(hql,idCliente,situacao,dsFormaAgrupamento,blAutomatico);
    	hql.addProjection("new Map(fa.id as idFormaAgrupamento, " +
    			          "        fa.dsFormaAgrupamento as dsFormaAgrupamento, " +
    			          "        fa.tpSituacao as tpSituacao, " +
    			          "        fa.blAutomatico as blAutomatico, " +
    			          "        fa.nrOrdemPrioridade as nrOrdemPrioridade, " +
    			          "        c.pessoa.nmPessoa as nmCliente)");

    	hql.addOrderBy("c.pessoa.nmPessoa");
    	hql.addOrderBy("fa.nrOrdemPrioridade");
    	hql.addOrderBy(OrderVarcharI18n.hqlOrder("fa.dsFormaAgrupamento",LocaleContextHolder.getLocale()));

    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(), hql.getCriteria());
	}

	/**
	 * Row Count para Manter formas Agrupamento
	 *
	 * @author José Rodrigo Moraes
	 * @since 03/11/2006
	 *
	 * @param idCliente
	 * @param situacao
	 * @param dsFormaAgrupamento
	 * @param blAutomatico
	 * @return
	 */
	public Integer getRowCountSpecific(Long idCliente, String situacao, String dsFormaAgrupamento, String blAutomatico) {
		SqlTemplate hql = new SqlTemplate();
		getHqlDefault(hql,idCliente,situacao,dsFormaAgrupamento,blAutomatico);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}
	
	/**
	 * Método auxiliar para montagem do hql básico para o Formas Agrupamento
	 *
	 * @author José Rodrigo Moraes
	 * @since 03/11/2006
	 *
	 * @param hql
	 * @param idCliente
	 * @param situacao
	 * @param dsFormaAgrupamento
	 * @param blAutomatico
	 * @return
	 */
	private SqlTemplate getHqlDefault(SqlTemplate hql,Long idCliente, String situacao, String dsFormaAgrupamento, String blAutomatico){
		
		hql.addInnerJoin(FormaAgrupamento.class.getName(),"fa");
		hql.addInnerJoin("fa.cliente","c");
		
		hql.addCriteria("c.id","=",idCliente);
		hql.addCriteria("fa.tpSituacao","=",situacao);
		
		if( dsFormaAgrupamento != null && !dsFormaAgrupamento.equals("") ) {
			hql.addCustomCriteria(LikeVarcharI18n.hqlLike("fa.dsFormaAgrupamento",LocaleContextHolder.getLocale(),true));
			hql.addCriteriaValue(dsFormaAgrupamento);
		}
		
		hql.addCriteria("fa.blAutomatico","=",blAutomatico);
		
		return hql;
	}

}