<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:i18nLabels>
	<adsm:include key="pcDiferencaPadrao"/>
	<adsm:include key="pcDiferencaMinima"/>
	<adsm:include key="vlDiferencaPadrao"/>
	<adsm:include key="vlDiferencaMinima"/>
	<adsm:include key="ufOrigem"/>
	<adsm:include key="ufDestino"/>
	<adsm:include key="freteValor"/>
	<adsm:include key="fretePeso"/>
</adsm:i18nLabels>
<adsm:window service="lms.tabelaprecos.manterDiferencaCapitalInteriorAction">
	<adsm:form action="/tabelaPrecos/manterDiferencaCapitalInterior" idProperty="idDiferencaCapitalInterior" >
		
		<adsm:hidden property="ufOrigem.siglaDescricao"/>
		<adsm:combobox
			label="ufOrigem"
			service="lms.tabelaprecos.manterDiferencaCapitalInteriorAction.findUnidadeFederativaByPais"
			property="ufOrigem.idUnidadeFederativa"
			optionLabelProperty="sgUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			onlyActiveValues="true"
			labelWidth="12%"
			width="70%"
			boxWidth="150"
		>
			<adsm:propertyMapping relatedProperty="ufOrigem.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:hidden property="ufDestino.siglaDescricao"/>
		<adsm:combobox
			label="ufDestino"
			service="lms.tabelaprecos.manterDiferencaCapitalInteriorAction.findUnidadeFederativaByPais"
			property="ufDestino.idUnidadeFederativa"
			optionLabelProperty="sgUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			onlyActiveValues="true"
			labelWidth="12%"
			width="70%"
			boxWidth="150"
		>
			<adsm:propertyMapping relatedProperty="ufDestino.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>
				
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="diferencaCapitalInterior"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		idProperty="idDiferencaCapitalInterior" 
		property="diferencaCapitalInterior"
		scrollBars="horizontal" 
		gridHeight="250" 
		unique="true" 
		rows="11" >		

		<adsm:gridColumn title="ufOrigem" 			property="ufOrigem.sgUnidadeFederativa" 	width="20%" />
		<adsm:gridColumn title="ufDestino" 			property="ufDestino.sgUnidadeFederativa" 	width="20%" />
	
		<adsm:gridColumn title="fretePeso" 	property="pcDiferencaPadrao" 				width="15%" dataType="decimal" />		
		<adsm:gridColumn title="" 	property="pcDiferencaMinima" 				width="15%" dataType="decimal" />		

		<adsm:gridColumn title="freteValor" 	property="pcDiferencaPadraoAdvalorem" 		width="15%" dataType="decimal" />		
		<adsm:gridColumn title="" 	property="pcDiferencaMinimaAdvalorem" 		width="15%" dataType="decimal" />
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>	
	 
</adsm:window>

<script type="text/javascript">
	/*Função chamada ao carregar a tela*/
	function initWindow(eventObj) {
		createGridHtml();
	}

	// Função totalmente desaconselhada para manipular os dados na tela
	// afim de atender as necessidades do usuario no prazo que a arquitetura nao suporta :s
	function createGridHtml() {
		var headerTable = getElement("diferencaCapitalInterior.headerTable");
		if(headerTable.children[0].children.length > 1) {
			return;
		}	
				
		headerTable.children[0].children[0].children[0].rowSpan = "2";
		headerTable.children[0].children[0].children[0].width = "24";
		headerTable.children[0].children[0].children[1].rowSpan = "2";
		headerTable.children[0].children[0].children[1].width = "20%";
		headerTable.children[0].children[0].children[2].rowSpan = "2";
		headerTable.children[0].children[0].children[2].width = "20%";
		headerTable.children[0].children[0].children[3].colSpan = "2";
		headerTable.children[0].children[0].children[3].width = "30%";
		headerTable.children[0].children[0].children[4].colSpan = "2";
		headerTable.children[0].children[0].children[4].width = "30%";	

		//Cria uma custom HeaderRow
		var gridHTML = new Array();
		gridHTML.push("<TR>");

		gridHTML.push('<th class="FmSep" style="text-align:center; width: 15%;">'+getI18nMessage("pcDiferencaPadrao")+'</th>');
		gridHTML.push('<th class="FmSep" style="text-align:center; width: 15%;">'+getI18nMessage("pcDiferencaMinima")+'</th>');
		
		gridHTML.push('<th class="FmSep" style="text-align:center; width: 15%;">'+getI18nMessage("vlDiferencaPadrao")+'</th>');
		gridHTML.push('<th class="FmSep" style="text-align:center; width: 15%;">'+getI18nMessage("vlDiferencaMinima")+'</th>');

		gridHTML.push("</TR>");

		var fakeDiv = document.createElement("<DIV></DIV>");
    	fakeDiv.innerHTML = "<table>" + gridHTML.join('') + "</table>";
		var tr = fakeDiv.children[0].children[0].children[0];		
		
		headerTable.children[0].appendChild(tr);
	}
</script>
