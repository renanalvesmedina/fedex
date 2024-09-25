package com.mercurio.lms.expedicao.util;

import com.mercurio.lms.edi.dto.ProcessarEdiDTO;
import com.mercurio.lms.expedicao.dto.*;
import com.mercurio.lms.expedicao.model.Conhecimento;
import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessarEdiUtils{
    private DozerBeanMapper mapper = new DozerBeanMapper();

    public List<ProcessaNotasEdiItemDto> toListProcessaNotasEdiItemDto(List<ProcessarEdiDTO> processarEdiDTOList){
        if (processarEdiDTOList.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProcessaNotasEdiItemDto>  processaNotasEdiItem = processarEdiDTOList
                .stream()
                .map(pe -> {
                    ProcessaNotasEdiItemDto processaNotasEdiItemDto = mapper.map(pe, ProcessaNotasEdiItemDto.class);
                    processaNotasEdiItemDto.gerarUuid();
                    Conhecimento conhecimento = (Conhecimento)pe.getParameters().get("conhecimento");
                    processaNotasEdiItemDto.getParameters()
                            .getConhecimento().setTpCalculoPreco(conhecimento.getTpCalculoPreco().getValue());
                    return processaNotasEdiItemDto;
                })
                .collect(Collectors.toList());
        return processaNotasEdiItem;
    }

    public ProcessarEdiDTO toProcessarEdiDTO(ProcessaNotasEdiItemDto processaNotasEdiItemDto) {
        ProcessarEdiDTO processarEdiDTO = mapper.map(processaNotasEdiItemDto, ProcessarEdiDTO.class);
        Map mapDadosComplementos = new HashMap();
        Map<String, Object> parameters = processarEdiDTO.getParameters();
        ParameterDto parameterDto = processaNotasEdiItemDto.getParameters();
        mapDadosComplementos.put("campoAdicionalConhecimento", parameterDto.getMapDadosComplementos().getCampoAdicionalConhecimento());
        parameters.put("mapDadosComplementos", mapDadosComplementos);
        parameters.put("notaFiscalConhecimento", parameterDto.getNotaFiscalConhecimento());
        parameters.put("mapDadosComplementos", mapDadosComplementos);
        parameters.put("meioTransporte", parameterDto.getMeioTransporte());
        parameters.put("mapDataHoraNotaFiscal", parameterDto.getMapDataHoraNotaFiscal());
        parameters.put("conhecimento", toConhecimento(parameterDto.getConhecimento()));
        parameters.put("idFilial", processaNotasEdiItemDto.getIdFilial());
        processarEdiDTO.setParameters(parameters);
;        return processarEdiDTO;
   }

   public Conhecimento toConhecimento(ConhecimentoDto conhecimentoDto) {
        return mapper.map(conhecimentoDto, Conhecimento.class);
   }

    public List<ConhecimentoSemPesagemDto>
    toListConhecimentoSemPesagemDto
        (List<Map<String, Object>> conhecimentoSemPessagem, ProcessaNotasEdiItemDto processaNotasEdiItemDto){

        return conhecimentoSemPessagem.stream()
            .map(csp -> instanciaConhecimntoSemPessagemDto(csp, processaNotasEdiItemDto)).collect(Collectors.toList());

    }

    private ConhecimentoSemPesagemDto instanciaConhecimntoSemPessagemDto
    (Map<String, Object> csp, ProcessaNotasEdiItemDto processaNotasEdiItemDto) {
        ProcessaNotasEdiItemDto processaNotasEdiItem =
            new ProcessaNotasEdiItemDto
                (
                    processaNotasEdiItemDto.getIdFilial(),
                    processaNotasEdiItemDto.getIdUsuario(),
                    processaNotasEdiItemDto.getIdProcessamentoEdi(),
                    processaNotasEdiItemDto.getIdMonitoramentoDescarga(),
                    processaNotasEdiItemDto.getListIdsNotasFiscaisEdiInformada(),
                    processaNotasEdiItemDto.getNrProcessamento(),
                    processaNotasEdiItemDto.getTpProcessamento(),
                    processaNotasEdiItemDto.getIdPedidoColeta(),
                    processaNotasEdiItemDto.getInicioIndex(),
                    processaNotasEdiItemDto.getFinalIndex(),
                    processaNotasEdiItemDto.getParameters()
                );
        return new ConhecimentoSemPesagemDto
            (
                (Long)csp.get("idConhecimento"),
                (Boolean)csp.get("blContingencia"),
                processaNotasEdiItem
            );
    }
}
