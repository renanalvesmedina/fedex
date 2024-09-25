package com.mercurio.lms.carregamento.util.mdfe.converter.eventoMdfe;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.carregamento.util.mdfe.type.TipoEventoMdfe;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.InfEvento;
import com.mercurio.lms.mdfe.model.v100.types.TAmb;
import com.mercurio.lms.mdfe.model.v100.types.TCOrgaoIBGE;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;

public class InfEventoConverter {

    private ManifestoEletronico mdfe;
    
    private TipoEventoMdfe tipoEventoMdfe;
    
    private String ambienteMdfe;
    
    private Filial filialUsuarioLogado;
    
    private String justificativaCancelamentoMdfe;
    
    private String nrVersaoLayout;
    
    public InfEventoConverter(ManifestoEletronico mdfe, TipoEventoMdfe tipoEventoMdfe,
            String ambienteMdfe, Filial filialUsuarioLogado, String justificativaCancelamentoMdfe, String nrVersaoLayout) {
        super();
        this.mdfe = mdfe;
        this.tipoEventoMdfe = tipoEventoMdfe;
        this.ambienteMdfe = ambienteMdfe;
        this.filialUsuarioLogado = filialUsuarioLogado;
        this.justificativaCancelamentoMdfe = justificativaCancelamentoMdfe;
        this.nrVersaoLayout = nrVersaoLayout;
    }

    public InfEvento convert() {
        InfEvento infEvento = new InfEvento();

        String tipoEvento = tipoEventoMdfe.getTpEvento();
        
        infEvento.setId("ID"+tipoEvento+mdfe.getNrChave()+"01");
        infEvento.setCOrgao(TCOrgaoIBGE.fromValue(filialUsuarioLogado.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge().toString()));
        infEvento.setTpAmb(TAmb.fromValue(ambienteMdfe));
        infEvento.setCNPJ(MdfeConverterUtils.tratarString(mdfe.getFilialOrigem().getPessoa().getNrIdentificacao(),14));
        infEvento.setChMDFe(mdfe.getNrChave());
        infEvento.setDhEvento(JTDateTimeUtils.getDataHoraAtual(mdfe.getFilialOrigem()).toString("yyyy'-'MM'-'dd'T'HH':'mm':'ss"));
        infEvento.setTpEvento(tipoEvento);
        infEvento.setNSeqEvento("1");

        infEvento.setDetEvento(new DetEventoConverter(mdfe, filialUsuarioLogado, justificativaCancelamentoMdfe, tipoEventoMdfe, nrVersaoLayout).convert());
        
        return infEvento;
    }
    
}
