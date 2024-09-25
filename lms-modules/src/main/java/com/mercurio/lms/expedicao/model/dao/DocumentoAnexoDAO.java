package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DocumentoAnexo;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class DocumentoAnexoDAO extends BaseCrudDao<DocumentoAnexo, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return DocumentoAnexo.class;
	}
	
	public java.util.List findByIdDoctoServico(java.lang.Long idCtoInternacional){
		StringBuilder hql = new StringBuilder()
		.append("select	new Map(da.idDocumentoAnexo as idDocumentoAnexo\n")
		.append("      	,da.dsDocumento as dsDocumento\n")
		.append("      	,daads.idAnexoDoctoServico as anexoDoctoServico_idAnexoDoctoServico\n")
		.append("      	,daads.dsAnexoDoctoServico as anexoDoctoServico_dsAnexoDoctoServico\n")
		.append(")\n")
		.append("from	").append(getPersistentClass().getName()).append(" as da\n")
		.append("join	da.anexoDoctoServico as daads\n")
		.append("join	da.ctoInternacional as daci\n")
		.append("where	daci.id = ").append(idCtoInternacional)
		;

		List result = getAdsmHibernateTemplate().find(hql.toString());
		if(result != null && !result.isEmpty()){
			result = new AliasToNestedBeanResultTransformer(getPersistentClass()).transformListResult(result);
			return result;
		}

		return null;
	}

	public void removeByIdDoctoServico(Long id, Boolean isFlushSession){
		StringBuilder hql = new StringBuilder()
		.append("delete	").append(getPersistentClass().getName()).append("\n")
		.append("where	ctoInternacional = :id")
		;
		CtoInternacional ctoInternacional = new CtoInternacional();
		ctoInternacional.setIdDoctoServico(id);

		getAdsmHibernateTemplate().removeById(hql.toString(), ctoInternacional);

		if(isFlushSession.booleanValue()) getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}
	
	public List findDoctoServicoByFaturaPedido(Long idClienteDest,Long idClienteRem, String fatura, VarcharI18n identificaFatura ){
		SqlTemplate hql = new SqlTemplate();
		
		
		hql.addProjection("new Map(coI.idDoctoServico as idDoctoServicoConsulta)");
		
		hql.addFrom(DocumentoAnexo.class.getName()+ " da " +
				"join da.ctoInternacional coI " +
				"left outer join coI.clienteByIdClienteRemetente rem " +
				"left outer join coI.clienteByIdClienteDestinatario dest " +
				"join da.anexoDoctoServico aDocServ ");
		
		hql.addCriteria("rem.idCliente", "=", idClienteRem);
		hql.addCriteria("dest.idCliente","=", idClienteDest);
		hql.addCriteria(PropertyVarcharI18nProjection.createProjection("aDocServ.dsAnexoDoctoServico"),"like",identificaFatura.getValue());
		hql.addCriteria("da.dsDocumento","=", fatura);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		
	}
	
}