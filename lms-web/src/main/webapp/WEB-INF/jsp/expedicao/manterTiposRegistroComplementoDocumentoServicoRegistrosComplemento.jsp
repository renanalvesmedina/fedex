<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:i18nLabels>
		<adsm:include key="LMS-01004"/>
		<adsm:include key="LMS-40006"/>
		<adsm:include key="LMS-40007"/>
		<adsm:include key="LMS-40008"/>
		<adsm:include key="LMS-40009"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/manterTiposRegistroComplementoDocumentoServico"
		idProperty="idInformacaoDocServico" onDataLoadCallBack="myOnDataLoad"
		service="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction.findByIdInformacaoDocServico">
		<adsm:masterLink showSaveAll="true" idProperty="idTipoRegistroComplemento" >
			<adsm:masterLinkItem boxWidth="480" property="dsTipoRegistroComplemento"  label="tipoComplemento" itemWidth="80" />
		</adsm:masterLink>
		<adsm:textbox maxLength="60" dataType="text" size="43"
			property="dsCampo" label="campo"  required="true"/>
		<adsm:combobox property="tpCampo" label="tipo" required="true" domain="DM_TIPO_CAMPO" onchange="onSelectType();"/>
		<adsm:textbox maxLength="60" dataType="text" property="dsFormatacao" disabled="true" onchange="return validateFormat()" label="formatacao" size="43"/>
		<adsm:textbox label="tamanho" property="nrTamanho" maxLength="2" disabled="true" minValue="1" dataType="integer" size="5"/>
		<adsm:checkbox property="blOpcional" label="opcional" />
		<adsm:checkbox property="blImprimeConhecimento" label="imprimeCTO" />
		<adsm:checkbox property="blIndicadorNotaFiscal" label="indicadorNF" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton
				caption="salvarRegistroComplemento"
				service="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction.saveInformacaoDocServico"/>
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="tipoRegistroComplemento" idProperty="idInformacaoDocServico" 
		gridHeight="200" unique="true" detailFrameName="registrosComplemento" 
		autoSearch="false" showGotoBox="true" showPagging="true" rows="8" 
		service="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction.findPaginatedInformacaoDocServico"
		rowCountService="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction.getRowCountInformacaoDocServico">
		<adsm:gridColumn title="campo" property="dsCampo" width="25%" />
		<adsm:gridColumn title="tipo" property="tpCampo" isDomain="true" width="13%" />
		<adsm:gridColumn title="formatacao" property="dsFormatacao" width="20%" />
		<adsm:gridColumn title="opcional" property="blOpcional" renderMode="image-check" width="8%" />
		<adsm:gridColumn title="imprimeCTO" property="blImprimeConhecimento" renderMode="image-check" width="12%" />
		<adsm:gridColumn title="indicadorNF" property="blIndicadorNotaFiscal" renderMode="image-check" width="14%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="8%" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirRegistroComplemento"
				service="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction.removeByIdsInformacaoDocServico" />

		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript">

	function myOnDataLoad_cb(dados, erros, errMsg, eventObj){
		onDataLoad_cb(dados, erros, errMsg, eventObj);
		var tipo = getElementValue("tpCampo");
	    var tamanho = document.getElementById("nrTamanho");
  		if (tipo == 'D')
  			setDisabled(tamanho, true);
	  	else
  			setDisabled(tamanho, false);
		setDisabled(document.getElementById("dsFormatacao"), false);
	}

	function validateTab() {
		return validateTabScript(document.forms) && validateFormat(true);
	}
	
	
	function validateFormat(store){
		var tipo = getElementValue("tpCampo");
		var formatacao = getElementValue("dsFormatacao");
		var tamanho = getElementValue("nrTamanho");
		var validacao = false;
		var caracteres = "";
		var fields = new Array(2);
			fields[0] = formatacao;
			fields[1] = tamanho;
		
		if (tipo == "A") {
			if(store) {
				if(!xorFilled(fields)) {
					focus("LMS-40006");
					return false;
				}
			}
			caracteres = "A9?-/."
		}
		if (tipo == "N") {
			if(store) {
				if(!xorFilled(fields)) {
					focus("LMS-40007");
					return false;
				}
			}
			caracteres = "#0,."
		}
		if (tipo == "D" && formatacao == "") {
			if(store) {
				focus("LMS-40008");
				return false;
			}
			caracteres = "dMy/ Hm:"
		}
		for (j=0; j < formatacao.length; j++){
			validacao = false;
			for (i=0; i < caracteres.length;i++){
    		  if (caracteres.charAt(i)==formatacao.charAt(j))
        		  validacao = true;
    		}
    		if (validacao == false){
    			focus("LMS-40009");
    			return false;
    		}
		}
		return true;
	}

	function focus(message) {
		var format = document.getElementById("dsFormatacao");
		alertI18nMessage(message);
		format.select();
		setFocus(format);
	}

	function xorFilled(fields) {
		var flag = false;
		for(var i=0;i<fields.length;i++) {
			if(fields[i] != "") {
				if(flag) {
					return false;
				} else {
					flag = true;
				}
			}
		}
		return flag;
	}

	// Função que desabilita o campo tamanho quando o tipo selecionado é Data/Hora
	function onSelectType() {
		var selectedType = getElementValue("tpCampo");
		var tamanho = document.getElementById("nrTamanho");
		var formatacao = document.getElementById("dsFormatacao");
		if (selectedType == '' || selectedType == undefined) {
			setDisabled(tamanho,true);
			setDisabled(formatacao,true);
		} else if (selectedType == 'D') {
			setDisabled(formatacao,false);
			setDisabled(tamanho,true);
		} else {
			setDisabled(formatacao,false);
			setDisabled(tamanho,false);
		}
		resetValue(tamanho);		
		resetValue(formatacao);
	}

	function initWindow(eventObj) {
		var event = eventObj.name;
		if (event == "tab_click" || event == "newItemButton_click"
				|| event == "storeItemButton" || event == "removeButton_grid") {
			onSelectType();
		} 
 	}
 	

</script>
