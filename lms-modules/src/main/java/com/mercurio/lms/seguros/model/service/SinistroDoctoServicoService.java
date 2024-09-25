package com.mercurio.lms.seguros.model.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.service.DoctoServicoIndenizacaoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.seguros.model.dao.SinistroDoctoServicoDAO;
import com.mercurio.lms.vendas.model.Cliente;
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
 * @spring.bean id="lms.seguros.sinistroDoctoServicoService"
 */
public class SinistroDoctoServicoService extends CrudService<SinistroDoctoServico, Long> {
	
	private DomainValueService domainValueService;
	private DoctoServicoService doctoServicoService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
	
	public VersaoDescritivoPceService getVersaoDescritivoPceService() {
		return versaoDescritivoPceService;
	}

	public void setVersaoDescritivoPceService(
			VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}
	
	public void setDoctoServicoIndenizacaoService(DoctoServicoIndenizacaoService doctoServicoIndenizacaoService) {
		this.doctoServicoIndenizacaoService = doctoServicoIndenizacaoService;
	}
	
	/**
	 * Recupera uma instância de <code>SinistroDoctoServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public SinistroDoctoServico findById(java.lang.Long id) {
        return (SinistroDoctoServico)super.findById(id);
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
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeByIdProcessoSinistro(java.lang.Long id) {
    	getSinistroDoctoServicoDAO().removeByIdProcessoSinistro(id);
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
    public java.io.Serializable store(SinistroDoctoServico bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSinistroDoctoServicoDAO(SinistroDoctoServicoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SinistroDoctoServicoDAO getSinistroDoctoServicoDAO() {
        return (SinistroDoctoServicoDAO) getDao();
    }
    
    public List findPaginatedByIdDoctoServico(Long idDoctoServico) {
    	return getSinistroDoctoServicoDAO().findPaginatedByIdDoctoServico(idDoctoServico);
    }
    
    /**
     * Chamada para o findPaginated da tela de 'emitir carta ocorrencia'.
     * 
     * @param idsSinistroDoctoServico
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedCartaOcorrencia(List idsSinistroDoctoServico, FindDefinition findDefinition) {
    	return this.getSinistroDoctoServicoDAO().findPaginatedCartaOcorrencia(idsSinistroDoctoServico, findDefinition);
    }
    
    /**
     * Chamada para o getRowCountCartaOcorrencia da tela de 'emitir carta ocorrencia'.
     * 
     * @param idsSinistroDoctoServico
     * @return
     */
    public Integer getRowCountCartaOcorrencia(List idsSinistroDoctoServico) {
    	return this.getSinistroDoctoServicoDAO().getRowCountCartaOcorrencia(idsSinistroDoctoServico); 
    }

    /**
     * Chamada para a consulta do findPaginated da tela de 'selecionarDocumentos'.
     * 
     * @param idClienteDestinatario
     * @param idClienteRemetente
     * @param tpDocumentoServico
     * @param idFilialOrigemDoctoServico
     * @param idDocumentoServico
     * @param tpPrejuizo
     * @param blNaoEnviados
     * @param semPrejuizo
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedSelecionarDocumentos(Long idClienteDestinatario, Long idClienteRemetente, String tpDocumentoServico, 
    		Long idFilialOrigemDoctoServico, Long idDocumentoServico, String tpPrejuizo, boolean blNaoEnviados, boolean semPrejuizo, 
    		FindDefinition findDefinition) {
    	
    	return this.getSinistroDoctoServicoDAO().findPaginatedCartaOcorrencia(idClienteDestinatario, idClienteRemetente, tpDocumentoServico, 
    			idDocumentoServico, idFilialOrigemDoctoServico, null, tpPrejuizo, blNaoEnviados, semPrejuizo, null, findDefinition);
    }
    
    /**
     * Chamada para a consulta de getRowCount da tela de 'selecionarDocumentos'.
     * 
     * @param idClienteDestinatario
     * @param idClienteRemetente
     * @param tpDocumentoServico
     * @param idFilialOrigemDoctoServico
     * @param idDocumentoServico
     * @param tpPrejuizo
     * @param blNaoEnviados
     * @param semPrejuizo
     * @return
     */
    public Integer getRowCountSelecionarDocumentos(Long idClienteDestinatario, Long idClienteRemetente, String tpDocumentoServico, 
    		Long idFilialOrigemDoctoServico, Long idDocumentoServico, String tpPrejuizo, boolean blNaoEnviados, boolean semPrejuizo) {
    	
    	return this.getSinistroDoctoServicoDAO().getRowCountCartaOcorrencia(idClienteDestinatario, idClienteRemetente, tpDocumentoServico, 
    			idDocumentoServico, idFilialOrigemDoctoServico, null, tpPrejuizo, blNaoEnviados, semPrejuizo, null); 
    }
    
