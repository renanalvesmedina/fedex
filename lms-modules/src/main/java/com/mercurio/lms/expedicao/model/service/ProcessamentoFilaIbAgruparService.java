package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;

import com.mercurio.lms.expedicao.model.ProcessamentoFilaIbAgrupar;
import com.mercurio.lms.expedicao.model.dao.ProcessamentoFilaIbAgruparDAO;

public class ProcessamentoFilaIbAgruparService extends CrudService<ProcessamentoFilaIbAgrupar, Long> {

    public ProcessamentoFilaIbAgruparService() {
    }

    public ProcessamentoFilaIbAgrupar findByNrProcessamento(Long nrProcessamento) {
        return getProcessamentoFilaIbAgruparDAO().findByNrProcessamento(nrProcessamento);
    }
    public java.io.Serializable store(ProcessamentoFilaIbAgrupar bean){
        return super.store(bean);
    }

    public void removeByNrProcessamento(Long nrProcessamento) {
        getProcessamentoFilaIbAgruparDAO().removeByNrProcessamento(nrProcessamento);
    }

    public ProcessamentoFilaIbAgruparDAO getProcessamentoFilaIbAgruparDAO() {
        return (ProcessamentoFilaIbAgruparDAO)getDao();
    }

    public void setProcessamentoFilaIbAgruparDAO(ProcessamentoFilaIbAgruparDAO processamentoFilaIbAgruparDAO) {
        setDao(processamentoFilaIbAgruparDAO);
    }

}
