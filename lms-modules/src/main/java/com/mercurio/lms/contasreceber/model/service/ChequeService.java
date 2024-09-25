package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Cheque;
import com.mercurio.lms.contasreceber.model.HistoricoCheque;
import com.mercurio.lms.contasreceber.model.dao.ChequeDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.chequeService"
 */
public class ChequeService extends CrudService<Cheque, Long> {
	
	HistoricoChequeService historicoChequeService;
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setChequeDAO(ChequeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    public ChequeDAO getChequeDAO() {
        return (ChequeDAO) getDao();
    }	
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */    
    public java.io.Serializable store(Cheque bean) {
    	return super.store(bean);
    }	

	/**
     * Método que busca os cheques de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
	public ResultSetPage findPaginated(TypedFlatMap criteria){
		return getChequeDAO().findPaginated(criteria);
	}		

    /**
     * Método que retorna o número de registros de acordo com os filtros passados
     * @param criteria
     * @return Integer quantidade de registros encontrados
     */
	public Integer getRowCount(TypedFlatMap criteria){
		return getChequeDAO().getRowCount(criteria);
	}
		
	public String findHistoricoChequeByIdCheque(Long idCheque){
		return getChequeDAO().findHistoricoChequeByIdCheque(idCheque);
	}
	
	/**
	 * Retorna a lista de cheque Ativo por Lote.
	 * 
	 * @author Mickaël Jalbert
	 * 22/03/2006
	 * 
	 * @param Long idLoteCheque
	 * */	
	public List findAtivoByLoteCheque(Long idLoteCheque){
		return this.getChequeDAO().findChequeByLoteSituacao(idLoteCheque, "A");
	}
	
	/**
	 * Retorna a lista de cheque Ativo por Lote.
	 * 
	 * @author Mickaël Jalbert
	 * 22/03/2006
	 * 
	 * @param Long idLoteCheque
	 * */	
	public List findByLoteCheque(Long idLoteCheque){
		return this.getChequeDAO().findByCriterios(idLoteCheque, null, null, null, null);
	}	
	
	public List findLookupCheque(Long nrCheque, Long idFilial){		
		return getChequeDAO().findByCriterios(null,null,null, nrCheque, idFilial);
	}
	
	/**
	 * Pesquisa de cheques pelo id do cheque 
	 * @param Long idCheque
	 * @return Cheque selecionado 
	 */
	public Cheque findById(Long idCheque){
		return this.getChequeDAO().findById(idCheque);
	}	
	
	public Cheque findChequeById(Long idCheque){
		return this.getChequeDAO().findChequeById(idCheque);
	}	
	
	/**
	 * Altera a data de vencimento do cheque. Utilizado pela tela de movimento de cheques
	 * autor Edenilson 
	 * @param idCheque
	 * @param idUltimoHistorico
	 * @param novaDataVencimento
	 */
	public void storeAlteraDataVencimento(Long idCheque, Long idUltimoHistorico, YearMonthDay novaDataVencimento){

		// Buscar o historicoCheque passado por parâmetro
		HistoricoCheque historicoChequeOld = historicoChequeService.findById(idUltimoHistorico);

		// O último histórico do cheque deve ser RM - Recebido Matriz
		String tpHistCheOld = historicoChequeOld.getTpHistoricoCheque().getValue();
		if (!tpHistCheOld.equals("RM")) {
			throw new BusinessException("LMS-36173");
		}
		
		// O cheque não pode ter sido LI - Liquidado
		List lst = new ArrayList();
		lst.add("LI");
		if (historicoChequeService.validadeExisteHistorico(idCheque, lst)){
			throw new BusinessException("LMS-36174");
		}
		
		Cheque cheque = this.findChequeById(idCheque);
		cheque.setDtVencimento(novaDataVencimento);
		this.store(cheque);

	}

	public void setHistoricoChequeService(
			HistoricoChequeService historicoChequeService) {
		this.historicoChequeService = historicoChequeService;
	}
}