<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.manterOcorrenciasEntregaAction">
	<adsm:form action="/entrega/manterOcorrenciasEntrega"
		onDataLoadCallBack="pageLoad"
		idProperty="idOcorrenciaEntrega"
	>
		<adsm:textbox dataType="integer" property="cdOcorrenciaEntrega" label="codigoOcorrencia" size="5" maxLength="3" labelWidth="20%" width="20%" required="true" />
		<adsm:textbox dataType="text" property="dsOcorrenciaEntrega" label="descricaoOcorrencia" maxLength="60" size="40" labelWidth="20%" width="40%" required="true" />

		<adsm:combobox property="tpOcorrencia" domain="DM_TIPO_OCORRENCIA_ENTREGA" label="tipoOcorrencia" required="true" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="20%"/>

		<adsm:hidden property="evento.tpSituacao" value="A"/>

		<adsm:lookup
			property="evento"
			idProperty="idEvento"
			criteriaProperty="cdEvento"
			service="lms.entrega.manterOcorrenciasEntregaAction.findLookupEventoAssociado"
			action="/sim/manterEventosDocumentosServico"
			dataType="integer"
			serializable="true"
			label="eventoAssociado"
			width="8%"
			size="3"
			labelWidth="20%"
			maxLength="3"
		>
			<adsm:propertyMapping criteriaProperty="evento.tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="evento.dsEvento" modelProperty="dsEvento"/>
			<adsm:textbox dataType="text" property="evento.dsEvento" disabled="true" size="43" width="20%" serializable="false"/>
		</adsm:lookup>

		<adsm:checkbox property="blDescontoDpe" onclick="disableOcorrenciaPendencia()" label="descontoDPE" labelWidth="20%" width="20%"/>
		<adsm:checkbox property="blOcasionadoMercurio" onclick="disableOcorrenciaPendencia()" label="ocasionadoPelaMercurio" labelWidth="20%" width="30%"/>

		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="20%" required="true"/>
		
		<adsm:lookup property="ocorrenciaPendencia" 
					 idProperty="idOcorrenciaPendencia" 
					 criteriaProperty="cdOcorrencia" 
					 label="ocorrenciaPendencia"
					 action="/pendencia/manterOcorrenciasPendencia" 
					 service="lms.entrega.manterOcorrenciasEntregaAction.findLookupOcorrenciaPendencia"
					 cmd="list" 
					 size="3" 
					 maxLength="3"
					 dataType="integer"
					 labelWidth="20%"					 
					 width="80%"
					 >
			<adsm:hidden property="ocorrenciaPendencia.tpSituacao" value="A"/>
			<adsm:propertyMapping criteriaProperty="ocorrenciaPendencia.tpSituacao" modelProperty="tpSituacao" />
			<adsm:hidden property="ocorrenciaPendencia.tpOcorrencia" value="B"/>
			<adsm:propertyMapping criteriaProperty="ocorrenciaPendencia.tpOcorrencia" modelProperty="tpOcorrencia" />
			<adsm:hidden property="ocorrenciaPendencia.blDescontaDpe" value="S"/>
			<adsm:propertyMapping criteriaProperty="ocorrenciaPendencia.blDescontaDpe" modelProperty="blDescontaDpe" />
			<adsm:textbox property="ocorrenciaPendencia.dsOcorrencia" size="40" disabled="true" dataType="text" serializable="false" />
			<adsm:propertyMapping relatedProperty="ocorrenciaPendencia.dsOcorrencia" modelProperty="dsOcorrencia"/>
		</adsm:lookup>
			
		
		<adsm:checkbox property="blContabilizarEntrega" label="contabilizarEntrega" width="30%" labelWidth="20%"/>
		<adsm:checkbox property="blContabilizarTentativaEntrega" label="contabilizarTentativaEntrega" width="50%" labelWidth="20%"/>
		
		
		
		<adsm:buttonBar>
			<adsm:button caption="salvar" onclick="store()" id="storeButtom"/>
			<adsm:button id="botaoLimpar" caption="limpar" onclick="limpar_OnClick();" disabled="false" buttonType="newButton"/>
			<adsm:button id="botaoExcluir" caption="excluir" buttonType="removeButton" onclick="excluir();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function excluir() {
		removeButtonScript('lms.entrega.manterOcorrenciasEntregaAction.removeById', 'myNewButtonScript', 'idOcorrenciaEntrega', this.document);
	}

	function myNewButtonScript_cb(data,exception){
		if (exception != undefined){
			alert(exception);
		} else {
			newButtonScript();
			setDisabled("botaoExcluir", true);
			setDisabled("storeButtom", false);
		}
		setFocusOnFirstFocusableField(document);
	}

	function limpar_OnClick() {
		// Chama funcao limpar padrao do botao limpar
		newButtonScript(this.document, true, {name:'newButton_click'});
		setDisabled("storeButtom", false);
		setFocusOnFirstFocusableField(document);
		disableOcorrenciaPendencia();
	}

	function pageLoad_cb(data, error) {
		onDataLoad_cb(data,error);
		setDisabled("cdOcorrenciaEntrega",true);
		if (getElementValue("idOcorrenciaEntrega") == "") {
			setDisabled("cdOcorrenciaEntrega",false);
		}
		disableOcorrenciaPendencia();
	}

	function disableOcorrenciaPendencia(){
		
		var blDescontoODT = getElementValue("blDescontoDpe");
		var blOcasionadoTNT = getElementValue("blOcasionadoMercurio");
		if(blDescontoODT && !blOcasionadoTNT){
			setDisabled("ocorrenciaPendencia.idOcorrenciaPendencia",false);
		}else{
			resetValue("ocorrenciaPendencia.idOcorrenciaPendencia");
			setDisabled("ocorrenciaPendencia.idOcorrenciaPendencia",true);
		}	
	}

	// Faz a verificacao e salva os dois forms 
	function store() {
		storeEditGridScript('lms.entrega.manterOcorrenciasEntregaAction.store', 'afterStore', document.forms[0]);
	}

	// Método é chamado depois que é gravado os dados no banco
	function afterStore_cb(data,error){
		store_cb(data,error);
		setFocus("botaoLimpar", false);
		disableOcorrenciaPendencia();
	}

	function initWindow(evento) {
		if (evento.name == "newButton_click" || evento.name == "tab_click") {
			setDisabled("cdOcorrenciaEntrega", false);
			setDisabled("storeButtom", false);
		}
		disableOcorrenciaPendencia();
	}

</script>
