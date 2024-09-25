package com.mercurio.lms.financeiro.model.service;

import java.util.Arrays;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.dao.CarregarArquivoRecebidoPreFaturaPadraoDAO;
import com.mercurio.lms.contasreceber.model.service.DescontoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.municipios.model.Filial;

import br.com.tntbrasil.integracao.domains.financeiro.DocumentoPreFatura;

public class PreFatValidationService {

	private static final long FILIAL_INEXISTENTE = -1L;
	private static final long CLIENTE_INEXISTENTE = -1L;

	private static final String EM_FATURA = "F";
	private static final String LIQUIDADO = "L";

	private static final String CONHECIMENTO = "CTR";

	private static final String NOTA_FISCAL = "NFT";
	private static final String CONHECIMENTO_ELETRONICO = "CTE";

	private static final String NOTA_FISCAL_ELETRONICO = "NTE";
	private static final String NOTA_FISCAL_DESCONTO = "NDN";
	private static final String NOTA_FISCAL_SERVICO = "NFS";

	private static final String NOTA_FISCAL_SERV_ELETRONICO = "NSE";

	private static final List<String> TP_SITUACAO_APROVACAO = Arrays.asList(new String[] { "E", "R" });
	private static final String TP_SITUACAO_AUTORIZADO = "A";

	private ConfiguracoesFacade configuracoesFacade;
	private CarregarArquivoRecebidoPreFaturaPadraoDAO carregarArquivoRecebidoPreFaturaPadraoDAO;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private DescontoService descontoService;

