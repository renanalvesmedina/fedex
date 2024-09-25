package com.mercurio.lms.indenizacoes.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.dao.DoctoServicoIndenizacaoDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.indenizacoes.doctoServicoIndenizacaoService"
 */
public class DoctoServicoIndenizacaoService extends CrudService<DoctoServicoIndenizacao, Long> {

	private ReciboIndenizacaoService reciboIndenizacaoService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private ReciboIndenizacaoNfService reciboIndenizacaoNfService;
	private FilialDebitadaService filialDebitadaService;

	/**
	 * Recupera uma instância de <code>DoctoServicoIndenizacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public DoctoServicoIndenizacao findById(java.lang.Long id) {
        return (DoctoServicoIndenizacao)super.findById(id);
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
    public java.io.Serializable store(DoctoServicoIndenizacao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDoctoServicoIndenizacaoDAO(DoctoServicoIndenizacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DoctoServicoIndenizacaoDAO getDoctoServicoIndenizacaoDAO() {
        return (DoctoServicoIndenizacaoDAO) getDao();
    }

    /**
     * Método de pesquisa customizado para buscar os doctosServicos baseado no idReciboIndenizacao.
     * Utilizado nas telas de RIM 
     * @param idReciboIndenizacao
     * @param fd
     * @return
     */
    public ResultSetPage findDoctoServicosByIdReciboIndenizacao(Long idReciboIndenizacao, FindDefinition fd) {
    	return this.getDoctoServicoIndenizacaoDAO().findDoctoServicosByIdReciboIndenizacao(idReciboIndenizacao, fd);
    }
    
