<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.coleta.enviarEmailAgendamentoAction">
	
		
	<adsm:buttonBar freeLayout="true">
		<adsm:button caption="salvarItem" disabled="false" onclick="storeAnexo();" id="salvar"/>	
	 </adsm:buttonBar>
	<adsm:form action="/coleta/enviarEmailAgendamento">

	<adsm:hidden property="idAgendamentoEntrega" serializable="false"/>
	<adsm:hidden property="dirArquivo" serializable="false"/>


	<adsm:textbox labelWidth="10%" label="arquivo" property="dcArquivo" dataType="file" width="50%" size="60" required="true" serializable="true"/>
	</adsm:form>
	<adsm:grid property="agendamentoAnexo"  idProperty="idAgendamentoAnexo" service="lms.coleta.enviarEmailAgendamentoAction.findPaginatedAgendamentoAnexo"
			rowCountService="lms.coleta.enviarEmailAgendamentoAction.findAgendamentoAnexoRowCount"
			 rows="10" gridHeight="100" onDataLoadCallBack="findAgendamentoAnexo" detailFrameName="anexo">
		<adsm:gridColumn property="dsAnexo" title="arquivo"  width="270" />
		<adsm:gridColumn property="dhInclusao" dataType="JTDateTimeZone" title="dataHoraDeInclusao" width="80" />
	<adsm:buttonBar freeLayout="false">
	
		<adsm:button caption="excluirItem" buttonType="removeButton" onclick="excluirItem();"  />
	 </adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">


	function initWindow(evento){
			var url = new URL(parent.location.href);
	 		window.onbeforeunload  = fechar;
			var idAgendamentoEntrega = url.parameters["idAgendamentoEntrega"];
			if(getElementValue("idAgendamentoEntrega") == "" && idAgendamentoEntrega != undefined){
				excluiAnexos();
				setDisplay("dcArquivo_browse",false);
				setElementValue("idAgendamentoEntrega",idAgendamentoEntrega);
			}
	}

	function formLoadScript(){}
	function onDataLoad(){}
	
	function fechar(){
		excluiAnexos();
	}
	
	function excluiAnexos(){
		var formData = new Object();
 		setNestedBeanPropertyValue(formData,"idAgendamentoEntrega", getElementValue("idAgendamentoEntrega"));
 		
 		var sdo = createServiceDataObject("lms.coleta.enviarEmailAgendamentoAction.excluiAnexos", "excluiAnexos", formData);
 		xmit({serviceDataObjects:[sdo]}); 
	}
	function excluiAnexos_cb(){}
 	
	function loadPage_cb(data, error){
		if (error != undefined) {
			alert(error);
			return;
		}
		
	}
 	
 	function excluirItem(){
 		agendamentoAnexoGridDef.removeByIds("lms.coleta.enviarEmailAgendamentoAction.removeByIdsAgendamentoAnexo", "excluirItem");
 	}

 	function excluirItem_cb(data, error){
 		if(error != undefined) {
 			alert(error);
 		}
 		refreshGrid();
 	}
 	
 	
	function refreshGrid(){
	 		var data= new Array();
	 		setNestedBeanPropertyValue(data,"idAgendamentoEntrega", getElementValue("idAgendamentoEntrega"));
			agendamentoAnexoGridDef.executeSearch(data,true);
 	}
 	
	function findAgendamentoAnexo_cb(data, error){
		setDisabled("salvar",false);
		if(error != undefined){
			alert(error);
			return;
		}
	}
	
 	function storeAnexo(){
 		setDisabled("salvar",true);
 		var formData = new Object();
 		setNestedBeanPropertyValue(formData,"dirArquivo",getElement("dcArquivo").value);
 		setNestedBeanPropertyValue(formData,"dcArquivo", getElementValue("dcArquivo"));
 		setNestedBeanPropertyValue(formData,"idAgendamentoEntrega", getElementValue("idAgendamentoEntrega"));
 		
 		var sdo = createServiceDataObject("lms.coleta.enviarEmailAgendamentoAction.storeAnexo", "storeAnexo", formData);
 		xmit({serviceDataObjects:[sdo]}); 
 	}
	
 	function storeAnexo_cb(data, error){
		if(error != undefined){
			alert(error);
			setDisabled("salvar",false);
			return;
		}
		refreshGrid();
 	}
 	
	
</script>