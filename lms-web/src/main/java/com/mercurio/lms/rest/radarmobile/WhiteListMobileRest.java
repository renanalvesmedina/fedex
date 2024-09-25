package com.mercurio.lms.rest.radarmobile;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.facade.radar.impl.WhiteListFacadeImpl;
import com.mercurio.lms.tracking.util.TrackingContantsUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Public
@Path("/whitelist/mobile")
public class WhiteListMobileRest extends BaseRest {
	
	private static final Logger LOGGER = LogManager.getLogger(WhiteListMobileRest.class);

	@InjectInJersey
	private WhiteListFacadeImpl whiteListFacade;

	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;

	@POST
	@Path("/storeWhiteListFacade")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> storeWhiteListFacade(TypedFlatMap map) {
		try {
			return whiteListFacade.storeWhiteList(map);
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
		return null;
	}

	@POST
	@Path("/storeWhiteListService")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> storeWhiteListService(TypedFlatMap map) {
		try {
			String situacao = map.getString(TrackingContantsUtil.WHITE_LIST_PRM_SITUACAO);

			if(TrackingContantsUtil.WHITE_LIST_SITUACAO_ATIVO.equals(situacao)){
				return whiteListFacade.storeWhiteList(map);
			}
			if(TrackingContantsUtil.WHITE_LIST_SITUACAO_INATIVO.equals(situacao)){
				return whiteListFacade.disableWhiteList(map);
			}
			String msg = MessageFormat.format(configuracoesFacade.getMensagem("LMS-25132"), TrackingContantsUtil.WHITE_LIST_PRM_SITUACAO, situacao);
			throw new InvalidParameterException(msg);
		} catch (Exception exception) {
			LOGGER.error(map, exception);
			TypedFlatMap retorno = new TypedFlatMap();
			retorno.put("msgErro", new ArrayList<String>(Arrays.asList(new String[]{exception.getMessage()})));
			return retorno;
		}
	}
	
}
