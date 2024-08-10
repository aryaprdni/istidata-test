function fetchUserDetail(nik) {
    $.ajax({
        url: 'http://localhost:8080/api/users/' + nik,
        method: 'GET',
        success: function (response) {
            var user = response.data;
            if (user) {
                $('#nik').val(user.nik);
                $('#fullName').val(user.nama_Lengkap);
                $('#jenisKelamin').val(user.jenis_Kelamin);
                $('#tanggalLahir').val(user.tanggal_Lahir);
                $('#alamat').val(user.alamat);
                $('#negara').val(user.negara);
            } else {
                $('#userDetailForm').html('<p>No data found.</p>');
            }
        },
        error: function (error) {
            console.error('Error fetching user detail:', error);
            $('#userDetailForm').html('<p>Failed to fetch user detail. Please try again later.</p>');
        }
    });
}

$(document).ready(function () {
    var urlParams = new URLSearchParams(window.location.search);
    var nik = urlParams.get('nik');
    
    if (nik) {
        fetchUserDetail(nik);
    }
});