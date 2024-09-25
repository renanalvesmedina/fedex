<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.seguros.tipoDocumentoSeguroService" >
	<adsm:form action="/seguros/manterTiposDocumentoProcessoSeguro" idProperty="idTipoDocumentoSeguro" >
		<adsm:textbox dataType="text" property="dsTipo" label="descricao" maxLength="50" size="50" width="85%" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>