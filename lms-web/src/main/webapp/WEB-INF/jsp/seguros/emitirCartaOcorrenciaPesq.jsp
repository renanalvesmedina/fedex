<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoad="loadPage" onPageLoadCallBack="pageLoad" service="lms.seguros.emitirCartaOcorrenciaAction">
	<adsm:form action="/seguros/emitirCartaOcorrencia" height="200" idProperty="idprocessoSinistro" id="formCartaOcorrencia">

		<adsm:i18nLabels>
			<adsm:include key="LMS-22050"/>
		</adsm:i18nLabels>

		<adsm:lookup label="processoSinistro" 
					property="processoSinistro" 
					idProperty="idProcessoSinistro" 
					criteriaProperty="nrProcessoSinistro"
					disabled="false"
					onDataLoadCallBack="loadDataNumeroProcesso"
					picker="true" 	
					popupLabel="pesquisarProcessoSinistro"				
					dataType="text"
					onchange="return onProcessoSinistroChange(this);"
					onPopupSetValue="onProcessoSinistroPopupSetValue"
					action="/seguros/manterProcessosSinistro" cmd="list"
					service="lms.seguros.emitirCartaOcorrenciaAction.findLookupProcessoSinistro"
					>

					<adsm:propertyMapping modelProperty="dhSinistro" relatedProperty="dhSinistro" />
					<adsm:propertyMapping modelProperty="tipoSinistro.dsTipo" relatedProperty="tipoSinistro.dsTipo" />
					<adsm:propertyMapping modelProperty="municipio.nmMunicipio" relatedProperty="municipio.nmMunicipio" />
					<adsm:propertyMapping modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
					<adsm:propertyMapping modelProperty="dsSinistro" relatedProperty="dsSinistro" />

					<adsm:propertyMapping relatedProperty="nrProcessoSinistro" modelProperty="nrProcessoSinistro"/>
					<adsm:hidden property="nrProcessoSinistro" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox property="dhSinistro" dataType="JTDateTimeZone" label="dataHoraSinistro" size="20" maxLength="16" disabled="true" picker="false"/>

		<adsm:textbox property="tipoSinistro.dsTipo" dataType="text" label="tipoSinistro" disabled="true" />

		<adsm:textbox property="municipio.nmMunicipio" dataType="text" label="municipio" maxLength="60" size="30" disabled="true" />

		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" size="2" disabled="true" />

		<adsm:textarea property="dsSinistro" label="descricaoSinistro" maxLength="200" columns="109" rows="5" width="85%" disabled="true" />
		
		<adsm:buttonBar freeLayout="bottom">
			<adsm:button id="gerarCarta" caption="gerarCarta" onclick="openLetter()" />
			<adsm:button id="botaoLimpar" caption="limpar" buttonType="resetButton" onclick="onClickBotaoLimpar()"  />
		</adsm:buttonBar>
		<script>
			function lms_22012() {
				alert("<adsm:label key='LMS-22012'/>");
			}
		</script>
	</adsm:form>
</adsm:window>

