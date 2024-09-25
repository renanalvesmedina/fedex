<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.contratacaoveiculos.manterBloqueiosMotoristaProprietarioAction"
		onPageLoad="pageLoadCustom" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/contratacaoVeiculos/popupBloqueios" idProperty="idBloqueioMotoristaProp">
	
		<adsm:hidden property="meioTransporte.idMeioTransporte" serializable="true"/>
		<adsm:hidden property="proprietario.idProprietario" serializable="true"/>		
		<adsm:hidden property="motorista.idMotorista" serializable="true"/>
	
		<adsm:textarea maxLength="500" property="obBloqueioMotoristaProp" required="true" label="motivo" width="82%" rows="10" columns="60"/>
		<adsm:range label="vigencia" width="82%" >
			<adsm:textbox property="dhVigenciaInicial" required="true" dataType="JTDateTimeZone"/>
			<adsm:hidden property="dhVigenciaInicialDetalhe" />
			<adsm:textbox property="dhVigenciaFinal" dataType="JTDateTimeZone"/>
			<adsm:hidden property="dhVigenciaFinalDetalhe" />
		</adsm:range>
		<adsm:buttonBar freeLayout="false" >
			<adsm:button caption="salvar" id="botaoSalvar"
					onclick="storeButtonScript('lms.contratacaoveiculos.manterBloqueiosMotoristaProprietarioAction.storeMap', 'store', this.form);" 
					disabled="false" />			
			<adsm:button caption="fechar" id="botaoFechar" onclick="self.close();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 
<script>
	function store_cb(dados, erros) {
		if (erros == undefined) {
			if (dialogArguments.atualizaLocalizacao)
				dialogArguments.atualizaLocalizacao();
			window.close();
		} else
			alert(erros);
	}

	function pageLoadCustom() {
		if (dialogArguments.document.forms[0].elements["idMotorista"])
			document.forms[0].elements["motorista.idMotorista"].value = dialogArguments.document.forms[0].elements["idMotorista"].value;
			
		else if (dialogArguments.document.forms[0].elements["idProprietario"])
			document.forms[0].elements["proprietario.idProprietario"].value = dialogArguments.document.forms[0].elements["idProprietario"].value;
				
		else if (dialogArguments.document.forms[0].elements["idMeioTransporte"])
			document.forms[0].elements["meioTransporte.idMeioTransporte"].value = dialogArguments.document.forms[0].elements["idMeioTransporte"].value;
		onPageLoad();
		
		setDisabled("botaoSalvar",false);
		setDisabled("botaoFechar",false);	
	}

	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		loadDadosBloqueio();
	}
		
	function loadDadosBloqueio(){
		var data = new Array();
		var opName;
		
		if (getElementValue("motorista.idMotorista") != ''){
			setNestedBeanPropertyValue(data, "idMotorista", getElementValue("motorista.idMotorista"));
			opName = "findDadosBloqueioMotorista";
		}
			
		if (getElementValue("proprietario.idProprietario") != '') {
			setNestedBeanPropertyValue(data, "idProprietario", getElementValue("proprietario.idProprietario"));
			opName = "findDadosBloqueioProprietario";
		}
		
		if (getElementValue("meioTransporte.idMeioTransporte") != ''){
			setNestedBeanPropertyValue(data, "idMeioTransporte", getElementValue("meioTransporte.idMeioTransporte"));
			opName = "findDadosBloqueioMeioTransporte";
		}
		
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterBloqueiosMotoristaProprietarioAction." + opName,
				"preencheDadosBloqueio",data);
				 
		xmit({serviceDataObjects:[sdo]});
	} 
	
	function preencheDadosBloqueio_cb(data){
		var blBloqueio = getNestedBeanPropertyValue(data, "blBloqueio");
	
		var id = getNestedBeanPropertyValue(data, "idBloqueioMotoristaProp");
		setElementValue("idBloqueioMotoristaProp", id);
					
		var obs = getNestedBeanPropertyValue(data, "obBloqueioMotoristaProp");
		setElementValue("obBloqueioMotoristaProp", obs);
			
		var dhInicio = getNestedBeanPropertyValue(data, "dhVigenciaInicial");
		if (dhInicio != undefined) {
			setElementValue("dhVigenciaInicial", setFormat("dhVigenciaInicial", dhInicio));
			document.getElementById("dhVigenciaInicialDetalhe").value = dhInicio;
		}
		
		var dhFim = getNestedBeanPropertyValue(data, "dhVigenciaFinal");
		if (dhFim != undefined) {
			setElementValue("dhVigenciaFinal", setFormat("dhVigenciaFinal", dhFim));
			document.getElementById("dhVigenciaFinalDetalhe").value = dhFim;
		}
		
		if (blBloqueio == "S") {
			setDisabled("dhVigenciaInicial", true);	
			setDisabled("obBloqueioMotoristaProp", true);
			document.getElementById("dhVigenciaInicial").required = "false";
			document.getElementById("obBloqueioMotoristaProp").required = "false";
			document.getElementById("dhVigenciaFinal").required = "true";
		}
		setFocusOnFirstFocusableField(document);
	}
</script>
