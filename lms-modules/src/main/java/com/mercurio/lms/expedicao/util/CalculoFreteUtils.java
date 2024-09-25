package com.mercurio.lms.expedicao.util;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CALCULO_MANUAL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoFreteCiaAerea;
import com.mercurio.lms.expedicao.model.CalculoNFServico;
import com.mercurio.lms.expedicao.model.CalculoNFT;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.ParcelaServicoAdicional;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaSpot;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.ParcelaCotacao;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;

public abstract class CalculoFreteUtils {

	public static void copyResult(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		copyCommonResult(conhecimento, calculoFrete);

		//Tributos
		DomainValue tpDocumentoServico = conhecimento.getTpDocumentoServico();
		if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDocumentoServico.getValue()) || (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico.getValue()) && calculoFrete.isCalculoNotaTransporte())) {
			List<ImpostoServico> impostoServicos = ((CalculoNFT) calculoFrete).getTributos();
			if(impostoServicos != null) {
				for (ImpostoServico impostoServico : impostoServicos) {
					impostoServico.setConhecimento(conhecimento);
				}
			}
			conhecimento.setImpostoServicos(impostoServicos);

			conhecimento.setPcAliquotaIcms(BigDecimalUtils.ZERO);
			conhecimento.setVlImposto(BigDecimalUtils.ZERO);
			conhecimento.setVlBaseCalcImposto(BigDecimalUtils.ZERO);
			conhecimento.setVlIcmsSubstituicaoTributaria(BigDecimalUtils.ZERO);
			conhecimento.setBlIncideIcmsPedagio(Boolean.FALSE);
			conhecimento.setTipoTributacaoIcms(null);
		} else {
			ImpostoServico impostoServico = calculoFrete.getTributo();

			conhecimento.setPcAliquotaIcms(impostoServico.getPcAliquota());
			conhecimento.setVlImposto(impostoServico.getVlImposto());
			conhecimento.setVlBaseCalcImposto(impostoServico.getVlBaseCalculo());
			conhecimento.setVlIcmsSubstituicaoTributaria(calculoFrete.getVlRetensaoSituacaoTributaria());
			conhecimento.setBlIncideIcmsPedagio(calculoFrete.getBlIncideIcmsPedagio());
			TipoTributacaoIcms tipoTributacaoIcms = new TipoTributacaoIcms();
			tipoTributacaoIcms.setIdTipoTributacaoIcms(calculoFrete.getIdTipoTributacaoICMS());
			conhecimento.setTipoTributacaoIcms(tipoTributacaoIcms);
		}

		conhecimento.setTpFrete(new DomainValue(calculoFrete.getTpFrete()));
		if(calculoFrete.getBlParametroFretePercentual() != null)
			conhecimento.setBlIndicadorFretePercentual(calculoFrete.getBlParametroFretePercentual());
		else
			conhecimento.setBlIndicadorFretePercentual(Boolean.FALSE);

	}

	public static void copyResult(Cotacao cotacao, CalculoFrete calculoFrete) {
		//Totais
		cotacao.setVlTotalCotacao(calculoFrete.getVlTotal());
		cotacao.setVlTotalParcelas(calculoFrete.getVlTotalParcelas());
		cotacao.setVlTotalServicos(calculoFrete.getVlTotalServicosAdicionais());
		cotacao.setVlDesconto(calculoFrete.getVlDesconto());
		cotacao.setVlLiquido(calculoFrete.getVlDevido());

		//Tibutos
		DomainValue tpDocumentoServico = cotacao.getTpDocumentoCotacao();
		if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDocumentoServico.getValue())) {
			cotacao.setVlImposto(calculoFrete.getVlTotalTributos());
			cotacao.setVlIcmsSubstituicaoTributaria(BigDecimalUtils.ZERO);
			cotacao.setBlIncideIcmsPedagio(Boolean.FALSE);
		} else {
			cotacao.setVlImposto(calculoFrete.getTributo().getVlImposto());
			cotacao.setVlIcmsSubstituicaoTributaria(calculoFrete.getVlRetensaoSituacaoTributaria());
			cotacao.setBlIncideIcmsPedagio(calculoFrete.getBlIncideIcmsPedagio());
		}

		cotacao.setTabelaPreco(calculoFrete.getTabelaPreco());

		List<ParcelaCotacao> parcelasCotacao = new ArrayList<ParcelaCotacao>();
		//Parcelas de Frete
		List<ParcelaServico> parcelas = calculoFrete.getParcelas();
		if(cotacao.getBlFrete().booleanValue() && parcelas != null) {
			for(ParcelaServico parcela : parcelas) {
				parcelasCotacao.add(createParcelaCotacao(cotacao, parcela));
			}
		}

		//Parcelas de Servico Adicional
		List<ServicoAdicionalCliente> servicosAdicionaisCliente = null;
		List<ParcelaServicoAdicional> parcelasServico = calculoFrete.getServicosAdicionais();
		ServicoAdicionalCliente servicoAdicionalCliente = null;
		if(cotacao.getBlServicosAdicionais().booleanValue() && parcelasServico != null) {
			servicosAdicionaisCliente = new ArrayList<ServicoAdicionalCliente>();
			for(ParcelaServicoAdicional parcelaServicoAdicional : parcelasServico) {
				parcelasCotacao.add(createParcelaCotacao(cotacao, parcelaServicoAdicional));
				servicoAdicionalCliente = createServicoAdicionalCliente(cotacao, parcelaServicoAdicional);
				if(servicoAdicionalCliente != null) {
					servicosAdicionaisCliente.add(servicoAdicionalCliente);
				}
			}
		}
		cotacao.setParcelaCotacoes(parcelasCotacao);

		//Parcelas de Servicos Adicionais Auxiliares
		parcelasServico = calculoFrete.getServicosAdicionaisAuxiliares();
		if(cotacao.getBlServicosAdicionais().booleanValue() && parcelasServico != null) {
			if(servicosAdicionaisCliente == null) {
				servicosAdicionaisCliente = new ArrayList<ServicoAdicionalCliente>();
			}
			for(ParcelaServicoAdicional parcelaServicoAdicional : parcelasServico) {
				servicoAdicionalCliente = createServicoAdicionalCliente(cotacao, parcelaServicoAdicional);
				if(servicoAdicionalCliente != null) {
					servicosAdicionaisCliente.add(servicoAdicionalCliente);
				}
			}
		}
		cotacao.setServicoAdicionalClientes(servicosAdicionaisCliente);
	}

	public static void copyResult(Mda mda, CalculoFrete calculoFrete) {
		copyCommonResult(mda, calculoFrete);

		//Tributos
		ImpostoServico impostoServico = calculoFrete.getTributo();
		mda.setPcAliquotaIcms(impostoServico.getPcAliquota());
		mda.setVlImposto(impostoServico.getVlImposto());
		mda.setVlBaseCalcImposto(impostoServico.getVlBaseCalculo());
		mda.setVlIcmsSubstituicaoTributaria(calculoFrete.getVlRetensaoSituacaoTributaria());
		mda.setBlIncideIcmsPedagio(calculoFrete.getBlIncideIcmsPedagio());
	}

	private static void copyCommonResult(DoctoServico doctoServico, CalculoFrete calculoFrete) {
		//Totais
		doctoServico.setVlTotalDocServico(calculoFrete.getVlTotal());
		doctoServico.setVlTotalParcelas(calculoFrete.getVlTotalParcelas());
		doctoServico.setVlTotalServicos(calculoFrete.getVlTotalServicosAdicionais());
		doctoServico.setVlDesconto(calculoFrete.getVlDesconto());
		doctoServico.setVlLiquido(calculoFrete.getVlDevido());

		doctoServico.setPsReferenciaCalculo(calculoFrete.getPsReferencia());
		doctoServico.setTpCalculoPreco(new DomainValue(calculoFrete.getTpCalculo()));
		doctoServico.setNrFatorDensidade(calculoFrete.getDoctoServico().getNrFatorDensidade());
		doctoServico.setParametroCliente(calculoFrete.getParametroCliente());
		if(calculoFrete.getIdTarifa() != null) {
			doctoServico.setTarifaPreco(new TarifaPreco(calculoFrete.getIdTarifa()));
		}
		doctoServico.setTabelaPreco(calculoFrete.getTabelaPreco());

		/* Observacoes do calculo */
		List<String> observacoes = calculoFrete.getObservacoes();
		List<String> observacoesMasterSaf = calculoFrete.getEmbLegaisMastersaf();
		
		/* Transforma em um Set removendo as obs duplicadas */
		Set<String> killObsDuplicated = new HashSet<String>(observacoes);
		
		/* Salva o set novamente em uma List */
		observacoes = new ArrayList<String>(killObsDuplicated);
		boolean blIndicadorEDI = false;
		boolean blLiberaEtiquetaEdi = false;
		if( doctoServico instanceof Conhecimento ){
			Conhecimento conhecimento = (Conhecimento)doctoServico;
			blIndicadorEDI = BooleanUtils.isTrue(conhecimento.getBlIndicadorEdi());
			blLiberaEtiquetaEdi = ConhecimentoUtils.isLiberaEtiquetaEdi(conhecimento);
		}else{
			blLiberaEtiquetaEdi = doctoServico.getClienteByIdClienteRemetente().getBlLiberaEtiquetaEdi();
		}
		
		if(observacoes != null )
			if ((doctoServico.getTpCalculoPreco() != null && CALCULO_MANUAL.equals(doctoServico.getTpCalculoPreco().getValue())) 
					|| (doctoServico.getNrDoctoServico() != null)
					|| (doctoServico.getClienteByIdClienteRemetente() != null && blLiberaEtiquetaEdi && blIndicadorEDI )
					|| (calculoFrete.getDoctoServico() != null && calculoFrete.getDoctoServico().getTpConhecimento() != null && ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(calculoFrete.getDoctoServico().getTpConhecimento().getValue())))  {
			for (int i = 0; i < observacoes.size(); i++) {
				String dsObservacao = observacoes.get(i);
				String cdEmbLegalMastersaf = null;
				if(observacoesMasterSaf != null && observacoesMasterSaf.size() > i) {
					cdEmbLegalMastersaf = observacoesMasterSaf.get(i);
			}
				
				if (!obsAlreadyExists(doctoServico, dsObservacao)) {
				doctoServico.addObservacaoDoctoServico(ConhecimentoUtils.createObservacaoDocumentoServico(doctoServico, dsObservacao, Boolean.TRUE, cdEmbLegalMastersaf));
		}
		}
		}

		/* Parcelas */
		List<ParcelaDoctoServico> parcelasDoctoServico = new ArrayList<ParcelaDoctoServico>();

		/* Parcelas de Frete */
		List<ParcelaServico> parcelasFrete = calculoFrete.getParcelas();
		if(parcelasFrete != null) {
			for(ParcelaServico parcela : parcelasFrete) {
				parcelasDoctoServico.add(createParcelaDoctoServico(doctoServico, parcela));
			}
		}

		/* Parcelas de Servico Adicional */
		List<ParcelaServicoAdicional> parcelasServico = calculoFrete.getServicosAdicionais();
		if(parcelasServico != null) {
			for(ParcelaServicoAdicional parcelaServico : parcelasServico) {
				parcelasDoctoServico.add(createParcelaDoctoServico(doctoServico, parcelaServico));
			}
		}
		doctoServico.setParcelaDoctoServicos(parcelasDoctoServico);
	}
	
	
	private static Boolean obsAlreadyExists(DoctoServico doctoServico, String dsObservacao) {
		if (doctoServico.getObservacaoDoctoServicos() != null ) {
			for (ObservacaoDoctoServico obs : doctoServico.getObservacaoDoctoServicos()) {
				if (obs.getDsObservacaoDoctoServico().equals(dsObservacao))
					return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	

	private static ParcelaDoctoServico createParcelaDoctoServico(DoctoServico doctoServico, ParcelaServico parcelaServico) {
		return new ParcelaDoctoServico(parcelaServico.getVlBrutoParcela(), parcelaServico.getVlParcela(), parcelaServico.getParcelaPreco(), doctoServico);
	}

	private static ParcelaCotacao createParcelaCotacao(Cotacao cotacao, ParcelaServico parcelaServico) {
		ParcelaCotacao parcelaCotacao = new ParcelaCotacao();

		parcelaCotacao.setCotacao(cotacao);
		parcelaCotacao.setParcelaPreco(parcelaServico.getParcelaPreco());
		parcelaCotacao.setVlBrutoParcela(parcelaServico.getVlBrutoParcela());
		parcelaCotacao.setVlParcelaCotacao(parcelaServico.getVlParcela());

		return parcelaCotacao;
	}

	private static ServicoAdicionalCliente createServicoAdicionalCliente(Cotacao cotacao, ParcelaServico parcelaServico) {
		ServicoAdicionalCliente servicoAdicionalCliente = (ServicoAdicionalCliente) parcelaServico.getParametro();

		if(servicoAdicionalCliente != null) {
			servicoAdicionalCliente.setParcelaPreco(parcelaServico.getParcelaPreco());
			servicoAdicionalCliente.setCotacao(cotacao);
			return servicoAdicionalCliente;
		}
		return null;
	}

	public static void copyResult(Awb awb, CalculoFreteCiaAerea calculoFreteCiaAerea) {

		ParcelaServico parcelaFretePeso = calculoFreteCiaAerea.getParcelaGeral(ConstantesExpedicao.CD_FRETE_PESO);
		if(parcelaFretePeso != null) {
			awb.setVlFretePeso(parcelaFretePeso.getVlParcela());
		}
		ParcelaServico parcelaTaxaTerrestre = calculoFreteCiaAerea.getParcelaGeral(ConstantesExpedicao.CD_TAXA_TERRESTRE);
		if(parcelaTaxaTerrestre != null) {
			awb.setVlTaxaTerrestre(parcelaTaxaTerrestre.getVlParcela());
		}
		ParcelaServico parcelaTaxaCombustivel = calculoFreteCiaAerea.getParcelaGeral(ConstantesExpedicao.CD_TAXA_COMBUSTIVEL);
		if(parcelaTaxaCombustivel != null) {
			awb.setVlTaxaCombustivel(parcelaTaxaCombustivel.getVlParcela());
		}

		//Tributos
		awb.setVlICMS(calculoFreteCiaAerea.getTributo().getVlImposto());
		awb.setPcAliquotaICMS(calculoFreteCiaAerea.getTributo().getPcAliquota());
		awb.setVlBaseCalcImposto(calculoFreteCiaAerea.getTributo().getVlBaseCalculo());

		TarifaSpot tarifaSpot = calculoFreteCiaAerea.getTarifaSpot();
		if(tarifaSpot != null) {
			awb.setTarifaSpot(tarifaSpot);
			awb.setVlQuiloTarifaSpot(tarifaSpot.getVlTarifaSpot());
		}

		Long idProdutoEspecifico = calculoFreteCiaAerea.getIdProdutoEspecifico();
		if(idProdutoEspecifico != null) {
			ProdutoEspecifico produtoEspecifico = new ProdutoEspecifico();
			produtoEspecifico.setIdProdutoEspecifico(idProdutoEspecifico);
			awb.setProdutoEspecifico(produtoEspecifico);
		}

		awb.setTpFrete(new DomainValue(calculoFreteCiaAerea.getTpFrete()));
		awb.setVlFrete(calculoFreteCiaAerea.getVlTotal());
	}

	public static void copyResult(NotaFiscalServico notaFiscalServico, CalculoNFServico calculoNFServico) {
		/** Parcela Servico */
		ParcelaServico parcelaServico = (ParcelaServico) calculoNFServico.getServicosAdicionais().get(0);
		notaFiscalServico.setParametroCliente(null);
		notaFiscalServico.setTabelaPreco(calculoNFServico.getTabelaPreco());

		/** Parcela Docto Servico */
		ParcelaDoctoServico parcelaDoctoServico = new ParcelaDoctoServico();
		parcelaDoctoServico.setParcelaPreco(parcelaServico.getParcelaPreco());
		parcelaDoctoServico.setVlParcela(parcelaServico.getVlParcela());
		parcelaDoctoServico.setDoctoServico(notaFiscalServico);

		List<ParcelaDoctoServico> parcelasDoctoServico = new ArrayList<ParcelaDoctoServico>(1);
		parcelasDoctoServico.add(parcelaDoctoServico);
		notaFiscalServico.setParcelaDoctoServicos(parcelasDoctoServico);

		/** Totais Valores/Impostos */
		notaFiscalServico.setVlTotalDocServico(calculoNFServico.getVlTotal());
		notaFiscalServico.setVlTotalServicos(calculoNFServico.getVlTotalServicosAdicionais());
		notaFiscalServico.setImpostoServicos(calculoNFServico.getTributos());
		notaFiscalServico.setVlBaseCalcImposto(calculoNFServico.getVlTotalServicosAdicionais());
		notaFiscalServico.setVlImposto(calculoNFServico.getVlTotalTributos());
		notaFiscalServico.setVlLiquido(calculoNFServico.getVlDevido());

		/** Devedor DoctoServico */
		DevedorDocServ devedorDoctoServico = new DevedorDocServ();
		devedorDoctoServico.setCliente(notaFiscalServico.getClienteByIdClienteDestinatario());
		devedorDoctoServico.setDoctoServico(notaFiscalServico);
		devedorDoctoServico.setFilial(notaFiscalServico.getFilialByIdFilialOrigem());
		devedorDoctoServico.setVlDevido(calculoNFServico.getVlDevido());

		List<DevedorDocServ> devedoresDoctoServico = new ArrayList<DevedorDocServ>(1);
		devedoresDoctoServico.add(devedorDoctoServico);
		notaFiscalServico.setDevedorDocServs(devedoresDoctoServico);

		/** Devedor DoctoServico Fatura */
		DevedorDocServFat devedorDoctoServicoFatura = new DevedorDocServFat();
		devedorDoctoServicoFatura.setCliente(notaFiscalServico.getClienteByIdClienteDestinatario());
		devedorDoctoServicoFatura.setDoctoServico(notaFiscalServico);
		devedorDoctoServicoFatura.setFilial(notaFiscalServico.getFilialByIdFilialOrigem());
		devedorDoctoServicoFatura.setVlDevido(calculoNFServico.getVlDevido());
		devedorDoctoServicoFatura.setTpSituacaoCobranca(new DomainValue("P"));
		devedorDoctoServicoFatura.setDivisaoCliente(notaFiscalServico.getDivisaoCliente());

		List<DevedorDocServFat> devedoresDoctoServicoFatura = new ArrayList<DevedorDocServFat>(1);
		devedoresDoctoServicoFatura.add(devedorDoctoServicoFatura);
		notaFiscalServico.setDevedorDocServFats(devedoresDoctoServicoFatura);
	}

	public static void ordenaParcelas(List<ParcelaServico> parcelas) {
		for (ParcelaServico parcelaServico : parcelas) {
			ParcelaPreco parcelaPreco = parcelaServico.getParcelaPreco();
			defineNrOrdem(parcelaPreco);
		}
		Collections.sort(parcelas, new Comparator<ParcelaServico>() {
			public int compare(ParcelaServico p1, ParcelaServico p2) {
				return (p1.getParcelaPreco().getNrOrdem() < p2.getParcelaPreco().getNrOrdem())  ? -1 : (p1.getParcelaPreco().getNrOrdem() > p2.getParcelaPreco().getNrOrdem()) ? 1 : 0;
			}
		});
	}

	public static void ordenaParcelasCotacao(List<ParcelaCotacao> parcelas) {
		for (ParcelaCotacao parcelaCotacao : parcelas) {
			ParcelaPreco parcelaPreco = parcelaCotacao.getParcelaPreco();
			defineNrOrdem(parcelaPreco);
		}
		Collections.sort(parcelas, new Comparator<ParcelaCotacao>() {
			public int compare(ParcelaCotacao p1, ParcelaCotacao p2) {
				return (CompareUtils.le(p1.getParcelaPreco().getNrOrdem(), p2.getParcelaPreco().getNrOrdem())  ? -1 : CompareUtils.ge(p1.getParcelaPreco().getNrOrdem(), p2.getParcelaPreco().getNrOrdem()) ? 1 : 0);
			}
		});
	}

	private static void defineNrOrdem(ParcelaPreco parcelaPreco) {
		String cdParcelaPreco = parcelaPreco.getCdParcelaPreco();
		if(ConstantesExpedicao.CD_FRETE_PESO.equals(cdParcelaPreco)) {
			parcelaPreco.setNrOrdem(LongUtils.getLong(1));
		} else if(ConstantesExpedicao.CD_FRETE_VALOR.equals(cdParcelaPreco)) {
			parcelaPreco.setNrOrdem(LongUtils.getLong(2));
		} else if(ConstantesExpedicao.CD_GRIS.equals(cdParcelaPreco)) {
			parcelaPreco.setNrOrdem(LongUtils.getLong(3));
		} else if(ConstantesExpedicao.CD_PEDAGIO.equals(cdParcelaPreco)) {
			parcelaPreco.setNrOrdem(LongUtils.getLong(4));
		} else {
			parcelaPreco.setNrOrdem(LongUtils.getLong(999));
		}
	}
	
	public static boolean isUtilizaPesoEDI(DoctoServico doctoServico){
		if(doctoServico instanceof Conhecimento){
			ConhecimentoUtils.isUtilizaPesoEDI((Conhecimento)doctoServico);
		}else{
			if(doctoServico.getClienteByIdClienteRemetente() != null && BooleanUtils.isTrue(doctoServico.getClienteByIdClienteRemetente().getBlUtilizaPesoEDI())){
				return true;
			}
		}
		
		return false;
	}
}
