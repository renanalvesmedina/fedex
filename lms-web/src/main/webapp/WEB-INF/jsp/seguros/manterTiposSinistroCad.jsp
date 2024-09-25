<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.tipoSinistroService" >
	<adsm:form action="/seguros/manterTiposSinistro" idProperty="idTipoSinistro" >
		<adsm:textbox dataType="text" property="dsTipo" label="descricao" size="40" maxLength="30" width="85%" required="true" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:textarea property="dsTextoCartaOcorrencia" label="textoCartaOcorrencia" maxLength="2000" columns="70" rows="5" width="85%" required="true" />
		<adsm:textarea property="dsTextoCartaRetificacao" label="textoCartaRetificacao" maxLength="2000" columns="70" rows="5" width="85%" required="false" />
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>