    /**
     * Chamada para a consulta do findPaginated da tela de 'selecionarDocumentos'.
     * 
     * @param idClienteDestinatario
     * @param idClienteRemetente
     * @param tpDocumentoServico
     * @param idDocumentoServico
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param tpPrejuizo
     * @param blNaoEnviados
     * @param semPrejuizo
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedSelecionarDocumentosRim(Long idClienteDestinatario, Long idClienteRemetente, String tpDocumentoServico, 
    		Long idDocumentoServico, Long idFilialOrigem, Long idFilialDestino, String tpPrejuizo, boolean blNaoEnviados, boolean semPrejuizo, 
    		FindDefinition findDefinition) {
    	
    	return this.getSinistroDoctoServicoDAO().findPaginatedCartaOcorrencia(idClienteDestinatario, idClienteRemetente, tpDocumentoServico, 
    			idDocumentoServico, idFilialOrigem, idFilialDestino, tpPrejuizo, blNaoEnviados, semPrejuizo, null, findDefinition);
    }
    
    /**
     * Chamada para a consulta de getRowCount da tela de 'selecionarDocumentos'.
     * 
     * @param idClienteDestinatario
     * @param idClienteRemetente
     * @param tpDocumentoServico
     * @param idDocumentoServico
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param tpPrejuizo
     * @param blNaoEnviados
     * @param semPrejuizo
     * @return
     */
    public Integer getRowCountSelecionarDocumentosRim(Long idClienteDestinatario, Long idClienteRemetente, String tpDocumentoServico, 
    		Long idDocumentoServico, Long idFilialOrigem, Long idFilialDestino, String tpPrejuizo, boolean blNaoEnviados, boolean semPrejuizo) {
    	
    	return this.getSinistroDoctoServicoDAO().getRowCountCartaOcorrencia(idClienteDestinatario, idClienteRemetente, tpDocumentoServico, 
    			idDocumentoServico, idFilialOrigem, idFilialDestino, tpPrejuizo, blNaoEnviados, semPrejuizo, null); 
    }
    
    /**
     * Chamada para o findPaginated da tela de 'emitir carta ocorrencia'.
     * Caso o destinatario da carta seja 'ambos' e gerada duas consultas e iterado sobre
     * ambas para buscar suas pecualiaridades em uma lista so.
     * 
     * @param idsSinistroDoctoServico
     * @param findDefinition
     * @return
     */
    public List findPaginatedEmailCartaOcorrencia(List idsSinistroDoctoServico, String destinatarioCarta) {
    	List result = new ArrayList();
    	
    	if (!"A".equals(destinatarioCarta)) {
    		result = this.getSinistroDoctoServicoDAO().findEmailCartaOcorrencia(idsSinistroDoctoServico, destinatarioCarta);
    	} else {
    		String[] tipoDestinatariosList = {"D", "R", "O", "S"};
    		
    		List resultTemporario = null;
    		for (int i = 0; i < tipoDestinatariosList.length; i++) {
    			resultTemporario = this.getSinistroDoctoServicoDAO().findEmailCartaOcorrencia(idsSinistroDoctoServico, tipoDestinatariosList[i]);
    			if (resultTemporario != null) {
    				result.addAll(resultTemporario);
    			}
    		}
    		
    		Comparator comparator = new Comparator() {
    			public int compare(Object arg0, Object arg1) {
    				Object[] objList1 = (Object[]) arg0;
    				Object[] objList2 = (Object[]) arg1;
    				
    				String obj1 = (String) objList1[1];
    				String obj2 = (String) objList2[1];
    				
    				return obj1.compareTo(obj2);
    			}
    		};
    		
    		Collections.sort(result, comparator);

    	}    	

    	return result;
    }
    
