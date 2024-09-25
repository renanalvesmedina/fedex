package com.mercurio.lms.tabelaprecos.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.TagTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TagTabelaPrecoService;

public class TagsTabelaPrecoAction {
	
	private TagTabelaPrecoService tagTabelaPrecoService;
	
	public void setTagTabelaPrecoService(TagTabelaPrecoService tagTabelaPrecoService) {
		this.tagTabelaPrecoService = tagTabelaPrecoService;
	}

	/**
     * 
     * @param criteria
     * @return
     */
	@SuppressWarnings("unchecked")
	public ResultSetPage findDicionarioTags(Map criteria) {
    	ResultSetPage rsp = tagTabelaPrecoService.findPaginated(criteria);
    	List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>();
    	for (Iterator<TagTabelaPreco> iter = rsp.getList().iterator(); iter.hasNext();) {
    		TagTabelaPreco ttp = iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("parcelaPreco", ttp.getIdParcelaPreco());
    		tfm.put("descricao", ttp.getDescricao());
    		tfm.put("tag", ttp.getTag());
    		tfm.put("observacoes", ttp.getObservacoes());
    		retorno.add(tfm);
    	}
    	rsp.setList(retorno);
    	return rsp;
    }
    

    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountDicionarioTags(Map criteria) {
    	return tagTabelaPrecoService.getRowCount(criteria);
    }
    
}
