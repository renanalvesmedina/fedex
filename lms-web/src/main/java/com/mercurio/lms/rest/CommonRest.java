package com.mercurio.lms.rest;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.dto.DomainDTO;
import com.mercurio.lms.util.model.service.DomainService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("util/commom")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class CommonRest extends BaseRest {

    @InjectInJersey
    private DomainService domainService;

    @GET
    @Path("findDomianValue/{domainName}")
    public Response findDomainValue(@PathParam("domainName") String domainName){
        List<DomainDTO> domainList = domainService.findDomainByDomainName(domainName);

        return Response.ok(domainList).build();
    }

    @GET
    @Path("findMessageErro/{erroCode}")
    public Response findMessageErro(@PathParam("erroCode") String erroCode) {
        throw new BusinessException(erroCode);
    }
}
