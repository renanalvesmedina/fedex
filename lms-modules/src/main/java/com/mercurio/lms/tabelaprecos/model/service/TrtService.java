package com.mercurio.lms.tabelaprecos.model.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.vendas.model.MunicipioTrtCliente;
import com.mercurio.lms.vendas.model.TrtCliente;
import com.mercurio.lms.vendas.model.dao.MunicipioTrtClienteDAO;
import com.mercurio.lms.vendas.model.dao.TrtClienteDAO;


public class TrtService {

	private static final String NAO = "N";
	private static final String SIM = "S";
	private static final int FISRT = 0;
	private TrtClienteDAO trtClienteDAO;
	private MunicipioTrtClienteDAO municipioTrtClienteDAO;
	private static Log log = LogFactory.getLog(CloneTabelaPrecoService.class);
	
	
	
	public void executeCloneTRTbyTabela(Long idTabelaBase, Long idTabelaNova){
		existeTabelaNovaVigenteCadastrada(idTabelaNova);
		trtClienteDAO.insertTrtClienteByTabela(idTabelaNova, idTabelaBase);
		municipioTrtClienteDAO.insertMunicipio(idTabelaNova, idTabelaBase);
	}
	
	public boolean cloneTRTbyTabelaForReajuste(Long idTabelaBase, Long idTabelaNova){
		try{
			executeCloneTRTbyTabela(idTabelaBase, idTabelaNova);
		} catch(BusinessException be){
			log.warn("TRT não pode travar o processo de clone - TABELA_BASE["+idTabelaBase+"] TABELA_NOVA["+idTabelaNova+"]",be);
		}
		return true;
	}
	
	
	private void existeTabelaNovaVigenteCadastrada(Long idTabelaNova){
		List<TrtCliente> listTabelaNova = trtClienteDAO.findCurrentTrtClienteByIdTabela(idTabelaNova);
		if(listTabelaNova != null && !listTabelaNova.isEmpty()){
			throw new BusinessException("LMS-30078");
		}
	}
	
	private TrtCliente getTrtCliente(Long idTabelaBase){
		List<TrtCliente> listTabelaBase = trtClienteDAO.findCurrentTrtClienteByIdTabela(idTabelaBase);
		if(listTabelaBase == null || listTabelaBase.isEmpty()){
			throw new BusinessException("LMS-30076");
		}
		
		return listTabelaBase.get(FISRT);
	}
	
	private List<MunicipioTrtCliente> getListMunicipioTrtCliente(Long idTrtCliente){
		List<MunicipioTrtCliente> listTrtMunicipio = municipioTrtClienteDAO.findByIdTrtCliente(idTrtCliente);
		if(listTrtMunicipio == null || listTrtMunicipio.isEmpty()){
			throw new BusinessException("LMS-30077");
		}
		
		return listTrtMunicipio;
	}
	
	public void setTrtClienteDAO(TrtClienteDAO trtClienteDAO) {
		this.trtClienteDAO = trtClienteDAO;
	}
	
	public void setMunicipioTrtClienteDAO(MunicipioTrtClienteDAO municipioTrtClienteDAO) {
		this.municipioTrtClienteDAO = municipioTrtClienteDAO;
	}
	
}
