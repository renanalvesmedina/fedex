/**
 * Applica a m?scara em cima do campo informado baseado no tipo
 * de documento informado.
 *
 * @author Micka?l Jalbert
 * @since 01/12/2006 
 */
function setMaskNrDocumento(element, type){
	var obj;
	obj = getElement(element);
	
	switch(type) {
	case "CRT":
		obj.mask = "000000";
		break;
	case "CTR":
		obj.mask = "00000000";
		break;
	case "NDN":
		obj.mask = "0000000000";
		break;
	case "NFS":
		obj.mask = "00000000";
		break;		
	case "NFT":
		obj.mask = "00000000";			
		break;
	case "RNC":
		obj.mask = "00000000";
		break;	
	case "BDM":
		obj.mask = "0000000000";
		break;	
	case "DDE":
		obj.mask = "0000000000";
		break;	
	case "RDE":
		obj.mask = "0000000000";
		break;	
	case "TRA":
		obj.mask = "0000000000";
		break;	
	case "RCB":
		obj.mask = "0000000000";
		break;	
	case "MVN":
		obj.mask = "00000000";
		break;	
	case "RRE":
		obj.mask = "00000000";
		break;	
	case "MDA":
		obj.mask = "00000000";
		break;																															
	}
}