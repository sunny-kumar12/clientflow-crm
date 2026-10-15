document.addEventListener("DOMContentLoaded", function () {
    const menuToggle = document.getElementById("menuToggle");
    const sidebar = document.querySelector(".sidebar");
    const todayDate = document.getElementById("todayDate");

    // Display today's date using the browser's locale.
    if (todayDate) {
        todayDate.textContent = new Intl.DateTimeFormat("en-IN", {
            day: "numeric",
            month: "short",
            year: "numeric"
        }).format(new Date());
    }

    // Open and close the sidebar on smaller screens.
    if (menuToggle && sidebar) {
        menuToggle.addEventListener("click", function () {
            sidebar.classList.toggle("sidebar-open");
        });
    }
});