package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import br.com.tntbrasil.integracao.domains.expedicao.SolicitarRelatatorioIroadsDMN;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AdsmHibernateUtils;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.expedicao.exception.RelatorioIroadsException;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.dao.ManifestoViagemNacionalDAO;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.dao.LMManifestoDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.manifestoViagemNacionalService"
 */
public class ManifestoViagemNacionalService extends CrudService<ManifestoViagemNacional, Long> {
	private LMManifestoDAO lmManifestoDao;
	private FilialService filialService;

	public void setLmManifestoDao(LMManifestoDAO lmManifestoDao) {
		this.lmManifestoDao = lmManifestoDao;
	}

	/**
	 * Recupera uma inst�ncia de <code>ManifestoViagemNacional</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ManifestoViagemNacional findById(java.lang.Long id) {
		return (ManifestoViagemNacional)super.findById(id);
	}

	public ResultSetPage findPaginatedManifestoViagem(TypedFlatMap criteria) { 
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		return getManifestoViagemNacionalDAO().findPaginatedManifestoViagem(criteria, fd);
	}
	
	public List findPaginatedCadManifestoViagem(TypedFlatMap criteria) { 
		return getManifestoViagemNacionalDAO().findPaginatedCadManifestoViagem(criteria);
	}

	public ResultSetPage findPaginatedConhecimentoManifestoViagem(TypedFlatMap criteria) {
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		return getManifestoViagemNacionalDAO().findPaginatedConhecimentoManifestoViagem(criteria, fd);
	}

	public Integer getRowCountManifestoViagem(TypedFlatMap criteria) {
		return getManifestoViagemNacionalDAO().getRowCountManifestoViagem(criteria);
	}

	public Integer getRowCountConhecimentoManifestoViagem(TypedFlatMap criteria) {
		return getManifestoViagemNacionalDAO().getRowCountConhecimentoManifestoViagem(criteria);
	}

	public ManifestoViagemNacional findByIdManifestoViagem(java.lang.Long id) {
		return getManifestoViagemNacionalDAO().findByIdManifestoViagem(id);
	}

	public List findByNrManifestoOrigemByFilial(Integer nrManifestoOrigem, Long idFilial){
		return getManifestoViagemNacionalDAO().findByNrManifestoOrigemByFilial(nrManifestoOrigem, idFilial);
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
	public java.io.Serializable store(ManifestoViagemNacional bean) {
		return super.store(bean);
	}

	/**
	 * For�a execucao do Flush.
	 * @author Andre Valadas
	 */
	public void executeFlush() {
		getManifestoViagemNacionalDAO().getAdsmHibernateTemplate().flush();
	}

	public void evict(ManifestoViagemNacional manifestoViagemNacional) {
		getManifestoViagemNacionalDAO().getAdsmHibernateTemplate().evict(manifestoViagemNacional);
	}

