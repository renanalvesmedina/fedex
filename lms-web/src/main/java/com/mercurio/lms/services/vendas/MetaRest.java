package com.mercurio.lms.services.vendas;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.tntbrasil.integracao.domains.comissionamento.MetaDTO;
import br.com.tntbrasil.integracao.domains.comissionamento.MetaTerritorioDTO;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.vendas.model.Meta;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.service.MetaService;
import com.mercurio.lms.vendas.model.service.TerritorioService;


@Path("/vendas/meta")
public class MetaRest {
	
	public static final String RETORNO_OK = "OK";
	private static final Logger LOGGER = LogManager.getLogger(MetaRest.class);

	@InjectInJersey
	private MetaService metaService;
	@InjectInJersey
	private TerritorioService territorioService;
	@InjectInJersey
	private DomainValueService domainValueService;
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	@InjectInJersey
	private UsuarioLMSService usuarioLMSService;
	


	@POST
	@Path("storeMetaTerritorio")
	public Response storeMetaTerritorio(MetaTerritorioDTO metaTerritorio) {
		try {
			List<Meta> metas = extrairMetas(metaTerritorio);
			metaService.storeAll(metas); 
			
			metaTerritorio.setStatusRetorno(RETORNO_OK);
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			} 
			
			if (StringUtils.isNotBlank(exception.getMessage())) {
				metaTerritorio.setStatusRetorno(exception.getMessage());
			} else {
				metaTerritorio.setStatusRetorno(exception.getClass().toString());
			}
		}
		return Response.ok(metaTerritorio).build();
	}

	public MetaService getMetaService() {
		return metaService;
	}

	public void setMetaService(MetaService metaService) {
		this.metaService = metaService;
	}
	
	private List<Meta> extrairMetas(MetaTerritorioDTO metaTerritorio) {
		
		Territorio territorio = null;
		if (StringUtils.isNotBlank(metaTerritorio.getTerritorio())) {
			territorio = territorioService.findAtivoPorNome(metaTerritorio.getTerritorio());
		}
		
		DomainValue tpModal = null;
		if (StringUtils.isNotBlank(metaTerritorio.getModal())) {
			try {
				tpModal = domainValueService.findDomainValueByValue("DM_MODAL", metaTerritorio.getModal());
			} catch (InfrastructureException infrastructureException) { 
				//capturando para, na validação, lançar a exceção correta ('tpModal' irá continuar null)
			}
		}
		
		List<Meta> metas = new ArrayList<Meta>();
		for (MetaDTO metaDTO : metaTerritorio.getMeta()) {
			metas.add(createMeta(metaDTO, territorio, tpModal, metaTerritorio.getAno()));
		}

		return metas;
	}
	
	private Meta createMeta(MetaDTO metaDTO, Territorio territorio, DomainValue tpModal, Integer nrAno) {
		Meta meta = null;
		if (territorio != null && tpModal != null && nrAno != null && metaDTO.getNmMes() != null) {
			meta = metaService.findByNaturalKey(territorio.getIdTerritorio(), tpModal, nrAno, metaDTO.getNmMes());
		} 
		
		if (meta == null) {
			meta = new Meta();
		}
		meta.setTerritorio(territorio);
		meta.setTpModal(tpModal);
		meta.setNrAno(nrAno);
		meta.setNmMes(StringUtils.upperCase(metaDTO.getNmMes()));

		meta.setVlMeta(metaDTO.getVlMeta());
		meta.setUsuarioAlteracao(findUsuarioIntegracao());
		return meta;
	}
	
	private UsuarioLMS findUsuarioIntegracao() {
		ParametroGeral idUsuario = parametroGeralService.findByNomeParametro("ID_USUARIO_PADRAO_INTEGRACAO");
		return usuarioLMSService.findById(Long.valueOf(idUsuario.getDsConteudo()));
	}

	public TerritorioService getTerritorioService() {
		return territorioService;
	}

	public void setTerritorioService(TerritorioService territorioService) {
		this.territorioService = territorioService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
}
