package com.mercurio.lms.vol.model.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.vol.volGerenciaBaixasService" FIXME hj a qtd de coletas
 * é passada do celular, mas não é usada
 */
public class VolGerenciaBaixasService {

    private UsuarioService usuarioService;
    private PaisService paisService;
    private VolBaixarColetasService volBaixarColetasService;
    private VolBaixarEntregasService volBaixarEntregasService;
    private FilialService filialService;
    private VolDadosSessaoService volDadosSessaoService;

    /**
     * Método que seta na sessão os dados necessários para realizar a baixa de
     * coleta/entrega
     *
     * @param map
     */
    public void executeGerenciaBaixa(TypedFlatMap map) throws IOException {
        Usuario usuario = getUsuarioService().findUsuarioByLogin("vol");
        Pais pais = getPaisService().findByIdPessoa(map.getLong("idFilial"));
        Filial filial = getFilialService().findById(map.getLong("idFilial"));

        volDadosSessaoService.setDadosSessaoBanco(usuario, filial, pais);

        if ("C".equalsIgnoreCase(map.getString("tpBaixa"))) {
            getVolBaixarColetasService().executeBaixa(map);
        } else if ("E".equalsIgnoreCase(map.getString("tpBaixa"))) {
            getVolBaixarEntregasService().executeBaixa(map);
        } else if ("EA".equalsIgnoreCase(map.getString("tpBaixa"))) {
            String ids = map.getString("ids");
            List<String> listId = CollectionUtils.arrayToList(ids.split(","));

            List<Long> l = new ArrayList<Long>();
            for (String id : listId) {
                l.add(Long.parseLong(id));
            }

            getVolBaixarEntregasService().executarBaixaEntregaAerea(l);
        }
        
        getVolBaixarEntregasService().saveComprovanteEntrega(map);
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public PaisService getPaisService() {
        return paisService;
    }

    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }

    public VolBaixarColetasService getVolBaixarColetasService() {
        return volBaixarColetasService;
    }

    public void setVolBaixarColetasService(VolBaixarColetasService volBaixarColetasService) {
        this.volBaixarColetasService = volBaixarColetasService;
    }

    public VolBaixarEntregasService getVolBaixarEntregasService() {
        return volBaixarEntregasService;
    }

    public void setVolBaixarEntregasService(VolBaixarEntregasService volBaixarEntregasService) {
        this.volBaixarEntregasService = volBaixarEntregasService;
    }

    public FilialService getFilialService() {
        return filialService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
        this.volDadosSessaoService = volDadosSessaoService;
    }

}
