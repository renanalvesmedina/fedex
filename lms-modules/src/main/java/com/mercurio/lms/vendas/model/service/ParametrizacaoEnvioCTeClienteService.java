package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.ParametrizacaoEnvioCTeCliente;
import com.mercurio.lms.vendas.model.dao.ParametrizacaoEnvioCTeClienteDAO;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.parametrizacaoEnvioCTeClienteService"
 */
public class ParametrizacaoEnvioCTeClienteService  extends CrudService<ParametrizacaoEnvioCTeCliente, Long> {

    private ParametrizacaoEnvioCTeClienteDAO parametrizacaoEnvioCTeClienteDAO;

    public String findNomeFantasiaCliente(String nrIdentificacao, String tpCnpj){
        String nomeFantasia = null;

        if(tpCnpj.equals("Parcial")) {
            nomeFantasia = getParametrizacaoEnvioCTeClienteDAO().findNomeByCNPJParcial(nrIdentificacao);
        } else if (tpCnpj.equals("Completo")){
            nomeFantasia = getParametrizacaoEnvioCTeClienteDAO().findNomeByCNPJCompleto(nrIdentificacao);
        }

        if(nomeFantasia == null) {
            throw new BusinessException("LMS-23030");
        }

        return nomeFantasia;
    }

    public List<ParametrizacaoEnvioCTeCliente>
        findByNrIdentificacao
            (String tpCnpj, String nrIdentificacao, String dsDiretorioArmazenagem, String tpPesquisa, Boolean blAtivo) {

        List parametrizacaoEnvioCTeCliente = getParametrizacaoEnvioCTeClienteDAO()
                                                .findBy
                                                  (tpCnpj, nrIdentificacao,dsDiretorioArmazenagem, tpPesquisa, blAtivo);

        if(parametrizacaoEnvioCTeCliente == null) {
            throw new BusinessException("LMS-23030");
        }

        return parametrizacaoEnvioCTeCliente;
    }

    @Override
    public Serializable store(ParametrizacaoEnvioCTeCliente dto) {
        ParametrizacaoEnvioCTeCliente parametrizacaoEnvioCTeCliente = getParametrizacaoEnvioCTeClienteDAO().
                find(dto.getNrIdentificacao(), dto.getDsDiretorioArmazenagem(), dto.getTpPesquisa().getValue());


        if (parametrizacaoEnvioCTeCliente == null) {
            dto.setDhInclusao(new DateTime());
            return super.store(dto);
        }

        mapperParametrizacaoEnvioCTeCliente(dto, parametrizacaoEnvioCTeCliente);
        return super.store(parametrizacaoEnvioCTeCliente);
    }

    public String findDsDiretorioArmazenagem
        (
            String nrIdentificaoDestinatario,
            String nrIdentificaoRemetente,
            String nrIdentificaoTomador){

        nrIdentificaoDestinatario = nrIdentificaoDestinatario.substring(0,8);
        nrIdentificaoRemetente    = nrIdentificaoRemetente.substring(0, 8);
        nrIdentificaoTomador      = nrIdentificaoTomador.substring(0, 8);

        List<Map<String, Object>> listResult =  getParametrizacaoEnvioCTeClienteDAO()
                                                    .findDsDiretorioArmazenagem
                                                        (
                                                            nrIdentificaoDestinatario,
                                                            nrIdentificaoRemetente,
                                                            nrIdentificaoTomador
                                                        );

        StringBuilder pastaDestinoXML = new StringBuilder();
        for(Map<String, Object> diretorioArmazenagem : listResult){
            pastaDestinoXML.append((String)diretorioArmazenagem.get("dsDiretorioArmazenagem"));
            pastaDestinoXML.append(";");
        }

        int lengthPastaDestino = pastaDestinoXML.length();
        if(lengthPastaDestino > 0){
            pastaDestinoXML.setLength(lengthPastaDestino - 1);
            return pastaDestinoXML.toString();
        }

        return null;
    }

    public ParametrizacaoEnvioCTeClienteDAO getParametrizacaoEnvioCTeClienteDAO() {
        return (ParametrizacaoEnvioCTeClienteDAO) getDao();
    }

    public void setParametrizacaoEnvioCTeClienteDAO(ParametrizacaoEnvioCTeClienteDAO parametrizacaoEnvioCTeClienteDAO) {
        setDao(parametrizacaoEnvioCTeClienteDAO);
    }

    private void mapperParametrizacaoEnvioCTeCliente
                    (ParametrizacaoEnvioCTeCliente dto, ParametrizacaoEnvioCTeCliente parametrizacaoEnvioCTeCliente){
        parametrizacaoEnvioCTeCliente.setDsDiretorioArmazenagem(dto.getDsDiretorioArmazenagem());
        parametrizacaoEnvioCTeCliente.setTpPesquisa(dto.getTpPesquisa());
        parametrizacaoEnvioCTeCliente.setBlAtivo(dto.getBlAtivo());
    }
}