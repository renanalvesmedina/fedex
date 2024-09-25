package com.mercurio.lms.expedicao.util.nfe.converter;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.municipios.model.Municipio;

public class RpsNseConverter extends RpsConverter {
	
	private final NotaFiscalServico notaFiscalServico;

	public RpsNseConverter(String versaoNfe, String naturezaOperacaoNfe,
			String regimeTributarioNfe, String serieNfe, String codigoCnae,
			String codigoTribMunicipio, String servicoNte, String bairroPadrao,
			NotaFiscalServico notaFiscalServico, Long nrFiscalRps, String tipoDocumento,
			String munIncidenciaOutro, String codMunicipioOutros, Boolean nfseGeraVlrIss, Boolean nfseIssPrestOutroLocal, Boolean nfseRetIss,Boolean reenviarDataAtual) {
		super(versaoNfe, naturezaOperacaoNfe, regimeTributarioNfe, serieNfe,
				codigoCnae, codigoTribMunicipio, servicoNte, bairroPadrao, nrFiscalRps, tipoDocumento,
				munIncidenciaOutro, codMunicipioOutros, nfseGeraVlrIss, nfseIssPrestOutroLocal,nfseRetIss,reenviarDataAtual);
		this.notaFiscalServico = notaFiscalServico;
	}

	@Override
	protected DoctoServico getDoctoServico() {
		return notaFiscalServico;
	}
	
	@Override
	protected List<ImpostoServico> getImpostoServicos() {
		return notaFiscalServico.getImpostoServicos();
	}
	
	@Override
	protected List<NotaFiscalConhecimento> getNotaFiscalConhecimentos() {
		return new ArrayList<NotaFiscalConhecimento>();
	}
	
	@Override
	protected Municipio getMunicipioPrestacaoServico() {
		return notaFiscalServico.getMunicipio();
	}

}
