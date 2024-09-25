package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ComposicaoCodigoVolume;
import com.mercurio.lms.edi.model.dao.ComposicaoCodigoVolumeDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.composicaoCodigoVolumeService"
 */

public class ComposicaoCodigoVolumeService extends CrudService<ComposicaoCodigoVolume, Long> {
	
	@Override
	public ComposicaoCodigoVolume findById(Long id) {		
		return (ComposicaoCodigoVolume)super.findById(id);
	}
	
	@Override
	public Serializable store(ComposicaoCodigoVolume bean) {
		return super.store(bean);
	}
	
	@Override
	public void removeById(Long id) {		
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
		
	public List findByComposicaoLayoutId(Long idComposicaoLayoutEDI){
		return this.getComposicaoCodigoVolumeDAO().findByComposicaoLayoutId(idComposicaoLayoutEDI);
	}
	
	public ResultSetPage<ComposicaoCodigoVolume> findPaginated(PaginatedQuery paginatedQuery) {
		return getComposicaoCodigoVolumeDAO().findPaginated(paginatedQuery);
	}  	

    private ComposicaoCodigoVolumeDAO getComposicaoCodigoVolumeDAO() {
        return (ComposicaoCodigoVolumeDAO) getDao();
    }
    
    public void setComposicaoCodigoVolumeDAO(ComposicaoCodigoVolumeDAO dao) {
        setDao(dao);
    }			
    
	public List<ComposicaoCodigoVolume> findByIdClienteLayoutEdi(Long idClienteLayoutEdi){
		return this.getComposicaoCodigoVolumeDAO().findByIdClienteLayoutEdi(idClienteLayoutEdi);
}

	public List<Object> findNomeByIdClienteLayoutEdi(Long idClienteLayoutEdi){
		List<Object[]> listao = this.getComposicaoCodigoVolumeDAO().findNomeByIdClienteLayoutEdi(idClienteLayoutEdi);
		List<Object> listaNomes = new ArrayList<Object>();
		
		for(Object[] l:listao) {
			for(Object nomes:l) {
				if(nomes != null) {
					listaNomes.add(nomes);
				}
			}
		}
	
		return listaNomes;
	}
	
	public List findNomeCampoVolumeByIdClienteLayoutEdi(Long idClienteLayoutEdi){
		return this.getComposicaoCodigoVolumeDAO().findNomeCampoVolumeByIdClienteLayoutEdi(idClienteLayoutEdi);
	}
}
