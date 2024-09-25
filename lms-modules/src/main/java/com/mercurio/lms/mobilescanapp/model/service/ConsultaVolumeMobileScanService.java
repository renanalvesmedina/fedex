package com.mercurio.lms.mobilescanapp.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.sim.model.DescricaoEvento;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaVolumeMobileScanService {

    private IntegracaoJwtService integracaoJwtService;
    private MoedaService moedaService;
    private PaisService paisService;
    private EventoVolumeService eventoVolumeService;
    private HistoricoFilialService historicoFilialService;
    private ConferirVolumeService conferirVolumeService;
    private VolumeNotaFiscalService volumeNotaFiscalService;

    public Map<String, Object> executeVolumeByBarCode(String barCode, String token){
        criaSessao(token);
        Map<String, Object> volumeMap = conferirVolumeService.findVolumeByBarCode(barCode);
        Long idFilialLocalizacao =  volumeMap.get("idFilialLocalizacao") == null ? null : (Long) volumeMap.get("idFilialLocalizacao");

        if(idFilialLocalizacao != null && !idFilialLocalizacao.equals(SessionUtils.getFilialSessao().getIdFilial())){
            volumeMap.put("isFilialLocalizacaoDiferente", true);
            volumeMap.put("sgFilialSessaoLogada", SessionUtils.getFilialSessao().getSgFilial());
        }

        try {
            executeEventoVolumeLido((Long)volumeMap.get("idVolumeNotaFiscal"));
            return volumeMap;
        } finally {
            destroiSessao();
        }
    }

    public java.io.Serializable executeEventoVolumeLido(Long idVolumeNotaFiscal){
        return conferirVolumeService.executeEventoVolumeLido(idVolumeNotaFiscal);
    }

    public Map<String, Object> executeGeraEventoVolumeEncontrado(Long idVolumeNotaFiscal, String token) {
        criaSessao(token);
        try {
            VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findById(idVolumeNotaFiscal);
            conferirVolumeService.executeGeraEventoVolumeEncontrado(idVolumeNotaFiscal);
            return conferirVolumeService.findVolumeByBarCode(volumeNotaFiscal.getNrVolumeEmbarque());
        } finally {
            destroiSessao();
        }
    }

    public Map<String, Object> executeAtualizarFilialLocalizacaoVolume(Long idVolumeNotaFiscal, String token) {
        criaSessao(token);
        try {
            VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findById(idVolumeNotaFiscal);
            volumeNotaFiscalService.executeAtualizarFilialLocalizacaoVolume(volumeNotaFiscal);
            return conferirVolumeService.findVolumeByBarCode(volumeNotaFiscal.getNrVolumeEmbarque());
        } finally {
            destroiSessao();
        }
    }

    public Map<String, Object> findPaginatedEventoVolumeByIdVolume(Long idVolumeNotaFiscal, int pageNumber, int pageSize){
        Integer numberPage = Integer.valueOf(pageNumber);
        ResultSetPage<EventoVolume> rs = eventoVolumeService.findPaginatedByIdVolume(idVolumeNotaFiscal, new FindDefinition(numberPage, pageSize, null));
        return this.getEventoVolumePaginatedMappedList(rs, pageSize);
    }

    public void findMensagemAlteraLocalizacao (String key, String token) {
        criaSessao(token);
        try {
            throw new BusinessException(key);
        } finally {
            destroiSessao();
        }
    }

    private void criaSessao(String token){
        Filial filial =  integracaoJwtService.getFilialSessao(integracaoJwtService.getIdFilialByToken(token));
        Usuario usuario = integracaoJwtService.getUsuarioSessaoByToken(token);
        Empresa empresa = integracaoJwtService.getEmpresaSessao(integracaoJwtService.getIdEmpresaByToken(token));
        SessionContext.setUser(usuario);
        SessionContext.set(SessionKey.EMPRESA_KEY, empresa);
        SessionContext.set(SessionKey.FILIAL_KEY, filial);
        SessionContext.set(SessionKey.FILIAL_DTZ, filial.getDateTimeZone());
        SessionContext.set(SessionKey.MOEDA_KEY, moedaService.findMoedaByUsuarioEmpresa(usuario, empresa));
        SessionContext.set(SessionKey.PAIS_KEY, paisService.findPaisByUsuarioEmpresa(usuario, empresa));
        SessionContext.set(SessionKey.ULT_HIST_FILIAL_KEY, historicoFilialService.findUltimoHistoricoFilial(filial.getIdFilial()));
        SessionContext.set(SessionKey.FILIAL_MATRIZ_KEY, historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial()));
    }

    private void destroiSessao(){
        SessionContext.remove(SessionKey.EMPRESA_KEY);
        SessionContext.remove(SessionKey.FILIAL_KEY);
        SessionContext.remove(SessionKey.FILIAL_DTZ);
        SessionContext.remove(SessionKey.MOEDA_KEY);
        SessionContext.remove(SessionKey.PAIS_KEY);
        SessionContext.remove("adsm.session.authenticatedUser");
        SessionContext.remove(SessionKey.ULT_HIST_FILIAL_KEY);
        SessionContext.remove(SessionKey.FILIAL_MATRIZ_KEY);
    }

    private Map<String,Object> getEventoVolumePaginatedMappedList(ResultSetPage<EventoVolume> rs, Integer pageSize) {
        Map<String,Object> retorno = createPaginatedMap(rs, pageSize);
        List<EventoVolume> itens = rs.getList();
        retorno.put("itens", getEventoVolumeListMapped(itens));
        return retorno;
    }

    private Map<String,Object> createPaginatedMap(ResultSetPage rs, Integer pageSize){
        Map<String,Object> retorno = new HashMap<>();

        BigDecimal bdRowCount = new BigDecimal(rs.getRowCount());
        BigDecimal bdPageSize = new BigDecimal(pageSize);

        Integer pageCount = bdRowCount.divide(bdPageSize, RoundingMode.DOWN).add(BigDecimal.valueOf(1)).intValue();

        retorno.put("rowCount", rs.getRowCount());
        retorno.put("pageCount", pageCount);
        retorno.put("currentPage", rs.getCurrentPage());
        retorno.put("hasNext", rs.getHasNextPage());
        retorno.put("hasPrior", rs.getHasPriorPage());
        return retorno;
    }

    private List<Map<String,Object>> getEventoVolumeListMapped(List<EventoVolume> itens) {
        List<Map<String,Object>> listMapEvtVolume = new ArrayList();
        for(EventoVolume evtVol : itens){
            listMapEvtVolume.add(getEventoVolumeMapped(evtVol));
        }
        return listMapEvtVolume;
    }

    private Map<String, Object> getEventoVolumeMapped(EventoVolume evtVol) {
        if(evtVol == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("tpScan", evtVol.getTpScan().getValue());
        map.put("idEventoVolume", evtVol.getIdEventoVolume());
        map.put("dhEvento", JTDateTimeUtils.formatDateTimeToString(evtVol.getDhEvento()));
        map.put("dhInclusao", JTDateTimeUtils.formatDateTimeToString(evtVol.getDhInclusao()));
        map.put("blEventoCancelado", evtVol.getBlEventoCancelado());
        map.put("obComplemento", evtVol.getObComplemento());
        map.put("evento", getEventoMapped(evtVol.getEvento()));
        map.put("filial", getFilialMapped(evtVol.getFilial()));
        map.put("usuario", getUsuarioMapped(evtVol.getUsuario()));
        return map;
    }

    private Map<String, Object> getUsuarioMapped(Usuario usuario) {
        if(usuario == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("login", usuario.getLogin());
        map.put("idUsuario", usuario.getIdUsuario());
        map.put("nomeUsuario", usuario.getNmUsuario());
        return map;
    }

    private Map<String, Object> getFilialMapped(Filial filial) {
        if(filial == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("idFilial", filial.getIdFilial());
        map.put("sgFilial", filial.getSgFilial());
        return map;
    }

    private Map<String, Object> getEventoMapped(Evento evento) {
        if(evento == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("idEvento", evento.getIdEvento());
        map.put("cdEvento", evento.getCdEvento());
        map.put("tpEvento", evento.getTpEvento().getValue());
        map.put("blExibeCliente", evento.getBlExibeCliente());
        map.put("blGeraParceiro", evento.getBlGeraParceiro());
        map.put("cancelaEvento", getEventoMapped(evento.getCancelaEvento()));
        map.put("tpSituacao", evento.getTpSituacao().getValue());
        map.put("descricaoEvento", getDescricaoEventoMapped(evento.getDescricaoEvento()));
        return map;
    }

    private Map<String, Object> getDescricaoEventoMapped(DescricaoEvento descricaoEvento) {
        if(descricaoEvento == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("idDescricaoEvento", descricaoEvento.getIdDescricaoEvento());
        map.put("cdDescricaoEvento", descricaoEvento.getCdDescricaoEvento());
        map.put("dsDescricaoEvento", descricaoEvento.getDsDescricaoEvento());
        map.put("tpSituacao", descricaoEvento.getTpSituacao().getValue());
        return map;
    }

    public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
        this.integracaoJwtService = integracaoJwtService;
    }

    public void setMoedaService(MoedaService moedaService) {
        this.moedaService = moedaService;
    }

    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }

    public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
        this.historicoFilialService = historicoFilialService;
    }

    public void setConferirVolumeService(ConferirVolumeService conferirVolumeService) {
        this.conferirVolumeService = conferirVolumeService;
    }

    public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
        this.volumeNotaFiscalService = volumeNotaFiscalService;
    }

    public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
        this.eventoVolumeService = eventoVolumeService;
    }
}
