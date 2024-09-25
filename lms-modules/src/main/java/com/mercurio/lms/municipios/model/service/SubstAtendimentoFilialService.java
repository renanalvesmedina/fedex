package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.SubstAtendimentoFilial;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.SubstAtendimentoFilialDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para CRUD:   
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.municipios.substAtendimentoFilialService"
 */
public class SubstAtendimentoFilialService extends CrudService<SubstAtendimentoFilial, Long> {
	
	private FilialService filialService;
	private VigenciaService vigenciaService;
	private EnderecoPessoaService enderecoPessoaService;
	private RegionalFilialService regionalFilialService;

	/**
	 * Recupera uma instância de <code>SubstAtendimentoFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public SubstAtendimentoFilial findById(java.lang.Long id) {
        return (SubstAtendimentoFilial)super.findById(id);
    }
    
    public Map findByIdDetalhamento(java.lang.Long id) {
    	SubstAtendimentoFilial bean = (SubstAtendimentoFilial)super.findById(id);
    	
		TypedFlatMap result = new TypedFlatMap();
		Filial filialDestino = bean.getFilialByIdFilialDestino(); 
		Filial filialSubstituta = bean.getFilialByIdFilialDestinoSubstituta();
		
		result.put("filialByIdFilialDestino.idFilial",filialDestino.getIdFilial());
		result.put("filialByIdFilialDestino.sgFilial",filialDestino.getSgFilial());
		result.put("filialByIdFilialDestino.pessoa.nmFantasia",filialDestino.getPessoa().getNmFantasia());
		result.put("filialByIdFilialDestinoSubstituta.idFilial",filialSubstituta.getIdFilial());
		result.put("filialByIdFilialDestinoSubstituta.sgFilial",filialSubstituta.getSgFilial());
		result.put("filialByIdFilialDestinoSubstituta.pessoa.nmFantasia",filialSubstituta.getPessoa().getNmFantasia());
		result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
		result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
		result.put("idSubstAtendimentoFilial",bean.getIdSubstAtendimentoFilial());
		
		UnidadeFederativa unidadeFederativa = bean.getUnidadeFederativa();
		Regional regional = bean.getRegional();
		Municipio municipio = bean.getMunicipio();
		Servico servico = bean.getServico();
		NaturezaProduto naturezaProduto = bean.getNaturezaProduto();
		Cliente cliente = bean.getCliente();
		
		if (unidadeFederativa != null) {
			result.put("unidadeFederativa.sgUnidadeFederativa",unidadeFederativa.getSgUnidadeFederativa());
			result.put("unidadeFederativa.idUnidadeFederativa",unidadeFederativa.getIdUnidadeFederativa());
			result.put("unidadeFederativa.nmUnidadeFederativa",unidadeFederativa.getNmUnidadeFederativa());
		}

        if (regional != null) {
			result.put("regional.idRegional",regional.getIdRegional());
        }

		Filial filialOrigem = bean.getFilialByIdFilialOrigem();
		if (filialOrigem != null) {
			result.put("filialByIdFilialOrigem.idFilial",filialOrigem.getIdFilial());
			result.put("filialByIdFilialOrigem.sgFilial",filialOrigem.getSgFilial());
			result.put("filialByIdFilialOrigem.pessoa.nmFantasia",filialOrigem.getPessoa().getNmFantasia());
		}

		if (municipio != null) {
			result.put("municipio.municipioFiliais.municipio.idMunicipio",municipio.getIdMunicipio());
			result.put("municipio.municipioFiliais.municipio.nmMunicipio",municipio.getNmMunicipio());
			result.put("municipio.idMunicipio",municipio.getIdMunicipio());
		}
		
        if (servico != null) {
			result.put("servico.idServico",servico.getIdServico());
        }
		
        if (naturezaProduto != null) {
			result.put("naturezaProduto.idNaturezaProduto",naturezaProduto.getIdNaturezaProduto());
        }
		
		if (cliente != null) {  
			result.put("cliente.pessoa.nmPessoa",cliente.getPessoa().getNmPessoa());
			result.put("cliente.idCliente",cliente.getIdCliente());  
			result.put("cliente.pessoa.nrIdentificacaoFormatado",  
					FormatUtils.formatIdentificacao(cliente.getPessoa().getTpIdentificacao(),cliente.getPessoa().getNrIdentificacao()));
			result.put("cliente.pessoa.nrIdentificacao",  
					FormatUtils.formatIdentificacao(cliente.getPessoa().getTpIdentificacao(),cliente.getPessoa().getNrIdentificacao()));
		}
		
		if(bean.getTpDesvioCarga() != null){
			result.put("tpDesvioCarga", bean.getTpDesvioCarga().getValue());
		}
		
		result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
    	return result;
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
    public java.io.Serializable store(SubstAtendimentoFilial bean) {
        if (bean.getRegional() == null || bean.getRegional().getIdRegional() == null) {
    		bean.setRegional(null);
        }
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
     * @param map bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public TypedFlatMap storeMap(Map map) {
    	SubstAtendimentoFilial bean = new SubstAtendimentoFilial();

        ReflectionUtils.copyNestedBean(bean,map);
        
        vigenciaService.validaVigenciaBeforeStore(bean);
        
        if (bean.getRegional() == null || bean.getRegional().getIdRegional() == null) {
    		bean.setRegional(null);
        }
        
        TypedFlatMap result = new TypedFlatMap();
        result.put("idSubstAtendimentoFilial",(Long)super.store(bean));
        result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
        return result;
    }
    
    protected SubstAtendimentoFilial beforeStore(SubstAtendimentoFilial bean) {
    	SubstAtendimentoFilial saf = (SubstAtendimentoFilial)bean;
        if (saf.getFilialByIdFilialOrigem() == null || saf.getFilialByIdFilialOrigem().getIdFilial() == null) {
			saf.setFilialByIdFilialOrigem(null);
        }
        if (saf.getCliente() == null || saf.getCliente().getIdCliente() == null) {
			saf.setCliente(null);
        }
        if (saf.getRegional() == null || saf.getRegional().getIdRegional() == null) {
			saf.setRegional(null);
        }
        if (saf.getUnidadeFederativa() == null || saf.getUnidadeFederativa().getIdUnidadeFederativa() == null) {
			saf.setUnidadeFederativa(null);
        }
        if (saf.getNaturezaProduto() == null || saf.getNaturezaProduto().getIdNaturezaProduto() == null) {
			saf.setNaturezaProduto(null);
        }
        if (saf.getServico() == null || saf.getServico().getIdServico() == null) {
			saf.setServico(null);
        }
        if (saf.getMunicipio() == null || saf.getMunicipio().getIdMunicipio() == null) {
			saf.setMunicipio(null);
        }
    	
        if (saf.getFilialByIdFilialOrigem() != null && saf.getFilialByIdFilialOrigem().getIdFilial() != null) {
    		filialService.verificaExistenciaHistoricoFilial(saf.getFilialByIdFilialOrigem().getIdFilial(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal());
        }
    	filialService.verificaExistenciaHistoricoFilial(saf.getFilialByIdFilialDestino().getIdFilial(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal());
    	filialService.verificaExistenciaHistoricoFilial(saf.getFilialByIdFilialDestinoSubstituta().getIdFilial(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal());
    	
    	if (saf.getCliente() != null){
            if (saf.getUnidadeFederativa() != null && !this.getSubstAtendimentoFilialDAO().verificaRegistroEndereco(saf)) {
	    		throw new BusinessException("LMS-29066");
            }
	    	
            if (saf.getRegional() != null && !this.getSubstAtendimentoFilialDAO().verificaRegionalCliente(saf)) {
	    		throw new BusinessException("LMS-29067");
            }
	    	
            if (saf.getFilialByIdFilialOrigem() != null && !this.getSubstAtendimentoFilialDAO().verificaFilialCliente(saf)) {
	    		throw new BusinessException("LMS-29068");
    	}
        }
	    	
        if (saf.getFilialByIdFilialOrigem() != null && saf.getFilialByIdFilialOrigem().getIdFilial() != null) {
    		filialService.verificaVigenciasEmHistoricoFilial(saf.getFilialByIdFilialOrigem().getIdFilial(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal());
        }
    	
        if (getSubstAtendimentoFilialDAO().findSubstAtendimentoVigente(saf)) {
    		throw new BusinessException("LMS-00003");
        }
    	
    	Integer count = Integer.valueOf(0);
    	
        if (saf.getFilialByIdFilialOrigem() != null) {
			count = getSubstAtendimentoFilialDAO().getRowCountBy(saf.getIdSubstAtendimentoFilial(),saf.getFilialByIdFilialDestino().getIdFilial(),saf.getFilialByIdFilialOrigem().getIdFilial(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal(),SubstAtendimentoFilialDAO.FILIAL);
        } else if (saf.getCliente() != null) {
    		count = getSubstAtendimentoFilialDAO().getRowCountBy(saf.getIdSubstAtendimentoFilial(),saf.getFilialByIdFilialDestino().getIdFilial(),saf.getCliente().getIdCliente(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal(),SubstAtendimentoFilialDAO.CLIENTE);
        } else if (saf.getRegional() != null) {
    		count = getSubstAtendimentoFilialDAO().getRowCountBy(saf.getIdSubstAtendimentoFilial(),saf.getFilialByIdFilialDestino().getIdFilial(),saf.getRegional().getIdRegional(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal(),SubstAtendimentoFilialDAO.REGIONAL);
        } else if (saf.getUnidadeFederativa() != null) {
    		count = getSubstAtendimentoFilialDAO().getRowCountBy(saf.getIdSubstAtendimentoFilial(),saf.getFilialByIdFilialDestino().getIdFilial(),saf.getUnidadeFederativa().getIdUnidadeFederativa(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal(),SubstAtendimentoFilialDAO.UF);
        }
    	
        if ((saf.getServico() != null || saf.getMunicipio() != null || saf.getNaturezaProduto() != null || saf.getCliente() != null)
                && validarCriteriosExistentes(saf)) {
    			throw new BusinessException("LMS-29127");
    		}
    	
        if (count > 0) {
			throw new BusinessException("LMS-29127");
        }
        if (saf.getFilialByIdFilialOrigem() == null && saf.getCliente() == null && saf.getRegional() == null && saf.getUnidadeFederativa() == null) {
    		count = getSubstAtendimentoFilialDAO().getRowCountBy(saf.getIdSubstAtendimentoFilial(),saf.getFilialByIdFilialDestino().getIdFilial(),saf.getFilialByIdFilialDestinoSubstituta().getIdFilial(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal(),SubstAtendimentoFilialDAO.FILIAL_SUBST);
        } else {
    		count = getSubstAtendimentoFilialDAO().getRowCountBy(saf.getIdSubstAtendimentoFilial(),saf.getFilialByIdFilialDestino().getIdFilial(),saf.getFilialByIdFilialDestinoSubstituta().getIdFilial(),saf.getDtVigenciaInicial(),saf.getDtVigenciaFinal(),SubstAtendimentoFilialDAO.FILIAL_SUBST_ONLY);
        }
        if (count > 0) {
			throw new BusinessException("LMS-29127");
        }
    	
    	return super.beforeStore(bean);
    }
    
    private boolean validarCriteriosExistentes(SubstAtendimentoFilial saf) {
        return getSubstAtendimentoFilialDAO().getRowCountEquals(saf) > 0 &&
                getSubstAtendimentoFilialDAO().getRowCountEqualsWithCliente(saf) > 0;
    }

    protected void beforeRemoveByIds(List ids) {
    	SubstAtendimentoFilial saf = null;
    	for(int x = 0; x < ids.size(); x++) {
    		saf = findById((Long)ids.get(x));
    		JTVigenciaUtils.validaVigenciaRemocao(saf);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	SubstAtendimentoFilial saf = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(saf);
    	super.beforeRemoveById(id);
    }

    public ResultSetPage findPaginated(Map criteria) {
    	FilterResultSetPage rsp = new FilterResultSetPage(super.findPaginated(criteria)) {
    		public Map filterItem(Object item) {
    			SubstAtendimentoFilial bean = (SubstAtendimentoFilial)item;
    			TypedFlatMap result = new TypedFlatMap();
				Filial filialDestino = bean.getFilialByIdFilialDestino();
				Filial filialSubstituta = bean.getFilialByIdFilialDestinoSubstituta();
				
				result.put("filialByIdFilialDestino.sgFilial",filialDestino.getSgFilial());
				result.put("filialByIdFilialDestino.pessoa.nmFantasia",filialDestino.getPessoa().getNmFantasia());
				result.put("filialByIdFilialDestinoSubstituta.sgFilial",filialSubstituta.getSgFilial());
				result.put("filialByIdFilialDestinoSubstituta.pessoa.nmFantasia",filialSubstituta.getPessoa().getNmFantasia());
				result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
				result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
				result.put("idSubstAtendimentoFilial",bean.getIdSubstAtendimentoFilial());
				
				UnidadeFederativa unidadeFederativa = bean.getUnidadeFederativa();
				Filial filialOrigem = bean.getFilialByIdFilialOrigem();
				Regional regional = bean.getRegional();
				Municipio municipio = bean.getMunicipio();
				Servico servico = bean.getServico();
				NaturezaProduto naturezaProduto = bean.getNaturezaProduto();
				Cliente cliente = bean.getCliente();
                if (unidadeFederativa != null) {
					result.put("unidadeFederativa.sgUnidadeFederativa",unidadeFederativa.getSgUnidadeFederativa());
                }
                if (regional != null) {
					result.put("regional.dsRegional",regional.getDsRegional());
                }
                if (municipio != null) {
					result.put("municipio.nmMunicipio",municipio.getNmMunicipio());
                }
                if (servico != null) {
					result.put("servico.dsServico",servico.getDsServico());
                }
                if (naturezaProduto != null) {
					result.put("naturezaProduto.dsNaturezaProduto",naturezaProduto.getDsNaturezaProduto());
                }
                if (cliente != null) {
					result.put("cliente.pessoa.nmPessoa",cliente.getPessoa().getNmPessoa());
                }
				if (filialOrigem != null) {
					result.put("filialByIdFilialOrigem.idFilial",filialOrigem.getIdFilial());
					result.put("filialByIdFilialOrigem.sgFilial",filialOrigem.getSgFilial());
					result.put("filialByIdFilialOrigem.pessoa.nmFantasia",filialOrigem.getPessoa().getNmFantasia());
				}
				
    			return result;
    		}
    	};
		return (ResultSetPage)rsp.doFilter();
    }

    /**
     * Descobre se a filial de destino do documento de serviço possui alguma 
     * substituição de atendimento que mude a filial para onde o documento de serviço será destinado.
     * <p>
     * <b>Este método ainda não foi implementado. Apenas a 'Interface'.</b>
     * 
     * @param idDoctoServico Long Documento de serviço que deseja-se consultar a filial de destino.
     * @param idFilialOrigem Long Filial de origem quando não se considerar a origem do documento de serviço.
     * @param dtConsulta YearMonthDay Data em que a operação será efetuada
     * @return Filial filial de destino do documento de serviço.
     * @author FelipeF
     * @author Rodrigo
     */
    public Filial findFilialDestinoDoctoServico(Long idDoctoServico, Long idFilialOrigem, YearMonthDay dtConsulta, DomainValue tpDesvioCarga) {
    	
    	if (idDoctoServico == null){
    		throw new IllegalArgumentException("O argumento idDoctoServico é obrigatório.");
    	}
    	
    	if (dtConsulta == null){
    		dtConsulta = JTDateTimeUtils.getDataAtual();
    	}	

    	DoctoServico doctoServico = (DoctoServico)getSubstAtendimentoFilialDAO().getAdsmHibernateTemplate().load(DoctoServico.class,idDoctoServico);
    	Hibernate.initialize(doctoServico);
    	getSubstAtendimentoFilialDAO().getAdsmHibernateTemplate().evict(doctoServico);

		Long idNatureza = null;

		if( ArrayUtils.contains(new String[]{"CTR","NFT","CTE","NTE","CRT"},doctoServico.getTpDocumentoServico().getValue()) ){
    	Conhecimento conhecimento = (Conhecimento)getSubstAtendimentoFilialDAO().getAdsmHibernateTemplate().load(Conhecimento.class,idDoctoServico);
    	Hibernate.initialize(conhecimento);
    	getSubstAtendimentoFilialDAO().getAdsmHibernateTemplate().evict(conhecimento);
			idNatureza = conhecimento.getNaturezaProduto() != null ? conhecimento.getNaturezaProduto().getIdNaturezaProduto() : null;
        } else if ("RRE".equals(doctoServico.getTpDocumentoServico().getValue())) {
    		ReciboReembolso reciboReembolso = (ReciboReembolso)getSubstAtendimentoFilialDAO().getAdsmHibernateTemplate().load(ReciboReembolso.class,idDoctoServico);
    		Hibernate.initialize(reciboReembolso);
        	getSubstAtendimentoFilialDAO().getAdsmHibernateTemplate().evict(reciboReembolso);
        	idFilialOrigem = reciboReembolso.getDoctoServicoByIdDoctoServReembolsado().getFilialByIdFilialOrigem().getIdFilial();
    	} 

    	if (idFilialOrigem == null){ 
    		idFilialOrigem = doctoServico.getFilialByIdFilialOrigem().getIdFilial();
    	}	

    	Long idFilialDestino = doctoServico.getFilialByIdFilialDestino().getIdFilial();
		Long idServico = doctoServico.getServico() != null ? doctoServico.getServico().getIdServico():null;

		EnderecoPessoa endereco = enderecoPessoaService.findEnderecoPessoaPadrao(idFilialOrigem);
		getSubstAtendimentoFilialDAO().getAdsmHibernateTemplate().evict(endereco);
		
		Long idUF = null;
		Long idMunicipio = null;
		if( endereco != null && endereco.getMunicipio() != null){
			idMunicipio = endereco.getMunicipio().getIdMunicipio();
			if( endereco.getMunicipio().getUnidadeFederativa() != null ){
				idUF = endereco.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
			}
		}

		Regional regional = regionalFilialService.findLastRegionalVigente(idFilialOrigem);
		Long idRegional = regional != null ? regional.getIdRegional():null;

		Long idCliente = doctoServico.getClienteByIdClienteRemetente() != null ? doctoServico.getClienteByIdClienteRemetente().getIdCliente():null;

    	return findFilialSubstituta(tpDesvioCarga,dtConsulta,idFilialDestino,idNatureza,idServico,idUF,idRegional,idFilialOrigem,idCliente,idMunicipio,true);
    }
    
