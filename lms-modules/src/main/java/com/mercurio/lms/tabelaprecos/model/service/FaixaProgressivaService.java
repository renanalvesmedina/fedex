package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.dao.FaixaProgressivaDAO;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;

/**
 * Classe de servi�o para CRUD:   
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.faixaProgressivaService"
 */
public class FaixaProgressivaService extends CrudService<FaixaProgressiva, Long> {
	private TabelaPrecoService tabelaPrecoService;
	private ValorFaixaProgressivaService valorFaixaProgressivaService;
	/**
	 * Recupera uma inst�ncia de <code>FaixaProgressiva</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public FaixaProgressiva findById(java.lang.Long id) {
		return (FaixaProgressiva)super.findById(id);
	}

	private void validateRemove(Long id) {
		FaixaProgressiva faixaProgressiva = (FaixaProgressiva)findById(id);
		for(Iterator it = faixaProgressiva.getValoresFaixasProgressivas().iterator();it.hasNext();) {
			valorFaixaProgressivaService.validateVigenciasRemocao((ValorFaixaProgressiva)it.next());
		}
	}

	
	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for(Iterator it = ids.iterator();it.hasNext();){
			validateRemove((Long)it.next());
		}
		validateTabelasPrecoEfetivadas(ids);
		
		/*
		 * Verifica se a TabelaPreco relacionada com FaixaProgressiva possui Workflow
		 * pendente de aprova��o. Como a TabelaPreco � igual para todos os
		 * FaixaProgressiva, realiza a valida��o somente para o primeiro FaixaProgressiva.
		 */
		validateHistoricoWorkflow((Long) ids.get(0));
		super.removeByIds(ids);
	}

	private void validateHistoricoWorkflow(Long id) {
		FaixaProgressiva faixaProgressiva = this.findById(id);
		tabelaPrecoService.validatePendenciaWorkflow(faixaProgressiva.getTabelaPrecoParcela().getTabelaPreco());
	}
	
	/**
	 * Apaga uma entidade atrav�s do Id.
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		validateRemove(id);
		validateHistoricoWorkflow(id);
		
		List ids = new ArrayList(1);
		ids.add(id);
		validateTabelasPrecoEfetivadas(ids);

		super.removeById(id);
	}

	/**
     * Valida se a tabela preco esta efetiva para os ids faixaProgressiva
     * @param ids
     */
    public void validateTabelasPrecoEfetivadas(List ids){
    	Integer count = getFaixaProgressivaDAO().getCountTabelaPrecoEfetuada(ids);
    	if(CompareUtils.gt(count, IntegerUtils.ZERO)) throw new BusinessException("LMS-30042");
    }

	public java.io.Serializable store(FaixaProgressiva bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @param Inst�ncia do DAO.
	 */
	public void setFaixaProgressivaDAO(FaixaProgressivaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @return Inst�ncia do DAO.
	 */
	private FaixaProgressivaDAO getFaixaProgressivaDAO() {
		return (FaixaProgressivaDAO) getDao();
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getFaixaProgressivaDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		rsp.setList(AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	/**
	 * Retorna a Faixa de Valor enquadrada pelo Peso de Refer�ncia.
	 * 
	 * @param idTabelaPreco
	 * @param idParcelaPreco
	 * @param pesoReferencia
	 * @return
	 */
	public FaixaProgressiva findFaixaProgressivaEnquadrada(Long idTabelaPreco, Long idParcelaPreco, BigDecimal pesoReferencia) {
		return findFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, pesoReferencia, null);
	}

	/**
	 * Retorna a Faixa de Valor enquadrada pelo Peso de Refer�ncia.
	 *  
	 * @param idTabelaPreco
	 * @param idParcelaPreco
	 * @param pesoReferencia
	 * @return FaixaProgressiva
	 */
	public FaixaProgressiva findFaixaProgressivaEnquadrada(Long idTabelaPreco, Long idParcelaPreco, BigDecimal pesoReferencia, Long idProdutoEspecifico) {
		FaixaProgressiva faixaProgressiva = getFaixaProgressivaDAO().findFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, pesoReferencia, idProdutoEspecifico);
		if(faixaProgressiva == null) {
			throw new BusinessException("LMS-04125");
		}
		return faixaProgressiva;
	}

	/**
	 * Retorna a Faixa de menor peso.
	 * 
	 * @param idTabelaPreco
	 * @param idParcelaPreco
	 * @return FaixaProgressiva
	 */
	public FaixaProgressiva findFaixaProgressivaMenorPeso(Long idTabelaPreco, Long idParcelaPreco) {
		return getFaixaProgressivaDAO().findFaixaProgressivaMenorPeso(idTabelaPreco, idParcelaPreco);
	}
	
	public List<FaixaProgressiva> findFaixaProgressivaParaMarkup(Long idParcelaPreco, Long idTabelaPreco) {
		return getFaixaProgressivaDAO().findFaixaProgressivaParaMarkup(idParcelaPreco, idTabelaPreco);
	}
	
	public List<FaixaProgressiva> findByIds(List<Long> idsFaixaProgressiva) {
		return getFaixaProgressivaDAO().findByIds(idsFaixaProgressiva);
	}

	public void setValorFaixaProgressivaService(
			ValorFaixaProgressivaService valorFaixaProgressivaService) {
		this.valorFaixaProgressivaService = valorFaixaProgressivaService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
}