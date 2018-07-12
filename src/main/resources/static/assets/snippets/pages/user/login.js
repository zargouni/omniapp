//== Class Definition
var SnippetLogin = function() {

    var login = $('#m_login');

    var showErrorMsg = function(form, type, msg) {
        var alert = $('<div class="m-alert m-alert--outline alert alert-' + type + ' alert-dismissible" role="alert">\
			<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>\
			<span></span>\
		</div>');

        form.find('.alert').remove();
        alert.prependTo(form);
        alert.animateClass('fadeIn animated');
        alert.find('span').html(msg);
    }

    //== Private Functions

    var displaySignUpForm = function() {
        login.removeClass('m-login--forget-password');
        login.removeClass('m-login--signin');

        login.addClass('m-login--signup');
        login.find('.m-login__signup').animateClass('flipInX animated');
    }

    var displaySignInForm = function() {
        login.removeClass('m-login--forget-password');
        login.removeClass('m-login--signup');

        login.addClass('m-login--signin');
        login.find('.m-login__signin').animateClass('flipInX animated');
    }

    var displayForgetPasswordForm = function() {
        login.removeClass('m-login--signin');
        login.removeClass('m-login--signup');

        login.addClass('m-login--forget-password');
        login.find('.m-login__forget-password').animateClass('flipInX animated');
    }

    var handleFormSwitch = function() {
        $('#m_login_forget_password').click(function(e) {
            e.preventDefault();
            displayForgetPasswordForm();
        });

        $('#m_login_forget_password_cancel').click(function(e) {
            e.preventDefault();
            displaySignInForm();
        });

        $('#m_login_signup').click(function(e) {
            e.preventDefault();
            displaySignUpForm();
        });

        $('#m_login_signup_cancel').click(function(e) {
            e.preventDefault();
            displaySignInForm();
        });
    }

    var handleSignInFormSubmit = function() {
        $('#m_login_signin_submit').click(function(e) {
            e.preventDefault();
            var btn = $(this);
            var form = $(this).closest('form');

            form.validate({
                rules: {
                    email: {
                        required: false,
                        email: false
                    },
                    password: {
                        required: false
                    }
                }
            });

            if (!form.valid()) {
                return;
            }

            btn.addClass('m-loader m-loader--right m-loader--light').attr('disabled', true);

            /*  form.ajaxSubmit({
                url: '/home',
                success: function(response, status, xhr, $form) {
                	// similate 2s delay
                	setTimeout(function() {
	                    btn.removeClass('m-loader m-loader--right m-loader--light').attr('disabled', false);
	                    showErrorMsg(form, 'danger', 'Incorrect username or password. Please try again.');
                    }, 2000);
                }
            });*/
        });
    }

    var handleSignUpFormSubmit = function() {
        $('#m_login_signup_submit').click(function(e) {
           e.preventDefault();

            var btn = $(this);
            var form = $(this).closest('form');
            
            var fullname = $('#new_fullname').val();
            var username = $('#new_username').val();
            var email = $('#new_email').val();
            var password = $('#new_password').val();
            var formData = new FormData();
            formData.append("fullname", fullname);
            formData.append("username", username);
            formData.append("email", email);
            formData.append("password", password);
            form.validate({
                rules: {
                    fullname: {
                        required: true
                    },
                    email: {
                        required: true,
                        //email: true
                    },
                    password: {
                        required: true
                    },
                    rpassword: {
                        required: true
                    },
//                    agree: {
//                        required: true
//                    }
                }
            });

            if (!form.valid()) {
                return;
            }
            
            btn.addClass('m-loader m-loader--right m-loader--light').attr('disabled', true);
            
            	console.log("send...");
            	$.ajax({
            		url: '/register',
                    type: "POST",
                    data: formData,
                    async: false,
                    processData: false,
                    success: function( response ) {
                    	// similate 2s delay
                    	if(response.status == "SUCCESS")
                    		setTimeout(function() {
    	                    btn.removeClass('m-loader m-loader--right m-loader--light').attr('disabled', false);
    	                    form.clearForm();
    	                    form.validate().resetForm();

    	                    // display signup form
    	                    displaySignInForm();
    	                    var signInForm = login.find('.m-login__signin form');
    	                    signInForm.clearForm();
    	                    signInForm.validate().resetForm();

    	                    showErrorMsg(signInForm, 'success', 'Thank you. To complete your registration please check your email.');
    	                }, 2000);
                    	else if(response.status == "FAIL"){
                    		toastr.error("error");
                    	}
                    },
                    error : function(e) {
            			alert('Error: register ' + e);
            		}
            	});
            

           

//            form.ajaxSubmit(function(){
//            	
//            });
        });
    }

    var handleForgetPasswordFormSubmit = function() {
        $('#m_login_forget_password_submit').click(function(e) {
            e.preventDefault();

            var btn = $(this);
            var form = $(this).closest('form');

            form.validate({
                rules: {
                    email: {
                        required: true,
                        email: true
                    }
                }
            });

            if (!form.valid()) {
                return;
            }

            btn.addClass('m-loader m-loader--right m-loader--light').attr('disabled', true);

            form.ajaxSubmit({
                url: '',
                success: function(response, status, xhr, $form) { 
                	// similate 2s delay
                	setTimeout(function() {
                		btn.removeClass('m-loader m-loader--right m-loader--light').attr('disabled', false); // remove 
	                    form.clearForm(); // clear form
	                    form.validate().resetForm(); // reset validation states

	                    // display signup form
	                    displaySignInForm();
	                    var signInForm = login.find('.m-login__signin form');
	                    signInForm.clearForm();
	                    signInForm.validate().resetForm();

	                    showErrorMsg(signInForm, 'success', 'Cool! Password recovery instruction has been sent to your email.');
                	}, 2000);
                }
            });
        });
    }

    //== Public Functions
    return {
        // public functions
        init: function() {
            handleFormSwitch();
            handleSignInFormSubmit();
            handleSignUpFormSubmit();
            handleForgetPasswordFormSubmit();
        }
    };
}();

//== Class Initialization
jQuery(document).ready(function() {
    SnippetLogin.init();
});