package com.mercurio.lms.integracao.model.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.integracao.model.GetFile;
import com.mercurio.lms.integracao.model.dao.GetFileDAO;
/**
 * Classe de serviço para CRUD:   
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.integracao.getFileService"
 */
public class GetFileService extends CrudService<GetFile, Long> {
	
	public ResultSetPage findPaginated(String filial, FindDefinition findDef) {
		
		List<Map> result = new LinkedList<Map>();
		ResultSetPage rsp = this.getGetFileDAO().findPaginated(filial, findDef);
		
		List<GetFile> queryResult = rsp.getList();
		for (GetFile getFile : queryResult) {
			Map<String, Object> registro = new HashMap<String, Object>();
			registro.put("filial", getFile.getFilial());
			registro.put("dtAtualizacao", getFile.getDtAtualizacaoFormat());
			result.add(registro);
		}
		rsp.setList(result);
		return rsp;
	}
	
	/** 
     * 
     * @param Instância do DAO.
     */
    public void setGetFileDAO(GetFileDAO dao) {
        setDao( dao );
    }
	
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos 
     * dados deste serviço.
     * 
     * @return Instância do DAO.
     */
    private GetFileDAO getGetFileDAO() {
        return (GetFileDAO) getDao();
    }
}