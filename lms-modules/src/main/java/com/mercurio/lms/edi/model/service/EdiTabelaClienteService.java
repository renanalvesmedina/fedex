package com.mercurio.lms.edi.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.dto.TabelaClienteDTO;
import com.mercurio.lms.edi.model.EdiTabelaCliente;
import com.mercurio.lms.edi.model.EdiTabelaOcoren;
import com.mercurio.lms.edi.model.EdiTabelaOcorenDet;
import com.mercurio.lms.edi.model.dao.EdiTabelaClienteDAO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

import java.io.Serializable;
import java.util.List;

public class EdiTabelaClienteService extends CrudService<EdiTabelaCliente, Long> {

    private EdiTabelaOcorenService ediTabelaOcorenService;
    private PessoaService pessoaService;

    public Serializable store(EdiTabelaCliente bean) {
        return super.store(bean);
    }

    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

    public List<EdiTabelaCliente> findByIdEdiTabelaClienteAndIdEdiTabelaOcoren
        (long idEdiTabelaCliente, Long idEdiTabelaOcoren){

        return getEdiTabelaClienteDAO()
                .findByIdEdiTabelaClienteAndIdEdiTabelaOcoren(idEdiTabelaCliente, idEdiTabelaOcoren);
    }

    public Pessoa findNomePessoaByNumeroIdentificacao(String nrIdentificacao) throws Exception {
        Pessoa pessoa = pessoaService.findByNrIdentificacao(nrIdentificacao);
        if (pessoa == null){
            throw new Exception("Nenhum registro foi encontrado para este dado informado.");
        }

        return pessoa;
    }

    public List<TabelaClienteDTO> findByIdEdiTabelaOcoren(Long idEdiTabelaOcoren){
        return getEdiTabelaClienteDAO().findByIdEdiTabelaOcoren(idEdiTabelaOcoren);
    }

    public void setEdiTabelaClienteDAO(EdiTabelaClienteDAO dao) {
        super.setDao(dao);
    }

    private EdiTabelaClienteDAO getEdiTabelaClienteDAO() {
        return  (EdiTabelaClienteDAO)  super.getDao();
    }

    public void setEdiTabelaOcorenService(EdiTabelaOcorenService ediTabelaOcorenService) {
        this.ediTabelaOcorenService = ediTabelaOcorenService;
    }

    public void setPessoaService(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }



}
