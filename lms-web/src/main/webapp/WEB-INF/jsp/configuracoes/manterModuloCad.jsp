<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.configuracoes.manterModuloSistemaAction" >
	<adsm:form action="/seguranca/manterModulo" idProperty="idModuloSistema">

		<adsm:lookup dataType="text" label="nmSistema" 
		     	 	 property="sistema" idProperty="idSistema" criteriaProperty="nmSistema"
	 				 action="/seguranca/manterSistema"
	 				 maxLength="60"
	 				 exactMatch="false"
	 				 minLengthForAutoPopUpSearch="3"
	 				 service="lms.configuracoes.manterModuloSistemaAction.findLookupSistema"
	 				 required="true"
			 	     width="100%"  
	    />					    

		<adsm:textbox dataType="text" property="nmModuloSistema" label="modulo" width="35%" size="20" maxLength="60" required="true" />
		<adsm:hidden  property="idModulo" />
		<adsm:textbox dataType="text" property="dsModuloSistema" label="descModulo" width="20%" size="20" maxLength="250"/>

		<adsm:buttonBar>
			<adsm:button caption="metodosTelas" action="/seguranca/manterMetodoTela" boxWidth="150" id="btnMetodoTela"  cmd="main" >
				<adsm:linkProperty src="idModuloSistema" target="modulo.idModuloSistema"/>
				<adsm:linkProperty src="nmModuloSistema" target="modulo.nmModuloSistema"/>
			</adsm:button>
			
			
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>