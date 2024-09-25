package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.BaixaDevMerc;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ItemBaixaDevMerc;
import com.mercurio.lms.contasreceber.model.dao.BaixaDevMercDAO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoBDMFranqueadoDTO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.baixaDevMercService"
 */
public class BaixaDevMercService extends CrudService<BaixaDevMerc, Long> {

	private ConfiguracoesFacade configuracoesFacade;
	private ItemBaixaDevMercService itemBaixaDevMercService;
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setItemBaixaDevMercService(ItemBaixaDevMercService itemBaixaDevMercService) {
		this.itemBaixaDevMercService = itemBaixaDevMercService;
	}

	private DevedorDocServFatService devedorDocServFatService;
	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public List findPaginatedByIdBaixaDevMerc(Long idBaixaDevMerc) {
		return getBaixaDevMercDAO().findPaginatedByIdBaixaDevMerc(idBaixaDevMerc);
	}
	
	public Integer getRowCountByIdBaixaDevMerc(Long id) {
		return getBaixaDevMercDAO().getRowCountByIdBaixaDevMerc(id);
	}
		
	public BaixaDevMerc findBaixaDevMerc(TypedFlatMap tfm) {
		return getBaixaDevMercDAO().findBaixaDevMerc(tfm);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap tfm) {
		return getBaixaDevMercDAO().findPaginated(tfm);
	}
	
	public Integer getRowCount(TypedFlatMap tfm) {
		return getBaixaDevMercDAO().getRowCount(tfm);
	}
	
	public BaixaDevMerc findById(Long id) {
		return getBaixaDevMercDAO().findById(id);
	}
	    
	public ItemBaixaDevMerc findItemById(Long id) {
		return getBaixaDevMercDAO().findItemById(id);
	}
	    
	public BaixaDevMerc findByItemBaixaDevMerc(Long idPai){
		return getBaixaDevMercDAO().findByItemBaixaDevMerc(idPai);
	}

	public BaixaDevMerc store(BaixaDevMerc bean, ItemList items, ItemListConfig config) {
		
		boolean rollbackMasterId = bean.getIdBaixaDevMerc() == null;
		
		try {
			
			if (!items.hasItems())
				throw new BusinessException("LMS-36038");
			
			if (bean.getIdBaixaDevMerc() == null || bean.getIdBaixaDevMerc().longValue() < 0) {
				Long nrBdm = configuracoesFacade.incrementaParametroSequencial(bean.getFilialEmissora().getIdFilial(), "ULT_BDM", false);
				bean.setNrBdm(nrBdm);
			}
			
			bean.setDhTransmissao(null);
			
			bean = storeAux(bean, items, config);
			
		} catch (RuntimeException e) {
			this.rollbackMasterState(bean, rollbackMasterId, e);
			items.rollbackItemsState();
			throw e;			
		}
		return bean;
	}    

	
	private BaixaDevMerc storeAux(BaixaDevMerc baixaDevMerc, ItemList items, ItemListConfig itemConfig) {
		super.store(baixaDevMerc);
		removeItemBaixaDevMerc(items.getRemovedItems());
		
		for (Iterator iter = items.iterator(baixaDevMerc.getIdBaixaDevMerc(), itemConfig); iter.hasNext();) {
			ItemBaixaDevMerc item = (ItemBaixaDevMerc) iter.next();
			DevedorDocServFat devedorDocServFat = devedorDocServFatService.findByIdInitLazyProperties(item.getDevedorDocServFat().getIdDevedorDocServFat(), false);
			
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
			devedorDocServFat.setDtLiquidacao(baixaDevMerc.getDtEmissao());
			devedorDocServFatService.store(devedorDocServFat);
		}
		
		itemBaixaDevMercService.storeAll((List<ItemBaixaDevMerc>) items.getNewOrModifiedItems());
		super.flush();
		return baixaDevMerc;
	}

	private void removeItemBaixaDevMerc(List<ItemBaixaDevMerc> removeItems) {
		List<Long> ids = new ArrayList<Long>();
		for (ItemBaixaDevMerc itemBaixaDevMerc : removeItems) {
			ids.add(itemBaixaDevMerc.getIdItemBaixaDevMerc());
		}
		itemBaixaDevMercService.removeByIds(ids);
	}

