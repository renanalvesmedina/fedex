package com.mercurio.lms.tabelaprecos.model.service;

import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.tabelaprecos.model.CloneTabelaPrecoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPreco;

@ServiceSecurity
public class ReajusteTabelaPrecoFacade {
	
	private EfetivarReajusteService efetivarReajusteService;
	private CloneTabelaPrecoService cloneTabelaPrecoService;


	@MethodSecurity(processGroup = "tabelaPrecos.reajusteTabelaPrecoFacade", processName = "reajusteTabelaPreco", authenticationRequired=false)
	public ReajusteTabelaPreco reajusteTabelaPreco(Map<String, Object> dadosTelaReajuste) throws Throwable{
		return efetivarReajusteService.executeEfetivarReajuste(dadosTelaReajuste);
	}

	@MethodSecurity(processGroup = "tabelaPrecos.reajusteTabelaPrecoFacade", processName = "executeClonarTabelaPrecoETarifaPrecoRotas", authenticationRequired=false)
	public Boolean executeClonarTabelaPrecoETarifaPrecoRotas(CloneTabelaPrecoDTO cloneTabelaPreco) throws Throwable{
		return cloneTabelaPrecoService.executeClonarTabelaPrecoETarifaPrecoRotas(cloneTabelaPreco);
	}

	@MethodSecurity(processGroup = "tabelaPrecos.reajusteTabelaPrecoFacade", processName = "executeClonarTabelaPreco", authenticationRequired=false)
	public Boolean executeClonarTabelaPreco(CloneTabelaPrecoDTO cloneTabelaPreco) throws Exception{
		return cloneTabelaPrecoService.executeClonarTabelaPreco(cloneTabelaPreco);
	}
	
	@MethodSecurity(processGroup = "tabelaPrecos.reajusteTabelaPrecoFacade", processName = "executeClonarTarifaPrecoRotas", authenticationRequired=false)
	public Boolean executeClonarTarifaPrecoRotas(CloneTabelaPrecoDTO cloneTabelaPreco) throws Exception{
		return cloneTabelaPrecoService.executeClonarTarifaPrecoRotas(cloneTabelaPreco);
	}



	public void setReajusteTabelaPrecoService(EfetivarReajusteService reajusteTabelaPrecoService) {
		this.efetivarReajusteService = reajusteTabelaPrecoService;
	}

	public void setCloneTabelaPrecoService(
			CloneTabelaPrecoService cloneTabelaPrecoService) {
		this.cloneTabelaPrecoService = cloneTabelaPrecoService;
	}
	

}
