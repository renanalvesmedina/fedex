package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DiferencaCapitalInterior;
import com.mercurio.lms.vendas.model.dao.DiferencaCapitalInteriorDAO;


/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.diferencaCapitalInteriorService"
 */
public class DiferencaCapitalInteriorService extends CrudService<DiferencaCapitalInterior, Long> {

	
	public DiferencaCapitalInterior findById(java.lang.Long id) {
		return (DiferencaCapitalInterior)super.findById(id);
	}
	
	@Override
	public Serializable store(DiferencaCapitalInterior bean) {
		return super.store(bean);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {	
		return	super.findPaginated(criteria);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return super.getRowCount(criteria);
	}	
	
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	/**
	* Obtem o valor do percentual capital interior
	*  
	* @param idUnidadeFederativa
	* @return
	*/
	public DiferencaCapitalInterior findPercCapitalInterior(Long idUnidadeFederativa){		
		Long idUfOrigem = SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();		
		return findDiferencaCapitalInterior(idUfOrigem,idUnidadeFederativa);		
	}
	
	/**
	 * Obtem o valor da diferença através da tabela DIFERENCA_CAPITAL_INTERIOR
	 * @param idUFOrigem
	 * @param idUFDestino
	 * @return Valor da diferça encontrado
	 */
	public DiferencaCapitalInterior findDiferencaCapitalInterior(Long idUFOrigem, Long idUFDestino){

		DiferencaCapitalInterior diferencaCapitalInterior = this.findByUF(idUFOrigem, idUFDestino);
		if(diferencaCapitalInterior == null){
			diferencaCapitalInterior = this.findByUF(null, idUFDestino);
			if(diferencaCapitalInterior == null){
				diferencaCapitalInterior = this.findByUF(null, null);
			}
		}		
		
		return diferencaCapitalInterior;
		}		
	
	/**
	 * Obtem a DiferencaCapitalInterior através da UF de Origem e Destino 
	 * @param idUFOrigem
	 * @param idUFDestino
	 * @return DiferencaCapitalInterior
	 */
	public DiferencaCapitalInterior findByUF(Long idUFOrigem, Long idUFDestino){
		return getDiferencaCapitalInteriorDAO().findByUF(idUFOrigem,idUFDestino);
	}
	
	public void setDiferencaCapitalInteriorDAO(DiferencaCapitalInteriorDAO dao) {
		setDao(dao);
	}

	private DiferencaCapitalInteriorDAO getDiferencaCapitalInteriorDAO() {
		return (DiferencaCapitalInteriorDAO) getDao();
	}	

	public Map<String, Object> findByIdSimulacao(Long idSimulacao, Long idUnidadeFederativa) {
		return getDiferencaCapitalInteriorDAO().findByIdSimulacao(idSimulacao,idUnidadeFederativa);
}
}
