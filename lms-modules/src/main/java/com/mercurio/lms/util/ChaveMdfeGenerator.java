package com.mercurio.lms.util;

import com.mercurio.lms.mdfe.model.v100a.Ide;
import com.mercurio.lms.mdfe.model.v100a.InfMDFe;

public abstract class ChaveMdfeGenerator {

	 public static String generate(com.mercurio.lms.mdfe.model.v300.InfMDFe infMDFe) {
        StringBuilder sb = new StringBuilder();
        com.mercurio.lms.mdfe.model.v300.Ide ide = infMDFe.getIde();
        
        sb.append(FormatUtils.fillNumberWithZero(ide.getCUF().toString(), 2));
        sb.append(getYM(ide.getDhEmi()));
        sb.append(FormatUtils.fillNumberWithZero(infMDFe.getEmit().getCNPJ(), 14));
        sb.append(FormatUtils.fillNumberWithZero(ide.getMod().toString(),2));
        sb.append(FormatUtils.fillNumberWithZero(ide.getSerie(),3));
        sb.append(FormatUtils.fillNumberWithZero(ide.getNMDF(),9));
        sb.append(FormatUtils.fillNumberWithZero(ide.getTpEmis().toString(),1));
        sb.append(FormatUtils.fillNumberWithZero(ide.getCMDF(),8));
        sb.append(modulo11(sb.toString()));
        
        return sb.toString();
    }
    public static String generate(InfMDFe infMDFe) {
        StringBuilder sb = new StringBuilder();
        Ide ide = infMDFe.getIde();
        
        sb.append(FormatUtils.fillNumberWithZero(ide.getCUF().toString(), 2));
        sb.append(getYM(ide.getDhEmi()));
        sb.append(FormatUtils.fillNumberWithZero(infMDFe.getEmit().getCNPJ(), 14));
        sb.append(FormatUtils.fillNumberWithZero(ide.getMod().toString(),2));
        sb.append(FormatUtils.fillNumberWithZero(ide.getSerie(),3));
        sb.append(FormatUtils.fillNumberWithZero(ide.getNMDF(),9));
        sb.append(FormatUtils.fillNumberWithZero(ide.getTpEmis().toString(),1));
        sb.append(FormatUtils.fillNumberWithZero(ide.getCMDF(),8));
        sb.append(modulo11(sb.toString()));
        
        return sb.toString();
    }
    public static String generate(com.mercurio.lms.mdfe.model.v100.InfMDFe infMDFe) {
        StringBuilder sb = new StringBuilder();
        com.mercurio.lms.mdfe.model.v100.Ide ide = infMDFe.getIde();
        
        sb.append(FormatUtils.fillNumberWithZero(ide.getCUF().toString(), 2));
        sb.append(getYM(ide.getDhEmi()));
        sb.append(FormatUtils.fillNumberWithZero(infMDFe.getEmit().getCNPJ(), 14));
        sb.append(FormatUtils.fillNumberWithZero(ide.getMod().toString(),2));
        sb.append(FormatUtils.fillNumberWithZero(ide.getSerie(),3));
        sb.append(FormatUtils.fillNumberWithZero(ide.getNMDF(),9));
        sb.append(FormatUtils.fillNumberWithZero(ide.getTpEmis().toString(),1));
        sb.append(FormatUtils.fillNumberWithZero(ide.getCMDF(),8));
        sb.append(modulo11(sb.toString()));
        
        return sb.toString();
    }
    private static String getYM(String dhEmi) {
        return dhEmi.substring(2,4)+dhEmi.substring(5,7);
    }
    
    private static int modulo11(String numero){
        char[] n = numero.toCharArray();
        int peso = 2;
        int soma = 0;
        for (int i = n.length-1; i >= 0; i--) {
            soma += Integer.valueOf(String.valueOf(n[i])) * peso++;
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

}
