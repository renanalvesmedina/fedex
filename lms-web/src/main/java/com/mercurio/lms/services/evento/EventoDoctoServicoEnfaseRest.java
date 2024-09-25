package com.mercurio.lms.services.evento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import br.com.tntbrasil.integracao.domains.expedicao.DocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoServicoDMN.FilialDoumentoServico;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoServicoDMN.PessoaDoumentoServico;
import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalDMN;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
	
@Path("/enfase") 
public class EventoDoctoServicoEnfaseRest extends BaseRest{

	@InjectInJersey
	private DoctoServicoService doctoServicoService;
	
	@InjectInJersey
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;

	@GET
	@Path("/doctoservico/{idDoctoServico}/evento/{cdEvento}")
	public Response findByCdEvento(@PathParam("idDoctoServico") Long idDoctoServico, @PathParam("cdEvento") Integer cdEvento) {
		List<Map<String, Object>> dadosPodScanning = doctoServicoService.findDadosPodScanning(idDoctoServico);

		if (dadosPodScanning.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.ok(
			this.populateDocumentoServicoMNO(
				dadosPodScanning.get(0),
				this.getNotasFiscais(idDoctoServico),
				this.getIndicadorCancelado(cdEvento)
			)
		).build();
	}

	private DocumentoServicoDMN populateDocumentoServicoMNO(Map<String,Object> data, 
	List<NotaFiscalDMN> notasFiscais, String indicadorCancelado) {
		DocumentoServicoDMN documentoServicoDMN = new DocumentoServicoDMN();
		
		documentoServicoDMN.setDhEmissao((DateTime) data.get("dh_emissao"));
		documentoServicoDMN.setNrDoctoServico((Long) data.get("nr_docto_servico"));
		documentoServicoDMN.setChaveDoctoServico((String) data.get("nr_chave"));
		documentoServicoDMN.setIndicadorCancelado(indicadorCancelado);
		documentoServicoDMN.setNotasFiscaisList(notasFiscais);

		FilialDoumentoServico filial = documentoServicoDMN.new FilialDoumentoServico();
		filial.setCodigo((String) data.get("cd_filial"));
		filial.setCnpj((String) data.get("nr_identificacao"));
		documentoServicoDMN.setFilialOrigem(filial);

		PessoaDoumentoServico devedor = documentoServicoDMN.new PessoaDoumentoServico();
		devedor.setNome((String) data.get("nomeDevedor"));
		devedor.setCnpj((String) data.get("cnpjDevedor"));
		documentoServicoDMN.setDevedor(devedor);
		
		PessoaDoumentoServico remetente = documentoServicoDMN.new PessoaDoumentoServico();
		remetente.setNome((String) data.get("nomeRemetente"));
		documentoServicoDMN.setRemetente(remetente);

		PessoaDoumentoServico destinatario = documentoServicoDMN.new PessoaDoumentoServico();
		destinatario.setNome((String) data.get("nomeDestinatario"));
		documentoServicoDMN.setDestinatario(destinatario);

		return documentoServicoDMN;
	}

	private List<NotaFiscalDMN> getNotasFiscais(Long idDoctoServico) {
		List<NotaFiscalDMN> retorno = new ArrayList<NotaFiscalDMN>();
		List idsNotaFiscal = notaFiscalConhecimentoService.findByidDoctoServico(idDoctoServico);

		for (int i = 0; i < idsNotaFiscal.size(); i++) {
			NotaFiscalDMN notaFiscalDMN = new NotaFiscalDMN();
			notaFiscalDMN.setNrNotaFiscal(idsNotaFiscal.get(i).toString());
			retorno.add(notaFiscalDMN);
		}

		return retorno;
	}
	
	private String getIndicadorCancelado(Integer cdEvento) {
		switch(cdEvento) {
			case 12:
				return "S";

			case 6:
				return "N";

			default:
				return null;
		}
	}
}
