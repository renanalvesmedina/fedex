package com.mercurio.lms.rest.expedicao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.rest.expedicao.dto.NaturezaProdutoChosenDTO;

/**
 * LMS-6885 - REST para diretiva "chosen" de {@link NaturezaProduto}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
@Path("expedicao/naturezaProduto")
public class NaturezaProdutoChosenRest extends BaseRest {

	@InjectInJersey
	private NaturezaProdutoService naturezaProdutoService;

	@GET
	@Path("findChosen")
	public Response findChosen() {
		return Response.ok(convertNaturezaProdutoChosenDTO(naturezaProdutoService.findChosen())).build();
	}

	private List<NaturezaProdutoChosenDTO> convertNaturezaProdutoChosenDTO(List<NaturezaProduto> list) {
		List<NaturezaProdutoChosenDTO> dtos = new ArrayList<NaturezaProdutoChosenDTO>();
		for (NaturezaProduto bean : list) {
			dtos.add(buildNaturezaProdutoChosenDTO(bean));
		}
		return dtos;
	}

	private NaturezaProdutoChosenDTO buildNaturezaProdutoChosenDTO(NaturezaProduto bean) {
		NaturezaProdutoChosenDTO dto = new NaturezaProdutoChosenDTO();
		dto.setId(bean.getIdNaturezaProduto());
		dto.setDsNaturezaProduto(bean.getDsNaturezaProduto().getValue());
		return dto;
	}

}
