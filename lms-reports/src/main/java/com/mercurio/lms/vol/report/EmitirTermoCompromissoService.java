package com.mercurio.lms.vol.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.municipios.model.service.FilialService;




/**
 * Classe responsável pela geração do Termo de Comunicação e Compromisso
 * Especificação técnica 41.01.07.01
 * @author Luciano Flores
 * 
 * @spring.bean id="lms.vol.emitirTermoCompromissoService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/emitirTermoCompromisso.jasper"
 */
public class EmitirTermoCompromissoService extends ReportServiceSupport{
	private FilialService filialService;
	private FuncionarioService funcionarioService;
	private MotoristaService motoristaService;
	private PessoaService pessoaService;


	public JRReportDataObject execute(Map parameters) throws Exception {
		 	TypedFlatMap criteria = (TypedFlatMap) parameters;
	
		 	/* gera os campos para  o termo e o PDF*/
		 	//nome e sigla da filial
		 	List listNmFilial = filialService.findNmSgFilialByIdFilial(criteria.getLong("filial.idFilial"));
		    Iterator it = listNmFilial.iterator();
		    Map linha = (Map)it.next();
		    String nmFilial = (String)linha.get("pessoa.nmFantasia");
		    
		    String cargo = "";
		    String matricula = "";
		    Map parametersReport = new HashMap();
		    	
		    Pessoa pessoa = getPessoaService().findByNrIdentificacao(criteria.getString("nrIdentificacao"));
		    	
		    //seta o termo de compromisso
		    getMotoristaService().updateBlTermoComp( pessoa.getIdPessoa(), true);
		    
		    	//busca o cargo do funcionario
	    	Funcionario funcionario = funcionarioService.findByNrIdentificacao(criteria.getString("nrIdentificacao"));
			    
	    	if( funcionario != null ){
	    		cargo = funcionario.getDsFuncao();
	    		matricula = funcionario.getNrMatricula();
		    }else{
	    		matricula = criteria.getString("nrIdentificacao");
		    }
		     		 
		    parametersReport.put("nome", criteria.getString("nmPessoa"));
	    	parametersReport.put("matricula", matricula);
	    	parametersReport.put("funcao", cargo);
	    	parametersReport.put("filial", nmFilial);
	    	
	    	parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
	    	List listReport = new ArrayList();
	    	listReport.add(parametersReport);
	    	
	    	JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(listReport);
			
			JRReportDataObject jr = createReportDataObject(jrMap, parameters);
	    		
	        return jr;
	 }
	


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}
	public MotoristaService getMotoristaService() {
		return motoristaService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}



	public PessoaService getPessoaService() {
		return pessoaService;
}



	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

}
