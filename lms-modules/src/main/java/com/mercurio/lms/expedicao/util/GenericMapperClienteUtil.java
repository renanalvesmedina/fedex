package com.mercurio.lms.expedicao.util;

import com.mercurio.lms.edi.dto.DadosValidacaoEdiDTO;
import com.mercurio.lms.edi.dto.ValidarEdiDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public abstract class GenericMapperClienteUtil {

    private PropertyMap<DadosValidacaoEdiDTO, ValidarEdiDTO> mapearPropriedadeValidarEdiDTO() {
        return new PropertyMap<DadosValidacaoEdiDTO, ValidarEdiDTO>() {
            @Override
            protected void configure() {
                map().getClienteRemetente().setIdCliente(source.getClienteRemetente().getIdCliente());
                map().getClienteRemetente().setBlAtualizaDestinatarioEdi(source.getClienteRemetente().getBlAtualizaDestinatarioEdi());
                map().getClienteRemetente().setBlObrigaPesoCubadoEdi(source.getClienteRemetente().getBlObrigaPesoCubadoEdi());

            }
        };
    }

    protected ModelMapper modelMapearPropriedadeValidarEdiDTO() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(mapearPropriedadeValidarEdiDTO());
        return modelMapper;
    }

   /* private PropertyMap<ProcessarEdiDTO, ProcessaNotasEdiItemDMN> mapearPropriedadeProcessaNotasEdiItemDMN() {
        return new PropertyMap<ProcessarEdiDTO, ProcessaNotasEdiItemDMN>() {

            @Override
            protected void configure() {
                map().getClienteRemetente().setIdCliente(source.getClienteRemetente().getIdCliente());
                map().getClienteRemetente().setBlAtualizaDestinatarioEdi(source.getClienteRemetente().getBlAtualizaDestinatarioEdi());
                map().getClienteRemetente().setBlObrigaPesoCubadoEdi(source.getClienteRemetente().getBlObrigaPesoCubadoEdi());
            }
        };

    }*/


    protected ModelMapper modelMapearPropriedadeProcessaNotasEdiItemDMN() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
       // modelMapper.addMappings(mapearPropriedadeProcessaNotasEdiItemDMN());
        return modelMapper;
    }

    private PropertyMap<ValidarEdiDTO, DadosValidacaoEdiDTO> mapearPropriedadeDadosValidacaoEdiDTO() {
        return new PropertyMap<ValidarEdiDTO, DadosValidacaoEdiDTO>() {
            @Override
            protected void configure() {
                map().getClienteRemetente().setIdCliente(source.getClienteRemetente().getIdCliente());
                map().getClienteRemetente().setBlAtualizaDestinatarioEdi(source.getClienteRemetente().getBlAtualizaDestinatarioEdi());
                map().getClienteRemetente().setBlObrigaPesoCubadoEdi(source.getClienteRemetente().getBlObrigaPesoCubadoEdi());

            }
        };
    }

    protected ModelMapper modelMapearPropriedadeDadosValidacaoEdiDTO() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(mapearPropriedadeDadosValidacaoEdiDTO());
        return modelMapper;
    }

}
