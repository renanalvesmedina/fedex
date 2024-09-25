package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.util.mdfe.converter.v300.InfRespConverter;
import com.mercurio.lms.carregamento.util.mdfe.converter.v300.InfSegConverter;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.constantes.entidades.ConsParametroGeral;
import com.mercurio.lms.edi.enums.CampoNotaFiscalEdiComplementoFedex;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServicoSeguros;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.dao.DoctoServicoSegurosDAO;
import com.mercurio.lms.mdfe.model.v300.InfResp;
import com.mercurio.lms.mdfe.model.v300.InfSeg;
import com.mercurio.lms.mdfe.model.v300.Seg;
import com.mercurio.lms.mdfe.model.v300.types.RespSegType;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServico;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.seguros.model.service.AverbacaoDoctoServicoService;
import com.mercurio.lms.vendas.model.service.SeguroClienteService;

/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.expedicao.doctoServicoSegurosService"
 */
public class DoctoServicoSegurosService extends CrudService<DoctoServicoSeguros, Long> {
	
	//private static final String RESPONSAVEL_SEGURO_CTE_FEDEX = "RESPONSAVEL SEGURO CTE FEDEX";
	//private static final String NOME_SEGURADORA_FEDEX = "NOME SEGURADORA FEDEX";
	//private static final String CNPJ_SEGURADORA_FEDEX = "CNPJ SEGURADORA FEDEX";
	//private static final String APOLICE_SEGURO_CTE_FEDEX = "APOLICE SEGURO CTE FEDEX";
	//private static final String AVERBACAO_CTE_FEDEX = "AVERBACAO CTE FEDEX";

    private static final int ARR_POS_TP_PESSOA = 4;
    private static final int ARR_POS_NR_IDENTIFICACAO = 3;
    private static final int LENGTH_X_SEG = 30;
    private static final String TP_WEBSERVICE_E = "E";
    private static final int LENGTH_APOL_AVER = 40;

    private SeguroClienteService seguroClienteService;
    private ApoliceSeguroService apoliceSeguroService;
    private ParametroGeralService parametroGeralService;
    private DadosComplementoService dadosComplementoService;
    private AverbacaoDoctoServicoService averbacaoDoctoServicoService;

