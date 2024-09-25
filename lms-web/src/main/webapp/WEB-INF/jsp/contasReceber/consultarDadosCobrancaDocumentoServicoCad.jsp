<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction">

	<adsm:form action="/contasReceber/consultarDadosCobrancaDocumentoServico" idProperty="idDoctoServico" onDataLoadCallBack="myDataLoadCallBack">
	
		<adsm:hidden property="doctoServico" serializable="false"/>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="nmPessoa" serializable="false"/>
		
		<adsm:hidden property="tpDocumentoServicoValue" serializable="false"/>
		
		<adsm:textbox 
					 dataType="text"
					 property="tpDocumentoServico" 
					 label="documentoServico" 
					 disabled="true"
					 size="4"
					 labelWidth="20%" 
					 width="6%">
					 
					<adsm:textbox 
								 dataType="text" 
								 disabled="true"
								 size="3"
								 property="filial" 
								 width="5%"/>
								 
					<adsm:textbox 
								 dataType="integer" 
								 disabled="true"
								 property="nrDoctoServico" 
								 size="12" 
								 maxLength="8" 
								 mask="00000000"
								 width="19%"/>
								 
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="numeroNfe" label="docEletronicoNumeroNfe" disabled="true" labelWidth="25%" width="25%" style="text-align:right"/>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhEmissao" label="emissao" disabled="true" labelWidth="20%" width="30%"/>

		<adsm:textbox dataType="text" property="tpSituacao" label="situacaoDocumento" disabled="true" labelWidth="25%" width="25%"/>

		<adsm:textbox dataType="currency" property="vlTotalDocServico" label="valorDocumento" disabled="true" labelWidth="20%" width="30%"/>
		
		<adsm:textbox dataType="text" property="siglaSimbolo" label="moeda" disabled="true" labelWidth="25%" width="25%"/>
		
		<adsm:textbox dataType="currency" property="vlIcmsSubstituicaoTributaria" label="valorRetencaoST" disabled="true" labelWidth="20%" width="30%"/>
		
		<adsm:textbox dataType="text" property="tpFrete" label="tipoFrete" disabled="true" labelWidth="25%" width="25%"/>
		
		<adsm:textbox dataType="currency" property="vlDevido" label="valorDevido" disabled="true" labelWidth="20%" width="30%"/>
		
    </adsm:form>

	<adsm:buttonBar/>	
</adsm:window>

<script>

function myDataLoadCallBack_cb(data, erro, o){
	
	onDataLoad_cb(data); 
	
	if (erro != null){
		alert(erro);
	}else{
		var doctoServico = data.tpDocumentoServico + " " + data.filial + " " + document.getElementById("nrDoctoServico").value;
		setElementValue("doctoServico", doctoServico);
	}	
}

/**
  * Monta os parâmetros para serem passados na url
  */
function buildLinkPropertiesQueryString_consultaGeral() {

	var qs = "";
	
	if( getElementValue("tpDocumentoServico") == "NFS" ){
	
		qs += "&filial.sgFilial=" + document.getElementById("filial").value;
		qs += "&filial.idFilial=" + document.getElementById("idFilial").value;
		qs += "&nrNotaFiscalServico=" + document.getElementById("nrDoctoServico").value;
		qs += "&filial.pessoa.nmFantasia=" + document.getElementById("nmPessoa").value;
		
	}else{
	
		qs += "&doctoServico.idDoctoServico=" + document.getElementById("idDoctoServico").value;
		qs += "&doctoServico.filialByIdFilialOrigem.sgFilial=" + document.getElementById("filial").value;
		qs += "&doctoServico.nrDoctoServico=" + document.getElementById("nrDoctoServico").value;
		qs += "&doctoServico.tpDocumentoServico=" + document.getElementById("tpDocumentoServicoValue").value;
		
	}
	
	return qs;
}

</script>