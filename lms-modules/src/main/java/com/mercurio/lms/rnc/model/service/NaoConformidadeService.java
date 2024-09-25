package com.mercurio.lms.rnc.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rnc.model.Disposicao;
import com.mercurio.lms.rnc.model.MotivoDisposicao;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.dao.NaoConformidadeDAO;
import com.mercurio.lms.sim.model.dao.LMRNCDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.rnc.naoConformidadeService"
 */
public class NaoConformidadeService extends CrudService<NaoConformidade, Long> {

	private MotivoDisposicaoService motivoDisposicaoService;
	private DisposicaoService disposicaoService;
	private ConfiguracoesFacade configuracoesFacade;
	private LMRNCDAO lmrncdao;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	
	public void setDisposicaoService(DisposicaoService disposicaoService) {
		this.disposicaoService = disposicaoService;
	}
	public void setLmrncdao(LMRNCDAO lmrncdao) {
		this.lmrncdao = lmrncdao;
	}
	public void setMotivoDisposicaoService(MotivoDisposicaoService motivoDisposicaoService) {
		this.motivoDisposicaoService = motivoDisposicaoService;
	}
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public OcorrenciaNaoConformidadeService getOcorrenciaNaoConformidadeService() {
		return ocorrenciaNaoConformidadeService;
	}
	/**
	 * Recupera uma instância de <code>NaoConformidade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public NaoConformidade findById(java.lang.Long id) {
        return (NaoConformidade)super.findById(id);
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
	 *
	 *
	 */
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
    public java.io.Serializable store(NaoConformidade bean) {
    	
        if (bean.getDhEmissao() == null && bean.getIdNaoConformidade() == null) {
        	bean.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
        }
    	
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setNaoConformidadeDAO(NaoConformidadeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private NaoConformidadeDAO getNaoConformidadeDAO() {
        return (NaoConformidadeDAO) getDao();
    }

    /**
     * retorna uma lista de notas fiscais conhecimento pelo id da nao conformidade
     * 
     */
    public List findNotaFiscalConhecimentoByIdNaoConformidade(Long idNaoConformidade){
    	return getNaoConformidadeDAO().findNotaFiscalConhecimentoByIdNaoConformidade(idNaoConformidade);
    }
    
    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountByNaoConformidade(TypedFlatMap criteria) {
    	Integer rowCount = getNaoConformidadeDAO().getRowCountByNaoConformidade(criteria);
    	return rowCount;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedByNaoConformidade(TypedFlatMap criteria) {
    	ResultSetPage rsp = getNaoConformidadeDAO().findPaginatedByNaoConformidade((criteria), FindDefinition.createFindDefinition(criteria));
    	rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
    	return rsp;
    }

    /**
     * Método que retorna uma NaoConformidade a partir de um ID de DoctoServico.
     * 
     * @param idDoctoServico
     * @return
     */
    public NaoConformidade findNaoConformidadeByIdDoctoServico(Long idDoctoServico) {
    	return this.getNaoConformidadeDAO().findNaoConformidadeByIdDoctoServico(idDoctoServico);
    }
    
    /**
     * GetRowCount da tela de <code>Consultar mercadorias em pendencia</code>.
     * 
     * @param idFilialAbertura
     * @param idFilialResponsavel
     * @param dhInclusao
     * @param idDoctoServico
     * @param idMotivoAberturaNC
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param dsOcorrenciaNC
     * @return
     */
    public Integer getRowCountNaoConformidadeRegistrarOcorrencia(Long idFilialAbertura, Long idFilialResponsavel, 
    		DateTime dhInclusaoInicial, DateTime dhInclusaoFinal, String tpDoctoServico, Long idFilialOrigem,
    		Long idDoctoServico, Long idMotivoAberturaNC, Long idClienteRemetente, Long idClienteDestinatario, String dsOcorrenciaNC) {
    	
    	return this.getNaoConformidadeDAO().getRowCountNaoConformidadeRegistrarOcorrencia(idFilialAbertura, 
    			idFilialResponsavel, dhInclusaoInicial, dhInclusaoFinal, tpDoctoServico, idFilialOrigem, idDoctoServico, 
    			idMotivoAberturaNC, idClienteRemetente, idClienteDestinatario, dsOcorrenciaNC);
    }
    
    /**
     * FindPaginated da tela de <code>Consultar mercadorias em pendencia</code>.
     * 
     * @param idFilialAbertura
     * @param idFilialResponsavel
     * @param dhInclusao
     * @param idDoctoServico
     * @param idMotivoAberturaNC
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param dsOcorrenciaNC
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedNaoConformidadeRegistrarOcorrencia(Long idFilialAbertura, Long idFilialResponsavel, 
    		DateTime dhInclusaoInicial, DateTime dhInclusaoFinal, String tpDoctoServico, Long idFilialOrigem,
    		Long idDoctoServico, Long idMotivoAberturaNC, Long idClienteRemetente, Long idClienteDestinatario, String dsOcorrenciaNC,
    		FindDefinition findDefinition) {
    	
    	return this.getNaoConformidadeDAO().findPaginatedNaoConformidadeRegistrarOcorrencia(idFilialAbertura, 
    			idFilialResponsavel, dhInclusaoInicial, dhInclusaoFinal, tpDoctoServico, idFilialOrigem, idDoctoServico, idMotivoAberturaNC, idClienteRemetente, 
    			idClienteDestinatario, dsOcorrenciaNC, findDefinition);
    }
    
    
    /**
     * Obtém uma nao conformidade pelo id do documento de servico, fazendo join com suas ocorrencias e motivos de abertura. 
     * @param idDoctoServico
     * @return
     */

    public NaoConformidade findByIdDoctoServicoJoinOcorrencias(Long idDoctoServico) {
    	NaoConformidade nc = getNaoConformidadeDAO().findByIdDoctoServicoJoinOcorrencias(idDoctoServico);
    	if (nc != null && nc.getOcorrenciaNaoConformidades() != null && !nc.getOcorrenciaNaoConformidades().isEmpty()) {
    		nc.getOcorrenciaNaoConformidades().get(0);
    	}
    	return nc;
    }
    
    /**
     * verifica se existe uma nao conformidade para o documento de servico 
     * @param idDoctoServico
     * @return
     */

    public boolean findNCByIdDoctoServico(Long idDoctoServico) {
    	return getNaoConformidadeDAO().findNCByIdDoctoServico(idDoctoServico);
    }
    
    /**
     * verifica se existe uma nao conformidade para o documento de servico 
     * @param idDoctoServico
     * @return
     */

    public List findNaoConformidadeByIdDoctoServicoLocMerc(Long idDoctoServico){
    	return lmrncdao.findNaoConformidadeByIdDoctoServico(idDoctoServico);
    }

    /**
     * Altera o status da NaoConformidade e suas Ocorrencias, conforme os atributos recebidos.
     * Remove todas as disposições associadas cujos motivos possuam BL_SOMENTE_AUTOMATICO = 'S'
     * @param idDoctoServico
     * @param tpStatusNaoConformidade ('ROI' ou 'RNC')
     * @param tpStatusOcorrencia ('F' ou 'A')
     */
    public void executeUpdateStatusNaoConformidade(Long idDoctoServico, String tpStatusNaoConformidade, String tpStatusOcorrencia) {
		NaoConformidade naoConformidade = findByIdDoctoServico(idDoctoServico);
		if (naoConformidade!=null) {
			naoConformidade.setTpStatusNaoConformidade(new DomainValue(tpStatusNaoConformidade));
			store(naoConformidade);
			List<OcorrenciaNaoConformidade> ocorrencias = ocorrenciaNaoConformidadeService.findOcorrenciasNaoConformidadeSomenteAutomatico(idDoctoServico);
			for (OcorrenciaNaoConformidade ocorrenciaNaoConformidade : ocorrencias) {
				ocorrenciaNaoConformidade.setTpStatusOcorrenciaNc(new DomainValue(tpStatusOcorrencia));
				//Limpa as disposições.
				List<Disposicao> disposicoes = ocorrenciaNaoConformidade.getDisposicoes();
				for (Disposicao disposicao : disposicoes) {
					disposicaoService.removeById(disposicao.getIdDisposicao());
				}
				ocorrenciaNaoConformidade.setDisposicoes(null);
				ocorrenciaNaoConformidadeService.store(ocorrenciaNaoConformidade);
			}
		}
    }

    
    /**
     * Altera o status da NaoConformidade e suas Ocorrencias, conforme os atributos recebidos.
     * @param idDoctoServico
     * @param tpStatusNaoConformidade ('ROI' ou 'RNC')
     * @param tpStatusOcorrencia ('F' ou 'A')
     */
    public void executeUpdateStatusNaoConformidadeByIncluirRIM(Long idDoctoServico, String tpStatusNaoConformidade, String tpStatusOcorrencia, String dsDisposicao) {
    	Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
    	Filial filailSessao = SessionUtils.getFilialSessao();
    	DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
    	
    	Map mapDisposicao = new HashMap();
    	mapDisposicao.put("dsMotivo", "Mercadoria indenizada"); // Gian pediu para colocar dessa forma, sem internacionalização.
    	List listaMotivos = motivoDisposicaoService.find(mapDisposicao);
    	MotivoDisposicao motivoDisposicao = null; 
    	if (!listaMotivos.isEmpty()) {
    		motivoDisposicao = (MotivoDisposicao)listaMotivos.get(0);
    	}
    	
		NaoConformidade naoConformidade = findByIdDoctoServicoJoinOcorrencias(idDoctoServico);
		if (naoConformidade!=null) {
			naoConformidade.setTpStatusNaoConformidade(new DomainValue(tpStatusNaoConformidade));
			store(naoConformidade);

			List ocorrencias = naoConformidade.getOcorrenciaNaoConformidades();
			if (ocorrencias!=null) {
				for (Iterator i = ocorrencias.iterator(); i.hasNext(); ) {
					OcorrenciaNaoConformidade ocorrenciaNaoConformidade = (OcorrenciaNaoConformidade)i.next();
					boolean isFechado = ocorrenciaNaoConformidade.getTpStatusOcorrenciaNc().getValue().equals("F");
					ocorrenciaNaoConformidade.setTpStatusOcorrenciaNc(new DomainValue(tpStatusOcorrencia));
					ocorrenciaNaoConformidadeService.store(ocorrenciaNaoConformidade);

					if (motivoDisposicao != null && !isFechado) {
						Disposicao disposicao = new Disposicao();
						disposicao.setDhDisposicao(dhAtual);
						disposicao.setDsDisposicao(dsDisposicao);
						disposicao.setFilial(filailSessao);
						disposicao.setMotivoDisposicao(motivoDisposicao);
						disposicao.setOcorrenciaNaoConformidade(ocorrenciaNaoConformidade);
						disposicao.setUsuario(usuarioLogado);
						disposicaoService.store(disposicao);
					}
				}
			}
		}
    }

    
	/**
	 * Faz a validacao do PCE para a tela de registrarDisposicao.
	 * 
	 * @param idNaoConformidade
	 * @return
	 */
	public TypedFlatMap validatePCE(Long idNaoConformidade) {
		NaoConformidade naoConformidade = this.findById(idNaoConformidade);
		TypedFlatMap result = new TypedFlatMap();
		
		if (naoConformidade.getDoctoServico()!=null) {
			
			if (naoConformidade.getDoctoServico().getClienteByIdClienteRemetente()!=null) {
				result.put("remetente", versaoDescritivoPceService
						.validatePCE(naoConformidade.getDoctoServico().getClienteByIdClienteRemetente().getIdCliente(),
								Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_RNC), 
				    			Long.valueOf(EventoPce.ID_EVENTO_PCE_REGISTRAR_DISPOSICAO),
				    			Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_ENCERRAMENTO_RNC_REGISTR_DISPOSICAO)));
			}
			
			if (naoConformidade.getDoctoServico().getClienteByIdClienteDestinatario()!=null) {
				result.put("destinatario", versaoDescritivoPceService
						.validatePCE(naoConformidade.getDoctoServico().getClienteByIdClienteDestinatario().getIdCliente(),
								Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_RNC), 
				    			Long.valueOf(EventoPce.ID_EVENTO_PCE_REGISTRAR_DISPOSICAO),
				    			Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_ENCERRAMENTO_RNC_REGISTR_DISPOSICAO)));
			}	
		}
		return result;
	}
	
    /**
     * Método que retorna uma NaoConformidade a partir de um ID de DoctoServico
     * sem fazer FETCH em nenhuma outra entidade.
     * 
     * @param idDoctoServico
     * @return
     */
    public NaoConformidade findByIdDoctoServico(Long idDoctoServico) {
		return getNaoConformidadeDAO().findByIdDoctoServico(idDoctoServico);
    }  
	
    /**
     * Carrega a entidade NaoConformidade de acordo com
     * o nrNaoConfornmidade e o idFilial.
     * 
     * Hector Julian Esnaola Junior
     * 22/02/2008
     *
     * @param nrNaoConformidade
     * @param idFilial
     * @return
     *
     * NaoConformidade
     *
     */
    public NaoConformidade findNaoConformidadeByNrNaoConformidadeAndIdFilial (
    		Integer nrNaoConformidade, 
    		Long idFilial) {
		return getNaoConformidadeDAO()
				.findNaoConformidadeByNrNaoConformidadeAndIdFilial(
						nrNaoConformidade, 
						idFilial);
	}

	public List executeRelatorioNaoConformidade(TypedFlatMap tfm){
		StringBuilder sql = mountSqlNC(tfm);
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("SG_FILIAL_NC", Hibernate.STRING);
				sqlQuery.addScalar("NAO_CONFORMIDADE_NR", Hibernate.LONG);
				sqlQuery.addScalar("STATUS_NAO_CONFORMIDADE", Hibernate.STRING);
				sqlQuery.addScalar("NR_OCORRENCIA_NC", Hibernate.INTEGER);
				sqlQuery.addScalar("MOTIVO_NAO_CONFORMIDADE", Hibernate.STRING);
				sqlQuery.addScalar("FILIAL_EMITENTE", Hibernate.STRING);
				sqlQuery.addScalar("FILIAL_RESPONSAVEL", Hibernate.STRING);
				sqlQuery.addScalar("DATA_ABERTURA", Hibernate.STRING);
				sqlQuery.addScalar("QT_VOLUMES", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("VL_CONVERTIDO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("DIAS_PENDENTES", Hibernate.LONG);
				sqlQuery.addScalar("STATUS_OCORRENCIA_NC", Hibernate.STRING);
				sqlQuery.addScalar("DS_TP_DOCUMENTO_SERVICO", Hibernate.STRING);
				sqlQuery.addScalar("SG_FILIAL_DS", Hibernate.STRING);
				sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("FILIAL_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("MOTIVO_DISPOSICAO", Hibernate.STRING);
			}
		};
		
		List<Map> result = new ArrayList<Map>();
		List<Object[]> list = getNaoConformidadeDAO().executeRelatorioNaoConformidade(sql, null, csq); 
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] nc: list){
				result.add(populaMapRNC(nc));
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Map populaMapRNC(Object[] objects) {
		int i = 0;
		Map map = new HashMap();
		map.put("SG_FILIAL_NC", (String) objects[i++]);
		map.put("NAO_CONFORMIDADE_NR", (Long) objects[i++]);
		map.put("STATUS_NAO_CONFORMIDADE", (String) objects[i++]);
		map.put("NR_OCORRENCIA_NC", (Integer) objects[i++]);
		map.put("MOTIVO_NAO_CONFORMIDADE", (String) objects[i++]);
		map.put("FILIAL_EMITENTE", (String) objects[i++]);
		map.put("FILIAL_RESPONSAVEL", (String) objects[i++]);
		map.put("DATA_ABERTURA", (String) objects[i++]);
		map.put("QT_VOLUMES", (BigDecimal) objects[i++]);
		map.put("VL_CONVERTIDO", (BigDecimal) objects[i++]);
		map.put("DIAS_PENDENTES", (Long) objects[i++]);
		map.put("STATUS_OCORRENCIA_NC", (String) objects[i++]);
		map.put("DS_TP_DOCUMENTO_SERVICO", (String) objects[i++]);
		map.put("SG_FILIAL_DS", (String) objects[i++]);
		map.put("NR_DOCTO_SERVICO", (Long) objects[i++]);
		map.put("FILIAL_DESTINO", (String) objects[i++]);
		map.put("MOTIVO_DISPOSICAO", (String) objects[i++]);
		return map;
	}
	
	private StringBuilder mountSqlNC(TypedFlatMap tfm){
    	StringBuilder sql = new StringBuilder()
    	.append("SELECT * ")
    	.append("FROM     (SELECT F_NC.SG_FILIAL AS SG_FILIAL_NC, ")
    	.append("		  NC.NR_NAO_CONFORMIDADE AS NAO_CONFORMIDADE_NR, ")
    	.append("         NC.TP_STATUS_NAO_CONFORMIDADE AS STATUS_NAO_CONFORMIDADE, ")
    	.append("         ONC.NR_OCORRENCIA_NC AS NR_OCORRENCIA_NC, ")
    	.append("         SUBSTR(REGEXP_SUBSTR(MA_NC.DS_MOTIVO_ABERTURA_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(MA_NC.DS_MOTIVO_ABERTURA_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»'))  as MOTIVO_NAO_CONFORMIDADE , ")
    	.append("         F_AB.SG_FILIAL AS FILIAL_EMITENTE, ")
    	.append("         F_RES.SG_FILIAL AS FILIAL_RESPONSAVEL, ")
    	.append("         ONC.DH_INCLUSAO AS DATA_ABERTURA, ")
    	.append("         ONC.QT_VOLUMES AS QT_VOLUMES, ")
    	.append("         CASE WHEN ONC.ID_MOEDA IS NULL THEN 0 WHEN ONC.ID_MOEDA IS NOT NULL THEN F_CONV_MOEDA(PAIS_F_AB.ID_PAIS, ONC.ID_MOEDA, 30, 1, to_date('25012017', 'ddMMyyyy'), ONC.VL_OCORRENCIA_NC ) END AS VL_CONVERTIDO, ")
    	.append("         CASE WHEN TP_STATUS_OCORRENCIA_NC = 'A' THEN  extract(day from (systimestamp - ONC.dh_inclusao)) WHEN TP_STATUS_OCORRENCIA_NC = 'F' THEN  0 END AS DIAS_PENDENTES , ")
    	.append("         ONC.TP_STATUS_OCORRENCIA_NC AS STATUS_OCORRENCIA_NC, ")
    	.append("         DS.TP_DOCUMENTO_SERVICO AS DS_TP_DOCUMENTO_SERVICO, ")
    	.append("         F_DS.SG_FILIAL AS SG_FILIAL_DS, ")
    	.append("         DS.NR_DOCTO_SERVICO AS NR_DOCTO_SERVICO, ")
    	.append("         F_DEST.SG_FILIAL AS FILIAL_DESTINO, ")
    	.append("         SUBSTR(REGEXP_SUBSTR(M_DISP.DS_MOTIVO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(M_DISP.DS_MOTIVO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»'))  as MOTIVO_DISPOSICAO  ")
    	.append("FROM     NAO_CONFORMIDADE NC, ")
    	.append("         OCORRENCIA_NAO_CONFORMIDADE ONC, ")
    	.append("         DOCTO_SERVICO DS, ")
    	.append("         FILIAL F_NC, ")
    	.append("         FILIAL F_AB, ")
    	.append("         FILIAL F_RES, ")
    	.append("         FILIAL F_DS, ")
    	.append("         MOTIVO_ABERTURA_NC MA_NC, ")
    	.append("         DISPOSICAO DISP, ")
    	.append("         MOTIVO_DISPOSICAO M_DISP, ")
    	.append("         PESSOA PES, ")
    	.append("         ENDERECO_PESSOA END_PES, ")
    	.append("         MUNICIPIO MUN_F_AB, ")
    	.append("         UNIDADE_FEDERATIVA UF_F_AB, ")
    	.append("         PAIS PAIS_F_AB, ")
    	.append("         FILIAL F_DEST ")
    	.append("WHERE    NC.ID_NAO_CONFORMIDADE = ONC.ID_NAO_CONFORMIDADE ")
    	.append("         AND NC.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO (+) ")
    	.append("         AND DS.ID_FILIAL_ORIGEM = F_DS.ID_FILIAL (+) ")
    	.append("         AND NC.ID_FILIAL = F_NC.ID_FILIAL ")
    	.append("         AND ONC.ID_FILIAL_ABERTURA = F_AB.ID_FILIAL ")
    	.append("         AND ONC.ID_FILIAL_RESPONSAVEL = F_RES.ID_FILIAL ")
    	.append("         AND ONC.ID_MOTIVO_ABERTURA_NC = MA_NC.ID_MOTIVO_ABERTURA_NC ")
    	.append("         AND DISP.ID_OCORRENCIA_NAO_CONFORMIDADE (+) = ONC.ID_OCORRENCIA_NAO_CONFORMIDADE ")
    	.append("         AND DISP.ID_MOTIVO_DISPOSICAO = M_DISP.ID_MOTIVO_DISPOSICAO (+) ")
    	.append("         AND F_AB.ID_FILIAL = PES.ID_PESSOA ")
    	.append("         AND PES.ID_ENDERECO_PESSOA = END_PES.ID_ENDERECO_PESSOA ")
    	.append("         AND END_PES.ID_MUNICIPIO = MUN_F_AB.ID_MUNICIPIO ")
    	.append("         AND MUN_F_AB.ID_UNIDADE_FEDERATIVA = UF_F_AB.ID_UNIDADE_FEDERATIVA ")
    	.append("         AND UF_F_AB.ID_PAIS = PAIS_F_AB.ID_PAIS ")
    	.append("         AND DS.ID_FILIAL_DESTINO = F_DEST.ID_FILIAL ");
    	
    	mountSqlParameters(tfm, sql);
    	mountSqlGoupBy(sql);
    	
    	return sql;
    }
	
	private void mountSqlParameters(TypedFlatMap tfm, StringBuilder sql) {
		String statusNaoConformidade = tfm.getString("statusNaoConformidade");
        if(StringUtils.isNotEmpty(statusNaoConformidade)) {
        	sql.append(" AND NC.TP_STATUS_NAO_CONFORMIDADE = '" + statusNaoConformidade+"' ");
        }

        Long idFilialAbertura = tfm.getLong("filialEmitente.idFilial");
        if (idFilialAbertura!=null) {
        	sql.append(" AND ONC.ID_FILIAL_ABERTURA = " + idFilialAbertura );  
        }

        Long idFilialResponsavel = tfm.getLong("filialResponsavel.idFilial");
        if(idFilialResponsavel!=null) {
        	sql.append(" AND ONC.ID_FILIAL_RESPONSAVEL =  "+ idFilialResponsavel);
        }
        
        Long idFilialDestino = tfm.getLong("filialDestino.idFilial");
        if(idFilialDestino != null) {
        	sql.append(" AND DS.ID_FILIAL_DESTINO = "+ idFilialDestino);
        }
        
        YearMonthDay dataInicial = tfm.getYearMonthDay("dataInicial");
        if(dataInicial!=null) {
        	sql.append(" AND TRUNC(CAST(ONC.DH_INCLUSAO AS DATE)) >= TO_DATE('")
					.append(dataInicial.toString("dd/MM/yyyy"))
					.append("', 'DD/MM/YYYY')");
        }
        
        YearMonthDay dataFinal = tfm.getYearMonthDay("dataFinal");
        if (dataFinal!=null){
        	sql.append(" AND TRUNC(CAST(ONC.DH_INCLUSAO AS DATE)) <= TO_DATE('")
					.append(dataFinal.toString("dd/MM/yyyy"))
					.append("', 'DD/MM/YYYY')");
			;
        }
        
        String tipoDocumento = tfm.getString("tipoDocumento");
        if(StringUtils.isNotEmpty(tipoDocumento)) {
        	sql.append(" AND DS.TP_DOCUMENTO_SERVICO = '" + tipoDocumento+"' ");
        }
        
        String motivoNaoConformidade = tfm.getString("motivoNaoConformidade");
        if(StringUtils.isNotEmpty(motivoNaoConformidade)) {
        	sql.append(" AND MA_NC.ID_MOTIVO_ABERTURA_NC = " + motivoNaoConformidade);
        }

        String causaNaoConformidade = tfm.getString("causaNaoConformidade");
        if(StringUtils.isNotEmpty(causaNaoConformidade)) {
        	sql.append(" AND ONC.ID_CAUSA_NAO_CONFORMIDADE = " + causaNaoConformidade);
        }
        
        String statusOcorrencia = tfm.getString("statusOcorrencia");
        if(StringUtils.isNotEmpty(statusOcorrencia)) {
        	sql.append(" AND ONC.TP_STATUS_OCORRENCIA_NC = '" + statusOcorrencia+"' ");
        }
        
        Long idRemetente= tfm.getLong("remetente.idCliente");
        if(idRemetente!=null) {
        	sql.append(" AND NC.ID_CLIENTE_REMETENTE = " + idRemetente);
        }        
        
        Long idDestinatario = tfm.getLong("destinatario.idCliente");
        if(idDestinatario!=null) {
        	sql.append(" AND NC.ID_CLIENTE_DESTINATARIO = " + idDestinatario);
        }
        
        String diaInicial = tfm.getString("diaInicial");
        if(StringUtils.isNotEmpty(diaInicial)) {
        	sql.append(" AND DECODE (TP_STATUS_OCORRENCIA_NC, 'A', extract(day from (systimestamp - ONC.dh_inclusao)), 'F', 0, 0) >= " + diaInicial);
        }

        String diaFinal = tfm.getString("diaFinal");
        if(StringUtils.isNotEmpty(diaFinal)) {
        	sql.append(" AND DECODE (TP_STATUS_OCORRENCIA_NC, 'A', extract(day from (systimestamp - ONC.dh_inclusao)), 'F', 0, 0) <= " + diaFinal);
        }
	}
    
	private void mountSqlGoupBy(StringBuilder sql) {
		sql.append(") GROUP BY SG_FILIAL_NC, ")
    	.append("         NAO_CONFORMIDADE_NR, ")
    	.append("         STATUS_NAO_CONFORMIDADE, ")
    	.append("         NR_OCORRENCIA_NC, ")
    	.append("         MOTIVO_NAO_CONFORMIDADE, ")
    	.append("         FILIAL_EMITENTE, ")
    	.append("         FILIAL_RESPONSAVEL, ")
    	.append("         DATA_ABERTURA, ")
    	.append("         QT_VOLUMES, ")
    	.append("         VL_CONVERTIDO, ")
    	.append("         DIAS_PENDENTES, ")
    	.append("         STATUS_OCORRENCIA_NC, ")
    	.append("         DS_TP_DOCUMENTO_SERVICO, ")
    	.append("         SG_FILIAL_DS, ")
    	.append("         NR_DOCTO_SERVICO, ")
    	.append("         FILIAL_DESTINO, ")
    	.append("         MOTIVO_DISPOSICAO ")
    	.append("ORDER BY SG_FILIAL_NC, ")
    	.append("         Nao_Conformidade_Nr, ")
    	.append("         NR_OCORRENCIA_NC ");
	}
    
    /**
     * LMS - 4460
     * Buscar as Não Conformidades de acordo com uma lista de Ocorrências
     * @param listIdOcorrencia String com lista de ID´s de Ocorrências separadas por ','
     * @return lista de Não Conformidades
     */
    public List<NaoConformidade> findByOcorrenciasNaoConformidade(String listIdOcorrencia) {
    	return getNaoConformidadeDAO().findByOcorrenciasNaoConformidade(listIdOcorrencia);	
    }
 
    public List<NaoConformidade> findByOcorrenciasNaoConformidadeWithOcorrenciaEmAberto(String listIdOcorrencia) {
    	return getNaoConformidadeDAO().findByOcorrenciasNaoConformidadeWithOcorrenciaEmAberto(listIdOcorrencia);	
    }
 
    public void updateTpStatusNaoConformidade(Integer nrNaoConformidade, Long idFilial, String tpStatus) {
     	getNaoConformidadeDAO().updateTpStatusNaoConformidade(nrNaoConformidade, idFilial, tpStatus);
 	}
    
    public NaoConformidade findByIdDoctoServicoAndStatusNaoConformidade(Long idDoctoServico, String tpStatusNaoConformidade) {
		return getNaoConformidadeDAO().findByIdDoctoServicoAndStatusNaoConformidade(idDoctoServico, tpStatusNaoConformidade);
    }
    
	public ResultSetPage findPaginatedByAwb(TypedFlatMap criteria) {
		ResultSetPage rsp = getNaoConformidadeDAO().findPaginatedByAwb(criteria, FindDefinition.createFindDefinition(criteria));
    	rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
    	return rsp;
	} 
	
	 public Integer getRowCountByAwb(TypedFlatMap criteria) {
	   	return getNaoConformidadeDAO().getRowCountByAwb(criteria);
	 }
}