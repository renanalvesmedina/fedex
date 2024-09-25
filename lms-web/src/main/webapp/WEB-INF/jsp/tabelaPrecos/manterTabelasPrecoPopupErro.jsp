<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:i18nLabels>
		<adsm:include key="LMS-00070"/>
	</adsm:i18nLabels>
	<adsm:form action="/tabelaPrecos/manterTabelasPrecoPopupErro">

		<adsm:hidden property="idTabelaPreco"/>
		<adsm:textbox property="dcArquivo" 
			dataType="file" 
			label="arquivo"
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

	function importarArquivoTabelaPreco_cb(data, erros) {	
		var retorno = [];
		retorno.action = "retornoImportar";
		if (erros != undefined){
			alert(erros);
			window.returnValue = retorno;
			window.close();
		}else{
			if (data.resultadoValidacao) {
				setElementValue("resultadoValidacao", data.resultadoValidacao);
			}	
			if (data.sucesso) {
				alert(data.sucesso);
				if (data.resultadoAtualizacaoAutomatica) {
					alert(data.resultadoAtualizacaoAutomatica);
				}
				window.returnValue = retorno;
				window.close();
			} 
		}
		
	}

	function confirmImportar() {
		var dcArquivoValue = getElementValue("dcArquivo");
		if(dcArquivoValue != '') {
			setElementValue("resultadoValidacao", "");
			storeButtonScript("lms.tabelaprecos.importacaoTabelaPrecoAction.importarTabela", "importarArquivoTabelaPreco", document.forms[0]);
		} else { 
			alertI18nMessage("LMS-00070");
		}
	}

	var u = new URL(parent.location.href);
	setElementValue("idTabelaPreco", u.parameters["idTabelaPreco"]);
	
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
	
	//fixme: hotfix para uso em modais (goHorseBaby)
	setInterval(function(){
		setDisplay('dcArquivo', true);
		setDisplay('dcArquivo_browse', false);
	}, 10);
</script>