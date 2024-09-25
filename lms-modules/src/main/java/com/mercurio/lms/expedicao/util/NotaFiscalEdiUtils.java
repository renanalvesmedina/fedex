package com.mercurio.lms.expedicao.util;

import com.mercurio.lms.edi.dto.DadosValidacaoEdiDTO;
import com.mercurio.lms.edi.dto.ValidarEdiDTO;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.expedicao.dto.NotaFiscalEdiDto;
import org.dozer.DozerBeanMapper;
import java.util.List;
import java.util.stream.Collectors;

public class NotaFiscalEdiUtils extends GenericMapperClienteUtil{

    public NotaFiscalEdiDto toDTO(NotaFiscalEdi notaFiscalEdi) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        return mapper.map(notaFiscalEdi, NotaFiscalEdiDto.class);
    }

    public NotaFiscalEdi toEntity(NotaFiscalEdiDto notaFiscalEdiDto) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        return mapper.map(notaFiscalEdiDto, NotaFiscalEdi.class);
    }

    public ValidarEdiDTO toValidarEdiDTO(DadosValidacaoEdiDTO dadosValidacaoEdiDTO){
        DozerBeanMapper mapper = new DozerBeanMapper();
        ValidarEdiDTO validarEdiDTO = mapper.map(dadosValidacaoEdiDTO, ValidarEdiDTO.class);
        return validarEdiDTO;
    }

    public DadosValidacaoEdiDTO toDadosValidacaoEdiDTO(ValidarEdiDTO validarEdiDTO){
        return new DadosValidacaoEdiDTO(validarEdiDTO);
    }

    public List<ValidarEdiDTO> toListValidarEdiDTO(List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTOS) {
        List<ValidarEdiDTO> listaValidarEdiDTO = dadosValidacaoEdiDTOS.stream()
            .map(dve -> toValidarEdiDTO(dve))
            .collect(Collectors.toList());
        return listaValidarEdiDTO;
    }

    public List<DadosValidacaoEdiDTO> toListDadostValidarEdiDTO(List<ValidarEdiDTO> validacaoEdiDTOS) {
        List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTOList = validacaoEdiDTOS.stream()
            .map(dve -> toDadosValidacaoEdiDTO(dve))
            .collect(Collectors.toList());
        return dadosValidacaoEdiDTOList;
    }


}
