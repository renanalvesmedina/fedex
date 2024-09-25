<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">

	function carregaPagina() {
		onPageLoad();
		
		var sdo = createServiceDataObject("lms.coleta.manterMilkRunAction.newMaster");
		xmit({serviceDataObjects:[sdo]});		
	}

	function milkRunLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		loadButton();
		return false;
	}

	function loadButton() {
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var childGroup = tabCad.childTabGroup;
		var tabSemana1 = childGroup.getTab("semana1");
		var tabSemana2 = childGroup.getTab("semana2");
		var tabSemana3 = childGroup.getTab("semana3");
		var tabSemana4 = childGroup.getTab("semana4");
		var tabIframe1 = document.getElementById(tabSemana1.properties.id + '_iframe');
		var tabIframe2 = document.getElementById(tabSemana2.properties.id + '_iframe');
		var tabIframe3 = document.getElementById(tabSemana3.properties.id + '_iframe');
		var tabIframe4 = document.getElementById(tabSemana4.properties.id + '_iframe');
		
		tabIframe1.contentWindow.idMilkRun = getElementValue("idMilkRun");
		tabIframe1.contentWindow.iFrameSemana2 = tabIframe2;
		tabIframe1.contentWindow.iFrameSemana3 = tabIframe3;
		tabIframe1.contentWindow.iFrameSemana4 = tabIframe4;
		
		var populaExigencias = tabIframe1.contentWindow.populaExigencias;
		populaExigencias();
	}
	
</script>

<adsm:window service="lms.coleta.manterMilkRunAction" onPageLoad="carregaPagina">

	<adsm:form id="form1" action="/coleta/manterMilkRun" idProperty="idMilkRun" onDataLoadCallBack="milkRunLoad">
			   
		<adsm:lookup label="destinatario" labelWidth="20%" width="80%" size="20" maxLength="20"  serializable="true"
					 service="lms.coleta.manterMilkRunAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao"
					 dataType="text" 
					 property="cliente" 
					 idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="false"
					 disabled="false"
					 required="true"
					 onPopupSetValue="desabilitaRemetente"
					 onDataLoadCallBack="desabilitaRemetente"
					 onchange="return limpaCampos();">					 	
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		
		</adsm:lookup>
		
		<adsm:checkbox property="blColetasInterdependentes" label="coletasInterdependentes" labelWidth="20%" width="80%"/>
		
		<adsm:label key="espacoBranco" width="100%" style="border:none"/>
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="1Semana" id="semana1" height="250" src="/coleta/manterMilkRun" cmd="semana1" boxWidth="62"/>
			<adsm:tab title="2Semana" id="semana2" height="250" src="/coleta/manterMilkRun" cmd="semana2" boxWidth="62"/>
			<adsm:tab title="3Semana" id="semana3" height="250" src="/coleta/manterMilkRun" cmd="semana3" boxWidth="62"/>
			<adsm:tab title="4Semana" id="semana4" height="250" src="/coleta/manterMilkRun" cmd="semana4" boxWidth="62"/>
		</adsm:tabGroup>

		<adsm:buttonBar freeLayout="false">
			<adsm:button id="storeButton" onclick="salvar(this.form);" caption="salvar" />		
			<adsm:newButton/>						
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	
	function initWindow(eventObj) {
		if(eventObj.name == "tab_click") {
			if(getElementValue("idMilkRun") != null && getElementValue("idMilkRun") != "") {
				desabilitaTab("remetentes", false);
				loadButton();
			}
		}
		setDisabled("storeButton", false);
	}
	
	function salvar(form) {
		if(validateForm(form)) {
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");
			var childGroup = tabCad.childTabGroup;
	
			// Pega o FORM da tela principal		
			var dataFinal = new Object();
			setNestedBeanPropertyValue(dataFinal, "milkRun", buildFormBeanFromForm(tabCad.getElementById("form1")));
			
			// Pega a aba da semana 1 para pegar o FORM da sub-aba e as Lists dos dias.
			var tabSemana1 = childGroup.getTab("semana1");
			var fromSemana1 = tabSemana1.getElementById("idSemana1");
			var tabIframe1 = document.getElementById(tabSemana1.properties.id + '_iframe');
			var dataSemana1 = new Object();
			setNestedBeanPropertyValue(dataSemana1, "semana1", buildFormBeanFromForm(fromSemana1));
			
			// Pega a aba da semana 2 para pegar o FORM da sub-aba e as Lists dos dias.
			var tabSemana2 = childGroup.getTab("semana2");
			var fromSemana2 = tabSemana2.getElementById("idSemana2");
			var tabIframe2 = document.getElementById(tabSemana2.properties.id + '_iframe');
			var dataSemana2 = new Object();
			setNestedBeanPropertyValue(dataSemana2, "semana2", buildFormBeanFromForm(fromSemana2));
			
			// Pega a aba da semana 3 para pegar o FORM da sub-aba e as Lists dos dias.
			var tabSemana3 = childGroup.getTab("semana3");
			var fromSemana3 = tabSemana3.getElementById("idSemana3");
			var tabIframe3 = document.getElementById(tabSemana3.properties.id + '_iframe');
			var dataSemana3 = new Object();
			setNestedBeanPropertyValue(dataSemana3, "semana3", buildFormBeanFromForm(fromSemana3));
			
			// Pega a aba da semana 4 para pegar o FORM da sub-aba e as Lists dos dias.
			var tabSemana4 = childGroup.getTab("semana4");
			var fromSemana4 = tabSemana4.getElementById("idSemana4");
			var tabIframe4 = document.getElementById(tabSemana4.properties.id + '_iframe');
			var dataSemana4 = new Object();
			setNestedBeanPropertyValue(dataSemana4, "semana4", buildFormBeanFromForm(fromSemana4));				
			
			// Faz merge de todos os datas das semanas com o data da tela Cad
			merge(dataFinal, dataSemana1);
			merge(dataFinal, dataSemana2);
			merge(dataFinal, dataSemana3);
			merge(dataFinal, dataSemana4);
	
			// Xmit no resultado do merge	
		    var sdo = createServiceDataObject("lms.coleta.manterMilkRunAction.store", "resultSalvar", dataFinal);
			xmit({serviceDataObjects:[sdo]});	
		}
	}

	function resultSalvar_cb(data, error) {
		store_cb(data, error);
		loadButton();
		if(!error) {
			showSuccessMessage();
		}
	}
	
	/**
	 * Responsável por habilitar/desabilitar uma tab
	 */
	function desabilitaTab(aba, disabled) {
		var tabGroup = getTabGroup(this.document);	
		tabGroup.setDisabledTab(aba, disabled);
	}
	
	function desabilitaRemetente(data) {
		if(data.idCliente != undefined && data.idCliente != "") {
			desabilitaTab("remetentes", false);
		}
	}
	
	function desabilitaRemetente_cb(data) {	
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if(data[0] != undefined) {
			desabilitaTab("remetentes", false);				
		}
	}	
	
	/**
	 * Limpa os campos do form caso não exista cliente
	 */
	function limpaCampos() {
		cliente_pessoa_nrIdentificacaoOnChangeHandler();		
		if(getElementValue("cliente.pessoa.nrIdentificacao") == "" && 
		   getElementValue("cliente.idCliente") == "") {
		   	newButtonScript();
			desabilitaTab("remetentes", true);
		}		
		return true;
	}
	
</script>