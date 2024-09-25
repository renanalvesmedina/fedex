package com.mercurio.lms.sim.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.sim.model.UsuarioResponsavelClienteCCT;
import com.mercurio.lms.sim.model.dao.ClienteUsuarioCCTDAO;

public class ClienteUsuarioCCTService extends CrudService<UsuarioResponsavelClienteCCT, Long>{
	
	private ClienteUsuarioCCTDAO getClienteUsuarioCCTDAO() {
        return (ClienteUsuarioCCTDAO) getDao();
    }
	public void setClienteUsuarioCCTDAO(ClienteUsuarioCCTDAO dao) {
        setDao(dao);
    }

	public ResultSetPage findPaginated(PaginatedQuery paginatedQuery) {
		return getClienteUsuarioCCTDAO().findPaginated(paginatedQuery);
	}
	public UsuarioResponsavelClienteCCT findById(java.lang.Long id) {
        return getClienteUsuarioCCTDAO().findById(id);
    }
	
	public UsuarioResponsavelClienteCCT findById(java.lang.Long id,  String[] fetches) {
        return getClienteUsuarioCCTDAO().findById(id, fetches);
    }
	
	@Override
    public java.io.Serializable store(UsuarioResponsavelClienteCCT usuarioResponsavelClienteCCT) {	
		return super.store(usuarioResponsavelClienteCCT);
	}
    
	 public void removeById(java.lang.Long id) {
	    super.removeById(id);
	 }
    
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }
    
    public Map findClienteCCTByUsuario(Long idUsuario){
    	List result = getClienteUsuarioCCTDAO().findClienteCCTByUsuario(idUsuario);
    	Map retorno = new HashMap();
    	if (!result.isEmpty()){
			retorno = (Map<String, Object>)result.get(0);
			retorno.put("nrIdentificacaoRemetente", retorno.get("nrIdentificacao"));
			retorno.put("nrIdentificacao", retorno.get("nrIdentificacao"));
    	}
		return retorno;
    }
    
}
