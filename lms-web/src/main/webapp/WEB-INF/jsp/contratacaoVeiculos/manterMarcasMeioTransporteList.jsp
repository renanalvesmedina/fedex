<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMarcasMeioTransporte" service="lms.contratacaoveiculos.marcaMeioTransporteService">
	<adsm:form action="/contratacaoVeiculos/manterMarcasMeioTransporte" idProperty="idMarcaMeioTransporte">

		<adsm:combobox
			label="modalidade"
			property="tpMeioTransporte"
			domain="DM_TIPO_MEIO_TRANSPORTE"
			labelWidth="20%"
			width="50%"
		/>

		<adsm:textbox
			label="descricaoMarca"
			property="dsMarcaMeioTransporte"
			dataType="text"
			maxLength="60"
			labelWidth="20%"
			size="60"
			width="60%"
		/>

		<adsm:combobox
			label="situacao"
			property="tpSituacao"
			domain="DM_STATUS"
			labelWidth="20%"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="marcaMeioTransporte"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid property="marcaMeioTransporte" idProperty="idMarcaMeioTransporte" rows="12" unique="true" defaultOrder="tpMeioTransporte,dsMarcaMeioTransporte">
		<adsm:gridColumn width="30%" title="modalidade" property="tpMeioTransporte" isDomain="true"/>
		<adsm:gridColumn width="60%" title="descricaoMarca" property="dsMarcaMeioTransporte"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
