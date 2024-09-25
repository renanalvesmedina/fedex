<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.mediaIndicadoreFinanceiroMoedaService">
	<adsm:form action="/configuracoes/consultarMediaMensalIndicadoresFinanceiros">
		<adsm:textbox dataType="integer" property="ano" label="ano" size="5" maxLength="4" required="true"/>
		
	   	<adsm:lookup service="lms.municipios.paisService.findLookup" dataType="text" property="pais"
			criteriaProperty="nmPais" idProperty="idPais" exactMatch="false" minLengthForAutoPopUpSearch="3"
			label="pais" maxLength="40" action="/municipios/manterPaises" required="true"/>	
				
		<adsm:combobox onchange="limparCombo(document.getElementById('idIndicadorFinanceiro'));" service="lms.configuracoes.consultarMediaMensalIndicadoresFinanceirosAction.findMoedaPaisCombo" 
			property="moeda.idMoeda" optionProperty="idMoeda" optionLabelProperty="descricao" label="moeda">
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais"/>
		</adsm:combobox>

		<adsm:combobox onchange="limparCombo(document.getElementById('moeda.idMoeda'));" service="lms.configuracoes.indicadorFinanceiroService.findIndicadoresFinanceirosByPaisCombo" 
			property="idIndicadorFinanceiro" optionProperty="idIndicadorFinanceiro" optionLabelProperty="nmIndicadorFinanceiro" label="indicadorFinanceiro"
			boxWidth="200">
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais"/>
		</adsm:combobox>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button disabled="false" buttonType="findButton" caption="consultar" onclick="consultar();"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		<script>
			msgErro = "<adsm:label key='LMS-27031'/>";
		</script>
	</adsm:form>
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="mensal" autoLoad="true" id="mensal" src="/configuracoes/consultarMediaMensalIndicadoresFinanceiros" cmd="mensal" height="278"/>
		<adsm:tab title="diario" id="cad" disabled="true" onShow="myOnShow" src="/configuracoes/consultarMediaMensalIndicadoresFinanceiros" cmd="diario" height="278"/>
	</adsm:tabGroup>
</adsm:window>
<script>

function limparCombo(obj){
	setElementValue(obj,"");
}

function consultar(){
	if (validateForm(this.document.forms[0]) == true){
		if (getElementValue("moeda.idMoeda") == "" && getElementValue("idIndicadorFinanceiro")==""){
			alert(msgErro);
			setFocusOnFirstFocusableField(document);
			return;
		}
		//ref para tabgroup desse janela
		var tabGroup = getTabGroup(this.document);
		//ref tab dessa janela
		var tab = tabGroup.selectedTab;
		//ref para tabgroup filho
		var tabGroupChild = tab.childTabGroup;
		//ref para tab mensal
		var tabMensal = tabGroupChild.getTab("mensal");
		//ref da tela da tab mensal		
		var tela = tabMensal.tabOwnerFrame;
		
		//ref para tab diario
		var tabDiario = tabGroupChild.getTab("cad");		
		//ref da tela da tab mensal		
		var tela2 = tabDiario.tabOwnerFrame;		
		
		var filtrosMensal = new Object();
		filtrosMensal.ano = getElementValue("ano");	
		filtrosMensal.idPais = getElementValue("pais.idPais");
		filtrosMensal.idMoeda = getElementValue("moeda.idMoeda");
		filtrosMensal.idIndicadorFinanceiro = getElementValue("idIndicadorFinanceiro");	
			
		//chamar a pesquisa da grid mensal
		tela.mediaMensalGridDef.executeSearch(filtrosMensal);	
		tabGroupChild.selectTab("mensal");
		//limpar grid 
		tabDiario.setDisabled(true);
		tela2.mediaDiarioGridDef.resetGrid();
	}
}
</script>
