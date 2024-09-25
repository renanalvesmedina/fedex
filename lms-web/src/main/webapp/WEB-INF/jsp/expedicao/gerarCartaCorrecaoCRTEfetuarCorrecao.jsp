<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.gerarCartaCorrecaoCRTAction">
	<adsm:i18nLabels>
		<adsm:include key="pesoBruto"/>
		<adsm:include key="pesoNeto"/>
	</adsm:i18nLabels>
	<adsm:form action="/expedicao/gerarCartaCorrecaoCRT" idProperty="idCartaCorrecao" onDataLoadCallBack="myDataLoad">
		<adsm:lookup 
			service="lms.expedicao.gerarCartaCorrecaoCRTAction.findFilial" 
			action="/municipios/manterFiliais" 
			property="filialByIdFilialOrigem" 
			idProperty="idFilial" 
			serializable="false" 
			criteriaProperty="sgFilial" 
			label="filial" 	
			dataType="text" size="5" maxLength="3" width="9%" labelWidth="18%" required="true">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
	     	<adsm:textbox dataType="text" 
        		property="filialByIdFilialOrigem.pessoa.nmFantasia" 
	        	width="34%" size="30" serializable="false" 
    	    	disabled="true"/>
        </adsm:lookup>

		<adsm:textbox dataType="JTDate" picker="false" property="dtEmissao" 
			label="data" labelWidth="15%" width="24%" disabled="true"/>

		<adsm:complement label="numeroCRT" labelWidth="18%" required="true">
			<adsm:textbox 
				dataType="text" size="1"
				maxLength="2" width="82%"
				onchange="return sgPaisChange(this);"
				property="ctoInternacional.sgPais">
				<adsm:lookup dataType="integer" 
					property="ctoInternacional" 
					idProperty="idDoctoServico"
					criteriaProperty="nrCrt" 
					service="lms.expedicao.gerarCartaCorrecaoCRTAction.findCtoInternacionalDetalhe"
					action="/expedicao/manterCRT" 
					onDataLoadCallBack="ctoInternacional"
					onchange="return nrCrtOnChange(this);"
					onPopupSetValue="findLookupCRT"
					size="6" mask="000000" maxLength="6" >
					<adsm:propertyMapping 
						criteriaProperty="ctoInternacional.sgPais"
						modelProperty="sgPais"/>
					<adsm:propertyMapping 
						criteriaProperty="filialByIdFilialOrigem.idFilial"
						modelProperty="filialByIdFilialOrigem.idFilial"/>
					<adsm:propertyMapping 
						criteriaProperty="filialByIdFilialOrigem.sgFilial"
						modelProperty="filialByIdFilialOrigem.sgFilial"/>
					<adsm:propertyMapping 
						criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>

					<adsm:propertyMapping 
							modelProperty="clienteByIdClienteRemetente.pessoa.nrIdentificacao"
							relatedProperty="ctoInternacional.clienteByIdClienteRemetente.pessoa.nrIdentificacao"/>
					<adsm:propertyMapping 
							modelProperty="clienteByIdClienteRemetente.pessoa.nmPessoa"
							relatedProperty="ctoInternacional.clienteByIdClienteRemetente.pessoa.nmPessoa"/>
					<adsm:propertyMapping 
							modelProperty="clienteByIdClienteDestinatario.pessoa.nrIdentificacao"
							relatedProperty="ctoInternacional.clienteByIdClienteDestinatario.pessoa.nrIdentificacao"/>
					<adsm:propertyMapping 
							modelProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa"
							relatedProperty="ctoInternacional.clienteByIdClienteDestinatario.pessoa.nmPessoa"/>
					<adsm:propertyMapping
							modelProperty="nrPermisso"
							relatedProperty="ctoInternacional.nrPermisso"/>
				</adsm:lookup>
			</adsm:textbox>
		</adsm:complement>

		<adsm:hidden property="ctoInternacional.nrPermisso"/>
		<adsm:textbox label="importador" dataType="text" 
			property="ctoInternacional.clienteByIdClienteDestinatario.pessoa.nrIdentificacao" 
			size="20" width="82%" labelWidth="18%" disabled="true">
			<adsm:textbox dataType="text" 
				property="ctoInternacional.clienteByIdClienteDestinatario.pessoa.nmPessoa" 
				size="40" disabled="true"/>
		</adsm:textbox>
		<adsm:textbox label="exportador" dataType="text" 
			property="ctoInternacional.clienteByIdClienteRemetente.pessoa.nrIdentificacao" 
			size="20" labelWidth="18%" width="82%" disabled="true"	>
			<adsm:textbox dataType="text" 
				property="ctoInternacional.clienteByIdClienteRemetente.pessoa.nmPessoa" 
				size="40" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" label="destinatarioCarta" property="nmDestinatario" size="75" 
			maxLength="60" labelWidth="18%" width="82%" required="true"/>

		<adsm:combobox property="nrCampo" label="campoSerAlterado" domain="DM_CAMPOS_CRT_CARTA_CORRECAO" 
			required="true" width="82%" labelWidth="18%" disabled="true" onchange="return verificaCampo(this);"/>

		<adsm:textarea columns="100" rows="4" maxLength="1000" property="dsConteudoAtual" 
			label="ondeConsta" width="82%" labelWidth="18%" required="true" disabled="true"/>
		<adsm:textarea columns="100" rows="4" maxLength="1000" property="dsConteudoAlterado" 
			label="deveraConstar" width="82%" labelWidth="18%" required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton id="salvarCarta" disabled="true" callbackProperty="geraCartaCorrecao"/>
			<adsm:reportViewerButton id="gerarCarta" caption="imprimir"
				service="lms.expedicao.gerarCartaCorrecaoCRTAction"/>
 			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	document.getElementById("ctoInternacional.nrCrt").serializable = true;

	function myDataLoad_cb(data, errorMessage, errorCode, eventObj) {
	 	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
		setDisabled("salvarCarta", true);
	}

	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "gridRow_click" || event == "storeButton") {
			setDisabled("salvarCarta", true);
			setDisabled("gerarCarta", false);
			desabilitaCamposDetalhamento(true);
		} else if(event == "tab_click" || event == "newButton_click" || event == "removeButton") {
			setDisabled("salvarCarta", false);
			setDisabled("gerarCarta", true);
			desabilitaCamposDetalhamento(false);
			setFocusOnFirstFocusableField();
			/* Na inclusao Busca Dados da Sessao */
			buscaDadosSessao();
		}
		setDisabled("nrCampo", true);
		setDisabled("dsConteudoAtual", true);
	}

	/*
	* Carrega campos Default
	*/
	function buscaDadosSessao() {
		var sdo = createServiceDataObject("lms.expedicao.gerarCartaCorrecaoCRTAction.findDadosSessao", "buscaDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}

	function buscaDadosSessao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		setElementValue("filialByIdFilialOrigem.idFilial", data.idFilialUsuarioLogado);
		setElementValue("filialByIdFilialOrigem.sgFilial", data.sgFilialUsuarioLogado);
		setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", data.nmFantasiaFilialUsuarioLogado);
	}

	/*
	* CallBack do metodo STORE
	* Caso inclusao ele se encarrega de trazer a dtEmissao
	*/
	function geraCartaCorrecao_cb(data, error) {
		store_cb(data, error);
		if (error != undefined) {
			return;
		}
	}

	function desabilitaCamposDetalhamento(dis){
		setDisabled("dsConteudoAlterado", dis);
		setDisabled("ctoInternacional.sgPais", dis);
		setDisabled("ctoInternacional.idDoctoServico", dis);
		setDisabled("filialByIdFilialOrigem.idFilial", dis);
	}

	function sgPaisChange(campo) {
	 	var nrValue = getElementValue(campo);
	 	if(nrValue == "") {
	 		resetValue("ctoInternacional.idDoctoServico");
	 		desabilitaCamposAlterados(true);
	 	} else {
	 		setFocus(document.getElementById("ctoInternacional.nrCrt"), false);
	 		setElementValue(campo, nrValue.toUpperCase());
	 	}
	 	return true;
	}

	function desabilitaCamposAlterados(dis){
		resetValue("nrCampo");
		setDisabled("nrCampo", dis);
		resetValue("dsConteudoAtual");
		setDisabled("dsConteudoAtual", dis);
	}

	var ctoLocalizado;
	function ctoInternacional_cb(data, error) {
		if(data == undefined || data.length == 0){
			ctoLocalizado = null;
			desabilitaCamposAlterados(true);
		} else {
			setDisabled("nrCampo", false);
			setElementValue("filialByIdFilialOrigem.idFilial", 
				data[0].filialByIdFilialOrigem.idFilial);
			setElementValue("filialByIdFilialOrigem.sgFilial", 
				data[0].filialByIdFilialOrigem.sgFilial);
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", 
				data[0].filialByIdFilialOrigem.pessoa.nmFantasia);		
			ctoLocalizado = data[0];
		}
		return ctoInternacional_nrCrt_exactMatch_cb(data);
	}

	function findLookupCRT(data, error) {
		setElementValue("ctoInternacional.sgPais", data.sgPais);
		setElementValue("ctoInternacional.nrCrt", data.nrCrt);
		return nrCrtOnChange(getElement("ctoInternacional.nrCrt"));
	}

	function nrCrtOnChange(obj) {
		var isValid = getElementValue(obj).length > 0;
		if(!isValid) {
			desabilitaCamposAlterados(true);
		}
		return lookupChange({e:document.getElementById("ctoInternacional.idDoctoServico"), forceChange:true});
	}

	function verificaCampo(campo) {
		var vl = getElementValue(campo);
		setDisabled("dsConteudoAtual", true);
		resetValue("dsConteudoAtual");
		switch(vl) {
			case "01":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsDadosRemetente);
				break;
			case "04":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsDadosDestinatario);
				break;	
			case "05":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsLocalEmissao);
				break;	
			case "06":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsDadosConsignatario);
				break;	
			case "07":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsLocalCarregamento);
				break;	
			case "08":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsLocalEntrega);
				break;	
			case "09":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsNotificar);
				break;	
			case "10":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsTransportesSucessivos);
				break;
			case "11":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsDadosMercadoria);
				break;
			case "12":
				var ds = i18NLabel.getLabel("pesoBruto") + ".: ";
				if(ctoLocalizado.psReal) {
					ds += ctoLocalizado.psReal;
				}
				ds +=  "\n" + i18NLabel.getLabel("pesoNeto") + "..: ";
				if(ctoLocalizado.psLiquido) {
					ds += ctoLocalizado.psLiquido;
				}
				setElementValue("dsConteudoAtual", ds);
				break;		
			case "13":
				var ds = ctoLocalizado.vlVolume + " M3";
				setElementValue("dsConteudoAtual", ds);
				break;
			case "14":
				var ds = ctoLocalizado.moeda.dsSimbolo + " " + ctoLocalizado.vlMercadoria;
				setElementValue("dsConteudoAtual", ds);
				break;
			case "15":
				setDisabled("dsConteudoAtual", false);
				break;
			case "16":
				var ds = ctoLocalizado.dsValorMercadoria + " ";
				ds += ctoLocalizado.moeda.dsSimbolo + " " + ctoLocalizado.vlTotalMercadoria;
				setElementValue("dsConteudoAtual", ds);
				break;	
			case "17":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsAnexos);
				break;
			case "18":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsAduana);
				break;	
			case "19":
				var ds = ctoLocalizado.moeda.dsSimbolo + " " + ctoLocalizado.vlFreteExterno;
				setElementValue("dsConteudoAtual", ds);
				break;	
			case "21":
			case "23":
				setElementValue("dsConteudoAtual", ctoLocalizado.dsNomeRemetente);
				break;				
			case "22":
				setElementValue("dsConteudoAtual", ctoLocalizado.observacoes);
				break;	
			default: 
				resetValue("dsConteudoAtual");
				break;
		}
		return true;
	}
//-->
</script>