package com.mercurio.lms.util.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.expedicao.DoctoServicoLookupPojo;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.pendencia.model.Mda;

/**
 * Métodos de acesso a dados responsáveis por persistir informações do documento
 * de serviço para as tags de documento de serviço encontradas no LMS.
 * 
 * @author felipef
 *
 * @spring.bean 
 */
public class DoctoServicoLookupDAO extends AdsmDao {
	public List findCTR(DoctoServicoLookupPojo bean) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(C.idDoctoServico","idDoctoServico");
		hql.addProjection("C.idDoctoServico","idConhecimento");
		hql.addProjection("C.nrConhecimento","nrConhecimento");
		hql.addProjection("FO.idFilial","filialByIdFilialOrigem_idFilial");
		hql.addProjection("FO.sgFilial","filialByIdFilialOrigem_sgFilial");
		hql.addProjection("PO.nmFantasia","filialByIdFilialOrigem_pessoa_nmFantasia)");

		StringBuffer hqlFrom = new StringBuffer()
				.append(Conhecimento.class.getName()).append(" C ")
				.append(" inner join C.filialByIdFilialOrigem FO ")
				.append(" inner join FO.pessoa PO ");
		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("C.nrConhecimento","=",bean.getNrDoctoServico());
		hql.addCriteria("C.filialByIdFilialOrigem.id","=",bean.getIdFilialOrigem());
		hql.addCriteria("C.filialByIdFilialDestino.id","=",bean.getIdFilialDestino());
		hql.addCriteria("C.clienteByIdClienteRemetente.id","=",bean.getIdRemetente());
		hql.addCriteria("C.clienteByIdClienteDestinatario.id","=",bean.getIdDestinatario());
		hql.addCriteria("C.blBloqueado","=",bean.getBlBloqueado());
		
		hql.addCriteria("C.tpDocumentoServico","=","CTR");
		
		return this.findLookupBySqlTemplate(hql);
	}

	public List findCTE(DoctoServicoLookupPojo bean) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(C.idDoctoServico","idDoctoServico");
		hql.addProjection("C.idDoctoServico","idConhecimento");
		hql.addProjection("C.nrConhecimento","nrConhecimento");
		hql.addProjection("FO.idFilial","filialByIdFilialOrigem_idFilial");
		hql.addProjection("FO.sgFilial","filialByIdFilialOrigem_sgFilial");
		hql.addProjection("PO.nmFantasia","filialByIdFilialOrigem_pessoa_nmFantasia)");

		StringBuffer hqlFrom = new StringBuffer()
				.append(Conhecimento.class.getName()).append(" C ")
				.append(" inner join C.filialByIdFilialOrigem FO ")
				.append(" inner join FO.pessoa PO ");
		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("C.nrConhecimento","=",bean.getNrDoctoServico());
		hql.addCriteria("C.filialByIdFilialOrigem.id","=",bean.getIdFilialOrigem());
		hql.addCriteria("C.filialByIdFilialDestino.id","=",bean.getIdFilialDestino());
		hql.addCriteria("C.clienteByIdClienteRemetente.id","=",bean.getIdRemetente());
		hql.addCriteria("C.clienteByIdClienteDestinatario.id","=",bean.getIdDestinatario());
		hql.addCriteria("C.blBloqueado","=",bean.getBlBloqueado());
		
		hql.addCriteria("C.tpDocumentoServico","=","CTE");
		
		return this.findLookupBySqlTemplate(hql);
	}

	public List findCRT(DoctoServicoLookupPojo bean) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(C.idDoctoServico","idDoctoServico");
		hql.addProjection("C.nrCrt","nrCrt");
		hql.addProjection("FO.idFilial","filialByIdFilialOrigem_idFilial");
		hql.addProjection("FO.sgFilial","filialByIdFilialOrigem_sgFilial");
		hql.addProjection("PO.nmFantasia","filialByIdFilialOrigem_pessoa_nmFantasia)");
		
		StringBuffer hqlFrom = new StringBuffer()
				.append(CtoInternacional.class.getName()).append(" C ")
				.append(" inner join C.filialByIdFilialOrigem FO ")
				.append(" inner join FO.pessoa PO ");
		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("C.nrCrt","=",bean.getNrDoctoServico());
		hql.addCriteria("C.filialByIdFilialOrigem.id","=",bean.getIdFilialOrigem());
		hql.addCriteria("C.filialByIdFilialDestino.id","=",bean.getIdFilialDestino());
		hql.addCriteria("C.clienteByIdClienteRemetente.id","=",bean.getIdRemetente());
		hql.addCriteria("C.clienteByIdClienteDestinatario.id","=",bean.getIdDestinatario());
		hql.addCriteria("C.blBloqueado","=",bean.getBlBloqueado());
		
		return this.findLookupBySqlTemplate(hql);
	}
	
