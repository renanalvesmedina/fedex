package com.mercurio.lms.edi.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.edi.dto.OcorrenciaSelectDTO;
import com.mercurio.lms.edi.dto.TabelaOcorenDetSelectDTO;
import com.mercurio.lms.edi.dto.TipoOcorrenciaDTO;
import com.mercurio.lms.edi.model.EdiTabelaOcoren;
import com.mercurio.lms.edi.model.EdiTabelaOcorenDet;
import com.mercurio.lms.edi.model.dao.EdiTabelaOcorenDAO;
import com.mercurio.lms.edi.model.dao.EdiTabelaOcorenDetDAO;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EdiTabelaOcorenDetService extends CrudService<EdiTabelaOcorenDet, Long> {
    private EdiTabelaOcorenService ediTabelaOcorenService;
    private OcorrenciaEntregaService ocorrenciaEntregaService;
    private OcorrenciaPendenciaService ocorrenciaPendenciaService;
    private EventoService eventoService;
    private LocalizacaoMercadoriaService localizacaoMercadoriaService;
    private DomainValueService domainService;

    public Serializable store(EdiTabelaOcorenDet bean) {
        String[] flOcorrencia = {"", "E", "B", "V", "M"};
        int tpOcorrencia = Integer.parseInt(bean.getTpOcorrencia().getValue());
        bean.setFlOcorrencia(flOcorrencia[tpOcorrencia]);
        return super.store(bean);
    }

    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

    public List<EdiTabelaOcorenDet> findByIdEdiTabelaOcorenDetAndIdEdiTabelaOcoren
            (Long idEdiTabelaOcorenDet, Long idEdiTabelaOcoren){
        return getEdiTabelaOcorenDetDAO()
                .findByIdEdiTabelaOcorenDetAndIdEdiTabelaOcoren(idEdiTabelaOcorenDet, idEdiTabelaOcoren);
    }

    public List<TabelaOcorenDetSelectDTO> findByIdEdiTabelaOcoren(Long idEdiTabelaOcoren){
       List<EdiTabelaOcorenDet> ediTabelaOcorenDetList = getEdiTabelaOcorenDetDAO()
                                                            .findByIdEdiTabelaOcoren(idEdiTabelaOcoren);
       return ediTabelaOcorenDetList.stream().map(etod -> {
                    List<Object[]> ocorrencia =  getEdiTabelaOcorenDetDAO()
                                                .findCodigoOcorrenciaByTpOcorrenciaAndCdOcorrencia
                                                    (
                                                        etod.getTpOcorrencia().getValue(),
                                                        Short.valueOf(etod.getCdOcorrencia())
                                                    );
                    String descricaoOcorrecia = "Ocorrência inativa";
                    if(!ocorrencia.isEmpty()){
                        descricaoOcorrecia = (String)ocorrencia.get(0)[1];
                    }

                    return new TabelaOcorenDetSelectDTO
                        (
                            etod.getIdEdiTabelaOcorenDet(), etod.getEdiTabelaOcoren().getIdEdiTabelaOcoren(),
                            etod.getCdOcorrencia(), descricaoOcorrecia , etod.getCdOcorrenciaCliente(),
                            etod.getTpOcorrencia().getValue(), etod.getTpOcorrencia().getDescriptionAsString(),
                            etod.getDsOcorrencia(), etod.getCdWsDde(), etod.getCdWsExcecao(), etod.getDsWsExcecao()
                        );
                }).collect(Collectors.toList());
    }

    public List<OcorrenciaSelectDTO> findCodigoOcorrenciaByTpOcorrencia(String tipoOcorrencia){
        List<Object[]> ocorrencia = getEdiTabelaOcorenDetDAO().findCodigoOcorrenciaByTpOcorrencia(tipoOcorrencia);
        return ocorrencia.stream()
            .map( ocorr ->
                    new OcorrenciaSelectDTO((Long)ocorr[0], (String)ocorr[1], (String)ocorr[2])
            ).collect(Collectors.toList());
    }

    public List<TipoOcorrenciaDTO> findTipoOcorrenciaByValorDominio(){
        List<DomainValue> domainValueList = domainService.findByDomainName("DM_EDI_TABELA_TP_OCORRENCIA");
        return domainValueList.stream()
                .map(dv -> new TipoOcorrenciaDTO
                        (
                            dv.getValue(),
                            dv.getDescription().getValue())
                    ).collect(Collectors.toList());
    }

    public void setEdiTabelaOcorenDetDAO(EdiTabelaOcorenDetDAO dao) {
        super.setDao(dao);
    }

    private EdiTabelaOcorenDetDAO getEdiTabelaOcorenDetDAO() {
        return  (EdiTabelaOcorenDetDAO)  super.getDao();
    }

    public void setEdiTabelaOcorenService(EdiTabelaOcorenService ediTabelaOcorenService) {
        this.ediTabelaOcorenService = ediTabelaOcorenService;
    }

    public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
        this.ocorrenciaEntregaService = ocorrenciaEntregaService;
    }

    public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
        this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
    }

    public void setEventoService(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
        this.localizacaoMercadoriaService = localizacaoMercadoriaService;
    }

    public void setDomainService(DomainValueService domainService) {
        this.domainService = domainService;
    }
}
