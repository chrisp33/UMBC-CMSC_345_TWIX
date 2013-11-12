var navigationStatus = true;

function toggleNavigation() {
	var nav = document.getElementById("navigationList");
	nav.style.display = navigationStatus ? "none" : "block";
	document.getElementById("navigationToggle").innerHTML = navigationStatus ? "&gt;" : "&lt;";
	navigationStatus = !navigationStatus;
}
