package com.mercurio.lms.mobilescanapp.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.UnitizacaoService;
import com.mercurio.lms.carregamento.util.MeioTranspProprietarioBuilder;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.service.FilialRotaService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.TrechoRotaIdaVoltaService;
import com.mercurio.lms.mww.model.service.CarregamentoMobileService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarregamentoMobileScanViagemService  extends AbstractMobileScanService{

    private static final String LMS_26044 = "LMS-26044";
    private CarregamentoMobileService carregamentoMobileService;
    private ConferirVolumeService conferirVolumeService;
    private ControleCargaService controleCargaService;
    private FilialRotaService filialRotaService;
    private ManifestoService manifestoService;
    private UnitizacaoService unitizacaoService;
    private VolumeNotaFiscalService volumeService;
    private EquipeOperacaoService equipeOperacaoService;
    private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;
    private MeioTranspProprietarioService meioTranspProprietarioService;
    private IntegracaoJwtService integracaoJwtService;
    private MoedaService moedaService;
    private PaisService paisService;
    private HistoricoFilialService historicoFilialService;
    private static final String MANIFESTO_VIAGEM = "V";
    private static final String LIST_CONTROLE_CARGA = "listControleCarga";
    private static final String CONTROLE_CARGA = "controleCarga";
    private static final String TP_CONTROLE_CARGA = "tpControleCarga";
    private static final String MEIO_TRANPORTE = "meioTransporte";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map findMeioTransporteMobileScan(Long nrCodigoBarras) { // metodo1 Viagem e Entrega
        Long idFilialUsuarioLogado = SessionUtils.getFilialSessao().getIdFilial();

        List<String> listaStatus = new ArrayList<>();
        listaStatus.add("GE"); // Gerado
        listaStatus.add("EC"); // Em Carregamento
        listaStatus.add("PO"); // Parada Operacional
        listaStatus.add("CP"); // Carregamento Parcial
        listaStatus.add("PM"); // Pre manifesto

        MeioTransporte meioTransporte = carregamentoMobileService.findMeioTransporteByBarCode(nrCodigoBarras);
        meioTranspProprietarioService.validateBloqueioMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(meioTransporte));
        meioTranspProprietarioService.verificaSituacaoMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(meioTransporte));

        Map<String, Object> retorno = this.getMeioTransporteMapped(meioTransporte);
        try {
            List<ControleCarga> controleCargas = controleCargaService
                    .findByMeioTransporteAndStatusAndControleDeCargaAndIdFilialUsuario(meioTransporte.getIdMeioTransporte(), listaStatus, idFilialUsuarioLogado);
            retorno.put(MEIO_TRANPORTE, meioTransporte);
            retorno.put(CONTROLE_CARGA, controleCargas.get(0));


        retorno.put(TP_CONTROLE_CARGA, controleCargas.get(0).getTpControleCarga().getValue());
        return retorno;
        }catch(Exception ex){
            throw new BusinessException("LMS-45001");
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map findMeioTransporte(Long nrCodigoBarras) { // metodo1 Viagem e Entrega
            List<String> listaStatus = new ArrayList<>();
            listaStatus.add("GE"); // Gerado
            listaStatus.add("EC"); // Em Carregamento
            listaStatus.add("PO"); // Parada Operacional
            listaStatus.add("CP"); // Carregamento Parcial
            listaStatus.add("PM"); // Pre manifesto

            MeioTransporte meioTransporte = carregamentoMobileService.findMeioTransporteByBarCode(nrCodigoBarras);
            meioTranspProprietarioService.validateBloqueioMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(meioTransporte));
            meioTranspProprietarioService.verificaSituacaoMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(meioTransporte));

            Map<String, Object> retorno = this.getMeioTransporteMapped(meioTransporte);
            List<ControleCarga> controleCargas = controleCargaService.findByMeioTransporteAndStatus(meioTransporte.getIdMeioTransporte(), listaStatus);
            retorno.put(MEIO_TRANPORTE, meioTransporte);
            retorno.put(LIST_CONTROLE_CARGA, controleCargas);
            if (controleCargas.isEmpty()) {
                throw new BusinessException("LMS-45001");
            }
            retorno.put(TP_CONTROLE_CARGA, controleCargas.get(0).getTpControleCarga().getValue());
            return retorno;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map findControleCargaAberto(Map transporte) {

            ControleCarga controleCarga = (ControleCarga) transporte.get(CONTROLE_CARGA);
            transporte.remove(CONTROLE_CARGA);
            transporte.remove(MEIO_TRANPORTE);
            if (controleCarga != null) {
                transporte.put("controleCarga", this.getControleCargaMapped(controleCarga));
                CarregamentoDescarga carregamentoDescarga = carregamentoMobileService.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());
                if (carregamentoDescarga != null) {
                    EquipeOperacao equipeOperacao = equipeOperacaoService.findEquipeOperacaoByIdControleCargaAndCarregamentoDescarga(controleCarga.getIdControleCarga(), carregamentoDescarga.getIdCarregamentoDescarga());
                    if (equipeOperacao != null) {
                        getEquipeOperacaoService().storeIntegranteEquipeOperacao(equipeOperacao);
                    }
                }
            } else {
                throw new BusinessException("LMS-45218");
            }
            return transporte;

    }

    public Map storeVolume(Map param, String token) {
        criaSessao(token);
        try {
            boolean blSorter = Boolean.parseBoolean(param.get("blSorter").toString());

            /* Busca o volume a partir do código de barras vindo por parâmetro */
            String nrCodigoBarras = param.get("nrCodigoBarras").toString();

            /* Busca o controle de cargas a partir do id vindo no parâmetro */
            ControleCarga cc = this.controleCargaService.findByIdInitLazyProperties(Long.parseLong(param.get("idControleCarga").toString()), false);

            /* Pega a filial de desvio no parametro de entrada */
            Long idFilialDesvio = null;
            if (param.get("idFilialDesvio") != null)
                idFilialDesvio = Long.parseLong(param.get("idFilialDesvio").toString());

            Boolean blConfirmado = Boolean.parseBoolean(param.get("blConfirma").toString());

            if (blSorter) {
                return this.storeVolumeSorter(nrCodigoBarras, cc, idFilialDesvio, blConfirmado, blSorter);
            }
            return this.storeVolume(nrCodigoBarras, cc, idFilialDesvio, blConfirmado);
        }finally {
            destroiSessao();
        }
    }

    private Map<String, Object> storeVolumeSorter(String nrCodigoBarras, ControleCarga controleCarga,
                                                  Long idFilialDesvio, Boolean blConfirmado, Boolean blSorter) {

        VolumeNotaFiscal volume = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras);
        List<Map<String, Object>> exceptions = new ArrayList<>();

        try {
            /* Tenta executar store do Volume, caso não consiga retorna lista de Exceções */
            exceptions = carregamentoMobileService.storeVolumeBlsorter(volume, controleCarga, null, this.MANIFESTO_VIAGEM,
                    ConstantesSim.TP_SCAN_FISICO, idFilialDesvio, !blConfirmado, blSorter);
        } catch (BusinessException e) {
            // Quando ocorre o bloqueio do meio de transporte é necessário armazenar a informação do bloqueio.
            if (LMS_26044.equals(e.getMessageKey()) && e.getMessageArguments() != null) {
                Object[] args = e.getMessageArguments();
                controleCargaService.storeBloqueioViagemEventual(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()), Boolean.parseBoolean(args[2].toString()));
            }
            // Caso contrário passa quem sabe tratar o erro.
            throw e;
        }

        Map<String, Object> volumeMapped = this.getVolumeMapped(volume, controleCarga.getIdControleCarga());
        if (exceptions != null && !exceptions.isEmpty()) {
            volumeMapped.put("messages", exceptions);
        }

        return volumeMapped;
    }


    private Map<String, Object> storeVolume(String nrCodigoBarras, ControleCarga controleCarga,
                                            Long idFilialDesvio, Boolean blConfirmado) {
        /** LMS-1039 */
        final Map<String, String> alias = new HashMap<>();
        alias.put("localizacaoMercadoria", "loc");
        alias.put("localizacaoFilial", "locFil");
        alias.put("notaFiscalConhecimento", "nfc");
        alias.put("notaFiscalConhecimento.conhecimento", "con");
        alias.put("notaFiscalConhecimento.conhecimento.filialOrigem", "cfo");
        alias.put("dispositivoUnitizacao", "du");
        alias.put("dispositivoUnitizacao.tipoDispositivoUnitizacao", "tdu");
        alias.put("dispositivoUnitizacao.dispositivoUnitizacaoPai", "dup");

        final VolumeNotaFiscal volumeNotaFiscal = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras, alias);

        List<Map<String, Object>> exceptions = new ArrayList<>();

        try {
            /* Tenta executar store do Volume, caso não consiga retorna lista de Exceções */
            exceptions = carregamentoMobileService.storeVolume(volumeNotaFiscal, controleCarga, null,
                    this.MANIFESTO_VIAGEM, ConstantesSim.TP_SCAN_FISICO, idFilialDesvio, !blConfirmado);
        } catch (BusinessException e) {
            // Quando ocorre o bloqueio do meio de transporte é necessário armazenar a informação do bloqueio.
            if (LMS_26044.equals(e.getMessageKey()) && e.getMessageArguments() != null) {
                Object[] args = e.getMessageArguments();
                controleCargaService.storeBloqueioViagemEventual(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()), Boolean.parseBoolean(args[2].toString()));
            }
            // Caso contrário passa quem sabe tratar o erro.
            throw e;
        }

        Map<String, Object> volumeMapped = this.getVolumeMapped(volumeNotaFiscal, controleCarga.getIdControleCarga());
        if (exceptions != null && !exceptions.isEmpty()) {
            volumeMapped.put("messages", exceptions);
        }
        return volumeMapped;
    }

    public Map storeDispositivo(Map param, String token) {
        criaSessao(token);
        try {
            /* Busca o volume a partir do código de barras vindo por parâmetro */
            String nrCodigoBarras = param.get("nrCodigoBarras").toString();

            /* Busca o controle de cargas a partir do id vindo no parâmetro */
            ControleCarga cc = this.controleCargaService.findByIdInitLazyProperties(Long.parseLong(param.get("idControleCarga").toString()), false);

            /* Pega varíavel que indica se já foram feitas as confirmações necessárias */
            Boolean blConfirmado = Boolean.parseBoolean(param.get("blConfirma").toString());

            /* Pega a filial de desvio no parametro de entrada */
            Long idFilialDesvio = null;
            if (param.get("idFilialDesvio") != null) {
                idFilialDesvio = Long.parseLong(param.get("idFilialDesvio").toString());
            }

            return this.storeDispositivo(nrCodigoBarras, cc, idFilialDesvio, blConfirmado);
        }finally {
            destroiSessao();
        }
    }

    private Map<String, Object> storeDispositivo(String nrCodigoBarras, ControleCarga controleCarga,
                                                 Long idFilialDesvio, Boolean blConfirmado) {

        /* Busca o dispositivo de unitização pelo código de barras */
        DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcode(nrCodigoBarras);
        LocalizacaoMercadoria localizacaoMercadoria = dispositivo.getLocalizacaoMercadoria();

        /* Busca carregamento aberto para o controle de carga, retorna null se não existe */
        CarregamentoDescarga carregamento = carregamentoMobileService.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());
        List<Map<String, Object>> exceptions = new ArrayList<>();

        try {
            /* Chama método que grava o dispositivo de unitização */
            exceptions = carregamentoMobileService.storeDispositivo(controleCarga, null, dispositivo,
                    carregamento, null, this.MANIFESTO_VIAGEM, ConstantesSim.TP_SCAN_FISICO,
                    idFilialDesvio, !blConfirmado);
        } catch (BusinessException e) {
            // Quando ocorre o bloqueio do meio de transporte.
            if (LMS_26044.equals(e.getMessageKey()) && e.getMessageArguments() != null) {
                Object[] args = e.getMessageArguments();
                controleCargaService.storeBloqueioViagemEventual(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()), Boolean.parseBoolean(args[2].toString()));
            }
            throw e;
        }

        /* Retorna dispositivo Mapeado */
        Map<String, Object> dispositivoMapped = new HashMap<>();

        if (exceptions != null && !exceptions.isEmpty()) {
            dispositivoMapped.put("messages", exceptions);
        } else {
            dispositivo.setLocalizacaoMercadoria(localizacaoMercadoria);
            List<Map<String, Object>> listDispMap = new ArrayList<>();
            listDispMap.add(unitizacaoService.findMapDispositivoUnitizacao(dispositivo, controleCarga.getIdControleCarga(), "C"));
            verificaConhecimentoCompleto(controleCarga.getIdControleCarga(), (List<Map>) listDispMap.get(0).get("conhecimentos"));
            verificaDispositivoCompleto(controleCarga.getIdControleCarga(), (List<Map>) listDispMap.get(0).get("dispositivos"), dispositivo.getIdDispositivoUnitizacao());
            listDispMap.addAll((List<Map<String, Object>>) listDispMap.get(0).get("dispositivos"));
            dispositivoMapped.put("dispositivos", listDispMap);
        }

        return dispositivoMapped;
    }

    private void verificaDispositivoCompleto(Long idControleCarga,	List<Map> list, Long idPai) {
        if(list != null){
            for (Map<String, Object> disp : list) {
                verificaConhecimentoCompleto(idControleCarga, (List<Map>)disp.get("conhecimentos"));
                verificaDispositivoCompleto(idControleCarga, (List<Map>)disp.get("dispositivos"), idPai);
                Long id = Long.parseLong(disp.get("idDispositivoUnitizacao").toString());
                if(idPai.longValue() != id.longValue()){
                    disp.put("idDispositivoUnitizacaoPai", idPai);
                }
            }
        }
    }
    private void verificaConhecimentoCompleto(Long idControleCarga,	List<Map> list) {
        if(list != null){
            for (Map<String, Object> con : list) {
                final Integer rowCountVolumes = volumeService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga,
                        Long.parseLong(con.get("idDoctoServico").toString()), "C");
                con.put("completo", (rowCountVolumes == Integer.parseInt(con.get("qtVolumes").toString())));
            }
        }
    }

    public Map<String, Object> deleteVolume(Map param, String token) {
        criaSessao(token);
        try {
            Long idControleCarga = Long.parseLong(param.get("idControleCarga").toString());
            String nrCodigoBarras = param.get("nrCodigoBarras").toString();
            Boolean blConfirmado = Boolean.parseBoolean(param.get("blConfirma").toString());

            VolumeNotaFiscal volume = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras);
            ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

            List<Map<String, Object>> exceptions = carregamentoMobileService.removeVolume(volume, controleCarga, ConstantesSim.TP_SCAN_FISICO, !blConfirmado);

            Map<String, Object> conhecimento = this.getVolumeMapped(volume, idControleCarga);
            if (exceptions != null && !exceptions.isEmpty()) {
                conhecimento.put("messages", exceptions);
            }
            return conhecimento;
        }finally {
            destroiSessao();
        }
    }

    public Map<String, Object> deleteDispositivo(Map param, String token) {
        criaSessao(token);
        try {
            /* Busca o volume a partir do código de barras vindo por parâmetro */
            String nrCodigoBarras = param.get("nrCodigoBarras").toString();
            DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcode(nrCodigoBarras);

            /* Busca o controle de cargas a partir do id vindo no parâmetro */
            Long idControleCarga = this.getLongProperty("idControleCarga", param);
            ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

            /* Pega varíavel que indica se já foram feitas as confirmações necessárias */
            Boolean blConfirmado = Boolean.parseBoolean(param.get("blConfirma").toString());

            /* Busca carregamento aberto para o controle de carga, retorna null se não existe */
            CarregamentoDescarga carregamento = carregamentoMobileService.findCarregamentoByControleCarga(idControleCarga);

            List<Map<String, Object>> exceptions =
                    carregamentoMobileService.removeDispositivo(controleCarga, dispositivo, carregamento, MANIFESTO_VIAGEM, ConstantesSim.TP_SCAN_FISICO, !blConfirmado);

            /* Retorna dispositivo Mapeado */
            Map<String, Object> dispositivoMapped = new HashMap<>();

            if (exceptions != null && !exceptions.isEmpty()) {
                dispositivoMapped.put("messages", exceptions);
            }

            return dispositivoMapped;
        }finally {
            destroiSessao();
        }
    }

    public Map<String,Object> findFiliaisDesvio(Long idControleCarga, String token) {
        criaSessao(token);
        try {
            Map<String, Object> retorno = new HashMap<>();
            ControleCarga cc = this.controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

            List<Map<String, Object>> filiais = new ArrayList<>();
            List<FilialRota> filiaisRota = filialRotaService.findFiliaisRestantesByRota(cc.getRota().getIdRota(), SessionUtils.getFilialSessao().getIdFilial());
            for (FilialRota filialRota : filiaisRota) {
                List<Manifesto> manifestos = manifestoService.findManifestosByTrecho(MANIFESTO_VIAGEM, "EC", idControleCarga, SessionUtils.getFilialSessao().getIdFilial(), filialRota.getFilial().getIdFilial());

                if (manifestos != null && manifestos.size() > 0) {
                    Map<String, Object> filialMapped = new HashMap<>();
                    filialMapped.put("idFilial", filialRota.getFilial().getIdFilial());
                    filialMapped.put("sgFilial", filialRota.getFilial().getSgFilial());
                    filiais.add(filialMapped);
                }
            }

            /* Caso não exista pre manifesto aberto para poder desviar, levanta exceção */
            if (filiais.size() == 0) {
                throw new BusinessException("LMS-45044");
            }

            retorno.put("filiais", filiais);
            return retorno;
        }finally {
            destroiSessao();
        }
    }

    public Map<String,Object> getMeioTransporteMapped(MeioTransporte meioTransporte) {
        Map<String, Object> retorno = new HashMap<>();
        retorno.put("nrFrota", meioTransporte.getNrFrota());
        retorno.put("placa", meioTransporte.getNrIdentificador());
        retorno.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
        return retorno;
    }

    private Map<String,Object> getControleCargaMapped(ControleCarga cc) {

        Map<String, Object> retorno = new HashMap<>();
        if(cc != null) {
            retorno.put("conhecimentos", this.findConhecimentosControleCargaMapped(cc.getIdControleCarga(), null, cc.getTpControleCarga().getValue()));
            retorno.put("dispositivos", this.findDispositivosControleCarga(cc.getIdControleCarga(), cc.getTpControleCarga().getValue(), SessionUtils.getFilialSessao().getIdFilial()));
            retorno.put("idControleCarga", cc.getIdControleCarga());
            retorno.put("nrControleCarga", cc.getNrControleCarga());

            Map<String, Object> rota = new HashMap<>();

            /* LMSA-6370 */
            boolean isCargaCompartilhada = cc.getSolicitacaoContratacao() != null
                    && cc.getSolicitacaoContratacao().getTpCargaCompartilhada()!=null;

            rota.put("dsRota", (isCargaCompartilhada ? "FDX* " : "") + cc.getRota().getDsRota());
            rota.put("idRota", cc.getRota().getIdRota());

            if(cc.getRotaIdaVolta() != null) {
                TrechoRotaIdaVolta trechoRotaIdaVolta = trechoRotaIdaVoltaService.findTrechoByIdRotaIdaVoltaAndFilialUsuarioLogado(cc.getRotaIdaVolta().getIdRotaIdaVolta());
                rota.put("idRotaIdaVolta", cc.getRotaIdaVolta().getIdRotaIdaVolta());
                rota.put("nrRota", cc.getRotaIdaVolta().getNrRota());
                rota.put("hrSaida", trechoRotaIdaVolta != null ? trechoRotaIdaVolta.getHrSaida().toString(DateTimeFormat.forPattern("HH:mm")) : null);
            }
            retorno.put("rota", rota);

            if(cc.getFilialByIdFilialOrigem() != null) {
                Map<String, Object> filialOrigem = new HashMap<>();
                filialOrigem.put("sgFilial", cc.getFilialByIdFilialOrigem().getSgFilial());
                retorno.put("filialOrigem", filialOrigem);
            }
        } else {
            retorno.put("idControleCarga", null);
            retorno.put("nrControleCarga", null);
        }

        return retorno;
    }

    private List<Map<String, Object>> getConhecimentosControleCargaMapped(Long idControleCarga, String tpControleCarga) {
        return carregamentoMobileService.findConhecimentosControleCargaMapped(idControleCarga, tpControleCarga);
    }

    private Map<String,Object> getVolumeMapped(VolumeNotaFiscal volume, Long idControleCarga) {
        Map<String, Object> conhecimento = carregamentoMobileService.findConhecimentoMappedByVolumeAndIdControleCarga(volume,
                idControleCarga, volume.getNrConhecimento());

        if(volume.getLocalizacaoMercadoria()!=null){
            Boolean isExtraviado = conferirVolumeService.isVolumeExtraviado(volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());

            if(isExtraviado) {
                Map<String, Object> volumeMapped = new HashMap<>();
                volumeMapped.put("extraviado",isExtraviado);
                volumeMapped.put("nrSequencia", volume.getNrSequencia());
                volumeMapped.put("idVolumeNotaFiscal", volume.getIdVolumeNotaFiscal());

                List<Map<String,Object>> volumes = new ArrayList<>();
                volumes.add(volumeMapped);

                conhecimento.put("volumes", volumes);
            }
        }
        return conhecimento;
    }

    private Long getLongProperty(String mapKey, Map mapa) {
        if(mapa.get(mapKey) != null) {
            return Long.parseLong(mapa.get(mapKey).toString());
        } else {
            return null;
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

    public void setConferirVolumeService(ConferirVolumeService conferirVolumeService) {
        this.conferirVolumeService = conferirVolumeService;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    @Override
    public boolean isCarga() {
        return true;
    }

    public void setFilialRotaService(FilialRotaService filialRotaService) {
        this.filialRotaService = filialRotaService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setUnitizacaoService(UnitizacaoService unitizacaoService) {
        this.unitizacaoService = unitizacaoService;
    }

    public void setVolumeService(VolumeNotaFiscalService volumeService) {
        this.volumeService = volumeService;
    }

    public EquipeOperacaoService getEquipeOperacaoService() {
        return equipeOperacaoService;
    }

    public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
        this.equipeOperacaoService = equipeOperacaoService;
    }

    public void setTrechoRotaIdaVoltaService(TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
        this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
    }

    public TrechoRotaIdaVoltaService getTrechoRotaIdaVoltaService() {
        return trechoRotaIdaVoltaService;
    }

    public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
        this.meioTranspProprietarioService = meioTranspProprietarioService;
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

    public void setCarregamentoMobileService(CarregamentoMobileService carregamentoMobileService) {
        this.carregamentoMobileService = carregamentoMobileService;
    }

    public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
        this.historicoFilialService = historicoFilialService;
    }
}