<script>
	
	function loadPage() {
		setMasterLink(document, true);
		onPageLoad();
	}
	
	function pageLoad_cb() {
		onPageLoad_cb();
		onChangeProcesso();
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click"){
			setDisabled("gerarCarta", false);
		}
	}
	
	//##################################
    // Comportamentos apartir de objetos
	//##################################
		
	function loadDataNumeroProcesso_cb(data, error) {
		processoSinistro_nrProcessoSinistro_exactMatch_cb(data);
		
		if ((data._exception==undefined) && (data[0]!=undefined)) {
			setDisableButtons(false);
		} else if (data._exception!=undefined){
			alert(data._exception._message);
		}
	}
	
	function openLetter() {
		validatePCE();
	}
	
	//#####################################################
	// Inicio da validacao do pce
	//#####################################################
	
	//Esta variavel deve ser declarada decorrente de uma necessidade da tela de popUp...
	var codigos;
	function getCodigos() {
		return codigos;
	}
	
	//Esta variavel deve ser declarada decorrente de uma necessidade da tela de popUp...
	var idsSinistroDoctoServico;
	
	
	/**
	 * Faz o mining de ids de pedidoColeta para iniciar a validacao dos mesmos
	 *
	 * @param methodComplement
	 */
	function validatePCE() {
		var data = new Object();
		data.idProcessoSinistro = getElementValue("processoSinistro.idProcessoSinistro");
		var sdo = createServiceDataObject("lms.seguros.emitirCartaOcorrenciaAction.validatePCE", "validatePCE", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback da validacao. 
	 *
	 * @param data
	 * @param error
	 */ 
	function validatePCE_cb(data, error) {
	
		if(error != undefined) {
			alert("LMS-22050 - " + i18NLabel.getLabel("LMS-22050"));
			return false;
		}
	
		var list = data.list;
		if (list!=undefined) {
		
			codigos = new Array();
		
			//Carregando list que sera carregada pela de popUp...
			for (var i=0; i<list.length; i++) {
				if (list[i].codigo!=undefined) {
					codigos[i] = list[i].codigo;
				}
			} 
			
			// Janela de chamada para a tela de pce
			// Apos sua chamada cai na funcao de callBack - alertPCE
			if (codigos.length>0) {
				showModalDialog('vendas/alertaPce.do?cmd=list', window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:353px;');
			}
		}
		
		idsSinistroDoctoServico = new Array();
		
		//Carregando list que sera carregada pela de popUp...
		for (var i = 0; i < data.ids.length; i++) {
			if (data.ids != undefined) {
				idsSinistroDoctoServico[i] = data.ids[i];
			}
		} 
				
		showModalDialog('seguros/emitirCartaOcorrencia.do?cmd=dadosCarta',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
	}
	
	
	//#####################################################
	// Fim da validacao do pce
	//#####################################################
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * Desabilita os botao da tela.
	 *
	 * @param disable 
	 */
	function setDisableButtons(disable) {
		setDisabled("gerarCarta", disable);
	}
	
	/**
	 * Reseta os dados da tela
	 */
	function resetView(){
		resetValue(this.document);
	}
	
	/**
	* Caso a tela seja chamada por outra tela, então o número do processo assim como
	* outros valores ficam desabilitados.
	* Nesse caso, o botão limpar não deve limpar os campos.
	*/
	function onClickBotaoLimpar(){
		if (!document.getElementById("processoSinistro.nrProcessoSinistro").disabled){		
			cleanButtonScript(this.document);
			setDisabled("gerarCarta", true);
		}
	}

	/**
	* OnPopupSetValue da Lookup de Processo de Sinistro
	*/	
	function onProcessoSinistroPopupSetValue(data, error) {
		if (data!=undefined) {			
			if (data.idProcessoSinistro!=undefined) {
				var vet = new Array();				
				vet.idProcessoSinistro = data.idProcessoSinistro;
			    var sdo = createServiceDataObject("lms.seguros.emitirCartaOcorrenciaAction.findLookupProcessoSinistro", "loadDataNumeroProcesso", vet);
			    xmit({serviceDataObjects:[sdo]});
			}
		}
	}	
	
	/**
	 * Gerencia o comportamento para os campos texto do numero processo 
	 * 'nrProcessoMercurio'
	 */
	function onChangeProcesso(){ 
		
		var nrProcessoSinistro = getElementValue('processoSinistro.nrProcessoSinistro');
		var idProcessoSinistro = getElementValue('processoSinistro.idProcessoSinistro');
				
		if (nrProcessoSinistro!='') {
				
			resetView(document.getElementById("formCartaOcorrencia"));
			
			setElementValue("processoSinistro.nrProcessoSinistro", nrProcessoSinistro);
			setElementValue("processoSinistro.idProcessoSinistro", idProcessoSinistro);
			
		    document.getElementById("processoSinistro.idProcessoSinistro").forceChange=true;
		    document.getElementById("processoSinistro.nrProcessoSinistro").previousValue="";
		    processoSinistro_nrProcessoSinistroOnChangeHandler();							
		}
	}	
	
	// onchange do campo processoSinistro
	function onProcessoSinistroChange(e) {
		if (e.value == "") {
			setDisabled("gerarCarta", true);
		}
		return processoSinistro_nrProcessoSinistroOnChangeHandler();
	}
	
</script>
