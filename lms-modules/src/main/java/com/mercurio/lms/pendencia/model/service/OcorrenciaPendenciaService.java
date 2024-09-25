package com.mercurio.lms.pendencia.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.OrdemServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.dao.OcorrenciaPendenciaDAO;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.pendencia.ocorrenciaPendenciaService"
 */
public class OcorrenciaPendenciaService extends CrudService<OcorrenciaPendencia, Long> {

	private static final String MERCADORIA_LIBERADA_ENTREGA_DESTINATARIO = "208";
	private static final String MERCADORIA_RETORNADA = "35";
	private static final String NO_TERMINAL = "24";
	private static final String AGUARDANDO_AUTORIZACAO_SERVICOS_ADICIONAIS = "207";
	private DoctoServicoService doctoServicoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private EventoService eventoService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private HistoricoFilialService historicoFilialService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private OrdemServicoService ordemServicoService;
	
	public void setOrdemServicoService(OrdemServicoService ordemServicoService) {
		this.ordemServicoService = ordemServicoService;
	}
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	public OcorrenciaDoctoServicoService getOcorrenciaDoctoServicoService() {
		return ocorrenciaDoctoServicoService;
	}
	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	public HistoricoFilialService getHistoricoFilialService() {
		return historicoFilialService;
	}
	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

