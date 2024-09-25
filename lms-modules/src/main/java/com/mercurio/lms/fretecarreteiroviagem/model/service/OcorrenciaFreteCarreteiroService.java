package com.mercurio.lms.fretecarreteiroviagem.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.fretecarreteiroviagem.model.OcorrenciaFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.OcorrenciaFreteCarreteiroDAO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteiroviagem.ocorrenciaFreteCarreteiroService"
 */
public class OcorrenciaFreteCarreteiroService extends CrudService<OcorrenciaFreteCarreteiro, Long> {

	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private GerarRateioFreteCarreteiroService gerarRateioFreteCarreteiroService;
	
	/**
	 * Recupera uma instância de <code>OcorrenciaFreteCarreteiro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public OcorrenciaFreteCarreteiro findById(java.lang.Long id) {
        return (OcorrenciaFreteCarreteiro)super.findById(id);
    }

    public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
    	ResultSetPage rsp = getOcorrenciaFreteCarreteiroDAO().findPaginatedCustom(criteria,FindDefinition.createFindDefinition(criteria));
    	List newList = new ArrayList(); 
    	
    	Iterator i = rsp.getList().iterator();
    	while (i.hasNext()) {
    		Map oldRow = (Map)i.next();
    		TypedFlatMap newRow = this.bean2MapPaginated(oldRow);
    		newList.add(newRow);
    	}
    	rsp.setList(newList);
    	
    	return rsp;
    }
    
    private TypedFlatMap bean2MapPaginated(Map oldRow) {
    	TypedFlatMap newRow = new TypedFlatMap(); 
    	Set s = oldRow.keySet();
		Iterator i = s.iterator();
		while (i.hasNext()) {
			String key = (String)i.next();
			newRow.put(key.replace('_','.'),oldRow.get(key));
		}
		
		Object idComplementado = oldRow.get("idReciboComplementado");
		newRow.put("reciboFreteCarreteiro.nrReciboFreteCarreteiro",
				FormatUtils.formatLongWithZeros(newRow.getLong("reciboFreteCarreteiro.nrReciboFreteCarreteiro"),"0000000000") 
    			+ (idComplementado != null ? "C" : ""));
		
		Moeda moeda = (Moeda)newRow.get("moeda");
		newRow.put("dsMoeda",moeda.getSiglaSimbolo());
		newRow.remove("moeda");
		
		DomainValue dvAux = (DomainValue)newRow.get("tpRecibo");
		newRow.put("tpRecibo",dvAux.getDescription().getValue());
		
		dvAux = (DomainValue)newRow.get("tpSituacaoRecibo");
		newRow.put("tpSituacaoRecibo",dvAux.getDescription().getValue());
		
		dvAux = (DomainValue)newRow.get("tpOcorrencia");
		newRow.put("tpOcorrencia",dvAux.getDescription().getValue());
		
		return newRow;
    }
    
    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	return getOcorrenciaFreteCarreteiroDAO().getRowCountCustom(criteria);
    }
    
    public TypedFlatMap findValoresRecibo(Long idRecibo) {
    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro)getOcorrenciaFreteCarreteiroDAO().getHibernateTemplate()
    			.get(ReciboFreteCarreteiro.class,idRecibo);
    	
    	retorno.put("reciboFreteCarreteiro.dsMoeda",rfc.getMoedaPais().getMoeda().getSiglaSimbolo());
    	retorno.put("reciboFreteCarreteiro.vlBruto",rfc.getVlBruto());
    	
    	return retorno;
    }
    
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	OcorrenciaFreteCarreteiro ofc = (OcorrenciaFreteCarreteiro) getOcorrenciaFreteCarreteiroDAO().getHibernateTemplate()
				.get(OcorrenciaFreteCarreteiro.class,id);
    	
    	Long idRemover = null;
    	if (ofc.getTpOcorrencia().getValue().equals("D")) {
    		idRemover = ofc.getReciboFreteCarreteiro().getIdReciboFreteCarreteiro();
    	}

    	super.removeById(id);
    	
    	if (idRemover != null) {
	    	reciboFreteCarreteiroService.storeUpdateVlLiquido(idRemover);
    	}
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
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return TypedFlatMap com id e situação do recibo.
	 */
    public TypedFlatMap storeCustom(OcorrenciaFreteCarreteiro bean, TypedFlatMap map) {
    	ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro)getOcorrenciaFreteCarreteiroDAO().getHibernateTemplate()
        		.get(ReciboFreteCarreteiro.class,map.getLong("reciboFreteCarreteiro.idReciboFreteCarreteiro"));
        bean.setReciboFreteCarreteiro(rfc);
        
        String value = this.validateTpOcorrencia(bean);
        	
		Serializable idOcorrencia = super.store(bean);
		getOcorrenciaFreteCarreteiroDAO().getAdsmHibernateTemplate().flush();
		reciboFreteCarreteiroService.storeUpdateVlLiquido(bean.getReciboFreteCarreteiro().getIdReciboFreteCarreteiro());
		
