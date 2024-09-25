package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParamSimulacaoHistorica;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.ParamSimulacaoHistoricaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.paramSimulacaoHistoricaService"
 */
public class ParamSimulacaoHistoricaService extends CrudService<ParamSimulacaoHistorica, Long> {

	
	private DomainValueService domainValueService;
	private final String dmTipoSimulacaoReajuste = "DT_TIPO_SIMULACAO_REAJUSTE";

	/**
	 * Recupera uma instância de <code>ParamSimulacaoHistorica</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ParamSimulacaoHistorica findById(java.lang.Long id) {
        return (ParamSimulacaoHistorica)super.findById(id);
    }
    
    public List findNcParcelaSimulacao(Long idParamSimulacaoHistorica, String tpParcela) {
    	return getParamSimulacaoHistoricaDAO().findNcParcelaSimulacao(idParamSimulacaoHistorica,tpParcela);
    }
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        ParamSimulacaoHistorica bean = findById(id);
        bean.getNcParcelaSimulacoes().clear();
        getParamSimulacaoHistoricaDAO().getAdsmHibernateTemplate().delete(bean);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	for(Iterator i = ids.iterator(); i.hasNext();)
    		removeById((Long)i.next());
    }
    protected ParamSimulacaoHistorica beforeInsert(ParamSimulacaoHistorica beanT) {
    	ParamSimulacaoHistorica bean = (ParamSimulacaoHistorica)beanT;
    	
    		
    	bean.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
    	return super.beforeInsert(beanT);
    }
    protected ParamSimulacaoHistorica beforeStore(ParamSimulacaoHistorica beanT) {
    	ParamSimulacaoHistorica bean = (ParamSimulacaoHistorica)beanT;
    	if (bean.getNcParcelaSimulacoes() == null || bean.getNcParcelaSimulacoes().size() == 0)
    		throw new BusinessException("LMS-25033");
    	return super.beforeStore(bean);
    }
    
    protected ParamSimulacaoHistorica beforeUpdate(ParamSimulacaoHistorica bean) {
    	getParamSimulacaoHistoricaDAO().removeNcParcelaSimulacaoByIdParamSimulacao(((ParamSimulacaoHistorica)bean).getIdParamSimulacaoHistorica());
    	return super.beforeUpdate(bean);
    }
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
    public ParamSimulacaoHistorica store(ParamSimulacaoHistorica bean) {
        super.store(bean);
        return bean;
    }
    
    public List findParcelaTabelaCeByTpMeioTransTpTabColEnt(Long idTipoTabelaColetaEntrega, Long idTipoMeioTransporte, String tpParcela, YearMonthDay vigenteEm, Long idFilial) {
    	return getParamSimulacaoHistoricaDAO().findParcelaTabelaCeByTpMeioTransTpTabColEnt(idTipoTabelaColetaEntrega,idTipoMeioTransporte,tpParcela,vigenteEm,idFilial);
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	ResultSetPage rsp = getParamSimulacaoHistoricaDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
    	List newList = new ArrayList();
    	for(Iterator i = rsp.getList().iterator(); i.hasNext();) {
    		ParamSimulacaoHistorica bean = (ParamSimulacaoHistorica)i.next();
    		TypedFlatMap row = new TypedFlatMap();
    		row.put("idParamSimulacaoHistorica",bean.getIdParamSimulacaoHistorica());
    		row.put("dsParamSimulacaoHistorica",bean.getDsParamSimulacaoHistorica());
    		row.put("tpSimulacao",convertBooleanToDmTipoSimulacaoReajuste(bean.getBlPercentual()));
    		TipoTabelaColetaEntrega tipoTabelaColetaEntrega = bean.getTipoTabelaColetaEntrega();
    		if (tipoTabelaColetaEntrega != null)
    			row.put("tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega",tipoTabelaColetaEntrega.getDsTipoTabelaColetaEntrega());
    		row.put("dhCriacao",bean.getDhCriacao());
    		newList.add(row);
    	}
    	rsp.setList(newList);
    	return rsp;
    }
    
    
    
    public Map findQtdeNotasCredito(YearMonthDay dtVigenciaInicial,YearMonthDay dtVigenciaFinal,Long idFilial, Long idTipoTabelaColetaEntrega) {
    	return getParamSimulacaoHistoricaDAO().findQtdeNotasCredito(dtVigenciaInicial,dtVigenciaFinal,idFilial,idTipoTabelaColetaEntrega);
    }
    public Map findQtdeIdentMeioTransp(Long idMeioTransporte,Long idFilial, Long idTipoTabelaColetaEntrega) {
    	return getParamSimulacaoHistoricaDAO().findQtdeIdentMeioTransp(idMeioTransporte,idFilial,idTipoTabelaColetaEntrega);
    }
    public Map findQtdeTpMeioTransp(Long idTipoMeioTransporte,Long idFilial, Long idTipoTabelaColetaEntrega) {
    	return getParamSimulacaoHistoricaDAO().findQtdeTpMeioTransp(idTipoMeioTransporte,idFilial,idTipoTabelaColetaEntrega);
    }
    public Map findQtdeTpTabela(Long idTabelaColetaEntrega,Long idFilial) {
    	return getParamSimulacaoHistoricaDAO().findQtdeTpTabela(idTabelaColetaEntrega,idFilial);
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	return getParamSimulacaoHistoricaDAO().getRowCount(criteria);
    }
    
    public Map findQtde(Long idFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Long idTipoMeioTransporte, Long idMeioTransporte, Long idTipoTabelaColetaEntrega) {
    	return getParamSimulacaoHistoricaDAO().findQtde(idFilial,dtVigenciaInicial,dtVigenciaFinal,idTipoMeioTransporte,idMeioTransporte,idTipoTabelaColetaEntrega);
    }

    private DomainValue convertBooleanToDmTipoSimulacaoReajuste(Boolean blPercentual) {
    	return domainValueService.findDomainValueByValue(dmTipoSimulacaoReajuste,((blPercentual.equals(Boolean.TRUE)) ? "P" : "V"));
    }


    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setParamSimulacaoHistoricaDAO(ParamSimulacaoHistoricaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ParamSimulacaoHistoricaDAO getParamSimulacaoHistoricaDAO() {
        return (ParamSimulacaoHistoricaDAO) getDao();
    }

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
   }