	public List findLookupOcorrenciaPendencia(TypedFlatMap tfm) {
		return super.findLookup(tfm);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>OcorrenciaPendencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public OcorrenciaPendencia findById(java.lang.Long id) {
        return (OcorrenciaPendencia)super.findById(id);
    }

    
	/**
     * Recupera uma inst�ncia de <code>OcorrenciaPendencia</code> a partir do c�digo.
     * 
     * @param cdOcorrencia
     * @return
     */
    public OcorrenciaPendencia findByCodigoOcorrencia(java.lang.Short cdOcorrencia) {
        return getOcorrenciaPendenciaDAO().fingOcorrenciaByCdOcorrencia(cdOcorrencia);
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
    public java.io.Serializable store(OcorrenciaPendencia bean) {
    	if (validateCdOcorrenciaInOcorrencia(bean.getCdOcorrencia()) && (bean.getIdOcorrenciaPendencia()==null)) {
    		throw new BusinessException("LMS-17001");
    	}
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setOcorrenciaPendenciaDAO(OcorrenciaPendenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private OcorrenciaPendenciaDAO getOcorrenciaPendenciaDAO() {
        return (OcorrenciaPendenciaDAO) getDao();
    }
    
    public boolean validateCdOcorrenciaInOcorrencia(Short cdOcorrencia) {
    	return getOcorrenciaPendenciaDAO().validateCdOcorrenciaInOcorrencia(cdOcorrencia);
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * 
     * @param TypedFlatMap criteria
     * @return
     */
    public ResultSetPage findPaginatedToRegistrarOcorrenciasDoctosServico(TypedFlatMap criteria) {
    	return this.getOcorrenciaPendenciaDAO().
    				findPaginatedToRegistrarOcorrenciasDoctosServico(criteria, FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados 
     * para determinados parametros.
     * 
     * @param TypedFlatMap criteria
     * @return
     */
    public Integer getRowCountToRegistrarOcorrenciasDoctosServico(TypedFlatMap criteria) {
    	return this.getOcorrenciaPendenciaDAO().
    					getRowCountToRegistrarOcorrenciasDoctosServico(criteria, FindDefinition.createFindDefinition(criteria));
    }  
    
    /**
     * Retorna uma lista com os objetos.
     * 
     * @param TypedFlatMap criteria
     * @return
     */
    public List findLookupToRegistrarOcorrenciasDoctosServico(TypedFlatMap criteria) {
    	return this.getOcorrenciaPendenciaDAO().findLookupToRegistrarOcorrenciasDoctosServico(criteria);
    }
    
    public OcorrenciaPendencia findOcorrenciaBloqueioFromOcorrenciaDoctoServicoEmAberto(Long idDoctoServico){
    	OcorrenciaDoctoServico ocorrenciaDoctoServico = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(idDoctoServico);
    	if (ocorrenciaDoctoServico!=null){
    		getOcorrenciaPendenciaDAO().getAdsmHibernateTemplate().initialize(ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio());
    		return ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio();
    	}
    	return null;
    }
    
    
    
    /**
     * M�todo removido da action RegistrarOcorrenciasDocumentosServicoAction para c�.
     *  
     * @param blExigeRnc
     * @param idNaoConformidade
     * @param idDoctoServico
     * @param idEvento
     * @param idOcorrenciaPendencia (pode ser de bloqueio ou liberacao dependendo do caso)
     * @param idOcorrenciaBloqueio (necess�rio para valida��es internas e sera sempre a de bloqueio caso exista)
     * @param tpOcorrencia
     */
	public TypedFlatMap validateOcorrenciaPendenciaByRegistroOcorrenciasDocumentoServico(
			Boolean blExigeRnc, Long idNaoConformidade, Long idDoctoServico, Long idEvento, Long 
			idOcorrenciaPendencia, Long idOcorrenciaBloqueio, String tpOcorrencia) {
		
		TypedFlatMap validacoes = new TypedFlatMap();
		if (blExigeRnc) {
			if (idNaoConformidade == null) {
				//"O Documento de Servi�o deve ter uma N�o-Conformidade em aberto"
				throw new BusinessException("LMS-17017");
			}
			else
				if (ocorrenciaNaoConformidadeService.findOcorrenciasNaoConformidadeAbertas(idNaoConformidade).isEmpty()) {
					throw new BusinessException("LMS-17042");
				}
		}

		DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
		Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();

		List<Short> codigosLocalizacao = new ArrayList<Short>();
		codigosLocalizacao.add(Short.valueOf(NO_TERMINAL)); codigosLocalizacao.add(Short.valueOf(MERCADORIA_RETORNADA)); 
		codigosLocalizacao.add(Short.valueOf("40")); codigosLocalizacao.add(Short.valueOf("45")); 
		codigosLocalizacao.add(Short.valueOf("46")); codigosLocalizacao.add(Short.valueOf("47"));
		codigosLocalizacao.add(Short.valueOf("48")); codigosLocalizacao.add(Short.valueOf("50")); 
		codigosLocalizacao.add(Short.valueOf("51")); codigosLocalizacao.add(Short.valueOf("52"));
		
		Evento evento = eventoService.findByIdInitLazyProperties(idEvento, false);
		
		// Caso tpOcorrencia = "B":
		// Aceitar somente ocorr�ncias cujo c�digo do evento seja igual a
		// 39, 40, 41, 42, 43, 44, 45, 46 ou 78 sen�o emitir a mensagem
		// "C�digo do evento n�o permite bloqueio"
		if(tpOcorrencia.equalsIgnoreCase("B")) {
			
			List codigosEvento = new ArrayList();
			codigosEvento.add(Short.valueOf("39")); codigosEvento.add(Short.valueOf("40")); codigosEvento.add(Short.valueOf("41"));
			codigosEvento.add(Short.valueOf("42")); codigosEvento.add(Short.valueOf("43")); codigosEvento.add(Short.valueOf("44"));
			codigosEvento.add(Short.valueOf("45")); codigosEvento.add(Short.valueOf("46")); codigosEvento.add( Short.valueOf("78"));
			//codigosEvento.add( Short.valueOf("207"));

			if (!codigosEvento.contains(evento.getCdEvento())) {
				//"C�digo do evento n�o permite bloqueio"
				throw new BusinessException("LMS-17018");
			}

			// Se c�digo do evento da ocorr�ncia selecionada for igual a 39, 40 ou 78:
			// Permitir bloquear o documento, somente se o c�digo da localiza��o do mesmo
			// for igual a 24, 35, 40, 45, 46, 47, 48, 50, 51 ou 52, sen�o exibir a mensagem
			// "Localiza��o do documento n�o permite bloqueio"
			Short[] codigosEvento2 = new Short[]{ Short.valueOf("39"), Short.valueOf("40"), Short.valueOf("78")};
			if (ArrayUtils.contains(codigosEvento2, evento.getCdEvento())) {
				
				LocalizacaoMercadoria localizacaoMercadoria = doctoServico.getLocalizacaoMercadoria();

				List<Short> codigosLocalizacaoPermitirBloqueio = new ArrayList<Short>(codigosLocalizacao);
		        
				//Se c�digo do evento da ocorr�ncia selecionada (OCORRENCIA_PENDENCIA.ID_EVENTO -> EVENTO.CD_EVENTO) 
				//for igual a "40", permitir tamb�m bloquear o documento se o c�digo da localiza��o do mesmo 
				//(DOCTO_SERVICO.ID_LOCALIZACAO_MERCADORIA -> LOCALIZACAO_MERCADORIA.CD_LOCALIZACAO_MERCADORIA) 
				//for igual a "34" ou "38", al�m dos c�digos acima.
				if (Short.valueOf("40").equals(evento.getCdEvento())) {
		            codigosLocalizacaoPermitirBloqueio.add(Short.valueOf("34"));
		            codigosLocalizacaoPermitirBloqueio.add(Short.valueOf("38"));
		        }
				
				boolean localizacaoDocumentoNaoPermiteBloqueio = localizacaoMercadoria==null || !codigosLocalizacaoPermitirBloqueio.contains(localizacaoMercadoria.getCdLocalizacaoMercadoria());
				if (localizacaoDocumentoNaoPermiteBloqueio) {
					//"Localiza��o do documento n�o permite bloqueio"
					throw new BusinessException("LMS-17019");
				}
			}

			// Se c�digo do evento da ocorr�ncia selecionada for igual a 39:
			if (evento.getCdEvento().equals(Short.valueOf("39"))) {
				OcorrenciaPendencia ocorrenciaPendencia = findById(idOcorrenciaPendencia);
				
				// Se a ocorr�ncia em quest�o descontar DPE:
				if (!ocorrenciaPendencia.getBlDescontaDpe()) {
					//"Ocorr�ncia n�o est� descontando DPE"
					throw new BusinessException("LMS-17020");					
				}
				
				// Se a filial de destino do documento for null ou diferente da filial do usu�rio logado:
				if (doctoServico.getFilialByIdFilialDestino()==null || !doctoServico.getFilialByIdFilialDestino().getIdFilial().equals(idFilialUsuario)) {
					//"Filial n�o tem permiss�o para efetuar bloqueio."
					throw new BusinessException("LMS-17021");					
				}
				
				
				// 3.Se a filial do usu�rio logado N�O � uma LOJA 
				if (!historicoFilialService.validateFilialByTpFilial(SessionUtils.getFilialSessao().getIdFilial(), "LO")) {
					
					// Se a filial de destino do documento possuir uma filial de d�bito diferente
					// dela mesma, esta filial de d�bito deve ser igual � filial do usu�rio logado,
					// sen�o emitir mensagem: "Filial de d�bito n�o pode ser diferente da do destino do documento."
					// OBS: n�o precisa testar se doctoServico.getFilialByIdFilialDestino() � null, 
					// pois caso seja, j� cai no throw anterior.
					if (doctoServico.getFilialByIdFilialDestino().getFilialByIdFilialResponsavel() != null) {
						if (!doctoServico.getFilialByIdFilialDestino().getFilialByIdFilialResponsavel().getIdFilial().equals(idFilialUsuario)) {
								
							//"Filial de d�bito n�o pode ser diferente da do destino do documento."
							throw new BusinessException("LMS-17040");
						}
					}
				}
				// Se c�digo da ocorr�ncia selecionada for igual a 207:
				checkEventoIsAguardandoAutorizacaoServicosAdicionais(validacoes, doctoServico, idOcorrenciaPendencia);
			}
			
		} else {
			if (eventoDocumentoServicoService.validateExisteEventoDoctoServicoMaiorEventoBloqueio(idDoctoServico, idOcorrenciaBloqueio)) {
				LocalizacaoMercadoria localizacaoMercadoria = doctoServico.getLocalizacaoMercadoria();
				if (localizacaoMercadoria==null || !codigosLocalizacao.contains(localizacaoMercadoria.getCdLocalizacaoMercadoria())) {
					//"Localiza��o do documento n�o permite libera��o"
					throw new BusinessException("LMS-17044");
				}
			}
			checkEventoIsMercadoriaLiberadaEntregaDestinatario(doctoServico, idOcorrenciaPendencia);
		}
		return validacoes;
	}
	
	private void checkEventoIsMercadoriaLiberadaEntregaDestinatario(DoctoServico doctoServico, Long idOcorrenciaPendencia) {
		OcorrenciaPendencia ocorrenciaPendencia = findById(idOcorrenciaPendencia);
		if (ocorrenciaPendencia.getCdOcorrencia().equals(Short.valueOf(MERCADORIA_LIBERADA_ENTREGA_DESTINATARIO))) {
			
			ConteudoParametroFilial conteudoParametroFilial = null;
			
			try {
				conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "BL_LIB_OCOR_DOC_ADIC", false);
			} catch(BusinessException e){
				throw new BusinessException("LMS-17053");
			}
			
			if(conteudoParametroFilial == null || conteudoParametroFilial.getVlConteudoParametroFilial().equals("N")){
				throw new BusinessException("LMS-17053");
			}
			
			if(conteudoParametroFilial.getVlConteudoParametroFilial().equals("S") && checkExistsOrdemServicoDocumentoByDoctoServico(doctoServico)){
				throw new BusinessException("LMS-17054");
			}
		}
	}
	
	private boolean checkExistsOrdemServicoDocumentoByDoctoServico(DoctoServico doctoServico) {
		return ordemServicoService.findByDoctoServico(doctoServico.getIdDoctoServico(), Arrays.asList(ConstantesExpedicao.TP_SITUACAO_OS_APROVADA, ConstantesExpedicao.TP_SITUACAO_OS_DIGITADA)).isEmpty();
	}
	
	private void checkEventoIsAguardandoAutorizacaoServicosAdicionais(Map<String, Object> validacoes, DoctoServico doctoServico, Long idOcorrenciaPendencia) {
		OcorrenciaPendencia ocorrenciaPendencia = findById(idOcorrenciaPendencia);
		if (ocorrenciaPendencia.getCdOcorrencia().equals(Short.valueOf(AGUARDANDO_AUTORIZACAO_SERVICOS_ADICIONAIS))) {
			LocalizacaoMercadoria localizacaoMercadoria = doctoServico.getLocalizacaoMercadoria();
			if(!localizacaoMercadoria.getCdLocalizacaoMercadoria().equals(Short.valueOf(NO_TERMINAL)) && !localizacaoMercadoria.getCdLocalizacaoMercadoria().equals(Short.valueOf(MERCADORIA_RETORNADA))){
				throw new BusinessException("LMS-17019");
			}
			validacoes.put("existsDocumentoAdicional", true);
		}
	}
	public List<OcorrenciaPendencia> findOcorrenciaPendenciaAtiva() {
		return getOcorrenciaPendenciaDAO().findOcorrenciaPendenciaAtiva();
	}

	public Long fingEventoIdEventoByCdOcorrencia(Short cdOcorrencia){
		return getOcorrenciaPendenciaDAO().fingEventoIdEventoByCdOcorrencia(cdOcorrencia);
	}
}