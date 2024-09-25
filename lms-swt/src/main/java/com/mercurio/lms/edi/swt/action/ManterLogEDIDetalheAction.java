package com.mercurio.lms.edi.swt.action;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LogEDIComplemento;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.LogEDIItem;
import com.mercurio.lms.edi.model.LogEDIVolume;
import com.mercurio.lms.edi.model.service.LogEDIComplementoService;
import com.mercurio.lms.edi.model.service.LogEDIDetalheService;
import com.mercurio.lms.edi.model.service.LogEDIItemService;
import com.mercurio.lms.edi.model.service.LogEDIService;
import com.mercurio.lms.edi.model.service.LogEDIVolumeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.swt.manterLogEDIDetalheAction"
 */
public class ManterLogEDIDetalheAction {

	private Logger log = LogManager.getLogger(this.getClass());
	private LogEDIService logEDIService;
	private LogEDIDetalheService logEDIDetalheService;
	private LogEDIComplementoService logEDIComplementoService;
	private LogEDIVolumeService logEDIVolumeService;
	private LogEDIItemService logEDIItemService;
	
	
	public Map<String, Object> findById(Long id) {    	
    	
		LogEDIDetalhe logEDIDetalhe = logEDIDetalheService.findById(id);
		logEDIDetalhe.setLogComplementos(null);
		logEDIDetalhe.setLogItens(null);
		logEDIDetalhe.setLogVolumes(null);
		logEDIDetalhe.setLogEDI(null);
		Map<String, Object> bean = this.converteObjetoMapa(logEDIDetalhe);
		
    	return bean;
	}	
	
