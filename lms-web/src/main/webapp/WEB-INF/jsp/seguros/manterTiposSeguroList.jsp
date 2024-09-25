<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.seguros.tipoSeguroService">
	<adsm:form action="/seguros/manterTiposSeguro">
		<adsm:textbox dataType="text" property="sgTipo" label="sigla" maxLength="10" size="10" width="85%"/>
		<adsm:textbox dataType="text" property="dsTipo" label="descricao" maxLength="100" size="80" width="85%"/>
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" renderOptions="true"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" renderOptions="true"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="15%" width="35%" required="false" renderOptions="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tiposSeguro"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="tiposSeguro" idProperty="idTipoSeguro" rows="11" selectionMode="check" unique="true" defaultOrder="dsTipo:asc">
		<adsm:gridColumn title="sigla" 		property="sgTipo" width="10%" />
		<adsm:gridColumn title="descricao" 	property="dsTipo" width="44%" />
		<adsm:gridColumn title="modal" 		property="tpModal" isDomain="true" width="18%" />
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="18%" />
		<adsm:gridColumn title="situacao"	property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
