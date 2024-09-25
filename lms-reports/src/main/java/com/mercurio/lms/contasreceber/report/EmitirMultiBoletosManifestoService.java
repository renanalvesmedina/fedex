package com.mercurio.lms.contasreceber.report;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.MultiReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.service.CedenteService;

/**
 * Classe responsavel por executar o relat�rio de boletos no estilo MultiReport.
 * 
 * @author felipec
 * @spring.bean id="lms.contasreceber.emitirMultiBoletosManifestoService"
 *
 */
public class EmitirMultiBoletosManifestoService extends MultiReportServiceSupport {

	
	private CedenteService cedenteService;

	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

	@Override
	protected MultiReportCommand prepareMultiReport(TypedFlatMap tfm) throws Exception {
		/** Caso algum dos teste n�o for verdadeiro, lan�a uma exce��o */
		if(!(tfm.getLong("idManifesto") != null 
				&& tfm.getString("tpManifesto") != null
				&& tfm.getString("blReemissaoManifesto") != null
				&& (tfm.getString("tpManifesto").equals("V") || tfm.getString("tpManifesto").equals("E"))
				&& (tfm.getString("blReemissaoManifesto").equals("S") || tfm.getString("blReemissaoManifesto").equals("N")))){
			
			throw new BusinessException("LMS-36225");
		}
		
		/** Objeto que agrupa n relat�rios em um �nico */
		// FIXME corrigir nome do relat�rio para chave i18n
		MultiReportCommand mrc = new MultiReportCommand("emitirBoletos"); 
		
		/** Busca os ids dos cedentes */
		List<Map> idsCedentesMap = cedenteService.findCedentesAtivos();
		
		/** Itera os ids dos cedentes e adiciona comandos(relat�rios) */
		for(Map element : idsCedentesMap){
			
			tfm = new TypedFlatMap(tfm);
			tfm.put("cedente.idCedente", element.get("idCedente"));
			
			mrc.addCommand("lms.contasreceber.emitirBoletoService", tfm); 
		}
		
		return mrc;
	}

}
