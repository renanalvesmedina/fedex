<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.dadosCarregamentoAction" >

	<adsm:form action="/carregamento/dadosCarregamento" idProperty="idFotoCarregmtoDescarga" onDataLoadCallBack="retorno_carregaDados"
			   service="lms.carregamento.dadosCarregamentoAction.findByIdFotoCarregmtoDescarga" >

		<adsm:textbox label="controleCargas" property="sgFilialControleCarga" dataType="text" 
					  size="3" labelWidth="18%" width="22%" disabled="true" serializable="false" >
	 		<adsm:textbox property="nrControleCarga" dataType="integer" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="filial" property="filial.sgFilial" dataType="text" size="3" maxLength="3" labelWidth="10%" width="50%" disabled="true">
			<adsm:textbox property="filial.pessoa.nmFantasia" dataType="text" size="30" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox property="dsFoto" dataType="text" label="descricao" size="50" labelWidth="18%" width="82%" disabled="true" />

		<adsm:textbox
				property="foto.foto"
				blobColumnName="FOTO"
				tableName="FOTO"
				primaryKeyColumnName="ID_FOTO"
				primaryKeyValueProperty="foto.idFoto"
				dataType="picture"
				label="foto"
				labelWidth="18%"
				width="82%"
				disabled="true" />
		<adsm:hidden property="foto.idFoto" />
	</adsm:form>

	<adsm:grid property="fotosCarregamentoDescarga" idProperty="idFotoCarregmtoDescarga" selectionMode="none"
		showPagging="true" rows="13" onPopulateRow="povoaLinhas" autoSearch="false" defaultOrder="dsFoto:asc"
		onRowClick="populaForm"
		service="lms.carregamento.dadosCarregamentoAction.findPaginatedFotos"
		rowCountService="lms.carregamento.dadosCarregamentoAction.getRowCountFotos" >
		<adsm:gridColumn title="descricao"	property="dsFoto" width="90%" />
		<adsm:gridColumn title="foto"		property="foto.foto" width="10%" image="/images/camera.gif" align="center" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		resetValue("dsFoto");
		resetValue("foto.foto");

		var tabGroup = getTabGroup(this.document);
	    var tabDet = tabGroup.getTab("list");
	    povoaDadosMaster();
		povoaGrid(tabDet.getFormProperty("idCarregamentoDescarga"));
	}
	setaFocoBotaoFechar();
}

function povoaGrid(idCarregamentoDescarga) {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "carregamentoDescarga.idCarregamentoDescarga", idCarregamentoDescarga);
    fotosCarregamentoDescargaGridDef.executeSearch(filtro, true);
    return false;
}

function povoaDadosMaster() {
	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("list");
    setElementValue("sgFilialControleCarga", tabDet.getFormProperty("sgFilialControleCarga"));
    setElementValue("nrControleCarga", tabDet.getFormProperty("nrControleCarga"));
    setElementValue("filial.sgFilial", tabDet.getFormProperty("filial.sgFilial"));
    setElementValue("filial.pessoa.nmFantasia", tabDet.getFormProperty("filial.pessoa.nmFantasia"));
}

function populaForm(id) {
	onDataLoad(id);
	return false;
}


function retorno_carregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	povoaDadosMaster();
	setaFocoBotaoFechar();
}

function setaFocoBotaoFechar() {
	setDisabled("botaoFechar", false);
	setFocus(document.getElementById("botaoFechar"), true, true);
}

function povoaLinhas(tr, data) {
	var idBlob = data.foto.foto;
	fakeDiv = document.createElement("<DIV></DIV>");
	fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:showPicture('" + idBlob + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/camera.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
	tr.children[1].innerHTML = fakeDiv.children[0].children[0].children[0].children[0].innerHTML;
}</script>