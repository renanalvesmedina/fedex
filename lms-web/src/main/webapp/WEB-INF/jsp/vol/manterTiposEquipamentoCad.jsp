<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.vol.manterTiposEquipamentoAction" >
	<adsm:form action="/vol/manterTiposEquipamento" idProperty="idTipoEqpto">
	                                                            
		<adsm:textbox property="dsNome" dataType="text" label="descricao" maxLength="50" size="60" required="true" width="85%"/>
				
		<adsm:buttonBar>
			<adsm:button caption="modelosEqp" action="/vol/manterModelos" cmd="main">
				<adsm:linkProperty src="idTipoEqpto" target="volTiposEqpto.idTipoEqpto"/>
				<adsm:linkProperty src="dsNome" target="volTiposEqpto.dsNome"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>