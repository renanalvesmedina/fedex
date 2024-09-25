package com.mercurio.lms.rest.edi;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.edi.dto.OcorrenciaSelectDTO;
import com.mercurio.lms.edi.dto.TabelaClienteDTO;
import com.mercurio.lms.edi.dto.TabelaOcorenDetSelectDTO;
import com.mercurio.lms.edi.dto.TipoOcorrenciaDTO;
import com.mercurio.lms.edi.model.EdiTabelaCliente;
import com.mercurio.lms.edi.model.EdiTabelaOcoren;
import com.mercurio.lms.edi.model.EdiTabelaOcorenDet;
import com.mercurio.lms.rest.edi.dto.*;
import com.mercurio.lms.vendas.model.Cliente;
import org.apache.logging.log4j.util.Strings;
import org.apache.tools.ant.filters.StringInputStream;

import java.util.List;
import java.util.stream.Collectors;


public abstract class BaseEditTabelaRest extends BaseRest {

    protected EdiTabelaOcoren toEdiTabelaOcorenEntity(EdiTabelaOcorenDTO ediTabelaOcorenDTO){
        EdiTabelaOcoren ediTabelaOcoren = new EdiTabelaOcoren();
        ediTabelaOcoren.setNmTabelaOcoren(ediTabelaOcorenDTO.getNmTabelaOcoren());
        ediTabelaOcoren.setBlWebservice(ediTabelaOcorenDTO.getBlWebservice());
        return ediTabelaOcoren;
    }

    protected List<EdiTabelaOcorenDTO> toListEditTabelaOcorenDTO(List<EdiTabelaOcoren> ediTabelaOcorenList){
        return ediTabelaOcorenList.stream()
                                    .map(eto ->
                                            new EdiTabelaOcorenDTO
                                                    (
                                                        eto.getIdEdiTabelaOcoren(),
                                                        eto.getNmTabelaOcoren(),
                                                        eto.getBlWebservice()
                                                    )
                                    ).collect(Collectors.toList());
    }

    protected EdiTabelaOcorenDet toEdiTabelaOcorenDetEntity(EdiTabelaOcorenDetDTO ediTabelaOcorenDetDTO){
        EdiTabelaOcorenDet ediTabelaOcorenDet = new EdiTabelaOcorenDet();
        ediTabelaOcorenDet.setIdEdiTabelaOcorenDet(ediTabelaOcorenDetDTO.getIdEdiTabelaOcorenDet());
        ediTabelaOcorenDet.setEdiTabelaOcoren(new EdiTabelaOcoren());
        ediTabelaOcorenDet.getEdiTabelaOcoren().setIdEdiTabelaOcoren(ediTabelaOcorenDetDTO.getIdEdiTabelaOcoren());
        ediTabelaOcorenDet.setIdLms(ediTabelaOcorenDetDTO.getIdLms());
        ediTabelaOcorenDet.setCdOcorrencia(ediTabelaOcorenDetDTO.getCdOcorrencia());
        ediTabelaOcorenDet.setDsOcorrencia(ediTabelaOcorenDetDTO.getDsOcorrencia());
        ediTabelaOcorenDet.setCdOcorrenciaCliente(ediTabelaOcorenDetDTO.getCdOcorrenciaCliente());
        DomainValue tpOcorrencia = new DomainValue();
        tpOcorrencia.setValue(ediTabelaOcorenDetDTO.getTpOcorrencia());
        ediTabelaOcorenDet.setTpOcorrencia(tpOcorrencia);
        ediTabelaOcorenDet.setCdWsDde(ediTabelaOcorenDetDTO.getCdWsDde());
        ediTabelaOcorenDet.setCdWsExcecao(ediTabelaOcorenDetDTO.getCdWsExcecao());
        ediTabelaOcorenDet.setDsWsExcecao(ediTabelaOcorenDetDTO.getDsWsExcecao());

        return ediTabelaOcorenDet;
    }

    protected List<EdiTabelaOcorenDetDTO> toListEditTabelaOcorenDetDTO(List<TabelaOcorenDetSelectDTO> ediTabelaOcorenDetList){
        return ediTabelaOcorenDetList.stream()
                    .map(etod -> new EdiTabelaOcorenDetDTO
                        (
                                etod.getIdEdiTabelaOcorenDet(),
                                etod.getIdEdiTabelaOcoren(),
                                etod.getCdOcorrencia(),
                                padded(etod.getCdOcorrencia()) + "-" + etod.getDsCodicoOcorrencia(),
                                etod.getCdOcorrenciaCliente(),
                                etod.getTpOcorrencia(),
                                etod.getDescricaoDomino(),
                                etod.getDsOcorrencia(),
                                etod.getCdWsDde(),
                                etod.getCdWsExcecao(),
                                etod.getDsWsExcecao()
                        )
                    ).collect(Collectors.toList());
    }

    protected EdiTabelaCliente toEdiTabelaClienteEntity(EdiTabelaClienteDTO ediTabelaClienteDTO){
        EdiTabelaCliente ediTabelaCliente = new EdiTabelaCliente();
        ediTabelaCliente.setEdiTabelaOcoren(new EdiTabelaOcoren());
        ediTabelaCliente.getEdiTabelaOcoren().setIdEdiTabelaOcoren(ediTabelaClienteDTO.getIdEdiTabelaOcoren());
        ediTabelaCliente.setIdEdiTabelaCliente(ediTabelaClienteDTO.getIdEdiTabelaCliente());
        ediTabelaCliente.setIdCliente(ediTabelaClienteDTO.getIdCliente());

        return ediTabelaCliente;
    }

    protected List<EdiTabelaClienteDTO> toListEdiTabelaClienteDTO(List<TabelaClienteDTO> tabelaClienteDTO){
        return tabelaClienteDTO.stream()
                .map(tc -> new EdiTabelaClienteDTO
                        (
                            tc.getIdEdiTabelaCliente(),
                            tc.getIdEdiTabelaOcoren(),
                            tc.getIdCliente(),
                            tc.getNrIdentificacao(),
                            tc.getNomePessoa()
                        )
                    ).collect(Collectors.toList());
    }

    protected List<ComponentCodigoOcorreciaDTO> toListComponentCodigoOcorrecia
        (List<OcorrenciaSelectDTO> ocorrenciaSelectDTOList){

        return ocorrenciaSelectDTOList.stream()
                .map(os -> new ComponentCodigoOcorreciaDTO
                        (
                            os.getIdLms(),
                            os.getCdOcorrencia(),
                                padded(os.getCdOcorrencia()) + "-" + os.getDsOcorrencia()
                        )
                ).collect(Collectors.toList());
    }

    protected List<ComponentTipoOcorrenciaDTO> toListComponentTipoOcorrenciaDTO
        (List<TipoOcorrenciaDTO> tipoOcorrenciaDTOList){

        return tipoOcorrenciaDTOList.stream()
                .map(to -> new ComponentTipoOcorrenciaDTO
                        (
                            to.getValorDominio(),
                            to.getDescricaoDomino()
                        )
                    ).collect(Collectors.toList());
    }

    public PessoaDTO toPessoaDTO(Pessoa pessoa){
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setNomePossoa(pessoa.getNmPessoa());
        pessoaDTO.setIdCliente(pessoa.getIdPessoa());
        return pessoaDTO;
    }

    private String padded(String value){
        return "000".substring(value.length()) + value;
    }
}
