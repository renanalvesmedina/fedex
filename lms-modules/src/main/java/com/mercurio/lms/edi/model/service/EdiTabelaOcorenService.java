package com.mercurio.lms.edi.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.EdiTabelaOcoren;
import com.mercurio.lms.edi.model.dao.EdiTabelaOcorenDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EdiTabelaOcorenService extends CrudService<EdiTabelaOcoren, Long> {

    public Serializable store(EdiTabelaOcoren bean) {
        bean.setDtInsert(new Date());
        return super.store(bean);
    }

    public void remove(Long id) throws Exception {
        try {
            super.removeById(id);
        }catch (Exception ex){
            throw new Exception("Registro(s) filho(s) localizado(s).");
        }
    }

    @Override
    public EdiTabelaOcoren findById(Long id){
        EdiTabelaOcoren ediTabelaOcoren = (EdiTabelaOcoren)super.findById(id);
        return ediTabelaOcoren;
    }

    public List<EdiTabelaOcoren> findByNmTabelaOcorenWebService(String nmTabelaOcoren, String blWebservice){
        return getEdiTabelaOcorenDAO().findByNmTabelaOcorenWebService(nmTabelaOcoren, blWebservice);
    }

    public void setEdiTabelaOcorenDAO(EdiTabelaOcorenDAO dao) {
        super.setDao(dao);
    }

    private EdiTabelaOcorenDAO getEdiTabelaOcorenDAO() {
        return  (EdiTabelaOcorenDAO)  super.getDao();
    }



}
