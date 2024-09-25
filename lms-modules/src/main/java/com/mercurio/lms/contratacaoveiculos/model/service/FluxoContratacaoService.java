package com.mercurio.lms.contratacaoveiculos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.FluxoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.dao.FluxoContratacaoDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.FluxoContratacaoService"
 */
public class FluxoContratacaoService extends CrudService<FluxoContratacao, Long> {
	private RotaIdaVoltaService rotaIdaVoltaService;
	private DomainValueService domainValueService;
	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFluxoContratacaoDAO(FluxoContratacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FluxoContratacaoDAO getFluxoContratacaoDAO() {
        return (FluxoContratacaoDAO) getDao();
    }
    
    /**
     * Se passado o idSolicitacaoContratacao, busca a lista de fluxos no banco, do contrário, 
     * monta a lista de acordo com a rota passada no parâmetro
     * @param criteria
     * @return
     */
    public List<FluxoContratacao> find(TypedFlatMap criteria) {
    	Long idSolicitacaoContratacao = criteria.getLong("idSolicitacaoContratacao");
    	//Busca no banco
    	if (idSolicitacaoContratacao != null) {
    		return findByIdSolicitacaoContratacao(idSolicitacaoContratacao);
    	} else {
    		return generateFluxos(criteria);
    	}
    }

	private List<FluxoContratacao> generateFluxos(TypedFlatMap criteria) {
		List<FluxoContratacao> toReturn = new ArrayList<FluxoContratacao>();
		String tpFluxoContratacao = criteria.getDomainValue("tpFluxoContratacao").getValue();
		DomainValue tpAbrangencia = criteria.getDomainValue("tpAbrangencia");
		
		List<TypedFlatMap> rotas = createListRotas(criteria);
		
		if ("O".equals(tpFluxoContratacao)) {
			if (rotas.size() > 0) {
				//Pega a última filial da rota como filial destino, os outros são origem
				TypedFlatMap filialDestinoMap = (TypedFlatMap)rotas.get(rotas.size() - 1);
				Filial filialDestino = createPojoFilial(filialDestinoMap);
				
				for(TypedFlatMap filialOrigemMap: rotas) {
					Filial filialOrigem = createPojoFilial(filialOrigemMap);
					if (! filialDestino.getSgFilial().equals(filialOrigem.getSgFilial())) {
		    			toReturn.addAll(createPojos(filialOrigem, filialDestino, tpAbrangencia));
					}
		    	}
			}
		} else if ("D".equals(tpFluxoContratacao)) {
			Filial filialOrigem = null;
			for(TypedFlatMap filialMap: rotas) {
				//O primeiro da lista é a origem, os outros são destino
				if (filialOrigem == null) {
					filialOrigem = createPojoFilial(filialMap);
				} else {
					Filial filialDestino = createPojoFilial(filialMap);
					toReturn.addAll(createPojos(filialOrigem, filialDestino, tpAbrangencia));
				}
			}
		} else if ("A".equals(tpFluxoContratacao)) {
			List<TypedFlatMap> rotas2 = new ArrayList<TypedFlatMap>();
			rotas2.addAll(rotas);
			for(int i = 0; i < rotas.size(); i++) {
				TypedFlatMap filialOrigemMap = (TypedFlatMap)rotas.get(i);
				Filial filialOrigem = createPojoFilial(filialOrigemMap);
				for (int x = i + 1; x < rotas2.size(); x++) {
					TypedFlatMap filialDestinoMap = (TypedFlatMap)rotas2.get(x);
					Filial filialDestino = createPojoFilial(filialDestinoMap);
					toReturn.addAll(createPojos(filialOrigem, filialDestino, tpAbrangencia));
				}
			}
		}
		if (toReturn.size() == 1) {
			//Regra 4.1 Quando existir um único fluxo, atribuir 100% por default para ele. 
			((FluxoContratacao)toReturn.get(0)).setPcValorFrete(new BigDecimal(100));
		}
		return toReturn;
	}
	
	private List<TypedFlatMap> createListRotas(TypedFlatMap criteria) {
		List<TypedFlatMap> toReturn = new ArrayList<TypedFlatMap>();
		Long idRotaIdaVolta = criteria.getLong("idRotaIdaVolta");
		if (idRotaIdaVolta == null) {
			if (criteria.get("rotas") instanceof List) {
				toReturn = criteria.getList("rotas");
			}
		} else {
			List<FilialRota> filiaisRota = getRotaIdaVoltaService().findFiliaisRotaByIdRotaIdaVolta(idRotaIdaVolta);
			for (FilialRota filialRota: filiaisRota) {
				TypedFlatMap tfmTemp = new TypedFlatMap();
				tfmTemp.put("filial.idFilial", filialRota.getFilial().getIdFilial());
				tfmTemp.put("filial.sgFilial", filialRota.getFilial().getSgFilial());
				toReturn.add(tfmTemp);
			}
		}
		return toReturn;
	}

    /**
     * Retorna os fluxos de contratação da solicitação de contratação
     * @param idSolicitacaoContratacao
     * @return
     */
    public List<FluxoContratacao> findByIdSolicitacaoContratacao(Long idSolicitacaoContratacao) {
    	return getFluxoContratacaoDAO().find(idSolicitacaoContratacao, null, null);
    }

    /**
     * Retorna os fluxos contratação da solicitação cuja filial de origem está implantada na data do parâmetro today, 
     * ou cujo tipo de abrangência seja o tipo de abrangência informado
     * @param idSolicitacaoContratacao
     * @param today Data a ser considerada no teste se a filial já foi implantada (normalmente a data atual)
     * @param tpAbrangencia Tipo de abrangência a ser considerada, caso o fluxo seja do tipo de abrangência, 
     * 	desconsidera a data de implantação
     * @return
     */
    public List<FluxoContratacao> find(Long idSolicitacaoContratacao, YearMonthDay today, String tpAbrangencia) {
    	return getFluxoContratacaoDAO().find(idSolicitacaoContratacao, today, tpAbrangencia);
    }

	private Filial createPojoFilial(TypedFlatMap filialMap) {
		Filial filial = new Filial();
		filial.setIdFilial(filialMap.getLong("filial.idFilial"));
		filial.setSgFilial(filialMap.getString("filial.sgFilial"));
		return filial;
	}

	private List<FluxoContratacao> createPojos(Filial filialOrigem,
			Filial filialDestino, DomainValue tpAbrangencia) {
		List<FluxoContratacao> toReturn = new ArrayList<FluxoContratacao>();
		if ("A".equals(tpAbrangencia.getValue())) {
			//Adiciona um fluxo para cada abrangência (nacional e internacional)
			//Nacional
			FluxoContratacao row = createPojo(filialOrigem, filialDestino, domainValueService.findDomainValueByValue("DM_ABRANGENCIA_TOTAL", "N"));
			toReturn.add(row);

			//Internacional
			row = createPojo(filialOrigem, filialDestino, domainValueService.findDomainValueByValue("DM_ABRANGENCIA_TOTAL", "I"));
			toReturn.add(row);
		} else {
			FluxoContratacao row = createPojo(filialOrigem, filialDestino,
					domainValueService.findDomainValueByValue("DM_ABRANGENCIA_TOTAL", tpAbrangencia.getValue()));
			toReturn.add(row);
		}
		return toReturn;
	}

	private FluxoContratacao createPojo(Filial filialOrigem,
			Filial filialDestino, DomainValue tpAbrangencia) {
		FluxoContratacao row;
		row = new FluxoContratacao();
		row.setIdFluxoContratacao(null);
		row.setFilialOrigem(filialOrigem);
		row.setFilialDestino(filialDestino);
		row.setNrChaveLiberacao(null);
		row.setPcValorFrete(null);
		row.setTpAbrangencia(tpAbrangencia);
		return row;
	}
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	return getFluxoContratacaoDAO().getRowCount(criteria);
    }
    
