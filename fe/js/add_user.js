function addUser() {
    var formData = {
        nik: parseInt($('#nik').val(), 10),
        nama_Lengkap: $('#fullName').val().trim(),
        jenis_Kelamin: $('input[name="jenis_kelamin"]:checked').val(),
        tanggal_Lahir: $('#tanggalLahir').val(),
        alamat: $('#alamat').val().trim(),
        negara: $('#negara').val().trim()
    };

    console.log('Form Data:', formData);

    $.ajax({
        url: 'http://localhost:8080/api/users',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (response) {
            console.log('Success Response:', response);
            alert('Data berhasil ditambahkan.');
            window.location.href = 'index.html';
        },
        error: function (error) {
            console.error('Error adding data:', error.responseText);
            alert('Failed to add data. Please try again later.');
        }
    });
}

$('#saveBtn').click(addUser);