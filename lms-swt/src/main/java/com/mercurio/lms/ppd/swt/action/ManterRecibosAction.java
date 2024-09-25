package com.mercurio.lms.ppd.swt.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.PpdLoteRecibo;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.enums.PpdTipoDataRecibo;
import com.mercurio.lms.ppd.model.service.PpdCorporativoService;
import com.mercurio.lms.ppd.model.service.PpdFormaPgtoService;
import com.mercurio.lms.ppd.model.service.PpdLoteReciboService;
import com.mercurio.lms.ppd.model.service.PpdNaturezaProdutoService;
import com.mercurio.lms.ppd.model.service.PpdReciboService;
import com.mercurio.lms.ppd.model.service.PpdStatusReciboService;
import com.mercurio.lms.util.session.SessionUtils;
 
public class ManterRecibosAction {
	private PpdReciboService reciboService;	
	private PpdCorporativoService corporativoService;
	private PpdNaturezaProdutoService naturezaService;
	private PessoaService pessoaService;
	private FilialService filialService;
	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;
	private PpdFormaPgtoService formaPgtoService;
	private PpdStatusReciboService statusReciboService;
	private PpdLoteReciboService loteReciboService;

    public Map<String, Object> findById(Long id) {    	
    	Map<String,Object> bean = reciboService.findById(id).getMapped();

    	// Adiciona dados do Conhecimento extraídos do corporativo
    	if(bean.get("nrCtrc") != null && bean.get("sgFilialOrigem") != null && bean.get("dtEmissaoCtrc") != null) {
    		String sgFilialOrigem = (String)bean.get("sgFilialOrigem");
    		Long nrCtrc = (Long)bean.get("nrCtrc");
    		YearMonthDay dtEmissaoCtrc = (YearMonthDay) bean.get("dtEmissaoCtrc");
    		
    		Map<String,Object> dadosCtrc = corporativoService.findConhecimento(
    				sgFilialOrigem, 
    				nrCtrc,
    				dtEmissaoCtrc);
    		
    		if(dadosCtrc != null)
    			bean.putAll(dadosCtrc);    			    				    	    		
    		
    		Map<String,Object> dadosRnc = corporativoService.findRncByConhecimento(
					sgFilialOrigem, 
    				nrCtrc,
    				dtEmissaoCtrc);
    		
    		if(dadosRnc != null)
    			bean.putAll(dadosRnc);
    	}    
    	
    	PpdLoteRecibo loteRecibo = loteReciboService.findReciboInLoteNaoEnviado(id);
    	if(loteRecibo != null && loteRecibo.getLote().getBlBloqueado()) {    		
    		bean.put("idUsuarioBloqueioLote", loteRecibo.getLote().getUsuario().getIdUsuario());    		
    	} else {
    		bean.put("idUsuarioBloqueioLote", null);
    	}
    	
    	return bean;
    }
    
    public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
    	if(criteria.get("sgFilialOrigem") != null) {
			criteria.put("sgFilialOrigem", ((String)criteria.get("sgFilialOrigem")).toUpperCase());
		}	
    	if(criteria.get("sgFilialRnc") != null) {
			criteria.put("sgFilialRnc", ((String)criteria.get("sgFilialRnc")).toUpperCase());
		}	
    	
    	ResultSetPage rsp = reciboService.findPaginated(new PaginatedQuery(criteria));					
		
		List<PpdRecibo> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(int i=0;i<list.size();i++) {
			PpdRecibo item = list.get(i);
			retorno.add(item.getMapped());	
		}
				
