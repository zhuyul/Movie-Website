function popupWindow(movieid, counter){
	var ajaxRequest;
	var titleObj = document.getElementById("poptext" + counter);
	
	try{
		ajaxRequest = new XMLHttpRequest();
	} catch(e){
		try{
			ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			alert("There is an error!");
			return false;
		}
	}
	
	var temp;
	if (ajaxRequest.readyState == 0 || ajaxRequest.readyState == 4) {
		temp = escape(movieid);
		ajaxRequest.open("GET", 'popup?movieid=' + temp, true);
		ajaxRequest.onreadystatechange = helper; 
	}
	
	
	
	function helper() {
		if (ajaxRequest.readyState == 4) {
			var popUpWindow = document.getElementById('popbox');
			var ajaxResponse = ajaxRequest.responseText;
			popUpWindow.innerHTML = ajaxResponse;
			var rect = titleObj.getBoundingClientRect();
			console.log(rect);
			popUpWindow.style.display = "block";
			popUpWindow.style.top = rect.top + window.scrollY - popUpWindow.offsetHeight+"px";
			popUpWindow.style.left = rect.left+"px";
		}
	}
	
	ajaxRequest.send(null); 
	event.stopPropagation();
  
}


function mouseout() {
	var popUpWindow = document.getElementById("popbox")
	popUpWindow.style.display = "none";
	event.stopPropagation();
}