<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.marcaMeioTransporteService">
	<adsm:form action="/contratacaoVeiculos/manterMarcasMeioTransporte" idProperty="idMarcaMeioTransporte">
		<adsm:combobox property="tpMeioTransporte" domain="DM_TIPO_MEIO_TRANSPORTE" required="true" label="modalidade" labelWidth="20%" width="50%"/>		
		<adsm:textbox dataType="text" property="dsMarcaMeioTransporte" required="true" label="descricaoMarca" maxLength="60" labelWidth="20%" size="60" width="60%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="20%" required="true"/>		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   