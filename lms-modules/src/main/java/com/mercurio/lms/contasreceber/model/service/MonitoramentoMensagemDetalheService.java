package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemDetalhe;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemDetalheDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class MonitoramentoMensagemDetalheService extends CrudService<MonitoramentoMensagemDetalhe, Long> {

    @Override
    public Serializable store(MonitoramentoMensagemDetalhe entity) {
        return super.store(entity);
    }

    public MonitoramentoMensagemDetalhe findByIdMonitoramentoMensagem(Long idMonitoramentoMensagem) {
        Map<String, Object> criteria = new HashedMap();
        criteria.put("monitoramentoMensagem.idMonitoramentoMensagem", idMonitoramentoMensagem);
        List result = getDao().findListByCriteria(criteria);
        if (result.isEmpty()) {
            return new MonitoramentoMensagemDetalhe();
        }
        return (MonitoramentoMensagemDetalhe) result.get(0);
    }

    public MonitoramentoMensagemDetalhe findByIdMonitoramentoMensagem(MonitoramentoMensagem monitoramentoMensagem) {
        Map<String, Object> criteria = new HashedMap();
        criteria.put("monitoramentoMensagem.idMonitoramentoMensagem", monitoramentoMensagem.getIdMonitoramentoMensagem());
        List result = getDao().findListByCriteria(criteria);
        if (result.isEmpty()) {
            MonitoramentoMensagemDetalhe monitoramentoMensagemDetalhe = new MonitoramentoMensagemDetalhe();
            monitoramentoMensagemDetalhe.setMonitoramentoMensagem(monitoramentoMensagem);
            return monitoramentoMensagemDetalhe;
        }
        return (MonitoramentoMensagemDetalhe) result.get(0);
    }

    public void updateDhEnvioMensagemByIdMonitoramentoMensagem(MonitoramentoMensagem monitoramentoMensagem) {
        MonitoramentoMensagemDetalhe monitoramentoMensagemDetalhe = findByIdMonitoramentoMensagem(monitoramentoMensagem);
        if (monitoramentoMensagemDetalhe != null) {
            monitoramentoMensagemDetalhe.setDhEnvio(JTDateTimeUtils.getDataHoraAtual());
            getDao().store(monitoramentoMensagemDetalhe);
        }
    }

    public void setMonitoramentoMensagemDetalheDAO(MonitoramentoMensagemDetalheDAO monitoramentoMensagemDetalheDAO) {
        setDao(monitoramentoMensagemDetalheDAO);
    }

}
