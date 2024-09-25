<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>	

<script type="text/javascript">

	function myOnPageLoad(){
		onPageLoad();
        self.resizeTo(800,600);
		var url = new URL(parent.location.href);
		var idRecusa = url.parameters["idRecusa"];
		
	    setElementValue('idRecusa',idRecusa); 
	   
	   	findRecusa(idRecusa)
	   	findAcao(); 
	       
	}

</script>

				  
<adsm:window service="lms.vol.recusaTratativasClienteAction" onPageLoad="myOnPageLoad" title="tratativasCliente">
	<adsm:form action="/vol/recusaTratativas" idProperty="idTratativa" >

		<adsm:hidden property="idRecusa" />
		<adsm:hidden property="nrIdentificacao"/>
		<adsm:hidden property="dhRecusa"/>
		<adsm:hidden property="motivo"/>
		<adsm:hidden property="idFilial"/>
		<adsm:textbox property="documento"  label="ctrc" dataType="text" maxLength="10" size="12" width="70%" disabled="true"/>	
		<adsm:textbox property="destinatario" label="destinatario" dataType="text" size="50" width="70%" disabled="true"/>
		<adsm:combobox property="emailContato" optionProperty="idEmailRecusa" optionLabelProperty="nmContato" label="emailContatoDestinatario" width="70%" required="true"
			 cellStyle="vertical-align:bottom;" boxWidth="200"/>			 
		<adsm:combobox property="acao" label="acao" optionProperty="valueAcao" optionLabelProperty="dsAcao" required="true" width="70%" boxWidth="200"/>
		<adsm:textarea property="observacoes" columns="40" rows="4" width="85%" maxLength="255" label="observacoes" required="true" />
			
		<adsm:buttonBar>
			<adsm:storeButton id="salvar" callbackProperty="agradeceUsuario"/>
			<adsm:button id="fechar" caption="fechar" onclick="fecharPagina()" disabled="false"/> 
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-41013"/>
	</adsm:i18nLabels>
	
</adsm:window>

<script>
	
	 /*
	 * Retorna os dados da recusa e os contatos 
	 */
	function findRecusa(idRecusa){
		var data = new Object();
		data.idRecusa = idRecusa;
	
		var sdo = createServiceDataObject("lms.vol.recusaTratativasClienteAction.findRecusa", "populaCampos" , data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function populaCampos_cb(data){
	
		if (data._exception==undefined) {
			setElementValue('documento',getFormattedValue("integer", data[0].documento, "0000000000", true) );
			setElementValue('destinatario',data[0].destinatario);
			setElementValue('nrIdentificacao',data[0].nrIdentificacao);
			setElementValue('dhRecusa', getFormattedValue("JTDateTimeZone", data[0].dhRecusa, "dd-MM-yyyy", true));
			setElementValue('motivo',data[0].motivo);
			setElementValue('idFilial',data[0].idFilial);
		
			emailContato_cb(data);
		}else {
			alert(data._exception._message);
		}
	}
	
	
	function findAcao(){
		var data = new Object();
		var sdo = createServiceDataObject("lms.vol.recusaTratativasAction.findComboAcao", "populaComboAcao" , data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function populaComboAcao_cb(data){
		acao_cb(data);
	}
		
	function agradeceUsuario_cb(data, error){
		if (error == undefined){
			document.getElementById("salvar").disabled = true;
			alert(i18NLabel.getLabel("LMS-41013"));	
		}
			fecharPagina();
	}
	
		/**
	 * fecha a atual janela
	 */
	function fecharPagina(){
		window.close();
	}
	
</script>
