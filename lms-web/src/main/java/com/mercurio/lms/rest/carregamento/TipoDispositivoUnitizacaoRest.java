package com.mercurio.lms.rest.carregamento;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.TipoDispositivoUnitizacaoService;

@Path("/carregamento/tipoDispositivoUnitizacao")
public class TipoDispositivoUnitizacaoRest {

	private static final Long cdPallet = 24L;
	private static final Long cdBag = 23L;
	private static final Long cdGaiola = 21L;
	private static final Long cdCofre = 22L;
	
	@InjectInJersey TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;
	
	@GET
	@Path("findTipoDispositivoUnitizacao")
	public Response findTipoDispositivoUnitizacao() {
		
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("tpControleDispositivo", "I");
		List<TipoDispositivoUnitizacao> list = tipoDispositivoUnitizacaoService.findTipoDispositivoOrdenado(criteria);
		
		List<Map<String, Object>> listMap = new ListToMapConverter<TipoDispositivoUnitizacao>().mapRows(list, new RowMapper<TipoDispositivoUnitizacao>() {
			@Override
			public Map<String, Object> mapRow(TipoDispositivoUnitizacao o) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("idTipoDispositivoUnitizacao", o.getIdTipoDispositivoUnitizacao());
				map.put("dsTipoDispositivoUnitizacao", o.getDsTipoDispositivoUnitizacao().getValue());
				
				String tpNrIdentificacao = "tpNrIdentificacao";
				String dsTipoDispositivoUnitizacao = o.getDsTipoDispositivoUnitizacao().getValue(Locale.getDefault());
				if("BAG".equalsIgnoreCase(dsTipoDispositivoUnitizacao)){
					map.put(tpNrIdentificacao, cdBag);
				}else if("PALLET".equalsIgnoreCase(dsTipoDispositivoUnitizacao)){
					map.put(tpNrIdentificacao, cdPallet);
				}else if("COFRE".equalsIgnoreCase(dsTipoDispositivoUnitizacao)){
					map.put(tpNrIdentificacao, cdCofre);
				}else if("GAIOLA".equalsIgnoreCase(dsTipoDispositivoUnitizacao)){
					map.put(tpNrIdentificacao, cdGaiola);
				}

				
				return map;
			}
		});
		
		return Response.ok(listMap).build();
		
	}
	
}
