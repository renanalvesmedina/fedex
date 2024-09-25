package com.mercurio.lms.franqueados.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.franqueados.model.FixoFranqueado;
import com.mercurio.lms.franqueados.model.dao.FixoFranqueadoDAO;

public class FixoFranqueadoService {

	private FixoFranqueadoDAO fixoFranqueadoDAO;
	
	public List<FixoFranqueado> findParametrosFranquiaOrdenado(Long idFranquia) {
		List<FixoFranqueado> listFixo = fixoFranqueadoDAO.findParametrosFranquia(idFranquia);
		Queue<FixoFranqueado> queueFixo = new LinkedList<FixoFranqueado>(listFixo);

		List<FixoFranqueado> listFixoCliente = new ArrayList<FixoFranqueado>();
		List<FixoFranqueado> listFixoMunicipio = new ArrayList<FixoFranqueado>();
		List<FixoFranqueado> listFixoDefault = new ArrayList<FixoFranqueado>();
		
		while( !queueFixo.isEmpty() ){
			FixoFranqueado fixo = queueFixo.poll();
			if( fixo.getCliente() != null ){
				listFixoCliente.add(fixo);
			}else if( fixo.getMunicipio() != null ){
				listFixoMunicipio.add(fixo);
			}else{
				listFixoDefault.add(fixo);
			}
		}
			listFixo.clear();
			listFixo.addAll(listFixoCliente);
			listFixo.addAll(listFixoMunicipio);
			listFixo.addAll(listFixoDefault);

			listFixoCliente = null;
			listFixoMunicipio = null;
			listFixoDefault = null;
		return listFixo;
	}
	
	public List<FixoFranqueado> findParametrosFranquiaPeriodo(Long idFranquia, YearMonthDay dtInicioCompetencia) {
		return fixoFranqueadoDAO.findParametrosFranquiaPeriodo(idFranquia, dtInicioCompetencia);
	}
	
	@SuppressWarnings("rawtypes")
	public BigDecimal getValorCte(Long idFranquia, YearMonthDay dtInicioCompetencia) {
		
		List consultaCteList = fixoFranqueadoDAO.findParametrosFranquiaPeriodo(idFranquia, dtInicioCompetencia);
				
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! consultaCteList.isEmpty() && consultaCteList.get(0) != null) {
    		valor = ((FixoFranqueado) consultaCteList.get(0)).getVlColeta();
    	}
    	return valor;
	}
	
	public void setFixoFranqueadoDAO(FixoFranqueadoDAO fixoFranqueadoDAO) {
		this.fixoFranqueadoDAO = fixoFranqueadoDAO;

	}
	
}
