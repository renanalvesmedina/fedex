package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.dao.ImpostoServicoDAO;
import com.mercurio.lms.sim.model.dao.LMFreteDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.impostoServicoService"
 */
public class ImpostoServicoService extends CrudService<ImpostoServico, Long> {
	private LMFreteDAO lmFreteDao;
	public void setLmFreteDao(LMFreteDAO lmFreteDao) {
		this.lmFreteDao = lmFreteDao;
	}

	/**
	 * Recupera uma inst�ncia de <code>ImpostoServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ImpostoServico findById(java.lang.Long id) {
		return (ImpostoServico)super.findById(id);
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
	public java.io.Serializable store(ImpostoServico bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setImpostoServicoDAO(ImpostoServicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ImpostoServicoDAO getImpostoServicoDAO() {
		return (ImpostoServicoDAO) getDao();
	}

	public ResultSetPage<Map<String, Object>> findPaginated(PaginatedQuery paginatedQuery) {
		ResultSetPage result = getImpostoServicoDAO().findPaginated(paginatedQuery);

		List<ImpostoServico> impostos = result.getList();
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>(impostos.size());
		for (ImpostoServico imposto : impostos) {
			Map<String, Object> impostoMapped = new HashMap<String, Object>();
			impostoMapped.put("idImpostoServico", imposto.getIdImpostoServico());
			impostoMapped.put("tpImposto", imposto.getTpImposto());
			impostoMapped.put("vlBaseCalculo", imposto.getVlBaseCalculo());
			impostoMapped.put("pcAliquota", imposto.getPcAliquota());
			impostoMapped.put("vlImposto", imposto.getVlImposto());
			impostoMapped.put("blRetencaoTomadorServico", imposto.getBlRetencaoTomadorServico());
			if( imposto.getMunicipioByIdMunicipioIncidencia() != null ){
				impostoMapped.put("nmMunicipioIncidencia", imposto.getMunicipioByIdMunicipioIncidencia().getNmMunicipio());
			}
			toReturn.add(impostoMapped);
		}
		result.setList(toReturn);
		return result;
	}

	/**
	 * store os impostos servico recebendo como parametro o Conhecimento
	 * @param frete
	 */
	public void storeListImpostoServico( Conhecimento conhecimento ){	
    	List<ImpostoServico> listImpostoServico = conhecimento.getImpostoServicos();
    	for( ImpostoServico impostoServico : listImpostoServico ){
    		this.store(impostoServico);
    	}
	}

	
	public List findNFServicoAdicionalValores(Long idNotaFiscalServico) {
		return getImpostoServicoDAO().findNFServicoAdicionalValores(idNotaFiscalServico);
	}

	public List<ImpostoServico> findoByIdNotaFiscalServico(Long idNotaFiscalServico) {
		return getImpostoServicoDAO().findoByIdNotaFiscalServico(idNotaFiscalServico);
	}

	public List findPaginatedImpostos(Long idDoctoServico){
		return lmFreteDao.findPaginatedImpostos(idDoctoServico);
	}

	public List findTipoTributacaoIcms(Long idDoctoServico){
		return lmFreteDao.findTipoTributacaoIcms(idDoctoServico);
	}

	public List findDadosCalculoFrete(Long idDoctoServico){
		return lmFreteDao.findDadosCalculoFrete(idDoctoServico);
	}

	public Map findIcmsDoctoServico(Long idDoctoServico){
		return lmFreteDao.findIcmsDoctoServico(idDoctoServico);
	}

	public BigDecimal somaISSDoctoServico(Long idDoctoServico) {
		return lmFreteDao.somaISSDoctoServico(idDoctoServico);
}

	public Boolean findIssRetidoByIdConhecimento(Long idConhecimento) {
		return getImpostoServicoDAO().findIssRetidoByIdConhecimento(idConhecimento);
	}

	public String retornarIdDoctoServicosParaUtilizarNaRemocaoDoImpostoServico(Long idMonitoramentoDocEletronic){
		return getImpostoServicoDAO().retornarIdDoctoServicosParaUtilizarNaRemocaoDoImpostoServico(idMonitoramentoDocEletronic);
	}
	
	public void removeImpostoServicoByMonitoramentoDocEletronico(String idDoctoServicos) {
		getImpostoServicoDAO().removeImpostoServicoByMonitoramentoDocEletronico(idDoctoServicos);
}

}
