package com.mercurio.lms.expedicao;

import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * Interface contém os "findLookup's" utilizados nas tags de Documento de Serviço do LMS.
 * Todos os métodos recebem e tratam um objeto DoctoServicoLookupPojo que pode conter os 5 parâmetros a seguir:
 * 		nrDoctoServico: número do documento de serviço desejado.
 * 		idFilialOrigem: identificador da filial de origem do documento de serviço.
 * 			Se filial de origem é informada junto com o número do documento, garante-se que
 * 			a consulta retornará apenas um registro. 
 * 		idFilialDestino: identificador da filial de destino do documento de serviço.
 * 		idRemetente: identificador do cliente rementente do documento de serviço.
 * 		idDestinatario: identificador do cliente destinatário do documento de serviço.
 * 		blBloqueado: booleano identifica se Documento de Serviço está bloqueado.
 * 
 * A lista de retorno é composta por TypedFlatMap's, os quais contém as informações básicas
 * necessárias no retorno do findLookup na tag. Abaixo estão as chaves encontradas no Map:
 * 		'idDoctoServico'*: id do Documento de Serviço.
 * 		'nrDoctoServico'*: número do Documento de Serviço.
 * 		'filialByIdFilialOrigem.idFilial': id da filial origem que deve ser carregada na filial
 * 			origem que é encontrada na tag de Documento de Serviço do LMS.
 * 		'filialByIdFilialOrigem.sgFilial': sigla da filial origem que deve ser carregada na filial
 * 			origem que é encontrada na tag de Documento de Serviço do LMS.
 * 		'filialByIdFilialOrigem.pessoa.nmFantasia': nmFantasia da filial origem que deve ser
 * 			carregada na filial origem que é encontrada na tag de Documento de Serviço do LMS.
 * 		
 * 		* Por uma limitação do framework, atualmente (25/08/2006), a chave destes retornos variam
 * 			de acordo com o tipo de DoctoServico. Por exemplo, no findLookupCTR, o 'nrDoctoServico'
 * 			virá como 'nrConhecimento'. Já no findLookupCRT virá como 'ctoInternacional.nrCrt'.
 * 			Num futuro pode ser realizado um refactoring para padronização.
 * @since August, 2006
 * @author felipef
 */
public interface DoctoServicoLookupFacade {
	
	/**
	 * enum contendo os tipos de Documento de Serviço.
	 */
	public enum TipoDocumentoServico {CTR,CRT,NFT,MDA,RRE,NFS,NDN,NTE,CTE}; 
	
	
	/**
	 * FindLookup para o Documento de serviço do tipo CTRC
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupCTR(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de serviço do tipo CRT
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupCRT(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de serviço do tipo NFT
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupNFT(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de serviço do tipo MDA
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupMDA(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de serviço do tipo RRE
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupRRE(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de serviço do tipo NFS
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupNFS(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de serviço do tipo NDN
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupNDN(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de serviço do tipo NTE
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupNTE(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup para o Documento de serviço do tipo CTE
	 * 
	 * @param bean Objeto do tipo DoctoServicoLookupPojo com as propriedades desejadas
	 * 		como critério da consulta setadas.
	 * 
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookupCTE(DoctoServicoLookupPojo bean);
	
	/**
	 * FindLookup genérico para Documento de serviço.
	 * Deve-se obrigatoriamente informar um tipo de Documento de Serviço. Caso não
	 * seja informado, será retornado uma lista vazia (garantido na implementação).
	 * 
	 * @param criteria TypedFlatMap com chaves nos padrões definidos:
	 * 		nrDoctoServico* (pode varias de acordo com o tipo de Documento de Serviço)
	 * 		filialByIdFilialOrigem.idFilial
	 * 		filialByIdFilialDestino.idFilial
	 * 		clienteByIdClienteRemetente.idCliente
	 * 		clienteByIdClienteDestinatario.idCliente
	 * 
	 * @param tipo enum TipoDocumentoServico.
	 *  
	 * @return Lista de TypedFlatMap's contendo os properties definidos para tag de Documento de Serviço.
	 */
	public List findLookup(TypedFlatMap criteria, TipoDocumentoServico tipo);
	
	/**
	 * Transforma um TypedFlatMap (com properties seguindo o padrão definido para a tag de Documento
	 * de serviço) em um objeto do tipo com.mercurio.lms.expedicao.DoctoServicoLookupPojo.
	 * 
	 * @param tfm TypedFlatMap com chaves nos padrões definidos:
	 * 		nrDoctoServico
	 * 		filialByIdFilialOrigem.idFilial
	 * 		filialByIdFilialDestino.idFilial
	 * 		clienteByIdClienteRemetente.idCliente
	 * 		clienteByIdClienteDestinatario.idCliente
	 * 
	 * @return DoctoServicoLookupPojo objeto com as informações setadas.
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
