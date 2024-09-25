package com.mercurio.lms.carregamento.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;

public class ControleCargaHelper {

	public static List<String> createListByConteudoParametro(ConteudoParametroFilial conteudoParametroFilial) {
		if (conteudoParametroFilial != null && StringUtils.isNotBlank(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			return Arrays.asList(conteudoParametroFilial.getVlConteudoParametroFilial().split(";"));
		}  
		return Collections.emptyList();
	}

	public static String createKeyValidacaoParcelaColetaEntrega(Manifesto manifesto, TabelaColetaEntrega tabelaColetaEntrega, ParcelaTabelaCe parcelaTabelaCeLoaded) {
		String tpManifestoEntrega = getDomainValue(manifesto.getTpManifestoEntrega());
		String tpCalculo =  getDomainValue(tabelaColetaEntrega.getTpCalculo());
		String tpParcela = getDomainValue(parcelaTabelaCeLoaded.getTpParcela());
		return tpManifestoEntrega.concat(tpCalculo).concat(tpParcela);
	}
	
	public static String getDomainValue(DomainValue domainValue) {
		String result = "";
		if (domainValue != null) {
			result = domainValue.getValue();
		}
		return result;
	}

	public static String createKeyValidacaoParcelaColetaEntrega(String tpPreManifesto, TabelaColetaEntrega tabelaColetaEntrega, ParcelaTabelaCe parcelaTabelaCe) {
		String tpManifestoEntrega = tpPreManifesto;//manifesto.getTpManifestoEntrega()
		String tpCalculo =  getDomainValue(tabelaColetaEntrega.getTpCalculo());
		String tpParcela = getDomainValue(parcelaTabelaCe.getTpParcela());
		return tpManifestoEntrega.concat(tpCalculo).concat(tpParcela);
	}
	
}
