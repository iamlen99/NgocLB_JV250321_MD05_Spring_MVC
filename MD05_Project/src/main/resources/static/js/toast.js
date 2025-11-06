document.addEventListener("DOMContentLoaded", function() {
    const toast = document.querySelector(".toast");
    if (toast) {
        toast.classList.add("show");
        setTimeout(() => {
            toast.classList.remove("show");
        }, 3000);
    }
});