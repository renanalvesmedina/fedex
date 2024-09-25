<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.seguros.manterProcessosSinistroAction" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/seguros/manterProcessosSinistro" idProperty="idFotoProcessoSinistro" service="lms.seguros.manterProcessosSinistroAction.findFotosById" >

		<adsm:hidden property="idProcessoSinistro"/>	

		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />	

		<adsm:hidden property="idFoto" serializable="true" />		
		
		<adsm:textbox dataType="text" property="dsFoto" label="descricao" size="50"  maxLength="50" width="85%" required="true" />		
		<adsm:textbox dataType="file" property="foto" label="arquivo" width="85%" required="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="storeButton" service="lms.seguros.manterProcessosSinistroAction.storeFotos" callbackProperty="storeCallback"/>
			<adsm:newButton id="newButton" />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="fotoProcessoSinistro" 
				idProperty="idFotoProcessoSinistro" 
				selectionMode="check" 
				service="lms.seguros.manterProcessosSinistroAction.findPaginatedFotos" 
				rowCountService="lms.seguros.manterProcessosSinistroAction.getRowCountFotos" 
				detailFrameName="fotos"
				onPopulateRow="povoaLinhas"
				
				>
		<adsm:gridColumn title="descricao" property="dsFoto" width="90%" />
		<adsm:gridColumn title="arquivo"      property="foto.foto"   width="10%" image="/images/tratativas.gif" align="center"/>
		<adsm:buttonBar>
			<adsm:button id="removeButton" caption="excluir" buttonType="removeButton" onclick="onRemoveButtonClick();" />
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script>

	function onRemoveButtonClick() {
		fotoProcessoSinistroGridDef.removeByIds('lms.seguros.manterProcessosSinistroAction.removeFotosByIds', 'onRemoveCallback');
	}
	
	function onRemoveCallback_cb() {
		executeSearch();
		newButtonScript(document, true);
	}

	function povoaLinhas(tr, data) {
		var tdIdFoto = tr.children[2].innerHTML;
		var idFoto = tdIdFoto.substring(tdIdFoto.indexOf("</IMG>") + 6, tdIdFoto.indexOf("</A>")).replace(" ","");
		fakeDiv = document.createElement("<DIV></DIV>");
		fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:showPicture('" + idFoto + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"../images/tratativas.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
		tr.children[2].innerHTML = fakeDiv.children[0].children[0].children[0].children[0].innerHTML;
	}

	function onTabShow() {
		var tabGroup = getTabGroup(document);
		setElementValue("idProcessoSinistro", tabGroup.getTab("cad").getFormProperty("idProcessoSinistro"));
		setElementValue("nrProcessoSinistro", tabGroup.getTab("cad").getFormProperty("nrProcessoSinistro"));		
		document.getElementById("idProcessoSinistro").masterLink="true";
		document.getElementById("nrProcessoSinistro").masterLink="true";

		executeSearch();
	}
	
	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		data.idProcessoSinistro = getElementValue('idProcessoSinistro');
		fotoProcessoSinistroGridDef.executeSearch(data);
	}

	// FIXME: ao carregar a aba, chama evento de novo, que 
	// faz com que o campo foto esteja no padrão correto, sem 
	// textbox. Isso é uma falha cadastrada para a arquitetura.	
	function pageLoad_cb() {
		newButtonScript(this.document, true, {name:'newButton_click'});
	}
	
	function storeCallback_cb(data, error) {
		store_cb(data, error);
		if (error==undefined) 
			executeSearch();
	}
</script>