	private <X> Map<String, Object> converteObjetoMapa(X objeto){
		Map<String,Object> bean = new HashMap<String, Object>();
		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale( "pt", "BR" ));
		//pega a classe 
		Class classe = objeto.getClass();
		//pega todos os atributos
		Field[] fields = classe.getDeclaredFields();
		//percorre os atributos
		if(fields != null && fields.length > 0){
			for (int i = 0; i < fields.length; i++) {
				try{
					Field field = fields[i];	
					String prop = Character.toUpperCase(field.getName().charAt(0))+ field.getName().substring(1);
					String mname = "get" + prop;
				    Class[] types = new Class[] {};
				    try{
				    	Method method = classe.getMethod(mname, types);				 
						Object valor = method.invoke(objeto, new Object[0]);
						bean.put(field.getName(), valor);						
				    }catch (NoSuchMethodException e) {
					}
					
				}catch (Exception e) {
					log.error(e);
				}
			}
		}
		return bean;
	}
	
	public ResultSetPage<Map<String, Object>> findPaginatedLogDetalhe(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindCriteria(criteria);
		ResultSetPage rsp = logEDIDetalheService.findPaginatedLogDetalhe(tfmCriteria);
		List<LogEDIDetalhe> list = rsp.getList();	
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());		
		for (LogEDIDetalhe logEDIDetalhe : list) {
			retorno.add(this.getMapped(logEDIDetalhe));
		}
		rsp.setList(retorno);
		return rsp;				
	}
	public Integer getRowCountLogDetalhe(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindCriteria(criteria);
		Integer count =  this.logEDIDetalheService.getRowCountLogDetalhe(tfmCriteria);
		return count;
	}
	public ResultSetPage<Map<String, Object>> findPaginatedLogDetalheItens(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindDetalhesCriteria(criteria);
		ResultSetPage rsp = logEDIItemService.findPaginatedByIdLogDetalhe(tfmCriteria);
		List<LogEDIItem> list = rsp.getList();	
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());		
		for (LogEDIItem logEDIItem : list) {
			logEDIItem.setLogEDIDetalhe(null);
			retorno.add( this.converteObjetoMapa(logEDIItem));
		}
		rsp.setList(retorno);
		return rsp;				
	}
	public Integer getRowCountLogDetalheItens(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindDetalhesCriteria(criteria);
		Integer count =  this.logEDIItemService.getRowCountByIdLogDetalhe(tfmCriteria);
		return count;
	}
	public ResultSetPage<Map<String, Object>> findPaginatedLogDetalheVolumes(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindDetalhesCriteria(criteria);
		ResultSetPage rsp = logEDIVolumeService.findPaginatedByIdLogDetalhe(tfmCriteria);
		List<LogEDIVolume> list = rsp.getList();	
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());		
		for (LogEDIVolume logEDIVolume : list) {
			logEDIVolume.setLogEDIDetalhe(null);
			retorno.add( this.converteObjetoMapa(logEDIVolume));
		}
		rsp.setList(retorno);
		return rsp;				
	}
	public Integer getRowCountLogDetalheVolumes(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindDetalhesCriteria(criteria);
		Integer count =  this.logEDIVolumeService.getRowCountByIdLogDetalhe(tfmCriteria);
		return count;
	}
	public ResultSetPage<Map<String, Object>> findPaginatedLogDetalheComplementos(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindDetalhesCriteria(criteria);
		ResultSetPage rsp = logEDIComplementoService.findPaginatedByIdLogDetalhe(tfmCriteria);
		List<LogEDIComplemento> list = rsp.getList();	
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());		
		for (LogEDIComplemento logEDIComplemento : list) {
			logEDIComplemento.setLogEDIDetalhe(null);
			retorno.add( this.converteObjetoMapa(logEDIComplemento));
		}
		rsp.setList(retorno);
		return rsp;				
	}
	public Integer getRowCountLogDetalheComplementos(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindDetalhesCriteria(criteria);
		Integer count =  this.logEDIComplementoService.getRowCountByIdLogDetalhe(tfmCriteria);
		return count;
	}
	private TypedFlatMap createFindDetalhesCriteria(Map criteria) {		
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("idLogEdiDetalhe", criteria.get("idLogEdiDetalhe"));
		tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
    	tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
    	tfmCriteria.put("_order", criteria.get("_order"));
    	return tfmCriteria;
	}
	private Map<String, Object> getMapped(LogEDIDetalhe logEDIDetalhe){
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("idLogDetalhe", logEDIDetalhe.getIdLogEdiDetalhe());		
		param.put("data", logEDIDetalhe.getDataEmissaoNf());
		param.put("serie", logEDIDetalhe.getSerieNf());
		param.put("peso",logEDIDetalhe.getPesoReal());
		param.put("volume",logEDIDetalhe.getQtdeVolumes());
		param.put("valor", logEDIDetalhe.getVlrTotalMerc());
		param.put("observacao", logEDIDetalhe.getObservacao());
		param.put("status", logEDIDetalhe.getStatus());
		param.put("numeroNotaFiscal", logEDIDetalhe.getNrNotaFiscal());
		param.put("detalhes", "(+)");
		
		
		return param;
	}
	
	private TypedFlatMap createFindCriteria(Map criteria) {		
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("tiposNotas", criteria.get("tiposNotas"));
		tfmCriteria.put("numeroNotaFiscal", criteria.get("numeroNotaFiscal"));
		tfmCriteria.put("idLog", criteria.get("idLog"));
		tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
    	tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
    	tfmCriteria.put("_order", criteria.get("_order"));
    	return tfmCriteria;
	}
	public LogEDIService getLogEDIService() {
		return logEDIService;
	}
	public void setLogEDIService(LogEDIService logEDIService) {
		this.logEDIService = logEDIService;
	}
	public LogEDIDetalheService getLogEDIDetalheService() {
		return logEDIDetalheService;
	}
	public void setLogEDIDetalheService(LogEDIDetalheService logEDIDetalheService) {
		this.logEDIDetalheService = logEDIDetalheService;
	}
	public LogEDIComplementoService getLogEDIComplementoService() {
		return logEDIComplementoService;
	}
	public void setLogEDIComplementoService(LogEDIComplementoService logEDIComplementoService) {
		this.logEDIComplementoService = logEDIComplementoService;
	}
	public LogEDIVolumeService getLogEDIVolumeService() {
		return logEDIVolumeService;
	}
	public void setLogEDIVolumeService(LogEDIVolumeService logEDIVolumeService) {
		this.logEDIVolumeService = logEDIVolumeService;
	}
	public LogEDIItemService getLogEDIItemService() {
		return logEDIItemService;
	}
	public void setLogEDIItemService(LogEDIItemService logEDIItemService) {
		this.logEDIItemService = logEDIItemService;
	}
	


	
	
}
