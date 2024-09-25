<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.manterModuloSistemaAction">
	<adsm:form action="/seguranca/manterModulo">

		<adsm:lookup dataType="text" label="nmSistema" property="sistema"
			idProperty="idSistema" criteriaProperty="nmSistema"
			action="/seguranca/manterSistema" maxLength="60" exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			service="lms.configuracoes.manterModuloSistemaAction.findLookupSistema"
			width="100%">
		</adsm:lookup>

		<adsm:textbox dataType="text" property="nmModuloSistema" label="modulo" width="35%" size="20" maxLength="60" />
		<adsm:textbox dataType="text" property="dsModuloSistema" label="descModulo" width="20%" size="20" maxLength="250" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="moduloSistema" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idModuloSistema" property="moduloSistema"
			   gridHeight="200" rows="12" defaultOrder="sistema_.nmSistema, nmModuloSistema, dsModuloSistema">
		<adsm:gridColumn title="nmSistema" property="sistema.nmSistema" width="30%" />
		<adsm:gridColumn title="modulo" property="nmModuloSistema" width="45%" />
		<adsm:gridColumn title="descModulo" property="dsModuloSistema" width="25%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
