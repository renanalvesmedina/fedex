<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioLMSEmpresaFilialAction" onPageLoad="populaGrid">
	<adsm:form action="/seguranca/manterUsuarioLMSEmpresaFilial" >
		<adsm:hidden property="idEmpresaUsuario" />
		<adsm:grid 	idProperty="idFilialRegional" 
					property="gridUsuario"  
					autoSearch="true" 
					gridHeight="380"
					onRowClick="fakeClick"					
			   	rows="12"  
			   	selectionMode="none"
			   	service="lms.seguranca.manterUsuarioLMSEmpresaFilialAction.findPaginated"
			   	rowCountService="lms.seguranca.manterUsuarioLMSEmpresaFilialAction.getRowCount">
				   				   
			<adsm:editColumn property="tipoReg" dataType="text" field="hidden" title="tipo"/>
   		<adsm:gridColumn property="nmTipoReg" title="tipo" width="30%" />		
			<adsm:gridColumn property="nmFilialRegional" title="descricao" />					   
			<adsm:editColumn property="blAprovaWorkflow" width="20%" title="abrangeWorkflow" field="combobox" required="true" align="center" domain="DM_SIM_NAO"/>
			<adsm:buttonBar>
			    <adsm:storeButton id="btnSalvar" disabled="false"/>
			</adsm:buttonBar>
		</adsm:grid>
	</adsm:form>	
</adsm:window>

<script>
/**
 * Função chamada ao entrar na tela
 */
function populaGrid() {
	onPageLoad();
	findButtonScript('gridUsuario', this.document.forms[0]);
	setDisabled('btnSalvar',false);
}

function fakeClick(data) {
	return false;
}

function back() {
	parent.top.body.backPage()
}


</script>
