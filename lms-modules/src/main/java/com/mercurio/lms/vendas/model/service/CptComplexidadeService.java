package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.vendas.model.CptComplexidade;
import com.mercurio.lms.vendas.model.dao.CptComplexidadeDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.cptComplexidadeService"
 */
public class CptComplexidadeService extends CrudService<CptComplexidade, Long> {
	
	public Serializable findById(Long id) {		
		return super.findById(id);
	}	
	
	/**
	 * Salva a entidade
	 */
	public Serializable store(CptComplexidade bean) {		
		return super.store(bean);
	}
	
	/**
	 * Remove uma entidade
	 */
	public void removeById(Long id) {		
		super.removeById(id);
	}
	
	/**
	 * Remove uma lista de entidade
	 */
	@Override
	public void removeByIds(List<Long> ids) {		
		super.removeByIds(ids);
	}
	
	@Override
	public Integer getRowCount(Map criteria) {		
		return super.getRowCount(criteria);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {		
		return super.findPaginated(criteria);
	}
	
	public List findTiposComplexidade(Long idCptTipoValor) {
		if(idCptTipoValor == null){
			return null;
		}
		List<Map> list = getCptComplexidadeDAO().findTiposComplexidade(idCptTipoValor);
		for(Map map : list){
			map.put("descricao", MapUtilsPlus.getDomainValue(map, "tpComplexidade").getDescription().getValue()+" "+
					MapUtilsPlus.getBigDecimal(map, "vlComplexidade")+" "+
					MapUtilsPlus.getDomainValue(map, "tpMedidaComplexidade").getDescription().getValue() );
		}		
		return list;
	}
	
    public void setCptComplexidadeDAO(CptComplexidadeDAO dao) {
        setDao( dao );
    }
	
	private CptComplexidadeDAO getCptComplexidadeDAO(){
		return (CptComplexidadeDAO) getDao();
	}

	
}