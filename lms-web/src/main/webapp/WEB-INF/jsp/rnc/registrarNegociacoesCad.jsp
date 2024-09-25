<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	/**
	 * Carrega dados do usuario e dados para os objetos
	 */
	function loadDataObjects() {
    	var objSemLabel = document.getElementById('idsOcorrenciaNaoConformidade');
    	var objComLabel = document.getElementById('spanlbl_idsOcorrenciaNaoConformidade');
    	
    	objSemLabel.label = objComLabel.innerText;
    	
    	document.getElementById("registrarNovaNegociacao").disabled=false;
		document.getElementById("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade").required=true;
		
    	onPageLoad();
	}
</script>

<adsm:window service="lms.rnc.registrarNegociacoesAction" onPageLoad="loadDataObjects" >
	<adsm:form action="/rnc/registrarNegociacoes" idProperty="idNegociacao" onDataLoadCallBack="loadDataOcorrenciaNC">

		<adsm:hidden property="blOcorrenciaIsFechada" serializable="false" />
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		
		<adsm:lookup dataType="text" property="ocorrenciaNaoConformidade.naoConformidade.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.rnc.registrarNegociacoesAction.findFilialLookUp" action="/municipios/manterFiliais" onchange="return sgFilialOnChangeHandler();" onDataLoadCallBack="disableNrNaoConformidade"
					 label="naoConformidade" labelWidth="20%" width="30%" size="3" maxLength="3" picker="false" serializable="false" popupLabel="pesquisarFilial">
		        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
				<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:lookup dataType="integer" property="ocorrenciaNaoConformidade.naoConformidade" idProperty="idNaoConformidade" criteriaProperty="nrNaoConformidade"
						 action="/rnc/manterNaoConformidade" service="lms.rnc.registrarNegociacoesAction.findNaoConformidadeLookUp" onchange="return nrNaoConformidadeChangeHandler();"
						 onDataLoadCallBack="loadNrNaoConformidade" onPopupSetValue="enableNrNaoConformidade" disabled="true"
						 exactMatch="false" size="15" maxLength="8" required="true" mask="00000000" popupLabel="pesquisarNaoConformidade"> 
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial" blankFill="false"/>
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial"/>
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"/>
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhNegociacao" 
					  label="dataHoraNegociacao" labelWidth="17%" width="33%" disabled="true" picker="false"/>	
					
		<adsm:hidden property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"/>
		<adsm:listbox property="idsOcorrenciaNaoConformidade" 
	    			  optionProperty="idOcorrenciaNC" optionLabelProperty="nrOcorrenciaNc"
	    			  label="numeroOcorrencia" labelWidth="20%" width="50%" size="5" boxWidth="170" onchange="onChangeListNumOcorrencia();" required="true">
	    			  
	    	<adsm:combobox property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade" 
						   optionProperty="idOcorrenciaNaoConformidade" optionLabelProperty="nrOcorrenciaNc"
						   service="lms.rnc.registrarNegociacoesAction.findOcorrenciaAbertasDeNaoConformidade" 
						   onchange="return comboBoxOcorrenciaNCChangeHandler({e:this});" 
						   onDataLoadCallBack="loadOcorrencianNC" >
				<adsm:propertyMapping modelProperty="naoConformidade.idNaoConformidade" criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"/>
		    	<adsm:propertyMapping modelProperty="dsOcorrenciaNc" relatedProperty="ocorrenciaNaoConformidade.dsOcorrenciaNc"/>
			</adsm:combobox>
	    </adsm:listbox>
					   		
		<adsm:textbox dataType="text" property="usuario.nmUsuario" 
					  label="usuarioResponsavel" size="23" labelWidth="20%" width="30%" disabled="true"/>

		<adsm:textbox dataType="text" property="ocorrenciaNaoConformidade.filialByIdFilialLegado.sgFilial" size="3" label="rncLegado" 
					  labelWidth="17%" width="33%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="ocorrenciaNaoConformidade.nrRncLegado" size="8" mask="00000000" 
						  disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					service="lms.municipios.filialService.findLookup" action="/municipios/manterFiliais" 
					label="filialUsuario" size="3" maxLength="3" labelWidth="20%" width="80%" picker="false" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:textarea property="ocorrenciaNaoConformidade.dsOcorrenciaNc" label="descricaoOcorrencia" columns="100" rows="5" maxLength="255" labelWidth="20%" width="80%" disabled="true" />
		<adsm:textarea property="dsNegociacao" label="descricaoNegociacao" columns="100" rows="5" maxLength="500" labelWidth="20%" width="80%" required="true"/>
		
		<adsm:hidden property="usuario.idUsuario"/>
		
		<adsm:buttonBar>
			<adsm:button  caption="registrarNovaNegociacao" onclick="btnRegistrarNovaNegociacaoClick();" id="registrarNovaNegociacao"/>
			<adsm:storeButton id="btnSalvar"/>
			<adsm:button  id="btnLimpar" caption="limpar" buttonType="new" onclick="btnLimparOnClick();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	function isTelaLocalizacaoMercadorias(){
		var tabList = getTabGroup(this.document).getTab("pesq");
		var idOcorrenciaNaoConformidadeLocMerc = tabList.getFormProperty("idOcorrenciaNaoConformidadeLocMerc");
		if (idOcorrenciaNaoConformidadeLocMerc != undefined && idOcorrenciaNaoConformidadeLocMerc != ""){
			setDisabled("btnSalvar", true);
			setDisabled("btnLimpar",true);
			setDisabled("registrarNovaNegociacao",true);
		}
	}


	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
    		disableObjects(false);
    		disableNrNaoConformidade(true);
			isTelaLocalizacaoMercadorias();
			loadDataUsuario();
			
			if (getElementValue("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade") != "") {
				loadDataFromURL();
			}
		} else if (eventObj.name == "newButton_click") {

			disableNrNaoConformidade(true);
			if (getElementValue("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade") != "") {
				loadDataFromURL();
			}

		} else if (eventObj.name == "gridRow_click") {

			disableObjects(true);
			setDisabled('dsNegociacao', true);
			document.getElementById("btnLimpar").focus();

		} else if (eventObj.name == "storeButton") {
		
			disableObjects(true);
			disableNrNaoConformidade(true);
			
			setDisabled('dsNegociacao', true);
			if (document.getElementById("idsOcorrenciaNaoConformidade").length>1) {
				setDisabled('btnSalvar', true);
			} else {
				//Nao entendi o pq dessa linha
				setFocus(document.getElementById("dsNegociacao"));
			}
			document.getElementById("btnLimpar").focus();
			
		//Outros casos
		} else {
			setDisabled('dsNegociacao', false);
			disableObjects(false);
			setFocus(document.getElementById('ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial'));
			loadDataFromURL();
			loadDataUsuario();
		}
		
		//Override da função de remover elemento da listbox
		idsOcorrenciaNaoConformidadeListboxDef.deleteOption = customDeleteOption;
    }


	/**
	 * Controla o fluxo de carregamento de dados da tela
	 * carregando primeiro a combo de numero ocorrencia
	 */
	var oldData;
	var oldError;
	function loadDataOcorrenciaNC_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		
		//Utilizados no callBack do metodo abaixo...
		oldData = data;
		oldError = error;
		
		var beanValue = getNestedBeanPropertyValue(data, "ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade");
		
		var sdo = createServiceDataObject("lms.rnc.registrarNegociacoesAction.findOcorrenciaNaoConformidade", "loadDataCombo", {naoConformidade:{idNaoConformidade:beanValue}});
    	xmit({serviceDataObjects:[sdo]});
    }


	function loadDataCombo_cb(data, error){
		idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade_idOcorrenciaNaoConformidade_cb(data);
		document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").value = getNestedBeanPropertyValue(oldData, "ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade");
		onDataLoad_cb(oldData, oldError);				
		
		idsOcorrenciaNaoConformidadeListboxDef.insertOrUpdateOption();

		if (getElementValue("blOcorrenciaIsFechada") == "true") {
			setDisabled("registrarNovaNegociacao",true);
		}
		isTelaLocalizacaoMercadorias();
	}


	/**
	 * Controle para o objeto de 'Causas da não conformidade'
	 */
	function comboBoxOcorrenciaNCChangeHandler(element){
		setElementValue("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", getElementValue("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"));
		return comboboxChange(element);
	}


	/**
	 * Controle para o objeto de 'Não conformidade'
	 */	
	function sgFilialOnChangeHandler() {
		var r = lookupChange({e:document.forms[0].elements["ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"]});
		if (getElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial")=="") {
			disableNrNaoConformidade(true);
			resetView();
		} else {
			disableNrNaoConformidade(false);
		}
		return r;
	}

	function disableNrNaoConformidade_cb(data, error) {
		var r = lookupExactMatch({e:document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"), data:data});
		if (r == true) {
			disableNrNaoConformidade(false);
		}
		return r;
	}


	function loadNrNaoConformidade_cb(data, error) {
		ocorrenciaNaoConformidade_naoConformidade_nrNaoConformidade_exactMatch_cb(data);
		if (data[0]!=undefined) {
			document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").value=data[0].filial.sgFilial;
		}
	}


	function nrNaoConformidadeChangeHandler() {
		if (getElementValue("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade")=="") {
			var idFilial = getElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial");
			var sgFilial = getElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial");
			resetValue(this.document);
			setElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial", idFilial);
			setElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial", sgFilial);
			loadDataUsuario();
		} 
		return ocorrenciaNaoConformidade_naoConformidade_nrNaoConformidadeOnChangeHandler();
	}


	function enableNrNaoConformidade(data){
		if (data.nrNaoConformidade!=undefined) {
			disableNrNaoConformidade(false);
		} else {
			disableNrNaoConformidade(true);
		}
	}


	/**
	 * Controle para o objeto de Número da Ocorrência
	 */
	function loadOcorrencianNC_cb(data, error) {
		if (error){
			setDisabled("registrarNovaNegociacao",true);
			var tabList = getTabGroup(this.document).getTab("pesq");
			var idOcorrenciaNaoConformidadeLocMerc = tabList.getFormProperty("idOcorrenciaNaoConformidadeLocMerc");
			if (idOcorrenciaNaoConformidadeLocMerc != undefined && idOcorrenciaNaoConformidadeLocMerc != ""){
				setDisabled("btnSalvar", true);
				setDisabled("btnLimpar",true);
		    } else {
				alert(error);
				setElementValue("blOcorrenciaIsFechada", "true");
				setFocus(document.getElementById("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade"));
			}

		} else {
			idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade_idOcorrenciaNaoConformidade_cb(data);
			if (document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").isDisabled == true) {

				//busca os dados da URL
				var url = new URL(parent.location.href);
				var idOcorrenciaNaoConformidadeValue = url.parameters["ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"];
				if (idOcorrenciaNaoConformidadeValue != undefined && idOcorrenciaNaoConformidadeValue != "") {
					document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").options[0].selected = true; 
				}
			} else if (data.length==1) {
				document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").value=data[0].idOcorrenciaNaoConformidade;
				document.getElementById("ocorrenciaNaoConformidade.dsOcorrenciaNc").value=data[0].dsOcorrenciaNc;
			}
		}
	}


	/**
	 * Carrega o objeto a partir url
	 * caso o usuario tenha vindo de uma outra tela
	 */
	function loadDataFromURL(){
		var url = new URL(parent.location.href);
		var idNaoConformidade = url.parameters["ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"];
		if (idNaoConformidade!=undefined) {
			setElementValue("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", idNaoConformidade);

			//Carrega a combo numero ocorrencia
			notifyElementListeners({e:document.getElementById("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade")});

			//Deixa os objetos 
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial", true);
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", true);

			//aplica a mascara no campo
			format(document.getElementById("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade"));
		}
	}


	/**
	 * Controla o objeto 'Numero Ocorrencia'
	 */
	function onChangeListNumOcorrencia() {
	//alert();
		idsOcorrenciaNaoConformidadeListboxDef.updateRelateds();
		comboboxChange({e:document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade")});
	}


	/**
	 * Carrega os dados do usuario para a tela
	 */
	 function loadDataUsuario() {
	 	var data = new Object();
		var sdo = createServiceDataObject("lms.rnc.registrarNegociacoesAction.getDataUsuario", "loadDataUsuario", data);
    	xmit({serviceDataObjects:[sdo]});
	}

	 
	function loadDataUsuario_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		setElementValue("usuario.nmUsuario", data.usuario.nmUsuario);
		setElementValue("usuario.idUsuario", data.usuario.idUsuario);
		setElementValue("filial.idFilial", data.filial.idFilial);
		setElementValue("filial.sgFilial", data.filial.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);

		if (document.getElementById("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade").disabled == true) {
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial", true);
		}
	}


	/**
	 * Desabilita 'quase' todos os objetos da tela
	 * Utilizada quando se edita algum registro.
	 */
	function disableObjects(valor) {
    	setDisabled("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial", valor);
    	setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", valor);
    	setDisabled("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", valor);
    	setDisabled("idsOcorrenciaNaoConformidade", valor);
    	setDisabled("dsNegociacao", valor);
    }


    function disableNrNaoConformidade(valor) {
		if (valor == true && getElementValue("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade") == "") {
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", false);
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade", true);
		}
		else
			setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", valor);
	}

	
	/**
	 * Utilizado ao se clicar no botao 'limpar'
	 */
	function btnLimparOnClick(){
    	newButtonScript(this.document, true, {name:'newButton_click'});
    	disableObjects(false);
    	disableNrNaoConformidade(true);
    	setDisabled("registrarNovaNegociacao",true);
    	setFocusOnFirstFocusableField();
    	loadDataUsuario();
    }

    
	/**
	 * Utilizado ao se clicar no botao 'registrarNovaNegociacao'
	 */
	 function btnRegistrarNovaNegociacaoClick(){
	 	disableObjects(false);
	 	disableNrNaoConformidade(false);
	 	
	 	setDisabled("dsNegociacao", false);
	 	setDisabled("btnSalvar", false);
	 	
	 	resetValue("idNegociacao");
	 	resetValue("dhNegociacao");
	 	resetValue("ocorrenciaNaoConformidade.filialByIdFilialLegado.sgFilial");
	 	resetValue("ocorrenciaNaoConformidade.nrRncLegado");
		resetValue("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade");
		resetValue("idsOcorrenciaNaoConformidade");
		resetValue("ocorrenciaNaoConformidade.dsOcorrenciaNc");
		resetValue("dsNegociacao");
		setDisabled("registrarNovaNegociacao", true);
		
		document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").firstChild.selected=true;
		document.getElementById("idsOcorrenciaNaoConformidade_idOcorrenciaNC").value="";
		
		//Verifica se o campo filial do objeto nao conformidade esta preenchido, caso ele nao esteja sete o foco nele
		if (document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").value=="") {
			setFocus(document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial"));
		} else {
			setFocus(document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"));
		}
		
		loadDataUsuario();
		
		//Carrega o objeto de nrOcorrencia...
		var idNaoConformidade = getElementValue("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade");
		var sdo = createServiceDataObject("lms.rnc.registrarNegociacoesAction.findOcorrenciaAbertasDeNaoConformidade", "loadOcorrencianNC", {naoConformidade:{idNaoConformidade:idNaoConformidade}});
    	xmit({serviceDataObjects:[sdo]});
	 }

	 /**
	  * Limpa todos os valores na tela.
	  * Com exceção de 'filial' e 'usuario'
	  */
	function resetView(){
		//Busca os valores do campo usuario
		var nmUsuario = getElementValue("usuario.nmUsuario");
		var idUsuario = getElementValue("usuario.idUsuario");
		var idFilial = getElementValue("filial.idFilial");
		var sgFilial = getElementValue("filial.sgFilial");
		var nmFantasia = getElementValue("filial.pessoa.nmFantasia");

		resetValue(this.document);
		
		//Seta os valores para o campo usuario
		setElementValue("usuario.nmUsuario", nmUsuario);
		setElementValue("usuario.idUsuario", idUsuario);
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFantasia);
	}

	//Override da função de remover elemento da listbox
	function customDeleteOption(){
		var listboxElem = document.getElementById(this.properties.property);
		if (listboxElem.elementDisabled == true) { // se estiver desabilitado não executa
			return;
		}
	
		if (this.onContentChange({name:"deleteButton_click"}) == false) {
			return;
		}
	
		var option = null;	
		if (listboxElem.selectedIndex != -1) {
			var conf = true;
			if (this.properties.isPairedListbox == false) {
				conf = confirm(listbox_removeSelectedItem);
			}
			if (conf) { 
				var idx = listboxElem.selectedIndex;
				option = listboxElem.options[listboxElem.selectedIndex];
				listboxElem.remove(idx); 
				// atualiza o indice dos elementos abaixo do removido
				if (this.properties.showIndex) {
					for (var j = idx; j < listboxElem.length; j++) {
						var option = listboxElem.options[j];
						option.text = (j+1) + " - " + option.text.substr(option.text.indexOf(" - ")+3, option.text.length);
					}
				}
				this.cleanRelateds();
			}
		} else {
			alert(listbox_selectOptionToDelete);
		}
		// executa o evento após a inserção.
		this.onContentChange({name:"deleteButton_afterClick", src:option});
		// limpa a descrição da ocorrência
		resetValue("ocorrenciaNaoConformidade.dsOcorrenciaNc");
	}		
	
</script>