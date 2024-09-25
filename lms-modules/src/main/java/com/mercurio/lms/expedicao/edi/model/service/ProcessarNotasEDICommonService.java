package com.mercurio.lms.expedicao.edi.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.expedicao.model.CCEItem;
import com.mercurio.lms.util.BigDecimalUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.math.BigDecimal;
import java.util.*;

public class ProcessarNotasEDICommonService {

    private NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService;

    public String getChaveNfeFromMap(Integer nrNotaFiscal, List<String> chavesNfe) {
        String chave = null;
        if(chavesNfe != null && !chavesNfe.isEmpty()){
            for(String chaveNfe : chavesNfe){
                if(nrNotaFiscal.equals(Integer.valueOf(chaveNfe.substring(26,34)))){
                    chave = chaveNfe;
                }
            }
        }
        return chave;
    }

    /**
     * Regra 4 ET - 28.02.01.03
     *
     * Somar os volumes de cada grupo de notas e lançar o total na primeira nota
     * do agrupamento. Se a soma resultar um valor fracionado, sempre arredondar
     * para cima. As demais notas devem ficar com a qtde de volumes = 0
     *
     * @param list
     * @return
     */
    public List<NotaFiscalEdi> executeFiltroNotas(final List<NotaFiscalEdi> list) {

        final List<NotaFiscalEdi> listResult = new ArrayList<NotaFiscalEdi>();

        final List<Long> listInform = new ArrayList<Long>();

        List<NotaFiscalEdi> lsTransform;

        for (final NotaFiscalEdi notaFiscalEdi : list) {

            if(listInform.contains(notaFiscalEdi.getCnpjDest())) {
                continue;
            }

            final Predicate predicate = new Predicate() {

                public boolean evaluate(final Object object) {
                    final NotaFiscalEdi nf = (NotaFiscalEdi) object;
                    return notaFiscalEdi.getCnpjDest().equals(nf.getCnpjDest());
                }
            };

            lsTransform = new ArrayList<NotaFiscalEdi>();

            CollectionUtils.select(list, predicate, lsTransform);

            /* Ordena as notas através do numero de etiqueta */
            Collections.sort(lsTransform, new Comparator<NotaFiscalEdi>() {

                public int compare(final NotaFiscalEdi nf1, final NotaFiscalEdi nf2) {
                    if(nf1.getNrEtiquetaInicial() != null) {
                        return -1;
                    }
                    return 1;
                }

            });

            /* Soma os valores de volumes */
            this.executeSomaVolumes(lsTransform);

            /*
             * Verifica se os dados de volumes informados são os mesmos
             * existentes na nota da base
             */
            final NotaFiscalEdi nfEDI = lsTransform.get(0);
            if(!nfEDI.getQtdeVolumes().equals(nfEDI.getQtVolumeInformado())) {
                throw new BusinessException("LMS-04224");
            }

            /* Adiciona todas as notas na lista de retorno */
            listResult.addAll(lsTransform);
            listInform.add(nfEDI.getCnpjDest());

        }

        return listResult;
    }

    /**
     * Efetua a soma dos volumes nas notas fiscais digitadas na tela de
     * processamento do EDI
     *
     * @param list
     * @return NotaFiscalEdi
     */
    public void executeSomaVolumes(final List<NotaFiscalEdi> listTransform) {

        NotaFiscalEdi result = null;

        for (final NotaFiscalEdi notaFiscalEdi : listTransform) {

            if(result == null) {
                result = notaFiscalEdi;
            } else {
                result.setQtdeVolumes(BigDecimalUtils.add(result.getQtdeVolumes(), notaFiscalEdi.getQtdeVolumes()));
                result.setQtVolumeInformado(BigDecimalUtils.add(result.getQtVolumeInformado(), notaFiscalEdi.getQtVolumeInformado()));
                notaFiscalEdi.setQtdeVolumes(BigDecimal.ZERO);
            }

        }

        /* Arredonda o valor da soma dos volumes */
        result.setQtdeVolumes(BigDecimalUtils.round(result.getQtdeVolumes(), 2, BigDecimalUtils.ROUND_UP));

    }

    public Map<String, String> createMapChaveNFEXCae(List<CCEItem> itens) {
        Map<String, String> retorno = new HashMap<String, String>();
        for (CCEItem item : itens) {
            if (!retorno.containsKey(item.getNrChave())) {
                retorno.put(item.getNrChave(), item.getNrCae());
            }
        }
        return retorno;
    }

    public NotaFiscalExpedicaoEDIService getNotaFiscalExpedicaoEDIService() {
        return notaFiscalExpedicaoEDIService;
    }

    public void setNotaFiscalExpedicaoEDIService(NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService) {
        this.notaFiscalExpedicaoEDIService = notaFiscalExpedicaoEDIService;
    }
}
