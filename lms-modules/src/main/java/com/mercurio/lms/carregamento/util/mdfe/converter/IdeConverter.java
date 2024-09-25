package com.mercurio.lms.carregamento.util.mdfe.converter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.Ide;
import com.mercurio.lms.mdfe.model.v100.types.TAmb;
import com.mercurio.lms.mdfe.model.v100.types.TCodUfIBGE;
import com.mercurio.lms.mdfe.model.v100.types.TEmit;
import com.mercurio.lms.mdfe.model.v100.types.TModMD;
import com.mercurio.lms.mdfe.model.v100.types.TModalMD;
import com.mercurio.lms.mdfe.model.v100.types.TUf;
import com.mercurio.lms.mdfe.model.v100.types.TpEmisType;
import com.mercurio.lms.util.FormatUtils;

public class IdeConverter {
    
    private final ManifestoEletronico mdfe;

    private final String nrVersaoLayout;
    
    private final String ambienteMdfe;
    
    private final boolean utilizarTagXMunCarrega;
    
    public IdeConverter(ManifestoEletronico mdfe,
            String nrVersaoLayout,
            String ambienteMdfe,
            boolean utilizarTagXMunCarrega) {
        super();
        this.mdfe = mdfe;
        this.nrVersaoLayout = nrVersaoLayout;
        this.ambienteMdfe = ambienteMdfe;
        this.utilizarTagXMunCarrega = utilizarTagXMunCarrega;
    }

    public Ide convert() {
        Ide ide = new Ide();

        ide.setCUF(TCodUfIBGE.fromValue(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge().toString()));
        ide.setTpAmb(TAmb.fromValue(ambienteMdfe));
        ide.setTpEmit(TEmit.VALUE_1);
        ide.setMod(TModMD.VALUE_58);
        ide.setSerie("0");
        ide.setNMDF(mdfe.getNrManifestoEletronico().toString());
        if (StringUtils.isBlank(mdfe.getNrChave())) {
            ide.setCMDF(FormatUtils.fillNumberWithZero(String.valueOf(RandomUtils.nextInt(99999999)),8));
        } else {
            ide.setCMDF(MdfeConverterUtils.tratarString(StringUtils.substring(mdfe.getNrChave(), 35, 43),8));
        }
        ide.setModal(TModalMD.VALUE_1);
        ide.setDhEmi(mdfe.getDhEmissao().toString("yyyy'-'MM'-'dd'T'HH':'mm':'ss"));
        ide.setTpEmis(TpEmisType.VALUE_1);
        ide.setProcEmi("0");
        ide.setVerProc(nrVersaoLayout);
        ide.setUFIni(TUf.valueOf(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
        ide.setUFFim(TUf.valueOf(mdfe.getFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));

        //InfMunCarrega
        ide.setInfMunCarrega(new InfMunCarregaConverter(mdfe, utilizarTagXMunCarrega).convert());
        
        return ide;
    }
    
}
