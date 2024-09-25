package com.mercurio.lms.vendas.model.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.AnaliseCreditoCliente;
import com.mercurio.lms.vendas.model.HistoricoAnaliseCredito;
import com.mercurio.lms.vendas.model.SerasaCliente;
import com.mercurio.lms.vendas.model.dao.HistoricoAnaliseCreditoDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.historicoAnaliseCreditoService"
 */
public class HistoricoAnaliseCreditoService extends CrudService<HistoricoAnaliseCredito, Long> {
	private SerasaClienteService serasaClienteService;

	public HistoricoAnaliseCredito findById(Long id) {
		return (HistoricoAnaliseCredito)super.findById(id);
	}

	public HistoricoAnaliseCredito findByIdCliente(Long idCliente) {
		return getHistoricoAnaliseCreditoDAO().findByIdCliente(idCliente);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		ResultSetPage result = getHistoricoAnaliseCreditoDAO().findPaginated(criteria, def);

		List<TypedFlatMap> historicos = result.getList();
		for (TypedFlatMap historico : historicos) {
			/** Busca PDF Serasa */
			SerasaCliente serasaCliente = serasaClienteService.findByIdHistoricoAnaliseCredito(historico.getLong("idHistoricoAnaliseCredito"));
			if (serasaCliente != null) {
				historico.put("serasaCliente.idSerasaCliente", serasaCliente.getIdSerasaCliente());
			}
		}
		return result;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getHistoricoAnaliseCreditoDAO().getRowCount(criteria);
	}

	public List<HistoricoAnaliseCredito> findByIdAnaliseCreditoCliente(Long idAnaliseCreditoCliente) {
		return getHistoricoAnaliseCreditoDAO().findByIdAnaliseCreditoCliente(idAnaliseCreditoCliente);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void removeById(Long id) {
		super.removeById(id);
		serasaClienteService.removeById(id);
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
		serasaClienteService.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(HistoricoAnaliseCredito bean) {
		return super.store(bean);
	}

	/**
	 * Gera Historico Analise de Credito e PDF Serasa vinculado ao mesmo
	 * @param analiseCreditoCliente
	 * @param tpEvento
	 * @param obEvento
	 * @param imPdfSerasa
	 */
	public void generateHistoricoAnaliseCredito(AnaliseCreditoCliente analiseCreditoCliente, DomainValue tpEvento, String obEvento, String imPdfSerasa) {
		HistoricoAnaliseCredito historicoAnaliseCredito = new HistoricoAnaliseCredito();
		historicoAnaliseCredito.setAnaliseCreditoCliente(analiseCreditoCliente);
		historicoAnaliseCredito.setUsuario(SessionUtils.getUsuarioLogado());
		historicoAnaliseCredito.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
		historicoAnaliseCredito.setTpEvento(tpEvento);
		historicoAnaliseCredito.setObEvento(obEvento);
		this.store(historicoAnaliseCredito);

		/** STORE PDF Serasa vinculado ao Historico Gerado*/
		if(!StringUtils.isEmpty(imPdfSerasa)){
			serasaClienteService.storeSerasaCliente(historicoAnaliseCredito, imPdfSerasa);
		}
	}
	public void generateHistoricoAnaliseCredito(AnaliseCreditoCliente analiseCreditoCliente, DomainValue tpEvento, String obEvento) {
		this.generateHistoricoAnaliseCredito(analiseCreditoCliente, tpEvento, obEvento, null);
	}

	public void setHistoricoAnaliseCreditoDAO(HistoricoAnaliseCreditoDAO dao) {
		setDao( dao );
	}
	private HistoricoAnaliseCreditoDAO getHistoricoAnaliseCreditoDAO() {
		return (HistoricoAnaliseCreditoDAO) getDao();
	}

	public void setSerasaClienteService(SerasaClienteService serasaClienteService) {
		this.serasaClienteService = serasaClienteService;
	}
}