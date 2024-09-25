package com.mercurio.lms.sim.model.service;

import br.com.tntbrasil.integracao.domains.sim.*;
import com.mercurio.adsm.framework.model.CrudService;

import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.sim.model.RetornoIntegracaoCliente;
import com.mercurio.lms.sim.model.dao.RetornoIntegracaoClienteDAO;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.vendas.model.Cliente;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetornoIntegracaoClienteService extends CrudService<RetornoIntegracaoCliente, Long> {

    private EventoDocumentoServicoService eventoDocumentoServicoService;
    private ManifestoEntregaService manifestoEntregaService;
    private ConfiguracoesFacade configuracoesFacade;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;

    public void setRetornoIntegracaoClienteDAO(RetornoIntegracaoClienteDAO dao) {
        super.setDao(dao);
    }

    private RetornoIntegracaoClienteDAO getRetornoIntegracaoClienteDAO() {
        return  (RetornoIntegracaoClienteDAO) getDao();
    }

    public EventoDocumentoServicoService getEventoDocumentoServicoService() {
        return eventoDocumentoServicoService;
    }

    public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }

    public ManifestoEntregaService getManifestoEntregaService() {
        return manifestoEntregaService;
    }

    public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public ConfiguracoesFacade getConfiguracoesFacade() {
        return configuracoesFacade;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public ConteudoParametroFilialService getConteudoParametroFilialService() {
        return conteudoParametroFilialService;
    }

    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

    public EventoDoctoServicoCremerDMN findNotasEventoDoctoServicoCremer
            (Long idEventoDocumentoServico, Long idDoctoServico, Short cdEvento) {

        EventoDoctoServicoCremerDMN eventoDoctoServicoCremerDMN = new EventoDoctoServicoCremerDMN();
        eventoDoctoServicoCremerDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);
        eventoDoctoServicoCremerDMN.setIdDoctoServico(idDoctoServico);
        eventoDoctoServicoCremerDMN.setFormaBaixa(false);

        if(cdEvento == 21) {
            boolean isFormaBaixa = manifestoEntregaDocumentoService
                                    .isFormaBaixaByIdDoctoServico(idDoctoServico);
            eventoDoctoServicoCremerDMN.setFormaBaixa(isFormaBaixa);
        }
        List<Object[]> listEventoNota = eventoDocumentoServicoService
                                            .findNotasEventoDoctoServicoCremer(idEventoDocumentoServico);

        if(!listEventoNota.isEmpty()){

            eventoDoctoServicoCremerDMN.setIdCliente((Long)listEventoNota
                    .get(ConstantesEventosDocumentoServico.INDEX_ZERO)[ConstantesEventosDocumentoServico.INDEX_FOUR]);

            for (Object[] row : listEventoNota){

                EventoNotaFiscalCremerDMN eventoNotaFiscalCremer = new EventoNotaFiscalCremerDMN();
                eventoNotaFiscalCremer.setChaveNF((String)row[ConstantesEventosDocumentoServico.INDEX_ZERO]);
                eventoNotaFiscalCremer.setCode((String)row[ConstantesEventosDocumentoServico.INDEX_ONE]);
                eventoNotaFiscalCremer.setStartDate((String) row[ConstantesEventosDocumentoServico.INDEX_TWO]);
                eventoNotaFiscalCremer.setEndDate((String) row[ConstantesEventosDocumentoServico.INDEX_THREE]);
                eventoNotaFiscalCremer.getCoordenadaDMN().setLatitude(ConstantesEventosDocumentoServico.INDEX_ZERO);
                eventoNotaFiscalCremer.getCoordenadaDMN().setLongitude(ConstantesEventosDocumentoServico.INDEX_ZERO);

                eventoDoctoServicoCremerDMN.getEventoNotaFiscalCremer().add(eventoNotaFiscalCremer);

            }
        }

        return eventoDoctoServicoCremerDMN;
    }

    public Serializable storeRetornoIntegracao(RetornoIntegracaoClienteDMN retornoIntegracaoClienteDMN) {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(retornoIntegracaoClienteDMN.getIdCliente());
        RetornoIntegracaoCliente
                retornoIntegracaoCliente =
                new RetornoIntegracaoCliente
                        (
                                retornoIntegracaoClienteDMN.getIdDoctoServico(),
                                retornoIntegracaoClienteDMN.getIdEventoDocumentoServico(),
                                cliente,
                                retornoIntegracaoClienteDMN.getProtocolo(),
                                retornoIntegracaoClienteDMN.getCodRetorno(),
                                retornoIntegracaoClienteDMN.getDsRetorno(),
                                retornoIntegracaoClienteDMN.getDcEnviado(),
                                retornoIntegracaoClienteDMN.getTpIntegracao(),
                                retornoIntegracaoClienteDMN.getDhEnvio(),
                                retornoIntegracaoClienteDMN.getIdFatura()
                        );

        return super.store(retornoIntegracaoCliente);

    }

    public EventoDoctoServicoDecathlonDMN findNotasEventoDoctoServicoDecathlon(Long idEventoDocumentoServico, Long idDoctoServico){

        EventoDoctoServicoDecathlonDMN eventoDoctoServicoDecathlonDMN = new EventoDoctoServicoDecathlonDMN();
        eventoDoctoServicoDecathlonDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);
        eventoDoctoServicoDecathlonDMN.setIdDoctoServico(idDoctoServico);

        List<Object[]> listEventoNota = eventoDocumentoServicoService
                .findNotasEventoDoctoServicoDecathlon(idEventoDocumentoServico);

        if(listEventoNota.isEmpty()) {
            listEventoNota = eventoDocumentoServicoService.findNotasEventoDecathlon(idEventoDocumentoServico);
        }

        if(!listEventoNota.isEmpty()) {

            eventoDoctoServicoDecathlonDMN.setIdCliente((Long)listEventoNota
                    .get(ConstantesEventosDocumentoServico.INDEX_ZERO)[ConstantesEventosDocumentoServico.INDEX_FOUR]);

            for (Object[] row : listEventoNota) {

                String cpfCnpjRemetene = (String)row[ConstantesEventosDocumentoServico.INDEX_FIVE];
                String cpfCnpjDestinatario = (String)row[ConstantesEventosDocumentoServico.INDEX_SIX];

                EventoNotaFiscalDecathlonDMN eventoNotaFiscalDecathlonDMN = new EventoNotaFiscalDecathlonDMN();

                eventoNotaFiscalDecathlonDMN.setNumeroNF((String)row[ConstantesEventosDocumentoServico.INDEX_ZERO]);
                eventoNotaFiscalDecathlonDMN.setCodigoIntegracao((String) row[ConstantesEventosDocumentoServico.INDEX_ONE]
                        + (String)row[ConstantesEventosDocumentoServico.INDEX_SEVEN]);

                String[] descricao = row[ConstantesEventosDocumentoServico.INDEX_TWO]
                        .toString().split(ConstantesEventosDocumentoServico.MGT_SYMBOL);

                eventoNotaFiscalDecathlonDMN.setDescricao(descricao[ConstantesEventosDocumentoServico.INDEX_ONE]
                        .split(ConstantesEventosDocumentoServico.PIPE_SYMBOL)[ConstantesEventosDocumentoServico.INDEX_ZERO].trim());

                eventoNotaFiscalDecathlonDMN.setDataOcorrencia((String)row[ConstantesEventosDocumentoServico.INDEX_THREE]);
                eventoNotaFiscalDecathlonDMN.setCPFCNPJRemetente(ConstantesEventosDocumentoServico.BLANK);

                if (cpfCnpjRemetene != null){
                    eventoNotaFiscalDecathlonDMN.setCPFCNPJRemetente(cpfCnpjRemetene);
                }

                eventoNotaFiscalDecathlonDMN.setCPFCNPJDestinatario(ConstantesEventosDocumentoServico.BLANK);
                if(cpfCnpjDestinatario != null){
                    eventoNotaFiscalDecathlonDMN.setCPFCNPJDestinatario(cpfCnpjDestinatario);
                }
                eventoDoctoServicoDecathlonDMN.getEventoNotaFiscalDecathlonDMN().add(eventoNotaFiscalDecathlonDMN);
            }
        }
        return eventoDoctoServicoDecathlonDMN;
    }

    public EventoDoctoServicoRotaCremerDMN findNotasEventoDoctoServicoRotaCremer
            (Long idEventoDocumentoServico, Long idDoctoServico,
             Long idFilialEvento, DateTime dhEvento) {

        EventoDoctoServicoRotaCremerDMN eventoDoctoServicoRotaCremerDMN = new EventoDoctoServicoRotaCremerDMN();
        eventoDoctoServicoRotaCremerDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);
        eventoDoctoServicoRotaCremerDMN.setIdDoctoServico(idDoctoServico);
        eventoDoctoServicoRotaCremerDMN.setOccurrenceDate(formatarDhEvento(dhEvento.toString()));

        List<Object[]> listEventoNota = eventoDocumentoServicoService
                .findNotasEventoDoctoServicoRotaCremer(idEventoDocumentoServico);

        if(!listEventoNota.isEmpty()){
            String chaveNota = "";
            Object[] row = listEventoNota.get(0);
            String data = (String)row[0];
            String siglaFilial = (String)row[1];
            String codigo = (String)row[2];
            String razao = (String)row[3];
            String numeroDoctoServico = (String)row[5];
            Long idCliente = (Long)row[6];
            eventoDoctoServicoRotaCremerDMN.setRouteDate((String)row[7]);

            String usuario = obterMotorista(idFilialEvento);

            String placaVeiculo = (String) conteudoParametroFilialService
                                            .findConteudoByNomeParametro
                                                (idFilialEvento,
                                  "PLACA_VEICULO_CREMER",
                                       false
                                                );

            if (placaVeiculo == null){
                placaVeiculo = "";
            }

            DocumentoRotaCremerDMN documentoRotaCremerDMN = eventoDoctoServicoRotaCremerDMN.getDocumentoRota();
            RotaCremerDMN rotaCremerDMN = documentoRotaCremerDMN.getRotas().getRota();
            MotoristaCremerDMN motoristaCremerDMN = rotaCremerDMN.getMotorista();
            motoristaCremerDMN.setUsuario(usuario);
            motoristaCremerDMN.setPlacaVeiculo(placaVeiculo);

            Map<String, String> numeroTipoRota = gerarNumeroTipoRota(idDoctoServico, siglaFilial, numeroDoctoServico);

            String numero = numeroTipoRota.get("numero");
            String tipoRota = numeroTipoRota.get("tipoRota");
            String nomeArquivo = numero.concat(".xml");

            eventoDoctoServicoRotaCremerDMN.setIdCliente(idCliente);
            documentoRotaCremerDMN.setTipoRota(tipoRota);
            documentoRotaCremerDMN.setNomeArquivo(nomeArquivo);
            rotaCremerDMN.setData(data);
            rotaCremerDMN.setNumero(numero);
            TransportadoraCremerDMN transportadoraCremerDMN = rotaCremerDMN.getTransportadora();
            transportadoraCremerDMN.setCodigo(codigo);
            transportadoraCremerDMN.setRazao(razao);
            ParadasCremerDMN paradasCremerDMN = rotaCremerDMN.getParadas();
            int numeroParada = 0;
            for (Object[] rowDocumento : listEventoNota){
                chaveNota = (String)rowDocumento[4];
                if(chaveNota == null){
                    chaveNota = "";
                }
                ParadaCremerDMN paradaCremerDMN = new ParadaCremerDMN();
                DocumentoCremerDMN documentoCremerDMN = paradaCremerDMN.getDocumento();
                paradaCremerDMN.setNumero(++numeroParada);
                documentoCremerDMN.setChaveNota(chaveNota);
                paradasCremerDMN.getParada().add(paradaCremerDMN);
            }
        }

        return eventoDoctoServicoRotaCremerDMN;
    }

    private String formatarDhEvento(final String dhEvento){

        String[] splitDhEvento = dhEvento.split("T");
        String dataEvento = splitDhEvento[0];
        String horaEvento = splitDhEvento[1].substring(0,8);
        
        return dataEvento.concat(" ").concat(horaEvento);
    }
    private String obterMotorista(Long idFilialEvento){
        String motorista = (String) conteudoParametroFilialService
                                    .findConteudoByNomeParametro
                                        (idFilialEvento,"CD_MOTORISTA_CREMER",false);
        return motorista != null ? motorista : "";
    }

    private Map<String, String> gerarNumeroTipoRota(Long idDoctoServico, String siglaFilial, String numeroDoctoServico){

        Integer quantidadeManifestoEntrega = manifestoEntregaService.findQuantidadeManifestoEntrega(idDoctoServico);

        Map<String, String> retorno = new HashMap<>();
        String numero = "".concat(siglaFilial).concat(numeroDoctoServico);
        String tipoRota = "T";
        String totalManifestoEntrega = "";
        if(quantidadeManifestoEntrega > 0){
            tipoRota = "D";
            totalManifestoEntrega = String.valueOf(quantidadeManifestoEntrega);
        }

        numero = numero.concat(tipoRota).concat(totalManifestoEntrega);

        retorno.put("numero", numero);
        retorno.put("tipoRota", tipoRota);

        return retorno;
    }

    public RouteParameterCremerDMN findNotasEventoDoctoServicoFinalizaRotaCremer
        (Long idEventoDocumentoServico, Long idDoctoServico, Long idFilialEvento, DateTime dhEvento){

        RouteParameterCremerDMN routeParameterCremerDMN = new RouteParameterCremerDMN();
        routeParameterCremerDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);
        routeParameterCremerDMN.setIdDoctoServico(idDoctoServico);

        List<Object[]> listFimnaliza = eventoDocumentoServicoService
                                        .findNotasEventoDoctoServicoFinalizaRotaCremer
                                            (idEventoDocumentoServico, idFilialEvento);
        if(!listFimnaliza.isEmpty()) {
            Object[] row = listFimnaliza.get(0);
            String siglaFilial = (String)row[1];
            String numeroDoctoServico = (String)row[2];
            routeParameterCremerDMN.setIdCliente((Long)row[3]);

            Map<String, String> numeroTipoRota = gerarNumeroTipoRota(idDoctoServico, siglaFilial, numeroDoctoServico);
            String numero = numeroTipoRota.get("numero");

            RouteCremerDMN routeCremerDMN = new RouteCremerDMN();
            routeCremerDMN.setOccurrenceDate(formatarDhEvento(dhEvento.toString()));
            routeCremerDMN.setDriver(obterMotorista(idFilialEvento));
            routeCremerDMN.setRouteDate((String)row[0]);
            routeCremerDMN.setRouteNumber(numero);
            routeParameterCremerDMN.getRoutes().add(routeCremerDMN);

        }

        return routeParameterCremerDMN;
    }

    public ManifestoEntregaDocumentoService getManifestoEntregaDocumentoService() {
        return manifestoEntregaDocumentoService;
    }

    public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }
}
