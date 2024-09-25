package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.tributos.model.AliquotaIcms;
import com.mercurio.lms.tributos.model.CalcularPisCofinsCsllIrInss;
import com.mercurio.lms.tributos.model.ImpostoCalculado;
import com.mercurio.lms.tributos.model.param.CalcularIDifalParam;
import com.mercurio.lms.tributos.model.param.CalcularIcmsParam;
import com.mercurio.lms.tributos.model.service.*;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ParametroCliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.calculoTributoService"
 */
public class CalculoTributoService {
	private CalcularIcmsService calcularIcmsService;
	private CalcularIcmsAwbService calcularIcmsAwbService;
	private CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService;
	private CalcularIssService calcularIssService;
	private ConfiguracoesFacade configuracoesFacade;
	private CalcularDifalService calcularDifalService;
	private ParametroGeralService parametroGeralService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;

	public void calculaTributos(CalculoFrete calculoFrete) {
		if(ConstantesExpedicao.ABRANGENCIA_INTERNACIONAL.equals(calculoFrete.getTpAbrangencia())) {
			return;
		}
		if(calculoFrete.isCalculoNotaTransporte()) {
			calculaTributoNFT((CalculoNFT) calculoFrete);
		} else {

			CalcularIcmsParam calcularIcmsParam = calcularIcmsService.calcularIcms(calculoFrete);

			if(this.calcularDifalService.verificaIsCalculoDifal(calcularIcmsParam)) {

				Long idUfDestino = calcularIcmsParam.getIdUfDestino();

				AliquotaIcms aliquotaIcmsInterna = this.calcularDifalService.aliquotaIcmsInterna(idUfDestino,
						calcularIcmsParam.getTpSituacaoTributariaRemetente(),calcularIcmsParam.getTpSituacaoTributariaDestinatario(),
						calcularIcmsParam.getTpFrete(), calcularIcmsParam.getDtEmissao());

				CalcularIDifalParam calcularIDifalParam = this.calcularDifalService.calcularDifal(calcularIcmsParam,
						aliquotaIcmsInterna.getPcAliquota(), aliquotaIcmsInterna.getPcEmbute());

				calcularIcmsParam.getCalculoFrete().getDoctoServico().setVlImpostoDifal(calcularIDifalParam.getPcImpostoDifial());
				calcularIcmsParam.getCalculoFrete().getDoctoServico().setPcIcmsUfFim(calcularIDifalParam.getPcIcmsUfFim());

				if(Boolean.FALSE.equals(calculoFrete.getBlCotacao())) {
					BigDecimal cdOcorrenciaBloqueio = (BigDecimal) parametroGeralService.
							findConteudoByNomeParametroWithoutException(ConstantesExpedicao.NM_PARAMETRO_CD_OCORRENCIA_BLOQ_DIFAL, false);
					OcorrenciaPendencia ocorrencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf(cdOcorrenciaBloqueio.toString()));
					ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(calcularIcmsParam.getCalculoFrete().getDoctoServico().getIdDoctoServico(),
							ocorrencia.getIdOcorrenciaPendencia(), null, JTDateTimeUtils.getDataHoraAtual(), null);
				}

			}
		}
	}

	private void calcularValorTotal(CalculoNFT calculoFrete) {

		/*Obtem o parametro do cliente através do cálculo de frete*/
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();

		BigDecimal vlDesconto = null;

		/*Se tem que usar o desconto da tabela do cliente*/
		if ((parametroCliente == null) || Boolean.FALSE.equals(calculoFrete.getBlDescontoTotal())) {
			vlDesconto = calculoFrete.getVlDesconto();
			/*Se a tabela parametro_cliente tem desconto*/
		} else if (BigDecimalUtils.hasValue(parametroCliente.getPcDescontoFreteTotal())) {
			vlDesconto = calculoFrete.getVlTotalParcelas().multiply(BigDecimalUtils.percent(parametroCliente.getPcDescontoFreteTotal()));
		}

		/*Seta Zero caso o valor do desconto for nulo*/
		if (vlDesconto == null) {
			vlDesconto = BigDecimalUtils.ZERO;
		} else {
			vlDesconto = BigDecimalUtils.round(vlDesconto);
		}

		/*Se é um cálculo de cortesia*/
		if (ConstantesExpedicao.CALCULO_CORTESIA.equals(calculoFrete.getTpCalculo())) {
			vlDesconto = calculoFrete.getVlTotalParcelas().multiply(BigDecimalUtils.percent((BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.PC_DESCONTO_FRETE_CORTESIA)));
		}

		/*Se o cálculo for manual*/
		if(ConstantesExpedicao.CALCULO_MANUAL.equals(calculoFrete.getTpCalculo())) {
			vlDesconto =  BigDecimalUtils.round(BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlDesconto()));
		}

		/*Valor do Desconto*/
		calculoFrete.setVlDesconto(vlDesconto);
	}

	public void executeCalculoTributacaoNFT(CalculoFrete calculoFrete){
		calculaTributoNFT((CalculoNFT) calculoFrete);
	}

	public void calculaTributos(CalculoFreteCiaAerea calculoFreteCiaAerea) {
		calcularIcmsAwbService.calcularIcmsAwb(calculoFreteCiaAerea);
	}

	public void calculaTributos(CalculoNFServico calculoNFS) {
		ParcelaServico parcelaServico = (ParcelaServico) calculoNFS.getServicosAdicionais().get(0);
		BigDecimal vlBaseCalculo = parcelaServico.getVlBrutoParcela();
		parcelaServico.setVlParcela(vlBaseCalculo);

		Object[] tributos = montaTributos(
				calculoNFS.getClienteBase(),
				parcelaServico.getParcelaPreco().getServicoAdicional().getIdServicoAdicional(),
				null,
				vlBaseCalculo,
				SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio(),
				calculoNFS.getRestricaoRotaDestino().getIdMunicipio(),
				Boolean.FALSE
		);

		calculoNFS.setVlDevido((BigDecimal) tributos[0]);
		calculoNFS.setVlTotalTributos((BigDecimal) tributos[1]);
		calculoNFS.setTributos((List) tributos[2]);
		calculoNFS.setImpostosCalculados((List)tributos[3]);
		calculoNFS.setVlTotalParcelas(BigDecimalUtils.ZERO);
		calculoNFS.setVlTotalServicosAdicionais(vlBaseCalculo);
		calculoNFS.setVlTotal(vlBaseCalculo);
	}

	private void calculaTributoNFT(CalculoNFT calculoNFT) {
		calculoNFT.setVlTotalParcelas();
		calcularValorTotal(calculoNFT);
		BigDecimal vlBaseCalculo = calculoNFT.getVlTotalParcelas().subtract(calculoNFT.getVlDesconto());
		Long idServicoTributo = LongUtils.getLong((BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.ID_SERVICO_TRIBUTO_NFT));

		Object[] tributos = montaTributos(
				calculoNFT.getClienteBase(),
				null,
				idServicoTributo,
				vlBaseCalculo,
				SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio(),
				calculoNFT.getRestricaoRotaDestino().getIdMunicipio(),
				calculoNFT.getBlCotacao()
		);

		calculoNFT.setVlDevido((BigDecimal) tributos[0]);
		calculoNFT.setVlTotalTributos((BigDecimal) tributos[1]);
		calculoNFT.setTributos((List) tributos[2]);
		calculoNFT.setImpostosCalculados((List)tributos[3]);

		calculoNFT.setVlTotalServicosAdicionais(BigDecimal.ZERO);
		calculoNFT.setVlTotal(vlBaseCalculo);

	}

	public Object[] montaTributos(
			Cliente cliente,
			Long idServicoAdicional,
			Long idServicoTributo,
			BigDecimal vlBaseCalculo,
			Long idMunicipioOrigem,
			Long idMunicipioDestino,
			Boolean blCotacao
	) {
		ImpostoServico impostoServico = null;
		DomainValue tpImposto = null;
		BigDecimal vlImposto = null;
		BigDecimal vlTotalImposto = BigDecimalUtils.ZERO;
		List<ImpostoServico> tributos = new ArrayList<ImpostoServico>();
		List<ImpostoCalculado> impostosCalculados = new ArrayList<ImpostoCalculado>();

		if(!blCotacao) {
			if("J".equals(cliente.getPessoa().getTpPessoa().getValue())) {
				List list = calcularPisCofinsCsllIrInssService.calcularPisCofinsCsllIrInssPessoaJudirica(
						cliente.getIdCliente(),
						"",
						"OU",
						idServicoAdicional,
						idServicoTributo,
						JTDateTimeUtils.getDataAtual(),
						vlBaseCalculo
				);

				Iterator it = list.iterator();
				CalcularPisCofinsCsllIrInss imposto = null;
				while(it.hasNext()) {
					imposto = (CalcularPisCofinsCsllIrInss) it.next();
					vlImposto = imposto.getVlImposto();

					impostoServico = new ImpostoServico();
					impostoServico.setVlImposto(vlImposto);
					impostoServico.setVlBaseCalculo(imposto.getVlBaseCalculo());
					impostoServico.setPcAliquota(imposto.getPcAliquotaImposto());
					tpImposto = configuracoesFacade.getDomainValue("DM_TIPO_IMPOSTO", imposto.getTpImpostoCalculado());
					impostoServico.setTpImposto(tpImposto);
					impostoServico.setVlBaseEstorno(imposto.getVlBaseEstorno());
					impostoServico.setBlRetencaoTomadorServico(Boolean.TRUE);
					tributos.add(impostoServico);
					if( imposto.getImpostoCalculado() != null ){
						impostosCalculados.add(imposto.getImpostoCalculado());
					}
					vlTotalImposto = vlTotalImposto.add(vlImposto);
				}
			}
		}

		BigDecimal vlIssSemRetensao = BigDecimalUtils.ZERO;
		Map map = calcularIssService.calcularIss(
				cliente.getIdCliente(),
				idMunicipioOrigem,
				idMunicipioDestino,
				idServicoAdicional,
				idServicoTributo,
				JTDateTimeUtils.getDataAtual(),
				vlBaseCalculo
		);
		if(map != null) {
			vlImposto = (BigDecimal) map.get("vlIss");

			impostoServico = new ImpostoServico();
			impostoServico.setPcAliquota((BigDecimal) map.get("pcAliquotaIss"));
			tpImposto = configuracoesFacade.getDomainValue("DM_TIPO_IMPOSTO", ConstantesExpedicao.CD_ISS);
			impostoServico.setTpImposto(tpImposto);
			impostoServico.setVlImposto(vlImposto);
			impostoServico.setVlBaseCalculo(vlBaseCalculo);
			impostoServico.setVlBaseEstorno(vlBaseCalculo);
			impostoServico.setBlRetencaoTomadorServico((Boolean) map.get("blRetencaoTomador"));
			Municipio municipio = new Municipio();
			municipio.setIdMunicipio((Long) map.get("idMunicipioIncidencia"));
			impostoServico.setMunicipioByIdMunicipioIncidencia(municipio);
			tributos.add(impostoServico);
			if(Boolean.FALSE.equals(impostoServico.getBlRetencaoTomadorServico())) {
				vlIssSemRetensao = vlImposto;
			}
			vlTotalImposto = vlTotalImposto.add(vlImposto);
		}
		BigDecimal vlDevido = vlBaseCalculo.subtract(vlTotalImposto).add(vlIssSemRetensao);

		return new Object[]{vlDevido, vlTotalImposto, tributos,impostosCalculados};
	}

	public void setCalcularIcmsService(CalcularIcmsService calcularIcmsService) {
		this.calcularIcmsService = calcularIcmsService;
	}
	public void setCalcularIcmsAwbService(CalcularIcmsAwbService calcularIcmsAwbService) {
		this.calcularIcmsAwbService = calcularIcmsAwbService;
	}
	public void setCalcularIssService(CalcularIssService calcularIssService) {
		this.calcularIssService = calcularIssService;
	}
	public void setCalcularPisCofinsCsllIrInssService(CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService) {
		this.calcularPisCofinsCsllIrInssService = calcularPisCofinsCsllIrInssService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setCalcularDifalService(CalcularDifalService calcularDifalService) {
		this.calcularDifalService = calcularDifalService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}

	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}
}