	public List findNFT(DoctoServicoLookupPojo bean) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(C.idDoctoServico","idDoctoServico");
		hql.addProjection("C.idDoctoServico","idConhecimento");
		hql.addProjection("C.nrConhecimento","nrConhecimento");
		hql.addProjection("FO.idFilial","filialByIdFilialOrigem_idFilial");
		hql.addProjection("FO.sgFilial","filialByIdFilialOrigem_sgFilial");
		hql.addProjection("PO.nmFantasia","filialByIdFilialOrigem_pessoa_nmFantasia)");
		
		StringBuffer hqlFrom = new StringBuffer()
				.append(Conhecimento.class.getName()).append(" C ")
				.append(" inner join C.filialByIdFilialOrigem FO ")
				.append(" inner join FO.pessoa PO ");
		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("C.nrConhecimento","=",bean.getNrDoctoServico());
		hql.addCriteria("C.filialByIdFilialOrigem.id","=",bean.getIdFilialOrigem());
		hql.addCriteria("C.filialByIdFilialDestino.id","=",bean.getIdFilialDestino());
		hql.addCriteria("C.clienteByIdClienteRemetente.id","=",bean.getIdRemetente());
		hql.addCriteria("C.clienteByIdClienteDestinatario.id","=",bean.getIdDestinatario());
		hql.addCriteria("C.blBloqueado","=",bean.getBlBloqueado());
		
		hql.addCriteria("C.tpDocumentoServico","=","NFT");
		
		return this.findLookupBySqlTemplate(hql);
	}

	public List findNTE(DoctoServicoLookupPojo bean) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(C.idDoctoServico","idDoctoServico");
		hql.addProjection("C.idDoctoServico","idConhecimento");
		hql.addProjection("C.nrConhecimento","nrConhecimento");
		hql.addProjection("FO.idFilial","filialByIdFilialOrigem_idFilial");
		hql.addProjection("FO.sgFilial","filialByIdFilialOrigem_sgFilial");
		hql.addProjection("PO.nmFantasia","filialByIdFilialOrigem_pessoa_nmFantasia)");
		
		StringBuffer hqlFrom = new StringBuffer()
				.append(Conhecimento.class.getName()).append(" C ")
				.append(" inner join C.filialByIdFilialOrigem FO ")
				.append(" inner join FO.pessoa PO ");
		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("C.nrConhecimento","=",bean.getNrDoctoServico());
		hql.addCriteria("C.filialByIdFilialOrigem.id","=",bean.getIdFilialOrigem());
		hql.addCriteria("C.filialByIdFilialDestino.id","=",bean.getIdFilialDestino());
		hql.addCriteria("C.clienteByIdClienteRemetente.id","=",bean.getIdRemetente());
		hql.addCriteria("C.clienteByIdClienteDestinatario.id","=",bean.getIdDestinatario());
		hql.addCriteria("C.blBloqueado","=",bean.getBlBloqueado());
		
		hql.addCriteria("C.tpDocumentoServico","=","NTE");
		
		return this.findLookupBySqlTemplate(hql);
	}

	public List findMDA(DoctoServicoLookupPojo bean) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(M.idDoctoServico","idDoctoServico");
		hql.addProjection("M.nrDoctoServico","nrDoctoServico");
		hql.addProjection("FO.idFilial","filialByIdFilialOrigem_idFilial");
		hql.addProjection("FO.sgFilial","filialByIdFilialOrigem_sgFilial");
		hql.addProjection("PO.nmFantasia","filialByIdFilialOrigem_pessoa_nmFantasia)");
		
		StringBuffer hqlFrom = new StringBuffer()
				.append(Mda.class.getName()).append(" M ")
				.append(" inner join M.filialByIdFilialOrigem FO ")
				.append(" inner join FO.pessoa PO ");
		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("M.nrDoctoServico","=",bean.getNrDoctoServico());
		hql.addCriteria("M.filialByIdFilialOrigem.id","=",bean.getIdFilialOrigem());
		hql.addCriteria("M.filialByIdFilialDestino.id","=",bean.getIdFilialDestino());
		hql.addCriteria("M.clienteByIdClienteRemetente.id","=",bean.getIdRemetente());
		hql.addCriteria("M.clienteByIdClienteDestinatario.id","=",bean.getIdDestinatario());
		hql.addCriteria("M.blBloqueado","=",bean.getBlBloqueado());
		
		return this.findLookupBySqlTemplate(hql);
	}

