package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.expedicao.model.GeracaoXmlSorter;
import com.mercurio.lms.expedicao.model.dao.GeracaoXmlSorterDAO;
import com.mercurio.lms.expedicao.model.service.xml.AnnouncementByVolume;
import com.mercurio.lms.expedicao.model.service.xml.AnnouncementXML;
import com.mercurio.lms.expedicao.model.service.xml.GeneralAnnouncement;

public class GeracaoXmlSorterService extends CrudService<GeracaoXmlSorter, Long>{
    
    private ConteudoParametroFilialService conteudoParametroFilialService;
    
    public Map<Long,List> findDadosGeracaoXmlSorterPortaria(Long idControleCarga){
    	return findDadosGeracaoXml(idControleCarga);
    }
    
    
    public Map<Long,List> findDadosGeracaoXmlSorter(){
    	return findDadosGeracaoXml(null);
    }

	private Map<Long,List> findDadosGeracaoXml(Long idControleCarga){
		List <GeracaoXmlSorter> listGXS = this.getGeracaoXmlSorterDAO().findAllGeracaoXmlSorter();
		
		Map<Long, List<Map<String, Object>>> volumesFiliais = new HashMap<Long, List<Map<String, Object>>>();
		
		for(GeracaoXmlSorter geracaoXmlSorter: listGXS){
			List<Map<String, Object>> volumes = null;
			if(idControleCarga != null){
				volumes = this.getGeracaoXmlSorterDAO().findDadosGeracaoXmlSorterPortaria(geracaoXmlSorter,idControleCarga);
			}else{
				volumes = this.getGeracaoXmlSorterDAO().findDadosGeracaoXmlSorter(geracaoXmlSorter);
			}
			
		    if (volumes != null && volumes.size() >0){
		        Long idFilialDestino = geracaoXmlSorter.getFilialDestino().getIdFilial();

		        if (volumesFiliais.containsKey(idFilialDestino)){
		            volumesFiliais.get(idFilialDestino).addAll(volumes);
		        }else{
		            volumesFiliais.put(geracaoXmlSorter.getFilialDestino().getIdFilial(), volumes);
		        }
		    }
		    
		}
		
		Map<Long,List> retorno = new HashMap<Long, List>();
		for(Entry<Long, List<Map<String, Object>>> volumesFilial:volumesFiliais.entrySet()){
		    Long idFilialDestino = volumesFilial.getKey();
		    List<Map<String, Object>> dadosVolumes = volumesFilial.getValue();

            boolean xmlPorVolume = false;
		    try{
		        String conteudo =(String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialDestino, "XML_POR_VOLU_SORTER", false);
		        xmlPorVolume = "S".equalsIgnoreCase(conteudo);
		    }catch(IllegalArgumentException e){
		        xmlPorVolume = false;
		    }
		    
			AnnouncementXML announcementXML = null;
		    if (xmlPorVolume){
		        announcementXML = new AnnouncementByVolume();
		    }else{
		        announcementXML = new GeneralAnnouncement();
		    }
			retorno.put(idFilialDestino, announcementXML.getAnnouncements(dadosVolumes));
		}
		return retorno;
	}
	
	private GeracaoXmlSorterDAO getGeracaoXmlSorterDAO() {
		return (GeracaoXmlSorterDAO) getDao();
	}

	public void setGeracaoXmlSorterDAO(GeracaoXmlSorterDAO dao) {
		setDao(dao);
	}

	public GeracaoXmlSorter findById(Long id) {
		return (GeracaoXmlSorter) super.findById(id);
	}
	
	@Override
	public Serializable store(GeracaoXmlSorter geracaoXmlSorter) {
		return super.store(geracaoXmlSorter);
	}

	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }


}
