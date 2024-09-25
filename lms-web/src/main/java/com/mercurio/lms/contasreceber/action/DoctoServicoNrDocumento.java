package com.mercurio.lms.contasreceber.action;

import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * Interface para métodos do componente Documento de Serviço.<BR>
 * Representa os métodos utilizados para a busca do Documento.<BR>
 */
public interface DoctoServicoNrDocumento {
	
	public List findLookupServiceDocumentNumberCTR(TypedFlatMap criteria);
	public List findLookupServiceDocumentNumberCRT(TypedFlatMap criteria);
	public List findLookupServiceDocumentNumberNFS(TypedFlatMap criteria);
	public List findLookupServiceDocumentNumberNFT(TypedFlatMap criteria);
	public List findLookupServiceDocumentNumberMDA(TypedFlatMap criteria);
	public List findLookupServiceDocumentNumberRRE(TypedFlatMap criteria);
	public List findLookupServiceDocumentNumberNDN(TypedFlatMap criteria);	
	public List findLookupServiceDocumentNumberNTE(TypedFlatMap criteria);
	public List findLookupServiceDocumentNumberNSE(TypedFlatMap criteria);
	public List findLookupServiceDocumentNumberCTE(TypedFlatMap criteria);
}
