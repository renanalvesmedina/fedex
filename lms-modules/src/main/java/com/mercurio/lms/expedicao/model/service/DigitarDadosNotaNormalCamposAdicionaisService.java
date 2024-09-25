package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.expedicao.dto.CampoAdicionalConhecimentoDto;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.expedicao.model.NfDadosComp;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;


/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.expedicao.digitarDadosNotaNormalCamposAdicionaisService"
 */
public class DigitarDadosNotaNormalCamposAdicionaisService {

    private static final String STR_DS_CAMPO_COM_NOTA_FISCAL = "- Nota Fiscal:";
    private static final String CONHECIMENTO = "conhecimento";
    private static final String CAMPO_ADICIONAL_CONHECIMENTO = "campoAdicionalConhecimento";
    private static final String DS_CAMPO = "dsCampo";
    private static final String DS_VALOR_CAMPO = "dsValorCampo";
    private static final String ID = "id";
    private static final String ID_INFORMACAO_DOC_SERVICO = "idInformacaoDocServico";

    private InformacaoDocServicoService informacaoDocServicoService;

    public Map storeInSession(Map parameters) {
        Map<String, Object> result = new HashMap<String, Object>();
        List lista = (List) parameters.get(CAMPO_ADICIONAL_CONHECIMENTO);
        Conhecimento conhecimento = (Conhecimento) parameters.get(CONHECIMENTO);

        if (conhecimento.getDadosComplementos() != null && conhecimento.getDadosComplementos().size() > 0){
            for (Iterator iter = conhecimento.getDadosComplementos().iterator(); iter.hasNext();) {
                DadosComplemento complemento = (DadosComplemento) iter.next();
                if (complemento.getInformacaoDocServico()==null){
                    iter.remove();
                }
            }
        }

        List<DadosComplemento> listDadosComplemento = new ArrayList<DadosComplemento>();
        for (Iterator iter = lista.iterator(); iter.hasNext();) {
            storeInSessionSwt(conhecimento, listDadosComplemento, iter);
        }
        if( conhecimento.getDadosComplementos() == null ){
            conhecimento.setDadosComplementos(new ArrayList<DadosComplemento>());
        }
        conhecimento.getDadosComplementos().addAll(listDadosComplemento);

        result.put(CONHECIMENTO, conhecimento);
        return result;
    }

    private void storeInSessionSwt(Conhecimento conhecimento, List<DadosComplemento> listDadosComplemento, Iterator iter) {
        Map fields = (Map) iter.next();
        String dsCampo = (String) fields.get(DS_CAMPO);
        String dsValorCampo = null;
        Object objValorCampo = fields.get(DS_VALOR_CAMPO);
        if (objValorCampo != null) {
            dsValorCampo = objValorCampo.toString();
        }
        Long idInformacaoDoctoCliente = (Long) fields.get(ID);
        Long idInformacaoDocServico = (Long)fields.get(ID_INFORMACAO_DOC_SERVICO);

        DadosComplemento dadosComplemento = new DadosComplemento();
        dadosComplemento.setDsValorCampo(dsValorCampo);

        if(idInformacaoDocServico != null){
            InformacaoDocServico informacaoDocServico = informacaoDocServicoService
                    .findById(idInformacaoDocServico);
            dadosComplemento.setInformacaoDocServico(informacaoDocServico);

            addIfNotExistsByIdInformacaoDocServico(listDadosComplemento, dadosComplemento);
        }

        if(idInformacaoDoctoCliente != null){
            InformacaoDoctoCliente informacaoDoctoCliente = new InformacaoDoctoCliente();
            informacaoDoctoCliente.setIdInformacaoDoctoCliente(idInformacaoDoctoCliente);
            dadosComplemento.setInformacaoDoctoCliente(informacaoDoctoCliente);

            addIfNotExistsByIdInformacaoDoctoCliente(listDadosComplemento, dadosComplemento);
        }

        if (dsCampo != null && dsCampo.contains(STR_DS_CAMPO_COM_NOTA_FISCAL)){
            int indice = dsCampo.indexOf(STR_DS_CAMPO_COM_NOTA_FISCAL);
            String strNumero = dsCampo.substring(0, indice).trim();
            int numero = Integer.parseInt(strNumero);
            NfDadosComp nfDadosComp = new NfDadosComp();
            if (conhecimento.getNotaFiscalConhecimentos() != null && conhecimento.getNotaFiscalConhecimentos().get(numero-1) != null){
                nfDadosComp.setNotaFiscalConhecimento((NotaFiscalConhecimento) conhecimento.getNotaFiscalConhecimentos().get(numero-1));
            }
            nfDadosComp.setDadosComplemento(dadosComplemento);
            dadosComplemento.addNfDadosComps(nfDadosComp);
        }
    }

