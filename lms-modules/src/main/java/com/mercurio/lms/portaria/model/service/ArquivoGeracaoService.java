package com.mercurio.lms.portaria.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.portaria.model.ArquivoGeracao;
import com.mercurio.lms.portaria.model.dao.ArquivoGeracaoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.arquivoGeracaoService"
 */
public class ArquivoGeracaoService extends CrudService<ArquivoGeracao, Long>  {

	public ArquivoGeracaoDAO getArquivoGeracaoDAO() {
		return (ArquivoGeracaoDAO)getDao();
	}

	public void setArquivoGeracaoDAO(ArquivoGeracaoDAO arquivoGeracaoDAO) {
		setDao(arquivoGeracaoDAO);
	}
	
	public ResultSetPage findPaginatedDownloadArquivos(Map criteria) {
		ResultSetPage resultSetPage = getArquivoGeracaoDAO().findPaginatedDownloadArquivos(criteria);
		
		List listaRetorno = new ArrayList();
		for (Iterator iter = resultSetPage.getList().iterator(); iter.hasNext();) {
			ArquivoGeracao arquivoGeracao = (ArquivoGeracao) iter.next();
			
			Map map = new HashMap();
			map.put("idArquivoGeracao", arquivoGeracao.getIdArquivoGeracao());
			map.put("sgFilial", arquivoGeracao.getFilial().getSgFilial() + " - " + arquivoGeracao.getFilial().getPessoa().getNmFantasia());
			map.put("tpDocumento", arquivoGeracao.getTpDocumento());
			map.put("nrDocumento", arquivoGeracao.getNrDocumento());
			map.put("dsAcaoIntegracao", arquivoGeracao.getAcaoIntegracao().getDsAcaoIntegracao());
			map.put("dtGeracao", arquivoGeracao.getDtGeracao());
			map.put("dtGeracaoFormatada", arquivoGeracao.getDtGeracaoFormatada());
			map.put("arquivo",Base64Util.encode(arquivoGeracao.getArquivo())  );
			
			listaRetorno.add(map);
		}
		resultSetPage.setList(listaRetorno);
		
		return resultSetPage;
	}
	
	public Integer getRowCountDownloadArquivos(Map criteria) {
		return getArquivoGeracaoDAO().getRowCountDownloadArquivos(criteria);
	}
	
}
