package com.mercurio.lms.sim.model.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.lms.constantes.entidades.ConsVolumeNotaFiscal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class EventoVolumeGenerator {

    private EventoVolumeService service;

    private Short cdEvento;
    private String tpScan;
    private String obComplemento;
    private OcorrenciaEntrega ocorrenciaEntrega;
    private Filial filial;
    private Usuario usuario;
    private DateTime dhOcorrencia;

    private Set<VolumeNotaFiscal> volumeNotaFiscais = new HashSet<VolumeNotaFiscal>();
    private List<EventoVolume> eventoVolumes = new ArrayList<EventoVolume>();

    private Map<Short, Evento> cdEventoMap = new HashMap<Short, Evento>();

    private EventoVolumeGenerator(EventoVolumeService service) {
        this.service = service;
    }

    public EventoVolumeGenerator cdEvento(String nmParametroGeral) {
        return cdEvento(service.findParametroGeral(nmParametroGeral));
    }

    public EventoVolumeGenerator cdEvento(int cdEvento) {
        this.cdEvento = (short) cdEvento;
        return this;
    }

    public EventoVolumeGenerator tpScan(String tpScan) {
        this.tpScan = tpScan;
        return this;
    }

    public EventoVolumeGenerator obComplemento(String obComplemento) {
        this.obComplemento = obComplemento;
        return this;
    }

    public EventoVolumeGenerator ocorrenciaEntrega(OcorrenciaEntrega ocorrenciaEntrega) {
        this.ocorrenciaEntrega = ocorrenciaEntrega;
        return this;
    }

    public EventoVolumeGenerator filial(Long idFilial) {
        return idFilial != null ? filial(new Filial(idFilial)) : this;
    }

    public EventoVolumeGenerator filial(Filial filial) {
        this.filial = filial;
        return this;
    }

    public EventoVolumeGenerator usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public EventoVolumeGenerator dhOcorrencia(DateTime dhOcorrencia) {
        this.dhOcorrencia = dhOcorrencia;
        return this;
    }

    public EventoVolumeGenerator generate(Collection<VolumeNotaFiscal> volumeNotaFiscais) {
        for (VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscais) {
            generate(volumeNotaFiscal);
        }
        return this;
    }

    public EventoVolumeGenerator generate(Long idVolumeNotaFiscal) {
        return generate(service.findVolumeNotaFiscal(idVolumeNotaFiscal));
    }

    public EventoVolumeGenerator generate(VolumeNotaFiscal volumeNotaFiscal, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        if (volumeNotaFiscal != null) {
            if (volumeNotaFiscal.getTpVolume() == null) {
                volumeNotaFiscal = service.findVolumeNotaFiscal(volumeNotaFiscal.getIdVolumeNotaFiscal());
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("volumeNotaFiscal", volumeNotaFiscal);
            map.put("evento", getEvento(cdEvento));
            map.put("tpScan", getTpScan());
            map.put("obComplemento", obComplemento);
            map.put("ocorrenciaEntrega", ocorrenciaEntrega);
            map.put("filial", getFilial());
            map.put("usuario", getUsuario());
            map.put("dhOcorrencia", getDhOcorrencia());

            EventoVolume eventoVolume = service.generateEventoVolumeComBatch(
                    map, adsmNativeBatchSqlOperations);

            updateVolumeNotaFiscalComBatch(volumeNotaFiscal, adsmNativeBatchSqlOperations);
            storeEventoVolumeComBatch(eventoVolume, adsmNativeBatchSqlOperations);

        }
        return this;
    }

    private void storeEventoVolumeComBatch(EventoVolume eventoVolume, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        Map<String, Object> eventoVolumeKeyValueMap = new HashMap<String, Object>();
        eventoVolumeKeyValueMap.put("ID_EVENTO", eventoVolume.getEvento() != null ? eventoVolume.getEvento().getIdEvento() : null);
        eventoVolumeKeyValueMap.put("ID_VOLUME_NOTA_FISCAL", eventoVolume.getVolumeNotaFiscal() != null ? eventoVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal() : null);
        eventoVolumeKeyValueMap.put("TP_SCAN", eventoVolume.getTpScan() != null ? eventoVolume.getTpScan().getValue() : null);
        eventoVolumeKeyValueMap.put("ID_FILIAL", eventoVolume.getFilial() != null ? eventoVolume.getFilial().getIdFilial() : null);
        eventoVolumeKeyValueMap.put("DH_EVENTO", eventoVolume.getDhEvento());
        eventoVolumeKeyValueMap.put("DH_EVENTO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
        eventoVolumeKeyValueMap.put("DH_INCLUSAO", eventoVolume.getDhInclusao());
        eventoVolumeKeyValueMap.put("DH_INCLUSAO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
        eventoVolumeKeyValueMap.put("ID_USUARIO", eventoVolume.getUsuario() != null ? eventoVolume.getUsuario().getIdUsuario() : null);
        eventoVolumeKeyValueMap.put("BL_EVENTO_CANCELADO", eventoVolume.getBlEventoCancelado());
        eventoVolumeKeyValueMap.put("OB_COMPLEMENTO", eventoVolume.getObComplemento());
        eventoVolumeKeyValueMap.put("ID_OCORRENCIA_ENTREGA", eventoVolume.getOcorrenciaEntrega() != null ? eventoVolume.getOcorrenciaEntrega().getIdOcorrenciaEntrega() : null);

        adsmNativeBatchSqlOperations.addNativeBatchInsert("EVENTO_VOLUME", eventoVolumeKeyValueMap);

    }

    private void updateVolumeNotaFiscalComBatch(VolumeNotaFiscal volumeNotaFiscal, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        String alias =
                "volumeNotaFiscal" +
                        (volumeNotaFiscal.getLocalizacaoFilial() != null ? volumeNotaFiscal.getLocalizacaoFilial().getIdFilial() : "") +
                        (volumeNotaFiscal.getLocalizacaoMercadoria() != null ? volumeNotaFiscal.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() : "");

        if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsVolumeNotaFiscal.TN_VOLUME_NOTA_FISCAL, alias)) {

            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsVolumeNotaFiscal.TN_VOLUME_NOTA_FISCAL,
                    "ID_LOCALIZACAO_FILIAL",
                    volumeNotaFiscal.getLocalizacaoFilial() != null ? volumeNotaFiscal.getLocalizacaoFilial().getIdFilial() : null,
                    alias);

            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsVolumeNotaFiscal.TN_VOLUME_NOTA_FISCAL,
                    "ID_LOCALIZACAO_MERCADORIA",
                    volumeNotaFiscal.getLocalizacaoMercadoria() != null ? volumeNotaFiscal.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() : null,
                    alias);
        }

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsVolumeNotaFiscal.TN_VOLUME_NOTA_FISCAL,
                volumeNotaFiscal.getIdVolumeNotaFiscal(), alias);
    }

    public EventoVolumeGenerator generate(VolumeNotaFiscal volumeNotaFiscal) {
        if (volumeNotaFiscal != null) {
            if (volumeNotaFiscal.getTpVolume() == null) {
                volumeNotaFiscal = service.findVolumeNotaFiscal(volumeNotaFiscal.getIdVolumeNotaFiscal());
            }
            EventoVolume eventoVolume = service.generateEventoVolume(
                    volumeNotaFiscal,
                    getEvento(cdEvento),
                    getTpScan(),
                    obComplemento,
                    ocorrenciaEntrega,
                    getFilial(),
                    getUsuario(),
                    getDhOcorrencia());
            volumeNotaFiscais.add(volumeNotaFiscal);
            CollectionUtils.addIgnoreNull(eventoVolumes, eventoVolume);
        }
        return this;
    }

    public EventoVolumeGenerator storeAll() {
        service.storeEventoVolume(new ArrayList<VolumeNotaFiscal>(volumeNotaFiscais), eventoVolumes);
        volumeNotaFiscais.clear();
        eventoVolumes.clear();
        return this;
    }

    private Evento getEvento(Short cdEvento) {
        Evento evento = cdEventoMap.get(cdEvento);
        if (evento == null) {
            evento = service.findEvento(cdEvento);
            if (evento == null) {
                throw new BusinessException("LMS-45015", new Object[]{cdEvento});
            }
            cdEventoMap.put(cdEvento, evento);
        }
        return evento;
    }

    private String getTpScan() {
        if (StringUtils.isEmpty(tpScan)) {
            tpScan = ConstantesSim.TP_SCAN_LMS;
        }
        return tpScan;
    }

    private Filial getFilial() {
        if (filial == null) {
            filial = SessionUtils.getFilialSessao();
        }
        return filial;
    }

    private Usuario getUsuario() {
        if (usuario == null) {
            usuario = SessionUtils.getUsuarioLogado();
        }
        return usuario;
    }

    private DateTime getDhOcorrencia() {
        return dhOcorrencia != null ? dhOcorrencia : JTDateTimeUtils.getDataHoraAtual();
    }

    public static EventoVolumeGenerator newEventoVolumeGenerator(EventoVolumeService service) {
        return new EventoVolumeGenerator(service);
    }

}
