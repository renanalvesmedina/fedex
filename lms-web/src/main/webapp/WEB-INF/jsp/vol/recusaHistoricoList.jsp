<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script>
function myOnPageLoad(){
		onPageLoad();
		var url = new URL(parent.location.href);
		var idRecusa = url.parameters["idRecusa"];
        var frota 	= url.parameters["frota"];
        var documento = getFormattedValue("integer", url.parameters["documento"], "0000000000", true);
        var numero  = url.parameters["numero"];
         
        setElementValue('idRecusa',idRecusa);        
        setElementValue('frota',frota);
        setElementValue('documento',documento);
        
        if(numero && numero !=  "undefined"){
        setElementValue('numero',numero);    
        }else{
        	setElementValue('numero','');
        }
        
        findButtonScript('recusas', document.forms[0]);
     }
</script>

<adsm:window title="recusa" service="lms.vol.recusaHistoricoAction" onPageLoad="myOnPageLoad">
	<adsm:form action="/vol/recusaHistorico">

		<adsm:textbox property="documento" dataType="text" label="ctrc"
			maxLength="10" size="10" width="60%" disabled="true" />
		<adsm:textbox property="frota" dataType="text" label="frota"
			maxLength="10" size="6" width="60%" disabled="true"
			serializable="false" />
		<adsm:textbox property="numero" dataType="text" label="numeroEquipamento"
			maxLength="15" size="15" width="60%" disabled="true" />
		<adsm:hidden property="idRecusa"/>

	</adsm:form>
	
	<adsm:i18nLabels>
		<adsm:include key="resolvidoMercurio"/>
	</adsm:i18nLabels>

	<adsm:grid property="recusas" idProperty="idRecusa" disableMarkAll="true" onPopulateRow="populateRow"
		selectionMode="none" rows="12" unique="true" onRowClick="disableClick" 
		service="lms.vol.recusaHistoricoAction.findPaginatedRecusaHistorico" rowCountService="lms.vol.recusaHistoricoAction.getRowCountRecusaHistorico" >
		
		<adsm:gridColumn property="status" title="status" width="5%" image="/images/bandeira_verde.gif" align="center" imageLabel="resolvidoMercurio"/>
		<adsm:gridColumn property="data" dataType="JTDateTimeZone" title="data" width="15%"/>
		<adsm:gridColumn property="tpRecusa" title="tipo" width="15%"/>
		<adsm:gridColumn property="responsavel" title="responsavel" width="30%" />
		<adsm:gridColumn property="observacao" title="observacao" width="35%" />
		
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>	
	function populateRow(tr,data) { 
    	var status = getNestedBeanPropertyValue(data,"istatus");    
    	var tpRecusa = getNestedBeanPropertyValue(data,"tpRecusa");
    
    	 //Troca pra vazio se não tiver status
         if (status == undefined || status == '') {
            tr.children[0].innerHTML = "<NOBR></NOBR>";
         }
    	
    	 if(status != 'M'){
    	 	   tr.children[0].innerHTML = tr.children[0].innerHTML.replace(i18NLabel.getLabel("resolvidoMercurio"),tpRecusa);
    	 }
    	
    	  //Altera a coluna do Status da Entrega
		 if(status == 'N') {
		 	tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_verde.gif","bandeira_vermelha.gif"); 		 
		 }
		 else {
		 	if(status == 'C') {
		 		tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_verde.gif","bandeira_amarela.gif"); 
		 	}
		 	else {
		 		if(status == 'D'  || status == 'R') {
		 			tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_verde.gif","bandeira_azul.gif"); 
		 		}
		 	}
		 }
    	
    }
    

	function disableClick() {
		return false;
	}
	
</script>
