$(document).ready(function() {	

	var value = 0;
	
	Galleria.loadTheme('/assets/javascripts/galleria/themes/classic/galleria.classic.min.js');
	    var rotVal = 0;
		
		Galleria.ready(function() {
		    var gallery = this; 
			
	        gallery.attachKeyboard({
	            left: gallery.prev,
	            right: gallery.next,
	            up: function(){
    				rotVal += 90;
	            	$('#galleria').find('.galleria-stage').find('.galleria-images').find('.galleria-image').each(function(){
	    				$(this).find('img').rotate(rotVal);
	    			});
	            },
	        	down: function(){
    				rotVal -= 90;
	            	$('#galleria').find('.galleria-stage').find('.galleria-images').find('.galleria-image').each(function(){
	    				$(this).find('img').rotate(rotVal);
    				});
	        	}

	        });
	     });
		
		Galleria.run('#galleria');
		var gallery = Galleria.get(0);

		$('#rotate-right').click(function() {
			rotVal += 90;
			$('#galleria').find('.galleria-stage').find('.galleria-images').find('.galleria-image').each(function(){
				$(this).find('img').rotate(rotVal);
			});
		});
		$('#rotate-left').click(function() {
			rotVal -= 90;
			$('#galleria').find('.galleria-stage').find('.galleria-images').find('.galleria-image').each(function(){
				$(this).find('img').rotate(rotVal);
			});
		});
		
		$('#fullscreen').click(function() {
	        gallery.enterFullscreen(); // will go full screen when the #fullscreen element is clicked
	    });
	
});