package com.mercurio.lms.tabelaprecos.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.dao.ValorFaixaProgressivaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.valorFaixaProgressivaService"
 */
public class ValorFaixaProgressivaService extends CrudService<ValorFaixaProgressiva, Long> {

	private TabelaPrecoService tabelaPrecoService;
	
	/**
	 * Recupera uma instância de <code>ValorFaixaProgressiva</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public ValorFaixaProgressiva findById(java.lang.Long id) {
		return (ValorFaixaProgressiva)super.findById(id);
	}

	public TypedFlatMap findByIdCustom(Long id) {
		return getValorFaixaProgressivaDAO().findByIdCustom(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		ValorFaixaProgressiva valorFaixaProgressiva = findById(id);
		validateVigenciasRemocao(valorFaixaProgressiva);
		validateHistoricoWorkflow(valorFaixaProgressiva);
		super.removeById(id);
	}

	private void validateHistoricoWorkflow(ValorFaixaProgressiva valorFaixaProgressiva) {
		tabelaPrecoService.validatePendenciaWorkflow(valorFaixaProgressiva.getFaixaProgressiva().getTabelaPrecoParcela().getTabelaPreco());
	}
	
	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		List<ValorFaixaProgressiva> result = new ArrayList<ValorFaixaProgressiva>();
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			ValorFaixaProgressiva valorFaixaProgressiva = findById((Long)iter.next());
			result.add(valorFaixaProgressiva);
		}
		validateRemove(result);
		
		/*
		 * Verifica se a TabelaPreco relacionada com ValorFaixaProgressiva possui Workflow
		 * pendente de aprovação. Como a TabelaPreco é igual para todos os
		 * ValorFaixaProgressiva, realiza a validação somente para o primeiro ValorFaixaProgressiva.
		 */
		validateHistoricoWorkflow(result.get(0));
		
		super.removeByIds(ids);
	}

	private void validateRemove(List<ValorFaixaProgressiva> list) {
		for (ValorFaixaProgressiva valorFaixaProgressiva : list) {
			validateVigenciasRemocao(valorFaixaProgressiva);
		}
	}

	public void validateVigenciasRemocao(ValorFaixaProgressiva valorFaixaProgressiva) {
		if(valorFaixaProgressiva.getDtVigenciaPromocaoInicial() !=null ) { 
			JTVigenciaUtils.validaVigenciaRemocao(valorFaixaProgressiva);
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ValorFaixaProgressiva valorFaixaProgressiva) {
		validateVigencias(valorFaixaProgressiva);
		return super.store(valorFaixaProgressiva);
	}

	private void validateVigencias(ValorFaixaProgressiva valorFaixaProgressiva) {
		/** Valida periodo entre vigencias de entre registros de mesma natureza(RotaPreco ou TarifaPreco) da Faixa Progressiva */
		if( validateVigenciasTarifaRota(valorFaixaProgressiva) ) {
			throw new BusinessException("LMS-00003");
		}

		/** Valida para que VIGENCIA FINAL nao seja menos que a DATA ATUAL */
		if(valorFaixaProgressiva.getDtVigenciaPromocaoFinal() != null) {
			if (JTDateTimeUtils.comparaData(valorFaixaProgressiva.getDtVigenciaPromocaoFinal(), JTDateTimeUtils.getDataAtual()) < 0) {
				throw new BusinessException("LMS-00007");
			}
		}
	}

	public boolean validateVigenciasTarifaRota(ValorFaixaProgressiva valorFaixaProgressiva) {
		return getValorFaixaProgressivaDAO().validateVigenciasTarifaRota(valorFaixaProgressiva);
	}
	
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return getValorFaixaProgressivaDAO().findPaginatedCustom(criteria);
	}
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getValorFaixaProgressivaDAO().getRowCountCustom(criteria);
	}

	public void setValorFaixaProgressivaDAO(ValorFaixaProgressivaDAO dao) {
		setDao( dao );
	}
	private ValorFaixaProgressivaDAO getValorFaixaProgressivaDAO() {
		return (ValorFaixaProgressivaDAO) getDao();
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	
	public ValorFaixaProgressiva findMenorFaixaProgressivaByTabelaPrecoRotaPreco(Long idTabelaPreco, Long idRotaPreco){
		return getValorFaixaProgressivaDAO().findMenorFaixaProgressivaByTabelaPrecoRotaPreco(idTabelaPreco, idRotaPreco);
	}

	public ValorFaixaProgressiva findByIdTabelaPrecoParaMarkup(Long idTabelaPreco) {
		return getValorFaixaProgressivaDAO().findByIdTabelaPrecoParaMarkup(idTabelaPreco);
	}
}