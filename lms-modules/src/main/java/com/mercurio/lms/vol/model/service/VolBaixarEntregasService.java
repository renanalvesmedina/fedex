package com.mercurio.lms.vol.model.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.lob.BlobImpl;
import org.joda.time.DateTime;
import org.springframework.transaction.UnexpectedRollbackException;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.entrega.model.ComprovanteEntrega;
import com.mercurio.lms.entrega.model.GrauRiscoPerguntaResposta;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.ComprovanteEntregaService;
import com.mercurio.lms.entrega.model.service.GrauRiscoPerguntaRespostaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaVolumeService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregasOnTimeService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregasService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolRecusas;
import com.mercurio.lms.vol.utils.VolFomatterUtil;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.vol.volBaixarEntregasService"
 */
public class VolBaixarEntregasService {

    private static final String TP_FORMA_BAIXA_CELULAR = "C";
    private Logger log = LogManager.getLogger(this.getClass());
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    private OcorrenciaEntregaService ocorrenciaEntregaService;
    private ComprovanteEntregaService comprovanteEntregaService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    private VolRecusasService volRecusasService;
    private ManifestoEntregaService manifestoEntregaService;
    private DadosComplementoService dadosComplementoService;
    private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
    private RegistrarBaixaEntregasService registrarBaixaEntregasService;
    private ManifestoService manifestoService;
    private RegistrarBaixaEntregasOnTimeService registrarBaixaEntrefaOnTimeService;
    private GrauRiscoPerguntaRespostaService grauRiscoPerguntaRespostaService;

