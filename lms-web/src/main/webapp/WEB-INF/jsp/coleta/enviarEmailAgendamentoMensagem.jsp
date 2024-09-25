<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>


<adsm:window service="lms.coleta.enviarEmailAgendamentoAction" onPageLoad="carregaPagina" >
	<adsm:form action="/coleta/enviarEmailAgendamento" >
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-09130"/>
			<adsm:include key="LMS-09135"/>
			<adsm:include key="LMS-09129"/>
	   	</adsm:i18nLabels>

		<adsm:hidden property="enviarEmail.chavesNFe" />
		<adsm:hidden property="idAgendamentoEntrega"/>
		<adsm:hidden property="enviarEmail.idTemplateRelatorio" />

 		<adsm:textbox dataType="text" label="de" size="100" property="enviarEmail.de" maxLength="200" labelWidth="8%" width="80%" required="true" disabled="true" />
		
		<adsm:textbox dataType="text" label="para" size="100" property="enviarEmail.para" maxLength="200" labelWidth="8%" width="80%" required="true" disabled="false" />

		<adsm:section caption="branco" />
		
		<adsm:label key="anexo" width="14%">
			<adsm:checkbox property="enviarEmail.anexarDanfe" label="danfe" labelWidth="5%" width="5%" disabled="true"  /> 
			<adsm:checkbox property="enviarEmail.anexarXml" label="xmlTexto" labelWidth="5%" width="5%" disabled="true"  /> 
			<adsm:checkbox property="enviarEmail.anexarRelatorioAgendamento" label="relatorioAgendamento" labelWidth="17%" width="17%" disabled="true"  />	
		</adsm:label>
		
		<adsm:textbox dataType="text" label="assunto" size="100" property="enviarEmail.assunto" maxLength="100" labelWidth="8%" width="80%" required="true" disabled="false" />
	
		<adsm:textarea label="texto" property="enviarEmail.texto" maxLength="5000" style="width:522px; height:68px;" labelWidth="8%" width="83%" labelStyle="vertical-align:top" disabled="false" />
		
		

	</adsm:form>
	
	<adsm:buttonBar>
		<adsm:button caption="enviar" disabled="false" onclick="enviarEmail();" />
		<adsm:button caption="voltar" disabled="false" onclick="self.close();"/>
	</adsm:buttonBar>
</adsm:window>

<script type="text/javascript">


	function carregaPagina() {	
 		var url = new URL(parent.location.href);
		
		if (url.parameters["param"] != undefined){
		    //Se o parâmetro foi informado   
		    nrChaves = url.parameters["param"];
		    setElementValue("enviarEmail.chavesNFe", nrChaves);
		    
		    var idAgendamentoEntrega = url.parameters["idAgendamentoEntrega"];
			if(idAgendamentoEntrega != undefined){
				setElementValue("idAgendamentoEntrega",idAgendamentoEntrega);
			}
			
			var dtAgendamentoVar = url.parameters["dtAgendamento"];
		}
		
	
		var sdo = createServiceDataObject("lms.coleta.enviarEmailAgendamentoAction.loadPage", "loadPage", { parametro:nrChaves, idAgendamentoEntrega:getElementValue('idAgendamentoEntrega'), dtAgendamento: dtAgendamentoVar });
	   	xmit({serviceDataObjects:[sdo]}); 
	}

 	function loadPage_cb(data, error){
		if (error != undefined) {
			alert(error);
			return;
		}
		else{
			setElementValue("enviarEmail.de", data.infoEmail.de);
			setElementValue("enviarEmail.para", data.infoEmail.para);
			setElementValue("enviarEmail.assunto", data.infoEmail.assunto);
			setElementValue("enviarEmail.texto", data.infoEmail.texto);
			setElementValue("enviarEmail.anexarDanfe", (data.infoEmail.anexarDanfe == 'true') ? true : false);
			setElementValue("enviarEmail.anexarXml", (data.infoEmail.anexarXml == 'true') ? true : false);
			
			if(data.infoEmail.idTemplateRelatorio != null ){
				setElementValue("enviarEmail.anexarRelatorioAgendamento", true);
				setElementValue("enviarEmail.idTemplateRelatorio", data.infoEmail.idTemplateRelatorio);
			}else{
				setElementValue("enviarEmail.anexarRelatorioAgendamento", false);
				setElementValue("enviarEmail.idTemplateRelatorio", '');
			}
		}
	}
	
	function enviarEmail(){
		if(getElementValue("enviarEmail.para") == ""){
			alert("LMS-09129 - " + i18NLabel.getLabel("LMS-09129"));
			return;
		}
		
		storeEditGridScript("lms.coleta.enviarEmailAgendamentoAction.enviarEmail", "enviarEmail", document.forms[0], document.forms[1]);
	}
	
	function enviarEmail_cb(data, error){
		if(error != undefined){
			alert(error);
			return;
		}else{
			if(data.error == "true"){
				alert("LMS-09130 - " + i18NLabel.getLabel("LMS-09130"));
			}
			else{
				alert("LMS-09135 - " + i18NLabel.getLabel("LMS-09135"));
			}
		}
	} 
	
</script>