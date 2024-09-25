package com.mercurio.lms.vendas.model.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.HistoricoAnaliseCredito;
import com.mercurio.lms.vendas.model.SerasaCliente;
import com.mercurio.lms.vendas.model.dao.SerasaClienteDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.serasaClienteService"
 */
public class SerasaClienteService extends CrudService<SerasaCliente, Long> {

	public SerasaCliente findById(Long id) {
		return (SerasaCliente)super.findById(id);
	}

	public SerasaCliente findByIdHistoricoAnaliseCredito(Long idHistoricoAnaliseCredito) {
		return getSerasaClienteDAO().findByIdHistoricoAnaliseCredito(idHistoricoAnaliseCredito);
	}

	public SerasaCliente findByIdAnaliseCreditoCliente(Long idAnaliseCreditoCliente) {
		return getSerasaClienteDAO().findByIdAnaliseCreditoCliente(idAnaliseCreditoCliente);
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(SerasaCliente bean) {
		return super.store(bean);
	}

	/**
	 * Grava PDF de acordo com o HistoricoAnaliseCredito
	 * @param historicoAnaliseCredito
	 * @param imPdfSerasa
	 */
	public void storeSerasaCliente(HistoricoAnaliseCredito historicoAnaliseCredito, String imPdfSerasa) {
		if (StringUtils.isNotBlank(imPdfSerasa)) {
			try {
				byte[] decode = Base64Util.decode(imPdfSerasa);
				/** Verifica se foi informado um novo arquivo, para nao salvar ja existentes */
				if(decode.length > 1024) {
					SerasaCliente serasaCliente = new SerasaCliente();
					serasaCliente.setHistoricoAnaliseCredito(historicoAnaliseCredito);
					serasaCliente.setImPdfSerasa(decode);
					this.store(serasaCliente);
				}
			} catch (IOException e) {
				throw new InfrastructureException(e.getMessage());
			}
		}
	}

	public void setSerasaClienteDAO(SerasaClienteDAO dao) {
		setDao( dao );
	}
	private SerasaClienteDAO getSerasaClienteDAO() {
		return (SerasaClienteDAO) getDao();
	}
}