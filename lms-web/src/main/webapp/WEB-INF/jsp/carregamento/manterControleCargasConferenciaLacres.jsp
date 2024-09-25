<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	povoaForm();
}
</script>
<adsm:window title="manterControleCargasVeiculos" service="lms.carregamento.manterControleCargasJanelasAction" 
			 onPageLoad="carregaPagina" onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/manterControleCargasConferenciaLacres" idProperty="idLacreControleCarga" >

		<adsm:section caption="conferenciaLacre" width="70%" />

		<adsm:hidden property="idLacreControleCarga" />

		<adsm:combobox label="conferencia" property="tpStatusLacre" 
					   service="lms.carregamento.manterControleCargasJanelasAction.findTipoStatusLacre" 
					   optionProperty="value" optionLabelProperty="description" width="85%" required="true" />

		<adsm:textbox label="filial" property="filialByIdFilialAlteraStatus.sgFilial" dataType="text" size="3" width="85%" 
					  disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="filialByIdFilialAlteraStatus.pessoa.nmFantasia" size="30" 
						  disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="local" property="dsLocalConferencia" dataType="text" size="60" maxLength="80" width="85%" required="true" />

		<adsm:textarea label="observacao" property="obConferenciaLacre" maxLength="200" rows="3" columns="70" width="85%" />

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="salvar" id="botaoSalvar" onclick="botaoSalvar_onClick(this.form)" disabled="false" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

function povoaForm() {
	onPageLoad();
}


function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.getDadosIniciaisByLacre", "retornoDadosIniciais");
	    xmit({serviceDataObjects:[sdo]});
	}
}

function retornoDadosIniciais_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
   	setElementValue("filialByIdFilialAlteraStatus.sgFilial", getNestedBeanPropertyValue(data,"filialUsuario.sgFilial"));
   	setElementValue("filialByIdFilialAlteraStatus.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"filialUsuario.pessoa.nmFantasia"));
   	setFocusOnFirstFocusableField();
}


function botaoSalvar_onClick(form) {
	if (!validateForm(form)) {
		return false;
	}
    var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.storeConferenciaLacre", "store", buildFormBeanFromForm(form));
    xmit({serviceDataObjects:[sdo]});
}

function store_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
   	showSuccessMessage();
   	window.close();
}
</script>