    public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
        this.dadosComplementoService = dadosComplementoService;
    }

    private DomainValueService domainValueService;

    public DomainValueService getDomainValueService() {
        return domainValueService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void executeBaixa(TypedFlatMap tfm) {
        Long idDoctoServico = tfm.getLong("idDoctoServico");
        Long idManifesto = tfm.getLong("idManifesto");
        Short cdOcorrenciaEntrega = tfm.getShort("cdOcorrencia");
        String nmRecebedor = tfm.getString("nmRecebedor").replaceAll("[_]", " ");
        String cdComplementoMotivo = tfm.getString("cdComplementoMotivo");
        String dcma = tfm.getString("dcma");
        String rg = tfm.getString("rg");
        final String tpFormaBaixa = TP_FORMA_BAIXA_CELULAR;
        final String tpEntregaParcial = tfm.getString("tpEntregaParcial");
        final DateTime dhOcorrencia;
        final List<Map<String, Object>> volumes = tfm.getList("volumes");
        final List <Long> idsVolumesVaixados = this.obetmIdsVolumesBaixados(volumes);
        String grauParentesco = tfm.getString("grauParentesco");
        if (tfm.getString("dhOcorrencia") != null) {
            //LMS-3569
            Manifesto manifesto = (Manifesto) manifestoService.findById(idManifesto);
            Filial filial = manifesto.getControleCarga().getFilialByIdFilialOrigem();
            dhOcorrencia = VolFomatterUtil.formatStringToDateTime(tfm.getString("dhOcorrencia"), filial);
        } else {
            dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();
        }

        final String obManifesto = "Baixa por celular";
        final Boolean isValidExistenciaPceRemetente = false;
        final Boolean isValidExistenciaPceDestinatario = false;

        if ("S".equalsIgnoreCase(dcma)) {
            ManifestoEntregaDocumento manifestoEntregaDocumento = findManifestoEntregaDocumento(idDoctoServico, idManifesto);

            if (manifestoEntregaDocumento != null && manifestoEntregaDocumento.getDhOcorrencia() == null) {
                DoctoServico doctoServico = new DoctoServico();
                doctoServico.setIdDoctoServico(idDoctoServico);

                /**
                 * se a entrega exige complementoMotivo o celular sempre envia o
                 * complementoMotivo, então somente chamará
                 * dadosComplementoService.executeOcorrenciaSPPCliente se
                 * realmente for necessário, uma vez que o método exige o
                 * complemento motivo.
                 */
                if (StringUtils.isNotBlank(cdComplementoMotivo)) {
                    dadosComplementoService.executeOcorrenciaSPPCliente(doctoServico, new DomainValue(cdComplementoMotivo));
                }

                try {
                    manifestoEntregaDocumentoService.executeBaixaDocumento(idManifesto, idDoctoServico, cdOcorrenciaEntrega, tpFormaBaixa, tpEntregaParcial,
                            dhOcorrencia, nmRecebedor, obManifesto, isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario, rg, idsVolumesVaixados,
                            grauParentesco);
                } catch (UnexpectedRollbackException e) {
                    // LMS-4332 nesses casos deve silenciar a exceção e não
                    // exibir a mensagem na tela.
                    log.error(e);
                }
            }
        } else {
            List<ManifestoEntregaVolume> listManVolme = manifestoEntregaVolumeService.findByManifestoAndDoctoServico(idManifesto, idDoctoServico);
            Long[] idsArray = new Long[listManVolme.size()];
            int ct = 0;
            for (ManifestoEntregaVolume manifestoEntregaVolume : listManVolme) {
                idsArray[ct] = manifestoEntregaVolume.getIdManifestoEntregaVolume();
                ct++;
            }
            getRegistrarBaixaEntregasService().storeOcorrenciaOnListManifestoEntregaVolume(
                    idsArray,
                    cdOcorrenciaEntrega,
                    tpFormaBaixa,
                    dhOcorrencia,
                    isValidExistenciaPceDestinatario,
                    isValidExistenciaPceRemetente);
        }

    }

    public void executarBaixaEntregaAerea(List<Long> ids) {
        for (Long idDoctoServico : ids) {
            Short cdOcorrenciaEntrega = ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA_AEROPORTO;
            Boolean isValidExistenciaPceDestinatario = Boolean.TRUE;
            Boolean isValidExistenciaPceRemetente = Boolean.TRUE;
            String nmRecebedor = "";
            String obManifesto = "";

            registrarBaixaEntrefaOnTimeService.executeConfirmation(idDoctoServico, cdOcorrenciaEntrega, nmRecebedor, obManifesto, isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario, new DomainValue(), TP_FORMA_BAIXA_CELULAR);
        }
    }

    public VolRecusas generateRegistroRecusa(Short cdOcorrenciaEntrega, ManifestoEntregaDocumento manifestoEntregaDocumento) {
        VolRecusas recusa = null;

        if (manifestoEntregaDocumento != null) {
            recusa = volRecusasService.findByIdManifestoEntregaDocumento(manifestoEntregaDocumento.getIdManifestoEntregaDocumento());

            OcorrenciaEntrega ocorrenciaEntrega = this.findOcorrenciaEntrega(cdOcorrenciaEntrega);
            if (ocorrenciaEntrega.getTpOcorrencia().getValue().equals("R")) {
                if (recusa == null) {
                    recusa = new VolRecusas();
                    recusa.setManifestoEntregaDocumento(manifestoEntregaDocumento);
                    recusa.setFilial(manifestoEntregaDocumento.getManifestoEntrega().getFilial());
                }
                recusa.setDhRecusa(JTDateTimeUtils.getDataHoraAtual());
                recusa.setTpRecusa(domainValueService.findDomainValueByValue("DM_STATUS_RECUSA", "N"));
                recusa.setOcorrenciaEntrega(ocorrenciaEntrega);
                volRecusasService.store(recusa);
            } else {
                if (recusa != null) {
                    recusa.setDhResolucao(JTDateTimeUtils.getDataHoraAtual());
                    recusa.setTpRecusa(domainValueService.findDomainValueByValue("DM_STATUS_RECUSA", "E"));
                    recusa.setOcorrenciaEntrega(ocorrenciaEntrega);
                    volRecusasService.store(recusa);
                }
            }
        }
        return recusa;
    }

    private OcorrenciaEntrega findOcorrenciaEntrega(Short cdOcorrenciaEntrega) {
        Map criteria = new HashMap();
        criteria.put("cdOcorrenciaEntrega", cdOcorrenciaEntrega);
        List list = ocorrenciaEntregaService.find(criteria);
        if (list.size() > 0) {
            return (OcorrenciaEntrega) list.get(0);
        }
        return null;
    }

    private ManifestoEntregaDocumento findManifestoEntregaDocumento(Long idDoctoServico, Long idManifestoEntrega) {
        Map criteria = new HashMap();
        criteria.put("doctoServico.idDoctoServico", idDoctoServico);
        criteria.put("manifestoEntrega.idManifestoEntrega", idManifestoEntrega);
        List list = manifestoEntregaDocumentoService.find(criteria);
        if (list.size() > 0) {
            return (ManifestoEntregaDocumento) list.get(0);
        }
        return null;
    }

    public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }

    public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
        this.ocorrenciaEntregaService = ocorrenciaEntregaService;
    }

    public void setVolRecusasService(VolRecusasService volRecusasService) {
        this.volRecusasService = volRecusasService;
    }

    public ComprovanteEntregaService getComprovanteEntregaService() {
        return comprovanteEntregaService;
    }

    public void setComprovanteEntregaService(ComprovanteEntregaService comprovanteEntregaService) {
        this.comprovanteEntregaService = comprovanteEntregaService;
    }

    public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
        return notaFiscalConhecimentoService;
    }

    public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }

    public ManifestoEntregaService getManifestoEntregaService() {
        return manifestoEntregaService;
    }

    public void setManifestoEntregaService(
            ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public void setManifestoEntregaVolumeService(ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
        this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
    }

    public ManifestoEntregaVolumeService getManifestoEntregaVolumeService() {
        return manifestoEntregaVolumeService;
    }

    public void setRegistrarBaixaEntregasService(RegistrarBaixaEntregasService registrarBaixaEntregasService) {
        this.registrarBaixaEntregasService = registrarBaixaEntregasService;
    }

    public RegistrarBaixaEntregasService getRegistrarBaixaEntregasService() {
        return registrarBaixaEntregasService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public RegistrarBaixaEntregasOnTimeService getRegistrarBaixaEntrefaOnTimeService() {
        return registrarBaixaEntrefaOnTimeService;
    }

    public void setRegistrarBaixaEntrefaOnTimeService(RegistrarBaixaEntregasOnTimeService registrarBaixaEntrefaOnTimeService) {
        this.registrarBaixaEntrefaOnTimeService = registrarBaixaEntrefaOnTimeService;
    }    
    

    public GrauRiscoPerguntaRespostaService getGrauRiscoPerguntaRespostaService() {
		return grauRiscoPerguntaRespostaService;
	}

	public void setGrauRiscoPerguntaRespostaService(GrauRiscoPerguntaRespostaService grauRiscoPerguntaRespostaService) {
		this.grauRiscoPerguntaRespostaService = grauRiscoPerguntaRespostaService;
	}

    public void saveComprovanteEntrega(TypedFlatMap map) {
        Long idDoctoServico = map.getLong("idDoctoServico");
        String image = map.getString("image"); 
       
        if (idDoctoServico != null && StringUtils.isNotBlank(image)) {
            try {
                Map<String, Object> criteria = new HashMap<String, Object>();
                criteria.put("idDoctoServico", idDoctoServico);
                
                List list = getComprovanteEntregaService().find(criteria);

                if (list == null || list.isEmpty()) {
                    Blob blobImg = new BlobImpl(Base64Util.decode(image));

                    List<NotaFiscalConhecimento> notasFiscaisConhecimento = getNotaFiscalConhecimentoService().findByConhecimento(idDoctoServico);

                    for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimento) {
                        criteria.put("idNotaFiscalConhecimento", notaFiscalConhecimento.getIdNotaFiscalConhecimento());

                        list = getComprovanteEntregaService().find(criteria);

                        if (list != null && list.size() > 0) {
                            continue;
                        }
                        
                        ComprovanteEntrega comprovanteEntrega = new ComprovanteEntrega();
                        
                        comprovanteEntrega.setAssinatura(blobImg);
                        comprovanteEntrega.setBlEnviado(Boolean.FALSE);
                        comprovanteEntrega.setIdDoctoServico(idDoctoServico);
                        comprovanteEntrega.setIdNotaFiscalConhecimento(notaFiscalConhecimento.getIdNotaFiscalConhecimento());
                                               
                        getComprovanteEntregaService().storeAssinatura(comprovanteEntrega);
                        this.savePerguntasGrauRisco(map, comprovanteEntrega.getIdComprovanteEntrega());
                    }
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
    
	private void savePerguntasGrauRisco(TypedFlatMap map, Long idComprovanteEntrega) {
		List<Map<String, Object>> perguntas = map.getList("perguntasRiscoResposta");
		if(perguntas != null) {
			for(Map pergunta : perguntas) {
				Long idPergunta = Long.valueOf(pergunta.get("idPergunta").toString());
				String resposta = pergunta.get("resposta").toString();
				GrauRiscoPerguntaResposta risco = new GrauRiscoPerguntaResposta();
				risco.setIdComprovanteEntrega(idComprovanteEntrega);
				risco.setIdGrauRiscoPergunta(idPergunta);
				risco.setBlGrauRiscoPerguntaResposta("S".equals(resposta));
				getGrauRiscoPerguntaRespostaService().storeRisco(risco);
			}
		}
	}
	
    private List<Long> obetmIdsVolumesBaixados(List<Map<String, Object>> volumes){    	
    	List<Long> 	idsVolumesBaixados = null;
    	if(volumes != null) {
    		idsVolumesBaixados = new ArrayList<Long> ();    
	    	for(Map volume :volumes ) {
	    		if((Boolean) volume.get("entregue")) {
	    			idsVolumesBaixados.add(Long.valueOf(volume.get("idVolume").toString()));
	    		}
	    	}
    	}
    	return idsVolumesBaixados;
    }
}
