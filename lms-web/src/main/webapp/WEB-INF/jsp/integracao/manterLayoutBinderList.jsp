<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="com.mercurio.adsmmanager.integracao.action.manterLayoutBinderAction">
	<adsm:form action="/integracao/manterLayoutBinder"> 		
		
		<adsm:textbox dataType="text" property="nome" 
					  label="nome" maxLength="30" size="55" 
					  labelWidth="18%" width="80%" required="false" />
					  
		<adsm:textbox dataType="text" property="descricao" 
					  label="descricao" maxLength="300"  size="55" 
					  labelWidth="18%" width="80%" required="false" />
					  
		<adsm:textbox dataType="text" property="idIdentificadorPrimario" 
					  label="idPrimario" maxLength="2"  size="2" 
					  labelWidth="18%" width="80%" required="false" />				
					  
		<adsm:textbox dataType="text" property="idIdentificadorSecundario" 
					  label="idSecundario" maxLength="2"  size="2" 
					  labelWidth="18%" width="80%"/>					  
					  
		<adsm:textbox dataType="text" property="idIdentificadorTerciario" 
					  label="idTerciario" maxLength="2"  size="2" 
					  labelWidth="18%" width="80%"/>					  					  					  	  					  
		
		<%-- Hidden que serve de controle para saber se deve-se listar todos os Layouts ou se deve listar
			 apenas os Layouts com ValorLayout vinculados.  --%>
		<adsm:hidden property="hdBuscaVinculoValorLayout" serializable="true" value="N" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="layoutsGD"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="id"  property="layoutsGD" defaultOrder="nome"  rows="9" selectionMode="check" scrollBars="horizontal" gridHeight="200" unique="true">
		<adsm:gridColumn title="nome"          property="nome" align="center"   width="30%" />
		<adsm:gridColumn title="descricao"     property="descricao" align="center"   width="30%" />
		<adsm:gridColumn title="idPrimario"    property="idIdentificadorPrimario"   align="center" width="10%" />		
		<adsm:gridColumn title="idSecundario"  property="idIdentificadorSecundario" align="center" width="10%" />				
		<adsm:gridColumn title="idTerciario"   property="idIdentificadorTerciario"  align="center" width="10%" />		
		<adsm:gridColumn title="tpLayout"      property="tpOrigem"                  align="center" width="10%" isDomain="true"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>