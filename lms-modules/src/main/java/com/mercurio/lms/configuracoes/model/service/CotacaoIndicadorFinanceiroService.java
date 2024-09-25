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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.cotacaoIndicadorFinanceiroService"
 */
public class CotacaoIndicadorFinanceiroService extends CrudService<CotacaoIndicadorFinanceiro, Long> {	
    
    IndicadorFinanceiroService indicadorFinanceiroService;

    /**
     * Busca valor da Cotação do Indicador Financeiro em determinada data de vigência.<BR>
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
	 * Recupera uma instância de <code>CotacaoIndicadorFinanceiro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public CotacaoIndicadorFinanceiro findById(java.lang.Long id) {
        return (CotacaoIndicadorFinanceiro)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(CotacaoIndicadorFinanceiro bean) {
        return super.store(bean);
    }  
    
    /**
     * Esta função testa antes de inserir se o ano/mes da cotação Indicador Financeiro a ser inserida
     * não conflita com algum registro já salvo no mesmo ano/mes.
     * @param bean CotaçãoIndicadorFinanceiro a ser salvo.
     * @return Object CotaçãoIndicadorFinanceiro a ser salvo.
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
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setCotacaoIndicadorFinanceiroDAO(CotacaoIndicadorFinanceiroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
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