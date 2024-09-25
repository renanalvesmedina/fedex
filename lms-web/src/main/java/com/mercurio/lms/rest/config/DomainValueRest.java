package com.mercurio.lms.rest.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.lms.annotation.InjectInJersey;

@Path("/config/domainValue")
public class DomainValueRest {
	
	@InjectInJersey DomainValueService domainValueService;
	
	@GET
	@Path("findDomainValues")
	public Response findDomainValues(@QueryParam("name") String name) {
		List<DomainValue> values = domainValueService.findDomainValues(name, false);
		
		List<Map<String, Object>> list = new ListToMapConverter<DomainValue>().mapRows(values, new RowMapper<DomainValue>() {
			
			@Override
			public Map<String, Object> mapRow(DomainValue o) {
				Map<String, Object> toReturn = new HashMap<String, Object>();
				toReturn.put("id", o.getId());
				toReturn.put("description", o.getDescriptionAsString());
				toReturn.put("value", o.getValue());
				return toReturn;
			}
			
		});
		
		return Response.ok(list).build();
	}
	
	@GET
	@Path("findTipoIdentificacaoCnpjCpf")
    public Response findTipoIdentificacaoPessoa() {
    	List<String>dominiosValidos = new ArrayList<String>();
    	dominiosValidos.add("CPF");
    	dominiosValidos.add("CNPJ");
    	List tiposIdentificador = this.domainValueService.findByDomainNameAndValues("DM_TIPO_IDENTIFICACAO_PESSOA", dominiosValidos);
    	List<Map<String, Object>> list = new ListToMapConverter<DomainValue>().mapRows(tiposIdentificador, new RowMapper<DomainValue>() {
			@Override
			public Map<String, Object> mapRow(DomainValue domainValue) {
				Map<String, Object> toReturn = new HashMap<String, Object>();
				toReturn.put("idIdentificador", domainValue.getId());
				toReturn.put("dsIdentificador", domainValue.getDescription().getValue());
				return toReturn;
			}
		});
    	
    	return Response.ok(list).build();
    }

}
