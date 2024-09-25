<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterOperadorasAction" >
	<adsm:form  action="/vol/manterOperadoras">
		
		<adsm:hidden property="pessoa.tpPessoa" value="J"/>
	
		<adsm:complement label="identificacao" labelWidth="18%" width="82%">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" />
			<adsm:textbox definition="IDENTIFICACAO_PESSOA" />
		</adsm:complement>	

		<adsm:hidden property="pessoa.idPessoa" />
		
		<adsm:textbox dataType="text" size="95%" labelWidth="18%" width="82%"
			property="pessoa.nmPessoa" label="nome" maxLength="50"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS_PESSOA" 
			label="situacao" labelWidth="18%" width="32%" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="operadoras"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid property="operadoras" idProperty="idOperadora" selectionMode="check" 
			   rows="12" gridHeight="210" unique="true" 
			   service="lms.vol.manterOperadorasAction.findPaginatedOperadoras" 
			   rowCountService="lms.vol.manterOperadorasAction.getRowCountOperadoras"
    >			   
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" width="70" isDomain="true"/>
		<adsm:gridColumn title="" property="pessoa.nrIdentificacao" width="100" align="right"/>

		<adsm:gridColumn title="nome" property="pessoa.nmPessoa" width="250"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" width="70" isDomain="true"/>
		
		<adsm:gridColumn title="email" property="pessoa.dsEmail" width="200"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>