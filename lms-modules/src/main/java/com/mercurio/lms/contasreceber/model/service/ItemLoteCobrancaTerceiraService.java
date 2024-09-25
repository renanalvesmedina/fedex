package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ItemLoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.dao.ItemLoteCobrancaTerceiraDAO;

public class ItemLoteCobrancaTerceiraService  extends CrudService<ItemLoteCobrancaTerceira, Long> {

	private ItemLoteCobrancaTerceiraDAO itemLoteCobrancaTerceiraDAO;
	
	public void setLoteCobrancaTerceiraDAO(ItemLoteCobrancaTerceiraDAO itemLoteCobrancaTerceiraDAO) {
		setDao(itemLoteCobrancaTerceiraDAO);
		this.itemLoteCobrancaTerceiraDAO = itemLoteCobrancaTerceiraDAO;
	}
	
	public List<ItemLoteCobrancaTerceira> findAll(TypedFlatMap filtro) {
		return itemLoteCobrancaTerceiraDAO.findAll(filtro);
	}
	
	public ItemLoteCobrancaTerceira findItemLoteCobrancaById(Long id) {
		return itemLoteCobrancaTerceiraDAO.findLoteCobrancaById(id);
	}
	
	public void removeFaturasByIds(List<Long> ids){
		itemLoteCobrancaTerceiraDAO.removeFaturasByIds(ids);
	}
	public Serializable store(ItemLoteCobrancaTerceira entity) {
		return super.store(entity);
	}
	
	public void removeByIdLoteCobranca(Long idLoteCobraca) {
        itemLoteCobrancaTerceiraDAO.removeByIdLoteCobranca(idLoteCobraca);
    }
	
	public List<ItemLoteCobrancaTerceira> findByFaturaAndLoteCobranda(Long idFatura,Long idLoteCobranca){
		return itemLoteCobrancaTerceiraDAO.findByFaturaAndLoteCobranda(idFatura, idLoteCobranca);
	}
	
}
