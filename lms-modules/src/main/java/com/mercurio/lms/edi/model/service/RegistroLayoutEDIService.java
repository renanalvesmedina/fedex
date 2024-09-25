package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.RegistroLayoutEDI;
import com.mercurio.lms.edi.model.dao.RegistroLayoutEDIDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.registroLayoutEDIService"
 */

public class RegistroLayoutEDIService extends CrudService<RegistroLayoutEDI, Long> {

	
	@Override
	public RegistroLayoutEDI findById(Long id) {		
		return (RegistroLayoutEDI)super.findById(id);
	}
	
	@Override
	public Serializable store(RegistroLayoutEDI bean) {
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
	
    public RegistroLayoutEDIDAO getRegistroLayoutEDIDAO() {
        return (RegistroLayoutEDIDAO) getDao();
    }
    
    public void setRegistroLayoutEDIDAO(RegistroLayoutEDIDAO dao) {
        setDao(dao);
    }	
	
	public ResultSetPage<RegistroLayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		return getRegistroLayoutEDIDAO().findPaginated(paginatedQuery);
	}

	public List<RegistroLayoutEDI> findFilhos(Long idLayoutEDI, Long idRegistroPai, String cabecalho){
		return getRegistroLayoutEDIDAO().findFilhos(idLayoutEDI, idRegistroPai, cabecalho);
	}
	
	public List<RegistroLayoutEDI> findFilhosPorTipo(Long idLayoutEDI, Long idRegistroPai, String cabecalho, String...args){
		return getRegistroLayoutEDIDAO().findFilhosPorTipo(idLayoutEDI, idRegistroPai, cabecalho, args);
	}	
	
	
	public List<RegistroLayoutEDI> findByIdLayoutEDI(Long idLayoutEDI){
		return getRegistroLayoutEDIDAO().findByIdLayoutEDI(idLayoutEDI);
	}
	
	public Long findIdRegistroPrincipal(Long idLayoutEDI){
		return getRegistroLayoutEDIDAO().findIdRegistroPrincipal(idLayoutEDI);
	}
	
	public List<RegistroLayoutEDI> findLookupRegistro(Map<String, Object> params){
		return getRegistroLayoutEDIDAO().findLookupRegistro(params);
	}
	
	public Long findIdRegistroCabecalho(Long idLayoutEDI, String cabecalho){
		return getRegistroLayoutEDIDAO().findIdRegistroCabecalho(idLayoutEDI,cabecalho);
	}
	
	public List<RegistroLayoutEDI> findDadosQuebraArquivo(Long idLayoutEDI){
		return getRegistroLayoutEDIDAO().findDadosQuebraArquivo(idLayoutEDI);
	}
	
	public Map<String, String> findDadosQuebraArquivoMapped(final Long idLayoutEDI) {
		final List<RegistroLayoutEDI> registros = this.findDadosQuebraArquivo(idLayoutEDI);
		final Map<String, String> mapToReturn = new HashMap<String, String>();
		if (registros != null) {
			for (final RegistroLayoutEDI registroLayoutEDI : registros) {
				mapToReturn.put(registroLayoutEDI.getNomeIdentificador(), registroLayoutEDI.getIdentificador().toString());
			}
		}
		return mapToReturn;
	}
	
	public Long findIdRegistroPrincipalPorTipo(Long idClienteLayoutEdi, String tipoLayout){
		return getRegistroLayoutEDIDAO().findIdRegistroPrincipalPorTipo(idClienteLayoutEdi, tipoLayout);
	}
	
	public Long findIdRegistroPrincipalXml(Long idClienteLayoutEdi, String tipoLayout){
		return getRegistroLayoutEDIDAO().findIdRegistroPrincipalXml(idClienteLayoutEdi, tipoLayout);
	}	
	
	public Long findLayoutPorTipo(Long idClienteLayoutEdi, String tipoLayout){
		return getRegistroLayoutEDIDAO().findLayoutEdiPorTipo(idClienteLayoutEdi, tipoLayout);
}

	public Long findClienteLayoutPorTipo(Long idClienteLayoutEdi, String tipoLayout){
		return getRegistroLayoutEDIDAO().findClienteLayoutPorTipo(idClienteLayoutEdi, tipoLayout);
	}	
}