    /**
     * Recupera uma instância de <code>DoctoServicoSeguros</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    public DoctoServicoSeguros findById(java.lang.Long id) {
        return (DoctoServicoSeguros) super.findById(id);
    }

    /**
     * Apaga uma entidade através do Id.
     *
     * @param id indica a entidade que deverá ser removida.
     */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    /**
     * Apaga várias entidades através do Id.
     *
     * @param ids lista com as entidades que deverão ser removida.
     */
    @Override
    @ParametrizedAttribute(type = Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     *
     * @param bean entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable store(DoctoServicoSeguros bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setDoctoServicoSegurosDAO(DoctoServicoSegurosDAO dao) {
        setDao(dao);
    }


    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DoctoServicoSegurosDAO getDoctoServicoSegurosDAO() {
        return (DoctoServicoSegurosDAO) getDao();
    }


    /**
     * Busca DoctoServicoSeguro pelo idDoctoServico
     * <p>
     * Jira LMS-3996
     *
     * @param idDoctoServico id do documento de serviço
     * @return {@link DoctoServicoSeguros}
     */
    public DoctoServicoSeguros findByIdDoctoServico(Long idDoctoServico) {
        return getDoctoServicoSegurosDAO().findByIdDoctoServico(idDoctoServico);
    }

    /**
     * Gerar seguros mdfe
     *
     * @param mdfe Objeto seguro mdfe
     * @return List
     */
    public List<Seg> gerarSegurosMdfe(ManifestoEletronico mdfe) {
    	
        List<Seg> retorno = new ArrayList<Seg>();
        
        List<Seg> segListRedespacho = new ArrayList<Seg>();
        List<Seg> segList = new ArrayList<Seg>();

        for (Conhecimento conhecimento : mdfe.getConhecimentos()) {
        	 if (Boolean.TRUE.equals(conhecimento.getBlRedespachoIntermediario())) {
        		 segListRedespacho.add(gerarSegRedespachoIntermediario(conhecimento));
        	 } else {
        		 segList.add(gerarSeg(conhecimento));
        	 }
        }
        
        retorno.addAll(segListRedespacho);
        retorno.addAll(agruparPorApolice(segList));

        return retorno;
    }
    
    private List<Seg> agruparPorApolice(List<Seg> segList) {

        Map<String, Seg> segAgrupadosApolice = new HashMap<String, Seg>();

        for (Seg seg : segList) {
            if (!segAgrupadosApolice.containsKey(seg.getnApol())) {
                segAgrupadosApolice.put(seg.getnApol(), seg);
            }
        }

        return new ArrayList<Seg>(segAgrupadosApolice.values());
    }

    private Seg gerarSeg(final Conhecimento conhecimento) {
        Seg seg = new Seg();

        DoctoServicoSeguros doctoServicoSeguros = findByIdDoctoServico(conhecimento.getIdDoctoServico());
        Long idServico = conhecimento.getServico().getIdServico();
        Long idCliente = conhecimento.getDevedorDocServs().get(0).getCliente().getIdCliente();
        AverbacaoDoctoServico averbacaoDoctoServico = averbacaoDoctoServicoService
                .findByIdDoctoServicoAndTpWebservice(conhecimento.getIdDoctoServico(), TP_WEBSERVICE_E);

        if (doctoServicoSeguros == null) {
            //Seguro TNT
            String pmTpSeguroObrigatorio = parametroGeralService.findSimpleConteudoByNomeParametro(ConsParametroGeral.TP_SEG_OBRIGATORIO);
            List list = apoliceSeguroService.findSegValues(pmTpSeguroObrigatorio);

            if (CollectionUtils.isEmpty(list)) {
                throw new BusinessException("LMS-04561", new Object[]{pmTpSeguroObrigatorio});
            }

            seg = trataInfSeg(seg, list);
            seg = trataInfResp(RespSegType.VALUE_1, seg, list);
            seg = trataNApol(seg, list);
            seg = trataNAver(seg, averbacaoDoctoServico);
        } else {
            List list = seguroClienteService.findSegValues(idServico, idCliente, conhecimento.getDhEmissao());
            seg = trataInfSeg(seg, list);
            seg = trataInfResp(RespSegType.VALUE_2, seg, list);
            seg = trataNApol(seg, list);
            seg = trataNAver(seg, averbacaoDoctoServico);
        }

        return seg;
    }

    /**
     * Transforma o retorno da chamada que obtém os dados do Seguro
     *
     * @param seg  informação do seguro
     * @param list lista de informações
     * @return {@link Seg}
     */
    private Seg trataInfSeg(Seg seg, List list) {
        if (list != null && !list.isEmpty()) {
            Object[] o = (Object[]) list.get(0);

            String xSeg = (String) o[0];
            if (xSeg.length() > LENGTH_X_SEG) {
                xSeg = xSeg.substring(0, LENGTH_X_SEG);
            }

            String cnpj = (String) o[ARR_POS_NR_IDENTIFICACAO];

            InfSeg infSeg = new InfSegConverter(xSeg, cnpj).convert();
            seg.setInfSeg(infSeg);
        }

        return seg;
    }

    private Seg trataInfResp(com.mercurio.lms.mdfe.model.v300.types.RespSegType respSegType, Seg seg, List list) {
        if (list != null && !list.isEmpty()) {
            Object[] o = (Object[]) list.get(0);

            String cnpj = null;
            String cpf = null;
            String tpPessoa = (String) o[ARR_POS_TP_PESSOA];
            String nrIdentificacao = (String) o[ARR_POS_NR_IDENTIFICACAO];
            if ("J".equalsIgnoreCase(tpPessoa)) {
                cnpj = nrIdentificacao;
            } else {
                cpf = nrIdentificacao;
            }

            InfResp infResp = new InfRespConverter(respSegType, cnpj, cpf).convert();
            seg.setInfResp(infResp);
        }

        return seg;
    }

    private Seg trataNApol(Seg seg, List list) {
        if (list != null && !list.isEmpty()) {
            Object[] o = (Object[]) list.get(0);
            seg.setnApol((String) o[1]);
        }

        return seg;
    }

    private Seg trataNAver(Seg seg, AverbacaoDoctoServico averbacaoDoctoServico) {
        if (averbacaoDoctoServico != null && averbacaoDoctoServico.getNrAverbacao() != null) {
            seg.setnAver(averbacaoDoctoServico.getNrAverbacao());
        } else if (averbacaoDoctoServico != null && averbacaoDoctoServico.getNrProtocolo() != null) {
            //TODO - Contingência para averbações da averbweb. Alterar o serviço e depois remover.
            seg.setnAver(averbacaoDoctoServico.getNrProtocolo());
        } else {
            String nrAverbMDFE = parametroGeralService.findConteudoByNomeParametro("NUMERO_AVERB_MDFE", false).toString();
            seg.setnAver(nrAverbMDFE);
        }

        return seg;
    }
    
    private Seg gerarSegRedespachoIntermediario(final Conhecimento conhecimento) {
    	Seg seg = new Seg();
    	
		List<String> dsComplementoList = new ArrayList<String>();
		dsComplementoList.add(CampoNotaFiscalEdiComplementoFedex.RESPONSAVEL_SEGURO_CTE_FEDEX.getNomeCampo());
		dsComplementoList.add(CampoNotaFiscalEdiComplementoFedex.NOME_SEGURADORA_FEDEX.getNomeCampo());
		dsComplementoList.add(CampoNotaFiscalEdiComplementoFedex.CNPJ_SEGURADORA_FEDEX.getNomeCampo());
		dsComplementoList.add(CampoNotaFiscalEdiComplementoFedex.APOLICE_SEGURO_CTE_FEDEX.getNomeCampo());
		dsComplementoList.add(CampoNotaFiscalEdiComplementoFedex.AVERBACAO_CTE_FEDEX.getNomeCampo());
		
		Map<String, String> mapDadosCompl = new HashMap<String, String>();
		List<Map<String, Object>> listDadosCompl = dadosComplementoService.findByIdConhecimentoDocCliente(conhecimento.getIdDoctoServico(), dsComplementoList);
		if(listDadosCompl != null) {
			for (Map<String, Object> dadoCompl : listDadosCompl) {
				mapDadosCompl.put((String)dadoCompl.get("key"), (String)dadoCompl.get("value"));
			}
		
		seg = trataInfSegRedespachoIntermediario(seg, mapDadosCompl);
		seg = trataInfRespRedespachoIntermediario(RespSegType.VALUE_2, seg, mapDadosCompl);
		seg = trataNApolRedespachoIntermediario(seg, mapDadosCompl);
		seg = trataNAverRedespachoIntermediario(seg, mapDadosCompl);
		}
		return seg;
    }
    
    private Seg trataInfSegRedespachoIntermediario(Seg seg, Map<String, String> mapDadosCompl) {
    	if (mapDadosCompl != null) {
    		
    		String xSeg = mapDadosCompl.get(CampoNotaFiscalEdiComplementoFedex.NOME_SEGURADORA_FEDEX.getNomeCampo());
    		if (xSeg.length() > LENGTH_X_SEG) {
    			xSeg = xSeg.substring(0, LENGTH_X_SEG);
    		}
    		
    		String cnpj = mapDadosCompl.get(CampoNotaFiscalEdiComplementoFedex.CNPJ_SEGURADORA_FEDEX.getNomeCampo());
    		
    		InfSeg infSeg = new InfSegConverter(xSeg, cnpj).convert();
    		seg.setInfSeg(infSeg);
    	}
    	
    	return seg;
    }
    
    private Seg trataInfRespRedespachoIntermediario(com.mercurio.lms.mdfe.model.v300.types.RespSegType respSegType, Seg seg, Map<String, String> mapDadosCompl) {
    	if (mapDadosCompl != null) {
    		
    		String cnpj = mapDadosCompl.get(CampoNotaFiscalEdiComplementoFedex.RESPONSAVEL_SEGURO_CTE_FEDEX.getNomeCampo());
    		String cpf = null;
    		
    		InfResp infResp = new InfRespConverter(respSegType, cnpj, cpf).convert();
    		seg.setInfResp(infResp);
    	}
    	
    	return seg;
    }
    
    private Seg trataNApolRedespachoIntermediario(Seg seg, Map<String, String> mapDadosCompl) {
    	if (mapDadosCompl != null) {
    		
    		String nApol = mapDadosCompl.get(CampoNotaFiscalEdiComplementoFedex.APOLICE_SEGURO_CTE_FEDEX.getNomeCampo());
    		if (nApol.length() > LENGTH_APOL_AVER) {
    			nApol = nApol.substring(0, LENGTH_APOL_AVER);
    		}
    		
    		seg.setnApol(nApol);
    	}
    	
    	return seg;
    }
    
    private Seg trataNAverRedespachoIntermediario(Seg seg, Map<String, String> mapDadosCompl) {
    	String nAver = mapDadosCompl.get(CampoNotaFiscalEdiComplementoFedex.AVERBACAO_CTE_FEDEX.getNomeCampo());
		if (nAver.length() > LENGTH_APOL_AVER) {
			nAver = nAver.substring(0, LENGTH_APOL_AVER);
		}
    	seg.setnAver(nAver);
    	return seg;
    }

    public void setSeguroClienteService(SeguroClienteService seguroClienteService) {
        this.seguroClienteService = seguroClienteService;
    }

    public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
        this.apoliceSeguroService = apoliceSeguroService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setAverbacaoDoctoServicoService(AverbacaoDoctoServicoService averbacaoDoctoServicoService) {
        this.averbacaoDoctoServicoService = averbacaoDoctoServicoService;
    }


	public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}
}