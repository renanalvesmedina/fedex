package com.mercurio.lms.expedicao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.model.service.DoctoServicoLookupService;

/**
 * Implementação dos métodos findLookup's de Documento de Serviço.
 * Documentado o comportamento padrão dos métodos na Interface. 
 *  
 * @author felipef
 * @see com.mercurio.lms.expedicao.DoctoServicoLookup 
 * @since August, 2006
 * 
 * @spring.bean id="lms.DoctoServicoLookupFacade"
 */
public class DoctoServicoLookupFacadeImpl implements DoctoServicoLookupFacade {

	private DoctoServicoLookupService service;
	
	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupCTR(DoctoServicoLookupPojo bean)
	 */
	public List findLookupCTR(DoctoServicoLookupPojo bean) {
		return service.findCTR(bean);
	}

	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupCRT(DoctoServicoLookupPojo bean)
	 */
	public List findLookupCRT(DoctoServicoLookupPojo bean) {
		return service.findCRT(bean);
	}
	
	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupNFT(DoctoServicoLookupPojo bean)
	 */
	public List findLookupNFT(DoctoServicoLookupPojo bean) {
		return service.findNFT(bean);
	}

	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupMDA(DoctoServicoLookupPojo bean)
	 */
	public List findLookupMDA(DoctoServicoLookupPojo bean) {
		return service.findMDA(bean);
	}
	
	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupRRE(DoctoServicoLookupPojo bean)
	 */
	public List findLookupRRE(DoctoServicoLookupPojo bean) {
		return service.findRRE(bean);
	}

	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupNTE(DoctoServicoLookupPojo bean)
	 */
	public List findLookupNTE(DoctoServicoLookupPojo bean) {
		return service.findNTE(bean);
	}
	
	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupCTE(DoctoServicoLookupPojo bean)
	 */
	public List findLookupCTE(DoctoServicoLookupPojo bean) {
		return service.findCTE(bean);
	}

	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupNFS(DoctoServicoLookupPojo bean)
	 */
	public List findLookupNFS(DoctoServicoLookupPojo bean) {
		return service.findNFS(bean);
	}
	
	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookupNDN(DoctoServicoLookupPojo bean)
	 */
	public List findLookupNDN(DoctoServicoLookupPojo bean) {
		return service.findNDN(bean);
	}
		
	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#findLookup(TypedFlatMap criteria, TipoDocumentoServico tipo)
	 */
	public List findLookup(TypedFlatMap criteria, TipoDocumentoServico tipo) {
		List l = null;
		DoctoServicoLookupPojo d = this.defaultTypedFlatMap2Bean(criteria);
		
		if (tipo.equals(TipoDocumentoServico.CRT)) {
			d.setNrDoctoServico(criteria.getLong("nrCrt"));
			l = this.findLookupCRT(d);
		}
		else if (tipo.equals(TipoDocumentoServico.CTR)) {
			d.setNrDoctoServico(criteria.getLong("nrConhecimento"));
			l = this.findLookupCTR(d);
		}
		else if (tipo.equals(TipoDocumentoServico.MDA)) {
			l = this.findLookupMDA(d);
		}
		else if (tipo.equals(TipoDocumentoServico.NDN)) {
			l = this.findLookupNDN(d);
		}
		else if (tipo.equals(TipoDocumentoServico.NFS)) {
			l = this.findLookupNFS(d);
		}
		else if (tipo.equals(TipoDocumentoServico.NFT)) {
			d.setNrDoctoServico(criteria.getLong("nrConhecimento"));
			l = this.findLookupNFT(d);
		}
		else if (tipo.equals(TipoDocumentoServico.RRE)) {
			d.setNrDoctoServico(criteria.getLong("nrReciboReembolso"));
			l = this.findLookupRRE(d);
		}
		else if (tipo.equals(TipoDocumentoServico.NTE)) {
			d.setNrDoctoServico(criteria.getLong("nrConhecimento"));
			l = this.findLookupNTE(d);
		}
		else if (tipo.equals(TipoDocumentoServico.CTE)) {
			d.setNrDoctoServico(criteria.getLong("nrConhecimento"));
			l = this.findLookupCTE(d);
		}
		else {
			l = new ArrayList(0);
		}
		
		return l;
	}
	
