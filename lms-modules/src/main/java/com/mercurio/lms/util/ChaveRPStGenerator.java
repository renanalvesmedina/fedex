package com.mercurio.lms.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Contingencia;
import com.mercurio.lms.expedicao.model.service.ContingenciaService;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.session.SessionUtils;


public abstract class ChaveRPStGenerator {
    private static ContingenciaService contingenciaService;
    private static UnidadeFederativaService unidadeFederativaService;
    
    private final static DateTimeFormatter fmtAnoMes = DateTimeFormat.forPattern("yyMM");

    public static String gerarChaveAcesso(final Conhecimento conhecimento) {
        Integer random = getRandom();
        StringBuilder chave = new StringBuilder();
        if(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge() == null){
        	throw new BusinessException("LMS-04569",new Object[]{conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()});
        	
            
        }
        chave.append(StringUtils.leftPad(String.valueOf(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge()), 2, '0'));
        chave.append(fmtAnoMes.print(conhecimento.getDhEmissao()));
        chave.append(conhecimento.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
        chave.append("99");
        chave.append("000");
        chave.append(StringUtils.leftPad(String.valueOf(conhecimento.getNrConhecimento()), 9, '0'));
        chave.append("1");
        chave.append(StringUtils.leftPad(String.valueOf(random), 8, '0'));
        chave.append(modulo11(chave.toString()));
        return chave.toString();
    }
    
    private static Integer getRandom(){
        return RandomUtils.nextInt(99999999);
    }
    
    private static int modulo11(String numero){
        char[] n = numero.toCharArray();
        int peso = 2;
        int soma = 0;
        for (int i = n.length-1; i >= 0; i--) {
            soma = Integer.valueOf(String.valueOf(n[i])) * peso;
            if(peso == 10){
                peso = 2;
            }
        }
        int mod = soma % 11;
        int dv;
        
        if(mod == 0 || mod == 1){
            dv = 0;
        } else {
            dv = 11 - mod;
        }
        
        return dv;
    }
    
    public void setContingenciaService(ContingenciaService contingenciaService) {
        this.contingenciaService = contingenciaService;
    }
    
    public void setUnidadeFederativaService(
            UnidadeFederativaService unidadeFederativaService) {
        this.unidadeFederativaService = unidadeFederativaService;
    }
}
