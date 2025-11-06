// Slideshow ảnh
const images = document.querySelectorAll('.right-panel img');
let index = 0;
setInterval(() => {
    images[index].classList.remove('active');
    index = (index + 1) % images.length;
    images[index].classList.add('active');
}, 3000);

// Flatpickr cho ngày sinh
if(document.querySelector("#dob")){
    flatpickr("#dob", {
        altInput: true,
        altFormat: "d F Y",
        dateFormat: "Y-m-d",
    });
}

document.querySelectorAll(".toggle-password").forEach(icon => {
    icon.addEventListener("click", function () {
        const input = this.closest(".password-field").querySelector("input");
        const img = this.querySelector('img');
        if (input.type === "password") {
            input.type = "text";
            img.src = "/images/eye-off.png";
        } else {
            input.type = "password";
            img.src = "/images/eye.png";
        }
        img.style.width = "18px"
    });
});
