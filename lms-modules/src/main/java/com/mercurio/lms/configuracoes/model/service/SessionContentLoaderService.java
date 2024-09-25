package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.CommitBusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class SessionContentLoaderService {
	
	private MoedaService moedaService;
	private EmpresaService empresaService;
	private FilialService filialService;
	private SetorService setorService;
	private PaisService paisService;
	private ClienteService clienteService;
	private RegionalService regionalService;
	private HistoricoFilialService historicoFilialService;
	
	public void execute() {
		
		Usuario usuario = (Usuario) SessionContext.getUser();
		Empresa empresa = empresaService.findEmpresaPadraoByUsuario(usuario);
		if (empresa == null)
			throw new CommitBusinessException("LMS_EMPRESA_PADRAO_INVALIDA");
		Filial filial = filialService.findFilialPadraoByUsuarioEmpresa(usuario, empresa);
		if (filial == null)
			throw new CommitBusinessException("LMS_FILIAL_PADRAO_INVALIDA");
		this.executeForEmpresaFilial(usuario, empresa, filial);
		
		SessionContext.set(SessionKey.CLIENTES_ACESSO_KEY, clienteService.findClientesByUsuario(usuario));
		SessionContext.set(SessionKey.SETOR_KEY, setorService.findSetorByUsuario(usuario));
	}
	
	public void executeForEmpresaFilial(Usuario usuario, Empresa empresa, Filial filial) {
		
		SessionContext.set(SessionKey.EMPRESA_KEY, empresa);
		SessionContext.set(SessionKey.FILIAL_KEY, filial);
		SessionContext.set(SessionKey.FILIAL_DTZ, filial.getDateTimeZone());
		
		SessionContext.set(SessionKey.ULT_HIST_FILIAL_KEY,
				historicoFilialService.findUltimoHistoricoFilial(filial.getIdFilial()));
		SessionContext.set(SessionKey.PEN_HIST_FILIAL_KEY,
				historicoFilialService.getPenultimoHistoricoFilial(filial.getIdFilial()));

		SessionContext.set(SessionKey.FILIAL_MATRIZ_KEY,
				historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial()));
		
		List filiais = filialService.findFiliaisByUsuarioEmpresa(usuario, empresa, null);
		if (filiais == null || filiais.size() == 0)
			throw new CommitBusinessException("LMS_FILIAIS_PADRAO_INVALIDAS");
		SessionContext.set(SessionKey.FILIAIS_ACESSO_KEY, filiais);
		SessionContext.set(SessionKey.REGIONAIS_FILIAIS_ACESSO_KEY, regionalService.findRegionaisByFiliais(filiais));
		SessionContext.set(SessionKey.REGIONAIS_ACESSO_KEY, regionalService.findRegionaisByUsuario(usuario));
		SessionContext.set(SessionKey.EMPRESAS_ACESSO_KEY, empresaService.findEmpresasByUsuario(usuario));
		
		SessionContext.set(SessionKey.MOEDA_KEY, moedaService.findMoedaByUsuarioEmpresa(usuario, empresa));
		SessionContext.set(SessionKey.PAIS_KEY, paisService.findPaisByUsuarioEmpresa(usuario, empresa));
		
	}
	
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setSetorService(SetorService setorService) {
		this.setorService = setorService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}
	
	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
}