	public void validatePrimeiroDocumentoDeServicoPossuiDivisaoDeCobranca(DevedorDocServFat devedorDoc, boolean blPrimeiroDoc) {
		if (blPrimeiroDoc && !hasPrimeiroDocumentoDivisaoDeCobranca(devedorDoc)) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36366"));
		}
	}

	private boolean hasPrimeiroDocumentoDivisaoDeCobranca(DevedorDocServFat devedorDoc) {
		return devedorDoc.getDivisaoCliente() != null;
	}

	public void validateFiliaisDeCobrancaIguais(DevedorDocServFat devedorDoc, Long idFilialAtual) {
		if (!isFilialIgual(devedorDoc, idFilialAtual)) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36364"));
		}
	}

	private boolean isFilialIgual(DevedorDocServFat devedorDoc, Long idFilialAtual) {
		return idFilialAtual == FILIAL_INEXISTENTE || devedorDoc.getFilial().getIdFilial().equals(idFilialAtual);
	}

	public void validateClienteIgual(DevedorDocServFat devedorDoc, Long idClienteAtual) {
		if (!isClienteIgual(devedorDoc, idClienteAtual)) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36365"));
		}
	}

	private boolean isClienteIgual(DevedorDocServFat devedorDoc, Long idClienteAtual) {
		return idClienteAtual == CLIENTE_INEXISTENTE || devedorDoc.getCliente().getIdCliente().equals(idClienteAtual);
	}

	public void validateDoctoServicoNaoLiquidado(DevedorDocServFat devedorDoc, DoctoServico dctoServico) {
		if (isDoctoServicoLiquidado(devedorDoc)) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36385", new Object[] { dctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + dctoServico.getNrDoctoServico().toString() }));
		}
	}

	private boolean isDoctoServicoLiquidado(DevedorDocServFat devedorDoc) {
		return LIQUIDADO.equals(devedorDoc.getTpSituacaoCobranca().getValue());
	}

	public void validateDoctoServicoNaoFaturado(DevedorDocServFat devedorDoc, DoctoServico dctoServico) {
		if (isDoctoServicoFaturado(devedorDoc)) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36386", new Object[] { dctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + dctoServico.getNrDoctoServico().toString() }));
		}
	}

	private boolean isDoctoServicoFaturado(DevedorDocServFat devedorDoc) {
		return EM_FATURA.equals(devedorDoc.getTpSituacaoCobranca().getValue());
	}

	public void validateNotaFiscalAutorizada(DoctoServico dctoServico) {
		if (dctoServico.getTpDocumentoServico().getValue().equals(NOTA_FISCAL_ELETRONICO) || dctoServico.getTpDocumentoServico().getValue().equals(NOTA_FISCAL_SERV_ELETRONICO)) {
			MonitoramentoDocEletronico monitoramento = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(dctoServico.getIdDoctoServico());
			if (monitoramento != null && !monitoramento.getTpSituacaoDocumento().getValue().equals(TP_SITUACAO_AUTORIZADO)) {
				throw new PreFatException(configuracoesFacade.getMensagem("LMS-36282"));
			}
		}
	}

	public void validateNaoTemDescontoInvalido(DoctoServico dctoServico, DevedorDocServFat devedorDoc) {
		if (hasDescontoInvalido(dctoServico, devedorDoc)) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36387", new Object[] { dctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + dctoServico.getNrDoctoServico().toString() }));
		}
	}

	private boolean hasDescontoInvalido(DoctoServico dctoServico, DevedorDocServFat devedorDoc) {
		Desconto desconto = descontoService.findDescontoByIdDevedor(devedorDoc.getIdDevedorDocServFat());
		if (desconto != null && TP_SITUACAO_APROVACAO.contains(desconto.getTpSituacaoAprovacao().getValue())) {
			return true;
		}
		return false;
	}

	public void validateSituacaoDoctoServico(DoctoServico dctoServico) {

		String situacao = findSituacaoDoctoServico(dctoServico.getTpDocumentoServico(), dctoServico.getIdDoctoServico());
		if ("C".equals(situacao)) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36388", new Object[] { dctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + dctoServico.getNrDoctoServico().toString() }));
		}

	}

	private String findSituacaoDoctoServico(DomainValue tpDocumentoServico, Long idDoctoServico) {

		String strTpDoctoServico = tpDocumentoServico.getValue();
		String situacao = null;
		if (CONHECIMENTO.equals(strTpDoctoServico) || NOTA_FISCAL.equals(strTpDoctoServico) || CONHECIMENTO_ELETRONICO.equals(strTpDoctoServico)
				|| NOTA_FISCAL_ELETRONICO.equals(strTpDoctoServico)) {
			situacao = carregarArquivoRecebidoPreFaturaPadraoDAO.findTpSituacaoConhecimento(idDoctoServico);
		} else if (NOTA_FISCAL_DESCONTO.equals(strTpDoctoServico)) {
			situacao = carregarArquivoRecebidoPreFaturaPadraoDAO.findTpSituacaoNotaDebito(idDoctoServico);
		} else if (NOTA_FISCAL_SERVICO.equals(strTpDoctoServico) || NOTA_FISCAL_SERV_ELETRONICO.equals(strTpDoctoServico)) {
			situacao = carregarArquivoRecebidoPreFaturaPadraoDAO.findTpSituacaoNotaServico(idDoctoServico);
		}

		return situacao;
	}

	public void validateExistsDoctoServico(DocumentoPreFatura documento, DoctoServico dctoServico, Filial filial) {
		if (dctoServico == null) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36390",
					new Object[] { "SgFilial: " + filial.getSgFilial(), "NrDocumento: " + documento.getNrDocumento().toString(), "DtEmissao: " + documento.getDtEmissao() }));
		}
	}

	public void validateExistsFilial(DocumentoPreFatura documento, Filial filial) {
		if (filial == null) {
			throw new PreFatException(configuracoesFacade.getMensagem("LMS-36389", new Object[] { documento.getCnpjFilial() }));
		}
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setCarregarArquivoRecebidoPreFaturaPadraoDAO(CarregarArquivoRecebidoPreFaturaPadraoDAO carregarArquivoRecebidoPreFaturaPadraoDAO) {
		this.carregarArquivoRecebidoPreFaturaPadraoDAO = carregarArquivoRecebidoPreFaturaPadraoDAO;
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}
}