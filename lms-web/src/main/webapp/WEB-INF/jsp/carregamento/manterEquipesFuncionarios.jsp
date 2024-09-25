<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.manterEquipesAction" > 

	<adsm:form action="/carregamento/manterEquipes" idProperty="idIntegranteEquipe"
			   service="lms.carregamento.manterEquipesAction.findByIdIntegranteEquipeFuncionario">

		<adsm:masterLink showSaveAll="true" idProperty="idEquipe">
			<adsm:masterLinkItem property="filial.sgFilial" label="filial" itemWidth="20" />
			<adsm:masterLinkItem property="setor.idSetor" label="setor" itemWidth="30" />
			<adsm:masterLinkItem property="dsEquipe" label="descricaoEquipe" itemWidth="50"/>
			<adsm:hidden property="tpIntegrante" value="F"/>
		</adsm:masterLink>

		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />
		<adsm:hidden property="tpIntegrante" value="F" />

		<adsm:hidden property="usuario.dsFuncao"/>
		<adsm:lookup property="usuario" label="funcionario" 
					 idProperty="idUsuario" criteriaProperty="nrMatricula" 
                     service="lms.carregamento.manterEquipesAction.findLookupUsuarioFuncionario" 
            		 action="/configuracoes/consultarFuncionariosView"
            		 dataType="text" size="10" maxLength="16" width="85%" required="true">
        	<adsm:propertyMapping modelProperty="nmUsuario" relatedProperty="usuario.nmUsuario" />
        	<adsm:propertyMapping modelProperty="dsFuncao" relatedProperty="usuario.dsFuncao" />
        	<adsm:propertyMapping modelProperty="nmUsuario" criteriaProperty="usuario.nmUsuario" disable="false" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialUsuario.idFilial" disable="true" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialUsuario.sgFilial" disable="true" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" disable="true" />
        	<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" />
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarFuncionario" service="lms.carregamento.manterEquipesAction.saveIntegranteEquipeFuncionario" />
			<adsm:newButton caption="limpar"/>			
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="equipe"
			   idProperty="idIntegranteEquipe" detailFrameName="funcionarios"
			   autoSearch="false" rows="11" defaultOrder="idIntegranteEquipe:asc"
			   selectionMode="check" showPagging="true" showGotoBox="true"
			   service="lms.carregamento.manterEquipesAction.findPaginatedIntegranteEquipeFuncionario"
			   rowCountService="lms.carregamento.manterEquipesAction.getRowCountIntegranteEquipeFuncionario"
			   unique="true" >
		<adsm:gridColumn title="matricula" 	property="usuario.nrMatricula" width="25%" align="right" />
		<adsm:gridColumn title="nome" 		property="usuario.nmUsuario" width="75%" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirFuncionario" service="lms.carregamento.manterEquipesAction.removeByIdsIntegranteEquipe" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

document.getElementById("usuario.nrMatricula").required="true";

function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		if (getElementValue("filialUsuario.idFilial") == "")
			loadFilialSessao();
	}
}

/**
 * Função que irá buscar a filial do usuário logado para posteriormente
 * popular um campo hidden utilizado na lookup de funcionário.
 */
function loadFilialSessao(){
	var sdo = createServiceDataObject("lms.carregamento.manterEquipesAction.getFilialUsuarioLogado", "loadFilial");
   	xmit({serviceDataObjects:[sdo]});
}

function loadFilial_cb(data, error) {
   	if (data.filialSessao) {
		setElementValue("filialUsuario.idFilial", data.filialSessao.idFilial);
		setElementValue("filialUsuario.sgFilial", data.filialSessao.sgFilial);
		setElementValue("filialUsuario.pessoa.nmFantasia", data.filialSessao.pessoa.nmPessoa);
	}
}
</script>