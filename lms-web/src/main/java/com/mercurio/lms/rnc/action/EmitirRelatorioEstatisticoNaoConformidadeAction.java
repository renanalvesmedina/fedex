package com.mercurio.lms.rnc.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.EmpresaUsuarioService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaService;
import com.mercurio.lms.rnc.model.service.MotivoAberturaNcService;
import com.mercurio.lms.rnc.report.RelatorioEstatisticoNaoConformidadeService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.emitirRelatorioEstatisticoNaoConformidadeAction"
 */

public class EmitirRelatorioEstatisticoNaoConformidadeAction {
	
	private FilialService filialService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private RotaService rotaService; 
	private MotivoAberturaNcService motivoAberturaNcService;
	private EmpresaUsuarioService empresaUsuarioService;
	private ReportExecutionManager reportExecutionManager;
	private RelatorioEstatisticoNaoConformidadeService relatorioEstatisticoNaoConformidadeService;
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public MotivoAberturaNcService getMotivoAberturaNcService() {
		return motivoAberturaNcService;
	}

	public void setMotivoAberturaNcService(
			MotivoAberturaNcService motivoAberturaNcService) {
		this.motivoAberturaNcService = motivoAberturaNcService;
	}

	public RotaColetaEntregaService getRotaColetaEntregaService() {
		return rotaColetaEntregaService;
	}

	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public RotaService getRotaService() {
		return rotaService;
	}

	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
	}

	public EmpresaUsuarioService getEmpresaUsuarioService() {
		return empresaUsuarioService;
	}

	public void setEmpresaUsuarioService(EmpresaUsuarioService empresaUsuarioService) {
		this.empresaUsuarioService = empresaUsuarioService;
	}

	public void setDefaultService(RelatorioEstatisticoNaoConformidadeService relatorioEstatisticoNaoConformidadeService) {
		this.relatorioEstatisticoNaoConformidadeService = relatorioEstatisticoNaoConformidadeService;
	}
	
	public String execute(TypedFlatMap criteria) throws Exception {
		validateForm(criteria);
		return this.reportExecutionManager.generateReportLocator(this.relatorioEstatisticoNaoConformidadeService, criteria);
	}

	/**
	 * Val�da se o usu�rio logado tem permis�o para gerar o relat�rio.
	 * 
	 * @param parametros vindo da tela
	 */
    private void validateForm(TypedFlatMap parameters) {
    	
    	Empresa empresa = SessionUtils.getEmpresaSessao();
    	Usuario usuario = SessionUtils.getUsuarioLogado();
    	Long idFilialAbertura = parameters.getLong("filialAbertura.idFilial");
    	Long idFilialResponsavel = parameters.getLong("filialResponsavel.idFilial");
    	
    	EmpresaUsuario empresaUsuario = this.getEmpresaUsuarioService().findByEmpresaUsuario(empresa, usuario);
    	
    	if (!empresaUsuario.getBlIrrestritoFilial().booleanValue()) {
			if (idFilialAbertura==null && idFilialResponsavel==null) {
				throw new BusinessException("LMS-12006");
			}
			
			//Busca as filiais passadas por parametro...
			Filial filialAbertura = null;
			Filial filialResponsavel = null;
			
			if (idFilialAbertura!=null) filialAbertura = this.getFilialService().findById(parameters.getLong("filialAbertura.idFilial"));
			if (idFilialResponsavel!=null) filialResponsavel = this.getFilialService().findById(parameters.getLong("filialResponsavel.idFilial"));
			
			if (!SessionUtils.isFilialAllowedByUsuario(filialAbertura) && (!SessionUtils.isFilialAllowedByUsuario(filialResponsavel))) { 
				throw new BusinessException("LMS-12002");
			}
    	}
    }   
    
    public List findLookupRota(TypedFlatMap criteria) {
    	return this.getRotaService().findLookup(criteria);
    }
    
    public List findLookupRotaColetaEntrega(TypedFlatMap criteria) {
       	List list = rotaColetaEntregaService.findLookup(criteria);
       	List retorno = new ArrayList();
       	for (Iterator iter = list.iterator(); iter.hasNext();) {
       		RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega)iter.next();
       		TypedFlatMap typedFlatMap = new TypedFlatMap();
       		typedFlatMap.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
       		typedFlatMap.put("nrRota", rotaColetaEntrega.getNrRota());
       		typedFlatMap.put("dsRota", rotaColetaEntrega.getDsRota());
       		retorno.add(typedFlatMap);
       	}
       	return retorno;
    }
    
    public List findOrderByDsMotivoAbertura(TypedFlatMap criteria) {
    	return this.getMotivoAberturaNcService().findOrderByDsMotivoAbertura(criteria);
    }
    
    public List findLookupFilial(TypedFlatMap criteria) {
    	return this.getFilialService().findLookup(criteria);
    }
}
