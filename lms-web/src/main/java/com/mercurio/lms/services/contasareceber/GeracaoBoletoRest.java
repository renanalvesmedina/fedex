package com.mercurio.lms.services.contasareceber;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;

import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contasreceber.model.service.BoletoService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;

@Path("/contasareceber/geracaoBoleto")
public class GeracaoBoletoRest extends BaseRest {

	private static final Logger log = LogManager.getLogger(GeracaoBoletoRest.class);
	
	@InjectInJersey
	private BoletoService boletoService;

	@InjectInJersey
	private FaturaService faturaService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("gerarBoleto")
	public Response gerarBoleto(FaturaDMN faturaDMN) {
		setApplicationSession(faturaService.findSessionData());
		BoletoDMN boletoDMN = new BoletoDMN();
		if (null != faturaDMN) {
			
			//Apagar
			log.info("Empresa + fatura: " + faturaDMN.getDsEmpresaCliente() + " + " + faturaDMN.getSgFilialNrFatura());
			
			boletoDMN = boletoService.executeReportByIdFatura(faturaDMN.getIdFatura());
			if (boletoDMN != null) {
				boletoDMN.setEmpresaCliente(faturaDMN.getDsEmpresaCliente());
				boletoDMN.setSgFilialNrFatura(faturaDMN.getSgFilialNrFatura());
				boletoDMN.setPath(faturaDMN.getPath());
				log.info("Dados do boleto recuperados. idBoleto: " + boletoDMN.getIdBoleto() + " - idFatura: " + faturaDMN.getIdFatura());
				
			} else {
				log.error("Dados do boleto não recuperados. idFatura: " + faturaDMN.getIdFatura());
				
			}
		} 
		return Response.ok(boletoDMN).build();
		
	}

	/**
	 * Necessário para compatibilidade com as queries usadas na geração de relatórios
	 *  
	 * @param sessionData
	 */
	private void setApplicationSession(Map<String, Object> sessionData) {
		SessionContext.setUser((Usuario) MapUtils.getObject(sessionData, "usuario"));
		SessionContext.set("PAIS_KEY", (Pais) MapUtils.getObject(sessionData, "pais"));
		SessionContext.set("MOEDA_KEY", (Moeda) MapUtils.getObject(sessionData, "moeda"));
		SessionContext.set("FILIAL_KEY", (Filial) MapUtils.getObject(sessionData, "filial"));
	}

}
