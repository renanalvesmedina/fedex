package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

class ParserResultConhecimentoEletronico {

    private static final Logger LOGGER = LogManager.getLogger(ParserResultConhecimentoEletronico.class);

    public List<TypedFlatMap> parseToMap(List<Object[]> listFromResult) {

        List<TypedFlatMap> listRetorno = new ArrayList<>(listFromResult.size());
        TypedFlatMap map;
        for (final Object[] objResult : listFromResult) {
            map = new TypedFlatMap();
            map.put("idDoctoServico", objResult[0]);
            map.put("dsDadosDocumento", objResult[1] == null ? null : readBlob((Blob)objResult[1]).toString());
            map.put("tpDocumentServico", objResult[2]);
            map.put("nrDoctoServico", objResult[3]);
            map.put("sgFilial", objResult[4]);
            map.put("nrFatura", objResult[5]);
            map.put("nrProtocolo", objResult[6]);
            listRetorno.add(map);
        }

        return listRetorno;
    }

    private StringBuilder readBlob(Blob xml ){
        StringBuilder sb = new StringBuilder();
        InputStreamReader is = null;
        BufferedReader br = null;
        try {
            is = new InputStreamReader(xml.getBinaryStream(), StandardCharsets.UTF_8);
            br = new BufferedReader(is);

            String read = br.readLine();
            while(read != null) {
                sb.append(read);
                read =br.readLine();

            }
        }catch (Exception e) {
            LOGGER.error("Problema ao ler o arquivo BLOB: ", e);
        }finally{
            try {
                if(is != null){
                    is.close();
                }
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                LOGGER.error("Problema ao tentar fechar os objetos de IO", e);
            }
        }
        return sb;
    }
}