	public ManifestoViagemNacional get(Long id) {
		ManifestoViagemNacional manifestoViagemNacional = (ManifestoViagemNacional)getManifestoViagemNacionalDAO().getAdsmHibernateTemplate().get(ManifestoViagemNacional.class, (Serializable)id);
		return manifestoViagemNacional;
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param dao Inst�ncia do DAO.
	 */
	public void setManifestoViagemNacionalDAO(ManifestoViagemNacionalDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ManifestoViagemNacionalDAO getManifestoViagemNacionalDAO() {
		return (ManifestoViagemNacionalDAO) getDao();
	}

	/**
	 * M�todo respons�vel por carregar a data de emiss�o e a situa��o do manifesto passado por par�metro.

	 * @param nrManifestoOrigem
	 * @param idFilial
	 * @return List contendo data de emiss�o, situa��o, e o n�mero do manifesto.
	 */
	public List findLookupManifestoVN(String nrManifestoOrigem, Long idFilial){
		return getManifestoViagemNacionalDAO().findLookupManifestoVN(nrManifestoOrigem, idFilial);
	}

	/**
	 * M�todo de busca dos dados de manifesto para a Lookup de Manifesto
	 * 
	 * @author Jos� Rodrigo Moraes
	 * @since 07/11/2006
	 * 
	 * @param idFilial Identificador da filial de origem do manifesto
	 * @param nrManifestoOrigem N�mero do manifesto
	 * @return List contendo o resultado do hql.
	 */
	public List findLookupManifestoVNSpecific(String nrManifestoOrigem, Long idFilial){
		return getManifestoViagemNacionalDAO().findLookupManifestoVNSpecific(nrManifestoOrigem, idFilial);
	}

	/**
	 * M�todo (sobrescrito) respons�vel por carregar dados p�ginados de acordo com os filtros passados
	 * @param criteria
	 * @return List contendo o resultado do hql.
	 */
	public ResultSetPage findPaginatedByManifestoViagemNacional(TypedFlatMap criteria) {
		return getManifestoViagemNacionalDAO().findPaginatedByManifestoViagemNacional(criteria);
	}

	/**
	 * M�todo respons�vel por fazer a contagem dos registros que retornam do hql.
	 * @param criteria
	 * @return Integer contendo o n�mero de registros retornados.
	 */
	public Integer getRowCountByManifestoViagemNacional(TypedFlatMap criteria){
		return getManifestoViagemNacionalDAO().getRowCountByManifestoViagemNacional(criteria);
	}

	public ResultSetPage findPaginatedManifestosViagemByLocalizacaoMercadoria(Long idDoctoServico){
   	ResultSetPage rs = lmManifestoDao.findPaginatedManifestoViagem(idDoctoServico);
		List newList = null;
		if(!rs.getList().isEmpty()){
			newList = new ArrayList();
			for(Iterator iter = rs.getList().iterator();iter.hasNext();){
				Object[] obj = (Object[])iter.next();
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				if(obj[0]!= null)
					typedFlatMap.put("sgFilialOrigem",obj[0]);
				if(obj[1]!= null)
					typedFlatMap.put("nrManifesto",obj[1]);
				if(obj[2]!= null)
					typedFlatMap.put("sgFilialDest",obj[2]);
				if(obj[3]!= null)
					typedFlatMap.put("dhEmissaoMan",obj[3]);
				if(obj[4]!= null)
					typedFlatMap.put("dhSaidaProg",obj[4]);
				if(obj[5]!= null)
					typedFlatMap.put("dhSaidaEfet",obj[5]);
				if(obj[6]!= null)
					typedFlatMap.put("dhPrevisaoCheg",obj[6]);
				if(obj[7]!= null)
					typedFlatMap.put("dhCheg",obj[7]);
				if(obj[8]!= null)
					typedFlatMap.put("dhInicioDesc",obj[8]);
				if(obj[9]!= null)
					typedFlatMap.put("dhFimDesc",obj[9]);
				if(!typedFlatMap.isEmpty())
					newList.add(typedFlatMap);
			}
		}
		rs.setList(newList);
		
		return rs;
	}

	/**
	 * Pesquisa para ser utilizada na lookup que filtra por um docto servico
	 * @param criteria
	 * @return
	 */
	public List findLookupFilterDoctoServico(Map criteria) {
		return getManifestoViagemNacionalDAO().findLookupFilterDoctoServico(criteria);
	}

	/**
	 * Verifica se Existe Conhecimentos para Reembolsar 
	 * que ainda nao possuam Doctos de Reembolso.
	 * @author Andre Valadas.
	 * @param idManifestoViagemNacional
	 * @param blReembolsado
	 * @return 
	 */
	public boolean validateManifestoReembolso(Long idManifestoViagemNacional, Boolean blReembolsado) {
		return this.getManifestoViagemNacionalDAO().validateManifestoReembolso(idManifestoViagemNacional, blReembolsado);
	}

	/**
     * Verifica se Existe Boletos.
     * 
     * @param idManifestoViagemNacional
     * @return
     */
	public boolean validateManifestoBoleto(Long idManifestoViagemNacional) {
		return this.getManifestoViagemNacionalDAO().validateManifestoBoleto(idManifestoViagemNacional);
	}


	public List findByAdiantamentoTrecho(Long idControleCarga, Long idFilialOrigem) {
		return getManifestoViagemNacionalDAO().findByAdiantamentoTrecho(idControleCarga, idFilialOrigem);
	}
	
	public ManifestoViagemNacional findByFilialDestino(Long idControleCarga, Long idFilialDestino) {
		List<ManifestoViagemNacional> lista = getManifestoViagemNacionalDAO().findByFilialDestino(idControleCarga, idFilialDestino);
		if (lista != null && !lista.isEmpty()){
			return lista.get(0);
	}
		
		return new ManifestoViagemNacional();
	}

	public List<Map<String, Object>> findManifestoViagemNacionalSuggest(String sgFilial,Long nrManifestoOrigem, Long idEmpresa) {
		return getManifestoViagemNacionalDAO().findManifestoViagemNacionalSuggest(sgFilial, nrManifestoOrigem, idEmpresa);
	}

	public void flushModeParaCommit(){
		AdsmHibernateUtils.setFlushModeToCommit(this.getDao().getAdsmHibernateTemplate());
	}

	public void flushModeParaManual(){
		AdsmHibernateUtils.setFlushModeToManual(this.getDao().getAdsmHibernateTemplate());
	}

	public void flushModeParaAuto(){
		AdsmHibernateUtils.setFlushModeToAuto(this.getDao().getAdsmHibernateTemplate());
	}

	public ManifestoViagemNacional findByConhecimento(Conhecimento conhecimento, Filial filialFedex){
		return getManifestoViagemNacionalDAO().findByConhecimento(conhecimento, filialFedex);
	}
	
	public Long findCountConhecimentosEmDescargaControleCarga(ControleCarga controleCarga, Filial filial){
		return getManifestoViagemNacionalDAO().findCountConhecimentosEmDescargaControleCarga(controleCarga, filial);
	}

	@Transactional
	public boolean validationFieldsSolicitationReportIroads(SolicitarRelatatorioIroadsDMN solicitarRelatatorioIroadsDMN)
			throws RelatorioIroadsException {

		boolean result = true;
		Filial filial = this.filialService.findByIdBasic(solicitarRelatatorioIroadsDMN.getIdFilial());

		if(filial == null) {
			throw new RelatorioIroadsException("Filial n�o encontrada");
		}

		if(!ExpedicaoUtils.validaListEmail(solicitarRelatatorioIroadsDMN.getListEmail())) {
			throw new RelatorioIroadsException("E-mail(s) inv�lido(s)");
		}

		return result;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}
