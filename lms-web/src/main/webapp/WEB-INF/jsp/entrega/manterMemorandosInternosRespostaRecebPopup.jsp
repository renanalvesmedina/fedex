<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.municipios.manterRotasViagemAction" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/municipios/manterRotasViagem" idProperty="idMir" >
		<adsm:hidden property="filial.idFilial" />
		<adsm:textbox dataType="text" property="filial.sgFilial"
    			label="filial" size="3" labelWidth="20%" width="80%" disabled="true" required="true" serializable="false" >
         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"
         			size="30" disabled="true" serializable="false" />
         	<adsm:hidden property="filial.siglaNomeFilial" />
	    </adsm:textbox>		
		
		<adsm:combobox property="setor.idSetor" optionProperty="idSetor" optionLabelProperty="dsSetor"
				service="lms.entrega.manterMemorandosInternosRespostaAction.findComboSetor" onlyActiveValues="true"
				label="setor" labelWidth="20%" width="80%" required="true" onchange="onSetorChange(this);" >
			<adsm:propertyMapping relatedProperty="cdSetor" modelProperty="cdSetorRh" />
		</adsm:combobox>
		<adsm:hidden property="cdSetor" serializable="false" />

		<adsm:lookup dataType="text" property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula"
			     service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupUsuarioFuncionario" 
			     action="/configuracoes/consultarFuncionariosView"
			     label="funcionario" size="16" maxLength="16" labelWidth="20%" width="80%" >				
			<adsm:propertyMapping criteriaProperty="cdSetor" modelProperty="codSetor.codigo" />
						
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" 
					modelProperty="filial.sgFilial" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" 
					modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
			
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>				
			<adsm:textbox dataType="text" property="usuario.nmUsuario" 
					size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>
	
		<adsm:buttonBar>
			<adsm:button caption="salvar" service="lms.entrega.manterMemorandosInternosRespostaAction.storeClonarMirRecebimento"
					callbackProperty="store" disabled="false" />			
			<adsm:button caption="fechar" onclick="self.close();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 
<script>
	function store_cb(dados, erros) {
		if (erros == undefined) {
			resetValue(dialogArguments.document);
			dialogArguments.dataLoadCustom_cb(dados, erros);
			window.close();
		} else
			alert(erros);
	}

	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		
		var idMir = dialogArguments.document.forms[0].elements["idMir"].value;
		setElementValue("idMir",idMir);
		
		var data = new Array();
		setNestedBeanPropertyValue(data,"idMir",idMir);
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.findFilialDestinoByMir",
				"findFilialDestinoByMir",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findFilialDestinoByMir_cb(data,error) {
		onDataLoad_cb(data,error);
	}
	
	function onSetorChange(elem) {
		resetValue("usuario.idUsuario");
		comboboxChange({e:elem});
	}
		
</script>  