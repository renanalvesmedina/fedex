package com.mercurio.lms.rest;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.dto.FiltroPaginacaoDto;

public class PaginacaoUtil {

	private static final String PAGE_SIZE = "_pageSize";
	private static final String CURRENT_PAGE = "_currentPage";
	private static final String PAGE_DEFAULT = "1";

	private PaginacaoUtil(){
		
	}
	
	public static TypedFlatMap getPaginacao(FiltroPaginacaoDto filtro, Integer limiteRegistros) {	
		TypedFlatMap tfm = new TypedFlatMap();
		
		tfm.put(CURRENT_PAGE, getPage(filtro));
		tfm.put(PAGE_SIZE, getPageSize(filtro, limiteRegistros));
		
		return tfm;
	}	

	private static String getPageSize(FiltroPaginacaoDto filtro, Integer limiteRegistros) {
		return filtro.getQtRegistrosPagina() == null ? String.valueOf(limiteRegistros) : String.valueOf(filtro.getQtRegistrosPagina());
	}

	private static String getPage(FiltroPaginacaoDto filtro) {
		return filtro.getPagina() == null ? PAGE_DEFAULT : String.valueOf(filtro.getPagina());
	}
}