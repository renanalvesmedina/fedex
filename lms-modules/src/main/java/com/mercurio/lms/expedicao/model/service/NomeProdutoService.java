package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.dao.DetalheColetaDAO;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NomeProduto;
import com.mercurio.lms.expedicao.model.dao.NomeProdutoDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.detalheColetaService"
 */
public class NomeProdutoService extends CrudService<NomeProduto, Long> {

	private ProdutoService produtoService;

	public ProdutoService getProdutoService() {
        return produtoService;
    }

    public void setProdutoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public NomeProduto findById(java.lang.Long id) {
        return (NomeProduto)super.findById(id);
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
    public java.io.Serializable store(NomeProduto bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setNomeProdutoDAO(NomeProdutoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private NomeProdutoDAO getNomeProdutoDAO() {
        return (NomeProdutoDAO) getDao();
    }
    
	public List findNomesProduto(Long idProduto) {
		List listNomesProduto = new ArrayList();
		List listNomes = getNomeProdutoDAO().findNomeProdutoByIdProduto(idProduto);
		for(int i=0; i < listNomes.size(); i++) {
			NomeProduto nomeProduto = (NomeProduto) listNomes.get(i);
			Hibernate.initialize(nomeProduto.getProduto());

			listNomesProduto.add(i, nomeProduto);
		}
		return listNomesProduto;
	}
	
	public List findNomeProdutoByIdProduto(Long idProduto) {
		return getNomeProdutoDAO().findNomeProdutoByIdProduto(idProduto);
	}
	
	public Integer getRowCountByNomeProduto(Long idProduto) {
		return getNomeProdutoDAO().getRowCountNomeProduto(idProduto);
	}
	
	public boolean findBydsNomeProduto(Long idProduto, String dsNomeProduto) {
        return getNomeProdutoDAO().findBydsNomeProduto(idProduto, dsNomeProduto);
    }
}