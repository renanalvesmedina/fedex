package com.mercurio.lms.workflow.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.workflow.model.EventoWorkflow;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

public interface EventoWorkflowService {

	/**
	 * Recupera uma inst�ncia de <code>EventoWorkflow</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public abstract EventoWorkflow findById(java.lang.Long id);

	public abstract EventoWorkflow findByTipoEvento(Short nrTipoEvento);

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public abstract void removeById(java.lang.Long id);

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public abstract void removeByIds(List ids);

	/**
	 * Retorna uma lista EventosWorkflow que se resultem do filtro
	 * @param Map map
	 * @return List
	 */
	public abstract List findLookupEventoWorkflow(Map criteria);
	
	public abstract Serializable store(EventoWorkflow bean);
	
	public abstract ResultSetPage findPaginated(Map map);
	
	public abstract Integer getRowCount(Map map);	
	
}