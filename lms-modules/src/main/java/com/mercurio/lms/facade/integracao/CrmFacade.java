package com.mercurio.lms.facade.integracao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.vendas.model.service.ClienteService;

@ServiceSecurity
public class CrmFacade {

    private ClienteService clienteService;
    private ContatoService contatoService;

    @MethodSecurity(processGroup = "integracao.crmIntegracaoFacade", processName = "findOrganization", authenticationRequired = false)
    public List<Map<String, Object>> findOrganization(Map<String, Object> params) {
        return clienteService.findOrganization(params);
    }

    @MethodSecurity(processGroup = "integracao.crmIntegracaoFacade", processName = "findContatoCrm", authenticationRequired = false)
    public List<Map<String, Object>> findContatoCrm(Map<String, Object> params) {
        return contatoService.findContatoCrm(params);
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setContatoService(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

}
