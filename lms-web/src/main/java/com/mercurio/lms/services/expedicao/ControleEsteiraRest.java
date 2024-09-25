package com.mercurio.lms.services.expedicao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.expedicao.ControleEsteiraDMN;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.expedicao.VolumeRPP;

import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.model.ControleEsteira;
import com.mercurio.lms.expedicao.model.HistoricoAfericao;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.ControleEsteiraService;
import com.mercurio.lms.expedicao.model.service.HistoricoAfericaoSorterService;
import com.mercurio.lms.util.JTDateTimeUtils;

@Public
@Path("/expedicao/controleEsteira")
public class ControleEsteiraRest {
	private Logger log = LogManager.getLogger(ControleEsteiraRest.class);
	public static final String USER_INTEGRACAO = "integracao";

	@InjectInJersey ControleEsteiraService controleEsteiraService;
	@InjectInJersey UsuarioService usuarioService;
	@InjectInJersey ConhecimentoService conhecimentoService;
	@InjectInJersey HistoricoAfericaoSorterService historicoAfericaoSorterService;

	@POST
	@Path("atualizarLogAfericao")
	public Response atualizarLogAfericao(@HeaderParam(value="login") String login, VolumeRPP volumeRPP) {
		if (volumeRPP != null) {
			Map<String, Object> error = new HashMap<String, Object>();
			setarUsuarioSessao(login);

			try {
				HistoricoAfericao historicoAfericao = popularHistoricoAfericao(volumeRPP);
				String sgFilial = getSgFilial(volumeRPP);
				historicoAfericaoSorterService.storeHistoricoAfericaoFromIntegracao(historicoAfericao, sgFilial);
			} catch (Exception e) {
				log.error("Erro ao processar volume aferição", e);
				error = formatError(volumeRPP.getCodBarras(), "Erro ao processar volume aferição", e.getMessage());
			}
			
			return Response.ok(error).build();
		} else {
			throw new RuntimeException("Dados Não Informados");
		}
	}
	
	private String getSgFilial(VolumeRPP volumeRPP) {
		return volumeRPP.getNrLote().substring(0, 3).toUpperCase();
	}

	private HistoricoAfericao popularHistoricoAfericao(VolumeRPP volumeRPP){
		HistoricoAfericao historicoAfericao = new HistoricoAfericao();
		historicoAfericao.setDhAfericao(getDhAfericao(volumeRPP));
		historicoAfericao.setNrCodigoBarras(volumeRPP.getCodBarras());
		historicoAfericao.setNrAltura(arredondarValor(volumeRPP.getAltura()));
		historicoAfericao.setNrLargura(arredondarValor(volumeRPP.getLargura()));
		historicoAfericao.setNrComprimento(arredondarValor(volumeRPP.getComprimento()));
		historicoAfericao.setPsAferido(volumeRPP.getPeso());
		return historicoAfericao;
	}

	private Long arredondarValor(BigDecimal valor){
		return valor.setScale(0, BigDecimal.ROUND_UP).longValueExact();
	}
	
	private DateTime getDhAfericao(VolumeRPP volumeRPP) {
		TimeOfDay horaAfericao = JTDateTimeUtils.convertDataStringToTimeOfDay(volumeRPP.getHora());
		YearMonthDay dataAfericao = JTDateTimeUtils.convertDataStringToYearMonthDay(volumeRPP.getData(), JTDateTimeUtils.DATETIME_WITH_WITHOUT_TIME_PATTERN);
		return dataAfericao.toDateTime(horaAfericao);
	}
	
	@POST
	@Path("atualizaInformacaoVolumes")
	public Response atualizaInformacaoVolumes(@HeaderParam(value="login") String login, VolumeRPP volumeRPP) {
		if (volumeRPP != null) {
			Map<String, Object> error = new HashMap<>();
			setarUsuarioSessao(login);

			ControleEsteira controleEsteira = controleEsteiraService.gerarControleEsteira(volumeRPP);

			try {
				controleEsteiraService.updateInformacaoVolumes(controleEsteira);
			} catch (Exception e) {
				log.error("Erro ao processar volume", e);
				error = formatError(volumeRPP.getCodBarras(), "Erro ao processar volume", e.getMessage());
			}
			
			return Response.ok(error).build();
		} else {
			throw new RuntimeException("Dados Não Informados");
		}
	}

