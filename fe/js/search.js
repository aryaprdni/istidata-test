function searchUsers() {
    var nik = $('#nik').val();
    var fullName = $('#fullName').val();

    $.ajax({
        url: 'http://localhost:8080/api/users/search',
        method: 'GET',
        data: {
            NIK: nik || undefined,
            Nama_Lengkap: fullName || undefined
        },
        success: function (response) {
            var users = response.data;
            var tableBody = $('#personalDataTableBody');
            tableBody.empty();

            if (users.length === 0) {
                tableBody.append('<tr><td colspan="8" class="text-center">No data found</td></tr>');
            } else {
                users.forEach(function(user, index) {
                    tableBody.append(`
                        <tr>
                            <td>${index + 1}</td> <!-- Nomor urut otomatis -->
                            <td>${user.nik}</td>
                            <td>${user.nama_Lengkap}</td>
                            <td>${user.jenis_Kelamin}</td>
                            <td>${user.tanggal_Lahir}</td>
                            <td>${user.alamat}</td>
                            <td>${user.negara}</td>
                            <td>
                                <button class="btn btn-primary btn-sm" onclick="viewUser(${user.nik})">Detail</button>
                                <button class="btn btn-warning btn-sm" onclick="editUser(${user.nik})">Edit</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.nik})">Delete</button>
                            </td>
                        </tr>
                    `);
                });
            }
        },
        error: function (error) {
            console.error('Error searching users:', error);
            alert('Failed to search users. Please try again later.');
        }
    });
}

$('#searchBtn').click(function () {
    searchUsers();
});