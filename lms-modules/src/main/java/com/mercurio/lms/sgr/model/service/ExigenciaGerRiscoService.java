package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.dao.ExigenciaGerRiscoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.exigenciaGerRiscoService"
 */
public class ExigenciaGerRiscoService extends CrudService<ExigenciaGerRisco, Long> {
	
	/**
	 * Recupera uma instância de <code>ExigenciaGerRisco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ExigenciaGerRisco findById(java.lang.Long id) {
        return (ExigenciaGerRisco)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    @Override
    public void removeById(Long id) {
        ExigenciaGerRisco bean = findById(id);

		// LMS-6847
		((ExigenciaGerRiscoDAO) getDao()).deletePerifericosRastreador(bean);

        super.removeById(id);
        // Ordena a lista que o item pertencia
        reordenaLista(bean.getTipoExigenciaGerRisco().getIdTipoExigenciaGerRisco());
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

    /**
     * Apaga várias entidades através do Id.
     * Neste método, cada lista de tipoExigencia conforme a exigencia removida será reordenada. 
     * @param ids lista com as entidades que deverão ser removida.
     */
	@ParametrizedAttribute(type = java.lang.Long.class)    
    public void removeByIdsWithOrdering (List<Long> ids) {
        List<Long> idsTipoExigencia = new ArrayList<Long>();
        for(Long id : ids) {
            ExigenciaGerRisco exigenciaGerRisco = findById(id);

			// LMS-6847
            ((ExigenciaGerRiscoDAO) getDao()).deletePerifericosRastreador(exigenciaGerRisco);

            Long idTipo = exigenciaGerRisco.getTipoExigenciaGerRisco().getIdTipoExigenciaGerRisco();
            if (!idsTipoExigencia.contains(idTipo)) {
                idsTipoExigencia.add(idTipo);
            }
        }

        super.removeByIds(ids);
        
        //Para cada tipoExigencia será reordenada a lista que o item removido pertencia 
        for(Long idTipoExigencia : idsTipoExigencia) {
            reordenaLista(idTipoExigencia);
        }
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
    public ExigenciaGerRisco store(ExigenciaGerRisco bean) {
        // Este teste é necessário para garantir que o nrNivel que está sendo
        // atualizado é o atual, caso o usuario atualize na tela de Ordem os registros
        // e depois volte para o detalhe
        
        if (bean.getIdExigenciaGerRisco()!=null) {
            ExigenciaGerRisco oldBean = findById(bean.getIdExigenciaGerRisco()); 
            oldBean.setDsCompleta(bean.getDsCompleta());
            oldBean.setDsResumida(bean.getDsResumida());
            oldBean.setTpSituacao(bean.getTpSituacao());
           
            Long oldIdTipoExigencia = oldBean.getTipoExigenciaGerRisco().getIdTipoExigenciaGerRisco(); 
            Long newIdTipoExigencia = bean.getTipoExigenciaGerRisco().getIdTipoExigenciaGerRisco();
           
            // Verifica se houve troca de tipoExigencia ou no tpSituacao, 
            //se sim atualiza o nrNivel com o proximo nrNivel da lista ou seta para 0 se o registro for inativo
           if (oldIdTipoExigencia.longValue()!= newIdTipoExigencia.longValue() ) {
               if (oldBean.getTpSituacao().getValue().equalsIgnoreCase("A")) {
                   oldBean.setNrNivel(findNextNrNivel(newIdTipoExigencia));
               } else {
                   oldBean.setNrNivel(Long.valueOf(0));
               }
           } else {
               if (oldBean.getTpSituacao().getValue().equalsIgnoreCase("A")) {
                   oldBean.setNrNivel(findNextNrNivel(newIdTipoExigencia));
               } else {
                   oldBean.setNrNivel(Long.valueOf(0));
               }
           }
                   
           oldBean.setTipoExigenciaGerRisco(bean.getTipoExigenciaGerRisco());

           // LMS-6847
           oldBean.setTpCriterioAgrupamento(bean.getTpCriterioAgrupamento());
           oldBean.setBlAreaRisco(bean.getBlAreaRisco());
           ((ExigenciaGerRiscoDAO) getDao()).deletePerifericosRastreador(oldBean);
           ((ExigenciaGerRiscoDAO) getDao()).storePerifericosRastreador(bean);

           // LMS-7255
           oldBean.setCdExigenciaGerRisco(bean.getCdExigenciaGerRisco());

           bean = oldBean;
           // Foi necessário fazer no store ao invés do beforeupdate, pois é 
           // necessário atualizar o bean antes de reordenar o antigo.
           super.store(bean);
        } else {
            super.store(bean);    

			// LMS-6847
			((ExigenciaGerRiscoDAO) getDao()).storePerifericosRastreador(bean);
        }
        reordenaLista(bean.getTipoExigenciaGerRisco().getIdTipoExigenciaGerRisco());
        return bean;        
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setExigenciaGerRiscoDAO(ExigenciaGerRiscoDAO dao) {
        setDao(dao);
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar 
     * a persistência dos dados deste serviço.
     * @return Instância do DAO.
     */
    private ExigenciaGerRiscoDAO getExigenciaGerRiscoDAO() {
        return (ExigenciaGerRiscoDAO) getDao();
    }
	
    /**
	 * Neste método será feita a atribuição do nivel da Exigência.
	 * O nrNivel deve ser igual ao maior nivel existente + 1 
	 */
    @Override
	protected ExigenciaGerRisco beforeInsert(ExigenciaGerRisco bean) {
        Long maxNivel = Long.valueOf(0);
        if(bean.getTpSituacao().getValue().equalsIgnoreCase("A")) {
            maxNivel = this.findNextNrNivel(bean.getTipoExigenciaGerRisco().getIdTipoExigenciaGerRisco());
        }
        bean.setNrNivel(maxNivel);
		return super.beforeInsert(bean);
	}
    
    /**
     * Método para reordenar a lista, quando é alterado o tipoExigencia de um item.
     * @param idTipoExigencia
     */
    public void reordenaLista (Long idTipoExigencia) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> tipoExigencia = new HashMap<String, Object>(); 
        
        tipoExigencia.put("idTipoExigenciaGerRisco", idTipoExigencia);
        map.put("tipoExigenciaGerRisco", tipoExigencia);

        List<ExigenciaGerRisco> result = findListExigenciasByTipoExigencia(map);
        ExigenciaGerRisco oldBean = null;
        long tmp = 1;
        for(ExigenciaGerRisco bean : result) {
            oldBean = findById(bean.getIdExigenciaGerRisco());
            oldBean.setNrNivel(Long.valueOf(tmp));
            getExigenciaGerRiscoDAO().store(oldBean);
            tmp++;
        }
    }

    /**
     * Método que busca o proximo nrNivel, baseado no idTipoExigencia
     * @param bean
     */
    private Long findNextNrNivel(Long idTipoExigencia) {
        return getExigenciaGerRiscoDAO().findNextNrNivel(idTipoExigencia);
    }

	/**
     * Realiza uma pesquisa de Exigências baseada no Tipo de Exigência  
     * @param criteria contém o ID do Tipo de Exigência 
     * @return Exigências do Tipo de Exigência informado
     */
    public List findListExigenciasByTipoExigencia(Map<String, Object> criteria) {
        List<String> campoOrdenacao = new ArrayList<String>(1);
        campoOrdenacao.add("nrNivel:asc");
        criteria.put("tpSituacao", "A");
       return getDao().findListByCriteria(criteria, campoOrdenacao);
    }

	/**
	 * Método para retornar uma list ordenada. Utilizado em combobox.
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ExigenciaGerRisco> findOrdenadoPorNivel(Map<String, Object> criteria) {
		criteria.put("tpSituacao", "A");
		List<String> campoOrdenacao = new ArrayList<String>(1);
		campoOrdenacao.add("nrNivel:asc");
		return getDao().findListByCriteria(criteria, campoOrdenacao);
	}

	/**
	 * Método que recebe a coleção de exigências e atualiza	o nrNivel
     * da Exigência. 
	 * @param map
	 */
    public void storeListOrder(TypedFlatMap map) {
        // Teste para ver se a lista de exigencias não está vazia
        if (map.containsKey("exigenciasGerRisco")) {
        	List<TypedFlatMap> listExigencias = map.getList("exigenciasGerRisco");
        	ExigenciaGerRisco exigenciaGerRisco = null;
        	for(TypedFlatMap mapExigencia : listExigencias) {
				exigenciaGerRisco = findById(mapExigencia.getLong("idExigenciaGerRisco"));
				exigenciaGerRisco.setNrNivel(mapExigencia.getLong("nrNivel")+1);
				getExigenciaGerRiscoDAO().store(exigenciaGerRisco);
        	}
        }
    }

	public List<ExigenciaGerRisco> findByTipoExigenciaGerRisco(List<Long> idsTipoExigenciaGerRisco) {
		return getExigenciaGerRiscoDAO().findByTipoExigenciaGerRisco(idsTipoExigenciaGerRisco);
	}

}
