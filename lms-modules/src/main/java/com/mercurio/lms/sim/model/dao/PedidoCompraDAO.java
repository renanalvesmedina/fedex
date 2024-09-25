package com.mercurio.lms.sim.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.PedidoCompra;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PedidoCompraDAO extends BaseCrudDao<PedidoCompra, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PedidoCompra.class;
    }

    public List findByNrPedido(Long nrPedido){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("nrPedido", nrPedido));
    	    	
    	return findByDetachedCriteria(dc);
    }
    
    public List findByNrPedidoInternacional(String nrPedido){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("nrPedidoInternacional", nrPedido));
    	    	
    	return findByDetachedCriteria(dc);
    }

    public List findDocumentosServico(Long idPedidoCompra, Long codTipoAnexoFatura){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("ci.idDoctoServico");
    	sql.addProjection("ci.filialByIdFilialOrigem.idFilial");
    	sql.addFrom("PedidoCompra", "pc");
    	sql.addFrom("DocumentoAnexo da " +
    			" inner join da.anexoDoctoServico ads " +
    			" inner join da.ctoInternacional ci ");
    	
    	sql.addJoin("pc.nrFatura", "da.dsDocumento");
    	sql.addCriteria("ads.idAnexoDoctoServico", "=", codTipoAnexoFatura);
    	sql.addCriteria("pc.idPedidoCompra", "=", idPedidoCompra);
    	sql.addCustomCriteria("ci.clienteByIdClienteRemetente.idCliente = pc.remetente.idCliente");
    	sql.addCustomCriteria("ci.clienteByIdClienteDestinatario.idCliente = pc.destinatario.idCliente");
    	return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
    }
    
    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	SqlTemplate hql = this.getSqlTemplate(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
    }
    public SqlTemplate getSqlTemplate(TypedFlatMap criteria){
	  	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(PedidoCompra.class.getName() +" as PC " +
    					"left 	join fetch PC.remetente		as ClRemetente " +
    					"left	join fetch ClRemetente.pessoa		as PeRemetente " +

    					"left 	join fetch PC.destinatario		as ClDestinatario " +
    					"left  join fetch ClDestinatario.pessoa  	as PeDestinatario " +
    					
    					"left  join fetch PC.filial as fi"
    				);
    	if (criteria.getLong("filial.idFilial") != null) {
    		sql.addCriteria("fi.idFilial","=",criteria.getLong("filial.idFilial"));
    	}
    	if (criteria.getLong("remetente.idCliente") != null) {
    		sql.addCriteria("PeRemetente.idPessoa","=",criteria.getLong("remetente.idCliente"));
    	}
    	if (criteria.getLong("destinatario.idCliente") != null) {
    		sql.addCriteria("PeDestinatario.idPessoa","=",criteria.getLong("destinatario.idCliente"));
    	}
    	if (!criteria.getString("nrPedido").equalsIgnoreCase("") ) {
    		sql.addCriteria("PC.nrPedido","=",criteria.getString("nrPedido"));
    	} 
    	if (!criteria.getString("nrPedidoInternacional").equalsIgnoreCase("")) {
    		sql.addCriteria("PC.nrPedidoInternacional","like",criteria.getString("nrPedidoInternacional"));
    	} 
    	if (!criteria.getDomainValue("tpOrigem").getValue().equalsIgnoreCase("")) {
    		sql.addCriteria("PC.tpOrigem","=",criteria.getDomainValue("tpOrigem").getValue());
    	}
    	if (!criteria.getString("fatura").equalsIgnoreCase("")) {
    		sql.addCriteria("PC.nrFatura","like",criteria.getString("fatura"));
    	}    	
    	if (!criteria.getString("notaFiscal").equalsIgnoreCase("")) {
    		sql.addCriteria("PC.nrNotaFiscal","like",criteria.getString("notaFiscal"));
    	}    	
    	
    	//
    	
    	
    	if (criteria.getYearMonthDay("dataHoraInicial") != null) {
    		sql.addCriteria("PC.dhEmissao.value",">=",criteria.getYearMonthDay("dataHoraInicial"));
    	}    	
    	if (criteria.getYearMonthDay("dataHoraFinal") != null) {
    		sql.addCriteria("PC.dhEmissao.value","<=",criteria.getYearMonthDay("dataHoraFinal"));
    	} 
    	
    	sql.addOrderBy("PeRemetente.nmPessoa");
		sql.addOrderBy("PeDestinatario.nmPessoa");
		sql.addOrderBy("PC.nrPedido");
		return sql;
    }
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		SqlTemplate sql = getSqlTemplate(criteria);
   		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
   		
   		return  getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	} 

}