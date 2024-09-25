package com.mercurio.lms.rest.vendas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.security.model.service.UsuarioService;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.rest.vendas.dto.ConhecimentosNaoComissionaveisDTO;
import com.mercurio.lms.rest.vendas.dto.ConhecimentosNaoComissionaveisFilterDTO;
import com.mercurio.lms.rest.vendas.dto.ConhecimentosNaoComissionaveisGridDTO;
import com.mercurio.lms.vendas.model.service.DoctoServicoSemComissaoService;

@Path("/vendas/conhecimentosNaoComissionaveis")
public class ConhecimentosNaoComissionaveisRest extends
		BaseCrudRest<ConhecimentosNaoComissionaveisDTO, ConhecimentosNaoComissionaveisGridDTO, ConhecimentosNaoComissionaveisFilterDTO> {

	@InjectInJersey
	private ConhecimentoService conhecimentoService;

	@InjectInJersey
	private UsuarioService usuarioService;

	@InjectInJersey
	private DoctoServicoSemComissaoService doctoServicoSemComissaoService;

	public static final String EXCEPTION_REGISTRO_NAO_ENCONTRADO = "LMS-00053";
	public static final String EXCEPTION_FILTRO_INSUFICIENTE = "LMS-10047";

	@Override
	protected List<ConhecimentosNaoComissionaveisGridDTO> find(ConhecimentosNaoComissionaveisFilterDTO filter) {
		if ((filter.getExecutivo() == null) || (filter.getDtInicio() == null) || (filter.getDtFim() == null)) {
			throw new BusinessException(EXCEPTION_FILTRO_INSUFICIENTE);
		}

		Long idCliente = null;
		if (filter.getCliente() != null) {
			idCliente = filter.getCliente().getIdCliente();
		}
		
		List<Map<String, Object>> results = conhecimentoService.findConhecimentosNaoComissionaveis(filter.getExecutivo().getIdUsuario(),
			idCliente, filter.getDtInicio(), filter.getDtFim(), getTypedFlatMapWithPaginationInfo(filter));
				
		List<ConhecimentosNaoComissionaveisGridDTO> gridResults = new ArrayList<ConhecimentosNaoComissionaveisGridDTO>();
		for (Map<String, Object> conhecimentoNaoComissionavel : results) {
			gridResults.add(convertToGridDTO(conhecimentoNaoComissionavel));
		}
		return gridResults;
	}

	private ConhecimentosNaoComissionaveisGridDTO convertToGridDTO(Map<String, Object> object) {
		ConhecimentosNaoComissionaveisGridDTO conhecimentosNaoComissionaveis = new ConhecimentosNaoComissionaveisGridDTO();
		YearMonthDay dhEmissao = null;
		YearMonthDay dtPagamento = null;

		if (MapUtils.getString(object, "DHEMISSAO") != null) {
			dhEmissao = new YearMonthDay(MapUtils.getString(object, "DHEMISSAO"));
		}

		if (MapUtils.getString(object, "DTPAGAMENTO") != null) {
			dtPagamento = new YearMonthDay(MapUtils.getString(object, "DTPAGAMENTO"));
		}

		conhecimentosNaoComissionaveis.setId(MapUtils.getLong(object, "IDDOCTOSERVICO"));
		conhecimentosNaoComissionaveis.setNrDoctoServico(MapUtils.getLong(object, "NRDOCTOSERVICO"));
		conhecimentosNaoComissionaveis.setTpDocumento(MapUtils.getString(object, "TPDOCUMENTO"));
		conhecimentosNaoComissionaveis.setVlValorFreteLiquido(MapUtils.getDouble(object, "VLFRETELIQUIDO"));
		conhecimentosNaoComissionaveis.setDhEmissao(dhEmissao);
		conhecimentosNaoComissionaveis.setDtPagamento(dtPagamento);
		conhecimentosNaoComissionaveis.setTpFrete(MapUtils.getString(object, "TPFRETE"));
		conhecimentosNaoComissionaveis.setTpModal(MapUtils.getString(object, "TPMODAL"));
		conhecimentosNaoComissionaveis.setCnpjRemetente(MapUtils.getString(object, "CNPJREMETENTE"));
		conhecimentosNaoComissionaveis.setNmRemetente(MapUtils.getString(object, "NMREMETENTE"));
		conhecimentosNaoComissionaveis.setCnpjDestinatario(MapUtils.getString(object, "CNPJDESTINATARIO"));
		conhecimentosNaoComissionaveis.setNmDestinatario(MapUtils.getString(object, "NMDESTINATARIO"));
		conhecimentosNaoComissionaveis.setCnpjResponsavel(MapUtils.getString(object, "CNPJRESPONSAVEL"));
		conhecimentosNaoComissionaveis.setNmResponsavel(MapUtils.getString(object, "NMRESPONSAVEL"));
		conhecimentosNaoComissionaveis.setNmTerritorio(MapUtils.getString(object, "NMTERRITORIO"));
		conhecimentosNaoComissionaveis.setIdExecutivo(MapUtils.getLong(object, "IDEXECUTIVO"));

		if ("SIM".equals(MapUtils.getString(object, "NAOCOMISSIONAVEL"))) {
			conhecimentosNaoComissionaveis.setNaoComissionavel(true);
			conhecimentosNaoComissionaveis.setChecked(true);
		} else {
			conhecimentosNaoComissionaveis.setNaoComissionavel(false);
			conhecimentosNaoComissionaveis.setChecked(false);
		}

		return conhecimentosNaoComissionaveis;
	}

	@Override
	protected Long store(ConhecimentosNaoComissionaveisDTO bean) {
		return null;
	}

	@Override
	protected ConhecimentosNaoComissionaveisDTO findById(Long id) {
		return null;
	}

	@Override
	protected void removeById(Long id) {
	}

	@Override
	protected void removeByIds(List<Long> ids) {
	}

	@POST
	@Path("salvarNaoComissionaveis")
	public Response salvarNaoComissionaveis(ConhecimentosNaoComissionaveisDTO conhecimentosNaoComissionaveisDTO) {
		doctoServicoSemComissaoService.storeNaoComissionaveis(conhecimentosNaoComissionaveisDTO.getNaoComissionaveisList(),
				conhecimentosNaoComissionaveisDTO.getIdExecutivo(),
				conhecimentosNaoComissionaveisDTO.getDataCompetencia());
		doctoServicoSemComissaoService.removeNaoComissionaveis(conhecimentosNaoComissionaveisDTO.getComissionaveisList());
		return Response.ok().build();
	}

	@Override
	protected Integer count(ConhecimentosNaoComissionaveisFilterDTO filter) {
		if ((filter.getExecutivo() == null) || (filter.getDtInicio() == null) || (filter.getDtFim() == null)) {
			throw new BusinessException(EXCEPTION_FILTRO_INSUFICIENTE);
		}

		Long idCliente = null;
		if (filter.getCliente() != null) {
			idCliente = filter.getCliente().getIdCliente();
		}

		return conhecimentoService.findCountConhecimentosNaoComissionaveis(filter.getExecutivo().getIdUsuario(),
			idCliente, filter.getDtInicio(), filter.getDtFim());
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setDoctoServicoSemComissaoService(DoctoServicoSemComissaoService doctoServicoSemComissaoService) {
		this.doctoServicoSemComissaoService = doctoServicoSemComissaoService;
	}

}
