<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script type="text/javascript">
	function loadFilialUsuario() {
    	var data = new Array();
		var sdo = createServiceDataObject("lms.coleta.manterFuncionariosRegiaoAction.findFilialUsuarioLogado", "loadFilialUsuario", data);
    	xmit({serviceDataObjects:[sdo]});

	}
	
	var dataUsuario;
	function loadFilialUsuario_cb(data, error) {
		dataUsuario = data;
		onPageLoad();
	}
</script>
<adsm:window service="lms.coleta.manterFuncionariosRegiaoAction" onPageLoad="loadFilialUsuario">
	<adsm:form action="/coleta/manterFuncionariosRegiao" idProperty="idFuncionarioRegiao">
		
		<adsm:hidden property="regiaoColetaEntregaFil.filial.idFilial"/>
		<adsm:textbox dataType="text" property="regiaoColetaEntregaFil.filial.sgFilial" 
					  label="filial" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="regiaoColetaEntregaFil.filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>
			
		<adsm:lookup property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula" required="true"
					 dataType="text" label="funcionario" size="16" maxLength="16" labelWidth="18%" width="82%"  
				     service="lms.coleta.manterFuncionariosRegiaoAction.findLookupUsuarioFuncionario" 
				     action="/configuracoes/consultarFuncionariosView">
			
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
			<adsm:propertyMapping criteriaProperty="regiaoColetaEntregaFil.filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="regiaoColetaEntregaFil.filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="regiaoColetaEntregaFil.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>
		

		<adsm:combobox property="regiaoColetaEntregaFil.idRegiaoColetaEntregaFil" optionLabelProperty="dsRegiaoColetaEntregaFil" optionProperty="idRegiaoColetaEntregaFil" 
					   service="lms.coleta.manterFuncionariosRegiaoAction.findRegiaoColetaEntregaFil" 
					   required="true" label="regiaoColeta" labelWidth="18%" width="82%" />
					   
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	// document.getElementById("usuario.funcionario.idChapa").style.textAlign = "right";

	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click") {
			setElementValue("regiaoColetaEntregaFil.filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("regiaoColetaEntregaFil.filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("regiaoColetaEntregaFil.filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
		}
	}
	
	/* function lookupFuncionariosOnChange() {
		document.getElementById("usuario.funcionario.idChapa").value = formatNumber(document.getElementById("usuario.funcionario.idChapa").value, "000000000");
		var result = usuario_funcionario_idChapaOnChangeHandler();
		return result;
	}
	
	function onDataLoadFuncionario_cb(data, error) {
		usuario_funcionario_idChapa_exactMatch_cb(data);
		if (data[0]==undefined){
			setFocus(document.getElementById("usuario.funcionario.idChapa"));
		}
	} */
</script>