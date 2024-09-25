package com.mercurio.lms.expedicao.edi.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.dto.StoreLogEdiDto;
import com.mercurio.lms.expedicao.model.ProcessamentoNotaEdi;
import com.mercurio.lms.expedicao.model.dao.ProcessamentoNotaEdiDAO;

import java.util.List;
import java.util.Map;

public class ProcessamentoNotaEdiService extends CrudService<ProcessamentoNotaEdi, Long> {

    protected ProcessamentoNotaEdiDAO getProcessamentoNotaEdiDAO() {
        return (ProcessamentoNotaEdiDAO) getDao();
    }

    public void setProcessamentoNotaEdiDAO(ProcessamentoNotaEdiDAO dao) {
        setDao(dao);
    }

    public List<Map<String, Object>>  findByIdProcessamento(Long idProcessamento){
        return getProcessamentoNotaEdiDAO().findByIdProcessamento(idProcessamento);
    }

    public void  updateProcesamentoByNrNotafiscal(StoreLogEdiDto storeLogEdiDto){
        getProcessamentoNotaEdiDAO().updateProcesamentoByNrNotafiscal(storeLogEdiDto.getIdProcessamentoEdi(), storeLogEdiDto.getNrNotaFiscal(), storeLogEdiDto.getMensagem());
    }

    public void updateProcesamentoByNrNotafiscal(Long idProcessamentoEdi, Long nrNotaFiscal, String mensagem){
        this.getProcessamentoNotaEdiDAO().updateProcesamentoByNrNotafiscal(idProcessamentoEdi, nrNotaFiscal, mensagem);
    }

    public List<ProcessamentoNotaEdi> findByIdProcessamentoEdiAndDsMensagemErroIsNull(Long idProcessamentoEdi) {
        return getProcessamentoNotaEdiDAO().findByIdProcessamentoEdiAndDsMensagemErroIsNull(idProcessamentoEdi);
    }

    public List<ProcessamentoNotaEdi> findByIdProcessamentoEdiAndNrIndexAndDsMensagemErroIsNull
    (Long idProcessamentoEdi, Long inicioIndex, Long finalIndex) {
        List<ProcessamentoNotaEdi> retorno = getProcessamentoNotaEdiDAO()
            .findByIdProcessamentoEdiAndNrIndexAndDsMensagemErroIsNull(idProcessamentoEdi, inicioIndex, finalIndex);
        return retorno;
    }

}
