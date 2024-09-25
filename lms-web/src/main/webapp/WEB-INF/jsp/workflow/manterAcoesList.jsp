<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.workflow.manterAcoesAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/workflow/manterAcoes">
		<adsm:textbox dataType="text" size="40" maxLength="60" property="dsTipoEvento" label="evento" labelWidth="15%" width="32%"/>
		<adsm:range label="dataLiberacao" width="38%">
			<adsm:textbox dataType="JTDate" property="dhLiberacaoInicial"   required="true" />
			<adsm:textbox dataType="JTDate" property="dhLiberacaoFinal"   required="true" />
		</adsm:range>
		<adsm:textbox dataType="integer" property="idProcesso" size="10" maxLength="10" label="processo" labelWidth="15%" width="32%"/>
		<adsm:lookup property="usuario"
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula"
					 serializable="true"
					 dataType="text"
					 label="solicitante"
					 size="15"
					 maxLength="20"
					 width="38%"
					 service="lms.workflow.manterAcoesAction.findLookupUsuarioFuncionario"
					 action="/configuracoes/consultarFuncionariosView">
			<adsm:propertyMapping relatedProperty="nmUsuario" modelProperty="nmUsuario"/>
			<adsm:textbox dataType="text" property="nmUsuario" size="24" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" size="40" maxLength="60" property="dsPendencia" label="descricaoProcesso" labelWidth="15%" width="32%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="acao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idAcao" property="acao" gridHeight="240" rows="11" selectionMode="none" scrollBars="both">
		<adsm:gridColumn width="132" title="dataLiberacao" property="dhLiberacao" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="185" title="evento" property="dsTipoEvento" dataType="text"/>
		<adsm:gridColumn width="50" title="processo" property="idProcesso" dataType="text" align="right"/>

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="30" title="" property="openProcesso" align="center" image="/images/popup.gif" imageLabel="visualizarProcesso" link="javascript:openProcesso(this); event.cancelBubble=true;" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn width="463" title="descricaoProcesso" property="novaDescricao" dataType="text" />

	</adsm:grid>
	<adsm:buttonBar/>
</adsm:window>
<script>
	function myPageLoad_cb(data, erro) {
		//onPageLoad_cb();
		// findButtonScript('acao', document.forms[0]);
	}

	function openProcesso(imagem){
		var refRow = imagem.parentElement.parentElement.parentElement.id;
		var objData = acaoGridDef.gridState.data[refRow.substring(refRow.indexOf("_row:")+5, refRow.length)];
		navigate(objData);
		return false;
	}

	function navigate(objData) {
		var url = objData.nmClasseVisualizacao + "&idProcessoWorkflow=" + objData.idProcesso;

		if(isNewFrontEnd(url)) {
			var contextPath = '<%=request.getContextPath()%>/';
			var completeURL = contextPath + url;
			window.open(completeURL, "_blank", "resizable=yes, scrollbars=yes");
		} else {
			showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:500px;');
		}
	}

</script>