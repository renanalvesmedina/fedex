package com.mercurio.lms.prestcontasciaaerea.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.prestcontasciaaerea.model.ValorPrestacaoConta;
import com.mercurio.lms.prestcontasciaaerea.model.dao.ValorPrestacaoContaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.prestcontasciaaerea.valorPrestacaoContaService"
 */
public class ValorPrestacaoContaService extends CrudService<ValorPrestacaoConta, Long> {


	/**
	 * Busca Valor do Tipo de Presta��o de Conta pelo Tipo de Valor informado.<BR>
	 *@author Robson Edemar Gehl
	 * @param tpValor
	 * @return
	 */
	public BigDecimal findVlTipoPrestacaoConta(String tpValor, Long idPrestacaoConta){
		return getValorPrestacaoContaDAO().findVlTipoPrestacaoConta(tpValor, idPrestacaoConta);
	}

	/**
	 * Recupera uma inst�ncia de <code>ValorPrestacaoConta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ValorPrestacaoConta findById(java.lang.Long id) {
        return (ValorPrestacaoConta)super.findById(id);
    }
    
    /**
     * Busca Valores do Tipo de Prestacao pela Prestacao de Conta.<BR>
     *@author Robson Edemar Gehl
     * @param idPrestacaoConta
     * @param tpFormaPagamento
     * @return
     */
    public List findVlTipoPrestacaoConta(Long idPrestacaoConta){
    	return getValorPrestacaoContaDAO().findVlTipoPrestacaoConta(idPrestacaoConta);
    }

    /**
     * Totaliza os valores dos Tipos de Presta��o de Contas, agrupados pelo Tipo de Valor.<BR>
     * Exemplo de Tipos de Valor: QA, FR, TC, TD e PE.<BR>
     *@author Robson Edemar Gehl
     * @param list
     * @return list
     */
    public List findTotaisByTpValor(Long idPrestacaoConta){
    	return getValorPrestacaoContaDAO().findTotalByTpValor(idPrestacaoConta);
    }
    
    /**
     * Totaliza os valores dos Tipos de Presta��o de Contas por idPrestacao e tpValor
     *@author Edenilson
     */
    public BigDecimal findTotalByTpValor(Long idPrestacaoConta, String tpValor){
    	return getValorPrestacaoContaDAO().findTotalByTpValor(idPrestacaoConta, tpValor);
    }

    /**
     * Totaliza os valores dos Tipos de Presta��o de Contas por idPrestacao, tpValor e tpFormaPagamento
     *@author Edenilson
     */
    public BigDecimal findTotalByTpValor(Long idPrestacaoConta, String tpValor, String tpFormaPagamento){
    	return getValorPrestacaoContaDAO().findTotalByTpValor(idPrestacaoConta, tpValor, tpFormaPagamento);
    }

    /**
     * Totaliza os valores dos Tipos de Presta��o de Contas, agrupados por Forma de pagamento.<BR>
     *@author Robson Edemar Gehl
     * @param idPrestacaoConta
     * @return
     */
    public List findTotaisByTpFormaPagamento(Long idPrestacaoConta){
    	return getValorPrestacaoContaDAO().findTotaisByTpFormaPagamento(idPrestacaoConta, null);
    }

    /**
     * Totaliza os valores dos Tipos de Presta��o de Contas da forma de pagamento informada.<BR>
     *@author Robson Edemar Gehl
     * @param idPrestacaoConta
     * @param tpFormaPagamento
     * @return
     */
    public BigDecimal findTotaisByTpFormaPagamento(Long idPrestacaoConta, String tpFormaPagamento){
    	List list = getValorPrestacaoContaDAO().findTotaisByTpFormaPagamento(idPrestacaoConta, tpFormaPagamento);
    	if (!list.isEmpty()){
    		Map map = (Map) list.get(0);
    		return (BigDecimal) map.get("vlTipoPrestacaoConta");
    	}
    	return null;
    }
    
	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ValorPrestacaoConta bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setValorPrestacaoContaDAO(ValorPrestacaoContaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ValorPrestacaoContaDAO getValorPrestacaoContaDAO() {
        return (ValorPrestacaoContaDAO) getDao();
    }
    
    /**
     * Remove os registros de valores da presta��o em quest�o
     * @param idPrestacaoConta
     */
    public void removeDesmarcarPrestacaoConta(Long idPrestacaoConta){
    	
    	getValorPrestacaoContaDAO().removeDesmarcarPrestacaoConta(idPrestacaoConta);
    	
    }
    
   }