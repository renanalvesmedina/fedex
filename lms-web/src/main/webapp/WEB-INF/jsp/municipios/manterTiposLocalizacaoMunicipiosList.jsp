<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.tipoLocalizacaoMunicipioService">
	<adsm:form action="/municipios/manterTiposLocalizacaoMunicipios" idProperty="idTipoLocalizacaoMunicipio">
		<adsm:textbox dataType="text" property="dsTipoLocalizacaoMunicipio" label="descricao" maxLength="60" size="60" width="85%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" width="82%"/>
		<adsm:combobox property="tpLocalizacao" domain="DM_TIPO_LOCALIZACAO" label="tipoLocalizacao" width="82%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoLocalizacaoMunicipio"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
		defaultOrder="dsTipoLocalizacaoMunicipio" 
		idProperty="idTipoLocalizacaoMunicipio" 
		property="tipoLocalizacaoMunicipio" 
		selectionMode="check" 
		unique="true" 
		rows="12">
		
		<adsm:gridColumn title="descricao" property="dsTipoLocalizacaoMunicipio" width="70%"/>
		<adsm:gridColumn title="tipoLocalizacao" property="tpLocalizacao" isDomain="true" width="20%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

