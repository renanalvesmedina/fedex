package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Equipe;
import com.mercurio.lms.carregamento.model.IntegranteEquipe;
import com.mercurio.lms.carregamento.model.dao.EquipeDAO;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.equipeService"
 */
public class EquipeService extends CrudService<Equipe, Long> {

	private FuncionarioService funcionarioService;
	private IntegranteEquipeService integranteEquipeService;

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}
	public void setIntegranteEquipeService(IntegranteEquipeService integranteEquipeService) {
		this.integranteEquipeService = integranteEquipeService;
	}


	/**
	 * Recupera uma instância de <code>Equipe</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public Equipe findById(java.lang.Long id) {
        return (Equipe)super.findById(id);
    }

	public ResultSetPage findPaginated(Map criteria) {
		Filial filial = SessionUtils.getFilialSessao();
        criteria.put("filial.idFilial", filial.getIdFilial());
		return super.findPaginated(criteria);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	integranteEquipeService.removeByIdEquipe(id);    	
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
    	for(Iterator it=ids.iterator();it.hasNext();){
    		Long idEquipe = (Long)it.next();
        	integranteEquipeService.removeByIdEquipe(idEquipe);
    	}
        super.removeByIds(ids);
    }

    public TypedFlatMap getDataUsuario() {
    	Filial filialSessao = SessionUtils.getFilialSessao();
    	TypedFlatMap map = new TypedFlatMap();
    	map.put("filial.idFilial", filialSessao.getIdFilial());
    	map.put("filial.sgFilial", filialSessao.getSgFilial());
    	map.put("filial.pessoa.nmFantasia", filialSessao.getPessoa().getNmFantasia());
		return map;
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @param list 
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Equipe equipe, ItemList itemList, ItemListConfig config) {
    	
		if (!itemList.hasItems()) {
			throw new BusinessException("LMS-05101");
		} else {
			
			for (Iterator iter = itemList.iterator(equipe.getIdEquipe(), config); iter.hasNext();) {
	    		IntegranteEquipe integranteEquipe = (IntegranteEquipe) iter.next();
	    		integranteEquipe.setEquipe(equipe);
			}
		}
		
		boolean rollbackMasterId = equipe.getIdEquipe() == null;
		
    	try {
    		validaIntegrantesEmEquipes(itemList);

        	Long masterId = null;
			this.beforeStore(equipe);
			equipe = getEquipeDAO().store(equipe, itemList); 
			masterId = equipe.getIdEquipe();			
	        return masterId;
			
    	 } catch (RuntimeException e) {
    		 this.rollbackMasterState(equipe, rollbackMasterId, e); 
             itemList.rollbackItemsState();   
             throw e;
    	 }
    }

    
    /**
     * 
     * @param items
     */
	private void validaIntegrantesEmEquipes(ItemList items) {
		List listTerceiro = new ArrayList();
		List listFuncionario = new ArrayList();
		
		Map mapRemovedTerceiro = new HashMap();
		Map mapRemovedFuncionario = new HashMap();
		populateRegistrosRemovidos(items, mapRemovedTerceiro, mapRemovedFuncionario);

		for (Iterator iter = items.getNewOrModifiedItems().iterator(); iter.hasNext();) {
			IntegranteEquipe integranteEquipe = (IntegranteEquipe) iter.next();
			// Terceiro
			if (integranteEquipe.getTpIntegrante().getValue().endsWith("T")) {
				// verifica se o registro é novo
				if (integranteEquipe.getIdIntegranteEquipe() == null) {
					String strIdPessoa = integranteEquipe.getPessoa().getIdPessoa().toString();
					// verifica se o novo registro não está na lista dos registros excluídos
					if (mapRemovedTerceiro.containsKey(strIdPessoa)) {
						Long idIntegranteEquipe = (Long)mapRemovedTerceiro.get(strIdPessoa);
						validaIntegrantesEmEquipes(true, integranteEquipe, idIntegranteEquipe);
					} else {
						listTerceiro.add(integranteEquipe.getPessoa().getIdPessoa());
					}
				} else {
					validaIntegrantesEmEquipes(true, integranteEquipe, integranteEquipe.getIdIntegranteEquipe());
				}
			} else if (integranteEquipe.getTpIntegrante().getValue().endsWith("F")) {
				// verifica se o registro é novo
				if (integranteEquipe.getIdIntegranteEquipe() == null) {
					String strIdUsuario = integranteEquipe.getUsuario().getIdUsuario().toString();
					// verifica se o novo registro não está na lista dos registros excluídos
					if (mapRemovedFuncionario.containsKey(strIdUsuario)) {
						Long idIntegranteEquipe = (Long)mapRemovedFuncionario.get(strIdUsuario);
						validaIntegrantesEmEquipes(false, integranteEquipe, idIntegranteEquipe);
					} else {
						listFuncionario.add(integranteEquipe.getUsuario().getIdUsuario());
					}
				} else {
					validaIntegrantesEmEquipes(false, integranteEquipe, integranteEquipe.getIdIntegranteEquipe());
				}
			}
		}

		if (!listTerceiro.isEmpty() && !getEquipeDAO().findIntegrantesEmEquipes(null, Boolean.valueOf(true), listTerceiro).isEmpty()){
			throw new BusinessException("LMS-05005");
		}
   			
		if (!listFuncionario.isEmpty() && !getEquipeDAO().findIntegrantesEmEquipes(null, Boolean.valueOf(false), listFuncionario).isEmpty()){
			throw new BusinessException("LMS-05004");
		}
	}

	/**
	 * Povoa o Map (terceiro ou funcionario) com o seu respectivo id. Dessa forma, possibilita que se possa verificar se um 
	 * determinado registro foi excluído.
	 * 
	 * @param items
	 * @param mapRemovedTerceiro
	 * @param mapRemovedFuncionario
	 */
	private void populateRegistrosRemovidos(ItemList items, Map mapRemovedTerceiro, Map mapRemovedFuncionario) {
		for (Iterator iter = items.getRemovedItems().iterator(); iter.hasNext();) {
			IntegranteEquipe integranteEquipe = (IntegranteEquipe) iter.next();
			if (integranteEquipe.getPessoa() != null)
				mapRemovedTerceiro.put(integranteEquipe.getPessoa().getIdPessoa().toString(), integranteEquipe.getIdIntegranteEquipe());
			else
				mapRemovedFuncionario.put(integranteEquipe.getUsuario().getIdUsuario().toString(), integranteEquipe.getIdIntegranteEquipe());
		}
	}

	/**
	 * 
	 * @param isTerceiro
	 * @param integranteEquipe
	 * @param idIntegranteEquipe
	 */
	private void validaIntegrantesEmEquipes(boolean isTerceiro, IntegranteEquipe integranteEquipe, Long idIntegranteEquipe) {
		List list = new ArrayList();
		String exceptionKey = "";
		if (isTerceiro) {
			list.add(integranteEquipe.getPessoa().getIdPessoa());
			exceptionKey = "LMS-05005";
		}
		else {
			list.add(integranteEquipe.getUsuario().getIdUsuario());
			exceptionKey = "LMS-05004";
		}
		if (!getEquipeDAO().findIntegrantesEmEquipes(idIntegranteEquipe, Boolean.valueOf(isTerceiro), list).isEmpty())
			throw new BusinessException(exceptionKey);
	}

	
    /**
     * Verifica se um Terceiro é um funcionário. Se for, será lançada uma exceção.
     * 
     * @param nrIdentificacao
     */
    public void validateTerceiroIsFuncionario(String nrIdentificacao) {
    	if (nrIdentificacao == null)
    		return;
    	
    	Map mapFuncionario = new HashMap();
    	mapFuncionario.put("nrCpf", PessoaUtils.clearIdentificacao(nrIdentificacao));

    	if (!funcionarioService.find(mapFuncionario).isEmpty()) {
    		throw new BusinessException("LMS-05006");
    	}
	}

    
    
    public java.io.Serializable store(Equipe bean){
    	return super.store(bean);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEquipeDAO(EquipeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EquipeDAO getEquipeDAO() {
        return (EquipeDAO) getDao();
    }
    
	public List findIntegranteEquipe(Long masterId) {
		return getEquipeDAO().findIntegranteEquipeByEquipeId(masterId);
	}
	
	
	public Equipe findByDescricao(String descricao) {
		return getEquipeDAO().findByDescricao(descricao);
	}
	
	public Equipe findByDsEquipeFilial(String dsEquipe, Filial filial){
		return getEquipeDAO().findByDsEquipeFilial(dsEquipe, filial);
	}
	
	
}