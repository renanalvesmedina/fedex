<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.seguros.tipoDocumentoSeguroService" >
	<adsm:form action="/seguros/manterTiposDocumentoProcessoSeguro" idProperty="idTipoDocumentoSeguro" >
		<adsm:textbox dataType="text" property="dsTipo" label="descricao" maxLength="50" size="50" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="tipos" idProperty="idTipoDocumentoSeguro" defaultOrder="dsTipo:asc" selectionMode="check" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn property="dsTipo" title="descricao" width="90%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