    /**
     * Chamada para o findPaginated da tela de 'sinistroDoctoServico'.
     * 
     * @param idsSinistroDoctoServico
     * @param findDefinition
     * @return
     */
    public List findPaginatedEmailRim(List idsSinistroDoctoServico, String filial, FindDefinition findDefinition) {
    	return this.getSinistroDoctoServicoDAO().findEmailRim(idsSinistroDoctoServico, filial, true);
    }
    
    /**
     * Verifica se o ProcessoSinistro em questão está contido em SinistroDoctoServico
     * @param idDoctoServico
     * @return true em caso afirmativo.
     */
    public boolean validateDoctoServicoInSinistroDoctoServico(Long idDoctoServico, Long idProcessoSinistro) {
    	return getSinistroDoctoServicoDAO().validateDoctoServicoInSinistroDoctoServico(idDoctoServico, idProcessoSinistro);
    }    	
    
    public List findSinistroDoctoServicoByIdProcessoSinistro(Long idProcessoSinistro) {
    	return getSinistroDoctoServicoDAO().findSinistroDoctoServicoByIdProcessoSinistro(idProcessoSinistro);
    }
    
    public ResultSetPage findPaginatedByIdProcessoSinistro(Long idProcessoSinistro, TypedFlatMap tfm, List exceptDoctos, String tpBeneficiarioIndenizacao, Long idCliente, Boolean isFilialMatriz) {
    	return getSinistroDoctoServicoDAO().findPaginatedByIdProcessoSinistro(idProcessoSinistro, FindDefinition.createFindDefinition(tfm), exceptDoctos, tpBeneficiarioIndenizacao, idCliente, isFilialMatriz);
    }
    
    public Integer getRowCountByIdProcessoSinistro(Long idProcessoSinistro, List exceptDoctos, String tpBeneficiarioIndenizacao, Long idCliente, Boolean isFilialMatriz) {
    	return getSinistroDoctoServicoDAO().getRowCountByIdProcessoSinistro(idProcessoSinistro, exceptDoctos, tpBeneficiarioIndenizacao, idCliente, isFilialMatriz);
    }
    
