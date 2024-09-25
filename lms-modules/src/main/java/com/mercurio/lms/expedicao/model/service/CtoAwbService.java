package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.dao.CtoAwbDAO;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.vendas.model.service.CiaAereaClienteService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.ctoAwbService"
 */
public class CtoAwbService extends CrudService<CtoAwb, Long> {
	
	private ConfiguracoesFacade configuracoesFacade;
	private CiaAereaClienteService ciaAereaClienteService;

	/**
	 * Recupera uma instância de <code>CtoAwb</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public CtoAwb findById(java.lang.Long id) {
		return (CtoAwb)super.findById(id);
	}

	/**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idDoctoServico
     * @param idAwb
     * @return <b>CtoAwb</b>
     */
    public CtoAwb findCtoAwb(Long idDoctoServico, Long idAwb) {
    	return getCtoAwbDAO().findCtoAwb(idDoctoServico, idAwb);
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
	public java.io.Serializable store(CtoAwb bean) {
		return super.store(bean);
	}

	/**
	 * Pesquisa CtoAwbs relacionados ao conhecimento informado.
	 * 
	 * @param idDoctoServico id do conhecimento a ser pesquisado
	 * @return lista de CtoAwb relacionados ao conhecimento
	 */
	public List findByIdDoctoServico(Long idDoctoServico) {
		return getCtoAwbDAO().findByIdDoctoServico(idDoctoServico);
	}

	public List findByIdAwb(Long idAwb) {
		return getCtoAwbDAO().findByIdAwb(idAwb);
	}

	public void removeByIdAwb(Long idAwb) {
		getCtoAwbDAO().removeByIdAwb(idAwb);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setCtoAwbDAO(CtoAwbDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private CtoAwbDAO getCtoAwbDAO() {
		return (CtoAwbDAO) getDao();
	}

	public List<Awb> findAwbByCtoAwb(Long idCtoAwb) {
		return getCtoAwbDAO().findAwbByCtoAwb(idCtoAwb);
	}
	
	public Awb findAwbByIdCto(Long idCto, String tpStatusAwb) {
		return getCtoAwbDAO().findAwbByIdCto(idCto, tpStatusAwb);
	}
	public Awb findPreAwbByIdCto(Long idCto) {
		return getCtoAwbDAO().findAwbByIdCto(idCto, "P");
	}
	
	public ResultSetPage findPaginatedPreAwbConferencia(Long idAwb, FindDefinition findDef) {
		ResultSetPage rsp =  getCtoAwbDAO().findPaginatedPreAwbConferencia(idAwb, findDef);
		List newList = new ArrayList();
		
		for(Iterator i = rsp.getList().iterator(); i.hasNext();) {
			Object[] projections = (Object[])i.next();
			TypedFlatMap result = new TypedFlatMap();

			result.put("nmFilialOrigemPA",projections[0]);
			result.put("nrDoctoServicoPA",projections[1]);
			result.put("pesoRealPA",projections[2]);
			result.put("pesoCubadoPA",projections[3]);
			result.put("qtdVolumesPA",projections[4]);
			result.put("idConhecimento", projections[5]);
			result.put("ctoIdCtoAwb",projections[6]);
			result.put("preCtoIdCtoAwb",projections[7]);

			result.put("nrPreAwbPA", projections[8]);
			result.put("ciaAereaPA",  projections[9]);
			result.put("sgAeroportoOrigemPA",projections[10]);

			result.put("sgAeroportoDestinoPA", projections[11]);
			
			
			newList.add(result);
		}
		rsp.setList(newList);
		return rsp;
		
		
		
	}
	
	public Integer getRowCount(Map criteria) {
		return getCtoAwbDAO().getRowCount(criteria);
	}

	public void updateConferenciaPreAwb(Long idAwb, FindDefinition findDef) {
		ResultSetPage rsp =  findPaginatedPreAwbConferencia(idAwb, findDef);
		List<Map> lista = rsp.getList();
		for(Map mapa : lista){
			if(mapa.get("preCtoIdCtoAwb") != null){
				getCtoAwbDAO().updateConferenciaPreAwb((Long) mapa.get("ctoIdCtoAwb"), (Long) mapa.get("preCtoIdCtoAwb"));
			}
		}		
	}
	
	public TypedFlatMap customizaMensagem(Awb awb, TypedFlatMap result) {
		String coringa1 = "";
		String coringa2 = "";
		String coringa3 = "";
		
		if(("E").equals(awb.getTpStatusAwb().getValue()) || ("P").equals(awb.getTpStatusAwb().getValue())){
			coringa1 = configuracoesFacade.getDomainValue("DM_STATUS_AWB", "E").getDescriptionAsString();
		}
		
		coringa2 = ciaAereaClienteService.findCiaAereaPorAWB(awb.getCiaFilialMercurio().getIdCiaFilialMercurio());
		
		if(("P").equals(awb.getTpStatusAwb().getValue())){
			coringa3 = String.valueOf(awb.getIdAwb()) + ".";				
		}else{
			coringa3 = String.valueOf(AwbUtils.getNrAwbFormated(awb))+ ".";
		}
		
		result.put("coringa1", coringa1);
		result.put("coringa2", coringa2);
		result.put("coringa3", coringa3);
		
		return result;		
	}
	
	public List<Map<String, Object>> findDoctosForReport(Long idAwb, Integer mod, Long rowIni) {
		return getCtoAwbDAO().findDoctosForReport(idAwb, mod, rowIni);
	}
	
	public List<Map<String, Object>> findAwbForDocto(Long idDoctoServico){
		return getCtoAwbDAO().findAwbForDocto(idDoctoServico);
	}
	
	public List<Awb> findCtoAwbBydDoctoServico(Long idDoctoServico){
		return getCtoAwbDAO().findCtoAwbBydDoctoServico(idDoctoServico);
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public CiaAereaClienteService getCiaAereaClienteService() {
		return ciaAereaClienteService;
	}

	public void setCiaAereaClienteService(
			CiaAereaClienteService ciaAereaClienteService) {
		this.ciaAereaClienteService = ciaAereaClienteService;
	}

	public Conhecimento findByIdDoctoServicoAwbEmitido(Long id) {
		return getCtoAwbDAO().findByIdDoctoServicoAwbEmitido(id);
	}

	public List<CtoAwb> findByAwbAndPedidoColeta(Long idAwb, Long idPedidoColeta) {
		return getCtoAwbDAO().findByAwbAndPedidoColeta(idAwb, idPedidoColeta);
	}
	
	public List<Awb> findByPedidoColeta(Long idPedidoColeta) {
		return getCtoAwbDAO().findByPedidoColeta(idPedidoColeta);
	}
	
}