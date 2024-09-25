package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.mdfe.model.v300.Emit;
import com.mercurio.lms.util.FormatUtils;

public class EmitConverter {

    private Pessoa emitente;
    
    private String retiraZeroInicialIe;
    
    private String bairroPadrao;
    
    public EmitConverter(Pessoa emitente, String retiraZeroInicialIe,
            String bairroPadrao) {
        super();
        this.emitente = emitente;
        this.retiraZeroInicialIe = retiraZeroInicialIe;
        this.bairroPadrao = bairroPadrao;
    }

    public Emit convert() {
        Emit emit = new Emit();
        
        emit.setCNPJ(FormatUtils.fillNumberWithZero(emitente.getNrIdentificacao(), 14) );
        
        List<InscricaoEstadual> inscricaoEstaduais = emitente.getInscricaoEstaduais();
        for (InscricaoEstadual ie: inscricaoEstaduais) {
            if (Boolean.TRUE.equals(ie.getBlIndicadorPadrao())) {
                String nrInscricaoEstadual = ie.getNrInscricaoEstadual();
                if (retiraZeroInicialIe != null && retiraZeroInicialIe.indexOf(ie.getUnidadeFederativa().getSgUnidadeFederativa()) >= 0) {
                    if (StringUtils.isNotBlank(nrInscricaoEstadual) && nrInscricaoEstadual.length() == 9 && nrInscricaoEstadual.charAt(0) == '0') {
                        nrInscricaoEstadual = nrInscricaoEstadual.substring(1);
                    }
                }
                emit.setIE(nrInscricaoEstadual);
            }
        }
        emit.setXNome(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(emitente.getNmPessoa(), 60)));
        emit.setXFant(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(emitente.getNmFantasia(), 60)));
        
        //EnderEmit
        emit.setEnderEmit(new EnderEmitConverter(emitente.getEnderecoPessoa(), bairroPadrao, emitente).convert());
        
        return emit;
    }
    
}
