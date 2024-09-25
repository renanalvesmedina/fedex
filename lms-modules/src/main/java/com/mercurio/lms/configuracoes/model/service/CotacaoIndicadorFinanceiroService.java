package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.CotacaoIndicadorFinanceiro;
import com.mercurio.lms.configuracoes.model.IndicadorFinanceiro;
import com.mercurio.lms.configuracoes.model.dao.CotacaoIndicadorFinanceiroDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.cotacaoIndicadorFinanceiroService"
 */
public class CotacaoIndicadorFinanceiroService extends CrudService<CotacaoIndicadorFinanceiro, Long> {	
    
    IndicadorFinanceiroService indicadorFinanceiroService;

    /**
     * Busca valor da Cota��o do Indicador Financeiro em determinada data de vig�ncia.<BR>
     * @see com.mercurio.lms.contasreceber.model.service.CalcularJurosDiarioService 
     * @param nmIndicadorFinanceiro
     * @param idPais
     * @param dtVigencia
     * @return
     */
    public BigDecimal findVlCotacaoIndFinanceiro(String sgIndicadorFinanceiro, Long idPais, YearMonthDay dtVigencia){
    	return getCotacaoIndicadorFinanceiroDAO().findVlCotacaoIndFinanceiro(sgIndicadorFinanceiro, idPais, dtVigencia);
    }
    
    
    
	/**
	 * Recupera uma inst�ncia de <code>CotacaoIndicadorFinanceiro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public CotacaoIndicadorFinanceiro findById(java.lang.Long id) {
        return (CotacaoIndicadorFinanceiro)super.findById(id);
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
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(CotacaoIndicadorFinanceiro bean) {
        return super.store(bean);
    }  
    
    /**
     * Esta fun��o testa antes de inserir se o ano/mes da cota��o Indicador Financeiro a ser inserida
     * n�o conflita com algum registro j� salvo no mesmo ano/mes.
     * @param bean Cota��oIndicadorFinanceiro a ser salvo.
     * @return Object Cota��oIndicadorFinanceiro a ser salvo.
     */
    @Override
    protected CotacaoIndicadorFinanceiro beforeStore(CotacaoIndicadorFinanceiro bean) {
        IndicadorFinanceiro indFin = bean.getIndicadorFinanceiro();

        indFin = this.getIndicadorFinanceiroService().findById(indFin.getIdIndicadorFinanceiro());

        if( indFin.getFrequencia().getTpFrequenciaIndicadorFinanc().getValue().equalsIgnoreCase("M") ){
        
            HashMap<String, Object> map = new HashMap<String, Object>();
            HashMap<String, Object> indicadorFinanceiro = new HashMap<String, Object>();
            HashMap<String, Object> frequencia = new HashMap<String, Object>();
            frequencia.put("tpFrequenciaIndicadorFinanc", "M");
            indicadorFinanceiro.put("frequencia", frequencia);
            map.put("indicadorFinanceiro", indicadorFinanceiro);

            List<CotacaoIndicadorFinanceiro> listaCotacaoIndFin = this.find(map);

            int anoBean = bean.getDtCotacaoIndFinanceiro().getYear();
            int mesBean = bean.getDtCotacaoIndFinanceiro().getMonthOfYear();

            int ano, mes;

            for(CotacaoIndicadorFinanceiro cotacaoIndicadorFinanceiro : listaCotacaoIndFin) {
                ano = cotacaoIndicadorFinanceiro.getDtCotacaoIndFinanceiro().getYear();
                mes = cotacaoIndicadorFinanceiro.getDtCotacaoIndFinanceiro().getMonthOfYear();
                if( ano == anoBean && mes == mesBean ){
                    throw new BusinessException("LMS-27039"); 
                }
            }
        }

        return bean;
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setCotacaoIndicadorFinanceiroDAO(CotacaoIndicadorFinanceiroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private CotacaoIndicadorFinanceiroDAO getCotacaoIndicadorFinanceiroDAO() {
        return (CotacaoIndicadorFinanceiroDAO) getDao();
    }

    public IndicadorFinanceiroService getIndicadorFinanceiroService() {
        return indicadorFinanceiroService;
    }

    public void setIndicadorFinanceiroService(IndicadorFinanceiroService financeiroService) {
        this.indicadorFinanceiroService = financeiroService;
    }
    
   }