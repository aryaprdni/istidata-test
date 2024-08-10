function fetchUserData(nik) {
    $.ajax({
        url: 'http://localhost:8080/api/users/' + nik,
        method: 'GET',
        success: function(response) {
            console.log('Response from server:', response);
            var user = response.data;
            console.log(user)
            if (user) {
                $('#nik').val(user.nik);
                $('#fullName').val(user.nama_Lengkap);
                $('#tanggalLahir').val(user.tanggal_Lahir);
                $('#alamat').val(user.alamat);
                $('#negara').val(user.negara);

                if (user.jenis_Kelamin === 'Laki-laki') {
                    $('#Laki-laki').prop('checked', true);
                } else if (user.jenis_Kelamin === 'Perempuan') {
                    $('#Perempuan').prop('checked', true);
                }

                $('#negara').val(user.negara);
            } else {
                alert('No user data found.');
            }
        },
        error: function(error) {
            console.error('Error fetching user data:', error);
            alert('Failed to fetch user data. Please try again later.');
        }
    });
}

function updateUser() {
    var formData = {
        nik: $('#nik').val(),
        nama_Lengkap: $('#fullName').val().trim(),
        jenis_Kelamin: $('input[name="jenis_Kelamin"]:checked').val(),
        tanggal_Lahir: $('#tanggalLahir').val(),
        alamat: $('#alamat').val().trim(),
        negara: $('#negara').val().trim()
    };

    console.log('Form Data to Update:', formData);

    $.ajax({
        url: 'http://localhost:8080/api/users/' + formData.nik,
        method: 'PATCH',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            alert('Data berhasil diperbarui.');
            window.location.href = 'index.html';
        },
        error: function(error) {
            console.error('Error updating data:', error.responseText);
            alert('Failed to update data. Please try again later.');
        }
    });
}

$(document).ready(function () {
    var urlParams = new URLSearchParams(window.location.search);
    var nik = urlParams.get('nik');

    console.log('NIK from URL:', nik);

    if (nik) {
        fetchUserData(nik);
    }

    $('#updateBtn').click(updateUser);
});
