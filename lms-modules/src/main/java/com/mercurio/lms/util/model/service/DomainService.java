package com.mercurio.lms.util.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.dto.DomainDTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DomainService {
    private DomainValueService domainValueService;

    public List<DomainDTO> findDomainByDomainName(String domainName){
        List<DomainValue> domainValues = domainValueService.findByDomainName(domainName);
        domainValues.sort(Comparator.comparing(DomainValue::getOrder));
        return domainValues.stream()
                .map(dv -> new DomainDTO(dv.getValue(), dv.getDescriptionAsString()))
                .collect(Collectors.toList());
    }

    public DomainValueService getDomainValueService() {
        return domainValueService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }
}
