package com.mercurio.lms.expedicao;

import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * Interface cont�m os "findLookup's" utilizados nas tags de Documento de Servi�o do LMS.
 * Todos os m�todos recebem e tratam um objeto DoctoServicoLookupPojo que pode conter os 5 par�metros a seguir:
 * 		nrDoctoServico: n�mero do documento de servi�o desejado.
 * 		idFilialOrigem: identificador da filial de origem do documento de servi�o.
 * 			Se filial de origem � informada junto com o n�mero do documento, garante-se que
 * 			a consulta retornar� apenas um registro. 
 * 		idFilialDestino: identificador da filial de destino do documento de servi�o.
 * 		idRemetente: identificador do cliente rementente do documento de servi�o.
 * 		idDestinatario: identificador do cliente destinat�rio do documento de servi�o.
 * 		blBloqueado: booleano identifica se Documento de Servi�o est� bloqueado.
 * 
 * A lista de retorno � composta por TypedFlatMap's, os quais cont�m as informa��es b�sicas
 * necess�rias no retorno do findLookup na tag. Abaixo est�o as chaves encontradas no Map:
 * 		'idDoctoServico'*: id do Documento de Servi�o.
 * 		'nrDoctoServico'*: n�mero do Documento de Servi�o.
 * 		'filialByIdFilialOrigem.idFilial': id da filial origem que deve ser carregada na filial
 * 			origem que � encontrada na tag de Documento de Servi�o do LMS.
 * 		'filialByIdFilialOrigem.sgFilial': sigla da filial origem que deve ser carregada na filial
 * 			origem que � encontrada na tag de Documento de Servi�o do LMS.
 * 		'filialByIdFilialOrigem.pessoa.nmFantasia': nmFantasia da filial origem que deve ser
 * 			carregada na filial origem que � encontrada na tag de Documento de Servi�o do LMS.
 * 		
 * 		* Por uma limita��o do framework, atualmente (25/08/2006), a chave destes retornos variam
 * 			de acordo com o tipo de DoctoServico. Por exemplo, no findLookupCTR, o 'nrDoctoServico'
 * 			vir� como 'nrConhecimento'. J� no findLookupCRT vir� como 'ctoInternacional.nrCrt'.
 * 			Num futuro pode ser realizado um refactoring para padroniza��o.
 * @since August, 2006
 * @author felipef
 */
public interface DoctoServicoLookupFacade {
	
	/**
	 * enum contendo os tipos de Documento de Servi�o.
	 */
	public enum TipoDocumentoServico {CTR,CRT,NFT,MDA,RRE,NFS,NDN,NTE,CTE}; 
	
	
	/**
	 * FindLookup para o Documento de servi�o do tipo CTRC
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupCTR(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de servi�o do tipo CRT
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupCRT(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de servi�o do tipo NFT
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupNFT(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de servi�o do tipo MDA
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupMDA(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de servi�o do tipo RRE
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupRRE(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de servi�o do tipo NFS
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupNFS(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de servi�o do tipo NDN
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupNDN(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de servi�o do tipo NTE
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupNTE(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de servi�o do tipo CTE
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como crit�rio da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookupCTE(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup gen�rico para Documento de servi�o.
	 * Deve-se obrigatoriamente informar um tipo de Documento de Servi�o. Caso n�o
	 * seja informado, ser� retornado uma lista vazia (garantido na implementa��o).
	 * 
	 * @param criteria TypedFlatMap com chaves nos padr�es definidos:
	 * 		nrDoctoServico* (pode varias de acordo com o tipo de Documento de Servi�o)
	 * 		filialByIdFilialOrigem.idFilial
	 * 		filialByIdFilialDestino.idFilial
	 * 		clienteByIdClienteRemetente.idCliente
	 * 		clienteByIdClienteDestinatario.idCliente
	 * 
	 * @param tipo enum TipoDocumentoServico.
	 *  
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Servi�o.
	 */
	public List findLookup(TypedFlatMap criteria, TipoDocumentoServico tipo);
	
	/**
	 * Transforma um TypedFlatMap (com properties seguindo o padr�o definido para a tag de Documento
	 * de servi�o) em um objeto do tipo com.mercurio.lms.expedicao.DoctoServicoLookupPojo.
	 * 
	 * @param tfm TypedFlatMap com chaves nos padr�es definidos:
	 * 		nrDoctoServico
	 * 		filialByIdFilialOrigem.idFilial
	 * 		filialByIdFilialDestino.idFilial
	 * 		clienteByIdClienteRemetente.idCliente
	 * 		clienteByIdClienteDestinatario.idCliente
	 * 
	 * @return DoctoServicoLookupPojo objeto com as informa��es setadas.
	 */
	public DoctoServicoLookupPojo defaultTypedFlatMap2Bean(TypedFlatMap tfm);
	
	/**
	 * Formata a filial-documento de acordo com as regras para seu tipo
	 * @param tpDocumento
	 * @param sgFilial
	 * @param nrDocumento
	 * @return
	 */
	public String formatarDocumentoByTipo(String tpDocumento, String sgFilial, String nrDocumento);
}
