package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

import java.math.BigDecimal;
import java.util.List;

public class ExecuteWorkflowDifalService {

    private ParametroGeralService parametroGeralService;
    private OcorrenciaPendenciaService ocorrenciaPendenciaService;
    private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
    private ConhecimentoService	conhecimentoService;

    public void executeWorkflow(List<Long> idsSolicitacoes, List<String> tpsSituacoes) {

        for (int i = 0; i < idsSolicitacoes.size(); i++) {

            Conhecimento conhecimento = this.conhecimentoService.getConhecimentoDAO().findById(idsSolicitacoes.get(i));

            if (ConstantesWorkflow.APROVADO.equals(tpsSituacoes.get(i))) {

                conhecimento.getPendencia().setTpSituacaoPendencia(new DomainValue(ConstantesWorkflow.APROVADO));
                conhecimento.setTpSituacaoPendencia(new DomainValue(ConstantesWorkflow.APROVADO));
                this.conhecimentoService.store(conhecimento);
                this.conhecimentoService.flushConhecimento();

                BigDecimal cdOcorrenciaLiberacao = (BigDecimal)parametroGeralService.
                        findConteudoByNomeParametroWithoutException(ConstantesExpedicao.NM_PARAMETRO_CD_OCORRENCIA_LIB_DIFAL, false);
                OcorrenciaPendencia ocorrencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf(cdOcorrenciaLiberacao.toString()));
                this.ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(conhecimento.getIdDoctoServico(),
                        ocorrencia.getIdOcorrenciaPendencia(), null, JTDateTimeUtils.getDataHoraAtual(), null);

            } else if (ConstantesWorkflow.REPROVADO.equals(tpsSituacoes.get(i))) {
                conhecimento.getPendencia().setTpSituacaoPendencia(new DomainValue(ConstantesWorkflow.REPROVADO));
                this.conhecimentoService.store(conhecimento);
                this.conhecimentoService.flushConhecimento();
            }
        }
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
        this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
    }

    public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
        this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }
}
