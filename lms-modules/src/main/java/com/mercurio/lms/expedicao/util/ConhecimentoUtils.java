package com.mercurio.lms.expedicao.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.DoctoServicoSeguros;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.LiberacaoDocServ;
import com.mercurio.lms.expedicao.model.NfDadosComp;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.TipoCusto;
import com.mercurio.lms.expedicao.model.ValorCusto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

public abstract class ConhecimentoUtils {
	
	private static String[] CONSTANTES_LIBERACAO_PESO = new String[]{ConstantesExpedicao.LIBERACAO_PESO_LIMITE, ConstantesExpedicao.LIBERACAO_DIFERENCA_PESO};
	
	public static Conhecimento getConhecimentoInSession() {
		Conhecimento conhecimento = (Conhecimento) SessionContext.get(ConstantesExpedicao.CONHECIMENTO_IN_SESSION);
		if(conhecimento == null)
			throw new BusinessException("LMS-04124");
		return conhecimento;
	}

	public static void setConhecimentoInSession(Conhecimento conhecimento) {
		ExpedicaoUtils.setTpDocumentoInSession(conhecimento.getTpDocumentoServico().getValue());
		SessionContext.set(ConstantesExpedicao.CONHECIMENTO_IN_SESSION, conhecimento);
	}

	public static Conhecimento getConhecimentoPersistentInSession() {
		Conhecimento conhecimento = (Conhecimento) SessionContext.get(ConstantesExpedicao.CONHECIMENTO_PERSISTENT_IN_SESSION);
		if(conhecimento == null)
			throw new BusinessException("LMS-04124");
		return conhecimento;
	}

	public static void setConhecimentoPersistentInSession(Conhecimento conhecimento) {
		SessionContext.set(ConstantesExpedicao.CONHECIMENTO_PERSISTENT_IN_SESSION, conhecimento);
	}


	/**
	 * Formata número do Conhecimento sem dígito verificador.
	 * @param sgFilial
	 * @param nrConhecimento
	 * @return
	 */
	public static String formatConhecimento(String sgFilial, Long nrConhecimento) {
		return formatConhecimento(sgFilial, nrConhecimento, IntegerUtils.getInteger(8).intValue());
	}

	/**
	 * Formata número do Conhecimento sem dígito verificador.
	 * @param sgFilial
	 * @param nrConhecimento
	 * @param size
	 * @return
	 */
	public static String formatConhecimento(String sgFilial, Long nrConhecimento, int size) {
		StringBuilder builder = new StringBuilder();
		builder.append(sgFilial);
		builder.append(" ");
		builder.append(FormatUtils.fillNumberWithZero(nrConhecimento.toString(), size));
		return builder.toString();
	}
	
	public static String formatConhecimento(String sgFilial, Long nrConhecimento, Integer dvConhecimento) {
		return formatConhecimento(sgFilial, nrConhecimento, dvConhecimento, 8);
	}

