package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.mdfe.model.v300.EnderEmit;
import com.mercurio.lms.mdfe.model.v300.types.TUf;
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
        
        enderEmit.setXLgr(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(enderecoEmitente.getTipoLogradouro().getDsTipoLogradouro().getValue() + " " + enderecoEmitente.getDsEndereco(), 60)));
        enderEmit.setNro(MdfeConverterUtils.tratarString(enderecoEmitente.getNrEndereco(), 60));
        enderEmit.setXCpl(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(enderecoEmitente.getDsComplemento(), 60)));
        String bairro = bairroPadrao;
        if (StringUtils.isNotBlank(enderecoEmitente.getDsBairro()) && enderecoEmitente.getDsBairro().length() > 1) {
        	bairro = FormatUtils.removeAccents(enderecoEmitente.getDsBairro());
        }
        enderEmit.setXBairro(MdfeConverterUtils.tratarString(bairro, 60));
        
        enderEmit.setCMun(MdfeConverterUtils.formatCMun(enderecoEmitente.getMunicipio()));
        enderEmit.setXMun(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(enderecoEmitente.getMunicipio().getNmMunicipio(), 60)));

        enderEmit.setCEP(FormatUtils.fillNumberWithZero(enderecoEmitente.getNrCep(), 8));
        enderEmit.setUF(TUf.fromValue(enderecoEmitente.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
        
        String telefone = MdfeConverterUtils.formatTelefone(enderecoEmitente);
        enderEmit.setFone(MdfeConverterUtils.tratarString(telefone, 12));
        
        enderEmit.setEmail(emitente.getDsEmail());
        
        return enderEmit;
    }

}
