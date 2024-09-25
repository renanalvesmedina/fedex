package com.mercurio.lms.rest.contasareceber.retornodebanco;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.RetornoBanco;
import com.mercurio.lms.contasreceber.model.service.RetornoBancoService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.contasareceber.retornodebanco.dto.RetornoDeBancoDTO;
import com.mercurio.lms.rest.contasareceber.retornodebanco.dto.RetornoDeBancoFilterDTO;
 
@Path("/contasareceber/retornoDeBanco") 
public class RetornoDeBancoRest extends LmsBaseCrudReportRest<RetornoDeBancoDTO, RetornoDeBancoDTO, RetornoDeBancoFilterDTO> { 

	@InjectInJersey	
	RetornoBancoService retornoBancoService; 

	@Override 
	protected List<Map<String, String>> getColumns() { 
		List<Map<String, String>> map = new ArrayList<Map<String,String>>();
		map.add(createColumn("idRetornoBanco"));
		map.add(createColumn("nrBoletoMovimento"));
		map.add(createColumn("nrBancoMovimento"));
		map.add(createColumn("dtMovimento"));
		map.add(createColumn("dsRetorno"));
		map.add(createColumn("nrOcorrencia"));
		map.add(createColumn("dsOcorrencia"));
		map.add(createColumn("motivoRejeicao"));
		map.add(createColumn("dsMotivo"));
		map.add(createColumn("vlTotalBoletoRB"));
		map.add(createColumn("vlDesconto"));
		map.add(createColumn("vlAbatimento"));
		map.add(createColumn("vlJuros"));
		map.add(createColumn("dhInclusao"));
		map.add(createColumn("idBoleto"));
		map.add(createColumn("nrBoleto"));
		map.add(createColumn("dsCedenteBoleto"));
		map.add(createColumn("situacaoBoleto"));
		map.add(createColumn("dtEmissaoBoleto"));
		map.add(createColumn("dtVencimentoBoleto"));
		map.add(createColumn("dtVencimentoAprovacao"));
		map.add(createColumn("vlTotalBoleto"));
		map.add(createColumn("vlDescontoBoleto"));
		map.add(createColumn("vlJuroDiario"));
		map.add(createColumn("idFatura"));
		map.add(createColumn("sgFilial"));
		map.add(createColumn("nrFatura"));
		map.add(createColumn("cnpj"));
		map.add(createColumn("razaoSocial"));
		map.add(createColumn("filialCobCliente"));
		map.add(createColumn("tpPessoa"));
		map.add(createColumn("situacaoFatura"));
		map.add(createColumn("situacaoAprovacao"));
		map.add(createColumn("dtEmissao"));
		map.add(createColumn("dtVencimento"));
		map.add(createColumn("qtDocumentos"));
		map.add(createColumn("vlTotalFatura"));
		map.add(createColumn("vlTotalDescontos"));
		map.add(createColumn("vlTotalJuros"));
		map.add(createColumn("vlRecebidoParcial"));
		map.add(createColumn("valorSaldo"));
		map.add(createColumn("dtUltimoRecebimentoParcial"));
		map.add(createColumn("observacoes"));

		return map; 
	} 

	private Map<String, String> createColumn(String value, String title) {
		Map<String, String> coluna1 = new HashMap<String, String>();
		
		coluna1.put("title", getLabel(title));
		coluna1.put("column", value);
		
		return coluna1;
	}

	private Map<String, String> createColumn(String string) {
		return createColumn(string, string);
	}

 
	@Override 
	protected List<Map<String, Object>> findDataForReport( 
			RetornoDeBancoFilterDTO filter) { 
		
		return retornoBancoService.findRetornoBancoReport(convertFilterToMap(filter));
	} 
 
	@Override 
	protected RetornoDeBancoDTO findById(Long id) { 
		return null; 
	} 
 
	@Override 
	protected Long store(RetornoDeBancoDTO bean) { 
		return null; 
	} 
 
	@Override 
	protected void removeById(Long id) { 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
	} 
 
	@Override 
	protected List<RetornoDeBancoDTO> find( 
			RetornoDeBancoFilterDTO filter) {
		
		return convertEntityToDto(retornoBancoService.find(convertFilterToMap(filter)));
	} 
 
	private List<RetornoDeBancoDTO> convertEntityToDto(List<RetornoBanco> find) {
		List<RetornoDeBancoDTO> result = new ArrayList<RetornoDeBancoDTO>();
		
		for(RetornoBanco item : find){
			result.add(convertEntityToDtoItem(item));
		}
		
		return result;
	}

	private RetornoDeBancoDTO convertEntityToDtoItem(RetornoBanco item) {
		RetornoDeBancoDTO result = new RetornoDeBancoDTO();
		
		result.setNumeroBoleto( item.getNrBoleto());
		result.setNumeroBanco(item.getNrBanco());
		result.setDataDoMovimento(item.getDtMovimento());
		result.setMensagemRetorno(item.getDsRetornoBanco());
		result.setOcorrencia(item.getNrOcorrencia());
		result.setDescricaoOcorrencia(item.getDescricaoOcorrencia());
		result.setMotivo(item.getNrMotivoRejeicao());
		result.setDescricaoMotivo(item.getDescricaoMotivo());
		result.setValorTotal(item.getVlTotal());
		result.setValorDesconto(item.getVlDesconto());
		result.setValorAbatimento(item.getVlAbatimento());
		result.setValorJuros(item.getVlJuros());
		result.setDataHoraInclusao(item.getDhInclusao());
		
		return result;
	}

	private Map<String, Object> convertFilterToMap(RetornoDeBancoFilterDTO filter) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("DtEmissaoBoletoFinal",filter.getDtEmissaoBoletoFinal());
		map.put("DtEmissaoBoletoInicial",filter.getDtEmissaoBoletoInicial());
		map.put("DtEmissaoFaturaFinal",filter.getDtEmissaoFaturaFinal());
		map.put("DtEmissaoFaturaInicial",filter.getDtEmissaoFaturaInicial());
		map.put("DtMovimentoFinal",filter.getDtMovimentoFinal());
		map.put("DtMovimentoInicial",filter.getDtMovimentoInicial());
		map.put("DtVencimentoFinal",filter.getDtVencimentoFinal());
		map.put("DtVencimentoInicial",filter.getDtVencimentoInicial());
		map.put("IdFilial",(filter.getFilial()!=null)?filter.getFilial().getIdFilial():null);

		map.put("SomenteLiquidacao",(filter.getSomenteLiquidacao()!=null)?filter.getSomenteLiquidacao().getValue():null);
		map.put("SomenteNaoCadastrado",(filter.getSomenteNaoCadastrado()!=null)?filter.getSomenteNaoCadastrado().getValue():null);
		
		map.put("NrBanco",(filter.getBanco()!=null)?Long.valueOf(filter.getBanco().getNrBanco()):null);
		
		map.put("TpSituacaoBoleto",(filter.getTpSituacaoBoleto()!=null)?filter.getTpSituacaoBoleto().getValue():null);
		map.put("TpSituacaoFatura",(filter.getTpSituacaoFatura()!=null)?filter.getTpSituacaoFatura().getValue():null);
		
		return map;
	}

	@Override 
	protected Integer count(RetornoDeBancoFilterDTO filter) { 

		return retornoBancoService.findCount(convertFilterToMap(filter));
	} 
 
} 
