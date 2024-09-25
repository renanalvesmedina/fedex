package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import br.com.tntbrasil.integracao.domains.comissionamento.MetaDTO;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Meta;
import com.mercurio.lms.vendas.model.dao.MetaDAO;

public class MetaService extends CrudService<Meta, Long> {
	
	public static final String EXCEPTION_STORE_TERRITORIO_INVALIDO = "LMS-01277";
	public static final String EXCEPTION_STORE_META_INVALIDA = "LMS-01278";
	public static final String EXCEPTION_STORE_MES_INVALIDO = "LMS-01279";
	public static final String EXCEPTION_STORE_ANO_INVALIDO = "LMS-01280";
	public static final String EXCEPTION_STORE_MODAL_INVALIDO = "LMS-01281";
	
	@Override
	public java.io.Serializable store(Meta meta) {
		Meta metaStore = createMetaStore(meta);
		validateMeta(metaStore);
		return callInheritedStore(metaStore);
	}
	
	@Override
	public void storeAll(List<Meta> metas) {
		if (metas == null || metas.isEmpty()) {
			throw new RuntimeException("Nenhuma meta informada");//TODO: ver qual a exceção adequada
		}
		for (Meta meta : metas) {
			store(meta);
		}
	}
	
	public Meta findByNaturalKey(Long idTerritorio, DomainValue tpModal, Integer nrAno, String nmMes) {
		return getMetaDao().findByNaturalKey(idTerritorio, tpModal, nrAno, nmMes);
	}
	
	/**
	 * Wrapping the inherited method calling for mocking purposes.
	 */
	private java.io.Serializable callInheritedStore(Meta meta) {
		return super.store(meta);
	}
	
	
	
	private Meta createMetaStore(Meta meta) {
		
		DateTime dhStore = JTDateTimeUtils.getDataHoraAtual();
		Meta metaStore = new Meta();

		if (meta.getIdMeta() != null) {
			metaStore = findById(meta.getIdMeta());
		} else {
			metaStore.setUsuarioInclusao(meta.getUsuarioAlteracao());
			metaStore.setDhInclusao(dhStore);
		}

		metaStore.setTerritorio(meta.getTerritorio());
		metaStore.setTpModal(meta.getTpModal());
		metaStore.setNrAno(meta.getNrAno());
		metaStore.setNmMes(meta.getNmMes());
		metaStore.setVlMeta(meta.getVlMeta());

		metaStore.setUsuarioAlteracao(meta.getUsuarioAlteracao());
		metaStore.setDhAlteracao(dhStore);
		
		return metaStore;
	}
	
	
	private void validateMeta(Meta meta) {
		if (meta.getTerritorio() == null) {
			throw new BusinessException(EXCEPTION_STORE_TERRITORIO_INVALIDO);
		}
		
		Integer ano = meta.getNrAno();
		if (ano == null || ano < 0) {
			throw new BusinessException(EXCEPTION_STORE_ANO_INVALIDO);
			
		}
		
		if (meta.getTpModal() == null) {
			throw new BusinessException(EXCEPTION_STORE_MODAL_INVALIDO);
		}
		
		if (!isMesValido(meta.getNmMes())) {
			throw new BusinessException(EXCEPTION_STORE_MES_INVALIDO);
		}
		
		BigDecimal vlMeta = meta.getVlMeta();
		if (vlMeta == null ||  vlMeta.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(EXCEPTION_STORE_META_INVALIDA);
		}
	}
	
	private boolean isMesValido(String nmMes) {
		if (StringUtils.isNotBlank(nmMes)) {
			String[] mesesValidos = {
					"JANEIRO", 
					"FEVEREIRO", 
					"MARÇO",  
					"ABRIL",  
					"MAIO", 
					"JUNHO",  
					"JULHO",  
					"AGOSTO", 
					"SETEMBRO", 
					"OUTUBRO", 
					"NOVEMBRO", 
					"DEZEMBRO"
			};
			for (String mesValido : mesesValidos) {
				if (mesValido.equals(nmMes)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public void setMetaDao(MetaDAO dao) {
		setDao(dao);
	}

	public MetaDAO getMetaDao() {
		return (MetaDAO) getDao();
	}

	public List<Meta> find(Long idTerritorio, DomainValue tpModal, Integer nrAno, String nmMes) {
		return getMetaDao().find(idTerritorio, tpModal, nrAno, nmMes);
	}

	@Override
	public Meta findById(Long id) {
		return getMetaDao().findById(id);
	}

	@Override
	public void removeById(Long id) {
		getMetaDao().removeById(id);
	}

	public Integer findCount(Long idTerritorio, DomainValue tpModal, Integer nrAno, String nmMes) {
		return getMetaDao().findCount(idTerritorio, tpModal, nrAno, nmMes);
	}

}
