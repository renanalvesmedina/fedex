package com.mercurio.lms.mobilescanapp.model.service;

import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.ConferirDispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.UnitizacaoService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.CodigoBarrasService;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.mobilescanapp.model.dto.StoreAlocarDto;
import com.mercurio.lms.mobilescanapp.model.dto.StoreUnitilizacaoDto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.MacroZona;
import com.mercurio.lms.portaria.model.service.MacroZonaService;
import com.mercurio.lms.util.session.SessionKey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgrupamentoService {
    private UnitizacaoService unitizacaoService;
    private ConferirVolumeService conferirVolumeService;
    private CodigoBarrasService codigoBarrasService;
    private ConferirDispositivoUnitizacaoService conferirDispositivoUnitizacaoService;
    private UsuarioService usuarioService;
    private MacroZonaService macroZonaService;
    private IntegracaoJwtService integracaoJwtService;

    public Map<String, Object> executeFindDispositivoUnitizacaoByBarcode(String barCode, Long idFilial) {
        Filial filial = conferirVolumeService.getFilialService().findById(idFilial);
        SessionContext.set(SessionKey.EMPRESA_KEY, filial.getEmpresa());
        SessionContext.set(SessionKey.FILIAL_KEY, filial);
        Map<String, Object> retorno = null;
        try {
            retorno = unitizacaoService.executeFindDispositivoUnitizacaoByBarcode(barCode);
            retorno.put("volumes", new ArrayList<>());
        }
        finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
            SessionContext.remove(SessionKey.EMPRESA_KEY);
        }

        return retorno;
    }


    public Map<String, Object> findVolumeByBarCode(String barCode){
        return conferirVolumeService.findVolumeByBarCode(barCode);
    }

    public Map<String, Object> findEnderecoByBarcode(String barCode){
        return unitizacaoService.findEnderecoByBarcode(new BigDecimal(barCode));
    }

    public Map<String, Object> findIdCodigoBarras(){
        return codigoBarrasService.findIdCodigoBarras();
    }


    public Map<String, Object> findMapDispositivoUnitizacaoByBarcode(String barCode, Long idFilial) {
        SessionContext.set(SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(idFilial));

        Map<String, Object> retorno = null;
        try {
            retorno = unitizacaoService.findMapDispositivoUnitizacaoByBarcode(barCode);
            retorno.put("volumes", new ArrayList<>());
        }
        finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
        }

        return retorno;
    }


    public void executeGeraEventoDispositivoEncontrado(Long idDispsitivoUnitizacao, Long idFilial, Long idUsuario) {
        SessionContext.set(SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(idFilial));
        Usuario usuario = usuarioService.findById(idUsuario);
        SessionContext.setUser(usuario);
        try {
            conferirDispositivoUnitizacaoService.executeGeraEventoDispositivoEncontrado(idDispsitivoUnitizacao);
        }
        finally {
            SessionContext.remove("adsm.session.authenticatedUser");
            SessionContext.remove(SessionKey.FILIAL_KEY);
        }

    }

    public void executeEventoDispositivoLido(Long idDispsitivoUnitizacao, Long idFilial, Long idUsuario) {
        SessionContext.set(SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(idFilial));
        Usuario usuario = usuarioService.findById(idUsuario);
        SessionContext.setUser(usuario);
        try {
            conferirDispositivoUnitizacaoService.executeEventoDispositivoLido(idDispsitivoUnitizacao);
        }
        finally {
            SessionContext.remove("adsm.session.authenticatedUser");
            SessionContext.remove(SessionKey.FILIAL_KEY);
        }

    }

    public void executeGeraEventoVolumeEncontrado(Long idVolumeNotaFiscal, Long idFilial, Long idUsuario) {
        SessionContext.set(SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(idFilial));
        Usuario usuario = usuarioService.findById(idUsuario);
        SessionContext.setUser(usuario);
        try {
            conferirVolumeService.executeGeraEventoVolumeEncontrado(idVolumeNotaFiscal);
        }
        finally {
            SessionContext.remove("adsm.session.authenticatedUser");
            SessionContext.remove(SessionKey.FILIAL_KEY);
        }

    }

    public  void executeEventoVolumeLido(Long idVolumeNotaFiscal, Long idFilial, Long idUsuario) {
        SessionContext.set(SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(idFilial));
        Usuario usuario = usuarioService.findById(idUsuario);
        SessionContext.setUser(usuario);
        try {
            conferirVolumeService.executeEventoVolumeLido(idVolumeNotaFiscal);
        }
        finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
            SessionContext.remove("adsm.session.authenticatedUser");
        }

    }

    public void storeUnitizacao(StoreUnitilizacaoDto storeUnitilizacaoDto) {
        SessionContext.set
                (SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(storeUnitilizacaoDto.getIdFilial()));
        Usuario usuario = usuarioService.findById(storeUnitilizacaoDto.getIdUsuario());
        SessionContext.setUser(usuario);
        try {
            unitizacaoService
                    .storeUnitizacao
                            (
                                    storeUnitilizacaoDto.getIdDispositivoUnitizado(),
                                    storeUnitilizacaoDto.getIdsVolumes(),
                                    storeUnitilizacaoDto.getIdsDispositivosUnitizacao()
                            );
        }
        finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
            SessionContext.remove("adsm.session.authenticatedUser");
        }

    }

    public Map<String, Object> findDadosDispositivoUnitizacao(String barCode, Long idFilial) {
        SessionContext.set(SessionKey.FILIAL_KEY,  conferirVolumeService.getFilialService().findById(idFilial));
        Map<String, Object> retorno = null;
        try {
            retorno = unitizacaoService.findDadosDispositivoUnitizacao(barCode);
        }
        finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
        }

        return retorno;
    }
    public void storeDesunitizarParcial(StoreUnitilizacaoDto storeUnitilizacaoDto) {
        SessionContext.set
                (SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(storeUnitilizacaoDto.getIdFilial()));
        Usuario usuario = usuarioService.findById(storeUnitilizacaoDto.getIdUsuario());
        SessionContext.setUser(usuario);
        try {
            Long idDispositivoUnitizado = Long.parseLong(storeUnitilizacaoDto.getIdDispositivoUnitizado());
            List<VolumeNotaFiscal> idsVolumes = unitizacaoService.findVolumesByString(storeUnitilizacaoDto.getIdsVolumes());
            List<DispositivoUnitizacao> idsDispositivoUnitizacao = unitizacaoService
                    .findDispositivoByString(storeUnitilizacaoDto.getIdsDispositivosUnitizacao());
            unitizacaoService
                    .storeDesunitizarParcial
                            (idDispositivoUnitizado, idsVolumes, idsDispositivoUnitizacao);
        }
        finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
            SessionContext.remove("adsm.session.authenticatedUser");
        }

    }

    public Map<String, Object> storeDesunitizarTotal(StoreUnitilizacaoDto storeUnitilizacaoDto) {
        SessionContext.set
                (SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(storeUnitilizacaoDto.getIdFilial()));
        Usuario usuario = usuarioService.findById(storeUnitilizacaoDto.getIdUsuario());
        SessionContext.setUser(usuario);
        Map<String, Object> retorno = null;
        try {
            Long idDispositivoUnitizado = Long.parseLong(storeUnitilizacaoDto.getIdDispositivoUnitizado());
            List<VolumeNotaFiscal> idsVolumes = unitizacaoService.findVolumesByString(storeUnitilizacaoDto.getIdsVolumes());
            List<DispositivoUnitizacao> idsDispositivoUnitizacao = unitizacaoService
                    .findDispositivoByString(storeUnitilizacaoDto.getIdsDispositivosUnitizacao());
            retorno = unitizacaoService
                    .storeDesunitizarTotal(idDispositivoUnitizado, idsVolumes, idsDispositivoUnitizacao);
        }
        finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
            SessionContext.remove("adsm.session.authenticatedUser");
        }

        return retorno;
    }


    public Map<String, Object> findDispositivoUnitizacaoById(Long idDispositivoUnitizacao){

        return conferirDispositivoUnitizacaoService.findDispositivoUnitizacaoById(idDispositivoUnitizacao);
    }

    public void storeDesunitizarTotalComDivergencias(StoreUnitilizacaoDto storeUnitilizacaoDto) {
        SessionContext.set
                (SessionKey.FILIAL_KEY, conferirVolumeService.getFilialService().findById(storeUnitilizacaoDto.getIdFilial()));
        Usuario usuario = usuarioService.findById(storeUnitilizacaoDto.getIdUsuario());
        SessionContext.setUser(usuario);

        try {
            Long idDispositivoUnitizado = Long.parseLong(storeUnitilizacaoDto.getIdDispositivoUnitizado());
            List<VolumeNotaFiscal> idsVolumes = unitizacaoService.findVolumesByString(storeUnitilizacaoDto.getIdsVolumes());
            List<DispositivoUnitizacao> idsDispositivoUnitizacao = unitizacaoService
                    .findDispositivoByString(storeUnitilizacaoDto.getIdsDispositivosUnitizacao());

            unitizacaoService.storeDesunitizarTotalComDivergencia(idDispositivoUnitizado, idsVolumes, idsDispositivoUnitizacao);
        }
        finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
            SessionContext.remove("adsm.session.authenticatedUser");
        }
    }

    public void storeAlocar(StoreAlocarDto storeAlocarDto, MacroZona macroZona, String token) {
        addDadosSessao(token);

        try {
            unitizacaoService.storeAlocar(macroZona, storeAlocarDto.getIdsVolumes(), storeAlocarDto.getIdsDispositivosUnitizacao());
        } finally {
            removeDadosSessao();
        }
    }

    public void setUnitizacaoService(UnitizacaoService unitizacaoService) {
        this.unitizacaoService = unitizacaoService;
    }

    public void setConferirVolumeService(ConferirVolumeService conferirVolumeService) {
        this.conferirVolumeService = conferirVolumeService;
    }

    public void setCodigoBarrasService(CodigoBarrasService codigoBarrasService) {
        this.codigoBarrasService = codigoBarrasService;
    }

    public void setConferirDispositivoUnitizacaoService(ConferirDispositivoUnitizacaoService conferirDispositivoUnitizacaoService) {
        this.conferirDispositivoUnitizacaoService = conferirDispositivoUnitizacaoService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setMacroZonaService(MacroZonaService macroZonaService) {
        this.macroZonaService = macroZonaService;
    }

    public IntegracaoJwtService getIntegracaoJwtService() {
        return integracaoJwtService;
    }

    public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
        this.integracaoJwtService = integracaoJwtService;
    }

    public void storeDesalocar(StoreAlocarDto storeAlocarDto, String token){
        addDadosSessao(token);
        MacroZona macroZona = null;

        if(storeAlocarDto.getIdEnderecoTerminal() != null) {
            macroZona = macroZonaService.findById(Long.parseLong(storeAlocarDto.getIdEnderecoTerminal()));
        }

        try {
            unitizacaoService.storeDesalocar(macroZona, storeAlocarDto.getIdsVolumes(), storeAlocarDto.getIdsDispositivosUnitizacao());
        } finally {
            removeDadosSessao();
        }
    }

    private void addDadosSessao(String token){
        Filial filial =  integracaoJwtService.getFilialSessao(integracaoJwtService.getIdFilialByToken(token));
        Usuario usuario = integracaoJwtService.getUsuarioSessaoByToken(token);
        SessionContext.set(SessionKey.FILIAL_KEY, filial);
        SessionContext.set(SessionKey.FILIAL_DTZ, filial.getDateTimeZone());
        SessionContext.setUser(usuario);
    }

    private void removeDadosSessao(){
        SessionContext.remove(SessionKey.FILIAL_KEY);
        SessionContext.remove(SessionKey.FILIAL_DTZ);
        SessionContext.remove("adsm.session.authenticatedUser");
    }
}
