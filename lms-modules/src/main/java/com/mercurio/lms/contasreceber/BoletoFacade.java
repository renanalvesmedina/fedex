package com.mercurio.lms.contasreceber;

import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.service.AlterarCEPBoletoService;
import com.mercurio.lms.contasreceber.model.service.BaixarBancoBoletoService;
import com.mercurio.lms.contasreceber.model.service.BoletoService;
import com.mercurio.lms.contasreceber.model.service.CancelarBoletoService;
import com.mercurio.lms.contasreceber.model.service.GerarBoletoBoletoService;
import com.mercurio.lms.contasreceber.model.service.ProrrogarVencimentoBoletoService;
import com.mercurio.lms.contasreceber.model.service.ProtestarBoletoService;
import com.mercurio.lms.contasreceber.model.service.RetransmitirBoletoService;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.boletoFacade"
 */
public class BoletoFacade {
	
	private BoletoService boletoService;	
	
	private CancelarBoletoService cancelarBoletoService;
	
	private ProrrogarVencimentoBoletoService prorrogarVencimentoBoletoService;
	
	private BaixarBancoBoletoService baixarBancoBoletoService;
	
	private AlterarCEPBoletoService alterarCEPBoletoService;
	
	private RetransmitirBoletoService retransmitirBoletoService;
	
	private ProtestarBoletoService protestarBoletoService;
	
	private GerarBoletoBoletoService gerarBoletoService;	
	
	public Boleto store(Boleto boleto, ItemList listAnexos) {
		return gerarBoletoService.store(boleto, listAnexos);
	}
	
	public Boleto store(Boleto boleto, DevedorDocServFat devedorDocServFat, ItemList listAnexos) {
		return gerarBoletoService.store(boleto, devedorDocServFat, listAnexos);
	}	
	
	public Boleto findById(Long idBoleto){
		return boletoService.findById(idBoleto);
	}
	
    public void removeById(Long id) {
    	boletoService.removeById(id);
    }	
	
	public Boleto cancelarBoleto(Boleto boleto, MotivoOcorrencia motivoOcorrencia, String dsHistoricoBoleto){		
		return cancelarBoleto(boleto, motivoOcorrencia, dsHistoricoBoleto, null);
	}
	
	public Boleto cancelarBoleto(Boleto boleto, MotivoOcorrencia motivoOcorrencia, String dsHistoricoBoleto, ItemList listAnexos){		
		return cancelarBoletoService.executeCancelarBoleto(boleto, motivoOcorrencia, dsHistoricoBoleto, listAnexos);
	}
	
	public Boleto cancelarBoletoIntegracao(Boleto boleto){		
		return cancelarBoletoService.executeCancelarBoletoIntegracao(boleto);
	}
	
	public void prorrogarVencimentoBoleto(Boleto boleto, MotivoOcorrencia motivoOcorrencia, String dsHistoricoBoleto){		
		prorrogarVencimentoBoleto(boleto, motivoOcorrencia, dsHistoricoBoleto, null);
	}
	
	public void prorrogarVencimentoBoleto(Boleto boleto, MotivoOcorrencia motivoOcorrencia, String dsHistoricoBoleto, ItemList listAnexos){		
		prorrogarVencimentoBoletoService.executeProrrogaVencimentoBoleto(boleto, motivoOcorrencia, dsHistoricoBoleto, listAnexos);
	}
	
	public void baixarBancoBoleto(Boleto boleto){		
		baixarBancoBoletoService.executeBaixarBancoBoleto(boleto);
	}
	
	public void alterarCEPBoleto(Boleto boleto){		
		alterarCEPBoletoService.executeAlterarCEPBoleto(boleto);
	}
	
	public void retransmitirBoleto(Boleto boleto){		
		retransmitirBoletoService.executeRetransmitirBoleto(boleto);
	}
	
	public void protestarBoleto(Boleto boleto){		
		protestarBoletoService.executeProtestarBoleto(boleto);
	}

	public void setAlterarCEPBoletoService(
			AlterarCEPBoletoService alterarCEPBoletoService) {
		this.alterarCEPBoletoService = alterarCEPBoletoService;
	}

	public void setBaixarBancoBoletoService(
			BaixarBancoBoletoService baixarBancoBoletoService) {
		this.baixarBancoBoletoService = baixarBancoBoletoService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setCancelarBoletoService(CancelarBoletoService cancelarBoletoService) {
		this.cancelarBoletoService = cancelarBoletoService;
	}

	public void setProrrogarVencimentoBoletoService(
			ProrrogarVencimentoBoletoService prorrogarVencimentoBoletoService) {
		this.prorrogarVencimentoBoletoService = prorrogarVencimentoBoletoService;
	}

	public void setProtestarBoletoService(
			ProtestarBoletoService protestarBoletoService) {
		this.protestarBoletoService = protestarBoletoService;
	}

	public void setRetransmitirBoletoService(
			RetransmitirBoletoService retransmitirBoletoService) {
		this.retransmitirBoletoService = retransmitirBoletoService;
	}

	public void setGerarBoletoService(GerarBoletoBoletoService gerarBoletoService) {
		this.gerarBoletoService = gerarBoletoService;
	}	
	
}