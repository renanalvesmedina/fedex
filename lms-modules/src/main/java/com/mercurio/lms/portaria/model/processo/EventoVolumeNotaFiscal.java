package com.mercurio.lms.portaria.model.processo;

import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.sim.model.Evento;

public class EventoVolumeNotaFiscal {
    private Evento evento;
    private VolumeNotaFiscal volumeNotaFiscal;

    public EventoVolumeNotaFiscal(Evento evento, VolumeNotaFiscal volumeNotaFiscal) {
        this.evento = evento;
        this.volumeNotaFiscal = volumeNotaFiscal;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public VolumeNotaFiscal getVolumeNotaFiscal() {
        return volumeNotaFiscal;
    }

    public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
        this.volumeNotaFiscal = volumeNotaFiscal;
    }
}
