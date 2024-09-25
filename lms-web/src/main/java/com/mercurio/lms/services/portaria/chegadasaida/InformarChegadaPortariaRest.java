package com.mercurio.lms.services.portaria.chegadasaida;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.service.ConfiguracaoAuditoriaService;
import com.mercurio.lms.portaria.model.service.NewInformarChegadaService;
import com.mercurio.lms.rest.portaria.chegadasaida.dto.InformarChegadaSaidaDTO;
import com.mercurio.lms.util.session.SessionKey;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Locale;

@Path("portaria/informarChegadaPortaria")
public class InformarChegadaPortariaRest {

	private static final long USUARIO_INTEGRACAO = 5000L;

	@InjectInJersey
	private NewInformarChegadaService informarChegadaService;
	
	@InjectInJersey
	private ConfiguracaoAuditoriaService configuracaoAuditoriaService;

	@InjectInJersey
	private UsuarioService usuarioService;

	@InjectInJersey
	private FilialService filialService;

	@InjectInJersey
	private ControleTrechoService controleTrechoService;


	@InjectInJersey
	private ControleCargaService controleCargaService;

	@InjectInJersey
	private ParametroGeralService parametroGeralService;

	@InjectInJersey
	private MeioTransporteService meioTransporteService;

	@POST
	@Path("/")
	public Response informarChegadaPortaria(InformarChegadaSaidaDTO dto) {
		try {
			String idEmpresa = this.parametroGeralService.findSimpleConteudoByNomeParametro("ID_EMPRESA_MERCURIO");
			Filial filialChegada = filialService.findFilialBySgFilialAndIdEmpresa(dto.getSgFilial(), Long.parseLong(idEmpresa));

			if (filialChegada == null) {
				throw new BusinessException("Filial de Chegada não encontrada");
			}

			dto.setIdFilial(filialChegada.getIdFilial());
			SessionContext.setUser(this.usuarioService.findById(USUARIO_INTEGRACAO));
			SessionContext.set(SessionKey.FILIAL_KEY, filialChegada);

			ControleTrecho controleTrecho = this.controleTrechoService.findControleTrechoByNrControleCargaAndSgFilialOrigem(dto.getSgFilialOrigem(), dto.getNrControleCarga());
			if (controleTrecho == null) {
				throw new BusinessException("Nao existe Controle Trecho para esse controle de carga");
			}

			dto.setIdControleTrecho(controleTrecho.getIdControleTrecho());

			ControleCarga controleCarga = this.controleCargaService.findByIdControleCarga(controleTrecho.getControleCarga().getIdControleCarga());
			if(!controleCarga.getRota().getDsRota().toUpperCase(Locale.ROOT).contains(filialChegada.getSgFilial().toUpperCase(Locale.ROOT))){
				throw new BusinessException("Filial de Chegada não está prevista na ROTA");
			}

			MeioTransporte meioTransporte = controleCarga.getMeioTransporteByIdTransportado();

			if (!meioTransporte.getNrIdentificador().replace("-", "").equals(dto.getNrIdentificadorTransportado().replace("-", ""))){
				throw new BusinessException("Placa do Meio de Transporte diverge do controle de carga");
			}

			this.informarChegadaService.executeSalvar(dto.toMapChegada());
			return Response.ok().build();
		} catch (BusinessException | IllegalArgumentException | NullPointerException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}