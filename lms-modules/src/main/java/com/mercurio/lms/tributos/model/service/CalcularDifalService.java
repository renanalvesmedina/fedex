package com.mercurio.lms.tributos.model.service;

import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.AliquotaIcms;
import com.mercurio.lms.tributos.model.param.CalcularIDifalParam;
import com.mercurio.lms.tributos.model.param.CalcularIcmsParam;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.vendas.model.ParametroCliente;
import org.joda.time.YearMonthDay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class CalcularDifalService {

    private UnidadeFederativaService unidadeFederativaService;
    private ParametroGeralService parametroGeralService;
    private AliquotaIcmsService aliquotaIcmsService;
    private ConfiguracoesFacade configuracoesFacade;


    public CalcularIDifalParam calcularDifal(CalcularIcmsParam calcularIcmsParam, BigDecimal pcAliquotaInterna, BigDecimal pcEmbuteInterna) {

        calcularIcmsParam.getCalculoFrete().setVlTotalParcelas();
        calcularIcmsParam.getCalculoFrete().setVlTotalServicosAdicionais();
        calcularIcmsParam.setVlBase(BigDecimal.ZERO);
        calcularIcmsParam.setVlTotalParcelasComIcms(BigDecimal.ZERO);

        /** Calculo dos Valores*/
        calcularValorParcelas(calcularIcmsParam, pcEmbuteInterna);
        calcularValorTotal(calcularIcmsParam);
        calcularValorTotalIcms(calcularIcmsParam);
        calcularIcmsParam.getCalculoFrete().setVlDevido(calcularIcmsParam.getCalculoFrete().getVlTotal());
        calcularIcmsParam.setVlDevido(calcularIcmsParam.getCalculoFrete().getVlTotal());

        BigDecimal vlIcmsTotal = calcularIcmsParam.getVlBase().multiply(
                calcularIcmsParam.getCalculoFrete().getTributo().getPcAliquota().divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vlIcmsTotal2 = calcularIcmsParam.getVlBase().multiply(pcAliquotaInterna.divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vlImpostoDifal = vlIcmsTotal2.subtract(vlIcmsTotal);

        return new CalcularIDifalParam(vlImpostoDifal, pcAliquotaInterna);

    }

    private void calcularValorTotalIcms(CalcularIcmsParam cip) {
        cip.setVlIcmsTotal(cip.getVlBase().setScale(2, RoundingMode.HALF_UP).multiply(cip.getPcAliquota().divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.HALF_UP));
    }

    private void calcularValorTotal(CalcularIcmsParam cip) {

        ParametroCliente parametroCliente = cip.getCalculoFrete().getParametroCliente();

        BigDecimal vlDesconto = null;

        if ((parametroCliente == null) || Boolean.FALSE.equals(cip.getCalculoFrete().getBlDescontoTotal())) {
            vlDesconto = cip.getCalculoFrete().getVlDesconto();
        } else if (BigDecimalUtils.hasValue(parametroCliente.getPcDescontoFreteTotal())) {
            vlDesconto = cip.getVlTotalParcelas().multiply(BigDecimalUtils.percent(parametroCliente.getPcDescontoFreteTotal()));
        }

        if (vlDesconto == null) {
            vlDesconto = BigDecimalUtils.ZERO;
        } else {
            vlDesconto = BigDecimalUtils.round(vlDesconto);
        }

        if (ConstantesExpedicao.CALCULO_CORTESIA.equals(cip.getCalculoFrete().getTpCalculo())) {
            vlDesconto = cip.getVlTotalParcelas().multiply(BigDecimalUtils.percent((BigDecimal)this.configuracoesFacade.getValorParametro(ConstantesExpedicao.PC_DESCONTO_FRETE_CORTESIA)));
        }

        if(ConstantesExpedicao.CALCULO_MANUAL.equals(cip.getCalculoFrete().getTpCalculo())) {

            vlDesconto =  BigDecimalUtils.round(BigDecimalUtils.defaultBigDecimal(cip.getCalculoFrete().getVlDesconto()));

            BigDecimal valorFrete = cip.getVlTotalParcelas().add(cip.getVlTotalServicosAdicionais()).subtract(vlDesconto);

            cip.getCalculoFrete().setVlTotalParcelas(valorFrete);
            cip.getCalculoFrete().setVlTotal(valorFrete);
        }else{

            cip.getCalculoFrete().setVlTotal(cip.getVlTotalParcelas().add(cip.getVlTotalServicosAdicionais()).subtract(vlDesconto));
        }

        cip.getCalculoFrete().setVlDesconto(vlDesconto);

        cip.setVlBase(cip.getCalculoFrete().getVlTotal().subtract(cip.getVlParcelaPedagioSemIncidencia()));
    }

    private void calcularValorParcelas(CalcularIcmsParam cip, BigDecimal pcEmbuteInterna) {
        cip.setVlTotalParcelas(BigDecimal.ZERO);

        for(ParcelaServico parcelaServico : cip.getCalculoFrete().getParcelas()) {
            calcularValorParcelasSingular(cip, parcelaServico, pcEmbuteInterna);
            cip.addVlTotalParcelas(parcelaServico.getVlParcela());
        }

        if (cip.getCalculoFrete().getServicosAdicionais() != null) {
            for(ParcelaServico parcelaServico : cip.getCalculoFrete().getServicosAdicionais()) {
                calcularValorParcelasSingular(cip, parcelaServico, pcEmbuteInterna);
                cip.addVlTotalServicosAdicionais(parcelaServico.getVlParcela());
            }
        }
    }

    private void calcularValorParcelasSingular(CalcularIcmsParam cip, ParcelaServico parcelaServico, BigDecimal pcEmbuteInterna) {

        BigDecimal vlIcmsParcela;
        CalculoFrete calculoFrete = cip.getCalculoFrete();

        if(parcelaServico.getVlBrutoParcela() == null) {
            parcelaServico.setVlBrutoParcela(parcelaServico.getVlParcela());
        }

        if(calculoFrete.getTabelaPreco() != null && Boolean.TRUE.equals(calculoFrete.getTabelaPreco().getBlIcmsDestacado())
                && CompareUtils.gt(calculoFrete.getTributo().getPcAliquota(), BigDecimalUtils.ZERO) && parcelaServico.getVlParcela() != null) {

            cip.setVlBase(cip.getVlBase().add(parcelaServico.getVlParcela()));
            cip.setVlTotalParcelasComIcms(cip.getVlTotalParcelasComIcms().add(parcelaServico.getVlParcela()));

        }else {

            if (parcelaServico.getVlBrutoParcela() != null && ((parcelaServico.getParcelaPreco().getCdParcelaPreco().equals(ConstantesExpedicao.CD_PEDAGIO)
                    && cip.getBlIncidenciaIcmsPedagio().equals(Boolean.TRUE))
                    || !parcelaServico.getParcelaPreco().getCdParcelaPreco().equals(ConstantesExpedicao.CD_PEDAGIO)
            )) {
                vlIcmsParcela = parcelaServico.getVlBrutoParcela().multiply(pcEmbuteInterna.divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.HALF_UP);
            } else {
                vlIcmsParcela = BigDecimalUtils.ZERO;

                cip.addVlParcelaPedagioSemIncidencia(parcelaServico.getVlBrutoParcela());
            }

            EmbuteIcmsParcela(cip, parcelaServico, vlIcmsParcela);
        }
    }

    private void EmbuteIcmsParcela(CalcularIcmsParam cip,
                                   ParcelaServico parcelaServico, BigDecimal vlIcmsParcela) {
        if (cip.getBlEmbuteIcmsParcelas().equals(Boolean.TRUE) && cip.getBlAceitaSubstituicao().equals(Boolean.TRUE)) {
            parcelaServico.setVlParcela(BigDecimalUtils.defaultBigDecimal(parcelaServico.getVlBrutoParcela()).add(vlIcmsParcela));

            if(CompareUtils.gt(vlIcmsParcela, BigDecimalUtils.ZERO)) {
                cip.setVlBase(cip.getVlBase().add(parcelaServico.getVlParcela()));
            }
        } else if (cip.getBlEmbuteIcmsParcelas().equals(Boolean.FALSE)) {
            cip.setVlTotalParcelasComIcms(cip.getVlTotalParcelasComIcms().add(parcelaServico.getVlParcela().add(vlIcmsParcela)));
        }
    }

    public boolean verificaIsCalculoDifal(CalcularIcmsParam calcularIcmsParam) {

        Long idUfDestino = this.unidadeFederativaService.findUFByIdMunicipio(calcularIcmsParam.getCalculoFrete().
                getRestricaoRotaDestino().getIdMunicipio()).getIdUnidadeFederativa();
        Long idUfOrigem  = calcularIcmsParam.getCalculoFrete().getRestricaoRotaOrigem().getIdUnidadeFederativa();
        String tipoFrete = calcularIcmsParam.getTpFrete();
        BigDecimal pcAliquota = calcularIcmsParam.getPcAliquota();
        YearMonthDay dtEmissao = calcularIcmsParam.getDtEmissao();
        BigDecimal pcAliquotaInterna = new BigDecimal(0);

        AliquotaIcms aliquotaIcmsInterna = this.aliquotaIcmsInterna(idUfDestino, calcularIcmsParam.getTpSituacaoTributariaRemetente(),
                calcularIcmsParam.getTpSituacaoTributariaDestinatario(), tipoFrete, dtEmissao);

        if(aliquotaIcmsInterna != null) {
            pcAliquotaInterna = aliquotaIcmsInterna.getPcAliquota();
        }

        if(pcAliquota.compareTo(BigDecimal.ZERO) > 0) {

            return !Objects.equals(idUfOrigem, idUfDestino) &&
                    ConstantesExpedicao.TP_FRETE_FOB.equals(tipoFrete) &&
                    clienteResponsavelNaoContribuinte(calcularIcmsParam.getTpSituacaoTributariaResponsavel()) &&
                    (pcAliquotaInterna.compareTo(pcAliquota) > 0);
        }

        return false;

    }

    public AliquotaIcms aliquotaIcmsInterna(Long idUfDestino, String tipoSituacaoTributariaRemetente,
                                            String tipoSituacaoTributariaDestinatario, String tipoFrete, YearMonthDay dtEmissao) {

        return this.aliquotaIcmsService.findAliquotaIcms(idUfDestino, idUfDestino, tipoSituacaoTributariaRemetente,
                tipoSituacaoTributariaDestinatario, tipoFrete, dtEmissao);

    }

    private boolean clienteResponsavelNaoContribuinte(String tipoSituacaoTributaria) {

        ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("TP_SIT_TRIBUT_NAO_CONTRIB",false);

        if(parametroGeral != null) {
            return parametroGeral.getDsConteudo().contains(tipoSituacaoTributaria);
        }

        return false;

    }

    public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
        this.unidadeFederativaService = unidadeFederativaService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setAliquotaIcmsService(AliquotaIcmsService aliquotaIcmsService) {
        this.aliquotaIcmsService = aliquotaIcmsService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }
}