    public void storeInSessionForEdi(Conhecimento conhecimento, List<CampoAdicionalConhecimentoDto> campoAdicionalConhecimento) {

        removerIteracaoConhecimento(conhecimento);

        List<DadosComplemento> listDadosComplemento = new ArrayList<DadosComplemento>();
        List<String> listDadosComplementoAgrupamentoNf = new ArrayList<String>();

        for (CampoAdicionalConhecimentoDto fields : campoAdicionalConhecimento) {
            String dsCampo = fields.getDsCampo();
            String dsValorCampo = fields.getDsValorCampo();

            Long idInformacaoDoctoCliente =  fields.getId();
            Long idInformacaoDocServico   =  fields.getIdInformacaoDocServico();

            DadosComplemento dadosComplemento = new DadosComplemento();
                dadosComplemento.setDsValorCampo(dsValorCampo);

            addInformacaoDoctoServico(listDadosComplemento, idInformacaoDocServico, dadosComplemento);

            addInformacaoDoctoCliente(listDadosComplemento, listDadosComplementoAgrupamentoNf, dsCampo,
                    idInformacaoDoctoCliente, dadosComplemento);

            if (dsCampo != null && dsCampo.contains(STR_DS_CAMPO_COM_NOTA_FISCAL)) {
                int indice = dsCampo.indexOf(STR_DS_CAMPO_COM_NOTA_FISCAL);
                String strNumero = dsCampo.substring(0, indice).trim();
                int numero = Integer.parseInt(strNumero);
                NfDadosComp nfDadosComp = new NfDadosComp();
                if (conhecimento.getNotaFiscalConhecimentos() != null) {
                    NotaFiscalConhecimento notaFiscalConhecimento = fetchNotaFiscalConhecimento(conhecimento, numero);
                    nfDadosComp.setNotaFiscalConhecimento(notaFiscalConhecimento);
                }
                nfDadosComp.setDadosComplemento(dadosComplemento);
                dadosComplemento.addNfDadosComps(nfDadosComp);

            }

        }
        if (conhecimento.getDadosComplementos() == null) {
            conhecimento.setDadosComplementos(new ArrayList<DadosComplemento>());
        }
        conhecimento.getDadosComplementos().addAll(listDadosComplemento);

    }