    /**
     * Row count customizado para buscar o total de doctosServicos baseado no idReciboIndenizacao.
     * Utilizado nas telas de RIM. 
     * @param idReciboIndenizacao
     * @return
     */
    public Integer getRowCountDoctoServicosByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return this.getDoctoServicoIndenizacaoDAO().getRowCountDoctoServicosByIdReciboIndenizacao(idReciboIndenizacao);
    }
    
    /**
     * Verifica se existe registros em docto_servico_indenizacao com o docto_servico passado por parametro
     * e que o status do recibo indenizacao dos respectivos docto_servico_indenizacao esteja diferente de cancelado. 
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountDoctoServicoIndenizacaoNaoCanceladoByIdDoctoServico(Long idDoctoServico) {
    	return getDoctoServicoIndenizacaoDAO().getRowCountDoctoServicoIndenizacaoNaoCanceladoByIdDoctoServico(idDoctoServico);
    }
    
    public List findByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getDoctoServicoIndenizacaoDAO().findByIdReciboIndenizacao(idReciboIndenizacao);
    }
    
    /**
     * Find que busca apenas a entidade DoctoServicoIndenizacao (sem fetch) a partir do idReciboIndenizacao
     * @param idReciboIndenizacao
     * @return
     */
    public List findDoctoServicoIndenizacaoByIdReciboIndenizacao(Long idReciboIndenizacao){
    	return getDoctoServicoIndenizacaoDAO().findDoctoServicoIndenizacaoByIdReciboIndenizacao(idReciboIndenizacao);
    }
    
    public List findByIdReciboIndenizacaoJoinFirstOcorrenciaNaoConformidade(Long idReciboIndenizacao) {
    	List listaDoctoServicoIndenizacao = getDoctoServicoIndenizacaoDAO().findByIdReciboIndenizacao(idReciboIndenizacao);
    	ReciboIndenizacao reciboIndenizacao = reciboIndenizacaoService.findById(idReciboIndenizacao);
    	
    	List result = new ArrayList();
    	for (Iterator it = listaDoctoServicoIndenizacao.iterator(); it.hasNext(); ) {
    		DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
    		List naoConformidades = doctoServicoIndenizacao.getDoctoServico().getNaoConformidades();
    		
    		// setando a lista de notas fiscais no item
    		List reciboIndenizacaoNfs = reciboIndenizacaoNfService.findByIdDoctoServicoIndenizacao(doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
    		doctoServicoIndenizacao.setReciboIndenizacaoNfs(reciboIndenizacaoNfs);

    		if (reciboIndenizacao.getTpIndenizacao().getValue().equals("NC") && naoConformidades!=null && naoConformidades.size() == 1) {
    			NaoConformidade naoConformidade = (NaoConformidade)naoConformidades.get(0);
				// obtém primeira ocorrencia de nao conformidade da nao conformidade
				OcorrenciaNaoConformidade ocorrenciaNaoConformidade = ocorrenciaNaoConformidadeService.findFirstOcorrenciaByIdNaoConformidade(naoConformidade.getIdNaoConformidade());
				// obtém controle de carga e manifesto da ocorrencia, caso existam
				if (ocorrenciaNaoConformidade!=null) {
					// setando a ocorrencia (menor delas) na nao conformidade
					List ocorrencias = new ArrayList();
					ocorrencias.add(ocorrenciaNaoConformidade);
					naoConformidade.setOcorrenciaNaoConformidades(ocorrencias);
				}
    		}
    		result.add(doctoServicoIndenizacao);
    	}
    	return result;
    }
    
    public List<DoctoServicoIndenizacao> findByIdDoctoServico(Long idDoctoServico) {
    	return getDoctoServicoIndenizacaoDAO().findByIdDoctoServico(idDoctoServico);
    }
    
    public List<DoctoServicoIndenizacao> findByIdDoctoServicoTodosStatus(Long idDoctoServico) {
    	return getDoctoServicoIndenizacaoDAO().findByIdDoctoServicoTodosStatus(idDoctoServico);
    }
    
	/**
	 * LMS-4591: Para escolher o registro a ser exibido, utilizar a seguinte
	 * regra: selecionar o registro 
	 * com maior DOCTO_SERVICO_INDENIZACAO.ID_RECIBO_INDENIZACAO -> RECIBO_INDENIZACAO.DT_EMISSAO 
	 * e maior DOCTO_SERVICO_INDENIZACAO.ID_RECIBO_INDENIZACAO onde
	 * DOCTO_SERVICO_INDENIZACAO.ID_RECIBO_INDENIZACAO -> RECIBO_INDENIZACAO
	 * RECIBO_INDENIZACAO.TP_STATUS_INDENIZACAO <> "C". 
	 * Se não houver resultado, buscar o primeiro registro com maior
	 * DOCTO_SERVICO_INDENIZACAO.ID_RECIBO_INDENIZACAO -> RECIBO_INDENIZACAO.DT_EMISSAO 
	 * e maior DOCTO_SERVICO_INDENIZACAO.ID_RECIBO_INDENIZACAO independente da situação.
	 * 
	 * @param idDoctoServico
	 * @return
	 */
	public DoctoServicoIndenizacao findByIdDoctoServicoParaLocalizacaoMercadoria(Long idDoctoServico) {
		List<DoctoServicoIndenizacao> listaNaoCancelados = getDoctoServicoIndenizacaoDAO().findByIdDoctoServicoParaLocalizacaoMercadoria(idDoctoServico, "C");

		if (listaNaoCancelados != null && !listaNaoCancelados.isEmpty()) {
			return listaNaoCancelados.get(0);
		} else {
			List<DoctoServicoIndenizacao> listaTodosStatus = getDoctoServicoIndenizacaoDAO().findByIdDoctoServicoParaLocalizacaoMercadoria(idDoctoServico, null);
			if (listaTodosStatus != null && !listaTodosStatus.isEmpty()) {
				return listaTodosStatus.get(0);
			}
		}

		return null;
	}
    
	public Map<String, Object> findMapParaLocalizacaoMercadoria(Long idDoctoServicoIndenizacao) {

		Map<String, Object> mapa = new HashMap<String, Object>();

		if (idDoctoServicoIndenizacao != null) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = this.findById(idDoctoServicoIndenizacao);

			ReciboIndenizacao reciboIndenizacao = doctoServicoIndenizacao.getReciboIndenizacao();
			Filial filialRecibo = reciboIndenizacao.getFilial();
			mapa.put("idFilial", filialRecibo.getIdFilial());
			mapa.put("sgFilial", filialRecibo.getSgFilial());
			mapa.put("nmFilial", filialRecibo.getPessoa().getNmFantasia());
			mapa.put("nrRecibo", reciboIndenizacao.getNrReciboIndenizacao());
			mapa.put("dtRecibo", reciboIndenizacao.getDtEmissao());
			mapa.put("dsTpStatus", reciboIndenizacao.getTpStatusIndenizacao().getDescription().getValue());
			mapa.put("vlIndenizacao", reciboIndenizacao.getVlIndenizacao());
			mapa.put("dsTpIndenizacao", reciboIndenizacao.getTpIndenizacao().getDescription().getValue());
			if (reciboIndenizacao.getProcessoSinistro() != null) {
				mapa.put("nrSeguro", reciboIndenizacao.getProcessoSinistro().getNrProcessoSinistro());
			}
			if (doctoServicoIndenizacao.getFilialSinistro() != null) {
				mapa.put("sgFilialLocal1", doctoServicoIndenizacao.getFilialSinistro().getSgFilial());
			}
			if (doctoServicoIndenizacao.getRotaSinistro() != null) {
				mapa.put("sgFilialLocal2", doctoServicoIndenizacao.getRotaSinistro().getSgFilial());
			}
			if (doctoServicoIndenizacao.getProduto() != null) {
				mapa.put("tipoProduto", doctoServicoIndenizacao.getProduto().getTipoProduto().getDsTipoProduto().getValue());
			}
			mapa.put("obRecibo", reciboIndenizacao.getObReciboIndenizacao());

			Pessoa pessoa;
			if (reciboIndenizacao.getPessoaByIdFavorecido() != null) {
				pessoa = reciboIndenizacao.getPessoaByIdFavorecido();
			} else {
				pessoa = reciboIndenizacao.getPessoaByIdBeneficiario();
			}

			mapa.put("nrIdentificacao", FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao()));
			mapa.put("nmPessoa", pessoa.getNmPessoa());

			mapa.put("dsFormaPgto", reciboIndenizacao.getTpFormaPagamento().getDescription().getValue());
			if (reciboIndenizacao.getBanco() != null) {
				mapa.put("nrBanco", reciboIndenizacao.getBanco().getNrBanco());
			}
			if (reciboIndenizacao.getAgenciaBancaria() != null) {
				mapa.put("nrAgencia", reciboIndenizacao.getAgenciaBancaria().getNrAgenciaBancaria());
				mapa.put("nrDigitoAgencia", reciboIndenizacao.getAgenciaBancaria().getNrDigito());
			}

			mapa.put("nrContaCorrente", reciboIndenizacao.getNrContaCorrente());
			mapa.put("nrDigitoContaCorrente", reciboIndenizacao.getNrDigitoContaCorrente());
			mapa.put("dtProgramadaPagto", reciboIndenizacao.getDtProgramadaPagamento());
			mapa.put("dtPagamentoEfetuado", reciboIndenizacao.getDtPagamentoEfetuado());
		}
		
		return mapa;
	}
    
    
	public List<Map<String, Object>> findFilialDebitadaByIdDoctoServicoIndenizacao(Long idDoctoServicoIndenizacao) {
		List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();

		if (idDoctoServicoIndenizacao != null) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = this.findById(idDoctoServicoIndenizacao);
			ReciboIndenizacao reciboIndenizacao = doctoServicoIndenizacao.getReciboIndenizacao();
			List<FilialDebitada> listaFilialDebitada = filialDebitadaService.findByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
			for (FilialDebitada filialDebitada : listaFilialDebitada) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sgFilial", filialDebitada.getFilial().getSgFilial());
				map.put("pcDebitado", filialDebitada.getPcDebitado());
				lista.add(map);
			}
		}

		return lista;
	}
    
	public List<Map<String,Object>> findRelatorioIndenizacoesFranqueados(Map<String,Object> parameters){
		return getDoctoServicoIndenizacaoDAO().findRelatorioIndenizacoesFranqueados(parameters);
	}
    
    public Integer getRowCountByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getDoctoServicoIndenizacaoDAO().getRowCountByIdReciboIndenizacao(idReciboIndenizacao);
    }

	public void setReciboIndenizacaoService(
			ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}

	public void setOcorrenciaNaoConformidadeService(
			OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}



	public void setReciboIndenizacaoNfService(
			ReciboIndenizacaoNfService reciboIndenizacaoNfService) {
		this.reciboIndenizacaoNfService = reciboIndenizacaoNfService;
	}

	public void setFilialDebitadaService(FilialDebitadaService filialDebitadaService) {
		this.filialDebitadaService = filialDebitadaService;
	}

	public List findDoctoServicoIndenizacaoReciboIndenizacaoNaoCancelado(Long idDoctoServico) {
		return getDoctoServicoIndenizacaoDAO().findDoctoServicoIndenizacaoReciboIndenizacaoNaoCancelado(idDoctoServico);
	}

}