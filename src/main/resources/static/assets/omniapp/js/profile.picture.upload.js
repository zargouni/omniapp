/**
 * Profile picture
 * @author Daniel Salvagni <danielsalvagni@gmail.com>
 */


/**
 * Turn the globals into local variables.
 */
; (function (window, $, undefined) {
    if (!window.profilePicture) {
        window.profilePicture = profilePicture;
    }

    /**
     * Component
     */
    function profilePicture(cssSelector, imageFilePath, options) {
        var self = this;
        /**
         * Map the DOM elements
         */
        self.element = $(cssSelector);
        self.canvas = $(cssSelector + ' .photo__frame .photo__canvas')[0];
        self.photoImg = $(cssSelector + ' .photo__frame img');
        self.photoLoading = $(cssSelector + ' .photo__frame .message.is-loading');
        self.photoOptions = $(cssSelector + ' .photo__options');
        self.photoFrame = $(cssSelector + ' .photo__frame');
        self.photoArea = $(cssSelector + ' .photo');
        /**
         * Image info to post to the API
         */
        self.model = {
            imageSrc: null,
            width: null,
            height: null,
            originalWidth: null,
            originalHeight: null,
            y: null,
            x: null,
            zoom: 1,
            cropWidth: null,
            cropHeight: null
        };


        /**
         * Plugin options
         */
        self.options = {};
        /**
         * Plugins defaults
         */
        self.defaults = {};
        self.defaults.imageHelper = true;
        self.defaults.imageHelperColor = 'rgba(255,255,255,.90)';
        /**
         * Callbacks
         */
        self.defaults.onChange = null;
        self.defaults.onImageSizeChange = null;
        self.defaults.onPositionChange = null;
        self.defaults.onLoad = null;
        self.defaults.onError = null;
        /**
         * Zoom default options
         */
        self.defaults.zoom = {
            initialValue: 1,
            minValue: 0.1,
            maxValue: 2,
            step: 0.01
        };
        /**
         * Image default options
         */
        self.defaults.image = {
            originalWidth: 0,
            originalHeight: 0,
            originaly: 0,
            originalX: 0,
            minWidth: 100,
            minHeight: 100,
            maxWidth: 1000,
            maxHeight: 1000
        };

        /**
         * Zoom controls
         */
        self.zoom = $(cssSelector + ' .zoom');

        /**
         * Call the constructor
         */
        init(cssSelector, imageFilePath, options);

        /**
         * Return public methods
         */
        return {
            getData: getData,
            getAsDataURL: getAsDataURL,
        };



        /**
         * Constructor
         * Register all components and options.
         * Can load a preset image
         */
        function init(cssSelector, imageFilePath, options) {
            /**
             * Start canvas
             */
            self.canvas.width = self.photoFrame.outerWidth();
            self.canvas.height = self.photoFrame.outerHeight();
            self.canvasContext = self.canvas.getContext('2d');
            /**
             * Show the right text
             */
            if (isMobile()) {
                self.photoArea.addClass('is-mobile');
            } else {
                self.photoArea.addClass('is-desktop');
            }
            /**
             * Merge the defaults with the user options
             */
            self.options = $.extend({}, self.defaults, options);

            /**
             * Enable/disable the image helper
             */


            registerDropZoneEvents();
          
            /**
             * Start
             */
            if (imageFilePath) {
                processFile(imageFilePath);
            } else {
                self.photoArea.addClass('photo--empty');
            }
        }

        /**
         * Check if the user's device is a smartphone/tablet
         */
        function isMobile() {
            return navigator.userAgent.match(/BlackBerry|Android|iPhone|iPad|iPod|Opera Mini|IEMobile/i);
        }

        /**
         * Return the model
         */
        function getData() {
            return model;
        }
        /**
         * Set the model
         */
        function setModel(model) {
            self.model = model;
        }
        /**
         * Set the image to a canvas
         */
        function processFile(imageUrl) {
            function isDataURL(s) {
                s = s.toString();
                return !!s.match(isDataURL.regex);
            }
            isDataURL.regex = /^\s*data:([a-z]+\/[a-z]+(;[a-z\-]+\=[a-z\-]+)?)?(;base64)?,[a-z0-9\!\$\&\'\,\(\)\*\+\,\;\=\-\.\_\~\:\@\/\?\%\s]*\s*$/i;

            var image = new Image();
            if (!isDataURL(imageUrl)) {
                image.crossOrigin = 'anonymous';
            }
            self.photoArea.addClass('photo--loading');
            image.onload = function () {
                var ratio,
                    newH, newW,
                    w = this.width, h = this.height;



                self.photoArea.removeClass('photo--empty photo--error--file-type photo--loading');

                var frameRatio = self.options.image.maxHeight / self.options.image.maxWidth;
                var imageRatio = self.model.height / self.model.width;

                if (frameRatio > imageRatio) {
                    newH = self.options.image.maxHeight;
                    ratio = (newH / h);
                    newW = parseFloat(w) * ratio;
                } else {
                    newW = self.options.image.maxWidth;
                    ratio = (newW / w);
                    newH = parseFloat(h) * ratio;
                }
                h = newH;
                w = newW;

                self.model.imageSrc = image;
                self.model.originalHeight = h;
                self.model.originalWidth = w;
                self.model.height = h;
                self.model.width = w;
                self.model.cropWidth = self.photoFrame.outerWidth();
                self.model.cropHeight = self.photoFrame.outerHeight();
                self.model.x = 0;
                self.model.y = 0;
                self.photoOptions.removeClass('hide');
                fitToFrame();
                render();

                /**
                 * Call the onLoad callback
                 */
                if (typeof self.options.onLoad === 'function') {
                    self.options.onLoad(self.model);
                }

            };

            image.src = imageUrl;
        }
        /**
         * Remove the image and reset the component state
         */


        /**
         * Register the file drop zone events 
         */
        function registerDropZoneEvents() {
            var target = null;

            /**
             * On a file is selected, calls the readFile method.
             * It is allowed to select just one file - we're forcing it here.
             */
            self.element.on('change', 'input[type=file]', function (e) {
                if (this.files && this.files.length) {
                    readFile(this.files[0]);
                    uploadProfilePicture(this.files[0]);
                  
                }
            });
            /**
             * Handle the click to the hidden input file so we can browser files.
             */
            self.element.on('click', '.photo--empty .photo__frame', function (e) {
                $(cssSelector + ' input[type=file]').trigger('click');

            });

            /**
             * Only into the DropZone scope.
             * Read a file using the FileReader API.
             * Validates file type.
             */
            function readFile(file) {
                self.photoArea.removeClass('photo--error photo--error--file-type photo--error-image-size');
                /**
                 * Validate file type
                 */
                if (!file.type.match('image.*')) {
                    self.photoArea.addClass('photo--error--file-type');
                    /**
                     * Call the onError callback
                     */
                    if (typeof self.options.onError === 'function') {
                        self.options.onError('file-type');
                    }
                    return;
                }

                var reader;
                reader = new FileReader();
                reader.onloadstart = function () {
                    self.photoArea.addClass('photo--loading');
                }
                reader.onloadend = function (data) {
                    self.photoImg.css({ left: 0, top: 0 });
                    var base64Image = data.target.result;
                    processFile(base64Image, file.type);
                }
                reader.onerror = function () {
                    self.photoArea.addClass('photo--error');
                    /**
                     * Call the onError callback
                     */
                    if (typeof self.options.onError === 'function') {
                        self.options.onError('unknown');
                    }
                }
                reader.readAsDataURL(file);
            }
        }


        /**
         * Resize and position the image to fit into the frame
         */
        function fitToFrame() {
            var newHeight, newWidth, scaleRatio;

            var frameRatio = self.model.cropHeight / self.model.cropWidth;
            var imageRatio = self.model.height / self.model.width;

            if (frameRatio > imageRatio) {
                newHeight = self.model.cropHeight;
                scaleRatio = (newHeight / self.model.height);
                newWidth = parseFloat(self.model.width) * scaleRatio;
            } else {
                newWidth = self.model.cropWidth;
                scaleRatio = (newWidth / self.model.width);
                newHeight = parseFloat(self.model.height) * scaleRatio;
            }

            self.model.height = newHeight;
            self.model.width = newWidth;
        }
        /**
         * Update image's position and size
         */
        function render() {
             self.canvasContext.drawImage(self.model.imageSrc, self.model.x, self.model.y, self.model.width, self.model.height);
           
            if (self.options.imageHelper) {
                updateHelper();
            }
            /**
             * Call the onChange callback
             */
            if (typeof self.options.onChange === 'function') {
                self.options.onChange(self.model);
            }
        }

        /**
         * Updates the image helper attributes
         */
        function updateHelper() {
            var x = self.model.x + self.photoFrame.position().left;
            var y = self.model.y + self.photoFrame.position().top;
            
        }
        /**
         * Creates the canvas for the image helper
         */
        function registerImageHelper() {
            var canvas = document.createElement('canvas');
            canvas.className = "canvas--helper";
            canvas.width = self.photoHelper.outerWidth();
            canvas.height = self.photoHelper.outerHeight();

            self.photoHelper.prepend(canvas);

            self.imageHelperCanvas = canvas;
            self.imageHelperCanvasContext = canvas.getContext('2d');
            self.imageHelperCanvasContext.mozImageSmoothingEnabled = false;
                      self.imageHelperCanvasContext.msImageSmoothingEnabled = false;
            self.imageHelperCanvasContext.imageSmoothingEnabled = false;
        }
        /**
         * Return the image cropped as Base64 data URL
         */
        function getAsDataURL(quality) {
            if (!quality) { quality = 1; }
            return self.canvas.toDataURL(quality);
        }
    }
})(window, jQuery);


$(function() {
  
/**
     * DEMO
     */ 
    var p = new profilePicture('.profile', null,
        {
          imageHelper: false,
            
            onError: function (type) {
                console.log('Error type: ' + type);
            }
        });


    
});


function uploadProfilePicture(file) {
	
	 
		var formData = new FormData();
	    formData.append("id", "1");
	    formData.append("file", file);
	    $.ajax({
			type : "POST",
			url : '/upload-profile-picture',
			data: formData,
			processData: false,
			contentType: false,
			success : function(response) {
				if(response.status="SUCCESS")
					toastr.success("Profile picture updated successfully");
				
				else
					toastr.error("Couldn't update profile picture");
			},
			error : function(e) {
				alert('Error: Profile picture upload ' + e);
			}
		});
	    

}

