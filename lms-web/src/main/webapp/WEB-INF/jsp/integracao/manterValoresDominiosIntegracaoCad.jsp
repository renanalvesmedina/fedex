<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.integracao.manterValoresDominiosIntegracaoAction">
	<adsm:form action="/integracao/manterValoresDominiosIntegracao" idProperty="idDominioVinculoIntegracao">
	
		<adsm:hidden property="dominioNomeIntegracao.idDominioNomeIntegracao"/>
		
		<adsm:textbox dataType="text" 
					  property="dominioNomeIntegracao.nmDominio" 
					  label="dominio" 
					  disabled="true" 
					  size="40" 
					  required="true"
					  maxLength="60"/>
		
		<adsm:textbox dataType="text" 
					  property="dsValorLms" 
					  label="valorLms" 
					  size="20" 
					  maxLength="60"
					  required="true"/>
					  
        <adsm:textbox dataType="text" 
        			  property="dsValorCorporativo" 
        			  label="valorCorporativo" 
        			  size="20" 
        			  maxLength="60"/>

        <adsm:textbox dataType="text" 
        			  property="dsValorClipper" 
        			  label="valorClipper" 
        			  size="20" 
        			  maxLength="60"/>
		       			
		<adsm:checkbox property="blLmsCorporativo" label="padraoCorporativo"/>
		
		<adsm:checkbox property="blLmsClipper" label="padraoClipper"/>
		
		<adsm:textbox dataType="text" 
        			  property="dsSignificadoIntegracao" 
        			  label="significado" 
        			  size="80" 
        			  maxLength="100"
        			  width="100%"/>	

		<TR>
			<TD>
				<BR>
			</TD>
		</TR>
		<adsm:label key="infoValorDomInt" width="100%" style="border:none;color: rgb(0,0,255);font-family: Tahoma;font-size: 9pt;font-weight: normal;" />	
	
		<adsm:buttonBar>
			<adsm:button caption="salvar" id="__buttonBar:0.storeButton"
						 buttonType="storeButton" onclick="salva();"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
		<script>
			var lms_00013 = '<adsm:label key="LMS-00013"/>';
			var labelVlCorp = '<adsm:label key="valorCorporativo"/>';
			var labelOu = '<adsm:label key="ou"/>';
			var labelVlClipper = '<adsm:label key="valorClipper"/>';
		</script>
		
	</adsm:form>
</adsm:window>

<script type="text/javascript">		
 	function salva() {
		if (getElementValue("dsValorCorporativo") == "" &&
				getElementValue("dsValorClipper") == "") {
			alert(lms_00013 + ' \'' + labelVlCorp + '\' ' + labelOu + ' \'' + labelVlClipper+ '\'.');
			setFocus("dsValorCorporativo");
		}
		else {	
			storeButtonScript('lms.integracao.manterValoresDominiosIntegracaoAction.store', 'store', document.getElementById("form_idDominioVinculoIntegracao"));
		}				
	}
	
</script>