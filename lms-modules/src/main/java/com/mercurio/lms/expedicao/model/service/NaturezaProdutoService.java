package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.dao.NaturezaProdutoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.naturezaProdutoService"
 */
public class NaturezaProdutoService extends CrudService<NaturezaProduto, Long> {

	/**
	 * Recupera uma instância de <code>NaturezaProduto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public NaturezaProduto findById(java.lang.Long id) {
		return (NaturezaProduto)super.findById(id);
	}

	/**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param dsNaturezaProduto
     * @return <Lista de NaturezaProduto>  
     */
    public List findByDsNaturezaProduto(String dsNaturezaProduto) {
    	return getNaturezaProdutoDAO().findByDsNaturezaProduto(dsNaturezaProduto);
    }
	
	public List findOrderByDsNaturezaProduto(Map criteria) {
		List campoOrdenacao = new ArrayList();
		campoOrdenacao.add("dsNaturezaProduto:asc");
		return getDao().findListByCriteria(criteria, campoOrdenacao);
	}

	/**
	 * Busca a naturezaProduto de um conhecimento.
	 * @param idConhecimento
	 * @return
	 */
	public NaturezaProduto findByIdConhecimento(Long idConhecimento){
		return getNaturezaProdutoDAO().findByIdConhecimento(idConhecimento);
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(NaturezaProduto bean) {
		return super.store(bean);
	}

	public List findAllAtivo() {
		return getNaturezaProdutoDAO().findAllAtivo();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setNaturezaProdutoDAO(NaturezaProdutoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private NaturezaProdutoDAO getNaturezaProdutoDAO() {
		return (NaturezaProdutoDAO) getDao();
	}

	/**
	 * LMS-6885 - Busca opções para diretiva "chosen" de
	 * {@link NaturezaProduto}.
	 * 
	 * @return
	 */
	public List<NaturezaProduto> findChosen() {
		return getNaturezaProdutoDAO().findChosen();
	}

	public String findDsNaturezaProdutoByIdControleCarga(Long idControleCarga) {
		String dsNaturezaProduto = getNaturezaProdutoDAO().findDsNaturezaProdutoByIdControleCarga(idControleCarga);
		return dsNaturezaProduto.replaceAll("pt_BR»", "").replaceAll("¦", "");
	}

}
