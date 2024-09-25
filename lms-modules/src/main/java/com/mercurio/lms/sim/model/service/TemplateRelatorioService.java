package com.mercurio.lms.sim.model.service;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.sim.model.TemplateRelatorio;
import com.mercurio.lms.sim.model.dao.TemplateRelatorioDAO;
import com.mercurio.lms.util.ArquivoUtils;

/**
 * @author MarcelaDP
 *
 */

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sim.templateRelatorioService"
 */
public class TemplateRelatorioService extends CrudService<TemplateRelatorio, Long>{
	
	private TemplateRelatorioDAO getTemplateRelatorioDAO() {
        return (TemplateRelatorioDAO) getDao();
    }

	/**Popula o objeto dao 
	 * @param dao
	 */
	public void setTemplateRelatorioDAO(TemplateRelatorioDAO dao) {
        setDao(dao);
    }

	/**
	 * Retorna uma map com um array de bytes que representa o arquivo de
	 * template, desconsiderando o nome do arquivo, e uma string com o nome do
	 * arquivo.
	 * 
	 * @param idTemplateRelatorio
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map findArquivo(Long idTemplateRelatorio) throws UnsupportedEncodingException {
		Blob arquivo = getTemplateRelatorioDAO().findArquivo(idTemplateRelatorio);
		byte[] arquivoComNome = ArquivoUtils.toByteArray(arquivo);
		String nomeArquivo = new String(arquivoComNome, "ISO-8859-1");
		if (nomeArquivo != null && nomeArquivo.length() > 1024) {
			nomeArquivo = nomeArquivo.substring(0, 1024).trim();
		} else {
			nomeArquivo = "planilha";
		}

		Map retorno = new HashMap();
		retorno.put("arquivo", Arrays.copyOfRange(arquivoComNome, 1024, arquivoComNome.length));
		retorno.put("nomeArquivo", nomeArquivo);

		return retorno;
	}
	
	@Override
    public java.io.Serializable store(TemplateRelatorio templateRelatorio) {
		if(templateRelatorio.getIdTemplate() != null && templateRelatorio.getDcArquivo() == null){
			return getTemplateRelatorioDAO().update(templateRelatorio);
		}else{
			return super.store(templateRelatorio);
		}
    }
    
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    public TemplateRelatorio findById(java.lang.Long id) {
        return getTemplateRelatorioDAO().findById(id);
    }
    
    /**Pagina o resultado na listagem
     * @param paginatedQuery
     * @return
     */
    public ResultSetPage<TemplateRelatorio> findPaginated(PaginatedQuery paginatedQuery) {
		return getTemplateRelatorioDAO().findPaginated(paginatedQuery);
	}
    
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }
}
