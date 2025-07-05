document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('imageModal');
    const modalImg = document.getElementById('modalImage');
    const closeButton = document.querySelector('.image-modal-close');
    const mainContent = document.querySelector('body');

    if (mainContent && modal && modalImg) {
        mainContent.addEventListener('click', function(event) {
            if (event.target.matches('.expandable-image, .expandable-image-chat')) {
                modal.style.display = "block";
                modalImg.src = event.target.src;
            }
        });
    }

    if (closeButton) {
        closeButton.onclick = function() {
            if (modal) modal.style.display = "none";
        }
    }

    if (modal) {
        modal.onclick = function(event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        }
    }
});