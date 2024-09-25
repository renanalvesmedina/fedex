<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.vol.manterTiposUsoAction" >
	<adsm:form action="/vol/manterTiposUso" idProperty="idTiposUso">
	
		<adsm:textbox dataType="text" property="dsNome" label="nome" width="80%" size="40" maxLength="30" required="true"/>
		<adsm:textarea property="obDescricao" label="descricao" width="80%" maxLength="255" columns="40" rows="3"/>
				
		<adsm:buttonBar>
			<adsm:button id="cadEqp" caption="equipamentos" action="/vol/manterEquipamentos" cmd="main" disabled="false" boxWidth="150" >
			 	<adsm:linkProperty src="idTiposUso" target="tipoUso.idTiposUso"/>
				<adsm:linkProperty src="dsNome" target="tipoUso.dsNome"/>
			</adsm:button>		
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>