    /**
     * Busca documentos do processo de sinistro
     * 
     * Jira LMS-6180
     * 
     * @param idProcessoSinistro
     * @param tfm
     * @return
     */
    public List findByIdProcessoSinistro(Long masterId) {
    	
    	List<Object[]> list = getSinistroDoctoServicoDAO().findByIdProcessoSinistro(masterId);
    	List result = new ArrayList();
    	
    	if( list != null && !list.isEmpty() ){
    		
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Object[] row = (Object[]) iter.next();
				
				SinistroDoctoServico sinistroDoctoServico = new SinistroDoctoServico();
				// ID_SINISTRO_DOCTO_SERVICO
				sinistroDoctoServico.setIdSinistroDoctoServico((Long)row[0]); 
				
				DoctoServico doctoServico = new DoctoServico();
				// ID_DOCTO_SERVICO
				doctoServico.setIdDoctoServico((Long)row[1]); 
				// TP_DOCUMENTO_SERVICO
				doctoServico.setTpDocumentoServico(
						this.domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO", String.valueOf(row[2]))); 
				
				Filial filialOrigem = new Filial();
				// SG_FILIAL (origem)
				filialOrigem.setSgFilial(String.valueOf(row[3])); 
				doctoServico.setFilialByIdFilialOrigem(filialOrigem);
				
				// NR_DOCTO_SERVICO
				doctoServico.setNrDoctoServico((Long)row[4]); 
				
				Filial filialDestino = new Filial();
				// SG_FILIAL (destino)
				filialDestino.setSgFilial(String.valueOf(row[5])); 
				doctoServico.setFilialByIdFilialDestino(filialDestino);
				
				if(row[6] != null) {
					// DH_EMISSAO
					doctoServico.setDhEmissao(new DateTime((Timestamp)row[6])); 
				}
				
				if(row[7] != null) {
					// TP_PREJUIZO
					sinistroDoctoServico.setTpPrejuizo(
						this.domainValueService.findDomainValueByValue("DM_TIPO_PREJUIZO", String.valueOf(row[7]))); 
				}
				// VL_PREJUIZO
				sinistroDoctoServico.setVlPrejuizo((BigDecimal)row[8]); 
				// VL_MERCADORIA
				doctoServico.setVlMercadoria((BigDecimal)row[9]); 
				// QT_VOLUMES
				doctoServico.setQtVolumes((Integer)row[10]); 
				// PS_AFERIDO
				doctoServico.setPsAferido((BigDecimal)row[11]); 
				// PS_REAL
				doctoServico.setPsReal((BigDecimal)row[12]); 
				
				Cliente remetente = new Cliente();
				remetente.setPessoa(new Pessoa());
				// NM_PESSOA (remetente)
				remetente.getPessoa().setNmPessoa(String.valueOf(row[13])); 
				doctoServico.setClienteByIdClienteRemetente(remetente);
				
				Cliente destinatario = new Cliente();
				destinatario.setPessoa(new Pessoa());
				// NM_PESSOA (destinatario)
				destinatario.getPessoa().setNmPessoa(String.valueOf(row[14])); 
				doctoServico.setClienteByIdClienteDestinatario(destinatario);
				
				Cliente devedor = new Cliente();
				devedor.setPessoa(new Pessoa());
				// NM_PESSOA (devedor)
				devedor.getPessoa().setNmPessoa(String.valueOf(row[15])); 
				doctoServico.setDevedorDocServs(new ArrayList<DevedorDocServ>());
				doctoServico.getDevedorDocServs().add(new DevedorDocServ());
				doctoServico.getDevedorDocServs().get(0).setCliente(devedor);
				
				ReciboIndenizacao reciboIndenizacao = new ReciboIndenizacao();
				
				if(row[16] != null) {
					// DT_EMISSAO
					reciboIndenizacao.setDtEmissao(new YearMonthDay((Timestamp)row[16])); 
				}
				
				Filial filialRIM = new Filial();
				// SG_FILIAL (RIM)
				filialRIM.setSgFilial(String.valueOf(row[17])); 
				reciboIndenizacao.setFilial(filialRIM);
				// NR_RECIBO_INDENIZACAO
				reciboIndenizacao.setNrReciboIndenizacao((Integer)row[18]); 
				
				if(row[19] != null) {
					// TP_STATUS_INDENIZACAO
					reciboIndenizacao.setTpStatusIndenizacao(
						this.domainValueService.findDomainValueByValue("DM_STATUS_INDENIZACAO", String.valueOf(row[19]))); 
				}
				
				// VL_INDENIZACAO
				reciboIndenizacao.setVlIndenizacao((BigDecimal)row[20]); 
				
				if(row[21] != null) {
					// DT_PAGAMENTO_EFETUADO
					reciboIndenizacao.setDtPagamentoEfetuado(new YearMonthDay((Timestamp)row[21])); 
				}
				
				doctoServico.setDoctoServicoIndenizacoes(new ArrayList<DoctoServicoIndenizacao>());
				doctoServico.getDoctoServicoIndenizacoes().add(new DoctoServicoIndenizacao());
				doctoServico.getDoctoServicoIndenizacoes().get(0).setReciboIndenizacao(reciboIndenizacao);
				
				sinistroDoctoServico.setDoctoServico(doctoServico);
				
				if(row[22] != null) {
					// DH_GERACAO_CARTA_OCORRENCIA
					sinistroDoctoServico.setDhGeracaoCartaOcorrencia(new DateTime((Timestamp)row[22])); 
				}
				
				if(row[23] != null) {
					// DH_ENVIO_EMAIL_OCORRENCIA
					sinistroDoctoServico.setDhEnvioEmailOcorrencia(new DateTime((Timestamp)row[23])); 
				}
				
				if(row[24] != null) {
					// DH_GERACAO_CARTA_RETIFICACAO
					sinistroDoctoServico.setDhGeracaoCartaRetificacao(new DateTime((Timestamp)row[24])); 
				}
				
				if(row[25] != null) {
					// DH_ENVIO_EMAIL_RETIFICACAO
					sinistroDoctoServico.setDhEnvioEmailRetificacao(new DateTime((Timestamp)row[25])); 
				}
				
				if(row[26] != null) {
					// DH_GERACAO_FILIAL_RIM
					sinistroDoctoServico.setDhGeracaoFilialRim(new DateTime((Timestamp)row[26])); 
				}
				
				if(row[27] != null) {
					// DH_ENVIO_EMAIL_FILIAL_RIM
					sinistroDoctoServico.setDhEnvioEmailFilialRim(new DateTime((Timestamp)row[27])); 
				}
				
				// LMS-6611
				// BL_PREJUIZO_PROPRIO
				sinistroDoctoServico.setBlPrejuizoProprio(
						this.domainValueService.findDomainValueByValue("DM_SIM_NAO", String.valueOf(row[28])));
				
				result.add(sinistroDoctoServico);
			}
		}
    	
    	return result;
    }
    
