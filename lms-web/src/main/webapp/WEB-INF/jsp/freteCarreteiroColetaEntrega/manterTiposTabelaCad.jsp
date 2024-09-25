<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteirocoletaentrega.manterTiposTabelaAction">
	<adsm:form action="/freteCarreteiroColetaEntrega/manterTiposTabela" idProperty="idTipoTabelaColetaEntrega">
		<adsm:textbox dataType="text" property="dsTipoTabelaColetaEntrega" label="descricaoTipoTabela" maxLength="60" size="50" required="true" labelWidth="18%" width="75%"/>
		<adsm:checkbox property="blNormal" label="normal" labelWidth="18%" width="75%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="18%" width="75%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   