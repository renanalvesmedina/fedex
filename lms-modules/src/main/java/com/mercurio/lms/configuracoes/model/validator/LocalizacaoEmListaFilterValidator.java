package com.mercurio.lms.configuracoes.model.validator;

import br.com.tntbrasil.integracao.domains.edw.LocalizacaoEmListaFilterDto;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.util.JTDateTimeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.YearMonthDay;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.localizacaoEmListaFilterValidator"
 */
public class LocalizacaoEmListaFilterValidator {

    public void validate(LocalizacaoEmListaFilterDto filter) {

        if (isAlgumaDtEmissaoPreenchida(filter)) {
            validateDtEmissaoAmbosPreenchidos(filter);

            YearMonthDay dtEmissaoInicial = getYMDFromIsoDateString(filter.getDtEmissaoInicialString());
            YearMonthDay dtEmissaoFinal = getYMDFromIsoDateString(filter.getDtEmissaoFinalString());

            validateDtEmissaoOrdem(dtEmissaoInicial, dtEmissaoFinal);
            validateDtEmissaoDiasPeriodo(dtEmissaoInicial, dtEmissaoFinal);
        }
        validateNumeroDeParametros(filter);
    }

    private YearMonthDay getYMDFromIsoDateString(String isoDateString) {
        return JTDateTimeUtils.convertDataStringToYearMonthDay(isoDateString, DateFormatUtils.ISO_DATE_FORMAT.getPattern());
    }

    private void validateDtEmissaoAmbosPreenchidos(LocalizacaoEmListaFilterDto filter) {
        if (!isAmbasDtEmissaoPreenchidas(filter)) {
            throw new BusinessException("LMS-10045");
        }
    }

    private void validateDtEmissaoOrdem(YearMonthDay dtEmissaoInicial, YearMonthDay dtEmissaoFinal) {
        if (dtEmissaoInicial.isAfter(dtEmissaoFinal)) {
            throw new BusinessException("LMS-00008");
        }
    }

    private void validateDtEmissaoDiasPeriodo(YearMonthDay dtEmissaoInicial, YearMonthDay dtEmissaoFinal) {
        if(dtEmissaoFinal.isAfter(dtEmissaoInicial.plusDays(31))) {
            throw new BusinessException("LMS-10045");
        }
    }

    private void validateNumeroDeParametros(LocalizacaoEmListaFilterDto filter) {
        int nrCamposPreenchidos = 0;

        nrCamposPreenchidos = filter.getNrNotaFiscal() == null ? nrCamposPreenchidos : nrCamposPreenchidos + 1;
        nrCamposPreenchidos = filter.getNrDoctoServico() == null ? nrCamposPreenchidos : nrCamposPreenchidos + 1;
        nrCamposPreenchidos = filter.getNrPedidoCliente() == null ? nrCamposPreenchidos : nrCamposPreenchidos + 1;
        nrCamposPreenchidos = filter.getNrColeta() == null ? nrCamposPreenchidos : nrCamposPreenchidos + 1;
        nrCamposPreenchidos = filter.getSgFilialOrigem() == null ? nrCamposPreenchidos : nrCamposPreenchidos + 1;
        nrCamposPreenchidos = filter.getSgFilialDestino() == null ? nrCamposPreenchidos : nrCamposPreenchidos + 1;
        nrCamposPreenchidos = !isAmbasDtEmissaoPreenchidas(filter) ? nrCamposPreenchidos : nrCamposPreenchidos + 1;
        nrCamposPreenchidos = filter.getNrIdentificacaoRemetente() == null ? nrCamposPreenchidos : nrCamposPreenchidos + 1;
        nrCamposPreenchidos = filter.getNrIdentificacaoDestinatario() == null ? nrCamposPreenchidos : nrCamposPreenchidos + 1;

        if (nrCamposPreenchidos < 2) {
            throw new BusinessException("LMS-10089");
        }
    }

    private boolean isAmbasDtEmissaoPreenchidas(LocalizacaoEmListaFilterDto filter) {
        return filter.getDtEmissaoInicialString() != null && filter.getDtEmissaoFinalString() != null;
    }

    private boolean isAlgumaDtEmissaoPreenchida(LocalizacaoEmListaFilterDto filter) {
        return filter.getDtEmissaoInicialString() != null || filter.getDtEmissaoFinalString() != null;
    }

}