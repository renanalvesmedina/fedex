<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="manterModelosTipoMeiosTransporteAtributos" service="lms.contratacaoveiculos.modeloMeioTranspAtributoService">
	<adsm:form action="/contratacaoVeiculos/manterModelosTipoMeiosTransporteAtributos" idProperty="idModeloMeioTranspAtributo">
		<adsm:hidden property="tipoMeioTransporte.idTipoMeioTransporte"/>

		<adsm:combobox property="tipoMeioTransporte.tpMeioTransporte" boxWidth="170" label="modalidade" domain="DM_TIPO_MEIO_TRANSPORTE" labelWidth="23%" width="27%">
		</adsm:combobox>
		<adsm:textbox dataType="text" property="tipoMeioTransporte.dsTipoMeioTransporte" label="tipoMeioTransporte" maxLength="50" labelWidth="23%" width="27%" size="30" disabled="true" serializable="false"/>

		<adsm:combobox property="atributoMeioTransporte.idAtributoMeioTransporte" boxWidth="170" optionLabelProperty="dsAtributoMeioTransporte" optionProperty="idAtributoMeioTransporte" service="lms.contratacaoveiculos.atributoMeioTransporteService.findAtributoMeioTransporte" label="atributo" labelWidth="23%" width="27%">
		</adsm:combobox>
		
		
		<adsm:combobox property="blOpcional" domain="DM_SIM_NAO" label="opcional" labelWidth="23%" width="27%" cellStyle="vertical-align:bottom;"/>
		
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="23%" width="27%" cellStyle="vertical-align:bottom;"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="modeloMeioTranspAtributo"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="modeloMeioTranspAtributo" idProperty="idModeloMeioTranspAtributo" unique="true" defaultOrder="atributoMeioTransporte_.dsAtributoMeioTransporte,atributoMeioTransporte_.dsGrupo,atributoMeioTransporte_.nrOrdem" rows="11">
		<adsm:gridColumn width="70%" title="atributo" property="atributoMeioTransporte.dsAtributoMeioTransporte" />
		<adsm:gridColumn width="15%" title="opcional" property="blOpcional" renderMode="image-check"/>
		<adsm:gridColumn width="15%" title="situacao" property="tpSituacao" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>