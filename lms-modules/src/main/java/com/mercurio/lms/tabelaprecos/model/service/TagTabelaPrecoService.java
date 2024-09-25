package com.mercurio.lms.tabelaprecos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.tabelaprecos.model.TagTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.TagTabelaPrecoDAO;

public class TagTabelaPrecoService extends CrudService<TagTabelaPreco, Long>{

	public void setTagTabelaPrecoDAO(TagTabelaPrecoDAO dao) {
		setDao(dao);
	}

	public TagTabelaPreco findByTag(String tag) {
		return ((TagTabelaPrecoDAO) this.getDao()).findByTag(tag);
	}

	public ResultSetPage<TagTabelaPreco> findAll() {
		List<TagTabelaPreco> todos = ((TagTabelaPrecoDAO) this.getDao()).findAllEntities();
		return new ResultSetPage<TagTabelaPreco>(0, todos);
	}

}
