package com.mercurio.lms.gm.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.dao.UsuarioADSMDAO;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.PessoaDAO;
import com.mercurio.lms.gm.model.dao.CabecalhoCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.DetalheCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.TotalCarregamentoDAO;
import com.mercurio.lms.gm.model.service.EmbarqueService;
import com.mercurio.lms.gm.model.service.VolumeService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * 
 * @spring.bean id="lms.gm.relatorioDiscrepanciaService"
 * @spring.property name="reportName" value="com/mercurio/lms/gm/report/relatorioDeDiscrepancia.jasper"
 */
public class RelatorioDiscrepanciaService extends ReportServiceSupport {
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	private EmbarqueService embarqueService;
	private VolumeService volumeService;
	private DetalheCarregamentoDAO detalheCarregamentoDao;
	private CabecalhoCarregamentoDAO cabecalhoCarregamentoDao;
	private TotalCarregamentoDAO totalCarregamentoDao;
	private PessoaDAO pessoaDAO;
	private UsuarioADSMDAO usuarioADSMDAO;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		JRReportDataObject jr = null;
		List<String> listIds = (List) parameters.get("ids");//emailTitulo
		
		String tituloRelatorio = (String) parameters.get("emailTitulo");
		String tipoEmitente = (String) parameters.get("tipoEmitente");
		
		if(tipoEmitente != null && tipoEmitente.equals("GM")){
			jr = executeGM(parameters, listIds);
		} else if(tipoEmitente != null && tipoEmitente.equals("TNT - Veículo")){
			jr = executeTNTVeiculo(parameters, listIds);
		} else if(tipoEmitente != null && tipoEmitente.equals("TNT - Descarga")){
			jr = executeTNTDescarga(parameters, listIds);
		}else {
			jr = executeGM(parameters, listIds);
		}
		
		Carregamento carregamento = (Carregamento) embarqueService.findById(Long.valueOf(listIds.get(0)));
		Pessoa pessoa = pessoaDAO.findByNrIdentificacao(carregamento.getCnpjRemetenteCliente().toString());
		UsuarioADSM usuario = usuarioADSMDAO.findById(carregamento.getMatriculaChefia());
		
		Map parametersReport = new HashMap();
		parametersReport.put("cnpjRemetenteCliente", carregamento.getCnpjRemetenteCliente());
		parametersReport.put("veiculo", carregamento.getFrotaVeiculo()+"/"+carregamento.getPlacaVeiculo());
		parametersReport.put("rotaCarregamento", carregamento.getRotaCarregamento());
		parametersReport.put("origem", pessoa.getNmPessoa());
		parametersReport.put("autorizador", usuario.getNmUsuario());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("destino", "TNT - Mercurio - " + SessionUtils.getFilialSessao().getSgFilial());
		parametersReport.put("id_carregamento", carregamento.getIdCarregamento());
		parametersReport.put("tipoEmitente", tipoEmitente);
		parametersReport.put("tituloRelatorio", tituloRelatorio);
		
		jr.setParameters(parametersReport);
        