	public List findRRE(DoctoServicoLookupPojo bean) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(RR.idDoctoServico","idDoctoServico");
		hql.addProjection("RR.nrReciboReembolso","nrReciboReembolso");
		hql.addProjection("FO.idFilial","filialByIdFilialOrigem_idFilial");
		hql.addProjection("FO.sgFilial","filialByIdFilialOrigem_sgFilial");
		hql.addProjection("PO.nmFantasia","filialByIdFilialOrigem_pessoa_nmFantasia)");
		
		StringBuffer hqlFrom = new StringBuffer()
				.append(ReciboReembolso.class.getName()).append(" RR ")
				.append(" inner join RR.filialByIdFilialOrigem FO ")
				.append(" inner join FO.pessoa PO ");
		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("RR.nrReciboReembolso","=", Integer.valueOf(bean.getNrDoctoServico().intValue()));
		hql.addCriteria("RR.filialByIdFilialOrigem.id","=",bean.getIdFilialOrigem());
		hql.addCriteria("RR.filialByIdFilialDestino.id","=",bean.getIdFilialDestino());
		hql.addCriteria("RR.clienteByIdClienteRemetente.id","=",bean.getIdRemetente());
		hql.addCriteria("RR.clienteByIdClienteDestinatario.id","=",bean.getIdDestinatario());
		hql.addCriteria("RR.blBloqueado","=",bean.getBlBloqueado());
		
		return this.findLookupBySqlTemplate(hql);
	}

	public List findNFS(DoctoServicoLookupPojo bean) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List findNDN(DoctoServicoLookupPojo bean) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List findLookupBySqlTemplate(SqlTemplate hql) {
		return getAdsmHibernateTemplate().findPaginated(
				hql.getSql(),
				Integer.valueOf(1),
				Integer.valueOf(2),
				hql.getCriteria()).getList();
	}

	public List findDoctoServico(DoctoServicoLookupPojo bean, String tpDoctoServico, Class clazz) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(C.idDoctoServico","idDoctoServico");
		hql.addProjection("C.nrDoctoServico","nrDoctoServico");
		if (clazz == Conhecimento.class)
			hql.addProjection("C.dvConhecimento", "dvConhecimento");
		hql.addProjection("FO.idFilial","filialByIdFilialOrigem_idFilial");
		hql.addProjection("FO.sgFilial","filialByIdFilialOrigem_sgFilial");
		hql.addProjection("PO.nmFantasia","filialByIdFilialOrigem_pessoa_nmFantasia");
		hql.addProjection("C.servico.id", "idServico)");			
		
		StringBuffer hqlFrom = new StringBuffer()
				.append(clazz.getName()).append(" C ")
				.append(" inner join C.filialByIdFilialOrigem FO ")
				.append(" inner join FO.pessoa PO ");
		hql.addFrom(hqlFrom.toString());
		
		hql.addCriteria("C.nrDoctoServico","=",bean.getNrDoctoServico());
		hql.addCriteria("C.filialByIdFilialOrigem.id","=",bean.getIdFilialOrigem());
		hql.addCriteria("C.filialByIdFilialDestino.id","=",bean.getIdFilialDestino());
		hql.addCriteria("C.clienteByIdClienteRemetente.id","=",bean.getIdRemetente());
		hql.addCriteria("C.clienteByIdClienteDestinatario.id","=",bean.getIdDestinatario());
		hql.addCriteria("C.blBloqueado","=",bean.getBlBloqueado());
		
		hql.addCriteria("C.tpDocumentoServico","=",tpDoctoServico);
		
		return this.findLookupBySqlTemplate(hql);
	}
	
}
