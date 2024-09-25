package com.mercurio.lms.carregamento.util.mdfe.converter;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.mdfe.model.v100.EnderEmit;
import com.mercurio.lms.mdfe.model.v100.types.TUf;
import com.mercurio.lms.util.FormatUtils;

public class EnderEmitConverter {

    private EnderecoPessoa enderecoEmitente;
    
    private String bairroPadrao;
    
    private Pessoa emitente;
    
    public EnderEmitConverter(EnderecoPessoa enderecoEmitente, String bairroPadrao, Pessoa emitente) {
        super();
        this.enderecoEmitente = enderecoEmitente;
        this.bairroPadrao = bairroPadrao;
        this.emitente = emitente;
    }

    public EnderEmit convert() {
        EnderEmit enderEmit = new EnderEmit();
        
        enderEmit.setXLgr(MdfeConverterUtils.tratarString(enderecoEmitente.getTipoLogradouro().getDsTipoLogradouro().getValue() + " " + enderecoEmitente.getDsEndereco(), 255));
        enderEmit.setNro(MdfeConverterUtils.tratarString(enderecoEmitente.getNrEndereco(), 60));
        enderEmit.setXCpl(MdfeConverterUtils.tratarString(enderecoEmitente.getDsComplemento(), 60));
        String bairro = bairroPadrao;
        if (StringUtils.isNotBlank(enderecoEmitente.getDsBairro())) {
            bairro = enderecoEmitente.getDsBairro();
        }
        enderEmit.setXBairro(MdfeConverterUtils.tratarString(bairro, 60));
        
        enderEmit.setCMun(MdfeConverterUtils.formatCMun(enderecoEmitente.getMunicipio()));
        enderEmit.setXMun(MdfeConverterUtils.tratarString(enderecoEmitente.getMunicipio().getNmMunicipio(), 60));

        enderEmit.setCEP(FormatUtils.fillNumberWithZero(enderecoEmitente.getNrCep(), 8));
        enderEmit.setUF(TUf.fromValue(enderecoEmitente.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
        
        String telefone = MdfeConverterUtils.formatTelefone(enderecoEmitente);
        enderEmit.setFone(MdfeConverterUtils.tratarString(telefone, 14));
        
        enderEmit.setEmail(emitente.getDsEmail());
        
        return enderEmit;
    }

}
