package com.mercurio.lms.expedicao.model.service;

import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.FilialPercursoUF;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ManifestoEletronicoServiceTest {

    private ManifestoEletronicoService manifestoEletronicoService;

    @BeforeTest
    public void setup() {
        this.manifestoEletronicoService = new ManifestoEletronicoService();
    }

    /**
     * Arquivo: LMS\Especificações técnicas\04 Expedição\De-Para MDFe 3.00.xlsx
     * Aba: Detalhamento infPercurso
     * 
     * @author leonardo.carmona
     */
    @Test
    public void gerarPercursoUnidadeFederativaTest() {
        List<FilialPercursoUF> percursoAtual = this.getFilialPercursoUF();
        List<String> percursoEsperado = this.getPercursoEsperado();
        List<String> percursoGerado = this.manifestoEletronicoService.gerarPercursoUnidadeFederativa(percursoAtual, this.createFilialWithSgUnidadeFederativa(""), this.createFilialWithSgUnidadeFederativa(""));

        Assert.assertEquals(percursoGerado, percursoEsperado);
    }

    /**
     * Arquivo: LMS\Especificações técnicas\04 Expedição\De-Para MDFe 3.00.xlsx
     * Aba: Detalhamento infPercurso
     * 
     * @author leonardo.carmona
     */
    @Test
    public void gerarPercursoUnidadeFederativaSemOrigemDestinoTest() {
        List<FilialPercursoUF> percursoAtual = this.getFilialPercursoUFSemOrigemDestino();
        List<String> percursoEsperado = this.getPercursoEsperadoSemOrigemDestino();
        List<String> percursoGerado = this.manifestoEletronicoService.gerarPercursoUnidadeFederativa(percursoAtual, this.createFilialWithSgUnidadeFederativa("GO"), this.createFilialWithSgUnidadeFederativa("BA"));

        Assert.assertEquals(percursoGerado, percursoEsperado);
    }
    
    /**
     * Arquivo: LMS\Especificações técnicas\04 Expedição\De-Para MDFe 3.00.xlsx
     * Aba: Detalhamento infPercurso
     * 
     * @author leonardo.carmona
     */
    @Test
    public void gerarPercursoUnidadeFederativaComPercursoMinimoTest() {
        List<FilialPercursoUF> percursoAtual = this.getFilialPercursoUFComPercursoMinimo();
        List<String> percursoEsperado = this.getPercursoEsperadoComPercursoMinimo();
        List<String> percursoGerado = this.manifestoEletronicoService.gerarPercursoUnidadeFederativa(percursoAtual, this.createFilialWithSgUnidadeFederativa("RS"), this.createFilialWithSgUnidadeFederativa("SC"));

        Assert.assertEquals(percursoGerado, percursoEsperado);
    }

    private List<FilialPercursoUF> getFilialPercursoUF() {
        List<FilialPercursoUF> filialPercursoUF = new ArrayList<FilialPercursoUF>();

        this.addSgUnidadeFederativa(filialPercursoUF, "PB");
        this.addSgUnidadeFederativa(filialPercursoUF, "CE");
        this.addSgUnidadeFederativa(filialPercursoUF, "PE");
        this.addSgUnidadeFederativa(filialPercursoUF, "AL");
        this.addSgUnidadeFederativa(filialPercursoUF, "BA");
        this.addSgUnidadeFederativa(filialPercursoUF, "AL");
        this.addSgUnidadeFederativa(filialPercursoUF, "BA");
        this.addSgUnidadeFederativa(filialPercursoUF, "MG");

        return filialPercursoUF;
    }

    private List<String> getPercursoEsperado() {
        List<String> percursoEsperado = new ArrayList<String>();

        percursoEsperado.add("PB");
        percursoEsperado.add("CE");
        percursoEsperado.add("PE");
        percursoEsperado.add("AL");
        percursoEsperado.add("BA");
        percursoEsperado.add("AL");
        percursoEsperado.add("BA");
        percursoEsperado.add("MG");

        return percursoEsperado;
    }

    private List<FilialPercursoUF> getFilialPercursoUFSemOrigemDestino() {
        List<FilialPercursoUF> filialPercursoUF = new ArrayList<FilialPercursoUF>();

        this.addSgUnidadeFederativa(filialPercursoUF, "GO");
        this.addSgUnidadeFederativa(filialPercursoUF, "DF");
        this.addSgUnidadeFederativa(filialPercursoUF, "GO");
        this.addSgUnidadeFederativa(filialPercursoUF, "DF");
        this.addSgUnidadeFederativa(filialPercursoUF, "BA");

        return filialPercursoUF;
    }

    private List<String> getPercursoEsperadoSemOrigemDestino() {
        List<String> percursoEsperado = new ArrayList<String>();

        percursoEsperado.add("DF");
        percursoEsperado.add("GO");
        percursoEsperado.add("DF");

        return percursoEsperado;
    }
    
    private List<FilialPercursoUF> getFilialPercursoUFComPercursoMinimo() {
        List<FilialPercursoUF> filialPercursoUF = new ArrayList<FilialPercursoUF>();

        this.addSgUnidadeFederativa(filialPercursoUF, "RS");
        this.addSgUnidadeFederativa(filialPercursoUF, "SC");

        return filialPercursoUF;
    }

    private List<String> getPercursoEsperadoComPercursoMinimo() {
        return new ArrayList<String>();
    }

    private Filial createFilialWithSgUnidadeFederativa(String sgUF) {
        Filial filial = new Filial();

        filial.setPessoa(new Pessoa());
        filial.getPessoa().setEnderecoPessoa(new EnderecoPessoa());
        filial.getPessoa().getEnderecoPessoa().setMunicipio(new Municipio());
        filial.getPessoa().getEnderecoPessoa().getMunicipio().setUnidadeFederativa(new UnidadeFederativa());
        filial.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().setSgUnidadeFederativa(sgUF);

        return filial;
    }

    private void addSgUnidadeFederativa(List<FilialPercursoUF> filialPercursoUF, String sgUF) {
        FilialPercursoUF fp = new FilialPercursoUF();
        UnidadeFederativa uf = new UnidadeFederativa();

        fp.setUnidadeFederativa(uf);
        uf.setSgUnidadeFederativa(sgUF);

        filialPercursoUF.add(fp);
    }

}
