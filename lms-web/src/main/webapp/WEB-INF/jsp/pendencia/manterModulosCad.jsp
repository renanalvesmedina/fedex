<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.manterModulosAction" onPageLoadCallBack="loadPage">
	<adsm:form action="/pendencia/manterModulos" idProperty="idModulo" service="lms.pendencia.manterModulosAction.findByIdCustom">

		<adsm:lookup dataType="text" property="terminal.filial" idProperty="idFilial" criteriaProperty="sgFilial"  onDataLoadCallBack="lookupFilial"
	   				 service="lms.pendencia.manterModulosAction.findLookupFilial" action="/municipios/manterFiliais" 
					 label="filial" size="3" maxLength="3" width="85%" disabled="false" onPopupSetValue="validaAcessoFilial"
					 serializable="false" required="true">
			<adsm:propertyMapping relatedProperty="terminal.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="terminal.filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true" serializable="false"/>
			<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" />
			<adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="flagBuscaEmpresaUsuarioLogado"/>				
		</adsm:lookup>
		
		<adsm:combobox label="terminal" 
				optionProperty="idTerminal" 
				optionLabelProperty="pessoa.nmPessoa" 
				property="terminal.idTerminal" 
				service="lms.pendencia.manterModulosAction.findTerminaisVigentesOrVigenciaFuturaByFilial" 
				width="85%"
				required="true">
				<adsm:propertyMapping criteriaProperty="terminal.filial.idFilial" modelProperty="terminal.filial.idFilial" />
		</adsm:combobox>

		<adsm:textbox label="numeroModulo" property="numeroModulo" dataType="integer" width="85%" required="true" size="3" maxLength="3"/>
		<adsm:combobox label="situacao" property="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:button caption="excluir" buttonType="removeButton" disabled="false" onclick="onRemoveButtonClick();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function onRemoveButtonClick() {
		removeButtonScript('lms.pendencia.manterModulosAction.removeById', 'remove', 'idModulo', this.document);
	}
	
	function remove_cb(data, error) {
		if (error == undefined) {
			newButtonScript(this.document, true, {name:'newButton_click'});
			fillDataUsuario();			
		}
	}

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "newButton_click") {
			setElementValue("terminal.filial.sgFilial", "");
			setElementValue("terminal.filial.idFilial", "");
			setElementValue("terminal.filial.pessoa.nmFantasia", "");
			setElementValue("situacao", "A");
			fillDataUsuario();
			setFocusOnFirstFocusableField(document);
		}
	}

	/**
	 * Verifica se o usuario tem acesso a filial selecionada na popup de filial.
	 * Função necessária pois quando é selecionado um item na popup não é chamado
	 * o serviço definido na lookup.
	 */
	 function validaAcessoFilial(data) {
		var criteria = new Array();
	    setNestedBeanPropertyValue(criteria, "idFilial", data.idFilial);
	    var sdo = createServiceDataObject("lms.pendencia.manterModulosAction.findLookupFilial", "resultadoLookup", criteria);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Função que trata o retorno da função validaAcessoFilial.
	 */
	 function resultadoLookup_cb(data, error) {
		if (error != undefined) {
			alert(error);
		    setFocus(document.getElementById("terminal.filial.sgFilial"));
		    resetValue("terminal.filial.idFilial");
		    resetValue("terminal.filial.pessoa.nmFantasia");
			return false;
		} else {
			notifyElementListeners({e:document.getElementById("terminal.filial.idFilial")});
			setFocus(document.getElementById("terminal.idTerminal"));
		}
	}
	
	function loadPage_cb() {
    	var data = new Array();
		var sdo = createServiceDataObject("lms.pendencia.manterModulosAction.findFilialUsuarioLogado", "loadFilialUsuario", data);
   		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega os dados de filial do usuario logado
	 */
	var dataUsuario;
	function loadFilialUsuario_cb(data, error) {
		dataUsuario = data;
		fillDataUsuario();
		// onPageLoad();
	}
	
	/**
	 * Faz o callBack do carregamento da pagina
	 *
	function loadPage_cb(data, error) {
		setDisabled("terminal.idFilial", false);
		document.getElementById("terminal.sgFilial").disabled=false;
		document.getElementById("terminal.sgFilial").focus;
	} */
	
	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		setElementValue("terminal.filial.idFilial", dataUsuario.filial.idFilial);
		setElementValue("terminal.filial.sgFilial", dataUsuario.filial.sgFilial);
		setElementValue("terminal.filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
		
		lookupChange({e:document.forms[0].elements["terminal.filial.idFilial"], forceChange:true});
	}
	
	function lookupFilial_cb(data, error) {
		terminal_filial_sgFilial_exactMatch_cb(data);
		setFocusOnFirstFocusableField(document);
	}
	

</script>