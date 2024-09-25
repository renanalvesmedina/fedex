package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DevedorDocServDAO extends BaseCrudDao<DevedorDocServ, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DevedorDocServ.class;
    }

	/**
     * Retorna o devedor do documento de serviço (DevedorDocServ), através do id
	 * 
     * @param  idDoctoServico
     * @return DevedorDocServ
     */
    public DevedorDocServ findDevedorByDoctoServico(Long idDoctoServico) {

		DetachedCriteria dc = DetachedCriteria.forClass(DevedorDocServ.class, "dds")
			.createAlias("dds.cliente", "cliente")
			.setFetchMode("dds.cliente", FetchMode.JOIN)
			
			.createAlias("cliente.pessoa", "pessoa")
			.setFetchMode("cliente.pessoa", FetchMode.JOIN)
			.add(Restrictions.eq("dds.doctoServico.id", idDoctoServico));

		List<DevedorDocServ> list = findByDetachedCriteria(dc);
		if(list != null && !list.isEmpty()) {
			return (DevedorDocServ) list.get(0);
		}
		
		return null;
	}
    
    public List<DevedorDocServ> findDevedoresByDoctoServico(Long idDoctoServico) {

		DetachedCriteria dc = DetachedCriteria.forClass(DevedorDocServ.class, "dds")
			.createAlias("dds.cliente", "cliente")
			.setFetchMode("dds.cliente", FetchMode.JOIN)
			
			.createAlias("cliente.pessoa", "pessoa")
			.setFetchMode("cliente.pessoa", FetchMode.JOIN)
			.add(Restrictions.eq("dds.doctoServico.id", idDoctoServico));

		List<DevedorDocServ> list = findByDetachedCriteria(dc);
		return list;
	}
    
	/**
	 * 
	 * 
	 * @param idDoctoServico
	 * @return
	 */
    public Cliente findClienteDevedorByIdDoctoServico(Long idDoctoServico) {
		Cliente cliente = null;

		ProjectionList projectionList = Projections.projectionList()
			.add(Projections.property("c.idCliente"), "idCliente")
			.add(Projections.property("c.tpCliente"), "tpCliente");

		DetachedCriteria dc = DetachedCriteria.forClass(DevedorDocServ.class, "dds")
		.createAlias("dds.cliente", "c")
		.setProjection(projectionList)
		.add(Restrictions.eq("dds.doctoServico.id", idDoctoServico))
		.setResultTransformer(new AliasToBeanResultTransformer(Cliente.class));

		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty()) {
			cliente = (Cliente) l.get(0);
		}
		return cliente;
	}

   
	public List findIdsByIdDoctoServico(Long idDoctoServico)
	{
		String sql = "select pojo.idDevedorDocServ " +
		"from "+ DevedorDocServ.class.getName() + " as  pojo " +
		"join pojo.doctoServico as ds " +
		"where ds.idDoctoServico = :idDoctoServico ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idDoctoServico", idDoctoServico);
	}
	
	public Integer getRowCountByIdDoctoServicoIdCliente(Long idDoctoServico, Long idCliente) {
		return getAdsmHibernateTemplate().getRowCountForQuery("from "+DevedorDocServ.class.getName()+" dds where dds.doctoServico.id = ? and dds.cliente.id = ?", new Object[]{idDoctoServico, idCliente});
	}


}