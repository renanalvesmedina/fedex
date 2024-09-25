<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="300" idProperty="idDoctoServico">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="observacoes" id="ComplementosObservacoes" src="/sim/consultarLocalizacoesMercadorias" cmd="complementosObservacoes" height="230" copyMasterTabProperties="true" onShow="findPaginatedComplObservacoes" autoLoad="false"/>
		<adsm:tab title="embalagens" id="ComplementosEmbalagens" src="/sim/consultarLocalizacoesMercadorias" cmd="complementosEmbalagem" height="230" onShow="findPaginatedComplEmbalagens" copyMasterTabProperties="true" disabled="true" autoLoad="false"/>
		<adsm:tab title="dadosComplementares" id="DadosComplementares" src="/sim/consultarLocalizacoesMercadorias" cmd="complementosDadosComplementares" height="230" boxWidth="150" onShow="findPaginatedComplDados" disabled="true" autoLoad="false"/>
		<adsm:tab title="reembolso" id="ComplementosReembolso" src="/sim/consultarLocalizacoesMercadorias" cmd="complementosReembolso" height="230" disabled="true" onShow="findReembolso" autoLoad="false"/>
		<adsm:tab title="agendamentos" id="complementosAgendamentos" src="/sim/consultarLocalizacoesMercadorias" cmd="complementosAgendamentos" height="230" onShow="findAgendamentos" disabled="true" autoLoad="false"/>
		<adsm:tab title="outros" id="complementosOutros" src="/sim/consultarLocalizacoesMercadorias" cmd="complementosOutros" height="230" onShow="findComplOutros" autoLoad="false"/>
	</adsm:tabGroup>
	</adsm:form>
</adsm:window>   
<script>
function buscaIdDoctoServico(){
	var tabGroup = getTabGroup(this.document);
	var tabCompl = tabGroup.getTab('complementos');
	tabCompl.childTabGroup.selectTab('ComplementosObservacoes',{name:'tab_click'},true);	
	
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
	
		
	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findRespostaAbas", "retorno", {idDoctoServico:idDoctoServico}));
  	xmit();
  	
}

function retorno_cb(data, exception){
	var tabGroup = getTabGroup(this.document);
	var tabCompl = tabGroup.getTab('complementos');
	
	if(data != null){
		if(getNestedBeanPropertyValue(data,"abaEmb")!= undefined){	
			tabCompl.childTabGroup.setDisabledTab('ComplementosEmbalagens', false);
		}else{
		   tabCompl.childTabGroup.setDisabledTab('ComplementosEmbalagens', true);	
		}
		
		if(getNestedBeanPropertyValue(data,"abaDados")!= undefined)	
			tabCompl.childTabGroup.setDisabledTab('DadosComplementares', false);
		else
			tabCompl.childTabGroup.setDisabledTab('DadosComplementares', true);	
				
		
		if(getNestedBeanPropertyValue(data,"abaReemb")!= undefined)
			tabCompl.childTabGroup.setDisabledTab('ComplementosReembolso', false);
		else
			tabCompl.childTabGroup.setDisabledTab('ComplementosReembolso', true);	
			
			
		if(getNestedBeanPropertyValue(data,"abaAgend")!= undefined)	
			tabCompl.childTabGroup.setDisabledTab('complementosAgendamentos', false);
		else	
			tabCompl.childTabGroup.setDisabledTab('complementosAgendamentos', true);
			
		
		
  	  		
	}
}
</script>