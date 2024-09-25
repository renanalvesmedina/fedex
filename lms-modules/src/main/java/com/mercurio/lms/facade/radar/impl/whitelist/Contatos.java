package com.mercurio.lms.facade.radar.impl.whitelist;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.DispositivoContato;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.util.JTDateTimeUtils;
import java.util.Arrays;
import java.util.List;

public class Contatos {


    private static final String NM_CONTATO_RADAR = "RADAR";

    private final DispositivoContato dispositivo;

    private Contatos(DispositivoContato dispositivo) {
        this.dispositivo = dispositivo;
    }

    public static Contatos from(DispositivoContato dispositivo) {
        DispositivoContato d = (dispositivo != null) ? dispositivo : new DispositivoContato();
        
        Contato contato;
        
        if(d.getContato() == null){
        	contato = novoContato(d);
        } else {
        	contato = d.getContato();
        	contato.setDtUltimaMovimentacao(JTDateTimeUtils.getDataAtual());
        }
        d.setContato(contato);
        return new Contatos(d);
    }

    private static Contato novoContato(DispositivoContato dispositivo) {
        Contato contato = new Contato();
        contato.setNmContato(NM_CONTATO_RADAR);
        contato.setDtCadastro(JTDateTimeUtils.getDataAtual());
        contato.setDispositivoContato(dispositivo);
        return contato;
    }

    public Contatos token(String token) {
        dispositivo.setDsToken(token);
        return this;
    }

    public Contatos plataforma(DomainValue plataforma) {
        dispositivo.setTpPlataforma(plataforma);
        return this;
    }

    public Contatos ddd(String ddd) {
        dispositivo.setNrDdd(ddd);
        return this;
    }

    public Contatos numero(String numero) {
        dispositivo.setNrTelefone(numero);
        return this;
    }

    public Contatos versao(String versao) {
        dispositivo.setDsVersao(versao);
        return this;
    }

    public Contatos tipo(DomainValue tipo) {
        dispositivo.getContato().setTpContato(tipo);
        return this;
    }

    public Contatos pessoa(Pessoa pessoa) {
        dispositivo.getContato().setPessoa(pessoa);
        return this;
    }

    public List<Contato> asList() {
        return Arrays.asList(dispositivo.getContato());
    }

}
