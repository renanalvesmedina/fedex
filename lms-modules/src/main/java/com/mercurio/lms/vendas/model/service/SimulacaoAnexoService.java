package com.mercurio.lms.vendas.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.SimulacaoAnexo;
import com.mercurio.lms.vendas.model.dao.SimulacaoAnexoDAO;

/**
 * LMS-6172 - Automatização efetivação da proposta - Solicitar Efetivação da
 * Proposta
 * 
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @author FabianoP
 * 
 * @spring.bean id="lms.contasreceber.simulacaoAnexoService"
 */
public class SimulacaoAnexoService extends CrudService<SimulacaoAnexo, Long> {

	private SimulacaoService simulacaoService;

	public void setSimulacaoAnexoDAO(SimulacaoAnexoDAO simulacaoAnexoDAO) {
		setDao(simulacaoAnexoDAO);
	}

	public SimulacaoAnexoDAO getSimulacaoAnexoDAO() {
		return (SimulacaoAnexoDAO) getDao();
	}

	public SimulacaoService getSimulacaoService() {
		return simulacaoService;
	}

	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}

	/**
	 * Busca instância de <tt>SimulacaoAnexo</tt> pelo atributo
	 * <tt>idSimulacaoAnexo</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>idSimulacaoAnexo<tt>
	 * @return instância de <tt>SimulacaoAnexo</tt>
	 */
	public SimulacaoAnexo findSimulacaoAnexo(TypedFlatMap criteria) {
		Long idSimulacaoAnexo = criteria.getLong("idSimulacaoAnexo");
		return getSimulacaoAnexoDAO().findSimulacaoAnexo(idSimulacaoAnexo);
	}

	/**
	 * Busca lista de <tt>SimulacaoAnexo</tt> relacionadas a uma
	 * <tt>Simulacao</tt> com id correspondente atributo
	 * <tt>simulacao.idSimulacao</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return lista de <tt>SimulacaoAnexo</tt>
	 */
	public ResultSetPage<SimulacaoAnexo> findSimulacaoAnexoList(TypedFlatMap criteria) {
		return getSimulacaoAnexoDAO().findSimulacaoAnexoList(criteria);
	}

	/**
	 * Busca quantidade de <tt>SimulacaoAnexo</tt> relacionadas a uma
	 * <tt>Simulacao</tt> com id correspondente atributo
	 * <tt>simulacao.idSimulacao</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return quantidade de <tt>SimulacaoAnexo</tt>
	 */
	public Integer findSimulacaoAnexoRowCount(TypedFlatMap criteria) {
		Long idSimulacao = criteria.getLong("simulacao.idSimulacao");
		return getSimulacaoAnexoDAO().findSimulacaoAnexoRowCount(idSimulacao);
	}

	@Override
	public Serializable store(SimulacaoAnexo simulacaoAnexo) {
		if(simulacaoAnexo.getDhInclusao() == null){
			simulacaoAnexo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		}
		return super.store(simulacaoAnexo);
	}
	
	/**
	 * Inclui ou atualiza instância de <tt>SimulacaoAnexo</tt> no banco de
	 * dados. Se existir atributo <tt>idSimulacaoAnexo</tt> no mapeamento faz a
	 * atualização, caso contrário faz a inserção relacionando também ao
	 * <tt>Simulacao<tt>.
	 * 
	 * @param criteria
	 *            mapa de atributos de <tt>SimulacaoAnexo</tt>
	 */
	public void storeSimulacaoAnexo(TypedFlatMap criteria) {
		Long idSimulacaoAnexo = criteria.getLong("idSimulacaoAnexo");
		SimulacaoAnexo simulacaoAnexo;
		if (idSimulacaoAnexo == null) {
			Long idSimulacao = criteria.getLong("simulacao.idSimulacao");
			Simulacao simulacao = simulacaoService.findById(idSimulacao);
			simulacaoAnexo = new SimulacaoAnexo();
			simulacaoAnexo.setSimulacao(simulacao);
			simulacaoAnexo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		} else {
			simulacaoAnexo = getSimulacaoAnexoDAO().findSimulacaoAnexo(idSimulacaoAnexo);
		}
		try {
			byte[] dsDocumento = Base64Util.decode(criteria.getString("dsDocumento"));
			simulacaoAnexo.setDsDocumento(dsDocumento);
		} catch (IOException e) {
			throw new InfrastructureException(e);
		}
		simulacaoAnexo.setDsAnexo(criteria.getString("dsAnexo"));
		getSimulacaoAnexoDAO().storeSimulacaoAnexo(simulacaoAnexo);
	}

	/**
	 * Exclui um conjunto de <tt>SimulacaoAnexo</tt> de acordo com a lista de
	 * id's mapeada no atributo <tt>ids</tt>.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>ids</tt>
	 */
	public void removeSimulacaoAnexo(TypedFlatMap criteria) {
		List<Long> ids = new ArrayList<Long>();
		for (Object object : criteria.getList("ids")) {
			if (object instanceof String) {
				ids.add(Long.valueOf((String) object));
			}
		}
		getSimulacaoAnexoDAO().removeByIds(ids);
	}

}
