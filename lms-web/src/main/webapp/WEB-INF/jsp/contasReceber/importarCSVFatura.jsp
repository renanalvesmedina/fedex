<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:i18nLabels>
		<adsm:include key="LMS-36283"/>
		<adsm:include key="LMS-36292"/>
	</adsm:i18nLabels>

	<adsm:form action="/contasReceber/manterFaturas">

		<adsm:hidden property="idFatura"/>
		<adsm:textbox property="dcArquivo" 
			dataType="file" 
			label="arquivoCSV"
			labelWidth="18%" 
			width="82%" 
			size="60" 
			required="true"
			serializable="true" 
			picker="false" />

		<adsm:textarea width="400" label="resultadoImportacao"
			labelWidth="180" columns="85" property="resultadoValidacao" rows="15"
			maxLength="400" required="false" disabled="true"/>

		<adsm:buttonBar lines="1">
		
			<adsm:button 
				id="importarArquivo" 
				onclick="confirmImportar();" 
				caption="importarArquivo" 
				disabled="false"/>
			<adsm:button id="fechar" caption="fechar" onclick="self.close();"
				disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>


<script>
	function importarArquivo_cb(dados, erros) {			
		if(dados.resultadoValidacao == undefined || dados.resultadoValidacao == ''){
			alert(i18NLabel.getLabel("LMS-36283"));
			window.close();
		} else {
			setElementValue("resultadoValidacao", dados.resultadoValidacao);
		}
	}

	function confirmImportar() {
		var dcArquivoValue = getElementValue("dcArquivo");
		if(dcArquivoValue != '') {
			var confirmaImportacao = confirm(getI18nMessage("LMS-36292"));
			if (!confirmaImportacao) {
				window.close();
			} else {
				setElementValue("resultadoValidacao", "");
				var data = new Array();
				setNestedBeanPropertyValue(data, "idFatura", getElementValue("idFatura"));
				setNestedBeanPropertyValue(data, "dcArquivo", getElementValue("dcArquivo"));
				var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.importFaturasDesconto","importarArquivo", data);
				xmit({serviceDataObjects:[sdo]});
			}
		} else { alert ("Arquivo CSV obrigatório!"); }
	}

	var u = new URL(parent.location.href);
	setElementValue("idFatura", u.parameters["idFatura"]);
	
	//sobrescrita do componente para uso em modais
   	resetValue('dcArquivo');
 	setDisplay("dcArquivo_picker", false);
	var imgDelete = document.getElementById('dcArquivo_delete');
	var imgDeleteParent = imgDelete.parentNode;
	imgDeleteParent.removeChild(imgDelete);
	var a = document.createElement('a');
	a.href = 'javascript:void(0)';
	a.id = 'dcArquivo_delete';
	imgDeleteParent.appendChild(a);
	

	//sobrescrita do componente para uso em modais
	function browseEnhanced(widgetName, me) {
		me.document.getElementById(widgetName).click(); 
		if (me.document.getElementById(widgetName).value != '') {
			setDisplay(me.document.getElementById(widgetName), true); 
			setDisplay(me, false);
			//getTab(document).setChanged(true);
		}
	}
	
	//fixme: hotfix para uso em modais
	setInterval(function(){
		setDisplay('dcArquivo', true);
		setDisplay('dcArquivo_browse', false);
	}, 10);
</script>