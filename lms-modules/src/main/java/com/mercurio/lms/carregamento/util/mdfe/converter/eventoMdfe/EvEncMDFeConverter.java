package com.mercurio.lms.carregamento.util.mdfe.converter.eventoMdfe;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.EvEncMDFe;
import com.mercurio.lms.mdfe.model.v100.types.DescEventoType;
import com.mercurio.lms.mdfe.model.v100.types.TCodUfIBGE;
import com.mercurio.lms.municipios.model.Filial;

public class EvEncMDFeConverter {

    private ManifestoEletronico mdfe;
    
    private Filial filialUsuarioLogado;
    
    public EvEncMDFeConverter(ManifestoEletronico mdfe,
            Filial filialUsuarioLogado) {
        super();
        this.mdfe = mdfe;
        this.filialUsuarioLogado = filialUsuarioLogado;
    }

    public EvEncMDFe convert() {
        EvEncMDFe evEncMDFe = new EvEncMDFe();
        
        evEncMDFe.setDescEvento(DescEventoType.ENCERRAMENTO);
        evEncMDFe.setNProt(mdfe.getNrProtocolo() == null ? null : mdfe.getNrProtocolo().toString());
        evEncMDFe.setDtEnc(new YearMonthDay().toString("yyyy'-'MM'-'dd"));
        evEncMDFe.setCUF(TCodUfIBGE.fromValue(filialUsuarioLogado.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge().toString()));
        evEncMDFe.setCMun(MdfeConverterUtils.formatCMun(filialUsuarioLogado.getPessoa().getEnderecoPessoa().getMunicipio()));

        return evEncMDFe;
    }
    
}