	/**
	 * @see com.mercurio.lms.expedicao.DoctoServicoLookupFacade#defaultTypedFlatMap2Bean(TypedFlatMap tfm)
	 */
	public DoctoServicoLookupPojo defaultTypedFlatMap2Bean(TypedFlatMap tfm) {
		DoctoServicoLookupPojo d = new DoctoServicoLookupPojo();

		d.setNrDoctoServico(tfm.getLong("nrDoctoServico"));
		d.setIdFilialOrigem(tfm.getLong("filialByIdFilialOrigem.idFilial"));
		d.setIdFilialDestino(tfm.getLong("filialByIdFilialDestino.idFilial"));
		d.setIdRemetente(tfm.getLong("clienteByIdClienteRemetente.idCliente"));
		d.setIdDestinatario(tfm.getLong("clienteByIdClienteDestinatario.idCliente"));

		Boolean blBloqueado = tfm.getBoolean("blBloqueado");
		if(blBloqueado == null) {
			blBloqueado = Boolean.FALSE;
		}
		d.setBlBloqueado(blBloqueado);

		return d;
	}

	public void setService(DoctoServicoLookupService service) {
		this.service = service;
	}

	/**
	 * Formata a filial-documento de acordo com as regras para seu tipo
	 * @param tpDocumento
	 * @param sgFilial
	 * @param nrDocumento
	 * @return
	 */
	public String formatarDocumentoByTipo(String tpDocumento, String sgFilial, String nrDocumento) {
		String toReturn = nrDocumento;
		if (tpDocumento.equals(ConstantesEventosDocumentoServico.TP_DOCTO_PED_NACIONAL)
				|| tpDocumento.equals(ConstantesEventosDocumentoServico.TP_DOCTO_PEI_INTERNACIONAL)) {
			toReturn = nrDocumento;
		} else if (tpDocumento.equals(ConstantesEventosDocumentoServico.TP_DOCTO_CONTROLE_CARGA)
				|| tpDocumento.equals(ConstantesEventosDocumentoServico.TP_DOCTO_MANIFESTO_VIAGEM)
				|| tpDocumento.equals(ConstantesEntrega.MANIFESTO_ENTREGA)
				|| tpDocumento.equals(ConstantesExpedicao.MANIFESTO_ITERNACIONAL_CARGA)
				|| tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_NACIONAL)
				|| tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_ELETRONICO)
				|| tpDocumento.equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE)
				|| tpDocumento.equals(ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO)
				|| tpDocumento.equals(ConstantesExpedicao.RECIBO_REEMBOLSO)) {
			toReturn = formatarDocumento(sgFilial, nrDocumento, 8);
		} else if (tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL)
				|| tpDocumento.equals(ConstantesEventosDocumentoServico.TP_DOCTO_MID)
				|| tpDocumento.equals(ConstantesExpedicao.AIRWAY_BILL)) {
			toReturn = formatarDocumento(sgFilial, nrDocumento, 6);
		}
		return toReturn;
	}
	
	/**
	 * Formata a filial-documento de acordo com o nro de caracteres
	 * @param tpDocumento
	 * @param sgFilial
	 * @param nrDocumento
	 * @return
	 */
	private String formatarDocumento(String sgFilial, String nrDocumento, int chars) {
		String format = String.valueOf(nrDocumento);
		for (int i = nrDocumento.toString().length(); i < chars; i++) {
			format = ("0" + format);
		}
		if (sgFilial != null && ! "".equals(sgFilial)) {
			format = (sgFilial + " " + format);
		}
		return format;
	}
}
