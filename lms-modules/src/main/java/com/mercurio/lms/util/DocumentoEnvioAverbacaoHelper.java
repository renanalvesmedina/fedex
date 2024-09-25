/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercurio.lms.util;

import br.com.tntbrasil.integracao.domains.expedicao.DocumentoEnvioAverbacao;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author Daniel.Maria
 */
public class DocumentoEnvioAverbacaoHelper {
    
    public static DocumentoEnvioAverbacao createDocumentoEnvioAverbacao(Long idAverbacaoDocumento, String tpEnvio, Map<String, Object> dados) {
        DocumentoEnvioAverbacao dea = new DocumentoEnvioAverbacao();

        dea.setIdentificador(idAverbacaoDocumento.toString());
        dea.setTpEnvio(tpEnvio);

        if (dados != null) {
            dea.setEmitNome((String) dados.get("emitNome"));
            dea.setEmitCnpj((String) dados.get("emitCnpj"));
            dea.setEmitCidade((String) dados.get("emitCidade"));
            dea.setEmitUf((String) dados.get("emitUf"));

            dea.setRemetCidade((String) dados.get("remeCidade"));
            dea.setRemetCnpj((String) dados.get("remeCnpj"));
            dea.setRemetNome((String) dados.get("remeNome"));
            dea.setRemetUf((String) dados.get("remeUf"));

            dea.setDestCidade((String) dados.get("destCidade"));
            dea.setDestCnpj((String) dados.get("destCnpj"));
            dea.setDestNome((String) dados.get("destNome"));
            dea.setDestUf((String) dados.get("destUf"));

            dea.setConsigCidade((String) dados.get("consCidade"));
            dea.setConsigCnpj((String) dados.get("consCnpj"));
            dea.setConsigNome((String) dados.get("consNome"));
            dea.setConsigUf((String) dados.get("consUf"));

            dea.setTipoDocumento((String) dados.get("tipoDocumento"));
            dea.setNumDocumento((String) dados.get("nrDocumento"));

            dea.setDataEmissao(JTFormatUtils.format((DateTime) dados.get("dhEmissao"), "dd/MM/yyyy"));
            dea.setTipoEmbarque((String) dados.get("tipoEmbarque"));
            dea.setTipoMercadoria((String) dados.get("tipoMercadoria"));
            dea.setValorMercadoria((String) dados.get("valorMercadoria"));
            dea.setBlOperacaoSpitFire((String) dados.get("blOperacaoSpitFire"));
        }

        return dea;
    }
    
}