    public Map storeInSessionForEdi(Map parameters) {
        Map<String, Object> result = new HashMap<String, Object>();
        List lista = (List) parameters.get(CAMPO_ADICIONAL_CONHECIMENTO);
        Conhecimento conhecimento = (Conhecimento) parameters.get(CONHECIMENTO);

        removerIteracaoConhecimento(conhecimento);

        List<DadosComplemento> listDadosComplemento = new ArrayList<DadosComplemento>();
        List<String> listDadosComplementoAgrupamentoNf = new ArrayList<String>();

        for (Iterator iter = lista.iterator(); iter.hasNext(); ) {
            Map fields = (Map) iter.next();
            String dsCampo = (String) fields.get(DS_CAMPO);
            String dsValorCampo = null;
            Object objValorCampo = fields.get(DS_VALOR_CAMPO);
            if (objValorCampo != null) {
                dsValorCampo = objValorCampo.toString();
            }
            Long idInformacaoDoctoCliente = (Long) fields.get(ID);
            Long idInformacaoDocServico = (Long) fields.get(ID_INFORMACAO_DOC_SERVICO);

            DadosComplemento dadosComplemento = new DadosComplemento();
            dadosComplemento.setDsValorCampo(dsValorCampo);

            addInformacaoDoctoServico(listDadosComplemento, idInformacaoDocServico, dadosComplemento);

            addInformacaoDoctoCliente(listDadosComplemento, listDadosComplementoAgrupamentoNf, dsCampo,
                    idInformacaoDoctoCliente, dadosComplemento);

            if (dsCampo != null && dsCampo.contains(STR_DS_CAMPO_COM_NOTA_FISCAL)) {
                int indice = dsCampo.indexOf(STR_DS_CAMPO_COM_NOTA_FISCAL);
                String strNumero = dsCampo.substring(0, indice).trim();
                int numero = Integer.parseInt(strNumero);
                NfDadosComp nfDadosComp = new NfDadosComp();
                if (conhecimento.getNotaFiscalConhecimentos() != null) {
                    NotaFiscalConhecimento notaFiscalConhecimento = fetchNotaFiscalConhecimento(conhecimento, numero);
                    nfDadosComp.setNotaFiscalConhecimento(notaFiscalConhecimento);
                }
                nfDadosComp.setDadosComplemento(dadosComplemento);
                dadosComplemento.addNfDadosComps(nfDadosComp);
                //listDadosComplemento.add(dadosComplemento);
            }
            
            /*if (Boolean.TRUE.equals(conhecimento.getBlRedespachoIntermediario())){
                for (NotaFiscalConhecimento notaFiscalConhecimento : conhecimento.getNotaFiscalConhecimentos()) {
                    NfDadosComp nfDadosComp = new NfDadosComp();
                    nfDadosComp.setNotaFiscalConhecimento(notaFiscalConhecimento);
                    nfDadosComp.setDadosComplemento(dadosComplemento);
                    dadosComplemento.addNfDadosComps(nfDadosComp);
                }
            }*/
            
        }
        if (conhecimento.getDadosComplementos() == null) {
            conhecimento.setDadosComplementos(new ArrayList<DadosComplemento>());
        }
        conhecimento.getDadosComplementos().addAll(listDadosComplemento);

        result.put(CONHECIMENTO, conhecimento);
        return result;
    }

    private NotaFiscalConhecimento fetchNotaFiscalConhecimento(Conhecimento conhecimento, int numero) {
        NotaFiscalConhecimento notaFiscalConhecimentoAux = new NotaFiscalConhecimento();
        for (NotaFiscalConhecimento notaFiscalConhecimento : conhecimento.getNotaFiscalConhecimentos()) {
            if (notaFiscalConhecimento.getNrNotaFiscal().equals(numero)) {
                notaFiscalConhecimentoAux = notaFiscalConhecimento;
            }
        }
        return notaFiscalConhecimentoAux;
    }

    private void addInformacaoDoctoServico(List<DadosComplemento> listDadosComplemento, Long idInformacaoDocServico, DadosComplemento dadosComplemento) {
        if (idInformacaoDocServico != null) {
            InformacaoDocServico informacaoDocServico = informacaoDocServicoService
                    .findById(idInformacaoDocServico);
            dadosComplemento.setInformacaoDocServico(informacaoDocServico);

            addIfNotExistsByIdInformacaoDocServico(listDadosComplemento, dadosComplemento);
        }
    }

