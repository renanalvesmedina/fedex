package com.mercurio.lms.expedicao.model.service.xml;

import java.util.List;
import java.util.Map;

public abstract class AnnouncementXML {
    public abstract List<Map<String, String>> getAnnouncements(List<Map<String, Object>> volumeData);
}
