package com.mercurio.lms.entrega.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.dao.ReciboReembolsoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.consultarPosicaoReembolsosService"
 */
public class ConsultarPosicaoReembolsosService {
	
	private ReciboReembolsoDAO reciboReembolsoDAO;
	
	/**
	 * Consulta posicao do recibo no evento 'Mercadoria no manifesto de entrega'
	 * @param idReciboReembolso
	 * @return
	 */
	public Map findPosReciboMercadoriaManifestoEntrega(Long idReciboReembolso){
		List result = getDAO().findPosReciboMercadoriaManifestoEntrega(idReciboReembolso);
		if (!result.isEmpty())
			return (Map) result.get(0);
		else
			return null; 
	}
	
	/**
	 * Consulta posicao do recibo no evento 'Recebimento do reembolso no destinatario'
	 * @param idReciboReembolso
	 * @return
	 */
	public Map findPosReciboRecebimento(Long idReciboReembolso){
		List result = getDAO().findPosReciboRecebimento(idReciboReembolso);
		if (!result.isEmpty())
			return (Map) result.get(0);
		else
			return null; 
	}
	
	/**
	 * Consulta posicao do recibo nos eventos relativos ao MIR
	 * @param idReciboReembolso
	 * @return
	 */
	public List findPosReciboMIR(Long idReciboReembolso){
		return getDAO().findPosReciboMIR(idReciboReembolso);
	}
	
	/**
	 * Consulta posicao do recibo no evento 'Reembolso no Manifesto de Entrega'
	 * @param idReciboReembolso
	 * @return
	 */
	public Map findPosReciboManifestoEntrega(Long idReciboReembolso){
		List result = getDAO().findPosReciboManifestoEntrega(idReciboReembolso);
		if (!result.isEmpty())
			return (Map) result.get(0);
		else
			return null; 
	}
	
	/**
	 * Consulta posicao do recibo no evento 'Entrega do reembolso ao cliente'
	 * 
	 * @param idReciboReembolso
	 * @return
	 */
	public Map findPosReciboEntregaCliente(Long idReciboReembolso){
		List result = getDAO().findPosReciboEntregaCliente(idReciboReembolso);
		if (!result.isEmpty())
			return (Map) result.get(0);
		else
			return null; 
	}
	
	/**
	 * Consulta posicao do recibo no evento 'Aguardando Confirmação dos Cheques'
	 * @param idReciboReembolso
	 * @return
	 */
	public Map findPosReciboAguardaCheques(Long idReciboReembolso){
		List result = getDAO().findPosReciboAguardaCheques(idReciboReembolso);
		if (!result.isEmpty())
			return (Map) result.get(0);
		else
			return null; 
	}
	
	/**
	 * Consulta a posicao do reembolso no evento 'Reembolso Cancelado'
	 * @param idReciboReembolso
	 * @return
	 */
	public Map findPosReembolsoCancelado(Long idReciboReembolso){
		List result = getDAO().findPosReembolsoCancelado(idReciboReembolso);
		if (!result.isEmpty())
			return (Map) result.get(0);
		else
			return null;
	}
	
	/**
	 * Consulta posicao do recibo no evento 'Mercadoria Devolvida'
	 * 
	 * @param idReciboReembolso
	 * @return
	 */
	public Map findPosMercadoriaDevolvida(Long idReciboReembolso){
		List result = getDAO().findPosMercadoriaDevolvida(idReciboReembolso);
		if (!result.isEmpty())
			return (Map) result.get(0);
		else
			return null; 
	}
	/**
	 * Detalhamento da tela Consulta Posicao de Reembolsos
	 * 
	 * 
	 * @param parametros
	 * @param findDef
	 * @return
	 */
	public TypedFlatMap findByIdPosicaoReembolso(Long idReciboReembolso){
		return getDAO().findByIdPosicaoReembolso(idReciboReembolso);
	}
	
	/**
	 * Grid da tela Consulta Posicao de Reembolsos
	 * @param parametros
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginated(TypedFlatMap parametros){
		return getDAO().findGridPosicaoReembolso(parametros, FindDefinition.createFindDefinition(parametros));
	}
	
	/**
	 * RowCount da grid da tela Consulta Posicao de Reembolsos
	 * @param parametros
	 * @return
	 */
	public Integer getRowCount(TypedFlatMap criteria) {	
		return getDAO().getRowCountGridPosicaoReembolso(criteria);
	}
	
	public void setDAO(ReciboReembolsoDAO dao) {
		this.reciboReembolsoDAO = dao;
	}
	
	private ReciboReembolsoDAO getDAO() {
		return reciboReembolsoDAO;
	}
}