    /**
     * Carrega os dados apresentados na grid de Documentos (manterProcessosSinistroDocumentos)
     * 
     * Jira LMS-6180
     * 
     * @param list
     * @return
     */
    public List findPopulateDataGridProcessoSinistro(List list) {
    	
    	List retorno = new ArrayList();
    	
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		SinistroDoctoServico sinistroDoctoServico = (SinistroDoctoServico) iter.next();
    		
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		
    		// Busca os dados do documento de serviço
    		sinistroDoctoServico.setDoctoServico(
    				this.doctoServicoService.findById(
    						sinistroDoctoServico.getDoctoServico().getIdDoctoServico()));
    		
    		typedFlatMap.put("idSinistroDoctoServico", sinistroDoctoServico.getIdSinistroDoctoServico());
    		typedFlatMap.put("doctoServico.idDoctoServico", sinistroDoctoServico.getDoctoServico().getIdDoctoServico());
    		typedFlatMap.put("doctoServico.tpDocumentoServico", sinistroDoctoServico.getDoctoServico().getTpDocumentoServico());
    		typedFlatMap.put("doctoServico.filialByIdFilialOrigem.sgFilial", sinistroDoctoServico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
    		typedFlatMap.put("doctoServico.nrDoctoServico", sinistroDoctoServico.getDoctoServico().getNrDoctoServico());
    		
    		if(sinistroDoctoServico.getDoctoServico().getFilialByIdFilialDestino() != null){
    			typedFlatMap.put("doctoServico.filialByIdFilialDestino.sgFilial", sinistroDoctoServico.getDoctoServico().getFilialByIdFilialDestino().getSgFilial());
    		}
    		
    		typedFlatMap.put("doctoServico.dhEmissao", sinistroDoctoServico.getDoctoServico().getDhEmissao());
    		typedFlatMap.put("tpPrejuizo", sinistroDoctoServico.getTpPrejuizo().getValue());
    		typedFlatMap.put("vlPrejuizo", sinistroDoctoServico.getVlPrejuizo());
    		// LMS-6611
    		typedFlatMap.put("blPrejuizoProprio", sinistroDoctoServico.getBlPrejuizoProprio().getValue());
    		typedFlatMap.put("doctoServico.vlMercadoria", sinistroDoctoServico.getDoctoServico().getVlMercadoria());
    		typedFlatMap.put("vlMercadoriaHidden", sinistroDoctoServico.getDoctoServico().getVlMercadoria());
    		typedFlatMap.put("doctoServico.qtVolumes", sinistroDoctoServico.getDoctoServico().getQtVolumes());
    		typedFlatMap.put("peso", sinistroDoctoServico.getDoctoServico().getPsAferido() == null ? 
    				sinistroDoctoServico.getDoctoServico().getPsReal() :
    					sinistroDoctoServico.getDoctoServico().getPsAferido());
    		typedFlatMap.put("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa", sinistroDoctoServico.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
    		typedFlatMap.put("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa", sinistroDoctoServico.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
    		typedFlatMap.put("doctoServico.devedorDocServs.cliente.pessoa.nmPessoa", 
    				sinistroDoctoServico.getDoctoServico().getDevedorDocServs().isEmpty() ? null :
    					sinistroDoctoServico.getDoctoServico().getDevedorDocServs().get(0).getCliente().getPessoa().getNmPessoa());
    		
    		String dtEmissao = "";
    		if(!sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().isEmpty() 
    				&& sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0).getReciboIndenizacao().getDtEmissao() != null) {
    			dtEmissao = sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0).getReciboIndenizacao().getDtEmissao().toString("dd/MM/yyyy");
    		}
    		
    		typedFlatMap.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.dtEmissao", dtEmissao);
    		
    		typedFlatMap.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.filial.sgFilial",
    				sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().isEmpty() ? null :
    					sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0).getReciboIndenizacao().getFilial().getSgFilial());
    		typedFlatMap.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.nrReciboIndenizacao",
    				sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().isEmpty() ? null :
    					sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0).getReciboIndenizacao().getNrReciboIndenizacao());
    		typedFlatMap.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.tpStatusIndenizacao",
    				sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().isEmpty() ? null :
    					sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0).getReciboIndenizacao().getTpStatusIndenizacao());
    		typedFlatMap.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.vlIndenizacao",
    				sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().isEmpty() ? null :
    					sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0).getReciboIndenizacao().getVlIndenizacao());
    		
    		String dtPagamentoEfetuado = "";
    		if(sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes() != null 
    				&& !sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().isEmpty()
    				&& sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0).getReciboIndenizacao().getDtPagamentoEfetuado() != null) {
    			dtPagamentoEfetuado = sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0).getReciboIndenizacao().getDtPagamentoEfetuado().toString("dd/MM/yyyy");
    		}
    		
    		typedFlatMap.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.dtPagamentoEfetuado", dtPagamentoEfetuado);
    		
    		String dataCartaComunicado = "";
    		if(sinistroDoctoServico.getDhGeracaoCartaOcorrencia() != null){
    			dataCartaComunicado = sinistroDoctoServico.getDhGeracaoCartaOcorrencia().toString("dd/MM/YYYY HH:MM");
    		} else if(sinistroDoctoServico.getDhEnvioEmailOcorrencia() != null){
				dataCartaComunicado = sinistroDoctoServico.getDhEnvioEmailOcorrencia().toString("dd/MM/YYYY HH:MM");
			}
    		
    		typedFlatMap.put("dataCartaComunicado", dataCartaComunicado);
    		
    		String dataCartaAtualizacao = "";
    		if(sinistroDoctoServico.getDhGeracaoCartaRetificacao() != null){
    			dataCartaAtualizacao = sinistroDoctoServico.getDhGeracaoCartaRetificacao().toString("dd/MM/YYYY HH:MM");
    		}else if(sinistroDoctoServico.getDhEnvioEmailRetificacao() != null){
				sinistroDoctoServico.getDhEnvioEmailRetificacao().toString("dd/MM/YYYY HH:MM");
			}
    		
    		typedFlatMap.put("dataCartaAtualizacao",  dataCartaAtualizacao);
    		
    		String dataCartaEmissaoRim = "";
    		if(sinistroDoctoServico.getDhGeracaoFilialRim() != null ){
    			dataCartaEmissaoRim = sinistroDoctoServico.getDhGeracaoFilialRim().toString("dd/MM/YYYY HH:MM");
    		}else if(sinistroDoctoServico.getDhEnvioEmailFilialRim() != null){
    			dataCartaEmissaoRim = sinistroDoctoServico.getDhEnvioEmailFilialRim().toString("dd/MM/YYYY HH:MM");
    		}
    		
    		typedFlatMap.put("dataCartaEmissaoRim",  dataCartaEmissaoRim);
    		
    		retorno.add(typedFlatMap);
    	}
    	
    	return retorno;
    }
    
    /**
     * 
     * Calcula quantidade de documentos do processo de sinistro
     * 
     * Jira LMS-6180
     * 
     * @param idProcessoSinistro
     * @param tfm
     * @return
     */
    public Integer getRowCountByIdProcessoSinistro(Long masterId) {
    	return getSinistroDoctoServicoDAO().getRowCountByIdProcessoSinistro(masterId);
    }

    public void removeSinistroDoctoServicosByIdProcessoSinistro(Long idProcessoSinistro) {
    	getSinistroDoctoServicoDAO().removeSinistroDoctoServicosByIdProcessoSinistro(idProcessoSinistro);
    }
    
    /**
     * Validação do documento de serviço 
     * 
     * Jira LMS-6180
     * 
     * @param idProcessoSinistro
     * @param doctoServico
     * @param mensagensConfirmadas
     * @return
     */
    public List<String> validateDocumentoServico(Long idProcessoSinistro, BigDecimal valorPrejuizo, String tpPrejuizo, 
    		List<String> idsDoctoServico) {
    	
    	final String tpPrejuizoSemPrejuizo = "S";
    	List<String> validationMessages = new ArrayList<String>();
    	
    	for (String idDoctoServico : idsDoctoServico) {
    		
    		DoctoServico doctoServico = this.doctoServicoService.findById(Long.valueOf(idDoctoServico));
    		
    		if(!tpPrejuizoSemPrejuizo.equals(tpPrejuizo)) {
    			validateDoctoServicoOutroProcesso(idProcessoSinistro, doctoServico.getIdDoctoServico());
    			
    			if(doctoServico.getVlMercadoria() != null && valorPrejuizo.compareTo(doctoServico.getVlMercadoria()) > 0) {
        			throw new BusinessException("LMS-22009");
        		}
        		
            	if (valorPrejuizo.compareTo(doctoServico.getVlMercadoria() == null ? BigDecimal.ZERO : doctoServico.getVlMercadoria())
            			> 0 || valorPrejuizo.compareTo(BigDecimal.ZERO) <= 0) {
            		throw new BusinessException("LMS-22038");
            	}
    		}
	    	
    		List result = getSinistroDoctoServicoDAO().findDoctoServicoOutroProcessoSinistro(doctoServico.getIdDoctoServico(), idProcessoSinistro);
	    	
	    	if(result != null && !result.isEmpty()) {
	    		validationMessages.add("LMS-22036 - " + getMessage("LMS-22036", new Object[] {
	    				doctoServico.getFilialByIdFilialOrigem().getSgFilial(),
	    				doctoServico.getNrDoctoServico().toString()
	    		}));
	    	}
	    	
	    	// LMS-22037
	    	result = doctoServicoIndenizacaoService.findDoctoServicoIndenizacaoReciboIndenizacaoNaoCancelado(doctoServico.getIdDoctoServico());
	    	
	    	if(result != null && !result.isEmpty()) {
	    		validationMessages.add("LMS-22037 - " + getMessage("LMS-22037", new Object[] {
	    				doctoServico.getFilialByIdFilialOrigem().getSgFilial(),
	    				doctoServico.getNrDoctoServico().toString()
	    		}));
	    	}
    	}
    	
    	return validationMessages;
    }

    /**
     * Valida se o documento já está em outro processo de sinisitro com prejuízo
     * 
     * Rotina auxiliar - Jira LMS-6180
     * 
     * @param idProcessoSinistro
     * @param doctoServico
     */
	public void validateDoctoServicoOutroProcesso(Long idProcessoSinistro, Long idDoctoServico) {
		
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		
		// LMS-22035
		List result = getSinistroDoctoServicoDAO().findDoctoServicoOutroProcessoSinistroComPrejuizo(idDoctoServico, idProcessoSinistro);
		
		if(result != null && !result.isEmpty()) {
			throw new BusinessException("LMS-22035", new Object[] { 
					doctoServico.getFilialByIdFilialOrigem().getSgFilial(),
					doctoServico.getNrDoctoServico().toString()
			});
		}
	}
    
    /**
     * LMS-6178
     * 
   	 * Faz a validacao do PCE para a tela de Detalhamento - Processo de Sinistro
   	 * 
   	 * @param idProcessoSinistro
   	 * @return
   	 */
   	public TypedFlatMap validatePCE(ItemList doctoServicos) {
       	
   		final String dmTtipoDocumento = "DM_TIPO_DOCUMENTO";
   		final List resultList = new ArrayList();
   		List<String> tiposDocumento = new ArrayList<String>();
       	
       	for (DomainValue domainValue : (List<DomainValue>)domainValueService.findDomainValues(dmTtipoDocumento)) {
       		tiposDocumento.add(domainValue.getValue());
		}
       	       	       			
       	for (Object item : doctoServicos.getItems()) {
   		       		
       		DoctoServico doctoServico = ((SinistroDoctoServico)item).getDoctoServico(); 
       		
   			if (doctoServico.getClienteByIdClienteRemetente()!=null) {
   				resultList.add(this.getVersaoDescritivoPceService()
   						.validatePCE(doctoServico.getClienteByIdClienteRemetente().getIdCliente(),
   								Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_SINISTRO), 
   				    			Long.valueOf(EventoPce.ID_EVENTO_PCE_CADASTRAR_PROC_SINISTRO),
       							Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_CAD_PROCESSO_CAD_PROC_SINISTRO)));
       		}
   				
   			if (doctoServico.getClienteByIdClienteDestinatario()!=null) {
   				resultList.add(this.getVersaoDescritivoPceService()
   						.validatePCE(doctoServico.getClienteByIdClienteDestinatario().getIdCliente(),
   								Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_SINISTRO), 
   								Long.valueOf(EventoPce.ID_EVENTO_PCE_CADASTRAR_PROC_SINISTRO),
   								Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_CAD_PROCESSO_CAD_PROC_SINISTRO)));
   			}	
   		}
   		
   		TypedFlatMap result = new TypedFlatMap();
   		result.put("list", resultList);
   		return result;
   	}
    
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	//LMS-6178
	public List findSomaValoresPrejuizo(Long idProcessoSinistro) {
		return getSinistroDoctoServicoDAO().findSomaValoresPrejuizo(idProcessoSinistro);
	}
	
	//LMS-6611
	public List findSomaValoresPrejuizoProprio(Long idProcessoSinistro) {
		return getSinistroDoctoServicoDAO().findSomaValoresPrejuizoProprio(idProcessoSinistro);
	}
    
	//LMS-6140
	public List findSinistrosDoctoServicoByIdProcessoSinistro(Long idProcessoSinistro) {
		return getSinistroDoctoServicoDAO().findSinistrosDoctoServicoByIdProcessoSinistro(idProcessoSinistro);
	}
    
	public List findDoctoServicoByIdSinistroDoctoServico(Long idSinistroDoctoServico){
		return getSinistroDoctoServicoDAO().findDoctoServicoByIdSinistroDoctoServico(idSinistroDoctoServico);
	}

	public TypedFlatMap findClienteByIdsSinistroDoctoServico(TypedFlatMap criteria) {
		TypedFlatMap parameters = new TypedFlatMap();
		
		List<Long> ids = new ArrayList<Long>();
		List<String> idsSinistroDoctoServico = criteria.getList("idsSinistroDoctoServico");
		
		for (String idSinistroDoctoServico : idsSinistroDoctoServico) {
			ids.add(Long.valueOf(idSinistroDoctoServico));
		}
		
		parameters.put("idsSinistroDoctoServico", ids);
		return getSinistroDoctoServicoDAO().findClienteByIdsSinistroDoctoServico(parameters);
	}
	
	//LMS-6155
	public List findSinistroDoctoServicoByIdProcessoSinistroAndListIdsDoctoServico(Long idProcessoSinistro, List idsDoctoServico) {
		return getSinistroDoctoServicoDAO().findSinistroDoctoServicoByIdProcessoSinistroAndListIdsDoctoServico(idProcessoSinistro, idsDoctoServico);
	}
	
	//LMS-6154
	public List findIdsSinistroDoctoServicoByIdDoctoServicoComPrejuizo(Long idDoctoServico){
		return getSinistroDoctoServicoDAO().findIdsSinistroDoctoServicoByIdDoctoServicoComPrejuizo(idDoctoServico);
	}
}