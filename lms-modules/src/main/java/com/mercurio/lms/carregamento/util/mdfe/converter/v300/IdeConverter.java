package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v300.Ide;
import com.mercurio.lms.mdfe.model.v300.types.TAmb;
import com.mercurio.lms.mdfe.model.v300.types.TCodUfIBGE;
import com.mercurio.lms.mdfe.model.v300.types.TEmit;
import com.mercurio.lms.mdfe.model.v300.types.TModMD;
import com.mercurio.lms.mdfe.model.v300.types.TModalMD;
import com.mercurio.lms.mdfe.model.v300.types.TUf;
import com.mercurio.lms.mdfe.model.v300.types.TpEmisType;
import com.mercurio.lms.mdfe.model.v300.types.TpTransp;
import com.mercurio.lms.util.FormatUtils;

public class IdeConverter {
    
    private final ManifestoEletronico mdfe;

    private final String nrVersaoLayout;
    
    private final String ambienteMdfe;
    
    private final boolean utilizarTagXMunCarrega;

	private final boolean contingencia;
	
	private final List<String> filiaisPercurso;
    
    public IdeConverter(ManifestoEletronico mdfe,
            String nrVersaoLayout,
            String ambienteMdfe,
            boolean utilizarTagXMunCarrega,
            boolean contingencia, List<String> filiaisPercurso) {
        super();
        this.mdfe = mdfe;
        this.nrVersaoLayout = nrVersaoLayout;
        this.ambienteMdfe = ambienteMdfe;
        this.utilizarTagXMunCarrega = utilizarTagXMunCarrega;
        this.contingencia = contingencia;
        this.filiaisPercurso = filiaisPercurso;
    }

    public Ide convert(boolean isViagem, boolean isAgrupaPorUFDestino) {
        Ide ide = new Ide();
        
        MeioTransporte veiculo = mdfe.getControleCarga().getMeioTransporteByIdTransportado();       
        Pessoa pessoa = null;
        if (mdfe.getControleCarga() != null && mdfe.getControleCarga().getProprietario() != null && mdfe.getControleCarga().getProprietario().getPessoa() != null) {
			 pessoa = mdfe.getControleCarga().getProprietario().getPessoa();
		}

        ide.setCUF(TCodUfIBGE.fromValue(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge().toString()));
        ide.setTpAmb(TAmb.fromValue(ambienteMdfe));
        ide.setTpEmit(TEmit.VALUE_1);
        
        //tpTransp
        if (!"P".equals(veiculo.getTpVinculo().getValue())) {
        	if (pessoa != null) {    			
    			if ("F".equals(pessoa.getTpPessoa().getValue())) {
    				ide.setTpTransp(TpTransp.VALUE_FISICA);    				
    			} else if ("J".equals(pessoa.getTpPessoa().getValue())) {
    				ide.setTpTransp(TpTransp.VALUE_JURIDICA);
    			}    		
    		}          	
        }
        
        ide.setMod(TModMD.VALUE_58);
        ide.setSerie("0");
        ide.setNMDF(mdfe.getNrManifestoEletronico().toString());
        if (StringUtils.isBlank(mdfe.getNrChave())) {
            ide.setCMDF(FormatUtils.fillNumberWithZero(String.valueOf(RandomUtils.nextInt(99999999)),8));
        } else {
            ide.setCMDF(MdfeConverterUtils.tratarString(StringUtils.substring(mdfe.getNrChave(), 35, 43),8));
        }
        ide.setModal(TModalMD.VALUE_1);
        ide.setDhEmi(mdfe.getDhEmissao().toString("yyyy'-'MM'-'dd'T'HH':'mm':'ssZZ"));
        ide.setTpEmis(contingencia ? TpEmisType.VALUE_2 : TpEmisType.VALUE_1);
        ide.setProcEmi("0");
        ide.setVerProc(nrVersaoLayout.replace("a", ""));
        ide.setUFIni(TUf.valueOf( MdfeConverterUtils.getUnidadeFederativaOrigemByManifesto(mdfe).getSgUnidadeFederativa()));
        
        if(!isViagem){
        	if(isAgrupaPorUFDestino && mdfe.getConhecimentos() != null && ! mdfe.getConhecimentos().isEmpty()){
        		ide.setUFFim(TUf.valueOf(mdfe.getConhecimentos().get(0).getMunicipioByIdMunicipioEntrega().getUnidadeFederativa().getSgUnidadeFederativa()));
        	}else{
        		ide.setUFFim(TUf.valueOf(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
        	}
        }else{
        	if (mdfe.getConhecimentos() != null && ! mdfe.getConhecimentos().isEmpty()) {
        		ide.setUFFim(TUf.valueOf(MdfeConverterUtils.getUnidadeFederativaProximoDestinoControleCarga(mdfe.getControleCarga()).getSgUnidadeFederativa()));
        	}
        }

        //InfMunCarrega
        ide.setInfMunCarrega(new InfMunCarregaConverter(mdfe, utilizarTagXMunCarrega).convert());
        
        ide.setInfPercurso(new InfPercursoConverter(filiaisPercurso).convert());
        return ide;
    }
}
