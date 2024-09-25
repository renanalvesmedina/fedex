package com.mercurio.lms.tributos.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.dto.DadosInscricaoEstadualColetivaDto;
import com.mercurio.lms.tributos.dto.InscricaoEstadualColetivaFilterDto;
import com.mercurio.lms.tributos.model.InscricaoEstadualColetiva;
import com.mercurio.lms.tributos.model.dao.InscricaoEstadualColetivaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.ValidateUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class InscricaoEstadualColetivaService extends CrudService<InscricaoEstadualColetiva, Long> {

	private UnidadeFederativaService unidadeFederativaService;
	private ClienteService clienteService;
	
	@SuppressWarnings("rawtypes")
	@Override
	protected BaseCrudDao getDao() {
		return super.getDao();
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(PaginatedQuery paginatedQuery) {
		return getInscricaoEstadualColetivaDao().findPaginated(paginatedQuery);
	}
	
	@Override
	public InscricaoEstadualColetiva findById(Long id) {
		InscricaoEstadualColetiva bean = (InscricaoEstadualColetiva) super.findById(id);
		if(bean.getCliente() != null && bean.getCliente().getPessoa() != null){
			Hibernate.initialize(bean.getCliente());
			Hibernate.initialize(bean.getCliente().getPessoa());
		}
		return bean;
	}
	
	@Override
	public Serializable store(InscricaoEstadualColetiva inscricaoEstadualColetiva) {
		verificaDadosInscricaoEstadualColetiva(inscricaoEstadualColetiva);
		validaInscricaoEstadualColetiva(inscricaoEstadualColetiva);
		return super.store(inscricaoEstadualColetiva);
	}
	
	private void verificaDadosInscricaoEstadualColetiva(InscricaoEstadualColetiva inscricaoEstadualColetiva) {
		if(inscricaoEstadualColetiva.getDtVigenciaFinal() == null) {
			inscricaoEstadualColetiva.setDtVigenciaFinal(new YearMonthDay().withDayOfMonth(1).withMonthOfYear(1).withYear(4000));
		}
		if (inscricaoEstadualColetiva.getUnidadeFederativa() != null) {
			UnidadeFederativa unidadeFederativa = unidadeFederativaService.findById(inscricaoEstadualColetiva.getUnidadeFederativa().getIdUnidadeFederativa());
			inscricaoEstadualColetiva.setUnidadeFederativa(unidadeFederativa);
		}
		
		if (inscricaoEstadualColetiva.getCliente() != null) {
			Cliente cliente = clienteService.findByIdComPessoa(inscricaoEstadualColetiva.getCliente().getIdCliente());
			inscricaoEstadualColetiva.setCliente(cliente);
		}
		
	}

	private void validaInscricaoEstadualColetiva(InscricaoEstadualColetiva bean) {
		validaDatasInscricaoEstadualColetiva(bean);
		validaNrInscricaoEstadual(bean);
		validaExistenciaInscricaoEstadualColetivaVigente(bean);
	}

	private void validaExistenciaInscricaoEstadualColetivaVigente(InscricaoEstadualColetiva bean) {
			Boolean existenciaInscricaoEstadualColetivaVigente = getInscricaoEstadualColetivaDao().findExistenciaInscricaoEstadualColetivaVigente(bean);
			if (existenciaInscricaoEstadualColetivaVigente) {
				throw new BusinessException("LMS-00047");
			}
	}

	private void validaDatasInscricaoEstadualColetiva(
			InscricaoEstadualColetiva bean) {
		YearMonthDay today = JTDateTimeUtils.getDataAtual();
		YearMonthDay dtVigenciaInicial = bean.getDtVigenciaInicial();
		YearMonthDay dtVigenciaFinal = bean.getDtVigenciaFinal();
		
		if (dtVigenciaFinal.isBefore(dtVigenciaInicial)) {
			throw new BusinessException("LMS-00008");
		}
		if (dtVigenciaFinal.isBefore(today)) {
			throw new BusinessException("LMS-00007");
		}
		if (bean.getIdInscricaoEstadualColetiva() == null && today.isAfter(dtVigenciaInicial)) {
			throw new BusinessException("LMS-00006");
		}
	}

	private void validaNrInscricaoEstadual(InscricaoEstadualColetiva bean) {
		boolean isNrIEValid = ValidateUtils.validateInscricaoEstadual(bean.getUnidadeFederativa().getSgUnidadeFederativa(), bean.getNrInscricaoEstadualColetiva());
		if (!isNrIEValid) {
			List<String> throwArgs = new ArrayList<String>();
			if (bean.getCliente() != null && bean.getCliente().getPessoa() != null) {
				throwArgs.add(bean.getCliente().getPessoa().getNmFantasia());
			}
			throw new BusinessException("LMS-28007", throwArgs.toArray());
		}
	}

	public void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			removeById(id);
		}
	}
	
	public void removeById(Long id) {
		InscricaoEstadualColetiva bean = super.get(id);
		remove(bean);
	}
	
	private void remove(InscricaoEstadualColetiva bean) {
		validaVigenciaInscricaoEstadualColetivaToDelete(bean);
		super.removeById(bean.getIdInscricaoEstadualColetiva());
	}

	private void validaVigenciaInscricaoEstadualColetivaToDelete(InscricaoEstadualColetiva bean) {
		YearMonthDay today = JTDateTimeUtils.getDataAtual();
		if (today.equals(bean.getDtVigenciaInicial()) || today.isAfter(bean.getDtVigenciaInicial())) {
			throw new BusinessException("LMS-00005");
		}
	}
	
	public DadosInscricaoEstadualColetivaDto findDadosIEColetativaByFilter(InscricaoEstadualColetivaFilterDto filterDto) {
		@SuppressWarnings("rawtypes")
		List result = getInscricaoEstadualColetivaDao().findDadosIEColetativaByFilter(filterDto);
		if (CollectionUtils.isNotEmpty(result)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> mapResult = (Map<String, Object>) result.get(0);
			DadosInscricaoEstadualColetivaDto resultDto = new DadosInscricaoEstadualColetivaDto().buildByMap(mapResult);
			return resultDto;
		}
		return null; 
	}

	public void setInscricaoEstadualColetivaDao(InscricaoEstadualColetivaDAO inscricaoEstadualColetivaDAO) {
		setDao(inscricaoEstadualColetivaDAO);
	}
	
	private InscricaoEstadualColetivaDAO getInscricaoEstadualColetivaDao() {
		return (InscricaoEstadualColetivaDAO) getDao();
	}
	
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

}
