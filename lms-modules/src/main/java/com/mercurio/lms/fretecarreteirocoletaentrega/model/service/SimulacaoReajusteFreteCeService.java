package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.FilialReajuste;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaReajuste;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.SimulacaoReajusteFreteCeDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.simulacaoReajusteFreteCeService"
 */
public class SimulacaoReajusteFreteCeService extends CrudService<SimulacaoReajusteFreteCe, Long> {

	private Logger log = LogManager.getLogger(this.getClass());

	private ParcelaReajusteService parcelaReajusteService;
	
	private FilialReajusteService filialReajusteService;
	
	private ParcelaTabelaCeService parcelaTabelaCeService;
	
	private final static String[] tpsParcela = new String[] {"DH","EV","QU","FP"};
	
	private EnderecoPessoaService enderecoPessoaService;
	
	private MoedaPaisService moedaPaisService;
	
	private FilialService filialService;
	
	/**
	 * Recupera uma instância de <code>SimulacaoReajusteFreteCe</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public SimulacaoReajusteFreteCe findById(java.lang.Long id) {
        return (SimulacaoReajusteFreteCe)super.findById(id);
    }
    
    public List findViewParameters(TypedFlatMap parameters) {
    	return getSimulacaoReajusteFreteCeDAO().findViewParameters(parameters);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	deleteAll();
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }
    
    public boolean validateDataConsistence(SimulacaoReajusteFreteCe bean) {
    	SimulacaoReajusteFreteCe test = findFirstEntity(bean.getIdSimulacaoReajusteFreteCe());
		if (test != null) {
			if((test.getDtVigenciaFinal() != null && bean.getDtVigenciaFinal() != null && !test.getDtVigenciaFinal().equals(bean.getDtVigenciaFinal())) ||
					(test.getDtVigenciaFinal() != bean.getDtVigenciaFinal() && (test.getDtVigenciaFinal() == null || bean.getDtVigenciaFinal() == null)))
				return false;
			if(!test.getDtVigenciaInicial().equals(bean.getDtVigenciaInicial()))
				return false;
			if(!test.getTipoMeioTransporte().getIdTipoMeioTransporte().equals(bean.getTipoMeioTransporte().getIdTipoMeioTransporte()))
				return false;
			if(!test.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega().equals(bean.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega()))
				return false;
			
			if (!bean.getMoedaPais().getIdMoedaPais().equals(test.getMoedaPais().getIdMoedaPais()))
				return false;
			
			if (bean.getParcelaReajustes().size() != test.getParcelaReajustes().size())
				return false;
			
			if ((bean.getFilialReajustes() != null && bean.getFilialReajustes().size() != test.getFilialReajustes().size())
					|| bean.getFilialReajustes() == null &&  test.getFilialReajustes().size() != 0)
				return false;
			
			Map criteria;
			criteria = new HashMap();
			for(Iterator i = bean.getParcelaReajustes().iterator(); i.hasNext();) {
				ParcelaReajuste parcela = (ParcelaReajuste)i.next();
				criteria.put("tpParcela",parcela.getTpParcela().getValue());
				criteria.put("vlBruto",parcela.getVlBruto());
				criteria.put("pcPercentual",parcela.getVlReajustado());
				
				if (parcelaReajusteService.getRowCount(criteria). compareTo(Integer.valueOf(0)) == 0)
					return false;
			}
			if (bean.getFilialReajustes() != null) {
				criteria = new HashMap();
				Map criteriaFilial = new HashMap();
				for(Iterator i = bean.getFilialReajustes().iterator(); i.hasNext();) {
					FilialReajuste filialR = (FilialReajuste)i.next();
					criteriaFilial.put("idFilial",filialR.getFilial().getIdFilial());
					criteria.put("blAjusta",filialR.getBlAjusta());
					criteria.put("filial",criteriaFilial);
					if (filialReajusteService.getRowCount(criteria).compareTo(Integer.valueOf(0)) == 0)
						return false;
					
				}
			}
			return true;
		}
		return true;

    }

    @Override
    protected SimulacaoReajusteFreteCe beforeStore(SimulacaoReajusteFreteCe bean) {
		if (bean.getParcelaReajustes() == null || bean.getParcelaReajustes().size() == 0)
			throw new BusinessException("LMS-25019");

		//Regra refente --> Rotina: Salvar Último Reajuste - 2
		if (!validateDataConsistence(bean))
			throw new BusinessException("LMS-25018");
		
		if (bean.getIdSimulacaoReajusteFreteCe() != null) 
			return findFirstEntity(bean.getIdSimulacaoReajusteFreteCe());
		else
			return bean;
		
    }
    
    public SimulacaoReajusteFreteCe findFirstEntity(Long id) {
    	Map criteria = new HashMap();
    	if (id != null)
    		criteria.put("idSimulacaoReajusteFreteCe",id);
    	List simulacoes = find(criteria);
		if (simulacoes.size() > 0)
			return (SimulacaoReajusteFreteCe)simulacoes.get(0);
		return null;
    }
    
    private void deleteAll() {
    	for(Iterator i = find(null).iterator(); i.hasNext();) {
    		SimulacaoReajusteFreteCe bean = (SimulacaoReajusteFreteCe)i.next();
    		if (bean.getParcelaReajustes() != null)
    			bean.getParcelaReajustes().clear();
    		if (bean.getFilialReajustes() != null)
    			bean.getFilialReajustes().clear();
    		getSimulacaoReajusteFreteCeDAO().getAdsmHibernateTemplate().delete(bean);
    	}
    	getSimulacaoReajusteFreteCeDAO().getAdsmHibernateTemplate().flush();
    }
    
    public void executeEfetivarReajuste(SimulacaoReajusteFreteCe bean,TypedFlatMap parameters) {
    	if (!validateDataConsistence(bean))
			throw new BusinessException("LMS-25027");
		
		List rs = findViewParameters(parameters);
		if (rs != null && rs.size() > 0) {
			StringBuffer filiais = new StringBuffer();
			
			Iterator i = rs.iterator();
			Object[] projections = (Object[])i.next();
			filiais.append(projections[0]).append(" - ").append(projections[1]);
			
			for(; i.hasNext();) {
				projections = (Object[])i.next();
				filiais.append(", ").append(projections[0]).append(" - ").append(projections[1]);
			}
			throw new BusinessException("LMS-25020",new Object[]{filiais.toString()});
		}
		
		List tabelas = findEfetivarReajuste(parameters);
		if (tabelas.size() > 0) {
			for(Iterator i = tabelas.iterator(); i.hasNext();) {
				TabelaColetaEntrega tce = (TabelaColetaEntrega)i.next();
				YearMonthDay dtVigenciaInicial = parameters.getYearMonthDay("dtEmissaoInicial");
				YearMonthDay dtVigenciaFinal = parameters.getYearMonthDay("dtEmissaoFinal");
				
				tce.setDtVigenciaFinal(dtVigenciaInicial.minusDays(1));
				getSimulacaoReajusteFreteCeDAO().store(tce);
				
				TabelaColetaEntrega tceNew = new TabelaColetaEntrega();
				try {
					BeanUtils.copyProperties(tceNew,tce);
				} catch (IllegalAccessException e) {
					log.error(e);
				} catch (InvocationTargetException e) {
					log.error(e);
				}
				tceNew.setIdTabelaColetaEntrega(null);
				tceNew.setDtVigenciaInicial(dtVigenciaInicial);
				tceNew.setDtVigenciaFinal(dtVigenciaFinal);
				//Limpa os lista para evitar o erro: Found shared references to a collection
				tceNew.setControleCargas(null);
				tceNew.setParcelaTabelaCes(null);
				getSimulacaoReajusteFreteCeDAO().store(tceNew);
				for(int x = 0; x < tpsParcela.length; x++) {
					createNewParcelaTabelaCe(tceNew,tpsParcela[x],tce.getIdTabelaColetaEntrega(),bean.getIdSimulacaoReajusteFreteCe());
					getSimulacaoReajusteFreteCeDAO().getAdsmHibernateTemplate().flush();
				}

				getSimulacaoReajusteFreteCeDAO().getAdsmHibernateTemplate().flush();
			}
		}
    }

    public void createNewParcelaTabelaCe(TabelaColetaEntrega tce, String tpParcela, Long idTabelaColetaEntregaOld, Long idSimulacaoReajusteFreteCe) {
		ParcelaTabelaCe parcela = parcelaTabelaCeService.findParcelaTabelaCeByTpParcelaAndTabelaCE(tpParcela,idTabelaColetaEntregaOld);
		if (parcela == null)
			return;
		
		Map criteria = new HashMap();
		Map simulacaoReajusteFreteCe = new HashMap();
		simulacaoReajusteFreteCe.put("idSimulacaoReajusteFreteCe",idSimulacaoReajusteFreteCe);
		criteria.put("simulacaoReajusteFreteCe",simulacaoReajusteFreteCe);
		criteria.put("tpParcela",tpParcela);
		
		List rs = parcelaReajusteService.find(criteria);
		if (rs != null && rs.size() >= 1) {
			ParcelaReajuste pr = (ParcelaReajuste)rs.get(0);
			if (BigDecimalUtils.hasValue(pr.getVlBruto())) {
				//Se a variação for por Valor
				if (BigDecimalUtils.hasValue(parcela.getVlDefinido()))
					parcela.setVlDefinido(parcela.getVlDefinido().add(pr.getVlBruto()));
				else
					parcela.setVlDefinido(pr.getVlBruto());
			}else{
				//Se a variação for por Percentual
				if (BigDecimalUtils.hasValue(parcela.getVlDefinido())){
					BigDecimal valorReajustado = pr.getVlReajustado() == null ? BigDecimal.ZERO : pr.getVlReajustado();
					parcela.setVlDefinido(parcela.getVlDefinido().multiply(valorReajustado).divide(BigDecimal.valueOf(100l),2,BigDecimal.ROUND_UP).add(parcela.getVlDefinido()));
			}
		}
		}
		parcela.setTabelaColetaEntrega(tce);
		parcela.setIdParcelaTabelaCe(null);
		
		ParcelaTabelaCe parcelaNew = null;
		try {
			parcelaNew = (ParcelaTabelaCe)BeanUtils.cloneBean(parcela);
			parcelaNew.setIdParcelaTabelaCe(null);
		} catch (IllegalAccessException e) {
			log.error(e);
		} catch (InvocationTargetException e) {
			log.error(e);
		} catch (InstantiationException e) {
			log.error(e);
		} catch (NoSuchMethodException e) {
			log.error(e);
		}
		parcelaNew.setNotaCreditoParcelas(null);
		getSimulacaoReajusteFreteCeDAO().getAdsmHibernateTemplate().evict(parcela);
		getSimulacaoReajusteFreteCeDAO().store(parcelaNew);
    }
    
    public List findEfetivarReajuste(TypedFlatMap parameters) {
    	return getSimulacaoReajusteFreteCeDAO().findEfetivarReajuste(parameters);
    }
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    private Serializable store2(SimulacaoReajusteFreteCe bean) {
        super.store(bean);
        return bean;
    }

    public MoedaPais findMoedaPaisByFilial(Filial filial) {
    	if (filial == null)
    		throw new IllegalArgumentException("A filial não pode ser nula");
    	
    	Filial filialLoad = filialService.findById(filial.getIdFilial());
    	getSimulacaoReajusteFreteCeDAO().getAdsmHibernateTemplate().evict(filialLoad);
    	if (filialLoad.getMoeda() == null)
    		return null;
    	
    	Long idMoeda = filialLoad.getMoeda().getIdMoeda();
    	EnderecoPessoa endereco = enderecoPessoaService.findEnderecoPessoaPadrao(filialLoad.getIdFilial());

    	if (endereco == null)
    		return null;
    	Long idPais = endereco.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
    	
    	return moedaPaisService.findByPaisAndMoeda(idPais,idMoeda); 
    }
    
	// FIXME corrigir para retornar o ID
    public java.io.Serializable store(SimulacaoReajusteFreteCe bean, Boolean blAlterAccpet) {
    	if (bean.getFilialReajustes() != null && !bean.getFilialReajustes().isEmpty()) {
    		StringBuffer FiliaisError = new StringBuffer();
    		String token = "";
    		for(Iterator i = bean.getFilialReajustes().iterator();i.hasNext();) {
	    		FilialReajuste filialReajuste = (FilialReajuste)i.next();
	    		MoedaPais moedaPais = findMoedaPaisByFilial(filialReajuste.getFilial());
	    		if (moedaPais == null || !moedaPais.getIdMoedaPais().equals(bean.getMoedaPais().getIdMoedaPais())) {
	    			Filial filial = (Filial)getSimulacaoReajusteFreteCeDAO().getAdsmHibernateTemplate().get(Filial.class,filialReajuste.getFilial().getIdFilial());
	    			Pessoa pessoa = filial.getPessoa();
	    			FiliaisError.append(token).append(filial.getSgFilial()).append(" - ").append(pessoa.getNmFantasia());
	    			token = ", "; 
	    		}
    		}
    		if (FiliaisError.length() > 0)
    			throw new BusinessException("LMS-25035",new Object[] {FiliaisError.toString()});
    	}

    	VigenciaService.validaVigenciaInsercao(bean.getDtVigenciaInicial());
    		
    	if (blAlterAccpet == null || !blAlterAccpet.booleanValue()){
    		return store2(bean);
    	}else{
    		deleteAll();
    		bean.setIdSimulacaoReajusteFreteCe(null);
    		getSimulacaoReajusteFreteCeDAO().store(bean);
    		return bean;
    	}
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSimulacaoReajusteFreteCeDAO(SimulacaoReajusteFreteCeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SimulacaoReajusteFreteCeDAO getSimulacaoReajusteFreteCeDAO() {
        return (SimulacaoReajusteFreteCeDAO) getDao();
    }

	public void setParcelaReajusteService(
			ParcelaReajusteService parcelaReajusteService) {
		this.parcelaReajusteService = parcelaReajusteService;
	}

	public void setFilialReajusteService(FilialReajusteService filialReajusteService) {
		this.filialReajusteService = filialReajusteService;
	}

	public void setParcelaTabelaCeService(
			ParcelaTabelaCeService parcelaTabelaCeService) {
		this.parcelaTabelaCeService = parcelaTabelaCeService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


   }
