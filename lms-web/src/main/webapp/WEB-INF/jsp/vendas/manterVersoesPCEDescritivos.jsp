<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/manterVersoesPCE" idProperty="idVersaoDescritivoPce" id="Lazy" service="lms.vendas.manterVersoesPCEAction.findByIdVersaoDescritivasPce"  height="185" onDataLoadCallBack="dataLoad">
		<adsm:masterLink idProperty="idVersaoPce" showSaveAll="true">
			<adsm:masterLinkItem property="cliente.pessoa.nmPessoa" label="cliente" itemWidth="50"/>
			<adsm:masterLinkItem property="cliente.pessoa.nrIdentificacao" label="identificacao" itemWidth="40"/>
			<adsm:masterLinkItem property="nrversaoPce" label="versao" itemWidth="100"/>
		</adsm:masterLink> 
		<adsm:hidden property="cliente.idCliente" serializable="false" value=""/>
		
		<adsm:combobox property="descritivoPce.ocorrenciaPce.eventoPce.processoPce.idProcessoPce"
				service="lms.vendas.manterVersoesPCEAction.findProcessoPce" optionLabelProperty="processoPceCombo" optionProperty="idProcessoPce"
				label="processo" labelWidth="10%" width="40%" required="true" onlyActiveValues="true" boxWidth="220">
			<adsm:propertyMapping relatedProperty="descritivoPce.ocorrenciaPce.eventoPce.processoPce.cdProcessoPce" modelProperty="cdProcessoPce"/>
			<adsm:propertyMapping relatedProperty="descritivoPce.ocorrenciaPce.eventoPce.processoPce.dsProcessoPce" modelProperty="dsProcessoPce"/>
		</adsm:combobox>

		<adsm:combobox property="descritivoPce.ocorrenciaPce.eventoPce.idEventoPce" optionLabelProperty="eventoPceCombo"
				optionProperty="idEventoPce" service="lms.vendas.manterVersoesPCEAction.findEventoPce"
				label="evento" required="true" labelWidth="16%" width="34%" onlyActiveValues="true" boxWidth="220">
			<adsm:propertyMapping criteriaProperty="descritivoPce.ocorrenciaPce.eventoPce.processoPce.idProcessoPce" modelProperty="processoPce.idProcessoPce"/>
			<adsm:propertyMapping relatedProperty="descritivoPce.ocorrenciaPce.eventoPce.cdEventoPce" modelProperty="cdEventoPce"/>
			<adsm:propertyMapping relatedProperty="descritivoPce.ocorrenciaPce.eventoPce.dsEventoPce" modelProperty="dsEventoPce"/>
		</adsm:combobox>

		<adsm:combobox property="descritivoPce.ocorrenciaPce.idOcorrenciaPce"
				optionLabelProperty="ocorrenciaPceCombo" optionProperty="idOcorrenciaPce"
				service="lms.vendas.manterVersoesPCEAction.findOcorrenciaPce"
				label="ocorrencia" boxWidth="220" required="true" labelWidth="10%" width="40%" onlyActiveValues="true">
			<adsm:propertyMapping criteriaProperty="descritivoPce.ocorrenciaPce.eventoPce.idEventoPce" modelProperty="eventoPce.idEventoPce"/>
			<adsm:propertyMapping relatedProperty="descritivoPce.ocorrenciaPce.cdOcorrenciaPce" modelProperty="cdOcorrenciaPce"/>
			<adsm:propertyMapping relatedProperty="descritivoPce.ocorrenciaPce.dsOcorrenciaPce" modelProperty="dsOcorrenciaPce"/>
		</adsm:combobox>
		
		<adsm:hidden property="descritivoPce.ocorrenciaPce.eventoPce.processoPce.cdProcessoPce"/>
		<adsm:hidden property="descritivoPce.ocorrenciaPce.eventoPce.cdEventoPce"/>
		<adsm:hidden property="descritivoPce.ocorrenciaPce.cdOcorrenciaPce"/>
		<adsm:hidden property="descritivoPce.ocorrenciaPce.eventoPce.processoPce.dsProcessoPce"/>
		<adsm:hidden property="descritivoPce.ocorrenciaPce.eventoPce.dsEventoPce"/>
		<adsm:hidden property="descritivoPce.ocorrenciaPce.dsOcorrenciaPce"/>

		<adsm:combobox property="descritivoPce.idDescritivoPce" onchange="change_descritivoPce(this)"
				optionLabelProperty="cdDescritivoPce" optionProperty="idDescritivoPce"
				service="lms.vendas.manterVersoesPCEAction.findDescritivoPce" 
				label="codigoDescritivo" required="true" labelWidth="16%" width="34%" onlyActiveValues="true" boxWidth="220">
			<adsm:propertyMapping criteriaProperty="descritivoPce.ocorrenciaPce.idOcorrenciaPce" modelProperty="ocorrenciaPce.idOcorrenciaPce"/>
			<adsm:propertyMapping relatedProperty="descritivoPce.dsDescritivoPce" modelProperty="dsDescritivoPce"/>
			<adsm:propertyMapping relatedProperty="descritivoPce.cdDescritivoPce" modelProperty="cdDescritivoPce"/>
			<adsm:propertyMapping relatedProperty="descritivoPce.blIndicadorAviso" modelProperty="blIndicadorAviso"/>
		</adsm:combobox>		

		<adsm:hidden property="descritivoPce.cdDescritivoPce"/>
		<adsm:checkbox property="descritivoPce.blIndicadorAviso"
				label="indicadorAviso" labelWidth="10%" width="90%" disabled="true" style="width:16px" cellStyle="vertical-align:bottom" />
		
		<adsm:listbox optionProperty="idVersaoContatoPce" property="versaoContatoPces1" size="6" boxWidth="285"
				label="funcionario" labelWidth="10%" width="40%" >
			<adsm:lookup property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula" 
					service="lms.vendas.manterVersoesPCEAction.findLookupFuncionario"
					dataType="text"
					size="10"
					maxLength="20"
					labelStyle="vertical-align: text-top; margin-top: 4px;padding-top:3px" 
					action="/configuracoes/consultarFuncionariosView" 
					exactMatch="true"
					disabled="false">
				<adsm:propertyMapping relatedProperty="versaoContatoPces1_usuario.codPessoa.nome" modelProperty="nmFuncionario"/>
				<adsm:textbox dataType="text" property="usuario.codPessoa.nome" size="25" maxLength="60" disabled="true" serializable="true"/>
				
			</adsm:lookup>
				
		</adsm:listbox>
		
		<adsm:listbox optionProperty="idVersaoContatoPce" onContentChange="contentChangeContatos"
				width="50%" property="versaoContatoPces" size="3" boxWidth="344">
			<adsm:label key="formaComunicacao" >:&nbsp;&nbsp;
				<adsm:combobox boxWidth="150" property="tpFormaComunicacao"
						domain="DM_FORMA_COMUNICACAO_PCE" onlyActiveValues="true"
						disabled="true" required="false"/>
			</adsm:label>
		    
		    <adsm:label key="contato" >:&nbsp;&nbsp;
		    	<adsm:combobox boxWidth="220" onlyActiveValues="true" 
			            labelStyle="vertical-align: text-top; margin-top: 4px;padding-top:3px" 
						optionProperty="idContato" 
						service="lms.vendas.manterVersoesPCEAction.findContato" 
						optionLabelProperty="nmContato" property="contato" 
						autoLoad="false"
						disabled="true" required="false">
				</adsm:combobox>
			</adsm:label>
		    
		  	<adsm:label key="regiao">:&nbsp;&nbsp;&nbsp;&nbsp;
		  		<adsm:textbox dataType="text" property="dsRegiaoAtuacao"
		  				maxLength="60" disabled="true" required="false" size="30" />
		  	</adsm:label>
		</adsm:listbox>
		
		<adsm:textarea columns="126" rows="5" maxLength="250" property="descritivoPce.dsDescritivoPce" label="descricao"
				labelWidth="10%" width="90%" disabled="true"/>

