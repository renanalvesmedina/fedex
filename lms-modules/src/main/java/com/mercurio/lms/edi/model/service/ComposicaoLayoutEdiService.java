package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.RegistroLayoutEDI;
import com.mercurio.lms.edi.model.dao.ComposicaoLayoutEDIDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.composicaoLayoutEdiService"
 */

public class ComposicaoLayoutEdiService extends CrudService<ComposicaoLayoutEDI, Long> {
	
	@Override
	public ComposicaoLayoutEDI findById(Long id) {		
		return getComposicaoLayoutEDIDAO().findById(id);
	}
	
	@Override
	public Serializable store(ComposicaoLayoutEDI bean) {
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
	
	@Override
	protected ComposicaoLayoutEDI beforeStore(ComposicaoLayoutEDI bean) {
		if(bean.getIdComposicaoLayout() != null || getComposicaoLayoutEDIDAO().find(bean).isEmpty()){
			return bean;			
		}else{
			throw new BusinessException("LMS-00003");
		}		
	}
	
	public List<ComposicaoLayoutEDI> findComposicaoCliente(ComposicaoLayoutEDI composicao){
		 List<ComposicaoLayoutEDI> composicoes = getComposicaoLayoutEDIDAO().findComposicaoCliente( composicao);
		 List<ComposicaoLayoutEDI> retorno = new ArrayList<ComposicaoLayoutEDI>();
		 if(composicoes != null && composicoes.size() > 0){
			 for (ComposicaoLayoutEDI composicaoLayoutEDI : composicoes) {
				 ComposicaoLayoutEDI c = new ComposicaoLayoutEDI();
				 RegistroLayoutEDI r = new RegistroLayoutEDI();
				 r.setDescricao(composicaoLayoutEDI.getRegistroLayout().getDescricao());
				 r.setIdentificador(composicaoLayoutEDI.getRegistroLayout().getIdentificador());
				 r.setIdRegistroLayoutEdi(composicaoLayoutEDI.getRegistroLayout().getIdRegistroLayoutEdi());
				 r.setIdRegistroPai(composicaoLayoutEDI.getRegistroLayout().getIdRegistroPai());
				 r.setNomeIdentificador(composicaoLayoutEDI.getRegistroLayout().getNomeIdentificador());
				 r.setOcorrencias(composicaoLayoutEDI.getRegistroLayout().getOcorrencias());
				 r.setOrdem(composicaoLayoutEDI.getRegistroLayout().getOrdem());
				 r.setPreenchimento(composicaoLayoutEDI.getRegistroLayout().getPreenchimento());
				 r.setTamanhoRegistro(composicaoLayoutEDI.getRegistroLayout().getTamanhoRegistro());		
				 c.setIdComposicaoLayout(composicaoLayoutEDI.getIdComposicaoLayout());
				 c.setClienteLayoutEDI(composicaoLayoutEDI.getClienteLayoutEDI());
				 c.setRegistroLayout(r);
				 c.setValorDefault(composicaoLayoutEDI.getValorDefault());
				 retorno.add(c);
			}
		 }
		return retorno;
	}
	
	public ResultSetPage<ComposicaoLayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		return getComposicaoLayoutEDIDAO().findPaginated(paginatedQuery);
	}
	
	public ResultSetPage<ComposicaoLayoutEDI> findPaginatedReport(PaginatedQuery paginatedQuery) {
		return getComposicaoLayoutEDIDAO().findPaginatedReport(paginatedQuery);
	}  	

    private ComposicaoLayoutEDIDAO getComposicaoLayoutEDIDAO() {
        return (ComposicaoLayoutEDIDAO) getDao();
    }
    
    public void setComposicaoLayoutEDIDAO(ComposicaoLayoutEDIDAO dao) {
        setDao(dao);
    }		
	
	public List<ComposicaoLayoutEDI> findComposicaoClienteTipo(Map crit){
		return getComposicaoLayoutEDIDAO().findComposicaoClienteTipo(crit); 
}
	
}
