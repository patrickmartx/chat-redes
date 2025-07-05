document.addEventListener('DOMContentLoaded', () => {
    const imagemFileInput = document.getElementById('imagemFile');
    if (imagemFileInput) {
        imagemFileInput.addEventListener('change', previewImage);
    }
});

function previewImage(event) {
    const reader = new FileReader();
    const placeholder = document.getElementById('imagePreviewPlaceholder');

    reader.onload = function() {
        if (placeholder) {
            placeholder.innerHTML = '';
            const img = document.createElement('img');
            img.src = reader.result;
            img.classList.add('img-fluid', 'rounded');
            placeholder.appendChild(img);
        }
    };

    if (event.target.files && event.target.files[0]) {
        reader.readAsDataURL(event.target.files[0]);
    } else if (placeholder) {
        placeholder.innerHTML = '<span>Pré-visualização da Imagem</span>';
    }
}