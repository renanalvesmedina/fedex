package com.mercurio.lms.vendas.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.DivisaoParcela;
import com.mercurio.lms.vendas.model.dao.DivisaoParcelaDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.divisaoParcelaService"
 */
public class DivisaoParcelaService extends CrudService<DivisaoParcela, Long> {
	
	@Override
	public DivisaoParcela findById(Long id) {
        return getDivisaoParcelaDao().findById(id);
    } 

	public List<DivisaoParcela> find(Long idTabelaDivisaoCliente, Long idParcelaPreco, Long idParcelaPrecoCobranca) {
		return getDivisaoParcelaDao().find(idTabelaDivisaoCliente, idParcelaPreco, idParcelaPrecoCobranca);
	}

	public List<Map<String, Object>> findByDivisaoCliente(Long idDivisaoCliente, String tabelaPreco) {
		return getDivisaoParcelaDao().findByDivisaoCliente(idDivisaoCliente, tabelaPreco, null);
	}

	public List<Map<String, Object>> findByTabelaDivisaoCliente(Long idTabelaDivisaoCliente) {
		return getDivisaoParcelaDao().findByDivisaoCliente(null, null, idTabelaDivisaoCliente);
	}

	public List<Map<String, Object>> findByDivisaoCliente(Long idDivisaoCliente, String tabelaPreco, Long idTabelaDivisaoCliente) {
		return getDivisaoParcelaDao().findByDivisaoCliente(idDivisaoCliente, tabelaPreco, idTabelaDivisaoCliente);
	}

	public DivisaoParcelaDAO getDivisaoParcelaDao() {
		return (DivisaoParcelaDAO) getDao();
	}
	
	public void setDivisaoParcelaDAO(DivisaoParcelaDAO divisaoParcelaDAO) {
		this.setDao(divisaoParcelaDAO);
	}

}
