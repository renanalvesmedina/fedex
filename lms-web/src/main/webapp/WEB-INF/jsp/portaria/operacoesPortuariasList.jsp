<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="operacoesPortuarias" service="lms.portaria.operacoesPortuariasAction" onPageLoadCallBack="pageLoadd">
  <adsm:form action="/portaria/operacoesPortuarias" id="formList">
  		<adsm:hidden property="idControleCarga"/>
  	 	<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				 service="lms.portaria.operacoesPortuariasAction.findLookupFilial" dataType="text" label="filial" size="3" 
				 action="/municipios/manterFiliais" required="true"
				 labelWidth="15%" width="35%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px" disabled="true">
		
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:grid idProperty="idControle" property="entregasPorto" onRowClick="gridEntregaRowClick" autoSearch="false"
				   service="lms.portaria.operacoesPortuariasAction.findGridEntrega" showPagging="false" title="entregasPorto" 
				   gridHeight="130" rows="5" scrollBars="vertical" selectionMode="none">
			<adsm:gridColumn title="meioTransporte" property="nrFrota" width="65"/>
			<adsm:gridColumn title="" property="nrIdentificador" width="75" align="left"/>
			<adsm:gridColumn title="semiReboque" property="nrFrotaReboque" width="65"/>
			<adsm:gridColumn title="" property="nrIdentificadorReboque" width="75" align="left"/>
			<adsm:gridColumnGroup customSeparator=" ">
				<adsm:gridColumn title="controleCarga" property="sgFilial" width="70" />
				<adsm:gridColumn title="" property="nrControleCarga" width="70" mask="00000000" dataType="integer"/>
			</adsm:gridColumnGroup>
			<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="105"/>
			<adsm:gridColumn title="rota" property="dsRota" width="160" />
		</adsm:grid>

		<adsm:grid idProperty="idControle" property="coletasPorto" onRowClick="gridColetaRowClick" autoSearch="false"
					service="lms.portaria.operacoesPortuariasAction.findGridColeta" 
					title="coletasPorto" showPagging="false" gridHeight="130" rows="5" scrollBars="vertical"
					selectionMode="none">
			<adsm:gridColumn title="semiReboque" property="nrFrotaReboque" width="100" />
			<adsm:gridColumn title="" property="nrIdentificadorReboque" width="100" align="left"/>
			<adsm:gridColumnGroup customSeparator=" ">
				<adsm:gridColumn title="controleCarga" property="sgFilial" width="95" />
				<adsm:gridColumn title="" property="nrControleCarga" width="95" mask="00000000" dataType="integer"/>
			</adsm:gridColumnGroup>
			<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="140"/>
			<adsm:gridColumn title="rota" property="dsRota" width="160" />
		</adsm:grid>
	
		<script>
			function semiReboqueEntreguePorto() {
				var msg = "<adsm:label key='semiReboqueEntreguePorto'/>";
				return msg;
			}
		</script>
		
		<adsm:buttonBar>
			<adsm:button id="atualizar" caption="atualizar" onclick="atualizarClick()"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script type="text/javascript">

function pageLoadd_cb() {
	onPageLoad_cb();
	findFilialSessao();
}

function initWindow(){  
		setDisabled("atualizar", false);
}

function findFilialSessao(){
	var data = new Array();
	var sdo = createServiceDataObject("lms.portaria.operacoesPortuariasAction.findFilialSessao",
				"findFilialSessao",data);
	xmit({serviceDataObjects:[sdo]});
}

//Funcao de callback do servico que retorna os dados do usuario logado. 
function findFilialSessao_cb(data, exception){
	if (exception == null){
		setElementValue("filial.idFilial", data.idFilial);
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
		notifyElementListeners({e:document.getElementById("filial.idFilial")});
		loadGrids();
	}
}

function atualizarClick(){
	var tab = getTab(document);

	if (tab.validate({name:"findButton_click"})) {
		loadGrids();
		setFocusOnFirstFocusableField();
	}
}

function loadGrids(){
	entregasPortoLoad_cb();
	coletasPortoLoad_cb();
	
}

function entregasPortoLoad_cb(){
	if (getElementValue("filial.idFilial") != '') {
		var data= new Array();
		setNestedBeanPropertyValue(data, "idFilial", getElementValue("filial.idFilial"));
		entregasPortoGridDef.executeSearch(data);
	}
}

function coletasPortoLoad_cb(){
	if (getElementValue("filial.idFilial") != '') {
		var data= new Array();
		setNestedBeanPropertyValue(data, "idFilial", getElementValue("filial.idFilial"));
		coletasPortoGridDef.executeSearch(data);
	}
}

function gridEntregaRowClick(id){
	if (confirm(semiReboqueEntreguePorto())) {
		setElementValue("idControleCarga", id);
		storeButtonScript('lms.portaria.operacoesPortuariasAction.generateEntrega', 'generateEntrega', document.getElementById("formList"));
	} else {
		entregasPortoLoad_cb();
	}
	
	return false;
}

function generateEntrega_cb(data, error) {
	entregasPortoLoad_cb();
}

function gridColetaRowClick(id){
	setElementValue("idControleCarga", id);
	showModalDialog('portaria/operacoesPortuarias.do?cmd=proc',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:120px;');
	coletasPortoLoad_cb();
	return false;
}

</script>