    /**
     * Recebe um list de TypedFlatMap que vem da tela e salva no banco
     * @param idSolicitacaoContratacao
     * @param beans
     */
    public void storeList(Long idSolicitacaoContratacao, List<FluxoContratacao> beans) {
    	if (idSolicitacaoContratacao != null) {
	    	List<FluxoContratacao> oldList = findByIdSolicitacaoContratacao(idSolicitacaoContratacao);
	    	boolean exists;
	    	for (FluxoContratacao fluxoContratacaoOld: oldList) {
	    		exists = false;
	    		for (FluxoContratacao fluxoContratacao: beans) {
	    			if (fluxoContratacaoOld.getIdFluxoContratacao().equals(fluxoContratacao.getIdFluxoContratacao())) {
	    				exists = true;
	    				break;
	    			}
	    		}
	    		if (! exists) {
	    			removeById(fluxoContratacaoOld.getIdFluxoContratacao());
	    		}
	    	}
    	}
    	for (FluxoContratacao fluxoContratacao: beans) {
    		store(fluxoContratacao);
    	}
    }
    
    public java.io.Serializable store(FluxoContratacao bean) {
    	return super.store(bean);
    }

	public RotaIdaVoltaService getRotaIdaVoltaService() {
		return rotaIdaVoltaService;
	}

	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	
	/**
	 * 
	 * @param idSolicitacaoContratacao (required)
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return
	 */
	public List findFluxoContratacaoByIdSolicitacaoContratacao(Long idSolicitacaoContratacao, Long idFilialOrigem, Long idFilialDestino) {
		return getFluxoContratacaoDAO().
					findFluxoContratacaoByIdSolicitacaoContratacao(idSolicitacaoContratacao, idFilialOrigem, idFilialDestino);
	}
}