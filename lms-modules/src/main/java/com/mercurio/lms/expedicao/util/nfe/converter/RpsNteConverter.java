package com.mercurio.lms.expedicao.util.nfe.converter;

import java.util.List;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.municipios.model.Municipio;

public class RpsNteConverter extends RpsConverter {

	private final Conhecimento conhecimento;

	public RpsNteConverter(String versaoNfe, String naturezaOperacaoNfe,
			String regimeTributarioNfe, String serieNfe, String codigoCnae,
			String codigoTribMunicipio, String servicoNte, String bairroPadrao,
			Conhecimento conhecimento, Long nrFiscalRps, String tipoDocumento,
			String munIncidenciaOutro, String codMunicipioOutros, Boolean nfseGeraVlrIss, Boolean nfseIssPrestOutroLocal, Boolean nfseRetIss,Boolean reenviarDataAtual) {
		super(versaoNfe, naturezaOperacaoNfe, regimeTributarioNfe, serieNfe,
				codigoCnae, codigoTribMunicipio, servicoNte, bairroPadrao,nrFiscalRps,tipoDocumento,
				munIncidenciaOutro, codMunicipioOutros, nfseGeraVlrIss, nfseIssPrestOutroLocal,nfseRetIss, reenviarDataAtual);
		this.conhecimento = conhecimento;
	}

	@Override
	protected DoctoServico getDoctoServico() {
		return conhecimento;
	}
	
	@Override
	protected List<ImpostoServico> getImpostoServicos() {
		return conhecimento.getImpostoServicos();
	}
	
	@Override
	protected List<NotaFiscalConhecimento> getNotaFiscalConhecimentos() {
		return conhecimento.getNotaFiscalConhecimentos();
	}
	
	@Override
	protected Municipio getMunicipioPrestacaoServico() {
		return conhecimento.getMunicipioByIdMunicipioColeta();
	}
	
}
