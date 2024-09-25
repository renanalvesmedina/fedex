<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioAction" >
	<adsm:form action="/seguranca/manterUsuario" idProperty="idUsuario"  onDataLoadCallBack="desabilita">

		<adsm:lookup size="40" maxLength="40" width="80%" labelWidth="125"
					 idProperty="chapa"
					 property="usuario.funcionario" 
					 criteriaProperty="idChapa" 
					 action="/configuracoes/consultarFuncionarios" 
					 service="lms.seguranca.manterUsuarioAction.findLookupUsuarioADSM"
					 dataType="integer"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 label="usuarioADSM" 
					 required="true">
					 
		</adsm:lookup>
    
<!-- 	<adsm:textbox dataType="text" property="nmUsuario" label="usuario" width="50%" size="40" maxLength="60" labelWidth="120" required="true"/> 

		<adsm:textbox dataType="text" property="dsEmail" label="email" width="40%" size="40" maxLength="60" labelWidth="120"/> -->
<!-- 	<adsm:combobox property="tpCategoriaUsuario" label="categoria" domain="DM_CATEGORIA_USUARIO" labelWidth="100" required="true"/>
		<adsm:textbox dataType="password" property="dsSenha" label="senha" width="13%" size="8" maxLength="20" labelWidth="100" required="true"/>
		<adsm:textbox dataType="password" property="dsSenhaVerify" label="confirmacaoSenha" maxLength="20" labelWidth="150" width="12%" size="8" required="true"/>
	 	<adsm:combobox property="tpLinguagem" label="idioma" domain="TP_LINGUAGEM" width="15%" labelWidth="100" required="true"/>  -->
		<adsm:checkbox property="blIrrestritoCliente" label="acessoIrrestritoClientes" onclick="desabilitaLookup();" labelWidth="180" width="10%"/>
<!-- 	<adsm:checkbox property="blIrrestritoFilial" label="acessoIrrestritoFiliais" onclick="desabilitaLookup();" labelWidth="20%" width="10%"/>  
		<adsm:checkbox property="blAdministrador" label="admin" labelWidth="12%" width="10%"/>  -->


		<adsm:listbox label="cliente" size="4" boxWidth="170" width="40%" labelWidth="125"
	         			property="clienteUsuario" 
	     				optionProperty="idClienteUsuario"
	     				optionLabelProperty="pessoa.nmPessoa">
				<adsm:lookup property="cliente" 
							 idProperty="idCliente"
							 criteriaProperty="pessoa.nmPessoa"
							 action="/vendas/manterDadosIdentificacao"
							 service="lms.vendas.clienteService.findLookup" 
							 dataType="text"
							 exactMatch="false"
							 minLengthForAutoPopUpSearch="3"
							 size="30" maxLength="50" 
							 serializable="false">
				 </adsm:lookup>
		</adsm:listbox>
		

<!--
 		<adsm:listbox label="empresa" size="4" boxWidth="170" width="40%"  labelWidth="100"
	         			property="empresaUsuario"
	     				optionProperty="idEmpresaUsuario"
	     				optionLabelProperty="pessoa.nmPessoa">
				<adsm:lookup property="empresa"
							 idProperty="idEmpresa"
							 criteriaProperty="pessoa.nmPessoa"
							 action="/municipios/manterEmpresas"
							 service="lms.seguranca.manterUsuarioAction.findLookupEmpresa" 
							 dataType="text"
							 exactMatch="false"
							 minLengthForAutoPopUpSearch="3"
							 size="30" maxLength="50"
							 serializable="false">
				 </adsm:lookup>
		</adsm:listbox>



		<adsm:listbox label="filiais" size="4" boxWidth="170" width="40%" labelWidth="100"
					  property="filiaisUsuario" 
					  optionProperty="idFilialUsuario"
					  optionLabelProperty="sgFilial">
  				<adsm:label key="abrangeWorkflow"/>
				<adsm:combobox property="abrangeWorkflow" domain="DM_SIM_NAO" boxWidth="80" /><BR>
				<adsm:lookup property="filial"
		  					 idProperty="idFilial"
		  	                 criteriaProperty="sgFilial"
		  					 action="/municipios/manterFiliais"
		  					 service="lms.seguranca.manterUsuarioAction.findLookupFilial"
		  					 dataType="text"
							 exactMatch="false"
							 minLengthForAutoPopUpSearch="3"
		  					 size="30" maxLength="3"
		  					 serializable="false">
  				</adsm:lookup>
		</adsm:listbox>		


		<adsm:listbox label="regional" size="4" boxWidth="170" width="40%" labelWidth="100"
	         			property="regionalUsuario" 
	     				optionProperty="idRegionalUsuario"
	     				optionLabelProperty="regional.dsRegional">
				<adsm:lookup property="regional" 
							 idProperty="idRegional"
							 criteriaProperty="regional.dsRegional"
							 action="/municipios/manterRegionalFilial"
							 service="lms.seguranca.manterUsuarioAction.findLookupRegional" 
							 dataType="text"
							 exactMatch="false"
							 minLengthForAutoPopUpSearch="3"
							 size="30" maxLength="50">
				 </adsm:lookup>
		</adsm:listbox>
-->


		<adsm:buttonBar>
			<adsm:button id="vincularEmpresa" caption="vincularEmpresa" action="/seguranca/manterUsuarioLMSEmpresa" cmd="main" disabled="false">
			</adsm:button>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
function desabilita_cb(data,erro){
	onDataLoad_cb(data,erro);
	setDisabled("vincularEmpresa", false);
}

function initWindow(eventObj) {
		setDisabled("vincularEmpresa", false);
}
</script>