		String tpOcorrencia = bean.getTpOcorrencia().getValue();
		// Se é uma ocorrencia de desconto
		if (tpOcorrencia.equals("D")) {
			if (rfc.getTpReciboFreteCarreteiro().getValue().equals("V")) {
	    		ControleCarga controleCarga = rfc.getControleCarga();
	    		if (controleCarga != null) {
	    			this.gerarRateioFreteCarreteiroService.execute(controleCarga.getIdControleCarga());
	    		}
	    	}
		}		
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idOcorrenciaFreteCarreteiro",idOcorrencia);
		retorno.put("tpSituacaoReciboValue",value);
		return retorno;
    }
    
    /**
     * Trata a situação do recibo associado de acordo com a ocorrência incluída.
     * @param bean
     * @return String com o 'value' do domínio da situação do recibo.
     */
    private String validateTpOcorrencia(OcorrenciaFreteCarreteiro bean) {
    	String tpOcorrencia = bean.getTpOcorrencia().getValue();
    	ReciboFreteCarreteiro rfc = bean.getReciboFreteCarreteiro();
    	String tpSituacaoRecibo = rfc.getTpSituacaoRecibo().getValue();
    	
    	if (tpOcorrencia.equals("D")) {
    		if (!tpSituacaoRecibo.equals("GE")) {
    			throw new BusinessException("LMS-24018");
    		}
    		
    		if (!BigDecimalUtils.hasValue(bean.getVlDesconto())) {
    			throw new BusinessException("LMS-24017");
    		}
    	} else if (!tpOcorrencia.equals("C")) {
    		String value = "";
    		
    		if (tpOcorrencia.equals("E")) {
        		if (!tpSituacaoRecibo.equals("BL"))
        			throw new BusinessException("LMS-24014");
        		
        		if (rfc.getDhEmissao() == null) {
        			value = "GE";
        		} else {
        			value = "EM";
        		}
        	}
        	else if (tpOcorrencia.equals("L")) {
        		boolean ccOk = true;
        		
        		if (rfc.getTpReciboFreteCarreteiro().getValue().equals("C"))
                	throw new BusinessException("LMS-24028");
        		
        		if (rfc.getControleCarga() != null) {
        			ControleCarga cc = (ControleCarga)getOcorrenciaFreteCarreteiroDAO().getHibernateTemplate()
    						.get(ControleCarga.class,rfc.getControleCarga().getIdControleCarga());
        			if (cc != null && cc.getTpStatusControleCarga().getValue().equals("FE")) {
        				ccOk = false;
        			}
        		}
        		if (!ccOk || !tpSituacaoRecibo.equals("GE"))
        			throw new BusinessException("LMS-24015");
        		
        		value = "LI";
        	}
        	else if (tpOcorrencia.equals("B")) {
        		boolean reciboOk = (tpSituacaoRecibo.equals("EM") &&
        				rfc.getDhGeracaoMovimento() == null
        			);
        		
        		if (!(tpSituacaoRecibo.equals("GE") || reciboOk))
        			throw new BusinessException("LMS-24016");
        			
        		value = "BL";
        	}
    		
    		DomainValue newDvTpSituacaoRecibo = new DomainValue();
    		newDvTpSituacaoRecibo.setValue(value);
    		rfc.setTpSituacaoRecibo(newDvTpSituacaoRecibo);
    		getOcorrenciaFreteCarreteiroDAO().store(rfc);
    	}
    	
    	return rfc.getTpSituacaoRecibo().getValue();
    }
    
    public void storeCancelarCancelarDesconto(Long idOcorrencia) {
    	OcorrenciaFreteCarreteiro ofc = (OcorrenciaFreteCarreteiro) getOcorrenciaFreteCarreteiroDAO().getHibernateTemplate()
    			.load(OcorrenciaFreteCarreteiro.class,idOcorrencia);
    	
    	ofc.setBlDescontoCancelado(Boolean.TRUE);
    	super.store(ofc);
    	getOcorrenciaFreteCarreteiroDAO().getAdsmHibernateTemplate().flush();
    	
    	ReciboFreteCarreteiro rfc = ofc.getReciboFreteCarreteiro();
    	reciboFreteCarreteiroService.storeUpdateVlLiquido(rfc.getIdReciboFreteCarreteiro());
    	
    	String tpOcorrencia = ofc.getTpOcorrencia().getValue();
		// Se é uma ocorrencia de desconto
		if (tpOcorrencia.equals("D")) {
	    	if (rfc.getTpReciboFreteCarreteiro().getValue().equals("V")) {
	    		ControleCarga controleCarga = rfc.getControleCarga();
	    		if (controleCarga != null) {
	    			this.gerarRateioFreteCarreteiroService.execute(controleCarga.getIdControleCarga());
	    		}
	    	}
		}
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setOcorrenciaFreteCarreteiroDAO(OcorrenciaFreteCarreteiroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private OcorrenciaFreteCarreteiroDAO getOcorrenciaFreteCarreteiroDAO() {
        return (OcorrenciaFreteCarreteiroDAO) getDao();
    }

	public void setReciboFreteCarreteiroService(
			ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}

	public void setGerarRateioFreteCarreteiroService(
			GerarRateioFreteCarreteiroService gerarRateioFreteCarreteiroService) {
		this.gerarRateioFreteCarreteiroService = gerarRateioFreteCarreteiroService;
	}

}