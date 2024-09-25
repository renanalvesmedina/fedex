
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
		carregaDadosUsuario();
		onPageLoad();
	}
</script>
<adsm:window service="lms.coleta.manterFuncionariosRegiaoAction" onPageLoad="loadFilialUsuario">
	<adsm:form action="/coleta/manterFuncionariosRegiao">
	
		<adsm:hidden property="regiaoColetaEntregaFil.filial.idFilial"/>
		<adsm:textbox dataType="text" property="regiaoColetaEntregaFil.filial.sgFilial" 
					  label="filial" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="regiaoColetaEntregaFil.filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		<adsm:lookup property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula" 
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
					   label="regiaoColeta" labelWidth="18%" width="82%" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="funcionariosRegiao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="funcionariosRegiao"  idProperty="idFuncionarioRegiao" service="lms.coleta.manterFuncionariosRegiaoAction.findPaginatedFuncionariosRegiao"
			rowCountService="lms.coleta.manterFuncionariosRegiaoAction.getRowCountFuncionariosRegiao"
			defaultOrder="usuario_.nmUsuario,regiaoColetaEntregaFil_.dsRegiaoColetaEntregaFil:asc" rows="12">
		<adsm:gridColumn property="usuario.nrMatricula" title="matricula" width="15%"/>
		<adsm:gridColumn property="usuario.nmUsuario" title="funcionario" width="45%"/>
		<adsm:gridColumn property="regiaoColetaEntregaFil.dsRegiaoColetaEntregaFil" title="regiaoColeta" width="40%"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	
	function initWindow(eventObj) {
		if (dataUsuario!=undefined) carregaDadosUsuario();
	}
	
	function carregaDadosUsuario() {
		setElementValue("regiaoColetaEntregaFil.filial.idFilial", dataUsuario.filial.idFilial);
		setElementValue("regiaoColetaEntregaFil.filial.sgFilial", dataUsuario.filial.sgFilial);
		setElementValue("regiaoColetaEntregaFil.filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
	}
	
</script>