<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.configuracoes.manterSistemaAction" >
	<adsm:form action="/seguranca/manterSistema" idProperty="idSistema">
		<adsm:textbox dataType="text" property="nmSistema" label="descricao" required="true" maxLength="60" />
		<adsm:buttonBar>
			<adsm:button caption="modulos" action="/seguranca/manterModulo" boxWidth="65" cmd="main">
				<adsm:linkProperty src="idSistema" target="sistema.idSistema"/>
				<adsm:linkProperty src="nmSistema" target="sistema.nmSistema"/>
			</adsm:button>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />			
		</adsm:buttonBar>								
	</adsm:form>
</adsm:window>