	/**
     * Método que retorna uma lista de SubstAtendimentoFilial pelo ID da Filial de Destino
     * e tipo de desvio de carga.
     * 
     * @param idFilialDestino
     * @return
     */
    public List findSubstAtendimentoFilialByIdFilialDestino(Long idFilialDestino, DomainValue tpDesvioCarga) {
    	return this.getSubstAtendimentoFilialDAO().findSubstAtendimentoFilialByIdFilialDestino(idFilialDestino,tpDesvioCarga);
    }
    
    /**
     * Método que retorna SubstAtendimentoFilial pelo ID da Filial de Destino.
     */
    public Filial findMatchSubstAtendimentoFilialByIdFilialDestino(DomainValue tpDesvioCarga, Long idFilialDestino, Long idNaturezaProduto, Long idServico, Long idUFOrigem, Long idRegionalOrigem, Long idFilialOrigem, Long idClienteRemetente, Long idMunicipioDestino) {
    	return this.getSubstAtendimentoFilialDAO().findMatchSubstAtendimentoFilialByIdFilialDestino(tpDesvioCarga, idFilialDestino, idNaturezaProduto, idServico, idUFOrigem, idRegionalOrigem, idFilialOrigem, idClienteRemetente, idMunicipioDestino);
    }
    
    public Filial findMatchSubstAtendimentoFilialByIdFilialDestino(Long idFilialDestino) {
    	return findMatchSubstAtendimentoFilialByIdFilialDestino(null, idFilialDestino, null, null, null, null, null, null, null);
	}


