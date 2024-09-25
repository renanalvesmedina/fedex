package com.mercurio.lms.sim.model.service;

import br.com.tntbrasil.integracao.domains.pedidocoleta.AceiteColetaBoschDMN;
import br.com.tntbrasil.integracao.domains.sim.ColetaRealizadaDMN;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.sim.model.dao.PedidoColetaDAO;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoColetaService extends CrudService<PedidoColeta, Long> {

    public PedidoColetaDAO getPedidoColetaDAO() {
        return (PedidoColetaDAO) super.getDao();
    }

    public void setPedidoColetaDAO(PedidoColetaDAO dao) {
        super.setDao(dao);
    }

    public List<ColetaRealizadaDMN> findEventoColetaRealizadaBosch(Long idEventoDocumentoServico){
        List<Object[]> listaEventoColetaRealizada = getPedidoColetaDAO()
                                                    .findEventoColetaRealizadaBosch
                                                        (idEventoDocumentoServico, "BOSCH");

        return listaEventoColetaRealizada.stream().map(row -> new
                        ColetaRealizadaDMN
                            (
                                (Long)row[0], (String)row[1], (String)row[2],
                                (String)row[3], (String)row[4], (String)row[5],
                                (String)row[6], (Long)row[7], (String)row[8],
                                (String)row[9]
                            )
                        ).collect(Collectors.toList());

    }

    public AceiteColetaBoschDMN findEventoAceiteColetaBosch
    (Long idPedidoColeta, Long idEventoColeta, DateTime dataHoraEvento){

        AceiteColetaBoschDMN aceiteColetaBosch = null;

        PedidoColeta pedidoColeta = getPedidoColetaDAO().findEventoAceiteColetaBosch(idPedidoColeta, idEventoColeta);

        if (pedidoColeta != null){
            aceiteColetaBosch = new AceiteColetaBoschDMN();
            EventoColeta eventoColeta = (EventoColeta)pedidoColeta.getEventoColetas().get(0);
            aceiteColetaBosch.setDataHoraAgendamento(dataHoraEvento.toString("ddMMyyyy"));
            aceiteColetaBosch.setDataHoraPrevistaColeta(pedidoColeta.getDtPrevisaoColeta().toString("ddMMyyyy"));
            aceiteColetaBosch.setStatusColeta("ACEITE");
            aceiteColetaBosch.setDescricaoMotivoRejete("");
            if("CA".equals(eventoColeta.getTpEventoColeta().getValue())){
                aceiteColetaBosch.setStatusColeta("REJEITADO");
                aceiteColetaBosch.setDescricaoMotivoRejete(eventoColeta.getDsDescricao());
            }

            aceiteColetaBosch.setNumeroNotaQM(pedidoColeta.getCdColetaCliente());
            aceiteColetaBosch.setDescricaoObsMotivoRejete("");
            aceiteColetaBosch.setNrColeta(String.valueOf(pedidoColeta.getNrColeta()));
        }

        return aceiteColetaBosch;
    }
}
