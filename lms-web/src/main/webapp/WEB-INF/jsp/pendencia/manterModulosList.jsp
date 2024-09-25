<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

	function loadPage() {
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
		onPageLoad();
	}
	
	/**
	 * Faz o callBack do carregamento da pagina
	 */
	function loadPage_cb(data, error) {
		setDisabled("terminal.filial.idFilial", false);
		document.getElementById("terminal.filial.sgFilial").disabled=false;
	}
</script>


<adsm:window service="lms.pendencia.manterModulosAction" onPageLoad="loadPage" onPageLoadCallBack="loadPage">
	<adsm:form action="/pendencia/manterModulos" idProperty="idModulo">
				
		<adsm:lookup dataType="text" property="terminal.filial" idProperty="idFilial" criteriaProperty="sgFilial"
	   				 service="lms.pendencia.manterModulosAction.findLookupFilial" action="/municipios/manterFiliais" onDataLoadCallBack="lookupFilial"
					 label="filial" size="3" maxLength="3" width="85%" disabled="false" onPopupSetValue="validaAcessoFilial" required="true">
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
				width="85%">
				<adsm:propertyMapping criteriaProperty="terminal.filial.idFilial" modelProperty="terminal.filial.idFilial"/>
		</adsm:combobox>
		<adsm:textbox label="numeroModulo" property="numeroModulo" dataType="integer" width="85%" size="3" maxLength="3"/>
		<adsm:combobox label="situacao" property="situacao" domain="DM_STATUS" width="85%" renderOptions="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="modulo"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid service="lms.pendencia.manterModulosAction.findPaginatedCustom" 
			rowCountService="lms.pendencia.manterModulosAction.getRowCountCustom" 
			idProperty="idModulo" 
			property="modulo" 
			selectionMode="check" 
			gridHeight="100" 
			unique="true" rows="11">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn property="sgFilial" title="filial" width="30" />
			<adsm:gridColumn property="nmFilial" title="" width="250"/>
		</adsm:gridColumnGroup>			
		<adsm:gridColumn property="terminal" title="terminal" />
		<adsm:gridColumn property="modulo" title="numeroModulo" align="right" width="100"/>
		<adsm:gridColumn property="situacao" title="situacao" isDomain="true" width="70"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			resetValue("terminal.filial.idFilial");
			resetValue("terminal.filial.pessoa.nmFantasia");
			fillDataUsuario();
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
	

	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		setElementValue("terminal.filial.idFilial", dataUsuario.filial.idFilial);
		setElementValue("terminal.filial.sgFilial", dataUsuario.filial.sgFilial);
		setElementValue("terminal.filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
		
		var args = new Array();
		
		setNestedBeanPropertyValue(args, "e", document.forms[0].elements["terminal.filial.idFilial"]);
		setNestedBeanPropertyValue(args, "forceChange", true);
		
 		lookupChange(args);
		setFocus("terminal.filial.sgFilial");
	}
	
	function lookupFilial_cb(data, error) {
		terminal_filial_sgFilial_exactMatch_cb(data);
		setFocusOnFirstFocusableField(document);
	}
	
</script>
