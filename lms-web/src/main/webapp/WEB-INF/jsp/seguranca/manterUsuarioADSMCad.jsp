<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script type="text/javascript">
<!--
function validaCategoriaUsuario() {

   var cmb = getElementValue("tpCategoriaUsuario");
    
   switch(cmb) {
		case 'F': 
			document.getElementById("nrMatricula.idChapa").required = "true"; 
			break;
		default : 
			document.getElementById("nrMatricula.idChapa").required = "false"; 
			break;
   }   
}

//-->
</script>

<adsm:window service="lms.seguranca.manterUsuarioADSMAction">
	<adsm:form action="/seguranca/manterUsuarioADSM" idProperty="idUsuario">

		<adsm:hidden property="criteriaFindSemUsuario" value="sim"
			serializable="false" />

		<adsm:lookup size="16" maxLength="16" width="20%" labelWidth="100"
			required="false" property="nrMatricula" idProperty="chapa"
			criteriaProperty="idChapa"
			action="/configuracoes/consultarFuncionarios"
			service="lms.seguranca.manterUsuarioADSMAction.findLookupMat"
			dataType="text" label="matricula" exactMatch="true">
			<adsm:propertyMapping modelProperty="hModoConsultaNoJoin"
				criteriaProperty="criteriaFindSemUsuario" disable="true" />
		</adsm:lookup>

		<adsm:textbox dataType="text" property="nome" label="login"
			width="35%" labelWidth="100" size="40" maxLength="60" required="true" />

		<adsm:checkbox property="blAdministrador" label="admin" width="10%"
			labelWidth="115" />

		<adsm:combobox property="tpCategoriaUsuario" label="categoria"
			domain="DM_CATEGORIA_USUARIO" width="20%" labelWidth="100"
			required="true" onchange="return validaCategoriaUsuario();" />
		<adsm:combobox property="tpLinguagem" label="idioma"
			domain="TP_LINGUAGEM" labelWidth="100" required="true" />


		<adsm:textbox dataType="email" property="dsEmail" label="email"
			width="40%" size="40" maxLength="60" labelWidth="100" />


		<adsm:textbox dataType="text" property="login" label="login"
			width="80%" size="15" maxLength="60" labelWidth="100" required="true" />
		<adsm:textbox dataType="password" property="dsSenha" label="senha"
			size="25" maxLength="20" labelWidth="100" required="true" />
		<adsm:textbox dataType="password" property="dsSenhaVerify"
			label="confirmacaoSenha" maxLength="20" labelWidth="170" size="25"
			required="true" />


		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
