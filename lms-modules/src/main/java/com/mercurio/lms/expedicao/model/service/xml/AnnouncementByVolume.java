package com.mercurio.lms.expedicao.model.service.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnouncementByVolume extends AnnouncementXML{

    @Override
    public List<Map<String,String>> getAnnouncements(List<Map<String, Object>> volumeData) {
        List<Map<String,String>> retorno = new ArrayList<Map<String,String>>();
        for(Map<String, Object> dadosVolume:volumeData){
            String nrVolumeColeta = (String) dadosVolume.get("NRVOLUMECOLETA");
            String cdFilial = (String) dadosVolume.get("CDFILIAL");
            String nrRota = (String) dadosVolume.get("NRROTA");
            String sgFilial = (String) dadosVolume.get("SGFILIAL");
            
            Map<String, String> mapXML = new HashMap<String, String>();
            StringBuilder xml = new StringBuilder();
            buildXMLHeader(xml, cdFilial, nrRota);
            buildXMLBody(xml, nrVolumeColeta);
            buildXMLFooter(xml);

            mapXML.put("fileName", "Announcement"+sgFilial+nrVolumeColeta+".xml");
            mapXML.put("xml", xml.toString());
            retorno.add(mapXML);
        }
        return retorno;
    }

    private void buildXMLHeader(StringBuilder dadosXmlSorter, String cdFilial, String nrRota) {
        dadosXmlSorter.append("<?xml version='1.0' encoding='UTF-8' ?>");
        dadosXmlSorter.append("<Announcement>");
        dadosXmlSorter.append("<PlanID>PM</PlanID>");
        dadosXmlSorter.append("<Depot>");
        dadosXmlSorter.append(cdFilial);
        dadosXmlSorter.append("</Depot>");
        dadosXmlSorter.append("<Service>");
        dadosXmlSorter.append(nrRota);
        dadosXmlSorter.append("</Service>");
    }

    private void buildXMLBody(StringBuilder dadosXmlSorter, String nrVolumeColeta) {
        dadosXmlSorter.append("<Barcode>");
        dadosXmlSorter.append(nrVolumeColeta);
        dadosXmlSorter.append("</Barcode>");
        
    }

    private void buildXMLFooter(StringBuilder dadosXmlSorter) {
        dadosXmlSorter.append("</Announcement>");
    }
    

}
