package com.mercurio.lms.seguros.model.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.util.XMLUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.tntbrasil.integracao.domains.expedicao.DocumentoEnvioAverbacao;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoRetornoAverbacao;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServicoMdfe;
import com.mercurio.lms.seguros.model.dao.AverbacaoDoctoServicoMdfeDAO;
import com.mercurio.lms.util.DocumentoEnvioAverbacaoHelper;

/**
 * Classe de serviço para CRUD: 
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.anexoDoctoServicoService"
 */
@SuppressWarnings("deprecation")
public class AverbacaoDoctoServicoMdfeService extends CrudService<AverbacaoDoctoServicoMdfe, Long> {

    public static final String AVERBACAO_DOCTO_SERVICO_MDFE = "AVERBACAO_DOCTO_SERVICO_MDFE";
    private static final String TP_SITUACAO_DECLARA_MDFE = "declaraMDFe";
    private static final String TP_ATEM = "ATEM";
    private static final String TP_SITUACAO_MDFE_AUTORIZADO = "E";
    private static final String TP_SITUACAO_MDFE_CANCELADO = "C";
    private static final String TP_SITUACAO_MDFE_ENCERRADO = "G";
    private static final String PADRAO_DATA_BRASIL = "dd/MM/yyyy HH:mm:ss";

    private ConhecimentoService conhecimentoService;
	
