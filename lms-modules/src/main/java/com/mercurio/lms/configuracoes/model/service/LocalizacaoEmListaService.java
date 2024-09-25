package com.mercurio.lms.configuracoes.model.service;

import br.com.tntbrasil.integracao.domains.edw.LocalizacaoEmListaFilterDto;
import br.com.tntbrasil.integracao.domains.edw.LocalizacaoEmListaResultadoDto;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.validator.LocalizacaoEmListaFilterValidator;
import com.mercurio.lms.util.page.PageForWeblogic;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.configuracoes.localizacaoEmListaService"
 */
public class LocalizacaoEmListaService {

    private static final String METODO_FIND_PAGINADO = "findPaginado";
    private static final String METODO_COUNT = "count";
    private static final String METODO_FIND = "find";
    private static final String SERVICO_NOTA_DOCTO_CONSOLIDADO = "notaDoctoConsolidado";
    private static final String EDW_ADDRESS = "edw.service.address";

    @Inject
    private LocalizacaoEmListaFilterValidator localizacaoEmListaFilterValidator;

    public ResultSetPage<LocalizacaoEmListaResultadoDto> findPaginado(LocalizacaoEmListaFilterDto filter) {
        localizacaoEmListaFilterValidator.validate(filter);
        PageForWeblogic<LocalizacaoEmListaResultadoDto> result = buildWebTarget(METODO_FIND_PAGINADO)
                .post(Entity.entity(filter, MediaType.APPLICATION_JSON), new GenericType<PageForWeblogic<LocalizacaoEmListaResultadoDto>>() {
                });
        return result.toResultSetPage();
    }

    public int count(LocalizacaoEmListaFilterDto filter) {
        localizacaoEmListaFilterValidator.validate(filter);
        Entity<LocalizacaoEmListaFilterDto> entity = Entity.entity(filter, MediaType.APPLICATION_JSON);
        // GAMBIARRA: ISSO NÃO FAZ O MENOR SENTIDO POIS É ENVIADO UM INTEIRO MAS FOI JEITO DE FAZER FUNCIONAR
        final String count = buildWebTarget(METODO_COUNT).post(entity, String.class);
        return Integer.parseInt(count);
    }

    public List<LocalizacaoEmListaResultadoDto> find(LocalizacaoEmListaFilterDto filter) {
        localizacaoEmListaFilterValidator.validate(filter);
        return buildWebTarget(METODO_FIND)
                .post(Entity.entity(filter, MediaType.APPLICATION_JSON), new GenericType<List<LocalizacaoEmListaResultadoDto>>() {
                });
    }

    private Builder buildWebTarget(String metodo) {
        final Client client = ClientBuilder.newClient();
        final String property = System.getProperty(EDW_ADDRESS);
        final WebTarget target = client.target(property);
        final WebTarget path = target.path(SERVICO_NOTA_DOCTO_CONSOLIDADO + "/" + metodo);
        final Builder request = path.request(MediaType.APPLICATION_JSON);
        return request;
    }

    public void setLocalizacaoEmListaFilterValidator(LocalizacaoEmListaFilterValidator localizacaoEmListaFilterValidator) {
        this.localizacaoEmListaFilterValidator = localizacaoEmListaFilterValidator;
    }

}
