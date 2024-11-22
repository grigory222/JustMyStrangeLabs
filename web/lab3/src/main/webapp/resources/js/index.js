function updateDateTime() {
    const now = new Date();

    // Update the date
    const dateOptions = { year: 'numeric', month: 'long', day: 'numeric' };
    document.getElementById('currentDate').innerText = now.toLocaleDateString(undefined, dateOptions);

    // Update the time
    document.getElementById('currentTime').innerText = now.toLocaleTimeString();
}

// Update every 11 seconds
setInterval(updateDateTime, 11000);
window.onload = updateDateTime;
