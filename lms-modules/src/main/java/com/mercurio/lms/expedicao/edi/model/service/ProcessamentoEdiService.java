package com.mercurio.lms.expedicao.edi.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.edi.dto.FiltroProcessamentoNotasEdiDTO;
import com.mercurio.lms.expedicao.dto.StoreLogEdiDto;
import com.mercurio.lms.expedicao.model.ProcessamentoEdi;

import com.mercurio.lms.expedicao.model.dao.ProcessamentoEdiDAO;
import com.mercurio.lms.expedicao.model.service.ContingenciaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.vendas.dto.ClienteDTO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessamentoEdiService extends CrudService<ProcessamentoEdi, Long> {

    private ClienteService clienteService;
    private FilialService filialService;
    private ContingenciaService contingenciaService;
    private ProcessamentoNotaEdiService processamentoNotaEdiService;

    private Integer maxLengthMessage = 300;

    protected ProcessamentoEdiDAO getProcessamentoEdiDAO() {
        return (ProcessamentoEdiDAO) getDao();
    }

    public void setProcessamentoEdiDAO(ProcessamentoEdiDAO dao) {
        setDao(dao);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateQtNotasProcessadasProcessamentoEdi(StoreLogEdiDto storeLogEdiDto) {
        this.getProcessamentoEdiDAO().updateQtNotasProcessadasProcessamentoEdi(storeLogEdiDto.getIdProcessamentoEdi(), storeLogEdiDto.getNrNotaFiscal());

        if(!StringUtils.isEmpty(storeLogEdiDto.getMensagem()) && storeLogEdiDto.getMensagem().length() > maxLengthMessage) {
            storeLogEdiDto.setMensagem(storeLogEdiDto.getMensagem().substring(0, maxLengthMessage));
        }

        this.processamentoNotaEdiService.updateProcesamentoByNrNotafiscal(storeLogEdiDto);
    }

    public void updateQtNotasProcessadasProcessamentoEdi(Long idProcessamentoEdi, Long nrNotaFiscal){
        this.getProcessamentoEdiDAO().updateQtNotasProcessadasProcessamentoEdi(idProcessamentoEdi, nrNotaFiscal);
    }


    public void  findProcessamentoNotasEdiByIdProcessamentoEdi(StoreLogEdiDto storeLogEdiDto){
        processamentoNotaEdiService.updateProcesamentoByNrNotafiscal(storeLogEdiDto);
    }


    public ClienteDTO findDadosCliente(String nrIdentificacao, Long idFilial) throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();
        Cliente cliente = clienteService.findByNumeroIdentificacao(nrIdentificacao);
        if(cliente == null){
            throw new Exception("Cliente não localizado");
        }
        contingenciaService.validateProcessarClienteEdi(cliente.getBlLiberaEtiquetaEdi(), idFilial);
        clienteDTO.setNomePessoa(cliente.getPessoa().getNmPessoa());
        clienteDTO.setIdCliente(cliente.getIdCliente());
        clienteDTO.setIdFilial(idFilial);
        return clienteDTO;
    }

    public List<Map<String, Object>>  findByFilterProcessamentoEdi(FiltroProcessamentoNotasEdiDTO filtroProcessamentoNotasEdiDTO){
        return findProcessamentoNotasEdiByIdProcessamentoEdi(
                getProcessamentoEdiDAO().findByFilterProcessamentoEdi(
                     mountCriteriaByFiltro(filtroProcessamentoNotasEdiDTO), filtroProcessamentoNotasEdiDTO.getSituacao()
                ));
    }

    private Map<String, Object> mountCriteriaByFiltro(FiltroProcessamentoNotasEdiDTO filtroProcessamentoNotasEdiDTO){
        Map criteria = new HashMap();
        criteria.put("idFilial", filtroProcessamentoNotasEdiDTO.getIdFilial());
        criteria.put("dhProcessamento", filtroProcessamentoNotasEdiDTO.getDhProcessamento().toString("yyyy-MM-dd"));
        if(filtroProcessamentoNotasEdiDTO.getIdClienteProcessamento() != null) {
            criteria.put("idCliente", filtroProcessamentoNotasEdiDTO.getIdClienteProcessamento());
        }
        if(filtroProcessamentoNotasEdiDTO.getIdUsuario() != null) {
            criteria.put("idUsuario", filtroProcessamentoNotasEdiDTO.getIdUsuario());
        }
        if(filtroProcessamentoNotasEdiDTO.getNrNotaFiscal() != null) {
            criteria.put("nrNotaFiscal", filtroProcessamentoNotasEdiDTO.getNrNotaFiscal());
        }
        return criteria;
    }

    public List<Map<String, Object>>  findProcessamentoNotasEdiByIdProcessamentoEdi(List<Map<String, Object>> listProcessamentoEdi){
        for(Map item: listProcessamentoEdi){
            item.put("processamentoNotaEdiList", processamentoNotaEdiService.findByIdProcessamento(Long.parseLong(item.get("idProcessamentoEdi").toString())));
        }
        return listProcessamentoEdi;
    }

    public void updateTpStatus(String nrProcessamento, DomainValue tpStatus) {
        getProcessamentoEdiDAO().updateTpStatus(nrProcessamento, tpStatus);
    }

    public ProcessamentoEdi findById(Long idProcessamentoEdi) {
        return getProcessamentoEdiDAO().findById(idProcessamentoEdi);
    }

    public Serializable store(ProcessamentoEdi bean) {
        return super.store(bean);
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public FilialService getFilialService() {
        return filialService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public ContingenciaService getContingenciaService() {
        return contingenciaService;
    }

    public void setContingenciaService(ContingenciaService contingenciaService) {
        this.contingenciaService = contingenciaService;
    }

    public ProcessamentoNotaEdiService getProcessamentoNotaEdiService() {
        return processamentoNotaEdiService;
    }

    public void setProcessamentoNotaEdiService(ProcessamentoNotaEdiService processamentoNotaEdiService) {
        this.processamentoNotaEdiService = processamentoNotaEdiService;
    }
}
