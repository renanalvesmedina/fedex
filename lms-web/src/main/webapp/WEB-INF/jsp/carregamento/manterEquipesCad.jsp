<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	function initWindow(eventObj) {
		var idFilial = getElementValue("filial.idFilial");
		if (idFilial == "") {
			loadDataUsuario();		
		}
		if (eventObj.name=="storeButton"){
			setFocusOnNewButton();			
		}
	}

	function loadPage() {
		onPageLoad();		
		loadDataUsuario();	
	}

	function loadDataUsuario() {
    	var data = new Array();
		var sdo = createServiceDataObject("lms.carregamento.equipeService.getDataUsuario", "loadDataUsuario", data);
    	xmit({serviceDataObjects:[sdo]});
	}	

	function loadDataUsuario_cb(data, error) {
		setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
		setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
		setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
	}
	
	function limpaUsuario(document) {
		loadDataUsuario();		
		newButtonScript(document);
	}
	
</script>
<adsm:window service="lms.carregamento.manterEquipesAction" onPageLoad="loadPage">
	<adsm:form action="/carregamento/manterEquipes" idProperty="idEquipe" newService="lms.carregamento.manterEquipesAction.newMaster">
		<adsm:hidden property="filial.idFilial" />
     	<adsm:textbox property="filial.sgFilial" label="filial" dataType="text" size="3" width="85%" disabled="true" serializable="false" >
			<adsm:textbox property="filial.pessoa.nmFantasia" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
	
		<adsm:combobox width="85%" label="setor" 
					    property="setor.idSetor"
					    service="lms.configuracoes.setorService.findSetorOrderByDsSetor" 
					    optionProperty="idSetor" 
					    optionLabelProperty="dsSetor" 
					    onlyActiveValues="true"
					    required="true"
		/>	
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:textbox property="dsEquipe" label="descricao" dataType="text" size="57" width="85%" maxLength="50" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:button caption="limpar" id="limpar" buttonType="newButton" onclick="limpaUsuario(this.document)" disabled="false"/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>