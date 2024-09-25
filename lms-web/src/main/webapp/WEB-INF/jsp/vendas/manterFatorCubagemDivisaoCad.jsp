<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterFatorCubagemDivisaoAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00006"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/vendas/manterFatorCubagemDivisao" idProperty="idFatorCubagemDivisao" onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="divisaoCliente.idDivisaoCliente" />
		<adsm:hidden property="divisaoCliente.cliente.idCliente"/>

		<adsm:complement
			label="cliente"
			width="70%"
			labelWidth="19%"
			separator="branco"
			>
			<adsm:textbox 
				dataType="text"
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				size="20" 
				maxLength="20"
				disabled="true" 
				serializable="false"
			/>
			<adsm:textbox 
				dataType="text" 
				maxLength="50" 
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				size="47" 
				disabled="true"
				serializable="false"
			/>
		</adsm:complement>

		<adsm:textbox 
        	dataType="integer" 
        	property="divisaoCliente.cdDivisaoCliente" 
        	label="codigo" 
        	disabled="true"
			labelWidth="19%"
			width="28%"
        	maxLength="10" 
        	size="10"
        />
        
		<adsm:textbox 
			dataType="text" 
			property="divisaoCliente.dsDivisaoCliente" 
			label="divisao" 
			disabled="true"
			maxLength="60" 
			labelWidth="10%"
			size="40"
		/>
		
		<adsm:textbox 
			dataType="decimal" 
			property="nrFatorCubagemReal" 
			label="fatorCubagemReal" 
			labelWidth="19%"
			size="10"
			maxValue="999999.99"
			minValue="0.01"
			width="28%"
			required="true"
			mask="###,##0.00"
		/>
		
			<adsm:range label="vigencia" labelWidth="10%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" onchange="return dtVigenciaInicial_change(this)"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" disabled="true"/> 
		</adsm:range>

		<adsm:buttonBar>

			<adsm:storeButton id="storeButton" />
 			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton" />
			
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script>

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);

		if (getElement("dtVigenciaFinal").value == "" || getElement("dtVigenciaFinal").value == "4000-01-01") {
			setElementValue("dtVigenciaFinal", "");
			enableFields();
		} else {
			
			var dtVigenciaFinal = stringToDate(getElement("dtVigenciaFinal").value, getElement("dtVigenciaFinal").mask);
			var dtVigenciaInicial = new Date();
			var time1 = LZ(dtVigenciaFinal.getYear())+LZ(dtVigenciaFinal.getMonth())+LZ(dtVigenciaFinal.getDate());
			var time2 = LZ(dtVigenciaInicial.getFullYear())+LZ(dtVigenciaInicial.getMonth())+LZ(dtVigenciaInicial.getDate());

			if (time1 < time2) {
				disableFields();
				disableButtons();
			} else {
				enableFields();
			}
			
			
			
		}
		 
		validaPermissao();

	}
	 
	 function enableFields() {
			setDisabled("nrFatorCubagemReal", false);
			setDisabled("dtVigenciaInicial", false);
	 }
	 
	 function disableFields() {
			setDisabled("nrFatorCubagemReal", true);
			setDisabled("dtVigenciaInicial", true);
	 }
	 
	 function disableButtons() {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
	 }

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validaPermissao(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			disableButtons();
		}
	}
	
	function initWindow(obj){
		if (obj.name == "tab_click" || obj.name == "cleanButton_click"){
			enableFields();

		}
		if (obj.name == "tab_click" || obj.name == "gridRow_click"){
			validaPermissao();
		}
	}
	
	function dtVigenciaInicial_change(obj) {
		if (obj.value != "") {
			var dtVigenciaInicial = stringToDate(obj.value, obj.mask);
			if (dtVigenciaInicial != "0") {
				var currentDate = new Date();
				var time1 = LZ(dtVigenciaInicial.getYear())+LZ(dtVigenciaInicial.getMonth())+LZ(dtVigenciaInicial.getDate());
				var time2 = LZ(currentDate.getFullYear())+LZ(currentDate.getMonth())+LZ(currentDate.getDate());
		
				if (time1 < time2) {
					alertI18nMessage("LMS-00006");
					obj.value = "";
					return false;
				}
			}
		}
		return true;
	}

</script>