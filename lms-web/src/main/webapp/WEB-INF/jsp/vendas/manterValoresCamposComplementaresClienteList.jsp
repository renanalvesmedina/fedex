<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.valorCampoComplementarService">
	<adsm:form action="/vendas/manterValoresCamposComplementaresCliente" >

		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox
			property="cliente.pessoa.nrIdentificacao"
			dataType="text"
			size="20"
			width="16%"
			maxLength="20"
			disabled="true"
			serializable="false"
			label="cliente">
			<adsm:textbox
				property="cliente.pessoa.nmPessoa"
				dataType="text"
				size="30"
				disabled="true"
				serializable="false" 
				width="60%"
				maxLength="60"/>
		</adsm:textbox>

		<adsm:combobox
			property="campoComplementar.idCampoComplementar"
			label="campo"
			boxWidth="250"
			optionLabelProperty="dsCampoComplementar"
			optionProperty="idCampoComplementar"
			onchange="return campoComplementarOnChange({e:this});"
			service="lms.vendas.campoComplementarService.find"/>

		<adsm:textbox
			property="vlValor"
			label="valor"
			dataType="text"
			size="30"
			maxLength="60"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="valores"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		property="valores"
		idProperty="idValorCampoComplementar"
		gridHeight="200"
		unique="true"
		defaultOrder="campoComplementar_.dsCampoComplementar"
		rows="13">
		<adsm:gridColumn title="campo" property="campoComplementar.dsCampoComplementar" width="30%"/>
		<adsm:gridColumn title="valor" property="vlValor" width="70%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	function initWindow(evenObj) {
		if (evenObj.name != "tab_click") {
			resetFieldValor();
		}
	}

	/**
	 * Com os valores do Campo Complementar, recebido como parametro, aplica as propriedades para a 
	 * formata��o do campo Valor (vlValor), apagando o valor atual (se existir).
	 * @param dsFormatacao
	 * @param nrTamanho
	 * @param tpCampoComplementar
	 */
	function aplicaPropriedadesValor(dsFormatacao, nrTamanho, tpCampoComplementar){
		//Apaga valor para nova formata��o
		fieldValor = getElement("vlValor");
		resetValue(fieldValor);

		//Define "DataType"
		fieldValor.dataType = "text";
		if (tpCampoComplementar == "N"){
			if (dsFormatacao != undefined && dsFormatacao.indexOf('.') > 0){
				fieldValor.dataType = "decimal";
			}else{
				fieldValor.dataType = "integer";
			}
		}else if (tpCampoComplementar == "A"){
			fieldValor.dataType = "text";
		}else if (tpCampoComplementar == "D"){
			//Para data/hora apenas m�scara
			if (dsFormatacao != undefined){
				fieldValor.dataType = "jtdatetimezone";
			}
		}

		//Define Length
		nrTamanho = (nrTamanho == undefined && dsFormatacao != undefined) ? dsFormatacao.length : nrTamanho;
		fieldValor.maxChars = nrTamanho;

		//Define Mascara
		fieldValor.mask = "";
		if (dsFormatacao != undefined) {
			fieldValor.mask = dsFormatacao;
		}else if (fieldValor.dataType != "text") {
			fieldValor.mask = '#'.repeat(stringToNumber(nrTamanho));
		}
		im_init(fieldValor)

		setFocus(fieldValor);
	}

	/**
	 * On Change do campo Campo Complementar
	 *@param eThis elemento
	 */
	function campoComplementarOnChange(eThis){
		comboboxChange(eThis);

		var idCampoComplementar = getElementValue("campoComplementar.idCampoComplementar");
		if (idCampoComplementar != "") {
			var data = new Array();
			setNestedBeanPropertyValue(data, "idCampoComplementar", getElementValue("campoComplementar.idCampoComplementar") );
			var sdo = createServiceDataObject("lms.vendas.campoComplementarService.findById",
				"campoComplementar",data);
	        xmit({serviceDataObjects:[sdo]});
        } else {
        	resetFieldValor();
        }
		return false;
	}

	function campoComplementar_cb(data,erro){
		aplicaPropriedadesValor(
			getNestedBeanPropertyValue(data, "dsFormatacao"),
			getNestedBeanPropertyValue(data, "nrTamanho"), 
			getNestedBeanPropertyValue(data, "tpCampoComplementar.value"));
	}

	function resetFieldValor() {
		var fieldValor = getElement("vlValor");
       	fieldValor.mask = "";
       	resetValue(fieldValor);
       	fieldValor.dataType = "text";
       	fieldValor.maxChars = 60;
	}
</script>