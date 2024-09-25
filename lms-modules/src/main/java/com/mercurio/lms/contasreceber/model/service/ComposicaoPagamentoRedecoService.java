package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ComposicaoPagamentoRedeco;
import com.mercurio.lms.contasreceber.model.CreditoBancarioEntity;
import com.mercurio.lms.contasreceber.model.dao.ComposicaoPagamentoRedecoDAO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe de servi�o para CRUD:   
 * 
 * N�o inserir documenta��oo ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.composicaoPagamentoRedecoService"
 */
public class ComposicaoPagamentoRedecoService extends CrudService<ComposicaoPagamentoRedeco, Long> {

	private RedecoService redecoService;
	private CreditoBancarioService creditoBancarioService;
	
	@Override
	public ComposicaoPagamentoRedeco findById(Long id) {
		ComposicaoPagamentoRedeco composicao = (ComposicaoPagamentoRedeco)super.findById(id);
		composicao.getTpComposicaoPagamentoRedeco();
		if(composicao.getFilial() != null){
			if(composicao.getFilial().getPessoa()!=null){
				composicao.getFilial().getPessoa().getNmFantasia();
			}
			composicao.getFilial().getSgFilial();
		}
		
		if(composicao.getCreditoBancario() == null){
			return composicao;
		}
		CreditoBancarioEntity creditoBancario = creditoBancarioService.findById(composicao.getCreditoBancario().getIdCreditoBancario()) ;
		if(creditoBancario == null){
			return composicao;
		}
		if(creditoBancario.getBanco()!=null){
			creditoBancario.getBanco().getNmBanco();
		}

		composicao.setCreditoBancario(creditoBancario);
		
		return composicao;
	}

	
	public List<ComposicaoPagamentoRedeco> findByIdRedeco(Long idRedeco) {
		return getComposicaoPagamentoRedecoDAO().findByIdRedeco(idRedeco);
	}

	public List<ComposicaoPagamentoRedeco> findByIdRedecoTpComposicao(Long idRedeco, String tpComposicaoPagamentoRedeco) {
		return getComposicaoPagamentoRedecoDAO().findByIdRedecoTpComposicao(idRedeco, tpComposicaoPagamentoRedeco);
	}
	
	
    @Override
	public Serializable store(ComposicaoPagamentoRedeco bean) {
        return super.store(bean);
    }	
    
	@Override
	public void removeByIds(List<Long> ids) {
		
		/*N�o � poss�vel excluir registros com status DI e EM ou que a digita��o esteja concluida*/
		ComposicaoPagamentoRedeco cpr = null;
		for(Long id : ids){
			cpr = findById(id);
			if(!SessionUtils.isIntegrationRunning()){
			if((!"DI".equals(cpr.getRedeco().getTpSituacaoRedeco().getValue()) 
					&& !"EM".equals(cpr.getRedeco().getTpSituacaoRedeco().getValue())) 
					|| ("S".equals(cpr.getRedeco().getBlDigitacaoConcluida().getValue())) ){
				throw new BusinessException("LMS-27081");
			}
		}
		}
		/*Remove o registro*/
		super.removeByIds(ids);
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}	   
	
	/**
	 * Faz a pagina��o da tela de ManterCompPagamentoRedecoList
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedComposicao(TypedFlatMap criteria) {
		return getComposicaoPagamentoRedecoDAO().findPaginated(criteria);
	}
	
	/**
	 * Obtem o numero de registros atrav�s da consulta da tela
	 * de listagem 
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCount(TypedFlatMap criteria) {		
		return getComposicaoPagamentoRedecoDAO().getRowCount(criteria);
	}
	
	
	
	/**
	 * Calcular valor l�quido das composi��es de pagamento do redeco.
	 */
	public BigDecimal findValorLiquidoComposicoesPagamentoRedeco(Long idRedeco) {
    	BigDecimal vlTotalPagtos = BigDecimalUtils.defaultBigDecimal(this.findSumCompPagamentoRedeco(idRedeco, false));
    	
    	BigDecimal vlTotalClassificar = BigDecimalUtils.defaultBigDecimal(this.findSumCompPagamentoRedeco(idRedeco, true));
    	
		return BigDecimalUtils.defaultBigDecimal(vlTotalPagtos).subtract(BigDecimalUtils.defaultBigDecimal(vlTotalClassificar));
	}

	/**
	 * Retorna a maior data de pagamento das composi��es do redeco informado
	 * 
	 * @param idRedeco
	 * @return
	 */
	public YearMonthDay findMaiorDataCredito(Long idRedeco) {
		return getComposicaoPagamentoRedecoDAO().findMaiorDataCredito(idRedeco);
		
	}
	
	/**
	 * Obtem a soma total do pagamento para um tipo de composi��o
	 * Frete Classificar ou n�o
	 * 
	 * @param idRedeco
	 * @param freteClassificar
	 * @return BigDecimal
	 */	
	public BigDecimal findSumCompPagamentoRedeco(Long idRedeco, Boolean freteClassificar){
		return getComposicaoPagamentoRedecoDAO().findSumCompPagamentoRedeco(idRedeco, freteClassificar);
	}
		
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * @param Inst�ncia do DAO.
     */
    public void setComposicaoPagamentoRedecoDAO(ComposicaoPagamentoRedecoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * @return Inst�ncia do DAO.
     */
    private ComposicaoPagamentoRedecoDAO getComposicaoPagamentoRedecoDAO() {
        return (ComposicaoPagamentoRedecoDAO) getDao();
    }

	public RedecoService getRedecoService() {
		return redecoService;
	}

	public void setRedecoService(RedecoService redecoService) {
		this.redecoService = redecoService;
	}

	public void setCreditoBancarioService(
			CreditoBancarioService creditoBancarioService) {
		this.creditoBancarioService = creditoBancarioService;
	}

	
}