	public static String formatConhecimento(String sgFilial, Long nrConhecimento, Integer dvConhecimento, int size) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(sgFilial);
		buffer.append(" ");
		buffer.append(FormatUtils.fillNumberWithZero(nrConhecimento.toString(), size));
		//Validação para nao mostrar "null" quando não se tem um dígito verificador.
		if (dvConhecimento != null) {
			buffer.append("-");
			buffer.append(dvConhecimento);
		}
		return buffer.toString();
	}

	/**
	 * Formata número do Conhecimento Internacional
	 * @author Regis Novais
	 * @param sgPais
	 * @param nrPermisso
	 * @param nrCrt
	 * @return
	 */
	public static String formatConhecimentoInternacional(String sgPais, Integer nrPermisso, Long nrCrt) {
		StringBuilder builder = new StringBuilder();

		builder.append(sgPais).append(".");
		if(CompareUtils.lt(nrPermisso, IntegerUtils.getInteger(1000))) {
			builder.append(FormatUtils.formatDecimal("000", nrPermisso)).append(".");
			builder.append(FormatUtils.formatDecimal("000000", nrCrt));
		} else {
			builder.append(FormatUtils.formatDecimal("0000", nrPermisso)).append(".");
			builder.append(FormatUtils.formatDecimal("00000", nrCrt));
		}

		return builder.toString();
	}

	public static String formatNotaFiscalServico(Long nrNotaFiscalServico) {
		return FormatUtils.formatDecimal("00000000", nrNotaFiscalServico);
	}
	
	public static String formatDoctoServico(String tpDoctoServico, String sgFilial, Long nrConhecimento, Integer dvConhecimento) {
		return formatDoctoServico(tpDoctoServico, sgFilial, nrConhecimento, dvConhecimento, 8);
	}

	public static String formatDoctoServico(String tpDoctoServico, String sgFilial, Long nrConhecimento, Integer dvConhecimento, int size) {
		if(ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpDoctoServico) 
				|| ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDoctoServico)) {
			return "C "+ formatConhecimento(sgFilial, nrConhecimento, dvConhecimento, size); 
		} else if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDoctoServico)) {
			return "N "+ formatConhecimento(sgFilial, nrConhecimento, dvConhecimento, size);
		}
		return null;
	}

	public static Integer getDigitoVerificador(Long nrConhecimento) {
		nrConhecimento = nrConhecimento % 1000000; //Adicionado para que o dvConhecimento tenha compatibilidade com o SOM
		int dvConhecimento = nrConhecimento.intValue() % 11;
		if(dvConhecimento == 10) {
			dvConhecimento = 0;
		}
		return Integer.valueOf(dvConhecimento);
	}

	public static boolean validaDigitoVerificador(Long nrConhecimento, Integer dvConhecimento) {
		Integer dv = getDigitoVerificador(nrConhecimento);
		return dvConhecimento.equals(dv);
	}

	/**
	 * Cria um novo Conhecimento copiando valores do Conhecimento Original.
	 * Não realiza a copia das collections.
	 * @param conhecimentoOriginal
	 * @return
	 */
	public static Conhecimento createCopy(Conhecimento conhecimentoOriginal) {
		Conhecimento conhecimentoNovo = new Conhecimento();

		conhecimentoNovo.setNrCepEntrega(conhecimentoOriginal.getNrCepEntrega());
		conhecimentoNovo.setNrCepColeta(conhecimentoOriginal.getNrCepColeta());
		conhecimentoNovo.setDsEspecieVolume(conhecimentoOriginal.getDsEspecieVolume());
		conhecimentoNovo.setTpFrete(conhecimentoOriginal.getTpFrete());
		conhecimentoNovo.setTpConhecimento(conhecimentoOriginal.getTpConhecimento());
		conhecimentoNovo.setBlIndicadorEdi(conhecimentoOriginal.getBlIndicadorEdi());
		conhecimentoNovo.setBlIndicadorFretePercentual(conhecimentoOriginal.getBlIndicadorFretePercentual());
		conhecimentoNovo.setTpSituacaoConhecimento(conhecimentoOriginal.getTpSituacaoConhecimento());
		conhecimentoNovo.setBlPermiteTransferencia(conhecimentoOriginal.getBlPermiteTransferencia());
		conhecimentoNovo.setBlReembolso(conhecimentoOriginal.getBlReembolso());
		conhecimentoNovo.setTpDevedorFrete(conhecimentoOriginal.getTpDevedorFrete());
		conhecimentoNovo.setBlSeguroRr(conhecimentoOriginal.getBlSeguroRr());
		conhecimentoNovo.setDsEnderecoEntrega(conhecimentoOriginal.getDsEnderecoEntrega());
		conhecimentoNovo.setNrEntrega(conhecimentoOriginal.getNrEntrega());
		conhecimentoNovo.setDtAutDevMerc(conhecimentoNovo.getDtAutDevMerc());
		conhecimentoNovo.setDsCodigoColeta(conhecimentoOriginal.getDsCodigoColeta());
		conhecimentoNovo.setDsRespAutDevMerc(conhecimentoOriginal.getDsRespAutDevMerc());
		conhecimentoNovo.setDsCtoRedespacho(conhecimentoOriginal.getDsCtoRedespacho());
		conhecimentoNovo.setDsCalculadoAte(conhecimentoOriginal.getDsCalculadoAte());
		conhecimentoNovo.setDsComplementoEntrega(conhecimentoOriginal.getDsComplementoEntrega());
		conhecimentoNovo.setDsBairroEntrega(conhecimentoOriginal.getDsBairroEntrega());
		conhecimentoNovo.setTpDoctoServico(conhecimentoOriginal.getTpDoctoServico());
		conhecimentoNovo.setTpCtrcParceria(conhecimentoOriginal.getTpCtrcParceria());
		conhecimentoNovo.setDsLocalEntrega(conhecimentoOriginal.getDsLocalEntrega());
		conhecimentoNovo.setBlColetaEmergencia(conhecimentoOriginal.getBlColetaEmergencia());
		conhecimentoNovo.setBlEntregaEmergencia(conhecimentoOriginal.getBlEntregaEmergencia());
		conhecimentoNovo.setNrCtrcSubcontratante(conhecimentoOriginal.getNrCtrcSubcontratante());
		conhecimentoNovo.setTipoTributacaoIcms(conhecimentoOriginal.getTipoTributacaoIcms());
		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimentoOriginal.getMunicipioByIdMunicipioEntrega());
		conhecimentoNovo.setMunicipioByIdMunicipioColeta(conhecimentoOriginal.getMunicipioByIdMunicipioColeta());
		conhecimentoNovo.setNaturezaProduto(conhecimentoOriginal.getNaturezaProduto());
		conhecimentoNovo.setProduto(conhecimentoOriginal.getProduto());
		conhecimentoNovo.setDensidade(conhecimentoOriginal.getDensidade());
		conhecimentoNovo.setFilialByIdFilialDeposito(conhecimentoOriginal.getFilialByIdFilialDeposito());
		conhecimentoNovo.setAeroportoByIdAeroportoOrigem(conhecimentoOriginal.getAeroportoByIdAeroportoOrigem());
		conhecimentoNovo.setAeroportoByIdAeroportoDestino(conhecimentoOriginal.getAeroportoByIdAeroportoDestino());
		conhecimentoNovo.setProdutoEspecifico(conhecimentoOriginal.getProdutoEspecifico());
		conhecimentoNovo.setVlTotalServicos(conhecimentoOriginal.getVlTotalServicos());
		conhecimentoNovo.setVlTotalParcelas(conhecimentoOriginal.getVlTotalParcelas());
		conhecimentoNovo.setVlTotalDocServico(conhecimentoOriginal.getVlTotalDocServico());
		conhecimentoNovo.setVlImposto(conhecimentoOriginal.getVlImposto());
		conhecimentoNovo.setVlDesconto(conhecimentoOriginal.getVlDesconto());
		conhecimentoNovo.setVlLiquido(conhecimentoOriginal.getVlLiquido());
		conhecimentoNovo.setBlParcelas(conhecimentoOriginal.getBlParcelas());
		conhecimentoNovo.setBlServicosAdicionais(conhecimentoOriginal.getBlServicosAdicionais());
		conhecimentoNovo.setVlMercadoria(conhecimentoOriginal.getVlMercadoria());
		conhecimentoNovo.setTpDocumentoServico(conhecimentoOriginal.getTpDocumentoServico());
		conhecimentoNovo.setBlBloqueado(conhecimentoOriginal.getBlBloqueado());
		conhecimentoNovo.setVlBaseCalcImposto(conhecimentoOriginal.getVlBaseCalcImposto());
		conhecimentoNovo.setVlIcmsSubstituicaoTributaria(conhecimentoOriginal.getVlIcmsSubstituicaoTributaria());
		conhecimentoNovo.setBlIncideIcmsPedagio(conhecimentoOriginal.getBlIncideIcmsPedagio());
		conhecimentoNovo.setNrCfop(conhecimentoOriginal.getNrCfop());
		conhecimentoNovo.setPsReferenciaCalculo(conhecimentoOriginal.getPsReferenciaCalculo());
		conhecimentoNovo.setDhEntradaSetorEntrega(conhecimentoOriginal.getDhEntradaSetorEntrega());
		conhecimentoNovo.setTpCalculoPreco(conhecimentoOriginal.getTpCalculoPreco());
		conhecimentoNovo.setBlPrioridadeCarregamento(conhecimentoOriginal.getBlPrioridadeCarregamento());
		conhecimentoNovo.setPcAliquotaIcms(conhecimentoOriginal.getPcAliquotaIcms());
		conhecimentoNovo.setPsReal(conhecimentoOriginal.getPsReal());
		conhecimentoNovo.setPsAforado(conhecimentoOriginal.getPsAforado());
		conhecimentoNovo.setQtVolumes(conhecimentoOriginal.getQtVolumes());
		conhecimentoNovo.setDsEnderecoEntregaReal(conhecimentoOriginal.getDsEnderecoEntregaReal());
		conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimentoOriginal.getClienteByIdClienteDestinatario());
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimentoOriginal.getClienteByIdClienteConsignatario());
		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimentoOriginal.getClienteByIdClienteRemetente());
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimentoOriginal.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setEnderecoPessoa(conhecimentoOriginal.getEnderecoPessoa());
		conhecimentoNovo.setFluxoFilial(conhecimentoOriginal.getFluxoFilial());
		conhecimentoNovo.setFilialDestinoOperacional(conhecimentoOriginal.getFilialByIdFilialDestino());
		conhecimentoNovo.setInscricaoEstadualRemetente(conhecimentoOriginal.getInscricaoEstadualRemetente());
		conhecimentoNovo.setInscricaoEstadualDestinatario(conhecimentoOriginal.getInscricaoEstadualDestinatario());
		conhecimentoNovo.setTabelaPreco(conhecimentoOriginal.getTabelaPreco());
		conhecimentoNovo.setMoeda(conhecimentoOriginal.getMoeda());
		conhecimentoNovo.setServico(conhecimentoOriginal.getServico());
		conhecimentoNovo.setDivisaoCliente(conhecimentoOriginal.getDivisaoCliente());
		conhecimentoNovo.setRotaColetaEntregaByIdRotaColetaEntregaSugerid(conhecimentoOriginal.getRotaColetaEntregaByIdRotaColetaEntregaSugerid());
		conhecimentoNovo.setPedidoColeta(conhecimentoOriginal.getPedidoColeta());
		conhecimentoNovo.setFilialByIdFilialDestino(conhecimentoOriginal.getFilialByIdFilialDestino());
		conhecimentoNovo.setFilialByIdFilialOrigem(conhecimentoOriginal.getFilialByIdFilialOrigem());
		conhecimentoNovo.setPaisOrigem(conhecimentoOriginal.getPaisOrigem());
		conhecimentoNovo.setParametroCliente(conhecimentoOriginal.getParametroCliente());
		conhecimentoNovo.setTarifaPreco(conhecimentoOriginal.getTarifaPreco());
		conhecimentoNovo.setDoctoServicoOriginal(conhecimentoOriginal);

		return conhecimentoNovo;
	}

	//Devedores
	public static void copyDevedoresDoctoServico(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo, InscricaoEstadual inscricaoEstadual) {
		Filial filialCobranca = null;
		if (ConstantesExpedicao.TP_FRETE_CIF.equals(conhecimentoNovo.getTpFrete().getValue()) 
		|| (ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimentoNovo.getTpFrete().getValue()) && conhecimentoNovo.getClienteByIdClienteConsignatario() != null)){
			filialCobranca = conhecimentoNovo.getFilialByIdFilialOrigem();
		} else {
			filialCobranca = conhecimentoNovo.getFilialByIdFilialDestino().getFilialByIdFilialResponsavel();
		}
		List<DevedorDocServ> origem = conhecimentoOriginal.getDevedorDocServs();
		List<DevedorDocServ> destino = new ArrayList<DevedorDocServ>(origem.size());
		for(DevedorDocServ devedorDocServ : origem) {
			destino.add(createDevedorDoctoServico(conhecimentoNovo, devedorDocServ.getCliente(), filialCobranca, inscricaoEstadual));
		}
		conhecimentoNovo.setDevedorDocServs(destino);
	}

	public static void copyDevedoresDoctoServico(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		copyDevedoresDoctoServico(conhecimentoOriginal, conhecimentoNovo, null);
	}
	
	public static void copyLiberacaoDocServs(Conhecimento conhecimentoCalculo, Conhecimento conhecimentoNovo) {
		List<LiberacaoDocServ> liberacaoDocServs = conhecimentoCalculo.getLiberacaoDocServs();
		for(LiberacaoDocServ liberacaoDocServ : liberacaoDocServs) {
			liberacaoDocServ.setDoctoServico(conhecimentoNovo);
		}
		conhecimentoNovo.setLiberacaoDocServs(liberacaoDocServs);
	}

	public static void copyDevedoresDoctoServicoFromFatura(Fatura fatura, Conhecimento conhecimentoNovo) {
		List<DevedorDocServ> destino = new ArrayList<DevedorDocServ>(1);
		destino.add(createDevedorDoctoServico(conhecimentoNovo, fatura.getCliente(), fatura.getFilialByIdFilial(), null));
		conhecimentoNovo.setDevedorDocServs(destino);
	}

	private static DevedorDocServ createDevedorDoctoServico(DoctoServico doctoServico, Cliente cliente, Filial filial, InscricaoEstadual inscricaoEstadual) {
		DevedorDocServ devedorDoctoServico = new DevedorDocServ();
		devedorDoctoServico.setCliente(cliente);
		devedorDoctoServico.setInscricaoEstadual(inscricaoEstadual);
		devedorDoctoServico.setDoctoServico(doctoServico);
		devedorDoctoServico.setFilial(filial);
		devedorDoctoServico.setVlDevido(doctoServico.getVlLiquido());
		return devedorDoctoServico;
	}

	//Devedores Faturamento
	public static void copyDevedoresDoctoServicoFaturamento(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo, DomainValue tpSituacaoCobranca) {
		Filial filialCobranca = null;
		if (ConstantesExpedicao.TP_FRETE_CIF.equals(conhecimentoNovo.getTpFrete().getValue()) 
		|| (ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimentoNovo.getTpFrete().getValue()) && conhecimentoNovo.getClienteByIdClienteConsignatario() != null)){
			filialCobranca = conhecimentoNovo.getFilialByIdFilialOrigem();
		} else {
			filialCobranca = conhecimentoNovo.getFilialByIdFilialDestino().getFilialByIdFilialResponsavel();
		}
		List<DevedorDocServFat> origem = conhecimentoOriginal.getDevedorDocServFats();
		List<DevedorDocServFat> destino = new ArrayList<DevedorDocServFat>(origem.size());
		for(DevedorDocServFat devedorDocServFat : origem) {
			DevedorDocServFat devedorDoctoServicoFaturamentoCreated = createDevedorDoctoServicoFaturamento(conhecimentoNovo, devedorDocServFat.getCliente(), filialCobranca, tpSituacaoCobranca);
			if (conhecimentoOriginal.isGeraReceita()) {
				devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
				devedorDocServFat.setDtLiquidacao(JTDateTimeUtils.getDataAtual());
		}
			destino.add(devedorDoctoServicoFaturamentoCreated);
		}
		conhecimentoNovo.setDevedorDocServFats(destino);
	}

	public static void copyDevedoresDoctoServicoFaturamentoFromFatura(Fatura fatura, Conhecimento conhecimentoNovo) {
		List<DevedorDocServFat> destino = new ArrayList<DevedorDocServFat>(1);
		DomainValue tpSituacaoCobranca = new DomainValue("P");
		destino.add(createDevedorDoctoServicoFaturamento(conhecimentoNovo, fatura.getCliente(), fatura.getFilialByIdFilial(), tpSituacaoCobranca));
		conhecimentoNovo.setDevedorDocServFats(destino);
	}

	public static DevedorDocServFat createDevedorDoctoServicoFaturamento(DoctoServico doctoServico, Cliente cliente, Filial filial, DomainValue tpSituacaoCobranca) {
		DevedorDocServFat devedorDoctoServicoFaturamento = new DevedorDocServFat();
		devedorDoctoServicoFaturamento.setCliente(cliente);
		devedorDoctoServicoFaturamento.setDoctoServico(doctoServico);
		devedorDoctoServicoFaturamento.setFilial(filial);
		devedorDoctoServicoFaturamento.setVlDevido(doctoServico.getVlLiquido());
		devedorDoctoServicoFaturamento.setTpSituacaoCobranca(tpSituacaoCobranca);
		devedorDoctoServicoFaturamento.setDivisaoCliente(doctoServico.getDivisaoCliente());
		return devedorDoctoServicoFaturamento;
	}

	//Parcelas
	public static void copyParcelasDoctoServico(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		List<ParcelaDoctoServico> origem = conhecimentoOriginal.getParcelaDoctoServicos();
		List<ParcelaDoctoServico> destino = new ArrayList<ParcelaDoctoServico>(origem.size());
		for(ParcelaDoctoServico parcelaDoctoServico : origem) {
			ParcelaDoctoServico parcelaDoctoServicoNovo = new ParcelaDoctoServico();
			parcelaDoctoServicoNovo.setDoctoServico(conhecimentoNovo);
			parcelaDoctoServicoNovo.setParcelaPreco(parcelaDoctoServico.getParcelaPreco());
			parcelaDoctoServicoNovo.setVlParcela(parcelaDoctoServico.getVlParcela());
			parcelaDoctoServicoNovo.setVlBrutoParcela(parcelaDoctoServico.getVlBrutoParcela());
			destino.add(parcelaDoctoServicoNovo);
		}
		conhecimentoNovo.setParcelaDoctoServicos(destino);
	}

	public static void copyServicosAdicionaisDoctoServico(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		List<ServAdicionalDocServ> origem = conhecimentoOriginal.getServAdicionalDocServs();
		List<ServAdicionalDocServ> destino = new ArrayList<ServAdicionalDocServ>();
		if(origem != null) {
			for (ServAdicionalDocServ servicoAdicionalDoctoServico : origem) {
				ServAdicionalDocServ servicoAdicionalDoctoServicoNovo = new ServAdicionalDocServ();
				servicoAdicionalDoctoServicoNovo.setDtPrimeiroCheque(servicoAdicionalDoctoServico.getDtPrimeiroCheque());
				servicoAdicionalDoctoServicoNovo.setNrKmRodado(servicoAdicionalDoctoServico.getNrKmRodado());
				servicoAdicionalDoctoServicoNovo.setQtCheques(servicoAdicionalDoctoServico.getQtCheques());
				servicoAdicionalDoctoServicoNovo.setQtColetas(servicoAdicionalDoctoServico.getQtColetas());
				servicoAdicionalDoctoServicoNovo.setQtDias(servicoAdicionalDoctoServico.getQtDias());
				servicoAdicionalDoctoServicoNovo.setQtPaletes(servicoAdicionalDoctoServico.getQtPaletes());
				servicoAdicionalDoctoServicoNovo.setQtSegurancasAdicionais(servicoAdicionalDoctoServico.getQtSegurancasAdicionais());
				servicoAdicionalDoctoServicoNovo.setServicoAdicional(servicoAdicionalDoctoServico.getServicoAdicional());
				servicoAdicionalDoctoServicoNovo.setDoctoServico(conhecimentoNovo);
				destino.add(servicoAdicionalDoctoServicoNovo);
			}
		}
		conhecimentoNovo.setServAdicionalDocServs(destino);
	}

	public static void copyDimensoes(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		List<Dimensao> origem = conhecimentoOriginal.getDimensoes();
		List<Dimensao> destino = new ArrayList<Dimensao>();
		if(origem != null) {
			for (Dimensao dimensao : origem) {
				Dimensao dimensaoNova = new Dimensao();
				dimensaoNova.setNrAltura(dimensao.getNrAltura());
				dimensaoNova.setNrComprimento(dimensao.getNrComprimento());
				dimensaoNova.setNrLargura(dimensao.getNrLargura());
				dimensaoNova.setNrQuantidade(dimensao.getNrQuantidade());
				dimensaoNova.setConhecimento(conhecimentoNovo);
				destino.add(dimensaoNova);
			}
		}
		conhecimentoNovo.setDimensoes(destino);
	}

	public static void copyObservacoesDoctoServico(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo, String dsObservacaoGeral) {
		List<ObservacaoDoctoServico> origem = conhecimentoOriginal.getObservacaoDoctoServicos();
		List<ObservacaoDoctoServico> destino = new ArrayList<ObservacaoDoctoServico>();
		if(origem != null) {
			for(ObservacaoDoctoServico observacaoDoctoServico : origem) {
				destino.add(createObservacaoDocumentoServico(conhecimentoNovo, observacaoDoctoServico.getDsObservacaoDoctoServico(), Boolean.FALSE));
			}
		}
		//Observacao Geral
		if(StringUtils.isNotBlank(dsObservacaoGeral)) {
			destino.add(createObservacaoDocumentoServico(conhecimentoNovo, dsObservacaoGeral, Boolean.TRUE));
			conhecimentoNovo.setObservacaoDoctoServicos(destino);
		}
	}

	public static void copyDadosComplemento(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		List<DadosComplemento> origem = conhecimentoOriginal.getDadosComplementos();
		List<DadosComplemento> destino = new ArrayList<DadosComplemento>(origem.size());
		for(DadosComplemento dadosComplemento : origem) {
			DadosComplemento dadosComplementoNovo = new DadosComplemento();
			dadosComplementoNovo.setInformacaoDocServico(dadosComplemento.getInformacaoDocServico());
			dadosComplementoNovo.setInformacaoDoctoCliente(dadosComplemento.getInformacaoDoctoCliente());
			dadosComplementoNovo.setConhecimento(conhecimentoNovo);
			dadosComplementoNovo.setDsValorCampo(dadosComplemento.getDsValorCampo());
			//Tabela NF_DADOS_COMP (Deverá ser gerado um registro para cada registro existente para o CTRC original na tabela NF_DADOS_COMP)
			List<NfDadosComp> origemNfDadosComp = dadosComplemento.getNfDadosComps();
			List<NfDadosComp> destinoNfDadosComp = new ArrayList<NfDadosComp>(origemNfDadosComp.size());
			for(NfDadosComp nfDadosComp : origemNfDadosComp) {
				NfDadosComp nfDadosCompNovo = new NfDadosComp();
				nfDadosCompNovo.setDadosComplemento(dadosComplementoNovo);
				//Copia a Nota Fiscal para poder comparar e substituir no processo de copia das Notas Fiscais
				nfDadosCompNovo.setNotaFiscalConhecimento(nfDadosComp.getNotaFiscalConhecimento());
				destinoNfDadosComp.add(nfDadosCompNovo);
			}
			dadosComplementoNovo.setNfDadosComps(destinoNfDadosComp);
			destino.add(dadosComplementoNovo);
		}
		conhecimentoNovo.setDadosComplementos(destino);
	}

	//deve ser utilizado o que recebe dados das grids das telas. Ainda não pode ser apagado pois não foi feito para a tela de complemento
	@Deprecated
	public static void copyNotasFiscaisConhecimento(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		List<NotaFiscalConhecimento> origem = conhecimentoOriginal.getNotaFiscalConhecimentos();
		List<NotaFiscalConhecimento> destino = new ArrayList<NotaFiscalConhecimento>(origem.size());
		for(NotaFiscalConhecimento notaFiscalConhecimento : origem) {
			NotaFiscalConhecimento notaFiscalConhecimentoNovo = new NotaFiscalConhecimento();
			notaFiscalConhecimentoNovo.setConhecimento(conhecimentoNovo);
			notaFiscalConhecimentoNovo.setNrNotaFiscal(notaFiscalConhecimento.getNrNotaFiscal());
			notaFiscalConhecimentoNovo.setVlTotal(notaFiscalConhecimento.getVlTotal());
			notaFiscalConhecimentoNovo.setQtVolumes(notaFiscalConhecimento.getQtVolumes());
			notaFiscalConhecimentoNovo.setPsMercadoria(notaFiscalConhecimento.getPsMercadoria());
			notaFiscalConhecimentoNovo.setDtEmissao(notaFiscalConhecimento.getDtEmissao());
			if(conhecimentoNovo.getNrCfop() != null) {
				notaFiscalConhecimentoNovo.setNrCfop(new BigInteger(String.valueOf(conhecimentoNovo.getNrCfop())));
			}
			notaFiscalConhecimentoNovo.setVlBaseCalculo(notaFiscalConhecimento.getVlBaseCalculo());
			notaFiscalConhecimentoNovo.setVlIcms(notaFiscalConhecimento.getVlIcms());
			notaFiscalConhecimentoNovo.setVlBaseCalculoSt(notaFiscalConhecimento.getVlBaseCalculoSt());
			notaFiscalConhecimentoNovo.setVlIcmsSt(notaFiscalConhecimento.getVlIcmsSt());
			notaFiscalConhecimentoNovo.setDsSerie(notaFiscalConhecimento.getDsSerie());
			notaFiscalConhecimentoNovo.setNrChave(notaFiscalConhecimento.getNrChave());
			notaFiscalConhecimentoNovo.setCliente(notaFiscalConhecimento.getCliente());
			notaFiscalConhecimentoNovo.setPsCubado(notaFiscalConhecimento.getPsCubado());
			notaFiscalConhecimentoNovo.setVlTotalProdutos(notaFiscalConhecimento.getVlTotalProdutos());
			notaFiscalConhecimentoNovo.setNrPinSuframa(notaFiscalConhecimento.getNrPinSuframa());
			notaFiscalConhecimentoNovo.setTpDocumento(notaFiscalConhecimento.getTpDocumento());
			
			
			//Tabela NF_DADOS_COMP (Deverá ser gerado um registro para cada registro existente para o CTRC original na tabela NF_DADOS_COMP)
			if(conhecimentoNovo.getDadosComplementos() != null) {
				for(DadosComplemento dadosComplemento : conhecimentoNovo.getDadosComplementos()) {
					if(dadosComplemento.getNfDadosComps() != null) {
						for(NfDadosComp nfDadosComp : dadosComplemento.getNfDadosComps()) {
							final Long idNotaFiscalConhecimento = nfDadosComp.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento();
							if(idNotaFiscalConhecimento != null) {
								if(CompareUtils.eq(notaFiscalConhecimento.getIdNotaFiscalConhecimento(), idNotaFiscalConhecimento)) {
								nfDadosComp.setNotaFiscalConhecimento(notaFiscalConhecimentoNovo);
							}
						}
					}
				}
			}
			}
			destino.add(notaFiscalConhecimentoNovo);
		}
		conhecimentoNovo.setNotaFiscalConhecimentos(destino);
	}

	public static void copyNotasFiscaisConhecimento(final Conhecimento conhecimentoOriginal, final Conhecimento conhecimentoNovo, final List<NotaFiscalConhecimento> notasFiscaisConhecimento, boolean isEntregaParcial) {
		List<NotaFiscalConhecimento> origem = conhecimentoOriginal.getNotaFiscalConhecimentos();
		List<NotaFiscalConhecimento> destino = new ArrayList<NotaFiscalConhecimento>(origem.size());
	
		for(NotaFiscalConhecimento notaFiscalConhecimento : origem) {
			NotaFiscalConhecimento notaFiscalConhecimentoNovo = new NotaFiscalConhecimento();
			boolean isNfSelecionada = false;
			
			notaFiscalConhecimentoNovo.setConhecimento(conhecimentoNovo);
			notaFiscalConhecimentoNovo.setNrNotaFiscal(notaFiscalConhecimento.getNrNotaFiscal());
			notaFiscalConhecimentoNovo.setVlTotal(notaFiscalConhecimento.getVlTotal());
			notaFiscalConhecimentoNovo.setQtVolumes(notaFiscalConhecimento.getQtVolumes());
			notaFiscalConhecimentoNovo.setPsMercadoria(notaFiscalConhecimento.getPsMercadoria());
			notaFiscalConhecimentoNovo.setDtEmissao(notaFiscalConhecimento.getDtEmissao());
			//LMS-1552: conforme falado com Eri, utilizar da nota e não do conhecimento
			notaFiscalConhecimentoNovo.setNrCfop(notaFiscalConhecimento.getNrCfop());
			notaFiscalConhecimentoNovo.setVlBaseCalculo(notaFiscalConhecimento.getVlBaseCalculo());
			notaFiscalConhecimentoNovo.setVlIcms(notaFiscalConhecimento.getVlIcms());
			notaFiscalConhecimentoNovo.setVlBaseCalculoSt(notaFiscalConhecimento.getVlBaseCalculoSt());
			notaFiscalConhecimentoNovo.setVlIcmsSt(notaFiscalConhecimento.getVlIcmsSt());
			notaFiscalConhecimentoNovo.setDsSerie(notaFiscalConhecimento.getDsSerie());
			notaFiscalConhecimentoNovo.setNrChave(notaFiscalConhecimento.getNrChave());
			notaFiscalConhecimentoNovo.setCliente(notaFiscalConhecimento.getCliente());
			notaFiscalConhecimentoNovo.setPsCubado(notaFiscalConhecimento.getPsCubado());
			notaFiscalConhecimentoNovo.setVlTotalProdutos(notaFiscalConhecimento.getVlTotalProdutos());
			notaFiscalConhecimentoNovo.setNrPinSuframa(notaFiscalConhecimento.getNrPinSuframa());
			notaFiscalConhecimentoNovo.setTpDocumento(notaFiscalConhecimento.getTpDocumento());
			
			if(notasFiscaisConhecimento != null && !notasFiscaisConhecimento.isEmpty()){
				for (NotaFiscalConhecimento notaFiscalConhecimentoTela : notasFiscaisConhecimento) {
					if(notaFiscalConhecimentoTela.getIdNotaFiscalConhecimento().equals(notaFiscalConhecimento.getIdNotaFiscalConhecimento())){
						notaFiscalConhecimentoNovo.setNrChave(notaFiscalConhecimentoTela.getNrChave());
						notaFiscalConhecimentoNovo.setNrCfop(notaFiscalConhecimentoTela.getNrCfop());
						notaFiscalConhecimentoNovo.setVlBaseCalculo(notaFiscalConhecimentoTela.getVlBaseCalculo());
						notaFiscalConhecimentoNovo.setVlIcms(notaFiscalConhecimentoTela.getVlIcms());
						notaFiscalConhecimentoNovo.setVlBaseCalculoSt(notaFiscalConhecimentoTela.getVlBaseCalculoSt());
						notaFiscalConhecimentoNovo.setVlIcmsSt(notaFiscalConhecimentoTela.getVlIcmsSt());
						notaFiscalConhecimentoNovo.setVlTotalProdutos(notaFiscalConhecimentoTela.getVlTotalProdutos());
						notaFiscalConhecimentoNovo.setNrPinSuframa(notaFiscalConhecimentoTela.getNrPinSuframa());
						notaFiscalConhecimentoNovo.setTpDocumento(notaFiscalConhecimentoTela.getTpDocumento());
						isNfSelecionada = true;
						break;
					}
				}
			}
			
			if (isEntregaParcial && !isNfSelecionada) continue;
			
			//Tabela NF_DADOS_COMP (Deverá ser gerado um registro para cada registro existente para o CTRC original na tabela NF_DADOS_COMP)
			if(conhecimentoNovo.getDadosComplementos() != null) {
				for(DadosComplemento dadosComplemento : conhecimentoNovo.getDadosComplementos()) {
					if(dadosComplemento.getNfDadosComps() != null) {
						for(NfDadosComp nfDadosComp : dadosComplemento.getNfDadosComps()) {
							final Long idNotaFiscalConhecimento = nfDadosComp.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento();
							if(idNotaFiscalConhecimento != null) {
								if(CompareUtils.eq(notaFiscalConhecimento.getIdNotaFiscalConhecimento(), idNotaFiscalConhecimento)) {
									nfDadosComp.setNotaFiscalConhecimento(notaFiscalConhecimentoNovo);
								}
							}
						}
					}
				}
			}
			
			destino.add(notaFiscalConhecimentoNovo);
		}
		conhecimentoNovo.setNotaFiscalConhecimentos(destino);
	}

	public static void copyValorCusto(Conhecimento conhecimentoNovo, TipoCusto tipoCusto) {
		if(tipoCusto != null) {
		List<ValorCusto> destino = new ArrayList<ValorCusto>(1);
		ValorCusto valorCustoNovo = new ValorCusto();
		valorCustoNovo.setDoctoServico(conhecimentoNovo);
		valorCustoNovo.setTipoCusto(tipoCusto);
		valorCustoNovo.setVlCusto(conhecimentoNovo.getVlImposto());
		destino.add(valorCustoNovo);
		conhecimentoNovo.setValorCustos(destino);
	}
	}

	public static void copyDoctoServicoSeguros(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		List<DoctoServicoSeguros> origem = conhecimentoOriginal.getDoctoServicoSeguros();
		List<DoctoServicoSeguros> destino = new ArrayList<DoctoServicoSeguros>(origem.size());
		for(DoctoServicoSeguros doctoServicoSeguros : origem) {
			DoctoServicoSeguros doctoServicoSegurosNovo = new DoctoServicoSeguros();
			doctoServicoSegurosNovo.setConhecimento(conhecimentoNovo);
			doctoServicoSegurosNovo.setTipoSeguro(doctoServicoSeguros.getTipoSeguro());
			destino.add(doctoServicoSegurosNovo);
		}
		conhecimentoNovo.setDoctoServicoSeguros(destino);
	}

	/**
	 * Cria uma observacao para ser adicionada o Documento de Servico
	 * @author Claiton Grings
	 * @param doctoServico
	 * @param dsObservacao
	 * @param blPrioridade
	 * @return ObservacaoDoctoServico
	 */
	public static ObservacaoDoctoServico createObservacaoDocumentoServico(DoctoServico doctoServico, String dsObservacao, Boolean blPrioridade) {
		ObservacaoDoctoServico observacaoDoctoServico = new ObservacaoDoctoServico();
		observacaoDoctoServico.setBlPrioridade(blPrioridade);
		observacaoDoctoServico.setDsObservacaoDoctoServico(dsObservacao);
		observacaoDoctoServico.setDoctoServico(doctoServico);
		return observacaoDoctoServico;
	}

	/**
	 * Cria uma observacao para ser adicionada o Documento de Servico
	 * @author Claiton Grings
	 * @param doctoServico
	 * @param dsObservacao
	 * @param blPrioridade
	 * @return ObservacaoDoctoServico
	 */
	public static ObservacaoDoctoServico createObservacaoDocumentoServico(DoctoServico doctoServico, String dsObservacao, Boolean blPrioridade, String cdEmbLegalMastersaf) {
		ObservacaoDoctoServico observacaoDoctoServico = ConhecimentoUtils.createObservacaoDocumentoServico(doctoServico, dsObservacao, blPrioridade);
		observacaoDoctoServico.setCdEmbLegalMastersaf(cdEmbLegalMastersaf);
		return observacaoDoctoServico;
	}

	/**
	 * Seta Endereco de Entrega Real pelo Endereco de Entrega do Conhecimento.<p> 
	 * @author Andre Valadas.
	 * @param conhecimento
	 * @exception NullPointerException caso algum pojo nao esteja populado.
	 */
	public static void setEnderecoEntregaReal(Conhecimento conhecimento) {
		StringBuilder dsEndereco = new StringBuilder();

		if( conhecimento.getTipoLogradouroEntrega() != null && conhecimento.getTipoLogradouroEntrega().getDsTipoLogradouro() != null ){
			dsEndereco.append(StringUtils.defaultString(conhecimento.getTipoLogradouroEntrega().getDsTipoLogradouro().toString()));
			dsEndereco.append(" ");
		}
		
		dsEndereco.append(StringUtils.defaultString(conhecimento.getDsEnderecoEntrega()));
		dsEndereco.append(", nº. ");
		dsEndereco.append(StringUtils.defaultString(conhecimento.getNrEntrega()));
		dsEndereco.append(" - ");
		if(StringUtils.isNotBlank(conhecimento.getDsComplementoEntrega())) {
			dsEndereco.append(conhecimento.getDsComplementoEntrega());
			dsEndereco.append(" - ");
		}
		dsEndereco.append(StringUtils.defaultString(conhecimento.getDsBairroEntrega()));
		dsEndereco.append(" - ");
		dsEndereco.append(StringUtils.defaultString(conhecimento.getMunicipioByIdMunicipioEntrega().getNmMunicipio()));
		dsEndereco.append("/");
		dsEndereco.append(StringUtils.defaultString(conhecimento.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa().getSgUnidadeFederativa()));
		dsEndereco.append(" - CEP ");
		dsEndereco.append(StringUtils.defaultString(conhecimento.getNrCepEntrega()));
		/** Seta Endereco de Entrega Real */
		conhecimento.setDsEnderecoEntregaReal(dsEndereco.toString());
	}

	/**
	 * Gera Endereco de Entrega Real pelo Endereco de Entrega passado como parametro.<p> 
	 * @author Andre Valadas.
	 * @param enderecoPessoa
	 * @exception NullPointerException caso algum pojo nao esteja populado.
	 * @return
	 */
	public static String getEnderecoEntregaReal(EnderecoPessoa enderecoPessoa) {
		StringBuilder dsEndereco = new StringBuilder();

		/** Caso contrario seta o Endereco do Remetente */
		dsEndereco.append(StringUtils.defaultString(enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro().getValue()));
		dsEndereco.append(" ");
		dsEndereco.append(StringUtils.defaultString(enderecoPessoa.getDsEndereco()));
		dsEndereco.append(", nº. ");
		dsEndereco.append(StringUtils.defaultString(enderecoPessoa.getNrEndereco()));
		dsEndereco.append(" - ");
		if(StringUtils.isNotBlank(enderecoPessoa.getDsComplemento())) {
			dsEndereco.append(enderecoPessoa.getDsComplemento());
			dsEndereco.append(" - ");
		}
		dsEndereco.append(StringUtils.defaultString(enderecoPessoa.getDsBairro()));
		dsEndereco.append(" - ");
		dsEndereco.append(StringUtils.defaultString(enderecoPessoa.getMunicipio().getNmMunicipio()));
		dsEndereco.append("/");
		dsEndereco.append(StringUtils.defaultString(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		dsEndereco.append(" - CEP ");
		dsEndereco.append(StringUtils.defaultString(enderecoPessoa.getNrCep()));
		/** Retorna Endereco de Entrega Real */
		return dsEndereco.toString();
	}

	/**
	 * Seta Endereco de Entrega Real pelo Endereco de Entrega passado como parametro.<p> 
	 * @author Andre Valadas.
	 * @param doctoServico
	 * @param enderecoPessoa
	 */
	public static void setEnderecoEntregaReal(DoctoServico doctoServico, EnderecoPessoa enderecoPessoa) {
		/** Seta Endereco de Entrega Real */
		doctoServico.setDsEnderecoEntregaReal(getEnderecoEntregaReal(enderecoPessoa));
	}

	public static boolean verifyLiberacaoEmbarque(List<LiberacaoDocServ> liberacaoDocServs, String tpBloqueio, Boolean blEstacaoAutomatizada) {
		if(Arrays.binarySearch(CONSTANTES_LIBERACAO_PESO, tpBloqueio) > 0){
			if (blEstacaoAutomatizada == null) throw new IllegalArgumentException("Para liberacoes referentes a peso é preciso a informacao se esta se utilizando o sorter ou nao");
			if (blEstacaoAutomatizada) return true;
		}
		return verifyLiberacao(liberacaoDocServs, tpBloqueio);
		
	}

	
	private static boolean verifyLiberacao(List<LiberacaoDocServ> liberacaoDocServs, String tpBloqueio) {
		if (liberacaoDocServs == null) 	return false;
		for (LiberacaoDocServ liberacaoDocServ : liberacaoDocServs){
			if(tpBloqueio.equals(liberacaoDocServ.getTpBloqueioLiberado().getValue())) {
				return true;
			}
		}
		return false;
	}

	public static boolean verifyLiberacaoEmbarque(List<LiberacaoDocServ> liberacaoDocServs, String tpBloqueio) {
		return verifyLiberacaoEmbarque(liberacaoDocServs, tpBloqueio, null);
	}

	public static void copyEventoDocumentoServico(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		List<EventoDocumentoServico> origem = conhecimentoOriginal.getEventoDocumentoServicos();
		List<EventoDocumentoServico> destino = new ArrayList<EventoDocumentoServico>();
		if(origem != null) {
			for (EventoDocumentoServico eventoDocumentoServico : origem) {
				EventoDocumentoServico eventoDocumentoServicoNovo = new EventoDocumentoServico();
				eventoDocumentoServicoNovo.setBlEventoCancelado(eventoDocumentoServico.getBlEventoCancelado());
				eventoDocumentoServicoNovo.setDhComunicacao(eventoDocumentoServico.getDhComunicacao());
				eventoDocumentoServicoNovo.setDhEvento(eventoDocumentoServico.getDhEvento());
				eventoDocumentoServicoNovo.setDhGeracaoParceiro(eventoDocumentoServico.getDhGeracaoParceiro());
				eventoDocumentoServicoNovo.setDhInclusao(eventoDocumentoServico.getDhInclusao());
				eventoDocumentoServicoNovo.setFilial(eventoDocumentoServico.getFilial());
				eventoDocumentoServicoNovo.setNrDocumento(eventoDocumentoServico.getNrDocumento());
				eventoDocumentoServicoNovo.setObComplemento(eventoDocumentoServico.getObComplemento());
				eventoDocumentoServicoNovo.setOcorrenciaEntrega(eventoDocumentoServico.getOcorrenciaEntrega());
				eventoDocumentoServicoNovo.setOcorrenciaPendencia(eventoDocumentoServico.getOcorrenciaPendencia());
				eventoDocumentoServicoNovo.setPedidoCompra(eventoDocumentoServico.getPedidoCompra());
				eventoDocumentoServicoNovo.setTpDocumento(eventoDocumentoServico.getTpDocumento());
				eventoDocumentoServicoNovo.setUsuario(eventoDocumentoServico.getUsuario());
				eventoDocumentoServicoNovo.setEvento(eventoDocumentoServico.getEvento());
				eventoDocumentoServicoNovo.setDoctoServico(conhecimentoNovo);
				destino.add(eventoDocumentoServicoNovo);
			}
		}
		conhecimentoNovo.setEventoDocumentoServicos(destino);
	}
	
	public static void copyImpostoServico(Conhecimento conhecimentoOriginal, Conhecimento conhecimentoNovo) {
		if(conhecimentoOriginal.getImpostoServicos() != null){
			List<ImpostoServico> impostos = conhecimentoOriginal.getImpostoServicos();
			for (ImpostoServico impostoServico : impostos) {
				impostoServico.setConhecimento(conhecimentoNovo);
			}
			conhecimentoNovo.setImpostoServicos(impostos);
		}
	}
	
	/**
	 * Cria um objeto conhecimento sem dependencia com docto servico
	 * 
	 * @param conhecimento
	 * @return
	 */
	public static Conhecimento cloneConhecimento(Conhecimento conhecimento){

		Conhecimento conhecimentoNovo = new Conhecimento();
		conhecimentoNovo.setPsReal(conhecimento.getPsReal());
		conhecimentoNovo.setPsAferido(conhecimento.getPsAferido());
		conhecimentoNovo.setPsAforado(conhecimento.getPsAforado());
		conhecimentoNovo.setDhInclusao(conhecimento.getDhInclusao());
		conhecimentoNovo.setTpDocumentoServico(conhecimento.getTpDocumentoServico());
		conhecimentoNovo.setTpDoctoServico(conhecimento.getTpDoctoServico());				
		conhecimentoNovo.setDadosCliente(conhecimento.getDadosCliente());
		conhecimentoNovo.setObservacaoDoctoServicos(conhecimento.getObservacaoDoctoServicos());
		conhecimentoNovo.setDevedorDocServs(conhecimento.getDevedorDocServs());
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimento.getClienteByIdClienteConsignatario());
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimento.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setEnderecoPessoa(conhecimento.getEnderecoPessoa());
		conhecimentoNovo.setServAdicionalDocServs(conhecimento.getServAdicionalDocServs());
		conhecimentoNovo.setVlMercadoria(conhecimento.getVlMercadoria());
		conhecimentoNovo.setDimensoes(conhecimento.getDimensoes());

		if(conhecimento.getDadosComplementos() != null) {
			conhecimentoNovo.setDadosComplementos(conhecimento.getDadosComplementos());
		}		

		if(conhecimento.getBlReembolso() == null) {
			conhecimentoNovo.setBlReembolso(Boolean.FALSE);
		} else {
			conhecimentoNovo.setBlReembolso(conhecimento.getBlReembolso());
		}

		if(conhecimento.getBlIndicadorEdi() == null) {
			conhecimentoNovo.setBlIndicadorEdi(Boolean.FALSE);
		} else {
			conhecimentoNovo.setBlIndicadorEdi(conhecimento.getBlIndicadorEdi());
		}		

		conhecimentoNovo.setTpConhecimento(conhecimento.getTpConhecimento());
		conhecimentoNovo.setPsReal(conhecimento.getPsReal());
		conhecimentoNovo.setQtVolumes(conhecimento.getQtVolumes());
		conhecimentoNovo.setNotaFiscalConhecimentos(conhecimento.getNotaFiscalConhecimentos());		
		conhecimentoNovo.setDtPrevEntrega(conhecimento.getDtPrevEntrega());

		if (conhecimento.getDsLocalEntrega() != null || conhecimento.getDsEnderecoEntrega() != null) {

			conhecimentoNovo.setDsLocalEntrega(conhecimento.getDsLocalEntrega());
			conhecimentoNovo.setDsBairroEntrega(conhecimento.getDsBairroEntrega());
			conhecimentoNovo.setTipoLogradouroEntrega(conhecimento.getTipoLogradouroEntrega());
			conhecimentoNovo.setDsComplementoEntrega(conhecimento.getDsComplementoEntrega());
			conhecimentoNovo.setDsEnderecoEntrega(conhecimento.getDsEnderecoEntrega());
			conhecimentoNovo.setNrEntrega(conhecimento.getNrEntrega());
			conhecimentoNovo.setNrCepEntrega(conhecimento.getNrCepEntrega());
			conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimento.getMunicipioByIdMunicipioEntrega());

		} else if (conhecimento.getClienteByIdClienteConsignatario() != null) { // Caso não tenha sido informado endereço de entrega, verificar se foi informado algum consignatário

			Cliente clienteConsignatario = conhecimento.getClienteByIdClienteDestinatario();
			conhecimentoNovo.setDsLocalEntrega(null);
			EnderecoPessoa enderecoPessoa = clienteConsignatario.getPessoa().getEnderecoPessoa();
			if (enderecoPessoa != null) {
				conhecimentoNovo.setTipoLogradouroEntrega(enderecoPessoa.getTipoLogradouro());
				conhecimentoNovo.setDsBairroEntrega(enderecoPessoa.getDsBairro());
				conhecimentoNovo.setDsComplementoEntrega(enderecoPessoa.getDsComplemento());
				conhecimentoNovo.setDsEnderecoEntrega(enderecoPessoa.getDsEndereco());
				if(enderecoPessoa.getNrEndereco() != null){
					conhecimentoNovo.setNrEntrega(enderecoPessoa.getNrEndereco().trim());
				}
				conhecimentoNovo.setNrCepEntrega(enderecoPessoa.getNrCep());
				conhecimentoNovo.setMunicipioByIdMunicipioEntrega(enderecoPessoa.getMunicipio());
			}			
		
		} else if (conhecimento.getClienteByIdClienteDestinatario() != null) { // Caso não tenha sido informado local de entrega e consignatário, utilizar endereço do Destinatário

			Cliente clienteDestinatario = conhecimento.getClienteByIdClienteDestinatario();
			conhecimentoNovo.setDsLocalEntrega(null);
			EnderecoPessoa enderecoPessoa = clienteDestinatario.getPessoa().getEnderecoPessoa();
			if (enderecoPessoa != null) {
				conhecimentoNovo.setTipoLogradouroEntrega(enderecoPessoa.getTipoLogradouro());
				conhecimentoNovo.setDsBairroEntrega(enderecoPessoa.getDsBairro());
				conhecimentoNovo.setDsComplementoEntrega(enderecoPessoa.getDsComplemento());
				conhecimentoNovo.setDsEnderecoEntrega(enderecoPessoa.getDsEndereco());
				conhecimentoNovo.setNrEntrega(enderecoPessoa.getNrEndereco());
				conhecimentoNovo.setNrCepEntrega(enderecoPessoa.getNrCep());
				conhecimentoNovo.setMunicipioByIdMunicipioEntrega(enderecoPessoa.getMunicipio());
			}

		}		

		conhecimentoNovo.setDsLocalEntrega(conhecimento.getDsLocalEntrega());
		conhecimentoNovo.setDsEnderecoEntrega(conhecimento.getDsEnderecoEntrega());		
		conhecimentoNovo.setBlPaletizacao(conhecimento.getBlPaletizacao());
		conhecimentoNovo.setQtPaletes(conhecimento.getQtPaletes());
		conhecimentoNovo.setQtEtiquetasEmitidas(conhecimento.getQtEtiquetasEmitidas());
		conhecimentoNovo.setServico(conhecimento.getServico());

		conhecimentoNovo.setClienteByIdClienteRemetente(conhecimento.getClienteByIdClienteRemetente());
		conhecimentoNovo.setInscricaoEstadualRemetente(conhecimento.getInscricaoEstadualRemetente());

		conhecimentoNovo.setClienteByIdClienteDestinatario(conhecimento.getClienteByIdClienteDestinatario());
		conhecimentoNovo.setInscricaoEstadualDestinatario(conhecimento.getInscricaoEstadualDestinatario());

		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimento.getClienteByIdClienteConsignatario());		
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimento.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setClienteByIdClienteBaseCalculo(conhecimento.getClienteByIdClienteBaseCalculo());

		conhecimentoNovo.setDoctoServicoOriginal(conhecimento.getDoctoServicoOriginal());
		conhecimentoNovo.setCotacoes(conhecimento.getCotacoes());
		conhecimentoNovo.setPedidoColeta(conhecimento.getPedidoColeta());
		conhecimentoNovo.setTpCalculoPreco(conhecimento.getTpCalculoPreco());
		conhecimentoNovo.setBlParcelas(conhecimento.getBlParcelas());
		conhecimentoNovo.setBlServicosAdicionais(conhecimento.getBlServicosAdicionais());
		conhecimentoNovo.setTpFrete(conhecimento.getTpFrete());
		conhecimentoNovo.setTpDevedorFrete(conhecimento.getTpDevedorFrete());

		conhecimentoNovo.setDsCodigoColeta(conhecimento.getDsCodigoColeta());
		conhecimentoNovo.setBlColetaEmergencia(conhecimento.getBlColetaEmergencia());
		conhecimentoNovo.setBlEntregaEmergencia(conhecimento.getBlEntregaEmergencia());

		conhecimentoNovo.setBlCalculaFrete(conhecimento.getBlCalculaFrete());
		conhecimentoNovo.setBlCalculaServico(conhecimento.getBlCalculaServico());

		conhecimentoNovo.setNrCtrcSubcontratante(conhecimento.getNrCtrcSubcontratante());
		conhecimentoNovo.setFilialByIdFilialOrigem(conhecimento.getFilialByIdFilialOrigem());
		conhecimentoNovo.setFilialOrigem(conhecimento.getFilialOrigem());

		conhecimentoNovo.setFilialByIdFilialDestino(conhecimento.getFilialByIdFilialDestino());

		conhecimentoNovo.setMoeda(conhecimento.getMoeda());

		conhecimentoNovo.setDtColeta(conhecimento.getDtColeta());
		conhecimentoNovo.setAeroportoByIdAeroportoOrigem(conhecimento.getAeroportoByIdAeroportoOrigem());
		conhecimentoNovo.setAeroportoByIdAeroportoDestino(conhecimento.getAeroportoByIdAeroportoDestino());
		conhecimentoNovo.setProdutoEspecifico(conhecimento.getProdutoEspecifico());
		conhecimentoNovo.setNaturezaProduto(conhecimento.getNaturezaProduto());
		conhecimentoNovo.setMunicipioByIdMunicipioColeta(conhecimento.getMunicipioByIdMunicipioColeta());

		conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimento.getMunicipioByIdMunicipioEntrega());
		conhecimentoNovo.setNrCepEntrega(conhecimento.getNrCepEntrega());

		conhecimentoNovo.setDensidade(conhecimento.getDensidade());
		conhecimentoNovo.setCtoCtoCooperadas(conhecimento.getCtoCtoCooperadas());

		return conhecimentoNovo;
	}
	
	public static DevedorDocServ getDevedorDocServ(DoctoServico doctoServico) {
		List<DevedorDocServ> devedorDocServs = doctoServico.getDevedorDocServs();
		if (devedorDocServs != null && ! devedorDocServs.isEmpty()) {
			return doctoServico.getDevedorDocServs().get(0);
		}
		return null;
	}
	
	public static boolean isLiberaEtiquetaEdi(Conhecimento conhecimento){
        if(BooleanUtils.isTrue(conhecimento.getBlProcessamentoTomador())){
            DevedorDocServ devedorDocServs = getDevedorDocServ(conhecimento);
            if(BooleanUtils.isTrue(devedorDocServs.getCliente().getBlLiberaEtiquetaEdi())){
                return true;
            }
        }else if(BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario())){
            return BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlLiberaEtiquetaEdiLinehaul());
        }else{
            if(conhecimento.getClienteByIdClienteRemetente() != null && BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlLiberaEtiquetaEdi())){
                return true;
            }
        }
		return false;
	}

	public static boolean isAtualizaDestinatarioEdiOrConsignatarioEdi(Conhecimento conhecimento){
		if(BooleanUtils.isTrue(conhecimento.getBlProcessamentoTomador())){
			DevedorDocServ devedorDocServs = getDevedorDocServ(conhecimento);
			if(BooleanUtils.isTrue(devedorDocServs.getCliente().getBlAtualizaDestinatarioEdi()) 
					|| BooleanUtils.isTrue(devedorDocServs.getCliente().getBlAtualizaConsignatarioEdi())){
				return true;
			}
		}else{
			if(conhecimento.getClienteByIdClienteRemetente() != null && 
					(BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlAtualizaDestinatarioEdi()) 
						|| BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlAtualizaConsignatarioEdi())
					)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isUtilizaPesoEDI(Conhecimento conhecimento){
		if(BooleanUtils.isTrue(conhecimento.getBlProcessamentoTomador())){
			DevedorDocServ devedorDocServs = getDevedorDocServ(conhecimento);
			if(devedorDocServs != null && BooleanUtils.isTrue(devedorDocServs.getCliente().getBlUtilizaPesoEDI())){
				return true;
			}
		}else{
			if(conhecimento.getClienteByIdClienteRemetente() != null && BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlUtilizaPesoEDI())){
				return true;
			}
		}
		return false;
	}

	public static List<ObservacaoDoctoServico> criaObservacoesDoctoServicoPerigosoControlado(Conhecimento conhecimento, String dsProdutoPerigoso, String dsProdutoControlado) {
		List<ObservacaoDoctoServico> resultado = new ArrayList();

		if (BooleanUtils.isTrue(conhecimento.getBlProdutoPerigoso())) {
			resultado.add(createObservacaoDocumentoServico(conhecimento, dsProdutoPerigoso, false));
		}

		if (BooleanUtils.isTrue(conhecimento.getBlProdutoControlado())) {
			resultado.add(createObservacaoDocumentoServico(conhecimento, dsProdutoControlado, false));
		}

		return resultado;
	}
}