	public void cancelar(Long idBaixaDevMerc) {
    	BaixaDevMerc baixaDevMerc = findById(idBaixaDevMerc);
    	
    	int mesEmissao = baixaDevMerc.getDtEmissao().getMonthOfYear();
    	int mesAtual = JTDateTimeUtils.getDataAtual().getMonthOfYear();
    		
    	int anoEmissao = baixaDevMerc.getDtEmissao().getYear();
    	int anoAtual = JTDateTimeUtils.getDataAtual().getYear();
    	
    	if(!SessionUtils.isIntegrationRunning()){
    	if (anoEmissao != anoAtual || mesEmissao != mesAtual) {
    		throw new BusinessException("LMS-36045");
    	}
    	}
    	if (baixaDevMerc.getTpSituacao().getValue().equals("C")) {
    		throw new BusinessException("LMS-36046");
    	}
    	
    	long filialOrigem = baixaDevMerc.getFilialEmissora().getIdFilial().longValue();
    	long filialUsuario = SessionUtils.getFilialSessao().getIdFilial().longValue();
    	
    	if (filialOrigem != filialUsuario) {
    		throw new BusinessException("LMS-36061");
    	}
    	
    	baixaDevMerc.setDhTransmissao(null);
    	baixaDevMerc.setDhCancelamento(JTDateTimeUtils.getDataHoraAtual());
    	baixaDevMerc.setTpSituacao(new DomainValue("C"));
    	
    	storeObj(baixaDevMerc);
    	
    	List ibdm = findItemBaixaDevMercByBaixaDevMercId(baixaDevMerc.getIdBaixaDevMerc());
    	
    	for (Iterator iter = ibdm.iterator(); iter.hasNext();) {
			ItemBaixaDevMerc itemBaixaDevMerc = (ItemBaixaDevMerc) iter.next();
			
			DevedorDocServFat devedorDocServFat = devedorDocServFatService.findByIdInitLazyProperties(itemBaixaDevMerc.getDevedorDocServFat().getIdDevedorDocServFat(), false);
			
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("C"));
			devedorDocServFat.setDtLiquidacao(null);

			devedorDocServFatService.store(devedorDocServFat);
		}
    	
    }
	
	public List findItemBaixaDevMercByBaixaDevMercId(Long idBaixaDevMerc) {
		return getBaixaDevMercDAO().findItemBaixaDevMercByBaixaDevMercId(idBaixaDevMerc);
	}
	
	@Override
    public void removeById(Long id) {
		super.removeById(id);
	}

    public void removeByIdFlush(Long id) {    	
		getBaixaDevMercDAO().removeByIdFlush(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public void setBaixaDevMercDAO(BaixaDevMercDAO dao) {
		setDao( dao );
	}

	private BaixaDevMercDAO getBaixaDevMercDAO() {
		return (BaixaDevMercDAO) getDao();
	}

	// TODO mudar isso aqui está TERRIVEL!
	// a logica está implementada na classe ManterBDMAction
	public void storeObj(Object obj) {
		getDao().store(obj);
	}
	
	/**
	 * Carrega uma BDM de acordo com a filial de emissão e o número da BDM
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/03/2007
	 *
	 * @param nrBdm
	 * @param idFilialEmissora
	 * @return
	 *
	 */
	public BaixaDevMerc findBaixaDevMerc(Long nrBdm, Long idFilialEmissora) {
    	return getBaixaDevMercDAO().findBaixaDevMerc(nrBdm, idFilialEmissora);
    }
	
	public List<Map<String, Object>> findBMDByFranquiaAndCompetencia(Long idFranquia, YearMonthDay dataInicioCompetencia, YearMonthDay dataFimCompetencia){
		return getBaixaDevMercDAO().findBMDByFranquiaAndCompetencia(idFranquia, dataInicioCompetencia, dataFimCompetencia);
	}
	
	/*
	 * Novo calculo de franqueados
	 */

	public List<CalculoBDMFranqueadoDTO> findBMDFranqueado(Long idFranquia, YearMonthDay dataInicioCompetencia, YearMonthDay dataFimCompetencia){
		List<Map<String,Object>> list = getBaixaDevMercDAO().findBMDByFranquiaAndCompetencia(idFranquia, dataInicioCompetencia, dataFimCompetencia);
		List<CalculoBDMFranqueadoDTO> _return = null;
		if (CollectionUtils.isNotEmpty(list)) {
			_return = new LinkedList<CalculoBDMFranqueadoDTO>();
			for (Map<String,Object> map : list) {
				CalculoBDMFranqueadoDTO calculo = new CalculoBDMFranqueadoDTO();
				calculo.setIdDoctoServico( MapUtilsPlus.getLong(map,"idDoctoServico"));
				if( map.get("idDoctoServico") != null){
					calculo.setIdDoctoServico(MapUtilsPlus.getLong(map, "idDoctoServico"));
				}
				if( map.get("nrBDM") != null){
					calculo.setNrBDM(MapUtilsPlus.getLong(map, "nrBDM").toString());
				}
				if( map.get("nrDoctoServico") != null){
					calculo.setNrDoctoServico(MapUtilsPlus.getLong(map, "nrDoctoServico").toString());
				}
				calculo.setSgFilial(MapUtilsPlus.getString(map, "sgFilial"));
				calculo.setSgFilialOrigem(MapUtilsPlus.getString(map, "filialOrigem"));
				calculo.setTpDocumentoServico(MapUtilsPlus.getDomainValue(map, "tpDocumentoServico"));
				calculo.setVlParticipacao(MapUtilsPlus.getBigDecimal(map, "vlParticipacao"));
				_return.add(calculo);
			}
		}
		return _return;
	}
	
}