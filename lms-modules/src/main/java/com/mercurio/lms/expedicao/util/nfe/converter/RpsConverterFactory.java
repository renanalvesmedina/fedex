package com.mercurio.lms.expedicao.util.nfe.converter;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;

public class RpsConverterFactory {
	
	public static RpsConverter getInstance(String versaoNfe, String naturezaOperacaoNfe,
			String regimeTributarioNfe, String serieNfe, String codigoCnae,
			String codigoTribMunicipio,String servicoNte, String bairroPadrao, DoctoServico doctoServico, Long nrFiscalRps, String tipoDocumento,
			String munIncidenciaOutro, String codMunicipioOutros, Boolean nfseGeraVlrIss, Boolean nfseIssPrestOutroLocal, Boolean nfseRetEspIss,Boolean reenviarDataAtual) {
		if (doctoServico instanceof NotaFiscalServico) {
			return new RpsNseConverter(versaoNfe, naturezaOperacaoNfe, regimeTributarioNfe, serieNfe, codigoCnae, codigoTribMunicipio, servicoNte, bairroPadrao, (NotaFiscalServico)doctoServico,nrFiscalRps, tipoDocumento,
					munIncidenciaOutro, codMunicipioOutros, nfseGeraVlrIss, nfseIssPrestOutroLocal,nfseRetEspIss, reenviarDataAtual);
		} else if (doctoServico instanceof Conhecimento) {
			return new RpsNteConverter(versaoNfe, naturezaOperacaoNfe, regimeTributarioNfe, serieNfe, codigoCnae, codigoTribMunicipio, servicoNte, bairroPadrao, (Conhecimento)doctoServico,nrFiscalRps, tipoDocumento,
					munIncidenciaOutro, codMunicipioOutros, nfseGeraVlrIss, nfseIssPrestOutroLocal,nfseRetEspIss, reenviarDataAtual);
		}
		return null;
			
	}

}
