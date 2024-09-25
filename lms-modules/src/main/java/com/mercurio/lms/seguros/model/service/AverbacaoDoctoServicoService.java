package com.mercurio.lms.seguros.model.service;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.tntbrasil.integracao.domains.expedicao.DocumentoRetornoAverbacao;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServico;
import com.mercurio.lms.seguros.model.dao.AverbacaoDoctoServicoDAO;

/**
 * Classe de servi�o para CRUD: 
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.anexoDoctoServicoService"
 */
@SuppressWarnings("deprecation")
public class AverbacaoDoctoServicoService extends CrudService<AverbacaoDoctoServico, Long> {

    public static final String AVERBACAO_DOCTO_SERVICO = "AVERBACAO_DOCTO_SERVICO";
    private static final String TEXT_HTML= "text/html; charset='utf-8'";

    private IntegracaoJmsService integracaoJmsService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera uma inst�ncia de <code>AnexoDoctoServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public AverbacaoDoctoServico findById(java.lang.Long id) {
		return (AverbacaoDoctoServico)super.findById(id);
	}

        /**
	 * Recupera uma cole��o de inst�ncia de <code>AnexoDoctoServico</code> a partir dos IDs.
	 *
	 * @param ids representa a entidade que deve ser localizada.
	 * @return Cole��o de inst�ncias que possuem os ids informados.
	 */
	public List<AverbacaoDoctoServico> findByIds(Set<java.lang.Long> ids) {
		if(!ids.isEmpty()){
			return getAverbacaoDoctoServicoDAO().findByIds(ids);
		}
		return new ArrayList<AverbacaoDoctoServico>();
	}
        
	public AverbacaoDoctoServico findCanceladoPendenteByIdDoctoServico(Long idDoctoServico) {
        List<AverbacaoDoctoServico> averbacaoDoctoServicoList = getAverbacaoDoctoServicoDAO().findCanceladoPendenteByIdDoctoServico(idDoctoServico);
        if (!averbacaoDoctoServicoList.isEmpty()) {
            return averbacaoDoctoServicoList.get(0);
        }
		return null;
	}
	
	/**
	 * Retornar a averbacao do documento servico com status de averbacao = 'S'
	 * @param idDoctoServico
	 * @return
	 * LMSA-7369
	 */
	public AverbacaoDoctoServico findByIdDoctoServicoAverbado(Long idDoctoServico) {
        List<AverbacaoDoctoServico> averbacaoDoctoServicoList = getAverbacaoDoctoServicoDAO().findByIdDoctoServicoAverbado(idDoctoServico);
        if (averbacaoDoctoServicoList != null && !averbacaoDoctoServicoList.isEmpty()) {
            return averbacaoDoctoServicoList.get(0);
        }
		return null;
	}


	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {		
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		return mail;
	}
	
