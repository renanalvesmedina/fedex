package com.mercurio.lms.tabelaprecos.model.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.SubtipoTabelaPrecoDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.subtipoTabelaPrecoService"
 */
public class SubtipoTabelaPrecoService extends CrudService<SubtipoTabelaPreco, Long>     {

	/**
	 * Recupera uma inst�ncia de <code>SubtipoTabelaPreco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public SubtipoTabelaPreco findById(java.lang.Long id) {
        return (SubtipoTabelaPreco)super.findById(id);
    }

    /**
     * M�todo utilizado pela Integra��o
     * @author Andre Valadas
     * 
     * @param tpSubtipoTabelaPreco
     * @return SubtipoTabelaPreco
     */
    public SubtipoTabelaPreco findByTpSubtipoTabelaPreco(String tpSubtipoTabelaPreco) {
        return getSubtipoTabelaPrecoDAO().findByTpSubtipoTabelaPreco(tpSubtipoTabelaPreco);
    }

    public List findByTpTipoTabelaPreco(Map<String, Object> criteria) {
    	String tpTipoTabelaPreco = (String)criteria.get("tpTipoTabelaPreco");
    	String blTabelaFob = (String) criteria.get("blTabelaFob");
    	if ("N".equals(blTabelaFob)) {
    		return getSubtipoTabelaPrecoDAO().findByTpTipoTabelaPreco(tpTipoTabelaPreco, Boolean.FALSE);
    	}
    	return getSubtipoTabelaPrecoDAO().findByTpTipoTabelaPreco(tpTipoTabelaPreco, null);
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
    public java.io.Serializable store(SubtipoTabelaPreco bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setSubtipoTabelaPrecoDAO(SubtipoTabelaPrecoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private SubtipoTabelaPrecoDAO getSubtipoTabelaPrecoDAO() {
        return (SubtipoTabelaPrecoDAO) getDao();
    }
    
    /* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	protected SubtipoTabelaPreco beforeStore(SubtipoTabelaPreco bean) {
		super.beforeStore(bean);
		SubtipoTabelaPreco sttp = (SubtipoTabelaPreco)bean;
		sttp.setEmpresa(SessionUtils.getEmpresaSessao());
		return sttp;
	}
	
	public List findByTipoSelecionadoOuTipoNulo(Map criteria)
	{
		String tpTipoTabelaPreco = (String)criteria.get("tpTipoTabelaPreco");
		if(tpTipoTabelaPreco!=null)
		{
			return AliasToNestedMapResultTransformer.getInstance().transformListResult(getSubtipoTabelaPrecoDAO().findByTipoSelecionadoOuTipoNulo(tpTipoTabelaPreco,false));
		}
		return Collections.EMPTY_LIST;
	}

	public List findByTipoSelecionadoOuTipoNuloActiveValues(Map criteria)
	{
		String tpTipoTabelaPreco = (String)criteria.get("tpTipoTabelaPreco");
		if(tpTipoTabelaPreco!=null)
		{
			return AliasToNestedMapResultTransformer.getInstance().transformListResult(getSubtipoTabelaPrecoDAO().findByTipoSelecionadoOuTipoNulo(tpTipoTabelaPreco,true));
		}
		return Collections.EMPTY_LIST;
	}
	
}