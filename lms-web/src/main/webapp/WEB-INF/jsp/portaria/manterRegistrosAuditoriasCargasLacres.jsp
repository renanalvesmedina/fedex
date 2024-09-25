<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.portaria.manterRegistrosAuditoriasCargasAction" onPageLoadCallBack="onPageLoadCallback" >
	<adsm:form action="/portaria/manterRegistrosAuditoriasCargasLacres" >
		<adsm:section caption="conferenciaLacre" width="65%" />

		<adsm:combobox property="tpStatusLacre" label="conferencia" domain="DM_STATUS_LACRE_VEICULO" width="85%" disabled="true"/>

		<adsm:lookup service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupFilial" dataType="text"
				property="filial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" disabled="true"
				width="85%" labelWidth="15%" action="/municipios/manterFiliais" idProperty="idFilial" picker="false">
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
        	<adsm:hidden property="filial.siglaNomeFilial"/>
        </adsm:lookup>
		
		<adsm:textarea property="obConferenciaLacre" label="observacao" maxLength="200" rows="3" columns="60" width="85%"  />

		<adsm:buttonBar freeLayout="false">
			<adsm:button id="salvar" caption="salvar" onclick="onStoreButtonClick();" disabled="false" />			
			<adsm:button id="fechar" caption="fechar" onclick="self.close();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function onPageLoadCallback_cb(data, error) {
		setElementValue("tpStatusLacre", "RA");
		setFocusOnFirstFocusableField(document);
		setDisabled("salvar", false);
		setDisabled("fechar", false);
		
	}
	
	function onStoreButtonClick() {
		var listBox = dialogArguments.document.getElementById("lacresAtuais");
		var violado = new Array();
		setNestedBeanPropertyValue(violado, "idLacreControleCarga", listBox.options[listBox.selectedIndex].value );
		setNestedBeanPropertyValue(violado, "dsLocalConferencia", document.getElementById("filial.siglaNomeFilial").value );
		setNestedBeanPropertyValue(violado, "obConferenciaLacre", document.getElementById("obConferenciaLacre").value );
		dialogArguments.window.lacresViolados.push(violado);
		dialogArguments.window.removeLacre = true;
		self.close();
	}
	
	if (dialogArguments.document.forms[0].elements["filial.idFilial"] != undefined) {
		setElementValue("filial.idFilial", dialogArguments.document.forms[0].elements["filial.idFilial"].value);
		setElementValue("filial.sgFilial", dialogArguments.document.forms[0].elements["filial.sgFilial"].value);
		setElementValue("filial.pessoa.nmFantasia", dialogArguments.document.forms[0].elements["filial.pessoa.nmFantasia"].value);
		setElementValue("filial.siglaNomeFilial", dialogArguments.document.forms[0].elements["filial.siglaNomeFilial"].value);
	}
	
	

</script>