<script type="text/javascript">
<!--
	var lms01080 = "<adsm:label key="LMS-01080"/>";
	var lms01082 = "<adsm:label key="LMS-01082"/>";
	var LMS_01085 = "<adsm:label key="LMS-01085"/>";

//-->
</script>		

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="incluir" caption="salvarDescritivo" onclick="storeCustomizado()" />
			<adsm:newButton/>
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid idProperty="idVersaoDescritivoPce" property="VersaoDescritivoPce" detailFrameName="descritivos"
				selectionMode="check" rows="5" unique="true" autoSearch="true" service="lms.vendas.manterVersoesPCEAction.findPaginatedVersaoDescritivasPce"
				rowCountService="lms.vendas.manterVersoesPCEAction.getRowCountVersaoDescritivasPce">

		<adsm:gridColumn title="processo" property="descritivoPce.ocorrenciaPce.eventoPce.processoPce.processoPceGrid" width="180"/>
		<adsm:gridColumn title="evento" property="descritivoPce.ocorrenciaPce.eventoPce.eventoPceGrid" width="230"/>
		<adsm:gridColumn title="ocorrencia" property="descritivoPce.ocorrenciaPce.ocorrenciaPceGrid" width="180"/>
		<adsm:gridColumn title="codigoDescritivo" property="descritivoPce.cdDescritivoPce" align="right" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDescritivo" id="excluirDescritivo" service="lms.vendas.manterVersoesPCEAction.removeByIdsVersaoDescritivasPce"/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
