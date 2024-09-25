package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.core.jmx.MBeanFactory;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.jmx.ImagemCache;
import com.mercurio.lms.configuracoes.jmx.ImagemCacheMBean;
import com.mercurio.lms.configuracoes.model.*;
import org.apache.commons.lang.StringUtils;
import java.util.List;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ImagemDAO extends BaseCrudDao<Imagem, Long> {

	private static ImagemCacheMBean cacheMBean;
	private static List<ImagemCacheMBean> cacheMBeans;
	
	@Override
	protected Class getPersistentClass() {
		return Imagem.class;
	}

	public void refreshCache() {
		initializeMBean();
		for (ImagemCacheMBean bean : cacheMBeans) {
			bean.refresh();
		}
	}

	private void initializeMBean() {
		if (cacheMBeans == null || cacheMBeans.isEmpty()) {
			cacheMBeans = MBeanFactory.getJMXProxy("*:name=LMS-ImagemCache", ImagemCacheMBean.class, ImagemCache.class);
			if (cacheMBeans != null && !cacheMBeans.isEmpty()) {
				cacheMBean = cacheMBeans.get(0);
			}
		}
	}

	/**
	 * Retorna uma Imagem de acordo com o nome da chave informada.
	 *
	 * @param chave nome do parametro
	 * @param realizaLock realiza lock pessimista no parametro informado no banco de dados
	 *
	 * @return <code>null</code> caso nenhuma imagem seja encontrada, a imagem caso contrário.
	 */
	public Imagem findImagemByChave(String chave, boolean realizaLock) {
		if (StringUtils.isBlank(chave)) {
			throw new IllegalArgumentException("A chave não pode ser nula ou vazia");
		}
		Imagem imagem = null;

		if (realizaLock) {
			StringBuilder hql = new StringBuilder();
			hql.append(" SELECT img FROM ");
			hql.append(  Imagem.class.getName()).append(" img ");
			hql.append(" WHERE img.chave = ? ");
			imagem = (Imagem) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{chave});
		} else {
			initializeCache();
			List<Imagem> imagensCache = cacheMBean.getImagens();
			for (Imagem img : imagensCache) {
				if (img.getChave().equals(chave)) {
					imagem = img;
					break;
				}
			}
		}
		return imagem;
	}

	private void initializeCache() {
		initializeMBean();
		boolean needRefresh = false;
		for (ImagemCacheMBean bean : cacheMBeans) {
			if (bean.needRefresh()) {
				needRefresh = true;
				break;
			}
		}
		if (needRefresh) {
			StringBuilder hql = new StringBuilder();
			hql.append(" SELECT img FROM ");
			hql.append(  Imagem.class.getName());
			hql.append(" img ");
			List<Imagem> cacheData = getAdsmHibernateTemplate().find(hql.toString());
			for (ImagemCacheMBean bean : cacheMBeans) {
				bean.setImagens(cacheData);
			}
		}
	}
}
