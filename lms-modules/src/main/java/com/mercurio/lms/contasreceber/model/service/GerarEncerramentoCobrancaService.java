package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.lms.contasreceber.model.CobrancaInadimplencia;






/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.gerarEncerracaoCobrancaService"
 */
public class GerarEncerramentoCobrancaService {

	CobrancaInadimplenciaService cobrancaInadimplenciaService;
	
	/**
	 * Encerra a cobran�a de inadimplencia quando todas as faturas dela est�o 'Liquidadas' ou 'Canceladas'
	 * 
	 * @author Micka�l Jalbert
	 * @since 09/11/2006
	 */
	public void executeEncerrarCobranca(){
		List lstCobranca = cobrancaInadimplenciaService.findCobrancaNaoEncerrado();
		
		for (Iterator iter = lstCobranca.iterator(); iter.hasNext();) {
			CobrancaInadimplencia cobrancaInadimplencia = (CobrancaInadimplencia) iter.next();
			cobrancaInadimplencia.setBlCobrancaEncerrada(Boolean.TRUE);
			cobrancaInadimplenciaService.store(cobrancaInadimplencia);
		}
	}

	public void setCobrancaInadimplenciaService(CobrancaInadimplenciaService cobrancaInadimplenciaService) {
		this.cobrancaInadimplenciaService = cobrancaInadimplenciaService;
	}
}