		rsp.setList(retorno);
		return rsp;		
    }
    
	public Integer getRowCount(Map criteria) {
		return reciboService.getRowCount(criteria);
	}
    
    public Map<String, Object> store(Map<String, Object> bean) {    	
    	PpdRecibo recibo;
    	boolean isNew;
    	
    	if(bean.get("idRecibo") != null) {
    		recibo = reciboService.findById((Long)bean.get("idRecibo"));
    		isNew = false;
    	} else {
    		recibo = new PpdRecibo();
    		recibo.setTpStatus(domainValueService.findDomainValueByValue("DM_PPD_STATUS_INDENIZACAO", "P"));    		
    		isNew = true;
    	}
    	
    	recibo.setFilial(filialService.findById((Long) bean.get("idFilial")));
    	recibo.setDtRecibo((YearMonthDay)bean.get("dtRecibo"));
    	recibo.setNrRecibo((Long)bean.get("nrRecibo"));
    	recibo.setTpIndenizacao(domainValueService.findDomainValueByValue("DM_PPD_TIPO_INDENIZACAO", (String)bean.get("tpIndenizacao")));
    	recibo.setVlIndenizacao((BigDecimal)bean.get("vlIndenizacao"));
    	recibo.setTpLocalidade(domainValueService.findDomainValueByValue("DM_PPD_LOCAL_SINISTRO", (String)bean.get("tpLocalidade")));
    	recibo.setSgFilialLocal1((String)bean.get("sgFilialLocal1"));
    	recibo.setSgFilialLocal2((String)bean.get("sgFilialLocal2"));
    	recibo.setNrBanco((Long)bean.get("nrBanco"));
    	recibo.setNrAgencia((Long)bean.get("nrAgencia"));
    	recibo.setNrDigitoAgencia((String)bean.get("nrDigitoAgencia"));
    	recibo.setNrContaCorrente((Long)bean.get("nrContaCorrente"));
    	recibo.setNrDigitoContaCorrente((String) bean.get("nrDigitoContaCorrente"));
    	recibo.setDtProgramadaPagto((YearMonthDay)bean.get("dtProgramadaPagto"));
    	recibo.setNrCtrc((Long) bean.get("nrCtrc"));
    	recibo.setSgFilialOrigem((String)bean.get("sgFilialOrigem"));
    	recibo.setDtEmissaoCtrc((YearMonthDay) bean.get("dtEmissaoCtrc"));
    	recibo.setNrRnc((Long) bean.get("nrRnc"));
    	recibo.setSgFilialRnc((String)bean.get("sgFilialRnc"));
    	recibo.setDtEmissaoRnc((YearMonthDay) bean.get("dtEmissaoRnc"));    	
    	recibo.setSgFilialComp1((String)bean.get("sgFilialComp1"));
    	recibo.setSgFilialComp2((String)bean.get("sgFilialComp2"));
    	recibo.setSgFilialComp3((String)bean.get("sgFilialComp3"));
    	recibo.setPcFilialComp1((Integer)bean.get("pcFilialComp1"));
    	recibo.setPcFilialComp2((Integer)bean.get("pcFilialComp2"));
    	recibo.setPcFilialComp3((Integer)bean.get("pcFilialComp3"));
    	recibo.setObRecibo((String)bean.get("obRecibo"));
    	recibo.setNrSeguro((String)bean.get("nrSeguro"));
    	recibo.setBlSegurado((Boolean)bean.get("blSegurado"));
    	
    	
    	recibo.setDtProgramadaPagto((YearMonthDay)bean.get("dtProgramadaPagto"));
    	if(bean.get("idNaturezaProduto") != null)
    		recibo.setNaturezaProduto(naturezaService.findById((Long)bean.get("idNaturezaProduto")));    		
    	if(bean.get("idPessoa") != null)    	
    		recibo.setPessoa(pessoaService.findById((Long)bean.get("idPessoa")));
    	if(bean.get("idFormaPgto") != null) 
    		recibo.setFormaPgto(formaPgtoService.findById((Long)bean.get("idFormaPgto")));
    	
    	reciboService.store(recibo);
    	
    	if(isNew) {
    		statusReciboService.storeChangeStatus(
    				statusReciboService.generateStatus("P",
	    				configuracoesFacade.getMensagem("PPD-02004",
	    	    				new Object[]{SessionUtils.getUsuarioLogado().getNmUsuario()}
	    				)),
    				recibo);    		
    	}
    	
    	Map<String,Object> retorno = new HashMap<String, Object>();
    	retorno.put("idRecibo", recibo.getIdRecibo());
    	
    	return retorno;
    }
   
    public Serializable changeStatus(Map<String,Object> criteria) {
    	Long idRecibo = (Long)criteria.get("idRecibo");
    	String tpStatus = (String)criteria.get("tpStatus");
    	String obStatus = (String)criteria.get("obStatus");    	
    	
    	//Grava Recibo
    	PpdRecibo recibo = reciboService.findById(idRecibo);    	    	    	    	    
    	return statusReciboService.storeChangeStatus(
    			statusReciboService.generateStatus(tpStatus, obStatus), 
    			recibo);
    }       
      
    public List<Map<String,Object>> findFormaPgtoCombo(Map<String,Object> criteria) {    	
    	List<Map<String,Object>> formasPgto = formaPgtoService.find(criteria);    
    	return formasPgto;
    }       
    
    public List<Map<String,Object>> findComboTipoData(Map<String,Object> criteria) {    	
    	List<Map<String,Object>> tiposData = PpdTipoDataRecibo.getList();    	
    	return tiposData;
    }
    
    public Map<String,Object> findConhecimento(Map<String,Object> criteria) {    	    	
    	String unidSiglaOrigem = ((String)criteria.get("sgFilialOrigem")).toUpperCase();
    	Long numero = (Long)criteria.get("nrCtrc");
    	YearMonthDay dtEmissao = (YearMonthDay)criteria.get("dtEmissaoCtrc");
    	Map<String,Object> dadosCtrc = corporativoService.findConhecimento(unidSiglaOrigem, numero, dtEmissao);
    	if(dadosCtrc != null) {
    		Map<String,Object> dadosRnc = corporativoService.findRncByConhecimento(unidSiglaOrigem, numero, dtEmissao);
    		if(dadosRnc != null)
    			dadosCtrc.putAll(dadosRnc);
    		return dadosCtrc;
    	} else {
    		throw new BusinessException("PPD-02002");
    	}    	    
    }
  
    public Map<String,Object> findRnc(Map<String,Object> criteria) {    	
    	String unidSigla = ((String)criteria.get("sgFilialRnc")).toUpperCase();
    	Long numero = (Long)criteria.get("nrRnc");    	
    	Map<String,Object> dadosRnc = corporativoService.findLastRnc(unidSigla, numero);
    	if(dadosRnc != null) {
    		Map<String,Object> dadosCtrc = corporativoService.findConhecimentoByRnc(
    				unidSigla, numero, (YearMonthDay)dadosRnc.get("dtEmissaoRnc"));
    		if(dadosCtrc != null)
    			dadosRnc.putAll(dadosCtrc);
    		return dadosRnc;
    	} else {
    		String tpIndenizacao = (String)criteria.get("tpIndenizacao");
			if("1".equals(tpIndenizacao) || "2".equals(tpIndenizacao) ){
    		throw new BusinessException("PPD-02001");
    	}    	    
    		return dadosRnc;
    }
    }
    
    public void removeById(Long id) {
    	reciboService.removeById(id);
    }
    
    @ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
    	reciboService.removeByIds(ids);
	}

	//Sets das Services    
	public void setReciboService(PpdReciboService reciboService) {
		this.reciboService = reciboService;
	}

	public void setCorporativoService(PpdCorporativoService corporativoService) {
		this.corporativoService = corporativoService;
	}

	public void setNaturezaService(PpdNaturezaProdutoService naturezaService) {
		this.naturezaService = naturezaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setFormaPgtoService(PpdFormaPgtoService formaPgtoService) {
		this.formaPgtoService = formaPgtoService;
	}

	public void setStatusReciboService(PpdStatusReciboService statusReciboService) {
		this.statusReciboService = statusReciboService;
	}

	public void setLoteReciboService(PpdLoteReciboService loteReciboService) {
		this.loteReciboService = loteReciboService;
	}	
}