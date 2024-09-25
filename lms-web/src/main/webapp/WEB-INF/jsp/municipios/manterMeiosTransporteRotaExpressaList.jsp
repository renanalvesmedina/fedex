<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterMeiosTransporteRotaExpressaAction" >
	<adsm:form action="/municipios/manterMeiosTransporteRotaExpressa" idProperty="idMeioTransporteRotaViagem" >
		<adsm:hidden property="rotaViagem.idRotaViagem" value="1" />

		<adsm:textbox dataType="integer" label="rotaIda" property="rotaIda.nrRota" disabled="true" mask="0000" size="4" serializable="false" labelWidth="20%" width="30%">
			<adsm:textbox dataType="text" property="rotaIda.dsRota" disabled="true" size="25" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="integer" label="rotaVolta" property="rotaVolta.nrRota" disabled="true" mask="0000" size="4" serializable="false" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="text" property="rotaVolta.dsRota" disabled="true" size="25" serializable="false"/>
		</adsm:textbox>

		<adsm:hidden property="tipoMeioTransporte.idTipoMeioTransporte" />
		<adsm:lookup
			label="meioTransporte"
			property="meioTransporteRodoviario2"
			idProperty="idMeioTransporte"
			criteriaProperty="meioTransporte.nrFrota"
			service="lms.municipios.manterMeiosTransporteRotaExpressaAction.findLookupMeioTransporteRodoviario"
			dataType="text"
			action="/contratacaoVeiculos/manterMeiosTransporte"
			picker="false"
			cmd="rodo"
			labelWidth="20%"
			width="80%"
			maxLength="6"
			size="8"
			exactMatch="true"
			serializable="false"
		>
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte"/>		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador"/>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
			<adsm:lookup
				property="meioTransporteRodoviario"
				idProperty="idMeioTransporte"
				criteriaProperty="meioTransporte.nrIdentificador"
				service="lms.municipios.manterMeiosTransporteRotaExpressaAction.findLookupMeioTransporteRodoviario"
				action="/contratacaoVeiculos/manterMeiosTransporte"
				dataType="text"
				picker="true"
				cmd="rodo"
				minLengthForAutoPopUpSearch="3"
				maxLength="25"
				size="25"
			>
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota"/>
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte"/>	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota"/>
				<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
			</adsm:lookup>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="meioTransporteRotaViagem" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="meioTransporteRotaViagem" idProperty="idMeioTransporteRotaViagem" unique="true" rows="12"
			service="lms.municipios.manterMeiosTransporteRotaExpressaAction.findPaginatedDefault" 
			rowCountService="lms.municipios.manterMeiosTransporteRotaExpressaAction.findRowCountDefault" >
		<adsm:gridColumn title="identificacao" property="meioTransporteRodoviario.meioTransporte.nrFrota" width="60" />
		<adsm:gridColumn title="" property="meioTransporteRodoviario.meioTransporte.nrIdentificador" align="left" width="70" />		
		<adsm:gridColumn title="tipoMeioTransporte" width="150"
				property="meioTransporteRodoviario.meioTransporte.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" />
		<adsm:gridColumn title="marca"
				property="meioTransporteRodoviario.meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" />
		<adsm:gridColumn title="modelo"
				property="meioTransporteRodoviario.meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte" />
		<adsm:gridColumn title="ano" property="meioTransporteRodoviario.meioTransporte.nrAnoFabricao" align="right" width="40"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="80"/>
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
