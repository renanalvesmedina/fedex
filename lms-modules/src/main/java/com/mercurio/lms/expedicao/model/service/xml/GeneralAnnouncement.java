package com.mercurio.lms.expedicao.model.service.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralAnnouncement extends AnnouncementXML {

    @Override
    public List<Map<String, String>> getAnnouncements(List<Map<String, Object>> volumeData) {
        List<Map<String, String>> retorno = new ArrayList<Map<String, String>>();
        Map<String, String> mapXML = new HashMap<String, String>();
        StringBuilder xml = createNewXML();
        for(Map<String, Object> dadosVolume:volumeData){
            String nrVolumeColeta = (String) dadosVolume.get("NRVOLUMECOLETA");
            String cdFilial = (String) dadosVolume.get("CDFILIAL");
            String nrRota = (String) dadosVolume.get("NRROTA");
            buildXMLBody(xml, nrVolumeColeta, cdFilial, nrRota);
        }
        buildXMLFooter(xml);
        mapXML.put("fileName", "Announcement.xml");
        mapXML.put("xml", xml.toString());
        retorno.add(mapXML);
        return retorno;
    }
    
    private StringBuilder createNewXML() {
        StringBuilder dadosXmlSorter;
        dadosXmlSorter = new StringBuilder();
        buildXMLHeader(dadosXmlSorter);
        return dadosXmlSorter;
    }

    private void buildXMLHeader(StringBuilder dadosXmlSorter) {
        dadosXmlSorter.append("<?xml version='1.0' encoding='UTF-8' ?>");
        dadosXmlSorter.append("<Announcement>");
        dadosXmlSorter.append("<PlanID>PM</PlanID>");
        dadosXmlSorter.append("<PlanMode>mode_Outbound</PlanMode>");
        dadosXmlSorter.append("<PlanDescription>Outbound plan number 1</PlanDescription>");
        dadosXmlSorter.append("<PlanDetails type='Announcements'>");
    }

    private void buildXMLBody(StringBuilder dadosXmlSorter, String nrVolumeColeta, String cdFilial, String nrRota) {
        dadosXmlSorter.append("<Barcode value='");
        dadosXmlSorter.append(nrVolumeColeta);
        dadosXmlSorter.append("'>");
        dadosXmlSorter.append("<Depot>");
        dadosXmlSorter.append(cdFilial);
        dadosXmlSorter.append("</Depot>");
        dadosXmlSorter.append("<Service>");
        dadosXmlSorter.append(nrRota);
        dadosXmlSorter.append("</Service>");
        dadosXmlSorter.append("</Barcode>");
    }

    private void buildXMLFooter(StringBuilder dadosXmlSorter) {
        dadosXmlSorter.append("</PlanDetails>");
        dadosXmlSorter.append("</Announcement>");
    }

}