        return jr;
	}
	
	public JRReportDataObject executeGM(Map parameters, List<String> listIds) throws Exception {
				
		SqlTemplate sql = null;
		
		if(listIds != null){
			for(String ids: listIds){
				if(ids.equals("")){
					throw new BusinessException("LMS-04311");
				}else{
					List<Volume> volumes = this.volumeService.findVolumesByCarregamento(Long.valueOf(ids));
		
					if(volumes == null || volumes.size()== 0){
						throw new BusinessException("LMS-04310");
					}
				}
			}
		}else{
			throw new BusinessException("LMS-04311");
		}
		
		StringBuilder query = new StringBuilder();
		query
		.append(" SELECT ")
		.append("   'GM' as tipo_relatorio, ")
			.append("dc.mapa_carregamento, ")
			.append("ca.doca_carregamento, ")
		.append("   ca.rota_carregamento, ")
		.append("	ca.cnpj_reme_clnt, ")
		.append("	pe.nm_pessoa, ")
		.append("   dc.codigo_destino, ")
		.append("   dc.rota_destino, ")
		.append("	tc.total_volumes, ")
		.append("	tc.total_cubagem, ")
		.append("	tc.total_peso, ")
		.append("   dc.codigo_volume, ")
		.append("	CASE ")
		.append("       WHEN vol.codigo_status is null THEN 'Falta' ") 
		.append("       WHEN vol.codigo_status = '1'   THEN 'Carregado' ") 
		.append("	    WHEN vol.codigo_status = '2'   THEN 'Avaria/Expedido' ")
		.append("	    WHEN vol.codigo_status = '3'   THEN 'Carregando fora do mapa' ") 
		.append("	    WHEN vol.codigo_status = '4'   THEN 'Descarregado' ") 
		.append("	    WHEN vol.codigo_status = '5'   THEN 'Avaria' ") 
		.append("	    WHEN vol.codigo_status = '6'   THEN 'Expedido' ") 
		.append("	  ELSE '' ")
		.append("	END codigo_status, ")
		.append("	vol.matricula_responsavel, ")
		.append("	vol.codigo_volume as vol_codigo_volume, ")
			.append("ca.matricula_chefia, ")
		.append("	ca.placa_veiculo, ")
		.append("	ca.frota_veiculo, ")
		.append("	ca.id_carregamento, ")
		.append("	' - ' || ua.nm_usuario nm_usuario, ")
		.append("	ua.nr_matricula ")
		.append("FROM detalhe_carregamento dc ")
		.append("	LEFT JOIN volume vol ON dc.codigo_volume = vol.codigo_volume ")
		.append("	LEFT JOIN carregamento ca ON vol.id_carregamento = ca.id_carregamento ")
		.append("	LEFT JOIN total_carregamento tc ON dc.mapa_carregamento = tc.mapa_carregamento ")
		.append("	LEFT JOIN usuario_adsm ua ON vol.matricula_responsavel = ua.id_usuario ")
		.append("	LEFT JOIN pessoa pe ON ca.cnpj_reme_clnt = pe.nr_identificacao ")
		.append("WHERE dc.id_cabecalho_carregamento IN( ")
		.append("	SELECT vol2.id_cabecalho_carregamento ") 
		.append("	FROM volume vol2 ")
		.append("	WHERE vol2.id_carregamento = ?) and")
		.append("	dc.id_cabecalho_carregamento IN ( ")
		.append("	select cc.id_cabecalho_carregamento from cabecalho_carregamento cc where cc.id_cabecalho_carregamento IN( ")
		.append("	select v.id_cabecalho_carregamento from volume v where v.id_carregamento = ? and v.codigo_status IN ('1','2','3','6'))) ")
		.append("order by decode(ca.id_carregamento,?,1,2), ca.frota_veiculo, dc.mapa_carregamento, dc.codigo_destino, vol.codigo_status, vol.codigo_volume ");
		
		return executeQuery(query.toString(),new Object[]{listIds.get(0), listIds.get(0), listIds.get(0)});
	}
		
	public JRReportDataObject executeTNTVeiculo(Map parameters, List<String> listIds) throws Exception {
		
		SqlTemplate sql = null;	
		
		if(listIds != null){
			for(String ids: listIds){
				if(ids.equals("")){
					throw new BusinessException("LMS-04311");
				}else{
					List<Volume> volumes = this.volumeService.findVolumesByCarregamento(Long.valueOf(ids));
        
					if(volumes == null || volumes.size()== 0){
						throw new BusinessException("LMS-04310");
	}
				}
			}
		}else{
			throw new BusinessException("LMS-04311");
		}
	
		StringBuilder query = new StringBuilder();
		query  
		.append(" SELECT 'TNTV' AS tipo_relatorio, ")
		.append("   dc5.codigo_volume codigo_volume_dc, ")
		.append("   dc5.codigo_destino, ")
		.append("   v2.codigo_volume   AS vol_codigo_volume, ")
		.append("   ca.id_carregamento, ")
		.append("   v2.id_carregamento as id_carregamento_vol, ")
		.append("   pe2.nm_pessoa, ")
		.append("   ca.matricula_chefia, ")
		.append("   ca.cnpj_reme_clnt, ")
		.append("   ca.rota_carregamento, ")
		.append("   CASE ")
		.append("     WHEN (nvl2(v2.codigo_volume, 'S', 'N') = 'S' and ")
		.append("         nvl(v2.codigo_status, 4) = 6  and ")
		.append("   ca.id_carregamento = v2.id_carregamento)  ")
	    .append("     THEN ca.frota_veiculo ")
	    .append("     ELSE cveiculo.frota_veiculo  ")
	    .append("   END frota_veiculo, ")  
		.append("   tc.total_volumes total_volumes, ")
		.append("   tc.total_cubagem total_cubagem, ")
		.append("   tc.total_peso total_peso, ")
		.append("    ")
		.append("   nvl2(dc5.codigo_volume, 'S', 'N') detalhe_carregamento, ")
		.append("   nvl2(vnf2.nr_volume_coleta, 'S', 'N') volume_nota_fiscal, ")
		.append("   nvl2(v2.codigo_volume, 'S', 'N') volume, ")
		.append("   nvl(v2.codigo_status ,4) status_volume, ")
		.append("    ")
		.append("    ")
		.append("   CASE       ")
		.append("     WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("        and nvl(v2.codigo_status,4) = 6 ) ")
		.append("     THEN v2.codigo_volume ")
		.append("     ELSE '' ")
		.append("   END  vol_codigo_volume, ")
		.append("   CASE       ")
		.append("     WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("        and nvl(v2.codigo_status,4) = 6 ) ")
		.append("     THEN ca.doca_carregamento ")
		.append("     ELSE ''  ")
		.append("   END doca_carregamento, ")
		.append("   CASE       ")
		.append("     WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("        and nvl(v2.codigo_status,4) = 6   and ")
		.append("   ca.id_carregamento = v2.id_carregamento) ")
		.append("     THEN ca.placa_veiculo ")
		.append("     ELSE cveiculo.placa_veiculo  ")
		.append("   END  placa_veiculo, ")
		.append("   CASE       ")
		.append("     WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("        and nvl(v2.codigo_status,4) = 6 ) ")
		.append("     THEN u.nr_matricula ")
		.append("     ELSE  uveiculo.nr_matricula  ")
		.append("   END  nr_matricula, ")
		.append("    ")
		.append("   CASE       ")
		.append("     WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("        and nvl(v2.codigo_status,4) = 6 ) ")
		.append("     THEN ' - ' || u.nm_usuario ")
		.append("     ELSE ' - ' || uveiculo.nm_usuario  ")
		.append("   END  nm_usuario,      ")
		.append("   CASE ")
		.append("     WHEN nvl2(dc5.codigo_volume, 'S', 'N') = 'S'   ")
		.append("     THEN to_char(dc5.mapa_carregamento) ")
		.append("     ELSE ''  ")
		.append("   END mapa_carregamento, ")
		.append("   CASE ")
		.append("     WHEN (nvl2(vnf2.nr_volume_coleta, 'S', 'N') = 'S' ) ")
		.append("     THEN  pe.nm_pessoa ")
		.append("     WHEN nvl2(dc5.rota_destino, 'S', 'N') = 'S' ")
		.append("     THEN dc5.rota_destino ")
		.append("     ELSE  '' ")
		.append("   END rota_destino, ")
		.append("   CASE ")
		.append("     WHEN nvl2(vnf2.nr_volume_coleta, 'S', 'N') = 'S'  ")
		.append("     THEN vnf2.nr_volume_coleta  ")
		.append("      ")
		.append("     WHEN nvl2(dc5.codigo_volume, 'S', 'N') = 'S'  ")
		.append("     THEN dc5.codigo_volume  ")
		.append("      ")
		.append("     ELSE ''  ")
		.append("   END codigo_volume, ")
		.append("   CASE   ")
		.append("     WHEN nvl2(v2.codigo_volume, 'S', 'N') = 'S' and   ")
		.append("          v2.codigo_status = 6 AND   ")
		.append("          nvl2(vnf2.nr_volume_coleta, 'S', 'N') = 'N'    and ")
		.append("   ca.id_carregamento = v2.id_carregamento  ")
		.append("     THEN 'Não Faturado'   ")
		.append("     WHEN nvl2(vnf2.nr_volume_coleta, 'S', 'N') = 'S' ")
		.append("     THEN 'Faturado'             ")
		.append("     WHEN nvl2(v2.codigo_volume, 'S', 'N') = 'N' or   ")
		.append("          v2.codigo_status <> 6    and ")
		.append("   ca.id_carregamento = v2.id_carregamento  ")
		.append("     THEN 'Não Embarcado'             ")
		.append("     WHEN vveiculo.codigo_status is not null and vveiculo.codigo_status = 6 ")
		.append("     THEN 'Expedido' ")
		.append("     WHEN vveiculo.codigo_status is not null and vveiculo.codigo_status = 1 ")
		.append("     THEN 'Carregado' ")
		.append("     ELSE ''    ")
		.append("   END codigo_status   ")
		.append(" FROM ")
		.append("   (SELECT dc3.codigo_volume ")
		.append("   FROM ")
		.append("     ( SELECT DISTINCT dc2.mapa_carregamento ")
		.append("     FROM volume_nota_fiscal vnf, ")
		.append("       detalhe_carregamento dc, ")
		.append("       detalhe_carregamento dc2 ")
		.append("     WHERE vnf.id_monitoramento_descarga = ? ")
		.append("     AND dc.codigo_volume                = vnf.nr_volume_coleta ")
		.append("     AND dc.mapa_carregamento            =dc2.mapa_carregamento ")
		.append("     ) mapas , ")
		.append("     detalhe_carregamento dc3 ")
		.append("   WHERE dc3.mapa_carregamento=mapas.mapa_carregamento ")
		.append("   UNION ")
		.append("   SELECT dc4.codigo_volume ")
		.append("   FROM ")
		.append("     ( SELECT DISTINCT v.mapa_carregamento ")
		.append("     FROM volume v ")
		.append("     WHERE id_carregamento = ? ")
		.append("     ) mapas2 , ")
		.append("     detalhe_carregamento dc4 ")
		.append("   WHERE dc4.mapa_carregamento = mapas2.mapa_carregamento ")
		.append("   ) volumes , ")
		.append("   detalhe_carregamento dc5 , ")
		.append("   volume_nota_fiscal vnf2 , ")
		.append("   nota_fiscal_conhecimento nfc , ")
		.append("   volume v2 , ")
		.append("   carregamento ca , ")
		.append("   pessoa pe , ")
		.append("   pessoa pe2 , ")
		.append("   total_carregamento tc , ")
		.append("   usuario_adsm u , ")
		.append("   usuario_adsm u2, ")
		.append("   docto_servico ds ,  ")
		.append("   carregamento cveiculo, ")
		.append("   volume vveiculo, ")
		.append("   usuario_adsm uveiculo  ")
		.append(" WHERE volumes.codigo_volume          = dc5.codigo_volume (+) ")
		.append(" AND volumes.codigo_volume            = vnf2.nr_volume_coleta (+) ")
		.append(" AND volumes.codigo_volume            = v2.codigo_volume (+) ")
		.append(" and  v2.codigo_volume = vveiculo.codigo_volume (+) ")
		.append(" and vveiculo.id_carregamento = cveiculo.id_carregamento (+) ")
		.append(" and vveiculo.matricula_responsavel = uveiculo.id_usuario(+) ")
		.append(" AND ca.id_carregamento               = ? ")
		.append(" AND u2.id_usuario                    = ca.matricula_chefia ")
		.append(" AND dc5.mapa_carregamento            = tc.mapa_carregamento (+) ")
		.append(" AND v2.matricula_responsavel         = u.id_usuario (+) ")
		.append(" AND vnf2.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento(+) ")
		.append(" AND nfc.id_conhecimento              = ds.id_docto_servico (+) ")
		.append(" AND ds.id_cliente_destinatario       = pe.id_pessoa (+) ")  
		.append(" AND ca.cnpj_reme_clnt = pe2.nr_identificacao ")
		.append("  order by decode(id_carregamento_vol,?,1,2), ")
		.append("			decode(codigo_status,  ")
        .append("               'Faturado', 1 ")
        .append("             , 'Não Faturado', 2 ")
        .append("             , 'Carregado', 3 ")
        .append("             , 'Expedido', 4 ")
        .append("             , 'Descarregado', 5 ")
        .append("             , 'Não Descarregado', 6 ")
        .append("             , 'Não Embarcado', 7 ")
        .append("             , 8), ")
        .append("        ") 
        .append("       frota_veiculo, mapa_carregamento ");     
	
		return executeQuery(query.toString(),new Object[]{ parameters.get("idMonitoramentoDescarga") ,listIds.get(0), listIds.get(0), listIds.get(0)});
	}
	
	public JRReportDataObject executeTNTDescarga(Map parameters, List<String> listIds) throws Exception {
		
		SqlTemplate sql = null;	
		
		if(listIds != null){
			for(String ids: listIds){
				if(ids.equals("")){
					throw new BusinessException("LMS-04311");
				}else{
					List<Volume> volumes = this.volumeService.findVolumesByCarregamento(Long.valueOf(ids));
		
					if(volumes == null || volumes.size()== 0){
						throw new BusinessException("LMS-04310");
					}
				}
			}
		}else{
			throw new BusinessException("LMS-04311");
		}

		
		StringBuilder query = new StringBuilder();
		query
		.append("  select  ")
		.append("      'TNTD' as tipo_relatorio, ")
		.append("      dc5.codigo_volume codigo_volume_dc, ")
		.append("      dc5.codigo_destino, ")
		.append("      v2.codigo_volume   AS vol_codigo_volume, ")
		.append("      ca.id_carregamento, ")
		.append("      v2.id_carregamento as id_carregamento_vol, ")
		.append("      pe2.nm_pessoa, ")
		.append("      ca.matricula_chefia, ")
		.append("      CASE ")
		.append("        WHEN (nvl2(v2.codigo_volume, 'S', 'N') = 'S' and ")
		.append("            nvl(v2.codigo_status, 4) = 6   and ")
		.append("   ca.id_carregamento = v2.id_carregamento  )  ")
	    .append("        THEN ca.frota_veiculo ")
	    .append("        ELSE cveiculo.frota_veiculo  ")
	    .append("      END frota_veiculo, ")
		.append("      ca.cnpj_reme_clnt, ")
		.append("      ca.rota_carregamento, ")
		.append("      tc.total_volumes total_volumes, ")
		.append("      tc.total_cubagem total_cubagem, ")
		.append("      tc.total_peso total_peso, ")
		.append("       ")
		.append("      nvl2(dc5.codigo_volume, 'S', 'N') detalhe_carregamento, ")
		.append("      nvl2(vnf2.nr_volume_coleta, 'S', 'N') volume_nota_fiscal, ")
		.append("      nvl2(v2.codigo_volume, 'S', 'N') volume, ")
		.append("      nvl(v2.codigo_status ,4) status_volume, ")
		.append("       ")
		.append("       ")
		.append("      CASE       ")
		.append("        WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("           and nvl(v2.codigo_status,4) = 6 ) ")
		.append("        THEN v2.codigo_volume ")
		.append("        ELSE '' ")
		.append("      END  vol_codigo_volume, ")
		.append("      CASE       ")
		.append("        WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("           and nvl(v2.codigo_status,4) = 6 ) ")
		.append("        THEN ca.doca_carregamento ")
		.append("        ELSE cveiculo.doca_carregamento  ")
		.append("      END doca_carregamento, ")
		.append("      CASE       ")
		.append("        WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("           and nvl(v2.codigo_status,4) = 6    and ")
		.append("   ca.id_carregamento = v2.id_carregamento  ) ")
		.append("        THEN ca.placa_veiculo ")
		.append("        ELSE cveiculo.placa_veiculo  ")
		.append("      END  placa_veiculo, ")
		.append("      CASE       ")
		.append("        WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("           and nvl(v2.codigo_status,4) = 6 ) ")
		.append("        THEN u.nr_matricula ")
		.append("        ELSE  uveiculo.nr_matricula   ")
		.append("      END  nr_matricula, ")
		.append("       ")
		.append("      CASE       ")
		.append("        WHEN  (nvl2(v2.codigo_volume,'S','N')    = 'S'    ")
		.append("           and nvl(v2.codigo_status,4) = 6 ) ")
		.append("        THEN ' - ' || u.nm_usuario ")
		.append("        ELSE ' - ' || uveiculo.nm_usuario  ")
		.append("      END  nm_usuario,      ")
		.append("      CASE ")
		.append("        WHEN nvl2(dc5.codigo_volume, 'S', 'N') = 'S'   ")
		.append("        THEN to_char(dc5.mapa_carregamento) ")
		.append("        ELSE ''  ")
		.append("      END mapa_carregamento, ")
		.append("      CASE ")
		.append("        WHEN (nvl2(vnf2.nr_volume_coleta, 'S', 'N') = 'S' ) ")
		.append("        THEN  pe.nm_pessoa ")
		.append("        WHEN nvl2(dc5.rota_destino, 'S', 'N') = 'S' ")
		.append("        THEN dc5.rota_destino ")
		.append("        ELSE  '' ")
		.append("      END rota_destino, ")
		.append("      CASE ")
		.append("        WHEN nvl2(vnf2.nr_volume_coleta, 'S', 'N') = 'S'  ")
		.append("        THEN vnf2.nr_volume_coleta  ")
		.append("         ")
		.append("        WHEN nvl2(dc5.codigo_volume, 'S', 'N') = 'S'  ")
		.append("        THEN dc5.codigo_volume  ")
		.append("         ")
		.append("        ELSE ''  ")
		.append("      END codigo_volume, ")
		.append("      CASE ")
		.append("        WHEN vnf2.nr_volume_embarque is not null ")  
		.append("        THEN 'Descarregado' ")
		.append("        WHEN ((nvl2(v2.codigo_volume,'S','N')  = 'N') ")  
		.append("               or (nvl2(v2.codigo_volume,'S','N')    = 'S'   and nvl(v2.codigo_status,4) <> 6 )) ") 
		.append("          AND nvl2(vnf2.nr_volume_coleta,'S','N')='S'    and  ")
		.append("  ca.id_carregamento = v2.id_carregamento  ")
		.append("        THEN 'Não Embarcado'           ")
		.append("        WHEN nvl2(v2.codigo_volume, 'S', 'N') = 'S'")  
		.append("          and nvl2(vnf2.nr_volume_coleta, 'S', 'N') = 'N'") 
		.append("          and v2.codigo_status = '6'    and ")
		 .append("  ca.id_carregamento = v2.id_carregamento  ")
		 .append("       THEN 'Não Faturado' ")
        .append(" WHEN vveiculo.codigo_status is null") 
        .append(" THEN 'Não Embarcado' ")
		.append("        WHEN  vnf2.nr_volume_embarque is null  and") 
		.append("   ca.id_carregamento = v2.id_carregamento  ")
		.append("        THEN 'Não Descarregado' ")
		.append("        WHEN ((nvl2(v2.codigo_volume,'S','N')  = 'N') or ")  
		.append("                (nvl2(v2.codigo_volume,'S','N')    = 'S'   and nvl(v2.codigo_status,4) <> 6 )) ") 
		.append("           AND nvl2(vnf2.nr_volume_coleta,'S','N')='N'  and ")
		.append("   ca.id_carregamento = v2.id_carregamento  ")
		.append("        THEN 'Não Embarcado' ")
		.append("        WHEN vveiculo.codigo_status is not null and vveiculo.codigo_status = 6 ") 
        .append("  THEN 'Expedido' ")
        .append(" WHEN vveiculo.codigo_status is not null and vveiculo.codigo_status = 1 ") 
        .append(" THEN 'Carregado'  ") 
		.append("        ELSE ''  ")
		.append("      END codigo_status ")
		.append("        ")
		.append("  from (select dc3.codigo_volume ")
		.append("          from (select distinct dc2.mapa_carregamento ")
		.append("                  from volume_nota_fiscal   vnf, ")
		.append("                       detalhe_carregamento dc, ")
		.append("                       detalhe_carregamento dc2 ")
		.append("                 where vnf.id_monitoramento_descarga = ? ")
		.append("                   and dc.codigo_volume = vnf.nr_volume_coleta ")
		.append("                   and dc.mapa_carregamento = dc2.mapa_carregamento) mapas, ")
		.append("               detalhe_carregamento dc3 ")
		.append("         where dc3.mapa_carregamento = mapas.mapa_carregamento ")
		.append("        union ")
		.append("        select dc4.codigo_volume ")
		.append("          from (select distinct v.mapa_carregamento ")
		.append("                  from volume v ")
		.append("                 where id_carregamento = ?) mapas2, ")
		.append("               detalhe_carregamento dc4 ")
		.append("         where dc4.mapa_carregamento = mapas2.mapa_carregamento) volumes, ")
		.append("       detalhe_carregamento dc5, ")
		.append("       volume_nota_fiscal vnf2, ")
		.append("       nota_fiscal_conhecimento nfc, ")
		.append("       volume v2, ")
		.append("       carregamento ca, ")
		.append("       pessoa pe, ")
		.append("       pessoa pe2, ")
		.append("       total_carregamento tc, ")
		.append("       usuario_adsm u, ")
		.append("       usuario_adsm u2, ")
		.append("   docto_servico ds ,  ")
		.append("   carregamento cveiculo, ")
		.append("   usuario_adsm uveiculo, ")
		.append("   volume vveiculo ")
		.append("  where volumes.codigo_volume = dc5.codigo_volume(+) ")
		.append("    and volumes.codigo_volume = vnf2.nr_volume_coleta(+) ")
		.append("    and volumes.codigo_volume = v2.codigo_volume(+) ")
		.append(" and  v2.codigo_volume = vveiculo.codigo_volume (+) ")
		.append(" and vveiculo.id_carregamento = cveiculo.id_carregamento (+) ")
		.append(" and vveiculo.matricula_responsavel = uveiculo.id_usuario(+) ")
		.append("    and ca.id_carregamento = ? ")
		.append("   ")
		.append("    and u2.id_usuario = ca.matricula_chefia ")
		.append("    and dc5.mapa_carregamento = tc.mapa_carregamento(+) ")
		.append("    and v2.matricula_responsavel = u.id_usuario(+) ")
		.append("    and vnf2.id_nota_fiscal_conhecimento = ")
		.append("       nfc.id_nota_fiscal_conhecimento(+) ")
		.append("    and nfc.id_conhecimento = ds.id_docto_servico(+) ")
		.append("    and ds.id_cliente_destinatario = pe.id_pessoa(+) ")
		.append("    and ca.cnpj_reme_clnt = pe2.nr_identificacao ")
		.append("  order by decode(id_carregamento_vol,?,1,2), ")
		.append("			decode(codigo_status,  ")
        .append("               'Descarregado', 1 ")
        .append("             , 'Não Descarregado', 2 ")
        .append("             , 'Não Faturado', 3 ")
        .append("             , 'Embarcado', 4 ")
        .append("             , 'Carregado', 5 ")
        .append("             , 'Expedido', 6 ")
        .append("             , 'Não Embarcado', 7 ")
        .append("             , 8), ")
        .append("        ") 
        .append("       frota_veiculo, mapa_carregamento "); 
		
		return executeQuery(query.toString(),new Object[]{parameters.get("idMonitoramentoDescarga") ,listIds.get(0), listIds.get(0), listIds.get(0)});
	}
	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}	
	
	public EmbarqueService getEmbarqueService() {
		return embarqueService;
	}
	
	public void setEmbarqueService(EmbarqueService embarqueService) {
		this.embarqueService = embarqueService;
	}

	public VolumeService getVolumeService() {
		return volumeService;
	}

	public void setVolumeService(VolumeService volumeService) {
		this.volumeService = volumeService;
	}
	
	public DetalheCarregamentoDAO getDetalheCarregamentoDao() {
		return detalheCarregamentoDao;
	}

	public void setDetalheCarregamentoDao(
			DetalheCarregamentoDAO detalheCarregamentoDao) {
		this.detalheCarregamentoDao = detalheCarregamentoDao;
	}

	public CabecalhoCarregamentoDAO getCabecalhoCarregamentoDao() {
		return cabecalhoCarregamentoDao;
	}

	public void setCabecalhoCarregamentoDao(
			CabecalhoCarregamentoDAO cabecalhoCarregamentoDao) {
		this.cabecalhoCarregamentoDao = cabecalhoCarregamentoDao;
	}

	public TotalCarregamentoDAO getTotalCarregamentoDao() {
		return totalCarregamentoDao;
	}

	public void setTotalCarregamentoDao(TotalCarregamentoDAO totalCarregamentoDao) {
		this.totalCarregamentoDao = totalCarregamentoDao;
	}	

	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}

	public UsuarioADSMDAO getUsuarioADSMDAO() {
		return usuarioADSMDAO;
	}

	public void setUsuarioADSMDAO(UsuarioADSMDAO usuarioADSMDAO) {
		this.usuarioADSMDAO = usuarioADSMDAO;
	}	
	
	
} 