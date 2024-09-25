<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript">
<!--
function meuPerfilPageLoad() {

   onPageLoad();
   setDisabled("carregar", false);
   setDisabled("salvar", false);
}

function obtemDadosUsuarioLogado_cb() {

   onPageLoad_cb();
   
   var sdo = createServiceDataObject("lms.seguranca.manterMeuPerfilAction.findMeuPerfil", "preecheDadosUsuarioLogado");
   xmit({serviceDataObjects:[sdo]});
}

function preecheDadosUsuarioLogado_cb(data, error, errorKey) {

   fillFormWithFormBeanData(0, data);
   fillFormWithFormBeanData(1, data);
	
   if (getNestedBeanPropertyValue(data, "usuarioLogado.usuarioClienteNaoAdmin") == "true") {
      setDisabled("empresaLogado.idEmpresa", true);
      setDisabled("filialLogado.idFilial", true);
      setDisabled("carregar", true);

      setDisabled("empresaPadrao.idEmpresa", true);
      document.getElementById("empresaPadrao.idEmpresa").required = "false";
      setDisabled("filialPadrao.idFilial", true);
      document.getElementById("filialPadrao.idFilial").required = "false";
   }
}

function checkVerifyPassword() {

   var senha = getElementValue("dsSenha");
   var senhaVerify = getElementValue("dsSenhaVerify");
   return senha.toLowerCase() == senhaVerify.toLowerCase();
}

function xmitMeuPerfil(s, cb, f, handleCallbackUserMessages, showSuccessMessage) {

   valid = validateTabScript(f);
   if (valid == false)
      return false;
      
   if (handleCallbackUserMessages == undefined)
      handleCallbackUserMessages = true;

   var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:handleCallbackUserMessages, showSuccessMessage:showSuccessMessage};
   var storeSDO = createServiceDataObject(s, cb, buildFormBeanFromForm(f));
   remoteCall.serviceDataObjects.push(storeSDO);
   xmit(remoteCall);
}

function loadMeuPerfil(f) {
	
	var option = window.confirm(i18NLabel.getLabel('LMS-27119'));
	if (option == false) {
		return;
	} 

   xmitMeuPerfil("lms.seguranca.manterMeuPerfilAction.loadMeuPerfil", "loadMeuPerfil", f);

   return true;
}

function loadMeuPerfil_cb() {
	try {
		parent.parent.parent.frames[0].loadUserInfo();
	} catch (e) {
		window.status = "loadUserInfo";
		window.status = "";
	}
   
   return true;
}

function storeMeuPerfil(f) {

   var option = window.confirm(i18NLabel.getLabel('LMS-27119'));
   if (option == false) {
      return;
   } 

   if (checkVerifyPassword() == false) {
      alert(i18NLabel.getLabel('senhaNaoVerifica'));
      resetValue("dsSenha");
      resetValue("dsSenhaVerify");
      setFocus("dsSenha");
      return false;
   }
   
   xmitMeuPerfil("lms.seguranca.manterMeuPerfilAction.storeMeuPerfil", "storeMeuPerfil", f, true, true);
   
   return true;
}

function storeMeuPerfil_cb(data, errorMsg, errorKey) {

   // store_cb(data, errorMsg, errorKey);
   
   if (errorMsg != undefined)
      return false;
   
   setElementValue("empresaLogado.idEmpresa", getElementValue("empresaPadrao.idEmpresa"));
   setElementValue("filialLogado.idFilial", getElementValue("filialPadrao.idFilial"));
   setElementValue("filialLogado.sgFilial", getElementValue("filialPadrao.sgFilial"));
   setElementValue("filialLogado.pessoa.nmFantasia", getElementValue("filialPadrao.pessoa.nmFantasia"));
   
	try {
   parent.parent.parent.frames[0].loadUserInfo();
	} catch (e) {
		window.status = "loadUserInfo";
		window.status = "";
	}

   return true;
}

function disableIfEmpty(src, target) {

   comboboxChange({e:src});

   setDisabled(target, getElementValue(src) == "");
}

-->
</script>
<adsm:window onPageLoad="meuPerfilPageLoad" onPageLoadCallBack="obtemDadosUsuarioLogado">
	<adsm:i18nLabels>
		<adsm:include key="senhaNaoVerifica"/>
		<adsm:include key="LMS-27119"/>
	</adsm:i18nLabels>
	<adsm:form action="/seguranca/manterMeuPerfil" service="lms.seguranca.manterMeuPerfilAction">
		<adsm:combobox property="empresaLogado.idEmpresa" optionProperty="idEmpresa" optionLabelProperty="pessoa.nmPessoa" label="empresaLogado" service="lms.seguranca.manterMeuPerfilAction.findEmpresasByUsuarioLogado" width="85%" required="true" onchange="disableIfEmpty(this, 'filialLogado.idFilial');"/>

		<adsm:lookup dataType="text" property="filialLogado" idProperty="idFilial" criteriaProperty="sgFilial" label="filialLogado" size="3" maxLength="3" action="/seguranca/consultarFiliaisByUsuarioLogado" service="lms.seguranca.manterMeuPerfilAction.findLookupFiliaisByEmpresaUsuarioLogado" width="8%" required="true">
			<adsm:propertyMapping criteriaProperty="empresaLogado.idEmpresa" modelProperty="empresa.idEmpresa" />
            <adsm:propertyMapping relatedProperty="filialLogado.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
	        <adsm:textbox dataType="text" property="filialLogado.pessoa.nmFantasia" size="30" disabled="true" serializable="false" width="76%"/>
        </adsm:lookup>
        
        <adsm:buttonBar freeLayout="true">
           <adsm:button id="carregar" caption="carregar" onclick="return loadMeuPerfil(document.forms[0]);"/>
        </adsm:buttonBar>
    </adsm:form>

	<adsm:form action="/seguranca/manterMeuPerfil" service="lms.seguranca.manterMeuPerfilAction">
		<adsm:combobox property="empresaPadrao.idEmpresa" optionProperty="idEmpresa" optionLabelProperty="pessoa.nmPessoa" label="empresaPadrao" service="lms.seguranca.manterMeuPerfilAction.findEmpresasByUsuarioLogado" width="85%" required="true" onchange="disableIfEmpty(this, 'filialPadrao.idFilial');"/>

		<adsm:lookup dataType="text" property="filialPadrao" idProperty="idFilial" criteriaProperty="sgFilial" label="filialPadrao" size="3" maxLength="3" action="/seguranca/consultarFiliaisByUsuarioLogado" service="lms.seguranca.manterMeuPerfilAction.findLookupFiliaisByEmpresaUsuarioLogado" width="8%" required="true">
			<adsm:propertyMapping criteriaProperty="empresaPadrao.idEmpresa" modelProperty="empresa.idEmpresa" />
            <adsm:propertyMapping relatedProperty="filialPadrao.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
	        <adsm:textbox dataType="text" property="filialPadrao.pessoa.nmFantasia" size="30" disabled="true" serializable="false" width="76%"/>
        </adsm:lookup>

		<adsm:textbox dataType="password" property="dsSenha" label="senha" size="25" maxLength="20"/>
		<adsm:textbox dataType="password" property="dsSenhaVerify" label="confirmacaoSenha" maxLength="20" size="25" serializable="false"/>
				
		<adsm:combobox property="locale" label="idioma" domain="TP_LINGUAGEM" required="true" boxWidth="200" onlyActiveValues="true"/>

		<adsm:buttonBar>
			<adsm:button id="salvar" caption="salvar" onclick="return storeMeuPerfil(document.forms[1]);" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>