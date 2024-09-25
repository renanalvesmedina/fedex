package com.mercurio.lms.vendas.model.service;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.DeParaXmlCte;
import com.mercurio.lms.vendas.model.dao.DeParaXmlCteDAO;

public class DeParaXmlCteService extends CrudService<DeParaXmlCte, Long> {
	@Override
	public DeParaXmlCte findById(java.lang.Long id) {
		DeParaXmlCte deParaXmlCte = (DeParaXmlCte) super.findById(id);
		deParaXmlCte.getCliente().getPessoa().getNrIdentificacao();
		return deParaXmlCte;
	}

	public ResultSetPage<Map<String, Object>> findPaginated(TypedFlatMap criteria) {
		return getDeParaXmlCteDao().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}

	private DeParaXmlCteDAO getDeParaXmlCteDao() {
		return (DeParaXmlCteDAO) getDao();
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getDeParaXmlCteDao().getRowCount(criteria);
	}

	public SqlTemplate getSqlTemplate(TypedFlatMap criteria) {
		return getDeParaXmlCteDao().getSqlTemplate(criteria);
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	public void setDeParaXmlCteDao(DeParaXmlCteDAO deParaXmlCteDAO) {
		setDao(deParaXmlCteDAO);
	}

	@Override
	public java.io.Serializable store(DeParaXmlCte deParaXmlCte) {
		return super.store(deParaXmlCte);
	}

	@Transactional
	public String getByXNome(String xNome, Long idCliente) {
		String retorno=getDeParaXmlCteDao().getByXNome(xNome,idCliente);
		if(retorno==null || retorno.equals("")){
			retorno=xNome;
		}
		return retorno;
	}

	@Transactional
	public boolean verificaSeJaExiste(String xNome, Long idCliente, Boolean blMatriz) {
		return getDeParaXmlCteDao().verificaSeJaExiste(xNome, idCliente, blMatriz);
	}
}