function storeCustomizado() {
	var tabGroup = getTabGroup(this.document);
	tab	= tabGroup.getTab("descritivos");
	var valid = false;

	if (tab != null)
		valid = tab.validate({name:"storeButton_click"});

	if (valid == false)
		return false;

	if (getElementValue("versaoContatoPces_contato") != "" || getElementValue("versaoContatoPces_tpFormaComunicacao") != "" || getElementValue("versaoContatoPces_dsRegiaoAtuacao") != "" || getElementValue("versaoContatoPces1_usuario.idUsuario") != "") {
		if (confirm(LMS_01085))		
			storeButtonScript('lms.vendas.manterVersoesPCEAction.saveVersaoDescritivasPce', 'storeItemAdvanced', document.Lazy);
	}else
		storeButtonScript('lms.vendas.manterVersoesPCEAction.saveVersaoDescritivasPce', 'storeItemAdvanced', document.Lazy);
}

	function onShowTabDescritivos() {
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		if (tabCad.getFormProperty("cliente.idCliente") == "" ||
			tabCad.getFormProperty("cliente.idCliente") == undefined) {
			alert(lms01082);	
			var tabGroup = getTabGroup(this.document);	
				tabGroup.selectTab('cad',{name:'tab_click'})
			return false;
		}
		populaComboContato();
	}
	
	
	function contentChangeContatos(eventObj) {
		if (eventObj.name == "modifyButton_click" && (getElementValue("versaoContatoPces_tpFormaComunicacao") == "" || getElementValue("versaoContatoPces_contato") == "")){
								alert(lms01080);
								return false;
		}
							
	}
	function populaComboContato() {
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var remoteCall = {serviceDataObjects:new Array()};
		if (tabCad.getFormProperty("cliente.idCliente") != "") {
			remoteCall.serviceDataObjects.push(createServiceDataObject("lms.vendas.manterVersoesPCEAction.findContato", "versaoContatoPces_contato", { pessoa:{idPessoa:tabCad.getFormProperty("cliente.idCliente")}}));
			xmit(remoteCall);
		}
	}
	
	function storeItemAdvanced_cb(data,exception) {
		if (exception == undefined) {
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");
			tabCad.getElementById("cliente.pessoa.nrIdentificacao").masterLink = "true";
		}
		storeItem_cb(data,exception);
		populaComboContato();
		enabledFields();
	}
	
	function change_descritivoPce(field) {
		comboboxChange({e:field});
		changeContatos();
		limpaCampos();
	}
	
	function changeContatos() {
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		if (tabCad.getFormProperty("acaoVigenciaAtual") == "2" || !document.getElementById("descritivoPce.blIndicadorAviso").checked) {
			
			if (tabCad.getFormProperty("acaoVigenciaAtual") != "2")
				for(x = document.getElementById("versaoContatoPces").options.length - 1; x >= 0; x--)
					document.getElementById("versaoContatoPces").options[0] = null;
				
			versaoContatoPcesListboxDef.cleanRelateds();
			setDisabled("versaoContatoPces_contato",true);
			setDisabled("versaoContatoPces_tpFormaComunicacao",true);
			setDisabled("versaoContatoPces_dsRegiaoAtuacao",true);
			setDisabled("versaoContatoPces",true);
			setDisabled("versaoContatoPces1_usuario.idUsuario",true);
			
		}else{
			setDisabled("versaoContatoPces_contato",false);
			setDisabled("versaoContatoPces_tpFormaComunicacao",false);
			setDisabled("versaoContatoPces_dsRegiaoAtuacao",false);
			setDisabled("versaoContatoPces",false);
			setDisabled("versaoContatoPces1_usuario.idUsuario",false);
			setDisabled("versaoContatoPces1_usuario.codPessoa.nome",true);
		}
	}
	
	function limpaCampos(){
		versaoContatoPcesListboxDef.cleanRelateds();
		versaoContatoPces1ListboxDef.cleanRelateds();
		resetListBoxValue(document.getElementById("versaoContatoPces1"));
		resetListBoxValue(document.getElementById("versaoContatoPces"));
		
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "newItemButton_click") {
			enabledFields();
			populaComboContato();
		}
		if (eventObj.name == "removeButton_grid")
			setDisabled("incluir",false);
	}
	
	function enabledFields() {
		setDisabled(document,false);
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		if (tabCad.getFormProperty("acaoVigenciaAtual") != "2") {
			setDisabled(document,false);
			changeContatos();
		}else
			setDisabled(document,true);
		
		setDisabled("descritivoPce.blIndicadorAviso",true);
		setDisabled("descritivoPce.dsDescritivoPce",true);
		
		setDisabled("_cliente.pessoa.nmPessoa",false);
		setDisabled("_nrversaoPce",false);
		setFocusOnFirstFocusableField(document);
		setDisabled("excluirDescritivo",true);
	}
	
	function dataLoad_cb(data,exception) {
		if (exception) {
			alert(exception);
			return;
		}
		onDataLoad_cb(data,exception);
		populaComboContato();
		changeContatos();
	}
	
	
	
	
//-->
</script>

