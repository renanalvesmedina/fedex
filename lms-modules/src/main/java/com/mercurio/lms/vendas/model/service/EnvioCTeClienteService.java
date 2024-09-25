package com.mercurio.lms.vendas.model.service;

import br.com.tntbrasil.integracao.domains.vendas.CTeDMN;
import br.com.tntbrasil.integracao.domains.webservice.ParcelasFreteWebService;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.EnvioCteCliente;
import com.mercurio.lms.vendas.model.dao.EnvioCteClienteDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.envioCTeClienteService"
 */
public class EnvioCTeClienteService extends CrudService<EnvioCteCliente, Long> {

    private EnvioCteClienteDAO envioCteClienteDAO;

    Long TABELA_CLIENTE = 5L;
    Long TABELA_CONHECIMENTO = 6L;

    @Transactional
    public CTeDMN enriqueceEdw(CTeDMN cTeDMN) {
        cTeDMN.setParcelasFrete(enriqueceParcelasFrete(cTeDMN.getIdDoctoServico(), cTeDMN.getNrIdentificaoTomador()));
        cTeDMN.setTipoConhecimento(enriqueceTipoConhecimento(cTeDMN.getIdDoctoServico(), cTeDMN.getNrIdentificaoTomador()));
        cTeDMN.setTipoSituacaoDocumento(enriqueceTpSituacaoDocumento(cTeDMN.getIdDoctoServico()).toString());
        cTeDMN.setTipoDocumento("CTE");
        return cTeDMN;
    }

    private List<ParcelasFreteWebService> enriqueceParcelasFrete(Long idDoctoServico, String idCliente) {
        List<Object[]> idsLms = getEnvioCteClienteDAO().findIdLms(idDoctoServico);
        boolean hasTabelaCliente = getEnvioCteClienteDAO().existeTabelaCliente(TABELA_CLIENTE, idCliente);
        List<ParcelasFreteWebService> parcelas = new ArrayList<>();

        if (hasTabelaCliente) {
            for (Object[] idLms : idsLms) {
                List<Object[]> tabela = getEnvioCteClienteDAO().findCliente(TABELA_CLIENTE, idCliente, idLms[0]);
                if (!tabela.isEmpty()) {
                    Object dsParcela = tabela.get(0);
                    parcelas.add(new ParcelasFreteWebService(dsParcela.toString(), idLms[1].toString()));
                }
            }
        } else {
            for (Object[] idLms : idsLms) {
                List<Object[]> tabela = getEnvioCteClienteDAO().findTabelaGenerica(TABELA_CLIENTE, idLms[0]);
                if (!tabela.isEmpty()) {
                    Object dsParcela = tabela.get(0);
                    parcelas.add(new ParcelasFreteWebService(dsParcela.toString(), idLms[1].toString()));
                }
            }
        }

        return parcelas;
    }

    private String enriqueceTipoConhecimento(Long idDoctoServico, String nrIdentificaoTomador){
        Object tpConhecimento = getEnvioCteClienteDAO().findTpConhecimento(idDoctoServico).get(0);
        Object valorDominio = getEnvioCteClienteDAO().findValorDominio(tpConhecimento).get(0);
        boolean hasTabelaConhecimento = getEnvioCteClienteDAO().existeTabelaCliente(TABELA_CONHECIMENTO, nrIdentificaoTomador);

        Object clienteObject;

        if(hasTabelaConhecimento) {
             clienteObject = getEnvioCteClienteDAO().findCliente(TABELA_CONHECIMENTO, nrIdentificaoTomador, valorDominio).get(0);
        } else {
            clienteObject = getEnvioCteClienteDAO().findTabelaGenerica(TABELA_CONHECIMENTO, valorDominio).get(0);
        }

        return clienteObject.toString();
    }

    private Object enriqueceTpSituacaoDocumento(Long idDoctoServico) {
        return getEnvioCteClienteDAO().findTpSituacaoDocumento(idDoctoServico).get(0);
    }

    public EnvioCteClienteDAO getEnvioCteClienteDAO() {
        return (EnvioCteClienteDAO) getDao();
    }

    public void setEnvioCteClienteDAO(EnvioCteClienteDAO envioCteClienteDAO) {
        setDao(envioCteClienteDAO);
    }
}