	/**
	 * Recupera uma instância de <code>AnexoDoctoServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public AverbacaoDoctoServicoMdfe findById(java.lang.Long id) {
		return (AverbacaoDoctoServicoMdfe)super.findById(id);
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
	 * Recupera uma coleção de instância de <code>AnexoDoctoServico</code> a partir dos IDs.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Coleção de instâncias que possuem os ids informados.
	 */
	public List<AverbacaoDoctoServicoMdfe> findByIds(Set<java.lang.Long> ids) {
		if(!ids.isEmpty()){
			return getAverbacaoDoctoServicoDAO().findByIds(ids);
		}
		return new ArrayList<AverbacaoDoctoServicoMdfe>();
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(AverbacaoDoctoServicoMdfe bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param dao Instância do DAO.
	 */
	public void setAverbacaoDoctoServicoDAO(AverbacaoDoctoServicoMdfeDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	public AverbacaoDoctoServicoMdfeDAO getAverbacaoDoctoServicoDAO() {
		return (AverbacaoDoctoServicoMdfeDAO) getDao();
	}
        
    public void updateDataEnvioMdfe(Long idAverbacao, String tpWebservice){
        getAverbacaoDoctoServicoDAO().updateDataEnvioMdfe(idAverbacao, tpWebservice);
    }

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void storeDadosRetornoAverbacaoMdfe(DocumentoRetornoAverbacao averbacao, AverbacaoDoctoServicoMdfe mdfe) {
		preencherDadosRetornoAverbacaoDocumento(mdfe, averbacao);
		store(mdfe);
	}
    
    public List<AverbacaoDoctoServicoMdfe> findMonitoramentoAverbacoes(Map<String, Object> criteria) {
        return getAverbacaoDoctoServicoDAO().findMonitoramentoAverbacoes(criteria);
    }
    
    public List<Map<String,Object>> findMonitoramentoAverbacoesBySql(Map<String,Object> criteria){
    	return getAverbacaoDoctoServicoDAO().findMonitoramentoAverbacoesBySql(criteria);
    }
    
    private void preencherDadosRetornoAverbacaoDocumento(AverbacaoDoctoServicoMdfe adsm, DocumentoRetornoAverbacao retorno) {
        if (TP_SITUACAO_MDFE_AUTORIZADO.equals(retorno.getTipo())) {
            this.preencherDadosRetornoDeclaracaoMdfeAutorizado(adsm, retorno);
            return;
        } 
        if (TP_SITUACAO_MDFE_CANCELADO.equals(retorno.getTipo())) {
            this.preencherDadosRetornoDeclaracaoMdfeCancelado(adsm, retorno);
            return;
        } 
        if (TP_SITUACAO_MDFE_ENCERRADO.equals(retorno.getTipo())) {
            this.preencherDadosRetornoDeclaracaoMdfeEncerrado(adsm, retorno);
            return;
        }
    }

    /**
     * Método responsável por preencher os dados do retorno da autorização do
     * MDF-e em averbacao_docto_servico_mdfe.
     */
    private void preencherDadosRetornoDeclaracaoMdfeAutorizado(AverbacaoDoctoServicoMdfe ads, DocumentoRetornoAverbacao retorno) {
        ads.setBlAutorizado(retorno.getValido());
        ads.setDcRetornoAutorizado(retorno.getXml());

        if (StringUtils.isNotBlank(retorno.getData()) && ads.getDhEnvioAutorizado() == null) {
            ads.setDhEnvioAutorizado(DateTimeFormat.forPattern(PADRAO_DATA_BRASIL).parseDateTime(retorno.getData()));
        }

        if (ads.getNrProtocolo() == null) {
            ads.setNrProtocolo(retorno.getProtocolo());
        }

        ads.setBlAutorizado(retorno.getValido());
        ads.setDsRetornoAutorizado(retorno.getDescricao());

        if (StringUtils.isNotBlank(retorno.getData()) && ads.getDhEnvioAutorizado() == null) {
            ads.setDhEnvioAutorizado(DateTimeFormat.forPattern(PADRAO_DATA_BRASIL).parseDateTime(retorno.getData()));
        }

    }

    /**
     * Método responsável por preencher os dados do retorno do cancelamento do
     * MDF-e em averbacao_docto_servico_mdfe.
     */
    private void preencherDadosRetornoDeclaracaoMdfeCancelado(AverbacaoDoctoServicoMdfe ads, DocumentoRetornoAverbacao retorno) {
        ads.setBlCancelado(retorno.getValido());
        ads.setDcRetornoCancelado(retorno.getXml());

        if (StringUtils.isNotBlank(retorno.getData()) && ads.getDhEnvioCancelado() == null) {
            ads.setDhEnvioCancelado(DateTimeFormat.forPattern(PADRAO_DATA_BRASIL).parseDateTime(retorno.getData()));
        }

        if (ads.getNrProtocolo() == null) {
            ads.setNrProtocolo(retorno.getProtocolo());
        }
        
        ads.setBlCancelado(retorno.getValido());
        ads.setDsRetornoCancelado(retorno.getDescricao());

        if (StringUtils.isNotBlank(retorno.getData()) && ads.getDhEnvioCancelado() == null) {
            ads.setDhEnvioCancelado(DateTimeFormat.forPattern(PADRAO_DATA_BRASIL).parseDateTime(retorno.getData()));
        }
    }

    private void preencherDadosRetornoDeclaracaoMdfeEncerrado(AverbacaoDoctoServicoMdfe ads, DocumentoRetornoAverbacao retorno) {
        ads.setBlEncerrado(retorno.getValido());
        ads.setDcRetornoEncerrado(retorno.getXml());

        if (StringUtils.isNotBlank(retorno.getData()) && ads.getDhEnvioEncerrado() == null) {
            ads.setDhEnvioEncerrado(DateTimeFormat.forPattern(PADRAO_DATA_BRASIL).parseDateTime(retorno.getData()));
        }

        if (ads.getNrProtocolo() == null) {
            ads.setNrProtocolo(retorno.getProtocolo());
        }
        
        ads.setBlEncerrado(retorno.getValido());
        ads.setDsRetornoEncerrado(retorno.getDescricao());

        if (StringUtils.isNotBlank(retorno.getData()) && ads.getDhEnvioEncerrado() == null) {
            ads.setDhEnvioEncerrado(DateTimeFormat.forPattern(PADRAO_DATA_BRASIL).parseDateTime(retorno.getData()));
        }
    }

    /**
     * Método responsável por gerar os dados de MDF-e.
     */
    public List<DocumentoEnvioAverbacao> generateDadosManifestoEletronico() throws Exception {
        List<DocumentoEnvioAverbacao> listEnvio = new ArrayList<>();
        List<AverbacaoDoctoServicoMdfe> averbacaoPendenteEnvio = findDeclaracaoMdfePendenteEnvio(null);

        for (AverbacaoDoctoServicoMdfe averb : averbacaoPendenteEnvio) {

            if (averb.getTpDestino() == null) {
                averb.setTpDestino(TP_ATEM);
            }
            
            if (!TP_ATEM.equals(averb.getTpDestino())) {
                continue;
            }

            ManifestoEletronico manifestoEletronico = averb.getManifestoEletronico();

            List<String> listNrChaveCtes = XMLUtils.listNrChaveCtes(manifestoEletronico.getDsDados());

            if (TP_SITUACAO_MDFE_CANCELADO.equals(averb.getTpWebservice()) && !TP_SITUACAO_MDFE_CANCELADO.equals(manifestoEletronico.getTpSituacao().getValue())) {
                continue;
            }

            DocumentoEnvioAverbacao documentoEnvioAverbacao = DocumentoEnvioAverbacaoHelper.createDocumentoEnvioAverbacao(averb.getIdAverbacaoDoctoServicoMdfe(), averb.getTpWebservice(), null);
            String tpWebservice = averb.getTpWebservice();
            byte[] xml = null;

            if (TP_SITUACAO_MDFE_AUTORIZADO.equals(tpWebservice)) { 
                xml = manifestoEletronico.getDsDadosAutorizacao();
            } else if (TP_SITUACAO_MDFE_CANCELADO.equals(tpWebservice)) {
                xml = manifestoEletronico.getDsDadosCancelamento();
            } else if (TP_SITUACAO_MDFE_ENCERRADO.equals(tpWebservice)) {
                xml = manifestoEletronico.getDsDadosEncerramento();
            }

            documentoEnvioAverbacao.setXml(new String(xml, Charset.forName("UTF-8")));
            documentoEnvioAverbacao.setTpDestino(averb.getTpDestino());
            documentoEnvioAverbacao.setWebService(TP_SITUACAO_DECLARA_MDFE);

            if(isMdfeOperacaoSpitfire(listNrChaveCtes)) {
                documentoEnvioAverbacao.setBlOperacaoSpitFire("S");
            }else {
                documentoEnvioAverbacao.setBlOperacaoSpitFire("N");
            }

            listEnvio.add(documentoEnvioAverbacao);

            updateDataEnvioMdfe(averb.getIdAverbacaoDoctoServicoMdfe(), tpWebservice);
        }
        return listEnvio;
    }
    
    public List<AverbacaoDoctoServicoMdfe> findDeclaracaoMdfePendenteEnvio(String tpAverbacao) {
        return getAverbacaoDoctoServicoDAO().findDeclaracaoMdfePendenteEnvio(tpAverbacao);
    }

    private boolean isMdfeOperacaoSpitfire(List<String> listNrChave) {

        for(String nrChave: listNrChave) {
            Conhecimento conhecimento = this.conhecimentoService.findConhecimentoByNrChave(nrChave);
            if(conhecimento.getBlOperacaoSpitFire() == null || conhecimento.getBlOperacaoSpitFire() == false){
                return false;
            }
        }
        return true;
    }

    public ConhecimentoService getConhecimentoService() {
        return conhecimentoService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }
}