<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterUsuarioVolAction">
	<adsm:form action="/vol/manterUsuarioVol" idProperty="idUsuario">

		<adsm:lookup label="filial" width="8%" labelWidth="100"
			property="filial" idProperty="idFilial" criteriaProperty="sgFilial"
			action="/municipios/manterFiliais"
			dataType="text" size="3" maxLength="3" required="false"> 
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"
				size="50" maxLength="50" disabled="true" width="50%" />
		</adsm:lookup>
		<adsm:lookup size="16" maxLength="60" labelWidth="10%" width="85%"
			label="login"
			property="loginUsuario" idProperty="idLogin"
			criteriaProperty="login"
			action="/seguranca/manterUsuarioADSM" 
			dataType="text">
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridUsuario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="gridUsuario" idProperty="idUsuario"
		selectionMode="check" rows="12" gridHeight="200" unique="true">
		<adsm:gridColumn property="loginUsuario.login" title="login" width="10%" />
		<adsm:gridColumn property="nrMatricula" title="matricula" width="20%" />
		<adsm:gridColumn property="nmUsuario" title="nome" width="40%" />
		<adsm:gridColumn property="ctrAparelho" title="ctrAparelho"
			renderMode="image-check" width="15%" />
		<adsm:gridColumn property="termo" title="termo"
			renderMode="image-check" width="15%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
