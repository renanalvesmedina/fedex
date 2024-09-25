package com.mercurio.lms.entrega.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.dao.GerarReciboReembolsoDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ServAdicionalDocServService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para GerarReciboReembolso:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.gerarReciboReembolsoService"
 */
public class GerarReciboReembolsoService {
	private DoctoServicoService doctoServicoService;
	private ConfiguracoesFacade configuracoesFacade;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private ServAdicionalDocServService servAdicionalDocServService;
	private GerarReciboReembolsoDAO reembolsoDAO;
	private ReciboReembolsoService reciboReembolsoService;
	private EnderecoPessoaService enderecoPessoaService;	

	/**
	 * Geração de Recibo Reembolso.
	 *
	 * @param idManifestoEntrega representa o id do Manifesto de Entrega.
	 * @param idManifestoViagem representa o id do Manifesto de Viagem.
	 * @param idConhecimento representa o id do Conhecimento.	 
	 * @param isReemetir parâmetro para informar se é reemissão ou não.
	 */
	public void generateReciboReembolso(Long idManifestoEntrega, Long idManifestoViagem, Long idConhecimento){
		List doctoServicos = null;

		if (idManifestoEntrega != null) {
			doctoServicos = consultaDoctoServicoByManifestoEntrega(idManifestoEntrega);
		} else if (idManifestoViagem != null) {
			doctoServicos = consultaDoctoServicoByManifestoViagem(idManifestoViagem);
		} else if(idConhecimento != null) {
			doctoServicos = consultaDoctoServicoByConhecimento(idConhecimento);
		} else
			throw new BusinessException("LMS-00010");

		for (Iterator it = doctoServicos.iterator(); it.hasNext();){
			Map documento = (Map)it.next();
			if (idManifestoViagem != null){
				documento.put("idManifestoViagemNacional", idManifestoViagem);
				
			} else if (idManifestoEntrega != null){
				documento.put("idManifestoEntrega", idManifestoEntrega);
			}
			ReciboReembolso reciboReembolso = criaReciboReembolso(documento); 
			reembolsoDAO.store(reciboReembolso);
		}
	}

	/**
	 * Cria uma instancia de ReciboReembolso
	 * @param ds
	 * @return
	 */
	private ReciboReembolso criaReciboReembolso(Map ds){
		ReciboReembolso reciboReembolso = new ReciboReembolso();

		//Dados de DoctoServico
		reciboReembolso.setIdDoctoServico(null);

		Moeda moeda = new Moeda();
		moeda.setIdMoeda((Long) ds.get("idMoeda"));
		reciboReembolso.setMoeda(moeda);

		if (ds.get("idClienteRemetente") != null){
			Cliente cliente = new Cliente();
			cliente.setIdCliente((Long) ds.get("idClienteRemetente"));
			reciboReembolso.setClienteByIdClienteRemetente(cliente);
		}

		Servico servico = new Servico();
		servico.setIdServico((Long) ds.get("idServico"));
		reciboReembolso.setServico(servico);

		Pais pais = new Pais();
		pais.setIdPais((Long) ds.get("idPais"));
		reciboReembolso.setPaisOrigem(pais);

		if (ds.get("idFilial") != null) {
			Filial filialDestino = new Filial();
			filialDestino.setIdFilial((Long) ds.get("idFilial"));
			reciboReembolso.setFilialByIdFilialDestino(filialDestino);

			Filial filialOperacional = new Filial();
			filialOperacional.setIdFilial((Long) ds.get("idFilial"));
			reciboReembolso.setFilialDestinoOperacional(filialOperacional);
		}

		if (ds.get("idClienteDestinatario") != null) {
			Cliente clienteDestino = new Cliente();
			clienteDestino.setIdCliente((Long) ds.get("idClienteDestinatario"));
			reciboReembolso.setClienteByIdClienteDestinatario(clienteDestino);
		}

		if (ds.get("idClienteConsignatario") != null){
			Cliente clienteConsignatario = new Cliente();
			clienteConsignatario.setIdCliente((Long) ds.get("idClienteConsignatario"));
			reciboReembolso.setClienteByIdClienteConsignatario(clienteConsignatario);
		}

		if (ds.get("idClienteRedespacho") != null){
			Cliente clienteRedespacho = new Cliente();
			clienteRedespacho.setIdCliente((Long) ds.get("idClienteRedespacho"));
			reciboReembolso.setClienteByIdClienteRedespacho(clienteRedespacho);
		}

		if (ds.get("idEnderecoPessoa") != null) {
			EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
			enderecoPessoa.setIdEnderecoPessoa((Long) ds.get("idEnderecoPessoa"));
			reciboReembolso.setEnderecoPessoa(enderecoPessoa);

			enderecoPessoa = enderecoPessoaService.findById(enderecoPessoa.getIdEnderecoPessoa());

			ConhecimentoUtils.setEnderecoEntregaReal(reciboReembolso, enderecoPessoa);			
		}

		Filial filialOrigem = new Filial();
		filialOrigem.setIdFilial((Long)ds.get("idFilialOrigem"));
		reciboReembolso.setFilialByIdFilialOrigem(filialOrigem);		

		reciboReembolso.setVlMercadoria((BigDecimal) ds.get("vlMercadoria"));

		reciboReembolso.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());				
		reciboReembolso.setVlTotalDocServico(new BigDecimal(0));
		reciboReembolso.setVlImposto(new BigDecimal(0));
		reciboReembolso.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		reciboReembolso.setTpDocumentoServico(new DomainValue("RRE"));
		reciboReembolso.setBlBloqueado(Boolean.FALSE);

