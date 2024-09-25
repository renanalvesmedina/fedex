package com.mercurio.lms.rest.sim;

import br.com.tntbrasil.integracao.domains.edw.LocalizacaoEmListaResultadoDto;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.rest.BaseListRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.LocalizacaoEmListaService;
import com.mercurio.lms.rest.sim.dto.LocalizacaoEmListaFilterLmsDto;
import com.mercurio.lms.rest.sim.dto.LocalizacaoEmListaResultadoLmsWebDto;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sim/consultarLocalizacaoEmLista")
public class ConsultarLocalizacaoEmListaRest extends BaseListRest<LocalizacaoEmListaResultadoLmsWebDto, LocalizacaoEmListaFilterLmsDto> {

    @InjectInJersey
    private LocalizacaoEmListaService localizacaoEmListaService;

    @Override
    protected void removeByIds(List<Long> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected List<LocalizacaoEmListaResultadoLmsWebDto> find(LocalizacaoEmListaFilterLmsDto filter) {
        List<LocalizacaoEmListaResultadoDto> listResultado = localizacaoEmListaService.find(filter.toIntegrationDomainFilter());
        return fromIntegrationDomainContent(listResultado);
    }

    @Override
    protected Integer count(LocalizacaoEmListaFilterLmsDto filter) {
        int count = localizacaoEmListaService.count(filter.toIntegrationDomainFilter());

        if (count == 0) {
            throw new BusinessException("LMS-10040");
        }

        return count;
    }

    @POST()
    @Path("findPaginado")
    public Response findPaginado(LocalizacaoEmListaFilterLmsDto filter) {
        ResultSetPage<LocalizacaoEmListaResultadoLmsWebDto> listResultado = fromIntegrationDomain(localizacaoEmListaService.findPaginado(filter.toIntegrationDomainFilter()));

        if (listResultado.getRowCount() == 0) {
            throw new BusinessException("LMS-10040");
        }

        return getReturnFind(listResultado.getList(), listResultado.getRowCount().intValue());
    }

    private ResultSetPage<LocalizacaoEmListaResultadoLmsWebDto> fromIntegrationDomain(ResultSetPage<LocalizacaoEmListaResultadoDto> paginado) {
        List<LocalizacaoEmListaResultadoLmsWebDto> content = fromIntegrationDomainContent(paginado.getList());
        return new ResultSetPage<LocalizacaoEmListaResultadoLmsWebDto>(paginado.getCurrentPage(), paginado.getHasPriorPage(), paginado.getHasNextPage(), content, paginado.getRowCount());
    }

    private List<LocalizacaoEmListaResultadoLmsWebDto> fromIntegrationDomainContent(List<LocalizacaoEmListaResultadoDto> list) {
        List<LocalizacaoEmListaResultadoLmsWebDto> content = new ArrayList<LocalizacaoEmListaResultadoLmsWebDto>();

        for(LocalizacaoEmListaResultadoDto integrationDomainDto : list) {
            content.add(LocalizacaoEmListaResultadoLmsWebDto.of(integrationDomainDto));
        }

        return content;
    }

    public void setLocalizacaoEmListaService(LocalizacaoEmListaService localizacaoEmListaService) {
        this.localizacaoEmListaService = localizacaoEmListaService;
    }

}
