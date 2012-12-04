$(document).ready(function() {
	$('#triggerUpload').click(function() {
		uploader.uploadStoredFiles();
	});

	var uploader = new qq.FileUploader({
		// pass the dom node (ex. $(selector)[0] for jQuery users)
		element : document.getElementById('file-uploader'),
		multiple : true,
		maxConnections : 3,
		autoUpload : false,
		forceMultipart : true,
		uploadButtonText : "Select Files",
		// path to server-side upload script
		action : '/album/upload/files/',
		allowedExtensions : [ "jpg", "png", "gif", "zip" ],
		onComplete : function(id, fileName, responseJSON){console.log("bin da")},
		onSubmit: function() {
	        uploader.setParams({
	            albumId: $('#albumId').val()
	        });
	    }

	});
	
});