	/**
	 * For�a a formata��o do texto que veio das mensagens da base de dados
	 * @param text Texto a ser formatado
	 * @return
	 */
	public String formatEmail(String text) {
		return text.replace("\\r", "\r").replace("\\n", "\n").replace("\\t", "\t");
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(AverbacaoDoctoServico bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @param dao Inst�ncia do DAO.
	 */
	public void setAverbacaoDoctoServicoDAO(AverbacaoDoctoServicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	public AverbacaoDoctoServicoDAO getAverbacaoDoctoServicoDAO() {
		return (AverbacaoDoctoServicoDAO) getDao();
	}

	public List<AverbacaoDoctoServico> findAberbacaoPendenteEnvio() {
		return findAberbacaoPendenteEnvio(null);
	}

	public List<AverbacaoDoctoServico> findAberbacaoPendenteEnvio(String tpAverbacao) {
		return findAberbacaoPendenteEnvioComAcrescimoParametroRownum(tpAverbacao);
	}
	
	public List<AverbacaoDoctoServico> findAberbacaoPendenteEnvioComXml() {
		return findAberbacaoPendenteEnvio(null);
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
	public List<AverbacaoDoctoServico> findAberbacaoPendenteEnvioComAcrescimoParametroRownum(String tpAverbacao) {
		String rownumEnvio = String.valueOf((BigDecimal)configuracoesFacade.getValorParametro("QTD_ROWNUM_ENV_AVERBACAO"));
		return getAverbacaoDoctoServicoDAO().findAberbacaoPendenteEnvio(tpAverbacao, Integer.valueOf(rownumEnvio));
	}
	
    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
    
	public void updateDataEnvio(Long idAverbacao, Integer nrEnvio){
		getAverbacaoDoctoServicoDAO().updateDataEnvio(idAverbacao, nrEnvio);
	}
	
	public void updateDataEnvio(Long idAverbacao){
		getAverbacaoDoctoServicoDAO().updateDataEnvio(idAverbacao);
	}
	
    public Integer getRowCountMonitoramentoAverbacoes(Map<String, Object> criteria) {
        return getAverbacaoDoctoServicoDAO().getRowCountMonitoramentoAverbacoes(criteria);
    }

    public List<AverbacaoDoctoServico> findMonitoramentoAverbacoes(Map<String, Object> criteria) {
        return getAverbacaoDoctoServicoDAO().findMonitoramentoAverbacoes(criteria);
    }

    @Transactional
    public void reenviar(List<Long> idsMonitoramentoAverbacao) {
        AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations = new AdsmNativeBatchSqlOperations(getDao());

        String alias = "updateAverbacao";
        if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(AVERBACAO_DOCTO_SERVICO, alias)) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                    AVERBACAO_DOCTO_SERVICO, "DH_ENVIO", null, alias
            );
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                    AVERBACAO_DOCTO_SERVICO, "DH_ENVIO_TZR", null, alias
            );
        }

        for (Long id: idsMonitoramentoAverbacao) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(AVERBACAO_DOCTO_SERVICO,
                    id, alias);
        }

        adsmNativeBatchSqlOperations.runAllCommands();
    }

    public AverbacaoDoctoServico findByIdDoctoServicoAndTpWebservice(Long idDoctoServico, String tpWebserviceE) {
        return getAverbacaoDoctoServicoDAO().findByIdDoctoServicoAndTpWebservice(idDoctoServico, tpWebserviceE);
    }

    public AverbacaoDoctoServico findByIdComMdfe(Long id) {
        return getAverbacaoDoctoServicoDAO().findByIdComMdfe(id);
    }
    
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void storeDadosRetornoAverbacaoCte(DocumentoRetornoAverbacao averbacao, AverbacaoDoctoServico cte) {
		this.preencherDadosRetornoAverbacaoCte(cte, averbacao);
		store(cte);
	}
    
    /**
     * M�todo respons�vel por preencher os dados do retorno da averba��o do CT-e
     * em averbacao_docto_servico.
     */
    private void preencherDadosRetornoAverbacaoCte(AverbacaoDoctoServico ads, DocumentoRetornoAverbacao retorno) {
        ads.setBlAverbado(retorno.getValido());
        ads.setDcRetorno(retorno.getXml());

        if (StringUtils.isNotBlank(retorno.getData()) && ads.getDhEnvio() == null) {
            ads.setDhEnvio(DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").parseDateTime(retorno.getData()));
        }

        ads.setDsRetorno(retorno.getDescricao());
        ads.setNrProtocolo(retorno.getProtocolo());
        ads.setNrAverbacao(retorno.getNumeroAverbacao());
    }

    public List<AverbacaoDoctoServico> findAverbacoesParaReenvio(Integer numMaxReenvio, Integer rownumReenvio) {
    	return getAverbacaoDoctoServicoDAO().findAverbacoesParaReenvio(numMaxReenvio, rownumReenvio);
    }
    
    public List<AverbacaoDoctoServico> findAverbacoesSemRetornoParaEnvioEmail(Integer numMaxReenvio, Integer rownumEnvio) {
    	return getAverbacaoDoctoServicoDAO().findAverbacoesSemRetornoParaEnvioEmail(numMaxReenvio, rownumEnvio);
    }

	public List<Map<String,Object>> findMonitoramentoAverbacoesBySql(
			Map<String, Object> criteria) {
		
		return getAverbacaoDoctoServicoDAO().findMonitoramentoAverbacoesBySql(criteria);
	}
    
}
