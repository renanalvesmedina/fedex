package com.mercurio.lms.tabelaprecos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;
import com.mercurio.lms.tabelaprecos.model.dao.ValorTaxaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.valorTaxaService"
 */
public class ValorTaxaService extends CrudService<ValorTaxa, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ValorTaxa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public ValorTaxa findById(java.lang.Long id) {
        return (ValorTaxa)super.findById(id);
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
    public java.io.Serializable store(ValorTaxa bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setValorTaxaDAO(ValorTaxaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ValorTaxaDAO getValorTaxaDAO() {
        return (ValorTaxaDAO) getDao();
    }
    
    public TypedFlatMap findByIdTabelaPrecoParcelaIdTabelaPreco(Long idTabelaPrecoParcela, Long idTabelaPreco) {
    	return getValorTaxaDAO().findByIdTabelaPrecoParcelaIdTabelaPreco(idTabelaPrecoParcela, idTabelaPreco);
    }
    
    public ResultSetPage findPaginatedByIdTabelaPreco(TypedFlatMap criteria) {
    	Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
    	FindDefinition def = FindDefinition.createFindDefinition(criteria);
    	return getValorTaxaDAO().findPaginatedByIdTabelaPreco(idTabelaPreco, def);
    }
    
    public Integer getRowCountByIdTabelaPreco(TypedFlatMap criteria) {
    	Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
    	return getValorTaxaDAO().getRowCountByIdTabelaPreco(idTabelaPreco);
    }
    
}