    /**
     * Retorna a filial substituta para o cliente e destino informados.
     *
     * @param idFilialDestino
     * @param idCliente
     * @return
     */
    public Filial findFilialSubstitutaByFilialDestinoAndCliente(Long idFilialDestino, Long idCliente) {
    	return this.getSubstAtendimentoFilialDAO().findFilialSubstitutaByFilialDestinoAndCliente(idFilialDestino, idCliente);
    }
    
    /**
     * Método que retorna uma lista de SubstAtendimentoFilial pelo ID da Filial Substituta. 
     *
     * @param idFilialSubstituta
     * @return
     */
    public List findSubstAtendimentoFilialByIdFilialSubstituta(Long idFilialSubstituta) {
    	return this.getSubstAtendimentoFilialDAO().findSubstAtendimentoFilialByIdFilialSubstituta(idFilialSubstituta);
    }
    
    /**
     * Método que retorna um SubstAtendimentoFilial pelo ID da Filial de Destino e ID da Filial Substituta. 
     *
     * @param idFilialDestino
     * @return
     */
    public List<SubstAtendimentoFilial> findSubstAtendimentoFilialByIdFilialDestinoAndFilialSubstituta(Long idFilialDestino, Long idFilialSubstituta) {
    	return this.getSubstAtendimentoFilialDAO().findSubstAtendimentoFilialByIdFilialDestinoAndFilialSubstituta(idFilialDestino, idFilialSubstituta);
    }
    
