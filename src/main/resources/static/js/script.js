document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    const firstNameInput = document.getElementById('firstName');
    const lastNameInput = document.getElementById('lastName');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');

    const firstNameError = document.createElement('div');
    const lastNameError = document.createElement('div');
    const emailError = document.createElement('div');
    const passwordError = document.createElement('div');
    const confirmPasswordError = document.createElement('div');

    // Error message styling
    firstNameError.classList.add('error-message');
    lastNameError.classList.add('error-message');
    emailError.classList.add('error-message');
    passwordError.classList.add('error-message');
    confirmPasswordError.classList.add('error-message');

    // Add error message containers after the input fields
    firstNameInput.insertAdjacentElement('afterend', firstNameError);
    lastNameInput.insertAdjacentElement('afterend', lastNameError);
    emailInput.insertAdjacentElement('afterend', emailError);
    passwordInput.insertAdjacentElement('afterend', passwordError);
    confirmPasswordInput.insertAdjacentElement('afterend', confirmPasswordError);

    form.addEventListener('submit', function (event) {
        let isValid = true;

        // Reset error messages and styles
        firstNameError.textContent = '';
        lastNameError.textContent = '';
        emailError.textContent = '';
        passwordError.textContent = '';
        confirmPasswordError.textContent = '';

        firstNameInput.classList.remove('is-invalid');
        lastNameInput.classList.remove('is-invalid');
        emailInput.classList.remove('is-invalid');
        passwordInput.classList.remove('is-invalid');
        confirmPasswordInput.classList.remove('is-invalid');

        // First Name validation
        if (firstNameInput.value.trim() === '') {
            isValid = false;
            firstNameError.textContent = 'First name is required.';
            firstNameInput.classList.add('is-invalid');
        }

        // Last Name validation
        if (lastNameInput.value.trim() === '') {
            isValid = false;
            lastNameError.textContent = 'Last name is required.';
            lastNameInput.classList.add('is-invalid');
        }

        // Email validation using regex
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (emailInput.value.trim() === '') {
            isValid = false;
            emailError.textContent = 'Email is required.';
            emailInput.classList.add('is-invalid');
        } else if (!emailPattern.test(emailInput.value.trim())) {
            isValid = false;
            emailError.textContent = 'Please enter a valid email address.';
            emailInput.classList.add('is-invalid');
        }

        // Password validation with specific requirements
        const passwordValue = passwordInput.value.trim();
        const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (passwordValue === '') {
            isValid = false;
            passwordError.textContent = 'Password is required.';
            passwordInput.classList.add('is-invalid');
        } else if (!passwordPattern.test(passwordValue)) {
            isValid = false;
            passwordError.textContent = 'Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, a number, and a special character.';
            passwordInput.classList.add('is-invalid');
        }

        // Confirm Password validation
        if (confirmPasswordInput.value.trim() === '') {
            isValid = false;
            confirmPasswordError.textContent = 'Please confirm your password.';
            confirmPasswordInput.classList.add('is-invalid');
        } else if (passwordInput.value.trim() !== confirmPasswordInput.value.trim()) {
            isValid = false;
            confirmPasswordError.textContent = 'Passwords must match.';
            confirmPasswordInput.classList.add('is-invalid');
        }

        // Prevent form submission if validation fails
        if (!isValid) {
            event.preventDefault();
        }
    });
});
function menutoggl() {
    var innerItems = document.getElementById('inneritems');
    // Toggle visibility
    if (innerItems.style.display === 'block') {
        innerItems.style.display = 'none';
    } else {
        innerItems.style.display = 'block';
    }
}

document.addEventListener('DOMContentLoaded', function() {
    var innerItems = document.getElementById('inneritems');
    // Ensure menu is hidden initially when page loads
    innerItems.style.display = 'none';
});

document.addEventListener('click', function(event) {
    var dropdown = document.querySelector('.dropdown');
    var innerItems = document.getElementById('inneritems');
    // Close menu if clicking outside of the dropdown area
    if (innerItems.style.display === 'block' && !dropdown.contains(event.target)) {
        innerItems.style.display = 'none';
    }
});


function menutoggle() {
    var MenuItems = document.getElementById('MenuItems');
    // Toggle between expanded and collapsed states
    if (MenuItems.style.maxHeight === "0px") {
        MenuItems.style.maxHeight = "250px";
    } else {
        MenuItems.style.maxHeight = "0px";
    }
}

document.addEventListener('DOMContentLoaded', function() {
    var MenuItems = document.getElementById('MenuItems');
    // Ensure the menu items are hidden initially
    MenuItems.style.maxHeight = "0px";
});
function toggleCart() {
    var cartSidebar = document.getElementById('cartSidebar');
    if (cartSidebar.style.right === '0px') {
        cartSidebar.style.right = '-500px';
    } else {
        cartSidebar.style.right = '0px';
    }
}
// JavaScript for OTP validation and auto-focus
document.addEventListener('DOMContentLoaded', () => {
  const otpFields = document.querySelectorAll('.otp-field');
  const otpForm = document.getElementById('otpForm');

  // Move focus to the next field on input
  otpFields.forEach((field, index) => {
    field.addEventListener('input', (e) => {
      const value = e.target.value;
      if (value && !/^\d$/.test(value)) {
        // Clear the field if input is not a digit
        e.target.value = '';
        return;
      }
      if (value && index < otpFields.length - 1) {
        otpFields[index + 1].focus();
      }
    });

    // Move focus to the previous field on backspace if empty
    field.addEventListener('keydown', (e) => {
      if (e.key === 'Backspace' && !field.value && index > 0) {
        otpFields[index - 1].focus();
      }
    });
  });
  otpForm.addEventListener('submit', (e) => {
      // Check if all OTP fields are filled
      otpFields.forEach((field) => {
        if (!field.value) {
          isValid = false;
          field.classList.add('error'); // Add error styling
        } else {
          field.classList.remove('error'); // Remove error styling if field is filled
        }
      });
    });
});