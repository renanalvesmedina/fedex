package com.mercurio.lms.vol.model.service;

import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volTransmitirEntregasService"
 */
public class VolTransmitirEntregasService {

    private ManifestoEntregaService manifestoEntregaService;

    public Map<String, Object> findEntregas(TypedFlatMap map) {
        return manifestoEntregaService.findEntregasToMobile(map.getLong("idControleCarga"));
    }

    public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

}
