package com.mercurio.lms.vendas.util;

/**
 * Classe que implementa as regras de quebra de paginas dos relatorios.
 * @author Diego Pacheco - GT5 - LMS, Nilseu - Arquitetura - LMS
 * @version 3.0
 * 
 */
public class QIPageBreak {

	/* informadas */
	private int totRegistrosPorPagina;
	private int totDeducoesPrimeiraPagina;
	private int totalRegistros;
	private int totDeducoesPorPagina;
	
	/* calculadas */
	private int totPaginas;
	private int totDeducoes;	
	private int totSobraLasPage;
	private int totRegsLastPage;
	private int totEspacoQueSobrou;
	
	public QIPageBreak(int totRegistrosPorPagina, int totalRegistros) {	
		this.totRegistrosPorPagina = totRegistrosPorPagina;
		this.totalRegistros = totalRegistros;
		this.totDeducoesPorPagina = 0;
		this.totDeducoesPrimeiraPagina = 0;
		aplicaCalculos();
	}

	public QIPageBreak(int totRegistrosPorPagina, int totDeducoesPrimeiraPagina, int totalRegistros, int totDeducoesPorPagina) {	
		this.totRegistrosPorPagina = totRegistrosPorPagina;
		this.totDeducoesPrimeiraPagina = totDeducoesPrimeiraPagina;
		this.totalRegistros = totalRegistros;
		this.totDeducoesPorPagina = totDeducoesPorPagina;
		aplicaCalculos();
	}
	
	private void aplicaCalculos()
	{
		calculaTotalPaginas();
		calculaTotalDeducoes();
		calculaTotSobraLastPage();
		calculaNumRegsLastPage();
		calculaEspacoQueSobrou();
	}
	
	
	private void calculaTotalPaginas()
	{
		totPaginas = totalRegistros / totRegistrosPorPagina;
	}
	
	private void calculaTotalDeducoes()
	{
		totDeducoes = totDeducoesPrimeiraPagina + (totDeducoesPorPagina * totPaginas);
	}
	
	private void calculaTotSobraLastPage()
	{
		if(totRegistrosPorPagina < totalRegistros ){
			totSobraLasPage = totalRegistros - (totRegistrosPorPagina* totPaginas);
		}else{
			totSobraLasPage = totRegistrosPorPagina - totalRegistros; 
		}
	}
	
	private void calculaNumRegsLastPage()
	{
		totRegsLastPage = (totDeducoes + totSobraLasPage)%totRegistrosPorPagina;
	}	
	
	private void calculaEspacoQueSobrou()
	{
		if(totalRegistros < totRegistrosPorPagina){
			totEspacoQueSobrou =  totRegistrosPorPagina - totalRegistros;
		}else{
			totEspacoQueSobrou = totRegistrosPorPagina - totRegsLastPage;
		}
	}
	
	
	public int getTotRegsLastPage() {
		return totRegsLastPage;
	}
	public int getTotEspacoQueSobrou()
	{
		return  totEspacoQueSobrou; 
	}	
	public int getTotalRegistros() {
		return totalRegistros;
	}	
	public int getTotDeducoesPorPagina() {
		return totDeducoesPorPagina;
	}	
	public int getTotDeducoesPrimeiraPagina() {
		return totDeducoesPrimeiraPagina;
	}	
	public int getTotRegistrosPorPagina() {
		return totRegistrosPorPagina;
	}	
	
}
