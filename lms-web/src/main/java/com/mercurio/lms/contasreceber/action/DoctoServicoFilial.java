package com.mercurio.lms.contasreceber.action;

import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * Interface para m�todos do componente Documento de Servi�o.<BR>
 * Representa os m�todos utilizados para a busca da Filial.<BR>
 */
public interface DoctoServicoFilial {

	public List findLookupServiceDocumentFilialCTR(TypedFlatMap criteria);
	public List findLookupServiceDocumentFilialCRT(TypedFlatMap criteria);
	public List findLookupServiceDocumentFilialNFS(TypedFlatMap criteria);
	public List findLookupServiceDocumentFilialNFT(TypedFlatMap criteria);
	public List findLookupServiceDocumentFilialMDA(TypedFlatMap criteria);
	public List findLookupServiceDocumentFilialRRE(TypedFlatMap criteria);
	public List findLookupServiceDocumentFilialNDN(TypedFlatMap criteria);	
	public List findLookupServiceDocumentFilialNTE(TypedFlatMap criteria);
	public List findLookupServiceDocumentFilialNSE(TypedFlatMap criteria);
	public List findLookupServiceDocumentFilialCTE(TypedFlatMap criteria);
	
}
