package com.mercurio.lms.util.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.expedicao.DoctoServicoLookupPojo;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.model.dao.DoctoServicoLookupDAO;

/**
 * Serviços responsáveis por fornecer dados para serem usados nas tags
 * de documento de serviço encontradas no LMS.
 * 
 * @author felipef
 * @spring.bean id="lms.utils.DoctoServicoLookupService"
 */
public class DoctoServicoLookupService {

	private DoctoServicoLookupDAO dao;
	
	public List findCTR(DoctoServicoLookupPojo bean) {
		List l = dao.findCTR(bean);
		return transformList(l);
	}

	public List findCRT(DoctoServicoLookupPojo bean) {
		List l = dao.findCRT(bean);
		return transformList(l);
	}
	
	public List findNFT(DoctoServicoLookupPojo bean) {
		List l = dao.findNFT(bean);
		return transformList(l);
	}

	public List findMDA(DoctoServicoLookupPojo bean) {
		List l = dao.findMDA(bean);
		return transformList(l);
	}
	
	public List findRRE(DoctoServicoLookupPojo bean) {
		List l = dao.findRRE(bean);
		return transformList(l);
	}
	
	public List findNTE(DoctoServicoLookupPojo bean) {
		List l = dao.findNTE(bean);
		return transformList(l);
	}
	
	public List findCTE(DoctoServicoLookupPojo bean) {
		List l = dao.findCTE(bean);
		return transformList(l);
	}
	
	/**
	 * FIXME: Método não implementado.
	 * @param bean
	 * @return
	 */
	public List findNFS(DoctoServicoLookupPojo bean) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * FIXME: Método não implementado.
	 * @param bean
	 * @return
	 */
	public List findNDN(DoctoServicoLookupPojo bean) {
		// TODO Auto-generated method stub
		return null;
	}	
	
	public List findDoctoServicoByTpDoctoServicoByIdFilialOrigemByNumeroDoctoServico(Map criteria) {
		String tpDocumentoServico = (String)criteria.get("tpDocumentoServico");
		Long idFilialOrigem = (Long)criteria.get("idFilialOrigem");
		Long nrDoctoServico = (Long)criteria.get("nrDoctoServico");
		
		DoctoServicoLookupPojo doctoServicoLookupPojo = new DoctoServicoLookupPojo();
		doctoServicoLookupPojo.setIdFilialOrigem(idFilialOrigem);
		doctoServicoLookupPojo.setNrDoctoServico(nrDoctoServico);
		doctoServicoLookupPojo.setBlBloqueado(Boolean.FALSE);

		List list = null;		
		if (!StringUtils.isBlank(tpDocumentoServico)) {
			if (tpDocumentoServico.equals(ConstantesExpedicao.CONHECIMENTO_NACIONAL)) {
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, Conhecimento.class);
			} else if (tpDocumentoServico.equals(ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL)) {
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, CtoInternacional.class);
			} else if (tpDocumentoServico.equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE)) {
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, Conhecimento.class);
			} else if (tpDocumentoServico.equals(ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO)) {
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, Mda.class);
			} else if (tpDocumentoServico.equals(ConstantesExpedicao.RECIBO_REEMBOLSO)) {
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, ReciboReembolso.class);
			} else if (tpDocumentoServico.equals(ConstantesExpedicao.NOTA_FISCAL_SERVICO)) {
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, NotaFiscalServico.class);
			} else if(tpDocumentoServico.equals(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA)){
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, NotaFiscalServico.class);
			} else if(tpDocumentoServico.equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA)){
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, Conhecimento.class);
			} else if(tpDocumentoServico.equals(ConstantesExpedicao.CONHECIMENTO_ELETRONICO)){
				list = dao.findDoctoServico(doctoServicoLookupPojo, tpDocumentoServico, Conhecimento.class);
			} else if (tpDocumentoServico.equals(ConstantesExpedicao.NOTA_DEBITO_NACIONAL)) {
				return	null;
			}
		}
		
		return transformList(list); 
	}

	/**
	 * Método privado responsável por transformar uma lista de map's em
	 * uma lista de TypedFlatMap's. As chaves também são alteradas substituindo
	 * o caracter '_' pelo caracter '.'.
	 * 
	 * @param l List contendo Map's
	 * @return List contendo TypedFlatMap's
	 */
	private List transformList(List l) {
		AliasToTypedFlatMapResultTransformer a = new AliasToTypedFlatMapResultTransformer();
		return a.transformListResult(l);
	}	
	
	public void setDao(DoctoServicoLookupDAO dao) {
		this.dao = dao;
	}

}
