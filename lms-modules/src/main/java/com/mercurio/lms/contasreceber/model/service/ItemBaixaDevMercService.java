package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.ItemBaixaDevMerc;
import com.mercurio.lms.contasreceber.model.dao.ItemBaixaDevMercDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.itemBaixaDevMercService"
 */
public class ItemBaixaDevMercService extends CrudService<ItemBaixaDevMerc, Long> {

	public void setItemBaixaDevMercDAO(ItemBaixaDevMercDAO dao) {
		setDao ( dao );
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	@Override
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public java.io.Serializable store(ItemBaixaDevMerc bean) {
		return super.store(bean);
	}

}
