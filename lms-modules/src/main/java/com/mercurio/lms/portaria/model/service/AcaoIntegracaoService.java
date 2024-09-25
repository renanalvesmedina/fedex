package com.mercurio.lms.portaria.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.portaria.model.AcaoIntegracao;
import com.mercurio.lms.portaria.model.dao.AcaoIntegracaoDAO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.GrupoEconomico;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.acaoIntegracaoService"
 */
public class AcaoIntegracaoService extends CrudService<AcaoIntegracao, Long> {
	private EnderecoPessoaService enderecoPessoaService;
	private UnidadeFederativaService unidadeFederativaService;
	
	public void setAcaoIntegracaoDAO(AcaoIntegracaoDAO acaoIntegracaoDAO) {
		setDao(acaoIntegracaoDAO);
	}

	public AcaoIntegracaoDAO getAcaoIntegracaoDAO() {
		return (AcaoIntegracaoDAO)  getDao();
	}
	
	public List<AcaoIntegracao> findAcaoIntegracaoPorEvento( Filial filialByIdFilialOrigem, 
																Filial filialByIdFilialDestino,
																DomainValue tpModal, 
																String tpDocumento, 
																GrupoEconomico grupoEconomico,
																Cliente cliente){
		
		Long idUfCliente = null;
		if(cliente != null){
			idUfCliente = unidadeFederativaService.findConfirmacaoSaida(cliente.getIdCliente());
		}
		
		Long idUfFilialOrigem = unidadeFederativaService.findConfirmacaoSaida(filialByIdFilialOrigem.getIdFilial());
		Long idUfFilialDestino = unidadeFederativaService.findConfirmacaoSaida(filialByIdFilialDestino.getIdFilial());
		
		return getAcaoIntegracaoDAO().findAcaoIntegracaoPorEvento(filialByIdFilialOrigem, idUfFilialOrigem,
				filialByIdFilialDestino, idUfFilialDestino, tpModal, tpDocumento, grupoEconomico, idUfCliente);
	}

	public List<AcaoIntegracao> findByTpDocumento (String tpDocumento) {
		return getAcaoIntegracaoDAO().findByTpDocumento(tpDocumento);
	}
	
	/**
	 * Obtem AcaoIntegracao através do filtro em DS_PROCESSO_INTEGRACAO
	 * 
	 * @param dsProcesso
	 * @return
	 */
	public AcaoIntegracao findByProcesso(String dsProcesso){
		return getAcaoIntegracaoDAO().findByProcesso(dsProcesso);
	}
	
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	/**
	 * @param unidadeFederativaService
	 *            the unidadeFederativaService to set
	 */
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
}

}
