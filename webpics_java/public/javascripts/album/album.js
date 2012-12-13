$(document).ready(function() {	
	Galleria.loadTheme('@routes.Assets.at("javascripts/galleria/themes/classic/galleria.classic.min.js")');
	Galleria.run('#galleria');
	var gallery = Galleria.get(0);
	
	
	$('#fullscreen').click(function() {
        gallery.enterFullscreen(); // will go full screen when the #fullscreen element is clicked
    });
	
});