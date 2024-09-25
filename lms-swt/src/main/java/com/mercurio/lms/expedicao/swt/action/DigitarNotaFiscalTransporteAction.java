package com.mercurio.lms.expedicao.swt.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoNFT;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.ImpostoServicoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalTransporteService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tributos.model.service.AliquotaIssMunicipioServService;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Luis Carlos Poletto
 * 
 *         Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser utilizado para
 *         referenciar este serviço.
 * @spring.bean id="lms.expedicao.swt.digitarNotaFiscalTransporteAction"
 */
public class DigitarNotaFiscalTransporteAction extends DigitarNotaAction {
    private NotaFiscalTransporteService notaFiscalTransporteService;
    private ImpostoServicoService impostoServicoService;
    private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
    private AliquotaIssMunicipioServService aliquotaIssMunicipioServService;

    /**
     * Carrega e Valida Dados
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map validateNFTPrimeiraFase(Map parameters) {
        Map<String, Object> result = new HashMap<String, Object>();
        Long idServicoTributo = LongUtils.getLong(
                (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.ID_SERVICO_TRIBUTO_NFT));
        Long idMunicipioSede = SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio()
                .getIdMunicipio();
        Conhecimento conhecimentoFromTela = (Conhecimento) parameters.get("conhecimento");
        Long idMunicipioServico = conhecimentoFromTela.getMunicipioByIdMunicipioEntrega().getIdMunicipio();

        // Quest CQPRO00031163 - Jira 757
        Conhecimento conhecimento = null;
        TypedFlatMap emiteNfServico = aliquotaIssMunicipioServService.findEmiteNfServico(null, idServicoTributo,
                idMunicipioSede, idMunicipioServico);
        String tpDocumentoServico = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE;
        if (Boolean.TRUE.equals(emiteNfServico.getBoolean("BlEmiteNFeletronica"))) {
            tpDocumentoServico = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA;
        }
        conhecimento = createConhecimentoPersistente(parameters, new DomainValue(tpDocumentoServico));

        CalculoNFT calculoNFT = new CalculoNFT();
        calculoNFT.setIdServico(conhecimento.getServico().getIdServico());
        calculoNFT.setBlCalculaParcelas(conhecimento.getBlParcelas());
        calculoNFT.setBlCalculaServicosAdicionais(conhecimento.getBlServicosAdicionais());

        try {
            WarningCollectorUtils.remove();
            notaFiscalTransporteService.validateNotaPrimeiraFase(conhecimento, calculoNFT);
            parameters.put("conhecimento", conhecimento);
            parameters.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoNFT);
        } catch (BusinessException businessException) {
            result.put("conhecimento", conhecimento);
            result.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoNFT);
            result.put("exception", businessException);
            return result;
        }

        return validateNFTSegundaFase(parameters);
    }

    /**
     * Validações
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map validateNFTSegundaFase(Map parameters) {
        Map<String, Object> result = new HashMap<String, Object>();
        Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
        try {
            CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
            notaFiscalTransporteService.validateNotaSegundaFase(conhecimento, calculoFrete);
            result.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoFrete);
            result.put("conhecimento", conhecimento);
            result.put("idFilialDestino", conhecimento.getFilialByIdFilialDestino().getIdFilial());
            result.put("sgFilialDestino", conhecimento.getFilialByIdFilialDestino().getSgFilial());

            WarningCollectorUtils.putAll(result);

            return result;

        } catch (BusinessException businessException) {
            result.put("exception", businessException);
            result.put("conhecimento", conhecimento);
            result.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION,
                    parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION));
            return result;
        }
    }

    /*
     * GETTERS E SETTERS
     */
    public void setNotaFiscalTransporteService(NotaFiscalTransporteService notaFiscalTransporteService) {
        this.notaFiscalTransporteService = notaFiscalTransporteService;
    }

    public ImpostoServicoService getImpostoServicoService() {
        return impostoServicoService;
    }

    public void setImpostoServicoService(ImpostoServicoService impostoServicoService) {
        this.impostoServicoService = impostoServicoService;
    }

    public MonitoramentoDocEletronicoService getMonitoramentoDocEletronicoService() {
        return monitoramentoDocEletronicoService;
    }

    public void setMonitoramentoDocEletronicoService(
            MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
        this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
    }

    public void setAliquotaIssMunicipioServService(AliquotaIssMunicipioServService aliquotaIssMunicipioServService) {
        this.aliquotaIssMunicipioServService = aliquotaIssMunicipioServService;
    }

}