    /**
     * Obtem a filial substituta de uma filial passada por parâmetro, considerando o tipo de desvio EN
     * 
     * @param tpDesvioCarga
     * @param dtConsulta
     * @param idFilial
     * @param idNaturezaProduto
     * @param idServico
     * @param idUFOrigem
     * @param idRegionalOrigem
     * @param idFilialOrigem
     * @param idClienteRemetente
     * @param idMunicipioDestino
     * @param substituiFilial
     * @return Retorna a filial de destino da entidade SubstAtendimentoFilial ou DoctoServico
     */
	public Filial findFilialSubstituta(DomainValue tpDesvioCarga,
			YearMonthDay dtConsulta, Long idFilial, Long idNaturezaProduto,
			Long idServico, Long idUFOrigem, Long idRegionalOrigem,
			Long idFilialOrigem, Long idClienteRemetente,
			Long idMunicipioDestino, Boolean substituiFilial) {

		if (Boolean.TRUE.equals(substituiFilial)) {
			/* Informa a data atual cado a data de consulta for nula */
			if (dtConsulta == null) {
    		dtConsulta = JTDateTimeUtils.getDataAtual();
    	}

			Filial filial = this.findMatchSubstAtendimentoFilialByIdFilialDestino(tpDesvioCarga, idFilial, idNaturezaProduto, idServico, idUFOrigem, idRegionalOrigem, idFilialOrigem, idClienteRemetente, idMunicipioDestino);
            if (filial != null) {
				return filial;
		}
        }

		return (Filial) getSubstAtendimentoFilialDAO().getAdsmHibernateTemplate().load(Filial.class, idFilial);
    	}
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param dao Instância do DAO.
     */
    public void setSubstAtendimentoFilialDAO(SubstAtendimentoFilialDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SubstAtendimentoFilialDAO getSubstAtendimentoFilialDAO() {
        return (SubstAtendimentoFilialDAO) getDao();
    }

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}

}