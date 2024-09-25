<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.campoComplementarService">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01001"/>
		<adsm:include key="LMS-01002"/>
		<adsm:include key="LMS-01003"/>
	</adsm:i18nLabels>
	<adsm:form
		action="/vendas/manterCamposComplementares"
		idProperty="idCampoComplementar"
		onDataLoadCallBack="myOnDataLoad">

		<adsm:textbox
			dataType="text"
			label="nome"
			maxLength="60"
			property="nmCampoComplementar"
			required="true"
			size="43"/>

		<adsm:textbox
			dataType="text"
			label="descricao"
			maxLength="60"
			property="dsCampoComplementar"
			required="true"
			size="43"/>

		<adsm:combobox
			domain="DM_TIPO_CAMPO"
			label="tipo"
			property="tpCampoComplementar"
			required="true"
			onchange="testaCampoTamanho(this)"/>

		<adsm:textbox
			dataType="text"
			label="formatacao"
			maxLength="20"
			property="dsFormatacao"
			size="23"/>

		<adsm:textbox
			dataType="integer"
			label="tamanho"
			maxLength="2"
			property="nrTamanho"
			size="5"
			minValue="1"/>

		<adsm:checkbox label="opcional" property="blOpcional"/>

		<adsm:combobox domain="DM_STATUS" label="situacao" property="tpSituacao" required="true"/>

		<adsm:buttonBar>
			<adsm:button
				caption="salvar"
				buttonType="storeButton"
				onclick="validarTipoCampoComplementar()"
				id="btnSalvar"
				disabled="false"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="infDocClienteMaskInstrucoes" width="100%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="infDocClienteMaskNumerico" width="30%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		<adsm:label key="infDocClienteMaskAlfanumerico" width="30%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		<adsm:label key="infDocClienteMaskDataHora" width="40%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
	</adsm:form>
	
</adsm:window>
<script>

	/**
	* Testa se o campo Tipo está setado como Data/Hora, caso esteja desabilita o campo Tamanho	
	* @param element Recebe o elemento que deve ser testado (campo Tipo Campo Complementar)	* 
	* @return 
	*/
	function testaCampoTamanho(element){
		var valorCampoTipo = element[element.selectedIndex].value;
		var campoTamanho = document.getElementById("nrTamanho");
		
		if( valorCampoTipo == 'D' ){
			campoTamanho.value = "";
			setDisabled(campoTamanho,true);
		} else {
			setDisabled(campoTamanho,false);		
		}				
	}
	
	function initWindow(){
		testaCampoTamanho(document.getElementById("tpCampoComplementar"));
	}
	
	/**
	* Valida de acordo com o campo tipo Campo Complementar se os campos 'Descrição' ou 'Tamanho' estão preenchidos
	* antes de salvar o registro novo.
	* @return Retorna verdadeiro caso os campos estão preenchidos corretamente para cada tipo de campo complementar (salva o registro e retorna verdadeiro).
	*/
	function validarTipoCampoComplementar(){		
		var valorCampoTipo = getElementValue("tpCampoComplementar");				
		var item = -1;

		switch(valorCampoTipo) {
		case 'A':
			item = verificaCamposPreenchidos('dsFormatacao','nrTamanho');
			if(item != -1) {
				alertI18nMessage("LMS-01001"); 
				if(item == 0) {
					document.getElementById("dsFormatacao").focus();
				} else {
					document.getElementById("nrTamanho").focus();
				}
				return false;
			}
			break;
		case 'N':
			item = verificaCamposPreenchidos('dsFormatacao','nrTamanho')
			if(item != -1) {
				alertI18nMessage("LMS-01002");
				if(item == 0) {
					document.getElementById("dsFormatacao").focus();
				} else {
					document.getElementById("nrTamanho").focus();
				}
				return false;
			}
			break;
		case 'D':
			item = verificaCamposPreenchidos('dsFormatacao'); 
			if(item != -1) {
				alertI18nMessage("LMS-01003");
				document.getElementById("dsFormatacao").focus();
				return false;
			}
			break;
		}

		storeButtonScript('lms.vendas.campoComplementarService.store', 'myStore', document.getElementById('form_idCampoComplementar'));

		return true;
	}

	/**
	* Verifica se campo1 ou campo2 estão preenchidos exclusivamente.
	* @param idCampo1 id do campo1
	* @param idCampo2 id do campo2 (opcional)
	* @return Verdadeiro se os campos estão preenchidos exclusivamente ou se foi passado apenas um campo e este está preenchido, 
	* ou falso caso contrário.
	*/
	function verificaCamposPreenchidos(idCampo1, idCampo2){
		var campo1 = getElementValue(idCampo1);
		var campo2;

		if(idCampo2) {
			campo2 = getElementValue(idCampo2);
		}

		if( campo2 != null && campo2 != undefined ) {
			if( trim(campo1) != "" && trim(campo2) != "" ) {
				return 0;
			} else {
				if( (trim(campo1) != "" && trim(campo2) == "") ) {
					return -1;
				} else if(trim(campo1) == "" && trim(campo2) != "") {
					return -1;
				} else {
					return 0;
				} 
			}
		}

		if( trim(campo1) != "" ) {
			return -1;
		} else {
			return 0;
		}

	}

	/**
	* Meu callback da busca dos dados. Utilizado para corrigir o problema de não lançar algum erro na tela se este acontecer.
	*/
	function myStore_cb(data,erro) {
		store_cb(data,erro);
		setFocusOnNewButton(document);
	}

	function myOnDataLoad_cb(data,erro) {
		onDataLoad_cb(data,erro);
		if(erro != undefined) {
			alert(erro);
		}
		testaCampoTamanho(document.getElementById("tpCampoComplementar"));
	}	
</script>