    private void addInformacaoDoctoCliente(List<DadosComplemento> listDadosComplemento, List<String> listDadosComplementoAgrupamentoNf, String dsCampo, Long idInformacaoDoctoCliente, DadosComplemento dadosComplemento) {
        if (idInformacaoDoctoCliente != null) {
            InformacaoDoctoCliente informacaoDoctoCliente = new InformacaoDoctoCliente();
            informacaoDoctoCliente.setIdInformacaoDoctoCliente(idInformacaoDoctoCliente);
            dadosComplemento.setInformacaoDoctoCliente(informacaoDoctoCliente);

            //LMSA-4506 - Usado número da nota fiscal para determinar qual o agrupamento deverá ser utilizado
            if (dsCampo != null && dsCampo.contains(STR_DS_CAMPO_COM_NOTA_FISCAL)) {
                int indice = dsCampo.indexOf(STR_DS_CAMPO_COM_NOTA_FISCAL);
                String strNumero = dsCampo.substring(0, indice).trim();
                addIfNotExistsByIdInformacaoDoctoClienteAgrupamento(listDadosComplemento, dadosComplemento, listDadosComplementoAgrupamentoNf, strNumero);
            } else {
                addIfNotExistsByIdInformacaoDoctoCliente(listDadosComplemento, dadosComplemento);
            }
        }
    }

    private void removerIteracaoConhecimento(Conhecimento conhecimento) {
        if (conhecimento.getDadosComplementos() != null && conhecimento.getDadosComplementos().size() > 0) {
            for (Iterator iter = conhecimento.getDadosComplementos().iterator(); iter.hasNext(); ) {
                DadosComplemento complemento = (DadosComplemento) iter.next();
                if (complemento.getInformacaoDocServico() == null) {
                    iter.remove();
                }
            }
        }
    }

    private void addIfNotExistsByIdInformacaoDoctoClienteAgrupamento(List<DadosComplemento> listDadosComplemento, DadosComplemento dadosToAdd,
                                                                     List<String> agregacaoInserida, String nrAgregacao) {
        if (!agregacaoInserida.contains(nrAgregacao + "-" + dadosToAdd.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente())) {
            agregacaoInserida.add(nrAgregacao + "-" + dadosToAdd.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente());
            listDadosComplemento.add(dadosToAdd);
        }
    }


    private void addIfNotExistsByIdInformacaoDoctoCliente(List<DadosComplemento> listDadosComplemento, DadosComplemento dadosToAdd) {
        boolean achou = false;
        for (DadosComplemento dadosComplemento : listDadosComplemento) {
            if (dadosComplemento.getInformacaoDoctoCliente() != null &&
                    dadosComplemento.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente().equals(dadosToAdd.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente())) {
                achou = true;

                // Adiciona a nota fiscal ao dadosComplemento
                if (dadosToAdd.getNfDadosComps() != null && dadosComplemento.getNfDadosComps() != null) {
                    NfDadosComp dadosToAddNF = (NfDadosComp) dadosToAdd.getNfDadosComps().get(0);
                    dadosToAddNF.setDadosComplemento(dadosComplemento);
                    dadosComplemento.addNfDadosComps(dadosToAddNF);
                }

                break;
            }
        }

        if (!achou) {
            listDadosComplemento.add(dadosToAdd);
        }
    }

    private void addIfNotExistsByIdInformacaoDocServico(List<DadosComplemento> listDadosComplemento, DadosComplemento dadosToAdd) {
        boolean achou = false;
        for (DadosComplemento dadosComplemento : listDadosComplemento) {
            if (dadosComplemento.getInformacaoDocServico() != null &&
                    dadosComplemento.getInformacaoDocServico().getIdInformacaoDocServico().equals(dadosToAdd.getInformacaoDocServico().getIdInformacaoDocServico())) {
                achou = true;

                // Adiciona a nota fiscal ao dadosComplemento
                if (dadosToAdd.getNfDadosComps() != null && dadosComplemento.getNfDadosComps() != null) {
                    NfDadosComp dadosToAddNF = (NfDadosComp) dadosToAdd.getNfDadosComps().get(0);
                    dadosToAddNF.setDadosComplemento(dadosComplemento);
                    dadosComplemento.addNfDadosComps(dadosToAddNF);
                }

                break;
            }
        }

        if (!achou) {
            listDadosComplemento.add(dadosToAdd);
        }
    }

    public void setInformacaoDocServicoService(
            InformacaoDocServicoService informacaoDocServicoService) {
        this.informacaoDocServicoService = informacaoDocServicoService;
    }

}