		Long nrDocumento = configuracoesFacade.incrementaParametroSequencial(filialOrigem.getIdFilial(), "NR_RECIBO_REEMBOLSO", true);
		reciboReembolso.setNrDoctoServico(nrDocumento);

		EnderecoPessoa enderecoPessoa = reciboReembolso.getEnderecoPessoa();
		RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(
			(String) ds.get("nrCep"),
			(Long) ds.get("idClienteRemetente"),
			enderecoPessoa != null ? enderecoPessoa.getIdEnderecoPessoa() : null,
				(Long)ds.get("idFilial"), //LMS-1321
			JTDateTimeUtils.getDataAtual()
		);

		// Se a rota não foi encontrada, o recibo reembolso é gerado sem rota coleta entrega associada.
		if (rotaIntervaloCep != null) {
			reciboReembolso.setRotaColetaEntregaByIdRotaColetaEntregaSugerid(rotaIntervaloCep.getRotaColetaEntrega());
			reciboReembolso.setRotaIntervaloCep(rotaIntervaloCep);
		}

		//	Dados de ReciboReembolso
		reciboReembolso.setFilial(filialOrigem);
		reciboReembolso.setNrReciboReembolso(Integer.valueOf(nrDocumento.intValue()));

		DoctoServico doctoServico = new DoctoServico();
		doctoServico.setIdDoctoServico((Long) ds.get("idDoctoServico"));
		reciboReembolso.setDoctoServicoByIdDoctoServReembolsado(doctoServico);

		if (ds.get("idManifestoEntrega") != null){
			ManifestoEntrega manifestoEntrega = new ManifestoEntrega();
			manifestoEntrega.setIdManifestoEntrega((Long) ds.get("idManifestoEntrega"));
			reciboReembolso.setManifestoEntrega(manifestoEntrega);
		}

		if (ds.get("idManifestoViagemNacional") != null){
			ManifestoViagemNacional manifestoViagemNacional = new ManifestoViagemNacional();
			manifestoViagemNacional.setIdManifestoViagemNacional((Long) ds.get("idManifestoViagemNacional"));
			reciboReembolso.setManifestoViagemNacional(manifestoViagemNacional);
		}

		reciboReembolso.setTpSituacaoRecibo(new DomainValue("GE"));

		BigDecimal vlMercadoria = servAdicionalDocServService.findVlMercadoriaReembolsoByDoctoServico((Long) ds.get("idDoctoServico"));
		if (vlMercadoria == null)
			throw new BusinessException("LMS-09036");

		reciboReembolso.setVlReembolso(vlMercadoria);

		return reciboReembolso;
	}

	public void updateDoctoServico(Long idManifestoEntrega, Long idManifestoViagem, Long idConhecimento){
		List result = reciboReembolsoService.findRecibosNaoEmitidosByManifesto(idManifestoEntrega, idManifestoViagem, idConhecimento);
		for (Iterator it = result.iterator(); it.hasNext();){
			ReciboReembolso rr = (ReciboReembolso) it.next();
			rr.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
			rr.setTpSituacaoRecibo(new DomainValue("EM"));
			reciboReembolsoService.store(rr);
		}
	}

	/**
	 * Consulta os documentos sem recibo de reembolso associados ao conhecimento
	 * @param idConhecimento
	 * @return
	 */
	private List consultaDoctoServicoByConhecimento(Long idConhecimento) {
		return doctoServicoService.findDocumentosByConhecimento(idConhecimento);
	}

	/**
	 * Consulta os documentos sem recibo de reembolso associados ao manifesto de entrega
	 * @param idManifestoEntrega
	 * @return
	 */
	private List consultaDoctoServicoByManifestoEntrega(Long idManifestoEntrega) {
		return doctoServicoService.findDocumentosByManifestoEntrega(idManifestoEntrega);
	}

	/**
	 * Consulta os documentos sem recibo de reembolso associados ao manifesto de viagem nacional
	 * @param idManifestoViagem
	 * @return
	 */
	private List consultaDoctoServicoByManifestoViagem(Long idManifestoViagem) {
		return doctoServicoService.findDocumentosByManifestoViagemNacional(idManifestoViagem);
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	/**
	 * @param doctoServicoService The doctoServicoService to set.
	 */
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	/**
	 * @param rotaColetaEntregaService The rotaColetaEntregaService to set.
	 */
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	/**
	 * @param servAdicionalDocServService The servAdicionalDocServService to set.
	 */
	public void setServAdicionalDocServService(ServAdicionalDocServService servAdicionalDocServService) {
		this.servAdicionalDocServService = servAdicionalDocServService;
	}
	/**
	 * @param reembolsoDAO The reembolsoDAO to set.
	 */
	public void setReembolsoDAO(GerarReciboReembolsoDAO reembolsoDAO) {
		this.reembolsoDAO = reembolsoDAO;
	}
	/**
	 * @param reciboReembolsoService The reciboReembolsoService to set.
	 */
	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}

}
