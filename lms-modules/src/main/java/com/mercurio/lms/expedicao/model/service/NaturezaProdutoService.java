package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.dao.NaturezaProdutoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.naturezaProdutoService"
 */
public class NaturezaProdutoService extends CrudService<NaturezaProduto, Long> {

	/**
	 * Recupera uma inst�ncia de <code>NaturezaProduto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public NaturezaProduto findById(java.lang.Long id) {
		return (NaturezaProduto)super.findById(id);
	}

	/**
     * M�todo utilizado pela Integra��o
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
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
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
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setNaturezaProdutoDAO(NaturezaProdutoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private NaturezaProdutoDAO getNaturezaProdutoDAO() {
		return (NaturezaProdutoDAO) getDao();
	}

	/**
	 * LMS-6885 - Busca op��es para diretiva "chosen" de
	 * {@link NaturezaProduto}.
	 * 
	 * @return
	 */
	public List<NaturezaProduto> findChosen() {
		return getNaturezaProdutoDAO().findChosen();
	}

	public String findDsNaturezaProdutoByIdControleCarga(Long idControleCarga) {
		String dsNaturezaProduto = getNaturezaProdutoDAO().findDsNaturezaProdutoByIdControleCarga(idControleCarga);
		return dsNaturezaProduto.replaceAll("pt_BR�", "").replaceAll("�", "");
	}

}
