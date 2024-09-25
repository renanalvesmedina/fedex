package com.mercurio.lms.services.contasareceber;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contasreceber.model.FaturaCloud;
import com.mercurio.lms.contasreceber.model.service.FaturaCloudService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.service.GerarConhecimentoEletronicoXMLService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.service.ManterParametrizacaoEnvioService;

import br.com.tntbrasil.integracao.domains.financeiro.DoctoFaturaDMN;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.financeiro.MonitoramentoMensagemDMN;

@Path("/contasareceber/geracaoFaturas")
public class GeracaoFaturasRest extends BaseRest {

    private static final Logger LOGGER = LogManager.getLogger(GeracaoFaturasRest.class);

    @InjectInJersey
    private FaturaService faturaService;

    @InjectInJersey
    private GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService;

    @InjectInJersey
    private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;

    @InjectInJersey
    private ManterParametrizacaoEnvioService manterParametrizacaoEnvioService;

    @InjectInJersey
    private FaturaCloudService faturaCloudService;

    /**
     * WS que gera os para emissão da fatura
     *
     * @return
     */
    @GET
    @Path("generateFaturasProcessar")
    public Response gerarFaturas() {
        setApplicationSession(faturaService.findSessionData());
        faturaService.gerarFaturas();
        return Response.ok().build();

    }

    @POST
    @Path("buscaMonitoramentoMensagem")
    public Response buscaMonitoramentoMensagem(FaturaDMN faturaDMN) {
        setApplicationSession(faturaService.findSessionData());
        MonitoramentoMensagemDMN mensagemDMN = faturaService.buscaMonitoramentoMensagem(faturaDMN);
        return Response.ok(mensagemDMN).build();
    }

    @POST
    @Path("getDoctosFatura")
    public Response gerarDoctosFatura(FaturaDMN faturaDMN) {
        setApplicationSession(faturaService.findSessionData());
        if (null != faturaDMN) {
            List<DoctoFaturaDMN> doctosFatura = faturaService.findDoctosFatura(faturaDMN);
            String path = faturaService.getPath(faturaDMN.getSgFilialNrFatura(), faturaDMN.getDsEmpresaCliente());
            int indexIdDoctoServico = -1;
            for (DoctoFaturaDMN doctoFaturaDMN : doctosFatura) {
                if (null != doctoFaturaDMN.getDocumentoServico() && doctoFaturaDMN.getDocumentoServico().length() > 0) {
                    MonitoramentoDocEletronico docEletronico = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(doctoFaturaDMN.getIdDoctoServico());
                    doctoFaturaDMN.setPath(path);
                    doctoFaturaDMN.setChave(docEletronico != null ? docEletronico.getNrChave() : null);
                }
                if(doctoFaturaDMN.getIdDoctoServico() != null){
                    ++indexIdDoctoServico;
                }
                doctoFaturaDMN.getIdDoctoServico();
            }
            if(doctosFatura != null && !doctosFatura.isEmpty() && indexIdDoctoServico > -1){
                doctosFatura.get(indexIdDoctoServico).setLastPosition(Boolean.TRUE);
            }
            faturaDMN.setDoctosFatura(doctosFatura); // Tem que ser dentro do doctoFaturaDMN e não fora
        }
        return Response.ok(faturaDMN).build();
    }

    @POST
    @Path("getXmlDocto")
    public Response getXmlDocto(DoctoFaturaDMN doctoFaturaDMN) {
        if (null != doctoFaturaDMN && null != doctoFaturaDMN.getIdDoctoServico()) {
            Map<String, String> xml = gerarConhecimentoEletronicoXMLService.findXmlCteComComplementosParaDropbox(doctoFaturaDMN.getIdDoctoServico());
            doctoFaturaDMN.setXmlSemAssinatura(xml != null ? xml.get("xml") : null);
            doctoFaturaDMN.setXmlComAssinatura(xml != null ? xml.get("xmlOriginal") : null);
            LOGGER.info("XML" + (xml != null ? " " : " não ") + "recuperado - idFatura: " + doctoFaturaDMN.getIdFatura() + " - idDoctoServico: " + doctoFaturaDMN.getIdDoctoServico());
        }
        return Response.ok(doctoFaturaDMN).build();

    }

    @POST
    @Path("getDadosRps")
    public Response getDadosRps(DoctoFaturaDMN doctoFaturaDMN) {
        if (null != doctoFaturaDMN && null != doctoFaturaDMN.getIdDoctoServico()) {
            gerarConhecimentoEletronicoXMLService.findRpsComComplementosParaDropbox(doctoFaturaDMN);
            LOGGER.info("XML RPS recuperado - idFatura: " + doctoFaturaDMN.getIdFatura() + " - idDoctoServico: " + doctoFaturaDMN.getIdDoctoServico());
        }
        return Response.ok(doctoFaturaDMN).build();
    }

    @POST
    @Path("getDadosEnvio")
    public Response getDadosEnvio(MonitoramentoMensagemDMN mensagemDMN) {
        List<FaturaDMN> faturas = faturaService.findDadosEnvioFatura(mensagemDMN);
        return Response.ok(faturas).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("updateFatura")
    public Response updateFatura(FaturaDMN faturaDMN) {
        String idFatura = String.valueOf(faturaDMN.getIdFatura());
        TypedFlatMap parametros = new TypedFlatMap();
        parametros.put("importacaoPreFaturas", idFatura);

        try {
            faturaService.updateFatura(parametros);
            LOGGER.info("Fatura emitida. id: " + idFatura);

        } catch (Exception exception) {
            if (!(exception instanceof BusinessException)) {
                LOGGER.error(exception);
            }
        }
        return Response.ok().build();

    }

    /**
     * Necessário para compatibilidade com as queries usadas na geração de relatórios
     *
     * @param sessionData
     */
    private void setApplicationSession(Map<String, Object> sessionData) {
        SessionContext.setUser((Usuario) MapUtils.getObject(sessionData, "usuario"));
        SessionContext.set("PAIS_KEY", MapUtils.getObject(sessionData, "pais"));
        SessionContext.set("MOEDA_KEY", MapUtils.getObject(sessionData, "moeda"));
        SessionContext.set("FILIAL_KEY", MapUtils.getObject(sessionData, "filial"));
    }

}