	private void setarUsuarioSessao(String login) {
		Usuario u = usuarioService.findUsuarioByLogin(login);
		SessionContext.setUser(u);
	}
	
	@GET
	@Path("finalizaConhecimento")
	public Response finalizaConhecimento( @QueryParam("id") Long idConhecimento,@QueryParam("nrConhecimento") Long nrConhecimentoVNF ) {
		if( idConhecimento != null ){
			setarUsuarioSessao(USER_INTEGRACAO);
			controleEsteiraService.executeFinalizaConhecimento(newConhecimentoRPP(idConhecimento,nrConhecimentoVNF));
		}
		return Response.ok("Rotina 'finalizaConhecimento' executada sem erros").build();
	}

	@GET
	@Path("finalizaDescargaColeta")
	public Response finalizaDescargaColeta( @QueryParam("id") Long idMonitoramento) {
		if( idMonitoramento != null ){
			setarUsuarioSessao(USER_INTEGRACAO);
			controleEsteiraService.executeFinalizaDescargaColeta(idMonitoramento);
		}
		return Response.ok("Rotina 'finalizaDescargaColeta' executada sem erros").build();
	}

	@GET
	@Path("finalizaConhecimentoPorDescarga")
	public Response finalizaConhecimentoPorDescarga(@QueryParam("id") Long idMonitoramento) {
		Integer cont = 0;
		if( idMonitoramento != null ){
			setarUsuarioSessao(USER_INTEGRACAO);
			List<Map<String, Object>> list = conhecimentoService.findPendentesPorCalculo(idMonitoramento);
			if( CollectionUtils.isNotEmpty( list ) ){
				cont = list.size();
				for(Map<String,Object> map : list ){
					controleEsteiraService.executeFinalizaConhecimento(newConhecimentoRPP(MapUtilsPlus.getLong(map, "idConhecimento",0L),MapUtilsPlus.getLong(map, "nrConhecimento",0L)));
				}
			}
		}
		return Response.ok(String.format("Rotina 'finalizaConhecimentoPorDescarga' executada sem erros, %s conhecimentos processados.",cont)).build();
	}
	
	@GET
	@Path("finalizaConhecimentoPendentes")
	public Response finalizaConhecimentoPendentes() {
		Integer cont = 0;
		setarUsuarioSessao(USER_INTEGRACAO);
		List<Map<String, Object>> list = conhecimentoService.findPendentesPorCalculo(null);
		if( CollectionUtils.isNotEmpty( list ) ){
			cont = list.size();
			for(Map<String,Object> map : list ){
				controleEsteiraService.executeFinalizaConhecimento(newConhecimentoRPP(MapUtilsPlus.getLong(map, "idConhecimento",0L),MapUtilsPlus.getLong(map, "nrConhecimento",0L)));
			}
		}
		return Response.ok(String.format("Rotina 'finalizaConhecimentoPendentes' executada sem erros, %s conhecimentos processados.",cont)).build();
	}

	@POST
	@Path("atualizaInformacaoDescarga")
	public Response atualizaInformacaoDescarga(@HeaderParam(value="login") String login, ControleEsteiraDMN controleEsteiraDMN) {

		setarUsuarioSessao(login);
		ControleEsteira controleEsteira = this.controleEsteiraService.gerarControleEsteira(controleEsteiraDMN.getNrLote(),
				controleEsteiraDMN.getCodBarras(), controleEsteiraDMN.getData(), controleEsteiraDMN.getHora());

		controleEsteiraService.atualizaInformacaoDescarga(controleEsteira);

		return Response.ok().build();

	}

	private Map<String, Object> newConhecimentoRPP(Long idConhecimento,Long nrConhecimentoVNF){
		Map<String, Object> conhecimentoRPP = new HashMap<String, Object>();
		conhecimentoRPP.put("idConhecimento", idConhecimento);
		conhecimentoRPP.put("nrConhecimento", nrConhecimentoVNF);
		conhecimentoRPP.put("recalculo",false);
		return conhecimentoRPP;
	}

	private Map<String,Object> formatError(String codBarra, String descricao, String msg){
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("codBarra", codBarra);
		r.put("error", descricao);
		r.put("cause", msg);
		return r;
	}
	
}
